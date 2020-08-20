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
import javax.persistence.FetchType;
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
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpacad_areas_estudio", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadAreasEstudio.findAll", query = "SELECT c FROM CuerpacadAreasEstudio c")
    , @NamedQuery(name = "CuerpacadAreasEstudio.findByAreaEstudio", query = "SELECT c FROM CuerpacadAreasEstudio c WHERE c.areaEstudio = :areaEstudio")
    , @NamedQuery(name = "CuerpacadAreasEstudio.findByNombre", query = "SELECT c FROM CuerpacadAreasEstudio c WHERE c.nombre = :nombre")})
public class CuerpacadAreasEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "area_estudio")
    private Short areaEstudio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaEstudio", fetch = FetchType.LAZY)
    private List<CuerposAcademicosRegistroBitacora> cuerposAcademicosRegistroBitacoraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaEstudio", fetch = FetchType.LAZY)
    private List<CuerposAcademicosRegistro> cuerposAcademicosRegistroList;

    public CuerpacadAreasEstudio() {
    }

    public CuerpacadAreasEstudio(Short areaEstudio) {
        this.areaEstudio = areaEstudio;
    }

    public CuerpacadAreasEstudio(Short areaEstudio, String nombre) {
        this.areaEstudio = areaEstudio;
        this.nombre = nombre;
    }

    public Short getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(Short areaEstudio) {
        this.areaEstudio = areaEstudio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<CuerposAcademicosRegistroBitacora> getCuerposAcademicosRegistroBitacoraList() {
        return cuerposAcademicosRegistroBitacoraList;
    }

    public void setCuerposAcademicosRegistroBitacoraList(List<CuerposAcademicosRegistroBitacora> cuerposAcademicosRegistroBitacoraList) {
        this.cuerposAcademicosRegistroBitacoraList = cuerposAcademicosRegistroBitacoraList;
    }

    @XmlTransient
    public List<CuerposAcademicosRegistro> getCuerposAcademicosRegistroList() {
        return cuerposAcademicosRegistroList;
    }

    public void setCuerposAcademicosRegistroList(List<CuerposAcademicosRegistro> cuerposAcademicosRegistroList) {
        this.cuerposAcademicosRegistroList = cuerposAcademicosRegistroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (areaEstudio != null ? areaEstudio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpacadAreasEstudio)) {
            return false;
        }
        CuerpacadAreasEstudio other = (CuerpacadAreasEstudio) object;
        if ((this.areaEstudio == null && other.areaEstudio != null) || (this.areaEstudio != null && !this.areaEstudio.equals(other.areaEstudio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadAreasEstudio[ areaEstudio=" + areaEstudio + " ]";
    }
    
}
