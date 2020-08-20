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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "generaciones", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Generaciones.findAll", query = "SELECT g FROM Generaciones g")
    , @NamedQuery(name = "Generaciones.findByGeneracion", query = "SELECT g FROM Generaciones g WHERE g.generacion = :generacion")
    , @NamedQuery(name = "Generaciones.findByInicio", query = "SELECT g FROM Generaciones g WHERE g.inicio = :inicio")
    , @NamedQuery(name = "Generaciones.findByFin", query = "SELECT g FROM Generaciones g WHERE g.fin = :fin")})
public class Generaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "generacion")
    private Short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    private Short inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    private Short fin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion", fetch = FetchType.LAZY)
    private List<NivelOcupacion> nivelOcupacionList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "generaciones", fetch = FetchType.LAZY)
    private ResultadosEgetsu resultadosEgetsu;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion", fetch = FetchType.LAZY)
    private List<ActividadEgresado> actividadEgresadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion", fetch = FetchType.LAZY)
    private List<BolsaTrabajoContratados> bolsaTrabajoContratadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generaciones", fetch = FetchType.LAZY)
    private List<EficienciaTerminalTasaTitulacion> eficienciaTerminalTasaTitulacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion", fetch = FetchType.LAZY)
    private List<DesercionPorEstudiante> desercionPorEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion", fetch = FetchType.LAZY)
    private List<NivelIngreso> nivelIngresoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generacion", fetch = FetchType.LAZY)
    private List<CiclosescolaresGeneraciones> ciclosescolaresGeneracionesList;

    public Generaciones() {
    }

    public Generaciones(Short generacion) {
        this.generacion = generacion;
    }

    public Generaciones(Short generacion, Short inicio, Short fin) {
        this.generacion = generacion;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Short generacion) {
        this.generacion = generacion;
    }

    public Short getInicio() {
        return inicio;
    }

    public void setInicio(Short inicio) {
        this.inicio = inicio;
    }

    public Short getFin() {
        return fin;
    }

    public void setFin(Short fin) {
        this.fin = fin;
    }

    @XmlTransient
    public List<NivelOcupacion> getNivelOcupacionList() {
        return nivelOcupacionList;
    }

    public void setNivelOcupacionList(List<NivelOcupacion> nivelOcupacionList) {
        this.nivelOcupacionList = nivelOcupacionList;
    }

    public ResultadosEgetsu getResultadosEgetsu() {
        return resultadosEgetsu;
    }

    public void setResultadosEgetsu(ResultadosEgetsu resultadosEgetsu) {
        this.resultadosEgetsu = resultadosEgetsu;
    }

    @XmlTransient
    public List<ActividadEgresado> getActividadEgresadoList() {
        return actividadEgresadoList;
    }

    public void setActividadEgresadoList(List<ActividadEgresado> actividadEgresadoList) {
        this.actividadEgresadoList = actividadEgresadoList;
    }

    @XmlTransient
    public List<BolsaTrabajoContratados> getBolsaTrabajoContratadosList() {
        return bolsaTrabajoContratadosList;
    }

    public void setBolsaTrabajoContratadosList(List<BolsaTrabajoContratados> bolsaTrabajoContratadosList) {
        this.bolsaTrabajoContratadosList = bolsaTrabajoContratadosList;
    }

    @XmlTransient
    public List<EficienciaTerminalTasaTitulacion> getEficienciaTerminalTasaTitulacionList() {
        return eficienciaTerminalTasaTitulacionList;
    }

    public void setEficienciaTerminalTasaTitulacionList(List<EficienciaTerminalTasaTitulacion> eficienciaTerminalTasaTitulacionList) {
        this.eficienciaTerminalTasaTitulacionList = eficienciaTerminalTasaTitulacionList;
    }

    @XmlTransient
    public List<DesercionPorEstudiante> getDesercionPorEstudianteList() {
        return desercionPorEstudianteList;
    }

    public void setDesercionPorEstudianteList(List<DesercionPorEstudiante> desercionPorEstudianteList) {
        this.desercionPorEstudianteList = desercionPorEstudianteList;
    }

    @XmlTransient
    public List<NivelIngreso> getNivelIngresoList() {
        return nivelIngresoList;
    }

    public void setNivelIngresoList(List<NivelIngreso> nivelIngresoList) {
        this.nivelIngresoList = nivelIngresoList;
    }

    @XmlTransient
    public List<CiclosescolaresGeneraciones> getCiclosescolaresGeneracionesList() {
        return ciclosescolaresGeneracionesList;
    }

    public void setCiclosescolaresGeneracionesList(List<CiclosescolaresGeneraciones> ciclosescolaresGeneracionesList) {
        this.ciclosescolaresGeneracionesList = ciclosescolaresGeneracionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (generacion != null ? generacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Generaciones)) {
            return false;
        }
        Generaciones other = (Generaciones) object;
        if ((this.generacion == null && other.generacion != null) || (this.generacion != null && !this.generacion.equals(other.generacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones[ generacion=" + generacion + " ]";
    }
    
}
