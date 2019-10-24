package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorSubordinados;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class AdminPoaValidacionProgramacion implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidadsRegistros = new ArrayList<>();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad();
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    @Getter    @Setter    private List<ListaEjesEsLaAp> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<ListaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    @Getter    @Setter    private String mss="";
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0; 
    @Getter    @Setter    private List<ListaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    @EJB    EjbUtilidadesCH ejbUtilidadesCH;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA utilidadesPOA;

    @PostConstruct
    public void init() {
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

    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            mss = "";
            claveArea = 0;
            areaPOASeleccionada = new AreasUniversidad();
            procesopoa = new Procesopoa();
            claveArea = Short.parseShort(event.getNewValue().toString());
            areaPOASeleccionada = ejbAreasLogeo.mostrarAreasUniversidad(claveArea);
            procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(claveArea);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaValidacionProgramacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarListasValidacionFinal() {
        listaListaEjeEstrategia=new ArrayList<>();
        listaListaEjeEstrategia.clear();

        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        if (!ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).isEmpty()) {
            ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).forEach((ej) -> {
                listaListaEjeEstrategia = new ArrayList<>();
                listaListaEjeEstrategia.clear();
                listaListaEjeEstrategia.add(new ListaEjeEstrategia(ej, ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, claveArea)));
                if (!listaListaEjeEstrategia.isEmpty()) {
                    listaListaEjeEstrategia.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = ejbRegistroActividades.getActividadesPoasEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea);
                            listaEstrategiaActividadesesEje.add(new ListaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));

                        });
                        ejesEsLaAp.add(new ListaEjesEsLaAp(ej, listaEstrategiaActividadesesEje));
                        listaEstrategiaActividadesesEje = new ArrayList<>();
                        listaEstrategiaActividadesesEje.clear();
                    });
                }
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

    public void enviarmensajes(String proceso) {
        utilidadesPOA.enviarCorreo(proceso, "Ad", false, mss, areaPOASeleccionada);
    }

    public void onRowEditAc(RowEditEvent event) {
        try {
            ActividadesPoa ap = (ActividadesPoa) event.getObject();
            ejbRegistroActividades.actualizaActividadesPoa(ap);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void actualizarestrategica(ValueChangeEvent e) {
        try {
            String id = e.getComponent().getClientId();
            ListaEjesEsLaAp eje=ejesEsLaAp.get(Integer.parseInt(id.split("dtbBajas:")[1].split(":dtbEstra")[0]));
            ListaEstrategiaActividades estratea=eje.getListalistaEstrategiaLaAp().get(Integer.parseInt(id.split("dtbEstra:")[1].split(":dtbActividades")[0]));
            ActividadesPoa ap=estratea.getActividadesPoas().get(Integer.parseInt(id.split("dtbActividades:")[1].split(":validar")[0]));
            ap.setEstratejica((Boolean) e.getNewValue());
            ejbRegistroActividades.actualizaActividadesPoa(ap);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }

    public void imprimirValores() {
    }

  
    public static class ListaEjesEsLaAp {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<ListaEstrategiaActividades> listalistaEstrategiaLaAp;

        public ListaEjesEsLaAp(EjesRegistro ejeA, List<ListaEstrategiaActividades> listalistaEstrategiaLaAp) {
            this.ejeA = ejeA;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }
    }
    
    public static class ListaEjeEstrategia {

        @Getter        @Setter        private EjesRegistro ejess;
        @Getter        @Setter        private List<Estrategias> listaEstrategiases1;

        public ListaEjeEstrategia(EjesRegistro ejess, List<Estrategias> listaEstrategiases1) {
            this.ejess = ejess;
            this.listaEstrategiases1 = listaEstrategiases1;
        }        
    }
    
    
    public static class ListaEstrategiaActividades {

        @Getter        @Setter        private Estrategias estrategias;
        @Getter        @Setter        private List<ActividadesPoa> actividadesPoas;

        public ListaEstrategiaActividades(Estrategias estrategias, List<ActividadesPoa> actividadesPoas) {
            this.estrategias = estrategias;
            this.actividadesPoas = actividadesPoas;
        }       
    }
}

