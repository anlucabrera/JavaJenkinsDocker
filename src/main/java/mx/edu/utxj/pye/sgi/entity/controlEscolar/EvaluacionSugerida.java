/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluacion_sugerida", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionSugerida.findAll", query = "SELECT e FROM EvaluacionSugerida e")
    , @NamedQuery(name = "EvaluacionSugerida.findByEvaluacionSugerida", query = "SELECT e FROM EvaluacionSugerida e WHERE e.evaluacionSugerida = :evaluacionSugerida")
    , @NamedQuery(name = "EvaluacionSugerida.findByMetaInstrumento", query = "SELECT e FROM EvaluacionSugerida e WHERE e.metaInstrumento = :metaInstrumento")
    , @NamedQuery(name = "EvaluacionSugerida.findByPeriodoInicio", query = "SELECT e FROM EvaluacionSugerida e WHERE e.periodoInicio = :periodoInicio")
    , @NamedQuery(name = "EvaluacionSugerida.findByActivo", query = "SELECT e FROM EvaluacionSugerida e WHERE e.activo = :activo")})
public class EvaluacionSugerida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evaluacion_sugerida")
    private Integer evaluacionSugerida;
    @Basic(optional = false)
    @NotNull
    @Column(name = "meta_instrumento")
    private int metaInstrumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_inicio")
    private int periodoInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "evidencia", referencedColumnName = "evidencia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EvidenciaEvaluacion evidencia;
    @JoinColumn(name = "instrumento", referencedColumnName = "instrumento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InstrumentoEvaluacion instrumento;
    @JoinColumn(name = "unidad_materia", referencedColumnName = "id_unidad_materia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMateria unidadMateria;

    public EvaluacionSugerida() {
    }

    public EvaluacionSugerida(Integer evaluacionSugerida) {
        this.evaluacionSugerida = evaluacionSugerida;
    }

    public EvaluacionSugerida(Integer evaluacionSugerida, int metaInstrumento) {
        this.evaluacionSugerida = evaluacionSugerida;
        this.metaInstrumento = metaInstrumento;
    }

    public Integer getEvaluacionSugerida() {
        return evaluacionSugerida;
    }

    public void setEvaluacionSugerida(Integer evaluacionSugerida) {
        this.evaluacionSugerida = evaluacionSugerida;
    }

    public int getMetaInstrumento() {
        return metaInstrumento;
    }

    public void setMetaInstrumento(int metaInstrumento) {
        this.metaInstrumento = metaInstrumento;
    }

    public EvidenciaEvaluacion getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(EvidenciaEvaluacion evidencia) {
        this.evidencia = evidencia;
    }

    public InstrumentoEvaluacion getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(InstrumentoEvaluacion instrumento) {
        this.instrumento = instrumento;
    }

    public UnidadMateria getUnidadMateria() {
        return unidadMateria;
    }

    public void setUnidadMateria(UnidadMateria unidadMateria) {
        this.unidadMateria = unidadMateria;
    }
    
    public int getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(int periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacionSugerida != null ? evaluacionSugerida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionSugerida)) {
            return false;
        }
        EvaluacionSugerida other = (EvaluacionSugerida) object;
        if ((this.evaluacionSugerida == null && other.evaluacionSugerida != null) || (this.evaluacionSugerida != null && !this.evaluacionSugerida.equals(other.evaluacionSugerida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida[ evaluacionSugerida=" + evaluacionSugerida + " ]";
    }
    
}
