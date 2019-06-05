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
 * @author UTXJ
 */
@Entity
@Table(name = "sistema", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sistema.findAll", query = "SELECT s FROM Sistema s")
    , @NamedQuery(name = "Sistema.findByIdSistema", query = "SELECT s FROM Sistema s WHERE s.idSistema = :idSistema")
    , @NamedQuery(name = "Sistema.findByNombre", query = "SELECT s FROM Sistema s WHERE s.nombre = :nombre")})
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sistema")
    private Short idSistema;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sistemaPrimeraOpcion")
    private List<DatosAcademicos> datosAcademicosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sistemaSegundaOpcion")
    private List<DatosAcademicos> datosAcademicosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSistema")
    private List<Grupo> grupoList;

    public Sistema() {
    }

    public Sistema(Short idSistema) {
        this.idSistema = idSistema;
    }

    public Sistema(Short idSistema, String nombre) {
        this.idSistema = idSistema;
        this.nombre = nombre;
    }

    public Short getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Short idSistema) {
        this.idSistema = idSistema;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<DatosAcademicos> getDatosAcademicosList() {
        return datosAcademicosList;
    }

    public void setDatosAcademicosList(List<DatosAcademicos> datosAcademicosList) {
        this.datosAcademicosList = datosAcademicosList;
    }

    @XmlTransient
    public List<DatosAcademicos> getDatosAcademicosList1() {
        return datosAcademicosList1;
    }

    public void setDatosAcademicosList1(List<DatosAcademicos> datosAcademicosList1) {
        this.datosAcademicosList1 = datosAcademicosList1;
    }

    @XmlTransient
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSistema != null ? idSistema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sistema)) {
            return false;
        }
        Sistema other = (Sistema) object;
        if ((this.idSistema == null && other.idSistema != null) || (this.idSistema != null && !this.idSistema.equals(other.idSistema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Sistema[ idSistema=" + idSistema + " ]";
    }
    
}
