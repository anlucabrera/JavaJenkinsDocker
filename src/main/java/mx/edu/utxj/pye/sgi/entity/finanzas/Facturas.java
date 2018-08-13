/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "facturas", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facturas.findAll", query = "SELECT f FROM Facturas f")
    , @NamedQuery(name = "Facturas.findByFactura", query = "SELECT f FROM Facturas f WHERE f.factura = :factura")
    , @NamedQuery(name = "Facturas.findByPartida", query = "SELECT f FROM Facturas f WHERE f.partida = :partida")
    , @NamedQuery(name = "Facturas.findByRfcEmisor", query = "SELECT f FROM Facturas f WHERE f.rfcEmisor = :rfcEmisor")
    , @NamedQuery(name = "Facturas.findByRazonEmisor", query = "SELECT f FROM Facturas f WHERE f.razonEmisor = :razonEmisor")
    , @NamedQuery(name = "Facturas.findByConcepto", query = "SELECT f FROM Facturas f WHERE f.concepto = :concepto")
    , @NamedQuery(name = "Facturas.findByTimbreFiscalUUID", query = "SELECT f FROM Facturas f WHERE f.timbreFiscalUUID = :timbreFiscalUUID")
    , @NamedQuery(name = "Facturas.findByMontoTotal", query = "SELECT f FROM Facturas f WHERE f.montoTotal = :montoTotal")
    , @NamedQuery(name = "Facturas.findByMontoAceptado", query = "SELECT f FROM Facturas f WHERE f.montoAceptado = :montoAceptado")
    , @NamedQuery(name = "Facturas.findByFechaExpedicion", query = "SELECT f FROM Facturas f WHERE f.fechaExpedicion = :fechaExpedicion")
    , @NamedQuery(name = "Facturas.findByFechaAplicacion", query = "SELECT f FROM Facturas f WHERE f.fechaAplicacion = :fechaAplicacion")
    , @NamedQuery(name = "Facturas.findByFechaCoberturaInicio", query = "SELECT f FROM Facturas f WHERE f.fechaCoberturaInicio = :fechaCoberturaInicio")
    , @NamedQuery(name = "Facturas.findByFechaCoberturaFin", query = "SELECT f FROM Facturas f WHERE f.fechaCoberturaFin = :fechaCoberturaFin")
    , @NamedQuery(name = "Facturas.findByViaticoDiario", query = "SELECT f FROM Facturas f WHERE f.viaticoDiario = :viaticoDiario")
    , @NamedQuery(name = "Facturas.findByXml", query = "SELECT f FROM Facturas f WHERE f.xml = :xml")
    , @NamedQuery(name = "Facturas.findByPdf", query = "SELECT f FROM Facturas f WHERE f.pdf = :pdf")
    , @NamedQuery(name = "Facturas.findByLinkVerificacion", query = "SELECT f FROM Facturas f WHERE f.linkVerificacion = :linkVerificacion")
    , @NamedQuery(name = "Facturas.findByTicket", query = "SELECT f FROM Facturas f WHERE f.ticket = :ticket")
    , @NamedQuery(name = "Facturas.findByComentariosSistema", query = "SELECT f FROM Facturas f WHERE f.comentariosSistema = :comentariosSistema")
    , @NamedQuery(name = "Facturas.findByComentariosPdf", query = "SELECT f FROM Facturas f WHERE f.comentariosPdf = :comentariosPdf")
    , @NamedQuery(name = "Facturas.findByComentariosUsuario", query = "SELECT f FROM Facturas f WHERE f.comentariosUsuario = :comentariosUsuario")
    , @NamedQuery(name = "Facturas.findByComentariosFiscalizacion", query = "SELECT f FROM Facturas f WHERE f.comentariosFiscalizacion = :comentariosFiscalizacion")
    , @NamedQuery(name = "Facturas.findByValidacionSistema", query = "SELECT f FROM Facturas f WHERE f.validacionSistema = :validacionSistema")
    , @NamedQuery(name = "Facturas.findByValidacionPdf", query = "SELECT f FROM Facturas f WHERE f.validacionPdf = :validacionPdf")
    , @NamedQuery(name = "Facturas.findByValidacionFiscalizacion", query = "SELECT f FROM Facturas f WHERE f.validacionFiscalizacion = :validacionFiscalizacion")
    , @NamedQuery(name = "Facturas.findByProrrateada", query = "SELECT f FROM Facturas f WHERE f.prorrateada = :prorrateada")
    , @NamedQuery(name = "Facturas.findByLaborales", query = "SELECT f FROM Facturas f WHERE f.laborales = :laborales")
    , @NamedQuery(name = "Facturas.findBySabados", query = "SELECT f FROM Facturas f WHERE f.sabados = :sabados")
    , @NamedQuery(name = "Facturas.findByDomingos", query = "SELECT f FROM Facturas f WHERE f.domingos = :domingos")})
