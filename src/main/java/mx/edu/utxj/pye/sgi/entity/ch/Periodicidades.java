/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "periodicidades", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periodicidades.findAll", query = "SELECT p FROM Periodicidades p")
    , @NamedQuery(name = "Periodicidades.findByPeriodicidad", query = "SELECT p FROM Periodicidades p WHERE p.periodicidad = :periodicidad")})
public class Periodicidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "periodicidad")
    private String periodicidad;

    public Periodicidades() {
    }

    public Periodicidades(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (periodicidad != null ? periodicidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Periodicidades)) {
            return false;
        }
        Periodicidades other = (Periodicidades) object;
        if ((this.periodicidad == null && other.periodicidad != null) || (this.periodicidad != null && !this.periodicidad.equals(other.periodicidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Periodicidades[ periodicidad=" + periodicidad + " ]";
    }
    
}
