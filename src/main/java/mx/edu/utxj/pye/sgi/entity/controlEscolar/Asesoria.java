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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "asesoria", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asesoria.findAll", query = "SELECT a FROM Asesoria a")
    , @NamedQuery(name = "Asesoria.findByIdAsesoria", query = "SELECT a FROM Asesoria a WHERE a.idAsesoria = :idAsesoria")
    , @NamedQuery(name = "Asesoria.findByFechaHora", query = "SELECT a FROM Asesoria a WHERE a.fechaHora = :fechaHora")
    , @NamedQuery(name = "Asesoria.findByObservacionesCompromisos", query = "SELECT a FROM Asesoria a WHERE a.observacionesCompromisos = :observacionesCompromisos")
    , @NamedQuery(name = "Asesoria.findByTiempoInvertido", query = "SELECT a FROM Asesoria a WHERE a.tiempoInvertido = :tiempoInvertido")
    , @NamedQuery(name = "Asesoria.findByTipoTiempo", query = "SELECT a FROM Asesoria a WHERE a.tipoTiempo = :tipoTiempo")
    , @NamedQuery(name = "Asesoria.findByTipo", query = "SELECT a FROM Asesoria a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "Asesoria.findByEventoRegistro", query = "SELECT a FROM Asesoria a WHERE a.eventoRegistro = :eventoRegistro")})
public class Asesoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_asesoria")
    private Integer idAsesoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3000)
    @Column(name = "observaciones_compromisos")
    private String observacionesCompromisos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tiempo_invertido")
    private short tiempoInvertido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "tipo_tiempo")
    private String tipoTiempo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evento_registro")
    private int eventoRegistro;
    @JoinTable(name = "control_escolar.participantes_asesoria", joinColumns = {
        @JoinColumn(name = "asesoria", referencedColumnName = "id_asesoria")}, inverseJoinColumns = {
        @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")})
    @ManyToMany
    private List<Estudiante> estudianteList;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion")
    @ManyToOne(optional = false)
    private UnidadMateriaConfiguracion configuracion;

    public Asesoria() {
    }

    public Asesoria(Integer idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public Asesoria(Integer idAsesoria, Date fechaHora, String observacionesCompromisos, short tiempoInvertido, String tipoTiempo, String tipo, int eventoRegistro) {
        this.idAsesoria = idAsesoria;
        this.fechaHora = fechaHora;
        this.observacionesCompromisos = observacionesCompromisos;
        this.tiempoInvertido = tiempoInvertido;
        this.tipoTiempo = tipoTiempo;
        this.tipo = tipo;
        this.eventoRegistro = eventoRegistro;
    }

    public Integer getIdAsesoria() {
        return idAsesoria;
    }

    public void setIdAsesoria(Integer idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getObservacionesCompromisos() {
        return observacionesCompromisos;
    }

    public void setObservacionesCompromisos(String observacionesCompromisos) {
        this.observacionesCompromisos = observacionesCompromisos;
    }

    public short getTiempoInvertido() {
        return tiempoInvertido;
    }

    public void setTiempoInvertido(short tiempoInvertido) {
        this.tiempoInvertido = tiempoInvertido;
    }

    public String getTipoTiempo() {
        return tipoTiempo;
    }

    public void setTipoTiempo(String tipoTiempo) {
        this.tipoTiempo = tipoTiempo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(int eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    public UnidadMateriaConfiguracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(UnidadMateriaConfiguracion configuracion) {
        this.configuracion = configuracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAsesoria != null ? idAsesoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asesoria)) {
            return false;
        }
        Asesoria other = (Asesoria) object;
        if ((this.idAsesoria == null && other.idAsesoria != null) || (this.idAsesoria != null && !this.idAsesoria.equals(other.idAsesoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria[ idAsesoria=" + idAsesoria + " ]";
    }
    
}
