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
@Table(name = "tipo_sostenimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSostenimiento.findAll", query = "SELECT t FROM TipoSostenimiento t"),
    @NamedQuery(name = "TipoSostenimiento.findByIdtipoSostenimiento", query = "SELECT t FROM TipoSostenimiento t WHERE t.idtipoSostenimiento = :idtipoSostenimiento"),
    @NamedQuery(name = "TipoSostenimiento.findByNombre", query = "SELECT t FROM TipoSostenimiento t WHERE t.nombre = :nombre")})
public class TipoSostenimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idtipo_sostenimiento")
    private Short idtipoSostenimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoSostenimiento")
    private List<InstitucionAcademica> institucionAcademicaList;

    public TipoSostenimiento() {
    }

    public TipoSostenimiento(Short idtipoSostenimiento) {
        this.idtipoSostenimiento = idtipoSostenimiento;
    }

    public TipoSostenimiento(Short idtipoSostenimiento, String nombre) {
        this.idtipoSostenimiento = idtipoSostenimiento;
        this.nombre = nombre;
    }

    public Short getIdtipoSostenimiento() {
        return idtipoSostenimiento;
    }

    public void setIdtipoSostenimiento(Short idtipoSostenimiento) {
        this.idtipoSostenimiento = idtipoSostenimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<InstitucionAcademica> getInstitucionAcademicaList() {
        return institucionAcademicaList;
    }

    public void setInstitucionAcademicaList(List<InstitucionAcademica> institucionAcademicaList) {
        this.institucionAcademicaList = institucionAcademicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoSostenimiento != null ? idtipoSostenimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoSostenimiento)) {
            return false;
        }
        TipoSostenimiento other = (TipoSostenimiento) object;
        if ((this.idtipoSostenimiento == null && other.idtipoSostenimiento != null) || (this.idtipoSostenimiento != null && !this.idtipoSostenimiento.equals(other.idtipoSostenimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSostenimiento[ idtipoSostenimiento=" + idtipoSostenimiento + " ]";
    }
    
}
