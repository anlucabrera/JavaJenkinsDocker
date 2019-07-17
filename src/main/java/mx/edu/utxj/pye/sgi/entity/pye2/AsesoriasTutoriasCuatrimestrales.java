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
@Table(name = "asesorias_tutorias_cuatrimestrales", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findAll", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a")
    , @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findByRegistro", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a WHERE a.registro = :registro")
    , @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findByPeriodoEscolar", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a WHERE a.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findByTipo", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findByHombres", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a WHERE a.hombres = :hombres")
    , @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findByMujeres", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a WHERE a.mujeres = :mujeres")
    , @NamedQuery(name = "AsesoriasTutoriasCuatrimestrales.findByArea", query = "SELECT a FROM AsesoriasTutoriasCuatrimestrales a WHERE a.area = :area")})
public class AsesoriasTutoriasCuatrimestrales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private short hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private short mujeres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @JoinColumn(name = "dato_asesoria_tutoria", referencedColumnName = "dato_asesoria_tutoria")
    @ManyToOne(optional = false)
    private DatosAsesoriasTutorias datoAsesoriaTutoria;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public AsesoriasTutoriasCuatrimestrales() {
    }

    public AsesoriasTutoriasCuatrimestrales(Integer registro) {
        this.registro = registro;
    }

    public AsesoriasTutoriasCuatrimestrales(Integer registro, int periodoEscolar, String tipo, short hombres, short mujeres, short area) {
        this.registro = registro;
        this.periodoEscolar = periodoEscolar;
        this.tipo = tipo;
        this.hombres = hombres;
        this.mujeres = mujeres;
        this.area = area;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public short getHombres() {
        return hombres;
    }

    public void setHombres(short hombres) {
        this.hombres = hombres;
    }

    public short getMujeres() {
        return mujeres;
    }

    public void setMujeres(short mujeres) {
        this.mujeres = mujeres;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public DatosAsesoriasTutorias getDatoAsesoriaTutoria() {
        return datoAsesoriaTutoria;
    }

    public void setDatoAsesoriaTutoria(DatosAsesoriasTutorias datoAsesoriaTutoria) {
        this.datoAsesoriaTutoria = datoAsesoriaTutoria;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
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
        if (!(object instanceof AsesoriasTutoriasCuatrimestrales)) {
            return false;
        }
        AsesoriasTutoriasCuatrimestrales other = (AsesoriasTutoriasCuatrimestrales) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCuatrimestrales[ registro=" + registro + " ]";
    }
    
}
