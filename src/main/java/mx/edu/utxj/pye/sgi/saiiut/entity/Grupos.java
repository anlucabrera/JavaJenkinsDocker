/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "grupos", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupos.findAll", query = "SELECT g FROM Grupos g")
    , @NamedQuery(name = "Grupos.findByCveGrupo", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cveGrupo = :cveGrupo")
    , @NamedQuery(name = "Grupos.findByCveTurno", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cveTurno = :cveTurno")
    , @NamedQuery(name = "Grupos.findByCvePlan", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cvePlan = :cvePlan")
    , @NamedQuery(name = "Grupos.findByCveCarrera", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "Grupos.findByCveDivision", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cveDivision = :cveDivision")
    , @NamedQuery(name = "Grupos.findByCveUnidadAcademica", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cveUnidadAcademica = :cveUnidadAcademica")
    , @NamedQuery(name = "Grupos.findByCveUniversidad", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Grupos.findByCvePeriodo", query = "SELECT g FROM Grupos g WHERE g.gruposPK.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "Grupos.findByCveMaestro", query = "SELECT g FROM Grupos g WHERE g.cveMaestro = :cveMaestro")
    , @NamedQuery(name = "Grupos.findByGrado", query = "SELECT g FROM Grupos g WHERE g.grado = :grado")
    , @NamedQuery(name = "Grupos.findByIdGrupo", query = "SELECT g FROM Grupos g WHERE g.idGrupo = :idGrupo")
    , @NamedQuery(name = "Grupos.findByCapacidadMaxima", query = "SELECT g FROM Grupos g WHERE g.capacidadMaxima = :capacidadMaxima")
    , @NamedQuery(name = "Grupos.findByNumeroRepetidores", query = "SELECT g FROM Grupos g WHERE g.numeroRepetidores = :numeroRepetidores")})
public class Grupos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GruposPK gruposPK;
    @Column(name = "cve_maestro")
    private Integer cveMaestro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private short grado;
    @Size(max = 5)
    @Column(name = "id_grupo")
    private String idGrupo;
    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;
    @Column(name = "numero_repetidores")
    private Integer numeroRepetidores;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupos")
    private List<Alumnos> alumnosList;

    public Grupos() {
    }

    public Grupos(GruposPK gruposPK) {
        this.gruposPK = gruposPK;
    }

    public Grupos(GruposPK gruposPK, short grado) {
        this.gruposPK = gruposPK;
        this.grado = grado;
    }

    public Grupos(int cveGrupo, int cveTurno, int cvePlan, int cveCarrera, int cveDivision, int cveUnidadAcademica, int cveUniversidad, int cvePeriodo) {
        this.gruposPK = new GruposPK(cveGrupo, cveTurno, cvePlan, cveCarrera, cveDivision, cveUnidadAcademica, cveUniversidad, cvePeriodo);
    }

    public GruposPK getGruposPK() {
        return gruposPK;
    }

    public void setGruposPK(GruposPK gruposPK) {
        this.gruposPK = gruposPK;
    }

    public Integer getCveMaestro() {
        return cveMaestro;
    }

    public void setCveMaestro(Integer cveMaestro) {
        this.cveMaestro = cveMaestro;
    }

    public short getGrado() {
        return grado;
    }

    public void setGrado(short grado) {
        this.grado = grado;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getNumeroRepetidores() {
        return numeroRepetidores;
    }

    public void setNumeroRepetidores(Integer numeroRepetidores) {
        this.numeroRepetidores = numeroRepetidores;
    }

    @XmlTransient
    public List<Alumnos> getAlumnosList() {
        return alumnosList;
    }

    public void setAlumnosList(List<Alumnos> alumnosList) {
        this.alumnosList = alumnosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gruposPK != null ? gruposPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupos)) {
            return false;
        }
        Grupos other = (Grupos) object;
        if ((this.gruposPK == null && other.gruposPK != null) || (this.gruposPK != null && !this.gruposPK.equals(other.gruposPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Grupos[ gruposPK=" + gruposPK + " ]";
    }
    
}
