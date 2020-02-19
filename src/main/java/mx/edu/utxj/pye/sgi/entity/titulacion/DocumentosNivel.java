/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "documentos_nivel", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosNivel.findAll", query = "SELECT d FROM DocumentosNivel d")
    , @NamedQuery(name = "DocumentosNivel.findByDocumento", query = "SELECT d FROM DocumentosNivel d WHERE d.documentosNivelPK.documento = :documento")
    , @NamedQuery(name = "DocumentosNivel.findByNivel", query = "SELECT d FROM DocumentosNivel d WHERE d.documentosNivelPK.nivel = :nivel")})
public class DocumentosNivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentosNivelPK documentosNivelPK;
    @JoinColumn(name = "documento", referencedColumnName = "documento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Documentos documentos;

    public DocumentosNivel() {
    }

    public DocumentosNivel(DocumentosNivelPK documentosNivelPK) {
        this.documentosNivelPK = documentosNivelPK;
    }

    public DocumentosNivel(int documento, int nivel) {
        this.documentosNivelPK = new DocumentosNivelPK(documento, nivel);
    }

    public DocumentosNivelPK getDocumentosNivelPK() {
        return documentosNivelPK;
    }

    public void setDocumentosNivelPK(DocumentosNivelPK documentosNivelPK) {
        this.documentosNivelPK = documentosNivelPK;
    }

    public Documentos getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Documentos documentos) {
        this.documentos = documentos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentosNivelPK != null ? documentosNivelPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosNivel)) {
            return false;
        }
        DocumentosNivel other = (DocumentosNivel) object;
        if ((this.documentosNivelPK == null && other.documentosNivelPK != null) || (this.documentosNivelPK != null && !this.documentosNivelPK.equals(other.documentosNivelPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosNivel[ documentosNivelPK=" + documentosNivelPK + " ]";
    }
    
}
