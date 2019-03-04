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
@Table(name = "materias_programa_educativo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MateriasProgramaEducativo.findAll", query = "SELECT m FROM MateriasProgramaEducativo m")
    , @NamedQuery(name = "MateriasProgramaEducativo.findByCveMateria", query = "SELECT m FROM MateriasProgramaEducativo m WHERE m.cveMateria = :cveMateria")
    , @NamedQuery(name = "MateriasProgramaEducativo.findByNombre", query = "SELECT m FROM MateriasProgramaEducativo m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "MateriasProgramaEducativo.findByCuatrimestre", query = "SELECT m FROM MateriasProgramaEducativo m WHERE m.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "MateriasProgramaEducativo.findByProgramaEducativo", query = "SELECT m FROM MateriasProgramaEducativo m WHERE m.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "MateriasProgramaEducativo.findByActivo", query = "SELECT m FROM MateriasProgramaEducativo m WHERE m.activo = :activo")})
public class MateriasProgramaEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private short cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asignatura")
    private List<DesercionReprobacionMaterias> desercionReprobacionMateriasList;

    public MateriasProgramaEducativo() {
    }

    public MateriasProgramaEducativo(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public MateriasProgramaEducativo(String cveMateria, String nombre, short cuatrimestre, short programaEducativo, boolean activo) {
        this.cveMateria = cveMateria;
        this.nombre = nombre;
        this.cuatrimestre = cuatrimestre;
        this.programaEducativo = programaEducativo;
        this.activo = activo;
    }

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(short cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<DesercionReprobacionMaterias> getDesercionReprobacionMateriasList() {
        return desercionReprobacionMateriasList;
    }

    public void setDesercionReprobacionMateriasList(List<DesercionReprobacionMaterias> desercionReprobacionMateriasList) {
        this.desercionReprobacionMateriasList = desercionReprobacionMateriasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveMateria != null ? cveMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriasProgramaEducativo)) {
            return false;
        }
        MateriasProgramaEducativo other = (MateriasProgramaEducativo) object;
        if ((this.cveMateria == null && other.cveMateria != null) || (this.cveMateria != null && !this.cveMateria.equals(other.cveMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.MateriasProgramaEducativo[ cveMateria=" + cveMateria + " ]";
    }
    
}
