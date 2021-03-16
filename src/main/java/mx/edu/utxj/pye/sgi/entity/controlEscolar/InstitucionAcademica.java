/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "institucion_academica", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstitucionAcademica.findAll", query = "SELECT i FROM InstitucionAcademica i")
    , @NamedQuery(name = "InstitucionAcademica.findByIdInstitucionAcademica", query = "SELECT i FROM InstitucionAcademica i WHERE i.idInstitucionAcademica = :idInstitucionAcademica")
    , @NamedQuery(name = "InstitucionAcademica.findByNombre", query = "SELECT i FROM InstitucionAcademica i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "InstitucionAcademica.findByClaveCentro", query = "SELECT i FROM InstitucionAcademica i WHERE i.claveCentro = :claveCentro")
    , @NamedQuery(name = "InstitucionAcademica.findByCalle", query = "SELECT i FROM InstitucionAcademica i WHERE i.calle = :calle")
    , @NamedQuery(name = "InstitucionAcademica.findByPromedio", query = "SELECT i FROM InstitucionAcademica i WHERE i.promedio = :promedio")
    , @NamedQuery(name = "InstitucionAcademica.findByCveEstado", query = "SELECT i FROM InstitucionAcademica i WHERE i.cveEstado = :cveEstado")
    , @NamedQuery(name = "InstitucionAcademica.findByCveMunicipio", query = "SELECT i FROM InstitucionAcademica i WHERE i.cveMunicipio = :cveMunicipio")
    , @NamedQuery(name = "InstitucionAcademica.findByCveLocalidad", query = "SELECT i FROM InstitucionAcademica i WHERE i.cveLocalidad = :cveLocalidad")
    , @NamedQuery(name = "InstitucionAcademica.findByIdPais", query = "SELECT i FROM InstitucionAcademica i WHERE i.idPais = :idPais")})
public class InstitucionAcademica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_institucion_academica")
    private Integer idInstitucionAcademica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clave_centro")
    private String claveCentro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private double promedio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_estado")
    private int cveEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_municipio")
    private int cveMunicipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_localidad")
    private int cveLocalidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_pais")
    private int idPais;
    @JoinColumn(name = "id_especialidad", referencedColumnName = "id_especialidad_centro")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EspecialidadCentro idEspecialidad;
    @JoinColumn(name = "id_nivel_educativo", referencedColumnName = "id_nivel_educativo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private NivelEducativo idNivelEducativo;
    @JoinColumn(name = "id_servicio_educativo", referencedColumnName = "id_servicio_educativo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ServicioEducativo idServicioEducativo;
    @JoinColumn(name = "id_tipo_control", referencedColumnName = "id_tipo_control")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoControl idTipoControl;
    @JoinColumn(name = "id_tipo_sostenimiento", referencedColumnName = "idtipo_sostenimiento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoSostenimiento idTipoSostenimiento;
    @JoinColumn(name = "id_turno_ia", referencedColumnName = "id_turno_ia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TurnoIa idTurnoIa;

    public InstitucionAcademica() {
    }

    public InstitucionAcademica(Integer idInstitucionAcademica) {
        this.idInstitucionAcademica = idInstitucionAcademica;
    }

    public InstitucionAcademica(Integer idInstitucionAcademica, String nombre, String claveCentro, String calle, double promedio, int cveEstado, int cveMunicipio, int cveLocalidad, int idPais) {
        this.idInstitucionAcademica = idInstitucionAcademica;
        this.nombre = nombre;
        this.claveCentro = claveCentro;
        this.calle = calle;
        this.promedio = promedio;
        this.cveEstado = cveEstado;
        this.cveMunicipio = cveMunicipio;
        this.cveLocalidad = cveLocalidad;
        this.idPais = idPais;
    }

    public Integer getIdInstitucionAcademica() {
        return idInstitucionAcademica;
    }

    public void setIdInstitucionAcademica(Integer idInstitucionAcademica) {
        this.idInstitucionAcademica = idInstitucionAcademica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClaveCentro() {
        return claveCentro;
    }

    public void setClaveCentro(String claveCentro) {
        this.claveCentro = claveCentro;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getCveEstado() {
        return cveEstado;
    }

    public void setCveEstado(int cveEstado) {
        this.cveEstado = cveEstado;
    }

    public int getCveMunicipio() {
        return cveMunicipio;
    }

    public void setCveMunicipio(int cveMunicipio) {
        this.cveMunicipio = cveMunicipio;
    }

    public int getCveLocalidad() {
        return cveLocalidad;
    }

    public void setCveLocalidad(int cveLocalidad) {
        this.cveLocalidad = cveLocalidad;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public EspecialidadCentro getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(EspecialidadCentro idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public NivelEducativo getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(NivelEducativo idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public ServicioEducativo getIdServicioEducativo() {
        return idServicioEducativo;
    }

    public void setIdServicioEducativo(ServicioEducativo idServicioEducativo) {
        this.idServicioEducativo = idServicioEducativo;
    }

    public TipoControl getIdTipoControl() {
        return idTipoControl;
    }

    public void setIdTipoControl(TipoControl idTipoControl) {
        this.idTipoControl = idTipoControl;
    }

    public TipoSostenimiento getIdTipoSostenimiento() {
        return idTipoSostenimiento;
    }

    public void setIdTipoSostenimiento(TipoSostenimiento idTipoSostenimiento) {
        this.idTipoSostenimiento = idTipoSostenimiento;
    }

    public TurnoIa getIdTurnoIa() {
        return idTurnoIa;
    }

    public void setIdTurnoIa(TurnoIa idTurnoIa) {
        this.idTurnoIa = idTurnoIa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstitucionAcademica != null ? idInstitucionAcademica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstitucionAcademica)) {
            return false;
        }
        InstitucionAcademica other = (InstitucionAcademica) object;
        if ((this.idInstitucionAcademica == null && other.idInstitucionAcademica != null) || (this.idInstitucionAcademica != null && !this.idInstitucionAcademica.equals(other.idInstitucionAcademica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.InstitucionAcademica[ idInstitucionAcademica=" + idInstitucionAcademica + " ]";
    }
    
}
