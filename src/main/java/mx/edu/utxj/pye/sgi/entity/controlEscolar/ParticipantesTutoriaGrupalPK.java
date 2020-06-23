/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class ParticipantesTutoriaGrupalPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "tutoria_grupal")
    private int tutoriaGrupal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estudiante")
    private int estudiante;

    public ParticipantesTutoriaGrupalPK() {
    }

    public ParticipantesTutoriaGrupalPK(int tutoriaGrupal, int estudiante) {
        this.tutoriaGrupal = tutoriaGrupal;
        this.estudiante = estudiante;
    }

    public int getTutoriaGrupal() {
        return tutoriaGrupal;
    }

    public void setTutoriaGrupal(int tutoriaGrupal) {
        this.tutoriaGrupal = tutoriaGrupal;
    }

    public int getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(int estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) tutoriaGrupal;
        hash += (int) estudiante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParticipantesTutoriaGrupalPK)) {
            return false;
        }
        ParticipantesTutoriaGrupalPK other = (ParticipantesTutoriaGrupalPK) object;
        if (this.tutoriaGrupal != other.tutoriaGrupal) {
            return false;
        }
        if (this.estudiante != other.estudiante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupalPK[ tutoriaGrupal=" + tutoriaGrupal + ", estudiante=" + estudiante + " ]";
    }
    
}
