/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "calificaciones_cuatrimestre", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionesCuatrimestre.findAll", query = "SELECT c FROM CalificacionesCuatrimestre c")
    , @NamedQuery(name = "CalificacionesCuatrimestre.findByCiclo", query = "SELECT c FROM CalificacionesCuatrimestre c WHERE c.calificacionesCuatrimestrePK.ciclo = :ciclo")
    , @NamedQuery(name = "CalificacionesCuatrimestre.findByPeriodo", query = "SELECT c FROM CalificacionesCuatrimestre c WHERE c.calificacionesCuatrimestrePK.periodo = :periodo")
    , @NamedQuery(name = "CalificacionesCuatrimestre.findBySiglas", query = "SELECT c FROM CalificacionesCuatrimestre c WHERE c.calificacionesCuatrimestrePK.siglas = :siglas")
    , @NamedQuery(name = "CalificacionesCuatrimestre.findByMateria", query = "SELECT c FROM CalificacionesCuatrimestre c WHERE c.calificacionesCuatrimestrePK.materia = :materia")
    , @NamedQuery(name = "CalificacionesCuatrimestre.findByPromedio", query = "SELECT c FROM CalificacionesCuatrimestre c WHERE c.promedio = :promedio")})
public class CalificacionesCuatrimestre implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionesCuatrimestrePK calificacionesCuatrimestrePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private BigDecimal promedio;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "materia", referencedColumnName = "cve_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Materias materias;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodosEscolares;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos programasEducativos;

    public CalificacionesCuatrimestre() {
    }

    public CalificacionesCuatrimestre(CalificacionesCuatrimestrePK calificacionesCuatrimestrePK) {
        this.calificacionesCuatrimestrePK = calificacionesCuatrimestrePK;
    }

    public CalificacionesCuatrimestre(CalificacionesCuatrimestrePK calificacionesCuatrimestrePK, BigDecimal promedio) {
        this.calificacionesCuatrimestrePK = calificacionesCuatrimestrePK;
        this.promedio = promedio;
    }

    public CalificacionesCuatrimestre(int ciclo, int periodo, String siglas, String materia) {
        this.calificacionesCuatrimestrePK = new CalificacionesCuatrimestrePK(ciclo, periodo, siglas, materia);
    }

    public CalificacionesCuatrimestrePK getCalificacionesCuatrimestrePK() {
        return calificacionesCuatrimestrePK;
    }

    public void setCalificacionesCuatrimestrePK(CalificacionesCuatrimestrePK calificacionesCuatrimestrePK) {
        this.calificacionesCuatrimestrePK = calificacionesCuatrimestrePK;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public Materias getMaterias() {
        return materias;
    }

    public void setMaterias(Materias materias) {
        this.materias = materias;
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
        hash += (calificacionesCuatrimestrePK != null ? calificacionesCuatrimestrePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionesCuatrimestre)) {
            return false;
        }
        CalificacionesCuatrimestre other = (CalificacionesCuatrimestre) object;
        if ((this.calificacionesCuatrimestrePK == null && other.calificacionesCuatrimestrePK != null) || (this.calificacionesCuatrimestrePK != null && !this.calificacionesCuatrimestrePK.equals(other.calificacionesCuatrimestrePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CalificacionesCuatrimestre[ calificacionesCuatrimestrePK=" + calificacionesCuatrimestrePK + " ]";
    }
    
}
