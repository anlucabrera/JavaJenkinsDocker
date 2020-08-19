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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "permisosadminstracion", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisosadminstracion.findAll", query = "SELECT p FROM Permisosadminstracion p")
    , @NamedQuery(name = "Permisosadminstracion.findByAdministrador", query = "SELECT p FROM Permisosadminstracion p WHERE p.administrador = :administrador")
    , @NamedQuery(name = "Permisosadminstracion.findByModulo", query = "SELECT p FROM Permisosadminstracion p WHERE p.modulo = :modulo")
    , @NamedQuery(name = "Permisosadminstracion.findByRuta", query = "SELECT p FROM Permisosadminstracion p WHERE p.ruta = :ruta")
    , @NamedQuery(name = "Permisosadminstracion.findByIcono", query = "SELECT p FROM Permisosadminstracion p WHERE p.icono = :icono")
    , @NamedQuery(name = "Permisosadminstracion.findByTitulo", query = "SELECT p FROM Permisosadminstracion p WHERE p.titulo = :titulo")})
public class Permisosadminstracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "administrador")
    private Integer administrador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "modulo")
    private String modulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "icono")
    private String icono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "titulo")
    private String titulo;
    @JoinColumn(name = "persona", referencedColumnName = "clave")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Personal persona;

    public Permisosadminstracion() {
    }

    public Permisosadminstracion(Integer administrador) {
        this.administrador = administrador;
    }

    public Permisosadminstracion(Integer administrador, String modulo, String ruta, String icono, String titulo) {
        this.administrador = administrador;
        this.modulo = modulo;
        this.ruta = ruta;
        this.icono = icono;
        this.titulo = titulo;
    }

    public Integer getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Integer administrador) {
        this.administrador = administrador;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Personal getPersona() {
        return persona;
    }

    public void setPersona(Personal persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (administrador != null ? administrador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisosadminstracion)) {
            return false;
        }
        Permisosadminstracion other = (Permisosadminstracion) object;
        if ((this.administrador == null && other.administrador != null) || (this.administrador != null && !this.administrador.equals(other.administrador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Permisosadminstracion[ administrador=" + administrador + " ]";
    }
    
}
