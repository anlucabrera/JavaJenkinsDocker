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
@Table(name = "viewparticipantesactformintegral", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viewparticipantesactformintegral.findAll", query = "SELECT v FROM Viewparticipantesactformintegral v")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByAreaReg", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.areaReg = :areaReg")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByRegAct", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.regAct = :regAct")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByActividadFormacionIntegral", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.actividadFormacionIntegral = :actividadFormacionIntegral")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByFechaInicio", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByFechaFin", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.fechaFin = :fechaFin")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByNombre", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByTipoActividad", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.tipoActividad = :tipoActividad")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByTipoEvento", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.tipoEvento = :tipoEvento")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByMes", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.mes = :mes")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByAnio", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.anio = :anio")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByArea", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.area = :area")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByPrograma", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.programa = :programa")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByNivel", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.nivel = :nivel")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByRegParticipante", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.regParticipante = :regParticipante")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByCiclo", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.ciclo = :ciclo")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByPeriodo", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.periodo = :periodo")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByFechaNacimiento", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByGenero", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.genero = :genero")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByCurp", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.curp = :curp")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByMatricula", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByCuatrimestre", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByLenguaIndigena", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.lenguaIndigena = :lenguaIndigena")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByDiscapacidad", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.discapacidad = :discapacidad")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByComunidadIndigena", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.comunidadIndigena = :comunidadIndigena")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByEdad", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.edad = :edad")
    , @NamedQuery(name = "Viewparticipantesactformintegral.findByOrganizaParticipa", query = "SELECT v FROM Viewparticipantesactformintegral v WHERE v.organizaParticipa = :organizaParticipa")})
public class Viewparticipantesactformintegral implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "areaReg")
    private short areaReg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "regAct")
    private int regAct;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "actividad_formacion_integral")
    private String actividadFormacionIntegral;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "tipoActividad")
    private String tipoActividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "tipoEvento")
    private String tipoEvento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "mes")
    private String mes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private short anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cveArea")
    private short cveArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Area")
    private String area;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Programa")
    private String programa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "nivel")
    private String nivel;
    @Id
    @Basic(optional = false)
    @NotNull()
    @Column(name = "regParticipante")
    private int regParticipante;
    @Size(max = 9)
    @Column(name = "ciclo")
    private String ciclo;
    @Size(max = 41)
    @Column(name = "periodo")
    private String periodo;
    @Size(max = 10)
    @Column(name = "fechaNacimiento")
    private String fechaNacimiento;
    @Size(max = 1)
    @Column(name = "genero")
    private String genero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cuatrimestre")
    private String cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "lenguaIndigena")
    private String lenguaIndigena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "discapacidad")
    private String discapacidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "comunidadIndigena")
    private String comunidadIndigena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "organiza_participa")
    private String organizaParticipa;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "edad")
    private Double edad;

    public Viewparticipantesactformintegral() {
    }


    public String getActividadFormacionIntegral() {
        return actividadFormacionIntegral;
    }

    public void setActividadFormacionIntegral(String actividadFormacionIntegral) {
        this.actividadFormacionIntegral = actividadFormacionIntegral;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    public Double getEdad() {
        return edad;
    }
    public void setEdad(Double edad) {
        this.edad = edad;
    }
    public String getOrganizaParticipa() {
        return organizaParticipa;
    }
    public void setOrganizaParticipa(String organizaParticipa) {
        this.organizaParticipa = organizaParticipa;
    }

    public short getAreaReg() {
        return areaReg;
    }

    public void setAreaReg(short areaReg) {
        this.areaReg = areaReg;
    }

    public int getRegAct() {
        return regAct;
    }

    public void setRegAct(int regAct) {
        this.regAct = regAct;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public short getCveArea() {
        return cveArea;
    }

    public void setCveArea(short cveArea) {
        this.cveArea = cveArea;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getRegParticipante() {
        return regParticipante;
    }

    public void setRegParticipante(int regParticipante) {
        this.regParticipante = regParticipante;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(String cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getLenguaIndigena() {
        return lenguaIndigena;
    }

    public void setLenguaIndigena(String lenguaIndigena) {
        this.lenguaIndigena = lenguaIndigena;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getComunidadIndigena() {
        return comunidadIndigena;
    }

    public void setComunidadIndigena(String comunidadIndigena) {
        this.comunidadIndigena = comunidadIndigena;
    }

}
