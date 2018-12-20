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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "personal_categorias", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonalCategorias.findAll", query = "SELECT p FROM PersonalCategorias p")
    , @NamedQuery(name = "PersonalCategorias.findByCategoria", query = "SELECT p FROM PersonalCategorias p WHERE p.categoria = :categoria")
    , @NamedQuery(name = "PersonalCategorias.findByNombre", query = "SELECT p FROM PersonalCategorias p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PersonalCategorias.findByTipo", query = "SELECT p FROM PersonalCategorias p WHERE p.tipo = :tipo")})
public class PersonalCategorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "categoria")
    private Short categoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "tipo")
    private String tipo;
    @JoinTable(name = "categorias_habilidades", joinColumns = {
        @JoinColumn(name = "categoria", referencedColumnName = "categoria")}, inverseJoinColumns = {
        @JoinColumn(name = "habilidad", referencedColumnName = "habilidad")})
    @ManyToMany
    private List<Habilidades> habilidadesList;
    @JoinTable(name = "eventos_categorias", joinColumns = {
        @JoinColumn(name = "categoria_operativa", referencedColumnName = "categoria")}, inverseJoinColumns = {
        @JoinColumn(name = "evento", referencedColumnName = "evento")})
    @ManyToMany
    private List<Eventos> eventosList;
    @OneToMany(mappedBy = "categoria360")
    private List<Personal> personalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaOperativa")
    private List<Personal> personalList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaOficial")
    private List<Personal> personalList2;
    @OneToMany(mappedBy = "categoria360")
    private List<PersonalBitacora> personalBitacoraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaOperativa")
    private List<PersonalBitacora> personalBitacoraList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaOficial")
    private List<PersonalBitacora> personalBitacoraList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private List<Evaluaciones360Resultados> evaluaciones360ResultadosList;
    @OneToMany(mappedBy = "categoria")
    private List<Permisos> permisosList;
    @OneToMany(mappedBy = "categoriaOperativa")
    private List<Funciones> funcionesList;

    public PersonalCategorias() {
    }

    public PersonalCategorias(Short categoria) {
        this.categoria = categoria;
    }

    public PersonalCategorias(Short categoria, String nombre, String tipo) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Short getCategoria() {
        return categoria;
    }

    public void setCategoria(Short categoria) {
        this.categoria = categoria;
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
    public List<Habilidades> getHabilidadesList() {
        return habilidadesList;
    }

    public void setHabilidadesList(List<Habilidades> habilidadesList) {
        this.habilidadesList = habilidadesList;
    }

    @XmlTransient
    public List<Eventos> getEventosList() {
        return eventosList;
    }

    public void setEventosList(List<Eventos> eventosList) {
        this.eventosList = eventosList;
    }

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @XmlTransient
    public List<Personal> getPersonalList1() {
        return personalList1;
    }

    public void setPersonalList1(List<Personal> personalList1) {
        this.personalList1 = personalList1;
    }

    @XmlTransient
    public List<Personal> getPersonalList2() {
        return personalList2;
    }

    public void setPersonalList2(List<Personal> personalList2) {
        this.personalList2 = personalList2;
    }

    @XmlTransient
    public List<PersonalBitacora> getPersonalBitacoraList() {
        return personalBitacoraList;
    }

    public void setPersonalBitacoraList(List<PersonalBitacora> personalBitacoraList) {
        this.personalBitacoraList = personalBitacoraList;
    }

    @XmlTransient
    public List<PersonalBitacora> getPersonalBitacoraList1() {
        return personalBitacoraList1;
    }

    public void setPersonalBitacoraList1(List<PersonalBitacora> personalBitacoraList1) {
        this.personalBitacoraList1 = personalBitacoraList1;
    }

    @XmlTransient
    public List<PersonalBitacora> getPersonalBitacoraList2() {
        return personalBitacoraList2;
    }

    public void setPersonalBitacoraList2(List<PersonalBitacora> personalBitacoraList2) {
        this.personalBitacoraList2 = personalBitacoraList2;
    }

    @XmlTransient
    public List<Evaluaciones360Resultados> getEvaluaciones360ResultadosList() {
        return evaluaciones360ResultadosList;
    }

    public void setEvaluaciones360ResultadosList(List<Evaluaciones360Resultados> evaluaciones360ResultadosList) {
        this.evaluaciones360ResultadosList = evaluaciones360ResultadosList;
    }

    @XmlTransient
    public List<Permisos> getPermisosList() {
        return permisosList;
    }

    public void setPermisosList(List<Permisos> permisosList) {
        this.permisosList = permisosList;
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
        hash += (categoria != null ? categoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalCategorias)) {
            return false;
        }
        PersonalCategorias other = (PersonalCategorias) object;
        if ((this.categoria == null && other.categoria != null) || (this.categoria != null && !this.categoria.equals(other.categoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias[ categoria=" + categoria + " ]";
    }
    
}
