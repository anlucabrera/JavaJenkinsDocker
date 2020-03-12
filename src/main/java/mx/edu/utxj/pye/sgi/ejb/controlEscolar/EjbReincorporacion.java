package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
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
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistencias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionPromedio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionPromedioPK;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Escolaridad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EspecialidadCentro;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Ocupacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Sistema;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionDetalle;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
    
    public ResultadoEJB<List<TipoSangre>> getTiposSangre() {
        try {
            List<TipoSangre> sangres = em.createQuery("select t from TipoSangre t", TipoSangre.class).getResultList();
            return ResultadoEJB.crearCorrecto(sangres, "Tipos de sangre Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar los Tipos de Sangre(EjbReincorporacion.getTiposSangre).", e, null);
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
    
    public ResultadoEJB<List<Grupo>> getGrupos(Short pe) {
        try {
            List<Grupo> grupos = em.createQuery("select t from Grupo t WHERE t.idPe=:idPe", Grupo.class).setParameter("idPe", pe).getResultList();
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
            DtoReincorporacion.TutorR tr = new DtoReincorporacion.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE);
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
                            if (resFR.getCorrecto()) {fr = resFR.getValor();if(fr.getEcontrado()){tr=fr.getTutorR();}}
                            if (resAC.getCorrecto()) {ac = resAC.getValor();}
                            if (resDR.getCorrecto()) {dr = resDR.getValor();}
                            if (resER.getCorrecto()) {er = resER.getValor();}  
                            if (resES.getCorrecto()) {
                                es = resES.getValor();
                                if(!es.isEmpty()){
                                    ResultadoEJB<List<DtoReincorporacion.AlineacionCalificaciones>> resAl = getAlineacionCalificaciones(ar.getAspirante(),esEscolares);
                                    if (resAl.getCorrecto()) {al = resAl.getValor();}
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.FamiliaresR> getFamiliaresR(Aspirante a) {
        try {
            List<DatosFamiliares> datosFamiliareses = em.createQuery("select a from DatosFamiliares a INNER JOIN a.aspirante1 p WHERE p.idAspirante=:idAspirante", DatosFamiliares.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            DtoReincorporacion.TutorR tr = new DtoReincorporacion.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.TutorR> getTutorR(DatosFamiliares tutor) {
        try {
            List<TutorFamiliar> familiars = em.createQuery("select t from DatosFamiliares d INNER JOIN d.tutor t WHERE d.aspirante=:aspirante", TutorFamiliar.class).setParameter("aspirante", tutor.getAspirante()).getResultList();
            DtoReincorporacion.TutorR rr = new DtoReincorporacion.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE);
            if (!familiars.isEmpty()) {
                TutorFamiliar tf = familiars.get(0);
                rr.setTutorFamiliar(tf);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoReincorporacion.EstudianteR>> getEstudianteR(Aspirante a){
        try {
            List<Estudiante> das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante ORDER BY e.periodo", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            List<DtoReincorporacion.EstudianteR> ers = new ArrayList<>();
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
    
    public ResultadoEJB<List<DtoReincorporacion.AlineacionCalificaciones>> getAlineacionCalificaciones(Aspirante a, Boolean esEscolares) {
        try {
            List<Estudiante> das = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            List<DtoReincorporacion.AlineacionCalificaciones> rr = new ArrayList<>();
            if (!das.isEmpty()) {
                das.forEach((t) -> {
                    List<DtoReincorporacion.CalificacionesR> crs = new ArrayList<>();
                    List<CargaAcademica> cas = new ArrayList<>();
                    Boolean validar = Boolean.FALSE;
                    if (!t.getGrupo().getCargaAcademicaList().isEmpty()) {
                        cas = t.getGrupo().getCargaAcademicaList();
                        cas.forEach((cg) -> {
                            List<CalificacionPromedio> cps = em.createQuery("select d from CalificacionPromedio d INNER JOIN d.estudiante a INNER JOIN d.cargaAcademica c WHERE a.idEstudiante=:idEstudiante AND c.carga=:carga", CalificacionPromedio.class).setParameter("idEstudiante", t.getIdEstudiante()).setParameter("carga", cg.getCarga()).getResultList();
                            List<CalificacionNivelacion> cns = em.createQuery("select d from CalificacionNivelacion d INNER JOIN d.estudiante a INNER JOIN d.cargaAcademica c WHERE a.idEstudiante=:idEstudiante AND c.carga=:carga", CalificacionNivelacion.class).setParameter("idEstudiante", t.getIdEstudiante()).setParameter("carga", cg.getCarga()).getResultList();
                            Personal p = new Personal();
                            CalificacionPromedio cp = new CalificacionPromedio();
                            CalificacionNivelacion cn = new CalificacionNivelacion();

                            try {
                                p = ejbPersonal.mostrarPersonalLogeado(cg.getDocente());
                            } catch (Throwable ex) {
                                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                            }
                            Boolean editable = Boolean.TRUE;
                            Boolean ordinaria = Boolean.TRUE;
                            if (!cps.isEmpty()) {
                                cp = cps.get(0);
                                if (!cns.isEmpty()) {
                                    cn = cns.get(0);
                                    if (cp.getValor() > cn.getValor()) {
                                        ordinaria = Boolean.TRUE;
                                    } else {
                                        cp.setValor(cn.getValor());
                                        ordinaria = Boolean.FALSE;
                                    }
                                } else {
                                    ordinaria = Boolean.TRUE;
                                }
                                if (cp.getTipo().equals("Oficial")) {
                                    editable = Boolean.FALSE;
                                } else if (esEscolares && (t.getTipoRegistro().equals("Regularización de calificaciones por reincoporación") || t.getTipoRegistro().equals("Cambio de programa educativo") || t.getTipoRegistro().equals("Cambio de plan de estudio"))) {
                                    editable = Boolean.FALSE;
                                } else if (!esEscolares && (t.getTipoRegistro().equals("Regularización de calificaciones por reincoporación") || t.getTipoRegistro().equals("Cambio de programa educativo") || t.getTipoRegistro().equals("Cambio de plan de estudio"))) {
                                    editable = Boolean.TRUE;
                                } else {
                                    editable = esEscolares;
                                }
                                crs.add(new DtoReincorporacion.CalificacionesR(cg, cg.getIdPlanMateria(), cg.getIdPlanMateria().getIdMateria(), p, cp, editable, ordinaria, Operacion.PERSISTIR, Boolean.FALSE));
                            } else {
                                
                                if (!esEscolares && (t.getTipoRegistro().equals("Regularización de calificaciones por reincoporación") || t.getTipoRegistro().equals("Cambio de programa educativo") || t.getTipoRegistro().equals("Cambio de plan de estudio"))) {
                                    editable = Boolean.TRUE;
                                } else if (t.getTipoRegistro().equals("Regularización de calificaciones por reincoporación") && esEscolares) {
                                    editable = Boolean.FALSE;
                                } else {
                                    editable = esEscolares;
                                }
                                crs.add(new DtoReincorporacion.CalificacionesR(cg, cg.getIdPlanMateria(), cg.getIdPlanMateria().getIdMateria(), p, new CalificacionPromedio(), editable, ordinaria, Operacion.ACTUALIZAR, Boolean.TRUE));
                            }
                        });
                    }
                    if (crs.stream().filter(cal -> cal.getCalificacionPromedio().getValor() < 8D).collect(Collectors.toList()).isEmpty()) {
                        validar = Boolean.TRUE;
                    } else {
                        validar = Boolean.FALSE;
                    }
                    rr.add(new DtoReincorporacion.AlineacionCalificaciones(t, t.getGrupo(), crs, validar, Operacion.ACTUALIZAR, Boolean.TRUE));
                });
            }
            return ResultadoEJB.crearCorrecto(rr, "EstudianteR Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar EstudianteR (EjbReincorporacion.getAlineacionCalificaciones).", e, null);
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
            List<ProcesosInscripcion> pis = em.createQuery("select p from ProcesosInscripcion p WHERE (:fecha BETWEEN p.fechaInicio AND p.fechaFin)", ProcesosInscripcion.class).setParameter("fecha", new Date()).getResultList().stream().filter(t-> t.getActivoRe()==Boolean.TRUE).collect(Collectors.toList());;
            ProcesosInscripcion pi=new ProcesosInscripcion();
                        if(!pis.isEmpty()){
                pi=pis.get(0);                
            }
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    public ResultadoEJB<DtoReincorporacion.TutorR> operacionesTutorR(DtoReincorporacion.TutorR rr) {
        try {
            switch (rr.getOperacion()) {
                case PERSISTIR:  em.persist(rr.getTutorFamiliar());    f.setEntityClass(TutorFamiliar.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR);break;
                case ACTUALIZAR: em.merge(rr.getTutorFamiliar());  f.setEntityClass(TutorFamiliar.class); f.flush();  break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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

    public ResultadoEJB<DtoReincorporacion.ProcesoInscripcionRein> operacionesEstudianteR(DtoReincorporacion.ProcesoInscripcionRein rr,Aspirante a) {
        try {
            if (!rr.getGrupos().isEmpty()) {              
                rr.getGrupos().forEach((t) -> {
                    Operacion operacion;
                    List<Estudiante> es = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN e.grupo g WHERE a.idAspirante=:idAspirante AND e.periodo=:periodo", Estudiante.class).setParameter("idAspirante", a.getIdAspirante()).setParameter("periodo", t.getPeriodo()).getResultList();
                    Estudiante e = new Estudiante();
                    if (!es.isEmpty()) {
                        e=es.get(0);
                        e.setGrupo(new Grupo());
                        e.setGrupo(t);
                        operacion=Operacion.ACTUALIZAR;
                    } else {
                        e.setAspirante(new Aspirante());
                        e.setTipoEstudiante(new TipoEstudiante());
                        e.setGrupo(new Grupo());
                        e.setAspirante(a);
                        e.setTipoEstudiante(new TipoEstudiante(Short.parseShort("1")));
                        e.setGrupo(t);
                        e.setFechaAlta(new Date());
                        operacion = Operacion.PERSISTIR;
                    }
                    e.setPeriodo(t.getPeriodo());
                    e.setCarrera(t.getIdPe());
                    e.setTrabajadorInscribe(rr.getTrabajadorInscribe());
                    e.setMatricula(rr.getMatricula());                    
                    e.setOpcionIncripcion(rr.getOpcionIncripcion());
                    e.setTipoRegistro(rr.getTipoRegistro());
                    
                    switch (operacion) {
                        case PERSISTIR:
                            em.persist(e);
                            f.setEntityClass(Estudiante.class);
                            f.flush();
                            break;
                        case ACTUALIZAR:
                            em.merge(e);
                            f.setEntityClass(Estudiante.class);
                            f.flush();
                            break;
                    } 
                });
                List<Estudiante> es = em.createQuery("select e from Estudiante e WHERE e.matricula=:matricula ORDER BY e.periodo", Estudiante.class).setParameter("matricula", rr.getMatricula()).getResultList();
                Estudiante e = new Estudiante();
                if (!es.isEmpty()) {
                    e = es.get(es.size() - 1);
//                    asistenciasRegistradas(e, Operacion.PERSISTIR);
                }
                operacionesDocumentosentregadosestudiante(rr);
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }
    
    public ResultadoEJB<CalificacionPromedio> registrarCalificacionesPorPromedio(Integer idEstudiante, Integer idCarga, Double calificacion, String tipo) {
        try {
            Operacion opera;
            Estudiante estudiante = em.find(Estudiante.class, idEstudiante);
            CargaAcademica cargaAcademica = em.find(CargaAcademica.class, idCarga);
            List<CalificacionPromedio> cps = em.createQuery("select calP from CalificacionPromedio calP INNER JOIN calP.cargaAcademica c INNER JOIN calP.estudiante est WHERE c.carga=:carga AND est.idEstudiante=:idEstudiante", CalificacionPromedio.class).setParameter("carga", cargaAcademica.getCarga()).setParameter("idEstudiante", estudiante.getIdEstudiante()).getResultList();
            List<UnidadMateriaConfiguracionDetalle> cs = em.createQuery("select cs from UnidadMateriaConfiguracionDetalle cs INNER JOIN cs.configuracion con INNER JOIN con.carga c WHERE c.carga=:carga", UnidadMateriaConfiguracionDetalle.class).setParameter("carga", cargaAcademica.getCarga()).getResultList();
        
            CalificacionPromedio promedio = new CalificacionPromedio();
            if (!cps.isEmpty()) {
                promedio = cps.get(0);
                opera=Operacion.ACTUALIZAR;
            } else {
                promedio = new CalificacionPromedio();
                CalificacionPromedioPK pK = new CalificacionPromedioPK();                
                pK.setCarga(idCarga);
                pK.setIdEstudiante(idEstudiante);
                
                promedio.setCalificacionPromedioPK(new CalificacionPromedioPK());
                promedio.setCargaAcademica(new CargaAcademica());
                promedio.setEstudiante(new Estudiante());
                
                promedio.setCalificacionPromedioPK(pK);
                promedio.setCargaAcademica(cargaAcademica);
                promedio.setEstudiante(estudiante);
                opera=Operacion.PERSISTIR;
            }
            promedio.setFechaActualizacion(new Date());
            promedio.setTipo(tipo);
            promedio.setValor(calificacion);
            switch (opera) {
                case PERSISTIR:
                    em.persist(promedio);
                    em.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(promedio);
                    em.flush();
                    break;
            }
            if (!cs.isEmpty()) {
                cs.forEach((t) -> {
                    Operacion op;
                    List<Calificacion> cal = em.createQuery("select calP from Calificacion calP INNER JOIN calP.configuracionDetalle c INNER JOIN calP.idEstudiante est WHERE c.configuracionDetalle=:configuracionDetalle AND est.idEstudiante=:idEstudiante", Calificacion.class).setParameter("configuracionDetalle", t.getConfiguracionDetalle()).setParameter("idEstudiante", estudiante.getIdEstudiante()).getResultList();
                    Calificacion c=new Calificacion();
                    if (!cal.isEmpty()) {
                        op = Operacion.PERSISTIR;
                        c=cal.get(0);
                    } else {
                        op = Operacion.PERSISTIR;
                        c=new Calificacion();
                        c.setConfiguracionDetalle(new UnidadMateriaConfiguracionDetalle());
                        c.setIdEstudiante(new Estudiante());
                        
                        c.setConfiguracionDetalle(t);
                        c.setIdEstudiante(estudiante);
                    }
                    c.setValor(calificacion);
                    switch (op) {
                        case PERSISTIR:
                            em.persist(c);
                            em.flush();
                            break;
                        case ACTUALIZAR:
                            em.merge(c);
                            em.flush();
                            break;
                    }
                });
            }
            
        return ResultadoEJB.crearCorrecto(promedio, "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }   
    
    public void enviarConfirmacionCorreoElectronico(String correoDestino, String titulo, String asunto, String mensaje) {
        // El correo gmail de envío
        String correoEnvia = "sistemas@utxicotepec.edu.mx";//correo del arrea de desarrollo
        String claveCorreo = "piccoto2018";//contraseña del correo del arrea de desarrollo
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
}
