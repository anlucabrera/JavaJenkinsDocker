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
import javax.annotation.PreDestroy;
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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPresupuestacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorRegistroActividadesPOAPyE implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoasAreas = new ArrayList<>(), actividadesPoasAreasEjes = new ArrayList<>(),actividadesPoasAreasConRegistros=new ArrayList<>();
    @Getter    @Setter    private ActividadesPoa actividadesPoaEditando = new ActividadesPoa();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad();    
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>(),areasUniversidadsRegistros = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> ejesesFiltrado = new ArrayList<>(),ejeses = new ArrayList<>();
    @Getter    @Setter    private EjesRegistro ejes;
    @Getter    @Setter    private List<CuadroMandoIntegral> cuadroMandoIntegrals = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> lineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0;
    @Getter    @Setter    private Integer claveEje=0;
    @Getter    @Setter    private Date fechaActual=new Date();
    
    @Getter    @Setter    private Boolean ecxiste=false,liberado=true;
    @Getter    @Setter    private String claseP1="",claseP2="",claseP3="",claseP4="",clasePC="",clasePT="";
    @Getter    @Setter    private List<PretechoFinanciero> pretechoFinancieros = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividads2 = new ArrayList<>(),recursosActividads3 = new ArrayList<>(),recursosActividads4 = new ArrayList<>(),recursosActividads5 = new ArrayList<>(),recursosActividadscdh = new ArrayList<>();
    @Getter    @Setter    private Double pretecho2000=0D,pretecho3000=0D,pretecho4000=0D,pretecho5000=0D,pretechoCPDD=0D,totalPretecho=0D;
    @Getter    @Setter    private Double totalCaptitulos=0D,totalCaptitulo2000 = 0D,totalCaptitulo3000 = 0D,totalCaptitulo4000 = 0D,totalCaptitulo5000 = 0D,totalCaptituloCPDD = 0D;
    
    @Getter    @Setter    private List<capitulosLista> capitulo2000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo3000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo4000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo5000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capituloCPDD = new ArrayList<>();
    
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    
    @EJB    EjbPoaSelectec poaSelectec;
    @EJB    EjbPresupuestacion presupuestacion;
    @Inject    ControladorEmpleado controladorEmpleado;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    @EJB    EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    Facade f;

    @PostConstruct
    public void init() {
        System.out.println("ControladorHabilidadesIIL Inicio: " + System.currentTimeMillis());
        ejeses.clear();
        ejesesFiltrado.clear();
        actividadesPoasAreas.clear();
        actividadesPoasAreasEjes.clear();
        actividadesPoasAreas.clear();
        ejes = new EjesRegistro(0);

        claveArea = 0;
        ejercicioFiscal = Short.parseShort(String.valueOf(fechaActual.getYear() - 101));

        consultarListas();

        System.out.println(" ControladorHabilidadesIIL Fin: " + System.currentTimeMillis());
    }

