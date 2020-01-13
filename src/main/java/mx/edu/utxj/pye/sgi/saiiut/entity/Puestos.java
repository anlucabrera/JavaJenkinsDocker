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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeación
 */
@Entity
@Table(name = "puestos", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puestos.findAll", query = "SELECT p FROM Puestos p")
    , @NamedQuery(name = "Puestos.findByCvePuesto", query = "SELECT p FROM Puestos p WHERE p.puestosPK.cvePuesto = :cvePuesto")
    , @NamedQuery(name = "Puestos.findByCveUniversidad", query = "SELECT p FROM Puestos p WHERE p.puestosPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Puestos.findByDescripcion", query = "SELECT p FROM Puestos p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "Puestos.findByAbreviatura", query = "SELECT p FROM Puestos p WHERE p.abreviatura = :abreviatura")
    , @NamedQuery(name = "Puestos.findByActivo", query = "SELECT p FROM Puestos p WHERE p.activo = :activo")
    , @NamedQuery(name = "Puestos.findByAbreviaturaF", query = "SELECT p FROM Puestos p WHERE p.abreviaturaF = :abreviaturaF")
    , @NamedQuery(name = "Puestos.findByDescripcionMasculino", query = "SELECT p FROM Puestos p WHERE p.descripcionMasculino = :descripcionMasculino")
    , @NamedQuery(name = "Puestos.findByDescripcionFemenino", query = "SELECT p FROM Puestos p WHERE p.descripcionFemenino = :descripcionFemenino")})
public class Puestos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PuestosPK puestosPK;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "activo")
    private Boolean activo;
    @Size(max = 20)
    @Column(name = "abreviaturaF")
    private String abreviaturaF;
    @Size(max = 100)
    @Column(name = "descripcion_masculino")
    private String descripcionMasculino;
    @Size(max = 100)
    @Column(name = "descripcion_femenino")
    private String descripcionFemenino;
    @JoinColumn(name = "cve_universidad", referencedColumnName = "cve_universidad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Universidades universidades;

    public Puestos() {
    }

    public Puestos(PuestosPK puestosPK) {
        this.puestosPK = puestosPK;
    }

    public Puestos(int cvePuesto, int cveUniversidad) {
        this.puestosPK = new PuestosPK(cvePuesto, cveUniversidad);
    }

    public PuestosPK getPuestosPK() {
        return puestosPK;
    }

    public void setPuestosPK(PuestosPK puestosPK) {
        this.puestosPK = puestosPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getAbreviaturaF() {
        return abreviaturaF;
    }

    public void setAbreviaturaF(String abreviaturaF) {
        this.abreviaturaF = abreviaturaF;
    }

    public String getDescripcionMasculino() {
        return descripcionMasculino;
    }

    public void setDescripcionMasculino(String descripcionMasculino) {
        this.descripcionMasculino = descripcionMasculino;
    }

    public String getDescripcionFemenino() {
        return descripcionFemenino;
    }

    public void setDescripcionFemenino(String descripcionFemenino) {
        this.descripcionFemenino = descripcionFemenino;
    }

    public Universidades getUniversidades() {
        return universidades;
    }

    public void setUniversidades(Universidades universidades) {
        this.universidades = universidades;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (puestosPK != null ? puestosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puestos)) {
            return false;
        }
        Puestos other = (Puestos) object;
        if ((this.puestosPK == null && other.puestosPK != null) || (this.puestosPK != null && !this.puestosPK.equals(other.puestosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Puestos[ puestosPK=" + puestosPK + " ]";
    }
    
}
