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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "tramites", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tramites.findAll", query = "SELECT t FROM Tramites t")
    , @NamedQuery(name = "Tramites.findByTramite", query = "SELECT t FROM Tramites t WHERE t.tramite = :tramite")
    , @NamedQuery(name = "Tramites.findByAnio", query = "SELECT t FROM Tramites t WHERE t.anio = :anio")
    , @NamedQuery(name = "Tramites.findByFolio", query = "SELECT t FROM Tramites t WHERE t.folio = :folio")
    , @NamedQuery(name = "Tramites.findByClave", query = "SELECT t FROM Tramites t WHERE t.clave = :clave")
    , @NamedQuery(name = "Tramites.findByFechaInicio", query = "SELECT t FROM Tramites t WHERE t.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Tramites.findByFechaLimite", query = "SELECT t FROM Tramites t WHERE t.fechaLimite = :fechaLimite")
    , @NamedQuery(name = "Tramites.findByEstatus", query = "SELECT t FROM Tramites t WHERE t.estatus = :estatus")
    , @NamedQuery(name = "Tramites.findByTipo", query = "SELECT t FROM Tramites t WHERE t.tipo = :tipo")
    , @NamedQuery(name = "Tramites.findByGastoTipo", query = "SELECT t FROM Tramites t WHERE t.gastoTipo = :gastoTipo")})
public class Tramites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tramite")
    private Integer tramite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private short anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "folio")
    private short folio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_limite")
    @Temporal(TemporalType.DATE)
    private Date fechaLimite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "estatus")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "gasto_tipo")
    private String gastoTipo;
    @ManyToMany(mappedBy = "tramitesList")
    private List<Facturas> facturasList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tramites")
    private ComisionOficios comisionOficios;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tramites")
    private AlineacionTramites alineacionTramites;

    public Tramites() {
    }

    public Tramites(Integer tramite) {
        this.tramite = tramite;
    }

    public Tramites(Integer tramite, short anio, short folio, int clave, Date fechaInicio, Date fechaLimite, String estatus, String tipo, String gastoTipo) {
        this.tramite = tramite;
        this.anio = anio;
        this.folio = folio;
        this.clave = clave;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.estatus = estatus;
        this.tipo = tipo;
        this.gastoTipo = gastoTipo;
    }

    public Integer getTramite() {
        return tramite;
    }

    public void setTramite(Integer tramite) {
        this.tramite = tramite;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public short getFolio() {
        return folio;
    }

    public void setFolio(short folio) {
        this.folio = folio;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGastoTipo() {
        return gastoTipo;
    }

    public void setGastoTipo(String gastoTipo) {
        this.gastoTipo = gastoTipo;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    public ComisionOficios getComisionOficios() {
        return comisionOficios;
    }

    public void setComisionOficios(ComisionOficios comisionOficios) {
        this.comisionOficios = comisionOficios;
    }

    public AlineacionTramites getAlineacionTramites() {
        return alineacionTramites;
    }

    public void setAlineacionTramites(AlineacionTramites alineacionTramites) {
        this.alineacionTramites = alineacionTramites;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tramite != null ? tramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tramites)) {
            return false;
        }
        Tramites other = (Tramites) object;
        if ((this.tramite == null && other.tramite != null) || (this.tramite != null && !this.tramite.equals(other.tramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.Tramites[ tramite=" + tramite + " ]";
    }
    
}
