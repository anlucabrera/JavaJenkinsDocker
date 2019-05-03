/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "carga_academica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargaAcademica.findAll", query = "SELECT c FROM CargaAcademica c"),
    @NamedQuery(name = "CargaAcademica.findByIdCargaAcademica", query = "SELECT c FROM CargaAcademica c WHERE c.cargaAcademicaPK.idCargaAcademica = :idCargaAcademica"),
    @NamedQuery(name = "CargaAcademica.findByCveGrupo", query = "SELECT c FROM CargaAcademica c WHERE c.cargaAcademicaPK.cveGrupo = :cveGrupo"),
    @NamedQuery(name = "CargaAcademica.findByCveMateria", query = "SELECT c FROM CargaAcademica c WHERE c.cargaAcademicaPK.cveMateria = :cveMateria"),
    @NamedQuery(name = "CargaAcademica.findByDocente", query = "SELECT c FROM CargaAcademica c WHERE c.docente = :docente")})
public class CargaAcademica implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CargaAcademicaPK cargaAcademicaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente")
    private int docente;
    @JoinColumn(name = "cve_grupo", referencedColumnName = "id_grupo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupo;
    @JoinColumn(name = "cve_materia", referencedColumnName = "id_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materia materia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargaAcademica1")
    private List<ConfiguracionMateria> configuracionMateriaList;

    public CargaAcademica() {
    }

    public CargaAcademica(CargaAcademicaPK cargaAcademicaPK) {
        this.cargaAcademicaPK = cargaAcademicaPK;
    }

    public CargaAcademica(CargaAcademicaPK cargaAcademicaPK, int docente) {
        this.cargaAcademicaPK = cargaAcademicaPK;
        this.docente = docente;
    }

    public CargaAcademica(int idCargaAcademica, int cveGrupo, int cveMateria) {
        this.cargaAcademicaPK = new CargaAcademicaPK(idCargaAcademica, cveGrupo, cveMateria);
    }

    public CargaAcademicaPK getCargaAcademicaPK() {
        return cargaAcademicaPK;
    }

    public void setCargaAcademicaPK(CargaAcademicaPK cargaAcademicaPK) {
        this.cargaAcademicaPK = cargaAcademicaPK;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    @XmlTransient
    public List<ConfiguracionMateria> getConfiguracionMateriaList() {
        return configuracionMateriaList;
    }

    public void setConfiguracionMateriaList(List<ConfiguracionMateria> configuracionMateriaList) {
        this.configuracionMateriaList = configuracionMateriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cargaAcademicaPK != null ? cargaAcademicaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaAcademica)) {
            return false;
        }
        CargaAcademica other = (CargaAcademica) object;
        if ((this.cargaAcademicaPK == null && other.cargaAcademicaPK != null) || (this.cargaAcademicaPK != null && !this.cargaAcademicaPK.equals(other.cargaAcademicaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica[ cargaAcademicaPK=" + cargaAcademicaPK + " ]";
    }
    
}
