package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.facade.Facade;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import org.omnifaces.util.Messages;

@Stateless(name = "EjbReincorporacion")
public class EjbReincorporacion {
   @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPersonal ejbPersonal;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbFichaAdmision ejbFA;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    Integer claveMateria;
    
    Integer umcdTotal = 0;
    Integer umceiTotal = 0;
    
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
//---------------------------------------------------------------------------Acceso--------------------------------------------------------------------------//

    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReincorporacion.validarServiciosEscolares)", e, null);
        }
    }

    public ResultadoEJB<EventoEscolar> verificarEvento() {
        try {
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REINCORPORACIONES);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para reinscripción autónoma (EjbReinscripcionAutonoma.).", e, EventoEscolar.class);
        }
    }
//------------------------------------------------------------------------Catalogos CE------------------------------------------------------------------------//

    public ResultadoEJB<List<Estudiante>> getEstudiantes(AreasUniversidad au,EventoEscolar ee) {
        try {
            List<AreasUniversidad> aus = em.createQuery("select a from AreasUniversidad a WHERE a.areaSuperior=:areaSuperior AND a.vigente=:vigente", AreasUniversidad.class).setParameter("areaSuperior", au.getArea()).setParameter("vigente", au.getVigente()).getResultList();
            List<Estudiante> estudiantes = new ArrayList<>();
            if (!aus.isEmpty()) {
                List<Short> clvesA= new ArrayList<>();
                aus.forEach((t) -> {clvesA.add(t.getArea());});
                estudiantes = em.createQuery("select t from Estudiante t WHERE t.tipoEstudiante.idTipoEstudiante=:idTipoEstudiante AND t.carrera in :carrera", Estudiante.class)
                        .setParameter("idTipoEstudiante", Short.parseShort("1"))
                        .setParameter("carrera", clvesA)
                        .getResultList()
                        .stream().filter(t -> t.getPeriodo() == ee.getPeriodo())
                        .collect(Collectors.toList());
            }
            return ResultadoEJB.crearCorrecto(estudiantes, "estudiantes Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los estudiantes(EjbReincorporacion.getEstudiantes).", e, null);
        }
    }
    
    public ResultadoEJB<List<Estudiante>> getEstudiantesReincorporaciones() {
        try {
            List<String> reincorporaciones=new ArrayList<>();
            reincorporaciones.add("Cambio de grupo");
            reincorporaciones.add("Cambio de plan de estudio");
            reincorporaciones.add("Cambio de programa educativo");
            reincorporaciones.add("Equivalencia");
            reincorporaciones.add("Reincorporación otra UT");
            reincorporaciones.add("Reincorporación otra generación");
            reincorporaciones.add("Reincorporación misma UT");
            List<Estudiante> estudiantes = em.createQuery("SELECT t FROM Estudiante t INNER JOIN t.aspirante a INNER JOIN a.idPersona p WHERE t.tipoRegistro in :reincorporaciones GROUP BY t.aspirante ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, t.periodo", Estudiante.class)
                    .setParameter("reincorporaciones", reincorporaciones)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(estudiantes, "estudiantes Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los estudiantes(EjbReincorporacion.getEstudiantes).", e, null);
        }
    }
    
    public ResultadoEJB<List<TipoSangre>> getTiposSangre() {
        try {
            List<TipoSangre> sangres = em.createQuery("select t from TipoSangre t", TipoSangre.class).getResultList();
            return ResultadoEJB.crearCorrecto(sangres, "Tipos de sangre Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Sangre(EjbReincorporacion.getTiposSangre).", e, null);
        }
    }

    public ResultadoEJB<Generaciones> getgeneracion() {
        try {
            LocalDate ld = LocalDate.now();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getgeneracion(anio)" + ld.getYear());
            List<Generaciones> gener = em.createQuery("select t from Generaciones t WHERE t.inicio=:inicio", Generaciones.class)
                    .setParameter("inicio", ld.getYear())
                    .getResultList();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getgeneracion(1)" + gener.size());
            if (gener.isEmpty()) {
                gener = em.createQuery("select t from Generaciones t ORDER BY t.generacion DESC", Generaciones.class)
                        .getResultList();
            }
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getgeneracion(2)" + gener.size());
            Generaciones generacion = gener.get(0);
            return ResultadoEJB.crearCorrecto(generacion, "Tipos de sangre Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar las Generaciones(EjbReincorporacion.getgeneracion).", e, null);
        }
    }

    public ResultadoEJB<List<TipoDiscapacidad>> getTipoDiscapacidad() {
        try {
            List<TipoDiscapacidad> discapacidads = em.createQuery("select t from TipoDiscapacidad t", TipoDiscapacidad.class).getResultList();
            return ResultadoEJB.crearCorrecto(discapacidads, "Tipos de Discapacidad Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Discapacidad(EjbReincorporacion.getTipoDiscapacidad).", e, null);
        }
    }
    
    public ResultadoEJB<List<TipoAspirante>> getTipoAspirante() {
        try {
            List<TipoAspirante> aspirantes = em.createQuery("select t from TipoAspirante t", TipoAspirante.class).getResultList();
            return ResultadoEJB.crearCorrecto(aspirantes, "Tipos de Aspirante Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Aspirante(EjbReincorporacion.getTipoAspirante).", e, null);
        }
    }

    public ResultadoEJB<List<Ocupacion>> getOcupaciones() {
        try {
            List<Ocupacion> ocupacions = em.createQuery("select t from Ocupacion t", Ocupacion.class).getResultList();
            return ResultadoEJB.crearCorrecto(ocupacions, "Tipos de Ocupacion Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Ocupacion(EjbReincorporacion.getOcupaciones).", e, null);
        }
    }

    public ResultadoEJB<List<Escolaridad>> getEscolaridades() {
        try {
            List<Escolaridad> escolaridads = em.createQuery("select t from Escolaridad t", Escolaridad.class).getResultList();
            return ResultadoEJB.crearCorrecto(escolaridads, "Tipos de Escolaridad Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Escolaridad(EjbReincorporacion.getEscolaridades).", e, null);
        }
    }

    public ResultadoEJB<List<LenguaIndigena>> getLenguaIndigena() {
        try {
            List<LenguaIndigena> lenguaIndigenas = em.createQuery("select t from LenguaIndigena t", LenguaIndigena.class).getResultList();
            return ResultadoEJB.crearCorrecto(lenguaIndigenas, "Tipos de Lengua Indigena Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Lengua Indigena(EjbReincorporacion.getLenguaIndigena).", e, null);
        }
    }

    public ResultadoEJB<List<MedioDifusion>> getMedioDifusion() {
        try {
            List<MedioDifusion> difusions = em.createQuery("select t from MedioDifusion t", MedioDifusion.class).getResultList();
            return ResultadoEJB.crearCorrecto(difusions, "Tipos de Medio Difusion Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Medio Difusion(EjbReincorporacion.getMedioDifusion).", e, null);
        }
    }

    public ResultadoEJB<List<EspecialidadCentro>> getEspecialidadCentro() {
        try {
            List<EspecialidadCentro> centros = em.createQuery("select t from EspecialidadCentro t", EspecialidadCentro.class).getResultList();
            return ResultadoEJB.crearCorrecto(centros, "Tipos de Especialidad Centro Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Especialidad Centro(EjbReincorporacion.getEspecialidadCentro).", e, null);
        }
    }

    public ResultadoEJB<List<Sistema>> getSistema() {
        try {
            List<Sistema> sistemas = em.createQuery("select t from Sistema t", Sistema.class).getResultList();
            return ResultadoEJB.crearCorrecto(sistemas, "Tipos de Sistema Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Sistema(EjbReincorporacion.getSistema).", e, null);
        }
    }
    
    public ResultadoEJB<List<TipoEstudiante>> getTiposEstudiante() {
        try {
            List<TipoEstudiante> estudiantes = em.createQuery("select t from TipoEstudiante t", TipoEstudiante.class).getResultList();
            return ResultadoEJB.crearCorrecto(estudiantes, "Tipo Estudiante Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipo Estudiante(EjbReincorporacion.getTiposEstudiante).", e, null);
        }
    }
    
    public ResultadoEJB<List<Grupo>> getGrupos(Short pe,Integer periodo) {
        try {
            List<Grupo> grupos = em.createQuery("select t from Grupo t WHERE t.idPe=:idPe AND t.periodo=:periodo ORDER BY t.generacion, t.literal, t.grado", Grupo.class)
                    .setParameter("idPe", pe).setParameter("periodo", periodo).getResultList();
            return ResultadoEJB.crearCorrecto(grupos, "Grupo Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Grupo(EjbReincorporacion.getGrupos).", e, null);
        }
    }

    public ResultadoEJB<List<Grupo>> getGruposCarrera(Short pe) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getGruposCarrera(x)"+pe);
            List<Grupo> grupos = em.createQuery("select t from Grupo t WHERE t.idPe=:idPe", Grupo.class)
                    .setParameter("idPe", pe).getResultList();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getGruposCarrera(x1)");
            return ResultadoEJB.crearCorrecto(grupos, "Grupo Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Grupo(EjbReincorporacion.getGrupos).", e, null);
        }
    }
//-----------------------------------------------------------------------Catalogos PYE-----------------------------------------------------------------------//
    public ResultadoEJB<List<Pais>> getPais() {
        try {
            List<Pais> paises = em.createQuery("select t from Pais t", Pais.class).getResultList();
            return ResultadoEJB.crearCorrecto(paises, "paises Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los paises (EjbReincorporacion.getPais).", e, null);
        }
    }

    public ResultadoEJB<List<Estado>> getEstado(Pais p) {
        try {
            List<Estado> estados = em.createQuery("select t from Estado t INNER JOIN t.idpais p WHERE p.idpais=:idpais", Estado.class).setParameter("idpais", p.getIdpais()).getResultList();
            return ResultadoEJB.crearCorrecto(estados, "estados Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los estados (EjbReincorporacion.getEstado).", e, null);
        }
    }

    public ResultadoEJB<List<Municipio>> getMunicipio(Integer es) {
        try {
            List<Municipio> municipios = em.createQuery("select t from Municipio t INNER JOIN t.estado e WHERE e.idestado=:idestado", Municipio.class).setParameter("idestado", es).getResultList();
            return ResultadoEJB.crearCorrecto(municipios, "municipios Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los municipios (EjbReincorporacion.getMunicipio).", e, null);
        }
    }

    public ResultadoEJB<List<Localidad>> getLocalidad(Integer es,Integer m) {
        try {
            List<Localidad> localidads = em.createQuery("select t from Localidad t INNER JOIN t.municipio m WHERE m.municipioPK.claveMunicipio=:claveMunicipio AND m.municipioPK.claveEstado=:claveEstado", Localidad.class).setParameter("claveEstado", es).setParameter("claveMunicipio", m).getResultList();
            return ResultadoEJB.crearCorrecto(localidads, "Localidad Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Localidad (EjbReincorporacion.getLocalidad).", e, null);
        }
    }

    public ResultadoEJB<List<Asentamiento>> getAsentamiento(Integer es,Integer m) {
        try {
            List<Asentamiento> asentamientos = em.createQuery("select t from Asentamiento t INNER JOIN t.municipio1 m WHERE m.municipioPK.claveMunicipio=:claveMunicipio AND m.municipioPK.claveEstado=:claveEstado", Asentamiento.class).setParameter("claveEstado", es).setParameter("claveMunicipio", m).getResultList();
            return ResultadoEJB.crearCorrecto(asentamientos, "Asentamientos Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Asentamientos (EjbReincorporacion.getAsentamiento).", e, null);
        }
    }

    public ResultadoEJB<List<Iems>> getIems(Integer es,Integer m,Integer l) {
        try {
            List<Iems> iemses = em.createQuery("select t from Iems t INNER JOIN t.localidad l WHERE l.localidadPK.claveEstado=:claveEstado AND l.localidadPK.claveMunicipio=:claveMunicipio AND l.localidadPK.claveLocalidad=:claveLocalidad", Iems.class).setParameter("claveEstado", es).setParameter("claveMunicipio", m).setParameter("claveLocalidad", l).getResultList();
            return ResultadoEJB.crearCorrecto(iemses, "Iems Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Iems (EjbReincorporacion.getIems).", e, null);
        }
    }

//------------------------------------------------------------------------Catalogos Pr------------------------------------------------------------------------//
    public ResultadoEJB<List<AreasUniversidad>> getAreasUniversidad() {
        try {
            List<AreasUniversidad> areasUniversidads = em.createQuery("select t from AreasUniversidad t INNER JOIN t.categoria c WHERE c.categoria=:categoria", AreasUniversidad.class).setParameter("categoria", Short.parseShort("8")).getResultList();
            return ResultadoEJB.crearCorrecto(areasUniversidads, "Areas Universidads Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar las areas Universidads (EjbReincorporacion.getAreasUniversidad).", e, null);
        }
    }
    
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativos() {
        try {
            List<AreasUniversidad> areasUniversidads = em.createQuery("select t from AreasUniversidad t INNER JOIN t.categoria c WHERE c.categoria=:categoria AND t.vigente=:vigente", AreasUniversidad.class).setParameter("categoria", Short.parseShort("9")).setParameter("vigente", "1").getResultList();
            return ResultadoEJB.crearCorrecto(areasUniversidads, "Areas Universidads Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar las areas Universidads (EjbReincorporacion.getProgramasEducativos).", e, null);
        }
    }
    
    public ResultadoEJB<List<PlanEstudio>> getPlanesEstudio() {
        try {
            List<PlanEstudio> areasUniversidads = em.createQuery("SELECT t FROM PlanEstudio t WHERE t.estatus=:estatus", PlanEstudio.class).setParameter("estatus", true).getResultList();
            return ResultadoEJB.crearCorrecto(areasUniversidads, "Areas Universidads Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar las areas Universidads (EjbReincorporacion.getProgramasEducativos).", e, null);
        }
    }
    
     public ResultadoEJB<List<DtoReincorporacion.PlanesDeEstudioConsulta>> getProgramasEducativosConsulta() {
        try {
            List<DtoReincorporacion.PlanesDeEstudioConsulta> consultas=  new ArrayList<>();
           ResultadoEJB<List<AreasUniversidad>> rejb= getProgramasEducativosReincorporacion("TSU");
           List<Short> aus=new ArrayList<>();
            rejb.getValor().forEach((t) -> {
                aus.add(t.getArea());
            });
            consultas.add(new DtoReincorporacion.PlanesDeEstudioConsulta((0 + "-E"),0, "Nuevo Programa", Short.parseShort("20000"), "E"));
                
            
            List<PlanEstudio> estudios = em.createQuery("select t from PlanEstudio t WHERE t.idPe IN :idPs", PlanEstudio.class).setParameter("idPs", aus).getResultList();
            if (!estudios.isEmpty()) {
                estudios.forEach((t) -> {
                    consultas.add(new DtoReincorporacion.PlanesDeEstudioConsulta((t.getIdPlanEstudio() + "-I"),t.getIdPlanEstudio(), t.getDescripcion(), t.getAnio(), "I"));
                });
            }
            
            List<PlanesEstudioExternos> estudioExternoses = em.createQuery("select t from PlanesEstudioExternos t", PlanesEstudioExternos.class).getResultList();
            if (!estudioExternoses.isEmpty()) {
                estudioExternoses.forEach((t) -> {
                    consultas.add(new DtoReincorporacion.PlanesDeEstudioConsulta((t.getIdplanEstudio()+ "-E"), t.getIdplanEstudio(), t.getNombre(), t.getAnio(), "E"));
                });
            }
            return ResultadoEJB.crearCorrecto(consultas, "PlanesEstudioExternos Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar las PlanesEstudioExternos (EjbReincorporacion.getProgramasEducativosExternos).", e, null);
        }
    }
    
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosReincorporacion(String nivel) {
        try {
            List<AreasUniversidad> areasUniversidads = em.createQuery("select t from AreasUniversidad t INNER JOIN t.categoria c WHERE c.categoria=:categoria AND t.vigente=:vigente", AreasUniversidad.class).setParameter("categoria", Short.parseShort("9")).setParameter("vigente", "1").getResultList();
            List<AreasUniversidad> filtro= new ArrayList<>();            
            if(nivel.equals("TSU")){
                filtro=areasUniversidads.stream().filter(t -> t.getNivelEducativo().getNivel().equals("TSU")).collect(Collectors.toList());
            }else{                
                filtro=areasUniversidads.stream().filter(t -> !t.getNivelEducativo().getNivel().equals("TSU")).collect(Collectors.toList());
            }
            return ResultadoEJB.crearCorrecto(filtro, "Areas Universidads Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar las areas Universidads (EjbReincorporacion.getProgramasEducativos).", e, null);
        }
    }

    public String getAreaSeleccionada(Short area) {
        
            List<AreasUniversidad> areasUniversidads = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area = :area", AreasUniversidad.class).setParameter("area", area).getResultList();
            if(areasUniversidads.isEmpty()){
                return "";
            }else{
                return areasUniversidads.get(0).getNombre();
            }
        
    }

