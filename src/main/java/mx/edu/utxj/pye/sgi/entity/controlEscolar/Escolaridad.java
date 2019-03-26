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
@Table(name = "escolaridad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escolaridad.findAll", query = "SELECT e FROM Escolaridad e")
    , @NamedQuery(name = "Escolaridad.findByIdEscolaridad", query = "SELECT e FROM Escolaridad e WHERE e.idEscolaridad = :idEscolaridad")
    , @NamedQuery(name = "Escolaridad.findByDescripcion", query = "SELECT e FROM Escolaridad e WHERE e.descripcion = :descripcion")})
public class Escolaridad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_escolaridad")
    private Short idEscolaridad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escolaridadMadre")
    private List<DatosFamiliares> datosFamiliaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escolaridadPadre")
    private List<DatosFamiliares> datosFamiliaresList1;

    public Escolaridad() {
    }

    public Escolaridad(Short idEscolaridad) {
        this.idEscolaridad = idEscolaridad;
    }

    public Escolaridad(Short idEscolaridad, String descripcion) {
        this.idEscolaridad = idEscolaridad;
        this.descripcion = descripcion;
    }

    public Short getIdEscolaridad() {
        return idEscolaridad;
    }

    public void setIdEscolaridad(Short idEscolaridad) {
        this.idEscolaridad = idEscolaridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<DatosFamiliares> getDatosFamiliaresList() {
        return datosFamiliaresList;
    }

    public void setDatosFamiliaresList(List<DatosFamiliares> datosFamiliaresList) {
        this.datosFamiliaresList = datosFamiliaresList;
    }

    @XmlTransient
    public List<DatosFamiliares> getDatosFamiliaresList1() {
        return datosFamiliaresList1;
    }

    public void setDatosFamiliaresList1(List<DatosFamiliares> datosFamiliaresList1) {
        this.datosFamiliaresList1 = datosFamiliaresList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEscolaridad != null ? idEscolaridad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Escolaridad)) {
            return false;
        }
        Escolaridad other = (Escolaridad) object;
        if ((this.idEscolaridad == null && other.idEscolaridad != null) || (this.idEscolaridad != null && !this.idEscolaridad.equals(other.idEscolaridad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Escolaridad[ idEscolaridad=" + idEscolaridad + " ]";
    }
    
}
