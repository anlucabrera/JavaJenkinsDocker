/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "servicios_tecnologicos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosTecnologicos.findAll", query = "SELECT s FROM ServiciosTecnologicos s")
    , @NamedQuery(name = "ServiciosTecnologicos.findByCiclo", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.serviciosTecnologicosPK.ciclo = :ciclo")
    , @NamedQuery(name = "ServiciosTecnologicos.findByMes", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.serviciosTecnologicosPK.mes = :mes")
    , @NamedQuery(name = "ServiciosTecnologicos.findByClaveServicio", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.serviciosTecnologicosPK.claveServicio = :claveServicio")
    , @NamedQuery(name = "ServiciosTecnologicos.findByTipoServicio", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.tipoServicio = :tipoServicio")
    , @NamedQuery(name = "ServiciosTecnologicos.findByNombreServicio", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.nombreServicio = :nombreServicio")
    , @NamedQuery(name = "ServiciosTecnologicos.findByFechaInicio", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ServiciosTecnologicos.findByFechaFin", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.fechaFin = :fechaFin")
    , @NamedQuery(name = "ServiciosTecnologicos.findByDuracion", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.duracion = :duracion")
    , @NamedQuery(name = "ServiciosTecnologicos.findByParticipantes", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.participantes = :participantes")
    , @NamedQuery(name = "ServiciosTecnologicos.findByMontoIngreso", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.montoIngreso = :montoIngreso")
    , @NamedQuery(name = "ServiciosTecnologicos.findByFacilitador", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.facilitador = :facilitador")
    , @NamedQuery(name = "ServiciosTecnologicos.findByServicioOtorgado", query = "SELECT s FROM ServiciosTecnologicos s WHERE s.servicioOtorgado = :servicioOtorgado")})
public class ServiciosTecnologicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ServiciosTecnologicosPK serviciosTecnologicosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tipo_servicio")
    private String tipoServicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre_servicio")
    private String nombreServicio;
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
    @Column(name = "duracion")
    private int duracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "participantes")
    private int participantes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_ingreso")
    private BigDecimal montoIngreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "facilitador")
    private String facilitador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "servicio_otorgado")
    private String servicioOtorgado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveServicio")
    private List<ServiciosTecnologicosEncuestasSatisfaccion> serviciosTecnologicosEncuestasSatisfaccionList;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "mes", referencedColumnName = "numero", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Meses meses;

    public ServiciosTecnologicos() {
    }

    public ServiciosTecnologicos(ServiciosTecnologicosPK serviciosTecnologicosPK) {
        this.serviciosTecnologicosPK = serviciosTecnologicosPK;
    }

    public ServiciosTecnologicos(ServiciosTecnologicosPK serviciosTecnologicosPK, String tipoServicio, String nombreServicio, Date fechaInicio, Date fechaFin, int duracion, int participantes, BigDecimal montoIngreso, String facilitador, String servicioOtorgado) {
        this.serviciosTecnologicosPK = serviciosTecnologicosPK;
        this.tipoServicio = tipoServicio;
        this.nombreServicio = nombreServicio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracion = duracion;
        this.participantes = participantes;
        this.montoIngreso = montoIngreso;
        this.facilitador = facilitador;
        this.servicioOtorgado = servicioOtorgado;
    }

    public ServiciosTecnologicos(int ciclo, short mes, int claveServicio) {
        this.serviciosTecnologicosPK = new ServiciosTecnologicosPK(ciclo, mes, claveServicio);
    }

    public ServiciosTecnologicosPK getServiciosTecnologicosPK() {
        return serviciosTecnologicosPK;
    }

    public void setServiciosTecnologicosPK(ServiciosTecnologicosPK serviciosTecnologicosPK) {
        this.serviciosTecnologicosPK = serviciosTecnologicosPK;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public BigDecimal getMontoIngreso() {
        return montoIngreso;
    }

    public void setMontoIngreso(BigDecimal montoIngreso) {
        this.montoIngreso = montoIngreso;
    }

    public String getFacilitador() {
        return facilitador;
    }

    public void setFacilitador(String facilitador) {
        this.facilitador = facilitador;
    }

    public String getServicioOtorgado() {
        return servicioOtorgado;
    }

    public void setServicioOtorgado(String servicioOtorgado) {
        this.servicioOtorgado = servicioOtorgado;
    }

    @XmlTransient
    public List<ServiciosTecnologicosEncuestasSatisfaccion> getServiciosTecnologicosEncuestasSatisfaccionList() {
        return serviciosTecnologicosEncuestasSatisfaccionList;
    }

    public void setServiciosTecnologicosEncuestasSatisfaccionList(List<ServiciosTecnologicosEncuestasSatisfaccion> serviciosTecnologicosEncuestasSatisfaccionList) {
        this.serviciosTecnologicosEncuestasSatisfaccionList = serviciosTecnologicosEncuestasSatisfaccionList;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public Meses getMeses() {
        return meses;
    }

    public void setMeses(Meses meses) {
        this.meses = meses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviciosTecnologicosPK != null ? serviciosTecnologicosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosTecnologicos)) {
            return false;
        }
        ServiciosTecnologicos other = (ServiciosTecnologicos) object;
        if ((this.serviciosTecnologicosPK == null && other.serviciosTecnologicosPK != null) || (this.serviciosTecnologicosPK != null && !this.serviciosTecnologicosPK.equals(other.serviciosTecnologicosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ServiciosTecnologicos[ serviciosTecnologicosPK=" + serviciosTecnologicosPK + " ]";
    }
    
}
