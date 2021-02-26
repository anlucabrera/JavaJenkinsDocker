/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "unidad_materia_validacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateriaValidacion.findAll", query = "SELECT u FROM UnidadMateriaValidacion u")
    , @NamedQuery(name = "UnidadMateriaValidacion.findByConfiguracion", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.configuracion = :configuracion")
    , @NamedQuery(name = "UnidadMateriaValidacion.findBySolicitada", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.solicitada = :solicitada")
    , @NamedQuery(name = "UnidadMateriaValidacion.findByTutor", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.tutor = :tutor")
    , @NamedQuery(name = "UnidadMateriaValidacion.findByTutorValidacionFecha", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.tutorValidacionFecha = :tutorValidacionFecha")
    , @NamedQuery(name = "UnidadMateriaValidacion.findByDirector", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.director = :director")
    , @NamedQuery(name = "UnidadMateriaValidacion.findByDirectorValidacionFecha", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.directorValidacionFecha = :directorValidacionFecha")
    , @NamedQuery(name = "UnidadMateriaValidacion.findByEstado", query = "SELECT u FROM UnidadMateriaValidacion u WHERE u.estado = :estado")})
public class UnidadMateriaValidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion")
    private Integer configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "solicitada")
    private boolean solicitada;
    @Column(name = "tutor")
    private Integer tutor;
    @Column(name = "tutor_validacion_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tutorValidacionFecha;
    @Column(name = "director")
    private Integer director;
    @Size(max = 255)
    @Column(name = "director_validacion_fecha")
    private String directorValidacionFecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 33)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMateriaConfiguracion unidadMateriaConfiguracion;

    public UnidadMateriaValidacion() {
    }

    public UnidadMateriaValidacion(Integer configuracion) {
        this.configuracion = configuracion;
    }

    public UnidadMateriaValidacion(Integer configuracion, boolean solicitada, String estado) {
        this.configuracion = configuracion;
        this.solicitada = solicitada;
        this.estado = estado;
    }

    public Integer getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Integer configuracion) {
        this.configuracion = configuracion;
    }

    public boolean getSolicitada() {
        return solicitada;
    }

    public void setSolicitada(boolean solicitada) {
        this.solicitada = solicitada;
    }

    public Integer getTutor() {
        return tutor;
    }

    public void setTutor(Integer tutor) {
        this.tutor = tutor;
    }

    public Date getTutorValidacionFecha() {
        return tutorValidacionFecha;
    }

    public void setTutorValidacionFecha(Date tutorValidacionFecha) {
        this.tutorValidacionFecha = tutorValidacionFecha;
    }

    public Integer getDirector() {
        return director;
    }

    public void setDirector(Integer director) {
        this.director = director;
    }

    public String getDirectorValidacionFecha() {
        return directorValidacionFecha;
    }

    public void setDirectorValidacionFecha(String directorValidacionFecha) {
        this.directorValidacionFecha = directorValidacionFecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public UnidadMateriaConfiguracion getUnidadMateriaConfiguracion() {
        return unidadMateriaConfiguracion;
    }

    public void setUnidadMateriaConfiguracion(UnidadMateriaConfiguracion unidadMateriaConfiguracion) {
        this.unidadMateriaConfiguracion = unidadMateriaConfiguracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configuracion != null ? configuracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaValidacion)) {
            return false;
        }
        UnidadMateriaValidacion other = (UnidadMateriaValidacion) object;
        if ((this.configuracion == null && other.configuracion != null) || (this.configuracion != null && !this.configuracion.equals(other.configuracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaValidacion[ configuracion=" + configuracion + " ]";
    }
    
}
