package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCapturaCalificacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCapturaCalificacionAlineacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPresentacionCalificacionesReporte;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracionAlineacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadesCalificacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadesCalificacionAlineacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoVistaCalificaciones;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoVistaCalificacionestitulosTabla;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SegimientoAcadmicoSet;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaTareaIntegradora;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AccionesDeMejora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionEvidenciaInstrumento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;
import org.primefaces.model.charts.bar.BarChartModel;



/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class SegimientoAcademicoConsulta extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    SegimientoAcadmicoSet rol;

    @EJB EjbAsignacionIndicadoresCriterios ejb;
    @EJB EjbResultadosConfiguraciones configuraciones;
    @EJB EjbRegistroPlanEstudio erpe;
    @EJB EjbValidacionRol evr;
    @EJB EjbPropiedades ep;
    @EJB EjbAsistencias ea;
    @EJB EjbCapturaCalificaciones calificaciones;
    @EJB EjbCapturaTareaIntegradora tareaIntegradora;
    @EJB EjbPacker packer;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Getter    @Setter    private BarChartModel barModel2;
    Integer bt=0,bd=0,ri=0;
    List<BigDecimal> promedios;
    BigDecimal sumatoriaPromedios;
    List<DtoVistaCalificaciones> dvc;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init() {
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGIMIENTO_ACADEMICO);
            
            ResultadoEJB<Filter<PersonalActivo>> resValidacionDi = evr.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidacionEn = evr.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionSa = evr.validarSecretariaAcademica(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionDo = evr.validarTutor(logon.getPersonal().getClave());//validar si es director                        
            
            if (!resValidacionDi.getCorrecto() && !resValidacionEn.getCorrecto() && !resValidacionSa.getCorrecto() && !resValidacionDo.getCorrecto()) {
                rol.setMensajeV("El acceso solo está autorizado a Personla Académico.");
                mostrarMensajeResultadoEJB(resValidacionDi);
                mostrarMensajeResultadoEJB(resValidacionDo);
                mostrarMensajeResultadoEJB(resValidacionEn);
                mostrarMensajeResultadoEJB(resValidacionSa);
                return;
            }            
            
            Filter<PersonalActivo> filtroEn = resValidacionEn.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroDi = resValidacionDi.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroDo = resValidacionDo.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroSa = resValidacionSa.getValor();//se obtiene el filtro resultado de la validación   
                        
            PersonalActivo activoEn = filtroEn.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoDo = filtroDo.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoDi = filtroDi.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoSa = filtroSa.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
                        
            rol = new SegimientoAcadmicoSet(filtroEn, activoEn, activoEn.getAreaOperativa());
            rol.setEsEn(rol.tieneAcceso(activoEn));            
            if(rol.getEsEn()==null)rol.setEsEn(Boolean.FALSE);
            if (rol.getEsEn()) {
                rol = new SegimientoAcadmicoSet(filtroEn, activoEn, activoEn.getAreaOperativa());
                tieneAcceso=Boolean.TRUE;
                rol.setDirector(activoEn);
                rol.setTipoUser(1);
            }else{
                rol = new SegimientoAcadmicoSet(filtroDo, activoDo, activoDo.getAreaOperativa());
                rol.setEsDo(rol.tieneAcceso(activoDo));
                if(rol.getEsDo()==null)rol.setEsDo(Boolean.FALSE);
                if (rol.getEsDo()) {
                    rol = new SegimientoAcadmicoSet(filtroDo, activoDo, activoDo.getAreaOperativa());
                    tieneAcceso=Boolean.TRUE;
                    rol.setTipoUser(3);
                    rol.setDocente(activoDo);
                } else {
                    rol = new SegimientoAcadmicoSet(filtroDi, activoDi, activoDi.getAreaOperativa());
                    rol.setEsDi(rol.tieneAcceso(activoDi));
                    if(rol.getEsDi()==null)rol.setEsDi(Boolean.FALSE);
                    if (rol.getEsDi()) {
                        rol = new SegimientoAcadmicoSet(filtroDi, activoDo, activoDo.getAreaOperativa());
                        tieneAcceso=Boolean.TRUE;
                        rol.setDirector(activoEn);
                        rol.setTipoUser(1);
                    }else{
                        rol = new SegimientoAcadmicoSet(filtroSa, activoSa, activoSa.getAreaOperativa());
                        rol.setEsSa(rol.tieneAcceso(activoSa));
                        if(rol.getEsSa()==null)rol.setEsSa(Boolean.FALSE);
                        if (rol.getEsSa()) {
                            rol = new SegimientoAcadmicoSet(filtroSa, activoDo, activoDo.getAreaOperativa());
                            tieneAcceso=Boolean.TRUE;
                            rol.setAcademica(activoSa);
                            rol.setTipoUser(2);
                        } else  {
                            tieneAcceso=Boolean.FALSE;
                            mostrarMensajeNoAcceso();
                            return;
                        }
                    }
                }
            }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso           
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
              
            if(rol.getTipoUser().equals(3)){
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosTutor(rol.getDocente());
                if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
                rol.setPeriodos(resPeriodos.getValor());
            }else{
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
                if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
                rol.setPeriodos(resPeriodos.getValor());
            }
            periodoseleccionado();            
            rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan;            
            if (rol.getTipoUser().equals(3)) {
                resProgramaPlan= erpe.getProgramasEducativosTutorados(rol.getDocente().getPersonal().getClave());
                if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
                rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            } else {
                if (rol.getTipoUser().equals(2)) {
                    resProgramaPlan = erpe.getProgramasEducativostotal();
                } else {
                    resProgramaPlan = erpe.getProgramasEducativos(rol.getDirector());
                }
                if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
                rol.setAreaPlanEstudioMap(resProgramaPlan.getValor()); 
            }          
            
            planEstudioseleccionado();
            
            ResultadoEJB<List<Grupo>> resgrupos;
                
            if (rol.getTipoUser().equals(3)) {
                resgrupos = erpe.getListaGrupoPorTutorYPe(rol.getDocente(),rol.getPeriodo(),rol.getPlanEstudio().getIdPe());
            }else{
                resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            }
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);            
            rol.setGrupos(resgrupos.getValor()); 
            gruposeleccionado();
                 
            rol.setEventoActivo(new EventoEscolar(0, rol.getPeriodoActivo(), new Date(), "Captura_tarea_integradora", 0));
            rol.getEventoActivo().setFin(new Date());           
            
            existeAsignacion();
            
            if (rol.getPeriodoActivo() <= 56) {
                rol.setRender(Boolean.FALSE);
            } else {
                rol.setRender(Boolean.TRUE);
            }
            
            logon.setG2(0);
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "resultados pleneaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }  
    
    public void periodoseleccionado() {
        try {
            if (logon.getPer()== 0) {
                rol.setPeriodo(rol.getPeriodos().get(0));
            } else {
                rol.getPeriodos().forEach((t) -> {
                    if (Objects.equals(logon.getPer(), t.getPeriodo())) {
                        rol.setPeriodo(t);
                    }
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void planEstudioseleccionado() {
        try {
            if (logon.getPes()== 0) {
                rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
            } else {
                rol.getPlanesEstudios().forEach((t) -> {
                    if (Objects.equals(logon.getPes(), t.getIdPlanEstudio())) {
                        rol.setPlanEstudio(t);
                    }
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void gruposeleccionado() {
        try {
            if (logon.getG2()== 0) {
                rol.setGrupoSelec(rol.getGrupos().get(0));
            } else {
                rol.getGrupos().forEach((t) -> {
                    if (Objects.equals(logon.getG2(), t.getIdGrupo())) {
                        rol.setGrupoSelec(t);
                    }
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cambiarPeriodo() {
        if (rol.getPeriodo() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setGrupos(Collections.EMPTY_LIST);
            rol.setGrupoSelec(new Grupo());   
            return;
        }
        rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
        
        ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan;
        
        if (rol.getTipoUser().equals(3)) {
                resProgramaPlan= erpe.getProgramasEducativosTutorados(rol.getDocente().getPersonal().getClave());
                if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
                rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            } else {
                if (rol.getTipoUser().equals(2)) {
                    resProgramaPlan = erpe.getProgramasEducativostotal();
                } else {
                    resProgramaPlan = erpe.getProgramasEducativos(rol.getDirector());
                }
                if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
                rol.setAreaPlanEstudioMap(resProgramaPlan.getValor()); 
            }          
            
            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
            ResultadoEJB<List<Grupo>> resgrupos;
                
            if (rol.getTipoUser().equals(3)) {
                resgrupos = erpe.getListaGrupoPorTutorYPe(rol.getDocente(),rol.getPeriodo(),rol.getPlanEstudio().getIdPe());
            }else{
                resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            }
                
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);            
            rol.setGrupos(resgrupos.getValor()); 
            rol.setGrupoSelec(rol.getGrupos().get(0)); 
            logon.setPer(rol.getPeriodo().getPeriodo());        
            logon.setG2(0);
            logon.setPes(rol.getPlanEstudio().getIdPlanEstudio());
            Faces.redirect("controlEscolar/reportesMu/segimientoAcademico.xhtml");
        existeAsignacion();
    }
     
    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGrupos(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        ResultadoEJB<List<Grupo>> resgrupos;

        if (rol.getTipoUser().equals(3)) {
            resgrupos = erpe.getListaGrupoPorTutorYPe(rol.getDocente(), rol.getPeriodo(), rol.getPlanEstudio().getIdPe());
        } else {
            resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(), rol.getPeriodo());
        }
                
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);            
        rol.setGrupos(resgrupos.getValor()); 
        rol.setGrupoSelec(rol.getGrupos().get(0)); 
        logon.setG2(0);
        logon.setPes(rol.getPlanEstudio().getIdPlanEstudio());  
        Faces.redirect("controlEscolar/reportesMu/segimientoAcademico.xhtml");
        existeAsignacion();
    }    
    
   public void cambiarGrupo(ValueChangeEvent event) {
        rol.setGrupoSelec((Grupo) event.getNewValue());
        rol.setListaalumnoscas(new ArrayList<>());
        rol.setTitulos(new ArrayList<>());
        rol.setDvcs(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<Listaalumnosca>> rejb = erpe.getListaAlumnosPorGrupo(rol.getGrupoSelec());
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        rol.setListaalumnoscas(rejb.getValor());  
        logon.setG2(rol.getGrupoSelec().getIdGrupo());
        Faces.redirect("controlEscolar/reportesMu/segimientoAcademico.xhtml");
        existeAsignacion();
    }
    
    public String buscarPersonal(Integer clave) {
        try {            
            Personal p = new Personal();
            if (clave != null) {
                p = ejbPersonal.mostrarPersonalLogeado(clave);
                return p.getNombre();
            } else {
                return "Nombre del tutor";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void existeAsignacion() {
        rol.setTitulos(new ArrayList<>());
        rol.setDvcs(new ArrayList<>());
        ResultadoEJB<List<DtoCargaAcademica>> rejb = ejb.getCargaAcademicasPorGrupo(rol.getGrupoSelec(), rol.getPeriodo());
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        List<DtoCargaAcademica> dcas=rejb.getValor();

        if(!dcas.isEmpty()){
            dcas.forEach((t) -> {
                Boolean ti=Boolean.TRUE;
                if(t.getCargaAcademica().getTareaIntegradora() == null){
                    ti=Boolean.FALSE;
                }
            rol.getTitulos().add(new DtoVistaCalificacionestitulosTabla(t.getDocente().getPersonal().getNombre(),t.getMateria().getNombre(), t.getMateria().getUnidadMateriaList().size(), ti));
            });            
        }
     
        rol.getGrupoSelec().getEstudianteList().forEach((e) -> {
            String nomEs =  e.getAspirante().getIdPersona().getNombre()+" "+e.getAspirante().getIdPersona().getApellidoPaterno()+" "+e.getAspirante().getIdPersona().getApellidoMaterno();             
            if (!dcas.isEmpty()) {
                dvc= new ArrayList<>(); 
                dcas.forEach((c) -> { 
                    promedios= new ArrayList<>();
                    c.getCargaAcademica().getUnidadMateriaConfiguracionList().forEach((um) -> { 
                        if (rol.getPeriodoActivo() <= 56) {
                            ResultadoEJB<BigDecimal> resBig=calificaciones.promediarUnidadConsultaCal(crearCapturas(um, e));
                            if(!resBig.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
                            promedios.add(resBig.getValor());
                        }else{
                            ResultadoEJB<BigDecimal> resBig=calificaciones.promediarUnidadAlineacionConsultaCal(crearCapturasAlineacion(um, e));
                            if(!resBig.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
                            promedios.add(resBig.getValor());
                        }
                    }); 
                    BigDecimal ti=BigDecimal.ZERO; ;
                    Boolean tiv=Boolean.FALSE;
                
                    if(c.getCargaAcademica().getTareaIntegradora() != null){
                        tiv=Boolean.TRUE;
                        List<TareaIntegradoraPromedio> tis=c.getCargaAcademica().getTareaIntegradora().getTareaIntegradoraPromedioList().stream().filter(t-> Objects.equals(t.getEstudiante().getIdEstudiante(), e.getIdEstudiante())).collect(Collectors.toList());
                        if(!tis.isEmpty()){
                            TareaIntegradoraPromedio promedio=tis.get(0);
                            ti=BigDecimal.valueOf(promedio.getValor());
                        }
                    }
                    BigDecimal pordinario=BigDecimal.ZERO; 
                    ResultadoEJB<DtoEstudiante> resEst=packer.packEstudiante(e);      
                        if(resEst.getCorrecto()){
                            DtoEstudiante est=resEst.getValor();                        
                            if(rol.getPeriodoActivo()<=56){
                                ResultadoEJB<BigDecimal> res = tareaIntegradora.promediarAsignatura(getContenedor(c), c, est);
                                if(res.getCorrecto()){
                                    pordinario= res.getValor();
                                }else{
                                    mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", est.getPersona().getApellidoPaterno(), est.getPersona().getApellidoMaterno(), est.getPersona().getNombre(), est.getInscripcionActiva().getInscripcion().getMatricula()));
                                    pordinario= BigDecimal.ZERO;
                                }
                            }else{
                                ResultadoEJB<BigDecimal> res = tareaIntegradora.promediarAsignaturaAlineacion(getContenedorAlineacion(c), c, est);
                                if(res.getCorrecto()){
                                    pordinario= res.getValor();
                                }else{
                                    mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", est.getPersona().getApellidoPaterno(), est.getPersona().getApellidoMaterno(), est.getPersona().getNombre(), est.getInscripcionActiva().getInscripcion().getMatricula()));
                                    pordinario= BigDecimal.ZERO;
                                }
                            }
                        }else{
                            pordinario= BigDecimal.ZERO;
                        }
                    
                    List<CalificacionNivelacion> cns=c.getCargaAcademica().getCalificacionNivelacionList().stream().filter(t-> Objects.equals(t.getEstudiante().getIdEstudiante(), e.getIdEstudiante())).collect(Collectors.toList());
                    BigDecimal niv=BigDecimal.ZERO;   
                    BigDecimal pfinal=BigDecimal.ZERO;   
                    if(!cns.isEmpty()){
                        CalificacionNivelacion cn=cns.get(0);                            
                        niv= BigDecimal.valueOf(cn.getValor());
                        pfinal=BigDecimal.valueOf(cn.getValor());
                    }else{
                        pfinal=pordinario;   
                    }  
                    dvc.add(new DtoVistaCalificaciones(c.getMateria(), promedios, ti, pordinario, niv, pfinal,tiv));
                });
                String tipoEs="r";
                switch (e.getTipoEstudiante().getIdTipoEstudiante()){
                    case 1: tipoEs="Regular"; break;
                    case 2: tipoEs="Baja Temporal"; break;
                    case 3: tipoEs="Baja Definitiva"; break;
                    case 4: tipoEs="Egresado No Titulado"; break;
                    case 5: tipoEs="Reincorporacion"; break;
                }
                BigDecimal pf=BigDecimal.ZERO;
                pf=promedioCuatrimestral(dvc);
                rol.getDvcs().add(new DtoPresentacionCalificacionesReporte(e.getMatricula(), e.getTipoEstudiante().getIdTipoEstudiante(), nomEs, dvc, pf,tipoEs));
            }
        });
        Ajax.update("frm");
    }  
    
    public List<DtoCapturaCalificacionAlineacion.Captura> crearCapturasAlineacion(UnidadMateriaConfiguracion dca, Estudiante e) {
        List<DtoCapturaCalificacionAlineacion.Captura> cs= new ArrayList<>();
        dca.getUnidadMateriaConfiguracionEvidenciaInstrumentoList().forEach((u) -> {
                DtoUnidadConfiguracionAlineacion.Detalle d= new DtoUnidadConfiguracionAlineacion.Detalle(u, u.getEvidencia().getCriterio(), u.getEvidencia(), u.getInstrumento());
                List<CalificacionEvidenciaInstrumento> cal=u.getCalificacionEvidenciaInstrumentoList().stream().filter(est -> Objects.equals(est.getIdEstudiante().getIdEstudiante(), e.getIdEstudiante())).collect(Collectors.toList());
                if (cal.isEmpty()) {
                    cs.add(new DtoCapturaCalificacionAlineacion.Captura(d, new CalificacionEvidenciaInstrumento(0L)));
                } else {
                    cs.add(new DtoCapturaCalificacionAlineacion.Captura(d, cal.get(0)));
                }
            });    
        return cs;
    }
    
    public List<DtoCapturaCalificacion.Captura> crearCapturas(UnidadMateriaConfiguracion dca, Estudiante e) {
        List<DtoCapturaCalificacion.Captura> cs= new ArrayList<>();
        dca.getUnidadMateriaConfiguracionDetalleList().forEach((u) -> {
                DtoUnidadConfiguracion.Detalle d= new DtoUnidadConfiguracion.Detalle(u, u.getCriterio(), u.getIndicador());
                List<Calificacion> cal=u.getCalificacionList().stream().filter(est -> Objects.equals(est.getIdEstudiante().getIdEstudiante(), e.getIdEstudiante())).collect(Collectors.toList());
                if (cal.isEmpty()) {
                    cs.add(new DtoCapturaCalificacion.Captura(d, new Calificacion(0L)));
                } else {
                    cs.add(new DtoCapturaCalificacion.Captura(d, cal.get(0)));
                }
            });    
        return cs;
    }
    
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    public DtoUnidadesCalificacion getContenedor(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        if(rol.getEventoActivo()!= null){
            ResultadoEJB<DtoUnidadesCalificacion> resDtoUnidadesCalificacion = packer.packDtoUnidadesCalificacion(dtoCargaAcademica, getUnidades(dtoCargaAcademica), rol.getEventoActivo());
            if(!resDtoUnidadesCalificacion.getCorrecto()){
                mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
                return null;
            }
            rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
            return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        }else{
            mostrarMensaje("No existe evento activo para captura de tarea integradora y nivelación final. ");
            return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        }
    }
    
    public List<DtoUnidadConfiguracion> getUnidades(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);
        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = calificaciones.getConfiguraciones(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesMap().put(dtoCargaAcademica, resConfiguraciones.getValor());
        return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);
    }
    
    public DtoUnidadesCalificacionAlineacion getContenedorAlineacion(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionAlineacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica);
        if(rol.getEventoActivo()!= null){
            ResultadoEJB<DtoUnidadesCalificacionAlineacion> resDtoUnidadesCalificacion = packer.packDtoUnidadesCalificacionAlineacion(dtoCargaAcademica, getUnidadesAlineacion(dtoCargaAcademica), rol.getEventoActivo());
            if(!resDtoUnidadesCalificacion.getCorrecto()){
                mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
                return null;
            }
            rol.getDtoUnidadesCalificacionAlineacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
            return rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica);
        }else{
            mostrarMensaje("No existe evento activo para captura de tarea integradora y nivelación final. ");
            return rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica);
        }
    }
    
    public List<DtoUnidadConfiguracionAlineacion> getUnidadesAlineacion(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesAlineacionMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesAlineacionMap().get(dtoCargaAcademica);
        ResultadoEJB<List<DtoUnidadConfiguracionAlineacion>> resConfiguraciones = calificaciones.getConfiguracionesAlineacion(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesAlineacionMap().put(dtoCargaAcademica, resConfiguraciones.getValor());
        return  rol.getDtoUnidadConfiguracionesAlineacionMap().get(dtoCargaAcademica);
    }
    
    public BigDecimal promedioCuatrimestral(List<DtoVistaCalificaciones> vistaCal){
        sumatoriaPromedios =BigDecimal.ZERO;
        BigDecimal promedio=BigDecimal.ZERO;
        vistaCal.forEach((t) -> {
            sumatoriaPromedios=sumatoriaPromedios.add(t.getPromedioFinalN());            
        });  
        if(!sumatoriaPromedios.equals(promedio)){
            promedio=sumatoriaPromedios.divide(BigDecimal.valueOf(vistaCal.size()), 8, RoundingMode.HALF_DOWN);
        } 
        return promedio;
    }
}
