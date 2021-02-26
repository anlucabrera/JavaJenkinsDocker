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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "acciones_de_mejora", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccionesDeMejora.findAll", query = "SELECT a FROM AccionesDeMejora a")
    , @NamedQuery(name = "AccionesDeMejora.findByIdAccionmejora", query = "SELECT a FROM AccionesDeMejora a WHERE a.idAccionmejora = :idAccionmejora")
    , @NamedQuery(name = "AccionesDeMejora.findByAcciones", query = "SELECT a FROM AccionesDeMejora a WHERE a.acciones = :acciones")
    , @NamedQuery(name = "AccionesDeMejora.findByValorAlcanzado", query = "SELECT a FROM AccionesDeMejora a WHERE a.valorAlcanzado = :valorAlcanzado")})
public class AccionesDeMejora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_accionmejora")
    private Integer idAccionmejora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "acciones")
    private String acciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_alcanzado")
    private double valorAlcanzado;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMateriaConfiguracion configuracion;

    public AccionesDeMejora() {
    }

    public AccionesDeMejora(Integer idAccionmejora) {
        this.idAccionmejora = idAccionmejora;
    }

    public AccionesDeMejora(Integer idAccionmejora, String acciones, double valorAlcanzado) {
        this.idAccionmejora = idAccionmejora;
        this.acciones = acciones;
        this.valorAlcanzado = valorAlcanzado;
    }

    public Integer getIdAccionmejora() {
        return idAccionmejora;
    }

    public void setIdAccionmejora(Integer idAccionmejora) {
        this.idAccionmejora = idAccionmejora;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public double getValorAlcanzado() {
        return valorAlcanzado;
    }

    public void setValorAlcanzado(double valorAlcanzado) {
        this.valorAlcanzado = valorAlcanzado;
    }

    public UnidadMateriaConfiguracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(UnidadMateriaConfiguracion configuracion) {
        this.configuracion = configuracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAccionmejora != null ? idAccionmejora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccionesDeMejora)) {
            return false;
        }
        AccionesDeMejora other = (AccionesDeMejora) object;
        if ((this.idAccionmejora == null && other.idAccionmejora != null) || (this.idAccionmejora != null && !this.idAccionmejora.equals(other.idAccionmejora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AccionesDeMejora[ idAccionmejora=" + idAccionmejora + " ]";
    }
    
}
