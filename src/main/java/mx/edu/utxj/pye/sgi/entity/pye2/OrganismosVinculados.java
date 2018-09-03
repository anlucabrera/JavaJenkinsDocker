/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "organismos_vinculados", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganismosVinculados.findAll", query = "SELECT o FROM OrganismosVinculados o")
    , @NamedQuery(name = "OrganismosVinculados.findByRegistro", query = "SELECT o FROM OrganismosVinculados o WHERE o.registro = :registro")
    , @NamedQuery(name = "OrganismosVinculados.findByEmpresa", query = "SELECT o FROM OrganismosVinculados o WHERE o.empresa = :empresa")
    , @NamedQuery(name = "OrganismosVinculados.findByFecha", query = "SELECT o FROM OrganismosVinculados o WHERE o.fecha = :fecha")
    , @NamedQuery(name = "OrganismosVinculados.findByNombre", query = "SELECT o FROM OrganismosVinculados o WHERE o.nombre = :nombre")
    , @NamedQuery(name = "OrganismosVinculados.findByDireccion", query = "SELECT o FROM OrganismosVinculados o WHERE o.direccion = :direccion")
    , @NamedQuery(name = "OrganismosVinculados.findByCp", query = "SELECT o FROM OrganismosVinculados o WHERE o.cp = :cp")
    , @NamedQuery(name = "OrganismosVinculados.findByRepresentante", query = "SELECT o FROM OrganismosVinculados o WHERE o.representante = :representante")
    , @NamedQuery(name = "OrganismosVinculados.findByTelefono", query = "SELECT o FROM OrganismosVinculados o WHERE o.telefono = :telefono")
    , @NamedQuery(name = "OrganismosVinculados.findByTelefonoOtro", query = "SELECT o FROM OrganismosVinculados o WHERE o.telefonoOtro = :telefonoOtro")
    , @NamedQuery(name = "OrganismosVinculados.findByEmail", query = "SELECT o FROM OrganismosVinculados o WHERE o.email = :email")
    , @NamedQuery(name = "OrganismosVinculados.findByConvenio", query = "SELECT o FROM OrganismosVinculados o WHERE o.convenio = :convenio")
    , @NamedQuery(name = "OrganismosVinculados.findByEstatus", query = "SELECT o FROM OrganismosVinculados o WHERE o.estatus = :estatus")})
