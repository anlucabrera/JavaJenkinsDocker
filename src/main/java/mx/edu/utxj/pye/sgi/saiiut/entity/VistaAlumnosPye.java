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
 * @author UTXJ
 */
@Entity
@Table(name = "vista_alumnos_pye", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VistaAlumnosPye.findAll", query = "SELECT v FROM VistaAlumnosPye v")
    , @NamedQuery(name = "VistaAlumnosPye.findByMatricula", query = "SELECT v FROM VistaAlumnosPye v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "VistaAlumnosPye.findByCvePeriodo", query = "SELECT v FROM VistaAlumnosPye v WHERE v.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "VistaAlumnosPye.findByNombre", query = "SELECT v FROM VistaAlumnosPye v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "VistaAlumnosPye.findByApellidoPat", query = "SELECT v FROM VistaAlumnosPye v WHERE v.apellidoPat = :apellidoPat")
    , @NamedQuery(name = "VistaAlumnosPye.findByApellidoMat", query = "SELECT v FROM VistaAlumnosPye v WHERE v.apellidoMat = :apellidoMat")
    , @NamedQuery(name = "VistaAlumnosPye.findByCurp", query = "SELECT v FROM VistaAlumnosPye v WHERE v.curp = :curp")
    , @NamedQuery(name = "VistaAlumnosPye.findBySexo", query = "SELECT v FROM VistaAlumnosPye v WHERE v.sexo = :sexo")
    , @NamedQuery(name = "VistaAlumnosPye.findByAbreviatura", query = "SELECT v FROM VistaAlumnosPye v WHERE v.abreviatura = :abreviatura")
    , @NamedQuery(name = "VistaAlumnosPye.findByNombreCarrera", query = "SELECT v FROM VistaAlumnosPye v WHERE v.nombreCarrera = :nombreCarrera")
    , @NamedQuery(name = "VistaAlumnosPye.findByGrado", query = "SELECT v FROM VistaAlumnosPye v WHERE v.grado = :grado")
    , @NamedQuery(name = "VistaAlumnosPye.findByIdGrupo", query = "SELECT v FROM VistaAlumnosPye v WHERE v.idGrupo = :idGrupo")
    , @NamedQuery(name = "VistaAlumnosPye.findByDescripcion", query = "SELECT v FROM VistaAlumnosPye v WHERE v.descripcion = :descripcion")
    , @NamedQuery(name = "VistaAlumnosPye.findByNivelE", query = "SELECT v FROM VistaAlumnosPye v WHERE v.nivelE = :nivelE")})
public class VistaAlumnosPye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 15)
    @Column(name = "matricula")
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

    public VistaAlumnosPye() {
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

    @Override
    public String toString() {
        return "VistaAlumnosPye{" + "matricula=" + matricula + ", cvePeriodo=" + cvePeriodo + ", nombre=" + nombre + ", apellidoPat=" + apellidoPat + ", apellidoMat=" + apellidoMat + ", curp=" + curp + ", sexo=" + sexo + ", abreviatura=" + abreviatura + ", nombreCarrera=" + nombreCarrera + ", grado=" + grado + ", idGrupo=" + idGrupo + ", descripcion=" + descripcion + ", nivelE=" + nivelE + '}';
    }
    
}
