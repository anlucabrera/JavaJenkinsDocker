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
@Table(name = "telefonos_empresa", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TelefonosEmpresa.findAll", query = "SELECT t FROM TelefonosEmpresa t")
    , @NamedQuery(name = "TelefonosEmpresa.findByTelefonoEmpresa", query = "SELECT t FROM TelefonosEmpresa t WHERE t.telefonoEmpresa = :telefonoEmpresa")
    , @NamedQuery(name = "TelefonosEmpresa.findByTelefonoPrincipal", query = "SELECT t FROM TelefonosEmpresa t WHERE t.telefonoPrincipal = :telefonoPrincipal")})
public class TelefonosEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "telefono_empresa")
    private Integer telefonoEmpresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "telefono_principal")
    private String telefonoPrincipal;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false)
    private OrganismosVinculados empresa;

    public TelefonosEmpresa() {
    }

    public TelefonosEmpresa(Integer telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }

    public TelefonosEmpresa(Integer telefonoEmpresa, String telefonoPrincipal) {
        this.telefonoEmpresa = telefonoEmpresa;
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public Integer getTelefonoEmpresa() {
        return telefonoEmpresa;
    }

    public void setTelefonoEmpresa(Integer telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
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
        hash += (telefonoEmpresa != null ? telefonoEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TelefonosEmpresa)) {
            return false;
        }
        TelefonosEmpresa other = (TelefonosEmpresa) object;
        if ((this.telefonoEmpresa == null && other.telefonoEmpresa != null) || (this.telefonoEmpresa != null && !this.telefonoEmpresa.equals(other.telefonoEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.TelefonosEmpresa[ telefonoEmpresa=" + telefonoEmpresa + " ]";
    }
    
}
