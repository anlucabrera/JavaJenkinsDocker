/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "planeaciones_detalles", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaneacionesDetalles.findAll", query = "SELECT p FROM PlaneacionesDetalles p")
    , @NamedQuery(name = "PlaneacionesDetalles.findByPeriodo", query = "SELECT p FROM PlaneacionesDetalles p WHERE p.planeacionesDetallesPK.periodo = :periodo")
    , @NamedQuery(name = "PlaneacionesDetalles.findByDirector", query = "SELECT p FROM PlaneacionesDetalles p WHERE p.planeacionesDetallesPK.director = :director")
    , @NamedQuery(name = "PlaneacionesDetalles.findByEstudiantesEstadia", query = "SELECT p FROM PlaneacionesDetalles p WHERE p.estudiantesEstadia = :estudiantesEstadia")
    , @NamedQuery(name = "PlaneacionesDetalles.findByProyectosEstadia", query = "SELECT p FROM PlaneacionesDetalles p WHERE p.proyectosEstadia = :proyectosEstadia")})
public class PlaneacionesDetalles implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlaneacionesDetallesPK planeacionesDetallesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estudiantesEstadia")
    private int estudiantesEstadia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "proyectosEstadia")
    private int proyectosEstadia;
    @JoinColumn(name = "director", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;

    public PlaneacionesDetalles() {
    }

    public PlaneacionesDetalles(PlaneacionesDetallesPK planeacionesDetallesPK) {
        this.planeacionesDetallesPK = planeacionesDetallesPK;
    }

    public PlaneacionesDetalles(PlaneacionesDetallesPK planeacionesDetallesPK, int estudiantesEstadia, int proyectosEstadia) {
        this.planeacionesDetallesPK = planeacionesDetallesPK;
        this.estudiantesEstadia = estudiantesEstadia;
        this.proyectosEstadia = proyectosEstadia;
    }

    public PlaneacionesDetalles(int periodo, int director) {
        this.planeacionesDetallesPK = new PlaneacionesDetallesPK(periodo, director);
    }

    public PlaneacionesDetallesPK getPlaneacionesDetallesPK() {
        return planeacionesDetallesPK;
    }

    public void setPlaneacionesDetallesPK(PlaneacionesDetallesPK planeacionesDetallesPK) {
        this.planeacionesDetallesPK = planeacionesDetallesPK;
    }

    public int getEstudiantesEstadia() {
        return estudiantesEstadia;
    }

    public void setEstudiantesEstadia(int estudiantesEstadia) {
        this.estudiantesEstadia = estudiantesEstadia;
    }

    public int getProyectosEstadia() {
        return proyectosEstadia;
    }

    public void setProyectosEstadia(int proyectosEstadia) {
        this.proyectosEstadia = proyectosEstadia;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planeacionesDetallesPK != null ? planeacionesDetallesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaneacionesDetalles)) {
            return false;
        }
        PlaneacionesDetalles other = (PlaneacionesDetalles) object;
        if ((this.planeacionesDetallesPK == null && other.planeacionesDetallesPK != null) || (this.planeacionesDetallesPK != null && !this.planeacionesDetallesPK.equals(other.planeacionesDetallesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesDetalles[ planeacionesDetallesPK=" + planeacionesDetallesPK + " ]";
    }
    
}
