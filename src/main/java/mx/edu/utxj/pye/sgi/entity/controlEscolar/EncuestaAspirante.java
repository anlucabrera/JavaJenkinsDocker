/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "encuesta_aspirante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaAspirante.findAll", query = "SELECT e FROM EncuestaAspirante e")
    , @NamedQuery(name = "EncuestaAspirante.findByIdEncuestaAspirante", query = "SELECT e FROM EncuestaAspirante e WHERE e.encuestaAspirantePK.idEncuestaAspirante = :idEncuestaAspirante")
    , @NamedQuery(name = "EncuestaAspirante.findByCveAspirante", query = "SELECT e FROM EncuestaAspirante e WHERE e.encuestaAspirantePK.cveAspirante = :cveAspirante")
    , @NamedQuery(name = "EncuestaAspirante.findByR2", query = "SELECT e FROM EncuestaAspirante e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EncuestaAspirante.findByR3", query = "SELECT e FROM EncuestaAspirante e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EncuestaAspirante.findByR4", query = "SELECT e FROM EncuestaAspirante e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EncuestaAspirante.findByR5", query = "SELECT e FROM EncuestaAspirante e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EncuestaAspirante.findByR6", query = "SELECT e FROM EncuestaAspirante e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EncuestaAspirante.findByR7", query = "SELECT e FROM EncuestaAspirante e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EncuestaAspirante.findByR8", query = "SELECT e FROM EncuestaAspirante e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EncuestaAspirante.findByR9", query = "SELECT e FROM EncuestaAspirante e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EncuestaAspirante.findByR10", query = "SELECT e FROM EncuestaAspirante e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EncuestaAspirante.findByR11", query = "SELECT e FROM EncuestaAspirante e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EncuestaAspirante.findByR12", query = "SELECT e FROM EncuestaAspirante e WHERE e.r12 = :r12")})
public class EncuestaAspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaAspirantePK encuestaAspirantePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r2")
    private short r2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "r3")
    private String r3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r4")
    private boolean r4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r5")
    private boolean r5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "r6")
    private String r6;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r7")
    private short r7;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r8")
    private short r8;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r9")
    private short r9;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r10")
    private short r10;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r11")
    private short r11;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r12")
    private short r12;
    @JoinColumn(name = "cve_aspirante", referencedColumnName = "id_persona", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Aspirante aspirante;
    @JoinColumn(name = "r1", referencedColumnName = "id_lengua_indigena")
    @ManyToOne(optional = false)
    private LenguaIndigena r1;
    @JoinColumn(name = "r13", referencedColumnName = "id_medio_difusion")
    @ManyToOne(optional = false)
    private MedioDifusion r13;

    public EncuestaAspirante() {
    }

    public EncuestaAspirante(EncuestaAspirantePK encuestaAspirantePK) {
        this.encuestaAspirantePK = encuestaAspirantePK;
    }

    public EncuestaAspirante(EncuestaAspirantePK encuestaAspirantePK, short r2, String r3, boolean r4, boolean r5, String r6, short r7, short r8, short r9, short r10, short r11, short r12) {
        this.encuestaAspirantePK = encuestaAspirantePK;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
        this.r6 = r6;
        this.r7 = r7;
        this.r8 = r8;
        this.r9 = r9;
        this.r10 = r10;
        this.r11 = r11;
        this.r12 = r12;
    }

    public EncuestaAspirante(int idEncuestaAspirante, int cveAspirante) {
        this.encuestaAspirantePK = new EncuestaAspirantePK(idEncuestaAspirante, cveAspirante);
    }

    public EncuestaAspirantePK getEncuestaAspirantePK() {
        return encuestaAspirantePK;
    }

    public void setEncuestaAspirantePK(EncuestaAspirantePK encuestaAspirantePK) {
        this.encuestaAspirantePK = encuestaAspirantePK;
    }

    public short getR2() {
        return r2;
    }

    public void setR2(short r2) {
        this.r2 = r2;
    }

    public String getR3() {
        return r3;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }

    public boolean getR4() {
        return r4;
    }

    public void setR4(boolean r4) {
        this.r4 = r4;
    }

    public boolean getR5() {
        return r5;
    }

    public void setR5(boolean r5) {
        this.r5 = r5;
    }

    public String getR6() {
        return r6;
    }

    public void setR6(String r6) {
        this.r6 = r6;
    }

    public short getR7() {
        return r7;
    }

    public void setR7(short r7) {
        this.r7 = r7;
    }

    public short getR8() {
        return r8;
    }

    public void setR8(short r8) {
        this.r8 = r8;
    }

    public short getR9() {
        return r9;
    }

    public void setR9(short r9) {
        this.r9 = r9;
    }

    public short getR10() {
        return r10;
    }

    public void setR10(short r10) {
        this.r10 = r10;
    }

    public short getR11() {
        return r11;
    }

    public void setR11(short r11) {
        this.r11 = r11;
    }

    public short getR12() {
        return r12;
    }

    public void setR12(short r12) {
        this.r12 = r12;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public LenguaIndigena getR1() {
        return r1;
    }

    public void setR1(LenguaIndigena r1) {
        this.r1 = r1;
    }

    public MedioDifusion getR13() {
        return r13;
    }

    public void setR13(MedioDifusion r13) {
        this.r13 = r13;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaAspirantePK != null ? encuestaAspirantePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaAspirante)) {
            return false;
        }
        EncuestaAspirante other = (EncuestaAspirante) object;
        if ((this.encuestaAspirantePK == null && other.encuestaAspirantePK != null) || (this.encuestaAspirantePK != null && !this.encuestaAspirantePK.equals(other.encuestaAspirantePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante[ encuestaAspirantePK=" + encuestaAspirantePK + " ]";
    }
    
}
