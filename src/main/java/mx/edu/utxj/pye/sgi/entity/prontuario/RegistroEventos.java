/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author UTXJ
 */
@Entity
@Table(name = "registro_eventos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroEventos.findAll", query = "SELECT r FROM RegistroEventos r")
    , @NamedQuery(name = "RegistroEventos.findByNumEvento", query = "SELECT r FROM RegistroEventos r WHERE r.numEvento = :numEvento")
    , @NamedQuery(name = "RegistroEventos.findByFecha", query = "SELECT r FROM RegistroEventos r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "RegistroEventos.findByEvento", query = "SELECT r FROM RegistroEventos r WHERE r.evento = :evento")
    , @NamedQuery(name = "RegistroEventos.findByTipoActividad", query = "SELECT r FROM RegistroEventos r WHERE r.tipoActividad = :tipoActividad")
    , @NamedQuery(name = "RegistroEventos.findByTipoEvento", query = "SELECT r FROM RegistroEventos r WHERE r.tipoEvento = :tipoEvento")
    , @NamedQuery(name = "RegistroEventos.findByOrgPat", query = "SELECT r FROM RegistroEventos r WHERE r.orgPat = :orgPat")
    , @NamedQuery(name = "RegistroEventos.findByObjetivo", query = "SELECT r FROM RegistroEventos r WHERE r.objetivo = :objetivo")
    , @NamedQuery(name = "RegistroEventos.findByLugar", query = "SELECT r FROM RegistroEventos r WHERE r.lugar = :lugar")
    , @NamedQuery(name = "RegistroEventos.findByDuracion", query = "SELECT r FROM RegistroEventos r WHERE r.duracion = :duracion")
    , @NamedQuery(name = "RegistroEventos.findByNomfacEmprep", query = "SELECT r FROM RegistroEventos r WHERE r.nomfacEmprep = :nomfacEmprep")
    , @NamedQuery(name = "RegistroEventos.findByPartDoc", query = "SELECT r FROM RegistroEventos r WHERE r.partDoc = :partDoc")
    , @NamedQuery(name = "RegistroEventos.findByPartTrab", query = "SELECT r FROM RegistroEventos r WHERE r.partTrab = :partTrab")
    , @NamedQuery(name = "RegistroEventos.findByPartEst", query = "SELECT r FROM RegistroEventos r WHERE r.partEst = :partEst")
    , @NamedQuery(name = "RegistroEventos.findByHombres", query = "SELECT r FROM RegistroEventos r WHERE r.hombres = :hombres")
    , @NamedQuery(name = "RegistroEventos.findByMujeres", query = "SELECT r FROM RegistroEventos r WHERE r.mujeres = :mujeres")
    , @NamedQuery(name = "RegistroEventos.findByMenigu18", query = "SELECT r FROM RegistroEventos r WHERE r.menigu18 = :menigu18")
    , @NamedQuery(name = "RegistroEventos.findByA", query = "SELECT r FROM RegistroEventos r WHERE r.a = :a")
    , @NamedQuery(name = "RegistroEventos.findByA1", query = "SELECT r FROM RegistroEventos r WHERE r.a1 = :a1")
    , @NamedQuery(name = "RegistroEventos.findByA2", query = "SELECT r FROM RegistroEventos r WHERE r.a2 = :a2")
    , @NamedQuery(name = "RegistroEventos.findByMas50", query = "SELECT r FROM RegistroEventos r WHERE r.mas50 = :mas50")
    , @NamedQuery(name = "RegistroEventos.findByLengInd", query = "SELECT r FROM RegistroEventos r WHERE r.lengInd = :lengInd")
    , @NamedQuery(name = "RegistroEventos.findByDiscapacidad", query = "SELECT r FROM RegistroEventos r WHERE r.discapacidad = :discapacidad")
    , @NamedQuery(name = "RegistroEventos.findByEqproyParticipantes", query = "SELECT r FROM RegistroEventos r WHERE r.eqproyParticipantes = :eqproyParticipantes")
    , @NamedQuery(name = "RegistroEventos.findByEqGanadores", query = "SELECT r FROM RegistroEventos r WHERE r.eqGanadores = :eqGanadores")
    , @NamedQuery(name = "RegistroEventos.findByNotas", query = "SELECT r FROM RegistroEventos r WHERE r.notas = :notas")})
