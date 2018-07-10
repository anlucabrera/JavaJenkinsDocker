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
    , @NamedQuery(name = "ProgramasPertcal.findByPpc", query = "SELECT p FROM ProgramasPertcal p WHERE p.ppc = :ppc")
    , @NamedQuery(name = "ProgramasPertcal.findByProgramaInicial", query = "SELECT p FROM ProgramasPertcal p WHERE p.programaInicial = :programaInicial")
    , @NamedQuery(name = "ProgramasPertcal.findByProgramaActual", query = "SELECT p FROM ProgramasPertcal p WHERE p.programaActual = :programaActual")
    , @NamedQuery(name = "ProgramasPertcal.findByOrgAcreditador", query = "SELECT p FROM ProgramasPertcal p WHERE p.orgAcreditador = :orgAcreditador")
    , @NamedQuery(name = "ProgramasPertcal.findByFeciniAcred", query = "SELECT p FROM ProgramasPertcal p WHERE p.feciniAcred = :feciniAcred")
    , @NamedQuery(name = "ProgramasPertcal.findByFecfinAcred", query = "SELECT p FROM ProgramasPertcal p WHERE p.fecfinAcred = :fecfinAcred")
    , @NamedQuery(name = "ProgramasPertcal.findByFeciniReacred", query = "SELECT p FROM ProgramasPertcal p WHERE p.feciniReacred = :feciniReacred")
    , @NamedQuery(name = "ProgramasPertcal.findByFecfinReacred", query = "SELECT p FROM ProgramasPertcal p WHERE p.fecfinReacred = :fecfinReacred")
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
    @Column(name = "ppc")
    private short ppc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "programa_inicial")
    private String programaInicial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "programa_actual")
    private String programaActual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "org_acreditador")
    private int orgAcreditador;
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
    @Column(name = "fecini_reacred")
    @Temporal(TemporalType.DATE)
    private Date feciniReacred;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecfin_reacred")
    @Temporal(TemporalType.DATE)
    private Date fecfinReacred;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_estfac")
    @Temporal(TemporalType.DATE)
    private Date anioEstfac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_ultast")
    @Temporal(TemporalType.DATE)
    private Date anioUltast;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ProgramasPertcal() {
    }

    public ProgramasPertcal(Integer registro) {
        this.registro = registro;
    }

    public ProgramasPertcal(Integer registro, short ppc, String programaInicial, String programaActual, int orgAcreditador, Date feciniAcred, Date fecfinAcred, Date feciniReacred, Date fecfinReacred, Date anioEstfac, Date anioUltast) {
        this.registro = registro;
        this.ppc = ppc;
        this.programaInicial = programaInicial;
        this.programaActual = programaActual;
        this.orgAcreditador = orgAcreditador;
        this.feciniAcred = feciniAcred;
        this.fecfinAcred = fecfinAcred;
        this.feciniReacred = feciniReacred;
        this.fecfinReacred = fecfinReacred;
        this.anioEstfac = anioEstfac;
        this.anioUltast = anioUltast;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getPpc() {
        return ppc;
    }

    public void setPpc(short ppc) {
        this.ppc = ppc;
    }

    public String getProgramaInicial() {
        return programaInicial;
    }

    public void setProgramaInicial(String programaInicial) {
        this.programaInicial = programaInicial;
    }

    public String getProgramaActual() {
        return programaActual;
    }

    public void setProgramaActual(String programaActual) {
        this.programaActual = programaActual;
    }

    public int getOrgAcreditador() {
        return orgAcreditador;
    }

    public void setOrgAcreditador(int orgAcreditador) {
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

    public Date getFeciniReacred() {
        return feciniReacred;
    }

    public void setFeciniReacred(Date feciniReacred) {
        this.feciniReacred = feciniReacred;
    }

    public Date getFecfinReacred() {
        return fecfinReacred;
    }

    public void setFecfinReacred(Date fecfinReacred) {
        this.fecfinReacred = fecfinReacred;
    }

    public Date getAnioEstfac() {
        return anioEstfac;
    }

    public void setAnioEstfac(Date anioEstfac) {
        this.anioEstfac = anioEstfac;
    }

    public Date getAnioUltast() {
        return anioUltast;
    }

    public void setAnioUltast(Date anioUltast) {
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
