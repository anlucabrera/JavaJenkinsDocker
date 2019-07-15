package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.EstudianteDto;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless(name = "EjbReinscripcionAutonoma")
public class EjbReinscripcionAutonoma {
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    Integer claveMateria;
    /**
     * Permite validar si el usuario autenticado es un estudiante
     * @param clave Número de identificación del estudiante autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<EstudianteDto>> validarEstudiante(Integer clave){
        try{
            EstudianteDto p = ejbEstudianteBean.pack(clave);
            Filter<EstudianteDto> filtro = new Filter<>();
            filtro.setEntity(p);
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbReinscripcionAutonoma.validarEstudiante)", e, null);
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
    public ResultadoEJB<Grupo> getGrupoInmediatoSuperior(EstudianteDto estudiante, EventoEscolar eventoEscolar){
        try{
            Integer gradoAct = estudiante.getEstudiante().getGrupo().getGrado();
            Integer gradoSup = gradoAct + 1;
            // buscar grupo inmediato superior para reinscribirse (con la misma literal)
            Grupo grupoSup = f.getEntityManager().createQuery("select g from Grupo g where g.periodo =:periodo and g.idPe =:programa and g.grado =:grado and g.literal =:literal", Grupo.class)
                    .setParameter("periodo", eventoEscolar.getPeriodo())
                    .setParameter("programa", estudiante.getProgramaEducativo().getArea())
                    .setParameter("grado", gradoSup)
                    .setParameter("literal", estudiante.getEstudiante().getGrupo().getLiteral())
                    .getSingleResult();
            return ResultadoEJB.crearCorrecto(grupoSup, "Grupo superior inmediato");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener grupo inmediato superior. (EjbReinscripcionAutonoma.getGrupoInmediatoSuperior)", e, null);
        }
    }
    
    /**
     * Permite la asignación de grupo al estudiante
     * @param grupo Grupo que se asignará al estudiante
     * @param estudiante Estudiante al que se le asignará el grupo correspondiente
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> asignarGrupo(Grupo grupo, Estudiante estudiante){
        /*try{
            if(grupo == null) return ResultadoEJB.crearErroneo(2, "El grupo no debe ser nulo.", Estudiante.class);
            if(estudiante == null) return ResultadoEJB.crearErroneo(3, "El estudiante no debe ser nulo.", Estudiante.class);

            TypedQuery<Estudiante> q1 = f.getEntityManager().createQuery("update Estudiante e SET e.grupo =:grupo AND e.periodo =:periodo WHERE e.idEstudiante =:estudiante", Estudiante.class);
            q1.setParameter("grupo", grupo.getIdGrupo());
            q1.setParameter("periodo", grupo.getPeriodo());
            q1.setParameter("estudiante", estudiante.getIdEstudiante());
            q1.executeUpdate();
            
            Estudiante asignaGrupo = f.getEntityManager().find(Estudiante.class, estudiante.getIdEstudiante());
            return ResultadoEJB.crearCorrecto(asignaGrupo, "Grupo asignado"); 
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar el grupo al estudiante. (EjbReinscripcionAutonoma.asignarGrupo)", e, null);
        }*/
        return null;
    }

    /**
     * Permite obtener la lista de materias por asignar al estudiante recien inscrito
     * @param programa Programa al que deben pertenecer las materias
     * @param grupo Grupo al que deben pertenecer las materias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getMateriasPorAsignar(AreasUniversidad programa, Grupo grupo){
        /*try{
            //TODO: buscar lista de materias por asignar que pertenecen al grupo seleccionado
            List<Materia> materiasPorAsignar = f.getEntityManager().createQuery("select m from Materia m inner join m.idPlan p where p.idPe=:programaEducativo and m.grado =:grado and m.estatus =:estatus", Materia.class)
                    .setParameter("programaEducativo", programa.getArea())
                    .setParameter("grado", grupo.getGrado())
                    .setParameter("estatus", true)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(materiasPorAsignar, "Lista de materias para asignar al estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por asignar. (EjbReinscripcionAutonoma.getMateriasPorAsignar)", e, null);
        }*/
        return null;
    }
   
    /**
     * Permite la asignación de materias que cursará el estudiante
     * @param estudiante Estudiante al que se le asignarán las materias
     * @param materias Lista de materias que se asignarán
     * @return Resultado del proceso
     */
    public ResultadoEJB<Calificacion> asignarMateriasEstudiante(Estudiante estudiante, List<Materia> materias){
        /*try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Calificaciones.class);
            if(materias.isEmpty()) return ResultadoEJB.crearErroneo(3, "La lista de materias no puede ser vacia.", Calificaciones.class);
            
            //Obtener la clave de la materia
            materias.forEach(mat -> {claveMateria = mat.getIdMateria();});
            Materia materia = f.getEntityManager().find(Materia.class, claveMateria);
            
            CalificacionesPK pk = new CalificacionesPK(estudiante.getIdEstudiante(), estudiante.getGrupo().getIdGrupo(), claveMateria);
            Calificaciones calificaciones = f.getEntityManager().createQuery("select c from Calificaciones c where c.estudiante1.idEstudiante =:estudiante and c.estudiante1.grupo =:grupo and c.materia1.idMateria =:materia", Calificaciones.class)
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .setParameter("grupo", estudiante.getGrupo().getIdGrupo())
                    .setParameter("materia", claveMateria)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (calificaciones == null) {//comprobar si la asignación existe para impedir  duplicar
                calificaciones = new Calificaciones();
                calificaciones.setCalificacionesPK(pk);
                calificaciones.setEstudiante1(estudiante);
                calificaciones.setMateria1(materia);
                f.create(calificaciones);
                return ResultadoEJB.crearCorrecto(calificaciones, "La asignación fué registrada correctamente.");
            }
            return ResultadoEJB.crearErroneo(2, "La asignación ya fue realizada. (EjbReinscripcionAutonoma.asignarMateriasEstudiante)", Calificaciones.class);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar las materias. (EjbReinscripcionAutonoma.asignarMateriasEstudiante)", e, null);
        }*/
        return null;
    }
}