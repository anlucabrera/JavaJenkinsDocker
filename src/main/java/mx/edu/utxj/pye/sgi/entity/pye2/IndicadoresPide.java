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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "indicadores_pide", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadoresPide.findAll", query = "SELECT i FROM IndicadoresPide i")
    , @NamedQuery(name = "IndicadoresPide.findByIndicador", query = "SELECT i FROM IndicadoresPide i WHERE i.indicador = :indicador")
    , @NamedQuery(name = "IndicadoresPide.findByNombre", query = "SELECT i FROM IndicadoresPide i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "IndicadoresPide.findByFormula", query = "SELECT i FROM IndicadoresPide i WHERE i.formula = :formula")
    , @NamedQuery(name = "IndicadoresPide.findByFrecuencia", query = "SELECT i FROM IndicadoresPide i WHERE i.frecuencia = :frecuencia")})
public class IndicadoresPide implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indicador")
    private Integer indicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "formula")
    private String formula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "frecuencia")
    private String frecuencia;
    @JoinTable(name = "pye2.proyecto_indicador", joinColumns = {
        @JoinColumn(name = "indicador", referencedColumnName = "indicador")}, inverseJoinColumns = {
        @JoinColumn(name = "proyecto", referencedColumnName = "proyecto")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Proyectos> proyectosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicadoresPide", fetch = FetchType.LAZY)
    private List<IndicadorPlazo> indicadorPlazoList;
    @JoinColumn(name = "unidad_medida", referencedColumnName = "unidad_medida")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMedidas unidadMedida;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicadoresPide", fetch = FetchType.LAZY)
    private List<FuenteInformacion> fuenteInformacionList;

    public IndicadoresPide() {
    }

    public IndicadoresPide(Integer indicador) {
        this.indicador = indicador;
    }

    public IndicadoresPide(Integer indicador, String nombre, String formula, String frecuencia) {
        this.indicador = indicador;
        this.nombre = nombre;
        this.formula = formula;
        this.frecuencia = frecuencia;
    }

    public Integer getIndicador() {
        return indicador;
    }

    public void setIndicador(Integer indicador) {
        this.indicador = indicador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    @XmlTransient
    public List<Proyectos> getProyectosList() {
        return proyectosList;
    }

    public void setProyectosList(List<Proyectos> proyectosList) {
        this.proyectosList = proyectosList;
    }

    @XmlTransient
    public List<IndicadorPlazo> getIndicadorPlazoList() {
        return indicadorPlazoList;
    }

    public void setIndicadorPlazoList(List<IndicadorPlazo> indicadorPlazoList) {
        this.indicadorPlazoList = indicadorPlazoList;
    }

    public UnidadMedidas getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedidas unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @XmlTransient
    public List<FuenteInformacion> getFuenteInformacionList() {
        return fuenteInformacionList;
    }

    public void setFuenteInformacionList(List<FuenteInformacion> fuenteInformacionList) {
        this.fuenteInformacionList = fuenteInformacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicador != null ? indicador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadoresPide)) {
            return false;
        }
        IndicadoresPide other = (IndicadoresPide) object;
        if ((this.indicador == null && other.indicador != null) || (this.indicador != null && !this.indicador.equals(other.indicador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.IndicadoresPide[ indicador=" + indicador + " ]";
    }
    
}
