/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "medio_comunicacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MedioComunicacion.findAll", query = "SELECT m FROM MedioComunicacion m")
    , @NamedQuery(name = "MedioComunicacion.findByPersona", query = "SELECT m FROM MedioComunicacion m WHERE m.persona = :persona")
    , @NamedQuery(name = "MedioComunicacion.findByTelefonoFijo", query = "SELECT m FROM MedioComunicacion m WHERE m.telefonoFijo = :telefonoFijo")
    , @NamedQuery(name = "MedioComunicacion.findByTelefonoMovil", query = "SELECT m FROM MedioComunicacion m WHERE m.telefonoMovil = :telefonoMovil")
    , @NamedQuery(name = "MedioComunicacion.findByEmail", query = "SELECT m FROM MedioComunicacion m WHERE m.email = :email")})
public class MedioComunicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "persona")
    private Integer persona;
    @Size(max = 25)
    @Column(name = "telefono_fijo")
    private String telefonoFijo;
    @Size(max = 25)
    @Column(name = "telefono_movil")
    private String telefonoMovil;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 250)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "persona", referencedColumnName = "idpersona", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Persona persona1;

    public MedioComunicacion() {
    }

    public MedioComunicacion(Integer persona) {
        this.persona = persona;
    }

    public Integer getPersona() {
        return persona;
    }

    public void setPersona(Integer persona) {
        this.persona = persona;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Persona getPersona1() {
        return persona1;
    }

    public void setPersona1(Persona persona1) {
        this.persona1 = persona1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (persona != null ? persona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedioComunicacion)) {
            return false;
        }
        MedioComunicacion other = (MedioComunicacion) object;
        if ((this.persona == null && other.persona != null) || (this.persona != null && !this.persona.equals(other.persona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion[ persona=" + persona + " ]";
    }
    
}
