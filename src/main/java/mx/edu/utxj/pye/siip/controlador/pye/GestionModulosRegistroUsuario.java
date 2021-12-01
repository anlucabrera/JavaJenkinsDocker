/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controlador.pye;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
            
            rol.getInstrucciones().add("Seleccione eje de registro.");
            rol.getInstrucciones().add("Seleccione tipo de módulo.");
            rol.getInstrucciones().add("Dependiendo del tipo de módulo se actualizará la lista de módulos de registro.");
            rol.getInstrucciones().add("Seleccione el módulo de registro.");
            rol.getInstrucciones().add("Se actualizará la tabla y podrá consultar los usuarios que están asignados al módulo de registro.");
            rol.getInstrucciones().add("ELIMINAR ASIGNACIÓN.");
            rol.getInstrucciones().add("Deberá dar clic en el icono ubicado en la columna ELIMINAR de la fila del registro que desea eliminar.");
            rol.getInstrucciones().add("AGREGAR ASIGNACIÓN.");
            rol.getInstrucciones().add("Seleccione que desea agregar usuario.");
            rol.getInstrucciones().add("Ingrese nombre o clave del personal al que se le asignará el módulo, le aparecerá una lista de coincidencias, dará clic en el nombre que corresponda");
            rol.getInstrucciones().add("Deberá dar clic en el botón guardar para registrar la asignación.");
            
            rol.setAgregarUsuario(false);
            rol.setAgregarAreaRegistro(false);
            rol.setPestaniaActiva(0);
            listadoEjesRegistro();
            listaAreas();
        }catch (Exception e){mostrarExcepcion(e); }
    }

     /**
     * Permite obtener la lista de ejes de registro
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
     * Permite obtener la lista de tipos de módulo (Registro y/o Seguimiento)
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
     * Permite obtener la lista de módulos de registro
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
     * Permite obtener la lista de módulos de registro con los usuarios que tiene asignados
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
     * Permite obtener la lista de módulos de registros que tiene asignados el personal seleciconado
     */
    public void listaModulosAsignadosPersonal(){
        ResultadoEJB<List<DtoModulosRegistroUsuario>> res = ejb.getModulosAsignadosPersonal(rol.getPersonal().getPersonal());
        if(res.getCorrecto()){
            if(!res.getValor().isEmpty()){
                rol.setListaModulosAsignadosPersonal(res.getValor());
            }
            rol.setPestaniaActiva(0);
            Ajax.update("tbModulosRegAsigPer");
        }else{ 
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite obtener la lista de áreas que tienen POA que tienen módulos de registro que pueden asignarse al registrar una nueva asignación
     */
    public void listaAreas(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getAreasDepartamentos(rol.getTipo());
        if(res.getCorrecto()){
            rol.setAreas(res.getValor());
            Ajax.update("tbModulosRegAsig");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de áreas que tienen POA que tienen módulos de registro que pueden asignarse al actualizar un registro
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
     * Método para proporcionar lista de personal sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
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
     * Permite que al cambiar o seleccionar el eje de registro se actualice la información en la tabla que corresponda
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
     * Permite que al cambiar o seleccionar un tipo de módulo se actualice la información en la tabla que corresponda
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
     * Permite que al cambiar o seleccionar un módulo de registro se actualice la información en la tabla que corresponda
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
     * Permite que al cambiar el valor del input para agregar una nueva asignación y se muestren los componentes correspondientes
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
     * Permite que al cambiar o seleccionar el personal se puedan actualizar la lista de módulo que tiene asignados
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
     * Permite cambiar o seleccionar si se agregará un área de registro diferente a la que pertenece el personal se actualice el valor de la variable
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
     * Permite guardar una asignación
     */
    public void guardarAsignacion(){
        if(rol.getAgregarAreaRegistro()){
            ResultadoEJB<ModulosRegistroEspecifico> agregar = ejb.guardarAsignacionModulo(rol.getModuloRegistro(), rol.getPersonal().getPersonal(), rol.getAreaRegistro());
            if (agregar.getCorrecto()) {
                rol.setModulosRegistroEspecifico(agregar.getValor());
                listaModulosRegistroAsignados();
                listaModulosAsignadosPersonal();
                Ajax.update("frm");
            } mostrarMensajeResultadoEJB(agregar);
        }else{
            ResultadoEJB<ModulosRegistroEspecifico> agregar = ejb.guardarAsignacionModulo(rol.getModuloRegistro(), rol.getPersonal().getPersonal(), new AreasUniversidad());
            if (agregar.getCorrecto()) {
                rol.setModulosRegistroEspecifico(agregar.getValor());
                mostrarMensajeResultadoEJB(agregar);
                listaModulosRegistroAsignados();
                listaModulosAsignadosPersonal();
                Ajax.update("frm");
            } mostrarMensajeResultadoEJB(agregar);
        }
    }
    
     /**
     * Permite inicializar los valores
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
            rol.setListaModulosAsignadosPersonal(Collections.EMPTY_LIST);
            rol.setListaModulosRegistroAsignados(Collections.EMPTY_LIST);
            listaModulosRegistroAsignados();
            listaModulosAsignadosPersonal();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
        
    }
 
     /**
     * Permite editar el área de registro de la asignación correspondiente
     * @param event Evento de edición de la celda
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoModulosRegistroUsuario moduloAsignado = (DtoModulosRegistroUsuario) dataTable.getRowData();
        ResultadoEJB<ModulosRegistroEspecifico> resAct = ejb.actualizarAsignacionModulo(moduloAsignado);
        mostrarMensajeResultadoEJB(resAct);
    }
    
}
