/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "expedientes_titulacion", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpedientesTitulacion.findAll", query = "SELECT e FROM ExpedientesTitulacion e")
    , @NamedQuery(name = "ExpedientesTitulacion.findByExpediente", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.expediente = :expediente")
    , @NamedQuery(name = "ExpedientesTitulacion.findByFecha", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.fecha = :fecha")
    , @NamedQuery(name = "ExpedientesTitulacion.findByNivel", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.nivel = :nivel")
    , @NamedQuery(name = "ExpedientesTitulacion.findByProgramaEducativo", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "ExpedientesTitulacion.findByGeneracion", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "ExpedientesTitulacion.findByValidado", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.validado = :validado")
    , @NamedQuery(name = "ExpedientesTitulacion.findByPersonalValido", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.personalValido = :personalValido")
    , @NamedQuery(name = "ExpedientesTitulacion.findByFechaValidacion", query = "SELECT e FROM ExpedientesTitulacion e WHERE e.fechaValidacion = :fechaValidacion")})
public class ExpedientesTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "expediente")
    private Integer expediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivel")
    private int nivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "programa_educativo")
    private String programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expediente")
    private List<TituloExpediente> tituloExpedienteList;
    @Column(name = "personal_valido")
    private Integer personalValido;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.DATE)
    private Date fechaValidacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expediente")
    private List<DomiciliosExpediente> domiciliosExpedienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expediente")
    private List<DatosContacto> datosContactoList;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @ManyToOne(optional = false)
    private Egresados matricula;
    @JoinColumn(name = "proceso", referencedColumnName = "proceso")
    @ManyToOne(optional = false)
    private ProcesosIntexp proceso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expediente")
    private List<DocumentosExpediente> documentosExpedienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "expediente")
    private List<DatosTitulacion> datosTitulacionList;

    public ExpedientesTitulacion() {
    }

    public ExpedientesTitulacion(Integer expediente) {
        this.expediente = expediente;
    }

    public ExpedientesTitulacion(Integer expediente, Date fecha, int nivel, String programaEducativo, short generacion, boolean validado) {
        this.expediente = expediente;
        this.fecha = fecha;
        this.nivel = nivel;
        this.programaEducativo = programaEducativo;
        this.generacion = generacion;
        this.validado = validado;
    }

    public Integer getExpediente() {
        return expediente;
    }

    public void setExpediente(Integer expediente) {
        this.expediente = expediente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public boolean getValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    public Integer getPersonalValido() {
        return personalValido;
    }

    public void setPersonalValido(Integer personalValido) {
        this.personalValido = personalValido;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    @XmlTransient
    public List<DomiciliosExpediente> getDomiciliosExpedienteList() {
        return domiciliosExpedienteList;
    }

    public void setDomiciliosExpedienteList(List<DomiciliosExpediente> domiciliosExpedienteList) {
        this.domiciliosExpedienteList = domiciliosExpedienteList;
    }

    @XmlTransient
    public List<DatosContacto> getDatosContactoList() {
        return datosContactoList;
    }

    public void setDatosContactoList(List<DatosContacto> datosContactoList) {
        this.datosContactoList = datosContactoList;
    }

    public Egresados getMatricula() {
        return matricula;
    }

    public void setMatricula(Egresados matricula) {
        this.matricula = matricula;
    }

    public ProcesosIntexp getProceso() {
        return proceso;
    }

    public void setProceso(ProcesosIntexp proceso) {
        this.proceso = proceso;
    }

    @XmlTransient
    public List<DocumentosExpediente> getDocumentosExpedienteList() {
        return documentosExpedienteList;
    }

    public void setDocumentosExpedienteList(List<DocumentosExpediente> documentosExpedienteList) {
        this.documentosExpedienteList = documentosExpedienteList;
    }

    @XmlTransient
    public List<DatosTitulacion> getDatosTitulacionList() {
        return datosTitulacionList;
    }

    public void setDatosTitulacionList(List<DatosTitulacion> datosTitulacionList) {
        this.datosTitulacionList = datosTitulacionList;
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
        if (!(object instanceof ExpedientesTitulacion)) {
            return false;
        }
        ExpedientesTitulacion other = (ExpedientesTitulacion) object;
        if ((this.expediente == null && other.expediente != null) || (this.expediente != null && !this.expediente.equals(other.expediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion[ expediente=" + expediente + " ]";
    }

    @XmlTransient
    public List<TituloExpediente> getTituloExpedienteList() {
        return tituloExpedienteList;
    }

    public void setTituloExpedienteList(List<TituloExpediente> tituloExpedienteList) {
        this.tituloExpedienteList = tituloExpedienteList;
    }
    
}
