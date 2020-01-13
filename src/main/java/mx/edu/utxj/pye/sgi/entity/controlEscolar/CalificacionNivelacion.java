/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "calificacion_nivelacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionNivelacion.findAll", query = "SELECT c FROM CalificacionNivelacion c")
    , @NamedQuery(name = "CalificacionNivelacion.findByCarga", query = "SELECT c FROM CalificacionNivelacion c WHERE c.calificacionNivelacionPK.carga = :carga")
    , @NamedQuery(name = "CalificacionNivelacion.findByIdEstudiante", query = "SELECT c FROM CalificacionNivelacion c WHERE c.calificacionNivelacionPK.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "CalificacionNivelacion.findByValor", query = "SELECT c FROM CalificacionNivelacion c WHERE c.valor = :valor")})
public class CalificacionNivelacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionNivelacionPK calificacionNivelacionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @JoinColumn(name = "indicador", referencedColumnName = "indicador")
    @ManyToOne(optional = false)
    private Indicador indicador;
    @JoinColumn(name = "carga", referencedColumnName = "carga", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CargaAcademica cargaAcademica;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante;

    public CalificacionNivelacion() {
    }

    public CalificacionNivelacion(CalificacionNivelacionPK calificacionNivelacionPK) {
        this.calificacionNivelacionPK = calificacionNivelacionPK;
    }

    public CalificacionNivelacion(CalificacionNivelacionPK calificacionNivelacionPK, double valor) {
        this.calificacionNivelacionPK = calificacionNivelacionPK;
        this.valor = valor;
    }

    public CalificacionNivelacion(int carga, int idEstudiante) {
        this.calificacionNivelacionPK = new CalificacionNivelacionPK(carga, idEstudiante);
    }

    public CalificacionNivelacionPK getCalificacionNivelacionPK() {
        return calificacionNivelacionPK;
    }

    public void setCalificacionNivelacionPK(CalificacionNivelacionPK calificacionNivelacionPK) {
        this.calificacionNivelacionPK = calificacionNivelacionPK;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public CargaAcademica getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(CargaAcademica cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacionNivelacionPK != null ? calificacionNivelacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionNivelacion)) {
            return false;
        }
        CalificacionNivelacion other = (CalificacionNivelacion) object;
        if ((this.calificacionNivelacionPK == null && other.calificacionNivelacionPK != null) || (this.calificacionNivelacionPK != null && !this.calificacionNivelacionPK.equals(other.calificacionNivelacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion[ calificacionNivelacionPK=" + calificacionNivelacionPK + " ]";
    }
    
}
