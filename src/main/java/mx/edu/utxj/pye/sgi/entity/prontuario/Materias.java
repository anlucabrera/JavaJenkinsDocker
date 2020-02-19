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
@Table(name = "materias", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materias.findAll", query = "SELECT m FROM Materias m")
    , @NamedQuery(name = "Materias.findByCveMateria", query = "SELECT m FROM Materias m WHERE m.cveMateria = :cveMateria")
    , @NamedQuery(name = "Materias.findByNombre", query = "SELECT m FROM Materias m WHERE m.nombre = :nombre")})
public class Materias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materias")
    private List<CalificacionesCuatrimestre> calificacionesCuatrimestreList;

    public Materias() {
    }

    public Materias(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public Materias(String cveMateria, String nombre) {
        this.cveMateria = cveMateria;
        this.nombre = nombre;
    }

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<CalificacionesCuatrimestre> getCalificacionesCuatrimestreList() {
        return calificacionesCuatrimestreList;
    }

    public void setCalificacionesCuatrimestreList(List<CalificacionesCuatrimestre> calificacionesCuatrimestreList) {
        this.calificacionesCuatrimestreList = calificacionesCuatrimestreList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveMateria != null ? cveMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materias)) {
            return false;
        }
        Materias other = (Materias) object;
        if ((this.cveMateria == null && other.cveMateria != null) || (this.cveMateria != null && !this.cveMateria.equals(other.cveMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Materias[ cveMateria=" + cveMateria + " ]";
    }

}