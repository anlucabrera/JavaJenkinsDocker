/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "no_adeudo_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NoAdeudoEstudiante.findAll", query = "SELECT n FROM NoAdeudoEstudiante n")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByIdEstudiante", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.noAdeudoEstudiantePK.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByArea", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.noAdeudoEstudiantePK.area = :area")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByNivel", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.noAdeudoEstudiantePK.nivel = :nivel")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByGeneracion", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.generacion = :generacion")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByStatus", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.status = :status")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByObservaciones", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.observaciones = :observaciones")
    , @NamedQuery(name = "NoAdeudoEstudiante.findByTrabajador", query = "SELECT n FROM NoAdeudoEstudiante n WHERE n.trabajador = :trabajador")})
public class NoAdeudoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NoAdeudoEstudiantePK noAdeudoEstudiantePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "status")
    private String status;
    @Size(max = 250)
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "trabajador")
    private Integer trabajador;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @JoinColumn(name = "adeudo", referencedColumnName = "catalogo_no_adeudo")
    @ManyToOne(optional = false)
    private CatalogoNoAdeudoArea adeudo;

    public NoAdeudoEstudiante() {
    }

    public NoAdeudoEstudiante(NoAdeudoEstudiantePK noAdeudoEstudiantePK) {
        this.noAdeudoEstudiantePK = noAdeudoEstudiantePK;
    }

    public NoAdeudoEstudiante(NoAdeudoEstudiantePK noAdeudoEstudiantePK, short generacion, String status) {
        this.noAdeudoEstudiantePK = noAdeudoEstudiantePK;
        this.generacion = generacion;
        this.status = status;
    }

    public NoAdeudoEstudiante(int idEstudiante, String area, String nivel) {
        this.noAdeudoEstudiantePK = new NoAdeudoEstudiantePK(idEstudiante, area, nivel);
    }

    public NoAdeudoEstudiantePK getNoAdeudoEstudiantePK() {
        return noAdeudoEstudiantePK;
    }

    public void setNoAdeudoEstudiantePK(NoAdeudoEstudiantePK noAdeudoEstudiantePK) {
        this.noAdeudoEstudiantePK = noAdeudoEstudiantePK;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Integer trabajador) {
        this.trabajador = trabajador;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public CatalogoNoAdeudoArea getAdeudo() {
        return adeudo;
    }

    public void setAdeudo(CatalogoNoAdeudoArea adeudo) {
        this.adeudo = adeudo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noAdeudoEstudiantePK != null ? noAdeudoEstudiantePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NoAdeudoEstudiante)) {
            return false;
        }
        NoAdeudoEstudiante other = (NoAdeudoEstudiante) object;
        if ((this.noAdeudoEstudiantePK == null && other.noAdeudoEstudiantePK != null) || (this.noAdeudoEstudiantePK != null && !this.noAdeudoEstudiantePK.equals(other.noAdeudoEstudiantePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NoAdeudoEstudiante[ noAdeudoEstudiantePK=" + noAdeudoEstudiantePK + " ]";
    }
    
}
