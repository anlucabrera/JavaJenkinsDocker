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
 * @author Desarrollo
 */
@Entity
@Table(name = "nivel_educativo", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelEducativo.findAll", query = "SELECT n FROM NivelEducativo n")
    , @NamedQuery(name = "NivelEducativo.findByIdNivelEducativo", query = "SELECT n FROM NivelEducativo n WHERE n.idNivelEducativo = :idNivelEducativo")
    , @NamedQuery(name = "NivelEducativo.findByNombre", query = "SELECT n FROM NivelEducativo n WHERE n.nombre = :nombre")})
public class NivelEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_nivel_educativo")
    private Short idNivelEducativo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNivelEducativo", fetch = FetchType.LAZY)
    private List<InstitucionAcademica> institucionAcademicaList;

    public NivelEducativo() {
    }

    public NivelEducativo(Short idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public NivelEducativo(Short idNivelEducativo, String nombre) {
        this.idNivelEducativo = idNivelEducativo;
        this.nombre = nombre;
    }

    public Short getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(Short idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<InstitucionAcademica> getInstitucionAcademicaList() {
        return institucionAcademicaList;
    }

    public void setInstitucionAcademicaList(List<InstitucionAcademica> institucionAcademicaList) {
        this.institucionAcademicaList = institucionAcademicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNivelEducativo != null ? idNivelEducativo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelEducativo)) {
            return false;
        }
        NivelEducativo other = (NivelEducativo) object;
        if ((this.idNivelEducativo == null && other.idNivelEducativo != null) || (this.idNivelEducativo != null && !this.idNivelEducativo.equals(other.idNivelEducativo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NivelEducativo[ idNivelEducativo=" + idNivelEducativo + " ]";
    }
    
}
