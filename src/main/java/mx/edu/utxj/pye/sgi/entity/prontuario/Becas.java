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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "becas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Becas.findAll", query = "SELECT b FROM Becas b")
    , @NamedQuery(name = "Becas.findByBeca", query = "SELECT b FROM Becas b WHERE b.beca = :beca")
    , @NamedQuery(name = "Becas.findByMatricula", query = "SELECT b FROM Becas b WHERE b.matricula = :matricula")
    , @NamedQuery(name = "Becas.findByCuatrimestre", query = "SELECT b FROM Becas b WHERE b.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "Becas.findByHombres", query = "SELECT b FROM Becas b WHERE b.hombres = :hombres")
    , @NamedQuery(name = "Becas.findByMujeres", query = "SELECT b FROM Becas b WHERE b.mujeres = :mujeres")})
public class Becas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "beca")
    private Integer beca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private short matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private short cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private short hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private short mujeres;
    @JoinColumn(name = "beca_tipo", referencedColumnName = "beca_tipo")
    @ManyToOne(optional = false)
    private BecaTipos becaTipo;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos siglas;

    public Becas() {
    }

    public Becas(Integer beca) {
        this.beca = beca;
    }

    public Becas(Integer beca, short matricula, short cuatrimestre, short hombres, short mujeres) {
        this.beca = beca;
        this.matricula = matricula;
        this.cuatrimestre = cuatrimestre;
        this.hombres = hombres;
        this.mujeres = mujeres;
    }

    public Integer getBeca() {
        return beca;
    }

    public void setBeca(Integer beca) {
        this.beca = beca;
    }

    public short getMatricula() {
        return matricula;
    }

    public void setMatricula(short matricula) {
        this.matricula = matricula;
    }

    public short getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(short cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
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

    public BecaTipos getBecaTipo() {
        return becaTipo;
    }

    public void setBecaTipo(BecaTipos becaTipo) {
        this.becaTipo = becaTipo;
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
        hash += (beca != null ? beca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Becas)) {
            return false;
        }
        Becas other = (Becas) object;
        if ((this.beca == null && other.beca != null) || (this.beca != null && !this.beca.equals(other.beca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Becas[ beca=" + beca + " ]";
    }
    
}
