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
@Table(name = "encuesta_capacitacion_clima_laboral", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findAll", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByCveEncuesta", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.encuestaCapacitacionClimaLaboralPK.cveEncuesta = :cveEncuesta")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByAnio", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.encuestaCapacitacionClimaLaboralPK.anio = :anio")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByPregunta", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.encuestaCapacitacionClimaLaboralPK.pregunta = :pregunta")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByRespA", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.respA = :respA")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByRespB", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.respB = :respB")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByRespC", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.respC = :respC")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByRespD", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.respD = :respD")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByRespE", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.respE = :respE")
    , @NamedQuery(name = "EncuestaCapacitacionClimaLaboral.findByRespF", query = "SELECT e FROM EncuestaCapacitacionClimaLaboral e WHERE e.respF = :respF")})
public class EncuestaCapacitacionClimaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaCapacitacionClimaLaboralPK encuestaCapacitacionClimaLaboralPK;
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
    @JoinColumn(name = "pregunta", referencedColumnName = "cve_pregunta", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PreguntasEncuestaClimaLaboral preguntasEncuestaClimaLaboral;

    public EncuestaCapacitacionClimaLaboral() {
    }

    public EncuestaCapacitacionClimaLaboral(EncuestaCapacitacionClimaLaboralPK encuestaCapacitacionClimaLaboralPK) {
        this.encuestaCapacitacionClimaLaboralPK = encuestaCapacitacionClimaLaboralPK;
    }

    public EncuestaCapacitacionClimaLaboral(EncuestaCapacitacionClimaLaboralPK encuestaCapacitacionClimaLaboralPK, int respA, int respB, int respC, int respD, int respE, int respF) {
        this.encuestaCapacitacionClimaLaboralPK = encuestaCapacitacionClimaLaboralPK;
        this.respA = respA;
        this.respB = respB;
        this.respC = respC;
        this.respD = respD;
        this.respE = respE;
        this.respF = respF;
    }

    public EncuestaCapacitacionClimaLaboral(int cveEncuesta, int anio, int pregunta) {
        this.encuestaCapacitacionClimaLaboralPK = new EncuestaCapacitacionClimaLaboralPK(cveEncuesta, anio, pregunta);
    }

    public EncuestaCapacitacionClimaLaboralPK getEncuestaCapacitacionClimaLaboralPK() {
        return encuestaCapacitacionClimaLaboralPK;
    }

    public void setEncuestaCapacitacionClimaLaboralPK(EncuestaCapacitacionClimaLaboralPK encuestaCapacitacionClimaLaboralPK) {
        this.encuestaCapacitacionClimaLaboralPK = encuestaCapacitacionClimaLaboralPK;
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

    public PreguntasEncuestaClimaLaboral getPreguntasEncuestaClimaLaboral() {
        return preguntasEncuestaClimaLaboral;
    }

    public void setPreguntasEncuestaClimaLaboral(PreguntasEncuestaClimaLaboral preguntasEncuestaClimaLaboral) {
        this.preguntasEncuestaClimaLaboral = preguntasEncuestaClimaLaboral;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaCapacitacionClimaLaboralPK != null ? encuestaCapacitacionClimaLaboralPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaCapacitacionClimaLaboral)) {
            return false;
        }
        EncuestaCapacitacionClimaLaboral other = (EncuestaCapacitacionClimaLaboral) object;
        if ((this.encuestaCapacitacionClimaLaboralPK == null && other.encuestaCapacitacionClimaLaboralPK != null) || (this.encuestaCapacitacionClimaLaboralPK != null && !this.encuestaCapacitacionClimaLaboralPK.equals(other.encuestaCapacitacionClimaLaboralPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaCapacitacionClimaLaboral[ encuestaCapacitacionClimaLaboralPK=" + encuestaCapacitacionClimaLaboralPK + " ]";
    }
    
}
