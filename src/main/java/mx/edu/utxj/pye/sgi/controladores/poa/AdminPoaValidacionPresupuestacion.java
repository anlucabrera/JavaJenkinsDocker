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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPresupuestacion;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class AdminPoaValidacionPresupuestacion implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidadsRegistros = new ArrayList<>();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad();
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    @Getter    @Setter    private List<ListaEjesEsLaAp> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<ListaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    @Getter    @Setter    private String mss="";
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0; 
    @Getter    @Setter    private List<ListaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    
    @Getter    @Setter    private Boolean ecxiste=false;
    @Getter    @Setter    private String claseP1="",claseP2="",claseP3="",claseP4="",clasePC="",clasePT="";
    @Getter    @Setter    private List<PretechoFinanciero> pretechoFinancieros = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividads2 = new ArrayList<>(),recursosActividads3 = new ArrayList<>(),recursosActividads4 = new ArrayList<>(),recursosActividads5 = new ArrayList<>(),recursosActividadscdh = new ArrayList<>();
    @Getter    @Setter    private Double pretecho2000=0D,pretecho3000=0D,pretecho4000=0D,pretecho5000=0D,pretechoCPDD=0D,totalPretecho=0D;
    @Getter    @Setter    private Double totalCaptitulos=0D,totalCaptitulo2000 = 0D,totalCaptitulo3000 = 0D,totalCaptitulo4000 = 0D,totalCaptitulo5000 = 0D,totalCaptituloCPDD = 0D;
    
    @Getter    @Setter    private List<CapitulosLista> capitulo2000 = new ArrayList<>();
    @Getter    @Setter    private List<CapitulosLista> capitulo3000 = new ArrayList<>();
    @Getter    @Setter    private List<CapitulosLista> capitulo4000 = new ArrayList<>();
    @Getter    @Setter    private List<CapitulosLista> capitulo5000 = new ArrayList<>();
    @Getter    @Setter    private List<CapitulosLista> capituloCPDD = new ArrayList<>();
    
    
    
   @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbPresupuestacion ejbPresupuestacion;
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
            Logger.getLogger(ControladorEvaluacionActividadesPyE.class.getName()).log(Level.SEVERE, null, ex);
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
            procesopoa = ejbUtilidadesCH.mostrarEtapaPOA(claveArea);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaValidacionPresupuestacion.class.getName()).log(Level.SEVERE, null, ex);
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
        obtenerPretechos();
        obteneroTotalesCapitulos();
        obteneroTotalesFinales();
        obteneroTotalesCapitulosDesglosado();
    }

    public void enviarmensajes(String proceso) {
        utilidadesPOA.enviarCorreo(proceso, "Ad", false, mss, areaPOASeleccionada);
    }
    
    public void imprimirValores() {
        System.out.println("mx.edu.utxj.pye.sgi.poa.controladores.imprimirValores()");
    }

    public void obtenerPretechos() {
        pretechoFinancieros.clear();
        pretechoFinancieros = ejbPresupuestacion.mostrarPretechoFinancieros(claveArea, ejercicioFiscal);
        pretechoFinancieros.forEach((t) -> {
            switch (t.getCapituloTipo().getCapituloTipo()) {
                case 2:
                    pretecho2000 = t.getMonto();
                    break;
                case 3:
                    pretecho3000 = t.getMonto();
                    break;
                case 4:
                    pretecho4000 = t.getMonto();
                    break;
                case 5:
                    pretecho5000 = t.getMonto();
                    break;
                case 6:
                    pretechoCPDD = t.getMonto();
                    break;
            }
        });
        totalPretecho = pretecho2000 + pretecho3000 + pretecho4000 + pretecho5000 + pretechoCPDD;
    }
    
    public void obteneroTotalesCapitulos() {
        recursosActividads2 = new ArrayList<>();        recursosActividads3 = new ArrayList<>();
        recursosActividads4 = new ArrayList<>();        recursosActividads5 = new ArrayList<>();
        recursosActividadscdh = new ArrayList<>();

        recursosActividads2.clear();        recursosActividads3.clear();
        recursosActividads4.clear();        recursosActividads5.clear();
        recursosActividadscdh.clear();

        recursosActividads2=ejbPresupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("2"));
        recursosActividads3=ejbPresupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("3"));
        recursosActividads4=ejbPresupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("4"));
        recursosActividads5=ejbPresupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("5"));
        recursosActividadscdh=ejbPresupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("6"));
        
        totalCaptitulo2000 = 0D;
        totalCaptitulo3000 = 0D;
        totalCaptitulo4000 = 0D;
        totalCaptitulo5000 = 0D;
        totalCaptituloCPDD = 0D;

        if (!recursosActividads2.isEmpty()) {            recursosActividads2.forEach((t) -> {                totalCaptitulo2000 = totalCaptitulo2000 + t.getTotal();            });        }
        if (!recursosActividads3.isEmpty()) {            recursosActividads3.forEach((t) -> {                totalCaptitulo3000 = totalCaptitulo3000 + t.getTotal();            });        }
        if (!recursosActividads4.isEmpty()) {            recursosActividads4.forEach((t) -> {                totalCaptitulo4000 = totalCaptitulo4000 + t.getTotal();            });        }
        if (!recursosActividads5.isEmpty()) {            recursosActividads5.forEach((t) -> {                totalCaptitulo5000 = totalCaptitulo5000 + t.getTotal();            });        }
        if (!recursosActividadscdh.isEmpty()) {          recursosActividadscdh.forEach((t) -> {               totalCaptituloCPDD = totalCaptituloCPDD + t.getTotal();            });        }
        
    }

    public void obteneroTotalesFinales() {
        totalCaptitulos = totalCaptitulo2000 + totalCaptitulo3000 + totalCaptitulo4000 + totalCaptitulo5000 + totalCaptituloCPDD;
        if (totalCaptitulo2000 < pretecho2000) {            claseP1 = "mayor";        }
        if (totalCaptitulo3000 < pretecho3000) {            claseP2 = "mayor";        }
        if (totalCaptitulo4000 < pretecho4000) {            claseP3 = "mayor";        }
        if (totalCaptitulo5000 < pretecho5000) {            claseP4 = "mayor";        }
        if (totalCaptituloCPDD < pretechoCPDD) {            clasePC = "mayor";        }
        if (totalCaptitulos < totalPretecho) {            clasePT = "mayor";        }
    }
    
    public void obteneroTotalesCapitulosDesglosado() {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOAPyE.obteneroTotalesCapitulosDesglosado()");
        capitulo2000 = new ArrayList<>();
        capitulo2000.clear();
        capitulo3000 = new ArrayList<>();
        capitulo3000.clear();
        capitulo4000 = new ArrayList<>();
        capitulo4000.clear();
        capitulo5000 = new ArrayList<>();
        capitulo5000.clear();
        capituloCPDD = new ArrayList<>();
        capituloCPDD.clear();
        ecxiste = false;

        if (!recursosActividads2.isEmpty()) {
            recursosActividads2.forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 2) {
                    if (!capitulo2000.isEmpty()) {
                        capitulo2000.forEach((c) -> {
                            if (c.getPartidas1().equals(t.getProductoArea().getPartida())) {
                                c.setTotal(c.getTotal() + t.getTotal());
                                ecxiste = true;
                            }
                        });
                        if (!ecxiste) {
                            capitulo2000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo2000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                }
            });
        }
        ecxiste = false;
        if (!recursosActividads3.isEmpty()) {
            recursosActividads3.forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 3) {
                    if (!capitulo3000.isEmpty()) {
                        capitulo3000.forEach((c) -> {
                            if (c.getPartidas1().equals(t.getProductoArea().getPartida())) {
                                c.setTotal(c.getTotal() + t.getTotal());
                                ecxiste = true;
                            }
                        });
                        if (!ecxiste) {
                            capitulo3000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo3000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                }
            });
        }
        ecxiste = false;
        if (!recursosActividads4.isEmpty()) {
            recursosActividads4.forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 4) {
                    if (!capitulo4000.isEmpty()) {
                        capitulo4000.forEach((c) -> {
                            if (c.getPartidas1().equals(t.getProductoArea().getPartida())) {
                                c.setTotal(c.getTotal() + t.getTotal());
                                ecxiste = true;
                            }
                        });
                        if (!ecxiste) {
                            capitulo4000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo4000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                }
            });
        }
        ecxiste = false;
        if (!recursosActividads5.isEmpty()) {
            recursosActividads5.forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 5) {
                    if (!capitulo5000.isEmpty()) {
                        capitulo5000.forEach((c) -> {
                            if (c.getPartidas1().equals(t.getProductoArea().getPartida())) {
                                c.setTotal(c.getTotal() + t.getTotal());
                                ecxiste = true;
                            }
                        });
                        if (!ecxiste) {
                            capitulo5000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo5000.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                }
            });
        }
        ecxiste = false;
        if (!recursosActividadscdh.isEmpty()) {
            recursosActividadscdh.forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 6) {
                    if (!capituloCPDD.isEmpty()) {
                        capituloCPDD.forEach((c) -> {
                            if (c.getPartidas1().equals(t.getProductoArea().getPartida())) {
                                c.setTotal(c.getTotal() + t.getTotal());
                                ecxiste = true;
                            }
                        });
                        if (!ecxiste) {
                            capituloCPDD.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capituloCPDD.add(new CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                }
            });
        }
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

    public static class CapitulosLista {

        @Getter        @Setter        private Partidas partidas1;
        @Getter        @Setter        private Double total;

        public CapitulosLista(Partidas partidas1, Double total) {
            this.partidas1 = partidas1;
            this.total = total;
        }
    }
}

