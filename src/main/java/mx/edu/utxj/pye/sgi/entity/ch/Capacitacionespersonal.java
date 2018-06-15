/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Finanzas1
 */
@Entity
@Table(name = "capacitacionespersonal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Capacitacionespersonal.findAll", query = "SELECT c FROM Capacitacionespersonal c")
    , @NamedQuery(name = "Capacitacionespersonal.findByCursoClave", query = "SELECT c FROM Capacitacionespersonal c WHERE c.cursoClave = :cursoClave")
    , @NamedQuery(name = "Capacitacionespersonal.findByNombre", query = "SELECT c FROM Capacitacionespersonal c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Capacitacionespersonal.findByFechaInicio", query = "SELECT c FROM Capacitacionespersonal c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Capacitacionespersonal.findByFechaFin", query = "SELECT c FROM Capacitacionespersonal c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "Capacitacionespersonal.findByDuracionHoras", query = "SELECT c FROM Capacitacionespersonal c WHERE c.duracionHoras = :duracionHoras")
    , @NamedQuery(name = "Capacitacionespersonal.findByDuracionMinutos", query = "SELECT c FROM Capacitacionespersonal c WHERE c.duracionMinutos = :duracionMinutos")
    , @NamedQuery(name = "Capacitacionespersonal.findByEmpresaImpartidora", query = "SELECT c FROM Capacitacionespersonal c WHERE c.empresaImpartidora = :empresaImpartidora")
    , @NamedQuery(name = "Capacitacionespersonal.findByEvidenciaCapacitacion", query = "SELECT c FROM Capacitacionespersonal c WHERE c.evidenciaCapacitacion = :evidenciaCapacitacion")
    , @NamedQuery(name = "Capacitacionespersonal.findByEstatus", query = "SELECT c FROM Capacitacionespersonal c WHERE c.estatus = :estatus")
    , @NamedQuery(name = "Capacitacionespersonal.findByTipoCapacitacion", query = "SELECT c FROM Capacitacionespersonal c WHERE c.tipoCapacitacion = :tipoCapacitacion")})
public class Capacitacionespersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "curso_Clave")
    private Integer cursoClave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
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
    @Column(name = "duracion_horas")
    private short duracionHoras;
    @Column(name = "duracion_minutos")
    private Short duracionMinutos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "empresa_impartidora")
    private String empresaImpartidora;
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
    @Column(name = "lugar")
    private String lugar;
    @Size(max = 500)
    @Column(name = "evidencia_capacitacion")
    private String evidenciaCapacitacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 8)
    @Column(name = "tipo_capacitacion")
    private String tipoCapacitacion;
    @JoinColumn(name = "modalidad", referencedColumnName = "modalidad")
    @ManyToOne(optional = false)
    private CursosModalidad modalidad;
    @JoinColumn(name = "tipo", referencedColumnName = "tipo")
    @ManyToOne(optional = false)
    private CursosTipo tipo;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Capacitacionespersonal() {
    }

    public Capacitacionespersonal(Integer cursoClave) {
        this.cursoClave = cursoClave;
    }

    public Capacitacionespersonal(Integer cursoClave, String nombre, Date fechaInicio, Date fechaFin, short duracionHoras, String empresaImpartidora, String objetivo, String lugar, String estatus) {
        this.cursoClave = cursoClave;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracionHoras = duracionHoras;
        this.empresaImpartidora = empresaImpartidora;
        this.objetivo = objetivo;
        this.lugar = lugar;
        this.estatus = estatus;
    }

    public Integer getCursoClave() {
        return cursoClave;
    }

    public void setCursoClave(Integer cursoClave) {
        this.cursoClave = cursoClave;
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

    public short getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(short duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public Short getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Short duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEvidenciaCapacitacion() {
        return evidenciaCapacitacion;
    }

    public void setEvidenciaCapacitacion(String evidenciaCapacitacion) {
        this.evidenciaCapacitacion = evidenciaCapacitacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipoCapacitacion() {
        return tipoCapacitacion;
    }

    public void setTipoCapacitacion(String tipoCapacitacion) {
        this.tipoCapacitacion = tipoCapacitacion;
    }

    public CursosModalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(CursosModalidad modalidad) {
        this.modalidad = modalidad;
    }

    public CursosTipo getTipo() {
        return tipo;
    }

    public void setTipo(CursosTipo tipo) {
        this.tipo = tipo;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cursoClave != null ? cursoClave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Capacitacionespersonal)) {
            return false;
        }
        Capacitacionespersonal other = (Capacitacionespersonal) object;
        if ((this.cursoClave == null && other.cursoClave != null) || (this.cursoClave != null && !this.cursoClave.equals(other.cursoClave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal[ cursoClave=" + cursoClave + " ]";
    }
    
}
