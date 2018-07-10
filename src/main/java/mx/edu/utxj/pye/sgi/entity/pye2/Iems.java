/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "iems", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Iems.findAll", query = "SELECT i FROM Iems i")
    , @NamedQuery(name = "Iems.findByIems", query = "SELECT i FROM Iems i WHERE i.iems = :iems")
    , @NamedQuery(name = "Iems.findByClave", query = "SELECT i FROM Iems i WHERE i.clave = :clave")
    , @NamedQuery(name = "Iems.findByTurno", query = "SELECT i FROM Iems i WHERE i.turno = :turno")
    , @NamedQuery(name = "Iems.findByAmbito", query = "SELECT i FROM Iems i WHERE i.ambito = :ambito")
    , @NamedQuery(name = "Iems.findByNombre", query = "SELECT i FROM Iems i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "Iems.findByTipo", query = "SELECT i FROM Iems i WHERE i.tipo = :tipo")
    , @NamedQuery(name = "Iems.findByLada", query = "SELECT i FROM Iems i WHERE i.lada = :lada")
    , @NamedQuery(name = "Iems.findByTelefono", query = "SELECT i FROM Iems i WHERE i.telefono = :telefono")
    , @NamedQuery(name = "Iems.findByResponsable", query = "SELECT i FROM Iems i WHERE i.responsable = :responsable")
    , @NamedQuery(name = "Iems.findByCorreo", query = "SELECT i FROM Iems i WHERE i.correo = :correo")})
public class Iems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iems")
    private Integer iems;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 26)
    @Column(name = "turno")
    private String turno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "ambito")
    private String ambito;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "domicilio")
    private String domicilio;
    @Size(max = 15)
    @Column(name = "lada")
    private String lada;
    @Size(max = 15)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 200)
    @Column(name = "responsable")
    private String responsable;
    @Size(max = 150)
    @Column(name = "correo")
    private String correo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iems")
    private List<DifusionIems> difusionIemsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iems")
    private List<FeriasParticipantes> feriasParticipantesList;
    @JoinColumns({
        @JoinColumn(name = "entidad", referencedColumnName = "claveEstado")
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio")
        , @JoinColumn(name = "localidad", referencedColumnName = "claveLocalidad")})
    @ManyToOne(optional = false)
    private Localidad localidad;
    @JoinColumn(name = "servedu", referencedColumnName = "serveducativo")
    @ManyToOne(optional = false)
    private IemsServedu servedu;

    public Iems() {
    }

    public Iems(Integer iems) {
        this.iems = iems;
    }

    public Iems(Integer iems, String clave, String turno, String ambito, String nombre, String tipo, String domicilio) {
        this.iems = iems;
        this.clave = clave;
        this.turno = turno;
        this.ambito = ambito;
        this.nombre = nombre;
        this.tipo = tipo;
        this.domicilio = domicilio;
    }

    public Integer getIems() {
        return iems;
    }

    public void setIems(Integer iems) {
        this.iems = iems;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLada() {
        return lada;
    }

    public void setLada(String lada) {
        this.lada = lada;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @XmlTransient
    public List<DifusionIems> getDifusionIemsList() {
        return difusionIemsList;
    }

    public void setDifusionIemsList(List<DifusionIems> difusionIemsList) {
        this.difusionIemsList = difusionIemsList;
    }

    @XmlTransient
    public List<FeriasParticipantes> getFeriasParticipantesList() {
        return feriasParticipantesList;
    }

    public void setFeriasParticipantesList(List<FeriasParticipantes> feriasParticipantesList) {
        this.feriasParticipantesList = feriasParticipantesList;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public IemsServedu getServedu() {
        return servedu;
    }

    public void setServedu(IemsServedu servedu) {
        this.servedu = servedu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iems != null ? iems.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Iems)) {
            return false;
        }
        Iems other = (Iems) object;
        if ((this.iems == null && other.iems != null) || (this.iems != null && !this.iems.equals(other.iems))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Iems[ iems=" + iems + " ]";
    }
    
}
