/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.sescolares;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "alumno", catalog = "sescolares", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a")
    , @NamedQuery(name = "Alumno.findByIdalumno", query = "SELECT a FROM Alumno a WHERE a.idalumno = :idalumno")
    , @NamedQuery(name = "Alumno.findByMatricula", query = "SELECT a FROM Alumno a WHERE a.alumnoPK.matricula = :matricula")
    , @NamedQuery(name = "Alumno.findByPeriodo", query = "SELECT a FROM Alumno a WHERE a.alumnoPK.periodo = :periodo")
    , @NamedQuery(name = "Alumno.findByPe", query = "SELECT a FROM Alumno a WHERE a.pe = :pe")
    , @NamedQuery(name = "Alumno.findByCuatrimestre", query = "SELECT a FROM Alumno a WHERE a.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "Alumno.findByGrupo", query = "SELECT a FROM Alumno a WHERE a.grupo = :grupo")
    , @NamedQuery(name = "Alumno.findByApellidoPaterno", query = "SELECT a FROM Alumno a WHERE a.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "Alumno.findByApellidoMaterno", query = "SELECT a FROM Alumno a WHERE a.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "Alumno.findByNombre", query = "SELECT a FROM Alumno a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Alumno.findByFechaNacimiento", query = "SELECT a FROM Alumno a WHERE a.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Alumno.findByGenero", query = "SELECT a FROM Alumno a WHERE a.genero = :genero")
    , @NamedQuery(name = "Alumno.findByCurp", query = "SELECT a FROM Alumno a WHERE a.curp = :curp")
    , @NamedQuery(name = "Alumno.findByEstatura", query = "SELECT a FROM Alumno a WHERE a.estatura = :estatura")
    , @NamedQuery(name = "Alumno.findByPeso", query = "SELECT a FROM Alumno a WHERE a.peso = :peso")
    , @NamedQuery(name = "Alumno.findByTipoDiscapacidad", query = "SELECT a FROM Alumno a WHERE a.tipoDiscapacidad = :tipoDiscapacidad")
    , @NamedQuery(name = "Alumno.findByLenguaIndigenaP", query = "SELECT a FROM Alumno a WHERE a.lenguaIndigenaP = :lenguaIndigenaP")
    , @NamedQuery(name = "Alumno.findByComunidadIndigena", query = "SELECT a FROM Alumno a WHERE a.comunidadIndigena = :comunidadIndigena")
    , @NamedQuery(name = "Alumno.findByDiabetico", query = "SELECT a FROM Alumno a WHERE a.diabetico = :diabetico")
    , @NamedQuery(name = "Alumno.findByHipertenso", query = "SELECT a FROM Alumno a WHERE a.hipertenso = :hipertenso")
    , @NamedQuery(name = "Alumno.findByCardiaco", query = "SELECT a FROM Alumno a WHERE a.cardiaco = :cardiaco")
    , @NamedQuery(name = "Alumno.findByCancer", query = "SELECT a FROM Alumno a WHERE a.cancer = :cancer")
    , @NamedQuery(name = "Alumno.findByProgramaOportunidades", query = "SELECT a FROM Alumno a WHERE a.programaOportunidades = :programaOportunidades")
    , @NamedQuery(name = "Alumno.findByLugarTrabajo", query = "SELECT a FROM Alumno a WHERE a.lugarTrabajo = :lugarTrabajo")
    , @NamedQuery(name = "Alumno.findByMedioDifusion", query = "SELECT a FROM Alumno a WHERE a.medioDifusion = :medioDifusion")
    , @NamedQuery(name = "Alumno.findByCorreoElectronico", query = "SELECT a FROM Alumno a WHERE a.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "Alumno.findByNombreIEMS", query = "SELECT a FROM Alumno a WHERE a.nombreIEMS = :nombreIEMS")
    , @NamedQuery(name = "Alumno.findByEstadoIEMS", query = "SELECT a FROM Alumno a WHERE a.estadoIEMS = :estadoIEMS")
    , @NamedQuery(name = "Alumno.findByMunicipioIEMS", query = "SELECT a FROM Alumno a WHERE a.municipioIEMS = :municipioIEMS")
    , @NamedQuery(name = "Alumno.findByLocalidadIEMS", query = "SELECT a FROM Alumno a WHERE a.localidadIEMS = :localidadIEMS")
    , @NamedQuery(name = "Alumno.findByCalle", query = "SELECT a FROM Alumno a WHERE a.calle = :calle")
    , @NamedQuery(name = "Alumno.findByNumero", query = "SELECT a FROM Alumno a WHERE a.numero = :numero")
    , @NamedQuery(name = "Alumno.findByColonia", query = "SELECT a FROM Alumno a WHERE a.colonia = :colonia")
    , @NamedQuery(name = "Alumno.findByCp", query = "SELECT a FROM Alumno a WHERE a.cp = :cp")
    , @NamedQuery(name = "Alumno.findByEstadoActual", query = "SELECT a FROM Alumno a WHERE a.estadoActual = :estadoActual")
    , @NamedQuery(name = "Alumno.findByMunicipioActual", query = "SELECT a FROM Alumno a WHERE a.municipioActual = :municipioActual")
    , @NamedQuery(name = "Alumno.findByLocalidadActual", query = "SELECT a FROM Alumno a WHERE a.localidadActual = :localidadActual")
    , @NamedQuery(name = "Alumno.findByTelefonoAlumno", query = "SELECT a FROM Alumno a WHERE a.telefonoAlumno = :telefonoAlumno")
    , @NamedQuery(name = "Alumno.findByEstadoNacimiento", query = "SELECT a FROM Alumno a WHERE a.estadoNacimiento = :estadoNacimiento")
    , @NamedQuery(name = "Alumno.findByMunicipioNacimiento", query = "SELECT a FROM Alumno a WHERE a.municipioNacimiento = :municipioNacimiento")
    , @NamedQuery(name = "Alumno.findByLocalidadNacimiento", query = "SELECT a FROM Alumno a WHERE a.localidadNacimiento = :localidadNacimiento")
    , @NamedQuery(name = "Alumno.findByNombreTutor", query = "SELECT a FROM Alumno a WHERE a.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "Alumno.findByTelefonoTutor", query = "SELECT a FROM Alumno a WHERE a.telefonoTutor = :telefonoTutor")
    , @NamedQuery(name = "Alumno.findByIpo", query = "SELECT a FROM Alumno a WHERE a.ipo = :ipo")
    , @NamedQuery(name = "Alumno.findByEsc", query = "SELECT a FROM Alumno a WHERE a.esc = :esc")
    , @NamedQuery(name = "Alumno.findByPout", query = "SELECT a FROM Alumno a WHERE a.pout = :pout")
    , @NamedQuery(name = "Alumno.findByTurno", query = "SELECT a FROM Alumno a WHERE a.turno = :turno")
    , @NamedQuery(name = "Alumno.findByEstatus", query = "SELECT a FROM Alumno a WHERE a.estatus = :estatus")
    , @NamedQuery(name = "Alumno.findByNotas", query = "SELECT a FROM Alumno a WHERE a.notas = :notas")})
public class Alumno implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlumnoPK alumnoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idalumno")
    private int idalumno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pe")
    private int pe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private int cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "grupo")
    private String grupo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaNacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "genero")
    private String genero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatura")
    private double estatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private double peso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "tipoDiscapacidad")
    private String tipoDiscapacidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "lenguaIndigenaP")
    private String lenguaIndigenaP;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "comunidadIndigena")
    private String comunidadIndigena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "diabetico")
    private String diabetico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "hipertenso")
    private String hipertenso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "cardiaco")
    private String cardiaco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "cancer")
    private String cancer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "programaOportunidades")
    private String programaOportunidades;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "lugarTrabajo")
    private String lugarTrabajo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "medioDifusion")
    private String medioDifusion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "correoElectronico")
    private String correoElectronico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombreIEMS")
    private String nombreIEMS;
    @Column(name = "estadoIEMS")
    private Integer estadoIEMS;
    @Column(name = "municipioIEMS")
    private Integer municipioIEMS;
    @Column(name = "localidadIEMS")
    private Integer localidadIEMS;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "colonia")
    private String colonia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cp")
    private String cp;
    @Column(name = "estadoActual")
    private Integer estadoActual;
    @Column(name = "municipioActual")
    private Integer municipioActual;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "localidadActual")
    private String localidadActual;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "telefonoAlumno")
    private String telefonoAlumno;
    @Column(name = "estadoNacimiento")
    private Integer estadoNacimiento;
    @Column(name = "municipioNacimiento")
    private Integer municipioNacimiento;
    @Column(name = "localidadNacimiento")
    private Integer localidadNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombreTutor")
    private String nombreTutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "telefonoTutor")
    private String telefonoTutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "IPO")
    private String ipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "ESC")
    private String esc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "POUT")
    private String pout;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "turno")
    private String turno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 10)
    @Column(name = "notas")
    private String notas;

    public Alumno() {
    }

    public Alumno(AlumnoPK alumnoPK) {
        this.alumnoPK = alumnoPK;
    }

    public Alumno(AlumnoPK alumnoPK, int idalumno, int pe, int cuatrimestre, String grupo, String apellidoPaterno, String apellidoMaterno, String nombre, Date fechaNacimiento, String genero, String curp, double estatura, double peso, String tipoDiscapacidad, String lenguaIndigenaP, String comunidadIndigena, String diabetico, String hipertenso, String cardiaco, String cancer, String programaOportunidades, String lugarTrabajo, String medioDifusion, String correoElectronico, String nombreIEMS, String calle, String numero, String colonia, String cp, String localidadActual, String telefonoAlumno, String nombreTutor, String telefonoTutor, String ipo, String esc, String pout, String turno, String estatus) {
        this.alumnoPK = alumnoPK;
        this.idalumno = idalumno;
        this.pe = pe;
        this.cuatrimestre = cuatrimestre;
        this.grupo = grupo;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.curp = curp;
        this.estatura = estatura;
        this.peso = peso;
        this.tipoDiscapacidad = tipoDiscapacidad;
        this.lenguaIndigenaP = lenguaIndigenaP;
        this.comunidadIndigena = comunidadIndigena;
        this.diabetico = diabetico;
        this.hipertenso = hipertenso;
        this.cardiaco = cardiaco;
        this.cancer = cancer;
        this.programaOportunidades = programaOportunidades;
        this.lugarTrabajo = lugarTrabajo;
        this.medioDifusion = medioDifusion;
        this.correoElectronico = correoElectronico;
        this.nombreIEMS = nombreIEMS;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.cp = cp;
        this.localidadActual = localidadActual;
        this.telefonoAlumno = telefonoAlumno;
        this.nombreTutor = nombreTutor;
        this.telefonoTutor = telefonoTutor;
        this.ipo = ipo;
        this.esc = esc;
        this.pout = pout;
        this.turno = turno;
        this.estatus = estatus;
    }

    public Alumno(int matricula, int periodo) {
        this.alumnoPK = new AlumnoPK(matricula, periodo);
    }

    public AlumnoPK getAlumnoPK() {
        return alumnoPK;
    }

    public void setAlumnoPK(AlumnoPK alumnoPK) {
        this.alumnoPK = alumnoPK;
    }

    public int getIdalumno() {
        return idalumno;
    }

    public void setIdalumno(int idalumno) {
        this.idalumno = idalumno;
    }

    public int getPe() {
        return pe;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
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

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public String getLenguaIndigenaP() {
        return lenguaIndigenaP;
    }

    public void setLenguaIndigenaP(String lenguaIndigenaP) {
        this.lenguaIndigenaP = lenguaIndigenaP;
    }

    public String getComunidadIndigena() {
        return comunidadIndigena;
    }

    public void setComunidadIndigena(String comunidadIndigena) {
        this.comunidadIndigena = comunidadIndigena;
    }

    public String getDiabetico() {
        return diabetico;
    }

    public void setDiabetico(String diabetico) {
        this.diabetico = diabetico;
    }

    public String getHipertenso() {
        return hipertenso;
    }

    public void setHipertenso(String hipertenso) {
        this.hipertenso = hipertenso;
    }

    public String getCardiaco() {
        return cardiaco;
    }

    public void setCardiaco(String cardiaco) {
        this.cardiaco = cardiaco;
    }

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public String getProgramaOportunidades() {
        return programaOportunidades;
    }

    public void setProgramaOportunidades(String programaOportunidades) {
        this.programaOportunidades = programaOportunidades;
    }

    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public String getMedioDifusion() {
        return medioDifusion;
    }

    public void setMedioDifusion(String medioDifusion) {
        this.medioDifusion = medioDifusion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNombreIEMS() {
        return nombreIEMS;
    }

    public void setNombreIEMS(String nombreIEMS) {
        this.nombreIEMS = nombreIEMS;
    }

    public Integer getEstadoIEMS() {
        return estadoIEMS;
    }

    public void setEstadoIEMS(Integer estadoIEMS) {
        this.estadoIEMS = estadoIEMS;
    }

    public Integer getMunicipioIEMS() {
        return municipioIEMS;
    }

    public void setMunicipioIEMS(Integer municipioIEMS) {
        this.municipioIEMS = municipioIEMS;
    }

    public Integer getLocalidadIEMS() {
        return localidadIEMS;
    }

    public void setLocalidadIEMS(Integer localidadIEMS) {
        this.localidadIEMS = localidadIEMS;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Integer getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(Integer estadoActual) {
        this.estadoActual = estadoActual;
    }

    public Integer getMunicipioActual() {
        return municipioActual;
    }

    public void setMunicipioActual(Integer municipioActual) {
        this.municipioActual = municipioActual;
    }

    public String getLocalidadActual() {
        return localidadActual;
    }

    public void setLocalidadActual(String localidadActual) {
        this.localidadActual = localidadActual;
    }

    public String getTelefonoAlumno() {
        return telefonoAlumno;
    }

    public void setTelefonoAlumno(String telefonoAlumno) {
        this.telefonoAlumno = telefonoAlumno;
    }

    public Integer getEstadoNacimiento() {
        return estadoNacimiento;
    }

    public void setEstadoNacimiento(Integer estadoNacimiento) {
        this.estadoNacimiento = estadoNacimiento;
    }

    public Integer getMunicipioNacimiento() {
        return municipioNacimiento;
    }

    public void setMunicipioNacimiento(Integer municipioNacimiento) {
        this.municipioNacimiento = municipioNacimiento;
    }

    public Integer getLocalidadNacimiento() {
        return localidadNacimiento;
    }

    public void setLocalidadNacimiento(Integer localidadNacimiento) {
        this.localidadNacimiento = localidadNacimiento;
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

    public String getIpo() {
        return ipo;
    }

    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    public String getEsc() {
        return esc;
    }

    public void setEsc(String esc) {
        this.esc = esc;
    }

    public String getPout() {
        return pout;
    }

    public void setPout(String pout) {
        this.pout = pout;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alumnoPK != null ? alumnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.alumnoPK == null && other.alumnoPK != null) || (this.alumnoPK != null && !this.alumnoPK.equals(other.alumnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.sescolares.Alumno[ alumnoPK=" + alumnoPK + " ]";
    }
    
}
