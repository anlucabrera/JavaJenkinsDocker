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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "actividades_varias_registro", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadesVariasRegistro.findAll", query = "SELECT a FROM ActividadesVariasRegistro a")
    , @NamedQuery(name = "ActividadesVariasRegistro.findByRegistro", query = "SELECT a FROM ActividadesVariasRegistro a WHERE a.registro = :registro")
    , @NamedQuery(name = "ActividadesVariasRegistro.findByFechaInicio", query = "SELECT a FROM ActividadesVariasRegistro a WHERE a.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ActividadesVariasRegistro.findByFechaFin", query = "SELECT a FROM ActividadesVariasRegistro a WHERE a.fechaFin = :fechaFin")
    , @NamedQuery(name = "ActividadesVariasRegistro.findByTotalHombres", query = "SELECT a FROM ActividadesVariasRegistro a WHERE a.totalHombres = :totalHombres")
    , @NamedQuery(name = "ActividadesVariasRegistro.findByTotalMujeres", query = "SELECT a FROM ActividadesVariasRegistro a WHERE a.totalMujeres = :totalMujeres")})
public class ActividadesVariasRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "nombre")
    private String nombre;
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
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "impacto_beneficio")
    private String impactoBeneficio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_hombres")
    private int totalHombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_mujeres")
    private int totalMujeres;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "personalidades")
    private String personalidades;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public ActividadesVariasRegistro() {
    }

    public ActividadesVariasRegistro(Integer registro) {
        this.registro = registro;
    }

    public ActividadesVariasRegistro(Integer registro, String nombre, Date fechaInicio, Date fechaFin, String lugar, String objetivo, String impactoBeneficio, int totalHombres, int totalMujeres, String personalidades) {
        this.registro = registro;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.objetivo = objetivo;
        this.impactoBeneficio = impactoBeneficio;
        this.totalHombres = totalHombres;
        this.totalMujeres = totalMujeres;
        this.personalidades = personalidades;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getImpactoBeneficio() {
        return impactoBeneficio;
    }

    public void setImpactoBeneficio(String impactoBeneficio) {
        this.impactoBeneficio = impactoBeneficio;
    }

    public int getTotalHombres() {
        return totalHombres;
    }

    public void setTotalHombres(int totalHombres) {
        this.totalHombres = totalHombres;
    }

    public int getTotalMujeres() {
        return totalMujeres;
    }

    public void setTotalMujeres(int totalMujeres) {
        this.totalMujeres = totalMujeres;
    }

    public String getPersonalidades() {
        return personalidades;
    }

    public void setPersonalidades(String personalidades) {
        this.personalidades = personalidades;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
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
        if (!(object instanceof ActividadesVariasRegistro)) {
            return false;
        }
        ActividadesVariasRegistro other = (ActividadesVariasRegistro) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro[ registro=" + registro + " ]";
    }
    
}
