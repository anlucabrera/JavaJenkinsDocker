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
 * @author HOME
 */
@Entity
@Table(name = "tipo_sangre", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSangre.findAll", query = "SELECT t FROM TipoSangre t")
    , @NamedQuery(name = "TipoSangre.findByIdTipoSangre", query = "SELECT t FROM TipoSangre t WHERE t.idTipoSangre = :idTipoSangre")
    , @NamedQuery(name = "TipoSangre.findByNombre", query = "SELECT t FROM TipoSangre t WHERE t.nombre = :nombre")})
public class TipoSangre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_sangre")
    private Short idTipoSangre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cveTipoSangre")
    private List<DatosMedicos> datosMedicosList;

    public TipoSangre() {
    }

    public TipoSangre(Short idTipoSangre) {
        this.idTipoSangre = idTipoSangre;
    }

    public TipoSangre(Short idTipoSangre, String nombre) {
        this.idTipoSangre = idTipoSangre;
        this.nombre = nombre;
    }

    public Short getIdTipoSangre() {
        return idTipoSangre;
    }

    public void setIdTipoSangre(Short idTipoSangre) {
        this.idTipoSangre = idTipoSangre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<DatosMedicos> getDatosMedicosList() {
        return datosMedicosList;
    }

    public void setDatosMedicosList(List<DatosMedicos> datosMedicosList) {
        this.datosMedicosList = datosMedicosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoSangre != null ? idTipoSangre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoSangre)) {
            return false;
        }
        TipoSangre other = (TipoSangre) object;
        if ((this.idTipoSangre == null && other.idTipoSangre != null) || (this.idTipoSangre != null && !this.idTipoSangre.equals(other.idTipoSangre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre[ idTipoSangre=" + idTipoSangre + " ]";
    }
    
}
