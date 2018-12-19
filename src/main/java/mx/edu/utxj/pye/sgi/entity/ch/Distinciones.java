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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "distinciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Distinciones.findAll", query = "SELECT d FROM Distinciones d")
    , @NamedQuery(name = "Distinciones.findByDistincion", query = "SELECT d FROM Distinciones d WHERE d.distincion = :distincion")
    , @NamedQuery(name = "Distinciones.findByNombreDistincion", query = "SELECT d FROM Distinciones d WHERE d.nombreDistincion = :nombreDistincion")
    , @NamedQuery(name = "Distinciones.findByFechaRealizacion", query = "SELECT d FROM Distinciones d WHERE d.fechaRealizacion = :fechaRealizacion")
    , @NamedQuery(name = "Distinciones.findByMotivoDescripcion", query = "SELECT d FROM Distinciones d WHERE d.motivoDescripcion = :motivoDescripcion")
    , @NamedQuery(name = "Distinciones.findByInstitucion", query = "SELECT d FROM Distinciones d WHERE d.institucion = :institucion")
    , @NamedQuery(name = "Distinciones.findByEvidenciaDistincion", query = "SELECT d FROM Distinciones d WHERE d.evidenciaDistincion = :evidenciaDistincion")
    , @NamedQuery(name = "Distinciones.findByEstatus", query = "SELECT d FROM Distinciones d WHERE d.estatus = :estatus")})
public class Distinciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "distincion")
    private Integer distincion;
    @Size(max = 500)
    @Column(name = "nombre_distincion")
    private String nombreDistincion;
    @Column(name = "fecha_realizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaRealizacion;
    @Size(max = 500)
    @Column(name = "motivo_descripcion")
    private String motivoDescripcion;
    @Size(max = 500)
    @Column(name = "institucion")
    private String institucion;
    @Size(max = 500)
    @Column(name = "evidenciaDistincion")
    private String evidenciaDistincion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_empleado", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal claveEmpleado;

    public Distinciones() {
    }

    public Distinciones(Integer distincion) {
        this.distincion = distincion;
    }

    public Distinciones(Integer distincion, String estatus) {
        this.distincion = distincion;
        this.estatus = estatus;
    }

    public Integer getDistincion() {
        return distincion;
    }

    public void setDistincion(Integer distincion) {
        this.distincion = distincion;
    }

    public String getNombreDistincion() {
        return nombreDistincion;
    }

    public void setNombreDistincion(String nombreDistincion) {
        this.nombreDistincion = nombreDistincion;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getMotivoDescripcion() {
        return motivoDescripcion;
    }

    public void setMotivoDescripcion(String motivoDescripcion) {
        this.motivoDescripcion = motivoDescripcion;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getEvidenciaDistincion() {
        return evidenciaDistincion;
    }

    public void setEvidenciaDistincion(String evidenciaDistincion) {
        this.evidenciaDistincion = evidenciaDistincion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getClaveEmpleado() {
        return claveEmpleado;
    }

    public void setClaveEmpleado(Personal claveEmpleado) {
        this.claveEmpleado = claveEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (distincion != null ? distincion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distinciones)) {
            return false;
        }
        Distinciones other = (Distinciones) object;
        if ((this.distincion == null && other.distincion != null) || (this.distincion != null && !this.distincion.equals(other.distincion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Distinciones[ distincion=" + distincion + " ]";
    }
    
}
