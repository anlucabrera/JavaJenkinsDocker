/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PlantillaPlanAccionTutorialRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanAccionTutorial;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutorPlantilla;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorialPlantilla;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutoriasPlantilla;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class PlantillaPlanAccionTutorialPsicopedagogia extends ViewScopedRol implements Desarrollable{
    @Getter     @Setter                             PlantillaPlanAccionTutorialRolPsicopedagogia                    rol;
    @Getter     Boolean                             tieneAcceso = false;
    @Getter     private                             Boolean                                                         cargado = false;
    
    @EJB        EjbRegistroPlanAccionTutorial       ejb;
    @EJB        EjbPropiedades                      ep;
    @EJB        EjbValidacionRol                    ejbValidacionRol;
    
    @Inject     LogonMB                             logonMB;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Registro Plantillas de Plan de Acción Tutorial Psicopedagogia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_PlANTILLA_PLAN_ACCION_TUTORIAL_PSICOPEDAGOGIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbValidacionRol.validarPsicopedagogia(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo psicopedagogia = filtro.getEntity();
            
            rol = new PlantillaPlanAccionTutorialRolPsicopedagogia(filtro);
            tieneAcceso = rol.tieneAcceso(psicopedagogia);
            
            if(!tieneAcceso){return;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            inicializarPlantillaPlanAccionTutoria();
            inicializarFuncionTutor();
            inicializarFuncionesTutor();
            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
            cargarPlanAccionTutorial();
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarPlantillaPlanAccionTutoria(){
        rol.setPlanAccionTutorialPlantilla(new PlanAccionTutorialPlantilla());
        rol.getPlanAccionTutorialPlantilla().setGrado(rol.getGradoCuatrimestre());
    }
    
    public void inicializarFuncionTutor(){
        rol.setFuncionTutorPlantilla(new FuncionesTutorPlantilla());
        rol.getFuncionTutorPlantilla().setPlanAccionTutoriaPlantilla(rol.getPlanAccionTutorialPlantilla());
    }
    
    public void inicializarSesionGrupalTutoria(){
        rol.setSesionGrupalTutoriaPlantilla(new SesionesGrupalesTutoriasPlantilla());
        rol.getSesionGrupalTutoriaPlantilla().setPlanAccionTutoriaPlantilla(rol.getPlanAccionTutorialPlantilla());
    }
    
    public void inicializarFuncionesTutor(){
        rol.setListaFuncionesTutorPlantilla(new ArrayList<>());
        rol.setListaFuncionesTutorPlantilla(Collections.EMPTY_LIST);
    }
    
    public void inicializarSesionesGrupalesTutoria(){
        rol.setListaSesionesGrupalesTutoriaPlantilla(new ArrayList<>());
        rol.setListaSesionesGrupalesTutoriaPlantilla(Collections.EMPTY_LIST);
    }
    
    public void onTabChange(TabChangeEvent event) {
        if("1.- Registro de plan de acción tutorial".equals(event.getTab().getTitle())){
            rol.setTab1(Boolean.TRUE);
            rol.setTab2(Boolean.FALSE);
            rol.setTab3(Boolean.FALSE);
        }else if("2.- Registro de plan de metas de la función como tutor".equals(event.getTab().getTitle())){
            rol.setTab1(Boolean.FALSE);
            rol.setTab2(Boolean.TRUE);
            rol.setTab3(Boolean.FALSE);
            actualizarListaFuncionesTutor();
        }else if("3.- Registro de Organización de las sesiones grupales de tutoría".equals(event.getTab().getTitle())){
            rol.setTab1(Boolean.FALSE);
            rol.setTab2(Boolean.FALSE);
            rol.setTab3(Boolean.TRUE);
            actualizarListaSesionesGrupalesTutorias();
        }
    }
    
    public void onTabClose(TabCloseEvent event) {}
    /*************************************************************************************************************************/
    
    /*********************************************** Filtros *********************************************************/
    public void cargarPlanAccionTutorial() {
        if (rol.getGradoCuatrimestre() == null) {
            mostrarMensaje("No hay grado cuatrimestral seleccionado");
            return;
        }

        ResultadoEJB<List<PlanAccionTutorialPlantilla>> resPlanAccionTutorial = ejb.buscaPlantillaPlanAccionTutorialGradoAcademico(rol.getGradoCuatrimestre());
        if (!resPlanAccionTutorial.getCorrecto()) {
            mostrarMensajeResultadoEJB(resPlanAccionTutorial);
            inicializarPlantillaPlanAccionTutoria();
            inicializarFuncionTutor();
            inicializarFuncionesTutor();
            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
        } else {
            mostrarMensajeResultadoEJB(resPlanAccionTutorial);
            rol.setPlanAccionTutorialPlantilla(resPlanAccionTutorial.getValor().get(0));

            inicializarFuncionTutor();
            inicializarFuncionesTutor();

            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
        }
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Administración de datos *********************************************************/
    
    public void guardaNuevaPlantillaPlanAccionTutorial(){
        ResultadoEJB<PlanAccionTutorialPlantilla> resPlan = ejb.guardaPlantillaPlanAccionTutorial(rol.getPlanAccionTutorialPlantilla());
        if(resPlan.getCorrecto()){
            mostrarMensajeResultadoEJB(resPlan);
            rol.setPlanAccionTutorialPlantilla(resPlan.getValor());
        }else{
            mostrarMensajeResultadoEJB(resPlan);
            inicializarPlantillaPlanAccionTutoria();
        }
    }
    
    public void guardarFuncionTutor(){
        ResultadoEJB<FuncionesTutorPlantilla> resFuncionTutor = ejb.guardarFuncionTutorPlantilla(rol.getFuncionTutorPlantilla());
        if(resFuncionTutor.getCorrecto()){
            mostrarMensajeResultadoEJB(resFuncionTutor);
            inicializarFuncionTutor();
            inicializarFuncionesTutor();
            actualizarListaFuncionesTutor();
        }else{
            mostrarMensajeResultadoEJB(resFuncionTutor);
            inicializarFuncionTutor();
            inicializarFuncionesTutor();
        }
    }
    
    public void eliminarFuncionTutor(Integer funcionTutor){
        ResultadoEJB<Boolean> res = ejb.eliminarFuncionTutorPlantilla(funcionTutor);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListaFuncionesTutor();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void actualizarFuncionTutor(ValueChangeEvent event){
        FuncionesTutorPlantilla funcionTutorPlantilla = (FuncionesTutorPlantilla) event.getComponent().getAttributes().get("funcionTutor");
        funcionTutorPlantilla.setMetaFuncionTutor(event.getNewValue().toString());
        ResultadoEJB<Boolean> res = ejb.editaFuncionTutorPlantilla(funcionTutorPlantilla);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void guardarSesionGrupalTutoria(){
        ResultadoEJB<SesionesGrupalesTutoriasPlantilla> resSesionGrupal = ejb.guardaSesionGrupalTutoriaPlantilla(rol.getSesionGrupalTutoriaPlantilla());
        if(resSesionGrupal.getCorrecto()){
            mostrarMensajeResultadoEJB(resSesionGrupal);
            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
            actualizarListaSesionesGrupalesTutorias();
        }else{
            mostrarMensajeResultadoEJB(resSesionGrupal);
            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
        }
    }
    
    public void actualizaActividadProgramada(ValueChangeEvent event){
        SesionesGrupalesTutoriasPlantilla sesionGrupal = (SesionesGrupalesTutoriasPlantilla) event.getComponent().getAttributes().get("sesionGrupal");
        String actividadProgramda = event.getNewValue().toString();
        sesionGrupal.setActividadProgramada(actividadProgramda);
        actualizarSesionGrupalTutoria(sesionGrupal);
    }
    
    public void actualizaObjetivos(ValueChangeEvent event){
        SesionesGrupalesTutoriasPlantilla sesionGrupal = (SesionesGrupalesTutoriasPlantilla) event.getComponent().getAttributes().get("sesionGrupal");
        String objetivos = event.getNewValue().toString();
        sesionGrupal.setObjetivos(objetivos);
        actualizarSesionGrupalTutoria(sesionGrupal);
    }
    
    public void actualizarSesionGrupalTutoria(SesionesGrupalesTutoriasPlantilla sesionGrupal){
        ResultadoEJB<Boolean> res = ejb.editaSesionGrupalPlantilla(sesionGrupal);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void eliminarSesionGrupalTutor(Integer sesionGrupal){
        ResultadoEJB<Boolean> res = ejb.eliminarSesionGrupalPlantilla(sesionGrupal);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListaSesionesGrupalesTutorias();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Llenado de listas *********************************************************/
    
    public void actualizarListaFuncionesTutor(){
        rol.setListaFuncionesTutorPlantilla(ejb.buscaFuncionesTutor(rol.getPlanAccionTutorialPlantilla()).getValor());
        actualizar();
    }
    
    public void actualizarListaSesionesGrupalesTutorias(){
        rol.setListaSesionesGrupalesTutoriaPlantilla(ejb.buscaSesionesGrupalesTutoriaPlantilla(rol.getPlanAccionTutorialPlantilla()).getValor());
        actualizar();
    }
    
    /*************************************************************************************************************************/
    
}
