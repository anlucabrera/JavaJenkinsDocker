/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "asesorias_tutorias_ciclo_periodos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findAll", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByRegistro", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.registro = :registro")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByPeriodoEscolar", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByProgramaEducativo", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByCuatrimestre", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByGrupo", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.grupo = :grupo")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByTipoActividad", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.tipoActividad = :tipoActividad")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByTipo", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByAsunto", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.asunto = :asunto")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByNoTutoriasAsesorias", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.noTutoriasAsesorias = :noTutoriasAsesorias")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByAsistentesHombres", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.asistentesHombres = :asistentesHombres")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByAsistentesMujeres", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.asistentesMujeres = :asistentesMujeres")
    , @NamedQuery(name = "AsesoriasTutoriasCicloPeriodos.findByTutor", query = "SELECT a FROM AsesoriasTutoriasCicloPeriodos a WHERE a.tutor = :tutor")})
public class AsesoriasTutoriasCicloPeriodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cuatrimestre")
    private String cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "grupo")
    private String grupo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo_actividad")
    private String tipoActividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "asunto")
    private String asunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "noTutoriasAsesorias")
    private short noTutoriasAsesorias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asistentes_hombres")
    private short asistentesHombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asistentes_mujeres")
    private short asistentesMujeres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tutor")
    private int tutor;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public AsesoriasTutoriasCicloPeriodos() {
    }

    public AsesoriasTutoriasCicloPeriodos(Integer registro) {
        this.registro = registro;
    }

    public AsesoriasTutoriasCicloPeriodos(Integer registro, int periodoEscolar, short programaEducativo, String cuatrimestre, String grupo, String tipoActividad, String tipo, String asunto, short noTutoriasAsesorias, short asistentesHombres, short asistentesMujeres, int tutor) {
        this.registro = registro;
        this.periodoEscolar = periodoEscolar;
        this.programaEducativo = programaEducativo;
        this.cuatrimestre = cuatrimestre;
        this.grupo = grupo;
        this.tipoActividad = tipoActividad;
        this.tipo = tipo;
        this.asunto = asunto;
        this.noTutoriasAsesorias = noTutoriasAsesorias;
        this.asistentesHombres = asistentesHombres;
        this.asistentesMujeres = asistentesMujeres;
        this.tutor = tutor;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public String getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(String cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public short getNoTutoriasAsesorias() {
        return noTutoriasAsesorias;
    }

    public void setNoTutoriasAsesorias(short noTutoriasAsesorias) {
        this.noTutoriasAsesorias = noTutoriasAsesorias;
    }

    public short getAsistentesHombres() {
        return asistentesHombres;
    }

    public void setAsistentesHombres(short asistentesHombres) {
        this.asistentesHombres = asistentesHombres;
    }

    public short getAsistentesMujeres() {
        return asistentesMujeres;
    }

    public void setAsistentesMujeres(short asistentesMujeres) {
        this.asistentesMujeres = asistentesMujeres;
    }

    public int getTutor() {
        return tutor;
    }

    public void setTutor(int tutor) {
        this.tutor = tutor;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
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
        if (!(object instanceof AsesoriasTutoriasCicloPeriodos)) {
            return false;
        }
        AsesoriasTutoriasCicloPeriodos other = (AsesoriasTutoriasCicloPeriodos) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos[ registro=" + registro + " ]";
    }
    
}
