/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "baja_reprobacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BajaReprobacion.findAll", query = "SELECT b FROM BajaReprobacion b")
    , @NamedQuery(name = "BajaReprobacion.findByBajaReprobacion", query = "SELECT b FROM BajaReprobacion b WHERE b.bajaReprobacion = :bajaReprobacion")})
public class BajaReprobacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "baja_reprobacion")
    private Integer bajaReprobacion;
    @JoinColumn(name = "carga_academica", referencedColumnName = "carga")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CargaAcademica cargaAcademica;
    @JoinColumn(name = "registro_baja", referencedColumnName = "id_bajas")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Baja registroBaja;

    public BajaReprobacion() {
    }

    public BajaReprobacion(Integer bajaReprobacion) {
        this.bajaReprobacion = bajaReprobacion;
    }

    public Integer getBajaReprobacion() {
        return bajaReprobacion;
    }

    public void setBajaReprobacion(Integer bajaReprobacion) {
        this.bajaReprobacion = bajaReprobacion;
    }

    public CargaAcademica getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(CargaAcademica cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }

    public Baja getRegistroBaja() {
        return registroBaja;
    }

    public void setRegistroBaja(Baja registroBaja) {
        this.registroBaja = registroBaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bajaReprobacion != null ? bajaReprobacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BajaReprobacion)) {
            return false;
        }
        BajaReprobacion other = (BajaReprobacion) object;
        if ((this.bajaReprobacion == null && other.bajaReprobacion != null) || (this.bajaReprobacion != null && !this.bajaReprobacion.equals(other.bajaReprobacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.BajaReprobacion[ bajaReprobacion=" + bajaReprobacion + " ]";
    }
    
}
