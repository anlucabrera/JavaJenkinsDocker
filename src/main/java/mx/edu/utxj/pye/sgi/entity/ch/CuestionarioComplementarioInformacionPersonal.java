/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.stream.Stream;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
 * @author UTXJ
 */
@Entity
@Table(name = "cuestionario_complementario_informacion_personal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findAll", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByEvaluacion", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.cuestionarioComplementarioInformacionPersonalPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByPersonal", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.cuestionarioComplementarioInformacionPersonalPK.personal = :personal")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR1lenguaIndigina", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r1lenguaIndigina = :r1lenguaIndigina")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR2tipoLengua", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r2tipoLengua = :r2tipoLengua")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR3Discapacidad", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r3Discapacidad = :r3Discapacidad")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR4tipoDiscapacidad", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r4tipoDiscapacidad = :r4tipoDiscapacidad")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR5tipoSangre", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r5tipoSangre = :r5tipoSangre")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR6EstadoCivil", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r6EstadoCivil = :r6EstadoCivil")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR7noHijos", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r7noHijos = :r7noHijos")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByR8TelefonoCel", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.r8TelefonoCel = :r8TelefonoCel")
    , @NamedQuery(name = "CuestionarioComplementarioInformacionPersonal.findByCompleto", query = "SELECT c FROM CuestionarioComplementarioInformacionPersonal c WHERE c.completo = :completo")})
public class CuestionarioComplementarioInformacionPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CuestionarioComplementarioInformacionPersonalPK cuestionarioComplementarioInformacionPersonalPK;
    @Size(max = 5)
    @Column(name = "r1_lenguaIndigina")
    private String r1lenguaIndigina;
    @Column(name = "r2_tipoLengua")
    private Short r2tipoLengua;
    @Size(max = 5)
    @Column(name = "r3_Discapacidad")
    private String r3Discapacidad;
    @Column(name = "r4_tipoDiscapacidad")
    private Short r4tipoDiscapacidad;
    @Column(name = "r5_tipoSangre")
    private Short r5tipoSangre;
    @Column(name = "r6_estado_civil")
    private String r6EstadoCivil;
    @Size(max = 10)
    @Column(name = "r7_noHijos")
    private String r7noHijos;
    @Size(max = 25)
    @Column(name = "r8_TelefonoCel")
    private String r8TelefonoCel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completo")
    private long completo;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones evaluaciones;
    @JoinColumn(name = "personal", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personal personal1;

    public CuestionarioComplementarioInformacionPersonal() {
    }

    public CuestionarioComplementarioInformacionPersonal(CuestionarioComplementarioInformacionPersonalPK cuestionarioComplementarioInformacionPersonalPK) {
        this.cuestionarioComplementarioInformacionPersonalPK = cuestionarioComplementarioInformacionPersonalPK;
    }

    public CuestionarioComplementarioInformacionPersonal(CuestionarioComplementarioInformacionPersonalPK cuestionarioComplementarioInformacionPersonalPK, long completo) {
        this.cuestionarioComplementarioInformacionPersonalPK = cuestionarioComplementarioInformacionPersonalPK;
        this.completo = completo;
    }

    public CuestionarioComplementarioInformacionPersonal(int evaluacion, int personal) {
        this.cuestionarioComplementarioInformacionPersonalPK = new CuestionarioComplementarioInformacionPersonalPK(evaluacion, personal);
    }

    public CuestionarioComplementarioInformacionPersonalPK getCuestionarioComplementarioInformacionPersonalPK() {
        return cuestionarioComplementarioInformacionPersonalPK;
    }

    public void setCuestionarioComplementarioInformacionPersonalPK(CuestionarioComplementarioInformacionPersonalPK cuestionarioComplementarioInformacionPersonalPK) {
        this.cuestionarioComplementarioInformacionPersonalPK = cuestionarioComplementarioInformacionPersonalPK;
    }

    public String getR1lenguaIndigina() {
        return r1lenguaIndigina;
    }

    public void setR1lenguaIndigina(String r1lenguaIndigina) {
        this.r1lenguaIndigina = r1lenguaIndigina;
    }

    public Short getR2tipoLengua() {
        return r2tipoLengua;
    }

    public void setR2tipoLengua(Short r2tipoLengua) {
        this.r2tipoLengua = r2tipoLengua;
    }

    public String getR3Discapacidad() {
        return r3Discapacidad;
    }

    public void setR3Discapacidad(String r3Discapacidad) {
        this.r3Discapacidad = r3Discapacidad;
    }

    public Short getR4tipoDiscapacidad() {
        return r4tipoDiscapacidad;
    }

    public void setR4tipoDiscapacidad(Short r4tipoDiscapacidad) {
        this.r4tipoDiscapacidad = r4tipoDiscapacidad;
    }

    public Short getR5tipoSangre() {
        return r5tipoSangre;
    }

    public void setR5tipoSangre(Short r5tipoSangre) {
        this.r5tipoSangre = r5tipoSangre;
    }

    public String getR6EstadoCivil() {
        return r6EstadoCivil;
    }

    public void setR6EstadoCivil(String r6EstadoCivil) {
        this.r6EstadoCivil = r6EstadoCivil;
    }

    public String getR7noHijos() {
        return r7noHijos;
    }

    public void setR7noHijos(String r7noHijos) {
        this.r7noHijos = r7noHijos;
    }

    public String getR8TelefonoCel() {
        return r8TelefonoCel;
    }

    public void setR8TelefonoCel(String r8TelefonoCel) {
        this.r8TelefonoCel = r8TelefonoCel;
    }

    public long getCompleto() {
        return completo;
    }

    public void setCompleto(long completo) {
        this.completo = completo;
    }

    public Evaluaciones getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public Personal getPersonal1() {
        return personal1;
    }

    public void setPersonal1(Personal personal1) {
        this.personal1 = personal1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuestionarioComplementarioInformacionPersonalPK != null ? cuestionarioComplementarioInformacionPersonalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuestionarioComplementarioInformacionPersonal)) {
            return false;
        }
        CuestionarioComplementarioInformacionPersonal other = (CuestionarioComplementarioInformacionPersonal) object;
        if ((this.cuestionarioComplementarioInformacionPersonalPK == null && other.cuestionarioComplementarioInformacionPersonalPK != null) || (this.cuestionarioComplementarioInformacionPersonalPK != null && !this.cuestionarioComplementarioInformacionPersonalPK.equals(other.cuestionarioComplementarioInformacionPersonalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonal[ cuestionarioComplementarioInformacionPersonalPK=" + cuestionarioComplementarioInformacionPersonalPK + " ]";
    }
    
}
