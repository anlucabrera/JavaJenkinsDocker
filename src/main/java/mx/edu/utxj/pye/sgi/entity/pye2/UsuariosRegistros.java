/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
@Table(name = "usuarios_registros", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosRegistros.findAll", query = "SELECT u FROM UsuariosRegistros u")
    , @NamedQuery(name = "UsuariosRegistros.findByUsuario", query = "SELECT u FROM UsuariosRegistros u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "UsuariosRegistros.findByArea", query = "SELECT u FROM UsuariosRegistros u WHERE u.area = :area")
    , @NamedQuery(name = "UsuariosRegistros.findByRol", query = "SELECT u FROM UsuariosRegistros u WHERE u.rol = :rol")
    , @NamedQuery(name = "UsuariosRegistros.findByClavePersonal", query = "SELECT u FROM UsuariosRegistros u WHERE u.clavePersonal = :clavePersonal")})
public class UsuariosRegistros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usuario")
    private Integer usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "rol")
    private String rol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_personal")
    private int clavePersonal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuariosRegistros")
    private List<UsuariosRegistroEspecifico> usuariosRegistroEspecificoList;

    public UsuariosRegistros() {
    }

    public UsuariosRegistros(Integer usuario) {
        this.usuario = usuario;
    }

    public UsuariosRegistros(Integer usuario, short area, String rol, int clavePersonal) {
        this.usuario = usuario;
        this.area = area;
        this.rol = rol;
        this.clavePersonal = clavePersonal;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
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

    @XmlTransient
    public List<UsuariosRegistroEspecifico> getUsuariosRegistroEspecificoList() {
        return usuariosRegistroEspecificoList;
    }

    public void setUsuariosRegistroEspecificoList(List<UsuariosRegistroEspecifico> usuariosRegistroEspecificoList) {
        this.usuariosRegistroEspecificoList = usuariosRegistroEspecificoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosRegistros)) {
            return false;
        }
        UsuariosRegistros other = (UsuariosRegistros) object;
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.UsuariosRegistros[ usuario=" + usuario + " ]";
    }
    
}
