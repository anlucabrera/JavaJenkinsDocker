/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionRolesEstadiaRolDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRolEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Collections;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CoordinadorAreaEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AsignacionRolesEstadiaDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AsignacionRolesEstadiaRolDirector rol;
    
    @EJB EjbAsignacionRolesEstadia ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
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
            setVistaControlador(ControlEscolarVistaControlador.ASIGNACION_ROLES_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logon.getPersonal().getClave());//validar si es director

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es encargado de dirección
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AsignacionRolesEstadiaRolDirector(filtro, director);
            tieneAcceso = rol.tieneAcceso(director);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDirectorCarrera(director);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setDeshabilitarAsignacion(Boolean.TRUE);
            
//            rol.setSoloLectura(true);
            rol.setEventoActivo(ejb.buscarEventoActivo().getValor());
            
            if(rol.getEventoActivo()!= null){
                rol.setGeneracionEventoActivo(ejb.buscarGeneracionEventoActivo(rol.getEventoActivo()).getValor());
            }else{
                rol.setEventoActivo(new EventoEstadia());
                rol.setGeneracionEventoActivo(new Generaciones());
            }
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("A continuación podrá visualizar el listado de personal activo adscrito al área que represente.");
            rol.getInstrucciones().add("Para seleccionar coordinadores y asesores de estadía, deberá dar clic en el icono ASIGNAR ubicado en las columna que corresponda.");
            rol.getInstrucciones().add("Puede eliminar la asignación si cometió un error, dando clic nuevamente el icono.");
           
            rol.setAreaSuperior(ejb.getAreaSuperior(rol.getDirectorCarrera().getPersonal().getClave()).getValor());
            
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "asignacion roles estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesEventosRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de nivele educativos registradas en la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesGeneracionAreaEventosRegistrados(rol.getGeneracion(), rol.getAreaSuperior());
        if(res.getCorrecto()){
            if(!res.getValor().isEmpty()){
                rol.setNivelesEducativos(res.getValor());
                rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
                rol.setEventoSeleccionado(ejb.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Asignacion coordinador asesor estadia").getValor());
                listaPersonalRolesEstadiaAsignados();
            }else{
                rol.setNivelesEducativos(Collections.EMPTY_LIST);
            }
            
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de personal del área para asignar roles de estadía
     */
    public void listaPersonalRolesEstadiaAsignados(){
        ResultadoEJB<List<DtoRolEstadia>> res = ejb.getDocentesPorAreaRolesEstadia(rol.getAreaSuperior(), rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setRolesEstadia(res.getValor());
            if(rol.getEventoSeleccionado().getEvento() == rol.getEventoActivo().getEvento()){
                rol.setDeshabilitarAsignacion(Boolean.FALSE);
            }else{
                rol.setDeshabilitarAsignacion(Boolean.TRUE);
            }
            Ajax.update("tbListaRolesEstadia");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de personal
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de personal
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaPersonalRolesEstadiaAsignados();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite asignar/quitar el rol de coordinador de estadía del área
     * @param dtoRolEstadia
     */
    public void asignarCoordinadorEstadia(DtoRolEstadia dtoRolEstadia){
        ResultadoEJB<CoordinadorAreaEstadia> resAsignar = ejb.asignarCoordinadorEstadia(dtoRolEstadia, rol.getAreaSuperior(), rol.getEventoActivo());
        mostrarMensajeResultadoEJB(resAsignar);
        listaPersonalRolesEstadiaAsignados();
        Ajax.update("frm");
    }
    
     /**
     * Permite asignar/quitar el rol de asesor de estadía del área
     * @param dtoRolEstadia
     */
    public void asignarAsesorEstadia(DtoRolEstadia dtoRolEstadia){
        ResultadoEJB<AsesorAcademicoEstadia> resAsignar = ejb.asignarAsesorEstadia(dtoRolEstadia, rol.getAreaSuperior(), rol.getEventoActivo());
        mostrarMensajeResultadoEJB(resAsignar);
        listaPersonalRolesEstadiaAsignados();
        Ajax.update("frm");
    }
}
