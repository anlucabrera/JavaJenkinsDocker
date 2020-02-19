/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "personafinanzas", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personafinanzas.findAll", query = "SELECT p FROM Personafinanzas p")
    , @NamedQuery(name = "Personafinanzas.findByCurp", query = "SELECT p FROM Personafinanzas p WHERE p.curp = :curp")
    , @NamedQuery(name = "Personafinanzas.findByNombre", query = "SELECT p FROM Personafinanzas p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Personafinanzas.findByApellidoPaterno", query = "SELECT p FROM Personafinanzas p WHERE p.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "Personafinanzas.findByApellidoMaterno", query = "SELECT p FROM Personafinanzas p WHERE p.apellidoMaterno = :apellidoMaterno")})
public class Personafinanzas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curp")
    private List<Pago> pagoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curp")
    private List<AlumnoFinanzas> alumnoFinanzasList;

    public Personafinanzas() {
    }

    public Personafinanzas(String curp) {
        this.curp = curp;
    }

    public Personafinanzas(String curp, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.curp = curp;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    @XmlTransient
    public List<AlumnoFinanzas> getAlumnoFinanzasList() {
        return alumnoFinanzasList;
    }

    public void setAlumnoFinanzasList(List<AlumnoFinanzas> alumnoFinanzasList) {
        this.alumnoFinanzasList = alumnoFinanzasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (curp != null ? curp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personafinanzas)) {
            return false;
        }
        Personafinanzas other = (Personafinanzas) object;
        if ((this.curp == null && other.curp != null) || (this.curp != null && !this.curp.equals(other.curp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Personafinanzas[ curp=" + curp + " ]";
    }
    
}
