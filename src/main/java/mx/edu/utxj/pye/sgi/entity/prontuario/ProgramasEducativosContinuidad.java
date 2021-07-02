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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "programas_educativos_continuidad", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEducativosContinuidad.findAll", query = "SELECT p FROM ProgramasEducativosContinuidad p")
    , @NamedQuery(name = "ProgramasEducativosContinuidad.findByContinuidad", query = "SELECT p FROM ProgramasEducativosContinuidad p WHERE p.continuidad = :continuidad")
    , @NamedQuery(name = "ProgramasEducativosContinuidad.findByProgramaContinuidad", query = "SELECT p FROM ProgramasEducativosContinuidad p WHERE p.programaContinuidad = :programaContinuidad")
    , @NamedQuery(name = "ProgramasEducativosContinuidad.findByActivo", query = "SELECT p FROM ProgramasEducativosContinuidad p WHERE p.activo = :activo")})
public class ProgramasEducativosContinuidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "continuidad")
    private Integer continuidad;
    @Column(name = "programaContinuidad")
    private Short programaContinuidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "programaTSU", referencedColumnName = "area")
    @ManyToOne(optional = false)
    private AreasUniversidad programaTSU;

    public ProgramasEducativosContinuidad() {
    }

    public ProgramasEducativosContinuidad(Integer continuidad) {
        this.continuidad = continuidad;
    }

    public ProgramasEducativosContinuidad(Integer continuidad, boolean activo) {
        this.continuidad = continuidad;
        this.activo = activo;
    }

    public Integer getContinuidad() {
        return continuidad;
    }

    public void setContinuidad(Integer continuidad) {
        this.continuidad = continuidad;
    }

    public Short getProgramaContinuidad() {
        return programaContinuidad;
    }

    public void setProgramaContinuidad(Short programaContinuidad) {
        this.programaContinuidad = programaContinuidad;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public AreasUniversidad getProgramaTSU() {
        return programaTSU;
    }

    public void setProgramaTSU(AreasUniversidad programaTSU) {
        this.programaTSU = programaTSU;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (continuidad != null ? continuidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEducativosContinuidad)) {
            return false;
        }
        ProgramasEducativosContinuidad other = (ProgramasEducativosContinuidad) object;
        if ((this.continuidad == null && other.continuidad != null) || (this.continuidad != null && !this.continuidad.equals(other.continuidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad[ continuidad=" + continuidad + " ]";
    }
    
}
