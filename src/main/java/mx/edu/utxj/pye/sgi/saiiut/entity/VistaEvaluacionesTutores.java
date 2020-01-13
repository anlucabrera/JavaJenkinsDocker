/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "vista_evaluaciones_tutores", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VistaEvaluacionesTutores.findAll", query = "SELECT v FROM VistaEvaluacionesTutores v")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByPeriodo", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.pk.periodo = :periodo")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByMatricula", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.pk.matricula = :matricula")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByNombreAlumno", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.nombreAlumno = :nombreAlumno")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByGrado", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.grado = :grado")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByGrupo", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.grupo = :grupo")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByTurno", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.turno = :turno")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByNivelEducativo", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.nivelEducativo = :nivelEducativo")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByPeAbeviatura", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.peAbeviatura = :peAbeviatura")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByPeNombreCarrera", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.peNombreCarrera = :peNombreCarrera")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByCurp", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.curp = :curp")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByNombreTutor", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.nombreTutor = :nombreTutor")
    , @NamedQuery(name = "VistaEvaluacionesTutores.findByNumeroNomina", query = "SELECT v FROM VistaEvaluacionesTutores v WHERE v.pk.numeroNomina = :numeroNomina")})
public class VistaEvaluacionesTutores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private VistaEvaluacionesTutoresPK pk;
    @Size(max = 142)
    @Column(name = "nombreAlumno")
    private String nombreAlumno;
    @Column(name = "grado")
    private Short grado;
    @Size(max = 5)
    @Column(name = "grupo")
    private String grupo;
    @Size(max = 30)
    @Column(name = "turno")
    private String turno;
    @Size(max = 20)
    @Column(name = "nivelEducativo")
    private String nivelEducativo;
    @Size(max = 20)
    @Column(name = "peAbeviatura")
    private String peAbeviatura;
    @Size(max = 200)
    @Column(name = "peNombreCarrera")
    private String peNombreCarrera;
    @Size(max = 18)
    @Column(name = "curp")
    private String curp;
    @Size(max = 141)
    @Column(name = "nombreTutor")
    private String nombreTutor;

    public VistaEvaluacionesTutores() {
    }

    public VistaEvaluacionesTutores(VistaEvaluacionesTutoresPK pk) {
        this.pk = pk;
    }

    public VistaEvaluacionesTutoresPK getPk() {
        return pk;
    }

    public void setPk(VistaEvaluacionesTutoresPK pk) {
        this.pk = pk;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public Short getGrado() {
        return grado;
    }

    public void setGrado(Short grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public String getPeAbeviatura() {
        return peAbeviatura;
    }

    public void setPeAbeviatura(String peAbeviatura) {
        this.peAbeviatura = peAbeviatura;
    }

    public String getPeNombreCarrera() {
        return peNombreCarrera;
    }

    public void setPeNombreCarrera(String peNombreCarrera) {
        this.peNombreCarrera = peNombreCarrera;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombreTutor() {
        return nombreTutor;
    }

    public void setNombreTutor(String nombreTutor) {
        this.nombreTutor = nombreTutor;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.pk);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VistaEvaluacionesTutores other = (VistaEvaluacionesTutores) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VistaEvaluacionesTutores{" + "pk=" + pk + ", nombreAlumno=" + nombreAlumno + ", grado=" + grado + ", grupo=" + grupo + ", turno=" + turno + ", nivelEducativo=" + nivelEducativo + ", peAbeviatura=" + peAbeviatura + ", peNombreCarrera=" + peNombreCarrera + ", curp=" + curp + ", nombreTutor=" + nombreTutor + '}';
    }
}
