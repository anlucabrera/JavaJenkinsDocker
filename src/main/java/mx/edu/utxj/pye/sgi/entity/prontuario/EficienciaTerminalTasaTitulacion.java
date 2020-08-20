/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "eficiencia_terminal_tasa_titulacion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findAll", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByEttt", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.eficienciaTerminalTasaTitulacionPK.ettt = :ettt")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByFechaCorte", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.fechaCorte = :fechaCorte")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByGeneracion", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.eficienciaTerminalTasaTitulacionPK.generacion = :generacion")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByPeriodoInicio", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.periodoInicio = :periodoInicio")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByPeriodoFin", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.periodoFin = :periodoFin")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByAlumnosh", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.alumnosh = :alumnosh")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByAlumnosm", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.alumnosm = :alumnosm")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByEgrecorh", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.egrecorh = :egrecorh")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByEgrecorm", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.egrecorm = :egrecorm")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByRezagosh", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.rezagosh = :rezagosh")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByRezagosm", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.rezagosm = :rezagosm")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findBySegcarrerah", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.segcarrerah = :segcarrerah")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findBySegcarreram", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.segcarreram = :segcarreram")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByEgresadosh", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.egresadosh = :egresadosh")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByEgresadosm", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.egresadosm = :egresadosm")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByTituladosh", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.tituladosh = :tituladosh")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByTituladosm", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.tituladosm = :tituladosm")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByRegistradosh", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.registradosh = :registradosh")
    , @NamedQuery(name = "EficienciaTerminalTasaTitulacion.findByRegistradosm", query = "SELECT e FROM EficienciaTerminalTasaTitulacion e WHERE e.registradosm = :registradosm")})
public class EficienciaTerminalTasaTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EficienciaTerminalTasaTitulacionPK eficienciaTerminalTasaTitulacionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_corte")
    @Temporal(TemporalType.DATE)
    private Date fechaCorte;
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
    @Column(name = "alumnosh")
    private short alumnosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alumnosm")
    private short alumnosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egrecorh")
    private short egrecorh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egrecorm")
    private short egrecorm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rezagosh")
    private short rezagosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rezagosm")
    private short rezagosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "segcarrerah")
    private short segcarrerah;
    @Basic(optional = false)
    @NotNull
    @Column(name = "segcarreram")
    private short segcarreram;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egresadosh")
    private short egresadosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egresadosm")
    private short egresadosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tituladosh")
    private short tituladosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tituladosm")
    private short tituladosm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registradosh")
    private short registradosh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registradosm")
    private short registradosm;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Generaciones generaciones;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos siglas;

    public EficienciaTerminalTasaTitulacion() {
    }

    public EficienciaTerminalTasaTitulacion(EficienciaTerminalTasaTitulacionPK eficienciaTerminalTasaTitulacionPK) {
        this.eficienciaTerminalTasaTitulacionPK = eficienciaTerminalTasaTitulacionPK;
    }

    public EficienciaTerminalTasaTitulacion(EficienciaTerminalTasaTitulacionPK eficienciaTerminalTasaTitulacionPK, Date fechaCorte, int periodoInicio, int periodoFin, short alumnosh, short alumnosm, short egrecorh, short egrecorm, short rezagosh, short rezagosm, short segcarrerah, short segcarreram, short egresadosh, short egresadosm, short tituladosh, short tituladosm, short registradosh, short registradosm) {
        this.eficienciaTerminalTasaTitulacionPK = eficienciaTerminalTasaTitulacionPK;
        this.fechaCorte = fechaCorte;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.alumnosh = alumnosh;
        this.alumnosm = alumnosm;
        this.egrecorh = egrecorh;
        this.egrecorm = egrecorm;
        this.rezagosh = rezagosh;
        this.rezagosm = rezagosm;
        this.segcarrerah = segcarrerah;
        this.segcarreram = segcarreram;
        this.egresadosh = egresadosh;
        this.egresadosm = egresadosm;
        this.tituladosh = tituladosh;
        this.tituladosm = tituladosm;
        this.registradosh = registradosh;
        this.registradosm = registradosm;
    }

    public EficienciaTerminalTasaTitulacion(int ettt, short generacion) {
        this.eficienciaTerminalTasaTitulacionPK = new EficienciaTerminalTasaTitulacionPK(ettt, generacion);
    }

    public EficienciaTerminalTasaTitulacionPK getEficienciaTerminalTasaTitulacionPK() {
        return eficienciaTerminalTasaTitulacionPK;
    }

    public void setEficienciaTerminalTasaTitulacionPK(EficienciaTerminalTasaTitulacionPK eficienciaTerminalTasaTitulacionPK) {
        this.eficienciaTerminalTasaTitulacionPK = eficienciaTerminalTasaTitulacionPK;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
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

    public short getAlumnosh() {
        return alumnosh;
    }

    public void setAlumnosh(short alumnosh) {
        this.alumnosh = alumnosh;
    }

    public short getAlumnosm() {
        return alumnosm;
    }

    public void setAlumnosm(short alumnosm) {
        this.alumnosm = alumnosm;
    }

    public short getEgrecorh() {
        return egrecorh;
    }

    public void setEgrecorh(short egrecorh) {
        this.egrecorh = egrecorh;
    }

    public short getEgrecorm() {
        return egrecorm;
    }

    public void setEgrecorm(short egrecorm) {
        this.egrecorm = egrecorm;
    }

    public short getRezagosh() {
        return rezagosh;
    }

    public void setRezagosh(short rezagosh) {
        this.rezagosh = rezagosh;
    }

    public short getRezagosm() {
        return rezagosm;
    }

    public void setRezagosm(short rezagosm) {
        this.rezagosm = rezagosm;
    }

    public short getSegcarrerah() {
        return segcarrerah;
    }

    public void setSegcarrerah(short segcarrerah) {
        this.segcarrerah = segcarrerah;
    }

    public short getSegcarreram() {
        return segcarreram;
    }

    public void setSegcarreram(short segcarreram) {
        this.segcarreram = segcarreram;
    }

    public short getEgresadosh() {
        return egresadosh;
    }

    public void setEgresadosh(short egresadosh) {
        this.egresadosh = egresadosh;
    }

    public short getEgresadosm() {
        return egresadosm;
    }

    public void setEgresadosm(short egresadosm) {
        this.egresadosm = egresadosm;
    }

    public short getTituladosh() {
        return tituladosh;
    }

    public void setTituladosh(short tituladosh) {
        this.tituladosh = tituladosh;
    }

    public short getTituladosm() {
        return tituladosm;
    }

    public void setTituladosm(short tituladosm) {
        this.tituladosm = tituladosm;
    }

    public short getRegistradosh() {
        return registradosh;
    }

    public void setRegistradosh(short registradosh) {
        this.registradosh = registradosh;
    }

    public short getRegistradosm() {
        return registradosm;
    }

    public void setRegistradosm(short registradosm) {
        this.registradosm = registradosm;
    }

    public Generaciones getGeneraciones() {
        return generaciones;
    }

    public void setGeneraciones(Generaciones generaciones) {
        this.generaciones = generaciones;
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
        hash += (eficienciaTerminalTasaTitulacionPK != null ? eficienciaTerminalTasaTitulacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EficienciaTerminalTasaTitulacion)) {
            return false;
        }
        EficienciaTerminalTasaTitulacion other = (EficienciaTerminalTasaTitulacion) object;
        if ((this.eficienciaTerminalTasaTitulacionPK == null && other.eficienciaTerminalTasaTitulacionPK != null) || (this.eficienciaTerminalTasaTitulacionPK != null && !this.eficienciaTerminalTasaTitulacionPK.equals(other.eficienciaTerminalTasaTitulacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EficienciaTerminalTasaTitulacion[ eficienciaTerminalTasaTitulacionPK=" + eficienciaTerminalTasaTitulacionPK + " ]";
    }
    
}
