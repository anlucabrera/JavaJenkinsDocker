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
@Table(name = "caso_critico", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CasoCritico.findAll", query = "SELECT c FROM CasoCritico c")
    , @NamedQuery(name = "CasoCritico.findByCaso", query = "SELECT c FROM CasoCritico c WHERE c.caso = :caso")
    , @NamedQuery(name = "CasoCritico.findByTipo", query = "SELECT c FROM CasoCritico c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "CasoCritico.findByDescripcion", query = "SELECT c FROM CasoCritico c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "CasoCritico.findByEstado", query = "SELECT c FROM CasoCritico c WHERE c.estado = :estado")
    , @NamedQuery(name = "CasoCritico.findByTutor", query = "SELECT c FROM CasoCritico c WHERE c.tutor = :tutor")
    , @NamedQuery(name = "CasoCritico.findByComentariosTutor", query = "SELECT c FROM CasoCritico c WHERE c.comentariosTutor = :comentariosTutor")
    , @NamedQuery(name = "CasoCritico.findByEvidenciaTutor", query = "SELECT c FROM CasoCritico c WHERE c.evidenciaTutor = :evidenciaTutor")
    , @NamedQuery(name = "CasoCritico.findByEspecialista", query = "SELECT c FROM CasoCritico c WHERE c.especialista = :especialista")
    , @NamedQuery(name = "CasoCritico.findByComentariosEspecialista", query = "SELECT c FROM CasoCritico c WHERE c.comentariosEspecialista = :comentariosEspecialista")
    , @NamedQuery(name = "CasoCritico.findByEvidenciaEspecialista", query = "SELECT c FROM CasoCritico c WHERE c.evidenciaEspecialista = :evidenciaEspecialista")
    , @NamedQuery(name = "CasoCritico.findByFechaRegistro", query = "SELECT c FROM CasoCritico c WHERE c.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "CasoCritico.findByFechaCierre", query = "SELECT c FROM CasoCritico c WHERE c.fechaCierre = :fechaCierre")})
public class CasoCritico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "caso")
    private Integer caso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 72)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 1500)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 31)
    @Column(name = "estado")
    private String estado;
    @Column(name = "tutor")
    private Integer tutor;
    @Size(max = 1500)
    @Column(name = "comentarios_tutor")
    private String comentariosTutor;
    @Size(max = 1500)
    @Column(name = "evidencia_tutor")
    private String evidenciaTutor;
    @Column(name = "especialista")
    private Integer especialista;
    @Size(max = 1500)
    @Column(name = "comentarios_especialista")
    private String comentariosEspecialista;
    @Size(max = 1500)
    @Column(name = "evidencia_especialista")
    private String evidenciaEspecialista;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @JoinColumn(name = "carga", referencedColumnName = "carga")
    @ManyToOne(fetch = FetchType.LAZY)
    private CargaAcademica carga;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante idEstudiante;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion")
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadMateriaConfiguracion configuracion;
    @OneToMany(mappedBy = "casoCritico", fetch = FetchType.LAZY)
    private List<TutoriasIndividuales> tutoriasIndividualesList;

    public CasoCritico() {
    }

    public CasoCritico(Integer caso) {
        this.caso = caso;
    }

    public CasoCritico(Integer caso, String tipo, Date fechaRegistro) {
        this.caso = caso;
        this.tipo = tipo;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getCaso() {
        return caso;
    }

    public void setCaso(Integer caso) {
        this.caso = caso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getTutor() {
        return tutor;
    }

    public void setTutor(Integer tutor) {
        this.tutor = tutor;
    }

    public String getComentariosTutor() {
        return comentariosTutor;
    }

    public void setComentariosTutor(String comentariosTutor) {
        this.comentariosTutor = comentariosTutor;
    }

    public String getEvidenciaTutor() {
        return evidenciaTutor;
    }

    public void setEvidenciaTutor(String evidenciaTutor) {
        this.evidenciaTutor = evidenciaTutor;
    }

    public Integer getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Integer especialista) {
        this.especialista = especialista;
    }

    public String getComentariosEspecialista() {
        return comentariosEspecialista;
    }

    public void setComentariosEspecialista(String comentariosEspecialista) {
        this.comentariosEspecialista = comentariosEspecialista;
    }

    public String getEvidenciaEspecialista() {
        return evidenciaEspecialista;
    }

    public void setEvidenciaEspecialista(String evidenciaEspecialista) {
        this.evidenciaEspecialista = evidenciaEspecialista;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public CargaAcademica getCarga() {
        return carga;
    }

    public void setCarga(CargaAcademica carga) {
        this.carga = carga;
    }

    public Estudiante getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Estudiante idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public UnidadMateriaConfiguracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(UnidadMateriaConfiguracion configuracion) {
        this.configuracion = configuracion;
    }

    @XmlTransient
    public List<TutoriasIndividuales> getTutoriasIndividualesList() {
        return tutoriasIndividualesList;
    }

    public void setTutoriasIndividualesList(List<TutoriasIndividuales> tutoriasIndividualesList) {
        this.tutoriasIndividualesList = tutoriasIndividualesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (caso != null ? caso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CasoCritico)) {
            return false;
        }
        CasoCritico other = (CasoCritico) object;
        if ((this.caso == null && other.caso != null) || (this.caso != null && !this.caso.equals(other.caso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico[ caso=" + caso + " ]";
    }
    
}
