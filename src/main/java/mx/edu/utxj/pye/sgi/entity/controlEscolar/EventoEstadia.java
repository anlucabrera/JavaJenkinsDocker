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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "evento_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventoEstadia.findAll", query = "SELECT e FROM EventoEstadia e")
    , @NamedQuery(name = "EventoEstadia.findByEvento", query = "SELECT e FROM EventoEstadia e WHERE e.evento = :evento")
    , @NamedQuery(name = "EventoEstadia.findByGeneracion", query = "SELECT e FROM EventoEstadia e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EventoEstadia.findByNivel", query = "SELECT e FROM EventoEstadia e WHERE e.nivel = :nivel")
    , @NamedQuery(name = "EventoEstadia.findByActividad", query = "SELECT e FROM EventoEstadia e WHERE e.actividad = :actividad")
    , @NamedQuery(name = "EventoEstadia.findByUsuario", query = "SELECT e FROM EventoEstadia e WHERE e.usuario = :usuario")
    , @NamedQuery(name = "EventoEstadia.findByFechaInicio", query = "SELECT e FROM EventoEstadia e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "EventoEstadia.findByFechaFin", query = "SELECT e FROM EventoEstadia e WHERE e.fechaFin = :fechaFin")})
public class EventoEstadia implements Serializable {

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
    @Size(min = 1, max = 51)
    @Column(name = "actividad")
    private String actividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "usuario")
    private String usuario;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<EvaluacionEstadia> evaluacionEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<CoordinadorAreaEstadia> coordinadorAreaEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<AsesorAcademicoEstadia> asesorAcademicoEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<EntregaFotografiasEstudiante> entregaFotografiasEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<DocumentoSeguimientoEstadia> documentoSeguimientoEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<SeguimientoEstadiaEstudiante> seguimientoEstadiaEstudianteList;

    public EventoEstadia() {
    }

    public EventoEstadia(Integer evento) {
        this.evento = evento;
    }

    public EventoEstadia(Integer evento, short generacion, String nivel, String actividad, String usuario, Date fechaInicio, Date fechaFin) {
        this.evento = evento;
        this.generacion = generacion;
        this.nivel = nivel;
        this.actividad = actividad;
        this.usuario = usuario;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    @XmlTransient
    public List<EvaluacionEstadia> getEvaluacionEstadiaList() {
        return evaluacionEstadiaList;
    }

    public void setEvaluacionEstadiaList(List<EvaluacionEstadia> evaluacionEstadiaList) {
        this.evaluacionEstadiaList = evaluacionEstadiaList;
    }

    @XmlTransient
    public List<CoordinadorAreaEstadia> getCoordinadorAreaEstadiaList() {
        return coordinadorAreaEstadiaList;
    }

    public void setCoordinadorAreaEstadiaList(List<CoordinadorAreaEstadia> coordinadorAreaEstadiaList) {
        this.coordinadorAreaEstadiaList = coordinadorAreaEstadiaList;
    }

    @XmlTransient
    public List<AsesorAcademicoEstadia> getAsesorAcademicoEstadiaList() {
        return asesorAcademicoEstadiaList;
    }

    public void setAsesorAcademicoEstadiaList(List<AsesorAcademicoEstadia> asesorAcademicoEstadiaList) {
        this.asesorAcademicoEstadiaList = asesorAcademicoEstadiaList;
    }

    @XmlTransient
    public List<EntregaFotografiasEstudiante> getEntregaFotografiasEstudianteList() {
        return entregaFotografiasEstudianteList;
    }

    public void setEntregaFotografiasEstudianteList(List<EntregaFotografiasEstudiante> entregaFotografiasEstudianteList) {
        this.entregaFotografiasEstudianteList = entregaFotografiasEstudianteList;
    }

    @XmlTransient
    public List<DocumentoSeguimientoEstadia> getDocumentoSeguimientoEstadiaList() {
        return documentoSeguimientoEstadiaList;
    }

    public void setDocumentoSeguimientoEstadiaList(List<DocumentoSeguimientoEstadia> documentoSeguimientoEstadiaList) {
        this.documentoSeguimientoEstadiaList = documentoSeguimientoEstadiaList;
    }

    @XmlTransient
    public List<SeguimientoEstadiaEstudiante> getSeguimientoEstadiaEstudianteList() {
        return seguimientoEstadiaEstudianteList;
    }

    public void setSeguimientoEstadiaEstudianteList(List<SeguimientoEstadiaEstudiante> seguimientoEstadiaEstudianteList) {
        this.seguimientoEstadiaEstudianteList = seguimientoEstadiaEstudianteList;
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
        if (!(object instanceof EventoEstadia)) {
            return false;
        }
        EventoEstadia other = (EventoEstadia) object;
        if ((this.evento == null && other.evento != null) || (this.evento != null && !this.evento.equals(other.evento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia[ evento=" + evento + " ]";
    }
    
}
