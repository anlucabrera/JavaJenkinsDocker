/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "eventos_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventosTipos.findAll", query = "SELECT e FROM EventosTipos e")
    , @NamedQuery(name = "EventosTipos.findByEventoTipo", query = "SELECT e FROM EventosTipos e WHERE e.eventoTipo = :eventoTipo")
    , @NamedQuery(name = "EventosTipos.findByNombre", query = "SELECT e FROM EventosTipos e WHERE e.nombre = :nombre")})
public class EventosTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento_tipo")
    private Short eventoTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoTipo", fetch = FetchType.LAZY)
    private List<ActividadesFormacionIntegral> actividadesFormacionIntegralList;

    public EventosTipos() {
    }

    public EventosTipos(Short eventoTipo) {
        this.eventoTipo = eventoTipo;
    }

    public EventosTipos(Short eventoTipo, String nombre) {
        this.eventoTipo = eventoTipo;
        this.nombre = nombre;
    }

    public Short getEventoTipo() {
        return eventoTipo;
    }

    public void setEventoTipo(Short eventoTipo) {
        this.eventoTipo = eventoTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ActividadesFormacionIntegral> getActividadesFormacionIntegralList() {
        return actividadesFormacionIntegralList;
    }

    public void setActividadesFormacionIntegralList(List<ActividadesFormacionIntegral> actividadesFormacionIntegralList) {
        this.actividadesFormacionIntegralList = actividadesFormacionIntegralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventoTipo != null ? eventoTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosTipos)) {
            return false;
        }
        EventosTipos other = (EventosTipos) object;
        if ((this.eventoTipo == null && other.eventoTipo != null) || (this.eventoTipo != null && !this.eventoTipo.equals(other.eventoTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EventosTipos[ eventoTipo=" + eventoTipo + " ]";
    }
    
}
