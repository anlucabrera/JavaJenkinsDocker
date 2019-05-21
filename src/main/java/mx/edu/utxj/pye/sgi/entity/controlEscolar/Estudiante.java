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
@Table(name = "estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByIdEstudiante", query = "SELECT e FROM Estudiante e WHERE e.idEstudiante = :idEstudiante"),
    @NamedQuery(name = "Estudiante.findByMatricula", query = "SELECT e FROM Estudiante e WHERE e.matricula = :matricula"),
    @NamedQuery(name = "Estudiante.findByPeriodo", query = "SELECT e FROM Estudiante e WHERE e.periodo = :periodo"),
    @NamedQuery(name = "Estudiante.findByCarrera", query = "SELECT e FROM Estudiante e WHERE e.carrera = :carrera"),
    @NamedQuery(name = "Estudiante.findByOpcionIncripcion", query = "SELECT e FROM Estudiante e WHERE e.opcionIncripcion = :opcionIncripcion"),
    @NamedQuery(name = "Estudiante.findByFechaAlta", query = "SELECT e FROM Estudiante e WHERE e.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "Estudiante.findByTrabajadorInscribe", query = "SELECT e FROM Estudiante e WHERE e.trabajadorInscribe = :trabajadorInscribe")})
public class Estudiante implements Serializable {

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
    private int carrera;
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
    @ManyToMany(mappedBy = "estudianteList")
    private List<Asesoria> asesoriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<Baja> bajaList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "estudiante1")
    private Documentosentregadosestudiante documentosentregadosestudiante;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante")
    @ManyToOne(optional = false)
    private Aspirante aspirante;
    @JoinColumn(name = "tipo_estudiante", referencedColumnName = "id_tipo_estudiante")
    @ManyToOne(optional = false)
    private TipoEstudiante tipoEstudiante;
    @JoinColumn(name = "grupo", referencedColumnName = "id_grupo")
    @ManyToOne(optional = false)
    private Grupo grupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<DocumentoEstudiante> documentoEstudianteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "estudiante1")
    private Calificaciones calificaciones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<ParticipantesTutoria> participantesTutoriaList;

    public Estudiante() {
    }

    public Estudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Estudiante(Integer idEstudiante, int matricula, int periodo, int carrera, boolean opcionIncripcion, int trabajadorInscribe) {
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

    public int getCarrera() {
        return carrera;
    }

    public void setCarrera(int carrera) {
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
    public List<Baja> getBajaList() {
        return bajaList;
    }

    public void setBajaList(List<Baja> bajaList) {
        this.bajaList = bajaList;
    }

    public Documentosentregadosestudiante getDocumentosentregadosestudiante() {
        return documentosentregadosestudiante;
    }

    public void setDocumentosentregadosestudiante(Documentosentregadosestudiante documentosentregadosestudiante) {
        this.documentosentregadosestudiante = documentosentregadosestudiante;
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

    @XmlTransient
    public List<DocumentoEstudiante> getDocumentoEstudianteList() {
        return documentoEstudianteList;
    }

    public void setDocumentoEstudianteList(List<DocumentoEstudiante> documentoEstudianteList) {
        this.documentoEstudianteList = documentoEstudianteList;
    }

    public Calificaciones getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Calificaciones calificaciones) {
        this.calificaciones = calificaciones;
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
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.idEstudiante == null && other.idEstudiante != null) || (this.idEstudiante != null && !this.idEstudiante.equals(other.idEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante[ idEstudiante=" + idEstudiante + " ]";
    }
    
}
