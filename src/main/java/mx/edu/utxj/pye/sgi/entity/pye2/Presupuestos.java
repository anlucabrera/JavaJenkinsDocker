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
@Table(name = "presupuestos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Presupuestos.findAll", query = "SELECT p FROM Presupuestos p")
    , @NamedQuery(name = "Presupuestos.findByRegistro", query = "SELECT p FROM Presupuestos p WHERE p.registro = :registro")
    , @NamedQuery(name = "Presupuestos.findByPresupuestoOperacion", query = "SELECT p FROM Presupuestos p WHERE p.presupuestoOperacion = :presupuestoOperacion")
    , @NamedQuery(name = "Presupuestos.findByPresupuestoTipo", query = "SELECT p FROM Presupuestos p WHERE p.presupuestoTipo = :presupuestoTipo")
    , @NamedQuery(name = "Presupuestos.findByMonto", query = "SELECT p FROM Presupuestos p WHERE p.monto = :monto")
    , @NamedQuery(name = "Presupuestos.findByFechaAplicacion", query = "SELECT p FROM Presupuestos p WHERE p.fechaAplicacion = :fechaAplicacion")})
public class Presupuestos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "presupuesto_operacion")
    private String presupuestoOperacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "presupuesto_tipo")
    private String presupuestoTipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private double monto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;
    @JoinColumn(name = "capitulo_tipo", referencedColumnName = "capitulo_tipo")
    @ManyToOne(optional = false)
    private CapitulosTipos capituloTipo;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public Presupuestos() {
    }

    public Presupuestos(Integer registro) {
        this.registro = registro;
    }

    public Presupuestos(Integer registro, String presupuestoOperacion, String presupuestoTipo, double monto, Date fechaAplicacion) {
        this.registro = registro;
        this.presupuestoOperacion = presupuestoOperacion;
        this.presupuestoTipo = presupuestoTipo;
        this.monto = monto;
        this.fechaAplicacion = fechaAplicacion;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getPresupuestoOperacion() {
        return presupuestoOperacion;
    }

    public void setPresupuestoOperacion(String presupuestoOperacion) {
        this.presupuestoOperacion = presupuestoOperacion;
    }

    public String getPresupuestoTipo() {
        return presupuestoTipo;
    }

    public void setPresupuestoTipo(String presupuestoTipo) {
        this.presupuestoTipo = presupuestoTipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public CapitulosTipos getCapituloTipo() {
        return capituloTipo;
    }

    public void setCapituloTipo(CapitulosTipos capituloTipo) {
        this.capituloTipo = capituloTipo;
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
        if (!(object instanceof Presupuestos)) {
            return false;
        }
        Presupuestos other = (Presupuestos) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Presupuestos[ registro=" + registro + " ]";
    }
    
}
