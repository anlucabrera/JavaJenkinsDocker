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
@Table(name = "encuesta_satisfaccion_empleadores_pece", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findAll", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByCveEncuesta", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.encuestaSatisfaccionEmpleadoresPecePK.cveEncuesta = :cveEncuesta")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByCiclo", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.encuestaSatisfaccionEmpleadoresPecePK.ciclo = :ciclo")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByPregunta", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.encuestaSatisfaccionEmpleadoresPecePK.pregunta = :pregunta")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findBySiglas", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.encuestaSatisfaccionEmpleadoresPecePK.siglas = :siglas")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespA", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respA = :respA")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespB", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respB = :respB")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespC", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respC = :respC")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespD", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respD = :respD")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespE", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respE = :respE")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespF", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respF = :respF")
    , @NamedQuery(name = "EncuestaSatisfaccionEmpleadoresPece.findByRespG", query = "SELECT e FROM EncuestaSatisfaccionEmpleadoresPece e WHERE e.respG = :respG")})
public class EncuestaSatisfaccionEmpleadoresPece implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaSatisfaccionEmpleadoresPecePK encuestaSatisfaccionEmpleadoresPecePK;
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

    public EncuestaSatisfaccionEmpleadoresPece() {
    }

    public EncuestaSatisfaccionEmpleadoresPece(EncuestaSatisfaccionEmpleadoresPecePK encuestaSatisfaccionEmpleadoresPecePK) {
        this.encuestaSatisfaccionEmpleadoresPecePK = encuestaSatisfaccionEmpleadoresPecePK;
    }

    public EncuestaSatisfaccionEmpleadoresPece(EncuestaSatisfaccionEmpleadoresPecePK encuestaSatisfaccionEmpleadoresPecePK, int respA, int respB, int respC, int respD, int respE, int respF, int respG) {
        this.encuestaSatisfaccionEmpleadoresPecePK = encuestaSatisfaccionEmpleadoresPecePK;
        this.respA = respA;
        this.respB = respB;
        this.respC = respC;
        this.respD = respD;
        this.respE = respE;
        this.respF = respF;
        this.respG = respG;
    }

    public EncuestaSatisfaccionEmpleadoresPece(int cveEncuesta, int ciclo, int pregunta, String siglas) {
        this.encuestaSatisfaccionEmpleadoresPecePK = new EncuestaSatisfaccionEmpleadoresPecePK(cveEncuesta, ciclo, pregunta, siglas);
    }

    public EncuestaSatisfaccionEmpleadoresPecePK getEncuestaSatisfaccionEmpleadoresPecePK() {
        return encuestaSatisfaccionEmpleadoresPecePK;
    }

    public void setEncuestaSatisfaccionEmpleadoresPecePK(EncuestaSatisfaccionEmpleadoresPecePK encuestaSatisfaccionEmpleadoresPecePK) {
        this.encuestaSatisfaccionEmpleadoresPecePK = encuestaSatisfaccionEmpleadoresPecePK;
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
        hash += (encuestaSatisfaccionEmpleadoresPecePK != null ? encuestaSatisfaccionEmpleadoresPecePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccionEmpleadoresPece)) {
            return false;
        }
        EncuestaSatisfaccionEmpleadoresPece other = (EncuestaSatisfaccionEmpleadoresPece) object;
        if ((this.encuestaSatisfaccionEmpleadoresPecePK == null && other.encuestaSatisfaccionEmpleadoresPecePK != null) || (this.encuestaSatisfaccionEmpleadoresPecePK != null && !this.encuestaSatisfaccionEmpleadoresPecePK.equals(other.encuestaSatisfaccionEmpleadoresPecePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaSatisfaccionEmpleadoresPece[ encuestaSatisfaccionEmpleadoresPecePK=" + encuestaSatisfaccionEmpleadoresPecePK + " ]";
    }
    
}
