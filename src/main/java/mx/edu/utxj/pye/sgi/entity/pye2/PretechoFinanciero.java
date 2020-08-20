/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "pretecho_financiero", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PretechoFinanciero.findAll", query = "SELECT p FROM PretechoFinanciero p")
    , @NamedQuery(name = "PretechoFinanciero.findByPretecho", query = "SELECT p FROM PretechoFinanciero p WHERE p.pretecho = :pretecho")
    , @NamedQuery(name = "PretechoFinanciero.findByArea", query = "SELECT p FROM PretechoFinanciero p WHERE p.area = :area")
    , @NamedQuery(name = "PretechoFinanciero.findByMonto", query = "SELECT p FROM PretechoFinanciero p WHERE p.monto = :monto")})
public class PretechoFinanciero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pretecho")
    private Integer pretecho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private double monto;
    @JoinColumn(name = "capitulo_tipo", referencedColumnName = "capitulo_tipo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CapitulosTipos capituloTipo;
    @JoinColumn(name = "ejercicio_fiscal", referencedColumnName = "ejercicio_fiscal")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EjerciciosFiscales ejercicioFiscal;

    public PretechoFinanciero() {
    }

    public PretechoFinanciero(Integer pretecho) {
        this.pretecho = pretecho;
    }

    public PretechoFinanciero(Integer pretecho, short area, double monto) {
        this.pretecho = pretecho;
        this.area = area;
        this.monto = monto;
    }

    public Integer getPretecho() {
        return pretecho;
    }

    public void setPretecho(Integer pretecho) {
        this.pretecho = pretecho;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public CapitulosTipos getCapituloTipo() {
        return capituloTipo;
    }

    public void setCapituloTipo(CapitulosTipos capituloTipo) {
        this.capituloTipo = capituloTipo;
    }

    public EjerciciosFiscales getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(EjerciciosFiscales ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pretecho != null ? pretecho.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PretechoFinanciero)) {
            return false;
        }
        PretechoFinanciero other = (PretechoFinanciero) object;
        if ((this.pretecho == null && other.pretecho != null) || (this.pretecho != null && !this.pretecho.equals(other.pretecho))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero[ pretecho=" + pretecho + " ]";
    }
    
}
