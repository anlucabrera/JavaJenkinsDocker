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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evidencia_contratos_personal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvidenciaContratosPersonal.findAll", query = "SELECT e FROM EvidenciaContratosPersonal e")
    , @NamedQuery(name = "EvidenciaContratosPersonal.findByClave", query = "SELECT e FROM EvidenciaContratosPersonal e WHERE e.clave = :clave")
    , @NamedQuery(name = "EvidenciaContratosPersonal.findByPeriodo", query = "SELECT e FROM EvidenciaContratosPersonal e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "EvidenciaContratosPersonal.findByContratoEvidencia", query = "SELECT e FROM EvidenciaContratosPersonal e WHERE e.contratoEvidencia = :contratoEvidencia")})
public class EvidenciaContratosPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private Integer clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "periodo")
    private String periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "contrato_evidencia")
    private String contratoEvidencia;
    @JoinColumn(name = "clave", referencedColumnName = "clave", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Personal personal;

    public EvidenciaContratosPersonal() {
    }

    public EvidenciaContratosPersonal(Integer clave) {
        this.clave = clave;
    }

    public EvidenciaContratosPersonal(Integer clave, String periodo, String contratoEvidencia) {
        this.clave = clave;
        this.periodo = periodo;
        this.contratoEvidencia = contratoEvidencia;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getContratoEvidencia() {
        return contratoEvidencia;
    }

    public void setContratoEvidencia(String contratoEvidencia) {
        this.contratoEvidencia = contratoEvidencia;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvidenciaContratosPersonal)) {
            return false;
        }
        EvidenciaContratosPersonal other = (EvidenciaContratosPersonal) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvidenciaContratosPersonal[ clave=" + clave + " ]";
    }
    
}
