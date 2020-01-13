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
@Table(name = "servicios_enfermeria_ciclo_periodos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findAll", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByRegistro", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.registro = :registro")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByCicloEscolar", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByPeriodoEscolar", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByMes", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.mes = :mes")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByEstH", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.estH = :estH")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByEstM", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.estM = :estM")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByPerH", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.perH = :perH")
    , @NamedQuery(name = "ServiciosEnfermeriaCicloPeriodos.findByPerM", query = "SELECT s FROM ServiciosEnfermeriaCicloPeriodos s WHERE s.perM = :perM")})
public class ServiciosEnfermeriaCicloPeriodos implements Serializable {

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
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "mes")
    private String mes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_h")
    private int estH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_m")
    private int estM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "per_h")
    private int perH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "per_m")
    private int perM;
    @JoinColumn(name = "servicio", referencedColumnName = "servicio")
    @ManyToOne(optional = false)
    private ServiciosEnfermeriaTipo servicio;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public ServiciosEnfermeriaCicloPeriodos() {
    }

    public ServiciosEnfermeriaCicloPeriodos(Integer registro) {
        this.registro = registro;
    }

    public ServiciosEnfermeriaCicloPeriodos(Integer registro, int cicloEscolar, int periodoEscolar, String mes, int estH, int estM, int perH, int perM) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.mes = mes;
        this.estH = estH;
        this.estM = estM;
        this.perH = perH;
        this.perM = perM;
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

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getEstH() {
        return estH;
    }

    public void setEstH(int estH) {
        this.estH = estH;
    }

    public int getEstM() {
        return estM;
    }

    public void setEstM(int estM) {
        this.estM = estM;
    }

    public int getPerH() {
        return perH;
    }

    public void setPerH(int perH) {
        this.perH = perH;
    }

    public int getPerM() {
        return perM;
    }

    public void setPerM(int perM) {
        this.perM = perM;
    }

    public ServiciosEnfermeriaTipo getServicio() {
        return servicio;
    }

    public void setServicio(ServiciosEnfermeriaTipo servicio) {
        this.servicio = servicio;
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
        if (!(object instanceof ServiciosEnfermeriaCicloPeriodos)) {
            return false;
        }
        ServiciosEnfermeriaCicloPeriodos other = (ServiciosEnfermeriaCicloPeriodos) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos[ registro=" + registro + " ]";
    }
    
}
