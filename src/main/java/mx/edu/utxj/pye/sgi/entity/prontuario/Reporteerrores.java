/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "reporteerrores", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reporteerrores.findAll", query = "SELECT r FROM Reporteerrores r")
    , @NamedQuery(name = "Reporteerrores.findByIdError", query = "SELECT r FROM Reporteerrores r WHERE r.idError = :idError")
    , @NamedQuery(name = "Reporteerrores.findByClase", query = "SELECT r FROM Reporteerrores r WHERE r.clase = :clase")
    , @NamedQuery(name = "Reporteerrores.findByTipo", query = "SELECT r FROM Reporteerrores r WHERE r.tipo = :tipo")
    , @NamedQuery(name = "Reporteerrores.findByFecha", query = "SELECT r FROM Reporteerrores r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "Reporteerrores.findByRuta", query = "SELECT r FROM Reporteerrores r WHERE r.ruta = :ruta")
    , @NamedQuery(name = "Reporteerrores.findByMensaje", query = "SELECT r FROM Reporteerrores r WHERE r.mensaje = :mensaje")
    , @NamedQuery(name = "Reporteerrores.findByEstatus", query = "SELECT r FROM Reporteerrores r WHERE r.estatus = :estatus")})
public class Reporteerrores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_error")
    private Integer idError;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "clase")
    private String clase;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "mensaje")
    private String mensaje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "estatus")
    private String estatus;

    public Reporteerrores() {
    }

    public Reporteerrores(Integer idError) {
        this.idError = idError;
    }

    public Reporteerrores(Integer idError, String clase, String tipo, Date fecha, String ruta, String mensaje, String estatus) {
        this.idError = idError;
        this.clase = clase;
        this.tipo = tipo;
        this.fecha = fecha;
        this.ruta = ruta;
        this.mensaje = mensaje;
        this.estatus = estatus;
    }

    public Integer getIdError() {
        return idError;
    }

    public void setIdError(Integer idError) {
        this.idError = idError;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idError != null ? idError.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reporteerrores)) {
            return false;
        }
        Reporteerrores other = (Reporteerrores) object;
        if ((this.idError == null && other.idError != null) || (this.idError != null && !this.idError.equals(other.idError))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Reporteerrores[ idError=" + idError + " ]";
    }
    
}
