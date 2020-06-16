/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "tarea_integradora_promedio", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TareaIntegradoraPromedio.findAll", query = "SELECT t FROM TareaIntegradoraPromedio t")
    , @NamedQuery(name = "TareaIntegradoraPromedio.findByIdTareaIntegradora", query = "SELECT t FROM TareaIntegradoraPromedio t WHERE t.tareaIntegradoraPromedioPK.idTareaIntegradora = :idTareaIntegradora")
    , @NamedQuery(name = "TareaIntegradoraPromedio.findByIdEstudiante", query = "SELECT t FROM TareaIntegradoraPromedio t WHERE t.tareaIntegradoraPromedioPK.idEstudiante = :idEstudiante")
    , @NamedQuery(name = "TareaIntegradoraPromedio.findByValor", query = "SELECT t FROM TareaIntegradoraPromedio t WHERE t.valor = :valor")})
public class TareaIntegradoraPromedio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TareaIntegradoraPromedioPK tareaIntegradoraPromedioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @JoinColumn(name = "id_tarea_integradora", referencedColumnName = "id_tarea_integradora", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TareaIntegradora tareaIntegradora;

    public TareaIntegradoraPromedio() {
    }

    public TareaIntegradoraPromedio(TareaIntegradoraPromedioPK tareaIntegradoraPromedioPK) {
        this.tareaIntegradoraPromedioPK = tareaIntegradoraPromedioPK;
    }

    public TareaIntegradoraPromedio(TareaIntegradoraPromedioPK tareaIntegradoraPromedioPK, double valor) {
        this.tareaIntegradoraPromedioPK = tareaIntegradoraPromedioPK;
        this.valor = valor;
    }

    public TareaIntegradoraPromedio(int idTareaIntegradora, int idEstudiante) {
        this.tareaIntegradoraPromedioPK = new TareaIntegradoraPromedioPK(idTareaIntegradora, idEstudiante);
    }

    public TareaIntegradoraPromedioPK getTareaIntegradoraPromedioPK() {
        return tareaIntegradoraPromedioPK;
    }

    public void setTareaIntegradoraPromedioPK(TareaIntegradoraPromedioPK tareaIntegradoraPromedioPK) {
        this.tareaIntegradoraPromedioPK = tareaIntegradoraPromedioPK;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public TareaIntegradora getTareaIntegradora() {
        return tareaIntegradora;
    }

    public void setTareaIntegradora(TareaIntegradora tareaIntegradora) {
        this.tareaIntegradora = tareaIntegradora;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tareaIntegradoraPromedioPK != null ? tareaIntegradoraPromedioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TareaIntegradoraPromedio)) {
            return false;
        }
        TareaIntegradoraPromedio other = (TareaIntegradoraPromedio) object;
        if ((this.tareaIntegradoraPromedioPK == null && other.tareaIntegradoraPromedioPK != null) || (this.tareaIntegradoraPromedioPK != null && !this.tareaIntegradoraPromedioPK.equals(other.tareaIntegradoraPromedioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio[ tareaIntegradoraPromedioPK=" + tareaIntegradoraPromedioPK + " ]";
    }
    
}
