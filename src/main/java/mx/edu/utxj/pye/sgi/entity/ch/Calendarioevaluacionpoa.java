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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Zabdiel Pèrez Morale
 */
@Entity
@Table(name = "calendarioevaluacionpoa", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendarioevaluacionpoa.findAll", query = "SELECT c FROM Calendarioevaluacionpoa c")
    , @NamedQuery(name = "Calendarioevaluacionpoa.findByEvaluacionPOA", query = "SELECT c FROM Calendarioevaluacionpoa c WHERE c.evaluacionPOA = :evaluacionPOA")
    , @NamedQuery(name = "Calendarioevaluacionpoa.findByFechaInicio", query = "SELECT c FROM Calendarioevaluacionpoa c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Calendarioevaluacionpoa.findByFechaFin", query = "SELECT c FROM Calendarioevaluacionpoa c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "Calendarioevaluacionpoa.findByMesEvaluacion", query = "SELECT c FROM Calendarioevaluacionpoa c WHERE c.mesEvaluacion = :mesEvaluacion")
    , @NamedQuery(name = "Calendarioevaluacionpoa.findByJustificacion", query = "SELECT c FROM Calendarioevaluacionpoa c WHERE c.justificacion = :justificacion")})
public class Calendarioevaluacionpoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evaluacionPOA")
    private Integer evaluacionPOA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaInicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaFin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "mesEvaluacion")
    private String mesEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "justificacion")
    private boolean justificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacionPOA")
    private List<Permisosevaluacionpoaex> permisosevaluacionpoaexList;
    @OneToMany(mappedBy = "evaluacion", fetch = FetchType.LAZY)
    private List<Procesopoa> procesopoaList;

    public Calendarioevaluacionpoa() {
    }

    public Calendarioevaluacionpoa(Integer evaluacionPOA) {
        this.evaluacionPOA = evaluacionPOA;
    }

    public Calendarioevaluacionpoa(Integer evaluacionPOA, Date fechaInicio, Date fechaFin, String mesEvaluacion, boolean justificacion) {
        this.evaluacionPOA = evaluacionPOA;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.mesEvaluacion = mesEvaluacion;
        this.justificacion = justificacion;
    }

    public Integer getEvaluacionPOA() {
        return evaluacionPOA;
    }

    public void setEvaluacionPOA(Integer evaluacionPOA) {
        this.evaluacionPOA = evaluacionPOA;
    }


    @XmlTransient
    public List<Procesopoa> getProcesopoaList() {
        return procesopoaList;
    }

    public void setProcesopoaList(List<Procesopoa> procesopoaList) {
        this.procesopoaList = procesopoaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacionPOA != null ? evaluacionPOA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calendarioevaluacionpoa)) {
            return false;
        }
        Calendarioevaluacionpoa other = (Calendarioevaluacionpoa) object;
        if ((this.evaluacionPOA == null && other.evaluacionPOA != null) || (this.evaluacionPOA != null && !this.evaluacionPOA.equals(other.evaluacionPOA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa[ evaluacionPOA=" + evaluacionPOA + " ]";
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

    public String getMesEvaluacion() {
        return mesEvaluacion;
    }

    public void setMesEvaluacion(String mesEvaluacion) {
        this.mesEvaluacion = mesEvaluacion;
    }

    public boolean getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(boolean justificacion) {
        this.justificacion = justificacion;
    }

    @XmlTransient
    public List<Permisosevaluacionpoaex> getPermisosevaluacionpoaexList() {
        return permisosevaluacionpoaexList;
    }

    public void setPermisosevaluacionpoaexList(List<Permisosevaluacionpoaex> permisosevaluacionpoaexList) {
        this.permisosevaluacionpoaexList = permisosevaluacionpoaexList;
    }
    
}
