/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "registros_movilidad", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistrosMovilidad.findAll", query = "SELECT r FROM RegistrosMovilidad r")
    , @NamedQuery(name = "RegistrosMovilidad.findByRegistro", query = "SELECT r FROM RegistrosMovilidad r WHERE r.registro = :registro")
    , @NamedQuery(name = "RegistrosMovilidad.findByRegistroMovilidad", query = "SELECT r FROM RegistrosMovilidad r WHERE r.registroMovilidad = :registroMovilidad")
    , @NamedQuery(name = "RegistrosMovilidad.findByTipoParticipante", query = "SELECT r FROM RegistrosMovilidad r WHERE r.tipoParticipante = :tipoParticipante")
    , @NamedQuery(name = "RegistrosMovilidad.findByTipoMovilidad", query = "SELECT r FROM RegistrosMovilidad r WHERE r.tipoMovilidad = :tipoMovilidad")
    , @NamedQuery(name = "RegistrosMovilidad.findByFechaInicio", query = "SELECT r FROM RegistrosMovilidad r WHERE r.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "RegistrosMovilidad.findByFechaFin", query = "SELECT r FROM RegistrosMovilidad r WHERE r.fechaFin = :fechaFin")
    , @NamedQuery(name = "RegistrosMovilidad.findByParticipante", query = "SELECT r FROM RegistrosMovilidad r WHERE r.participante = :participante")
    , @NamedQuery(name = "RegistrosMovilidad.findByProgramaEducativo", query = "SELECT r FROM RegistrosMovilidad r WHERE r.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "RegistrosMovilidad.findByPeriodoEscolarCursado", query = "SELECT r FROM RegistrosMovilidad r WHERE r.periodoEscolarCursado = :periodoEscolarCursado")
    , @NamedQuery(name = "RegistrosMovilidad.findByCuatrimestreCursado", query = "SELECT r FROM RegistrosMovilidad r WHERE r.cuatrimestreCursado = :cuatrimestreCursado")
    , @NamedQuery(name = "RegistrosMovilidad.findByInstitucionOrganizacion", query = "SELECT r FROM RegistrosMovilidad r WHERE r.institucionOrganizacion = :institucionOrganizacion")
    , @NamedQuery(name = "RegistrosMovilidad.findByProyecto", query = "SELECT r FROM RegistrosMovilidad r WHERE r.proyecto = :proyecto")
    , @NamedQuery(name = "RegistrosMovilidad.findByDescripcion", query = "SELECT r FROM RegistrosMovilidad r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "RegistrosMovilidad.findByPresEst", query = "SELECT r FROM RegistrosMovilidad r WHERE r.presEst = :presEst")
    , @NamedQuery(name = "RegistrosMovilidad.findByPresFed", query = "SELECT r FROM RegistrosMovilidad r WHERE r.presFed = :presFed")
    , @NamedQuery(name = "RegistrosMovilidad.findByCapDer", query = "SELECT r FROM RegistrosMovilidad r WHERE r.capDer = :capDer")
    , @NamedQuery(name = "RegistrosMovilidad.findByIngPropios", query = "SELECT r FROM RegistrosMovilidad r WHERE r.ingPropios = :ingPropios")
    , @NamedQuery(name = "RegistrosMovilidad.findByIngExtra", query = "SELECT r FROM RegistrosMovilidad r WHERE r.ingExtra = :ingExtra")
    , @NamedQuery(name = "RegistrosMovilidad.findByDescripcionIngExt", query = "SELECT r FROM RegistrosMovilidad r WHERE r.descripcionIngExt = :descripcionIngExt")})
public class RegistrosMovilidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "registro_movilidad")
    private String registroMovilidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipo_participante")
    private String tipoParticipante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "tipo_movilidad")
    private String tipoMovilidad;
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
    @Size(min = 1, max = 250)
    @Column(name = "participante")
    private String participante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar_cursado")
    private int periodoEscolarCursado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "cuatrimestre_cursado")
    private String cuatrimestreCursado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "institucion_organizacion")
    private String institucionOrganizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "proyecto")
    private String proyecto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pres_est")
    private BigDecimal presEst;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pres_fed")
    private BigDecimal presFed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cap_der")
    private BigDecimal capDer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ing_propios")
    private BigDecimal ingPropios;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ing_extra")
    private BigDecimal ingExtra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion_ing_ext")
    private String descripcionIngExt;
    @JoinColumns({
        @JoinColumn(name = "ciudad", referencedColumnName = "idpais")
        , @JoinColumn(name = "pais", referencedColumnName = "idestado")})
    @ManyToOne(optional = false)
    private Estado estado;
    @JoinColumn(name = "programa_movilidad", referencedColumnName = "programa")
    @ManyToOne(optional = false)
    private ProgramasMovilidad programaMovilidad;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroMovilidad")
    private List<RegistroMovilidadDocente> registroMovilidadDocenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroMovilidad")
    private List<RegistroMovilidadEstudiante> registroMovilidadEstudianteList;

    public RegistrosMovilidad() {
    }

    public RegistrosMovilidad(Integer registro) {
        this.registro = registro;
    }

    public RegistrosMovilidad(Integer registro, String registroMovilidad, String tipoParticipante, String tipoMovilidad, Date fechaInicio, Date fechaFin, String participante, short programaEducativo, int periodoEscolarCursado, String cuatrimestreCursado, String institucionOrganizacion, String proyecto, String descripcion, BigDecimal presEst, BigDecimal presFed, BigDecimal capDer, BigDecimal ingPropios, BigDecimal ingExtra, String descripcionIngExt) {
        this.registro = registro;
        this.registroMovilidad = registroMovilidad;
        this.tipoParticipante = tipoParticipante;
        this.tipoMovilidad = tipoMovilidad;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.participante = participante;
        this.programaEducativo = programaEducativo;
        this.periodoEscolarCursado = periodoEscolarCursado;
        this.cuatrimestreCursado = cuatrimestreCursado;
        this.institucionOrganizacion = institucionOrganizacion;
        this.proyecto = proyecto;
        this.descripcion = descripcion;
        this.presEst = presEst;
        this.presFed = presFed;
        this.capDer = capDer;
        this.ingPropios = ingPropios;
        this.ingExtra = ingExtra;
        this.descripcionIngExt = descripcionIngExt;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getRegistroMovilidad() {
        return registroMovilidad;
    }

    public void setRegistroMovilidad(String registroMovilidad) {
        this.registroMovilidad = registroMovilidad;
    }

    public String getTipoParticipante() {
        return tipoParticipante;
    }

    public void setTipoParticipante(String tipoParticipante) {
        this.tipoParticipante = tipoParticipante;
    }

    public String getTipoMovilidad() {
        return tipoMovilidad;
    }

    public void setTipoMovilidad(String tipoMovilidad) {
        this.tipoMovilidad = tipoMovilidad;
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

    public String getParticipante() {
        return participante;
    }

    public void setParticipante(String participante) {
        this.participante = participante;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getPeriodoEscolarCursado() {
        return periodoEscolarCursado;
    }

    public void setPeriodoEscolarCursado(int periodoEscolarCursado) {
        this.periodoEscolarCursado = periodoEscolarCursado;
    }

    public String getCuatrimestreCursado() {
        return cuatrimestreCursado;
    }

    public void setCuatrimestreCursado(String cuatrimestreCursado) {
        this.cuatrimestreCursado = cuatrimestreCursado;
    }

    public String getInstitucionOrganizacion() {
        return institucionOrganizacion;
    }

    public void setInstitucionOrganizacion(String institucionOrganizacion) {
        this.institucionOrganizacion = institucionOrganizacion;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPresEst() {
        return presEst;
    }

    public void setPresEst(BigDecimal presEst) {
        this.presEst = presEst;
    }

    public BigDecimal getPresFed() {
        return presFed;
    }

    public void setPresFed(BigDecimal presFed) {
        this.presFed = presFed;
    }

    public BigDecimal getCapDer() {
        return capDer;
    }

    public void setCapDer(BigDecimal capDer) {
        this.capDer = capDer;
    }

    public BigDecimal getIngPropios() {
        return ingPropios;
    }

    public void setIngPropios(BigDecimal ingPropios) {
        this.ingPropios = ingPropios;
    }

    public BigDecimal getIngExtra() {
        return ingExtra;
    }

    public void setIngExtra(BigDecimal ingExtra) {
        this.ingExtra = ingExtra;
    }

    public String getDescripcionIngExt() {
        return descripcionIngExt;
    }

    public void setDescripcionIngExt(String descripcionIngExt) {
        this.descripcionIngExt = descripcionIngExt;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public ProgramasMovilidad getProgramaMovilidad() {
        return programaMovilidad;
    }

    public void setProgramaMovilidad(ProgramasMovilidad programaMovilidad) {
        this.programaMovilidad = programaMovilidad;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @XmlTransient
    public List<RegistroMovilidadDocente> getRegistroMovilidadDocenteList() {
        return registroMovilidadDocenteList;
    }

    public void setRegistroMovilidadDocenteList(List<RegistroMovilidadDocente> registroMovilidadDocenteList) {
        this.registroMovilidadDocenteList = registroMovilidadDocenteList;
    }

    @XmlTransient
    public List<RegistroMovilidadEstudiante> getRegistroMovilidadEstudianteList() {
        return registroMovilidadEstudianteList;
    }

    public void setRegistroMovilidadEstudianteList(List<RegistroMovilidadEstudiante> registroMovilidadEstudianteList) {
        this.registroMovilidadEstudianteList = registroMovilidadEstudianteList;
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
        if (!(object instanceof RegistrosMovilidad)) {
            return false;
        }
        RegistrosMovilidad other = (RegistrosMovilidad) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad[ registro=" + registro + " ]";
    }
    
}
