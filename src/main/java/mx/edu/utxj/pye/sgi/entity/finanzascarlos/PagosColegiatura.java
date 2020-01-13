/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "pagos_colegiatura", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagosColegiatura.findAll", query = "SELECT p FROM PagosColegiatura p")
    , @NamedQuery(name = "PagosColegiatura.findByCveRegistro", query = "SELECT p FROM PagosColegiatura p WHERE p.cveRegistro = :cveRegistro")
    , @NamedQuery(name = "PagosColegiatura.findByPeriodo", query = "SELECT p FROM PagosColegiatura p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PagosColegiatura.findByCuatrimestre", query = "SELECT p FROM PagosColegiatura p WHERE p.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "PagosColegiatura.findByReferencia", query = "SELECT p FROM PagosColegiatura p WHERE p.referencia = :referencia")})
public class PagosColegiatura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cve_registro")
    private Integer cveRegistro;
    @Basic(optional = false)
    @Column(name = "periodo")
    private int periodo;
    @Column(name = "cuatrimestre")
    private Integer cuatrimestre;
    @Column(name = "referencia")
    private String referencia;
    @JoinColumn(name = "cve_registro", referencedColumnName = "cve_registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registro registro;

    public PagosColegiatura() {
    }

    public PagosColegiatura(Integer cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    public PagosColegiatura(Integer cveRegistro, int periodo) {
        this.cveRegistro = cveRegistro;
        this.periodo = periodo;
    }

    public Integer getCveRegistro() {
        return cveRegistro;
    }

    public void setCveRegistro(Integer cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Integer getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Integer cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveRegistro != null ? cveRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagosColegiatura)) {
            return false;
        }
        PagosColegiatura other = (PagosColegiatura) object;
        if ((this.cveRegistro == null && other.cveRegistro != null) || (this.cveRegistro != null && !this.cveRegistro.equals(other.cveRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.PagosColegiatura[ cveRegistro=" + cveRegistro + " ]";
    }
    
}
