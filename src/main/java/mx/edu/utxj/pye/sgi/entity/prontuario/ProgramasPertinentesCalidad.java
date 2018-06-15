/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "programas_pertinentes_calidad", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasPertinentesCalidad.findAll", query = "SELECT p FROM ProgramasPertinentesCalidad p")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByIdProPertCal", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.idProPertCal = :idProPertCal")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByOrgAcred", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.orgAcred = :orgAcred")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByFechaIniAcred", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.fechaIniAcred = :fechaIniAcred")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByFechaFinAcred", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.fechaFinAcred = :fechaFinAcred")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByFechaIniReacred", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.fechaIniReacred = :fechaIniReacred")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByFechaFinReacred", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.fechaFinReacred = :fechaFinReacred")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByAnioEstFact", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.anioEstFact = :anioEstFact")
    , @NamedQuery(name = "ProgramasPertinentesCalidad.findByAnioUltAst", query = "SELECT p FROM ProgramasPertinentesCalidad p WHERE p.anioUltAst = :anioUltAst")})
public class ProgramasPertinentesCalidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_pro_pert_cal")
    private Integer idProPertCal;
    @Column(name = "org_acred")
    private Integer orgAcred;
    @Size(max = 10)
    @Column(name = "fecha_ini_acred")
    private String fechaIniAcred;
    @Size(max = 10)
    @Column(name = "fecha_fin_acred")
    private String fechaFinAcred;
    @Size(max = 10)
    @Column(name = "fecha_ini_reacred")
    private String fechaIniReacred;
    @Size(max = 10)
    @Column(name = "fecha_fin_reacred")
    private String fechaFinReacred;
    @Size(max = 255)
    @Column(name = "anio_est_fact")
    private String anioEstFact;
    @Size(max = 255)
    @Column(name = "anio_ult_ast")
    private String anioUltAst;
    @JoinColumn(name = "carrera_actual", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos carreraActual;
    @JoinColumn(name = "carrera_inicial", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos carreraInicial;
    @JoinColumn(name = "ciclo_escolar", referencedColumnName = "ciclo")
    @ManyToOne(optional = false)
    private CiclosEscolares cicloEscolar;

    public ProgramasPertinentesCalidad() {
    }

    public ProgramasPertinentesCalidad(Integer idProPertCal) {
        this.idProPertCal = idProPertCal;
    }

    public Integer getIdProPertCal() {
        return idProPertCal;
    }

    public void setIdProPertCal(Integer idProPertCal) {
        this.idProPertCal = idProPertCal;
    }

    public Integer getOrgAcred() {
        return orgAcred;
    }

    public void setOrgAcred(Integer orgAcred) {
        this.orgAcred = orgAcred;
    }

    public String getFechaIniAcred() {
        return fechaIniAcred;
    }

    public void setFechaIniAcred(String fechaIniAcred) {
        this.fechaIniAcred = fechaIniAcred;
    }

    public String getFechaFinAcred() {
        return fechaFinAcred;
    }

    public void setFechaFinAcred(String fechaFinAcred) {
        this.fechaFinAcred = fechaFinAcred;
    }

    public String getFechaIniReacred() {
        return fechaIniReacred;
    }

    public void setFechaIniReacred(String fechaIniReacred) {
        this.fechaIniReacred = fechaIniReacred;
    }

    public String getFechaFinReacred() {
        return fechaFinReacred;
    }

    public void setFechaFinReacred(String fechaFinReacred) {
        this.fechaFinReacred = fechaFinReacred;
    }

    public String getAnioEstFact() {
        return anioEstFact;
    }

    public void setAnioEstFact(String anioEstFact) {
        this.anioEstFact = anioEstFact;
    }

    public String getAnioUltAst() {
        return anioUltAst;
    }

    public void setAnioUltAst(String anioUltAst) {
        this.anioUltAst = anioUltAst;
    }

    public ProgramasEducativos getCarreraActual() {
        return carreraActual;
    }

    public void setCarreraActual(ProgramasEducativos carreraActual) {
        this.carreraActual = carreraActual;
    }

    public ProgramasEducativos getCarreraInicial() {
        return carreraInicial;
    }

    public void setCarreraInicial(ProgramasEducativos carreraInicial) {
        this.carreraInicial = carreraInicial;
    }

    public CiclosEscolares getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(CiclosEscolares cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProPertCal != null ? idProPertCal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasPertinentesCalidad)) {
            return false;
        }
        ProgramasPertinentesCalidad other = (ProgramasPertinentesCalidad) object;
        if ((this.idProPertCal == null && other.idProPertCal != null) || (this.idProPertCal != null && !this.idProPertCal.equals(other.idProPertCal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasPertinentesCalidad[ idProPertCal=" + idProPertCal + " ]";
    }
    
}
