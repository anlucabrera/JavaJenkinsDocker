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
 * @author jonny
 */
@Entity
@Table(name = "resultados_encuesta_satisfaccion_tsu", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findAll", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByEvaluacion", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.resultadosEncuestaSatisfaccionTsuPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByEvaluador", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.resultadosEncuestaSatisfaccionTsuPK.evaluador = :evaluador")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR1", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r1 = :r1")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR2", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r2 = :r2")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR3", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r3 = :r3")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR4", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r4 = :r4")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR5", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r5 = :r5")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR6", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r6 = :r6")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR7", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r7 = :r7")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR8", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r8 = :r8")
    , @NamedQuery(name = "ResultadosEncuestaSatisfaccionTsu.findByR9", query = "SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.r9 = :r9")})
public class ResultadosEncuestaSatisfaccionTsu implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ResultadosEncuestaSatisfaccionTsuPK resultadosEncuestaSatisfaccionTsuPK;
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

    public ResultadosEncuestaSatisfaccionTsu() {
    }

    public ResultadosEncuestaSatisfaccionTsu(ResultadosEncuestaSatisfaccionTsuPK resultadosEncuestaSatisfaccionTsuPK) {
        this.resultadosEncuestaSatisfaccionTsuPK = resultadosEncuestaSatisfaccionTsuPK;
    }

    public ResultadosEncuestaSatisfaccionTsu(int evaluacion, int evaluador) {
        this.resultadosEncuestaSatisfaccionTsuPK = new ResultadosEncuestaSatisfaccionTsuPK(evaluacion, evaluador);
    }

    public ResultadosEncuestaSatisfaccionTsuPK getResultadosEncuestaSatisfaccionTsuPK() {
        return resultadosEncuestaSatisfaccionTsuPK;
    }

    public void setResultadosEncuestaSatisfaccionTsuPK(ResultadosEncuestaSatisfaccionTsuPK resultadosEncuestaSatisfaccionTsuPK) {
        this.resultadosEncuestaSatisfaccionTsuPK = resultadosEncuestaSatisfaccionTsuPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resultadosEncuestaSatisfaccionTsuPK != null ? resultadosEncuestaSatisfaccionTsuPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResultadosEncuestaSatisfaccionTsu)) {
            return false;
        }
        ResultadosEncuestaSatisfaccionTsu other = (ResultadosEncuestaSatisfaccionTsu) object;
        if ((this.resultadosEncuestaSatisfaccionTsuPK == null && other.resultadosEncuestaSatisfaccionTsuPK != null) || (this.resultadosEncuestaSatisfaccionTsuPK != null && !this.resultadosEncuestaSatisfaccionTsuPK.equals(other.resultadosEncuestaSatisfaccionTsuPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu[ resultadosEncuestaSatisfaccionTsuPK=" + resultadosEncuestaSatisfaccionTsuPK + " ]";
    }
    
}
