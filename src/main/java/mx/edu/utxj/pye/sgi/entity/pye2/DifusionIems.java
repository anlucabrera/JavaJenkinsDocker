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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "difusion_iems", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DifusionIems.findAll", query = "SELECT d FROM DifusionIems d")
    , @NamedQuery(name = "DifusionIems.findByRegistro", query = "SELECT d FROM DifusionIems d WHERE d.registro = :registro")
    , @NamedQuery(name = "DifusionIems.findByTipo", query = "SELECT d FROM DifusionIems d WHERE d.tipo = :tipo")
    , @NamedQuery(name = "DifusionIems.findByFechaInicio", query = "SELECT d FROM DifusionIems d WHERE d.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "DifusionIems.findByFechaFin", query = "SELECT d FROM DifusionIems d WHERE d.fechaFin = :fechaFin")
    , @NamedQuery(name = "DifusionIems.findByDuracion", query = "SELECT d FROM DifusionIems d WHERE d.duracion = :duracion")
    , @NamedQuery(name = "DifusionIems.findByHombres", query = "SELECT d FROM DifusionIems d WHERE d.hombres = :hombres")
    , @NamedQuery(name = "DifusionIems.findByMujeres", query = "SELECT d FROM DifusionIems d WHERE d.mujeres = :mujeres")})
public class DifusionIems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tipo")
    private String tipo;
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
    @Size(min = 1, max = 8)
    @Column(name = "duracion")
    private String duracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private int hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private int mujeres;
    @JoinColumn(name = "iems", referencedColumnName = "iems")
    @ManyToOne(optional = false)
    private Iems iems;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public DifusionIems() {
    }

    public DifusionIems(Integer registro) {
        this.registro = registro;
    }

    public DifusionIems(Integer registro, String tipo, Date fechaInicio, Date fechaFin, String duracion, int hombres, int mujeres) {
        this.registro = registro;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracion = duracion;
        this.hombres = hombres;
        this.mujeres = mujeres;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
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

    public Iems getIems() {
        return iems;
    }

    public void setIems(Iems iems) {
        this.iems = iems;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
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
        if (!(object instanceof DifusionIems)) {
            return false;
        }
        DifusionIems other = (DifusionIems) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.DifusionIems[ registro=" + registro + " ]";
    }
    
}
