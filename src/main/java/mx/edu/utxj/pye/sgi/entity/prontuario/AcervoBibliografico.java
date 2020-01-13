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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "acervo_bibliografico", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AcervoBibliografico.findAll", query = "SELECT a FROM AcervoBibliografico a")
    , @NamedQuery(name = "AcervoBibliografico.findByIdAcervo", query = "SELECT a FROM AcervoBibliografico a WHERE a.idAcervo = :idAcervo")
    , @NamedQuery(name = "AcervoBibliografico.findByTitEducacion", query = "SELECT a FROM AcervoBibliografico a WHERE a.titEducacion = :titEducacion")
    , @NamedQuery(name = "AcervoBibliografico.findByVolEducacion", query = "SELECT a FROM AcervoBibliografico a WHERE a.volEducacion = :volEducacion")
    , @NamedQuery(name = "AcervoBibliografico.findByTitArtesyhum", query = "SELECT a FROM AcervoBibliografico a WHERE a.titArtesyhum = :titArtesyhum")
    , @NamedQuery(name = "AcervoBibliografico.findByVolArtesyhum", query = "SELECT a FROM AcervoBibliografico a WHERE a.volArtesyhum = :volArtesyhum")
    , @NamedQuery(name = "AcervoBibliografico.findByTitCsadmder", query = "SELECT a FROM AcervoBibliografico a WHERE a.titCsadmder = :titCsadmder")
    , @NamedQuery(name = "AcervoBibliografico.findByVolCsadmder", query = "SELECT a FROM AcervoBibliografico a WHERE a.volCsadmder = :volCsadmder")
    , @NamedQuery(name = "AcervoBibliografico.findByTitCnexacomp", query = "SELECT a FROM AcervoBibliografico a WHERE a.titCnexacomp = :titCnexacomp")
    , @NamedQuery(name = "AcervoBibliografico.findByVolCnexacomp", query = "SELECT a FROM AcervoBibliografico a WHERE a.volCnexacomp = :volCnexacomp")
    , @NamedQuery(name = "AcervoBibliografico.findByTitIngmancons", query = "SELECT a FROM AcervoBibliografico a WHERE a.titIngmancons = :titIngmancons")
    , @NamedQuery(name = "AcervoBibliografico.findByVolIngmancons", query = "SELECT a FROM AcervoBibliografico a WHERE a.volIngmancons = :volIngmancons")
    , @NamedQuery(name = "AcervoBibliografico.findByTitAgrovet", query = "SELECT a FROM AcervoBibliografico a WHERE a.titAgrovet = :titAgrovet")
    , @NamedQuery(name = "AcervoBibliografico.findByVolAgrovet", query = "SELECT a FROM AcervoBibliografico a WHERE a.volAgrovet = :volAgrovet")
    , @NamedQuery(name = "AcervoBibliografico.findByTitSalud", query = "SELECT a FROM AcervoBibliografico a WHERE a.titSalud = :titSalud")
    , @NamedQuery(name = "AcervoBibliografico.findByVolSalud", query = "SELECT a FROM AcervoBibliografico a WHERE a.volSalud = :volSalud")
    , @NamedQuery(name = "AcervoBibliografico.findByTitServicios", query = "SELECT a FROM AcervoBibliografico a WHERE a.titServicios = :titServicios")
    , @NamedQuery(name = "AcervoBibliografico.findByVolServicios", query = "SELECT a FROM AcervoBibliografico a WHERE a.volServicios = :volServicios")
    , @NamedQuery(name = "AcervoBibliografico.findByTitRevistas", query = "SELECT a FROM AcervoBibliografico a WHERE a.titRevistas = :titRevistas")
    , @NamedQuery(name = "AcervoBibliografico.findByVolRevistas", query = "SELECT a FROM AcervoBibliografico a WHERE a.volRevistas = :volRevistas")
    , @NamedQuery(name = "AcervoBibliografico.findByTitFolletos", query = "SELECT a FROM AcervoBibliografico a WHERE a.titFolletos = :titFolletos")
    , @NamedQuery(name = "AcervoBibliografico.findByVolFolletos", query = "SELECT a FROM AcervoBibliografico a WHERE a.volFolletos = :volFolletos")
    , @NamedQuery(name = "AcervoBibliografico.findByTitVideos", query = "SELECT a FROM AcervoBibliografico a WHERE a.titVideos = :titVideos")
    , @NamedQuery(name = "AcervoBibliografico.findByVolVideos", query = "SELECT a FROM AcervoBibliografico a WHERE a.volVideos = :volVideos")
    , @NamedQuery(name = "AcervoBibliografico.findByTotalTitulos", query = "SELECT a FROM AcervoBibliografico a WHERE a.totalTitulos = :totalTitulos")
    , @NamedQuery(name = "AcervoBibliografico.findByTotalVolumenes", query = "SELECT a FROM AcervoBibliografico a WHERE a.totalVolumenes = :totalVolumenes")})
