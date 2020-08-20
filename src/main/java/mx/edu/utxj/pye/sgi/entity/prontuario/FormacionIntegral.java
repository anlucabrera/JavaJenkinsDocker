/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "formacion_integral", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormacionIntegral.findAll", query = "SELECT f FROM FormacionIntegral f")
    , @NamedQuery(name = "FormacionIntegral.findByCiclo", query = "SELECT f FROM FormacionIntegral f WHERE f.formacionIntegralPK.ciclo = :ciclo")
    , @NamedQuery(name = "FormacionIntegral.findByPeriodo", query = "SELECT f FROM FormacionIntegral f WHERE f.formacionIntegralPK.periodo = :periodo")
    , @NamedQuery(name = "FormacionIntegral.findBySiglas", query = "SELECT f FROM FormacionIntegral f WHERE f.formacionIntegralPK.siglas = :siglas")
    , @NamedQuery(name = "FormacionIntegral.findByTotPart", query = "SELECT f FROM FormacionIntegral f WHERE f.totPart = :totPart")
    , @NamedQuery(name = "FormacionIntegral.findByActDeportivas", query = "SELECT f FROM FormacionIntegral f WHERE f.actDeportivas = :actDeportivas")
    , @NamedQuery(name = "FormacionIntegral.findByActCulturales", query = "SELECT f FROM FormacionIntegral f WHERE f.actCulturales = :actCulturales")
    , @NamedQuery(name = "FormacionIntegral.findByActComunitarias", query = "SELECT f FROM FormacionIntegral f WHERE f.actComunitarias = :actComunitarias")
    , @NamedQuery(name = "FormacionIntegral.findByActSalud", query = "SELECT f FROM FormacionIntegral f WHERE f.actSalud = :actSalud")
    , @NamedQuery(name = "FormacionIntegral.findByActPsicopedagogicas", query = "SELECT f FROM FormacionIntegral f WHERE f.actPsicopedagogicas = :actPsicopedagogicas")
    , @NamedQuery(name = "FormacionIntegral.findByActEmprendedores", query = "SELECT f FROM FormacionIntegral f WHERE f.actEmprendedores = :actEmprendedores")
    , @NamedQuery(name = "FormacionIntegral.findByActInnovtecnologica", query = "SELECT f FROM FormacionIntegral f WHERE f.actInnovtecnologica = :actInnovtecnologica")
    , @NamedQuery(name = "FormacionIntegral.findByActCreatividad", query = "SELECT f FROM FormacionIntegral f WHERE f.actCreatividad = :actCreatividad")})
public class FormacionIntegral implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FormacionIntegralPK formacionIntegralPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tot_part")
    private int totPart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_deportivas")
    private int actDeportivas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_culturales")
    private int actCulturales;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_comunitarias")
    private int actComunitarias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_salud")
    private int actSalud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_psicopedagogicas")
    private int actPsicopedagogicas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_emprendedores")
    private int actEmprendedores;
    @Basic(optional = false)
    @NotNull
    @Column(name = "act_innovtecnologica")
    private int actInnovtecnologica;
    @Column(name = "act_creatividad")
    private Integer actCreatividad;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodosEscolares;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos programasEducativos;

    public FormacionIntegral() {
    }

    public FormacionIntegral(FormacionIntegralPK formacionIntegralPK) {
        this.formacionIntegralPK = formacionIntegralPK;
    }

    public FormacionIntegral(FormacionIntegralPK formacionIntegralPK, int totPart, int actDeportivas, int actCulturales, int actComunitarias, int actSalud, int actPsicopedagogicas, int actEmprendedores, int actInnovtecnologica) {
        this.formacionIntegralPK = formacionIntegralPK;
        this.totPart = totPart;
        this.actDeportivas = actDeportivas;
        this.actCulturales = actCulturales;
        this.actComunitarias = actComunitarias;
        this.actSalud = actSalud;
        this.actPsicopedagogicas = actPsicopedagogicas;
        this.actEmprendedores = actEmprendedores;
        this.actInnovtecnologica = actInnovtecnologica;
    }

    public FormacionIntegral(int ciclo, int periodo, String siglas) {
        this.formacionIntegralPK = new FormacionIntegralPK(ciclo, periodo, siglas);
    }

    public FormacionIntegralPK getFormacionIntegralPK() {
        return formacionIntegralPK;
    }

    public void setFormacionIntegralPK(FormacionIntegralPK formacionIntegralPK) {
        this.formacionIntegralPK = formacionIntegralPK;
    }

    public int getTotPart() {
        return totPart;
    }

    public void setTotPart(int totPart) {
        this.totPart = totPart;
    }

    public int getActDeportivas() {
        return actDeportivas;
    }

    public void setActDeportivas(int actDeportivas) {
        this.actDeportivas = actDeportivas;
    }

    public int getActCulturales() {
        return actCulturales;
    }

    public void setActCulturales(int actCulturales) {
        this.actCulturales = actCulturales;
    }

    public int getActComunitarias() {
        return actComunitarias;
    }

    public void setActComunitarias(int actComunitarias) {
        this.actComunitarias = actComunitarias;
    }

    public int getActSalud() {
        return actSalud;
    }

    public void setActSalud(int actSalud) {
        this.actSalud = actSalud;
    }

    public int getActPsicopedagogicas() {
        return actPsicopedagogicas;
    }

    public void setActPsicopedagogicas(int actPsicopedagogicas) {
        this.actPsicopedagogicas = actPsicopedagogicas;
    }

    public int getActEmprendedores() {
        return actEmprendedores;
    }

    public void setActEmprendedores(int actEmprendedores) {
        this.actEmprendedores = actEmprendedores;
    }

    public int getActInnovtecnologica() {
        return actInnovtecnologica;
    }

    public void setActInnovtecnologica(int actInnovtecnologica) {
        this.actInnovtecnologica = actInnovtecnologica;
    }

    public Integer getActCreatividad() {
        return actCreatividad;
    }

    public void setActCreatividad(Integer actCreatividad) {
        this.actCreatividad = actCreatividad;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public PeriodosEscolares getPeriodosEscolares() {
        return periodosEscolares;
    }

    public void setPeriodosEscolares(PeriodosEscolares periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public ProgramasEducativos getProgramasEducativos() {
        return programasEducativos;
    }

    public void setProgramasEducativos(ProgramasEducativos programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formacionIntegralPK != null ? formacionIntegralPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormacionIntegral)) {
            return false;
        }
        FormacionIntegral other = (FormacionIntegral) object;
        if ((this.formacionIntegralPK == null && other.formacionIntegralPK != null) || (this.formacionIntegralPK != null && !this.formacionIntegralPK.equals(other.formacionIntegralPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.FormacionIntegral[ formacionIntegralPK=" + formacionIntegralPK + " ]";
    }
    
}
