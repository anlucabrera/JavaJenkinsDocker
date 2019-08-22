/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "grupo", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g")
    , @NamedQuery(name = "Grupo.findByIdGrupo", query = "SELECT g FROM Grupo g WHERE g.idGrupo = :idGrupo")
    , @NamedQuery(name = "Grupo.findByLiteral", query = "SELECT g FROM Grupo g WHERE g.literal = :literal")
    , @NamedQuery(name = "Grupo.findByGrado", query = "SELECT g FROM Grupo g WHERE g.grado = :grado")
    , @NamedQuery(name = "Grupo.findByCapMaxima", query = "SELECT g FROM Grupo g WHERE g.capMaxima = :capMaxima")
    , @NamedQuery(name = "Grupo.findByIdPe", query = "SELECT g FROM Grupo g WHERE g.idPe = :idPe")
    , @NamedQuery(name = "Grupo.findByPeriodo", query = "SELECT g FROM Grupo g WHERE g.periodo = :periodo")
    , @NamedQuery(name = "Grupo.findByTutor", query = "SELECT g FROM Grupo g WHERE g.tutor = :tutor")
    , @NamedQuery(name = "Grupo.findByGeneracion", query = "SELECT g FROM Grupo g WHERE g.generacion = :generacion")})
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_grupo")
    private Integer idGrupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "literal")
    private Character literal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cap_maxima")
    private int capMaxima;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_pe")
    private short idPe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Column(name = "tutor")
    private Integer tutor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @JoinColumn(name = "plan", referencedColumnName = "id_plan_estudio")
    @ManyToOne(optional = false)
    private PlanEstudio plan;
    @JoinColumn(name = "id_sistema", referencedColumnName = "id_sistema")
    @ManyToOne(optional = false)
    private Sistema idSistema;
    @JoinColumn(name = "id_turno", referencedColumnName = "id_turno")
    @ManyToOne
    private Turno idTurno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cveGrupo")
    private List<CargaAcademica> cargaAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private List<Estudiante> estudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private List<Tutoria> tutoriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrupo")
    private List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList;

    public Grupo() {
    }

    public Grupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Grupo(Integer idGrupo, Character literal, int grado, int capMaxima, short idPe, int periodo, short generacion) {
        this.idGrupo = idGrupo;
        this.literal = literal;
        this.grado = grado;
        this.capMaxima = capMaxima;
        this.idPe = idPe;
        this.periodo = periodo;
        this.generacion = generacion;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Character getLiteral() {
        return literal;
    }

    public void setLiteral(Character literal) {
        this.literal = literal;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public int getCapMaxima() {
        return capMaxima;
    }

    public void setCapMaxima(int capMaxima) {
        this.capMaxima = capMaxima;
    }

    public short getIdPe() {
        return idPe;
    }

    public void setIdPe(short idPe) {
        this.idPe = idPe;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Integer getTutor() {
        return tutor;
    }

    public void setTutor(Integer tutor) {
        this.tutor = tutor;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public PlanEstudio getPlan() {
        return plan;
    }

    public void setPlan(PlanEstudio plan) {
        this.plan = plan;
    }

    public Sistema getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Sistema idSistema) {
        this.idSistema = idSistema;
    }

    public Turno getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Turno idTurno) {
        this.idTurno = idTurno;
    }

    @XmlTransient
    public List<CargaAcademica> getCargaAcademicaList() {
        return cargaAcademicaList;
    }

    public void setCargaAcademicaList(List<CargaAcademica> cargaAcademicaList) {
        this.cargaAcademicaList = cargaAcademicaList;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    @XmlTransient
    public List<Tutoria> getTutoriaList() {
        return tutoriaList;
    }

    public void setTutoriaList(List<Tutoria> tutoriaList) {
        this.tutoriaList = tutoriaList;
    }

    @XmlTransient
    public List<PermisosCapturaExtemporaneaGrupal> getPermisosCapturaExtemporaneaGrupalList() {
        return permisosCapturaExtemporaneaGrupalList;
    }

    public void setPermisosCapturaExtemporaneaGrupalList(List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList) {
        this.permisosCapturaExtemporaneaGrupalList = permisosCapturaExtemporaneaGrupalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupo != null ? idGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.idGrupo == null && other.idGrupo != null) || (this.idGrupo != null && !this.idGrupo.equals(other.idGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo[ idGrupo=" + idGrupo + " ]";
    }
    
}