public class OrganismosVinculados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @Column(name = "empresa")
    private int empresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cp")
    private String cp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "representante")
    private String representante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 15)
    @Column(name = "telefono_otro")
    private String telefonoOtro;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "convenio")
    private String convenio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @ManyToMany(mappedBy = "organismosVinculadosList")
    private List<ActividadesVinculacion> actividadesVinculacionList;
    @OneToMany(mappedBy = "empresa")
    private List<ServiciosTecnologicosParticipantes> serviciosTecnologicosParticipantesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<VisitasIndustriales> visitasIndustrialesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Convenios> conveniosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "institucionOrganizacion")
    private List<RegistrosMovilidad> registrosMovilidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<BolsaTrabajo> bolsaTrabajoList;
    @JoinColumn(name = "emp_tip", referencedColumnName = "emptipo")
    @ManyToOne(optional = false)
    private EmpresasTipo empTip;
    @JoinColumn(name = "giro", referencedColumnName = "giro")
    @ManyToOne(optional = false)
    private GirosTipo giro;
    @JoinColumns({
        @JoinColumn(name = "estado", referencedColumnName = "claveEstado")
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio")})
    @ManyToOne(optional = false)
    private Municipio municipio;
    @JoinColumn(name = "org_tip", referencedColumnName = "orgtipo")
    @ManyToOne(optional = false)
    private OrganismosTipo orgTip;
    @JoinColumn(name = "pais", referencedColumnName = "idpais")
    @ManyToOne(optional = false)
    private Pais pais;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "sector", referencedColumnName = "sector")
    @ManyToOne(optional = false)
    private SectoresTipo sector;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<EstadiasPorEstudiante> estadiasPorEstudianteList;

    public OrganismosVinculados() {
    }

    public OrganismosVinculados(Integer registro) {
        this.registro = registro;
    }

    public OrganismosVinculados(Integer registro, int empresa, Date fecha, String nombre, String direccion, String cp, String representante, String telefono, String email, String convenio, boolean estatus) {
        this.registro = registro;
        this.empresa = empresa;
        this.fecha = fecha;
        this.nombre = nombre;
        this.direccion = direccion;
        this.cp = cp;
        this.representante = representante;
        this.telefono = telefono;
        this.email = email;
        this.convenio = convenio;
        this.estatus = estatus;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoOtro() {
        return telefonoOtro;
    }

    public void setTelefonoOtro(String telefonoOtro) {
        this.telefonoOtro = telefonoOtro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<ActividadesVinculacion> getActividadesVinculacionList() {
        return actividadesVinculacionList;
    }

    public void setActividadesVinculacionList(List<ActividadesVinculacion> actividadesVinculacionList) {
        this.actividadesVinculacionList = actividadesVinculacionList;
    }

    @XmlTransient
    public List<ServiciosTecnologicosParticipantes> getServiciosTecnologicosParticipantesList() {
        return serviciosTecnologicosParticipantesList;
    }

    public void setServiciosTecnologicosParticipantesList(List<ServiciosTecnologicosParticipantes> serviciosTecnologicosParticipantesList) {
        this.serviciosTecnologicosParticipantesList = serviciosTecnologicosParticipantesList;
    }

    @XmlTransient
    public List<VisitasIndustriales> getVisitasIndustrialesList() {
        return visitasIndustrialesList;
    }

    public void setVisitasIndustrialesList(List<VisitasIndustriales> visitasIndustrialesList) {
        this.visitasIndustrialesList = visitasIndustrialesList;
    }

    @XmlTransient
    public List<Convenios> getConveniosList() {
        return conveniosList;
    }

    public void setConveniosList(List<Convenios> conveniosList) {
        this.conveniosList = conveniosList;
    }

    @XmlTransient
    public List<RegistrosMovilidad> getRegistrosMovilidadList() {
        return registrosMovilidadList;
    }

    public void setRegistrosMovilidadList(List<RegistrosMovilidad> registrosMovilidadList) {
        this.registrosMovilidadList = registrosMovilidadList;
    }

    @XmlTransient
    public List<BolsaTrabajo> getBolsaTrabajoList() {
        return bolsaTrabajoList;
    }

    public void setBolsaTrabajoList(List<BolsaTrabajo> bolsaTrabajoList) {
        this.bolsaTrabajoList = bolsaTrabajoList;
    }

    public EmpresasTipo getEmpTip() {
        return empTip;
    }

    public void setEmpTip(EmpresasTipo empTip) {
        this.empTip = empTip;
    }

    public GirosTipo getGiro() {
        return giro;
    }

    public void setGiro(GirosTipo giro) {
        this.giro = giro;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public OrganismosTipo getOrgTip() {
        return orgTip;
    }

    public void setOrgTip(OrganismosTipo orgTip) {
        this.orgTip = orgTip;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public SectoresTipo getSector() {
        return sector;
    }

    public void setSector(SectoresTipo sector) {
        this.sector = sector;
    }

    @XmlTransient
    public List<EstadiasPorEstudiante> getEstadiasPorEstudianteList() {
        return estadiasPorEstudianteList;
    }

    public void setEstadiasPorEstudianteList(List<EstadiasPorEstudiante> estadiasPorEstudianteList) {
        this.estadiasPorEstudianteList = estadiasPorEstudianteList;
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
        if (!(object instanceof OrganismosVinculados)) {
            return false;
        }
        OrganismosVinculados other = (OrganismosVinculados) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados[ registro=" + registro + " ]";
    }
    
}
