/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

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
@Table(name = "status_alumno", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatusAlumno.findAll", query = "SELECT s FROM StatusAlumno s")
    , @NamedQuery(name = "StatusAlumno.findByCveStatus", query = "SELECT s FROM StatusAlumno s WHERE s.cveStatus = :cveStatus")
    , @NamedQuery(name = "StatusAlumno.findByDescripcion", query = "SELECT s FROM StatusAlumno s WHERE s.descripcion = :descripcion")
    , @NamedQuery(name = "StatusAlumno.findByActivo", query = "SELECT s FROM StatusAlumno s WHERE s.activo = :activo")})
public class StatusAlumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_status")
    private Integer cveStatus;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;

    public StatusAlumno() {
    }

    public StatusAlumno(Integer cveStatus) {
        this.cveStatus = cveStatus;
    }

    public StatusAlumno(Integer cveStatus, boolean activo) {
        this.cveStatus = cveStatus;
        this.activo = activo;
    }

    public Integer getCveStatus() {
        return cveStatus;
    }

    public void setCveStatus(Integer cveStatus) {
        this.cveStatus = cveStatus;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveStatus != null ? cveStatus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StatusAlumno)) {
            return false;
        }
        StatusAlumno other = (StatusAlumno) object;
        if ((this.cveStatus == null && other.cveStatus != null) || (this.cveStatus != null && !this.cveStatus.equals(other.cveStatus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.StatusAlumno[ cveStatus=" + cveStatus + " ]";
    }
    
}
