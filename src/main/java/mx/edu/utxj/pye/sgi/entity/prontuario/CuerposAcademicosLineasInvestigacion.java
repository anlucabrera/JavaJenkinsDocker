/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpos_academicos_lineas_investigacion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerposAcademicosLineasInvestigacion.findAll", query = "SELECT c FROM CuerposAcademicosLineasInvestigacion c")
    , @NamedQuery(name = "CuerposAcademicosLineasInvestigacion.findByIdLinea", query = "SELECT c FROM CuerposAcademicosLineasInvestigacion c WHERE c.idLinea = :idLinea")
    , @NamedQuery(name = "CuerposAcademicosLineasInvestigacion.findByLineaInvestigacion", query = "SELECT c FROM CuerposAcademicosLineasInvestigacion c WHERE c.lineaInvestigacion = :lineaInvestigacion")})
public class CuerposAcademicosLineasInvestigacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_linea")
    private Integer idLinea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "linea_investigacion")
    private String lineaInvestigacion;
    @JoinColumn(name = "clave_registro", referencedColumnName = "clave_registro")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CuerposAcademicos claveRegistro;

    public CuerposAcademicosLineasInvestigacion() {
    }

    public CuerposAcademicosLineasInvestigacion(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public CuerposAcademicosLineasInvestigacion(Integer idLinea, String lineaInvestigacion) {
        this.idLinea = idLinea;
        this.lineaInvestigacion = lineaInvestigacion;
    }

    public Integer getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public String getLineaInvestigacion() {
        return lineaInvestigacion;
    }

    public void setLineaInvestigacion(String lineaInvestigacion) {
        this.lineaInvestigacion = lineaInvestigacion;
    }

    public CuerposAcademicos getClaveRegistro() {
        return claveRegistro;
    }

    public void setClaveRegistro(CuerposAcademicos claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLinea != null ? idLinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerposAcademicosLineasInvestigacion)) {
            return false;
        }
        CuerposAcademicosLineasInvestigacion other = (CuerposAcademicosLineasInvestigacion) object;
        if ((this.idLinea == null && other.idLinea != null) || (this.idLinea != null && !this.idLinea.equals(other.idLinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CuerposAcademicosLineasInvestigacion[ idLinea=" + idLinea + " ]";
    }
    
}
