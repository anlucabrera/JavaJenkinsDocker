/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.poa;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuadro_mando_integral", catalog = "poa", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuadroMandoIntegral.findAll", query = "SELECT c FROM CuadroMandoIntegral c")
    , @NamedQuery(name = "CuadroMandoIntegral.findByCuadroMandoInt", query = "SELECT c FROM CuadroMandoIntegral c WHERE c.cuadroMandoInt = :cuadroMandoInt")
    , @NamedQuery(name = "CuadroMandoIntegral.findByEje", query = "SELECT c FROM CuadroMandoIntegral c WHERE c.eje = :eje")
    , @NamedQuery(name = "CuadroMandoIntegral.findByEjercicioFiscal", query = "SELECT c FROM CuadroMandoIntegral c WHERE c.ejercicioFiscal = :ejercicioFiscal")})
public class CuadroMandoIntegral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cuadro_mando_int")
    private Integer cuadroMandoInt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eje")
    private int eje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ejercicio_fiscal")
    private short ejercicioFiscal;
    @JoinColumn(name = "estrategia", referencedColumnName = "estrategia")
    @ManyToOne(optional = false)
    private Estrategias estrategia;
    @JoinColumn(name = "linea_accion", referencedColumnName = "linea_accion")
    @ManyToOne(optional = false)
    private LineasAccion lineaAccion;
    @JoinColumn(name = "proyecto", referencedColumnName = "proyecto")
    @ManyToOne
    private Proyectos proyecto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuadroMandoInt")
    private List<ActividadesPoa> actividadesPoaList;

    public CuadroMandoIntegral() {
    }

    public CuadroMandoIntegral(Integer cuadroMandoInt) {
        this.cuadroMandoInt = cuadroMandoInt;
    }

    public CuadroMandoIntegral(Integer cuadroMandoInt, int eje, short ejercicioFiscal) {
        this.cuadroMandoInt = cuadroMandoInt;
        this.eje = eje;
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public Integer getCuadroMandoInt() {
        return cuadroMandoInt;
    }

    public void setCuadroMandoInt(Integer cuadroMandoInt) {
        this.cuadroMandoInt = cuadroMandoInt;
    }

    public int getEje() {
        return eje;
    }

    public void setEje(int eje) {
        this.eje = eje;
    }

    public short getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public Estrategias getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(Estrategias estrategia) {
        this.estrategia = estrategia;
    }

    public LineasAccion getLineaAccion() {
        return lineaAccion;
    }

    public void setLineaAccion(LineasAccion lineaAccion) {
        this.lineaAccion = lineaAccion;
    }

    public Proyectos getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyectos proyecto) {
        this.proyecto = proyecto;
    }

    @XmlTransient
    public List<ActividadesPoa> getActividadesPoaList() {
        return actividadesPoaList;
    }

    public void setActividadesPoaList(List<ActividadesPoa> actividadesPoaList) {
        this.actividadesPoaList = actividadesPoaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuadroMandoInt != null ? cuadroMandoInt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuadroMandoIntegral)) {
            return false;
        }
        CuadroMandoIntegral other = (CuadroMandoIntegral) object;
        if ((this.cuadroMandoInt == null && other.cuadroMandoInt != null) || (this.cuadroMandoInt != null && !this.cuadroMandoInt.equals(other.cuadroMandoInt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.poa.entyti.CuadroMandoIntegral[ cuadroMandoInt=" + cuadroMandoInt + " ]";
    }
    
}
