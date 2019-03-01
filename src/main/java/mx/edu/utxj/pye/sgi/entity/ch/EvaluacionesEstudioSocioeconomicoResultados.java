/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluaciones_estudio_socioeconomico_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findAll", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByEvaluacion", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.evaluacionesEstudioSocioeconomicoResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByEvaluador", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.evaluacionesEstudioSocioeconomicoResultadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR1TienesHijos", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r1TienesHijos = :r1TienesHijos")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR2MadrePadreSoltero", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r2MadrePadreSoltero = :r2MadrePadreSoltero")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR3Trabajas", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r3Trabajas = :r3Trabajas")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR3aTrabajasIngresoMensual", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r3aTrabajasIngresoMensual = :r3aTrabajasIngresoMensual")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR4PadresEnfermedadTerminal", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r4PadresEnfermedadTerminal = :r4PadresEnfermedadTerminal")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR4aPadresEnfermedadTerminalCual", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r4aPadresEnfermedadTerminalCual = :r4aPadresEnfermedadTerminalCual")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR4bPadresEnfermedadTerminalOtra", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r4bPadresEnfermedadTerminalOtra = :r4bPadresEnfermedadTerminalOtra")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR5ProblemaAdverso", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r5ProblemaAdverso = :r5ProblemaAdverso")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR5aProblemaAdversoTipo", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r5aProblemaAdversoTipo = :r5aProblemaAdversoTipo")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR6PobrezaExtrema", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r6PobrezaExtrema = :r6PobrezaExtrema")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR7Desnutricion", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r7Desnutricion = :r7Desnutricion")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR8DeficienciaFisicaMental", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r8DeficienciaFisicaMental = :r8DeficienciaFisicaMental")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR8aDeficienciaFisicaMentalCual", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r8aDeficienciaFisicaMentalCual = :r8aDeficienciaFisicaMentalCual")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR8bDeficienciaFisicaMentalOtra", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r8bDeficienciaFisicaMentalOtra = :r8bDeficienciaFisicaMentalOtra")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR9DependenciaEconomicaPadres", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r9DependenciaEconomicaPadres = :r9DependenciaEconomicaPadres")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR10IngresoMensualPadres", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r10IngresoMensualPadres = :r10IngresoMensualPadres")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR11EscolaridadMaximaPadre", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r11EscolaridadMaximaPadre = :r11EscolaridadMaximaPadre")
    , @NamedQuery(name = "EvaluacionesEstudioSocioeconomicoResultados.findByR12EscolaridadMaximaMadre", query = "SELECT e FROM EvaluacionesEstudioSocioeconomicoResultados e WHERE e.r12EscolaridadMaximaMadre = :r12EscolaridadMaximaMadre")})