//------------------------------------------------------------------------Busquedas A------------------------------------------------------------------------//
    public ResultadoEJB<DtoReincorporacion.General> getDtoReincorporacion(String curp,Boolean esEscolares) {
        try {
            Boolean encontrado=Boolean.FALSE;
            DtoReincorporacion.PersonaR pr = new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.AspiranteR ar = new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.MedicosR mr = new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.TutorR tr = new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.FamiliaresR fr = new DtoReincorporacion.FamiliaresR(new DatosFamiliares(), tr, new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.DomicilioR dr = new DtoReincorporacion.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.AcademicosR ac = new DtoReincorporacion.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.EncuestaR er = new DtoReincorporacion.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE);
            List<DtoReincorporacion.EstudianteR> es = new ArrayList<>();
            List<DtoReincorporacion.AlineacionCalificaciones> al = new ArrayList<>();
            
            
            ResultadoEJB<DtoReincorporacion.PersonaR> resPR = getPersonaR(curp);
            if (resPR.getCorrecto()) {
                pr = resPR.getValor();
                if (pr.getEcontrado()) {
                    ResultadoEJB<DtoReincorporacion.AspiranteR> resAR = getAspiranteR(pr.getPersona());
                    if (resAR.getCorrecto()) {
                        ar = resAR.getValor();
                        if (ar.getEcontrado()) {
                            ResultadoEJB<DtoReincorporacion.FamiliaresR> resFR = getFamiliaresR(ar.getAspirante());
                            ResultadoEJB<DtoReincorporacion.AcademicosR> resAC = getAcademicosR(ar.getAspirante());
                            ResultadoEJB<DtoReincorporacion.DomicilioR> resDR = getDomicilioR(ar.getAspirante());
                            ResultadoEJB<DtoReincorporacion.EncuestaR> resER = getEncuestaR(ar.getAspirante());
                            ResultadoEJB< List<DtoReincorporacion.EstudianteR>> resES = getEstudianteR(ar.getAspirante());
                            if (resFR.getCorrecto()) {fr = resFR.getValor();
                            if(fr.getEcontrado()){tr=fr.getTutorR();}}
                            if (resAC.getCorrecto()) {ac = resAC.getValor();}
                            if (resDR.getCorrecto()) {dr = resDR.getValor();}
                            if (resER.getCorrecto()) {er = resER.getValor();}  
                            if (resES.getCorrecto()) {
                                es = resES.getValor();
                                if(!es.isEmpty()){
                                    ResultadoEJB<List<DtoReincorporacion.AlineacionCalificaciones>> resAl = getAlineacionCalificaciones(ar.getAspirante(),esEscolares,0);
                                    if (resAl.getCorrecto()) {
                                        al = resAl.getValor();}
                                }
                            }      
                        }
                    }
                    ResultadoEJB<DtoReincorporacion.MedicosR> resMR = getMedicosR(pr.getPersona());
                       
                    if (resMR.getCorrecto()) {mr = resMR.getValor();}
                    encontrado=Boolean.TRUE;
                }
            }                                                                        
            DtoReincorporacion.General rr=new DtoReincorporacion.General(pr, ar, mr, fr, tr, dr, ac, er, es,al, encontrado);
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getDtoReincorporacion).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.PersonaR> getPersonaR(String curp) {
        try {
            List<Persona> personas = em.createQuery("select p from Persona p WHERE p.curp=:curp", Persona.class).setParameter("curp", curp).getResultList();
            DtoReincorporacion.PersonaR rr = new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE);
            if (!personas.isEmpty()) {
                Persona per = personas.get(0);
                List<MedioComunicacion> mcs = em.createQuery("select c from MedioComunicacion c INNER JOIN c.persona1 p WHERE p.idpersona=:idpersona", MedioComunicacion.class).setParameter("idpersona", per.getIdpersona()).getResultList();
                if (!mcs.isEmpty()) {
                    rr.setMedioComunicacion(mcs.get(0));
                    rr.setOperacionMC(Operacion.ACTUALIZAR);
                }
                rr.setPersona(per);
                ResultadoEJB<Pais> pais = getPaisSeleccionado(per.getEstado());
                if (pais.getCorrecto()) {
                    rr.setPaisOr(pais.getValor());
                }
                rr.setOperacionGeneral(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.AspiranteR> getAspiranteR(Persona p) {
        try {
            List<Aspirante> aspirantes = em.createQuery("select a from Aspirante a INNER JOIN a.idPersona p WHERE p.idpersona=:idpersona", Aspirante.class).setParameter("idpersona", p.getIdpersona()).getResultList();
            DtoReincorporacion.AspiranteR rr = new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!aspirantes.isEmpty()) {
                Aspirante aspirante = aspirantes.get(aspirantes.size()-1);
                rr.setAspirante(aspirante);
                rr.setTipo(aspirante.getTipoAspirante());
                rr.setProcesosInscripcion(aspirante.getIdProcesoInscripcion());
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }
            return ResultadoEJB.crearCorrecto(rr, "AspiranteR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AspiranteR (EjbReincorporacion.getAspiranteR).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.AspiranteR> getAspiranteRCalificaciones(Integer estudiante) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getAspiranteRCalificaciones()"+estudiante);
            List<Aspirante> aspirantes = em.createQuery("select a from Estudiante e INNER JOIN e.aspirante a WHERE e.idEstudiante=:idEstudiante", Aspirante.class).setParameter("idEstudiante", estudiante).getResultList();
            DtoReincorporacion.AspiranteR rr = new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE);
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getAspiranteRCalificaciones()"+aspirantes.size());
            if (!aspirantes.isEmpty()) {
                Aspirante aspirante = aspirantes.get(0);
                rr.setAspirante(aspirante);
                rr.setTipo(aspirante.getTipoAspirante());
                rr.setProcesosInscripcion(aspirante.getIdProcesoInscripcion());
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }
            return ResultadoEJB.crearCorrecto(rr, "AspiranteR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AspiranteR (EjbReincorporacion.getAspiranteR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.MedicosR> getMedicosR(Persona p) {
        try {
            List<DatosMedicos> datosMedicoses = em.createQuery("select a from DatosMedicos a INNER JOIN a.persona p WHERE p.idpersona=:idpersona", DatosMedicos.class).setParameter("idpersona", p.getIdpersona()).getResultList();
            DtoReincorporacion.MedicosR rr = new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!datosMedicoses.isEmpty()) {
                DatosMedicos dm = datosMedicoses.get(0);
                rr.setDatosMedicos(dm);
                rr.setDiscapacidad(dm.getCveDiscapacidad());
                rr.setSangre(dm.getCveTipoSangre());
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getMedicosR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.FamiliaresR> getFamiliaresR(Aspirante a) {
        try {
            List<DatosFamiliares> datosFamiliareses = em.createQuery("select a from DatosFamiliares a INNER JOIN a.aspirante1 p WHERE p.idAspirante=:idAspirante", DatosFamiliares.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.TutorR tr = new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.FamiliaresR rr = new DtoReincorporacion.FamiliaresR(new DatosFamiliares(), tr, new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!datosFamiliareses.isEmpty()) {
                DatosFamiliares df = datosFamiliareses.get(0);
                rr.setDatosFamiliares(df);
                rr.setEscolaridadM(df.getEscolaridadMadre());
                rr.setEscolaridadP(df.getEscolaridadPadre());
                rr.setOcupacionM(df.getOcupacionMadre());
                rr.setOcupacionP(df.getOcupacionPadre());
                ResultadoEJB<DtoReincorporacion.TutorR> rejb = getTutorR(df);
                if (rejb.getCorrecto()) {
                    rr.setTutorR(rejb.getValor());
                }
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }

            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getFamiliaresR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.TutorR> getTutorR(DatosFamiliares tutor) {
        try {
            List<TutorFamiliar> familiars = em.createQuery("select t from DatosFamiliares d INNER JOIN d.tutor t INNER JOIN d.aspirante1 a WHERE a.idAspirante=:idAspirante", TutorFamiliar.class).setParameter("idAspirante", tutor.getAspirante()).getResultList();
            DtoReincorporacion.TutorR rr = new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!familiars.isEmpty()) {
                TutorFamiliar tf = familiars.get(0);
                rr.setTutorFamiliar(tf);
                if(null != tf.getEscolaridad()){
                    rr.setEscolaridad(tf.getEscolaridad());
                }
                if(null != tf.getOcupacion()){
                    rr.setOcupacion(tf.getOcupacion());
                }                
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }
            return ResultadoEJB.crearCorrecto(rr, "TutorR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar TutorR (EjbReincorporacion.getTutorR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.DomicilioR> getDomicilioR(Aspirante a) {
        List<Domicilio> domicilios = em.createQuery("select d from Domicilio d INNER JOIN d.aspirante1 a WHERE a.idAspirante=:idAspirante", Domicilio.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
        DtoReincorporacion.DomicilioR rr = new DtoReincorporacion.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE);
        if (!domicilios.isEmpty()) {
            Domicilio dom = domicilios.get(0);
            rr.setDomicilio(dom);
            if (dom.getAsentamientoProcedencia() == dom.getIdAsentamiento()) {
                rr.setIgualD(Boolean.TRUE);
            }
            rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
        }
        try {
            return ResultadoEJB.crearCorrecto(rr, "DomicilioR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DomicilioR (EjbReincorporacion.getDomicilioR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.AcademicosR> getAcademicosR(Aspirante a) {
        try {
            List<DatosAcademicos> das = em.createQuery("select d from DatosAcademicos d INNER JOIN d.aspirante1 a WHERE a.idAspirante=:idAspirante", DatosAcademicos.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.AcademicosR rr = new DtoReincorporacion.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!das.isEmpty()) {
                DatosAcademicos da = das.get(0);
                rr.setAcademicos(da);
                rr.setEspecialidad(da.getEspecialidadIems());
                rr.setSistemaPo(da.getSistemaPrimeraOpcion());
                rr.setSistemaSo(da.getSistemaSegundaOpcion());
                rr.setUniversidad1(getOpcionArea(da.getPrimeraOpcion()).getValor());
                rr.setUniversidad2(getOpcionArea(da.getSegundaOpcion()).getValor());
                ResultadoEJB<Iems> rejb = getIemsSeleccionada(da.getInstitucionAcademica());
                if (rejb.getCorrecto()) {
                    Iems i = rejb.getValor();
                    rr.setIems(i);
                    rr.setEstado(i.getLocalidad().getMunicipio().getEstado());
                    rr.setMunicipio(i.getLocalidad().getMunicipio());
                    rr.setLocalidad(i.getLocalidad());
                }
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getAcademicosR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.EncuestaR> getEncuestaR(Aspirante a) {
        try {
            DtoReincorporacion.EncuestaR rr = new DtoReincorporacion.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE);
            List<EncuestaAspirante> encuestaAspirantes = em.createQuery("select e from EncuestaAspirante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", EncuestaAspirante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            if(!encuestaAspirantes.isEmpty()) {
                EncuestaAspirante ea = encuestaAspirantes.get(0);
                rr.setEncuestaAspirante(ea);
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }                        
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getEncuestaR).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.VocacionalR> getEncuestaRVocacional(Persona a) {
        try {
            DtoReincorporacion.VocacionalR rr = new DtoReincorporacion.VocacionalR(new EncuestaVocacional(),new AreasUniversidad(), Operacion.PERSISTIR, Boolean.FALSE);
            List<EncuestaVocacional> encuestaVocacionals = em.createQuery("select e from EncuestaVocacional e INNER JOIN e.persona a WHERE a.idpersona=:idpersona", EncuestaVocacional.class).setParameter("idpersona", a.getIdpersona()).getResultList();
            if(!encuestaVocacionals.isEmpty()) {
                EncuestaVocacional ea = encuestaVocacionals.get(0);
                rr.setEncuestaAspirante(ea);
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
            }                        
            return ResultadoEJB.crearCorrecto(rr, "VocacionalR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar VocacionalR (EjbReincorporacion.getEncuestaRVocacional).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.EstudianteR>> getEstudianteR(Aspirante a){
        try {
            List<Estudiante> das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante ORDER BY e.periodo", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            List<DtoReincorporacion.EstudianteR> ers = new ArrayList<>();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getEstudianteR()"+das.size());
            if (!das.isEmpty()) {
                Estudiante estudiante=das.get(das.size()-1);
                das.forEach((e) -> {
                    List<Documentosentregadosestudiante> ds = em.createQuery("select d from Documentosentregadosestudiante d INNER JOIN d.estudiante1 a WHERE a.idEstudiante=:idEstudiante ORDER BY a.periodo", Documentosentregadosestudiante.class).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
                    Documentosentregadosestudiante d=new Documentosentregadosestudiante();
                    if (!ds.isEmpty()) {
                        d=ds.get(0);
                    }
                    Boolean editable;
                    if(e.getPeriodo()==estudiante.getPeriodo()){
                        editable=Boolean.TRUE;
                    }else{                        
                        editable=Boolean.FALSE;
                    }
                    ers.add(new DtoReincorporacion.EstudianteR(e, d, getAreaSeleccionada(e.getCarrera()), e.getTipoEstudiante(), e.getGrupo(), e.getOpcionIncripcion(), editable, Operacion.ACTUALIZAR));
                });
            }
            return ResultadoEJB.crearCorrecto(ers, "EstudianteR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar EstudianteR (EjbReincorporacion.getEstudianteR).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.AlineacionCalificaciones>> getAlineacionCalificaciones(Aspirante a, Boolean esEscolares,Integer matricula) {
        try {
            List<Estudiante> das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante AND e.matricula=:matricula ORDER BY e.grupo.grado", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("matricula", matricula).getResultList();
            
            List<DtoReincorporacion.AlineacionCalificaciones> rr = new ArrayList<>();
            if (!das.isEmpty()) {
                das.forEach((t) -> {
                    if (!t.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("2")) && !t.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("3"))) {
                        List<DtoReincorporacion.CalificacionesR> crs = new ArrayList<>();
                        Boolean validar = Boolean.FALSE;
                        List<CargaAcademica> cas = em.createQuery("select c from CargaAcademica c INNER JOIN c.cveGrupo g WHERE g.idGrupo=:idGrupo", CargaAcademica.class).setParameter("idGrupo", t.getGrupo().getIdGrupo()).getResultList();
                        if (!cas.isEmpty()) {
                            cas.forEach((cg) -> {
                                List<CalificacionPromedio> cps = em.createQuery("select d from CalificacionPromedio d INNER JOIN d.estudiante a INNER JOIN d.cargaAcademica c WHERE a.idEstudiante=:idEstudiante AND c.carga=:carga", CalificacionPromedio.class).setParameter("idEstudiante", t.getIdEstudiante()).setParameter("carga", cg.getCarga()).getResultList();
                                List<CalificacionNivelacion> cns = em.createQuery("select d from CalificacionNivelacion d INNER JOIN d.estudiante a INNER JOIN d.cargaAcademica c WHERE a.idEstudiante=:idEstudiante AND c.carga=:carga", CalificacionNivelacion.class).setParameter("idEstudiante", t.getIdEstudiante()).setParameter("carga", cg.getCarga()).getResultList();
                                CalificacionPromedio cp = new CalificacionPromedio();
                                CalificacionNivelacion cn = new CalificacionNivelacion();
                                Personal p = new Personal();
                                try {
                                    p = ejbPersonal.mostrarPersonalLogeado(cg.getDocente());
                                } catch (Throwable ex) {
                                    Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                                }
                                Boolean editable = Boolean.FALSE;
                                Boolean ordinaria = Boolean.TRUE;
                              
                                if (!cns.isEmpty()) {
                                    cn=cns.get(0);
                                    cp=cps.get(0);
                                    cp.setValor(cn.getValor());
                                    ordinaria = Boolean.FALSE;
                                } else if(!cps.isEmpty()){
                                    cp=cps.get(0);
                                    ordinaria = Boolean.TRUE;                                    
                                }
                                if(t.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("5"))){editable = Boolean.TRUE;}
                                
                                if (cps.isEmpty()) {
                                    crs.add(new DtoReincorporacion.CalificacionesR(cg, cg.getIdPlanMateria(), cg.getIdPlanMateria().getIdMateria(), p, cp, editable, ordinaria, Operacion.PERSISTIR, Boolean.FALSE));
                                }else{
                                    crs.add(new DtoReincorporacion.CalificacionesR(cg, cg.getIdPlanMateria(), cg.getIdPlanMateria().getIdMateria(), p, cp, editable, ordinaria, Operacion.ACTUALIZAR, Boolean.FALSE));
                                }
                            });
                        } else {
                            if (t.getGrupo().getGrado() == 6 || t.getGrupo().getGrado() == 11) {
                                if (t.getSeguimientoEstadiaEstudianteList().isEmpty()) {
                                    List<SeguimientoEstadiaEstudiante> e = t.getSeguimientoEstadiaEstudianteList().stream().filter(s -> s.getEvento().getGeneracion() == t.getGrupo().getGeneracion()).collect(Collectors.toList());
                                    if (!e.isEmpty()) {
                                        SeguimientoEstadiaEstudiante see = e.get(0);
                                        Personal p = new Personal();
                                        try {
                                            p = ejbPersonal.mostrarPersonalLogeado(see.getAsesor().getPersonal());
                                        } catch (Throwable ex) {
                                            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                                        }
                                        crs.add(new DtoReincorporacion.CalificacionesR(new CargaAcademica(), new PlanEstudioMateria(), new Materia(0, "Periodo de estadia", true), p , new CalificacionPromedio(), Boolean.FALSE, Boolean.TRUE, Operacion.ACTUALIZAR, Boolean.FALSE));
                                    }
                                }
                            }
                        }
                        if (crs.stream().filter(cal -> cal.getCalificacionPromedio().getValor() < 8D).collect(Collectors.toList()).isEmpty()) {
                            validar = Boolean.TRUE;
                        } else {
                            validar = Boolean.FALSE;
                        }
                        rr.add(new DtoReincorporacion.AlineacionCalificaciones(t, t.getGrupo(), crs, validar, Operacion.ACTUALIZAR, Boolean.TRUE));
                    } else {
                        List<DtoReincorporacion.CalificacionesR> crs = new ArrayList<>();
                        Personal p = new Personal();
                        p.setClave(0);
                        p.setNombre("Baja");
                        crs.add(new DtoReincorporacion.CalificacionesR(new CargaAcademica(), new PlanEstudioMateria(), new Materia(0, t.getTipoEstudiante().getDescripcion(), true), p, new CalificacionPromedio(), Boolean.FALSE, Boolean.TRUE, Operacion.ACTUALIZAR, Boolean.FALSE));
                        
                        rr.add(new DtoReincorporacion.AlineacionCalificaciones(t, t.getGrupo(), crs, Boolean.TRUE, Operacion.ACTUALIZAR, Boolean.TRUE));
                    }
                });
            }
            return ResultadoEJB.crearCorrecto(rr, "AlineacionCalificaciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AlineacionCalificaciones (EjbReincorporacion.getAlineacionCalificaciones).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.CalificacionesR>> getCalificaciones(Aspirante a,Integer matricula,String nivel) {
        try {
//            System.out.println("nivel= "+nivel+" matricula= "+matricula+" Aspirante= "+a.getIdAspirante());
            List<Estudiante> das = new ArrayList<>();
            if (nivel.equals("TSU")) {
                das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante AND e.matricula=:matricula AND e.grupo.grado <=:grado ORDER BY e.grupo.grado", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("matricula", matricula).setParameter("grado", 6).getResultList();
            } else {
                das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante AND e.matricula=:matricula AND e.grupo.grado >=:grado ORDER BY e.grupo.grado", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("matricula", matricula).setParameter("grado", 7).getResultList();
            }
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getCalificaciones()"+das.size());
            List<DtoReincorporacion.CalificacionesR> rr = new ArrayList<>();
            List<Integer> idEstudiantes= new ArrayList<>();
//            System.out.println("das "+das.size());
            if (!das.isEmpty()) {
                das.forEach((t) -> {
                    idEstudiantes.add(t.getIdEstudiante());
                });
                List<CalificacionPromedio> cps = em.createQuery("select p from CalificacionPromedio p INNER JOIN p.estudiante e WHERE e.idEstudiante IN :idEstudiantes", CalificacionPromedio.class).setParameter("idEstudiantes", idEstudiantes).getResultList();
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getCalificaciones()"+cps.size());
//                System.out.println("cps" + cps.size());
                if (!cps.isEmpty()) {
                    cps.forEach((t) -> {
                        rr.add(new DtoReincorporacion.CalificacionesR(t.getCargaAcademica(), t.getCargaAcademica().getIdPlanMateria(), t.getCargaAcademica().getIdPlanMateria().getIdMateria(), new Personal(), t, Boolean.TRUE, Boolean.TRUE, Operacion.DETACHAR, Boolean.TRUE));
                    });
                }
            }
            return ResultadoEJB.crearCorrecto(rr, "AlineacionCalificaciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AlineacionCalificaciones (EjbReincorporacion.getAlineacionCalificaciones).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.HistorialTsu> getCalificacionesHistoricas(Aspirante a,Integer matricula) {
        try {
            List<String> tiposR = new ArrayList<>();
            tiposR.add("Reincorporación otra generación");
            tiposR.add("Reincorporación otra UT");
            tiposR.add("Reincorporación misma UT");
            
//            System.out.println("nivel=  matricula= "+matricula+" Aspirante= "+a.getIdAspirante());
            List<Estudiante> das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante AND e.matricula=:matricula ORDER BY e.grupo.grado", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("matricula", matricula).getResultList();
            
            List<Estudiante> estr = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante AND e.matricula=:matricula AND e.tipoRegistro IN :tiposR ORDER BY e.grupo.grado", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("matricula", matricula).setParameter("tiposR", tiposR).getResultList();
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getCalificaciones()"+das.size());
            List<DtoReincorporacion.HistorialTsu> rr = new ArrayList<>();
            DtoReincorporacion.HistorialTsu ht = new DtoReincorporacion.HistorialTsu(a.getIdPersona(), a, new Estudiante(), new UniversidadEgresoAspirante(), Boolean.FALSE, new EstudianteHistorialTsu(), new PlanesEstudioExternos(), new ArrayList<>(), new ArrayList<>(),Boolean.FALSE);
            List<Integer> idEstudiantes= new ArrayList<>();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getCalificacionesHistoricas(estr)"+estr.size());
            if(!estr.isEmpty()){
                ht.setEstudiante(estr.get(0));
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getCalificacionesHistoricas()"+ht.getEstudiante());
            }
            if (!das.isEmpty()) {
                das.forEach((t) -> {
                    idEstudiantes.add(t.getIdEstudiante());
                });
                
                List<EstudianteHistorialTsu> cps = em.createQuery("select p from EstudianteHistorialTsu p INNER JOIN p.idEstudiante e WHERE e.idEstudiante IN :idEstudiantes", EstudianteHistorialTsu.class).setParameter("idEstudiantes", idEstudiantes).getResultList();
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getCalificaciones()"+cps.size());
                if (!cps.isEmpty()) {
                    cps.forEach((t) -> {
                        List<UniversidadEgresoAspirante> ueas = em.createQuery("select p from UniversidadEgresoAspirante p INNER JOIN p.aspirante1 a WHERE a.idAspirante=:idAspirante", UniversidadEgresoAspirante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
                        UniversidadEgresoAspirante uea = new UniversidadEgresoAspirante();
                        Boolean enocntrado= Boolean.FALSE;
                        if (!ueas.isEmpty()) {
                            uea = ueas.get(0);
                            enocntrado= Boolean.TRUE;
                        }
                        rr.add(new DtoReincorporacion.HistorialTsu(a.getIdPersona(), a, t.getIdEstudiante(), uea,enocntrado, t, new PlanesEstudioExternos(), t.getCalificacionesHistorialTsuOtrosPe(), t.getCalificacionesHistorialTsuList(),Boolean.TRUE));
                    });
                    ht=rr.get(0);
                }
            }
            return ResultadoEJB.crearCorrecto(ht, "AlineacionCalificaciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AlineacionCalificaciones (EjbReincorporacion.getAlineacionCalificaciones).", e, null);
        }
    }
// busquedas especiales

    public ResultadoEJB<Pais> getPaisSeleccionado(Integer clave) {
        try {
            List<Pais> paises = em.createQuery("select p from Estado e INNER JOIN e.idpais p WHERE e.idestado=:idestado", Pais.class).setParameter("idestado", clave).getResultList();
            Pais paise = new Pais();
            if (!paises.isEmpty()) {
                paise = paises.get(0);
            }
            return ResultadoEJB.crearCorrecto(paise, "paises Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los paises (EjbReincorporacion.getPais).", e, null);
        }
    }
    
    public ResultadoEJB<List<CalificacionesHistorialTsu>> getCalificacionestsuotrasaiiut(DtoReincorporacion.HistorialTsu clave) {
        try {
            List<CalificacionesHistorialTsu> paises = em.createQuery("select e from CalificacionesHistorialTsu e INNER JOIN e.estudianteHistoricoTSU p WHERE p.estudianteHistoricoTSU=:estudianteHistoricoTSU", CalificacionesHistorialTsu.class).setParameter("estudianteHistoricoTSU", clave.getHistorialTsu().getEstudianteHistoricoTSU()).getResultList();
            return ResultadoEJB.crearCorrecto(paises, "paises Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los paises (EjbReincorporacion.getPais).", e, null);
        }
    }
    
    public ResultadoEJB<List<CalificacionesHistorialTsuOtrosPe>> getCalificacionesHistorialTsu(DtoReincorporacion.HistorialTsu clave) {
        try {
            List<CalificacionesHistorialTsuOtrosPe> paises = em.createQuery("select e from CalificacionesHistorialTsuOtrosPe e INNER JOIN e.estudianteHistoricoTSU p WHERE p.estudianteHistoricoTSU=:estudianteHistoricoTSU", CalificacionesHistorialTsuOtrosPe.class).setParameter("estudianteHistoricoTSU", clave.getHistorialTsu().getEstudianteHistoricoTSU()).getResultList();
            return ResultadoEJB.crearCorrecto(paises, "paises Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los paises (EjbReincorporacion.getPais).", e, null);
        }
    }

    public ResultadoEJB<AreasUniversidad> getOpcionArea(Short clave) {
        try {
            AreasUniversidad areasS = ejbAreasLogeo.mostrarAreasUniversidad(clave);
            AreasUniversidad areasO = ejbAreasLogeo.mostrarAreasUniversidad(areasS.getAreaSuperior());

            return ResultadoEJB.crearCorrecto(areasO, "Area Encontrados");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el Area (EjbReincorporacion.getOpcionArea).", e, null);
        } catch (Throwable ex) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el Area (EjbReincorporacion.getOpcionArea).", ex, null);
        }
    }

    public ResultadoEJB<Iems> getIemsSeleccionada(Integer iems) {
        try {
            List<Iems> iemses = em.createQuery("select t from Iems t WHERE t.iems=:iems", Iems.class).setParameter("iems", iems).getResultList();
            Iems iem=new Iems();
            if(!iemses.isEmpty()){
                iem=iemses.get(0);                
            }
            return ResultadoEJB.crearCorrecto(iem, "Iems Encontrada");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar la Iems (EjbReincorporacion.getIemsSeleccionada).", e, null);
        }
    }
    
    public ResultadoEJB<ProcesosInscripcion> getProcesosInscripcionActivo() {
        try {
            List<ProcesosInscripcion> pis = em.createQuery("select p from ProcesosInscripcion p WHERE (:fecha BETWEEN p.fechaInicio AND p.fechaFin)", ProcesosInscripcion.class).setParameter("fecha", new Date()).getResultList().stream().filter(t -> t.getActivoRe() == Boolean.TRUE).collect(Collectors.toList());;
            ProcesosInscripcion pi = new ProcesosInscripcion();
            pi = pis.get(0);
            return ResultadoEJB.crearCorrecto(pi, "Iems Encontrada");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar la Iems (EjbReincorporacion.getIemsSeleccionada).", e, null);
        }
    }

//  Registro de informacion
    public ResultadoEJB<DtoReincorporacion.PersonaR> operacionesPersonaR(DtoReincorporacion.PersonaR rr) {
        try {
            switch (rr.getOperacionGeneral()) {
                case PERSISTIR: em.persist(rr.getPersona());    f.setEntityClass(Persona.class); f.flush(); rr.setOperacionGeneral(Operacion.ACTUALIZAR);break;
                case ACTUALIZAR: em.merge(rr.getPersona());  f.setEntityClass(Persona.class); f.flush();  break;
            }
            switch (rr.getOperacionMC()) {
                case PERSISTIR:
                    rr.getMedioComunicacion().setPersona1(new Persona());
                    rr.getMedioComunicacion().setPersona1(rr.getPersona());
                    rr.getMedioComunicacion().setPersona(rr.getPersona().getIdpersona());
                    em.persist(rr.getMedioComunicacion()); f.setEntityClass(MedioComunicacion.class); f.flush(); rr.setOperacionMC(Operacion.ACTUALIZAR);break;
                case ACTUALIZAR: em.merge(rr.getMedioComunicacion());  f.setEntityClass(MedioComunicacion.class); f.flush(); break;
            }

            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesPersonaR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.AspiranteR> operacionesAspiranteR(DtoReincorporacion.AspiranteR rr,Persona pr) {
        try {
            ResultadoEJB<ProcesosInscripcion> rejb = getProcesosInscripcionActivo();
            rr.setProcesosInscripcion(rejb.getValor());
            rr.getAspirante().setTipoAspirante(new TipoAspirante());
            rr.getAspirante().setTipoAspirante(rr.getTipo());
            rr.getAspirante().setEstatus(Boolean.TRUE);
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getAspirante().setIdPersona(new Persona());
                    rr.getAspirante().setIdProcesoInscripcion(new ProcesosInscripcion());
                    rr.getAspirante().setIdPersona(pr);
                    rr.getAspirante().setIdProcesoInscripcion(rr.getProcesosInscripcion());
                    rr.getAspirante().setFechaRegistro(new Date());
                    em.persist(rr.getAspirante()); f.setEntityClass(Aspirante.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(rr.getAspirante());  f.setEntityClass(Aspirante.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesAspiranteR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.MedicosR> operacionesMedicosR(DtoReincorporacion.MedicosR rr,Persona p) {
        try {
            rr.getDatosMedicos().setCveTipoSangre(new TipoSangre());
            rr.getDatosMedicos().setCveDiscapacidad(new TipoDiscapacidad());
            rr.getDatosMedicos().setCveTipoSangre(rr.getSangre());
            rr.getDatosMedicos().setCveDiscapacidad(rr.getDiscapacidad());
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getDatosMedicos().setPersona(new Persona());                    
                    rr.getDatosMedicos().setPersona(p);
                    rr.getDatosMedicos().setCvePersona(p.getIdpersona());                    
                    em.persist(rr.getDatosMedicos()); f.setEntityClass(DatosMedicos.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(rr.getDatosMedicos());  f.setEntityClass(DatosMedicos.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesMedicosR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.TutorR> operacionesTutorR(DtoReincorporacion.TutorR rr) {
        try {
            rr.getTutorFamiliar().setEscolaridad(new Escolaridad());
            rr.getTutorFamiliar().setOcupacion(new Ocupacion());
            rr.getTutorFamiliar().setEscolaridad(rr.getEscolaridad());
            rr.getTutorFamiliar().setOcupacion(rr.getOcupacion());
            switch (rr.getOperacion()) {
                case PERSISTIR:  em.persist(rr.getTutorFamiliar());    f.setEntityClass(TutorFamiliar.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR);break;
                case ACTUALIZAR: em.merge(rr.getTutorFamiliar());  f.setEntityClass(TutorFamiliar.class); f.flush();  break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesTutorR).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.FamiliaresR> operacionesFamiliaresR(DtoReincorporacion.FamiliaresR rr,Aspirante a) {
        try {
                    rr.getDatosFamiliares().setEscolaridadMadre(new Escolaridad());
                    rr.getDatosFamiliares().setOcupacionMadre(new Ocupacion());
                    rr.getDatosFamiliares().setEscolaridadPadre(new Escolaridad());
                    rr.getDatosFamiliares().setOcupacionPadre(new Ocupacion());
                    rr.getDatosFamiliares().setEscolaridadMadre(rr.getEscolaridadM());
                    rr.getDatosFamiliares().setOcupacionMadre(rr.getOcupacionM());
                    rr.getDatosFamiliares().setEscolaridadPadre(rr.getEscolaridadP());
                    rr.getDatosFamiliares().setOcupacionPadre(rr.getOcupacionP());
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getDatosFamiliares().setAspirante1(new Aspirante());
                    rr.getDatosFamiliares().setTutor(new TutorFamiliar());                    
                    rr.getDatosFamiliares().setAspirante1(a);
                    rr.getDatosFamiliares().setAspirante(a.getIdAspirante());
                    rr.getDatosFamiliares().setTutor(rr.getTutorR().getTutorFamiliar());                    
                    em.persist(rr.getDatosFamiliares()); f.setEntityClass(DatosFamiliares.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(rr.getDatosFamiliares());  f.setEntityClass(DatosFamiliares.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesFamiliaresR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.DomicilioR> operacionesDomicilioR(DtoReincorporacion.DomicilioR rr,Aspirante a) {
        try {
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getDomicilio().setAspirante1(new Aspirante());                    
                    rr.getDomicilio().setAspirante1(a);                               
                    rr.getDomicilio().setAspirante(a.getIdAspirante());                    
                    em.persist(rr.getDomicilio()); f.setEntityClass(Domicilio.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(rr.getDomicilio());  f.setEntityClass(Domicilio.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesDomicilioR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.AcademicosR> operacionesAcademicosR(DtoReincorporacion.AcademicosR rr,Aspirante a) {
        try {
                    rr.getAcademicos().setSistemaPrimeraOpcion(new Sistema());   
                    rr.getAcademicos().setSistemaSegundaOpcion(new Sistema());  
                    rr.getAcademicos().setEspecialidadIems(new EspecialidadCentro());  
                    rr.getAcademicos().setSistemaPrimeraOpcion(rr.getSistemaPo());   
                    rr.getAcademicos().setSistemaSegundaOpcion(rr.getSistemaSo());  
                    rr.getAcademicos().setEspecialidadIems(rr.getEspecialidad());  
                    rr.getAcademicos().setInstitucionAcademica(rr.getIems().getIems()); 
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getAcademicos().setAspirante1(new Aspirante());      
                    
                    rr.getAcademicos().setAspirante(a.getIdAspirante());    
                    rr.getAcademicos().setAspirante1(a);    
                    
                    em.persist(rr.getAcademicos()); f.setEntityClass(DatosAcademicos.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(rr.getAcademicos());  f.setEntityClass(DatosAcademicos.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesAcademicosR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.EncuestaR> operacionesEncuestaR(Aspirante a,String valor, Integer numP) {
        try {
            Operacion operacion;
            List<EncuestaAspirante> es = em.createQuery("select e from EncuestaAspirante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", EncuestaAspirante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            EncuestaAspirante e = new EncuestaAspirante();
            if (!es.isEmpty()) {
                e = es.get(0);
                operacion=Operacion.ACTUALIZAR;
            } else {
                e.setAspirante(new Aspirante());                
                e.setAspirante(a);
                e.setCveAspirante(a.getIdAspirante());
                operacion=Operacion.PERSISTIR;
            }
            switch (numP) {
                case 1:
                    e.setR1Lenguaindigena(valor);
                    e.setR2tipoLenguaIndigena(new LenguaIndigena());
                    e.setR2tipoLenguaIndigena(null);
                    break;
                case 2:
                    e.setR2tipoLenguaIndigena(new LenguaIndigena());
                    e.setR2tipoLenguaIndigena(null);
                    if (e.getR1Lenguaindigena().equals("SI")) {
                        LenguaIndigena li = em.find(LenguaIndigena.class, Short.parseShort(valor));
                        e.setR2tipoLenguaIndigena(li);
                    }
                    break;
                case 3:                    e.setR3comunidadIndigena(valor);                    break;
                case 4:                    e.setR4programaBienestar(valor);                    break;
                case 5:                    e.setR5ingresoMensual(Double.parseDouble(valor));                    break;
                case 6:                    e.setR6dependesEconomicamnete(valor);                    break;
                case 7:                    e.setR7ingresoFamiliar(Double.parseDouble(valor));                    break;
                case 8:                    e.setR8primerEstudiar(valor);                    break;
                case 9:                    e.setR9nivelMaximoEstudios(valor);                    break;
                case 10:                    e.setR10numeroDependientes(Short.parseShort(valor));                    break;
                case 11:                    e.setR11situacionEconomica(valor);                    break;
                case 12:                    e.setR12hijoPemex(valor);                    break;
                case 13:                    e.setR13utxjPrimeraOpcion(valor);                    break;
                case 14:                    e.setR14examenAdmisionOU(valor);                    break;
                case 15:
                    e.setR15medioImpacto(new MedioDifusion());
                    MedioDifusion li = em.find(MedioDifusion.class, Short.parseShort(valor));
                    e.setR15medioImpacto(li);
                    break;
                case 16:                    e.setR16segundaCarrera(valor);                    break;
                case 17:                    e.setR17Alergia(valor);                    break;
                case 18:                    e.setR18padecesEnfermedad(valor);                    break;
                case 19:                    e.setR19tratamientoMedico(valor);                    break;
                case 20:                    e.setR20Hijos(valor);                    break;
                case 21:                    e.setR21noHijos(valor);                    break;
                case 25:                    e.setR25enfermedadCual(valor);                    break;
            }
            
            switch (operacion) {
                case PERSISTIR:
                    em.persist(e);
                    em.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(e);
                    em.flush();
                    break;
            }
            ResultadoEJB<DtoReincorporacion.EncuestaR> b= getEncuestaR(a);
            return ResultadoEJB.crearCorrecto(b.getValor(), "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesEncuestaR).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.VocacionalR> operacionesEncuestaVocacional(Persona p,String valor, Integer numP) {
        try {
            Operacion operacion;
            List<EncuestaVocacional> es = em.createQuery("select e from EncuestaVocacional e INNER JOIN e.persona a WHERE a.idpersona=:idpersona", EncuestaVocacional.class).setParameter("idpersona", p.getIdpersona()).getResultList();
            EncuestaVocacional e = new EncuestaVocacional();
            if (!es.isEmpty()) {
                e = es.get(0);
                operacion=Operacion.ACTUALIZAR;
            } else {
                e.setPersona(new Persona());                
                e.setPersona(p);
                e.setIdPersona(p.getIdpersona());
                operacion=Operacion.PERSISTIR;
            }
            switch (numP) {
                case 1:                    e.setR1(valor);                    break;
                case 2:                    e.setR2(valor);                    break;
                case 5:                    e.setR5(valor);                    break;
            }            
            switch (operacion) {
                case PERSISTIR:
                    em.persist(e);
                    em.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(e);
                    em.flush();
                    break;
            }
            ResultadoEJB<DtoReincorporacion.VocacionalR> b= getEncuestaRVocacional(p);
            return ResultadoEJB.crearCorrecto(b.getValor(), "VocacionalR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar VocacionalR (EjbReincorporacion.operacionesEncuestaVocacional).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.EstudianteR> operacionesEstudianteRegistrados(DtoReincorporacion.EstudianteR rr) {
        try {
            Grupo gactual=rr.getEstudiante().getGrupo();                    
            if (!Objects.equals(gactual.getIdGrupo(), rr.getGrupo().getIdGrupo())) {              
                asistenciasRegistradas(rr.getEstudiante(), Operacion.ELIMINAR);   
                calificacionesRegistradas(rr.getEstudiante());
//                metodo para eliminar Asistencias a tutorias y asesorias
                rr.getEstudiante().setGrupo(new Grupo());
                rr.getEstudiante().setGrupo(rr.getGrupo());              
                asistenciasRegistradas(rr.getEstudiante(), Operacion.PERSISTIR);              
            }                          
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    em.persist(rr.getEstudiante());
                    f.setEntityClass(Estudiante.class);
                    f.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(rr.getEstudiante());
                    f.setEntityClass(Estudiante.class);
                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesEstudianteRegistrados).", e, null);
        }
    }
    
    public Boolean asistenciasRegistradas(Estudiante e, Operacion o) {      
        List<Asistenciasacademicas> listAs = em.createQuery("select asic from Asistenciasacademicas asic INNER JOIN asic.cargaAcademica c INNER JOIN c.cveGrupo g WHERE g.idGrupo=:idGrupo", Asistenciasacademicas.class).setParameter("idGrupo", e.getGrupo().getIdGrupo()).getResultList();      
        Boolean res = false;      
        if (!listAs.isEmpty()) {
            switch (o) {
                case PERSISTIR:
                    List<Asistenciasacademicas> listAsEsNR=em.createQuery("select a from Asistenciasacademicas a INNER JOIN a.cargaAcademica c INNER JOIN c.cveGrupo g WHERE g.idGrupo=:idGrupo GROUP BY a.asistencia.asistencia, c.carga", Asistenciasacademicas.class).setParameter("idGrupo", e.getGrupo().getIdGrupo()).getResultList();                  
                    if (!listAsEsNR.isEmpty()) {
                        listAsEsNR.forEach((t) -> {
                            Asistenciasacademicas a = new Asistenciasacademicas();
                            a.setAsistencia(new Asistencias());
                            a.setEstudiante(new Estudiante());
                            a.setCargaAcademica(new CargaAcademica());
                            
                            a.setAsistencia(t.getAsistencia());
                            a.setEstudiante(e);
                            a.setCargaAcademica(t.getCargaAcademica());
                            
                            a.setTipoAsistenciaA("Falta");
                            
                            em.persist(a);
                            em.flush();                            
                        });
                    }                    
                    f.setEntityClass(Asistenciasacademicas.class);
                    f.flush();
                    break;
                case ELIMINAR:
                    List<Asistenciasacademicas> listAsEs=listAs.stream().filter(t-> Objects.equals(t.getEstudiante().getIdEstudiante(), e.getIdEstudiante())).collect(Collectors.toList());                  
                    if (!listAsEs.isEmpty()) {
                        listAsEs.forEach((t) -> {
                            Asistenciasacademicas a = em.find(Asistenciasacademicas.class, t.getAcademica());
                            em.remove(a);
                            em.flush();
                        });
                    }
                    f.setEntityClass(Asistenciasacademicas.class);
                    f.flush();
                    break;
            }
            res = Boolean.TRUE;
        }else{
            res = Boolean.TRUE;
        }
        return res;
    }
    
    public Boolean calificacionesRegistradas(Estudiante e) {
        List<CalificacionPromedio> cps = em.createQuery("select calP from CalificacionPromedio calP INNER JOIN calP.cargaAcademica c INNER JOIN c.cveGrupo g INNER JOIN calP.estudiante est WHERE g.idGrupo=:idGrupo AND est.idEstudiante=:idEstudiante", CalificacionPromedio.class).setParameter("idGrupo", e.getGrupo().getIdGrupo()).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
        List<Calificacion> cs = em.createQuery("select cs from Calificacion cs INNER JOIN cs.configuracionDetalle cd INNER JOIN cd.configuracion con INNER JOIN con.carga c INNER JOIN c.cveGrupo g INNER JOIN cs.idEstudiante est WHERE g.idGrupo=:idGrupo AND est.idEstudiante=:idEstudiante", Calificacion.class).setParameter("idGrupo", e.getGrupo().getIdGrupo()).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
        List<TareaIntegradoraPromedio> tips = em.createQuery("select cs from TareaIntegradoraPromedio cs INNER JOIN cs.tareaIntegradora tin INNER JOIN tin.carga c INNER JOIN c.cveGrupo g INNER JOIN cs.estudiante est WHERE g.idGrupo=:idGrupo AND est.idEstudiante=:idEstudiante", TareaIntegradoraPromedio.class).setParameter("idGrupo", e.getGrupo().getIdGrupo()).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
        List<CalificacionNivelacion> cns = em.createQuery("select cs from CalificacionNivelacion cs INNER JOIN cs.cargaAcademica c INNER JOIN c.cveGrupo g INNER JOIN cs.estudiante est WHERE g.idGrupo=:idGrupo AND est.idEstudiante=:idEstudiante", CalificacionNivelacion.class).setParameter("idGrupo", e.getGrupo().getIdGrupo()).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
        
        Boolean res = false;

        if (!cps.isEmpty()) {
            cps.forEach((t) -> {
                CalificacionPromedio a = em.find(CalificacionPromedio.class, t.getCalificacionPromedioPK());
                em.remove(a);
                em.flush();
            });
            f.setEntityClass(CalificacionPromedio.class);
            f.flush();
            res = Boolean.TRUE;
        }
        if (!cs.isEmpty()) {
            cs.forEach((t) -> {
                Calificacion a = em.find(Calificacion.class, t.getCalificacion());
                em.remove(a);
                em.flush();
            });
            f.setEntityClass(Calificacion.class);
            f.flush();
            res = Boolean.TRUE;
        }
        if (!tips.isEmpty()) {
            tips.forEach((t) -> {
                TareaIntegradoraPromedio a = em.find(TareaIntegradoraPromedio.class, t.getTareaIntegradoraPromedioPK());
                em.remove(a);
                em.flush();
            });
            f.setEntityClass(TareaIntegradoraPromedio.class);
            f.flush();
            res = Boolean.TRUE;
        }
        if (!cns.isEmpty()) {
            cns.forEach((t) -> {
                CalificacionNivelacion a = em.find(CalificacionNivelacion.class, t.getCalificacionNivelacionPK());
                em.remove(a);
                em.flush();
            });
            f.setEntityClass(CalificacionNivelacion.class);
            f.flush();
            res = Boolean.TRUE;
        }
        return res;
    }
    
    public void operacionesEstudiantesA(DtoReincorporacion.ProcesoInscripcionRein rr, Aspirante a,String tipoReicorporacion) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesEstudiantesA()");
        if (!rr.getGrupos().isEmpty()) {
            Grupo g = rr.getGrupos().get(0);
            List<Estudiante> es = new ArrayList<>();
            Integer valorI = 0;
            if (tipoReicorporacion.equals("TSU")) {
                valorI = 1;
            } else {
                valorI = 7;
            }
//            System.out.println("valorI"+valorI);
            for (int i = valorI; i < g.getGrado(); i++) {
                es = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN e.grupo g WHERE a.idAspirante=:idAspirante AND g.grado=:grado AND g.plan.idPlanEstudio=:idPlanEstudio", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("grado", i).setParameter("idPlanEstudio", g.getPlan().getIdPlanEstudio()).getResultList();
                if (es.isEmpty()) {
                    Grupo grupo = new Grupo();
                    grupo = getGrupoIncrito(g.getLiteral(), i, (g.getPeriodo() - (g.getGrado() - i)), g.getPlan().getIdPlanEstudio());
                    Estudiante e = new Estudiante();
                    e.setAspirante(new Aspirante());
                    e.setTipoEstudiante(new TipoEstudiante());
                    e.setGrupo(new Grupo());
                    e.setAspirante(a);
                    if (tipoReicorporacion.equals("TSU")) {
                        e.setTipoEstudiante(new TipoEstudiante(Short.parseShort("5")));
                    } else {
                        e.setTipoEstudiante(new TipoEstudiante(Short.parseShort("6")));
                    }
                    e.setGrupo(grupo);
                    e.setFechaAlta(new Date());
                    e.setTipoRegistro("Regularización de calificaciones por reincoporación");
                    e.setPeriodo(grupo.getPeriodo());
                    e.setCarrera(grupo.getIdPe());
                    e.setTrabajadorInscribe(rr.getTrabajadorInscribe());
                    e.setMatricula(rr.getMatricula());
                    e.setOpcionIncripcion(rr.getOpcionIncripcion());
                    em.persist(e);
                    f.setEntityClass(Estudiante.class);
                    f.flush();
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesEstudiantesA()"+e.getIdEstudiante());
                    generaraRegistroCalificaciones(e,grupo);
                }
            }
        }

    }
    
    public Grupo getGrupoIncrito(Character literal, Integer grado, Integer periodo,Integer plan) {
        Grupo grupo = new Grupo();
        List<Grupo> gs = em.createQuery("select e from Grupo e WHERE e.grado=:grado AND e.periodo=:periodo AND e.plan.idPlanEstudio=:idPlanEstudio", Grupo.class).setParameter("grado", grado).setParameter("periodo", periodo).setParameter("idPlanEstudio", plan).getResultList();
        if (!gs.isEmpty()) {
            List<Grupo> gsfiltro = gs.stream().filter(t -> t.getLiteral().toString().equals(literal.toString())).collect(Collectors.toList());
            if (gsfiltro.isEmpty()) {
                grupo = gs.get(0);
            } else {
                grupo = gsfiltro.get(0);
            }
        }
        return grupo;
    }
    
    public ResultadoEJB<DtoReincorporacion.ProcesoInscripcionRein> operacionesEstudianteR(DtoReincorporacion.ProcesoInscripcionRein rr,Aspirante a,EventoEscolar escolar, String tipor) {
        try {
            if (!rr.getGrupos().isEmpty()) {   
                    Grupo grupo = new Grupo();
                    grupo = rr.getGrupos().get(0);
                    Estudiante e = new Estudiante();
                    e.setAspirante(new Aspirante());
                    e.setTipoEstudiante(new TipoEstudiante());
                    e.setGrupo(new Grupo());
                    e.setAspirante(a);
                    e.setTipoEstudiante(new TipoEstudiante(Short.parseShort("1")));
                    e.setGrupo(grupo);
                    e.setFechaAlta(new Date());
                    e.setTipoRegistro(rr.getTipoRegistro());
                    e.setPeriodo(grupo.getPeriodo());
                    e.setCarrera(grupo.getIdPe());
                    e.setTrabajadorInscribe(rr.getTrabajadorInscribe());
                    e.setMatricula(rr.getMatricula());
                    e.setOpcionIncripcion(rr.getOpcionIncripcion());
                    em.persist(e);
                    f.setEntityClass(Estudiante.class);
                    f.flush();
          
                List<Estudiante> es = em.createQuery("select e from Estudiante e WHERE e.matricula=:matricula ORDER BY e.periodo", Estudiante.class).setParameter("matricula", rr.getMatricula()).getResultList();
                e = new Estudiante();
                if (!es.isEmpty()) {
                    e = es.get(es.size() - 1);
//                    asistenciasRegistradas(e, Operacion.PERSISTIR);
                }
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesEstudianteR()"+e);
                operacionesDocumentosentregadosestudiante(rr);
                operacionesLogin(e);
//                generaraRegistroCalificaciones(a, escolar);
//System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesEstudianteR()"+tipor);
                if (!tipor.equals("TSU")) {
//                    System.out.println("Dentro");
                    operacionesHistoricoTSU(new DtoReincorporacion.HistorialTsu(a.getIdPersona(), a, e, new UniversidadEgresoAspirante(),Boolean.FALSE, new EstudianteHistorialTsu(), new PlanesEstudioExternos() , new ArrayList<>(), new ArrayList<>(),Boolean.FALSE));
                }
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesEstudianteR).", e, null);
        }
    }
    
    public ResultadoEJB<EstudianteHistorialTsu> operacionesHistoricoTSU(DtoReincorporacion.HistorialTsu et) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistoricoTSU()"+et.getEstudiante());
            List<Integer> clavesAspirantes= new ArrayList<>();
            List<Aspirante> asp = em.createQuery("select a from Aspirante a INNER JOIN a.idPersona p WHERE p.idpersona=:idpersona ", Aspirante.class).setParameter("idpersona", et.getPersona().getIdpersona()).getResultList();
//            System.out.println("asp "+asp.size());
            asp.forEach((t) -> {
                clavesAspirantes.add(t.getIdAspirante());
            });
//            System.out.println("clavesAspirantes "+clavesAspirantes.size());
            
            List<Estudiante> est = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.aspirante a INNER JOIN e.grupo g WHERE g.grado=:gradoTSU  AND  a.idAspirante IN :aspirantes", Estudiante.class).setParameter("aspirantes", clavesAspirantes).setParameter("gradoTSU", 6).getResultList();
//            System.out.println("est "+est.size());
            
            if (est.isEmpty()) {
//                System.out.println("Entro 2");
                List<EstudianteHistorialTsu> es = em.createQuery("SELECT e FROM EstudianteHistorialTsu e INNER JOIN e.idEstudiante p INNER JOIN p.aspirante a WHERE a.idAspirante=:idAspirante", EstudianteHistorialTsu.class).setParameter("idAspirante", et.getEstudiante().getAspirante().getIdAspirante()).getResultList();
                EstudianteHistorialTsu l = new EstudianteHistorialTsu();
//                System.out.println("es "+es.size());

                Operacion operacion;
                if (!es.isEmpty()) {
                    l = es.get(es.size() - 1);
                    operacion = Operacion.ACTUALIZAR;
                } else {
                    operacion = Operacion.PERSISTIR;
                }
                l.setGeneracion("");
                l.setPeriodoCarrera("");
                l.setPromedioEgreso(0.0);
                switch (operacion) {
                    case PERSISTIR:
                        l.setIdEstudiante(new Estudiante());
                        l.setIdEstudiante(et.getEstudiante());
                        em.persist(l);
                        f.setEntityClass(EstudianteHistorialTsu.class);
                        f.flush();
                        break;
                    case ACTUALIZAR:
                        em.merge(l);
                        f.setEntityClass(EstudianteHistorialTsu.class);
                        f.flush();
                        break;
                }
//                System.out.println("EstudianteHistorialTsu "+l);
                return ResultadoEJB.crearCorrecto(l, "EstudianteHistorialTsu Encontrados");
            } else {
                return ResultadoEJB.crearCorrecto(new EstudianteHistorialTsu(), "EstudianteHistorialTsu Encontrados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar EstudianteHistorialTsu (EjbReincorporacion.operacionesHistoricoTSU).", e, null);
        }
    }

    public ResultadoEJB<Documentosentregadosestudiante> operacionesDocumentosentregadosestudiante(DtoReincorporacion.ProcesoInscripcionRein rr) {
        try {
            List<Estudiante> es = em.createQuery("select e from Estudiante e WHERE e.matricula=:matricula ORDER BY e.periodo", Estudiante.class).setParameter("matricula", rr.getMatricula()).getResultList();
            Estudiante e = new Estudiante();
            if (!es.isEmpty()) {
                e = es.get(es.size() - 1);
            }
            List<Documentosentregadosestudiante> docs = em.createQuery("select d from Documentosentregadosestudiante d INNER JOIN d.estudiante1 e WHERE e.idEstudiante=:idEstudiante ORDER BY e.periodo", Documentosentregadosestudiante.class).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
            Operacion operacion;
            if (!docs.isEmpty()) {
                operacion = Operacion.ACTUALIZAR;
            } else {
                operacion = Operacion.PERSISTIR;
            }
            switch (operacion) {
                case PERSISTIR:
                    rr.getDocumentosentregadosestudiante().setEstudiante1(new Estudiante());
                    rr.getDocumentosentregadosestudiante().setEstudiante1(e);
                    rr.getDocumentosentregadosestudiante().setEstudiante(e.getIdEstudiante());
                    em.persist(rr.getDocumentosentregadosestudiante());
                    f.setEntityClass(Documentosentregadosestudiante.class);
                    f.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(rr.getDocumentosentregadosestudiante());
                    f.setEntityClass(Documentosentregadosestudiante.class);
                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr.getDocumentosentregadosestudiante(), "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesDocumentosentregadosestudiante).", e, null);
        }
    }
    
    public void generaraRegistroCalificaciones(Estudiante est, Grupo g) {
        try {
            if (est.getIdEstudiante() != null) {
                if (!g.getCargaAcademicaList().isEmpty()) {
                    g.getCargaAcademicaList().forEach((c) -> {
                        if (!c.getUnidadMateriaConfiguracionList().isEmpty()) {
                            c.getUnidadMateriaConfiguracionList().forEach((um) -> {
                                if (g.getPeriodo() <= 56) {
                                    if (!um.getUnidadMateriaConfiguracionDetalleList().isEmpty()) {
                                        um.getUnidadMateriaConfiguracionDetalleList().forEach((umcd) -> {
                                            Calificacion c1 = new Calificacion();
                                            c1.setConfiguracionDetalle(new UnidadMateriaConfiguracionDetalle());
                                            c1.setConfiguracionDetalle(umcd);
                                            c1.setIdEstudiante(new Estudiante());
                                            c1.setIdEstudiante(est);
                                            c1.setValor(0D);
                                            em.persist(c1);
                                            f.setEntityClass(Calificacion.class);
                                            f.flush();
                                        });
                                    }
                                } else {
                                    if (!um.getUnidadMateriaConfiguracionEvidenciaInstrumentoList().isEmpty()) {
                                        um.getUnidadMateriaConfiguracionEvidenciaInstrumentoList().forEach((umce) -> {
                                            CalificacionEvidenciaInstrumento c1 = new CalificacionEvidenciaInstrumento();
                                            c1.setConfiguracionEvidencia(new UnidadMateriaConfiguracionEvidenciaInstrumento());
                                            c1.setConfiguracionEvidencia(umce);
                                            c1.setIdEstudiante(new Estudiante());
                                            c1.setIdEstudiante(est);
                                            c1.setValor(0D);
                                            em.persist(c1);
                                            f.setEntityClass(CalificacionEvidenciaInstrumento.class);
                                            f.flush();
                                        });
                                    }
                                }
                            });
                        }
                        if (c.getTareaIntegradora() != null) {
                            TareaIntegradora integradora = new TareaIntegradora();
                            integradora = c.getTareaIntegradora();
                            TareaIntegradoraPromedio tip = new TareaIntegradoraPromedio();
                            TareaIntegradoraPromedioPK tippk = new TareaIntegradoraPromedioPK();
                            tippk.setIdEstudiante(est.getIdEstudiante());
                            tippk.setIdTareaIntegradora(integradora.getIdTareaIntegradora());
                            tip.setEstudiante(new Estudiante());
                            tip.setEstudiante(est);
                            tip.setTareaIntegradoraPromedioPK(new TareaIntegradoraPromedioPK());
                            tip.setTareaIntegradoraPromedioPK(tippk);
                            tip.setValor(0D);
                            em.persist(tip);
                            f.setEntityClass(TareaIntegradoraPromedio.class);
                            f.flush();
                        }
                        CalificacionPromedio promedio = new CalificacionPromedio();
                        CalificacionPromedioPK promedioPK = new CalificacionPromedioPK();
                        promedioPK.setCarga(c.getCarga());
                        promedioPK.setIdEstudiante(est.getIdEstudiante());
                        promedio.setCalificacionPromedioPK(new CalificacionPromedioPK());
                        promedio.setCalificacionPromedioPK(promedioPK);
                        promedio.setCargaAcademica(new CargaAcademica());
                        promedio.setCargaAcademica(c);
                        promedio.setEstudiante(new Estudiante());
                        promedio.setEstudiante(est);
                        promedio.setFechaActualizacion(new Date());
                        promedio.setTipo("Regulatoria");
                        em.persist(promedio);
                        f.setEntityClass(CalificacionPromedio.class);
                        f.flush();
                    });
                }
            }
        }catch (Exception e) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public ResultadoEJB<Login> operacionesLogin(Estudiante et) {
        try {
            Persona p=new Persona();
            p=et.getAspirante().getIdPersona();
            List<Login> es = em.createQuery("select e from Login e INNER JOIN e.persona1 p WHERE p.idpersona=:idpersona", Login.class).setParameter("idpersona", p.getIdpersona()).getResultList();
            Login l = new Login();
            Operacion operacion;
            if (!es.isEmpty()) {
                l = es.get(es.size() - 1);
                operacion = Operacion.ACTUALIZAR;
            } else {
                operacion = Operacion.PERSISTIR;
            }
            l.setActivo(Boolean.TRUE);
            l.setModificado(Boolean.FALSE);
            l.setPassword("3TASB6Q9S5bFbobwCXGD9A==");
            l.setUsuario(String.valueOf(et.getMatricula()));
            switch (operacion) {
                case PERSISTIR:
                    l.setPersona1(new Persona());
                    l.setPersona1(p);                    
                    l.setPersona(p.getIdpersona());
                    em.persist(l);
                    f.setEntityClass(Login.class);
                    f.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(l);
                    f.setEntityClass(Login.class);
                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(l, "Login Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Login (EjbReincorporacion.operacionesLogin).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.CalificacionesR> registrarCalificacionesPorPromedio(DtoReincorporacion.CalificacionesR cr) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(A)");
            Operacion opera;
            Estudiante estudiante = cr.getCalificacionPromedio().getEstudiante();
            CargaAcademica cargaAcademica = cr.getAcademica();
            
            List<Calificacion> umcd = new ArrayList<>();
            List<CalificacionEvidenciaInstrumento> umcei = new ArrayList<>();
            
            List<Calificacion> umcd2 = new ArrayList<>();
            List<CalificacionEvidenciaInstrumento> umcei2 = new ArrayList<>();
            
            List<UnidadMateriaConfiguracionDetalle> detalles = new ArrayList<>();
            List<UnidadMateriaConfiguracionEvidenciaInstrumento> instrumentos = new ArrayList<>();
            
            umcdTotal = 0;
            umceiTotal = 0;
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(B)"+cr.getCalificacionPromedio().getCargaAcademica().getCarga());
            
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(C)");
            if (estudiante.getPeriodo() <= 56) {
                umcd2 = em.createQuery("select cs from Calificacion cs INNER JOIN cs.idEstudiante est WHERE est.idEstudiante=:idEstudiante", Calificacion.class).setParameter("idEstudiante", cr.getCalificacionPromedio().getEstudiante().getIdEstudiante()).getResultList();
                cr.getAcademica().getUnidadMateriaConfiguracionList().forEach((t) -> {
                    umcdTotal = umcdTotal + t.getUnidadMateriaConfiguracionDetalleList().size();
                });
                umcd=umcd2.stream().filter(t-> Objects.equals(t.getConfiguracionDetalle().getConfiguracion().getCarga().getCarga(), cargaAcademica.getCarga())).collect(Collectors.toList());
                
            } else {
                umcei2 = em.createQuery("select cs from CalificacionEvidenciaInstrumento cs INNER JOIN cs.idEstudiante est WHERE est.idEstudiante=:idEstudiante", CalificacionEvidenciaInstrumento.class).setParameter("idEstudiante", cr.getCalificacionPromedio().getEstudiante().getIdEstudiante()).getResultList();
                cr.getAcademica().getUnidadMateriaConfiguracionList().forEach((t) -> {
                    umceiTotal = umceiTotal + t.getUnidadMateriaConfiguracionEvidenciaInstrumentoList().size();
                });
                umcei=umcei2.stream().filter(t-> Objects.equals(t.getConfiguracionEvidencia().getConfiguracion().getCarga().getCarga(), cargaAcademica.getCarga())).collect(Collectors.toList());
             
            }
//            System.out.println("umcd= "+umcd.size() + " umcdTotal= "+umcdTotal);
//            System.out.println("umcei= "+umcei.size() + " umceiTotal= "+umceiTotal);
            
            List<TareaIntegradoraPromedio> tps = em.createQuery("select t from TareaIntegradoraPromedio t INNER JOIN t.tareaIntegradora ti INNER JOIN ti.carga c WHERE c.carga=:carga AND t.estudiante.idEstudiante=:idEstudiante ", TareaIntegradoraPromedio.class).setParameter("carga", cr.getAcademica().getCarga()).setParameter("idEstudiante",  cr.getCalificacionPromedio().getEstudiante().getIdEstudiante()).getResultList();
            
            if ((!umcd.isEmpty()) && (umcd.size() == umcdTotal)) {
                umcd.forEach((t) -> {
                    t.setValor(cr.getCalificacionPromedio().getValor());
                    em.merge(t);
                    f.setEntityClass(Calificacion.class);
                    f.flush();
                });
            } else {
                if (!umcd.isEmpty()) {
                    umcd.forEach((t) -> {
                        detalles.add(t.getConfiguracionDetalle());
                        t.setValor(cr.getCalificacionPromedio().getValor());
                        em.merge(t);
                        em.flush();
                        f.setEntityClass(Calificacion.class);
                        f.flush();
                    });
                }
                cr.getAcademica().getUnidadMateriaConfiguracionList().forEach((t) -> {
                    t.getUnidadMateriaConfiguracionDetalleList().forEach((k) -> {
                        List<UnidadMateriaConfiguracionDetalle> cal = detalles.stream().filter(d -> Objects.equals(d.getConfiguracionDetalle(), k.getConfiguracionDetalle())).collect(Collectors.toList());
                        if (cal.isEmpty()) {
                            Calificacion c = new Calificacion();
                            c.setConfiguracionDetalle(new UnidadMateriaConfiguracionDetalle());
                            c.setConfiguracionDetalle(k);
                            c.setIdEstudiante(new Estudiante());
                            c.setIdEstudiante(estudiante);
                            c.setValor(cr.getCalificacionPromedio().getValor());
                            em.persist(t);
                            f.setEntityClass(Calificacion.class);
                            f.flush();
                        }
                    });
                });
            }
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(9)");
            if((!umcei.isEmpty()) && (umcei.size()==umceiTotal)){
                umcei.forEach((t) -> {
                    t.setValor(cr.getCalificacionPromedio().getValor());
                    em.merge(t);
                    f.setEntityClass(CalificacionEvidenciaInstrumento.class);
                    f.flush();
                });
            } else {
                if (!umcd.isEmpty()) {
                    umcei.forEach((t) -> {
                        instrumentos.add(t.getConfiguracionEvidencia());
                        t.setValor(cr.getCalificacionPromedio().getValor());
                        em.merge(t);
                        f.setEntityClass(CalificacionEvidenciaInstrumento.class);
                        f.flush();
                    });
                }
                cr.getAcademica().getUnidadMateriaConfiguracionList().forEach((t) -> {
                    t.getUnidadMateriaConfiguracionEvidenciaInstrumentoList().forEach((k) -> {
                        List<UnidadMateriaConfiguracionEvidenciaInstrumento> cal = instrumentos.stream().filter(d -> Objects.equals(d.getConfiguracionEvidenciaInstrumento(), k.getConfiguracionEvidenciaInstrumento())).collect(Collectors.toList());
                        if (cal.isEmpty()) {
                            CalificacionEvidenciaInstrumento c = new CalificacionEvidenciaInstrumento();
                            c.setConfiguracionEvidencia(new UnidadMateriaConfiguracionEvidenciaInstrumento());
                            c.setConfiguracionEvidencia(k);
                            c.setIdEstudiante(new Estudiante());
                            c.setIdEstudiante(estudiante);
                            c.setValor(cr.getCalificacionPromedio().getValor());
                            em.persist(t);
                            f.setEntityClass(CalificacionEvidenciaInstrumento.class);
                            f.flush();
                        }
                    });
                });
            }
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(10)");
            if (!tps.isEmpty()) {
                tps.forEach((t) -> {
                    t.setValor(cr.getCalificacionPromedio().getValor());
                    em.merge(t);
                    f.setEntityClass(TareaIntegradoraPromedio.class);
                    f.flush();
                });
            }
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio()"+cr.getCalificacionPromedio().getValor());
            cr.getCalificacionPromedio().setFechaActualizacion(new Date());
            em.merge(cr.getCalificacionPromedio());
            f.setEntityClass(CalificacionPromedio.class);
            f.flush();
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(11)");
        return ResultadoEJB.crearCorrecto(cr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.registrarCalificacionesPorPromedio).", e, null);
        }
    }   
    
    public ResultadoEJB<DtoReincorporacion.HistoricoReincorporaciones> actualizacionRegistrosReincorporaciones(DtoReincorporacion.HistoricoReincorporaciones cr) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.registrarCalificacionesPorPromedio(A)");
            List<Estudiante> est = em.createQuery("SELECT e FROM Estudiante e WHERE e.idEstudiante=:idEstudiante", Estudiante.class).setParameter("idEstudiante", cr.getEstudiante().getIdEstudiante()).getResultList();
            
            List<CalificacionPromedio> calificacionPromedios = em.createQuery("SELECT c FROM CalificacionPromedio c INNER JOIN c.estudiante e WHERE e.idEstudiante=:idEstudiante", CalificacionPromedio.class).setParameter("idEstudiante", cr.getEstudiante().getIdEstudiante()).getResultList();
            
            
            if (!est.isEmpty()) {
                Estudiante e = est.get(0);
                if (!Objects.equals(e.getTipoEstudiante().getIdTipoEstudiante(), cr.getTipoEstudiante().getIdTipoEstudiante())) {
                    e.setTipoEstudiante(new TipoEstudiante());
                    e.setTipoEstudiante(new TipoEstudiante(cr.getTipoEstudiante().getIdTipoEstudiante()));
                    em.merge(e);
                    f.setEntityClass(Estudiante.class);
                    f.flush();
                }
            }
            
            if(!calificacionPromedios.isEmpty()){
                calificacionPromedios.forEach((t) -> {
                    t.setTipo(cr.getTipoCalificacion());
                    em.merge(t);
                    f.setEntityClass(CalificacionPromedio.class);
                    f.flush();
                });
            }
        return ResultadoEJB.crearCorrecto(cr, "DTOHistoricoReincorporaciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOHistoricoReincorporaciones (EjbReincorporacion.actualizacionRegistrosReincorporaciones).", e, null);
        }
    }   
    
    public void enviarConfirmacionCorreoElectronico(String correoDestino, String titulo, String asunto, String mensaje) {
        // El correo gmail de envío
        String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";//correo del arrea de desarrollo
        String claveCorreo = "DServiciosEscolares19";//contraseña del correo del arrea de desarrollo
        Properties properties = new Properties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);

        if (correoDestino != null) {
            Session session = Session.getInstance(properties, null);
            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(correoEnvia, titulo));

                InternetAddress internetAddress = new InternetAddress(correoDestino);
                mimeMessage.setRecipient(Message.RecipientType.TO, internetAddress);

                mimeMessage.setSubject(asunto);

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(mensaje);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                mimeMessage.setContent(multipart);

                Transport transport = session.getTransport("smtp");
                transport.connect(correoEnvia, claveCorreo);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    //------------------------------------------------------------------------Busquedas A------------------------------------------------------------------------//
    public ResultadoEJB<DtoReincorporacion.RegistroEstudiante> getDtoRegistroEstudiante(Aspirante a, Persona p) {
        try {
            Boolean encontrado = Boolean.FALSE;
            DtoReincorporacion.RegDatosLaborales rdl = new DtoReincorporacion.RegDatosLaborales(new DatosLaborales(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.EstudianteR er = new DtoReincorporacion.EstudianteR(new Estudiante(), new Documentosentregadosestudiante(), "", new TipoEstudiante(), new Grupo(), Boolean.FALSE, Boolean.FALSE, Operacion.PERSISTIR);
            DtoReincorporacion.AcademicosCR dac=new DtoReincorporacion.AcademicosCR(new DatosAcademicosComplementarios(),Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.SosioeconomicosR ds = new DtoReincorporacion.SosioeconomicosR(new DatosSocioeconomicos(),Operacion.PERSISTIR, Boolean.FALSE);
            DtoReincorporacion.VocacionalR vr = new DtoReincorporacion.VocacionalR(new EncuestaVocacional(),new AreasUniversidad(),Operacion.PERSISTIR, Boolean.FALSE);
            List<DtoReincorporacion.RcontactoEmergencia> rem = new ArrayList<>();
            List<DtoReincorporacion.Familia> fam = new ArrayList<>();
            CuestionarioPsicopedagogicoResultados cr= new CuestionarioPsicopedagogicoResultados();


            ResultadoEJB<DtoReincorporacion.RegDatosLaborales> resRdl = getRegDatosLaborales(a);
            ResultadoEJB<DtoReincorporacion.EstudianteR> resEsr = getEstudianteRRes(a);
            ResultadoEJB<DtoReincorporacion.AcademicosCR> resDac = getRegDatosAcademicosC(a);
            ResultadoEJB<DtoReincorporacion.SosioeconomicosR> resDso = getRegDatosSoEconomicos(a);
            ResultadoEJB<List<DtoReincorporacion.RcontactoEmergencia>> resRce = getRegContactoEm(a);
            ResultadoEJB<List<DtoReincorporacion.Familia>> resFam = getRegFamiliares(a);
            ResultadoEJB<DtoReincorporacion.VocacionalR> resVoc = getRegVocacional(p);
            ResultadoEJB<CuestionarioPsicopedagogicoResultados> resCues= getCuestionarioPsico(a);


            if (resEsr.getCorrecto()) {er = resEsr.getValor();}
            if (resRdl.getCorrecto()) {rdl = resRdl.getValor();}
            if (resDac.getCorrecto()) {dac = resDac.getValor();}
            if (resDso.getCorrecto()) {ds = resDso.getValor();}
            if (resRce.getCorrecto()) {rem = resRce.getValor();}
            if (resFam.getCorrecto()) {fam = resFam.getValor();}
            if (resVoc.getCorrecto()) {vr = resVoc.getValor();}
            if (resCues.getCorrecto()) {cr = resCues.getValor();}
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoRegistroEstudiante()");



            DtoReincorporacion.RegistroEstudiante rr=new DtoReincorporacion.RegistroEstudiante(er, rem, dac, rdl, ds, vr, fam,cr, Boolean.TRUE);
            return ResultadoEJB.crearCorrecto(rr, "RegistroEstudiante Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar RegistroEstudiante (EjbReincorporacion.getDtoRegistroEstudiante).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.EstudianteR> getEstudianteRRes(Aspirante a) {
        try {
            List<Estudiante> das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante ORDER BY e.periodo", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.EstudianteR ers = new DtoReincorporacion.EstudianteR(new Estudiante(), new Documentosentregadosestudiante(), "", new TipoEstudiante(), new Grupo(), Boolean.FALSE, Boolean.FALSE, Operacion.ACTUALIZAR);
            if (!das.isEmpty()) {
                Estudiante estudiante = das.get(das.size() - 1);
                List<Documentosentregadosestudiante> ds = em.createQuery("select d from Documentosentregadosestudiante d INNER JOIN d.estudiante1 a WHERE a.idEstudiante=:idEstudiante ORDER BY a.periodo", Documentosentregadosestudiante.class).setParameter("idEstudiante", estudiante.getIdEstudiante()).getResultList();
                Documentosentregadosestudiante d = new Documentosentregadosestudiante();                
                Boolean editable = Boolean.FALSE;
                if (!ds.isEmpty()) {
                    d = ds.get(0);
                    editable = Boolean.TRUE;
                }
                ers = new DtoReincorporacion.EstudianteR(estudiante, d, getAreaSeleccionada(estudiante.getCarrera()), estudiante.getTipoEstudiante(), estudiante.getGrupo(), estudiante.getOpcionIncripcion(), editable, Operacion.ACTUALIZAR);
            }
            return ResultadoEJB.crearCorrecto(ers, "EstudianteR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar EstudianteR (EjbReincorporacion.getEstudianteRRes).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.RegDatosLaborales> getRegDatosLaborales(Aspirante a) {
        try {
            List<DatosLaborales> das = em.createQuery("select e from DatosLaborales e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", DatosLaborales.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.RegDatosLaborales ers = new DtoReincorporacion.RegDatosLaborales(new DatosLaborales(), Operacion.PERSISTIR, Boolean.FALSE);
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getRegDatosLaborales(das)"+das.size());
            if (!das.isEmpty()) {
                DatosLaborales dl = das.get(das.size() - 1);
                ResultadoEJB<Asentamiento> a1 = getAsentamiento(dl.getAsentamiento());
                Asentamiento a2 = new Asentamiento();
                if (a1.getCorrecto()) {
                    a2 = a1.getValor();
                }
                ers = new DtoReincorporacion.RegDatosLaborales(dl, Operacion.ACTUALIZAR,Boolean.TRUE);
            }else{
                DatosLaborales dl=new DatosLaborales();
                dl.setTrabajoActual(Boolean.FALSE);
                dl.setNombreEmpresa("");
                dl.setPuesto("");
                dl.setCalle("");
                dl.setNumero("");
                dl.setEstado(21);
                dl.setMunicipio(197);
                dl.setAsentamiento(97065);
                dl.setTelefonoEmpresa("");
                dl.setRazonDeTrabajo("");
                dl.setIngresoMensual(0);                
                ResultadoEJB<DtoReincorporacion.RegDatosLaborales> ders=operacionesLaborales(new DtoReincorporacion.RegDatosLaborales(dl , Operacion.DETACHAR, Boolean.TRUE), a);
                if(ders.getCorrecto()){
                    ers=ders.getValor();
                }
            }
            return ResultadoEJB.crearCorrecto(ers, "RegDatosLaborales Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar RegDatosLaborales (EjbReincorporacion.getRegDatosLaborales).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.AcademicosCR> getRegDatosAcademicosC(Aspirante a) {
        try {
            List<DatosAcademicosComplementarios> das = em.createQuery("select e from DatosAcademicosComplementarios e INNER JOIN e.aspirante1 a WHERE a.idAspirante=:idAspirante", DatosAcademicosComplementarios.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.AcademicosCR dac=new DtoReincorporacion.AcademicosCR(new DatosAcademicosComplementarios(),Operacion.PERSISTIR, Boolean.FALSE);
            if (!das.isEmpty()) {
                DatosAcademicosComplementarios dl = das.get(das.size() - 1);                
                dac = new DtoReincorporacion.AcademicosCR(dl,Operacion.ACTUALIZAR, Boolean.TRUE);
            }else{
                DatosAcademicosComplementarios dl=new DatosAcademicosComplementarios(a.getIdAspirante());
                ResultadoEJB<DtoReincorporacion.AcademicosCR> ders=operacionesAcademicosConplementarios(new DtoReincorporacion.AcademicosCR(dl , Operacion.DETACHAR, Boolean.TRUE), a);
                if(ders.getCorrecto()){
                    dac=ders.getValor();
                }
            }
            return ResultadoEJB.crearCorrecto(dac, "AcademicosCR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AcademicosCR (EjbReincorporacion.getRegDatosAcademicosC).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.SosioeconomicosR> getRegDatosSoEconomicos(Aspirante a) {
        try {
            List<DatosSocioeconomicos> das = em.createQuery("select e from DatosSocioeconomicos e INNER JOIN e.aspirante1 a WHERE a.idAspirante=:idAspirante", DatosSocioeconomicos.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.SosioeconomicosR ds = new DtoReincorporacion.SosioeconomicosR(new DatosSocioeconomicos(),Operacion.PERSISTIR, Boolean.FALSE);
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getRegDatosSoEconomicos()"+das.size());
            if (!das.isEmpty()) {
                DatosSocioeconomicos dl = das.get(das.size() - 1);                
                ds =new DtoReincorporacion.SosioeconomicosR(dl,Operacion.ACTUALIZAR, Boolean.TRUE);
            }else{
                DatosSocioeconomicos dl=new DatosSocioeconomicos(a.getIdAspirante());
                ResultadoEJB<DtoReincorporacion.SosioeconomicosR> ders=operacionesSocioeconomicos(new DtoReincorporacion.SosioeconomicosR(dl , Operacion.DETACHAR, Boolean.TRUE), a);
                if(ders.getCorrecto()){
                    ds=ders.getValor();
                }
            }
            return ResultadoEJB.crearCorrecto(ds, "SosioeconomicosR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar SosioeconomicosR (EjbReincorporacion.getRegDatosSoEconomicos).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.RcontactoEmergencia>> getRegContactoEm(Aspirante a) {
        try {
            List<ContactoEmergenciasEstudiante> das = em.createQuery("select e from ContactoEmergenciasEstudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", ContactoEmergenciasEstudiante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            List<DtoReincorporacion.RcontactoEmergencia> rem = new ArrayList<>();
            if (!das.isEmpty()) {
                das.forEach((t) -> {
                    rem.add(new DtoReincorporacion.RcontactoEmergencia(t, Operacion.ACTUALIZAR, Boolean.TRUE));
                });
            }
            return ResultadoEJB.crearCorrecto(rem, "RcontactoEmergencia Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar RcontactoEmergencia (EjbReincorporacion.getRegContactoEm).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.Familia>> getRegFamiliares(Aspirante a) {
        try {
            List<IntegrantesFamilia> das = em.createQuery("select e from IntegrantesFamilia e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", IntegrantesFamilia.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            List<DtoReincorporacion.Familia> fam = new ArrayList<>();
            if (!das.isEmpty()) {
                das.forEach((t) -> {                    
                fam.add(new DtoReincorporacion.Familia(t, t.getOcupacion(),t.getEscolaridad(),Operacion.ACTUALIZAR, Boolean.TRUE));
                });
            }
            return ResultadoEJB.crearCorrecto(fam, "Familia Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Familia (EjbReincorporacion.getRegFamiliares).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.VocacionalR> getRegVocacional(Persona p) {
        try {
            List<EncuestaVocacional> das = em.createQuery("select e from EncuestaVocacional e INNER JOIN e.persona p WHERE p.idpersona=:idpersona", EncuestaVocacional.class).setParameter("idpersona", p.getIdpersona()).getResultList();
            DtoReincorporacion.VocacionalR vrs = new DtoReincorporacion.VocacionalR(new EncuestaVocacional(), new AreasUniversidad(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!das.isEmpty()) {
                EncuestaVocacional ev=das.get(0);
                AreasUniversidad au=getOpcionArea(ev.getR4()).getValor();
                vrs = new DtoReincorporacion.VocacionalR(ev, au, Operacion.PERSISTIR, Boolean.TRUE);            
            }
            return ResultadoEJB.crearCorrecto(vrs, "VocacionalR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar VocacionalR (EjbReincorporacion.getRegVocacional).", e, null);
        }
    }
    
    public  ResultadoEJB<CuestionarioPsicopedagogicoResultados> getCuestionarioPsico(Aspirante a){
        try{
            //Obtiene el primero registro del estudiante
            Estudiante e = em.createQuery("select e from Estudiante  e where e.aspirante.idAspirante=:a",Estudiante.class)
                    .setParameter("a",a.getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(new Estudiante())
                    ;
//            System.out.println("EjbReincorporacion.getCuestionarioPsico e"+e);
            Estudiante est2= em.createQuery("select e from Estudiante  e where  e.matricula=:matricula order by  e.periodo asc ",Estudiante.class)
                    .setParameter("matricula",e.getMatricula())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
//            System.out.println("EjbReincorporacion.getCuestionarioPsico e2" +est2);
            // Obtiene los resultados
            CuestionarioPsicopedagogicoResultados c = em.createQuery("select c from CuestionarioPsicopedagogicoResultados c where  c.cuestionarioPsicopedagogicoResultadosPK.idEstudiante=:id",CuestionarioPsicopedagogicoResultados.class)
                    .setParameter("id",est2.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(new CuestionarioPsicopedagogicoResultados())
                    ;
//            System.out.println("EjbReincorporacion.getCuestionarioPsico "+ c);
            if(c!=null){
                return ResultadoEJB.crearCorrecto(c,"Resultados encontrados");
            }else {return ResultadoEJB.crearErroneo(2,c,"El estudiante aun no cuenta con resultados");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Cuestionario Psicopedagogico(EjbReincorporacion.getCuestionarioPsico).", e, null);

        }
    }

    public ResultadoEJB<Asentamiento> getAsentamiento(Integer ase) {
        try {
            List<Asentamiento> das = em.createQuery("select e from Asentamiento e WHERE e.asentamientoPK.asentamiento=:asentamiento", Asentamiento.class).setParameter("asentamiento", ase).getResultList();
            Asentamiento dl = das.get(das.size() - 1);
            return ResultadoEJB.crearCorrecto(dl, "Asentamiento Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Asentamiento (EjbReincorporacion.getAsentamiento).", e, null);
        }
    }
    
    public ResultadoEJB<Localidad> getLocalidadUn(Integer ase) {
        try {
            List<Localidad> das = em.createQuery("select e from Localidad e WHERE e.localidadPK.claveLocalidad=:claveLocalidad", Localidad.class).setParameter("claveLocalidad", ase).getResultList();
            Localidad dl = das.get(das.size() - 1);
            return ResultadoEJB.crearCorrecto(dl, "Localidad Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Localidad (EjbReincorporacion.getLocalidad).", e, null);
        }
    }
    
    public ResultadoEJB<Localidad> getLocalidadDireccion(Integer est,Integer mun,Integer loc) {
        try {
            List<Localidad> das = em.createQuery("select e from Localidad e WHERE e.localidadPK.claveLocalidad=:claveLocalidad AND e.localidadPK.claveEstado=:claveEstado AND e.localidadPK.claveMunicipio=:claveMunicipio", Localidad.class)
                    .setParameter("claveEstado", est)
                    .setParameter("claveMunicipio", mun)
                    .setParameter("claveLocalidad", loc)
                    .getResultList();
            Localidad dl = das.get(das.size() - 1);
            return ResultadoEJB.crearCorrecto(dl, "Localidad Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Localidad (EjbReincorporacion.getLocalidad).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.Familia> operacionesFamiliaR(DtoReincorporacion.Familia rr, Aspirante a) {
        try {
            rr.getFamilia().setEscolaridad(new Escolaridad());
            rr.getFamilia().setOcupacion(new Ocupacion());
            rr.getFamilia().setEscolaridad(rr.getEscolaridad());
            rr.getFamilia().setOcupacion(rr.getOcupacion());
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getFamilia().setAspirante(new Aspirante());
                    rr.getFamilia().setAspirante(a);
                    em.persist(rr.getFamilia());
                    f.setEntityClass(IntegrantesFamilia.class);
                    f.flush();
                    rr.setOperacion(Operacion.ACTUALIZAR);
                    break;
                case ACTUALIZAR:                    em.merge(rr.getFamilia());
                    f.setEntityClass(IntegrantesFamilia.class);                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "Familia Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Familia (EjbReincorporacion.operacionesFamiliaR).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.RcontactoEmergencia> operacionesContactoEmR(DtoReincorporacion.RcontactoEmergencia rr, Aspirante a) {
        try {
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getEmergenciasEstudiante().setAspirante(new Aspirante());
                    rr.getEmergenciasEstudiante().setAspirante(a);
                    em.persist(rr.getEmergenciasEstudiante());
                    f.setEntityClass(ContactoEmergenciasEstudiante.class);
                    f.flush();
                    rr.setOperacion(Operacion.ACTUALIZAR);
                    break;
                case ACTUALIZAR:                    em.merge(rr.getEmergenciasEstudiante());
                    f.setEntityClass(ContactoEmergenciasEstudiante.class);                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "RcontactoEmergencia Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar RcontactoEmergencia (EjbReincorporacion.operacionesContactoEmR).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.RegDatosLaborales> operacionesLaborales(DtoReincorporacion.RegDatosLaborales rr, Aspirante a) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesLaborales()"+rr.getOperacion());
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getDatosLaborales().setAspirante(new Aspirante());
                    rr.getDatosLaborales().setAspirante(a);
                    em.persist(rr.getDatosLaborales());
                    f.setEntityClass(DatosLaborales.class);
                    f.flush();
                    rr.setOperacion(Operacion.ACTUALIZAR);
                    rr.setEcontrado(Boolean.TRUE);
                    break;
                case ACTUALIZAR:                    em.merge(rr.getDatosLaborales());
                    f.setEntityClass(DatosLaborales.class);                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "RegDatosLaborales Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar RegDatosLaborales (EjbReincorporacion.operacionesLaborales).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.SosioeconomicosR> operacionesSocioeconomicos(DtoReincorporacion.SosioeconomicosR rr, Aspirante a) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesSocioeconomicos(AS)"+a.getIdAspirante());
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesSocioeconomicos(PE)"+a.getIdPersona().getIdpersona());
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesSocioeconomicos()"+rr.getOperacion());
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getSocioeconomicos().setAspirante1(new Aspirante());
                    rr.getSocioeconomicos().setAspirante1(a);
                    rr.getSocioeconomicos().setAspirante(a.getIdAspirante());
                    em.persist(rr.getSocioeconomicos());
                    f.setEntityClass(DatosSocioeconomicos.class);
                    f.flush();
                    rr.setOperacion(Operacion.ACTUALIZAR);
                    rr.setEcontrado(Boolean.TRUE);
                    break;
                case ACTUALIZAR:                    em.merge(rr.getSocioeconomicos());
                    f.setEntityClass(DatosSocioeconomicos.class);                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "SosioeconomicosR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar SosioeconomicosR (EjbReincorporacion.operacionesSocioeconomicos).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.AcademicosCR> operacionesAcademicosConplementarios(DtoReincorporacion.AcademicosCR rr, Aspirante a) {
        try {
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getComplementarios().setAspirante1(new Aspirante());                    
                    rr.getComplementarios().setAspirante1(a);
                    rr.getComplementarios().setAspirante(a.getIdAspirante());
                    em.persist(rr.getComplementarios());
                    f.setEntityClass(DatosAcademicosComplementarios.class);
                    f.flush();
                    rr.setOperacion(Operacion.ACTUALIZAR);
                    rr.setEcontrado(Boolean.TRUE);
                    break;
                case ACTUALIZAR:                    em.merge(rr.getComplementarios());
                    f.setEntityClass(DatosAcademicosComplementarios.class);                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "AcademicosCR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar AcademicosCR (EjbReincorporacion.operacionesAcademicosConplementarios).", e, null);
        }
    }
    
    public ResultadoEJB<List<Estudiante>> getEstudiantesGrupo(Grupo g) {
        try {
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e INNER JOIN e.grupo g WHERE g.idGrupo=:idGrupo", Estudiante.class).setParameter("idGrupo", g.getIdGrupo()).getResultList();
            if (estudiantes.isEmpty()) {
                estudiantes =new ArrayList<>();
            }
            return ResultadoEJB.crearCorrecto(estudiantes, "Estudiante Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar Estudiante (EjbReincorporacion.getEstudiantesGrupo).", e, null);
        }
    }
    
    public ResultadoEJB<DtoReincorporacion.HistorialTsu> operacionesHistorialTsu(DtoReincorporacion.HistorialTsu rr,DtoReincorporacion.PlanesDeEstudioConsulta pdec,String idPlam,Integer uniId) {
        try {
            em.merge(rr.getHistorialTsu());
            f.setEntityClass(EstudianteHistorialTsu.class);
            f.flush();
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu()"+rr.getHistorialTsu());
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(pdec.getId())"+pdec.getId());
            List<CalificacionesHistorialTsu> calInternas = em.createQuery("SELECT e FROM CalificacionesHistorialTsu e INNER JOIN e.estudianteHistoricoTSU g WHERE g.estudianteHistoricoTSU=:estudianteHistoricoTSU", CalificacionesHistorialTsu.class).setParameter("estudianteHistoricoTSU", rr.getHistorialTsu().getEstudianteHistoricoTSU()).getResultList();
            List<CalificacionesHistorialTsuOtrosPe> calexternas = em.createQuery("SELECT e FROM CalificacionesHistorialTsuOtrosPe e INNER JOIN e.estudianteHistoricoTSU g WHERE g.estudianteHistoricoTSU=:estudianteHistoricoTSU", CalificacionesHistorialTsuOtrosPe.class).setParameter("estudianteHistoricoTSU", rr.getHistorialTsu().getEstudianteHistoricoTSU()).getResultList();

            if (calInternas.isEmpty() && calexternas.isEmpty()) {

                if (idPlam.equals("0-E")) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(0-E)");
                    em.persist(rr.getExternos());
                    f.setEntityClass(PlanesEstudioExternos.class);
                    f.flush();
                    List<PlanesEstudioExternos> pees = f.findAll();
                    CalificacionesHistorialTsuOtrosPe c = new CalificacionesHistorialTsuOtrosPe();
                    c.setEstudianteHistoricoTSU(new EstudianteHistorialTsu());
                    c.setIdplanEstudio(new PlanesEstudioExternos());
                    c.setEstudianteHistoricoTSU(rr.getHistorialTsu());
                    c.setIdplanEstudio(pees.get(pees.size() - 1));
                    c.setValor(0.0);
                    c.setGrado(1);
                    c.setHoras(1);
                    c.setMateria("Nueva Materia");
                    em.persist(c);
                    f.setEntityClass(CalificacionesHistorialTsuOtrosPe.class);
                    f.flush();
                } else if (pdec.getTipo().equals("E")) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(E)");
//                em.merge(rr.getExternos());
//                f.setEntityClass(PlanesEstudioExternos.class);
//                f.flush();
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(1)");
                    PlanesEstudioExternos pees = em.find(PlanesEstudioExternos.class, pdec.getId());
                    CalificacionesHistorialTsuOtrosPe c = new CalificacionesHistorialTsuOtrosPe();
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(2)");
                    c.setEstudianteHistoricoTSU(new EstudianteHistorialTsu());
                    c.setIdplanEstudio(new PlanesEstudioExternos());
                    c.setEstudianteHistoricoTSU(rr.getHistorialTsu());
                    c.setIdplanEstudio(pees);
                    c.setValor(0.0);
                    c.setGrado(1);
                    c.setHoras(1);
                    c.setMateria("Nueva Materia");
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(3)");
                    em.persist(c);
                    f.setEntityClass(CalificacionesHistorialTsuOtrosPe.class);
                    f.flush();
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu(4)");
                }

//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu()"+pdec.getTipo());
            
                if (pdec.getTipo().equals("I")) {
                    List<PlanEstudioMateria> materias = em.createQuery("select e from PlanEstudioMateria e INNER JOIN e.idPlan g WHERE g.idPlanEstudio=:idPlanEstudio", PlanEstudioMateria.class).setParameter("idPlanEstudio", pdec.getId()).getResultList();
                    if (!materias.isEmpty()) {
                        materias.forEach((t) -> {
                            CalificacionesHistorialTsu c = new CalificacionesHistorialTsu();
                            c.setEstudianteHistoricoTSU(new EstudianteHistorialTsu());
                            c.setIdPlanMateria(new PlanEstudioMateria());
                            c.setEstudianteHistoricoTSU(rr.getHistorialTsu());
                            c.setIdPlanMateria(t);
                            c.setValor(0.0);
                            em.persist(c);
                            f.setEntityClass(CalificacionesHistorialTsu.class);
                            f.flush();
                        });
                    }
                }
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu()");
            }
            
            if (!rr.getUniversidadEncontrada()) {
                UniversidadEgresoAspirante aspirante = new UniversidadEgresoAspirante();
                UniversidadEgresoAspirantePK ueapk = new UniversidadEgresoAspirantePK();
                ueapk.setAspirante(rr.getAspirante().getIdAspirante());
                ueapk.setUniversidadEgreso(uniId);

                aspirante.setAspirante1(new Aspirante());
                aspirante.setAspirante1(rr.getAspirante());
                aspirante.setTipoEgreso(rr.getEgresoAspirante().getTipoEgreso());
                aspirante.setUniversidadEgresoAspirantePK(new UniversidadEgresoAspirantePK());
                aspirante.setUniversidadEgresoAspirantePK(ueapk);

                em.persist(aspirante);
                f.setEntityClass(UniversidadEgresoAspirante.class);
                f.flush();
            }else{
                em.merge(rr.getEgresoAspirante());
                f.setEntityClass(UniversidadEgresoAspirante.class);
                f.flush();
            }
            
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.operacionesHistorialTsu()");
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesMedicosR).", e, null);
        }
    }
    public ResultadoEJB<CalificacionesHistorialTsuOtrosPe> operacionesCalificacionesHistorialTsu(CalificacionesHistorialTsuOtrosPe pdec,String tipo) {
        try {
            if(tipo.equals("A")){
                em.merge(pdec);
            }else{
                em.persist(pdec);
            }
            f.setEntityClass(CalificacionesHistorialTsuOtrosPe.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(pdec, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesMedicosR).", e, null);
        }
    }
    public ResultadoEJB<CalificacionesHistorialTsu> operacionesCalificacionestsuotrasaiiut(CalificacionesHistorialTsu pdec) {
        try {
            em.persist(pdec);
            f.setEntityClass(CalificacionesHistorialTsu.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(pdec, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.operacionesMedicosR).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.ReporteReincorporaciones>> getReporteReincorporacionesPorEstudiante(Integer planc) {
        try {
            List<String> reincorporaciones=new ArrayList<>();
            reincorporaciones.add("Cambio de grupo");
            reincorporaciones.add("Cambio de plan de estudio");
            reincorporaciones.add("Cambio de programa educativo");
            reincorporaciones.add("Equivalencia");
            reincorporaciones.add("Reincorporación otra UT");
            reincorporaciones.add("Reincorporación otra generación");
            reincorporaciones.add("Reincorporación misma UT");
            List<DtoReincorporacion.ReporteReincorporaciones> reincorporacioneses= new ArrayList<>();
            List<Estudiante> matr = em.createQuery("select t from Estudiante t INNER JOIN t.aspirante a INNER JOIN a.idPersona p INNER JOIN t.grupo g INNER JOIN g.plan pe WHERE pe.idPlanEstudio=:idPlanEstudio AND t.tipoRegistro in :reincorporaciones GROUP BY t.aspirante ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, t.periodo", Estudiante.class)
                    .setParameter("reincorporaciones", reincorporaciones)
                    .setParameter("idPlanEstudio", planc)
                    .getResultList();
            List<Integer> aspi = new ArrayList<>();
            matr.forEach((t) -> {
                aspi.add(t.getAspirante().getIdAspirante());
            });
             aspi.forEach((t) -> {
                 List<Estudiante> estudiantes = em.createQuery("select t from Estudiante t INNER JOIN t.aspirante a INNER JOIN a.idPersona p WHERE a.idAspirante=:idAspirante ORDER BY t.periodo DESC", Estudiante.class)
                        .setParameter("idAspirante", t)
                        .getResultList();
                 Estudiante e=estudiantes.get(0);
                 reincorporacioneses.add(new DtoReincorporacion.ReporteReincorporaciones(e.getAspirante().getIdPersona(), e.getAspirante(), e, e.getTipoEstudiante(), e.getGrupo().getPlan(),  new ArrayList<>()));
             });
             
            reincorporacioneses.forEach((t) -> {
                List<Estudiante> estudiantes = em.createQuery("select t from Estudiante t INNER JOIN t.aspirante a INNER JOIN a.idPersona p WHERE a.idAspirante=:idAspirante ORDER BY t.periodo DESC", Estudiante.class)
                        .setParameter("idAspirante", t.getAspirante().getIdAspirante())
                        .getResultList();
                List<DtoReincorporacion.HistoricoReincorporaciones> historicoReincorporacioneses = new ArrayList<>();
                estudiantes.forEach((e) -> {
                    DtoReincorporacion.HistoricoReincorporaciones rr = new DtoReincorporacion.HistoricoReincorporaciones(e, e.getTipoEstudiante(), e.getGrupo(), e.getGrupo().getPlan(), "", 0, e.getGrupo().getCargaAcademicaList().size(), Boolean.FALSE);
                    List<CalificacionPromedio> calificacionPromedios = em.createQuery("select t from CalificacionPromedio t INNER JOIN t.estudiante c WHERE c.idEstudiante=:idEstudiante AND t.valor >= :calificacion", CalificacionPromedio.class).setParameter("idEstudiante", e.getIdEstudiante()).setParameter("calificacion", 8.0).getResultList();
                    List<CalificacionNivelacion> calificacionNivelacions = em.createQuery("select t from CalificacionNivelacion t INNER JOIN t.estudiante c WHERE c.idEstudiante=:idEstudiante AND t.valor >= :calificacion", CalificacionNivelacion.class).setParameter("idEstudiante", e.getIdEstudiante()).setParameter("calificacion", 8.0).getResultList();
                    List<CalificacionPromedio> tipoCalificacion = em.createQuery("SELECT t FROM CalificacionPromedio t INNER JOIN t.estudiante c WHERE c.idEstudiante=:idEstudiante GROUP BY t.tipo", CalificacionPromedio.class).setParameter("idEstudiante", e.getIdEstudiante()).setParameter("calificacion", 8.0).getResultList();
                    if (!tipoCalificacion.isEmpty()) {
                        rr.setTipoCalificacion(tipoCalificacion.get(0).getTipo());
                    }
                    if (e.getGrupo().getCargaAcademicaList().size() == calificacionPromedios.size()) {
                        rr.setCompleto(Boolean.TRUE);
                        rr.setCalificacionesRegistradas(calificacionPromedios.size());
                    } else if (e.getGrupo().getCargaAcademicaList().size() == (calificacionPromedios.size() + calificacionNivelacions.size())) {
                        rr.setCompleto(Boolean.TRUE);
                        rr.setCalificacionesRegistradas((calificacionPromedios.size() + calificacionNivelacions.size()));
                    } else if (e.getTipoEstudiante().getDescripcion().contains("Baja")) {
                        rr.setCompleto(Boolean.TRUE);
                        rr.setCalificacionesRegistradas(e.getGrupo().getCargaAcademicaList().size());
                    }
                    historicoReincorporacioneses.add(rr);
                });
                t.setHistoricoReincorporacioneses(historicoReincorporacioneses);
            });
             
            return ResultadoEJB.crearCorrecto(reincorporacioneses, "getReporteReincorporacionesPorEstudiante Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el getReporteReincorporacionesPorEstudiante (EjbReincorporacion.getReporteReincorporacionesPorEstudiante).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.HistoricoReincorporaciones>> getHistorialInscripciones(Aspirante a) {
        try {
            List<Estudiante> estudiantes = em.createQuery("select t from Estudiante t INNER JOIN t.aspirante a INNER JOIN a.idPersona p WHERE a.idAspirante=:idAspirante ORDER BY t.periodo DESC", Estudiante.class)
                    .setParameter("idAspirante", a.getIdAspirante())
                    .getResultList();
            List<DtoReincorporacion.HistoricoReincorporaciones> historicoReincorporacioneses = new ArrayList<>();
            estudiantes.forEach((e) -> {
                DtoReincorporacion.HistoricoReincorporaciones rr = new DtoReincorporacion.HistoricoReincorporaciones(e, e.getTipoEstudiante(), e.getGrupo(), e.getGrupo().getPlan(), "", 0, e.getGrupo().getCargaAcademicaList().size(), Boolean.FALSE);
                List<CalificacionPromedio> calificacionPromedios = em.createQuery("select t from CalificacionPromedio t INNER JOIN t.estudiante c WHERE c.idEstudiante=:idEstudiante AND t.valor >= :calificacion", CalificacionPromedio.class).setParameter("idEstudiante", e.getIdEstudiante()).setParameter("calificacion", 8.0).getResultList();
                List<CalificacionNivelacion> calificacionNivelacions = em.createQuery("select t from CalificacionNivelacion t INNER JOIN t.estudiante c WHERE c.idEstudiante=:idEstudiante AND t.valor >= :calificacion", CalificacionNivelacion.class).setParameter("idEstudiante", e.getIdEstudiante()).setParameter("calificacion", 8.0).getResultList();
                List<CalificacionPromedio> tipoCalificacion = em.createQuery("SELECT t FROM CalificacionPromedio t INNER JOIN t.estudiante c WHERE c.idEstudiante=:idEstudiante GROUP BY t.tipo", CalificacionPromedio.class).setParameter("idEstudiante", e.getIdEstudiante()).getResultList();
                if(!tipoCalificacion.isEmpty()){
                    rr.setTipoCalificacion(tipoCalificacion.get(0).getTipo());
                }
                if (e.getGrupo().getCargaAcademicaList().size() == calificacionPromedios.size()) {
                    rr.setCompleto(Boolean.TRUE);
                    rr.setCalificacionesRegistradas(calificacionPromedios.size());
                } else if (e.getGrupo().getCargaAcademicaList().size() == (calificacionPromedios.size() + calificacionNivelacions.size())) {
                    rr.setCompleto(Boolean.TRUE);
                    rr.setCalificacionesRegistradas((calificacionPromedios.size() + calificacionNivelacions.size()));
                } else if (e.getTipoEstudiante().getDescripcion().contains("Baja")) {
                    rr.setCompleto(Boolean.TRUE);
                    rr.setCalificacionesRegistradas(e.getGrupo().getCargaAcademicaList().size());
                }
                historicoReincorporacioneses.add(rr);
            });
             
            return ResultadoEJB.crearCorrecto(historicoReincorporacioneses, "getHistorialInscripciones Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el getHistorialInscripciones (EjbReincorporacion.getHistorialInscripciones).", e, null);
        }
    }
    
    public PeriodosEscolares buscaPeriodo(Integer idP) {
         return em.find(PeriodosEscolares.class, idP);
    }
}