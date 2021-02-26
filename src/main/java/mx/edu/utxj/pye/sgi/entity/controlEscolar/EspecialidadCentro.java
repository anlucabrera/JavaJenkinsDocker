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
 * @author Desarrollo
 */
@Entity
@Table(name = "especialidad_centro", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspecialidadCentro.findAll", query = "SELECT e FROM EspecialidadCentro e")
    , @NamedQuery(name = "EspecialidadCentro.findByIdEspecialidadCentro", query = "SELECT e FROM EspecialidadCentro e WHERE e.idEspecialidadCentro = :idEspecialidadCentro")
    , @NamedQuery(name = "EspecialidadCentro.findByNombre", query = "SELECT e FROM EspecialidadCentro e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EspecialidadCentro.findByEstatus", query = "SELECT e FROM EspecialidadCentro e WHERE e.estatus = :estatus")})
public class EspecialidadCentro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_especialidad_centro")
    private Integer idEspecialidadCentro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "especialidadIems", fetch = FetchType.LAZY)
    private List<DatosAcademicos> datosAcademicosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEspecialidad", fetch = FetchType.LAZY)
    private List<InstitucionAcademica> institucionAcademicaList;

    public EspecialidadCentro() {
    }

    public EspecialidadCentro(Integer idEspecialidadCentro) {
        this.idEspecialidadCentro = idEspecialidadCentro;
    }

    public EspecialidadCentro(Integer idEspecialidadCentro, String nombre, boolean estatus) {
        this.idEspecialidadCentro = idEspecialidadCentro;
        this.nombre = nombre;
        this.estatus = estatus;
    }

    public Integer getIdEspecialidadCentro() {
        return idEspecialidadCentro;
    }

    public void setIdEspecialidadCentro(Integer idEspecialidadCentro) {
        this.idEspecialidadCentro = idEspecialidadCentro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<DatosAcademicos> getDatosAcademicosList() {
        return datosAcademicosList;
    }

    public void setDatosAcademicosList(List<DatosAcademicos> datosAcademicosList) {
        this.datosAcademicosList = datosAcademicosList;
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
        hash += (idEspecialidadCentro != null ? idEspecialidadCentro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspecialidadCentro)) {
            return false;
        }
        EspecialidadCentro other = (EspecialidadCentro) object;
        if ((this.idEspecialidadCentro == null && other.idEspecialidadCentro != null) || (this.idEspecialidadCentro != null && !this.idEspecialidadCentro.equals(other.idEspecialidadCentro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EspecialidadCentro[ idEspecialidadCentro=" + idEspecialidadCentro + " ]";
    }
    
}
