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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jonny
 */
@Entity
@Table(name = "planeaciones_cuatrimestrales", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaneacionesCuatrimestrales.findAll", query = "SELECT p FROM PlaneacionesCuatrimestrales p")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByPlaneacion", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.planeacion = :planeacion")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByPeriodo", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByHorasClaseTsu", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.horasClaseTsu = :horasClaseTsu")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByHorasClaseIng", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.horasClaseIng = :horasClaseIng")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByEstadias", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.estadias = :estadias")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByEstudiantesEstadia", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.estudiantesEstadia = :estudiantesEstadia")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByProyectosEstadia", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.proyectosEstadia = :proyectosEstadia")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByProyectoInvestigacion", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.proyectoInvestigacion = :proyectoInvestigacion")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByAsesoriaClase", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.asesoriaClase = :asesoriaClase")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByTutoriaIndividual", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.tutoriaIndividual = :tutoriaIndividual")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByTutoriaGrupal", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.tutoriaGrupal = :tutoriaGrupal")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByReunionAcademia", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.reunionAcademia = :reunionAcademia")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByActividadesVarias", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.actividadesVarias = :actividadesVarias")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByValidacionDirector", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.validacionDirector = :validacionDirector")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByFechaValidacionDirector", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.fechaValidacionDirector = :fechaValidacionDirector")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByValidacionSecretarioAdemico", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.validacionSecretarioAdemico = :validacionSecretarioAdemico")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByFechaValidacionSecretarioAdemico", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.fechaValidacionSecretarioAdemico = :fechaValidacionSecretarioAdemico")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByValidacionJefePersonal", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.validacionJefePersonal = :validacionJefePersonal")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByFechaValidacionJefePersonal", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.fechaValidacionJefePersonal = :fechaValidacionJefePersonal")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByComentariosDirector", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.comentariosDirector = :comentariosDirector")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByComentariosSecretarioAcademico", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.comentariosSecretarioAcademico = :comentariosSecretarioAcademico")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByComentariosJefePersonal", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.comentariosJefePersonal = :comentariosJefePersonal")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByComentariosSistema", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.comentariosSistema = :comentariosSistema")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByValidacionSistema", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.validacionSistema = :validacionSistema")
    , @NamedQuery(name = "PlaneacionesCuatrimestrales.findByTotal", query = "SELECT p FROM PlaneacionesCuatrimestrales p WHERE p.total = :total")})