public class AcervoBibliografico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAcervo")
    private Short idAcervo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_educacion")
    private short titEducacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_educacion")
    private short volEducacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_artesyhum")
    private short titArtesyhum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_artesyhum")
    private short volArtesyhum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_csadmder")
    private short titCsadmder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_csadmder")
    private short volCsadmder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_cnexacomp")
    private short titCnexacomp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_cnexacomp")
    private short volCnexacomp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_ingmancons")
    private short titIngmancons;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_ingmancons")
    private short volIngmancons;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_agrovet")
    private short titAgrovet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_agrovet")
    private short volAgrovet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_salud")
    private short titSalud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_salud")
    private short volSalud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_servicios")
    private short titServicios;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_servicios")
    private short volServicios;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_revistas")
    private short titRevistas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_revistas")
    private short volRevistas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_folletos")
    private short titFolletos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_folletos")
    private short volFolletos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tit_videos")
    private short titVideos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vol_videos")
    private short volVideos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_titulos")
    private short totalTitulos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_volumenes")
    private short totalVolumenes;
    @JoinColumn(name = "programa_educativo", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos programaEducativo;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false)
    private CiclosEscolares ciclo;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false)
    private PeriodosEscolares periodo;

    public AcervoBibliografico() {
    }

    public AcervoBibliografico(Short idAcervo) {
        this.idAcervo = idAcervo;
    }

    public AcervoBibliografico(Short idAcervo, short titEducacion, short volEducacion, short titArtesyhum, short volArtesyhum, short titCsadmder, short volCsadmder, short titCnexacomp, short volCnexacomp, short titIngmancons, short volIngmancons, short titAgrovet, short volAgrovet, short titSalud, short volSalud, short titServicios, short volServicios, short titRevistas, short volRevistas, short titFolletos, short volFolletos, short titVideos, short volVideos, short totalTitulos, short totalVolumenes) {
        this.idAcervo = idAcervo;
        this.titEducacion = titEducacion;
        this.volEducacion = volEducacion;
        this.titArtesyhum = titArtesyhum;
        this.volArtesyhum = volArtesyhum;
        this.titCsadmder = titCsadmder;
        this.volCsadmder = volCsadmder;
        this.titCnexacomp = titCnexacomp;
        this.volCnexacomp = volCnexacomp;
        this.titIngmancons = titIngmancons;
        this.volIngmancons = volIngmancons;
        this.titAgrovet = titAgrovet;
        this.volAgrovet = volAgrovet;
        this.titSalud = titSalud;
        this.volSalud = volSalud;
        this.titServicios = titServicios;
        this.volServicios = volServicios;
        this.titRevistas = titRevistas;
        this.volRevistas = volRevistas;
        this.titFolletos = titFolletos;
        this.volFolletos = volFolletos;
        this.titVideos = titVideos;
        this.volVideos = volVideos;
        this.totalTitulos = totalTitulos;
        this.totalVolumenes = totalVolumenes;
    }

    public Short getIdAcervo() {
        return idAcervo;
    }

    public void setIdAcervo(Short idAcervo) {
        this.idAcervo = idAcervo;
    }

    public short getTitEducacion() {
        return titEducacion;
    }

    public void setTitEducacion(short titEducacion) {
        this.titEducacion = titEducacion;
    }

    public short getVolEducacion() {
        return volEducacion;
    }

    public void setVolEducacion(short volEducacion) {
        this.volEducacion = volEducacion;
    }

    public short getTitArtesyhum() {
        return titArtesyhum;
    }

    public void setTitArtesyhum(short titArtesyhum) {
        this.titArtesyhum = titArtesyhum;
    }

    public short getVolArtesyhum() {
        return volArtesyhum;
    }

    public void setVolArtesyhum(short volArtesyhum) {
        this.volArtesyhum = volArtesyhum;
    }

    public short getTitCsadmder() {
        return titCsadmder;
    }

    public void setTitCsadmder(short titCsadmder) {
        this.titCsadmder = titCsadmder;
    }

    public short getVolCsadmder() {
        return volCsadmder;
    }

    public void setVolCsadmder(short volCsadmder) {
        this.volCsadmder = volCsadmder;
    }

    public short getTitCnexacomp() {
        return titCnexacomp;
    }

    public void setTitCnexacomp(short titCnexacomp) {
        this.titCnexacomp = titCnexacomp;
    }

    public short getVolCnexacomp() {
        return volCnexacomp;
    }

    public void setVolCnexacomp(short volCnexacomp) {
        this.volCnexacomp = volCnexacomp;
    }

    public short getTitIngmancons() {
        return titIngmancons;
    }

    public void setTitIngmancons(short titIngmancons) {
        this.titIngmancons = titIngmancons;
    }

    public short getVolIngmancons() {
        return volIngmancons;
    }

    public void setVolIngmancons(short volIngmancons) {
        this.volIngmancons = volIngmancons;
    }

    public short getTitAgrovet() {
        return titAgrovet;
    }

    public void setTitAgrovet(short titAgrovet) {
        this.titAgrovet = titAgrovet;
    }

    public short getVolAgrovet() {
        return volAgrovet;
    }

    public void setVolAgrovet(short volAgrovet) {
        this.volAgrovet = volAgrovet;
    }

    public short getTitSalud() {
        return titSalud;
    }

    public void setTitSalud(short titSalud) {
        this.titSalud = titSalud;
    }

    public short getVolSalud() {
        return volSalud;
    }

    public void setVolSalud(short volSalud) {
        this.volSalud = volSalud;
    }

    public short getTitServicios() {
        return titServicios;
    }

    public void setTitServicios(short titServicios) {
        this.titServicios = titServicios;
    }

    public short getVolServicios() {
        return volServicios;
    }

    public void setVolServicios(short volServicios) {
        this.volServicios = volServicios;
    }

    public short getTitRevistas() {
        return titRevistas;
    }

    public void setTitRevistas(short titRevistas) {
        this.titRevistas = titRevistas;
    }

    public short getVolRevistas() {
        return volRevistas;
    }

    public void setVolRevistas(short volRevistas) {
        this.volRevistas = volRevistas;
    }

    public short getTitFolletos() {
        return titFolletos;
    }

    public void setTitFolletos(short titFolletos) {
        this.titFolletos = titFolletos;
    }

    public short getVolFolletos() {
        return volFolletos;
    }

    public void setVolFolletos(short volFolletos) {
        this.volFolletos = volFolletos;
    }

    public short getTitVideos() {
        return titVideos;
    }

    public void setTitVideos(short titVideos) {
        this.titVideos = titVideos;
    }

    public short getVolVideos() {
        return volVideos;
    }

    public void setVolVideos(short volVideos) {
        this.volVideos = volVideos;
    }

    public short getTotalTitulos() {
        return totalTitulos;
    }

    public void setTotalTitulos(short totalTitulos) {
        this.totalTitulos = totalTitulos;
    }

    public short getTotalVolumenes() {
        return totalVolumenes;
    }

    public void setTotalVolumenes(short totalVolumenes) {
        this.totalVolumenes = totalVolumenes;
    }

    public ProgramasEducativos getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(ProgramasEducativos programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public CiclosEscolares getCiclo() {
        return ciclo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
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
        hash += (idAcervo != null ? idAcervo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AcervoBibliografico)) {
            return false;
        }
        AcervoBibliografico other = (AcervoBibliografico) object;
        if ((this.idAcervo == null && other.idAcervo != null) || (this.idAcervo != null && !this.idAcervo.equals(other.idAcervo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.AcervoBibliografico[ idAcervo=" + idAcervo + " ]";
    }
    
}
