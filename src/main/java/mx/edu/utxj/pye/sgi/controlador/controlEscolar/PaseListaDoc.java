package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGraficaCronograma;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseLista;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseListaReporte;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseListaReporteConsulta;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ImpresionPlaneacionCuatrimestral;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PaseDeListaDocente;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;

/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class PaseListaDoc extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    PaseDeListaDocente rol;

    @EJB EjbAsistencias ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.PASE_DE_LISTA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDocente(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo docente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new PaseDeListaDocente(filtro, docente);
            tieneAcceso = rol.tieneAcceso(docente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setDocente(docente);
           
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            
            ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicaDocente(docente, rol.getPeriodo());
            if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
            rol.setCargas(resCarga.getValor());
            rol.setCarga(resCarga.getValor().get(0));
            existeAsignacion();
            rol.setFechaInpresion(new Date());
            List<String> asi=new ArrayList<>();
            asi.add("Asistencia");
            asi.add("Falta");
            asi.add("Permiso");
            asi.add("Justificado");
            rol.setAsistencias(asi);
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "pase de lista";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    public void cambiarCarga(ValueChangeEvent event) {
        rol.setExisteConfiguracion(false);
        rol.setCarga((DtoCargaAcademica) event.getNewValue());
        existeAsignacion();
    }

    public void existeAsignacion() {
        if (rol.getCarga() == null) {
            return;
        }
        rol.setListaalumnoscas(new ArrayList<>());
        ResultadoEJB<List<Listaalumnosca>> res = ejb.buscarListaGrupos(rol.getCarga());
//        System.err.println("existeAsignacion - res " + res.getValor().size());
        if (res.getValor().size() > 0 && !res.getValor().isEmpty()) {
            rol.setExisteAsignacionIndicadores(true);
            rol.setListaalumnoscas(res.getValor());
            existeConfiguracion();
        }
        Ajax.update("frm");
    }
    public void existeConfiguracion(){
        if (rol.getCarga() == null) {            return;        }
        ResultadoEJB<List<UnidadMateriaConfiguracion>> res = ejb.buscarConfiguracionUnidadMateria(rol.getCarga());
        if (res.getValor().size() > 0 && !res.getValor().isEmpty()) {
            rol.setExisteConfiguracion(true);
            crearDtoConfiguracionUnidadMateria();
        } else {
            rol.setExisteConfiguracion(false);
            mostrarMensajeResultadoEJB(res);
        }
        Ajax.update("frm");
    }
    
    public void crearDtoConfiguracionUnidadMateria() {
        if (rol.getCarga() == null) {            return;        }
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> res = ejb.getConfiguracionUnidadMateria(rol.getCarga());
        if (res.getCorrecto()) {
            rol.setListaDtoConfUniMat(res.getValor());
        } else {
            mostrarMensajeResultadoEJB(res);
        }

    }

    public String buscarDirector(Short clave){
        try {
            Personal p = new Personal();
            AreasUniversidad au=new AreasUniversidad();
            au=ejbAreasLogeo.mostrarAreasUniversidad(clave);
            return buscarPersonal(au.getResponsable());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public String buscarPersonal(Integer clave){
        try {
            Personal p = new Personal();
            p = ejbPersonal.mostrarPersonalLogeado(clave);
            return p.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
           
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void guardaPaseLista() {
        rol.setDpls(new ArrayList<>());
        rol.getDpls().clear();
        rol.getListaalumnoscas().forEach((t) -> {
            ResultadoEJB<Estudiante> resE = ejb.buscaEstudiante(t.getMatricula());
            ResultadoEJB<CargaAcademica> resC = ejb.buscaCargaAcademica(t.getCarga());
            if ((resE.getCorrecto()) && (resC.getCorrecto())) {
                rol.getDpls().add(new DtoPaseLista(t, resE.getValor(), resC.getValor()));
            }
        });
        ejb.agregarPaseLista(rol.getDpls());
        existeAsignacion();
    }
    
    public void consultarReporte(ValueChangeEvent event) {
        rol.setDtoConfUniMat((DtoConfiguracionUnidadMateria)event.getNewValue());
        createDynamicColumns();
    }
    
    private void createDynamicColumns() {
        rol.setDtoPaseListaReporteConsultas(new ArrayList<>());
        rol.getListaalumnoscas().forEach((a) -> {
//            rol.getDtoConfUniMat().getUnidadMateriaConfiguracion().getFechaInicio()
//            rol.getDtoConfUniMat().getUnidadMateriaConfiguracion().getFechaInicio()
            ResultadoEJB<List<Asistenciasacademicas>> res = ejb.buscarAsistenciasacademicas(rol.getCarga().getCargaAcademica(),a.getMatricula());
            if (res.getValor().size() > 0 && !res.getValor().isEmpty()) {
                rol.getDtoPaseListaReporteConsultas().add(new DtoPaseListaReporteConsulta(a, res.getValor()));
            }
        });
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm");
        rol.setVALID_COLUMN_KEYS(new ArrayList<>());
        rol.getVALID_COLUMN_KEYS().add("Matricula");
        rol.getVALID_COLUMN_KEYS().add("Nombre");
        ResultadoEJB<List<Asistenciasacademicas>> resF = ejb.buscarAsistenciasacademicasFechasMes(rol.getCarga().getCargaAcademica());
        
        rol.setColumnTemplate("Matricula/Nombre");
        if (resF.getValor().size() > 0 && !resF.getValor().isEmpty()) {
            resF.getValor().forEach((t) -> {
                rol.setColumnTemplate(rol.getColumnTemplate()+"/"+df.format(t.getAsistencia().getFechaHora()));
                rol.getVALID_COLUMN_KEYS().add(df.format(t.getAsistencia().getFechaHora()));
            });
        }
        
        rol.setDplsReportes(new ArrayList<>());
        String[] columnKeys = rol.getColumnTemplate().split("/");        
        rol.getDtoPaseListaReporteConsultas().forEach((t) -> {
            for (String columnKey : columnKeys) {
                String key = columnKey.trim();
                if(key.equals("Matricula")){
                    rol.getDplsReportes().add(new DtoPaseListaReporte(columnKey.toUpperCase(), String.valueOf(t.getListaalumnosca().getMatricula())));
                } else if (key.equals("Nombre")) {
                    rol.getDplsReportes().add(new DtoPaseListaReporte(columnKey.toUpperCase(), t.getListaalumnosca().getEsApePat() + " " + t.getListaalumnosca().getEsApeMat() + " " + t.getListaalumnosca().getEsNombre()));
                } else {
                    Integer p = 0;
                    for (Integer i = columnKeys.length; i > 2; i--) {
                        Asistenciasacademicas a= t.getAsistenciasacademicases().get(p);
                        rol.getDplsReportes().add(new DtoPaseListaReporte(columnKey.toUpperCase(), a.getTipoAsistenciaA()));
                        p++;
                    }
                }
            }
        });     
    }
}
