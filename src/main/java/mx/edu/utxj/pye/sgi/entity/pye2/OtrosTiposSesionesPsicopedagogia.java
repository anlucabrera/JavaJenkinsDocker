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
@Table(name = "otros_tipos_sesiones_psicopedagogia", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtrosTiposSesionesPsicopedagogia.findAll", query = "SELECT o FROM OtrosTiposSesionesPsicopedagogia o")
    , @NamedQuery(name = "OtrosTiposSesionesPsicopedagogia.findByOtroTipoSesionPsicopedagogia", query = "SELECT o FROM OtrosTiposSesionesPsicopedagogia o WHERE o.otroTipoSesionPsicopedagogia = :otroTipoSesionPsicopedagogia")
    , @NamedQuery(name = "OtrosTiposSesionesPsicopedagogia.findByDescripcion", query = "SELECT o FROM OtrosTiposSesionesPsicopedagogia o WHERE o.descripcion = :descripcion")})
public class OtrosTiposSesionesPsicopedagogia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "otro_tipo_sesion_psicopedagogia")
    private Short otroTipoSesionPsicopedagogia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "otroTipoSesion")
    private List<SesionIndividualMensualPsicopedogia> sesionIndividualMensualPsicopedogiaList;

    public OtrosTiposSesionesPsicopedagogia() {
    }

    public OtrosTiposSesionesPsicopedagogia(Short otroTipoSesionPsicopedagogia) {
        this.otroTipoSesionPsicopedagogia = otroTipoSesionPsicopedagogia;
    }

    public OtrosTiposSesionesPsicopedagogia(Short otroTipoSesionPsicopedagogia, String descripcion) {
        this.otroTipoSesionPsicopedagogia = otroTipoSesionPsicopedagogia;
        this.descripcion = descripcion;
    }

    public Short getOtroTipoSesionPsicopedagogia() {
        return otroTipoSesionPsicopedagogia;
    }

    public void setOtroTipoSesionPsicopedagogia(Short otroTipoSesionPsicopedagogia) {
        this.otroTipoSesionPsicopedagogia = otroTipoSesionPsicopedagogia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<SesionIndividualMensualPsicopedogia> getSesionIndividualMensualPsicopedogiaList() {
        return sesionIndividualMensualPsicopedogiaList;
    }

    public void setSesionIndividualMensualPsicopedogiaList(List<SesionIndividualMensualPsicopedogia> sesionIndividualMensualPsicopedogiaList) {
        this.sesionIndividualMensualPsicopedogiaList = sesionIndividualMensualPsicopedogiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otroTipoSesionPsicopedagogia != null ? otroTipoSesionPsicopedagogia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtrosTiposSesionesPsicopedagogia)) {
            return false;
        }
        OtrosTiposSesionesPsicopedagogia other = (OtrosTiposSesionesPsicopedagogia) object;
        if ((this.otroTipoSesionPsicopedagogia == null && other.otroTipoSesionPsicopedagogia != null) || (this.otroTipoSesionPsicopedagogia != null && !this.otroTipoSesionPsicopedagogia.equals(other.otroTipoSesionPsicopedagogia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.OtrosTiposSesionesPsicopedagogia[ otroTipoSesionPsicopedagogia=" + otroTipoSesionPsicopedagogia + " ]";
    }
    
}
