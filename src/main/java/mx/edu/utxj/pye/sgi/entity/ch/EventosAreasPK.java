/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class EventosAreasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evento")
    private int evento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_operativa")
    private int areaOperativa;

    public EventosAreasPK() {
    }

    public EventosAreasPK(int evento, int areaOperativa) {
        this.evento = evento;
        this.areaOperativa = areaOperativa;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public int getAreaOperativa() {
        return areaOperativa;
    }

    public void setAreaOperativa(int areaOperativa) {
        this.areaOperativa = areaOperativa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) evento;
        hash += (int) areaOperativa;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosAreasPK)) {
            return false;
        }
        EventosAreasPK other = (EventosAreasPK) object;
        if (this.evento != other.evento) {
            return false;
        }
        if (this.areaOperativa != other.areaOperativa) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK[ evento=" + evento + ", areaOperativa=" + areaOperativa + " ]";
    }
    
}
