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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "evaluaciones_clima_laboral_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findAll", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByEvaluacion", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.evaluacionesClimaLaboralResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByEvaluador", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.evaluacionesClimaLaboralResultadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR1", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR2", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR3", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR4", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR5", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR61", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r61 = :r61")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR62", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r62 = :r62")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR7", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR8", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR81", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r81 = :r81")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR9", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR10", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR11", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR12", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR13", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR14", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR15", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR16", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR17", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR18", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR19", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r19 = :r19")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR20", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r20 = :r20")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR21", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r21 = :r21")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR22", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r22 = :r22")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR23", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r23 = :r23")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR24", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r24 = :r24")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR25", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r25 = :r25")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR26", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r26 = :r26")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR27", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r27 = :r27")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR28", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r28 = :r28")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR29", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r29 = :r29")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR30", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r30 = :r30")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR31", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r31 = :r31")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR32", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r32 = :r32")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR33", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r33 = :r33")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR34", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r34 = :r34")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR35", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r35 = :r35")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR36", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r36 = :r36")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR37", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r37 = :r37")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR38", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r38 = :r38")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR39", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r39 = :r39")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR40", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r40 = :r40")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR41", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r41 = :r41")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR42", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r42 = :r42")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR43", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r43 = :r43")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR44", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r44 = :r44")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR45", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r45 = :r45")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR46", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r46 = :r46")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR47", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r47 = :r47")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR48", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r48 = :r48")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR49", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r49 = :r49")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR50", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r50 = :r50")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR51", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r51 = :r51")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR52", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r52 = :r52")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR53", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r53 = :r53")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR54", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r54 = :r54")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR55", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r55 = :r55")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR56", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r56 = :r56")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR57", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r57 = :r57")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR58", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r58 = :r58")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR59", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r59 = :r59")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR60", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r60 = :r60")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR611", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r611 = :r611")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR621", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r621 = :r621")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR63", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r63 = :r63")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR64", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r64 = :r64")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR65", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r65 = :r65")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR66", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r66 = :r66")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR67", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r67 = :r67")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR68", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r68 = :r68")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR69", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r69 = :r69")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR70", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r70 = :r70")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR71", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r71 = :r71")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR72", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r72 = :r72")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR73", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r73 = :r73")
    , @NamedQuery(name = "EvaluacionesClimaLaboralResultados.findByR74", query = "SELECT e FROM EvaluacionesClimaLaboralResultados e WHERE e.r74 = :r74")})
public class EvaluacionesClimaLaboralResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionesClimaLaboralResultadosPK evaluacionesClimaLaboralResultadosPK;
    @Size(max = 50)
    @Column(name = "r1")
    private String r1;
    @Size(max = 50)
    @Column(name = "r2")
    private String r2;
    @Size(max = 50)
    @Column(name = "r3")
    private String r3;
    @Size(max = 50)
    @Column(name = "r4")
    private String r4;
    @Size(max = 50)
    @Column(name = "r5")
    private String r5;
    @Column(name = "r6_1")
    private Short r61;
    @Column(name = "r6_2")
    private Short r62;
    @Size(max = 50)
    @Column(name = "r7")
    private String r7;
    @Size(max = 50)
    @Column(name = "r8")
    private String r8;
    @Size(max = 50)
    @Column(name = "r8_1")
    private String r81;
    @Size(max = 50)
    @Column(name = "r9")
    private String r9;
    @Size(max = 50)
    @Column(name = "r10")
    private String r10;
    @Size(max = 50)
    @Column(name = "r11")
    private String r11;
    @Size(max = 50)
    @Column(name = "r12")
    private String r12;
    @Size(max = 50)
    @Column(name = "r13")
    private String r13;
    @Size(max = 50)
    @Column(name = "r14")
    private String r14;
    @Size(max = 50)
    @Column(name = "r15")
    private String r15;
    @Size(max = 50)
    @Column(name = "r16")
    private String r16;
    @Size(max = 50)
    @Column(name = "r17")
    private String r17;
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
    @Column(name = "r37")
    private Short r37;
    @Column(name = "r38")
    private Short r38;
    @Column(name = "r39")
    private Short r39;
    @Column(name = "r40")
    private Short r40;
    @Column(name = "r41")
    private Short r41;
    @Column(name = "r42")
    private Short r42;
    @Column(name = "r43")
    private Short r43;
    @Column(name = "r44")
    private Short r44;
    @Column(name = "r45")
    private Short r45;
    @Column(name = "r46")
    private Short r46;
    @Column(name = "r47")
    private Short r47;
    @Column(name = "r48")
    private Short r48;
    @Column(name = "r49")
    private Short r49;
    @Column(name = "r50")
    private Short r50;
    @Column(name = "r51")
    private Short r51;
    @Column(name = "r52")
    private Short r52;
    @Column(name = "r53")
    private Short r53;
    @Column(name = "r54")
    private Short r54;
    @Column(name = "r55")
    private Short r55;
    @Column(name = "r56")
    private Short r56;
    @Column(name = "r57")
    private Short r57;
    @Column(name = "r58")
    private Short r58;
    @Column(name = "r59")
    private Short r59;
    @Column(name = "r60")
    private Short r60;
    @Column(name = "r61")
    private Short r611;
    @Column(name = "r62")
    private Short r621;
    @Column(name = "r63")
    private Short r63;
    @Column(name = "r64")
    private Short r64;
    @Column(name = "r65")
    private Short r65;
    @Column(name = "r66")
    private Short r66;
    @Column(name = "r67")
    private Short r67;
    @Column(name = "r68")
    private Short r68;
    @Column(name = "r69")
    private Short r69;
    @Column(name = "r70")
    private Short r70;
    @Column(name = "r71")
    private Short r71;
    @Column(name = "r72")
    private Short r72;
    @Column(name = "r73")
    private Short r73;
    @Column(name = "r74")
    private Short r74;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluador", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Personal personal;

    public EvaluacionesClimaLaboralResultados() {
    }

    public EvaluacionesClimaLaboralResultados(EvaluacionesClimaLaboralResultadosPK evaluacionesClimaLaboralResultadosPK) {
        this.evaluacionesClimaLaboralResultadosPK = evaluacionesClimaLaboralResultadosPK;
    }

    public EvaluacionesClimaLaboralResultados(int evaluacion, int evaluador) {
        this.evaluacionesClimaLaboralResultadosPK = new EvaluacionesClimaLaboralResultadosPK(evaluacion, evaluador);
    }

    public EvaluacionesClimaLaboralResultadosPK getEvaluacionesClimaLaboralResultadosPK() {
        return evaluacionesClimaLaboralResultadosPK;
    }

    public void setEvaluacionesClimaLaboralResultadosPK(EvaluacionesClimaLaboralResultadosPK evaluacionesClimaLaboralResultadosPK) {
        this.evaluacionesClimaLaboralResultadosPK = evaluacionesClimaLaboralResultadosPK;
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

    public Short getR61() {
        return r61;
    }

    public void setR61(Short r61) {
        this.r61 = r61;
    }

    public Short getR62() {
        return r62;
    }

    public void setR62(Short r62) {
        this.r62 = r62;
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

    public String getR81() {
        return r81;
    }

    public void setR81(String r81) {
        this.r81 = r81;
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

    public Short getR37() {
        return r37;
    }

    public void setR37(Short r37) {
        this.r37 = r37;
    }

    public Short getR38() {
        return r38;
    }

    public void setR38(Short r38) {
        this.r38 = r38;
    }

    public Short getR39() {
        return r39;
    }

    public void setR39(Short r39) {
        this.r39 = r39;
    }

    public Short getR40() {
        return r40;
    }

    public void setR40(Short r40) {
        this.r40 = r40;
    }

    public Short getR41() {
        return r41;
    }

    public void setR41(Short r41) {
        this.r41 = r41;
    }

    public Short getR42() {
        return r42;
    }

    public void setR42(Short r42) {
        this.r42 = r42;
    }

    public Short getR43() {
        return r43;
    }

    public void setR43(Short r43) {
        this.r43 = r43;
    }

    public Short getR44() {
        return r44;
    }

    public void setR44(Short r44) {
        this.r44 = r44;
    }

    public Short getR45() {
        return r45;
    }

    public void setR45(Short r45) {
        this.r45 = r45;
    }

    public Short getR46() {
        return r46;
    }

    public void setR46(Short r46) {
        this.r46 = r46;
    }

    public Short getR47() {
        return r47;
    }

    public void setR47(Short r47) {
        this.r47 = r47;
    }

    public Short getR48() {
        return r48;
    }

    public void setR48(Short r48) {
        this.r48 = r48;
    }

    public Short getR49() {
        return r49;
    }

    public void setR49(Short r49) {
        this.r49 = r49;
    }

    public Short getR50() {
        return r50;
    }

    public void setR50(Short r50) {
        this.r50 = r50;
    }

    public Short getR51() {
        return r51;
    }

    public void setR51(Short r51) {
        this.r51 = r51;
    }

    public Short getR52() {
        return r52;
    }

    public void setR52(Short r52) {
        this.r52 = r52;
    }

    public Short getR53() {
        return r53;
    }

    public void setR53(Short r53) {
        this.r53 = r53;
    }

    public Short getR54() {
        return r54;
    }

    public void setR54(Short r54) {
        this.r54 = r54;
    }

    public Short getR55() {
        return r55;
    }

    public void setR55(Short r55) {
        this.r55 = r55;
    }

    public Short getR56() {
        return r56;
    }

    public void setR56(Short r56) {
        this.r56 = r56;
    }

    public Short getR57() {
        return r57;
    }

    public void setR57(Short r57) {
        this.r57 = r57;
    }

    public Short getR58() {
        return r58;
    }

    public void setR58(Short r58) {
        this.r58 = r58;
    }

    public Short getR59() {
        return r59;
    }

    public void setR59(Short r59) {
        this.r59 = r59;
    }

    public Short getR60() {
        return r60;
    }

    public void setR60(Short r60) {
        this.r60 = r60;
    }

    public Short getR611() {
        return r611;
    }

    public void setR611(Short r611) {
        this.r611 = r611;
    }

    public Short getR621() {
        return r621;
    }

    public void setR621(Short r621) {
        this.r621 = r621;
    }

    public Short getR63() {
        return r63;
    }

    public void setR63(Short r63) {
        this.r63 = r63;
    }

    public Short getR64() {
        return r64;
    }

    public void setR64(Short r64) {
        this.r64 = r64;
    }

    public Short getR65() {
        return r65;
    }

    public void setR65(Short r65) {
        this.r65 = r65;
    }

    public Short getR66() {
        return r66;
    }

    public void setR66(Short r66) {
        this.r66 = r66;
    }

    public Short getR67() {
        return r67;
    }

    public void setR67(Short r67) {
        this.r67 = r67;
    }

    public Short getR68() {
        return r68;
    }

    public void setR68(Short r68) {
        this.r68 = r68;
    }

    public Short getR69() {
        return r69;
    }

    public void setR69(Short r69) {
        this.r69 = r69;
    }

    public Short getR70() {
        return r70;
    }

    public void setR70(Short r70) {
        this.r70 = r70;
    }

    public Short getR71() {
        return r71;
    }

    public void setR71(Short r71) {
        this.r71 = r71;
    }

    public Short getR72() {
        return r72;
    }

    public void setR72(Short r72) {
        this.r72 = r72;
    }

    public Short getR73() {
        return r73;
    }

    public void setR73(Short r73) {
        this.r73 = r73;
    }

    public Short getR74() {
        return r74;
    }

    public void setR74(Short r74) {
        this.r74 = r74;
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
        hash += (evaluacionesClimaLaboralResultadosPK != null ? evaluacionesClimaLaboralResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionesClimaLaboralResultados)) {
            return false;
        }
        EvaluacionesClimaLaboralResultados other = (EvaluacionesClimaLaboralResultados) object;
        if ((this.evaluacionesClimaLaboralResultadosPK == null && other.evaluacionesClimaLaboralResultadosPK != null) || (this.evaluacionesClimaLaboralResultadosPK != null && !this.evaluacionesClimaLaboralResultadosPK.equals(other.evaluacionesClimaLaboralResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesClimaLaboralResultados[ evaluacionesClimaLaboralResultadosPK=" + evaluacionesClimaLaboralResultadosPK + " ]";
    }
    
}
