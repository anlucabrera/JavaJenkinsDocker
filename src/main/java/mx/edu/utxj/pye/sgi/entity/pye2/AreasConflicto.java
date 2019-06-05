/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
 * @author UTXJ
 */
@Entity
@Table(name = "areas_conflicto", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreasConflicto.findAll", query = "SELECT a FROM AreasConflicto a")
    , @NamedQuery(name = "AreasConflicto.findByAreaConflicto", query = "SELECT a FROM AreasConflicto a WHERE a.areaConflicto = :areaConflicto")
    , @NamedQuery(name = "AreasConflicto.findByDescripcion", query = "SELECT a FROM AreasConflicto a WHERE a.descripcion = :descripcion")})
public class AreasConflicto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "area_conflicto")
    private Short areaConflicto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaConflicto")
    private List<SesionIndividualMensualPsicopedogia> sesionIndividualMensualPsicopedogiaList;

    public AreasConflicto() {
    }

    public AreasConflicto(Short areaConflicto) {
        this.areaConflicto = areaConflicto;
    }

    public AreasConflicto(Short areaConflicto, String descripcion) {
        this.areaConflicto = areaConflicto;
        this.descripcion = descripcion;
    }

    public Short getAreaConflicto() {
        return areaConflicto;
    }

    public void setAreaConflicto(Short areaConflicto) {
        this.areaConflicto = areaConflicto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<SesionIndividualMensualPsicopedogia> getSesionIndividualMensualPsicopedogiaList() {
        return sesionIndividualMensualPsicopedogiaList;
    }

    public void setSesionIndividualMensualPsicopedogiaList(List<SesionIndividualMensualPsicopedogia> sesionIndividualMensualPsicopedogiaList) {
        this.sesionIndividualMensualPsicopedogiaList = sesionIndividualMensualPsicopedogiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (areaConflicto != null ? areaConflicto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreasConflicto)) {
            return false;
        }
        AreasConflicto other = (AreasConflicto) object;
        if ((this.areaConflicto == null && other.areaConflicto != null) || (this.areaConflicto != null && !this.areaConflicto.equals(other.areaConflicto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.AreasConflicto[ areaConflicto=" + areaConflicto + " ]";
    }
    
}
