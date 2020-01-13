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
@Table(name = "vista_total_alumnos_carrera_pye", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findAll", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findById", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.id = :id")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findByClaveCarrera", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.claveCarrera = :claveCarrera")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findByAbreviatura", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.abreviatura = :abreviatura")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findByTotalMatricula", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.totalMatricula = :totalMatricula")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findByGrado", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.grado = :grado")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findByNumeroNomina", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.numeroNomina = :numeroNomina")
    , @NamedQuery(name = "VistaTotalAlumnosCarreraPye.findByPeriodo", query = "SELECT v FROM VistaTotalAlumnosCarreraPye v WHERE v.periodo = :periodo")})
public class VistaTotalAlumnosCarreraPye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 105)
    @Id
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "claveCarrera")
    private int claveCarrera;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "totalMatricula")
    private Integer totalMatricula;
    @Column(name = "grado")
    private Short grado;
    @Size(max = 20)
    @Column(name = "numeroNomina")
    private String numeroNomina;
    @Column(name = "periodo")
    private Integer periodo;

    public VistaTotalAlumnosCarreraPye() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(int claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Integer getTotalMatricula() {
        return totalMatricula;
    }

    public void setTotalMatricula(Integer totalMatricula) {
        this.totalMatricula = totalMatricula;
    }

    public Short getGrado() {
        return grado;
    }

    public void setGrado(Short grado) {
        this.grado = grado;
    }

    public String getNumeroNomina() {
        return numeroNomina;
    }

    public void setNumeroNomina(String numeroNomina) {
        this.numeroNomina = numeroNomina;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }
    
}
