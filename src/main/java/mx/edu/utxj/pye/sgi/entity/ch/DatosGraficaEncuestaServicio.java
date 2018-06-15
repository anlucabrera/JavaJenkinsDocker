/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author Planeación
 */
@Entity
@Table(name = "datos_grafica_encuesta_servicio", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosGraficaEncuestaServicio.findAll", query = "SELECT d FROM DatosGraficaEncuestaServicio d")
    , @NamedQuery(name = "DatosGraficaEncuestaServicio.findBySiglas", query = "SELECT d FROM DatosGraficaEncuestaServicio d WHERE d.siglas = :siglas")
    , @NamedQuery(name = "DatosGraficaEncuestaServicio.findByClave", query = "SELECT d FROM DatosGraficaEncuestaServicio d WHERE d.clave = :clave")
    , @NamedQuery(name = "DatosGraficaEncuestaServicio.findByTotalEncuestadosPorcarrera", query = "SELECT d FROM DatosGraficaEncuestaServicio d WHERE d.totalEncuestadosPorcarrera = :totalEncuestadosPorcarrera")
    , @NamedQuery(name = "DatosGraficaEncuestaServicio.findByTotalPorCarrera", query = "SELECT d FROM DatosGraficaEncuestaServicio d WHERE d.totalPorCarrera = :totalPorCarrera")
    , @NamedQuery(name = "DatosGraficaEncuestaServicio.findByPorcentaje", query = "SELECT d FROM DatosGraficaEncuestaServicio d WHERE d.porcentaje = :porcentaje")
    , @NamedQuery(name = "DatosGraficaEncuestaServicio.findByFaltantesPorContestar", query = "SELECT d FROM DatosGraficaEncuestaServicio d WHERE d.faltantesPorContestar = :faltantesPorContestar")})
public class DatosGraficaEncuestaServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 10)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;
    @Size(max = 15)
    @Column(name = "total_encuestados_porcarrera")
    private String totalEncuestadosPorcarrera;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_por_carrera")
    private long totalPorCarrera;
    @Size(max = 25)
    @Column(name = "porcentaje")
    private String porcentaje;
    @Size(max = 22)
    @Column(name = "faltantes_por_contestar")
    private String faltantesPorContestar;

    public DatosGraficaEncuestaServicio() {
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getTotalEncuestadosPorcarrera() {
        return totalEncuestadosPorcarrera;
    }

    public void setTotalEncuestadosPorcarrera(String totalEncuestadosPorcarrera) {
        this.totalEncuestadosPorcarrera = totalEncuestadosPorcarrera;
    }

    public long getTotalPorCarrera() {
        return totalPorCarrera;
    }

    public void setTotalPorCarrera(long totalPorCarrera) {
        this.totalPorCarrera = totalPorCarrera;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getFaltantesPorContestar() {
        return faltantesPorContestar;
    }

    public void setFaltantesPorContestar(String faltantesPorContestar) {
        this.faltantesPorContestar = faltantesPorContestar;
    }
    
}
