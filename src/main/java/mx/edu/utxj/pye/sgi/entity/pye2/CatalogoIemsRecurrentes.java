/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "catalogo_iems_recurrentes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoIemsRecurrentes.findAll", query = "SELECT c FROM CatalogoIemsRecurrentes c")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByIems", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.iems = :iems")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByEstado", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.estado = :estado")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByMunicipio", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.municipio = :municipio")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByLocalidad", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.localidad = :localidad")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByConcatenado", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.concatenado = :concatenado")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByClave", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.clave = :clave")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByTurno", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.turno = :turno")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByNombre", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CatalogoIemsRecurrentes.findByIemsConcatenado", query = "SELECT c FROM CatalogoIemsRecurrentes c WHERE c.iemsConcatenado = :iemsConcatenado")})
public class CatalogoIemsRecurrentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "iems")
    private int iems;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "localidad")
    private int localidad;
    @Size(max = 33)
    @Column(name = "concatenado")
    private String concatenado;
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
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 377)
    @Column(name = "iems_concatenado")
    private String iemsConcatenado;

    public CatalogoIemsRecurrentes() {
    }

    public int getIems() {
        return iems;
    }

    public void setIems(int iems) {
        this.iems = iems;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    public int getLocalidad() {
        return localidad;
    }

    public void setLocalidad(int localidad) {
        this.localidad = localidad;
    }

    public String getConcatenado() {
        return concatenado;
    }

    public void setConcatenado(String concatenado) {
        this.concatenado = concatenado;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIemsConcatenado() {
        return iemsConcatenado;
    }

    public void setIemsConcatenado(String iemsConcatenado) {
        this.iemsConcatenado = iemsConcatenado;
    }
    
}
