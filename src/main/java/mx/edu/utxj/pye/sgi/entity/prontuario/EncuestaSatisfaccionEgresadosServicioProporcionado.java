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
@Table(name = "encuesta_satisfaccion_egresados_servicio_proporcionado", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findAll", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByCveEncuesta", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.encuestaSatisfaccionEgresadosServicioProporcionadoPK.cveEncuesta = :cveEncuesta")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByCiclo", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.encuestaSatisfaccionEgresadosServicioProporcionadoPK.ciclo = :ciclo")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByPeriodo", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.encuestaSatisfaccionEgresadosServicioProporcionadoPK.periodo = :periodo")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByPregunta", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.encuestaSatisfaccionEgresadosServicioProporcionadoPK.pregunta = :pregunta")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespA", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respA = :respA")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespB", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respB = :respB")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespC", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respC = :respC")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespD", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respD = :respD")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespE", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respE = :respE")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespF", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respF = :respF")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosServicioProporcionado.findByRespG", query = "SELECT e FROM EncuestaSatisfaccionEgresadosServicioProporcionado e WHERE e.respG = :respG")})
public class EncuestaSatisfaccionEgresadosServicioProporcionado implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaSatisfaccionEgresadosServicioProporcionadoPK encuestaSatisfaccionEgresadosServicioProporcionadoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_a")
    private int respA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_b")
    private int respB;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_c")
    private int respC;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_d")
    private int respD;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_e")
    private int respE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_f")
    private int respF;
    @Basic(optional = false)
    @NotNull
    @Column(name = "resp_g")
    private int respG;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PeriodosEscolares periodosEscolares;
    @JoinColumn(name = "pregunta", referencedColumnName = "cve_pregunta", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PreguntasSatisfaccionEgresadosServicio preguntasSatisfaccionEgresadosServicio;

    public EncuestaSatisfaccionEgresadosServicioProporcionado() {
    }

    public EncuestaSatisfaccionEgresadosServicioProporcionado(EncuestaSatisfaccionEgresadosServicioProporcionadoPK encuestaSatisfaccionEgresadosServicioProporcionadoPK) {
        this.encuestaSatisfaccionEgresadosServicioProporcionadoPK = encuestaSatisfaccionEgresadosServicioProporcionadoPK;
    }

    public EncuestaSatisfaccionEgresadosServicioProporcionado(EncuestaSatisfaccionEgresadosServicioProporcionadoPK encuestaSatisfaccionEgresadosServicioProporcionadoPK, int respA, int respB, int respC, int respD, int respE, int respF, int respG) {
        this.encuestaSatisfaccionEgresadosServicioProporcionadoPK = encuestaSatisfaccionEgresadosServicioProporcionadoPK;
        this.respA = respA;
        this.respB = respB;
        this.respC = respC;
        this.respD = respD;
        this.respE = respE;
        this.respF = respF;
        this.respG = respG;
    }

    public EncuestaSatisfaccionEgresadosServicioProporcionado(int cveEncuesta, int ciclo, int periodo, int pregunta) {
        this.encuestaSatisfaccionEgresadosServicioProporcionadoPK = new EncuestaSatisfaccionEgresadosServicioProporcionadoPK(cveEncuesta, ciclo, periodo, pregunta);
    }

    public EncuestaSatisfaccionEgresadosServicioProporcionadoPK getEncuestaSatisfaccionEgresadosServicioProporcionadoPK() {
        return encuestaSatisfaccionEgresadosServicioProporcionadoPK;
    }

    public void setEncuestaSatisfaccionEgresadosServicioProporcionadoPK(EncuestaSatisfaccionEgresadosServicioProporcionadoPK encuestaSatisfaccionEgresadosServicioProporcionadoPK) {
        this.encuestaSatisfaccionEgresadosServicioProporcionadoPK = encuestaSatisfaccionEgresadosServicioProporcionadoPK;
    }

    public int getRespA() {
        return respA;
    }

    public void setRespA(int respA) {
        this.respA = respA;
    }

    public int getRespB() {
        return respB;
    }

    public void setRespB(int respB) {
        this.respB = respB;
    }

    public int getRespC() {
        return respC;
    }

    public void setRespC(int respC) {
        this.respC = respC;
    }

    public int getRespD() {
        return respD;
    }

    public void setRespD(int respD) {
        this.respD = respD;
    }

    public int getRespE() {
        return respE;
    }

    public void setRespE(int respE) {
        this.respE = respE;
    }

    public int getRespF() {
        return respF;
    }

    public void setRespF(int respF) {
        this.respF = respF;
    }

    public int getRespG() {
        return respG;
    }

    public void setRespG(int respG) {
        this.respG = respG;
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

    public PreguntasSatisfaccionEgresadosServicio getPreguntasSatisfaccionEgresadosServicio() {
        return preguntasSatisfaccionEgresadosServicio;
    }

    public void setPreguntasSatisfaccionEgresadosServicio(PreguntasSatisfaccionEgresadosServicio preguntasSatisfaccionEgresadosServicio) {
        this.preguntasSatisfaccionEgresadosServicio = preguntasSatisfaccionEgresadosServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaSatisfaccionEgresadosServicioProporcionadoPK != null ? encuestaSatisfaccionEgresadosServicioProporcionadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccionEgresadosServicioProporcionado)) {
            return false;
        }
        EncuestaSatisfaccionEgresadosServicioProporcionado other = (EncuestaSatisfaccionEgresadosServicioProporcionado) object;
        if ((this.encuestaSatisfaccionEgresadosServicioProporcionadoPK == null && other.encuestaSatisfaccionEgresadosServicioProporcionadoPK != null) || (this.encuestaSatisfaccionEgresadosServicioProporcionadoPK != null && !this.encuestaSatisfaccionEgresadosServicioProporcionadoPK.equals(other.encuestaSatisfaccionEgresadosServicioProporcionadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaSatisfaccionEgresadosServicioProporcionado[ encuestaSatisfaccionEgresadosServicioProporcionadoPK=" + encuestaSatisfaccionEgresadosServicioProporcionadoPK + " ]";
    }
    
}
