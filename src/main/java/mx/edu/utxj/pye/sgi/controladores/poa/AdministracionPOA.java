package mx.edu.utxj.pye.sgi.controladores.poa;

import mx.edu.utxj.pye.sgi.controladores.ch.*;
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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosevaluacionpoaex;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class AdministracionPOA implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;   
    
    @Getter    @Setter    private Integer calendario;     
    @Getter    @Setter    private Date fechF=new Date();     
    @Getter    @Setter    private ProcesoDetallePoa poa;   
    @Getter    @Setter    private Short ejerciciosFiscalesBusqueda; 
    @Getter    @Setter    private EjerciciosFiscales fiscales;
    @Getter    @Setter    private EjerciciosFiscales ef; 
    @Getter    @Setter    private Short anioB;

    @Getter    @Setter    private List<ProcesoDetallePoa> detallePoas = new ArrayList<>(); 
    @Getter    @Setter    private List<Calendarioevaluacionpoa> calendarioevaluacionpoas = new ArrayList<>();
    @Getter    @Setter    private List<Calendarioevaluacionpoa> calendarioPoaActivo = new ArrayList<>();
    @Getter    @Setter    private List<Boolean> estatus = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> personals = new ArrayList<>();
    
   
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador administrador;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private EjbCatalogosPoa ecp;

    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;
    @Inject    UtilidadesPOA utilidadesPOA;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
         
        buscarCatalogosGenerales();
        buscarCalendarioPOA();
        buscarProcesosPOA();
    }
    
