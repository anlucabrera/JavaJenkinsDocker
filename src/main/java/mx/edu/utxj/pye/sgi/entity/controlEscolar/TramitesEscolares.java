/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "tramites_escolares", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TramitesEscolares.findAll", query = "SELECT t FROM TramitesEscolares t")
    , @NamedQuery(name = "TramitesEscolares.findByIdTramite", query = "SELECT t FROM TramitesEscolares t WHERE t.idTramite = :idTramite")
    , @NamedQuery(name = "TramitesEscolares.findByTipoTramite", query = "SELECT t FROM TramitesEscolares t WHERE t.tipoTramite = :tipoTramite")
    , @NamedQuery(name = "TramitesEscolares.findByTipoPersona", query = "SELECT t FROM TramitesEscolares t WHERE t.tipoPersona = :tipoPersona")
    , @NamedQuery(name = "TramitesEscolares.findByMaxDia", query = "SELECT t FROM TramitesEscolares t WHERE t.maxDia = :maxDia")
    , @NamedQuery(name = "TramitesEscolares.findByFechaInicio", query = "SELECT t FROM TramitesEscolares t WHERE t.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "TramitesEscolares.findByFechaFin", query = "SELECT t FROM TramitesEscolares t WHERE t.fechaFin = :fechaFin")
    , @NamedQuery(name = "TramitesEscolares.findByPeriodo", query = "SELECT t FROM TramitesEscolares t WHERE t.periodo = :periodo")})
public class TramitesEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tramite")
    private Integer idTramite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "tipo_tramite")
    private String tipoTramite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tipo_persona")
    private String tipoPersona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_dia")
    private int maxDia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tramitesEscolares")
    private List<CitasAspirantes> citasAspirantesList;

    public TramitesEscolares() {
    }

    public TramitesEscolares(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public TramitesEscolares(Integer idTramite, String tipoTramite, String tipoPersona, int maxDia, Date fechaInicio, Date fechaFin, int periodo) {
        this.idTramite = idTramite;
        this.tipoTramite = tipoTramite;
        this.tipoPersona = tipoPersona;
        this.maxDia = maxDia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.periodo = periodo;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public String getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(String tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public int getMaxDia() {
        return maxDia;
    }

    public void setMaxDia(int maxDia) {
        this.maxDia = maxDia;
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

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @XmlTransient
    public List<CitasAspirantes> getCitasAspirantesList() {
        return citasAspirantesList;
    }

    public void setCitasAspirantesList(List<CitasAspirantes> citasAspirantesList) {
        this.citasAspirantesList = citasAspirantesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTramite != null ? idTramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TramitesEscolares)) {
            return false;
        }
        TramitesEscolares other = (TramitesEscolares) object;
        if ((this.idTramite == null && other.idTramite != null) || (this.idTramite != null && !this.idTramite.equals(other.idTramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TramitesEscolares[ idTramite=" + idTramite + " ]";
    }
    
}
