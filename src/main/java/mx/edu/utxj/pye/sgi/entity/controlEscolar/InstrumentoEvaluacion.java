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
@Table(name = "instrumento_evaluacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstrumentoEvaluacion.findAll", query = "SELECT i FROM InstrumentoEvaluacion i")
    , @NamedQuery(name = "InstrumentoEvaluacion.findByInstrumento", query = "SELECT i FROM InstrumentoEvaluacion i WHERE i.instrumento = :instrumento")
    , @NamedQuery(name = "InstrumentoEvaluacion.findByDescripcion", query = "SELECT i FROM InstrumentoEvaluacion i WHERE i.descripcion = :descripcion")
    , @NamedQuery(name = "InstrumentoEvaluacion.findByActivo", query = "SELECT i FROM InstrumentoEvaluacion i WHERE i.activo = :activo")})
public class InstrumentoEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "instrumento")
    private Integer instrumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instrumento", fetch = FetchType.LAZY)
    private List<EvaluacionSugerida> evaluacionSugeridaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instrumento", fetch = FetchType.LAZY)
    private List<UnidadMateriaConfiguracionEvidenciaInstrumento> unidadMateriaConfiguracionEvidenciaInstrumentoList;

    public InstrumentoEvaluacion() {
    }

    public InstrumentoEvaluacion(Integer instrumento) {
        this.instrumento = instrumento;
    }

    public InstrumentoEvaluacion(Integer instrumento, String descripcion, boolean activo) {
        this.instrumento = instrumento;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Integer instrumento) {
        this.instrumento = instrumento;
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
        hash += (instrumento != null ? instrumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstrumentoEvaluacion)) {
            return false;
        }
        InstrumentoEvaluacion other = (InstrumentoEvaluacion) object;
        if ((this.instrumento == null && other.instrumento != null) || (this.instrumento != null && !this.instrumento.equals(other.instrumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion[ instrumento=" + instrumento + " ]";
    }

    }
