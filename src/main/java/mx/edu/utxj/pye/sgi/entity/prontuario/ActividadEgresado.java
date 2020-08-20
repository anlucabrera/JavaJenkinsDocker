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
import javax.persistence.FetchType;
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
@Table(name = "actividad_egresado", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadEgresado.findAll", query = "SELECT a FROM ActividadEgresado a")
    , @NamedQuery(name = "ActividadEgresado.findByActagre", query = "SELECT a FROM ActividadEgresado a WHERE a.actagre = :actagre")
    , @NamedQuery(name = "ActividadEgresado.findByFechaCorte", query = "SELECT a FROM ActividadEgresado a WHERE a.fechaCorte = :fechaCorte")
    , @NamedQuery(name = "ActividadEgresado.findByTrabH", query = "SELECT a FROM ActividadEgresado a WHERE a.trabH = :trabH")
    , @NamedQuery(name = "ActividadEgresado.findByTrabM", query = "SELECT a FROM ActividadEgresado a WHERE a.trabM = :trabM")
    , @NamedQuery(name = "ActividadEgresado.findByNoTrabH", query = "SELECT a FROM ActividadEgresado a WHERE a.noTrabH = :noTrabH")
    , @NamedQuery(name = "ActividadEgresado.findByNoTrabM", query = "SELECT a FROM ActividadEgresado a WHERE a.noTrabM = :noTrabM")
    , @NamedQuery(name = "ActividadEgresado.findByEstH", query = "SELECT a FROM ActividadEgresado a WHERE a.estH = :estH")
    , @NamedQuery(name = "ActividadEgresado.findByEstM", query = "SELECT a FROM ActividadEgresado a WHERE a.estM = :estM")
    , @NamedQuery(name = "ActividadEgresado.findByTrabestH", query = "SELECT a FROM ActividadEgresado a WHERE a.trabestH = :trabestH")
    , @NamedQuery(name = "ActividadEgresado.findByTrabestM", query = "SELECT a FROM ActividadEgresado a WHERE a.trabestM = :trabestM")
    , @NamedQuery(name = "ActividadEgresado.findByLabhogH", query = "SELECT a FROM ActividadEgresado a WHERE a.labhogH = :labhogH")
    , @NamedQuery(name = "ActividadEgresado.findByLabhogM", query = "SELECT a FROM ActividadEgresado a WHERE a.labhogM = :labhogM")
    , @NamedQuery(name = "ActividadEgresado.findBySinInfH", query = "SELECT a FROM ActividadEgresado a WHERE a.sinInfH = :sinInfH")
    , @NamedQuery(name = "ActividadEgresado.findBySinInfM", query = "SELECT a FROM ActividadEgresado a WHERE a.sinInfM = :sinInfM")
    , @NamedQuery(name = "ActividadEgresado.findByFinH", query = "SELECT a FROM ActividadEgresado a WHERE a.finH = :finH")
    , @NamedQuery(name = "ActividadEgresado.findByFinM", query = "SELECT a FROM ActividadEgresado a WHERE a.finM = :finM")
    , @NamedQuery(name = "ActividadEgresado.findByAreaCompH", query = "SELECT a FROM ActividadEgresado a WHERE a.areaCompH = :areaCompH")
    , @NamedQuery(name = "ActividadEgresado.findByAreaCompM", query = "SELECT a FROM ActividadEgresado a WHERE a.areaCompM = :areaCompM")
    , @NamedQuery(name = "ActividadEgresado.findByColocadosH", query = "SELECT a FROM ActividadEgresado a WHERE a.colocadosH = :colocadosH")
    , @NamedQuery(name = "ActividadEgresado.findByColocadosM", query = "SELECT a FROM ActividadEgresado a WHERE a.colocadosM = :colocadosM")})
