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
 * @author Finanzas1
 */
@Entity
@Table(name = "formacion_academica", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormacionAcademica.findAll", query = "SELECT f FROM FormacionAcademica f")
    , @NamedQuery(name = "FormacionAcademica.findByFormacion", query = "SELECT f FROM FormacionAcademica f WHERE f.formacion = :formacion")
    , @NamedQuery(name = "FormacionAcademica.findByEvidenciaTitulo", query = "SELECT f FROM FormacionAcademica f WHERE f.evidenciaTitulo = :evidenciaTitulo")
    , @NamedQuery(name = "FormacionAcademica.findByFechaObtencion", query = "SELECT f FROM FormacionAcademica f WHERE f.fechaObtencion = :fechaObtencion")
    , @NamedQuery(name = "FormacionAcademica.findByModoTitulacion", query = "SELECT f FROM FormacionAcademica f WHERE f.modoTitulacion = :modoTitulacion")
    , @NamedQuery(name = "FormacionAcademica.findByCedulaProfesional", query = "SELECT f FROM FormacionAcademica f WHERE f.cedulaProfesional = :cedulaProfesional")
    , @NamedQuery(name = "FormacionAcademica.findByEvidenciaCedula", query = "SELECT f FROM FormacionAcademica f WHERE f.evidenciaCedula = :evidenciaCedula")
    , @NamedQuery(name = "FormacionAcademica.findByInstitucion", query = "SELECT f FROM FormacionAcademica f WHERE f.institucion = :institucion")
    , @NamedQuery(name = "FormacionAcademica.findByEstatus", query = "SELECT f FROM FormacionAcademica f WHERE f.estatus = :estatus")
    , @NamedQuery(name = "FormacionAcademica.findByNombreCarrera", query = "SELECT f FROM FormacionAcademica f WHERE f.nombreCarrera = :nombreCarrera")})
public class FormacionAcademica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "formacion")
    private Integer formacion;
    @Size(max = 500)
    @Column(name = "evidencia_titulo")
    private String evidenciaTitulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_obtencion")
    @Temporal(TemporalType.DATE)
    private Date fechaObtencion;
    @Size(max = 38)
    @Column(name = "modo_titulacion")
    private String modoTitulacion;
    @Size(max = 15)
    @Column(name = "cedula_profesional")
    private String cedulaProfesional;
    @Size(max = 500)
    @Column(name = "evidencia_cedula")
    private String evidenciaCedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "institucion")
    private String institucion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 500)
    @Column(name = "nombre_carrera")
    private String nombreCarrera;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;
    @JoinColumn(name = "nivel_escolaridad", referencedColumnName = "grado")
    @ManyToOne(optional = false)
    private Grados nivelEscolaridad;

    public FormacionAcademica() {
    }

    public FormacionAcademica(Integer formacion) {
        this.formacion = formacion;
    }

    public FormacionAcademica(Integer formacion, Date fechaObtencion, String institucion, String estatus) {
        this.formacion = formacion;
        this.fechaObtencion = fechaObtencion;
        this.institucion = institucion;
        this.estatus = estatus;
    }

    public Integer getFormacion() {
        return formacion;
    }

    public void setFormacion(Integer formacion) {
        this.formacion = formacion;
    }

    public String getEvidenciaTitulo() {
        return evidenciaTitulo;
    }

    public void setEvidenciaTitulo(String evidenciaTitulo) {
        this.evidenciaTitulo = evidenciaTitulo;
    }

    public Date getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(Date fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    public String getModoTitulacion() {
        return modoTitulacion;
    }

    public void setModoTitulacion(String modoTitulacion) {
        this.modoTitulacion = modoTitulacion;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getEvidenciaCedula() {
        return evidenciaCedula;
    }

    public void setEvidenciaCedula(String evidenciaCedula) {
        this.evidenciaCedula = evidenciaCedula;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    public Grados getNivelEscolaridad() {
        return nivelEscolaridad;
    }

    public void setNivelEscolaridad(Grados nivelEscolaridad) {
        this.nivelEscolaridad = nivelEscolaridad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formacion != null ? formacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormacionAcademica)) {
            return false;
        }
        FormacionAcademica other = (FormacionAcademica) object;
        if ((this.formacion == null && other.formacion != null) || (this.formacion != null && !this.formacion.equals(other.formacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica[ formacion=" + formacion + " ]";
    }
    
}
