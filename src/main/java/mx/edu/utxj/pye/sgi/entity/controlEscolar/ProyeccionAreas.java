/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "proyeccion_areas", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProyeccionAreas.findAll", query = "SELECT p FROM ProyeccionAreas p")
    , @NamedQuery(name = "ProyeccionAreas.findByProcesoInscripcion", query = "SELECT p FROM ProyeccionAreas p WHERE p.proyeccionAreasPK.procesoInscripcion = :procesoInscripcion")
    , @NamedQuery(name = "ProyeccionAreas.findByPe", query = "SELECT p FROM ProyeccionAreas p WHERE p.proyeccionAreasPK.pe = :pe")
    , @NamedQuery(name = "ProyeccionAreas.findByProyeccionValidadas", query = "SELECT p FROM ProyeccionAreas p WHERE p.proyeccionValidadas = :proyeccionValidadas")
    , @NamedQuery(name = "ProyeccionAreas.findByProyeccionMatricula", query = "SELECT p FROM ProyeccionAreas p WHERE p.proyeccionMatricula = :proyeccionMatricula")})
public class ProyeccionAreas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProyeccionAreasPK proyeccionAreasPK;
    @Column(name = "proyeccion_validadas")
    private Integer proyeccionValidadas;
    @Column(name = "proyeccion_matricula")
    private Integer proyeccionMatricula;
    @JoinColumn(name = "proceso_inscripcion", referencedColumnName = "id_procesos_inscripcion", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProcesosInscripcion procesosInscripcion;

    public ProyeccionAreas() {
    }

    public ProyeccionAreas(ProyeccionAreasPK proyeccionAreasPK) {
        this.proyeccionAreasPK = proyeccionAreasPK;
    }

    public ProyeccionAreas(int procesoInscripcion, short pe) {
        this.proyeccionAreasPK = new ProyeccionAreasPK(procesoInscripcion, pe);
    }

    public ProyeccionAreasPK getProyeccionAreasPK() {
        return proyeccionAreasPK;
    }

    public void setProyeccionAreasPK(ProyeccionAreasPK proyeccionAreasPK) {
        this.proyeccionAreasPK = proyeccionAreasPK;
    }

    public Integer getProyeccionValidadas() {
        return proyeccionValidadas;
    }

    public void setProyeccionValidadas(Integer proyeccionValidadas) {
        this.proyeccionValidadas = proyeccionValidadas;
    }

    public Integer getProyeccionMatricula() {
        return proyeccionMatricula;
    }

    public void setProyeccionMatricula(Integer proyeccionMatricula) {
        this.proyeccionMatricula = proyeccionMatricula;
    }

    public ProcesosInscripcion getProcesosInscripcion() {
        return procesosInscripcion;
    }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) {
        this.procesosInscripcion = procesosInscripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proyeccionAreasPK != null ? proyeccionAreasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProyeccionAreas)) {
            return false;
        }
        ProyeccionAreas other = (ProyeccionAreas) object;
        if ((this.proyeccionAreasPK == null && other.proyeccionAreasPK != null) || (this.proyeccionAreasPK != null && !this.proyeccionAreasPK.equals(other.proyeccionAreasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ProyeccionAreas[ proyeccionAreasPK=" + proyeccionAreasPK + " ]";
    }
    
}
