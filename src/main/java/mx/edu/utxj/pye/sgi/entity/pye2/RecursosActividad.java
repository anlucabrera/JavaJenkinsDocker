/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "recursos_actividad", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecursosActividad.findAll", query = "SELECT r FROM RecursosActividad r")
    , @NamedQuery(name = "RecursosActividad.findByRecursoActividad", query = "SELECT r FROM RecursosActividad r WHERE r.recursoActividad = :recursoActividad")
    , @NamedQuery(name = "RecursosActividad.findByRPEnero", query = "SELECT r FROM RecursosActividad r WHERE r.rPEnero = :rPEnero")
    , @NamedQuery(name = "RecursosActividad.findByRPFebero", query = "SELECT r FROM RecursosActividad r WHERE r.rPFebero = :rPFebero")
    , @NamedQuery(name = "RecursosActividad.findByRPMarzo", query = "SELECT r FROM RecursosActividad r WHERE r.rPMarzo = :rPMarzo")
    , @NamedQuery(name = "RecursosActividad.findByRPAbril", query = "SELECT r FROM RecursosActividad r WHERE r.rPAbril = :rPAbril")
    , @NamedQuery(name = "RecursosActividad.findByRPMayo", query = "SELECT r FROM RecursosActividad r WHERE r.rPMayo = :rPMayo")
    , @NamedQuery(name = "RecursosActividad.findByRPJunio", query = "SELECT r FROM RecursosActividad r WHERE r.rPJunio = :rPJunio")
    , @NamedQuery(name = "RecursosActividad.findByRPJulio", query = "SELECT r FROM RecursosActividad r WHERE r.rPJulio = :rPJulio")
    , @NamedQuery(name = "RecursosActividad.findByRPAgosto", query = "SELECT r FROM RecursosActividad r WHERE r.rPAgosto = :rPAgosto")
    , @NamedQuery(name = "RecursosActividad.findByRPSeptiembre", query = "SELECT r FROM RecursosActividad r WHERE r.rPSeptiembre = :rPSeptiembre")
    , @NamedQuery(name = "RecursosActividad.findByRPOctubre", query = "SELECT r FROM RecursosActividad r WHERE r.rPOctubre = :rPOctubre")
    , @NamedQuery(name = "RecursosActividad.findByRPNoviembre", query = "SELECT r FROM RecursosActividad r WHERE r.rPNoviembre = :rPNoviembre")
    , @NamedQuery(name = "RecursosActividad.findByRPDiciembre", query = "SELECT r FROM RecursosActividad r WHERE r.rPDiciembre = :rPDiciembre")
    , @NamedQuery(name = "RecursosActividad.findByTotal", query = "SELECT r FROM RecursosActividad r WHERE r.total = :total")
    , @NamedQuery(name = "RecursosActividad.findByJustificacion", query = "SELECT r FROM RecursosActividad r WHERE r.justificacion = :justificacion")})
