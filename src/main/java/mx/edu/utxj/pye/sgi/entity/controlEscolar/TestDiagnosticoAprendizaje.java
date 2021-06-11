/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "test_diagnostico_aprendizaje", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TestDiagnosticoAprendizaje.findAll", query = "SELECT t FROM TestDiagnosticoAprendizaje t")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByEvaluacion", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByEvaluador", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.testDiagnosticoAprendizajePK.evaluador = :evaluador")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByFechaAplicacion", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.fechaAplicacion = :fechaAplicacion")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR1", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r1 = :r1")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR2", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r2 = :r2")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR3", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r3 = :r3")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR4", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r4 = :r4")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR5", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r5 = :r5")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR6", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r6 = :r6")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR7", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r7 = :r7")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR8", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r8 = :r8")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR9", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r9 = :r9")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR10", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r10 = :r10")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR11", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r11 = :r11")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR12", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r12 = :r12")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR13", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r13 = :r13")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR14", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r14 = :r14")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR15", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r15 = :r15")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR16", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r16 = :r16")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR17", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r17 = :r17")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR18", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r18 = :r18")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR19", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r19 = :r19")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR20", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r20 = :r20")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR21", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r21 = :r21")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR22", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r22 = :r22")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR23", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r23 = :r23")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR24", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r24 = :r24")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR25", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r25 = :r25")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR26", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r26 = :r26")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR27", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r27 = :r27")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR28", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r28 = :r28")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR29", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r29 = :r29")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR30", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r30 = :r30")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR31", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r31 = :r31")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR32", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r32 = :r32")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR33", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r33 = :r33")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR34", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r34 = :r34")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR35", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r35 = :r35")
    , @NamedQuery(name = "TestDiagnosticoAprendizaje.findByR36", query = "SELECT t FROM TestDiagnosticoAprendizaje t WHERE t.r36 = :r36")})
public class TestDiagnosticoAprendizaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TestDiagnosticoAprendizajePK testDiagnosticoAprendizajePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;
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
    @Column(name = "r21")
    private Short r21;
    @Column(name = "r22")
    private Short r22;
    @Column(name = "r23")
    private Short r23;
    @Column(name = "r24")
    private Short r24;
    @Column(name = "r25")
    private Short r25;
    @Column(name = "r26")
    private Short r26;
    @Column(name = "r27")
    private Short r27;
    @Column(name = "r28")
    private Short r28;
    @Column(name = "r29")
    private Short r29;
    @Column(name = "r30")
    private Short r30;
    @Column(name = "r31")
    private Short r31;
    @Column(name = "r32")
    private Short r32;
    @Column(name = "r33")
    private Short r33;
    @Column(name = "r34")
    private Short r34;
    @Column(name = "r35")
    private Short r35;
    @Column(name = "r36")
    private Short r36;

    public TestDiagnosticoAprendizaje() {
    }

    public TestDiagnosticoAprendizaje(TestDiagnosticoAprendizajePK testDiagnosticoAprendizajePK) {
        this.testDiagnosticoAprendizajePK = testDiagnosticoAprendizajePK;
    }

    public TestDiagnosticoAprendizaje(TestDiagnosticoAprendizajePK testDiagnosticoAprendizajePK, Date fechaAplicacion) {
        this.testDiagnosticoAprendizajePK = testDiagnosticoAprendizajePK;
        this.fechaAplicacion = fechaAplicacion;
    }

    public TestDiagnosticoAprendizaje(int evaluacion, int evaluador) {
        this.testDiagnosticoAprendizajePK = new TestDiagnosticoAprendizajePK(evaluacion, evaluador);
    }

    public TestDiagnosticoAprendizajePK getTestDiagnosticoAprendizajePK() {
        return testDiagnosticoAprendizajePK;
    }

    public void setTestDiagnosticoAprendizajePK(TestDiagnosticoAprendizajePK testDiagnosticoAprendizajePK) {
        this.testDiagnosticoAprendizajePK = testDiagnosticoAprendizajePK;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
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

    public Short getR21() {
        return r21;
    }

    public void setR21(Short r21) {
        this.r21 = r21;
    }

    public Short getR22() {
        return r22;
    }

    public void setR22(Short r22) {
        this.r22 = r22;
    }

    public Short getR23() {
        return r23;
    }

    public void setR23(Short r23) {
        this.r23 = r23;
    }

    public Short getR24() {
        return r24;
    }

    public void setR24(Short r24) {
        this.r24 = r24;
    }

    public Short getR25() {
        return r25;
    }

    public void setR25(Short r25) {
        this.r25 = r25;
    }

    public Short getR26() {
        return r26;
    }

    public void setR26(Short r26) {
        this.r26 = r26;
    }

    public Short getR27() {
        return r27;
    }

    public void setR27(Short r27) {
        this.r27 = r27;
    }

    public Short getR28() {
        return r28;
    }

    public void setR28(Short r28) {
        this.r28 = r28;
    }

    public Short getR29() {
        return r29;
    }

    public void setR29(Short r29) {
        this.r29 = r29;
    }

    public Short getR30() {
        return r30;
    }

    public void setR30(Short r30) {
        this.r30 = r30;
    }

    public Short getR31() {
        return r31;
    }

    public void setR31(Short r31) {
        this.r31 = r31;
    }

    public Short getR32() {
        return r32;
    }

    public void setR32(Short r32) {
        this.r32 = r32;
    }

    public Short getR33() {
        return r33;
    }

    public void setR33(Short r33) {
        this.r33 = r33;
    }

    public Short getR34() {
        return r34;
    }

    public void setR34(Short r34) {
        this.r34 = r34;
    }

    public Short getR35() {
        return r35;
    }

    public void setR35(Short r35) {
        this.r35 = r35;
    }

    public Short getR36() {
        return r36;
    }

    public void setR36(Short r36) {
        this.r36 = r36;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testDiagnosticoAprendizajePK != null ? testDiagnosticoAprendizajePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TestDiagnosticoAprendizaje)) {
            return false;
        }
        TestDiagnosticoAprendizaje other = (TestDiagnosticoAprendizaje) object;
        if ((this.testDiagnosticoAprendizajePK == null && other.testDiagnosticoAprendizajePK != null) || (this.testDiagnosticoAprendizajePK != null && !this.testDiagnosticoAprendizajePK.equals(other.testDiagnosticoAprendizajePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje[ testDiagnosticoAprendizajePK=" + testDiagnosticoAprendizajePK + " ]";
    }
    
}
