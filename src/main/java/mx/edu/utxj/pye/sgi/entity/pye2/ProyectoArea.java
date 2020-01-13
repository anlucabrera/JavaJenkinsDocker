/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "proyecto_area", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProyectoArea.findAll", query = "SELECT p FROM ProyectoArea p")
    , @NamedQuery(name = "ProyectoArea.findByProyecto", query = "SELECT p FROM ProyectoArea p WHERE p.proyectoAreaPK.proyecto = :proyecto")
    , @NamedQuery(name = "ProyectoArea.findByArea", query = "SELECT p FROM ProyectoArea p WHERE p.proyectoAreaPK.area = :area")})
public class ProyectoArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProyectoAreaPK proyectoAreaPK;
    @JoinColumn(name = "proyecto", referencedColumnName = "proyecto", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Proyectos proyectos;

    public ProyectoArea() {
    }

    public ProyectoArea(ProyectoAreaPK proyectoAreaPK) {
        this.proyectoAreaPK = proyectoAreaPK;
    }

    public ProyectoArea(int proyecto, short area) {
        this.proyectoAreaPK = new ProyectoAreaPK(proyecto, area);
    }

    public ProyectoAreaPK getProyectoAreaPK() {
        return proyectoAreaPK;
    }

    public void setProyectoAreaPK(ProyectoAreaPK proyectoAreaPK) {
        this.proyectoAreaPK = proyectoAreaPK;
    }

    public Proyectos getProyectos() {
        return proyectos;
    }

    public void setProyectos(Proyectos proyectos) {
        this.proyectos = proyectos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proyectoAreaPK != null ? proyectoAreaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProyectoArea)) {
            return false;
        }
        ProyectoArea other = (ProyectoArea) object;
        if ((this.proyectoAreaPK == null && other.proyectoAreaPK != null) || (this.proyectoAreaPK != null && !this.proyectoAreaPK.equals(other.proyectoAreaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProyectoArea[ proyectoAreaPK=" + proyectoAreaPK + " ]";
    }
    
}
