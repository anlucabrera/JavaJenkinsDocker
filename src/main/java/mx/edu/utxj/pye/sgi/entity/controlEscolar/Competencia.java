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
 * @author UTXJ
 */
@Entity
@Table(name = "competencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competencia.findAll", query = "SELECT c FROM Competencia c"),
    @NamedQuery(name = "Competencia.findByIdCompetencia", query = "SELECT c FROM Competencia c WHERE c.idCompetencia = :idCompetencia"),
    @NamedQuery(name = "Competencia.findByNombre", query = "SELECT c FROM Competencia c WHERE c.nombre = :nombre")})
public class Competencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competencia")
    private Integer idCompetencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetencia")
    private List<Materia> materiaList;

    public Competencia() {
    }

    public Competencia(Integer idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public Competencia(Integer idCompetencia, String nombre) {
        this.idCompetencia = idCompetencia;
        this.nombre = nombre;
    }

    public Integer getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(Integer idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Materia> getMateriaList() {
        return materiaList;
    }

    public void setMateriaList(List<Materia> materiaList) {
        this.materiaList = materiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompetencia != null ? idCompetencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Competencia)) {
            return false;
        }
        Competencia other = (Competencia) object;
        if ((this.idCompetencia == null && other.idCompetencia != null) || (this.idCompetencia != null && !this.idCompetencia.equals(other.idCompetencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia[ idCompetencia=" + idCompetencia + " ]";
    }
    
}
