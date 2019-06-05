/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "procesos_intexp", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesosIntexp.findAll", query = "SELECT p FROM ProcesosIntexp p")
    , @NamedQuery(name = "ProcesosIntexp.findByProceso", query = "SELECT p FROM ProcesosIntexp p WHERE p.proceso = :proceso")
    , @NamedQuery(name = "ProcesosIntexp.findByFechaInicio", query = "SELECT p FROM ProcesosIntexp p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ProcesosIntexp.findByFechaFin", query = "SELECT p FROM ProcesosIntexp p WHERE p.fechaFin = :fechaFin")})
public class ProcesosIntexp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "proceso")
    private Integer proceso;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proceso")
    private List<ExpedientesTitulacion> expedientesTitulacionList;

    public ProcesosIntexp() {
    }

    public ProcesosIntexp(Integer proceso) {
        this.proceso = proceso;
    }

    public ProcesosIntexp(Integer proceso, Date fechaInicio, Date fechaFin) {
        this.proceso = proceso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getProceso() {
        return proceso;
    }

    public void setProceso(Integer proceso) {
        this.proceso = proceso;
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

    @XmlTransient
    public List<ExpedientesTitulacion> getExpedientesTitulacionList() {
        return expedientesTitulacionList;
    }

    public void setExpedientesTitulacionList(List<ExpedientesTitulacion> expedientesTitulacionList) {
        this.expedientesTitulacionList = expedientesTitulacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proceso != null ? proceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesosIntexp)) {
            return false;
        }
        ProcesosIntexp other = (ProcesosIntexp) object;
        if ((this.proceso == null && other.proceso != null) || (this.proceso != null && !this.proceso.equals(other.proceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp[ proceso=" + proceso + " ]";
    }
    
}
