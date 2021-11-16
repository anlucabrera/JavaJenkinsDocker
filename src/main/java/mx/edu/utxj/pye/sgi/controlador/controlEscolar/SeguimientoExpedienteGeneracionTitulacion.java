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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPagosEstudianteFinanzas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoExpedienteGeneracionRolTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoExpedienteGeneracion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoExpedienteGeneracionTitulacion extends ViewScopedRol implements Desarrollable{
    
    @Getter @Setter SeguimientoExpedienteGeneracionRolTitulacion rol;
    
    @EJB EjbSeguimientoExpedienteGeneracion ejb;
    @EJB EjbIntegracionExpedienteTitulacion  ejbIntegracionExpedienteTitulacion;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
     if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EXPEDIENTE_GENERACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTitulacion(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoExpedienteGeneracionRolTitulacion(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            rol.setAperturaPagos(Boolean.FALSE);
            
            rol.getInstrucciones().add("Seleccione generación para consultar expedientes registrados.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Seleccione programa educativo.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Consultar expediente, Validar o Invalidar expediente, Consultar lista de pagos.");
            rol.getInstrucciones().add("Al dar clic en el botón de Consultar expediente se abrirá en una nueva ventana el expediente individual.");
            rol.getInstrucciones().add("Puede validar y/o invalidar el expediente según lo requiera.");
            rol.getInstrucciones().add("Si da clic en Consultar lista de pagos, podrá visualizar una ventana con los pagos del estudiante registrados por finanzas.");
           
            listaGeneracionesExpedientesRegistrados();
            pasosRegistro();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento expediente generacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaGeneracionesExpedientesRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesExpedientes();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<String>> res = ejb.obtenerListaNivelesGeneracion(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaProgramasEducativosNivel();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    
    public void listaProgramasEducativosNivel(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<AreasUniversidad>> res = ejb.obtenerListaProgramasEducativos(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            listaExpedientesProgramaEducativo();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite que al cambiar o seleccionar generación se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion= (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar nivel educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String nivel= (String)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaProgramasEducativosNivel();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof AreasUniversidad){
            AreasUniversidad programa= (AreasUniversidad)e.getNewValue();
            rol.setProgramaEducativo(programa);
            listaExpedientesProgramaEducativo();
            Ajax.update("frm");
        }
    }
    
    /**
     * Permite obtener la lista de expedientes registrados de la generación, nivel y programa educativo seleccionado
     */
    public void listaExpedientesProgramaEducativo(){
        ResultadoEJB<List<DtoExpedienteTitulacion>> res = ejb.obtenerListaExpedientesProgramaEducativo(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            rol.setExpedientesTitulacionPE(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite validar expediente de titulación
     * @param expediente
     */
    public void validarExpediente(ExpedienteTitulacion expediente){
        ResultadoEJB<ExpedienteTitulacion> res = ejb.validarExpediente(expediente, rol.getPersonal().getPersonal());
        if(res.getCorrecto()){
            expediente.setValidado(res.getValor().getValidado());
            listaExpedientesProgramaEducativo();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     public void consultarListaPagos(Integer matricula) {
        ResultadoEJB<List<DtoPagosEstudianteFinanzas>> res = ejbIntegracionExpedienteTitulacion.getListaDtoPagosEstudianteFinanzas(matricula);
        if(res.getCorrecto()){
            rol.setListaPagosEstudianteFinanzas(res.getValor());
            Ajax.update("frmModalPagos");
            Ajax.oncomplete("skin();");
            rol.setAperturaPagos(Boolean.TRUE);
            forzarAperturaPagosDialogo();
        }else mostrarMensajeResultadoEJB(res); 
    }
     
    public void forzarAperturaPagosDialogo(){
        if(rol.getAperturaPagos()){
           Ajax.oncomplete("PF('modalPagos').show();");
           rol.setAperturaPagos(Boolean.FALSE);
        }
    }
    
    /**
     * Permite obtener la lista de pasos de registro 
     */
    public void pasosRegistro(){
        ResultadoEJB<List<String>> res = ejb.getPasosRegistro();
        if(res.getCorrecto()){
            rol.setPasosRegistro(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite actualizar el paso de registro del expediente de titulación seleccionado
     * @param event
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoExpedienteTitulacion registroNew = (DtoExpedienteTitulacion) dataTable.getRowData();
        ResultadoEJB<ExpedienteTitulacion> resAct = ejb.actualizarPasoRegistro(registroNew.getExpediente());
        mostrarMensajeResultadoEJB(resAct);

    }
    
    /**
     * Método que permite descargar en excel el reporte correspondiente del periodo seleccionado
     * @throws java.io.IOException
     */
     public void descargarBaseGeneracion() throws IOException, Throwable{
         File f = new File(ejb.getReporteBaseGeneracion(rol.getGeneracion(), rol.getNivelEducativo()));
         Faces.sendFile(f, true);
    }
}
