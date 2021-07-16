/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "documento_proceso", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoProceso.findAll", query = "SELECT d FROM DocumentoProceso d")
    , @NamedQuery(name = "DocumentoProceso.findByDocumentoProceso", query = "SELECT d FROM DocumentoProceso d WHERE d.documentoProceso = :documentoProceso")
    , @NamedQuery(name = "DocumentoProceso.findByProceso", query = "SELECT d FROM DocumentoProceso d WHERE d.proceso = :proceso")
    , @NamedQuery(name = "DocumentoProceso.findByObligatorio", query = "SELECT d FROM DocumentoProceso d WHERE d.obligatorio = :obligatorio")})
public class DocumentoProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento_proceso")
    private Integer documentoProceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "proceso")
    private String proceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "obligatorio")
    private boolean obligatorio;
    @JoinColumn(name = "documento", referencedColumnName = "documento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documento documento;
    @OneToMany(mappedBy = "documento")
    private List<DocumentoEstudianteProceso> documentoEstudianteProcesoList;
    public DocumentoProceso() {
    }

    public DocumentoProceso(Integer documentoProceso) {
        this.documentoProceso = documentoProceso;
    }

    public DocumentoProceso(Integer documentoProceso, String proceso, boolean obligatorio) {
        this.documentoProceso = documentoProceso;
        this.proceso = proceso;
        this.obligatorio = obligatorio;
    }

    public Integer getDocumentoProceso() {
        return documentoProceso;
    }

    public void setDocumentoProceso(Integer documentoProceso) {
        this.documentoProceso = documentoProceso;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @XmlTransient
    public List<DocumentoEstudianteProceso> getDocumentoEstudianteProcesoList() {
        return documentoEstudianteProcesoList;
    }

    public void setDocumentoEstudianteProcesoList(List<DocumentoEstudianteProceso> documentoEstudianteProcesoList) {
        this.documentoEstudianteProcesoList = documentoEstudianteProcesoList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoProceso != null ? documentoProceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoProceso)) {
            return false;
        }
        DocumentoProceso other = (DocumentoProceso) object;
        if ((this.documentoProceso == null && other.documentoProceso != null) || (this.documentoProceso != null && !this.documentoProceso.equals(other.documentoProceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso[ documentoProceso=" + documentoProceso + " ]";
    }
    
}
