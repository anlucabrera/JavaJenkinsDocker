/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "fechas_documentos", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FechasDocumentos.findAll", query = "SELECT f FROM FechasDocumentos f")
    , @NamedQuery(name = "FechasDocumentos.findByFechaDoc", query = "SELECT f FROM FechasDocumentos f WHERE f.fechaDoc = :fechaDoc")
    , @NamedQuery(name = "FechasDocumentos.findByGeneracion", query = "SELECT f FROM FechasDocumentos f WHERE f.generacion = :generacion")
    , @NamedQuery(name = "FechasDocumentos.findByProgramaEducativo", query = "SELECT f FROM FechasDocumentos f WHERE f.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "FechasDocumentos.findByFechaInicio", query = "SELECT f FROM FechasDocumentos f WHERE f.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "FechasDocumentos.findByFechaFin", query = "SELECT f FROM FechasDocumentos f WHERE f.fechaFin = :fechaFin")
    , @NamedQuery(name = "FechasDocumentos.findByActaExencion", query = "SELECT f FROM FechasDocumentos f WHERE f.actaExencion = :actaExencion")})
public class FechasDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fecha_doc")
    private Integer fechaDoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acta_exencion")
    @Temporal(TemporalType.DATE)
    private Date actaExencion;

    public FechasDocumentos() {
    }

    public FechasDocumentos(Integer fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public FechasDocumentos(Integer fechaDoc, short generacion, short programaEducativo, Date fechaInicio, Date fechaFin, Date actaExencion) {
        this.fechaDoc = fechaDoc;
        this.generacion = generacion;
        this.programaEducativo = programaEducativo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.actaExencion = actaExencion;
    }

    public Integer getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Integer fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getActaExencion() {
        return actaExencion;
    }

    public void setActaExencion(Date actaExencion) {
        this.actaExencion = actaExencion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaDoc != null ? fechaDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechasDocumentos)) {
            return false;
        }
        FechasDocumentos other = (FechasDocumentos) object;
        if ((this.fechaDoc == null && other.fechaDoc != null) || (this.fechaDoc != null && !this.fechaDoc.equals(other.fechaDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos[ fechaDoc=" + fechaDoc + " ]";
    }
    
}
