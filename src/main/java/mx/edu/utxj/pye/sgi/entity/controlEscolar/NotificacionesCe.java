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
@Table(name = "notificaciones_ce", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionesCe.findAll", query = "SELECT n FROM NotificacionesCe n"),
    @NamedQuery(name = "NotificacionesCe.findByNotificacion", query = "SELECT n FROM NotificacionesCe n WHERE n.notificacion = :notificacion"),
    @NamedQuery(name = "NotificacionesCe.findByFechaInicioDuracion", query = "SELECT n FROM NotificacionesCe n WHERE n.fechaInicioDuracion = :fechaInicioDuracion"),
    @NamedQuery(name = "NotificacionesCe.findByFechaFinDuracion", query = "SELECT n FROM NotificacionesCe n WHERE n.fechaFinDuracion = :fechaFinDuracion"),
    @NamedQuery(name = "NotificacionesCe.findByTituloPrincipal", query = "SELECT n FROM NotificacionesCe n WHERE n.tituloPrincipal = :tituloPrincipal"),
    @NamedQuery(name = "NotificacionesCe.findBySubtitulo", query = "SELECT n FROM NotificacionesCe n WHERE n.subtitulo = :subtitulo"),
    @NamedQuery(name = "NotificacionesCe.findByTipo", query = "SELECT n FROM NotificacionesCe n WHERE n.tipo = :tipo"),
    @NamedQuery(name = "NotificacionesCe.findByContenido", query = "SELECT n FROM NotificacionesCe n WHERE n.contenido = :contenido"),
    @NamedQuery(name = "NotificacionesCe.findByLugar", query = "SELECT n FROM NotificacionesCe n WHERE n.lugar = :lugar"),
    @NamedQuery(name = "NotificacionesCe.findByHoraInicio", query = "SELECT n FROM NotificacionesCe n WHERE n.horaInicio = :horaInicio"),
    @NamedQuery(name = "NotificacionesCe.findByDuracionEvento", query = "SELECT n FROM NotificacionesCe n WHERE n.duracionEvento = :duracionEvento"),
    @NamedQuery(name = "NotificacionesCe.findByPersonaResponsable", query = "SELECT n FROM NotificacionesCe n WHERE n.personaResponsable = :personaResponsable"),
    @NamedQuery(name = "NotificacionesCe.findByExpositor", query = "SELECT n FROM NotificacionesCe n WHERE n.expositor = :expositor"),
    @NamedQuery(name = "NotificacionesCe.findByParticipantesEstudiantes", query = "SELECT n FROM NotificacionesCe n WHERE n.participantesEstudiantes = :participantesEstudiantes"),
    @NamedQuery(name = "NotificacionesCe.findByParticipantesPersonal", query = "SELECT n FROM NotificacionesCe n WHERE n.participantesPersonal = :participantesPersonal"),
    @NamedQuery(name = "NotificacionesCe.findByParticipantesOtros", query = "SELECT n FROM NotificacionesCe n WHERE n.participantesOtros = :participantesOtros"),
    @NamedQuery(name = "NotificacionesCe.findByPersonaRegistro", query = "SELECT n FROM NotificacionesCe n WHERE n.personaRegistro = :personaRegistro"),
    @NamedQuery(name = "NotificacionesCe.findByAlcance", query = "SELECT n FROM NotificacionesCe n WHERE n.alcance = :alcance"),
    @NamedQuery(name = "NotificacionesCe.findByGeneral", query = "SELECT n FROM NotificacionesCe n WHERE n.general = :general"),
    @NamedQuery(name = "NotificacionesCe.findByFechaRegistro", query = "SELECT n FROM NotificacionesCe n WHERE n.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "NotificacionesCe.findByAreaResponsable", query = "SELECT n FROM NotificacionesCe n WHERE n.areaResponsable = :areaResponsable")})
public class NotificacionesCe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notificacion")
    private Integer notificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio_duracion")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioDuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin_duracion")
    @Temporal(TemporalType.DATE)
    private Date fechaFinDuracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "titulo_principal")
    private String tituloPrincipal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "subtitulo")
    private String subtitulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 5000)
    @Column(name = "contenido")
    private String contenido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "lugar")
    private String lugar;
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;
    @Column(name = "duracion_evento")
    @Temporal(TemporalType.TIME)
    private Date duracionEvento;
    @Size(max = 100)
    @Column(name = "persona_responsable")
    private String personaResponsable;
    @Size(max = 100)
    @Column(name = "expositor")
    private String expositor;
    @Column(name = "participantes_estudiantes")
    private Integer participantesEstudiantes;
    @Column(name = "participantes_personal")
    private Integer participantesPersonal;
    @Column(name = "participantes_otros")
    private Integer participantesOtros;
    @Basic(optional = false)
    @NotNull
    @Column(name = "persona_registro")
    private int personaRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 238)
    @Column(name = "alcance")
    private String alcance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "general")
    private boolean general;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notificacion", fetch = FetchType.LAZY)
    private List<NotificacionesCeImagenes> notificacionesCeImagenesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notificacionesCe", fetch = FetchType.LAZY)
    private List<NotificacionesAreas> notificacionesAreasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notificacion", fetch = FetchType.LAZY)
    private List<NotificacionesEnlaces> notificacionesEnlacesList;
    @Size(max = 255)
    @Column(name = "area_responsable")
    private String areaResponsable;

    public NotificacionesCe() {
    }

    public NotificacionesCe(Integer notificacion) {
        this.notificacion = notificacion;
    }

    public NotificacionesCe(Integer notificacion, Date fechaInicioDuracion, Date fechaFinDuracion, String tituloPrincipal, String subtitulo, String tipo, String lugar, int personaRegistro, String alcance, boolean general, Date fechaRegistro) {
        this.notificacion = notificacion;
        this.fechaInicioDuracion = fechaInicioDuracion;
        this.fechaFinDuracion = fechaFinDuracion;
        this.tituloPrincipal = tituloPrincipal;
        this.subtitulo = subtitulo;
        this.tipo = tipo;
        this.lugar = lugar;
        this.personaRegistro = personaRegistro;
        this.alcance = alcance;
        this.general = general;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Integer notificacion) {
        this.notificacion = notificacion;
    }

    public Date getFechaInicioDuracion() {
        return fechaInicioDuracion;
    }

    public void setFechaInicioDuracion(Date fechaInicioDuracion) {
        this.fechaInicioDuracion = fechaInicioDuracion;
    }

    public Date getFechaFinDuracion() {
        return fechaFinDuracion;
    }

    public void setFechaFinDuracion(Date fechaFinDuracion) {
        this.fechaFinDuracion = fechaFinDuracion;
    }

    public String getTituloPrincipal() {
        return tituloPrincipal;
    }

    public void setTituloPrincipal(String tituloPrincipal) {
        this.tituloPrincipal = tituloPrincipal;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getDuracionEvento() {
        return duracionEvento;
    }

    public void setDuracionEvento(Date duracionEvento) {
        this.duracionEvento = duracionEvento;
    }

    public String getPersonaResponsable() {
        return personaResponsable;
    }

    public void setPersonaResponsable(String personaResponsable) {
        this.personaResponsable = personaResponsable;
    }

    public String getExpositor() {
        return expositor;
    }

    public void setExpositor(String expositor) {
        this.expositor = expositor;
    }

    public Integer getParticipantesEstudiantes() {
        return participantesEstudiantes;
    }

    public void setParticipantesEstudiantes(Integer participantesEstudiantes) {
        this.participantesEstudiantes = participantesEstudiantes;
    }

    public Integer getParticipantesPersonal() {
        return participantesPersonal;
    }

    public void setParticipantesPersonal(Integer participantesPersonal) {
        this.participantesPersonal = participantesPersonal;
    }

    public Integer getParticipantesOtros() {
        return participantesOtros;
    }

    public void setParticipantesOtros(Integer participantesOtros) {
        this.participantesOtros = participantesOtros;
    }

    public int getPersonaRegistro() {
        return personaRegistro;
    }

    public void setPersonaRegistro(int personaRegistro) {
        this.personaRegistro = personaRegistro;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public boolean getGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
@XmlTransient
    public List<NotificacionesCeImagenes> getNotificacionesCeImagenesList() {
        return notificacionesCeImagenesList;
    }

    public void setNotificacionesCeImagenesList(List<NotificacionesCeImagenes> notificacionesCeImagenesList) {
        this.notificacionesCeImagenesList = notificacionesCeImagenesList;
    }
    
    @XmlTransient
    public List<NotificacionesAreas> getNotificacionesAreasList() {
        return notificacionesAreasList;
    }
    
    public void setNotificacionesAreasList(List<NotificacionesAreas> notificacionesAreasList) {
        this.notificacionesAreasList = notificacionesAreasList;
    }

    @XmlTransient
    public List<NotificacionesEnlaces> getNotificacionesEnlacesList() {
        return notificacionesEnlacesList;
    }
    
    public String getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(String areaResponsable) {
        this.areaResponsable = areaResponsable;
    }

    public void setNotificacionesEnlacesList(List<NotificacionesEnlaces> notificacionesEnlacesList) {
        this.notificacionesEnlacesList = notificacionesEnlacesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacion != null ? notificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionesCe)) {
            return false;
        }
        NotificacionesCe other = (NotificacionesCe) object;
        if ((this.notificacion == null && other.notificacion != null) || (this.notificacion != null && !this.notificacion.equals(other.notificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCe[ notificacion=" + notificacion + " ]";
    }
    
}
