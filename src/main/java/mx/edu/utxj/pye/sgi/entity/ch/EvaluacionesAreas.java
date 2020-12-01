/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluaciones_areas", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionesAreas.findAll", query = "SELECT e FROM EvaluacionesAreas e")
    , @NamedQuery(name = "EvaluacionesAreas.findByIdTipoEvaluacionArea", query = "SELECT e FROM EvaluacionesAreas e WHERE e.idTipoEvaluacionArea = :idTipoEvaluacionArea")
    , @NamedQuery(name = "EvaluacionesAreas.findByTipoEvaluacion", query = "SELECT e FROM EvaluacionesAreas e WHERE e.tipoEvaluacion = :tipoEvaluacion")
    , @NamedQuery(name = "EvaluacionesAreas.findByDescripcion", query = "SELECT e FROM EvaluacionesAreas e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "EvaluacionesAreas.findByAreaResponsable", query = "SELECT e FROM EvaluacionesAreas e WHERE e.areaResponsable = :areaResponsable")
    , @NamedQuery(name = "EvaluacionesAreas.findByAreaConsulta", query = "SELECT e FROM EvaluacionesAreas e WHERE e.areaConsulta = :areaConsulta")
    , @NamedQuery(name = "EvaluacionesAreas.findByBase", query = "SELECT e FROM EvaluacionesAreas e WHERE e.base = :base")
    , @NamedQuery(name = "EvaluacionesAreas.findByTablaResultados", query = "SELECT e FROM EvaluacionesAreas e WHERE e.tablaResultados = :tablaResultados")
    , @NamedQuery(name = "EvaluacionesAreas.findByCuestionario", query = "SELECT e FROM EvaluacionesAreas e WHERE e.cuestionario = :cuestionario")})
public class EvaluacionesAreas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_evaluacion_area")
    private Integer idTipoEvaluacionArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "tipo_evaluacion")
    private String tipoEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_responsable")
    private short areaResponsable;
    @Column(name = "area_consulta")
    private Short areaConsulta;
    @Size(max = 25)
    @Column(name = "base")
    private String base;
    @Size(max = 50)
    @Column(name = "tabla_resultados")
    private String tablaResultados;
    @Size(max = 50)
    @Column(name = "cuestionario")
    private String cuestionario;

    public EvaluacionesAreas() {
    }

    public EvaluacionesAreas(Integer idTipoEvaluacionArea) {
        this.idTipoEvaluacionArea = idTipoEvaluacionArea;
    }

    public EvaluacionesAreas(Integer idTipoEvaluacionArea, String tipoEvaluacion, String descripcion, short areaResponsable) {
        this.idTipoEvaluacionArea = idTipoEvaluacionArea;
        this.tipoEvaluacion = tipoEvaluacion;
        this.descripcion = descripcion;
        this.areaResponsable = areaResponsable;
    }

    public Integer getIdTipoEvaluacionArea() {
        return idTipoEvaluacionArea;
    }

    public void setIdTipoEvaluacionArea(Integer idTipoEvaluacionArea) {
        this.idTipoEvaluacionArea = idTipoEvaluacionArea;
    }

    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(short areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public Short getAreaConsulta() {
        return areaConsulta;
    }

    public void setAreaConsulta(Short areaConsulta) {
        this.areaConsulta = areaConsulta;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTablaResultados() {
        return tablaResultados;
    }

    public void setTablaResultados(String tablaResultados) {
        this.tablaResultados = tablaResultados;
    }

    public String getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(String cuestionario) {
        this.cuestionario = cuestionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEvaluacionArea != null ? idTipoEvaluacionArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionesAreas)) {
            return false;
        }
        EvaluacionesAreas other = (EvaluacionesAreas) object;
        if ((this.idTipoEvaluacionArea == null && other.idTipoEvaluacionArea != null) || (this.idTipoEvaluacionArea != null && !this.idTipoEvaluacionArea.equals(other.idTipoEvaluacionArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesAreas[ idTipoEvaluacionArea=" + idTipoEvaluacionArea + " ]";
    }
    
}
