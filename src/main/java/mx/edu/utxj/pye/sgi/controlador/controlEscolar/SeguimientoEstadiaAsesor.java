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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEstadiaRolAsesor;
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
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEmpresaComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEvaluacionEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoEstadiaAsesor extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoEstadiaRolAsesor rol;
    
    @EJB EjbSeguimientoEstadia ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject GenerarCedEvalAcredEstadia generarCedEvalAcredEstadia;

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
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_ESTADIA_ASESOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbAsignacionRolesEstadia.validarDocenteAsesorEstadia(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo asesorAcademico = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoEstadiaRolAsesor(filtro, asesorAcademico);
            tieneAcceso = rol.tieneAcceso(asesorAcademico);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDocente(asesorAcademico);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setAperturaDialogoRegistroInfEst(Boolean.FALSE);
            rol.setAperturaDialogoEdicionInfEst(Boolean.FALSE);
            rol.setAperturaDialogoEvalEst(Boolean.FALSE);
            rol.setEditarEmpresa(Boolean.FALSE);
            rol.setHabilitarGuardar(Boolean.FALSE);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("En la tabla podrá visualizar la información de estadía de los estudiantes que tiene asignados.");
            rol.getInstrucciones().add("Los datos indicados con * son aquellos en los que interviene.");
            rol.getInstrucciones().add("Dar clic en botón que se habilita para validar, si se encuentra desactivado es porque el periodo venció o aún no se encuentra activo.");
            rol.getInstrucciones().add("Dar clic en el campo del comentario para capturar, si no puede editar el comentario es porque el periodo vención o aún no se encuentra activo.");
            rol.getInstrucciones().add("El botón para generar la cédula evaluación y acreditación de estadía se habilita cuando el estudiante acreditó sus evaluaciones, de lo contrario no podrá generar el formato.");
            
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
            listaEstudiantesSeguimiento();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void listaEstudiantesSeguimiento(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesSeguimiento(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDocente().getPersonal());
        if(res.getCorrecto()){
            rol.setEstudiantesSeguimiento(res.getValor());
            Ajax.update("tbListaSeguimientoEstadia");
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
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de estudiantes asignados
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaEstudiantesSeguimiento();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Método para verificar si existe evento de estadía activo para la actividad seleccionada
     * @param dtoSeguimientoEstadia
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarCaptura(@NonNull DtoSeguimientoEstadia dtoSeguimientoEstadia){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Registro empresa y proyecto","Asesor academico");
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
   
     /**
     * Método para proporcionar lista de empresas sugeridad en un autocomplete donde se puede ingresar el nombre de la empresa, localidad, municipio y ciudad donde se localiza para realizar la búsqueda
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEmpresaComplete> completeEmpresa(String pista){
        ResultadoEJB<List<DtoEmpresaComplete>> res = ejb.buscarEmpresa(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar una empresa se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarEmpresa(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoEmpresaComplete){
            DtoEmpresaComplete empresa = (DtoEmpresaComplete)e.getNewValue();
            rol.setEmpresaSeleccionada(empresa);
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
    /**
     * Método que proporciona identificar el seguimiento de estadía que se va a registrar
     * @param dtoSeguimientoEstadia
     */
    public void registrarInformacionEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia){
            rol.setEstudianteSeguimiento(dtoSeguimientoEstadia);
            if(rol.getEstudianteSeguimiento().getSemanasEstadia() == null){
                rol.setSemanasEstadia(0.0);
                rol.setHabilitarGuardar(Boolean.FALSE);
            }else{
                rol.setSemanasEstadia(rol.getEstudianteSeguimiento().getSemanasEstadia());
                if (rol.getSemanasEstadia() >= 13.0) {
                    rol.setHabilitarGuardar(Boolean.TRUE);
                } else {
                    rol.setHabilitarGuardar(Boolean.FALSE);
                }
            }
            Ajax.update("frmModalRegistroInfEst");
            Ajax.oncomplete("skin();");
            rol.setAperturaDialogoRegistroInfEst(Boolean.TRUE);
            buscarAsesorEmpresarial();
            forzarAperturaRegistroDialogo();
    }
    
    /**
     * Método que proporciona identificar el registro de seguimiento de estadía que se va a modificar
     * @param dtoSeguimientoEstadia
     */
    public void editarSeguimiento(DtoSeguimientoEstadia dtoSeguimientoEstadia){
            rol.setEstudianteSeguimiento(dtoSeguimientoEstadia);
            rol.setSemanasEstadia(rol.getEstudianteSeguimiento().getSemanasEstadia());
            if(rol.getSemanasEstadia()>=13.0){
                rol.setHabilitarGuardar(Boolean.TRUE);
            }else{
                 rol.setHabilitarGuardar(Boolean.FALSE);
            }
            Ajax.update("frmModalEdicionInfEst");
            Ajax.oncomplete("skin();");
            rol.setAperturaDialogoEdicionInfEst(Boolean.TRUE);
            buscarAsesorEmpresarial();
            forzarAperturaEdicionDialogo();
    }
    
    /**
     * Método que permite cerrar la ventana en donde se registrará la información de estadía
     */
     public void forzarAperturaRegistroDialogo(){
        if(rol.getAperturaDialogoRegistroInfEst()){
           Ajax.oncomplete("PF('modalRegistroInfEst').show();");
           rol.setAperturaDialogoRegistroInfEst(Boolean.FALSE);
        }
    }
    
    /**
     * Método que permite cerrar la ventana en donde se modificará la información de estadía
     */
     public void forzarAperturaEdicionDialogo(){
        if(rol.getAperturaDialogoEdicionInfEst()){
           Ajax.oncomplete("PF('modalEdicionInfEst').show();");
           rol.setAperturaDialogoEdicionInfEst(Boolean.FALSE);
        }
    }
     
     /**
     * Permite guardar la información de estadía
     */
    public void guardarInformacionEstadia(){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resRegistrar = ejb.guardarInformacionEstadia(rol.getEmpresaSeleccionada(), rol.getEstudianteSeguimiento(), rol.getAsesorEmpresarialEstadia());
        mostrarMensajeResultadoEJB(resRegistrar);
        listaEstudiantesSeguimiento();
        rol.setEmpresaSeleccionada(null);
        Ajax.update("tbListaSeguimientoEstadia");
        Ajax.update("frm");
    }
     
     /**
     * Permite actualizar la información de estadía
     */
    public void actualizarInformacionEstadia(){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resRegistrar = ejb.actualizarInformacionEstadia(rol.getEditarEmpresa(), rol.getEmpresaSeleccionada(), rol.getEstudianteSeguimiento(), rol.getAsesorEmpresarialEstadia());
        mostrarMensajeResultadoEJB(resRegistrar);
        listaEstudiantesSeguimiento();
        rol.setEmpresaSeleccionada(null);
        rol.setEditarEmpresa(Boolean.FALSE);
        Ajax.update("tbListaSeguimientoEstadia");
        Ajax.update("frm");
    }
    
     /**
     * Método para habilitar el autocomplete de la empresa
     * @param e Evento del cambio de valor
     */
    public void cambiarEdicionEmpresa(ValueChangeEvent e){
        if(rol.getEditarEmpresa()){
            rol.setEditarEmpresa(Boolean.FALSE);
            Ajax.update("frmModalEdicionInfEst");
        }else{
            rol.setEditarEmpresa(Boolean.TRUE);
            Ajax.update("frmModalEdicionInfEst");
        }
    }

    /**
     * Método que permite cancular el número de semanas entre la fecha de inicio y fin de la estadía
     * @param event Evento al seleccionar una fecha de inicio y/o fin
     */
    public void calcularSemanasEstadia(SelectEvent event){
        if(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getFechaInicio() != null && rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getFechaFin() != null){
        ResultadoEJB<Double> resCalcular = ejb.calcularSemanasEstadia(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getFechaInicio(), rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getFechaFin());
        mostrarMensajeResultadoEJB(resCalcular);
        rol.setSemanasEstadia(resCalcular.getValor());
        if(rol.getSemanasEstadia()>=13.0){
            rol.setHabilitarGuardar(Boolean.TRUE);
        }else{
            rol.setHabilitarGuardar(Boolean.FALSE);
        }
            Ajax.update("frmModalRegistroInfEst");
            Ajax.update("frmModalEdicionInfEst");
        }
    }
    
     /**
     * Permite obtener el asesor empresaril de estadía del estudiante
     */
    public void buscarAsesorEmpresarial(){
        ResultadoEJB<AsesorEmpresarialEstadia> res = ejb.buscarAsesorEmpresarial(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            rol.setAsesorEmpresarialEstadia(res.getValor());
            Ajax.update("frmModalRegistroInfEst");
            Ajax.update("frmModalEdicionInfEst");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    
     /**
     * Método que permite consultar si el documento ha sido registrado en la base de datos
     * @param claveDocumento 
     * @param dtoSeguimientoEstadia 
     * @return  Verdadero o Falso según sea el caso
     */
    public Boolean consultarExisteDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.consultarClaveDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
     /**
     * Método que permite buscar los comentarios realizados al documento seleccionado
     * @param claveDocumento 
     * @param dtoSeguimientoEstadia 
     * @return Comentario del documento
     */
    public DocumentoSeguimientoEstadia comentariosDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.consultarComentarioDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
    /**
     * Método que busca la ruta del documento seleccionado para su descarga
     * @param claveDocumento
     * @param dtoSeguimientoEstadia
     * @throws java.io.IOException
     */
    public void descargarDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia) throws IOException{
        ResultadoEJB<DocumentoSeguimientoEstadia> resBuscarDoc = ejb.buscarDocumentoEstudiante(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), claveDocumento);
        File f = new File(resBuscarDoc.getValor().getRuta());
        Faces.sendFile(f, false);
    }
    
//    public DocumentoSeguimientoEstadia buscarDocumentoEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia, Integer claveDocumento){
//        return ejb.buscarDocumentoEstudiante(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), claveDocumento).getValor();
//    }
    public Boolean buscarValidacionDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.buscarValidacionDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
     /**
     * Método que permite validar o invalidar el documento del estudiante seleccionado
     * @param claveDocumento
     * @param dtoSeguimientoEstadia
     */
    public void validarDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        ResultadoEJB<DocumentoSeguimientoEstadia> resValidar = ejb.validarDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), rol.getDocente().getPersonal());
        mostrarMensajeResultadoEJB(resValidar);
        listaEstudiantesSeguimiento();
        Ajax.update("frm");
    }
    
     /**
     * Método para verificar si existe evento de estadía activo para la actividad de evaluación/validación seleccionada
     * @param dtoSeguimientoEstadia
     * @param valor
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarValidacion(@NonNull DtoSeguimientoEstadia dtoSeguimientoEstadia, Integer valor){
        Boolean permiso = Boolean.FALSE;
        
        switch (valor) {
                case 1:
                    ResultadoEJB<Boolean> res1 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion primer informe", "Asesor academico");
                    if (res1.getCorrecto()) {
                        permiso = res1.getValor();
                    } else { mostrarMensajeResultadoEJB(res1);}
                    break;
                case 2:
                    ResultadoEJB<Boolean> res2 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion segundo informe", "Asesor academico");
                    if (res2.getCorrecto()) {
                        permiso = res2.getValor();
                    } else { mostrarMensajeResultadoEJB(res2);}
                    break;
                case 3:
                    ResultadoEJB<Boolean> res3 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion tercer informe", "Asesor academico");
                    if (res3.getCorrecto()) {
                        permiso = res3.getValor();
                    } else { mostrarMensajeResultadoEJB(res3);}
                    break;
                case 4:
                    ResultadoEJB<Boolean> res4 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion informe final", "Asesor academico");
                    if (res4.getCorrecto()) {
                        permiso = res4.getValor();
                    } else { mostrarMensajeResultadoEJB(res4);}
                    break;
                default:

                    break;
            }
        return permiso;
    }
    
    /**
     * Método que permite cancular el número de semanas entre la fecha de inicio y fin de la estadía
     * @param event Evento al seleccionar una fecha de inicio y/o fin
     */
    public void guardarComentarioDocumento(ValueChangeEvent event){
        String comentario = event.getNewValue().toString();
        String claveDocumento = (String) event.getComponent().getAttributes().get("documento");
        DtoSeguimientoEstadia dtoSeguimientoEstadia = (DtoSeguimientoEstadia) event.getComponent().getAttributes().get("seguimientoEstadia");
        Integer documento = Integer.parseInt(claveDocumento);
        ResultadoEJB<DocumentoSeguimientoEstadia> res = ejb.guardarComentarioDocumento(comentario, documento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        Messages.addGlobalInfo("Se ha registrado correctamente el comentario");
        Ajax.update("frm");
        
    }
    
     /**
     * Método que proporciona identificar el registro de seguimiento de estadía al que se evaluaraá
     * @param dtoSeguimientoEstadia
     */
    public void editarEvaluacion(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        rol.setEstudianteSeguimiento(dtoSeguimientoEstadia);
        Ajax.update("frmModalCapturaEvaluacion");
        Ajax.oncomplete("skin();");
        rol.setAperturaDialogoEvalEst(Boolean.TRUE);
        rol.setPromedioAsesorInterno(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getPromedioAsesorInterno());
        rol.setPromedioAsesorExterno(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getPromedioAsesorExterno());
        if(rol.getPromedioAsesorInterno() == 0)
        {
            listaCriteriosEvaluacionEstadia();
            rol.setEvaluacionRegistrada(Boolean.FALSE);
        }else{
            listaCriteriosEvaluacionEstadiaRegistrada();
            rol.setEvaluacionRegistrada(Boolean.TRUE);
        }
        forzarAperturaEvaluacionDialogo();
    }
    
     /**
     * Método que permite cerrar la ventana en donde se modificará la evaluación de estadía
     */
     public void forzarAperturaEvaluacionDialogo(){
        if(rol.getAperturaDialogoEvalEst()){
           Ajax.oncomplete("PF('modalCapturaEvaluacion').show();");
           rol.setAperturaDialogoEvalEst(Boolean.FALSE);
        }
    }
    
     /**
     * Método para verificar si existe evento de estadía activo para el registro de evaluación
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEvaluacion(){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.buscarEventoActivo(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getEvento(), "Registro cedula evaluacion empresarial","Estudiante");
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
     /**
     * Permite obtener la lista de criterios de evaluación de estadía del estudiante
     */
    public void listaCriteriosEvaluacionEstadiaRegistrada(){
        ResultadoEJB<List<CalificacionCriterioEstadia>> res = ejb.getListaEvaluacionEstadiaRegistrada(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            rol.setListaEvaluacionEstadiaRegistrada(res.getValor());
            Ajax.update("frmModalCapturaEvaluacion");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de criterios de evaluación de estadía del estudiante
     */
    public void listaCriteriosEvaluacionEstadia(){
        ResultadoEJB<List<DtoEvaluacionEstadiaEstudiante>> res = ejb.getListaEvaluacionEstadia(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            rol.setListaEvaluacionEstadia(res.getValor());
            Ajax.update("frmModalCapturaEvaluacion");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Método que permite recalcular el promedio de la evaluación de estadía del estudiante
     */
    public void recalcularPromedio(){
       Double promedio = rol.getListaEvaluacionEstadiaRegistrada().stream().mapToDouble(x -> x.getCalificacion()).average().getAsDouble();
       rol.setPromedioAsesorInterno(promedio);
       Ajax.update("frmModalCapturaEvaluacion");
    }
    
     /**
     * Método que permite calcular el promedio de la evaluación de estadía del estudiante
     */
    public void calcularPromedio(){
       Double promedio = rol.getListaEvaluacionEstadia().stream().mapToDouble(x -> x.getCalificacion()).average().getAsDouble();
       rol.setPromedioAsesorInterno(promedio);
       Ajax.update("frmModalCapturaEvaluacion");
    }
    
     /**
     * Permite actualizar la evaluación de estadía del estudiante
     */
    public void actualizarEvaluacionEstadia(){
        if(rol.getPromedioAsesorInterno()>0 && rol.getPromedioAsesorExterno()>0){
        ResultadoEJB<List<CalificacionCriterioEstadia>> resAct = ejb.actualizarEvaluacionEstadia(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante(), rol.getListaEvaluacionEstadiaRegistrada(), rol.getPromedioAsesorInterno(), rol.getPromedioAsesorExterno());
        mostrarMensajeResultadoEJB(resAct);
        listaCriteriosEvaluacionEstadiaRegistrada();
        Ajax.update("frmModalCapturaEvaluacion");
        }else Messages.addGlobalWarn("El promedio o los promedios no pueden ser 0");
    }
    
     /**
     * Permite actualizar la evaluación de estadía del estudiante
     */
    public void guardarEvaluacionEstadia(){
        if(rol.getPromedioAsesorInterno()>0 && rol.getPromedioAsesorExterno()>0){
        ResultadoEJB<List<CalificacionCriterioEstadia>> resReg = ejb.guardarEvaluacionEstadia(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante(), rol.getListaEvaluacionEstadia(), rol.getPromedioAsesorInterno(), rol.getPromedioAsesorExterno());
        mostrarMensajeResultadoEJB(resReg);
        listaEstudiantesSeguimiento();
        Ajax.update("tbListaSeguimientoEstadia");
        Ajax.update("frm");
        Ajax.update("frmModalCapturaEvaluacion");
        }else Messages.addGlobalWarn("El promedio o los promedios no pueden ser 0");
    }
    
    /**
     * Permite generar el formato de cédula de evaluación y acreditación de estadía dele studiante seleccionado
     * @param dtoSeguimientoEstadia 
     */
    public void generarCedEvalAcredEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia) {
        rol.setEstudianteSeguimiento(dtoSeguimientoEstadia);
        rol.setPromedioAsesorInterno(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getPromedioAsesorInterno());
        if(rol.getPromedioAsesorInterno() == 0)
        {
            Messages.addGlobalWarn("No se ha registrado evaluación de estadía del estudiante seleccionado");
            rol.setEvaluacionRegistrada(Boolean.FALSE);
        }else{
            listaCriteriosEvaluacionEstadiaRegistrada();
            rol.setEvaluacionRegistrada(Boolean.TRUE);
        }
        generarCedEvalAcredEstadia.generarFormato(rol.getEstudianteSeguimiento(), rol.getEvaluacionRegistrada());
    }
    
     /**
     * Método para verificar si el estudiante seleccionado aprobó sus evaluaciones de estadía (asesor externo e interno)
     * @param dtoSeguimientoEstadia
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean aproboEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarAproboEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
}