public class RecursosActividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "recurso_actividad")
    private Integer recursoActividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Enero")
    private double rPEnero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Febero")
    private double rPFebero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Marzo")
    private double rPMarzo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Abril")
    private double rPAbril;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Mayo")
    private double rPMayo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Junio")
    private double rPJunio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Julio")
    private double rPJulio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Agosto")
    private double rPAgosto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Septiembre")
    private double rPSeptiembre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Octubre")
    private double rPOctubre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Noviembre")
    private double rPNoviembre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RP_Diciembre")
    private double rPDiciembre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private double total;
    @Size(max = 1000)
    @Column(name = "justificacion")
    private String justificacion;
    @JoinColumn(name = "actividad_poa", referencedColumnName = "actividad_poa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ActividadesPoa actividadPoa;
    @JoinColumn(name = "producto_area", referencedColumnName = "producto_area")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProductosAreas productoArea;

    public RecursosActividad() {
    }

    public RecursosActividad(Integer recursoActividad) {
        this.recursoActividad = recursoActividad;
    }

    public RecursosActividad(Integer recursoActividad, double rPEnero, double rPFebero, double rPMarzo, double rPAbril, double rPMayo, double rPJunio, double rPJulio, double rPAgosto, double rPSeptiembre, double rPOctubre, double rPNoviembre, double rPDiciembre, double total) {
        this.recursoActividad = recursoActividad;
        this.rPEnero = rPEnero;
        this.rPFebero = rPFebero;
        this.rPMarzo = rPMarzo;
        this.rPAbril = rPAbril;
        this.rPMayo = rPMayo;
        this.rPJunio = rPJunio;
        this.rPJulio = rPJulio;
        this.rPAgosto = rPAgosto;
        this.rPSeptiembre = rPSeptiembre;
        this.rPOctubre = rPOctubre;
        this.rPNoviembre = rPNoviembre;
        this.rPDiciembre = rPDiciembre;
        this.total = total;
    }

    public Integer getRecursoActividad() {
        return recursoActividad;
    }

    public void setRecursoActividad(Integer recursoActividad) {
        this.recursoActividad = recursoActividad;
    }

    public double getRPEnero() {
        return rPEnero;
    }

    public void setRPEnero(double rPEnero) {
        this.rPEnero = rPEnero;
    }

    public double getRPFebero() {
        return rPFebero;
    }

    public void setRPFebero(double rPFebero) {
        this.rPFebero = rPFebero;
    }

    public double getRPMarzo() {
        return rPMarzo;
    }

    public void setRPMarzo(double rPMarzo) {
        this.rPMarzo = rPMarzo;
    }

    public double getRPAbril() {
        return rPAbril;
    }

    public void setRPAbril(double rPAbril) {
        this.rPAbril = rPAbril;
    }

    public double getRPMayo() {
        return rPMayo;
    }

    public void setRPMayo(double rPMayo) {
        this.rPMayo = rPMayo;
    }

    public double getRPJunio() {
        return rPJunio;
    }

    public void setRPJunio(double rPJunio) {
        this.rPJunio = rPJunio;
    }

    public double getRPJulio() {
        return rPJulio;
    }

    public void setRPJulio(double rPJulio) {
        this.rPJulio = rPJulio;
    }

    public double getRPAgosto() {
        return rPAgosto;
    }

    public void setRPAgosto(double rPAgosto) {
        this.rPAgosto = rPAgosto;
    }

    public double getRPSeptiembre() {
        return rPSeptiembre;
    }

    public void setRPSeptiembre(double rPSeptiembre) {
        this.rPSeptiembre = rPSeptiembre;
    }

    public double getRPOctubre() {
        return rPOctubre;
    }

    public void setRPOctubre(double rPOctubre) {
        this.rPOctubre = rPOctubre;
    }

    public double getRPNoviembre() {
        return rPNoviembre;
    }

    public void setRPNoviembre(double rPNoviembre) {
        this.rPNoviembre = rPNoviembre;
    }

    public double getRPDiciembre() {
        return rPDiciembre;
    }

    public void setRPDiciembre(double rPDiciembre) {
        this.rPDiciembre = rPDiciembre;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public ActividadesPoa getActividadPoa() {
        return actividadPoa;
    }

    public void setActividadPoa(ActividadesPoa actividadPoa) {
        this.actividadPoa = actividadPoa;
    }

    public ProductosAreas getProductoArea() {
        return productoArea;
    }

    public void setProductoArea(ProductosAreas productoArea) {
        this.productoArea = productoArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recursoActividad != null ? recursoActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecursosActividad)) {
            return false;
        }
        RecursosActividad other = (RecursosActividad) object;
        if ((this.recursoActividad == null && other.recursoActividad != null) || (this.recursoActividad != null && !this.recursoActividad.equals(other.recursoActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad[ recursoActividad=" + recursoActividad + " ]";
    }
    
}
