/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
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
@Table(name = "exani_resultados_ciclos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExaniResultadosCiclosEscolares.findAll", query = "SELECT e FROM ExaniResultadosCiclosEscolares e")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByRegistro", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.registro = :registro")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByCicloEscolar", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByProgramaEducativo", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findBySustentantes", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.sustentantes = :sustentantes")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByICNEalto", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.iCNEalto = :iCNEalto")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByICNEmedio", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.iCNEmedio = :iCNEmedio")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByICNEbajo", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.iCNEbajo = :iCNEbajo")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findBySusInscritos", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.susInscritos = :susInscritos")
    , @NamedQuery(name = "ExaniResultadosCiclosEscolares.findByNosusInscritos", query = "SELECT e FROM ExaniResultadosCiclosEscolares e WHERE e.nosusInscritos = :nosusInscritos")})
public class ExaniResultadosCiclosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sustentantes")
    private int sustentantes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ICNE_alto")
    private int iCNEalto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ICNE_medio")
    private int iCNEmedio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ICNE_bajo")
    private int iCNEbajo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sus_inscritos")
    private int susInscritos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nosus_inscritos")
    private int nosusInscritos;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ExaniResultadosCiclosEscolares() {
    }

    public ExaniResultadosCiclosEscolares(Integer registro) {
        this.registro = registro;
    }

    public ExaniResultadosCiclosEscolares(Integer registro, int cicloEscolar, short programaEducativo, int sustentantes, int iCNEalto, int iCNEmedio, int iCNEbajo, int susInscritos, int nosusInscritos) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.programaEducativo = programaEducativo;
        this.sustentantes = sustentantes;
        this.iCNEalto = iCNEalto;
        this.iCNEmedio = iCNEmedio;
        this.iCNEbajo = iCNEbajo;
        this.susInscritos = susInscritos;
        this.nosusInscritos = nosusInscritos;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getSustentantes() {
        return sustentantes;
    }

    public void setSustentantes(int sustentantes) {
        this.sustentantes = sustentantes;
    }

    public int getICNEalto() {
        return iCNEalto;
    }

    public void setICNEalto(int iCNEalto) {
        this.iCNEalto = iCNEalto;
    }

    public int getICNEmedio() {
        return iCNEmedio;
    }

    public void setICNEmedio(int iCNEmedio) {
        this.iCNEmedio = iCNEmedio;
    }

    public int getICNEbajo() {
        return iCNEbajo;
    }

    public void setICNEbajo(int iCNEbajo) {
        this.iCNEbajo = iCNEbajo;
    }

    public int getSusInscritos() {
        return susInscritos;
    }

    public void setSusInscritos(int susInscritos) {
        this.susInscritos = susInscritos;
    }

    public int getNosusInscritos() {
        return nosusInscritos;
    }

    public void setNosusInscritos(int nosusInscritos) {
        this.nosusInscritos = nosusInscritos;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExaniResultadosCiclosEscolares)) {
            return false;
        }
        ExaniResultadosCiclosEscolares other = (ExaniResultadosCiclosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ExaniResultadosCiclosEscolares[ registro=" + registro + " ]";
    }
    
}
