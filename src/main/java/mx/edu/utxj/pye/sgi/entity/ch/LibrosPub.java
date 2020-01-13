/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "libros_pub", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LibrosPub.findAll", query = "SELECT l FROM LibrosPub l")
    , @NamedQuery(name = "LibrosPub.findByLibrosp", query = "SELECT l FROM LibrosPub l WHERE l.librosp = :librosp")
    , @NamedQuery(name = "LibrosPub.findByIsbn", query = "SELECT l FROM LibrosPub l WHERE l.isbn = :isbn")
    , @NamedQuery(name = "LibrosPub.findByIsbnTraducido", query = "SELECT l FROM LibrosPub l WHERE l.isbnTraducido = :isbnTraducido")
    , @NamedQuery(name = "LibrosPub.findByTitulo", query = "SELECT l FROM LibrosPub l WHERE l.titulo = :titulo")
    , @NamedQuery(name = "LibrosPub.findByTituloTraducido", query = "SELECT l FROM LibrosPub l WHERE l.tituloTraducido = :tituloTraducido")
    , @NamedQuery(name = "LibrosPub.findByPasi", query = "SELECT l FROM LibrosPub l WHERE l.pasi = :pasi")
    , @NamedQuery(name = "LibrosPub.findByIdioma", query = "SELECT l FROM LibrosPub l WHERE l.idioma = :idioma")
    , @NamedQuery(name = "LibrosPub.findByAnioPub", query = "SELECT l FROM LibrosPub l WHERE l.anioPub = :anioPub")
    , @NamedQuery(name = "LibrosPub.findByVolumen", query = "SELECT l FROM LibrosPub l WHERE l.volumen = :volumen")
    , @NamedQuery(name = "LibrosPub.findByTomo", query = "SELECT l FROM LibrosPub l WHERE l.tomo = :tomo")
    , @NamedQuery(name = "LibrosPub.findByTiraje", query = "SELECT l FROM LibrosPub l WHERE l.tiraje = :tiraje")
    , @NamedQuery(name = "LibrosPub.findByNumPag", query = "SELECT l FROM LibrosPub l WHERE l.numPag = :numPag")
    , @NamedQuery(name = "LibrosPub.findByPalabraC1", query = "SELECT l FROM LibrosPub l WHERE l.palabraC1 = :palabraC1")
    , @NamedQuery(name = "LibrosPub.findByPalabraC2", query = "SELECT l FROM LibrosPub l WHERE l.palabraC2 = :palabraC2")
    , @NamedQuery(name = "LibrosPub.findByPalabraC3", query = "SELECT l FROM LibrosPub l WHERE l.palabraC3 = :palabraC3")
    , @NamedQuery(name = "LibrosPub.findByEditorial", query = "SELECT l FROM LibrosPub l WHERE l.editorial = :editorial")
    , @NamedQuery(name = "LibrosPub.findByNumEdicion", query = "SELECT l FROM LibrosPub l WHERE l.numEdicion = :numEdicion")
    , @NamedQuery(name = "LibrosPub.findByLugarEdicion", query = "SELECT l FROM LibrosPub l WHERE l.lugarEdicion = :lugarEdicion")
    , @NamedQuery(name = "LibrosPub.findByAnioEdicion", query = "SELECT l FROM LibrosPub l WHERE l.anioEdicion = :anioEdicion")
    , @NamedQuery(name = "LibrosPub.findByIdiomaTraducido", query = "SELECT l FROM LibrosPub l WHERE l.idiomaTraducido = :idiomaTraducido")
    , @NamedQuery(name = "LibrosPub.findByApoyoUni", query = "SELECT l FROM LibrosPub l WHERE l.apoyoUni = :apoyoUni")
    , @NamedQuery(name = "LibrosPub.findByEstatus", query = "SELECT l FROM LibrosPub l WHERE l.estatus = :estatus")
    , @NamedQuery(name = "LibrosPub.findByEvidencia", query = "SELECT l FROM LibrosPub l WHERE l.evidencia = :evidencia")})
