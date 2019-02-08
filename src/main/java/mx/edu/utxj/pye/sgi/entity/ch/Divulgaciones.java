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
import javax.persistence.Lob;
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
 * @author jonny
 */
@Entity
@Table(name = "divulgaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Divulgaciones.findAll", query = "SELECT d FROM Divulgaciones d")
    , @NamedQuery(name = "Divulgaciones.findByDivulgacion", query = "SELECT d FROM Divulgaciones d WHERE d.divulgacion = :divulgacion")
    , @NamedQuery(name = "Divulgaciones.findByNombreTrabajo", query = "SELECT d FROM Divulgaciones d WHERE d.nombreTrabajo = :nombreTrabajo")
    , @NamedQuery(name = "Divulgaciones.findByTipoParticipacion", query = "SELECT d FROM Divulgaciones d WHERE d.tipoParticipacion = :tipoParticipacion")
    , @NamedQuery(name = "Divulgaciones.findByTipoEventoDivulgacion", query = "SELECT d FROM Divulgaciones d WHERE d.tipoEventoDivulgacion = :tipoEventoDivulgacion")
    , @NamedQuery(name = "Divulgaciones.findByInstitucionOrganizadora", query = "SELECT d FROM Divulgaciones d WHERE d.institucionOrganizadora = :institucionOrganizadora")
    , @NamedQuery(name = "Divulgaciones.findByLugarInstitucion", query = "SELECT d FROM Divulgaciones d WHERE d.lugarInstitucion = :lugarInstitucion")
    , @NamedQuery(name = "Divulgaciones.findByTipoPublico", query = "SELECT d FROM Divulgaciones d WHERE d.tipoPublico = :tipoPublico")
    , @NamedQuery(name = "Divulgaciones.findByFechaRealizacion", query = "SELECT d FROM Divulgaciones d WHERE d.fechaRealizacion = :fechaRealizacion")
    , @NamedQuery(name = "Divulgaciones.findByTipoMedioDivulgacion", query = "SELECT d FROM Divulgaciones d WHERE d.tipoMedioDivulgacion = :tipoMedioDivulgacion")
    , @NamedQuery(name = "Divulgaciones.findByPalabraClave1", query = "SELECT d FROM Divulgaciones d WHERE d.palabraClave1 = :palabraClave1")
    , @NamedQuery(name = "Divulgaciones.findByPalabraClave2", query = "SELECT d FROM Divulgaciones d WHERE d.palabraClave2 = :palabraClave2")
    , @NamedQuery(name = "Divulgaciones.findByPalabraClave3", query = "SELECT d FROM Divulgaciones d WHERE d.palabraClave3 = :palabraClave3")})
public class Divulgaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "divulgacion")
    private Integer divulgacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_trabajo")
    private String nombreTrabajo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 26)
    @Column(name = "tipo_participacion")
    private String tipoParticipacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 41)
    @Column(name = "tipo_evento_divulgacion")
    private String tipoEventoDivulgacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "institucion_organizadora")
    private String institucionOrganizadora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "lugar_institucion")
    private String lugarInstitucion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "tipo_publico")
    private String tipoPublico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_realizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaRealizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 37)
    @Column(name = "tipo_medio_divulgacion")
    private String tipoMedioDivulgacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra_clave1")
    private String palabraClave1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra_clave2")
    private String palabraClave2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra_clave3")
    private String palabraClave3;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "notas")
    private String notas;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Divulgaciones() {
    }

    public Divulgaciones(Integer divulgacion) {
        this.divulgacion = divulgacion;
    }

    public Divulgaciones(Integer divulgacion, String nombreTrabajo, String tipoParticipacion, String tipoEventoDivulgacion, String institucionOrganizadora, String lugarInstitucion, String tipoPublico, Date fechaRealizacion, String tipoMedioDivulgacion, String palabraClave1, String palabraClave2, String palabraClave3, String notas) {
        this.divulgacion = divulgacion;
        this.nombreTrabajo = nombreTrabajo;
        this.tipoParticipacion = tipoParticipacion;
        this.tipoEventoDivulgacion = tipoEventoDivulgacion;
        this.institucionOrganizadora = institucionOrganizadora;
        this.lugarInstitucion = lugarInstitucion;
        this.tipoPublico = tipoPublico;
        this.fechaRealizacion = fechaRealizacion;
        this.tipoMedioDivulgacion = tipoMedioDivulgacion;
        this.palabraClave1 = palabraClave1;
        this.palabraClave2 = palabraClave2;
        this.palabraClave3 = palabraClave3;
        this.notas = notas;
    }

    public Integer getDivulgacion() {
        return divulgacion;
    }

    public void setDivulgacion(Integer divulgacion) {
        this.divulgacion = divulgacion;
    }

    public String getNombreTrabajo() {
        return nombreTrabajo;
    }

    public void setNombreTrabajo(String nombreTrabajo) {
        this.nombreTrabajo = nombreTrabajo;
    }

    public String getTipoParticipacion() {
        return tipoParticipacion;
    }

    public void setTipoParticipacion(String tipoParticipacion) {
        this.tipoParticipacion = tipoParticipacion;
    }

    public String getTipoEventoDivulgacion() {
        return tipoEventoDivulgacion;
    }

    public void setTipoEventoDivulgacion(String tipoEventoDivulgacion) {
        this.tipoEventoDivulgacion = tipoEventoDivulgacion;
    }

    public String getInstitucionOrganizadora() {
        return institucionOrganizadora;
    }

    public void setInstitucionOrganizadora(String institucionOrganizadora) {
        this.institucionOrganizadora = institucionOrganizadora;
    }

    public String getLugarInstitucion() {
        return lugarInstitucion;
    }

    public void setLugarInstitucion(String lugarInstitucion) {
        this.lugarInstitucion = lugarInstitucion;
    }

    public String getTipoPublico() {
        return tipoPublico;
    }

    public void setTipoPublico(String tipoPublico) {
        this.tipoPublico = tipoPublico;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getTipoMedioDivulgacion() {
        return tipoMedioDivulgacion;
    }

    public void setTipoMedioDivulgacion(String tipoMedioDivulgacion) {
        this.tipoMedioDivulgacion = tipoMedioDivulgacion;
    }

    public String getPalabraClave1() {
        return palabraClave1;
    }

    public void setPalabraClave1(String palabraClave1) {
        this.palabraClave1 = palabraClave1;
    }

    public String getPalabraClave2() {
        return palabraClave2;
    }

    public void setPalabraClave2(String palabraClave2) {
        this.palabraClave2 = palabraClave2;
    }

    public String getPalabraClave3() {
        return palabraClave3;
    }

    public void setPalabraClave3(String palabraClave3) {
        this.palabraClave3 = palabraClave3;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (divulgacion != null ? divulgacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Divulgaciones)) {
            return false;
        }
        Divulgaciones other = (Divulgaciones) object;
        if ((this.divulgacion == null && other.divulgacion != null) || (this.divulgacion != null && !this.divulgacion.equals(other.divulgacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Divulgaciones[ divulgacion=" + divulgacion + " ]";
    }
    
}
