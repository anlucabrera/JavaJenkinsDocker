/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

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
@Table(name = "datos_contacto", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosContacto.findAll", query = "SELECT d FROM DatosContacto d")
    , @NamedQuery(name = "DatosContacto.findByDato", query = "SELECT d FROM DatosContacto d WHERE d.dato = :dato")
    , @NamedQuery(name = "DatosContacto.findByCelular", query = "SELECT d FROM DatosContacto d WHERE d.celular = :celular")
    , @NamedQuery(name = "DatosContacto.findByEmail", query = "SELECT d FROM DatosContacto d WHERE d.email = :email")})
public class DatosContacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dato")
    private Integer dato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "celular")
    private String celular;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "expediente", referencedColumnName = "expediente")
    @ManyToOne(optional = false)
    private ExpedientesTitulacion expediente;

    public DatosContacto() {
    }

    public DatosContacto(Integer dato) {
        this.dato = dato;
    }

    public DatosContacto(Integer dato, String celular, String email) {
        this.dato = dato;
        this.celular = celular;
        this.email = email;
    }

    public Integer getDato() {
        return dato;
    }

    public void setDato(Integer dato) {
        this.dato = dato;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ExpedientesTitulacion getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedientesTitulacion expediente) {
        this.expediente = expediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dato != null ? dato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosContacto)) {
            return false;
        }
        DatosContacto other = (DatosContacto) object;
        if ((this.dato == null && other.dato != null) || (this.dato != null && !this.dato.equals(other.dato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto[ dato=" + dato + " ]";
    }
    
}
