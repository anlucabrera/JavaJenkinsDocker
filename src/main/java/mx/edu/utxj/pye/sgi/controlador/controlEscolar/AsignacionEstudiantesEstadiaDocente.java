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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



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
            rol.setDeshabilitarAsignacion(Boolean.FALSE);
//            rol.setSoloLectura(true);
            rol.setEventoActivo(ejb.buscarEventoActivoAsigEstudiantes().getValor());
            rol.setGeneracionEventoActivo(ejb.buscarGeneracionEventoActivo(rol.getEventoActivo()).getValor());
            
            rol.getInstrucciones().add("Seleccione periodo escolar para consultar bajas registradas durante ese periodo.");
            rol.getInstrucciones().add("Seleccione programa educativo.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Validar o Invalidar baja, Consultar materias reprobadas, Generar formato de baja y Eliminar el registro.");
            rol.getInstrucciones().add("Dar clic en el botón de Validar/Invalidar baja, para que se cambie la situación académica en sistema.");
            rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
            rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
            rol.getInstrucciones().add("Dar clic en el botón Eliminar baja, para eliminar el registro en caso de que se haya equivocado al realizar el trámite.");
           
            rol.setAreaSuperior(rol.getDocente().getAreaSuperior());
            rol.setProgramasEducativos(ejb.getProgramasEducativosArea(rol.getAreaSuperior()).getValor());
            
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
            listaEstudiantesEstadiaAsignados();
        }else mostrarMensajeResultadoEJB(res);
    
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
        ResultadoEJB<List<DtoDatosEstudiante>> res = ejb.getListaEstudiantesEstadiaAsignados(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDocente().getPersonal(), rol.getEventoSeleccionado());
        if(res.getCorrecto()){
            rol.setEstudiantesRegistrados(res.getValor());
            if(rol.getEventoActivo() == null){
                rol.setDeshabilitarAsignacion(Boolean.TRUE);
            }else{
                if(rol.getEventoSeleccionado() == rol.getEventoActivo()){
                    rol.setDeshabilitarAsignacion(Boolean.FALSE);
                }
            }
            Ajax.update("tbListaRolesEstadia");
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
            listaEstudiantesEstadiaAsignados();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite asignar un estudiante al seguimiento de estadía del asesor académico
     */
    public void asignarEstudiante(){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resAsignar = ejb.asignarEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDocente().getPersonal(), rol.getEventoActivo(), rol.getEstudianteRegistrado());
        mostrarMensajeResultadoEJB(resAsignar);
        crearRegistroAsesorEmpresarial(resAsignar.getValor());
        listaEstudiantesEstadiaAsignados();
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
