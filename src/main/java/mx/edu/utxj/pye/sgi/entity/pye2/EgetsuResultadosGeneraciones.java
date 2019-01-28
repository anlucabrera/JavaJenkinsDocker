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
@Table(name = "egetsu_resultados_generaciones", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EgetsuResultadosGeneraciones.findAll", query = "SELECT e FROM EgetsuResultadosGeneraciones e")
    , @NamedQuery(name = "EgetsuResultadosGeneraciones.findByRegistro", query = "SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.registro = :registro")
    , @NamedQuery(name = "EgetsuResultadosGeneraciones.findByGeneracion", query = "SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.generacion = :generacion")
    , @NamedQuery(name = "EgetsuResultadosGeneraciones.findByEgreEgetsu", query = "SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.egreEgetsu = :egreEgetsu")
    , @NamedQuery(name = "EgetsuResultadosGeneraciones.findByTestSobre", query = "SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.testSobre = :testSobre")
    , @NamedQuery(name = "EgetsuResultadosGeneraciones.findByTestSatis", query = "SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.testSatis = :testSatis")
    , @NamedQuery(name = "EgetsuResultadosGeneraciones.findBySinTest", query = "SELECT e FROM EgetsuResultadosGeneraciones e WHERE e.sinTest = :sinTest")})
public class EgetsuResultadosGeneraciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egre_egetsu")
    private int egreEgetsu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "test_sobre")
    private int testSobre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "test_satis")
    private int testSatis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_test")
    private int sinTest;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public EgetsuResultadosGeneraciones() {
    }

    public EgetsuResultadosGeneraciones(Integer registro) {
        this.registro = registro;
    }

    public EgetsuResultadosGeneraciones(Integer registro, short generacion, int egreEgetsu, int testSobre, int testSatis, int sinTest) {
        this.registro = registro;
        this.generacion = generacion;
        this.egreEgetsu = egreEgetsu;
        this.testSobre = testSobre;
        this.testSatis = testSatis;
        this.sinTest = sinTest;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public int getEgreEgetsu() {
        return egreEgetsu;
    }

    public void setEgreEgetsu(int egreEgetsu) {
        this.egreEgetsu = egreEgetsu;
    }

    public int getTestSobre() {
        return testSobre;
    }

    public void setTestSobre(int testSobre) {
        this.testSobre = testSobre;
    }

    public int getTestSatis() {
        return testSatis;
    }

    public void setTestSatis(int testSatis) {
        this.testSatis = testSatis;
    }

    public int getSinTest() {
        return sinTest;
    }

    public void setSinTest(int sinTest) {
        this.sinTest = sinTest;
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
        if (!(object instanceof EgetsuResultadosGeneraciones)) {
            return false;
        }
        EgetsuResultadosGeneraciones other = (EgetsuResultadosGeneraciones) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EgetsuResultadosGeneraciones[ registro=" + registro + " ]";
    }
    
}
