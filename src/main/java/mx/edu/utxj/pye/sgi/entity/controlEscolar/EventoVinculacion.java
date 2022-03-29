/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evento_vinculacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventoVinculacion.findAll", query = "SELECT e FROM EventoVinculacion e")
    , @NamedQuery(name = "EventoVinculacion.findByEvento", query = "SELECT e FROM EventoVinculacion e WHERE e.evento = :evento")
    , @NamedQuery(name = "EventoVinculacion.findByGeneracion", query = "SELECT e FROM EventoVinculacion e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EventoVinculacion.findByNivel", query = "SELECT e FROM EventoVinculacion e WHERE e.nivel = :nivel")
    , @NamedQuery(name = "EventoVinculacion.findByActividad", query = "SELECT e FROM EventoVinculacion e WHERE e.actividad = :actividad")
    , @NamedQuery(name = "EventoVinculacion.findByFechaInicio", query = "SELECT e FROM EventoVinculacion e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "EventoVinculacion.findByFechaFin", query = "SELECT e FROM EventoVinculacion e WHERE e.fechaFin = :fechaFin")})
public class EventoVinculacion implements Serializable {

    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evento")
    private Integer evento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "nivel")
    private String nivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 29)
    @Column(name = "actividad")
    private String actividad;
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
    @JoinColumn(name = "documento_proceso", referencedColumnName = "documento_proceso")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DocumentoProceso documentoProceso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento", fetch = FetchType.LAZY)
    private List<AperturaExtemporaneaEventoVinculacion> aperturaExtemporaneaEventoVinculacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento", fetch = FetchType.LAZY)
    private List<DocumentoSeguimientoVinculacion> documentoSeguimientoVinculacionList;
    
    public EventoVinculacion() {
    }

    public EventoVinculacion(Integer evento) {
        this.evento = evento;
    }

    public EventoVinculacion(Integer evento, short generacion, String nivel, String actividad, Date fechaInicio, Date fechaFin) {
        this.evento = evento;
        this.generacion = generacion;
        this.nivel = nivel;
        this.actividad = actividad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getEvento() {
        return evento;
    }

    public void setEvento(Integer evento) {
        this.evento = evento;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
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
    
    public DocumentoProceso getDocumentoProceso() {
        return documentoProceso;
    }

    public void setDocumentoProceso(DocumentoProceso documentoProceso) {
        this.documentoProceso = documentoProceso;
    }
    
    @XmlTransient
    public List<DocumentoSeguimientoVinculacion> getDocumentoSeguimientoVinculacionList() {
        return documentoSeguimientoVinculacionList;
    }

    public void setDocumentoSeguimientoVinculacionList(List<DocumentoSeguimientoVinculacion> documentoSeguimientoVinculacionList) {
        this.documentoSeguimientoVinculacionList = documentoSeguimientoVinculacionList;
    }
    
    @XmlTransient
    public List<AperturaExtemporaneaEventoVinculacion> getAperturaExtemporaneaEventoVinculacionList() {
        return aperturaExtemporaneaEventoVinculacionList;
    }

    public void setAperturaExtemporaneaEventoVinculacionList(List<AperturaExtemporaneaEventoVinculacion> aperturaExtemporaneaEventoVinculacionList) {
        this.aperturaExtemporaneaEventoVinculacionList = aperturaExtemporaneaEventoVinculacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evento != null ? evento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventoVinculacion)) {
            return false;
        }
        EventoVinculacion other = (EventoVinculacion) object;
        if ((this.evento == null && other.evento != null) || (this.evento != null && !this.evento.equals(other.evento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion[ evento=" + evento + " ]";
    }
}
