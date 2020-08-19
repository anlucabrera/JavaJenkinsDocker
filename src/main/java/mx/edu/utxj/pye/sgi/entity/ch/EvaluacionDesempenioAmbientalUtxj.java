/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "evaluacion_desempenio_ambiental_utxj", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findAll", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByEvaluacion", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.evaluacionDesempenioAmbientalUtxjPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByEvaluador", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.evaluacionDesempenioAmbientalUtxjPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByFechaElaboracion", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.fechaElaboracion = :fechaElaboracion")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR1", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR11", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR12", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR2", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR21", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r21 = :r21")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR22", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r22 = :r22")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR3", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR31", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r31 = :r31")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR32", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r32 = :r32")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR4", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR41", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r41 = :r41")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR42", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r42 = :r42")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR5", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR51", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r51 = :r51")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR52", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r52 = :r52")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR6", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR61", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r61 = :r61")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR62", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r62 = :r62")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR7", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR71", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r71 = :r71")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR72", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r72 = :r72")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR8", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR81", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r81 = :r81")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR82", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r82 = :r82")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR9", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR91", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r91 = :r91")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR92", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r92 = :r92")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR10", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionDesempenioAmbientalUtxj.findByR111", query = "SELECT e FROM EvaluacionDesempenioAmbientalUtxj e WHERE e.r111 = :r111")})
public class EvaluacionDesempenioAmbientalUtxj implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionDesempenioAmbientalUtxjPK evaluacionDesempenioAmbientalUtxjPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_elaboracion")
    @Temporal(TemporalType.DATE)
    private Date fechaElaboracion;
    @Size(max = 50)
    @Column(name = "r1")
    private String r1;
    @Size(max = 300)
    @Column(name = "r1_1")
    private String r11;
    @Size(max = 300)
    @Column(name = "r1_2")
    private String r12;
    @Size(max = 50)
    @Column(name = "r2")
    private String r2;
    @Size(max = 300)
    @Column(name = "r2_1")
    private String r21;
    @Size(max = 300)
    @Column(name = "r2_2")
    private String r22;
    @Size(max = 50)
    @Column(name = "r3")
    private String r3;
    @Size(max = 300)
    @Column(name = "r3_1")
    private String r31;
    @Size(max = 300)
    @Column(name = "r3_2")
    private String r32;
    @Size(max = 50)
    @Column(name = "r4")
    private String r4;
    @Size(max = 300)
    @Column(name = "r4_1")
    private String r41;
    @Size(max = 300)
    @Column(name = "r4_2")
    private String r42;
    @Size(max = 50)
    @Column(name = "r5")
    private String r5;
    @Size(max = 300)
    @Column(name = "r5_1")
    private String r51;
    @Size(max = 300)
    @Column(name = "r5_2")
    private String r52;
    @Size(max = 50)
    @Column(name = "r6")
    private String r6;
    @Size(max = 300)
    @Column(name = "r6_1")
    private String r61;
    @Size(max = 300)
    @Column(name = "r6_2")
    private String r62;
    @Size(max = 50)
    @Column(name = "r7")
    private String r7;
    @Size(max = 300)
    @Column(name = "r7_1")
    private String r71;
    @Size(max = 300)
    @Column(name = "r7_2")
    private String r72;
    @Size(max = 50)
    @Column(name = "r8")
    private String r8;
    @Size(max = 300)
    @Column(name = "r8_1")
    private String r81;
    @Size(max = 300)
    @Column(name = "r8_2")
    private String r82;
    @Size(max = 50)
    @Column(name = "r9")
    private String r9;
    @Size(max = 300)
    @Column(name = "r9_1")
    private String r91;
    @Size(max = 300)
    @Column(name = "r9_2")
    private String r92;
    @Size(max = 50)
    @Column(name = "r10")
    private String r10;
    @Size(max = 50)
    @Column(name = "r11")
    private String r111;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Evaluaciones evaluaciones;

    public EvaluacionDesempenioAmbientalUtxj() {
    }

    public EvaluacionDesempenioAmbientalUtxj(EvaluacionDesempenioAmbientalUtxjPK evaluacionDesempenioAmbientalUtxjPK) {
        this.evaluacionDesempenioAmbientalUtxjPK = evaluacionDesempenioAmbientalUtxjPK;
    }

    public EvaluacionDesempenioAmbientalUtxj(EvaluacionDesempenioAmbientalUtxjPK evaluacionDesempenioAmbientalUtxjPK, Date fechaElaboracion) {
        this.evaluacionDesempenioAmbientalUtxjPK = evaluacionDesempenioAmbientalUtxjPK;
        this.fechaElaboracion = fechaElaboracion;
    }

    public EvaluacionDesempenioAmbientalUtxj(int evaluacion, int evaluador) {
        this.evaluacionDesempenioAmbientalUtxjPK = new EvaluacionDesempenioAmbientalUtxjPK(evaluacion, evaluador);
    }

    public EvaluacionDesempenioAmbientalUtxjPK getEvaluacionDesempenioAmbientalUtxjPK() {
        return evaluacionDesempenioAmbientalUtxjPK;
    }

    public void setEvaluacionDesempenioAmbientalUtxjPK(EvaluacionDesempenioAmbientalUtxjPK evaluacionDesempenioAmbientalUtxjPK) {
        this.evaluacionDesempenioAmbientalUtxjPK = evaluacionDesempenioAmbientalUtxjPK;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
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

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
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

    public String getR3() {
        return r3;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }

    public String getR31() {
        return r31;
    }

    public void setR31(String r31) {
        this.r31 = r31;
    }

    public String getR32() {
        return r32;
    }

    public void setR32(String r32) {
        this.r32 = r32;
    }

    public String getR4() {
        return r4;
    }

    public void setR4(String r4) {
        this.r4 = r4;
    }

    public String getR41() {
        return r41;
    }

    public void setR41(String r41) {
        this.r41 = r41;
    }

    public String getR42() {
        return r42;
    }

    public void setR42(String r42) {
        this.r42 = r42;
    }

    public String getR5() {
        return r5;
    }

    public void setR5(String r5) {
        this.r5 = r5;
    }

    public String getR51() {
        return r51;
    }

    public void setR51(String r51) {
        this.r51 = r51;
    }

    public String getR52() {
        return r52;
    }

    public void setR52(String r52) {
        this.r52 = r52;
    }

    public String getR6() {
        return r6;
    }

    public void setR6(String r6) {
        this.r6 = r6;
    }

    public String getR61() {
        return r61;
    }

    public void setR61(String r61) {
        this.r61 = r61;
    }

    public String getR62() {
        return r62;
    }

    public void setR62(String r62) {
        this.r62 = r62;
    }

    public String getR7() {
        return r7;
    }

    public void setR7(String r7) {
        this.r7 = r7;
    }

    public String getR71() {
        return r71;
    }

    public void setR71(String r71) {
        this.r71 = r71;
    }

    public String getR72() {
        return r72;
    }

    public void setR72(String r72) {
        this.r72 = r72;
    }

    public String getR8() {
        return r8;
    }

    public void setR8(String r8) {
        this.r8 = r8;
    }

    public String getR81() {
        return r81;
    }

    public void setR81(String r81) {
        this.r81 = r81;
    }

    public String getR82() {
        return r82;
    }

    public void setR82(String r82) {
        this.r82 = r82;
    }

    public String getR9() {
        return r9;
    }

    public void setR9(String r9) {
        this.r9 = r9;
    }

    public String getR91() {
        return r91;
    }

    public void setR91(String r91) {
        this.r91 = r91;
    }

    public String getR92() {
        return r92;
    }

    public void setR92(String r92) {
        this.r92 = r92;
    }

    public String getR10() {
        return r10;
    }

    public void setR10(String r10) {
        this.r10 = r10;
    }

    public String getR111() {
        return r111;
    }

    public void setR111(String r111) {
        this.r111 = r111;
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
        hash += (evaluacionDesempenioAmbientalUtxjPK != null ? evaluacionDesempenioAmbientalUtxjPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionDesempenioAmbientalUtxj)) {
            return false;
        }
        EvaluacionDesempenioAmbientalUtxj other = (EvaluacionDesempenioAmbientalUtxj) object;
        if ((this.evaluacionDesempenioAmbientalUtxjPK == null && other.evaluacionDesempenioAmbientalUtxjPK != null) || (this.evaluacionDesempenioAmbientalUtxjPK != null && !this.evaluacionDesempenioAmbientalUtxjPK.equals(other.evaluacionDesempenioAmbientalUtxjPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDesempenioAmbientalUtxj[ evaluacionDesempenioAmbientalUtxjPK=" + evaluacionDesempenioAmbientalUtxjPK + " ]";
    }
    
}
