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
 * @author PLANEACION
 */
@Entity
@Table(name = "contacto_emergencias", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactoEmergencias.findAll", query = "SELECT c FROM ContactoEmergencias c")
    , @NamedQuery(name = "ContactoEmergencias.findByContacto", query = "SELECT c FROM ContactoEmergencias c WHERE c.contacto = :contacto")
    , @NamedQuery(name = "ContactoEmergencias.findByNombre", query = "SELECT c FROM ContactoEmergencias c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "ContactoEmergencias.findByMovil", query = "SELECT c FROM ContactoEmergencias c WHERE c.movil = :movil")
    , @NamedQuery(name = "ContactoEmergencias.findByTelefonoFijo", query = "SELECT c FROM ContactoEmergencias c WHERE c.telefonoFijo = :telefonoFijo")
    , @NamedQuery(name = "ContactoEmergencias.findByParentesco", query = "SELECT c FROM ContactoEmergencias c WHERE c.parentesco = :parentesco")})
public class ContactoEmergencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contacto")
    private Integer contacto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 250)
    @Column(name = "movil")
    private String movil;
    @Size(max = 250)
    @Column(name = "telefono_fijo")
    private String telefonoFijo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "parentesco")
    private String parentesco;
    @JoinColumn(name = "clave_Personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public ContactoEmergencias() {
    }

    public ContactoEmergencias(Integer contacto) {
        this.contacto = contacto;
    }

    public ContactoEmergencias(Integer contacto, String nombre, String parentesco) {
        this.contacto = contacto;
        this.nombre = nombre;
        this.parentesco = parentesco;
    }

    public Integer getContacto() {
        return contacto;
    }

    public void setContacto(Integer contacto) {
        this.contacto = contacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contacto != null ? contacto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactoEmergencias)) {
            return false;
        }
        ContactoEmergencias other = (ContactoEmergencias) object;
        if ((this.contacto == null && other.contacto != null) || (this.contacto != null && !this.contacto.equals(other.contacto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.ContactoEmergencias[ contacto=" + contacto + " ]";
    }
    
}
