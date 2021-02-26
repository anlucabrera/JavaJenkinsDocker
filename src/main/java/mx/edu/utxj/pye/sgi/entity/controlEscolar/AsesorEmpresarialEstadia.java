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
@Table(name = "asesor_empresarial_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsesorEmpresarialEstadia.findAll", query = "SELECT a FROM AsesorEmpresarialEstadia a")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByAsesorEmpresarial", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.asesorEmpresarial = :asesorEmpresarial")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByApellidoPaterno", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByApellidoMaterno", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByNombre", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByPuesto", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.puesto = :puesto")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByTelefono", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.telefono = :telefono")
    , @NamedQuery(name = "AsesorEmpresarialEstadia.findByEmail", query = "SELECT a FROM AsesorEmpresarialEstadia a WHERE a.email = :email")})
public class AsesorEmpresarialEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "asesor_empresarial")
    private Integer asesorEmpresarial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "puesto")
    private String puesto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "telefono")
    private String telefono;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "seguimiento", referencedColumnName = "seguimiento")
    @ManyToOne(optional = false)
    private SeguimientoEstadiaEstudiante seguimiento;

    public AsesorEmpresarialEstadia() {
    }

    public AsesorEmpresarialEstadia(Integer asesorEmpresarial) {
        this.asesorEmpresarial = asesorEmpresarial;
    }

    public AsesorEmpresarialEstadia(Integer asesorEmpresarial, String apellidoPaterno, String apellidoMaterno, String nombre, String puesto, String telefono, String email) {
        this.asesorEmpresarial = asesorEmpresarial;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombre = nombre;
        this.puesto = puesto;
        this.telefono = telefono;
        this.email = email;
    }

    public Integer getAsesorEmpresarial() {
        return asesorEmpresarial;
    }

    public void setAsesorEmpresarial(Integer asesorEmpresarial) {
        this.asesorEmpresarial = asesorEmpresarial;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SeguimientoEstadiaEstudiante getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(SeguimientoEstadiaEstudiante seguimiento) {
        this.seguimiento = seguimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asesorEmpresarial != null ? asesorEmpresarial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsesorEmpresarialEstadia)) {
            return false;
        }
        AsesorEmpresarialEstadia other = (AsesorEmpresarialEstadia) object;
        if ((this.asesorEmpresarial == null && other.asesorEmpresarial != null) || (this.asesorEmpresarial != null && !this.asesorEmpresarial.equals(other.asesorEmpresarial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia[ asesorEmpresarial=" + asesorEmpresarial + " ]";
    }
    
}
