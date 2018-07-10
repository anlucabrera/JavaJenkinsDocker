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
@Table(name = "nivel_ingresos_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelIngresosTipos.findAll", query = "SELECT n FROM NivelIngresosTipos n")
    , @NamedQuery(name = "NivelIngresosTipos.findByNivel", query = "SELECT n FROM NivelIngresosTipos n WHERE n.nivel = :nivel")
    , @NamedQuery(name = "NivelIngresosTipos.findByIngresos", query = "SELECT n FROM NivelIngresosTipos n WHERE n.ingresos = :ingresos")})
public class NivelIngresosTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nivel")
    private Short nivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ingresos")
    private String ingresos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingreso")
    private List<NivelIngresosEgresadosGeneracion> nivelIngresosEgresadosGeneracionList;

    public NivelIngresosTipos() {
    }

    public NivelIngresosTipos(Short nivel) {
        this.nivel = nivel;
    }

    public NivelIngresosTipos(Short nivel, String ingresos) {
        this.nivel = nivel;
        this.ingresos = ingresos;
    }

    public Short getNivel() {
        return nivel;
    }

    public void setNivel(Short nivel) {
        this.nivel = nivel;
    }

    public String getIngresos() {
        return ingresos;
    }

    public void setIngresos(String ingresos) {
        this.ingresos = ingresos;
    }

    @XmlTransient
    public List<NivelIngresosEgresadosGeneracion> getNivelIngresosEgresadosGeneracionList() {
        return nivelIngresosEgresadosGeneracionList;
    }

    public void setNivelIngresosEgresadosGeneracionList(List<NivelIngresosEgresadosGeneracion> nivelIngresosEgresadosGeneracionList) {
        this.nivelIngresosEgresadosGeneracionList = nivelIngresosEgresadosGeneracionList;
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
        if (!(object instanceof NivelIngresosTipos)) {
            return false;
        }
        NivelIngresosTipos other = (NivelIngresosTipos) object;
        if ((this.nivel == null && other.nivel != null) || (this.nivel != null && !this.nivel.equals(other.nivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosTipos[ nivel=" + nivel + " ]";
    }
    
}
