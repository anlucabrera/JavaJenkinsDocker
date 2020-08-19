/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "personal_bitacora", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonalBitacora.findAll", query = "SELECT p FROM PersonalBitacora p")
    , @NamedQuery(name = "PersonalBitacora.findByBitacora", query = "SELECT p FROM PersonalBitacora p WHERE p.bitacora = :bitacora")
    , @NamedQuery(name = "PersonalBitacora.findByFechaCambio", query = "SELECT p FROM PersonalBitacora p WHERE p.fechaCambio = :fechaCambio")
    , @NamedQuery(name = "PersonalBitacora.findByClave", query = "SELECT p FROM PersonalBitacora p WHERE p.clave = :clave")
    , @NamedQuery(name = "PersonalBitacora.findByNombre", query = "SELECT p FROM PersonalBitacora p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PersonalBitacora.findByFechaIngreso", query = "SELECT p FROM PersonalBitacora p WHERE p.fechaIngreso = :fechaIngreso")
    , @NamedQuery(name = "PersonalBitacora.findByStatus", query = "SELECT p FROM PersonalBitacora p WHERE p.status = :status")
    , @NamedQuery(name = "PersonalBitacora.findByAreaOperativa", query = "SELECT p FROM PersonalBitacora p WHERE p.areaOperativa = :areaOperativa")
    , @NamedQuery(name = "PersonalBitacora.findByAreaSuperior", query = "SELECT p FROM PersonalBitacora p WHERE p.areaSuperior = :areaSuperior")
    , @NamedQuery(name = "PersonalBitacora.findByAreaOficial", query = "SELECT p FROM PersonalBitacora p WHERE p.areaOficial = :areaOficial")
    , @NamedQuery(name = "PersonalBitacora.findByPerfilProfesional", query = "SELECT p FROM PersonalBitacora p WHERE p.perfilProfesional = :perfilProfesional")
    , @NamedQuery(name = "PersonalBitacora.findByExperienciaDocente", query = "SELECT p FROM PersonalBitacora p WHERE p.experienciaDocente = :experienciaDocente")
    , @NamedQuery(name = "PersonalBitacora.findByExperienciaLaboral", query = "SELECT p FROM PersonalBitacora p WHERE p.experienciaLaboral = :experienciaLaboral")
    , @NamedQuery(name = "PersonalBitacora.findByFechaNacimiento", query = "SELECT p FROM PersonalBitacora p WHERE p.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "PersonalBitacora.findByEstado", query = "SELECT p FROM PersonalBitacora p WHERE p.estado = :estado")
    , @NamedQuery(name = "PersonalBitacora.findByMunicipio", query = "SELECT p FROM PersonalBitacora p WHERE p.municipio = :municipio")
    , @NamedQuery(name = "PersonalBitacora.findByLocalidad", query = "SELECT p FROM PersonalBitacora p WHERE p.localidad = :localidad")
    , @NamedQuery(name = "PersonalBitacora.findByPais", query = "SELECT p FROM PersonalBitacora p WHERE p.pais = :pais")
    , @NamedQuery(name = "PersonalBitacora.findBySni", query = "SELECT p FROM PersonalBitacora p WHERE p.sni = :sni")
    , @NamedQuery(name = "PersonalBitacora.findByPerfilProdep", query = "SELECT p FROM PersonalBitacora p WHERE p.perfilProdep = :perfilProdep")
    , @NamedQuery(name = "PersonalBitacora.findByCorreoElectronico", query = "SELECT p FROM PersonalBitacora p WHERE p.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "PersonalBitacora.findByCorreoElectronico2", query = "SELECT p FROM PersonalBitacora p WHERE p.correoElectronico2 = :correoElectronico2")})
public class PersonalBitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bitacora")
    private Integer bitacora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cambio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCambio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private Character status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_operativa")
    private short areaOperativa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_superior")
    private short areaSuperior;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_oficial")
    private short areaOficial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "perfil_profesional")
    private String perfilProfesional;
    @Basic(optional = false)
    @NotNull
    @Column(name = "experiencia_docente")
    private short experienciaDocente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "experiencia_laboral")
    private short experienciaLaboral;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "municipio")
    private String municipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "localidad")
    private String localidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "pais")
    private String pais;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sni")
    private boolean sni;
    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil_prodep")
    private boolean perfilProdep;
    @Size(max = 200)
    @Column(name = "correo_electronico")
    private String correoElectronico;
    @Size(max = 200)
    @Column(name = "correo_electronico2")
    private String correoElectronico2;
    @JoinColumn(name = "categoria_360", referencedColumnName = "categoria")
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalCategorias categoria360;
    @JoinColumn(name = "categoria_especifica", referencedColumnName = "categoriaEspecifica")
    @ManyToOne(fetch = FetchType.LAZY)
    private Categoriasespecificasfunciones categoriaEspecifica;
    @JoinColumn(name = "actividad", referencedColumnName = "actividad")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Actividades actividad;
    @JoinColumn(name = "categoria_operativa", referencedColumnName = "categoria")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PersonalCategorias categoriaOperativa;
    @JoinColumn(name = "categoria_oficial", referencedColumnName = "categoria")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PersonalCategorias categoriaOficial;
    @JoinColumn(name = "genero", referencedColumnName = "genero")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Generos genero;
    @JoinColumn(name = "grado", referencedColumnName = "grado")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Grados grado;

    public PersonalBitacora() {
    }

    public PersonalBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public PersonalBitacora(Integer bitacora, Date fechaCambio, int clave, String nombre, Date fechaIngreso, Character status, short areaOperativa, short areaSuperior, short areaOficial, String perfilProfesional, short experienciaDocente, short experienciaLaboral, Date fechaNacimiento, String estado, String municipio, String localidad, String pais, boolean sni, boolean perfilProdep) {
        this.bitacora = bitacora;
        this.fechaCambio = fechaCambio;
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

    public Integer getBitacora() {
        return bitacora;
    }

    public void setBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bitacora != null ? bitacora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalBitacora)) {
            return false;
        }
        PersonalBitacora other = (PersonalBitacora) object;
        if ((this.bitacora == null && other.bitacora != null) || (this.bitacora != null && !this.bitacora.equals(other.bitacora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.PersonalBitacora[ bitacora=" + bitacora + " ]";
    }
    
}
