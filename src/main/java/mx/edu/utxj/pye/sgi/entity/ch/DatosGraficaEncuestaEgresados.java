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
@Table(name = "datos_grafica_encuesta_egresados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosGraficaEncuestaEgresados.findAll", query = "SELECT d FROM DatosGraficaEncuestaEgresados d")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findByClave", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.clave = :clave")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findByEstatus", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.estatus = :estatus")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findBySiglas", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.siglas = :siglas")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findByTotalPorCarrera", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.totalPorCarrera = :totalPorCarrera")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findByTotalEncuestados", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.totalEncuestados = :totalEncuestados")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findByPorcentaje", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.porcentaje = :porcentaje")
    , @NamedQuery(name = "DatosGraficaEncuestaEgresados.findByFaltantesDeContestar", query = "SELECT d FROM DatosGraficaEncuestaEgresados d WHERE d.faltantesDeContestar = :faltantesDeContestar")})
public class DatosGraficaEncuestaEgresados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    
    private int clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 10)
    @Id
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_por_carrera")
    private long totalPorCarrera;
    @Size(max = 6)
    @Column(name = "totalEncuestados")
    private String totalEncuestados;
    @Size(max = 25)
    @Column(name = "porcentaje")
    private String porcentaje;
    @Size(max = 22)
    @Column(name = "faltantes_de_contestar")
    private String faltantesDeContestar;

    public DatosGraficaEncuestaEgresados() {
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public long getTotalPorCarrera() {
        return totalPorCarrera;
    }

    public void setTotalPorCarrera(long totalPorCarrera) {
        this.totalPorCarrera = totalPorCarrera;
    }

    public String getTotalEncuestados() {
        return totalEncuestados;
    }

    public void setTotalEncuestados(String totalEncuestados) {
        this.totalEncuestados = totalEncuestados;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getFaltantesDeContestar() {
        return faltantesDeContestar;
    }

    public void setFaltantesDeContestar(String faltantesDeContestar) {
        this.faltantesDeContestar = faltantesDeContestar;
    }
    
}
