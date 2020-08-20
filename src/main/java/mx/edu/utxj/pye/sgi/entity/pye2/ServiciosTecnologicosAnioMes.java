/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "servicios_tecnologicos_anio_mes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosTecnologicosAnioMes.findAll", query = "SELECT s FROM ServiciosTecnologicosAnioMes s")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByRegistro", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.registro = :registro")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByServicio", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.servicio = :servicio")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByNombre", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.nombre = :nombre")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByFechaInicio", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByFechaTermino", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.fechaTermino = :fechaTermino")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByDuracion", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.duracion = :duracion")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByMontoIngresado", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.montoIngresado = :montoIngresado")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByFacilitador", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.facilitador = :facilitador")
    , @NamedQuery(name = "ServiciosTecnologicosAnioMes.findByServicioDemandado", query = "SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.servicioDemandado = :servicioDemandado")})
public class ServiciosTecnologicosAnioMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "servicio")
    private String servicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_termino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion")
    private int duracion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_ingresado")
    private BigDecimal montoIngresado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "facilitador")
    private String facilitador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "servicio_demandado")
    private String servicioDemandado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioTecnologico", fetch = FetchType.LAZY)
    private List<ServiciosTecnologicosParticipantes> serviciosTecnologicosParticipantesList;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;
    @JoinColumn(name = "servicio_tipo", referencedColumnName = "servtipo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ServiciosTipos servicioTipo;

    public ServiciosTecnologicosAnioMes() {
    }

    public ServiciosTecnologicosAnioMes(Integer registro) {
        this.registro = registro;
    }

    public ServiciosTecnologicosAnioMes(Integer registro, String servicio, String nombre, Date fechaInicio, Date fechaTermino, int duracion, BigDecimal montoIngresado, String facilitador, String servicioDemandado) {
        this.registro = registro;
        this.servicio = servicio;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.duracion = duracion;
        this.montoIngresado = montoIngresado;
        this.facilitador = facilitador;
        this.servicioDemandado = servicioDemandado;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
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

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getMontoIngresado() {
        return montoIngresado;
    }

    public void setMontoIngresado(BigDecimal montoIngresado) {
        this.montoIngresado = montoIngresado;
    }

    public String getFacilitador() {
        return facilitador;
    }

    public void setFacilitador(String facilitador) {
        this.facilitador = facilitador;
    }

    public String getServicioDemandado() {
        return servicioDemandado;
    }

    public void setServicioDemandado(String servicioDemandado) {
        this.servicioDemandado = servicioDemandado;
    }

    @XmlTransient
    public List<ServiciosTecnologicosParticipantes> getServiciosTecnologicosParticipantesList() {
        return serviciosTecnologicosParticipantesList;
    }

    public void setServiciosTecnologicosParticipantesList(List<ServiciosTecnologicosParticipantes> serviciosTecnologicosParticipantesList) {
        this.serviciosTecnologicosParticipantesList = serviciosTecnologicosParticipantesList;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public ServiciosTipos getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServiciosTipos servicioTipo) {
        this.servicioTipo = servicioTipo;
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
        if (!(object instanceof ServiciosTecnologicosAnioMes)) {
            return false;
        }
        ServiciosTecnologicosAnioMes other = (ServiciosTecnologicosAnioMes) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes[ registro=" + registro + " ]";
    }
    
}
