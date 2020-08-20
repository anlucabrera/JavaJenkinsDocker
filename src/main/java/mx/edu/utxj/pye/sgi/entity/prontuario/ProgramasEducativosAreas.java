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
@Table(name = "programas_educativos_areas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEducativosAreas.findAll", query = "SELECT p FROM ProgramasEducativosAreas p")
    , @NamedQuery(name = "ProgramasEducativosAreas.findByArea", query = "SELECT p FROM ProgramasEducativosAreas p WHERE p.area = :area")
    , @NamedQuery(name = "ProgramasEducativosAreas.findByNombre", query = "SELECT p FROM ProgramasEducativosAreas p WHERE p.nombre = :nombre")})
public class ProgramasEducativosAreas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "area")
    private Short area;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "area", fetch = FetchType.LAZY)
    private List<ProgramasEducativos> programasEducativosList;

    public ProgramasEducativosAreas() {
    }

    public ProgramasEducativosAreas(Short area) {
        this.area = area;
    }

    public ProgramasEducativosAreas(Short area, String nombre) {
        this.area = area;
        this.nombre = nombre;
    }

    public Short getArea() {
        return area;
    }

    public void setArea(Short area) {
        this.area = area;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (area != null ? area.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEducativosAreas)) {
            return false;
        }
        ProgramasEducativosAreas other = (ProgramasEducativosAreas) object;
        if ((this.area == null && other.area != null) || (this.area != null && !this.area.equals(other.area))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosAreas[ area=" + area + " ]";
    }
    
}
