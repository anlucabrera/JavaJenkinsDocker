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
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class EncuestaSatisfaccionEgresadosPecePK implements Serializable {

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
    @Column(name = "pregunta")
    private int pregunta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;

    public EncuestaSatisfaccionEgresadosPecePK() {
    }

    public EncuestaSatisfaccionEgresadosPecePK(int cveEncuesta, int ciclo, int pregunta, String siglas) {
        this.cveEncuesta = cveEncuesta;
        this.ciclo = ciclo;
        this.pregunta = pregunta;
        this.siglas = siglas;
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

    public int getPregunta() {
        return pregunta;
    }

    public void setPregunta(int pregunta) {
        this.pregunta = pregunta;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cveEncuesta;
        hash += (int) ciclo;
        hash += (int) pregunta;
        hash += (siglas != null ? siglas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSatisfaccionEgresadosPecePK)) {
            return false;
        }
        EncuestaSatisfaccionEgresadosPecePK other = (EncuestaSatisfaccionEgresadosPecePK) object;
        if (this.cveEncuesta != other.cveEncuesta) {
            return false;
        }
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.pregunta != other.pregunta) {
            return false;
        }
        if ((this.siglas == null && other.siglas != null) || (this.siglas != null && !this.siglas.equals(other.siglas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EncuestaSatisfaccionEgresadosPecePK[ cveEncuesta=" + cveEncuesta + ", ciclo=" + ciclo + ", pregunta=" + pregunta + ", siglas=" + siglas + " ]";
    }
    
}
