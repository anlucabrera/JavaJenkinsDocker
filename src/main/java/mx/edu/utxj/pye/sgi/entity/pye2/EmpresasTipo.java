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
import javax.persistence.FetchType;
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
@Table(name = "empresas_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpresasTipo.findAll", query = "SELECT e FROM EmpresasTipo e")
    , @NamedQuery(name = "EmpresasTipo.findByEmptipo", query = "SELECT e FROM EmpresasTipo e WHERE e.emptipo = :emptipo")
    , @NamedQuery(name = "EmpresasTipo.findByDescripcion", query = "SELECT e FROM EmpresasTipo e WHERE e.descripcion = :descripcion")})
public class EmpresasTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "emptipo")
    private Short emptipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empTip", fetch = FetchType.LAZY)
    private List<OrganismosVinculados> organismosVinculadosList;

    public EmpresasTipo() {
    }

    public EmpresasTipo(Short emptipo) {
        this.emptipo = emptipo;
    }

    public EmpresasTipo(Short emptipo, String descripcion) {
        this.emptipo = emptipo;
        this.descripcion = descripcion;
    }

    public Short getEmptipo() {
        return emptipo;
    }

    public void setEmptipo(Short emptipo) {
        this.emptipo = emptipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<OrganismosVinculados> getOrganismosVinculadosList() {
        return organismosVinculadosList;
    }

    public void setOrganismosVinculadosList(List<OrganismosVinculados> organismosVinculadosList) {
        this.organismosVinculadosList = organismosVinculadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emptipo != null ? emptipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresasTipo)) {
            return false;
        }
        EmpresasTipo other = (EmpresasTipo) object;
        if ((this.emptipo == null && other.emptipo != null) || (this.emptipo != null && !this.emptipo.equals(other.emptipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EmpresasTipo[ emptipo=" + emptipo + " ]";
    }
    
}
