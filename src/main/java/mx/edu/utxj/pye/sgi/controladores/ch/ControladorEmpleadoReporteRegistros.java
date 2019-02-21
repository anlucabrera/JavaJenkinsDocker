package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorEmpleadoReporteRegistros implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<reporteRegistros> listareporteRegistrosPersonal = new ArrayList<reporteRegistros>();
    @Getter    @Setter    private List<ListaPersonal> nuevaVistaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private reporteRegistros listareporteRegistros;
    @Getter    @Setter    private Integer total;
    @Getter    @Setter    private String tipo, area;

    @Inject    ControladorEmpleado controladorEmpleado;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;

    @PostConstruct
    public void init() {
        generarReporte();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
    public void consultaRegistros() {
        try {
            listaFormacionAcademica.clear();
            listaExperienciasLaborales.clear();
            listaCapacitacionespersonal.clear();
            listaIdiomas.clear();
            listaHabilidadesInformaticas.clear();
            listaLenguas.clear();
            listaDesarrolloSoftwar.clear();
            listaDesarrollosTecnologicos.clear();
            listaInnovaciones.clear();
            listaDistinciones.clear();
            listaLibrosPubs.clear();
            listaArticulosp.clear();
            listaMemoriaspub.clear();
            listaInvestigacion.clear();
            listaCongresos.clear();
            total = 0;
            tipo = "";

            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(nuevoOBJListaPersonal.getClave());
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(nuevoOBJListaPersonal.getClave());
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(nuevoOBJListaPersonal.getClave());

            listaIdiomas = ejbHabilidades.mostrarIdiomas(nuevoOBJListaPersonal.getClave());
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(nuevoOBJListaPersonal.getClave());
            listaLenguas = ejbHabilidades.mostrarLenguas(nuevoOBJListaPersonal.getClave());

            listaDesarrolloSoftwar = ejbTecnologia.mostrarDesarrolloSoftware(nuevoOBJListaPersonal.getClave());
            listaDesarrollosTecnologicos = ejbTecnologia.mostrarDesarrollosTecnologicos(nuevoOBJListaPersonal.getClave());
            listaInnovaciones = ejbTecnologia.mostrarInnovaciones(nuevoOBJListaPersonal.getClave());

            listaDistinciones = ejbPremios.mostrarDistinciones(nuevoOBJListaPersonal.getClave());

            listaLibrosPubs = ejbProduccionProfecional.mostrarLibrosPub(nuevoOBJListaPersonal.getClave());
            listaArticulosp = ejbProduccionProfecional.mostrarArticulosp(nuevoOBJListaPersonal.getClave());
            listaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(nuevoOBJListaPersonal.getClave());
            listaInvestigacion = ejbProduccionProfecional.mostrarInvestigacion(nuevoOBJListaPersonal.getClave());
            listaCongresos = ejbProduccionProfecional.mostrarCongresos(nuevoOBJListaPersonal.getClave());

            total = listaFormacionAcademica.size() + listaExperienciasLaborales.size() + listaCapacitacionespersonal.size() + listaIdiomas.size() + listaHabilidadesInformaticas.size() + listaLenguas.size() + listaDesarrolloSoftwar.size() + listaDesarrollosTecnologicos.size() + listaInnovaciones.size() + listaDistinciones.size() + listaLibrosPubs.size() + listaArticulosp.size() + listaMemoriaspub.size() + listaInvestigacion.size() + listaCongresos.size();
            if (total.equals(0)) {
                tipo = "A";
            }
            if (total > 0 && total < 10) {
                tipo = "B";
            }
            if (total > 9) {
                tipo = "C";
            }
            listareporteRegistros = new reporteRegistros(
                    nuevoOBJListaPersonal.getClave(), nuevoOBJListaPersonal.getNombre(),
                    listaFormacionAcademica.size(),
                    listaExperienciasLaborales.size(),
                    listaCapacitacionespersonal.size(),
                    listaIdiomas.size(),
                    listaDesarrollosTecnologicos.size(),
                    listaDistinciones.size(),
                    listaLibrosPubs.size(),
                    listaArticulosp.size(),
                    listaMemoriaspub.size(),
                    listaHabilidadesInformaticas.size(),
                    listaLenguas.size(),
                    listaInnovaciones.size(),
                    listaDesarrolloSoftwar.size(),
                    listaInvestigacion.size(),
                    listaCongresos.size(),
                    total, tipo);
            tipo = "";
            total = 0;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoReporteRegistros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarReporte() {
        try {
            nuevaVistaListaPersonal.clear();
            nuevoOBJListaPersonal = new ListaPersonal();

            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(controladorEmpleado.getEmpleadoLogeado());
            area = nuevoOBJListaPersonal.getAreaOperativaNombre();
            nuevaVistaListaPersonal = ejbPersonal.mostrarListaPersonalListSubordinados(nuevoOBJListaPersonal);
            nuevoOBJListaPersonal = new ListaPersonal();
            for (int i = 0; i <= nuevaVistaListaPersonal.size() - 1; i++) {
                nuevoOBJListaPersonal = nuevaVistaListaPersonal.get(i);
                consultaRegistros();
                listareporteRegistrosPersonal.add(listareporteRegistros);
            }
            Collections.sort(listareporteRegistrosPersonal, (x, y) -> x.getTt().compareTo(y.getTt()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoReporteRegistros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class reporteRegistros {

        @Getter        @Setter        private String nombre, tipo;
        @Getter        @Setter        private Integer clave, fa, el, ca, id, dt, pr, lp, ap, mp, hi, li, in, ds, iv, co, tt;

        public reporteRegistros(Integer clave, String nombre, Integer fa, Integer el, Integer ca, Integer id, Integer dt, Integer pr, Integer lp, Integer ap, Integer mp, Integer hi, Integer li, Integer in, Integer ds, Integer iv, Integer co, Integer tt, String tipo) {
            this.clave = clave;
            this.nombre = nombre;
            this.fa = fa;
            this.el = el;
            this.ca = ca;
            this.id = id;
            this.dt = dt;
            this.pr = pr;
            this.lp = lp;
            this.ap = ap;
            this.mp = mp;
            this.hi = hi;
            this.li = li;
            this.in = in;
            this.ds = ds;
            this.iv = iv;
            this.co = co;
            this.tt = tt;
            this.tipo = tipo;
        }
    }
}