/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.eb;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
public final class DtoDistribucionEquipamiento {
    /************************** Lista áreas ****************************************/
    @Getter @Setter private List<AreasUniversidad> listaAreasPOA; 
    
    @Getter @Setter private AreasUniversidad areaUniversidadPOA;
    
    @Getter private AreasUniversidad areaPOA;
    @Getter private DTOEquiposComputoCPE registroEquiposComputoCPE;
    @Getter private DTOEquiposComputoInternetCPE registroEquiposInternetCPE;
    
    @Getter private EjesRegistro eje;
    @Getter private EventosRegistros eventoActual, eventoSeleccionado;
    @Getter private PeriodosEscolares periodo;   
    @Getter @Setter private PeriodosEscolares periodoEscolarActivo;
    @Getter private RegistrosTipo registroTipoEqCom;
    @Getter private RegistrosTipo registroTipoEqComInt;
    
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad;
    
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    
    @Getter private String rutaArchivo;
    
    @Getter private List<EventosRegistros> eventosPorPeriodo;
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    
    @Getter private List<DTOEquiposComputoCPE> listaEquiposComputo;
    @Getter private List<DTOEquiposComputoInternetCPE> listaEquiposComputoInternet;
    
    @Getter private List<PeriodosEscolares> periodos;
    @Getter private List<Part> archivos;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;

    public DtoDistribucionEquipamiento() {
        setRegistroTipoEqCom(new RegistrosTipo((short)12));
        setRegistroTipoEqComInt(new RegistrosTipo((short)13));
        setEje(new EjesRegistro(5));
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }

    public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }

    public void setRegistroTipoEqCom(RegistrosTipo registroTipoEqCom) {
        this.registroTipoEqCom = registroTipoEqCom;
    }

    public void setRegistroTipoEqComInt(RegistrosTipo registroTipoEqComInt) {
        this.registroTipoEqComInt = registroTipoEqComInt;
    }

    public void setListaEquiposComputo(List<DTOEquiposComputoCPE> listaEquiposComputo) {
        this.listaEquiposComputo = listaEquiposComputo;
    }

    public void setListaEquiposComputoInternet(List<DTOEquiposComputoInternetCPE> listaEquiposComputoInternet) {
        this.listaEquiposComputoInternet = listaEquiposComputoInternet;
    }
    
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodo == null){
            setEventosPorPeriodo(Collections.EMPTY_LIST);
        }
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos!=null && !periodos.isEmpty()){
            setPeriodo(getPeriodos().get(0));
        }
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

    public void setRegistroEquiposComputoCPE(DTOEquiposComputoCPE registroEquiposComputoCPE) {
        this.registroEquiposComputoCPE = registroEquiposComputoCPE;
    }

    public void setRegistroEquiposInternetCPE(DTOEquiposComputoInternetCPE registroEquiposInternetCPE) {
        this.registroEquiposInternetCPE = registroEquiposInternetCPE;
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
    
}
