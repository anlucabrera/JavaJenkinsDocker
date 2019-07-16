/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author UTXJ
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
    @OneToOne(optional = false)
    private CargaAcademica carga;

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
