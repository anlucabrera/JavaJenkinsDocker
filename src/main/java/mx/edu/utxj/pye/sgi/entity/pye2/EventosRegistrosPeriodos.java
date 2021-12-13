/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "eventos_registros_periodos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventosRegistrosPeriodos.findAll", query = "SELECT e FROM EventosRegistrosPeriodos e")
    , @NamedQuery(name = "EventosRegistrosPeriodos.findByEventoRegistro", query = "SELECT e FROM EventosRegistrosPeriodos e WHERE e.eventosRegistrosPeriodosPK.eventoRegistro = :eventoRegistro")
    , @NamedQuery(name = "EventosRegistrosPeriodos.findByPeriodoEscolar", query = "SELECT e FROM EventosRegistrosPeriodos e WHERE e.eventosRegistrosPeriodosPK.periodoEscolar = :periodoEscolar")})
public class EventosRegistrosPeriodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EventosRegistrosPeriodosPK eventosRegistrosPeriodosPK;
    @JoinColumn(name = "evento_registro", referencedColumnName = "evento_registro", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventosRegistros eventosRegistros;

    public EventosRegistrosPeriodos() {
    }

    public EventosRegistrosPeriodos(EventosRegistrosPeriodosPK eventosRegistrosPeriodosPK) {
        this.eventosRegistrosPeriodosPK = eventosRegistrosPeriodosPK;
    }

    public EventosRegistrosPeriodos(int eventoRegistro, int periodoEscolar) {
        this.eventosRegistrosPeriodosPK = new EventosRegistrosPeriodosPK(eventoRegistro, periodoEscolar);
    }

    public EventosRegistrosPeriodosPK getEventosRegistrosPeriodosPK() {
        return eventosRegistrosPeriodosPK;
    }

    public void setEventosRegistrosPeriodosPK(EventosRegistrosPeriodosPK eventosRegistrosPeriodosPK) {
        this.eventosRegistrosPeriodosPK = eventosRegistrosPeriodosPK;
    }

    public EventosRegistros getEventosRegistros() {
        return eventosRegistros;
    }

    public void setEventosRegistros(EventosRegistros eventosRegistros) {
        this.eventosRegistros = eventosRegistros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventosRegistrosPeriodosPK != null ? eventosRegistrosPeriodosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosRegistrosPeriodos)) {
            return false;
        }
        EventosRegistrosPeriodos other = (EventosRegistrosPeriodos) object;
        if ((this.eventosRegistrosPeriodosPK == null && other.eventosRegistrosPeriodosPK != null) || (this.eventosRegistrosPeriodosPK != null && !this.eventosRegistrosPeriodosPK.equals(other.eventosRegistrosPeriodosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistrosPeriodos[ eventosRegistrosPeriodosPK=" + eventosRegistrosPeriodosPK + " ]";
    }
    
}
