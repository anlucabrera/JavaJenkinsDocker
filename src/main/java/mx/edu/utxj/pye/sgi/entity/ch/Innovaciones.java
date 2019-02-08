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
 * @author jonny
 */
@Entity
@Table(name = "innovaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Innovaciones.findAll", query = "SELECT i FROM Innovaciones i")
    , @NamedQuery(name = "Innovaciones.findByInnovacion", query = "SELECT i FROM Innovaciones i WHERE i.innovacion = :innovacion")
    , @NamedQuery(name = "Innovaciones.findByTipoInnovacion", query = "SELECT i FROM Innovaciones i WHERE i.tipoInnovacion = :tipoInnovacion")
    , @NamedQuery(name = "Innovaciones.findByAplicacionInnovacion", query = "SELECT i FROM Innovaciones i WHERE i.aplicacionInnovacion = :aplicacionInnovacion")
    , @NamedQuery(name = "Innovaciones.findByPotencialCobertura", query = "SELECT i FROM Innovaciones i WHERE i.potencialCobertura = :potencialCobertura")
    , @NamedQuery(name = "Innovaciones.findByMecanismoProteccion", query = "SELECT i FROM Innovaciones i WHERE i.mecanismoProteccion = :mecanismoProteccion")
    , @NamedQuery(name = "Innovaciones.findByApoyoRecibidoo", query = "SELECT i FROM Innovaciones i WHERE i.apoyoRecibidoo = :apoyoRecibidoo")
    , @NamedQuery(name = "Innovaciones.findByEstatus", query = "SELECT i FROM Innovaciones i WHERE i.estatus = :estatus")})
public class Innovaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "innovacion")
    private Integer innovacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "tipo_innovacion")
    private String tipoInnovacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "aplicacion_innovacion")
    private String aplicacionInnovacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "potencial_cobertura")
    private String potencialCobertura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "mecanismo_proteccion")
    private String mecanismoProteccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "apoyo_recibidoo")
    private String apoyoRecibidoo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Innovaciones() {
    }

    public Innovaciones(Integer innovacion) {
        this.innovacion = innovacion;
    }

    public Innovaciones(Integer innovacion, String tipoInnovacion, String aplicacionInnovacion, String potencialCobertura, String mecanismoProteccion, String apoyoRecibidoo, String estatus) {
        this.innovacion = innovacion;
        this.tipoInnovacion = tipoInnovacion;
        this.aplicacionInnovacion = aplicacionInnovacion;
        this.potencialCobertura = potencialCobertura;
        this.mecanismoProteccion = mecanismoProteccion;
        this.apoyoRecibidoo = apoyoRecibidoo;
        this.estatus = estatus;
    }

    public Integer getInnovacion() {
        return innovacion;
    }

    public void setInnovacion(Integer innovacion) {
        this.innovacion = innovacion;
    }

    public String getTipoInnovacion() {
        return tipoInnovacion;
    }

    public void setTipoInnovacion(String tipoInnovacion) {
        this.tipoInnovacion = tipoInnovacion;
    }

    public String getAplicacionInnovacion() {
        return aplicacionInnovacion;
    }

    public void setAplicacionInnovacion(String aplicacionInnovacion) {
        this.aplicacionInnovacion = aplicacionInnovacion;
    }

    public String getPotencialCobertura() {
        return potencialCobertura;
    }

    public void setPotencialCobertura(String potencialCobertura) {
        this.potencialCobertura = potencialCobertura;
    }

    public String getMecanismoProteccion() {
        return mecanismoProteccion;
    }

    public void setMecanismoProteccion(String mecanismoProteccion) {
        this.mecanismoProteccion = mecanismoProteccion;
    }

    public String getApoyoRecibidoo() {
        return apoyoRecibidoo;
    }

    public void setApoyoRecibidoo(String apoyoRecibidoo) {
        this.apoyoRecibidoo = apoyoRecibidoo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (innovacion != null ? innovacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Innovaciones)) {
            return false;
        }
        Innovaciones other = (Innovaciones) object;
        if ((this.innovacion == null && other.innovacion != null) || (this.innovacion != null && !this.innovacion.equals(other.innovacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Innovaciones[ innovacion=" + innovacion + " ]";
    }
    
}
