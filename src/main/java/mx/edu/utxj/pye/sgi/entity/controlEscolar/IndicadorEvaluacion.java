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
@Table(name = "indicador_evaluacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadorEvaluacion.findAll", query = "SELECT i FROM IndicadorEvaluacion i"),
    @NamedQuery(name = "IndicadorEvaluacion.findByIdIndicadorEvaluacion", query = "SELECT i FROM IndicadorEvaluacion i WHERE i.idIndicadorEvaluacion = :idIndicadorEvaluacion"),
    @NamedQuery(name = "IndicadorEvaluacion.findByNombre", query = "SELECT i FROM IndicadorEvaluacion i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "IndicadorEvaluacion.findByEstatus", query = "SELECT i FROM IndicadorEvaluacion i WHERE i.estatus = :estatus")})
public class IndicadorEvaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_indicador_evaluacion")
    private Integer idIndicadorEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nombre")
    private int nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idIndicadorEvaluacion")
    private List<IndicadorConfiguracion> indicadorConfiguracionList;
    @JoinColumn(name = "criterio_evaluacion", referencedColumnName = "id_criterio_evaluacion")
    @ManyToOne(optional = false)
    private CriterioEvaluacion criterioEvaluacion;

    public IndicadorEvaluacion() {
    }

    public IndicadorEvaluacion(Integer idIndicadorEvaluacion) {
        this.idIndicadorEvaluacion = idIndicadorEvaluacion;
    }

    public IndicadorEvaluacion(Integer idIndicadorEvaluacion, int nombre, boolean estatus) {
        this.idIndicadorEvaluacion = idIndicadorEvaluacion;
        this.nombre = nombre;
        this.estatus = estatus;
    }

    public Integer getIdIndicadorEvaluacion() {
        return idIndicadorEvaluacion;
    }

    public void setIdIndicadorEvaluacion(Integer idIndicadorEvaluacion) {
        this.idIndicadorEvaluacion = idIndicadorEvaluacion;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<IndicadorConfiguracion> getIndicadorConfiguracionList() {
        return indicadorConfiguracionList;
    }

    public void setIndicadorConfiguracionList(List<IndicadorConfiguracion> indicadorConfiguracionList) {
        this.indicadorConfiguracionList = indicadorConfiguracionList;
    }

    public CriterioEvaluacion getCriterioEvaluacion() {
        return criterioEvaluacion;
    }

    public void setCriterioEvaluacion(CriterioEvaluacion criterioEvaluacion) {
        this.criterioEvaluacion = criterioEvaluacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIndicadorEvaluacion != null ? idIndicadorEvaluacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorEvaluacion)) {
            return false;
        }
        IndicadorEvaluacion other = (IndicadorEvaluacion) object;
        if ((this.idIndicadorEvaluacion == null && other.idIndicadorEvaluacion != null) || (this.idIndicadorEvaluacion != null && !this.idIndicadorEvaluacion.equals(other.idIndicadorEvaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorEvaluacion[ idIndicadorEvaluacion=" + idIndicadorEvaluacion + " ]";
    }
    
}
