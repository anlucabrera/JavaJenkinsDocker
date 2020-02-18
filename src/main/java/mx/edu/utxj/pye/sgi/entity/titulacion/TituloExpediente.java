/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "titulo_expediente", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TituloExpediente.findAll", query = "SELECT t FROM TituloExpediente t")
    , @NamedQuery(name = "TituloExpediente.findByTitExpediente", query = "SELECT t FROM TituloExpediente t WHERE t.titExpediente = :titExpediente")
    , @NamedQuery(name = "TituloExpediente.findByRuta", query = "SELECT t FROM TituloExpediente t WHERE t.ruta = :ruta")
    , @NamedQuery(name = "TituloExpediente.findByFechaCarga", query = "SELECT t FROM TituloExpediente t WHERE t.fechaCarga = :fechaCarga")
    , @NamedQuery(name = "TituloExpediente.findByFechaEmision", query = "SELECT t FROM TituloExpediente t WHERE t.fechaEmision = :fechaEmision")})
public class TituloExpediente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tit_expediente")
    private Integer titExpediente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_carga")
    @Temporal(TemporalType.DATE)
    private Date fechaCarga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @JoinColumn(name = "documento", referencedColumnName = "documento")
    @ManyToOne(optional = false)
    private Documentos documento;
    @JoinColumn(name = "expediente", referencedColumnName = "expediente")
    @ManyToOne(optional = false)
    private ExpedientesTitulacion expediente;

    public TituloExpediente() {
    }

    public TituloExpediente(Integer titExpediente) {
        this.titExpediente = titExpediente;
    }

    public TituloExpediente(Integer titExpediente, String ruta, Date fechaCarga, Date fechaEmision) {
        this.titExpediente = titExpediente;
        this.ruta = ruta;
        this.fechaCarga = fechaCarga;
        this.fechaEmision = fechaEmision;
    }

    public Integer getTitExpediente() {
        return titExpediente;
    }

    public void setTitExpediente(Integer titExpediente) {
        this.titExpediente = titExpediente;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public ExpedientesTitulacion getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedientesTitulacion expediente) {
        this.expediente = expediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (titExpediente != null ? titExpediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TituloExpediente)) {
            return false;
        }
        TituloExpediente other = (TituloExpediente) object;
        if ((this.titExpediente == null && other.titExpediente != null) || (this.titExpediente != null && !this.titExpediente.equals(other.titExpediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.TituloExpediente[ titExpediente=" + titExpediente + " ]";
    }
    
}
