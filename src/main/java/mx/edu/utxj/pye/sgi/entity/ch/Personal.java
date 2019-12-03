/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "personal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personal.findAll", query = "SELECT p FROM Personal p")
    , @NamedQuery(name = "Personal.findByClave", query = "SELECT p FROM Personal p WHERE p.clave = :clave")
    , @NamedQuery(name = "Personal.findByNombre", query = "SELECT p FROM Personal p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Personal.findByFechaIngreso", query = "SELECT p FROM Personal p WHERE p.fechaIngreso = :fechaIngreso")
    , @NamedQuery(name = "Personal.findByStatus", query = "SELECT p FROM Personal p WHERE p.status = :status")
    , @NamedQuery(name = "Personal.findByAreaOperativa", query = "SELECT p FROM Personal p WHERE p.areaOperativa = :areaOperativa")
    , @NamedQuery(name = "Personal.findByAreaSuperior", query = "SELECT p FROM Personal p WHERE p.areaSuperior = :areaSuperior")
    , @NamedQuery(name = "Personal.findByAreaOficial", query = "SELECT p FROM Personal p WHERE p.areaOficial = :areaOficial")
    , @NamedQuery(name = "Personal.findByPerfilProfesional", query = "SELECT p FROM Personal p WHERE p.perfilProfesional = :perfilProfesional")
    , @NamedQuery(name = "Personal.findByExperienciaDocente", query = "SELECT p FROM Personal p WHERE p.experienciaDocente = :experienciaDocente")
    , @NamedQuery(name = "Personal.findByExperienciaLaboral", query = "SELECT p FROM Personal p WHERE p.experienciaLaboral = :experienciaLaboral")
    , @NamedQuery(name = "Personal.findByFechaNacimiento", query = "SELECT p FROM Personal p WHERE p.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Personal.findByEstado", query = "SELECT p FROM Personal p WHERE p.estado = :estado")
    , @NamedQuery(name = "Personal.findByMunicipio", query = "SELECT p FROM Personal p WHERE p.municipio = :municipio")
    , @NamedQuery(name = "Personal.findByLocalidad", query = "SELECT p FROM Personal p WHERE p.localidad = :localidad")
    , @NamedQuery(name = "Personal.findByPais", query = "SELECT p FROM Personal p WHERE p.pais = :pais")
    , @NamedQuery(name = "Personal.findBySni", query = "SELECT p FROM Personal p WHERE p.sni = :sni")
    , @NamedQuery(name = "Personal.findByPerfilProdep", query = "SELECT p FROM Personal p WHERE p.perfilProdep = :perfilProdep")
    , @NamedQuery(name = "Personal.findByCorreoElectronico", query = "SELECT p FROM Personal p WHERE p.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "Personal.findByCorreoElectronico2", query = "SELECT p FROM Personal p WHERE p.correoElectronico2 = :correoElectronico2")})
public class Personal implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionConocimientoCodigoEticaResultados> evaluacionConocimientoCodigoEticaResultadosList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "clave")
    private Integer clave;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "status")
    private Character status;
    @Basic(optional = false)
    @Column(name = "area_operativa")
    private short areaOperativa;
    @Basic(optional = false)
    @Column(name = "area_superior")
    private short areaSuperior;
    @Basic(optional = false)
    @Column(name = "area_oficial")
    private short areaOficial;
    @Basic(optional = false)
    @Column(name = "perfil_profesional")
    private String perfilProfesional;
    @Basic(optional = false)
    @Column(name = "experiencia_docente")
    private short experienciaDocente;
    @Basic(optional = false)
    @Column(name = "experiencia_laboral")
    private short experienciaLaboral;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "municipio")
    private String municipio;
    @Basic(optional = false)
    @Column(name = "localidad")
    private String localidad;
    @Basic(optional = false)
    @Column(name = "pais")
    private String pais;
    @Basic(optional = false)
    @Column(name = "sni")
    private boolean sni;
    @Basic(optional = false)
    @Column(name = "perfil_prodep")
    private boolean perfilProdep;
    @Column(name = "correo_electronico")
    private String correoElectronico;
    @Column(name = "correo_electronico2")
    private String correoElectronico2;
    @ManyToMany(mappedBy = "personalList")
    private List<Eventos> eventosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Innovaciones> innovacionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPersonal")
    private List<Comentariosfunciones> comentariosfuncionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<ExperienciasLaborales> experienciasLaboralesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<FormacionAcademica> formacionAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionTutoresResultados> evaluacionTutoresResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona")
    private List<Permisosadminstracion> permisosadminstracionList;
    @JoinColumn(name = "categoria_360", referencedColumnName = "categoria")
    @ManyToOne
    private PersonalCategorias categoria360;
    @JoinColumn(name = "categoria_especifica", referencedColumnName = "categoriaEspecifica")
    @ManyToOne
    private Categoriasespecificasfunciones categoriaEspecifica;
    @JoinColumn(name = "actividad", referencedColumnName = "actividad")
    @ManyToOne(optional = false)
    private Actividades actividad;
    @JoinColumn(name = "categoria_operativa", referencedColumnName = "categoria")
    @ManyToOne(optional = false)
    private PersonalCategorias categoriaOperativa;
    @JoinColumn(name = "categoria_oficial", referencedColumnName = "categoria")
    @ManyToOne(optional = false)
    private PersonalCategorias categoriaOficial;
    @JoinColumn(name = "genero", referencedColumnName = "genero")
    @ManyToOne(optional = false)
    private Generos genero;
    @JoinColumn(name = "grado", referencedColumnName = "grado")
    @ManyToOne(optional = false)
    private Grados grado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Articulosp> articulospList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionDocentesMateriaResultados> evaluacionDocentesMateriaResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Idiomas> idiomasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Congresos> congresosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Incidencias> incidenciasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "director")
    private List<PlaneacionesCuatrimestrales> planeacionesCuatrimestralesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docente")
    private List<PlaneacionesCuatrimestrales> planeacionesCuatrimestralesList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveEmpleado")
    private List<Distinciones> distincionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<DesarrollosTecnologicos> desarrollosTecnologicosList;
    @OneToMany(mappedBy = "clavePersonal")
    private List<LibrosPub> librosPubList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Incapacidad> incapacidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Memoriaspub> memoriaspubList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<PlaneacionesLiberaciones> planeacionesLiberacionesList;
    @OneToMany(mappedBy = "clave")
    private List<Permisos> permisosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<DesarrolloSoftware> desarrolloSoftwareList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clave")
    private List<CursosPersonal> cursosPersonalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Docencias> docenciasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<DesempenioEvaluacionResultados> desempenioEvaluacionResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal1")
    private List<DesempenioEvaluacionResultados> desempenioEvaluacionResultadosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Lenguas> lenguasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionesClimaLaboralResultados> evaluacionesClimaLaboralResultadosList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personal")
    private InformacionAdicionalPersonal informacionAdicionalPersonal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Horarios> horariosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveTRemitente")
    private List<Notificaciones> notificacionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveTDestino")
    private List<Notificaciones> notificacionesList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionesControlInternoResultados> evaluacionesControlInternoResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<HabilidadesInformaticas> habilidadesInformaticasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionesPremiosResultados> evaluacionesPremiosResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal1")
    private List<EvaluacionesPremiosResultados> evaluacionesPremiosResultadosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<EvaluacionEstadiaResultados> evaluacionEstadiaResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Divulgaciones> divulgacionesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<PlaneacionesDetalles> planeacionesDetallesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<Capacitacionespersonal> capacitacionespersonalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<Evaluaciones360Resultados> evaluaciones360ResultadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal1")
    private List<Evaluaciones360Resultados> evaluaciones360ResultadosList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clavePersonal")
    private List<ContactoEmergencias> contactoEmergenciasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personal")
    private List<Cuidados> cuidadosList;

    public Personal() {
    }

    public Personal(Integer clave) {
        this.clave = clave;
    }

    public Personal(Integer clave, String nombre, Date fechaIngreso, Character status, short areaOperativa, short areaSuperior, short areaOficial, String perfilProfesional, short experienciaDocente, short experienciaLaboral, Date fechaNacimiento, String estado, String municipio, String localidad, String pais, boolean sni, boolean perfilProdep) {
        this.clave = clave;
        this.nombre = nombre;
        this.fechaIngreso = fechaIngreso;
        this.status = status;
        this.areaOperativa = areaOperativa;
        this.areaSuperior = areaSuperior;
        this.areaOficial = areaOficial;
        this.perfilProfesional = perfilProfesional;
        this.experienciaDocente = experienciaDocente;
        this.experienciaLaboral = experienciaLaboral;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado;
        this.municipio = municipio;
        this.localidad = localidad;
        this.pais = pais;
        this.sni = sni;
        this.perfilProdep = perfilProdep;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public short getAreaOperativa() {
        return areaOperativa;
    }

    public void setAreaOperativa(short areaOperativa) {
        this.areaOperativa = areaOperativa;
    }

    public short getAreaSuperior() {
        return areaSuperior;
    }

    public void setAreaSuperior(short areaSuperior) {
        this.areaSuperior = areaSuperior;
    }

    public short getAreaOficial() {
        return areaOficial;
    }

    public void setAreaOficial(short areaOficial) {
        this.areaOficial = areaOficial;
    }

    public String getPerfilProfesional() {
        return perfilProfesional;
    }

    public void setPerfilProfesional(String perfilProfesional) {
        this.perfilProfesional = perfilProfesional;
    }

    public short getExperienciaDocente() {
        return experienciaDocente;
    }

    public void setExperienciaDocente(short experienciaDocente) {
        this.experienciaDocente = experienciaDocente;
    }

    public short getExperienciaLaboral() {
        return experienciaLaboral;
    }

    public void setExperienciaLaboral(short experienciaLaboral) {
        this.experienciaLaboral = experienciaLaboral;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean getSni() {
        return sni;
    }

    public void setSni(boolean sni) {
        this.sni = sni;
    }

    public boolean getPerfilProdep() {
        return perfilProdep;
    }

    public void setPerfilProdep(boolean perfilProdep) {
        this.perfilProdep = perfilProdep;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCorreoElectronico2() {
        return correoElectronico2;
    }

    public void setCorreoElectronico2(String correoElectronico2) {
        this.correoElectronico2 = correoElectronico2;
    }

    @XmlTransient
    public List<Eventos> getEventosList() {
        return eventosList;
    }

    public void setEventosList(List<Eventos> eventosList) {
        this.eventosList = eventosList;
    }

    @XmlTransient
    public List<Innovaciones> getInnovacionesList() {
        return innovacionesList;
    }

    public void setInnovacionesList(List<Innovaciones> innovacionesList) {
        this.innovacionesList = innovacionesList;
    }

    @XmlTransient
    public List<Comentariosfunciones> getComentariosfuncionesList() {
        return comentariosfuncionesList;
    }

    public void setComentariosfuncionesList(List<Comentariosfunciones> comentariosfuncionesList) {
        this.comentariosfuncionesList = comentariosfuncionesList;
    }

    @XmlTransient
    public List<ExperienciasLaborales> getExperienciasLaboralesList() {
        return experienciasLaboralesList;
    }

    public void setExperienciasLaboralesList(List<ExperienciasLaborales> experienciasLaboralesList) {
        this.experienciasLaboralesList = experienciasLaboralesList;
    }

    @XmlTransient
    public List<FormacionAcademica> getFormacionAcademicaList() {
        return formacionAcademicaList;
    }

    public void setFormacionAcademicaList(List<FormacionAcademica> formacionAcademicaList) {
        this.formacionAcademicaList = formacionAcademicaList;
    }

    @XmlTransient
    public List<EvaluacionTutoresResultados> getEvaluacionTutoresResultadosList() {
        return evaluacionTutoresResultadosList;
    }

    public void setEvaluacionTutoresResultadosList(List<EvaluacionTutoresResultados> evaluacionTutoresResultadosList) {
        this.evaluacionTutoresResultadosList = evaluacionTutoresResultadosList;
    }

    @XmlTransient
    public List<Permisosadminstracion> getPermisosadminstracionList() {
        return permisosadminstracionList;
    }

    public void setPermisosadminstracionList(List<Permisosadminstracion> permisosadminstracionList) {
        this.permisosadminstracionList = permisosadminstracionList;
    }

    public PersonalCategorias getCategoria360() {
        return categoria360;
    }

    public void setCategoria360(PersonalCategorias categoria360) {
        this.categoria360 = categoria360;
    }

    public Categoriasespecificasfunciones getCategoriaEspecifica() {
        return categoriaEspecifica;
    }

    public void setCategoriaEspecifica(Categoriasespecificasfunciones categoriaEspecifica) {
        this.categoriaEspecifica = categoriaEspecifica;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public PersonalCategorias getCategoriaOperativa() {
        return categoriaOperativa;
    }

    public void setCategoriaOperativa(PersonalCategorias categoriaOperativa) {
        this.categoriaOperativa = categoriaOperativa;
    }

    public PersonalCategorias getCategoriaOficial() {
        return categoriaOficial;
    }

    public void setCategoriaOficial(PersonalCategorias categoriaOficial) {
        this.categoriaOficial = categoriaOficial;
    }

    public Generos getGenero() {
        return genero;
    }

    public void setGenero(Generos genero) {
        this.genero = genero;
    }

    public Grados getGrado() {
        return grado;
    }

    public void setGrado(Grados grado) {
        this.grado = grado;
    }

    @XmlTransient
    public List<Articulosp> getArticulospList() {
        return articulospList;
    }

    public void setArticulospList(List<Articulosp> articulospList) {
        this.articulospList = articulospList;
    }

    @XmlTransient
    public List<EvaluacionDocentesMateriaResultados> getEvaluacionDocentesMateriaResultadosList() {
        return evaluacionDocentesMateriaResultadosList;
    }

    public void setEvaluacionDocentesMateriaResultadosList(List<EvaluacionDocentesMateriaResultados> evaluacionDocentesMateriaResultadosList) {
        this.evaluacionDocentesMateriaResultadosList = evaluacionDocentesMateriaResultadosList;
    }

    @XmlTransient
    public List<Idiomas> getIdiomasList() {
        return idiomasList;
    }

    public void setIdiomasList(List<Idiomas> idiomasList) {
        this.idiomasList = idiomasList;
    }

    @XmlTransient
    public List<Congresos> getCongresosList() {
        return congresosList;
    }

    public void setCongresosList(List<Congresos> congresosList) {
        this.congresosList = congresosList;
    }

    @XmlTransient
    public List<Incidencias> getIncidenciasList() {
        return incidenciasList;
    }

    public void setIncidenciasList(List<Incidencias> incidenciasList) {
        this.incidenciasList = incidenciasList;
    }

    @XmlTransient
    public List<PlaneacionesCuatrimestrales> getPlaneacionesCuatrimestralesList() {
        return planeacionesCuatrimestralesList;
    }

    public void setPlaneacionesCuatrimestralesList(List<PlaneacionesCuatrimestrales> planeacionesCuatrimestralesList) {
        this.planeacionesCuatrimestralesList = planeacionesCuatrimestralesList;
    }

    @XmlTransient
    public List<PlaneacionesCuatrimestrales> getPlaneacionesCuatrimestralesList1() {
        return planeacionesCuatrimestralesList1;
    }

    public void setPlaneacionesCuatrimestralesList1(List<PlaneacionesCuatrimestrales> planeacionesCuatrimestralesList1) {
        this.planeacionesCuatrimestralesList1 = planeacionesCuatrimestralesList1;
    }

    @XmlTransient
    public List<Distinciones> getDistincionesList() {
        return distincionesList;
    }

    public void setDistincionesList(List<Distinciones> distincionesList) {
        this.distincionesList = distincionesList;
    }

    @XmlTransient
    public List<DesarrollosTecnologicos> getDesarrollosTecnologicosList() {
        return desarrollosTecnologicosList;
    }

    public void setDesarrollosTecnologicosList(List<DesarrollosTecnologicos> desarrollosTecnologicosList) {
        this.desarrollosTecnologicosList = desarrollosTecnologicosList;
    }

    @XmlTransient
    public List<LibrosPub> getLibrosPubList() {
        return librosPubList;
    }

    public void setLibrosPubList(List<LibrosPub> librosPubList) {
        this.librosPubList = librosPubList;
    }

    @XmlTransient
    public List<Incapacidad> getIncapacidadList() {
        return incapacidadList;
    }

    public void setIncapacidadList(List<Incapacidad> incapacidadList) {
        this.incapacidadList = incapacidadList;
    }

    @XmlTransient
    public List<Memoriaspub> getMemoriaspubList() {
        return memoriaspubList;
    }

    public void setMemoriaspubList(List<Memoriaspub> memoriaspubList) {
        this.memoriaspubList = memoriaspubList;
    }

    @XmlTransient
    public List<PlaneacionesLiberaciones> getPlaneacionesLiberacionesList() {
        return planeacionesLiberacionesList;
    }

    public void setPlaneacionesLiberacionesList(List<PlaneacionesLiberaciones> planeacionesLiberacionesList) {
        this.planeacionesLiberacionesList = planeacionesLiberacionesList;
    }

    @XmlTransient
    public List<Permisos> getPermisosList() {
        return permisosList;
    }

    public void setPermisosList(List<Permisos> permisosList) {
        this.permisosList = permisosList;
    }

    @XmlTransient
    public List<DesarrolloSoftware> getDesarrolloSoftwareList() {
        return desarrolloSoftwareList;
    }

    public void setDesarrolloSoftwareList(List<DesarrolloSoftware> desarrolloSoftwareList) {
        this.desarrolloSoftwareList = desarrolloSoftwareList;
    }

    @XmlTransient
    public List<CursosPersonal> getCursosPersonalList() {
        return cursosPersonalList;
    }

    public void setCursosPersonalList(List<CursosPersonal> cursosPersonalList) {
        this.cursosPersonalList = cursosPersonalList;
    }

    @XmlTransient
    public List<Docencias> getDocenciasList() {
        return docenciasList;
    }

    public void setDocenciasList(List<Docencias> docenciasList) {
        this.docenciasList = docenciasList;
    }

    @XmlTransient
    public List<DesempenioEvaluacionResultados> getDesempenioEvaluacionResultadosList() {
        return desempenioEvaluacionResultadosList;
    }

    public void setDesempenioEvaluacionResultadosList(List<DesempenioEvaluacionResultados> desempenioEvaluacionResultadosList) {
        this.desempenioEvaluacionResultadosList = desempenioEvaluacionResultadosList;
    }

    @XmlTransient
    public List<DesempenioEvaluacionResultados> getDesempenioEvaluacionResultadosList1() {
        return desempenioEvaluacionResultadosList1;
    }

    public void setDesempenioEvaluacionResultadosList1(List<DesempenioEvaluacionResultados> desempenioEvaluacionResultadosList1) {
        this.desempenioEvaluacionResultadosList1 = desempenioEvaluacionResultadosList1;
    }

    @XmlTransient
    public List<Lenguas> getLenguasList() {
        return lenguasList;
    }

    public void setLenguasList(List<Lenguas> lenguasList) {
        this.lenguasList = lenguasList;
    }

    @XmlTransient
    public List<EvaluacionesClimaLaboralResultados> getEvaluacionesClimaLaboralResultadosList() {
        return evaluacionesClimaLaboralResultadosList;
    }

    public void setEvaluacionesClimaLaboralResultadosList(List<EvaluacionesClimaLaboralResultados> evaluacionesClimaLaboralResultadosList) {
        this.evaluacionesClimaLaboralResultadosList = evaluacionesClimaLaboralResultadosList;
    }

    public InformacionAdicionalPersonal getInformacionAdicionalPersonal() {
        return informacionAdicionalPersonal;
    }

    public void setInformacionAdicionalPersonal(InformacionAdicionalPersonal informacionAdicionalPersonal) {
        this.informacionAdicionalPersonal = informacionAdicionalPersonal;
    }

    @XmlTransient
    public List<Horarios> getHorariosList() {
        return horariosList;
    }

    public void setHorariosList(List<Horarios> horariosList) {
        this.horariosList = horariosList;
    }

    @XmlTransient
    public List<Notificaciones> getNotificacionesList() {
        return notificacionesList;
    }

    public void setNotificacionesList(List<Notificaciones> notificacionesList) {
        this.notificacionesList = notificacionesList;
    }

    @XmlTransient
    public List<Notificaciones> getNotificacionesList1() {
        return notificacionesList1;
    }

    public void setNotificacionesList1(List<Notificaciones> notificacionesList1) {
        this.notificacionesList1 = notificacionesList1;
    }

    @XmlTransient
    public List<EvaluacionesControlInternoResultados> getEvaluacionesControlInternoResultadosList() {
        return evaluacionesControlInternoResultadosList;
    }

    public void setEvaluacionesControlInternoResultadosList(List<EvaluacionesControlInternoResultados> evaluacionesControlInternoResultadosList) {
        this.evaluacionesControlInternoResultadosList = evaluacionesControlInternoResultadosList;
    }

    @XmlTransient
    public List<HabilidadesInformaticas> getHabilidadesInformaticasList() {
        return habilidadesInformaticasList;
    }

    public void setHabilidadesInformaticasList(List<HabilidadesInformaticas> habilidadesInformaticasList) {
        this.habilidadesInformaticasList = habilidadesInformaticasList;
    }

    @XmlTransient
    public List<EvaluacionesPremiosResultados> getEvaluacionesPremiosResultadosList() {
        return evaluacionesPremiosResultadosList;
    }

    public void setEvaluacionesPremiosResultadosList(List<EvaluacionesPremiosResultados> evaluacionesPremiosResultadosList) {
        this.evaluacionesPremiosResultadosList = evaluacionesPremiosResultadosList;
    }

    @XmlTransient
    public List<EvaluacionesPremiosResultados> getEvaluacionesPremiosResultadosList1() {
        return evaluacionesPremiosResultadosList1;
    }

    public void setEvaluacionesPremiosResultadosList1(List<EvaluacionesPremiosResultados> evaluacionesPremiosResultadosList1) {
        this.evaluacionesPremiosResultadosList1 = evaluacionesPremiosResultadosList1;
    }

    @XmlTransient
    public List<EvaluacionEstadiaResultados> getEvaluacionEstadiaResultadosList() {
        return evaluacionEstadiaResultadosList;
    }

    public void setEvaluacionEstadiaResultadosList(List<EvaluacionEstadiaResultados> evaluacionEstadiaResultadosList) {
        this.evaluacionEstadiaResultadosList = evaluacionEstadiaResultadosList;
    }

    @XmlTransient
    public List<Divulgaciones> getDivulgacionesList() {
        return divulgacionesList;
    }

    public void setDivulgacionesList(List<Divulgaciones> divulgacionesList) {
        this.divulgacionesList = divulgacionesList;
    }

    @XmlTransient
    public List<PlaneacionesDetalles> getPlaneacionesDetallesList() {
        return planeacionesDetallesList;
    }

    public void setPlaneacionesDetallesList(List<PlaneacionesDetalles> planeacionesDetallesList) {
        this.planeacionesDetallesList = planeacionesDetallesList;
    }

    @XmlTransient
    public List<Capacitacionespersonal> getCapacitacionespersonalList() {
        return capacitacionespersonalList;
    }

    public void setCapacitacionespersonalList(List<Capacitacionespersonal> capacitacionespersonalList) {
        this.capacitacionespersonalList = capacitacionespersonalList;
    }

    @XmlTransient
    public List<Evaluaciones360Resultados> getEvaluaciones360ResultadosList() {
        return evaluaciones360ResultadosList;
    }

    public void setEvaluaciones360ResultadosList(List<Evaluaciones360Resultados> evaluaciones360ResultadosList) {
        this.evaluaciones360ResultadosList = evaluaciones360ResultadosList;
    }

    @XmlTransient
    public List<Evaluaciones360Resultados> getEvaluaciones360ResultadosList1() {
        return evaluaciones360ResultadosList1;
    }

    public void setEvaluaciones360ResultadosList1(List<Evaluaciones360Resultados> evaluaciones360ResultadosList1) {
        this.evaluaciones360ResultadosList1 = evaluaciones360ResultadosList1;
    }

    @XmlTransient
    public List<ContactoEmergencias> getContactoEmergenciasList() {
        return contactoEmergenciasList;
    }

    public void setContactoEmergenciasList(List<ContactoEmergencias> contactoEmergenciasList) {
        this.contactoEmergenciasList = contactoEmergenciasList;
    }

    @XmlTransient
    public List<Cuidados> getCuidadosList() {
        return cuidadosList;
    }

    public void setCuidadosList(List<Cuidados> cuidadosList) {
        this.cuidadosList = cuidadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personal)) {
            return false;
        }
        Personal other = (Personal) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Personal[ clave=" + clave + " ]";
    }

    @XmlTransient
    public List<EvaluacionConocimientoCodigoEticaResultados> getEvaluacionConocimientoCodigoEticaResultadosList() {
        return evaluacionConocimientoCodigoEticaResultadosList;
    }

    public void setEvaluacionConocimientoCodigoEticaResultadosList(List<EvaluacionConocimientoCodigoEticaResultados> evaluacionConocimientoCodigoEticaResultadosList) {
        this.evaluacionConocimientoCodigoEticaResultadosList = evaluacionConocimientoCodigoEticaResultadosList;
    }
    
}
