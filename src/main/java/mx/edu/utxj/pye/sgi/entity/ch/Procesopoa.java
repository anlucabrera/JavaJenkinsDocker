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
@Table(name = "procesopoa", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Procesopoa.findAll", query = "SELECT p FROM Procesopoa p")
    , @NamedQuery(name = "Procesopoa.findByProcesoPOA", query = "SELECT p FROM Procesopoa p WHERE p.procesoPOA = :procesoPOA")
    , @NamedQuery(name = "Procesopoa.findByArea", query = "SELECT p FROM Procesopoa p WHERE p.area = :area")
    , @NamedQuery(name = "Procesopoa.findByResponsable", query = "SELECT p FROM Procesopoa p WHERE p.responsable = :responsable")
    , @NamedQuery(name = "Procesopoa.findByRegistroAFinalizado", query = "SELECT p FROM Procesopoa p WHERE p.registroAFinalizado = :registroAFinalizado")
    , @NamedQuery(name = "Procesopoa.findByValidacionRegistroA", query = "SELECT p FROM Procesopoa p WHERE p.validacionRegistroA = :validacionRegistroA")
    , @NamedQuery(name = "Procesopoa.findByAsiganacionRFinalizado", query = "SELECT p FROM Procesopoa p WHERE p.asiganacionRFinalizado = :asiganacionRFinalizado")
    , @NamedQuery(name = "Procesopoa.findByValidacionRFFinalizado", query = "SELECT p FROM Procesopoa p WHERE p.validacionRFFinalizado = :validacionRFFinalizado")
    , @NamedQuery(name = "Procesopoa.findByRegistroJustificacionFinalizado", query = "SELECT p FROM Procesopoa p WHERE p.registroJustificacionFinalizado = :registroJustificacionFinalizado")
    , @NamedQuery(name = "Procesopoa.findByValidacionJustificacion", query = "SELECT p FROM Procesopoa p WHERE p.validacionJustificacion = :validacionJustificacion")
    , @NamedQuery(name = "Procesopoa.findByEjercicioFiscalEtapa1", query = "SELECT p FROM Procesopoa p WHERE p.ejercicioFiscalEtapa1 = :ejercicioFiscalEtapa1")
    , @NamedQuery(name = "Procesopoa.findByEjercicioFiscalEtapa2", query = "SELECT p FROM Procesopoa p WHERE p.ejercicioFiscalEtapa2 = :ejercicioFiscalEtapa2")
    , @NamedQuery(name = "Procesopoa.findByActivaEtapa1", query = "SELECT p FROM Procesopoa p WHERE p.activaEtapa1 = :activaEtapa1")
    , @NamedQuery(name = "Procesopoa.findByActivaEtapa2", query = "SELECT p FROM Procesopoa p WHERE p.activaEtapa2 = :activaEtapa2")})
