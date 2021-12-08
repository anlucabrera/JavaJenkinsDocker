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
import javax.inject.Inject;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPresupuestacion;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Objects;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.dto.poa.DTOreportePoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class AdminPoaValidacionPresupuestacion implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidadsRegistros = new ArrayList<>();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad();
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    @Getter    @Setter    private List<DTOreportePoa.ProgramacionActividades> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private String mss="";
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0; 
    
    @Getter    @Setter    private Boolean ecxiste=false;
    @Getter    @Setter    private String claseP1="",claseP2="",claseP3="",claseP4="",clasePC="",clasePT="";
    @Getter    @Setter    private List<PretechoFinanciero> pretechoFinancieros = new ArrayList<>();
    @Getter    @Setter    private List<ProductosAreas> productosAreases = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> ras = new ArrayList<>();
    @Getter    @Setter    private Double pretecho2000=0D,pretecho3000=0D,pretecho4000=0D,pretecho5000=0D,pretechoCPDD=0D,totalPretecho=0D;
    @Getter    @Setter    private Double totalCaptitulos=0D,totalCaptitulo2000 = 0D,totalCaptitulo3000 = 0D,totalCaptitulo4000 = 0D,totalCaptitulo5000 = 0D,totalCaptituloCPDD = 0D;
    @Getter    @Setter    private Double totalRecursoActividad = 0D;
    
    @Getter    @Setter    private List<DTOreportePoa.CapitulosLista> capitulo2000 = new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.CapitulosLista> capitulo3000 = new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.CapitulosLista> capitulo4000 = new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.CapitulosLista> capitulo5000 = new ArrayList<>();
    @Getter    @Setter    private List<DTOreportePoa.CapitulosLista> capituloCPDD = new ArrayList<>();
    
    
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbPresupuestacion ejbPresupuestacion;
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
            Logger.getLogger(AdminPoaValidacionPresupuestacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarListasValidacionFinal() {
        try {
            ejesEsLaAp = new ArrayList<>();
            ejesEsLaAp.clear();
            ejesEsLaAp = ejbCatalogosPoa.getPresentacionPOA(claveArea, ejercicioFiscal);

            productosAreases = new ArrayList<>();
            productosAreases = ejbPresupuestacion.mostrarProductosAreases(claveArea, ejercicioFiscal);

            obtenerPretechos();
            obteneroTotalesCapitulosDesglosado();
            obteneroTotalesFinales();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaValidacionPresupuestacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarmensajes(String proceso) {
        utilidadesPOA.enviarCorreo(proceso, "Ad", false, mss, areaPOASeleccionada);
    }
    
    public void imprimirValores() {
    }

    public void obtenerPretechos() {
        pretechoFinancieros.clear();
        pretechoFinancieros = ejbPresupuestacion.mostrarPretechoFinancieros(claveArea, ejercicioFiscal);
        pretechoFinancieros.forEach((t) -> {
            switch (t.getCapituloTipo().getCapituloTipo()) {
                case 2:
                    pretecho2000 = t.getMonto();
                    break;
                case 3:
                    pretecho3000 = t.getMonto();
                    break;
                case 4:
                    pretecho4000 = t.getMonto();
                    break;
                case 5:
                    pretecho5000 = t.getMonto();
                    break;
                case 6:
                    pretechoCPDD = t.getMonto();
                    break;
            }
        });
        totalPretecho = pretecho2000 + pretecho3000 + pretecho4000 + pretecho5000 + pretechoCPDD;
    }

    public void obteneroTotalesFinales() {
        totalCaptitulos = totalCaptitulo2000 + totalCaptitulo3000 + totalCaptitulo4000 + totalCaptitulo5000 + totalCaptituloCPDD;
        if (totalCaptitulo2000 < pretecho2000) {            claseP1 = "mayor";        }
        if (totalCaptitulo3000 < pretecho3000) {            claseP2 = "mayor";        }
        if (totalCaptitulo4000 < pretecho4000) {            claseP3 = "mayor";        }
        if (totalCaptitulo5000 < pretecho5000) {            claseP4 = "mayor";        }
        if (totalCaptituloCPDD < pretechoCPDD) {            clasePC = "mayor";        }
        if (totalCaptitulos < totalPretecho) {            clasePT = "mayor";        }
    }
    
    public void obteneroTotalesCapitulosDesglosado() {
        ras = new ArrayList<>(); 
        ras=ejbPresupuestacion.mostrarRecursosActividades(claveArea, ejercicioFiscal);
                       
        totalCaptitulo2000 = 0D;
        totalCaptitulo3000 = 0D;
        totalCaptitulo4000 = 0D;
        totalCaptitulo5000 = 0D;
        totalCaptituloCPDD = 0D;
        
        capitulo2000 = new ArrayList<>();
        capitulo2000.clear();
        capitulo3000 = new ArrayList<>();
        capitulo3000.clear();
        capitulo4000 = new ArrayList<>();
        capitulo4000.clear();
        capitulo5000 = new ArrayList<>();
        capitulo5000.clear();
        capituloCPDD = new ArrayList<>();
        capituloCPDD.clear();
        ecxiste = false;
        
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.AdminPoaValidacionPresupuestacion.obteneroTotalesCapitulosDesglosado()"+ras.size());

        if (!ras.isEmpty()) {
            ras.forEach((t) -> {
                CapitulosTipos ct = new CapitulosTipos();
                ct = t.getProductoArea().getCapitulo();
                switch (ct.getCapituloTipo()) {
                    case 2:
                        totalCaptitulo2000 = totalCaptitulo2000 + t.getTotal();
                        break;
                    case 3:
                        totalCaptitulo3000 = totalCaptitulo3000 + t.getTotal();
                        break;
                    case 4:
                        totalCaptitulo4000 = totalCaptitulo4000 + t.getTotal();
                        break;
                    case 5:
                        totalCaptitulo5000 = totalCaptitulo5000 + t.getTotal();
                        break;
                    case 6:
                        totalCaptituloCPDD = totalCaptituloCPDD + t.getTotal();
                        break;
                }
            });
        }
        capitulo2000 = totalDesglosado(Short.parseShort("2"));
        capitulo3000 = totalDesglosado(Short.parseShort("3"));
        capitulo4000 = totalDesglosado(Short.parseShort("4"));
        capitulo5000 = totalDesglosado(Short.parseShort("5"));
        capituloCPDD = totalDesglosado(Short.parseShort("6"));
    }
    
    public List<DTOreportePoa.CapitulosLista> totalDesglosado(Short numcap) {
        ecxiste = false;
        List<RecursosActividad> actividads = new ArrayList<>();
        List<DTOreportePoa.CapitulosLista> cls = new ArrayList<>();
        actividads = ras.stream().filter(t -> Objects.equals(t.getProductoArea().getCapitulo().getCapituloTipo(), numcap)).collect(Collectors.toList());
        if (!actividads.isEmpty()) {
            actividads.forEach((t) -> {
                if (!cls.isEmpty()) {
                    cls.forEach((c) -> {
                        if (c.getPartidas1().equals(t.getProductoArea().getPartida())) {
                            c.setTotal(c.getTotal() + t.getTotal());
                            ecxiste = true;
                        }
                    });
                    if (!ecxiste) {
                        cls.add(new DTOreportePoa.CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                    }
                    ecxiste = false;
                } else {
                    ecxiste = false;
                    cls.add(new DTOreportePoa.CapitulosLista(t.getProductoArea().getPartida(), t.getTotal()));
                }
            });
        }
        return cls;
    }
    
    public void onCellEditProductos(RowEditEvent event) {
        DTOreportePoa.RecursoActividad modificada = (DTOreportePoa.RecursoActividad) event.getObject();
        RecursosActividad actividad= new RecursosActividad();
        actividad=modificada.getRecursosActividad();
        Integer newProdAre=0;
        newProdAre=actividad.getProductoArea().getProductoArea();
        totalRecursoActividad = actividad.getRPEnero() + actividad.getRPFebero() + actividad.getRPMarzo() + actividad.getRPAbril() + actividad.getRPMayo() + actividad.getRPJunio() + actividad.getRPJulio() + actividad.getRPAgosto() + actividad.getRPSeptiembre() + actividad.getRPOctubre() + actividad.getRPNoviembre() + actividad.getRPDiciembre();
        actividad.setTotal(totalRecursoActividad);
        actividad.setProductoArea(new ProductosAreas());
        actividad.setProductoArea(new ProductosAreas(newProdAre));
        ejbPresupuestacion.actualizaRecursosActividad(actividad);
        consultarListasValidacionFinal();
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("!!Operación cancelada!!");
    }
}

