/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

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
@Table(name = "nivel_servidores", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelServidores.findAll", query = "SELECT n FROM NivelServidores n")
    , @NamedQuery(name = "NivelServidores.findByNivel", query = "SELECT n FROM NivelServidores n WHERE n.nivel = :nivel")
    , @NamedQuery(name = "NivelServidores.findByDescripcion", query = "SELECT n FROM NivelServidores n WHERE n.descripcion = :descripcion")})
public class NivelServidores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nivel")
    private Integer nivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel")
    private List<TarifasPorKilometro> tarifasPorKilometroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivel")
    private List<TarifasPorZona> tarifasPorZonaList;

    public NivelServidores() {
    }

    public NivelServidores(Integer nivel) {
        this.nivel = nivel;
    }

    public NivelServidores(Integer nivel, String descripcion) {
        this.nivel = nivel;
        this.descripcion = descripcion;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<TarifasPorKilometro> getTarifasPorKilometroList() {
        return tarifasPorKilometroList;
    }

    public void setTarifasPorKilometroList(List<TarifasPorKilometro> tarifasPorKilometroList) {
        this.tarifasPorKilometroList = tarifasPorKilometroList;
    }

    @XmlTransient
    public List<TarifasPorZona> getTarifasPorZonaList() {
        return tarifasPorZonaList;
    }

    public void setTarifasPorZonaList(List<TarifasPorZona> tarifasPorZonaList) {
        this.tarifasPorZonaList = tarifasPorZonaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivel != null ? nivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelServidores)) {
            return false;
        }
        NivelServidores other = (NivelServidores) object;
        if ((this.nivel == null && other.nivel != null) || (this.nivel != null && !this.nivel.equals(other.nivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.NivelServidores[ nivel=" + nivel + " ]";
    }
    
}
