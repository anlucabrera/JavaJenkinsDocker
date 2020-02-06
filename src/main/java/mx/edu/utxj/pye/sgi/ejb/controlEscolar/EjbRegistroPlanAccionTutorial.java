/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutorPlantilla;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorialPlantilla;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutoriasPlantilla;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroPlanAccionTutorial")
public class EjbRegistroPlanAccionTutorial {
    @EJB        Facade                      f;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Método que permite la consulta y existencia de la plantilla del plan de acción tutoríal del grado seleccionado.
     * @param grado
     * @return 
     */
    public ResultadoEJB<List<PlanAccionTutorialPlantilla>> buscaPlantillaPlanAccionTutorialGradoAcademico(String grado){
        try {
            List<PlanAccionTutorialPlantilla> listarPlanAccionTutorialPlantilla = new ArrayList<>();
            listarPlanAccionTutorialPlantilla = em.createQuery("SELECT pat FROM PlanAccionTutorialPlantilla pat WHERE pat.grado = :grado", PlanAccionTutorialPlantilla.class)
                    .setParameter("grado", grado)
                    .getResultList();
            if(listarPlanAccionTutorialPlantilla.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null, "Aún no se ha registrado la plantilla del plan de acción tutorial del grado seleccionado");
            }else{
                return ResultadoEJB.crearCorrecto(listarPlanAccionTutorialPlantilla, "Plantilla de Plan de Acción Tutorial Encontrada");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la plantilla del plan de acción tutorial del grado seleccionado (EjbRegistroPlanAccionTutorial.buscaPlantillaPlanAccionTutorialGradoAcademico)", e, null);
        }
    }
    
