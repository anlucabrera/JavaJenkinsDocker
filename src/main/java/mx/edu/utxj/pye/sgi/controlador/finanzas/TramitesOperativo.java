package mx.edu.utxj.pye.sgi.controlador.finanzas;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesDto;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesRolOperativo;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.enums.FiscalizacionEtapa;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.XMLReader;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;

@Named
@ViewScoped
public class TramitesOperativo extends ViewScopedRol {
    @Getter @Setter private TramitesRolOperativo rolOperativo;

    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB Facade f;
    @Inject Caster caster;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso;
    @Getter private XMLReader reader;

    @PostConstruct
    public void init(){
        Short area = logon.getPersonal().getAreaOperativa();
        Filter<PersonalActivo> filtro = new Filter<>();
//        System.out.println("filtro.getParams() = " + filtro.getParams());
        rolOperativo = new TramitesRolOperativo(
                filtro,
                ejbPersonalBean.pack(logon.getPersonal()),
                ejbFiscalizacion.getTramitesAcargo(logon.getPersonal()));
        rolOperativo.setEjes(ejbFiscalizacion.getEjes(Short.parseShort(caster.getEjercicioFiscal()),rolOperativo.getOperativo().getAreaOperativa()));

//        System.out.println("TramitesOperativo.init");
        tieneAcceso = rolOperativo.tieneAcceso(rolOperativo.getOperativo());
//        System.out.println("tieneAcceso = " + tieneAcceso);
    }

    public void initTramitesArea(){
        rolOperativo.setTramites(ejbFiscalizacion.getTramitesArea(logon.getPersonal().getAreaOperativa()));
    }

    public void initTramitesACargo(){
        rolOperativo.setTramites(ejbFiscalizacion.getTramitesAcargo(logon.getPersonal()));
    }

    public void initNuevoTramite(){
        rolOperativo.setTramite(new TramitesDto(rolOperativo.getOperativo(), rolOperativo.getOperativo(), new Tramites()));
        rolOperativo.getTramite().setTramiteTipo(TramiteTipo.COMISION);
        rolOperativo.getTramite().setGastoTipo(GastoTipo.ANTICIPADO);
        rolOperativo.setEjes(ejbFiscalizacion.getEjes(Short.valueOf(caster.getEjercicioFiscal()), ejbFiscalizacion.getAreaConPOA(logon.getPersonal().getAreaOperativa())));
        Ajax.update("eje");
        Faces.setSessionAttribute("ejes", rolOperativo.getEjes());
    }

    public void initTramiteFacturas(TramitesDto tramite){
        rolOperativo.setTramite(tramite);
        //NOTE: Si marca error al intentar leer la lista de facturas, se debe declarar la tabla intermedia en la relación de muchos a muchos como 'finanzas.tramites_facturas'
        rolOperativo.getTramite().setFacturas(ejbFiscalizacion.actualizarFacturasEnTramite(rolOperativo.getTramite().getTramite()));
    }

    public void agregarFactura(){
        if(rolOperativo.getFactura() != null){
            ejbFiscalizacion.guardarFactura(rolOperativo.getTramite().getTramite(), rolOperativo.getFactura(), reader);
            initTramiteFacturas(rolOperativo.getTramite());
        }
    }

    public Double calcularSubtotalAcumulado(Facturas factura){
        return ejbFiscalizacion.calcularSubtotalFacturasPorFecha(rolOperativo.getTramite().getFacturas(), factura, true);
    }

    public void descargarPDF(Facturas factura) throws IOException {
        Faces.sendFile(new File(factura.getPdf()), false);
    }

    public void descargarTramiteEvidencia() throws IOException{
        Faces.sendFile(new File(rolOperativo.getTramite().getTramite().getComisionOficios().getRuta()), false);
    }

    public void descargarTicket(Facturas factura) throws IOException{
        Faces.sendFile(new File(factura.getTicket()), false);
    }

    public void descargarXML(Facturas factura) throws IOException{
        Faces.sendFile(new File(factura.getXml()), true);
    }

    public void eliminarFactura(Facturas factura){
        ejbFiscalizacion.eliminarFactura(rolOperativo.getTramite().getTramite(), factura);
        initTramiteFacturas(rolOperativo.getTramite());
    }

    public void liberarTramite(TramitesDto tramite){
        ResultadoEJB<TramitesDto> res = ejbFiscalizacion.liberarTramitePorComisionado(tramite, logon.getPersonal());

        if(res.getCorrecto()) initTramitesACargo();
        else rolOperativo.getTramite().setLiberado(false);
        Ajax.update("tblSCC");
        mostrarMensajeResultadoEJB(res);
    }

