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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroPlanAccionTutorialRolDocente;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
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
public class RegistroPlanTutoriaTutor extends ViewScopedRol implements Desarrollable{
    @Getter     @Setter                         private         RegistroPlanAccionTutorialRolDocente       rol;
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    @Inject     LogonMB                         logon;
    @Getter     Boolean                         tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String  valor = "plan de acción tutorial";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init() {
        setVistaControlador(ControlEscolarVistaControlador.PLAN_ACCION_TUTORIAL);
        ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTutor(logon.getPersonal().getClave());
        if(!resAcceso.getCorrecto()) {mostrarMensajeResultadoEJB(resAcceso);return;}
        rol = new RegistroPlanAccionTutorialRolDocente(resAcceso.getValor());
        tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado());
        
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCapturaCargaAcademicaTutor(rol.getDocenteLogueado());
        if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
        if(verificarInvocacionMenu()) return;
        if(!validarIdentificacion()) return;
        if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
        
        rol.setNivelRol(NivelRol.OPERATIVO);
        
        rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
        rol.setPeriodosConCargaGrupo(resPeriodos.getValor());
        cambiarPeriodo();
    }
    
    /*********************************************** Inicializadores *********************************************************/
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarPlanAccionTutorial() {
        rol.setPlanAccionTutorial(new PlanAccionTutorial());
        rol.getPlanAccionTutorial().setPlanAccionTutoria(rol.getGrupoTutorSeleccionado().getGrupo().getIdGrupo());
        rol.getPlanAccionTutorial().setGrupo(rol.getGrupoTutorSeleccionado().getGrupo());
    }
    
    public void inicializarFuncionTutor(){
        rol.setFuncionesTutor(new FuncionesTutor());
        rol.getFuncionesTutor().setPlanAccionTutoria(rol.getPlanAccionTutorial());
    }
    
    public void inicializarFuncionesTutor(){
        rol.setListaFuncionesTutor(new ArrayList<>());
        rol.setListaFuncionesTutor(Collections.EMPTY_LIST);
    }
    
    public void inicializarSesionGrupalTutoria(){
        rol.setSesionesGrupalesTutorias(new SesionesGrupalesTutorias());
        rol.getSesionesGrupalesTutorias().setPlanAccionTutoria(rol.getPlanAccionTutorial());
        rol.getSesionesGrupalesTutorias().setCumplimiento(Boolean.FALSE);
        rol.getSesionesGrupalesTutorias().setJustificacion("En espera de registro de tutorías grupales");
    }
    
    public void inicializarSesionesGrupalesTutoria(){
        rol.setListaSesionesGrupalesTutorias(new ArrayList<>());
        rol.setListaSesionesGrupalesTutorias(Collections.EMPTY_LIST);
    }
    
    public void onTabChange(TabChangeEvent event) {
        if("1.- Registro de plan de acción tutorial".equals(event.getTab().getTitle())){
            rol.setTab1(Boolean.TRUE);
            rol.setTab2(Boolean.FALSE);
            rol.setTab3(Boolean.FALSE);
        }else if("2.- Registro de plan de metas de su función como tutor".equals(event.getTab().getTitle())){
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
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setListadoGruposTutor(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<DtoListadoTutores>> resListadoGruposTutores = ejb.listarGruposTutor(rol.getPeriodoSeleccionado(), rol.getDocenteLogueado());
        if(!resListadoGruposTutores.getCorrecto()) mostrarMensajeResultadoEJB(resListadoGruposTutores);
        else rol.setListadoGruposTutor(resListadoGruposTutores.getValor());
        
        cambiarGrupo();
    }
    
    public void cambiarGrupo(){
        if(rol.getGrupoTutorSeleccionado() == null){
            mostrarMensaje("No hay un grupo tutorado seleccionado");
            return;
        }
        ResultadoEJB<List<PlanAccionTutorial>> planAccionTutorial = ejb.buscaPlanAccionTutorialExistente(rol.getGrupoTutorSeleccionado().getGrupo());
        if(!planAccionTutorial.getCorrecto()){
            mostrarMensajeResultadoEJB(planAccionTutorial);
            inicializarPlanAccionTutorial();
            inicializarFuncionTutor();
            inicializarFuncionesTutor();
            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
        }else{
            mostrarMensajeResultadoEJB(planAccionTutorial);
            rol.setPlanAccionTutorial(planAccionTutorial.getValor().get(0));
            inicializarFuncionTutor();
            inicializarFuncionesTutor();
            inicializarSesionGrupalTutoria();
            inicializarSesionesGrupalesTutoria();
        }
//        TODO: Acciones que se llevan a cabo cuando el tutor selecciona el grupo.
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Administración de datos *********************************************************/
    
    public void guardarNuevoPlanAccionTutorial(){
        ResultadoEJB<PlanAccionTutorial> resPlan = ejb.guardaPlanAccionTutorial(rol.getPlanAccionTutorial());
        if(resPlan.getCorrecto()){
            rol.setPlanAccionTutorial(resPlan.getValor());
            mostrarMensajeResultadoEJB(resPlan);
        }else{
            mostrarMensajeResultadoEJB(resPlan);
            inicializarPlanAccionTutorial();
        }
    }
    
    public void eliminarPlanAccionTutorial(PlanAccionTutorial planAccionTutoria){
        ResultadoEJB<Boolean> res = ejb.eliminarPlanAccionTutorial(planAccionTutoria);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            inicializarPlanAccionTutorial();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void guardarFuncionTutor(){
        ResultadoEJB<FuncionesTutor> resFuncionTutor = ejb.guardarFuncionTutor(rol.getFuncionesTutor());
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
        ResultadoEJB<Boolean> res = ejb.eliminarFuncionTutor(funcionTutor);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListaFuncionesTutor();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void actualizarFuncionTutor(ValueChangeEvent event){
        FuncionesTutor funcionTutor = (FuncionesTutor) event.getComponent().getAttributes().get("funcionTutor");
        funcionTutor.setMetaFuncionTutor(event.getNewValue().toString());
        ResultadoEJB<Boolean> res = ejb.editaFuncionTutor(funcionTutor);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListaFuncionesTutor();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void guardarSesionGrupalTutoria(){
        ResultadoEJB<SesionesGrupalesTutorias> resSesionGrupal = ejb.guardaSesionGrupalTutoria(rol.getSesionesGrupalesTutorias());
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
    
    public void actulizaActividadProgramada(ValueChangeEvent event){
        SesionesGrupalesTutorias sesionGrupal = (SesionesGrupalesTutorias) event.getComponent().getAttributes().get("sesionGrupal");
        String actividadProgramda = event.getNewValue().toString();
        sesionGrupal.setActividadProgramada(actividadProgramda);
        actualizarSesionGrupalTutoria(sesionGrupal);
    }
    
    public void actulizaObjetivos(ValueChangeEvent event){
        SesionesGrupalesTutorias sesionGrupal = (SesionesGrupalesTutorias) event.getComponent().getAttributes().get("sesionGrupal");
        String objetivos = event.getNewValue().toString();
        sesionGrupal.setObjetivos(objetivos);
        actualizarSesionGrupalTutoria(sesionGrupal);
    }

    public void actulizaJustificacion(ValueChangeEvent event){
        SesionesGrupalesTutorias sesionGrupal = (SesionesGrupalesTutorias) event.getComponent().getAttributes().get("sesionGrupal");
        String justificacion = event.getNewValue().toString();
        sesionGrupal.setJustificacion(justificacion);
        actualizarSesionGrupalTutoria(sesionGrupal);
    }
    
    public void actualizarSesionGrupalTutoria(SesionesGrupalesTutorias sesionGrupal){
        ResultadoEJB<Boolean> res = ejb.editaSesionGrupal(sesionGrupal);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListaSesionesGrupalesTutorias();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void eliminarSesionGrupalTutor(Integer sesionGrupal){
        ResultadoEJB<Boolean> res = ejb.eliminarSesionGrupal(sesionGrupal);
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
        rol.setListaFuncionesTutor(ejb.buscaFuncionesTutor(rol.getPlanAccionTutorial()).getValor());
    }
    
    public void actualizarListaSesionesGrupalesTutorias(){
        rol.setListaSesionesGrupalesTutorias(ejb.buscaSesionesGrupalesXPlanAT(rol.getPlanAccionTutorial().getGrupo()).getValor());
    }
    
    /*************************************************************************************************************************/
    
}
