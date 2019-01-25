/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evidencias", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evidencias.findAll", query = "SELECT e FROM Evidencias e")
    , @NamedQuery(name = "Evidencias.findByEvidencia", query = "SELECT e FROM Evidencias e WHERE e.evidencia = :evidencia")
    , @NamedQuery(name = "Evidencias.findByCategoria", query = "SELECT e FROM Evidencias e WHERE e.categoria = :categoria")})
public class Evidencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evidencia")
    private Integer evidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "categoria")
    private String categoria;
    @ManyToMany(mappedBy = "evidenciasList")
    private List<ActividadesPoa> actividadesPoaList;
    @JoinTable(name = "registros_evidencias", joinColumns = {
        @JoinColumn(name = "evidencia", referencedColumnName = "evidencia")}, inverseJoinColumns = {
        @JoinColumn(name = "registro", referencedColumnName = "registro")})
    @ManyToMany
    private List<Registros> registrosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evidencia")
    private List<EvidenciasDetalle> evidenciasDetalleList;

    public Evidencias() {
    }

    public Evidencias(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public Evidencias(Integer evidencia, String categoria) {
        this.evidencia = evidencia;
        this.categoria = categoria;
    }

    public Integer getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public List<ActividadesPoa> getActividadesPoaList() {
        return actividadesPoaList;
    }

    public void setActividadesPoaList(List<ActividadesPoa> actividadesPoaList) {
        this.actividadesPoaList = actividadesPoaList;
    }

    @XmlTransient
    public List<Registros> getRegistrosList() {
        return registrosList;
    }

    public void setRegistrosList(List<Registros> registrosList) {
        this.registrosList = registrosList;
    }

    @XmlTransient
    public List<EvidenciasDetalle> getEvidenciasDetalleList() {
        return evidenciasDetalleList;
    }

    public void setEvidenciasDetalleList(List<EvidenciasDetalle> evidenciasDetalleList) {
        this.evidenciasDetalleList = evidenciasDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evidencia != null ? evidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evidencias)) {
            return false;
        }
        Evidencias other = (Evidencias) object;
        if ((this.evidencia == null && other.evidencia != null) || (this.evidencia != null && !this.evidencia.equals(other.evidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Evidencias[ evidencia=" + evidencia + " ]";
    }
    
}
