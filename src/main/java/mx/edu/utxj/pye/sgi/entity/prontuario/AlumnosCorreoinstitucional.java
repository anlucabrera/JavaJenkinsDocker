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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "alumnos_correoinstitucional", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlumnosCorreoinstitucional.findAll", query = "SELECT a FROM AlumnosCorreoinstitucional a")
    , @NamedQuery(name = "AlumnosCorreoinstitucional.findByMatricula", query = "SELECT a FROM AlumnosCorreoinstitucional a WHERE a.matricula = :matricula")
    , @NamedQuery(name = "AlumnosCorreoinstitucional.findByCorreoInstitucional", query = "SELECT a FROM AlumnosCorreoinstitucional a WHERE a.correoInstitucional = :correoInstitucional")})
public class AlumnosCorreoinstitucional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private Integer matricula;
    @Size(max = 255)
    @Column(name = "correo_institucional")
    private String correoInstitucional;

    public AlumnosCorreoinstitucional() {
    }

    public AlumnosCorreoinstitucional(Integer matricula) {
        this.matricula = matricula;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlumnosCorreoinstitucional)) {
            return false;
        }
        AlumnosCorreoinstitucional other = (AlumnosCorreoinstitucional) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.AlumnosCorreoinstitucional[ matricula=" + matricula + " ]";
    }
    
}
