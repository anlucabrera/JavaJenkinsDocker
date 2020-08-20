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
@Table(name = "programas_educativos_niveles", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEducativosNiveles.findAll", query = "SELECT p FROM ProgramasEducativosNiveles p")
    , @NamedQuery(name = "ProgramasEducativosNiveles.findByNivel", query = "SELECT p FROM ProgramasEducativosNiveles p WHERE p.nivel = :nivel")
    , @NamedQuery(name = "ProgramasEducativosNiveles.findByNombre", query = "SELECT p FROM ProgramasEducativosNiveles p WHERE p.nombre = :nombre")})
public class ProgramasEducativosNiveles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "nivel")
    private String nivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel", fetch = FetchType.LAZY)
    private List<ProgramasEducativos> programasEducativosList;
    @OneToMany(mappedBy = "nivelEducativo", fetch = FetchType.LAZY)
    private List<AreasUniversidad> areasUniversidadList;

    public ProgramasEducativosNiveles() {
    }

    public ProgramasEducativosNiveles(String nivel) {
        this.nivel = nivel;
    }

    public ProgramasEducativosNiveles(String nivel, String nombre) {
        this.nivel = nivel;
        this.nombre = nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ProgramasEducativos> getProgramasEducativosList() {
        return programasEducativosList;
    }

    public void setProgramasEducativosList(List<ProgramasEducativos> programasEducativosList) {
        this.programasEducativosList = programasEducativosList;
    }

    @XmlTransient
    public List<AreasUniversidad> getAreasUniversidadList() {
        return areasUniversidadList;
    }

    public void setAreasUniversidadList(List<AreasUniversidad> areasUniversidadList) {
        this.areasUniversidadList = areasUniversidadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivel != null ? nivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEducativosNiveles)) {
            return false;
        }
        ProgramasEducativosNiveles other = (ProgramasEducativosNiveles) object;
        if ((this.nivel == null && other.nivel != null) || (this.nivel != null && !this.nivel.equals(other.nivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles[ nivel=" + nivel + " ]";
    }
    
}
