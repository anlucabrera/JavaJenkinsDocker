/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
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
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEstadiaRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoEstadiaPorEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private SeguimientoEstadiaRolEstudiante rol = new SeguimientoEstadiaRolEstudiante();
    
    @EJB EjbSeguimientoEstadia ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
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
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                cargado = true;
                setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_ESTADIA_ESTUDIANTE);
                ResultadoEJB<DtoEstudiante> resAcceso = ejb.validarEstudianteAsignado(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<DtoEstudiante> resValidacion = ejb.validarEstudianteAsignado(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                
                DtoEstudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante, UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setDtoEstudiante(estudiante);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);

                rol.getInstrucciones().add("Seleccione periodo escolar para consultar bajas registradas durante ese periodo.");
                rol.getInstrucciones().add("Seleccione programa educativo.");
                rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Validar o Invalidar baja, Consultar materias reprobadas, Generar formato de baja y Eliminar el registro.");
                rol.getInstrucciones().add("Dar clic en el botón de Validar/Invalidar baja, para que se cambie la situación académica en sistema.");
                rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
                rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
                rol.getInstrucciones().add("Dar clic en el botón Eliminar baja, para eliminar el registro en caso de que se haya equivocado al realizar el trámite.");

                generacionesSeguimientosRegistrados();

            }else{
                return;
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento estudiante estadia individual";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesSeguimientosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesSeguimientoEstudiante(rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            seguimientoEstudiante();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void seguimientoEstudiante(){
        ResultadoEJB<DtoSeguimientoEstadiaEstudiante> res = ejb.getSeguimientoEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
        if(res.getCorrecto()){
            rol.setDtoSeguimientoEstadiaEstudiante(res.getValor());
            Ajax.update("tbSeguimientoEstadia");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se pueda actualizar la lista de bajas del periodo
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
     * Permite que al cambiar o seleccionar un programa educativo se pueda actualizar la lista de bajas por programa educativo
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            seguimientoEstudiante();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    public Boolean deshabilitarCarga(@NonNull DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<EventoEstadia> res = ejb.buscarEventoActivoDocumento(rol.getDtoSeguimientoEstadiaEstudiante(),dtoDocumentoEstadiaEstudiante, "Estudiante");
        if(res.getCorrecto() && res.getValor() != null){
            permiso= Boolean.TRUE;
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    public EventoEstadia eventoActivoDocumento(@NonNull DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante){
        EventoEstadia eventoEstadia =  new EventoEstadia();
        ResultadoEJB<EventoEstadia> res = ejb.buscarEventoActivoDocumento(rol.getDtoSeguimientoEstadiaEstudiante(),dtoDocumentoEstadiaEstudiante, "Estudiante");
        if(res.getCorrecto()){
            eventoEstadia=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return eventoEstadia;
    }
    
    public EventoEstadia eventoDocumento(@NonNull DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante){
        EventoEstadia eventoEstadia =  new EventoEstadia();
        ResultadoEJB<EventoEstadia> res = ejb.buscarEventoDocumento(rol.getDtoSeguimientoEstadiaEstudiante(),dtoDocumentoEstadiaEstudiante);
        if(res.getCorrecto()){
            eventoEstadia=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return eventoEstadia;
    }
    
  
}
