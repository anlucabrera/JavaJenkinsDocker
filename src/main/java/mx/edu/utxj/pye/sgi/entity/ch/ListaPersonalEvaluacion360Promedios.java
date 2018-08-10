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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "lista_personal_evaluacion_360_promedios", catalog = "capital_humano", schema = "")
@XmlRootElement @RequiredArgsConstructor @EqualsAndHashCode(of = "pk")
@NamedQueries({
    @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findAll", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByAnio", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.anio = :anio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByPeriodo", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByMesInicio", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.mesInicio = :mesInicio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByMesFin", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.mesFin = :mesFin")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluacion", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.pk.evaluacion = :evaluacion")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByFechaInicio", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByFechaFin", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.fechaFin = :fechaFin")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluado", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.pk.evaluado = :evaluado")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoNombre = :evaluadoNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoAreaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoAreaOperativa = :evaluadoAreaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoAreaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoAreaOperativaNombre = :evaluadoAreaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoCategoriaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoCategoriaOperativa = :evaluadoCategoriaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoCategoriaOperativaNombre = :evaluadoCategoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoAreaSuperior", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoAreaSuperior = :evaluadoAreaSuperior")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoAreaSuperiorNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoAreaSuperiorNombre = :evaluadoAreaSuperiorNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoActividad", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoActividad = :evaluadoActividad")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoActividadNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoActividadNombre = :evaluadoActividadNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoFechaIngreso", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoFechaIngreso = :evaluadoFechaIngreso")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByEvaluadoGeneroNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.evaluadoGeneroNombre = :evaluadoGeneroNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByTipo", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.tipo = :tipo")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByCategoria", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.categoria = :categoria")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByCategoriaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.categoriaNombre = :categoriaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR1", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r1 = :r1")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR2", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r2 = :r2")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR3", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r3 = :r3")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR4", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r4 = :r4")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR5", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r5 = :r5")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR6", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r6 = :r6")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR7", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r7 = :r7")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR8", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r8 = :r8")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR9", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r9 = :r9")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR10", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r10 = :r10")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR11", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r11 = :r11")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR12", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r12 = :r12")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR13", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r13 = :r13")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR14", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r14 = :r14")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR15", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r15 = :r15")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR16", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r16 = :r16")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR17", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r17 = :r17")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR18", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r18 = :r18")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR19", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r19 = :r19")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR20", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r20 = :r20")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR21", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r21 = :r21")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR22", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r22 = :r22")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR23", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r23 = :r23")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR24", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r24 = :r24")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR25", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r25 = :r25")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR26", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r26 = :r26")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR27", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r27 = :r27")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR28", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r28 = :r28")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR29", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r29 = :r29")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR30", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r30 = :r30")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByR31", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.r31 = :r31")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Promedios.findByPromedio", query = "SELECT l FROM ListaPersonalEvaluacion360Promedios l WHERE l.promedio = :promedio")})
