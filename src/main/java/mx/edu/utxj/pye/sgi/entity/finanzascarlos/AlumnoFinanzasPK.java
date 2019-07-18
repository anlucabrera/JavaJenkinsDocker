/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Planeacion
 */
@Embeddable
public class AlumnoFinanzasPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "matricula")
    private int matricula;
    @Basic(optional = false)
    @Column(name = "periodo")
    private int periodo;

    public AlumnoFinanzasPK() {
    }

    public AlumnoFinanzasPK(int matricula, int periodo) {
        this.matricula = matricula;
        this.periodo = periodo;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) matricula;
        hash += (int) periodo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlumnoFinanzasPK)) {
            return false;
        }
        AlumnoFinanzasPK other = (AlumnoFinanzasPK) object;
        if (this.matricula != other.matricula) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.AlumnoFinanzasPK[ matricula=" + matricula + ", periodo=" + periodo + " ]";
    }
    
}
