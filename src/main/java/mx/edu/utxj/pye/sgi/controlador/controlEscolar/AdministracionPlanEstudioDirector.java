/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaRegistro;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaUnidades;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudioMateriaCompetencias;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroPlanesEstudioRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class AdministracionPlanEstudioDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroPlanesEstudioRolDirector rol;
    
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            
    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_PLAN_ESTUDIOS);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidaEnc = ejb.validarEncargadoDirector(logon.getPersonal().getClave());//validar si es director

            if (!resValidaEnc.getCorrecto() && !resValidacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resValidacion);
                return;
            }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroPlanesEstudioRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);

            if (!tieneAcceso) {
                rol.setFiltro(resValidaEnc.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }

            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            ResultadoEJB<List<AreaConocimiento>> resAreasConocimiento = ejb.getAreasConocimiento();
            
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            
            if(!resAreasConocimiento.getCorrecto()) mostrarMensajeResultadoEJB(resAreasConocimiento);
            rol.setConocimientos(resAreasConocimiento.getValor());
                        
            rol.setPlanEstudio1(new DtoPlanEstudio(new PlanEstudio(), rol.getPrograma()));
            rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
            rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateria()));
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(),rol.getPlanEstudio(), rol.getMateria()));
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(),new Competencia(),rol.getPlanEstudioMateria(),rol.getPlanEstudio()));
            rol.setUnidadesMateria(new ArrayList<>());
            rol.setNewCompetencia(false);
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Admin Plan Estudio";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
// eventos de registro

    public void guardarPlanEstudio() {
        rol.getPlanEstudio1().getPlanEstudio().setIdPe(rol.getPlanEstudio1().getPrograma().getArea());
        ejb.registrarPlanEstudio(rol.getPrograma(), rol.getPlanEstudio1().getPlanEstudio(), Operacion.PERSISTIR);
    }

    public void guardarMateria() {
        ejb.registrarMateria(rol.getMateriaRegistro1(), Operacion.PERSISTIR);
        guardarPlanEstudioMateria();
        rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
    }

    public void guardarUnidad() {
        rol.getMateriaUnidades1().getUnidadMateria().setIntegradora(false);
        ejb.registrarUnidadMateria(rol.getMateriaUnidades1(), Operacion.PERSISTIR);
        unidadesPorMateriaConsulta();
        rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateriaUnidades1().getMateria()));
    }

    public void guardarPlanEstudioMateria() {
        rol.getMateriaPlanEstudio1().setMateria(rol.getMateriaRegistro1().getMateria());
        ejb.registrarPlanEstudioMateria(rol.getMateriaPlanEstudio1().getMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), rol.getMateriaPlanEstudio1().getPlanEstudioMateria(), Operacion.PERSISTIR);
        materiasPorPlanEstudioConsulta();
        rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
    }

    public void guardarPlanEstudioMateriaCompetencia() {
        if (rol.getPlanEstudioMateriaCompetencias1().getCompetencia().getIdCompetencia().equals(0)) {
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.PERSISTIR);
        } else {
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.ACTUALIZAR);
        }
        competenciasPorPlanEstudioMateriaConsulta();
        rol.setNewCompetencia(false);
        rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(), new Competencia(), new PlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
    }
  
