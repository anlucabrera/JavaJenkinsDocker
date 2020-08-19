/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "evauacioneticares", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evauacioneticares.findAll", query = "SELECT e FROM Evauacioneticares e")
    , @NamedQuery(name = "Evauacioneticares.findByEvaluacion", query = "SELECT e FROM Evauacioneticares e WHERE e.evauacioneticaresPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "Evauacioneticares.findByEvaluador", query = "SELECT e FROM Evauacioneticares e WHERE e.evauacioneticaresPK.evaluador = :evaluador")
    , @NamedQuery(name = "Evauacioneticares.findByR1", query = "SELECT e FROM Evauacioneticares e WHERE e.r1 = :r1")
    , @NamedQuery(name = "Evauacioneticares.findByR2", query = "SELECT e FROM Evauacioneticares e WHERE e.r2 = :r2")
    , @NamedQuery(name = "Evauacioneticares.findByR3", query = "SELECT e FROM Evauacioneticares e WHERE e.r3 = :r3")
    , @NamedQuery(name = "Evauacioneticares.findByR4", query = "SELECT e FROM Evauacioneticares e WHERE e.r4 = :r4")
    , @NamedQuery(name = "Evauacioneticares.findByR5", query = "SELECT e FROM Evauacioneticares e WHERE e.r5 = :r5")
    , @NamedQuery(name = "Evauacioneticares.findByR6", query = "SELECT e FROM Evauacioneticares e WHERE e.r6 = :r6")
    , @NamedQuery(name = "Evauacioneticares.findByR7", query = "SELECT e FROM Evauacioneticares e WHERE e.r7 = :r7")
    , @NamedQuery(name = "Evauacioneticares.findByR8", query = "SELECT e FROM Evauacioneticares e WHERE e.r8 = :r8")
    , @NamedQuery(name = "Evauacioneticares.findByR9", query = "SELECT e FROM Evauacioneticares e WHERE e.r9 = :r9")
    , @NamedQuery(name = "Evauacioneticares.findByR10", query = "SELECT e FROM Evauacioneticares e WHERE e.r10 = :r10")})
public class Evauacioneticares implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvauacioneticaresPK evauacioneticaresPK;
    @Column(name = "r1")
    private Short r1;
    @Column(name = "r2")
    private Short r2;
    @Column(name = "r3")
    private Short r3;
    @Column(name = "r4")
    private Short r4;
    @Column(name = "r5")
    private Short r5;
    @Column(name = "r6")
    private Short r6;
    @Column(name = "r7")
    private Short r7;
    @Column(name = "r8")
    private Short r8;
    @Column(name = "r9")
    private Short r9;
    @Column(name = "r10")
    private Short r10;

    public Evauacioneticares() {
    }

    public Evauacioneticares(EvauacioneticaresPK evauacioneticaresPK) {
        this.evauacioneticaresPK = evauacioneticaresPK;
    }

    public Evauacioneticares(int evaluacion, int evaluador) {
        this.evauacioneticaresPK = new EvauacioneticaresPK(evaluacion, evaluador);
    }

    public EvauacioneticaresPK getEvauacioneticaresPK() {
        return evauacioneticaresPK;
    }

    public void setEvauacioneticaresPK(EvauacioneticaresPK evauacioneticaresPK) {
        this.evauacioneticaresPK = evauacioneticaresPK;
    }

    public Short getR1() {
        return r1;
    }

    public void setR1(Short r1) {
        this.r1 = r1;
    }

    public Short getR2() {
        return r2;
    }

    public void setR2(Short r2) {
        this.r2 = r2;
    }

    public Short getR3() {
        return r3;
    }

    public void setR3(Short r3) {
        this.r3 = r3;
    }

    public Short getR4() {
        return r4;
    }

    public void setR4(Short r4) {
        this.r4 = r4;
    }

    public Short getR5() {
        return r5;
    }

    public void setR5(Short r5) {
        this.r5 = r5;
    }

    public Short getR6() {
        return r6;
    }

    public void setR6(Short r6) {
        this.r6 = r6;
    }

    public Short getR7() {
        return r7;
    }

    public void setR7(Short r7) {
        this.r7 = r7;
    }

    public Short getR8() {
        return r8;
    }

    public void setR8(Short r8) {
        this.r8 = r8;
    }

    public Short getR9() {
        return r9;
    }

    public void setR9(Short r9) {
        this.r9 = r9;
    }

    public Short getR10() {
        return r10;
    }

    public void setR10(Short r10) {
        this.r10 = r10;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evauacioneticaresPK != null ? evauacioneticaresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evauacioneticares)) {
            return false;
        }
        Evauacioneticares other = (Evauacioneticares) object;
        if ((this.evauacioneticaresPK == null && other.evauacioneticaresPK != null) || (this.evauacioneticaresPK != null && !this.evauacioneticaresPK.equals(other.evauacioneticaresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Evauacioneticares[ evauacioneticaresPK=" + evauacioneticaresPK + " ]";
    }
    
}
