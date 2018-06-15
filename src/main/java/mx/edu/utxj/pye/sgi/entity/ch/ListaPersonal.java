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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "lista_personal", catalog = "capital_humano", schema = "")
@XmlRootElement
@ToString @AllArgsConstructor @NoArgsConstructor @RequiredArgsConstructor @lombok.EqualsAndHashCode(of = "clave")
@NamedQueries({
    @NamedQuery(name = "ListaPersonal.findAll", query = "SELECT l FROM ListaPersonal l")
    , @NamedQuery(name = "ListaPersonal.findByClave", query = "SELECT l FROM ListaPersonal l WHERE l.clave = :clave")
    , @NamedQuery(name = "ListaPersonal.findByNombre", query = "SELECT l FROM ListaPersonal l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "ListaPersonal.findByFechaIngreso", query = "SELECT l FROM ListaPersonal l WHERE l.fechaIngreso = :fechaIngreso")
    , @NamedQuery(name = "ListaPersonal.findByStatus", query = "SELECT l FROM ListaPersonal l WHERE l.status = :status")
    , @NamedQuery(name = "ListaPersonal.findByGenero", query = "SELECT l FROM ListaPersonal l WHERE l.genero = :genero")
    , @NamedQuery(name = "ListaPersonal.findByGeneroAbreviacion", query = "SELECT l FROM ListaPersonal l WHERE l.generoAbreviacion = :generoAbreviacion")
    , @NamedQuery(name = "ListaPersonal.findByGeneroNombre", query = "SELECT l FROM ListaPersonal l WHERE l.generoNombre = :generoNombre")
    , @NamedQuery(name = "ListaPersonal.findByAreaOperativa", query = "SELECT l FROM ListaPersonal l WHERE l.areaOperativa = :areaOperativa")
    , @NamedQuery(name = "ListaPersonal.findByAreaOperativaNombre", query = "SELECT l FROM ListaPersonal l WHERE l.areaOperativaNombre = :areaOperativaNombre")
    , @NamedQuery(name = "ListaPersonal.findByCategoriaOperativa", query = "SELECT l FROM ListaPersonal l WHERE l.categoriaOperativa = :categoriaOperativa")
    , @NamedQuery(name = "ListaPersonal.findByCategoriaOperativaNombre", query = "SELECT l FROM ListaPersonal l WHERE l.categoriaOperativaNombre = :categoriaOperativaNombre")
    , @NamedQuery(name = "ListaPersonal.findByAreaOficial", query = "SELECT l FROM ListaPersonal l WHERE l.areaOficial = :areaOficial")
    , @NamedQuery(name = "ListaPersonal.findByAreaOficialNombre", query = "SELECT l FROM ListaPersonal l WHERE l.areaOficialNombre = :areaOficialNombre")
    , @NamedQuery(name = "ListaPersonal.findByCategoriaOficial", query = "SELECT l FROM ListaPersonal l WHERE l.categoriaOficial = :categoriaOficial")
    , @NamedQuery(name = "ListaPersonal.findByCategoriaOficialNombre", query = "SELECT l FROM ListaPersonal l WHERE l.categoriaOficialNombre = :categoriaOficialNombre")
    , @NamedQuery(name = "ListaPersonal.findByActividad", query = "SELECT l FROM ListaPersonal l WHERE l.actividad = :actividad")
    , @NamedQuery(name = "ListaPersonal.findByActividadNombre", query = "SELECT l FROM ListaPersonal l WHERE l.actividadNombre = :actividadNombre")
    , @NamedQuery(name = "ListaPersonal.findByGrado", query = "SELECT l FROM ListaPersonal l WHERE l.grado = :grado")
    , @NamedQuery(name = "ListaPersonal.findByGradoNombre", query = "SELECT l FROM ListaPersonal l WHERE l.gradoNombre = :gradoNombre")
    , @NamedQuery(name = "ListaPersonal.findByPerfilProfesional", query = "SELECT l FROM ListaPersonal l WHERE l.perfilProfesional = :perfilProfesional")
    , @NamedQuery(name = "ListaPersonal.findByExperienciaDocente", query = "SELECT l FROM ListaPersonal l WHERE l.experienciaDocente = :experienciaDocente")
    , @NamedQuery(name = "ListaPersonal.findByExperienciaLaboral", query = "SELECT l FROM ListaPersonal l WHERE l.experienciaLaboral = :experienciaLaboral")
    , @NamedQuery(name = "ListaPersonal.findByFechaNacimiento", query = "SELECT l FROM ListaPersonal l WHERE l.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "ListaPersonal.findByEstado", query = "SELECT l FROM ListaPersonal l WHERE l.estado = :estado")
    , @NamedQuery(name = "ListaPersonal.findByMunicipio", query = "SELECT l FROM ListaPersonal l WHERE l.municipio = :municipio")
    , @NamedQuery(name = "ListaPersonal.findByLocalidad", query = "SELECT l FROM ListaPersonal l WHERE l.localidad = :localidad")
    , @NamedQuery(name = "ListaPersonal.findByPais", query = "SELECT l FROM ListaPersonal l WHERE l.pais = :pais")
    , @NamedQuery(name = "ListaPersonal.findBySni", query = "SELECT l FROM ListaPersonal l WHERE l.sni = :sni")
    , @NamedQuery(name = "ListaPersonal.findByPerfilProdep", query = "SELECT l FROM ListaPersonal l WHERE l.perfilProdep = :perfilProdep")
    , @NamedQuery(name = "ListaPersonal.findByCorreoElectronico", query = "SELECT l FROM ListaPersonal l WHERE l.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "ListaPersonal.findByCorreoElectronico2", query = "SELECT l FROM ListaPersonal l WHERE l.correoElectronico2 = :correoElectronico2")})
public class ListaPersonal implements Serializable {
   
    @Id
    @NonNull
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private Integer clave;

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre", nullable = false, length = 250)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status", nullable = false)
    private Character status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "genero", nullable = false)
    private short genero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "genero_abreviacion", nullable = false)
    private Character generoAbreviacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "genero_nombre", nullable = false, length = 50)
    private String generoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_operativa", nullable = false)
    private int areaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "area_operativa_nombre", nullable = false, length = 150)
    private String areaOperativaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria_operativa", nullable = false)
    private short categoriaOperativa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "categoria_operativa_nombre", nullable = false, length = 100)
    private String categoriaOperativaNombre;    
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_superior")
    private int areaSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "area_superior_nombre")
    private String areaSuperiorNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_oficial", nullable = false)
    private int areaOficial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "area_oficial_nombre", nullable = false, length = 150)
    private String areaOficialNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria_oficial", nullable = false)
    private short categoriaOficial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "categoria_oficial_nombre", nullable = false, length = 100)
    private String categoriaOficialNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actividad", nullable = false)
    private short actividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "actividad_nombre", nullable = false, length = 50)
    private String actividadNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado", nullable = false)
    private short grado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "grado_nombre", nullable = false, length = 100)
    private String gradoNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "perfil_profesional", nullable = false, length = 150)
    private String perfilProfesional;
    @Basic(optional = false)
    @NotNull
    @Column(name = "experiencia_docente", nullable = false)
    private short experienciaDocente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "experiencia_laboral", nullable = false)
    private short experienciaLaboral;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "estado", nullable = false, length = 50)
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "municipio", nullable = false, length = 100)
    private String municipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "localidad", nullable = false, length = 150)
    private String localidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "pais", nullable = false, length = 50)
    private String pais;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sni", nullable = false)
    private boolean sni;
    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil_prodep", nullable = false)
    private boolean perfilProdep;
    @Size(max = 200)
    @Column(name = "correo_electronico", length = 200)
    private String correoElectronico;
    @Size(max = 200)
    @Column(name = "correo_electronico2", length = 200)
    private String correoElectronico2;


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

    public short getGenero() {
        return genero;
    }

    public void setGenero(short genero) {
        this.genero = genero;
    }

    public Character getGeneroAbreviacion() {
        return generoAbreviacion;
    }

    public void setGeneroAbreviacion(Character generoAbreviacion) {
        this.generoAbreviacion = generoAbreviacion;
    }

    public String getGeneroNombre() {
        return generoNombre;
    }

    public void setGeneroNombre(String generoNombre) {
        this.generoNombre = generoNombre;
    }

    public int getAreaOperativa() {
        return areaOperativa;
    }

    public void setAreaOperativa(int areaOperativa) {
        this.areaOperativa = areaOperativa;
    }

    public String getAreaOperativaNombre() {
        return areaOperativaNombre;
    }

    public void setAreaOperativaNombre(String areaOperativaNombre) {
        this.areaOperativaNombre = areaOperativaNombre;
    }

    public short getCategoriaOperativa() {
        return categoriaOperativa;
    }

    public void setCategoriaOperativa(short categoriaOperativa) {
        this.categoriaOperativa = categoriaOperativa;
    }

    public String getCategoriaOperativaNombre() {
        return categoriaOperativaNombre;
    }

    public void setCategoriaOperativaNombre(String categoriaOperativaNombre) {
        this.categoriaOperativaNombre = categoriaOperativaNombre;
    }

    public int getAreaOficial() {
        return areaOficial;
    }

    public void setAreaOficial(int areaOficial) {
        this.areaOficial = areaOficial;
    }

    public String getAreaOficialNombre() {
        return areaOficialNombre;
    }

    public void setAreaOficialNombre(String areaOficialNombre) {
        this.areaOficialNombre = areaOficialNombre;
    }

    public short getCategoriaOficial() {
        return categoriaOficial;
    }

    public void setCategoriaOficial(short categoriaOficial) {
        this.categoriaOficial = categoriaOficial;
    }

    public String getCategoriaOficialNombre() {
        return categoriaOficialNombre;
    }

    public void setCategoriaOficialNombre(String categoriaOficialNombre) {
        this.categoriaOficialNombre = categoriaOficialNombre;
    }

    public short getActividad() {
        return actividad;
    }

    public void setActividad(short actividad) {
        this.actividad = actividad;
    }

    public String getActividadNombre() {
        return actividadNombre;
    }

    public void setActividadNombre(String actividadNombre) {
        this.actividadNombre = actividadNombre;
    }

    public short getGrado() {
        return grado;
    }

    public void setGrado(short grado) {
        this.grado = grado;
    }

    public String getGradoNombre() {
        return gradoNombre;
    }

    public void setGradoNombre(String gradoNombre) {
        this.gradoNombre = gradoNombre;
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

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public int getAreaSuperior() {
        return areaSuperior;
    }

    public void setAreaSuperior(int areaSuperior) {
        this.areaSuperior = areaSuperior;
    }

    public String getAreaSuperiorNombre() {
        return areaSuperiorNombre;
    }

    public void setAreaSuperiorNombre(String areaSuperiorNombre) {
        this.areaSuperiorNombre = areaSuperiorNombre;
    }
    
}
