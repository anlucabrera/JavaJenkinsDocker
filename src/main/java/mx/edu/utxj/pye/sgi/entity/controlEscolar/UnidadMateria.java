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
@Table(name = "unidad_materia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateria.findAll", query = "SELECT u FROM UnidadMateria u")
    , @NamedQuery(name = "UnidadMateria.findByIdUnidadMateria", query = "SELECT u FROM UnidadMateria u WHERE u.idUnidadMateria = :idUnidadMateria")
    , @NamedQuery(name = "UnidadMateria.findByNombre", query = "SELECT u FROM UnidadMateria u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "UnidadMateria.findByObjetivo", query = "SELECT u FROM UnidadMateria u WHERE u.objetivo = :objetivo")
    , @NamedQuery(name = "UnidadMateria.findByNoUnidad", query = "SELECT u FROM UnidadMateria u WHERE u.noUnidad = :noUnidad")
    , @NamedQuery(name = "UnidadMateria.findByHorasTeoricas", query = "SELECT u FROM UnidadMateria u WHERE u.horasTeoricas = :horasTeoricas")
    , @NamedQuery(name = "UnidadMateria.findByHorasPracticas", query = "SELECT u FROM UnidadMateria u WHERE u.horasPracticas = :horasPracticas")
    , @NamedQuery(name = "UnidadMateria.findByIntegradora", query = "SELECT u FROM UnidadMateria u WHERE u.integradora = :integradora")})
public class UnidadMateria implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 500)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_unidad")
    private int noUnidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horas_teoricas")
    private int horasTeoricas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horas_practicas")
    private int horasPracticas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "integradora")
    private boolean integradora;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUnidadMateria")
    private List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_unidad_materia")
    private Integer idUnidadMateria;
    @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")
    @ManyToOne(optional = false)
    private Materia idMateria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUnidadMateria")
    private List<UnidadMateriaConfiguracion> unidadMateriaConfiguracionList;

    public UnidadMateria() {
    }

    public UnidadMateria(Integer idUnidadMateria) {
        this.idUnidadMateria = idUnidadMateria;
    }

    public UnidadMateria(Integer idUnidadMateria, String nombre, String objetivo, int noUnidad, int horasTeoricas, int horasPracticas, boolean integradora) {
        this.idUnidadMateria = idUnidadMateria;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.noUnidad = noUnidad;
        this.horasTeoricas = horasTeoricas;
        this.horasPracticas = horasPracticas;
        this.integradora = integradora;
    }

    public Integer getIdUnidadMateria() {
        return idUnidadMateria;
    }

    public void setIdUnidadMateria(Integer idUnidadMateria) {
        this.idUnidadMateria = idUnidadMateria;
    }


    public int getNoUnidad() {
        return noUnidad;
    }

    public void setNoUnidad(int noUnidad) {
        this.noUnidad = noUnidad;
    }

    public int getHorasTeoricas() {
        return horasTeoricas;
    }

    public void setHorasTeoricas(int horasTeoricas) {
        this.horasTeoricas = horasTeoricas;
    }

    public int getHorasPracticas() {
        return horasPracticas;
    }

    public void setHorasPracticas(int horasPracticas) {
        this.horasPracticas = horasPracticas;
    }


    public Materia getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Materia idMateria) {
        this.idMateria = idMateria;
    }

    @XmlTransient
    public List<UnidadMateriaConfiguracion> getUnidadMateriaConfiguracionList() {
        return unidadMateriaConfiguracionList;
    }

    public void setUnidadMateriaConfiguracionList(List<UnidadMateriaConfiguracion> unidadMateriaConfiguracionList) {
        this.unidadMateriaConfiguracionList = unidadMateriaConfiguracionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUnidadMateria != null ? idUnidadMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateria)) {
            return false;
        }
        UnidadMateria other = (UnidadMateria) object;
        if ((this.idUnidadMateria == null && other.idUnidadMateria != null) || (this.idUnidadMateria != null && !this.idUnidadMateria.equals(other.idUnidadMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria[ idUnidadMateria=" + idUnidadMateria + " ]";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public boolean getIntegradora() {
        return integradora;
    }

    public void setIntegradora(boolean integradora) {
        this.integradora = integradora;
    }

    @XmlTransient
    public List<PermisosCapturaExtemporaneaGrupal> getPermisosCapturaExtemporaneaGrupalList() {
        return permisosCapturaExtemporaneaGrupalList;
    }

    public void setPermisosCapturaExtemporaneaGrupalList(List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList) {
        this.permisosCapturaExtemporaneaGrupalList = permisosCapturaExtemporaneaGrupalList;
    }
    
}
