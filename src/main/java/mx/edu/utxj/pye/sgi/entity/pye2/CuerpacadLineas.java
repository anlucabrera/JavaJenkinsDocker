/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpacad_lineas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadLineas.findAll", query = "SELECT c FROM CuerpacadLineas c")
    , @NamedQuery(name = "CuerpacadLineas.findByCuerpoAcademico", query = "SELECT c FROM CuerpacadLineas c WHERE c.cuerpacadLineasPK.cuerpoAcademico = :cuerpoAcademico")
    , @NamedQuery(name = "CuerpacadLineas.findByNombre", query = "SELECT c FROM CuerpacadLineas c WHERE c.cuerpacadLineasPK.nombre = :nombre")})
public class CuerpacadLineas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CuerpacadLineasPK cuerpacadLineasPK;
    @JoinColumn(name = "cuerpo_academico", referencedColumnName = "cuerpo_academico", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerposAcademicosRegistro;

    public CuerpacadLineas() {
    }

    public CuerpacadLineas(CuerpacadLineasPK cuerpacadLineasPK) {
        this.cuerpacadLineasPK = cuerpacadLineasPK;
    }

    public CuerpacadLineas(String cuerpoAcademico, String nombre) {
        this.cuerpacadLineasPK = new CuerpacadLineasPK(cuerpoAcademico, nombre);
    }

    public CuerpacadLineasPK getCuerpacadLineasPK() {
        return cuerpacadLineasPK;
    }

    public void setCuerpacadLineasPK(CuerpacadLineasPK cuerpacadLineasPK) {
        this.cuerpacadLineasPK = cuerpacadLineasPK;
    }

    public CuerposAcademicosRegistro getCuerposAcademicosRegistro() {
        return cuerposAcademicosRegistro;
    }

    public void setCuerposAcademicosRegistro(CuerposAcademicosRegistro cuerposAcademicosRegistro) {
        this.cuerposAcademicosRegistro = cuerposAcademicosRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuerpacadLineasPK != null ? cuerpacadLineasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpacadLineas)) {
            return false;
        }
        CuerpacadLineas other = (CuerpacadLineas) object;
        if ((this.cuerpacadLineasPK == null && other.cuerpacadLineasPK != null) || (this.cuerpacadLineasPK != null && !this.cuerpacadLineasPK.equals(other.cuerpacadLineasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas[ cuerpacadLineasPK=" + cuerpacadLineasPK + " ]";
    }
    
}