//     @PreDestroy
//    public void fin(){
//        actividadesPoasAreas.forEach((t) -> {
//            f.getEntityManager().detach(t);
//        });
//    }
    
    public void consultarListas() {
        try {
            actividadesPoasAreasConRegistros.clear();
            areasUniversidadsRegistros.clear();
            areasUniversidads.clear();
            actividadesPoasAreas.clear();

            actividadesPoasAreasConRegistros = poaSelectec.mostrarAreasQueRegistraronActividades();
            areasUniversidads = ejbAreasLogeo.mostrarAreasUniversidadActivas();
            areasUniversidadsRegistros.add(new AreasUniversidad(Short.parseShort("0"), "Seleccione uno", "Seleccione uno", "1", false));
            if (!actividadesPoasAreasConRegistros.isEmpty()) {
                actividadesPoasAreasConRegistros.forEach((t) -> {
                    areasUniversidads.forEach((a) -> {
                        if (t.getArea() == a.getArea()) {
                            areasUniversidadsRegistros.add(a);
                        }
                    });
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEvaluacionActividadesPyE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ---------------------------------------------------------------- Listas -------------------------------------------------------------
    public void consultarListas(ValueChangeEvent event) {
        ejesesFiltrado.clear();
        ejesesFiltrado.add(new EjesRegistro(0, "Seleccione uno", "Seleccione uno", "", ""));

        claveArea = Short.parseShort(event.getNewValue().toString());

        if (!poaSelectec.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).isEmpty()) {
            ejesesFiltrado.addAll(poaSelectec.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal));
            Collections.sort(ejesesFiltrado, (x, y) -> Integer.compare(x.getEje(), y.getEje()));
        }
        
    }
    
    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            claveArea = 0;
            areaPOASeleccionada = new AreasUniversidad();
            procesopoa = new Procesopoa();
            claveArea = Short.parseShort(event.getNewValue().toString());
            areaPOASeleccionada = ejbAreasLogeo.mostrarAreasUniversidad(claveArea);
            procesopoa = ejbUtilidadesCH.mostrarEtapaPOA(claveArea);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorRegistroActividadesPOAPyE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarListasValidacionFinal() {
        ejeses.clear();
        ejesesFiltrado.clear();
        cuadroMandoIntegrals.clear();
        listaListaEjeEstrategia.clear();

        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        actividadesPoasAreas = poaSelectec.mostrarActividadesPoasArea(claveArea);
        if (!actividadesPoasAreas.isEmpty()) {
            actividadesPoasAreas.forEach((t) -> {
                if (!t.getValidadpPyeFinal()) {
                    liberado = false;
                }
            });
        }

        if (!poaSelectec.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).isEmpty()) {
            poaSelectec.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).forEach((ej) -> {
                listaListaEjeEstrategia = new ArrayList<>();
                listaListaEjeEstrategia.clear();
                listaListaEjeEstrategia.add(new listaEjeEstrategia(ej, poaSelectec.getEstarategiasPorEje(ej, ejercicioFiscal, claveArea)));
                if (!listaListaEjeEstrategia.isEmpty()) {
                    listaListaEjeEstrategia.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea);
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
        obtenerPretechos();
        obteneroTotalesCapitulos();
        obteneroTotalesFinales();
        obteneroTotalesCapitulosDesglosado();
    }

    public void consultarEje(ValueChangeEvent event) {
        ejesesFiltrado.forEach((t) -> {
            if(t.getEje()==Integer.parseInt(event.getNewValue().toString())){
                ejes=t;
            }
        });
        claveEje=ejes.getEje();
    }

    public void generaListaActividades() {
        actividadesPoasAreasEjes.clear();
        actividadesPoasAreas.forEach((t) -> {
            if (t.getCuadroMandoInt().getEje().equals(ejes)) {
                if (!actividadesPoasAreasEjes.contains(t)) {
                    actividadesPoasAreasEjes.add(t);
                }
            }
        });
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    public void obtenerPretechos() {
        pretechoFinancieros.clear();
        pretechoFinancieros = poaSelectec.mostrarPretechoFinancieros(claveArea, ejercicioFiscal);
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

        recursosActividads2=presupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("2"));
        recursosActividads3=presupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("3"));
        recursosActividads4=presupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("4"));
        recursosActividads5=presupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("5"));
        recursosActividadscdh=presupuestacion.mostrarRecursosActividad(claveArea, ejercicioFiscal, Short.parseShort("6"));
        
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
                            capitulo2000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo2000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
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
                            capitulo3000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo3000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
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
                            capitulo4000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo4000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
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
                            capitulo5000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capitulo5000.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
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
                            capituloCPDD.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                        }
                        ecxiste = false;
                    } else {
                        ecxiste = false;
                        capituloCPDD.add(new capitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                }
            });
        }
    }

       public void onRowEditAc(RowEditEvent event) {
        try {
            ActividadesPoa ap = (ActividadesPoa) event.getObject();
            poaSelectec.actualizaActividadesPoa(ap);            
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
    
    public void validarPlaneacion() {
         actividadesPoasAreas.forEach((t) -> {
            t.setValidadoPyE(true);
            poaSelectec.actualizaActividadesPoa(t);
        });
        liberado=true;
    }

    public void validarFinanzas(ActividadesPoa actividadesPoa) {
        actividadesPoa.setValidadoFinanzas(!actividadesPoa.getValidadoFinanzas());
        poaSelectec.actualizaActividadesPoa(actividadesPoa);
        consultarListasValidacionFinal();
    }

    public void validarPyEFinal() {
        actividadesPoasAreas.forEach((t) -> {
            t.setValidadpPyeFinal(true);
            poaSelectec.actualizaActividadesPoa(t);
        });
        liberado=true;
    }

    public void imprimirValores() {
        System.out.println("mx.edu.utxj.pye.sgi.poa.controladores.imprimirValores()");
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

    public static class capitulosLista {

        @Getter        @Setter        private Partidas partidas1;
        @Getter        @Setter        private Double total;

        public capitulosLista(Partidas partidas1, Double total) {
            this.partidas1 = partidas1;
            this.total = total;
        }
    }
}

