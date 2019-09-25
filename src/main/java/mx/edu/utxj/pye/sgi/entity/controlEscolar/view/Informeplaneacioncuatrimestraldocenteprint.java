/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "informeplaneacioncuatrimestraldocenteprint", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findAll", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByConfiguracionDetalle", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.configuracionDetalle = :configuracionDetalle")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByCarga", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.carga = :carga")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByConfiguracion", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.configuracion = :configuracion")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByUnidad", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.unidad = :unidad")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByNombreUnidad", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.nombreUnidad = :nombreUnidad")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByObjetivo", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.objetivo = :objetivo")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByFechaInicio", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByFechaFin", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.fechaFin = :fechaFin")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByPorUnidad", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.porUnidad = :porUnidad")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByIdCriterio", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.idCriterio = :idCriterio")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByCriterio", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.criterio = :criterio")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByPorCriterio", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.porCriterio = :porCriterio")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByIdIndicador", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.idIndicador = :idIndicador")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByIndicador", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.indicador = :indicador")
    , @NamedQuery(name = "Informeplaneacioncuatrimestraldocenteprint.findByPorcentaje", query = "SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.porcentaje = :porcentaje")})
public class Informeplaneacioncuatrimestraldocenteprint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracionDetalle")
    private long configuracionDetalle;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porUnidad")
    private double porUnidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCriterio")
    private int idCriterio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "criterio")
    private String criterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porCriterio")
    private double porCriterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idIndicador")
    private int idIndicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "indicador")
    private String indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;

    public Informeplaneacioncuatrimestraldocenteprint() {
    }

    public long getConfiguracionDetalle() {
        return configuracionDetalle;
    }

    public void setConfiguracionDetalle(long configuracionDetalle) {
        this.configuracionDetalle = configuracionDetalle;
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

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getPorUnidad() {
        return porUnidad;
    }

    public void setPorUnidad(double porUnidad) {
        this.porUnidad = porUnidad;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public double getPorCriterio() {
        return porCriterio;
    }

    public void setPorCriterio(double porCriterio) {
        this.porCriterio = porCriterio;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
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
