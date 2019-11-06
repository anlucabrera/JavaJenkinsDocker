package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import org.omnifaces.util.Faces;

@Named
@ManagedBean
@SessionScoped
public class AreaPoaReporte implements Serializable {
    
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Date fechaActual=new Date();
    
    @Getter    @Setter    private List<ListaEjes> ejeses=new ArrayList<>();
    @Getter    @Setter    private List<ListaEstrategias> estrategiases=new ArrayList<>();
    @Getter    @Setter    private List<ListaLineasAccion> lineasAccions = new ArrayList<>();
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @Inject    ControladorEmpleado controladorEmpleado;

    
    @PostConstruct
    public void init() {
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        consultarListasValidacionFinal();
    }

    public void numeroAnioAsiganado(ValueChangeEvent event) {
        ejercicioFiscal = 0;
        ejercicioFiscal = Short.parseShort(event.getNewValue().toString());
        consultarListasValidacionFinal();
    }
    
    public void consultarListasValidacionFinal() {
        ejeses = new ArrayList<>();
        estrategiases = new ArrayList<>();
        lineasAccions = new ArrayList<>();
        
        ejeses.clear();
        estrategiases.clear();
        lineasAccions.clear();

        List<EjesRegistro> ejesRegistros = new ArrayList<>();
        ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((ej) -> {
                List<Estrategias> listEstrategias = new ArrayList<>();
                listEstrategias = ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea());
                if (!listEstrategias.isEmpty()) {
                    estrategiases = new ArrayList<>();
                    estrategiases.clear();
                    listEstrategias.forEach((es) -> {
                        List<LineasAccion> listLineasAccions = new ArrayList<>();
                        listLineasAccions = ejbCatalogosPoa.mostrarLineasAccionRegistroParametros(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, ej, es);
                        if (!listLineasAccions.isEmpty()) {
                            lineasAccions = new ArrayList<>();
                            lineasAccions.clear();
                            listLineasAccions.forEach((li) -> {
                                CuadroMandoIntegral cmi = ejbCatalogosPoa.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ej, es, li).get(0);
                                List<ActividadesPoa> listActividadesPoas = new ArrayList<>();
                                listActividadesPoas = ejbRegistroActividades.mostrarActividadesPoaCuadroDeMandoRecurso(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, cmi);
                                lineasAccions.add(new ListaLineasAccion(li, listActividadesPoas));
                            });
                        }
                        estrategiases.add(new ListaEstrategias(es, lineasAccions));
                    });
                }
                ejeses.add(new ListaEjes(ej, estrategiases));
            });
        }
        Faces.refresh();
    }

    public void imprimirValores() {
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

