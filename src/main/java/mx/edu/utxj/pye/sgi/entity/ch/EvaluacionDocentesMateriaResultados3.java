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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "evaluacion_docentes_materia_resultados_3", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findAll", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByEvaluacion", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.evaluacionDocentesMateriaResultados3PK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByEvaluador", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.evaluacionDocentesMateriaResultados3PK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByCveMateria", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.evaluacionDocentesMateriaResultados3PK.cveMateria = :cveMateria")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByEvaluado", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.evaluacionDocentesMateriaResultados3PK.evaluado = :evaluado")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR1", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR2", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR3", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR4", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR5", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR6", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR7", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR8", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR9", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR10", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR11", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR12", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR13", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR14", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR15", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR16", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR17", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByR18", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByCompleto", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.completo = :completo")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByIncompleto", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.incompleto = :incompleto")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados3.findByPromedio", query = "SELECT e FROM EvaluacionDocentesMateriaResultados3 e WHERE e.promedio = :promedio")})
public class EvaluacionDocentesMateriaResultados3 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionDocentesMateriaResultados3PK evaluacionDocentesMateriaResultados3PK;
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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluado", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Personal personal;

    public EvaluacionDocentesMateriaResultados3() {
    }

    public EvaluacionDocentesMateriaResultados3(EvaluacionDocentesMateriaResultados3PK evaluacionDocentesMateriaResultados3PK) {
        this.evaluacionDocentesMateriaResultados3PK = evaluacionDocentesMateriaResultados3PK;
    }

    public EvaluacionDocentesMateriaResultados3(EvaluacionDocentesMateriaResultados3PK evaluacionDocentesMateriaResultados3PK, boolean completo, boolean incompleto, double promedio) {
        this.evaluacionDocentesMateriaResultados3PK = evaluacionDocentesMateriaResultados3PK;
        this.completo = completo;
        this.incompleto = incompleto;
        this.promedio = promedio;
    }

    public EvaluacionDocentesMateriaResultados3(int evaluacion, int evaluador, String cveMateria, int evaluado) {
        this.evaluacionDocentesMateriaResultados3PK = new EvaluacionDocentesMateriaResultados3PK(evaluacion, evaluador, cveMateria, evaluado);
    }

    public EvaluacionDocentesMateriaResultados3PK getEvaluacionDocentesMateriaResultados3PK() {
        return evaluacionDocentesMateriaResultados3PK;
    }

    public void setEvaluacionDocentesMateriaResultados3PK(EvaluacionDocentesMateriaResultados3PK evaluacionDocentesMateriaResultados3PK) {
        this.evaluacionDocentesMateriaResultados3PK = evaluacionDocentesMateriaResultados3PK;
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
        hash += (evaluacionDocentesMateriaResultados3PK != null ? evaluacionDocentesMateriaResultados3PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionDocentesMateriaResultados3)) {
            return false;
        }
        EvaluacionDocentesMateriaResultados3 other = (EvaluacionDocentesMateriaResultados3) object;
        if ((this.evaluacionDocentesMateriaResultados3PK == null && other.evaluacionDocentesMateriaResultados3PK != null) || (this.evaluacionDocentesMateriaResultados3PK != null && !this.evaluacionDocentesMateriaResultados3PK.equals(other.evaluacionDocentesMateriaResultados3PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados3[ evaluacionDocentesMateriaResultados3PK=" + evaluacionDocentesMateriaResultados3PK + " ]";
    }
    
}
