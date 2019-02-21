package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorEmpleadoLogeado implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;
////////////////////////////////////////////////////////////////////////////////CV
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////Datos personales
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private Personal nuevoOBJListaPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaListaPersonal;

////////////////////////////////////////////////////////////////////////////////Listas complementarias
    @Getter    @Setter    private List<Funciones> listaFuncionesLogeado = new ArrayList<>();
    @Getter    @Setter    private Integer empleadoLogeado;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
        mostrarPerfilLogeado();
    }

    public void mostrarPerfilLogeado() {
        try {
            nuevoOBJListaPersonal = new Personal();
            nuevoOBJInformacionAdicionalPersonal = controladorEmpleado.getNuevoOBJInformacionAdicionalPersonal();
            nuevoOBJListaListaPersonal = controladorEmpleado.getNuevoOBJListaPersonal();
            nuevoOBJListaPersonal = ejbPersonal.mostrarPersonalLogeado(empleadoLogeado);
            informacionCV();
            mostrarFuncionesLogeado();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void informacionCV() {
        try {
            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(empleadoLogeado);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(empleadoLogeado);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(empleadoLogeado);

            listaIdiomas = ejbHabilidades.mostrarIdiomas(empleadoLogeado);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(empleadoLogeado);
            listaLenguas = ejbHabilidades.mostrarLenguas(empleadoLogeado);

            listaDesarrolloSoftwar = ejbTecnologia.mostrarDesarrolloSoftware(empleadoLogeado);
            listaDesarrollosTecnologicos = ejbTecnologia.mostrarDesarrollosTecnologicos(empleadoLogeado);
            listaInnovaciones = ejbTecnologia.mostrarInnovaciones(empleadoLogeado);

            listaDistinciones = ejbPremios.mostrarDistinciones(empleadoLogeado);

            listaLibrosPubs = ejbProduccionProfecional.mostrarLibrosPub(empleadoLogeado);
            listaArticulosp = ejbProduccionProfecional.mostrarArticulosp(empleadoLogeado);
            listaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(empleadoLogeado);
            listaInvestigacion = ejbProduccionProfecional.mostrarInvestigacion(empleadoLogeado);
            listaCongresos = ejbProduccionProfecional.mostrarCongresos(empleadoLogeado);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarFuncionesLogeado() {
        try {
            if (nuevoOBJListaListaPersonal.getCategoriaOperativa() == 30
                    || nuevoOBJListaListaPersonal.getCategoriaOperativa() == 32
                    || nuevoOBJListaListaPersonal.getCategoriaOperativa() == 41
                    || (nuevoOBJListaListaPersonal.getCategoriaOperativa() == 34 && (nuevoOBJListaListaPersonal.getAreaSuperior() >= 23 && nuevoOBJListaListaPersonal.getAreaSuperior() <= 29))) {
                listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
            } else {
                listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaListaPersonal.getAreaOperativa(), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}