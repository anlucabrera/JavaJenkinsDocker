package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorSubordinados;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.sql.Array;
import mx.edu.utxj.pye.sgi.dto.poa.DTOreportePoa;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;

@Named
@ManagedBean
@ViewScoped
public class AdminPoaValidacionProgramacion implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidadsRegistros = new ArrayList<>();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad();
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    @Getter    @Setter    private List<DTOreportePoa.ProgramacionActividades> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.ListaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    @Getter    @Setter    private String mss="";
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0; 
    @Getter    @Setter    private List<DTOreportePoa.EstrategiaLineas> listaEstrategiaActividadesesEje = new ArrayList<>();
    
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    @EJB    EjbUtilidadesCH ejbUtilidadesCH;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA utilidadesPOA;

    

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;


@PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        ejercicioFiscal=controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        buscarAreasQueTienenPOA();

    }
    
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        ejercicioFiscal = Short.parseShort(event.getNewValue().toString());
        consultarListasValidacionFinal();
    }
    
    public void buscarAreasQueTienenPOA() {
        try {
            areasUniversidadsRegistros = new ArrayList<>();
            areasUniversidadsRegistros.clear();
            areasUniversidadsRegistros.add(new AreasUniversidad(Short.parseShort("0"), "Seleccione uno", "Seleccione uno", "1", false));
            areasUniversidadsRegistros.addAll(ejbAreasLogeo.getAreasUniversidadConPoa());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            mss = "";
            claveArea = 0;
            areaPOASeleccionada = new AreasUniversidad();
            procesopoa = new Procesopoa();
            claveArea = Short.parseShort(event.getNewValue().toString());
            areaPOASeleccionada = ejbAreasLogeo.mostrarAreasUniversidad(claveArea);
            procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(claveArea);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaValidacionProgramacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarListasValidacionFinal() {
        listaListaEjeEstrategia=new ArrayList<>();
        listaListaEjeEstrategia.clear();

        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();
        List<EjesRegistro> ers = new ArrayList<>();
        ers=ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal);
        if (!ers.isEmpty()) {
            ers.forEach((ej) -> {
                listaListaEjeEstrategia.add(new DTOreportePoa.ListaEjeEstrategia(ej, ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, claveArea)));
            });
        }
        
        if (!listaListaEjeEstrategia.isEmpty()) {
            ejesEsLaAp = new ArrayList<>();
            listaListaEjeEstrategia.forEach((ej) -> {
                List<DTOreportePoa.EstrategiaLineas> els = new ArrayList<>();
                ej.getEstrategiases().forEach((es) -> {
                    List<DTOreportePoa.ActividadRecurso> ars = new ArrayList<>();
                    List<ActividadesPoa> aps = new ArrayList<>();
                    aps = ejbRegistroActividades.getActividadesPoasEstarategias(es, ej.getEjess(), ejercicioFiscal, claveArea);
                    aps.forEach((ap) -> {
                        ars.add(new DTOreportePoa.ActividadRecurso(ap, ap.getUnidadMedida(), new ArrayList<>()));
                    });
                    els.add(new DTOreportePoa.EstrategiaLineas(es, ars));
                });
                ejesEsLaAp.add(new DTOreportePoa.ProgramacionActividades(ej.getEjess(), els));
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjesRegistro().getEje(), y.getEjesRegistro().getEje()));
    }

    public void enviarmensajes(String proceso) {
        utilidadesPOA.enviarCorreo(proceso, "Ad", false, mss, areaPOASeleccionada);
    }

    public void onRowEditAc(RowEditEvent event) {
        try {
            DTOreportePoa.ActividadRecurso ap = (DTOreportePoa.ActividadRecurso) event.getObject();
            ejbRegistroActividades.actualizaActividadesPoa(ap.getActividadesPoa());
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void actualizarestrategica(ValueChangeEvent e) {
        try {
            String id = e.getComponent().getClientId();
            DTOreportePoa.ProgramacionActividades eje=ejesEsLaAp.get(Integer.parseInt(id.split("dtbBajas:")[1].split(":dtbEstra")[0]));
            DTOreportePoa.EstrategiaLineas estratea=eje.getEstrategiaLineas().get(Integer.parseInt(id.split("dtbEstra:")[1].split(":dtbActividades")[0]));
            DTOreportePoa.ActividadRecurso ar=estratea.getActividadeRecurso().get(Integer.parseInt(id.split("dtbActividades:")[1].split(":validar")[0]));
            ActividadesPoa ap=ar.getActividadesPoa();
            ap.setEstratejica((Boolean) e.getNewValue());
            ejbRegistroActividades.actualizaActividadesPoa(ap);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }

    public void imprimirValores() {
    }
    
}

