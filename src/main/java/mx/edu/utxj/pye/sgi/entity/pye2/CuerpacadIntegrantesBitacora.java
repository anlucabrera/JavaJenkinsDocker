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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpacad_integrantes_bitacora", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadIntegrantesBitacora.findAll", query = "SELECT c FROM CuerpacadIntegrantesBitacora c")
    , @NamedQuery(name = "CuerpacadIntegrantesBitacora.findByBitacora", query = "SELECT c FROM CuerpacadIntegrantesBitacora c WHERE c.bitacora = :bitacora")
    , @NamedQuery(name = "CuerpacadIntegrantesBitacora.findByFechaCambio", query = "SELECT c FROM CuerpacadIntegrantesBitacora c WHERE c.fechaCambio = :fechaCambio")
    , @NamedQuery(name = "CuerpacadIntegrantesBitacora.findByRegistro", query = "SELECT c FROM CuerpacadIntegrantesBitacora c WHERE c.registro = :registro")
    , @NamedQuery(name = "CuerpacadIntegrantesBitacora.findByPersonal", query = "SELECT c FROM CuerpacadIntegrantesBitacora c WHERE c.personal = :personal")
    , @NamedQuery(name = "CuerpacadIntegrantesBitacora.findByEstatus", query = "SELECT c FROM CuerpacadIntegrantesBitacora c WHERE c.estatus = :estatus")})
public class CuerpacadIntegrantesBitacora implements Serializable {

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
    @Column(name = "personal")
    private int personal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @JoinColumn(name = "cuerpo_academico", referencedColumnName = "cuerpo_academico")
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerpoAcademico;

    public CuerpacadIntegrantesBitacora() {
    }

    public CuerpacadIntegrantesBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public CuerpacadIntegrantesBitacora(Integer bitacora, Date fechaCambio, int registro, int personal, boolean estatus) {
        this.bitacora = bitacora;
        this.fechaCambio = fechaCambio;
        this.registro = registro;
        this.personal = personal;
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

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
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
        if (!(object instanceof CuerpacadIntegrantesBitacora)) {
            return false;
        }
        CuerpacadIntegrantesBitacora other = (CuerpacadIntegrantesBitacora) object;
        if ((this.bitacora == null && other.bitacora != null) || (this.bitacora != null && !this.bitacora.equals(other.bitacora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantesBitacora[ bitacora=" + bitacora + " ]";
    }
    
}
