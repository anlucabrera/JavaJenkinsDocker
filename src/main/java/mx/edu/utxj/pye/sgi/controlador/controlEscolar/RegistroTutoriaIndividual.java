/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasosCriticosPendientes;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTutoriaIndividualCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroTutoriaIndividualRolTutor;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasIndividuales;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroTutoriaIndividual extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = 638459276492926302L;
    @Getter     @Setter                         private         RegistroTutoriaIndividualRolTutor           rol;
    @Getter     Boolean                         tieneAcceso = false;
    
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    @EJB        EjbCasoCritico                  ejbCritico;
    
    @Inject     Caster                          caster;
    @Inject     LogonMB                         logon;
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "tutoria individual";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_TUTORIA_INDIVIDUAL);

            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTutor(logon.getPersonal().getClave());
            if(!resAcceso.getCorrecto()) {mostrarMensajeResultadoEJB(resAcceso);return;}
            rol = new RegistroTutoriaIndividualRolTutor(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado());
            ResultadoEJB<EventosRegistros> resEventoRegistro = ejb.verificarEvento();
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCapturaCargaAcademicaTutor(rol.getDocenteLogueado());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEventoRegistro.getCorrecto()) mostrarMensajeResultadoEJB(resEventoRegistro);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEventoRegistroActivo(resEventoRegistro.getValor());
            rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConCargaGrupo(resPeriodos.getValor());
            cambiarPeriodo();
            initFiltros();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void initFiltros(){
        try {
            rol.setEventosPorPeriodo(ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(rol.getPeriodosConCargaGrupo(), rol.getEventosPorPeriodo(), rol.getEventoRegistroActivo(), rol.getDocenteLogueado()).getValor();
            if(entrada != null){
                rol.setPeriodosConCargaGrupo(entrada.getKey());
                rol.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(RegistroAsesoriaDocente.class.getName()).log(Level.SEVERE, null, ex);
        }
        cambiarSesiones();
    }
    
    public void inicializarTutoriaIndividual(){
        rol.setDtoTutoriaIndividualCE(new DtoTutoriaIndividualCE());
        rol.setFecha(new Date());
        rol.setHoraInicio(new Date());
        
        TutoriasIndividuales tutoriaIndividual = new TutoriasIndividuales();
        tutoriaIndividual.setSesionGrupal(rol.getSesionGrupalSeleccionada());
        tutoriaIndividual.setEventoRegistro(rol.getEventoSeleccionado().getEventoRegistro());
        rol.getDtoTutoriaIndividualCE().setTutoriaIndividual(tutoriaIndividual);
        
        Estudiante estudiantesPye = new Estudiante();
        DtoEstudianteComplete estudiante = new DtoEstudianteComplete(estudiantesPye, "");
        rol.setEstudianteSeguimientoSeleccionado(estudiante);
        
        rol.setMensajeVerificacionAsesorias("");
        rol.setVerificacionAsesorias(Boolean.FALSE);
//        TODO: Verificar si en la tutoría se seleccionará de la misma manera que de las demas
    }
    
    public void inicializarListaTutoriasIndividuales(){
        rol.setListaDtoTutoriasIndividuales(new ArrayList<>());
        rol.setListaDtoTutoriasIndividuales(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaCasosCriticos(){
        rol.setListaCasosCriticos(Collections.EMPTY_LIST);
        rol.setDtoCasoCritico(null);
    }
    
    /*********************************************** Filtros *********************************************************/
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo seleccionado.");
            rol.setListadoGruposTutor(Collections.EMPTY_LIST);
            rol.setListaSesionesGrupalesTutorias(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<DtoListadoTutores>> resListadoGruposTutores = ejb.listarGruposTutor(rol.getPeriodoSeleccionado(), rol.getDocenteLogueado());
        if(!resListadoGruposTutores.getCorrecto()) mostrarMensajeResultadoEJB(resListadoGruposTutores);
        else rol.setListadoGruposTutor(resListadoGruposTutores.getValor());
        
        rol.setEventosPorPeriodo(ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
        
        cambiarGrupo();
    }
    
    public void cambiarGrupo(){
        if(rol.getGrupoTutorSeleccionado() == null){
            mostrarMensaje("No hay un grupo tutorado seleccionado");
            rol.setListaSesionesGrupalesTutorias(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<SesionesGrupalesTutorias>> resSesiones = ejb.buscaSesionesGrupalesXPlanAT(rol.getGrupoTutorSeleccionado().getGrupo());
        if(!resSesiones.getCorrecto())mostrarMensajeResultadoEJB(resSesiones);
        else{
            rol.setListaSesionesGrupalesTutorias(resSesiones.getValor());
        }
        cambiarSesiones();
        actualizarListaTutoriasIndividuales();
    }
    
    public void cambiarSesiones(){
        if(rol.getSesionGrupalSeleccionada() == null){
            mostrarMensaje("No hay sesion grupal de tutoría seleccionada.");
            return;
        }
        inicializarTutoriaIndividual();
        inicializarListaTutoriasIndividuales();
        actualizarListaTutoriasIndividuales();
    }
    
    public void cambiarSesionesDate(){
        if(rol.getSesionGrupalSeleccionada() == null){
            mostrarMensaje("No hay sesion grupal de tutoría seleccionada.");
            return;
        }
        inicializarTutoriaIndividual();
        inicializarListaTutoriasIndividuales();
        actualizarListaTutoriasIndividuales();
    }
    
    /*********************************************** Administración de datos *********************************************************/
    public Date parsearFechaAHora(Date hora) {
        try {
            DateFormat formatoHora = new SimpleDateFormat("HH:mm a");
            String horaCadena = formatoHora.format(hora);
            Date horaParseada = formatoHora.parse(horaCadena);
            return horaParseada;
        } catch (ParseException e) {
            mostrarMensajeResultadoEJB(ResultadoEJB.crearErroneo(1, "Ha ocurrido un error al momento de convertir la hora de la tutoría, la tutoría individual se ha guardado con la fecha y hora actual, favor de notificar al administrador del sistema ", e, null));
            return new Date();
        }
    }
    
    public String parsearFechaCasoCritico(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        String horaCadena = formatter.format(fecha);
        return horaCadena;
    }
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            ResultadoEJB<Estudiante> estudianteEncontrado = ejb.buscaEstudiante(estudiante.getEstudiantes().getIdEstudiante());
            if(estudianteEncontrado.getCorrecto()){
                rol.setEstudianteSeleccionado(estudianteEncontrado.getValor());
                actualizarListaCasosCriticos();
            }
            rol.setMensajeVerificacionAsesorias("");
            rol.setVerificacionAsesorias(Boolean.FALSE);
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void actualizarListaCasosCriticos() {
        rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setEstudiante(rol.getEstudianteSeleccionado());
        ResultadoEJB<List<DtoCasoCritico>> casosCriticosEstudiane = ejbCritico.identificarPorEsdudiante(rol.getEstudianteSeleccionado());
        if (casosCriticosEstudiane.getCorrecto()) {
            rol.setListaCasosCriticos(Collections.EMPTY_LIST);
            rol.setListaCasosCriticos(casosCriticosEstudiane.getValor());
        } else {
            rol.setListaCasosCriticos(Collections.EMPTY_LIST);
        }
        rol.getDtoTutoriaIndividualCE().setCasoCritico(null);
        rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setCasoCritico(null);
    }

    public void verificarAsistenciaAsesoria(ValueChangeEvent event) {
        if ((DtoCasoCritico) event.getNewValue() != null) {
            rol.setDtoCasoCritico((DtoCasoCritico) event.getNewValue());
            actualizarCasoCriticoSeleccionado();
        }else{
            rol.getDtoTutoriaIndividualCE().setCasoCritico(null);
            rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setCasoCritico(null);
            rol.setMensajeVerificacionAsesorias("");
        }
    }
    
    public void actualizarCasoCriticoSeleccionado() {
        ResultadoEJB<List<DtoCasoCritico>> cc = ejbCritico.identificarPorEsdudianteCaso(rol.getEstudianteSeleccionado(), rol.getDtoCasoCritico().getCasoCritico().getCaso());
        if (cc.getCorrecto()) {
            rol.setDtoCasoCritico(cc.getValor().get(0));
            rol.setFechaCasoCritico(parsearFechaCasoCritico(rol.getDtoCasoCritico().getCasoCritico().getFechaRegistro()));
            //Asignacion de caso crítico
            rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setCasoCritico(rol.getDtoCasoCritico().getCasoCritico());
            rol.getDtoTutoriaIndividualCE().setCasoCritico(rol.getDtoCasoCritico());
            
            if (rol.getDtoCasoCritico().getCasoCritico().getConfiguracion() != null) {
                ResultadoEJB<Boolean> asistenciaAsesoria = ejb.verificarParticipanteAsesoriaParaTutoria(rol.getDtoCasoCritico().getCasoCritico().getIdEstudiante().getIdEstudiante(), rol.getDtoCasoCritico().getCasoCritico().getConfiguracion().getConfiguracion());
                if (asistenciaAsesoria.getCorrecto()) {
                    rol.setMensajeVerificacionAsesorias("El estudiante asistió al menos a una asesoría en la unidad que se encuentra reprobada o en caso crítico");
                    rol.setVerificacionAsesorias(Boolean.TRUE);
                } else {
                    rol.setMensajeVerificacionAsesorias("El estudiante no ha asistido a ninguna asesoría en la unidad en donde se encuentra en caso crítico");
                    rol.setVerificacionAsesorias(Boolean.FALSE);
                }
            } else {
                rol.setMensajeVerificacionAsesorias("El estudiante ha sido detectado en una tutoria grupal");
                rol.setVerificacionAsesorias(Boolean.TRUE);
            }
        }
    }

    public void abriDialogoEvidencia(){
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalCargaEvidenciaCasoCriticoTutor').show();");
    }
    
    public void abriDialogoSeguimientoEspecilista(){
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalSeguimientoEspecilista').show();");
    }
    
    public void guardarTutoriaIndividual(){
        rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setFecha(rol.getFecha());
        rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setHoraInicio(parsearFechaAHora(rol.getHoraInicio()));
        ResultadoEJB<TutoriasIndividuales> res = ejb.guardaTutoriasIndividuales(rol.getDtoTutoriaIndividualCE().getTutoriaIndividual(), rol.getDtoCasoCritico());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaIndividual();
            inicializarListaCasosCriticos();
            actualizarListaTutoriasIndividuales();
        }else{
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaIndividual();
            inicializarListaCasosCriticos();
            actualizarListaTutoriasIndividuales();
        }
    }
    
    public void editatutoriaIndividual(){
        rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setFecha(rol.getFecha());
        rol.getDtoTutoriaIndividualCE().getTutoriaIndividual().setHoraInicio(parsearFechaAHora(rol.getHoraInicio()));
        ResultadoEJB<TutoriasIndividuales> res = ejb.editaTutoriasIndividuales(rol.getDtoTutoriaIndividualCE().getTutoriaIndividual(),rol.getDtoCasoCritico());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaIndividual();
            inicializarListaCasosCriticos();
            actualizarListaTutoriasIndividuales();
        }else{
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaIndividual();
            inicializarListaCasosCriticos();
            actualizarListaTutoriasIndividuales();
        }
    }
    
    public void eliminarTutoriaIndividual(TutoriasIndividuales tutoriaIndividual, DtoCasoCritico dtoCasoCritico){
        ResultadoEJB<Boolean> res = ejb.eliminaTutoriaIndividual(tutoriaIndividual, dtoCasoCritico);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListaTutoriasIndividuales();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void seleccionarTutoriaIndividual() {
//        Valor seleccionado
        rol.getTutoriaIndividualSeleccionada().getTutoriaIndividual();
//        Valores anidados
        rol.setEstudianteSeguimientoSeleccionado(caster.dtoEstudianteAutocomplete(rol.getTutoriaIndividualSeleccionada().getTutoriaIndividual().getEstudiante().getIdEstudiante()));
        rol.setDtoTutoriaIndividualCE(rol.getTutoriaIndividualSeleccionada());
        rol.setFecha(rol.getTutoriaIndividualSeleccionada().getTutoriaIndividual().getFecha());
        rol.setHoraInicio(rol.getTutoriaIndividualSeleccionada().getTutoriaIndividual().getHoraInicio());
//        Actualizar lista de casos criticos el estudiante seleccionado
        ResultadoEJB<Estudiante> estudianteEncontrado = ejb.buscaEstudiante(rol.getEstudianteSeguimientoSeleccionado().getEstudiantes().getIdEstudiante());
        rol.setEstudianteSeleccionado(estudianteEncontrado.getValor());

//        Validar que el caso critico sea o no nulo
        if (rol.getTutoriaIndividualSeleccionada().getCasoCritico() != null) {
            ResultadoEJB<List<DtoCasoCritico>> casosCriticosEstudiane = ejbCritico.identificarPorEsdudiante(rol.getEstudianteSeleccionado());
            if (casosCriticosEstudiane.getCorrecto()) {
                rol.setListaCasosCriticos(casosCriticosEstudiane.getValor());

//            Actualizar caso critico de la tutoria individual consultada
                rol.setDtoCasoCritico(rol.getTutoriaIndividualSeleccionada().getCasoCritico());
                actualizarCasoCriticoSeleccionado();
            }
        }else{
            rol.setListaCasosCriticos(Collections.EMPTY_LIST);
        }
    }
    
    public void subirEvidencia(){
        try {
            Map.Entry<Boolean, Integer> res = ejb.registrarEvidenciaCasoCritico(rol.getDtoCasoCritico(), rol.getArchivo());
            if (res.getKey()) {
                Messages.addGlobalInfo("Las evidencia se ha registrado correctamente.");
//                Mover a codigo independiente para evitar repeticion, colocado solo como prueba
                actualizarCasoCriticoSeleccionado();           
            } else {
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(), String.valueOf(1)));
            }
        } catch (Throwable ex) {
            Logger.getLogger(RegistroTutoriaIndividual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(){
        Boolean eliminado = ejb.eliminarEvidenciaCasoCritico(rol.getDtoCasoCritico());
        if(eliminado){
           Messages.addGlobalInfo("El archivo se eliminó de forma correcta."); 
//                Mover a codigo independiente para evitar repeticion, colocado solo como prueba
            actualizarCasoCriticoSeleccionado();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void liberarCasoCritico(){
        ResultadoEJB<Boolean> res = ejb.liberarCasoCritico(rol.getTutoriaIndividualSeleccionada().getTutoriaIndividual(), rol.getDtoCasoCritico());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaIndividual();
            inicializarListaCasosCriticos();
            actualizarListaTutoriasIndividuales();
        }else{
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaIndividual();
            inicializarListaCasosCriticos();
            actualizarListaTutoriasIndividuales();
        }
    }
    
    public void descargarEvidencia() throws IOException {
        File f = new File(rol.getDtoCasoCritico().getCasoCritico().getEvidenciaTutor());
        Faces.sendFile(f, false);
    }
    
    public void descargarEvidenciaEspecialista() throws IOException {
        File f = new File(rol.getDtoCasoCritico().getCasoCritico().getEvidenciaEspecialista());
        Faces.sendFile(f, false);
    }
    
    
    public List<PersonalActivo> completeDocentes(String pista){
        ResultadoEJB<List<PersonalActivo>> res = ejb.buscarDocente(pista);
        if(res.getCorrecto()){
            return  res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.EMPTY_LIST;
        }
    }
    
    public void seleccionarEspecialista(ValueChangeEvent event){
        if(event.getNewValue() instanceof PersonalActivo){
            PersonalActivo personalActivo = (PersonalActivo) event.getNewValue();
            rol.getDtoCasoCritico().getCasoCritico().setEspecialista(personalActivo.getPersonal().getClave());
            ResultadoEJB<DtoCasoCritico> res = ejbCritico.actualizarCasoCritico(rol.getDtoCasoCritico());
            if(res.getCorrecto()){
                mostrarMensajeResultadoEJB(res);
                actualizarCasoCriticoSeleccionado();
                Ajax.update("frmEvidencias");
            }else mostrarMensajeResultadoEJB(res);
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    /*********************************************** Llenado de listas *********************************************************/
    
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(pista, rol.getPeriodoSeleccionado().getPeriodo(), rol.getGrupoTutorSeleccionado().getGrupo().getGrado(), rol.getGrupoTutorSeleccionado().getGrupo().getLiteral(), rol.getGrupoTutorSeleccionado().getGrupo().getIdPe());
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    public void actualizarListaTutoriasIndividuales(){
        ResultadoEJB<List<DtoTutoriaIndividualCE>> res = ejb.buscaTutoriasIndividuales(rol.getSesionGrupalSeleccionada(), rol.getEventoSeleccionado());
        if(res.getCorrecto()){
            rol.setListaDtoTutoriasIndividuales(res.getValor());
        }else{
            inicializarListaTutoriasIndividuales();
        }
    }
    
    public void consultarCasosCriticos(){
        ResultadoEJB<List<DtoCasosCriticosPendientes>> res = ejb.getCasoCriticosPendientes(rol.getGrupoTutorSeleccionado().getGrupo());
        if(res.getCorrecto()){
            rol.setListaCasosCriticosPendientes(Collections.EMPTY_LIST);
            rol.setListaCasosCriticosPendientes(res.getValor().stream().sorted(Comparator.comparingInt(DtoCasosCriticosPendientes::getCasosCriticosPorAtender).reversed()).collect(Collectors.toList()));
        }else{
            rol.setListaCasosCriticosPendientes(Collections.EMPTY_LIST);
            mostrarMensajeResultadoEJB(res);
        }
        Ajax.update("frmCasosCriticosPendientes");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalCasosCriticosPendientes').show();");
    }
    
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            DtoCasosCriticosPendientes casoAgrupado = (DtoCasosCriticosPendientes) event.getComponent().getAttributes().get("casoCriticoPendiente");
            ResultadoEJB<List<DtoCasoCritico>> res = ejbCritico.identificarPorGrupo(casoAgrupado.getIdGrupo(), casoAgrupado.getTipo(), casoAgrupado.getEstado());
            List<String> listaSecundaria = new ArrayList<>();
            if(res.getCorrecto()){
                rol.setListaCasosCriticosPorGrupo(res.getValor());
                rol.getListaCasosCriticosPorGrupo().stream().forEach((t) -> {
                    listaSecundaria.add(t.toLabelTutoriaIndividual(t));
                });
                rol.setListaSecundaria(listaSecundaria);
            }else{
                rol.setListaCasosCriticosPorGrupo(Collections.EMPTY_LIST);
                rol.setListaSecundaria(Collections.EMPTY_LIST);
            }
        } else {
            rol.setListaCasosCriticosPorGrupo(Collections.EMPTY_LIST);
        }
    }
}
