/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "organismos_evaluadores", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganismosEvaluadores.findAll", query = "SELECT o FROM OrganismosEvaluadores o")
    , @NamedQuery(name = "OrganismosEvaluadores.findBySiglas", query = "SELECT o FROM OrganismosEvaluadores o WHERE o.siglas = :siglas")
    , @NamedQuery(name = "OrganismosEvaluadores.findByNombre", query = "SELECT o FROM OrganismosEvaluadores o WHERE o.nombre = :nombre")
    , @NamedQuery(name = "OrganismosEvaluadores.findByPagina", query = "SELECT o FROM OrganismosEvaluadores o WHERE o.pagina = :pagina")})
public class OrganismosEvaluadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 100)
    @Column(name = "pagina")
    private String pagina;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organismo")
    private List<ProgramasEducativosOrganismosEvaluadores> programasEducativosOrganismosEvaluadoresList;

    public OrganismosEvaluadores() {
    }

    public OrganismosEvaluadores(String siglas) {
        this.siglas = siglas;
    }

    public OrganismosEvaluadores(String siglas, String nombre) {
        this.siglas = siglas;
        this.nombre = nombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    @XmlTransient
    public List<ProgramasEducativosOrganismosEvaluadores> getProgramasEducativosOrganismosEvaluadoresList() {
        return programasEducativosOrganismosEvaluadoresList;
    }

    public void setProgramasEducativosOrganismosEvaluadoresList(List<ProgramasEducativosOrganismosEvaluadores> programasEducativosOrganismosEvaluadoresList) {
        this.programasEducativosOrganismosEvaluadoresList = programasEducativosOrganismosEvaluadoresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siglas != null ? siglas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganismosEvaluadores)) {
            return false;
        }
        OrganismosEvaluadores other = (OrganismosEvaluadores) object;
        if ((this.siglas == null && other.siglas != null) || (this.siglas != null && !this.siglas.equals(other.siglas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.OrganismosEvaluadores[ siglas=" + siglas + " ]";
    }
    
}
