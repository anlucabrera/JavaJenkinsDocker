/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "datos_academicos", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosAcademicos.findAll", query = "SELECT d FROM DatosAcademicos d")
    , @NamedQuery(name = "DatosAcademicos.findByAspirante", query = "SELECT d FROM DatosAcademicos d WHERE d.aspirante = :aspirante")
    , @NamedQuery(name = "DatosAcademicos.findByPrimeraOpcion", query = "SELECT d FROM DatosAcademicos d WHERE d.primeraOpcion = :primeraOpcion")
    , @NamedQuery(name = "DatosAcademicos.findBySegundaOpcion", query = "SELECT d FROM DatosAcademicos d WHERE d.segundaOpcion = :segundaOpcion")
    , @NamedQuery(name = "DatosAcademicos.findByPromedio", query = "SELECT d FROM DatosAcademicos d WHERE d.promedio = :promedio")
    , @NamedQuery(name = "DatosAcademicos.findByInstitucionAcademica", query = "SELECT d FROM DatosAcademicos d WHERE d.institucionAcademica = :institucionAcademica")})
public class DatosAcademicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private Integer aspirante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "primera_opcion")
    private short primeraOpcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "segunda_opcion")
    private short segundaOpcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio")
    private double promedio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "institucion_academica")
    private int institucionAcademica;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Aspirante aspirante1;
    @JoinColumn(name = "especialidad_iems", referencedColumnName = "id_especialidad_centro")
    @ManyToOne(optional = false)
    private EspecialidadCentro especialidadIems;
    @JoinColumn(name = "sistema_primera_opcion", referencedColumnName = "id_sistema")
    @ManyToOne(optional = false)
    private Sistema sistemaPrimeraOpcion;
    @JoinColumn(name = "sistema_segunda_opcion", referencedColumnName = "id_sistema")
    @ManyToOne(optional = false)
    private Sistema sistemaSegundaOpcion;

    public DatosAcademicos() {
    }

    public DatosAcademicos(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public DatosAcademicos(Integer aspirante, short primeraOpcion, short segundaOpcion, double promedio, int institucionAcademica) {
        this.aspirante = aspirante;
        this.primeraOpcion = primeraOpcion;
        this.segundaOpcion = segundaOpcion;
        this.promedio = promedio;
        this.institucionAcademica = institucionAcademica;
    }

    public Integer getAspirante() {
        return aspirante;
    }

    public void setAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public short getPrimeraOpcion() {
        return primeraOpcion;
    }

    public void setPrimeraOpcion(short primeraOpcion) {
        this.primeraOpcion = primeraOpcion;
    }

    public short getSegundaOpcion() {
        return segundaOpcion;
    }

    public void setSegundaOpcion(short segundaOpcion) {
        this.segundaOpcion = segundaOpcion;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getInstitucionAcademica() {
        return institucionAcademica;
    }

    public void setInstitucionAcademica(int institucionAcademica) {
        this.institucionAcademica = institucionAcademica;
    }

    public Aspirante getAspirante1() {
        return aspirante1;
    }

    public void setAspirante1(Aspirante aspirante1) {
        this.aspirante1 = aspirante1;
    }

    public EspecialidadCentro getEspecialidadIems() {
        return especialidadIems;
    }

    public void setEspecialidadIems(EspecialidadCentro especialidadIems) {
        this.especialidadIems = especialidadIems;
    }

    public Sistema getSistemaPrimeraOpcion() {
        return sistemaPrimeraOpcion;
    }

    public void setSistemaPrimeraOpcion(Sistema sistemaPrimeraOpcion) {
        this.sistemaPrimeraOpcion = sistemaPrimeraOpcion;
    }

    public Sistema getSistemaSegundaOpcion() {
        return sistemaSegundaOpcion;
    }

    public void setSistemaSegundaOpcion(Sistema sistemaSegundaOpcion) {
        this.sistemaSegundaOpcion = sistemaSegundaOpcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspirante != null ? aspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosAcademicos)) {
            return false;
        }
        DatosAcademicos other = (DatosAcademicos) object;
        if ((this.aspirante == null && other.aspirante != null) || (this.aspirante != null && !this.aspirante.equals(other.aspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos[ aspirante=" + aspirante + " ]";
    }
    
}
