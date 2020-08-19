/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "procesos_inscripcion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesosInscripcion.findAll", query = "SELECT p FROM ProcesosInscripcion p")
    , @NamedQuery(name = "ProcesosInscripcion.findByIdProcesosInscripcion", query = "SELECT p FROM ProcesosInscripcion p WHERE p.idProcesosInscripcion = :idProcesosInscripcion")
    , @NamedQuery(name = "ProcesosInscripcion.findByFechaInicio", query = "SELECT p FROM ProcesosInscripcion p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ProcesosInscripcion.findByFechaFin", query = "SELECT p FROM ProcesosInscripcion p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "ProcesosInscripcion.findByActivoNi", query = "SELECT p FROM ProcesosInscripcion p WHERE p.activoNi = :activoNi")
    , @NamedQuery(name = "ProcesosInscripcion.findByActivoIng", query = "SELECT p FROM ProcesosInscripcion p WHERE p.activoIng = :activoIng")
    , @NamedQuery(name = "ProcesosInscripcion.findByActivoRe", query = "SELECT p FROM ProcesosInscripcion p WHERE p.activoRe = :activoRe")
    , @NamedQuery(name = "ProcesosInscripcion.findByIdPeriodo", query = "SELECT p FROM ProcesosInscripcion p WHERE p.idPeriodo = :idPeriodo")})
public class ProcesosInscripcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_procesos_inscripcion")
    private Integer idProcesosInscripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "activo_ni")
    private Boolean activoNi;
    @Column(name = "activo_ing")
    private Boolean activoIng;
    @Column(name = "activo_re")
    private Boolean activoRe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_periodo")
    private int idPeriodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProcesoInscripcion", fetch = FetchType.LAZY)
    private List<Aspirante> aspiranteList;

    public ProcesosInscripcion() {
    }

    public ProcesosInscripcion(Integer idProcesosInscripcion) {
        this.idProcesosInscripcion = idProcesosInscripcion;
    }

    public ProcesosInscripcion(Integer idProcesosInscripcion, Date fechaInicio, Date fechaFin, int idPeriodo) {
        this.idProcesosInscripcion = idProcesosInscripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idPeriodo = idPeriodo;
    }

    public Integer getIdProcesosInscripcion() {
        return idProcesosInscripcion;
    }

    public void setIdProcesosInscripcion(Integer idProcesosInscripcion) {
        this.idProcesosInscripcion = idProcesosInscripcion;
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

    public Boolean getActivoNi() {
        return activoNi;
    }

    public void setActivoNi(Boolean activoNi) {
        this.activoNi = activoNi;
    }

    public Boolean getActivoIng() {
        return activoIng;
    }

    public void setActivoIng(Boolean activoIng) {
        this.activoIng = activoIng;
    }

    public Boolean getActivoRe() {
        return activoRe;
    }

    public void setActivoRe(Boolean activoRe) {
        this.activoRe = activoRe;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    @XmlTransient
    public List<Aspirante> getAspiranteList() {
        return aspiranteList;
    }

    public void setAspiranteList(List<Aspirante> aspiranteList) {
        this.aspiranteList = aspiranteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProcesosInscripcion != null ? idProcesosInscripcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesosInscripcion)) {
            return false;
        }
        ProcesosInscripcion other = (ProcesosInscripcion) object;
        if ((this.idProcesosInscripcion == null && other.idProcesosInscripcion != null) || (this.idProcesosInscripcion != null && !this.idProcesosInscripcion.equals(other.idProcesosInscripcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion[ idProcesosInscripcion=" + idProcesosInscripcion + " ]";
    }
    
}
