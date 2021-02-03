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
 * @author Desarrollo
 */
@Entity
@Table(name = "asesor_academico_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsesorAcademicoEstadia.findAll", query = "SELECT a FROM AsesorAcademicoEstadia a")
    , @NamedQuery(name = "AsesorAcademicoEstadia.findByAsesorEstadia", query = "SELECT a FROM AsesorAcademicoEstadia a WHERE a.asesorEstadia = :asesorEstadia")
    , @NamedQuery(name = "AsesorAcademicoEstadia.findByProgramaEducativo", query = "SELECT a FROM AsesorAcademicoEstadia a WHERE a.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "AsesorAcademicoEstadia.findByPersonal", query = "SELECT a FROM AsesorAcademicoEstadia a WHERE a.personal = :personal")})
public class AsesorAcademicoEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "asesor_estadia")
    private Integer asesorEstadia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @JoinColumn(name = "coordinador", referencedColumnName = "coordinador_estadia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CoordinadorAreaEstadia coordinador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "asesor", fetch = FetchType.LAZY)
    private List<SeguimientoEstadiaEstudiante> seguimientoEstadiaEstudianteList;

    public AsesorAcademicoEstadia() {
    }

    public AsesorAcademicoEstadia(Integer asesorEstadia) {
        this.asesorEstadia = asesorEstadia;
    }

    public AsesorAcademicoEstadia(Integer asesorEstadia, short programaEducativo, int personal) {
        this.asesorEstadia = asesorEstadia;
        this.programaEducativo = programaEducativo;
        this.personal = personal;
    }

    public Integer getAsesorEstadia() {
        return asesorEstadia;
    }

    public void setAsesorEstadia(Integer asesorEstadia) {
        this.asesorEstadia = asesorEstadia;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
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

    public CoordinadorAreaEstadia getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(CoordinadorAreaEstadia coordinador) {
        this.coordinador = coordinador;
    }

    @XmlTransient
    public List<SeguimientoEstadiaEstudiante> getSeguimientoEstadiaEstudianteList() {
        return seguimientoEstadiaEstudianteList;
    }

    public void setSeguimientoEstadiaEstudianteList(List<SeguimientoEstadiaEstudiante> seguimientoEstadiaEstudianteList) {
        this.seguimientoEstadiaEstudianteList = seguimientoEstadiaEstudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asesorEstadia != null ? asesorEstadia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsesorAcademicoEstadia)) {
            return false;
        }
        AsesorAcademicoEstadia other = (AsesorAcademicoEstadia) object;
        if ((this.asesorEstadia == null && other.asesorEstadia != null) || (this.asesorEstadia != null && !this.asesorEstadia.equals(other.asesorEstadia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia[ asesorEstadia=" + asesorEstadia + " ]";
    }
    
}
