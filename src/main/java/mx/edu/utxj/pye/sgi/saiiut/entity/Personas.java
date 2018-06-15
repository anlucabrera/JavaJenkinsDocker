/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "personas", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personas.findAll", query = "SELECT p FROM Personas p")
    , @NamedQuery(name = "Personas.findByCvePersona", query = "SELECT p FROM Personas p WHERE p.personasPK.cvePersona = :cvePersona")
    , @NamedQuery(name = "Personas.findByCveUniversidad", query = "SELECT p FROM Personas p WHERE p.personasPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Personas.findByNombre", query = "SELECT p FROM Personas p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Personas.findByApellidoPat", query = "SELECT p FROM Personas p WHERE p.apellidoPat = :apellidoPat")
    , @NamedQuery(name = "Personas.findByApellidoMat", query = "SELECT p FROM Personas p WHERE p.apellidoMat = :apellidoMat")
    , @NamedQuery(name = "Personas.findByCveEstadoCivil", query = "SELECT p FROM Personas p WHERE p.cveEstadoCivil = :cveEstadoCivil")
    , @NamedQuery(name = "Personas.findByRfc", query = "SELECT p FROM Personas p WHERE p.rfc = :rfc")
    , @NamedQuery(name = "Personas.findByFechaNacimiento", query = "SELECT p FROM Personas p WHERE p.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Personas.findByCurp", query = "SELECT p FROM Personas p WHERE p.curp = :curp")
    , @NamedQuery(name = "Personas.findBySexo", query = "SELECT p FROM Personas p WHERE p.sexo = :sexo")
    , @NamedQuery(name = "Personas.findByPeso", query = "SELECT p FROM Personas p WHERE p.peso = :peso")
    , @NamedQuery(name = "Personas.findByEstatura", query = "SELECT p FROM Personas p WHERE p.estatura = :estatura")
    , @NamedQuery(name = "Personas.findByFamDiabetico", query = "SELECT p FROM Personas p WHERE p.famDiabetico = :famDiabetico")
    , @NamedQuery(name = "Personas.findByFamHipertenso", query = "SELECT p FROM Personas p WHERE p.famHipertenso = :famHipertenso")
    , @NamedQuery(name = "Personas.findByFamCardiaco", query = "SELECT p FROM Personas p WHERE p.famCardiaco = :famCardiaco")
    , @NamedQuery(name = "Personas.findByLugarNacimiento", query = "SELECT p FROM Personas p WHERE p.lugarNacimiento = :lugarNacimiento")
    , @NamedQuery(name = "Personas.findByCveEstadoNacimiento", query = "SELECT p FROM Personas p WHERE p.cveEstadoNacimiento = :cveEstadoNacimiento")
    , @NamedQuery(name = "Personas.findByCveMunicipioNacimiento", query = "SELECT p FROM Personas p WHERE p.cveMunicipioNacimiento = :cveMunicipioNacimiento")
    , @NamedQuery(name = "Personas.findByCveLocalidadNacimiento", query = "SELECT p FROM Personas p WHERE p.cveLocalidadNacimiento = :cveLocalidadNacimiento")
    , @NamedQuery(name = "Personas.findByFamCancer", query = "SELECT p FROM Personas p WHERE p.famCancer = :famCancer")})
public class Personas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PersonasPK personasPK;
    @Size(max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "apellido_pat")
    private String apellidoPat;
    @Size(max = 40)
    @Column(name = "apellido_mat")
    private String apellidoMat;
    @Column(name = "cve_estado_civil")
    private Integer cveEstadoCivil;
    @Size(max = 15)
    @Column(name = "rfc")
    private String rfc;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Size(max = 18)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sexo")
    private Character sexo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso")
    private BigDecimal peso;
    @Column(name = "estatura")
    private BigDecimal estatura;
    @Column(name = "fam_diabetico")
    private Boolean famDiabetico;
    @Column(name = "fam_hipertenso")
    private Boolean famHipertenso;
    @Column(name = "fam_cardiaco")
    private Boolean famCardiaco;
    @Size(max = 80)
    @Column(name = "lugar_nacimiento")
    private String lugarNacimiento;
    @Column(name = "cve_estado_nacimiento")
    private Integer cveEstadoNacimiento;
    @Column(name = "cve_municipio_nacimiento")
    private Integer cveMunicipioNacimiento;
    @Column(name = "cve_localidad_nacimiento")
    private Integer cveLocalidadNacimiento;
    @Column(name = "fam_cancer")
    private Boolean famCancer;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personas")
    private Usuarios usuarios;

    public Personas() {
    }

    public Personas(PersonasPK personasPK) {
        this.personasPK = personasPK;
    }

    public Personas(PersonasPK personasPK, String apellidoPat, Character sexo) {
        this.personasPK = personasPK;
        this.apellidoPat = apellidoPat;
        this.sexo = sexo;
    }

    public Personas(int cvePersona, int cveUniversidad) {
        this.personasPK = new PersonasPK(cvePersona, cveUniversidad);
    }

    public PersonasPK getPersonasPK() {
        return personasPK;
    }

    public void setPersonasPK(PersonasPK personasPK) {
        this.personasPK = personasPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public Integer getCveEstadoCivil() {
        return cveEstadoCivil;
    }

    public void setCveEstadoCivil(Integer cveEstadoCivil) {
        this.cveEstadoCivil = cveEstadoCivil;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getEstatura() {
        return estatura;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura;
    }

    public Boolean getFamDiabetico() {
        return famDiabetico;
    }

    public void setFamDiabetico(Boolean famDiabetico) {
        this.famDiabetico = famDiabetico;
    }

    public Boolean getFamHipertenso() {
        return famHipertenso;
    }

    public void setFamHipertenso(Boolean famHipertenso) {
        this.famHipertenso = famHipertenso;
    }

    public Boolean getFamCardiaco() {
        return famCardiaco;
    }

    public void setFamCardiaco(Boolean famCardiaco) {
        this.famCardiaco = famCardiaco;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public Integer getCveEstadoNacimiento() {
        return cveEstadoNacimiento;
    }

    public void setCveEstadoNacimiento(Integer cveEstadoNacimiento) {
        this.cveEstadoNacimiento = cveEstadoNacimiento;
    }

    public Integer getCveMunicipioNacimiento() {
        return cveMunicipioNacimiento;
    }

    public void setCveMunicipioNacimiento(Integer cveMunicipioNacimiento) {
        this.cveMunicipioNacimiento = cveMunicipioNacimiento;
    }

    public Integer getCveLocalidadNacimiento() {
        return cveLocalidadNacimiento;
    }

    public void setCveLocalidadNacimiento(Integer cveLocalidadNacimiento) {
        this.cveLocalidadNacimiento = cveLocalidadNacimiento;
    }

    public Boolean getFamCancer() {
        return famCancer;
    }

    public void setFamCancer(Boolean famCancer) {
        this.famCancer = famCancer;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personasPK != null ? personasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personas)) {
            return false;
        }
        Personas other = (Personas) object;
        if ((this.personasPK == null && other.personasPK != null) || (this.personasPK != null && !this.personasPK.equals(other.personasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.ejb.saiiut.entity.Personas[ personasPK=" + personasPK + " ]";
    }
    
}
