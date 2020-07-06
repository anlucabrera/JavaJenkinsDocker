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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
 * @author UTXJ
 */
@Entity
@Table(name = "citas_aspirantes", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitasAspirantes.findAll", query = "SELECT c FROM CitasAspirantes c")
    , @NamedQuery(name = "CitasAspirantes.findByIdCita", query = "SELECT c FROM CitasAspirantes c WHERE c.citasAspirantesPK.idCita = :idCita")
    , @NamedQuery(name = "CitasAspirantes.findByIdTramite", query = "SELECT c FROM CitasAspirantes c WHERE c.citasAspirantesPK.idTramite = :idTramite")
    , @NamedQuery(name = "CitasAspirantes.findByIdAspirante", query = "SELECT c FROM CitasAspirantes c WHERE c.citasAspirantesPK.idAspirante = :idAspirante")
    , @NamedQuery(name = "CitasAspirantes.findByFechaCita", query = "SELECT c FROM CitasAspirantes c WHERE c.fechaCita = :fechaCita")
    , @NamedQuery(name = "CitasAspirantes.findByFolioCita", query = "SELECT c FROM CitasAspirantes c WHERE c.folioCita = :folioCita")
    , @NamedQuery(name = "CitasAspirantes.findByStatus", query = "SELECT c FROM CitasAspirantes c WHERE c.status = :status")})
public class CitasAspirantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CitasAspirantesPK citasAspirantesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cita")
    @Temporal(TemporalType.DATE)
    private Date fechaCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "folio_cita")
    private int folioCita;
    @Size(max = 13)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "id_aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Aspirante aspirante;
    @JoinColumn(name = "id_tramite", referencedColumnName = "id_tramite", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TramitesEscolares tramitesEscolares;

    public CitasAspirantes() {
    }

    public CitasAspirantes(CitasAspirantesPK citasAspirantesPK) {
        this.citasAspirantesPK = citasAspirantesPK;
    }

    public CitasAspirantes(CitasAspirantesPK citasAspirantesPK, Date fechaCita, int folioCita) {
        this.citasAspirantesPK = citasAspirantesPK;
        this.fechaCita = fechaCita;
        this.folioCita = folioCita;
    }

    public CitasAspirantes(int idCita, int idTramite, int idAspirante) {
        this.citasAspirantesPK = new CitasAspirantesPK(idCita, idTramite, idAspirante);
    }

    public CitasAspirantesPK getCitasAspirantesPK() {
        return citasAspirantesPK;
    }

    public void setCitasAspirantesPK(CitasAspirantesPK citasAspirantesPK) {
        this.citasAspirantesPK = citasAspirantesPK;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public int getFolioCita() {
        return folioCita;
    }

    public void setFolioCita(int folioCita) {
        this.folioCita = folioCita;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public TramitesEscolares getTramitesEscolares() {
        return tramitesEscolares;
    }

    public void setTramitesEscolares(TramitesEscolares tramitesEscolares) {
        this.tramitesEscolares = tramitesEscolares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citasAspirantesPK != null ? citasAspirantesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitasAspirantes)) {
            return false;
        }
        CitasAspirantes other = (CitasAspirantes) object;
        if ((this.citasAspirantesPK == null && other.citasAspirantesPK != null) || (this.citasAspirantesPK != null && !this.citasAspirantesPK.equals(other.citasAspirantesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CitasAspirantes[ citasAspirantesPK=" + citasAspirantesPK + " ]";
    }
    
}
