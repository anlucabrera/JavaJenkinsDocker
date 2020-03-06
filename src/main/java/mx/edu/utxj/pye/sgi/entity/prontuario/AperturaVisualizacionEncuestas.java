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
@Table(name = "apertura_visualizacion_encuestas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AperturaVisualizacionEncuestas.findAll", query = "SELECT a FROM AperturaVisualizacionEncuestas a")
    , @NamedQuery(name = "AperturaVisualizacionEncuestas.findByApertura", query = "SELECT a FROM AperturaVisualizacionEncuestas a WHERE a.apertura = :apertura")
    , @NamedQuery(name = "AperturaVisualizacionEncuestas.findByEncuesta", query = "SELECT a FROM AperturaVisualizacionEncuestas a WHERE a.encuesta = :encuesta")
    , @NamedQuery(name = "AperturaVisualizacionEncuestas.findByFechaInicial", query = "SELECT a FROM AperturaVisualizacionEncuestas a WHERE a.fechaInicial = :fechaInicial")
    , @NamedQuery(name = "AperturaVisualizacionEncuestas.findByFechaFinal", query = "SELECT a FROM AperturaVisualizacionEncuestas a WHERE a.fechaFinal = :fechaFinal")})
public class AperturaVisualizacionEncuestas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "apertura")
    private Integer apertura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "encuesta")
    private String encuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;

    public AperturaVisualizacionEncuestas() {
    }

    public AperturaVisualizacionEncuestas(Integer apertura) {
        this.apertura = apertura;
    }

    public AperturaVisualizacionEncuestas(Integer apertura, String encuesta, Date fechaInicial, Date fechaFinal) {
        this.apertura = apertura;
        this.encuesta = encuesta;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    public Integer getApertura() {
        return apertura;
    }

    public void setApertura(Integer apertura) {
        this.apertura = apertura;
    }

    public String getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(String encuesta) {
        this.encuesta = encuesta;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apertura != null ? apertura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AperturaVisualizacionEncuestas)) {
            return false;
        }
        AperturaVisualizacionEncuestas other = (AperturaVisualizacionEncuestas) object;
        if ((this.apertura == null && other.apertura != null) || (this.apertura != null && !this.apertura.equals(other.apertura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas[ apertura=" + apertura + " ]";
    }
    
}
