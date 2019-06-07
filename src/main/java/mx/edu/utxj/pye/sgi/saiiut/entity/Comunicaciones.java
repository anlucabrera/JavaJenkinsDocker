/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "comunicaciones", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comunicaciones.findAll", query = "SELECT c FROM Comunicaciones c")
    , @NamedQuery(name = "Comunicaciones.findByCvePersona", query = "SELECT c FROM Comunicaciones c WHERE c.comunicacionesPK.cvePersona = :cvePersona")
    , @NamedQuery(name = "Comunicaciones.findByCveUniversidad", query = "SELECT c FROM Comunicaciones c WHERE c.comunicacionesPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Comunicaciones.findByConsecutivoComunicacion", query = "SELECT c FROM Comunicaciones c WHERE c.comunicacionesPK.consecutivoComunicacion = :consecutivoComunicacion")
    , @NamedQuery(name = "Comunicaciones.findByCveTipo", query = "SELECT c FROM Comunicaciones c WHERE c.cveTipo = :cveTipo")
    , @NamedQuery(name = "Comunicaciones.findByComentario", query = "SELECT c FROM Comunicaciones c WHERE c.comentario = :comentario")
    , @NamedQuery(name = "Comunicaciones.findByActivo", query = "SELECT c FROM Comunicaciones c WHERE c.activo = :activo")})
public class Comunicaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected ComunicacionesPK comunicacionesPK;
    @Column(name = "cve_tipo")
    private Integer cveTipo;
    @Lob
    @Size(max = 65535)
    @Column(name = "dato")
    private String dato;
    @Size(max = 100)
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "activo")
    private Boolean activo;

    public Comunicaciones() {
    }
    
    public Comunicaciones(ComunicacionesPK comunicacionesPK) {
        this.comunicacionesPK = comunicacionesPK;
    }
    
    public Comunicaciones(Integer cvePersona, int cveUniversidad, int consecutivoComunicacion) {
        this.comunicacionesPK = new ComunicacionesPK(cvePersona, cveUniversidad, consecutivoComunicacion);
        
    }
    
    public ComunicacionesPK getComunicacionesPK() {
        return comunicacionesPK;
    }

    public void setComunicacionesPK(ComunicacionesPK comunicacionesPK) {
        this.comunicacionesPK = comunicacionesPK;
    }
    
    public Integer getCveTipo() {
        return cveTipo;
    }

    public void setCveTipo(Integer cveTipo) {
        this.cveTipo = cveTipo;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.comunicacionesPK);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aspirantes)) {
            return false;
        }
        Comunicaciones other = (Comunicaciones) object;
        if ((this.comunicacionesPK == null && other.comunicacionesPK != null) || (this.comunicacionesPK != null && !this.comunicacionesPK.equals(other.comunicacionesPK))) {
            return false;
        }
        return true;
    }

     @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Comunicaciones[ comunicacionesPK=" + comunicacionesPK + " ]";
    }
}
