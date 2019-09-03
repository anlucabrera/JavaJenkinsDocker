package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGraficaCronograma;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ImpresionPlaneacionCuatrimestral;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.el.functions.Dates;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class PlaneacionCuatrimestralImpresion extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    ImpresionPlaneacionCuatrimestral rol;

    @EJB EjbAsignacionIndicadoresCriterios ejb;
    @EJB EjbPropiedades ep;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.REPORTE_PLANEACION_CUATRIMESTRAL);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDocente(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo docente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ImpresionPlaneacionCuatrimestral(filtro, docente);
            tieneAcceso = rol.tieneAcceso(docente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setDocente(docente);
           
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            
            ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicaDocente(docente, rol.getPeriodo());
            if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
            rol.setCargas(resCarga.getValor());
            rol.setCarga(resCarga.getValor().get(0));
            existeAsignacion();
            rol.setFechaInpresion(new Date());
//            existeConfiguracion();
            crearCronograma(rol.getCarga());
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "planeacion cuatrimestral print";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    public void cambiarCarga(ValueChangeEvent event) {
        rol.setExisteConfiguracion(false);
        rol.setCarga((DtoCargaAcademica) event.getNewValue());
        existeAsignacion();
        crearCronograma(rol.getCarga());
    }

    public void existeAsignacion() {
        if (rol.getCarga() == null) {
            return;
        }
        ResultadoEJB<List<Informeplaneacioncuatrimestraldocenteprint>> res = ejb.buscarInforme(rol.getCarga());
//        System.err.println("existeAsignacion - res " + res.getValor().size());
        if (res.getValor().size() > 0 && !res.getValor().isEmpty()) {
            rol.setExisteAsignacionIndicadores(true);
            rol.setInformeplaneacioncuatrimestraldocenteprints(res.getValor());
        }
        Ajax.update("frm");
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
            Logger.getLogger(PlaneacionCuatrimestralImpresion.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
   
    
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    private void crearCronograma(DtoCargaAcademica dca) {
        rol.setCronograma(new ArrayList<>());
        rol.getCronograma().clear();
        rol.setPorcIni(0D);
        rol.setNumDtotales(0);
        dca.getCargaAcademica().getUnidadMateriaConfiguracionList().forEach((t) -> {
            rol.setNumDtotales(rol.getNumDtotales()+((int) ((t.getFechaFin().getTime() - t.getFechaInicio().getTime()) / 86400000)));
        });
        dca.getCargaAcademica().getUnidadMateriaConfiguracionList().forEach((t) -> {
            Integer cuuatri = 0;
            Integer numD = 0;
            Double porcU = 0D;
            Double porcRes = 0D;
            if (t.getFechaInicio().getMonth() <= 3) {
                rol.setCuatrimestre(1);
            } else {
                if (t.getFechaInicio().getMonth() <= 7) {
                    rol.setCuatrimestre(2);
                } else {
                    rol.setCuatrimestre(3);
                }
            }
            
            numD = ((int) ((t.getFechaFin().getTime() - t.getFechaInicio().getTime()) / 86400000));
            porcU=((numD*100.0)/rol.getNumDtotales());
            porcRes=100-(porcU+rol.getPorcIni());          
            rol.getCronograma().add(new DtoGraficaCronograma((t.getIdUnidadMateria().getNoUnidad() + ".-" + t.getIdUnidadMateria().getNombre()), rol.getPorcIni(), porcU,porcRes));
            rol.setPorcIni(rol.getPorcIni()+(porcU));
        });
    }

}
