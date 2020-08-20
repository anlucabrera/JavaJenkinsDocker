/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "bajas_causa", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BajasCausa.findAll", query = "SELECT b FROM BajasCausa b")
    , @NamedQuery(name = "BajasCausa.findByCveCausa", query = "SELECT b FROM BajasCausa b WHERE b.cveCausa = :cveCausa")
    , @NamedQuery(name = "BajasCausa.findByCausa", query = "SELECT b FROM BajasCausa b WHERE b.causa = :causa")})
public class BajasCausa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_causa")
    private Integer cveCausa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "causa")
    private String causa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "causaBaja", fetch = FetchType.LAZY)
    private List<DesercionPorEstudiante> desercionPorEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bajasCausa", fetch = FetchType.LAZY)
    private List<BajasCausaCategoria> bajasCausaCategoriaList;

    public BajasCausa() {
    }

    public BajasCausa(Integer cveCausa) {
        this.cveCausa = cveCausa;
    }

    public BajasCausa(Integer cveCausa, String causa) {
        this.cveCausa = cveCausa;
        this.causa = causa;
    }

    public Integer getCveCausa() {
        return cveCausa;
    }

    public void setCveCausa(Integer cveCausa) {
        this.cveCausa = cveCausa;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    @XmlTransient
    public List<DesercionPorEstudiante> getDesercionPorEstudianteList() {
        return desercionPorEstudianteList;
    }

    public void setDesercionPorEstudianteList(List<DesercionPorEstudiante> desercionPorEstudianteList) {
        this.desercionPorEstudianteList = desercionPorEstudianteList;
    }
    
    @XmlTransient
    public List<BajasCausaCategoria> getBajasCausaCategoriaList() {
        return bajasCausaCategoriaList;
    }

    public void setBajasCausaCategoriaList(List<BajasCausaCategoria> bajasCausaCategoriaList) {
        this.bajasCausaCategoriaList = bajasCausaCategoriaList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveCausa != null ? cveCausa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BajasCausa)) {
            return false;
        }
        BajasCausa other = (BajasCausa) object;
        if ((this.cveCausa == null && other.cveCausa != null) || (this.cveCausa != null && !this.cveCausa.equals(other.cveCausa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa[ cveCausa=" + cveCausa + " ]";
    }
    
}
