package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteReinscripcion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMatariaPromedio;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import java.awt.geom.Area;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import org.apache.commons.jexl2.parser.ParserConstants;
import org.apache.poi.ddf.EscherRecord;

@Stateless(name = "EjbReinscripcionExtemporaneaSE")

public class EjbReinscripcionExtemporaneaSE {

    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbReinscripcionAutonoma ejbReinscripcionAutonoma;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;


    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    /**
     * Verifica que el personal logueado sea de Servicios Escolares
     * @param clave Clave del trabajador logueado
     * @return Resultado del Proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaSE(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReinscripcionExtemporaneaSE.validaSE)", e, null);
        }
    }

    /**
     * Obtiene el periodo actual
     * @return Resultado del proceso (Periodo activo)
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoEscolarActivo(){
        try{
            PeriodosEscolares p = new PeriodosEscolares();
            StoredProcedureQuery spq =em.createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
            List<PeriodosEscolares> l = spq.getResultList();

            if (l == null || l.isEmpty()) {
                return ResultadoEJB.crearErroneo(2,p,"No hay periodo activo");
            } else {
                p=l.get(0);
                return ResultadoEJB.crearCorrecto(p,"Periodo activo ");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo escolar activo(EjbReinscripcionExtemporaneaSE.getPeriodoEscolarActivo))", e, null);

        }
    }

    /**
     * Obtiene el último evento escolar de tipo Resinscripcion
     * @return Resultado del proceso (Evento Escolar de Reinscripcion )
     */
   public ResultadoEJB<EventoEscolar> getUltimoEventoReinscripcion(){
        try{
            EventoEscolar eventoEscolar = new EventoEscolar();
            //Se busca el ultimo evento de tipo Reinscripcion
            eventoEscolar = em.createQuery("select e from EventoEscolar  e where e.tipo=:tipo order by e.periodo",EventoEscolar.class)
            .setParameter("tipo","Reinscripción_autonóma")
            .getResultStream()
            .findFirst()
            .orElse(null);
            ;
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(3,eventoEscolar,"No se encontro evento");}
            else {return ResultadoEJB.crearCorrecto(eventoEscolar,"Evento escolar");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el útimo evento de reinscripcion(EjbReinscripcionExtemporaneaSE.getUltimoEventoReinscripcion))", e, null);
        }

    }

    /**
     * Obtiene una lista de estudiantes regulares que estan inscritos en el periodo programado segun el evento de reinscripción
     * @param eventoEscolar Ultimo evento escolar de tipo Reinscripcion
     * @return Resultado del proces (Lista de estudiantes inscritos al periodo programado)
     */

    public ResultadoEJB<List<Estudiante>> getEstudiantesReinscritos(EventoEscolar eventoEscolar){
        try {
            List<Estudiante> listEstudiantesInscritos = new ArrayList<>();
            listEstudiantesInscritos = em.createQuery("select e from Estudiante e where e.tipoEstudiante.idTipoEstudiante=:tipo and e.periodo=:periodo and e.tipoRegistro =:tipoR or e.tipoRegistro=:tipoR2",Estudiante.class)
                .setParameter("tipo",1)
                .setParameter("periodo",eventoEscolar.getPeriodo())
                    .setParameter("tipoR","Reinscripcion Autónoma")
                    .setParameter("tipoR2", "Reinscripcion")
                .getResultList()
            ;
            if(listEstudiantesInscritos==null || listEstudiantesInscritos.isEmpty()){return ResultadoEJB.crearErroneo(4,listEstudiantesInscritos,"No se encontraron estudiantes inscritos en el periodo programado.");}
            else {return ResultadoEJB.crearCorrecto(listEstudiantesInscritos,"Lista de estudiantes inscritos en el periodo programado");}


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes reinscritos en periodo programado(EjbReinscripcionExtemporaneaSE.getEstudiantesReinscritos))", e, null);

        }
    }

    /**
     * Obtiene una lista de estudiantes regulares que estaban inscritos en el periodo anterior al periodo programaado para la reinscripción
     * @param eventoEscolar
     * @return
     */
    public ResultadoEJB<List<Estudiante>> getEstudiantesActivosbyPeriodoAnterior(EventoEscolar eventoEscolar){
        try {
            List<Estudiante> listEstudiantes= new ArrayList<>();
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(2,listEstudiantes,"El evento no debe ser nulo");}
            Integer periodoAnterior = eventoEscolar.getPeriodo()-1;
            //Busca a los estudiantes regulares que estaban inscritos al periodo anterior al del periodo del evento programado
            listEstudiantes = em.createQuery("select e from Estudiante e where e.tipoEstudiante.idTipoEstudiante=1 and e.periodo=:periodo",Estudiante.class)
            .setParameter("periodo",periodoAnterior)
            .getResultList()
            ;
            if(listEstudiantes==null || listEstudiantes.isEmpty()){return  ResultadoEJB.crearErroneo(3,listEstudiantes,"No se encontraron estudiantes regulares inscritos al periodo anterior");}
            else {//System.out.println("Estudiantes periodo Anterior :->" + listEstudiantes.size());
                 return ResultadoEJB.crearCorrecto(listEstudiantes,"Lista de estudiantes");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes regulares inscritos al periodo anaterior (EjbReinscripcionExtemporaneaSE.getEstudiantesActivosbyPeriodoAnterior))", e, null);
        }
    }

    /**
     * Busca al estudiante segun el periodo del evento
     * @param eventoEscolar Ultimo evento escoclar de reinscripción autónoma
     * @param estudiante Estudiante regular del periodo anterios
     * @return Resultado del proceso (Estudiante)
     */
    public ResultadoEJB<Estudiante> getEstudianteEvento (EventoEscolar eventoEscolar, Estudiante estudiante){
        try{
            Estudiante estudianteReinscrito = new Estudiante();
            if(eventoEscolar ==null){return  ResultadoEJB.crearErroneo(2,estudianteReinscrito,"El evento no debe ser nulo");}
            if(estudiante==null){return  ResultadoEJB.crearErroneo(3,estudianteReinscrito,"El estudiante no debe ser nulo");}
            //Se hace la consulta -- Busca al estudiante por matricula y por el periodo del evento
            estudianteReinscrito = em.createQuery("select e from Estudiante e where e.matricula=:matricula and e.periodo =:periodo",Estudiante.class)
            .setParameter("matricula",estudiante.getMatricula())
            .setParameter("periodo",eventoEscolar.getPeriodo())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(estudianteReinscrito ==null){return ResultadoEJB.crearErroneo(4,estudianteReinscrito,"El estudiante no se ha reinscrito en el periodo programado");}
            else {return ResultadoEJB.crearCorrecto(estudianteReinscrito,"Estudiante reinscrito");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al estudiante (EjbReinscripcionExtemporaneaSE.getEstudianteEvento))", e, null);
        }
    }


    /**
     * Obtiene una lista de estudiantes que no se han reinscrito al periodo programado
     * -----Pasos
     * 1.- Obtiene el último evento escolar de tipo Reinscripcion Autonoma
     * 2.- Obtiene la lista de estudiantes regulares que estaban inscritos en el periodo anterior segun el evento programado
     * 3.- Recorre la lista de estudiantes inscritos y busca si ya se ha reinscrito
     * @return Resultado del proceso(Lista de estudiantes que no se han reinscrito)
     */
   public ResultadoEJB<List<Estudiante>> getEstudiantesNoReinscritos (){
        try {
            List<Estudiante> listaEstudiantesNoReinscritos = new ArrayList<>();
            //Obtiene último evento escolar de tipo reisncripción
            ResultadoEJB<EventoEscolar> resEvento = getUltimoEventoReinscripcion();
            if(resEvento.getCorrecto()==true){
                //Se buscan a los estudiantes regulares que estaban inscritos en el periodo anterior al evento programado
                List<Estudiante> listEstudiantesInscritosByPeriodoAnterior = new ArrayList<>();
                ResultadoEJB<List<Estudiante>> resEstudiantesInscritos = getEstudiantesActivosbyPeriodoAnterior(resEvento.getValor());
                if(resEstudiantesInscritos.getCorrecto()==true){listEstudiantesInscritosByPeriodoAnterior= resEstudiantesInscritos.getValor();}
                else {return ResultadoEJB.crearErroneo(3,listaEstudiantesNoReinscritos,"Error al obtener estudiantes inscritos en periodo anterior");}
                List<Estudiante> reinscritos2 = new ArrayList<>();
                //Se recorren la lista de estudiantes que estaban inscritos en el periodo anterior
                listEstudiantesInscritosByPeriodoAnterior.stream().forEach(ei->{
                    //Verifica que el estudiante este inscrito en el periodo del evento programado, en caso de que no se agrega a la lista de estudiantes no reinscritoss
                ResultadoEJB<Estudiante> resReinscrito = getEstudianteEvento(resEvento.getValor(),ei);
                if(resReinscrito.getCorrecto()!=true){
                    //El estudiante no ha realizado su inscripción en el periodo programado
                    listaEstudiantesNoReinscritos.add(ei);
                }
                else {reinscritos2.add(ei);}
                });
               // System.out.println("Estudiantes regulares Periodo Anterior ->"+listEstudiantesInscritosByPeriodoAnterior.size());
                //System.out.println("Estudiantes No reinscritos ->"+listaEstudiantesNoReinscritos.size());
                //System.out.println("Estudiantes reinscritos 2  ->"+reinscritos2.size());
                // TODO: Buscamos la carrera a la que pertenece
                //TODO: Se busca su tutor de grupo

                return ResultadoEJB.crearCorrecto(listaEstudiantesNoReinscritos,"Estudiantes no reinscritos");


            }else {
                return ResultadoEJB.crearErroneo(2,listaEstudiantesNoReinscritos,"Error en buscar último evento ");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes reinscritos (EjbReinscripcionExtemporaneaSE.getEstudiantesNoReinscritos))", e, null);

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
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista cargas cadémicas por grupo y periodo (EjbReinscripcionExtemporaneaSE.getCargaAdemicabyGrupo).", e, null);

        }
    }

    /**
     * Obtiene un nuevo grupo para el estudiante (El grupo al que se va a reinscribir)
     * Primero busca el grupo inmediato (desde el ejb de reinscripcion autonoma), en caso de que no lo encuentre, busca un grupo alterno
     * @param estudiante Estudiante en el periodo anterior al periodo programado
     * @param eventoEscolar Evento escolar de reinscripcion autonoma (De ahí se toma el periodo del grupo)
     * @return Resultado del proceso
     */
    public  ResultadoEJB<Grupo> getNuevoGrupo(Estudiante estudiante,EventoEscolar eventoEscolar){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new Grupo(),"El estudiante no debe ser nulo");}
            if(eventoEscolar ==null){return ResultadoEJB.crearErroneo(3,new Grupo(),"El evento escolar no debe ser nulo");}
            //Se obtiene el grupo inmediato (Ejb Reinscripción Autonoma)
            ResultadoEJB<Grupo> resGrupoInmediato = ejbReinscripcionAutonoma.getGrupoInmediatoSuperior(estudiante,eventoEscolar);
            //System.out.println("Grupo Inmediato->"+ resGrupoInmediato.getValor() );
            if(resGrupoInmediato.getCorrecto()==true){ return ResultadoEJB.crearCorrecto(resGrupoInmediato.getValor(),"Grupo inmediato");}
            else {
                //Se obtiene el grupo alterno(Ejb Reinscripcion autonoma)
                ResultadoEJB<Grupo> resGrupoAlterno = ejbReinscripcionAutonoma.getGrupoAlterno(estudiante,eventoEscolar);
              //  System.out.println("Grupo Alternpo->"+ resGrupoAlterno.getValor() );

                if(resGrupoAlterno.getCorrecto()==true){return ResultadoEJB.crearCorrecto(resGrupoAlterno.getValor(),"Grupo alterno");}
                else { return ResultadoEJB.crearErroneo(4,resGrupoAlterno.getValor(),"No existe algún grupo para el estudiante en el periodo programado");}
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al obtener el nuevo grupo del estudiante (EjbReinscripcionExtemporaneaSE.getNuevoGrupo).", e, null);
        }

    }

    /**
     * Obtiene el promedio por materia segun la carga acedemica
     * @param cargaAcademica Carga academica del estudiante
     * @param estudiante Estudiante
     * @return DtoMateriaPromedio Promedio de la materia
     */
    public ResultadoEJB<CalificacionPromedio> getPromedioMateria (DtoCargaAcademica cargaAcademica,Estudiante estudiante){
        try{
            CalificacionPromedio calificacionPromedio = new CalificacionPromedio();
            if(cargaAcademica==null){return ResultadoEJB.crearErroneo(2,calificacionPromedio,"La carga académica no debe ser nula");}
            if(estudiante==null){return ResultadoEJB.crearErroneo(3,calificacionPromedio,"El estudiante no debe ser nulo");}
            //Se busca el promedio de la carga

            calificacionPromedio = em.createQuery("select c from CalificacionPromedio  c where c.calificacionPromedioPK.carga=:carga and c.calificacionPromedioPK.idEstudiante=:id",CalificacionPromedio.class)
                    .setParameter("carga",cargaAcademica.getCargaAcademica().getCarga())
                    .setParameter("id",estudiante.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(calificacionPromedio ==null){return ResultadoEJB.crearErroneo(4,calificacionPromedio,"No se encontro promedio de la materia");}
            else {return ResultadoEJB.crearCorrecto(calificacionPromedio,"Promedio de l materia");}
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error a l obtener el promedio de la materia (EjbReinscripcionExtemporaneaSE.getPromedioMateria).", e, null);
        }
    }
    public  ResultadoEJB<CalificacionNivelacion> getNivelacion (DtoCargaAcademica cargaAcademica, Estudiante estudiante){
        try {
            CalificacionNivelacion nivelacion = new CalificacionNivelacion();
            if(cargaAcademica ==null){return ResultadoEJB.crearErroneo(2,nivelacion,"La carga académica no debe ser nula");}
            if(estudiante ==null){return ResultadoEJB.crearErroneo(3,nivelacion,"El estudiante no debe ser nulo");}
            nivelacion = em.createQuery("select c from CalificacionNivelacion c where c.calificacionNivelacionPK.idEstudiante=:idEstudiante and c.calificacionNivelacionPK.carga =:carga", CalificacionNivelacion.class)
            .setParameter("idEstudiante", estudiante.getIdEstudiante())
            .setParameter("carga",cargaAcademica.getCargaAcademica().getCarga())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(nivelacion==null){return ResultadoEJB.crearErroneo(4,nivelacion,"No se encontro calificación de nivelación");}
            else {return ResultadoEJB.crearCorrecto(nivelacion,"Calificación de nivelación");}
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al obtener la calificación de nivelacion (EjbReinscripcionExtemporaneaSE.getNivelacion).", e, null);
        }
    }

    /**
     * Obitene la lista de promedios por materia del estududiante
     * @param estudiante Estudiante (No reinscrito)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoMatariaPromedio>> getCalilficacionesbyEstudiante(Estudiante estudiante){
        try{
            List<DtoMatariaPromedio> calificacionPromedios = new ArrayList<>();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,calificacionPromedios,"El estudiante no debe ser nulo");}
            //Se obtienen la listas de cargas académicas del estudiante
            ResultadoEJB<List<DtoCargaAcademica>> resCargasA = getCargaAdemicabyGrupo(estudiante.getGrupo());
            if(resCargasA.getCorrecto()==true){
                //Recorre la lista de cargas académicas e ir buscando el promedio por materia
                resCargasA.getValor().forEach(ca-> {
                    DtoMatariaPromedio dto = new DtoMatariaPromedio();
                    dto.setCargaAcademica(ca);
                    //Se obiene el promedio de esa materia -carga
                    ResultadoEJB<CalificacionPromedio> resProm = getPromedioMateria(ca,estudiante);
                   if(resProm.getCorrecto()==true){
                       dto.setPromedioOrdinario(BigDecimal.valueOf(resProm.getValor().getValor()));
                       if(dto.getPromedioOrdinario().compareTo(BigDecimal.valueOf(8))<0){
                           //Se obtiene la calificacion de nivelacion
                           ResultadoEJB<CalificacionNivelacion> resNivelacion = getNivelacion(ca,estudiante);
                           if(resNivelacion.getCorrecto()==true){
                               dto.setNivelacion(BigDecimal.valueOf(resNivelacion.getValor().getValor()));
                               dto.setPromedio(dto.getNivelacion());
                               calificacionPromedios.add(dto);
                           }
                           else {dto.setPromedio(dto.getPromedioOrdinario());calificacionPromedios.add(dto);}
                       }
                       else {dto.setPromedio(dto.getPromedioOrdinario());calificacionPromedios.add(dto);}
                   }
                });
                return ResultadoEJB.crearCorrecto(calificacionPromedios,"Lista de promedios");
            }else {return ResultadoEJB.crearErroneo(3,calificacionPromedios,"Error al obtener las cargas academicas del estudiante");}
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error a l obtener el promedio de la materia (EjbReinscripcionAutonoma.getCargaAdemicabyGrupo).", e, null);
        }
    }
    ResultadoEJB<Boolean> compruebaAprobo (List<DtoMatariaPromedio> promedioMaterias){
        try{
            if(promedioMaterias==null){return ResultadoEJB.crearErroneo(2,new Boolean(false),"La lista de calificaiones por materia no debe ser nula");}
            long matR = 0;
            Boolean aprobo;
            matR = promedioMaterias.stream().filter(p->p.getPromedio().compareTo(BigDecimal.valueOf(8))<0).count();
            if(matR>0){
               // System.out.println("No aprobo");
                aprobo =false;
                return ResultadoEJB.crearCorrecto(aprobo,"El estudiante tiene materias reprobadas");
            }else {//System.out.println("Aprobo");
                //System.out.println("Aprobo");
                aprobo =true;
                return ResultadoEJB.crearCorrecto(aprobo,"El estudiante acredito todas sus materias");
            }

        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al comprobar si el estudiante acredito todas sus materias (EjbReinscripcionExtemporaneaSE.compruebaAprobo).", e, null);
        }

    }

    /**
     * Empaqueta una lista de estudiantes :
     * Estudiante
     * Carrera del estudiante
     * Grupo Actual
     * Tutor de grupo actual
     * Nuevo Grupo
     * Lista de promedio por materias
     * Cantidad de materias reprobadas
     * Aprobo
     * @param estudiantes Lista de estudiantes
     * @param eventoEscolar Evento de Reinscripcion Autonoma
     * @return Resultado del proceso
     */

    public ResultadoEJB<List<DtoEstudianteReinscripcion>> packEstudianteReinscripcion(List<Estudiante> estudiantes,EventoEscolar eventoEscolar){
        try{
            List<DtoEstudianteReinscripcion> estudiantesPack = new ArrayList<>();
            if(estudiantes==null){return ResultadoEJB.crearErroneo(2,estudiantesPack,"La lista de estudiantes no debe ser nula");}
            if(eventoEscolar ==null){return ResultadoEJB.crearErroneo(3,estudiantesPack,"El evento escolar no debe ser nulo");}
            estudiantes.stream().forEach(e->{
                DtoEstudianteReinscripcion dto = new DtoEstudianteReinscripcion();
                //Asigana al estudiante
                dto.setEstudiante(e);
                //Obtenemos la carrera del estudiante
                ResultadoEJB<AreasUniversidad> resCarrera = getCarreraEstudiante(e);
                if(resCarrera.getCorrecto()==true){dto.setCarrera(resCarrera.getValor());}
                else {//System.out.println("Error al obtener la carrera");return;
                }
                //Grupo actual
                dto.setGrupoActual(e.getGrupo());
                //Tutor del grupo
                if(e.getGrupo().getTutor()==null){
                    dto.setNombreTutor("Sin asignar");
                }else {
                    ResultadoEJB<Personal> resTutor =getTutorEstudiante(e);
                    if(resTutor.getCorrecto()==true){dto.setNombreTutor(resTutor.getValor().getNombre());}
                    else {//System.out.println("Error al obtener al tutor");
                    }
                }
                //Valida si el estudiante esta inscrito en el periodo del evento(En caso de que si, no existirá nuevo grupo)
                if(e.getPeriodo()==eventoEscolar.getPeriodo()-1){
                    //El estudiante aun esta inscrito en el periodo anterior al evento de reinscripcion, entonces se puede obtener el grupo
                    //Se busca su nuevo grupo
                    ResultadoEJB<Grupo> resNuevoGrupo = getNuevoGrupo(e,eventoEscolar);
                    if(resNuevoGrupo.getCorrecto()==true){dto.setNuevoGrupo(resNuevoGrupo.getValor());}
                    else {//System.out.println("Error al obtener grupo");return;
                    }
                }
                //Personal que inscribió en caso que el tipo de registro sea "Reinscripción"
                if(e.getTrabajadorInscribe()!=null & e.getTipoRegistro().equals("Reinscripción")){
                    PersonalActivo resTrabIns = ejbPersonalBean.pack(e.getTrabajadorInscribe());
                    dto.setTrabajadorIncribe(resTrabIns.getPersonal());
                }
                //Lista de promedio por materias
                ResultadoEJB<List<DtoMatariaPromedio>> resPromedios = getCalilficacionesbyEstudiante(e);
                if(resPromedios.getCorrecto()==true){dto.setPromedioMateria(resPromedios.getValor());}
                else {//System.out.println("Error al obetener los promedios de la materia");return;
                }
                if(e.getPeriodo()==eventoEscolar.getPeriodo()-1){
                    //Cantidad de materias reprobadas
                    dto.setMateriasReprobadas((int) dto.getPromedioMateria().stream().filter(c->c.getPromedio().compareTo(BigDecimal.valueOf(8))<0).count());
                    //Comprobar si aprobo todas sus materias
                    ResultadoEJB<Boolean> resAprobo = compruebaAprobo(dto.getPromedioMateria());
                    if(resAprobo.getCorrecto()==true){dto.setAprobo(resAprobo.getValor());}
                    else {//System.out.println("Error en comprobar si reprobo algunas materias");
                    return;
                    }

                }
                estudiantesPack.add(dto);

            });
            //System.out.println("Empaquetado!");
            return ResultadoEJB.crearCorrecto(estudiantesPack,"Lista de estudiantes empaquetado");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al empaquetar a los estudiantes(EjbReinscripcionExtemporaneaSE.packEstudianteReinscripcion).", e, null);
        }
    }

    /**
     * Reinscibe al estudiante en el periodo programado
     * @param estudianteReinscripcion Estudiante regular del periodo anterior al programado
     * @param eventoEscolar Ultimo evento de "Reinscripción autonoma"
     * @param trabajadorInscribe Trabajador logueado
     * @return Resultado del proceso
     * Tipos Errores:
     * 2-> Estudiante nulo
     * 3-> Nuevo grupo nulo
     * 4-> Evento escolar nulo
     * 5-> Trabajador logueado nulo
     * 6 -> El estudiante no acreditado el cuatrimestre
     */
    public ResultadoEJB<Estudiante> reinscribirEstudiante(DtoEstudianteReinscripcion estudianteReinscripcion, EventoEscolar eventoEscolar, PersonalActivo trabajadorInscribe){
        try{
            Estudiante nuevoEstudiante = new Estudiante();
            if(estudianteReinscripcion.getEstudiante()==null){return ResultadoEJB.crearErroneo(2,nuevoEstudiante,"El estudiante no debe ser nulo");}
            if(estudianteReinscripcion.getNuevoGrupo() ==null){return ResultadoEJB.crearErroneo(3,nuevoEstudiante,"El nuevo grupo no debe ser nulo");}
            if(eventoEscolar==null){return ResultadoEJB.crearErroneo(4,nuevoEstudiante,"El evento no debe ser nulo");}
            if(trabajadorInscribe==null){return ResultadoEJB.crearErroneo(5,nuevoEstudiante,"El personal que inscribe no debe ser nulo");}
            if(estudianteReinscripcion.getAprobo()==false){
                return ResultadoEJB.crearErroneo(6,nuevoEstudiante,"¡Estudiante no acreditado!. No se puede reinscribir al periodo programado");
            }else {
                //Se le asignan los valores al estudiante
                nuevoEstudiante.setMatricula(estudianteReinscripcion.getEstudiante().getMatricula());
                nuevoEstudiante.setAspirante(estudianteReinscripcion.getEstudiante().getAspirante());
                nuevoEstudiante.setCarrera(estudianteReinscripcion.getEstudiante().getCarrera());
                nuevoEstudiante.setGrupo(estudianteReinscripcion.getNuevoGrupo());
                nuevoEstudiante.setTipoEstudiante(estudianteReinscripcion.getEstudiante().getTipoEstudiante());
                nuevoEstudiante.setPeriodo(eventoEscolar.getPeriodo());
                nuevoEstudiante.setOpcionIncripcion(false);
                nuevoEstudiante.setFechaAlta(new Date());
                nuevoEstudiante.setTrabajadorInscribe(trabajadorInscribe.getPersonal().getClave());
                nuevoEstudiante.setTipoRegistro("Reinscripción");
                em.persist(nuevoEstudiante);
                return ResultadoEJB.crearCorrecto(nuevoEstudiante,"Estudiante reincrito con exito en el periodo programado");

            }

        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Error al reinscribir al estudiante.(EjbReinscripcionExtemporaneaSE.reinscribirEstudiante).", e, null);
        }
    }

    /**
     * Busca la carrera del estudiante
     * @param estudiante Estudiante no reinscrito
     * @return Resultado del proceso
     */

    public ResultadoEJB<AreasUniversidad> getCarreraEstudiante(Estudiante estudiante){
       try{
           AreasUniversidad carrera = new AreasUniversidad();
           if(estudiante ==null){return ResultadoEJB.crearErroneo(2,carrera,"El estudiante no debe ser nulo");}
           carrera = em.createQuery("select a from AreasUniversidad a where a.area=:area",AreasUniversidad.class)
           .setParameter("area", estudiante.getCarrera())
           .getResultStream()
           .findFirst()
           .orElse(null)
           ;
           if(carrera !=null){return ResultadoEJB.crearCorrecto(carrera,"Carrera del estudiante");}
           else {return ResultadoEJB.crearErroneo(3,carrera,"No se encontro la carrera");}

       }catch (Exception e){
           return ResultadoEJB.crearErroneo(1, "No se pudo obtener la carrera del estudiante (EjbReinscripcionExtemporaneaSE.getCarreraEstudiante))", e, null);
       }
    }

    /**
     * Obtiene al tutor del estudiante
     * @param estudiante Estudiante no reinscrito
     * @return Resultado del proceso
     */
    public ResultadoEJB<Personal> getTutorEstudiante (Estudiante estudiante){
        try{
            Personal tutor = new Personal();
            if(estudiante ==null){return ResultadoEJB.crearErroneo(2,tutor,"El estudiante no debe ser nulo");}
            tutor = em.createQuery("select p from Personal p where p.clave=:clave", Personal.class)
            .setParameter("clave",estudiante.getGrupo().getTutor())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(tutor != null){return  ResultadoEJB.crearCorrecto(tutor,"Tutor del estudiante");}
            else {return ResultadoEJB.crearErroneo(3,tutor,"Error en busqueda de tutor");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al tutor del estudiante (EjbReinscripcionExtemporaneaSE.getTutorEstudiante))", e, null);

        }
    }





}
