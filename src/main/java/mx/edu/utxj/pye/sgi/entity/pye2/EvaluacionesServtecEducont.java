/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluaciones_servtec_educont", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionesServtecEducont.findAll", query = "SELECT e FROM EvaluacionesServtecEducont e")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByEvaluacion", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByDescripcion", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByActiva", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.activa = :activa")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta1", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta1 = :pregunta1")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta2", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta2 = :pregunta2")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta3", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta3 = :pregunta3")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta4", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta4 = :pregunta4")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta5", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta5 = :pregunta5")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta6", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta6 = :pregunta6")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta7", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta7 = :pregunta7")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta8", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta8 = :pregunta8")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta9", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta9 = :pregunta9")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta10", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta10 = :pregunta10")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta11", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta11 = :pregunta11")
    , @NamedQuery(name = "EvaluacionesServtecEducont.findByPregunta12", query = "SELECT e FROM EvaluacionesServtecEducont e WHERE e.pregunta12 = :pregunta12")})
public class EvaluacionesServtecEducont implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evaluacion")
    private Integer evaluacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activa")
    private boolean activa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta1")
    private String pregunta1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta2")
    private String pregunta2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta3")
    private String pregunta3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta4")
    private String pregunta4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta5")
    private String pregunta5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta6")
    private String pregunta6;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta7")
    private String pregunta7;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta8")
    private String pregunta8;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta9")
    private String pregunta9;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta10")
    private String pregunta10;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta11")
    private String pregunta11;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta12")
    private String pregunta12;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacion", fetch = FetchType.LAZY)
    private List<EvaluacionSatisfaccionResultados> evaluacionSatisfaccionResultadosList;

    public EvaluacionesServtecEducont() {
    }

    public EvaluacionesServtecEducont(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public EvaluacionesServtecEducont(Integer evaluacion, String descripcion, boolean activa, String pregunta1, String pregunta2, String pregunta3, String pregunta4, String pregunta5, String pregunta6, String pregunta7, String pregunta8, String pregunta9, String pregunta10, String pregunta11, String pregunta12) {
        this.evaluacion = evaluacion;
        this.descripcion = descripcion;
        this.activa = activa;
        this.pregunta1 = pregunta1;
        this.pregunta2 = pregunta2;
        this.pregunta3 = pregunta3;
        this.pregunta4 = pregunta4;
        this.pregunta5 = pregunta5;
        this.pregunta6 = pregunta6;
        this.pregunta7 = pregunta7;
        this.pregunta8 = pregunta8;
        this.pregunta9 = pregunta9;
        this.pregunta10 = pregunta10;
        this.pregunta11 = pregunta11;
        this.pregunta12 = pregunta12;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public String getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(String pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public String getPregunta4() {
        return pregunta4;
    }

    public void setPregunta4(String pregunta4) {
        this.pregunta4 = pregunta4;
    }

    public String getPregunta5() {
        return pregunta5;
    }

    public void setPregunta5(String pregunta5) {
        this.pregunta5 = pregunta5;
    }

    public String getPregunta6() {
        return pregunta6;
    }

    public void setPregunta6(String pregunta6) {
        this.pregunta6 = pregunta6;
    }

    public String getPregunta7() {
        return pregunta7;
    }

    public void setPregunta7(String pregunta7) {
        this.pregunta7 = pregunta7;
    }

    public String getPregunta8() {
        return pregunta8;
    }

    public void setPregunta8(String pregunta8) {
        this.pregunta8 = pregunta8;
    }

    public String getPregunta9() {
        return pregunta9;
    }

    public void setPregunta9(String pregunta9) {
        this.pregunta9 = pregunta9;
    }

    public String getPregunta10() {
        return pregunta10;
    }

    public void setPregunta10(String pregunta10) {
        this.pregunta10 = pregunta10;
    }

    public String getPregunta11() {
        return pregunta11;
    }

    public void setPregunta11(String pregunta11) {
        this.pregunta11 = pregunta11;
    }

    public String getPregunta12() {
        return pregunta12;
    }

    public void setPregunta12(String pregunta12) {
        this.pregunta12 = pregunta12;
    }

    @XmlTransient
    public List<EvaluacionSatisfaccionResultados> getEvaluacionSatisfaccionResultadosList() {
        return evaluacionSatisfaccionResultadosList;
    }

    public void setEvaluacionSatisfaccionResultadosList(List<EvaluacionSatisfaccionResultados> evaluacionSatisfaccionResultadosList) {
        this.evaluacionSatisfaccionResultadosList = evaluacionSatisfaccionResultadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacion != null ? evaluacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionesServtecEducont)) {
            return false;
        }
        EvaluacionesServtecEducont other = (EvaluacionesServtecEducont) object;
        if ((this.evaluacion == null && other.evaluacion != null) || (this.evaluacion != null && !this.evaluacion.equals(other.evaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionesServtecEducont[ evaluacion=" + evaluacion + " ]";
    }
    
}
