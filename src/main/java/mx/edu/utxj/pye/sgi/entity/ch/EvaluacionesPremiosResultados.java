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
@Table(name = "evaluaciones_premios_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionesPremiosResultados.findAll", query = "SELECT e FROM EvaluacionesPremiosResultados e")
    , @NamedQuery(name = "EvaluacionesPremiosResultados.findByEvaluacion", query = "SELECT e FROM EvaluacionesPremiosResultados e WHERE e.evaluacionesPremiosResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionesPremiosResultados.findByEvaluador", query = "SELECT e FROM EvaluacionesPremiosResultados e WHERE e.evaluacionesPremiosResultadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionesPremiosResultados.findByEvaluado", query = "SELECT e FROM EvaluacionesPremiosResultados e WHERE e.evaluacionesPremiosResultadosPK.evaluado = :evaluado")
    , @NamedQuery(name = "EvaluacionesPremiosResultados.findByTipo", query = "SELECT e FROM EvaluacionesPremiosResultados e WHERE e.tipo = :tipo")})
public class EvaluacionesPremiosResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionesPremiosResultadosPK evaluacionesPremiosResultadosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "evaluado", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;
    @JoinColumn(name = "evaluador", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal1;

    public EvaluacionesPremiosResultados() {
    }

    public EvaluacionesPremiosResultados(EvaluacionesPremiosResultadosPK evaluacionesPremiosResultadosPK) {
        this.evaluacionesPremiosResultadosPK = evaluacionesPremiosResultadosPK;
    }

    public EvaluacionesPremiosResultados(EvaluacionesPremiosResultadosPK evaluacionesPremiosResultadosPK, String tipo) {
        this.evaluacionesPremiosResultadosPK = evaluacionesPremiosResultadosPK;
        this.tipo = tipo;
    }

    public EvaluacionesPremiosResultados(int evaluacion, int evaluador, int evaluado) {
        this.evaluacionesPremiosResultadosPK = new EvaluacionesPremiosResultadosPK(evaluacion, evaluador, evaluado);
    }

    public EvaluacionesPremiosResultadosPK getEvaluacionesPremiosResultadosPK() {
        return evaluacionesPremiosResultadosPK;
    }

    public void setEvaluacionesPremiosResultadosPK(EvaluacionesPremiosResultadosPK evaluacionesPremiosResultadosPK) {
        this.evaluacionesPremiosResultadosPK = evaluacionesPremiosResultadosPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Evaluaciones getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public Personal getPersonal1() {
        return personal1;
    }

    public void setPersonal1(Personal personal1) {
        this.personal1 = personal1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacionesPremiosResultadosPK != null ? evaluacionesPremiosResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionesPremiosResultados)) {
            return false;
        }
        EvaluacionesPremiosResultados other = (EvaluacionesPremiosResultados) object;
        if ((this.evaluacionesPremiosResultadosPK == null && other.evaluacionesPremiosResultadosPK != null) || (this.evaluacionesPremiosResultadosPK != null && !this.evaluacionesPremiosResultadosPK.equals(other.evaluacionesPremiosResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesPremiosResultados[ evaluacionesPremiosResultadosPK=" + evaluacionesPremiosResultadosPK + " ]";
    }
    
}
