/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "estudiantes_claves", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstudiantesClaves.findAll", query = "SELECT e FROM EstudiantesClaves e")
    , @NamedQuery(name = "EstudiantesClaves.findByClave", query = "SELECT e FROM EstudiantesClaves e WHERE e.clave = :clave")
    , @NamedQuery(name = "EstudiantesClaves.findByRegistro", query = "SELECT e FROM EstudiantesClaves e WHERE e.registro = :registro")
    , @NamedQuery(name = "EstudiantesClaves.findByIdEstudiante", query = "SELECT e FROM EstudiantesClaves e WHERE e.idEstudiante = :idEstudiante")})
public class EstudiantesClaves implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "clave")
    private Integer clave;
    @Column(name = "registro")
    private Integer registro;
    @Column(name = "id_estudiante")
    private Integer idEstudiante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiantesClaves")
    private List<EvaluacionTutoresResultados> evaluacionTutoresResultadosList;

    public EstudiantesClaves() {
    }

    public EstudiantesClaves(Integer clave) {
        this.clave = clave;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @XmlTransient
    public List<EvaluacionTutoresResultados> getEvaluacionTutoresResultadosList() {
        return evaluacionTutoresResultadosList;
    }

    public void setEvaluacionTutoresResultadosList(List<EvaluacionTutoresResultados> evaluacionTutoresResultadosList) {
        this.evaluacionTutoresResultadosList = evaluacionTutoresResultadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstudiantesClaves)) {
            return false;
        }
        EstudiantesClaves other = (EstudiantesClaves) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EstudiantesClaves[ clave=" + clave + " ]";
    }
    
}
