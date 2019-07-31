/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "listaasignacionindicadorescargaacademica", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findAll", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByCarga", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.carga = :carga")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByConfiguracion", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.configuracion = :configuracion")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByUnidad", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.unidad = :unidad")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByNombreUnidad", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.nombreUnidad = :nombreUnidad")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByConfiguracionDetalle", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.configuracionDetalle = :configuracionDetalle")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByCriterio", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.criterio = :criterio")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByIndicador", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.indicador = :indicador")
    , @NamedQuery(name = "Listaasignacionindicadorescargaacademica.findByPorcentaje", query = "SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.porcentaje = :porcentaje")})
public class Listaasignacionindicadorescargaacademica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carga")
    private int carga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion")
    private int configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unidad")
    private int unidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombreUnidad")
    private String nombreUnidad;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracionDetalle")
    private long configuracionDetalle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "criterio")
    private String criterio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "indicador")
    private String indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;

    public Listaasignacionindicadorescargaacademica() {
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public int getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(int configuracion) {
        this.configuracion = configuracion;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public long getConfiguracionDetalle() {
        return configuracionDetalle;
    }

    public void setConfiguracionDetalle(long configuracionDetalle) {
        this.configuracionDetalle = configuracionDetalle;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    
}
