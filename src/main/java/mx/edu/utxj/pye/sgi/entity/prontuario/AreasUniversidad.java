/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "areas_universidad", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreasUniversidad.findAll", query = "SELECT a FROM AreasUniversidad a")
    , @NamedQuery(name = "AreasUniversidad.findByArea", query = "SELECT a FROM AreasUniversidad a WHERE a.area = :area")
    , @NamedQuery(name = "AreasUniversidad.findByAreaSuperior", query = "SELECT a FROM AreasUniversidad a WHERE a.areaSuperior = :areaSuperior")
    , @NamedQuery(name = "AreasUniversidad.findByNombre", query = "SELECT a FROM AreasUniversidad a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AreasUniversidad.findBySiglas", query = "SELECT a FROM AreasUniversidad a WHERE a.siglas = :siglas")
    , @NamedQuery(name = "AreasUniversidad.findByResponsable", query = "SELECT a FROM AreasUniversidad a WHERE a.responsable = :responsable")
    , @NamedQuery(name = "AreasUniversidad.findByVigente", query = "SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente")
    , @NamedQuery(name = "AreasUniversidad.findByTienePoa", query = "SELECT a FROM AreasUniversidad a WHERE a.tienePoa = :tienePoa")
    , @NamedQuery(name = "AreasUniversidad.findByCorreoInstitucional", query = "SELECT a FROM AreasUniversidad a WHERE a.correoInstitucional = :correoInstitucional")})
public class AreasUniversidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "area")
    private Short area;
    @Column(name = "area_superior")
    private Short areaSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;
    @Column(name = "responsable")
    private Integer responsable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "vigente")
    private String vigente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tiene_poa")
    private boolean tienePoa;
    @Size(max = 255)
    @Column(name = "correoInstitucional")
    private String correoInstitucional;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areasUniversidad", fetch = FetchType.LAZY)
    private List<SatisfaccionHistorico> satisfaccionHistoricoList;
    @JoinColumn(name = "categoria", referencedColumnName = "categoria")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Categorias categoria;
    @JoinColumn(name = "nivelEducativo", referencedColumnName = "nivel")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramasEducativosNiveles nivelEducativo;

    public AreasUniversidad() {
    }

    public AreasUniversidad(Short area) {
        this.area = area;
    }

    public AreasUniversidad(Short area, String nombre, String siglas, String vigente, boolean tienePoa) {
        this.area = area;
        this.nombre = nombre;
        this.siglas = siglas;
        this.vigente = vigente;
        this.tienePoa = tienePoa;
    }

    public Short getArea() {
        return area;
    }

    public void setArea(Short area) {
        this.area = area;
    }

    public Short getAreaSuperior() {
        return areaSuperior;
    }

    public void setAreaSuperior(Short areaSuperior) {
        this.areaSuperior = areaSuperior;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public Integer getResponsable() {
        return responsable;
    }

    public void setResponsable(Integer responsable) {
        this.responsable = responsable;
    }

    public String getVigente() {
        return vigente;
    }

    public void setVigente(String vigente) {
        this.vigente = vigente;
    }

    public boolean getTienePoa() {
        return tienePoa;
    }

    public void setTienePoa(boolean tienePoa) {
        this.tienePoa = tienePoa;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    @XmlTransient
    public List<SatisfaccionHistorico> getSatisfaccionHistoricoList() {
        return satisfaccionHistoricoList;
    }

    public void setSatisfaccionHistoricoList(List<SatisfaccionHistorico> satisfaccionHistoricoList) {
        this.satisfaccionHistoricoList = satisfaccionHistoricoList;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

    public ProgramasEducativosNiveles getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (area != null ? area.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreasUniversidad)) {
            return false;
        }
        AreasUniversidad other = (AreasUniversidad) object;
        if ((this.area == null && other.area != null) || (this.area != null && !this.area.equals(other.area))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad[ area=" + area + " ]";
    }
    
}
