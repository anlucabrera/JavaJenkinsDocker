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
import javax.persistence.FetchType;
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
@Table(name = "preguntas_encuesta_satisfaccion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreguntasEncuestaSatisfaccion.findAll", query = "SELECT p FROM PreguntasEncuestaSatisfaccion p")
    , @NamedQuery(name = "PreguntasEncuestaSatisfaccion.findByCvePregunta", query = "SELECT p FROM PreguntasEncuestaSatisfaccion p WHERE p.cvePregunta = :cvePregunta")
    , @NamedQuery(name = "PreguntasEncuestaSatisfaccion.findByTipo", query = "SELECT p FROM PreguntasEncuestaSatisfaccion p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "PreguntasEncuestaSatisfaccion.findByNoPregunta", query = "SELECT p FROM PreguntasEncuestaSatisfaccion p WHERE p.noPregunta = :noPregunta")
    , @NamedQuery(name = "PreguntasEncuestaSatisfaccion.findByPregunta", query = "SELECT p FROM PreguntasEncuestaSatisfaccion p WHERE p.pregunta = :pregunta")})
public class PreguntasEncuestaSatisfaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_pregunta")
    private Integer cvePregunta;
    @Size(max = 90)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "noPregunta")
    private Integer noPregunta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pregunta")
    private String pregunta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntasEncuestaSatisfaccion", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccion> encuestaSatisfaccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntasEncuestaSatisfaccion", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccionEmpleadoresPece> encuestaSatisfaccionEmpleadoresPeceList;
    @JoinColumn(name = "categoria", referencedColumnName = "id_categoria")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CategoriasEncuestaSatisfaccion categoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntasEncuestaSatisfaccion", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccionEgresadosPece> encuestaSatisfaccionEgresadosPeceList;

    public PreguntasEncuestaSatisfaccion() {
    }

    public PreguntasEncuestaSatisfaccion(Integer cvePregunta) {
        this.cvePregunta = cvePregunta;
    }

    public PreguntasEncuestaSatisfaccion(Integer cvePregunta, String pregunta) {
        this.cvePregunta = cvePregunta;
        this.pregunta = pregunta;
    }

    public Integer getCvePregunta() {
        return cvePregunta;
    }

    public void setCvePregunta(Integer cvePregunta) {
        this.cvePregunta = cvePregunta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getNoPregunta() {
        return noPregunta;
    }

    public void setNoPregunta(Integer noPregunta) {
        this.noPregunta = noPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    @XmlTransient
    public List<EncuestaSatisfaccion> getEncuestaSatisfaccionList() {
        return encuestaSatisfaccionList;
    }

    public void setEncuestaSatisfaccionList(List<EncuestaSatisfaccion> encuestaSatisfaccionList) {
        this.encuestaSatisfaccionList = encuestaSatisfaccionList;
    }

    @XmlTransient
    public List<EncuestaSatisfaccionEmpleadoresPece> getEncuestaSatisfaccionEmpleadoresPeceList() {
        return encuestaSatisfaccionEmpleadoresPeceList;
    }

    public void setEncuestaSatisfaccionEmpleadoresPeceList(List<EncuestaSatisfaccionEmpleadoresPece> encuestaSatisfaccionEmpleadoresPeceList) {
        this.encuestaSatisfaccionEmpleadoresPeceList = encuestaSatisfaccionEmpleadoresPeceList;
    }

    public CategoriasEncuestaSatisfaccion getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriasEncuestaSatisfaccion categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public List<EncuestaSatisfaccionEgresadosPece> getEncuestaSatisfaccionEgresadosPeceList() {
        return encuestaSatisfaccionEgresadosPeceList;
    }

    public void setEncuestaSatisfaccionEgresadosPeceList(List<EncuestaSatisfaccionEgresadosPece> encuestaSatisfaccionEgresadosPeceList) {
        this.encuestaSatisfaccionEgresadosPeceList = encuestaSatisfaccionEgresadosPeceList;
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
        if (!(object instanceof PreguntasEncuestaSatisfaccion)) {
            return false;
        }
        PreguntasEncuestaSatisfaccion other = (PreguntasEncuestaSatisfaccion) object;
        if ((this.cvePregunta == null && other.cvePregunta != null) || (this.cvePregunta != null && !this.cvePregunta.equals(other.cvePregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.PreguntasEncuestaSatisfaccion[ cvePregunta=" + cvePregunta + " ]";
    }
    
}
