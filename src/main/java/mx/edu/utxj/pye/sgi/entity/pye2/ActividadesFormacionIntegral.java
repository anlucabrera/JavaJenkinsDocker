/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "actividades_formacion_integral", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadesFormacionIntegral.findAll", query = "SELECT a FROM ActividadesFormacionIntegral a")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByRegistro", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.registro = :registro")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByActividadFormacionIntegral", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.actividadFormacionIntegral = :actividadFormacionIntegral")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByFechaInicio", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByFechaFin", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.fechaFin = :fechaFin")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByNombre", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByOrganizaParticipa", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.organizaParticipa = :organizaParticipa")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByObjetivo", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.objetivo = :objetivo")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByLugarRealizacion", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.lugarRealizacion = :lugarRealizacion")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByDuracion", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.duracion = :duracion")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByFacilitadorEmpresa", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.facilitadorEmpresa = :facilitadorEmpresa")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByEquiposParticipantes", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.equiposParticipantes = :equiposParticipantes")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByEquiposGanadores", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.equiposGanadores = :equiposGanadores")
    , @NamedQuery(name = "ActividadesFormacionIntegral.findByPeriodo", query = "SELECT a FROM ActividadesFormacionIntegral a WHERE a.periodo = :periodo")})
public class ActividadesFormacionIntegral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "actividad_formacion_integral")
    private String actividadFormacionIntegral;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "organiza_participa")
    private String organizaParticipa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "lugar_realizacion")
    private String lugarRealizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "duracion")
    private String duracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "facilitador_empresa")
    private String facilitadorEmpresa;
    @Size(max = 1000)
    @Column(name = "equipos_participantes")
    private String equiposParticipantes;
    @Size(max = 1000)
    @Column(name = "equipos_ganadores")
    private String equiposGanadores;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividadFormacionIntegral")
    private List<ParticipantesActividadesFormacionIntegral> participantesActividadesFormacionIntegralList;
    @JoinColumn(name = "actividad_tipo", referencedColumnName = "actividad_tipo")
    @ManyToOne(optional = false)
    private ActividadesTipos actividadTipo;
    @JoinColumn(name = "evento_tipo", referencedColumnName = "evento_tipo")
    @ManyToOne(optional = false)
    private EventosTipos eventoTipo;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ActividadesFormacionIntegral() {
    }

    public ActividadesFormacionIntegral(Integer registro) {
        this.registro = registro;
    }

    public ActividadesFormacionIntegral(Integer registro, String actividadFormacionIntegral, Date fechaInicio, Date fechaFin, String nombre, String organizaParticipa, String objetivo, String lugarRealizacion, String duracion, String facilitadorEmpresa, int periodo) {
        this.registro = registro;
        this.actividadFormacionIntegral = actividadFormacionIntegral;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombre = nombre;
        this.organizaParticipa = organizaParticipa;
        this.objetivo = objetivo;
        this.lugarRealizacion = lugarRealizacion;
        this.duracion = duracion;
        this.facilitadorEmpresa = facilitadorEmpresa;
        this.periodo = periodo;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getActividadFormacionIntegral() {
        return actividadFormacionIntegral;
    }

    public void setActividadFormacionIntegral(String actividadFormacionIntegral) {
        this.actividadFormacionIntegral = actividadFormacionIntegral;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOrganizaParticipa() {
        return organizaParticipa;
    }

    public void setOrganizaParticipa(String organizaParticipa) {
        this.organizaParticipa = organizaParticipa;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getLugarRealizacion() {
        return lugarRealizacion;
    }

    public void setLugarRealizacion(String lugarRealizacion) {
        this.lugarRealizacion = lugarRealizacion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFacilitadorEmpresa() {
        return facilitadorEmpresa;
    }

    public void setFacilitadorEmpresa(String facilitadorEmpresa) {
        this.facilitadorEmpresa = facilitadorEmpresa;
    }

    public String getEquiposParticipantes() {
        return equiposParticipantes;
    }

    public void setEquiposParticipantes(String equiposParticipantes) {
        this.equiposParticipantes = equiposParticipantes;
    }

    public String getEquiposGanadores() {
        return equiposGanadores;
    }

    public void setEquiposGanadores(String equiposGanadores) {
        this.equiposGanadores = equiposGanadores;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @XmlTransient
    public List<ParticipantesActividadesFormacionIntegral> getParticipantesActividadesFormacionIntegralList() {
        return participantesActividadesFormacionIntegralList;
    }

    public void setParticipantesActividadesFormacionIntegralList(List<ParticipantesActividadesFormacionIntegral> participantesActividadesFormacionIntegralList) {
        this.participantesActividadesFormacionIntegralList = participantesActividadesFormacionIntegralList;
    }

    public ActividadesTipos getActividadTipo() {
        return actividadTipo;
    }

    public void setActividadTipo(ActividadesTipos actividadTipo) {
        this.actividadTipo = actividadTipo;
    }

    public EventosTipos getEventoTipo() {
        return eventoTipo;
    }

    public void setEventoTipo(EventosTipos eventoTipo) {
        this.eventoTipo = eventoTipo;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadesFormacionIntegral)) {
            return false;
        }
        ActividadesFormacionIntegral other = (ActividadesFormacionIntegral) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral[ registro=" + registro + " ]";
    }
    
}
