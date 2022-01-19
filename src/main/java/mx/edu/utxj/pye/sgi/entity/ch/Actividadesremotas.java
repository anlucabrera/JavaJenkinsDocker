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
import javax.persistence.Lob;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "actividadesremotas", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividadesremotas.findAll", query = "SELECT a FROM Actividadesremotas a")
    , @NamedQuery(name = "Actividadesremotas.findByIdActividadesRemotos", query = "SELECT a FROM Actividadesremotas a WHERE a.idActividadesRemotos = :idActividadesRemotos")
    , @NamedQuery(name = "Actividadesremotas.findByFecha", query = "SELECT a FROM Actividadesremotas a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "Actividadesremotas.findByValidado", query = "SELECT a FROM Actividadesremotas a WHERE a.validado = :validado")})
public class Actividadesremotas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idActividadesRemotos")
    private Integer idActividadesRemotos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "actividades")
    private String actividades;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Actividadesremotas() {
    }

    public Actividadesremotas(Integer idActividadesRemotos) {
        this.idActividadesRemotos = idActividadesRemotos;
    }

    public Actividadesremotas(Integer idActividadesRemotos, Date fecha, String actividades, boolean validado) {
        this.idActividadesRemotos = idActividadesRemotos;
        this.fecha = fecha;
        this.actividades = actividades;
        this.validado = validado;
    }

    public Integer getIdActividadesRemotos() {
        return idActividadesRemotos;
    }

    public void setIdActividadesRemotos(Integer idActividadesRemotos) {
        this.idActividadesRemotos = idActividadesRemotos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public boolean getValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
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
        hash += (idActividadesRemotos != null ? idActividadesRemotos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividadesremotas)) {
            return false;
        }
        Actividadesremotas other = (Actividadesremotas) object;
        if ((this.idActividadesRemotos == null && other.idActividadesRemotos != null) || (this.idActividadesRemotos != null && !this.idActividadesRemotos.equals(other.idActividadesRemotos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Actividadesremotas[ idActividadesRemotos=" + idActividadesRemotos + " ]";
    }
    
}
