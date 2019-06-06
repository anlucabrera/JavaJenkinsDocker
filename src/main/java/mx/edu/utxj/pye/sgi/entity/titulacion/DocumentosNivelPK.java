/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

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
public class DocumentosNivelPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "documento")
    private int documento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivel")
    private int nivel;

    public DocumentosNivelPK() {
    }

    public DocumentosNivelPK(int documento, int nivel) {
        this.documento = documento;
        this.nivel = nivel;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) documento;
        hash += (int) nivel;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosNivelPK)) {
            return false;
        }
        DocumentosNivelPK other = (DocumentosNivelPK) object;
        if (this.documento != other.documento) {
            return false;
        }
        if (this.nivel != other.nivel) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosNivelPK[ documento=" + documento + ", nivel=" + nivel + " ]";
    }
    
}
