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
@Table(name = "movilidad_docente", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovilidadDocente.findAll", query = "SELECT m FROM MovilidadDocente m")
    , @NamedQuery(name = "MovilidadDocente.findByCiclo", query = "SELECT m FROM MovilidadDocente m WHERE m.movilidadDocentePK.ciclo = :ciclo")
    , @NamedQuery(name = "MovilidadDocente.findByPeriodo", query = "SELECT m FROM MovilidadDocente m WHERE m.movilidadDocentePK.periodo = :periodo")
    , @NamedQuery(name = "MovilidadDocente.findByAreaAcademica", query = "SELECT m FROM MovilidadDocente m WHERE m.movilidadDocentePK.areaAcademica = :areaAcademica")
    , @NamedQuery(name = "MovilidadDocente.findByTotalProfesores", query = "SELECT m FROM MovilidadDocente m WHERE m.totalProfesores = :totalProfesores")
    , @NamedQuery(name = "MovilidadDocente.findByNacional", query = "SELECT m FROM MovilidadDocente m WHERE m.nacional = :nacional")
    , @NamedQuery(name = "MovilidadDocente.findByColombia", query = "SELECT m FROM MovilidadDocente m WHERE m.colombia = :colombia")
    , @NamedQuery(name = "MovilidadDocente.findByEcuador", query = "SELECT m FROM MovilidadDocente m WHERE m.ecuador = :ecuador")
    , @NamedQuery(name = "MovilidadDocente.findByCanada", query = "SELECT m FROM MovilidadDocente m WHERE m.canada = :canada")
    , @NamedQuery(name = "MovilidadDocente.findByCuba", query = "SELECT m FROM MovilidadDocente m WHERE m.cuba = :cuba")
    , @NamedQuery(name = "MovilidadDocente.findByEspania", query = "SELECT m FROM MovilidadDocente m WHERE m.espania = :espania")
    , @NamedQuery(name = "MovilidadDocente.findByUsa", query = "SELECT m FROM MovilidadDocente m WHERE m.usa = :usa")
    , @NamedQuery(name = "MovilidadDocente.findByVenezuela", query = "SELECT m FROM MovilidadDocente m WHERE m.venezuela = :venezuela")
    , @NamedQuery(name = "MovilidadDocente.findByPeru", query = "SELECT m FROM MovilidadDocente m WHERE m.peru = :peru")
    , @NamedQuery(name = "MovilidadDocente.findByGrecia", query = "SELECT m FROM MovilidadDocente m WHERE m.grecia = :grecia")
    , @NamedQuery(name = "MovilidadDocente.findByFrancia", query = "SELECT m FROM MovilidadDocente m WHERE m.francia = :francia")
    , @NamedQuery(name = "MovilidadDocente.findByChile", query = "SELECT m FROM MovilidadDocente m WHERE m.chile = :chile")
    , @NamedQuery(name = "MovilidadDocente.findByPanama", query = "SELECT m FROM MovilidadDocente m WHERE m.panama = :panama")
    , @NamedQuery(name = "MovilidadDocente.findByJapon", query = "SELECT m FROM MovilidadDocente m WHERE m.japon = :japon")
    , @NamedQuery(name = "MovilidadDocente.findByArgentina", query = "SELECT m FROM MovilidadDocente m WHERE m.argentina = :argentina")})
