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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "asistenciasacademicas", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asistenciasacademicas.findAll", query = "SELECT a FROM Asistenciasacademicas a")
    , @NamedQuery(name = "Asistenciasacademicas.findByAcademica", query = "SELECT a FROM Asistenciasacademicas a WHERE a.academica = :academica")
    , @NamedQuery(name = "Asistenciasacademicas.findByTipoAsistenciaA", query = "SELECT a FROM Asistenciasacademicas a WHERE a.tipoAsistenciaA = :tipoAsistenciaA")})
public class Asistenciasacademicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "academica")
    private Long academica;
    @Size(max = 11)
    @Column(name = "tipoAsistenciaA")
    private String tipoAsistenciaA;
    @JoinColumn(name = "asistencia", referencedColumnName = "asistencia")
    @ManyToOne
    private Asistencias asistencia;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne
    private Estudiante estudiante;
    @JoinColumn(name = "cargaAcademica", referencedColumnName = "carga")
    @ManyToOne
    private CargaAcademica cargaAcademica;

    public Asistenciasacademicas() {
    }

    public Asistenciasacademicas(Long academica) {
        this.academica = academica;
    }

    public Long getAcademica() {
        return academica;
    }

    public void setAcademica(Long academica) {
        this.academica = academica;
    }

    public String getTipoAsistenciaA() {
        return tipoAsistenciaA;
    }

    public void setTipoAsistenciaA(String tipoAsistenciaA) {
        this.tipoAsistenciaA = tipoAsistenciaA;
    }

    public Asistencias getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencias asistencia) {
        this.asistencia = asistencia;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public CargaAcademica getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(CargaAcademica cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (academica != null ? academica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asistenciasacademicas)) {
            return false;
        }
        Asistenciasacademicas other = (Asistenciasacademicas) object;
        if ((this.academica == null && other.academica != null) || (this.academica != null && !this.academica.equals(other.academica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas[ academica=" + academica + " ]";
    }
    
}
