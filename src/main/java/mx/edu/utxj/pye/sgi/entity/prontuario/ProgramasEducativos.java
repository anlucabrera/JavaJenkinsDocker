/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "programas_educativos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEducativos.findAll", query = "SELECT p FROM ProgramasEducativos p")
    , @NamedQuery(name = "ProgramasEducativos.findBySiglas", query = "SELECT p FROM ProgramasEducativos p WHERE p.siglas = :siglas")
    , @NamedQuery(name = "ProgramasEducativos.findByNombre", query = "SELECT p FROM ProgramasEducativos p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "ProgramasEducativos.findByEvaluable", query = "SELECT p FROM ProgramasEducativos p WHERE p.evaluable = :evaluable")
    , @NamedQuery(name = "ProgramasEducativos.findByAnioInicio", query = "SELECT p FROM ProgramasEducativos p WHERE p.anioInicio = :anioInicio")
    , @NamedQuery(name = "ProgramasEducativos.findByAnioFin", query = "SELECT p FROM ProgramasEducativos p WHERE p.anioFin = :anioFin")
    , @NamedQuery(name = "ProgramasEducativos.findByActivo", query = "SELECT p FROM ProgramasEducativos p WHERE p.activo = :activo")})
public class ProgramasEducativos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluable")
    private boolean evaluable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_inicio")
    @Temporal(TemporalType.DATE)
    private Date anioInicio;
    @Column(name = "anio_fin")
    @Temporal(TemporalType.DATE)
    private Date anioFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<MovilidadAcademica> movilidadAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<EducacionContinua> educacionContinuaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programa")
    private List<ProgramasEducativosOrganismosEvaluadores> programasEducativosOrganismosEvaluadoresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carreraActual")
    private List<ProgramasPertinentesCalidad> programasPertinentesCalidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carreraInicial")
    private List<ProgramasPertinentesCalidad> programasPertinentesCalidadList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<EncuestaSatisfaccion> encuestaSatisfaccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<DesagregadosProgramas> desagregadosProgramasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<ActividadEgresado> actividadEgresadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<AprovechamientoEscolar> aprovechamientoEscolarList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<Becas> becasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programaEducativo")
    private List<BolsaTrabajoContratados> bolsaTrabajoContratadosList;
    @JoinColumn(name = "nivel", referencedColumnName = "nivel")
    @ManyToOne(optional = false)
    private ProgramasEducativosNiveles nivel;
    @JoinColumn(name = "area", referencedColumnName = "area")
    @ManyToOne(optional = false)
    private ProgramasEducativosAreas area;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<EncuestaSatisfaccionEmpleadoresPece> encuestaSatisfaccionEmpleadoresPeceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<FormacionIntegral> formacionIntegralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<EficienciaTerminalTasaTitulacion> eficienciaTerminalTasaTitulacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<DesercionPorEstudiante> desercionPorEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<DesercionHistorico> desercionHistoricoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<MatriculaProgramaPeriodoCuatri> matriculaProgramaPeriodoCuatriList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<MatriculaProgramaPeriodoGenero> matriculaProgramaPeriodoGeneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<CalificacionesCuatrimestre> calificacionesCuatrimestreList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siglas")
    private List<NivelIngreso> nivelIngresoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programasEducativos")
    private List<EncuestaSatisfaccionEgresadosPece> encuestaSatisfaccionEgresadosPeceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programaEducativo")
    private List<AcervoBibliografico> acervoBibliograficoList;

    public ProgramasEducativos() {
    }

    public ProgramasEducativos(String siglas) {
        this.siglas = siglas;
    }

    public ProgramasEducativos(String siglas, String nombre, boolean evaluable, Date anioInicio, boolean activo) {
        this.siglas = siglas;
        this.nombre = nombre;
        this.evaluable = evaluable;
        this.anioInicio = anioInicio;
        this.activo = activo;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEvaluable() {
        return evaluable;
    }

    public void setEvaluable(boolean evaluable) {
        this.evaluable = evaluable;
    }

    public Date getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(Date anioInicio) {
        this.anioInicio = anioInicio;
    }

    public Date getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(Date anioFin) {
        this.anioFin = anioFin;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<MovilidadAcademica> getMovilidadAcademicaList() {
        return movilidadAcademicaList;
    }

    public void setMovilidadAcademicaList(List<MovilidadAcademica> movilidadAcademicaList) {
        this.movilidadAcademicaList = movilidadAcademicaList;
    }

    @XmlTransient
    public List<EducacionContinua> getEducacionContinuaList() {
        return educacionContinuaList;
    }

    public void setEducacionContinuaList(List<EducacionContinua> educacionContinuaList) {
        this.educacionContinuaList = educacionContinuaList;
    }

    @XmlTransient
    public List<ProgramasEducativosOrganismosEvaluadores> getProgramasEducativosOrganismosEvaluadoresList() {
        return programasEducativosOrganismosEvaluadoresList;
    }

    public void setProgramasEducativosOrganismosEvaluadoresList(List<ProgramasEducativosOrganismosEvaluadores> programasEducativosOrganismosEvaluadoresList) {
        this.programasEducativosOrganismosEvaluadoresList = programasEducativosOrganismosEvaluadoresList;
    }

    @XmlTransient
    public List<ProgramasPertinentesCalidad> getProgramasPertinentesCalidadList() {
        return programasPertinentesCalidadList;
    }

    public void setProgramasPertinentesCalidadList(List<ProgramasPertinentesCalidad> programasPertinentesCalidadList) {
        this.programasPertinentesCalidadList = programasPertinentesCalidadList;
    }

    @XmlTransient
    public List<ProgramasPertinentesCalidad> getProgramasPertinentesCalidadList1() {
        return programasPertinentesCalidadList1;
    }

    public void setProgramasPertinentesCalidadList1(List<ProgramasPertinentesCalidad> programasPertinentesCalidadList1) {
        this.programasPertinentesCalidadList1 = programasPertinentesCalidadList1;
    }

    @XmlTransient
    public List<EncuestaSatisfaccion> getEncuestaSatisfaccionList() {
        return encuestaSatisfaccionList;
    }

    public void setEncuestaSatisfaccionList(List<EncuestaSatisfaccion> encuestaSatisfaccionList) {
        this.encuestaSatisfaccionList = encuestaSatisfaccionList;
    }

    @XmlTransient
    public List<DesagregadosProgramas> getDesagregadosProgramasList() {
        return desagregadosProgramasList;
    }

    public void setDesagregadosProgramasList(List<DesagregadosProgramas> desagregadosProgramasList) {
        this.desagregadosProgramasList = desagregadosProgramasList;
    }

    @XmlTransient
    public List<ActividadEgresado> getActividadEgresadoList() {
        return actividadEgresadoList;
    }

    public void setActividadEgresadoList(List<ActividadEgresado> actividadEgresadoList) {
        this.actividadEgresadoList = actividadEgresadoList;
    }

    @XmlTransient
    public List<AprovechamientoEscolar> getAprovechamientoEscolarList() {
        return aprovechamientoEscolarList;
    }

    public void setAprovechamientoEscolarList(List<AprovechamientoEscolar> aprovechamientoEscolarList) {
        this.aprovechamientoEscolarList = aprovechamientoEscolarList;
    }

    @XmlTransient
    public List<Becas> getBecasList() {
        return becasList;
    }

    public void setBecasList(List<Becas> becasList) {
        this.becasList = becasList;
    }

    @XmlTransient
    public List<BolsaTrabajoContratados> getBolsaTrabajoContratadosList() {
        return bolsaTrabajoContratadosList;
    }

    public void setBolsaTrabajoContratadosList(List<BolsaTrabajoContratados> bolsaTrabajoContratadosList) {
        this.bolsaTrabajoContratadosList = bolsaTrabajoContratadosList;
    }

    public ProgramasEducativosNiveles getNivel() {
        return nivel;
    }

    public void setNivel(ProgramasEducativosNiveles nivel) {
        this.nivel = nivel;
    }

    public ProgramasEducativosAreas getArea() {
        return area;
    }

    public void setArea(ProgramasEducativosAreas area) {
        this.area = area;
    }

    @XmlTransient
    public List<EncuestaSatisfaccionEmpleadoresPece> getEncuestaSatisfaccionEmpleadoresPeceList() {
        return encuestaSatisfaccionEmpleadoresPeceList;
    }

    public void setEncuestaSatisfaccionEmpleadoresPeceList(List<EncuestaSatisfaccionEmpleadoresPece> encuestaSatisfaccionEmpleadoresPeceList) {
        this.encuestaSatisfaccionEmpleadoresPeceList = encuestaSatisfaccionEmpleadoresPeceList;
    }

    @XmlTransient
    public List<FormacionIntegral> getFormacionIntegralList() {
        return formacionIntegralList;
    }

    public void setFormacionIntegralList(List<FormacionIntegral> formacionIntegralList) {
        this.formacionIntegralList = formacionIntegralList;
    }

    @XmlTransient
    public List<EficienciaTerminalTasaTitulacion> getEficienciaTerminalTasaTitulacionList() {
        return eficienciaTerminalTasaTitulacionList;
    }

    public void setEficienciaTerminalTasaTitulacionList(List<EficienciaTerminalTasaTitulacion> eficienciaTerminalTasaTitulacionList) {
        this.eficienciaTerminalTasaTitulacionList = eficienciaTerminalTasaTitulacionList;
    }

    @XmlTransient
    public List<DesercionPorEstudiante> getDesercionPorEstudianteList() {
        return desercionPorEstudianteList;
    }

    public void setDesercionPorEstudianteList(List<DesercionPorEstudiante> desercionPorEstudianteList) {
        this.desercionPorEstudianteList = desercionPorEstudianteList;
    }

    @XmlTransient
    public List<DesercionHistorico> getDesercionHistoricoList() {
        return desercionHistoricoList;
    }

    public void setDesercionHistoricoList(List<DesercionHistorico> desercionHistoricoList) {
        this.desercionHistoricoList = desercionHistoricoList;
    }

    @XmlTransient
    public List<MatriculaProgramaPeriodoCuatri> getMatriculaProgramaPeriodoCuatriList() {
        return matriculaProgramaPeriodoCuatriList;
    }

    public void setMatriculaProgramaPeriodoCuatriList(List<MatriculaProgramaPeriodoCuatri> matriculaProgramaPeriodoCuatriList) {
        this.matriculaProgramaPeriodoCuatriList = matriculaProgramaPeriodoCuatriList;
    }

    @XmlTransient
    public List<MatriculaProgramaPeriodoGenero> getMatriculaProgramaPeriodoGeneroList() {
        return matriculaProgramaPeriodoGeneroList;
    }

    public void setMatriculaProgramaPeriodoGeneroList(List<MatriculaProgramaPeriodoGenero> matriculaProgramaPeriodoGeneroList) {
        this.matriculaProgramaPeriodoGeneroList = matriculaProgramaPeriodoGeneroList;
    }

    @XmlTransient
    public List<CalificacionesCuatrimestre> getCalificacionesCuatrimestreList() {
        return calificacionesCuatrimestreList;
    }

    public void setCalificacionesCuatrimestreList(List<CalificacionesCuatrimestre> calificacionesCuatrimestreList) {
        this.calificacionesCuatrimestreList = calificacionesCuatrimestreList;
    }

    @XmlTransient
    public List<NivelIngreso> getNivelIngresoList() {
        return nivelIngresoList;
    }

    public void setNivelIngresoList(List<NivelIngreso> nivelIngresoList) {
        this.nivelIngresoList = nivelIngresoList;
    }

    @XmlTransient
    public List<EncuestaSatisfaccionEgresadosPece> getEncuestaSatisfaccionEgresadosPeceList() {
        return encuestaSatisfaccionEgresadosPeceList;
    }

    public void setEncuestaSatisfaccionEgresadosPeceList(List<EncuestaSatisfaccionEgresadosPece> encuestaSatisfaccionEgresadosPeceList) {
        this.encuestaSatisfaccionEgresadosPeceList = encuestaSatisfaccionEgresadosPeceList;
    }

    @XmlTransient
    public List<AcervoBibliografico> getAcervoBibliograficoList() {
        return acervoBibliograficoList;
    }

    public void setAcervoBibliograficoList(List<AcervoBibliografico> acervoBibliograficoList) {
        this.acervoBibliograficoList = acervoBibliograficoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siglas != null ? siglas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEducativos)) {
            return false;
        }
        ProgramasEducativos other = (ProgramasEducativos) object;
        if ((this.siglas == null && other.siglas != null) || (this.siglas != null && !this.siglas.equals(other.siglas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos[ siglas=" + siglas + " ]";
    }
    
}
