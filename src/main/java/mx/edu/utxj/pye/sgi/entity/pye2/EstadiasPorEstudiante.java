/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "estadias_por_estudiante", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadiasPorEstudiante.findAll", query = "SELECT e FROM EstadiasPorEstudiante e")
    , @NamedQuery(name = "EstadiasPorEstudiante.findByRegistro", query = "SELECT e FROM EstadiasPorEstudiante e WHERE e.registro = :registro")
    , @NamedQuery(name = "EstadiasPorEstudiante.findByCicloEscolar", query = "SELECT e FROM EstadiasPorEstudiante e WHERE e.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "EstadiasPorEstudiante.findByGeneracion", query = "SELECT e FROM EstadiasPorEstudiante e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EstadiasPorEstudiante.findByProgramaEducativo", query = "SELECT e FROM EstadiasPorEstudiante e WHERE e.programaEducativo = :programaEducativo")})
public class EstadiasPorEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrganismosVinculados empresa;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo_escolar", referencedColumnName = "periodo")})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MatriculaPeriodosEscolares matriculaPeriodosEscolares;

    public EstadiasPorEstudiante() {
    }

    public EstadiasPorEstudiante(Integer registro) {
        this.registro = registro;
    }

    public EstadiasPorEstudiante(Integer registro, int cicloEscolar, short generacion, short programaEducativo) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.generacion = generacion;
        this.programaEducativo = programaEducativo;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public OrganismosVinculados getEmpresa() {
        return empresa;
    }

    public void setEmpresa(OrganismosVinculados empresa) {
        this.empresa = empresa;
    }

    public MatriculaPeriodosEscolares getMatriculaPeriodosEscolares() {
        return matriculaPeriodosEscolares;
    }

    public void setMatriculaPeriodosEscolares(MatriculaPeriodosEscolares matriculaPeriodosEscolares) {
        this.matriculaPeriodosEscolares = matriculaPeriodosEscolares;
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
        if (!(object instanceof EstadiasPorEstudiante)) {
            return false;
        }
        EstadiasPorEstudiante other = (EstadiasPorEstudiante) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EstadiasPorEstudiante[ registro=" + registro + " ]";
    }
    
}
