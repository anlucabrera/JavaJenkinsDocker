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
import javax.persistence.Lob;
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
@Table(name = "planes_estudio", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanesEstudio.findAll", query = "SELECT p FROM PlanesEstudio p")
    , @NamedQuery(name = "PlanesEstudio.findByCvePlan", query = "SELECT p FROM PlanesEstudio p WHERE p.planesEstudioPK.cvePlan = :cvePlan")
    , @NamedQuery(name = "PlanesEstudio.findByCveCarrera", query = "SELECT p FROM PlanesEstudio p WHERE p.planesEstudioPK.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "PlanesEstudio.findByCveDivision", query = "SELECT p FROM PlanesEstudio p WHERE p.planesEstudioPK.cveDivision = :cveDivision")
    , @NamedQuery(name = "PlanesEstudio.findByCveUnidadAcademica", query = "SELECT p FROM PlanesEstudio p WHERE p.planesEstudioPK.cveUnidadAcademica = :cveUnidadAcademica")
    , @NamedQuery(name = "PlanesEstudio.findByCveUniversidad", query = "SELECT p FROM PlanesEstudio p WHERE p.planesEstudioPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "PlanesEstudio.findByCveCgut", query = "SELECT p FROM PlanesEstudio p WHERE p.cveCgut = :cveCgut")
    , @NamedQuery(name = "PlanesEstudio.findByCvePeriodoInicio", query = "SELECT p FROM PlanesEstudio p WHERE p.cvePeriodoInicio = :cvePeriodoInicio")
    , @NamedQuery(name = "PlanesEstudio.findByNombre", query = "SELECT p FROM PlanesEstudio p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PlanesEstudio.findByFechaInicio", query = "SELECT p FROM PlanesEstudio p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "PlanesEstudio.findByFechaFin", query = "SELECT p FROM PlanesEstudio p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "PlanesEstudio.findByVigente", query = "SELECT p FROM PlanesEstudio p WHERE p.vigente = :vigente")
    , @NamedQuery(name = "PlanesEstudio.findByCveAnioPlan", query = "SELECT p FROM PlanesEstudio p WHERE p.cveAnioPlan = :cveAnioPlan")
    , @NamedQuery(name = "PlanesEstudio.findByCveUnidadEstudio", query = "SELECT p FROM PlanesEstudio p WHERE p.cveUnidadEstudio = :cveUnidadEstudio")
    , @NamedQuery(name = "PlanesEstudio.findByTipo", query = "SELECT p FROM PlanesEstudio p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "PlanesEstudio.findByClaveDgp", query = "SELECT p FROM PlanesEstudio p WHERE p.claveDgp = :claveDgp")
    , @NamedQuery(name = "PlanesEstudio.findByCveModalidadEstudio", query = "SELECT p FROM PlanesEstudio p WHERE p.cveModalidadEstudio = :cveModalidadEstudio")})
public class PlanesEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanesEstudioPK planesEstudioPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "cve_cgut")
    private String cveCgut;
    @Column(name = "cve_periodo_inicio")
    private Integer cvePeriodoInicio;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Lob
    @Size(max = 65535)
    @Column(name = "notas")
    private String notas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigente")
    private boolean vigente;
    @Column(name = "cve_anio_plan")
    private Integer cveAnioPlan;
    @Column(name = "cve_unidad_estudio")
    private Integer cveUnidadEstudio;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "clave_dgp")
    private Integer claveDgp;
    @Column(name = "cve_modalidad_estudio")
    private Integer cveModalidadEstudio;

    public PlanesEstudio() {
    }

    public PlanesEstudio(PlanesEstudioPK planesEstudioPK) {
        this.planesEstudioPK = planesEstudioPK;
    }

    public PlanesEstudio(PlanesEstudioPK planesEstudioPK, String cveCgut, boolean vigente) {
        this.planesEstudioPK = planesEstudioPK;
        this.cveCgut = cveCgut;
        this.vigente = vigente;
    }

    public PlanesEstudio(int cvePlan, int cveCarrera, int cveDivision, int cveUnidadAcademica, int cveUniversidad) {
        this.planesEstudioPK = new PlanesEstudioPK(cvePlan, cveCarrera, cveDivision, cveUnidadAcademica, cveUniversidad);
    }

    public PlanesEstudioPK getPlanesEstudioPK() {
        return planesEstudioPK;
    }

    public void setPlanesEstudioPK(PlanesEstudioPK planesEstudioPK) {
        this.planesEstudioPK = planesEstudioPK;
    }

    public String getCveCgut() {
        return cveCgut;
    }

    public void setCveCgut(String cveCgut) {
        this.cveCgut = cveCgut;
    }

    public Integer getCvePeriodoInicio() {
        return cvePeriodoInicio;
    }

    public void setCvePeriodoInicio(Integer cvePeriodoInicio) {
        this.cvePeriodoInicio = cvePeriodoInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public boolean getVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    public Integer getCveAnioPlan() {
        return cveAnioPlan;
    }

    public void setCveAnioPlan(Integer cveAnioPlan) {
        this.cveAnioPlan = cveAnioPlan;
    }

    public Integer getCveUnidadEstudio() {
        return cveUnidadEstudio;
    }

    public void setCveUnidadEstudio(Integer cveUnidadEstudio) {
        this.cveUnidadEstudio = cveUnidadEstudio;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getClaveDgp() {
        return claveDgp;
    }

    public void setClaveDgp(Integer claveDgp) {
        this.claveDgp = claveDgp;
    }

    public Integer getCveModalidadEstudio() {
        return cveModalidadEstudio;
    }

    public void setCveModalidadEstudio(Integer cveModalidadEstudio) {
        this.cveModalidadEstudio = cveModalidadEstudio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planesEstudioPK != null ? planesEstudioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanesEstudio)) {
            return false;
        }
        PlanesEstudio other = (PlanesEstudio) object;
        if ((this.planesEstudioPK == null && other.planesEstudioPK != null) || (this.planesEstudioPK != null && !this.planesEstudioPK.equals(other.planesEstudioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.PlanesEstudio[ planesEstudioPK=" + planesEstudioPK + " ]";
    }
    
}
