/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

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
@Table(name = "estados", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estados.findAll", query = "SELECT e FROM Estados e")
    , @NamedQuery(name = "Estados.findByCveEstado", query = "SELECT e FROM Estados e WHERE e.cveEstado = :cveEstado")
    , @NamedQuery(name = "Estados.findByNombre", query = "SELECT e FROM Estados e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Estados.findByAbreviatura", query = "SELECT e FROM Estados e WHERE e.abreviatura = :abreviatura")})
public class Estados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_estado")
    private Integer cveEstado;
    @Size(max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;

    public Estados() {
    }

    public Estados(Integer cveEstado) {
        this.cveEstado = cveEstado;
    }

    public Integer getCveEstado() {
        return cveEstado;
    }

    public void setCveEstado(Integer cveEstado) {
        this.cveEstado = cveEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveEstado != null ? cveEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estados)) {
            return false;
        }
        Estados other = (Estados) object;
        if ((this.cveEstado == null && other.cveEstado != null) || (this.cveEstado != null && !this.cveEstado.equals(other.cveEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Estados[ cveEstado=" + cveEstado + " ]";
    }
    
}
