/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsesoriaEstudianteCe;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroAsesoriaEstudiante")
public class EjbRegistroAsesoriaEstudiante {
    @EJB        Facade                      f;
    @EJB        EjbPacker                   pack;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Método que permite la búsqueda de una asesoría que esta a punto de ser registrada, se realiza esta validación para evitar duplicidad de información
     * @param dtoAsesoriaEstudianteCe Entidad que contiene la información que se ocupará para hacer la búsqueda correspondiente
     * @return Devuelve una lista de resultados de las coincidencias, en caso contrario devuelve una lista vacía.
     */
    public ResultadoEJB<List<AsesoriasEstudiantes>> buscaAsesoriaEstudianteCeInsercion(DtoAsesoriaEstudianteCe dtoAsesoriaEstudianteCe) {
        try {
            List<AsesoriasEstudiantes> asesoriasEstudiantes = em.createQuery("SELECT a FROM AsesoriasEstudiantes a WHERE a.personal = :personal AND a.fechaHora = :fechaHora AND a.tipo = :tipo AND a.eventoRegistro = :eventoRegistro", AsesoriasEstudiantes.class)
                    .setParameter("personal", dtoAsesoriaEstudianteCe.getAsesoriaEstudiante().getPersonal())
                    .setParameter("fechaHora", dtoAsesoriaEstudianteCe.getAsesoriaEstudiante().getFechaHora())
                    .setParameter("tipo", dtoAsesoriaEstudianteCe.getAsesoriaEstudiante().getTipo())
                    .setParameter("eventoRegistro", dtoAsesoriaEstudianteCe.getEventosRegistros().getEventoRegistro())
                    .getResultList();
            if(asesoriasEstudiantes.isEmpty()){
                return ResultadoEJB.crearCorrecto(asesoriasEstudiantes, "Puede registrar la asesoría de estudiante, no se han encontrado coincidencias en la base de datos");
            }else{
                return ResultadoEJB.crearErroneo(3, null,"Lista de una posible coincidencia de su registro de asesoría de estudiante");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la asesoría que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaEstudiante.buscaAsesoriaEstudianteCeInsercion.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaEstudiante.buscaAsesoriaEstudianteCeInsercion.Exception).", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de una asesoría que ya ha sido guardada previamente en la base de datos, compara la información recibida con la base de datos, se realiza esta búsqueda para evitar duplicidad de información
     * @param dtoAsesoriaEstudianteCe Entidad que contiene la información que se ocupará para hacer la búsqueda correspondiente
     * @return Devuelve una lista de resultados de las coincidencias, en caso contrario devuelve una lista vacía.
     */
    public ResultadoEJB<List<AsesoriasEstudiantes>> buscaAsesoriaEstudianteCeEdicion(AsesoriasEstudiantes asesoriaEstudiante){
        try {
            List<AsesoriasEstudiantes> asesoriasEstudiantes = em.createQuery("SELECT a FROM AsesoriasEstudiantes a WHERE a.personal = :personal AND a.fechaHora = :fechaHora AND a.tipo = :tipo AND a.eventoRegistro = :eventoRegistro AND a.asesoriaEstudiante <> :asesoriaEstudiante", AsesoriasEstudiantes.class)
                    .setParameter("personal", asesoriaEstudiante.getPersonal())
                    .setParameter("fechaHora", asesoriaEstudiante.getFechaHora())
                    .setParameter("tipo", asesoriaEstudiante.getTipo())
                    .setParameter("eventoRegistro", asesoriaEstudiante.getEventoRegistro())
                    .setParameter("asesoriaEstudiante", asesoriaEstudiante.getAsesoriaEstudiante())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(asesoriasEstudiantes, "Lista de una posible coincidencia de su registro de asesoría de estudiante");
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta actualizar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaEstudiante.buscaAsesoriaEstudianteCeEdicion.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta actualizar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaEstudiante.buscaAsesoriaEstudianteCeEdicion.Exception).", e, null);
        }   
    }
    
    /**
     * Método que permite el registro de una asesoría en el sistema, incluye validación de un registro previo para evitar duplicidad de información
     * @param dtoAsesoriaEstudianteCe Entidad que contiene los valores que serán almacenados en la base de datos
     * @return Devuelve el valor de la entidad almacenada en la base de datos.
     */
    public ResultadoEJB<DtoAsesoriaEstudianteCe> guardarAsesoriaEstudiante(DtoAsesoriaEstudianteCe dtoAsesoriaEstudianteCe){
        try {
            if(buscaAsesoriaEstudianteCeInsercion(dtoAsesoriaEstudianteCe).getCorrecto()){
                AsesoriasEstudiantes asesoriaRegistrada = dtoAsesoriaEstudianteCe.getAsesoriaEstudiante();
                em.persist(asesoriaRegistrada);
                dtoAsesoriaEstudianteCe.setAsesoriaEstudiante(asesoriaRegistrada);
                return ResultadoEJB.crearCorrecto(dtoAsesoriaEstudianteCe, "La asesoria ha sido guardada correctamente en el sistema, favor de asignar sus participantes en la parte inferior de la pantalla activa");
            }else{
                return ResultadoEJB.crearErroneo(2, "El registro de esta asesoría ya se encuentra en sistema, favor de verificar los filtros de información", DtoAsesoriaEstudianteCe.class);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar la asesoria (EjbRegistroAsesoriaEstudiante.guardarAsesoriaEstudiante).", e, DtoAsesoriaEstudianteCe.class);
        }
    }
    
    /**
     * Método que permite la actualización de una asesoría en especifico, actualiza todos los valores nuevos recibidos, valida que los valores nuevos no coincidan con un registro existente
     * @param asesoriaEstudiante Entidad que contiene los valores nuevos de la asesoría
     * @return Devuelve VERDADERO si se llevó a cabo correctamente la actualización, devuelve FALSO si ha ocurrido un error durante la operación
     */
    public ResultadoEJB<Boolean> editaAsesoriaEstudiante(AsesoriasEstudiantes asesoriaEstudiante){
        try {
            if(buscaAsesoriaEstudianteCeEdicion(asesoriaEstudiante).getResultados().isEmpty()){
                em.merge(asesoriaEstudiante);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La actualización de información de la asesoría seleccionada ha sido guardada correctamente");
            }else{return ResultadoEJB.crearErroneo(2, "Los datos que ha registrado corresponden a una asesoría ya existente, favor de verificar su información", Boolean.TYPE);}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la asesoria seleccionada, favor de verificar la siguiente información. (EjbRegistroAsesoriaEstudiante.editaAsesoria) ", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la eliminación de una asesoría en especifico, valida que la asesoría no tenga participantes de lo contrario no podrá eliminarse
     * @param asesoriaEstudiante Valor de tipo Integer que contiene el identificador único de la asesoría que se eliminará
     * @return Devuelve VERDADERO si la eliminación se llevo a cabo correctamente, devuelve FALSE si hubo un error durante la operación o bien si se ha detectado que la asesoría tiene participantes asignados
     */
    public ResultadoEJB<Boolean> eliminaAsesoriaEstudiante(Integer asesoriaEstudiante){
        try {
            AsesoriasEstudiantes a = em.find(AsesoriasEstudiantes.class, asesoriaEstudiante);
            if(a.getEstudianteList().isEmpty()){
                em.remove(a);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La asesoría se ha eliminado correctamente del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, Boolean.FALSE,"No se ha podido eliminar la asesoria seleccionada debido a que tiene participantes asignados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la asesoría, favor de verificar la siguiente información. (EjbRegistroAsesoriaEstudiante.eliminaAsesoriaEstudiante)", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la búsqueda de una lista de asesorías registradas por Personal y Evento de Registro
     * @param personal Valor númerico, identificador único del personal
     * @param eventoRegistro Valor númerico, identificador único del evento de registro
     * @return Devuelve una lista de Entidades de tipo AsesoriasEstudiante
     */
    public ResultadoEJB<List<AsesoriasEstudiantes>> buscaAsesoriasEstudiantesPorPersonalEventoRegistro(Integer personal, Integer eventoRegistro){
        try {
            List<AsesoriasEstudiantes> asesoriasEstudiante = em.createQuery("SELECT a FROM AsesoriasEstudiantes a WHERE a.personal = :personal AND a.eventoRegistro = :eventoRegistro ORDER BY a.asesoriaEstudiante DESC", AsesoriasEstudiantes.class)
                    .setParameter("personal", personal)
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(asesoriasEstudiante, "Asesorías encontradas del docente y evento de registro seleccionado");
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de las asesorías, favor de verificar la siguiente información: (EjbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesPorPersonalEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de las asesorías, favor de verificar la siguiente información: (EjbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesPorPersonalEventoRegistro.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> verificarParticipanteAsesoria(AsesoriasEstudiantes asesoria, Integer estudiante){
        try {
            AsesoriasEstudiantes a = em.find(AsesoriasEstudiantes.class, asesoria.getAsesoriaEstudiante());
            em.refresh(a);
            List<Estudiante> est = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.asesoriasEstudiantesList a WHERE a.asesoriaEstudiante = :asesoriaEstudiante AND e.idEstudiante = :estudiante", Estudiante.class)
                    .setParameter("asesoriaEstudiante", a.getAsesoriaEstudiante())
                    .setParameter("estudiante", estudiante)
                    .getResultList();
            if (est.isEmpty()) return ResultadoEJB.crearErroneo(4, Boolean.FALSE, "No se han encontrado resultados");
            if (est.size()>1) return ResultadoEJB.crearErroneo(3, Boolean.FALSE, "Se han encontrado varios resultados de la consulta solicitada");
            if (!est.get(0).getAsesoriasEstudiantesList().isEmpty()) {
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Verificado");
            } else {
                return ResultadoEJB.crearErroneo(2, Boolean.FALSE, "No se ha encontrado el resultado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la consulta (EjbRegistroAsesoriaEstudiante.verificarParticipanteAsesoriaEstudiante.Exception)",e, null);
        }
    }
    
    public ResultadoEJB<Boolean> eliminarParticipanteAsesoria(AsesoriasEstudiantes asesoria, Integer estudiante) {
        try {
            AsesoriasEstudiantes as = em.find(AsesoriasEstudiantes.class, asesoria.getAsesoriaEstudiante());
            Estudiante es = em.find(Estudiante.class, estudiante);
            em.refresh(as);
            em.refresh(es);
            if ((verificarParticipanteAsesoria(as, es.getIdEstudiante())).getCorrecto()) {
                as.getEstudianteList().remove(es);
                es.getAsesoriasEstudiantesList().remove(as);
                em.flush();
            }
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha removido el estudiante de la asesoría");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la eliminación del estudiante (EjbRegistroAsesoriaEstudiante.eliminarParticipanteAsesoria.Exception)", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> asignaParticipanteAsesoria(AsesoriasEstudiantes asesoria, Integer estudiante){
        try {
            AsesoriasEstudiantes a = em.find(AsesoriasEstudiantes.class, asesoria.getAsesoriaEstudiante());
            Estudiante e = em.find(Estudiante.class, estudiante);
            
            em.refresh(a);
            em.refresh(e);
            
            if(verificarParticipanteAsesoria(asesoria, estudiante).getCorrecto()){
                eliminarParticipanteAsesoria(asesoria, estudiante);
            }else{
                a.getEstudianteList().add(e);
                e.getAsesoriasEstudiantesList().add(a);
            }
            em.flush();
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha asignado el estudiante a la asesoría");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar este estudiante a la asesoria seleccionada (EjbRegistroAsesoriaEstudiante.asignaParticipanteAsesoria.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<List<PeriodosEscolares>> consultarPeriodosEscolaresPorGrupo(){
        try {
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT g.periodo FROM Grupo g GROUP BY g.periodo", Integer.class)
                    .getResultStream()
                    .map(idPeriodo -> em.find(PeriodosEscolares.class, idPeriodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if(!periodosEscolares.isEmpty()){
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares que contienen al menos un grupo asignado");
            }else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "Aún no hay grupos asignados a un periodo escolar");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron consultar los periodos escolares que tienen asignados grupos", e, null);
        }
    }
    
    public ResultadoEJB<List<AreasUniversidad>> consultarProgramasEducativosGruposPorPeriodo(Integer periodo){
        try {
            List<AreasUniversidad> programasEducativos = em.createQuery("SELECT g.idPe FROM Grupo g WHERE g.periodo = :periodo GROUP BY g.idPe", Short.class)
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .map(idPE -> em.find(AreasUniversidad.class, idPE))
                    .distinct()
                    .sorted((a1,a2) -> a1.getAreaSuperior().compareTo(a2.getAreaSuperior()))
                    .sorted((a1,a2) -> a1.getNombre().compareTo(a2.getNombre()))
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos que tienen asignados grupos en el periodo escolar activo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron consultar los programas educativos por periodo, favor de verificar la siguiente información: (EjbRegistroAsesoriaEstudiante.consultarProgramasEducativosGruposPorPeriodo)", e, null);
        }
    }
    
    public ResultadoEJB<List<Grupo>> consultarGruposPorPeriodoProgramaEducativo(Integer periodo, Short programaEducativo){
        try {
            List<Grupo> grupo = em.createQuery("SELECT g FROM Grupo g WHERE  (g.periodo = :periodo) AND (g.idPe = :programaEducativo) AND (g.tutor <> NULL) ORDER BY g.grado,g.literal", Grupo.class)
                    .setParameter("periodo", periodo)
                    .setParameter("programaEducativo", programaEducativo)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(grupo, "Lista de grupos por periodo escolar y programa educativo seleccionados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron consultar los grupos del periodo escolar y programa educativo seleccionados, favor de verificar la siguiente información: (EjbRegistroAsesoriaEstudiante.consultarGruposPorPeriodoProgramaEducativo)", e, null);
        }
    }
    
//    TODO: Para obtener una lista de estudiantes general de un grupo, ocupar el método alojado en EjbRegistroAsesoriaTutoria.obtenerListaEstudiantes
}
