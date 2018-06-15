/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

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
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "lista_usuario_clave_nomina", catalog = "saiiut", schema = "saiiut")
@XmlRootElement @EqualsAndHashCode(of = "loginUsuario") @ToString @RequiredArgsConstructor
@NamedQueries({
    @NamedQuery(name = "ListaUsuarioClaveNomina.findAll", query = "SELECT l FROM ListaUsuarioClaveNomina l")
    , @NamedQuery(name = "ListaUsuarioClaveNomina.findByCvePersona", query = "SELECT l FROM ListaUsuarioClaveNomina l WHERE l.cvePersona = :cvePersona")
    , @NamedQuery(name = "ListaUsuarioClaveNomina.findByLoginUsuario", query = "SELECT l FROM ListaUsuarioClaveNomina l WHERE l.loginUsuario = :loginUsuario")
    , @NamedQuery(name = "ListaUsuarioClaveNomina.findByNumeroNomina", query = "SELECT l FROM ListaUsuarioClaveNomina l WHERE l.numeroNomina = :numeroNomina")
    , @NamedQuery(name = "ListaUsuarioClaveNomina.findByActivo", query = "SELECT l FROM ListaUsuarioClaveNomina l WHERE l.activo = :activo")})
public class ListaUsuarioClaveNomina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_persona")
    private int cvePersona;
    @Id
    @NonNull
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "login_usuario")
    private String loginUsuario;
    @Size(max = 20)
    @Column(name = "numero_nomina")
    private String numeroNomina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;

    public ListaUsuarioClaveNomina() {
    }

    public int getCvePersona() {
        return cvePersona;
    }

    public void setCvePersona(int cvePersona) {
        this.cvePersona = cvePersona;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getNumeroNomina() {
        return numeroNomina;
    }

    public void setNumeroNomina(String numeroNomina) {
        this.numeroNomina = numeroNomina;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}
