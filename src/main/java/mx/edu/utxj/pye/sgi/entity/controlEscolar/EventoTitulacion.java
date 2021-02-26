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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evento_titulacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventoTitulacion.findAll", query = "SELECT e FROM EventoTitulacion e")
    , @NamedQuery(name = "EventoTitulacion.findByEvento", query = "SELECT e FROM EventoTitulacion e WHERE e.evento = :evento")
    , @NamedQuery(name = "EventoTitulacion.findByNumProceso", query = "SELECT e FROM EventoTitulacion e WHERE e.numProceso = :numProceso")
    , @NamedQuery(name = "EventoTitulacion.findByFechaInicio", query = "SELECT e FROM EventoTitulacion e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "EventoTitulacion.findByFechaFin", query = "SELECT e FROM EventoTitulacion e WHERE e.fechaFin = :fechaFin")
    , @NamedQuery(name = "EventoTitulacion.findByGeneracion", query = "SELECT e FROM EventoTitulacion e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EventoTitulacion.findByNivel", query = "SELECT e FROM EventoTitulacion e WHERE e.nivel = :nivel")})
public class EventoTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento")
    private Integer evento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_proceso")
    private int numProceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "nivel")
    private String nivel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<ExpedienteTitulacion> expedienteTitulacionList;

    public EventoTitulacion() {
    }

    public EventoTitulacion(Integer evento) {
        this.evento = evento;
    }

    public EventoTitulacion(Integer evento, int numProceso, Date fechaInicio, Date fechaFin, short generacion, String nivel) {
        this.evento = evento;
        this.numProceso = numProceso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.generacion = generacion;
        this.nivel = nivel;
    }

    public Integer getEvento() {
        return evento;
    }

    public void setEvento(Integer evento) {
        this.evento = evento;
    }

    public int getNumProceso() {
        return numProceso;
    }

    public void setNumProceso(int numProceso) {
        this.numProceso = numProceso;
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

    @XmlTransient
    public List<ExpedienteTitulacion> getExpedienteTitulacionList() {
        return expedienteTitulacionList;
    }

    public void setExpedienteTitulacionList(List<ExpedienteTitulacion> expedienteTitulacionList) {
        this.expedienteTitulacionList = expedienteTitulacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evento != null ? evento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventoTitulacion)) {
            return false;
        }
        EventoTitulacion other = (EventoTitulacion) object;
        if ((this.evento == null && other.evento != null) || (this.evento != null && !this.evento.equals(other.evento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion[ evento=" + evento + " ]";
    }
    
}
