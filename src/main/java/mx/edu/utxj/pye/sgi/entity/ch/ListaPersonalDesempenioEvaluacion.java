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
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "pk")
@Entity
@Table(name = "lista_personal_desempenio_evaluacion", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findAll", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByAnio", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.anio = :anio")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByPeriodo", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByMesInicio", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.mesInicio = :mesInicio")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByMesFin", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.mesFin = :mesFin")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluacion", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.pk.evaluacion = :evaluacion")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByFechaInicio", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByFechaFin", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.fechaFin = :fechaFin")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluador", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.pk.evaluador = :evaluador")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorNombre = :evaluadorNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorAreaOperativa", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorAreaOperativa = :evaluadorAreaOperativa")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorAreaOperativaNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorAreaOperativaNombre = :evaluadorAreaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorCategoriaOperativa", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorCategoriaOperativa = :evaluadorCategoriaOperativa")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorCategoriaOperativaNombre = :evaluadorCategoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorAreaSuperior", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorAreaSuperior = :evaluadorAreaSuperior")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorAreaSuperiorNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorAreaSuperiorNombre = :evaluadorAreaSuperiorNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorActividad", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorActividad = :evaluadorActividad")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadorActividadNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadorActividadNombre = :evaluadorActividadNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluado", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.pk.evaluado = :evaluado")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoNombre = :evaluadoNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoAreaOperativa", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoAreaOperativa = :evaluadoAreaOperativa")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoAreaOperativaNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoAreaOperativaNombre = :evaluadoAreaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoCategoriaOperativa", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoCategoriaOperativa = :evaluadoCategoriaOperativa")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoCategoriaOperativaNombre = :evaluadoCategoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoAreaSuperior", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoAreaSuperior = :evaluadoAreaSuperior")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoAreaSuperiorNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoAreaSuperiorNombre = :evaluadoAreaSuperiorNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoActividad", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoActividad = :evaluadoActividad")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByEvaluadoActividadNombre", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.evaluadoActividadNombre = :evaluadoActividadNombre")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR1", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r1 = :r1")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR2", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r2 = :r2")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR3", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r3 = :r3")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR4", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r4 = :r4")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR5", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r5 = :r5")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR6", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r6 = :r6")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR7", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r7 = :r7")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR8", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r8 = :r8")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR9", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r9 = :r9")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR10", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r10 = :r10")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR11", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r11 = :r11")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR12", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r12 = :r12")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR13", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r13 = :r13")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR14", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r14 = :r14")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR15", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r15 = :r15")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR16", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r16 = :r16")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR17", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r17 = :r17")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR18", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r18 = :r18")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR19", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r19 = :r19")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR20", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r20 = :r20")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByR21", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.r21 = :r21")
    , @NamedQuery(name = "ListaPersonalDesempenioEvaluacion.findByCompleto", query = "SELECT l FROM ListaPersonalDesempenioEvaluacion l WHERE l.completo = :completo")})
public class ListaPersonalDesempenioEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    @NotNull
    @NonNull
    @Getter @Setter private DesempenioEvaluacionResultadosPK pk;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    @Getter @Setter private int anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    @Getter @Setter private int periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mes_inicio")
    @Getter @Setter private String mesInicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mes_fin")
    @Getter @Setter private String mesFin;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    @Getter @Setter private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    @Getter @Setter private Date fechaFin;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "evaluador_nombre")
    @Getter @Setter private String evaluadorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_area_operativa")
    @Getter @Setter private int evaluadorAreaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluador_area_operativa_nombre")
    @Getter @Setter private String evaluadorAreaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_categoria_operativa")
    @Getter @Setter private short evaluadorCategoriaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "evaluador_categoria_operativa_nombre")
    @Getter @Setter private String evaluadorCategoriaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_area_superior")
    @Getter @Setter private int evaluadorAreaSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluador_area_superior_nombre")
    @Getter @Setter private String evaluadorAreaSuperiorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador_actividad")
    @Getter @Setter private short evaluadorActividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "evaluador_actividad_nombre")
    @Getter @Setter private String evaluadorActividadNombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "evaluado_nombre")
    @Getter @Setter private String evaluadoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_area_operativa")
    @Getter @Setter private int evaluadoAreaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluado_area_operativa_nombre")
    @Getter @Setter private String evaluadoAreaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_categoria_operativa")
    @Getter @Setter private short evaluadoCategoriaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "evaluado_categoria_operativa_nombre")
    @Getter @Setter private String evaluadoCategoriaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_area_superior")
    @Getter @Setter private int evaluadoAreaSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluado_area_superior_nombre")
    @Getter @Setter private String evaluadoAreaSuperiorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado_actividad")
    @Getter @Setter private short evaluadoActividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "evaluado_actividad_nombre")
    @Getter @Setter private String evaluadoActividadNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "evaluado_genero_nombre")
    @Getter @Setter private String evaluadoGeneroNombre;
    @Column(name = "r1")
    @Getter @Setter private Short r1;
    @Column(name = "r2")
    @Getter @Setter private Short r2;
    @Column(name = "r3")
    @Getter @Setter private Short r3;
    @Column(name = "r4")
    @Getter @Setter private Short r4;
    @Column(name = "r5")
    @Getter @Setter private Short r5;
    @Column(name = "r6")
    @Getter @Setter private Short r6;
    @Column(name = "r7")
    @Getter @Setter private Short r7;
    @Column(name = "r8")
    @Getter @Setter private Short r8;
    @Column(name = "r9")
    @Getter @Setter private Short r9;
    @Column(name = "r10")
    @Getter @Setter private Short r10;
    @Column(name = "r11")
    @Getter @Setter private Short r11;
    @Column(name = "r12")
    @Getter @Setter private Short r12;
    @Column(name = "r13")
    @Getter @Setter private Short r13;
    @Column(name = "r14")
    @Getter @Setter private Short r14;
    @Column(name = "r15")
    @Getter @Setter private Short r15;
    @Column(name = "r16")
    @Getter @Setter private Short r16;
    @Column(name = "r17")
    @Getter @Setter private Short r17;
    @Column(name = "r18")
    @Getter @Setter private Short r18;
    @Column(name = "r19")
    @Getter @Setter private Short r19;
    @Column(name = "r20")
    @Getter @Setter private Short r20;
    @Size(max = 500)
    @Column(name = "r21")
    @Getter @Setter private String r21;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completo")
    @Getter @Setter private boolean completo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "incompleto")
    @Getter @Setter private boolean incompleto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "promedio")
    @Getter @Setter private Double promedio;
    
}
