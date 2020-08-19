/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch.view;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360ResultadosPK;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "lista_personal_evaluacion_360", catalog = "capital_humano", schema = "")
@XmlRootElement @ToString
@NamedQueries({
    @NamedQuery(name = "ListaPersonalEvaluacion360.findAll", query = "SELECT l FROM ListaPersonalEvaluacion360 l")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByAnio", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.anio = :anio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByPeriodo", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByMesInicio", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.mesInicio = :mesInicio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByMesFin", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.mesFin = :mesFin")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluacion", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.pk.evaluacion = :evaluacion")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByFechaInicio", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByFechaFin", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.fechaFin = :fechaFin")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluador", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.pk.evaluador = :evaluador")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorNombre = :evaluadorNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorAreaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorAreaOperativa = :evaluadorAreaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorAreaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorAreaOperativaNombre = :evaluadorAreaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorCategoriaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorCategoriaOperativa = :evaluadorCategoriaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorCategoriaOperativaNombre = :evaluadorCategoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorAreaSuperior", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorAreaSuperior = :evaluadorAreaSuperior")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorAreaSuperiorNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorAreaSuperiorNombre = :evaluadorAreaSuperiorNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorActividad", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorActividad = :evaluadorActividad")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadorActividadNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadorActividadNombre = :evaluadorActividadNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluado", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.pk.evaluado = :evaluado")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoNombre = :evaluadoNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoAreaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoAreaOperativa = :evaluadoAreaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoAreaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoAreaOperativaNombre = :evaluadoAreaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoCategoriaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoCategoriaOperativa = :evaluadoCategoriaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoCategoriaOperativaNombre = :evaluadoCategoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoAreaSuperior", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoAreaSuperior = :evaluadoAreaSuperior")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoAreaSuperiorNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoAreaSuperiorNombre = :evaluadoAreaSuperiorNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoActividad", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoActividad = :evaluadoActividad")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByEvaluadoActividadNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.evaluadoActividadNombre = :evaluadoActividadNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByTipo", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.tipo = :tipo")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByCategoria", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.categoria = :categoria")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByCategoriaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.categoriaNombre = :categoriaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR1", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r1 = :r1")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR2", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r2 = :r2")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR3", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r3 = :r3")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR4", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r4 = :r4")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR5", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r5 = :r5")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR6", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r6 = :r6")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR7", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r7 = :r7")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR8", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r8 = :r8")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR9", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r9 = :r9")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR10", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r10 = :r10")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR11", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r11 = :r11")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR12", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r12 = :r12")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR13", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r13 = :r13")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR14", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r14 = :r14")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR15", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r15 = :r15")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR16", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r16 = :r16")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR17", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r17 = :r17")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR18", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r18 = :r18")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR19", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r19 = :r19")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR20", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r20 = :r20")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR21", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r21 = :r21")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR22", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r22 = :r22")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR23", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r23 = :r23")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR24", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r24 = :r24")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR25", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r25 = :r25")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR26", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r26 = :r26")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR27", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r27 = :r27")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR28", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r28 = :r28")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR29", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r29 = :r29")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR30", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r30 = :r30")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR31", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r31 = :r31")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR32", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r32 = :r32")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByR33", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.r33 = :r33")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByIncompleto", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.incompleto = :incompleto")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByCompleto", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.completo = :completo")
    , @NamedQuery(name = "ListaPersonalEvaluacion360.findByPromedio", query = "SELECT l FROM ListaPersonalEvaluacion360 l WHERE l.promedio = :promedio")})
