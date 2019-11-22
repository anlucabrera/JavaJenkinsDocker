/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar.view;

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
@Table(name = "listaindicadoresporcriterioporconfiguracion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findAll", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByClave", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.clave = :clave")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByConfiguracion", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.configuracion = :configuracion")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByPeriodo", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByCargaAcademica", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.cargaAcademica = :cargaAcademica")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByNombreUnidad", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.nombreUnidad = :nombreUnidad")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByUnidad", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.unidad = :unidad")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByClaveCriterio", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.claveCriterio = :claveCriterio")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByCriterio", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.criterio = :criterio")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByPorcentaje", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.porcentaje = :porcentaje")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByClaveIndicador", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.claveIndicador = :claveIndicador")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByIndicador", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.indicador = :indicador")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByPorcentajeIndicador", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.porcentajeIndicador = :porcentajeIndicador")})
public class Listaindicadoresporcriterioporconfiguracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 44)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion")
    private int configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cargaAcademica")
    private int cargaAcademica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombreUnidad")
    private String nombreUnidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unidad")
    private int unidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "claveCriterio")
    private int claveCriterio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "criterio")
    private String criterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "claveIndicador")
    private int claveIndicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "indicador")
    private String indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentajeIndicador")
    private double porcentajeIndicador;

    public Listaindicadoresporcriterioporconfiguracion() {
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(int configuracion) {
        this.configuracion = configuracion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(int cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public int getClaveCriterio() {
        return claveCriterio;
    }

    public void setClaveCriterio(int claveCriterio) {
        this.claveCriterio = claveCriterio;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getClaveIndicador() {
        return claveIndicador;
    }

    public void setClaveIndicador(int claveIndicador) {
        this.claveIndicador = claveIndicador;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public double getPorcentajeIndicador() {
        return porcentajeIndicador;
    }

    public void setPorcentajeIndicador(double porcentajeIndicador) {
        this.porcentajeIndicador = porcentajeIndicador;
    }
    
}
