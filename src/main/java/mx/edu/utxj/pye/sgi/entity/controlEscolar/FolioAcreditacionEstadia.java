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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "folio_acreditacion_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FolioAcreditacionEstadia.findAll", query = "SELECT f FROM FolioAcreditacionEstadia f")
    , @NamedQuery(name = "FolioAcreditacionEstadia.findByFolio", query = "SELECT f FROM FolioAcreditacionEstadia f WHERE f.folio = :folio")
    , @NamedQuery(name = "FolioAcreditacionEstadia.findByNumero", query = "SELECT f FROM FolioAcreditacionEstadia f WHERE f.numero = :numero")
    , @NamedQuery(name = "FolioAcreditacionEstadia.findByFolioCompleto", query = "SELECT f FROM FolioAcreditacionEstadia f WHERE f.folioCompleto = :folioCompleto")
    , @NamedQuery(name = "FolioAcreditacionEstadia.findByCodigoQr", query = "SELECT f FROM FolioAcreditacionEstadia f WHERE f.codigoQr = :codigoQr")})
public class FolioAcreditacionEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "folio")
    private Integer folio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "folio_completo")
    private String folioCompleto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "codigo_qr")
    private String codigoQr;
    @JoinColumn(name = "seguimiento", referencedColumnName = "seguimiento")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private SeguimientoEstadiaEstudiante seguimiento;

    public FolioAcreditacionEstadia() {
    }

    public FolioAcreditacionEstadia(Integer folio) {
        this.folio = folio;
    }

    public FolioAcreditacionEstadia(Integer folio, int numero, String folioCompleto, String codigoQr) {
        this.folio = folio;
        this.numero = numero;
        this.folioCompleto = folioCompleto;
        this.codigoQr = codigoQr;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFolioCompleto() {
        return folioCompleto;
    }

    public void setFolioCompleto(String folioCompleto) {
        this.folioCompleto = folioCompleto;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public SeguimientoEstadiaEstudiante getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(SeguimientoEstadiaEstudiante seguimiento) {
        this.seguimiento = seguimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (folio != null ? folio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FolioAcreditacionEstadia)) {
            return false;
        }
        FolioAcreditacionEstadia other = (FolioAcreditacionEstadia) object;
        if ((this.folio == null && other.folio != null) || (this.folio != null && !this.folio.equals(other.folio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.FolioAcreditacionEstadia[ folio=" + folio + " ]";
    }
    
}
