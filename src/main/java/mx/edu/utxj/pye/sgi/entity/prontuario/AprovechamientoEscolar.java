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
@Table(name = "aprovechamiento_escolar", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AprovechamientoEscolar.findAll", query = "SELECT a FROM AprovechamientoEscolar a")
    , @NamedQuery(name = "AprovechamientoEscolar.findByCiclo", query = "SELECT a FROM AprovechamientoEscolar a WHERE a.aprovechamientoEscolarPK.ciclo = :ciclo")
    , @NamedQuery(name = "AprovechamientoEscolar.findByPeriodo", query = "SELECT a FROM AprovechamientoEscolar a WHERE a.aprovechamientoEscolarPK.periodo = :periodo")
    , @NamedQuery(name = "AprovechamientoEscolar.findBySiglas", query = "SELECT a FROM AprovechamientoEscolar a WHERE a.aprovechamientoEscolarPK.siglas = :siglas")
    , @NamedQuery(name = "AprovechamientoEscolar.findByPromedio", query = "SELECT a FROM AprovechamientoEscolar a WHERE a.promedio = :promedio")})
public class AprovechamientoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AprovechamientoEscolarPK aprovechamientoEscolarPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private BigDecimal promedio;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PeriodosEscolares periodosEscolares;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramasEducativos programasEducativos;

    public AprovechamientoEscolar() {
    }

    public AprovechamientoEscolar(AprovechamientoEscolarPK aprovechamientoEscolarPK) {
        this.aprovechamientoEscolarPK = aprovechamientoEscolarPK;
    }

    public AprovechamientoEscolar(AprovechamientoEscolarPK aprovechamientoEscolarPK, BigDecimal promedio) {
        this.aprovechamientoEscolarPK = aprovechamientoEscolarPK;
        this.promedio = promedio;
    }

    public AprovechamientoEscolar(int ciclo, int periodo, String siglas) {
        this.aprovechamientoEscolarPK = new AprovechamientoEscolarPK(ciclo, periodo, siglas);
    }

    public AprovechamientoEscolarPK getAprovechamientoEscolarPK() {
        return aprovechamientoEscolarPK;
    }

    public void setAprovechamientoEscolarPK(AprovechamientoEscolarPK aprovechamientoEscolarPK) {
        this.aprovechamientoEscolarPK = aprovechamientoEscolarPK;
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
        hash += (aprovechamientoEscolarPK != null ? aprovechamientoEscolarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AprovechamientoEscolar)) {
            return false;
        }
        AprovechamientoEscolar other = (AprovechamientoEscolar) object;
        if ((this.aprovechamientoEscolarPK == null && other.aprovechamientoEscolarPK != null) || (this.aprovechamientoEscolarPK != null && !this.aprovechamientoEscolarPK.equals(other.aprovechamientoEscolarPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.AprovechamientoEscolar[ aprovechamientoEscolarPK=" + aprovechamientoEscolarPK + " ]";
    }
    
}
