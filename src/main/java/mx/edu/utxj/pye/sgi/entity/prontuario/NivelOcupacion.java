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
@Table(name = "nivel_ocupacion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelOcupacion.findAll", query = "SELECT n FROM NivelOcupacion n")
    , @NamedQuery(name = "NivelOcupacion.findByNivocup", query = "SELECT n FROM NivelOcupacion n WHERE n.nivocup = :nivocup")
    , @NamedQuery(name = "NivelOcupacion.findByFechaCorte", query = "SELECT n FROM NivelOcupacion n WHERE n.fechaCorte = :fechaCorte")
    , @NamedQuery(name = "NivelOcupacion.findBySiglas", query = "SELECT n FROM NivelOcupacion n WHERE n.siglas = :siglas")
    , @NamedQuery(name = "NivelOcupacion.findByOpeH", query = "SELECT n FROM NivelOcupacion n WHERE n.opeH = :opeH")
    , @NamedQuery(name = "NivelOcupacion.findByOpeM", query = "SELECT n FROM NivelOcupacion n WHERE n.opeM = :opeM")
    , @NamedQuery(name = "NivelOcupacion.findByTecgralH", query = "SELECT n FROM NivelOcupacion n WHERE n.tecgralH = :tecgralH")
    , @NamedQuery(name = "NivelOcupacion.findByTecgralM", query = "SELECT n FROM NivelOcupacion n WHERE n.tecgralM = :tecgralM")
    , @NamedQuery(name = "NivelOcupacion.findByTecespH", query = "SELECT n FROM NivelOcupacion n WHERE n.tecespH = :tecespH")
    , @NamedQuery(name = "NivelOcupacion.findByTecespM", query = "SELECT n FROM NivelOcupacion n WHERE n.tecespM = :tecespM")
    , @NamedQuery(name = "NivelOcupacion.findByAdmH", query = "SELECT n FROM NivelOcupacion n WHERE n.admH = :admH")
    , @NamedQuery(name = "NivelOcupacion.findByAdmM", query = "SELECT n FROM NivelOcupacion n WHERE n.admM = :admM")
    , @NamedQuery(name = "NivelOcupacion.findBySupH", query = "SELECT n FROM NivelOcupacion n WHERE n.supH = :supH")
    , @NamedQuery(name = "NivelOcupacion.findBySupM", query = "SELECT n FROM NivelOcupacion n WHERE n.supM = :supM")
    , @NamedQuery(name = "NivelOcupacion.findByGerH", query = "SELECT n FROM NivelOcupacion n WHERE n.gerH = :gerH")
    , @NamedQuery(name = "NivelOcupacion.findByGerM", query = "SELECT n FROM NivelOcupacion n WHERE n.gerM = :gerM")
    , @NamedQuery(name = "NivelOcupacion.findByDirH", query = "SELECT n FROM NivelOcupacion n WHERE n.dirH = :dirH")
    , @NamedQuery(name = "NivelOcupacion.findByDirM", query = "SELECT n FROM NivelOcupacion n WHERE n.dirM = :dirM")
    , @NamedQuery(name = "NivelOcupacion.findByAutH", query = "SELECT n FROM NivelOcupacion n WHERE n.autH = :autH")
    , @NamedQuery(name = "NivelOcupacion.findByAutM", query = "SELECT n FROM NivelOcupacion n WHERE n.autM = :autM")
    , @NamedQuery(name = "NivelOcupacion.findByOtrosH", query = "SELECT n FROM NivelOcupacion n WHERE n.otrosH = :otrosH")
    , @NamedQuery(name = "NivelOcupacion.findByOtrosM", query = "SELECT n FROM NivelOcupacion n WHERE n.otrosM = :otrosM")})
