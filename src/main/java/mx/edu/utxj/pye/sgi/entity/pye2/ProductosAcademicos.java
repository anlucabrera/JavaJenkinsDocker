/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "productos_academicos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductosAcademicos.findAll", query = "SELECT p FROM ProductosAcademicos p")
    , @NamedQuery(name = "ProductosAcademicos.findByRegistro", query = "SELECT p FROM ProductosAcademicos p WHERE p.registro = :registro")
    , @NamedQuery(name = "ProductosAcademicos.findByProductoAcademico", query = "SELECT p FROM ProductosAcademicos p WHERE p.productoAcademico = :productoAcademico")
    , @NamedQuery(name = "ProductosAcademicos.findByAreaAcademica", query = "SELECT p FROM ProductosAcademicos p WHERE p.areaAcademica = :areaAcademica")
    , @NamedQuery(name = "ProductosAcademicos.findByTipo", query = "SELECT p FROM ProductosAcademicos p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "ProductosAcademicos.findByNombreProd", query = "SELECT p FROM ProductosAcademicos p WHERE p.nombreProd = :nombreProd")
    , @NamedQuery(name = "ProductosAcademicos.findByEventrevPresentacion", query = "SELECT p FROM ProductosAcademicos p WHERE p.eventrevPresentacion = :eventrevPresentacion")
    , @NamedQuery(name = "ProductosAcademicos.findByFechaInicio", query = "SELECT p FROM ProductosAcademicos p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ProductosAcademicos.findByFechaFin", query = "SELECT p FROM ProductosAcademicos p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "ProductosAcademicos.findByLugar", query = "SELECT p FROM ProductosAcademicos p WHERE p.lugar = :lugar")
    , @NamedQuery(name = "ProductosAcademicos.findByDescripcion", query = "SELECT p FROM ProductosAcademicos p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "ProductosAcademicos.findByIssn", query = "SELECT p FROM ProductosAcademicos p WHERE p.issn = :issn")
    , @NamedQuery(name = "ProductosAcademicos.findByArbitradoIndexado", query = "SELECT p FROM ProductosAcademicos p WHERE p.arbitradoIndexado = :arbitradoIndexado")})
public class ProductosAcademicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "producto_academico")
    private String productoAcademico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_academica")
    private short areaAcademica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "nombre_prod")
    private String nombreProd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "eventrev_presentacion")
    private String eventrevPresentacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "issn")
    private String issn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "arbitradoIndexado")
    private String arbitradoIndexado;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "pais", referencedColumnName = "idpais")
    @ManyToOne(optional = false)
    private Pais pais;
    @JoinColumns({
        @JoinColumn(name = "estado", referencedColumnName = "claveEstado")
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio")})
    @ManyToOne(optional = false)
    private Municipio municipio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoAcademico")
    private List<ProductosAcademicosPersonal> productosAcademicosPersonalList;

    public ProductosAcademicos() {
    }

    public ProductosAcademicos(Integer registro) {
        this.registro = registro;
    }

    public ProductosAcademicos(Integer registro, String productoAcademico, short areaAcademica, String tipo, String nombreProd, String eventrevPresentacion, Date fechaInicio, Date fechaFin, String lugar, String descripcion, String issn, String arbitradoIndexado) {
        this.registro = registro;
        this.productoAcademico = productoAcademico;
        this.areaAcademica = areaAcademica;
        this.tipo = tipo;
        this.nombreProd = nombreProd;
        this.eventrevPresentacion = eventrevPresentacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.descripcion = descripcion;
        this.issn = issn;
        this.arbitradoIndexado = arbitradoIndexado;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getProductoAcademico() {
        return productoAcademico;
    }

    public void setProductoAcademico(String productoAcademico) {
        this.productoAcademico = productoAcademico;
    }

    public short getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(short areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public String getEventrevPresentacion() {
        return eventrevPresentacion;
    }

    public void setEventrevPresentacion(String eventrevPresentacion) {
        this.eventrevPresentacion = eventrevPresentacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getArbitradoIndexado() {
        return arbitradoIndexado;
    }

    public void setArbitradoIndexado(String arbitradoIndexado) {
        this.arbitradoIndexado = arbitradoIndexado;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    @XmlTransient
    public List<ProductosAcademicosPersonal> getProductosAcademicosPersonalList() {
        return productosAcademicosPersonalList;
    }

    public void setProductosAcademicosPersonalList(List<ProductosAcademicosPersonal> productosAcademicosPersonalList) {
        this.productosAcademicosPersonalList = productosAcademicosPersonalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductosAcademicos)) {
            return false;
        }
        ProductosAcademicos other = (ProductosAcademicos) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos[ registro=" + registro + " ]";
    }
    
}
