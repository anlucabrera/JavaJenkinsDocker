/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "servicios_tecnologicos_encuestas_satisfaccion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findAll", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByClaveEncuesta", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.claveEncuesta = :claveEncuesta")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByTotalEncuestas", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.totalEncuestas = :totalEncuestas")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByMuySatis", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.muySatis = :muySatis")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findBySatisfechos", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.satisfechos = :satisfechos")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByRegularmenteSatis", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.regularmenteSatis = :regularmenteSatis")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByPocoSatis", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.pocoSatis = :pocoSatis")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByNoSatis", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.noSatis = :noSatis")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findByNoAplica", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.noAplica = :noAplica")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findBySatisbase5", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.satisbase5 = :satisbase5")
    , @NamedQuery(name = "ServiciosTecnologicosEncuestasSatisfaccion.findBySatisbase10", query = "SELECT s FROM ServiciosTecnologicosEncuestasSatisfaccion s WHERE s.satisbase10 = :satisbase10")})
public class ServiciosTecnologicosEncuestasSatisfaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_encuesta")
    private Integer claveEncuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_encuestas")
    private int totalEncuestas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "muy_satis")
    private int muySatis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "satisfechos")
    private int satisfechos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "regularmente_satis")
    private int regularmenteSatis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "poco_satis")
    private int pocoSatis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_satis")
    private int noSatis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_aplica")
    private int noAplica;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "satisbase_5")
    private BigDecimal satisbase5;
    @Basic(optional = false)
    @NotNull
    @Column(name = "satisbase_10")
    private BigDecimal satisbase10;
    @JoinColumns({
        @JoinColumn(name = "ciclo", referencedColumnName = "ciclo"),
        @JoinColumn(name = "mes", referencedColumnName = "mes"),
        @JoinColumn(name = "clave_servicio", referencedColumnName = "clave_servicio"),
    })
    @ManyToOne(optional = false)
    private ServiciosTecnologicos claveServicio;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;

    public ServiciosTecnologicosEncuestasSatisfaccion() {
    }

    public ServiciosTecnologicosEncuestasSatisfaccion(Integer claveEncuesta) {
        this.claveEncuesta = claveEncuesta;
    }

    public ServiciosTecnologicosEncuestasSatisfaccion(Integer claveEncuesta, int totalEncuestas, int muySatis, int satisfechos, int regularmenteSatis, int pocoSatis, int noSatis, int noAplica, BigDecimal satisbase5, BigDecimal satisbase10) {
        this.claveEncuesta = claveEncuesta;
        this.totalEncuestas = totalEncuestas;
        this.muySatis = muySatis;
        this.satisfechos = satisfechos;
        this.regularmenteSatis = regularmenteSatis;
        this.pocoSatis = pocoSatis;
        this.noSatis = noSatis;
        this.noAplica = noAplica;
        this.satisbase5 = satisbase5;
        this.satisbase10 = satisbase10;
    }

    public Integer getClaveEncuesta() {
        return claveEncuesta;
    }

    public void setClaveEncuesta(Integer claveEncuesta) {
        this.claveEncuesta = claveEncuesta;
    }

    public int getTotalEncuestas() {
        return totalEncuestas;
    }

    public void setTotalEncuestas(int totalEncuestas) {
        this.totalEncuestas = totalEncuestas;
    }

    public int getMuySatis() {
        return muySatis;
    }

    public void setMuySatis(int muySatis) {
        this.muySatis = muySatis;
    }

    public int getSatisfechos() {
        return satisfechos;
    }

    public void setSatisfechos(int satisfechos) {
        this.satisfechos = satisfechos;
    }

    public int getRegularmenteSatis() {
        return regularmenteSatis;
    }

    public void setRegularmenteSatis(int regularmenteSatis) {
        this.regularmenteSatis = regularmenteSatis;
    }

    public int getPocoSatis() {
        return pocoSatis;
    }

    public void setPocoSatis(int pocoSatis) {
        this.pocoSatis = pocoSatis;
    }

    public int getNoSatis() {
        return noSatis;
    }

    public void setNoSatis(int noSatis) {
        this.noSatis = noSatis;
    }

    public int getNoAplica() {
        return noAplica;
    }

    public void setNoAplica(int noAplica) {
        this.noAplica = noAplica;
    }

    public BigDecimal getSatisbase5() {
        return satisbase5;
    }

    public void setSatisbase5(BigDecimal satisbase5) {
        this.satisbase5 = satisbase5;
    }

    public BigDecimal getSatisbase10() {
        return satisbase10;
    }

    public void setSatisbase10(BigDecimal satisbase10) {
        this.satisbase10 = satisbase10;
    }

    public ServiciosTecnologicos getClaveServicio() {
        return claveServicio;
    }

    public void setClaveServicio(ServiciosTecnologicos claveServicio) {
        this.claveServicio = claveServicio;
    }

    public PeriodosEscolares getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveEncuesta != null ? claveEncuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosTecnologicosEncuestasSatisfaccion)) {
            return false;
        }
        ServiciosTecnologicosEncuestasSatisfaccion other = (ServiciosTecnologicosEncuestasSatisfaccion) object;
        if ((this.claveEncuesta == null && other.claveEncuesta != null) || (this.claveEncuesta != null && !this.claveEncuesta.equals(other.claveEncuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ServiciosTecnologicosEncuestasSatisfaccion[ claveEncuesta=" + claveEncuesta + " ]";
    }
    
}
