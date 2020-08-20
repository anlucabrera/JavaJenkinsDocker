/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "eficiencia_terminal_titulacion_registro", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findAll", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByRegistro", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.registro = :registro")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByFechaCorte", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.fechaCorte = :fechaCorte")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByProgramaEducativo", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByPeriodoInicio", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.periodoInicio = :periodoInicio")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByPeriodoFin", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.periodoFin = :periodoFin")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByGeneracion", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByAlumnosh", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.alumnosh = :alumnosh")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByAlumnosm", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.alumnosm = :alumnosm")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByEgrecorh", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.egrecorh = :egrecorh")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByEgrecorm", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.egrecorm = :egrecorm")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByRezagadosh", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.rezagadosh = :rezagadosh")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByRezagadosm", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.rezagadosm = :rezagadosm")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findBySegcarrerah", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.segcarrerah = :segcarrerah")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findBySegcarreram", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.segcarreram = :segcarreram")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByEgresadosh", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.egresadosh = :egresadosh")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByEgresadosm", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.egresadosm = :egresadosm")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByTituladosh", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.tituladosh = :tituladosh")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByTituladosm", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.tituladosm = :tituladosm")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByRegistradosh", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.registradosh = :registradosh")
    , @NamedQuery(name = "EficienciaTerminalTitulacionRegistro.findByRegistradosm", query = "SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.registradosm = :registradosm")})
public class EficienciaTerminalTitulacionRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_corte")
    @Temporal(TemporalType.DATE)
    private Date fechaCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_inicio")
    private int periodoInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_fin")
    private int periodoFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alumnosh")
    private int alumnosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alumnosm")
    private int alumnosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egrecorh")
    private int egrecorh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egrecorm")
    private int egrecorm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rezagadosh")
    private int rezagadosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rezagadosm")
    private int rezagadosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "segcarrerah")
    private int segcarrerah;
    @Basic(optional = false)
    @NotNull
    @Column(name = "segcarreram")
    private int segcarreram;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egresadosh")
    private int egresadosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egresadosm")
    private int egresadosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tituladosh")
    private int tituladosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tituladosm")
    private int tituladosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registradosh")
    private int registradosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registradosm")
    private int registradosm;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public EficienciaTerminalTitulacionRegistro() {
    }

    public EficienciaTerminalTitulacionRegistro(Integer registro) {
        this.registro = registro;
    }

    public EficienciaTerminalTitulacionRegistro(Integer registro, Date fechaCorte, short programaEducativo, int periodoInicio, int periodoFin, short generacion, int alumnosh, int alumnosm, int egrecorh, int egrecorm, int rezagadosh, int rezagadosm, int segcarrerah, int segcarreram, int egresadosh, int egresadosm, int tituladosh, int tituladosm, int registradosh, int registradosm) {
        this.registro = registro;
        this.fechaCorte = fechaCorte;
        this.programaEducativo = programaEducativo;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.generacion = generacion;
        this.alumnosh = alumnosh;
        this.alumnosm = alumnosm;
        this.egrecorh = egrecorh;
        this.egrecorm = egrecorm;
        this.rezagadosh = rezagadosh;
        this.rezagadosm = rezagadosm;
        this.segcarrerah = segcarrerah;
        this.segcarreram = segcarreram;
        this.egresadosh = egresadosh;
        this.egresadosm = egresadosm;
        this.tituladosh = tituladosh;
        this.tituladosm = tituladosm;
        this.registradosh = registradosh;
        this.registradosm = registradosm;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(int periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public int getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(int periodoFin) {
        this.periodoFin = periodoFin;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public int getAlumnosh() {
        return alumnosh;
    }

    public void setAlumnosh(int alumnosh) {
        this.alumnosh = alumnosh;
    }

    public int getAlumnosm() {
        return alumnosm;
    }

    public void setAlumnosm(int alumnosm) {
        this.alumnosm = alumnosm;
    }

    public int getEgrecorh() {
        return egrecorh;
    }

    public void setEgrecorh(int egrecorh) {
        this.egrecorh = egrecorh;
    }

    public int getEgrecorm() {
        return egrecorm;
    }

    public void setEgrecorm(int egrecorm) {
        this.egrecorm = egrecorm;
    }

    public int getRezagadosh() {
        return rezagadosh;
    }

    public void setRezagadosh(int rezagadosh) {
        this.rezagadosh = rezagadosh;
    }

    public int getRezagadosm() {
        return rezagadosm;
    }

    public void setRezagadosm(int rezagadosm) {
        this.rezagadosm = rezagadosm;
    }

    public int getSegcarrerah() {
        return segcarrerah;
    }

    public void setSegcarrerah(int segcarrerah) {
        this.segcarrerah = segcarrerah;
    }

    public int getSegcarreram() {
        return segcarreram;
    }

    public void setSegcarreram(int segcarreram) {
        this.segcarreram = segcarreram;
    }

    public int getEgresadosh() {
        return egresadosh;
    }

    public void setEgresadosh(int egresadosh) {
        this.egresadosh = egresadosh;
    }

    public int getEgresadosm() {
        return egresadosm;
    }

    public void setEgresadosm(int egresadosm) {
        this.egresadosm = egresadosm;
    }

    public int getTituladosh() {
        return tituladosh;
    }

    public void setTituladosh(int tituladosh) {
        this.tituladosh = tituladosh;
    }

    public int getTituladosm() {
        return tituladosm;
    }

    public void setTituladosm(int tituladosm) {
        this.tituladosm = tituladosm;
    }

    public int getRegistradosh() {
        return registradosh;
    }

    public void setRegistradosh(int registradosh) {
        this.registradosh = registradosh;
    }

    public int getRegistradosm() {
        return registradosm;
    }

    public void setRegistradosm(int registradosm) {
        this.registradosm = registradosm;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EficienciaTerminalTitulacionRegistro)) {
            return false;
        }
        EficienciaTerminalTitulacionRegistro other = (EficienciaTerminalTitulacionRegistro) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EficienciaTerminalTitulacionRegistro[ registro=" + registro + " ]";
    }
    
}
