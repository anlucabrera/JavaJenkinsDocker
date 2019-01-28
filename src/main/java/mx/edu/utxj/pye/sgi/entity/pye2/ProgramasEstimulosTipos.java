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
@Table(name = "programas_estimulos_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEstimulosTipos.findAll", query = "SELECT p FROM ProgramasEstimulosTipos p")
    , @NamedQuery(name = "ProgramasEstimulosTipos.findByTipoPrograma", query = "SELECT p FROM ProgramasEstimulosTipos p WHERE p.tipoPrograma = :tipoPrograma")
    , @NamedQuery(name = "ProgramasEstimulosTipos.findByDescripcion", query = "SELECT p FROM ProgramasEstimulosTipos p WHERE p.descripcion = :descripcion")})
public class ProgramasEstimulosTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tipo_programa")
    private Short tipoPrograma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPrograma")
    private List<ProgramasEstimulos> programasEstimulosList;

    public ProgramasEstimulosTipos() {
    }

    public ProgramasEstimulosTipos(Short tipoPrograma) {
        this.tipoPrograma = tipoPrograma;
    }

    public ProgramasEstimulosTipos(Short tipoPrograma, String descripcion) {
        this.tipoPrograma = tipoPrograma;
        this.descripcion = descripcion;
    }

    public Short getTipoPrograma() {
        return tipoPrograma;
    }

    public void setTipoPrograma(Short tipoPrograma) {
        this.tipoPrograma = tipoPrograma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ProgramasEstimulos> getProgramasEstimulosList() {
        return programasEstimulosList;
    }

    public void setProgramasEstimulosList(List<ProgramasEstimulos> programasEstimulosList) {
        this.programasEstimulosList = programasEstimulosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoPrograma != null ? tipoPrograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEstimulosTipos)) {
            return false;
        }
        ProgramasEstimulosTipos other = (ProgramasEstimulosTipos) object;
        if ((this.tipoPrograma == null && other.tipoPrograma != null) || (this.tipoPrograma != null && !this.tipoPrograma.equals(other.tipoPrograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulosTipos[ tipoPrograma=" + tipoPrograma + " ]";
    }
    
}
