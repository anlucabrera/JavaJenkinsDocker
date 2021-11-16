/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluacion_conocimiento_codigo_etica_resultados_2", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findAll", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByEvaluacion", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.evaluacionConocimientoCodigoEticaResultados2PK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByEvaluador", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.evaluacionConocimientoCodigoEticaResultados2PK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR1", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR2", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR3", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR4", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR5", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR6", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR7", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR8", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR9", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR10", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR11", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByR12", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findByCompleto", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.completo = :completo")
    , @NamedQuery(name = "EvaluacionConocimientoCodigoEticaResultados2.findBytotalCorrectas", query = "SELECT e FROM EvaluacionConocimientoCodigoEticaResultados2 e WHERE e.totalCorrectas = :totalCorrectas")})
public class EvaluacionConocimientoCodigoEticaResultados2 implements Serializable {

    @Size(max = 4)
    @Column(name = "r1")
    private String r1;
    @Size(max = 4)
    @Column(name = "r2")
    private String r2;
    @Size(max = 4)
    @Column(name = "r3")
    private String r3;
    @Size(max = 4)
    @Column(name = "r4")
    private String r4;
    @Size(max = 4)
    @Column(name = "r5")
    private String r5;
    @Size(max = 4)
    @Column(name = "r6")
    private String r6;
    @Size(max = 4)
    @Column(name = "r7")
    private String r7;
    @Size(max = 4)
    @Column(name = "r8")
    private String r8;
    @Size(max = 4)
    @Column(name = "r9")
    private String r9;
    @Size(max = 4)
    @Column(name = "r10")
    private String r10;
    @Size(max = 4)
    @Column(name = "r11")
    private String r11;
    @Size(max = 4)
    @Column(name = "r12")
    private String r12;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completo")
    private boolean completo;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionConocimientoCodigoEticaResultados2PK evaluacionConocimientoCodigoEticaResultados2PK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalCorrectas")
    private Integer totalCorrectas;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluador", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;

    public EvaluacionConocimientoCodigoEticaResultados2() {
    }

    public EvaluacionConocimientoCodigoEticaResultados2(EvaluacionConocimientoCodigoEticaResultados2PK evaluacionConocimientoCodigoEticaResultados2PK) {
        this.evaluacionConocimientoCodigoEticaResultados2PK = evaluacionConocimientoCodigoEticaResultados2PK;
    }

    public EvaluacionConocimientoCodigoEticaResultados2(EvaluacionConocimientoCodigoEticaResultados2PK evaluacionConocimientoCodigoEticaResultados2PK, boolean completo) {
        this.evaluacionConocimientoCodigoEticaResultados2PK = evaluacionConocimientoCodigoEticaResultados2PK;
        this.completo = completo;
    }

    public EvaluacionConocimientoCodigoEticaResultados2(int evaluacion, int evaluador) {
        this.evaluacionConocimientoCodigoEticaResultados2PK = new EvaluacionConocimientoCodigoEticaResultados2PK(evaluacion, evaluador);
    }

    public EvaluacionConocimientoCodigoEticaResultados2PK getEvaluacionConocimientoCodigoEticaResultados2PK() {
        return evaluacionConocimientoCodigoEticaResultados2PK;
    }

    public void setEvaluacionConocimientoCodigoEticaResultados2PK(EvaluacionConocimientoCodigoEticaResultados2PK evaluacionConocimientoCodigoEticaResultados2PK) {
        this.evaluacionConocimientoCodigoEticaResultados2PK = evaluacionConocimientoCodigoEticaResultados2PK;
    }


    public Integer getTotalCorrectas() { return totalCorrectas; }

    public void setTotalCorrectas(Integer totalCorrectas) { this.totalCorrectas = totalCorrectas; }

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
        hash += (evaluacionConocimientoCodigoEticaResultados2PK != null ? evaluacionConocimientoCodigoEticaResultados2PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionConocimientoCodigoEticaResultados2)) {
            return false;
        }
        EvaluacionConocimientoCodigoEticaResultados2 other = (EvaluacionConocimientoCodigoEticaResultados2) object;
        if ((this.evaluacionConocimientoCodigoEticaResultados2PK == null && other.evaluacionConocimientoCodigoEticaResultados2PK != null) || (this.evaluacionConocimientoCodigoEticaResultados2PK != null && !this.evaluacionConocimientoCodigoEticaResultados2PK.equals(other.evaluacionConocimientoCodigoEticaResultados2PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionConocimientoCodigoEticaResultados2[ evaluacionConocimientoCodigoEticaResultados2PK=" + evaluacionConocimientoCodigoEticaResultados2PK + " ]";
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

    public boolean getCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }
    
}
