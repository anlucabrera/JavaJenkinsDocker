/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsesoriaCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroAsesoriaRolDocente;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroAsesoriaDocente extends ViewScopedRol implements Desarrollable{
    @Getter     @Setter     private     RegistroAsesoriaRolDocente      rol;
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    
    @Inject     LogonMB                         logon;
    
    @Getter     Boolean                         tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro de asesorías";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init() {
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_ASESORIA);

    //        Verificar si el usuario logueado es docente
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());
            if (!resAcceso.getCorrecto()) {mostrarMensajeResultadoEJB(resAcceso);return;}
            rol = new RegistroAsesoriaRolDocente(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado());
            ResultadoEJB<EventosRegistros> resEventoRegistro = ejb.verificarEvento();
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCapturaCargaAcademica(rol.getDocenteLogueado());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEventoRegistro.getCorrecto()) mostrarMensajeResultadoEJB(resEventoRegistro);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEventoRegistroActivo(resEventoRegistro.getValor());
            rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConCarga(resPeriodos.getValor());
            initFiltros();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void actualizarEstudiantes(){
        verificarParticipantes();
        repetirUltimoMensaje();
    }
    
    public void inicializarDtoAsesoria() {
        DtoAsesoriaCE dtoACE = new DtoAsesoriaCE();
        rol.setFecha(new Date());
        rol.setHora(new Date());
        dtoACE.setAsesoria(new Asesoria());
        dtoACE.getAsesoria().setFechaHora(new Date());
        dtoACE.getAsesoria().setConfiguracion(rol.getUnidadConfiguracionSeleccionada().getUnidadMateriaConfiguracion());
        dtoACE.getAsesoria().setEventoRegistro(rol.getEventoSeleccionado().getEventoRegistro());
        dtoACE.setDtoUnidadConfiguracion(rol.getUnidadConfiguracionSeleccionada());
        dtoACE.setEventosRegistros(rol.getEventoSeleccionado());
        rol.setDtoAsesoriaCE(dtoACE);
        
        rol.setAsesoriaSeleccionada(new Asesoria());
    }
    
    public void inicializarListaAsesorias(){
        rol.setListaAsesorias(new ArrayList<>());
        rol.setListaAsesorias(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaEstudiantes(){
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setListaEstudiantes(Collections.EMPTY_LIST);
    }
    
    
    public void initFiltros(){
        try {
            rol.setEventosPorPeriodo(ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(rol.getPeriodosConCarga(), rol.getEventosPorPeriodo(), rol.getEventoRegistroActivo(), rol.getDocenteLogueado()).getValor();
            if(entrada != null){
                rol.setPeriodosConCarga(entrada.getKey());
                rol.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(RegistroAsesoriaDocente.class.getName()).log(Level.SEVERE, null, ex);
        }
        cambiarPeriodo();
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Filtros *********************************************************/
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setCargasDocente(Collections.EMPTY_LIST);
            return;
        }

        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.getCargasAcademicasPorPeriodo(rol.getDocenteLogueado(), rol.getPeriodoSeleccionado());
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasDocente(resCargas.getValor());
        
        rol.setEventosPorPeriodo(ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
        
        cambiarCarga();
    }
    
    public void cambiarCarga(){
        if(rol.getCargaAcademicaSeleccionada() == null){
            mostrarMensaje("No hay carga académica seleccionada");
            rol.setUnidadConfiguraciones(Collections.EMPTY_LIST);
            return;
        }

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejb.getConfiguraciones(rol.getCargaAcademicaSeleccionada());
        if(!resConfiguraciones.getCorrecto()) mostrarMensajeResultadoEJB(resConfiguraciones);
        else {
            rol.setUnidadConfiguraciones(resConfiguraciones.getValor());
        }
        cambiarUnidad();
        listaEstudiantesGrupo();
        actualizarListadoAsesorias();
    }
    
    public void cambiarUnidad(){
        if(rol.getUnidadConfiguracionSeleccionada() == null) {
            mostrarMensaje("No hay unidad de evaluación seleccionada.");
//            rol.setEstudiantesPorGrupo(null);
            return;
        }
        inicializarDtoAsesoria();
        inicializarListaAsesorias();
        actualizarListadoAsesorias();
    }
    
    public void cambiarUnidadDate(){
        if(rol.getUnidadConfiguracionSeleccionada() == null) {
            mostrarMensaje("No hay unidad de evaluación seleccionada.");
//            rol.setEstudiantesPorGrupo(null);
            return;
        }
        inicializarDtoAsesoria();
        inicializarListaAsesorias();
        actualizarListadoAsesorias();
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Administración de datos *********************************************************/
 
    public Date parsearFechaHora(Date fecha, Date hora) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fechaCadena = dateFormat.format(fecha);
            DateFormat hourFormat = new SimpleDateFormat("HH:mm a");
            String horaCadena = hourFormat.format(hora);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            String fechaCompleta = fechaCadena + " " + horaCadena;
            Date fechaParseada = formatter.parse(fechaCompleta);
            return fechaParseada;
        } catch (ParseException e) {
            mostrarMensajeResultadoEJB(ResultadoEJB.crearErroneo(1, "Ha ocurrido un error al momento de convertir la fecha, la asesoría se ha guardado con la fecha y hora actual, favor de notificar al administrador del sistema ", e, null));
            return new Date();
        }
    }

    public void guardarNuevaAsesoria() {
        rol.getDtoAsesoriaCE().getAsesoria().setFechaHora(parsearFechaHora(rol.getFecha(), rol.getHora()));
        ResultadoEJB<DtoAsesoriaCE> res = ejb.guardaAsesoriaCE(rol.getDtoAsesoriaCE());
        if (res.getCorrecto()) {
            mostrarMensajeResultadoEJB(res);
            inicializarDtoAsesoria();
            actualizarListadoAsesorias();
        } else {
            mostrarMensajeResultadoEJB(res);
            inicializarDtoAsesoria();
        }

    }
    
    public void actualizarFecha(ValueChangeEvent event) {
        Asesoria asesoria = (Asesoria) event.getComponent().getAttributes().get("asesoria");
        Date fecha = (Date) event.getNewValue();
        asesoria.setFechaHora(parsearFechaHora(fecha, (Date)event.getOldValue()));
        actualizarAsesoria(asesoria);
    }

    public void actualizarHora(ValueChangeEvent event) {
        Asesoria asesoria = (Asesoria) event.getComponent().getAttributes().get("asesoria");
        Date hora = (Date) event.getNewValue();
        asesoria.setFechaHora(parsearFechaHora((Date)event.getOldValue(), hora));
        actualizarAsesoria(asesoria);
    }

    public void actualizarObservacionesCompromiso(ValueChangeEvent event){
        Asesoria asesoria = (Asesoria) event.getComponent().getAttributes().get("asesoria");
        String oc = event.getNewValue().toString();
        asesoria.setObservacionesCompromisos(oc);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarTiempoInvertido(ValueChangeEvent event){
        Asesoria asesoria = (Asesoria) event.getComponent().getAttributes().get("asesoria");
        short tiempoInvertido = Short.parseShort(event.getNewValue().toString());
        asesoria.setTiempoInvertido(tiempoInvertido);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarTipoTiempo(ValueChangeEvent event){
        Asesoria asesoria = (Asesoria) event.getComponent().getAttributes().get("asesoria");
        String tipoTiempo = event.getNewValue().toString();
        asesoria.setTipoTiempo(tipoTiempo);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarTipo(ValueChangeEvent event){
        Asesoria asesoria = (Asesoria) event.getComponent().getAttributes().get("asesoria");
        String tipo = event.getNewValue().toString();
        asesoria.setTipo(tipo);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarAsesoria(Asesoria asesoria) {
        ResultadoEJB<Boolean> res = ejb.editaAsesoria(asesoria);
        if (res.getCorrecto()) {
            mostrarMensajeResultadoEJB(res);
            actualizarListadoAsesorias();
        } else {
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void eliminarAsesoria(Integer idAsesoria){
        ResultadoEJB<Boolean> res = ejb.eliminaAsesoria(idAsesoria);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListadoAsesorias();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void verificarParticipantes() {
        if (!rol.getListaEstudiantes().isEmpty()) {
            rol.getListaEstudiantes().forEach((t) -> {
                ResultadoEJB<Boolean> res = ejb.verificarParticipanteAsesoria(rol.getAsesoriaSeleccionada(), t.getEstudiante().getEstudiante().getIdEstudiante());
                if (res.getCorrecto()) {
                    t.setParticipacion(Boolean.TRUE);
                } else {
                    t.setParticipacion(Boolean.FALSE);
                }
            });
        }
    }

    public void guardarAsistenciaAsesoria(ValueChangeEvent event){
        Estudiante estudiante = (Estudiante) event.getComponent().getAttributes().get("estudiante");
        System.err.println("Estudiante: " + estudiante.getIdEstudiante());
        ResultadoEJB<Boolean> res = ejb.asignaParticipanteAsesoria(rol.getAsesoriaSeleccionada(), estudiante.getIdEstudiante());
        mostrarMensajeResultadoEJB(res);
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Llenado de listas *********************************************************/
    public void actualizarListadoAsesorias(){
        rol.setListaAsesorias(ejb.buscaAsesoriasPorUnidadEventoRegistro(rol.getUnidadConfiguracionSeleccionada().getUnidadMateriaConfiguracion().getConfiguracion(), rol.getEventoSeleccionado().getEventoRegistro()).getValor());
    }
    
    public void listaEstudiantesGrupo(){
        rol.setListaEstudiantes(ejb.obtenerListaEstudiantes(rol.getCargaAcademicaSeleccionada().getGrupo()).getValor());
    }
    
}
