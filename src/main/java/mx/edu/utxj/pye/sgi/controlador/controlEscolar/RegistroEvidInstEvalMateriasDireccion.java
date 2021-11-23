/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroEvidInstEvaluacionMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEvidInstEvalMateriasRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEvidInstEvalMaterias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioRegEvidInsEval;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.util.Ajax;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroEvidInstEvalMateriasDireccion extends ViewScopedRol implements Desarrollable {
    @Getter @Setter RegistroEvidInstEvalMateriasRolDirector rol;

    @EJB EjbRegistroEvidInstEvalMaterias ejb;
    @EJB EjbAsignacionIndicadoresCriterios ejbAsignacionIndicadoresCriterios;
    @EJB ServicioRegEvidInsEval ejbServicioRegEvidInsEval;
    
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
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EVIDINST_EVALUACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarRolesRegistroEvidInstEvaluacion(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEvidInstEvalMateriasRolDirector(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDirector(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("CARGA MASIVA DE EVIDENCIAS E INSTRUMENTOS DE EVALUACIÓN DESDE PLANTILLA.");
            rol.getInstrucciones().add("Descargue la plantilla, dando clic en el icono de descarga ubicado en la parte superior derecha.");
            rol.getInstrucciones().add("Abrir la plantilla con excel, deberá de llenar la información correspondiente.");
            rol.getInstrucciones().add("Para la información, deberá de dar clic en el botón CARGAR ARCHIVO, seleccionar el documento y dar clic en subir.");
            rol.getInstrucciones().add("A continuación visualizará la información que contiene el documento de excel, para guardar deberá dar clic en el botón GUARDAR, en caso contrario en CANCELAR");
            rol.getInstrucciones().add("ASIGNAR EVIDENCIA E INSTRUMENTO DE EVALUACIÓN DESDE INTERFAZ.");
            rol.getInstrucciones().add("Seleccionar programa educativo y plan de estudio del que desea registrar información.");
            rol.getInstrucciones().add("Dar clic en Agregar más evidencias.");
            rol.getInstrucciones().add("Seleccionar si asignará evidencia e intrumento de forma INDIVIDUAL(por unidad) o MASIVA (para todas las unidades).");
            rol.getInstrucciones().add("Seleccionar grado y mateia.");
            rol.getInstrucciones().add("Si seleccionó INDIVIDUAL, deberá seleccionar Unidad a la que aplicará la asignación que configurará.");
            rol.getInstrucciones().add("Seleccionar categoría: Ser, Saber o Saber - Hacer.");
            rol.getInstrucciones().add("Seleccionar evidencia de la lista de opciones, las opciones dependen de la categoría seleccionada previamente.");
            rol.getInstrucciones().add("Seleccionar instrumento de la lista de opciones.");
            rol.getInstrucciones().add("Ingresar valor de la meta del instrumento.");
            rol.getInstrucciones().add("Una vez que haya realizado los pasos anteriores, de clic en AGREGAR para guardar la información.");

            rol.setTipoBusqueda("busquedaGeneral");
            rol.setAgregarEvidencia(Boolean.FALSE);
            rol.setTipoAgregarEvid("masivaEvid");
            
            listaProgramasEducativos();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro evidencias e instrumentos evaluacion materia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaProgramasEducativos(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getProgramasEducativos(rol.getDirector());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            listaPlanesEstudio();
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaPlanesEstudio(){
        ResultadoEJB<List<PlanEstudio>> res = ejb.getPlanesEstudio(rol.getProgramaEducativo());
        if(res.getCorrecto()){
            rol.setPlanesEstudio(res.getValor());
            if (!rol.getPlanesEstudio().isEmpty()) {
                rol.setPlanEstudio(rol.getPlanesEstudio().get(0));
                ejb.activarDesactivarEvaluacionSugerida(rol.getPlanEstudio());
                listaCuatrimestres();
                listaEvaluacionesRegistradas();
            }else{
            rol.setPlanEstudio(null);
            rol.setCuatrimestres(Collections.emptyList());
            rol.setListaEvidenciasInstrumentos(Collections.emptyList());
            }
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaCuatrimestres(){
        ResultadoEJB<List<Integer>> res = ejb.getGradosProgramaEducativo(rol.getPlanEstudio());
        if(res.getCorrecto()){
            rol.setCuatrimestres(res.getValor());
            rol.setCuatrimestre(rol.getCuatrimestres().get(0));
        }else mostrarMensajeResultadoEJB(res); 
    }
    
    public void listaEvaluacionesRegistradas(){
        if(rol.getTipoBusqueda().equals("busquedaGeneral")){
            ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> res = ejb.buscarEvaluacionSugerida(rol.getProgramaEducativo(), rol.getPlanEstudio(), rol.getDirector());
            if(res.getCorrecto()){
                rol.setListaEvidenciasInstrumentos(res.getValor());
            }else mostrarMensajeResultadoEJB(res);  
        }else{
            ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> res = ejb.buscarEvaluacionSugeridaGrado(rol.getProgramaEducativo(), rol.getPlanEstudio(), rol.getDirector(), rol.getCuatrimestre());
            if(res.getCorrecto()){
                rol.setListaEvidenciasInstrumentos(res.getValor());
            }else mostrarMensajeResultadoEJB(res);  
        }
        registrosCriteriosIncompletos();
    }
    
    public void listaGrados(){
            ResultadoEJB<List<Integer>> res = ejb.getGradosProgramaEducativo(rol.getPlanEstudio());
            if(res.getCorrecto()){
                rol.setGrados(res.getValor());
                rol.setGrado(rol.getGrados().get(0));
                listaMateriasGrado();
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaMateriasGrado(){
            ResultadoEJB<List<Materia>> res = ejb.getMateriasGrado(rol.getPlanEstudio(), rol.getGrado(), rol.getDirector());
            if(res.getCorrecto()){
                rol.setMaterias(res.getValor());
                rol.setMateria(rol.getMaterias().get(0));
                listaUnidadesMateria();
                listaCategorias();
                listaInstrumentosEvaluacion();
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaUnidadesMateria(){
            ResultadoEJB<List<UnidadMateria>> res = ejb.getUnidadesMateria(rol.getMateria());
            if(res.getCorrecto()){
                rol.setUnidadMaterias(res.getValor());
                rol.setUnidadMateria(rol.getUnidadMaterias().get(0));
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaCategorias(){
            ResultadoEJB<List<Criterio>> res = ejb.getCategoriasNivel(rol.getProgramaEducativo());
            if(res.getCorrecto()){
                rol.setCategorias(res.getValor());
                rol.setCategoria(rol.getCategorias().get(0));
                listaEvidenciasEvaluacionCategoria();
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaEvidenciasEvaluacionCategoria(){
            ResultadoEJB<List<EvidenciaEvaluacion>> res = ejbAsignacionIndicadoresCriterios.getEvidenciasCategoria(rol.getCategoria());
            if(res.getCorrecto()){
                rol.setEvidencias(res.getValor());
                rol.setEvidencia(rol.getEvidencias().get(0));
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaInstrumentosEvaluacion(){
            ResultadoEJB<List<InstrumentoEvaluacion>> res = ejbAsignacionIndicadoresCriterios.getInstrumentosEvaluacion();
            if(res.getCorrecto()){
                rol.setInstrumentos(res.getValor());
                rol.setInstrumento(rol.getInstrumentos().get(0));
                rol.setMetaInstrumento(80);
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    
     public void cambiarPrograma(ValueChangeEvent event){
        rol.setProgramaEducativo((AreasUniversidad)event.getNewValue());
        listaPlanesEstudio();
        Ajax.update("frm");
    }
        
    
    public void cambiarPlan(ValueChangeEvent event){
        rol.setPlanEstudio((PlanEstudio)event.getNewValue());
        ejb.activarDesactivarEvaluacionSugerida(rol.getPlanEstudio());
        listaEvaluacionesRegistradas();
    }
    
    public void cambiarCuatrimestre(ValueChangeEvent event){
        rol.setCuatrimestre((Integer)event.getNewValue());
        listaEvaluacionesRegistradas();
        Ajax.update("frm");
    }
    
    public void cambiarTipoBusqueda(ValueChangeEvent event){
        rol.setTipoBusqueda((String)event.getNewValue());
        listaEvaluacionesRegistradas();
        Ajax.update("frm");
    }
 
    public void cambiarAgregarEvid(ValueChangeEvent event){
        if(rol.getAgregarEvidencia()){
            rol.setAgregarEvidencia(false);
            Ajax.update("frm");
        }else{
            rol.setAgregarEvidencia(true);
            listaGrados();
            Ajax.update("frm");
        }
    }
    
    public void cambiarTipoAgregarEvid(ValueChangeEvent event){
        rol.setTipoAgregarEvid((String)event.getNewValue());
        Ajax.update("frm");
    }
    
    public void cambiarGrado(ValueChangeEvent event){
        rol.setGrado((Integer)event.getNewValue());
        listaMateriasGrado();
        Ajax.update("frm");
    }
    
    public void cambiarMateria(ValueChangeEvent event){
        rol.setMateria((Materia)event.getNewValue());
        listaUnidadesMateria();
        Ajax.update("frm");
    }
    
    public void cambiarUnidad(ValueChangeEvent event){
        rol.setUnidadMateria((UnidadMateria)event.getNewValue());
        Ajax.update("frm");
    }
    
    public void cambiarCategoria(ValueChangeEvent event){
        rol.setCategoria((Criterio)event.getNewValue());
        listaEvidenciasEvaluacionCategoria();
        Ajax.update("frm");
    }
    
    public void cambiarEvidencia(ValueChangeEvent event){
        rol.setEvidencia((EvidenciaEvaluacion)event.getNewValue());
        Ajax.update("frm");
    }
    
    public void cambiarInstrumento(ValueChangeEvent event){
        rol.setInstrumento((InstrumentoEvaluacion)event.getNewValue());
        Ajax.update("frm");
    }
   
     /**
     * Permite registrar una evidencia e intrumento de evaluación a una unidad o a todas las unidades de una materia
     */
    public void agregarNuevaEvidencia(){
        if("masivaEvid".equals(rol.getTipoAgregarEvid())){
            ResultadoEJB<Integer> res = ejb.buscarEvidenciaListaEvidenciasInstrumentos(rol.getUnidadMaterias(), rol.getEvidencia());
            if(res.getCorrecto() && res.getValor()==0){
                ResultadoEJB<List<EvaluacionSugerida>> agregar = ejb.agregarEvidenciaListaEvidenciasInstrumentos(rol.getUnidadMaterias(), rol.getEvidencia(), rol.getInstrumento(), rol.getMetaInstrumento(), rol.getPeriodoActivo());
                if(agregar.getCorrecto()){
                    mostrarMensajeResultadoEJB(agregar);
                    listaEvaluacionesRegistradas();
                    Ajax.update("frm");
                }
            }else if(res.getCorrecto() && res.getValor()>0){
                Messages.addGlobalWarn("La evidencia ya se encuentra registrada en las unidades seleccionadas.");
            }else{
                mostrarMensajeResultadoEJB(res);
            }
        }else{
            ResultadoEJB<Boolean> res = ejb.buscarEvidenciaUnidadListaEvidenciasInstrumentos(rol.getUnidadMateria(), rol.getEvidencia());
            if(res.getCorrecto() && !res.getValor()){
                ResultadoEJB<EvaluacionSugerida> agregar = ejb.agregarEvidenciaUnidadListaEvidenciasInstrumentos(rol.getUnidadMateria(), rol.getEvidencia(), rol.getInstrumento(), rol.getMetaInstrumento(), rol.getPeriodoActivo());
                if(agregar.getCorrecto()){
                    mostrarMensajeResultadoEJB(agregar);
                    listaEvaluacionesRegistradas();
                    Ajax.update("frm");
                }
            }else if(res.getCorrecto() && res.getValor()){
                Messages.addGlobalWarn("La evidencia ya se encuentra en las unidad de la materia seleccionada.");
            }else{
                mostrarMensajeResultadoEJB(res);
            }
        }
    }
  
     /**
     * Permite eliminar una evidencia e instrumentos de evaluación registrado
     * @param dtoRegistroEvidInstEvaluacionMateria
     */
    public void eliminarEvidencia(DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarEvidenciaUnidadListaEvidenciasInstrumentos(dtoRegistroEvidInstEvaluacionMateria);
        mostrarMensajeResultadoEJB(resEliminar);
        listaEvaluacionesRegistradas();
        Ajax.update("frm");
    }
    
     /**
     * Permite eliminar los registros del plan de estudio activo seleccionado
     */
    public void eliminarEvaluacionSugerida(){
        if(rol.getPlanEstudio().getEstatus()){
            ResultadoEJB<Integer> resEliminar = ejb.eliminarRegistrosPlanEstudio(rol.getPlanEstudio(), rol.getDirector());
            mostrarMensajeResultadoEJB(resEliminar);
            rol.setListaEvidenciasInstrumentos(Collections.EMPTY_LIST);
            Ajax.update("frm");
        }else{
            Messages.addGlobalWarn("No se pueden eliminar registros de un plan de estudio inactivo.");
        }
    }
    
     /**
     * Permite descargar la plantilla con datos del plan de estudio seleccionado
     * @throws java.io.IOException
     */
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejb.getPlantillaEvidInstMateria(rol.getPlanEstudio(), rol.getProgramaEducativo(), rol.getDirector()));
        Faces.sendFile(f, true);
    }
    
     /**
     * Permite descargar reporte de registros del plan de estudio seleccionado
     * @throws java.io.IOException
     */
    public void descargarRegistrosPlan() throws IOException, Throwable{
        File f = new File(ejb.getRegistrosPlan(rol.getPlanEstudio(), rol.getProgramaEducativo(), rol.getDirector()));
        Faces.sendFile(f, true);
    }
    
     /**
     * Permite mostrar la lista de evidencias e instrumentos de evaluación de la plantilla cargada de excel, previa a guardarse.
     * @param rutaArchivo
     */
    public void listaPreviaEvidenciasInstrumentos(String rutaArchivo) {
       try {
            if(rutaArchivo != null){
                rol.setRutaArchivo(rutaArchivo);
                rol.setListaPreviaEvidenciasInstrumentos(ejbServicioRegEvidInsEval.getListaRegEvidInstEvaluacion(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(RegistroEvidInstEvalMateriasDireccion.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    /**
     * Permite guardar la lista de evidencias e instrumentos de evaluación de la plantilla cargada de excel.
     */
    public void guardarEvidInstEval() {
        if (rol.getListaPreviaEvidenciasInstrumentos() != null) {
            try {
                ResultadoEJB<List<EvaluacionSugerida>> resGuardar = ejbServicioRegEvidInsEval.guardarEvidInstEval(rol.getListaPreviaEvidenciasInstrumentos(), rol.getPeriodoActivo());
                if (resGuardar.getCorrecto()) {
                    rol.setListaEvaluacioneGuardadas(resGuardar.getValor());
                    mostrarMensajeResultadoEJB(resGuardar);
                } else {
                    mostrarMensajeResultadoEJB(resGuardar);
                }
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                Logger.getLogger(RegistroEvidInstEvalMateriasDireccion.class.getName()).log(Level.SEVERE, null, ex);
                if (rol.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(rol.getRutaArchivo());
                }
            } finally {
                rol.getListaPreviaEvidenciasInstrumentos().clear();
                rol.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    /**
     * Permite cancelar el registro de la lista de evidencias e instrumentos de evaluación de la plantilla cargada de excel, así como la carga del archivo al servidor.
    */
    public void cancelarArchivo(){
        rol.getListaPreviaEvidenciasInstrumentos().clear();
        if (rol.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(rol.getRutaArchivo());
            rol.setRutaArchivo(null);
        }
    }
    
    public void registrosCriteriosIncompletos(){
        ResultadoEJB<List<String>> res = ejb.getEvaluacionesSugeridasCiteriosIncompletos(rol.getListaEvidenciasInstrumentos());
        if(res.getCorrecto()){
            rol.setRegistrosIncompletos(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res); 
    }
}
