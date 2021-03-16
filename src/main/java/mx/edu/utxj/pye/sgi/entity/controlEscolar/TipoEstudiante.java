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
 * @author UTXJ
 */
@Entity
@Table(name = "tipo_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEstudiante.findAll", query = "SELECT t FROM TipoEstudiante t")
    , @NamedQuery(name = "TipoEstudiante.findByIdTipoEstudiante", query = "SELECT t FROM TipoEstudiante t WHERE t.idTipoEstudiante = :idTipoEstudiante")
    , @NamedQuery(name = "TipoEstudiante.findByDescripcion", query = "SELECT t FROM TipoEstudiante t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "TipoEstudiante.findByActivo", query = "SELECT t FROM TipoEstudiante t WHERE t.activo = :activo")})
public class TipoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_estudiante")
    private Short idTipoEstudiante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoEstudiante", fetch = FetchType.LAZY)
    private List<Estudiante> estudianteList;

    public TipoEstudiante() {
    }

    public TipoEstudiante(Short idTipoEstudiante) {
        this.idTipoEstudiante = idTipoEstudiante;
    }

    public TipoEstudiante(Short idTipoEstudiante, String descripcion, boolean activo) {
        this.idTipoEstudiante = idTipoEstudiante;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Short getIdTipoEstudiante() {
        return idTipoEstudiante;
    }

    public void setIdTipoEstudiante(Short idTipoEstudiante) {
        this.idTipoEstudiante = idTipoEstudiante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEstudiante != null ? idTipoEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEstudiante)) {
            return false;
        }
        TipoEstudiante other = (TipoEstudiante) object;
        if ((this.idTipoEstudiante == null && other.idTipoEstudiante != null) || (this.idTipoEstudiante != null && !this.idTipoEstudiante.equals(other.idTipoEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante[ idTipoEstudiante=" + idTipoEstudiante + " ]";
    }
    
}
