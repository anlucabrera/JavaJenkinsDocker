/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbConsultaTutoriasAsesorias")
public class EjbConsultaTutoriasAsesorias {
    @EJB        EjbRegistroAsesoriaTutoria  ejbRegistroAsesoriaTutoria;
    @EJB        Facade                      f;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Consulta el listado de tutorías grupales por pat y evento de registro 
     * @param pat
     * @param eventoRegistro
     * @return 
     */
    public ResultadoEJB<List<TutoriasGrupales>> buscaTutoriasGrupalesPorPlanAccionTutorialEventoRegistro(PlanAccionTutorial pat, EventosRegistros eventoRegistro){
        try {
            List<TutoriasGrupales> listaTutoriasGrupales = new ArrayList<>();
            listaTutoriasGrupales = em.createQuery("SELECT tg FROM TutoriasGrupales tg WHERE tg.sesionGrupal.planAccionTutoria.planAccionTutoria = :pat AND tg.eventoRegistro = :eventoRegistro ORDER BY tg.tutoriaGrupal DESC", TutoriasGrupales.class)
                    .setParameter("pat", pat.getPlanAccionTutoria())
                    .setParameter("eventoRegistro", eventoRegistro.getEventoRegistro())
                    .getResultList();
            if(listaTutoriasGrupales.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null,"No se han registrado previamente tutorias grupales");
            }else{
                return ResultadoEJB.crearCorrecto(listaTutoriasGrupales, "Lista de Tutorias Grupales por Plan de Acción Tutorial y Evento de Registro");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tutorias grupales por plan y evento de registro (EjbConsultaTutorias.buscaTutoriasGrupalesPorPlanAccionTutorialEventoRegistro)", e, null);
        }
    } 
    
    /**
     * Consulta de Asesorías de tipo grupal filtradas por docente y evento de registro
     * @param docente
     * @param eventoRegistro
     * @return 
     */
    public ResultadoEJB<List<Asesoria>> buscaAsesoriasGrupalesPorDocenteEventoRegistro(int docente, Integer eventoRegistro){
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.carga.docente = :docente AND a.tipo = 'Grupal' AND a.eventoRegistro = :eventoRegistro ORDER BY a.idAsesoria DESC", Asesoria.class)
                    .setParameter("docente", docente)
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            if(asesorias.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han registrado previamente asesorías");
            }else{
                return ResultadoEJB.crearCorrecto(asesorias, "Asesorías encontradas del docente y evento de registro seleccionada");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasPorDocenteEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasPorDocenteEventoRegistro.Exception).", e, null);
        }
    }
    
    /**
     * Consulta de Asesorías de tipo individual filtradas por docente y evento de registro
     * @param docente
     * @param eventoRegistro
     * @return 
     */
    public ResultadoEJB<List<Asesoria>> buscaAsesoriasIndividualesPorDocenteEventoRegistro(int docente, Integer eventoRegistro){
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.carga.docente = :docente AND a.tipo = 'Individual' AND a.eventoRegistro = :eventoRegistro ORDER BY a.idAsesoria DESC", Asesoria.class)
                    .setParameter("docente", docente)
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            if(asesorias.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han registrado previamente asesorías");
            }else{
                return ResultadoEJB.crearCorrecto(asesorias, "Asesorías encontradas del docente y evento de registro seleccionada");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasPorDocenteEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasPorDocenteEventoRegistro.Exception).", e, null);
        }
    }
    
    
    public ResultadoEJB<List<Asesoria>> buscaAsesoriasGrupalesPorGrupoEventoRegistro(Grupo grupo, Integer eventoRegistro){
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.carga.cveGrupo.idGrupo = :idGrupo AND a.eventoRegistro = :eventoRegistro AND a.tipo = 'Grupal' ORDER BY a.idAsesoria DESC", Asesoria.class)
                    .setParameter("idGrupo", grupo.getIdGrupo())
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            if(asesorias.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han registrado previamente asesorías");
            }else{
                return ResultadoEJB.crearCorrecto(asesorias, "Asesorías encontradas del grupo y evento de registro seleccionado");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasPorGrupoEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasPorGrupoEventoRegistro.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<List<Asesoria>> buscaAsesoriasInvidualesPorGrupoEventoRegistro(Grupo grupo, Integer eventoRegistro){
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.carga.cveGrupo.idGrupo = :idGrupo AND a.eventoRegistro = :eventoRegistro AND a.tipo = 'Individual' ORDER BY a.idAsesoria DESC", Asesoria.class)
                    .setParameter("idGrupo", grupo.getIdGrupo())
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            if(asesorias.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han registrado previamente asesorías");
            }else{
                return ResultadoEJB.crearCorrecto(asesorias, "Asesorías encontradas del grupo y evento de registro seleccionado");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesorías (EjbConsultaTutorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<List<AsesoriasEstudiantes>> buscaAsesoriasIndividualesEstudiantesPorGrupoEventoRegistro(Grupo grupo, Integer eventoRegistro){
        try {
            
            List<AsesoriasEstudiantes> asesoriasEstudiante = em.createQuery("SELECT DISTINCT a FROM AsesoriasEstudiantes a INNER JOIN a.estudianteList el WHERE el.grupo.idGrupo = :idGrupo AND a.eventoRegistro = :eventoRegistro AND a.tipo = 'Individual' ORDER BY a.asesoriaEstudiante DESC", AsesoriasEstudiantes.class)
                    .setParameter("idGrupo", grupo.getIdGrupo())
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            if(asesoriasEstudiante.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han registrado previamente tutorías geneales en el evento seleccionado");
            }else{
                
                return ResultadoEJB.crearCorrecto(asesoriasEstudiante, "Asesorías generales encontradas del grupo y evento de registro seleccionado");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de las asesorías, favor de verificar la siguiente información: (EjbConsultaTutorias.buscaAsesoriasEstudiantesPorGrupoEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de las asesorías, favor de verificar la siguiente información: (EjbConsultaTutorias.buscaAsesoriasEstudiantesPorGrupoEventoRegistro.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<List<AsesoriasEstudiantes>> buscaAsesoriasGrupalesEstudiantesPorGrupoEventoRegistro(Grupo grupo, Integer eventoRegistro){
        try {
            
            List<AsesoriasEstudiantes> asesoriasEstudiante = em.createQuery("SELECT DISTINCT a FROM AsesoriasEstudiantes a INNER JOIN a.estudianteList el WHERE el.grupo.idGrupo = :idGrupo AND a.eventoRegistro = :eventoRegistro AND a.tipo = 'Grupal' ORDER BY a.asesoriaEstudiante DESC", AsesoriasEstudiantes.class)
                    .setParameter("idGrupo", grupo.getIdGrupo())
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            if(asesoriasEstudiante.isEmpty()){
                return ResultadoEJB.crearErroneo(3, null, "No se han registrado previamente tutorías geneales en el evento seleccionado");
            }else{
                
                return ResultadoEJB.crearCorrecto(asesoriasEstudiante, "Asesorías generales encontradas del grupo y evento de registro seleccionado");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de las asesorías, favor de verificar la siguiente información: (EjbConsultaTutorias.buscaAsesoriasEstudiantesPorGrupoEventoRegistro.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de las asesorías, favor de verificar la siguiente información: (EjbConsultaTutorias.buscaAsesoriasEstudiantesPorGrupoEventoRegistro.Exception).", e, null);
        }
    }
    
}