    public void onCellEditFacturas(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        Facturas facturaModificada = rolOperativo.getTramite().getFacturas().get(event.getRowIndex());

        if(newValue != null && !newValue.equals(oldValue) && facturaModificada != null) {
            ejbFiscalizacion.guardarFactura(rolOperativo.getTramite().getTramite(), facturaModificada, null);
            initTramiteFacturas(rolOperativo.getTramite());
        }
    }

    public void onRowEditFacturas(RowEditEvent event) {
        Facturas facturaModificada = (Facturas)event.getObject();
        reader = null; //cancelar el lector de xml de la ultima factura subida y decretar que se está editando

        if(facturaModificada != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Fiscalizacion.onRowEditFacturas() guardar");
            ejbFiscalizacion.guardarFactura(rolOperativo.getTramite().getTramite(), facturaModificada, null);
            initTramiteFacturas(rolOperativo.getTramite());
        }
    }

    public void seguirTramite(TramitesDto tramite) {
        if (tramite == null) {
            Messages.addGlobalWarn("Sucedió un error al intentar seguir el trámite, contacte con el administrador.");
        } else {
            ejbFiscalizacion.seguirTramiteDeArea(tramite.getTramite());
            initTramitesACargo();
            //switchTramites(opcionTramites, t2);
        }
    }

    public void seleccionarFactura(Facturas factura){
        rolOperativo.setFactura(factura);
    }

    public void seleccionarTramite(TramitesDto tramite){
        rolOperativo.setTramite(tramite);
    }

    public void subirFacturaPDF(){
        if(rolOperativo.getFile() != null && rolOperativo.getFactura() != null){
            ejbFiscalizacion.almacenarCFDI(rolOperativo.getFile(), rolOperativo.getFactura());
            initTramiteFacturas(rolOperativo.getTramite());
            Ajax.update(":frmFacturas", ":frmFacturas:tblFacturas");
        }
    }

    public void subirTramiteEvidencia(){
        if(rolOperativo.getFile() != null && rolOperativo.getTramite() != null){
            ResultadoEJB<File> res = ejbFiscalizacion.almacenarTramiteEvidencia(rolOperativo.getFile(), rolOperativo.getTramite().getTramite());
            mostrarMensajeResultadoEJB(res);//Messages.addGlobal(res.getCorrecto()? FacesMessage.SEVERITY_INFO:FacesMessage.SEVERITY_ERROR,res.getMensaje());
        }
    }

    public void subirFacturaTicket(){
        if(rolOperativo.getFile() != null && rolOperativo.getFactura() != null){
            ejbFiscalizacion.almacenarTicket(rolOperativo.getFile(), rolOperativo.getTramite().getTramite(), rolOperativo.getFactura());
            initTramiteFacturas(rolOperativo.getTramite());
            Ajax.update(":frmFacturas", ":frmFacturas:tblFacturas");
        }
    }

    public void subirFacturaXML() throws IOException {
        if(rolOperativo.getFile() != null){
            reader = ejbFiscalizacion.leerFacturaXML(rolOperativo.getFile());
            rolOperativo.setFactura(reader.getFactura());
            Boolean repetida = ejbFiscalizacion.comprobarFacturaRepetida(rolOperativo.getTramite().getTramite(), rolOperativo.getFactura());
            if(repetida){
                addDetailMessage("La factura con serie de certificado <b>" + rolOperativo.getFactura().getTimbreFiscalUUID() + "</b> del emisor <b>" + rolOperativo.getFactura().getRfcEmisor() + "</b> ya ha sido registrada en el presente trámite, por lo que no puede ser registrada nuevamente.");
            }else{
                agregarFactura();
            }

            Map.Entry<Boolean, List<Tramites>> res = ejbFiscalizacion.comprobarFacturaRepetida(rolOperativo.getFactura());
            if(res.getKey()){
                res.getValue().remove(rolOperativo.getTramite().getTramite());
                if(res.getKey() && !res.getValue().isEmpty()){
                    Double disponible = ejbFiscalizacion.calcularMontoDisponible(res.getValue(), rolOperativo.getFactura());
                    if(disponible > 0){
                        rolOperativo.getFactura().setMontoAceptado(disponible);
                        agregarFactura();
                    }else{
                        String folios = res.getValue().stream().map(tr -> String.valueOf(tr.getFolio())).collect(Collectors.joining(", "));
                        addDetailMessage("La factura con serie de certificado <b>" + rolOperativo.getFactura().getTimbreFiscalUUID() + "</b> del emisor <b>" + rolOperativo.getFactura().getRfcEmisor() + "</b> ya ha sido registrada en otro u otros trámites (folios: " + folios + ") y ya se acepttó el monto total, por lo que no puede ser registrada nuevamente.");
                    }
                }
            }
            Ajax.update(":frmFacturas", ":frmFacturas:tblFacturas");
        }
    }
}
