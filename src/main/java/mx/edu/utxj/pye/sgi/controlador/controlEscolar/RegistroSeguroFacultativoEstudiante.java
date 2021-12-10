/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroSeguroFacultativoRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroSeguroFacultativoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.TipoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroSeguroFacultativoEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter             @Setter                                         private                             RegistroSeguroFacultativoRolEstudiante              rol;
    @Getter             @Setter                                         private                             Boolean                                             tieneAcceso = false;
    
    @EJB                EjbPropiedades                                  ep;
    @EJB                EjbRegistroSeguroFacultativoEstudiante          ejb;
    @EJB                EjbPacker                                       packer;
    @EJB                EjbPeriodoEventoRegistro                        ejbPeriodoEventoRegistro;
    
    @Inject             LogonMB                                         logonMB;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro seguro facultativo estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
//        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.REGISTRO_SEGURO_FACULTATIVO);
                ResultadoEJB<Estudiante> resAcceso = ejb.validarEstudiante(logonMB.getCurrentUser());
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
                rol = new RegistroSeguroFacultativoRolEstudiante(packer.packEstudianteGeneral(resAcceso.getValor()).getValor());
                tieneAcceso = rol.tieneAcceso(rol.getDtoEstudiante(), UsuarioTipo.ESTUDIANTE19);
                rol.setNivelRol(NivelRol.OPERATIVO);
                if(verificarInvocacionMenu()) return;
                if(!validarIdentificacion()) return;
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                rol.setPeriodoEscolarActivo(ejbPeriodoEventoRegistro.getPeriodoEscolarActivo().getValor());
                filtros();
            }
//        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
//    Incializar DtoSeguroFacultativo
    public void inicializarDtoSeguroFacultivo(){
        rol.setDtoSeguroFacultativo(new DtoSeguroFacultativo(new SegurosFacultativosEstudiante(), rol.getDtoEstudiante()));
    }
