/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evidencias_detalle", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvidenciasDetalle.findAll", query = "SELECT e FROM EvidenciasDetalle e")
    , @NamedQuery(name = "EvidenciasDetalle.findByDetalleEvidencia", query = "SELECT e FROM EvidenciasDetalle e WHERE e.detalleEvidencia = :detalleEvidencia")
    , @NamedQuery(name = "EvidenciasDetalle.findByMime", query = "SELECT e FROM EvidenciasDetalle e WHERE e.mime = :mime")
    , @NamedQuery(name = "EvidenciasDetalle.findByTamanioBytes", query = "SELECT e FROM EvidenciasDetalle e WHERE e.tamanioBytes = :tamanioBytes")
    , @NamedQuery(name = "EvidenciasDetalle.findByMes", query = "SELECT e FROM EvidenciasDetalle e WHERE e.mes = :mes")})
public class EvidenciasDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detalle_evidencia")
    private Integer detalleEvidencia;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "mime")
    private String mime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tamanio_bytes")
    private long tamanioBytes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "mes")
    private String mes;
    @JoinColumn(name = "evidencia", referencedColumnName = "evidencia")
    @ManyToOne(optional = false)
    private Evidencias evidencia;

    public EvidenciasDetalle() {
    }

    public EvidenciasDetalle(Integer detalleEvidencia) {
        this.detalleEvidencia = detalleEvidencia;
    }

    public EvidenciasDetalle(Integer detalleEvidencia, String ruta, String mime, long tamanioBytes, String mes) {
        this.detalleEvidencia = detalleEvidencia;
        this.ruta = ruta;
        this.mime = mime;
        this.tamanioBytes = tamanioBytes;
        this.mes = mes;
    }

    public Integer getDetalleEvidencia() {
        return detalleEvidencia;
    }

    public void setDetalleEvidencia(Integer detalleEvidencia) {
        this.detalleEvidencia = detalleEvidencia;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public long getTamanioBytes() {
        return tamanioBytes;
    }

    public void setTamanioBytes(long tamanioBytes) {
        this.tamanioBytes = tamanioBytes;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Evidencias getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Evidencias evidencia) {
        this.evidencia = evidencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleEvidencia != null ? detalleEvidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvidenciasDetalle)) {
            return false;
        }
        EvidenciasDetalle other = (EvidenciasDetalle) object;
        if ((this.detalleEvidencia == null && other.detalleEvidencia != null) || (this.detalleEvidencia != null && !this.detalleEvidencia.equals(other.detalleEvidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle[ detalleEvidencia=" + detalleEvidencia + " ]";
    }
    
}
