/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ContactosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.CorreosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.TelefonosEmpresa;

/**
 *
 * @author Planeacion
 */
public final class DtoOrganismosVinculados {
    /****************** Programas Educativos Beneficiados con la vinculación *******************/
    @Getter @Setter AreasUniversidad areaUniversidad;
    @Getter private List<DTOProgramasBeneficiadosVinculacion> listaProgramasEducativosBeneficiadosV = new ArrayList<>();
    
    /************************** Ubicación *************************************/
    @Getter private Pais pais;
    @Getter private Estado estado;
    @Getter private Municipio municipio;
    @Getter private Localidad localidad;
    
    @Getter private List<Pais> paises;
    @Getter private List<Estado> estados;
    @Getter private List<Municipio> municipios;
    @Getter private List<Localidad> localidades;
    
    /************************** Teléfonos Empresa *************************************/
    @Getter @Setter TelefonosEmpresa telefonoEmpresa = new TelefonosEmpresa();
    @Getter @Setter private List<TelefonosEmpresa> listaTelefonosEmpresa = new ArrayList<>();
    
    /************************** Correos Empresa *************************************/
    @Getter @Setter CorreosEmpresa correoEmpresa = new CorreosEmpresa();
    @Getter @Setter private List<CorreosEmpresa> listaCorreosEmpresa = new ArrayList<>();
    
    /************************** Contactos empresa *************************************/
    @Getter @Setter ContactosEmpresa contactoEmpresa = new ContactosEmpresa();
    @Getter @Setter private List<ContactosEmpresa> listaContactosEmpresas = new ArrayList<>();
    
    /************************** Actividades de vinculación *************************************/
    @Getter @Setter ActividadesVinculacion actividadVinculacion;
    @Getter @Setter private List<DTOActividadesVinculacion> listaActividadesVinculacion = new ArrayList<>();
    
    /************************** Evidencias *************************************/
    @Getter private DTOOrganismoVinculado registro;
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    @Getter private List<Part> archivos;
    
    /************************** Alineación POA  *************************************/
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad; 
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
    
    /******************** Consulta de información *********************/
    @Getter @Setter private List<String> mesesConsulta = new ArrayList<>();
    @Getter @Setter private List<Short> aniosConsulta = new ArrayList<>();
    
    @Getter @Setter private String mesConsulta = null;
    @Getter @Setter private Short anioConsulta = null;
    
    @Getter @Setter private List<Short> registros;
    
    @Getter @Setter RegistrosTipo registroTipo;
    @Getter @Setter EjesRegistro ejesRegistro;
    
    @Getter @Setter AreasUniversidad area;
    @Getter @Setter String rutaArchivo;
    
    @Getter @Setter private List<OrganismosVinculados> lstOrganismosVinculados;

    public DtoOrganismosVinculados() {        
        setRegistroTipo(new RegistrosTipo((short)27));
        setEjesRegistro(new EjesRegistro(4));
        setRegistros(new ArrayList<>(Arrays.asList(registroTipo.getRegistroTipo())));
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }

    public void setRegistro(DTOOrganismoVinculado registro) {
        this.registro = registro;
        if(registro==null){
            setListaEvidencias(Collections.EMPTY_LIST);
        }
    }
    
    public void setArchivos(List<Part> archivos) {
        this.archivos = archivos;
    }
    
    public void setListaEvidencias(List<EvidenciasDetalle> listaEvidencias) {
        this.listaEvidencias = listaEvidencias;
        setTieneEvidencia(!listaEvidencias.isEmpty());
    }

    public void setTieneEvidencia(Boolean tieneEvidencia) {
        this.tieneEvidencia = tieneEvidencia;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }
    
    /********************** Alineación POA ************************************/
    
    public void setAlineacionEje(EjesRegistro alineacionEje) {
        this.alineacionEje = alineacionEje;
        if(alineacionEje == null)
            nulificarEje();
    }
    
    public void nulificarEje(){
        estrategias = Collections.EMPTY_LIST;
        nulificarEstrategia();
    }
    
    public void setAlineacionEstrategia(Estrategias alineacionEstrategia) {
        this.alineacionEstrategia = alineacionEstrategia;
        if(alineacionEstrategia == null)
            nulificarEstrategia();
    }
    
    public void nulificarEstrategia(){
        lineasAccion = Collections.EMPTY_LIST;
        nulificarLinea();
    }
    
    public void setAlineacionLinea(LineasAccion alineacionLinea) {
        this.alineacionLinea = alineacionLinea;
        if(alineacionLinea == null)
            nulificarLinea();
    }
    
    public void nulificarLinea(){
         actividades = Collections.EMPTY_LIST; 
    }
    
    public void setAlineacionActividad(ActividadesPoa alineacionActividad) {
        this.alineacionActividad = alineacionActividad;
    }
    
    public void nulificarActividad(){
        setAlineacionActividad(null);
    }
    
    public void setActividades(List<ActividadesPoa> actividades) {
        this.actividades = actividades;
    }
    
    public void setEjes(List<EjesRegistro> ejes) {
        this.ejes = ejes;
        if(ejes.isEmpty())
            nulificarEje();
    }

    public void setEstrategias(List<Estrategias> estrategias) {
        this.estrategias = estrategias;
        if(estrategias.isEmpty())
            nulificarEstrategia();
    }

    public void setLineasAccion(List<LineasAccion> lineasAccion) {
        this.lineasAccion = lineasAccion;
        if(lineasAccion.isEmpty())
            nulificarLinea();
    }
    
    /********************** Ubicación ************************************/
    
    public void setPais(Pais pais){
        this.pais = pais;
        if(pais == null){
            nulificarPais();
        }
    }
    
    public void nulificarPais(){
        estados = Collections.EMPTY_LIST;
        nulificarEstados();
    }
    
    public void nulificarEstados(){
        municipios = Collections.EMPTY_LIST;
        nulificarMunicipios();
    }
    
    public void nulificarMunicipios(){
        localidades = Collections.EMPTY_LIST;
    }
    
    public void setEstado(Estado estado){
        this.estado = estado;
        if(estado == null){
            nulificarEstados();
        }
    }
    
    public void setMunicipio(Municipio municipio){
        this.municipio = municipio;
        if(municipio == null){
            nulificarMunicipios();
        }
    }
    
    public void setLocalidad(Localidad localidad){
        this.localidad = localidad;
    }
    
    public void setPaises(List<Pais> paises){
        this.paises = paises;
        if(paises.isEmpty()){
            nulificarPais();
        }
    }
    
    public void setEstados(List<Estado> estados){
        this.estados = estados;
        if(estados.isEmpty()){
            nulificarEstados();
        }
    }
    
    public void setMunicipios(List<Municipio> municipios){
        this.municipios = municipios;
        if(municipios.isEmpty()){
            nulificarMunicipios();
        }
    }
    
    public void setLocalidades(List<Localidad> localidades){
        this.localidades = localidades;
    }
    
     public void setListaProgramasEducativosBeneficiadosV(List<DTOProgramasBeneficiadosVinculacion> listaProgramasEducativosBeneficiadosV) {
        this.listaProgramasEducativosBeneficiadosV = listaProgramasEducativosBeneficiadosV;
    }
}
