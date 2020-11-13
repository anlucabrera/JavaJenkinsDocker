package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Stateless(name = "EjbReinscripcionAutonoma")
public class EjbReinscripcionAutonoma {
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB  EjbConsultaCalificacion ejbConsultaCalificacion;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EJBAdimEstudianteBase estudianteBase;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    /**
     * Permite validar si el usuario autenticado es un estudiante
     * @param matricula Número de identificación del estudiante autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> validaEstudiante(Integer matricula){
        try{
            Estudiante e = em.createQuery("select e from Estudiante as e where e.matricula =:matricula and e.tipoEstudiante.idTipoEstudiante=:tipo order by e.periodo desc ", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("tipo",1)
                    .getResultStream().findFirst().orElse(new Estudiante());
            return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbPerfilEstudiante.validadEstudiante)", e, null);
        }
    }


    /**
     * Permite verificar si hay un periodo abierto para reinscripción autónoma
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(){
        try{
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REINSCRIPCION_AUTONOMA);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para reinscripción autónoma (EjbReinscripcionAutonoma.).", e, EventoEscolar.class);
        }
    }



    /**
     * Permite obtener grupo inmediato superior al que se reinscribirá el estudiante
     * @param estudiante Estudiante que va a realizar su reinscripción
     * @param eventoEscolar Evento Escolar para recuperar periodo disponible para reinscripción
     * @return Resultado del proceso
     */
    public ResultadoEJB<Grupo> getGrupoInmediatoSuperior(Estudiante estudiante, EventoEscolar eventoEscolar){
        try{

            Integer gradoAct = estudiante.getGrupo().getGrado();
            Integer gradoSup = gradoAct + 1;
            // buscar grupo inmediato superior para reinscribirse (con la misma literal)
            Grupo grupoSup = em.createQuery("select g from Grupo g where g.periodo =:periodo and g.idPe =:programa and g.grado =:grado and g.literal =:literal", Grupo.class)
                    .setParameter("periodo", eventoEscolar.getPeriodo())
                    .setParameter("programa", estudiante.getGrupo().getIdPe())
                    .setParameter("grado", gradoSup)
                    .setParameter("literal", estudiante.getGrupo().getLiteral())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(grupoSup ==null){return ResultadoEJB.crearErroneo(3,grupoSup,"No se encontro grupo superior inmediato");}
            else { return ResultadoEJB.crearCorrecto(grupoSup, "Grupo superior inmediato");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener grupo inmediato superior. (EjbReinscripcionAutonoma.getGrupoInmediatoSuperior)", e, null);
        }
    }

    /**
     * Permite obtener un grupo alterno al estudiante (Esto en caso de que no exista un grupo con la misma literal que su grupo actual)
     * @param estudiante Estudiante activo
     * @param eventoEscolar evento escolar (Resincripcion autonoma)
     * @return Resultado del proceso(Grupo alterno)
     */
    public ResultadoEJB<Grupo> getGrupoAlterno(Estudiante estudiante, EventoEscolar eventoEscolar){
        try{
            Grupo grupoAlterno = new Grupo();
            if(estudiante ==null){return  ResultadoEJB.crearErroneo(2,grupoAlterno,"El estudiante no debe ser nulo");}
            if(eventoEscolar ==null){return ResultadoEJB.crearErroneo(3,grupoAlterno,"El evento no debe ser nulo");}
            Integer gradoAct = estudiante.getGrupo().getGrado();
            Integer gradoSup = gradoAct + 1;
            //Busca un grupo alterno, con diferente literal pero que sea del mismo sistema que en el que estaba
            grupoAlterno = em.createQuery("select g from Grupo g where g.periodo=:periodo and g.idPe=:programa and g.grado=:grado and g.literal !=:literal and g.idSistema.idSistema=:sistema",Grupo.class)
                    .setParameter("periodo",eventoEscolar.getPeriodo())
                    .setParameter("programa",estudiante.getGrupo().getIdPe())
                    .setParameter("grado",gradoSup)
                    .setParameter("literal",estudiante.getGrupo().getLiteral())
                    .setParameter("sistema",estudiante.getGrupo().getIdSistema().getIdSistema())
                    .getResultStream()
                    .findAny()
                    .orElse(null)
            ;
            if(grupoAlterno==null){return ResultadoEJB.crearErroneo(4,grupoAlterno,"No se encontro ningún grupo alterno para el estudiante");}
            else {return ResultadoEJB.crearCorrecto(grupoAlterno,"Grupo Alterno");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener grupo alterno. (EjbReinscripcionAutonoma.getGrupoAlterno)", e, null);
        }
    }

    /**
     * Permite saber si el estudiante aprobo todas su materias (
     * @param estudiante Estudiante activo
     * @return Resultado del proceso (true -> Si aprobo false-> Si cuenta con materias reprobadas)
     */

    public ResultadoEJB<Boolean> compruebaAprobo(Estudiante estudiante){
        try{
            Boolean aprobo=false;
            if(estudiante ==null){return ResultadoEJB.crearErroneo(2,aprobo,"El estudiante no debe ser nulo");}
            //Hace una consulta de sus calificaciones finales
            ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resCalificacionesFinales = ejbConsultaCalificacion.packCalificacionesFinales(estudiante);
            if(resCalificacionesFinales.getCorrecto()==true){
                List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionesFinales = resCalificacionesFinales.getValor();
                System.out.println("Lista de calificaciones finales ->" + calificacionesFinales);
                 BigDecimal minAprobatoria = new BigDecimal ("8.00");
                calificacionesFinales.forEach(c ->{
                   double prom = c.getPromedio().doubleValue();
                   System.out.println("Promedio" +prom);

                });
                return ResultadoEJB.crearCorrecto(true,"Promedio");
            }else {
                return ResultadoEJB.crearErroneo(3,aprobo,"Error al obtener las calificaciones finales del estudiante");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener si el estudiante aprobó todas sus materias (EjbReinscripcionAutonoma.compruebaAprobo)", e, null);
        }
    }

    /**
     * Obtiene el listado de materias que va a cursar el estudiante
     * @param nuevoGrupo Grupo al que va ser reinscrito
     * @return Resultado del proceso (Lista de materias por cursar)
     */
    public ResultadoEJB<List<dtoEstudianteMateria>> getNuevasMaterias(Grupo nuevoGrupo){
        try{
            List<dtoEstudianteMateria> listaMaterias= new ArrayList<>();
            if(nuevoGrupo ==null){return ResultadoEJB.crearErroneo(2, listaMaterias,"El grupo no debe ser nulo");}
            List<CargaAcademica> cargaAcademica = new ArrayList<>();
            cargaAcademica = em.createQuery("select c from CargaAcademica c where c.cveGrupo.idGrupo=:idGrupo and c.cveGrupo.periodo=:periodo",CargaAcademica.class)
                    .setParameter("idGrupo",nuevoGrupo.getIdGrupo())
                    .setParameter("periodo",nuevoGrupo.getPeriodo())
                    .getResultList()
            ;
            if(cargaAcademica==null || cargaAcademica.isEmpty()){return ResultadoEJB.crearErroneo(4,listaMaterias,"No se han encontrado cargas académicas para el grupo."); }
            else {
                //Se recorre la lista de carga acedemica para llenar las materias del estudiante
                cargaAcademica.stream().forEach(c->{
                    dtoEstudianteMateria materia = new dtoEstudianteMateria();
                    //Se busca al docente que imparte la materia
                    ResultadoEJB<Personal> docente = estudianteBase.getPersonalbyClave(c.getDocente());
                    if(docente.getCorrecto()==true){materia.setDocenteImparte(docente.getValor());}
                    materia.setClaveMateria(c.getIdPlanMateria().getClaveMateria());
                    materia.setNombreMateria(c.getIdPlanMateria().getIdMateria().getNombre());
                    listaMaterias.add(materia);
                });
                return ResultadoEJB.crearCorrecto(listaMaterias,"Lista de materias");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el listado de materias que cursará el estudiante (EjbReinscripcionAutonoma.getNuevasMaterias)", e, null);
        }
    }


    /**
     * Reinscribe al estudiante logueado
     * @param estudiante
     * @param nuevoGrupo
     * @return
     */
    public ResultadoEJB<Estudiante> reinscribirEstudiante (Estudiante estudiante,Grupo nuevoGrupo){
        try{
            Estudiante estudianteReinscrito = new Estudiante();
            Personal p = new Personal();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,estudianteReinscrito,"El estudiante no debe ser nulo");}
            //Se le asignan los valores al estudiante
            estudianteReinscrito.setFechaAlta(new Date());
            //System.out.println("fecha --->" +estudianteReinscrito.getFechaAlta());
            estudianteReinscrito.setCarrera(estudiante.getCarrera());
           // System.out.println("carrea --->" +estudianteReinscrito.getCarrera());
            estudianteReinscrito.setGrupo(nuevoGrupo);
           // System.out.println("grupo --->" +estudianteReinscrito.getGrupo());
            estudianteReinscrito.setAspirante(estudiante.getAspirante());
            //System.out.println("aspirante --->" +estudianteReinscrito.getAspirante());
            estudianteReinscrito.setTipoEstudiante(estudiante.getTipoEstudiante());
            //System.out.println("tipo --->" +estudianteReinscrito.getTipoEstudiante());
            estudianteReinscrito.setMatricula(estudiante.getMatricula());
            //System.out.println("matricula --->" +estudiante.getMatricula());
            estudianteReinscrito.setPeriodo(nuevoGrupo.getPeriodo());
            //System.out.println("periodo --->" +estudiante.getPeriodo());
            estudianteReinscrito.setTipoRegistro("Reinscripcion Autónoma");
           // System.out.println("tiporegustro --->" +estudianteReinscrito.getTipoRegistro());
           // System.out.println("Estudiante nuevo --->" +estudianteReinscrito);
            em.persist(estudianteReinscrito);
            return ResultadoEJB.crearCorrecto(estudianteReinscrito,"Estudiante reinscrito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo reinscribir al estudiante (EjbReinscripcionAutonoma.reinscribirEstudiante)", e, null);
        }
    }

    /**
     * Busca un estudiante por matricula y por periodo del evento
     * @param estudiante
     * @param eventoEscolar Evento de reinscripcion
     * @return Resultado del proceso
     */
    ResultadoEJB<Estudiante> getEstudianteEvento (Estudiante estudiante, EventoEscolar eventoEscolar){
        try {
            Estudiante estudianteR= new Estudiante();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,estudianteR,"El estudianate no debe ser nulo");}
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(3,estudianteR,"El evento no debe ser nulo");}
            estudianteR = em.createQuery("select e from Estudiante  e where e.matricula=:matricula and e.periodo=:periodo",Estudiante.class)
                .setParameter("matricula",estudiante.getMatricula())
            .setParameter("periodo",eventoEscolar.getPeriodo())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(estudianteR ==null){return ResultadoEJB.crearErroneo(4,estudianteR,"No se encontro al estudiante con el periodo del evento escolar");}
            else {
                return ResultadoEJB.crearCorrecto(estudianteR,"Estudiante con periodo del evento");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo reinscribir al estudiante (EjbReinscripcionAutonoma.getEstudianteEvento)", e, null);
        }
    }

