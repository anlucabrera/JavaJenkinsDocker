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
@Table(name = "nivel_ingreso", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelIngreso.findAll", query = "SELECT n FROM NivelIngreso n")
    , @NamedQuery(name = "NivelIngreso.findByNiving", query = "SELECT n FROM NivelIngreso n WHERE n.niving = :niving")
    , @NamedQuery(name = "NivelIngreso.findByFechaCorte", query = "SELECT n FROM NivelIngreso n WHERE n.fechaCorte = :fechaCorte")
    , @NamedQuery(name = "NivelIngreso.findByMen2500H", query = "SELECT n FROM NivelIngreso n WHERE n.men2500H = :men2500H")
    , @NamedQuery(name = "NivelIngreso.findByMen2500M", query = "SELECT n FROM NivelIngreso n WHERE n.men2500M = :men2500M")
    , @NamedQuery(name = "NivelIngreso.findByH", query = "SELECT n FROM NivelIngreso n WHERE n.h = :h")
    , @NamedQuery(name = "NivelIngreso.findByM", query = "SELECT n FROM NivelIngreso n WHERE n.m = :m")
    , @NamedQuery(name = "NivelIngreso.findByH1", query = "SELECT n FROM NivelIngreso n WHERE n.h1 = :h1")
    , @NamedQuery(name = "NivelIngreso.findByM1", query = "SELECT n FROM NivelIngreso n WHERE n.m1 = :m1")
    , @NamedQuery(name = "NivelIngreso.findByH2", query = "SELECT n FROM NivelIngreso n WHERE n.h2 = :h2")
    , @NamedQuery(name = "NivelIngreso.findByM2", query = "SELECT n FROM NivelIngreso n WHERE n.m2 = :m2")
    , @NamedQuery(name = "NivelIngreso.findByH3", query = "SELECT n FROM NivelIngreso n WHERE n.h3 = :h3")
    , @NamedQuery(name = "NivelIngreso.findByM3", query = "SELECT n FROM NivelIngreso n WHERE n.m3 = :m3")
    , @NamedQuery(name = "NivelIngreso.findByH4", query = "SELECT n FROM NivelIngreso n WHERE n.h4 = :h4")
    , @NamedQuery(name = "NivelIngreso.findByM4", query = "SELECT n FROM NivelIngreso n WHERE n.m4 = :m4")
    , @NamedQuery(name = "NivelIngreso.findByMas12000H", query = "SELECT n FROM NivelIngreso n WHERE n.mas12000H = :mas12000H")
    , @NamedQuery(name = "NivelIngreso.findByMas12000M", query = "SELECT n FROM NivelIngreso n WHERE n.mas12000M = :mas12000M")
    , @NamedQuery(name = "NivelIngreso.findByNoEspH", query = "SELECT n FROM NivelIngreso n WHERE n.noEspH = :noEspH")
    , @NamedQuery(name = "NivelIngreso.findByNoEspM", query = "SELECT n FROM NivelIngreso n WHERE n.noEspM = :noEspM")})
public class NivelIngreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "niving")
    private Integer niving;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "fecha_corte")
    private String fechaCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "men_2500_h")
    private int men2500H;
    @Basic(optional = false)
    @NotNull
    @Column(name = "men_2500_m")
    private int men2500M;
    @Basic(optional = false)
    @NotNull
    @Column(name = "2500_3999_h")
    private int h;
    @Basic(optional = false)
    @NotNull
    @Column(name = "2500_3999_m")
    private int m;
    @Basic(optional = false)
    @NotNull
    @Column(name = "4000_5999_h")
    private int h1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "4000_5999_m")
    private int m1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "6000_7999_h")
    private int h2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "6000_7999_m")
    private int m2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "8000_9999_h")
    private int h3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "8000_9999_m")
    private int m3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "10000_11999_h")
    private int h4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "10000_11999_m")
    private int m4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mas_12000_h")
    private int mas12000H;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mas_12000_m")
    private int mas12000M;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_esp_h")
    private int noEspH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_esp_m")
    private int noEspM;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion")
    @ManyToOne(optional = false)
    private Generaciones generacion;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos siglas;

    public NivelIngreso() {
    }

    public NivelIngreso(Integer niving) {
        this.niving = niving;
    }

    public NivelIngreso(Integer niving, String fechaCorte, int men2500H, int men2500M, int h, int m, int h1, int m1, int h2, int m2, int h3, int m3, int h4, int m4, int mas12000H, int mas12000M, int noEspH, int noEspM) {
        this.niving = niving;
        this.fechaCorte = fechaCorte;
        this.men2500H = men2500H;
        this.men2500M = men2500M;
        this.h = h;
        this.m = m;
        this.h1 = h1;
        this.m1 = m1;
        this.h2 = h2;
        this.m2 = m2;
        this.h3 = h3;
        this.m3 = m3;
        this.h4 = h4;
        this.m4 = m4;
        this.mas12000H = mas12000H;
        this.mas12000M = mas12000M;
        this.noEspH = noEspH;
        this.noEspM = noEspM;
    }

    public Integer getNiving() {
        return niving;
    }

    public void setNiving(Integer niving) {
        this.niving = niving;
    }

    public String getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(String fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public int getMen2500H() {
        return men2500H;
    }

    public void setMen2500H(int men2500H) {
        this.men2500H = men2500H;
    }

    public int getMen2500M() {
        return men2500M;
    }

    public void setMen2500M(int men2500M) {
        this.men2500M = men2500M;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getH1() {
        return h1;
    }

    public void setH1(int h1) {
        this.h1 = h1;
    }

    public int getM1() {
        return m1;
    }

    public void setM1(int m1) {
        this.m1 = m1;
    }

    public int getH2() {
        return h2;
    }

    public void setH2(int h2) {
        this.h2 = h2;
    }

    public int getM2() {
        return m2;
    }

    public void setM2(int m2) {
        this.m2 = m2;
    }

    public int getH3() {
        return h3;
    }

    public void setH3(int h3) {
        this.h3 = h3;
    }

    public int getM3() {
        return m3;
    }

    public void setM3(int m3) {
        this.m3 = m3;
    }

    public int getH4() {
        return h4;
    }

    public void setH4(int h4) {
        this.h4 = h4;
    }

    public int getM4() {
        return m4;
    }

    public void setM4(int m4) {
        this.m4 = m4;
    }

    public int getMas12000H() {
        return mas12000H;
    }

    public void setMas12000H(int mas12000H) {
        this.mas12000H = mas12000H;
    }

    public int getMas12000M() {
        return mas12000M;
    }

    public void setMas12000M(int mas12000M) {
        this.mas12000M = mas12000M;
    }

    public int getNoEspH() {
        return noEspH;
    }

    public void setNoEspH(int noEspH) {
        this.noEspH = noEspH;
    }

    public int getNoEspM() {
        return noEspM;
    }

    public void setNoEspM(int noEspM) {
        this.noEspM = noEspM;
    }

    public Generaciones getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public ProgramasEducativos getSiglas() {
        return siglas;
    }

    public void setSiglas(ProgramasEducativos siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (niving != null ? niving.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelIngreso)) {
            return false;
        }
        NivelIngreso other = (NivelIngreso) object;
        if ((this.niving == null && other.niving != null) || (this.niving != null && !this.niving.equals(other.niving))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.NivelIngreso[ niving=" + niving + " ]";
    }
    
}
