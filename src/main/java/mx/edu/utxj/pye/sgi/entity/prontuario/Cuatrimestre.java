/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuatrimestre", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuatrimestre.findAll", query = "SELECT c FROM Cuatrimestre c")
    , @NamedQuery(name = "Cuatrimestre.findByCuatrimestre", query = "SELECT c FROM Cuatrimestre c WHERE c.cuatrimestre = :cuatrimestre")})
public class Cuatrimestre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cuatrimestre")
    private Short cuatrimestre;

    public Cuatrimestre() {
    }

    public Cuatrimestre(Short cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Short getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(Short cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuatrimestre != null ? cuatrimestre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuatrimestre)) {
            return false;
        }
        Cuatrimestre other = (Cuatrimestre) object;
        if ((this.cuatrimestre == null && other.cuatrimestre != null) || (this.cuatrimestre != null && !this.cuatrimestre.equals(other.cuatrimestre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Cuatrimestre[ cuatrimestre=" + cuatrimestre + " ]";
    }
    
}
