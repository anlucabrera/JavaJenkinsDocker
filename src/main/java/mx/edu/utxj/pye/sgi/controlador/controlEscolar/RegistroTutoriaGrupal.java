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
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoParticipantesTutoriaGrupalCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTutoriaGrupalCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroTutoriaGrupalRolTutor;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.ParticipanteTutoriaGrupalAcuerdos;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroTutoriaGrupal extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = -3878336964509182820L;
    @Getter     @Setter                         private             RegistroTutoriaGrupalRolTutor   rol;
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    @EJB        EjbCasoCritico                  ejbCritico;
    @EJB        EjbPacker                       ejbPacker;
    @Inject     LogonMB                         logon;
    @Inject     Caster                          caster;
    @Getter     Boolean                         tieneAcceso = false;

    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "tutorías grupales";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_TUTORIA_GRUPAL);

            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTutor(logon.getPersonal().getClave());
            if(!resAcceso.getCorrecto()) {mostrarMensajeResultadoEJB(resAcceso);return;}
            
            rol = new RegistroTutoriaGrupalRolTutor(resAcceso.getValor());
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
            initFiltros();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void actualizarEstudiantes(){
        listarEstudiantes();
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
        cambiarPeriodo();
    }
    
    public void inicializarTutoriaGrupal(){
        rol.setTutoriaGrupal(new TutoriasGrupales());
        rol.setFecha(new Date());
        rol.setHoraInicio(new Date());
        rol.setHoraCierre(new Date());
        TutoriasGrupales tutoriasGrupales = new TutoriasGrupales();
        tutoriasGrupales.setSesionGrupal(rol.getSesionGrupalSeleccionada());
        tutoriasGrupales.setEventoRegistro(rol.getEventoSeleccionado().getEventoRegistro());
        tutoriasGrupales.setOrdenDia(rol.getSesionGrupalSeleccionada().getActividadProgramada());
        rol.setTutoriaGrupal(tutoriasGrupales);
        
        Estudiante estudiantePye = new Estudiante();
        DtoEstudianteComplete estudiante = new DtoEstudianteComplete(estudiantePye, "");
        rol.setEstudianteJefeGrupoSeleccionado(estudiante);
        
        rol.setTutoriaGrupalSeleccionada(new DtoTutoriaGrupalCE(new TutoriasGrupales(), new DtoEstudianteComplete(new Estudiante(), "")));
    }
    
    public void inicializarListaTutoriasGrupales(){
        rol.setListaDtoTuriasGrupales(new ArrayList<>());
        rol.setListaDtoTuriasGrupales(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaEstudiantes(){
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setListaEstudiantes(Collections.EMPTY_LIST);
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
        if (!resListadoGruposTutores.getCorrecto()) {
            mostrarMensajeResultadoEJB(resListadoGruposTutores);
        } else {
            rol.setListadoGruposTutor(resListadoGruposTutores.getValor());
        }

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
        actualizarListadoTutoriasGrupales();
        inicializarListaEstudiantes();
    }
    
    public void cambiarSesiones(){
        if(rol.getSesionGrupalSeleccionada() == null){
            mostrarMensaje("No hay sesion grupal de tutoría seleccionada.");
            return;
        }
        inicializarTutoriaGrupal();
        inicializarListaTutoriasGrupales();
        actualizarListadoTutoriasGrupales();
        inicializarListaEstudiantes();
    }
    
    public void cambiarSesionesDate(){
        if(rol.getSesionGrupalSeleccionada() == null){
            mostrarMensaje("No hay sesion grupal de tutoría seleccionada.");
            return;
        }
        inicializarTutoriaGrupal();
        inicializarListaTutoriasGrupales();
        actualizarListadoTutoriasGrupales();
    }
    
    /*********************************************** Administración de datos *********************************************************/
    public Date parsearFechaAHora(Date hora) {
        try {
            DateFormat formatoHora = new SimpleDateFormat("HH:mm a");
            String horaCadena = formatoHora.format(hora);
            Date horaParseada = formatoHora.parse(horaCadena);
            return horaParseada;
        } catch (ParseException e) {
            mostrarMensajeResultadoEJB(ResultadoEJB.crearErroneo(1, "Ha ocurrido un error al momento de convertir la hora de la tutoría, la tutoría grupal se ha guardado con la fecha y hora actual, favor de notificar al administrador del sistema ", e, null));
            return new Date();
        }
    }
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            rol.getTutoriaGrupal().setJefeGrupo(ejb.buscaEstudiante(estudiante.getEstudiantes().getIdEstudiante()).getValor());
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void guardarTutoriaGrupal(){
        rol.getTutoriaGrupal().setFecha(rol.getFecha());
        rol.getTutoriaGrupal().setHoraInicio(parsearFechaAHora(rol.getHoraInicio()));
        rol.getTutoriaGrupal().setHoraCierre(parsearFechaAHora(rol.getHoraCierre()));
        ResultadoEJB<TutoriasGrupales> res = ejb.guardaTutoriaGrupal(rol.getTutoriaGrupal());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaGrupal();
            actualizarListadoTutoriasGrupales();
        }else{
            mostrarMensajeResultadoEJB(res);
            inicializarTutoriaGrupal();
        }
    }
    
    public void actualizarJefeGrupo(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete)event.getNewValue();
            TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
            ResultadoEJB<Estudiante> resEstudiante = ejb.buscaEstudiante(estudiante.getEstudiantes().getIdEstudiante());
            if(resEstudiante.getCorrecto()){
                tutoria.setJefeGrupo(resEstudiante.getValor());
                actualizarTutoriaGrupal(tutoria);
            }else{
                mostrarMensajeResultadoEJB(resEstudiante);
            }
        }else mostrarMensaje("El valor seleccionado no es valído.");
    }
    
    public void actualizarFecha(ValueChangeEvent event){
        TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
        Date fecha = (Date) event.getNewValue();
        tutoria.setFecha(fecha);
        actualizarTutoriaGrupal(tutoria);
    }
    
    public void actualizarHoraInicio(ValueChangeEvent event){
        TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
        Date horaInicio = (Date) event.getNewValue();
        tutoria.setHoraInicio(parsearFechaAHora(horaInicio));
        actualizarTutoriaGrupal(tutoria);
    }
    
    public void actualizarHoraCierre(ValueChangeEvent event){
        TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
        Date horaCierre = (Date) event.getNewValue();
        tutoria.setHoraCierre(parsearFechaAHora(horaCierre));
        actualizarTutoriaGrupal(tutoria);
    }
    
    public void actualizarOrdenDia(ValueChangeEvent event){
        TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
        String ordenDia = (String) event.getNewValue();
        tutoria.setOrdenDia(ordenDia);
        actualizarTutoriaGrupal(tutoria);
    }
    
    public void actualizarAcuerdos(ValueChangeEvent event){
        TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
        String acuerdos = (String) event.getNewValue();
        tutoria.setAcuerdos(acuerdos);
        actualizarTutoriaGrupal(tutoria);
    }
    
    public void actualizarObservaciones(ValueChangeEvent event){
        TutoriasGrupales tutoria = (TutoriasGrupales) event.getComponent().getAttributes().get("tutoriaGrupal");
        String observaciones = (String) event.getNewValue();
        tutoria.setObservaciones(observaciones);
        actualizarTutoriaGrupal(tutoria);
    }
    
    public void actualizarTutoriaGrupal(TutoriasGrupales tutoriaG){
        ResultadoEJB<Boolean> res = ejb.editaTutoriaGrupal(tutoriaG);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListadoTutoriasGrupales();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void eliminarTutoriaGrupal(Integer tutoriaGrupal){
        ResultadoEJB<Boolean> res = ejb.eliminaTutoriaGrupal(tutoriaGrupal);
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            actualizarListadoTutoriasGrupales();
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void guardarParticipanteTutoriaGrupal(ValueChangeEvent event){
        ParticipantesTutoriaGrupal participante = (ParticipantesTutoriaGrupal) event.getComponent().getAttributes().get("participante");
        ResultadoEJB<ParticipantesTutoriaGrupal> res = ejb.editaParticipanteTutoriaGrupal(participante);
        mostrarMensajeResultadoEJB(res);
    }
    
    public void guardarParticipantesGeneralTutoriaGrupal(List<DtoParticipantesTutoriaGrupalCE> listaParticipantesTutoriaGrupal){
        if(!listaParticipantesTutoriaGrupal.isEmpty()){
            listaParticipantesTutoriaGrupal.stream().forEach((t) -> {
                ResultadoEJB<List<ParticipantesTutoriaGrupal>> resCompruebaParticipacion = ejb.buscaParticipanteTutoriaGrupal(rol.getTutoriaGrupalSeleccionada().getTutoriaGrupal().getTutoriaGrupal(), t.getEstudiante().getEstudiante().getIdEstudiante());
                if(resCompruebaParticipacion.getCorrecto()){
                    ejb.guardarAsistenciaParticipanteTutoriaGrupalGeneral(t.getParticipanteTutoriaGrupal());
                }
            });
            mostrarMensaje("Se ha realizado el pase de lista general correctamente");
        }
    }
    
    public void eliminarParticipantesGeneralTutoriaGrupal(List<DtoParticipantesTutoriaGrupalCE> listaParticipantesTutoriaGrupal){
        if(!listaParticipantesTutoriaGrupal.isEmpty()){
            listaParticipantesTutoriaGrupal.stream().forEach((t) -> {
                ResultadoEJB<List<ParticipantesTutoriaGrupal>> resCompruebaParticipacion = ejb.buscaParticipanteTutoriaGrupal(rol.getTutoriaGrupalSeleccionada().getTutoriaGrupal().getTutoriaGrupal(), t.getEstudiante().getEstudiante().getIdEstudiante());
                if(resCompruebaParticipacion.getCorrecto()){
                    ejb.eliminarAsistenciaParticipanteTutoriaGrupalGeneral(t.getParticipanteTutoriaGrupal());
                }
            });
            mostrarMensaje("Se ha eliminado el pase de lista general correctamente");
        }
    }
    
    /*********************************************** Llenado de listas *********************************************************/
    public void actualizarListadoTutoriasGrupales(){
        ResultadoEJB<List<TutoriasGrupales>> listaTutorias = ejb.buscaTutoriasGrupalesPorSesionEventoRegistro(rol.getSesionGrupalSeleccionada(), rol.getEventoSeleccionado());
        if (listaTutorias.getCorrecto()) {
            List<DtoTutoriaGrupalCE> listaDto = new ArrayList<>();
            if (!listaTutorias.getValor().isEmpty()) {
                listaTutorias.getValor().forEach((t) -> {
                    listaDto.add(new DtoTutoriaGrupalCE(
                            t, 
                            caster.dtoEstudianteAutocomplete(t.getJefeGrupo().getIdEstudiante()))
                    );
                });
                rol.setListaDtoTuriasGrupales(listaDto);
            }
        }else{
            rol.setListaDtoTuriasGrupales(Collections.EMPTY_LIST);
        }
    }
    
    public void listarEstudiantes() {
        if (rol.getTutoriaGrupalSeleccionada() != null) {
            ResultadoEJB<List<DtoParticipantesTutoriaGrupalCE>> res = ejb.obtenerListaEstudiantesTutoriaGrupal(rol.getGrupoTutorSeleccionado().getGrupo(), rol.getTutoriaGrupalSeleccionada().getTutoriaGrupal());
            if (res.getCorrecto()) {
                res.getValor().forEach((t) -> {
                    ResultadoEJB<List<DtoCasoCritico>> lista = ejbCritico.identificarPorEsdudiante(t.getEstudiante().getEstudiante());
                    if (lista.getCorrecto()) {
                        t.setListaCasosCriticos(lista.getValor());
                    }
                });
                rol.setListaEstudiantes(res.getValor());
            }else{
                rol.setListaEstudiantes(Collections.EMPTY_LIST);
            }
        }
    }
    
    public Double comprobarPorcentaje(SesionesGrupalesTutorias sesionGrupal) {
        if (!sesionGrupal.getTutoriasGrupalesList().isEmpty()) {
            TutoriasGrupales tutoriaGrupal = sesionGrupal.getTutoriasGrupalesList().get(0);
            if (!tutoriaGrupal.getParticipantesTutoriaGrupalList().isEmpty()) {
                List<ParticipantesTutoriaGrupal> part = tutoriaGrupal.getParticipantesTutoriaGrupalList();
                List<ParticipantesTutoriaGrupal> partFaltantes = new ArrayList<>();
                part.removeIf(tipoEstudiante -> tipoEstudiante.getEstudiante1().getTipoEstudiante().getIdTipoEstudiante().equals((short)2) || tipoEstudiante.getEstudiante1().getTipoEstudiante().getIdTipoEstudiante().equals((short)3));
                part.stream().forEach((t) -> {
                    if (!t.getAceptacionAcuerdos().equals(ParticipanteTutoriaGrupalAcuerdos.PENDIENTE_DE_REGISTRO.getLabel())) {    
                        partFaltantes.add(t);
                    }
                });
                double participantesDouble = part.size();
                double partFDouble = partFaltantes.size();
                Double suma = partFDouble / participantesDouble;
                Double porcentaje = suma * 100D;
                return (double)Math.round(porcentaje * 100d) / 100d;
            }else{
                return 0.0D;
            }
        } else {
            return 0.0D;
        }
    }
    
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(pista, rol.getPeriodoSeleccionado().getPeriodo(), rol.getGrupoTutorSeleccionado().getGrupo().getGrado(), rol.getGrupoTutorSeleccionado().getGrupo().getLiteral(), rol.getGrupoTutorSeleccionado().getGrupo().getIdPe());
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    public void listarCasosCriticos(Estudiante estudiante){
        ResultadoEJB<List<DtoCasoCritico>> casosCriticos = ejbCritico.identificarPorEsdudiante(estudiante);
        rol.setEstudiante(estudiante);
        List<String> listaSecundaria = new ArrayList<>();
        if (casosCriticos.getCorrecto()) {
            if (!casosCriticos.getValor().isEmpty()) {
                rol.setListaCasosCriticos(casosCriticos.getValor());
                rol.getListaCasosCriticos().stream().forEach((t) -> {
                    listaSecundaria.add(t.toLabel(t));
                });
                rol.setListaSecundaria(listaSecundaria);
//                System.err.println("Cantidad: " + rol.getListaSecundaria().size());
                Ajax.oncomplete("skin();");
                Ajax.update("frmCasosCriticos");
                Ajax.oncomplete("PF('modalCasosCriticos').show();");
            }
        } else {
            rol.setListaCasosCriticos(Collections.EMPTY_LIST);
            rol.setListaSecundaria(Collections.EMPTY_LIST);
            Ajax.oncomplete("skin();");
            Ajax.update("frmCasosCriticos");
            Ajax.oncomplete("PF('modalCasosCriticos').show();");
        }
    }
    
    public void abriDialogoRegistroCasoCritico(){
        ResultadoEJB<DtoEstudiante> dtoEstudiante = ejbPacker.packEstudianteGeneral(rol.getEstudiante());
        if(dtoEstudiante.getCorrecto()){
            rol.setDtoEstudiante(dtoEstudiante.getValor());
            ResultadoEJB<DtoCasoCritico> generarNuevo = ejbCritico.generarNuevoDesdeTutoriaGrupal(rol.getDtoEstudiante(), CasoCriticoTipo.SISTEMA_TUTORIA_GRUPAL, rol.getDocenteLogueado().getPersonal().getClave());
            if(generarNuevo.getCorrecto()){
                rol.setDtoCasoCritico(generarNuevo.getValor());
                Ajax.oncomplete("skin();");
                Ajax.update("frmRegistroCasoCritico");
                Ajax.oncomplete("PF('modalRegistroCasoCritico').show();");
                mostrarMensajeResultadoEJB(generarNuevo);
            }else mostrarMensajeResultadoEJB(generarNuevo);
        }        
    }
    
    public void guardaCasoCritico(){
        
        if(rol.getDtoCasoCritico().getCasoCritico().getDescripcion() == null || rol.getDtoCasoCritico().getCasoCritico().getDescripcion().trim().isEmpty()){
            mostrarMensaje("Debe ingresar una descripción del caso crítico");
            Ajax.oncomplete("PF('modalRegistroCasoCritico').show();");
            return;
        }
        ResultadoEJB<DtoCasoCritico> registrar = ejbCritico.registrarTG(rol.getDtoCasoCritico());
//        System.out.println("registrar = " + registrar);
        if(registrar.getCorrecto()){
            ejbCritico.registrarTG(rol.getDtoCasoCritico());
            listarCasosCriticos(rol.getDtoEstudiante().getInscripcionActiva().getInscripcion());
//            System.out.println("rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico() = " + rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico());
        }else mostrarMensajeResultadoEJB(registrar);
        
    }
    
    public void eliminarCasoCritico(){
        if (rol.getDtoCasoCritico() == null) {
            mostrarMensaje("No se puede eliminar un caso crítico nulo");
        } else {
            ResultadoEJB<Boolean> eliminar = ejbCritico.eliminar(rol.getDtoCasoCritico());
            listarCasosCriticos(rol.getDtoEstudiante().getInscripcionActiva().getInscripcion());
            mostrarMensajeResultadoEJB(eliminar);
        }
    }
    
    public List<CasoCriticoTipo> getListaCasoCriticoTipos(){
        return CasoCriticoTipo.Lista();
    }
    
}