public class ListaPersonalEvaluacion360Promedios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @NonNull private EvaluacionEvaluadoPK pk; 
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private int anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mes_inicio")
    private String mesInicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mes_fin")
    private String mesFin;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Lob
    @Size(max = 65535)
    @Column(name = "evaluadores")
    private String evaluadores;
    @Lob
    @Size(max = 65535)
    @Column(name = "evaluadores_nombre")
    private String evaluadoresNombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "evaluado_nombre")
    private String evaluadoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_area_operativa")
    private int evaluadoAreaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluado_area_operativa_nombre")
    private String evaluadoAreaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_categoria_operativa")
    private short evaluadoCategoriaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "evaluado_categoria_operativa_nombre")
    private String evaluadoCategoriaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_area_superior")
    private int evaluadoAreaSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluado_area_superior_nombre")
    private String evaluadoAreaSuperiorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_actividad")
    private short evaluadoActividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "evaluado_actividad_nombre")
    private String evaluadoActividadNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date evaluadoFechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "evaluado_genero_nombre")
    private String evaluadoGeneroNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 41)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria")
    private short categoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "categoria_nombre")
    private String categoriaNombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "r1")
    private Double r1;
    @Column(name = "r2")
    private Double r2;
    @Column(name = "r3")
    private Double r3;
    @Column(name = "r4")
    private Double r4;
    @Column(name = "r5")
    private Double r5;
    @Column(name = "r6")
    private Double r6;
    @Column(name = "r7")
    private Double r7;
    @Column(name = "r8")
    private Double r8;
    @Column(name = "r9")
    private Double r9;
    @Column(name = "r10")
    private Double r10;
    @Column(name = "r11")
    private Double r11;
    @Column(name = "r12")
    private Double r12;
    @Column(name = "r13")
    private Double r13;
    @Column(name = "r14")
    private Double r14;
    @Column(name = "r15")
    private Double r15;
    @Column(name = "r16")
    private Double r16;
    @Column(name = "r17")
    private Double r17;
    @Column(name = "r18")
    private Double r18;
    @Column(name = "r19")
    private Double r19;
    @Column(name = "r20")
    private Double r20;
    @Column(name = "r21")
    private Double r21;
    @Column(name = "r22")
    private Double r22;
    @Column(name = "r23")
    private Double r23;
    @Column(name = "r24")
    private Double r24;
    @Column(name = "r25")
    private Double r25;
    @Column(name = "r26")
    private Double r26;
    @Column(name = "r27")
    private Double r27;
    @Column(name = "r28")
    private Double r28;
    @Column(name = "r29")
    private Double r29;
    @Column(name = "r30")
    private Double r30;
    @Column(name = "r31")
    private Double r31;
    @Lob
    @Size(max = 65535)
    @Column(name = "r32")
    private String r32;
    @Lob
    @Size(max = 65535)
    @Column(name = "r33")
    private String r33;
    @Lob
    @Size(max = 65535)
    @Column(name = "incompleto")
    private String incompleto;
    @Lob
    @Size(max = 65535)
    @Column(name = "completo")
    private String completo;
    @Column(name = "promedio")
    private Double promedio;

    public ListaPersonalEvaluacion360Promedios() {
}

    public EvaluacionEvaluadoPK getPk() {
        return pk;
    }

    public void setPk(EvaluacionEvaluadoPK pk) {
        this.pk = pk;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getMesFin() {
        return mesFin;
    }

    public void setMesFin(String mesFin) {
        this.mesFin = mesFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEvaluadores() {
        return evaluadores;
    }

    public void setEvaluadores(String evaluadores) {
        this.evaluadores = evaluadores;
    }

    public String getEvaluadoresNombre() {
        return evaluadoresNombre;
    }

    public void setEvaluadoresNombre(String evaluadoresNombre) {
        this.evaluadoresNombre = evaluadoresNombre;
    }

    public String getEvaluadoNombre() {
        return evaluadoNombre;
    }

    public void setEvaluadoNombre(String evaluadoNombre) {
        this.evaluadoNombre = evaluadoNombre;
    }

    public int getEvaluadoAreaOperativa() {
        return evaluadoAreaOperativa;
    }

    public void setEvaluadoAreaOperativa(int evaluadoAreaOperativa) {
        this.evaluadoAreaOperativa = evaluadoAreaOperativa;
    }

    public String getEvaluadoAreaOperativaNombre() {
        return evaluadoAreaOperativaNombre;
    }

    public void setEvaluadoAreaOperativaNombre(String evaluadoAreaOperativaNombre) {
        this.evaluadoAreaOperativaNombre = evaluadoAreaOperativaNombre;
    }

    public short getEvaluadoCategoriaOperativa() {
        return evaluadoCategoriaOperativa;
    }

    public void setEvaluadoCategoriaOperativa(short evaluadoCategoriaOperativa) {
        this.evaluadoCategoriaOperativa = evaluadoCategoriaOperativa;
    }

    public String getEvaluadoCategoriaOperativaNombre() {
        return evaluadoCategoriaOperativaNombre;
    }

    public void setEvaluadoCategoriaOperativaNombre(String evaluadoCategoriaOperativaNombre) {
        this.evaluadoCategoriaOperativaNombre = evaluadoCategoriaOperativaNombre;
    }

    public int getEvaluadoAreaSuperior() {
        return evaluadoAreaSuperior;
    }

    public void setEvaluadoAreaSuperior(int evaluadoAreaSuperior) {
        this.evaluadoAreaSuperior = evaluadoAreaSuperior;
    }

    public String getEvaluadoAreaSuperiorNombre() {
        return evaluadoAreaSuperiorNombre;
    }

    public void setEvaluadoAreaSuperiorNombre(String evaluadoAreaSuperiorNombre) {
        this.evaluadoAreaSuperiorNombre = evaluadoAreaSuperiorNombre;
    }

    public short getEvaluadoActividad() {
        return evaluadoActividad;
    }

    public void setEvaluadoActividad(short evaluadoActividad) {
        this.evaluadoActividad = evaluadoActividad;
    }

    public String getEvaluadoActividadNombre() {
        return evaluadoActividadNombre;
    }

    public void setEvaluadoActividadNombre(String evaluadoActividadNombre) {
        this.evaluadoActividadNombre = evaluadoActividadNombre;
    }

    public Date getEvaluadoFechaIngreso() {
        return evaluadoFechaIngreso;
    }

    public void setEvaluadoFechaIngreso(Date evaluadoFechaIngreso) {
        this.evaluadoFechaIngreso = evaluadoFechaIngreso;
    }

    public String getEvaluadoGeneroNombre() {
        return evaluadoGeneroNombre;
    }

    public void setEvaluadoGeneroNombre(String evaluadoGeneroNombre) {
        this.evaluadoGeneroNombre = evaluadoGeneroNombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public short getCategoria() {
        return categoria;
    }

    public void setCategoria(short categoria) {
        this.categoria = categoria;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Double getR1() {
        return r1;
    }

    public void setR1(Double r1) {
        this.r1 = r1;
    }

    public Double getR2() {
        return r2;
    }

    public void setR2(Double r2) {
        this.r2 = r2;
    }

    public Double getR3() {
        return r3;
    }

    public void setR3(Double r3) {
        this.r3 = r3;
    }

    public Double getR4() {
        return r4;
    }

    public void setR4(Double r4) {
        this.r4 = r4;
    }

    public Double getR5() {
        return r5;
    }

    public void setR5(Double r5) {
        this.r5 = r5;
    }

    public Double getR6() {
        return r6;
    }

    public void setR6(Double r6) {
        this.r6 = r6;
    }

    public Double getR7() {
        return r7;
    }

    public void setR7(Double r7) {
        this.r7 = r7;
    }

    public Double getR8() {
        return r8;
    }

    public void setR8(Double r8) {
        this.r8 = r8;
    }

    public Double getR9() {
        return r9;
    }

    public void setR9(Double r9) {
        this.r9 = r9;
    }

    public Double getR10() {
        return r10;
    }

    public void setR10(Double r10) {
        this.r10 = r10;
    }

    public Double getR11() {
        return r11;
    }

    public void setR11(Double r11) {
        this.r11 = r11;
    }

    public Double getR12() {
        return r12;
    }

    public void setR12(Double r12) {
        this.r12 = r12;
    }

    public Double getR13() {
        return r13;
    }

    public void setR13(Double r13) {
        this.r13 = r13;
    }

    public Double getR14() {
        return r14;
    }

    public void setR14(Double r14) {
        this.r14 = r14;
    }

    public Double getR15() {
        return r15;
    }

    public void setR15(Double r15) {
        this.r15 = r15;
    }

    public Double getR16() {
        return r16;
    }

    public void setR16(Double r16) {
        this.r16 = r16;
    }

    public Double getR17() {
        return r17;
    }

    public void setR17(Double r17) {
        this.r17 = r17;
    }

    public Double getR18() {
        return r18;
    }

    public void setR18(Double r18) {
        this.r18 = r18;
    }

    public Double getR19() {
        return r19;
    }

    public void setR19(Double r19) {
        this.r19 = r19;
    }

    public Double getR20() {
        return r20;
    }

    public void setR20(Double r20) {
        this.r20 = r20;
    }

    public Double getR21() {
        return r21;
    }

    public void setR21(Double r21) {
        this.r21 = r21;
    }

    public Double getR22() {
        return r22;
    }

    public void setR22(Double r22) {
        this.r22 = r22;
    }

    public Double getR23() {
        return r23;
    }

    public void setR23(Double r23) {
        this.r23 = r23;
    }

    public Double getR24() {
        return r24;
    }

    public void setR24(Double r24) {
        this.r24 = r24;
    }

    public Double getR25() {
        return r25;
    }

    public void setR25(Double r25) {
        this.r25 = r25;
    }

    public Double getR26() {
        return r26;
    }

    public void setR26(Double r26) {
        this.r26 = r26;
    }

    public Double getR27() {
        return r27;
    }

    public void setR27(Double r27) {
        this.r27 = r27;
    }

    public Double getR28() {
        return r28;
    }

    public void setR28(Double r28) {
        this.r28 = r28;
    }

    public Double getR29() {
        return r29;
    }

    public void setR29(Double r29) {
        this.r29 = r29;
    }

    public Double getR30() {
        return r30;
    }

    public void setR30(Double r30) {
        this.r30 = r30;
    }

    public Double getR31() {
        return r31;
    }

    public void setR31(Double r31) {
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

    public String getIncompleto() {
        return incompleto;
    }

    public void setIncompleto(String incompleto) {
        this.incompleto = incompleto;
    }

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    @Override
    public String toString() {
        return "ListaPersonalEvaluacion360Promedios{" + "pk=" + pk + ", anio=" + anio + ", periodo=" + periodo + ", mesInicio=" + mesInicio + ", mesFin=" + mesFin + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", evaluadores=" + evaluadores + ", evaluadoresNombre=" + evaluadoresNombre + ", evaluadoNombre=" + evaluadoNombre + ", evaluadoAreaOperativa=" + evaluadoAreaOperativa + ", evaluadoAreaOperativaNombre=" + evaluadoAreaOperativaNombre + ", evaluadoCategoriaOperativa=" + evaluadoCategoriaOperativa + ", evaluadoCategoriaOperativaNombre=" + evaluadoCategoriaOperativaNombre + ", evaluadoAreaSuperior=" + evaluadoAreaSuperior + ", evaluadoAreaSuperiorNombre=" + evaluadoAreaSuperiorNombre + ", evaluadoActividad=" + evaluadoActividad + ", evaluadoActividadNombre=" + evaluadoActividadNombre + ", evaluadoFechaIngreso=" + evaluadoFechaIngreso + ", evaluadoGeneroNombre=" + evaluadoGeneroNombre + ", tipo=" + tipo + ", categoria=" + categoria + ", categoriaNombre=" + categoriaNombre + ", r1=" + r1 + ", r2=" + r2 + ", r3=" + r3 + ", r4=" + r4 + ", r5=" + r5 + ", r6=" + r6 + ", r7=" + r7 + ", r8=" + r8 + ", r9=" + r9 + ", r10=" + r10 + ", r11=" + r11 + ", r12=" + r12 + ", r13=" + r13 + ", r14=" + r14 + ", r15=" + r15 + ", r16=" + r16 + ", r17=" + r17 + ", r18=" + r18 + ", r19=" + r19 + ", r20=" + r20 + ", r21=" + r21 + ", r22=" + r22 + ", r23=" + r23 + ", r24=" + r24 + ", r25=" + r25 + ", r26=" + r26 + ", r27=" + r27 + ", r28=" + r28 + ", r29=" + r29 + ", r30=" + r30 + ", r31=" + r31 + ", r32=" + r32 + ", r33=" + r33 + ", incompleto=" + incompleto + ", completo=" + completo + ", promedio=" + promedio + '}';
    }
    
}
