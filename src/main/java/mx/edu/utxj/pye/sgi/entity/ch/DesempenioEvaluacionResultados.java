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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "desempenio_evaluacion_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesempenioEvaluacionResultados.findAll", query = "SELECT d FROM DesempenioEvaluacionResultados d")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByEvaluacion", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.pk.evaluacion = :evaluacion")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByEvaluador", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.pk.evaluador = :evaluador")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByEvaluado", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.pk.evaluado = :evaluado")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR1", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r1 = :r1")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR2", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r2 = :r2")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR3", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r3 = :r3")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR4", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r4 = :r4")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR5", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r5 = :r5")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR6", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r6 = :r6")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR7", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r7 = :r7")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR8", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r8 = :r8")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR9", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r9 = :r9")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR10", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r10 = :r10")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR11", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r11 = :r11")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR12", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r12 = :r12")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR13", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r13 = :r13")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR14", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r14 = :r14")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR15", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r15 = :r15")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR16", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r16 = :r16")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR17", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r17 = :r17")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR18", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r18 = :r18")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR19", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r19 = :r19")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR20", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r20 = :r20")
    , @NamedQuery(name = "DesempenioEvaluacionResultados.findByR21", query = "SELECT d FROM DesempenioEvaluacionResultados d WHERE d.r21 = :r21")})
public class DesempenioEvaluacionResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DesempenioEvaluacionResultadosPK pk;
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
    @Column(name = "r11")
    private Short r11;
    @Column(name = "r12")
    private Short r12;
    @Column(name = "r13")
    private Short r13;
    @Column(name = "r14")
    private Short r14;
    @Column(name = "r15")
    private Short r15;
    @Column(name = "r16")
    private Short r16;
    @Column(name = "r17")
    private Short r17;
    @Column(name = "r18")
    private Short r18;
    @Column(name = "r19")
    private Short r19;
    @Column(name = "r20")
    private Short r20;
    @Size(max = 500)
    @Column(name = "r21")
    private String r21;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DesempenioEvaluaciones desempenioEvaluaciones;
    @JoinColumn(name = "evaluado", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;
    @JoinColumn(name = "evaluador", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal1;

    public DesempenioEvaluacionResultados() {
    }

    public DesempenioEvaluacionResultados(DesempenioEvaluacionResultadosPK desempenioEvaluacionResultadosPK) {
        this.pk = desempenioEvaluacionResultadosPK;
    }

    public DesempenioEvaluacionResultados(int evaluacion, int evaluador, int evaluado) {
        this.pk = new DesempenioEvaluacionResultadosPK(evaluacion, evaluador, evaluado);
    }

    public DesempenioEvaluacionResultadosPK getPk() {
        return pk;
    }

    public void setPk(DesempenioEvaluacionResultadosPK pk) {
        this.pk = pk;
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

    public Short getR11() {
        return r11;
    }

    public void setR11(Short r11) {
        this.r11 = r11;
    }

    public Short getR12() {
        return r12;
    }

    public void setR12(Short r12) {
        this.r12 = r12;
    }

    public Short getR13() {
        return r13;
    }

    public void setR13(Short r13) {
        this.r13 = r13;
    }

    public Short getR14() {
        return r14;
    }

    public void setR14(Short r14) {
        this.r14 = r14;
    }

    public Short getR15() {
        return r15;
    }

    public void setR15(Short r15) {
        this.r15 = r15;
    }

    public Short getR16() {
        return r16;
    }

    public void setR16(Short r16) {
        this.r16 = r16;
    }

    public Short getR17() {
        return r17;
    }

    public void setR17(Short r17) {
        this.r17 = r17;
    }

    public Short getR18() {
        return r18;
    }

    public void setR18(Short r18) {
        this.r18 = r18;
    }

    public Short getR19() {
        return r19;
    }

    public void setR19(Short r19) {
        this.r19 = r19;
    }

    public Short getR20() {
        return r20;
    }

    public void setR20(Short r20) {
        this.r20 = r20;
    }

    public String getR21() {
        return r21;
    }

    public void setR21(String r21) {
        this.r21 = r21;
    }

    public DesempenioEvaluaciones getDesempenioEvaluaciones() {
        return desempenioEvaluaciones;
    }

    public void setDesempenioEvaluaciones(DesempenioEvaluaciones desempenioEvaluaciones) {
        this.desempenioEvaluaciones = desempenioEvaluaciones;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public Personal getPersonal1() {
        return personal1;
    }

    public void setPersonal1(Personal personal1) {
        this.personal1 = personal1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesempenioEvaluacionResultados)) {
            return false;
        }
        DesempenioEvaluacionResultados other = (DesempenioEvaluacionResultados) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados[ desempenioEvaluacionResultadosPK=" + pk + " ]";
    }
    
}
