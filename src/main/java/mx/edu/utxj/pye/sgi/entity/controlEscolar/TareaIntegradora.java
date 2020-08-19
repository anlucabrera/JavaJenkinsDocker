/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "tarea_integradora", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TareaIntegradora.findAll", query = "SELECT t FROM TareaIntegradora t")
    , @NamedQuery(name = "TareaIntegradora.findByIdTareaIntegradora", query = "SELECT t FROM TareaIntegradora t WHERE t.idTareaIntegradora = :idTareaIntegradora")
    , @NamedQuery(name = "TareaIntegradora.findByDescripcion", query = "SELECT t FROM TareaIntegradora t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "TareaIntegradora.findByFechaEntrega", query = "SELECT t FROM TareaIntegradora t WHERE t.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "TareaIntegradora.findByPorcentaje", query = "SELECT t FROM TareaIntegradora t WHERE t.porcentaje = :porcentaje")})
public class TareaIntegradora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tarea_integradora")
    private Integer idTareaIntegradora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @JoinColumn(name = "carga", referencedColumnName = "carga")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private CargaAcademica carga;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tareaIntegradora", fetch = FetchType.LAZY)
    private List<TareaIntegradoraPromedio> tareaIntegradoraPromedioList;

    public TareaIntegradora() {
    }

    public TareaIntegradora(Integer idTareaIntegradora) {
        this.idTareaIntegradora = idTareaIntegradora;
    }

    public TareaIntegradora(Integer idTareaIntegradora, String descripcion, Date fechaEntrega, double porcentaje) {
        this.idTareaIntegradora = idTareaIntegradora;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.porcentaje = porcentaje;
    }

    public Integer getIdTareaIntegradora() {
        return idTareaIntegradora;
    }

    public void setIdTareaIntegradora(Integer idTareaIntegradora) {
        this.idTareaIntegradora = idTareaIntegradora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public CargaAcademica getCarga() {
        return carga;
    }

    public void setCarga(CargaAcademica carga) {
        this.carga = carga;
    }

    @XmlTransient
    public List<TareaIntegradoraPromedio> getTareaIntegradoraPromedioList() {
        return tareaIntegradoraPromedioList;
    }

    public void setTareaIntegradoraPromedioList(List<TareaIntegradoraPromedio> tareaIntegradoraPromedioList) {
        this.tareaIntegradoraPromedioList = tareaIntegradoraPromedioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTareaIntegradora != null ? idTareaIntegradora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TareaIntegradora)) {
            return false;
        }
        TareaIntegradora other = (TareaIntegradora) object;
        if ((this.idTareaIntegradora == null && other.idTareaIntegradora != null) || (this.idTareaIntegradora != null && !this.idTareaIntegradora.equals(other.idTareaIntegradora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora[ idTareaIntegradora=" + idTareaIntegradora + " ]";
    }
    
}
