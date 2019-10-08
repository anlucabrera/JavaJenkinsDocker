package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

@Named
@ManagedBean
@ViewScoped
public class AreaPoaJustificacion implements Serializable {
// Listas de entities 
    @Getter    @Setter    private List<ActividadesPoa> listaActividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> listaEjesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> listaEstrategias = new ArrayList<>();
// variables de datos Primitivos
    @Getter    @Setter    private Short ejercicioFiscal = 0;   
    @Getter    @Setter    private Boolean alineacionActiva = false;
    @Getter    @Setter    private Date fechaActual=new Date();   
// variables de entities
    @Getter    @Setter    private ActividadesPoa actividadesPoa = new ActividadesPoa();
    @Getter    @Setter    private EjesRegistro ejesRegistro = new EjesRegistro();
    @Getter    @Setter    private Estrategias estrategias = new Estrategias();
// Listas de DTO's
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA pOAUtilidades;

    @PostConstruct
    public void init() {
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();;
        alineacionActiva = false; 
        consultarListasInit();
    }

    public void consultarListasInit() {
        listaEstrategias.clear();
        listaEjesRegistros.clear();
        listaActividadesPoas.clear();

        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        listaEjesRegistros.add(new EjesRegistro(0, "Selecciones Uno", "Selecciones Uno", "Selecciones Uno", "Selecciones Uno"));
        ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal).forEach((t) -> {
            listaEjesRegistros.add(t);
        });

        listaActividadesPoas = ejbRegistroActividades.mostrarActividadesPoasTotalArea(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        if (!ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal).isEmpty()) {
            ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal).forEach((ej) -> {
                listaListaEjeEstrategia = new ArrayList<>();
                listaListaEjeEstrategia.clear();
                listaListaEjeEstrategia.add(new listaEjeEstrategia(ej, ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea())));
                if (!listaListaEjeEstrategia.isEmpty()) {
                    listaListaEjeEstrategia.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = ejbRegistroActividades.getActividadesPoasEstarategias(t, e.getEjess(), ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea());
                            listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));

                        });
                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje));
                        listaEstrategiaActividadesesEje = new ArrayList<>();
                        listaEstrategiaActividadesesEje.clear();
                    });
                }
            });
        }
        
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

    public void resetearValores() {
        actividadesPoa = new ActividadesPoa();
        listaActividadesPoas.clear();
        actividadesPoa = new ActividadesPoa();
    }

    public void consultarListas() {
        listaActividadesPoas = new ArrayList();
        listaActividadesPoas.clear();

        listaActividadesPoas = ejbRegistroActividades.mostrarActividadesPoasEje(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, ejesRegistro);
        

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();
         
        List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
        listaActividadesPoasFiltradas.clear();
        listaActividadesPoasFiltradas = ejbRegistroActividades.getActividadesPoasEstarategias(estrategias, ejesRegistro, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea());
        Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
        listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(estrategias, listaActividadesPoasFiltradas));

        Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
        ejesEsLaAp.add(new listaEjesEsLaAp(ejesRegistro, listaEstrategiaActividadesesEje));
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
                

    }

    public void asignarParametrosRegistro(ValueChangeEvent event) {
        if (Short.parseShort(event.getNewValue().toString()) != Short.parseShort("0")) {
            switch (event.getComponent().getId()) {
                case "eje":
                    ejesRegistro = new EjesRegistro();
                    estrategias = new Estrategias();
                    ejesRegistro = ejbCatalogosPoa.mostrarEjeRegistro(Integer.parseInt(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        listaEstrategias.clear();
                        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        ejbCatalogosPoa.getEstarategiasPorEje(ejesRegistro,ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea()).forEach((t) -> {
                            listaEstrategias.add(t);
                        });
                    }
                    resetearValores();
                    break;
                case "estrategia":
                    estrategias = new Estrategias();
                    estrategias = ejbCatalogosPoa.mostrarEstrategia(Short.parseShort(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        alineacionActiva = true;
                        consultarListas();
                    }
                    resetearValores();
                    break;
            }
        } else {
            ejesRegistro = new EjesRegistro();
            estrategias = new Estrategias();
            listaEstrategias.clear();
            consultarListasInit();
        }
    }

    public void actualizarNuavActividad() {
        actividadesPoa = ejbRegistroActividades.actualizaActividadesPoa(actividadesPoa);
        if(alineacionActiva=false){
            consultarListasInit();
        }else{
        consultarListas();
        }
        Faces.refresh();
    }
    
    public static class listaEjesEsLaAp {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<listaEstrategiaActividades> listalistaEstrategiaLaAp;

        public listaEjesEsLaAp(EjesRegistro ejeA, List<listaEstrategiaActividades> listalistaEstrategiaLaAp) {
            this.ejeA = ejeA;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }
    }

    public static class listaEjeEstrategia {

        @Getter        @Setter        private EjesRegistro ejess;
        @Getter        @Setter        private List<Estrategias> listaEstrategiases1;

        public listaEjeEstrategia(EjesRegistro ejess, List<Estrategias> listaEstrategiases1) {
            this.ejess = ejess;
            this.listaEstrategiases1 = listaEstrategiases1;
        }
    }

    public static class listaEstrategiaActividades {

        @Getter        @Setter        private Estrategias estrategias;
        @Getter        @Setter        private List<ActividadesPoa> actividadesPoas;

        public listaEstrategiaActividades(Estrategias estrategias, List<ActividadesPoa> actividadesPoas) {
            this.estrategias = estrategias;
            this.actividadesPoas = actividadesPoas;
        }
    }

    public void imprimirValores() {
    }

}
