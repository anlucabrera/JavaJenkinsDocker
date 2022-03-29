package mx.edu.utxj.pye.sgi.controladores.poa;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.pye2.*;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import mx.edu.utxj.pye.sgi.dto.poa.DTOreportePoa;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPresupuestacion;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.PdfUtilidades;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

@Named
@ManagedBean
@ViewScoped
public class ConsultarPOAReportePYE implements Serializable {
    
    @Getter    @Setter    private Short ejercicioFiscal = 0, claveArea = 0;
    @Getter    @Setter    private String nombreArchivo="";
    @Getter    @Setter    private EjerciciosFiscales ef=new EjerciciosFiscales();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad();
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidadsRegistros = new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.ProgramacionActividades> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.ListaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    
    @Getter    @Setter    private List<DTOreportePoa.ActividadListaRecursoActividades> listaRecursoActividadeses = new ArrayList<>(); 
    @Getter    @Setter    private List<DTOreportePoa.EjeListaEstrategias> listaEstrategiases = new ArrayList<>();    
    @Getter    @Setter    private List<DTOreportePoa.EstrategiasListaLineasAccion> listaLineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.LineasAccionListaActividad> accionListaActividads = new ArrayList<>();
    
    @Getter private Boolean cargado = false;
    
    
    @Getter    @Setter    private LocalDate fechaActual=LocalDate.now();    
    @Getter    @Setter    private ListaPersonal listaPersonal=new ListaPersonal();
   
        
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    @EJB    EjbPersonal ejbPersonal;
    @EJB    EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    EjbPresupuestacion presupuestacion;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA utilidadesPOA;
    @Inject PdfUtilidades pu;

    
    

@Inject LogonMB logonMB;


@PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        ejercicioFiscal=controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        buscarAreasQueTienenPOA();
    }
    
    public void buscarAreasQueTienenPOA() {
        try {
            areasUniversidadsRegistros = new ArrayList<>();
            areasUniversidadsRegistros.clear();
            areasUniversidadsRegistros.add(new AreasUniversidad(Short.parseShort("0"), "Seleccione uno", "Seleccione uno", "1", false));
            areasUniversidadsRegistros.addAll(ejbAreasLogeo.getAreasUniversidadConPoa());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        ejercicioFiscal = Short.parseShort(event.getNewValue().toString());
        consultarListasValidacionFinal();
    }

    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            claveArea = 0;
            areaPOASeleccionada = new AreasUniversidad();
            procesopoa = new Procesopoa();
            claveArea = Short.parseShort(event.getNewValue().toString());
            areaPOASeleccionada = ejbAreasLogeo.mostrarAreasUniversidad(claveArea);
            procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(claveArea);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaValidacionPresupuestacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarListasValidacionFinal() {
        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();
        List<EjesRegistro> ers = new ArrayList<>();
        ers = ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal);
        if (!ers.isEmpty()) {
            ers.forEach((ej) -> {
                listaListaEjeEstrategia.add(new DTOreportePoa.ListaEjeEstrategia(ej, ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, claveArea)));
            });
        }
        if (!listaListaEjeEstrategia.isEmpty()) {
            ejesEsLaAp = new ArrayList<>();
            listaListaEjeEstrategia.forEach((ej) -> {
                List<DTOreportePoa.EstrategiaLineas> els = new ArrayList<>();
                ej.getEstrategiases().forEach((es) -> {
                    List<DTOreportePoa.ActividadRecurso> ars = new ArrayList<>();
                    List<ActividadesPoa> aps = new ArrayList<>();
                    aps = ejbRegistroActividades.getActividadesPoasEstarategias(es, ej.getEjess(), ejercicioFiscal, claveArea);
                    aps.forEach((ap) -> {
                        List<DTOreportePoa.RecursoActividad> ras = new ArrayList<>();
                        ras = ejbCatalogosPoa.getPresupuestacionPOA(ap);
                        ars.add(new DTOreportePoa.ActividadRecurso(ap, ap.getUnidadMedida(), ras));
                    });
                    els.add(new DTOreportePoa.EstrategiaLineas(es, ars));
                });
                ejesEsLaAp.add(new DTOreportePoa.ProgramacionActividades(ej.getEjess(), els));
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjesRegistro().getEje(), y.getEjesRegistro().getEje()));
        
        if (!ers.isEmpty()) {
            ers.forEach((ej) -> {
                List<Estrategias> estrategiases = new ArrayList<>();
                estrategiases.clear();
                estrategiases = ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, claveArea);
                if (!estrategiases.isEmpty()) {
                    estrategiases.forEach((es) -> {
                        List<LineasAccion> lineasAccions = new ArrayList<>();
                        lineasAccions.clear();
                        lineasAccions = ejbCatalogosPoa.mostrarLineasAccionRegistroParametros(claveArea, ejercicioFiscal, ej, es);
                        if (!lineasAccions.isEmpty()) {
                            lineasAccions.forEach((li) -> {
                                List<CuadroMandoIntegral> cuadro = new ArrayList<>();
                                cuadro = ejbCatalogosPoa.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ej, es, li);
                                List<ActividadesPoa> actividadesPoas = new ArrayList<>();
                                actividadesPoas.clear();
                                actividadesPoas = presupuestacion.mostrarActividadesPoaCuadroDeMandoRecurso(claveArea, ejercicioFiscal, cuadro.get(0));
                                if (!actividadesPoas.isEmpty()) {
                                    actividadesPoas.forEach((ap) -> {
                                        List<RecursosActividad> recursosActividadsP = new ArrayList<>();
                                        recursosActividadsP.clear();
                                        recursosActividadsP = presupuestacion.mostrarRecursosActividadReporte(ap);
                                        listaRecursoActividadeses.add(new DTOreportePoa.ActividadListaRecursoActividades(ap, recursosActividadsP));
                                    });
                                }
                                accionListaActividads.add(new DTOreportePoa.LineasAccionListaActividad(li, listaRecursoActividadeses));
                                listaRecursoActividadeses = new ArrayList<>();
                                listaRecursoActividadeses.clear();
                            });
                        }
                        listaLineasAccions.add(new DTOreportePoa.EstrategiasListaLineasAccion(es, accionListaActividads));
                        accionListaActividads = new ArrayList<>();
                        accionListaActividads.clear();
                    });
                }
                listaEstrategiases.add(new DTOreportePoa.EjeListaEstrategias(ej, listaLineasAccions));
                listaLineasAccions = new ArrayList<>();
                listaLineasAccions.clear();
            });
        }
    }

    public void imprimirReporte() {
        try {
            ef = utilidadesPOA.obtenerAnioRegistro(ejercicioFiscal);
            pu.reportePOA(listaEstrategiases, ef, areaPOASeleccionada,procesopoa);
            nombreArchivo = "";
            nombreArchivo = areaPOASeleccionada.getArea() + " - " + areaPOASeleccionada.getNombre();
//            Ajax.oncomplete("descargar('" + "http://siip.utxicotepec.edu.mx/archivos/evidencias2/" + ef.getAnio() + "/reportes/" + nombreArchivo + ".pdf" + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimirValores() {
    }
}
