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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "servicios_tecnologicos_participantes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosTecnologicosParticipantes.findAll", query = "SELECT s FROM ServiciosTecnologicosParticipantes s")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByRegistro", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.registro = :registro")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByNombre", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.nombre = :nombre")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findBySexo", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.sexo = :sexo")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByEdad", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.edad = :edad")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByLenguaIndigena", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.lenguaIndigena = :lenguaIndigena")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByDiscapacidad", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.discapacidad = :discapacidad")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByGeneracion", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.generacion = :generacion")
    , @NamedQuery(name = "ServiciosTecnologicosParticipantes.findByProgramaEducativo", query = "SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.programaEducativo = :programaEducativo")})
public class ServiciosTecnologicosParticipantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "edad")
    private short edad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "lenguaIndigena")
    private String lenguaIndigena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "discapacidad")
    private String discapacidad;
    @Column(name = "generacion")
    private Short generacion;
    @Column(name = "programa_educativo")
    private Short programaEducativo;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "servicio_tecnologico", referencedColumnName = "servicio")
    @ManyToOne(optional = false)
    private ServiciosTecnologicosAnioMes servicioTecnologico;
    @JoinColumns({
        @JoinColumn(name = "estado", referencedColumnName = "claveEstado")
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio")})
    @ManyToOne(optional = false)
    private Municipio municipio;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne
    private OrganismosVinculados empresa;

    public ServiciosTecnologicosParticipantes() {
    }

    public ServiciosTecnologicosParticipantes(Integer registro) {
        this.registro = registro;
    }

    public ServiciosTecnologicosParticipantes(Integer registro, String nombre, String sexo, short edad, String lenguaIndigena, String discapacidad) {
        this.registro = registro;
        this.nombre = nombre;
        this.sexo = sexo;
        this.edad = edad;
        this.lenguaIndigena = lenguaIndigena;
        this.discapacidad = discapacidad;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public short getEdad() {
        return edad;
    }

    public void setEdad(short edad) {
        this.edad = edad;
    }

    public String getLenguaIndigena() {
        return lenguaIndigena;
    }

    public void setLenguaIndigena(String lenguaIndigena) {
        this.lenguaIndigena = lenguaIndigena;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public Short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Short generacion) {
        this.generacion = generacion;
    }

    public Short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(Short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public ServiciosTecnologicosAnioMes getServicioTecnologico() {
        return servicioTecnologico;
    }

    public void setServicioTecnologico(ServiciosTecnologicosAnioMes servicioTecnologico) {
        this.servicioTecnologico = servicioTecnologico;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public OrganismosVinculados getEmpresa() {
        return empresa;
    }

    public void setEmpresa(OrganismosVinculados empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosTecnologicosParticipantes)) {
            return false;
        }
        ServiciosTecnologicosParticipantes other = (ServiciosTecnologicosParticipantes) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes[ registro=" + registro + " ]";
    }
    
}
