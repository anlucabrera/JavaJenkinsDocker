/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "aclaracion", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aclaracion.findAll", query = "SELECT a FROM Aclaracion a")
    , @NamedQuery(name = "Aclaracion.findByCveAclaracion", query = "SELECT a FROM Aclaracion a WHERE a.cveAclaracion = :cveAclaracion")
    , @NamedQuery(name = "Aclaracion.findByDescripcion", query = "SELECT a FROM Aclaracion a WHERE a.descripcion = :descripcion")
    , @NamedQuery(name = "Aclaracion.findByFecha", query = "SELECT a FROM Aclaracion a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "Aclaracion.findByRecibido", query = "SELECT a FROM Aclaracion a WHERE a.recibido = :recibido")
    , @NamedQuery(name = "Aclaracion.findByIgnorar", query = "SELECT a FROM Aclaracion a WHERE a.ignorar = :ignorar")})
public class Aclaracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cve_aclaracion")
    private Integer cveAclaracion;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "recibido")
    private Character recibido;
    @Column(name = "ignorar")
    private Character ignorar;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo", referencedColumnName = "periodo")})
    @ManyToOne
    private AlumnoFinanzas alumnoFinanzas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cveAclaracion")
    private List<SeguimientoAclaracion> seguimientoAclaracionList;

    public Aclaracion() {
    }

    public Aclaracion(Integer cveAclaracion) {
        this.cveAclaracion = cveAclaracion;
    }

    public Integer getCveAclaracion() {
        return cveAclaracion;
    }

    public void setCveAclaracion(Integer cveAclaracion) {
        this.cveAclaracion = cveAclaracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Character getRecibido() {
        return recibido;
    }

    public void setRecibido(Character recibido) {
        this.recibido = recibido;
    }

    public Character getIgnorar() {
        return ignorar;
    }

    public void setIgnorar(Character ignorar) {
        this.ignorar = ignorar;
    }

    public AlumnoFinanzas getAlumnoFinanzas() {
        return alumnoFinanzas;
    }

    public void setAlumnoFinanzas(AlumnoFinanzas alumnoFinanzas) {
        this.alumnoFinanzas = alumnoFinanzas;
    }

    @XmlTransient
    public List<SeguimientoAclaracion> getSeguimientoAclaracionList() {
        return seguimientoAclaracionList;
    }

    public void setSeguimientoAclaracionList(List<SeguimientoAclaracion> seguimientoAclaracionList) {
        this.seguimientoAclaracionList = seguimientoAclaracionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveAclaracion != null ? cveAclaracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aclaracion)) {
            return false;
        }
        Aclaracion other = (Aclaracion) object;
        if ((this.cveAclaracion == null && other.cveAclaracion != null) || (this.cveAclaracion != null && !this.cveAclaracion.equals(other.cveAclaracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Aclaracion[ cveAclaracion=" + cveAclaracion + " ]";
    }
    
}
