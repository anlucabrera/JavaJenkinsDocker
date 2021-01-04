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
 * @author UTXJ
 */
@Entity
@Table(name = "encuesta_vocacional", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaVocacional.findAll", query = "SELECT e FROM EncuestaVocacional e")
    , @NamedQuery(name = "EncuestaVocacional.findByIdPersona", query = "SELECT e FROM EncuestaVocacional e WHERE e.idPersona = :idPersona")
    , @NamedQuery(name = "EncuestaVocacional.findByR1", query = "SELECT e FROM EncuestaVocacional e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EncuestaVocacional.findByR2", query = "SELECT e FROM EncuestaVocacional e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EncuestaVocacional.findByR3", query = "SELECT e FROM EncuestaVocacional e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EncuestaVocacional.findByR4", query = "SELECT e FROM EncuestaVocacional e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EncuestaVocacional.findByR5", query = "SELECT e FROM EncuestaVocacional e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EncuestaVocacional.findByR6", query = "SELECT e FROM EncuestaVocacional e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EncuestaVocacional.findByR7", query = "SELECT e FROM EncuestaVocacional e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EncuestaVocacional.findByR8", query = "SELECT e FROM EncuestaVocacional e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EncuestaVocacional.findByR9", query = "SELECT e FROM EncuestaVocacional e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EncuestaVocacional.findByCompleto", query = "SELECT e FROM EncuestaVocacional e WHERE e.completo = :completo")})
public class EncuestaVocacional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_persona")
    private Integer idPersona;
    @Size(max = 200)
    @Column(name = "r1")
    private String r1;
    @Size(max = 200)
    @Column(name = "r2")
    private String r2;
    @Size(max = 10)
    @Column(name = "r3")
    private String r3;
    @Column(name = "r4")
    private Short r4;
    @Size(max = 10)
    @Column(name = "r5")
    private String r5;
    @Size(max = 100)
    @Column(name = "r6")
    private String r6;
    @Size(max = 200)
    @Column(name = "r7")
    private String r7;
    @Size(max = 10)
    @Column(name = "r8")
    private String r8;
    @Size(max = 200)
    @Column(name = "r9")
    private String r9;
    @Column(name = "completo")
    private Boolean completo;
    @JoinColumn(name = "id_persona", referencedColumnName = "idpersona", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Persona persona;

    public EncuestaVocacional() {
    }

    public EncuestaVocacional(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public String getR3() {
        return r3;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }

    public Short getR4() {
        return r4;
    }

    public void setR4(Short r4) {
        this.r4 = r4;
    }

    public String getR5() {
        return r5;
    }

    public void setR5(String r5) {
        this.r5 = r5;
    }

    public String getR6() {
        return r6;
    }

    public void setR6(String r6) {
        this.r6 = r6;
    }

    public String getR7() {
        return r7;
    }

    public void setR7(String r7) {
        this.r7 = r7;
    }

    public String getR8() {
        return r8;
    }

    public void setR8(String r8) {
        this.r8 = r8;
    }

    public String getR9() {
        return r9;
    }

    public void setR9(String r9) {
        this.r9 = r9;
    }

    public Boolean getCompleto() {
        return completo;
    }

    public void setCompleto(Boolean completo) {
        this.completo = completo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaVocacional)) {
            return false;
        }
        EncuestaVocacional other = (EncuestaVocacional) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaVocacional[ idPersona=" + idPersona + " ]";
    }
    
}
