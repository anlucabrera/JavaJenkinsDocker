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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionEstudiantesEstadiaRolDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
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
import java.util.Collections;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AsignacionEstudiantesEstadiaDocente extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AsignacionEstudiantesEstadiaRolDocente rol;
    
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
            setVistaControlador(ControlEscolarVistaControlador.ASIGNACION_ESTUDIANTES_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocenteAsesorEstadia(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo asesorAcademico = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AsignacionEstudiantesEstadiaRolDocente(filtro, asesorAcademico);
            tieneAcceso = rol.tieneAcceso(asesorAcademico);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDocente(asesorAcademico);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setDeshabilitarAsignacion(Boolean.TRUE);
            rol.setRolAsesorActivo(Boolean.FALSE);
//            rol.setSoloLectura(true);
            rol.setEventoActivo(ejb.buscarEventoActivoAsigEstudiantes().getValor());
            rol.setGeneracionEventoActivo(ejb.buscarGeneracionEventoActivo(rol.getEventoActivo()).getValor());
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("A continuación busque el estudiante por matricula o nombre.");
            rol.getInstrucciones().add("Seleccione el registro del estudiante que corresponda.");
            rol.getInstrucciones().add("A continuación de clic en el icono ASIGNAR, el botón estará desactivado cuando el periodo de asignación haya vencido o aún no este activo");
            rol.getInstrucciones().add("En la tabla podrá consultar los estudiantes que tiene asignados.");
            rol.getInstrucciones().add("En caso de error, puede dar clic en el icono ELIMINAR, para quitar la asignación correspondiente.");
           
            rol.setAreaSuperior(rol.getDocente().getAreaSuperior());
            
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "asignacion estudiantes estadia";
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
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            rol.setEventoSeleccionado(ejb.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Asignacion estudiantes").getValor());
            rol.setProgramasEducativos(ejb.getProgramasEducativosArea(rol.getAreaSuperior(), rol.getNivelEducativo()).getValor());
            listaEstudiantesEstadiaAsignados();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite inicializar los valores de la lista de estudiantes asignados y el de deshabilitar la asignación
     */
    public void inicializarValores(){
        rol.setEstudiantesRegistrados(Collections.EMPTY_LIST);
        rol.setDeshabilitarAsignacion(Boolean.TRUE);
        rol.setRolAsesorActivo(Boolean.FALSE);
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(rol.getGeneracion(), rol.getProgramasEducativos(),pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un estudiante se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarEstudiante(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete)e.getNewValue();
            rol.setEstudianteSeleccionado(estudiante);
            buscarDatosEstudiante(rol.getEstudianteSeleccionado().getEstudiantes().getIdEstudiante());
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
    
    public void buscarDatosEstudiante(Integer claveEstudiante){
        ResultadoEJB<DtoDatosEstudiante> res = ejb.buscarDatosEstudiante(claveEstudiante);
        if(res.getCorrecto()){
        rol.setEstudianteRegistrado(res.getValor());
        Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void listaEstudiantesEstadiaAsignados(){
        inicializarValores();
        comprobarRolAsesorEvento();
        comprobarEventos();
        ResultadoEJB<List<DtoDatosEstudiante>> res = ejb.getListaEstudiantesEstadiaAsignados(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDocente().getPersonal(), rol.getEventoSeleccionado());
        if(res.getCorrecto()){
            if(!res.getValor().isEmpty()){
                rol.setEstudiantesRegistrados(res.getValor());
            }
        }else mostrarMensajeResultadoEJB(res);
        Ajax.update("frm");
        Ajax.update("tbListaRolesEstadia");
    }
    
    /**
     * Permite verificar si se habilita el botón de asignar estudiante
     */
    public void comprobarEventos(){
        if(rol.getEventoActivo() != null){
            if (rol.getEventoSeleccionado().getEvento() == rol.getEventoActivo().getEvento() && rol.getRolAsesorActivo()) {
                rol.setDeshabilitarAsignacion(Boolean.FALSE);
            } else {
                rol.setDeshabilitarAsignacion(Boolean.TRUE);
            }
        }else{
            rol.setDeshabilitarAsignacion(Boolean.TRUE);
        }
        Ajax.update("frm");
    }
    
    /**
     * Permite comprobar si el personal docente tiene rol de asesor académico para el evento seleccionado (generación y nivel educativo)
     */
    public void comprobarRolAsesorEvento(){
        ResultadoEJB<EventoEstadia> resEvento = ejb.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Asignacion coordinador asesor estadia");
        if(resEvento.getCorrecto() && resEvento.getValor() != null){
            ResultadoEJB<AsesorAcademicoEstadia> resAsesor = ejb.buscarAsesorAcademico(rol.getDocente().getPersonal(), resEvento.getValor());
            if(resAsesor.getCorrecto() && resAsesor.getValor() != null){
                rol.setRolAsesorActivo(Boolean.TRUE);
            }else{
                mostrarMensajeResultadoEJB(resAsesor);
            } 
        }else mostrarMensajeResultadoEJB(resEvento);
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
            listaEstudiantesEstadiaAsignados();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite asignar un estudiante al seguimiento de estadía del asesor académico
     */
    public void asignarEstudiante(){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resAsignar = ejb.asignarEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDocente().getPersonal(), rol.getEventoActivo(), rol.getEstudianteRegistrado());
        Messages.addGlobalWarn(resAsignar.getMensaje());
        crearRegistroAsesorEmpresarial(resAsignar.getValor());
        listaEstudiantesEstadiaAsignados();
        rol.setEstudianteRegistrado(null);
        Ajax.update("frm");
    }
    
     /**
     * Permite crear registro de asesor empresarial para el seguimiento de estadía
     * @param seguimientoEstadiaEstudiante
     */
    public void crearRegistroAsesorEmpresarial(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        ResultadoEJB<AsesorEmpresarialEstadia> resCrear = ejb.crearRegistroAsesorEmpresarial(seguimientoEstadiaEstudiante);
        mostrarMensajeResultadoEJB(resCrear);
        listaEstudiantesEstadiaAsignados();
        Ajax.update("frm");
    }
    
     /**
     * Permite eliminar asignación del estudiante
     * @param dtoDatosEstudiante
     */
    public void eliminarAsignacion(DtoDatosEstudiante dtoDatosEstudiante){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarAsignacion(rol.getEventoSeleccionado(), dtoDatosEstudiante);
        mostrarMensajeResultadoEJB(resEliminar);
        listaEstudiantesEstadiaAsignados();
        Ajax.update("frm");
    }
    
}
