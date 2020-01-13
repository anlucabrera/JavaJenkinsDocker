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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "pago", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p")
    , @NamedQuery(name = "Pago.findByPago", query = "SELECT p FROM Pago p WHERE p.pago = :pago")})
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pago")
    private Integer pago;
    @JoinColumn(name = "curp", referencedColumnName = "curp")
    @ManyToOne(optional = false)
    private Personafinanzas curp;
    @JoinColumn(name = "cve_registro", referencedColumnName = "cve_registro")
    @ManyToOne(optional = false)
    private Registro cveRegistro;

    public Pago() {
    }

    public Pago(Integer pago) {
        this.pago = pago;
    }

    public Integer getPago() {
        return pago;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }

    public Personafinanzas getCurp() {
        return curp;
    }

    public void setCurp(Personafinanzas curp) {
        this.curp = curp;
    }

    public Registro getCveRegistro() {
        return cveRegistro;
    }

    public void setCveRegistro(Registro cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pago != null ? pago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.pago == null && other.pago != null) || (this.pago != null && !this.pago.equals(other.pago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Pago[ pago=" + pago + " ]";
    }
    
}
