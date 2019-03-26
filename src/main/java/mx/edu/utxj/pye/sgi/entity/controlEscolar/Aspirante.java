/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "aspirante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aspirante.findAll", query = "SELECT a FROM Aspirante a")
    , @NamedQuery(name = "Aspirante.findByIdAspirante", query = "SELECT a FROM Aspirante a WHERE a.idAspirante = :idAspirante")
    , @NamedQuery(name = "Aspirante.findByFolioAspirante", query = "SELECT a FROM Aspirante a WHERE a.folioAspirante = :folioAspirante")
    , @NamedQuery(name = "Aspirante.findByEstatus", query = "SELECT a FROM Aspirante a WHERE a.estatus = :estatus")})
public class Aspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_aspirante")
    private Integer idAspirante;
    @Column(name = "folio_aspirante")
    private Integer folioAspirante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "aspirante1")
    private DatosAcademicos datosAcademicos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aspirante")
    private List<Estudiante> estudianteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "aspirante1")
    private DocumentoAspirante documentoAspirante;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aspirante")
    private List<EncuestaAspirante> encuestaAspiranteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "aspirante1")
    private Domicilio domicilio;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "aspirante1")
    private DatosFamiliares datosFamiliares;
    @JoinColumn(name = "id_persona", referencedColumnName = "idpersona")
    @OneToOne(optional = false)
    private Persona idPersona;
    @JoinColumn(name = "id_proceso_inscripcion", referencedColumnName = "id_procesos_inscripcion")
    @ManyToOne(optional = false)
    private ProcesosInscripcion idProcesoInscripcion;
    @JoinColumn(name = "tipo_aspirante", referencedColumnName = "id_tipo_aspirante")
    @ManyToOne(optional = false)
    private TipoAspirante tipoAspirante;

    public Aspirante() {
    }

    public Aspirante(Integer idAspirante) {
        this.idAspirante = idAspirante;
    }

    public Aspirante(Integer idAspirante, boolean estatus) {
        this.idAspirante = idAspirante;
        this.estatus = estatus;
    }

    public Integer getIdAspirante() {
        return idAspirante;
    }

    public void setIdAspirante(Integer idAspirante) {
        this.idAspirante = idAspirante;
    }

    public Integer getFolioAspirante() {
        return folioAspirante;
    }

    public void setFolioAspirante(Integer folioAspirante) {
        this.folioAspirante = folioAspirante;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public DatosAcademicos getDatosAcademicos() {
        return datosAcademicos;
    }

    public void setDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    public DocumentoAspirante getDocumentoAspirante() {
        return documentoAspirante;
    }

    public void setDocumentoAspirante(DocumentoAspirante documentoAspirante) {
        this.documentoAspirante = documentoAspirante;
    }

    @XmlTransient
    public List<EncuestaAspirante> getEncuestaAspiranteList() {
        return encuestaAspiranteList;
    }

    public void setEncuestaAspiranteList(List<EncuestaAspirante> encuestaAspiranteList) {
        this.encuestaAspiranteList = encuestaAspiranteList;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public DatosFamiliares getDatosFamiliares() {
        return datosFamiliares;
    }

    public void setDatosFamiliares(DatosFamiliares datosFamiliares) {
        this.datosFamiliares = datosFamiliares;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    public ProcesosInscripcion getIdProcesoInscripcion() {
        return idProcesoInscripcion;
    }

    public void setIdProcesoInscripcion(ProcesosInscripcion idProcesoInscripcion) {
        this.idProcesoInscripcion = idProcesoInscripcion;
    }

    public TipoAspirante getTipoAspirante() {
        return tipoAspirante;
    }

    public void setTipoAspirante(TipoAspirante tipoAspirante) {
        this.tipoAspirante = tipoAspirante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAspirante != null ? idAspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aspirante)) {
            return false;
        }
        Aspirante other = (Aspirante) object;
        if ((this.idAspirante == null && other.idAspirante != null) || (this.idAspirante != null && !this.idAspirante.equals(other.idAspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante[ idAspirante=" + idAspirante + " ]";
    }
    
}
