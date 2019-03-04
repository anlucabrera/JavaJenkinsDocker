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
@Table(name = "programas_estimulos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEstimulos.findAll", query = "SELECT p FROM ProgramasEstimulos p")
    , @NamedQuery(name = "ProgramasEstimulos.findByRegistro", query = "SELECT p FROM ProgramasEstimulos p WHERE p.registro = :registro")
    , @NamedQuery(name = "ProgramasEstimulos.findByTrabajador", query = "SELECT p FROM ProgramasEstimulos p WHERE p.trabajador = :trabajador")
    , @NamedQuery(name = "ProgramasEstimulos.findByDescripcion", query = "SELECT p FROM ProgramasEstimulos p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "ProgramasEstimulos.findByMonto", query = "SELECT p FROM ProgramasEstimulos p WHERE p.monto = :monto")
    , @NamedQuery(name = "ProgramasEstimulos.findByFechaInicio", query = "SELECT p FROM ProgramasEstimulos p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ProgramasEstimulos.findByFechaTermino", query = "SELECT p FROM ProgramasEstimulos p WHERE p.fechaTermino = :fechaTermino")})
public class ProgramasEstimulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trabajador")
    private int trabajador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private double monto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_termino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @JoinColumn(name = "tipo_programa", referencedColumnName = "tipo_programa")
    @ManyToOne(optional = false)
    private ProgramasEstimulosTipos tipoPrograma;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ProgramasEstimulos() {
    }

    public ProgramasEstimulos(Integer registro) {
        this.registro = registro;
    }

    public ProgramasEstimulos(Integer registro, int trabajador, String descripcion, double monto, Date fechaInicio, Date fechaTermino) {
        this.registro = registro;
        this.trabajador = trabajador;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(int trabajador) {
        this.trabajador = trabajador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public ProgramasEstimulosTipos getTipoPrograma() {
        return tipoPrograma;
    }

    public void setTipoPrograma(ProgramasEstimulosTipos tipoPrograma) {
        this.tipoPrograma = tipoPrograma;
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
        if (!(object instanceof ProgramasEstimulos)) {
            return false;
        }
        ProgramasEstimulos other = (ProgramasEstimulos) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulos[ registro=" + registro + " ]";
    }
    
}