public class Facturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "factura")
    private Integer factura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "partida")
    private String partida;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "rfc_emisor")
    private String rfcEmisor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "razon_emisor")
    private String razonEmisor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "concepto")
    private String concepto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "timbreFiscalUUID")
    private String timbreFiscalUUID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_total")
    private double montoTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_aceptado")
    private double montoAceptado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_expedicion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpedicion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cobertura_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaCoberturaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cobertura_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaCoberturaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "viatico_diario")
    private boolean viaticoDiario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "xml")
    private String xml;
    @Size(max = 1000)
    @Column(name = "pdf")
    private String pdf;
    @Size(max = 500)
    @Column(name = "link_verificacion")
    private String linkVerificacion;
    @Size(max = 1000)
    @Column(name = "ticket")
    private String ticket;
    @Size(max = 500)
    @Column(name = "comentarios_sistema")
    private String comentariosSistema;
    @Size(max = 500)
    @Column(name = "comentarios_pdf")
    private String comentariosPdf;
    @Size(max = 500)
    @Column(name = "comentarios_usuario")
    private String comentariosUsuario;
    @Size(max = 500)
    @Column(name = "comentarios_fiscalizacion")
    private String comentariosFiscalizacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_sistema")
    private boolean validacionSistema;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_pdf")
    private boolean validacionPdf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_fiscalizacion")
    private boolean validacionFiscalizacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prorrateada")
    private boolean prorrateada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "laborales")
    private short laborales;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sabados")
    private short sabados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "domingos")
    private short domingos;
    @JoinTable(name = "finanzas.tramites_facturas", joinColumns = {
        @JoinColumn(name = "factura", referencedColumnName = "factura")}, inverseJoinColumns = {
        @JoinColumn(name = "tramite", referencedColumnName = "tramite")})
    @ManyToMany
    private List<Tramites> tramitesList;
    @JoinTable(name = "finanzas.facturas_presupuestos", joinColumns = {
        @JoinColumn(name = "factura", referencedColumnName = "factura")}, inverseJoinColumns = {
        @JoinColumn(name = "presupuesto", referencedColumnName = "presupuesto")})
    @ManyToMany
    private List<CatalogoPresupuestos> catalogoPresupuestosList;

    public Facturas() {
    }

    public Facturas(Integer factura) {
        this.factura = factura;
    }

    public Facturas(Integer factura, String partida, String rfcEmisor, String razonEmisor, String concepto, String timbreFiscalUUID, double montoTotal, double montoAceptado, Date fechaExpedicion, Date fechaAplicacion, Date fechaCoberturaInicio, Date fechaCoberturaFin, boolean viaticoDiario, String xml, boolean validacionSistema, boolean validacionPdf, boolean validacionFiscalizacion, boolean prorrateada, short laborales, short sabados, short domingos) {
        this.factura = factura;
        this.partida = partida;
        this.rfcEmisor = rfcEmisor;
        this.razonEmisor = razonEmisor;
        this.concepto = concepto;
        this.timbreFiscalUUID = timbreFiscalUUID;
        this.montoTotal = montoTotal;
        this.montoAceptado = montoAceptado;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaCoberturaInicio = fechaCoberturaInicio;
        this.fechaCoberturaFin = fechaCoberturaFin;
        this.viaticoDiario = viaticoDiario;
        this.xml = xml;
        this.validacionSistema = validacionSistema;
        this.validacionPdf = validacionPdf;
        this.validacionFiscalizacion = validacionFiscalizacion;
        this.prorrateada = prorrateada;
        this.laborales = laborales;
        this.sabados = sabados;
        this.domingos = domingos;
    }

    public Integer getFactura() {
        return factura;
    }

    public void setFactura(Integer factura) {
        this.factura = factura;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getRfcEmisor() {
        return rfcEmisor;
    }

    public void setRfcEmisor(String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }

    public String getRazonEmisor() {
        return razonEmisor;
    }

    public void setRazonEmisor(String razonEmisor) {
        this.razonEmisor = razonEmisor;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getTimbreFiscalUUID() {
        return timbreFiscalUUID;
    }

    public void setTimbreFiscalUUID(String timbreFiscalUUID) {
        this.timbreFiscalUUID = timbreFiscalUUID;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getMontoAceptado() {
        return montoAceptado;
    }

    public void setMontoAceptado(double montoAceptado) {
        this.montoAceptado = montoAceptado;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Date getFechaCoberturaInicio() {
        return fechaCoberturaInicio;
    }

    public void setFechaCoberturaInicio(Date fechaCoberturaInicio) {
        this.fechaCoberturaInicio = fechaCoberturaInicio;
    }

    public Date getFechaCoberturaFin() {
        return fechaCoberturaFin;
    }

    public void setFechaCoberturaFin(Date fechaCoberturaFin) {
        this.fechaCoberturaFin = fechaCoberturaFin;
    }

    public boolean getViaticoDiario() {
        return viaticoDiario;
    }

    public void setViaticoDiario(boolean viaticoDiario) {
        this.viaticoDiario = viaticoDiario;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getLinkVerificacion() {
        return linkVerificacion;
    }

    public void setLinkVerificacion(String linkVerificacion) {
        this.linkVerificacion = linkVerificacion;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getComentariosSistema() {
        return comentariosSistema;
    }

    public void setComentariosSistema(String comentariosSistema) {
        this.comentariosSistema = comentariosSistema;
    }

    public String getComentariosPdf() {
        return comentariosPdf;
    }

    public void setComentariosPdf(String comentariosPdf) {
        this.comentariosPdf = comentariosPdf;
    }

    public String getComentariosUsuario() {
        return comentariosUsuario;
    }

    public void setComentariosUsuario(String comentariosUsuario) {
        this.comentariosUsuario = comentariosUsuario;
    }

    public String getComentariosFiscalizacion() {
        return comentariosFiscalizacion;
    }

    public void setComentariosFiscalizacion(String comentariosFiscalizacion) {
        this.comentariosFiscalizacion = comentariosFiscalizacion;
    }

    public boolean getValidacionSistema() {
        return validacionSistema;
    }

    public void setValidacionSistema(boolean validacionSistema) {
        this.validacionSistema = validacionSistema;
    }

    public boolean getValidacionPdf() {
        return validacionPdf;
    }

    public void setValidacionPdf(boolean validacionPdf) {
        this.validacionPdf = validacionPdf;
    }

    public boolean getValidacionFiscalizacion() {
        return validacionFiscalizacion;
    }

    public void setValidacionFiscalizacion(boolean validacionFiscalizacion) {
        this.validacionFiscalizacion = validacionFiscalizacion;
    }

    public boolean getProrrateada() {
        return prorrateada;
    }

    public void setProrrateada(boolean prorrateada) {
        this.prorrateada = prorrateada;
    }

    public short getLaborales() {
        return laborales;
    }

    public void setLaborales(short laborales) {
        this.laborales = laborales;
    }

    public short getSabados() {
        return sabados;
    }

    public void setSabados(short sabados) {
        this.sabados = sabados;
    }

    public short getDomingos() {
        return domingos;
    }

    public void setDomingos(short domingos) {
        this.domingos = domingos;
    }

    @XmlTransient
    public List<Tramites> getTramitesList() {
        return tramitesList;
    }

    public void setTramitesList(List<Tramites> tramitesList) {
        this.tramitesList = tramitesList;
    }

    @XmlTransient
    public List<CatalogoPresupuestos> getCatalogoPresupuestosList() {
        return catalogoPresupuestosList;
    }

    public void setCatalogoPresupuestosList(List<CatalogoPresupuestos> catalogoPresupuestosList) {
        this.catalogoPresupuestosList = catalogoPresupuestosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (factura != null ? factura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturas)) {
            return false;
        }
        Facturas other = (Facturas) object;
        if ((this.factura == null && other.factura != null) || (this.factura != null && !this.factura.equals(other.factura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.Facturas[ factura=" + factura + " ]";
    }
    
}
