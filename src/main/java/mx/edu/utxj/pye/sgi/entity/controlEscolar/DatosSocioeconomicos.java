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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "datos_socioeconomicos", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosSocioeconomicos.findAll", query = "SELECT d FROM DatosSocioeconomicos d")
    , @NamedQuery(name = "DatosSocioeconomicos.findByAspirante", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.aspirante = :aspirante")
    , @NamedQuery(name = "DatosSocioeconomicos.findByVivienda", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.vivienda = :vivienda")
    , @NamedQuery(name = "DatosSocioeconomicos.findByTransporte", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.transporte = :transporte")
    , @NamedQuery(name = "DatosSocioeconomicos.findByRentaViviendaActual", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.rentaViviendaActual = :rentaViviendaActual")
    , @NamedQuery(name = "DatosSocioeconomicos.findByCostoRenta", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.costoRenta = :costoRenta")
    , @NamedQuery(name = "DatosSocioeconomicos.findByDependienteEconomico", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.dependienteEconomico = :dependienteEconomico")
    , @NamedQuery(name = "DatosSocioeconomicos.findByReParaEstudiar", query = "SELECT d FROM DatosSocioeconomicos d WHERE d.reParaEstudiar = :reParaEstudiar")})
public class DatosSocioeconomicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private Integer aspirante;
    @Size(max = 8)
    @Column(name = "vivienda")
    private String vivienda;
    @Size(max = 8)
    @Column(name = "transporte")
    private String transporte;
    @Column(name = "renta_vivienda_actual")
    private Boolean rentaViviendaActual;
    @Column(name = "costo_renta")
    private Integer costoRenta;
    @Size(max = 7)
    @Column(name = "dependiente_economico")
    private String dependienteEconomico;
    @Size(max = 13)
    @Column(name = "re_para__estudiar")
    private String reParaEstudiar;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Aspirante aspirante1;

    public DatosSocioeconomicos() {
    }

    public DatosSocioeconomicos(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public Integer getAspirante() {
        return aspirante;
    }

    public void setAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public Boolean getRentaViviendaActual() {
        return rentaViviendaActual;
    }

    public void setRentaViviendaActual(Boolean rentaViviendaActual) {
        this.rentaViviendaActual = rentaViviendaActual;
    }

    public Integer getCostoRenta() {
        return costoRenta;
    }

    public void setCostoRenta(Integer costoRenta) {
        this.costoRenta = costoRenta;
    }

    public String getDependienteEconomico() {
        return dependienteEconomico;
    }

    public void setDependienteEconomico(String dependienteEconomico) {
        this.dependienteEconomico = dependienteEconomico;
    }

    public String getReParaEstudiar() {
        return reParaEstudiar;
    }

    public void setReParaEstudiar(String reParaEstudiar) {
        this.reParaEstudiar = reParaEstudiar;
    }

    public Aspirante getAspirante1() {
        return aspirante1;
    }

    public void setAspirante1(Aspirante aspirante1) {
        this.aspirante1 = aspirante1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspirante != null ? aspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosSocioeconomicos)) {
            return false;
        }
        DatosSocioeconomicos other = (DatosSocioeconomicos) object;
        if ((this.aspirante == null && other.aspirante != null) || (this.aspirante != null && !this.aspirante.equals(other.aspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosSocioeconomicos[ aspirante=" + aspirante + " ]";
    }
    
}
