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
 * @author Desarrollo
 */
@Entity
@Table(name = "baja", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Baja.findAll", query = "SELECT b FROM Baja b")
    , @NamedQuery(name = "Baja.findByIdBajas", query = "SELECT b FROM Baja b WHERE b.idBajas = :idBajas")
    , @NamedQuery(name = "Baja.findByPeriodoEscolar", query = "SELECT b FROM Baja b WHERE b.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "Baja.findByTipoBaja", query = "SELECT b FROM Baja b WHERE b.tipoBaja = :tipoBaja")
    , @NamedQuery(name = "Baja.findByCausaBaja", query = "SELECT b FROM Baja b WHERE b.causaBaja = :causaBaja")
    , @NamedQuery(name = "Baja.findByEmpleadoRegistro", query = "SELECT b FROM Baja b WHERE b.empleadoRegistro = :empleadoRegistro")
    , @NamedQuery(name = "Baja.findByFechaBaja", query = "SELECT b FROM Baja b WHERE b.fechaBaja = :fechaBaja")
    , @NamedQuery(name = "Baja.findByAccionesTutor", query = "SELECT b FROM Baja b WHERE b.accionesTutor = :accionesTutor")
    , @NamedQuery(name = "Baja.findByDictamenPsicopedagogia", query = "SELECT b FROM Baja b WHERE b.dictamenPsicopedagogia = :dictamenPsicopedagogia")
    , @NamedQuery(name = "Baja.findByFechaValpsicopedagogia", query = "SELECT b FROM Baja b WHERE b.fechaValpsicopedagogia = :fechaValpsicopedagogia")
    , @NamedQuery(name = "Baja.findByValidoPsicopedagogia", query = "SELECT b FROM Baja b WHERE b.validoPsicopedagogia = :validoPsicopedagogia")
    , @NamedQuery(name = "Baja.findByFechaValidacion", query = "SELECT b FROM Baja b WHERE b.fechaValidacion = :fechaValidacion")
    , @NamedQuery(name = "Baja.findByValidada", query = "SELECT b FROM Baja b WHERE b.validada = :validada")})
public class Baja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bajas")
    private Integer idBajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_baja")
    private int tipoBaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "causa_baja")
    private int causaBaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empleado_registro")
    private int empleadoRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "acciones_tutor")
    private String accionesTutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "dictamen_psicopedagogia")
    private String dictamenPsicopedagogia;
    @Column(name = "fecha_valpsicopedagogia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValpsicopedagogia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valido_psicopedagogia")
    private int validoPsicopedagogia;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validada")
    private int validada;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroBaja")
    private List<BajaReprobacion> bajaReprobacionList;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @Size(max = 500)

    public Baja() {
    }

    public Baja(Integer idBajas) {
        this.idBajas = idBajas;
    }

    public Baja(Integer idBajas, int periodoEscolar, int tipoBaja, int causaBaja, int empleadoRegistro, Date fechaBaja, String accionesTutor, String dictamenPsicopedagogia, Date fechaValpsicopedagogia, int validoPsicopedagogia, Date fechaValidacion, int validada, List<BajaReprobacion> bajaReprobacionList, Estudiante estudiante) {
        this.idBajas = idBajas;
        this.periodoEscolar = periodoEscolar;
        this.tipoBaja = tipoBaja;
        this.causaBaja = causaBaja;
        this.empleadoRegistro = empleadoRegistro;
        this.fechaBaja = fechaBaja;
        this.accionesTutor = accionesTutor;
        this.dictamenPsicopedagogia = dictamenPsicopedagogia;
        this.fechaValpsicopedagogia = fechaValpsicopedagogia;
        this.validoPsicopedagogia = validoPsicopedagogia;
        this.fechaValidacion = fechaValidacion;
        this.validada = validada;
        this.bajaReprobacionList = bajaReprobacionList;
        this.estudiante = estudiante;
    }

    
    public Integer getIdBajas() {
        return idBajas;
    }

    public void setIdBajas(Integer idBajas) {
        this.idBajas = idBajas;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public int getTipoBaja() {
        return tipoBaja;
    }

    public void setTipoBaja(int tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    public int getCausaBaja() {
        return causaBaja;
    }

    public void setCausaBaja(int causaBaja) {
        this.causaBaja = causaBaja;
    }

    public int getEmpleadoRegistro() {
        return empleadoRegistro;
    }

    public void setEmpleadoRegistro(int empleadoRegistro) {
        this.empleadoRegistro = empleadoRegistro;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getAccionesTutor() {
        return accionesTutor;
    }

    public void setAccionesTutor(String accionesTutor) {
        this.accionesTutor = accionesTutor;
    }

    public String getDictamenPsicopedagogia() {
        return dictamenPsicopedagogia;
    }

    public void setDictamenPsicopedagogia(String dictamenPsicopedagogia) {
        this.dictamenPsicopedagogia = dictamenPsicopedagogia;
    }

    public Date getFechaValpsicopedagogia() {
        return fechaValpsicopedagogia;
    }

    public void setFechaValpsicopedagogia(Date fechaValpsicopedagogia) {
        this.fechaValpsicopedagogia = fechaValpsicopedagogia;
    }
    
    public int getValidoPsicopedagogia() {
        return validoPsicopedagogia;
    }

    public void setValidoPsicopedagogia(int validoPsicopedagogia) {
        this.validoPsicopedagogia = validoPsicopedagogia;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public int getValidada() {
        return validada;
    }

    public void setValidada(int validada) {
        this.validada = validada;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBajas != null ? idBajas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Baja)) {
            return false;
        }
        Baja other = (Baja) object;
        if ((this.idBajas == null && other.idBajas != null) || (this.idBajas != null && !this.idBajas.equals(other.idBajas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja[ idBajas=" + idBajas + " ]";
    }
    
    @XmlTransient
    public List<BajaReprobacion> getBajaReprobacionList() {
        return bajaReprobacionList;
    }

    public void setBajaReprobacionList(List<BajaReprobacion> bajaReprobacionList) {
        this.bajaReprobacionList = bajaReprobacionList;
    }
}
