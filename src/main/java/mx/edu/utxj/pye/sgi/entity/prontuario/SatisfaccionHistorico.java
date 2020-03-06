/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "satisfaccion_historico", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SatisfaccionHistorico.findAll", query = "SELECT s FROM SatisfaccionHistorico s")
    , @NamedQuery(name = "SatisfaccionHistorico.findByCiclo", query = "SELECT s FROM SatisfaccionHistorico s WHERE s.satisfaccionHistoricoPK.ciclo = :ciclo")
    , @NamedQuery(name = "SatisfaccionHistorico.findByArea", query = "SELECT s FROM SatisfaccionHistorico s WHERE s.satisfaccionHistoricoPK.area = :area")
    , @NamedQuery(name = "SatisfaccionHistorico.findByApartado", query = "SELECT s FROM SatisfaccionHistorico s WHERE s.satisfaccionHistoricoPK.apartado = :apartado")
    , @NamedQuery(name = "SatisfaccionHistorico.findBySatisfaccionNivel", query = "SELECT s FROM SatisfaccionHistorico s WHERE s.satisfaccionNivel = :satisfaccionNivel")
    , @NamedQuery(name = "SatisfaccionHistorico.findBySatisfaccionPorcentaje", query = "SELECT s FROM SatisfaccionHistorico s WHERE s.satisfaccionPorcentaje = :satisfaccionPorcentaje")})
public class SatisfaccionHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SatisfaccionHistoricoPK satisfaccionHistoricoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "satisfaccion_nivel")
    private Double satisfaccionNivel;
    @Column(name = "satisfaccion_porcentaje")
    private Double satisfaccionPorcentaje;
    @JoinColumn(name = "area", referencedColumnName = "area", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AreasUniversidad areasUniversidad;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CiclosEscolares ciclosEscolares;

    public SatisfaccionHistorico() {
    }

    public SatisfaccionHistorico(SatisfaccionHistoricoPK satisfaccionHistoricoPK) {
        this.satisfaccionHistoricoPK = satisfaccionHistoricoPK;
    }

    public SatisfaccionHistorico(int ciclo, short area, double apartado) {
        this.satisfaccionHistoricoPK = new SatisfaccionHistoricoPK(ciclo, area, apartado);
    }

    public SatisfaccionHistoricoPK getSatisfaccionHistoricoPK() {
        return satisfaccionHistoricoPK;
    }

    public void setSatisfaccionHistoricoPK(SatisfaccionHistoricoPK satisfaccionHistoricoPK) {
        this.satisfaccionHistoricoPK = satisfaccionHistoricoPK;
    }

    public Double getSatisfaccionNivel() {
        return satisfaccionNivel;
    }

    public void setSatisfaccionNivel(Double satisfaccionNivel) {
        this.satisfaccionNivel = satisfaccionNivel;
    }

    public Double getSatisfaccionPorcentaje() {
        return satisfaccionPorcentaje;
    }

    public void setSatisfaccionPorcentaje(Double satisfaccionPorcentaje) {
        this.satisfaccionPorcentaje = satisfaccionPorcentaje;
    }

    public AreasUniversidad getAreasUniversidad() {
        return areasUniversidad;
    }

    public void setAreasUniversidad(AreasUniversidad areasUniversidad) {
        this.areasUniversidad = areasUniversidad;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (satisfaccionHistoricoPK != null ? satisfaccionHistoricoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SatisfaccionHistorico)) {
            return false;
        }
        SatisfaccionHistorico other = (SatisfaccionHistorico) object;
        if ((this.satisfaccionHistoricoPK == null && other.satisfaccionHistoricoPK != null) || (this.satisfaccionHistoricoPK != null && !this.satisfaccionHistoricoPK.equals(other.satisfaccionHistoricoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SatisfaccionHistorico{" +
                "satisfaccionHistoricoPK=" + satisfaccionHistoricoPK +
                ", satisfaccionNivel=" + satisfaccionNivel +
                '}';
    }
}