public class RegistroEventos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_evento")
    private Integer numEvento;
    @Size(max = 255)
    @Column(name = "fecha")
    private String fecha;
    @Size(max = 255)
    @Column(name = "evento")
    private String evento;
    @Size(max = 50)
    @Column(name = "tipo_actividad")
    private String tipoActividad;
    @Size(max = 50)
    @Column(name = "tipo_evento")
    private String tipoEvento;
    @Size(max = 25)
    @Column(name = "org_pat")
    private String orgPat;
    @Size(max = 500)
    @Column(name = "objetivo")
    private String objetivo;
    @Size(max = 255)
    @Column(name = "lugar")
    private String lugar;
    @Size(max = 50)
    @Column(name = "duracion")
    private String duracion;
    @Size(max = 500)
    @Column(name = "nomfac_emprep")
    private String nomfacEmprep;
    @Column(name = "part_doc")
    private Integer partDoc;
    @Column(name = "part_trab")
    private Integer partTrab;
    @Column(name = "part_est")
    private Integer partEst;
    @Column(name = "hombres")
    private Integer hombres;
    @Column(name = "mujeres")
    private Integer mujeres;
    @Column(name = "menigu_18")
    private Integer menigu18;
    @Column(name = "19_30")
    private Integer a;
    @Column(name = "31_40")
    private Integer a1;
    @Column(name = "41_50")
    private Integer a2;
    @Column(name = "mas_50")
    private Integer mas50;
    @Column(name = "leng_ind")
    private Integer lengInd;
    @Column(name = "discapacidad")
    private Integer discapacidad;
    @Size(max = 255)
    @Column(name = "eqproy_participantes")
    private String eqproyParticipantes;
    @Size(max = 250)
    @Column(name = "eq_ganadores")
    private String eqGanadores;
    @Size(max = 255)
    @Column(name = "notas")
    private String notas;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclo;

    public RegistroEventos() {
    }

    public RegistroEventos(Integer numEvento) {
        this.numEvento = numEvento;
    }

    public Integer getNumEvento() {
        return numEvento;
    }

    public void setNumEvento(Integer numEvento) {
        this.numEvento = numEvento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getOrgPat() {
        return orgPat;
    }

    public void setOrgPat(String orgPat) {
        this.orgPat = orgPat;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getNomfacEmprep() {
        return nomfacEmprep;
    }

    public void setNomfacEmprep(String nomfacEmprep) {
        this.nomfacEmprep = nomfacEmprep;
    }

    public Integer getPartDoc() {
        return partDoc;
    }

    public void setPartDoc(Integer partDoc) {
        this.partDoc = partDoc;
    }

    public Integer getPartTrab() {
        return partTrab;
    }

    public void setPartTrab(Integer partTrab) {
        this.partTrab = partTrab;
    }

    public Integer getPartEst() {
        return partEst;
    }

    public void setPartEst(Integer partEst) {
        this.partEst = partEst;
    }

    public Integer getHombres() {
        return hombres;
    }

    public void setHombres(Integer hombres) {
        this.hombres = hombres;
    }

    public Integer getMujeres() {
        return mujeres;
    }

    public void setMujeres(Integer mujeres) {
        this.mujeres = mujeres;
    }

    public Integer getMenigu18() {
        return menigu18;
    }

    public void setMenigu18(Integer menigu18) {
        this.menigu18 = menigu18;
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getA1() {
        return a1;
    }

    public void setA1(Integer a1) {
        this.a1 = a1;
    }

    public Integer getA2() {
        return a2;
    }

    public void setA2(Integer a2) {
        this.a2 = a2;
    }

    public Integer getMas50() {
        return mas50;
    }

    public void setMas50(Integer mas50) {
        this.mas50 = mas50;
    }

    public Integer getLengInd() {
        return lengInd;
    }

    public void setLengInd(Integer lengInd) {
        this.lengInd = lengInd;
    }

    public Integer getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Integer discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getEqproyParticipantes() {
        return eqproyParticipantes;
    }

    public void setEqproyParticipantes(String eqproyParticipantes) {
        this.eqproyParticipantes = eqproyParticipantes;
    }

    public String getEqGanadores() {
        return eqGanadores;
    }

    public void setEqGanadores(String eqGanadores) {
        this.eqGanadores = eqGanadores;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public CiclosEscolares getCiclo() {
        return ciclo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numEvento != null ? numEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistroEventos)) {
            return false;
        }
        RegistroEventos other = (RegistroEventos) object;
        if ((this.numEvento == null && other.numEvento != null) || (this.numEvento != null && !this.numEvento.equals(other.numEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.RegistroEventos[ numEvento=" + numEvento + " ]";
    }
    
}
