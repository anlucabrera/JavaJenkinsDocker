/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "cuerpacad_lineas_bitacora", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadLineasBitacora.findAll", query = "SELECT c FROM CuerpacadLineasBitacora c")
    , @NamedQuery(name = "CuerpacadLineasBitacora.findByBitacora", query = "SELECT c FROM CuerpacadLineasBitacora c WHERE c.bitacora = :bitacora")
    , @NamedQuery(name = "CuerpacadLineasBitacora.findByFechaCambio", query = "SELECT c FROM CuerpacadLineasBitacora c WHERE c.fechaCambio = :fechaCambio")
    , @NamedQuery(name = "CuerpacadLineasBitacora.findByRegistro", query = "SELECT c FROM CuerpacadLineasBitacora c WHERE c.registro = :registro")
    , @NamedQuery(name = "CuerpacadLineasBitacora.findByNombre", query = "SELECT c FROM CuerpacadLineasBitacora c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CuerpacadLineasBitacora.findByEstatus", query = "SELECT c FROM CuerpacadLineasBitacora c WHERE c.estatus = :estatus")})
public class CuerpacadLineasBitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bitacora")
    private Integer bitacora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cambio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCambio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private int registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @JoinColumn(name = "cuerpo_academico", referencedColumnName = "cuerpo_academico")
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerpoAcademico;

    public CuerpacadLineasBitacora() {
    }

    public CuerpacadLineasBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public CuerpacadLineasBitacora(Integer bitacora, Date fechaCambio, int registro, String nombre, boolean estatus) {
        this.bitacora = bitacora;
        this.fechaCambio = fechaCambio;
        this.registro = registro;
        this.nombre = nombre;
        this.estatus = estatus;
    }

    public Integer getBitacora() {
        return bitacora;
    }

    public void setBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public CuerposAcademicosRegistro getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
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
        if (!(object instanceof CuerpacadLineasBitacora)) {
            return false;
        }
        CuerpacadLineasBitacora other = (CuerpacadLineasBitacora) object;
        if ((this.bitacora == null && other.bitacora != null) || (this.bitacora != null && !this.bitacora.equals(other.bitacora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineasBitacora[ bitacora=" + bitacora + " ]";
    }
    
}
