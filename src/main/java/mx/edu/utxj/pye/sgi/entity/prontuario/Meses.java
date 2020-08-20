/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "meses", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meses.findAll", query = "SELECT m FROM Meses m")
    , @NamedQuery(name = "Meses.findByNumero", query = "SELECT m FROM Meses m WHERE m.numero = :numero")
    , @NamedQuery(name = "Meses.findByMes", query = "SELECT m FROM Meses m WHERE m.mes = :mes")
    , @NamedQuery(name = "Meses.findByAbreviacion", query = "SELECT m FROM Meses m WHERE m.abreviacion = :abreviacion")})
public class Meses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private Short numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mes")
    private String mes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "abreviacion")
    private String abreviacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesFin", fetch = FetchType.LAZY)
    private List<PeriodosEscolares> periodosEscolaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mesInicio", fetch = FetchType.LAZY)
    private List<PeriodosEscolares> periodosEscolaresList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meses", fetch = FetchType.LAZY)
    private List<ServiciosTecnologicos> serviciosTecnologicosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meses", fetch = FetchType.LAZY)
    private List<ServiciosEnfermeria> serviciosEnfermeriaList;

    public Meses() {
    }

    public Meses(Short numero) {
        this.numero = numero;
    }

    public Meses(Short numero, String mes, String abreviacion) {
        this.numero = numero;
        this.mes = mes;
        this.abreviacion = abreviacion;
    }

    public Short getNumero() {
        return numero;
    }

    public void setNumero(Short numero) {
        this.numero = numero;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    @XmlTransient
    public List<PeriodosEscolares> getPeriodosEscolaresList() {
        return periodosEscolaresList;
    }

    public void setPeriodosEscolaresList(List<PeriodosEscolares> periodosEscolaresList) {
        this.periodosEscolaresList = periodosEscolaresList;
    }

    @XmlTransient
    public List<PeriodosEscolares> getPeriodosEscolaresList1() {
        return periodosEscolaresList1;
    }

    public void setPeriodosEscolaresList1(List<PeriodosEscolares> periodosEscolaresList1) {
        this.periodosEscolaresList1 = periodosEscolaresList1;
    }

    @XmlTransient
    public List<ServiciosTecnologicos> getServiciosTecnologicosList() {
        return serviciosTecnologicosList;
    }

    public void setServiciosTecnologicosList(List<ServiciosTecnologicos> serviciosTecnologicosList) {
        this.serviciosTecnologicosList = serviciosTecnologicosList;
    }

    @XmlTransient
    public List<ServiciosEnfermeria> getServiciosEnfermeriaList() {
        return serviciosEnfermeriaList;
    }

    public void setServiciosEnfermeriaList(List<ServiciosEnfermeria> serviciosEnfermeriaList) {
        this.serviciosEnfermeriaList = serviciosEnfermeriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meses)) {
            return false;
        }
        Meses other = (Meses) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Meses[ numero=" + numero + " ]";
    }
    
}
