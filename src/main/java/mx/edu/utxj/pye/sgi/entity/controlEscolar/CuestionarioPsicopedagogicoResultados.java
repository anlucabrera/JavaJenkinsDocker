/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

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
@Table(name = "cuestionario_psicopedagogico_resultados", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findAll", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByEvaluacion", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.cuestionarioPsicopedagogicoResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByIdEstudiante", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.cuestionarioPsicopedagogicoResultadosPK.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByClave", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.clave = :clave")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR1", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r1 = :r1")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR2", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r2 = :r2")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR3", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r3 = :r3")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR4", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r4 = :r4")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR5", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r5 = :r5")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR6", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r6 = :r6")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR7", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r7 = :r7")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR8", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r8 = :r8")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR9", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r9 = :r9")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR10", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r10 = :r10")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR11", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r11 = :r11")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR12", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r12 = :r12")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR13", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r13 = :r13")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR14", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r14 = :r14")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR15", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r15 = :r15")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR16", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r16 = :r16")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR17", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r17 = :r17")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR18", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r18 = :r18")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR19", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r19 = :r19")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR20", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r20 = :r20")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR21", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r21 = :r21")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR22", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r22 = :r22")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR23", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r23 = :r23")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR24", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r24 = :r24")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR25", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r25 = :r25")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR26", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r26 = :r26")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR27", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r27 = :r27")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR28", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r28 = :r28")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR29", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r29 = :r29")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR30", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r30 = :r30")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR31", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r31 = :r31")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR32", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r32 = :r32")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR33", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r33 = :r33")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR34", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r34 = :r34")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR35", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r35 = :r35")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR36", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r36 = :r36")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR37", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r37 = :r37")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR38", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r38 = :r38")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR39", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r39 = :r39")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR40", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r40 = :r40")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR41", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r41 = :r41")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR42", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r42 = :r42")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR43", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r43 = :r43")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR44", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r44 = :r44")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR45", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r45 = :r45")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR46", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r46 = :r46")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR47", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r47 = :r47")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR48", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r48 = :r48")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR49", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r49 = :r49")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR50", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r50 = :r50")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR51", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r51 = :r51")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR52", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r52 = :r52")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByR53", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.r53 = :r53")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByCompleto", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.completo = :completo")
    , @NamedQuery(name = "CuestionarioPsicopedagogicoResultados.findByReviso", query = "SELECT c FROM CuestionarioPsicopedagogicoResultados c WHERE c.reviso = :reviso")})
public class CuestionarioPsicopedagogicoResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CuestionarioPsicopedagogicoResultadosPK cuestionarioPsicopedagogicoResultadosPK;
    @Column(name = "clave")
    private Integer clave;
    @Size(max = 5)
    @Column(name = "r1")
    private String r1;
    @Size(max = 1000)
    @Column(name = "r2")
    private String r2;
    @Size(max = 1000)
    @Column(name = "r3")
    private String r3;
    @Size(max = 1000)
    @Column(name = "r4")
    private String r4;
    @Size(max = 1000)
    @Column(name = "r5")
    private String r5;
    @Size(max = 1000)
    @Column(name = "r6")
    private String r6;
    @Size(max = 1000)
    @Column(name = "r7")
    private String r7;
    @Size(max = 5)
    @Column(name = "r8")
    private String r8;
    @Size(max = 5)
    @Column(name = "r9")
    private String r9;
    @Size(max = 1000)
    @Column(name = "r10")
    private String r10;
    @Size(max = 1000)
    @Column(name = "r11")
    private String r11;
    @Size(max = 1000)
    @Column(name = "r12")
    private String r12;
    @Size(max = 1000)
    @Column(name = "r13")
    private String r13;
    @Size(max = 1000)
    @Column(name = "r14")
    private String r14;
    @Size(max = 1000)
    @Column(name = "r15")
    private String r15;
    @Size(max = 50)
    @Column(name = "r16")
    private String r16;
    @Size(max = 50)
    @Column(name = "r17")
    private String r17;
    @Size(max = 1000)
    @Column(name = "r18")
    private String r18;
    @Size(max = 5)
    @Column(name = "r19")
    private String r19;
    @Size(max = 50)
    @Column(name = "r20")
    private String r20;
    @Size(max = 5)
    @Column(name = "r21")
    private String r21;
    @Size(max = 1000)
    @Column(name = "r22")
    private String r22;
    @Size(max = 5)
    @Column(name = "r23")
    private String r23;
    @Size(max = 500)
    @Column(name = "r24")
    private String r24;
    @Size(max = 5)
    @Column(name = "r25")
    private String r25;
    @Size(max = 50)
    @Column(name = "r26")
    private String r26;
    @Size(max = 50)
    @Column(name = "r27")
    private String r27;
    @Size(max = 50)
    @Column(name = "r28")
    private String r28;
    @Size(max = 50)
    @Column(name = "r29")
    private String r29;
    @Size(max = 50)
    @Column(name = "r30")
    private String r30;
    @Size(max = 50)
    @Column(name = "r31")
    private String r31;
    @Size(max = 50)
    @Column(name = "r32")
    private String r32;
    @Size(max = 50)
    @Column(name = "r33")
    private String r33;
    @Size(max = 50)
    @Column(name = "r34")
    private String r34;
    @Size(max = 50)
    @Column(name = "r35")
    private String r35;
    @Size(max = 50)
    @Column(name = "r36")
    private String r36;
    @Size(max = 50)
    @Column(name = "r37")
    private String r37;
    @Size(max = 50)
    @Column(name = "r38")
    private String r38;
    @Size(max = 50)
    @Column(name = "r39")
    private String r39;
    @Size(max = 50)
    @Column(name = "r40")
    private String r40;
    @Size(max = 50)
    @Column(name = "r41")
    private String r41;
    @Size(max = 50)
    @Column(name = "r42")
    private String r42;
    @Size(max = 5)
    @Column(name = "r43")
    private String r43;
    @Size(max = 500)
    @Column(name = "r44")
    private String r44;
    @Size(max = 5)
    @Column(name = "r45")
    private String r45;
    @Size(max = 5)
    @Column(name = "r46")
    private String r46;
    @Size(max = 5)
    @Column(name = "r47")
    private String r47;
    @Size(max = 5)
    @Column(name = "r48")
    private String r48;
    @Size(max = 5)
    @Column(name = "r49")
    private String r49;
    @Size(max = 5)
    @Column(name = "r50")
    private String r50;
    @Size(max = 5)
    @Column(name = "r51")
    private String r51;
    @Size(max = 5)
    @Column(name = "r52")
    private String r52;
    @Size(max = 500)
    @Column(name = "r53")
    private String r53;
    @Column(name = "completo")
    private Boolean completo;
    @Column(name = "reviso")
    private Boolean reviso;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante estudiante;

    public CuestionarioPsicopedagogicoResultados() {
    }

    public CuestionarioPsicopedagogicoResultados(CuestionarioPsicopedagogicoResultadosPK cuestionarioPsicopedagogicoResultadosPK) {
        this.cuestionarioPsicopedagogicoResultadosPK = cuestionarioPsicopedagogicoResultadosPK;
    }

    public CuestionarioPsicopedagogicoResultados(int evaluacion, int idEstudiante) {
        this.cuestionarioPsicopedagogicoResultadosPK = new CuestionarioPsicopedagogicoResultadosPK(evaluacion, idEstudiante);
    }

    public CuestionarioPsicopedagogicoResultadosPK getCuestionarioPsicopedagogicoResultadosPK() {
        return cuestionarioPsicopedagogicoResultadosPK;
    }

    public void setCuestionarioPsicopedagogicoResultadosPK(CuestionarioPsicopedagogicoResultadosPK cuestionarioPsicopedagogicoResultadosPK) {
        this.cuestionarioPsicopedagogicoResultadosPK = cuestionarioPsicopedagogicoResultadosPK;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
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

    public String getR28() {
        return r28;
    }

    public void setR28(String r28) {
        this.r28 = r28;
    }

    public String getR29() {
        return r29;
    }

    public void setR29(String r29) {
        this.r29 = r29;
    }

    public String getR30() {
        return r30;
    }

    public void setR30(String r30) {
        this.r30 = r30;
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

    public String getR33() {
        return r33;
    }

    public void setR33(String r33) {
        this.r33 = r33;
    }

    public String getR34() {
        return r34;
    }

    public void setR34(String r34) {
        this.r34 = r34;
    }

    public String getR35() {
        return r35;
    }

    public void setR35(String r35) {
        this.r35 = r35;
    }

    public String getR36() {
        return r36;
    }

    public void setR36(String r36) {
        this.r36 = r36;
    }

    public String getR37() {
        return r37;
    }

    public void setR37(String r37) {
        this.r37 = r37;
    }

    public String getR38() {
        return r38;
    }

    public void setR38(String r38) {
        this.r38 = r38;
    }

    public String getR39() {
        return r39;
    }

    public void setR39(String r39) {
        this.r39 = r39;
    }

    public String getR40() {
        return r40;
    }

    public void setR40(String r40) {
        this.r40 = r40;
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

    public String getR43() {
        return r43;
    }

    public void setR43(String r43) {
        this.r43 = r43;
    }

    public String getR44() {
        return r44;
    }

    public void setR44(String r44) {
        this.r44 = r44;
    }

    public String getR45() {
        return r45;
    }

    public void setR45(String r45) {
        this.r45 = r45;
    }

    public String getR46() {
        return r46;
    }

    public void setR46(String r46) {
        this.r46 = r46;
    }

    public String getR47() {
        return r47;
    }

    public void setR47(String r47) {
        this.r47 = r47;
    }

    public String getR48() {
        return r48;
    }

    public void setR48(String r48) {
        this.r48 = r48;
    }

    public String getR49() {
        return r49;
    }

    public void setR49(String r49) {
        this.r49 = r49;
    }

    public String getR50() {
        return r50;
    }

    public void setR50(String r50) {
        this.r50 = r50;
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

    public String getR53() {
        return r53;
    }

    public void setR53(String r53) {
        this.r53 = r53;
    }

    public Boolean getCompleto() {
        return completo;
    }

    public void setCompleto(Boolean completo) {
        this.completo = completo;
    }

    public Boolean getReviso() {
        return reviso;
    }

    public void setReviso(Boolean reviso) {
        this.reviso = reviso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuestionarioPsicopedagogicoResultadosPK != null ? cuestionarioPsicopedagogicoResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuestionarioPsicopedagogicoResultados)) {
            return false;
        }
        CuestionarioPsicopedagogicoResultados other = (CuestionarioPsicopedagogicoResultados) object;
        if ((this.cuestionarioPsicopedagogicoResultadosPK == null && other.cuestionarioPsicopedagogicoResultadosPK != null) || (this.cuestionarioPsicopedagogicoResultadosPK != null && !this.cuestionarioPsicopedagogicoResultadosPK.equals(other.cuestionarioPsicopedagogicoResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados[ cuestionarioPsicopedagogicoResultadosPK=" + cuestionarioPsicopedagogicoResultadosPK + " ]";
    }
    
}
