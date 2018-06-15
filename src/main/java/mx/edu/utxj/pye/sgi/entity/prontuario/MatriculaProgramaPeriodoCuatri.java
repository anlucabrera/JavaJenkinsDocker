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
@Table(name = "matricula_programa_periodo_cuatri", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatriculaProgramaPeriodoCuatri.findAll", query = "SELECT m FROM MatriculaProgramaPeriodoCuatri m")
    , @NamedQuery(name = "MatriculaProgramaPeriodoCuatri.findByMppc", query = "SELECT m FROM MatriculaProgramaPeriodoCuatri m WHERE m.mppc = :mppc")
    , @NamedQuery(name = "MatriculaProgramaPeriodoCuatri.findByCuatrimestre", query = "SELECT m FROM MatriculaProgramaPeriodoCuatri m WHERE m.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "MatriculaProgramaPeriodoCuatri.findByHombres", query = "SELECT m FROM MatriculaProgramaPeriodoCuatri m WHERE m.hombres = :hombres")
    , @NamedQuery(name = "MatriculaProgramaPeriodoCuatri.findByMujeres", query = "SELECT m FROM MatriculaProgramaPeriodoCuatri m WHERE m.mujeres = :mujeres")
    , @NamedQuery(name = "MatriculaProgramaPeriodoCuatri.findByMatricula", query = "SELECT m FROM MatriculaProgramaPeriodoCuatri m WHERE m.matricula = :matricula")})
public class MatriculaProgramaPeriodoCuatri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mppc")
    private Integer mppc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private boolean cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private short hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private short mujeres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private short matricula;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos siglas;

    public MatriculaProgramaPeriodoCuatri() {
    }

    public MatriculaProgramaPeriodoCuatri(Integer mppc) {
        this.mppc = mppc;
    }

    public MatriculaProgramaPeriodoCuatri(Integer mppc, boolean cuatrimestre, short hombres, short mujeres, short matricula) {
        this.mppc = mppc;
        this.cuatrimestre = cuatrimestre;
        this.hombres = hombres;
        this.mujeres = mujeres;
        this.matricula = matricula;
    }

    public Integer getMppc() {
        return mppc;
    }

    public void setMppc(Integer mppc) {
        this.mppc = mppc;
    }

    public boolean getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(boolean cuatrimestre) {
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

    public short getMatricula() {
        return matricula;
    }

    public void setMatricula(short matricula) {
        this.matricula = matricula;
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
        hash += (mppc != null ? mppc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatriculaProgramaPeriodoCuatri)) {
            return false;
        }
        MatriculaProgramaPeriodoCuatri other = (MatriculaProgramaPeriodoCuatri) object;
        if ((this.mppc == null && other.mppc != null) || (this.mppc != null && !this.mppc.equals(other.mppc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MatriculaProgramaPeriodoCuatri[ mppc=" + mppc + " ]";
    }
    
}
