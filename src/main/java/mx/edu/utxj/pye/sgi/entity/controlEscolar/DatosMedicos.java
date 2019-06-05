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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "datos_medicos", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosMedicos.findAll", query = "SELECT d FROM DatosMedicos d")
    , @NamedQuery(name = "DatosMedicos.findByCvePersona", query = "SELECT d FROM DatosMedicos d WHERE d.cvePersona = :cvePersona")
    , @NamedQuery(name = "DatosMedicos.findByPeso", query = "SELECT d FROM DatosMedicos d WHERE d.peso = :peso")
    , @NamedQuery(name = "DatosMedicos.findByEstatura", query = "SELECT d FROM DatosMedicos d WHERE d.estatura = :estatura")
    , @NamedQuery(name = "DatosMedicos.findByFDiabetes", query = "SELECT d FROM DatosMedicos d WHERE d.fDiabetes = :fDiabetes")
    , @NamedQuery(name = "DatosMedicos.findByFHipertenso", query = "SELECT d FROM DatosMedicos d WHERE d.fHipertenso = :fHipertenso")
    , @NamedQuery(name = "DatosMedicos.findByFCardiaco", query = "SELECT d FROM DatosMedicos d WHERE d.fCardiaco = :fCardiaco")
    , @NamedQuery(name = "DatosMedicos.findByFCancer", query = "SELECT d FROM DatosMedicos d WHERE d.fCancer = :fCancer")
    , @NamedQuery(name = "DatosMedicos.findByNssVigente", query = "SELECT d FROM DatosMedicos d WHERE d.nssVigente = :nssVigente")
    , @NamedQuery(name = "DatosMedicos.findByNss", query = "SELECT d FROM DatosMedicos d WHERE d.nss = :nss")})
public class DatosMedicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_persona")
    private Integer cvePersona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private double peso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatura")
    private double estatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f_diabetes")
    private boolean fDiabetes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f_hipertenso")
    private boolean fHipertenso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f_cardiaco")
    private boolean fCardiaco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f_cancer")
    private boolean fCancer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nss_vigente")
    private boolean nssVigente;
    @Size(max = 20)
    @Column(name = "nss")
    private String nss;
    @JoinColumn(name = "cve_persona", referencedColumnName = "idpersona", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Persona persona;
    @JoinColumn(name = "cve_discapacidad", referencedColumnName = "id_tipo_discapacidad")
    @ManyToOne(optional = false)
    private TipoDiscapacidad cveDiscapacidad;
    @JoinColumn(name = "cve_tipo_sangre", referencedColumnName = "id_tipo_sangre")
    @ManyToOne(optional = false)
    private TipoSangre cveTipoSangre;

    public DatosMedicos() {
    }

    public DatosMedicos(Integer cvePersona) {
        this.cvePersona = cvePersona;
    }

    public DatosMedicos(Integer cvePersona, double peso, double estatura, boolean fDiabetes, boolean fHipertenso, boolean fCardiaco, boolean fCancer, boolean nssVigente) {
        this.cvePersona = cvePersona;
        this.peso = peso;
        this.estatura = estatura;
        this.fDiabetes = fDiabetes;
        this.fHipertenso = fHipertenso;
        this.fCardiaco = fCardiaco;
        this.fCancer = fCancer;
        this.nssVigente = nssVigente;
    }

    public Integer getCvePersona() {
        return cvePersona;
    }

    public void setCvePersona(Integer cvePersona) {
        this.cvePersona = cvePersona;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public boolean getFDiabetes() {
        return fDiabetes;
    }

    public void setFDiabetes(boolean fDiabetes) {
        this.fDiabetes = fDiabetes;
    }

    public boolean getFHipertenso() {
        return fHipertenso;
    }

    public void setFHipertenso(boolean fHipertenso) {
        this.fHipertenso = fHipertenso;
    }

    public boolean getFCardiaco() {
        return fCardiaco;
    }

    public void setFCardiaco(boolean fCardiaco) {
        this.fCardiaco = fCardiaco;
    }

    public boolean getFCancer() {
        return fCancer;
    }

    public void setFCancer(boolean fCancer) {
        this.fCancer = fCancer;
    }

    public boolean getNssVigente() {
        return nssVigente;
    }

    public void setNssVigente(boolean nssVigente) {
        this.nssVigente = nssVigente;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public TipoDiscapacidad getCveDiscapacidad() {
        return cveDiscapacidad;
    }

    public void setCveDiscapacidad(TipoDiscapacidad cveDiscapacidad) {
        this.cveDiscapacidad = cveDiscapacidad;
    }

    public TipoSangre getCveTipoSangre() {
        return cveTipoSangre;
    }

    public void setCveTipoSangre(TipoSangre cveTipoSangre) {
        this.cveTipoSangre = cveTipoSangre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cvePersona != null ? cvePersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosMedicos)) {
            return false;
        }
        DatosMedicos other = (DatosMedicos) object;
        if ((this.cvePersona == null && other.cvePersona != null) || (this.cvePersona != null && !this.cvePersona.equals(other.cvePersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos[ cvePersona=" + cvePersona + " ]";
    }
    
}
