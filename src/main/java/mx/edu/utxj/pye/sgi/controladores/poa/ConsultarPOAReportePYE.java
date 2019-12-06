package mx.edu.utxj.pye.sgi.controladores.poa;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.pye2.*;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

@Named
@ManagedBean
@SessionScoped
public class ConsultarPOAReportePYE implements Serializable {
    
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private LocalDate fechaActual=LocalDate.now();
    
    @Getter    @Setter    private ListaPersonal listaPersonal=new ListaPersonal();
    @Getter    @Setter    private AreasUniversidad areasUniversidad=new AreasUniversidad();
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads=new ArrayList<>();
    
    @Getter    @Setter    private List<ListaEjes> ejeses=new ArrayList<>();
    @Getter    @Setter    private List<ListaEstrategias> estrategiases=new ArrayList<>();
    @Getter    @Setter    private List<ListaLineasAccion> lineasAccions = new ArrayList<>();
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    @EJB    EjbPersonal ejbPersonal;
    @Inject    ControladorEmpleado controladorEmpleado;

    
    

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;


@PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        ejeses=new ArrayList<>();
        ejeses.clear();
        mostrarAreasTienenPOA();
    }

    public void mostrarAreasTienenPOA() {
        try {            
            areasUniversidad = new AreasUniversidad();
            listaPersonal = new ListaPersonal();
            areasUniversidads = new ArrayList<>();
            areasUniversidads.clear();
            areasUniversidads.add(new AreasUniversidad(Short.parseShort("0"), "Seleccione uno", "Seleccione uno", "1", false));
            ejbAreasLogeo.mostrarAreasUniversidadActivas().forEach((t) -> {
                if (t.getTienePoa()) {
                    areasUniversidads.add(t);
                }
            });
            areasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(controladorEmpleado.getProcesopoa().getArea());
            listaPersonal = ejbPersonal.mostrarListaPersonal(areasUniversidad.getResponsable());
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ConsultarPOAReportePYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        ejeses = new ArrayList<>();
        estrategiases = new ArrayList<>();
        lineasAccions = new ArrayList<>();

        ejeses.clear();
        estrategiases.clear();
        lineasAccions.clear();
        ejercicioFiscal = 0;
        ejercicioFiscal = Short.parseShort(event.getNewValue().toString());
        consultarListasValidacionFinal();
    }

    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            ejeses = new ArrayList<>();
            estrategiases = new ArrayList<>();
            lineasAccions = new ArrayList<>();

            ejeses.clear();
            estrategiases.clear();
            lineasAccions.clear();
            if (event.getNewValue().toString().equals("0")) {
                return;
            }
            areasUniversidad = new AreasUniversidad();
            listaPersonal = new ListaPersonal();
            areasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(Short.parseShort(event.getNewValue().toString()));
            listaPersonal = ejbPersonal.mostrarListaPersonal(areasUniversidad.getResponsable());
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ConsultarPOAReportePYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarListasValidacionFinal() {
        ejeses = new ArrayList<>();
        estrategiases = new ArrayList<>();
        lineasAccions = new ArrayList<>();

        ejeses.clear();
        estrategiases.clear();
        lineasAccions.clear();

        List<EjesRegistro> ejesRegistros = new ArrayList<>();
        ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(areasUniversidad.getArea(), ejercicioFiscal);
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((ej) -> {
                List<Estrategias> listEstrategias = new ArrayList<>();
                listEstrategias = ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, areasUniversidad.getArea());
                if (!listEstrategias.isEmpty()) {
                    estrategiases = new ArrayList<>();
                    estrategiases.clear();
                    listEstrategias.forEach((es) -> {
                        List<LineasAccion> listLineasAccions = new ArrayList<>();
                        listLineasAccions = ejbCatalogosPoa.mostrarLineasAccionRegistroParametros(areasUniversidad.getArea(), ejercicioFiscal, ej, es);
                        if (!listLineasAccions.isEmpty()) {
                            lineasAccions = new ArrayList<>();
                            lineasAccions.clear();
                            listLineasAccions.forEach((li) -> {
                                CuadroMandoIntegral cmi = ejbCatalogosPoa.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ej, es, li).get(0);
                                List<ActividadesPoa> listActividadesPoas = new ArrayList<>();
                                listActividadesPoas = ejbRegistroActividades.mostrarActividadesPoaCuadroDeMandoRecurso(areasUniversidad.getArea(), ejercicioFiscal, cmi);
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
