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
 * @author Desarrollo
 */
@Entity
@Table(name = "permisosevaluacionpoaex", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisosevaluacionpoaex.findAll", query = "SELECT p FROM Permisosevaluacionpoaex p")
    , @NamedQuery(name = "Permisosevaluacionpoaex.findByPermiso", query = "SELECT p FROM Permisosevaluacionpoaex p WHERE p.permiso = :permiso")
    , @NamedQuery(name = "Permisosevaluacionpoaex.findByFechaApertura", query = "SELECT p FROM Permisosevaluacionpoaex p WHERE p.fechaApertura = :fechaApertura")
    , @NamedQuery(name = "Permisosevaluacionpoaex.findByFechaCierre", query = "SELECT p FROM Permisosevaluacionpoaex p WHERE p.fechaCierre = :fechaCierre")})
public class Permisosevaluacionpoaex implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "permiso")
    private Integer permiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_apertura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaApertura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @JoinColumn(name = "evaluacionPOA", referencedColumnName = "evaluacionPOA")
    @ManyToOne(optional = false)
    private Calendarioevaluacionpoa evaluacionPOA;
    @JoinColumn(name = "procesoPOA", referencedColumnName = "procesoPOA")
    @ManyToOne(optional = false)
    private Procesopoa procesoPOA;

    public Permisosevaluacionpoaex() {
    }

    public Permisosevaluacionpoaex(Integer permiso) {
        this.permiso = permiso;
    }

    public Permisosevaluacionpoaex(Integer permiso, Date fechaApertura, Date fechaCierre) {
        this.permiso = permiso;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
    }

    public Integer getPermiso() {
        return permiso;
    }

    public void setPermiso(Integer permiso) {
        this.permiso = permiso;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Calendarioevaluacionpoa getEvaluacionPOA() {
        return evaluacionPOA;
    }

    public void setEvaluacionPOA(Calendarioevaluacionpoa evaluacionPOA) {
        this.evaluacionPOA = evaluacionPOA;
    }

    public Procesopoa getProcesoPOA() {
        return procesoPOA;
    }

    public void setProcesoPOA(Procesopoa procesoPOA) {
        this.procesoPOA = procesoPOA;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permiso != null ? permiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisosevaluacionpoaex)) {
            return false;
        }
        Permisosevaluacionpoaex other = (Permisosevaluacionpoaex) object;
        if ((this.permiso == null && other.permiso != null) || (this.permiso != null && !this.permiso.equals(other.permiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Permisosevaluacionpoaex[ permiso=" + permiso + " ]";
    }
    
}