public class EvaluacionesEstudioSocioeconomicoResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionesEstudioSocioeconomicoResultadosPK evaluacionesEstudioSocioeconomicoResultadosPK;
    @Size(max = 3)
    @Column(name = "r1_tienes_hijos")
    private String r1TienesHijos;
    @Size(max = 3)
    @Column(name = "r2_madre_padre_soltero")
    private String r2MadrePadreSoltero;
    @Size(max = 3)
    @Column(name = "r3_trabajas")
    private String r3Trabajas;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "r3a_trabajas_ingreso_mensual")
    private Double r3aTrabajasIngresoMensual;
    @Size(max = 3)
    @Column(name = "r4_padres_enfermedad_terminal")
    private String r4PadresEnfermedadTerminal;
    @Size(max = 28)
    @Column(name = "r4a_padres_enfermedad_terminal_cual")
    private String r4aPadresEnfermedadTerminalCual;
    @Size(max = 500)
    @Column(name = "r4b_padres_enfermedad_terminal_otra")
    private String r4bPadresEnfermedadTerminalOtra;
    @Size(max = 3)
    @Column(name = "r5_problema_adverso")
    private String r5ProblemaAdverso;
    @Size(max = 500)
    @Column(name = "r5a_problema_adverso_tipo")
    private String r5aProblemaAdversoTipo;
    @Size(max = 3)
    @Column(name = "r6_pobreza_extrema")
    private String r6PobrezaExtrema;
    @Size(max = 3)
    @Column(name = "r7_desnutricion")
    private String r7Desnutricion;
    @Size(max = 3)
    @Column(name = "r8_deficiencia_fisica_mental")
    private String r8DeficienciaFisicaMental;
    @Size(max = 52)
    @Column(name = "r8a_deficiencia_fisica_mental_cual")
    private String r8aDeficienciaFisicaMentalCual;
    @Size(max = 500)
    @Column(name = "r8b_deficiencia_fisica_mental_otra")
    private String r8bDeficienciaFisicaMentalOtra;
    @Size(max = 3)
    @Column(name = "r9_dependencia_economica_padres")
    private String r9DependenciaEconomicaPadres;
    @Column(name = "r10_ingreso_mensual_padres")
    private Double r10IngresoMensualPadres;
    @Size(max = 50)
    @Column(name = "r11_escolaridad_maxima_padre")
    private String r11EscolaridadMaximaPadre;
    @Size(max = 50)
    @Column(name = "r12_escolaridad_maxima_madre")
    private String r12EscolaridadMaximaMadre;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;

    public EvaluacionesEstudioSocioeconomicoResultados() {
    }

    public EvaluacionesEstudioSocioeconomicoResultados(EvaluacionesEstudioSocioeconomicoResultadosPK evaluacionesEstudioSocioeconomicoResultadosPK) {
        this.evaluacionesEstudioSocioeconomicoResultadosPK = evaluacionesEstudioSocioeconomicoResultadosPK;
    }

    public EvaluacionesEstudioSocioeconomicoResultados(int evaluacion, int evaluador) {
        this.evaluacionesEstudioSocioeconomicoResultadosPK = new EvaluacionesEstudioSocioeconomicoResultadosPK(evaluacion, evaluador);
    }

    public EvaluacionesEstudioSocioeconomicoResultadosPK getEvaluacionesEstudioSocioeconomicoResultadosPK() {
        return evaluacionesEstudioSocioeconomicoResultadosPK;
    }

    public void setEvaluacionesEstudioSocioeconomicoResultadosPK(EvaluacionesEstudioSocioeconomicoResultadosPK evaluacionesEstudioSocioeconomicoResultadosPK) {
        this.evaluacionesEstudioSocioeconomicoResultadosPK = evaluacionesEstudioSocioeconomicoResultadosPK;
    }

    public String getR1TienesHijos() {
        return r1TienesHijos;
    }

    public void setR1TienesHijos(String r1TienesHijos) {
        this.r1TienesHijos = r1TienesHijos;
    }

    public String getR2MadrePadreSoltero() {
        return r2MadrePadreSoltero;
    }

    public void setR2MadrePadreSoltero(String r2MadrePadreSoltero) {
        this.r2MadrePadreSoltero = r2MadrePadreSoltero;
    }

    public String getR3Trabajas() {
        return r3Trabajas;
    }

    public void setR3Trabajas(String r3Trabajas) {
        this.r3Trabajas = r3Trabajas;
    }

    public Double getR3aTrabajasIngresoMensual() {
        return r3aTrabajasIngresoMensual;
    }

    public void setR3aTrabajasIngresoMensual(Double r3aTrabajasIngresoMensual) {
        this.r3aTrabajasIngresoMensual = r3aTrabajasIngresoMensual;
    }

    public String getR4PadresEnfermedadTerminal() {
        return r4PadresEnfermedadTerminal;
    }

    public void setR4PadresEnfermedadTerminal(String r4PadresEnfermedadTerminal) {
        this.r4PadresEnfermedadTerminal = r4PadresEnfermedadTerminal;
    }

    public String getR4aPadresEnfermedadTerminalCual() {
        return r4aPadresEnfermedadTerminalCual;
    }

    public void setR4aPadresEnfermedadTerminalCual(String r4aPadresEnfermedadTerminalCual) {
        this.r4aPadresEnfermedadTerminalCual = r4aPadresEnfermedadTerminalCual;
    }

    public String getR4bPadresEnfermedadTerminalOtra() {
        return r4bPadresEnfermedadTerminalOtra;
    }

    public void setR4bPadresEnfermedadTerminalOtra(String r4bPadresEnfermedadTerminalOtra) {
        this.r4bPadresEnfermedadTerminalOtra = r4bPadresEnfermedadTerminalOtra;
    }

    public String getR5ProblemaAdverso() {
        return r5ProblemaAdverso;
    }

    public void setR5ProblemaAdverso(String r5ProblemaAdverso) {
        this.r5ProblemaAdverso = r5ProblemaAdverso;
    }

    public String getR5aProblemaAdversoTipo() {
        return r5aProblemaAdversoTipo;
    }

    public void setR5aProblemaAdversoTipo(String r5aProblemaAdversoTipo) {
        this.r5aProblemaAdversoTipo = r5aProblemaAdversoTipo;
    }

    public String getR6PobrezaExtrema() {
        return r6PobrezaExtrema;
    }

    public void setR6PobrezaExtrema(String r6PobrezaExtrema) {
        this.r6PobrezaExtrema = r6PobrezaExtrema;
    }

    public String getR7Desnutricion() {
        return r7Desnutricion;
    }

    public void setR7Desnutricion(String r7Desnutricion) {
        this.r7Desnutricion = r7Desnutricion;
    }

    public String getR8DeficienciaFisicaMental() {
        return r8DeficienciaFisicaMental;
    }

    public void setR8DeficienciaFisicaMental(String r8DeficienciaFisicaMental) {
        this.r8DeficienciaFisicaMental = r8DeficienciaFisicaMental;
    }

    public String getR8aDeficienciaFisicaMentalCual() {
        return r8aDeficienciaFisicaMentalCual;
    }

    public void setR8aDeficienciaFisicaMentalCual(String r8aDeficienciaFisicaMentalCual) {
        this.r8aDeficienciaFisicaMentalCual = r8aDeficienciaFisicaMentalCual;
    }

    public String getR8bDeficienciaFisicaMentalOtra() {
        return r8bDeficienciaFisicaMentalOtra;
    }

    public void setR8bDeficienciaFisicaMentalOtra(String r8bDeficienciaFisicaMentalOtra) {
        this.r8bDeficienciaFisicaMentalOtra = r8bDeficienciaFisicaMentalOtra;
    }

    public String getR9DependenciaEconomicaPadres() {
        return r9DependenciaEconomicaPadres;
    }

    public void setR9DependenciaEconomicaPadres(String r9DependenciaEconomicaPadres) {
        this.r9DependenciaEconomicaPadres = r9DependenciaEconomicaPadres;
    }

    public Double getR10IngresoMensualPadres() {
        return r10IngresoMensualPadres;
    }

    public void setR10IngresoMensualPadres(Double r10IngresoMensualPadres) {
        this.r10IngresoMensualPadres = r10IngresoMensualPadres;
    }

    public String getR11EscolaridadMaximaPadre() {
        return r11EscolaridadMaximaPadre;
    }

    public void setR11EscolaridadMaximaPadre(String r11EscolaridadMaximaPadre) {
        this.r11EscolaridadMaximaPadre = r11EscolaridadMaximaPadre;
    }

    public String getR12EscolaridadMaximaMadre() {
        return r12EscolaridadMaximaMadre;
    }

    public void setR12EscolaridadMaximaMadre(String r12EscolaridadMaximaMadre) {
        this.r12EscolaridadMaximaMadre = r12EscolaridadMaximaMadre;
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
        hash += (evaluacionesEstudioSocioeconomicoResultadosPK != null ? evaluacionesEstudioSocioeconomicoResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionesEstudioSocioeconomicoResultados)) {
            return false;
        }
        EvaluacionesEstudioSocioeconomicoResultados other = (EvaluacionesEstudioSocioeconomicoResultados) object;
        if ((this.evaluacionesEstudioSocioeconomicoResultadosPK == null && other.evaluacionesEstudioSocioeconomicoResultadosPK != null) || (this.evaluacionesEstudioSocioeconomicoResultadosPK != null && !this.evaluacionesEstudioSocioeconomicoResultadosPK.equals(other.evaluacionesEstudioSocioeconomicoResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados[ evaluacionesEstudioSocioeconomicoResultadosPK=" + evaluacionesEstudioSocioeconomicoResultadosPK + " ]";
    }
    
}
