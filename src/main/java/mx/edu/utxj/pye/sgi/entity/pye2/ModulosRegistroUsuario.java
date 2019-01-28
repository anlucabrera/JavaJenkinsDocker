/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "modulos_registro_usuario", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModulosRegistroUsuario.findAll", query = "SELECT m FROM ModulosRegistroUsuario m")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByInformacionCorrespondiente", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.informacionCorrespondiente = :informacionCorrespondiente")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByClase", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.clase = :clase")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByTamanioFuente", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.tamanioFuente = :tamanioFuente")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByTituloPrincipal", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.tituloPrincipal = :tituloPrincipal")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByTipo", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.tipo = :tipo")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByIcono", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.icono = :icono")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByLink", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.link = :link")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByDescripcionLink", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.descripcionLink = :descripcionLink")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByEstado", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.estado = :estado")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByClaveEje", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.claveEje = :claveEje")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByEje", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.eje = :eje")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByIconoEje", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.iconoEje = :iconoEje")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByLinkEje", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.linkEje = :linkEje")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByArea", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.area = :area")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByUsuario", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.usuario = :usuario")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByRol", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.rol = :rol")
    , @NamedQuery(name = "ModulosRegistroUsuario.findByClavePersonal", query = "SELECT m FROM ModulosRegistroUsuario m WHERE m.clavePersonal = :clavePersonal")})
public class ModulosRegistroUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "informacion_correspondiente")
    private short informacionCorrespondiente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "clase")
    private String clase;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tamanio_fuente")
    private short tamanioFuente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "titulo_principal")
    private String tituloPrincipal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "icono")
    private String icono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "link")
    private String link;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion_link")
    private String descripcionLink;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_eje")
    private int claveEje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "eje")
    private String eje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "icono_eje")
    private String iconoEje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "link_eje")
    private String linkEje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private int usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "rol")
    private String rol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_personal")
    private int clavePersonal;

    public ModulosRegistroUsuario() {
    }

    public short getInformacionCorrespondiente() {
        return informacionCorrespondiente;
    }

    public void setInformacionCorrespondiente(short informacionCorrespondiente) {
        this.informacionCorrespondiente = informacionCorrespondiente;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public short getTamanioFuente() {
        return tamanioFuente;
    }

    public void setTamanioFuente(short tamanioFuente) {
        this.tamanioFuente = tamanioFuente;
    }

    public String getTituloPrincipal() {
        return tituloPrincipal;
    }

    public void setTituloPrincipal(String tituloPrincipal) {
        this.tituloPrincipal = tituloPrincipal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescripcionLink() {
        return descripcionLink;
    }

    public void setDescripcionLink(String descripcionLink) {
        this.descripcionLink = descripcionLink;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getClaveEje() {
        return claveEje;
    }

    public void setClaveEje(int claveEje) {
        this.claveEje = claveEje;
    }

    public String getEje() {
        return eje;
    }

    public void setEje(String eje) {
        this.eje = eje;
    }

    public String getIconoEje() {
        return iconoEje;
    }

    public void setIconoEje(String iconoEje) {
        this.iconoEje = iconoEje;
    }

    public String getLinkEje() {
        return linkEje;
    }

    public void setLinkEje(String linkEje) {
        this.linkEje = linkEje;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(int clavePersonal) {
        this.clavePersonal = clavePersonal;
    }
    
}
