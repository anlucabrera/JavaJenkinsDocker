/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ingresos_serv_est_tec", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngresosServEstTec.findAll", query = "SELECT i FROM IngresosServEstTec i")
    , @NamedQuery(name = "IngresosServEstTec.findByRegistro", query = "SELECT i FROM IngresosServEstTec i WHERE i.registro = :registro")
    , @NamedQuery(name = "IngresosServEstTec.findByRecursoCaptadoPorServ", query = "SELECT i FROM IngresosServEstTec i WHERE i.recursoCaptadoPorServ = :recursoCaptadoPorServ")
    , @NamedQuery(name = "IngresosServEstTec.findByNoServPro", query = "SELECT i FROM IngresosServEstTec i WHERE i.noServPro = :noServPro")
    , @NamedQuery(name = "IngresosServEstTec.findByServicio", query = "SELECT i FROM IngresosServEstTec i WHERE i.servicio = :servicio")})
public class IngresosServEstTec implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "recurso_captado_por_serv")
    private BigDecimal recursoCaptadoPorServ;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_serv_pro")
    private int noServPro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "servicio")
    private String servicio;
    @JoinColumn(name = "ciclo_escolar", referencedColumnName = "ciclo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares cicloEscolar;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodo;

    public IngresosServEstTec() {
    }

    public IngresosServEstTec(Integer registro) {
        this.registro = registro;
    }

    public IngresosServEstTec(Integer registro, BigDecimal recursoCaptadoPorServ, int noServPro, String servicio) {
        this.registro = registro;
        this.recursoCaptadoPorServ = recursoCaptadoPorServ;
        this.noServPro = noServPro;
        this.servicio = servicio;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public BigDecimal getRecursoCaptadoPorServ() {
        return recursoCaptadoPorServ;
    }

    public void setRecursoCaptadoPorServ(BigDecimal recursoCaptadoPorServ) {
        this.recursoCaptadoPorServ = recursoCaptadoPorServ;
    }

    public int getNoServPro() {
        return noServPro;
    }

    public void setNoServPro(int noServPro) {
        this.noServPro = noServPro;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public CiclosEscolares getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(CiclosEscolares cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public PeriodosEscolares getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
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
        if (!(object instanceof IngresosServEstTec)) {
            return false;
        }
        IngresosServEstTec other = (IngresosServEstTec) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.IngresosServEstTec[ registro=" + registro + " ]";
    }
    
}
