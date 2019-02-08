/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author jonny
 */
@Entity
@Table(name = "evaluacion_docentes_materia_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findAll", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByEvaluacion", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.evaluacionDocentesMateriaResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByEvaluador", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.evaluacionDocentesMateriaResultadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByCveMateria", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.evaluacionDocentesMateriaResultadosPK.cveMateria = :cveMateria")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByEvaluado", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.evaluacionDocentesMateriaResultadosPK.evaluado = :evaluado")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR1", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR2", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR3", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR4", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR5", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR6", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR7", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR8", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR9", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR10", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR11", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR12", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR13", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR14", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR15", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR16", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR17", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR18", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR19", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r19 = :r19")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR20", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r20 = :r20")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR21", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r21 = :r21")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR22", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r22 = :r22")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR23", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r23 = :r23")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR24", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r24 = :r24")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR25", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r25 = :r25")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR26", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r26 = :r26")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR27", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r27 = :r27")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR28", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r28 = :r28")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR29", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r29 = :r29")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR30", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r30 = :r30")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR31", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r31 = :r31")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByR32", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.r32 = :r32")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByCompleto", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.completo = :completo")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByIncompleto", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.incompleto = :incompleto")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados.findByPromedio", query = "SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.promedio = :promedio")})
public class EvaluacionDocentesMateriaResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionDocentesMateriaResultadosPK evaluacionDocentesMateriaResultadosPK;
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
    @Size(max = 1500)
    @Column(name = "r32")
    private String r32;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completo")
    private boolean completo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "incompleto")
    private boolean incompleto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private double promedio;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluado", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;

    public EvaluacionDocentesMateriaResultados() {
    }

    public EvaluacionDocentesMateriaResultados(EvaluacionDocentesMateriaResultadosPK evaluacionDocentesMateriaResultadosPK) {
        this.evaluacionDocentesMateriaResultadosPK = evaluacionDocentesMateriaResultadosPK;
    }

    public EvaluacionDocentesMateriaResultados(EvaluacionDocentesMateriaResultadosPK evaluacionDocentesMateriaResultadosPK, boolean completo, boolean incompleto, double promedio) {
        this.evaluacionDocentesMateriaResultadosPK = evaluacionDocentesMateriaResultadosPK;
        this.completo = completo;
        this.incompleto = incompleto;
        this.promedio = promedio;
    }

    public EvaluacionDocentesMateriaResultados(int evaluacion, int evaluador, String cveMateria, int evaluado) {
        this.evaluacionDocentesMateriaResultadosPK = new EvaluacionDocentesMateriaResultadosPK(evaluacion, evaluador, cveMateria, evaluado);
    }

    public EvaluacionDocentesMateriaResultadosPK getEvaluacionDocentesMateriaResultadosPK() {
        return evaluacionDocentesMateriaResultadosPK;
    }

    public void setEvaluacionDocentesMateriaResultadosPK(EvaluacionDocentesMateriaResultadosPK evaluacionDocentesMateriaResultadosPK) {
        this.evaluacionDocentesMateriaResultadosPK = evaluacionDocentesMateriaResultadosPK;
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

    public String getR32() {
        return r32;
    }

    public void setR32(String r32) {
        this.r32 = r32;
    }

    public boolean getCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }

    public boolean getIncompleto() {
        return incompleto;
    }

    public void setIncompleto(boolean incompleto) {
        this.incompleto = incompleto;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public Evaluaciones getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacionDocentesMateriaResultadosPK != null ? evaluacionDocentesMateriaResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionDocentesMateriaResultados)) {
            return false;
        }
        EvaluacionDocentesMateriaResultados other = (EvaluacionDocentesMateriaResultados) object;
        if ((this.evaluacionDocentesMateriaResultadosPK == null && other.evaluacionDocentesMateriaResultadosPK != null) || (this.evaluacionDocentesMateriaResultadosPK != null && !this.evaluacionDocentesMateriaResultadosPK.equals(other.evaluacionDocentesMateriaResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados[ evaluacionDocentesMateriaResultadosPK=" + evaluacionDocentesMateriaResultadosPK + " ]";
    }
    
}