public class ListaPersonalEvaluacion360 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId @NonNull
    @Getter @Setter private Evaluaciones360ResultadosPK pk;
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
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "evaluador_nombre")
    private String evaluadorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_area_operativa")
    private int evaluadorAreaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluador_area_operativa_nombre")
    private String evaluadorAreaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_categoria_operativa")
    private short evaluadorCategoriaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "evaluador_categoria_operativa_nombre")
    private String evaluadorCategoriaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_area_superior")
    private int evaluadorAreaSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluador_area_superior_nombre")
    private String evaluadorAreaSuperiorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_actividad")
    private short evaluadorActividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "evaluador_actividad_nombre")
    private String evaluadorActividadNombre;
    
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
    @Column(name = "evaluado_fecha_ingreso", nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter private Date evaluadoFechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluado_genero_nombre")
    @Getter @Setter private String evaluadoGeneroNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria")
    private short categoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 41)
    @Column(name = "tipo")
    private String tipo;    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "categoria_nombre")
    private String categoriaNombre;
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
    @Size(max = 500)
    @Column(name = "r32")
    private String r32;
    @Size(max = 500)
    @Column(name = "r33")
    private String r33;
    @Basic(optional = false)
    @NotNull
    @Column(name = "incompleto")
    private boolean incompleto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completo")
    private boolean completo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private double promedio;

    public ListaPersonalEvaluacion360() {
    }

    public ListaPersonalEvaluacion360(Evaluaciones360ResultadosPK pk) {
        this.pk = pk;
    }

    public short getCategoria() {
        return categoria;
    }

    public void setCategoria(short categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getEvaluadorNombre() {
        return evaluadorNombre;
    }

    public void setEvaluadorNombre(String evaluadorNombre) {
        this.evaluadorNombre = evaluadorNombre;
    }

    public int getEvaluadorAreaOperativa() {
        return evaluadorAreaOperativa;
    }

    public void setEvaluadorAreaOperativa(int evaluadorAreaOperativa) {
        this.evaluadorAreaOperativa = evaluadorAreaOperativa;
    }

    public String getEvaluadorAreaOperativaNombre() {
        return evaluadorAreaOperativaNombre;
    }

    public void setEvaluadorAreaOperativaNombre(String evaluadorAreaOperativaNombre) {
        this.evaluadorAreaOperativaNombre = evaluadorAreaOperativaNombre;
    }

    public short getEvaluadorCategoriaOperativa() {
        return evaluadorCategoriaOperativa;
    }

    public void setEvaluadorCategoriaOperativa(short evaluadorCategoriaOperativa) {
        this.evaluadorCategoriaOperativa = evaluadorCategoriaOperativa;
    }

    public String getEvaluadorCategoriaOperativaNombre() {
        return evaluadorCategoriaOperativaNombre;
    }

    public void setEvaluadorCategoriaOperativaNombre(String evaluadorCategoriaOperativaNombre) {
        this.evaluadorCategoriaOperativaNombre = evaluadorCategoriaOperativaNombre;
    }

    public int getEvaluadorAreaSuperior() {
        return evaluadorAreaSuperior;
    }

    public void setEvaluadorAreaSuperior(int evaluadorAreaSuperior) {
        this.evaluadorAreaSuperior = evaluadorAreaSuperior;
    }

    public String getEvaluadorAreaSuperiorNombre() {
        return evaluadorAreaSuperiorNombre;
    }

    public void setEvaluadorAreaSuperiorNombre(String evaluadorAreaSuperiorNombre) {
        this.evaluadorAreaSuperiorNombre = evaluadorAreaSuperiorNombre;
    }

    public short getEvaluadorActividad() {
        return evaluadorActividad;
    }

    public void setEvaluadorActividad(short evaluadorActividad) {
        this.evaluadorActividad = evaluadorActividad;
    }

    public String getEvaluadorActividadNombre() {
        return evaluadorActividadNombre;
    }

    public void setEvaluadorActividadNombre(String evaluadorActividadNombre) {
        this.evaluadorActividadNombre = evaluadorActividadNombre;
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

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
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

    public boolean getIncompleto() {
        return incompleto;
    }

    public void setIncompleto(boolean incompleto) {
        this.incompleto = incompleto;
    }

    public boolean getCompleto() {
        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.pk);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListaPersonalEvaluacion360 other = (ListaPersonalEvaluacion360) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }
    
}
