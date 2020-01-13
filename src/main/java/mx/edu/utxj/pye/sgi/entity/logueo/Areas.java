/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.logueo;

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
@Table(name = "areas", catalog = "logueo", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Areas.findAll", query = "SELECT a FROM Areas a")
    , @NamedQuery(name = "Areas.findByIdareas", query = "SELECT a FROM Areas a WHERE a.idareas = :idareas")
    , @NamedQuery(name = "Areas.findByClave", query = "SELECT a FROM Areas a WHERE a.clave = :clave")
    , @NamedQuery(name = "Areas.findByNombre", query = "SELECT a FROM Areas a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Areas.findByTipo", query = "SELECT a FROM Areas a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "Areas.findByResponsable", query = "SELECT a FROM Areas a WHERE a.responsable = :responsable")
    , @NamedQuery(name = "Areas.findByAbreveviacion", query = "SELECT a FROM Areas a WHERE a.abreveviacion = :abreveviacion")
    , @NamedQuery(name = "Areas.findByIdentificador", query = "SELECT a FROM Areas a WHERE a.identificador = :identificador")})
public class Areas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idareas")
    private Integer idareas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "responsable")
    private String responsable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "abreveviacion")
    private String abreveviacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "identificador")
    private int identificador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveArea")
    private List<Pe> peList;

    public Areas() {
    }

    public Areas(Integer idareas) {
        this.idareas = idareas;
    }

    public Areas(Integer idareas, int clave, String nombre, String tipo, String responsable, String abreveviacion, int identificador) {
        this.idareas = idareas;
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
        this.responsable = responsable;
        this.abreveviacion = abreveviacion;
        this.identificador = identificador;
    }

    public Integer getIdareas() {
        return idareas;
    }

    public void setIdareas(Integer idareas) {
        this.idareas = idareas;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getAbreveviacion() {
        return abreveviacion;
    }

    public void setAbreveviacion(String abreveviacion) {
        this.abreveviacion = abreveviacion;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    @XmlTransient
    public List<Pe> getPeList() {
        return peList;
    }

    public void setPeList(List<Pe> peList) {
        this.peList = peList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idareas != null ? idareas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Areas)) {
            return false;
        }
        Areas other = (Areas) object;
        if ((this.idareas == null && other.idareas != null) || (this.idareas != null && !this.idareas.equals(other.idareas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Areas{" + "idareas=" + idareas + ", clave=" + clave + ", nombre=" + nombre + ", tipo=" + tipo + ", responsable=" + responsable + ", abreveviacion=" + abreveviacion + ", identificador=" + identificador + '}';
    }
    
}
