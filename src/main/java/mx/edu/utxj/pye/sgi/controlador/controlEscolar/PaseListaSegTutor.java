package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseLista;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseListaReporte;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseListaReporteConsulta;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PaseDeListaSegTutor;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
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
public class PaseListaSegTutor extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    PaseDeListaSegTutor rol;

    @EJB EjbAsistencias ejb;
    @EJB EjbValidacionRol evr;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker packer;
    @EJB EjbCasoCritico ecc;
    private Integer hora=0,minutos=0,tasis=0;
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
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarTutor(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new PaseDeListaSegTutor(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getTutor());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
           
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
           
            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.PaseListaSegTutor.init()"+rol.getTutor());
            
            ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicasPorTutor(rol.getTutor(), rol.getPeriodo());
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
        rol.setDtoPaseListaReporteConsultas(new ArrayList<>());
        rol.setDplsReportesMes(new ArrayList<>());
        rol.setDiasPaseLista(new ArrayList<>());
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
            Logger.getLogger(PaseListaSegTutor.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PaseListaSegTutor.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void consultarReporte(ValueChangeEvent event) {
        rol.setDtoConfUniMat((DtoConfiguracionUnidadMateria)event.getNewValue());
        rol.setDtoPaseListaReporteConsultas(new ArrayList<>());
        rol.setDplsReportesMes(new ArrayList<>());
        rol.setDiasPaseLista(new ArrayList<>());
        
        if(rol.getDtoConfUniMat() == null) {
            mostrarMensaje("No hay unidad de evaluación seleccionada.");
            rol.setEstudiantesPorGrupo(null);
            return;
        }
        ResultadoEJB<DtoUnidadConfiguracion> ducB=packer.packUnidadConfiguracion(rol.getDtoConfUniMat().getUnidadMateriaConfiguracion(), rol.getCarga());
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(rol.getCarga(), ducB.getValor());
        if(!resGrupo.getCorrecto()) mostrarMensajeResultadoEJB(resGrupo);
        else {
            rol.setEstudiantesPorGrupo(resGrupo.getValor());
        }
        
        createDynamicColumns();
    }
    
    private void createDynamicColumns() {
        rol.setDtoPaseListaReporteConsultas(new ArrayList<>());
        rol.setDplsReportesMes(new ArrayList<>());
        rol.setDiasPaseLista(new ArrayList<>());
        Date fI=rol.getDtoConfUniMat().getUnidadMateriaConfiguracion().getFechaInicio();
        Date fF=rol.getDtoConfUniMat().getUnidadMateriaConfiguracion().getFechaFin();
        
        rol.getEstudiantesPorGrupo().getEstudiantes().forEach((a) -> {
            ResultadoEJB<List<Asistenciasacademicas>> res = ejb.buscarAsistenciasacademicas(rol.getCarga().getCargaAcademica(), a.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
            List<Asistenciasacademicas> asFilter = new ArrayList<>();
            asFilter = res.getValor().stream().filter(t -> (t.getAsistencia().getFechaHora().after(fI) || t.getAsistencia().getFechaHora().equals(fI)) && (t.getAsistencia().getFechaHora().before(fF) || t.getAsistencia().getFechaHora().equals(fF))).collect(Collectors.toList());
            tasis = 0;
            if (asFilter.size() > 0 && !asFilter.isEmpty()) {
                asFilter.forEach((t) -> {
                    switch (t.getTipoAsistenciaA()) {
                        case "Asistencia":                            t.setTipoAsistenciaA("A");                            break;
                        case "Falta":                            t.setTipoAsistenciaA("F");                            break;
                        case "Permiso":                            t.setTipoAsistenciaA("P");                            break;
                        case "Justificado":                            t.setTipoAsistenciaA("J");                            break;
                    }
                    if(!t.getTipoAsistenciaA().equals("F")){
                        tasis=tasis+1;
                    }
                });
                Double d = (tasis * 100.0) / asFilter.size();
                Boolean b = (d < 80.0);
                ResultadoEJB<DtoCasoCritico> rejb=ecc.generarNuevo(a.getDtoEstudiante(), a.getDtoCargaAcademica(), a.getDtoUnidadConfiguracion(), CasoCriticoTipo.SISTEMA_ASISTENCIA_IRREGURLAR);
                if (rejb.getCorrecto()) {
                    a.setDtoCasoCritico(rejb.getValor());
                }

                ResultadoEJB<DtoCasoCritico> registrarPorAsistenciaIrregular = ecc.registrarPorAsistenciaIrregular(a, d);
                if (registrarPorAsistenciaIrregular.getCorrecto()) {
                    mostrarMensaje("Se generó un caso crítico automáticamente por Asistencia irregular.");
                } else if (registrarPorAsistenciaIrregular.getResultado() < 4) {
                    mostrarMensajeResultadoEJB(registrarPorAsistenciaIrregular);
                }

                List<Listaalumnosca> ls = rol.getListaalumnoscas().stream().filter(t -> t.getMatricula() == a.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula()).collect(Collectors.toList());
                rol.getDtoPaseListaReporteConsultas().add(new DtoPaseListaReporteConsulta(ls.get(0), asFilter, asFilter.size(), d, b));
            }
        });     
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        ResultadoEJB<List<Asistenciasacademicas>> resF = ejb.buscarAsistenciasacademicasFechasMes(rol.getCarga().getCargaAcademica());
        List<Asistenciasacademicas> asFilter = new ArrayList<>();
        asFilter = resF.getValor().stream().filter(t -> (t.getAsistencia().getFechaHora().after(fI) || t.getAsistencia().getFechaHora().equals(fI)) && (t.getAsistencia().getFechaHora().before(fF) || t.getAsistencia().getFechaHora().equals(fF))).collect(Collectors.toList());
        if (asFilter.size() > 0 && !asFilter.isEmpty()) {
            rol.setDplsReportesMes(new ArrayList<>());
            rol.setDiasPaseLista(new ArrayList<>());
            rol.setHorasPaseLista(new ArrayList<>());
            List<Asistenciasacademicas> as = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                switch (i) {
                    case 1:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 0).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Enero", as.size()));
                        }
                        break;
                    case 2:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 1).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Febrero", as.size()));
                        }
                        break;
                    case 3:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 2).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Marzo", as.size()));
                        }
                        break;
                    case 4:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 3).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Abril", as.size()));
                        }
                        break;
                    case 5:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 4).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Mayo", as.size()));
                        }
                        break;
                    case 6:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 5).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Junio", as.size()));
                        }
                        break;
                    case 7:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 6).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Julio", as.size()));
                        }
                        break;
                    case 8:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 7).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Agosto", as.size()));
                        }
                        break;
                    case 9:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 8).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Septiembre", as.size()));
                        }
                        break;
                    case 10:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 9).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Octubre", as.size()));
                        }
                        break;
                    case 11:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 10).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Noviembre", as.size()));
                        }
                        break;
                    case 12:
                        as = new ArrayList<>();
                        as = asFilter.stream().filter(t -> t.getAsistencia().getFechaHora().getMonth() == 11).collect(Collectors.toList());
                        if (!as.isEmpty()) {
                            as.forEach((t) -> {
                                rol.getDiasPaseLista().add(t.getAsistencia().getFechaHora().getDate());
                                hora = t.getAsistencia().getFechaHora().getHours();
                                minutos = t.getAsistencia().getFechaHora().getMinutes();
                                if (hora >= 10 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "" + minutos);
                                } else if (hora <= 9 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "0" + minutos);
                                } else if (hora >= 10 && minutos <= 9) {
                                    rol.getHorasPaseLista().add("" + hora + ":" + "0" + minutos);
                                } else if (hora <= 9 && minutos >= 10) {
                                    rol.getHorasPaseLista().add("0" + hora + ":" + "" + minutos);
                                }
                            });
                            rol.getDplsReportesMes().add(new DtoPaseListaReporte("Diciembre", as.size()));
                        }
                        break;
                }
            }
        }
    }
}
