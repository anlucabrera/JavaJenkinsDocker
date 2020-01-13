/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "personal_capacitado_cursos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonalCapacitadoCursos.findAll", query = "SELECT p FROM PersonalCapacitadoCursos p")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByCurso", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.curso = :curso")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByNombre", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByFechaInicio", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByFechaFin", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByDuracionHoras", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.duracionHoras = :duracionHoras")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByTipo", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByTipoNombre", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.tipoNombre = :tipoNombre")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByEmpresaImpartidora", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.empresaImpartidora = :empresaImpartidora")
    , @NamedQuery(name = "PersonalCapacitadoCursos.findByMontoInvertido", query = "SELECT p FROM PersonalCapacitadoCursos p WHERE p.montoInvertido = :montoInvertido")})
public class PersonalCapacitadoCursos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "curso", nullable = false)
    private int curso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre", nullable = false, length = 300)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion_horas", nullable = false)
    private short duracionHoras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo", nullable = false)
    private short tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tipoNombre", nullable = false, length = 50)
    private String tipoNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "empresa_impartidora", nullable = false, length = 300)
    private String empresaImpartidora;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "objetivo", nullable = false, length = 65535)
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "lugar", nullable = false, length = 65535)
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_invertido", nullable = false)
    private double montoInvertido;

    public PersonalCapacitadoCursos() {
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public short getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(short duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public String getTipoNombre() {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }

    public String getEmpresaImpartidora() {
        return empresaImpartidora;
    }

    public void setEmpresaImpartidora(String empresaImpartidora) {
        this.empresaImpartidora = empresaImpartidora;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public double getMontoInvertido() {
        return montoInvertido;
    }

    public void setMontoInvertido(double montoInvertido) {
        this.montoInvertido = montoInvertido;
    }
    
}
