/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionCatEvidInstEvalRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionCatEvidInstEval;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AdministracionCatEvidInstEvalEscolares extends ViewScopedRol implements Desarrollable {
    
    @Getter @Setter AdministracionCatEvidInstEvalRolEscolares rol;

    @EJB EjbAdministracionCatEvidInstEval ejb;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB EjbAsignacionIndicadoresCriterios ejbAsignacionIndicadoresCriterios;
    
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    
     /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init(){
        try{
        if(!logon.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_CATEVIDINSTEVAL);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionCatEvidInstEvalRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR EVIDENCIA Y/O INSTRUMENTO DE EVALUACIÓN.");
            rol.getInstrucciones().add("Seleccionar la opción que corresponda AGREGAR EVIDENCIA o AGREGAR INSTRUMENTO.");
            rol.getInstrucciones().add("Seleccionar de la lista la opción requerida o bien ingresar el nombre en el campo correspondiente.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ACTIVAR O DESACTIVAR EVIDENCIA O INSTRUMENTO DE EVALUACIÓN");
            rol.getInstrucciones().add("Dar clic en el primer icono (X o ✓) en la columna OPCIONES de la fila que corresponda.");
            rol.getInstrucciones().add("ELIMINAR EVIDENCIA O INSTRUMENTO DE EVALUACIÓN.");
            rol.getInstrucciones().add("Dar clic en el segundo icono (cesto de basura) en la columna OPCIONES de la fila que corresponda.");

            rol.setPestaniaActiva(0);
            rol.setAgregarEvidenciaCategoria(false);
            rol.setAgregarInstrumentoEvaluacion(false);
            listaNivelesEducativos();
            listaInstrumentosEvaluacion();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración categorias evidencias instrumentos evaluación";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaNivelesEducativos(){
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesEducativos();
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaCategoriasEvidencias();
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaCategoriasEvidencias(){
        ResultadoEJB<List<EvidenciaEvaluacion>> res = ejb.getCategoriasEvidenciaNivel(rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setListaCategoriasEvidencias(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaCategorias(){
        ResultadoEJB<List<Criterio>> res = ejb.getCategoriasNivel(rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setCategorias(res.getValor());
            rol.setCategoria(rol.getCategorias().get(0));
            listaEvidencias();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaEvidencias(){
        ResultadoEJB<List<String>> res = ejb.getEvidenciasDisponiblesAgregar(rol.getNivelEducativo(), rol.getListaCategoriasEvidencias(), rol.getCategoria());
        if(res.getCorrecto()){
            rol.setEvidencias(res.getValor());
            rol.setEvidenciaSeleccionada(rol.getEvidencias().get(0));
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaInstrumentosEvaluacion(){
        ResultadoEJB<List<InstrumentoEvaluacion>> res = ejb.getInstrumentosEvaluacion();
        if(res.getCorrecto()){
            rol.setInstrumentosEvaluacion(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    /**
     * Permite que al cambiar de pestaña se muestren las opciones correspondientes
     * @param event Evento del cambio de valor
     */
    public void cambiarPestania(TabChangeEvent event){
        TabView tv = (TabView) event.getComponent();
        rol.setPestaniaActiva(tv.getActiveIndex());
        rol.setAgregarEvidenciaCategoria(false);
        rol.setAgregarInstrumentoEvaluacion(false);
    }
        
    /**
     * Permite que al cambiar el nivel educativo se actualice la lista de evidencias de evaluación por categoría
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        rol.setNivelEducativo((ProgramasEducativosNiveles)e.getNewValue());
        listaCategoriasEvidencias();
        Ajax.update("frm");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar evidencia de evaluación se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarEvidencia(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarEvidenciaCategoria(valor);
            if(rol.getAgregarEvidenciaCategoria()){
                listaCategorias();
                rol.setNuevaEvidencia("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar instrumento de evaluación se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarInstrumento(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarInstrumentoEvaluacion(valor);
            if(rol.getAgregarInstrumentoEvaluacion()){
                listaInstrumentosEvaluacion();
                rol.setNuevoInstrumento("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que al cambiar la categoría de evaluación se actualice la lista de evidencias disponibles para agregar o bien agregar una nueva
     * @param e Evento del cambio de valor
     */
    public void cambiarCategoria(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Criterio criterio = (Criterio)e.getNewValue();
            rol.setCategoria(criterio);
            listaEvidencias();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que cambiar el valor de la evidencia seleccionada
     * @param e Evento del cambio de valor
     */
    public void cambiarEvidencia(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            String evidencia = (String)e.getNewValue();
            rol.setEvidenciaSeleccionada(evidencia);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite activar o desactivar evidencia de la categoría de evaluación
     * @param evidenciaEvaluacion
     */
    public void activarDesactivarEvidenciaCategoria(EvidenciaEvaluacion evidenciaEvaluacion){
        ResultadoEJB<EvidenciaEvaluacion> res = ejb.activarDesactivarEvidenciaCategoria(evidenciaEvaluacion);
        if(res.getCorrecto()){
            evidenciaEvaluacion.setActivo(res.getValor().getActivo());
            listaCategoriasEvidencias();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite guardar la evidencia a la categoría de evaluación seleccionada
     */
    public void guardarEvidenciaCategoria(){
        if (rol.getNuevaEvidencia().equals("Ingresar nombre") || "".equals(rol.getNuevaEvidencia())) {
            ResultadoEJB<EvidenciaEvaluacion> agregar = ejb.guardarEvidenciaCategoria(rol.getCategoria(), rol.getEvidenciaSeleccionada());
            if (agregar.getCorrecto()) {
                mostrarMensajeResultadoEJB(agregar);
                listaCategoriasEvidencias();
                Ajax.update("frm");
            }
        } else {
            ResultadoEJB<EvidenciaEvaluacion> agregar = ejb.guardarEvidenciaCategoria(rol.getCategoria(), rol.getNuevaEvidencia());
            if (agregar.getCorrecto()) {
                mostrarMensajeResultadoEJB(agregar);
                listaCategoriasEvidencias();
                Ajax.update("frm");
            }
        }
    }
    
     /**
     * Permite eliminar la evidencia de la categoría de evaluación seleccionada
     * @param evidenciaEvaluacion
     */
    public void eliminarEvidenciaCategoria(EvidenciaEvaluacion evidenciaEvaluacion){
        ResultadoEJB<Integer> eliminar = ejb.eliminarEvidenciaCategoria(evidenciaEvaluacion);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaCategoriasEvidencias();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite activar o desactivar un instrumento de evaluación
     * @param instrumentoEvaluacion
     */
    public void activarDesactivarInstrumento(InstrumentoEvaluacion instrumentoEvaluacion){
        ResultadoEJB<InstrumentoEvaluacion> res = ejb.activarDesactivarInstrumentoEvaluacion(instrumentoEvaluacion);
        if(res.getCorrecto()){
            instrumentoEvaluacion.setActivo(res.getValor().getActivo());
            listaInstrumentosEvaluacion();
            mostrarMensajeResultadoEJB(res);
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite guardar un nuevo instrumento de evaluación
     */
    public void guardarInstrumentoEvaluacion(){
        if (rol.getNuevoInstrumento().equals("Ingresar nombre") || "".equals(rol.getNuevoInstrumento())) {
              Messages.addGlobalWarn("Debe ingresar el nombre del instrumento de evaluación.");
        } else{
            Integer coincidencia = (int) rol.getInstrumentosEvaluacion().stream().filter(p -> p.getDescripcion().equals(rol.getNuevoInstrumento())).count();
            if(coincidencia == 0){
                ResultadoEJB<InstrumentoEvaluacion> agregar = ejb.guardarInstrumentoEvaluacion(rol.getNuevoInstrumento());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaInstrumentosEvaluacion();
                    rol.setPestaniaActiva(1);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El instrumento de evaluación que desea agregar ya está registrado.");
            }
        }
    }
    
    /**
     * Permite eliminar el instrumento de evaluación seleccionado
     * @param instrumentoEvaluacion
     */
    public void eliminarInstrumento(InstrumentoEvaluacion instrumentoEvaluacion){
        ResultadoEJB<Integer> eliminar = ejb.eliminarInstrumentoEvaluacion(instrumentoEvaluacion);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaInstrumentosEvaluacion();
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existe registro de la evidencia de evaluación seleccionada
     * @param evidenciaEvaluacion
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionEvidencia(@NonNull EvidenciaEvaluacion evidenciaEvaluacion){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarPlaneacionEvidenciaEvaluacion(evidenciaEvaluacion);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método para verificar si existe registro del instrumento de evaluación seleccionado
     * @param instrumentoEvaluacion
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionInstrumento(@NonNull InstrumentoEvaluacion instrumentoEvaluacion){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarPlaneacionInstrumentoEvaluacion(instrumentoEvaluacion);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}
