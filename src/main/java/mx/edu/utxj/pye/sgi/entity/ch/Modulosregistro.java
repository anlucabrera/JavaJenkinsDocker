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
 * @author Finanzas1
 */
@Entity
@Table(name = "modulosregistro", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modulosregistro.findAll", query = "SELECT m FROM Modulosregistro m")
    , @NamedQuery(name = "Modulosregistro.findByModulo", query = "SELECT m FROM Modulosregistro m WHERE m.modulo = :modulo")
    , @NamedQuery(name = "Modulosregistro.findByNombre", query = "SELECT m FROM Modulosregistro m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "Modulosregistro.findByFechaInicio", query = "SELECT m FROM Modulosregistro m WHERE m.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Modulosregistro.findByFechaFin", query = "SELECT m FROM Modulosregistro m WHERE m.fechaFin = :fechaFin")
    , @NamedQuery(name = "Modulosregistro.findByActividadUsuario", query = "SELECT m FROM Modulosregistro m WHERE m.actividadUsuario = :actividadUsuario")})
public class Modulosregistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "modulo")
    private Integer modulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "actividadUsuario")
    private String actividadUsuario;

    public Modulosregistro() {
    }

    public Modulosregistro(Integer modulo) {
        this.modulo = modulo;
    }

    public Modulosregistro(Integer modulo, String nombre, Date fechaInicio, Date fechaFin, String actividadUsuario) {
        this.modulo = modulo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.actividadUsuario = actividadUsuario;
    }

    public Integer getModulo() {
        return modulo;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
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

    public String getActividadUsuario() {
        return actividadUsuario;
    }

    public void setActividadUsuario(String actividadUsuario) {
        this.actividadUsuario = actividadUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modulo != null ? modulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modulosregistro)) {
            return false;
        }
        Modulosregistro other = (Modulosregistro) object;
        if ((this.modulo == null && other.modulo != null) || (this.modulo != null && !this.modulo.equals(other.modulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro[ modulo=" + modulo + " ]";
    }
    
}
