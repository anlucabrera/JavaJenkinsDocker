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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "cuerpacad_disciplinas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadDisciplinas.findAll", query = "SELECT c FROM CuerpacadDisciplinas c")
    , @NamedQuery(name = "CuerpacadDisciplinas.findByDisciplina", query = "SELECT c FROM CuerpacadDisciplinas c WHERE c.disciplina = :disciplina")
    , @NamedQuery(name = "CuerpacadDisciplinas.findByNombre", query = "SELECT c FROM CuerpacadDisciplinas c WHERE c.nombre = :nombre")})
public class CuerpacadDisciplinas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "disciplina")
    private Short disciplina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina")
    private List<CuerposAcademicosRegistro> cuerposAcademicosRegistroList;

    public CuerpacadDisciplinas() {
    }

    public CuerpacadDisciplinas(Short disciplina) {
        this.disciplina = disciplina;
    }

    public CuerpacadDisciplinas(Short disciplina, String nombre) {
        this.disciplina = disciplina;
        this.nombre = nombre;
    }

    public Short getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Short disciplina) {
        this.disciplina = disciplina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<CuerposAcademicosRegistro> getCuerposAcademicosRegistroList() {
        return cuerposAcademicosRegistroList;
    }

    public void setCuerposAcademicosRegistroList(List<CuerposAcademicosRegistro> cuerposAcademicosRegistroList) {
        this.cuerposAcademicosRegistroList = cuerposAcademicosRegistroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (disciplina != null ? disciplina.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpacadDisciplinas)) {
            return false;
        }
        CuerpacadDisciplinas other = (CuerpacadDisciplinas) object;
        if ((this.disciplina == null && other.disciplina != null) || (this.disciplina != null && !this.disciplina.equals(other.disciplina))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadDisciplinas[ disciplina=" + disciplina + " ]";
    }
    
}
