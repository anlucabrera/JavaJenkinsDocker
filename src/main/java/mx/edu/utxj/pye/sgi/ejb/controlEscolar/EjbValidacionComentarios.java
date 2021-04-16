package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCapturaCalificacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaComentario;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCapturaCalificacionAlineacion;

@Stateless
public class EjbValidacionComentarios {
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }

    /**
     * Permite filtrar los estudiantes que cuenten con una calificación reprobatoria en la unidaded de materia
     * @param dtoGrupoEstudiante Empaquetado que contiene las líneas de captura de calificaciones por unidad correspondientes a un grupo de estudiantes
     * @return Regresa un empaquetado con las líneas de captura reprobatorias del grupo<br/>
     * Código 0: error desconocido
     */
    public ResultadoEJB<DtoGrupoEstudiante> filtrarReprobados(DtoGrupoEstudiante dtoGrupoEstudiante){
        try{
            List<DtoCapturaCalificacion> collect = dtoGrupoEstudiante.getEstudiantes()
                    .stream()
                    .filter(dtoCapturaCalificacion -> dtoCapturaCalificacion.getPromedio().compareTo(ejbCapturaCalificaciones.leerCalificacionMínimaAprobatoria()) == -1)
                    .collect(Collectors.toList());

            DtoGrupoEstudiante dtoGrupoEstudiante1 = new DtoGrupoEstudiante(dtoGrupoEstudiante.getDtoCargaAcademica(), collect);
            return ResultadoEJB.crearCorrecto(dtoGrupoEstudiante1, "Estudiantes con calificación reprobatoria filtrados");
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "No se pudieron filtrar los estudiantes reprobados del grupo", e, DtoGrupoEstudiante.class);
        }
    }

    /**
     * Permite eliminar un comentario reprobatorio si se cumplen las condiciones que el estudiante ya tenga un promedio aprobatorio y exista un comentario
     * @param dtoCapturaCalificacion Empaquetado de la captura de calificación del estudiante por unidad
     * @return Devuelve TRUE si se pudo eliminar el comentario, código 1 para error desconocido,
     * código 2 para indicar que no se puede eliminar el comentario ya que el promedio es reprobatorio,
     * código 3 para indicar que no existe comentario para eliminar,
     * código 4 para un caso no identificado en el que no se pueda eliminar, realizar debug si se presta el caso 4
     */
    public ResultadoEJB<Boolean> eliminarComentarioReprobatorio(DtoCapturaCalificacion dtoCapturaCalificacion){
        try{
            if(dtoCapturaCalificacion.getEstaAprobado() && dtoCapturaCalificacion.getTieneComentarioReprobatorio()){
                UnidadMateriaComentario unidadMateriaComentario = em.find(UnidadMateriaComentario.class, dtoCapturaCalificacion.getComentarioReprobatorio().getUnidadMateriaComentarioPK());
                em.remove(unidadMateriaComentario);
                dtoCapturaCalificacion.setComentarioReprobatorio(null);
                dtoCapturaCalificacion.setTieneComentarioReprobatorio(false);
                return ResultadoEJB.crearCorrecto(true, "El comentario fue eliminado correctamente");
            }else {
                if(!dtoCapturaCalificacion.getEstaAprobado()) return ResultadoEJB.crearErroneo(2, "No se puede eliminar el comentario por reprobación ya que el promedio es reprobatorio.", Boolean.TYPE);
                if(!dtoCapturaCalificacion.getTieneComentarioReprobatorio())return ResultadoEJB.crearErroneo(3, "No se puede eliminar el comentario por reprobación ya no se tiene un comentario registrado.", Boolean.TYPE);
                return ResultadoEJB.crearErroneo(4, "Caso no considerado, no se eliminó el comentario", Boolean.TYPE);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite eliminar un comentario reprobatorio si se cumplen las condiciones que el estudiante ya tenga un promedio aprobatorio y exista un comentario
     * @param dtoCapturaCalificacion Empaquetado de la captura de calificación del estudiante por unidad
     * @return Devuelve TRUE si se pudo eliminar el comentario, código 1 para error desconocido,
     * código 2 para indicar que no se puede eliminar el comentario ya que el promedio es reprobatorio,
     * código 3 para indicar que no existe comentario para eliminar,
     * código 4 para un caso no identificado en el que no se pueda eliminar, realizar debug si se presta el caso 4
     */
    public ResultadoEJB<Boolean> eliminarComentarioReprobatorioAlineacion(DtoCapturaCalificacionAlineacion dtoCapturaCalificacion){
        try{
            if(dtoCapturaCalificacion.getEstaAprobado() && dtoCapturaCalificacion.getTieneComentarioReprobatorio()){
                UnidadMateriaComentario unidadMateriaComentario = em.find(UnidadMateriaComentario.class, dtoCapturaCalificacion.getComentarioReprobatorio().getUnidadMateriaComentarioPK());
                em.remove(unidadMateriaComentario);
                dtoCapturaCalificacion.setComentarioReprobatorio(null);
                dtoCapturaCalificacion.setTieneComentarioReprobatorio(false);
                return ResultadoEJB.crearCorrecto(true, "El comentario fue eliminado correctamente");
            }else {
                if(!dtoCapturaCalificacion.getEstaAprobado()) return ResultadoEJB.crearErroneo(2, "No se puede eliminar el comentario por reprobación ya que el promedio es reprobatorio.", Boolean.TYPE);
                if(!dtoCapturaCalificacion.getTieneComentarioReprobatorio())return ResultadoEJB.crearErroneo(3, "No se puede eliminar el comentario por reprobación ya no se tiene un comentario registrado.", Boolean.TYPE);
                return ResultadoEJB.crearErroneo(4, "Caso no considerado, no se eliminó el comentario", Boolean.TYPE);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
}
