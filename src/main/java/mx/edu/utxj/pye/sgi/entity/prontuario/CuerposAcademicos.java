/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpos_academicos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerposAcademicos.findAll", query = "SELECT c FROM CuerposAcademicos c")
    , @NamedQuery(name = "CuerposAcademicos.findByClaveRegistro", query = "SELECT c FROM CuerposAcademicos c WHERE c.claveRegistro = :claveRegistro")
    , @NamedQuery(name = "CuerposAcademicos.findByFechaInicioVig", query = "SELECT c FROM CuerposAcademicos c WHERE c.fechaInicioVig = :fechaInicioVig")
    , @NamedQuery(name = "CuerposAcademicos.findByFechaTerVig", query = "SELECT c FROM CuerposAcademicos c WHERE c.fechaTerVig = :fechaTerVig")
    , @NamedQuery(name = "CuerposAcademicos.findByCuerpoAcademico", query = "SELECT c FROM CuerposAcademicos c WHERE c.cuerpoAcademico = :cuerpoAcademico")
    , @NamedQuery(name = "CuerposAcademicos.findByNivRecProdep", query = "SELECT c FROM CuerposAcademicos c WHERE c.nivRecProdep = :nivRecProdep")
    , @NamedQuery(name = "CuerposAcademicos.findByAreaAcademica", query = "SELECT c FROM CuerposAcademicos c WHERE c.areaAcademica = :areaAcademica")
    , @NamedQuery(name = "CuerposAcademicos.findByAreaEstudio", query = "SELECT c FROM CuerposAcademicos c WHERE c.areaEstudio = :areaEstudio")
    , @NamedQuery(name = "CuerposAcademicos.findByDisciplina", query = "SELECT c FROM CuerposAcademicos c WHERE c.disciplina = :disciplina")
    , @NamedQuery(name = "CuerposAcademicos.findByOrden", query = "SELECT c FROM CuerposAcademicos c WHERE c.orden = :orden")})
public class CuerposAcademicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "clave_registro")
    private String claveRegistro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio_vig")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioVig;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ter_vig")
    @Temporal(TemporalType.DATE)
    private Date fechaTerVig;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "cuerpo_academico")
    private String cuerpoAcademico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "niv_rec_prodep")
    private String nivRecProdep;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "area_academica")
    private String areaAcademica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "area_estudio")
    private String areaEstudio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "disciplina")
    private String disciplina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private short orden;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveRegistro", fetch = FetchType.LAZY)
    private List<CuerposAcademicosLineasInvestigacion> cuerposAcademicosLineasInvestigacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveRegistro", fetch = FetchType.LAZY)
    private List<CuerposAcademicosIntegrantes> cuerposAcademicosIntegrantesList;

    public CuerposAcademicos() {
    }

    public CuerposAcademicos(String claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    public CuerposAcademicos(String claveRegistro, Date fechaInicioVig, Date fechaTerVig, String cuerpoAcademico, String nivRecProdep, String areaAcademica, String areaEstudio, String disciplina, short orden) {
        this.claveRegistro = claveRegistro;
        this.fechaInicioVig = fechaInicioVig;
        this.fechaTerVig = fechaTerVig;
        this.cuerpoAcademico = cuerpoAcademico;
        this.nivRecProdep = nivRecProdep;
        this.areaAcademica = areaAcademica;
        this.areaEstudio = areaEstudio;
        this.disciplina = disciplina;
        this.orden = orden;
    }

    public String getClaveRegistro() {
        return claveRegistro;
    }

    public void setClaveRegistro(String claveRegistro) {
        this.claveRegistro = claveRegistro;
    }

    public Date getFechaInicioVig() {
        return fechaInicioVig;
    }

    public void setFechaInicioVig(Date fechaInicioVig) {
        this.fechaInicioVig = fechaInicioVig;
    }

    public Date getFechaTerVig() {
        return fechaTerVig;
    }

    public void setFechaTerVig(Date fechaTerVig) {
        this.fechaTerVig = fechaTerVig;
    }

    public String getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(String cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    public String getNivRecProdep() {
        return nivRecProdep;
    }

    public void setNivRecProdep(String nivRecProdep) {
        this.nivRecProdep = nivRecProdep;
    }

    public String getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(String areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    public String getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(String areaEstudio) {
        this.areaEstudio = areaEstudio;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public short getOrden() {
        return orden;
    }

    public void setOrden(short orden) {
        this.orden = orden;
    }

    @XmlTransient
    public List<CuerposAcademicosLineasInvestigacion> getCuerposAcademicosLineasInvestigacionList() {
        return cuerposAcademicosLineasInvestigacionList;
    }

    public void setCuerposAcademicosLineasInvestigacionList(List<CuerposAcademicosLineasInvestigacion> cuerposAcademicosLineasInvestigacionList) {
        this.cuerposAcademicosLineasInvestigacionList = cuerposAcademicosLineasInvestigacionList;
    }

    @XmlTransient
    public List<CuerposAcademicosIntegrantes> getCuerposAcademicosIntegrantesList() {
        return cuerposAcademicosIntegrantesList;
    }

    public void setCuerposAcademicosIntegrantesList(List<CuerposAcademicosIntegrantes> cuerposAcademicosIntegrantesList) {
        this.cuerposAcademicosIntegrantesList = cuerposAcademicosIntegrantesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveRegistro != null ? claveRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerposAcademicos)) {
            return false;
        }
        CuerposAcademicos other = (CuerposAcademicos) object;
        if ((this.claveRegistro == null && other.claveRegistro != null) || (this.claveRegistro != null && !this.claveRegistro.equals(other.claveRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CuerposAcademicos[ claveRegistro=" + claveRegistro + " ]";
    }
    
}
