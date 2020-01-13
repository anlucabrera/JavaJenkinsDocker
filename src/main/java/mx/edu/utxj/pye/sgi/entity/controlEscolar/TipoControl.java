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
 * @author Desarrollo
 */
@Entity
@Table(name = "tipo_control", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoControl.findAll", query = "SELECT t FROM TipoControl t")
    , @NamedQuery(name = "TipoControl.findByIdTipoControl", query = "SELECT t FROM TipoControl t WHERE t.idTipoControl = :idTipoControl")
    , @NamedQuery(name = "TipoControl.findByNombre", query = "SELECT t FROM TipoControl t WHERE t.nombre = :nombre")})
public class TipoControl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_control")
    private Short idTipoControl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoControl")
    private List<InstitucionAcademica> institucionAcademicaList;

    public TipoControl() {
    }

    public TipoControl(Short idTipoControl) {
        this.idTipoControl = idTipoControl;
    }

    public TipoControl(Short idTipoControl, String nombre) {
        this.idTipoControl = idTipoControl;
        this.nombre = nombre;
    }

    public Short getIdTipoControl() {
        return idTipoControl;
    }

    public void setIdTipoControl(Short idTipoControl) {
        this.idTipoControl = idTipoControl;
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
        hash += (idTipoControl != null ? idTipoControl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoControl)) {
            return false;
        }
        TipoControl other = (TipoControl) object;
        if ((this.idTipoControl == null && other.idTipoControl != null) || (this.idTipoControl != null && !this.idTipoControl.equals(other.idTipoControl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoControl[ idTipoControl=" + idTipoControl + " ]";
    }
    
}
