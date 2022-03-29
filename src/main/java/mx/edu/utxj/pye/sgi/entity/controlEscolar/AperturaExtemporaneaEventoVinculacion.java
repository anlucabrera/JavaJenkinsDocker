/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "apertura_extemporanea_evento_vinculacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AperturaExtemporaneaEventoVinculacion.findAll", query = "SELECT a FROM AperturaExtemporaneaEventoVinculacion a")
    , @NamedQuery(name = "AperturaExtemporaneaEventoVinculacion.findByAperturaExtemporanea", query = "SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.aperturaExtemporanea = :aperturaExtemporanea")
    , @NamedQuery(name = "AperturaExtemporaneaEventoVinculacion.findByFechaInicio", query = "SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "AperturaExtemporaneaEventoVinculacion.findByFechaFin", query = "SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.fechaFin = :fechaFin")
    , @NamedQuery(name = "AperturaExtemporaneaEventoVinculacion.findByGrabaApertura", query = "SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.grabaApertura = :grabaApertura")})
public class AperturaExtemporaneaEventoVinculacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "apertura_extemporanea")
    private Integer aperturaExtemporanea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "graba_apertura")
    private int grabaApertura;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoVinculacion evento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "seguimiento_vinculacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SeguimientoVinculacionEstudiante seguimiento;

    public AperturaExtemporaneaEventoVinculacion() {
    }

    public AperturaExtemporaneaEventoVinculacion(Integer aperturaExtemporanea) {
        this.aperturaExtemporanea = aperturaExtemporanea;
    }

    public AperturaExtemporaneaEventoVinculacion(Integer aperturaExtemporanea, Date fechaInicio, Date fechaFin, int grabaApertura) {
        this.aperturaExtemporanea = aperturaExtemporanea;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.grabaApertura = grabaApertura;
    }

    public Integer getAperturaExtemporanea() {
        return aperturaExtemporanea;
    }

    public void setAperturaExtemporanea(Integer aperturaExtemporanea) {
        this.aperturaExtemporanea = aperturaExtemporanea;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getGrabaApertura() {
        return grabaApertura;
    }

    public void setGrabaApertura(int grabaApertura) {
        this.grabaApertura = grabaApertura;
    }

    public EventoVinculacion getEvento() {
        return evento;
    }

    public void setEvento(EventoVinculacion evento) {
        this.evento = evento;
    }

    public SeguimientoVinculacionEstudiante getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(SeguimientoVinculacionEstudiante seguimiento) {
        this.seguimiento = seguimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aperturaExtemporanea != null ? aperturaExtemporanea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AperturaExtemporaneaEventoVinculacion)) {
            return false;
        }
        AperturaExtemporaneaEventoVinculacion other = (AperturaExtemporaneaEventoVinculacion) object;
        if ((this.aperturaExtemporanea == null && other.aperturaExtemporanea != null) || (this.aperturaExtemporanea != null && !this.aperturaExtemporanea.equals(other.aperturaExtemporanea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoVinculacion[ aperturaExtemporanea=" + aperturaExtemporanea + " ]";
    }
    
}
