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
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGraficaCronograma;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ImpresionPlaneacionCuatrimestral;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInformePlaneaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class PlaneacionCuatrimestralImpresion extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    ImpresionPlaneacionCuatrimestral rol;

    @EJB EjbAsignacionIndicadoresCriterios ejb;
    @EJB EjbValidacionRol evr;
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
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REPORTE_PLANEACION_CUATRIMESTRAL);
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDocente(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo docente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ImpresionPlaneacionCuatrimestral(filtro, docente);
            tieneAcceso = rol.tieneAcceso(docente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu

            rol.setDocente(docente);
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
//            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosCargaAcademica(rol.getDocente(), rol.getPeriodoActivo());
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos2 = ejb.getPeriodosCargaAcademica(docente, rol.getPeriodoActivo());
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            rol.setPeriodo(ejb.getPeriodoActual());
            rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
////            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.init()"+rol.getPeriodos().size());
            
            ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicaDocente(docente, rol.getPeriodo());
            ResultadoEJB<List<DtoCargaAcademica>> resCarga2 = ejb.getCargaAcademicaDocente(docente, rol.getPeriodos().get(0));
            
            if(!resCarga.getCorrecto() || !resCarga2.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);            
            if (resCarga.getValor().isEmpty() && resCarga2.getValor().isEmpty()) {
                tieneAcceso = Boolean.FALSE;
                if(!resPeriodos2.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos2);                
                if (!resPeriodos2.getValor().isEmpty()) {
                    rol.setPeriodos(resPeriodos2.getValor());
                    tieneAcceso = Boolean.TRUE;
                }
            }
            
            resCarga = ejb.getCargaAcademicaDocente(docente, rol.getPeriodo());
            resCarga2 = ejb.getCargaAcademicaDocente(docente, rol.getPeriodos().get(0));

            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if (!resCarga.getValor().isEmpty()) {
                rol.setCargas(resCarga.getValor());
                rol.setCarga(resCarga.getValor().get(0));
            } else {
                rol.setCargas(resCarga2.getValor());
                rol.setCarga(resCarga2.getValor().get(0));
            }
            if (rol.getPeriodoActivo() <= 56) {
                rol.setRender(Boolean.FALSE);
            } else {
                rol.setRender(Boolean.TRUE);
            }
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

    public void cambiarPeriodo() {
////        System.out.println("rol.getPeriodoSeleccionado() = " + caster.periodoToString(rol.getPeriodoSeleccionado()));
        if (rol.getPeriodo() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setCargas(Collections.EMPTY_LIST);
            rol.setCarga(null);
            return;
        }
         rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
        if(rol.getPeriodoActivo()<=56){
            rol.setRender(Boolean.FALSE);
        }else{
            rol.setRender(Boolean.TRUE);
        }
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.cambiarPeriodo()"+rol.getPeriodoActivo());
        ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodo());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.cambiarPeriodo()"+resCarga.getValor().size());
        rol.setInformeplaneacioncuatrimestraldocenteprints(Collections.EMPTY_LIST);
        rol.setCargas(Collections.EMPTY_LIST);
        rol.setCronograma(Collections.EMPTY_LIST);
        
        if (!resCarga.getValor().isEmpty()) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.cambiarPeriodo(1)");
            rol.setCargas(resCarga.getValor());
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.cambiarPeriodo(2)"+rol.getCargas().size());
            rol.setCarga(rol.getCargas().get(0));
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.cambiarPeriodo(3)"+rol.getCarga());
            rol.setEstudioMateria(new PlanEstudioMateria());
            existeAsignacion();
            crearCronograma(rol.getCarga());
        }
        Ajax.update("frm");
    }
    
    public void cambiarCarga(ValueChangeEvent event) {
        rol.setExisteConfiguracion(false);
        rol.setCarga((DtoCargaAcademica) event.getNewValue());
        existeAsignacion();
        crearCronograma(rol.getCarga());
    }

    public void existeAsignacion() {
        if (rol.getCarga() == null) {
            rol.setEstudioMateria(new PlanEstudioMateria());
            return;
        }
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.existeAsignacion()"+rol.getCarga());        
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.existeAsignacion()"+rol.getPeriodoActivo());
        ResultadoEJB<List<DtoInformePlaneaciones>> res = ejb.buscarUnidadMateriaConfiguracionDetalle(rol.getCarga(),rol.getPeriodoActivo());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.existeAsignacion()"+res.getValor().size());
        rol.setInformeplaneacioncuatrimestraldocenteprints(new ArrayList<>());
////        System.err.println("existeAsignacion - res " + res.getValor().size());
        if (res.getValor().size() > 0 && !res.getValor().isEmpty()) {
            rol.setEstudioMateria(new PlanEstudioMateria());
            rol.setExisteAsignacionIndicadores(true);
            rol.setInformeplaneacioncuatrimestraldocenteprints(res.getValor());
            rol.setEstudioMateria(rol.getCarga().getPlanEstudioMateria());
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
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PlaneacionCuatrimestralImpresion.crearCronograma()"+dca);
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
