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
@Table(name = "view_alumnos", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewAlumnos.findAll", query = "SELECT v FROM ViewAlumnos v")
    , @NamedQuery(name = "ViewAlumnos.findByMatricula", query = "SELECT v FROM ViewAlumnos v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "ViewAlumnos.findByCvePeriodo", query = "SELECT v FROM ViewAlumnos v WHERE v.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "ViewAlumnos.findByNombre", query = "SELECT v FROM ViewAlumnos v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "ViewAlumnos.findByApellidoPat", query = "SELECT v FROM ViewAlumnos v WHERE v.apellidoPat = :apellidoPat")
    , @NamedQuery(name = "ViewAlumnos.findByApellidoMat", query = "SELECT v FROM ViewAlumnos v WHERE v.apellidoMat = :apellidoMat")
    , @NamedQuery(name = "ViewAlumnos.findByCurp", query = "SELECT v FROM ViewAlumnos v WHERE v.curp = :curp")
    , @NamedQuery(name = "ViewAlumnos.findBySexo", query = "SELECT v FROM ViewAlumnos v WHERE v.sexo = :sexo")
    , @NamedQuery(name = "ViewAlumnos.findByAbreviatura", query = "SELECT v FROM ViewAlumnos v WHERE v.abreviatura = :abreviatura")
    , @NamedQuery(name = "ViewAlumnos.findByNombreCarrera", query = "SELECT v FROM ViewAlumnos v WHERE v.nombreCarrera = :nombreCarrera")
    , @NamedQuery(name = "ViewAlumnos.findByGrado", query = "SELECT v FROM ViewAlumnos v WHERE v.grado = :grado")
    , @NamedQuery(name = "ViewAlumnos.findByIdGrupo", query = "SELECT v FROM ViewAlumnos v WHERE v.idGrupo = :idGrupo")
    , @NamedQuery(name = "ViewAlumnos.findByDescripcion", query = "SELECT v FROM ViewAlumnos v WHERE v.descripcion = :descripcion")
    , @NamedQuery(name = "ViewAlumnos.findByNivelE", query = "SELECT v FROM ViewAlumnos v WHERE v.nivelE = :nivelE")
    , @NamedQuery(name = "ViewAlumnos.findByCveStatus", query = "SELECT v FROM ViewAlumnos v WHERE v.cveStatus = :cveStatus")
    , @NamedQuery(name = "ViewAlumnos.findByCveCarrera", query = "SELECT v FROM ViewAlumnos v WHERE v.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "ViewAlumnos.findByCveMaestro", query = "SELECT v FROM ViewAlumnos v WHERE v.cveMaestro = :cveMaestro")
    , @NamedQuery(name = "ViewAlumnos.findByNombreTutor", query = "SELECT v FROM ViewAlumnos v WHERE v.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "ViewAlumnos.findByApPatTutor", query = "SELECT v FROM ViewAlumnos v WHERE v.apPatTutor = :apPatTutor")
    , @NamedQuery(name = "ViewAlumnos.findByApMatTutor", query = "SELECT v FROM ViewAlumnos v WHERE v.apMatTutor = :apMatTutor")
    , @NamedQuery(name = "ViewAlumnos.findByNombreDirector", query = "SELECT v FROM ViewAlumnos v WHERE v.nombreDirector = :nombreDirector")
    , @NamedQuery(name = "ViewAlumnos.findByApPatDir", query = "SELECT v FROM ViewAlumnos v WHERE v.apPatDir = :apPatDir")
    , @NamedQuery(name = "ViewAlumnos.findByApMatDir", query = "SELECT v FROM ViewAlumnos v WHERE v.apMatDir = :apMatDir")
    , @NamedQuery(name = "ViewAlumnos.findByCvePersona", query = "SELECT v FROM ViewAlumnos v WHERE v.cvePersona = :cvePersona")})
public class ViewAlumnos implements Serializable {

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
    @Size(max = 60)
    @Column(name = "nombreDirector")
    private String nombreDirector;
    @Size(max = 40)
    @Column(name = "apPatDir")
    private String apPatDir;
    @Size(max = 40)
    @Column(name = "apMatDir")
    private String apMatDir;
    @Column(name = "cve_persona")
    private Integer cvePersona;

    public ViewAlumnos() {
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

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public String getApPatDir() {
        return apPatDir;
    }

    public void setApPatDir(String apPatDir) {
        this.apPatDir = apPatDir;
    }

    public String getApMatDir() {
        return apMatDir;
    }

    public void setApMatDir(String apMatDir) {
        this.apMatDir = apMatDir;
    }

    public Integer getCvePersona() {
        return cvePersona;
    }

    public void setCvePersona(Integer cvePersona) {
        this.cvePersona = cvePersona;
    }
    
}
