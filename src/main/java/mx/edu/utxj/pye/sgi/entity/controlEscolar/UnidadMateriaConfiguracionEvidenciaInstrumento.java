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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "unidad_materia_configuracion_evidencia_instrumento", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateriaConfiguracionEvidenciaInstrumento.findAll", query = "SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u")
    , @NamedQuery(name = "UnidadMateriaConfiguracionEvidenciaInstrumento.findByConfiguracionEvidenciaInstrumento", query = "SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.configuracionEvidenciaInstrumento = :configuracionEvidenciaInstrumento")
    , @NamedQuery(name = "UnidadMateriaConfiguracionEvidenciaInstrumento.findByPorcentaje", query = "SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.porcentaje = :porcentaje")
    , @NamedQuery(name = "UnidadMateriaConfiguracionEvidenciaInstrumento.findByMetaInstrumento", query = "SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.metaInstrumento = :metaInstrumento")})
public class UnidadMateriaConfiguracionEvidenciaInstrumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "configuracion_evidencia_instrumento")
    private Long configuracionEvidenciaInstrumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "meta_instrumento")
    private double metaInstrumento;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMateriaConfiguracion configuracion;
    @JoinColumn(name = "evidencia", referencedColumnName = "evidencia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EvidenciaEvaluacion evidencia;
    @JoinColumn(name = "instrumento", referencedColumnName = "instrumento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InstrumentoEvaluacion instrumento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configuracionEvidencia", fetch = FetchType.LAZY)
    private List<CalificacionEvidenciaInstrumento> calificacionEvidenciaInstrumentoList;

    public UnidadMateriaConfiguracionEvidenciaInstrumento() {
    }

    public UnidadMateriaConfiguracionEvidenciaInstrumento(Long configuracionEvidenciaInstrumento) {
        this.configuracionEvidenciaInstrumento = configuracionEvidenciaInstrumento;
    }

    public UnidadMateriaConfiguracionEvidenciaInstrumento(Long configuracionEvidenciaInstrumento, double porcentaje, double metaInstrumento) {
        this.configuracionEvidenciaInstrumento = configuracionEvidenciaInstrumento;
        this.porcentaje = porcentaje;
        this.metaInstrumento = metaInstrumento;
    }

    public Long getConfiguracionEvidenciaInstrumento() {
        return configuracionEvidenciaInstrumento;
    }

    public void setConfiguracionEvidenciaInstrumento(Long configuracionEvidenciaInstrumento) {
        this.configuracionEvidenciaInstrumento = configuracionEvidenciaInstrumento;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getMetaInstrumento() {
        return metaInstrumento;
    }

    public void setMetaInstrumento(double metaInstrumento) {
        this.metaInstrumento = metaInstrumento;
    }

    public UnidadMateriaConfiguracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(UnidadMateriaConfiguracion configuracion) {
        this.configuracion = configuracion;
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
    
    @XmlTransient
    public List<CalificacionEvidenciaInstrumento> getCalificacionEvidenciaInstrumentoList() {
        return calificacionEvidenciaInstrumentoList;
    }

    public void setCalificacionEvidenciaInstrumentoList(List<CalificacionEvidenciaInstrumento> calificacionEvidenciaInstrumentoList) {
        this.calificacionEvidenciaInstrumentoList = calificacionEvidenciaInstrumentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configuracionEvidenciaInstrumento != null ? configuracionEvidenciaInstrumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaConfiguracionEvidenciaInstrumento)) {
            return false;
        }
        UnidadMateriaConfiguracionEvidenciaInstrumento other = (UnidadMateriaConfiguracionEvidenciaInstrumento) object;
        if ((this.configuracionEvidenciaInstrumento == null && other.configuracionEvidenciaInstrumento != null) || (this.configuracionEvidenciaInstrumento != null && !this.configuracionEvidenciaInstrumento.equals(other.configuracionEvidenciaInstrumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionEvidenciaInstrumento[ configuracionEvidenciaInstrumento=" + configuracionEvidenciaInstrumento + " ]";
    }
    
}
