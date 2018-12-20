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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "encuesta_satisfaccion_egresados_ing", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findAll", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByEvaluacion", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.encuestaSatisfaccionEgresadosIngPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByEvaluador", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.encuestaSatisfaccionEgresadosIngPK.evaluador = :evaluador")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR1", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR2", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR3", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR4", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR5", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR6", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR7", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR8", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosIng.findByR9", query = "SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.r9 = :r9")})
public class EncuestaSatisfaccionEgresadosIng implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaSatisfaccionEgresadosIngPK encuestaSatisfaccionEgresadosIngPK;
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
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;

    public EncuestaSatisfaccionEgresadosIng() {
    }

    public EncuestaSatisfaccionEgresadosIng(EncuestaSatisfaccionEgresadosIngPK encuestaSatisfaccionEgresadosIngPK) {
        this.encuestaSatisfaccionEgresadosIngPK = encuestaSatisfaccionEgresadosIngPK;
    }

    public EncuestaSatisfaccionEgresadosIng(int evaluacion, int evaluador) {
        this.encuestaSatisfaccionEgresadosIngPK = new EncuestaSatisfaccionEgresadosIngPK(evaluacion, evaluador);
    }

    public EncuestaSatisfaccionEgresadosIngPK getEncuestaSatisfaccionEgresadosIngPK() {
        return encuestaSatisfaccionEgresadosIngPK;
    }

    public void setEncuestaSatisfaccionEgresadosIngPK(EncuestaSatisfaccionEgresadosIngPK encuestaSatisfaccionEgresadosIngPK) {
        this.encuestaSatisfaccionEgresadosIngPK = encuestaSatisfaccionEgresadosIngPK;
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

    public Evaluaciones getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaSatisfaccionEgresadosIngPK != null ? encuestaSatisfaccionEgresadosIngPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccionEgresadosIng)) {
            return false;
        }
        EncuestaSatisfaccionEgresadosIng other = (EncuestaSatisfaccionEgresadosIng) object;
        if ((this.encuestaSatisfaccionEgresadosIngPK == null && other.encuestaSatisfaccionEgresadosIngPK != null) || (this.encuestaSatisfaccionEgresadosIngPK != null && !this.encuestaSatisfaccionEgresadosIngPK.equals(other.encuestaSatisfaccionEgresadosIngPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng[ encuestaSatisfaccionEgresadosIngPK=" + encuestaSatisfaccionEgresadosIngPK + " ]";
    }
    
}
