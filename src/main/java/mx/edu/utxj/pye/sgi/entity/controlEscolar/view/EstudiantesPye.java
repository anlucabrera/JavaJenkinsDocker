/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar.view;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "estudiantes_pye", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstudiantesPye.findAll", query = "SELECT e FROM EstudiantesPye e")
    , @NamedQuery(name = "EstudiantesPye.findByIdEstudiante", query = "SELECT e FROM EstudiantesPye e WHERE e.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "EstudiantesPye.findByMatricula", query = "SELECT e FROM EstudiantesPye e WHERE e.matricula = :matricula")
    , @NamedQuery(name = "EstudiantesPye.findByPeriodo", query = "SELECT e FROM EstudiantesPye e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "EstudiantesPye.findByNombre", query = "SELECT e FROM EstudiantesPye e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EstudiantesPye.findByAPaterno", query = "SELECT e FROM EstudiantesPye e WHERE e.aPaterno = :aPaterno")
    , @NamedQuery(name = "EstudiantesPye.findByAMaterno", query = "SELECT e FROM EstudiantesPye e WHERE e.aMaterno = :aMaterno")
    , @NamedQuery(name = "EstudiantesPye.findByCurp", query = "SELECT e FROM EstudiantesPye e WHERE e.curp = :curp")
    , @NamedQuery(name = "EstudiantesPye.findBySexo", query = "SELECT e FROM EstudiantesPye e WHERE e.sexo = :sexo")
    , @NamedQuery(name = "EstudiantesPye.findByCarrera", query = "SELECT e FROM EstudiantesPye e WHERE e.carrera = :carrera")
    , @NamedQuery(name = "EstudiantesPye.findByGrado", query = "SELECT e FROM EstudiantesPye e WHERE e.grado = :grado")
    , @NamedQuery(name = "EstudiantesPye.findByGrupo", query = "SELECT e FROM EstudiantesPye e WHERE e.grupo = :grupo")
    , @NamedQuery(name = "EstudiantesPye.findByDescripcion", query = "SELECT e FROM EstudiantesPye e WHERE e.descripcion = :descripcion")})
public class EstudiantesPye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante")
    private int idEstudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "aPaterno")
    private String aPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "aMaterno")
    private String aMaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carrera")
    private short carrera;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grupo")
    private Character grupo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;

    public EstudiantesPye() {
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAPaterno() {
        return aPaterno;
    }

    public void setAPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getAMaterno() {
        return aMaterno;
    }

    public void setAMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public short getCarrera() {
        return carrera;
    }

    public void setCarrera(short carrera) {
        this.carrera = carrera;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public Character getGrupo() {
        return grupo;
    }

    public void setGrupo(Character grupo) {
        this.grupo = grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
