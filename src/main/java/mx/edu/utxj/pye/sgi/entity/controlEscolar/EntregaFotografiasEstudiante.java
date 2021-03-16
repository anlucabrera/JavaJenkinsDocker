/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "entrega_fotografias_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntregaFotografiasEstudiante.findAll", query = "SELECT e FROM EntregaFotografiasEstudiante e")
    , @NamedQuery(name = "EntregaFotografiasEstudiante.findByEntrega", query = "SELECT e FROM EntregaFotografiasEstudiante e WHERE e.entrega = :entrega")
    , @NamedQuery(name = "EntregaFotografiasEstudiante.findByFechaEntrega", query = "SELECT e FROM EntregaFotografiasEstudiante e WHERE e.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "EntregaFotografiasEstudiante.findByPersonalRecibio", query = "SELECT e FROM EntregaFotografiasEstudiante e WHERE e.personalRecibio = :personalRecibio")})
public class EntregaFotografiasEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "entrega")
    private Integer entrega;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal_recibio")
    private int personalRecibio;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante matricula;

    public EntregaFotografiasEstudiante() {
    }

    public EntregaFotografiasEstudiante(Integer entrega) {
        this.entrega = entrega;
    }

    public EntregaFotografiasEstudiante(Integer entrega, Date fechaEntrega, int personalRecibio) {
        this.entrega = entrega;
        this.fechaEntrega = fechaEntrega;
        this.personalRecibio = personalRecibio;
    }

    public Integer getEntrega() {
        return entrega;
    }

    public void setEntrega(Integer entrega) {
        this.entrega = entrega;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getPersonalRecibio() {
        return personalRecibio;
    }

    public void setPersonalRecibio(int personalRecibio) {
        this.personalRecibio = personalRecibio;
    }

    public EventoEstadia getEvento() {
        return evento;
    }

    public void setEvento(EventoEstadia evento) {
        this.evento = evento;
    }

    public Estudiante getMatricula() {
        return matricula;
    }

    public void setMatricula(Estudiante matricula) {
        this.matricula = matricula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entrega != null ? entrega.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntregaFotografiasEstudiante)) {
            return false;
        }
        EntregaFotografiasEstudiante other = (EntregaFotografiasEstudiante) object;
        if ((this.entrega == null && other.entrega != null) || (this.entrega != null && !this.entrega.equals(other.entrega))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EntregaFotografiasEstudiante[ entrega=" + entrega + " ]";
    }
    
}
