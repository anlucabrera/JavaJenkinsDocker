/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "evaluaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluaciones.findAll", query = "SELECT e FROM Evaluaciones e")
    , @NamedQuery(name = "Evaluaciones.findByEvaluacion", query = "SELECT e FROM Evaluaciones e WHERE e.evaluacion = :evaluacion")
    , @NamedQuery(name = "Evaluaciones.findByPeriodo", query = "SELECT e FROM Evaluaciones e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "Evaluaciones.findByFechaInicio", query = "SELECT e FROM Evaluaciones e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Evaluaciones.findByFechaFin", query = "SELECT e FROM Evaluaciones e WHERE e.fechaFin = :fechaFin")
    , @NamedQuery(name = "Evaluaciones.findByTipo", query = "SELECT e FROM Evaluaciones e WHERE e.tipo = :tipo")})
public class Evaluaciones implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionConocimientoCodigoEticaResultados> evaluacionConocimientoCodigoEticaResultadosList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evaluacion")
    private Integer evaluacion;
    @Basic(optional = false)
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionTutoresResultados> evaluacionTutoresResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionDocentesMateriaResultados> evaluacionDocentesMateriaResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EncuestaServiciosResultados> encuestaServiciosResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EncuestaSatisfaccionEgresadosIng> encuestaSatisfaccionEgresadosIngList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionesClimaLaboralResultados> evaluacionesClimaLaboralResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionesControlInternoResultados> evaluacionesControlInternoResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionesEstudioSocioeconomicoResultados> evaluacionesEstudioSocioeconomicoResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionesPremiosResultados> evaluacionesPremiosResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluaciones")
    private List<EvaluacionEstadiaResultados> evaluacionEstadiaResultadosList;

    public Evaluaciones() {
    }

    public Evaluaciones(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Evaluaciones(Integer evaluacion, int periodo, Date fechaInicio, Date fechaFin, String tipo) {
        this.evaluacion = evaluacion;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<EvaluacionTutoresResultados> getEvaluacionTutoresResultadosList() {
        return evaluacionTutoresResultadosList;
    }

    public void setEvaluacionTutoresResultadosList(List<EvaluacionTutoresResultados> evaluacionTutoresResultadosList) {
        this.evaluacionTutoresResultadosList = evaluacionTutoresResultadosList;
    }

    @XmlTransient
    public List<EvaluacionDocentesMateriaResultados> getEvaluacionDocentesMateriaResultadosList() {
        return evaluacionDocentesMateriaResultadosList;
    }

    public void setEvaluacionDocentesMateriaResultadosList(List<EvaluacionDocentesMateriaResultados> evaluacionDocentesMateriaResultadosList) {
        this.evaluacionDocentesMateriaResultadosList = evaluacionDocentesMateriaResultadosList;
    }

    @XmlTransient
    public List<EncuestaServiciosResultados> getEncuestaServiciosResultadosList() {
        return encuestaServiciosResultadosList;
    }

    public void setEncuestaServiciosResultadosList(List<EncuestaServiciosResultados> encuestaServiciosResultadosList) {
        this.encuestaServiciosResultadosList = encuestaServiciosResultadosList;
    }

    @XmlTransient
    public List<EncuestaSatisfaccionEgresadosIng> getEncuestaSatisfaccionEgresadosIngList() {
        return encuestaSatisfaccionEgresadosIngList;
    }

    public void setEncuestaSatisfaccionEgresadosIngList(List<EncuestaSatisfaccionEgresadosIng> encuestaSatisfaccionEgresadosIngList) {
        this.encuestaSatisfaccionEgresadosIngList = encuestaSatisfaccionEgresadosIngList;
    }

    @XmlTransient
    public List<EvaluacionesClimaLaboralResultados> getEvaluacionesClimaLaboralResultadosList() {
        return evaluacionesClimaLaboralResultadosList;
    }

    public void setEvaluacionesClimaLaboralResultadosList(List<EvaluacionesClimaLaboralResultados> evaluacionesClimaLaboralResultadosList) {
        this.evaluacionesClimaLaboralResultadosList = evaluacionesClimaLaboralResultadosList;
    }

    @XmlTransient
    public List<EvaluacionesControlInternoResultados> getEvaluacionesControlInternoResultadosList() {
        return evaluacionesControlInternoResultadosList;
    }

    public void setEvaluacionesControlInternoResultadosList(List<EvaluacionesControlInternoResultados> evaluacionesControlInternoResultadosList) {
        this.evaluacionesControlInternoResultadosList = evaluacionesControlInternoResultadosList;
    }

    @XmlTransient
    public List<EvaluacionesEstudioSocioeconomicoResultados> getEvaluacionesEstudioSocioeconomicoResultadosList() {
        return evaluacionesEstudioSocioeconomicoResultadosList;
    }

    public void setEvaluacionesEstudioSocioeconomicoResultadosList(List<EvaluacionesEstudioSocioeconomicoResultados> evaluacionesEstudioSocioeconomicoResultadosList) {
        this.evaluacionesEstudioSocioeconomicoResultadosList = evaluacionesEstudioSocioeconomicoResultadosList;
    }

    @XmlTransient
    public List<EvaluacionesPremiosResultados> getEvaluacionesPremiosResultadosList() {
        return evaluacionesPremiosResultadosList;
    }

    public void setEvaluacionesPremiosResultadosList(List<EvaluacionesPremiosResultados> evaluacionesPremiosResultadosList) {
        this.evaluacionesPremiosResultadosList = evaluacionesPremiosResultadosList;
    }

    @XmlTransient
    public List<EvaluacionEstadiaResultados> getEvaluacionEstadiaResultadosList() {
        return evaluacionEstadiaResultadosList;
    }

    public void setEvaluacionEstadiaResultadosList(List<EvaluacionEstadiaResultados> evaluacionEstadiaResultadosList) {
        this.evaluacionEstadiaResultadosList = evaluacionEstadiaResultadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacion != null ? evaluacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evaluaciones)) {
            return false;
        }
        Evaluaciones other = (Evaluaciones) object;
        if ((this.evaluacion == null && other.evaluacion != null) || (this.evaluacion != null && !this.evaluacion.equals(other.evaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones[ evaluacion=" + evaluacion + " ]";
    }

    @XmlTransient
    public List<EvaluacionConocimientoCodigoEticaResultados> getEvaluacionConocimientoCodigoEticaResultadosList() {
        return evaluacionConocimientoCodigoEticaResultadosList;
    }

    public void setEvaluacionConocimientoCodigoEticaResultadosList(List<EvaluacionConocimientoCodigoEticaResultados> evaluacionConocimientoCodigoEticaResultadosList) {
        this.evaluacionConocimientoCodigoEticaResultadosList = evaluacionConocimientoCodigoEticaResultadosList;
    }
    
}
