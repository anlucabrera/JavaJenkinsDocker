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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "seguros_facultativos_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SegurosFacultativosEstudiante.findAll", query = "SELECT s FROM SegurosFacultativosEstudiante s")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findBySeguroFacultativoEstudiante", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.seguroFacultativoEstudiante = :seguroFacultativoEstudiante")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByCorreoElectronico", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByNss", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.nss = :nss")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByTipoSeguro", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.tipoSeguro = :tipoSeguro")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByEstatusTarjeton", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.estatusTarjeton = :estatusTarjeton")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByEstatusComprobanteLocalizacion", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.estatusComprobanteLocalizacion = :estatusComprobanteLocalizacion")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByEstatusComprobanteVigenciaDerechos", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.estatusComprobanteVigenciaDerechos = :estatusComprobanteVigenciaDerechos")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByValidacionEnfermeria", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.validacionEnfermeria = :validacionEnfermeria")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByFechaRegistro", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByEstatusRegistro", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.estatusRegistro = :estatusRegistro")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByComentariosEnfermeria", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.comentariosEnfermeria = :comentariosEnfermeria")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByFechaAlta", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.fechaAlta = :fechaAlta")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByFechaBaja", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.fechaBaja = :fechaBaja")
    , @NamedQuery(name = "SegurosFacultativosEstudiante.findByUsuarioOperacion", query = "SELECT s FROM SegurosFacultativosEstudiante s WHERE s.usuarioOperacion = :usuarioOperacion")})
public class SegurosFacultativosEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "seguro_facultativo_estudiante")
    private Integer seguroFacultativoEstudiante;
    @Size(max = 200)
    @Column(name = "correo_electronico")
    private String correoElectronico;
    @Size(max = 255)
    @Column(name = "nss")
    private String nss;
    @Size(max = 11)
    @Column(name = "tipo_seguro")
    private String tipoSeguro;
    @Lob
    @Size(max = 65535)
    @Column(name = "ruta_tarjeton")
    private String rutaTarjeton;
    @Size(max = 23)
    @Column(name = "estatus_tarjeton")
    private String estatusTarjeton;
    @Lob
    @Size(max = 65535)
    @Column(name = "ruta_comprobante_localizacion")
    private String rutaComprobanteLocalizacion;
    @Size(max = 23)
    @Column(name = "estatus_comprobante_localizacion")
    private String estatusComprobanteLocalizacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "ruta_comprobante_vigencia_de_derechos")
    private String rutaComprobanteVigenciaDeDerechos;
    @Size(max = 23)
    @Column(name = "estatus_comprobante_vigencia_derechos")
    private String estatusComprobanteVigenciaDerechos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "validacion_enfermeria")
    private String validacionEnfermeria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "estatus_registro")
    private String estatusRegistro;
    @Size(max = 1000)
    @Column(name = "comentarios_enfermeria")
    private String comentariosEnfermeria;
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Column(name = "usuario_operacion")
    private Integer usuarioOperacion;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante estudiante;

    public SegurosFacultativosEstudiante() {
    }

    public SegurosFacultativosEstudiante(Integer seguroFacultativoEstudiante) {
        this.seguroFacultativoEstudiante = seguroFacultativoEstudiante;
    }

    public SegurosFacultativosEstudiante(Integer seguroFacultativoEstudiante, String validacionEnfermeria, Date fechaRegistro, String estatusRegistro) {
        this.seguroFacultativoEstudiante = seguroFacultativoEstudiante;
        this.validacionEnfermeria = validacionEnfermeria;
        this.fechaRegistro = fechaRegistro;
        this.estatusRegistro = estatusRegistro;
    }

    public Integer getSeguroFacultativoEstudiante() {
        return seguroFacultativoEstudiante;
    }

    public void setSeguroFacultativoEstudiante(Integer seguroFacultativoEstudiante) {
        this.seguroFacultativoEstudiante = seguroFacultativoEstudiante;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public String getRutaTarjeton() {
        return rutaTarjeton;
    }

    public void setRutaTarjeton(String rutaTarjeton) {
        this.rutaTarjeton = rutaTarjeton;
    }

    public String getEstatusTarjeton() {
        return estatusTarjeton;
    }

    public void setEstatusTarjeton(String estatusTarjeton) {
        this.estatusTarjeton = estatusTarjeton;
    }

    public String getRutaComprobanteLocalizacion() {
        return rutaComprobanteLocalizacion;
    }

    public void setRutaComprobanteLocalizacion(String rutaComprobanteLocalizacion) {
        this.rutaComprobanteLocalizacion = rutaComprobanteLocalizacion;
    }

    public String getEstatusComprobanteLocalizacion() {
        return estatusComprobanteLocalizacion;
    }

    public void setEstatusComprobanteLocalizacion(String estatusComprobanteLocalizacion) {
        this.estatusComprobanteLocalizacion = estatusComprobanteLocalizacion;
    }

    public String getRutaComprobanteVigenciaDeDerechos() {
        return rutaComprobanteVigenciaDeDerechos;
    }

    public void setRutaComprobanteVigenciaDeDerechos(String rutaComprobanteVigenciaDeDerechos) {
        this.rutaComprobanteVigenciaDeDerechos = rutaComprobanteVigenciaDeDerechos;
    }

    public String getEstatusComprobanteVigenciaDerechos() {
        return estatusComprobanteVigenciaDerechos;
    }

    public void setEstatusComprobanteVigenciaDerechos(String estatusComprobanteVigenciaDerechos) {
        this.estatusComprobanteVigenciaDerechos = estatusComprobanteVigenciaDerechos;
    }

    public String getValidacionEnfermeria() {
        return validacionEnfermeria;
    }

    public void setValidacionEnfermeria(String validacionEnfermeria) {
        this.validacionEnfermeria = validacionEnfermeria;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstatusRegistro() {
        return estatusRegistro;
    }

    public void setEstatusRegistro(String estatusRegistro) {
        this.estatusRegistro = estatusRegistro;
    }

    public String getComentariosEnfermeria() {
        return comentariosEnfermeria;
    }

    public void setComentariosEnfermeria(String comentariosEnfermeria) {
        this.comentariosEnfermeria = comentariosEnfermeria;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Integer getUsuarioOperacion() {
        return usuarioOperacion;
    }

    public void setUsuarioOperacion(Integer usuarioOperacion) {
        this.usuarioOperacion = usuarioOperacion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seguroFacultativoEstudiante != null ? seguroFacultativoEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SegurosFacultativosEstudiante)) {
            return false;
        }
        SegurosFacultativosEstudiante other = (SegurosFacultativosEstudiante) object;
        if ((this.seguroFacultativoEstudiante == null && other.seguroFacultativoEstudiante != null) || (this.seguroFacultativoEstudiante != null && !this.seguroFacultativoEstudiante.equals(other.seguroFacultativoEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante[ seguroFacultativoEstudiante=" + seguroFacultativoEstudiante + " ]";
    }
    
}
