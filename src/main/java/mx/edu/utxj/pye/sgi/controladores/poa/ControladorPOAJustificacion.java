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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.util.POAUtilidades;
import org.omnifaces.cdi.ViewScoped;

@Named
@ManagedBean
@ViewScoped
public class ControladorPOAJustificacion implements Serializable {
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
    
    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    POAUtilidades pOAUtilidades;

    @PostConstruct
    public void init() {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.init()");
        ejercicioFiscal =  pOAUtilidades.obtenerejercicioFiscal("Justificacion",100);
        alineacionActiva = false; 
        consultarListasInit();
    }

    public void consultarListasInit() {
        listaEstrategias.clear();
        listaEjesRegistros.clear();
        listaActividadesPoas.clear();

        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        listaEjesRegistros.add(new EjesRegistro(0, "Selecciones Uno", "Selecciones Uno", "Selecciones Uno", "Selecciones Uno"));
        poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).forEach((t) -> {
            listaEjesRegistros.add(t);
        });

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        if (!poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).isEmpty()) {
            poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).forEach((ej) -> {
                listaListaEjeEstrategia = new ArrayList<>();
                listaListaEjeEstrategia.clear();
                listaListaEjeEstrategia.add(new listaEjeEstrategia(ej, poaSelectec.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())));
                if (!listaListaEjeEstrategia.isEmpty()) {
                    listaListaEjeEstrategia.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
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

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasAreaEjeyEjercicioFiscal(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ejesRegistro);
        

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();
         
        List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
        listaActividadesPoasFiltradas.clear();
        listaActividadesPoasFiltradas = poaSelectec.getActividadesPoasporEstarategias(estrategias, ejesRegistro, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
        listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(estrategias, listaActividadesPoasFiltradas));

        Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
        ejesEsLaAp.add(new listaEjesEsLaAp(ejesRegistro, listaEstrategiaActividadesesEje));
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
                

    }

    public void asignarParametrosRegistro(ValueChangeEvent event) {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.asignarParametrosRegistro()" + Short.parseShort(event.getNewValue().toString()));
        if (Short.parseShort(event.getNewValue().toString()) != Short.parseShort("0")) {
            switch (event.getComponent().getId()) {
                case "eje":
                    ejesRegistro = new EjesRegistro();
                    estrategias = new Estrategias();
                    ejesRegistro = poaSelectec.mostrarEjeRegistro(Integer.parseInt(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        listaEstrategias.clear();
                        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        poaSelectec.getEstarategiasPorEje(ejesRegistro,ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).forEach((t) -> {
                            listaEstrategias.add(t);
                        });
                    }
                    resetearValores();
                    break;
                case "estrategia":
                    estrategias = new Estrategias();
                    estrategias = poaSelectec.mostrarEstrategia(Short.parseShort(event.getNewValue().toString()));
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
        actividadesPoa = poaSelectec.actualizaActividadesPoa(actividadesPoa);
        if(alineacionActiva=false){
            consultarListasInit();
        }else{
        consultarListas();
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

    public void imprimirValores() {
    }

}
