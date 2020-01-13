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
@Table(name = "encuesta_satisfaccion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaSatisfaccion.findAll", query = "SELECT e FROM EncuestaSatisfaccion e")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByCveEncuesta", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.encuestaSatisfaccionPK.cveEncuesta = :cveEncuesta")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByCiclo", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.encuestaSatisfaccionPK.ciclo = :ciclo")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByPregunta", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.encuestaSatisfaccionPK.pregunta = :pregunta")
    , @NamedQuery(name = "EncuestaSatisfaccion.findBySiglas", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.encuestaSatisfaccionPK.siglas = :siglas")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespA", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respA = :respA")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespB", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respB = :respB")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespC", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respC = :respC")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespD", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respD = :respD")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespE", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respE = :respE")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespF", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respF = :respF")
    , @NamedQuery(name = "EncuestaSatisfaccion.findByRespG", query = "SELECT e FROM EncuestaSatisfaccion e WHERE e.respG = :respG")})
public class EncuestaSatisfaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaSatisfaccionPK encuestaSatisfaccionPK;
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
    @JoinColumn(name = "pregunta", referencedColumnName = "cve_pregunta", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PreguntasEncuestaSatisfaccion preguntasEncuestaSatisfaccion;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ProgramasEducativos programasEducativos;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;

    public EncuestaSatisfaccion() {
    }

    public EncuestaSatisfaccion(EncuestaSatisfaccionPK encuestaSatisfaccionPK) {
        this.encuestaSatisfaccionPK = encuestaSatisfaccionPK;
    }

    public EncuestaSatisfaccion(EncuestaSatisfaccionPK encuestaSatisfaccionPK, int respA, int respB, int respC, int respD, int respE, int respF, int respG) {
        this.encuestaSatisfaccionPK = encuestaSatisfaccionPK;
        this.respA = respA;
        this.respB = respB;
        this.respC = respC;
        this.respD = respD;
        this.respE = respE;
        this.respF = respF;
        this.respG = respG;
    }

    public EncuestaSatisfaccion(int cveEncuesta, int ciclo, int pregunta, String siglas) {
        this.encuestaSatisfaccionPK = new EncuestaSatisfaccionPK(cveEncuesta, ciclo, pregunta, siglas);
    }

    public EncuestaSatisfaccionPK getEncuestaSatisfaccionPK() {
        return encuestaSatisfaccionPK;
    }

    public void setEncuestaSatisfaccionPK(EncuestaSatisfaccionPK encuestaSatisfaccionPK) {
        this.encuestaSatisfaccionPK = encuestaSatisfaccionPK;
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

    public PreguntasEncuestaSatisfaccion getPreguntasEncuestaSatisfaccion() {
        return preguntasEncuestaSatisfaccion;
    }

    public void setPreguntasEncuestaSatisfaccion(PreguntasEncuestaSatisfaccion preguntasEncuestaSatisfaccion) {
        this.preguntasEncuestaSatisfaccion = preguntasEncuestaSatisfaccion;
    }

    public ProgramasEducativos getProgramasEducativos() {
        return programasEducativos;
    }

    public void setProgramasEducativos(ProgramasEducativos programasEducativos) {
        this.programasEducativos = programasEducativos;
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
        hash += (encuestaSatisfaccionPK != null ? encuestaSatisfaccionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccion)) {
            return false;
        }
        EncuestaSatisfaccion other = (EncuestaSatisfaccion) object;
        if ((this.encuestaSatisfaccionPK == null && other.encuestaSatisfaccionPK != null) || (this.encuestaSatisfaccionPK != null && !this.encuestaSatisfaccionPK.equals(other.encuestaSatisfaccionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaSatisfaccion[ encuestaSatisfaccionPK=" + encuestaSatisfaccionPK + " ]";
    }
    
}
