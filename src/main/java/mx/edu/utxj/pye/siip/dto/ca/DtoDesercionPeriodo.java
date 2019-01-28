/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.servlet.http.Part;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTODesercion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionPeriodos;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoDesercion;

/**
 *
 * @author Planeacion
 */
public class DtoDesercionPeriodo {
    /*Propiedades orginales del contolador*/
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter private EventosRegistros eventoActual, eventoSeleccionado;
    @Getter Short area;
    
    @Getter private ListaDesercionPeriodos listaDesercionPeriodos = new ListaDesercionPeriodos();
    /*Fin de las propiedades originales del controlador*/
     /*Propiedades nuevas para filtros y eliminacion*/
    @Getter private String mes;
    @Getter private Short ejercicioFiscal;
    //Propiedad que nos permitira revisar las fechas para los filtros....
    @Getter private Date hoy = new Date();
    
    @Getter private Integer claveActividad;
    
    @Getter private List<SelectItem> selectItemMes = new ArrayList<>();
    @Getter private List<SelectItem> selectItemEjercicioFiscal = new ArrayList<>();
    @Getter private List<DesercionPeriodosEscolares> listaDesercion;
    @Getter private List<ListaDtoDesercion> listaDtoDesercion = new ArrayList<>();
    //**Cambio de lista directa de una entitie para mostrar datos por un DTO **//
    @Getter private List<DTODesercion> listaDesercionDTO ;
    ////////////////////////////////////////////////
    /*Controles para la carga de evidencia*/
    
    @Getter private AreasUniversidad areaPOA;
    @Getter private ListaDtoDesercion registro;//representa al registro seleccionado o al registro circulante en la tabla de datos.
    
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad; 
    
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    
    @Getter private List<EventosRegistros> eventosPorPeriodo;
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    @Getter private List<Part> archivos;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
    
    @Getter private List<Short> clavesAreasSubordinadas;//claves de areas subordinas que no tienes poa
    ////////////////////////////////////////////////

    public DtoDesercionPeriodo() { 
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }

    
    public void setRegistroTipo(RegistrosTipo registroTipo) {
        this.registroTipo = registroTipo;
    }

    public void setEjesRegistro(EjesRegistro ejesRegistro) {
        this.ejesRegistro = ejesRegistro;
    }

    public void setArea(Short area) {
        this.area = area;
    }

    public void setListaDesercionPeriodos(ListaDesercionPeriodos listaDesercionPeriodos) {
        this.listaDesercionPeriodos = listaDesercionPeriodos;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setEjercicioFiscal(Short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public void setClaveActividad(Integer claveActividad) {
        this.claveActividad = claveActividad;
    }

    public void setSelectItemMes(List<SelectItem> selectItemMes) {
        this.selectItemMes = selectItemMes;
    }

    public void setSelectItemEjercicioFiscal(List<SelectItem> selectItemEjercicioFiscal) {
        this.selectItemEjercicioFiscal = selectItemEjercicioFiscal;
    }

    public void setListaDesercion(List<DesercionPeriodosEscolares> listaDesercion) {
        this.listaDesercion = listaDesercion;
    }

    public void setListaDesercionDTO(List<DTODesercion> listaDesercionDTO) {
        this.listaDesercionDTO = listaDesercionDTO;
    }

    public void setListaDtoDesercion(List<ListaDtoDesercion> listaDtoDesercion) {
        this.listaDtoDesercion = listaDtoDesercion;
    }
 public void setRegistro(ListaDtoDesercion registro) {
        this.registro = registro;
        if(registro==null){//si el registro es nullo se nulifican las evidencias
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

    public void setAreaPOA(AreasUniversidad areaPOA) {
        this.areaPOA = areaPOA;
    }

    public void setClavesAreasSubordinadas(List<Short> clavesAreasSubordinadas) {
        this.clavesAreasSubordinadas = clavesAreasSubordinadas;
    }
    
     public void setEventoActual(EventosRegistros eventoActual) {
        this.eventoActual = eventoActual;
       
    }

    public void setEventoSeleccionado(EventosRegistros eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public void setEventosPorPeriodo(List<EventosRegistros> eventosPorPeriodo) {
        this.eventosPorPeriodo = eventosPorPeriodo;
        if(eventosPorPeriodo != null && !eventosPorPeriodo.isEmpty()){
            setEventoSeleccionado(eventosPorPeriodo.get(0));
            if(getEventosPorPeriodo().contains(getEventoActual())){
                setEventoSeleccionado(getEventoActual());
            }
        }
    }
}
