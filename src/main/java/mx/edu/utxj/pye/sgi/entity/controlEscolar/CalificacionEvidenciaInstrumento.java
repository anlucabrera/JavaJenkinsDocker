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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "calificacion_evidencia_instrumento", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionEvidenciaInstrumento.findAll", query = "SELECT c FROM CalificacionEvidenciaInstrumento c")
    , @NamedQuery(name = "CalificacionEvidenciaInstrumento.findByCalificacionEvidenciaInstrumento", query = "SELECT c FROM CalificacionEvidenciaInstrumento c WHERE c.calificacionEvidenciaInstrumento = :calificacionEvidenciaInstrumento")
    , @NamedQuery(name = "CalificacionEvidenciaInstrumento.findByValor", query = "SELECT c FROM CalificacionEvidenciaInstrumento c WHERE c.valor = :valor")})
public class CalificacionEvidenciaInstrumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "calificacion_evidencia_instrumento")
    private Long calificacionEvidenciaInstrumento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private Double valor;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante idEstudiante;
    @JoinColumn(name = "configuracion_evidencia", referencedColumnName = "configuracion_evidencia_instrumento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMateriaConfiguracionEvidenciaInstrumento configuracionEvidencia;

    public CalificacionEvidenciaInstrumento() {
    }

    public CalificacionEvidenciaInstrumento(Long calificacionEvidenciaInstrumento) {
        this.calificacionEvidenciaInstrumento = calificacionEvidenciaInstrumento;
    }

    public Long getCalificacionEvidenciaInstrumento() {
        return calificacionEvidenciaInstrumento;
    }

    public void setCalificacionEvidenciaInstrumento(Long calificacionEvidenciaInstrumento) {
        this.calificacionEvidenciaInstrumento = calificacionEvidenciaInstrumento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Estudiante getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Estudiante idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public UnidadMateriaConfiguracionEvidenciaInstrumento getConfiguracionEvidencia() {
        return configuracionEvidencia;
    }

    public void setConfiguracionEvidencia(UnidadMateriaConfiguracionEvidenciaInstrumento configuracionEvidencia) {
        this.configuracionEvidencia = configuracionEvidencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacionEvidenciaInstrumento != null ? calificacionEvidenciaInstrumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionEvidenciaInstrumento)) {
            return false;
        }
        CalificacionEvidenciaInstrumento other = (CalificacionEvidenciaInstrumento) object;
        if ((this.calificacionEvidenciaInstrumento == null && other.calificacionEvidenciaInstrumento != null) || (this.calificacionEvidenciaInstrumento != null && !this.calificacionEvidenciaInstrumento.equals(other.calificacionEvidenciaInstrumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionEvidenciaInstrumento[ calificacionEvidenciaInstrumento=" + calificacionEvidenciaInstrumento + " ]";
    }
    
}