    public ResultadoEJB<PlanAccionTutorialPlantilla> guardaPlantillaPlanAccionTutorial(PlanAccionTutorialPlantilla planAccionTutorialPlantilla) {
        try {
            if (!(buscaPlantillaPlanAccionTutorialGradoAcademico(planAccionTutorialPlantilla.getGrado()).getCorrecto())) {
                em.persist(planAccionTutorialPlantilla);
                return ResultadoEJB.crearCorrecto(planAccionTutorialPlantilla, "El registro de la plantilla del plan de acción tutorial del cuatrimestre: " + planAccionTutorialPlantilla.getGrado() + "° ha sido guardado correctamente");
            } else {
                em.merge(planAccionTutorialPlantilla);
                return ResultadoEJB.crearCorrecto(planAccionTutorialPlantilla, "El registro de la plantilla del plan de acción tutoria del cuatrimestre: " + planAccionTutorialPlantilla.getGrado() + "° ha sido guardado correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el plan de acción tutorial del grupo seleccionado (EjbRegistroPlanAccionTutorial.guardaPlantillaPlanAccionTutorial)", e, null);
        }
    }
    
    /**
     * Método que permite la consulta de las metas de la función del tutor de la plantilla de acción tutorial seleccionada
     * @param planAccionTutorialPlantilla
     * @return 
     */
    public ResultadoEJB<List<FuncionesTutorPlantilla>> buscaFuncionesTutor(PlanAccionTutorialPlantilla planAccionTutorialPlantilla){
        try {
            List<FuncionesTutorPlantilla> listarFunciones = new ArrayList<>();
            listarFunciones = em.createQuery("SELECT ftp FROM FuncionesTutorPlantilla ftp WHERE ftp.planAccionTutoriaPlantilla.grado = :grado ORDER BY ftp.noSesion", FuncionesTutorPlantilla.class)
                    .setParameter("grado", planAccionTutorialPlantilla.getGrado())
                    .getResultList();
            if(listarFunciones.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null,"No se han registrado previamente funciones del tutor");
            }else{
                return ResultadoEJB.crearCorrecto(listarFunciones, "Funciones del tutor de la plantilla del plan de acción tutorial");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo consultar la función del tutor (EjbRegistroPlanAccionTutorial.buscaFuncionesTutor)", e, null);
        }
    }
    
    /**
     * Método que permite registrar metas como función de tutor en el plan de acción tutorial 
     * @param funcionTutorPlantilla
     * @return 
     */
    public ResultadoEJB<FuncionesTutorPlantilla> guardarFuncionTutorPlantilla(FuncionesTutorPlantilla funcionTutorPlantilla){
        try {
            short verificarRegistros = 0;
            verificarRegistros = em.createQuery("SELECT MAX(ftp.noSesion) FROM FuncionesTutorPlantilla ftp WHERE ftp.planAccionTutoriaPlantilla.grado = :planAccionTutoriaPlantilla", Short.class)
                    .setParameter("planAccionTutoriaPlantilla", funcionTutorPlantilla.getPlanAccionTutoriaPlantilla().getGrado())
                    .getResultStream()
                    .findFirst()
                    .orElse((short)0);
            if(verificarRegistros == 0){
                funcionTutorPlantilla.setNoSesion((short) 1);
                em.persist(funcionTutorPlantilla);
            }else{
                int verificarInt = verificarRegistros;
                int consecutivo = verificarInt + 1;
                funcionTutorPlantilla.setNoSesion((short)consecutivo);
                em.persist(funcionTutorPlantilla);
            }
            return ResultadoEJB.crearCorrecto(funcionTutorPlantilla, "La función del tutor en la plantilla del plan de acción tutorial ha sido guardada correctamente en sistema");
        } catch (NonUniqueResultException nure) {
            return ResultadoEJB.crearErroneo(2, "Se ha encontrado más de un resultado al momento de asignar el consecutivo en la función del tutor", nure, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la función del tutor en la Plantilla del Plan Acción Tutorial (EjbRegistroPlanAccionTutorial.guardarFuncionTutorPlantilla)", e, null);
        }
    }
    
    /**
     * Método que permite la edición especifíca de una meta de la función del tutor
     * @param funcionTutorPlantilla
     * @return 
     */
    public ResultadoEJB<Boolean> editaFuncionTutorPlantilla(FuncionesTutorPlantilla funcionTutorPlantilla) {
        try {
            em.merge(funcionTutorPlantilla);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La función del tutor se ha actualizado correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la función del tutor seleccionada (EjbRegistroPlanAccionTutorial.editaFuncionTutor)", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la eliminación de una meta de la función del tutor específica, editando de igual manera el consecutivo de las metas de la función del tutor previamente registradas
     * @param funcionTutorPlantilla
     * @return 
     */
    public ResultadoEJB<Boolean> eliminarFuncionTutorPlantilla(Integer funcionTutorPlantilla){
        try {
            String planAccionTutorialPlantilla = em.find(FuncionesTutorPlantilla.class, funcionTutorPlantilla).getPlanAccionTutoriaPlantilla().getGrado();
            em.remove(em.find(FuncionesTutorPlantilla.class, funcionTutorPlantilla));
            FuncionesTutorPlantilla ftp = em.find(FuncionesTutorPlantilla.class, funcionTutorPlantilla);
            if(ftp == null){
                List<FuncionesTutorPlantilla> lista = em.createQuery("SELECT ftp FROM FuncionesTutorPlantilla ftp WHERE ftp.planAccionTutoriaPlantilla.grado = :planAccionTutorialPlantilla ORDER BY ftp.noSesion", FuncionesTutorPlantilla.class)
                    .setParameter("planAccionTutorialPlantilla", planAccionTutorialPlantilla)
                    .getResultList();
                if (!lista.isEmpty()) {
                    IntStream.range(0, lista.size()).forEach(i -> {
                        int noSesion = i+1;
                        lista.get(i).setNoSesion((short)noSesion);
                        em.merge(lista.get(i));
                    });
                }
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La función del tutor ha sido eliminada correctamente del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se podido eliminar la función del tutor, el valor sigue asignado en la plantilla del plan de acción tutorial", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la función de tutor de la plantilla del plan de acción tutorial (EjbRegistroPlanAccionTutorial.eliminarFuncionTutorPlantilla)", e, null);
        }
    }
    
    
    public ResultadoEJB<List<SesionesGrupalesTutoriasPlantilla>> buscaSesionesGrupalesTutoriaPlantilla(PlanAccionTutorialPlantilla planAccionTutorialPlantilla) {
        try {
            List<SesionesGrupalesTutoriasPlantilla> sesiones = new ArrayList<>();
            sesiones = em.createQuery("SELECT sgtp FROM SesionesGrupalesTutoriasPlantilla sgtp WHERE sgtp.planAccionTutoriaPlantilla.grado = :grado ORDER BY sgtp.noSesion", SesionesGrupalesTutoriasPlantilla.class)
                    .setParameter("grado", planAccionTutorialPlantilla.getGrado())
                    .getResultList();
            if (sesiones.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se han encontrado sesiones grupales asignadas de la Plantilla de Plan de Accion Tutorial seleccionada");
            } else {
                return ResultadoEJB.crearCorrecto(sesiones, "Lista de sesiones grupales de la plantilla del plan de acción tutorial encontrada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la busqueda de la sesiones grupales de la Plantilla del Plan de Acción Tutorial (EjbRegistroPlanAccionTutorial.buscaSesionesGrupalesTutoriaPlantilla)", e, null);
        }
    }
    
    /**
     * Método que permite guardar sesiones grupales de tutoría en la plantilla de acción tutoríal, asignando el consecutivo al último o primer registro almacenado.
     * @param sesionGrupalPlantilla
     * @return 
     */
    public ResultadoEJB<SesionesGrupalesTutoriasPlantilla> guardaSesionGrupalTutoriaPlantilla(SesionesGrupalesTutoriasPlantilla sesionGrupalPlantilla) {
        try {
            short verificarRegistros = 0;
            verificarRegistros = em.createQuery("SELECT MAX(sgtp.noSesion) FROM SesionesGrupalesTutoriasPlantilla sgtp WHERE sgtp.planAccionTutoriaPlantilla.grado = :planAccionTutoriaPlantilla", Short.class)
                    .setParameter("planAccionTutoriaPlantilla", sesionGrupalPlantilla.getPlanAccionTutoriaPlantilla().getGrado())
                    .getResultStream()
                    .findFirst()
                    .orElse((short)0);
            if(verificarRegistros == 0){
                sesionGrupalPlantilla.setNoSesion((short) 1);
                em.persist(sesionGrupalPlantilla);
            }else{
                int consecutivo = verificarRegistros + 1;
                sesionGrupalPlantilla.setNoSesion((short)consecutivo);
                em.persist(sesionGrupalPlantilla);
            }
            return ResultadoEJB.crearCorrecto(sesionGrupalPlantilla, "La Sesión Grupal de la tutoría ha sido guardada correctamente en sistema");
        } catch (NonUniqueResultException nure) {
            return ResultadoEJB.crearErroneo(2, "Se ha encontrado más de un resultado al momento de asignar el consecutivo en la Sesión Grupal", nure, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la Sesión Grupal de la plantilla del Plan de Acción Tutorial (EjbRegistroPlanAccionTutorial.guardaSesionGrupalTutoriaPlantilla)", e, null);
        }
    }
    
    /**
     * Método que permite la edición de información de la Sesión Grupal de Tutoría seleccionada
     * @param sesionGrupalPlantilla
     * @return 
     */
    public ResultadoEJB<Boolean> editaSesionGrupalPlantilla(SesionesGrupalesTutoriasPlantilla sesionGrupalPlantilla) {
        try {
            em.merge(sesionGrupalPlantilla);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La sesión grupal del tutor se ha actualizado correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la sesión grupal seleccionada de la plantilla del plan de acción tutorial (EjbRegistroPlanAccionTutorial.editaSesionGrupalPlantilla)", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la eliminación de la sesión grupal de tutoría seleccionada, corrigiendo el consecutivo de las sesiones grupales asignadas a la plantilla del plan de acción tutoríal 
     * @param sesionGrupalPlantilla
     * @return 
     */
    public ResultadoEJB<Boolean> eliminarSesionGrupalPlantilla(Integer sesionGrupalPlantilla){
        try {
            String planAccionTutorialPlantilla = em.find(SesionesGrupalesTutoriasPlantilla.class, sesionGrupalPlantilla).getPlanAccionTutoriaPlantilla().getGrado();
            em.remove(em.find(SesionesGrupalesTutoriasPlantilla.class, sesionGrupalPlantilla));
            SesionesGrupalesTutoriasPlantilla sgp = em.find(SesionesGrupalesTutoriasPlantilla.class, sesionGrupalPlantilla);
            if(sgp == null){
                List<SesionesGrupalesTutoriasPlantilla> lista = em.createQuery("SELECT sgtp FROM SesionesGrupalesTutoriasPlantilla sgtp WHERE sgtp.planAccionTutoriaPlantilla.grado = :planAccionTutorialPlantilla ORDER BY sgtp.noSesion", SesionesGrupalesTutoriasPlantilla.class)
                    .setParameter("planAccionTutorialPlantilla", planAccionTutorialPlantilla)
                    .getResultList();
                if (!lista.isEmpty()) {
                    IntStream.range(0, lista.size()).forEach(i -> {
                        int noSesion = i+1;
                        lista.get(i).setNoSesion((short)noSesion);
                        em.merge(lista.get(i));
                    });
                }
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La sesión grupal ha sido eliminada correctamente del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se podido eliminar la sesión grupal, el valor sigue asignado en la plantilla del plan de acción tutorial", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar sesión grupal de la plantilla del plan de acción tutorial (EjbRegistroPlanAccionTutorial.eliminarSesionGrupalPlantilla)", e, null);
        }
    }
    
}
