/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "registros", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registros.findAll", query = "SELECT r FROM Registros r")
    , @NamedQuery(name = "Registros.findByRegistro", query = "SELECT r FROM Registros r WHERE r.registro = :registro")
    , @NamedQuery(name = "Registros.findByArea", query = "SELECT r FROM Registros r WHERE r.area = :area")
    , @NamedQuery(name = "Registros.findByFechaRegistro", query = "SELECT r FROM Registros r WHERE r.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "Registros.findByValidado", query = "SELECT r FROM Registros r WHERE r.validado = :validado")})
public class Registros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @ManyToMany(mappedBy = "registrosList", fetch = FetchType.LAZY)
    private List<ActividadesPoa> actividadesPoaList;
    @ManyToMany(mappedBy = "registrosList", fetch = FetchType.LAZY)
    private List<Evidencias> evidenciasList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ProductosAcademicos productosAcademicos;
    @JoinColumn(name = "eje", referencedColumnName = "eje")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EjesRegistro eje;
    @JoinColumn(name = "evento_registro", referencedColumnName = "evento_registro")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventosRegistros eventoRegistro;
    @JoinColumn(name = "tipo", referencedColumnName = "registro_tipo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegistrosTipo tipo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private VisitasIndustriales visitasIndustriales;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ProgramasEstimulos programasEstimulos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private AsesoriasTutoriasMensualPeriodosEscolares asesoriasTutoriasMensualPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ProductosAcademicosPersonal productosAcademicosPersonal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private CuerpacadIntegrantes cuerpacadIntegrantes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private PersonalCapacitado personalCapacitado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private SesionIndividualMensualPsicopedogia sesionIndividualMensualPsicopedogia;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private Convenios convenios;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private AcervoBibliograficoPeriodosEscolares acervoBibliograficoPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ReconocimientoProdepRegistros reconocimientoProdepRegistros;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ParticipantesPersonalCapacitado participantesPersonalCapacitado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ActividadesVariasRegistro actividadesVariasRegistro;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private IngresosPropiosCaptados ingresosPropiosCaptados;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private CuerpacadLineas cuerpacadLineas;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private RegistrosMovilidad registrosMovilidad;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private BolsaTrabajo bolsaTrabajo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private FeriasParticipantes feriasParticipantes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private Presupuestos presupuestos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private RegistroMovilidadDocente registroMovilidadDocente;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private AsesoriasTutoriasCicloPeriodos asesoriasTutoriasCicloPeriodos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private BecasPeriodosEscolares becasPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private DesercionReprobacionMaterias desercionReprobacionMaterias;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private OrganismosVinculados organismosVinculados;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ComisionesAcademicasParticipantes comisionesAcademicasParticipantes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private DifusionIems difusionIems;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ProgramasPertcal programasPertcal;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ComisionesAcademicas comisionesAcademicas;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private RegistroMovilidadEstudiante registroMovilidadEstudiante;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private EgetsuResultadosGeneraciones egetsuResultadosGeneraciones;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ActividadEgresadoGeneracion actividadEgresadoGeneracion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private EstadiasPorEstudiante estadiasPorEstudiante;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private CuerposAcademicosRegistro cuerposAcademicosRegistro;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private FeriasProfesiograficas feriasProfesiograficas;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ExaniResultadosCiclosEscolares exaniResultadosCiclosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private DesercionPeriodosEscolares desercionPeriodosEscolares;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private AsesoriasTutoriasCuatrimestrales asesoriasTutoriasCuatrimestrales;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private ActividadesFormacionIntegral actividadesFormacionIntegral;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registros", fetch = FetchType.LAZY)
    private SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes;

    public Registros() {
    }

    public Registros(Integer registro) {
        this.registro = registro;
    }

    public Registros(Integer registro, short area, Date fechaRegistro, boolean validado) {
        this.registro = registro;
        this.area = area;
        this.fechaRegistro = fechaRegistro;
        this.validado = validado;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean getValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    @XmlTransient
    public List<ActividadesPoa> getActividadesPoaList() {
        return actividadesPoaList;
    }

    public void setActividadesPoaList(List<ActividadesPoa> actividadesPoaList) {
        this.actividadesPoaList = actividadesPoaList;
    }

    @XmlTransient
    public List<Evidencias> getEvidenciasList() {
        return evidenciasList;
    }

    public void setEvidenciasList(List<Evidencias> evidenciasList) {
        this.evidenciasList = evidenciasList;
    }

    public ServiciosTecnologicosParticipantes getServiciosTecnologicosParticipantes() {
        return serviciosTecnologicosParticipantes;
    }

    public void setServiciosTecnologicosParticipantes(ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes) {
        this.serviciosTecnologicosParticipantes = serviciosTecnologicosParticipantes;
    }

    public ProductosAcademicos getProductosAcademicos() {
        return productosAcademicos;
    }

    public void setProductosAcademicos(ProductosAcademicos productosAcademicos) {
        this.productosAcademicos = productosAcademicos;
    }

    public EjesRegistro getEje() {
        return eje;
    }

    public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }

    public EventosRegistros getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(EventosRegistros eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    public RegistrosTipo getTipo() {
        return tipo;
    }

    public void setTipo(RegistrosTipo tipo) {
        this.tipo = tipo;
    }

    public ActividadEconomicaEgresadoGeneracion getActividadEconomicaEgresadoGeneracion() {
        return actividadEconomicaEgresadoGeneracion;
    }

    public void setActividadEconomicaEgresadoGeneracion(ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion) {
        this.actividadEconomicaEgresadoGeneracion = actividadEconomicaEgresadoGeneracion;
    }

    public VisitasIndustriales getVisitasIndustriales() {
        return visitasIndustriales;
    }

    public void setVisitasIndustriales(VisitasIndustriales visitasIndustriales) {
        this.visitasIndustriales = visitasIndustriales;
    }

    public ProgramasEstimulos getProgramasEstimulos() {
        return programasEstimulos;
    }

    public void setProgramasEstimulos(ProgramasEstimulos programasEstimulos) {
        this.programasEstimulos = programasEstimulos;
    }

    public AsesoriasTutoriasMensualPeriodosEscolares getAsesoriasTutoriasMensualPeriodosEscolares() {
        return asesoriasTutoriasMensualPeriodosEscolares;
    }

    public void setAsesoriasTutoriasMensualPeriodosEscolares(AsesoriasTutoriasMensualPeriodosEscolares asesoriasTutoriasMensualPeriodosEscolares) {
        this.asesoriasTutoriasMensualPeriodosEscolares = asesoriasTutoriasMensualPeriodosEscolares;
    }

    public ProductosAcademicosPersonal getProductosAcademicosPersonal() {
        return productosAcademicosPersonal;
    }

    public void setProductosAcademicosPersonal(ProductosAcademicosPersonal productosAcademicosPersonal) {
        this.productosAcademicosPersonal = productosAcademicosPersonal;
    }

    public CuerpacadIntegrantes getCuerpacadIntegrantes() {
        return cuerpacadIntegrantes;
    }

    public void setCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrantes) {
        this.cuerpacadIntegrantes = cuerpacadIntegrantes;
    }

    public PersonalCapacitado getPersonalCapacitado() {
        return personalCapacitado;
    }

    public void setPersonalCapacitado(PersonalCapacitado personalCapacitado) {
        this.personalCapacitado = personalCapacitado;
    }

    public MatriculaPeriodosEscolares getMatriculaPeriodosEscolares() {
        return matriculaPeriodosEscolares;
    }

    public void setMatriculaPeriodosEscolares(MatriculaPeriodosEscolares matriculaPeriodosEscolares) {
        this.matriculaPeriodosEscolares = matriculaPeriodosEscolares;
    }

    public SesionIndividualMensualPsicopedogia getSesionIndividualMensualPsicopedogia() {
        return sesionIndividualMensualPsicopedogia;
    }

    public void setSesionIndividualMensualPsicopedogia(SesionIndividualMensualPsicopedogia sesionIndividualMensualPsicopedogia) {
        this.sesionIndividualMensualPsicopedogia = sesionIndividualMensualPsicopedogia;
    }

    public EquiposComputoInternetCicloPeriodoEscolar getEquiposComputoInternetCicloPeriodoEscolar() {
        return equiposComputoInternetCicloPeriodoEscolar;
    }

    public void setEquiposComputoInternetCicloPeriodoEscolar(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar) {
        this.equiposComputoInternetCicloPeriodoEscolar = equiposComputoInternetCicloPeriodoEscolar;
    }

    public EficienciaTerminalTitulacionRegistro getEficienciaTerminalTitulacionRegistro() {
        return eficienciaTerminalTitulacionRegistro;
    }

    public void setEficienciaTerminalTitulacionRegistro(EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro) {
        this.eficienciaTerminalTitulacionRegistro = eficienciaTerminalTitulacionRegistro;
    }

    public CapacidadInstaladaCiclosEscolares getCapacidadInstaladaCiclosEscolares() {
        return capacidadInstaladaCiclosEscolares;
    }

    public void setCapacidadInstaladaCiclosEscolares(CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares) {
        this.capacidadInstaladaCiclosEscolares = capacidadInstaladaCiclosEscolares;
    }

    public ServiciosTecnologicosAnioMes getServiciosTecnologicosAnioMes() {
        return serviciosTecnologicosAnioMes;
    }

    public void setServiciosTecnologicosAnioMes(ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes) {
        this.serviciosTecnologicosAnioMes = serviciosTecnologicosAnioMes;
    }

    public Convenios getConvenios() {
        return convenios;
    }

    public void setConvenios(Convenios convenios) {
        this.convenios = convenios;
    }

    public AcervoBibliograficoPeriodosEscolares getAcervoBibliograficoPeriodosEscolares() {
        return acervoBibliograficoPeriodosEscolares;
    }

    public void setAcervoBibliograficoPeriodosEscolares(AcervoBibliograficoPeriodosEscolares acervoBibliograficoPeriodosEscolares) {
        this.acervoBibliograficoPeriodosEscolares = acervoBibliograficoPeriodosEscolares;
    }

    public DistribucionLabtallCicloPeriodosEscolares getDistribucionLabtallCicloPeriodosEscolares() {
        return distribucionLabtallCicloPeriodosEscolares;
    }

    public void setDistribucionLabtallCicloPeriodosEscolares(DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares) {
        this.distribucionLabtallCicloPeriodosEscolares = distribucionLabtallCicloPeriodosEscolares;
    }

    public ReconocimientoProdepRegistros getReconocimientoProdepRegistros() {
        return reconocimientoProdepRegistros;
    }

    public void setReconocimientoProdepRegistros(ReconocimientoProdepRegistros reconocimientoProdepRegistros) {
        this.reconocimientoProdepRegistros = reconocimientoProdepRegistros;
    }

    public ParticipantesPersonalCapacitado getParticipantesPersonalCapacitado() {
        return participantesPersonalCapacitado;
    }

    public void setParticipantesPersonalCapacitado(ParticipantesPersonalCapacitado participantesPersonalCapacitado) {
        this.participantesPersonalCapacitado = participantesPersonalCapacitado;
    }

    public ActividadesVariasRegistro getActividadesVariasRegistro() {
        return actividadesVariasRegistro;
    }

    public void setActividadesVariasRegistro(ActividadesVariasRegistro actividadesVariasRegistro) {
        this.actividadesVariasRegistro = actividadesVariasRegistro;
    }

    public IngresosPropiosCaptados getIngresosPropiosCaptados() {
        return ingresosPropiosCaptados;
    }

    public void setIngresosPropiosCaptados(IngresosPropiosCaptados ingresosPropiosCaptados) {
        this.ingresosPropiosCaptados = ingresosPropiosCaptados;
    }

    public NivelIngresosEgresadosGeneracion getNivelIngresosEgresadosGeneracion() {
        return nivelIngresosEgresadosGeneracion;
    }

    public void setNivelIngresosEgresadosGeneracion(NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion) {
        this.nivelIngresosEgresadosGeneracion = nivelIngresosEgresadosGeneracion;
    }

    public CuerpacadLineas getCuerpacadLineas() {
        return cuerpacadLineas;
    }

    public void setCuerpacadLineas(CuerpacadLineas cuerpacadLineas) {
        this.cuerpacadLineas = cuerpacadLineas;
    }

    public RegistrosMovilidad getRegistrosMovilidad() {
        return registrosMovilidad;
    }

    public void setRegistrosMovilidad(RegistrosMovilidad registrosMovilidad) {
        this.registrosMovilidad = registrosMovilidad;
    }

    public BolsaTrabajo getBolsaTrabajo() {
        return bolsaTrabajo;
    }

    public void setBolsaTrabajo(BolsaTrabajo bolsaTrabajo) {
        this.bolsaTrabajo = bolsaTrabajo;
    }

    public FeriasParticipantes getFeriasParticipantes() {
        return feriasParticipantes;
    }

    public void setFeriasParticipantes(FeriasParticipantes feriasParticipantes) {
        this.feriasParticipantes = feriasParticipantes;
    }

    public Presupuestos getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(Presupuestos presupuestos) {
        this.presupuestos = presupuestos;
    }

    public RegistroMovilidadDocente getRegistroMovilidadDocente() {
        return registroMovilidadDocente;
    }

    public void setRegistroMovilidadDocente(RegistroMovilidadDocente registroMovilidadDocente) {
        this.registroMovilidadDocente = registroMovilidadDocente;
    }

    public ServiciosEnfermeriaCicloPeriodos getServiciosEnfermeriaCicloPeriodos() {
        return serviciosEnfermeriaCicloPeriodos;
    }

    public void setServiciosEnfermeriaCicloPeriodos(ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos) {
        this.serviciosEnfermeriaCicloPeriodos = serviciosEnfermeriaCicloPeriodos;
    }

    public AsesoriasTutoriasCicloPeriodos getAsesoriasTutoriasCicloPeriodos() {
        return asesoriasTutoriasCicloPeriodos;
    }

    public void setAsesoriasTutoriasCicloPeriodos(AsesoriasTutoriasCicloPeriodos asesoriasTutoriasCicloPeriodos) {
        this.asesoriasTutoriasCicloPeriodos = asesoriasTutoriasCicloPeriodos;
    }

    public BolsaTrabajoEntrevistas getBolsaTrabajoEntrevistas() {
        return bolsaTrabajoEntrevistas;
    }

    public void setBolsaTrabajoEntrevistas(BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas) {
        this.bolsaTrabajoEntrevistas = bolsaTrabajoEntrevistas;
    }

    public DistribucionAulasCicloPeriodosEscolares getDistribucionAulasCicloPeriodosEscolares() {
        return distribucionAulasCicloPeriodosEscolares;
    }

    public void setDistribucionAulasCicloPeriodosEscolares(DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares) {
        this.distribucionAulasCicloPeriodosEscolares = distribucionAulasCicloPeriodosEscolares;
    }

    public BecasPeriodosEscolares getBecasPeriodosEscolares() {
        return becasPeriodosEscolares;
    }

    public void setBecasPeriodosEscolares(BecasPeriodosEscolares becasPeriodosEscolares) {
        this.becasPeriodosEscolares = becasPeriodosEscolares;
    }

    public DesercionReprobacionMaterias getDesercionReprobacionMaterias() {
        return desercionReprobacionMaterias;
    }

    public void setDesercionReprobacionMaterias(DesercionReprobacionMaterias desercionReprobacionMaterias) {
        this.desercionReprobacionMaterias = desercionReprobacionMaterias;
    }

    public ParticipantesActividadesFormacionIntegral getParticipantesActividadesFormacionIntegral() {
        return participantesActividadesFormacionIntegral;
    }

    public void setParticipantesActividadesFormacionIntegral(ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral) {
        this.participantesActividadesFormacionIntegral = participantesActividadesFormacionIntegral;
    }

    public OrganismosVinculados getOrganismosVinculados() {
        return organismosVinculados;
    }

    public void setOrganismosVinculados(OrganismosVinculados organismosVinculados) {
        this.organismosVinculados = organismosVinculados;
    }

    public ComisionesAcademicasParticipantes getComisionesAcademicasParticipantes() {
        return comisionesAcademicasParticipantes;
    }

    public void setComisionesAcademicasParticipantes(ComisionesAcademicasParticipantes comisionesAcademicasParticipantes) {
        this.comisionesAcademicasParticipantes = comisionesAcademicasParticipantes;
    }

    public DifusionIems getDifusionIems() {
        return difusionIems;
    }

    public void setDifusionIems(DifusionIems difusionIems) {
        this.difusionIems = difusionIems;
    }

    public ProgramasPertcal getProgramasPertcal() {
        return programasPertcal;
    }

    public void setProgramasPertcal(ProgramasPertcal programasPertcal) {
        this.programasPertcal = programasPertcal;
    }

    public ComisionesAcademicas getComisionesAcademicas() {
        return comisionesAcademicas;
    }

    public void setComisionesAcademicas(ComisionesAcademicas comisionesAcademicas) {
        this.comisionesAcademicas = comisionesAcademicas;
    }

    public RegistroMovilidadEstudiante getRegistroMovilidadEstudiante() {
        return registroMovilidadEstudiante;
    }

    public void setRegistroMovilidadEstudiante(RegistroMovilidadEstudiante registroMovilidadEstudiante) {
        this.registroMovilidadEstudiante = registroMovilidadEstudiante;
    }

    public EgetsuResultadosGeneraciones getEgetsuResultadosGeneraciones() {
        return egetsuResultadosGeneraciones;
    }

    public void setEgetsuResultadosGeneraciones(EgetsuResultadosGeneraciones egetsuResultadosGeneraciones) {
        this.egetsuResultadosGeneraciones = egetsuResultadosGeneraciones;
    }

    public ActividadEgresadoGeneracion getActividadEgresadoGeneracion() {
        return actividadEgresadoGeneracion;
    }

    public void setActividadEgresadoGeneracion(ActividadEgresadoGeneracion actividadEgresadoGeneracion) {
        this.actividadEgresadoGeneracion = actividadEgresadoGeneracion;
    }

    public EstadiasPorEstudiante getEstadiasPorEstudiante() {
        return estadiasPorEstudiante;
    }

    public void setEstadiasPorEstudiante(EstadiasPorEstudiante estadiasPorEstudiante) {
        this.estadiasPorEstudiante = estadiasPorEstudiante;
    }

    public CuerposAcademicosRegistro getCuerposAcademicosRegistro() {
        return cuerposAcademicosRegistro;
    }

    public void setCuerposAcademicosRegistro(CuerposAcademicosRegistro cuerposAcademicosRegistro) {
        this.cuerposAcademicosRegistro = cuerposAcademicosRegistro;
    }

    public NivelOcupacionEgresadosGeneracion getNivelOcupacionEgresadosGeneracion() {
        return nivelOcupacionEgresadosGeneracion;
    }

    public void setNivelOcupacionEgresadosGeneracion(NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion) {
        this.nivelOcupacionEgresadosGeneracion = nivelOcupacionEgresadosGeneracion;
    }

    public EquiposComputoCicloPeriodoEscolar getEquiposComputoCicloPeriodoEscolar() {
        return equiposComputoCicloPeriodoEscolar;
    }

    public void setEquiposComputoCicloPeriodoEscolar(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar) {
        this.equiposComputoCicloPeriodoEscolar = equiposComputoCicloPeriodoEscolar;
    }

    public FeriasProfesiograficas getFeriasProfesiograficas() {
        return feriasProfesiograficas;
    }

    public void setFeriasProfesiograficas(FeriasProfesiograficas feriasProfesiograficas) {
        this.feriasProfesiograficas = feriasProfesiograficas;
    }

    public ExaniResultadosCiclosEscolares getExaniResultadosCiclosEscolares() {
        return exaniResultadosCiclosEscolares;
    }

    public void setExaniResultadosCiclosEscolares(ExaniResultadosCiclosEscolares exaniResultadosCiclosEscolares) {
        this.exaniResultadosCiclosEscolares = exaniResultadosCiclosEscolares;
    }

    public DesercionPeriodosEscolares getDesercionPeriodosEscolares() {
        return desercionPeriodosEscolares;
    }

    public void setDesercionPeriodosEscolares(DesercionPeriodosEscolares desercionPeriodosEscolares) {
        this.desercionPeriodosEscolares = desercionPeriodosEscolares;
    }

    public AsesoriasTutoriasCuatrimestrales getAsesoriasTutoriasCuatrimestrales() {
        return asesoriasTutoriasCuatrimestrales;
    }

    public void setAsesoriasTutoriasCuatrimestrales(AsesoriasTutoriasCuatrimestrales asesoriasTutoriasCuatrimestrales) {
        this.asesoriasTutoriasCuatrimestrales = asesoriasTutoriasCuatrimestrales;
    }

    public ActividadesFormacionIntegral getActividadesFormacionIntegral() {
        return actividadesFormacionIntegral;
    }

    public void setActividadesFormacionIntegral(ActividadesFormacionIntegral actividadesFormacionIntegral) {
        this.actividadesFormacionIntegral = actividadesFormacionIntegral;
    }
    
    public EvaluacionSatisfaccionResultados getEvaluacionSatisfaccionResultados() {
        return evaluacionSatisfaccionResultados;
    }

    public void setEvaluacionSatisfaccionResultados(EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados) {
        this.evaluacionSatisfaccionResultados = evaluacionSatisfaccionResultados;
    }

    public SatisfaccionServtecEducontAnioMes getSatisfaccionServtecEducontAnioMes() {
        return satisfaccionServtecEducontAnioMes;
    }

    public void setSatisfaccionServtecEducontAnioMes(SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes) {
        this.satisfaccionServtecEducontAnioMes = satisfaccionServtecEducontAnioMes;
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
        if (!(object instanceof Registros)) {
            return false;
        }
        Registros other = (Registros) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Registros[ registro=" + registro + " ]";
    }
    
}
