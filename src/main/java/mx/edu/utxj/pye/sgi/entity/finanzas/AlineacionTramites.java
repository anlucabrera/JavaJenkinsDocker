/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "alineacion_tramites", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlineacionTramites.findAll", query = "SELECT a FROM AlineacionTramites a")
    , @NamedQuery(name = "AlineacionTramites.findByTramite", query = "SELECT a FROM AlineacionTramites a WHERE a.tramite = :tramite")
    , @NamedQuery(name = "AlineacionTramites.findByArea", query = "SELECT a FROM AlineacionTramites a WHERE a.area = :area")
    , @NamedQuery(name = "AlineacionTramites.findByEje", query = "SELECT a FROM AlineacionTramites a WHERE a.eje = :eje")
    , @NamedQuery(name = "AlineacionTramites.findByEstrategia", query = "SELECT a FROM AlineacionTramites a WHERE a.estrategia = :estrategia")
    , @NamedQuery(name = "AlineacionTramites.findByLineaAccion", query = "SELECT a FROM AlineacionTramites a WHERE a.lineaAccion = :lineaAccion")
    , @NamedQuery(name = "AlineacionTramites.findByActividad", query = "SELECT a FROM AlineacionTramites a WHERE a.actividad = :actividad")})
public class AlineacionTramites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tramite")
    private Integer tramite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eje")
    private int eje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estrategia")
    private short estrategia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "linea_accion")
    private short lineaAccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actividad")
    private int actividad;
    @JoinColumn(name = "tramite", referencedColumnName = "tramite", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Tramites tramites;

    public AlineacionTramites() {
    }

    public AlineacionTramites(Integer tramite) {
        this.tramite = tramite;
    }

    public AlineacionTramites(Integer tramite, short area, int eje, short estrategia, short lineaAccion, int actividad) {
        this.tramite = tramite;
        this.area = area;
        this.eje = eje;
        this.estrategia = estrategia;
        this.lineaAccion = lineaAccion;
        this.actividad = actividad;
    }

    public Integer getTramite() {
        return tramite;
    }

    public void setTramite(Integer tramite) {
        this.tramite = tramite;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public int getEje() {
        return eje;
    }

    public void setEje(int eje) {
        this.eje = eje;
    }

    public short getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(short estrategia) {
        this.estrategia = estrategia;
    }

    public short getLineaAccion() {
        return lineaAccion;
    }

    public void setLineaAccion(short lineaAccion) {
        this.lineaAccion = lineaAccion;
    }

    public int getActividad() {
        return actividad;
    }

    public void setActividad(int actividad) {
        this.actividad = actividad;
    }

    public Tramites getTramites() {
        return tramites;
    }

    public void setTramites(Tramites tramites) {
        this.tramites = tramites;
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
        if (!(object instanceof AlineacionTramites)) {
            return false;
        }
        AlineacionTramites other = (AlineacionTramites) object;
        if ((this.tramite == null && other.tramite != null) || (this.tramite != null && !this.tramite.equals(other.tramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.AlineacionTramites[ tramite=" + tramite + " ]";
    }
    
}
