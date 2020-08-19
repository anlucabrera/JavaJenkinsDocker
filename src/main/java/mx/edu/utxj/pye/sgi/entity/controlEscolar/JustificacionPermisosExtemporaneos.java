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
@Table(name = "justificacion_permisos_extemporaneos", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JustificacionPermisosExtemporaneos.findAll", query = "SELECT j FROM JustificacionPermisosExtemporaneos j")
    , @NamedQuery(name = "JustificacionPermisosExtemporaneos.findByJustificacion", query = "SELECT j FROM JustificacionPermisosExtemporaneos j WHERE j.justificacion = :justificacion")
    , @NamedQuery(name = "JustificacionPermisosExtemporaneos.findByDescripcion", query = "SELECT j FROM JustificacionPermisosExtemporaneos j WHERE j.descripcion = :descripcion")
    , @NamedQuery(name = "JustificacionPermisosExtemporaneos.findByActivo", query = "SELECT j FROM JustificacionPermisosExtemporaneos j WHERE j.activo = :activo")})
public class JustificacionPermisosExtemporaneos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "justificacion")
    private Integer justificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "justificacionPermiso", fetch = FetchType.LAZY)
    private List<PermisosCapturaExtemporaneaEstudiante> permisosCapturaExtemporaneaEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "justificacionPermiso", fetch = FetchType.LAZY)
    private List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList;

    public JustificacionPermisosExtemporaneos() {
    }

    public JustificacionPermisosExtemporaneos(Integer justificacion) {
        this.justificacion = justificacion;
    }

    public JustificacionPermisosExtemporaneos(Integer justificacion, String descripcion, boolean activo) {
        this.justificacion = justificacion;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(Integer justificacion) {
        this.justificacion = justificacion;
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
    public List<PermisosCapturaExtemporaneaEstudiante> getPermisosCapturaExtemporaneaEstudianteList() {
        return permisosCapturaExtemporaneaEstudianteList;
    }

    public void setPermisosCapturaExtemporaneaEstudianteList(List<PermisosCapturaExtemporaneaEstudiante> permisosCapturaExtemporaneaEstudianteList) {
        this.permisosCapturaExtemporaneaEstudianteList = permisosCapturaExtemporaneaEstudianteList;
    }

    @XmlTransient
    public List<PermisosCapturaExtemporaneaGrupal> getPermisosCapturaExtemporaneaGrupalList() {
        return permisosCapturaExtemporaneaGrupalList;
    }

    public void setPermisosCapturaExtemporaneaGrupalList(List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList) {
        this.permisosCapturaExtemporaneaGrupalList = permisosCapturaExtemporaneaGrupalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (justificacion != null ? justificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JustificacionPermisosExtemporaneos)) {
            return false;
        }
        JustificacionPermisosExtemporaneos other = (JustificacionPermisosExtemporaneos) object;
        if ((this.justificacion == null && other.justificacion != null) || (this.justificacion != null && !this.justificacion.equals(other.justificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.JustificacionPermisosExtemporaneos[ justificacion=" + justificacion + " ]";
    }
    
}
