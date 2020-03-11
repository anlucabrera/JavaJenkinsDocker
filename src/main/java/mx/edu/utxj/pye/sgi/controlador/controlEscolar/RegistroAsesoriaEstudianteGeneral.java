/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsesoriaEstudianteCe;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoParticipantesAsesoriaCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroAsesoriaEstudianteRolGeneral;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidadorDocente;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroAsesoriaEstudianteGeneral extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = -3511442883135044923L;
    @Getter     @Setter                             private                 RegistroAsesoriaEstudianteRolGeneral       rol;
    @Getter     Boolean                             tieneAcceso = false;
    @Getter     private                             Boolean                 cargado = false;
    @EJB        EjbRegistroAsesoriaEstudiante       ejb;
    @EJB        EjbValidadorDocente                 ejbValidadorDocente;
    @EJB        EjbRegistroAsesoriaTutoria          ejbRegistroAsesoriaTutoria;
    @EJB        EjbPeriodoEventoRegistro            ejbPeriodoEventoRegistro;
    @EJB        EjbPropiedades                      ep;
    
    @Inject     LogonMB                             logonMB;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Registro de Asesorias Generales";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init() {
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_ASESORIAS_GENERAL);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbValidadorDocente.validarDocente(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEspecialista = ejbRegistroAsesoriaTutoria.validarUsuarioEspecialista(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto() && !resAccesoEspecialista.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            rol = new RegistroAsesoriaEstudianteRolGeneral(resAcceso.getValor());
            if(rol.getPersonal() != null){tieneAcceso = rol.tieneAcceso(rol.getPersonal());}
            if(!tieneAcceso){
                rol = new RegistroAsesoriaEstudianteRolGeneral(resAccesoEspecialista.getValor());
                tieneAcceso = rol.tieneAcceso(rol.getPersonal());
            }
            if(!tieneAcceso){return;}
            ResultadoEJB<EventosRegistros> resEventoRegistro = ejbPeriodoEventoRegistro.getEventoRegistro();
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.consultarPeriodosEscolaresPorGrupo();
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEventoRegistro.getCorrecto())mostrarMensajeResultadoEJB(resEventoRegistro);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEventoRegistroActivo(resEventoRegistro.getValor());
            rol.setPeriodoActivo(ejbPeriodoEventoRegistro.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConGrupo(resPeriodos.getValor());
            initFiltros();
        }catch(Exception e){mostrarExcepcion(e);}
    }
    
    /*********************************************** Inicializadores *********************************************************/
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void actualizarEstudiantes(){
        verificarParticipantes();
        repetirUltimoMensaje();
    }
    
    public void inicializarDtoAsesoriaEstudianteCe(){
        rol.setFecha(new Date());
        rol.setHora(new Date());
        DtoAsesoriaEstudianteCe dtoAeCe = new DtoAsesoriaEstudianteCe();
        dtoAeCe.setAsesoriaEstudiante(new AsesoriasEstudiantes());
        dtoAeCe.getAsesoriaEstudiante().setFechaHora(new Date());
        dtoAeCe.getAsesoriaEstudiante().setPersonal(rol.getPersonal().getPersonal().getClave());
        dtoAeCe.getAsesoriaEstudiante().setEventoRegistro(rol.getEventoSeleccionado().getEventoRegistro());
        dtoAeCe.setPersonalActivo(rol.getFiltro().getEntity());
        dtoAeCe.setEventosRegistros(rol.getEventoSeleccionado());
        rol.setDtoAsesoriaEstudianteCe(dtoAeCe);
        rol.setAsesoriaEstudiante(new AsesoriasEstudiantes());
    }
    
    public void inicializarListaAsesorias(){
        rol.setListaAsesoriasEstudiantes(new ArrayList<>());
        rol.setListaAsesoriasEstudiantes(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaEstudiantes(){
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setListaEstudiantes(Collections.EMPTY_LIST);
    }
    
    public void initFiltros(){
        try {
            rol.setEventosPorPeriodo(ejbPeriodoEventoRegistro.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejbPeriodoEventoRegistro.comprobarEventoActualDocenteGeneral(rol.getPeriodosConGrupo(), rol.getEventosPorPeriodo(), rol.getEventoRegistroActivo(), rol.getPersonal()).getValor();
            if(entrada != null){
                rol.setPeriodosConGrupo(entrada.getKey());
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
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.nulificarPeriodoSeleccionado();
            return;
        }
        ResultadoEJB<List<EventosRegistros>> resEventosPorPeriodo = ejbPeriodoEventoRegistro.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado());
        if(resEventosPorPeriodo.getCorrecto()){
            rol.setEventosPorPeriodo(resEventosPorPeriodo.getValor());
        }else mostrarMensajeResultadoEJB(resEventosPorPeriodo);
        
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativosGruposPorPeriodo = ejb.consultarProgramasEducativosGruposPorPeriodo(rol.getPeriodoSeleccionado().getPeriodo());
        if (resProgramasEducativosGruposPorPeriodo.getCorrecto()) {
            rol.setListaProgramasEducativosPorPeriodo(resProgramasEducativosGruposPorPeriodo.getValor());
        } else mostrarMensajeResultadoEJB(resEventosPorPeriodo);

        cambiarEventoRegistro();
        cambiarProgramaEducativoGrupoPorPeriodo();
    }
    
    public void cambiarEventoRegistro(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado.");
            return;
        }
        
        inicializarDtoAsesoriaEstudianteCe();
        inicializarListaAsesorias();
        actualizarListadoAsesorias();
    }
    
    public void cambiarEventoRegistroDate(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado.");
            return;
        }
        
        inicializarDtoAsesoriaEstudianteCe();
        inicializarListaAsesorias();
        actualizarListadoAsesorias();
    }
    
    public void cambiarProgramaEducativoGrupoPorPeriodo(){
        if(rol.getProgramaEducativoSeleccionado() == null){
            mostrarMensaje("No hay programa educativo seleccionado.");
            return;
        }
        ResultadoEJB<List<Grupo>> resGruposPorPeriodoProgramaEducativo = ejb.consultarGruposPorPeriodoProgramaEducativo(rol.getPeriodoSeleccionado().getPeriodo(), rol.getProgramaEducativoSeleccionado().getArea());
        if(resGruposPorPeriodoProgramaEducativo.getCorrecto()){
            rol.setListaGruposPorPeriodoProgramaEducativo(resGruposPorPeriodoProgramaEducativo.getValor());
        }else mostrarMensajeResultadoEJB(resGruposPorPeriodoProgramaEducativo);
        cambiarGrupoPorPeriodoProgramaEducativo();
        
        listarEstudiantesGrupo();
        verificarParticipantes();
    }
    
    public void cambiarGrupoPorPeriodoProgramaEducativo(){
        if(rol.getGrupoSeleccionado() == null){
            mostrarMensaje("No hay grupo seleccionado.");
            return;
        }
        listarEstudiantesGrupo();
        verificarParticipantes();
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
    
    public void guardarNuevaAsesoriaEstudiante(){
        rol.getDtoAsesoriaEstudianteCe().getAsesoriaEstudiante().setFechaHora(parsearFechaHora(rol.getFecha(), rol.getHora()));
        ResultadoEJB<DtoAsesoriaEstudianteCe> resGuardar = ejb.guardarAsesoriaEstudiante(rol.getDtoAsesoriaEstudianteCe());
        if(resGuardar.getCorrecto()){
            mostrarMensajeResultadoEJB(resGuardar);
            inicializarDtoAsesoriaEstudianteCe();
            actualizarListadoAsesorias();
        } else{
            mostrarMensajeResultadoEJB(resGuardar);
            inicializarDtoAsesoriaEstudianteCe();
        }
    }
    
    public void actualizarFecha(ValueChangeEvent event) {
        AsesoriasEstudiantes asesoria = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoria");
        Date fecha = (Date) event.getNewValue();
        asesoria.setFechaHora(parsearFechaHora(fecha, (Date)event.getOldValue()));
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarHora(ValueChangeEvent event) {
        AsesoriasEstudiantes asesoria = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoria");
        Date hora = (Date) event.getNewValue();
        asesoria.setFechaHora(parsearFechaHora((Date)event.getOldValue(), hora));
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarObservacionesCompromiso(ValueChangeEvent event){
        AsesoriasEstudiantes asesoria = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoria");
        String oc = event.getNewValue().toString();
        asesoria.setObservacionesCompromisos(oc);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarTiempoInvertido(ValueChangeEvent event){
        AsesoriasEstudiantes asesoria = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoria");
        short tiempoInvertido = Short.parseShort(event.getNewValue().toString());
        asesoria.setTiempoInvertido(tiempoInvertido);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarTipoTiempo(ValueChangeEvent event){
        AsesoriasEstudiantes asesoria = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoria");
        String tipoTiempo = event.getNewValue().toString();
        asesoria.setTipoTiempo(tipoTiempo);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarTipo(ValueChangeEvent event){
        AsesoriasEstudiantes asesoria = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoria");
        String tipo = event.getNewValue().toString();
        asesoria.setTipo(tipo);
        actualizarAsesoria(asesoria);
    }
    
    public void actualizarAsesoria(AsesoriasEstudiantes asesoria) {
        ResultadoEJB<Boolean> res = ejb.editaAsesoriaEstudiante(asesoria);
        if (res.getCorrecto()) {
            mostrarMensajeResultadoEJB(res);
            actualizarListadoAsesorias();
        } else {
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void eliminarAsesoria(Integer idAsesoria){
        ResultadoEJB<Boolean> res = ejb.eliminaAsesoriaEstudiante(idAsesoria);
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
                ResultadoEJB<Boolean> res = ejb.verificarParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
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
        ResultadoEJB<Boolean> res = ejb.asignaParticipanteAsesoria(rol.getAsesoriaEstudiante(), estudiante.getIdEstudiante());
        mostrarMensajeResultadoEJB(res);
    }
    
    public void guardarAsistenciaGrupalAsesoriaEstudiante(List<DtoParticipantesAsesoriaCE> listaEstudiantes){
        if(!listaEstudiantes.isEmpty()){
            listaEstudiantes.stream().forEach((t) -> {
                ResultadoEJB<Boolean> resCompruebaParticipacion = ejb.verificarParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
                if(resCompruebaParticipacion.getCorrecto()){
                    ejb.eliminarParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
                    ejb.asignaParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
                }else ejb.asignaParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
            });
            mostrarMensaje("Se ha realizado el pase de lista general correctamente");
            verificarParticipantes();
        }
    }
    
    public void eliminarAsistenciaGrupalAsesoriaEstudiante(List<DtoParticipantesAsesoriaCE> listaEstudiantes){
        if(!listaEstudiantes.isEmpty()){
            listaEstudiantes.stream().forEach((t) -> {
                ResultadoEJB<Boolean> resCompruebaParticipacion = ejb.verificarParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
                if(resCompruebaParticipacion.getCorrecto()){
                    ejb.eliminarParticipanteAsesoria(rol.getAsesoriaEstudiante(), t.getEstudiante().getEstudiante().getIdEstudiante());
                }
            });
            mostrarMensaje("Se eliminado el pase de lista general correctamente");
            verificarParticipantes();
        }
    }
    
    /*************************************************************************************************************************/
    /*********************************************** Llenado de listas *********************************************************/
    public void actualizarListadoAsesorias(){
        ResultadoEJB<List<AsesoriasEstudiantes>> resAsesorias = ejb.buscaAsesoriasEstudiantesPorPersonalEventoRegistro(rol.getPersonal().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
        if(resAsesorias.getCorrecto()){
            rol.setListaAsesoriasEstudiantes(resAsesorias.getValor());
        }else mostrarMensajeResultadoEJB(resAsesorias);
    }
    
    public void listarEstudiantesGrupo(){
        ResultadoEJB<List<DtoParticipantesAsesoriaCE>> resParticipantes = ejbRegistroAsesoriaTutoria.obtenerListaEstudiantes(rol.getGrupoSeleccionado());
        if(resParticipantes.getCorrecto()){
            rol.setListaEstudiantes(resParticipantes.getValor());
        }else mostrarMensajeResultadoEJB(resParticipantes);
    }
    
}
