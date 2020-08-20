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
@Table(name = "indicadores_categorias", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadoresCategorias.findAll", query = "SELECT i FROM IndicadoresCategorias i")
    , @NamedQuery(name = "IndicadoresCategorias.findByCategoria", query = "SELECT i FROM IndicadoresCategorias i WHERE i.categoria = :categoria")
    , @NamedQuery(name = "IndicadoresCategorias.findByNombre", query = "SELECT i FROM IndicadoresCategorias i WHERE i.nombre = :nombre")})
public class IndicadoresCategorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "categoria")
    private Integer categoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<IndicadoresCarrousel> indicadoresCarrouselList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Indicadores> indicadoresList;

    public IndicadoresCategorias() {
    }

    public IndicadoresCategorias(Integer categoria) {
        this.categoria = categoria;
    }

    public IndicadoresCategorias(Integer categoria, String nombre) {
        this.categoria = categoria;
        this.nombre = nombre;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<IndicadoresCarrousel> getIndicadoresCarrouselList() {
        return indicadoresCarrouselList;
    }

    public void setIndicadoresCarrouselList(List<IndicadoresCarrousel> indicadoresCarrouselList) {
        this.indicadoresCarrouselList = indicadoresCarrouselList;
    }

    @XmlTransient
    public List<Indicadores> getIndicadoresList() {
        return indicadoresList;
    }

    public void setIndicadoresList(List<Indicadores> indicadoresList) {
        this.indicadoresList = indicadoresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoria != null ? categoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadoresCategorias)) {
            return false;
        }
        IndicadoresCategorias other = (IndicadoresCategorias) object;
        if ((this.categoria == null && other.categoria != null) || (this.categoria != null && !this.categoria.equals(other.categoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.IndicadoresCategorias[ categoria=" + categoria + " ]";
    }
    
}
