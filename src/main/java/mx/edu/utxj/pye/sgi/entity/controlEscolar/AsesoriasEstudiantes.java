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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "asesorias_estudiantes", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsesoriasEstudiantes.findAll", query = "SELECT a FROM AsesoriasEstudiantes a")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByAsesoriaEstudiante", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.asesoriaEstudiante = :asesoriaEstudiante")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByPersonal", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.personal = :personal")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByFechaHora", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.fechaHora = :fechaHora")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByObservacionesCompromisos", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.observacionesCompromisos = :observacionesCompromisos")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByTiempoInvertido", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.tiempoInvertido = :tiempoInvertido")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByTipoTiempo", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.tipoTiempo = :tipoTiempo")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByTipo", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "AsesoriasEstudiantes.findByEventoRegistro", query = "SELECT a FROM AsesoriasEstudiantes a WHERE a.eventoRegistro = :eventoRegistro")})
public class AsesoriasEstudiantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "asesoria_estudiante")
    private Integer asesoriaEstudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;
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
    @JoinTable(name = "control_escolar.participantes_asesorias_estudiantes", joinColumns = {
        @JoinColumn(name = "asesoria_estudiante", referencedColumnName = "asesoria_estudiante")}, inverseJoinColumns = {
        @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Estudiante> estudianteList;

    public AsesoriasEstudiantes() {
    }

    public AsesoriasEstudiantes(Integer asesoriaEstudiante) {
        this.asesoriaEstudiante = asesoriaEstudiante;
    }

    public AsesoriasEstudiantes(Integer asesoriaEstudiante, int personal, Date fechaHora, String observacionesCompromisos, short tiempoInvertido, String tipoTiempo, String tipo, int eventoRegistro) {
        this.asesoriaEstudiante = asesoriaEstudiante;
        this.personal = personal;
        this.fechaHora = fechaHora;
        this.observacionesCompromisos = observacionesCompromisos;
        this.tiempoInvertido = tiempoInvertido;
        this.tipoTiempo = tipoTiempo;
        this.tipo = tipo;
        this.eventoRegistro = eventoRegistro;
    }

    public Integer getAsesoriaEstudiante() {
        return asesoriaEstudiante;
    }

    public void setAsesoriaEstudiante(Integer asesoriaEstudiante) {
        this.asesoriaEstudiante = asesoriaEstudiante;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asesoriaEstudiante != null ? asesoriaEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsesoriasEstudiantes)) {
            return false;
        }
        AsesoriasEstudiantes other = (AsesoriasEstudiantes) object;
        if ((this.asesoriaEstudiante == null && other.asesoriaEstudiante != null) || (this.asesoriaEstudiante != null && !this.asesoriaEstudiante.equals(other.asesoriaEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes[ asesoriaEstudiante=" + asesoriaEstudiante + " ]";
    }
    
}
