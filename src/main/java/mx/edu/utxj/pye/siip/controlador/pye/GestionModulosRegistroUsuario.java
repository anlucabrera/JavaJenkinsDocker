/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controlador.pye;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.siip.dto.pye.GestionModulosRegistroUsuarioRolPYE;
import mx.edu.utxj.pye.siip.ejb.pye.EjbGestionModulosRegistro;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecifico;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.siip.dto.pye.DtoModulosRegistroUsuario;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class GestionModulosRegistroUsuario extends ViewScopedRol{
     @Getter @Setter GestionModulosRegistroUsuarioRolPYE rol;
    
    @EJB EjbGestionModulosRegistro ejb;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol personal del departamento de planeación y evaluación, administradores de sistema con clave 349 y 511<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.GESTION_USUARIO_MODULOS_REGISTRO);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarAdministrador(logon.getPersonal().getClave());//validar si es administrador
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new GestionModulosRegistroUsuarioRolPYE(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbPermisoAperturaExtemporanea.getPeriodoActual().getPeriodo());
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione periodo escolar.");
            rol.getInstrucciones().add("A continuación visualizará la lista de aperturas registradas en el periodo seleccionado.");
            rol.getInstrucciones().add("ACTUALIZACIÓN FECHAS - APERTURA REGISTRADA.");
            rol.getInstrucciones().add("Para modificar las fechas de la apertura deberá dar clic en el campo de fecha de inicio o fin, seleccionar la fecha del calendario y dar enter para guardar cambios.");
            rol.getInstrucciones().add("AGREGAR NUEVA APERTURA.");
            rol.getInstrucciones().add("De clic en el componente AGREGAR APERTURA, a continuación deberá indicar si será una apertura por área o personal, indicar el área o la clave del personal, seleccionar el evento, las fechas de inicio y fin en que estará habilitado, dará clic en GUARDAR para registrar la apertura.");
            rol.getInstrucciones().add("ELIMINAR APERTURA.");
            rol.getInstrucciones().add("Ubique la fila que corresponda a la apertura que desea eliminar, dará clic en el icono de la columna ELIMINAR.");
           
            rol.setAgregarUsuario(false);
            rol.setAgregarAreaRegistro(false);
            listadoEjesRegistro();
            listaAreas();
        }catch (Exception e){mostrarExcepcion(e); }
    }

     /**
     * Permite obtener la lista de periodos escolares en los que existen eventos escolares registrados
     */
    public void listadoEjesRegistro(){
        ResultadoEJB<List<EjesRegistro>> res =  ejb.getEjesRegistro();
        if(res.getCorrecto()){
            rol.setEjesRegistro(res.getValor());
            rol.setEjeRegistro(rol.getEjesRegistro().get(0));
            listadoTiposModulo();;
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de periodos escolares en los que existen eventos escolares registrados
     */
    public void listadoTiposModulo(){
        ResultadoEJB<List<String>> res =  ejb.getTiposModulo();
        if(res.getCorrecto()){
            rol.setTipos(res.getValor());
            rol.setTipo(rol.getTipos().get(0));
            listadoModulosRegistro();
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de periodos escolares en los que existen eventos escolares registrados
     */
    public void listadoModulosRegistro(){
        ResultadoEJB<List<ModulosRegistro>> res =  ejb.getModulosRegistroEje(rol.getEjeRegistro(), rol.getTipo());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setModulosRegistro(res.getValor());
                rol.setModuloRegistro(rol.getModulosRegistro().get(0));
                listaModulosRegistroAsignados();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaModulosRegistroAsignados(){
        ResultadoEJB<List<DtoModulosRegistroUsuario>> res = ejb.getModulosRegistroAsignados(rol.getModuloRegistro());
        if(res.getCorrecto()){
            rol.setListaModulosRegistroAsignados(res.getValor());
            Ajax.update("tbModulosRegAsig");
        }else{ 
            
            mostrarMensajeResultadoEJB(res);
        }
    
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaModulosAsignadosPersonal(){
        ResultadoEJB<List<DtoModulosRegistroUsuario>> res = ejb.getModulosAsignadosPersonal(rol.getPersonal().getPersonal());
        if(res.getCorrecto()){
            if(!res.getValor().isEmpty()){
                rol.setListaModulosAsignadosPersonal(res.getValor());
            }else{
                rol.setListaModulosAsignadosPersonal(Collections.EMPTY_LIST);
            }
        }else{ 
            mostrarMensajeResultadoEJB(res);
        }
        Ajax.update("tbModulosRegAsigPer");
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaAreas(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getAreasDepartamentos(rol.getTipo());
        if(res.getCorrecto()){
            rol.setAreas(res.getValor());
            Ajax.update("tbModulosRegAsig");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaAreasRegistro(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getAreasDepartamentos(rol.getTipo());
        if(res.getCorrecto()){
            rol.setAreasRegistro(res.getValor());
            rol.setAreaRegistro(rol.getAreasRegistro().get(0));
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }

     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<PersonalActivo> completePersonal(String pista){
        ResultadoEJB<List<PersonalActivo>> res = ejbPermisoAperturaExtemporanea.buscarDocente(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarEje(ValueChangeEvent e){
        if(e.getNewValue() instanceof EjesRegistro){
            EjesRegistro eje = (EjesRegistro)e.getNewValue();
            rol.setEjeRegistro(eje);
            listadoModulosRegistro();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarTipo(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String tipo = (String)e.getNewValue();
            rol.setTipo(tipo);
            listadoModulosRegistro();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarModulo(ValueChangeEvent e){
        if(e.getNewValue() instanceof ModulosRegistro){
            ModulosRegistro modulo = (ModulosRegistro)e.getNewValue();
            rol.setModuloRegistro(modulo);
            listaModulosRegistroAsignados();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del input para agregar una apertura y se muestren los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarUsuario(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarUsuario(valor);
            if(!rol.getAgregarUsuario()){
                inicializarValoresAgregar();
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
     * @param e Evento del cambio de valor
     */
    public void cambiarPersonal(ValueChangeEvent e){
        if(e.getNewValue() instanceof PersonalActivo){
            PersonalActivo docente = (PersonalActivo)e.getNewValue();
            rol.setPersonal(docente);
            listaModulosAsignadosPersonal();
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como personal no es del tipo necesario.");
    }
    
    /**
     * Permite cambiar o seleccionar un tipo de búsqueda se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarArea(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarAreaRegistro(valor);
            if(rol.getAgregarAreaRegistro()){
                listaAreasRegistro();
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite cambiar o seleccionar un área se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarAreaRegistro(ValueChangeEvent e){
        if(e.getNewValue() instanceof AreasUniversidad){
            AreasUniversidad area = (AreasUniversidad)e.getNewValue();
            rol.setAreaRegistro(area);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }

     /**
     * Permite guardar una apertura extemporánea por personal
     */
    public void guardarAsignacion(){
        if(rol.getAgregarAreaRegistro()){
            ResultadoEJB<ModulosRegistroEspecifico> agregar = ejb.guardarAsignacionModulo(rol.getModuloRegistro(), rol.getPersonal().getPersonal(), rol.getAreaRegistro());
            if (agregar.getCorrecto()) {
                mostrarMensajeResultadoEJB(agregar);
                listaModulosRegistroAsignados();
                listaModulosAsignadosPersonal();
                Ajax.update("frm");
            } mostrarMensajeResultadoEJB(agregar);
        }else{
            ResultadoEJB<ModulosRegistroEspecifico> agregar = ejb.guardarAsignacionModulo(rol.getModuloRegistro(), rol.getPersonal().getPersonal(), new AreasUniversidad());
            if (agregar.getCorrecto()) {
                mostrarMensajeResultadoEJB(agregar);
                listaModulosRegistroAsignados();
                listaModulosAsignadosPersonal();
                Ajax.update("frm");
            } mostrarMensajeResultadoEJB(agregar);
        }
    }
    
     /**
     * Permite guardar una apertura extemporánea por personal
     */
    public void inicializarValoresAgregar(){
        rol.setAgregarAreaRegistro(false);
        rol.setAreaRegistro(null);
        rol.setAgregarUsuario(false);
        rol.setPersonal(null);
        rol.setListaModulosAsignadosPersonal(Collections.EMPTY_LIST);
        Ajax.update("frm");
    }
    
    /**
     * Permite eliminar la asignación del usuario al módulo de registro seleccionado
     * @param dtoModulosRegistroUsuario
    */
    public void eliminarAsignacion(DtoModulosRegistroUsuario dtoModulosRegistroUsuario){
        ResultadoEJB<Integer> res = ejb.eliminarAsignacionModulo(dtoModulosRegistroUsuario.getModulosRegistroEspecifico());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            listaModulosRegistroAsignados();
            listaModulosAsignadosPersonal();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
        
    }
 
    
     /**
     * Permite editar la fechas de inicio y fin de la apertura extemporánea seleccionada
     * @param event Evento de edición de la celda
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoModulosRegistroUsuario moduloAsignado = (DtoModulosRegistroUsuario) dataTable.getRowData();
        ResultadoEJB<ModulosRegistroEspecifico> resAct = ejb.actualizarAsignacionModulo(moduloAsignado);
        mostrarMensajeResultadoEJB(resAct);
    }
    
}
