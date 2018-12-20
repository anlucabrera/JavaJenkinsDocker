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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "planeaciones_liberaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaneacionesLiberaciones.findAll", query = "SELECT p FROM PlaneacionesLiberaciones p")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByPeriodo", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.planeacionesLiberacionesPK.periodo = :periodo")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByDirector", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.planeacionesLiberacionesPK.director = :director")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByLiberacionDirector", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.liberacionDirector = :liberacionDirector")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByLiberacionDirectorFecha", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.liberacionDirectorFecha = :liberacionDirectorFecha")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByValidacionSecretarioAcademico", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.validacionSecretarioAcademico = :validacionSecretarioAcademico")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByValidacionSecretarioAcademicoFecha", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.validacionSecretarioAcademicoFecha = :validacionSecretarioAcademicoFecha")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByRechazoSecretarioAcademico", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.rechazoSecretarioAcademico = :rechazoSecretarioAcademico")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByRechazoSecretarioAcademicoFecha", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.rechazoSecretarioAcademicoFecha = :rechazoSecretarioAcademicoFecha")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByValidacionJefePersonal", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.validacionJefePersonal = :validacionJefePersonal")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByValidacionJefePersonalFecha", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.validacionJefePersonalFecha = :validacionJefePersonalFecha")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByRechazoJefePersonal", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.rechazoJefePersonal = :rechazoJefePersonal")
    , @NamedQuery(name = "PlaneacionesLiberaciones.findByRechazoJefePersonalFecha", query = "SELECT p FROM PlaneacionesLiberaciones p WHERE p.rechazoJefePersonalFecha = :rechazoJefePersonalFecha")})
public class PlaneacionesLiberaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlaneacionesLiberacionesPK planeacionesLiberacionesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "liberacion_director")
    private boolean liberacionDirector;
    @Column(name = "liberacion_director_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date liberacionDirectorFecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_secretario_academico")
    private boolean validacionSecretarioAcademico;
    @Column(name = "validacion_secretario_academico_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validacionSecretarioAcademicoFecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rechazo_secretario_academico")
    private boolean rechazoSecretarioAcademico;
    @Column(name = "rechazo_secretario_academico_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rechazoSecretarioAcademicoFecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_jefe_personal")
    private boolean validacionJefePersonal;
    @Column(name = "validacion_jefe_personal_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validacionJefePersonalFecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rechazo_jefe_personal")
    private boolean rechazoJefePersonal;
    @Column(name = "rechazo_jefe_personal_fecha")
    @Temporal(TemporalType.DATE)
    private Date rechazoJefePersonalFecha;
    @JoinColumn(name = "director", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal;

    public PlaneacionesLiberaciones() {
    }

    public PlaneacionesLiberaciones(PlaneacionesLiberacionesPK planeacionesLiberacionesPK) {
        this.planeacionesLiberacionesPK = planeacionesLiberacionesPK;
    }

    public PlaneacionesLiberaciones(PlaneacionesLiberacionesPK planeacionesLiberacionesPK, boolean liberacionDirector, boolean validacionSecretarioAcademico, boolean rechazoSecretarioAcademico, boolean validacionJefePersonal, boolean rechazoJefePersonal) {
        this.planeacionesLiberacionesPK = planeacionesLiberacionesPK;
        this.liberacionDirector = liberacionDirector;
        this.validacionSecretarioAcademico = validacionSecretarioAcademico;
        this.rechazoSecretarioAcademico = rechazoSecretarioAcademico;
        this.validacionJefePersonal = validacionJefePersonal;
        this.rechazoJefePersonal = rechazoJefePersonal;
    }

    public PlaneacionesLiberaciones(int periodo, int director) {
        this.planeacionesLiberacionesPK = new PlaneacionesLiberacionesPK(periodo, director);
    }

    public PlaneacionesLiberacionesPK getPlaneacionesLiberacionesPK() {
        return planeacionesLiberacionesPK;
    }

    public void setPlaneacionesLiberacionesPK(PlaneacionesLiberacionesPK planeacionesLiberacionesPK) {
        this.planeacionesLiberacionesPK = planeacionesLiberacionesPK;
    }

    public boolean getLiberacionDirector() {
        return liberacionDirector;
    }

    public void setLiberacionDirector(boolean liberacionDirector) {
        this.liberacionDirector = liberacionDirector;
    }

    public Date getLiberacionDirectorFecha() {
        return liberacionDirectorFecha;
    }

    public void setLiberacionDirectorFecha(Date liberacionDirectorFecha) {
        this.liberacionDirectorFecha = liberacionDirectorFecha;
    }

    public boolean getValidacionSecretarioAcademico() {
        return validacionSecretarioAcademico;
    }

    public void setValidacionSecretarioAcademico(boolean validacionSecretarioAcademico) {
        this.validacionSecretarioAcademico = validacionSecretarioAcademico;
    }

    public Date getValidacionSecretarioAcademicoFecha() {
        return validacionSecretarioAcademicoFecha;
    }

    public void setValidacionSecretarioAcademicoFecha(Date validacionSecretarioAcademicoFecha) {
        this.validacionSecretarioAcademicoFecha = validacionSecretarioAcademicoFecha;
    }

    public boolean getRechazoSecretarioAcademico() {
        return rechazoSecretarioAcademico;
    }

    public void setRechazoSecretarioAcademico(boolean rechazoSecretarioAcademico) {
        this.rechazoSecretarioAcademico = rechazoSecretarioAcademico;
    }

    public Date getRechazoSecretarioAcademicoFecha() {
        return rechazoSecretarioAcademicoFecha;
    }

    public void setRechazoSecretarioAcademicoFecha(Date rechazoSecretarioAcademicoFecha) {
        this.rechazoSecretarioAcademicoFecha = rechazoSecretarioAcademicoFecha;
    }

    public boolean getValidacionJefePersonal() {
        return validacionJefePersonal;
    }

    public void setValidacionJefePersonal(boolean validacionJefePersonal) {
        this.validacionJefePersonal = validacionJefePersonal;
    }

    public Date getValidacionJefePersonalFecha() {
        return validacionJefePersonalFecha;
    }

    public void setValidacionJefePersonalFecha(Date validacionJefePersonalFecha) {
        this.validacionJefePersonalFecha = validacionJefePersonalFecha;
    }

    public boolean getRechazoJefePersonal() {
        return rechazoJefePersonal;
    }

    public void setRechazoJefePersonal(boolean rechazoJefePersonal) {
        this.rechazoJefePersonal = rechazoJefePersonal;
    }

    public Date getRechazoJefePersonalFecha() {
        return rechazoJefePersonalFecha;
    }

    public void setRechazoJefePersonalFecha(Date rechazoJefePersonalFecha) {
        this.rechazoJefePersonalFecha = rechazoJefePersonalFecha;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planeacionesLiberacionesPK != null ? planeacionesLiberacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaneacionesLiberaciones)) {
            return false;
        }
        PlaneacionesLiberaciones other = (PlaneacionesLiberaciones) object;
        if ((this.planeacionesLiberacionesPK == null && other.planeacionesLiberacionesPK != null) || (this.planeacionesLiberacionesPK != null && !this.planeacionesLiberacionesPK.equals(other.planeacionesLiberacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesLiberaciones[ planeacionesLiberacionesPK=" + planeacionesLiberacionesPK + " ]";
    }
    
}
