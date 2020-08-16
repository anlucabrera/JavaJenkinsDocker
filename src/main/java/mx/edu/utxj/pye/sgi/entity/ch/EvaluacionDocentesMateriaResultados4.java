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
 * @author UTXJ
 */
@Entity
@Table(name = "evaluacion_docentes_materia_resultados_4", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findAll", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByEvaluacion", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.evaluacionDocentesMateriaResultados4PK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByEvaluador", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.evaluacionDocentesMateriaResultados4PK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByCveMateria", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.evaluacionDocentesMateriaResultados4PK.cveMateria = :cveMateria")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByEvaluado", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.evaluacionDocentesMateriaResultados4PK.evaluado = :evaluado")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR1", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR2", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR3", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR4", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR5", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR6", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR7", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR8", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR9", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR10", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR11", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR12", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR13", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR14", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR15", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR16", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR17", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR18", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR19", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r19 = :r19")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByR20", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.r20 = :r20")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByCompleto", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.completo = :completo")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByIncompleto", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.incompleto = :incompleto")
    , @NamedQuery(name = "EvaluacionDocentesMateriaResultados4.findByPromedio", query = "SELECT e FROM EvaluacionDocentesMateriaResultados4 e WHERE e.promedio = :promedio")})
public class EvaluacionDocentesMateriaResultados4 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionDocentesMateriaResultados4PK evaluacionDocentesMateriaResultados4PK;
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
    @Size(max = 1500)
    @Column(name = "r20")
    private String r20;
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

    public EvaluacionDocentesMateriaResultados4() {
    }

    public EvaluacionDocentesMateriaResultados4(EvaluacionDocentesMateriaResultados4PK evaluacionDocentesMateriaResultados4PK) {
        this.evaluacionDocentesMateriaResultados4PK = evaluacionDocentesMateriaResultados4PK;
    }

    public EvaluacionDocentesMateriaResultados4(EvaluacionDocentesMateriaResultados4PK evaluacionDocentesMateriaResultados4PK, boolean completo, boolean incompleto, double promedio) {
        this.evaluacionDocentesMateriaResultados4PK = evaluacionDocentesMateriaResultados4PK;
        this.completo = completo;
        this.incompleto = incompleto;
        this.promedio = promedio;
    }

    public EvaluacionDocentesMateriaResultados4(int evaluacion, int evaluador, String cveMateria, int evaluado) {
        this.evaluacionDocentesMateriaResultados4PK = new EvaluacionDocentesMateriaResultados4PK(evaluacion, evaluador, cveMateria, evaluado);
    }

    public EvaluacionDocentesMateriaResultados4PK getEvaluacionDocentesMateriaResultados4PK() {
        return evaluacionDocentesMateriaResultados4PK;
    }

    public void setEvaluacionDocentesMateriaResultados4PK(EvaluacionDocentesMateriaResultados4PK evaluacionDocentesMateriaResultados4PK) {
        this.evaluacionDocentesMateriaResultados4PK = evaluacionDocentesMateriaResultados4PK;
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

    public String getR20() {
        return r20;
    }

    public void setR20(String r20) {
        this.r20 = r20;
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
        hash += (evaluacionDocentesMateriaResultados4PK != null ? evaluacionDocentesMateriaResultados4PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionDocentesMateriaResultados4)) {
            return false;
        }
        EvaluacionDocentesMateriaResultados4 other = (EvaluacionDocentesMateriaResultados4) object;
        if ((this.evaluacionDocentesMateriaResultados4PK == null && other.evaluacionDocentesMateriaResultados4PK != null) || (this.evaluacionDocentesMateriaResultados4PK != null && !this.evaluacionDocentesMateriaResultados4PK.equals(other.evaluacionDocentesMateriaResultados4PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados4[ evaluacionDocentesMateriaResultados4PK=" + evaluacionDocentesMateriaResultados4PK + " ]";
    }
    
}
