/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jonny
 */
@Entity
@Table(name = "idiomas", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Idiomas.findAll", query = "SELECT i FROM Idiomas i")
    , @NamedQuery(name = "Idiomas.findByIdioma", query = "SELECT i FROM Idiomas i WHERE i.idioma = :idioma")
    , @NamedQuery(name = "Idiomas.findByInstitucionOtorgante", query = "SELECT i FROM Idiomas i WHERE i.institucionOtorgante = :institucionOtorgante")
    , @NamedQuery(name = "Idiomas.findByIdiomaHablado", query = "SELECT i FROM Idiomas i WHERE i.idiomaHablado = :idiomaHablado")
    , @NamedQuery(name = "Idiomas.findByGradoDominio", query = "SELECT i FROM Idiomas i WHERE i.gradoDominio = :gradoDominio")
    , @NamedQuery(name = "Idiomas.findByNivelConversacion", query = "SELECT i FROM Idiomas i WHERE i.nivelConversacion = :nivelConversacion")
    , @NamedQuery(name = "Idiomas.findByNivelLectura", query = "SELECT i FROM Idiomas i WHERE i.nivelLectura = :nivelLectura")
    , @NamedQuery(name = "Idiomas.findByNivelEscritura", query = "SELECT i FROM Idiomas i WHERE i.nivelEscritura = :nivelEscritura")
    , @NamedQuery(name = "Idiomas.findByCertificacion", query = "SELECT i FROM Idiomas i WHERE i.certificacion = :certificacion")
    , @NamedQuery(name = "Idiomas.findByFechaEvaluacion", query = "SELECT i FROM Idiomas i WHERE i.fechaEvaluacion = :fechaEvaluacion")
    , @NamedQuery(name = "Idiomas.findByEvidenciaDoc", query = "SELECT i FROM Idiomas i WHERE i.evidenciaDoc = :evidenciaDoc")
    , @NamedQuery(name = "Idiomas.findByFechaVigenciaDe", query = "SELECT i FROM Idiomas i WHERE i.fechaVigenciaDe = :fechaVigenciaDe")
    , @NamedQuery(name = "Idiomas.findByFechaVigenciaA", query = "SELECT i FROM Idiomas i WHERE i.fechaVigenciaA = :fechaVigenciaA")
    , @NamedQuery(name = "Idiomas.findByEstatus", query = "SELECT i FROM Idiomas i WHERE i.estatus = :estatus")})
public class Idiomas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idioma")
    private Integer idioma;
    @Size(max = 500)
    @Column(name = "institucion_otorgante")
    private String institucionOtorgante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "idioma_hablado")
    private String idiomaHablado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "grado_dominio")
    private String gradoDominio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "nivel_conversacion")
    private String nivelConversacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "nivel_lectura")
    private String nivelLectura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "nivel_escritura")
    private String nivelEscritura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "certificacion")
    private String certificacion;
    @Column(name = "fecha_evaluacion")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacion;
    @Size(max = 500)
    @Column(name = "evidencia_doc")
    private String evidenciaDoc;
    @Column(name = "fecha_vigencia_de")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaDe;
    @Column(name = "fecha_vigencia_a")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaA;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Idiomas() {
    }

    public Idiomas(Integer idioma) {
        this.idioma = idioma;
    }

    public Idiomas(Integer idioma, String idiomaHablado, String gradoDominio, String nivelConversacion, String nivelLectura, String nivelEscritura, String certificacion, String estatus) {
        this.idioma = idioma;
        this.idiomaHablado = idiomaHablado;
        this.gradoDominio = gradoDominio;
        this.nivelConversacion = nivelConversacion;
        this.nivelLectura = nivelLectura;
        this.nivelEscritura = nivelEscritura;
        this.certificacion = certificacion;
        this.estatus = estatus;
    }

    public Integer getIdioma() {
        return idioma;
    }

    public void setIdioma(Integer idioma) {
        this.idioma = idioma;
    }

    public String getInstitucionOtorgante() {
        return institucionOtorgante;
    }

    public void setInstitucionOtorgante(String institucionOtorgante) {
        this.institucionOtorgante = institucionOtorgante;
    }

    public String getIdiomaHablado() {
        return idiomaHablado;
    }

    public void setIdiomaHablado(String idiomaHablado) {
        this.idiomaHablado = idiomaHablado;
    }

    public String getGradoDominio() {
        return gradoDominio;
    }

    public void setGradoDominio(String gradoDominio) {
        this.gradoDominio = gradoDominio;
    }

    public String getNivelConversacion() {
        return nivelConversacion;
    }

    public void setNivelConversacion(String nivelConversacion) {
        this.nivelConversacion = nivelConversacion;
    }

    public String getNivelLectura() {
        return nivelLectura;
    }

    public void setNivelLectura(String nivelLectura) {
        this.nivelLectura = nivelLectura;
    }

    public String getNivelEscritura() {
        return nivelEscritura;
    }

    public void setNivelEscritura(String nivelEscritura) {
        this.nivelEscritura = nivelEscritura;
    }

    public String getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(String certificacion) {
        this.certificacion = certificacion;
    }

    public Date getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(Date fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public String getEvidenciaDoc() {
        return evidenciaDoc;
    }

    public void setEvidenciaDoc(String evidenciaDoc) {
        this.evidenciaDoc = evidenciaDoc;
    }

    public Date getFechaVigenciaDe() {
        return fechaVigenciaDe;
    }

    public void setFechaVigenciaDe(Date fechaVigenciaDe) {
        this.fechaVigenciaDe = fechaVigenciaDe;
    }

    public Date getFechaVigenciaA() {
        return fechaVigenciaA;
    }

    public void setFechaVigenciaA(Date fechaVigenciaA) {
        this.fechaVigenciaA = fechaVigenciaA;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idioma != null ? idioma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Idiomas)) {
            return false;
        }
        Idiomas other = (Idiomas) object;
        if ((this.idioma == null && other.idioma != null) || (this.idioma != null && !this.idioma.equals(other.idioma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Idiomas[ idioma=" + idioma + " ]";
    }
    
}
