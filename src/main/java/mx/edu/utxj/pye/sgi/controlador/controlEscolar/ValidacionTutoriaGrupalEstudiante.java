/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionTutoriaGrupalRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistenciaTutoriaGrupalEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ValidacionTutoriaGrupalEstudiante extends ViewScopedRol implements Desarrollable{

    @Getter     @Setter                                     private                                     ValidacionTutoriaGrupalRolEstudiante                rol;
    @Getter     @Setter                                     private                                     Boolean                                             tieneAcceso = false;
    @EJB        EjbPropiedades                              ep;
    @EJB        EjbAsistenciaTutoriaGrupalEstudiante        ejb;
    @EJB        EjbPeriodoEventoRegistro                    ejbPeriodoEventoRegistro;
    @EJB        EjbPacker                                   packer;
    
    @Inject     LogonMB                                     logonMB;
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "tutorías grupales estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.TUTORIAS_GRUPALES_ESTUDIANTE);
                ResultadoEJB<Estudiante> resAcceso = ejb.validarEstudiante(logonMB.getCurrentUser());
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
                rol = new ValidacionTutoriaGrupalRolEstudiante(packer.packEstudianteGeneral(resAcceso.getValor()).getValor());
                tieneAcceso = rol.tieneAcceso(rol.getDtoEstudiante(), UsuarioTipo.ESTUDIANTE19);
                rol.setNivelRol(NivelRol.CONSULTA);
                ResultadoEJB<EventosRegistros> resEventoRegistro = ejbPeriodoEventoRegistro.getEventoRegistro();
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresPorParticipacionTutoriaGrupal(rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
                
                rol.setPeriodoActivo(ejbPeriodoEventoRegistro.getPeriodoEscolarActivo().getValor().getPeriodo());
                ResultadoEJB<Long> firmasPendientes = ejb.verificarTutoriasPendientesFirmas(rol.getPeriodoActivo(), rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
                if(firmasPendientes.getCorrecto()) rol.setFirmasPendiente(firmasPendientes.getValor());
                
                if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
                if(verificarInvocacionMenu()) return;
                if(!validarIdentificacion()) return;
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                if(!resEventoRegistro.getCorrecto()) mostrarMensajeResultadoEJB(resEventoRegistro);
                rol.setEventoRegistroActivo(resEventoRegistro.getValor());
                rol.setPeriodosConTutoriasGrupales(resPeriodos.getValor());
                initFiltros();
            }
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    /*********************************************** Inicializadores *********************************************************/
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarParticipanteTutoriaGrupal(){
        rol.setParticipantesTutoriaGrupal(new ParticipantesTutoriaGrupal());
    }
    
    public void inicializarListaParticipantesTutoriaGrupal(){
        rol.setListaParticipantesTutoriaGrupal(new ArrayList<>());
        rol.setListaParticipantesTutoriaGrupal(Collections.EMPTY_LIST);
    }
    
    public void initFiltros(){
        try {
            rol.setEventosPorPeriodo(ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActualEstudiante(rol.getPeriodosConTutoriasGrupales(), rol.getEventosPorPeriodo(), rol.getEventoRegistroActivo(), rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula()).getValor();
            if(entrada != null){
                rol.setPeriodosConTutoriasGrupales(entrada.getKey());
                rol.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            mostrarMensaje("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
        }
        cambiarPeriodo();
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Filtros *********************************************************/
    
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setListaParticipantesTutoriaGrupal(Collections.EMPTY_LIST);
            return;
        }
        rol.setEventosPorPeriodo(ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado()).getValor());
        cambiarEventoRegistro();
        inicializarParticipanteTutoriaGrupal();
        inicializarListaParticipantesTutoriaGrupal();
        actualizarListadoParticipanteTutoriaGrupal();
    }
    
    public void cambiarEventoRegistro(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            rol.setListaParticipantesTutoriaGrupal(Collections.EMPTY_LIST);
            return;
        }
        inicializarParticipanteTutoriaGrupal();
        inicializarListaParticipantesTutoriaGrupal();
        actualizarListadoParticipanteTutoriaGrupal();
    }
    
    /*************************************************************************************************************************/
    
    /*********************************************** Administración de datos *********************************************************/

    public void actualizarAceptacionAcuerdos(ValueChangeEvent event){
        if(event.getNewValue() instanceof String){
        ParticipantesTutoriaGrupal ptga = (ParticipantesTutoriaGrupal) event.getComponent().getAttributes().get("participanteTutoriaGrupal");    
        String aceptacionAcuerdos = (String) event.getNewValue();
        ptga.setAceptacionAcuerdos(aceptacionAcuerdos);
        actualizarParticipanteTutoriaGrupal(ptga);
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void verificarComentariosDesacuerdo() {
        if (!rol.getListaParticipantesTutoriaGrupal().isEmpty()) {
            rol.getListaParticipantesTutoriaGrupal().stream().forEach((t) -> {
                if (t.getAceptacionAcuerdos().equals("Asistí - No estoy de acuerdo") || t.getAceptacionAcuerdos().equals("No asistí - No estoy de acuerdo")) {
                    if (t.getComentarios().equals("Pendiente de registro") || t.getComentarios().equals("") || t.getComentarios() == null) {
                        rol.setComentarioPendiente(Boolean.TRUE);
                    } else {
                        rol.setComentarioPendiente(Boolean.FALSE);
                    }
                }
            });
        }
    } 
    
    public void actualizarComentarios(ValueChangeEvent event){
        if(event.getNewValue() instanceof String){
        ParticipantesTutoriaGrupal ptga = (ParticipantesTutoriaGrupal) event.getComponent().getAttributes().get("participanteTutoriaGrupal");
        String comentarios = (String) event.getNewValue();
        ptga.setComentarios(comentarios);
        actualizarParticipanteTutoriaGrupal(ptga);
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void actualizarParticipanteTutoriaGrupal(ParticipantesTutoriaGrupal participantesTutoriaGrupal){
        ResultadoEJB<ParticipantesTutoriaGrupal> res = ejb.editarAsistenciaParticipacionTutoriaGrupal(participantesTutoriaGrupal);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListadoParticipanteTutoriaGrupal();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    
    /*************************************************************************************************************************/
    
    /*********************************************** Llenado de listas *********************************************************/
    public void actualizarListadoParticipanteTutoriaGrupal() {
        ResultadoEJB<List<ParticipantesTutoriaGrupal>> res = ejb.getListaParticipantesTutoriaGrupalPorEventoRegistro(rol.getEventoSeleccionado(),rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
        if (res.getCorrecto()) {
            rol.setListaParticipantesTutoriaGrupal(res.getValor());
            verificarComentariosDesacuerdo();
        } else {
            rol.setListaParticipantesTutoriaGrupal(Collections.EMPTY_LIST);
        }
    }
    
}
