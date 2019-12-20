/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "tipo_aspirante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAspirante.findAll", query = "SELECT t FROM TipoAspirante t")
    , @NamedQuery(name = "TipoAspirante.findByIdTipoAspirante", query = "SELECT t FROM TipoAspirante t WHERE t.idTipoAspirante = :idTipoAspirante")
    , @NamedQuery(name = "TipoAspirante.findByDescripcion", query = "SELECT t FROM TipoAspirante t WHERE t.descripcion = :descripcion")})
public class TipoAspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_aspirante")
    private Short idTipoAspirante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAspirante")
    private List<Aspirante> aspiranteList;

    public TipoAspirante() {
    }

    public TipoAspirante(Short idTipoAspirante) {
        this.idTipoAspirante = idTipoAspirante;
    }

    public TipoAspirante(Short idTipoAspirante, String descripcion) {
        this.idTipoAspirante = idTipoAspirante;
        this.descripcion = descripcion;
    }

    public Short getIdTipoAspirante() {
        return idTipoAspirante;
    }

    public void setIdTipoAspirante(Short idTipoAspirante) {
        this.idTipoAspirante = idTipoAspirante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Aspirante> getAspiranteList() {
        return aspiranteList;
    }

    public void setAspiranteList(List<Aspirante> aspiranteList) {
        this.aspiranteList = aspiranteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoAspirante != null ? idTipoAspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAspirante)) {
            return false;
        }
        TipoAspirante other = (TipoAspirante) object;
        if ((this.idTipoAspirante == null && other.idTipoAspirante != null) || (this.idTipoAspirante != null && !this.idTipoAspirante.equals(other.idTipoAspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante[ idTipoAspirante=" + idTipoAspirante + " ]";
    }
    
}
