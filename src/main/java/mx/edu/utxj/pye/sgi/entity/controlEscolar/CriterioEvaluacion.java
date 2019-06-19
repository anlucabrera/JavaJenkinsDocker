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
import javax.persistence.Id;
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
@Table(name = "criterio_evaluacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CriterioEvaluacion.findAll", query = "SELECT c FROM CriterioEvaluacion c"),
    @NamedQuery(name = "CriterioEvaluacion.findByIdCriterioEvaluacion", query = "SELECT c FROM CriterioEvaluacion c WHERE c.idCriterioEvaluacion = :idCriterioEvaluacion"),
    @NamedQuery(name = "CriterioEvaluacion.findByNombre", query = "SELECT c FROM CriterioEvaluacion c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CriterioEvaluacion.findByPorcentaje", query = "SELECT c FROM CriterioEvaluacion c WHERE c.porcentaje = :porcentaje"),
    @NamedQuery(name = "CriterioEvaluacion.findByEstatus", query = "SELECT c FROM CriterioEvaluacion c WHERE c.estatus = :estatus")})
public class CriterioEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_criterio_evaluacion")
    private Short idCriterioEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nombre")
    private int nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private int porcentaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criterioEvaluacion")
    private List<IndicadorEvaluacion> indicadorEvaluacionList;

    public CriterioEvaluacion() {
    }

    public CriterioEvaluacion(Short idCriterioEvaluacion) {
        this.idCriterioEvaluacion = idCriterioEvaluacion;
    }

    public CriterioEvaluacion(Short idCriterioEvaluacion, int nombre, int porcentaje, boolean estatus) {
        this.idCriterioEvaluacion = idCriterioEvaluacion;
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.estatus = estatus;
    }

    public Short getIdCriterioEvaluacion() {
        return idCriterioEvaluacion;
    }

    public void setIdCriterioEvaluacion(Short idCriterioEvaluacion) {
        this.idCriterioEvaluacion = idCriterioEvaluacion;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<IndicadorEvaluacion> getIndicadorEvaluacionList() {
        return indicadorEvaluacionList;
    }

    public void setIndicadorEvaluacionList(List<IndicadorEvaluacion> indicadorEvaluacionList) {
        this.indicadorEvaluacionList = indicadorEvaluacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCriterioEvaluacion != null ? idCriterioEvaluacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioEvaluacion)) {
            return false;
        }
        CriterioEvaluacion other = (CriterioEvaluacion) object;
        if ((this.idCriterioEvaluacion == null && other.idCriterioEvaluacion != null) || (this.idCriterioEvaluacion != null && !this.idCriterioEvaluacion.equals(other.idCriterioEvaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioEvaluacion[ idCriterioEvaluacion=" + idCriterioEvaluacion + " ]";
    }
    
}
