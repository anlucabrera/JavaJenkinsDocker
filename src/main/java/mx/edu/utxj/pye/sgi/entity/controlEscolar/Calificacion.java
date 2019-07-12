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
 * @author HOME
 */
@Entity
@Table(name = "calificacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calificacion.findAll", query = "SELECT c FROM Calificacion c")
    , @NamedQuery(name = "Calificacion.findByCalificacion", query = "SELECT c FROM Calificacion c WHERE c.calificacion = :calificacion")
    , @NamedQuery(name = "Calificacion.findByValor", query = "SELECT c FROM Calificacion c WHERE c.valor = :valor")})
public class Calificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "calificacion")
    private Long calificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante idEstudiante;
    @JoinColumn(name = "configuracion_detalle", referencedColumnName = "configuracion_detalle")
    @ManyToOne(optional = false)
    private UnidadMateriaConfiguracionDetalle configuracionDetalle;

    public Calificacion() {
    }

    public Calificacion(Long calificacion) {
        this.calificacion = calificacion;
    }

    public Calificacion(Long calificacion, double valor) {
        this.calificacion = calificacion;
        this.valor = valor;
    }

    public Long getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Long calificacion) {
        this.calificacion = calificacion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Estudiante getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Estudiante idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public UnidadMateriaConfiguracionDetalle getConfiguracionDetalle() {
        return configuracionDetalle;
    }

    public void setConfiguracionDetalle(UnidadMateriaConfiguracionDetalle configuracionDetalle) {
        this.configuracionDetalle = configuracionDetalle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacion != null ? calificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calificacion)) {
            return false;
        }
        Calificacion other = (Calificacion) object;
        if ((this.calificacion == null && other.calificacion != null) || (this.calificacion != null && !this.calificacion.equals(other.calificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion[ calificacion=" + calificacion + " ]";
    }
    
}
