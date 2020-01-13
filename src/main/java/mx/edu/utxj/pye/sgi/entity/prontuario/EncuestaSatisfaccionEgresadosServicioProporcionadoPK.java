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
public class EncuestaSatisfaccionEgresadosServicioProporcionadoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_encuesta")
    private int cveEncuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pregunta")
    private int pregunta;

    public EncuestaSatisfaccionEgresadosServicioProporcionadoPK() {
    }

    public EncuestaSatisfaccionEgresadosServicioProporcionadoPK(int cveEncuesta, int ciclo, int periodo, int pregunta) {
        this.cveEncuesta = cveEncuesta;
        this.ciclo = ciclo;
        this.periodo = periodo;
        this.pregunta = pregunta;
    }

    public int getCveEncuesta() {
        return cveEncuesta;
    }

    public void setCveEncuesta(int cveEncuesta) {
        this.cveEncuesta = cveEncuesta;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
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
        hash += (int) ciclo;
        hash += (int) periodo;
        hash += (int) pregunta;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccionEgresadosServicioProporcionadoPK)) {
            return false;
        }
        EncuestaSatisfaccionEgresadosServicioProporcionadoPK other = (EncuestaSatisfaccionEgresadosServicioProporcionadoPK) object;
        if (this.cveEncuesta != other.cveEncuesta) {
            return false;
        }
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if (this.pregunta != other.pregunta) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaSatisfaccionEgresadosServicioProporcionadoPK[ cveEncuesta=" + cveEncuesta + ", ciclo=" + ciclo + ", periodo=" + periodo + ", pregunta=" + pregunta + " ]";
    }
    
}
