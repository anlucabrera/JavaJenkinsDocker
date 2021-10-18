/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import lombok.Getter;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.caphum.DTOReconocimientoProdep;
/**
 *
 * @author UTXJ
 */
public class DtoReconocimientoProdep {
    @Getter private AreasUniversidad areaPOA;
    @Getter private DTOReconocimientoProdep registro;//representa al registro seleccionado o al registro circulante en la tabla de datos.
    @Getter private EjesRegistro eje;
    @Getter private EventosRegistros eventoActual;
    @Getter private RegistrosTipo registroTipo;
    
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad; 
    
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    @Getter private Short area;
    @Getter private String rutaArchivo;
    
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    @Getter private List<DTOReconocimientoProdep> lista;
    @Getter private List<Part> archivos;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
    
    @Getter private List<Short> clavesAreasSubordinadas;//claves de areas subordinas que no tienes poa
    
     //Para filtrado
    @Getter private String mes;
    @Getter private Short ejercicioFiscal;
    @Getter private Date hoy = new Date();
    
    @Getter private Integer claveActividad;
    @Getter private List<SelectItem> selectItemMes = new ArrayList<>();
    @Getter private List<SelectItem> selectItemEjercicioFiscal = new ArrayList<>();

    public DtoReconocimientoProdep() {
        setRegistroTipo(new RegistrosTipo((short)48));
        setEje(new EjesRegistro(1));
        
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }
     public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }

    public void setLista(List<DTOReconocimientoProdep> lista) {
        this.lista = lista;
    }

    public void setRegistroTipo(RegistrosTipo registroTipo) {
        this.registroTipo = registroTipo;
    }

    public void setArea(Short area) {
        this.area = area;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

   
    public void setEventoActual(EventosRegistros eventoActual) {
        this.eventoActual = eventoActual;
    }

    public void setRegistro(DTOReconocimientoProdep registro) {
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
    
}
