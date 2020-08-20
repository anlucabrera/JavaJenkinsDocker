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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "municipio", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Municipio.findAll", query = "SELECT m FROM Municipio m")
    , @NamedQuery(name = "Municipio.findByClaveEstado", query = "SELECT m FROM Municipio m WHERE m.municipioPK.claveEstado = :claveEstado")
    , @NamedQuery(name = "Municipio.findByClaveMunicipio", query = "SELECT m FROM Municipio m WHERE m.municipioPK.claveMunicipio = :claveMunicipio")
    , @NamedQuery(name = "Municipio.findByNombre", query = "SELECT m FROM Municipio m WHERE m.nombre = :nombre")})
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MunicipioPK municipioPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio", fetch = FetchType.LAZY)
    private List<ServiciosTecnologicosParticipantes> serviciosTecnologicosParticipantesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio", fetch = FetchType.LAZY)
    private List<ProductosAcademicos> productosAcademicosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio1", fetch = FetchType.LAZY)
    private List<Asentamiento> asentamientoList;
    @JoinColumn(name = "claveEstado", referencedColumnName = "idestado", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estado estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio", fetch = FetchType.LAZY)
    private List<Localidad> localidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "municipio1", fetch = FetchType.LAZY)
    private List<Codigopostal> codigopostalList;

    public Municipio() {
    }

    public Municipio(MunicipioPK municipioPK) {
        this.municipioPK = municipioPK;
    }

    public Municipio(MunicipioPK municipioPK, String nombre) {
        this.municipioPK = municipioPK;
        this.nombre = nombre;
    }

    public Municipio(int claveEstado, int claveMunicipio) {
        this.municipioPK = new MunicipioPK(claveEstado, claveMunicipio);
    }

    public MunicipioPK getMunicipioPK() {
        return municipioPK;
    }

    public void setMunicipioPK(MunicipioPK municipioPK) {
        this.municipioPK = municipioPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ServiciosTecnologicosParticipantes> getServiciosTecnologicosParticipantesList() {
        return serviciosTecnologicosParticipantesList;
    }

    public void setServiciosTecnologicosParticipantesList(List<ServiciosTecnologicosParticipantes> serviciosTecnologicosParticipantesList) {
        this.serviciosTecnologicosParticipantesList = serviciosTecnologicosParticipantesList;
    }

    @XmlTransient
    public List<ProductosAcademicos> getProductosAcademicosList() {
        return productosAcademicosList;
    }

    public void setProductosAcademicosList(List<ProductosAcademicos> productosAcademicosList) {
        this.productosAcademicosList = productosAcademicosList;
    }

    @XmlTransient
    public List<Asentamiento> getAsentamientoList() {
        return asentamientoList;
    }

    public void setAsentamientoList(List<Asentamiento> asentamientoList) {
        this.asentamientoList = asentamientoList;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Localidad> getLocalidadList() {
        return localidadList;
    }

    public void setLocalidadList(List<Localidad> localidadList) {
        this.localidadList = localidadList;
    }

    @XmlTransient
    public List<Codigopostal> getCodigopostalList() {
        return codigopostalList;
    }

    public void setCodigopostalList(List<Codigopostal> codigopostalList) {
        this.codigopostalList = codigopostalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (municipioPK != null ? municipioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Municipio)) {
            return false;
        }
        Municipio other = (Municipio) object;
        if ((this.municipioPK == null && other.municipioPK != null) || (this.municipioPK != null && !this.municipioPK.equals(other.municipioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Municipio[ municipioPK=" + municipioPK + " ]";
    }
    
}
