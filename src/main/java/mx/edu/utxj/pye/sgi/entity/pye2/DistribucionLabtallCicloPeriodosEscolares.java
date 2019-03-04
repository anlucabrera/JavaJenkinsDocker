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
@Table(name = "distribucion_labtall_ciclo_periodos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findAll", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByRegistro", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.registro = :registro")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByCicloEscolar", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByPeriodoEscolar", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByNombre", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByCapacidad", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.capacidad = :capacidad")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByAreaResponsable", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.areaResponsable = :areaResponsable")
    , @NamedQuery(name = "DistribucionLabtallCicloPeriodosEscolares.findByFechaHabilitacion", query = "SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d WHERE d.fechaHabilitacion = :fechaHabilitacion")})
public class DistribucionLabtallCicloPeriodosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "capacidad")
    private String capacidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_responsable")
    private short areaResponsable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_habilitacion")
    @Temporal(TemporalType.DATE)
    private Date fechaHabilitacion;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "aula_tipo", referencedColumnName = "aulatipo")
    @ManyToOne(optional = false)
    private AulasTipo aulaTipo;

    public DistribucionLabtallCicloPeriodosEscolares() {
    }

    public DistribucionLabtallCicloPeriodosEscolares(Integer registro) {
        this.registro = registro;
    }

    public DistribucionLabtallCicloPeriodosEscolares(Integer registro, int cicloEscolar, int periodoEscolar, String nombre, String capacidad, short areaResponsable, Date fechaHabilitacion) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.areaResponsable = areaResponsable;
        this.fechaHabilitacion = fechaHabilitacion;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public short getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(short areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public Date getFechaHabilitacion() {
        return fechaHabilitacion;
    }

    public void setFechaHabilitacion(Date fechaHabilitacion) {
        this.fechaHabilitacion = fechaHabilitacion;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public AulasTipo getAulaTipo() {
        return aulaTipo;
    }

    public void setAulaTipo(AulasTipo aulaTipo) {
        this.aulaTipo = aulaTipo;
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
        if (!(object instanceof DistribucionLabtallCicloPeriodosEscolares)) {
            return false;
        }
        DistribucionLabtallCicloPeriodosEscolares other = (DistribucionLabtallCicloPeriodosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares[ registro=" + registro + " ]";
    }
    
}
