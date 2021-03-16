/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
 * @author UTXJ
 */
@Entity
@Table(name = "tutorias_grupales", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TutoriasGrupales.findAll", query = "SELECT t FROM TutoriasGrupales t")
    , @NamedQuery(name = "TutoriasGrupales.findByTutoriaGrupal", query = "SELECT t FROM TutoriasGrupales t WHERE t.tutoriaGrupal = :tutoriaGrupal")
    , @NamedQuery(name = "TutoriasGrupales.findByFecha", query = "SELECT t FROM TutoriasGrupales t WHERE t.fecha = :fecha")
    , @NamedQuery(name = "TutoriasGrupales.findByHoraInicio", query = "SELECT t FROM TutoriasGrupales t WHERE t.horaInicio = :horaInicio")
    , @NamedQuery(name = "TutoriasGrupales.findByHoraCierre", query = "SELECT t FROM TutoriasGrupales t WHERE t.horaCierre = :horaCierre")
    , @NamedQuery(name = "TutoriasGrupales.findByEventoRegistro", query = "SELECT t FROM TutoriasGrupales t WHERE t.eventoRegistro = :eventoRegistro")})
public class TutoriasGrupales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tutoria_grupal")
    private Integer tutoriaGrupal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_cierre")
    @Temporal(TemporalType.TIME)
    private Date horaCierre;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "orden_dia")
    private String ordenDia;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "acuerdos")
    private String acuerdos;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evento_registro")
    private int eventoRegistro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tutoriasGrupales", fetch = FetchType.LAZY)
    private List<ParticipantesTutoriaGrupal> participantesTutoriaGrupalList;
    @JoinColumn(name = "jefe_grupo", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante jefeGrupo;
    @JoinColumn(name = "sesion_grupal", referencedColumnName = "sesion_grupal")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SesionesGrupalesTutorias sesionGrupal;

    public TutoriasGrupales() {
    }

    public TutoriasGrupales(Integer tutoriaGrupal) {
        this.tutoriaGrupal = tutoriaGrupal;
    }

    public TutoriasGrupales(Integer tutoriaGrupal, Date fecha, Date horaInicio, Date horaCierre, String ordenDia, String acuerdos, String observaciones, int eventoRegistro) {
        this.tutoriaGrupal = tutoriaGrupal;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaCierre = horaCierre;
        this.ordenDia = ordenDia;
        this.acuerdos = acuerdos;
        this.observaciones = observaciones;
        this.eventoRegistro = eventoRegistro;
    }

    public Integer getTutoriaGrupal() {
        return tutoriaGrupal;
    }

    public void setTutoriaGrupal(Integer tutoriaGrupal) {
        this.tutoriaGrupal = tutoriaGrupal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(Date horaCierre) {
        this.horaCierre = horaCierre;
    }

    public String getOrdenDia() {
        return ordenDia;
    }

    public void setOrdenDia(String ordenDia) {
        this.ordenDia = ordenDia;
    }

    public String getAcuerdos() {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos) {
        this.acuerdos = acuerdos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(int eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    @XmlTransient
    public List<ParticipantesTutoriaGrupal> getParticipantesTutoriaGrupalList() {
        return participantesTutoriaGrupalList;
    }

    public void setParticipantesTutoriaGrupalList(List<ParticipantesTutoriaGrupal> participantesTutoriaGrupalList) {
        this.participantesTutoriaGrupalList = participantesTutoriaGrupalList;
    }

    public Estudiante getJefeGrupo() {
        return jefeGrupo;
    }

    public void setJefeGrupo(Estudiante jefeGrupo) {
        this.jefeGrupo = jefeGrupo;
    }

    public SesionesGrupalesTutorias getSesionGrupal() {
        return sesionGrupal;
    }

    public void setSesionGrupal(SesionesGrupalesTutorias sesionGrupal) {
        this.sesionGrupal = sesionGrupal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tutoriaGrupal != null ? tutoriaGrupal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TutoriasGrupales)) {
            return false;
        }
        TutoriasGrupales other = (TutoriasGrupales) object;
        if ((this.tutoriaGrupal == null && other.tutoriaGrupal != null) || (this.tutoriaGrupal != null && !this.tutoriaGrupal.equals(other.tutoriaGrupal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales[ tutoriaGrupal=" + tutoriaGrupal + " ]";
    }
    
}
