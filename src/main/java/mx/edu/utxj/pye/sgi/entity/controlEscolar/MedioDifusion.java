/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
 * @author UTXJ
 */
@Entity
@Table(name = "medio_difusion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedioDifusion.findAll", query = "SELECT m FROM MedioDifusion m")
    , @NamedQuery(name = "MedioDifusion.findByIdMedioDifusion", query = "SELECT m FROM MedioDifusion m WHERE m.idMedioDifusion = :idMedioDifusion")
    , @NamedQuery(name = "MedioDifusion.findByDescripcion", query = "SELECT m FROM MedioDifusion m WHERE m.descripcion = :descripcion")
    , @NamedQuery(name = "MedioDifusion.findByEstatus", query = "SELECT m FROM MedioDifusion m WHERE m.estatus = :estatus")})
public class MedioDifusion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medio_difusion")
    private Short idMedioDifusion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToMany(mappedBy = "r15medioImpacto")
    private List<EncuestaAspirante> encuestaAspiranteList;

    public MedioDifusion() {
    }

    public MedioDifusion(Short idMedioDifusion) {
        this.idMedioDifusion = idMedioDifusion;
    }

    public MedioDifusion(Short idMedioDifusion, String descripcion, boolean estatus) {
        this.idMedioDifusion = idMedioDifusion;
        this.descripcion = descripcion;
        this.estatus = estatus;
    }

    public Short getIdMedioDifusion() {
        return idMedioDifusion;
    }

    public void setIdMedioDifusion(Short idMedioDifusion) {
        this.idMedioDifusion = idMedioDifusion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<EncuestaAspirante> getEncuestaAspiranteList() {
        return encuestaAspiranteList;
    }

    public void setEncuestaAspiranteList(List<EncuestaAspirante> encuestaAspiranteList) {
        this.encuestaAspiranteList = encuestaAspiranteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedioDifusion != null ? idMedioDifusion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedioDifusion)) {
            return false;
        }
        MedioDifusion other = (MedioDifusion) object;
        if ((this.idMedioDifusion == null && other.idMedioDifusion != null) || (this.idMedioDifusion != null && !this.idMedioDifusion.equals(other.idMedioDifusion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion[ idMedioDifusion=" + idMedioDifusion + " ]";
    }
    
}
