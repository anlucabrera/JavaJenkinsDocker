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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "ciclosescolares_generaciones", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CiclosescolaresGeneraciones.findAll", query = "SELECT c FROM CiclosescolaresGeneraciones c")
    , @NamedQuery(name = "CiclosescolaresGeneraciones.findByCeg", query = "SELECT c FROM CiclosescolaresGeneraciones c WHERE c.ceg = :ceg")})
public class CiclosescolaresGeneraciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ceg")
    private Integer ceg;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false)
    private CiclosEscolares ciclo;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion")
    @ManyToOne(optional = false)
    private Generaciones generacion;

    public CiclosescolaresGeneraciones() {
    }

    public CiclosescolaresGeneraciones(Integer ceg) {
        this.ceg = ceg;
    }

    public Integer getCeg() {
        return ceg;
    }

    public void setCeg(Integer ceg) {
        this.ceg = ceg;
    }

    public CiclosEscolares getCiclo() {
        return ciclo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
    }

    public Generaciones getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ceg != null ? ceg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CiclosescolaresGeneraciones)) {
            return false;
        }
        CiclosescolaresGeneraciones other = (CiclosescolaresGeneraciones) object;
        if ((this.ceg == null && other.ceg != null) || (this.ceg != null && !this.ceg.equals(other.ceg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CiclosescolaresGeneraciones[ ceg=" + ceg + " ]";
    }
    
}
