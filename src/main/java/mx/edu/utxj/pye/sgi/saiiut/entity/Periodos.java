/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "periodos", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periodos.findAll", query = "SELECT p FROM Periodos p")
    , @NamedQuery(name = "Periodos.findByCvePeriodo", query = "SELECT p FROM Periodos p WHERE p.periodosPK.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "Periodos.findByCveUniversidad", query = "SELECT p FROM Periodos p WHERE p.periodosPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Periodos.findByCveCiclo", query = "SELECT p FROM Periodos p WHERE p.cveCiclo = :cveCiclo")
    , @NamedQuery(name = "Periodos.findByNumeroPeriodo", query = "SELECT p FROM Periodos p WHERE p.numeroPeriodo = :numeroPeriodo")
    , @NamedQuery(name = "Periodos.findByAno", query = "SELECT p FROM Periodos p WHERE p.ano = :ano")
    , @NamedQuery(name = "Periodos.findByFechaInicio", query = "SELECT p FROM Periodos p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Periodos.findByFechaFin", query = "SELECT p FROM Periodos p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "Periodos.findByActivo", query = "SELECT p FROM Periodos p WHERE p.activo = :activo")})
public class Periodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PeriodosPK periodosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_ciclo")
    private int cveCiclo;
    @Column(name = "numero_periodo")
    private Integer numeroPeriodo;
    @Column(name = "ano")
    private Integer ano;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "activo")
    private Boolean activo;

    public Periodos() {
    }

    public Periodos(PeriodosPK periodosPK) {
        this.periodosPK = periodosPK;
    }

    public Periodos(PeriodosPK periodosPK, int cveCiclo, Date fechaInicio, Date fechaFin) {
        this.periodosPK = periodosPK;
        this.cveCiclo = cveCiclo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Periodos(int cvePeriodo, int cveUniversidad) {
        this.periodosPK = new PeriodosPK(cvePeriodo, cveUniversidad);
    }

    public PeriodosPK getPeriodosPK() {
        return periodosPK;
    }

    public void setPeriodosPK(PeriodosPK periodosPK) {
        this.periodosPK = periodosPK;
    }

    public int getCveCiclo() {
        return cveCiclo;
    }

    public void setCveCiclo(int cveCiclo) {
        this.cveCiclo = cveCiclo;
    }

    public Integer getNumeroPeriodo() {
        return numeroPeriodo;
    }

    public void setNumeroPeriodo(Integer numeroPeriodo) {
        this.numeroPeriodo = numeroPeriodo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (periodosPK != null ? periodosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Periodos)) {
            return false;
        }
        Periodos other = (Periodos) object;
        if ((this.periodosPK == null && other.periodosPK != null) || (this.periodosPK != null && !this.periodosPK.equals(other.periodosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Periodos[ periodosPK=" + periodosPK + " ]";
    }
    
}
