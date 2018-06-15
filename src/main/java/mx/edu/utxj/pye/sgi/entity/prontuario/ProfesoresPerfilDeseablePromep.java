/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author UTXJ
 */
@Entity
@Table(name = "profesores_perfil_deseable_promep", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findAll", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByRegistro", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.registro = :registro")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByProfesor", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.profesor = :profesor")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByAreaAcademica", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.areaAcademica = :areaAcademica")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByCuerpoAcademico", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.cuerpoAcademico = :cuerpoAcademico")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByTipoApoyo", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.tipoApoyo = :tipoApoyo")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByMontoPago", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.montoPago = :montoPago")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByInicioVig", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.inicioVig = :inicioVig")
    , @NamedQuery(name = "ProfesoresPerfilDeseablePromep.findByFinVig", query = "SELECT p FROM ProfesoresPerfilDeseablePromep p WHERE p.finVig = :finVig")})
public class ProfesoresPerfilDeseablePromep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "profesor")
    private String profesor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "area_academica")
    private String areaAcademica;
    @Size(max = 50)
    @Column(name = "cuerpo_academico")
    private String cuerpoAcademico;
    @Size(max = 255)
    @Column(name = "tipo_apoyo")
    private String tipoApoyo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_pago")
    private BigDecimal montoPago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio_vig")
    @Temporal(TemporalType.DATE)
    private Date inicioVig;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin_vig")
    @Temporal(TemporalType.DATE)
    private Date finVig;

    public ProfesoresPerfilDeseablePromep() {
    }

    public ProfesoresPerfilDeseablePromep(Integer registro) {
        this.registro = registro;
    }

    public ProfesoresPerfilDeseablePromep(Integer registro, String profesor, String areaAcademica, Date inicioVig, Date finVig) {
        this.registro = registro;
        this.profesor = profesor;
        this.areaAcademica = areaAcademica;
        this.inicioVig = inicioVig;
        this.finVig = finVig;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(String areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    public String getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(String cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    public String getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(String tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public Date getInicioVig() {
        return inicioVig;
    }

    public void setInicioVig(Date inicioVig) {
        this.inicioVig = inicioVig;
    }

    public Date getFinVig() {
        return finVig;
    }

    public void setFinVig(Date finVig) {
        this.finVig = finVig;
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
        if (!(object instanceof ProfesoresPerfilDeseablePromep)) {
            return false;
        }
        ProfesoresPerfilDeseablePromep other = (ProfesoresPerfilDeseablePromep) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProfesoresPerfilDeseablePromep[ registro=" + registro + " ]";
    }
    
}
