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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "capacidad_instalada_ciclos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CapacidadInstaladaCiclosEscolares.findAll", query = "SELECT c FROM CapacidadInstaladaCiclosEscolares c")
    , @NamedQuery(name = "CapacidadInstaladaCiclosEscolares.findByRegistro", query = "SELECT c FROM CapacidadInstaladaCiclosEscolares c WHERE c.registro = :registro")
    , @NamedQuery(name = "CapacidadInstaladaCiclosEscolares.findByCicloEscolar", query = "SELECT c FROM CapacidadInstaladaCiclosEscolares c WHERE c.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "CapacidadInstaladaCiclosEscolares.findByUnidades", query = "SELECT c FROM CapacidadInstaladaCiclosEscolares c WHERE c.unidades = :unidades")})
public class CapacidadInstaladaCiclosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unidades")
    private int unidades;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "instalacion", referencedColumnName = "instalacion")
    @ManyToOne(optional = false)
    private CapacidadInstaladaTiposInstalaciones instalacion;

    public CapacidadInstaladaCiclosEscolares() {
    }

    public CapacidadInstaladaCiclosEscolares(Integer registro) {
        this.registro = registro;
    }

    public CapacidadInstaladaCiclosEscolares(Integer registro, int cicloEscolar, int unidades) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.unidades = unidades;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public CapacidadInstaladaTiposInstalaciones getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(CapacidadInstaladaTiposInstalaciones instalacion) {
        this.instalacion = instalacion;
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
        if (!(object instanceof CapacidadInstaladaCiclosEscolares)) {
            return false;
        }
        CapacidadInstaladaCiclosEscolares other = (CapacidadInstaladaCiclosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares[ registro=" + registro + " ]";
    }
    
}
