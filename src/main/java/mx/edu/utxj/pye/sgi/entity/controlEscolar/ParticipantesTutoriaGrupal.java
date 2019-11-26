/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "participantes_tutoria_grupal", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipantesTutoriaGrupal.findAll", query = "SELECT p FROM ParticipantesTutoriaGrupal p")
    , @NamedQuery(name = "ParticipantesTutoriaGrupal.findByTutoriaGrupal", query = "SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.participantesTutoriaGrupalPK.tutoriaGrupal = :tutoriaGrupal")
    , @NamedQuery(name = "ParticipantesTutoriaGrupal.findByEstudiante", query = "SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.participantesTutoriaGrupalPK.estudiante = :estudiante")
    , @NamedQuery(name = "ParticipantesTutoriaGrupal.findByAsistencia", query = "SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.asistencia = :asistencia")
    , @NamedQuery(name = "ParticipantesTutoriaGrupal.findByAceptacionAcuerdos", query = "SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.aceptacionAcuerdos = :aceptacionAcuerdos")
    , @NamedQuery(name = "ParticipantesTutoriaGrupal.findByComentarios", query = "SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.comentarios = :comentarios")})
public class ParticipantesTutoriaGrupal implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ParticipantesTutoriaGrupalPK participantesTutoriaGrupalPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asistencia")
    private boolean asistencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 31)
    @Column(name = "aceptacion_acuerdos")
    private String aceptacionAcuerdos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "comentarios")
    private String comentarios;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante1;
    @JoinColumn(name = "tutoria_grupal", referencedColumnName = "tutoria_grupal", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TutoriasGrupales tutoriasGrupales;

    public ParticipantesTutoriaGrupal() {
    }

    public ParticipantesTutoriaGrupal(ParticipantesTutoriaGrupalPK participantesTutoriaGrupalPK) {
        this.participantesTutoriaGrupalPK = participantesTutoriaGrupalPK;
    }

    public ParticipantesTutoriaGrupal(ParticipantesTutoriaGrupalPK participantesTutoriaGrupalPK, boolean asistencia, String aceptacionAcuerdos, String comentarios) {
        this.participantesTutoriaGrupalPK = participantesTutoriaGrupalPK;
        this.asistencia = asistencia;
        this.aceptacionAcuerdos = aceptacionAcuerdos;
        this.comentarios = comentarios;
    }

    public ParticipantesTutoriaGrupal(int tutoriaGrupal, int estudiante) {
        this.participantesTutoriaGrupalPK = new ParticipantesTutoriaGrupalPK(tutoriaGrupal, estudiante);
    }

    public ParticipantesTutoriaGrupalPK getParticipantesTutoriaGrupalPK() {
        return participantesTutoriaGrupalPK;
    }

    public void setParticipantesTutoriaGrupalPK(ParticipantesTutoriaGrupalPK participantesTutoriaGrupalPK) {
        this.participantesTutoriaGrupalPK = participantesTutoriaGrupalPK;
    }

    public boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public String getAceptacionAcuerdos() {
        return aceptacionAcuerdos;
    }

    public void setAceptacionAcuerdos(String aceptacionAcuerdos) {
        this.aceptacionAcuerdos = aceptacionAcuerdos;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Estudiante getEstudiante1() {
        return estudiante1;
    }

    public void setEstudiante1(Estudiante estudiante1) {
        this.estudiante1 = estudiante1;
    }

    public TutoriasGrupales getTutoriasGrupales() {
        return tutoriasGrupales;
    }

    public void setTutoriasGrupales(TutoriasGrupales tutoriasGrupales) {
        this.tutoriasGrupales = tutoriasGrupales;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (participantesTutoriaGrupalPK != null ? participantesTutoriaGrupalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParticipantesTutoriaGrupal)) {
            return false;
        }
        ParticipantesTutoriaGrupal other = (ParticipantesTutoriaGrupal) object;
        if ((this.participantesTutoriaGrupalPK == null && other.participantesTutoriaGrupalPK != null) || (this.participantesTutoriaGrupalPK != null && !this.participantesTutoriaGrupalPK.equals(other.participantesTutoriaGrupalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal[ participantesTutoriaGrupalPK=" + participantesTutoriaGrupalPK + " ]";
    }
    
}