public class ActividadEgresado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "actagre")
    private Integer actagre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "fecha_corte")
    private String fechaCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trab_h")
    private int trabH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trab_m")
    private int trabM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_trab_h")
    private int noTrabH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_trab_m")
    private int noTrabM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_h")
    private int estH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_m")
    private int estM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trabest_h")
    private int trabestH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trabest_m")
    private int trabestM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "labhog_h")
    private int labhogH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "labhog_m")
    private int labhogM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_inf_h")
    private int sinInfH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_inf_m")
    private int sinInfM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin_h")
    private int finH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin_m")
    private int finM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_comp_h")
    private int areaCompH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_comp_m")
    private int areaCompM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "colocados_h")
    private int colocadosH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "colocados_m")
    private int colocadosM;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Generaciones generacion;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos siglas;

    public ActividadEgresado() {
    }

    public ActividadEgresado(Integer actagre) {
        this.actagre = actagre;
    }

    public ActividadEgresado(Integer actagre, String fechaCorte, int trabH, int trabM, int noTrabH, int noTrabM, int estH, int estM, int trabestH, int trabestM, int labhogH, int labhogM, int sinInfH, int sinInfM, int finH, int finM, int areaCompH, int areaCompM, int colocadosH, int colocadosM) {
        this.actagre = actagre;
        this.fechaCorte = fechaCorte;
        this.trabH = trabH;
        this.trabM = trabM;
        this.noTrabH = noTrabH;
        this.noTrabM = noTrabM;
        this.estH = estH;
        this.estM = estM;
        this.trabestH = trabestH;
        this.trabestM = trabestM;
        this.labhogH = labhogH;
        this.labhogM = labhogM;
        this.sinInfH = sinInfH;
        this.sinInfM = sinInfM;
        this.finH = finH;
        this.finM = finM;
        this.areaCompH = areaCompH;
        this.areaCompM = areaCompM;
        this.colocadosH = colocadosH;
        this.colocadosM = colocadosM;
    }

    public Integer getActagre() {
        return actagre;
    }

    public void setActagre(Integer actagre) {
        this.actagre = actagre;
    }

    public String getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(String fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public int getTrabH() {
        return trabH;
    }

    public void setTrabH(int trabH) {
        this.trabH = trabH;
    }

    public int getTrabM() {
        return trabM;
    }

    public void setTrabM(int trabM) {
        this.trabM = trabM;
    }

    public int getNoTrabH() {
        return noTrabH;
    }

    public void setNoTrabH(int noTrabH) {
        this.noTrabH = noTrabH;
    }

    public int getNoTrabM() {
        return noTrabM;
    }

    public void setNoTrabM(int noTrabM) {
        this.noTrabM = noTrabM;
    }

    public int getEstH() {
        return estH;
    }

    public void setEstH(int estH) {
        this.estH = estH;
    }

    public int getEstM() {
        return estM;
    }

    public void setEstM(int estM) {
        this.estM = estM;
    }

    public int getTrabestH() {
        return trabestH;
    }

    public void setTrabestH(int trabestH) {
        this.trabestH = trabestH;
    }

    public int getTrabestM() {
        return trabestM;
    }

    public void setTrabestM(int trabestM) {
        this.trabestM = trabestM;
    }

    public int getLabhogH() {
        return labhogH;
    }

    public void setLabhogH(int labhogH) {
        this.labhogH = labhogH;
    }

    public int getLabhogM() {
        return labhogM;
    }

    public void setLabhogM(int labhogM) {
        this.labhogM = labhogM;
    }

    public int getSinInfH() {
        return sinInfH;
    }

    public void setSinInfH(int sinInfH) {
        this.sinInfH = sinInfH;
    }

    public int getSinInfM() {
        return sinInfM;
    }

    public void setSinInfM(int sinInfM) {
        this.sinInfM = sinInfM;
    }

    public int getFinH() {
        return finH;
    }

    public void setFinH(int finH) {
        this.finH = finH;
    }

    public int getFinM() {
        return finM;
    }

    public void setFinM(int finM) {
        this.finM = finM;
    }

    public int getAreaCompH() {
        return areaCompH;
    }

    public void setAreaCompH(int areaCompH) {
        this.areaCompH = areaCompH;
    }

    public int getAreaCompM() {
        return areaCompM;
    }

    public void setAreaCompM(int areaCompM) {
        this.areaCompM = areaCompM;
    }

    public int getColocadosH() {
        return colocadosH;
    }

    public void setColocadosH(int colocadosH) {
        this.colocadosH = colocadosH;
    }

    public int getColocadosM() {
        return colocadosM;
    }

    public void setColocadosM(int colocadosM) {
        this.colocadosM = colocadosM;
    }

    public Generaciones getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public ProgramasEducativos getSiglas() {
        return siglas;
    }

    public void setSiglas(ProgramasEducativos siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actagre != null ? actagre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadEgresado)) {
            return false;
        }
        ActividadEgresado other = (ActividadEgresado) object;
        if ((this.actagre == null && other.actagre != null) || (this.actagre != null && !this.actagre.equals(other.actagre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ActividadEgresado[ actagre=" + actagre + " ]";
    }
    
}
