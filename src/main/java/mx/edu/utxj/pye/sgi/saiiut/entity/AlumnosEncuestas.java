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
 * @author Planeación
 */
@Entity
@Table(name = "alumnos_encuestas", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlumnosEncuestas.findAll", query = "SELECT a FROM AlumnosEncuestas a")
    , @NamedQuery(name = "AlumnosEncuestas.findByMatricula", query = "SELECT a FROM AlumnosEncuestas a WHERE a.matricula = :matricula")
    , @NamedQuery(name = "AlumnosEncuestas.findByCvePeriodo", query = "SELECT a FROM AlumnosEncuestas a WHERE a.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "AlumnosEncuestas.findByNombre", query = "SELECT a FROM AlumnosEncuestas a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AlumnosEncuestas.findByApellidoPat", query = "SELECT a FROM AlumnosEncuestas a WHERE a.apellidoPat = :apellidoPat")
    , @NamedQuery(name = "AlumnosEncuestas.findByApellidoMat", query = "SELECT a FROM AlumnosEncuestas a WHERE a.apellidoMat = :apellidoMat")
    , @NamedQuery(name = "AlumnosEncuestas.findByCurp", query = "SELECT a FROM AlumnosEncuestas a WHERE a.curp = :curp")
    , @NamedQuery(name = "AlumnosEncuestas.findBySexo", query = "SELECT a FROM AlumnosEncuestas a WHERE a.sexo = :sexo")
    , @NamedQuery(name = "AlumnosEncuestas.findByAbreviatura", query = "SELECT a FROM AlumnosEncuestas a WHERE a.abreviatura = :abreviatura")
    , @NamedQuery(name = "AlumnosEncuestas.findByNombreCarrera", query = "SELECT a FROM AlumnosEncuestas a WHERE a.nombreCarrera = :nombreCarrera")
    , @NamedQuery(name = "AlumnosEncuestas.findByGrado", query = "SELECT a FROM AlumnosEncuestas a WHERE a.grado = :grado")
    , @NamedQuery(name = "AlumnosEncuestas.findByIdGrupo", query = "SELECT a FROM AlumnosEncuestas a WHERE a.idGrupo = :idGrupo")
    , @NamedQuery(name = "AlumnosEncuestas.findByDescripcion", query = "SELECT a FROM AlumnosEncuestas a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "AlumnosEncuestas.findByNivelE", query = "SELECT a FROM AlumnosEncuestas a WHERE a.nivelE = :nivelE")
    , @NamedQuery(name = "AlumnosEncuestas.findByCveStatus", query = "SELECT a FROM AlumnosEncuestas a WHERE a.cveStatus = :cveStatus")
    , @NamedQuery(name = "AlumnosEncuestas.findByCveCarrera", query = "SELECT a FROM AlumnosEncuestas a WHERE a.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "AlumnosEncuestas.findByCveMaestro", query = "SELECT a FROM AlumnosEncuestas a WHERE a.cveMaestro = :cveMaestro")
    , @NamedQuery(name = "AlumnosEncuestas.findByNombreTutor", query = "SELECT a FROM AlumnosEncuestas a WHERE a.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "AlumnosEncuestas.findByApPatTutor", query = "SELECT a FROM AlumnosEncuestas a WHERE a.apPatTutor = :apPatTutor")
    , @NamedQuery(name = "AlumnosEncuestas.findByApMatTutor", query = "SELECT a FROM AlumnosEncuestas a WHERE a.apMatTutor = :apMatTutor")
    , @NamedQuery(name = "AlumnosEncuestas.findByCveDirector", query = "SELECT a FROM AlumnosEncuestas a WHERE a.cveDirector = :cveDirector")})
public class AlumnosEncuestas implements Serializable {

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

    public AlumnosEncuestas() {
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
