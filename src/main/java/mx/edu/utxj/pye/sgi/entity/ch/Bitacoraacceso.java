/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Finanzas1
 */
@Entity
@Table(name = "bitacoraacceso", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bitacoraacceso.findAll", query = "SELECT b FROM Bitacoraacceso b")
    , @NamedQuery(name = "Bitacoraacceso.findByBitacora", query = "SELECT b FROM Bitacoraacceso b WHERE b.bitacora = :bitacora")
    , @NamedQuery(name = "Bitacoraacceso.findByClavePersonal", query = "SELECT b FROM Bitacoraacceso b WHERE b.clavePersonal = :clavePersonal")
    , @NamedQuery(name = "Bitacoraacceso.findByTabla", query = "SELECT b FROM Bitacoraacceso b WHERE b.tabla = :tabla")
    , @NamedQuery(name = "Bitacoraacceso.findByAccion", query = "SELECT b FROM Bitacoraacceso b WHERE b.accion = :accion")
    , @NamedQuery(name = "Bitacoraacceso.findByNumeroRegistro", query = "SELECT b FROM Bitacoraacceso b WHERE b.numeroRegistro = :numeroRegistro")
    , @NamedQuery(name = "Bitacoraacceso.findByFechaHora", query = "SELECT b FROM Bitacoraacceso b WHERE b.fechaHora = :fechaHora")})
public class Bitacoraacceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bitacora")
    private Integer bitacora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clavePersonal")
    private int clavePersonal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "tabla")
    private String tabla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "accion")
    private String accion;
    @Size(max = 255)
    @Column(name = "numeroRegistro")
    private String numeroRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;

    public Bitacoraacceso() {
    }

    public Bitacoraacceso(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public Bitacoraacceso(Integer bitacora, int clavePersonal, String tabla, String accion, Date fechaHora) {
        this.bitacora = bitacora;
        this.clavePersonal = clavePersonal;
        this.tabla = tabla;
        this.accion = accion;
        this.fechaHora = fechaHora;
    }

    public Integer getBitacora() {
        return bitacora;
    }

    public void setBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public int getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(int clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bitacora != null ? bitacora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bitacoraacceso)) {
            return false;
        }
        Bitacoraacceso other = (Bitacoraacceso) object;
        if ((this.bitacora == null && other.bitacora != null) || (this.bitacora != null && !this.bitacora.equals(other.bitacora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso[ bitacora=" + bitacora + " ]";
    }
    
}
