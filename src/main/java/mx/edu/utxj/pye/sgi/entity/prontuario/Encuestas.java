/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "encuestas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Encuestas.findAll", query = "SELECT e FROM Encuestas e")
    , @NamedQuery(name = "Encuestas.findByEncuesta", query = "SELECT e FROM Encuestas e WHERE e.encuesta = :encuesta")
    , @NamedQuery(name = "Encuestas.findByPeriodo", query = "SELECT e FROM Encuestas e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "Encuestas.findByFechaInicio", query = "SELECT e FROM Encuestas e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Encuestas.findByFechafin", query = "SELECT e FROM Encuestas e WHERE e.fechafin = :fechafin")
    , @NamedQuery(name = "Encuestas.findByTipo", query = "SELECT e FROM Encuestas e WHERE e.tipo = :tipo")})
public class Encuestas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "encuesta")
    private Integer encuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechafin")
    @Temporal(TemporalType.DATE)
    private Date fechafin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 39)
    @Column(name = "tipo")
    private String tipo;

    public Encuestas() {
    }

    public Encuestas(Integer encuesta) {
        this.encuesta = encuesta;
    }

    public Encuestas(Integer encuesta, int periodo, Date fechaInicio, Date fechafin, String tipo) {
        this.encuesta = encuesta;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechafin = fechafin;
        this.tipo = tipo;
    }

    public Integer getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Integer encuesta) {
        this.encuesta = encuesta;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuesta != null ? encuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encuestas)) {
            return false;
        }
        Encuestas other = (Encuestas) object;
        if ((this.encuesta == null && other.encuesta != null) || (this.encuesta != null && !this.encuesta.equals(other.encuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Encuestas[ encuesta=" + encuesta + " ]";
    }
    
}
