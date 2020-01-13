/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cursos_evidencia", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CursosEvidencia.findAll", query = "SELECT c FROM CursosEvidencia c")
    , @NamedQuery(name = "CursosEvidencia.findByEvidencia", query = "SELECT c FROM CursosEvidencia c WHERE c.evidencia = :evidencia")
    , @NamedQuery(name = "CursosEvidencia.findByRuta", query = "SELECT c FROM CursosEvidencia c WHERE c.ruta = :ruta")
    , @NamedQuery(name = "CursosEvidencia.findByMime", query = "SELECT c FROM CursosEvidencia c WHERE c.mime = :mime")})
public class CursosEvidencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evidencia")
    private Integer evidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "mime")
    private String mime;
    @ManyToMany(mappedBy = "cursosEvidenciaList")
    private List<Cursos> cursosList;

    public CursosEvidencia() {
    }

    public CursosEvidencia(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public CursosEvidencia(Integer evidencia, String ruta, String mime) {
        this.evidencia = evidencia;
        this.ruta = ruta;
        this.mime = mime;
    }

    public Integer getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    @XmlTransient
    public List<Cursos> getCursosList() {
        return cursosList;
    }

    public void setCursosList(List<Cursos> cursosList) {
        this.cursosList = cursosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evidencia != null ? evidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursosEvidencia)) {
            return false;
        }
        CursosEvidencia other = (CursosEvidencia) object;
        if ((this.evidencia == null && other.evidencia != null) || (this.evidencia != null && !this.evidencia.equals(other.evidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CursosEvidencia[ evidencia=" + evidencia + " ]";
    }
    
}
