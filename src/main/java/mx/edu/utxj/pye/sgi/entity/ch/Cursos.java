/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jonny
 */
@Entity
@Table(name = "cursos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c")
    , @NamedQuery(name = "Cursos.findByCurso", query = "SELECT c FROM Cursos c WHERE c.curso = :curso")
    , @NamedQuery(name = "Cursos.findByNombre", query = "SELECT c FROM Cursos c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Cursos.findByFechaInicio", query = "SELECT c FROM Cursos c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Cursos.findByFechaFin", query = "SELECT c FROM Cursos c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "Cursos.findByDuracionHoras", query = "SELECT c FROM Cursos c WHERE c.duracionHoras = :duracionHoras")
    , @NamedQuery(name = "Cursos.findByDuracionMinutos", query = "SELECT c FROM Cursos c WHERE c.duracionMinutos = :duracionMinutos")
    , @NamedQuery(name = "Cursos.findByEmpresaImpartidora", query = "SELECT c FROM Cursos c WHERE c.empresaImpartidora = :empresaImpartidora")
    , @NamedQuery(name = "Cursos.findByMontoInvertido", query = "SELECT c FROM Cursos c WHERE c.montoInvertido = :montoInvertido")
    , @NamedQuery(name = "Cursos.findByEvento", query = "SELECT c FROM Cursos c WHERE c.evento = :evento")
    , @NamedQuery(name = "Cursos.findByPoa", query = "SELECT c FROM Cursos c WHERE c.poa = :poa")
    , @NamedQuery(name = "Cursos.findByMes", query = "SELECT c FROM Cursos c WHERE c.mes = :mes")})
public class Cursos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "curso")
    private Integer curso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion_horas")
    private short duracionHoras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duracion_minutos")
    private short duracionMinutos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "empresa_impartidora")
    private String empresaImpartidora;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_invertido")
    private double montoInvertido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evento")
    private int evento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "poa")
    @Temporal(TemporalType.DATE)
    private Date poa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mes")
    private String mes;
    @JoinTable(name = "cursos_cursosevidencia", joinColumns = {
        @JoinColumn(name = "curso", referencedColumnName = "curso")}, inverseJoinColumns = {
        @JoinColumn(name = "evidencia", referencedColumnName = "evidencia")})
    @ManyToMany
    private List<CursosEvidencia> cursosEvidenciaList;
    @JoinColumn(name = "modalidad", referencedColumnName = "modalidad")
    @ManyToOne(optional = false)
    private CursosModalidad modalidad;
    @JoinColumn(name = "tipo", referencedColumnName = "tipo")
    @ManyToOne(optional = false)
    private CursosTipo tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso")
    private List<CursosPersonal> cursosPersonalList;

    public Cursos() {
    }

    public Cursos(Integer curso) {
        this.curso = curso;
    }

    public Cursos(Integer curso, String nombre, Date fechaInicio, Date fechaFin, short duracionHoras, short duracionMinutos, String empresaImpartidora, String objetivo, String lugar, double montoInvertido, int evento, Date poa, String mes) {
        this.curso = curso;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracionHoras = duracionHoras;
        this.duracionMinutos = duracionMinutos;
        this.empresaImpartidora = empresaImpartidora;
        this.objetivo = objetivo;
        this.lugar = lugar;
        this.montoInvertido = montoInvertido;
        this.evento = evento;
        this.poa = poa;
        this.mes = mes;
    }

    public Integer getCurso() {
        return curso;
    }

    public void setCurso(Integer curso) {
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

    public short getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(short duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
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

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public Date getPoa() {
        return poa;
    }

    public void setPoa(Date poa) {
        this.poa = poa;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @XmlTransient
    public List<CursosEvidencia> getCursosEvidenciaList() {
        return cursosEvidenciaList;
    }

    public void setCursosEvidenciaList(List<CursosEvidencia> cursosEvidenciaList) {
        this.cursosEvidenciaList = cursosEvidenciaList;
    }

    public CursosModalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(CursosModalidad modalidad) {
        this.modalidad = modalidad;
    }

    public CursosTipo getTipo() {
        return tipo;
    }

    public void setTipo(CursosTipo tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<CursosPersonal> getCursosPersonalList() {
        return cursosPersonalList;
    }

    public void setCursosPersonalList(List<CursosPersonal> cursosPersonalList) {
        this.cursosPersonalList = cursosPersonalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (curso != null ? curso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cursos)) {
            return false;
        }
        Cursos other = (Cursos) object;
        if ((this.curso == null && other.curso != null) || (this.curso != null && !this.curso.equals(other.curso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Cursos[ curso=" + curso + " ]";
    }
    
}
