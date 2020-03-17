/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.utxj.pye.sgi.entity.ch;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "encuesta_condiciones_estudio", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaCondicionesEstudio.findAll", query = "SELECT e FROM EncuestaCondicionesEstudio e")
    , @NamedQuery(name = "EncuestaCondicionesEstudio.findByEvaluacion", query = "SELECT e FROM EncuestaCondicionesEstudio e WHERE e.encuestaCondicionesEstudioPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EncuestaCondicionesEstudio.findByEvaluador", query = "SELECT e FROM EncuestaCondicionesEstudio e WHERE e.encuestaCondicionesEstudioPK.evaluador = :evaluador")
    , @NamedQuery(name = "EncuestaCondicionesEstudio.findByR1", query = "SELECT e FROM EncuestaCondicionesEstudio e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EncuestaCondicionesEstudio.findByR2", query = "SELECT e FROM EncuestaCondicionesEstudio e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EncuestaCondicionesEstudio.findByR3", query = "SELECT e FROM EncuestaCondicionesEstudio e WHERE e.r3 = :r3")})
public class EncuestaCondicionesEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaCondicionesEstudioPK encuestaCondicionesEstudioPK;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "r1")
    private String r1;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "r2")
    private String r2;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "r3")
    private String r3;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;

    public EncuestaCondicionesEstudio() {
    }

    public EncuestaCondicionesEstudio(EncuestaCondicionesEstudioPK encuestaCondicionesEstudioPK) {
        this.encuestaCondicionesEstudioPK = encuestaCondicionesEstudioPK;
    }

    public EncuestaCondicionesEstudio(EncuestaCondicionesEstudioPK encuestaCondicionesEstudioPK, String r1, String r2, String r3) {
        this.encuestaCondicionesEstudioPK = encuestaCondicionesEstudioPK;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public EncuestaCondicionesEstudio(int evaluacion, int evaluador) {
        this.encuestaCondicionesEstudioPK = new EncuestaCondicionesEstudioPK(evaluacion, evaluador);
    }

    public EncuestaCondicionesEstudioPK getEncuestaCondicionesEstudioPK() {
        return encuestaCondicionesEstudioPK;
    }

    public void setEncuestaCondicionesEstudioPK(EncuestaCondicionesEstudioPK encuestaCondicionesEstudioPK) {
        this.encuestaCondicionesEstudioPK = encuestaCondicionesEstudioPK;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public String getR3() {
        return r3;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }

    public Evaluaciones getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaCondicionesEstudioPK != null ? encuestaCondicionesEstudioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaCondicionesEstudio)) {
            return false;
        }
        EncuestaCondicionesEstudio other = (EncuestaCondicionesEstudio) object;
        if ((this.encuestaCondicionesEstudioPK == null && other.encuestaCondicionesEstudioPK != null) || (this.encuestaCondicionesEstudioPK != null && !this.encuestaCondicionesEstudioPK.equals(other.encuestaCondicionesEstudioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio[ encuestaCondicionesEstudioPK=" + encuestaCondicionesEstudioPK + " ]";
    }

}
