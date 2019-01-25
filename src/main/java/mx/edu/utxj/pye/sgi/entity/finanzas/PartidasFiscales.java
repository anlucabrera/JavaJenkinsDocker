/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "partidas_fiscales", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PartidasFiscales.findAll", query = "SELECT p FROM PartidasFiscales p")
    , @NamedQuery(name = "PartidasFiscales.findByPartida", query = "SELECT p FROM PartidasFiscales p WHERE p.partida = :partida")
    , @NamedQuery(name = "PartidasFiscales.findByTransparencia", query = "SELECT p FROM PartidasFiscales p WHERE p.transparencia = :transparencia")})
public class PartidasFiscales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "partida")
    private Short partida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "transparencia")
    private boolean transparencia;
    @JoinColumn(name = "presupuesto", referencedColumnName = "presupuesto")
    @ManyToOne(optional = false)
    private CatalogoPresupuestos presupuesto;

    public PartidasFiscales() {
    }

    public PartidasFiscales(Short partida) {
        this.partida = partida;
    }

    public PartidasFiscales(Short partida, boolean transparencia) {
        this.partida = partida;
        this.transparencia = transparencia;
    }

    public Short getPartida() {
        return partida;
    }

    public void setPartida(Short partida) {
        this.partida = partida;
    }

    public boolean getTransparencia() {
        return transparencia;
    }

    public void setTransparencia(boolean transparencia) {
        this.transparencia = transparencia;
    }

    public CatalogoPresupuestos getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(CatalogoPresupuestos presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partida != null ? partida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartidasFiscales)) {
            return false;
        }
        PartidasFiscales other = (PartidasFiscales) object;
        if ((this.partida == null && other.partida != null) || (this.partida != null && !this.partida.equals(other.partida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.PartidasFiscales[ partida=" + partida + " ]";
    }
    
}
