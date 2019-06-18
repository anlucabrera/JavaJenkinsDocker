/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "aspirantes", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aspirantes.findAll", query = "SELECT a FROM Aspirantes a")
    , @NamedQuery(name = "Aspirantes.findByCveAspirante", query = "SELECT a FROM Aspirantes a WHERE a.aspirantesPK.cveAspirante = :cveAspirante")
    , @NamedQuery(name = "Aspirantes.findByFolioCeneval", query = "SELECT a FROM Aspirantes a WHERE a.aspirantesPK.folioCeneval = :folioCeneval")
    , @NamedQuery(name = "Aspirantes.findByCveUniversidad", query = "SELECT a FROM Aspirantes a WHERE a.aspirantesPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Aspirantes.findByCveProceso", query = "SELECT a FROM Aspirantes a WHERE a.aspirantesPK.cveProceso = :cveProceso")
    , @NamedQuery(name = "Aspirantes.findByCvePeriodo", query = "SELECT a FROM Aspirantes a WHERE a.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "Aspirantes.findByCveCarrera1", query = "SELECT a FROM Aspirantes a WHERE a.cveCarrera1 = :cveCarrera1")
    , @NamedQuery(name = "Aspirantes.findByCveDivision1", query = "SELECT a FROM Aspirantes a WHERE a.cveDivision1 = :cveDivision1")
    , @NamedQuery(name = "Aspirantes.findByCveUnidadAcademica1", query = "SELECT a FROM Aspirantes a WHERE a.cveUnidadAcademica1 = :cveUnidadAcademica1")
    , @NamedQuery(name = "Aspirantes.findByCveTurno1", query = "SELECT a FROM Aspirantes a WHERE a.cveTurno1 = :cveTurno1")
    , @NamedQuery(name = "Aspirantes.findByCveTurno12", query = "SELECT a FROM Aspirantes a WHERE a.cveTurno12 = :cveTurno12")
    , @NamedQuery(name = "Aspirantes.findByCveCarrera2", query = "SELECT a FROM Aspirantes a WHERE a.cveCarrera2 = :cveCarrera2")
    , @NamedQuery(name = "Aspirantes.findByCveDivision2", query = "SELECT a FROM Aspirantes a WHERE a.cveDivision2 = :cveDivision2")
    , @NamedQuery(name = "Aspirantes.findByCveUnidadAcademica2", query = "SELECT a FROM Aspirantes a WHERE a.cveUnidadAcademica2 = :cveUnidadAcademica2")
    , @NamedQuery(name = "Aspirantes.findByCveTurno2", query = "SELECT a FROM Aspirantes a WHERE a.cveTurno2 = :cveTurno2")
    , @NamedQuery(name = "Aspirantes.findByCveTurno22", query = "SELECT a FROM Aspirantes a WHERE a.cveTurno22 = :cveTurno22")
    , @NamedQuery(name = "Aspirantes.findByCveEspecialidadEscuela", query = "SELECT a FROM Aspirantes a WHERE a.cveEspecialidadEscuela = :cveEspecialidadEscuela")
    , @NamedQuery(name = "Aspirantes.findByCveEscuelaProcedencia", query = "SELECT a FROM Aspirantes a WHERE a.cveEscuelaProcedencia = :cveEscuelaProcedencia")
    , @NamedQuery(name = "Aspirantes.findByCveEstadoNacimiento", query = "SELECT a FROM Aspirantes a WHERE a.cveEstadoNacimiento = :cveEstadoNacimiento")
    , @NamedQuery(name = "Aspirantes.findByCveMunicipioNacimiento", query = "SELECT a FROM Aspirantes a WHERE a.cveMunicipioNacimiento = :cveMunicipioNacimiento")
    , @NamedQuery(name = "Aspirantes.findByCveLocalidadNacimiento", query = "SELECT a FROM Aspirantes a WHERE a.cveLocalidadNacimiento = :cveLocalidadNacimiento")
    , @NamedQuery(name = "Aspirantes.findByFechaRegistro", query = "SELECT a FROM Aspirantes a WHERE a.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "Aspirantes.findByTrabaja", query = "SELECT a FROM Aspirantes a WHERE a.trabaja = :trabaja")
    , @NamedQuery(name = "Aspirantes.findByLugarTrabajo", query = "SELECT a FROM Aspirantes a WHERE a.lugarTrabajo = :lugarTrabajo")
    , @NamedQuery(name = "Aspirantes.findByPromedioBachillerato", query = "SELECT a FROM Aspirantes a WHERE a.promedioBachillerato = :promedioBachillerato")
    , @NamedQuery(name = "Aspirantes.findByApellidoPaternoTutor", query = "SELECT a FROM Aspirantes a WHERE a.apellidoPaternoTutor = :apellidoPaternoTutor")
    , @NamedQuery(name = "Aspirantes.findByApellidoMaternoTutor", query = "SELECT a FROM Aspirantes a WHERE a.apellidoMaternoTutor = :apellidoMaternoTutor")
    , @NamedQuery(name = "Aspirantes.findByNombreTutor", query = "SELECT a FROM Aspirantes a WHERE a.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "Aspirantes.findByAnoBachInicio", query = "SELECT a FROM Aspirantes a WHERE a.anoBachInicio = :anoBachInicio")
    , @NamedQuery(name = "Aspirantes.findByAnoBachFin", query = "SELECT a FROM Aspirantes a WHERE a.anoBachFin = :anoBachFin")
    , @NamedQuery(name = "Aspirantes.findByIntentosUt", query = "SELECT a FROM Aspirantes a WHERE a.intentosUt = :intentosUt")
    , @NamedQuery(name = "Aspirantes.findByIntentosOtra", query = "SELECT a FROM Aspirantes a WHERE a.intentosOtra = :intentosOtra")
    , @NamedQuery(name = "Aspirantes.findByFolioInternoUt", query = "SELECT a FROM Aspirantes a WHERE a.folioInternoUt = :folioInternoUt")
    , @NamedQuery(name = "Aspirantes.findByPreregistrado", query = "SELECT a FROM Aspirantes a WHERE a.preregistrado = :preregistrado")
    , @NamedQuery(name = "Aspirantes.findByAprobado", query = "SELECT a FROM Aspirantes a WHERE a.aprobado = :aprobado")
    , @NamedQuery(name = "Aspirantes.findByCondicionado", query = "SELECT a FROM Aspirantes a WHERE a.condicionado = :condicionado")
    , @NamedQuery(name = "Aspirantes.findByCveCapturo", query = "SELECT a FROM Aspirantes a WHERE a.cveCapturo = :cveCapturo")
    , @NamedQuery(name = "Aspirantes.findByExMatricula", query = "SELECT a FROM Aspirantes a WHERE a.exMatricula = :exMatricula")
    , @NamedQuery(name = "Aspirantes.findByTelefonoTutor", query = "SELECT a FROM Aspirantes a WHERE a.telefonoTutor = :telefonoTutor")
    , @NamedQuery(name = "Aspirantes.findByCvemedioDifusion", query = "SELECT a FROM Aspirantes a WHERE a.cvemedioDifusion = :cvemedioDifusion")
    , @NamedQuery(name = "Aspirantes.findByFechaValidacion", query = "SELECT a FROM Aspirantes a WHERE a.fechaValidacion = :fechaValidacion")})
public class Aspirantes implements Serializable {

    private static final long serialVersionUID = 1L;
     
    @EmbeddedId
    protected AspirantesPK aspirantesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_periodo")
    private int cvePeriodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_carrera1")
    private int cveCarrera1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_division1")
    private int cveDivision1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_unidad_academica1")
    private int cveUnidadAcademica1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_turno1")
    private int cveTurno1;
    @Column(name = "cve_turno12")
    private Integer cveTurno12;
    @Column(name = "cve_carrera2")
    private Integer cveCarrera2;
    @Column(name = "cve_division2")
    private Integer cveDivision2;
    @Column(name = "cve_unidad_academica2")
    private Integer cveUnidadAcademica2;
    @Column(name = "cve_turno2")
    private Integer cveTurno2;
    @Column(name = "cve_turno22")
    private Integer cveTurno22;
    @Column(name = "cve_especialidad_escuela")
    private Integer cveEspecialidadEscuela;
    @Column(name = "cve_escuela_procedencia")
    private Integer cveEscuelaProcedencia;
    @Column(name = "cve_estado_nacimiento")
    private Integer cveEstadoNacimiento;
    @Column(name = "cve_municipio_nacimiento")
    private Integer cveMunicipioNacimiento;
    @Column(name = "cve_localidad_nacimiento")
    private Integer cveLocalidadNacimiento;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "trabaja")
    private Boolean trabaja;
    @Size(max = 200)
    @Column(name = "lugar_trabajo")
    private String lugarTrabajo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio_bachillerato")
    private BigDecimal promedioBachillerato;
    @Size(max = 20)
    @Column(name = "apellido_paterno_tutor")
    private String apellidoPaternoTutor;
    @Size(max = 20)
    @Column(name = "apellido_materno_tutor")
    private String apellidoMaternoTutor;
    @Size(max = 40)
    @Column(name = "nombre_tutor")
    private String nombreTutor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ano_bach_inicio")
    private int anoBachInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ano_bach_fin")
    private int anoBachFin;
    @Column(name = "intentos_ut")
    private Integer intentosUt;
    @Column(name = "intentos_otra")
    private Boolean intentosOtra;
    @Size(max = 20)
    @Column(name = "folio_interno_ut")
    private String folioInternoUt;
    @Column(name = "preregistrado")
    private Boolean preregistrado;
    @Column(name = "aprobado")
    private Boolean aprobado;
    @Column(name = "condicionado")
    private Boolean condicionado;
    @Column(name = "cve_capturo")
    private Integer cveCapturo;
    @Size(max = 11)
    @Column(name = "ex_matricula")
    private String exMatricula;
    @Size(max = 15)
    @Column(name = "telefonoTutor")
    private String telefonoTutor;
    @Column(name = "cve_medioDifusion")
    private Integer cvemedioDifusion;
    @Column(name = "fechaValidacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion;

    public Aspirantes() {
    }

    public Aspirantes(AspirantesPK aspirantesPK) {
        this.aspirantesPK = aspirantesPK;
    }
    
    public Aspirantes(Integer cveAspirante, String folioCeneval, int cveUniversidad, int cveProceso) {
        this.aspirantesPK = new AspirantesPK(cveAspirante, folioCeneval, cveUniversidad, cveProceso);
        
    }

    public AspirantesPK getAspirantesPK() {
        return aspirantesPK;
    }

    public void setAspirantesPK(AspirantesPK aspirantesPK) {
        this.aspirantesPK = aspirantesPK;
    }

    public int getCvePeriodo() {
        return cvePeriodo;
    }

    public void setCvePeriodo(int cvePeriodo) {
        this.cvePeriodo = cvePeriodo;
    }

    public int getCveCarrera1() {
        return cveCarrera1;
    }

    public void setCveCarrera1(int cveCarrera1) {
        this.cveCarrera1 = cveCarrera1;
    }

    public int getCveDivision1() {
        return cveDivision1;
    }

    public void setCveDivision1(int cveDivision1) {
        this.cveDivision1 = cveDivision1;
    }

    public int getCveUnidadAcademica1() {
        return cveUnidadAcademica1;
    }

    public void setCveUnidadAcademica1(int cveUnidadAcademica1) {
        this.cveUnidadAcademica1 = cveUnidadAcademica1;
    }

    public int getCveTurno1() {
        return cveTurno1;
    }

    public void setCveTurno1(int cveTurno1) {
        this.cveTurno1 = cveTurno1;
    }

    public Integer getCveTurno12() {
        return cveTurno12;
    }

    public void setCveTurno12(Integer cveTurno12) {
        this.cveTurno12 = cveTurno12;
    }

    public Integer getCveCarrera2() {
        return cveCarrera2;
    }

    public void setCveCarrera2(Integer cveCarrera2) {
        this.cveCarrera2 = cveCarrera2;
    }

    public Integer getCveDivision2() {
        return cveDivision2;
    }

    public void setCveDivision2(Integer cveDivision2) {
        this.cveDivision2 = cveDivision2;
    }

    public Integer getCveUnidadAcademica2() {
        return cveUnidadAcademica2;
    }

    public void setCveUnidadAcademica2(Integer cveUnidadAcademica2) {
        this.cveUnidadAcademica2 = cveUnidadAcademica2;
    }

    public Integer getCveTurno2() {
        return cveTurno2;
    }

    public void setCveTurno2(Integer cveTurno2) {
        this.cveTurno2 = cveTurno2;
    }

    public Integer getCveTurno22() {
        return cveTurno22;
    }

    public void setCveTurno22(Integer cveTurno22) {
        this.cveTurno22 = cveTurno22;
    }

    public Integer getCveEspecialidadEscuela() {
        return cveEspecialidadEscuela;
    }

    public void setCveEspecialidadEscuela(Integer cveEspecialidadEscuela) {
        this.cveEspecialidadEscuela = cveEspecialidadEscuela;
    }

    public Integer getCveEscuelaProcedencia() {
        return cveEscuelaProcedencia;
    }

    public void setCveEscuelaProcedencia(Integer cveEscuelaProcedencia) {
        this.cveEscuelaProcedencia = cveEscuelaProcedencia;
    }

    public Integer getCveEstadoNacimiento() {
        return cveEstadoNacimiento;
    }

    public void setCveEstadoNacimiento(Integer cveEstadoNacimiento) {
        this.cveEstadoNacimiento = cveEstadoNacimiento;
    }

    public Integer getCveMunicipioNacimiento() {
        return cveMunicipioNacimiento;
    }

    public void setCveMunicipioNacimiento(Integer cveMunicipioNacimiento) {
        this.cveMunicipioNacimiento = cveMunicipioNacimiento;
    }

    public Integer getCveLocalidadNacimiento() {
        return cveLocalidadNacimiento;
    }

    public void setCveLocalidadNacimiento(Integer cveLocalidadNacimiento) {
        this.cveLocalidadNacimiento = cveLocalidadNacimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public BigDecimal getPromedioBachillerato() {
        return promedioBachillerato;
    }

    public void setPromedioBachillerato(BigDecimal promedioBachillerato) {
        this.promedioBachillerato = promedioBachillerato;
    }

    public String getApellidoPaternoTutor() {
        return apellidoPaternoTutor;
    }

    public void setApellidoPaternoTutor(String apellidoPaternoTutor) {
        this.apellidoPaternoTutor = apellidoPaternoTutor;
    }

    public String getApellidoMaternoTutor() {
        return apellidoMaternoTutor;
    }

    public void setApellidoMaternoTutor(String apellidoMaternoTutor) {
        this.apellidoMaternoTutor = apellidoMaternoTutor;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }

    public int getAnoBachInicio() {
        return anoBachInicio;
    }

    public void setAnoBachInicio(int anoBachInicio) {
        this.anoBachInicio = anoBachInicio;
    }

    public int getAnoBachFin() {
        return anoBachFin;
    }

    public void setAnoBachFin(int anoBachFin) {
        this.anoBachFin = anoBachFin;
    }

    public Integer getIntentosUt() {
        return intentosUt;
    }

    public void setIntentosUt(Integer intentosUt) {
        this.intentosUt = intentosUt;
    }

    public Boolean getIntentosOtra() {
        return intentosOtra;
    }

    public void setIntentosOtra(Boolean intentosOtra) {
        this.intentosOtra = intentosOtra;
    }

    public String getFolioInternoUt() {
        return folioInternoUt;
    }

    public void setFolioInternoUt(String folioInternoUt) {
        this.folioInternoUt = folioInternoUt;
    }

    public Boolean getPreregistrado() {
        return preregistrado;
    }

    public void setPreregistrado(Boolean preregistrado) {
        this.preregistrado = preregistrado;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public Boolean getCondicionado() {
        return condicionado;
    }

    public void setCondicionado(Boolean condicionado) {
        this.condicionado = condicionado;
    }

    public Integer getCveCapturo() {
        return cveCapturo;
    }

    public void setCveCapturo(Integer cveCapturo) {
        this.cveCapturo = cveCapturo;
    }

    public String getExMatricula() {
        return exMatricula;
    }

    public void setExMatricula(String exMatricula) {
        this.exMatricula = exMatricula;
    }

    public String getTelefonoTutor() {
        return telefonoTutor;
    }

    public void setTelefonoTutor(String telefonoTutor) {
        this.telefonoTutor = telefonoTutor;
    }

    public Integer getCvemedioDifusion() {
        return cvemedioDifusion;
    }

    public void setCvemedioDifusion(Integer cvemedioDifusion) {
        this.cvemedioDifusion = cvemedioDifusion;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.aspirantesPK);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aspirantes)) {
            return false;
        }
        Aspirantes other = (Aspirantes) object;
        if ((this.aspirantesPK == null && other.aspirantesPK != null) || (this.aspirantesPK != null && !this.aspirantesPK.equals(other.aspirantesPK))) {
            return false;
        }
        return true;
    }

     @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Aspirantes[ aspirantesPK=" + aspirantesPK + " ]";
    }
    
}