public class NivelOcupacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivocup")
    private Integer nivocup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "fecha_corte")
    private String fechaCorte;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ope_h")
    private int opeH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ope_m")
    private int opeM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tecgral_h")
    private int tecgralH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tecgral_m")
    private int tecgralM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tecesp_h")
    private int tecespH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tecesp_m")
    private int tecespM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adm_h")
    private int admH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adm_m")
    private int admM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sup_h")
    private int supH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sup_m")
    private int supM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ger_h")
    private int gerH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ger_m")
    private int gerM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dir_h")
    private int dirH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dir_m")
    private int dirM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aut_h")
    private int autH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aut_m")
    private int autM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "otros_h")
    private int otrosH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "otros_m")
    private int otrosM;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion")
    @ManyToOne(optional = false)
    private Generaciones generacion;

    public NivelOcupacion() {
    }

    public NivelOcupacion(Integer nivocup) {
        this.nivocup = nivocup;
    }

    public NivelOcupacion(Integer nivocup, String fechaCorte, String siglas, int opeH, int opeM, int tecgralH, int tecgralM, int tecespH, int tecespM, int admH, int admM, int supH, int supM, int gerH, int gerM, int dirH, int dirM, int autH, int autM, int otrosH, int otrosM) {
        this.nivocup = nivocup;
        this.fechaCorte = fechaCorte;
        this.siglas = siglas;
        this.opeH = opeH;
        this.opeM = opeM;
        this.tecgralH = tecgralH;
        this.tecgralM = tecgralM;
        this.tecespH = tecespH;
        this.tecespM = tecespM;
        this.admH = admH;
        this.admM = admM;
        this.supH = supH;
        this.supM = supM;
        this.gerH = gerH;
        this.gerM = gerM;
        this.dirH = dirH;
        this.dirM = dirM;
        this.autH = autH;
        this.autM = autM;
        this.otrosH = otrosH;
        this.otrosM = otrosM;
    }

    public Integer getNivocup() {
        return nivocup;
    }

    public void setNivocup(Integer nivocup) {
        this.nivocup = nivocup;
    }

    public String getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(String fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public int getOpeH() {
        return opeH;
    }

    public void setOpeH(int opeH) {
        this.opeH = opeH;
    }

    public int getOpeM() {
        return opeM;
    }

    public void setOpeM(int opeM) {
        this.opeM = opeM;
    }

    public int getTecgralH() {
        return tecgralH;
    }

    public void setTecgralH(int tecgralH) {
        this.tecgralH = tecgralH;
    }

    public int getTecgralM() {
        return tecgralM;
    }

    public void setTecgralM(int tecgralM) {
        this.tecgralM = tecgralM;
    }

    public int getTecespH() {
        return tecespH;
    }

    public void setTecespH(int tecespH) {
        this.tecespH = tecespH;
    }

    public int getTecespM() {
        return tecespM;
    }

    public void setTecespM(int tecespM) {
        this.tecespM = tecespM;
    }

    public int getAdmH() {
        return admH;
    }

    public void setAdmH(int admH) {
        this.admH = admH;
    }

    public int getAdmM() {
        return admM;
    }

    public void setAdmM(int admM) {
        this.admM = admM;
    }

    public int getSupH() {
        return supH;
    }

    public void setSupH(int supH) {
        this.supH = supH;
    }

    public int getSupM() {
        return supM;
    }

    public void setSupM(int supM) {
        this.supM = supM;
    }

    public int getGerH() {
        return gerH;
    }

    public void setGerH(int gerH) {
        this.gerH = gerH;
    }

    public int getGerM() {
        return gerM;
    }

    public void setGerM(int gerM) {
        this.gerM = gerM;
    }

    public int getDirH() {
        return dirH;
    }

    public void setDirH(int dirH) {
        this.dirH = dirH;
    }

    public int getDirM() {
        return dirM;
    }

    public void setDirM(int dirM) {
        this.dirM = dirM;
    }

    public int getAutH() {
        return autH;
    }

    public void setAutH(int autH) {
        this.autH = autH;
    }

    public int getAutM() {
        return autM;
    }

    public void setAutM(int autM) {
        this.autM = autM;
    }

    public int getOtrosH() {
        return otrosH;
    }

    public void setOtrosH(int otrosH) {
        this.otrosH = otrosH;
    }

    public int getOtrosM() {
        return otrosM;
    }

    public void setOtrosM(int otrosM) {
        this.otrosM = otrosM;
    }

    public Generaciones getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivocup != null ? nivocup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelOcupacion)) {
            return false;
        }
        NivelOcupacion other = (NivelOcupacion) object;
        if ((this.nivocup == null && other.nivocup != null) || (this.nivocup != null && !this.nivocup.equals(other.nivocup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.NivelOcupacion[ nivocup=" + nivocup + " ]";
    }
    
}
