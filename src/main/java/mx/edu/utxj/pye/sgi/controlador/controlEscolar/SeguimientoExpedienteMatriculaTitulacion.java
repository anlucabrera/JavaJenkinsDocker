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
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoExpedienteMatriculaRolTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoExpedienteGeneracion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoExpedienteMatriculaTitulacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoExpedienteMatriculaRolTitulacion rol;
    
    @EJB EjbSeguimientoExpedienteGeneracion ejb;
    @EJB EjbIntegracionExpedienteTitulacion  ejbIntegracionExpedienteTitulacion;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @Getter @Setter private Integer expediente;
    @Getter @Setter private ExpedienteTitulacion expedienteTit;
    @Getter @Setter private DtoExpedienteTitulacion nuevoDtoExpMat;
    
    @Getter @Setter private List<DocumentoExpedienteTitulacion> listaDocsExp;
    
    @Inject MostrarFotoExpSegMatriculaTitulacion mostrarFotoExpSegMatriculaTitulacion;
    
    @PostConstruct
    public void init(){
     if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EXPEDIENTE_MATRICULA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTitulacion(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoExpedienteMatriculaRolTitulacion(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            rol.setAperturaPagos(Boolean.FALSE);
            
            rol.getInstrucciones().add("Ingrese matricula o nombre del estuidante del que requiere consultar expediente de titulación");
            rol.getInstrucciones().add("Seleccione el registro.");
            rol.getInstrucciones().add("De clic en el botón Cargar Expediente");
           
            expedientesRegistrados();
            
            rol.setSeleccionoExpediente(Boolean.FALSE);
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento expediente matricula";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite obtener la lista de generaciones 
     */
    public void expedientesRegistrados(){
        ResultadoEJB<List<DtoExpedienteTitulacion>> res = ejb.getExpedientesRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setExpedientesTitulacion(res.getValor());
            }
        }else mostrarMensajeResultadoEJB(res);
    }

    /**
     * Permite mostrar expediente de titulación
     */
    public void mostrarExpediente(){
        expedienteTit = new ExpedienteTitulacion();
        expedienteTit = ejbIntegracionExpedienteTitulacion.buscarExpedienteRegistradoClave(expediente).getValor();
        rol.setDtoExpedienteTitulacion(ejbIntegracionExpedienteTitulacion.getDtoExpedienteTitulacion(expedienteTit).getValor());
        rol.setSeleccionoExpediente(Boolean.TRUE);
        consultarExpediente();
        Ajax.update("formMuestraExpediente");
    }
    
    /**
     * Permite consultar fotografía y documentos del expediente de titulación
     */
    public void consultarExpediente() {
        mostrarFotoExpSegMatriculaTitulacion.mostrarFotografia(rol.getDtoExpedienteTitulacion());
        obtenerListaDocumentosExpediente();
        Ajax.update("formMuestraExpediente");
        Ajax.update("frmDocsExp");
        Ajax.update("frmDocExp");
        Ajax.update("frmFotoExp");
    }
   
     /**
     * Permite validar expediente
     */
    public void validarExpediente(){
        ResultadoEJB<ExpedienteTitulacion> res = ejb.validarExpediente(rol.getDtoExpedienteTitulacion().getExpediente(), rol.getPersonal().getPersonal());
        if(res.getCorrecto()){
            consultarExpediente();
        }
    }
    
    public void actualizarFechaTerminacion() {
        ResultadoEJB<DatosAcademicos> res = ejbIntegracionExpedienteTitulacion.actualizarDatosAcademicos(rol.getDtoExpedienteTitulacion().getDatosAcademicos());
        if(res.getCorrecto()){
            Messages.addGlobalInfo("Se actualizó la fecha de terminación correctamente.");
            consultarExpediente();
        }
    }
    
    public void guardarDatosTitulacion() {
        System.err.println("guardarDatosTitulacion");
//        expedienteTit = rol.getDtoExpedienteTitulacion().getExpediente();
//        ResultadoEJB<ExpedienteTitulacion> res = ejbIntegracionExpedienteTitulacion.actualizarDatosTitulacion(expedienteTit);
//        if(res.getCorrecto()){
//            Messages.addGlobalInfo("Se actualizó el promedio y el dato de servicio social correctamente.");
//            consultarExpediente();
//        }
    }
    
     public void obtenerListaDocumentosExpediente() {
        ResultadoEJB<List<DocumentoExpedienteTitulacion>> res = ejb.obtenerListaDocumentosExpediente(rol.getDtoExpedienteTitulacion().getExpediente());
        if(res.getCorrecto()){
            setListaDocsExp(res.getValor());
            Ajax.update("frmDocsExp");
            Ajax.update("frmDocExp");
        } 
    }
    
}