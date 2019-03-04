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
@Table(name = "contactos_empresa", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactosEmpresa.findAll", query = "SELECT c FROM ContactosEmpresa c")
    , @NamedQuery(name = "ContactosEmpresa.findByContactoEmpresa", query = "SELECT c FROM ContactosEmpresa c WHERE c.contactoEmpresa = :contactoEmpresa")
    , @NamedQuery(name = "ContactosEmpresa.findByRepresentante", query = "SELECT c FROM ContactosEmpresa c WHERE c.representante = :representante")
    , @NamedQuery(name = "ContactosEmpresa.findByCargoRepresentante", query = "SELECT c FROM ContactosEmpresa c WHERE c.cargoRepresentante = :cargoRepresentante")})
public class ContactosEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contacto_empresa")
    private Integer contactoEmpresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "representante")
    private String representante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "cargo_representante")
    private String cargoRepresentante;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false)
    private OrganismosVinculados empresa;

    public ContactosEmpresa() {
    }

    public ContactosEmpresa(Integer contactoEmpresa) {
        this.contactoEmpresa = contactoEmpresa;
    }

    public ContactosEmpresa(Integer contactoEmpresa, String representante, String cargoRepresentante) {
        this.contactoEmpresa = contactoEmpresa;
        this.representante = representante;
        this.cargoRepresentante = cargoRepresentante;
    }

    public Integer getContactoEmpresa() {
        return contactoEmpresa;
    }

    public void setContactoEmpresa(Integer contactoEmpresa) {
        this.contactoEmpresa = contactoEmpresa;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getCargoRepresentante() {
        return cargoRepresentante;
    }

    public void setCargoRepresentante(String cargoRepresentante) {
        this.cargoRepresentante = cargoRepresentante;
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
        hash += (contactoEmpresa != null ? contactoEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactosEmpresa)) {
            return false;
        }
        ContactosEmpresa other = (ContactosEmpresa) object;
        if ((this.contactoEmpresa == null && other.contactoEmpresa != null) || (this.contactoEmpresa != null && !this.contactoEmpresa.equals(other.contactoEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ContactosEmpresa[ contactoEmpresa=" + contactoEmpresa + " ]";
    }
    
}
