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
 * @author jonny
 */
@Entity
@Table(name = "articulosp", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulosp.findAll", query = "SELECT a FROM Articulosp a")
    , @NamedQuery(name = "Articulosp.findByArticuloId", query = "SELECT a FROM Articulosp a WHERE a.articuloId = :articuloId")
    , @NamedQuery(name = "Articulosp.findByIssnNimpreso", query = "SELECT a FROM Articulosp a WHERE a.issnNimpreso = :issnNimpreso")
    , @NamedQuery(name = "Articulosp.findByIssnElectronico", query = "SELECT a FROM Articulosp a WHERE a.issnElectronico = :issnElectronico")
    , @NamedQuery(name = "Articulosp.findByNombre", query = "SELECT a FROM Articulosp a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Articulosp.findByPais", query = "SELECT a FROM Articulosp a WHERE a.pais = :pais")
    , @NamedQuery(name = "Articulosp.findByTituloArticulo", query = "SELECT a FROM Articulosp a WHERE a.tituloArticulo = :tituloArticulo")
    , @NamedQuery(name = "Articulosp.findByNumRevista", query = "SELECT a FROM Articulosp a WHERE a.numRevista = :numRevista")
    , @NamedQuery(name = "Articulosp.findByVolumenRevista", query = "SELECT a FROM Articulosp a WHERE a.volumenRevista = :volumenRevista")
    , @NamedQuery(name = "Articulosp.findByEnioEdicion", query = "SELECT a FROM Articulosp a WHERE a.enioEdicion = :enioEdicion")
    , @NamedQuery(name = "Articulosp.findByAnioPublicacion", query = "SELECT a FROM Articulosp a WHERE a.anioPublicacion = :anioPublicacion")
    , @NamedQuery(name = "Articulosp.findByPaginaInicio", query = "SELECT a FROM Articulosp a WHERE a.paginaInicio = :paginaInicio")
    , @NamedQuery(name = "Articulosp.findByPaginaFin", query = "SELECT a FROM Articulosp a WHERE a.paginaFin = :paginaFin")
    , @NamedQuery(name = "Articulosp.findByPalabraC1", query = "SELECT a FROM Articulosp a WHERE a.palabraC1 = :palabraC1")
    , @NamedQuery(name = "Articulosp.findByPalabraC2", query = "SELECT a FROM Articulosp a WHERE a.palabraC2 = :palabraC2")
    , @NamedQuery(name = "Articulosp.findByPalabraC3", query = "SELECT a FROM Articulosp a WHERE a.palabraC3 = :palabraC3")
    , @NamedQuery(name = "Articulosp.findByEstatus", query = "SELECT a FROM Articulosp a WHERE a.estatus = :estatus")
    , @NamedQuery(name = "Articulosp.findByEvidencia", query = "SELECT a FROM Articulosp a WHERE a.evidencia = :evidencia")})
public class Articulosp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "articuloId")
    private Integer articuloId;
    @Size(max = 255)
    @Column(name = "issnNimpreso")
    private String issnNimpreso;
    @Size(max = 255)
    @Column(name = "issnElectronico")
    private String issnElectronico;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "pais")
    private String pais;
    @Size(max = 255)
    @Column(name = "tituloArticulo")
    private String tituloArticulo;
    @Column(name = "numRevista")
    private Integer numRevista;
    @Column(name = "volumenRevista")
    private Integer volumenRevista;
    @Column(name = "enioEdicion")
    @Temporal(TemporalType.DATE)
    private Date enioEdicion;
    @Column(name = "anioPublicacion")
    @Temporal(TemporalType.DATE)
    private Date anioPublicacion;
    @Column(name = "paginaInicio")
    private Integer paginaInicio;
    @Column(name = "PaginaFin")
    private Integer paginaFin;
    @Size(max = 255)
    @Column(name = "palabraC1")
    private String palabraC1;
    @Size(max = 255)
    @Column(name = "palabraC2")
    private String palabraC2;
    @Size(max = 255)
    @Column(name = "palabraC3")
    private String palabraC3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 255)
    @Column(name = "evidencia")
    private String evidencia;
    @JoinColumn(name = "clavePersonal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Articulosp() {
    }

    public Articulosp(Integer articuloId) {
        this.articuloId = articuloId;
    }

    public Articulosp(Integer articuloId, String estatus) {
        this.articuloId = articuloId;
        this.estatus = estatus;
    }

    public Integer getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(Integer articuloId) {
        this.articuloId = articuloId;
    }

    public String getIssnNimpreso() {
        return issnNimpreso;
    }

    public void setIssnNimpreso(String issnNimpreso) {
        this.issnNimpreso = issnNimpreso;
    }

    public String getIssnElectronico() {
        return issnElectronico;
    }

    public void setIssnElectronico(String issnElectronico) {
        this.issnElectronico = issnElectronico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTituloArticulo() {
        return tituloArticulo;
    }

    public void setTituloArticulo(String tituloArticulo) {
        this.tituloArticulo = tituloArticulo;
    }

    public Integer getNumRevista() {
        return numRevista;
    }

    public void setNumRevista(Integer numRevista) {
        this.numRevista = numRevista;
    }

    public Integer getVolumenRevista() {
        return volumenRevista;
    }

    public void setVolumenRevista(Integer volumenRevista) {
        this.volumenRevista = volumenRevista;
    }

    public Date getEnioEdicion() {
        return enioEdicion;
    }

    public void setEnioEdicion(Date enioEdicion) {
        this.enioEdicion = enioEdicion;
    }

    public Date getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Date anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public Integer getPaginaInicio() {
        return paginaInicio;
    }

    public void setPaginaInicio(Integer paginaInicio) {
        this.paginaInicio = paginaInicio;
    }

    public Integer getPaginaFin() {
        return paginaFin;
    }

    public void setPaginaFin(Integer paginaFin) {
        this.paginaFin = paginaFin;
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
        hash += (articuloId != null ? articuloId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulosp)) {
            return false;
        }
        Articulosp other = (Articulosp) object;
        if ((this.articuloId == null && other.articuloId != null) || (this.articuloId != null && !this.articuloId.equals(other.articuloId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Articulosp[ articuloId=" + articuloId + " ]";
    }
    
}
