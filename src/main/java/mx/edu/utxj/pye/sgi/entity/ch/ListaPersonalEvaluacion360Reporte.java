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
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "lista_personal_evaluacion_360_reporte", catalog = "capital_humano", schema = "")
@XmlRootElement @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(of = "pk")
@NamedQueries({
    @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findAll", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByAnio", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.anio = :anio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByPeriodo", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByMesInicio", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.mesInicio = :mesInicio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByMesFin", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.mesFin = :mesFin")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluacion", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.pk.evaluacion = :evaluacion")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByFechaInicio", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByFechaFin", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.fechaFin = :fechaFin")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluador", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.pk.evaluador = :evaluador")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorNombre = :evaluadorNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorAreaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorAreaOperativa = :evaluadorAreaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorAreaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorAreaOperativaNombre = :evaluadorAreaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorCategoriaOperativa", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorCategoriaOperativa = :evaluadorCategoriaOperativa")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorCategoriaOperativaNombre = :evaluadorCategoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorAreaSuperior", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorAreaSuperior = :evaluadorAreaSuperior")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorAreaSuperiorNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorAreaSuperiorNombre = :evaluadorAreaSuperiorNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorActividad", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorActividad = :evaluadorActividad")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByEvaluadorActividadNombre", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.evaluadorActividadNombre = :evaluadorActividadNombre")
    , @NamedQuery(name = "ListaPersonalEvaluacion360Reporte.findByCompleto", query = "SELECT l FROM ListaPersonalEvaluacion360Reporte l WHERE l.completo = :completo")})
public class ListaPersonalEvaluacion360Reporte implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @NotNull
    @NonNull
    @Getter @Setter private EvaluacionEvaluadorPK pk;
    
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
    @Lob
    @Size(max = 65535)
    @Column(name = "tipo")
    private String tipo;
    @Lob
    @Size(max = 65535)
    @Column(name = "categoria")
    private String categoria;
    @Lob
    @Size(max = 65535)
    @Column(name = "categoria_nombre")
    private String categoriaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completo")
    private int completo;
    @Lob
    @Size(max = 65535)
    @Column(name = "resultado")
    private String resultado; 
    @Lob
    @Size(max = 65535)
    @Column(name = "evaluados")
    private String evaluados;
    @Lob
    @Size(max = 65535)
    @Column(name = "evaluados_nombre")
    private String evaluadosNombre;

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public int getCompleto() {
        return completo;
    }

    public void setCompleto(int completo) {
        this.completo = completo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getEvaluados() {
        return evaluados;
    }

    public void setEvaluados(String evaluados) {
        this.evaluados = evaluados;
    }

    public String getEvaluadosNombre() {
        return evaluadosNombre;
    }

    public void setEvaluadosNombre(String evaluadosNombre) {
        this.evaluadosNombre = evaluadosNombre;
    }
    
}
