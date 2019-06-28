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
@Table(name = "documentosentregadosestudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentosentregadosestudiante.findAll", query = "SELECT d FROM Documentosentregadosestudiante d")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByEstudiante", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.estudiante = :estudiante")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByActaNacimiento", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.actaNacimiento = :actaNacimiento")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCopiaActaNacimiento", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.copiaActaNacimiento = :copiaActaNacimiento")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCertificadoIems", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.certificadoIems = :certificadoIems")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCopiaCertificadoIems", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.copiaCertificadoIems = :copiaCertificadoIems")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByHistorial", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.historial = :historial")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCurp", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.curp = :curp")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByTipoSangre", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.tipoSangre = :tipoSangre")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByAcreditacionEstadia", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.acreditacionEstadia = :acreditacionEstadia")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCopiaTitulo", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.copiaTitulo = :copiaTitulo")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCopiaCedula", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.copiaCedula = :copiaCedula")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByCertificadoTSU", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.certificadoTSU = :certificadoTSU")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByActaExcencionExamen", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.actaExcencionExamen = :actaExcencionExamen")
    , @NamedQuery(name = "Documentosentregadosestudiante.findByPagoColegiatura", query = "SELECT d FROM Documentosentregadosestudiante d WHERE d.pagoColegiatura = :pagoColegiatura")})
public class Documentosentregadosestudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "estudiante")
    private Integer estudiante;
    @Column(name = "acta_nacimiento")
    private Boolean actaNacimiento;
    @Column(name = "copia_acta_nacimiento")
    private Boolean copiaActaNacimiento;
    @Column(name = "certificado_iems")
    private Boolean certificadoIems;
    @Column(name = "copia_certificado_iems")
    private Boolean copiaCertificadoIems;
    @Column(name = "historial")
    private Boolean historial;
    @Column(name = "curp")
    private Boolean curp;
    @Column(name = "tipo_sangre")
    private Boolean tipoSangre;
    @Column(name = "acreditacion_estadia")
    private Boolean acreditacionEstadia;
    @Column(name = "copia_titulo")
    private Boolean copiaTitulo;
    @Column(name = "copia_cedula")
    private Boolean copiaCedula;
    @Column(name = "certificadoTSU")
    private Boolean certificadoTSU;
    @Column(name = "acta_excencion_examen")
    private Boolean actaExcencionExamen;
    @Column(name = "pago_colegiatura")
    private Boolean pagoColegiatura;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Inscripcion inscripcion;

    public Documentosentregadosestudiante() {
    }

    public Documentosentregadosestudiante(Integer estudiante) {
        this.estudiante = estudiante;
    }

    public Integer getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Integer estudiante) {
        this.estudiante = estudiante;
    }

    public Boolean getActaNacimiento() {
        return actaNacimiento;
    }

    public void setActaNacimiento(Boolean actaNacimiento) {
        this.actaNacimiento = actaNacimiento;
    }

    public Boolean getCopiaActaNacimiento() {
        return copiaActaNacimiento;
    }

    public void setCopiaActaNacimiento(Boolean copiaActaNacimiento) {
        this.copiaActaNacimiento = copiaActaNacimiento;
    }

    public Boolean getCertificadoIems() {
        return certificadoIems;
    }

    public void setCertificadoIems(Boolean certificadoIems) {
        this.certificadoIems = certificadoIems;
    }

    public Boolean getCopiaCertificadoIems() {
        return copiaCertificadoIems;
    }

    public void setCopiaCertificadoIems(Boolean copiaCertificadoIems) {
        this.copiaCertificadoIems = copiaCertificadoIems;
    }

    public Boolean getHistorial() {
        return historial;
    }

    public void setHistorial(Boolean historial) {
        this.historial = historial;
    }

    public Boolean getCurp() {
        return curp;
    }

    public void setCurp(Boolean curp) {
        this.curp = curp;
    }

    public Boolean getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(Boolean tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public Boolean getAcreditacionEstadia() {
        return acreditacionEstadia;
    }

    public void setAcreditacionEstadia(Boolean acreditacionEstadia) {
        this.acreditacionEstadia = acreditacionEstadia;
    }

    public Boolean getCopiaTitulo() {
        return copiaTitulo;
    }

    public void setCopiaTitulo(Boolean copiaTitulo) {
        this.copiaTitulo = copiaTitulo;
    }

    public Boolean getCopiaCedula() {
        return copiaCedula;
    }

    public void setCopiaCedula(Boolean copiaCedula) {
        this.copiaCedula = copiaCedula;
    }

    public Boolean getCertificadoTSU() {
        return certificadoTSU;
    }

    public void setCertificadoTSU(Boolean certificadoTSU) {
        this.certificadoTSU = certificadoTSU;
    }

    public Boolean getActaExcencionExamen() {
        return actaExcencionExamen;
    }

    public void setActaExcencionExamen(Boolean actaExcencionExamen) {
        this.actaExcencionExamen = actaExcencionExamen;
    }

    public Boolean getPagoColegiatura() {
        return pagoColegiatura;
    }

    public void setPagoColegiatura(Boolean pagoColegiatura) {
        this.pagoColegiatura = pagoColegiatura;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estudiante != null ? estudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documentosentregadosestudiante)) {
            return false;
        }
        Documentosentregadosestudiante other = (Documentosentregadosestudiante) object;
        if ((this.estudiante == null && other.estudiante != null) || (this.estudiante != null && !this.estudiante.equals(other.estudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante[ estudiante=" + estudiante + " ]";
    }
    
}
