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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "datos_laborales", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosLaborales.findAll", query = "SELECT d FROM DatosLaborales d")
    , @NamedQuery(name = "DatosLaborales.findByIdDatosLaborales", query = "SELECT d FROM DatosLaborales d WHERE d.idDatosLaborales = :idDatosLaborales")
    , @NamedQuery(name = "DatosLaborales.findByTrabajoActual", query = "SELECT d FROM DatosLaborales d WHERE d.trabajoActual = :trabajoActual")
    , @NamedQuery(name = "DatosLaborales.findByNombreEmpresa", query = "SELECT d FROM DatosLaborales d WHERE d.nombreEmpresa = :nombreEmpresa")
    , @NamedQuery(name = "DatosLaborales.findByPuesto", query = "SELECT d FROM DatosLaborales d WHERE d.puesto = :puesto")
    , @NamedQuery(name = "DatosLaborales.findByCalle", query = "SELECT d FROM DatosLaborales d WHERE d.calle = :calle")
    , @NamedQuery(name = "DatosLaborales.findByNumero", query = "SELECT d FROM DatosLaborales d WHERE d.numero = :numero")
    , @NamedQuery(name = "DatosLaborales.findByEstado", query = "SELECT d FROM DatosLaborales d WHERE d.estado = :estado")
    , @NamedQuery(name = "DatosLaborales.findByMunicipio", query = "SELECT d FROM DatosLaborales d WHERE d.municipio = :municipio")
    , @NamedQuery(name = "DatosLaborales.findByAsentamiento", query = "SELECT d FROM DatosLaborales d WHERE d.asentamiento = :asentamiento")
    , @NamedQuery(name = "DatosLaborales.findByTelefonoEmpresa", query = "SELECT d FROM DatosLaborales d WHERE d.telefonoEmpresa = :telefonoEmpresa")
    , @NamedQuery(name = "DatosLaborales.findByRazonDeTrabajo", query = "SELECT d FROM DatosLaborales d WHERE d.razonDeTrabajo = :razonDeTrabajo")
    , @NamedQuery(name = "DatosLaborales.findByRelacionTrabajoCarrera", query = "SELECT d FROM DatosLaborales d WHERE d.relacionTrabajoCarrera = :relacionTrabajoCarrera")
    , @NamedQuery(name = "DatosLaborales.findByIngresoMensual", query = "SELECT d FROM DatosLaborales d WHERE d.ingresoMensual = :ingresoMensual")})
public class DatosLaborales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_datos_laborales")
    private Integer idDatosLaborales;
    @Column(name = "trabajo_actual")
    private Boolean trabajoActual;
    @Size(max = 255)
    @Column(name = "nombre_empresa")
    private String nombreEmpresa;
    @Size(max = 255)
    @Column(name = "puesto")
    private String puesto;
    @Size(max = 150)
    @Column(name = "calle")
    private String calle;
    @Size(max = 10)
    @Column(name = "numero")
    private String numero;
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
    @Column(name = "asentamiento")
    private int asentamiento;
    @Size(max = 20)
    @Column(name = "telefono_empresa")
    private String telefonoEmpresa;
    @Size(max = 500)
    @Column(name = "razon_de_trabajo")
    private String razonDeTrabajo;
    @Column(name = "relacion_trabajo_carrera")
    private Boolean relacionTrabajoCarrera;
    @Column(name = "ingresoMensual")
    private Integer ingresoMensual;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante")
    @ManyToOne
    private Aspirante aspirante;

    public DatosLaborales() {
    }

    public DatosLaborales(Integer idDatosLaborales) {
        this.idDatosLaborales = idDatosLaborales;
    }

    public DatosLaborales(Integer idDatosLaborales, int estado, int municipio, int asentamiento) {
        this.idDatosLaborales = idDatosLaborales;
        this.estado = estado;
        this.municipio = municipio;
        this.asentamiento = asentamiento;
    }

    public Integer getIdDatosLaborales() {
        return idDatosLaborales;
    }

    public void setIdDatosLaborales(Integer idDatosLaborales) {
        this.idDatosLaborales = idDatosLaborales;
    }

    public Boolean getTrabajoActual() {
        return trabajoActual;
    }

    public void setTrabajoActual(Boolean trabajoActual) {
        this.trabajoActual = trabajoActual;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public int getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(int asentamiento) {
        this.asentamiento = asentamiento;
    }

    public String getTelefonoEmpresa() {
        return telefonoEmpresa;
    }

    public void setTelefonoEmpresa(String telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }

    public String getRazonDeTrabajo() {
        return razonDeTrabajo;
    }

    public void setRazonDeTrabajo(String razonDeTrabajo) {
        this.razonDeTrabajo = razonDeTrabajo;
    }

    public Boolean getRelacionTrabajoCarrera() {
        return relacionTrabajoCarrera;
    }

    public void setRelacionTrabajoCarrera(Boolean relacionTrabajoCarrera) {
        this.relacionTrabajoCarrera = relacionTrabajoCarrera;
    }

    public Integer getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(Integer ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDatosLaborales != null ? idDatosLaborales.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosLaborales)) {
            return false;
        }
        DatosLaborales other = (DatosLaborales) object;
        if ((this.idDatosLaborales == null && other.idDatosLaborales != null) || (this.idDatosLaborales != null && !this.idDatosLaborales.equals(other.idDatosLaborales))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosLaborales[ idDatosLaborales=" + idDatosLaborales + " ]";
    }
    
}
