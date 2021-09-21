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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "satisfaccion_servtec_educont_anio_mes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findAll", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByRegistro", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.registro = :registro")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByTotalEncuestados", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.totalEncuestados = :totalEncuestados")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByNumMuySatisfechos", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.numMuySatisfechos = :numMuySatisfechos")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByNumSatisfechos", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.numSatisfechos = :numSatisfechos")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByNumRegSatisfechos", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.numRegSatisfechos = :numRegSatisfechos")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByNumPocoSatisfechos", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.numPocoSatisfechos = :numPocoSatisfechos")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByNumNoSatisfechos", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.numNoSatisfechos = :numNoSatisfechos")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByNumNoAplica", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.numNoAplica = :numNoAplica")
    , @NamedQuery(name = "SatisfaccionServtecEducontAnioMes.findByTotalEvaluables", query = "SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.totalEvaluables = :totalEvaluables")})
public class SatisfaccionServtecEducontAnioMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_encuestados")
    private int totalEncuestados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_muy_satisfechos")
    private int numMuySatisfechos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_satisfechos")
    private int numSatisfechos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_reg_satisfechos")
    private int numRegSatisfechos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_poco_satisfechos")
    private int numPocoSatisfechos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_no_satisfechos")
    private int numNoSatisfechos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_no_aplica")
    private int numNoAplica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_evaluables")
    private int totalEvaluables;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;
    @JoinColumn(name = "servicio", referencedColumnName = "servicio")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private ServiciosTecnologicosAnioMes servicio;

    public SatisfaccionServtecEducontAnioMes() {
    }

    public SatisfaccionServtecEducontAnioMes(Integer registro) {
        this.registro = registro;
    }

    public SatisfaccionServtecEducontAnioMes(Integer registro, int totalEncuestados, int numMuySatisfechos, int numSatisfechos, int numRegSatisfechos, int numPocoSatisfechos, int numNoSatisfechos, int numNoAplica, int totalEvaluables) {
        this.registro = registro;
        this.totalEncuestados = totalEncuestados;
        this.numMuySatisfechos = numMuySatisfechos;
        this.numSatisfechos = numSatisfechos;
        this.numRegSatisfechos = numRegSatisfechos;
        this.numPocoSatisfechos = numPocoSatisfechos;
        this.numNoSatisfechos = numNoSatisfechos;
        this.numNoAplica = numNoAplica;
        this.totalEvaluables = totalEvaluables;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getTotalEncuestados() {
        return totalEncuestados;
    }

    public void setTotalEncuestados(int totalEncuestados) {
        this.totalEncuestados = totalEncuestados;
    }

    public int getNumMuySatisfechos() {
        return numMuySatisfechos;
    }

    public void setNumMuySatisfechos(int numMuySatisfechos) {
        this.numMuySatisfechos = numMuySatisfechos;
    }

    public int getNumSatisfechos() {
        return numSatisfechos;
    }

    public void setNumSatisfechos(int numSatisfechos) {
        this.numSatisfechos = numSatisfechos;
    }

    public int getNumRegSatisfechos() {
        return numRegSatisfechos;
    }

    public void setNumRegSatisfechos(int numRegSatisfechos) {
        this.numRegSatisfechos = numRegSatisfechos;
    }

    public int getNumPocoSatisfechos() {
        return numPocoSatisfechos;
    }

    public void setNumPocoSatisfechos(int numPocoSatisfechos) {
        this.numPocoSatisfechos = numPocoSatisfechos;
    }

    public int getNumNoSatisfechos() {
        return numNoSatisfechos;
    }

    public void setNumNoSatisfechos(int numNoSatisfechos) {
        this.numNoSatisfechos = numNoSatisfechos;
    }

    public int getNumNoAplica() {
        return numNoAplica;
    }

    public void setNumNoAplica(int numNoAplica) {
        this.numNoAplica = numNoAplica;
    }

    public int getTotalEvaluables() {
        return totalEvaluables;
    }

    public void setTotalEvaluables(int totalEvaluables) {
        this.totalEvaluables = totalEvaluables;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public ServiciosTecnologicosAnioMes getServicio() {
        return servicio;
    }

    public void setServicio(ServiciosTecnologicosAnioMes servicio) {
        this.servicio = servicio;
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
        if (!(object instanceof SatisfaccionServtecEducontAnioMes)) {
            return false;
        }
        SatisfaccionServtecEducontAnioMes other = (SatisfaccionServtecEducontAnioMes) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.SatisfaccionServtecEducontAnioMes[ registro=" + registro + " ]";
    }
    
}
