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
@Table(name = "distribucion_aulas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DistribucionAulas.findAll", query = "SELECT d FROM DistribucionAulas d")
    , @NamedQuery(name = "DistribucionAulas.findByDa", query = "SELECT d FROM DistribucionAulas d WHERE d.da = :da")
    , @NamedQuery(name = "DistribucionAulas.findByEdificio", query = "SELECT d FROM DistribucionAulas d WHERE d.edificio = :edificio")
    , @NamedQuery(name = "DistribucionAulas.findByNumero", query = "SELECT d FROM DistribucionAulas d WHERE d.numero = :numero")
    , @NamedQuery(name = "DistribucionAulas.findByCapTurno", query = "SELECT d FROM DistribucionAulas d WHERE d.capTurno = :capTurno")
    , @NamedQuery(name = "DistribucionAulas.findByAcondicionadas", query = "SELECT d FROM DistribucionAulas d WHERE d.acondicionadas = :acondicionadas")})
public class DistribucionAulas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "da")
    private Integer da;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "edificio")
    private String edificio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cap_turno")
    private String capTurno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acondicionadas")
    private int acondicionadas;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclo;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodo;

    public DistribucionAulas() {
    }

    public DistribucionAulas(Integer da) {
        this.da = da;
    }

    public DistribucionAulas(Integer da, String edificio, int numero, String capTurno, int acondicionadas) {
        this.da = da;
        this.edificio = edificio;
        this.numero = numero;
        this.capTurno = capTurno;
        this.acondicionadas = acondicionadas;
    }

    public Integer getDa() {
        return da;
    }

    public void setDa(Integer da) {
        this.da = da;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCapTurno() {
        return capTurno;
    }

    public void setCapTurno(String capTurno) {
        this.capTurno = capTurno;
    }

    public int getAcondicionadas() {
        return acondicionadas;
    }

    public void setAcondicionadas(int acondicionadas) {
        this.acondicionadas = acondicionadas;
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
        hash += (da != null ? da.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DistribucionAulas)) {
            return false;
        }
        DistribucionAulas other = (DistribucionAulas) object;
        if ((this.da == null && other.da != null) || (this.da != null && !this.da.equals(other.da))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.DistribucionAulas[ da=" + da + " ]";
    }
    
}
