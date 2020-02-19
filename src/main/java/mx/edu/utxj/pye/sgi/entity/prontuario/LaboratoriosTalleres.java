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
@Table(name = "laboratorios_talleres", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LaboratoriosTalleres.findAll", query = "SELECT l FROM LaboratoriosTalleres l")
    , @NamedQuery(name = "LaboratoriosTalleres.findByIdLab", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.idLab = :idLab")
    , @NamedQuery(name = "LaboratoriosTalleres.findByCiclo", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.ciclo = :ciclo")
    , @NamedQuery(name = "LaboratoriosTalleres.findByPeriodo", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "LaboratoriosTalleres.findByEdificio", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.edificio = :edificio")
    , @NamedQuery(name = "LaboratoriosTalleres.findByLaboratorio", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.laboratorio = :laboratorio")
    , @NamedQuery(name = "LaboratoriosTalleres.findByCapEst", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.capEst = :capEst")
    , @NamedQuery(name = "LaboratoriosTalleres.findByAreaResp", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.areaResp = :areaResp")
    , @NamedQuery(name = "LaboratoriosTalleres.findByFechaHabilitacion", query = "SELECT l FROM LaboratoriosTalleres l WHERE l.fechaHabilitacion = :fechaHabilitacion")})
public class LaboratoriosTalleres implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_lab")
    private Integer idLab;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private short periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "edificio")
    private String edificio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "laboratorio")
    private String laboratorio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cap_est")
    private String capEst;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "area_resp")
    private String areaResp;
    @Column(name = "fecha_habilitacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHabilitacion;

    public LaboratoriosTalleres() {
    }

    public LaboratoriosTalleres(Integer idLab) {
        this.idLab = idLab;
    }

    public LaboratoriosTalleres(Integer idLab, int ciclo, short periodo, String edificio, String laboratorio, String capEst, String areaResp) {
        this.idLab = idLab;
        this.ciclo = ciclo;
        this.periodo = periodo;
        this.edificio = edificio;
        this.laboratorio = laboratorio;
        this.capEst = capEst;
        this.areaResp = areaResp;
    }

    public Integer getIdLab() {
        return idLab;
    }

    public void setIdLab(Integer idLab) {
        this.idLab = idLab;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getCapEst() {
        return capEst;
    }

    public void setCapEst(String capEst) {
        this.capEst = capEst;
    }

    public String getAreaResp() {
        return areaResp;
    }

    public void setAreaResp(String areaResp) {
        this.areaResp = areaResp;
    }

    public Date getFechaHabilitacion() {
        return fechaHabilitacion;
    }

    public void setFechaHabilitacion(Date fechaHabilitacion) {
        this.fechaHabilitacion = fechaHabilitacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLab != null ? idLab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LaboratoriosTalleres)) {
            return false;
        }
        LaboratoriosTalleres other = (LaboratoriosTalleres) object;
        if ((this.idLab == null && other.idLab != null) || (this.idLab != null && !this.idLab.equals(other.idLab))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.LaboratoriosTalleres[ idLab=" + idLab + " ]";
    }
    
}
