/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "calificacion_promedio", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionPromedio.findAll", query = "SELECT c FROM CalificacionPromedio c")
    , @NamedQuery(name = "CalificacionPromedio.findByCarga", query = "SELECT c FROM CalificacionPromedio c WHERE c.calificacionPromedioPK.carga = :carga")
    , @NamedQuery(name = "CalificacionPromedio.findByIdEstudiante", query = "SELECT c FROM CalificacionPromedio c WHERE c.calificacionPromedioPK.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "CalificacionPromedio.findByValor", query = "SELECT c FROM CalificacionPromedio c WHERE c.valor = :valor")
    , @NamedQuery(name = "CalificacionPromedio.findByFechaActualizacion", query = "SELECT c FROM CalificacionPromedio c WHERE c.fechaActualizacion = :fechaActualizacion")})
public class CalificacionPromedio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionPromedioPK calificacionPromedioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_actualizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "carga", referencedColumnName = "carga", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CargaAcademica cargaAcademica;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante;

    public CalificacionPromedio() {
    }

    public CalificacionPromedio(CalificacionPromedioPK calificacionPromedioPK) {
        this.calificacionPromedioPK = calificacionPromedioPK;
    }

    public CalificacionPromedio(CalificacionPromedioPK calificacionPromedioPK, double valor, Date fechaActualizacion, String tipo) {
        this.calificacionPromedioPK = calificacionPromedioPK;
        this.valor = valor;
        this.fechaActualizacion = fechaActualizacion;
        this.tipo = tipo;
    }

    public CalificacionPromedio(int carga, int idEstudiante) {
        this.calificacionPromedioPK = new CalificacionPromedioPK(carga, idEstudiante);
    }

    public CalificacionPromedioPK getCalificacionPromedioPK() {
        return calificacionPromedioPK;
    }

    public void setCalificacionPromedioPK(CalificacionPromedioPK calificacionPromedioPK) {
        this.calificacionPromedioPK = calificacionPromedioPK;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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
        hash += (calificacionPromedioPK != null ? calificacionPromedioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionPromedio)) {
            return false;
        }
        CalificacionPromedio other = (CalificacionPromedio) object;
        if ((this.calificacionPromedioPK == null && other.calificacionPromedioPK != null) || (this.calificacionPromedioPK != null && !this.calificacionPromedioPK.equals(other.calificacionPromedioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionPromedio[ calificacionPromedioPK=" + calificacionPromedioPK + " ]";
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