    /**
     * Comprueba que el estudiante logueado ya este inscrito en el periodo programado
     * @param estudiante Estudiante logueado
     * @param evento Evento escolar de reinscripcion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> compruebaReinscrito(Estudiante estudiante, EventoEscolar evento){
        try{
            Boolean inscrito = false;
            if(estudiante ==null){return  ResultadoEJB.crearErroneo(2,inscrito,"El estudiante no debe ser nulo");}
            if(evento==null){return ResultadoEJB.crearErroneo(3,inscrito,"El evanto escolar no debe ser nulo");}
            //Busca al estudiante por el periodo del evento
            ResultadoEJB<Estudiante> resEstudiante = getEstudianteEvento(estudiante,evento);
            if(resEstudiante.getCorrecto()==true){
                inscrito =true;
                return ResultadoEJB.crearCorrecto(inscrito,"Estudiante inscrito");
            }else {inscrito=false;return ResultadoEJB.crearErroneo(4,inscrito,"El estudiante no se ha reinscrito");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo comprobar (EjbReinscripcionAutonoma.compruebaReinscrito)", e, null);
        }
    }
    //-------------- Para obtener sus materias

    /**
     * Busca el periodo del grupo en el que estaba inscrito el estudiante
     * @param grupoAnt Grupo en el que se encuentra inscrito el estudiante
     * @return Resultado del proceso (Periodo escolar)
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoGrupo(Grupo grupoAnt){
        try{
            PeriodosEscolares periodo = new PeriodosEscolares();
            if(grupoAnt ==null){return ResultadoEJB.crearErroneo(2,periodo,"El grupo no debe ser nulo");}
            //Se busca el periodo segun el grupo que estaba el estudiante
            periodo = em.createQuery("select  p from PeriodosEscolares  p where p.periodo=:periodo",PeriodosEscolares.class)
                    .setParameter("periodo",grupoAnt.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(periodo==null){return  ResultadoEJB.crearErroneo(3,periodo,"No se encontro el periodo del grupo");}
            else {return ResultadoEJB.crearCorrecto(periodo,"Periodo del grupo encontrado");}

        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error en buscar Periodo del grupo (EjbReinscripcionAutonoma.getPeriodoGrupo).", e, PeriodosEscolares.class);

        }
    }

    /**
     * Obtiene la lista de cargas academicas por grupo
     * @param grupo Grupo en el que se encuentra inscrito el estudiante
     * @return Resultado del proceso (Lista de cargas academicas)
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAdemicabyGrupo(Grupo grupo){
        try{
            List<DtoCargaAcademica> cargaAcademicas = new ArrayList<>();
            if(grupo==null){return ResultadoEJB.crearErroneo(2,cargaAcademicas,"El grupo no debe ser nulo");}
            cargaAcademicas = em.createQuery("select ca from CargaAcademica ca inner join ca.evento e where ca.cveGrupo.idGrupo=:grupo and e.periodo=:periodo", CargaAcademica.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .setParameter("periodo", grupo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> ejbAsignacionAcademica.pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());

            if(cargaAcademicas.isEmpty()) return  ResultadoEJB.crearErroneo(2, cargaAcademicas, "El grupo no cuenta con cargas academicas");
            else return ResultadoEJB.crearCorrecto(cargaAcademicas, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista cargas cadémicas por grupo y periodo (EjbReinscripcionAutonoma.getCargaAdemicabyGrupo).", e, null);

        }
    }
        //----------------------APARTADO PARA EDIDICION DE DATOS PERSONALES ------------------------------------
    /**
     * Obtiene la lista de estados de México
     * @return
     */
    public ResultadoEJB<List<Estado>> getEstados (){
        try{
            List<Estado> estados = new ArrayList<>();
            estados = em.createQuery("select e from Estado e where e.idpais.idpais=:pais",Estado.class)
                    .setParameter("pais",42)
                    .getResultList();
            if(estados==null || estados.isEmpty()){return ResultadoEJB.crearErroneo(2,estados,"No se encontraron estados");}
            else {return ResultadoEJB.crearCorrecto(estados,"Lista de estados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener los estados(EjbReinscripcionAutonoma.getEstados)", e, null);
        }
    }

    /**
     * Obtiene una lista de municipios x estado
     * @param domicilio
     * @return
     */
    public ResultadoEJB<List<Municipio>>getMunicipioByEstado(Domicilio domicilio){
        try{
            List<Municipio> municipios = new ArrayList<>();
            if(domicilio==null){return ResultadoEJB.crearErroneo(2,municipios,"El estado no debe ser nulo");}
            municipios = em.createQuery("select m from Municipio m where m.estado.idestado=:estado",Municipio.class)
                .setParameter("estado", domicilio.getIdEstado())
                .getResultList()
            ;
            if(municipios==null || municipios.isEmpty()){return ResultadoEJB.crearErroneo(3,municipios,"No se encontraron municipios");}
            else {return ResultadoEJB.crearCorrecto(municipios,"Lista de municipos");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los municipio x estado(EjbReinscripcionAutonoma.getMunicipioByEstado)", e, null);
        }

    }

    /**
     * Obtiene la lista de asentamientos x estado y x municipio
     * @param domicilio Estado del estudiante
     * @return Resultado del proceso (Lista de asentamientos)
     */
    public ResultadoEJB<List<Asentamiento>> getAsentamientobyMunicipio (Domicilio domicilio){
        try{
            List<Asentamiento> asentamientos = new ArrayList<>();
            if(domicilio ==null){return ResultadoEJB.crearErroneo(2,asentamientos,"El domicilio no debe ser nulo");}
            asentamientos = em.createQuery("select a from Asentamiento a where a.asentamientoPK.estado =:estado and a.asentamientoPK.municipio=:municipio",Asentamiento.class)
                        .setParameter("estado",domicilio.getIdEstado())
                        .setParameter("municipio",domicilio.getIdMunicipio())
                        .getResultList()
            ;
            if(asentamientos==null || asentamientos.isEmpty()){return ResultadoEJB.crearErroneo(4,asentamientos,"No se encontro lista de asentamientos");}
            else {return ResultadoEJB.crearCorrecto(asentamientos,"Lista de asentamientos");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los asentamientos x municipio(EjbReinscripcionAutonoma.getMunicipioByEstado)", e, null);
        }
    }

    /**
     * Actualiza los datos del domicilio del estudiante
     * @param domicilio
     * @return
     */
    public ResultadoEJB<Domicilio> updateDomicilio(Domicilio domicilio){
        try{
            if(domicilio ==null){return ResultadoEJB.crearErroneo(3,domicilio,"El domicilio no debe ser nulo");}
            Domicilio domicilioNew = new Domicilio();
            domicilioNew =domicilio;
            em.merge(domicilioNew);
            return ResultadoEJB.crearCorrecto(domicilioNew,"Se han actualizado los datos de domicilio del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, " Error al actualizar los datos del domicilio del estudiante(EjbReinscripcionAutonoma.updateDomicilio)", e, null);
        }
    }

    /**
     * Actualiza los medio de comunicacion del estudiante
     * @param medioComunicacion
     * @return Resultado del proceso
     */
    public  ResultadoEJB<MedioComunicacion> updateMedioComunicacion(MedioComunicacion medioComunicacion){
        try{
            if(medioComunicacion ==null){return ResultadoEJB.crearErroneo(2,medioComunicacion,"El medio de comunicacion no debe ser nulo");}
            em.merge(medioComunicacion);
            return ResultadoEJB.crearCorrecto(medioComunicacion,"Medios de comunicación actualizados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, " Error al actualizar los datos del domicilio del estudiante(EjbReinscripcionAutonoma.updateDomicilio)", e, null);

        }
    }

    /**
     * Actualiza los datos familiares del estudiante
     * @param datosFamiliares
     * @return
     */
    public ResultadoEJB<DatosFamiliares> updateDatosFam(DatosFamiliares datosFamiliares){
        try {
            if(datosFamiliares ==null){return ResultadoEJB.crearErroneo(2,datosFamiliares,"Los datos familiares no deben ser nulos");}
            em.merge(datosFamiliares);
            return ResultadoEJB.crearCorrecto(datosFamiliares,"Datos familiares actualizados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, " Error al actualizar los datos familiares(EjbReinscripcionAutonoma.updateDatosFam )", e, null);
        }
    }

    /**
     * Actualiza los datos personales del estudiante (Domicilio, medios de comunicaciones)
     *      * @param medioComunicacion
     * @param domicilio
     * @return
     */
    public ResultadoEJB<Boolean> updateDatosPersonales(MedioComunicacion medioComunicacion, Domicilio domicilio){
        try{
            Boolean update =false;
            if(medioComunicacion ==null){return ResultadoEJB.crearErroneo(4,update,"Los medios de comunicacion no deben ser nulos");}
            if(domicilio ==null){return ResultadoEJB.crearErroneo(5,update,"El domicilio no debe ser nulos");}
            //System.out.println("Datos de contacto "+ medioComunicacion + "Domicilio "+ domicilio );
            //Actualiza domicilio
             ResultadoEJB<Domicilio> resDomicilio = updateDomicilio(domicilio);
            //Actualiza medios de comunicacion
            ResultadoEJB<MedioComunicacion> resMediosComunicacion = updateMedioComunicacion(medioComunicacion);
            //if(resDomicilio.getCorrecto()==true & resDatosFam.getCorrecto()==true & resMediosComunicacion.getCorrecto()==true){ update=true;return ResultadoEJB.crearCorrecto(update,"Datos actualizados");}
            //else { update =false;return ResultadoEJB.crearErroneo(6,update,"Ocurrio un error al actualizar los datos personales"); }
            if(resMediosComunicacion.getCorrecto()==true & resDomicilio.getCorrecto()==true){
                update=true;
                return ResultadoEJB.crearCorrecto(update,"Datos personalizados actualizados");
            }
            else {return ResultadoEJB.crearErroneo(3,update,"Error al actualizar");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, " Error al actualizar los datos personales del Estudiante(EjbReinscripcionAutonoma.updateDatosPersonales )", e, null);
        }
    }


}