// -----------------------------------------------------------------------------Catalogos   
    public void buscarCatalogosGenerales() {
        try {
            estatus = new ArrayList<>();
            estatus.clear();
            estatus.add(Boolean.FALSE);
            estatus.add(Boolean.TRUE);
            personals = new ArrayList<>();
            personals.clear();
            personals = ejbPersonal.mostrarListaDeEmpleados();
            List<EjerciciosFiscales> efs = ecp.mostrarEjercicioFiscalesesTotales();
            ef = efs.get(efs.size() - 1);
            ejerciciosFiscalesBusqueda = ef.getEjercicioFiscal();
            anioB = ef.getAnio();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.AdministracionPOA.buscarCatalogosGenerales()"+ejerciciosFiscalesBusqueda);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    public void buscarCalendarioPOA() {
        try {
            calendarioevaluacionpoas = new ArrayList<>();
            calendarioevaluacionpoas.clear();
            List<Calendarioevaluacionpoa> cs = ejbUtilidadesCH.mostrarCalendarioevaluacionpoas();
            if (!cs.isEmpty()) {
                calendarioevaluacionpoas = cs.stream().filter(t -> t.getEjercicioFiscal() == ejerciciosFiscalesBusqueda).collect(Collectors.toList());
                calendarioPoaActivo = new ArrayList<>();
                calendarioPoaActivo.clear();
                System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.AdministracionPOA.buscarCalendarioPOA()"+calendarioevaluacionpoas.size());
                if (!calendarioevaluacionpoas.isEmpty()) {
                    calendarioevaluacionpoas.forEach((t) -> {
                        if (new Date().before(t.getFechaFin()) && new Date().after(t.getFechaInicio())) {
                            calendarioPoaActivo.add(t);
                        }
                    });
                }else{
                    calendarioevaluacionpoas = new ArrayList<>();
                    calendarioevaluacionpoas.clear();
                }
            }           
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscarProcesosPOA() {
        try {
            detallePoas = new ArrayList<>();
            detallePoas.clear();
            
            List<Procesopoa> p = new ArrayList<>();
            p.clear();
            p = ejbUtilidadesCH.mostrarProcesopoa();
            p.forEach((t) -> {
                try {
                    ListaPersonal p1 = new ListaPersonal();
                    AreasUniversidad au = new AreasUniversidad();
                    if(t.getResponsable()!=0){
                        p1 = ejbPersonal.mostrarListaPersonal(t.getResponsable());
                    }                    
                    au = areasLogeo.mostrarAreasUniversidad(t.getArea()); 
                    detallePoas.add(new ProcesoDetallePoa(au, p1, t, t.getPermisosevaluacionpoaexList(),t.getRegistroAFinalizado(),t.getAsiganacionRFinalizado(),t.getRegistroJustificacionFinalizado(),t.getValidacionRegistroA(),t.getValidacionRFFinalizado(),t.getValidacionJustificacion()));                    
                } catch (Throwable ex) {
                    Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                    Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            buscarCalendarioPOA();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscarPresupuestos() {
        try {
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
// -----------------------------------------------------------------------------Creacion     
    public void agregarPresupuesto() {
        try {
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarCalendario() {
        try {
            List<EjerciciosFiscales> efs = ecp.mostrarEjercicioFiscalesesTotales();
            EjerciciosFiscales ef = efs.get(efs.size() - 2);
            if (ef.getAnio() == LocalDate.now().getYear()) {
                Integer anio = ef.getAnio() + 1;
                fiscales = new EjerciciosFiscales();
                fiscales.setEjercicioFiscal(ejerciciosFiscalesBusqueda);
                fiscales.setAnio(Short.parseShort(anio.toString()));
//                fiscales = administrador.crearEjerciciosFiscales(fiscales);
            }
            
            List<Calendarioevaluacionpoa> cs = ejbUtilidadesCH.mostrarCalendarioevaluacionpoas();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.AdministracionPOA.agregarCalendario()"+cs);
            if (!cs.isEmpty()) {
                calendarioevaluacionpoas= new ArrayList<>();
                calendarioevaluacionpoas = cs.stream().filter(t -> t.getEjercicioFiscal() == ef.getEjercicioFiscal()).collect(Collectors.toList());
                System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.AdministracionPOA.agregarCalendario()"+calendarioevaluacionpoas.size());
                if (!calendarioevaluacionpoas.isEmpty()) {
                    calendarioevaluacionpoas.forEach((t) -> {
                        Calendarioevaluacionpoa c = new Calendarioevaluacionpoa();
                        c.setFechaInicio(t.getFechaInicio());
                        c.getFechaInicio().setYear(t.getFechaInicio().getYear()+1);
                        c.setFechaFin(t.getFechaFin());
                        c.getFechaFin().setYear(t.getFechaFin().getYear()+1);
                        c.setMesEvaluacion(t.getMesEvaluacion());
                        c.setEstapa(t.getEstapa());
                        c.setJustificacion(t.getJustificacion());
                        c.setEjercicioFiscal(ejerciciosFiscalesBusqueda);
                        administrador.agregarCalendarioPoa(c);
                    });
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarPermiso() {
        try {
            Permisosevaluacionpoaex p= new Permisosevaluacionpoaex();
            p.setFechaApertura(new Date());
            p.setFechaCierre(fechF);
            p.setEvaluacionPOA(new Calendarioevaluacionpoa());
            p.setProcesoPOA(new Procesopoa());
            p.setEvaluacionPOA(new Calendarioevaluacionpoa(calendario));
            p.setProcesoPOA(poa.getProcesopoa());
            administrador.crearPermisosevaluacionpoaex(p);
            buscarProcesosPOA();
            Ajax.update("frmPorceso");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// -----------------------------------------------------------------------------Edicion    
    public void actualizarCalendarioPOA(RowEditEvent event) {
        try {
            Calendarioevaluacionpoa e = (Calendarioevaluacionpoa) event.getObject();
            administrador.actualizarCalendarioPoa(e);
            Messages.addGlobalInfo("¡Operación exitosa!");
            buscarCalendarioPOA();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public void actualizarProcesosPOA(RowEditEvent event) {
        try {
            ProcesoDetallePoa ef = (ProcesoDetallePoa) event.getObject();
            Procesopoa e = ef.getProcesopoa();
            e.setResponsable(ef.getPersonal().getClave());
            e.setRegistroAFinalizado(ef.getRact());
            e.setAsiganacionRFinalizado(ef.getRrec());
            e.setRegistroJustificacionFinalizado(ef.getRjus());
            e.setValidacionRegistroA(ef.getVact());
            e.setValidacionRFFinalizado(ef.getVrec());
            e.setValidacionJustificacion(ef.getVjus());
            administrador.actualizarPorcesoPoa(e);
            Messages.addGlobalInfo("¡Operación exitosa!");
            buscarProcesosPOA();
            Ajax.update("frmPorceso");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarPresupuestos(RowEditEvent event) {
        try {
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void actualizarPermiso(RowEditEvent event) {
        try {
            Permisosevaluacionpoaex p= (Permisosevaluacionpoaex) event.getObject();
            administrador.actualizarPermisosevaluacionpoaex(p);
            buscarProcesosPOA();
            Ajax.update("frmPorceso");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
// -----------------------------------------------------------------------------Eliminacion
  public void eliminarPresupuestos(PresupuestoPOA ppoa) {
        try {
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void eliminarPermiso(Permisosevaluacionpoaex permisosevaluacionpoaex) {
        try {
            administrador.eliminarPermisosevaluacionpoaex(permisosevaluacionpoaex);
            buscarProcesosPOA();
            Ajax.update("frmPorceso");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPOA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// -----------------------------------------------------------------------------Utilidades
    public void imprimirValores() {
    }
    
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        ejerciciosFiscalesBusqueda = Short.parseShort(event.getNewValue().toString());
        ef = ecp.mostrarEjercicioFiscaleses(ejerciciosFiscalesBusqueda);
        ejerciciosFiscalesBusqueda = ef.getEjercicioFiscal();
        anioB = ef.getAnio();
        buscarCalendarioPOA();
        Ajax.update("cataogosPtw");
    }
    
    public Boolean vencida(Date d) {
        Date d1=new Date();
        if(d.before(d1)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    public static class PresupuestoPOA {
        
        @Getter        @Setter        private Short areaID;
        @Getter        @Setter        private String area;
        @Getter        @Setter        private String responsable;
        @Getter        @Setter        private PretechoFinanciero cap2000;
        @Getter        @Setter        private PretechoFinanciero cap3000;        
        @Getter        @Setter        private PretechoFinanciero cap4000;
        @Getter        @Setter        private PretechoFinanciero capdder;

        public PresupuestoPOA(Short areaID, String area, String responsable, PretechoFinanciero cap2000, PretechoFinanciero cap3000, PretechoFinanciero cap4000, PretechoFinanciero capdder) {
            this.areaID = areaID;
            this.area = area;
            this.responsable = responsable;
            this.cap2000 = cap2000;
            this.cap3000 = cap3000;
            this.cap4000 = cap4000;
            this.capdder = capdder;
        }        
    }
    
    public static class ProcesoDetallePoa {
        
        @Getter        @Setter        private AreasUniversidad universidad;
        @Getter        @Setter        private ListaPersonal personal;
        @Getter        @Setter        private Procesopoa procesopoa;        
        @Getter        @Setter        private List<Permisosevaluacionpoaex> permisosevaluacionpoaexs;
        @Getter        @Setter        private Boolean ract;
        @Getter        @Setter        private Boolean rrec;
        @Getter        @Setter        private Boolean rjus;
        @Getter        @Setter        private Boolean vact;
        @Getter        @Setter        private Boolean vrec;
        @Getter        @Setter        private Boolean vjus;

        public ProcesoDetallePoa(AreasUniversidad universidad, ListaPersonal personal, Procesopoa procesopoa, List<Permisosevaluacionpoaex> permisosevaluacionpoaexs, Boolean ract, Boolean rrec, Boolean rjus, Boolean vact, Boolean vrec, Boolean vjus) {
            this.universidad = universidad;
            this.personal = personal;
            this.procesopoa = procesopoa;
            this.permisosevaluacionpoaexs = permisosevaluacionpoaexs;
            this.ract = ract;
            this.rrec = rrec;
            this.rjus = rjus;
            this.vact = vact;
            this.vrec = vrec;
            this.vjus = vjus;
        }       
        
    }
}
