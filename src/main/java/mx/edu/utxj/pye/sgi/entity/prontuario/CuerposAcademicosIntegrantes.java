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
@Table(name = "cuerpos_academicos_integrantes", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerposAcademicosIntegrantes.findAll", query = "SELECT c FROM CuerposAcademicosIntegrantes c")
    , @NamedQuery(name = "CuerposAcademicosIntegrantes.findByIdIntegrante", query = "SELECT c FROM CuerposAcademicosIntegrantes c WHERE c.idIntegrante = :idIntegrante")
    , @NamedQuery(name = "CuerposAcademicosIntegrantes.findByIntegrante", query = "SELECT c FROM CuerposAcademicosIntegrantes c WHERE c.integrante = :integrante")})
public class CuerposAcademicosIntegrantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_integrante")
    private Integer idIntegrante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "integrante")
    private String integrante;
    @JoinColumn(name = "clave_registro", referencedColumnName = "clave_registro")
    @ManyToOne(optional = false)
    private CuerposAcademicos claveRegistro;

    public CuerposAcademicosIntegrantes() {
    }

    public CuerposAcademicosIntegrantes(Integer idIntegrante) {
        this.idIntegrante = idIntegrante;
    }

    public CuerposAcademicosIntegrantes(Integer idIntegrante, String integrante) {
        this.idIntegrante = idIntegrante;
        this.integrante = integrante;
    }

    public Integer getIdIntegrante() {
        return idIntegrante;
    }

    public void setIdIntegrante(Integer idIntegrante) {
        this.idIntegrante = idIntegrante;
    }

    public String getIntegrante() {
        return integrante;
    }

    public void setIntegrante(String integrante) {
        this.integrante = integrante;
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
        hash += (idIntegrante != null ? idIntegrante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerposAcademicosIntegrantes)) {
            return false;
        }
        CuerposAcademicosIntegrantes other = (CuerposAcademicosIntegrantes) object;
        if ((this.idIntegrante == null && other.idIntegrante != null) || (this.idIntegrante != null && !this.idIntegrante.equals(other.idIntegrante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CuerposAcademicosIntegrantes[ idIntegrante=" + idIntegrante + " ]";
    }
    
}
