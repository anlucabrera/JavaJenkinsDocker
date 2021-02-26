/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "participantes_tutoria", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipantesTutoria.findAll", query = "SELECT p FROM ParticipantesTutoria p")
    , @NamedQuery(name = "ParticipantesTutoria.findByTutoria", query = "SELECT p FROM ParticipantesTutoria p WHERE p.tutoria = :tutoria")
    , @NamedQuery(name = "ParticipantesTutoria.findByComentarios", query = "SELECT p FROM ParticipantesTutoria p WHERE p.comentarios = :comentarios")})
public class ParticipantesTutoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tutoria")
    private Integer tutoria;
    @Size(max = 150)
    @Column(name = "comentarios")
    private String comentarios;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante estudiante;
    @JoinColumn(name = "tutoria", referencedColumnName = "id_tutoria", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Tutoria tutoria1;

    public ParticipantesTutoria() {
    }

    public ParticipantesTutoria(Integer tutoria) {
        this.tutoria = tutoria;
    }

    public Integer getTutoria() {
        return tutoria;
    }

    public void setTutoria(Integer tutoria) {
        this.tutoria = tutoria;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Tutoria getTutoria1() {
        return tutoria1;
    }

    public void setTutoria1(Tutoria tutoria1) {
        this.tutoria1 = tutoria1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tutoria != null ? tutoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParticipantesTutoria)) {
            return false;
        }
        ParticipantesTutoria other = (ParticipantesTutoria) object;
        if ((this.tutoria == null && other.tutoria != null) || (this.tutoria != null && !this.tutoria.equals(other.tutoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoria[ tutoria=" + tutoria + " ]";
    }
    
}
