/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

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
 * @author Desarrollo
 */
@Entity
@Table(name = "planes_estudio_externos", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanesEstudioExternos.findAll", query = "SELECT p FROM PlanesEstudioExternos p")
    , @NamedQuery(name = "PlanesEstudioExternos.findByIdplanEstudio", query = "SELECT p FROM PlanesEstudioExternos p WHERE p.idplanEstudio = :idplanEstudio")
    , @NamedQuery(name = "PlanesEstudioExternos.findByNombre", query = "SELECT p FROM PlanesEstudioExternos p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PlanesEstudioExternos.findByAnio", query = "SELECT p FROM PlanesEstudioExternos p WHERE p.anio = :anio")})
public class PlanesEstudioExternos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_planEstudio")
    private Integer idplanEstudio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "anio")
    private Short anio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idplanEstudio")
    private List<CalificacionesHistorialTsu> calificacionesHistorialTsuList;

    public PlanesEstudioExternos() {
    }

    public PlanesEstudioExternos(Integer idplanEstudio) {
        this.idplanEstudio = idplanEstudio;
    }

    public PlanesEstudioExternos(Integer idplanEstudio, String nombre) {
        this.idplanEstudio = idplanEstudio;
        this.nombre = nombre;
    }

    public Integer getIdplanEstudio() {
        return idplanEstudio;
    }

    public void setIdplanEstudio(Integer idplanEstudio) {
        this.idplanEstudio = idplanEstudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    @XmlTransient
    public List<CalificacionesHistorialTsu> getCalificacionesHistorialTsuList() {
        return calificacionesHistorialTsuList;
    }

    public void setCalificacionesHistorialTsuList(List<CalificacionesHistorialTsu> calificacionesHistorialTsuList) {
        this.calificacionesHistorialTsuList = calificacionesHistorialTsuList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idplanEstudio != null ? idplanEstudio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanesEstudioExternos)) {
            return false;
        }
        PlanesEstudioExternos other = (PlanesEstudioExternos) object;
        if ((this.idplanEstudio == null && other.idplanEstudio != null) || (this.idplanEstudio != null && !this.idplanEstudio.equals(other.idplanEstudio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanesEstudioExternos[ idplanEstudio=" + idplanEstudio + " ]";
    }
    
}
