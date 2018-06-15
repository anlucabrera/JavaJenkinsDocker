/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeación
 */
@Entity
@Table(name = "promedio_encuesta_por_carrera", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PromedioEncuestaPorCarrera.findAll", query = "SELECT p FROM PromedioEncuestaPorCarrera p")
    , @NamedQuery(name = "PromedioEncuestaPorCarrera.findBySiglas", query = "SELECT p FROM PromedioEncuestaPorCarrera p WHERE p.siglas = :siglas")
    , @NamedQuery(name = "PromedioEncuestaPorCarrera.findByRegistros", query = "SELECT p FROM PromedioEncuestaPorCarrera p WHERE p.registros = :registros")
    , @NamedQuery(name = "PromedioEncuestaPorCarrera.findByTotal", query = "SELECT p FROM PromedioEncuestaPorCarrera p WHERE p.total = :total")
    , @NamedQuery(name = "PromedioEncuestaPorCarrera.findByRegistro", query = "SELECT p FROM PromedioEncuestaPorCarrera p WHERE p.registro = :registro")
    , @NamedQuery(name = "PromedioEncuestaPorCarrera.findByPromedio", query = "SELECT p FROM PromedioEncuestaPorCarrera p WHERE p.promedio = :promedio")})
public class PromedioEncuestaPorCarrera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 10)
    @Column(name = "siglas")
    @Id
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Registros")
    private long registros;
    @Column(name = "total")
    private BigInteger total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private long registro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "promedio")
    private BigDecimal promedio;

    public PromedioEncuestaPorCarrera() {
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public long getRegistros() {
        return registros;
    }

    public void setRegistros(long registros) {
        this.registros = registros;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    public long getRegistro() {
        return registro;
    }

    public void setRegistro(long registro) {
        this.registro = registro;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }
    
}
