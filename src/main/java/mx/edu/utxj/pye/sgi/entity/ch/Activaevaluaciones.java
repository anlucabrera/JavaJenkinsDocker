/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
@Table(name = "activaevaluaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activaevaluaciones.findAll", query = "SELECT a FROM Activaevaluaciones a")
    , @NamedQuery(name = "Activaevaluaciones.findById", query = "SELECT a FROM Activaevaluaciones a WHERE a.id = :id")
    , @NamedQuery(name = "Activaevaluaciones.findByTipo", query = "SELECT a FROM Activaevaluaciones a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "Activaevaluaciones.findByEvaluacion", query = "SELECT a FROM Activaevaluaciones a WHERE a.evaluacion = :evaluacion")
    , @NamedQuery(name = "Activaevaluaciones.findByClave", query = "SELECT a FROM Activaevaluaciones a WHERE a.clave = :clave")
    , @NamedQuery(name = "Activaevaluaciones.findByFechaInicio", query = "SELECT a FROM Activaevaluaciones a WHERE a.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Activaevaluaciones.findByFechaFin", query = "SELECT a FROM Activaevaluaciones a WHERE a.fechaFin = :fechaFin")})
public class Activaevaluaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;
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

    public Activaevaluaciones() {
    }

    public Activaevaluaciones(Integer id) {
        this.id = id;
    }

    public Activaevaluaciones(Integer id, String tipo, int evaluacion, int clave, Date fechaInicio, Date fechaFin) {
        this.id = id;
        this.tipo = tipo;
        this.evaluacion = evaluacion;
        this.clave = clave;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Activaevaluaciones)) {
            return false;
        }
        Activaevaluaciones other = (Activaevaluaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Activaevaluaciones[ id=" + id + " ]";
    }
    
}
