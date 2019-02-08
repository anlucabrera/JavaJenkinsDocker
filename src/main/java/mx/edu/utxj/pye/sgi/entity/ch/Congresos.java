/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author jonny
 */
@Entity
@Table(name = "congresos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Congresos.findAll", query = "SELECT c FROM Congresos c")
    , @NamedQuery(name = "Congresos.findByCongreso", query = "SELECT c FROM Congresos c WHERE c.congreso = :congreso")
    , @NamedQuery(name = "Congresos.findByNombreCongreso", query = "SELECT c FROM Congresos c WHERE c.nombreCongreso = :nombreCongreso")
    , @NamedQuery(name = "Congresos.findByTituloTrabajo", query = "SELECT c FROM Congresos c WHERE c.tituloTrabajo = :tituloTrabajo")
    , @NamedQuery(name = "Congresos.findByParticipacion", query = "SELECT c FROM Congresos c WHERE c.participacion = :participacion")
    , @NamedQuery(name = "Congresos.findByLugarPais", query = "SELECT c FROM Congresos c WHERE c.lugarPais = :lugarPais")
    , @NamedQuery(name = "Congresos.findByFechaCongreso", query = "SELECT c FROM Congresos c WHERE c.fechaCongreso = :fechaCongreso")
    , @NamedQuery(name = "Congresos.findByPalabraClave1", query = "SELECT c FROM Congresos c WHERE c.palabraClave1 = :palabraClave1")
    , @NamedQuery(name = "Congresos.findByPalabraClave2", query = "SELECT c FROM Congresos c WHERE c.palabraClave2 = :palabraClave2")
    , @NamedQuery(name = "Congresos.findByPalabraClave3", query = "SELECT c FROM Congresos c WHERE c.palabraClave3 = :palabraClave3")
    , @NamedQuery(name = "Congresos.findByEstatus", query = "SELECT c FROM Congresos c WHERE c.estatus = :estatus")})
public class Congresos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "congreso")
    private Integer congreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_congreso")
    private String nombreCongreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo_trabajo")
    private String tituloTrabajo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 33)
    @Column(name = "participacion")
    private String participacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "lugar_pais")
    private String lugarPais;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "fecha_congreso")
    private String fechaCongreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra_clave1")
    private String palabraClave1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra_clave2")
    private String palabraClave2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "palabra_clave3")
    private String palabraClave3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Congresos() {
    }

    public Congresos(Integer congreso) {
        this.congreso = congreso;
    }

    public Congresos(Integer congreso, String nombreCongreso, String tituloTrabajo, String participacion, String lugarPais, String fechaCongreso, String palabraClave1, String palabraClave2, String palabraClave3, String estatus) {
        this.congreso = congreso;
        this.nombreCongreso = nombreCongreso;
        this.tituloTrabajo = tituloTrabajo;
        this.participacion = participacion;
        this.lugarPais = lugarPais;
        this.fechaCongreso = fechaCongreso;
        this.palabraClave1 = palabraClave1;
        this.palabraClave2 = palabraClave2;
        this.palabraClave3 = palabraClave3;
        this.estatus = estatus;
    }

    public Integer getCongreso() {
        return congreso;
    }

    public void setCongreso(Integer congreso) {
        this.congreso = congreso;
    }

    public String getNombreCongreso() {
        return nombreCongreso;
    }

    public void setNombreCongreso(String nombreCongreso) {
        this.nombreCongreso = nombreCongreso;
    }

    public String getTituloTrabajo() {
        return tituloTrabajo;
    }

    public void setTituloTrabajo(String tituloTrabajo) {
        this.tituloTrabajo = tituloTrabajo;
    }

    public String getParticipacion() {
        return participacion;
    }

    public void setParticipacion(String participacion) {
        this.participacion = participacion;
    }

    public String getLugarPais() {
        return lugarPais;
    }

    public void setLugarPais(String lugarPais) {
        this.lugarPais = lugarPais;
    }

    public String getFechaCongreso() {
        return fechaCongreso;
    }

    public void setFechaCongreso(String fechaCongreso) {
        this.fechaCongreso = fechaCongreso;
    }

    public String getPalabraClave1() {
        return palabraClave1;
    }

    public void setPalabraClave1(String palabraClave1) {
        this.palabraClave1 = palabraClave1;
    }

    public String getPalabraClave2() {
        return palabraClave2;
    }

    public void setPalabraClave2(String palabraClave2) {
        this.palabraClave2 = palabraClave2;
    }

    public String getPalabraClave3() {
        return palabraClave3;
    }

    public void setPalabraClave3(String palabraClave3) {
        this.palabraClave3 = palabraClave3;
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
        hash += (congreso != null ? congreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Congresos)) {
            return false;
        }
        Congresos other = (Congresos) object;
        if ((this.congreso == null && other.congreso != null) || (this.congreso != null && !this.congreso.equals(other.congreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Congresos[ congreso=" + congreso + " ]";
    }
    
}
