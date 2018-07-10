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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuadro_mando_integral", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuadroMandoIntegral.findAll", query = "SELECT c FROM CuadroMandoIntegral c")
    , @NamedQuery(name = "CuadroMandoIntegral.findByCuadroMandoInt", query = "SELECT c FROM CuadroMandoIntegral c WHERE c.cuadroMandoInt = :cuadroMandoInt")})
public class CuadroMandoIntegral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cuadro_mando_int")
    private Integer cuadroMandoInt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuadroMandoInt")
    private List<ActividadesPoa> actividadesPoaList;
    @JoinColumn(name = "ejercicio_fiscal", referencedColumnName = "ejercicio_fiscal")
    @ManyToOne(optional = false)
    private EjerciciosFiscales ejercicioFiscal;
    @JoinColumn(name = "eje", referencedColumnName = "eje")
    @ManyToOne(optional = false)
    private EjesRegistro eje;
    @JoinColumn(name = "estrategia", referencedColumnName = "estrategia")
    @ManyToOne(optional = false)
    private Estrategias estrategia;
    @JoinColumn(name = "linea_accion", referencedColumnName = "linea_accion")
    @ManyToOne(optional = false)
    private LineasAccion lineaAccion;
    @JoinColumn(name = "proyecto", referencedColumnName = "proyecto")
    @ManyToOne
    private Proyectos proyecto;

    public CuadroMandoIntegral() {
    }

    public CuadroMandoIntegral(Integer cuadroMandoInt) {
        this.cuadroMandoInt = cuadroMandoInt;
    }

    public Integer getCuadroMandoInt() {
        return cuadroMandoInt;
    }

    public void setCuadroMandoInt(Integer cuadroMandoInt) {
        this.cuadroMandoInt = cuadroMandoInt;
    }

    @XmlTransient
    public List<ActividadesPoa> getActividadesPoaList() {
        return actividadesPoaList;
    }

    public void setActividadesPoaList(List<ActividadesPoa> actividadesPoaList) {
        this.actividadesPoaList = actividadesPoaList;
    }

    public EjerciciosFiscales getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(EjerciciosFiscales ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public EjesRegistro getEje() {
        return eje;
    }

    public void setEje(EjesRegistro eje) {
        this.eje = eje;
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
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral[ cuadroMandoInt=" + cuadroMandoInt + " ]";
    }
    
}
