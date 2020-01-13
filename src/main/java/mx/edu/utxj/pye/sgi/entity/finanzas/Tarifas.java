/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "tarifas", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarifas.findAll", query = "SELECT t FROM Tarifas t")
    , @NamedQuery(name = "Tarifas.findByTarifa", query = "SELECT t FROM Tarifas t WHERE t.tarifa = :tarifa")
    , @NamedQuery(name = "Tarifas.findByTipo", query = "SELECT t FROM Tarifas t WHERE t.tipo = :tipo")
    , @NamedQuery(name = "Tarifas.findByFechaAplicacion", query = "SELECT t FROM Tarifas t WHERE t.fechaAplicacion = :fechaAplicacion")
    , @NamedQuery(name = "Tarifas.findByFechaCancelacion", query = "SELECT t FROM Tarifas t WHERE t.fechaCancelacion = :fechaCancelacion")})
public class Tarifas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tarifa")
    private Integer tarifa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;
    @Column(name = "fecha_cancelacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCancelacion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tarifas")
    private TarifasPorKilometro tarifasPorKilometro;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tarifas")
    private TarifasViajes tarifasViajes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tarifas")
    private TarifasPorZona tarifasPorZona;
    @OneToMany(mappedBy = "tarifaViaje")
    private List<ComisionAvisos> comisionAvisosList;
    @OneToMany(mappedBy = "tarifaViaticos")
    private List<ComisionAvisos> comisionAvisosList1;

    public Tarifas() {
    }

    public Tarifas(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public Tarifas(Integer tarifa, String tipo, Date fechaAplicacion) {
        this.tarifa = tarifa;
        this.tipo = tipo;
        this.fechaAplicacion = fechaAplicacion;
    }

    public Integer getTarifa() {
        return tarifa;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public TarifasPorKilometro getTarifasPorKilometro() {
        return tarifasPorKilometro;
    }

    public void setTarifasPorKilometro(TarifasPorKilometro tarifasPorKilometro) {
        this.tarifasPorKilometro = tarifasPorKilometro;
    }

    public TarifasViajes getTarifasViajes() {
        return tarifasViajes;
    }

    public void setTarifasViajes(TarifasViajes tarifasViajes) {
        this.tarifasViajes = tarifasViajes;
    }

    public TarifasPorZona getTarifasPorZona() {
        return tarifasPorZona;
    }

    public void setTarifasPorZona(TarifasPorZona tarifasPorZona) {
        this.tarifasPorZona = tarifasPorZona;
    }

    @XmlTransient
    public List<ComisionAvisos> getComisionAvisosList() {
        return comisionAvisosList;
    }

    public void setComisionAvisosList(List<ComisionAvisos> comisionAvisosList) {
        this.comisionAvisosList = comisionAvisosList;
    }

    @XmlTransient
    public List<ComisionAvisos> getComisionAvisosList1() {
        return comisionAvisosList1;
    }

    public void setComisionAvisosList1(List<ComisionAvisos> comisionAvisosList1) {
        this.comisionAvisosList1 = comisionAvisosList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarifa != null ? tarifa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarifas)) {
            return false;
        }
        Tarifas other = (Tarifas) object;
        if ((this.tarifa == null && other.tarifa != null) || (this.tarifa != null && !this.tarifa.equals(other.tarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tarifas{" +
                "tarifa=" + tarifa +
                ", tipo='" + tipo + '\'' +
                ", fechaAplicacion=" + fechaAplicacion +
                ", fechaCancelacion=" + fechaCancelacion +
                ", tarifasPorKilometro=" + tarifasPorKilometro +
                ", tarifasViajes=" + tarifasViajes +
                ", tarifasPorZona=" + tarifasPorZona +
                '}';
    }
}
