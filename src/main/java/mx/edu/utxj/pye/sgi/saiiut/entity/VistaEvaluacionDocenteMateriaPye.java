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
@Table(name = "vista_evaluacion_docente_materia_pye", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findAll", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findById", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.id = :id")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByPeriodo", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.periodo = :periodo")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByMatricula", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByNombre", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByApellidoPat", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.apellidoPat = :apellidoPat")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByApellidoMat", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.apellidoMat = :apellidoMat")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findBySiglas", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.siglas = :siglas")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByNombreCarrera", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.nombreCarrera = :nombreCarrera")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByCveMateria", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.cveMateria = :cveMateria")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByNombreMateria", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.nombreMateria = :nombreMateria")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByCveMaestro", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.cveMaestro = :cveMaestro")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByNumeroNomina", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.numeroNomina = :numeroNomina")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByNombreDocente", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.nombreDocente = :nombreDocente")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByApellidoPDocente", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.apellidoPDocente = :apellidoPDocente")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByApellidoMDocente", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.apellidoMDocente = :apellidoMDocente")
    , @NamedQuery(name = "VistaEvaluacionDocenteMateriaPye.findByNombreCompletoDocente", query = "SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.nombreCompletoDocente = :nombreCompletoDocente")})
public class VistaEvaluacionDocenteMateriaPye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 35)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Size(max = 15)
    @Column(name = "matricula")
    private String matricula;
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
    @Size(max = 20)
    @Column(name = "siglas")
    private String siglas;
    @Size(max = 221)
    @Column(name = "nombreCarrera")
    private String nombreCarrera;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 125)
    @Column(name = "nombreMateria")
    private String nombreMateria;
    @Column(name = "cve_maestro")
    private Integer cveMaestro;
    @Size(max = 20)
    @Column(name = "numeroNomina")
    private String numeroNomina;
    @Size(max = 60)
    @Column(name = "nombreDocente")
    private String nombreDocente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "apellidoPDocente")
    private String apellidoPDocente;
    @Size(max = 40)
    @Column(name = "apellidoMDocente")
    private String apellidoMDocente;
    @Size(max = 142)
    @Column(name = "nombreCompletoDocente")
    private String nombreCompletoDocente;

    public VistaEvaluacionDocenteMateriaPye() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public Integer getCveMaestro() {
        return cveMaestro;
    }

    public void setCveMaestro(Integer cveMaestro) {
        this.cveMaestro = cveMaestro;
    }

    public String getNumeroNomina() {
        return numeroNomina;
    }

    public void setNumeroNomina(String numeroNomina) {
        this.numeroNomina = numeroNomina;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public String getApellidoPDocente() {
        return apellidoPDocente;
    }

    public void setApellidoPDocente(String apellidoPDocente) {
        this.apellidoPDocente = apellidoPDocente;
    }

    public String getApellidoMDocente() {
        return apellidoMDocente;
    }

    public void setApellidoMDocente(String apellidoMDocente) {
        this.apellidoMDocente = apellidoMDocente;
    }

    public String getNombreCompletoDocente() {
        return nombreCompletoDocente;
    }

    public void setNombreCompletoDocente(String nombreCompletoDocente) {
        this.nombreCompletoDocente = nombreCompletoDocente;
    }

    @Override
    public String toString() {
        return "VistaEvaluacionDocenteMateriaPye{" + "id=" + id + ", periodo=" + periodo + ", matricula=" + matricula + ", nombre=" + nombre + ", apellidoPat=" + apellidoPat + ", apellidoMat=" + apellidoMat + ", siglas=" + siglas + ", nombreCarrera=" + nombreCarrera + ", cveMateria=" + cveMateria + ", nombreMateria=" + nombreMateria + ", cveMaestro=" + cveMaestro + ", numeroNomina=" + numeroNomina + ", nombreDocente=" + nombreDocente + ", apellidoPDocente=" + apellidoPDocente + ", apellidoMDocente=" + apellidoMDocente + ", nombreCompletoDocente=" + nombreCompletoDocente + '}';
    }

}
