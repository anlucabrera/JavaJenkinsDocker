/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "desercion_por_estudiante", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesercionPorEstudiante.findAll", query = "SELECT d FROM DesercionPorEstudiante d")
    , @NamedQuery(name = "DesercionPorEstudiante.findByCveBaja", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.cveBaja = :cveBaja")
    , @NamedQuery(name = "DesercionPorEstudiante.findByCiclo", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.ciclo = :ciclo")
    , @NamedQuery(name = "DesercionPorEstudiante.findByCuatrimestre", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "DesercionPorEstudiante.findByPeriodo", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.periodo = :periodo")
    , @NamedQuery(name = "DesercionPorEstudiante.findByMatricula", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.matricula = :matricula")
    , @NamedQuery(name = "DesercionPorEstudiante.findByNombre", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "DesercionPorEstudiante.findByGenero", query = "SELECT d FROM DesercionPorEstudiante d WHERE d.genero = :genero")})
public class DesercionPorEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_baja")
    private Integer cveBaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private short cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "genero")
    private Character genero;
    @JoinColumn(name = "causa_baja", referencedColumnName = "cve_causa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BajasCausa causaBaja;
    @JoinColumn(name = "tipo_baja", referencedColumnName = "tipo_baja")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BajasTipo tipoBaja;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Generaciones generacion;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos siglas;

    public DesercionPorEstudiante() {
    }

    public DesercionPorEstudiante(Integer cveBaja) {
        this.cveBaja = cveBaja;
    }

    public DesercionPorEstudiante(Integer cveBaja, int ciclo, short cuatrimestre, int periodo, String matricula, String nombre, Character genero) {
        this.cveBaja = cveBaja;
        this.ciclo = ciclo;
        this.cuatrimestre = cuatrimestre;
        this.periodo = periodo;
        this.matricula = matricula;
        this.nombre = nombre;
        this.genero = genero;
    }

    public Integer getCveBaja() {
        return cveBaja;
    }

    public void setCveBaja(Integer cveBaja) {
        this.cveBaja = cveBaja;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public short getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(short cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
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

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public BajasCausa getCausaBaja() {
        return causaBaja;
    }

    public void setCausaBaja(BajasCausa causaBaja) {
        this.causaBaja = causaBaja;
    }

    public BajasTipo getTipoBaja() {
        return tipoBaja;
    }

    public void setTipoBaja(BajasTipo tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    public Generaciones getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public ProgramasEducativos getSiglas() {
        return siglas;
    }

    public void setSiglas(ProgramasEducativos siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveBaja != null ? cveBaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesercionPorEstudiante)) {
            return false;
        }
        DesercionPorEstudiante other = (DesercionPorEstudiante) object;
        if ((this.cveBaja == null && other.cveBaja != null) || (this.cveBaja != null && !this.cveBaja.equals(other.cveBaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.DesercionPorEstudiante[ cveBaja=" + cveBaja + " ]";
    }
    
}
