/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2.view;

import java.io.Serializable;
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
 * @author UTXJ
 */
@Entity
@Table(name = "cuadro_mando_integral_2019", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuadroMandoIntegral2019.findAll", query = "SELECT c FROM CuadroMandoIntegral2019 c")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByCuadroMandoInt", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.cuadroMandoInt = :cuadroMandoInt")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByAnio", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.anio = :anio")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByEje", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.eje = :eje")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByNombre", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByEstrategia", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.estrategia = :estrategia")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByNumeroEstrategia", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.numeroEstrategia = :numeroEstrategia")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByNombreEstrategia", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.nombreEstrategia = :nombreEstrategia")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByIdLinea", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.idLinea = :idLinea")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByNumeroLinea", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.numeroLinea = :numeroLinea")
    , @NamedQuery(name = "CuadroMandoIntegral2019.findByNombreLinea", query = "SELECT c FROM CuadroMandoIntegral2019 c WHERE c.nombreLinea = :nombreLinea")})
public class CuadroMandoIntegral2019 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuadro_mando_int")
    @Id
    private int cuadroMandoInt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private short anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eje")
    private int eje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estrategia")
    private short estrategia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numeroEstrategia")
    private short numeroEstrategia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombreEstrategia")
    private String nombreEstrategia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idLinea")
    private short idLinea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numeroLinea")
    private short numeroLinea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombreLinea")
    private String nombreLinea;

    public CuadroMandoIntegral2019() {
    }

    public int getCuadroMandoInt() {
        return cuadroMandoInt;
    }

    public void setCuadroMandoInt(int cuadroMandoInt) {
        this.cuadroMandoInt = cuadroMandoInt;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public int getEje() {
        return eje;
    }

    public void setEje(int eje) {
        this.eje = eje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(short estrategia) {
        this.estrategia = estrategia;
    }

    public short getNumeroEstrategia() {
        return numeroEstrategia;
    }

    public void setNumeroEstrategia(short numeroEstrategia) {
        this.numeroEstrategia = numeroEstrategia;
    }

    public String getNombreEstrategia() {
        return nombreEstrategia;
    }

    public void setNombreEstrategia(String nombreEstrategia) {
        this.nombreEstrategia = nombreEstrategia;
    }

    public short getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(short idLinea) {
        this.idLinea = idLinea;
    }

    public short getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(short numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getNombreLinea() {
        return nombreLinea;
    }

    public void setNombreLinea(String nombreLinea) {
        this.nombreLinea = nombreLinea;
    }
    
}
