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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "inscripcion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inscripcion.findAll", query = "SELECT i FROM Inscripcion i")
    , @NamedQuery(name = "Inscripcion.findByIdEstudiante", query = "SELECT i FROM Inscripcion i WHERE i.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "Inscripcion.findByMatricula", query = "SELECT i FROM Inscripcion i WHERE i.matricula = :matricula")
    , @NamedQuery(name = "Inscripcion.findByPeriodo", query = "SELECT i FROM Inscripcion i WHERE i.periodo = :periodo")
    , @NamedQuery(name = "Inscripcion.findByCarrera", query = "SELECT i FROM Inscripcion i WHERE i.carrera = :carrera")
    , @NamedQuery(name = "Inscripcion.findByOpcionIncripcion", query = "SELECT i FROM Inscripcion i WHERE i.opcionIncripcion = :opcionIncripcion")
    , @NamedQuery(name = "Inscripcion.findByFechaAlta", query = "SELECT i FROM Inscripcion i WHERE i.fechaAlta = :fechaAlta")
    , @NamedQuery(name = "Inscripcion.findByTrabajadorInscribe", query = "SELECT i FROM Inscripcion i WHERE i.trabajadorInscribe = :trabajadorInscribe")})
public class Inscripcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private int matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carrera")
    private short carrera;
    @Basic(optional = false)
    @NotNull
    @Column(name = "opcionIncripcion")
    private boolean opcionIncripcion;
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trabajador_inscribe")
    private int trabajadorInscribe;
    @ManyToMany(mappedBy = "inscripcionList")
    private List<Asesoria> asesoriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstudiante")
    private List<Calificacion> calificacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<Baja> bajaList;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante")
    @ManyToOne(optional = false)
    private Aspirante aspirante;
    @JoinColumn(name = "tipo_estudiante", referencedColumnName = "id_tipo_estudiante")
    @ManyToOne(optional = false)
    private TipoEstudiante tipoEstudiante;
    @JoinColumn(name = "grupo", referencedColumnName = "id_grupo")
    @ManyToOne(optional = false)
    private Grupo grupo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "inscripcion")
    private Documentosentregadosestudiante documentosentregadosestudiante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<DocumentoEstudiante> documentoEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<ParticipantesTutoria> participantesTutoriaList;

    public Inscripcion() {
    }

    public Inscripcion(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Inscripcion(Integer idEstudiante, int matricula, int periodo, short carrera, boolean opcionIncripcion, int trabajadorInscribe) {
        this.idEstudiante = idEstudiante;
        this.matricula = matricula;
        this.periodo = periodo;
        this.carrera = carrera;
        this.opcionIncripcion = opcionIncripcion;
        this.trabajadorInscribe = trabajadorInscribe;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public short getCarrera() {
        return carrera;
    }

    public void setCarrera(short carrera) {
        this.carrera = carrera;
    }

    public boolean getOpcionIncripcion() {
        return opcionIncripcion;
    }

    public void setOpcionIncripcion(boolean opcionIncripcion) {
        this.opcionIncripcion = opcionIncripcion;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public int getTrabajadorInscribe() {
        return trabajadorInscribe;
    }

    public void setTrabajadorInscribe(int trabajadorInscribe) {
        this.trabajadorInscribe = trabajadorInscribe;
    }

    @XmlTransient
    public List<Asesoria> getAsesoriaList() {
        return asesoriaList;
    }

    public void setAsesoriaList(List<Asesoria> asesoriaList) {
        this.asesoriaList = asesoriaList;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    @XmlTransient
    public List<Baja> getBajaList() {
        return bajaList;
    }

    public void setBajaList(List<Baja> bajaList) {
        this.bajaList = bajaList;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public TipoEstudiante getTipoEstudiante() {
        return tipoEstudiante;
    }

    public void setTipoEstudiante(TipoEstudiante tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Documentosentregadosestudiante getDocumentosentregadosestudiante() {
        return documentosentregadosestudiante;
    }

    public void setDocumentosentregadosestudiante(Documentosentregadosestudiante documentosentregadosestudiante) {
        this.documentosentregadosestudiante = documentosentregadosestudiante;
    }

    @XmlTransient
    public List<DocumentoEstudiante> getDocumentoEstudianteList() {
        return documentoEstudianteList;
    }

    public void setDocumentoEstudianteList(List<DocumentoEstudiante> documentoEstudianteList) {
        this.documentoEstudianteList = documentoEstudianteList;
    }

    @XmlTransient
    public List<ParticipantesTutoria> getParticipantesTutoriaList() {
        return participantesTutoriaList;
    }

    public void setParticipantesTutoriaList(List<ParticipantesTutoria> participantesTutoriaList) {
        this.participantesTutoriaList = participantesTutoriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstudiante != null ? idEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inscripcion)) {
            return false;
        }
        Inscripcion other = (Inscripcion) object;
        if ((this.idEstudiante == null && other.idEstudiante != null) || (this.idEstudiante != null && !this.idEstudiante.equals(other.idEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Inscripcion[ idEstudiante=" + idEstudiante + " ]";
    }
    
}
