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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "ejesprocedu", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ejesprocedu.findAll", query = "SELECT e FROM Ejesprocedu e")
    , @NamedQuery(name = "Ejesprocedu.findByEjeprocedu", query = "SELECT e FROM Ejesprocedu e WHERE e.ejeprocedu = :ejeprocedu")
    , @NamedQuery(name = "Ejesprocedu.findByNombre", query = "SELECT e FROM Ejesprocedu e WHERE e.nombre = :nombre")})
public class Ejesprocedu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ejeprocedu")
    private Integer ejeprocedu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;

    public Ejesprocedu() {
    }

    public Ejesprocedu(Integer ejeprocedu) {
        this.ejeprocedu = ejeprocedu;
    }

    public Ejesprocedu(Integer ejeprocedu, String nombre) {
        this.ejeprocedu = ejeprocedu;
        this.nombre = nombre;
    }

    public Integer getEjeprocedu() {
        return ejeprocedu;
    }

    public void setEjeprocedu(Integer ejeprocedu) {
        this.ejeprocedu = ejeprocedu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ejeprocedu != null ? ejeprocedu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ejesprocedu)) {
            return false;
        }
        Ejesprocedu other = (Ejesprocedu) object;
        if ((this.ejeprocedu == null && other.ejeprocedu != null) || (this.ejeprocedu != null && !this.ejeprocedu.equals(other.ejeprocedu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Ejesprocedu[ ejeprocedu=" + ejeprocedu + " ]";
    }
    
}
