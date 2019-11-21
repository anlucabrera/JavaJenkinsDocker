/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "reporteplaneacioncuatrimestralareaacademica", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findAll", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByConfiguracionDetalle", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.configuracionDetalle = :configuracionDetalle")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByCarga", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.carga = :carga")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByDescripcion", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByDocente", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.docente = :docente")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByGrado", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.grado = :grado")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByLiteral", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.literal = :literal")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByClaveMateria", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.claveMateria = :claveMateria")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByMateria", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.materia = :materia")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByNoUnidad", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.noUnidad = :noUnidad")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByNombreUnidad", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.nombreUnidad = :nombreUnidad")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByObjetivo", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.objetivo = :objetivo")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByFechaInicio", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByFechaFin", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.fechaFin = :fechaFin")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByPorUnidad", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.porUnidad = :porUnidad")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByCriterio", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.criterio = :criterio")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByPorCriterio", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.porCriterio = :porCriterio")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByIndicador", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.indicador = :indicador")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByPorcentajeInd", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.porcentajeInd = :porcentajeInd")
    , @NamedQuery(name = "Reporteplaneacioncuatrimestralareaacademica.findByAreaSuperior", query = "SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.areaSuperior = :areaSuperior")})
public class Reporteplaneacioncuatrimestralareaacademica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion_detalle")
    private long configuracionDetalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carga")
    private int carga;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 250)
    @Column(name = "Docente")
    private String docente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "literal")
    private Character literal;
    @Size(max = 90)
    @Column(name = "clave_materia")
    private String claveMateria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Materia")
    private String materia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NoUnidad")
    private int noUnidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombreUnidad")
    private String nombreUnidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "objetivo")
    private String objetivo;
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
    @Column(name = "porUnidad")
    private double porUnidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "criterio")
    private String criterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porCriterio")
    private double porCriterio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "indicador")
    private String indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentajeInd")
    private double porcentajeInd;
    @Column(name = "area_superior")
    private Short areaSuperior;

    public Reporteplaneacioncuatrimestralareaacademica() {
    }

    public long getConfiguracionDetalle() {
        return configuracionDetalle;
    }

    public void setConfiguracionDetalle(long configuracionDetalle) {
        this.configuracionDetalle = configuracionDetalle;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public Character getLiteral() {
        return literal;
    }

    public void setLiteral(Character literal) {
        this.literal = literal;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getNoUnidad() {
        return noUnidad;
    }

    public void setNoUnidad(int noUnidad) {
        this.noUnidad = noUnidad;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
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

    public double getPorUnidad() {
        return porUnidad;
    }

    public void setPorUnidad(double porUnidad) {
        this.porUnidad = porUnidad;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public double getPorCriterio() {
        return porCriterio;
    }

    public void setPorCriterio(double porCriterio) {
        this.porCriterio = porCriterio;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public double getPorcentajeInd() {
        return porcentajeInd;
    }

    public void setPorcentajeInd(double porcentajeInd) {
        this.porcentajeInd = porcentajeInd;
    }

    public Short getAreaSuperior() {
        return areaSuperior;
    }

    public void setAreaSuperior(Short areaSuperior) {
        this.areaSuperior = areaSuperior;
    }
    
}
