/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
public class EventosRegistrosPeriodosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evento_registro")
    private int eventoRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;

    public EventosRegistrosPeriodosPK() {
    }

    public EventosRegistrosPeriodosPK(int eventoRegistro, int periodoEscolar) {
        this.eventoRegistro = eventoRegistro;
        this.periodoEscolar = periodoEscolar;
    }

    public int getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(int eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) eventoRegistro;
        hash += (int) periodoEscolar;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosRegistrosPeriodosPK)) {
            return false;
        }
        EventosRegistrosPeriodosPK other = (EventosRegistrosPeriodosPK) object;
        if (this.eventoRegistro != other.eventoRegistro) {
            return false;
        }
        if (this.periodoEscolar != other.periodoEscolar) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistrosPeriodosPK[ eventoRegistro=" + eventoRegistro + ", periodoEscolar=" + periodoEscolar + " ]";
    }
    
}
