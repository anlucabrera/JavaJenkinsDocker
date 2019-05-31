package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import org.omnifaces.cdi.ViewScoped;

@Named
@ManagedBean
@ViewScoped
public class AreaPoaReporte implements Serializable {
    
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Date fechaActual=new Date();
    
    @Getter    @Setter    private List<ListaEjes> ejeses=new ArrayList<>();
    @Getter    @Setter    private List<ListaEstrategias> estrategiases=new ArrayList<>();
    @Getter    @Setter    private List<ListaLineasAccion> lineasAccions = new ArrayList<>();
    
    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;

    
    @PostConstruct
    public void init() {
        System.out.println("ControladorHabilidadesIIL Inicio: " + System.currentTimeMillis());
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        consultarListasValidacionFinal();
        System.out.println(" ControladorHabilidadesIIL Fin: " + System.currentTimeMillis());
    }

    public void consultarListasValidacionFinal() {
        ejeses = new ArrayList<>();
        estrategiases = new ArrayList<>();
        lineasAccions = new ArrayList<>();
        
        ejeses.clear();
        estrategiases.clear();
        lineasAccions.clear();

        List<EjesRegistro> ejesRegistros = new ArrayList<>();
        ejesRegistros = poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((ej) -> {
                List<Estrategias> listEstrategias = new ArrayList<>();
                listEstrategias = poaSelectec.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if (!listEstrategias.isEmpty()) {
                    estrategiases = new ArrayList<>();
                    estrategiases.clear();
                    listEstrategias.forEach((es) -> {
                        List<LineasAccion> listLineasAccions = new ArrayList<>();
                        listLineasAccions = poaSelectec.mostrarLineasAccionRegistroParametros(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ej, es);
                        if (!listLineasAccions.isEmpty()) {
                            lineasAccions = new ArrayList<>();
                            lineasAccions.clear();
                            listLineasAccions.forEach((li) -> {
                                CuadroMandoIntegral cmi = poaSelectec.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ej, es, li).get(0);
                                List<ActividadesPoa> listActividadesPoas = new ArrayList<>();
                                listActividadesPoas = poaSelectec.mostrarActividadesPoaCuadroDeMandoRecurso(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, cmi);
                                lineasAccions.add(new ListaLineasAccion(li, listActividadesPoas));
                            });
                        }
                        estrategiases.add(new ListaEstrategias(es, lineasAccions));
                    });
                }
                ejeses.add(new ListaEjes(ej, estrategiases));
            });
        }
    }

    public void imprimirValores() {
        System.out.println("mx.edu.utxj.pye.sgi.poa.controladores.imprimirValores()");
    }

    public static class ListaEjes {

        @Getter        @Setter        private EjesRegistro ejesRegistro;
        @Getter        @Setter        private List<ListaEstrategias> estrategiases;

        public ListaEjes(EjesRegistro ejesRegistro, List<ListaEstrategias> estrategiases) {
            this.ejesRegistro = ejesRegistro;
            this.estrategiases = estrategiases;
        }
    }

    public static class ListaEstrategias {

        @Getter        @Setter        private Estrategias estrategias;
        @Getter        @Setter        private List<ListaLineasAccion> lineasAccions;

        public ListaEstrategias(Estrategias estrategias, List<ListaLineasAccion> lineasAccions) {
            this.estrategias = estrategias;
            this.lineasAccions = lineasAccions;
        }
    }

    public static class ListaLineasAccion {

        @Getter        @Setter        private LineasAccion lineasAccion;
        @Getter        @Setter        private List<ActividadesPoa> actividadesPoas;

        public ListaLineasAccion(LineasAccion lineasAccion, List<ActividadesPoa> actividadesPoas) {
            this.lineasAccion = lineasAccion;
            this.actividadesPoas = actividadesPoas;
        }
    }
}

