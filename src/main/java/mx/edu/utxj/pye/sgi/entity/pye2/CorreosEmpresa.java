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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "correos_empresa", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CorreosEmpresa.findAll", query = "SELECT c FROM CorreosEmpresa c")
    , @NamedQuery(name = "CorreosEmpresa.findByCorreoEmpresa", query = "SELECT c FROM CorreosEmpresa c WHERE c.correoEmpresa = :correoEmpresa")
    , @NamedQuery(name = "CorreosEmpresa.findByEmail", query = "SELECT c FROM CorreosEmpresa c WHERE c.email = :email")})
public class CorreosEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "correo_empresa")
    private Integer correoEmpresa;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false)
    private OrganismosVinculados empresa;

    public CorreosEmpresa() {
    }

    public CorreosEmpresa(Integer correoEmpresa) {
        this.correoEmpresa = correoEmpresa;
    }

    public CorreosEmpresa(Integer correoEmpresa, String email) {
        this.correoEmpresa = correoEmpresa;
        this.email = email;
    }

    public Integer getCorreoEmpresa() {
        return correoEmpresa;
    }

    public void setCorreoEmpresa(Integer correoEmpresa) {
        this.correoEmpresa = correoEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OrganismosVinculados getEmpresa() {
        return empresa;
    }

    public void setEmpresa(OrganismosVinculados empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (correoEmpresa != null ? correoEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorreosEmpresa)) {
            return false;
        }
        CorreosEmpresa other = (CorreosEmpresa) object;
        if ((this.correoEmpresa == null && other.correoEmpresa != null) || (this.correoEmpresa != null && !this.correoEmpresa.equals(other.correoEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CorreosEmpresa[ correoEmpresa=" + correoEmpresa + " ]";
    }
    
}
