/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "bolsa_trabajo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BolsaTrabajo.findAll", query = "SELECT b FROM BolsaTrabajo b")
    , @NamedQuery(name = "BolsaTrabajo.findByRegistro", query = "SELECT b FROM BolsaTrabajo b WHERE b.registro = :registro")
    , @NamedQuery(name = "BolsaTrabajo.findByBolsatrab", query = "SELECT b FROM BolsaTrabajo b WHERE b.bolsatrab = :bolsatrab")
    , @NamedQuery(name = "BolsaTrabajo.findByFecha", query = "SELECT b FROM BolsaTrabajo b WHERE b.fecha = :fecha")
    , @NamedQuery(name = "BolsaTrabajo.findByPuestoOfertado", query = "SELECT b FROM BolsaTrabajo b WHERE b.puestoOfertado = :puestoOfertado")
    , @NamedQuery(name = "BolsaTrabajo.findByPlazas", query = "SELECT b FROM BolsaTrabajo b WHERE b.plazas = :plazas")})
public class BolsaTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bolsatrab")
    private int bolsatrab;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "puesto_ofertado")
    private String puestoOfertado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plazas")
    private short plazas;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false)
    private OrganismosVinculados empresa;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bolsatrabent")
    private List<BolsaTrabajoEntrevistas> bolsaTrabajoEntrevistasList;

    public BolsaTrabajo() {
    }

    public BolsaTrabajo(Integer registro) {
        this.registro = registro;
    }

    public BolsaTrabajo(Integer registro, int bolsatrab, Date fecha, String puestoOfertado, short plazas) {
        this.registro = registro;
        this.bolsatrab = bolsatrab;
        this.fecha = fecha;
        this.puestoOfertado = puestoOfertado;
        this.plazas = plazas;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getBolsatrab() {
        return bolsatrab;
    }

    public void setBolsatrab(int bolsatrab) {
        this.bolsatrab = bolsatrab;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPuestoOfertado() {
        return puestoOfertado;
    }

    public void setPuestoOfertado(String puestoOfertado) {
        this.puestoOfertado = puestoOfertado;
    }

    public short getPlazas() {
        return plazas;
    }

    public void setPlazas(short plazas) {
        this.plazas = plazas;
    }

    public OrganismosVinculados getEmpresa() {
        return empresa;
    }

    public void setEmpresa(OrganismosVinculados empresa) {
        this.empresa = empresa;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @XmlTransient
    public List<BolsaTrabajoEntrevistas> getBolsaTrabajoEntrevistasList() {
        return bolsaTrabajoEntrevistasList;
    }

    public void setBolsaTrabajoEntrevistasList(List<BolsaTrabajoEntrevistas> bolsaTrabajoEntrevistasList) {
        this.bolsaTrabajoEntrevistasList = bolsaTrabajoEntrevistasList;
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
        if (!(object instanceof BolsaTrabajo)) {
            return false;
        }
        BolsaTrabajo other = (BolsaTrabajo) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo[ registro=" + registro + " ]";
    }
    
}
