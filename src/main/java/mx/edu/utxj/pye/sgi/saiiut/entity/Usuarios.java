/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "usuarios", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findByCvePersona", query = "SELECT u FROM Usuarios u WHERE u.usuariosPK.cvePersona = :cvePersona")
    , @NamedQuery(name = "Usuarios.findByCveUniversidad", query = "SELECT u FROM Usuarios u WHERE u.usuariosPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Usuarios.findByFechaAlta", query = "SELECT u FROM Usuarios u WHERE u.fechaAlta = :fechaAlta")
    , @NamedQuery(name = "Usuarios.findByCambiado", query = "SELECT u FROM Usuarios u WHERE u.cambiado = :cambiado")
    , @NamedQuery(name = "Usuarios.findByActivo", query = "SELECT u FROM Usuarios u WHERE u.activo = :activo")
    , @NamedQuery(name = "Usuarios.findByLoginUsuario", query = "SELECT u FROM Usuarios u WHERE u.loginUsuario = :loginUsuario")
    , @NamedQuery(name = "Usuarios.findByContrasena", query = "SELECT u FROM Usuarios u WHERE u.contrasena = :contrasena")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuariosPK usuariosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "cambiado")
    private Boolean cambiado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "login_usuario")
    private String loginUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "contrasena")
    private String contrasena;
    @JoinColumns({
        @JoinColumn(name = "cve_persona", referencedColumnName = "cve_persona", insertable = false, updatable = false)
        , @JoinColumn(name = "cve_universidad", referencedColumnName = "cve_universidad", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Personas personas;

    public Usuarios() {
    }

    public Usuarios(UsuariosPK usuariosPK) {
        this.usuariosPK = usuariosPK;
    }

    public Usuarios(UsuariosPK usuariosPK, Date fechaAlta, boolean activo, String loginUsuario, String contrasena) {
        this.usuariosPK = usuariosPK;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
        this.loginUsuario = loginUsuario;
        this.contrasena = contrasena;
    }

    public Usuarios(int cvePersona, int cveUniversidad) {
        this.usuariosPK = new UsuariosPK(cvePersona, cveUniversidad);
    }

    public UsuariosPK getUsuariosPK() {
        return usuariosPK;
    }

    public void setUsuariosPK(UsuariosPK usuariosPK) {
        this.usuariosPK = usuariosPK;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Boolean getCambiado() {
        return cambiado;
    }

    public void setCambiado(Boolean cambiado) {
        this.cambiado = cambiado;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Personas getPersonas() {
        return personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuariosPK != null ? usuariosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.usuariosPK == null && other.usuariosPK != null) || (this.usuariosPK != null && !this.usuariosPK.equals(other.usuariosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.ejb.saiiut.entity.Usuarios[ usuariosPK=" + usuariosPK + " ]";
    }
    
}
