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
@Table(name = "participantes_actividades_formacion_integral", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipantesActividadesFormacionIntegral.findAll", query = "SELECT p FROM ParticipantesActividadesFormacionIntegral p")
    , @NamedQuery(name = "ParticipantesActividadesFormacionIntegral.findByRegistro", query = "SELECT p FROM ParticipantesActividadesFormacionIntegral p WHERE p.registro = :registro")})
public class ParticipantesActividadesFormacionIntegral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @JoinColumn(name = "actividad_formacion_integral", referencedColumnName = "actividad_formacion_integral")
    @ManyToOne(optional = false)
    private ActividadesFormacionIntegral actividadFormacionIntegral;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo_escolar", referencedColumnName = "periodo")})
    @ManyToOne(optional = false)
    private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ParticipantesActividadesFormacionIntegral() {
    }

    public ParticipantesActividadesFormacionIntegral(Integer registro) {
        this.registro = registro;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public ActividadesFormacionIntegral getActividadFormacionIntegral() {
        return actividadFormacionIntegral;
    }

    public void setActividadFormacionIntegral(ActividadesFormacionIntegral actividadFormacionIntegral) {
        this.actividadFormacionIntegral = actividadFormacionIntegral;
    }

    public MatriculaPeriodosEscolares getMatriculaPeriodosEscolares() {
        return matriculaPeriodosEscolares;
    }

    public void setMatriculaPeriodosEscolares(MatriculaPeriodosEscolares matriculaPeriodosEscolares) {
        this.matriculaPeriodosEscolares = matriculaPeriodosEscolares;
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
        if (!(object instanceof ParticipantesActividadesFormacionIntegral)) {
            return false;
        }
        ParticipantesActividadesFormacionIntegral other = (ParticipantesActividadesFormacionIntegral) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesActividadesFormacionIntegral[ registro=" + registro + " ]";
    }
    
}
