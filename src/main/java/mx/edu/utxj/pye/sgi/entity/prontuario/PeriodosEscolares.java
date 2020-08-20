/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "periodos_escolares", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodosEscolares.findAll", query = "SELECT p FROM PeriodosEscolares p")
    , @NamedQuery(name = "PeriodosEscolares.findByPeriodo", query = "SELECT p FROM PeriodosEscolares p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PeriodosEscolares.findByAnio", query = "SELECT p FROM PeriodosEscolares p WHERE p.anio = :anio")
    , @NamedQuery(name = "PeriodosEscolares.findByTipo", query = "SELECT p FROM PeriodosEscolares p WHERE p.tipo = :tipo")})
public class PeriodosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "periodo")
    private Integer periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private int anio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 76)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<MovilidadAcademica> movilidadAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<EducacionContinua> educacionContinuaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<EquiposComputo> equiposComputoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private PeriodoEscolarFechas periodoEscolarFechas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<AprovechamientoEscolar> aprovechamientoEscolarList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<Becas> becasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<ResultadosExani> resultadosExaniList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<EquiposComputoInternet> equiposComputoInternetList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<DistribucionAulas> distribucionAulasList;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclo;
    @JoinColumn(name = "mes_fin", referencedColumnName = "numero")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Meses mesFin;
    @JoinColumn(name = "mes_inicio", referencedColumnName = "numero")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Meses mesInicio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<FormacionIntegral> formacionIntegralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<ServiciosTecnologicosEncuestasSatisfaccion> serviciosTecnologicosEncuestasSatisfaccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<MovilidadDocente> movilidadDocenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<IngresosServEstTec> ingresosServEstTecList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<DesercionHistorico> desercionHistoricoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<CapacidadInstalada> capacidadInstaladaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<MatriculaProgramaPeriodoCuatri> matriculaProgramaPeriodoCuatriList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<MatriculaProgramaPeriodoGenero> matriculaProgramaPeriodoGeneroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<CalificacionesCuatrimestre> calificacionesCuatrimestreList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccionEgresadosServicioProporcionado> encuestaSatisfaccionEgresadosServicioProporcionadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodosEscolares", fetch = FetchType.LAZY)
    private List<ServiciosEnfermeria> serviciosEnfermeriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodo", fetch = FetchType.LAZY)
    private List<AcervoBibliografico> acervoBibliograficoList;

    public PeriodosEscolares() {
    }

    public PeriodosEscolares(Integer periodo) {
        this.periodo = periodo;
    }

    public PeriodosEscolares(Integer periodo, int anio, String tipo) {
        this.periodo = periodo;
        this.anio = anio;
        this.tipo = tipo;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
    public List<EquiposComputo> getEquiposComputoList() {
        return equiposComputoList;
    }

    public void setEquiposComputoList(List<EquiposComputo> equiposComputoList) {
        this.equiposComputoList = equiposComputoList;
    }

    public PeriodoEscolarFechas getPeriodoEscolarFechas() {
        return periodoEscolarFechas;
    }

    public void setPeriodoEscolarFechas(PeriodoEscolarFechas periodoEscolarFechas) {
        this.periodoEscolarFechas = periodoEscolarFechas;
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
    public List<ResultadosExani> getResultadosExaniList() {
        return resultadosExaniList;
    }

    public void setResultadosExaniList(List<ResultadosExani> resultadosExaniList) {
        this.resultadosExaniList = resultadosExaniList;
    }

    @XmlTransient
    public List<EquiposComputoInternet> getEquiposComputoInternetList() {
        return equiposComputoInternetList;
    }

    public void setEquiposComputoInternetList(List<EquiposComputoInternet> equiposComputoInternetList) {
        this.equiposComputoInternetList = equiposComputoInternetList;
    }

    @XmlTransient
    public List<DistribucionAulas> getDistribucionAulasList() {
        return distribucionAulasList;
    }

    public void setDistribucionAulasList(List<DistribucionAulas> distribucionAulasList) {
        this.distribucionAulasList = distribucionAulasList;
    }

    public CiclosEscolares getCiclo() {
        return ciclo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
    }

    public Meses getMesFin() {
        return mesFin;
    }

    public void setMesFin(Meses mesFin) {
        this.mesFin = mesFin;
    }

    public Meses getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Meses mesInicio) {
        this.mesInicio = mesInicio;
    }

    @XmlTransient
    public List<FormacionIntegral> getFormacionIntegralList() {
        return formacionIntegralList;
    }

    public void setFormacionIntegralList(List<FormacionIntegral> formacionIntegralList) {
        this.formacionIntegralList = formacionIntegralList;
    }

    @XmlTransient
    public List<ServiciosTecnologicosEncuestasSatisfaccion> getServiciosTecnologicosEncuestasSatisfaccionList() {
        return serviciosTecnologicosEncuestasSatisfaccionList;
    }

    public void setServiciosTecnologicosEncuestasSatisfaccionList(List<ServiciosTecnologicosEncuestasSatisfaccion> serviciosTecnologicosEncuestasSatisfaccionList) {
        this.serviciosTecnologicosEncuestasSatisfaccionList = serviciosTecnologicosEncuestasSatisfaccionList;
    }

    @XmlTransient
    public List<MovilidadDocente> getMovilidadDocenteList() {
        return movilidadDocenteList;
    }

    public void setMovilidadDocenteList(List<MovilidadDocente> movilidadDocenteList) {
        this.movilidadDocenteList = movilidadDocenteList;
    }

    @XmlTransient
    public List<IngresosServEstTec> getIngresosServEstTecList() {
        return ingresosServEstTecList;
    }

    public void setIngresosServEstTecList(List<IngresosServEstTec> ingresosServEstTecList) {
        this.ingresosServEstTecList = ingresosServEstTecList;
    }

    @XmlTransient
    public List<DesercionHistorico> getDesercionHistoricoList() {
        return desercionHistoricoList;
    }

    public void setDesercionHistoricoList(List<DesercionHistorico> desercionHistoricoList) {
        this.desercionHistoricoList = desercionHistoricoList;
    }

    @XmlTransient
    public List<CapacidadInstalada> getCapacidadInstaladaList() {
        return capacidadInstaladaList;
    }

    public void setCapacidadInstaladaList(List<CapacidadInstalada> capacidadInstaladaList) {
        this.capacidadInstaladaList = capacidadInstaladaList;
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
    public List<EncuestaSatisfaccionEgresadosServicioProporcionado> getEncuestaSatisfaccionEgresadosServicioProporcionadoList() {
        return encuestaSatisfaccionEgresadosServicioProporcionadoList;
    }

    public void setEncuestaSatisfaccionEgresadosServicioProporcionadoList(List<EncuestaSatisfaccionEgresadosServicioProporcionado> encuestaSatisfaccionEgresadosServicioProporcionadoList) {
        this.encuestaSatisfaccionEgresadosServicioProporcionadoList = encuestaSatisfaccionEgresadosServicioProporcionadoList;
    }

    @XmlTransient
    public List<ServiciosEnfermeria> getServiciosEnfermeriaList() {
        return serviciosEnfermeriaList;
    }

    public void setServiciosEnfermeriaList(List<ServiciosEnfermeria> serviciosEnfermeriaList) {
        this.serviciosEnfermeriaList = serviciosEnfermeriaList;
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
        hash += (periodo != null ? periodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodosEscolares)) {
            return false;
        }
        PeriodosEscolares other = (PeriodosEscolares) object;
        if ((this.periodo == null && other.periodo != null) || (this.periodo != null && !this.periodo.equals(other.periodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares[ periodo=" + periodo + " ]";
    }
    
}
