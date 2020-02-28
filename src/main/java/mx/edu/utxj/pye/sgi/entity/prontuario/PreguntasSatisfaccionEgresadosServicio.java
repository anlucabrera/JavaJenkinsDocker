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
@Table(name = "preguntas_satisfaccion_egresados_servicio", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreguntasSatisfaccionEgresadosServicio.findAll", query = "SELECT p FROM PreguntasSatisfaccionEgresadosServicio p")
    , @NamedQuery(name = "PreguntasSatisfaccionEgresadosServicio.findByCvePregunta", query = "SELECT p FROM PreguntasSatisfaccionEgresadosServicio p WHERE p.cvePregunta = :cvePregunta")
    , @NamedQuery(name = "PreguntasSatisfaccionEgresadosServicio.findByPregunta", query = "SELECT p FROM PreguntasSatisfaccionEgresadosServicio p WHERE p.pregunta = :pregunta")})
public class PreguntasSatisfaccionEgresadosServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_pregunta")
    private Integer cvePregunta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "pregunta")
    private String pregunta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntasSatisfaccionEgresadosServicio")
    private List<EncuestaSatisfaccionEgresadosServicioProporcionado> encuestaSatisfaccionEgresadosServicioProporcionadoList;

    public PreguntasSatisfaccionEgresadosServicio() {
    }

    public PreguntasSatisfaccionEgresadosServicio(Integer cvePregunta) {
        this.cvePregunta = cvePregunta;
    }

    public PreguntasSatisfaccionEgresadosServicio(Integer cvePregunta, String pregunta) {
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

    @XmlTransient
    public List<EncuestaSatisfaccionEgresadosServicioProporcionado> getEncuestaSatisfaccionEgresadosServicioProporcionadoList() {
        return encuestaSatisfaccionEgresadosServicioProporcionadoList;
    }

    public void setEncuestaSatisfaccionEgresadosServicioProporcionadoList(List<EncuestaSatisfaccionEgresadosServicioProporcionado> encuestaSatisfaccionEgresadosServicioProporcionadoList) {
        this.encuestaSatisfaccionEgresadosServicioProporcionadoList = encuestaSatisfaccionEgresadosServicioProporcionadoList;
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
        if (!(object instanceof PreguntasSatisfaccionEgresadosServicio)) {
            return false;
        }
        PreguntasSatisfaccionEgresadosServicio other = (PreguntasSatisfaccionEgresadosServicio) object;
        if ((this.cvePregunta == null && other.cvePregunta != null) || (this.cvePregunta != null && !this.cvePregunta.equals(other.cvePregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.PreguntasSatisfaccionEgresadosServicio[ cvePregunta=" + cvePregunta + " ]";
    }
    
}
