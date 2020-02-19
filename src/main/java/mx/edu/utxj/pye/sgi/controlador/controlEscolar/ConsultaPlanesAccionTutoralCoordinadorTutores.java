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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CordinadoresTutores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
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
public class ConsultaPlanesAccionTutoralCoordinadorTutores extends ViewScopedRol implements  Desarrollable{
    private static final long serialVersionUID = 8859022633250858708L;
    @Getter     @Setter                         private         ValidacionPlanAccionTutorialRolDirector        rol;
    @Getter     private                         Boolean         tieneAcceso = false;
    @Getter     private                         Boolean         cargado = false;
    
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    @EJB        EjbValidacionRol                ejbValidacion;
    
    @Inject     LogonMB                         logonMB;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta plan acción tutorial coordinador tutores";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_PLANES_ACCION_TUTORIAL_COORDINADOR_TUTORES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbValidacion.validarCoordinadorTutor(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo coordinadorTutores = filtro.getEntity();
            rol = new ValidacionPlanAccionTutorialRolDirector(filtro, coordinadorTutores, coordinadorTutores.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(coordinadorTutores);
            if(!tieneAcceso){
                tieneAcceso = rol.tieneAcceso(coordinadorTutores);
            }
            if(!tieneAcceso){return;} 
            rol.setDirector(coordinadorTutores);
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCoordinadoresTutor(rol.getDirector());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConPlanAccionTutorial(resPeriodos.getValor());
            cambiarPeriodo();
        } catch (Exception e) {mostrarExcepcion(e);}
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
        ResultadoEJB<CordinadoresTutores> resCoordinadorTuror = ejb.getCoordinadoresTutores(rol.getPeriodoSeleccionado(), rol.getDirector().getPersonal().getClave());
        if(resCoordinadorTuror.getCorrecto()){
            ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos = ejb.getProgramasEducativosConPlanAccionTutorialCoordinadorTutores(resCoordinadorTuror.getValor().getAreaAcademica(), rol.getPeriodoSeleccionado());
            if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
            else rol.setProgramasEducativos(resProgramasEducativos.getValor());
            cambiarProgramaEducativo();
        }
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
    
//    public void validarPlanAccionTutorial(DtoPlanAccionTutorial dtoPAT) {
//        rol.setDtoPlanAccionTutorial(dtoPAT);
//        if(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().getEstatus().equals(PlanAccionTutorialEstado.VALIDADO.getLabel())){
//            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.EN_OBSERVACIONES.getLabel());
//            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setComentariosDirector("Pendiente de registrar observaciones");
//        }else{
//            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.VALIDADO.getLabel());
//            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setComentariosDirector("Plan de Accion Tutoríal Validado");
//        }  
//        ResultadoEJB<PlanAccionTutorial> res = ejb.validarPlanDeAccionTutorial(dtoPAT.getPlanAccionTutorial());
//        if (res.getCorrecto()) {
//            actualizarPlanesAccionTutorial();
//            mostrarMensajeResultadoEJB(res);
//        } else {
//            actualizarPlanesAccionTutorial();
//            mostrarMensajeResultadoEJB(res);
//        }
//    }
//    
//    public void actualizarComentariosObservacionesDirector(ValueChangeEvent event){
//        DtoPlanAccionTutorial pat = (DtoPlanAccionTutorial) event.getComponent().getAttributes().get("pat");
//        String comentarioObservaciones = (String) event.getNewValue();
//        if(comentarioObservaciones == null || "".equals(comentarioObservaciones)){
//            pat.getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.ENVIADO_PARA_REVISION.getLabel());
//            pat.getPlanAccionTutorial().setComentariosDirector(null);
//        }else{
//            pat.getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.EN_OBSERVACIONES.getLabel());
//            mostrarMensaje("Se han enviado los comentarios u observaciones al tutor");
//            pat.getPlanAccionTutorial().setComentariosDirector(comentarioObservaciones);
//        }
//        actualizarPAT(pat);
//    }
//    
//    public void actualizarPAT(DtoPlanAccionTutorial dtoPAT) {
//        rol.setDtoPlanAccionTutorial(dtoPAT);
//        ResultadoEJB<PlanAccionTutorial> res = ejb.validarPlanDeAccionTutorial(dtoPAT.getPlanAccionTutorial());
//        if (res.getCorrecto()) {
//            actualizarPlanesAccionTutorial();
//            mostrarMensajeResultadoEJB(res);
//        } else {
//            actualizarPlanesAccionTutorial();
//            mostrarMensajeResultadoEJB(res);
//        }
//    }

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
