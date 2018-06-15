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
@Table(name = "lista_encuesta_satisfaccion_egresados_ing", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findAll", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByEvaluador", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.evaluador = :evaluador")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByNombre", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByApellidoPaterno", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByApellidoMaterno", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findBySiglas", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.siglas = :siglas")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta1", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta1 = :pregunta1")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta2", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta2 = :pregunta2")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta3", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta3 = :pregunta3")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta4", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta4 = :pregunta4")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta5", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta5 = :pregunta5")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta6", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta6 = :pregunta6")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta7", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta7 = :pregunta7")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta8", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta8 = :pregunta8")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPregunta9", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.pregunta9 = :pregunta9")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByTipo", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.tipo = :tipo")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByClave", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.clave = :clave")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByEstatus", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.estatus = :estatus")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByTotal", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.total = :total")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByPromedio", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.promedio = :promedio")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByGrupo", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.grupo = :grupo")
    , @NamedQuery(name = "ListaEncuestaSatisfaccionEgresadosIng.findByCuatrimestre", query = "SELECT l FROM ListaEncuestaSatisfaccionEgresadosIng l WHERE l.cuatrimestre = :cuatrimestre")})
public class ListaEncuestaSatisfaccionEgresadosIng implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador")
    @Id
    private int evaluador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Size(max = 10)
    @Column(name = "siglas")
    private String siglas;
    @Column(name = "pregunta1")
    private Short pregunta1;
    @Column(name = "pregunta2")
    private Short pregunta2;
    @Column(name = "pregunta3")
    private Short pregunta3;
    @Column(name = "pregunta4")
    private Short pregunta4;
    @Column(name = "pregunta5")
    private Short pregunta5;
    @Column(name = "pregunta6")
    private Short pregunta6;
    @Column(name = "pregunta7")
    private Short pregunta7;
    @Column(name = "pregunta8")
    private Short pregunta8;
    @Column(name = "pregunta9")
    private Short pregunta9;
    @Size(max = 45)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private int clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "estatus")
    private String estatus;
    @Column(name = "total")
    private BigInteger total;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "promedio")
    private BigDecimal promedio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "grupo")
    private String grupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private int cuatrimestre;

    public ListaEncuestaSatisfaccionEgresadosIng() {
    }

    public int getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(int evaluador) {
        this.evaluador = evaluador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public Short getPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(Short pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public Short getPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(Short pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public Short getPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(Short pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public Short getPregunta4() {
        return pregunta4;
    }

    public void setPregunta4(Short pregunta4) {
        this.pregunta4 = pregunta4;
    }

    public Short getPregunta5() {
        return pregunta5;
    }

    public void setPregunta5(Short pregunta5) {
        this.pregunta5 = pregunta5;
    }

    public Short getPregunta6() {
        return pregunta6;
    }

    public void setPregunta6(Short pregunta6) {
        this.pregunta6 = pregunta6;
    }

    public Short getPregunta7() {
        return pregunta7;
    }

    public void setPregunta7(Short pregunta7) {
        this.pregunta7 = pregunta7;
    }

    public Short getPregunta8() {
        return pregunta8;
    }

    public void setPregunta8(Short pregunta8) {
        this.pregunta8 = pregunta8;
    }

    public Short getPregunta9() {
        return pregunta9;
    }

    public void setPregunta9(Short pregunta9) {
        this.pregunta9 = pregunta9;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getClave() {
        return clave;
    }

    public void setClave(int clave) {
        this.clave = clave;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }
    
}
