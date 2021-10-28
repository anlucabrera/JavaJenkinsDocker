/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "registro_egresados_terminacion_estudios", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findAll", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r")
    , @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findByIdRegistro", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r WHERE r.idRegistro = :idRegistro")
    , @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findByGeneracion", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r WHERE r.generacion = :generacion")
    , @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findByFolio", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r WHERE r.folio = :folio")
    , @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findByLibro", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r WHERE r.libro = :libro")
    , @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findByFoja", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r WHERE r.foja = :foja")
    , @NamedQuery(name = "RegistroEgresadosTerminacionEstudios.findByFechaEmision", query = "SELECT r FROM RegistroEgresadosTerminacionEstudios r WHERE r.fechaEmision = :fechaEmision")})
public class RegistroEgresadosTerminacionEstudios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_registro")
    private Integer idRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "generacion")
    private String generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "folio")
    private int folio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "libro")
    private int libro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "foja")
    private int foja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante idEstudiante;

    public RegistroEgresadosTerminacionEstudios() {
    }

    public RegistroEgresadosTerminacionEstudios(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public RegistroEgresadosTerminacionEstudios(Integer idRegistro, String generacion, int folio, int libro, int foja, Date fechaEmision) {
        this.idRegistro = idRegistro;
        this.generacion = generacion;
        this.folio = folio;
        this.libro = libro;
        this.foja = foja;
        this.fechaEmision = fechaEmision;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public int getLibro() {
        return libro;
    }

    public void setLibro(int libro) {
        this.libro = libro;
    }

    public int getFoja() {
        return foja;
    }

    public void setFoja(int foja) {
        this.foja = foja;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Estudiante getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Estudiante idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegistro != null ? idRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistroEgresadosTerminacionEstudios)) {
            return false;
        }
        RegistroEgresadosTerminacionEstudios other = (RegistroEgresadosTerminacionEstudios) object;
        if ((this.idRegistro == null && other.idRegistro != null) || (this.idRegistro != null && !this.idRegistro.equals(other.idRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.RegistroEgresadosTerminacionEstudios[ idRegistro=" + idRegistro + " ]";
    }
    
}
