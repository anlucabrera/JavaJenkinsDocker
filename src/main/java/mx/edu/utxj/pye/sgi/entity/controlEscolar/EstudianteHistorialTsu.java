/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "estudiante_historial_tsu", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstudianteHistorialTsu.findAll", query = "SELECT e FROM EstudianteHistorialTsu e")
    , @NamedQuery(name = "EstudianteHistorialTsu.findByEstudianteHistoricoTSU", query = "SELECT e FROM EstudianteHistorialTsu e WHERE e.estudianteHistoricoTSU = :estudianteHistoricoTSU")
    , @NamedQuery(name = "EstudianteHistorialTsu.findByGeneracion", query = "SELECT e FROM EstudianteHistorialTsu e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EstudianteHistorialTsu.findByPeriodoCarrera", query = "SELECT e FROM EstudianteHistorialTsu e WHERE e.periodoCarrera = :periodoCarrera")
    , @NamedQuery(name = "EstudianteHistorialTsu.findByPromedioEgreso", query = "SELECT e FROM EstudianteHistorialTsu e WHERE e.promedioEgreso = :promedioEgreso")})
public class EstudianteHistorialTsu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "estudianteHistoricoTSU")
    private Integer estudianteHistoricoTSU;
    @Size(max = 255)
    @Column(name = "generacion")
    private String generacion;
    @Size(max = 255)
    @Column(name = "periodoCarrera")
    private String periodoCarrera;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "promedioEgreso")
    private Double promedioEgreso;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante idEstudiante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteHistoricoTSU")
    private List<CalificacionesHistorialTsu> calificacionesHistorialTsuList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteHistoricoTSU")
    private List<CalificacionesHistorialTsuOtrosPe> calificacionesHistorialTsuOtrosPes;

    public EstudianteHistorialTsu() {
    }

    public EstudianteHistorialTsu(Integer estudianteHistoricoTSU) {
        this.estudianteHistoricoTSU = estudianteHistoricoTSU;
    }

    public Integer getEstudianteHistoricoTSU() {
        return estudianteHistoricoTSU;
    }

    public void setEstudianteHistoricoTSU(Integer estudianteHistoricoTSU) {
        this.estudianteHistoricoTSU = estudianteHistoricoTSU;
    }

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public String getPeriodoCarrera() {
        return periodoCarrera;
    }

    public void setPeriodoCarrera(String periodoCarrera) {
        this.periodoCarrera = periodoCarrera;
    }

    public Double getPromedioEgreso() {
        return promedioEgreso;
    }

    public void setPromedioEgreso(Double promedioEgreso) {
        this.promedioEgreso = promedioEgreso;
    }

    public Estudiante getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Estudiante idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @XmlTransient
    public List<CalificacionesHistorialTsu> getCalificacionesHistorialTsuList() {
        return calificacionesHistorialTsuList;
    }

    public void setCalificacionesHistorialTsuList(List<CalificacionesHistorialTsu> calificacionesHistorialTsuList) {
        this.calificacionesHistorialTsuList = calificacionesHistorialTsuList;
    }

    @XmlTransient
    public List<CalificacionesHistorialTsuOtrosPe> getCalificacionesHistorialTsuOtrosPe() {
        return calificacionesHistorialTsuOtrosPes;
    }

    public void setCalificacionesHistorialTsuOtrosPe(List<CalificacionesHistorialTsuOtrosPe> calificacionesHistorialTsuOtrosPes) {
        this.calificacionesHistorialTsuOtrosPes = calificacionesHistorialTsuOtrosPes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estudianteHistoricoTSU != null ? estudianteHistoricoTSU.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstudianteHistorialTsu)) {
            return false;
        }
        EstudianteHistorialTsu other = (EstudianteHistorialTsu) object;
        if ((this.estudianteHistoricoTSU == null && other.estudianteHistoricoTSU != null) || (this.estudianteHistoricoTSU != null && !this.estudianteHistoricoTSU.equals(other.estudianteHistoricoTSU))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EstudianteHistorialTsu[ estudianteHistoricoTSU=" + estudianteHistoricoTSU + " ]";
    }
    
}
