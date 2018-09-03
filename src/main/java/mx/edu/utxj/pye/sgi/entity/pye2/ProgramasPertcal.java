/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "programas_pertcal", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasPertcal.findAll", query = "SELECT p FROM ProgramasPertcal p")
    , @NamedQuery(name = "ProgramasPertcal.findByRegistro", query = "SELECT p FROM ProgramasPertcal p WHERE p.registro = :registro")
    , @NamedQuery(name = "ProgramasPertcal.findByProgramaEducativo", query = "SELECT p FROM ProgramasPertcal p WHERE p.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "ProgramasPertcal.findByAnioInicio", query = "SELECT p FROM ProgramasPertcal p WHERE p.anioInicio = :anioInicio")
    , @NamedQuery(name = "ProgramasPertcal.findByEvaluable", query = "SELECT p FROM ProgramasPertcal p WHERE p.evaluable = :evaluable")
    , @NamedQuery(name = "ProgramasPertcal.findByPertinente", query = "SELECT p FROM ProgramasPertcal p WHERE p.pertinente = :pertinente")
    , @NamedQuery(name = "ProgramasPertcal.findByOrgAcreditador", query = "SELECT p FROM ProgramasPertcal p WHERE p.orgAcreditador = :orgAcreditador")
    , @NamedQuery(name = "ProgramasPertcal.findByFeciniAcred", query = "SELECT p FROM ProgramasPertcal p WHERE p.feciniAcred = :feciniAcred")
    , @NamedQuery(name = "ProgramasPertcal.findByFecfinAcred", query = "SELECT p FROM ProgramasPertcal p WHERE p.fecfinAcred = :fecfinAcred")
    , @NamedQuery(name = "ProgramasPertcal.findByAnioEstfac", query = "SELECT p FROM ProgramasPertcal p WHERE p.anioEstfac = :anioEstfac")
    , @NamedQuery(name = "ProgramasPertcal.findByAnioUltast", query = "SELECT p FROM ProgramasPertcal p WHERE p.anioUltast = :anioUltast")})
public class ProgramasPertcal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_inicio")
    private short anioInicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "evaluable")
    private String evaluable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "pertinente")
    private String pertinente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "org_acreditador")
    private String orgAcreditador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecini_acred")
    @Temporal(TemporalType.DATE)
    private Date feciniAcred;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecfin_acred")
    @Temporal(TemporalType.DATE)
    private Date fecfinAcred;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_estfac")
    private short anioEstfac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_ultast")
    private short anioUltast;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ProgramasPertcal() {
    }

    public ProgramasPertcal(Integer registro) {
        this.registro = registro;
    }

    public ProgramasPertcal(Integer registro, short programaEducativo, short anioInicio, String evaluable, String pertinente, String orgAcreditador, Date feciniAcred, Date fecfinAcred, short anioEstfac, short anioUltast) {
        this.registro = registro;
        this.programaEducativo = programaEducativo;
        this.anioInicio = anioInicio;
        this.evaluable = evaluable;
        this.pertinente = pertinente;
        this.orgAcreditador = orgAcreditador;
        this.feciniAcred = feciniAcred;
        this.fecfinAcred = fecfinAcred;
        this.anioEstfac = anioEstfac;
        this.anioUltast = anioUltast;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public short getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(short anioInicio) {
        this.anioInicio = anioInicio;
    }

    public String getEvaluable() {
        return evaluable;
    }

    public void setEvaluable(String evaluable) {
        this.evaluable = evaluable;
    }

    public String getPertinente() {
        return pertinente;
    }

    public void setPertinente(String pertinente) {
        this.pertinente = pertinente;
    }

    public String getOrgAcreditador() {
        return orgAcreditador;
    }

    public void setOrgAcreditador(String orgAcreditador) {
        this.orgAcreditador = orgAcreditador;
    }

    public Date getFeciniAcred() {
        return feciniAcred;
    }

    public void setFeciniAcred(Date feciniAcred) {
        this.feciniAcred = feciniAcred;
    }

    public Date getFecfinAcred() {
        return fecfinAcred;
    }

    public void setFecfinAcred(Date fecfinAcred) {
        this.fecfinAcred = fecfinAcred;
    }

    public short getAnioEstfac() {
        return anioEstfac;
    }

    public void setAnioEstfac(short anioEstfac) {
        this.anioEstfac = anioEstfac;
    }

    public short getAnioUltast() {
        return anioUltast;
    }

    public void setAnioUltast(short anioUltast) {
        this.anioUltast = anioUltast;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasPertcal)) {
            return false;
        }
        ProgramasPertcal other = (ProgramasPertcal) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProgramasPertcal[ registro=" + registro + " ]";
    }
    
}
