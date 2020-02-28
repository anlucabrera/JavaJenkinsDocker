/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "lengua_indigena", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LenguaIndigena.findAll", query = "SELECT l FROM LenguaIndigena l")
    , @NamedQuery(name = "LenguaIndigena.findByIdLenguaIndigena", query = "SELECT l FROM LenguaIndigena l WHERE l.idLenguaIndigena = :idLenguaIndigena")
    , @NamedQuery(name = "LenguaIndigena.findByNombre", query = "SELECT l FROM LenguaIndigena l WHERE l.nombre = :nombre")})
public class LenguaIndigena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_lengua_indigena")
    private Short idLenguaIndigena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "r2tipoLenguaIndigena")
    private List<EncuestaAspirante> encuestaAspiranteList;

    public LenguaIndigena() {
    }

    public LenguaIndigena(Short idLenguaIndigena) {
        this.idLenguaIndigena = idLenguaIndigena;
    }

    public LenguaIndigena(Short idLenguaIndigena, String nombre) {
        this.idLenguaIndigena = idLenguaIndigena;
        this.nombre = nombre;
    }

    public Short getIdLenguaIndigena() {
        return idLenguaIndigena;
    }

    public void setIdLenguaIndigena(Short idLenguaIndigena) {
        this.idLenguaIndigena = idLenguaIndigena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<EncuestaAspirante> getEncuestaAspiranteList() {
        return encuestaAspiranteList;
    }

    public void setEncuestaAspiranteList(List<EncuestaAspirante> encuestaAspiranteList) {
        this.encuestaAspiranteList = encuestaAspiranteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLenguaIndigena != null ? idLenguaIndigena.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LenguaIndigena)) {
            return false;
        }
        LenguaIndigena other = (LenguaIndigena) object;
        if ((this.idLenguaIndigena == null && other.idLenguaIndigena != null) || (this.idLenguaIndigena != null && !this.idLenguaIndigena.equals(other.idLenguaIndigena))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena[ idLenguaIndigena=" + idLenguaIndigena + " ]";
    }
    
}
