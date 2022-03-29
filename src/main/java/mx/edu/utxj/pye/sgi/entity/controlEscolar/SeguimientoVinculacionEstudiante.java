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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "seguimiento_vinculacion_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeguimientoVinculacionEstudiante.findAll", query = "SELECT s FROM SeguimientoVinculacionEstudiante s")
    , @NamedQuery(name = "SeguimientoVinculacionEstudiante.findBySeguimientoVinculacion", query = "SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.seguimientoVinculacion = :seguimientoVinculacion")
    , @NamedQuery(name = "SeguimientoVinculacionEstudiante.findByNivel", query = "SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.nivel = :nivel")
    , @NamedQuery(name = "SeguimientoVinculacionEstudiante.findByValidado", query = "SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.validado = :validado")
    , @NamedQuery(name = "SeguimientoVinculacionEstudiante.findByActivo", query = "SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.activo = :activo")})
public class SeguimientoVinculacionEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "seguimiento_vinculacion")
    private Integer seguimientoVinculacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "nivel")
    private String nivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento")
    private List<AperturaExtemporaneaEventoVinculacion> aperturaExtemporaneaEventoVinculacionList;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento", fetch = FetchType.LAZY)
    private List<DocumentoSeguimientoVinculacion> documentoSeguimientoVinculacionList;


    public SeguimientoVinculacionEstudiante() {
    }

    public SeguimientoVinculacionEstudiante(Integer seguimientoVinculacion) {
        this.seguimientoVinculacion = seguimientoVinculacion;
    }

    public SeguimientoVinculacionEstudiante(Integer seguimientoVinculacion, String nivel, boolean validado, boolean activo) {
        this.seguimientoVinculacion = seguimientoVinculacion;
        this.nivel = nivel;
        this.validado = validado;
        this.activo = activo;
    }

    public Integer getSeguimientoVinculacion() {
        return seguimientoVinculacion;
    }

    public void setSeguimientoVinculacion(Integer seguimientoVinculacion) {
        this.seguimientoVinculacion = seguimientoVinculacion;
    }
    
    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public boolean getValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }
    
    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    @XmlTransient
    public List<DocumentoSeguimientoVinculacion> getDocumentoSeguimientoVinculacionList() {
        return documentoSeguimientoVinculacionList;
    }

    public void setDocumentoSeguimientoVinculacionList(List<DocumentoSeguimientoVinculacion> documentoSeguimientoVinculacionList) {
        this.documentoSeguimientoVinculacionList = documentoSeguimientoVinculacionList;
    }
    
    @XmlTransient
    public List<AperturaExtemporaneaEventoVinculacion> getAperturaExtemporaneaEventoVinculacionList() {
        return aperturaExtemporaneaEventoVinculacionList;
    }

    public void setAperturaExtemporaneaEventoVinculacionList(List<AperturaExtemporaneaEventoVinculacion> aperturaExtemporaneaEventoVinculacionList) {
        this.aperturaExtemporaneaEventoVinculacionList = aperturaExtemporaneaEventoVinculacionList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seguimientoVinculacion != null ? seguimientoVinculacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeguimientoVinculacionEstudiante)) {
            return false;
        }
        SeguimientoVinculacionEstudiante other = (SeguimientoVinculacionEstudiante) object;
        if ((this.seguimientoVinculacion == null && other.seguimientoVinculacion != null) || (this.seguimientoVinculacion != null && !this.seguimientoVinculacion.equals(other.seguimientoVinculacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante[ seguimientoVinculacion=" + seguimientoVinculacion + " ]";
    }

}
