/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
public class EncuestaCapacitacionClimaLaboralPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_encuesta")
    private int cveEncuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private int anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pregunta")
    private int pregunta;

    public EncuestaCapacitacionClimaLaboralPK() {
    }

    public EncuestaCapacitacionClimaLaboralPK(int cveEncuesta, int anio, int pregunta) {
        this.cveEncuesta = cveEncuesta;
        this.anio = anio;
        this.pregunta = pregunta;
    }

    public int getCveEncuesta() {
        return cveEncuesta;
    }

    public void setCveEncuesta(int cveEncuesta) {
        this.cveEncuesta = cveEncuesta;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getPregunta() {
        return pregunta;
    }

    public void setPregunta(int pregunta) {
        this.pregunta = pregunta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cveEncuesta;
        hash += (int) anio;
        hash += (int) pregunta;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaCapacitacionClimaLaboralPK)) {
            return false;
        }
        EncuestaCapacitacionClimaLaboralPK other = (EncuestaCapacitacionClimaLaboralPK) object;
        if (this.cveEncuesta != other.cveEncuesta) {
            return false;
        }
        if (this.anio != other.anio) {
            return false;
        }
        if (this.pregunta != other.pregunta) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaCapacitacionClimaLaboralPK[ cveEncuesta=" + cveEncuesta + ", anio=" + anio + ", pregunta=" + pregunta + " ]";
    }
    
}
