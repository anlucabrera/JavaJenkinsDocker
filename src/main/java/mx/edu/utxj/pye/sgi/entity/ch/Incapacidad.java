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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "incapacidad", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incapacidad.findAll", query = "SELECT i FROM Incapacidad i")
    , @NamedQuery(name = "Incapacidad.findByIncapacidad", query = "SELECT i FROM Incapacidad i WHERE i.incapacidad = :incapacidad")
    , @NamedQuery(name = "Incapacidad.findByNumeroIncapacidad", query = "SELECT i FROM Incapacidad i WHERE i.numeroIncapacidad = :numeroIncapacidad")
    , @NamedQuery(name = "Incapacidad.findByFechaInicio", query = "SELECT i FROM Incapacidad i WHERE i.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Incapacidad.findByFechaFin", query = "SELECT i FROM Incapacidad i WHERE i.fechaFin = :fechaFin")
    , @NamedQuery(name = "Incapacidad.findByEvidencia", query = "SELECT i FROM Incapacidad i WHERE i.evidencia = :evidencia")})
public class Incapacidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "incapacidad")
    private Integer incapacidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_incapacidad")
    private int numeroIncapacidad;
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
    @Size(min = 1, max = 500)
    @Column(name = "evidencia")
    private String evidencia;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Personal clavePersonal;

    public Incapacidad() {
    }

    public Incapacidad(Integer incapacidad) {
        this.incapacidad = incapacidad;
    }

    public Incapacidad(Integer incapacidad, int numeroIncapacidad, Date fechaInicio, Date fechaFin, String evidencia) {
        this.incapacidad = incapacidad;
        this.numeroIncapacidad = numeroIncapacidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.evidencia = evidencia;
    }

    public Integer getIncapacidad() {
        return incapacidad;
    }

    public void setIncapacidad(Integer incapacidad) {
        this.incapacidad = incapacidad;
    }

    public int getNumeroIncapacidad() {
        return numeroIncapacidad;
    }

    public void setNumeroIncapacidad(int numeroIncapacidad) {
        this.numeroIncapacidad = numeroIncapacidad;
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

    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (incapacidad != null ? incapacidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Incapacidad)) {
            return false;
        }
        Incapacidad other = (Incapacidad) object;
        if ((this.incapacidad == null && other.incapacidad != null) || (this.incapacidad != null && !this.incapacidad.equals(other.incapacidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Incapacidad[ incapacidad=" + incapacidad + " ]";
    }
    
}
