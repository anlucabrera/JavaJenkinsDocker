/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "seguimiento_aclaracion", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeguimientoAclaracion.findAll", query = "SELECT s FROM SeguimientoAclaracion s")
    , @NamedQuery(name = "SeguimientoAclaracion.findByCveAclaracionSeguimiento", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.cveAclaracionSeguimiento = :cveAclaracionSeguimiento")
    , @NamedQuery(name = "SeguimientoAclaracion.findByEmisor", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.emisor = :emisor")
    , @NamedQuery(name = "SeguimientoAclaracion.findByReceptor", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.receptor = :receptor")
    , @NamedQuery(name = "SeguimientoAclaracion.findByMensaje", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.mensaje = :mensaje")
    , @NamedQuery(name = "SeguimientoAclaracion.findByFecha", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.fecha = :fecha")
    , @NamedQuery(name = "SeguimientoAclaracion.findByEstatus", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.estatus = :estatus")
    , @NamedQuery(name = "SeguimientoAclaracion.findByEstatusOnOF", query = "SELECT s FROM SeguimientoAclaracion s WHERE s.estatusOnOF = :estatusOnOF")})
public class SeguimientoAclaracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cve_aclaracion_seguimiento")
    private Integer cveAclaracionSeguimiento;
    @Column(name = "emisor")
    private Integer emisor;
    @Column(name = "receptor")
    private Integer receptor;
    @Column(name = "mensaje")
    private String mensaje;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "estatus")
    private Character estatus;
    @Column(name = "estatusOnOF")
    private Character estatusOnOF;
    @JoinColumn(name = "cve_aclaracion", referencedColumnName = "cve_aclaracion")
    @ManyToOne(optional = false)
    private Aclaracion cveAclaracion;

    public SeguimientoAclaracion() {
    }

    public SeguimientoAclaracion(Integer cveAclaracionSeguimiento) {
        this.cveAclaracionSeguimiento = cveAclaracionSeguimiento;
    }

    public Integer getCveAclaracionSeguimiento() {
        return cveAclaracionSeguimiento;
    }

    public void setCveAclaracionSeguimiento(Integer cveAclaracionSeguimiento) {
        this.cveAclaracionSeguimiento = cveAclaracionSeguimiento;
    }

    public Integer getEmisor() {
        return emisor;
    }

    public void setEmisor(Integer emisor) {
        this.emisor = emisor;
    }

    public Integer getReceptor() {
        return receptor;
    }

    public void setReceptor(Integer receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Character getEstatus() {
        return estatus;
    }

    public void setEstatus(Character estatus) {
        this.estatus = estatus;
    }

    public Character getEstatusOnOF() {
        return estatusOnOF;
    }

    public void setEstatusOnOF(Character estatusOnOF) {
        this.estatusOnOF = estatusOnOF;
    }

    public Aclaracion getCveAclaracion() {
        return cveAclaracion;
    }

    public void setCveAclaracion(Aclaracion cveAclaracion) {
        this.cveAclaracion = cveAclaracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveAclaracionSeguimiento != null ? cveAclaracionSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeguimientoAclaracion)) {
            return false;
        }
        SeguimientoAclaracion other = (SeguimientoAclaracion) object;
        if ((this.cveAclaracionSeguimiento == null && other.cveAclaracionSeguimiento != null) || (this.cveAclaracionSeguimiento != null && !this.cveAclaracionSeguimiento.equals(other.cveAclaracionSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.SeguimientoAclaracion[ cveAclaracionSeguimiento=" + cveAclaracionSeguimiento + " ]";
    }
    
}
