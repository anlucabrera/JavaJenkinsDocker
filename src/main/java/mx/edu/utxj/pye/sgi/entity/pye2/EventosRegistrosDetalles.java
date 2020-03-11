/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "eventos_registros_detalles", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventosRegistrosDetalles.findAll", query = "SELECT e FROM EventosRegistrosDetalles e")
    , @NamedQuery(name = "EventosRegistrosDetalles.findByEventoDetalle", query = "SELECT e FROM EventosRegistrosDetalles e WHERE e.eventoDetalle = :eventoDetalle")
    , @NamedQuery(name = "EventosRegistrosDetalles.findByArea", query = "SELECT e FROM EventosRegistrosDetalles e WHERE e.area = :area")
    , @NamedQuery(name = "EventosRegistrosDetalles.findByPersona", query = "SELECT e FROM EventosRegistrosDetalles e WHERE e.persona = :persona")
    , @NamedQuery(name = "EventosRegistrosDetalles.findByFechaInicio", query = "SELECT e FROM EventosRegistrosDetalles e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "EventosRegistrosDetalles.findByFechaFin", query = "SELECT e FROM EventosRegistrosDetalles e WHERE e.fechaFin = :fechaFin")})
public class EventosRegistrosDetalles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento_detalle")
    private Integer eventoDetalle;
    @Column(name = "area")
    private Integer area;
    @Column(name = "persona")
    private Integer persona;
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
    @JoinColumn(name = "evento_registro", referencedColumnName = "evento_registro")
    @ManyToOne(optional = false)
    private EventosRegistros eventoRegistro;

    public EventosRegistrosDetalles() {
    }

    public EventosRegistrosDetalles(Integer eventoDetalle) {
        this.eventoDetalle = eventoDetalle;
    }

    public EventosRegistrosDetalles(Integer eventoDetalle, Date fechaInicio, Date fechaFin) {
        this.eventoDetalle = eventoDetalle;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getEventoDetalle() {
        return eventoDetalle;
    }

    public void setEventoDetalle(Integer eventoDetalle) {
        this.eventoDetalle = eventoDetalle;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getPersona() {
        return persona;
    }

    public void setPersona(Integer persona) {
        this.persona = persona;
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

    public EventosRegistros getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(EventosRegistros eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventoDetalle != null ? eventoDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosRegistrosDetalles)) {
            return false;
        }
        EventosRegistrosDetalles other = (EventosRegistrosDetalles) object;
        if ((this.eventoDetalle == null && other.eventoDetalle != null) || (this.eventoDetalle != null && !this.eventoDetalle.equals(other.eventoDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistrosDetalles[ eventoDetalle=" + eventoDetalle + " ]";
    }
    
}
