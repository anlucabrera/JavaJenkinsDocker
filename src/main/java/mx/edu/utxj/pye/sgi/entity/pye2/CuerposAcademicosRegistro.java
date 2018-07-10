/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "cuerpos_academicos_registro", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerposAcademicosRegistro.findAll", query = "SELECT c FROM CuerposAcademicosRegistro c")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByRegistro", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.registro = :registro")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByCuerpoAcademico", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.cuerpoAcademico = :cuerpoAcademico")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByFechaInicio", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByFechaTermino", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.fechaTermino = :fechaTermino")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByNombre", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByNivelProdep", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.nivelProdep = :nivelProdep")
    , @NamedQuery(name = "CuerposAcademicosRegistro.findByArea", query = "SELECT c FROM CuerposAcademicosRegistro c WHERE c.area = :area")})
public class CuerposAcademicosRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cuerpo_academico")
    private String cuerpoAcademico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_termino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "nivel_prodep")
    private String nivelProdep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuerpoAcademico")
    private List<CuerpacadIntegrantes> cuerpacadIntegrantesList;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "area_estudio", referencedColumnName = "area_estudio")
    @ManyToOne(optional = false)
    private CuerpacadAreasEstudio areaEstudio;
    @JoinColumn(name = "disciplina", referencedColumnName = "disciplina")
    @ManyToOne(optional = false)
    private CuerpacadDisciplinas disciplina;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuerposAcademicosRegistro")
    private List<CuerpacadLineas> cuerpacadLineasList;

    public CuerposAcademicosRegistro() {
    }

    public CuerposAcademicosRegistro(Integer registro) {
        this.registro = registro;
    }

    public CuerposAcademicosRegistro(Integer registro, String cuerpoAcademico, Date fechaInicio, Date fechaTermino, String nombre, String nivelProdep, short area) {
        this.registro = registro;
        this.cuerpoAcademico = cuerpoAcademico;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.nombre = nombre;
        this.nivelProdep = nivelProdep;
        this.area = area;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(String cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivelProdep() {
        return nivelProdep;
    }

    public void setNivelProdep(String nivelProdep) {
        this.nivelProdep = nivelProdep;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    @XmlTransient
    public List<CuerpacadIntegrantes> getCuerpacadIntegrantesList() {
        return cuerpacadIntegrantesList;
    }

    public void setCuerpacadIntegrantesList(List<CuerpacadIntegrantes> cuerpacadIntegrantesList) {
        this.cuerpacadIntegrantesList = cuerpacadIntegrantesList;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public CuerpacadAreasEstudio getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(CuerpacadAreasEstudio areaEstudio) {
        this.areaEstudio = areaEstudio;
    }

    public CuerpacadDisciplinas getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(CuerpacadDisciplinas disciplina) {
        this.disciplina = disciplina;
    }

    @XmlTransient
    public List<CuerpacadLineas> getCuerpacadLineasList() {
        return cuerpacadLineasList;
    }

    public void setCuerpacadLineasList(List<CuerpacadLineas> cuerpacadLineasList) {
        this.cuerpacadLineasList = cuerpacadLineasList;
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
        if (!(object instanceof CuerposAcademicosRegistro)) {
            return false;
        }
        CuerposAcademicosRegistro other = (CuerposAcademicosRegistro) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro[ registro=" + registro + " ]";
    }
    
}
