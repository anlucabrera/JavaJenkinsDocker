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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "aulas_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AulasTipo.findAll", query = "SELECT a FROM AulasTipo a")
    , @NamedQuery(name = "AulasTipo.findByAulatipo", query = "SELECT a FROM AulasTipo a WHERE a.aulatipo = :aulatipo")
    , @NamedQuery(name = "AulasTipo.findByNombre", query = "SELECT a FROM AulasTipo a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AulasTipo.findByCapacidadTurno", query = "SELECT a FROM AulasTipo a WHERE a.capacidadTurno = :capacidadTurno")})
public class AulasTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aulatipo")
    private Short aulatipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "capacidad_turno")
    private String capacidadTurno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aulaTipo", fetch = FetchType.LAZY)
    private List<DistribucionLabtallCicloPeriodosEscolares> distribucionLabtallCicloPeriodosEscolaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aula", fetch = FetchType.LAZY)
    private List<DistribucionAulasCicloPeriodosEscolares> distribucionAulasCicloPeriodosEscolaresList;

    public AulasTipo() {
    }

    public AulasTipo(Short aulatipo) {
        this.aulatipo = aulatipo;
    }

    public AulasTipo(Short aulatipo, String nombre, String capacidadTurno) {
        this.aulatipo = aulatipo;
        this.nombre = nombre;
        this.capacidadTurno = capacidadTurno;
    }

    public Short getAulatipo() {
        return aulatipo;
    }

    public void setAulatipo(Short aulatipo) {
        this.aulatipo = aulatipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCapacidadTurno() {
        return capacidadTurno;
    }

    public void setCapacidadTurno(String capacidadTurno) {
        this.capacidadTurno = capacidadTurno;
    }

    @XmlTransient
    public List<DistribucionLabtallCicloPeriodosEscolares> getDistribucionLabtallCicloPeriodosEscolaresList() {
        return distribucionLabtallCicloPeriodosEscolaresList;
    }

    public void setDistribucionLabtallCicloPeriodosEscolaresList(List<DistribucionLabtallCicloPeriodosEscolares> distribucionLabtallCicloPeriodosEscolaresList) {
        this.distribucionLabtallCicloPeriodosEscolaresList = distribucionLabtallCicloPeriodosEscolaresList;
    }

    @XmlTransient
    public List<DistribucionAulasCicloPeriodosEscolares> getDistribucionAulasCicloPeriodosEscolaresList() {
        return distribucionAulasCicloPeriodosEscolaresList;
    }

    public void setDistribucionAulasCicloPeriodosEscolaresList(List<DistribucionAulasCicloPeriodosEscolares> distribucionAulasCicloPeriodosEscolaresList) {
        this.distribucionAulasCicloPeriodosEscolaresList = distribucionAulasCicloPeriodosEscolaresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aulatipo != null ? aulatipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AulasTipo)) {
            return false;
        }
        AulasTipo other = (AulasTipo) object;
        if ((this.aulatipo == null && other.aulatipo != null) || (this.aulatipo != null && !this.aulatipo.equals(other.aulatipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.AulasTipo[ aulatipo=" + aulatipo + " ]";
    }
    
}
