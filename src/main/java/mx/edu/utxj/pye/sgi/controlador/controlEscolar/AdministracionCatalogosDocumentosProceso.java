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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import javax.faces.event.ValueChangeEvent;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionCatalogosDocumentosProcesoRolVarios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEventoEscolar;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionDocumentosProceso;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AdministracionCatalogosDocumentosProceso extends ViewScopedRol implements Desarrollable {
    @Getter @Setter AdministracionCatalogosDocumentosProcesoRolVarios rol;
    
    @EJB EjbAdministracionDocumentosProceso ejb;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_DOCUMENTOS_PROCESO);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarRolesCatalogoDocumentosProceso(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionCatalogosDocumentosProcesoRolVarios(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            
            asignarAreaPersonal();
            
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione la pestaña que corresponda 'Documentos por proceso' o 'Documentos'");
            rol.getInstrucciones().add("Podrá visualizar la información registrada según la pestaña y el filtro seleccionado.");
            rol.getInstrucciones().add("Podrá ACTIVAR/DESACTIVAR o ELIMINAR el registro ubicandose en la columna de la acción a realizar del registro que corresponda y dando clic en el icono.");
            rol.getInstrucciones().add("Para AGREGAR un documento a un proceso o un nuevo documento, deberá dar clic en el componente.");
            rol.getInstrucciones().add("Se habilitarán las opciones o campos para que pueda realizar el nuevo registro.");
           
            rol.setPeriodoActivo(ejbEventoEscolar.getPeriodoActual());
            rol.setPestaniaActiva(0);
            rol.setAgregarDocumento(false);
            rol.setAgregarDocumentoProceso(false);
            listaProcesos();
            listaDocumentos();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración documentos proceso";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite asignar el área dependiendo del personal logueado
     */
    public void asignarAreaPersonal(){
        if (rol.getPersonal().getAreaOperativa().getArea() == 10) {
            rol.setAreaPersonal("ServiciosEscolares");
        } else if (rol.getPersonal().getAreaOperativa().getArea() == 60) {
            rol.setAreaPersonal("Titulacion");
        } else {
            rol.setAreaPersonal("InformacionEstadistica");
        }
    }
    
     /**
     * Permite obtener la lista de procesos regitrados
     */
    public void listaProcesos(){
        ResultadoEJB<List<String>> res =  ejb.getProcesos(rol.getAreaPersonal());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setProcesos(res.getValor());
                rol.setProceso(rol.getProcesos().get(0));
                listaDocumentosProceso();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de documentos del proceso seleccionado
     */
    public void listaDocumentosProceso(){
        ResultadoEJB<List<DocumentoProceso>> res =  ejb.getDocumentosProceso(rol.getProceso());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setListaDocumentosProceso(res.getValor());
                Ajax.update("frm");
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de documentos registrados disponibles para relacionar a un proceso
     */
    public void listaDocumentosDisponibles(){
        ResultadoEJB<List<Documento>> res =  ejb.getDocumentosDisponibles(rol.getListaDocumentosProceso());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setDocumentosDisponibles(res.getValor());
                rol.setDocumentoSeleccionado(rol.getDocumentosDisponibles().get(0));
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de documentos registrados 
     */
    public void listaDocumentos(){
        ResultadoEJB<List<Documento>> res =  ejb.getDocumentosRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setListaDocumentos(res.getValor());
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite que al cambiar el proceso se actualice la lista de documentos relacionadas
     * @param e Evento del cambio de valor
     */
    public void cambiarProceso(ValueChangeEvent e){
        rol.setProceso((String)e.getNewValue());
        listaDocumentosProceso();
        rol.setPestaniaActiva(0);
        Ajax.update("frm");
    }
    
     /**
     * Permite que al cambiar el documento se cambie el valor del documento seleccionado a relacionar
     * @param e Evento del cambio de valor
     */
    public void cambiarDocumento(ValueChangeEvent e){
        rol.setDocumentoSeleccionado((Documento)e.getNewValue());
    }
    
     /**
     * Permite activar o desactivar si es obligatorio o no el documento del proceso seleccionado
     * @param documentoProceso
     */
    public void activarDesactivarDocumentoProcesoObligatorio(DocumentoProceso documentoProceso){
        ResultadoEJB<DocumentoProceso> res = ejb.activarDesactivarDocumentoProcesoObligatorio(documentoProceso);
        if(res.getCorrecto()){
            documentoProceso.setObligatorio(res.getValor().getObligatorio());
            listaDocumentosProceso();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite eliminar el documento del proceso seleccionado
     * @param documentoProceso
     */
    public void eliminarDocumentoProceso(DocumentoProceso documentoProceso){
        ResultadoEJB<Integer> eliminar = ejb.eliminarDocumentoProceso(documentoProceso);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaDocumentosProceso();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite activar o desactivar el documento seleccionado
     * @param documento
     */
    public void activarDesactivarDocumento(Documento documento){
        ResultadoEJB<Documento> res = ejb.activarDesactivarDocumento(documento);
        if(res.getCorrecto()){
            documento.setActivo(res.getValor().getActivo());
            listaDocumentos();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite eliminar el documento seleccionado
     * @param documento
     */
    public void eliminarDocumento(Documento documento){
        Boolean valor = ejb.registrosDocumento(documento).getValor();
        if(valor){
            Messages.addGlobalWarn("El documento se encuentra relacionado a uno o más procesos, razón por la que no se puede eliminar.");
        }else{
            ResultadoEJB<Integer> eliminar = ejb.eliminarDocumento(documento);
            if(eliminar.getCorrecto()){
                mostrarMensajeResultadoEJB(eliminar);
                listaDocumentos();
                Ajax.update("frm");
            }else mostrarMensajeResultadoEJB(eliminar);
        }
    }
    
    /**
     * Permite que al cambiar de pestaña se muestren las opciones correspondientes
     * @param event Evento del cambio de valor
     */
    public void cambiarPestania(TabChangeEvent event){
        TabView tv = (TabView) event.getComponent();
        rol.setPestaniaActiva(tv.getActiveIndex());
        rol.setAgregarDocumentoProceso(false);
        rol.setAgregarDocumento(false);
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un documento al proceso se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarDocumentoProceso(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarDocumentoProceso(valor);
            if(rol.getAgregarDocumentoProceso()){
                listaDocumentosDisponibles();
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
       
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un documento se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarDocumento(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarDocumento(valor);
            if(rol.getAgregarDocumento()){
                rol.setNuevoDocumento("Ingresar nombre");
                rol.setEspecificacionesDocumento("Ingresar especificaciones");
                rol.setNomenclaturaDocumento("Ingresar nomenclatura");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite guardar el documento al proceso
     */
    public void guardarDocumentoProceso(){
        ResultadoEJB<DocumentoProceso> agregar = ejb.guardarDocumentoProceso(rol.getProceso(), rol.getDocumentoSeleccionado());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaDocumentosProceso();
            rol.setAgregarDocumentoProceso(false);
            rol.setPestaniaActiva(0);
            Ajax.update("frm");
        }
    }
    
    
     /**
     * Permite guardar un nuevo documento
     */
    public void guardarDocumento(){
        if (rol.getNuevoDocumento().equals("Ingresar nombre") || rol.getEspecificacionesDocumento().equals("Ingresar especificaciones") || rol.getNomenclaturaDocumento().equals("Ingresar nomenclatura")) {
              Messages.addGlobalWarn("Verifique que haya ingresado el nombre, especificaciones y nomenclatura del nuevo documento correctamente.");
        } else{
            Integer coincidencia = (int) rol.getListaDocumentos().stream().filter(p -> p.getDescripcion().equals(rol.getNuevoDocumento())).count();
            if(coincidencia == 0){
                ResultadoEJB<Documento> agregar = ejb.guardarDocumento(rol.getNuevoDocumento(), rol.getEspecificacionesDocumento(), rol.getNomenclaturaDocumento());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaDocumentos();
                    rol.setAgregarDocumento(false);
                    rol.setPestaniaActiva(1);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El documento que desea agregar ya está registrado.");
            }
        }
    }
    
    /**
     * Permite actualizar la información del documento: descripción, especificaciones y nomenclatura
     * @param event
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        Documento actualizaciones = (Documento) dataTable.getRowData();
        ResultadoEJB<Documento> resAct = ejb.actualizarDocumento(actualizaciones);
        mostrarMensajeResultadoEJB(resAct);

    }
    
     /**
     * Método para verificar si existe algún registro con el documento seleccionado
     * @param documento
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacion(@NonNull Documento documento){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosDocumento(documento);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}
