/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "unidad_materia_comentario", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateriaComentario.findAll", query = "SELECT u FROM UnidadMateriaComentario u")
    , @NamedQuery(name = "UnidadMateriaComentario.findByConfiguracion", query = "SELECT u FROM UnidadMateriaComentario u WHERE u.unidadMateriaComentarioPK.configuracion = :configuracion")
    , @NamedQuery(name = "UnidadMateriaComentario.findByIdEstudiante", query = "SELECT u FROM UnidadMateriaComentario u WHERE u.unidadMateriaComentarioPK.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "UnidadMateriaComentario.findByPromedio", query = "SELECT u FROM UnidadMateriaComentario u WHERE u.promedio = :promedio")
    , @NamedQuery(name = "UnidadMateriaComentario.findByNivelacion", query = "SELECT u FROM UnidadMateriaComentario u WHERE u.nivelacion = :nivelacion")})
public class UnidadMateriaComentario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UnidadMateriaComentarioPK unidadMateriaComentarioPK;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private double promedio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nivelacion")
    private Double nivelacion;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UnidadMateriaConfiguracion unidadMateriaConfiguracion;

    public UnidadMateriaComentario() {
    }

    public UnidadMateriaComentario(UnidadMateriaComentarioPK unidadMateriaComentarioPK) {
        this.unidadMateriaComentarioPK = unidadMateriaComentarioPK;
    }

    public UnidadMateriaComentario(UnidadMateriaComentarioPK unidadMateriaComentarioPK, String comentario, double promedio) {
        this.unidadMateriaComentarioPK = unidadMateriaComentarioPK;
        this.comentario = comentario;
        this.promedio = promedio;
    }

    public UnidadMateriaComentario(int configuracion, int idEstudiante) {
        this.unidadMateriaComentarioPK = new UnidadMateriaComentarioPK(configuracion, idEstudiante);
    }

    public UnidadMateriaComentarioPK getUnidadMateriaComentarioPK() {
        return unidadMateriaComentarioPK;
    }

    public void setUnidadMateriaComentarioPK(UnidadMateriaComentarioPK unidadMateriaComentarioPK) {
        this.unidadMateriaComentarioPK = unidadMateriaComentarioPK;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public Double getNivelacion() {
        return nivelacion;
    }

    public void setNivelacion(Double nivelacion) {
        this.nivelacion = nivelacion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public UnidadMateriaConfiguracion getUnidadMateriaConfiguracion() {
        return unidadMateriaConfiguracion;
    }

    public void setUnidadMateriaConfiguracion(UnidadMateriaConfiguracion unidadMateriaConfiguracion) {
        this.unidadMateriaConfiguracion = unidadMateriaConfiguracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unidadMateriaComentarioPK != null ? unidadMateriaComentarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaComentario)) {
            return false;
        }
        UnidadMateriaComentario other = (UnidadMateriaComentario) object;
        if ((this.unidadMateriaComentarioPK == null && other.unidadMateriaComentarioPK != null) || (this.unidadMateriaComentarioPK != null && !this.unidadMateriaComentarioPK.equals(other.unidadMateriaComentarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaComentario[ unidadMateriaComentarioPK=" + unidadMateriaComentarioPK + " ]";
    }
    
}