//    Inicializar Lista DtoSeguroFacultativo
    public void inicializarListaDtoSeguroFacultativo(){
        rol.setListaDtoSeguroFacultativo(new ArrayList<>());
        rol.setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
    }
    
    public List<TipoSeguroFacultativo> getListaTiposSegurosFacultativos(){
        return TipoSeguroFacultativo.Lista();
    }
    
    public void actualizarInterfaz() {
        Ajax.update("frmRegistro");
        Ajax.update("formularioArchivoTarjetonIMSS");
        Ajax.update("formularioArchivoComprobanteLocalizacion");
        Ajax.update("formularioComprobanteVigenciaDerechos");
        Ajax.update("frmMensajes");
        Ajax.update("frmListadoSegurosFacultativos");
        Ajax.update("formularioBotonValidacion");
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Filtros *********************************************************/
    public void filtros(){
        if(rol.getDtoEstudiante() == null){
            mostrarMensaje("No se pueden realizar las demás acciones debido a que no se pudo consultar la información del estudiante");
        }
        inicializarDtoSeguroFacultivo();
        inicializarListaDtoSeguroFacultativo();

//        Validación para verificar si existen registros anteriormente registrados
        ResultadoEJB<List<DtoSeguroFacultativo>> listaDtoSegurosFacultativosEstudiante  = ejb.buscaSegurosFacultativosRegistrados(rol.getDtoEstudiante());
        if(listaDtoSegurosFacultativosEstudiante.getCorrecto()){
            mostrarMensajeResultadoEJB(listaDtoSegurosFacultativosEstudiante);
            inicializarListaDtoSeguroFacultativo();
            rol.setListaDtoSeguroFacultativo(listaDtoSegurosFacultativosEstudiante.getValor());
        }else{
            mostrarMensajeResultadoEJB(listaDtoSegurosFacultativosEstudiante);
            inicializarListaDtoSeguroFacultativo();
        }
        
//        Validación que permite verificar si el estudiante es activo y evitar que se cree un registro erroneo
        if (rol.getDtoEstudiante().getInscripcionActiva().getActivo()) {
            rol.setEstudianteActivo(true);
            //        Validación para un nyuevo registro o creación de uno nuevo
            ResultadoEJB<List<DtoSeguroFacultativo>> aplicarRegistroAutomaticoSeguroFacultativo = ejb.aplicarRegistroAutomaticoSeguroFacultativo(rol.getDtoEstudiante());
            if (aplicarRegistroAutomaticoSeguroFacultativo.getCorrecto()) {
                mostrarMensajeResultadoEJB(aplicarRegistroAutomaticoSeguroFacultativo);
                inicializarDtoSeguroFacultivo();
                rol.setDtoSeguroFacultativo(aplicarRegistroAutomaticoSeguroFacultativo.getValor().get(0));
            } else {
                mostrarMensajeResultadoEJB(aplicarRegistroAutomaticoSeguroFacultativo);
                inicializarDtoSeguroFacultivo();
            }
        }else{
            rol.setEstudianteActivo(false);
        }
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Método y funciones *********************************************************/
    
    public void actualizarSeguroFacultativo(SegurosFacultativosEstudiante seguroFacultativo){
        ResultadoEJB<Boolean> res = ejb.editaRegistroSeguroFacultativo(seguroFacultativo);
        if(res.getCorrecto()) {
            filtros();
            actualizarInterfaz();
            mostrarMensajeResultadoEJB(res);
        }else{
            actualizarInterfaz();
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void actualizarTipoSeguroFacultativo(ValueChangeEvent event){
        if(event == null){mostrarMensaje("No se ha enviado ningún dato para actualizar");return;}
        if(" - Seleccione - ".equals((String) event.getNewValue())) {mostrarMensaje("Se ha seleccionado un dato invalido");return;}
        SegurosFacultativosEstudiante seguroFacultativo = (SegurosFacultativosEstudiante) event.getComponent().getAttributes().get("seguroFacultativo");
        String tipoSeguroFacultativo = (String) event.getNewValue();
        seguroFacultativo.setTipoSeguro(tipoSeguroFacultativo);
        actualizarSeguroFacultativo(seguroFacultativo);
    }
    
    public void actualizarCorreoElectronico(ValueChangeEvent event){
        if(event == null){mostrarMensaje("No se ha enviado ningún dato para actualizar");return;}
        if(event.getNewValue() == null){mostrarMensaje("Favor de ingresar un correo electronico");return;}
        if("".equals((String) event.getNewValue())) {mostrarMensaje("Se ha seleccionado un dato invalido");return;}
        SegurosFacultativosEstudiante seguroFacultativo = (SegurosFacultativosEstudiante) event.getComponent().getAttributes().get("seguroFacultativo");
        String correoElectronico = (String) event.getNewValue();
        seguroFacultativo.setCorreoElectronico(correoElectronico);
        actualizarSeguroFacultativo(seguroFacultativo);
    }
    
    public void enviarARevisionSeguroFacultativo(DtoSeguroFacultativo dtoSeguroFacultativo){
        ResultadoEJB<DtoSeguroFacultativo> resultado = ejb.enviarRevisionSeguroFacultativoEstudiante(dtoSeguroFacultativo);
        if(resultado.getCorrecto()){
            filtros();
            actualizarInterfaz();
            mostrarMensajeResultadoEJB(resultado);
        }else{
            actualizarInterfaz();
            mostrarMensajeResultadoEJB(resultado);
        }
    }
    
    public void cancelarEnvioARevision(DtoSeguroFacultativo dtoSeguroFacultativo){
        ResultadoEJB<DtoSeguroFacultativo> resultado = ejb.cancelarEnvioARevision(dtoSeguroFacultativo);
        if(resultado.getCorrecto()){
            filtros();
            actualizarInterfaz();
            mostrarMensajeResultadoEJB(resultado);
        }else{
            actualizarInterfaz();
            mostrarMensajeResultadoEJB(resultado);
        }
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Evidencias *********************************************************/
    
    public void subirTarjetonImss() {
        try {
            ResultadoEJB<Map.Entry<Boolean, Integer>> res = ejb.registrarArchivoTarjetonImssSf(rol.getDtoSeguroFacultativo(), rol.getArchivoTarjetonImss());
            if (res.getCorrecto()) {
                filtros();
                actualizarInterfaz();
                Messages.addGlobalInfo(res.getMensaje());
            } else {
                filtros();
                actualizarInterfaz();
                Messages.addGlobalError(res.getMensaje());
            }
        } catch (Throwable ex) {
            Logger.getLogger(RegistroSeguroFacultativoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirComprobanteLocalizacion(){
        try {
            Map.Entry<Boolean, Integer> res = ejb.registrarArchivoRutaComprobanteLocalizacionSf(rol.getDtoSeguroFacultativo(), rol.getArchivoComprobanteLocalizacion());
            if (res.getKey()) {
                Messages.addGlobalInfo("El Comprobante de Localización se ha guardado correctamente.");
//                Mover a codigo independiente para evitar repeticion, colocado solo como prueba - Ingresar codigo para actualización de interfaz
                filtros();
                actualizarInterfaz();
            } else {
                Messages.addGlobalError(String.format("Se registraron %s de %s archivos, verifique e intente agregar el archivo faltante.", res.getValue().toString(), String.valueOf(1)));
            }
        } catch (Throwable ex) {
            Logger.getLogger(RegistroSeguroFacultativoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirComprobanteVigenciaDerechos(){
        try {
            Map.Entry<Boolean, Integer> res = ejb.registrarArchivoComprobanteVigenciaDerechosSf(rol.getDtoSeguroFacultativo(), rol.getArchivoComprobanteVigenciaDerechos());
            if (res.getKey()) {
                Messages.addGlobalInfo("El Comprobante de Vigencia de Derechos se ha guardado correctamente.");
//                Mover a codigo independiente para evitar repeticion, colocado solo como prueba - Ingresar codigo para actualización de interfaz
                filtros();
                actualizarInterfaz();
            } else {
                Messages.addGlobalError(String.format("Se registraron %s de %s archivos, verifique e intente agregar el archivo faltante.", res.getValue().toString(), String.valueOf(1)));
            }
        } catch (Throwable ex) {
            Logger.getLogger(RegistroSeguroFacultativoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void descargarArchivoTarjetonImss() throws IOException{
        File f = new File(rol.getDtoSeguroFacultativo().getSeguroFactultativo().getRutaTarjeton());
        Faces.sendFile(f, false);
    }
    
    public void descargarArchivoComprobanteLocalizacion() throws IOException{
        File f = new File(rol.getDtoSeguroFacultativo().getSeguroFactultativo().getRutaComprobanteLocalizacion());
        Faces.sendFile(f, false);
    }
    
    public void descargarArchivoComprobanteVigenciaDerechos() throws IOException{
        File f = new File(rol.getDtoSeguroFacultativo().getSeguroFactultativo().getRutaComprobanteVigenciaDeDerechos());
        Faces.sendFile(f, false);
    }
    
    public void eliminarArchivoTarjetonImss(){
        Boolean eliminado = ejb.eliminarArchivoTarjetonImss(rol.getDtoSeguroFacultativo());
        if(eliminado){
            Messages.addGlobalInfo("El archivo de tipo Tarjetón IMSS se eliminó de forma correcta.");
            filtros();
            actualizarInterfaz();
        }else Messages.addGlobalError("El archivo de tipo Tarjetón IMSS no pudo eliminarse.");
    }
    
    public void eliminarArchivoComprobanteLocalizacion(){
        Boolean eliminado = ejb.eliminarArchivoComprobanteLocalizacion(rol.getDtoSeguroFacultativo());
        if(eliminado){
            Messages.addGlobalInfo("El archivo de tipo Comprobante Localización se eliminó de forma correcta.");
            filtros();
            actualizarInterfaz();
        }else Messages.addGlobalError("El archivo de tipo Comprobante Localización no pudo eliminarse.");
    }
    
    public void eliminarArchivoComprobanteVigenciaDerechos(){
        Boolean eliminado = ejb.eliminarArchivoComprobanteVigenciaDerechos(rol.getDtoSeguroFacultativo());
        if(eliminado){
            Messages.addGlobalInfo("El archivo de tipo Comprobante Vigencia de Derechos se eliminó de forma correcta.");
            filtros();
            actualizarInterfaz();
        }else Messages.addGlobalError("El archivo de tipo Comprobante Vigencia de Derechos no pudo eliminarse.");
    }
    
    public void descargarArchivoTarjetonImss(String rutaArchivo) throws IOException{
        if(rutaArchivo == null) {mostrarMensaje("No se cargó ningún archivo"); return;}
        if("".equals(rutaArchivo)){mostrarMensaje("No se cargó ningún archivo"); return;}
        File f = new File(rutaArchivo);
        Faces.sendFile(f, false);
    }
    
    public void descargarArchivoComprobanteLocalizacion(String rutaArchivo) throws IOException{
        if(rutaArchivo == null) {mostrarMensaje("No se cargó ningún archivo"); return;}
        if("".equals(rutaArchivo)){mostrarMensaje("No se cargó ningún archivo"); return;}
        File f = new File(rutaArchivo);
        Faces.sendFile(f, false);
    }
    
    public void descargarArchivoComprobanteVigenciaDerechos(String rutaArchivo) throws IOException{
        if(rutaArchivo == null) {mostrarMensaje("No se cargó ningún archivo"); return;}
        if("".equals(rutaArchivo)){mostrarMensaje("No se cargó ningún archivo"); return;}
        File f = new File(rutaArchivo);
        Faces.sendFile(f, false);
    }
    
}
