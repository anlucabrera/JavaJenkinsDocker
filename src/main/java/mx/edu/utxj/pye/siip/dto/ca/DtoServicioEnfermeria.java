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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaTipo;

/**
 *
 * @author UTXJ
 */
public final class DtoServicioEnfermeria {
    /************************** Edición ****************************************/
    @Getter @Setter List<ServiciosEnfermeriaTipo> listaServiciosEnfermeriaTipos;
    @Getter @Setter CiclosEscolares cicloEscolar;
    @Getter @Setter PeriodosEscolares periodoEscolar;
    @Getter @Setter Integer cicloInicio, cicloFin;
    @Getter @Setter String mensaje;
    
    /************************** Lista áreas ****************************************/
    @Getter private List<Categorias> listaCategoriasPOA;
    @Getter private List<AreasUniversidad> listaAreasPOA; 
    
    @Getter private Categorias categoria;
    @Getter private AreasUniversidad areaUniversidadPOA;
    
    /************************** Evidencias *************************************/
    @Getter private DTOServiciosEnfemeriaCicloPeriodos registro;
    
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
    @Getter private List<String> mesesConsulta = new ArrayList<>();
    @Getter private List<Short> aniosConsulta = new ArrayList<>();

    @Getter private String mesConsulta = null;
    @Getter private Short anioConsulta = null;
    
    @Getter @Setter private List<Short> registros;
    
    @Getter @Setter RegistrosTipo registroTipo;
    @Getter @Setter EjesRegistro ejesRegistro;
    
    @Getter @Setter AreasUniversidad area;
    @Getter @Setter String rutaArchivo;
    
    @Getter @Setter private List<DTOServiciosEnfemeriaCicloPeriodos> lista;
    
    public DtoServicioEnfermeria() {
        setRegistroTipo(new RegistrosTipo((short)7));
        setEjesRegistro(new EjesRegistro(3));
        setRegistros(new ArrayList<>(Arrays.asList(registroTipo.getRegistroTipo())));
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }
    
    public void setRegistro(DTOServiciosEnfemeriaCicloPeriodos registro) {
        this.registro = registro;
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
    
}
