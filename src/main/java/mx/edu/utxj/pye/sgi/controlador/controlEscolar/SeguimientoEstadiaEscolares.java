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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEstadiaRolVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.stream.Collectors;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoEstadiaEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoEstadiaRolVinculacion rol;
    
    @EJB EjbSeguimientoEstadia ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbEstadiasServiciosEscolares ejbEstadiasServiciosEscolares;
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
@Getter private SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;

@PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_ESTADIA_ESCOLARES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbEstadiasServiciosEscolares.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo coordinadorEstadia = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoEstadiaRolVinculacion(filtro, coordinadorEstadia);
            tieneAcceso = rol.tieneAcceso(coordinadorEstadia);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setCoordinadorEstadia(coordinadorEstadia);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Seleccione programa educativo");
            rol.getInstrucciones().add("En la tabla podrá visualizar la información de estadía de los estudiantes.");
            rol.getInstrucciones().add("Los datos indicados con * son aquellos en los que interviene.");
            rol.getInstrucciones().add("Dar clic en botón que se habilita para validar o invalidar los documentos.");
            rol.getInstrucciones().add("Dar clic en el campo del comentario para capturar las observaciones, en caso contrario dejar con el texto predeterminado.");
            
            rol.setMostrarSegValVinc(false);
            rol.setOcultarColumnas(false);
            
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento estudiantes estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesSeguimientoRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de niveles educativos que existen en eventos de estadía registrados dependiendo de la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaProgramasNivelGeneracion();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de programas educativos que existen en eventos de estadía registrados dependiendo de la generación y nivel educativo seleccionado
     */
    public void listaProgramasNivelGeneracion(){
        if(rol.getNivelEducativo()== null) return;
        ResultadoEJB<List<AreasUniversidad>> res = ejbAsignacionRolesEstadia.getProgramasNivelesGeneracionEventosRegistrados(rol.getGeneracion(), rol.getNivelEducativo(), rol.getCoordinadorEstadia().getAreaOperativa().getArea());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            listaEstudiantesSeguimiento();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado
     */
    public void listaEstudiantesSeguimiento(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesSeguimientoCoordinadorEstadias(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            if(!rol.getMostrarSegValVinc()){
                rol.setEstudiantesSeguimiento(res.getValor());
            }else{
                rol.setEstudiantesSeguimiento(res.getValor().stream().filter(p->p.getSeguimientoEstadiaEstudiante().getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==2 || p.getSeguimientoEstadiaEstudiante().getEstudiante().getTipoEstudiante().getIdTipoEstudiante()==3).collect(Collectors.toList()));
            }
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de estudiantes asignados
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
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de programas educativos
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaProgramasNivelGeneracion();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un programa educativo se pueda actualizar la lista de estudiantes asignados
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  AreasUniversidad){
            AreasUniversidad programa = (AreasUniversidad)e.getNewValue();
            rol.setProgramaEducativo(programa);
            listaEstudiantesSeguimiento();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Método que permite desactivar/activar el seguimiento de estadía seleccionado
     * @param dtoSeguimientoEstadia
     */
    public void desactivarSeguimiento(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resDesactivar = ejb.desactivarSeguimientoEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        if (resDesactivar.getCorrecto()) {
            mostrarMensajeResultadoEJB(resDesactivar);
            listaEstudiantesSeguimiento();
            Ajax.update("frm");
        }
    }
    
    /**
     * Método para verificar si el seguimiento está validado por dirección de carrera y vinculación, además de la situación académica del estudiante.
     * @param dtoSeguimientoEstadia
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarDesactivarSeguimiento(@NonNull DtoSeguimientoEstadia dtoSeguimientoEstadia){
        Boolean permiso = Boolean.TRUE;
        
        if(!dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getValidacionDirector() && !dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getValidacionVinculacion() && (dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 2 || dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEstudiante().getTipoEstudiante().getIdTipoEstudiante() == 3)){
            permiso = Boolean.FALSE;
        }
        return permiso;
    }
    
    /**
     * Método que cambia el tipo de visualización de la información (todos los seguimientos o solo los estudiante que se dieron de baja temporal o definitiva)
     * @param event Evento al tipo de visualización
     */
    public void cambiarMostrarEstBaja(ValueChangeEvent event){
        if(rol.getMostrarSegValVinc()){
            rol.setMostrarSegValVinc(false);
            listaEstudiantesSeguimiento();
        }else{
            rol.setMostrarSegValVinc(true);
            listaEstudiantesSeguimiento();
        }
        Ajax.update("frm");
    }
    
      /**
     * Método que cambia el tipo de visualización de la información (todas las columnas u oculta columnas para visualizar en primera instancia el botón de desactivar/activar seguimiento)
     * @param event Evento al tipo de visualización
     */
    public void cambiarOcultarColumnas(ValueChangeEvent event){
        if(rol.getOcultarColumnas()){
            rol.setOcultarColumnas(false);
        }else{
            rol.setOcultarColumnas(true);
        }
        Ajax.update("frm");
    }
   
}