// eventos de Actualizacion
    
     public void onRowEditPlanEstudio(RowEditEvent event) {
        try {
            ejb.registrarPlanEstudio(rol.getPrograma(),(PlanEstudio) event.getObject(), Operacion.ACTUALIZAR);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    public void onRowEditMateria(RowEditEvent event) {
        try {
            PlanEstudioMateria m=(PlanEstudioMateria) event.getObject();
            rol.setMateriaRegistro1(new DtoMateriaRegistro(m.getIdMateria(), m.getIdMateria().getIdAreaConocimiento()));
            ejb.registrarMateria(rol.getMateriaRegistro1(), Operacion.ACTUALIZAR);
//            materiasRegistradasConsulta();
            rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditUnidad(RowEditEvent event) {
       try {
            rol.setMateriaUnidades1(new DtoMateriaUnidades((UnidadMateria) event.getObject(), rol.getMateriaUnidades1().getMateria()));
            ejb.registrarUnidadMateria(rol.getMateriaUnidades1(), Operacion.ACTUALIZAR);
            unidadesPorMateriaConsulta();
            rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateriaUnidades1().getMateria()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPlanEstudioMateria(RowEditEvent event) {
        try {            
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio((PlanEstudioMateria) event.getObject(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
            ejb.registrarPlanEstudioMateria(rol.getMateriaPlanEstudio1().getMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), rol.getMateriaPlanEstudio1().getPlanEstudioMateria(), Operacion.ACTUALIZAR);
            onRowEditMateria(event);
            materiasPorPlanEstudioConsulta();
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPlanEstudioMateriaCompetencia(RowEditEvent event) {
        try {
            DtoPlanEstudioMateriaCompetencias dpemc=(DtoPlanEstudioMateriaCompetencias) event.getObject();
            
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(dpemc.getCompetencia(),new Competencia(), dpemc.getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.ACTUALIZAR);
            competenciasPorPlanEstudioMateriaConsulta();
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(),new Competencia(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

// eventos de Eliminacion    
    public void eliminarPlanEstudio(PlanEstudio pe) {
        try {
            ejb.registrarPlanEstudio(rol.getPrograma(), pe, Operacion.ELIMINAR);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarMateria(Materia m) {
        try {
            rol.setMateriaRegistro1(new DtoMateriaRegistro(m, rol.getConocimiento()));
            ejb.registrarMateria(rol.getMateriaRegistro1(), Operacion.ELIMINAR);
//            materiasRegistradasConsulta();
            rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarUnidadMateria(UnidadMateria um) {
        try {
            rol.setMateriaUnidades1(new DtoMateriaUnidades(um, rol.getMateriaUnidades1().getMateria()));
            ejb.registrarUnidadMateria(rol.getMateriaUnidades1(), Operacion.ELIMINAR);
            unidadesPorMateriaConsulta();
            rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateriaUnidades1().getMateria()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarPlanEstudioMateria(PlanEstudioMateria pem) {
        try {
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(pem, rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
            ejb.registrarPlanEstudioMateria(rol.getMateriaPlanEstudio1().getMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), rol.getMateriaPlanEstudio1().getPlanEstudioMateria(), Operacion.ELIMINAR);
            materiasPorPlanEstudioConsulta();
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarPlanEstudioMateriaCompetencia(Competencia c) {
        try {
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(c,new Competencia(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.ELIMINAR);
            competenciasPorPlanEstudioMateriaConsulta();
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(),new Competencia(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
// Eventos Ajax en interfaz
//    public void materiasRegistradasConsulta() {
//        ResultadoEJB<List<Materia>> resMaterias = ejb.getListadoMaterias();
//        if (!resMaterias.getCorrecto()) {
//            mostrarMensajeResultadoEJB(resMaterias);
//        }
//        rol.setMaterias(resMaterias.getValor());
//    }

    public void unidadesPorMateriaConsulta() {
        ResultadoEJB<Map<Materia, List<UnidadMateria>>> resUnidadesMateria = ejb.getListaUnidadesMateria();
        if (!resUnidadesMateria.getCorrecto()) {
            mostrarMensajeResultadoEJB(resUnidadesMateria);
        }
        rol.setMateriasUnidadesMap(resUnidadesMateria.getValor());
    }

    public void materiasPorPlanEstudioConsulta() {
        rol.setPlanEstudioMaterias(new ArrayList<>());
        rol.getMateriaUnidades1().setMateria(new Materia());
        List<PlanEstudioMateria> pems = new ArrayList<>();
        PlanEstudio plan = new PlanEstudio();

        if (rol.getMateriaPlanEstudio1().getPlanEstudio() != null) {
            plan = rol.getMateriaPlanEstudio1().getPlanEstudio();
        } else {
            plan = rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio();
        }
        pems = ejb.generarPlanEstuidoMaterias(plan);
        if (!pems.isEmpty()) {
            rol.setPlanEstudioMaterias(pems);

            ResultadoEJB<List<Materia>> resMaterias = ejb.getListadoMaterias(plan);
            if (!resMaterias.getCorrecto()) {
                mostrarMensajeResultadoEJB(resMaterias);
            }
            rol.setMaterias(resMaterias.getValor());
        }
    }

    public void competenciasPorPlanEstudioMateriaConsulta() {
        rol.setCompetencias(new ArrayList<>());
        ResultadoEJB<List<Competencia>> resCompetencia = ejb.getCompetenciasPlan(rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio());
        if (!resCompetencia.getCorrecto()) {
            mostrarMensajeResultadoEJB(resCompetencia);
        }
        rol.setCompetencias(resCompetencia.getValor());        
        materiasPorPlanEstudioConsulta();
        listaCompetenciasPorMateria();
    }
    
    public void listaCompetenciasPorMateria() {
        rol.setPlanEstudioMateriaCompetenciasesList(new ArrayList<>());
        ResultadoEJB<List<DtoPlanEstudioMateriaCompetencias>> resCompetenciaPEM = ejb.obtenerDtoPEMC(rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio());
        if (!resCompetenciaPEM.getCorrecto()) {
            mostrarMensajeResultadoEJB(resCompetenciaPEM);
        }
        rol.setPlanEstudioMateriaCompetenciasesList(resCompetenciaPEM.getValor());
        materiasPorPlanEstudioConsulta();
    }
    
    
    
    
// validaciones realizadas  desde la interfaz
    public void comprobarMateria() {
        final List<Materia> materias = new ArrayList<>();
        rol.getPlanEstudioMaterias().forEach((t) -> {
            materias.add(t.getIdMateria());
        });

        if (materias.contains(rol.getMateriaPlanEstudio1().getMateria())) {
            mostrarMensaje("La materia ya se encuentra asignada al plan de estudio");
        }
    }

    public void comprobarRegistroDeCompetencias() {
        if (rol.getPlanEstudioMateriaCompetencias1().getCompetencia().getIdCompetencia().equals(0)) {
            rol.setNewCompetencia(true);
        }
    }

// eventos de tablas
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
    
    

    
    public void metodoBase() {}
}
