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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "apertura_extemporanea_evento_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findAll", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByAperturaExtemporanea", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.aperturaExtemporanea = :aperturaExtemporanea")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByFechaInicio", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByFechaFin", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.fechaFin = :fechaFin")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByTipoApertura", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.tipoApertura = :tipoApertura")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByGrabaApertura", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.grabaApertura = :grabaApertura")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByPersonalValida", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.personalValida = :personalValida")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByFechaValidacion", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.fechaValidacion = :fechaValidacion")
    , @NamedQuery(name = "AperturaExtemporaneaEventoEstadia.findByValidada", query = "SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.validada = :validada")})
public class AperturaExtemporaneaEventoEstadia implements Serializable {

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
    @Size(min = 1, max = 21)
    @Column(name = "tipo_apertura")
    private String tipoApertura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "graba_apertura")
    private int grabaApertura;
    @Column(name = "personal_valida")
    private Integer personalValida;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validada")
    private boolean validada;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "seguimiento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SeguimientoEstadiaEstudiante seguimiento;

    public AperturaExtemporaneaEventoEstadia() {
    }

    public AperturaExtemporaneaEventoEstadia(Integer aperturaExtemporanea) {
        this.aperturaExtemporanea = aperturaExtemporanea;
    }

    public AperturaExtemporaneaEventoEstadia(Integer aperturaExtemporanea, Date fechaInicio, Date fechaFin, String tipoApertura, int grabaApertura, boolean validada) {
        this.aperturaExtemporanea = aperturaExtemporanea;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoApertura = tipoApertura;
        this.grabaApertura = grabaApertura;
        this.validada = validada;
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

    public String getTipoApertura() {
        return tipoApertura;
    }

    public void setTipoApertura(String tipoApertura) {
        this.tipoApertura = tipoApertura;
    }

    public int getGrabaApertura() {
        return grabaApertura;
    }

    public void setGrabaApertura(int grabaApertura) {
        this.grabaApertura = grabaApertura;
    }

    public Integer getPersonalValida() {
        return personalValida;
    }

    public void setPersonalValida(Integer personalValida) {
        this.personalValida = personalValida;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public boolean getValidada() {
        return validada;
    }

    public void setValidada(boolean validada) {
        this.validada = validada;
    }

    public EventoEstadia getEvento() {
        return evento;
    }

    public void setEvento(EventoEstadia evento) {
        this.evento = evento;
    }

    public SeguimientoEstadiaEstudiante getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(SeguimientoEstadiaEstudiante seguimiento) {
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
        if (!(object instanceof AperturaExtemporaneaEventoEstadia)) {
            return false;
        }
        AperturaExtemporaneaEventoEstadia other = (AperturaExtemporaneaEventoEstadia) object;
        if ((this.aperturaExtemporanea == null && other.aperturaExtemporanea != null) || (this.aperturaExtemporanea != null && !this.aperturaExtemporanea.equals(other.aperturaExtemporanea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoEstadia[ aperturaExtemporanea=" + aperturaExtemporanea + " ]";
    }
    
}
