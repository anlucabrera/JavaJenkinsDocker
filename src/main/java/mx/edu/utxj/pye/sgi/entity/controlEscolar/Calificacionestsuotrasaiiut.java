/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "calificacionestsuotrasaiiut", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calificacionestsuotrasaiiut.findAll", query = "SELECT c FROM Calificacionestsuotrasaiiut c")
    , @NamedQuery(name = "Calificacionestsuotrasaiiut.findByIdcalificacionHistorica", query = "SELECT c FROM Calificacionestsuotrasaiiut c WHERE c.idcalificacionHistorica = :idcalificacionHistorica")
    , @NamedQuery(name = "Calificacionestsuotrasaiiut.findByValor", query = "SELECT c FROM Calificacionestsuotrasaiiut c WHERE c.valor = :valor")})
public class Calificacionestsuotrasaiiut implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcalificacionHistorica")
    private Integer idcalificacionHistorica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @JoinColumn(name = "estudianteHistoricoTSU", referencedColumnName = "estudianteHistoricoTSU")
    @ManyToOne(optional = false)
    private EstudianteHistorialTsu estudianteHistoricoTSU;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia")
    @ManyToOne(optional = false)
    private PlanEstudioMateria idPlanMateria;

    public Calificacionestsuotrasaiiut() {
    }

    public Calificacionestsuotrasaiiut(Integer idcalificacionHistorica) {
        this.idcalificacionHistorica = idcalificacionHistorica;
    }

    public Calificacionestsuotrasaiiut(Integer idcalificacionHistorica, double valor) {
        this.idcalificacionHistorica = idcalificacionHistorica;
        this.valor = valor;
    }

    public Integer getIdcalificacionHistorica() {
        return idcalificacionHistorica;
    }

    public void setIdcalificacionHistorica(Integer idcalificacionHistorica) {
        this.idcalificacionHistorica = idcalificacionHistorica;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public EstudianteHistorialTsu getEstudianteHistoricoTSU() {
        return estudianteHistoricoTSU;
    }

    public void setEstudianteHistoricoTSU(EstudianteHistorialTsu estudianteHistoricoTSU) {
        this.estudianteHistoricoTSU = estudianteHistoricoTSU;
    }

    public PlanEstudioMateria getIdPlanMateria() {
        return idPlanMateria;
    }

    public void setIdPlanMateria(PlanEstudioMateria idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcalificacionHistorica != null ? idcalificacionHistorica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calificacionestsuotrasaiiut)) {
            return false;
        }
        Calificacionestsuotrasaiiut other = (Calificacionestsuotrasaiiut) object;
        if ((this.idcalificacionHistorica == null && other.idcalificacionHistorica != null) || (this.idcalificacionHistorica != null && !this.idcalificacionHistorica.equals(other.idcalificacionHistorica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacionestsuotrasaiiut[ idcalificacionHistorica=" + idcalificacionHistorica + " ]";
    }
    
}
