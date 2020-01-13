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
@Table(name = "capacidad_instalada", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CapacidadInstalada.findAll", query = "SELECT c FROM CapacidadInstalada c")
    , @NamedQuery(name = "CapacidadInstalada.findByCi", query = "SELECT c FROM CapacidadInstalada c WHERE c.ci = :ci")
    , @NamedQuery(name = "CapacidadInstalada.findByCapIns", query = "SELECT c FROM CapacidadInstalada c WHERE c.capIns = :capIns")
    , @NamedQuery(name = "CapacidadInstalada.findByPorcentaje", query = "SELECT c FROM CapacidadInstalada c WHERE c.porcentaje = :porcentaje")})
public class CapacidadInstalada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ci")
    private Integer ci;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cap_ins")
    private int capIns;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private int porcentaje;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false)
    private CiclosEscolares ciclo;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;

    public CapacidadInstalada() {
    }

    public CapacidadInstalada(Integer ci) {
        this.ci = ci;
    }

    public CapacidadInstalada(Integer ci, int capIns, int porcentaje) {
        this.ci = ci;
        this.capIns = capIns;
        this.porcentaje = porcentaje;
    }

    public Integer getCi() {
        return ci;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    public int getCapIns() {
        return capIns;
    }

    public void setCapIns(int capIns) {
        this.capIns = capIns;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public CiclosEscolares getCiclo() {
        return ciclo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
    }

    public PeriodosEscolares getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ci != null ? ci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CapacidadInstalada)) {
            return false;
        }
        CapacidadInstalada other = (CapacidadInstalada) object;
        if ((this.ci == null && other.ci != null) || (this.ci != null && !this.ci.equals(other.ci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CapacidadInstalada[ ci=" + ci + " ]";
    }
    
}
