/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "actividad_egresado_generacion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadEgresadoGeneracion.findAll", query = "SELECT a FROM ActividadEgresadoGeneracion a")
    , @NamedQuery(name = "ActividadEgresadoGeneracion.findByRegistro", query = "SELECT a FROM ActividadEgresadoGeneracion a WHERE a.registro = :registro")
    , @NamedQuery(name = "ActividadEgresadoGeneracion.findByFecha", query = "SELECT a FROM ActividadEgresadoGeneracion a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "ActividadEgresadoGeneracion.findByGeneracion", query = "SELECT a FROM ActividadEgresadoGeneracion a WHERE a.generacion = :generacion")
    , @NamedQuery(name = "ActividadEgresadoGeneracion.findByProgramaEducativo", query = "SELECT a FROM ActividadEgresadoGeneracion a WHERE a.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "ActividadEgresadoGeneracion.findByHombres", query = "SELECT a FROM ActividadEgresadoGeneracion a WHERE a.hombres = :hombres")
    , @NamedQuery(name = "ActividadEgresadoGeneracion.findByMujeres", query = "SELECT a FROM ActividadEgresadoGeneracion a WHERE a.mujeres = :mujeres")})
public class ActividadEgresadoGeneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private int hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private int mujeres;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;
    @JoinColumn(name = "actividad", referencedColumnName = "actividad")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ActividadEgresadoTipos actividad;

    public ActividadEgresadoGeneracion() {
    }

    public ActividadEgresadoGeneracion(Integer registro) {
        this.registro = registro;
    }

    public ActividadEgresadoGeneracion(Integer registro, Date fecha, short generacion, short programaEducativo, int hombres, int mujeres) {
        this.registro = registro;
        this.fecha = fecha;
        this.generacion = generacion;
        this.programaEducativo = programaEducativo;
        this.hombres = hombres;
        this.mujeres = mujeres;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getHombres() {
        return hombres;
    }

    public void setHombres(int hombres) {
        this.hombres = hombres;
    }

    public int getMujeres() {
        return mujeres;
    }

    public void setMujeres(int mujeres) {
        this.mujeres = mujeres;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public ActividadEgresadoTipos getActividad() {
        return actividad;
    }

    public void setActividad(ActividadEgresadoTipos actividad) {
        this.actividad = actividad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadEgresadoGeneracion)) {
            return false;
        }
        ActividadEgresadoGeneracion other = (ActividadEgresadoGeneracion) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoGeneracion[ registro=" + registro + " ]";
    }
    
}
