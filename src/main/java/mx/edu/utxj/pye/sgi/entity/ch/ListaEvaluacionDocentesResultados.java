/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author UTXJ
 */
@Entity
@Table(name = "lista_evaluacion_docentes_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaEvaluacionDocentesResultados.findAll", query = "SELECT l FROM ListaEvaluacionDocentesResultados l")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findById", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.id = :id")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByEvaluacion", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.evaluacion = :evaluacion")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByCveMateria", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.cveMateria = :cveMateria")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByEvaluado", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.evaluado = :evaluado")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByMatricula", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.matricula = :matricula")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByCuatrimestre", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByGrupo", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.grupo = :grupo")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findBySiglas", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.siglas = :siglas")
    , @NamedQuery(name = "ListaEvaluacionDocentesResultados.findByEstatus", query = "SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.estatus = :estatus")})
public class ListaEvaluacionDocentesResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 34)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado")
    private int evaluado;
    @Lob
    @Size(max = 65535)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private int matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private int cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "grupo")
    private String grupo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "siglas")
    private String siglas;
    @Size(max = 10)
    @Column(name = "estatus")
    private String estatus;

    public ListaEvaluacionDocentesResultados() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public int getEvaluado() {
        return evaluado;
    }

    public void setEvaluado(int evaluado) {
        this.evaluado = evaluado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public String toString() {
        return "ListaEvaluacionDocentesResultados{" + "id=" + id + ", evaluacion=" + evaluacion + ", cveMateria=" + cveMateria + ", evaluado=" + evaluado + ", nombre=" + nombre + ", matricula=" + matricula + ", cuatrimestre=" + cuatrimestre + ", grupo=" + grupo + ", siglas=" + siglas + ", estatus=" + estatus + '}';
    }
    
}