public class LibrosPub implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "librosp")
    private Integer librosp;
    @Size(max = 255)
    @Column(name = "isbn")
    private String isbn;
    @Size(max = 255)
    @Column(name = "isbnTraducido")
    private String isbnTraducido;
    @Size(max = 255)
    @Column(name = "titulo")
    private String titulo;
    @Size(max = 255)
    @Column(name = "tituloTraducido")
    private String tituloTraducido;
    @Size(max = 255)
    @Column(name = "pasi")
    private String pasi;
    @Size(max = 255)
    @Column(name = "idioma")
    private String idioma;
    @Column(name = "anioPub")
    @Temporal(TemporalType.DATE)
    private Date anioPub;
    @Column(name = "volumen")
    private Integer volumen;
    @Column(name = "tomo")
    private Integer tomo;
    @Size(max = 255)
    @Column(name = "tiraje")
    private String tiraje;
    @Column(name = "numPag")
    private Integer numPag;
    @Size(max = 255)
    @Column(name = "palabraC1")
    private String palabraC1;
    @Size(max = 255)
    @Column(name = "palabraC2")
    private String palabraC2;
    @Size(max = 255)
    @Column(name = "palabraC3")
    private String palabraC3;
    @Size(max = 255)
    @Column(name = "editorial")
    private String editorial;
    @Size(max = 255)
    @Column(name = "numEdicion")
    private String numEdicion;
    @Size(max = 255)
    @Column(name = "lugarEdicion")
    private String lugarEdicion;
    @Column(name = "anioEdicion")
    @Temporal(TemporalType.DATE)
    private Date anioEdicion;
    @Size(max = 255)
    @Column(name = "idiomaTraducido")
    private String idiomaTraducido;
    @Size(max = 255)
    @Column(name = "apoyoUni")
    private String apoyoUni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 255)
    @Column(name = "evidencia")
    private String evidencia;
    @JoinColumn(name = "clavePersonal", referencedColumnName = "clave")
    @ManyToOne
    private Personal clavePersonal;

    public LibrosPub() {
    }

    public LibrosPub(Integer librosp) {
        this.librosp = librosp;
    }

    public LibrosPub(Integer librosp, String estatus) {
        this.librosp = librosp;
        this.estatus = estatus;
    }

    public Integer getLibrosp() {
        return librosp;
    }

    public void setLibrosp(Integer librosp) {
        this.librosp = librosp;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbnTraducido() {
        return isbnTraducido;
    }

    public void setIsbnTraducido(String isbnTraducido) {
        this.isbnTraducido = isbnTraducido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloTraducido() {
        return tituloTraducido;
    }

    public void setTituloTraducido(String tituloTraducido) {
        this.tituloTraducido = tituloTraducido;
    }

    public String getPasi() {
        return pasi;
    }

    public void setPasi(String pasi) {
        this.pasi = pasi;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Date getAnioPub() {
        return anioPub;
    }

    public void setAnioPub(Date anioPub) {
        this.anioPub = anioPub;
    }

    public Integer getVolumen() {
        return volumen;
    }

    public void setVolumen(Integer volumen) {
        this.volumen = volumen;
    }

    public Integer getTomo() {
        return tomo;
    }

    public void setTomo(Integer tomo) {
        this.tomo = tomo;
    }

    public String getTiraje() {
        return tiraje;
    }

    public void setTiraje(String tiraje) {
        this.tiraje = tiraje;
    }

    public Integer getNumPag() {
        return numPag;
    }

    public void setNumPag(Integer numPag) {
        this.numPag = numPag;
    }

    public String getPalabraC1() {
        return palabraC1;
    }

    public void setPalabraC1(String palabraC1) {
        this.palabraC1 = palabraC1;
    }

    public String getPalabraC2() {
        return palabraC2;
    }

    public void setPalabraC2(String palabraC2) {
        this.palabraC2 = palabraC2;
    }

    public String getPalabraC3() {
        return palabraC3;
    }

    public void setPalabraC3(String palabraC3) {
        this.palabraC3 = palabraC3;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getNumEdicion() {
        return numEdicion;
    }

    public void setNumEdicion(String numEdicion) {
        this.numEdicion = numEdicion;
    }

    public String getLugarEdicion() {
        return lugarEdicion;
    }

    public void setLugarEdicion(String lugarEdicion) {
        this.lugarEdicion = lugarEdicion;
    }

    public Date getAnioEdicion() {
        return anioEdicion;
    }

    public void setAnioEdicion(Date anioEdicion) {
        this.anioEdicion = anioEdicion;
    }

    public String getIdiomaTraducido() {
        return idiomaTraducido;
    }

    public void setIdiomaTraducido(String idiomaTraducido) {
        this.idiomaTraducido = idiomaTraducido;
    }

    public String getApoyoUni() {
        return apoyoUni;
    }

    public void setApoyoUni(String apoyoUni) {
        this.apoyoUni = apoyoUni;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (librosp != null ? librosp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LibrosPub)) {
            return false;
        }
        LibrosPub other = (LibrosPub) object;
        if ((this.librosp == null && other.librosp != null) || (this.librosp != null && !this.librosp.equals(other.librosp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.LibrosPub[ librosp=" + librosp + " ]";
    }
    
}
