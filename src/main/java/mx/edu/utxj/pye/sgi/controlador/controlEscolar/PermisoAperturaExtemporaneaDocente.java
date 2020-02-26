/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PermisoAperturaExtemporaneaRolAdministrador;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPermisoCapturaExtemporanea;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.JustificacionPermisosExtemporaneos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaGrupal;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAperturaExtPorEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaEstudiante;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class PermisoAperturaExtemporaneaDocente extends ViewScopedRol implements Desarrollable{
    @Getter @Setter PermisoAperturaExtemporaneaRolAdministrador rol;

    @EJB EjbPermisoAperturaExtemporanea ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.PERMISO_APERTURA_EXTEMPORANEA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDocente(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo administrador = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new PermisoAperturaExtemporaneaRolAdministrador(filtro, administrador);
            tieneAcceso = rol.tieneAcceso(administrador);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setAdministrador(administrador);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
                   
            rol.getInstrucciones().add("1. Seleccionar Periodo Escolar Activo, en caso de seleccionar un periodo anterior no podrá registrar la solicitud.");
            rol.getInstrucciones().add("2. Seleccionar Materia - Grupo - Programa Educativo.");
            rol.getInstrucciones().add("3. Seleccionar Tipo de Evaluación: Ordinaria o Nivelación Final.");
            rol.getInstrucciones().add("4. En caso de que haya seleccionado ORDINARIA en tipo de evaluación, DEBERÁ seleccionar la unidad correspondiente, en caso contrario NO es necesario seleccionar UNIDAD");
            rol.getInstrucciones().add("5. Ingresar fecha de inicio y fin en la que estará habilitada la captura extemporánea.");
            rol.getInstrucciones().add("6. Seleccionar la justificación por la cual el docente solicitó el permiso.");
            rol.getInstrucciones().add("7. SOLICITUD APERTURA EXTEMPORÁNEA GRUPAL:");
            rol.getInstrucciones().add("7.1 Una vez que haya ingresado la información, puede proceder a GUARDAR la solicitud.");
            rol.getInstrucciones().add("7.2 En la tabla inferior podrá VISUALIZAR los permisos de captura extemporánea vigentes y su situación actual (validado o sin validar).");
            rol.getInstrucciones().add("7.3 En caso de existir un error puede ELIMINAR el permiso de captura, al dar clic en el botón ubicado en la columna opciones de la tabla.");
            rol.getInstrucciones().add("8. SOLICITUD APERTURA EXTEMPORÁNEA POR ESTUDIANTE:");
            rol.getInstrucciones().add("8.1 En la parte inferior podrá visualizar una tabla con el listado de estudiantes que integran el grupo seleccionado");
            rol.getInstrucciones().add("8.2 Para solicitar apertura extemporánea deberá dar clic en el botón localizado en la columna SELECCIONAR que corresponda a la fila del estudiante correspondiente.");
            rol.getInstrucciones().add("8.3 Una vez que se haya registrado correctamente, podrá visualizar en la columna 'Apertura Solicitada' la información del permiso solicitado y en la columna 'Validada' la situación actual del permiso (validado o sin validar).");
           
            buscarCargaAcademica();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "permiso apertura extemporanea";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
     * @param e Evento del cambio de valor
     */
    public void buscarCargaAcademica(){
            rol.setDocente(rol.getAdministrador());
            final ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorDocente(rol.getDocente());
            if(!res.getCorrecto()) {
                mostrarMensajeResultadoEJB(res);
                return;
            }
            rol.setCargasDocente(res.getValor());
            existenPermisosCapturasVigentes(rol.getAdministrador());
            periodosCargas();
    }
    
    /**
     * Permite obtener la lista de periodo escolares en los que el docente tiene carga académica
     */
    public void periodosCargas(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.getPeriodosCargas(rol.getCargasDocente());
        if(res.getCorrecto()){
            rol.setPeriodos(res.getValor());
            rol.setPeriodo(rol.getPeriodos().get(0));
            cargasAcademicas();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de cargas académicas del docente y el periodo seleccionado previamente
     */
    public void cargasAcademicas(){
        if(rol.getPeriodo() == null) return;
        if(rol.getDocente()== null) return;
        ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorPeriodo(rol.getDocente(), rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setCargasPeriodo(res.getValor());
            rol.setCarga(rol.getCargasPeriodo().get(0));
            tiposEvaluaciones();
            actualizarUnidadesMateria();
            justificacionesPermiso();
            actualizarListaEstudiantes();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de tipos de evaluaciones disponibles para realizar permiso
     */
    public void tiposEvaluaciones(){
        if(rol.getCarga()== null) return;
        ResultadoEJB<List<String>> res = ejb.getTiposEvaluaciones();
        if(res.getCorrecto()){
            rol.setTiposEvaluacion(res.getValor());
            rol.setTipoEvaluacion(rol.getTiposEvaluacion().get(0));
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite actualizar las unidades materia dependiendo de la carga académica seleccionada
     */
    public void actualizarUnidadesMateria(){
        ResultadoEJB<List<UnidadMateria>> res = ejb.getUnidadesMateria(rol.getCarga());
        if(res.getCorrecto()){
            rol.setUnidadesMateria(res.getValor());
            rol.setUnidadMateria(rol.getUnidadesMateria().get(0));
            rangoFechasPermiso();
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite actualizar las unidades materia dependiendo de la carga académica seleccionada
     */
    public void actualizarListaEstudiantes(){
        ResultadoEJB<List<DtoAperturaExtPorEstudiante>> res = ejb.getListaEstudiantes(rol.getCarga(),rol.getUnidadMateria(), rol.getTipoEvaluacion());
        if(res.getCorrecto()){
            rol.setListaEstudiantes(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite que al cambiar el periodo escolar se actualicen las cargas académicas asignadas en ese periodo
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
       rol.setPeriodo((PeriodosEscolares)e.getNewValue());
       cargasAcademicas();
       Ajax.update("frm");
    }
    
    /**
     * Permite que al cambiar el tipo de evaluación se pueda habilitar o deshabilitar la opción de unidad de la materia
     * @param e Evento del cambio de valor
     */
    public void cambiarCargaAcademica(ValueChangeEvent e){
       rol.setCarga((DtoCargaAcademica)e.getNewValue());
       actualizarUnidadesMateria();
       actualizarListaEstudiantes();
       Ajax.update("frm");
    }
    
     /**
     * Permite que al cambiar el tipo de evaluación se pueda habilitar o deshabilitar la opción de unidad de la materia
     * @param e Evento del cambio de valor
     */
    public void cambiarTipoEvaluacion(ValueChangeEvent e){
       rol.setTipoEvaluacion((String)e.getNewValue());
       rangoFechasPermiso();
       actualizarListaEstudiantes();
       Ajax.update("frm");
    }
    
      /**
     * Permite que al cambiar la unidad se pueda obtener el rango de fechas disponibles para realizar permiso
     * @param e Evento del cambio de valor
     */
    public void cambiarUnidad(ValueChangeEvent e){
       rol.setUnidadMateria((UnidadMateria)e.getNewValue());
       rangoFechasPermiso();
       actualizarListaEstudiantes();
       Ajax.update("frm");
    }
    
     /**
     * Permite obtener el rango de fechas disponibles para realizar permiso, dependiendo de la fecha de fin de la unidad seleccionada y de la fecha fin del 
     * periodo escolar
     */
    public void rangoFechasPermiso(){
        if("Ordinaria".equals(rol.getTipoEvaluacion()))
        {
            ResultadoEJB<DtoRangoFechasPermiso> res = ejb.getRangoFechasPermisoOrdinarias(rol.getCarga(), rol.getUnidadMateria());
            if(res.getCorrecto()){
                rol.setDtoRangoFechasPermiso(res.getValor());
                rol.setRangoFechaInicial(rol.getDtoRangoFechasPermiso().getRangoFechaInicial());
                rol.setRangoFechaFinal(rol.getDtoRangoFechasPermiso().getRangoFechaFinal());
                Ajax.update("frm");
            }else mostrarMensajeResultadoEJB(res);
        }
        else{
            ResultadoEJB<DtoRangoFechasPermiso> res = ejb.getRangoFechasPermisoNivFinal(rol.getCarga());
            if(res.getCorrecto()){
                rol.setDtoRangoFechasPermiso(res.getValor());
                rol.setRangoFechaInicial(rol.getDtoRangoFechasPermiso().getRangoFechaInicial());
                rol.setRangoFechaFinal(rol.getDtoRangoFechasPermiso().getRangoFechaFinal());
                Ajax.update("frm");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea ordinaria, para ello el usuario debió haber llenado todos los datos correspondientes y haber seleccionado
     * en tipo de evaluacion "Ordinaria"
     */
    public void guardarPermisoCapturaOrdinaria(){
        if(rol.getCarga().getPeriodo().getPeriodo().equals(rol.getPeriodoActivo())){
        ResultadoEJB<PermisosCapturaExtemporaneaGrupal> res = ejb.guardarPermisoCapturaOrdinariaDocente(rol.getCarga(), rol.getUnidadMateria(), rol.getTipoEvaluacion(), rol.getFechaInicio(), rol.getFechaFin(), rol.getJustificacionPermisosExtemporaneos(), rol.getAdministrador());
        if(res.getCorrecto()){
            rol.setPermisosCapturaExtemporaneaGrupal(res.getValor());
            mostrarMensajeResultadoEJB(res);
            existenPermisosCapturasVigentes(rol.getDocente());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
        }else Messages.addGlobalWarn("No puede solicitar permisos de periodos anteriores");
    }
    
    /**
     * Permite guardar el permiso de captura extemporánea de nivelación final, para ello el usuario debió haber llenado todos los datos correspondientes y haber 
     * seleccionado en tipo de evaluacion "Nivelación Final"
     */
    public void guardarPermisoCapturaNivFinal(){
        if(rol.getCarga().getPeriodo().getPeriodo().equals(rol.getPeriodoActivo())){
        ResultadoEJB<PermisosCapturaExtemporaneaGrupal> res = ejb.guardarPermisoCapturaNivFinalDocente(rol.getCarga(), rol.getTipoEvaluacion(), rol.getFechaInicio(), rol.getFechaFin(), rol.getJustificacionPermisosExtemporaneos(), rol.getAdministrador());
        if(res.getCorrecto()){
            rol.setPermisosCapturaExtemporaneaGrupal(res.getValor());
            mostrarMensajeResultadoEJB(res);
            existenPermisosCapturasVigentes(rol.getDocente());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
        }else Messages.addGlobalWarn("No puede solicitar permisos de periodos anteriores");
    }
    
    /**
     * Permite obtener la lista de justificación disponibles para solicitar permiso de captura extemporánea
     */
    public void justificacionesPermiso(){
        if(rol.getCarga()== null) return;
        ResultadoEJB<List<JustificacionPermisosExtemporaneos>> res = ejb.getJustificacionesPermisoCaptura();
        if(res.getCorrecto()){
            rol.setListaJustificaciones(res.getValor());
            rol.setJustificacionPermisosExtemporaneos(rol.getListaJustificaciones().get(0));
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite cambiar la justificación del permiso
     * @param e Evento del cambio de valor
     */
    public void cambiarJustificacion(ValueChangeEvent e){
       rol.setJustificacionPermisosExtemporaneos((JustificacionPermisosExtemporaneos)e.getNewValue());
    }
    
    
     /**
     * Permite verificar si existen permisos de captura extemporánea vigentes del docente seleccionado
     * @param docente Docente del que se obtendrá la información
     */
    public void existenPermisosCapturasVigentes(PersonalActivo docente){
        Date fechaActual = new Date();
        ResultadoEJB<List<DtoPermisoCapturaExtemporanea>> res = ejb.buscarPermisosCapturaVigentes(docente, fechaActual);
        if(res.getCorrecto()){
            rol.setListaPermisosCapturasExtemporaneas(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite eliminar un permiso de captura extemporánea vigente
     * @param clavePermiso Clave del permiso que se desea eliminar
     */
    public void eliminarPermiso(Integer clavePermiso){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarPermisoCaptura(clavePermiso);
        mostrarMensajeResultadoEJB(resEliminar);
        existenPermisosCapturasVigentes(rol.getDocente());
        Ajax.update("frm");
    }
    
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoPermisoCapturaExtemporanea permisoNew = (DtoPermisoCapturaExtemporanea) dataTable.getRowData();
        ejb.actualizarPermisoCaptura(permisoNew);
    }
    
     public void aperturaExtEst(DtoAperturaExtPorEstudiante estudiante) {
         rol.setEstudiante(estudiante);
         if(rol.getCarga().getPeriodo().getPeriodo().equals(rol.getPeriodoActivo())){
            if(rol.getEstudiante().getPermisosCapturaExtemporaneaEstudiante() == null){
                ResultadoEJB<PermisosCapturaExtemporaneaEstudiante> resGuardar = ejb.guardarPermisoCapturaOrdinariaEstudianteDocente(rol.getCarga(), rol.getEstudiante().getEstudiante(), rol.getUnidadMateria(), rol.getTipoEvaluacion(), rol.getFechaInicio(), rol.getFechaFin(), rol.getJustificacionPermisosExtemporaneos(), rol.getAdministrador());
                if (resGuardar.getCorrecto()) {
                    actualizarListaEstudiantes();
                    rol.setFechaInicio(null);
                    rol.setFechaFin(null);
                    Ajax.update("frm");
                } mostrarMensajeResultadoEJB(resGuardar);
            }else{
                ResultadoEJB<Integer> resEliminar = ejb.eliminarPermisoCapturaEstudiante(estudiante.getPermisosCapturaExtemporaneaEstudiante().getPermisoEstudiante());
                if (resEliminar.getCorrecto()) {
                    actualizarListaEstudiantes();
                    rol.setFechaInicio(null);
                    rol.setFechaFin(null);
                    Ajax.update("frm");
                } mostrarMensajeResultadoEJB(resEliminar);
            }
         }else Messages.addGlobalWarn("No puede solicitar permisos de periodos anteriores");
    }
//     
//       /**
//     * Permite verificar si existe permiso activo para el estudiante
//     * @param estudiante Estudiante
//     * @return valor boolean según sea el caso
//     */
//    public PermisosCapturaExtemporaneaEstudiante consultarPermisoActivo(Estudiante estudiante){
//        rol.setPermisoActivo(ejb.buscarPermisoActivo(estudiante, rol.getCarga(), rol.getUnidadMateria(), rol.getTipoEvaluacion()));
//        System.err.println("consultarPermisoActivo " + ejb.buscarPermisoActivo(estudiante, rol.getCarga(), rol.getUnidadMateria(), rol.getTipoEvaluacion()));
//        return rol.getPermisoActivo();
//    }
}
