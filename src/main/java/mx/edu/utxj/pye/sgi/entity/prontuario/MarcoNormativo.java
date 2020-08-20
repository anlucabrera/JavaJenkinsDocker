/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "marco_normativo", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcoNormativo.findAll", query = "SELECT m FROM MarcoNormativo m")
    , @NamedQuery(name = "MarcoNormativo.findByMarcoNormativo", query = "SELECT m FROM MarcoNormativo m WHERE m.marcoNormativo = :marcoNormativo")
    , @NamedQuery(name = "MarcoNormativo.findByReglamento", query = "SELECT m FROM MarcoNormativo m WHERE m.reglamento = :reglamento")
    , @NamedQuery(name = "MarcoNormativo.findByFechaAprobacion", query = "SELECT m FROM MarcoNormativo m WHERE m.fechaAprobacion = :fechaAprobacion")
    , @NamedQuery(name = "MarcoNormativo.findByLibro", query = "SELECT m FROM MarcoNormativo m WHERE m.libro = :libro")})
public class MarcoNormativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "marco_normativo")
    private Integer marcoNormativo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "reglamento")
    private String reglamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "fecha_aprobacion")
    private String fechaAprobacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "libro")
    private String libro;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MarcoClasificacion clasificacion;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MarcoTipo tipo;

    public MarcoNormativo() {
    }

    public MarcoNormativo(Integer marcoNormativo) {
        this.marcoNormativo = marcoNormativo;
    }

    public MarcoNormativo(Integer marcoNormativo, String reglamento, String fechaAprobacion, String libro) {
        this.marcoNormativo = marcoNormativo;
        this.reglamento = reglamento;
        this.fechaAprobacion = fechaAprobacion;
        this.libro = libro;
    }

    public Integer getMarcoNormativo() {
        return marcoNormativo;
    }

    public void setMarcoNormativo(Integer marcoNormativo) {
        this.marcoNormativo = marcoNormativo;
    }

    public String getReglamento() {
        return reglamento;
    }

    public void setReglamento(String reglamento) {
        this.reglamento = reglamento;
    }

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public MarcoClasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(MarcoClasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public MarcoTipo getTipo() {
        return tipo;
    }

    public void setTipo(MarcoTipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marcoNormativo != null ? marcoNormativo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarcoNormativo)) {
            return false;
        }
        MarcoNormativo other = (MarcoNormativo) object;
        if ((this.marcoNormativo == null && other.marcoNormativo != null) || (this.marcoNormativo != null && !this.marcoNormativo.equals(other.marcoNormativo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MarcoNormativo[ marcoNormativo=" + marcoNormativo + " ]";
    }
    
}
