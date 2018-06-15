/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
@Table(name = "bolsa_trabajo_plazas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BolsaTrabajoPlazas.findAll", query = "SELECT b FROM BolsaTrabajoPlazas b")
    , @NamedQuery(name = "BolsaTrabajoPlazas.findByIdPlaza", query = "SELECT b FROM BolsaTrabajoPlazas b WHERE b.idPlaza = :idPlaza")
    , @NamedQuery(name = "BolsaTrabajoPlazas.findByFecha", query = "SELECT b FROM BolsaTrabajoPlazas b WHERE b.fecha = :fecha")
    , @NamedQuery(name = "BolsaTrabajoPlazas.findByEmpresa", query = "SELECT b FROM BolsaTrabajoPlazas b WHERE b.empresa = :empresa")
    , @NamedQuery(name = "BolsaTrabajoPlazas.findByPlazasOfertadas", query = "SELECT b FROM BolsaTrabajoPlazas b WHERE b.plazasOfertadas = :plazasOfertadas")})
public class BolsaTrabajoPlazas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_plaza")
    private Integer idPlaza;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "empresa")
    private String empresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plazas_ofertadas")
    private int plazasOfertadas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlaza")
    private List<BolsaTrabajoContratados> bolsaTrabajoContratadosList;

    public BolsaTrabajoPlazas() {
    }

    public BolsaTrabajoPlazas(Integer idPlaza) {
        this.idPlaza = idPlaza;
    }

    public BolsaTrabajoPlazas(Integer idPlaza, Date fecha, String empresa, int plazasOfertadas) {
        this.idPlaza = idPlaza;
        this.fecha = fecha;
        this.empresa = empresa;
        this.plazasOfertadas = plazasOfertadas;
    }

    public Integer getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(Integer idPlaza) {
        this.idPlaza = idPlaza;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getPlazasOfertadas() {
        return plazasOfertadas;
    }

    public void setPlazasOfertadas(int plazasOfertadas) {
        this.plazasOfertadas = plazasOfertadas;
    }

    @XmlTransient
    public List<BolsaTrabajoContratados> getBolsaTrabajoContratadosList() {
        return bolsaTrabajoContratadosList;
    }

    public void setBolsaTrabajoContratadosList(List<BolsaTrabajoContratados> bolsaTrabajoContratadosList) {
        this.bolsaTrabajoContratadosList = bolsaTrabajoContratadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlaza != null ? idPlaza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BolsaTrabajoPlazas)) {
            return false;
        }
        BolsaTrabajoPlazas other = (BolsaTrabajoPlazas) object;
        if ((this.idPlaza == null && other.idPlaza != null) || (this.idPlaza != null && !this.idPlaza.equals(other.idPlaza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BolsaTrabajoPlazas[ idPlaza=" + idPlaza + " ]";
    }
    
}
