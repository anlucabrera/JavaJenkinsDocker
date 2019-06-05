/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
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
@Table(name = "listaperiodosescolares", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaperiodosescolares.findAll", query = "SELECT l FROM Listaperiodosescolares l")
    , @NamedQuery(name = "Listaperiodosescolares.findByCiclo", query = "SELECT l FROM Listaperiodosescolares l WHERE l.ciclo = :ciclo")
    , @NamedQuery(name = "Listaperiodosescolares.findByPeriodo", query = "SELECT l FROM Listaperiodosescolares l WHERE l.periodo = :periodo")
    , @NamedQuery(name = "Listaperiodosescolares.findByAnio", query = "SELECT l FROM Listaperiodosescolares l WHERE l.anio = :anio")
    , @NamedQuery(name = "Listaperiodosescolares.findByTipo", query = "SELECT l FROM Listaperiodosescolares l WHERE l.tipo = :tipo")
    , @NamedQuery(name = "Listaperiodosescolares.findByMesInicio", query = "SELECT l FROM Listaperiodosescolares l WHERE l.mesInicio = :mesInicio")
    , @NamedQuery(name = "Listaperiodosescolares.findByMesFin", query = "SELECT l FROM Listaperiodosescolares l WHERE l.mesFin = :mesFin")
    , @NamedQuery(name = "Listaperiodosescolares.findByInicio", query = "SELECT l FROM Listaperiodosescolares l WHERE l.inicio = :inicio")
    , @NamedQuery(name = "Listaperiodosescolares.findByFin", query = "SELECT l FROM Listaperiodosescolares l WHERE l.fin = :fin")})
public class Listaperiodosescolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    @Id
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private int anio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 76)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "mesInicio")
    private String mesInicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "mesFin")
    private String mesFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.DATE)
    private Date inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    @Temporal(TemporalType.DATE)
    private Date fin;

    public Listaperiodosescolares() {
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getMesFin() {
        return mesFin;
    }

    public void setMesFin(String mesFin) {
        this.mesFin = mesFin;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }
    
}
