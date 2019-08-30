/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "asistencias", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asistencias.findAll", query = "SELECT a FROM Asistencias a")
    , @NamedQuery(name = "Asistencias.findByAsistencia", query = "SELECT a FROM Asistencias a WHERE a.asistencia = :asistencia")
    , @NamedQuery(name = "Asistencias.findByFechaHora", query = "SELECT a FROM Asistencias a WHERE a.fechaHora = :fechaHora")
    , @NamedQuery(name = "Asistencias.findByTipoAsistencia", query = "SELECT a FROM Asistencias a WHERE a.tipoAsistencia = :tipoAsistencia")})
public class Asistencias implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipoAsistencia")
    private String tipoAsistencia;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "asistencia")
    private Long asistencia;
    @OneToMany(mappedBy = "asistencia")
    private List<Asistenciasacademicas> asistenciasacademicasList;

    public Asistencias() {
    }

    public Asistencias(Long asistencia) {
        this.asistencia = asistencia;
    }

    public Asistencias(Long asistencia, Date fechaHora, String tipoAsistencia) {
        this.asistencia = asistencia;
        this.fechaHora = fechaHora;
        this.tipoAsistencia = tipoAsistencia;
    }

    public Long getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Long asistencia) {
        this.asistencia = asistencia;
    }


    @XmlTransient
    public List<Asistenciasacademicas> getAsistenciasacademicasList() {
        return asistenciasacademicasList;
    }

    public void setAsistenciasacademicasList(List<Asistenciasacademicas> asistenciasacademicasList) {
        this.asistenciasacademicasList = asistenciasacademicasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asistencia != null ? asistencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asistencias)) {
            return false;
        }
        Asistencias other = (Asistencias) object;
        if ((this.asistencia == null && other.asistencia != null) || (this.asistencia != null && !this.asistencia.equals(other.asistencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistencias[ asistencia=" + asistencia + " ]";
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoAsistencia() {
        return tipoAsistencia;
    }

    public void setTipoAsistencia(String tipoAsistencia) {
        this.tipoAsistencia = tipoAsistencia;
    }
    
}
