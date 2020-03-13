/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "pruebaguardadoexcel", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pruebaguardadoexcel.findAll", query = "SELECT p FROM Pruebaguardadoexcel p")
    , @NamedQuery(name = "Pruebaguardadoexcel.findByNombre", query = "SELECT p FROM Pruebaguardadoexcel p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Pruebaguardadoexcel.findByTotalPeriodoAnterior", query = "SELECT p FROM Pruebaguardadoexcel p WHERE p.totalPeriodoAnterior = :totalPeriodoAnterior")
    , @NamedQuery(name = "Pruebaguardadoexcel.findByTotalPeriodoActual", query = "SELECT p FROM Pruebaguardadoexcel p WHERE p.totalPeriodoActual = :totalPeriodoActual")})
public class Pruebaguardadoexcel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalPeriodoAnterior")
    private int totalPeriodoAnterior;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalPeriodoActual")
    private int totalPeriodoActual;

    public Pruebaguardadoexcel() {
    }

    public Pruebaguardadoexcel(String nombre) {
        this.nombre = nombre;
    }

    public Pruebaguardadoexcel(String nombre, int totalPeriodoAnterior, int totalPeriodoActual) {
        this.nombre = nombre;
        this.totalPeriodoAnterior = totalPeriodoAnterior;
        this.totalPeriodoActual = totalPeriodoActual;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTotalPeriodoAnterior() {
        return totalPeriodoAnterior;
    }

    public void setTotalPeriodoAnterior(int totalPeriodoAnterior) {
        this.totalPeriodoAnterior = totalPeriodoAnterior;
    }

    public int getTotalPeriodoActual() {
        return totalPeriodoActual;
    }

    public void setTotalPeriodoActual(int totalPeriodoActual) {
        this.totalPeriodoActual = totalPeriodoActual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pruebaguardadoexcel)) {
            return false;
        }
        Pruebaguardadoexcel other = (Pruebaguardadoexcel) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Pruebaguardadoexcel[ nombre=" + nombre + " ]";
    }
    
}
