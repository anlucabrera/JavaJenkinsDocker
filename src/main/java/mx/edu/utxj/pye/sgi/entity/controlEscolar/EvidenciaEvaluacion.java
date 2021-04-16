/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evidencia_evaluacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvidenciaEvaluacion.findAll", query = "SELECT e FROM EvidenciaEvaluacion e")
    , @NamedQuery(name = "EvidenciaEvaluacion.findByEvidencia", query = "SELECT e FROM EvidenciaEvaluacion e WHERE e.evidencia = :evidencia")
    , @NamedQuery(name = "EvidenciaEvaluacion.findByDescripcion", query = "SELECT e FROM EvidenciaEvaluacion e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "EvidenciaEvaluacion.findByActivo", query = "SELECT e FROM EvidenciaEvaluacion e WHERE e.activo = :activo")})
public class EvidenciaEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evidencia")
    private Integer evidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evidencia", fetch = FetchType.LAZY)
    private List<EvaluacionSugerida> evaluacionSugeridaList;
    @JoinColumn(name = "criterio", referencedColumnName = "criterio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Criterio criterio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evidencia", fetch = FetchType.LAZY)
    private List<UnidadMateriaConfiguracionEvidenciaInstrumento> unidadMateriaConfiguracionEvidenciaInstrumentoList;

    public EvidenciaEvaluacion() {
    }

    public EvidenciaEvaluacion(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public EvidenciaEvaluacion(Integer evidencia, String descripcion, boolean activo) {
        this.evidencia = evidencia;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<EvaluacionSugerida> getEvaluacionSugeridaList() {
        return evaluacionSugeridaList;
    }

    public void setEvaluacionSugeridaList(List<EvaluacionSugerida> evaluacionSugeridaList) {
        this.evaluacionSugeridaList = evaluacionSugeridaList;
    }

    public Criterio getCriterio() {
        return criterio;
    }

    public void setCriterio(Criterio criterio) {
        this.criterio = criterio;
    }
    
    @XmlTransient
    public List<UnidadMateriaConfiguracionEvidenciaInstrumento> getUnidadMateriaConfiguracionEvidenciaInstrumentoList() {
        return unidadMateriaConfiguracionEvidenciaInstrumentoList;
    }

    public void setUnidadMateriaConfiguracionEvidenciaInstrumentoList(List<UnidadMateriaConfiguracionEvidenciaInstrumento> unidadMateriaConfiguracionEvidenciaInstrumentoList) {
        this.unidadMateriaConfiguracionEvidenciaInstrumentoList = unidadMateriaConfiguracionEvidenciaInstrumentoList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evidencia != null ? evidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvidenciaEvaluacion)) {
            return false;
        }
        EvidenciaEvaluacion other = (EvidenciaEvaluacion) object;
        if ((this.evidencia == null && other.evidencia != null) || (this.evidencia != null && !this.evidencia.equals(other.evidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion[ evidencia=" + evidencia + " ]";
    }

    }
