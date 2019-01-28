/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

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
 * @author Planeacion
 */
@Entity
@Table(name = "carreras_cgut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CarrerasCgut.findAll", query = "SELECT c FROM CarrerasCgut c")
    , @NamedQuery(name = "CarrerasCgut.findByCveCarrera", query = "SELECT c FROM CarrerasCgut c WHERE c.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "CarrerasCgut.findByNombre", query = "SELECT c FROM CarrerasCgut c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CarrerasCgut.findByFechaCreacion", query = "SELECT c FROM CarrerasCgut c WHERE c.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "CarrerasCgut.findByFechaTerminacion", query = "SELECT c FROM CarrerasCgut c WHERE c.fechaTerminacion = :fechaTerminacion")
    , @NamedQuery(name = "CarrerasCgut.findByAbreviatura", query = "SELECT c FROM CarrerasCgut c WHERE c.abreviatura = :abreviatura")
    , @NamedQuery(name = "CarrerasCgut.findByActivo", query = "SELECT c FROM CarrerasCgut c WHERE c.activo = :activo")
    , @NamedQuery(name = "CarrerasCgut.findByCveNivel", query = "SELECT c FROM CarrerasCgut c WHERE c.cveNivel = :cveNivel")
    , @NamedQuery(name = "CarrerasCgut.findByCveCarreraContinuidad", query = "SELECT c FROM CarrerasCgut c WHERE c.cveCarreraContinuidad = :cveCarreraContinuidad")})
public class CarrerasCgut implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_carrera")
    private Integer cveCarrera;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_terminacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTerminacion;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "cve_nivel")
    private Integer cveNivel;
    @Column(name = "cve_carrera_continuidad")
    private Integer cveCarreraContinuidad;

    public CarrerasCgut() {
    }

    public CarrerasCgut(Integer cveCarrera) {
        this.cveCarrera = cveCarrera;
    }

    public Integer getCveCarrera() {
        return cveCarrera;
    }

    public void setCveCarrera(Integer cveCarrera) {
        this.cveCarrera = cveCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaTerminacion() {
        return fechaTerminacion;
    }

    public void setFechaTerminacion(Date fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getCveNivel() {
        return cveNivel;
    }

    public void setCveNivel(Integer cveNivel) {
        this.cveNivel = cveNivel;
    }

    public Integer getCveCarreraContinuidad() {
        return cveCarreraContinuidad;
    }

    public void setCveCarreraContinuidad(Integer cveCarreraContinuidad) {
        this.cveCarreraContinuidad = cveCarreraContinuidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveCarrera != null ? cveCarrera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarrerasCgut)) {
            return false;
        }
        CarrerasCgut other = (CarrerasCgut) object;
        if ((this.cveCarrera == null && other.cveCarrera != null) || (this.cveCarrera != null && !this.cveCarrera.equals(other.cveCarrera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut[ cveCarrera=" + cveCarrera + " ]";
    }
    
}
