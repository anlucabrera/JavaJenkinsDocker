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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.DecimalFormat;
import java.util.Objects;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInformePlaneaciones;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoResultadosCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ResultadosPlaneacionesMultipleConsulta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AccionesDeMejora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;



/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class ResultadosPlaneacionesConsulta extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    ResultadosPlaneacionesMultipleConsulta rol;

    @EJB EjbAsignacionIndicadoresCriterios ejb;
    @EJB EjbResultadosConfiguraciones configuraciones;
    @EJB EjbRegistroPlanEstudio erpe;
    @EJB EjbValidacionRol evr;
    @EJB EjbPropiedades ep;
    @EJB EjbAsistencias ea;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Getter    @Setter    private BarChartModel barModel2;
    Integer bt=0,bd=0,ri=0;

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
            setVistaControlador(ControlEscolarVistaControlador.RESULTADOS_PLANEACIONES);
            
            ResultadoEJB<Filter<PersonalActivo>> resValidacionDi = evr.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidacionEn = evr.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionSa = evr.validarSecretariaAcademica(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionDo = evr.validarDocente(logon.getPersonal().getClave());//validar si es director                        
            
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
                        
            rol = new ResultadosPlaneacionesMultipleConsulta(filtroEn, activoEn, activoEn.getAreaOperativa());
            rol.setEsEn(rol.tieneAcceso(activoEn));            
            if(rol.getEsEn()==null)rol.setEsEn(Boolean.FALSE);
            if (rol.getEsEn()) {
                rol = new ResultadosPlaneacionesMultipleConsulta(filtroEn, activoEn, activoEn.getAreaOperativa());
                tieneAcceso=Boolean.TRUE;
                rol.setDirector(activoEn);
                rol.setTipoUser(1);
            }else{
                rol = new ResultadosPlaneacionesMultipleConsulta(filtroDo, activoDo, activoDo.getAreaOperativa());
                rol.setEsDo(rol.tieneAcceso(activoDo));
                if(rol.getEsDo()==null)rol.setEsDo(Boolean.FALSE);
                if (rol.getEsDo()) {
                    rol = new ResultadosPlaneacionesMultipleConsulta(filtroDo, activoDo, activoDo.getAreaOperativa());
                    tieneAcceso=Boolean.TRUE;
                    rol.setTipoUser(3);
                    rol.setDocente(activoDo);
                } else {
                    rol = new ResultadosPlaneacionesMultipleConsulta(filtroDi, activoDi, activoDi.getAreaOperativa());
                    rol.setEsDi(rol.tieneAcceso(activoDi));
                    if(rol.getEsDi()==null)rol.setEsDi(Boolean.FALSE);
                    if (rol.getEsDi()) {
                        rol = new ResultadosPlaneacionesMultipleConsulta(filtroDi, activoDo, activoDo.getAreaOperativa());
                        tieneAcceso=Boolean.TRUE;
                        rol.setDirector(activoEn);
                        rol.setTipoUser(1);
                    }else{
                        rol = new ResultadosPlaneacionesMultipleConsulta(filtroSa, activoSa, activoSa.getAreaOperativa());
                        rol.setEsSa(rol.tieneAcceso(activoSa));
                        if(rol.getEsSa()==null)rol.setEsSa(Boolean.FALSE);
                        if (rol.getEsSa()) {
                            rol = new ResultadosPlaneacionesMultipleConsulta(filtroSa, activoDo, activoDo.getAreaOperativa());
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
              
            if(!rol.getTipoUser().equals(3)){
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
                if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
                
                rol.setPeriodos(resPeriodos.getValor());
                rol.setPeriodo(ea.getPeriodoActual());
                rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
                ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan;
                if (rol.getTipoUser().equals(2)) {
                    resProgramaPlan = erpe.getProgramasEducativostotal();
                } else {
                    resProgramaPlan = erpe.getProgramasEducativos(rol.getDirector());
                }
                if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
                
                rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());           
                rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
                ResultadoEJB<List<Grupo>> resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
                if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
                
                rol.setGrupos(resgrupos.getValor()); 
                rol.setGrupoSelec(rol.getGrupos().get(0));
                ResultadoEJB<List<DtoCargaAcademica>> resCarga = ea.getCargaAcademicasPorGrupo(rol.getGrupoSelec().getIdGrupo(), rol.getPeriodo());            
                if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
                
                rol.setCargas(resCarga.getValor());
                rol.setCarga(resCarga.getValor().get(0));            
            }else{
                llenaListasDo();
            }
            
            rol.setAccionesDeMejora(new AccionesDeMejora());
            rol.setFechaInpresion(new Date());
            existeAsignacion();
            if (rol.getPeriodoActivo() <= 56) {
                rol.setRender(Boolean.FALSE);
            } else {
                rol.setRender(Boolean.TRUE);
            }
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "resultados pleneaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void llenaListasDo() {
        rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos2 = ejb.getPeriodosCargaAcademica(rol.getDocente(), rol.getPeriodoActivo());
        if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
        rol.setPeriodos(resPeriodos.getValor());
        rol.setPeriodo(ejb.getPeriodoActual());
            
        ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodo());
        ResultadoEJB<List<DtoCargaAcademica>> resCarga2 = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodos().get(0));
            
        if(!resCarga.getCorrecto() || !resCarga2.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);            
        if (resCarga.getValor().isEmpty() && resCarga2.getValor().isEmpty()) {
            tieneAcceso = Boolean.FALSE;
            if(!resPeriodos2.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos2);                
            if (!resPeriodos2.getValor().isEmpty()) {
                rol.setPeriodos(resPeriodos2.getValor());
                tieneAcceso = Boolean.TRUE;
            }
        }
            
        resCarga = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodo());
        resCarga2 = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodos().get(0));

        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        if (!resCarga.getValor().isEmpty()) {
            rol.setCargas(resCarga.getValor());
            rol.setCarga(resCarga.getValor().get(0));
        } else {
            rol.setCargas(resCarga2.getValor());
            rol.setCarga(resCarga2.getValor().get(0));
        }
        ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan= erpe.getProgramasEducativostotal();
        if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
        rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
        rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
        rol.setGrupos(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());

    }    
    
    public void cambiarPeriodo() {
        if (rol.getPeriodo() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setGrupos(Collections.EMPTY_LIST);
            rol.setCargas(Collections.EMPTY_LIST);  
            rol.setGrupoSelec(new Grupo());            
            rol.setCarga(null);
            return;
        }
        rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
        if(rol.getPeriodoActivo()<=56){
            rol.setRender(Boolean.FALSE);
        }else{
            rol.setRender(Boolean.TRUE);
        }
        if(rol.getTipoUser()!=3){
            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
            ResultadoEJB<List<Grupo>> resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);

            rol.setGrupos(resgrupos.getValor()); 
            rol.setGrupoSelec(rol.getGrupos().get(0));
        }
        ResultadoEJB<List<DtoCargaAcademica>> resCarga;
        
        if(rol.getTipoUser()!=3){ 
            resCarga = ea.getCargaAcademicasPorGrupo(rol.getGrupoSelec().getIdGrupo(), rol.getPeriodo());            
            if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
        }else{
            resCarga = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodo());
        }
        if (resCarga.getValor().isEmpty()) {
            rol.setPlaneacioneses(Collections.EMPTY_LIST);
            rol.setEstudioMateria(new PlanEstudioMateria());
            rol.setMetasPropuestas(new MetasPropuestas());
            rol.setCargas(Collections.EMPTY_LIST);
            rol.setCronograma(Collections.EMPTY_LIST);
            rol.setDrcas(Collections.EMPTY_LIST);
            return;
        }
        rol.setCargas(resCarga.getValor());
        rol.setCarga(resCarga.getValor().get(resCarga.getValor().size()-1));      
        existeAsignacion();
    }
     
    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGrupos(new ArrayList<>());
        rol.setCargas(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        ResultadoEJB<List<Grupo>> resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        rol.setGrupos(resgrupos.getValor()); 
        rol.setGrupoSelec(rol.getGrupos().get(0));
        ResultadoEJB<List<DtoCargaAcademica>> resCarga = ea.getCargaAcademicasPorGrupo(rol.getGrupoSelec().getIdGrupo(), rol.getPeriodo());            
        if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
        if (resCarga.getValor().isEmpty()) {
            rol.setPlaneacioneses(Collections.EMPTY_LIST);
            rol.setEstudioMateria(new PlanEstudioMateria());
            rol.setMetasPropuestas(new MetasPropuestas());
            rol.setCargas(Collections.EMPTY_LIST);
            rol.setCronograma(Collections.EMPTY_LIST);
            return;
        }
        rol.setCargas(resCarga.getValor());
        rol.setCarga(resCarga.getValor().get(0));
        existeAsignacion();
    }    
    
   public void cambiarGrupo(ValueChangeEvent event) {
        rol.setCargas(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<DtoCargaAcademica>> resCarga = ea.getCargaAcademicasPorGrupo(rol.getGrupoSelec().getIdGrupo(), rol.getPeriodo());            
        if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
        if (resCarga.getValor().isEmpty()) {
            rol.setPlaneacioneses(Collections.EMPTY_LIST);
            rol.setEstudioMateria(new PlanEstudioMateria());
            rol.setMetasPropuestas(new MetasPropuestas());
            rol.setCargas(Collections.EMPTY_LIST);
            rol.setCronograma(Collections.EMPTY_LIST);
            return;
        }
        rol.setCargas(resCarga.getValor());
        rol.setCarga(resCarga.getValor().get(0));
        existeAsignacion();
    }
    public void cambiarCarga(ValueChangeEvent event) {
        rol.setExisteConfiguracion(false);
        rol.setCarga((DtoCargaAcademica) event.getNewValue());
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
        if (rol.getCarga() == null) {      
            rol.setPlaneacioneses(Collections.EMPTY_LIST);      
            rol.setPlaneacioneses(Collections.EMPTY_LIST);
            rol.setEstudioMateria(new PlanEstudioMateria());
            rol.setMetasPropuestas(new MetasPropuestas());
            rol.setMetasPropuestas(new MetasPropuestas());
            rol.setCargas(Collections.EMPTY_LIST);
            rol.setCronograma(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<DtoInformePlaneaciones>> res = ejb.buscarUnidadMateriaConfiguracionDetalle(rol.getCarga(),rol.getPeriodoActivo());
        rol.setPlaneacioneses(new ArrayList<>());
        if (res.getValor().size() > 0 && !res.getValor().isEmpty()) {
            rol.setEstudioMateria(new PlanEstudioMateria());
            rol.setMetasPropuestas(new MetasPropuestas());
            rol.setExisteAsignacionIndicadores(true);
            rol.setPlaneacioneses(res.getValor());
            rol.setEstudioMateria(rol.getCarga().getPlanEstudioMateria());
            if(!rol.getEstudioMateria().getMetasPropuestasList().isEmpty()){
                rol.setMetasPropuestas(rol.getEstudioMateria().getMetasPropuestasList().get(rol.getEstudioMateria().getMetasPropuestasList().size()-1));
            }
            rol.setDrcas(Collections.EMPTY_LIST);
        }
        calculaDatosGenerales();
        calcularesultados();   
        Ajax.update("frm");
    }
   
    public void calcularesultados() {  
        DecimalFormat df = new DecimalFormat("#.##");
        if(rol.getPlaneacioneses().isEmpty())return;
        rol.setDrcas(new ArrayList<>());
        rol.getPlaneacioneses().forEach((t) -> {
            ResultadoEJB<DtoResultadosCargaAcademica> resc = configuraciones.getCalificacionesPorConfiguracionDetalle(t, t.getMeta(),rol.getPeriodoActivo());
            if (!resc.getCorrecto()) {
                mostrarMensajeResultadoEJB(resc);
            } else {
                rol.getDrcas().add(resc.getValor());
            }
        });    
            
        barModel2=new BarChartModel();
        ChartData data = new ChartData();
        
        rol.setAutonomo(configuraciones.getestudiantesAlcanzaMeta(rol.getCarga().getCargaAcademica(),1));
        rol.setDestacado(configuraciones.getestudiantesAlcanzaMeta(rol.getCarga().getCargaAcademica(),2));
        rol.setSatisfactorio(configuraciones.getestudiantesAlcanzaMeta(rol.getCarga().getCargaAcademica(),3));
        rol.setNoAcreditado(configuraciones.getestudiantesAlcanzaMeta(rol.getCarga().getCargaAcademica(),4));
        
        rol.getEstudioMateria().getIndicadorAlineacionPlanMateriaList().forEach((t) -> {
            Double porcentaje=0D;
             ResultadoEJB<Double> resc = configuraciones.getCalificacionesIndicador(rol.getCarga().getCargaAcademica(), t.getMetaIndicador());
            if (!resc.getCorrecto()) {
                mostrarMensajeResultadoEJB(resc);
            } else {
                porcentaje=resc.getValor();
            }
            BarChartDataSet barDataSet = new BarChartDataSet();
            barDataSet.setLabel(t.getIndicadorAlineacion().getClave() +" Meta esperada " + t.getMetaIndicador()+ " % " +" Meta alcanzada " + df.format(porcentaje)+ " % ");
            if (porcentaje>t.getMetaIndicador()) {
                barDataSet.setBackgroundColor("rgb(0,255,0)");
                barDataSet.setBorderColor("rgb(0,255,0)");
            } else {
                barDataSet.setBackgroundColor("rgb(255,0,0)");
                barDataSet.setBorderColor("rgb(255,0,0)");
            }
            barDataSet.setBorderWidth(1);
            List<Number> values = new ArrayList<>();
            values.add(porcentaje);
            barDataSet.setData(values);
            data.addChartDataSet(barDataSet);
        });
        
        List<String> labels = new ArrayList<>();
        labels.add(rol.getEstudioMateria().getClaveMateria());

        data.setLabels(labels);
        barModel2.setData(data);
 
        

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Metas");
        options.setTitle(title);

        barModel2.setOptions(options);
       
    }

    public void calculaDatosGenerales() {
        bt = 0;        bd = 0;        ri = 0;
        rol.setMetaP(0d);
        if (!rol.getCarga().getGrupo().getEstudianteList().isEmpty()) {
            List<Estudiante> es = new ArrayList<>();
            es = rol.getCarga().getGrupo().getEstudianteList();
            es.forEach((t) -> {
                switch (t.getTipoEstudiante().getIdTipoEstudiante()) {
                    case 2:                        bt = bt + 1;                        break;
                    case 3:                        bd = bd + 1;                        break;
                    case 5:                        ri = ri + 1;                        break;
                }
            });
            rol.setEsInsc(es.size());
            rol.setEsActivos((es.size()-bd-bt));
            rol.setEsBajT(bt);
            rol.setEsBajD(bd);
            rol.setEsRein(ri);
        }
    }
   
    public String buscarDirector(Short clave){
        try {
            Personal p = new Personal();
            AreasUniversidad au=new AreasUniversidad();
            au=ejbAreasLogeo.mostrarAreasUniversidad(clave);
            p = ejbPersonal.mostrarPersonalLogeado(au.getResponsable());
            return p.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ResultadosPlaneacionesConsulta.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void guardarAccion() {        
        configuraciones.registrarAccionMejora(rol.getAccionesDeMejora(),rol.getConfiguracion(), Operacion.PERSISTIR);
        existeAsignacion();
        rol.setAccionesDeMejora(new AccionesDeMejora());
    }
    
    public void actualizarAnalisisFinal() {
        if (rol.getAnalisisDeResultados().length() < 200) {
            rol.setMensajeVAnalisisF("Solo agrego "+rol.getAnalisisDeResultados().length() +" caracteres y se pide un mínimo de 200");
            Ajax.oncomplete("PF('dlgAnalisisFina').update();");
            Ajax.oncomplete("PF('dlgAnalisisFina').show();");
        } else {
            rol.setMensajeVAnalisisF("");
            rol.getCarga().getCargaAcademica().setAnalisisDeResultados(rol.getAnalisisDeResultados());
            configuraciones.agregaeAnalisisFinal(rol.getCarga());
            existeAsignacion();
            rol.setAccionesDeMejora(new AccionesDeMejora());
            rol.setAnalisisDeResultados("");
            Ajax.oncomplete("PF('dlgAnalisisFina').hide();");
        }
    }
    
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
}
