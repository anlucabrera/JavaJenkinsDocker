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
 * @author jonny
 */
@Entity
@Table(name = "investigaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Investigaciones.findAll", query = "SELECT i FROM Investigaciones i")
    , @NamedQuery(name = "Investigaciones.findByInvestigacion", query = "SELECT i FROM Investigaciones i WHERE i.investigacion = :investigacion")
    , @NamedQuery(name = "Investigaciones.findByClavePerosnal", query = "SELECT i FROM Investigaciones i WHERE i.clavePerosnal = :clavePerosnal")
    , @NamedQuery(name = "Investigaciones.findByNombreInvestigacion", query = "SELECT i FROM Investigaciones i WHERE i.nombreInvestigacion = :nombreInvestigacion")
    , @NamedQuery(name = "Investigaciones.findByFechaInicio", query = "SELECT i FROM Investigaciones i WHERE i.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Investigaciones.findByFechaFin", query = "SELECT i FROM Investigaciones i WHERE i.fechaFin = :fechaFin")
    , @NamedQuery(name = "Investigaciones.findByInstitucion", query = "SELECT i FROM Investigaciones i WHERE i.institucion = :institucion")
    , @NamedQuery(name = "Investigaciones.findByDecripcion", query = "SELECT i FROM Investigaciones i WHERE i.decripcion = :decripcion")
    , @NamedQuery(name = "Investigaciones.findByTipo", query = "SELECT i FROM Investigaciones i WHERE i.tipo = :tipo")})
public class Investigaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "investigacion")
    private Integer investigacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_Perosnal")
    private int clavePerosnal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre_investigacion")
    private String nombreInvestigacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "institucion")
    private String institucion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "decripcion")
    private String decripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo")
    private String tipo;

    public Investigaciones() {
    }

    public Investigaciones(Integer investigacion) {
        this.investigacion = investigacion;
    }

    public Investigaciones(Integer investigacion, int clavePerosnal, String nombreInvestigacion, Date fechaInicio, String institucion, String decripcion, String tipo) {
        this.investigacion = investigacion;
        this.clavePerosnal = clavePerosnal;
        this.nombreInvestigacion = nombreInvestigacion;
        this.fechaInicio = fechaInicio;
        this.institucion = institucion;
        this.decripcion = decripcion;
        this.tipo = tipo;
    }

    public Integer getInvestigacion() {
        return investigacion;
    }

    public void setInvestigacion(Integer investigacion) {
        this.investigacion = investigacion;
    }

    public int getClavePerosnal() {
        return clavePerosnal;
    }

    public void setClavePerosnal(int clavePerosnal) {
        this.clavePerosnal = clavePerosnal;
    }

    public String getNombreInvestigacion() {
        return nombreInvestigacion;
    }

    public void setNombreInvestigacion(String nombreInvestigacion) {
        this.nombreInvestigacion = nombreInvestigacion;
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

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
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
        hash += (investigacion != null ? investigacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Investigaciones)) {
            return false;
        }
        Investigaciones other = (Investigaciones) object;
        if ((this.investigacion == null && other.investigacion != null) || (this.investigacion != null && !this.investigacion.equals(other.investigacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Investigaciones[ investigacion=" + investigacion + " ]";
    }
    
}
