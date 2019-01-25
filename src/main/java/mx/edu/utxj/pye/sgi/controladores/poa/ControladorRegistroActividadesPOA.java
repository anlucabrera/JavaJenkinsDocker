package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.ejb.poa.FacadePoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorRegistroActividadesPOA implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<ProductosAreas> productosAreases = new ArrayList<>();
    @Getter    @Setter    private List<CuadroMandoIntegral> cuadroMandoIntegrals = new ArrayList<>(),cuadroMandoIntegralArea = new ArrayList<>();
    @Getter    @Setter    private List<UnidadMedidas> unidadMedidases = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> lineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<PretechoFinanciero> pretechoFinancieros = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();
    @Getter    @Setter    private List<Productos> productoses = new ArrayList<>();
    @Getter    @Setter    private List<Partidas> partidases = new ArrayList<>(),partidasesSumatorias = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> listaActividadesPoas=new ArrayList<>(),listaActividadesPoasFiltroEje=new ArrayList<>(),listaActividadesPrincipales=new ArrayList<>(),listaActividadesSecundarias=new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> ejesesFiltrado = new ArrayList<>(),ejeses = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividads = new ArrayList<>(), recursosActividadsPorActividad = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoaTotalesCapitulos = new ArrayList<>(),cuadroMandoIntegralsRegistrados = new ArrayList<>();
    
    @Getter    @Setter    private Productos productoSeleccionado = new Productos();
    @Getter    @Setter    private RecursosActividad recursosActividad = new RecursosActividad();
    @Getter    @Setter    private CuadroMandoIntegral cuadroMandoIntegral;
    @Getter    @Setter    private Partidas partidas;
    @Getter    @Setter    private LineasAccion lineasAccion;
    @Getter    @Setter    private Estrategias estrategias;
    @Getter    @Setter    private EjesRegistro ejes;
    @Getter    @Setter    private UnidadMedidas nuevaUnidadMedidas = new UnidadMedidas();
    
    @Getter    @Setter    private Short unidadDMedida = 0, claveArea = 0, ejercicioFiscal = 0;
    @Getter    @Setter    private String tipo = "",claseP2="",claseP3="",claseP4="",claseP5="",clasePC="",actividadP="NO",nombreUnidad="";
    @Getter    @Setter    private Integer claveEje=0,numeroP = 1, numeroS = 1, numeroPEliminado = 1, numeroSEliminado = 1, totalNP = 0, totalActividadPrincipal,totalesAPCM=0,numeroPPasado = 0;
    @Getter    @Setter    private Integer mes1 = 0, mes2 = 0, mes3 = 0, mes4 = 0, mes5 = 0, mes6 = 0, mes7 = 0, mes8 = 0, mes9 = 0, mes10 = 0, mes11 = 0, mes12 = 0;
    @Getter    @Setter    private Double pretecho2000=0D,pretecho3000=0D,pretecho4000=0D,pretecho5000=0D,pretechoCPDD=0D,totalPretecho=0D;
    @Getter    @Setter    private Double totalRecursoActividad = 0D,totalCaptitulos=0D,totalCaptituloPartida = 0D,totalCaptitulo2000 = 0D,totalCaptitulo3000 = 0D,totalCaptitulo4000 = 0D,totalCaptitulo5000 = 0D,totalCaptituloCPDD = 0D;
    @Getter    @Setter    private Date fechaActual=new Date();
    @Getter    @Setter    private Boolean visible = false,nuevaUnidad=false;
    
    @Getter    @Setter    private List<capitulosLista> capitulo2000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo3000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo4000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo5000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capituloCPDD = new ArrayList<>();
    @Getter    @Setter    private List<reporteRegistros> reporte = new ArrayList<>();
    
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>(); 
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje= new ArrayList<>();
    
    @Getter    @Setter    private ActividadesPoa actividadesPoa = new ActividadesPoa(), actividadesPoaPrincipal = new ActividadesPoa(), actividadesPoaEditando = new ActividadesPoa();
    @Getter    @Setter    private ActividadesPoa poaEliminada=new ActividadesPoa();
    
    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {

        actividadesPoaEditando = new ActividadesPoa();
        actividadesPoa = new ActividadesPoa();

        ejeses.clear();
        partidases.clear();
        productoses.clear();
        lineasAccions.clear();
        estrategiases.clear();
        unidadMedidases.clear();
        productosAreases.clear();
        cuadroMandoIntegrals.clear();
        listaActividadesPoas.clear();
        listaActividadesPrincipales.clear();
        listaActividadesSecundarias.clear();
        listaActividadesPoasFiltroEje.clear();
                

        lineasAccion = new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Seleccione uno");
        estrategias = new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Seleccione uno");
        ejesesFiltrado.add(new EjesRegistro(0, "Seleccione uno","Seleccione uno","",""));

        lineasAccions.add(lineasAccion);
        estrategiases.add(estrategias);

        claveArea = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        ejercicioFiscal = Short.parseShort("17");

        consultarListas();
        limpiarParametros();
    }
    
    public void limpiarParametros() {
        tipo = "Actividad";
        unidadDMedida = 0;
        actividadP = "NO";
        actividadesPoa = new ActividadesPoa();
        numeroP = 1;
        nuevaUnidad = false;
        nombreUnidad="";
        actividadesPoaEditando=new ActividadesPoa();
    }

    // ---------------------------------------------------------------- Listas -------------------------------------------------------------
    public void consultarListas() {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarListas()"+claveArea);
        ejeses = poaSelectec.mostrarEjesRegistros();
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals("/poa/areas/registro.xhtml")) {
            cuadroMandoIntegrals = poaSelectec.mostrarCuadroMandoIntegrals(ejercicioFiscal);
            ejeses.forEach((t) -> {
                if (t.getEje() != 0) {
                    ejesesFiltrado.add(t);
                }
            });
        }else{
            generaListaEjes();
        }
        unidadMedidases = poaSelectec.mostrarUnidadMedidases();
        productosAreases = poaSelectec.mostrarProductosAreases(claveArea, ejercicioFiscal);
        listaActividadesPoas = poaSelectec.mostrarActividadesPoasArea(claveArea);
        recursosActividads=poaSelectec.mostrarRecursosActividad();
    }

    public void consultarActividadesPorParametros() {
        System.out.println("__________________________________________________________________________________________________________");
        unidadMedidases = poaSelectec.mostrarUnidadMedidases();
        listaActividadesPoasFiltroEje.clear();
        listaActividadesPrincipales.clear();
        listaActividadesSecundarias.clear();
        listaActividadesPoas.clear();

        listaListaEjeEstrategia.clear();
        listaEstrategiaActividadesesEje.clear();
        
        ejesEsLaAp.clear();

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(claveArea, ejercicioFiscal);
        listaListaEjeEstrategia.add(new listaEjeEstrategia(ejes, poaSelectec.getEstarategiasPorEje(ejes, ejercicioFiscal, claveArea)));

        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarActividadesPorParametros(1)" + listaListaEjeEstrategia.size());
        if (!listaListaEjeEstrategia.isEmpty()) {
            listaListaEjeEstrategia.forEach((e) -> {
                e.getListaEstrategiases1().forEach((t) -> {
                    List<ActividadesPoa> listaActividadesPoasFiltradas=new ArrayList<>();
                    listaActividadesPoasFiltradas=poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea);
                    Collections.sort(listaActividadesPoasFiltradas, (x,y) -> Short.compare(x.getCuadroMandoInt().getLineaAccion().getNumero(), y.getCuadroMandoInt().getLineaAccion().getNumero()));
                    Collections.sort(listaActividadesPoasFiltradas, (x,y) -> Short.compare(x.getNumeroP(), y.getNumeroP()));
                    listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                });
            });
            Collections.sort(listaEstrategiaActividadesesEje, (x,y) -> Short.compare(x.getEstrategias().getEstrategia(),y.getEstrategias().getEstrategia()));
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarActividadesPorParametros(2)" + listaEstrategiaActividadesesEje.size());
            ejesEsLaAp.add(new listaEjesEsLaAp(ejes, listaEstrategiaActividadesesEje));
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarActividadesPorParametros(3)" + ejesEsLaAp.size());
        } else {
            ejesEsLaAp.clear();
        }

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(claveArea, ejercicioFiscal);
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarActividadesPorParametros()" + listaActividadesPoas.size());
        listaActividadesPoas.forEach((t) -> {
            if (t.getCuadroMandoInt().equals(cuadroMandoIntegral)) {
                if (!listaActividadesPoasFiltroEje.contains(t)) {
                    listaActividadesPoasFiltroEje.add(t);

                }
            }
        });

        listaActividadesPoasFiltroEje.forEach((t) -> {
            if (t.getNumeroS() == 0) {
                listaActividadesPrincipales.add(t);
            } else {
                listaActividadesSecundarias.add(t);
            }
        });

        Collections.sort(listaActividadesPoasFiltroEje, (x, y) -> Short.compare(x.getNumeroP(), y.getNumeroP()));

        productosAreases.forEach((t) -> {
            if (!partidases.contains(t.getPartida())) {
                partidases.add(t.getPartida());
            }
        });

        obteneroTotalesCapitulo2000();
        obteneroTotalesCapitulo3000();
        obteneroTotalesCapitulo4000();
        obteneroTotalesCapitulo5000();
        obteneroTotalesCapituloCPDD();
        obtenerPretechos();
        obteneroTotalesFinales();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarActividadesPorParametros(4)" + listaActividadesPoasFiltroEje.size());

    }
  
    public void consultarParametrosRegistrados() {
        cuadroMandoIntegralsRegistrados.clear();
        reporte.clear();
        cuadroMandoIntegralsRegistrados = poaSelectec.mostrarActividadesPoasReporteArea(claveArea, ejercicioFiscal);
        cuadroMandoIntegralsRegistrados.forEach((t) -> {
            if (!cuadroMandoIntegralArea.contains(t.getCuadroMandoInt())) {
                cuadroMandoIntegralArea.add(t.getCuadroMandoInt());
            }
        });
        cuadroMandoIntegralArea.forEach((t) -> {
            totalesAPCM=0;
            cuadroMandoIntegralsRegistrados.forEach((a) -> {
                if (t.equals(a.getCuadroMandoInt())) {
                    totalesAPCM=totalesAPCM+1;
                }
            });
            reporte.add(new reporteRegistros(t, totalesAPCM));
        });
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.consultarActividadesPorParametros(3)" + reporte.size());
    }

    // ------------------------------------------------------------- Actividades ------------------------------------------------------------
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("�Operaci�n cancelada!!");
    }
      
    public void buscarActividadesVariasPlaneacionCuatrimestral(ActividadesPoa actividadesPoa) {
        Integer tamaño=0;
        unidadDMedida = 0;
        tipo = "";
        actividadP = "";
        numeroP = 0;
        numeroPPasado=0;

        tamaño=poaSelectec.mostrarSubActividadesPoa(claveArea, ejercicioFiscal, actividadesPoa.getNumeroP(),actividadesPoa.getCuadroMandoInt()).size();
        
        unidadDMedida = actividadesPoa.getUnidadMedida().getUnidadMedida();
        numeroP = Integer.parseInt(String.valueOf(actividadesPoa.getNumeroP()));
        numeroPPasado = Integer.parseInt(String.valueOf(actividadesPoa.getNumeroP()));
        cuadroMandoIntegral=actividadesPoa.getCuadroMandoInt();
        if (actividadesPoa.getNumeroS() == 0) {
            tipo = "Actividad";
        } else {
            tipo = "Subactividad";
        }

        if ("Actividad".equals(tipo)) {
            if (tamaño == 1) {
                actividadP = "NO";
            } else {
                actividadP = "SI";
            }
        } else {
            actividadP = "NO";
        }

        generaListaEjes();

    }
    
    public void onRowEdit() {
        listaActividadesPrincipales.clear();
        listaActividadesSecundarias.clear();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.onRowEdit()");
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        numeroS = 0;
        totalActividadPrincipal = 0;
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.onRowEdit()");    
        totalActividadPrincipal = actividadesPoaEditando.getNPEnero() + actividadesPoaEditando.getNPFebrero() + actividadesPoaEditando.getNPMarzo() + actividadesPoaEditando.getNPAbril() + actividadesPoaEditando.getNPMayo() + actividadesPoaEditando.getNPJunio() + actividadesPoaEditando.getNPJulio() + actividadesPoaEditando.getNPAgosto() + actividadesPoaEditando.getNPSeptiembre() + actividadesPoaEditando.getNPOctubre() + actividadesPoaEditando.getNPNoviembre() + actividadesPoaEditando.getNPDiciembre();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.onRowEdit(numeroP)"+numeroP);
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.onRowEdit(numeroPPasado)"+numeroPPasado);
        actividadesPoaEditando.setTotal(Short.parseShort(totalActividadPrincipal.toString()));  
        actividadesPoaEditando.setUnidadMedida(new UnidadMedidas(unidadDMedida));
        actividadesPoaEditando.setNumeroP(Short.parseShort(numeroP.toString()));
        actividadesPoaEditando.setCuadroMandoInt(cuadroMandoIntegral);
        
        poaSelectec.actualizaActividadesPoa(actividadesPoaEditando);
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.onRowEdit()");
        
        totalActividadPrincipal = 0;
        
        if (Short.parseShort(numeroPPasado.toString()) != actividadesPoaEditando.getNumeroP()) {
            poaSelectec.mostrarSubActividadesPoa(claveArea, ejercicioFiscal, Short.parseShort(numeroPPasado.toString()), actividadesPoaEditando.getCuadroMandoInt()).forEach((t) -> {
                if (t.getNumeroS() == 0) {
                    listaActividadesPrincipales.add(t);
                } else {
                    listaActividadesSecundarias.add(t);
                }
            });

            listaActividadesSecundarias.forEach((s) -> {
                if (s.getNumeroP() == numeroPPasado) {
                    numeroS = numeroS + 1;                    s.setNumeroS(Short.parseShort(numeroS.toString()));                    poaSelectec.actualizaActividadesPoa(s);
                    mes1 = mes1 + s.getNPEnero();                    mes2 = mes2 + s.getNPFebrero();
                    mes3 = mes3 + s.getNPMarzo();                    mes4 = mes4 + s.getNPAbril();
                    mes5 = mes5 + s.getNPMayo();                    mes6 = mes6 + s.getNPJunio();
                    mes7 = mes7 + s.getNPJulio();                    mes8 = mes8 + s.getNPAgosto();
                    mes9 = mes9 + s.getNPSeptiembre();                    mes10 = mes10 + s.getNPOctubre();
                    mes11 = mes11 + s.getNPNoviembre();                    mes12 = mes12 + s.getNPDiciembre();
                }
            });

            listaActividadesPrincipales.forEach((t) -> {
                if (t.getNumeroP() == numeroPPasado) {
                    totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                    t.setNPEnero(Short.parseShort(mes1.toString()));                    t.setNPFebrero(Short.parseShort(mes2.toString()));
                    t.setNPMarzo(Short.parseShort(mes3.toString()));                    t.setNPAbril(Short.parseShort(mes4.toString()));
                    t.setNPMayo(Short.parseShort(mes5.toString()));                    t.setNPJunio(Short.parseShort(mes6.toString()));
                    t.setNPJulio(Short.parseShort(mes7.toString()));                    t.setNPAgosto(Short.parseShort(mes8.toString()));
                    t.setNPSeptiembre(Short.parseShort(mes9.toString()));                    t.setNPOctubre(Short.parseShort(mes10.toString()));
                    t.setNPNoviembre(Short.parseShort(mes11.toString()));                    t.setNPDiciembre(Short.parseShort(mes12.toString()));
                    t.setTotal(Short.parseShort(totalActividadPrincipal.toString()));                    poaSelectec.actualizaActividadesPoa(t);
                }
            });

        }
        if(actividadesPoaEditando.getNumeroS()==0 && "y".equals(actividadesPoaEditando.getBandera())){
            
        }else{
        totalActividadPrincipal = 0;
        numeroS = 0;
        listaActividadesPrincipales.clear();
        listaActividadesSecundarias.clear();
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        totalActividadPrincipal = 0;

        poaSelectec.mostrarSubActividadesPoa(claveArea, ejercicioFiscal, actividadesPoaEditando.getNumeroP(), actividadesPoaEditando.getCuadroMandoInt()).forEach((t) -> {
            if (t.getNumeroS() == 0) {
                listaActividadesPrincipales.add(t);
            } else {
                listaActividadesSecundarias.add(t);
            }
        });
        
        listaActividadesSecundarias.forEach((s) -> {
            if (s.getNumeroP() == actividadesPoaEditando.getNumeroP()) {
                if (Short.parseShort(numeroPPasado.toString()) != actividadesPoaEditando.getNumeroP()) {
                    numeroS = numeroS + 1;                    s.setNumeroS(Short.parseShort(numeroS.toString()));                    poaSelectec.actualizaActividadesPoa(s);
                }
                mes1 = mes1 + s.getNPEnero();                mes2 = mes2 + s.getNPFebrero();
                mes3 = mes3 + s.getNPMarzo();                mes4 = mes4 + s.getNPAbril();
                mes5 = mes5 + s.getNPMayo();                mes6 = mes6 + s.getNPJunio();
                mes7 = mes7 + s.getNPJulio();                mes8 = mes8 + s.getNPAgosto();
                mes9 = mes9 + s.getNPSeptiembre();                mes10 = mes10 + s.getNPOctubre();
                mes11 = mes11 + s.getNPNoviembre();                mes12 = mes12 + s.getNPDiciembre();
            }
        });

        listaActividadesPrincipales.forEach((t) -> {
            if (t.getNumeroP() == actividadesPoaEditando.getNumeroP()) {
                totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                t.setNPEnero(Short.parseShort(mes1.toString()));                t.setNPFebrero(Short.parseShort(mes2.toString()));
                t.setNPMarzo(Short.parseShort(mes3.toString()));                t.setNPAbril(Short.parseShort(mes4.toString()));
                t.setNPMayo(Short.parseShort(mes5.toString()));                t.setNPJunio(Short.parseShort(mes6.toString()));
                t.setNPJulio(Short.parseShort(mes7.toString()));                t.setNPAgosto(Short.parseShort(mes8.toString()));
                t.setNPSeptiembre(Short.parseShort(mes9.toString()));                t.setNPOctubre(Short.parseShort(mes10.toString()));
                t.setNPNoviembre(Short.parseShort(mes11.toString()));                t.setNPDiciembre(Short.parseShort(mes12.toString()));
                t.setTotal(Short.parseShort(totalActividadPrincipal.toString()));                poaSelectec.actualizaActividadesPoa(t);
            }
        });
        }
        consultarListas();
        limpiarParametros();
        consultarActividadesPorParametros();
        consultarActividadesPorParametros();
    }

     public void asignarUnidadMedida(ValueChangeEvent event) {
        if (Integer.parseInt(event.getNewValue().toString()) == 0) {
            nuevaUnidad = true;
            unidadDMedida = 0;
        } else {
            nuevaUnidad = false;
            unidadDMedida = Short.parseShort(event.getNewValue().toString());
        }
    }
    
    public void asignarnombreUnidad(ValueChangeEvent event) {
        nombreUnidad = event.getNewValue().toString();
    }
    
    public void anadirNuavActividad() {
        if(visible==true){
       
            if (0 == unidadDMedida) {
                nuevaUnidadMedidas.setNombre(nombreUnidad);
                nuevaUnidadMedidas = poaSelectec.agregarUnidadMedidas(nuevaUnidadMedidas);
                unidadDMedida = nuevaUnidadMedidas.getUnidadMedida();
            }        
            
            
            mes1 = 0;            mes2 = 0;            mes3 = 0;            mes4 = 0;            mes5 = 0;            mes6 = 0;
            mes7 = 0;            mes8 = 0;            mes9 = 0;            mes10 = 0;            mes11 = 0;            mes12 = 0;
            numeroS = 1;
            totalActividadPrincipal = 0;
            if ("Actividad".equals(tipo)) {
                numeroP = 0;
                numeroP = listaActividadesPrincipales.size() + 1;
                actividadesPoa.setNumeroP(Short.parseShort(numeroP.toString()));
            } else {
                actividadesPoaPrincipal = new ActividadesPoa();
                listaActividadesPrincipales.forEach((t) -> {
                    if (t.getNumeroP() == numeroP) {
                        actividadesPoaPrincipal = t;
                    }
                });
                listaActividadesSecundarias.forEach((t) -> {
                    if (t.getNumeroP() == numeroP) {
                        numeroS = numeroS + 1;
                    }
                });
                actividadesPoa.setNumeroP(Short.parseShort(numeroP.toString()));
                actividadesPoa.setNumeroS(Short.parseShort(numeroS.toString()));
            }

            actividadesPoa.setCuadroMandoInt(new CuadroMandoIntegral(cuadroMandoIntegral.getCuadroMandoInt()));
            actividadesPoa.setUnidadMedida(new UnidadMedidas(unidadDMedida));
            actividadesPoa.setBandera("y");
            actividadesPoa.setArea(claveArea);

            actividadesPoa = poaSelectec.agregarActividadesPoa(actividadesPoa);

            consultarActividadesPorParametros();
            if ("Subactividad".equals(tipo)) {
                actividadesPoaPrincipal.setBandera("x");
                listaActividadesSecundarias.forEach((t) -> {
                    if (t.getNumeroP() == numeroP) {
                        mes1 = mes1 + t.getNPEnero();
                        mes2 = mes2 + t.getNPFebrero();
                        mes3 = mes3 + t.getNPMarzo();
                        mes4 = mes4 + t.getNPAbril();
                        mes5 = mes5 + t.getNPMayo();
                        mes6 = mes6 + t.getNPJunio();
                        mes7 = mes7 + t.getNPJulio();
                        mes8 = mes8 + t.getNPAgosto();
                        mes9 = mes9 + t.getNPSeptiembre();
                        mes10 = mes10 + t.getNPOctubre();
                        mes11 = mes11 + t.getNPNoviembre();
                        mes12 = mes12 + t.getNPDiciembre();
                    }
                });

                totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;

                actividadesPoaPrincipal.setNPEnero(Short.parseShort(mes1.toString()));
                actividadesPoaPrincipal.setNPFebrero(Short.parseShort(mes2.toString()));
                actividadesPoaPrincipal.setNPMarzo(Short.parseShort(mes3.toString()));
                actividadesPoaPrincipal.setNPAbril(Short.parseShort(mes4.toString()));
                actividadesPoaPrincipal.setNPMayo(Short.parseShort(mes5.toString()));
                actividadesPoaPrincipal.setNPJunio(Short.parseShort(mes6.toString()));
                actividadesPoaPrincipal.setNPJulio(Short.parseShort(mes7.toString()));
                actividadesPoaPrincipal.setNPAgosto(Short.parseShort(mes8.toString()));
                actividadesPoaPrincipal.setNPSeptiembre(Short.parseShort(mes9.toString()));
                actividadesPoaPrincipal.setNPOctubre(Short.parseShort(mes10.toString()));
                actividadesPoaPrincipal.setNPNoviembre(Short.parseShort(mes11.toString()));
                actividadesPoaPrincipal.setNPDiciembre(Short.parseShort(mes12.toString()));

                actividadesPoaPrincipal.setTotal(Short.parseShort(totalActividadPrincipal.toString()));

                poaSelectec.actualizaActividadesPoa(actividadesPoaPrincipal);
            }

            actividadesPoa = new ActividadesPoa();
            numeroP = 1;
            numeroS = 1;
            consultarActividadesPorParametros();
            limpiarParametros();
        } else {
            Messages.addGlobalWarn("Seleccione una alineación estratégica");
        }
    }

    public void actualizarNuavActividad() {
        if (!(actividadesPoaEditando.getDescripcion() == null || "".equals(actividadesPoaEditando.getDescripcion()))) {
            //actividadesPoa.setva.setValidadoSistema(true);
        }
        actividadesPoa = poaSelectec.actualizaActividadesPoa(actividadesPoaEditando);
        consultarActividadesPorParametros();
    }

    public void eliminarActividad(ActividadesPoa pOa) {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.eliminarActividad()"+pOa.getActividadPoa());
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        numeroP=1;
        listaActividadesPrincipales.clear();
        listaActividadesSecundarias.clear();
        poaEliminada=new ActividadesPoa();
        totalActividadPrincipal = 0;
        numeroPEliminado = Integer.parseInt(String.valueOf(pOa.getNumeroP()));
        numeroSEliminado = Integer.parseInt(String.valueOf(pOa.getNumeroS()));
        
        poaEliminada=pOa;
        
        poaSelectec.eliminarActividadesPoa(pOa);

        listaActividadesPoas.clear();
        
        listaActividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(claveArea, ejercicioFiscal);
        listaActividadesPoas.forEach((t) -> {
            if (t.getCuadroMandoInt().equals(poaEliminada.getCuadroMandoInt())) {
                if (t.getNumeroS() == 0) {
                    listaActividadesPrincipales.add(t);
                } else {
                    listaActividadesSecundarias.add(t);
                }
            }
        });

        if (numeroSEliminado == 0) {
            listaActividadesSecundarias.forEach((s) -> {
                if (s.getNumeroP() == pOa.getNumeroP()) {
                    poaSelectec.eliminarActividadesPoa(s);
                }
            });

            listaActividadesPrincipales.forEach((t) -> {
                listaActividadesSecundarias.forEach((s) -> {
                        if (t.getNumeroP() == s.getNumeroP()) {
                            s.setNumeroP(Short.valueOf(numeroP.toString()));
                            poaSelectec.actualizaActividadesPoa(s);
                        }
                });

                t.setNumeroP(Short.valueOf(numeroP.toString()));
                poaSelectec.actualizaActividadesPoa(t);
                numeroP = numeroP + 1;
            });

        } else {
            listaActividadesSecundarias.forEach((s) -> {
                if (numeroPEliminado == s.getNumeroP()) {
                    s.setNumeroS(Short.valueOf(numeroS.toString()));
                    poaSelectec.actualizaActividadesPoa(s);
                    numeroS = numeroS + 1;
                }
            });
            listaActividadesPrincipales.forEach((t) -> {
                if (t.getNumeroP() == numeroPEliminado) {
                    listaActividadesSecundarias.forEach((s) -> {
                        if (s.getNumeroP() == t.getNumeroP() && s.getNumeroS() != 0) {
                            mes1 = mes1 + s.getNPEnero();                            mes2 = mes2 + s.getNPFebrero();
                            mes3 = mes3 + s.getNPMarzo();                            mes4 = mes4 + s.getNPAbril();
                            mes5 = mes5 + s.getNPMayo();                            mes6 = mes6 + s.getNPJunio();
                            mes7 = mes7 + s.getNPJulio();                            mes8 = mes8 + s.getNPAgosto();
                            mes9 = mes9 + s.getNPSeptiembre();                            mes10 = mes10 + s.getNPOctubre();
                            mes11 = mes11 + s.getNPNoviembre();                            mes12 = mes12 + s.getNPDiciembre();
                        }
                        totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;

                        t.setNPEnero(Short.parseShort(mes1.toString()));                        t.setNPFebrero(Short.parseShort(mes2.toString()));
                        t.setNPMarzo(Short.parseShort(mes3.toString()));                        t.setNPAbril(Short.parseShort(mes4.toString()));
                        t.setNPMayo(Short.parseShort(mes5.toString()));                        t.setNPJunio(Short.parseShort(mes6.toString()));
                        t.setNPJulio(Short.parseShort(mes7.toString()));                        t.setNPAgosto(Short.parseShort(mes8.toString()));
                        t.setNPSeptiembre(Short.parseShort(mes9.toString()));                        t.setNPOctubre(Short.parseShort(mes10.toString()));
                        t.setNPNoviembre(Short.parseShort(mes11.toString()));                        t.setNPDiciembre(Short.parseShort(mes12.toString()));
                        t.setTotal(Short.parseShort(totalActividadPrincipal.toString()));                        poaSelectec.actualizaActividadesPoa(t);
                    });
                }
            });
        }
        consultarActividadesPorParametros();
    }

    public void asignarNumerosProgramados(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":
                actividadesPoa.setNPEnero(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes2":
                actividadesPoa.setNPFebrero(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes3":
                actividadesPoa.setNPMarzo(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes4":
                actividadesPoa.setNPAbril(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes5":
                actividadesPoa.setNPMayo(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes6":
                actividadesPoa.setNPJunio(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes7":
                actividadesPoa.setNPJulio(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes8":
                actividadesPoa.setNPAgosto(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes9":
                actividadesPoa.setNPSeptiembre(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes10":
                actividadesPoa.setNPOctubre(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes11":
                actividadesPoa.setNPNoviembre(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes12":
                actividadesPoa.setNPDiciembre(Short.parseShort(event.getNewValue().toString()));
                break;
        }
        totalNP = actividadesPoa.getNPEnero() + actividadesPoa.getNPFebrero() + actividadesPoa.getNPMarzo() + actividadesPoa.getNPAbril() + actividadesPoa.getNPMayo() + actividadesPoa.getNPJunio() + actividadesPoa.getNPJulio() + actividadesPoa.getNPAgosto() + actividadesPoa.getNPSeptiembre() + actividadesPoa.getNPOctubre() + actividadesPoa.getNPNoviembre() + actividadesPoa.getNPDiciembre();
        actividadesPoa.setTotal(Short.parseShort(totalNP.toString()));
    }

     public void asignarNumerosProgramadosEdicion(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":
                actividadesPoaEditando.setNPEnero(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes2":
                actividadesPoaEditando.setNPFebrero(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes3":
                actividadesPoaEditando.setNPMarzo(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes4":
                actividadesPoaEditando.setNPAbril(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes5":
                actividadesPoaEditando.setNPMayo(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes6":
                actividadesPoaEditando.setNPJunio(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes7":
                actividadesPoaEditando.setNPJulio(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes8":
                actividadesPoaEditando.setNPAgosto(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes9":
                actividadesPoaEditando.setNPSeptiembre(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes10":
                actividadesPoaEditando.setNPOctubre(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes11":
                actividadesPoaEditando.setNPNoviembre(Short.parseShort(event.getNewValue().toString()));
                break;
            case "mes12":
                actividadesPoaEditando.setNPDiciembre(Short.parseShort(event.getNewValue().toString()));
                break;
        }
        totalNP = actividadesPoaEditando.getNPEnero() + actividadesPoaEditando.getNPFebrero() + actividadesPoaEditando.getNPMarzo() + actividadesPoaEditando.getNPAbril() + actividadesPoaEditando.getNPMayo() + actividadesPoaEditando.getNPJunio() + actividadesPoaEditando.getNPJulio() + actividadesPoaEditando.getNPAgosto() + actividadesPoaEditando.getNPSeptiembre() + actividadesPoaEditando.getNPOctubre() + actividadesPoaEditando.getNPNoviembre() + actividadesPoaEditando.getNPDiciembre();
        actividadesPoaEditando.setTotal(Short.parseShort(totalNP.toString()));
    }

    
    // --------------------------------------------------------------- Recurso --------------------------------------------------------------
    public void eliminarRecursoActividad(RecursosActividad recursosActividad) {
        poaSelectec.eliminarRecursosActividad(recursosActividad);
        consultarListas();
        consultarActividadesPorParametros();
        asignaListaRecursoPorActividad(actividadesPoaEditando);
    }

    public void onCellEditProductos(RowEditEvent event) {
        RecursosActividad modificada = (RecursosActividad) event.getObject();
        totalRecursoActividad = modificada.getRPEnero() + modificada.getRPFebero() + modificada.getRPMarzo() + modificada.getRPAbril() + modificada.getRPMayo() + modificada.getRPJunio() + modificada.getRPJulio() + modificada.getRPAgosto() + modificada.getRPSeptiembre() + modificada.getRPOctubre() + modificada.getRPNoviembre() + modificada.getRPDiciembre();
        modificada.setTotal(totalRecursoActividad);
        poaSelectec.actualizaRecursosActividad(modificada);
        consultarListas();
        consultarActividadesPorParametros();
    }

    public void agregarRecursoyProductos() {
        recursosActividad.setActividadPoa(actividadesPoaEditando);

        recursosActividad.getActividadPoa();
        recursosActividad.getProductoArea();
        recursosActividad.getRPEnero();
        recursosActividad.getRPFebero();
        recursosActividad.getRPMarzo();
        recursosActividad.getRPAbril();
        recursosActividad.getRPMayo();
        recursosActividad.getRPJunio();
        recursosActividad.getRPJulio();
        recursosActividad.getRPAgosto();
        recursosActividad.getRPSeptiembre();
        recursosActividad.getRPOctubre();
        recursosActividad.getRPNoviembre();
        recursosActividad.getRPDiciembre();
        recursosActividad.getTotal();

        poaSelectec.agregarRecursosActividad(recursosActividad);
        consultarListas();
        consultarActividadesPorParametros();
        recursosActividad = new RecursosActividad();
        asignaListaRecursoPorActividad(actividadesPoaEditando);
    }

    public void asignarRevursoProgramados(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":
                recursosActividad.setRPEnero(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes2":
                recursosActividad.setRPFebero(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes3":
                recursosActividad.setRPMarzo(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes4":
                recursosActividad.setRPAbril(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes5":
                recursosActividad.setRPMayo(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes6":
                recursosActividad.setRPJunio(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes7":
                recursosActividad.setRPJulio(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes8":
                recursosActividad.setRPAgosto(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes9":
                recursosActividad.setRPSeptiembre(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes10":
                recursosActividad.setRPOctubre(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes11":
                recursosActividad.setRPNoviembre(Double.parseDouble(event.getNewValue().toString()));
                break;
            case "mes12":
                recursosActividad.setRPDiciembre(Double.parseDouble(event.getNewValue().toString()));
                break;
        }
        totalRecursoActividad = recursosActividad.getRPEnero() + recursosActividad.getRPFebero() + recursosActividad.getRPMarzo() + recursosActividad.getRPAbril() + recursosActividad.getRPMayo() + recursosActividad.getRPJunio() + recursosActividad.getRPJulio() + recursosActividad.getRPAgosto() + recursosActividad.getRPSeptiembre() + recursosActividad.getRPOctubre() + recursosActividad.getRPNoviembre() + recursosActividad.getRPDiciembre();
        recursosActividad.setTotal(totalRecursoActividad);
    }

    // -------------------------------------------------------------- Par�metros ------------------------------------------------------------
    public void generaListaEjes() {
        ejesesFiltrado.clear();
        ejesesFiltrado.add(new EjesRegistro(0, "Seleccione uno","Seleccione uno","",""));

        listaActividadesPoas.clear();
        cuadroMandoIntegrals.clear();
        
        listaActividadesPoas=poaSelectec.mostrarActividadesPoasArea(claveArea);
        
        listaActividadesPoas.forEach((t) -> {
            if (!cuadroMandoIntegrals.contains(t.getCuadroMandoInt())) {
                cuadroMandoIntegrals.add(t.getCuadroMandoInt());
            }
        });
        
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.generaListaEjes(1)"+cuadroMandoIntegrals.size());

        cuadroMandoIntegrals.forEach((t) -> {
            ejeses.forEach((e) -> {
                if (t.getEje().equals(e)) {
                    if (!ejesesFiltrado.contains(e)) {
                        ejesesFiltrado.add(e);
                    }
                }
            });
        });
        
        Collections.sort(ejesesFiltrado, (x, y) -> Integer.compare(x.getEje(), y.getEje()));
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.generaListaEjes(2)"+ejesesFiltrado.size());
    }

    public void asignaEstrategias(ValueChangeEvent event) {
        visible = false;
        lineasAccions.clear();
        lineasAccion = new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Seleccione uno");
        lineasAccions.add(lineasAccion);
        ejes = new EjesRegistro();

        estrategiases.clear();
        estrategias = new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Seleccione uno");
        estrategiases.add(estrategias);

        if (event.getNewValue() != null) {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaEstrategias()" + event.getNewValue().toString());
            ejesesFiltrado.forEach((t) -> {
                if (t.getEje() == Integer.parseInt(event.getNewValue().toString())) {
                    ejes = t;
                }
            });
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaEstrategias()" + ejes.getNombre());
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaEstrategias()" + claveEje);
            claveEje = ejes.getEje();
            cuadroMandoIntegrals.forEach((t) -> {
                if (claveEje.equals(t.getEje().getEje())) {
                    if (!estrategiases.contains(t.getEstrategia())) {
                        estrategiases.add(t.getEstrategia());
                    }
                }
            });
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaEstrategias()" + estrategiases);
            Collections.sort(estrategiases, (x, y) -> Short.compare(x.getNumero(), y.getNumero()));
            limpiarParametros();
        }
    }

    public void asignaLineasAccion(ValueChangeEvent event2) {
        lineasAccions.clear();
        lineasAccion = new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Seleccione uno");
        lineasAccions.add(lineasAccion);
        estrategias = new Estrategias();
        if (event2.getNewValue() != null) {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaLineasAccion()" + event2.getNewValue().toString());
            estrategiases.forEach((t) -> {
                if (t.getEstrategia() == Short.parseShort(event2.getNewValue().toString())) {
                    estrategias = t;
                }
            });
            cuadroMandoIntegrals.forEach((t) -> {
                if (Objects.equals(t.getEjercicioFiscal().getEjercicioFiscal(), ejercicioFiscal)) {
                    if (t.getEstrategia().equals(estrategias)) {
                        if (!lineasAccions.contains(t.getLineaAccion())) {
                            lineasAccions.add(t.getLineaAccion());
                        }
                    }
                }
            });
            Collections.sort(lineasAccions, (x, y) -> Short.compare(x.getNumero(), y.getNumero()));
                            }
    }

    public void asignaLineaAccionSeleccionada(ValueChangeEvent event3) {
        lineasAccion = new LineasAccion();
        if (event3.getNewValue() != null) {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaLineaAccionSeleccionada()" + event3.getNewValue().toString());
            lineasAccions.forEach((t) -> {
                if (t.getLineaAccion() == Short.parseShort(event3.getNewValue().toString())) {
                    lineasAccion = t;
                    visible = true;
                }
            });
            asignaCuadroMando();
        }
    }

    public void asignaCuadroMando() {
        cuadroMandoIntegral = new CuadroMandoIntegral();
        cuadroMandoIntegrals.forEach((t) -> {
            if (Objects.equals(t.getEje().getEje(), claveEje) && t.getEstrategia() == estrategias && t.getLineaAccion() == lineasAccion) {
                cuadroMandoIntegral = t;
            }
        });
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignaCuadroMando()"+cuadroMandoIntegral);
        consultarActividadesPorParametros();
    }

    public void asignaListaRecursoPorActividad(ActividadesPoa nuevaActividadesPoaEditada) {
        recursosActividadsPorActividad.clear();
        recursosActividads.forEach((t) -> {
            if (t.getActividadPoa().equals(nuevaActividadesPoaEditada)) {
                if (!recursosActividadsPorActividad.contains(t)) {
                    recursosActividadsPorActividad.add(t);
                }
            }
        });
    }

    public void asignaProductos(ValueChangeEvent event2) {
        partidas = new Partidas(Short.parseShort(String.valueOf(event2.getNewValue().toString())));
        productoses.clear();
        productosAreases.forEach((t) -> {
            if (t.getPartida().getPartida() == Short.parseShort(String.valueOf(event2.getNewValue().toString()))) {
                if (!productoses.contains(t.getProductos())) {
                    productoses.add(t.getProductos());
                }
            }
        });
    }

    public void asignaRecursoProductos(ValueChangeEvent event2) {
        productoses.forEach((t) -> {
            if (t.getProductosPK().getProducto().equals(event2.getNewValue().toString())) {
                productoSeleccionado = t;
            }
        });

        productosAreases.forEach((t) -> {
            if (partidas.equals(t.getPartida())) {
                if (productoSeleccionado.equals(t.getProductos())) {
                    recursosActividad.setProductoArea(t);
                }
            }
        });
    }

    public void asignarNumeroP(ValueChangeEvent event) {
        numeroP=0;
        numeroPPasado=0;
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignarNumeroP()"+event.getNewValue().toString());
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.asignarNumeroP()"+event.getOldValue().toString());
        numeroP = Integer.parseInt(event.getNewValue().toString());
        numeroPPasado=Integer.parseInt(event.getOldValue().toString());
    }

    // ------------------------------------------------------------- Totales Capitulo -----------------------------------------------------------  
    public void obteneroTotalesCapitulo2000() {
        totalCaptitulo2000 = 0D;
        capitulo2000.clear();
        partidasesSumatorias.clear();
        totalCaptituloPartida = 0D;

        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.obteneroTotalesCapitulo2000()"+listaActividadesPoas.size());
        listaActividadesPoas.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo()== 2) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOA.obteneroTotalesCapitulo2000()"+listaActividadesPoas.size());
        partidasesSumatorias.forEach((p) -> {
            listaActividadesPoas.forEach((a) -> {
                a.getRecursosActividadList().forEach((r) -> {
                    if (p == r.getProductoArea().getPartida()) {
                        totalCaptituloPartida = totalCaptituloPartida + r.getTotal();
                    }
                });
            });
            capitulo2000.add(new capitulosLista(p, totalCaptituloPartida));
            totalCaptituloPartida = 0D;
        });

        capitulo2000.forEach((t) -> {
            totalCaptitulo2000 = totalCaptitulo2000 + t.getTotal();
        });
    }

    public void obteneroTotalesCapitulo3000() {
        totalCaptitulo3000 = 0D;
        capitulo3000.clear();
        partidasesSumatorias.clear();
        totalCaptituloPartida = 0D;
        listaActividadesPoas.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 3) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            listaActividadesPoas.forEach((a) -> {
                a.getRecursosActividadList().forEach((r) -> {
                    if (p == r.getProductoArea().getPartida()) {
                        totalCaptituloPartida = totalCaptituloPartida + r.getTotal();
                    }
                });
            });
            capitulo3000.add(new capitulosLista(p, totalCaptituloPartida));
            totalCaptituloPartida = 0D;
        });

        capitulo3000.forEach((t) -> {
            totalCaptitulo3000 = totalCaptitulo3000 + t.getTotal();
        });
    }

    public void obteneroTotalesCapitulo4000() {
        totalCaptitulo4000 = 0D;
        capitulo4000.clear();
        partidasesSumatorias.clear();
        totalCaptituloPartida = 0D;
        listaActividadesPoas.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 4) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            listaActividadesPoas.forEach((a) -> {
                a.getRecursosActividadList().forEach((r) -> {
                    if (p == r.getProductoArea().getPartida()) {
                        totalCaptituloPartida = totalCaptituloPartida + r.getTotal();
                    }
                });
            });
            capitulo4000.add(new capitulosLista(p, totalCaptituloPartida));
            totalCaptituloPartida = 0D;
        });

        capitulo4000.forEach((t) -> {
            totalCaptitulo4000 = totalCaptitulo4000 + t.getTotal();
        });
    }

    public void obteneroTotalesCapitulo5000() {
        totalCaptitulo5000 = 0D;
        capitulo5000.clear();
        partidasesSumatorias.clear();
        totalCaptituloPartida = 0D;
        listaActividadesPoas.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 5) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            listaActividadesPoas.forEach((a) -> {
                a.getRecursosActividadList().forEach((r) -> {
                    if (p == r.getProductoArea().getPartida()) {
                        totalCaptituloPartida = totalCaptituloPartida + r.getTotal();
                    }
                });
            });
            capitulo5000.add(new capitulosLista(p, totalCaptituloPartida));
            totalCaptituloPartida = 0D;
        });

        capitulo5000.forEach((t) -> {
            totalCaptitulo5000 = totalCaptitulo5000 + t.getTotal();
        });
    }

    public void obteneroTotalesCapituloCPDD() {
        totalCaptituloCPDD = 0D;
        capituloCPDD.clear();
        partidasesSumatorias.clear();
        totalCaptituloPartida = 0D;
        listaActividadesPoas.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo().getCapituloTipo() == 6) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            listaActividadesPoas.forEach((a) -> {
                a.getRecursosActividadList().forEach((r) -> {
                    if (p == r.getProductoArea().getPartida()) {
                        totalCaptituloPartida = totalCaptituloPartida + r.getTotal();
                    }
                });
            });
            capituloCPDD.add(new capitulosLista(p, totalCaptituloPartida));
            totalCaptituloPartida = 0D;
        });

        capituloCPDD.forEach((t) -> {
            totalCaptituloCPDD = totalCaptituloCPDD + t.getTotal();
        });
    }

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

    public void obteneroTotalesFinales() {
        if (totalCaptitulo2000 < pretecho2000) {
            claseP2 = "mayor";
        }
        if (totalCaptitulo3000 < pretecho3000) {
            claseP3 = "mayor";
        }
        if (totalCaptitulo4000 < pretecho4000) {
            claseP4 = "mayor";
        }
        if (totalCaptitulo5000 < pretecho5000) {
            claseP5 = "mayor";
        }
        if (totalCaptituloCPDD < pretechoCPDD) {
            clasePC = "mayor";
        }
        totalCaptitulos = totalCaptitulo2000 + totalCaptitulo3000 + totalCaptitulo4000 + totalCaptitulo5000 + totalCaptituloCPDD;

    }
    // ---------------------------------------------------------------- Bases --------------------------------------------------------------    

    public void imprimirValores() {
    }

    public static class capitulosLista {

        @Getter        @Setter        private Partidas partidas1;
        @Getter        @Setter        private Double total;

        public capitulosLista(Partidas partidas1, Double total) {
            this.partidas1 = partidas1;
            this.total = total;
        }
    }
    
     public static class reporteRegistros {

        @Getter        @Setter        private CuadroMandoIntegral cuadroMandoIntegral1;
        @Getter        @Setter        private Integer totalRegistros;

        public reporteRegistros(CuadroMandoIntegral cuadroMandoIntegral1, Integer totalRegistros) {
            this.cuadroMandoIntegral1 = cuadroMandoIntegral1;
            this.totalRegistros = totalRegistros;
        }
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
}
