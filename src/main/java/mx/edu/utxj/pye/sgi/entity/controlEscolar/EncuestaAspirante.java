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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "encuesta_aspirante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaAspirante.findAll", query = "SELECT e FROM EncuestaAspirante e"),
    @NamedQuery(name = "EncuestaAspirante.findByCveAspirante", query = "SELECT e FROM EncuestaAspirante e WHERE e.cveAspirante = :cveAspirante"),
    @NamedQuery(name = "EncuestaAspirante.findByR1Lenguaindigena", query = "SELECT e FROM EncuestaAspirante e WHERE e.r1Lenguaindigena = :r1Lenguaindigena"),
    @NamedQuery(name = "EncuestaAspirante.findByR3comunidadIndigena", query = "SELECT e FROM EncuestaAspirante e WHERE e.r3comunidadIndigena = :r3comunidadIndigena"),
    @NamedQuery(name = "EncuestaAspirante.findByR4programaBienestar", query = "SELECT e FROM EncuestaAspirante e WHERE e.r4programaBienestar = :r4programaBienestar"),
    @NamedQuery(name = "EncuestaAspirante.findByR5ingresoMensual", query = "SELECT e FROM EncuestaAspirante e WHERE e.r5ingresoMensual = :r5ingresoMensual"),
    @NamedQuery(name = "EncuestaAspirante.findByR6dependesEconomicamnete", query = "SELECT e FROM EncuestaAspirante e WHERE e.r6dependesEconomicamnete = :r6dependesEconomicamnete"),
    @NamedQuery(name = "EncuestaAspirante.findByR7ingresoFamiliar", query = "SELECT e FROM EncuestaAspirante e WHERE e.r7ingresoFamiliar = :r7ingresoFamiliar"),
    @NamedQuery(name = "EncuestaAspirante.findByR8primerEstudiar", query = "SELECT e FROM EncuestaAspirante e WHERE e.r8primerEstudiar = :r8primerEstudiar"),
    @NamedQuery(name = "EncuestaAspirante.findByR9nivelMaximoEstudios", query = "SELECT e FROM EncuestaAspirante e WHERE e.r9nivelMaximoEstudios = :r9nivelMaximoEstudios"),
    @NamedQuery(name = "EncuestaAspirante.findByR10numeroDependientes", query = "SELECT e FROM EncuestaAspirante e WHERE e.r10numeroDependientes = :r10numeroDependientes"),
    @NamedQuery(name = "EncuestaAspirante.findByR11situacionEconomica", query = "SELECT e FROM EncuestaAspirante e WHERE e.r11situacionEconomica = :r11situacionEconomica"),
    @NamedQuery(name = "EncuestaAspirante.findByR12hijoPemex", query = "SELECT e FROM EncuestaAspirante e WHERE e.r12hijoPemex = :r12hijoPemex"),
    @NamedQuery(name = "EncuestaAspirante.findByR13utxjPrimeraOpcion", query = "SELECT e FROM EncuestaAspirante e WHERE e.r13utxjPrimeraOpcion = :r13utxjPrimeraOpcion"),
    @NamedQuery(name = "EncuestaAspirante.findByR14examenAdmisionOU", query = "SELECT e FROM EncuestaAspirante e WHERE e.r14examenAdmisionOU = :r14examenAdmisionOU"),
    @NamedQuery(name = "EncuestaAspirante.findByR16segundaCarrera", query = "SELECT e FROM EncuestaAspirante e WHERE e.r16segundaCarrera = :r16segundaCarrera"),
    @NamedQuery(name = "EncuestaAspirante.findByR17Alergia", query = "SELECT e FROM EncuestaAspirante e WHERE e.r17Alergia = :r17Alergia"),
    @NamedQuery(name = "EncuestaAspirante.findByR18padecesEnfermedad", query = "SELECT e FROM EncuestaAspirante e WHERE e.r18padecesEnfermedad = :r18padecesEnfermedad"),
    @NamedQuery(name = "EncuestaAspirante.findByR19tratamientoMedico", query = "SELECT e FROM EncuestaAspirante e WHERE e.r19tratamientoMedico = :r19tratamientoMedico")})
