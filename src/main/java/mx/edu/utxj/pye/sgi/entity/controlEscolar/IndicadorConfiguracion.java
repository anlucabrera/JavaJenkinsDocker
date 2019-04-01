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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "indicador_configuracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadorConfiguracion.findAll", query = "SELECT i FROM IndicadorConfiguracion i"),
    @NamedQuery(name = "IndicadorConfiguracion.findByIdConfiguracion", query = "SELECT i FROM IndicadorConfiguracion i WHERE i.idConfiguracion = :idConfiguracion"),
    @NamedQuery(name = "IndicadorConfiguracion.findByPorcentaje", query = "SELECT i FROM IndicadorConfiguracion i WHERE i.porcentaje = :porcentaje")})
public class IndicadorConfiguracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_configuracion")
    private Integer idConfiguracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private int porcentaje;
    @JoinColumn(name = "id_configuracion", referencedColumnName = "id_configuracion_materia", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ConfiguracionMateria configuracionMateria;
    @JoinColumn(name = "id_indicador_evaluacion", referencedColumnName = "id_indicador_evaluacion")
    @ManyToOne(optional = false)
    private IndicadorEvaluacion idIndicadorEvaluacion;

    public IndicadorConfiguracion() {
    }

    public IndicadorConfiguracion(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public IndicadorConfiguracion(Integer idConfiguracion, int porcentaje) {
        this.idConfiguracion = idConfiguracion;
        this.porcentaje = porcentaje;
    }

    public Integer getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(Integer idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public ConfiguracionMateria getConfiguracionMateria() {
        return configuracionMateria;
    }

    public void setConfiguracionMateria(ConfiguracionMateria configuracionMateria) {
        this.configuracionMateria = configuracionMateria;
    }

    public IndicadorEvaluacion getIdIndicadorEvaluacion() {
        return idIndicadorEvaluacion;
    }

    public void setIdIndicadorEvaluacion(IndicadorEvaluacion idIndicadorEvaluacion) {
        this.idIndicadorEvaluacion = idIndicadorEvaluacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfiguracion != null ? idConfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorConfiguracion)) {
            return false;
        }
        IndicadorConfiguracion other = (IndicadorConfiguracion) object;
        if ((this.idConfiguracion == null && other.idConfiguracion != null) || (this.idConfiguracion != null && !this.idConfiguracion.equals(other.idConfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorConfiguracion[ idConfiguracion=" + idConfiguracion + " ]";
    }
    
}
