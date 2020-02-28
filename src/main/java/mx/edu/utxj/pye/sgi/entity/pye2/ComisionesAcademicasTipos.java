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
@Table(name = "comisiones_academicas_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisionesAcademicasTipos.findAll", query = "SELECT c FROM ComisionesAcademicasTipos c")
    , @NamedQuery(name = "ComisionesAcademicasTipos.findByTipoComision", query = "SELECT c FROM ComisionesAcademicasTipos c WHERE c.tipoComision = :tipoComision")
    , @NamedQuery(name = "ComisionesAcademicasTipos.findByDescripcion", query = "SELECT c FROM ComisionesAcademicasTipos c WHERE c.descripcion = :descripcion")})
public class ComisionesAcademicasTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tipo_comision")
    private Short tipoComision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoComision")
    private List<ComisionesAcademicas> comisionesAcademicasList;

    public ComisionesAcademicasTipos() {
    }

    public ComisionesAcademicasTipos(Short tipoComision) {
        this.tipoComision = tipoComision;
    }

    public ComisionesAcademicasTipos(Short tipoComision, String descripcion) {
        this.tipoComision = tipoComision;
        this.descripcion = descripcion;
    }

    public Short getTipoComision() {
        return tipoComision;
    }

    public void setTipoComision(Short tipoComision) {
        this.tipoComision = tipoComision;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ComisionesAcademicas> getComisionesAcademicasList() {
        return comisionesAcademicasList;
    }

    public void setComisionesAcademicasList(List<ComisionesAcademicas> comisionesAcademicasList) {
        this.comisionesAcademicasList = comisionesAcademicasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoComision != null ? tipoComision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionesAcademicasTipos)) {
            return false;
        }
        ComisionesAcademicasTipos other = (ComisionesAcademicasTipos) object;
        if ((this.tipoComision == null && other.tipoComision != null) || (this.tipoComision != null && !this.tipoComision.equals(other.tipoComision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasTipos[ tipoComision=" + tipoComision + " ]";
    }
    
}
