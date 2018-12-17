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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@Table(name = "proyectos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proyectos.findAll", query = "SELECT p FROM Proyectos p")
    , @NamedQuery(name = "Proyectos.findByProyecto", query = "SELECT p FROM Proyectos p WHERE p.proyecto = :proyecto")
    , @NamedQuery(name = "Proyectos.findByNumero", query = "SELECT p FROM Proyectos p WHERE p.numero = :numero")})
public class Proyectos implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private short numero;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "nombre")
    private String nombre;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "proyecto")
    private Integer proyecto;
    @ManyToMany(mappedBy = "proyectosList")
    private List<IndicadoresPide> indicadoresPideList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proyectos")
    private List<ProyectoArea> proyectoAreaList;
    @OneToMany(mappedBy = "proyecto")
    private List<CuadroMandoIntegral> cuadroMandoIntegralList;

    public Proyectos() {
    }

    public Proyectos(Integer proyecto) {
        this.proyecto = proyecto;
    }

    public Proyectos(Integer proyecto, short numero, String nombre) {
        this.proyecto = proyecto;
        this.numero = numero;
        this.nombre = nombre;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }


    @XmlTransient
    public List<IndicadoresPide> getIndicadoresPideList() {
        return indicadoresPideList;
    }

    public void setIndicadoresPideList(List<IndicadoresPide> indicadoresPideList) {
        this.indicadoresPideList = indicadoresPideList;
    }

    @XmlTransient
    public List<ProyectoArea> getProyectoAreaList() {
        return proyectoAreaList;
    }

    public void setProyectoAreaList(List<ProyectoArea> proyectoAreaList) {
        this.proyectoAreaList = proyectoAreaList;
    }

    @XmlTransient
    public List<CuadroMandoIntegral> getCuadroMandoIntegralList() {
        return cuadroMandoIntegralList;
    }

    public void setCuadroMandoIntegralList(List<CuadroMandoIntegral> cuadroMandoIntegralList) {
        this.cuadroMandoIntegralList = cuadroMandoIntegralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proyecto != null ? proyecto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proyectos)) {
            return false;
        }
        Proyectos other = (Proyectos) object;
        if ((this.proyecto == null && other.proyecto != null) || (this.proyecto != null && !this.proyecto.equals(other.proyecto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Proyectos[ proyecto=" + proyecto + " ]";
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
