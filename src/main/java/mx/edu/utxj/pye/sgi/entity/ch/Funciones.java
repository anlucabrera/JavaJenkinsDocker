/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "funciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funciones.findAll", query = "SELECT f FROM Funciones f")
    , @NamedQuery(name = "Funciones.findByFuncion", query = "SELECT f FROM Funciones f WHERE f.funcion = :funcion")
    , @NamedQuery(name = "Funciones.findByAreaOperativa", query = "SELECT f FROM Funciones f WHERE f.areaOperativa = :areaOperativa")
    , @NamedQuery(name = "Funciones.findByNombre", query = "SELECT f FROM Funciones f WHERE f.nombre = :nombre")
    , @NamedQuery(name = "Funciones.findByTipo", query = "SELECT f FROM Funciones f WHERE f.tipo = :tipo")})
public class Funciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "funcion")
    private Integer funcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_operativa")
    private short areaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncion")
    private List<Comentariosfunciones> comentariosfuncionesList;
    @JoinColumn(name = "categoria_espesifica", referencedColumnName = "categoriaEspecifica")
    @ManyToOne
    private Categoriasespecificasfunciones categoriaEspesifica;
    @JoinColumn(name = "categoria_operativa", referencedColumnName = "categoria")
    @ManyToOne
    private PersonalCategorias categoriaOperativa;

    public Funciones() {
    }

    public Funciones(Integer funcion) {
        this.funcion = funcion;
    }

    public Funciones(Integer funcion, short areaOperativa, String nombre, String tipo) {
        this.funcion = funcion;
        this.areaOperativa = areaOperativa;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Integer getFuncion() {
        return funcion;
    }

    public void setFuncion(Integer funcion) {
        this.funcion = funcion;
    }

    public short getAreaOperativa() {
        return areaOperativa;
    }

    public void setAreaOperativa(short areaOperativa) {
        this.areaOperativa = areaOperativa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<Comentariosfunciones> getComentariosfuncionesList() {
        return comentariosfuncionesList;
    }

    public void setComentariosfuncionesList(List<Comentariosfunciones> comentariosfuncionesList) {
        this.comentariosfuncionesList = comentariosfuncionesList;
    }

    public Categoriasespecificasfunciones getCategoriaEspesifica() {
        return categoriaEspesifica;
    }

    public void setCategoriaEspesifica(Categoriasespecificasfunciones categoriaEspesifica) {
        this.categoriaEspesifica = categoriaEspesifica;
    }

    public PersonalCategorias getCategoriaOperativa() {
        return categoriaOperativa;
    }

    public void setCategoriaOperativa(PersonalCategorias categoriaOperativa) {
        this.categoriaOperativa = categoriaOperativa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funcion != null ? funcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funciones)) {
            return false;
        }
        Funciones other = (Funciones) object;
        if ((this.funcion == null && other.funcion != null) || (this.funcion != null && !this.funcion.equals(other.funcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Funciones[ funcion=" + funcion + " ]";
    }
    
}
