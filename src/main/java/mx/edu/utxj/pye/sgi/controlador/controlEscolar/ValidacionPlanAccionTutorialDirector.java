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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanAccionTutorial;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionPlanAccionTutorialRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ValidacionPlanAccionTutorialDirector extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = 301868003422183195L;
    @Getter     @Setter                         private             ValidacionPlanAccionTutorialRolDirector             rol;
    @Getter     Boolean                         tieneAcceso = false;
    
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    @EJB        EjbValidacionRol                ejbValidacion;
    
    @Inject     LogonMB                         logon;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "validación plan accion tutorial";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.VALIDACION_PLAN_ACCION_TUTORIAL_DIRECTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbValidacion.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEncargado = ejbValidacion.validarEncargadoDireccion(logon.getPersonal().getClave());
            if(!resAccesoEncargado.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo director = filtro.getEntity();
            rol = new ValidacionPlanAccionTutorialRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            if(!tieneAcceso){
                rol.setFiltro(resAccesoEncargado.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
            if(!tieneAcceso){return;} 
            rol.setDirector(director);
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConPlanAccionTutorial(rol.getDirector());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConPlanAccionTutorial(resPeriodos.getValor());
            cambiarPeriodo();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarListaDtoPlanesAccionTutorial(){
        rol.setListaDtoPlanAccionTutorial(new ArrayList<>());
        rol.setListaDtoPlanAccionTutorial(Collections.EMPTY_LIST);
    }
    
    public void inicializarFuncionesTutor(){
        rol.setListaFuncionesTutor(new ArrayList<>());
        rol.setListaFuncionesTutor(Collections.EMPTY_LIST);
    }
    
    public void inicializarSesiones(){
        rol.setListaSesionesGrupalesTutorias(new ArrayList<>());
        rol.setListaSesionesGrupalesTutorias(Collections.EMPTY_LIST);
    }
    
    /*********************************************** Filtros *********************************************************/
    
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo seleccionado");
            rol.setProgramasEducativos(Collections.EMPTY_LIST);
            inicializarListaDtoPlanesAccionTutorial();
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos = ejb.getProgramasEducativosConPlanAccionTutorial(rol.getDirector(), rol.getPeriodoSeleccionado());
        if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
        else rol.setProgramasEducativos(resProgramasEducativos.getValor());
        cambiarProgramaEducativo();
    }
    
    public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay un programa educativo seleccionado");
            inicializarListaDtoPlanesAccionTutorial();
            return;
        }
        actualizarPlanesAccionTutorial();
    }
    
    /*********************************************** Administración de datos *********************************************************/
    
    public void consultarFunciones(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        ResultadoEJB<List<FuncionesTutor>> res = ejb.buscaFuncionesTutor(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial());
        if(res.getCorrecto()){
            inicializarFuncionesTutor();
            rol.setListaFuncionesTutor(res.getValor());
        }else{
            inicializarFuncionesTutor();
        }
        Ajax.update("frmFunciones");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalFuncionesPlanAccionTutorial').show();");
    }
    
    public void consultarSesiones(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        ResultadoEJB<List<SesionesGrupalesTutorias>> res = ejb.buscaSesionesGrupalesXPlanAT(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().getGrupo());
        if(res.getCorrecto()){
            inicializarSesiones();
            rol.setListaSesionesGrupalesTutorias(res.getValor());
        }else{
            inicializarSesiones();
        }
        Ajax.update("frmSesiones");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalSesionesPlanAccionTutorial').show();");
    }
    
    public void validarPlanAccionTutorial(DtoPlanAccionTutorial dtoPAT) {
        rol.setDtoPlanAccionTutorial(dtoPAT);
        if(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().getValidacionDirector()){
            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setValidacionDirector(Boolean.FALSE);
        }else{
            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setValidacionDirector(Boolean.TRUE);
        }  
        ResultadoEJB<PlanAccionTutorial> res = ejb.guardaPlanAccionTutorial(dtoPAT.getPlanAccionTutorial());
        if (res.getCorrecto()) {
            actualizarPlanesAccionTutorial();
            mostrarMensajeResultadoEJB(res);
        } else {
            actualizarPlanesAccionTutorial();
            mostrarMensajeResultadoEJB(res);
        }
    }

    public void actualizarPlanesAccionTutorial(){
        ResultadoEJB<List<DtoPlanAccionTutorial>> res = ejb.getPlanesAccionTutorialPeriodoArea(rol.getPeriodoSeleccionado(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            inicializarListaDtoPlanesAccionTutorial();
            rol.setListaDtoPlanAccionTutorial(res.getValor());
        }else{
            inicializarListaDtoPlanesAccionTutorial();
        }
    }
    
}
