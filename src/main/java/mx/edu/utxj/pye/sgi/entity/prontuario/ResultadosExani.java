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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "resultados_exani", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResultadosExani.findAll", query = "SELECT r FROM ResultadosExani r")
    , @NamedQuery(name = "ResultadosExani.findByCicloEscolar", query = "SELECT r FROM ResultadosExani r WHERE r.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "ResultadosExani.findBySustentantes", query = "SELECT r FROM ResultadosExani r WHERE r.sustentantes = :sustentantes")
    , @NamedQuery(name = "ResultadosExani.findByICNEalto", query = "SELECT r FROM ResultadosExani r WHERE r.iCNEalto = :iCNEalto")
    , @NamedQuery(name = "ResultadosExani.findByICNEmedio", query = "SELECT r FROM ResultadosExani r WHERE r.iCNEmedio = :iCNEmedio")
    , @NamedQuery(name = "ResultadosExani.findByICNEbajo", query = "SELECT r FROM ResultadosExani r WHERE r.iCNEbajo = :iCNEbajo")
    , @NamedQuery(name = "ResultadosExani.findBySusInscritos", query = "SELECT r FROM ResultadosExani r WHERE r.susInscritos = :susInscritos")})
public class ResultadosExani implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private Integer cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sustentantes")
    private short sustentantes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ICNE_alto")
    private short iCNEalto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ICNE_medio")
    private short iCNEmedio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ICNE_bajo")
    private short iCNEbajo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sus_inscritos")
    private short susInscritos;
    @JoinColumn(name = "ciclo_escolar", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;

    public ResultadosExani() {
    }

    public ResultadosExani(Integer cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public ResultadosExani(Integer cicloEscolar, short sustentantes, short iCNEalto, short iCNEmedio, short iCNEbajo, short susInscritos) {
        this.cicloEscolar = cicloEscolar;
        this.sustentantes = sustentantes;
        this.iCNEalto = iCNEalto;
        this.iCNEmedio = iCNEmedio;
        this.iCNEbajo = iCNEbajo;
        this.susInscritos = susInscritos;
    }

    public Integer getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(Integer cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public short getSustentantes() {
        return sustentantes;
    }

    public void setSustentantes(short sustentantes) {
        this.sustentantes = sustentantes;
    }

    public short getICNEalto() {
        return iCNEalto;
    }

    public void setICNEalto(short iCNEalto) {
        this.iCNEalto = iCNEalto;
    }

    public short getICNEmedio() {
        return iCNEmedio;
    }

    public void setICNEmedio(short iCNEmedio) {
        this.iCNEmedio = iCNEmedio;
    }

    public short getICNEbajo() {
        return iCNEbajo;
    }

    public void setICNEbajo(short iCNEbajo) {
        this.iCNEbajo = iCNEbajo;
    }

    public short getSusInscritos() {
        return susInscritos;
    }

    public void setSusInscritos(short susInscritos) {
        this.susInscritos = susInscritos;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
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
        hash += (cicloEscolar != null ? cicloEscolar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResultadosExani)) {
            return false;
        }
        ResultadosExani other = (ResultadosExani) object;
        if ((this.cicloEscolar == null && other.cicloEscolar != null) || (this.cicloEscolar != null && !this.cicloEscolar.equals(other.cicloEscolar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ResultadosExani[ cicloEscolar=" + cicloEscolar + " ]";
    }
    
}
