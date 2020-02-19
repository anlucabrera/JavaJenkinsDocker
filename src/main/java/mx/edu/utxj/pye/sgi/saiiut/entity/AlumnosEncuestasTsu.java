/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "alumnos_encuestas_tsu", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlumnosEncuestasTsu.findAll", query = "SELECT a FROM AlumnosEncuestasTsu a")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByMatricula", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.matricula = :matricula")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByCvePeriodo", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByNombre", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByApellidoPat", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.apellidoPat = :apellidoPat")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByApellidoMat", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.apellidoMat = :apellidoMat")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByCurp", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.curp = :curp")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findBySexo", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.sexo = :sexo")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByAbreviatura", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.abreviatura = :abreviatura")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByNombreCarrera", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.nombreCarrera = :nombreCarrera")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByGrado", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.grado = :grado")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByIdGrupo", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.idGrupo = :idGrupo")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByDescripcion", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByNivelE", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.nivelE = :nivelE")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByCveStatus", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.cveStatus = :cveStatus")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByCveCarrera", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByCveMaestro", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.cveMaestro = :cveMaestro")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByNombreTutor", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByApPatTutor", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.apPatTutor = :apPatTutor")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByApMatTutor", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.apMatTutor = :apMatTutor")
    , @NamedQuery(name = "AlumnosEncuestasTsu.findByCveDirector", query = "SELECT a FROM AlumnosEncuestasTsu a WHERE a.cveDirector = :cveDirector")})
public class AlumnosEncuestasTsu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 15)
    @Column(name = "matricula")
    @Id
    private String matricula;
    @Column(name = "cve_periodo")
    private Integer cvePeriodo;
    @Size(max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "apellido_pat")
    private String apellidoPat;
    @Size(max = 40)
    @Column(name = "apellido_mat")
    private String apellidoMat;
    @Size(max = 18)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sexo")
    private Character sexo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Size(max = 200)
    @Column(name = "nombre_carrera")
    private String nombreCarrera;
    @Column(name = "grado")
    private Short grado;
    @Size(max = 5)
    @Column(name = "id_grupo")
    private String idGrupo;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 20)
    @Column(name = "nivel_e")
    private String nivelE;
    @Column(name = "cve_status")
    private Integer cveStatus;
    @Column(name = "cve_carrera")
    private Integer cveCarrera;
    @Column(name = "cve_maestro")
    private Integer cveMaestro;
    @Size(max = 60)
    @Column(name = "nombreTutor")
    private String nombreTutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "apPatTutor")
    private String apPatTutor;
    @Size(max = 40)
    @Column(name = "apMatTutor")
    private String apMatTutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "cveDirector")
    private String cveDirector;

    public AlumnosEncuestasTsu() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getCvePeriodo() {
        return cvePeriodo;
    }

    public void setCvePeriodo(Integer cvePeriodo) {
        this.cvePeriodo = cvePeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getApellidoMat() {
        return apellidoMat;
    }

    public void setApellidoMat(String apellidoMat) {
        this.apellidoMat = apellidoMat;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public Short getGrado() {
        return grado;
    }

    public void setGrado(Short grado) {
        this.grado = grado;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivelE() {
        return nivelE;
    }

    public void setNivelE(String nivelE) {
        this.nivelE = nivelE;
    }

    public Integer getCveStatus() {
        return cveStatus;
    }

    public void setCveStatus(Integer cveStatus) {
        this.cveStatus = cveStatus;
    }

    public Integer getCveCarrera() {
        return cveCarrera;
    }

    public void setCveCarrera(Integer cveCarrera) {
        this.cveCarrera = cveCarrera;
    }

    public Integer getCveMaestro() {
        return cveMaestro;
    }

    public void setCveMaestro(Integer cveMaestro) {
        this.cveMaestro = cveMaestro;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getApPatTutor() {
        return apPatTutor;
    }

    public void setApPatTutor(String apPatTutor) {
        this.apPatTutor = apPatTutor;
    }

    public String getApMatTutor() {
        return apMatTutor;
    }

    public void setApMatTutor(String apMatTutor) {
        this.apMatTutor = apMatTutor;
    }

    public String getCveDirector() {
        return cveDirector;
    }

    public void setCveDirector(String cveDirector) {
        this.cveDirector = cveDirector;
    }
    
}
