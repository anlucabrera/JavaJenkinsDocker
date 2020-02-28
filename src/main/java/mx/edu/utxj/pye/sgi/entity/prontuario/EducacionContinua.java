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
@Table(name = "educacion_continua", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducacionContinua.findAll", query = "SELECT e FROM EducacionContinua e")
    , @NamedQuery(name = "EducacionContinua.findByCiclo", query = "SELECT e FROM EducacionContinua e WHERE e.educacionContinuaPK.ciclo = :ciclo")
    , @NamedQuery(name = "EducacionContinua.findByPeriodo", query = "SELECT e FROM EducacionContinua e WHERE e.educacionContinuaPK.periodo = :periodo")
    , @NamedQuery(name = "EducacionContinua.findByPrograma", query = "SELECT e FROM EducacionContinua e WHERE e.educacionContinuaPK.programa = :programa")
    , @NamedQuery(name = "EducacionContinua.findByTotalEgresados", query = "SELECT e FROM EducacionContinua e WHERE e.totalEgresados = :totalEgresados")
    , @NamedQuery(name = "EducacionContinua.findByPartCapacitacion", query = "SELECT e FROM EducacionContinua e WHERE e.partCapacitacion = :partCapacitacion")
    , @NamedQuery(name = "EducacionContinua.findByPartActualizacion", query = "SELECT e FROM EducacionContinua e WHERE e.partActualizacion = :partActualizacion")
    , @NamedQuery(name = "EducacionContinua.findByParDesprof", query = "SELECT e FROM EducacionContinua e WHERE e.parDesprof = :parDesprof")})
public class EducacionContinua implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EducacionContinuaPK educacionContinuaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_egresados")
    private int totalEgresados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "part_capacitacion")
    private int partCapacitacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "part_actualizacion")
    private int partActualizacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "par_desprof")
    private int parDesprof;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PeriodosEscolares periodosEscolares;
    @JoinColumn(name = "programa", referencedColumnName = "siglas", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramasEducativos programasEducativos;

    public EducacionContinua() {
    }

    public EducacionContinua(EducacionContinuaPK educacionContinuaPK) {
        this.educacionContinuaPK = educacionContinuaPK;
    }

    public EducacionContinua(EducacionContinuaPK educacionContinuaPK, int totalEgresados, int partCapacitacion, int partActualizacion, int parDesprof) {
        this.educacionContinuaPK = educacionContinuaPK;
        this.totalEgresados = totalEgresados;
        this.partCapacitacion = partCapacitacion;
        this.partActualizacion = partActualizacion;
        this.parDesprof = parDesprof;
    }

    public EducacionContinua(int ciclo, int periodo, String programa) {
        this.educacionContinuaPK = new EducacionContinuaPK(ciclo, periodo, programa);
    }

    public EducacionContinuaPK getEducacionContinuaPK() {
        return educacionContinuaPK;
    }

    public void setEducacionContinuaPK(EducacionContinuaPK educacionContinuaPK) {
        this.educacionContinuaPK = educacionContinuaPK;
    }

    public int getTotalEgresados() {
        return totalEgresados;
    }

    public void setTotalEgresados(int totalEgresados) {
        this.totalEgresados = totalEgresados;
    }

    public int getPartCapacitacion() {
        return partCapacitacion;
    }

    public void setPartCapacitacion(int partCapacitacion) {
        this.partCapacitacion = partCapacitacion;
    }

    public int getPartActualizacion() {
        return partActualizacion;
    }

    public void setPartActualizacion(int partActualizacion) {
        this.partActualizacion = partActualizacion;
    }

    public int getParDesprof() {
        return parDesprof;
    }

    public void setParDesprof(int parDesprof) {
        this.parDesprof = parDesprof;
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
        hash += (educacionContinuaPK != null ? educacionContinuaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EducacionContinua)) {
            return false;
        }
        EducacionContinua other = (EducacionContinua) object;
        if ((this.educacionContinuaPK == null && other.educacionContinuaPK != null) || (this.educacionContinuaPK != null && !this.educacionContinuaPK.equals(other.educacionContinuaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EducacionContinua[ educacionContinuaPK=" + educacionContinuaPK + " ]";
    }
    
}
