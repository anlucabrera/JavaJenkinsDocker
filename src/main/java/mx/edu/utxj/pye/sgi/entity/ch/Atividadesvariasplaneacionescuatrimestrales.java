/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "atividadesvariasplaneacionescuatrimestrales", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atividadesvariasplaneacionescuatrimestrales.findAll", query = "SELECT a FROM Atividadesvariasplaneacionescuatrimestrales a")
    , @NamedQuery(name = "Atividadesvariasplaneacionescuatrimestrales.findByActividad", query = "SELECT a FROM Atividadesvariasplaneacionescuatrimestrales a WHERE a.atividadesvariasplaneacionescuatrimestralesPK.actividad = :actividad")
    , @NamedQuery(name = "Atividadesvariasplaneacionescuatrimestrales.findByPlaneacion", query = "SELECT a FROM Atividadesvariasplaneacionescuatrimestrales a WHERE a.atividadesvariasplaneacionescuatrimestralesPK.planeacion = :planeacion")
    , @NamedQuery(name = "Atividadesvariasplaneacionescuatrimestrales.findByHorasAsignadas", query = "SELECT a FROM Atividadesvariasplaneacionescuatrimestrales a WHERE a.horasAsignadas = :horasAsignadas")})
public class Atividadesvariasplaneacionescuatrimestrales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AtividadesvariasplaneacionescuatrimestralesPK atividadesvariasplaneacionescuatrimestralesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horasAsignadas")
    private int horasAsignadas;
    @JoinColumn(name = "actividad", referencedColumnName = "actividad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Actividadesvarias actividadesvarias;
    @JoinColumn(name = "planeacion", referencedColumnName = "planeacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PlaneacionesCuatrimestrales planeacionesCuatrimestrales;

    public Atividadesvariasplaneacionescuatrimestrales() {
    }

    public Atividadesvariasplaneacionescuatrimestrales(AtividadesvariasplaneacionescuatrimestralesPK atividadesvariasplaneacionescuatrimestralesPK) {
        this.atividadesvariasplaneacionescuatrimestralesPK = atividadesvariasplaneacionescuatrimestralesPK;
    }

    public Atividadesvariasplaneacionescuatrimestrales(AtividadesvariasplaneacionescuatrimestralesPK atividadesvariasplaneacionescuatrimestralesPK, int horasAsignadas) {
        this.atividadesvariasplaneacionescuatrimestralesPK = atividadesvariasplaneacionescuatrimestralesPK;
        this.horasAsignadas = horasAsignadas;
    }

    public Atividadesvariasplaneacionescuatrimestrales(int actividad, int planeacion) {
        this.atividadesvariasplaneacionescuatrimestralesPK = new AtividadesvariasplaneacionescuatrimestralesPK(actividad, planeacion);
    }

    public AtividadesvariasplaneacionescuatrimestralesPK getAtividadesvariasplaneacionescuatrimestralesPK() {
        return atividadesvariasplaneacionescuatrimestralesPK;
    }

    public void setAtividadesvariasplaneacionescuatrimestralesPK(AtividadesvariasplaneacionescuatrimestralesPK atividadesvariasplaneacionescuatrimestralesPK) {
        this.atividadesvariasplaneacionescuatrimestralesPK = atividadesvariasplaneacionescuatrimestralesPK;
    }

    public int getHorasAsignadas() {
        return horasAsignadas;
    }

    public void setHorasAsignadas(int horasAsignadas) {
        this.horasAsignadas = horasAsignadas;
    }

    public Actividadesvarias getActividadesvarias() {
        return actividadesvarias;
    }

    public void setActividadesvarias(Actividadesvarias actividadesvarias) {
        this.actividadesvarias = actividadesvarias;
    }

    public PlaneacionesCuatrimestrales getPlaneacionesCuatrimestrales() {
        return planeacionesCuatrimestrales;
    }

    public void setPlaneacionesCuatrimestrales(PlaneacionesCuatrimestrales planeacionesCuatrimestrales) {
        this.planeacionesCuatrimestrales = planeacionesCuatrimestrales;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (atividadesvariasplaneacionescuatrimestralesPK != null ? atividadesvariasplaneacionescuatrimestralesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atividadesvariasplaneacionescuatrimestrales)) {
            return false;
        }
        Atividadesvariasplaneacionescuatrimestrales other = (Atividadesvariasplaneacionescuatrimestrales) object;
        if ((this.atividadesvariasplaneacionescuatrimestralesPK == null && other.atividadesvariasplaneacionescuatrimestralesPK != null) || (this.atividadesvariasplaneacionescuatrimestralesPK != null && !this.atividadesvariasplaneacionescuatrimestralesPK.equals(other.atividadesvariasplaneacionescuatrimestralesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Atividadesvariasplaneacionescuatrimestrales[ atividadesvariasplaneacionescuatrimestralesPK=" + atividadesvariasplaneacionescuatrimestralesPK + " ]";
    }
    
}
