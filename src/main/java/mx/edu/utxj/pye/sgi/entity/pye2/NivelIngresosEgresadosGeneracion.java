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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "nivel_ingresos_egresados_generacion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findAll", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n")
    , @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findByRegistro", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n WHERE n.registro = :registro")
    , @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findByFecha", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n WHERE n.fecha = :fecha")
    , @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findByGeneracion", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n WHERE n.generacion = :generacion")
    , @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findByProgramaEducativo", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n WHERE n.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findByHombres", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n WHERE n.hombres = :hombres")
    , @NamedQuery(name = "NivelIngresosEgresadosGeneracion.findByMujeres", query = "SELECT n FROM NivelIngresosEgresadosGeneracion n WHERE n.mujeres = :mujeres")})
public class NivelIngresosEgresadosGeneracion implements Serializable {

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
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "ingreso", referencedColumnName = "nivel")
    @ManyToOne(optional = false)
    private NivelIngresosTipos ingreso;

    public NivelIngresosEgresadosGeneracion() {
    }

    public NivelIngresosEgresadosGeneracion(Integer registro) {
        this.registro = registro;
    }

    public NivelIngresosEgresadosGeneracion(Integer registro, Date fecha, short generacion, short programaEducativo, int hombres, int mujeres) {
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

    public NivelIngresosTipos getIngreso() {
        return ingreso;
    }

    public void setIngreso(NivelIngresosTipos ingreso) {
        this.ingreso = ingreso;
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
        if (!(object instanceof NivelIngresosEgresadosGeneracion)) {
            return false;
        }
        NivelIngresosEgresadosGeneracion other = (NivelIngresosEgresadosGeneracion) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion[ registro=" + registro + " ]";
    }
    
}
