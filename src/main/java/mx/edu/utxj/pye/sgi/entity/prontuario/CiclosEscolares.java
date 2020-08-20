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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "ciclos_escolares", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CiclosEscolares.findAll", query = "SELECT c FROM CiclosEscolares c")
    , @NamedQuery(name = "CiclosEscolares.findByCiclo", query = "SELECT c FROM CiclosEscolares c WHERE c.ciclo = :ciclo")
    , @NamedQuery(name = "CiclosEscolares.findByInicio", query = "SELECT c FROM CiclosEscolares c WHERE c.inicio = :inicio")
    , @NamedQuery(name = "CiclosEscolares.findByFin", query = "SELECT c FROM CiclosEscolares c WHERE c.fin = :fin")})
public class CiclosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ciclo")
    private Integer ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.DATE)
    private Date inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    @Temporal(TemporalType.DATE)
    private Date fin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<MovilidadAcademica> movilidadAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<EducacionContinua> educacionContinuaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<EquiposComputo> equiposComputoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cicloEscolar", fetch = FetchType.LAZY)
    private List<ProgramasPertinentesCalidad> programasPertinentesCalidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccion> encuestaSatisfaccionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<SatisfaccionHistorico> satisfaccionHistoricoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<RegistroEventos> registroEventosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<AprovechamientoEscolar> aprovechamientoEscolarList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private CostoPorAlumno costoPorAlumno;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private ResultadosExani resultadosExani;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private EquiposComputoInternet equiposComputoInternet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<DistribucionAulas> distribucionAulasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<PeriodosEscolares> periodosEscolaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccionEmpleadoresPece> encuestaSatisfaccionEmpleadoresPeceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<FormacionIntegral> formacionIntegralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<MovilidadDocente> movilidadDocenteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cicloEscolar", fetch = FetchType.LAZY)
    private List<IngresosServEstTec> ingresosServEstTecList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<DesercionHistorico> desercionHistoricoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<CapacidadInstalada> capacidadInstaladaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<ServiciosTecnologicos> serviciosTecnologicosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<CalificacionesCuatrimestre> calificacionesCuatrimestreList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<CiclosescolaresGeneraciones> ciclosescolaresGeneracionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccionEgresadosServicioProporcionado> encuestaSatisfaccionEgresadosServicioProporcionadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<ServiciosEnfermeria> serviciosEnfermeriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclosEscolares", fetch = FetchType.LAZY)
    private List<EncuestaSatisfaccionEgresadosPece> encuestaSatisfaccionEgresadosPeceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciclo", fetch = FetchType.LAZY)
    private List<AcervoBibliografico> acervoBibliograficoList;

    public CiclosEscolares() {
    }

    public CiclosEscolares(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public CiclosEscolares(Integer ciclo, Date inicio, Date fin) {
        this.ciclo = ciclo;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
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

    @XmlTransient
    public List<ProgramasPertinentesCalidad> getProgramasPertinentesCalidadList() {
        return programasPertinentesCalidadList;
    }

    public void setProgramasPertinentesCalidadList(List<ProgramasPertinentesCalidad> programasPertinentesCalidadList) {
        this.programasPertinentesCalidadList = programasPertinentesCalidadList;
    }

    @XmlTransient
    public List<EncuestaSatisfaccion> getEncuestaSatisfaccionList() {
        return encuestaSatisfaccionList;
    }

    public void setEncuestaSatisfaccionList(List<EncuestaSatisfaccion> encuestaSatisfaccionList) {
        this.encuestaSatisfaccionList = encuestaSatisfaccionList;
    }

    @XmlTransient
    public List<SatisfaccionHistorico> getSatisfaccionHistoricoList() {
        return satisfaccionHistoricoList;
    }

    public void setSatisfaccionHistoricoList(List<SatisfaccionHistorico> satisfaccionHistoricoList) {
        this.satisfaccionHistoricoList = satisfaccionHistoricoList;
    }

    @XmlTransient
    public List<RegistroEventos> getRegistroEventosList() {
        return registroEventosList;
    }

    public void setRegistroEventosList(List<RegistroEventos> registroEventosList) {
        this.registroEventosList = registroEventosList;
    }

    @XmlTransient
    public List<AprovechamientoEscolar> getAprovechamientoEscolarList() {
        return aprovechamientoEscolarList;
    }

    public void setAprovechamientoEscolarList(List<AprovechamientoEscolar> aprovechamientoEscolarList) {
        this.aprovechamientoEscolarList = aprovechamientoEscolarList;
    }

    public CostoPorAlumno getCostoPorAlumno() {
        return costoPorAlumno;
    }

    public void setCostoPorAlumno(CostoPorAlumno costoPorAlumno) {
        this.costoPorAlumno = costoPorAlumno;
    }

    public ResultadosExani getResultadosExani() {
        return resultadosExani;
    }

    public void setResultadosExani(ResultadosExani resultadosExani) {
        this.resultadosExani = resultadosExani;
    }

    public EquiposComputoInternet getEquiposComputoInternet() {
        return equiposComputoInternet;
    }

    public void setEquiposComputoInternet(EquiposComputoInternet equiposComputoInternet) {
        this.equiposComputoInternet = equiposComputoInternet;
    }

    @XmlTransient
    public List<DistribucionAulas> getDistribucionAulasList() {
        return distribucionAulasList;
    }

    public void setDistribucionAulasList(List<DistribucionAulas> distribucionAulasList) {
        this.distribucionAulasList = distribucionAulasList;
    }

    @XmlTransient
    public List<PeriodosEscolares> getPeriodosEscolaresList() {
        return periodosEscolaresList;
    }

    public void setPeriodosEscolaresList(List<PeriodosEscolares> periodosEscolaresList) {
        this.periodosEscolaresList = periodosEscolaresList;
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
    public List<ServiciosTecnologicos> getServiciosTecnologicosList() {
        return serviciosTecnologicosList;
    }

    public void setServiciosTecnologicosList(List<ServiciosTecnologicos> serviciosTecnologicosList) {
        this.serviciosTecnologicosList = serviciosTecnologicosList;
    }

    @XmlTransient
    public List<CalificacionesCuatrimestre> getCalificacionesCuatrimestreList() {
        return calificacionesCuatrimestreList;
    }

    public void setCalificacionesCuatrimestreList(List<CalificacionesCuatrimestre> calificacionesCuatrimestreList) {
        this.calificacionesCuatrimestreList = calificacionesCuatrimestreList;
    }

    @XmlTransient
    public List<CiclosescolaresGeneraciones> getCiclosescolaresGeneracionesList() {
        return ciclosescolaresGeneracionesList;
    }

    public void setCiclosescolaresGeneracionesList(List<CiclosescolaresGeneraciones> ciclosescolaresGeneracionesList) {
        this.ciclosescolaresGeneracionesList = ciclosescolaresGeneracionesList;
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
        hash += (ciclo != null ? ciclo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CiclosEscolares)) {
            return false;
        }
        CiclosEscolares other = (CiclosEscolares) object;
        if ((this.ciclo == null && other.ciclo != null) || (this.ciclo != null && !this.ciclo.equals(other.ciclo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares[ ciclo=" + ciclo + " ]";
    }
    
}
