package mx.edu.utxj.pye.sgi.controlador.finanzas;

import com.github.adminfaces.starter.infra.security.LogonMB;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.FiscalizacionEtapa;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.TramiteEstatus;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import mx.edu.utxj.pye.sgi.util.XMLReader;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author UTXJ
 */
@Named(value = "fiscalizacion")
//@ConversationScoped
@ViewScoped
public class Fiscalizacion implements Serializable {
    //variables de lectura/escritura
    @Getter @Setter private String origen = "Universidad Tecnológica de Xicotepec de Juárez",destino = "Coordinación General de Universidades Tecnológicas y Politécnicas",ruta;
    @Getter @Setter private GastoTipo gastoTipo = GastoTipo.ANTICIPADO;
    @Getter @Setter private TramiteTipo tipo = TramiteTipo.COMISION;
//    @Getter @Setter private TramiteEtapa tramiteNuevoEtapa = TramiteEtapa.FORMULARIO;
    @Getter @Setter private TramiteEstatus tramiteEstatus;
    @Getter @Setter private ActividadesPoa actividad;
    @Getter @Setter private EjesRegistro eje;
    @Getter @Setter private Estrategias estrategia;
    @Getter @Setter private Facturas facturaNueva, facturaSeleccionada;
    @Getter @Setter private LineasAccion lineaAccion;
    @Getter @Setter private Part file;
    @Getter @Setter private Tramites tramiteNuevo, tramiteSeleccionado;

    //variables enumerativas de control en solo lectura
    @Getter private final GastoTipo gastoTipoAnticipado = GastoTipo.ANTICIPADO, gastoTipoDevengado = GastoTipo.DEVENGADO;
//    @Getter private final TramiteEtapa tramiteNuevoEtapaFormulario = TramiteEtapa.FORMULARIO, tramiteNuevoEtapaMapa = TramiteEtapa.MAPA;
    @Getter private final TramiteTipo tramiteTipoComision = TramiteTipo.COMISION, tramiteTipoProductoServicio = TramiteTipo.PRODUCTO_SERVICIO;

    //variables intermedias de solo lectura
    @Getter private Double distancia=0d;
    @Getter private FiscalizacionEtapa etapa;
//    @Getter private UsuariosRegistros usuarioPOA;
    @Getter private XMLReader reader;
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<Facturas> facturas;
    @Getter private List<LineasAccion> lineasAccion;
    @Getter private List<Tramites> tramitesACargo, tramitesArea;
    
    //recursos
    @Inject LogonMB logon;
    @Inject Caster caster;
    @EJB EjbFiscalizacion ejb;

    @PostConstruct
    public void init(){
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.init() 1");
        tramitesACargo = ejb.getTramitesAcargo(logon.getPersonal());
        etapa = FiscalizacionEtapa.CONSULTA;
//        usuarioPOA = ejb.getUsuarioSIIP(logon.getPersonal().getAreaOperativa());
    }

    public void initTramitesArea(){
        tramitesArea = ejb.getTramitesArea(logon.getPersonal().getAreaOperativa());
    }

