/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "egresados", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Egresados.findAll", query = "SELECT e FROM Egresados e")
    , @NamedQuery(name = "Egresados.findByMatricula", query = "SELECT e FROM Egresados e WHERE e.matricula = :matricula")
    , @NamedQuery(name = "Egresados.findByNombre", query = "SELECT e FROM Egresados e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Egresados.findByApellidoPaterno", query = "SELECT e FROM Egresados e WHERE e.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "Egresados.findByApellidoMaterno", query = "SELECT e FROM Egresados e WHERE e.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "Egresados.findByCurp", query = "SELECT e FROM Egresados e WHERE e.curp = :curp")
    , @NamedQuery(name = "Egresados.findByGenero", query = "SELECT e FROM Egresados e WHERE e.genero = :genero")
    , @NamedQuery(name = "Egresados.findByFechaNacimiento", query = "SELECT e FROM Egresados e WHERE e.fechaNacimiento = :fechaNacimiento")})
public class Egresados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Size(max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 150)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Size(max = 150)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Size(max = 18)
    @Column(name = "curp")
    private String curp;
    @Size(max = 2)
    @Column(name = "genero")
    private String genero;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matricula")
    private List<ExpedientesTitulacion> expedientesTitulacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matricula")
    private List<AntecedentesAcademicos> antecedentesAcademicosList;

    public Egresados() {
    }

    public Egresados(String matricula) {
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @XmlTransient
    public List<ExpedientesTitulacion> getExpedientesTitulacionList() {
        return expedientesTitulacionList;
    }

    public void setExpedientesTitulacionList(List<ExpedientesTitulacion> expedientesTitulacionList) {
        this.expedientesTitulacionList = expedientesTitulacionList;
    }

    @XmlTransient
    public List<AntecedentesAcademicos> getAntecedentesAcademicosList() {
        return antecedentesAcademicosList;
    }

    public void setAntecedentesAcademicosList(List<AntecedentesAcademicos> antecedentesAcademicosList) {
        this.antecedentesAcademicosList = antecedentesAcademicosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Egresados)) {
            return false;
        }
        Egresados other = (Egresados) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.Egresados[ matricula=" + matricula + " ]";
    }
    
}
