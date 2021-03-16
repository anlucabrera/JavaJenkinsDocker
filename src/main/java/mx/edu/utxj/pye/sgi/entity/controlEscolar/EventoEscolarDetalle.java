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
@Table(name = "evento_escolar_detalle", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventoEscolarDetalle.findAll", query = "SELECT e FROM EventoEscolarDetalle e")
    , @NamedQuery(name = "EventoEscolarDetalle.findByEventoDetalle", query = "SELECT e FROM EventoEscolarDetalle e WHERE e.eventoDetalle = :eventoDetalle")
    , @NamedQuery(name = "EventoEscolarDetalle.findByArea", query = "SELECT e FROM EventoEscolarDetalle e WHERE e.area = :area")
    , @NamedQuery(name = "EventoEscolarDetalle.findByPersona", query = "SELECT e FROM EventoEscolarDetalle e WHERE e.persona = :persona")
    , @NamedQuery(name = "EventoEscolarDetalle.findByInicio", query = "SELECT e FROM EventoEscolarDetalle e WHERE e.inicio = :inicio")
    , @NamedQuery(name = "EventoEscolarDetalle.findByFin", query = "SELECT e FROM EventoEscolarDetalle e WHERE e.fin = :fin")})
public class EventoEscolarDetalle implements Serializable {

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
    @Column(name = "inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fin;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEscolar evento;

    public EventoEscolarDetalle() {
    }

    public EventoEscolarDetalle(Integer eventoDetalle) {
        this.eventoDetalle = eventoDetalle;
    }

    public EventoEscolarDetalle(Integer eventoDetalle, Date inicio, Date fin) {
        this.eventoDetalle = eventoDetalle;
        this.inicio = inicio;
        this.fin = fin;
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

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public EventoEscolar getEvento() {
        return evento;
    }

    public void setEvento(EventoEscolar evento) {
        this.evento = evento;
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
        if (!(object instanceof EventoEscolarDetalle)) {
            return false;
        }
        EventoEscolarDetalle other = (EventoEscolarDetalle) object;
        if ((this.eventoDetalle == null && other.eventoDetalle != null) || (this.eventoDetalle != null && !this.eventoDetalle.equals(other.eventoDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolarDetalle[ eventoDetalle=" + eventoDetalle + " ]";
    }
    
}
