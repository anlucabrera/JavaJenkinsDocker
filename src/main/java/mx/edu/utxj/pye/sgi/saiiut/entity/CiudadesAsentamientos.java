/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "ciudades_asentamientos", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CiudadesAsentamientos.findAll", query = "SELECT c FROM CiudadesAsentamientos c")
    , @NamedQuery(name = "CiudadesAsentamientos.findByCveEstado", query = "SELECT c FROM CiudadesAsentamientos c WHERE c.ciudadesAsentamientosPK.cveEstado = :cveEstado")
    , @NamedQuery(name = "CiudadesAsentamientos.findByCveCiudad", query = "SELECT c FROM CiudadesAsentamientos c WHERE c.ciudadesAsentamientosPK.cveCiudad = :cveCiudad")
    , @NamedQuery(name = "CiudadesAsentamientos.findByNombre", query = "SELECT c FROM CiudadesAsentamientos c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CiudadesAsentamientos.findByActivo", query = "SELECT c FROM CiudadesAsentamientos c WHERE c.activo = :activo")})
public class CiudadesAsentamientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CiudadesAsentamientosPK ciudadesAsentamientosPK;
    @Size(max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "activo")
    private Boolean activo;

    public CiudadesAsentamientos() {
    }

    public CiudadesAsentamientos(CiudadesAsentamientosPK ciudadesAsentamientosPK) {
        this.ciudadesAsentamientosPK = ciudadesAsentamientosPK;
    }

    public CiudadesAsentamientos(int cveEstado, int cveCiudad) {
        this.ciudadesAsentamientosPK = new CiudadesAsentamientosPK(cveEstado, cveCiudad);
    }

    public CiudadesAsentamientosPK getCiudadesAsentamientosPK() {
        return ciudadesAsentamientosPK;
    }

    public void setCiudadesAsentamientosPK(CiudadesAsentamientosPK ciudadesAsentamientosPK) {
        this.ciudadesAsentamientosPK = ciudadesAsentamientosPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ciudadesAsentamientosPK != null ? ciudadesAsentamientosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CiudadesAsentamientos)) {
            return false;
        }
        CiudadesAsentamientos other = (CiudadesAsentamientos) object;
        if ((this.ciudadesAsentamientosPK == null && other.ciudadesAsentamientosPK != null) || (this.ciudadesAsentamientosPK != null && !this.ciudadesAsentamientosPK.equals(other.ciudadesAsentamientosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.CiudadesAsentamientos[ ciudadesAsentamientosPK=" + ciudadesAsentamientosPK + " ]";
    }
    
}
