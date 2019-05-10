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
@Table(name = "ejes", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ejes.findAll", query = "SELECT e FROM Ejes e")
    , @NamedQuery(name = "Ejes.findByEje", query = "SELECT e FROM Ejes e WHERE e.eje = :eje")
    , @NamedQuery(name = "Ejes.findByNombre", query = "SELECT e FROM Ejes e WHERE e.nombre = :nombre")})
public class Ejes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eje")
    private Integer eje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eje")
    private List<IndicadoresCarrousel> indicadoresCarrouselList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eje")
    private List<Indicadores> indicadoresList;

    public Ejes() {
    }

    public Ejes(Integer eje) {
        this.eje = eje;
    }

    public Ejes(Integer eje, String nombre) {
        this.eje = eje;
        this.nombre = nombre;
    }

    public Integer getEje() {
        return eje;
    }

    public void setEje(Integer eje) {
        this.eje = eje;
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
        hash += (eje != null ? eje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ejes)) {
            return false;
        }
        Ejes other = (Ejes) object;
        if ((this.eje == null && other.eje != null) || (this.eje != null && !this.eje.equals(other.eje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Ejes[ eje=" + eje + " ]";
    }
    
}
