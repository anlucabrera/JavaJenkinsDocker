package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Actividadesremotas;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasAdministrador implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidadoses = new ArrayList<>();
    @Getter    @Setter    private List<Actividadesremotas> listaActividadesremotases = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> listaAreasUniversidads = new ArrayList<>();
    @Getter    @Setter    private AreasUniversidad au = new AreasUniversidad();    
    @Getter    @Setter    private Integer anioNumero = 0;
    

    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private EjbAreasLogeo areasLogeo;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        anioNumero = LocalDate.now().getYear();
        mostrarareas();
    }
    public void mostrarareas() {
        try {
            listaAreasUniversidads = new ArrayList<>();
            listaAreasUniversidads.clear();
            listaAreasUniversidads = areasLogeo.mostrarAreasUniversidadActivas();
            au=listaAreasUniversidads.get(0);
            mostrarIncidencias();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        anioNumero = 0;
        anioNumero = Integer.parseInt(event.getNewValue().toString());
        mostrarIncidencias();
    }

    public void mostrarIncidencias() {
        try {
            listaIncidencias = new ArrayList<>();
            listaIncapacidads = new ArrayList<>();
            listaCuidadoses = new ArrayList<>();
            listaActividadesremotases = new ArrayList<>();
            listaIncidencias.clear();
            listaIncapacidads.clear();
            listaCuidadoses.clear();
            listaActividadesremotases.clear();
            List<Incidencias> is = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 01, 01)), utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 12, 31)));
            List<Incidencias> l =is.stream().filter(t-> t.getClavePersonal().getAreaOperativa()==au.getArea() || t.getClavePersonal().getAreaSuperior()==au.getArea()).collect(Collectors.toList());
            
            List<Incapacidad> iin = ejbNotificacionesIncidencias.mostrarIncapacidadReporte(utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 01, 01)), utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 12, 31)));
            List<Incapacidad> lin =iin.stream().filter(t-> t.getClavePersonal().getAreaOperativa()==au.getArea() || t.getClavePersonal().getAreaSuperior()==au.getArea()).collect(Collectors.toList());
            
            List<Cuidados> icu = ejbNotificacionesIncidencias.mostrarCuidadosReporte(utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 01, 01)), utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 12, 31)));
            List<Cuidados> lcu =icu.stream().filter(t-> t.getPersonal().getAreaOperativa()==au.getArea() || t.getPersonal().getAreaSuperior()==au.getArea()).collect(Collectors.toList());
            
            List<Actividadesremotas> acr = ejbNotificacionesIncidencias.mostrarActividadesremotasReporte(utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 01, 01)), utilidadesCH.castearLDaD(LocalDate.of(anioNumero, 12, 31)));
            List<Actividadesremotas> lar =acr.stream().filter(t-> t.getClavePersonal().getAreaOperativa()==au.getArea() || t.getClavePersonal().getAreaSuperior()==au.getArea()).collect(Collectors.toList());
            
            if(!l.isEmpty()){
                l.forEach((t) -> {if(!au.getNombre().equals("Rectoría")){if(!au.getResponsable().equals(t.getClavePersonal().getClave())){listaIncidencias.add(t);}}else{listaIncidencias.add(t);}});
            }
            if(!lin.isEmpty()){
                lin.forEach((t) -> {if(!au.getNombre().equals("Rectoría")){if(!au.getResponsable().equals(t.getClavePersonal().getClave())){listaIncapacidads.add(t);}}else{listaIncapacidads.add(t);}});
            }
            if(!lcu.isEmpty()){
                lcu.forEach((t) -> {if(!au.getNombre().equals("Rectoría")){if(!au.getResponsable().equals(t.getPersonal().getClave())){listaCuidadoses.add(t);}}else{listaCuidadoses.add(t);}});
            }
            
            if(!acr.isEmpty()){
                lar.forEach((t) -> {if(!au.getNombre().equals("Rectoría")){if(!au.getResponsable().equals(t.getClavePersonal().getClave())){listaActividadesremotases.add(t);}}else{listaActividadesremotases.add(t);}});
            }
            
            listaIncidencias.stream().forEachOrdered(t-> t.getClavePersonal().getClave());
            listaIncapacidads.stream().forEachOrdered(t-> t.getClavePersonal().getClave());
            listaCuidadoses.stream().forEachOrdered(t-> t.getPersonal().getClave());
            listaActividadesremotases.stream().forEachOrdered(t-> t.getClavePersonal().getClave());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            utilidadesCH.agregaBitacora(controladorEmpleado.getNuevoOBJListaPersonal().getClave(), incidencias.getIncidenciaID().toString(), "Incidencia", "Update Admin");
            ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarIncidencias();
            Ajax.update("frmInciGeneral");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarAP(ValueChangeEvent e) {
        try {
            String id = e.getComponent().getClientId();
            Actividadesremotas ag = listaActividadesremotases.get(Integer.parseInt(id.split("tblActividades:")[1].split(":validar")[0]));
            ag.setValidado((Boolean) e.getNewValue());
            utilidadesCH.agregaBitacora(controladorEmpleado.getNuevoOBJListaPersonal().getClave(), ag.getIdActividadesRemotos().toString(), "Actividades Remotas", "Update Admin");
            ejbNotificacionesIncidencias.actualizarActividadesremotas(ag);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarIncidencias();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void imprimirValores() {
    }
 }
 