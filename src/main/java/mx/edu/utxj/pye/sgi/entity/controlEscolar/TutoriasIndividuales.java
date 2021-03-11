/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "tutorias_individuales", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TutoriasIndividuales.findAll", query = "SELECT t FROM TutoriasIndividuales t")
    , @NamedQuery(name = "TutoriasIndividuales.findByTutoriaIndividual", query = "SELECT t FROM TutoriasIndividuales t WHERE t.tutoriaIndividual = :tutoriaIndividual")
    , @NamedQuery(name = "TutoriasIndividuales.findByFecha", query = "SELECT t FROM TutoriasIndividuales t WHERE t.fecha = :fecha")
    , @NamedQuery(name = "TutoriasIndividuales.findByHoraInicio", query = "SELECT t FROM TutoriasIndividuales t WHERE t.horaInicio = :horaInicio")
    , @NamedQuery(name = "TutoriasIndividuales.findByTiempoInvertido", query = "SELECT t FROM TutoriasIndividuales t WHERE t.tiempoInvertido = :tiempoInvertido")
    , @NamedQuery(name = "TutoriasIndividuales.findByTipoTiempo", query = "SELECT t FROM TutoriasIndividuales t WHERE t.tipoTiempo = :tipoTiempo")
    , @NamedQuery(name = "TutoriasIndividuales.findByMotivo", query = "SELECT t FROM TutoriasIndividuales t WHERE t.motivo = :motivo")
    , @NamedQuery(name = "TutoriasIndividuales.findByAccionesObservaciones", query = "SELECT t FROM TutoriasIndividuales t WHERE t.accionesObservaciones = :accionesObservaciones")
    , @NamedQuery(name = "TutoriasIndividuales.findByEventoRegistro", query = "SELECT t FROM TutoriasIndividuales t WHERE t.eventoRegistro = :eventoRegistro")})
public class TutoriasIndividuales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tutoria_individual")
    private Integer tutoriaIndividual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
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
    @Size(min = 1, max = 5000)
    @Column(name = "motivo")
    private String motivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "acciones_observaciones")
    private String accionesObservaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evento_registro")
    private int eventoRegistro;
    @JoinColumn(name = "caso_critico", referencedColumnName = "caso")
    @ManyToOne
    private CasoCritico casoCritico;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante estudiante;

    public TutoriasIndividuales() {
    }

    public TutoriasIndividuales(Integer tutoriaIndividual) {
        this.tutoriaIndividual = tutoriaIndividual;
    }

    public TutoriasIndividuales(Integer tutoriaIndividual, Date fecha, Date horaInicio, short tiempoInvertido, String tipoTiempo, String motivo, String accionesObservaciones, int eventoRegistro) {
        this.tutoriaIndividual = tutoriaIndividual;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.tiempoInvertido = tiempoInvertido;
        this.tipoTiempo = tipoTiempo;
        this.motivo = motivo;
        this.accionesObservaciones = accionesObservaciones;
        this.eventoRegistro = eventoRegistro;
    }

    public Integer getTutoriaIndividual() {
        return tutoriaIndividual;
    }

    public void setTutoriaIndividual(Integer tutoriaIndividual) {
        this.tutoriaIndividual = tutoriaIndividual;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getAccionesObservaciones() {
        return accionesObservaciones;
    }

    public void setAccionesObservaciones(String accionesObservaciones) {
        this.accionesObservaciones = accionesObservaciones;
    }

    public int getEventoRegistro() {
        return eventoRegistro;
    }

    public void setEventoRegistro(int eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    public CasoCritico getCasoCritico() {
        return casoCritico;
    }

    public void setCasoCritico(CasoCritico casoCritico) {
        this.casoCritico = casoCritico;
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
        hash += (tutoriaIndividual != null ? tutoriaIndividual.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TutoriasIndividuales)) {
            return false;
        }
        TutoriasIndividuales other = (TutoriasIndividuales) object;
        if ((this.tutoriaIndividual == null && other.tutoriaIndividual != null) || (this.tutoriaIndividual != null && !this.tutoriaIndividual.equals(other.tutoriaIndividual))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasIndividuales[ tutoriaIndividual=" + tutoriaIndividual + " ]";
    }
    
}
