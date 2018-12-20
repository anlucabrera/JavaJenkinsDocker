/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "desarrollos_tecnologicos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesarrollosTecnologicos.findAll", query = "SELECT d FROM DesarrollosTecnologicos d")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByDesarrollo", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.desarrollo = :desarrollo")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByTipoDesarrollo", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.tipoDesarrollo = :tipoDesarrollo")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByDocumentoRespaldo", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.documentoRespaldo = :documentoRespaldo")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByObjetivoDesarrollo", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.objetivoDesarrollo = :objetivoDesarrollo")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByResumenDesarrollo", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.resumenDesarrollo = :resumenDesarrollo")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByApoyoRecibido", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.apoyoRecibido = :apoyoRecibido")
    , @NamedQuery(name = "DesarrollosTecnologicos.findByEstatus", query = "SELECT d FROM DesarrollosTecnologicos d WHERE d.estatus = :estatus")})
public class DesarrollosTecnologicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "desarrollo")
    private Integer desarrollo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "tipo_desarrollo")
    private String tipoDesarrollo;
    @Size(max = 500)
    @Column(name = "documento_respaldo")
    private String documentoRespaldo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "objetivo_desarrollo")
    private String objetivoDesarrollo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "resumen_desarrollo")
    private String resumenDesarrollo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "apoyo_recibido")
    private String apoyoRecibido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public DesarrollosTecnologicos() {
    }

    public DesarrollosTecnologicos(Integer desarrollo) {
        this.desarrollo = desarrollo;
    }

    public DesarrollosTecnologicos(Integer desarrollo, String tipoDesarrollo, String objetivoDesarrollo, String resumenDesarrollo, String apoyoRecibido, String estatus) {
        this.desarrollo = desarrollo;
        this.tipoDesarrollo = tipoDesarrollo;
        this.objetivoDesarrollo = objetivoDesarrollo;
        this.resumenDesarrollo = resumenDesarrollo;
        this.apoyoRecibido = apoyoRecibido;
        this.estatus = estatus;
    }

    public Integer getDesarrollo() {
        return desarrollo;
    }

    public void setDesarrollo(Integer desarrollo) {
        this.desarrollo = desarrollo;
    }

    public String getTipoDesarrollo() {
        return tipoDesarrollo;
    }

    public void setTipoDesarrollo(String tipoDesarrollo) {
        this.tipoDesarrollo = tipoDesarrollo;
    }

    public String getDocumentoRespaldo() {
        return documentoRespaldo;
    }

    public void setDocumentoRespaldo(String documentoRespaldo) {
        this.documentoRespaldo = documentoRespaldo;
    }

    public String getObjetivoDesarrollo() {
        return objetivoDesarrollo;
    }

    public void setObjetivoDesarrollo(String objetivoDesarrollo) {
        this.objetivoDesarrollo = objetivoDesarrollo;
    }

    public String getResumenDesarrollo() {
        return resumenDesarrollo;
    }

    public void setResumenDesarrollo(String resumenDesarrollo) {
        this.resumenDesarrollo = resumenDesarrollo;
    }

    public String getApoyoRecibido() {
        return apoyoRecibido;
    }

    public void setApoyoRecibido(String apoyoRecibido) {
        this.apoyoRecibido = apoyoRecibido;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
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
        hash += (desarrollo != null ? desarrollo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesarrollosTecnologicos)) {
            return false;
        }
        DesarrollosTecnologicos other = (DesarrollosTecnologicos) object;
        if ((this.desarrollo == null && other.desarrollo != null) || (this.desarrollo != null && !this.desarrollo.equals(other.desarrollo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos[ desarrollo=" + desarrollo + " ]";
    }
    
}
