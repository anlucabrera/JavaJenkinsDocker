/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "preguntas_encuesta_clima_laboral", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreguntasEncuestaClimaLaboral.findAll", query = "SELECT p FROM PreguntasEncuestaClimaLaboral p")
    , @NamedQuery(name = "PreguntasEncuestaClimaLaboral.findByCvePregunta", query = "SELECT p FROM PreguntasEncuestaClimaLaboral p WHERE p.cvePregunta = :cvePregunta")
    , @NamedQuery(name = "PreguntasEncuestaClimaLaboral.findByPregunta", query = "SELECT p FROM PreguntasEncuestaClimaLaboral p WHERE p.pregunta = :pregunta")})
public class PreguntasEncuestaClimaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cve_pregunta")
    private Integer cvePregunta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta")
    private String pregunta;
    @JoinColumn(name = "categoria", referencedColumnName = "id_categoria")
    @ManyToOne(optional = false)
    private CategoriasEncuestaClimaLaboral categoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntasEncuestaClimaLaboral")
    private List<EncuestaCapacitacionClimaLaboral> encuestaCapacitacionClimaLaboralList;

    public PreguntasEncuestaClimaLaboral() {
    }

    public PreguntasEncuestaClimaLaboral(Integer cvePregunta) {
        this.cvePregunta = cvePregunta;
    }

    public PreguntasEncuestaClimaLaboral(Integer cvePregunta, String pregunta) {
        this.cvePregunta = cvePregunta;
        this.pregunta = pregunta;
    }

    public Integer getCvePregunta() {
        return cvePregunta;
    }

    public void setCvePregunta(Integer cvePregunta) {
        this.cvePregunta = cvePregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public CategoriasEncuestaClimaLaboral getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriasEncuestaClimaLaboral categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public List<EncuestaCapacitacionClimaLaboral> getEncuestaCapacitacionClimaLaboralList() {
        return encuestaCapacitacionClimaLaboralList;
    }

    public void setEncuestaCapacitacionClimaLaboralList(List<EncuestaCapacitacionClimaLaboral> encuestaCapacitacionClimaLaboralList) {
        this.encuestaCapacitacionClimaLaboralList = encuestaCapacitacionClimaLaboralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cvePregunta != null ? cvePregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreguntasEncuestaClimaLaboral)) {
            return false;
        }
        PreguntasEncuestaClimaLaboral other = (PreguntasEncuestaClimaLaboral) object;
        if ((this.cvePregunta == null && other.cvePregunta != null) || (this.cvePregunta != null && !this.cvePregunta.equals(other.cvePregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.PreguntasEncuestaClimaLaboral[ cvePregunta=" + cvePregunta + " ]";
    }
    
}
