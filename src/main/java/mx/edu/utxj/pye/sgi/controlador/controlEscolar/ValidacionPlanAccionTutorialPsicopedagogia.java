/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanAccionTutorial;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTutoriaIndividualCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionPlanAccionTutorialRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionTutores;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaTutoriasAsesorias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CordinadoresTutores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasIndividuales;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.ParticipanteTutoriaGrupalAcuerdos;
import mx.edu.utxj.pye.sgi.enums.PlanAccionTutorialEstado;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ValidacionPlanAccionTutorialPsicopedagogia extends ViewScopedRol implements Desarrollable{
    @Getter     @Setter                         ValidacionPlanAccionTutorialRolPsicopedagogia       rol;
    @Getter     Boolean                         tieneAcceso = false;
    @Getter     private                         Boolean                                             cargado = false;
    
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbPropiedades                  ep;
    @EJB        EjbValidacionRol                ejbValidacion;
    @EJB        EjbConsultaTutoriasAsesorias    ejbConsultaTutoriasAsesorias;
    @EJB        EjbRegistroAsesoriaEstudiante   ejbRegistroAsesoriaEstudiante;
    @EJB        EjbAsignacionTutores            ejbAsignacionTutores;
    
    @Inject     LogonMB                         logonMB;
    @Inject     Caster                          caster;
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento asesorias tutorias";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_TUTORIAS_ASESORIAS);
            
            ResultadoEJB<Filter<PersonalActivo>> resAccesoDirector = ejbValidacion.validarDirector(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEncargado = ejbValidacion.validarEncargadoDireccion(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoCoordinadorTutores = ejbValidacion.validarCoordinadorTutor(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbValidacion.validarConsultaPlanAccionTutorialCompleto(logonMB.getPersonal().getClave());
            
            
            if(!resAccesoDirector.getCorrecto() && !resAccesoEncargado.getCorrecto() && !resAccesoCoordinadorTutores.getCorrecto() && !resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
           
            Filter<PersonalActivo> filtro = resAccesoDirector.getValor();
            PersonalActivo psicopedagogia = filtro.getEntity();
            rol = new ValidacionPlanAccionTutorialRolPsicopedagogia(filtro, psicopedagogia);
            tieneAcceso = rol.tieneAcceso(psicopedagogia);
            
            if(!tieneAcceso){
                rol.setFiltro(resAccesoEncargado.getValor());
                tieneAcceso = rol.tieneAcceso(psicopedagogia);
            }
            
            if(!tieneAcceso){
                if(resAccesoCoordinadorTutores.getCorrecto()){
                    rol.setFiltro(resAccesoCoordinadorTutores.getValor());
                    tieneAcceso = rol.tieneAcceso(psicopedagogia);
                }
            }
            
            if(!tieneAcceso){
                if(resAcceso.getCorrecto()){
                    rol.setFiltro(resAcceso.getValor());
                    tieneAcceso = rol.tieneAcceso(psicopedagogia);
                    rol.setModuloAsignado(true);
                }
            }
            
            if(!tieneAcceso){return;}
            rol.setPsicopedagogia(psicopedagogia);
            ResultadoEJB<EventosRegistros> resEventoRegistro = ejb.verificarEvento();
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos;
            if(resAccesoCoordinadorTutores.getCorrecto()){
                resPeriodos = ejb.getPeriodosConCoordinadoresTutor(rol.getPsicopedagogia());
            }else{
                resPeriodos = ejb.getPeriodosConPlanAccionTutorialGeneralPsicopedagogia();
            }
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos);tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEventoRegistro.getCorrecto()) mostrarMensajeResultadoEJB(resEventoRegistro);
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.setEventoRegistroActivo(resEventoRegistro.getValor());
            rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConPlanAccionTutorial(resPeriodos.getValor());
            cambiarPeriodo();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarListaDtoPlanesAccionTutorial(){
        rol.setListaDtoPlanAccionTutorial(new ArrayList<>());
        rol.setListaDtoPlanAccionTutorial(Collections.EMPTY_LIST);
    }
    
    public void inicializarFuncionesTutor(){
        rol.setListaFuncionesTutor(new ArrayList<>());
        rol.setListaFuncionesTutor(Collections.EMPTY_LIST);
    }
    
    public void inicializarSesiones(){
        rol.setListaSesionesGrupalesTutorias(new ArrayList<>());
        rol.setListaSesionesGrupalesTutorias(Collections.EMPTY_LIST);
    }
    
    public void inicializarEventosRegistros(){
        rol.setEventosPorPeriodo(new ArrayList<>());
        rol.setEventosPorPeriodo(Collections.EMPTY_LIST);
    }
    
    public void inicializarTutoriasGrupales(){
        rol.setListaTutoriasGrupales(new ArrayList<>());
        rol.setListaTutoriasGrupales(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaTutoriasIndividuales(){
        rol.setListaDtoTutoriasIndividuales(new ArrayList<>());
        rol.setListaDtoTutoriasIndividuales(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaAsesorias(){
        rol.setListaAsesorias(new ArrayList<>());
        rol.setListaAsesorias(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaAsesoriasGenerales(){
        rol.setListaAsesoriasGenerales(new ArrayList<>());
        rol.setListaAsesoriasGenerales(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaDocentes(){
        rol.setDocentes(new ArrayList<>());
        rol.setDocentes(Collections.EMPTY_LIST);
    }
    
    /*********************************************** Filtros *********************************************************/
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo seleccionado");
            rol.setProgramasEducativos(Collections.EMPTY_LIST);
            inicializarListaDtoPlanesAccionTutorial();
            inicializarEventosRegistros();
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos;
        if(rol.getModuloAsignado()){
            resProgramasEducativos = ejb.getProgramasEducativosConPlanAccionTutorialPsicopedagogiaAsignado(rol.getPeriodoSeleccionado());
        }else{
            resProgramasEducativos = ejb.getProgramasEducativosConPlanAccionTutorialPsicopedagogia(rol.getPeriodoSeleccionado(), rol.getPsicopedagogia(),rol.getModuloAsignado());
        }
        if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
        else rol.setProgramasEducativos(resProgramasEducativos.getValor());
        consultarEventosRegistros();
        cambiarProgramaEducativo();
        validaCoordinadorTutores();
    }
    
    public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay un programa educativo seleccionado");
            inicializarListaDtoPlanesAccionTutorial();
            return;
        }
        actualizarPlanesAccionTutorial();
    }
    
    public void validaCoordinadorTutores(){
        ResultadoEJB<CordinadoresTutores> cordinadorTutores = ejbAsignacionTutores.buscarCordinadorTutorActual(rol.getPeriodoSeleccionado().getPeriodo(), rol.getPsicopedagogia().getAreaOperativa().getAreaSuperior());
        if (!cordinadorTutores.getCorrecto()) {
            rol.setValidaCT(false);
        }else{
            if(cordinadorTutores.getValor().getPersonal() == rol.getPsicopedagogia().getPersonal().getClave()){
                rol.setValidaCT(true);
            }else{
                rol.setValidaCT(false);
            }
        }
    }
    
    /*********************************************** Administración de datos *********************************************************/
    
    public void consultarFunciones(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        ResultadoEJB<List<FuncionesTutor>> res = ejb.buscaFuncionesTutor(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial());
        if(res.getCorrecto()){
            inicializarFuncionesTutor();
            rol.setListaFuncionesTutor(res.getValor());
        }else{
            inicializarFuncionesTutor();
        }
        Ajax.update("frmFunciones");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalFuncionesPlanAccionTutorial').show();");
    }
    
    public void consultarSesiones(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        ResultadoEJB<List<SesionesGrupalesTutorias>> res = ejb.buscaSesionesGrupalesXPlanAT(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().getGrupo());
        if(res.getCorrecto()){
            inicializarSesiones();
            rol.setListaSesionesGrupalesTutorias(res.getValor());
        }else{
            inicializarSesiones();
        }
        Ajax.update("frmSesiones");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalSesionesPlanAccionTutorial').show();");
    }
    
    public void validarPlanAccionTutorial(DtoPlanAccionTutorial dtoPAT) {
        rol.setDtoPlanAccionTutorial(dtoPAT);
        if(rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().getEstatus().equals(PlanAccionTutorialEstado.VALIDADO.getLabel())){
            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.EN_OBSERVACIONES.getLabel());
            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setComentariosDirector("Pendiente de registrar observaciones");
        }else{
            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.VALIDADO.getLabel());
            rol.getDtoPlanAccionTutorial().getPlanAccionTutorial().setComentariosDirector("Plan de Accion Tutoríal Validado");
        }  
        ResultadoEJB<PlanAccionTutorial> res = ejb.validarPlanDeAccionTutorial(dtoPAT.getPlanAccionTutorial());
        if (res.getCorrecto()) {
            actualizarPlanesAccionTutorial();
            mostrarMensajeResultadoEJB(res);
        } else {
            actualizarPlanesAccionTutorial();
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void actualizarComentariosObservacionesDirector(ValueChangeEvent event){
        DtoPlanAccionTutorial pat = (DtoPlanAccionTutorial) event.getComponent().getAttributes().get("pat");
        String comentarioObservaciones = (String) event.getNewValue();
        if(comentarioObservaciones == null || "".equals(comentarioObservaciones)){
            pat.getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.ENVIADO_PARA_REVISION.getLabel());
            pat.getPlanAccionTutorial().setComentariosDirector(null);
        }else{
            pat.getPlanAccionTutorial().setEstatus(PlanAccionTutorialEstado.EN_OBSERVACIONES.getLabel());
            mostrarMensaje("Se han enviado los comentarios u observaciones al tutor");
            pat.getPlanAccionTutorial().setComentariosDirector(comentarioObservaciones);
        }
        actualizarPAT(pat);
    }
    
    public void actualizarPAT(DtoPlanAccionTutorial dtoPAT) {
        rol.setDtoPlanAccionTutorial(dtoPAT);
        ResultadoEJB<PlanAccionTutorial> res = ejb.validarPlanDeAccionTutorial(dtoPAT.getPlanAccionTutorial());
        if (res.getCorrecto()) {
            actualizarPlanesAccionTutorial();
            mostrarMensajeResultadoEJB(res);
        } else {
            actualizarPlanesAccionTutorial();
            mostrarMensajeResultadoEJB(res);
        }
    }

    public void actualizarPlanesAccionTutorial(){
        ResultadoEJB<List<DtoPlanAccionTutorial>> res = ejb.getPlanesAccionTutorialPeriodoArea(rol.getPeriodoSeleccionado(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            inicializarListaDtoPlanesAccionTutorial();
            rol.setListaDtoPlanAccionTutorial(res.getValor());
        }else{
            inicializarListaDtoPlanesAccionTutorial();
        }
    }
    
    public void consultarEventosRegistros(){
        ResultadoEJB<List<EventosRegistros>> eventosRegistros = ejb.getEventosRegistroPorPeriodo(rol.getPeriodoSeleccionado());
        if(eventosRegistros.getCorrecto()){
            inicializarEventosRegistros();
            rol.setEventosPorPeriodo(eventosRegistros.getValor());
        }else{
            inicializarEventosRegistros();
        }
    }
    
    ////////////////////////////////////  Concentrado de tutorías y asesorías por mes //////////////////////////////////////////
    
    public void cambiarEventoConcentradoTutoriasAsesoriasMensual(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            return;
        }
        Ajax.update("frmConcentradoProgramaEducativo");
    }
    
    // Tutorias Grupales Concentrado Mensual
    
    public Integer noTutoriasGrupalesConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        List<SesionesGrupalesTutorias> sesionesGrupales = new ArrayList<>();
        dtoPat.getPlanAccionTutorial().getSesionesGrupalesTutoriasList().stream().forEach((t) -> {
            sesionesGrupales.add(t);
        });
        List<TutoriasGrupales> tutoriasGrupales = new ArrayList<>();
        sesionesGrupales.stream().forEach((sg) -> {
            sg.getTutoriasGrupalesList().stream().forEach((tg) -> {
                if(tg.getEventoRegistro() == eventoRegistro.getEventoRegistro())
                    tutoriasGrupales.add(tg);
            });
        });
        if(tutoriasGrupales.isEmpty()){
            return 0;
        }else{
            return tutoriasGrupales.size();
        }
    }
    
    public Integer noAsistentesMujeresTutoriaGrupalConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        List<SesionesGrupalesTutorias> sesionesGrupales = new ArrayList<>();
        dtoPat.getPlanAccionTutorial().getSesionesGrupalesTutoriasList().stream().forEach((t) -> {
            sesionesGrupales.add(t);
        });
        List<TutoriasGrupales> tutoriasGrupales = new ArrayList<>();
        sesionesGrupales.stream().forEach((sg) -> {
            sg.getTutoriasGrupalesList().stream().forEach((tg) -> {
                tutoriasGrupales.add(tg);
            });
        });
        List<ParticipantesTutoriaGrupal> participantes = new ArrayList<>();
        tutoriasGrupales.stream().forEach((tg) -> {
            if (tg.getEventoRegistro() == eventoRegistro.getEventoRegistro()) {
                tg.getParticipantesTutoriaGrupalList().stream().forEach((pe) -> {
                    participantes.add(pe);
                });
            }
        });
        List<ParticipantesTutoriaGrupal> estudiantesHombres = new ArrayList<>();
        participantes.stream().forEach((p) -> {
            if(p.getEstudiante1().getAspirante().getIdPersona().getGenero() == 1 && p.getAsistencia()){
                estudiantesHombres.add(p);
            }
        });
        if(estudiantesHombres.isEmpty()){
            return 0;
        }else{
            return estudiantesHombres.size();
        }
    }
    
    public Integer noAsistentesHombresTutoriaGrupalConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        List<SesionesGrupalesTutorias> sesionesGrupales = new ArrayList<>();
        dtoPat.getPlanAccionTutorial().getSesionesGrupalesTutoriasList().stream().forEach((t) -> {
            sesionesGrupales.add(t);
        });
        List<TutoriasGrupales> tutoriasGrupales = new ArrayList<>();
        sesionesGrupales.stream().forEach((sg) -> {
            sg.getTutoriasGrupalesList().stream().forEach((tg) -> {
                tutoriasGrupales.add(tg);
            });
        });
        List<ParticipantesTutoriaGrupal> participantes = new ArrayList<>();
        tutoriasGrupales.stream().forEach((tg) -> {
            if (tg.getEventoRegistro() == eventoRegistro.getEventoRegistro()) {
                tg.getParticipantesTutoriaGrupalList().stream().forEach((pe) -> {
                    participantes.add(pe);
                });
            }
        });
        List<ParticipantesTutoriaGrupal> estudiantesHombres = new ArrayList<>();
        participantes.stream().forEach((p) -> {
            if(p.getEstudiante1().getAspirante().getIdPersona().getGenero() == 2 && p.getAsistencia()){
                estudiantesHombres.add(p);
            }
        });
        if(estudiantesHombres.isEmpty()){
            return 0;
        }else{
            return estudiantesHombres.size();
        }
    }
    
    // Tutorias Individuales Concentrado Mensual
    
    public Integer noTutoriasIndividualesConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        List<Estudiante> estudiante = new ArrayList<>();
        dtoPat.getPlanAccionTutorial().getGrupo().getEstudianteList().stream().forEach((e) -> {
            estudiante.add(e);
        });
        List<TutoriasIndividuales> tutoriasIndividuales = new ArrayList<>();
        estudiante.stream().forEach((est) -> {
            est.getTutoriasIndividualesList().stream().forEach((ti) -> {
                if(ti.getEventoRegistro() == eventoRegistro.getEventoRegistro()){
                    tutoriasIndividuales.add(ti);
                }
            });
        });
        if(tutoriasIndividuales.isEmpty()){
            return 0;
        }else{
            return tutoriasIndividuales.size();
        }
    }
    
    public Integer noAsistentesMujeresTutoriaIndividualConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        List<Estudiante> estudiante = new ArrayList<>();
        dtoPat.getPlanAccionTutorial().getGrupo().getEstudianteList().stream().forEach((e) -> {
            estudiante.add(e);
        });
        List<TutoriasIndividuales> tutoriasIndividuales = new ArrayList<>();
        estudiante.stream().forEach((est) -> {
            est.getTutoriasIndividualesList().stream().forEach((ti) -> {
                if(ti.getEventoRegistro() == eventoRegistro.getEventoRegistro() && ti.getEstudiante().getAspirante().getIdPersona().getGenero() == 1){
                    tutoriasIndividuales.add(ti);
                }
            });
        });
        
        if(tutoriasIndividuales.isEmpty()){
            return 0;
        }else{
            return tutoriasIndividuales.size();
        }
    }
    
    public Integer noAsistentesHombresTutoriaIndividualConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        List<Estudiante> estudiante = new ArrayList<>();
        dtoPat.getPlanAccionTutorial().getGrupo().getEstudianteList().stream().forEach((e) -> {
            estudiante.add(e);
        });
        List<TutoriasIndividuales> tutoriasIndividuales = new ArrayList<>();
        estudiante.stream().forEach((est) -> {
            est.getTutoriasIndividualesList().stream().forEach((ti) -> {
                if(ti.getEventoRegistro() == eventoRegistro.getEventoRegistro() && ti.getEstudiante().getAspirante().getIdPersona().getGenero() == 2){
                    tutoriasIndividuales.add(ti);
                }
            });
        });
        
        if(tutoriasIndividuales.isEmpty()){
            return 0;
        }else{
            return tutoriasIndividuales.size();
        }
    }
    
    // Asesorias Grupales Concentrado Mensual
    
    public Integer noAsesoriasGrupalesConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            return resultadoAsesorias.getValor().size();
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesMujeresAsesoriaGrupalConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesorias.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 1){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesHombresAsesoriaGrupalConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesorias.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 2){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    // Asesorias Individuales Concentrado Mensual
    
    public Integer noAsesoriasIndividualesConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            return resultadoAsesorias.getValor().size();
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesMujeresAsesoriaIndividualConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesorias.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 1){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesHombresAsesoriaIndividualConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesorias.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 2){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    // Asesorias Individuales Generales Concentrado Mensual
    
    public Integer noAsesoriasIndividualesGeneralesConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesEstudiantesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            return resultadoAsesoriasEstudiantes.getValor().size();
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesMujeresAsesoriaGeneralConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesEstudiantesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesoriasEstudiantes.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 1){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesHombresAsesoriaGeneralConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesEstudiantesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesoriasEstudiantes.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 2){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    
    // Asesorias Grupales Generales Concentrado Mensual
    
    public Integer noAsesoriasGrupalesGeneralesConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesEstudiantesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            return resultadoAsesoriasEstudiantes.getValor().size();
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesMujeresAsesoriaGrupalGeneralConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesEstudiantesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesoriasEstudiantes.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 1){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    public Integer noAsistentesHombresAsesoriaGrupalGeneralConcentradoMensual(DtoPlanAccionTutorial dtoPat, EventosRegistros eventoRegistro){
        if(dtoPat == null || eventoRegistro == null){
            return 0;
        }
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesEstudiantesPorGrupoEventoRegistro(dtoPat.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            List<Estudiante> estudiantes = new ArrayList<>();
            resultadoAsesoriasEstudiantes.getValor().stream().forEach((as) -> {
                as.getEstudianteList().stream().forEach((est) -> {
                    estudiantes.add(est);
                });
            });
            List<Estudiante> estudianteSecundaria = new ArrayList<>();
            estudiantes.stream().forEach((t) -> {
                if(t.getAspirante().getIdPersona().getGenero() == 2){
                    estudianteSecundaria.add(t);
                }
            });
            if(estudianteSecundaria.isEmpty()){
                return 0;
            }else{
                return estudianteSecundaria.size();
            }
        }else{
            return 0;
        }
    }
    
    ////////////////////////////////////  Consulta de Tutorías Grupales  //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoTutoriasGrupales(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        inicializarTutoriasGrupales();
        consultarTutoriasPorPlanEventoRegistro(dtoPAT);
        Ajax.update("frmTutoriasGrupales");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalTutoriasGrupales').show();");
    }
    
    public void consultarTutoriasPorPlanEventoRegistro(DtoPlanAccionTutorial dtoPAT){
        ResultadoEJB<List<TutoriasGrupales>> resultadoTutoriasGrupales = ejbConsultaTutoriasAsesorias.buscaTutoriasGrupalesPorPlanAccionTutorialEventoRegistro(dtoPAT.getPlanAccionTutorial(),rol.getEventoSeleccionado());
        if(resultadoTutoriasGrupales.getCorrecto()){
            inicializarTutoriasGrupales();
            rol.setListaTutoriasGrupales(resultadoTutoriasGrupales.getValor());
        }else{
            inicializarTutoriasGrupales();
        }
    }
    
    public Boolean verficarTutoriasGrupales(DtoPlanAccionTutorial dtoPAT){
        if(rol.getPeriodoActivo().equals(rol.getPeriodoSeleccionado().getPeriodo())){
            ResultadoEJB<List<TutoriasGrupales>> resultadoTutoriasGrupales = ejbConsultaTutoriasAsesorias.buscaTutoriasGrupalesPorPlanAccionTutorialEventoRegistro(dtoPAT.getPlanAccionTutorial(),rol.getEventoRegistroActivo());
            return resultadoTutoriasGrupales.getCorrecto();
        }else{
            return true;
        }
    }
    
    public void cambiarEventoRegistro(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            inicializarTutoriasGrupales();
            return;
        }
        consultarTutoriasPorPlanEventoRegistro(rol.getDtoPlanAccionTutorial());
    }
    
    public Double comprobarPorcentaje(SesionesGrupalesTutorias sesionGrupal) {
        if (!sesionGrupal.getTutoriasGrupalesList().isEmpty()) {
            TutoriasGrupales tutoriaGrupal = sesionGrupal.getTutoriasGrupalesList().get(0);
            if (!tutoriaGrupal.getParticipantesTutoriaGrupalList().isEmpty()) {
                List<ParticipantesTutoriaGrupal> part = tutoriaGrupal.getParticipantesTutoriaGrupalList();
                List<ParticipantesTutoriaGrupal> partFaltantes = new ArrayList<>();
                part.stream().forEach((t) -> {
                    if (!t.getAceptacionAcuerdos().equals(ParticipanteTutoriaGrupalAcuerdos.PENDIENTE_DE_REGISTRO.getLabel())) {
                        if(!t.getEstudiante1().getTipoEstudiante().getIdTipoEstudiante().equals(2) || !t.getEstudiante1().getTipoEstudiante().getIdTipoEstudiante().equals(3))
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
    
    public Integer contarAsistentesTutoriaGrupalMujeres(TutoriasGrupales tutoriaGrupal){
        if(tutoriaGrupal.getParticipantesTutoriaGrupalList().isEmpty()) return 0;
        List<ParticipantesTutoriaGrupal> estudiantes = tutoriaGrupal.getParticipantesTutoriaGrupalList();
        List<ParticipantesTutoriaGrupal> estudiantesMujeres = new ArrayList<>();
        estudiantes.stream().forEach((t) -> {
            if( t.getEstudiante1().getAspirante().getIdPersona().getGenero() == 1 && t.getAsistencia()){
                estudiantesMujeres.add(t);
            }
        });
        if(estudiantesMujeres.isEmpty()){
            return 0;
        }else{
            return estudiantesMujeres.size();
        }
    }
    
    public Integer contarAsistentesTutoriaGrupalHombres(TutoriasGrupales tutoriaGrupal){
        if(tutoriaGrupal.getParticipantesTutoriaGrupalList().isEmpty()) return 0;
        List<ParticipantesTutoriaGrupal> estudiantes = tutoriaGrupal.getParticipantesTutoriaGrupalList();
        List<ParticipantesTutoriaGrupal> estudiantesHombres = new ArrayList<>();
        estudiantes.stream().forEach((t) -> {
            if( t.getEstudiante1().getAspirante().getIdPersona().getGenero() == 2 && t.getAsistencia()){
                estudiantesHombres.add(t);
            }
        });
        if(estudiantesHombres.isEmpty()){
            return 0;
        }else{
            return estudiantesHombres.size();
        }
    }
    
    ////////////////////////////////////  Consulta de Tutorías Individuales //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoTutoriasIndividuales(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        inicializarListaTutoriasIndividuales();
        consultarTutoriasIndividualesPorGrupoEventoRegistro(dtoPAT);
        Ajax.update("frmTutoriasIndividuales");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalTutoriasIndividuales').show();");
        Ajax.oncomplete();
    }
    
    public void consultarTutoriasIndividualesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT){
        ResultadoEJB<List<DtoTutoriaIndividualCE>> resultadoTutoriasIndividuales = ejb.buscaTutoriasIndividuales(dtoPAT.getPlanAccionTutorial().getGrupo(), rol.getEventoSeleccionado());
        if(resultadoTutoriasIndividuales.getCorrecto()){
            inicializarListaTutoriasIndividuales();
            rol.setListaDtoTutoriasIndividuales(resultadoTutoriasIndividuales.getValor());
        }else{
            inicializarListaTutoriasIndividuales();
        }
    }
    
    public Boolean verficarTutoriasIndividuales(DtoPlanAccionTutorial dtoPAT){
        if(rol.getPeriodoActivo().equals(rol.getPeriodoSeleccionado().getPeriodo())){
        ResultadoEJB<List<DtoTutoriaIndividualCE>> resultadoTutoriasIndividuales = ejb.buscaTutoriasIndividuales(dtoPAT.getPlanAccionTutorial().getGrupo(), rol.getEventoRegistroActivo());
        return resultadoTutoriasIndividuales.getCorrecto();
        }else{
            return true;
        }
    }
    
    public void cambiarEventoRegistroTutoriasIndividuales(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            inicializarListaTutoriasIndividuales();
            return;
        }
        consultarTutoriasIndividualesPorGrupoEventoRegistro(rol.getDtoPlanAccionTutorial());
    }
    
    ////////////////////////////////////  Consulta de Asesorias Grupales //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasGrupales(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        inicializarListaAsesorias();
        consultarAsesoriasGrupalesPorDocenteEventoRegistro(dtoPAT);
        Ajax.update("frmAsesorias");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesorias').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasGrupalesPorDocenteEventoRegistro(DtoPlanAccionTutorial dtoPAT){
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorDocenteEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesorias(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasGrupales(DtoPlanAccionTutorial dtoPAT){
        if (rol.getPeriodoActivo().equals(rol.getPeriodoSeleccionado().getPeriodo())) {
            ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorDocenteEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
            return resultadoAsesorias.getCorrecto();
        } else {
            return true;
        }
    }
    
    public void cambiarEventoRegistroAsesoriasGrupales(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            inicializarListaAsesorias();
            return;
        }
        consultarAsesoriasGrupalesPorDocenteEventoRegistro(rol.getDtoPlanAccionTutorial());
    }
    
    ////////////////////////////////////  Consulta de Asesorias Individuales //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasIndividuales(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        inicializarListaAsesorias();
        consultarAsesoriasIndividualesPorDocenteEventoRegistro(dtoPAT);
        Ajax.update("frmAsesorias");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesorias').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasIndividualesPorDocenteEventoRegistro(DtoPlanAccionTutorial dtoPAT){
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesPorDocenteEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesorias(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasIndividuales(DtoPlanAccionTutorial dtoPAT){
        if (rol.getPeriodoActivo().equals(rol.getPeriodoSeleccionado().getPeriodo())) {
            ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesPorDocenteEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
            return resultadoAsesorias.getCorrecto();
        } else {
            return true;
        }
    }
    
    public void cambiarEventoRegistroAsesoriasIndividuales(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            inicializarListaAsesorias();
            return;
        }
        consultarAsesoriasIndividualesPorDocenteEventoRegistro(rol.getDtoPlanAccionTutorial());
    }
    
    
//    Función para conteo de asistentes en asesorías individuales y grupales
    
    public Integer contarAsistentesAsesoriaMujeres(Asesoria asesoria){
        if(asesoria.getEstudianteList().isEmpty()) return 0;
        List<Estudiante> estudiantes = asesoria.getEstudianteList();
        List<Estudiante> estudiantesMujeres = new ArrayList<>();
        estudiantes.stream().forEach((t) -> {
            if( t.getAspirante().getIdPersona().getGenero() == 1 ){
                estudiantesMujeres.add(t);
            }
        });
        if(estudiantesMujeres.isEmpty()){
            return 0;
        }else{
            return estudiantesMujeres.size();
        }
    }
    
    public Integer contarAsistentesAsesoriaHombres(Asesoria asesoria){
        if(asesoria.getEstudianteList().isEmpty()) return 0;
        List<Estudiante> estudiantes = asesoria.getEstudianteList();
        List<Estudiante> estudiantesHombres = new ArrayList<>();
        estudiantes.stream().forEach((t) -> {
            if( t.getAspirante().getIdPersona().getGenero() == 2 ){
                estudiantesHombres.add(t);
            }
        });
        if(estudiantesHombres.isEmpty()){
            return 0;
        }else{
            return estudiantesHombres.size();
        }
    }
    
    ////////////////////////////////////  Consulta de Asesorias Generales Grupales //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasGeneralesGrupales(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        inicializarListaAsesoriasGenerales();
        consultarAsesoriasGeneralesGrupalesPorDocenteEventoRegistro(dtoPAT);
        Ajax.update("frmAsesoriasGenerales");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasGenerales').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasGeneralesGrupalesPorDocenteEventoRegistro(DtoPlanAccionTutorial dtoPAT){
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesGrupalPorPersonalEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            inicializarListaAsesoriasGenerales();
            rol.setListaAsesoriasGenerales(resultadoAsesoriasEstudiantes.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasGeneralesGrupales(DtoPlanAccionTutorial dtoPAT){
        if (rol.getPeriodoActivo().equals(rol.getPeriodoSeleccionado().getPeriodo())) {
            ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesGrupalPorPersonalEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
            return resultadoAsesoriasEstudiantes.getCorrecto();
        } else {
            return true;
        }
    }
    
    public void cambiarEventoRegistroAsesoriasGeneralesGrupales(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            inicializarListaAsesoriasGenerales();
            return;
        }
        consultarAsesoriasGeneralesGrupalesPorDocenteEventoRegistro(rol.getDtoPlanAccionTutorial());
    }
    
    ////////////////////////////////////  Consulta de Asesorias Generales Individuales //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasIndividualesGeneralesGrupales(DtoPlanAccionTutorial dtoPAT){
        rol.setDtoPlanAccionTutorial(dtoPAT);
        inicializarListaAsesoriasGenerales();
        consultarAsesoriasIndividualesGeneralesPorDocenteEventoRegistro(dtoPAT);
        Ajax.update("frmAsesoriasGenerales");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasGenerales').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasIndividualesGeneralesPorDocenteEventoRegistro(DtoPlanAccionTutorial dtoPAT){
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesIndividualPorPersonalEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            inicializarListaAsesoriasGenerales();
            rol.setListaAsesoriasGenerales(resultadoAsesoriasEstudiantes.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasIndividualesGenerales(DtoPlanAccionTutorial dtoPAT){
        if (rol.getPeriodoActivo().equals(rol.getPeriodoSeleccionado().getPeriodo())) {
            ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesIndividualPorPersonalEventoRegistro(dtoPAT.getTutor().getPersonal().getClave(), rol.getEventoSeleccionado().getEventoRegistro());
            return resultadoAsesoriasEstudiantes.getCorrecto();
        } else {
            return true;
        }
    }
    
    public void cambiarEventoRegistroAsesoriasIndividualesGenerales(){
        if(rol.getEventoSeleccionado() == null){
            mostrarMensaje("No hay evento de registro seleccionado");
            inicializarListaAsesoriasGenerales();
            return;
        }
        consultarAsesoriasIndividualesGeneralesPorDocenteEventoRegistro(rol.getDtoPlanAccionTutorial());
    }
    
    public Integer contarAsistentesAsesoriaGeneralMujeres(AsesoriasEstudiantes asesoria){
        if(asesoria.getEstudianteList().isEmpty()) return 0;
        List<Estudiante> estudiantes = asesoria.getEstudianteList();
        List<Estudiante> estudiantesMujeres = new ArrayList<>();
        estudiantes.stream().forEach((t) -> {
            if( t.getAspirante().getIdPersona().getGenero() == 1 ){
                estudiantesMujeres.add(t);
            }
        });
        if(estudiantesMujeres.isEmpty()){
            return 0;
        }else{
            return estudiantesMujeres.size();
        }
    }
    
    public Integer contarAsistentesAsesoriaGeneralHombres(AsesoriasEstudiantes asesoria){
        if(asesoria.getEstudianteList().isEmpty()) return 0;
        List<Estudiante> estudiantes = asesoria.getEstudianteList();
        List<Estudiante> estudiantesHombres = new ArrayList<>();
        estudiantes.stream().forEach((t) -> {
            if( t.getAspirante().getIdPersona().getGenero() == 2 ){
                estudiantesHombres.add(t);
            }
        });
        if(estudiantesHombres.isEmpty()){
            return 0;
        }else{
            return estudiantesHombres.size();
        }
    }
    
    public void onRowToggle(ToggleEvent event) {
        if (event.getVisibility() == Visibility.VISIBLE) {
            AsesoriasEstudiantes asesoriaEstudiante = (AsesoriasEstudiantes) event.getComponent().getAttributes().get("asesoriaEstudiante");
            List<String> listaSecundaria = new ArrayList<>();
            if(!asesoriaEstudiante.getEstudianteList().isEmpty()){
                asesoriaEstudiante.getEstudianteList().stream().forEach((t) -> {
                    listaSecundaria.add(caster.getNombreCarrera(t.getCarrera()).getNombre() + " " + t.getGrupo().getGrado() + "° " + t.getGrupo().getLiteral());
                });
                rol.setListaSecundariaProgramasEducativos(listaSecundaria.stream().distinct().collect(Collectors.toList()));
            }else{
                rol.setListaSecundariaProgramasEducativos(Collections.EMPTY_LIST);
            }
        }else{
            rol.setListaSecundariaProgramasEducativos(Collections.EMPTY_LIST);
        }
    }
    
////////////    Consulta de asesorías y asesorias generales por grupo y evento de registro seleccionado
    
    ////////////////////////////////////  Consulta de Asesorias Grupales por grupo //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasGrupalesPorEvento(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegisto){
        inicializarListaAsesorias();
        consultarAsesoriasGrupalesPorGrupoEventoRegistro(dtoPAT, eventoRegisto);
        Ajax.update("frmAsesoriasDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasGrupalesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro){
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesorias(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasGrupalesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro) {
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        return resultadoAsesorias.getCorrecto();
    }
    
    ////////////////////////////////////  Consulta de Asesorias Individuales por grupo //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasIndividualesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegisto){
        inicializarListaAsesorias();
        consultarAsesoriasIndividualesPorGrupoEventoRegistro(dtoPAT, eventoRegisto);
        Ajax.update("frmAsesoriasDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasIndividualesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro){
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesorias(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasIndividualesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro) {
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasInvidualesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        return resultadoAsesorias.getCorrecto();
    }
    
    ////////////////////////////////////  Consulta de Asesorias Generales Grupales por grupo //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasGrupalesGeneralesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegisto){
        inicializarListaAsesoriasGenerales();
        consultarAsesoriasGrupalesGeneralesPorGrupoEventoRegistro(dtoPAT, eventoRegisto);
        Ajax.update("frmAsesoriasGeneralesDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasGeneralesDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasGrupalesGeneralesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro){
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesEstudiantesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesoriasGenerales();
            rol.setListaAsesoriasGenerales(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasGrupalesGeneralesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro) {
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesEstudiantesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        return resultadoAsesorias.getCorrecto();
    }

    ////////////////////////////////////  Consulta de Asesorias Generales Individuales por grupo //////////////////////////////////////////
    
    public void abrirCuadroDeDialogoAsesoriasIndividualesGeneralesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegisto){
        inicializarListaAsesorias();
        consultarAsesoriasIndividualesGeneralesPorGrupoEventoRegistro(dtoPAT, eventoRegisto);
        Ajax.update("frmAsesoriasGeneralesDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasGeneralesDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
    public void consultarAsesoriasIndividualesGeneralesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro){
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesEstudiantesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesoriasGenerales();
            rol.setListaAsesoriasGenerales(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarRegistroDeAsesoriasIndividualesGeneralesPorGrupoEventoRegistro(DtoPlanAccionTutorial dtoPAT, EventosRegistros eventoRegistro) {
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesEstudiantesPorGrupoEventoRegistro(dtoPAT.getPlanAccionTutorial().getGrupo(), eventoRegistro.getEventoRegistro());
        return resultadoAsesorias.getCorrecto();
    }
    
    
    ////////////////////////////////////  Consulta de Asesorias registradas por el Docente sin PAT //////////////////////////////////////////
    
    public List<PersonalActivo> completeDocentes(String pista){
        ResultadoEJB<List<PersonalActivo>> res = ejb.buscarDocentePAT(pista,rol.getPsicopedagogia());
        if(res.getCorrecto()){
            return  res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.EMPTY_LIST;
        }
    }
    
    public void seleccionarDocenteAsesorias(ValueChangeEvent event){
        if(event.getNewValue() instanceof PersonalActivo){
            PersonalActivo personalActivo = (PersonalActivo) event.getNewValue();
            List<PersonalActivo> lista = new ArrayList<>();
            inicializarListaDocentes();
            lista.add(personalActivo);
            rol.setDocentes(lista);
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
//    Asesorias grupales por docente sin pat evento registro
    public void abrirCuadroDeDialogoAsesoriasGrupalesDocenteSinPat(EventosRegistros eventoRegistro){
        inicializarListaAsesorias();
        consultarAsesoriasGrupalesPorDocenteSinPatEventoRegistro(eventoRegistro);
        Ajax.update("frmAsesoriasDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
//    Asesorias individuales por docente sin pat evento registro
    public void abrirCuadroDeDialogoAsesoriasIndividualesDocenteSinPat(EventosRegistros eventoRegistro){
        inicializarListaAsesorias();
        consultarAsesoriasIndividualesPorDocenteSinPatEventoRegistro(eventoRegistro);
        Ajax.update("frmAsesoriasDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
    ////////////////////////////////////  Consulta de Asesorias Grupales Docente sin PAT //////////////////////////////////////////
    
    public void consultarAsesoriasGrupalesPorDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorDocenteEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesorias(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarAsesoriasGrupalesDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasGrupalesPorDocenteEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        return resultadoAsesorias.getCorrecto();
    }
    
    ////////////////////////////////////  Consulta de Asesorias Individuales Docente sin PAT //////////////////////////////////////////
    
    public void consultarAsesoriasIndividualesPorDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesPorDocenteEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesorias.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesorias(resultadoAsesorias.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarAsesoriasIndividualesDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<Asesoria>> resultadoAsesorias = ejbConsultaTutoriasAsesorias.buscaAsesoriasIndividualesPorDocenteEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        return resultadoAsesorias.getCorrecto();
    }
    
////////////////////////////////////  Consulta de Asesorias Generales Docente sin PAT //////////////////////////////////////////
    public void abrirCuadroDeDialogoAsesoriasGrupalesGeneralesDocenteSinPat(EventosRegistros eventoRegistro){
        inicializarListaAsesoriasGenerales();
        consultarAsesoriasGrupalesGeneralesPorDocenteSinPatEventoRegistro(eventoRegistro);
        Ajax.update("frmAsesoriasGeneralesDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasGeneralesDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
    public void abrirCuadroDeDialogoAsesoriasIndividualesGeneralesDocenteSinPat(EventosRegistros eventoRegistro){
        inicializarListaAsesoriasGenerales();
        consultarAsesoriasIndividualesGeneralesPorDocenteSinPatEventoRegistro(eventoRegistro);
        Ajax.update("frmAsesoriasGeneralesDocenteSinPat");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAsesoriasGeneralesDocenteSinPat').show();");
        Ajax.oncomplete();
    }
    
////////////////////////////////////  Consulta de Asesorias Generales Grupales Docente sin PAT //////////////////////////////////////////
    public void consultarAsesoriasGrupalesGeneralesPorDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesGrupalPorPersonalEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesoriasGenerales(resultadoAsesoriasEstudiantes.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarAsesoriasGrupalesGeneralesDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesGrupalPorPersonalEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        return resultadoAsesoriasEstudiantes.getCorrecto();
    }
    
    
////////////////////////////////////  Consulta de Asesorias Individuales Grupales Docente sin PAT //////////////////////////////////////////
    public void consultarAsesoriasIndividualesGeneralesPorDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesIndividualPorPersonalEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        if(resultadoAsesoriasEstudiantes.getCorrecto()){
            inicializarListaAsesorias();
            rol.setListaAsesoriasGenerales(resultadoAsesoriasEstudiantes.getValor());
        }else{
            inicializarListaAsesorias();
        }
    }
    
    public Boolean verficarAsesoriasIndividualesGeneralesDocenteSinPatEventoRegistro(EventosRegistros eventoRegistro){
        if(rol.getPeriodoSeleccionado() == null) return false;
        ResultadoEJB<List<AsesoriasEstudiantes>> resultadoAsesoriasEstudiantes = ejbRegistroAsesoriaEstudiante.buscaAsesoriasEstudiantesIndividualPorPersonalEventoRegistro(rol.getDocente().getPersonal().getClave(), eventoRegistro.getEventoRegistro());
        return resultadoAsesoriasEstudiantes.getCorrecto();
    }
    
}
