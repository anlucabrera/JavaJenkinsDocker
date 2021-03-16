/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "coordinador_area_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoordinadorAreaEstadia.findAll", query = "SELECT c FROM CoordinadorAreaEstadia c")
    , @NamedQuery(name = "CoordinadorAreaEstadia.findByCoordinadorEstadia", query = "SELECT c FROM CoordinadorAreaEstadia c WHERE c.coordinadorEstadia = :coordinadorEstadia")
    , @NamedQuery(name = "CoordinadorAreaEstadia.findByAreaAcademica", query = "SELECT c FROM CoordinadorAreaEstadia c WHERE c.areaAcademica = :areaAcademica")
    , @NamedQuery(name = "CoordinadorAreaEstadia.findByPersonal", query = "SELECT c FROM CoordinadorAreaEstadia c WHERE c.personal = :personal")})
public class CoordinadorAreaEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "coordinador_estadia")
    private Integer coordinadorEstadia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_academica")
    private short areaAcademica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @OneToMany(mappedBy = "coordinador", fetch = FetchType.LAZY)
    private List<AsesorAcademicoEstadia> asesorAcademicoEstadiaList;

    public CoordinadorAreaEstadia() {
    }

    public CoordinadorAreaEstadia(Integer coordinadorEstadia) {
        this.coordinadorEstadia = coordinadorEstadia;
    }

    public CoordinadorAreaEstadia(Integer coordinadorEstadia, short areaAcademica, int personal) {
        this.coordinadorEstadia = coordinadorEstadia;
        this.areaAcademica = areaAcademica;
        this.personal = personal;
    }

    public Integer getCoordinadorEstadia() {
        return coordinadorEstadia;
    }

    public void setCoordinadorEstadia(Integer coordinadorEstadia) {
        this.coordinadorEstadia = coordinadorEstadia;
    }

    public short getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(short areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public EventoEstadia getEvento() {
        return evento;
    }

    public void setEvento(EventoEstadia evento) {
        this.evento = evento;
    }

    @XmlTransient
    public List<AsesorAcademicoEstadia> getAsesorAcademicoEstadiaList() {
        return asesorAcademicoEstadiaList;
    }

    public void setAsesorAcademicoEstadiaList(List<AsesorAcademicoEstadia> asesorAcademicoEstadiaList) {
        this.asesorAcademicoEstadiaList = asesorAcademicoEstadiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coordinadorEstadia != null ? coordinadorEstadia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoordinadorAreaEstadia)) {
            return false;
        }
        CoordinadorAreaEstadia other = (CoordinadorAreaEstadia) object;
        if ((this.coordinadorEstadia == null && other.coordinadorEstadia != null) || (this.coordinadorEstadia != null && !this.coordinadorEstadia.equals(other.coordinadorEstadia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CoordinadorAreaEstadia[ coordinadorEstadia=" + coordinadorEstadia + " ]";
    }
    
}
