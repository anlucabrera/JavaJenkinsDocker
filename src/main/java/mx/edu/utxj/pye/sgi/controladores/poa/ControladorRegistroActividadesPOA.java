package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
import mx.edu.utxj.pye.sgi.entity.poa.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.poa.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.poa.Estrategias;
import mx.edu.utxj.pye.sgi.entity.poa.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.poa.Partidas;
import mx.edu.utxj.pye.sgi.entity.poa.Productos;
import mx.edu.utxj.pye.sgi.entity.poa.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.poa.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.poa.UnidadMedidas;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import org.omnifaces.facesviews.FacesViews;
import org.omnifaces.util.Faces;
import org.primefaces.event.CellEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorRegistroActividadesPOA implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private String eje = "", tipo = "",claseP2="",claseP3="",claseP4="",claseP5="",clasePC="";
    @Getter    @Setter    private Integer numeroP = 1, numeroS = 1, numeroPEliminado = 1, numeroSEliminado = 1, cuadroMando = 0, ejeClave, totalNP = 0, totalActividadPrincipal;
    @Getter    @Setter    private Double totalRecursoActividad = 0D,totalCaptitulos=0D,totalCaptituloPartida = 0D,totalCaptitulo2000 = 0D,totalCaptitulo3000 = 0D,totalCaptitulo4000 = 0D,totalCaptitulo5000 = 0D,totalCaptituloCPDD = 0D;
    @Getter    @Setter    private Double pretecho2000=0D,pretecho3000=0D,pretecho4000=0D,pretecho5000=0D,pretechoCPDD=0D,totalPretecho=0D;
    @Getter    @Setter    private Integer mes1 = 0, mes2 = 0, mes3 = 0, mes4 = 0, mes5 = 0, mes6 = 0, mes7 = 0, mes8 = 0, mes9 = 0, mes10 = 0, mes11 = 0, mes12 = 0;
    @Getter    @Setter    private Short unidadDMedida = 0;

    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>(), actividadesPoasPorParametros = new ArrayList<>(), actividadesPoaPrincipales = new ArrayList<>(), actividadesPoaSubActividades = new ArrayList<>(),actividadesPoaTotalesCapitulos = new ArrayList<>();
    @Getter    @Setter    private List<CuadroMandoIntegral> cuadroMandoIntegrals = new ArrayList<>();
    @Getter    @Setter    private List<UnidadMedidas> unidadMedidases = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> lineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividads = new ArrayList<>(), recursosActividadsPorActividad = new ArrayList<>();
    @Getter    @Setter    private List<ProductosAreas> productosAreases = new ArrayList<>();
    @Getter    @Setter    private List<Productos> productoses = new ArrayList<>();
    @Getter    @Setter    private List<Partidas> partidases = new ArrayList<>(),partidasesSumatorias = new ArrayList<>();

    @Getter    @Setter    private RecursosActividad recursosActividad = new RecursosActividad(), recursosActividadEditada = new RecursosActividad();
    @Getter    @Setter    private Productos productoSeleccionado = new Productos();
    @Getter    @Setter    private Productos productos;
    @Getter    @Setter    private Partidas partidas;
    @Getter    @Setter    private CuadroMandoIntegral cuadroMandoIntegral;
    @Getter    @Setter    private ActividadesPoa actividadesPoa, actividadesPoaPrincipal, actividadesPoaRecurso = new ActividadesPoa(), actividadesPoaEditando = new ActividadesPoa();
    @Getter    @Setter    private Estrategias estrategias;
    @Getter    @Setter    private LineasAccion lineasAccion;

    @Getter    @Setter    private List<capitulosLista> capitulo2000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo3000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo4000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capitulo5000 = new ArrayList<>();
    @Getter    @Setter    private List<capitulosLista> capituloCPDD = new ArrayList<>();

    @Getter    @Setter    private List<listaEjes> ejeses = new ArrayList<>();
    @Getter    @Setter    private listaEjes ejes;

    @EJB
    EjbPoaSelectec poaSelectec;
    @Inject
    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorHabilidadesIIL Inicio: " + System.currentTimeMillis());
        
        actividadesPoaEditando = new ActividadesPoa();

        actividadesPoa = new ActividadesPoa();
        ejeses.add(new listaEjes(1, "Consolidar el Capital Humano", "Fortalecer el capital humano mediante su formación, capacitación y actualización profesional."));
        ejeses.add(new listaEjes(2, "Fortalecer el Proceso Administrativo", "Fortalecer la gestión institucional a través de una administración efectiva y transparente."));
        ejeses.add(new listaEjes(3, "Garantizar la Calidad Académica", "Promover y contribuir a la mejora y al aseguramiento de una educación de calidad."));
        ejeses.add(new listaEjes(4, "Consolidar la Vinculación con un Enfoque Global", "Consolidar la vinculación con un enfoque global."));
        consultarListas();
        
        generaListaEjes();
        
        System.out.println(" ControladorHabilidadesIIL Fin: " + System.currentTimeMillis());
    }

    // ---------------------------------------------------------------- Listas -------------------------------------------------------------
    public void consultarListas() {
        cuadroMandoIntegrals = poaSelectec.mostrarCuadroMandoIntegrals();
        unidadMedidases = poaSelectec.mostrarUnidadMedidases();
        recursosActividads = poaSelectec.mostrarRecursosActividads();
        productosAreases = poaSelectec.mostrarProductosAreases(Short.valueOf("6"), Short.valueOf("17"));
    }

    public void consultarActividadesPorParametros() {
        actividadesPoas.clear();
        actividadesPoaPrincipales.clear();
        actividadesPoaSubActividades.clear();
        actividadesPoasPorParametros.clear();

        actividadesPoas = poaSelectec.mostrarActividadesPoas();

        actividadesPoas.forEach((t) -> {
            if (t.getArea() == Short.parseShort(String.valueOf(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()))) {
                actividadesPoaTotalesCapitulos.add(t);
            }
        });

        actividadesPoaTotalesCapitulos.forEach((t) -> {
            if (Objects.equals(t.getCuadroMandoInt().getCuadroMandoInt(), cuadroMandoIntegral.getCuadroMandoInt())) {
                if(!actividadesPoasPorParametros.contains(t)){
                actividadesPoasPorParametros.add(t);                    
                }
            }
        });

        actividadesPoasPorParametros.forEach((t) -> {
            if (t.getNumeroS() == 0) {
                actividadesPoaPrincipales.add(t);
            } else {
                actividadesPoaSubActividades.add(t);
            }
        });
         Collections.sort(actividadesPoasPorParametros, (x, y) -> Short.compare(x.getNumeroP(), y.getNumeroP()));

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
        obteneroTotalesFinales();
    }

    // ------------------------------------------------------------- Actividades ------------------------------------------------------------
    public void onCellEdit(CellEditEvent event) {
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        totalActividadPrincipal = 0;
        ActividadesPoa modificada = actividadesPoasPorParametros.get(event.getRowIndex());
        if (modificada.getNumeroS() != 0) {
            actividadesPoaPrincipales.forEach((t) -> {
                if (t.getNumeroP() == modificada.getNumeroP()) {
                    actividadesPoaSubActividades.forEach((s) -> {
                        if (s.getNumeroP() == modificada.getNumeroP()) {
                            mes1 = mes1 + s.getNPEnero();                            mes2 = mes2 + s.getNPFebrero();
                            mes3 = mes3 + s.getNPMarzo();                            mes4 = mes4 + s.getNPAbril();
                            mes5 = mes5 + s.getNPMayo();                            mes6 = mes6 + s.getNPJunio();
                            mes7 = mes7 + s.getNPJulio();                            mes8 = mes8 + s.getNPAgosto();
                            mes9 = mes9 + s.getNPSeptiembre();                            mes10 = mes10 + s.getNPOctubre();
                            mes11 = mes11 + s.getNPNoviembre();                            mes12 = mes12 + s.getNPDiciembre();
                        }
                    });
                    if (t.getNumeroS() == 0) {
                        totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                        t.setNPEnero(Short.parseShort(mes1.toString()));
                        t.setNPFebrero(Short.parseShort(mes2.toString()));
                        t.setNPMarzo(Short.parseShort(mes3.toString()));
                        t.setNPAbril(Short.parseShort(mes4.toString()));
                        t.setNPMayo(Short.parseShort(mes5.toString()));
                        t.setNPJunio(Short.parseShort(mes6.toString()));
                        t.setNPJulio(Short.parseShort(mes7.toString()));
                        t.setNPAgosto(Short.parseShort(mes8.toString()));
                        t.setNPSeptiembre(Short.parseShort(mes9.toString()));
                        t.setNPOctubre(Short.parseShort(mes10.toString()));
                        t.setNPNoviembre(Short.parseShort(mes11.toString()));
                        t.setNPDiciembre(Short.parseShort(mes12.toString()));
                        t.setTotal(Short.parseShort(totalActividadPrincipal.toString()));
                        poaSelectec.actualizaActividadesPoa(t);
                    }
                }
            });
        }
        mes1 = 0 + modificada.getNPEnero();
        mes2 = 0 + modificada.getNPFebrero();
        mes3 = 0 + modificada.getNPMarzo();
        mes4 = 0 + modificada.getNPAbril();
        mes5 = 0 + modificada.getNPMayo();
        mes6 = 0 + modificada.getNPJunio();
        mes7 = 0 + modificada.getNPJulio();
        mes8 = 0 + modificada.getNPAgosto();
        mes9 = 0 + modificada.getNPSeptiembre();
        mes10 = 0 + modificada.getNPOctubre();
        mes11 = 0 + modificada.getNPNoviembre();
        mes12 = 0 + modificada.getNPDiciembre();
        totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
        modificada.setTotal(Short.parseShort(totalActividadPrincipal.toString()));
        poaSelectec.actualizaActividadesPoa(modificada);
        consultarActividadesPorParametros();
    }

    public void anadirNuavActividad() {
        mes1 = 0;
        mes2 = 0;
        mes3 = 0;
        mes4 = 0;
        mes5 = 0;
        mes6 = 0;
        mes7 = 0;
        mes8 = 0;
        mes9 = 0;
        mes10 = 0;
        mes11 = 0;
        mes12 = 0;
        numeroS = 1;
        totalActividadPrincipal = 0;
        if ("Actividad".equals(tipo)) {
            numeroP = 0;
            numeroP = actividadesPoaPrincipales.size() + 1;
            actividadesPoa.setNumeroP(Short.parseShort(numeroP.toString()));
        } else {
            actividadesPoaPrincipal = new ActividadesPoa();
            actividadesPoaPrincipales.forEach((t) -> {
                if (t.getNumeroP() == numeroP) {
                    actividadesPoaPrincipal = t;
                }
            });
            actividadesPoaSubActividades.forEach((t) -> {
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
        actividadesPoa.setArea(Short.parseShort(String.valueOf(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())));

        actividadesPoa = poaSelectec.agregarActividadesPoa(actividadesPoa);

        consultarActividadesPorParametros();
        /// modificar parate para lactuvlizacin de valos de las actividades principales
        if ("Subactividad".equals(tipo)) {
            actividadesPoaPrincipal.setBandera("x");
            actividadesPoaSubActividades.forEach((t) -> {
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
    }

    public void actualizarNuavActividad() {
        actividadesPoa = poaSelectec.actualizaActividadesPoa(actividadesPoaEditando);
        consultarActividadesPorParametros();
    }

    public void eliminarActividad(ActividadesPoa pOa) {
        mes1 = 0;
        mes2 = 0;
        mes3 = 0;
        mes4 = 0;
        mes5 = 0;
        mes6 = 0;
        mes7 = 0;
        mes8 = 0;
        mes9 = 0;
        mes10 = 0;
        mes11 = 0;
        mes12 = 0;
        totalActividadPrincipal = 0;
        numeroPEliminado = Integer.parseInt(String.valueOf(pOa.getNumeroP()));
        numeroSEliminado = Integer.parseInt(String.valueOf(pOa.getNumeroS()));
//        ActividadesPoa pOaEliminada = pOa;
        poaSelectec.eliminarActividadesPoa(pOa);

        consultarActividadesPorParametros();

        if (numeroSEliminado == 0) {
            actividadesPoaSubActividades.forEach((t) -> {
                if (t.getNumeroP() == pOa.getNumeroP()) {
                    poaSelectec.eliminarActividadesPoa(t);
                }
            });

            actividadesPoaPrincipales.forEach((t) -> {
                actividadesPoaSubActividades.forEach((s) -> {
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
            actividadesPoaSubActividades.forEach((s) -> {
                if (numeroPEliminado == s.getNumeroP()) {
                    s.setNumeroS(Short.valueOf(numeroS.toString()));
                    poaSelectec.actualizaActividadesPoa(s);
                    numeroS = numeroS + 1;
                }
            });

            actividadesPoaPrincipales.forEach((t) -> {
                if (t.getNumeroP() == numeroPEliminado) {
                    actividadesPoaSubActividades.forEach((s) -> {
                        if (s.getNumeroP() == t.getNumeroP() && s.getNumeroS() != 0) {
                            mes1 = mes1 + s.getNPEnero();
                            mes2 = mes2 + s.getNPFebrero();
                            mes3 = mes3 + s.getNPMarzo();
                            mes4 = mes4 + s.getNPAbril();
                            mes5 = mes5 + s.getNPMayo();
                            mes6 = mes6 + s.getNPJunio();
                            mes7 = mes7 + s.getNPJulio();
                            mes8 = mes8 + s.getNPAgosto();
                            mes9 = mes9 + s.getNPSeptiembre();
                            mes10 = mes10 + s.getNPOctubre();
                            mes11 = mes11 + s.getNPNoviembre();
                            mes12 = mes12 + s.getNPDiciembre();
                        }
                        totalActividadPrincipal = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;

                        t.setNPEnero(Short.parseShort(mes1.toString()));
                        t.setNPFebrero(Short.parseShort(mes2.toString()));
                        t.setNPMarzo(Short.parseShort(mes3.toString()));
                        t.setNPAbril(Short.parseShort(mes4.toString()));
                        t.setNPMayo(Short.parseShort(mes5.toString()));
                        t.setNPJunio(Short.parseShort(mes6.toString()));
                        t.setNPJulio(Short.parseShort(mes7.toString()));
                        t.setNPAgosto(Short.parseShort(mes8.toString()));
                        t.setNPSeptiembre(Short.parseShort(mes9.toString()));
                        t.setNPOctubre(Short.parseShort(mes10.toString()));
                        t.setNPNoviembre(Short.parseShort(mes11.toString()));
                        t.setNPDiciembre(Short.parseShort(mes12.toString()));
                        t.setTotal(Short.parseShort(totalActividadPrincipal.toString()));

                        poaSelectec.actualizaActividadesPoa(t);
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

    // --------------------------------------------------------------- Recurso --------------------------------------------------------------
    public void eliminarRecursoActividad(RecursosActividad recursosActividad) {
        poaSelectec.eliminarRecursosActividad(recursosActividad);
        consultarListas();
        consultarActividadesPorParametros();
        asignaListaRecursoPorActividad(actividadesPoaEditando);
    }

    public void onCellEditProductos(CellEditEvent event) {
        RecursosActividad modificada = actividadesPoaEditando.getRecursosActividadList().get(event.getRowIndex());
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
        recursosActividad=new RecursosActividad();
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

    // -------------------------------------------------------------- Parametros ------------------------------------------------------------
    public void generaListaEjes() {
        ejeses.clear();
        cuadroMandoIntegrals.clear();
        actividadesPoas.clear();
        actividadesPoaTotalesCapitulos.clear();
        actividadesPoas = poaSelectec.mostrarActividadesPoas();
        actividadesPoas.forEach((t) -> {
            if (t.getArea() == Short.parseShort(String.valueOf(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()))) {
                actividadesPoaTotalesCapitulos.add(t);
            }

        });
        actividadesPoaTotalesCapitulos.forEach((t) -> {
            if (!cuadroMandoIntegrals.contains(t.getCuadroMandoInt())) {
                cuadroMandoIntegrals.add(t.getCuadroMandoInt());
            }
        });
        cuadroMandoIntegrals.forEach((t) -> {
            switch (t.getEje()) {
                case 1:
                    ejes = new listaEjes(1, "Consolidar el Capital Humano", "Fortalecer el capital humano mediante su formación, capacitación y actualización profesional.");
                    if (!ejeses.contains(ejes)) {
                        ejeses.add(ejes);
                    }
                    break;
                case 2:
                    ejes = new listaEjes(2, "Fortalecer el Proceso Administrativo", "Fortalecer la gestión institucional a través de una administración efectiva y transparente.");
                    if (!ejeses.contains(ejes)) {
                        ejeses.add(ejes);
                    }
                    break;
                case 3:
                    ejes = new listaEjes(3, "Garantizar la Calidad Académica", "Promover y contribuir a la mejora y al aseguramiento de una educación de calidad.");
                    if (!ejeses.contains(ejes)) {
                        ejeses.add(ejes);
                    }
                    break;
                case 4:
                    ejes = new listaEjes(4, "Consolidar la Vinculación con un Enfoque Global", "Consolidar la vinculación con un enfoque global.");
                    if (!ejeses.contains(ejes)) {
                        ejeses.add(ejes);
                    }
                    break;
            }
        });
    }

    public void asignaEstrategias(ValueChangeEvent event) {
        lineasAccions.clear();
        lineasAccion = new LineasAccion();
        estrategiases.clear();
        estrategias = new Estrategias();
        if (event.getNewValue() != null) {
            ejes = ejeses.get(Integer.parseInt(event.getNewValue().toString()) - 2);
            eje = ejes.getNombre();
            ejeClave = ejes.getNumero();
            cuadroMandoIntegrals.forEach((t) -> {
                if (t.getEjercicioFiscal() == 17) {
                    if (t.getEje() == ejeClave) {
                        if (!estrategiases.contains(t.getEstrategia())) {
                            estrategiases.add(t.getEstrategia());
                        }
                    }
                }
            });
            lineasAccions.clear();
        }
    }

    public void asignaLineasAccion(ValueChangeEvent event2) {
        lineasAccions.clear();
        lineasAccion = new LineasAccion();
        estrategias = new Estrategias();
        if (event2.getNewValue() != null) {
            estrategias = estrategiases.get(Integer.parseInt(event2.getNewValue().toString()) - 2);
            cuadroMandoIntegrals.forEach((t) -> {
                if (t.getEstrategia() == estrategias) {
                    if (!lineasAccions.contains(t.getLineaAccion())) {
                        lineasAccions.add(t.getLineaAccion());
                    }
                }
            });
        }
    }

    public void asignaLineaAccionSeleccionada(ValueChangeEvent event3) {
        lineasAccion = new LineasAccion();
        if (event3.getNewValue() != null) {
            lineasAccion = lineasAccions.get(Integer.parseInt(event3.getNewValue().toString()) - 1);
            asignaCuadroMando();
        }
    }

    public void asignaCuadroMando() {
        cuadroMandoIntegral = new CuadroMandoIntegral();
        cuadroMandoIntegrals.forEach((t) -> {
            if (t.getEje() == ejeClave && t.getEstrategia() == estrategias && t.getLineaAccion() == lineasAccion) {
                cuadroMandoIntegral = t;
            }
        });
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
        numeroP = Integer.parseInt(event.getNewValue().toString());
    }

    // ------------------------------------------------------------- Totales Capitulo -----------------------------------------------------------  
    public void obteneroTotalesCapitulo2000() {
        totalCaptitulo2000 = 0D;
        capitulo2000.clear();
        partidasesSumatorias.clear();
        totalCaptituloPartida = 0D;

        actividadesPoaTotalesCapitulos.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo() == 2) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });
        partidasesSumatorias.forEach((p) -> {
            actividadesPoaTotalesCapitulos.forEach((a) -> {
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
        actividadesPoaTotalesCapitulos.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo() == 3) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            actividadesPoaTotalesCapitulos.forEach((a) -> {
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
        actividadesPoaTotalesCapitulos.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo() == 4) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            actividadesPoaTotalesCapitulos.forEach((a) -> {
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
        actividadesPoaTotalesCapitulos.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo() == 5) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            actividadesPoaTotalesCapitulos.forEach((a) -> {
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
        actividadesPoaTotalesCapitulos.forEach((a) -> {
            a.getRecursosActividadList().forEach((t) -> {
                if (t.getProductoArea().getCapitulo() == 6) {
                    if (!partidasesSumatorias.contains(t.getProductoArea().getPartida())) {
                        partidasesSumatorias.add(t.getProductoArea().getPartida());
                    }
                }
            });
        });

        partidasesSumatorias.forEach((p) -> {
            actividadesPoaTotalesCapitulos.forEach((a) -> {
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
        System.out.println("mx.edu.utxj.pye.sgi.poa.controladores.ControladorRegistroActividadesPOA.imprimirValores()");
    }

    public static class listaEjes {

        @Getter        @Setter        private Integer numero;
        @Getter        @Setter        private String nombre, descripcion;

        public listaEjes(Integer numero, String nombre, String descripcion) {
            this.numero = numero;
            this.nombre = nombre;
            this.descripcion = descripcion;
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
