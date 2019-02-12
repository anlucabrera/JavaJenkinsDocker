/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "comision_oficios", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisionOficios.findAll", query = "SELECT c FROM ComisionOficios c")
    , @NamedQuery(name = "ComisionOficios.findByTramite", query = "SELECT c FROM ComisionOficios c WHERE c.tramite = :tramite")
    , @NamedQuery(name = "ComisionOficios.findByOficio", query = "SELECT c FROM ComisionOficios c WHERE c.oficio = :oficio")
    , @NamedQuery(name = "ComisionOficios.findByArea", query = "SELECT c FROM ComisionOficios c WHERE c.area = :area")
    , @NamedQuery(name = "ComisionOficios.findByComisionado", query = "SELECT c FROM ComisionOficios c WHERE c.comisionado = :comisionado")
    , @NamedQuery(name = "ComisionOficios.findByActividades", query = "SELECT c FROM ComisionOficios c WHERE c.actividades = :actividades")
    , @NamedQuery(name = "ComisionOficios.findByDependencia", query = "SELECT c FROM ComisionOficios c WHERE c.dependencia = :dependencia")
    , @NamedQuery(name = "ComisionOficios.findByEstado", query = "SELECT c FROM ComisionOficios c WHERE c.estado = :estado")
    , @NamedQuery(name = "ComisionOficios.findByMunicipio", query = "SELECT c FROM ComisionOficios c WHERE c.municipio = :municipio")
    , @NamedQuery(name = "ComisionOficios.findByFechaComisionInicio", query = "SELECT c FROM ComisionOficios c WHERE c.fechaComisionInicio = :fechaComisionInicio")
    , @NamedQuery(name = "ComisionOficios.findByFechaComisionFin", query = "SELECT c FROM ComisionOficios c WHERE c.fechaComisionFin = :fechaComisionFin")
    , @NamedQuery(name = "ComisionOficios.findByFechaGeneracion", query = "SELECT c FROM ComisionOficios c WHERE c.fechaGeneracion = :fechaGeneracion")
    , @NamedQuery(name = "ComisionOficios.findByVistoBueno", query = "SELECT c FROM ComisionOficios c WHERE c.vistoBueno = :vistoBueno")
    , @NamedQuery(name = "ComisionOficios.findBySuperior", query = "SELECT c FROM ComisionOficios c WHERE c.superior = :superior")
    , @NamedQuery(name = "ComisionOficios.findByGenerador", query = "SELECT c FROM ComisionOficios c WHERE c.generador = :generador")
    , @NamedQuery(name = "ComisionOficios.findByEstatus", query = "SELECT c FROM ComisionOficios c WHERE c.estatus = :estatus")
    , @NamedQuery(name = "ComisionOficios.findByObservaciones", query = "SELECT c FROM ComisionOficios c WHERE c.observaciones = :observaciones")
    , @NamedQuery(name = "ComisionOficios.findByRuta", query = "SELECT c FROM ComisionOficios c WHERE c.ruta = :ruta")
    , @NamedQuery(name = "ComisionOficios.findBySabadosSolicitados", query = "SELECT c FROM ComisionOficios c WHERE c.sabadosSolicitados = :sabadosSolicitados")
    , @NamedQuery(name = "ComisionOficios.findBySabadosAutorizados", query = "SELECT c FROM ComisionOficios c WHERE c.sabadosAutorizados = :sabadosAutorizados")
    , @NamedQuery(name = "ComisionOficios.findByDomingosSolicitados", query = "SELECT c FROM ComisionOficios c WHERE c.domingosSolicitados = :domingosSolicitados")
    , @NamedQuery(name = "ComisionOficios.findByDomingosAutorizados", query = "SELECT c FROM ComisionOficios c WHERE c.domingosAutorizados = :domingosAutorizados")})
