/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
public final class DtoProductoAcademico {
    /****************************** Edición ****************************************/
    @Getter @Setter List<AreasUniversidad> listaAreasAcademicas;
    @Getter @Setter List<ProductosAcademicos> listaProductosAcademicoInt;
    
    @Getter @Setter AreasUniversidad areaAcademica;
    
    @Getter private Pais pais;
    @Getter private Estado estado;
    @Getter private Municipio municipio;
    
    @Getter private List<Pais> paises;
    @Getter private List<Estado> estados;
    @Getter private List<Municipio> municipios;
    @Getter @Setter private String mensaje;
    
    @Getter @Setter List<ListaPersonal> listaPersonal;
    @Getter @Setter ListaPersonal persona;
    
    /************************** Lista áreas ****************************************/
    @Getter private List<Categorias> listaCategoriasPOA;
    @Getter private List<AreasUniversidad> listaAreasPOA; 
    
    @Getter private Categorias categoria;
    @Getter private AreasUniversidad areaUniversidadPOA;
    
    /************************** Evidencias *************************************/
    @Getter private DTOProductosAcademicos registroProductoAcademico;
    @Getter private DTOProductosAcademicosPersonal registroProductosAcademicosPersonal;
    
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
    
    @Getter @Setter RegistrosTipo registroTipoPA;
    @Getter @Setter RegistrosTipo registroTipoPAP;
    @Getter @Setter EjesRegistro ejesRegistro;
    
    @Getter @Setter private AreasUniversidad area;
    @Getter @Setter private String rutaArchivo;
 
    @Getter @Setter private List<DTOProductosAcademicos> lstDtoProductosAcademicos;
    @Getter @Setter private List<DTOProductosAcademicosPersonal> lstDtoProductosAcademicosPersonal;
    
    public DtoProductoAcademico(){
        setRegistroTipoPA(new RegistrosTipo((short)41));
        setRegistroTipoPAP(new RegistrosTipo((short)42));
        setEjesRegistro(new EjesRegistro(3));
        setRegistros(new ArrayList<>(Arrays.asList(registroTipoPA.getRegistroTipo(),registroTipoPAP.getRegistroTipo())));
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }

    public void setRegistroProductoAcademico(DTOProductosAcademicos registroProductoAcademico) {
        this.registroProductoAcademico = registroProductoAcademico;
        if(registroProductoAcademico == null){
            setListaEvidencias(Collections.EMPTY_LIST);
        }
    }

    public void setRegistroProductosAcademicosPersonal(DTOProductosAcademicosPersonal registroProductosAcademicosPersonal) {
        this.registroProductosAcademicosPersonal = registroProductosAcademicosPersonal;
        if(registroProductosAcademicosPersonal == null){
            setListaEvidencias(Collections.EMPTY_LIST);
        }
    }
    
    public void setArchivos(List<Part> archivos){
        this.archivos = archivos;
    }
    
    public void setListaEvidencias(List<EvidenciasDetalle> listaEvidencias){
        this.listaEvidencias = listaEvidencias;
        setTieneEvidencia(!listaEvidencias.isEmpty());
    }
    
    public void setTieneEvidencia(Boolean tieneEvidencia){
        this.tieneEvidencia = tieneEvidencia;
    }
    
    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo){
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }
    
    /********************** Alineación POA ************************************/
    
    public void setAlineacionEje(EjesRegistro alineacionEje){
        this.alineacionEje = alineacionEje;
        if(alineacionEje == null)
            nulificarEje();
    }
    
    public void nulificarEje(){
        estrategias = Collections.EMPTY_LIST;
        nulificarEstrategia();
    }
    
    public void setAlineacionEstrategia(Estrategias alineacionEstrategia) {
//        System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setAlineacionEstrategia(): " + alineacionEstrategia);
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
    
    public void setListaCategoriasPOA(List<Categorias> listaCategoriasPOA) {
        this.listaCategoriasPOA = listaCategoriasPOA;
        if(listaCategoriasPOA.isEmpty())
            nulificarCategoria();
    }

    public void setListaAreasPOA(List<AreasUniversidad> listaAreasPOA) {
        this.listaAreasPOA = listaAreasPOA;
        if(listaAreasPOA.isEmpty())
            nulificarAreaPOA();
    }

    public void setAniosConsulta(List<Short> aniosConsulta) {
        this.aniosConsulta = aniosConsulta;
        if(aniosConsulta.isEmpty())
            nulificarAnioConsulta();
    }
    
    public void setMesesConsulta(List<String> mesesConsulta) {
        this.mesesConsulta = mesesConsulta;
    }
    
    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
        if(categoria == null)
            nulificarCategoria();
    }

    public void setAreaUniversidadPOA(AreasUniversidad areaUniversidadPOA) {
        this.areaUniversidadPOA = areaUniversidadPOA;
        if(areaUniversidadPOA == null)
            nulificarAreaPOA();
    }

    public void setAnioConsulta(Short anioConsulta) {
        this.anioConsulta = anioConsulta;
        if(anioConsulta == null)
            nulificarAnioConsulta();
    }
    
    public void setMesConsulta(String mesConsulta) {
        this.mesConsulta = mesConsulta;
    }
    
    public void nulificarCategoria(){
        listaAreasPOA = Collections.EMPTY_LIST;
        nulificarAreaPOA();
    }
    
    public void nulificarAreaPOA(){
        aniosConsulta = Collections.EMPTY_LIST;
        nulificarAnioConsulta();
    }
    
    public void nulificarAnioConsulta(){
        mesesConsulta = Collections.EMPTY_LIST;
    }
    
    /**
     * ******************** Ubicación ***********************************
     */
    public void setPais(Pais pais) {
        this.pais = pais;
        if (pais == null) {
            nulificarPais();
        }
    }

    public void nulificarPais() {
        estados = Collections.EMPTY_LIST;
        nulificarEstados();
    }

    public void nulificarEstados() {
        municipios = Collections.EMPTY_LIST;
    }

    
    public void setEstado(Estado estado) {
        this.estado = estado;
        if (estado == null) {
            nulificarEstados();
        }
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public void setPaises(List<Pais> paises) {
        this.paises = paises;
        if (paises.isEmpty()) {
            nulificarPais();
        }
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
        if (estados.isEmpty()) {
            nulificarEstados();
        }
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

}
