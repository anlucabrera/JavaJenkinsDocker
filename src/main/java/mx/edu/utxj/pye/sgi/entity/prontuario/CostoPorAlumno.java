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
import javax.persistence.Entity;
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
@Table(name = "costo_por_alumno", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CostoPorAlumno.findAll", query = "SELECT c FROM CostoPorAlumno c")
    , @NamedQuery(name = "CostoPorAlumno.findByCiclo", query = "SELECT c FROM CostoPorAlumno c WHERE c.ciclo = :ciclo")
    , @NamedQuery(name = "CostoPorAlumno.findByPresupuestoAutorizado", query = "SELECT c FROM CostoPorAlumno c WHERE c.presupuestoAutorizado = :presupuestoAutorizado")})
public class CostoPorAlumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private Integer ciclo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "presupuesto_autorizado")
    private BigDecimal presupuestoAutorizado;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private CiclosEscolares ciclosEscolares;

    public CostoPorAlumno() {
    }

    public CostoPorAlumno(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public CostoPorAlumno(Integer ciclo, BigDecimal presupuestoAutorizado) {
        this.ciclo = ciclo;
        this.presupuestoAutorizado = presupuestoAutorizado;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public BigDecimal getPresupuestoAutorizado() {
        return presupuestoAutorizado;
    }

    public void setPresupuestoAutorizado(BigDecimal presupuestoAutorizado) {
        this.presupuestoAutorizado = presupuestoAutorizado;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ciclo != null ? ciclo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CostoPorAlumno)) {
            return false;
        }
        CostoPorAlumno other = (CostoPorAlumno) object;
        if ((this.ciclo == null && other.ciclo != null) || (this.ciclo != null && !this.ciclo.equals(other.ciclo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CostoPorAlumno[ ciclo=" + ciclo + " ]";
    }
    
}