public class ComisionOficios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tramite")
    private Integer tramite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "oficio")
    private String oficio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "comisionado")
    private int comisionado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "actividades")
    private String actividades;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "dependencia")
    private String dependencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_comision_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaComisionInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_comision_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaComisionFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_generacion")
    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;
    @Column(name = "visto_bueno")
    private Integer vistoBueno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "superior")
    private int superior;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generador")
    private int generador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 41)
    @Column(name = "estatus")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sabados_solicitados")
    private boolean sabadosSolicitados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sabados_autorizados")
    private boolean sabadosAutorizados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "domingos_solicitados")
    private boolean domingosSolicitados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "domingos_autorizados")
    private boolean domingosAutorizados;
    @JoinColumn(name = "tramite", referencedColumnName = "tramite", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Tramites tramites;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "comisionOficios")
    private ComisionAvisos comisionAvisos;

    public ComisionOficios() {
    }

    public ComisionOficios(Integer tramite) {
        this.tramite = tramite;
    }

    public ComisionOficios(Integer tramite, String oficio, short area, int comisionado, String actividades, String dependencia, int estado, int municipio, Date fechaComisionInicio, Date fechaComisionFin, Date fechaGeneracion, int superior, int generador, String estatus, String observaciones, String ruta, boolean sabadosSolicitados, boolean sabadosAutorizados, boolean domingosSolicitados, boolean domingosAutorizados) {
        this.tramite = tramite;
        this.oficio = oficio;
        this.area = area;
        this.comisionado = comisionado;
        this.actividades = actividades;
        this.dependencia = dependencia;
        this.estado = estado;
        this.municipio = municipio;
        this.fechaComisionInicio = fechaComisionInicio;
        this.fechaComisionFin = fechaComisionFin;
        this.fechaGeneracion = fechaGeneracion;
        this.superior = superior;
        this.generador = generador;
        this.estatus = estatus;
        this.observaciones = observaciones;
        this.ruta = ruta;
        this.sabadosSolicitados = sabadosSolicitados;
        this.sabadosAutorizados = sabadosAutorizados;
        this.domingosSolicitados = domingosSolicitados;
        this.domingosAutorizados = domingosAutorizados;
    }

    public Integer getTramite() {
        return tramite;
    }

    public void setTramite(Integer tramite) {
        this.tramite = tramite;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public int getComisionado() {
        return comisionado;
    }

    public void setComisionado(int comisionado) {
        this.comisionado = comisionado;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public String getDependencia() {
        return dependencia;
    }

    public void setDependencia(String dependencia) {
        this.dependencia = dependencia;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    public Date getFechaComisionInicio() {
        return fechaComisionInicio;
    }

    public void setFechaComisionInicio(Date fechaComisionInicio) {
        this.fechaComisionInicio = fechaComisionInicio;
    }

    public Date getFechaComisionFin() {
        return fechaComisionFin;
    }

    public void setFechaComisionFin(Date fechaComisionFin) {
        this.fechaComisionFin = fechaComisionFin;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Integer getVistoBueno() {
        return vistoBueno;
    }

    public void setVistoBueno(Integer vistoBueno) {
        this.vistoBueno = vistoBueno;
    }

    public int getSuperior() {
        return superior;
    }

    public void setSuperior(int superior) {
        this.superior = superior;
    }

    public int getGenerador() {
        return generador;
    }

    public void setGenerador(int generador) {
        this.generador = generador;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean getSabadosSolicitados() {
        return sabadosSolicitados;
    }

    public void setSabadosSolicitados(boolean sabadosSolicitados) {
        this.sabadosSolicitados = sabadosSolicitados;
    }

    public boolean getSabadosAutorizados() {
        return sabadosAutorizados;
    }

    public void setSabadosAutorizados(boolean sabadosAutorizados) {
        this.sabadosAutorizados = sabadosAutorizados;
    }

    public boolean getDomingosSolicitados() {
        return domingosSolicitados;
    }

    public void setDomingosSolicitados(boolean domingosSolicitados) {
        this.domingosSolicitados = domingosSolicitados;
    }

    public boolean getDomingosAutorizados() {
        return domingosAutorizados;
    }

    public void setDomingosAutorizados(boolean domingosAutorizados) {
        this.domingosAutorizados = domingosAutorizados;
    }

    public Tramites getTramites() {
        return tramites;
    }

    public void setTramites(Tramites tramites) {
        this.tramites = tramites;
    }

    public ComisionAvisos getComisionAvisos() {
        return comisionAvisos;
    }

    public void setComisionAvisos(ComisionAvisos comisionAvisos) {
        this.comisionAvisos = comisionAvisos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tramite != null ? tramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionOficios)) {
            return false;
        }
        ComisionOficios other = (ComisionOficios) object;
        if ((this.tramite == null && other.tramite != null) || (this.tramite != null && !this.tramite.equals(other.tramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios[ tramite=" + tramite + " ]";
    }
    
}