public class EncuestaAspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_aspirante")
    private Integer cveAspirante;
    @Size(max = 2)
    @Column(name = "r1_lenguaindigena")
    private String r1Lenguaindigena;
    @Size(max = 2)
    @Column(name = "r3_comunidadIndigena")
    private String r3comunidadIndigena;
    @Size(max = 2)
    @Column(name = "r4_programaBienestar")
    private String r4programaBienestar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "r5_ingresoMensual")
    private Double r5ingresoMensual;
    @Size(max = 45)
    @Column(name = "r6_dependesEconomicamnete")
    private String r6dependesEconomicamnete;
    @Column(name = "r7_ingresoFamiliar")
    private Double r7ingresoFamiliar;
    @Size(max = 2)
    @Column(name = "r8_primerEstudiar")
    private String r8primerEstudiar;
    @Size(max = 30)
    @Column(name = "r9_nivelMaximoEstudios")
    private String r9nivelMaximoEstudios;
    @Column(name = "r10_numeroDependientes")
    private Short r10numeroDependientes;
    @Size(max = 20)
    @Column(name = "r11_situacionEconomica")
    private String r11situacionEconomica;
    @Size(max = 2)
    @Column(name = "r12_hijoPemex")
    private String r12hijoPemex;
    @Size(max = 2)
    @Column(name = "r13_utxjPrimeraOpcion")
    private String r13utxjPrimeraOpcion;
    @Size(max = 2)
    @Column(name = "r14_examenAdmisionOU")
    private String r14examenAdmisionOU;
    @Size(max = 2)
    @Column(name = "r16_segundaCarrera")
    private String r16segundaCarrera;
    @Size(max = 2)
    @Column(name = "r17_alergia")
    private String r17Alergia;
    @Size(max = 2)
    @Column(name = "r18_padecesEnfermedad")
    private String r18padecesEnfermedad;
    @Size(max = 2)
    @Column(name = "r19_tratamientoMedico")
    private String r19tratamientoMedico;
    @JoinColumn(name = "cve_aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Aspirante aspirante;
    @JoinColumn(name = "r2_tipoLenguaIndigena", referencedColumnName = "id_lengua_indigena")
    @ManyToOne
    private LenguaIndigena r2tipoLenguaIndigena;
    @JoinColumn(name = "r15_medioImpacto", referencedColumnName = "id_medio_difusion")
    @ManyToOne
    private MedioDifusion r15medioImpacto;

    public EncuestaAspirante() {
    }

    public EncuestaAspirante(Integer cveAspirante) {
        this.cveAspirante = cveAspirante;
    }

    public Integer getCveAspirante() {
        return cveAspirante;
    }

    public void setCveAspirante(Integer cveAspirante) {
        this.cveAspirante = cveAspirante;
    }

    public String getR1Lenguaindigena() {
        return r1Lenguaindigena;
    }

    public void setR1Lenguaindigena(String r1Lenguaindigena) {
        this.r1Lenguaindigena = r1Lenguaindigena;
    }

    public String getR3comunidadIndigena() {
        return r3comunidadIndigena;
    }

    public void setR3comunidadIndigena(String r3comunidadIndigena) {
        this.r3comunidadIndigena = r3comunidadIndigena;
    }

    public String getR4programaBienestar() {
        return r4programaBienestar;
    }

    public void setR4programaBienestar(String r4programaBienestar) {
        this.r4programaBienestar = r4programaBienestar;
    }

    public Double getR5ingresoMensual() {
        return r5ingresoMensual;
    }

    public void setR5ingresoMensual(Double r5ingresoMensual) {
        this.r5ingresoMensual = r5ingresoMensual;
    }

    public String getR6dependesEconomicamnete() {
        return r6dependesEconomicamnete;
    }

    public void setR6dependesEconomicamnete(String r6dependesEconomicamnete) {
        this.r6dependesEconomicamnete = r6dependesEconomicamnete;
    }

    public Double getR7ingresoFamiliar() {
        return r7ingresoFamiliar;
    }

    public void setR7ingresoFamiliar(Double r7ingresoFamiliar) {
        this.r7ingresoFamiliar = r7ingresoFamiliar;
    }

    public String getR8primerEstudiar() {
        return r8primerEstudiar;
    }

    public void setR8primerEstudiar(String r8primerEstudiar) {
        this.r8primerEstudiar = r8primerEstudiar;
    }

    public String getR9nivelMaximoEstudios() {
        return r9nivelMaximoEstudios;
    }

    public void setR9nivelMaximoEstudios(String r9nivelMaximoEstudios) {
        this.r9nivelMaximoEstudios = r9nivelMaximoEstudios;
    }

    public Short getR10numeroDependientes() {
        return r10numeroDependientes;
    }

    public void setR10numeroDependientes(Short r10numeroDependientes) {
        this.r10numeroDependientes = r10numeroDependientes;
    }

    public String getR11situacionEconomica() {
        return r11situacionEconomica;
    }

    public void setR11situacionEconomica(String r11situacionEconomica) {
        this.r11situacionEconomica = r11situacionEconomica;
    }

    public String getR12hijoPemex() {
        return r12hijoPemex;
    }

    public void setR12hijoPemex(String r12hijoPemex) {
        this.r12hijoPemex = r12hijoPemex;
    }

    public String getR13utxjPrimeraOpcion() {
        return r13utxjPrimeraOpcion;
    }

    public void setR13utxjPrimeraOpcion(String r13utxjPrimeraOpcion) {
        this.r13utxjPrimeraOpcion = r13utxjPrimeraOpcion;
    }

    public String getR14examenAdmisionOU() {
        return r14examenAdmisionOU;
    }

    public void setR14examenAdmisionOU(String r14examenAdmisionOU) {
        this.r14examenAdmisionOU = r14examenAdmisionOU;
    }

    public String getR16segundaCarrera() {
        return r16segundaCarrera;
    }

    public void setR16segundaCarrera(String r16segundaCarrera) {
        this.r16segundaCarrera = r16segundaCarrera;
    }

    public String getR17Alergia() {
        return r17Alergia;
    }

    public void setR17Alergia(String r17Alergia) {
        this.r17Alergia = r17Alergia;
    }

    public String getR18padecesEnfermedad() {
        return r18padecesEnfermedad;
    }

    public void setR18padecesEnfermedad(String r18padecesEnfermedad) {
        this.r18padecesEnfermedad = r18padecesEnfermedad;
    }

    public String getR19tratamientoMedico() {
        return r19tratamientoMedico;
    }

    public void setR19tratamientoMedico(String r19tratamientoMedico) {
        this.r19tratamientoMedico = r19tratamientoMedico;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public LenguaIndigena getR2tipoLenguaIndigena() {
        return r2tipoLenguaIndigena;
    }

    public void setR2tipoLenguaIndigena(LenguaIndigena r2tipoLenguaIndigena) {
        this.r2tipoLenguaIndigena = r2tipoLenguaIndigena;
    }

    public MedioDifusion getR15medioImpacto() {
        return r15medioImpacto;
    }

    public void setR15medioImpacto(MedioDifusion r15medioImpacto) {
        this.r15medioImpacto = r15medioImpacto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveAspirante != null ? cveAspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaAspirante)) {
            return false;
        }
        EncuestaAspirante other = (EncuestaAspirante) object;
        if ((this.cveAspirante == null && other.cveAspirante != null) || (this.cveAspirante != null && !this.cveAspirante.equals(other.cveAspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante[ cveAspirante=" + cveAspirante + " ]";
    }
    
}
