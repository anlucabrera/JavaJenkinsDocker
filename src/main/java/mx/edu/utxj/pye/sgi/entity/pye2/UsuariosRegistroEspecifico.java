/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "usuarios_registro_especifico", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosRegistroEspecifico.findAll", query = "SELECT u FROM UsuariosRegistroEspecifico u")
    , @NamedQuery(name = "UsuariosRegistroEspecifico.findByClavePersonal", query = "SELECT u FROM UsuariosRegistroEspecifico u WHERE u.usuariosRegistroEspecificoPK.clavePersonal = :clavePersonal")
    , @NamedQuery(name = "UsuariosRegistroEspecifico.findByRegistro", query = "SELECT u FROM UsuariosRegistroEspecifico u WHERE u.usuariosRegistroEspecificoPK.registro = :registro")})
public class UsuariosRegistroEspecifico implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuariosRegistroEspecificoPK usuariosRegistroEspecificoPK;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave_personal", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UsuariosRegistros usuariosRegistros;

    public UsuariosRegistroEspecifico() {
    }

    public UsuariosRegistroEspecifico(UsuariosRegistroEspecificoPK usuariosRegistroEspecificoPK) {
        this.usuariosRegistroEspecificoPK = usuariosRegistroEspecificoPK;
    }

    public UsuariosRegistroEspecifico(int clavePersonal, String registro) {
        this.usuariosRegistroEspecificoPK = new UsuariosRegistroEspecificoPK(clavePersonal, registro);
    }

    public UsuariosRegistroEspecificoPK getUsuariosRegistroEspecificoPK() {
        return usuariosRegistroEspecificoPK;
    }

    public void setUsuariosRegistroEspecificoPK(UsuariosRegistroEspecificoPK usuariosRegistroEspecificoPK) {
        this.usuariosRegistroEspecificoPK = usuariosRegistroEspecificoPK;
    }

    public UsuariosRegistros getUsuariosRegistros() {
        return usuariosRegistros;
    }

    public void setUsuariosRegistros(UsuariosRegistros usuariosRegistros) {
        this.usuariosRegistros = usuariosRegistros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuariosRegistroEspecificoPK != null ? usuariosRegistroEspecificoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosRegistroEspecifico)) {
            return false;
        }
        UsuariosRegistroEspecifico other = (UsuariosRegistroEspecifico) object;
        if ((this.usuariosRegistroEspecificoPK == null && other.usuariosRegistroEspecificoPK != null) || (this.usuariosRegistroEspecificoPK != null && !this.usuariosRegistroEspecificoPK.equals(other.usuariosRegistroEspecificoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.UsuariosRegistroEspecifico[ usuariosRegistroEspecificoPK=" + usuariosRegistroEspecificoPK + " ]";
    }
    
}
