/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "menu_dinamico", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MenuDinamico.findAll", query = "SELECT m FROM MenuDinamico m")
    , @NamedQuery(name = "MenuDinamico.findByModulo", query = "SELECT m FROM MenuDinamico m WHERE m.modulo = :modulo")
    , @NamedQuery(name = "MenuDinamico.findByEncabezado", query = "SELECT m FROM MenuDinamico m WHERE m.encabezado = :encabezado")
    , @NamedQuery(name = "MenuDinamico.findByTituloNivel1", query = "SELECT m FROM MenuDinamico m WHERE m.tituloNivel1 = :tituloNivel1")
    , @NamedQuery(name = "MenuDinamico.findByIconoNivel1", query = "SELECT m FROM MenuDinamico m WHERE m.iconoNivel1 = :iconoNivel1")
    , @NamedQuery(name = "MenuDinamico.findByEnlaceNivel1", query = "SELECT m FROM MenuDinamico m WHERE m.enlaceNivel1 = :enlaceNivel1")
    , @NamedQuery(name = "MenuDinamico.findByTituloNivel2", query = "SELECT m FROM MenuDinamico m WHERE m.tituloNivel2 = :tituloNivel2")
    , @NamedQuery(name = "MenuDinamico.findByEnlaceNivel2", query = "SELECT m FROM MenuDinamico m WHERE m.enlaceNivel2 = :enlaceNivel2")
    , @NamedQuery(name = "MenuDinamico.findByIconoNivel2", query = "SELECT m FROM MenuDinamico m WHERE m.iconoNivel2 = :iconoNivel2")
    , @NamedQuery(name = "MenuDinamico.findByTitulonivel3", query = "SELECT m FROM MenuDinamico m WHERE m.titulonivel3 = :titulonivel3")
    , @NamedQuery(name = "MenuDinamico.findByEnlacenivel3", query = "SELECT m FROM MenuDinamico m WHERE m.enlacenivel3 = :enlacenivel3")
    , @NamedQuery(name = "MenuDinamico.findByIconoNivel3", query = "SELECT m FROM MenuDinamico m WHERE m.iconoNivel3 = :iconoNivel3")
    , @NamedQuery(name = "MenuDinamico.findByTitulonivel4", query = "SELECT m FROM MenuDinamico m WHERE m.titulonivel4 = :titulonivel4")
    , @NamedQuery(name = "MenuDinamico.findByEnlacenivel4", query = "SELECT m FROM MenuDinamico m WHERE m.enlacenivel4 = :enlacenivel4")
    , @NamedQuery(name = "MenuDinamico.findByIconoNivel4", query = "SELECT m FROM MenuDinamico m WHERE m.iconoNivel4 = :iconoNivel4")
    , @NamedQuery(name = "MenuDinamico.findByTitulonivel5", query = "SELECT m FROM MenuDinamico m WHERE m.titulonivel5 = :titulonivel5")
    , @NamedQuery(name = "MenuDinamico.findByEnlacenivel5", query = "SELECT m FROM MenuDinamico m WHERE m.enlacenivel5 = :enlacenivel5")
    , @NamedQuery(name = "MenuDinamico.findByIconoNivel5", query = "SELECT m FROM MenuDinamico m WHERE m.iconoNivel5 = :iconoNivel5")
    , @NamedQuery(name = "MenuDinamico.findByPersonas", query = "SELECT m FROM MenuDinamico m WHERE m.personas = :personas")
    , @NamedQuery(name = "MenuDinamico.findByAreas", query = "SELECT m FROM MenuDinamico m WHERE m.areas = :areas")
    , @NamedQuery(name = "MenuDinamico.findByCategorias", query = "SELECT m FROM MenuDinamico m WHERE m.categorias = :categorias")
    , @NamedQuery(name = "MenuDinamico.findByActividades", query = "SELECT m FROM MenuDinamico m WHERE m.actividades = :actividades")
    , @NamedQuery(name = "MenuDinamico.findByEstatus", query = "SELECT m FROM MenuDinamico m WHERE m.estatus = :estatus")
    , @NamedQuery(name = "MenuDinamico.findByTipoUsuario", query = "SELECT m FROM MenuDinamico m WHERE m.tipoUsuario = :tipoUsuario")
    , @NamedQuery(name = "MenuDinamico.findByActivo", query = "SELECT m FROM MenuDinamico m WHERE m.activo = :activo")
    , @NamedQuery(name = "MenuDinamico.findByTipoenlace", query = "SELECT m FROM MenuDinamico m WHERE m.tipoenlace = :tipoenlace")})
