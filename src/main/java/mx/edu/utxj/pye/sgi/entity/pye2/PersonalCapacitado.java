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
@Table(name = "personal_capacitado", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonalCapacitado.findAll", query = "SELECT p FROM PersonalCapacitado p")
    , @NamedQuery(name = "PersonalCapacitado.findByRegistro", query = "SELECT p FROM PersonalCapacitado p WHERE p.registro = :registro")
    , @NamedQuery(name = "PersonalCapacitado.findByCurso", query = "SELECT p FROM PersonalCapacitado p WHERE p.curso = :curso")
    , @NamedQuery(name = "PersonalCapacitado.findByNombre", query = "SELECT p FROM PersonalCapacitado p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PersonalCapacitado.findByFechaInicial", query = "SELECT p FROM PersonalCapacitado p WHERE p.fechaInicial = :fechaInicial")
    , @NamedQuery(name = "PersonalCapacitado.findByFechaFinal", query = "SELECT p FROM PersonalCapacitado p WHERE p.fechaFinal = :fechaFinal")
    , @NamedQuery(name = "PersonalCapacitado.findByDuracion", query = "SELECT p FROM PersonalCapacitado p WHERE p.duracion = :duracion")
    , @NamedQuery(name = "PersonalCapacitado.findByEmpresaImpartidora", query = "SELECT p FROM PersonalCapacitado p WHERE p.empresaImpartidora = :empresaImpartidora")
    , @NamedQuery(name = "PersonalCapacitado.findByObjetivo", query = "SELECT p FROM PersonalCapacitado p WHERE p.objetivo = :objetivo")
    , @NamedQuery(name = "PersonalCapacitado.findByLugarImparticion", query = "SELECT p FROM PersonalCapacitado p WHERE p.lugarImparticion = :lugarImparticion")
    , @NamedQuery(name = "PersonalCapacitado.findByMontoInvertido", query = "SELECT p FROM PersonalCapacitado p WHERE p.montoInvertido = :montoInvertido")})
public class PersonalCapacitado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "curso")
    private String curso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "duracion")
    private String duracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "empresa_impartidora")
    private String empresaImpartidora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "lugar_imparticion")
    private String lugarImparticion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_invertido")
    private BigDecimal montoInvertido;
    @JoinColumn(name = "modalidad", referencedColumnName = "percap_mod")
    @ManyToOne(optional = false)
    private PercapModalidad modalidad;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "tipo", referencedColumnName = "percap_tipo")
    @ManyToOne(optional = false)
    private PercapTipo tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "percap")
    private List<ParticipantesPersonalCapacitado> participantesPersonalCapacitadoList;

    public PersonalCapacitado() {
    }

    public PersonalCapacitado(Integer registro) {
        this.registro = registro;
    }

    public PersonalCapacitado(Integer registro, String curso, String nombre, Date fechaInicial, Date fechaFinal, String duracion, String empresaImpartidora, String objetivo, String lugarImparticion, BigDecimal montoInvertido) {
        this.registro = registro;
        this.curso = curso;
        this.nombre = nombre;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.duracion = duracion;
        this.empresaImpartidora = empresaImpartidora;
        this.objetivo = objetivo;
        this.lugarImparticion = lugarImparticion;
        this.montoInvertido = montoInvertido;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getEmpresaImpartidora() {
        return empresaImpartidora;
    }

    public void setEmpresaImpartidora(String empresaImpartidora) {
        this.empresaImpartidora = empresaImpartidora;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getLugarImparticion() {
        return lugarImparticion;
    }

    public void setLugarImparticion(String lugarImparticion) {
        this.lugarImparticion = lugarImparticion;
    }

    public BigDecimal getMontoInvertido() {
        return montoInvertido;
    }

    public void setMontoInvertido(BigDecimal montoInvertido) {
        this.montoInvertido = montoInvertido;
    }

    public PercapModalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(PercapModalidad modalidad) {
        this.modalidad = modalidad;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public PercapTipo getTipo() {
        return tipo;
    }

    public void setTipo(PercapTipo tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<ParticipantesPersonalCapacitado> getParticipantesPersonalCapacitadoList() {
        return participantesPersonalCapacitadoList;
    }

    public void setParticipantesPersonalCapacitadoList(List<ParticipantesPersonalCapacitado> participantesPersonalCapacitadoList) {
        this.participantesPersonalCapacitadoList = participantesPersonalCapacitadoList;
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
        if (!(object instanceof PersonalCapacitado)) {
            return false;
        }
        PersonalCapacitado other = (PersonalCapacitado) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado[ registro=" + registro + " ]";
    }
    
}
