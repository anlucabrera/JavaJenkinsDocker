/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "viewMatriculaF911", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewMatriculaF911.findAll", query = "SELECT v FROM ViewMatriculaF911 v")
    , @NamedQuery(name = "ViewMatriculaF911.findByCiclo", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.ciclo = :ciclo")
    , @NamedQuery(name = "ViewMatriculaF911.findByPeriodos", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.periodos = :periodos")
    , @NamedQuery(name = "ViewMatriculaF911.findByCarrera", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.carrera = :carrera")
    , @NamedQuery(name = "ViewMatriculaF911.findByGrado", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.grado = :grado")
    , @NamedQuery(name = "ViewMatriculaF911.findByGrupo", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.grupo = :grupo")
    , @NamedQuery(name = "ViewMatriculaF911.findByMatricula", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "ViewMatriculaF911.findByAlumno", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.alumno = :alumno")
    , @NamedQuery(name = "ViewMatriculaF911.findByFechaNacimiento", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "ViewMatriculaF911.findBySexo", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.sexo = :sexo")
    , @NamedQuery(name = "ViewMatriculaF911.findByCurp", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.curp = :curp")
    , @NamedQuery(name = "ViewMatriculaF911.findByEstadoCivil", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.estadoCivil = :estadoCivil")
    , @NamedQuery(name = "ViewMatriculaF911.findByEstatura", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.estatura = :estatura")
    , @NamedQuery(name = "ViewMatriculaF911.findByPeso", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.peso = :peso")
    , @NamedQuery(name = "ViewMatriculaF911.findByTipoSangre", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.tipoSangre = :tipoSangre")
    , @NamedQuery(name = "ViewMatriculaF911.findByCapacidadesDiferentes", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.capacidadesDiferentes = :capacidadesDiferentes")
    , @NamedQuery(name = "ViewMatriculaF911.findByLenguaAutoctona", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.lenguaAutoctona = :lenguaAutoctona")
    , @NamedQuery(name = "ViewMatriculaF911.findByComunidadIndigena", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.comunidadIndigena = :comunidadIndigena")
    , @NamedQuery(name = "ViewMatriculaF911.findByFamDiabetico", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.famDiabetico = :famDiabetico")
    , @NamedQuery(name = "ViewMatriculaF911.findByFamHipertenso", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.famHipertenso = :famHipertenso")
    , @NamedQuery(name = "ViewMatriculaF911.findByFamCardiaco", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.famCardiaco = :famCardiaco")
    , @NamedQuery(name = "ViewMatriculaF911.findByFamCancer", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.famCancer = :famCancer")
    , @NamedQuery(name = "ViewMatriculaF911.findByFamProgOportunidades", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.famProgOportunidades = :famProgOportunidades")
    , @NamedQuery(name = "ViewMatriculaF911.findByTrabaja", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.trabaja = :trabaja")
    , @NamedQuery(name = "ViewMatriculaF911.findByLugarTrabajo", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.lugarTrabajo = :lugarTrabajo")
    , @NamedQuery(name = "ViewMatriculaF911.findByEspecialidadIEMS", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.especialidadIEMS = :especialidadIEMS")
    , @NamedQuery(name = "ViewMatriculaF911.findByMedioDifusion", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.medioDifusion = :medioDifusion")
    , @NamedQuery(name = "ViewMatriculaF911.findByNombreIEMS", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.nombreIEMS = :nombreIEMS")
    , @NamedQuery(name = "ViewMatriculaF911.findByLocalidad", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.localidad = :localidad")
    , @NamedQuery(name = "ViewMatriculaF911.findByMunicipios", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.municipios = :municipios")
    , @NamedQuery(name = "ViewMatriculaF911.findByEstado", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.estado = :estado")
    , @NamedQuery(name = "ViewMatriculaF911.findByLocalidadNacimiento", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.localidadNacimiento = :localidadNacimiento")
    , @NamedQuery(name = "ViewMatriculaF911.findByMunicipioNacimiento", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.municipioNacimiento = :municipioNacimiento")
    , @NamedQuery(name = "ViewMatriculaF911.findByEstadoMunicipio", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.estadoMunicipio = :estadoMunicipio")
    , @NamedQuery(name = "ViewMatriculaF911.findByNombreTutor", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "ViewMatriculaF911.findByTelefonoTutor", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.telefonoTutor = :telefonoTutor")
    , @NamedQuery(name = "ViewMatriculaF911.findByUnicaOpcion", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.unicaOpcion = :unicaOpcion")
    , @NamedQuery(name = "ViewMatriculaF911.findByTurnos", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.turnos = :turnos")
    , @NamedQuery(name = "ViewMatriculaF911.findByDescripcion", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.descripcion = :descripcion")
    , @NamedQuery(name = "ViewMatriculaF911.findByUniversidadProcedencia", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.universidadProcedencia = :universidadProcedencia")
    , @NamedQuery(name = "ViewMatriculaF911.findByNombreOtraUni", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.nombreOtraUni = :nombreOtraUni")
    , @NamedQuery(name = "ViewMatriculaF911.findByNombreTutorING", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.nombreTutorING = :nombreTutorING")
    , @NamedQuery(name = "ViewMatriculaF911.findByTelTutorING", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.telTutorING = :telTutorING")
    , @NamedQuery(name = "ViewMatriculaF911.findByTrabajoING", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.trabajoING = :trabajoING")
    , @NamedQuery(name = "ViewMatriculaF911.findByLugarTraIng", query = "SELECT v FROM ViewMatriculaF911 v WHERE v.lugarTraIng = :lugarTraIng")})
public class ViewMatriculaF911 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "ciclo")
    private String ciclo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "periodos")
    private String periodos;
    @Size(max = 224)
    @Column(name = "Carrera")
    private String carrera;
    @Column(name = "Grado")
    private Short grado;
    @Size(max = 5)
    @Column(name = "Grupo")
    private String grupo;
    @Id
    @Size(max = 15)
    @Column(name = "Matricula")
    private String matricula;
    @Size(max = 142)
    @Column(name = "Alumno")
    private String alumno;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sexo")
    private Character sexo;
    @Size(max = 18)
    @Column(name = "curp")
    private String curp;
    @Size(max = 40)
    @Column(name = "EstadoCivil")
    private String estadoCivil;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "estatura")
    private BigDecimal estatura;
    @Column(name = "peso")
    private BigDecimal peso;
    @Size(max = 5)
    @Column(name = "TipoSangre")
    private String tipoSangre;
    @Size(max = 50)
    @Column(name = "capacidades_diferentes")
    private String capacidadesDiferentes;
    @Size(max = 50)
    @Column(name = "lengua_autoctona")
    private String lenguaAutoctona;
    @Size(max = 150)
    @Column(name = "comunidadIndigena")
    private String comunidadIndigena;
    @Column(name = "fam_diabetico")
    private Boolean famDiabetico;
    @Column(name = "fam_hipertenso")
    private Boolean famHipertenso;
    @Column(name = "fam_cardiaco")
    private Boolean famCardiaco;
    @Column(name = "fam_cancer")
    private Boolean famCancer;
    @Column(name = "fam_prog_oportunidades")
    private Boolean famProgOportunidades;
    @Column(name = "trabaja")
    private Boolean trabaja;
    @Size(max = 200)
    @Column(name = "lugar_trabajo")
    private String lugarTrabajo;
    @Size(max = 75)
    @Column(name = "especialidadIEMS")
    private String especialidadIEMS;
    @Size(max = 60)
    @Column(name = "medioDifusion")
    private String medioDifusion;
    @Size(max = 200)
    @Column(name = "nombreIEMS")
    private String nombreIEMS;
    @Size(max = 60)
    @Column(name = "localidad")
    private String localidad;
    @Size(max = 60)
    @Column(name = "municipios")
    private String municipios;
    @Size(max = 60)
    @Column(name = "estado")
    private String estado;
    @Size(max = 60)
    @Column(name = "localidadNacimiento")
    private String localidadNacimiento;
    @Size(max = 60)
    @Column(name = "municipioNacimiento")
    private String municipioNacimiento;
    @Size(max = 60)
    @Column(name = "estadoMunicipio")
    private String estadoMunicipio;
    @Size(max = 82)
    @Column(name = "nombreTutor")
    private String nombreTutor;
    @Size(max = 15)
    @Column(name = "telefonoTutor")
    private String telefonoTutor;
    @Column(name = "unica_opcion")
    private Boolean unicaOpcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "turnos")
    private String turnos;
    @Size(max = 30)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 60)
    @Column(name = "universidadProcedencia")
    private String universidadProcedencia;
    @Size(max = 255)
    @Column(name = "nombreOtraUni")
    private String nombreOtraUni;
    @Size(max = 82)
    @Column(name = "nombreTutorING")
    private String nombreTutorING;
    @Size(max = 15)
    @Column(name = "telTutorING")
    private String telTutorING;
    @Column(name = "trabajoING")
    private Boolean trabajoING;
    @Size(max = 200)
    @Column(name = "lugarTraIng")
    private String lugarTraIng;

    public ViewMatriculaF911() {
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getPeriodos() {
        return periodos;
    }

    public void setPeriodos(String periodos) {
        this.periodos = periodos;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Short getGrado() {
        return grado;
    }

    public void setGrado(Short grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getAlumno() {
        return alumno;
    }

    public void setAlumno(String alumno) {
        this.alumno = alumno;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public BigDecimal getEstatura() {
        return estatura;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getCapacidadesDiferentes() {
        return capacidadesDiferentes;
    }

    public void setCapacidadesDiferentes(String capacidadesDiferentes) {
        this.capacidadesDiferentes = capacidadesDiferentes;
    }

    public String getLenguaAutoctona() {
        return lenguaAutoctona;
    }

    public void setLenguaAutoctona(String lenguaAutoctona) {
        this.lenguaAutoctona = lenguaAutoctona;
    }

    public String getComunidadIndigena() {
        return comunidadIndigena;
    }

    public void setComunidadIndigena(String comunidadIndigena) {
        this.comunidadIndigena = comunidadIndigena;
    }

    public Boolean getFamDiabetico() {
        return famDiabetico;
    }

    public void setFamDiabetico(Boolean famDiabetico) {
        this.famDiabetico = famDiabetico;
    }

    public Boolean getFamHipertenso() {
        return famHipertenso;
    }

    public void setFamHipertenso(Boolean famHipertenso) {
        this.famHipertenso = famHipertenso;
    }

    public Boolean getFamCardiaco() {
        return famCardiaco;
    }

    public void setFamCardiaco(Boolean famCardiaco) {
        this.famCardiaco = famCardiaco;
    }

    public Boolean getFamCancer() {
        return famCancer;
    }

    public void setFamCancer(Boolean famCancer) {
        this.famCancer = famCancer;
    }

    public Boolean getFamProgOportunidades() {
        return famProgOportunidades;
    }

    public void setFamProgOportunidades(Boolean famProgOportunidades) {
        this.famProgOportunidades = famProgOportunidades;
    }

    public Boolean getTrabaja() {
        return trabaja;
    }

    public void setTrabaja(Boolean trabaja) {
        this.trabaja = trabaja;
    }

    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public String getEspecialidadIEMS() {
        return especialidadIEMS;
    }

    public void setEspecialidadIEMS(String especialidadIEMS) {
        this.especialidadIEMS = especialidadIEMS;
    }

    public String getMedioDifusion() {
        return medioDifusion;
    }

    public void setMedioDifusion(String medioDifusion) {
        this.medioDifusion = medioDifusion;
    }

    public String getNombreIEMS() {
        return nombreIEMS;
    }

    public void setNombreIEMS(String nombreIEMS) {
        this.nombreIEMS = nombreIEMS;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getMunicipios() {
        return municipios;
    }

    public void setMunicipios(String municipios) {
        this.municipios = municipios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLocalidadNacimiento() {
        return localidadNacimiento;
    }

    public void setLocalidadNacimiento(String localidadNacimiento) {
        this.localidadNacimiento = localidadNacimiento;
    }

    public String getMunicipioNacimiento() {
        return municipioNacimiento;
    }

    public void setMunicipioNacimiento(String municipioNacimiento) {
        this.municipioNacimiento = municipioNacimiento;
    }

    public String getEstadoMunicipio() {
        return estadoMunicipio;
    }

    public void setEstadoMunicipio(String estadoMunicipio) {
        this.estadoMunicipio = estadoMunicipio;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public String getTelefonoTutor() {
        return telefonoTutor;
    }

    public void setTelefonoTutor(String telefonoTutor) {
        this.telefonoTutor = telefonoTutor;
    }

    public Boolean getUnicaOpcion() {
        return unicaOpcion;
    }

    public void setUnicaOpcion(Boolean unicaOpcion) {
        this.unicaOpcion = unicaOpcion;
    }

    public String getTurnos() {
        return turnos;
    }

    public void setTurnos(String turnos) {
        this.turnos = turnos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUniversidadProcedencia() {
        return universidadProcedencia;
    }

    public void setUniversidadProcedencia(String universidadProcedencia) {
        this.universidadProcedencia = universidadProcedencia;
    }

    public String getNombreOtraUni() {
        return nombreOtraUni;
    }

    public void setNombreOtraUni(String nombreOtraUni) {
        this.nombreOtraUni = nombreOtraUni;
    }

    public String getNombreTutorING() {
        return nombreTutorING;
    }

    public void setNombreTutorING(String nombreTutorING) {
        this.nombreTutorING = nombreTutorING;
    }

    public String getTelTutorING() {
        return telTutorING;
    }

    public void setTelTutorING(String telTutorING) {
        this.telTutorING = telTutorING;
    }

    public Boolean getTrabajoING() {
        return trabajoING;
    }

    public void setTrabajoING(Boolean trabajoING) {
        this.trabajoING = trabajoING;
    }

    public String getLugarTraIng() {
        return lugarTraIng;
    }

    public void setLugarTraIng(String lugarTraIng) {
        this.lugarTraIng = lugarTraIng;
    }
    
}
