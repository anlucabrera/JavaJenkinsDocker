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
 * @author jonny
 */
@Entity
@Table(name = "evaluaciones_control_interno_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionesControlInternoResultados.findAll", query = "SELECT e FROM EvaluacionesControlInternoResultados e")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByEvaluacion", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.evaluacionesControlInternoResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByEvaluador", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.evaluacionesControlInternoResultadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR1", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR2", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR3", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR4", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR5", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR6", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR7", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR8", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR9", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR10", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR11", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR12", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR13", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR14", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR15", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR16", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR17", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR18", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR19", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r19 = :r19")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR20", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r20 = :r20")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR21", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r21 = :r21")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR22", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r22 = :r22")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR23", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r23 = :r23")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR24", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r24 = :r24")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR25", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r25 = :r25")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR26", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r26 = :r26")
    , @NamedQuery(name = "EvaluacionesControlInternoResultados.findByR27", query = "SELECT e FROM EvaluacionesControlInternoResultados e WHERE e.r27 = :r27")})
public class EvaluacionesControlInternoResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionesControlInternoResultadosPK evaluacionesControlInternoResultadosPK;
    @Size(max = 2)
    @Column(name = "r1")
    private String r1;
    @Size(max = 2)
    @Column(name = "r2")
    private String r2;
    @Size(max = 2)
    @Column(name = "r3")
    private String r3;
    @Size(max = 2)
    @Column(name = "r4")
    private String r4;
    @Size(max = 2)
    @Column(name = "r5")
    private String r5;
    @Size(max = 2)
    @Column(name = "r6")
    private String r6;
    @Size(max = 2)
    @Column(name = "r7")
    private String r7;
    @Size(max = 2)
    @Column(name = "r8")
    private String r8;
    @Size(max = 2)
    @Column(name = "r9")
    private String r9;
    @Size(max = 2)
    @Column(name = "r10")
    private String r10;
    @Size(max = 2)
    @Column(name = "r11")
    private String r11;
    @Size(max = 2)
    @Column(name = "r12")
    private String r12;
    @Size(max = 2)
    @Column(name = "r13")
    private String r13;
    @Size(max = 2)
    @Column(name = "r14")
    private String r14;
    @Size(max = 2)
    @Column(name = "r15")
    private String r15;
    @Size(max = 2)
    @Column(name = "r16")
    private String r16;
    @Size(max = 2)
    @Column(name = "r17")
    private String r17;
    @Size(max = 2)
    @Column(name = "r18")
    private String r18;
    @Size(max = 2)
    @Column(name = "r19")
    private String r19;
    @Size(max = 2)
    @Column(name = "r20")
    private String r20;
    @Size(max = 2)
    @Column(name = "r21")
    private String r21;
    @Size(max = 2)
    @Column(name = "r22")
    private String r22;
    @Size(max = 2)
    @Column(name = "r23")
    private String r23;
    @Size(max = 2)
    @Column(name = "r24")
    private String r24;
    @Size(max = 2)
    @Column(name = "r25")
    private String r25;
    @Size(max = 2)
    @Column(name = "r26")
    private String r26;
    @Size(max = 2)
    @Column(name = "r27")
    private String r27;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluador", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;

    public EvaluacionesControlInternoResultados() {
    }

    public EvaluacionesControlInternoResultados(EvaluacionesControlInternoResultadosPK evaluacionesControlInternoResultadosPK) {
        this.evaluacionesControlInternoResultadosPK = evaluacionesControlInternoResultadosPK;
    }

    public EvaluacionesControlInternoResultados(int evaluacion, int evaluador) {
        this.evaluacionesControlInternoResultadosPK = new EvaluacionesControlInternoResultadosPK(evaluacion, evaluador);
    }

    public EvaluacionesControlInternoResultadosPK getEvaluacionesControlInternoResultadosPK() {
        return evaluacionesControlInternoResultadosPK;
    }

    public void setEvaluacionesControlInternoResultadosPK(EvaluacionesControlInternoResultadosPK evaluacionesControlInternoResultadosPK) {
        this.evaluacionesControlInternoResultadosPK = evaluacionesControlInternoResultadosPK;
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

    public String getR4() {
        return r4;
    }

    public void setR4(String r4) {
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

    public String getR10() {
        return r10;
    }

    public void setR10(String r10) {
        this.r10 = r10;
    }

    public String getR11() {
        return r11;
    }

    public void setR11(String r11) {
        this.r11 = r11;
    }

    public String getR12() {
        return r12;
    }

    public void setR12(String r12) {
        this.r12 = r12;
    }

    public String getR13() {
        return r13;
    }

    public void setR13(String r13) {
        this.r13 = r13;
    }

    public String getR14() {
        return r14;
    }

    public void setR14(String r14) {
        this.r14 = r14;
    }

    public String getR15() {
        return r15;
    }

    public void setR15(String r15) {
        this.r15 = r15;
    }

    public String getR16() {
        return r16;
    }

    public void setR16(String r16) {
        this.r16 = r16;
    }

    public String getR17() {
        return r17;
    }

    public void setR17(String r17) {
        this.r17 = r17;
    }

    public String getR18() {
        return r18;
    }

    public void setR18(String r18) {
        this.r18 = r18;
    }

    public String getR19() {
        return r19;
    }

    public void setR19(String r19) {
        this.r19 = r19;
    }

    public String getR20() {
        return r20;
    }

    public void setR20(String r20) {
        this.r20 = r20;
    }

    public String getR21() {
        return r21;
    }

    public void setR21(String r21) {
        this.r21 = r21;
    }

    public String getR22() {
        return r22;
    }

    public void setR22(String r22) {
        this.r22 = r22;
    }

    public String getR23() {
        return r23;
    }

    public void setR23(String r23) {
        this.r23 = r23;
    }

    public String getR24() {
        return r24;
    }

    public void setR24(String r24) {
        this.r24 = r24;
    }

    public String getR25() {
        return r25;
    }

    public void setR25(String r25) {
        this.r25 = r25;
    }

    public String getR26() {
        return r26;
    }

    public void setR26(String r26) {
        this.r26 = r26;
    }

    public String getR27() {
        return r27;
    }

    public void setR27(String r27) {
        this.r27 = r27;
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
        hash += (evaluacionesControlInternoResultadosPK != null ? evaluacionesControlInternoResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionesControlInternoResultados)) {
            return false;
        }
        EvaluacionesControlInternoResultados other = (EvaluacionesControlInternoResultados) object;
        if ((this.evaluacionesControlInternoResultadosPK == null && other.evaluacionesControlInternoResultadosPK != null) || (this.evaluacionesControlInternoResultadosPK != null && !this.evaluacionesControlInternoResultadosPK.equals(other.evaluacionesControlInternoResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesControlInternoResultados[ evaluacionesControlInternoResultadosPK=" + evaluacionesControlInternoResultadosPK + " ]";
    }
    
}
