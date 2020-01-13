/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author PLANEACION
 */
@Entity
@Table(name = "incidencias", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Incidencias.findAll", query = "SELECT i FROM Incidencias i")
    , @NamedQuery(name = "Incidencias.findByIncidenciaID", query = "SELECT i FROM Incidencias i WHERE i.incidenciaID = :incidenciaID")
    , @NamedQuery(name = "Incidencias.findByNumeroOficio", query = "SELECT i FROM Incidencias i WHERE i.numeroOficio = :numeroOficio")
    , @NamedQuery(name = "Incidencias.findByTipo", query = "SELECT i FROM Incidencias i WHERE i.tipo = :tipo")
    , @NamedQuery(name = "Incidencias.findByFecha", query = "SELECT i FROM Incidencias i WHERE i.fecha = :fecha")
    , @NamedQuery(name = "Incidencias.findByTiempo", query = "SELECT i FROM Incidencias i WHERE i.tiempo = :tiempo")
    , @NamedQuery(name = "Incidencias.findByJustificacion", query = "SELECT i FROM Incidencias i WHERE i.justificacion = :justificacion")
    , @NamedQuery(name = "Incidencias.findByEstatus", query = "SELECT i FROM Incidencias i WHERE i.estatus = :estatus")
    , @NamedQuery(name = "Incidencias.findByEvidencia", query = "SELECT i FROM Incidencias i WHERE i.evidencia = :evidencia")})
public class Incidencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "incidencia_ID")
    private Integer incidenciaID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numeroOficio")
    private int numeroOficio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tiempo")
    @Temporal(TemporalType.TIME)
    private Date tiempo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "justificacion")
    private String justificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 500)
    @Column(name = "evidencia")
    private String evidencia;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Incidencias() {
    }

    public Incidencias(Integer incidenciaID) {
        this.incidenciaID = incidenciaID;
    }

    public Incidencias(Integer incidenciaID, int numeroOficio, String tipo, Date fecha, Date tiempo, String justificacion, String estatus) {
        this.incidenciaID = incidenciaID;
        this.numeroOficio = numeroOficio;
        this.tipo = tipo;
        this.fecha = fecha;
        this.tiempo = tiempo;
        this.justificacion = justificacion;
        this.estatus = estatus;
    }

    public Integer getIncidenciaID() {
        return incidenciaID;
    }

    public void setIncidenciaID(Integer incidenciaID) {
        this.incidenciaID = incidenciaID;
    }

    public int getNumeroOficio() {
        return numeroOficio;
    }

    public void setNumeroOficio(int numeroOficio) {
        this.numeroOficio = numeroOficio;
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

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (incidenciaID != null ? incidenciaID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Incidencias)) {
            return false;
        }
        Incidencias other = (Incidencias) object;
        if ((this.incidenciaID == null && other.incidenciaID != null) || (this.incidenciaID != null && !this.incidenciaID.equals(other.incidenciaID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Incidencias[ incidenciaID=" + incidenciaID + " ]";
    }
    
}