public class PlaneacionesCuatrimestrales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "planeacion")
    private Integer planeacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horas_clase_tsu")
    private short horasClaseTsu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horas_clase_ing")
    private short horasClaseIng;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estadias")
    private short estadias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estudiantesEstadia")
    private short estudiantesEstadia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "proyectosEstadia")
    private short proyectosEstadia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "proyecto_investigacion")
    private short proyectoInvestigacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asesoria_clase")
    private short asesoriaClase;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tutoria_individual")
    private short tutoriaIndividual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tutoria_grupal")
    private short tutoriaGrupal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reunion_academia")
    private short reunionAcademia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actividades_varias")
    private short actividadesVarias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_director")
    private boolean validacionDirector;
    @Column(name = "fecha_validacion_director")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacionDirector;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_secretario_ademico")
    private boolean validacionSecretarioAdemico;
    @Column(name = "fecha_validacion_secretario_ademico")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacionSecretarioAdemico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_jefe_personal")
    private boolean validacionJefePersonal;
    @Column(name = "fecha_validacion_jefe_personal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacionJefePersonal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "comentarios_director")
    private String comentariosDirector;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "comentarios_secretario_academico")
    private String comentariosSecretarioAcademico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "comentarios_jefe_personal")
    private String comentariosJefePersonal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "comentarios_sistema")
    private String comentariosSistema;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_sistema")
    private boolean validacionSistema;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private short total;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planeacionesCuatrimestrales")
    private List<Atividadesvariasplaneacionescuatrimestrales> atividadesvariasplaneacionescuatrimestralesList;
    @JoinColumn(name = "director", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal director;
    @JoinColumn(name = "docente", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal docente;

    public PlaneacionesCuatrimestrales() {
    }

    public PlaneacionesCuatrimestrales(Integer planeacion) {
        this.planeacion = planeacion;
    }

    public PlaneacionesCuatrimestrales(Integer planeacion, int periodo, short horasClaseTsu, short horasClaseIng, short estadias, short estudiantesEstadia, short proyectosEstadia, short proyectoInvestigacion, short asesoriaClase, short tutoriaIndividual, short tutoriaGrupal, short reunionAcademia, short actividadesVarias, boolean validacionDirector, boolean validacionSecretarioAdemico, boolean validacionJefePersonal, String comentariosDirector, String comentariosSecretarioAcademico, String comentariosJefePersonal, String comentariosSistema, boolean validacionSistema, short total) {
        this.planeacion = planeacion;
        this.periodo = periodo;
        this.horasClaseTsu = horasClaseTsu;
        this.horasClaseIng = horasClaseIng;
        this.estadias = estadias;
        this.estudiantesEstadia = estudiantesEstadia;
        this.proyectosEstadia = proyectosEstadia;
        this.proyectoInvestigacion = proyectoInvestigacion;
        this.asesoriaClase = asesoriaClase;
        this.tutoriaIndividual = tutoriaIndividual;
        this.tutoriaGrupal = tutoriaGrupal;
        this.reunionAcademia = reunionAcademia;
        this.actividadesVarias = actividadesVarias;
        this.validacionDirector = validacionDirector;
        this.validacionSecretarioAdemico = validacionSecretarioAdemico;
        this.validacionJefePersonal = validacionJefePersonal;
        this.comentariosDirector = comentariosDirector;
        this.comentariosSecretarioAcademico = comentariosSecretarioAcademico;
        this.comentariosJefePersonal = comentariosJefePersonal;
        this.comentariosSistema = comentariosSistema;
        this.validacionSistema = validacionSistema;
        this.total = total;
    }

    public Integer getPlaneacion() {
        return planeacion;
    }

    public void setPlaneacion(Integer planeacion) {
        this.planeacion = planeacion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public short getHorasClaseTsu() {
        return horasClaseTsu;
    }

    public void setHorasClaseTsu(short horasClaseTsu) {
        this.horasClaseTsu = horasClaseTsu;
    }

    public short getHorasClaseIng() {
        return horasClaseIng;
    }

    public void setHorasClaseIng(short horasClaseIng) {
        this.horasClaseIng = horasClaseIng;
    }

    public short getEstadias() {
        return estadias;
    }

    public void setEstadias(short estadias) {
        this.estadias = estadias;
    }

    public short getEstudiantesEstadia() {
        return estudiantesEstadia;
    }

    public void setEstudiantesEstadia(short estudiantesEstadia) {
        this.estudiantesEstadia = estudiantesEstadia;
    }

    public short getProyectosEstadia() {
        return proyectosEstadia;
    }

    public void setProyectosEstadia(short proyectosEstadia) {
        this.proyectosEstadia = proyectosEstadia;
    }

    public short getProyectoInvestigacion() {
        return proyectoInvestigacion;
    }

    public void setProyectoInvestigacion(short proyectoInvestigacion) {
        this.proyectoInvestigacion = proyectoInvestigacion;
    }

    public short getAsesoriaClase() {
        return asesoriaClase;
    }

    public void setAsesoriaClase(short asesoriaClase) {
        this.asesoriaClase = asesoriaClase;
    }

    public short getTutoriaIndividual() {
        return tutoriaIndividual;
    }

    public void setTutoriaIndividual(short tutoriaIndividual) {
        this.tutoriaIndividual = tutoriaIndividual;
    }

    public short getTutoriaGrupal() {
        return tutoriaGrupal;
    }

    public void setTutoriaGrupal(short tutoriaGrupal) {
        this.tutoriaGrupal = tutoriaGrupal;
    }

    public short getReunionAcademia() {
        return reunionAcademia;
    }

    public void setReunionAcademia(short reunionAcademia) {
        this.reunionAcademia = reunionAcademia;
    }

    public short getActividadesVarias() {
        return actividadesVarias;
    }

    public void setActividadesVarias(short actividadesVarias) {
        this.actividadesVarias = actividadesVarias;
    }

    public boolean getValidacionDirector() {
        return validacionDirector;
    }

    public void setValidacionDirector(boolean validacionDirector) {
        this.validacionDirector = validacionDirector;
    }

    public Date getFechaValidacionDirector() {
        return fechaValidacionDirector;
    }

    public void setFechaValidacionDirector(Date fechaValidacionDirector) {
        this.fechaValidacionDirector = fechaValidacionDirector;
    }

    public boolean getValidacionSecretarioAdemico() {
        return validacionSecretarioAdemico;
    }

    public void setValidacionSecretarioAdemico(boolean validacionSecretarioAdemico) {
        this.validacionSecretarioAdemico = validacionSecretarioAdemico;
    }

    public Date getFechaValidacionSecretarioAdemico() {
        return fechaValidacionSecretarioAdemico;
    }

    public void setFechaValidacionSecretarioAdemico(Date fechaValidacionSecretarioAdemico) {
        this.fechaValidacionSecretarioAdemico = fechaValidacionSecretarioAdemico;
    }

    public boolean getValidacionJefePersonal() {
        return validacionJefePersonal;
    }

    public void setValidacionJefePersonal(boolean validacionJefePersonal) {
        this.validacionJefePersonal = validacionJefePersonal;
    }

    public Date getFechaValidacionJefePersonal() {
        return fechaValidacionJefePersonal;
    }

    public void setFechaValidacionJefePersonal(Date fechaValidacionJefePersonal) {
        this.fechaValidacionJefePersonal = fechaValidacionJefePersonal;
    }

    public String getComentariosDirector() {
        return comentariosDirector;
    }

    public void setComentariosDirector(String comentariosDirector) {
        this.comentariosDirector = comentariosDirector;
    }

    public String getComentariosSecretarioAcademico() {
        return comentariosSecretarioAcademico;
    }

    public void setComentariosSecretarioAcademico(String comentariosSecretarioAcademico) {
        this.comentariosSecretarioAcademico = comentariosSecretarioAcademico;
    }

    public String getComentariosJefePersonal() {
        return comentariosJefePersonal;
    }

    public void setComentariosJefePersonal(String comentariosJefePersonal) {
        this.comentariosJefePersonal = comentariosJefePersonal;
    }

    public String getComentariosSistema() {
        return comentariosSistema;
    }

    public void setComentariosSistema(String comentariosSistema) {
        this.comentariosSistema = comentariosSistema;
    }

    public boolean getValidacionSistema() {
        return validacionSistema;
    }

    public void setValidacionSistema(boolean validacionSistema) {
        this.validacionSistema = validacionSistema;
    }

    public short getTotal() {
        return total;
    }

    public void setTotal(short total) {
        this.total = total;
    }

    @XmlTransient
    public List<Atividadesvariasplaneacionescuatrimestrales> getAtividadesvariasplaneacionescuatrimestralesList() {
        return atividadesvariasplaneacionescuatrimestralesList;
    }

    public void setAtividadesvariasplaneacionescuatrimestralesList(List<Atividadesvariasplaneacionescuatrimestrales> atividadesvariasplaneacionescuatrimestralesList) {
        this.atividadesvariasplaneacionescuatrimestralesList = atividadesvariasplaneacionescuatrimestralesList;
    }

    public Personal getDirector() {
        return director;
    }

    public void setDirector(Personal director) {
        this.director = director;
    }

    public Personal getDocente() {
        return docente;
    }

    public void setDocente(Personal docente) {
        this.docente = docente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planeacion != null ? planeacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaneacionesCuatrimestrales)) {
            return false;
        }
        PlaneacionesCuatrimestrales other = (PlaneacionesCuatrimestrales) object;
        if ((this.planeacion == null && other.planeacion != null) || (this.planeacion != null && !this.planeacion.equals(other.planeacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales[ planeacion=" + planeacion + " ]";
    }
    
}
