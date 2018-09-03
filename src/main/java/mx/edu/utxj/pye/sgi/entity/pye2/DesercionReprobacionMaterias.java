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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "desercion_reprobacion_materias", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesercionReprobacionMaterias.findAll", query = "SELECT d FROM DesercionReprobacionMaterias d")
    , @NamedQuery(name = "DesercionReprobacionMaterias.findByRegistro", query = "SELECT d FROM DesercionReprobacionMaterias d WHERE d.registro = :registro")
    , @NamedQuery(name = "DesercionReprobacionMaterias.findByAsignatura", query = "SELECT d FROM DesercionReprobacionMaterias d WHERE d.asignatura = :asignatura")
    , @NamedQuery(name = "DesercionReprobacionMaterias.findByDocente", query = "SELECT d FROM DesercionReprobacionMaterias d WHERE d.docente = :docente")})
public class DesercionReprobacionMaterias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "asignatura")
    private String asignatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente")
    private int docente;
    @JoinColumn(name = "dpe", referencedColumnName = "dpe")
    @ManyToOne(optional = false)
    private DesercionPeriodosEscolares dpe;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public DesercionReprobacionMaterias() {
    }

    public DesercionReprobacionMaterias(Integer registro) {
        this.registro = registro;
    }

    public DesercionReprobacionMaterias(Integer registro, String asignatura, int docente) {
        this.registro = registro;
        this.asignatura = asignatura;
        this.docente = docente;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    public DesercionPeriodosEscolares getDpe() {
        return dpe;
    }

    public void setDpe(DesercionPeriodosEscolares dpe) {
        this.dpe = dpe;
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
        if (!(object instanceof DesercionReprobacionMaterias)) {
            return false;
        }
        DesercionReprobacionMaterias other = (DesercionReprobacionMaterias) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias[ registro=" + registro + " ]";
    }
    
}