public class MovilidadDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovilidadDocentePK movilidadDocentePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_profesores")
    private int totalProfesores;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nacional")
    private int nacional;
    @Basic(optional = false)
    @NotNull
    @Column(name = "colombia")
    private int colombia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ecuador")
    private int ecuador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canada")
    private int canada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuba")
    private int cuba;
    @Basic(optional = false)
    @NotNull
    @Column(name = "espania")
    private int espania;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usa")
    private int usa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "venezuela")
    private int venezuela;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peru")
    private int peru;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grecia")
    private int grecia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "francia")
    private int francia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "chile")
    private int chile;
    @Basic(optional = false)
    @NotNull
    @Column(name = "panama")
    private int panama;
    @Basic(optional = false)
    @NotNull
    @Column(name = "japon")
    private int japon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "argentina")
    private int argentina;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PeriodosEscolares periodosEscolares;

    public MovilidadDocente() {
    }

    public MovilidadDocente(MovilidadDocentePK movilidadDocentePK) {
        this.movilidadDocentePK = movilidadDocentePK;
    }

    public MovilidadDocente(MovilidadDocentePK movilidadDocentePK, int totalProfesores, int nacional, int colombia, int ecuador, int canada, int cuba, int espania, int usa, int venezuela, int peru, int grecia, int francia, int chile, int panama, int japon, int argentina) {
        this.movilidadDocentePK = movilidadDocentePK;
        this.totalProfesores = totalProfesores;
        this.nacional = nacional;
        this.colombia = colombia;
        this.ecuador = ecuador;
        this.canada = canada;
        this.cuba = cuba;
        this.espania = espania;
        this.usa = usa;
        this.venezuela = venezuela;
        this.peru = peru;
        this.grecia = grecia;
        this.francia = francia;
        this.chile = chile;
        this.panama = panama;
        this.japon = japon;
        this.argentina = argentina;
    }

    public MovilidadDocente(int ciclo, int periodo, String areaAcademica) {
        this.movilidadDocentePK = new MovilidadDocentePK(ciclo, periodo, areaAcademica);
    }

    public MovilidadDocentePK getMovilidadDocentePK() {
        return movilidadDocentePK;
    }

    public void setMovilidadDocentePK(MovilidadDocentePK movilidadDocentePK) {
        this.movilidadDocentePK = movilidadDocentePK;
    }

    public int getTotalProfesores() {
        return totalProfesores;
    }

    public void setTotalProfesores(int totalProfesores) {
        this.totalProfesores = totalProfesores;
    }

    public int getNacional() {
        return nacional;
    }

    public void setNacional(int nacional) {
        this.nacional = nacional;
    }

    public int getColombia() {
        return colombia;
    }

    public void setColombia(int colombia) {
        this.colombia = colombia;
    }

    public int getEcuador() {
        return ecuador;
    }

    public void setEcuador(int ecuador) {
        this.ecuador = ecuador;
    }

    public int getCanada() {
        return canada;
    }

    public void setCanada(int canada) {
        this.canada = canada;
    }

    public int getCuba() {
        return cuba;
    }

    public void setCuba(int cuba) {
        this.cuba = cuba;
    }

    public int getEspania() {
        return espania;
    }

    public void setEspania(int espania) {
        this.espania = espania;
    }

    public int getUsa() {
        return usa;
    }

    public void setUsa(int usa) {
        this.usa = usa;
    }

    public int getVenezuela() {
        return venezuela;
    }

    public void setVenezuela(int venezuela) {
        this.venezuela = venezuela;
    }

    public int getPeru() {
        return peru;
    }

    public void setPeru(int peru) {
        this.peru = peru;
    }

    public int getGrecia() {
        return grecia;
    }

    public void setGrecia(int grecia) {
        this.grecia = grecia;
    }

    public int getFrancia() {
        return francia;
    }

    public void setFrancia(int francia) {
        this.francia = francia;
    }

    public int getChile() {
        return chile;
    }

    public void setChile(int chile) {
        this.chile = chile;
    }

    public int getPanama() {
        return panama;
    }

    public void setPanama(int panama) {
        this.panama = panama;
    }

    public int getJapon() {
        return japon;
    }

    public void setJapon(int japon) {
        this.japon = japon;
    }

    public int getArgentina() {
        return argentina;
    }

    public void setArgentina(int argentina) {
        this.argentina = argentina;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movilidadDocentePK != null ? movilidadDocentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovilidadDocente)) {
            return false;
        }
        MovilidadDocente other = (MovilidadDocente) object;
        if ((this.movilidadDocentePK == null && other.movilidadDocentePK != null) || (this.movilidadDocentePK != null && !this.movilidadDocentePK.equals(other.movilidadDocentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MovilidadDocente[ movilidadDocentePK=" + movilidadDocentePK + " ]";
    }
    
}
