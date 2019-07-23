/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "listaindicadoresporcriterioporconfiguracion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findAll", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByConfiguracion", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.listaindicadoresporcriterioporconfiguracionPK.configuracion = :configuracion")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByPeriodo", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.listaindicadoresporcriterioporconfiguracionPK.periodo = :periodo")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByCargaAcademica", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.cargaAcademica = :cargaAcademica")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByNombreUnidad", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.nombreUnidad = :nombreUnidad")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByUnidad", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.unidad = :unidad")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByClaveCriterio", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.listaindicadoresporcriterioporconfiguracionPK.claveCriterio = :claveCriterio")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByCriterio", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.criterio = :criterio")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByPorcentaje", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.porcentaje = :porcentaje")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByClaveIndicador", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.listaindicadoresporcriterioporconfiguracionPK.claveIndicador = :claveIndicador")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByIndicador", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.indicador = :indicador")
    , @NamedQuery(name = "Listaindicadoresporcriterioporconfiguracion.findByPorcentajeIndicador", query = "SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.porcentajeIndicador = :porcentajeIndicador")})
public class Listaindicadoresporcriterioporconfiguracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ListaindicadoresporcriterioporconfiguracionPK listaindicadoresporcriterioporconfiguracionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cargaAcademica")
    private int cargaAcademica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombreUnidad")
    private String nombreUnidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unidad")
    private int unidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "criterio")
    private String criterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "indicador")
    private String indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentajeIndicador")
    private double porcentajeIndicador;

        public Listaindicadoresporcriterioporconfiguracion() {
    }

    public Listaindicadoresporcriterioporconfiguracion(ListaindicadoresporcriterioporconfiguracionPK listaindicadoresporcriterioporconfiguracionPK) {
        this.listaindicadoresporcriterioporconfiguracionPK = listaindicadoresporcriterioporconfiguracionPK;
    }

    public Listaindicadoresporcriterioporconfiguracion(ListaindicadoresporcriterioporconfiguracionPK listaindicadoresporcriterioporconfiguracionPK, int cargaAcademica, String nombreUnidad, int unidad, String criterio, double porcentaje, String indicador) {
        this.listaindicadoresporcriterioporconfiguracionPK = listaindicadoresporcriterioporconfiguracionPK;
        this.cargaAcademica = cargaAcademica;
        this.nombreUnidad = nombreUnidad;
        this.unidad = unidad;
        this.criterio = criterio;
        this.porcentaje = porcentaje;
        this.indicador = indicador;
    }
    
    public Listaindicadoresporcriterioporconfiguracion(int configuracion, int periodo, int claveCriterio, int claveIndicador) {
        this.listaindicadoresporcriterioporconfiguracionPK = new ListaindicadoresporcriterioporconfiguracionPK(configuracion, periodo, claveCriterio, claveIndicador);
    }

    public ListaindicadoresporcriterioporconfiguracionPK getListaindicadoresporcriterioporconfiguracionPK() {
        return listaindicadoresporcriterioporconfiguracionPK;
    }

    public void setListaindicadoresporcriterioporconfiguracionPK(ListaindicadoresporcriterioporconfiguracionPK listaindicadoresporcriterioporconfiguracionPK) {
        this.listaindicadoresporcriterioporconfiguracionPK = listaindicadoresporcriterioporconfiguracionPK;
    }
    
    public int getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(int cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public double getPorcentajeIndicador() {
        return porcentajeIndicador;
    }

    public void setPorcentajeIndicador(double porcentajeIndicador) {
        this.porcentajeIndicador = porcentajeIndicador;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.listaindicadoresporcriterioporconfiguracionPK);
        hash = 11 * hash + this.cargaAcademica;
        hash = 11 * hash + Objects.hashCode(this.nombreUnidad);
        hash = 11 * hash + this.unidad;
        hash = 11 * hash + Objects.hashCode(this.criterio);
        hash = 11 * hash + (int) (Double.doubleToLongBits(this.porcentaje) ^ (Double.doubleToLongBits(this.porcentaje) >>> 32));
        hash = 11 * hash + Objects.hashCode(this.indicador);
        hash = 11 * hash + (int) (Double.doubleToLongBits(this.porcentajeIndicador) ^ (Double.doubleToLongBits(this.porcentajeIndicador) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Listaindicadoresporcriterioporconfiguracion other = (Listaindicadoresporcriterioporconfiguracion) obj;
        if (this.cargaAcademica != other.cargaAcademica) {
            return false;
        }
        if (this.unidad != other.unidad) {
            return false;
        }
        if (Double.doubleToLongBits(this.porcentaje) != Double.doubleToLongBits(other.porcentaje)) {
            return false;
        }
        if (Double.doubleToLongBits(this.porcentajeIndicador) != Double.doubleToLongBits(other.porcentajeIndicador)) {
            return false;
        }
        if (!Objects.equals(this.nombreUnidad, other.nombreUnidad)) {
            return false;
        }
        if (!Objects.equals(this.criterio, other.criterio)) {
            return false;
        }
        if (!Objects.equals(this.indicador, other.indicador)) {
            return false;
        }
        if (!Objects.equals(this.listaindicadoresporcriterioporconfiguracionPK, other.listaindicadoresporcriterioporconfiguracionPK)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Listaindicadoresporcriterioporconfiguracion{" + "listaindicadoresporcriterioporconfiguracionPK=" + listaindicadoresporcriterioporconfiguracionPK + ", cargaAcademica=" + cargaAcademica + ", nombreUnidad=" + nombreUnidad + ", unidad=" + unidad + ", criterio=" + criterio + ", porcentaje=" + porcentaje + ", indicador=" + indicador + ", porcentajeIndicador=" + porcentajeIndicador + '}';
    }
}