    public void initNuevoTramite(){
        tipo = TramiteTipo.COMISION;
        etapa =FiscalizacionEtapa.OFICIO_COMISION;
        tramiteNuevo = new Tramites();
//        tramiteNuevoEtapa = TramiteEtapa.FORMULARIO;
        tipo = TramiteTipo.COMISION;
        gastoTipo = GastoTipo.ANTICIPADO;
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.initNuevoTramite() caster: " + caster);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.initNuevoTramite() ejercicio: " + caster.getEjercicioFiscal());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.initNuevoTramite() usuario: " + usuarioPOA);
        ejes = ejb.getEjes(Short.valueOf(caster.getEjercicioFiscal()), (short)logon.getPersonal().getAreaOperativa());
        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.initNuevoTramite() ejes: " + ejes);
        Ajax.update("eje");
        Faces.setSessionAttribute("ejes", ejes);
    }
    
    public void initTramiteFacturas(Tramites tramites){
        tramiteSeleccionado = tramites;
        //NOTE: Si marca error al intentar leer la lista de facturas, se debe declarar la tabla intermedia en la relación de muchos a muchos como 'finanzas.tramites_facturas'
        facturas = ejb.actualizarFacturasEnTramite(tramiteSeleccionado);
        etapa = FiscalizacionEtapa.FACTURAS;
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.initTramiteFacturas() etapa: " + etapa);
    }

    public void agregarFactura(){
        if(facturaNueva != null){
            ejb.guardarFactura(tramiteSeleccionado, facturaNueva, reader);
            initTramiteFacturas(tramiteSeleccionado);
        }
    }

    public Double calcularSubtotalAcumulado(Facturas factura){
        Double subtotal = ejb.calcularSubtotalFacturasPorFecha(facturas, factura, true);
        return subtotal;
    }

    public Double calcularSubtotalFacturas(Facturas factura){
        Double subtotal = ejb.calcularSubtotalFacturasPorFecha(facturas, factura, false);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.calcularSubtotalFacturas() subtotal: " + subtotal);
        return subtotal;
    }

    public void descargarPDF(Facturas factura) throws IOException{
        File f = new File(factura.getPdf());
        Faces.sendFile(f, false);
    }

    public void descargarTicket(Facturas factura) throws IOException{
        File f = new File(factura.getTicket());
        Faces.sendFile(f, false);
    }
    
    public void descargarXML(Facturas factura) throws IOException{
        File f = new File(factura.getXml());
        Faces.sendFile(f, true);
    }
    
    public void eliminarFactura(Facturas factura){
        ejb.eliminarFactura(tramiteSeleccionado, factura);
        initTramiteFacturas(tramiteSeleccionado);
    }
    
    public void mostrarListaTramites(){
        etapa = FiscalizacionEtapa.CONSULTA;
        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.mostrarListaTramites() etapa: " + etapa);
    }
    
    public void onCellEditFacturas(CellEditEvent event) {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.onCellEditFacturas()");
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        Facturas facturaModificada = facturas.get(event.getRowIndex());
         
        if(newValue != null && !newValue.equals(oldValue) && facturaModificada != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.onCellEditFacturas() guardar");
            ejb.guardarFactura(tramiteSeleccionado, facturaModificada, null);
            initTramiteFacturas(tramiteSeleccionado);
        }
    }
    
    public void onRowEditFacturas(RowEditEvent event) {
        Facturas facturaModificada = (Facturas)event.getObject();
        reader = null; //cancelar el lector de xml de la ultima factura subida y decretar que se está editando
        
        if(facturaModificada != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.onRowEditFacturas() guardar");
            ejb.guardarFactura(tramiteSeleccionado, facturaModificada, null);
            initTramiteFacturas(tramiteSeleccionado);
        }
    }
    
    public void seleccionarFactura(Facturas factura){
        facturaSeleccionada = factura;
    }
    
    public void subirFacturaPDF(){
        if(file != null && facturaSeleccionada != null){
            ejb.almacenarCFDI(file, facturaSeleccionada);
            initTramiteFacturas(tramiteSeleccionado);
            Ajax.update(":frmFacturas", ":frmFacturas:tblFacturas");
        }
    }
    
    public void subirFacturaTicket(){
        if(file != null && facturaSeleccionada != null){
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.subirFacturaTicket()");
            ejb.almacenarTicket(file, tramiteSeleccionado, facturaSeleccionada);
            initTramiteFacturas(tramiteSeleccionado);
            Ajax.update(":frmFacturas", ":frmFacturas:tblFacturas");
        }
    }
    
    public void subirFacturaXML() throws IOException {
        if(file != null){
            reader = ejb.leerFacturaXML(file);
            facturaNueva = reader.getFactura();
            Boolean repetida = ejb.comprobarFacturaRepetida(tramiteSeleccionado, facturaNueva);
            if(repetida){
                addDetailMessage("La factura con serie de certificado <b>" + facturaNueva.getTimbreFiscalUUID() + "</b> del emisor <b>" + facturaNueva.getRfcEmisor() + "</b> ya ha sido registrada en el presente trámite, por lo que no puede ser registrada nuevamente.");
            }else{
                agregarFactura();
            }
            
            Map.Entry<Boolean, List<Tramites>> res = ejb.comprobarFacturaRepetida(facturaNueva);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.subirFacturaXML() res: " + res);
            if(res.getKey()){
                res.getValue().remove(tramiteSeleccionado);

//                System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.subirFacturaXML(" + res.getKey() + "): " + res.getValue());
                if(res.getKey() && !res.getValue().isEmpty()){                
                    Double disponible = ejb.calcularMontoDisponible(res.getValue(), facturaNueva);
                    if(disponible > 0){
                        facturaNueva.setMontoAceptado(disponible);
                        agregarFactura();
                    }else{
                        String folios = res.getValue().stream().map(tr -> String.valueOf(tr.getFolio())).collect(Collectors.joining(", "));
                        addDetailMessage("La factura con serie de certificado <b>" + facturaNueva.getTimbreFiscalUUID() + "</b> del emisor <b>" + facturaNueva.getRfcEmisor() + "</b> ya ha sido registrada en otro u otros trámites (folios: " + folios + ") y ya se acepttó el monto total, por lo que no puede ser registrada nuevamente.");
                    }
                }
            }
//            Faces.refresh();
            Ajax.update(":frmFacturas", ":frmFacturas:tblFacturas");
        }
    }
}
