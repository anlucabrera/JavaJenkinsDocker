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
 * @author Desarrollo
 */
@Entity
@Table(name = "evaluacion_tutores_resultados_2", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionTutoresResultados2.findAll", query = "SELECT e FROM EvaluacionTutoresResultados2 e")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByEvaluacion", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.evaluacionTutoresResultados2PK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByEvaluador", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.evaluacionTutoresResultados2PK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByEvaluado", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.evaluacionTutoresResultados2PK.evaluado = :evaluado")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR1", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR2", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR3", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR4", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR5", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR6", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR7", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByR8", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionTutoresResultados2.findByCompleto", query = "SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.completo = :completo")})
public class EvaluacionTutoresResultados2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionTutoresResultados2PK evaluacionTutoresResultados2PK;
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
    @Column(name = "completo")
    private Boolean completo;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluado", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;

    public EvaluacionTutoresResultados2() {
    }

    public EvaluacionTutoresResultados2(EvaluacionTutoresResultados2PK evaluacionTutoresResultados2PK) {
        this.evaluacionTutoresResultados2PK = evaluacionTutoresResultados2PK;
    }

    public EvaluacionTutoresResultados2(int evaluacion, int evaluador, int evaluado) {
        this.evaluacionTutoresResultados2PK = new EvaluacionTutoresResultados2PK(evaluacion, evaluador, evaluado);
    }

    public EvaluacionTutoresResultados2PK getEvaluacionTutoresResultados2PK() {
        return evaluacionTutoresResultados2PK;
    }

    public void setEvaluacionTutoresResultados2PK(EvaluacionTutoresResultados2PK evaluacionTutoresResultados2PK) {
        this.evaluacionTutoresResultados2PK = evaluacionTutoresResultados2PK;
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

    public Boolean getCompleto() {
        return completo;
    }

    public void setCompleto(Boolean completo) {
        this.completo = completo;
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
        hash += (evaluacionTutoresResultados2PK != null ? evaluacionTutoresResultados2PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionTutoresResultados2)) {
            return false;
        }
        EvaluacionTutoresResultados2 other = (EvaluacionTutoresResultados2) object;
        if ((this.evaluacionTutoresResultados2PK == null && other.evaluacionTutoresResultados2PK != null) || (this.evaluacionTutoresResultados2PK != null && !this.evaluacionTutoresResultados2PK.equals(other.evaluacionTutoresResultados2PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados2[ evaluacionTutoresResultados2PK=" + evaluacionTutoresResultados2PK + " ]";
    }
    
}
