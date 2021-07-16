/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "documento_estudiante_proceso", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoEstudianteProceso.findAll", query = "SELECT d FROM DocumentoEstudianteProceso d")
    , @NamedQuery(name = "DocumentoEstudianteProceso.findByDocumentoEstudiante", query = "SELECT d FROM DocumentoEstudianteProceso d WHERE d.documentoEstudiante = :documentoEstudiante")})
public class DocumentoEstudianteProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento_estudiante")
    private Integer documentoEstudiante;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne
    private Estudiante estudiante;
    @JoinColumn(name = "documento", referencedColumnName = "documento_proceso")
    @ManyToOne
    private DocumentoProceso documento;

    public DocumentoEstudianteProceso() {
    }

    public DocumentoEstudianteProceso(Integer documentoEstudiante) {
        this.documentoEstudiante = documentoEstudiante;
    }

    public Integer getDocumentoEstudiante() {
        return documentoEstudiante;
    }

    public void setDocumentoEstudiante(Integer documentoEstudiante) {
        this.documentoEstudiante = documentoEstudiante;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public DocumentoProceso getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoProceso documento) {
        this.documento = documento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoEstudiante != null ? documentoEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoEstudianteProceso)) {
            return false;
        }
        DocumentoEstudianteProceso other = (DocumentoEstudianteProceso) object;
        if ((this.documentoEstudiante == null && other.documentoEstudiante != null) || (this.documentoEstudiante != null && !this.documentoEstudiante.equals(other.documentoEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoEstudianteProceso[ documentoEstudiante=" + documentoEstudiante + " ]";
    }
    
}
