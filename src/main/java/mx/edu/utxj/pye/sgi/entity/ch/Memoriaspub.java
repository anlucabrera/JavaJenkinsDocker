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
@Table(name = "memoriaspub", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Memoriaspub.findAll", query = "SELECT m FROM Memoriaspub m")
    , @NamedQuery(name = "Memoriaspub.findByMemoriaID", query = "SELECT m FROM Memoriaspub m WHERE m.memoriaID = :memoriaID")
    , @NamedQuery(name = "Memoriaspub.findByTituloDeMemoria", query = "SELECT m FROM Memoriaspub m WHERE m.tituloDeMemoria = :tituloDeMemoria")
    , @NamedQuery(name = "Memoriaspub.findByTituloDeObra", query = "SELECT m FROM Memoriaspub m WHERE m.tituloDeObra = :tituloDeObra")
    , @NamedQuery(name = "Memoriaspub.findByTituloDePublicacion", query = "SELECT m FROM Memoriaspub m WHERE m.tituloDePublicacion = :tituloDePublicacion")
    , @NamedQuery(name = "Memoriaspub.findByPaginaInicio", query = "SELECT m FROM Memoriaspub m WHERE m.paginaInicio = :paginaInicio")
    , @NamedQuery(name = "Memoriaspub.findByPaginaFin", query = "SELECT m FROM Memoriaspub m WHERE m.paginaFin = :paginaFin")
    , @NamedQuery(name = "Memoriaspub.findByAnioDePublicacion", query = "SELECT m FROM Memoriaspub m WHERE m.anioDePublicacion = :anioDePublicacion")
    , @NamedQuery(name = "Memoriaspub.findByPais", query = "SELECT m FROM Memoriaspub m WHERE m.pais = :pais")
    , @NamedQuery(name = "Memoriaspub.findByPalabraC1", query = "SELECT m FROM Memoriaspub m WHERE m.palabraC1 = :palabraC1")
    , @NamedQuery(name = "Memoriaspub.findByPalabraC2", query = "SELECT m FROM Memoriaspub m WHERE m.palabraC2 = :palabraC2")
    , @NamedQuery(name = "Memoriaspub.findByPalabraC3", query = "SELECT m FROM Memoriaspub m WHERE m.palabraC3 = :palabraC3")
    , @NamedQuery(name = "Memoriaspub.findByEstatus", query = "SELECT m FROM Memoriaspub m WHERE m.estatus = :estatus")
    , @NamedQuery(name = "Memoriaspub.findByEvidencia", query = "SELECT m FROM Memoriaspub m WHERE m.evidencia = :evidencia")})
public class Memoriaspub implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "memoriaID")
    private Integer memoriaID;
    @Size(max = 255)
    @Column(name = "tituloDeMemoria")
    private String tituloDeMemoria;
    @Size(max = 255)
    @Column(name = "tituloDeObra")
    private String tituloDeObra;
    @Size(max = 255)
    @Column(name = "tituloDePublicacion")
    private String tituloDePublicacion;
    @Column(name = "paginaInicio")
    private Integer paginaInicio;
    @Column(name = "paginaFin")
    private Integer paginaFin;
    @Column(name = "anioDePublicacion")
    @Temporal(TemporalType.DATE)
    private Date anioDePublicacion;
    @Size(max = 255)
    @Column(name = "pais")
    private String pais;
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

    public Memoriaspub() {
    }

    public Memoriaspub(Integer memoriaID) {
        this.memoriaID = memoriaID;
    }

    public Memoriaspub(Integer memoriaID, String estatus) {
        this.memoriaID = memoriaID;
        this.estatus = estatus;
    }

    public Integer getMemoriaID() {
        return memoriaID;
    }

    public void setMemoriaID(Integer memoriaID) {
        this.memoriaID = memoriaID;
    }

    public String getTituloDeMemoria() {
        return tituloDeMemoria;
    }

    public void setTituloDeMemoria(String tituloDeMemoria) {
        this.tituloDeMemoria = tituloDeMemoria;
    }

    public String getTituloDeObra() {
        return tituloDeObra;
    }

    public void setTituloDeObra(String tituloDeObra) {
        this.tituloDeObra = tituloDeObra;
    }

    public String getTituloDePublicacion() {
        return tituloDePublicacion;
    }

    public void setTituloDePublicacion(String tituloDePublicacion) {
        this.tituloDePublicacion = tituloDePublicacion;
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

    public Date getAnioDePublicacion() {
        return anioDePublicacion;
    }

    public void setAnioDePublicacion(Date anioDePublicacion) {
        this.anioDePublicacion = anioDePublicacion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
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
        hash += (memoriaID != null ? memoriaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memoriaspub)) {
            return false;
        }
        Memoriaspub other = (Memoriaspub) object;
        if ((this.memoriaID == null && other.memoriaID != null) || (this.memoriaID != null && !this.memoriaID.equals(other.memoriaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub[ memoriaID=" + memoriaID + " ]";
    }
    
}
