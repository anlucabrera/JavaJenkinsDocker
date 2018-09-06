/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "eventos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventos.findAll", query = "SELECT e FROM Eventos e")
    , @NamedQuery(name = "Eventos.findByEvento", query = "SELECT e FROM Eventos e WHERE e.evento = :evento")
    , @NamedQuery(name = "Eventos.findByPeriodo", query = "SELECT e FROM Eventos e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "Eventos.findByTipo", query = "SELECT e FROM Eventos e WHERE e.tipo = :tipo")
    , @NamedQuery(name = "Eventos.findByNombre", query = "SELECT e FROM Eventos e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Eventos.findByFechaInicio", query = "SELECT e FROM Eventos e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Eventos.findByFechaFin", query = "SELECT e FROM Eventos e WHERE e.fechaFin = :fechaFin")})
public class Eventos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento")
    private Integer evento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
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
    @JoinTable(name = "capital_humano.eventos_personal", joinColumns = {
        @JoinColumn(name = "evento", referencedColumnName = "evento")}, inverseJoinColumns = {
        @JoinColumn(name = "clave", referencedColumnName = "clave")})
    @ManyToMany
    private List<Personal> personalList;
    @ManyToMany(mappedBy = "eventosList")
    private List<PersonalCategorias> personalCategoriasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventos")
    private List<EventosAreas> eventosAreasList;

    public Eventos() {
    }

    public Eventos(Integer evento) {
        this.evento = evento;
    }

    public Eventos(Integer evento, int periodo, String tipo, String nombre, Date fechaInicio, Date fechaFin) {
        this.evento = evento;
        this.periodo = periodo;
        this.tipo = tipo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getEvento() {
        return evento;
    }

    public void setEvento(Integer evento) {
        this.evento = evento;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @XmlTransient
    public List<PersonalCategorias> getPersonalCategoriasList() {
        return personalCategoriasList;
    }

    public void setPersonalCategoriasList(List<PersonalCategorias> personalCategoriasList) {
        this.personalCategoriasList = personalCategoriasList;
    }

    @XmlTransient
    public List<EventosAreas> getEventosAreasList() {
        return eventosAreasList;
    }

    public void setEventosAreasList(List<EventosAreas> eventosAreasList) {
        this.eventosAreasList = eventosAreasList;
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
        if (!(object instanceof Eventos)) {
            return false;
        }
        Eventos other = (Eventos) object;
        if ((this.evento == null && other.evento != null) || (this.evento != null && !this.evento.equals(other.evento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Eventos[ evento=" + evento + " ]";
    }
    
}
