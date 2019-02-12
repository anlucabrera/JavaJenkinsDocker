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
@Table(name = "cuerpo_areas_academicas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpoAreasAcademicas.findAll", query = "SELECT c FROM CuerpoAreasAcademicas c")
    , @NamedQuery(name = "CuerpoAreasAcademicas.findByCuerpoAcademico", query = "SELECT c FROM CuerpoAreasAcademicas c WHERE c.cuerpoAreasAcademicasPK.cuerpoAcademico = :cuerpoAcademico")
    , @NamedQuery(name = "CuerpoAreasAcademicas.findByAreaAcademica", query = "SELECT c FROM CuerpoAreasAcademicas c WHERE c.cuerpoAreasAcademicasPK.areaAcademica = :areaAcademica")})
public class CuerpoAreasAcademicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CuerpoAreasAcademicasPK cuerpoAreasAcademicasPK;
    @JoinColumn(name = "cuerpo_academico", referencedColumnName = "cuerpo_academico", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerposAcademicosRegistro;

    public CuerpoAreasAcademicas() {
    }

    public CuerpoAreasAcademicas(CuerpoAreasAcademicasPK cuerpoAreasAcademicasPK) {
        this.cuerpoAreasAcademicasPK = cuerpoAreasAcademicasPK;
    }

    public CuerpoAreasAcademicas(String cuerpoAcademico, short areaAcademica) {
        this.cuerpoAreasAcademicasPK = new CuerpoAreasAcademicasPK(cuerpoAcademico, areaAcademica);
    }

    public CuerpoAreasAcademicasPK getCuerpoAreasAcademicasPK() {
        return cuerpoAreasAcademicasPK;
    }

    public void setCuerpoAreasAcademicasPK(CuerpoAreasAcademicasPK cuerpoAreasAcademicasPK) {
        this.cuerpoAreasAcademicasPK = cuerpoAreasAcademicasPK;
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
        hash += (cuerpoAreasAcademicasPK != null ? cuerpoAreasAcademicasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpoAreasAcademicas)) {
            return false;
        }
        CuerpoAreasAcademicas other = (CuerpoAreasAcademicas) object;
        if ((this.cuerpoAreasAcademicasPK == null && other.cuerpoAreasAcademicasPK != null) || (this.cuerpoAreasAcademicasPK != null && !this.cuerpoAreasAcademicasPK.equals(other.cuerpoAreasAcademicasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicas[ cuerpoAreasAcademicasPK=" + cuerpoAreasAcademicasPK + " ]";
    }
    
}
