/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
@Table(name = "resultados_egetsu", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResultadosEgetsu.findAll", query = "SELECT r FROM ResultadosEgetsu r")
    , @NamedQuery(name = "ResultadosEgetsu.findByGeneracion", query = "SELECT r FROM ResultadosEgetsu r WHERE r.generacion = :generacion")
    , @NamedQuery(name = "ResultadosEgetsu.findByEgresados", query = "SELECT r FROM ResultadosEgetsu r WHERE r.egresados = :egresados")
    , @NamedQuery(name = "ResultadosEgetsu.findByEgreEgetsu", query = "SELECT r FROM ResultadosEgetsu r WHERE r.egreEgetsu = :egreEgetsu")
    , @NamedQuery(name = "ResultadosEgetsu.findByTestSobre", query = "SELECT r FROM ResultadosEgetsu r WHERE r.testSobre = :testSobre")
    , @NamedQuery(name = "ResultadosEgetsu.findByTestSatis", query = "SELECT r FROM ResultadosEgetsu r WHERE r.testSatis = :testSatis")
    , @NamedQuery(name = "ResultadosEgetsu.findBySinTest", query = "SELECT r FROM ResultadosEgetsu r WHERE r.sinTest = :sinTest")})
public class ResultadosEgetsu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private Short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egresados")
    private short egresados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egre_egetsu")
    private short egreEgetsu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "test_sobre")
    private short testSobre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "test_satis")
    private short testSatis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_test")
    private short sinTest;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Generaciones generaciones;

    public ResultadosEgetsu() {
    }

    public ResultadosEgetsu(Short generacion) {
        this.generacion = generacion;
    }

    public ResultadosEgetsu(Short generacion, short egresados, short egreEgetsu, short testSobre, short testSatis, short sinTest) {
        this.generacion = generacion;
        this.egresados = egresados;
        this.egreEgetsu = egreEgetsu;
        this.testSobre = testSobre;
        this.testSatis = testSatis;
        this.sinTest = sinTest;
    }

    public Short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Short generacion) {
        this.generacion = generacion;
    }

    public short getEgresados() {
        return egresados;
    }

    public void setEgresados(short egresados) {
        this.egresados = egresados;
    }

    public short getEgreEgetsu() {
        return egreEgetsu;
    }

    public void setEgreEgetsu(short egreEgetsu) {
        this.egreEgetsu = egreEgetsu;
    }

    public short getTestSobre() {
        return testSobre;
    }

    public void setTestSobre(short testSobre) {
        this.testSobre = testSobre;
    }

    public short getTestSatis() {
        return testSatis;
    }

    public void setTestSatis(short testSatis) {
        this.testSatis = testSatis;
    }

    public short getSinTest() {
        return sinTest;
    }

    public void setSinTest(short sinTest) {
        this.sinTest = sinTest;
    }

    public Generaciones getGeneraciones() {
        return generaciones;
    }

    public void setGeneraciones(Generaciones generaciones) {
        this.generaciones = generaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (generacion != null ? generacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResultadosEgetsu)) {
            return false;
        }
        ResultadosEgetsu other = (ResultadosEgetsu) object;
        if ((this.generacion == null && other.generacion != null) || (this.generacion != null && !this.generacion.equals(other.generacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ResultadosEgetsu[ generacion=" + generacion + " ]";
    }
    
}
