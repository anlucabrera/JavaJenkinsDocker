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
    , @NamedQuery(name = "TutoriasIndividuales.findByFechaHora", query = "SELECT t FROM TutoriasIndividuales t WHERE t.fechaHora = :fechaHora")
    , @NamedQuery(name = "TutoriasIndividuales.findByMotivo", query = "SELECT t FROM TutoriasIndividuales t WHERE t.motivo = :motivo")
    , @NamedQuery(name = "TutoriasIndividuales.findByAccionesObservaciones", query = "SELECT t FROM TutoriasIndividuales t WHERE t.accionesObservaciones = :accionesObservaciones")
    , @NamedQuery(name = "TutoriasIndividuales.findByTiempoSesion", query = "SELECT t FROM TutoriasIndividuales t WHERE t.tiempoSesion = :tiempoSesion")
    , @NamedQuery(name = "TutoriasIndividuales.findByEventoRegistro", query = "SELECT t FROM TutoriasIndividuales t WHERE t.eventoRegistro = :eventoRegistro")})
public class TutoriasIndividuales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tutoria_individual")
    private String tutoriaIndividual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "motivo")
    private String motivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "acciones_observaciones")
    private String accionesObservaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tiempo_sesion")
    private String tiempoSesion;
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

    public TutoriasIndividuales(String tutoriaIndividual) {
        this.tutoriaIndividual = tutoriaIndividual;
    }

    public TutoriasIndividuales(String tutoriaIndividual, Date fechaHora, String motivo, String accionesObservaciones, String tiempoSesion, int eventoRegistro) {
        this.tutoriaIndividual = tutoriaIndividual;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.accionesObservaciones = accionesObservaciones;
        this.tiempoSesion = tiempoSesion;
        this.eventoRegistro = eventoRegistro;
    }

    public String getTutoriaIndividual() {
        return tutoriaIndividual;
    }

    public void setTutoriaIndividual(String tutoriaIndividual) {
        this.tutoriaIndividual = tutoriaIndividual;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
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

    public String getTiempoSesion() {
        return tiempoSesion;
    }

    public void setTiempoSesion(String tiempoSesion) {
        this.tiempoSesion = tiempoSesion;
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
