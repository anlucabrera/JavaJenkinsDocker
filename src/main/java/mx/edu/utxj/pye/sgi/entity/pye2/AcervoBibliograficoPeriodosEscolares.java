/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "acervo_bibliografico_periodos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findAll", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByRegistro", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.registro = :registro")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByCicloEscolar", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByPeriodoEscolar", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByProgramaEducativo", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByEduTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.eduTit = :eduTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByEduVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.eduVol = :eduVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByArthumTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.arthumTit = :arthumTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByArthumVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.arthumVol = :arthumVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByCsaydTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.csaydTit = :csaydTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByCsaydVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.csaydVol = :csaydVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByCneycTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.cneycTit = :cneycTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByCneycVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.cneycVol = :cneycVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByImycTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.imycTit = :imycTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByImycVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.imycVol = :imycVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByAyvTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.ayvTit = :ayvTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByAyvVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.ayvVol = :ayvVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findBySalTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.salTit = :salTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findBySalVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.salVol = :salVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findBySerTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.serTit = :serTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findBySerVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.serVol = :serVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByRevTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.revTit = :revTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByRevVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.revVol = :revVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByFollTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.follTit = :follTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByFollVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.follVol = :follVol")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByVidTit", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.vidTit = :vidTit")
    , @NamedQuery(name = "AcervoBibliograficoPeriodosEscolares.findByVidVol", query = "SELECT a FROM AcervoBibliograficoPeriodosEscolares a WHERE a.vidVol = :vidVol")})
public class AcervoBibliograficoPeriodosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "edu_tit")
    private int eduTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "edu_vol")
    private int eduVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "arthum_tit")
    private int arthumTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "arthum_vol")
    private int arthumVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "csayd_tit")
    private int csaydTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "csayd_vol")
    private int csaydVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cneyc_tit")
    private int cneycTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cneyc_vol")
    private int cneycVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "imyc_tit")
    private int imycTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "imyc_vol")
    private int imycVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ayv_tit")
    private int ayvTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ayv_vol")
    private int ayvVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sal_tit")
    private int salTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sal_vol")
    private int salVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ser_tit")
    private int serTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ser_vol")
    private int serVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rev_tit")
    private int revTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rev_vol")
    private int revVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "foll_tit")
    private int follTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "foll_vol")
    private int follVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vid_tit")
    private int vidTit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vid_vol")
    private int vidVol;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public AcervoBibliograficoPeriodosEscolares() {
    }

    public AcervoBibliograficoPeriodosEscolares(Integer registro) {
        this.registro = registro;
    }

    public AcervoBibliograficoPeriodosEscolares(Integer registro, int cicloEscolar, int periodoEscolar, short programaEducativo, int eduTit, int eduVol, int arthumTit, int arthumVol, int csaydTit, int csaydVol, int cneycTit, int cneycVol, int imycTit, int imycVol, int ayvTit, int ayvVol, int salTit, int salVol, int serTit, int serVol, int revTit, int revVol, int follTit, int follVol, int vidTit, int vidVol) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.programaEducativo = programaEducativo;
        this.eduTit = eduTit;
        this.eduVol = eduVol;
        this.arthumTit = arthumTit;
        this.arthumVol = arthumVol;
        this.csaydTit = csaydTit;
        this.csaydVol = csaydVol;
        this.cneycTit = cneycTit;
        this.cneycVol = cneycVol;
        this.imycTit = imycTit;
        this.imycVol = imycVol;
        this.ayvTit = ayvTit;
        this.ayvVol = ayvVol;
        this.salTit = salTit;
        this.salVol = salVol;
        this.serTit = serTit;
        this.serVol = serVol;
        this.revTit = revTit;
        this.revVol = revVol;
        this.follTit = follTit;
        this.follVol = follVol;
        this.vidTit = vidTit;
        this.vidVol = vidVol;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getEduTit() {
        return eduTit;
    }

    public void setEduTit(int eduTit) {
        this.eduTit = eduTit;
    }

    public int getEduVol() {
        return eduVol;
    }

    public void setEduVol(int eduVol) {
        this.eduVol = eduVol;
    }

    public int getArthumTit() {
        return arthumTit;
    }

    public void setArthumTit(int arthumTit) {
        this.arthumTit = arthumTit;
    }

    public int getArthumVol() {
        return arthumVol;
    }

    public void setArthumVol(int arthumVol) {
        this.arthumVol = arthumVol;
    }

    public int getCsaydTit() {
        return csaydTit;
    }

    public void setCsaydTit(int csaydTit) {
        this.csaydTit = csaydTit;
    }

    public int getCsaydVol() {
        return csaydVol;
    }

    public void setCsaydVol(int csaydVol) {
        this.csaydVol = csaydVol;
    }

    public int getCneycTit() {
        return cneycTit;
    }

    public void setCneycTit(int cneycTit) {
        this.cneycTit = cneycTit;
    }

    public int getCneycVol() {
        return cneycVol;
    }

    public void setCneycVol(int cneycVol) {
        this.cneycVol = cneycVol;
    }

    public int getImycTit() {
        return imycTit;
    }

    public void setImycTit(int imycTit) {
        this.imycTit = imycTit;
    }

    public int getImycVol() {
        return imycVol;
    }

    public void setImycVol(int imycVol) {
        this.imycVol = imycVol;
    }

    public int getAyvTit() {
        return ayvTit;
    }

    public void setAyvTit(int ayvTit) {
        this.ayvTit = ayvTit;
    }

    public int getAyvVol() {
        return ayvVol;
    }

    public void setAyvVol(int ayvVol) {
        this.ayvVol = ayvVol;
    }

    public int getSalTit() {
        return salTit;
    }

    public void setSalTit(int salTit) {
        this.salTit = salTit;
    }

    public int getSalVol() {
        return salVol;
    }

    public void setSalVol(int salVol) {
        this.salVol = salVol;
    }

    public int getSerTit() {
        return serTit;
    }

    public void setSerTit(int serTit) {
        this.serTit = serTit;
    }

    public int getSerVol() {
        return serVol;
    }

    public void setSerVol(int serVol) {
        this.serVol = serVol;
    }

    public int getRevTit() {
        return revTit;
    }

    public void setRevTit(int revTit) {
        this.revTit = revTit;
    }

    public int getRevVol() {
        return revVol;
    }

    public void setRevVol(int revVol) {
        this.revVol = revVol;
    }

    public int getFollTit() {
        return follTit;
    }

    public void setFollTit(int follTit) {
        this.follTit = follTit;
    }

    public int getFollVol() {
        return follVol;
    }

    public void setFollVol(int follVol) {
        this.follVol = follVol;
    }

    public int getVidTit() {
        return vidTit;
    }

    public void setVidTit(int vidTit) {
        this.vidTit = vidTit;
    }

    public int getVidVol() {
        return vidVol;
    }

    public void setVidVol(int vidVol) {
        this.vidVol = vidVol;
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
        if (!(object instanceof AcervoBibliograficoPeriodosEscolares)) {
            return false;
        }
        AcervoBibliograficoPeriodosEscolares other = (AcervoBibliograficoPeriodosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.AcervoBibliograficoPeriodosEscolares[ registro=" + registro + " ]";
    }
    
}
