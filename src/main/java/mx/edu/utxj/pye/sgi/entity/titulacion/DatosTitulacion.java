/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author UTXJ
 */
@Entity
@Table(name = "datos_titulacion", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosTitulacion.findAll", query = "SELECT d FROM DatosTitulacion d")
    , @NamedQuery(name = "DatosTitulacion.findByDatoTitulacion", query = "SELECT d FROM DatosTitulacion d WHERE d.datoTitulacion = :datoTitulacion")
    , @NamedQuery(name = "DatosTitulacion.findByModalidadTitulacion", query = "SELECT d FROM DatosTitulacion d WHERE d.modalidadTitulacion = :modalidadTitulacion")
    , @NamedQuery(name = "DatosTitulacion.findByPromedioFinal", query = "SELECT d FROM DatosTitulacion d WHERE d.promedioFinal = :promedioFinal")
    , @NamedQuery(name = "DatosTitulacion.findByServicioSocial", query = "SELECT d FROM DatosTitulacion d WHERE d.servicioSocial = :servicioSocial")})
public class DatosTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dato_titulacion")
    private Integer datoTitulacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "modalidad_titulacion")
    private String modalidadTitulacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio_final")
    private BigDecimal promedioFinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_social")
    private boolean servicioSocial;
    @JoinColumn(name = "expediente", referencedColumnName = "expediente")
    @ManyToOne(optional = false)
    private ExpedientesTitulacion expediente;

    public DatosTitulacion() {
    }

    public DatosTitulacion(Integer datoTitulacion) {
        this.datoTitulacion = datoTitulacion;
    }

    public DatosTitulacion(Integer datoTitulacion, String modalidadTitulacion, BigDecimal promedioFinal, boolean servicioSocial) {
        this.datoTitulacion = datoTitulacion;
        this.modalidadTitulacion = modalidadTitulacion;
        this.promedioFinal = promedioFinal;
        this.servicioSocial = servicioSocial;
    }

    public Integer getDatoTitulacion() {
        return datoTitulacion;
    }

    public void setDatoTitulacion(Integer datoTitulacion) {
        this.datoTitulacion = datoTitulacion;
    }

    public String getModalidadTitulacion() {
        return modalidadTitulacion;
    }

    public void setModalidadTitulacion(String modalidadTitulacion) {
        this.modalidadTitulacion = modalidadTitulacion;
    }

    public BigDecimal getPromedioFinal() {
        return promedioFinal;
    }

    public void setPromedioFinal(BigDecimal promedioFinal) {
        this.promedioFinal = promedioFinal;
    }

    public boolean getServicioSocial() {
        return servicioSocial;
    }

    public void setServicioSocial(boolean servicioSocial) {
        this.servicioSocial = servicioSocial;
    }

    public ExpedientesTitulacion getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedientesTitulacion expediente) {
        this.expediente = expediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datoTitulacion != null ? datoTitulacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosTitulacion)) {
            return false;
        }
        DatosTitulacion other = (DatosTitulacion) object;
        if ((this.datoTitulacion == null && other.datoTitulacion != null) || (this.datoTitulacion != null && !this.datoTitulacion.equals(other.datoTitulacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.DatosTitulacion[ datoTitulacion=" + datoTitulacion + " ]";
    }
    
}
