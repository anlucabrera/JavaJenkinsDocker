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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "documento_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoEstudiante.findAll", query = "SELECT d FROM DocumentoEstudiante d")
    , @NamedQuery(name = "DocumentoEstudiante.findByIdDocumentoEstudiante", query = "SELECT d FROM DocumentoEstudiante d WHERE d.idDocumentoEstudiante = :idDocumentoEstudiante")
    , @NamedQuery(name = "DocumentoEstudiante.findByEvidenciaActaNacimiento", query = "SELECT d FROM DocumentoEstudiante d WHERE d.evidenciaActaNacimiento = :evidenciaActaNacimiento")
    , @NamedQuery(name = "DocumentoEstudiante.findByEvidenciaCertificado", query = "SELECT d FROM DocumentoEstudiante d WHERE d.evidenciaCertificado = :evidenciaCertificado")
    , @NamedQuery(name = "DocumentoEstudiante.findByEvidenciaCertificadoTsu", query = "SELECT d FROM DocumentoEstudiante d WHERE d.evidenciaCertificadoTsu = :evidenciaCertificadoTsu")
    , @NamedQuery(name = "DocumentoEstudiante.findByAvidenciaActaExcencion", query = "SELECT d FROM DocumentoEstudiante d WHERE d.avidenciaActaExcencion = :avidenciaActaExcencion")
    , @NamedQuery(name = "DocumentoEstudiante.findByEvidenciaLiberacionEstadia", query = "SELECT d FROM DocumentoEstudiante d WHERE d.evidenciaLiberacionEstadia = :evidenciaLiberacionEstadia")})
public class DocumentoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_documento_estudiante")
    private Integer idDocumentoEstudiante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "evidencia_acta_nacimiento")
    private String evidenciaActaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "evidencia_certificado")
    private String evidenciaCertificado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "evidencia_certificado_tsu")
    private String evidenciaCertificadoTsu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "avidencia_acta_excencion")
    private String avidenciaActaExcencion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "evidencia_liberacion_estadia")
    private String evidenciaLiberacionEstadia;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante estudiante;

    public DocumentoEstudiante() {
    }

    public DocumentoEstudiante(Integer idDocumentoEstudiante) {
        this.idDocumentoEstudiante = idDocumentoEstudiante;
    }

    public DocumentoEstudiante(Integer idDocumentoEstudiante, String evidenciaActaNacimiento, String evidenciaCertificado, String evidenciaCertificadoTsu, String avidenciaActaExcencion, String evidenciaLiberacionEstadia) {
        this.idDocumentoEstudiante = idDocumentoEstudiante;
        this.evidenciaActaNacimiento = evidenciaActaNacimiento;
        this.evidenciaCertificado = evidenciaCertificado;
        this.evidenciaCertificadoTsu = evidenciaCertificadoTsu;
        this.avidenciaActaExcencion = avidenciaActaExcencion;
        this.evidenciaLiberacionEstadia = evidenciaLiberacionEstadia;
    }

    public Integer getIdDocumentoEstudiante() {
        return idDocumentoEstudiante;
    }

    public void setIdDocumentoEstudiante(Integer idDocumentoEstudiante) {
        this.idDocumentoEstudiante = idDocumentoEstudiante;
    }

    public String getEvidenciaActaNacimiento() {
        return evidenciaActaNacimiento;
    }

    public void setEvidenciaActaNacimiento(String evidenciaActaNacimiento) {
        this.evidenciaActaNacimiento = evidenciaActaNacimiento;
    }

    public String getEvidenciaCertificado() {
        return evidenciaCertificado;
    }

    public void setEvidenciaCertificado(String evidenciaCertificado) {
        this.evidenciaCertificado = evidenciaCertificado;
    }

    public String getEvidenciaCertificadoTsu() {
        return evidenciaCertificadoTsu;
    }

    public void setEvidenciaCertificadoTsu(String evidenciaCertificadoTsu) {
        this.evidenciaCertificadoTsu = evidenciaCertificadoTsu;
    }

    public String getAvidenciaActaExcencion() {
        return avidenciaActaExcencion;
    }

    public void setAvidenciaActaExcencion(String avidenciaActaExcencion) {
        this.avidenciaActaExcencion = avidenciaActaExcencion;
    }

    public String getEvidenciaLiberacionEstadia() {
        return evidenciaLiberacionEstadia;
    }

    public void setEvidenciaLiberacionEstadia(String evidenciaLiberacionEstadia) {
        this.evidenciaLiberacionEstadia = evidenciaLiberacionEstadia;
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
        hash += (idDocumentoEstudiante != null ? idDocumentoEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoEstudiante)) {
            return false;
        }
        DocumentoEstudiante other = (DocumentoEstudiante) object;
        if ((this.idDocumentoEstudiante == null && other.idDocumentoEstudiante != null) || (this.idDocumentoEstudiante != null && !this.idDocumentoEstudiante.equals(other.idDocumentoEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoEstudiante[ idDocumentoEstudiante=" + idDocumentoEstudiante + " ]";
    }
    
}
