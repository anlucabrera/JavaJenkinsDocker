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
import javax.persistence.Lob;
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
@Table(name = "viewEstudianteAsesorAcademico", schema = "", catalog = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewEstudianteAsesorAcademico.findAll", query = "SELECT v FROM ViewEstudianteAsesorAcademico v")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByNombrePE", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.nombrePE = :nombrePE")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByMatricula", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByNombreAlumno", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.nombreAlumno = :nombreAlumno")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByApellidoPaternoAlumno", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.apellidoPaternoAlumno = :apellidoPaternoAlumno")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByApellidoMaternoAlumno", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.apellidoMaternoAlumno = :apellidoMaternoAlumno")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByNombreAsesorAcademico", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.nombreAsesorAcademico = :nombreAsesorAcademico")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByApellidoPaternoAsesorAcademico", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.apellidoPaternoAsesorAcademico = :apellidoPaternoAsesorAcademico")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByApellidoMaternoAsesorAcademico", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.apellidoMaternoAsesorAcademico = :apellidoMaternoAsesorAcademico")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByNumeroNomina", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.numeroNomina = :numeroNomina")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByCvePeriodo", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByCveMaestro", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.cveMaestro = :cveMaestro")
    , @NamedQuery(name = "ViewEstudianteAsesorAcademico.findByCveDirector", query = "SELECT v FROM ViewEstudianteAsesorAcademico v WHERE v.cveDirector = :cveDirector")})
public class ViewEstudianteAsesorAcademico implements Serializable {

    @Size(max = 224)
    @Column(name = "nombrePE")
    private String nombrePE;
    @Size(max = 15)
    @Column(name = "matricula")
    @Id
    private String matricula;
    @Size(max = 60)
    @Column(name = "nombre_Alumno")
    private String nombreAlumno;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 40)
    @Column(name = "apellidoPaterno_Alumno")
    private String apellidoPaternoAlumno;
    @Size(max = 40)
    @Column(name = "apellidoMaterno_Alumno")
    private String apellidoMaternoAlumno;
    @Size(max = 60)
    @Column(name = "nombre_AsesorAcademico")
    private String nombreAsesorAcademico;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 40)
    @Column(name = "apellidoPaterno_AsesorAcademico")
    private String apellidoPaternoAsesorAcademico;
    @Size(max = 40)
    @Column(name = "apellidoMaterno_AsesorAcademico")
    private String apellidoMaternoAsesorAcademico;
    @Size(max = 20)
    @Column(name = "numero_nomina")
    private String numeroNomina;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "cve_periodo")
    private int cvePeriodo;
    @Lob()
    @Size(max = 2147483647)
    @Column(name = "nombreProyecto")
    private String nombreProyecto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "cveDirector")
    private String cveDirector;
    @Size(max = 60)
    @Column(name = "nombreTutor")
    private String nombreTutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "apellidoPatTutor")
    private String apellidoPatTutor;
    @Size(max = 40)
    @Column(name = "apellidoMatTutor")
    private String apellidoMatTutor;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Size(max = 5)
    @Column(name = "id_grupo")
    private String idGrupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private short grado;
    private static final long serialVersionUID = 1L;
    @Column(name = "cve_maestro")
    private Integer cveMaestro;

    public ViewEstudianteAsesorAcademico() {
    }


    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getApellidoPaternoAlumno() {
        return apellidoPaternoAlumno;
    }

    public void setApellidoPaternoAlumno(String apellidoPaternoAlumno) {
        this.apellidoPaternoAlumno = apellidoPaternoAlumno;
    }

    public String getApellidoMaternoAlumno() {
        return apellidoMaternoAlumno;
    }

    public void setApellidoMaternoAlumno(String apellidoMaternoAlumno) {
        this.apellidoMaternoAlumno = apellidoMaternoAlumno;
    }

    public String getNombreAsesorAcademico() {
        return nombreAsesorAcademico;
    }

    public void setNombreAsesorAcademico(String nombreAsesorAcademico) {
        this.nombreAsesorAcademico = nombreAsesorAcademico;
    }

    public String getApellidoPaternoAsesorAcademico() {
        return apellidoPaternoAsesorAcademico;
    }

    public void setApellidoPaternoAsesorAcademico(String apellidoPaternoAsesorAcademico) {
        this.apellidoPaternoAsesorAcademico = apellidoPaternoAsesorAcademico;
    }

    public String getApellidoMaternoAsesorAcademico() {
        return apellidoMaternoAsesorAcademico;
    }

    public void setApellidoMaternoAsesorAcademico(String apellidoMaternoAsesorAcademico) {
        this.apellidoMaternoAsesorAcademico = apellidoMaternoAsesorAcademico;
    }

    public String getNumeroNomina() {
        return numeroNomina;
    }

    public void setNumeroNomina(String numeroNomina) {
        this.numeroNomina = numeroNomina;
    }

    public int getCvePeriodo() {
        return cvePeriodo;
    }

    public void setCvePeriodo(int cvePeriodo) {
        this.cvePeriodo = cvePeriodo;
    }

    public Integer getCveMaestro() {
        return cveMaestro;
    }

    public void setCveMaestro(Integer cveMaestro) {
        this.cveMaestro = cveMaestro;
    }

    public String getNombrePE() {
        return nombrePE;
    }

    public void setNombrePE(String nombrePE) {
        this.nombrePE = nombrePE;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getCveDirector() {
        return cveDirector;
    }

    public void setCveDirector(String cveDirector) {
        this.cveDirector = cveDirector;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getApellidoPatTutor() {
        return apellidoPatTutor;
    }

    public void setApellidoPatTutor(String apellidoPatTutor) {
        this.apellidoPatTutor = apellidoPatTutor;
    }

    public String getApellidoMatTutor() {
        return apellidoMatTutor;
    }

    public void setApellidoMatTutor(String apellidoMatTutor) {
        this.apellidoMatTutor = apellidoMatTutor;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public short getGrado() {
        return grado;
    }

    public void setGrado(short grado) {
        this.grado = grado;
    }
    
}
