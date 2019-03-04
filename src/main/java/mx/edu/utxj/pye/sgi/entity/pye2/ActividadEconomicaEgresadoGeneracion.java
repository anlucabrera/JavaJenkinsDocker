/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "actividad_economica_egresado_generacion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findAll", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a")
    , @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findByRegistro", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a WHERE a.registro = :registro")
    , @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findByFecha", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findByGeneracion", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a WHERE a.generacion = :generacion")
    , @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findByProgramaEducativo", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a WHERE a.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findByCantSector", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a WHERE a.cantSector = :cantSector")
    , @NamedQuery(name = "ActividadEconomicaEgresadoGeneracion.findByCantGiro", query = "SELECT a FROM ActividadEconomicaEgresadoGeneracion a WHERE a.cantGiro = :cantGiro")})
public class ActividadEconomicaEgresadoGeneracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_sector")
    private int cantSector;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cant_giro")
    private int cantGiro;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "sector", referencedColumnName = "sector")
    @ManyToOne(optional = false)
    private SectoresTipo sector;
    @JoinColumn(name = "giro", referencedColumnName = "giro")
    @ManyToOne(optional = false)
    private GirosTipo giro;

    public ActividadEconomicaEgresadoGeneracion() {
    }

    public ActividadEconomicaEgresadoGeneracion(Integer registro) {
        this.registro = registro;
    }

    public ActividadEconomicaEgresadoGeneracion(Integer registro, Date fecha, short generacion, short programaEducativo, int cantSector, int cantGiro) {
        this.registro = registro;
        this.fecha = fecha;
        this.generacion = generacion;
        this.programaEducativo = programaEducativo;
        this.cantSector = cantSector;
        this.cantGiro = cantGiro;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getCantSector() {
        return cantSector;
    }

    public void setCantSector(int cantSector) {
        this.cantSector = cantSector;
    }

    public int getCantGiro() {
        return cantGiro;
    }

    public void setCantGiro(int cantGiro) {
        this.cantGiro = cantGiro;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public SectoresTipo getSector() {
        return sector;
    }

    public void setSector(SectoresTipo sector) {
        this.sector = sector;
    }

    public GirosTipo getGiro() {
        return giro;
    }

    public void setGiro(GirosTipo giro) {
        this.giro = giro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadEconomicaEgresadoGeneracion)) {
            return false;
        }
        ActividadEconomicaEgresadoGeneracion other = (ActividadEconomicaEgresadoGeneracion) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion[ registro=" + registro + " ]";
    }
    
}
