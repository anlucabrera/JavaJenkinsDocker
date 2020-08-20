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
@Table(name = "encuesta_satisfaccion_egresados_pece", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findAll", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByCveEncuesta", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.encuestaSatisfaccionEgresadosPecePK.cveEncuesta = :cveEncuesta")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByCiclo", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.encuestaSatisfaccionEgresadosPecePK.ciclo = :ciclo")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByPregunta", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.encuestaSatisfaccionEgresadosPecePK.pregunta = :pregunta")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findBySiglas", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.encuestaSatisfaccionEgresadosPecePK.siglas = :siglas")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespA", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respA = :respA")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespB", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respB = :respB")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespC", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respC = :respC")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespD", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respD = :respD")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespE", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respE = :respE")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespF", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respF = :respF")
    , @NamedQuery(name = "EncuestaSatisfaccionEgresadosPece.findByRespG", query = "SELECT e FROM EncuestaSatisfaccionEgresadosPece e WHERE e.respG = :respG")})
public class EncuestaSatisfaccionEgresadosPece implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaSatisfaccionEgresadosPecePK encuestaSatisfaccionEgresadosPecePK;
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
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PreguntasEncuestaSatisfaccion preguntasEncuestaSatisfaccion;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos programasEducativos;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclosEscolares;

    public EncuestaSatisfaccionEgresadosPece() {
    }

    public EncuestaSatisfaccionEgresadosPece(EncuestaSatisfaccionEgresadosPecePK encuestaSatisfaccionEgresadosPecePK) {
        this.encuestaSatisfaccionEgresadosPecePK = encuestaSatisfaccionEgresadosPecePK;
    }

    public EncuestaSatisfaccionEgresadosPece(EncuestaSatisfaccionEgresadosPecePK encuestaSatisfaccionEgresadosPecePK, int respA, int respB, int respC, int respD, int respE, int respF, int respG) {
        this.encuestaSatisfaccionEgresadosPecePK = encuestaSatisfaccionEgresadosPecePK;
        this.respA = respA;
        this.respB = respB;
        this.respC = respC;
        this.respD = respD;
        this.respE = respE;
        this.respF = respF;
        this.respG = respG;
    }

    public EncuestaSatisfaccionEgresadosPece(int cveEncuesta, int ciclo, int pregunta, String siglas) {
        this.encuestaSatisfaccionEgresadosPecePK = new EncuestaSatisfaccionEgresadosPecePK(cveEncuesta, ciclo, pregunta, siglas);
    }

    public EncuestaSatisfaccionEgresadosPecePK getEncuestaSatisfaccionEgresadosPecePK() {
        return encuestaSatisfaccionEgresadosPecePK;
    }

    public void setEncuestaSatisfaccionEgresadosPecePK(EncuestaSatisfaccionEgresadosPecePK encuestaSatisfaccionEgresadosPecePK) {
        this.encuestaSatisfaccionEgresadosPecePK = encuestaSatisfaccionEgresadosPecePK;
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
        hash += (encuestaSatisfaccionEgresadosPecePK != null ? encuestaSatisfaccionEgresadosPecePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccionEgresadosPece)) {
            return false;
        }
        EncuestaSatisfaccionEgresadosPece other = (EncuestaSatisfaccionEgresadosPece) object;
        if ((this.encuestaSatisfaccionEgresadosPecePK == null && other.encuestaSatisfaccionEgresadosPecePK != null) || (this.encuestaSatisfaccionEgresadosPecePK != null && !this.encuestaSatisfaccionEgresadosPecePK.equals(other.encuestaSatisfaccionEgresadosPecePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaSatisfaccionEgresadosPece[ encuestaSatisfaccionEgresadosPecePK=" + encuestaSatisfaccionEgresadosPecePK + " ]";
    }
    
}
