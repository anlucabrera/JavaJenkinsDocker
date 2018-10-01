package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import org.omnifaces.cdi.ViewScoped;

@Named
@ManagedBean
@ViewScoped
public class ControladorPOAEvaluacion implements Serializable {
    
    @Getter    @Setter
    private Short ejercicioFiscal = 0;
    @Getter    @Setter
    private EjesRegistro ejesRegistro = new EjesRegistro();
    // Listas de DTO's
    @Getter    @Setter
    private List<listaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter
    private List<listaEjeEstrategia> listaListaEjeEstrategia = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();

    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        
    }
    
    public void consultarListas() {

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        listaListaEjeEstrategia.add(new listaEjeEstrategia(ejesRegistro, poaSelectec.getEstarategiasPorEje(ejesRegistro, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())));
        if (!listaListaEjeEstrategia.isEmpty()) {
            listaListaEjeEstrategia.forEach((e) -> {
                e.getListaEstrategiases1().forEach((t) -> {
                    List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                    listaActividadesPoasFiltradas.clear();
                    listaActividadesPoasFiltradas = poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                    Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
                    listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                });
            });
            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
            ejesEsLaAp.add(new listaEjesEsLaAp(ejesRegistro, listaEstrategiaActividadesesEje));
            Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
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
