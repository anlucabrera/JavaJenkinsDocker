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
@Table(name = "registro_movilidad_estudiante", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroMovilidadEstudiante.findAll", query = "SELECT r FROM RegistroMovilidadEstudiante r")
    , @NamedQuery(name = "RegistroMovilidadEstudiante.findByRegistro", query = "SELECT r FROM RegistroMovilidadEstudiante r WHERE r.registro = :registro")})
public class RegistroMovilidadEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo_escolar", referencedColumnName = "periodo")})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @JoinColumn(name = "registro_movilidad", referencedColumnName = "registro_movilidad")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RegistrosMovilidad registroMovilidad;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public RegistroMovilidadEstudiante() {
    }

    public RegistroMovilidadEstudiante(Integer registro) {
        this.registro = registro;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public MatriculaPeriodosEscolares getMatriculaPeriodosEscolares() {
        return matriculaPeriodosEscolares;
    }

    public void setMatriculaPeriodosEscolares(MatriculaPeriodosEscolares matriculaPeriodosEscolares) {
        this.matriculaPeriodosEscolares = matriculaPeriodosEscolares;
    }

    public RegistrosMovilidad getRegistroMovilidad() {
        return registroMovilidad;
    }

    public void setRegistroMovilidad(RegistrosMovilidad registroMovilidad) {
        this.registroMovilidad = registroMovilidad;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
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
        if (!(object instanceof RegistroMovilidadEstudiante)) {
            return false;
        }
        RegistroMovilidadEstudiante other = (RegistroMovilidadEstudiante) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadEstudiante[ registro=" + registro + " ]";
    }
    
}
