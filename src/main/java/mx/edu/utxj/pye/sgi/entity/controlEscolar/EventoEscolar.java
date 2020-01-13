/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "evento_escolar", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventoEscolar.findAll", query = "SELECT e FROM EventoEscolar e")
    , @NamedQuery(name = "EventoEscolar.findByPeriodo", query = "SELECT e FROM EventoEscolar e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "EventoEscolar.findByEvento", query = "SELECT e FROM EventoEscolar e WHERE e.evento = :evento")
    , @NamedQuery(name = "EventoEscolar.findByInicio", query = "SELECT e FROM EventoEscolar e WHERE e.inicio = :inicio")
    , @NamedQuery(name = "EventoEscolar.findByFin", query = "SELECT e FROM EventoEscolar e WHERE e.fin = :fin")
    , @NamedQuery(name = "EventoEscolar.findByTipo", query = "SELECT e FROM EventoEscolar e WHERE e.tipo = :tipo")
    , @NamedQuery(name = "EventoEscolar.findByCreador", query = "SELECT e FROM EventoEscolar e WHERE e.creador = :creador")})
public class EventoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento")
    private Integer evento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    @Column(name = "fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 43)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creador")
    private int creador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<EventoEscolarDetalle> eventoEscolarDetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<CargaAcademica> cargaAcademicaList;

    public EventoEscolar() {
    }

    public EventoEscolar(Integer evento) {
        this.evento = evento;
    }

    public EventoEscolar(Integer evento, int periodo, Date inicio, String tipo, int creador) {
        this.evento = evento;
        this.periodo = periodo;
        this.inicio = inicio;
        this.tipo = tipo;
        this.creador = creador;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Integer getEvento() {
        return evento;
    }

    public void setEvento(Integer evento) {
        this.evento = evento;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCreador() {
        return creador;
    }

    public void setCreador(int creador) {
        this.creador = creador;
    }

    @XmlTransient
    public List<EventoEscolarDetalle> getEventoEscolarDetalleList() {
        return eventoEscolarDetalleList;
    }

    public void setEventoEscolarDetalleList(List<EventoEscolarDetalle> eventoEscolarDetalleList) {
        this.eventoEscolarDetalleList = eventoEscolarDetalleList;
    }

    @XmlTransient
    public List<CargaAcademica> getCargaAcademicaList() {
        return cargaAcademicaList;
    }

    public void setCargaAcademicaList(List<CargaAcademica> cargaAcademicaList) {
        this.cargaAcademicaList = cargaAcademicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evento != null ? evento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventoEscolar)) {
            return false;
        }
        EventoEscolar other = (EventoEscolar) object;
        if ((this.evento == null && other.evento != null) || (this.evento != null && !this.evento.equals(other.evento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar[ evento=" + evento + " ]";
    }
    
}
