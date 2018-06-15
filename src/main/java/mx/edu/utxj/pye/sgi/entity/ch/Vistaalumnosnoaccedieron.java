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
@Table(name = "vistaalumnosnoaccedieron", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vistaalumnosnoaccedieron.findAll", query = "SELECT v FROM Vistaalumnosnoaccedieron v")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByMatricula", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByPeriodo", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.periodo = :periodo")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByPe", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.pe = :pe")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByCuatrimestre", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByGrupo", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.grupo = :grupo")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByApellidoPaterno", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByApellidoMaterno", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByNombre", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findBySiglas", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.siglas = :siglas")
    , @NamedQuery(name = "Vistaalumnosnoaccedieron.findByClave", query = "SELECT v FROM Vistaalumnosnoaccedieron v WHERE v.clave = :clave")})
public class Vistaalumnosnoaccedieron implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    @Id
    private int matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pe")
    private int pe;
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
    @Size(min = 1, max = 300)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;

    public Vistaalumnosnoaccedieron() {
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

    public int getPe() {
        return pe;
    }

    public void setPe(int pe) {
        this.pe = pe;
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

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }
    
}
