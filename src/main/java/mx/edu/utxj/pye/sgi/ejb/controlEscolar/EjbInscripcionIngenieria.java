package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspiranteIng;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupo;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.UniversidadesUT;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import mx.edu.utxj.pye.sgi.util.Encrypted;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;

import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "EjbInscripcionIngenieria")
public class EjbInscripcionIngenieria {
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbCitaInscripcionAspirante ejbCita;
    @EJB EjbRegistroFichaIngenieria ejbRegistroFichaIngenieria;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;

    private EntityManager em;
    private  EntityManager emS;

    @PostConstruct
    public void init() {
        em = f.getEntityManager(); emS = f2.getEntityManager();
    }


    /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
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
    /**
     * Verifica el evento de inscripcion Ingeniería
     * @return
     */

    public ResultadoEJB<EventoEscolar> verficarEvento(){
        try {
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.INSCRIPCION_INGENIERIA);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision de ingeniería (EjbResgitroFichaIngenieria.verificaEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }

    /**
     * Obtiene el proceso de inscripción de Ingeniería activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProcesosInscripcion> getProcesosInscripcionActivo() {
        try {
            ProcesosInscripcion procesosInscripcion = new ProcesosInscripcion();
            procesosInscripcion = em.createQuery("select p from ProcesosInscripcion p where :fecha between p.fechaInicio and p.fechaFin and p.activoIng=:tipo", ProcesosInscripcion.class)
                    .setParameter("fecha", new Date())
                    .setParameter("tipo",true)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (procesosInscripcion == null) {
                return ResultadoEJB.crearErroneo(2, procesosInscripcion, "No se encontro proceso de inscripción");
            } else {
                return ResultadoEJB.crearCorrecto(procesosInscripcion, "Proceso de incripción activo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el proceso de inscripcion activo(EjbResgitroFichaIngenieria.getProcesosInscripcionActivo).", e, null);
        }
    }

    /**
     * Obtiene el tramite de inscripcion de ingeniría que se uso para agendar las citas
     * @param procesosInscripcion Proceso de inscripcion activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<TramitesEscolares> getTramiteInscripcion(@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new TramitesEscolares(),"El proceso de inscripción no debe ser nulo");}
            return ejbRegistroFichaIngenieria.getTramiteInscripcionActivo(procesosInscripcion);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el tramite de inscripción  de ing/lic(EjbResgitroFichaIngenieria.getTramiteInscripcion).", e, null);

        }
    }

    /**
     * Obtiene la lista de aspirantes validados de ingeniería por proceso de inscripcion
     * @param procesosInscripcion Proceso de inscripcion ing activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Aspirante>> getAspirantesIngbyProceso(@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El proceso de inscripción no debe ser nulo");}
            List<Aspirante> aspirantes = new ArrayList<>();
            aspirantes = em.createQuery("select a from Aspirante a where a.tipoAspirante.idTipoAspirante=:tipo and  a.idProcesoInscripcion.idProcesosInscripcion=:proceso and a.estatus=true order by a.folioAspirante desc ", Aspirante.class)
            .setParameter("tipo",3)
            .setParameter("proceso",procesosInscripcion.getIdProcesosInscripcion())
            .getResultList()
            ;
            if(aspirantes==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No se encontraron aspirantes");}
            else {return ResultadoEJB.crearCorrecto(aspirantes,"Lista de aspirantes");}
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de aspirantes (EjbResgitroFichaIngenieria.getAspirantesIngbyProceso).", e, null);
        }
    }

    /**
     * Obtiene el resgistro del estudiante egresado por CURP
     * @param aspirante Aspirante registrado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> getEstudianteCEbyCurp (@NonNull Aspirante aspirante, @NonNull EventoEscolar eventoEscolar){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new Estudiante(),"El aspirante no debe ser nulo");}
            int periodo = eventoEscolar.getPeriodo()-1;
            Estudiante estudiante = new Estudiante();
            estudiante = em.createQuery("select e from Estudiante e where e.aspirante.idPersona.curp=:curp and e.periodo=:periodo order by  e.periodo desc ", Estudiante.class)
            .setParameter("curp",aspirante.getIdPersona().getCurp())
                    .setParameter("periodo", periodo)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;

            if(estudiante==null){return ResultadoEJB.crearErroneo(3,new Estudiante(),"No se encontro al estudiante");}
            else {return ResultadoEJB.crearCorrecto(estudiante,"Estudiante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener estudiante (EjbInscripcionIngenieria.getEstudianteCEbyCurp).", e, null);
        }
    }



    /**
     * Obtiene los datos de medios de comunicación por aspirante
     * @param aspirante
     * @return
     */
    public ResultadoEJB<MedioComunicacion> getMedioComunicacionbyAspirante(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new MedioComunicacion(),"El aspirante no debe ser nulo");}
           MedioComunicacion medioComunicacion = new MedioComunicacion();
            medioComunicacion = em.createQuery("select m from MedioComunicacion m where m.persona=:per", MedioComunicacion.class)
            .setParameter("per",aspirante.getIdPersona().getIdpersona())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(medioComunicacion==null){return ResultadoEJB.crearErroneo(3,new MedioComunicacion(),"No se encontraron medios de comunicación");}
            else {return ResultadoEJB.crearCorrecto(medioComunicacion,"Medios de comunicación");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los medios de comunicacion por aspirante (EjbInscripcionIngenieria.getEstudianteCEbyCurp).", e, null);

        }
    }

    /**
     * Obtiene los datos académicos del aspirante
     * @param aspirante Aspirante de ing/Lic
     * @return Resultado del proceso
     */
    public ResultadoEJB<DatosAcademicos> getAcademicosbyAspirante(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new DatosAcademicos(),"El aspirante no debe ser nulo");}
            DatosAcademicos da= new DatosAcademicos();
            da= em.createQuery("select d from DatosAcademicos d where d.aspirante=:aspirante", DatosAcademicos.class)
            .setParameter("aspirante",aspirante.getIdAspirante())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(da!=null){return ResultadoEJB.crearCorrecto(da,"Datos académicos");}
            else {return ResultadoEJB.crearErroneo(3,new DatosAcademicos(),"No se encontraron datos académicos");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los datos academicos por aspirante (EjbInscripcionIngenieria.getAcademicosbyAspirante).", e, null);

        }
    }
    /**
     * Obtiene los datos medicos del aspirante
     * @param aspirante Aspirante de ing/Lic
     * @return Resultado del proceso
     */
    public ResultadoEJB<DatosMedicos> getMedicosbyAspirante(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new DatosMedicos(),"El aspirante no debe ser nulo");}
            DatosMedicos da= new DatosMedicos();
            da= em.createQuery("select m from DatosMedicos m where m.persona.idpersona=:per", DatosMedicos.class)
                    .setParameter("per",aspirante.getIdPersona().getIdpersona())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(da!=null){return ResultadoEJB.crearCorrecto(da,"Datos académicos");}
            else {return ResultadoEJB.crearErroneo(3,new DatosMedicos(),"No se encontraron datos médicos");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los datos academicos por aspirante (EjbInscripcionIngenieria.getAcademicosbyAspirante).", e, null);

        }
    }

    /**
     * Obtiene la universidad de egreso del aspirante de ing/lic
     * @param aspirante aspirante de ing /lic
     * @return Resultado  del proceso
     */
    public ResultadoEJB<UniversidadesUT> getUniversidadbyAspirante(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(3,new UniversidadesUT(),"El Aspirante no debe ser nulo");}
            ResultadoEJB<UniversidadEgresoAspirante> resU= ejbRegistroFichaIngenieria.getUniversidadbyAspirante(aspirante);
            if(resU.getCorrecto()){
                return ejbRegistroFichaIngenieria.getUniversidadbyAspirante(resU.getValor());
            }else {return ResultadoEJB.crearErroneo(2,new UniversidadesUT(),"Error al obtener la universidad de egreso del aspirante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la universidad de egreso del  aspirante (EjbInscripcionIngenieria.getUniversidadbyAspirante).", e, null);

        }
    }


    /**
     * Obtiene el estudiante inscrito en el periodo programado
     * @param eventoInscripcion Evento de inscripcion de ing/lic
     * @param aspirante aspirante ing registrado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> getEstudiantebyPeriodo(@NonNull EventoEscolar eventoInscripcion, @NonNull Aspirante aspirante){
        try{
            if(eventoInscripcion==null){return ResultadoEJB.crearErroneo(2,new Estudiante(),"El evento no debe ser nulo");}
            if(aspirante==null){return ResultadoEJB.crearErroneo(3,new Estudiante(),"El Aspirante no debe ser nulo");}
           Estudiante estudiante = new Estudiante();
            estudiante = em.createQuery("select e from Estudiante  e where e.aspirante.idAspirante=:aspirante and e.periodo=:periodo", Estudiante.class)
            .setParameter("aspirante", aspirante.getIdAspirante())
            .setParameter("periodo",eventoInscripcion.getPeriodo())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new Estudiante(),"El estudiante no ha sido inscrito en el periodo programado");}
            else {return ResultadoEJB.crearCorrecto(estudiante,"Estudiante inscrito");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los medios de comunicacion por aspirante (EjbInscripcionIngenieria.getEstudianteCEbyCurp).", e, null);

        }
    }

    /**
     * Obtiene el programa por clave
     * @param clave Clave del programa educativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getPEbyClave(@NonNull Short  clave){
        try{
            if(clave==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del programa educativo no debe ser nulo");}
            AreasUniversidad a = new AreasUniversidad();
            a= em.createQuery("select a from AreasUniversidad  a where a.area=:area", AreasUniversidad.class)
            .setParameter("area",clave)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(a!=null){return ResultadoEJB.crearCorrecto(a,"Programa educativo");}
            else {return ResultadoEJB.crearErroneo(3,a, "No se encontro programa educativo");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el programa educativo por clave(EjbInscripcionIngenieria.getPEbyClave).", e, null);

        }
    }

    /**
     * Obtiene la lista de grupos de 7mo por programa educativo
     * @param pe Programa educatuvo
     * @param eventoEscolar Evento escolar de inscripcion de ingenieria
     * @return
     */

    public ResultadoEJB<List<Grupo>> getGruposbyPE(@NonNull AreasUniversidad pe, @NonNull Sistema sistema, @NonNull EventoEscolar eventoEscolar){
        try{
            if(pe==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El programa educativo no debe ser nula");}
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El periodo no debe ser nula");}
            List<Grupo> grupos= new ArrayList<>();
            grupos = em.createQuery("select g from Grupo g where g.idPe=:pe  and g.idSistema.idSistema=:sistema and g.periodo=:periodo and g.grado=:grado order by g.literal asc", Grupo.class)
            .setParameter("pe", pe.getArea())
            .setParameter("sistema", sistema.getIdSistema())
            .setParameter("periodo", eventoEscolar.getPeriodo())
            .setParameter("grado",7)
            .getResultList()
            ;
            if(grupos.isEmpty() || grupos==null){return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "No existen grupos");}
            else {return ResultadoEJB.crearCorrecto(grupos,"Lista de grupos");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los grupos(EjbInscripcionIngenieria.getGruposbyPE).", e, null);

        }
    }
    /**
     * Obtiene los possibes grupos por programa educativo y los empaqueta
     * @param eventoEscolar evento activo para inscripción
     * @param aspirante Aspirante validado
     * @param pe Programa educativo
     * @param datosAcademicos datos académicos del aspirante
     * @return Resultado del proceso (Lista de posibles grupos )
     */
    public ResultadoEJB<List<DtoGrupo>> getGruposbyOpcion(@NonNull EventoEscolar eventoEscolar, @NonNull Aspirante aspirante, @NonNull AreasUniversidad pe, @NonNull DatosAcademicos datosAcademicos, @NonNull Sistema sistemaSeleccionado) {
        try{
            List<DtoGrupo> grupos= new ArrayList<>();
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2,grupos,"El evento no debe ser nulo");}
            if(aspirante==null){return ResultadoEJB.crearErroneo(3,grupos,"El aspirante no debe ser nulo");}
            if(pe==null){return ResultadoEJB.crearErroneo(4,grupos,"Los datos académicos no deben ser nulos"); }
            if(sistemaSeleccionado==null){return ResultadoEJB.crearErroneo(2,grupos,"El sistema no debe ser nulo");}

            //Se obtienen grupos de la primera opción
            ResultadoEJB<List<Grupo>> resGruposPO = getGruposbyPE(pe,sistemaSeleccionado,eventoEscolar);
            if(resGruposPO.getCorrecto()==true){
                //Se recorren los grupos para poder empaquetarlos
                resGruposPO.getValor().forEach(g->{
                    //Se empaquetan los grupos
                    ResultadoEJB<DtoGrupo> resPack= ejbProcesoInscripcion.packGrupo(g);
                    if(resPack.getCorrecto()==true){
                        grupos.add(resPack.getValor());
                    }
                });
                return ResultadoEJB.crearCorrecto(grupos,"Opcion de grupos");
            }else {return ResultadoEJB.crearErroneo(5,grupos,"No existen grupos creados del programa "+ pe.getNombre());}
        }catch (Exception e){return  ResultadoEJB.crearErroneo(1, "Error al obtener los posibles grupos(EJBProcesoInscripcion.getGruposbyPe).", e, null);}
    }

    /**
     * Obtiene la lista de documentos que debe entregar el estudiante segun el proceso
     * @param proceso Proceso (Inscripcion - ET (Egresado titulado) ENT- (Egresado no titulado)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DocumentoProceso>> getDocumentosbyProceso(@NonNull String proceso){
        try{
            if(proceso== null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El tipo de proceso no deb e ser nulo");}
            List<DocumentoProceso> documentos = new ArrayList<>();
            documentos = em.createQuery("select  d from DocumentoProceso d where d.proceso=:proceso and d.obligatorio=:activo", DocumentoProceso.class)
            .setParameter("proceso", proceso)
            .setParameter("activo",true)
            .getResultList()
            ;
            if(documentos.isEmpty() || documentos==null){return ResultadoEJB.crearErroneo(2, new ArrayList<>(),"No se encontraron documentos del proceso");}
            else {return  ResultadoEJB.crearCorrecto(documentos,"Documentos entregados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los grupos(EjbInscripcionIngenieria.getDocumentosbyProceso).", e, null);
        }

    }

    /**
     * Obtiene la lista de documentos entregados del estudiante
     * @param estudiante Estudiante inscrito
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DocumentoEstudianteProceso>> getDocumentosbyEstudiante(@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2, new ArrayList<>(),"El estudiante no debe ser nulo");}
            List<DocumentoEstudianteProceso> documentosEstudiante= new ArrayList<>();
            documentosEstudiante = em.createQuery("select d from DocumentoEstudianteProceso d where d.estudiante.idEstudiante=:estudiante", DocumentoEstudianteProceso.class)
            .setParameter("estudiante", estudiante.getIdEstudiante())
            .getResultList()
            ;
            if(documentosEstudiante.isEmpty() || documentosEstudiante==null){ return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"No se encontrarón documentos del estudiante");}
            else {return ResultadoEJB.crearCorrecto(documentosEstudiante,"Documentos del estudiante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los documentos del estudiante (EjbInscripcionIngenieria.getDocumentosbyEstudiante).", e, null);

        }
    }

    /**
     * Obtiene  la lista de aspirantes de ing/lic registrados en el proceso de inscripcion activo
     * @param procesosInscripcion Proceso de inscripcion para ing/lic activo
     * @param eventoEscolar Evento escolar para inscripcion Ing/ Lic
     * @param tramitesEscolares Tramite escolar para el registro de cita de inscipcion ing/liv
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAspiranteIng>> getAspirantesIng(@NonNull ProcesosInscripcion procesosInscripcion, @NonNull EventoEscolar eventoEscolar, @NonNull TramitesEscolares tramitesEscolares){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2, new ArrayList<>(),"El proceso de inscripcion no debe ser nulo");}
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2, new ArrayList<>(),"El proceso de inscripcion no debe ser nulo");}
            //Obtener la lista de aspirantes de ing registrados en el proceso de inscripción
            ResultadoEJB<List<Aspirante>> resAspirante= getAspirantesIngbyProceso(procesosInscripcion);
            if(resAspirante.getCorrecto()){
                List<DtoAspiranteIng> aspirantesIng= new ArrayList<>();
                //recorrer la lista para saber el tipo de aspirante
                resAspirante.getValor().stream().forEach(a->{
                    ResultadoEJB<DtoAspiranteIng> pack= packAspiranteIng(procesosInscripcion,eventoEscolar,tramitesEscolares,a);
                    if(pack.getCorrecto()){
                        aspirantesIng.add(pack.getValor());
                    }else {
                        System.out.println("EjbInscripcionIngenieria.getAspirantesIng Erro con el aspirante "+ a.getFolioAspirante());
                    }
                });
                return ResultadoEJB.crearCorrecto(aspirantesIng,"Aspirantes Ing empaquetados");

            }else {return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"Error al obtner la lista de aspirantes de ingeniería del proceso ");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los aspirantes(EjbInscripcionIngenieria.getAspirantesIng).", e, null);

        }
    }

    /**
     * Empaqueta al aspirante
     * @param procesosInscripcion
     * @param eventoEscolar
     * @param tramite
     * @param aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspiranteIng> packAspiranteIng (@NonNull ProcesosInscripcion procesosInscripcion, @NonNull EventoEscolar eventoEscolar, @NonNull TramitesEscolares tramite, @NonNull Aspirante aspirante){
        try{
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2, new DtoAspiranteIng(),"El proceso de inscripcion no debe ser nulo");}
            if(aspirante==null){return ResultadoEJB.crearErroneo(2, new DtoAspiranteIng(),"El proceso de inscripcion no debe ser nulo");}
            DtoAspiranteIng dto = new DtoAspiranteIng();
            dto.setAspirante(aspirante);
            //Busca si es estudiante recien egresado
            ResultadoEJB<Estudiante> resEs = getEstudianteCEbyCurp(aspirante,eventoEscolar);
            if(resEs.getCorrecto()){
                //Es estudiante recien egresado
                dto.setEstudianteCE(resEs.getValor());
                dto.setMatricula(String.valueOf(resEs.getValor().getMatricula()));
                dto.setTipoAspirante(AspiranteTipoIng.ASPIRANTE_ING);
                dto.setUniversidadEgreso(ejbRegistroFichaIngenieria.getUtxj().getValor());//Universidad de egreso UTXJ
                dto.setTipoInscripción(TipoRegistroEstudiante.INSCRIPCION_ING_LIC);
                //Comprueba los documentos de estadía
                ResultadoEJB<String> resEstadia= getDocumentosEstadia(resEs.getValor());
                if(resEs.getCorrecto()){
                    dto.setDocumentosEstadia(resEstadia.getValor());
                    dto.setDocumentosEstadiaCompletos(Boolean.TRUE);
                }else {dto.setDocumentosEstadia(resEstadia.getValor());dto.setDocumentosEstadiaCompletos(Boolean.FALSE);}
            }else {
                //Revisar si es estudiante de otra generacion o otra UT
                //obtener la universidad de egreso del aspirante
                ResultadoEJB<UniversidadesUT> resU = getUniversidadbyAspirante(aspirante);
                if(resU.getCorrecto()){
                    dto.setUniversidadEgreso(resU.getValor());
                    //si es UTXJ(Clave 50) es aspirante de otra generacion, caso contrario es de otra ut
                    if(resU.getValor().getCveUniversidad().equals(50)){
                        //Egresada de otra generacion
                        dto.setTipoAspirante(AspiranteTipoIng.ASPPIRANTE_ING_OTRA_GENERACION);
                        dto.setMatricula("Consultar (SAIIUT)");
                        dto.setTipoInscripción(TipoRegistroEstudiante.INSCRIPCION_ING_lIC_OTRA_GENERACION);
                    }else {
                        //Egresada de otra Ut
                        dto.setTipoAspirante(AspiranteTipoIng.ASPPIRANTE_ING_OTRA_UT);
                        dto.setMatricula("Por definir");
                        dto.setTipoInscripción(TipoRegistroEstudiante.INSCRIPCION_ING_LIC_OTRA_UT);
                    }
                    //busca los datos de su cita
                    dto.setDatosCita(ejbCita.packAspiranteCita(String.valueOf(aspirante.getFolioAspirante()),tramite,procesosInscripcion).getValor());
                }else {return ResultadoEJB.crearErroneo(4,dto, "Error al obtener la universidad de egreso del aspirante");}
            }
            //datos academicos
            dto.setDatosAcademicos(getAcademicosbyAspirante(aspirante).getValor());
            //pe
            dto.setPeElegido(getPEbyClave(dto.getDatosAcademicos().getPrimeraOpcion()).getValor());
            //datos medicos
            dto.setDatosMedicos(getMedicosbyAspirante(aspirante).getValor());
            //datos medios de comunicacion
            dto.setMedioComunicacion(getMedioComunicacionbyAspirante(aspirante).getValor());
            //Comprobar si está inscrito en el periodo programado del evento
            ResultadoEJB<Estudiante> resEsIns= getEstudiantebyPeriodo(eventoEscolar,aspirante);
            if(resEsIns.getCorrecto()){
                dto.setEstudianteIncrito(resEsIns.getValor());
                //Esta inscrito en el periodo programado
                dto.setInscrito(Boolean.TRUE);
                //documentos entregados
                dto.setDocumentos(getDocumentosbyEstudiante(resEsIns.getValor()).getValor());
                //Grupo
                dto.setGrupo(ejbProcesoInscripcion.packGrupo(dto.getEstudianteIncrito().getGrupo()).getValor());
                dto.setPeIncrito(getPEbyClave(resEsIns.getValor().getCarrera()).getValor());
                return ResultadoEJB.crearCorrecto(dto,"Aspirante Empaquetado");
            }else {
                dto.setInscrito(Boolean.FALSE);
                dto.setPeIncrito(new AreasUniversidad());
                dto.setEstudianteIncrito(new Estudiante());
                dto.setDocumentos(new ArrayList<>());
                return ResultadoEJB.crearCorrecto(dto,"Estudiante empaquetado");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new DtoAspiranteIng(),"Error al empaquetar al aspirante");
        }
    }

    /**
     * Genera matricula del aspirante
     * @param eventoEscolar Evento escolcar aperturado de inscripcion ing
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> generaMatricula(@NonNull EventoEscolar eventoEscolar){
        try{
            if(eventoEscolar==null){return  ResultadoEJB.crearErroneo(1,new Integer(0),"El evento escolar no debe ser nulo");}
            int matricula = 0;
            String folio = "";
            //Asignar Matricula
            String anyo2 = new SimpleDateFormat("yy").format(new Date());
            folio = anyo2.concat("0000");
            TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(e.matricula) FROM Estudiante e WHERE e.periodo = :idPeriodo")
                    .setParameter("idPeriodo", eventoEscolar.getPeriodo());

            if(v.getSingleResult() == 0){
                matricula = Integer.valueOf(folio);
            }else{
                matricula = v.getSingleResult() + 1;
            }
            return  ResultadoEJB.crearCorrecto(matricula,"Matricula generada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new Integer(0),"Error al generar la matricula");

        }
    }

    /**
     * Genera login del estudiante
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Login> generaLogin (@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new Login(),"El estudiante no debe ser nulo");}
            Login newLogin = new Login();
            String contrasena="";
            int generador;
            for(int i=0;i<6;i++){
                generador = (int)(Math.random()*10);
                contrasena+= generador;
            }
           // System.out.println("EjbInscripcionIngenieria.generaLogin "+estudiante.getAspirante().getIdPersona().getIdpersona());
            newLogin.setPersona(estudiante.getAspirante().getIdPersona().getIdpersona());
            newLogin.setPassword(encriptaPassword(contrasena));
            newLogin.setUsuario(Integer.toString(estudiante.getMatricula()));
            newLogin.setModificado(false);
            newLogin.setActivo(true);
            em.persist(newLogin);
            f.setEntityClass(Login.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(newLogin,"Se genero el login");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new Login(),"Error al generar login");

        }
    }

    public static String encriptaPassword(String password) throws Exception{
        String contraseñaEncriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaEncriptada = Encrypted.encrypt(key, iv, password);

        return contraseñaEncriptada;
    }
    public static String desencriptaPassword(String password) throws Exception{
        String contraseñaDesencriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaDesencriptada = Encrypted.decrypt(key, iv, password);

        return contraseñaDesencriptada;
    }

    /**
     * Guarda documentos del estudiante
     * @param estudiante Estudiantes
     * @param documentos Lista documentos
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DocumentoEstudianteProceso>> saveDocumentosEstudiante (@NonNull Estudiante estudiante, @NonNull List<DocumentoProceso> documentos){
        try{
            if(estudiante ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El estudiante no debe ser nulo");}
            if(documentos.isEmpty()|| documentos==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"Los documentos no deben ser nulos");}
            List<DocumentoEstudianteProceso> docsEstudiante = new ArrayList<>();
            documentos.forEach(d->{
                DocumentoEstudianteProceso docs = new DocumentoEstudianteProceso();
                docs.setEstudiante(estudiante);
                docs.setDocumento(d);
                em.persist(docs);
                f.setEntityClass(DocumentoEstudianteProceso.class);
                f.flush();
                docsEstudiante.add(docs);
            });
            return ResultadoEJB.crearCorrecto(docsEstudiante,"Lista de documentos del estudiantes guardados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"Error al generar la matricula");

        }
    }
    public ResultadoEJB<Estudiante> guardaEstudiante(@NonNull Estudiante estudiante, Operacion operacion){
        try{
            Estudiante newEs = new Estudiante();
            switch (operacion){

                case PERSISTIR:
                    newEs = estudiante;
                    em.persist(newEs);
                    f.setEntityClass(Estudiante.class);
                    f.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(estudiante);
                    f.flush();

                    break;

            }
            return ResultadoEJB.crearCorrecto(newEs,"Estudiante");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new Estudiante(),"Error al generar la matricula");

        }
    }


    public ResultadoEJB<Estudiante> saveEstudiante(@NonNull ProcesosInscripcion procesosInscripcion, @NonNull DtoAspiranteIng dtoAspirante, @NonNull List<DocumentoProceso> documentos, @NonNull EventoEscolar eventoEscolar, @NonNull Personal trabajadorIncribe, @NonNull TramitesEscolares tramite){
        try{
            if(eventoEscolar==null){return  ResultadoEJB.crearErroneo(1,new Estudiante(),"El evento escolar no debe ser nulo");}
            if(dtoAspirante==null){return  ResultadoEJB.crearErroneo(1,new Estudiante(),"El aspirante no debe ser nulo");}
            if(dtoAspirante.getGrupo().getLleno()==false){
                if(dtoAspirante.getInscrito()==true){
                    //Actualiza al estudiante
                    //Obtener al estudiante
                    //Actualizar los datos (Solo se puede actualizar el grupo o la matricula)
                    return ResultadoEJB.crearCorrecto(new Estudiante(),"");

                }else {
                    TipoEstudiante tipoEstudiante = new TipoEstudiante((short)1, "Regular", true);
                    //Guarda al estudiante
                    Estudiante studiante = new Estudiante();
                    studiante.setAspirante(dtoAspirante.getAspirante());
                    studiante.setGrupo(dtoAspirante.getGrupo().getGrupo());
                    studiante.setTipoEstudiante(tipoEstudiante);
                    studiante.setMatricula(dtoAspirante.getEstudianteIncrito().getMatricula());
                    studiante.setPeriodo(eventoEscolar.getPeriodo());
                    studiante.setCarrera(dtoAspirante.getPeElegido().getArea());
                    studiante.setOpcionIncripcion(Boolean.TRUE);
                    studiante.setFechaAlta(new Date());
                    studiante.setTrabajadorInscribe(trabajadorIncribe.getClave());
                    studiante.setTipoRegistro(dtoAspirante.getTipoInscripción().getLabel());
                    ResultadoEJB<Estudiante> resEs = guardaEstudiante(studiante, Operacion.PERSISTIR);
                  if(resEs.getCorrecto()){
                     return ResultadoEJB.crearCorrecto(resEs.getValor(),"Estudiante guardado");

                  }else {return ResultadoEJB.crearErroneo(4,new Estudiante(),"Error al guardar el estudiante");}

                }

            }else {return ResultadoEJB.crearErroneo(2,new Estudiante(),"El grupo seleccionado está lleno. Seleccione otro");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new Estudiante(),"Error al generar al inscribir al estudiante");

        }
    }

    /**
     * Obtiene el seguimietno de la estadía del estudiante
     * @param estudiante Estudiante 6to grado
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> getSeguimientoEstadiabyEstudiante (@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new SeguimientoEstadiaEstudiante(),"El estudiante no debe ser nulo");}
            SeguimientoEstadiaEstudiante s = new SeguimientoEstadiaEstudiante();
            s= em.createQuery("select s from SeguimientoEstadiaEstudiante s where s.matricula.matricula=:matricula order by s.evento.evento desc ", SeguimientoEstadiaEstudiante.class)
            .setParameter("matricula",estudiante.getMatricula())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(s==null){return ResultadoEJB.crearErroneo(3,new SeguimientoEstadiaEstudiante(),"El estudiante no cuenta con estadía");}
            else {return ResultadoEJB.crearCorrecto(s,"Estadía del estudiante");}
        }catch (Exception E){
            return ResultadoEJB.crearErroneo(3,new SeguimientoEstadiaEstudiante(),"Error al generar al inscribir al estudiante");
        }
    }

    /**
     * Obtiene los docuemntos entregados del estudiante en estadia
     * @param seguimiento Seguimiento de estadía del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DocumentoSeguimientoEstadia>> getDocumentosEstadíabyEstudiante(@NonNull SeguimientoEstadiaEstudiante seguimiento){
        try{
            if(seguimiento==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El seguimiento de estadía no debe ser nulo");}
            List<DocumentoSeguimientoEstadia> documentos = new ArrayList<>();
            documentos = em.createQuery("select  d from DocumentoSeguimientoEstadia d where d.seguimientoEstadia.seguimiento=:seguimiento", DocumentoSeguimientoEstadia.class)
            .setParameter("seguimiento",seguimiento.getSeguimiento())
            .getResultList()
            ;
            if(documentos==null || documentos.isEmpty()){
                return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El estudiante no cuenta con documentos entregados");
            }else {return ResultadoEJB.crearCorrecto(documentos,"Docuementos");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"Error al obtener los documentos de estadía del estudiante");

        }
    }

    /**
     * Comprueba si el estudiante cuante con el documento
     * @param documentos
     * @param documento
     * @return
     */

    public ResultadoEJB<String> comprubaDocumentoEstadia(@NonNull List<DocumentoSeguimientoEstadia> documentos, Integer documento){
        try{
            if(documentos.isEmpty() || documentos==null){return ResultadoEJB.crearErroneo(2,new String(),"Los documentos no deben ser nulos");}
            long doc= 0;
            doc=documentos.stream().filter(d->d.getDocumento().getDocumento().equals(documento)).count();
            if(doc==0){return ResultadoEJB.crearErroneo(3,new String(),"El estudiante no cuenta con el documento");}
            else {return ResultadoEJB.crearCorrecto("Cuenta con el documento","Cuenta con el documento");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(4,new String(),"Error al comprobar los documentos entregados de estadía del estudiante");

        }
    }

    /**
     * Comprueba que el estudiante haya entregado todos los documentos de estadía
     * Primer informe
     * Segundo informe
     * Tercer informe
     * Informe final
     * @param estudiante Estudiante regular de 6to
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> getDocumentosEstadia(@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new String(),"El estudiante no debe ser nulo");}
            //Obtiene el seguimiento del estudiante
            ResultadoEJB<SeguimientoEstadiaEstudiante> resSe= getSeguimientoEstadiabyEstudiante(estudiante);
            if(resSe.getCorrecto()){
                //Obtiene a lista de documentos del estudiante
                ResultadoEJB<List<DocumentoSeguimientoEstadia>> resDoc= getDocumentosEstadíabyEstudiante(resSe.getValor());
                if(resDoc.getCorrecto()){
                    //Comprueba primer informe
                    ResultadoEJB<String> resPrimerInforme= comprubaDocumentoEstadia(resDoc.getValor(),35);
                    if(resPrimerInforme.getCorrecto()){
                        //Comprueba segundo informe
                        ResultadoEJB<String> resSegundoInforme= comprubaDocumentoEstadia(resDoc.getValor(),36);
                        if(resSegundoInforme.getCorrecto()){
                            //Comprueba 3er Informe
                            ResultadoEJB<String> resTercerInforme= comprubaDocumentoEstadia(resDoc.getValor(),37);
                            if(resTercerInforme.getCorrecto()){
                                //Comprueba informe final
                                ResultadoEJB<String> resInformeFinal= comprubaDocumentoEstadia(resDoc.getValor(),38);
                                if(resInformeFinal.getCorrecto()){
                                    return ResultadoEJB.crearCorrecto("Todos los documentos de estadía entregados","Todos los documentos entregados");
                                }else { return ResultadoEJB.crearErroneo(3,"Informe final sin entregar","No cuenta con el informe final"); }

                            }else { return ResultadoEJB.crearErroneo(3,"Tercer informe sin entregar","No cuenta con el 3er informe"); }

                        }else { return ResultadoEJB.crearErroneo(3,"Segundo informe sin entregar","No cuenta con el segundo informe"); }

                    }else { return ResultadoEJB.crearErroneo(4,"Primer informe sin entregar","No cuenta con primer informe"); }

                }else {return ResultadoEJB.crearErroneo(3,"No cuenta con documentos de estadía entregados","No cuenta con documentos de estadía entregados");}

            }else {return ResultadoEJB.crearErroneo(4,"Sin proceso de estadía iniciado","El estudiante no cuenta con proceso de estadía ");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(4,new String(),"Error al comprobar los documentos entregados de estadía del estudiante");

        }
    }



    /**
     * Obtiene login del aspirante
     * @param aspirante
     * @return
     */
    public ResultadoEJB<Login> getLogin(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new Login(),"El aspirante no debe ser nulo");}
            Login l = new Login();
            l = em.createQuery("select l from Login l where l.persona=:persona ", Login.class)
            .setParameter("persona",aspirante.getIdPersona().getIdpersona())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(l==null){return ResultadoEJB.crearErroneo(3,new Login(),"No se encontró Login");}
            else {return ResultadoEJB.crearCorrecto(l,"Login");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new Login(),"Error al obtener Login");

        }
    }

    public ResultadoEJB<MedioComunicacion> getMedioComunicacion(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new MedioComunicacion(),"El aspirante no debe ser nulo");}
            MedioComunicacion mc = em.createQuery("select m from MedioComunicacion m where m.persona=:persona", MedioComunicacion.class)
                    .setParameter("persona",aspirante.getIdPersona().getIdpersona())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(mc==null){return ResultadoEJB.crearErroneo(3,new MedioComunicacion(),"No se encontré");}
            else {return ResultadoEJB.crearCorrecto(mc,"Medio comunicación");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new MedioComunicacion(),"Error al obtener los medios de comunicacion");

        }
    }
    /**
     * Actualizacion de Datos Académicos
     * @param datosAcademicos
     * @return
     */

    public ResultadoEJB<DatosAcademicos> updateDatosAcademicos (@NonNull DatosAcademicos datosAcademicos){
        try{
            if(datosAcademicos==null){return ResultadoEJB.crearErroneo(2,new DatosAcademicos(),"El aspirante no debe ser nulo");}
            em.merge(datosAcademicos);
            f.setEntityClass(DatosAcademicos.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(datosAcademicos,"Datos actualizados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new DatosAcademicos(),"Error al actaulizar los datos");

        }
    }
    public ResultadoEJB<DatosMedicos> updateDatosMedicos (@NonNull DatosMedicos datosMedicos){
        try{
            if(datosMedicos==null){return ResultadoEJB.crearErroneo(2,new DatosMedicos(),"El aspirante no debe ser nulo");}
            em.merge(datosMedicos);
            f.setEntityClass(DatosAcademicos.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(datosMedicos,"Datos actualizados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new DatosMedicos(),"Error al actaulizar los datos");

        }
    }
    /**
     * Genera comprobante de inscripción
     * @param uso
     * @throws IOException
     * @throws DocumentException
     */
    public ResultadoEJB<Boolean> generaComprobante (@NonNull DtoAspiranteIng estudiante, @NonNull Login login, @NonNull MedioComunicacion mc, @NonNull String uso) throws IOException, DocumentException {
        if(estudiante==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"La persona no debe ser nula");}
        if(uso==null){return ResultadoEJB.crearErroneo(7,Boolean.FALSE,"El uso no debe ser nulo");}
        String ruta = "C://archivos//plantillas//comprobanteInscripcion__ing.pdf";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");

        InputStream is = new FileInputStream(ruta);
        PdfReader pdfReader = new PdfReader(is,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);

        BarcodePDF417 pdfcodigo = new BarcodePDF417();
        pdfcodigo.setText(String.valueOf(estudiante.getEstudianteIncrito().getMatricula()));
        Image img = pdfcodigo.getImage();
        img.setAbsolutePosition(280f, 70f);
        img.scalePercent(200, 100 * pdfcodigo.getYHeight());
        PdfContentByte  content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
        content.addImage(img);

        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("txtAP", estudiante.getAspirante().getIdPersona().getApellidoPaterno());
        fields.setField("txtAM", estudiante.getAspirante().getIdPersona().getApellidoMaterno());
        fields.setField("txtNombre", estudiante.getAspirante().getIdPersona().getNombre());
        fields.setField("txtCarrera", estudiante.getPeIncrito().getNombre());
        fields.setField("txtMatricula", String.valueOf(estudiante.getEstudianteIncrito().getMatricula()));
        fields.setField("txtGrupo", String.valueOf(estudiante.getEstudianteIncrito().getGrupo().getGrado()).concat(" ").concat(estudiante.getEstudianteIncrito().getGrupo().getLiteral().toString()));
        fields.setField("txtTurno", estudiante.getEstudianteIncrito().getGrupo().getIdSistema().getNombre());
        try {
            fields.setField("txtPassword", desencriptaPassword(login.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fields.setField("txtCAbreviatura", estudiante.getPeIncrito().getSiglas());
        fields.setField("txtFicha", String.valueOf(estudiante.getAspirante().getFolioAspirante()));
        fields.setField("txtNombreEstudiante", estudiante.getAspirante().getIdPersona().getApellidoPaterno().concat(" ").concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno().concat(" ").concat(estudiante.getAspirante().getIdPersona().getNombre())));
        fields.setField("txtActaNac", compruebaDoc(42,estudiante.getDocumentos()) ==true ? "Entregado": "No entregado");
        fields.setField("txtC", compruebaDoc(43,estudiante.getDocumentos()) == true ? "Entregado" : "No Entregado");
        fields.setField("txtCURP", compruebaDoc(2,estudiante.getDocumentos()) == true ? "Entregado" : "No Entregado");
        fields.setField("txtTitulo", compruebaDoc(31,estudiante.getDocumentos()) == true ? "Entregado" : "No Entregado");
        fields.setField("txtHist", compruebaDoc(45,estudiante.getDocumentos()) == true ? "Entregado" : "No Entregado");
        fields.setField("txtPago", compruebaDoc(11,estudiante.getDocumentos()) == true ? "Entregado" : "No Entregado");
        fields.setField("txtTipo", compruebaDoc(13,estudiante.getDocumentos())== true ? "Entregado" : "No Entregado");

        pdfStamper.close();
        pdfStamper.close();

        Object response = facesContext.getExternalContext().getResponse();
        if (response instanceof HttpServletResponse) {
            HttpServletResponse hsr = (HttpServletResponse) response;
            hsr.setContentType("application/pdf");
            hsr.setHeader("Content-disposition", "attachment; filename=\""+estudiante.getEstudianteIncrito().getMatricula()+".pdf\"");
            hsr.setContentLength(baos.size());
            try {
                ServletOutputStream out = hsr.getOutputStream();
                baos.writeTo(out);
                out.flush();
                out.close();
            } catch (IOException ex) {
                System.out.println("Error:  " + ex.getMessage());
            }
            facesContext.responseComplete();
        }

        if(uso.equals("Alumno")){
            // El correo gmail de envío
            String correoEnvia = ep.leerPropiedad("correoElectronicoEscolares").getValorCadena();    
            String claveCorreo = ep.leerPropiedad("passwordCorreoElectronicoEscolares").getValorCadena();
            String mensaje = "Estimado(a)"+ estudiante.getAspirante().getIdPersona().getNombre() + ", se te notifica que se ha realizado con éxito tu inscripción."+
                    "\n\n Matricula: " + estudiante.getEstudianteIncrito().getMatricula()+"\n"
                    + "Grupo : "+estudiante.getEstudianteIncrito().getGrupo().getGrado() +"-"+estudiante.getEstudianteIncrito().getGrupo().getLiteral() + " de la carrera "+ estudiante.getPeIncrito().getNombre()+"\n\n"
                    + "ATENTAMENTE \n" +
                    "Departamento de Servicios Escolares";
            String identificador = "Inscripciones UTXJ " + new SimpleDateFormat("yyyy").format(new Date()) + " ";
            String asunto = "Inscripción Ingeniería - Licenciatura";
            // System.out.println(medioComunicacion.getEmail());
            if(mc.getEmail() != null){
                // System.out.println("Correo "+ medioComunicacion.getEmail());
                try {
                    DataSource source = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
                    EnvioCorreos.EnviarCorreoArchivos(correoEnvia, claveCorreo,identificador,asunto,mc.getEmail(),mensaje,source,String.valueOf(estudiante.getEstudianteIncrito().getMatricula()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return ResultadoEJB.crearCorrecto(Boolean.TRUE,"Ficha de admisión creada correctamente");
    }

    public Boolean compruebaDoc(@NonNull Integer documento, @NonNull List<DocumentoEstudianteProceso> documentos){
        Boolean entregado= false;
        long d =0;
        d =documentos.stream().filter(documentoEstudianteProceso -> documentoEstudianteProceso.getDocumento().getDocumento().getDocumento()==documento).count();
       if(d==0){return false;}else {return true;}
    }

    /**
     * Inscribe al estudiante
     * @param procesosInscripcion Proceso de inscripcion
     * @param dto Dto del aspirante
     * @param documentos Documentos entregados
     * @param evento Evento activo para inscripcion
     * @param personal Personal que inscribe
     * @param tramite Tramite activo para la inscripcion
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspiranteIng> inscribirEstudiante (@NonNull ProcesosInscripcion procesosInscripcion, @NonNull DtoAspiranteIng dto, @NonNull List<DocumentoProceso> documentos, @NonNull EventoEscolar evento, @NonNull Personal personal, @NonNull TramitesEscolares tramite){
        try{
            if(dto.getGrupo()==null){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(),"Debe seleccionar un grupo");}
            if(dto.getEstudianteIncrito().getMatricula()==0){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(),"Debe asignarle una matricula");}
            if(procesosInscripcion ==null){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(), "El proceso de inscrupción no debe ser nulo");}
            if(dto ==null){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(), "El aspirante no debe ser nulo");}
            if(documentos ==null || documentos.isEmpty()){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(), "Debe seleccionar los documentos correspondientes");}
            if(evento ==null){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(), "El evento escolar no debe ser nulo");}
            if(personal ==null){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(), "El evento escolar no debe ser nulo");}
            if(tramite==null){return ResultadoEJB.crearErroneo(3,new DtoAspiranteIng(),"El tramite no debe ser nulo");}
            if(dto.getGrupo().getLleno()==false){
                ResultadoEJB<Estudiante> resEstudiante = saveEstudiante(procesosInscripcion,dto,documentos,evento,personal,tramite);
                if(resEstudiante.getCorrecto()){
                    if(dto.getTipoAspirante()!=(AspiranteTipoIng.ASPIRANTE_ING)) {
                        //Genera Login
                        ResultadoEJB<Login> resLogin = generaLogin(resEstudiante.getValor());
                        if (resLogin.getCorrecto()) {
                        } else {
                            return ResultadoEJB.crearErroneo(4, new DtoAspiranteIng(), "Error al generar Login");
                        }
                    }
                    //Genera los documentos
                    ResultadoEJB<List<DocumentoEstudianteProceso>> resDoc= saveDocumentosEstudiante(resEstudiante.getValor(),documentos);
                    if(resDoc.getCorrecto()){
                        //Actualiza los datos
                        ResultadoEJB<DatosAcademicos> resDA = updateDatosAcademicos(dto.getDatosAcademicos());
                        ResultadoEJB<DatosMedicos> resMed= updateDatosMedicos(dto.getDatosMedicos());
                        ResultadoEJB<DtoAspiranteIng> resPack= packAspiranteIng(procesosInscripcion,evento,tramite,dto.getAspirante());
                        if(resPack.getCorrecto()){
                            return ResultadoEJB.crearCorrecto(resPack.getValor(),"Estudiante incrito con éxito");
                        }else {return ResultadoEJB.crearErroneo(5,new DtoAspiranteIng(),"Error al inscribir al estudiante");}

                    }else {return ResultadoEJB.crearErroneo(6,new DtoAspiranteIng(),"Error al guardar los documentos");}

                }
                else {return ResultadoEJB.crearErroneo(3,new DtoAspiranteIng(),"Error al ");}
            }else {return ResultadoEJB.crearErroneo(4,new DtoAspiranteIng(),"El grupo seleccionado está lleno, seleccione otro");}


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(3,new DtoAspiranteIng(),"Error al inscribir al estudiante");

        }

    }


}
