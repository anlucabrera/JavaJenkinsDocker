package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;

@Named
@ManagedBean
@ViewScoped
public class ControladorRegistroActividadesPOAPyE implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<ActividadesPoa> actividadesPoasAreas = new ArrayList<>(), actividadesPoasAreasEjes = new ArrayList<>();
    @Getter    @Setter    private ActividadesPoa actividadesPoaEditando = new ActividadesPoa();
    @Getter    @Setter    private List<EjesRegistro> ejesesFiltrado = new ArrayList<>(),ejeses = new ArrayList<>();
    @Getter    @Setter    private EjesRegistro ejes;
    @Getter    @Setter    private List<CuadroMandoIntegral> cuadroMandoIntegrals = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> lineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0;
    @Getter    @Setter    private Integer claveEje=0;
    @Getter    @Setter    private Date fechaActual=new Date();
    @Getter    @Setter    private String areaNombre="";
    
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje2 = new ArrayList<>(), listaEstrategiaActividadesesEje3 = new ArrayList<>(), listaEstrategiaActividadesesEje4 = new ArrayList<>(), listaEstrategiaActividadesesEje5 = new ArrayList<>();

    
    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorHabilidadesIIL Inicio: " + System.currentTimeMillis());
        areaNombre=controladorEmpleado.getNuevoOBJListaPersonal().getAreaOficialNombre();
        ejeses.clear();
        ejesesFiltrado.clear();
        actividadesPoasAreas.clear();
        actividadesPoasAreasEjes.clear();
                
        ejes=new EjesRegistro(0);
                
        claveArea = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        ejercicioFiscal = Short.parseShort("17");
        
        System.out.println(" ControladorHabilidadesIIL Fin: " + System.currentTimeMillis());
    }

    // ---------------------------------------------------------------- Listas -------------------------------------------------------------
    public void generaListaActividadesEje() {
        actividadesPoasAreasEjes.clear();
        listaListaEjeEstrategia.clear();
        listaEstrategiaActividadesesEje.clear();
        
        listaListaEjeEstrategia.add(new listaEjeEstrategia(ejes, poaSelectec.getEstarategiasPorEje(ejes, ejercicioFiscal, claveArea)));    
            
        listaListaEjeEstrategia.forEach((e) -> {
            e.getListaEstrategiases1().forEach((t) -> {
                listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea)));
            });
        });    
    }
    
    public void consultarListas(ValueChangeEvent event) {
        ejesesFiltrado.clear();
        ejesesFiltrado.add(new EjesRegistro(0, "Seleccione uno","Seleccione uno","",""));

        claveArea=Short.parseShort(event.getNewValue().toString());
        actividadesPoasAreas = poaSelectec.mostrarActividadesPoasArea(claveArea);

        if (!actividadesPoasAreas.isEmpty()) {
            actividadesPoasAreas.forEach((t) -> {
                if (!cuadroMandoIntegrals.contains(t.getCuadroMandoInt())) {
                    cuadroMandoIntegrals.add(t.getCuadroMandoInt());
                }
            });
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
        }        
    }    
    
    public void consultarListasValidacionFinal(ValueChangeEvent event) {
        claveArea = 0;
        ejeses.clear();
        ejesesFiltrado.clear();
        cuadroMandoIntegrals.clear();
        listaListaEjeEstrategia.clear();
        listaEstrategiaActividadesesEje2.clear();
        listaEstrategiaActividadesesEje3.clear();
        listaEstrategiaActividadesesEje4.clear();
        listaEstrategiaActividadesesEje5.clear();
        
        claveArea = Short.parseShort(event.getNewValue().toString());
        actividadesPoasAreas = poaSelectec.mostrarActividadesPoasArea(claveArea);
        ejeses=poaSelectec.mostrarEjesRegistros();
        
        actividadesPoasAreas.forEach((t) -> {
            if (!cuadroMandoIntegrals.contains(t.getCuadroMandoInt())) {
                cuadroMandoIntegrals.add(t.getCuadroMandoInt());
            }
        });
        
        cuadroMandoIntegrals.forEach((t) -> {
            ejeses.forEach((e) -> {
                if (t.getEje().equals(e)) {
                    if (!ejesesFiltrado.contains(e)) {
                        ejesesFiltrado.add(e);
                    }
                }
            });
        });       
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOAPyE.consultarListasValidacionFinal()"+ejesesFiltrado.size());
        ejesesFiltrado.forEach((e) -> {
            listaListaEjeEstrategia.add(new listaEjeEstrategia(e, poaSelectec.getEstarategiasPorEje(e, ejercicioFiscal, claveArea)));
        });
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorRegistroActividadesPOAPyE.consultarListasValidacionFinal()"+listaListaEjeEstrategia.size());
        listaListaEjeEstrategia.forEach((e) -> {
            e.getListaEstrategiases1().forEach((t) -> {
                switch (e.getEjess().getEje()) {
                    case 1:
                        listaEstrategiaActividadesesEje2.add(new listaEstrategiaActividades(t, poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea)));
                        break;
                    case 2:
                        listaEstrategiaActividadesesEje3.add(new listaEstrategiaActividades(t, poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea)));
                        break;
                    case 3:
                        listaEstrategiaActividadesesEje4.add(new listaEstrategiaActividades(t, poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea)));
                        break;
                    case 4:
                        listaEstrategiaActividadesesEje5.add(new listaEstrategiaActividades(t, poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea)));
                        break;
                }
            });
        });
        
        ejesesFiltrado.forEach((t) -> {
            switch (t.getEje()) {
                case 1:
                    ejesEsLaAp.add(new listaEjesEsLaAp(t, listaEstrategiaActividadesesEje2));
                    break;
                case 2:
                    ejesEsLaAp.add(new listaEjesEsLaAp(t, listaEstrategiaActividadesesEje3));
                    break;
                case 3:
                    ejesEsLaAp.add(new listaEjesEsLaAp(t, listaEstrategiaActividadesesEje4));
                    break;
                case 4:
                    ejesEsLaAp.add(new listaEjesEsLaAp(t, listaEstrategiaActividadesesEje5));
                    break;
            }
        });        
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
    
    public void validarPlaneacion(ValueChangeEvent e) {
        String id = e.getComponent().getClientId();
        ActividadesPoa pc = actividadesPoasAreasEjes.get(Integer.parseInt(id.split("dtbBajas:")[1].split(":validar")[0]));
        pc.setValidadoPyE((Boolean) e.getNewValue());
        poaSelectec.actualizaActividadesPoa(pc);
    }

    public void validarFinanzas(ValueChangeEvent e) {
        String id = e.getComponent().getClientId();
        ActividadesPoa pc = actividadesPoasAreasEjes.get(Integer.parseInt(id.split("dtbBajas:")[1].split(":validar")[0]));
        pc.setValidadoFinanzas((Boolean) e.getNewValue());
        poaSelectec.actualizaActividadesPoa(pc);
    }

    public void validarPyEFinal() {
        actividadesPoasAreas.forEach((t) -> {
            t.setValidadpPyeFinal(true);
            poaSelectec.actualizaActividadesPoa(t);
        });
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
}

