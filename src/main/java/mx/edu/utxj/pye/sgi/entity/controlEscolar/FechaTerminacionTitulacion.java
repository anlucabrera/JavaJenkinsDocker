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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "fecha_terminacion_titulacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FechaTerminacionTitulacion.findAll", query = "SELECT f FROM FechaTerminacionTitulacion f")
    , @NamedQuery(name = "FechaTerminacionTitulacion.findByRegistro", query = "SELECT f FROM FechaTerminacionTitulacion f WHERE f.registro = :registro")
    , @NamedQuery(name = "FechaTerminacionTitulacion.findByGeneracion", query = "SELECT f FROM FechaTerminacionTitulacion f WHERE f.generacion = :generacion")
    , @NamedQuery(name = "FechaTerminacionTitulacion.findByNivel", query = "SELECT f FROM FechaTerminacionTitulacion f WHERE f.nivel = :nivel")
    , @NamedQuery(name = "FechaTerminacionTitulacion.findByFechaInicio", query = "SELECT f FROM FechaTerminacionTitulacion f WHERE f.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "FechaTerminacionTitulacion.findByFechaFin", query = "SELECT f FROM FechaTerminacionTitulacion f WHERE f.fechaFin = :fechaFin")
    , @NamedQuery(name = "FechaTerminacionTitulacion.findByActaExencion", query = "SELECT f FROM FechaTerminacionTitulacion f WHERE f.actaExencion = :actaExencion")})
public class FechaTerminacionTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "nivel")
    private String nivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acta_exencion")
    @Temporal(TemporalType.DATE)
    private Date actaExencion;

    public FechaTerminacionTitulacion() {
    }

    public FechaTerminacionTitulacion(Integer registro) {
        this.registro = registro;
    }

    public FechaTerminacionTitulacion(Integer registro, short generacion, String nivel, Date fechaInicio, Date fechaFin, Date actaExencion) {
        this.registro = registro;
        this.generacion = generacion;
        this.nivel = nivel;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.actaExencion = actaExencion;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getActaExencion() {
        return actaExencion;
    }

    public void setActaExencion(Date actaExencion) {
        this.actaExencion = actaExencion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechaTerminacionTitulacion)) {
            return false;
        }
        FechaTerminacionTitulacion other = (FechaTerminacionTitulacion) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.FechaTerminacionTitulacion[ registro=" + registro + " ]";
    }
    
}
