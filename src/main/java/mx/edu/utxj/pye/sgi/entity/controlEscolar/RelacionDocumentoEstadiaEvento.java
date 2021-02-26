/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "relacion_documento_estadia_evento", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelacionDocumentoEstadiaEvento.findAll", query = "SELECT r FROM RelacionDocumentoEstadiaEvento r")
    , @NamedQuery(name = "RelacionDocumentoEstadiaEvento.findByDocumentoEstadia", query = "SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.documentoEstadia = :documentoEstadia")
    , @NamedQuery(name = "RelacionDocumentoEstadiaEvento.findByActividad", query = "SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.actividad = :actividad")
    , @NamedQuery(name = "RelacionDocumentoEstadiaEvento.findByUsuario", query = "SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.usuario = :usuario")})
public class RelacionDocumentoEstadiaEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "documento_estadia")
    private Integer documentoEstadia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "actividad")
    private String actividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "usuario")
    private String usuario;

    public RelacionDocumentoEstadiaEvento() {
    }

    public RelacionDocumentoEstadiaEvento(Integer documentoEstadia) {
        this.documentoEstadia = documentoEstadia;
    }

    public RelacionDocumentoEstadiaEvento(Integer documentoEstadia, String actividad, String usuario) {
        this.documentoEstadia = documentoEstadia;
        this.actividad = actividad;
        this.usuario = usuario;
    }

    public Integer getDocumentoEstadia() {
        return documentoEstadia;
    }

    public void setDocumentoEstadia(Integer documentoEstadia) {
        this.documentoEstadia = documentoEstadia;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoEstadia != null ? documentoEstadia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelacionDocumentoEstadiaEvento)) {
            return false;
        }
        RelacionDocumentoEstadiaEvento other = (RelacionDocumentoEstadiaEvento) object;
        if ((this.documentoEstadia == null && other.documentoEstadia != null) || (this.documentoEstadia != null && !this.documentoEstadia.equals(other.documentoEstadia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.RelacionDocumentoEstadiaEvento[ documentoEstadia=" + documentoEstadia + " ]";
    }
    
}