public class Procesopoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "procesoPOA")
    private Integer procesoPOA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Column(name = "responsable")
    private Integer responsable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registroAFinalizado")
    private boolean registroAFinalizado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacionRegistroA")
    private boolean validacionRegistroA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asiganacionRFinalizado")
    private boolean asiganacionRFinalizado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacionRFFinalizado")
    private boolean validacionRFFinalizado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registroJustificacionFinalizado")
    private boolean registroJustificacionFinalizado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacionJustificacion")
    private boolean validacionJustificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ejercicioFiscalEtapa1")
    private short ejercicioFiscalEtapa1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ejercicioFiscalEtapa2")
    private short ejercicioFiscalEtapa2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoPOA")
    private List<Permisosevaluacionpoaex> permisosevaluacionpoaexList;
    @Column(name = "activaEtapa1")
    private Boolean activaEtapa1;
    @Column(name = "activaEtapa2")
    private Boolean activaEtapa2;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacionPOA")
    @ManyToOne(fetch = FetchType.LAZY)
    private Calendarioevaluacionpoa evaluacion;

    public Procesopoa() {
    }

    public Procesopoa(Integer procesoPOA) {
        this.procesoPOA = procesoPOA;
    }

    public Procesopoa(Integer procesoPOA, short area, Integer responsable, boolean registroAFinalizado, boolean validacionRegistroA, boolean asiganacionRFinalizado, boolean validacionRFFinalizado, boolean registroJustificacionFinalizado, boolean validacionJustificacion, short ejercicioFiscalEtapa1, short ejercicioFiscalEtapa2, Boolean activaEtapa1, Boolean activaEtapa2, Calendarioevaluacionpoa evaluacion) {
        this.procesoPOA = procesoPOA;
        this.area = area;
        this.responsable = responsable;
        this.registroAFinalizado = registroAFinalizado;
        this.validacionRegistroA = validacionRegistroA;
        this.asiganacionRFinalizado = asiganacionRFinalizado;
        this.validacionRFFinalizado = validacionRFFinalizado;
        this.registroJustificacionFinalizado = registroJustificacionFinalizado;
        this.validacionJustificacion = validacionJustificacion;
        this.ejercicioFiscalEtapa1 = ejercicioFiscalEtapa1;
        this.ejercicioFiscalEtapa2 = ejercicioFiscalEtapa2;
        this.activaEtapa1 = activaEtapa1;
        this.activaEtapa2 = activaEtapa2;
        this.evaluacion = evaluacion;
    }

    public Integer getProcesoPOA() {
        return procesoPOA;
    }

    public void setProcesoPOA(Integer procesoPOA) {
        this.procesoPOA = procesoPOA;
    }


    public Integer getResponsable() {
        return responsable;
    }

    public void setResponsable(Integer responsable) {
        this.responsable = responsable;
    }


    public Boolean getActivaEtapa1() {
        return activaEtapa1;
    }

    public void setActivaEtapa1(Boolean activaEtapa1) {
        this.activaEtapa1 = activaEtapa1;
    }

    public Boolean getActivaEtapa2() {
        return activaEtapa2;
    }

    public void setActivaEtapa2(Boolean activaEtapa2) {
        this.activaEtapa2 = activaEtapa2;
    }

    public Calendarioevaluacionpoa getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Calendarioevaluacionpoa evaluacion) {
        this.evaluacion = evaluacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procesoPOA != null ? procesoPOA.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Procesopoa)) {
            return false;
        }
        Procesopoa other = (Procesopoa) object;
        if ((this.procesoPOA == null && other.procesoPOA != null) || (this.procesoPOA != null && !this.procesoPOA.equals(other.procesoPOA))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Procesopoa[ procesoPOA=" + procesoPOA + " ]";
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public boolean getRegistroAFinalizado() {
        return registroAFinalizado;
    }

    public void setRegistroAFinalizado(boolean registroAFinalizado) {
        this.registroAFinalizado = registroAFinalizado;
    }

    public boolean getValidacionRegistroA() {
        return validacionRegistroA;
    }

    public void setValidacionRegistroA(boolean validacionRegistroA) {
        this.validacionRegistroA = validacionRegistroA;
    }

    public boolean getAsiganacionRFinalizado() {
        return asiganacionRFinalizado;
    }

    public void setAsiganacionRFinalizado(boolean asiganacionRFinalizado) {
        this.asiganacionRFinalizado = asiganacionRFinalizado;
    }

    public boolean getValidacionRFFinalizado() {
        return validacionRFFinalizado;
    }

    public void setValidacionRFFinalizado(boolean validacionRFFinalizado) {
        this.validacionRFFinalizado = validacionRFFinalizado;
    }

    public boolean getRegistroJustificacionFinalizado() {
        return registroJustificacionFinalizado;
    }

    public void setRegistroJustificacionFinalizado(boolean registroJustificacionFinalizado) {
        this.registroJustificacionFinalizado = registroJustificacionFinalizado;
    }

    public boolean getValidacionJustificacion() {
        return validacionJustificacion;
    }

    public void setValidacionJustificacion(boolean validacionJustificacion) {
        this.validacionJustificacion = validacionJustificacion;
    }

    public short getEjercicioFiscalEtapa1() {
        return ejercicioFiscalEtapa1;
    }

    public void setEjercicioFiscalEtapa1(short ejercicioFiscalEtapa1) {
        this.ejercicioFiscalEtapa1 = ejercicioFiscalEtapa1;
    }

    public short getEjercicioFiscalEtapa2() {
        return ejercicioFiscalEtapa2;
    }

    public void setEjercicioFiscalEtapa2(short ejercicioFiscalEtapa2) {
        this.ejercicioFiscalEtapa2 = ejercicioFiscalEtapa2;
    }

    @XmlTransient
    public List<Permisosevaluacionpoaex> getPermisosevaluacionpoaexList() {
        return permisosevaluacionpoaexList;
    }

    public void setPermisosevaluacionpoaexList(List<Permisosevaluacionpoaex> permisosevaluacionpoaexList) {
        this.permisosevaluacionpoaexList = permisosevaluacionpoaexList;
    }
    
}