public class MenuDinamico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "modulo")
    private Integer modulo;
    @Size(max = 255)
    @Column(name = "encabezado")
    private String encabezado;
    @Size(max = 50)
    @Column(name = "tituloNivel1")
    private String tituloNivel1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "iconoNivel1")
    private String iconoNivel1;
    @Size(max = 255)
    @Column(name = "enlaceNivel1")
    private String enlaceNivel1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tituloNivel2")
    private String tituloNivel2;
    @Size(max = 255)
    @Column(name = "enlaceNivel2")
    private String enlaceNivel2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "iconoNivel2")
    private String iconoNivel2;
    @Size(max = 1000)
    @Column(name = "titulonivel3")
    private String titulonivel3;
    @Size(max = 1000)
    @Column(name = "enlacenivel3")
    private String enlacenivel3;
    @Size(max = 1000)
    @Column(name = "iconoNivel3")
    private String iconoNivel3;
    @Size(max = 255)
    @Column(name = "titulonivel4")
    private String titulonivel4;
    @Size(max = 255)
    @Column(name = "enlacenivel4")
    private String enlacenivel4;
    @Size(max = 255)
    @Column(name = "iconoNivel4")
    private String iconoNivel4;
    @Size(max = 255)
    @Column(name = "titulonivel5")
    private String titulonivel5;
    @Size(max = 255)
    @Column(name = "enlacenivel5")
    private String enlacenivel5;
    @Size(max = 255)
    @Column(name = "iconoNivel5")
    private String iconoNivel5;
    @Size(max = 250)
    @Column(name = "personas")
    private String personas;
    @Size(max = 255)
    @Column(name = "areas")
    private String areas;
    @Size(max = 255)
    @Column(name = "categorias")
    private String categorias;
    @Size(max = 250)
    @Column(name = "actividades")
    private String actividades;
    @Size(max = 250)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 10)
    @Column(name = "TipoUsuario")
    private String tipoUsuario;
    @Column(name = "activo")
    private Boolean activo;
    @Size(max = 9)
    @Column(name = "Tipoenlace")
    private String tipoenlace;

    public MenuDinamico() {
    }

    public MenuDinamico(Integer modulo) {
        this.modulo = modulo;
    }

    public MenuDinamico(Integer modulo, String iconoNivel1, String tituloNivel2, String iconoNivel2) {
        this.modulo = modulo;
        this.iconoNivel1 = iconoNivel1;
        this.tituloNivel2 = tituloNivel2;
        this.iconoNivel2 = iconoNivel2;
    }

    public Integer getModulo() {
        return modulo;
    }

    public void setModulo(Integer modulo) {
        this.modulo = modulo;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }

    public String getTituloNivel1() {
        return tituloNivel1;
    }

    public void setTituloNivel1(String tituloNivel1) {
        this.tituloNivel1 = tituloNivel1;
    }

    public String getIconoNivel1() {
        return iconoNivel1;
    }

    public void setIconoNivel1(String iconoNivel1) {
        this.iconoNivel1 = iconoNivel1;
    }

    public String getEnlaceNivel1() {
        return enlaceNivel1;
    }

    public void setEnlaceNivel1(String enlaceNivel1) {
        this.enlaceNivel1 = enlaceNivel1;
    }

    public String getTituloNivel2() {
        return tituloNivel2;
    }

    public void setTituloNivel2(String tituloNivel2) {
        this.tituloNivel2 = tituloNivel2;
    }

    public String getEnlaceNivel2() {
        return enlaceNivel2;
    }

    public void setEnlaceNivel2(String enlaceNivel2) {
        this.enlaceNivel2 = enlaceNivel2;
    }

    public String getIconoNivel2() {
        return iconoNivel2;
    }

    public void setIconoNivel2(String iconoNivel2) {
        this.iconoNivel2 = iconoNivel2;
    }

    public String getTitulonivel3() {
        return titulonivel3;
    }

    public void setTitulonivel3(String titulonivel3) {
        this.titulonivel3 = titulonivel3;
    }

    public String getEnlacenivel3() {
        return enlacenivel3;
    }

    public void setEnlacenivel3(String enlacenivel3) {
        this.enlacenivel3 = enlacenivel3;
    }

    public String getIconoNivel3() {
        return iconoNivel3;
    }

    public void setIconoNivel3(String iconoNivel3) {
        this.iconoNivel3 = iconoNivel3;
    }

    public String getTitulonivel4() {
        return titulonivel4;
    }

    public void setTitulonivel4(String titulonivel4) {
        this.titulonivel4 = titulonivel4;
    }

    public String getEnlacenivel4() {
        return enlacenivel4;
    }

    public void setEnlacenivel4(String enlacenivel4) {
        this.enlacenivel4 = enlacenivel4;
    }

    public String getIconoNivel4() {
        return iconoNivel4;
    }

    public void setIconoNivel4(String iconoNivel4) {
        this.iconoNivel4 = iconoNivel4;
    }

    public String getTitulonivel5() {
        return titulonivel5;
    }

    public void setTitulonivel5(String titulonivel5) {
        this.titulonivel5 = titulonivel5;
    }

    public String getEnlacenivel5() {
        return enlacenivel5;
    }

    public void setEnlacenivel5(String enlacenivel5) {
        this.enlacenivel5 = enlacenivel5;
    }

    public String getIconoNivel5() {
        return iconoNivel5;
    }

    public void setIconoNivel5(String iconoNivel5) {
        this.iconoNivel5 = iconoNivel5;
    }

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getTipoenlace() {
        return tipoenlace;
    }

    public void setTipoenlace(String tipoenlace) {
        this.tipoenlace = tipoenlace;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modulo != null ? modulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuDinamico)) {
            return false;
        }
        MenuDinamico other = (MenuDinamico) object;
        if ((this.modulo == null && other.modulo != null) || (this.modulo != null && !this.modulo.equals(other.modulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico[ modulo=" + modulo + " ]";
    }
    
}
