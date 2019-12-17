/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.Date;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PrestamoDocumentoRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPrestamoDocumentos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PrestamosDocumentos;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class PrestamoDocumentoServiciosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter             @Setter                         PrestamoDocumentoRolServiciosEscolares              rol;
    @Getter             Boolean                         tieneAcceso = false;
    @Getter             private                         Boolean                                             cargado = false;
    
    @EJB                EjbPrestamoDocumentos           ejb;
    @EJB                EjbPropiedades                  ep;
    
    @Inject             LogonMB                         logonMB;
    
     @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            
            setVistaControlador(ControlEscolarVistaControlador.PRESTAMO_DOCUMENTOS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
//            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new PrestamoDocumentoRolServiciosEscolares(filtro);
            tieneAcceso = rol.tieneAcceso(rol.getPersonal());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            mostrarPrestamoDocumentos();
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarValores() {
        rol.setEstudianteSeleccionado(new Estudiante());
        rol.setEstudianteSeguimientoSeleccionado(new DtoEstudianteComplete(new Estudiante(), ""));
        rol.setDocumentosentregadosestudiante(new Documentosentregadosestudiante());
        
        rol.setPrestamoDocumento(new PrestamosDocumentos());
    }
    
    public void inicializarPrestamoDocumento() {
        rol.getPrestamoDocumento().setEstudiante(rol.getEstudianteSeleccionado());
        rol.getPrestamoDocumento().setFechaDevolucion(null);
        rol.getPrestamoDocumento().setObservaciones(null);
        rol.getPrestamoDocumento().setEntregadoEstudiante(false);
        rol.getPrestamoDocumento().setPersona(rol.getPersonal().getPersonal().getClave());
    }
    
    /*********************************************** Administración de datos ***************************************************/
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            ResultadoEJB<Estudiante> estudianteEncontrado = ejb.buscaEstudiante(estudiante.getEstudiantes().getIdEstudiante());
            if(estudianteEncontrado.getCorrecto()){
                rol.setEstudianteSeleccionado(estudianteEncontrado.getValor());
                mostrarPrestamoPorEstudiante();
                mostrarDocumentosEntregadosEstudiante();
                inicializarPrestamoDocumento();
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void valifaFechaInicio(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getFechaFin() != null || rol.getFechaFin() != null) {
            rol.setFechaInicio((Date) event.getNewValue());
            if (rol.getFechaInicio().before(rol.getFechaFin())) {
            } else {
                if (rol.getFechaFin().before(rol.getFechaInicio())) {
                    rol.setFechaFin(null);
                    rol.setFechaFin(rol.getFechaInicio());
                } else {}
            }
        }
    }
    
    public void validaFechaFin(ValueChangeEvent event){
        if ((Date) event.getNewValue() != null && rol.getFechaInicio() != null || rol.getFechaInicio() != null) {
            rol.setFechaFin((Date) event.getNewValue());
            if (rol.getFechaFin().after(rol.getFechaInicio())) {
            } else {
                if (rol.getFechaInicio().after(rol.getFechaFin())) {
                    rol.setFechaInicio(null);
                    rol.setFechaInicio(rol.getFechaFin());
                } else {}
            }
        }
    }
    
    public void guardarPrestamoDocumento(){
        if(rol.getPrestamoDocumento().getTipoPrestamo().equals("Préstamo")){
//            Acciones a tomar si es un prestamo normal
            ResultadoEJB<PrestamosDocumentos> res = ejb.guardarPrestamoDocumento(rol.getPrestamoDocumento());
            mostrarMensajeResultadoEJB(res);
            mostrarPrestamoDocumentos();
        }else{
//            Acciones a tomar si es un alta de documento
            rol.getPrestamoDocumento().setFechaPrestamo(null);
            rol.getPrestamoDocumento().setFechaDevolucionEstudiante(null);
            rol.getPrestamoDocumento().setFechaDevolucion(new Date());
            rol.getPrestamoDocumento().setEntregadoEstudiante(Boolean.TRUE);
            ResultadoEJB<Boolean> res = ejb.altaDocumentoEstudiante(rol.getEstudianteSeleccionado(),rol.getPrestamoDocumento().getTipoDocumento(), rol.getPrestamoDocumento());
            mostrarMensajeResultadoEJB(res);
            mostrarPrestamoDocumentos();
        }
    }
    
    public void actualizarComentariosPrestamoDocumento(ValueChangeEvent event){
        if(event.getNewValue() instanceof String){
            PrestamosDocumentos prestamoDocumento = (PrestamosDocumentos) event.getComponent().getAttributes().get("prestamoDocumento");
            String observaciones = (String) event.getNewValue().toString();
            prestamoDocumento.setObservaciones(observaciones);
            actualizarPrestamoDocumento(prestamoDocumento);
        }else{mostrarMensaje("Favor de ingresar una observación.");}
    }
    
    public void actualizarPrestamoDocumento(PrestamosDocumentos prestamoDocumento){
        ResultadoEJB<PrestamosDocumentos> res = ejb.actualizarObservacionesPrestamoDocumento(prestamoDocumento);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void liberarPrestamoDocumento(ValueChangeEvent event) {
        if (event.getComponent().getAttributes().get("prestamoDocumento") != null) {
            PrestamosDocumentos prestamo = (PrestamosDocumentos) event.getComponent().getAttributes().get("prestamoDocumento");
            ResultadoEJB<PrestamosDocumentos> res = ejb.liberarPrestamoDocumento(prestamo);
            mostrarMensajeResultadoEJB(res);
        } else {mostrarMensaje("Favor de elegir un préstamo de documento");}
    }
    
    /*********************************************** Llenado de listas *********************************************************/
    
    public void mostrarPrestamoDocumentos(){
        ResultadoEJB<List<PrestamosDocumentos>> res = ejb.consultaPrestamosUltimosDiez();
        if(res.getCorrecto()){
            rol.setListaPrestamosDocumentos(res.getValor());
            inicializarValores();
        }else{
            mostrarMensajeResultadoEJB(res);
            rol.setListaPrestamosDocumentos(Collections.EMPTY_LIST);
            inicializarValores();
        }
    }
    
    public void mostrarPrestamoPorEstudiante(){
        ResultadoEJB<List<PrestamosDocumentos>> res = ejb.consultaPrestamosPorEstudiante(rol.getEstudianteSeleccionado());
        if(res.getCorrecto()){
            rol.setListaPrestamosDocumentos(res.getValor());
        }else{
            mostrarMensajeResultadoEJB(res);
            rol.setListaPrestamosDocumentos(Collections.EMPTY_LIST);
        }
    }
    
    public void mostrarDocumentosEntregadosEstudiante(){
        ResultadoEJB<Documentosentregadosestudiante> res = ejb.consultarDocumentosEstudiante(rol.getEstudianteSeleccionado());
        if(res.getCorrecto()){
            rol.setDocumentosentregadosestudiante(res.getValor());
        }else{
            mostrarMensajeResultadoEJB(res);
            rol.setDocumentosentregadosestudiante(new Documentosentregadosestudiante());
        }
    }
    
    public void mostrarDocumentosPorFechaOEstudiante(){
        if(rol.getEstudianteSeleccionado().getIdEstudiante() == null){
            if(rol.getFechaInicio() != null || rol.getFechaFin() != null){
                ResultadoEJB<List<PrestamosDocumentos>> res = ejb.consultaPrestamosPorFechaPrestamo(rol.getFechaInicio(), rol.getFechaFin());
                rol.setListaPrestamosDocumentos(res.getValor());
            }else{mostrarMensaje("Favor de revisar si ha seleccionado correctamente las fechas para la consulta");}
        }else{
            if(rol.getFechaInicio() != null || rol.getFechaFin() != null){
                ResultadoEJB<List<PrestamosDocumentos>> res = ejb.consultaPrestamosEstudiantePorFechaPrestamo(rol.getEstudianteSeleccionado(),rol.getFechaInicio(), rol.getFechaFin());
                rol.setListaPrestamosDocumentos(res.getValor());
            }else{mostrarMensaje("Favor de revisar si ha seleccionado correctamente las fechas para la consulta");}
        }
    }
    
    public void mostrarPrestamoTipoEntregadosEstudiante(){
        if(rol.getEstudianteSeleccionado().getIdEstudiante() != null){
            ResultadoEJB<List<PrestamosDocumentos>> res = ejb.consultaEntregasPorEstudiante(rol.getEstudianteSeleccionado());    
            rol.setListaPrestamosDocumentos(res.getValor());
        }else{mostrarMensaje("Para poder mostrar datos favor de realizar previamente una búsqueda de estudiante");}
    }
    
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "prestamo de documentos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
}
