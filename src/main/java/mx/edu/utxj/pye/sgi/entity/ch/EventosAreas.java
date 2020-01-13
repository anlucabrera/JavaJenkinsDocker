/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "eventos_areas", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventosAreas.findAll", query = "SELECT e FROM EventosAreas e")
    , @NamedQuery(name = "EventosAreas.findByEvento", query = "SELECT e FROM EventosAreas e WHERE e.eventosAreasPK.evento = :evento")
    , @NamedQuery(name = "EventosAreas.findByAreaOperativa", query = "SELECT e FROM EventosAreas e WHERE e.eventosAreasPK.areaOperativa = :areaOperativa")})
public class EventosAreas implements Serializable {

    @Column(name = "diasExtra")
    private Integer diasExtra;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EventosAreasPK eventosAreasPK;
    @JoinColumn(name = "evento", referencedColumnName = "evento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Eventos eventos;

    public EventosAreas() {
    }

    public EventosAreas(EventosAreasPK eventosAreasPK) {
        this.eventosAreasPK = eventosAreasPK;
    }

    public EventosAreas(int evento, short areaOperativa) {
        this.eventosAreasPK = new EventosAreasPK(evento, areaOperativa);
    }

    public EventosAreasPK getEventosAreasPK() {
        return eventosAreasPK;
    }

    public void setEventosAreasPK(EventosAreasPK eventosAreasPK) {
        this.eventosAreasPK = eventosAreasPK;
    }

    public Eventos getEventos() {
        return eventos;
    }

    public void setEventos(Eventos eventos) {
        this.eventos = eventos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventosAreasPK != null ? eventosAreasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosAreas)) {
            return false;
        }
        EventosAreas other = (EventosAreas) object;
        if ((this.eventosAreasPK == null && other.eventosAreasPK != null) || (this.eventosAreasPK != null && !this.eventosAreasPK.equals(other.eventosAreasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EventosAreas[ eventosAreasPK=" + eventosAreasPK + " ]";
    }

    public Integer getDiasExtra() {
        return diasExtra;
    }

    public void setDiasExtra(Integer diasExtra) {
        this.diasExtra = diasExtra;
    }
    
}
