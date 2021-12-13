/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "eventos_registros", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventosRegistros.findAll", query = "SELECT e FROM EventosRegistros e")
    , @NamedQuery(name = "EventosRegistros.findByEventoRegistro", query = "SELECT e FROM EventosRegistros e WHERE e.eventoRegistro = :eventoRegistro")
    , @NamedQuery(name = "EventosRegistros.findByFechaInicio", query = "SELECT e FROM EventosRegistros e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "EventosRegistros.findByFechaFin", query = "SELECT e FROM EventosRegistros e WHERE e.fechaFin = :fechaFin")
    , @NamedQuery(name = "EventosRegistros.findByMes", query = "SELECT e FROM EventosRegistros e WHERE e.mes = :mes")})
public class EventosRegistros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento_registro")
    private Integer eventoRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "mes")
    private String mes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventosRegistros", fetch = FetchType.LAZY)
    private List<EventosRegistrosPeriodos> eventosRegistrosPeriodosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoRegistro", fetch = FetchType.LAZY)
    private List<Registros> registrosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoRegistro", fetch = FetchType.LAZY)
    private List<EventosRegistrosDetalles> eventosRegistrosDetallesList;
    @JoinColumn(name = "ejercicio_fiscal", referencedColumnName = "ejercicio_fiscal")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EjerciciosFiscales ejercicioFiscal;

    public EventosRegistros() {
    }

    public EventosRegistros(Integer eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    public EventosRegistros(Integer eventoRegistro, Date fechaInicio, Date fechaFin, String mes) {
        this.eventoRegistro = eventoRegistro;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.mes = mes;
    }

    public Integer getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(Integer eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
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

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @XmlTransient
    public List<Registros> getRegistrosList() {
        return registrosList;
    }

    public void setRegistrosList(List<Registros> registrosList) {
        this.registrosList = registrosList;
    }

    @XmlTransient
    public List<EventosRegistrosDetalles> getEventosRegistrosDetallesList() {
        return eventosRegistrosDetallesList;
    }

    public void setEventosRegistrosDetallesList(List<EventosRegistrosDetalles> eventosRegistrosDetallesList) {
        this.eventosRegistrosDetallesList = eventosRegistrosDetallesList;
    }

    public EjerciciosFiscales getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(EjerciciosFiscales ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventoRegistro != null ? eventoRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventosRegistros)) {
            return false;
        }
        EventosRegistros other = (EventosRegistros) object;
        if ((this.eventoRegistro == null && other.eventoRegistro != null) || (this.eventoRegistro != null && !this.eventoRegistro.equals(other.eventoRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros[ eventoRegistro=" + eventoRegistro + " ]";
    }

    @XmlTransient
    public List<EventosRegistrosPeriodos> getEventosRegistrosPeriodosList() {
        return eventosRegistrosPeriodosList;
    }

    public void setEventosRegistrosPeriodosList(List<EventosRegistrosPeriodos> eventosRegistrosPeriodosList) {
        this.eventosRegistrosPeriodosList = eventosRegistrosPeriodosList;
    }
    
}
