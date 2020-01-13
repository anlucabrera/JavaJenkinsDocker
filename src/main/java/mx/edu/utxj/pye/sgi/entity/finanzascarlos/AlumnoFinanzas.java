/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;


import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "alumno_finanzas", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlumnoFinanzas.findAll", query = "SELECT a FROM AlumnoFinanzas a")
    , @NamedQuery(name = "AlumnoFinanzas.findByMatricula", query = "SELECT a FROM AlumnoFinanzas a WHERE a.alumnoFinanzasPK.matricula = :matricula")
    , @NamedQuery(name = "AlumnoFinanzas.findByPeriodo", query = "SELECT a FROM AlumnoFinanzas a WHERE a.alumnoFinanzasPK.periodo = :periodo")
    , @NamedQuery(name = "AlumnoFinanzas.findBySiglas", query = "SELECT a FROM AlumnoFinanzas a WHERE a.siglas = :siglas")
    , @NamedQuery(name = "AlumnoFinanzas.findByValcartanoadueudoTSU", query = "SELECT a FROM AlumnoFinanzas a WHERE a.valcartanoadueudoTSU = :valcartanoadueudoTSU")
    , @NamedQuery(name = "AlumnoFinanzas.findByValcartanoadedudoIng", query = "SELECT a FROM AlumnoFinanzas a WHERE a.valcartanoadedudoIng = :valcartanoadedudoIng")})
public class AlumnoFinanzas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlumnoFinanzasPK alumnoFinanzasPK;
    @Basic(optional = false)
    @Column(name = "siglas")
    private String siglas;
    @Column(name = "val_carta_no_adueudo_TSU")
    private Boolean valcartanoadueudoTSU;
    @Column(name = "val_carta_no_adedudo_Ing")
    private Boolean valcartanoadedudoIng;
    @OneToMany(mappedBy = "alumnoFinanzas")
    private List<Aclaracion> aclaracionList;
    @JoinColumn(name = "curp", referencedColumnName = "curp")
    @ManyToOne(optional = false)
    private Personafinanzas curp;
    @OneToMany(mappedBy = "alumnoFinanzas")
    private List<Registro> registroList;

    public AlumnoFinanzas() {
    }

    public AlumnoFinanzas(AlumnoFinanzasPK alumnoFinanzasPK) {
        this.alumnoFinanzasPK = alumnoFinanzasPK;
    }

    public AlumnoFinanzas(AlumnoFinanzasPK alumnoFinanzasPK, String siglas) {
        this.alumnoFinanzasPK = alumnoFinanzasPK;
        this.siglas = siglas;
    }

    public AlumnoFinanzas(int matricula, int periodo) {
        this.alumnoFinanzasPK = new AlumnoFinanzasPK(matricula, periodo);
    }

    public AlumnoFinanzasPK getAlumnoFinanzasPK() {
        return alumnoFinanzasPK;
    }

    public void setAlumnoFinanzasPK(AlumnoFinanzasPK alumnoFinanzasPK) {
        this.alumnoFinanzasPK = alumnoFinanzasPK;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public Boolean getValcartanoadueudoTSU() {
        return valcartanoadueudoTSU;
    }

    public void setValcartanoadueudoTSU(Boolean valcartanoadueudoTSU) {
        this.valcartanoadueudoTSU = valcartanoadueudoTSU;
    }

    public Boolean getValcartanoadedudoIng() {
        return valcartanoadedudoIng;
    }

    public void setValcartanoadedudoIng(Boolean valcartanoadedudoIng) {
        this.valcartanoadedudoIng = valcartanoadedudoIng;
    }

    @XmlTransient
    public List<Aclaracion> getAclaracionList() {
        return aclaracionList;
    }

    public void setAclaracionList(List<Aclaracion> aclaracionList) {
        this.aclaracionList = aclaracionList;
    }

    public Personafinanzas getCurp() {
        return curp;
    }

    public void setCurp(Personafinanzas curp) {
        this.curp = curp;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alumnoFinanzasPK != null ? alumnoFinanzasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlumnoFinanzas)) {
            return false;
        }
        AlumnoFinanzas other = (AlumnoFinanzas) object;
        if ((this.alumnoFinanzasPK == null && other.alumnoFinanzasPK != null) || (this.alumnoFinanzasPK != null && !this.alumnoFinanzasPK.equals(other.alumnoFinanzasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.AlumnoFinanzas[ alumnoFinanzasPK=" + alumnoFinanzasPK + " ]";
    }
    
}
