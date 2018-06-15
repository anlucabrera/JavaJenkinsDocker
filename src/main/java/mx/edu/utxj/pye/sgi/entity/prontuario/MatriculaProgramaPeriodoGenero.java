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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "matricula_programa_periodo_genero", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatriculaProgramaPeriodoGenero.findAll", query = "SELECT m FROM MatriculaProgramaPeriodoGenero m")
    , @NamedQuery(name = "MatriculaProgramaPeriodoGenero.findByMppg", query = "SELECT m FROM MatriculaProgramaPeriodoGenero m WHERE m.mppg = :mppg")
    , @NamedQuery(name = "MatriculaProgramaPeriodoGenero.findByHombres", query = "SELECT m FROM MatriculaProgramaPeriodoGenero m WHERE m.hombres = :hombres")
    , @NamedQuery(name = "MatriculaProgramaPeriodoGenero.findByMujeres", query = "SELECT m FROM MatriculaProgramaPeriodoGenero m WHERE m.mujeres = :mujeres")})
public class MatriculaProgramaPeriodoGenero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "mppg")
    private Integer mppg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private short hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private short mujeres;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos siglas;

    public MatriculaProgramaPeriodoGenero() {
    }

    public MatriculaProgramaPeriodoGenero(Integer mppg) {
        this.mppg = mppg;
    }

    public MatriculaProgramaPeriodoGenero(Integer mppg, short hombres, short mujeres) {
        this.mppg = mppg;
        this.hombres = hombres;
        this.mujeres = mujeres;
    }

    public Integer getMppg() {
        return mppg;
    }

    public void setMppg(Integer mppg) {
        this.mppg = mppg;
    }

    public short getHombres() {
        return hombres;
    }

    public void setHombres(short hombres) {
        this.hombres = hombres;
    }

    public short getMujeres() {
        return mujeres;
    }

    public void setMujeres(short mujeres) {
        this.mujeres = mujeres;
    }

    public PeriodosEscolares getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
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
        hash += (mppg != null ? mppg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatriculaProgramaPeriodoGenero)) {
            return false;
        }
        MatriculaProgramaPeriodoGenero other = (MatriculaProgramaPeriodoGenero) object;
        if ((this.mppg == null && other.mppg != null) || (this.mppg != null && !this.mppg.equals(other.mppg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MatriculaProgramaPeriodoGenero[ mppg=" + mppg + " ]";
    }
    
}
