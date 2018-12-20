/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author UTXJ
 */
@Entity
@Table(name = "categoriasespecificasfunciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categoriasespecificasfunciones.findAll", query = "SELECT c FROM Categoriasespecificasfunciones c")
    , @NamedQuery(name = "Categoriasespecificasfunciones.findByCategoriaEspecifica", query = "SELECT c FROM Categoriasespecificasfunciones c WHERE c.categoriaEspecifica = :categoriaEspecifica")
    , @NamedQuery(name = "Categoriasespecificasfunciones.findByNombreCategoria", query = "SELECT c FROM Categoriasespecificasfunciones c WHERE c.nombreCategoria = :nombreCategoria")
    , @NamedQuery(name = "Categoriasespecificasfunciones.findByArea", query = "SELECT c FROM Categoriasespecificasfunciones c WHERE c.area = :area")})
public class Categoriasespecificasfunciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "categoriaEspecifica")
    private Short categoriaEspecifica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombreCategoria")
    private String nombreCategoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private int area;
    @OneToMany(mappedBy = "categoriaEspecifica")
    private List<Personal> personalList;
    @OneToMany(mappedBy = "categoriaEspecifica")
    private List<PersonalBitacora> personalBitacoraList;
    @OneToMany(mappedBy = "categoriaEspesifica")
    private List<Funciones> funcionesList;

    public Categoriasespecificasfunciones() {
    }

    public Categoriasespecificasfunciones(Short categoriaEspecifica) {
        this.categoriaEspecifica = categoriaEspecifica;
    }

    public Categoriasespecificasfunciones(Short categoriaEspecifica, String nombreCategoria, int area) {
        this.categoriaEspecifica = categoriaEspecifica;
        this.nombreCategoria = nombreCategoria;
        this.area = area;
    }

    public Short getCategoriaEspecifica() {
        return categoriaEspecifica;
    }

    public void setCategoriaEspecifica(Short categoriaEspecifica) {
        this.categoriaEspecifica = categoriaEspecifica;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @XmlTransient
    public List<PersonalBitacora> getPersonalBitacoraList() {
        return personalBitacoraList;
    }

    public void setPersonalBitacoraList(List<PersonalBitacora> personalBitacoraList) {
        this.personalBitacoraList = personalBitacoraList;
    }

    @XmlTransient
    public List<Funciones> getFuncionesList() {
        return funcionesList;
    }

    public void setFuncionesList(List<Funciones> funcionesList) {
        this.funcionesList = funcionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoriaEspecifica != null ? categoriaEspecifica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoriasespecificasfunciones)) {
            return false;
        }
        Categoriasespecificasfunciones other = (Categoriasespecificasfunciones) object;
        if ((this.categoriaEspecifica == null && other.categoriaEspecifica != null) || (this.categoriaEspecifica != null && !this.categoriaEspecifica.equals(other.categoriaEspecifica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones[ categoriaEspecifica=" + categoriaEspecifica + " ]";
    }
    
}
