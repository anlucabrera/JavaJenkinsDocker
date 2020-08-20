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
@Table(name = "ingresos_propios_captados", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngresosPropiosCaptados.findAll", query = "SELECT i FROM IngresosPropiosCaptados i")
    , @NamedQuery(name = "IngresosPropiosCaptados.findByRegistro", query = "SELECT i FROM IngresosPropiosCaptados i WHERE i.registro = :registro")
    , @NamedQuery(name = "IngresosPropiosCaptados.findByPeriodoEscolar", query = "SELECT i FROM IngresosPropiosCaptados i WHERE i.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "IngresosPropiosCaptados.findByConceptoIngresosCaptados", query = "SELECT i FROM IngresosPropiosCaptados i WHERE i.conceptoIngresosCaptados = :conceptoIngresosCaptados")
    , @NamedQuery(name = "IngresosPropiosCaptados.findByFechaIngreso", query = "SELECT i FROM IngresosPropiosCaptados i WHERE i.fechaIngreso = :fechaIngreso")
    , @NamedQuery(name = "IngresosPropiosCaptados.findByMonto", query = "SELECT i FROM IngresosPropiosCaptados i WHERE i.monto = :monto")
    , @NamedQuery(name = "IngresosPropiosCaptados.findByDescripcion", query = "SELECT i FROM IngresosPropiosCaptados i WHERE i.descripcion = :descripcion")})
public class IngresosPropiosCaptados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "concepto_ingresos_captados")
    private String conceptoIngresosCaptados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private double monto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public IngresosPropiosCaptados() {
    }

    public IngresosPropiosCaptados(Integer registro) {
        this.registro = registro;
    }

    public IngresosPropiosCaptados(Integer registro, int periodoEscolar, String conceptoIngresosCaptados, Date fechaIngreso, double monto, String descripcion) {
        this.registro = registro;
        this.periodoEscolar = periodoEscolar;
        this.conceptoIngresosCaptados = conceptoIngresosCaptados;
        this.fechaIngreso = fechaIngreso;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public String getConceptoIngresosCaptados() {
        return conceptoIngresosCaptados;
    }

    public void setConceptoIngresosCaptados(String conceptoIngresosCaptados) {
        this.conceptoIngresosCaptados = conceptoIngresosCaptados;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof IngresosPropiosCaptados)) {
            return false;
        }
        IngresosPropiosCaptados other = (IngresosPropiosCaptados) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.IngresosPropiosCaptados[ registro=" + registro + " ]";
    }
    
}
