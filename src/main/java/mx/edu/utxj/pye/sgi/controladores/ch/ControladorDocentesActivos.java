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
import javax.faces.event.ValueChangeEvent;
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
public class ControladorDocentesActivos implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private Integer docenteBusqueda = 0;
            
    @Getter    @Setter    private Personal personal = new Personal();
    @Getter    @Setter    private ListaPersonal listaPersonal = new ListaPersonal();
    @Getter    @Setter    private InformacionAdicionalPersonal informacionAdicionalPersonal = new InformacionAdicionalPersonal();
    
    @Getter    @Setter    private List<ListaPersonal> personalsD = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> habilidadesInformaticases = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> desarrollosTecnologicoses = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> capacitacionespersonals = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> experienciasLaboraleses = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> desarrolloSoftwares = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> formacionAcademicas = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> investigacioneses = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> innovacioneses = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> distincioneses = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> memoriaspubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> articulosps = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> congresoses = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> librosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> idiomases = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> lenguases = new ArrayList<>();
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbDatosUsuarioLogeado;
    
    @Inject    ControladorEmpleado controladorEmpleado;
     
    @PostConstruct
    public void init() {        
        generarListasAreas();        
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListasAreas() {
        try {
            personalsD = new ArrayList<>();

            personalsD.clear();

            ejbPersonal.mostrarListaDeEmpleados().forEach((p) -> {
                if (p.getActividad() == 3) {
                    if (p.getCategoriaOperativa() != 41) {
                        personalsD.add(p);
                    }
                }
            });
            Collections.sort(personalsD, (x, y) -> Short.compare(x.getCategoriaOperativa(), y.getCategoriaOperativa()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDocentesActivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilDocente(ValueChangeEvent event) {
        try {
            docenteBusqueda = Integer.parseInt(event.getNewValue().toString());

            personal = new Personal();
            listaPersonal = new ListaPersonal();
            informacionAdicionalPersonal = new InformacionAdicionalPersonal();

            personal = ejbPersonal.mostrarPersonalLogeado(docenteBusqueda);
            listaPersonal = ejbPersonal.mostrarListaPersonal(docenteBusqueda);
            informacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(docenteBusqueda);

            if(informacionAdicionalPersonal == null){
            informacionAdicionalPersonal = new InformacionAdicionalPersonal();
            informacionAdicionalPersonal.setAutorizacion(false);
                informacionAdicionalPersonal=ejbPersonal.crearNuevoInformacionAdicionalPersonal(informacionAdicionalPersonal);
            }            
            informacionCV();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDocentesActivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void informacionCV() {
        try {

            habilidadesInformaticases = new ArrayList<>();
            desarrollosTecnologicoses = new ArrayList<>();
            capacitacionespersonals = new ArrayList<>();
            experienciasLaboraleses = new ArrayList<>();
            desarrolloSoftwares = new ArrayList<>();
            formacionAcademicas = new ArrayList<>();
            investigacioneses = new ArrayList<>();
            innovacioneses = new ArrayList<>();
            distincioneses = new ArrayList<>();
            memoriaspubs = new ArrayList<>();
            articulosps = new ArrayList<>();
            congresoses = new ArrayList<>();
            librosPubs = new ArrayList<>();
            idiomases = new ArrayList<>();
            lenguases = new ArrayList<>();
    
            habilidadesInformaticases.clear();
            desarrollosTecnologicoses.clear();
            capacitacionespersonals.clear();
            experienciasLaboraleses.clear();
            desarrolloSoftwares.clear();
            formacionAcademicas.clear();
            investigacioneses.clear();
            innovacioneses.clear();
            distincioneses.clear();
            memoriaspubs.clear();
            articulosps.clear();
            congresoses.clear();
            librosPubs.clear();
            idiomases.clear();
            lenguases.clear();
            
            habilidadesInformaticases = ejbHabilidades.mostrarHabilidadesInformaticas(docenteBusqueda);
            desarrollosTecnologicoses = ejbTecnologia.mostrarDesarrollosTecnologicos(docenteBusqueda);
            capacitacionespersonals = ejbEducacion.mostrarCapacitacionespersonal(docenteBusqueda);
            experienciasLaboraleses = ejbEducacion.mostrarExperienciasLaborales(docenteBusqueda);
            desarrolloSoftwares = ejbTecnologia.mostrarDesarrolloSoftware(docenteBusqueda);
            formacionAcademicas = ejbEducacion.mostrarFormacionAcademica(docenteBusqueda);
            investigacioneses = ejbProduccionProfecional.mostrarInvestigacion(docenteBusqueda);
            innovacioneses = ejbTecnologia.mostrarInnovaciones(docenteBusqueda);
            distincioneses = ejbPremios.mostrarDistinciones(docenteBusqueda);
            memoriaspubs = ejbProduccionProfecional.mostrarMemoriaspub(docenteBusqueda);
            articulosps = ejbProduccionProfecional.mostrarArticulosp(docenteBusqueda);
            congresoses = ejbProduccionProfecional.mostrarCongresos(docenteBusqueda);
            librosPubs = ejbProduccionProfecional.mostrarLibrosPub(docenteBusqueda);
            idiomases = ejbHabilidades.mostrarIdiomas(docenteBusqueda);
            lenguases = ejbHabilidades.mostrarLenguas(docenteBusqueda);
           
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDocentesActivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
