/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "matricula_periodos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatriculaPeriodosEscolares.findAll", query = "SELECT m FROM MatriculaPeriodosEscolares m")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByRegistro", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.registro = :registro")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByMatricula", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula = :matricula")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByPeriodo", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo = :periodo")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByProgramaEducativo", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByCuatrimestre", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByGrupo", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.grupo = :grupo")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByCurp", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.curp = :curp")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByLenguaIndigena", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.lenguaIndigena = :lenguaIndigena")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByDiscapacidad", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.discapacidad = :discapacidad")
    , @NamedQuery(name = "MatriculaPeriodosEscolares.findByComunidadIndigena", query = "SELECT m FROM MatriculaPeriodosEscolares m WHERE m.comunidadIndigena = :comunidadIndigena")})
public class MatriculaPeriodosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
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
    @Size(min = 1, max = 18)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "lenguaIndigena")
    private String lenguaIndigena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "discapacidad")
    private String discapacidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "comunidadIndigena")
    private String comunidadIndigena;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculaPeriodosEscolares")
    private List<BecasPeriodosEscolares> becasPeriodosEscolaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculaPeriodosEscolares")
    private List<ParticipantesActividadesFormacionIntegral> participantesActividadesFormacionIntegralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculaPeriodosEscolares")
    private List<RegistroMovilidadEstudiante> registroMovilidadEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculaPeriodosEscolares")
    private List<EstadiasPorEstudiante> estadiasPorEstudianteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculaPeriodosEscolares")
    private List<DesercionPeriodosEscolares> desercionPeriodosEscolaresList;

    public MatriculaPeriodosEscolares() {
    }

    public MatriculaPeriodosEscolares(Integer registro) {
        this.registro = registro;
    }

    public MatriculaPeriodosEscolares(Integer registro, String matricula, int periodo, short programaEducativo, String cuatrimestre, String grupo, String curp, String lenguaIndigena, String discapacidad, String comunidadIndigena) {
        this.registro = registro;
        this.matricula = matricula;
        this.periodo = periodo;
        this.programaEducativo = programaEducativo;
        this.cuatrimestre = cuatrimestre;
        this.grupo = grupo;
        this.curp = curp;
        this.lenguaIndigena = lenguaIndigena;
        this.discapacidad = discapacidad;
        this.comunidadIndigena = comunidadIndigena;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
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

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getLenguaIndigena() {
        return lenguaIndigena;
    }

    public void setLenguaIndigena(String lenguaIndigena) {
        this.lenguaIndigena = lenguaIndigena;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getComunidadIndigena() {
        return comunidadIndigena;
    }

    public void setComunidadIndigena(String comunidadIndigena) {
        this.comunidadIndigena = comunidadIndigena;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @XmlTransient
    public List<BecasPeriodosEscolares> getBecasPeriodosEscolaresList() {
        return becasPeriodosEscolaresList;
    }

    public void setBecasPeriodosEscolaresList(List<BecasPeriodosEscolares> becasPeriodosEscolaresList) {
        this.becasPeriodosEscolaresList = becasPeriodosEscolaresList;
    }

    @XmlTransient
    public List<ParticipantesActividadesFormacionIntegral> getParticipantesActividadesFormacionIntegralList() {
        return participantesActividadesFormacionIntegralList;
    }

    public void setParticipantesActividadesFormacionIntegralList(List<ParticipantesActividadesFormacionIntegral> participantesActividadesFormacionIntegralList) {
        this.participantesActividadesFormacionIntegralList = participantesActividadesFormacionIntegralList;
    }

    @XmlTransient
    public List<RegistroMovilidadEstudiante> getRegistroMovilidadEstudianteList() {
        return registroMovilidadEstudianteList;
    }

    public void setRegistroMovilidadEstudianteList(List<RegistroMovilidadEstudiante> registroMovilidadEstudianteList) {
        this.registroMovilidadEstudianteList = registroMovilidadEstudianteList;
    }

    @XmlTransient
    public List<EstadiasPorEstudiante> getEstadiasPorEstudianteList() {
        return estadiasPorEstudianteList;
    }

    public void setEstadiasPorEstudianteList(List<EstadiasPorEstudiante> estadiasPorEstudianteList) {
        this.estadiasPorEstudianteList = estadiasPorEstudianteList;
    }

    @XmlTransient
    public List<DesercionPeriodosEscolares> getDesercionPeriodosEscolaresList() {
        return desercionPeriodosEscolaresList;
    }

    public void setDesercionPeriodosEscolaresList(List<DesercionPeriodosEscolares> desercionPeriodosEscolaresList) {
        this.desercionPeriodosEscolaresList = desercionPeriodosEscolaresList;
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
        if (!(object instanceof MatriculaPeriodosEscolares)) {
            return false;
        }
        MatriculaPeriodosEscolares other = (MatriculaPeriodosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares[ registro=" + registro + " ]";
    }
    
}
