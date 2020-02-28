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
@Table(name = "categorias_encuesta_clima_laboral", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoriasEncuestaClimaLaboral.findAll", query = "SELECT c FROM CategoriasEncuestaClimaLaboral c")
    , @NamedQuery(name = "CategoriasEncuestaClimaLaboral.findByIdCategoria", query = "SELECT c FROM CategoriasEncuestaClimaLaboral c WHERE c.idCategoria = :idCategoria")
    , @NamedQuery(name = "CategoriasEncuestaClimaLaboral.findByNombre", query = "SELECT c FROM CategoriasEncuestaClimaLaboral c WHERE c.nombre = :nombre")})
public class CategoriasEncuestaClimaLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_categoria")
    private Integer idCategoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private List<PreguntasEncuestaClimaLaboral> preguntasEncuestaClimaLaboralList;

    public CategoriasEncuestaClimaLaboral() {
    }

    public CategoriasEncuestaClimaLaboral(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public CategoriasEncuestaClimaLaboral(Integer idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<PreguntasEncuestaClimaLaboral> getPreguntasEncuestaClimaLaboralList() {
        return preguntasEncuestaClimaLaboralList;
    }

    public void setPreguntasEncuestaClimaLaboralList(List<PreguntasEncuestaClimaLaboral> preguntasEncuestaClimaLaboralList) {
        this.preguntasEncuestaClimaLaboralList = preguntasEncuestaClimaLaboralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategoria != null ? idCategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriasEncuestaClimaLaboral)) {
            return false;
        }
        CategoriasEncuestaClimaLaboral other = (CategoriasEncuestaClimaLaboral) object;
        if ((this.idCategoria == null && other.idCategoria != null) || (this.idCategoria != null && !this.idCategoria.equals(other.idCategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CategoriasEncuestaClimaLaboral[ idCategoria=" + idCategoria + " ]";
    }
    
}
