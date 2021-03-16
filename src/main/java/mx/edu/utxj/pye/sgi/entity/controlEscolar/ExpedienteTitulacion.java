/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "expediente_titulacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpedienteTitulacion.findAll", query = "SELECT e FROM ExpedienteTitulacion e")
    , @NamedQuery(name = "ExpedienteTitulacion.findByExpediente", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.expediente = :expediente")
    , @NamedQuery(name = "ExpedienteTitulacion.findByFechaRegistro", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "ExpedienteTitulacion.findByValidado", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.validado = :validado")
    , @NamedQuery(name = "ExpedienteTitulacion.findByFechaValidacion", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.fechaValidacion = :fechaValidacion")
    , @NamedQuery(name = "ExpedienteTitulacion.findByPersonalValido", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.personalValido = :personalValido")
    , @NamedQuery(name = "ExpedienteTitulacion.findByPasoRegistro", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.pasoRegistro = :pasoRegistro")
    , @NamedQuery(name = "ExpedienteTitulacion.findByFechaPaso", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.fechaPaso = :fechaPaso")
    , @NamedQuery(name = "ExpedienteTitulacion.findByPromedio", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.promedio = :promedio")
    , @NamedQuery(name = "ExpedienteTitulacion.findByServicioSocial", query = "SELECT e FROM ExpedienteTitulacion e WHERE e.servicioSocial = :servicioSocial")})
public class ExpedienteTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "expediente")
    private Integer expediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion;
    @Column(name = "personal_valido")
    private Integer personalValido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 26)
    @Column(name = "paso_registro")
    private String pasoRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_paso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPaso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private float promedio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_social")
    private boolean servicioSocial;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expediente", fetch = FetchType.LAZY)
    private List<DocumentoExpedienteTitulacion> documentoExpedienteTitulacionList;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoTitulacion evento;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante matricula;

    public ExpedienteTitulacion() {
    }

    public ExpedienteTitulacion(Integer expediente) {
        this.expediente = expediente;
    }

    public ExpedienteTitulacion(Integer expediente, Date fechaRegistro, boolean validado, String pasoRegistro, Date fechaPaso, float promedio, boolean servicioSocial) {
        this.expediente = expediente;
        this.fechaRegistro = fechaRegistro;
        this.validado = validado;
        this.pasoRegistro = pasoRegistro;
        this.fechaPaso = fechaPaso;
        this.promedio = promedio;
        this.servicioSocial = servicioSocial;
    }

    public Integer getExpediente() {
        return expediente;
    }

    public void setExpediente(Integer expediente) {
        this.expediente = expediente;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean getValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Integer getPersonalValido() {
        return personalValido;
    }

    public void setPersonalValido(Integer personalValido) {
        this.personalValido = personalValido;
    }

    public String getPasoRegistro() {
        return pasoRegistro;
    }

    public void setPasoRegistro(String pasoRegistro) {
        this.pasoRegistro = pasoRegistro;
    }

    public Date getFechaPaso() {
        return fechaPaso;
    }

    public void setFechaPaso(Date fechaPaso) {
        this.fechaPaso = fechaPaso;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }

    public boolean getServicioSocial() {
        return servicioSocial;
    }

    public void setServicioSocial(boolean servicioSocial) {
        this.servicioSocial = servicioSocial;
    }

    @XmlTransient
    public List<DocumentoExpedienteTitulacion> getDocumentoExpedienteTitulacionList() {
        return documentoExpedienteTitulacionList;
    }

    public void setDocumentoExpedienteTitulacionList(List<DocumentoExpedienteTitulacion> documentoExpedienteTitulacionList) {
        this.documentoExpedienteTitulacionList = documentoExpedienteTitulacionList;
    }

    public EventoTitulacion getEvento() {
        return evento;
    }

    public void setEvento(EventoTitulacion evento) {
        this.evento = evento;
    }

    public Estudiante getMatricula() {
        return matricula;
    }

    public void setMatricula(Estudiante matricula) {
        this.matricula = matricula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expediente != null ? expediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpedienteTitulacion)) {
            return false;
        }
        ExpedienteTitulacion other = (ExpedienteTitulacion) object;
        if ((this.expediente == null && other.expediente != null) || (this.expediente != null && !this.expediente.equals(other.expediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion[ expediente=" + expediente + " ]";
    }
    
}
