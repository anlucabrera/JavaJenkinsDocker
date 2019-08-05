/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.DatosAsesoriasTutorias;
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
public final class DtoAsesoriaTutoriaCuatrimestral {
    /************************** Edición ****************************************/
    @Getter @Setter private PeriodosEscolares               periodoEscolarAsesoriaTutoria;
    @Getter @Setter private List<DatosAsesoriasTutorias>    listaDatosAsesoriasTutorias;
    @Getter @Setter private DatosAsesoriasTutorias          datoAsesoriaTutoria;
    @Getter @Setter private String                          mensaje;
    @Getter @Setter private Boolean                         nuevoRegistro;
    
    /************************** Lista áreas ****************************************/
    @Getter @Setter private List<AreasUniversidad> listaAreasPOA; 
    
    @Getter @Setter private AreasUniversidad areaUniversidadPOAAdministrador;
    @Getter private AreasUniversidad areaPOA;
    @Getter private DTOAsesoriasTutoriasCuatrimestrales dtoAsesoriaTutoriaCuatrimestralR;
    
    @Getter private EjesRegistro eje;
    @Getter private EventosRegistros eventoActual, eventoSeleccionado;
    @Getter private PeriodosEscolares periodo;
    @Getter @Setter private PeriodosEscolares periodoEscolarActivo;
    
    @Getter @Setter private RegistrosTipo registroTipo;
    
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad;
    
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    
    @Getter private List<EventosRegistros> eventosPorPeriodo;
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    @Getter @Setter private List<DTOAsesoriasTutoriasCuatrimestrales> listaDtoAsesoriasTutoriasCuatrimestrales;
    @Getter private List<PeriodosEscolares> periodos;
    @Getter private List<Part> archivos;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
    
    
    public DtoAsesoriaTutoriaCuatrimestral(){
        setRegistroTipo(new RegistrosTipo((short)53));
        setEje(new EjesRegistro(3));
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }
    
    public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }
    
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodo == null){//si el periodo es nulo se nulifican los eventos
            setEventosPorPeriodo(Collections.EMPTY_LIST);
        }
    }
    
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos!=null && !periodos.isEmpty()){    //si la lista de periodos existe se selecciona el primer periodo enlistado        
            setPeriodo(getPeriodos().get(0));
        }
    }
    
    public void setEventoActual(EventosRegistros eventoActual) {
        this.eventoActual = eventoActual;
//        System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setEventoActual(): " + eventoActual);
    }

    public void setEventoSeleccionado(EventosRegistros eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public void setEventosPorPeriodo(List<EventosRegistros> eventosPorPeriodo) {
        this.eventosPorPeriodo = eventosPorPeriodo;
        if(eventosPorPeriodo != null && !eventosPorPeriodo.isEmpty()){//si la lista de eventos existe
            setEventoSeleccionado(eventosPorPeriodo.get(0));//se selecciona el primer evento de la lista
            if(getEventosPorPeriodo().contains(getEventoActual())){//si la lista de eventos contiene al evento actual se declara a este como seleccionado
                setEventoSeleccionado(getEventoActual());
            }
        }
    }

    public void setDtoAsesoriaTutoriaCuatrimestralR(DTOAsesoriasTutoriasCuatrimestrales dtoAsesoriaTutoriaCuatrimestralR) {
        this.dtoAsesoriaTutoriaCuatrimestralR = dtoAsesoriaTutoriaCuatrimestralR;
        if(dtoAsesoriaTutoriaCuatrimestralR==null){//si el registro es nullo se nulifican las evidencias
            setListaEvidencias(Collections.EMPTY_LIST);
        }
    }
    
    public void setArchivos(List<Part> archivos) {
        this.archivos = archivos;
    }

    public void setListaEvidencias(List<EvidenciasDetalle> listaEvidencias) {
        this.listaEvidencias = listaEvidencias;
        setTieneEvidencia(!listaEvidencias.isEmpty());
//        this.evidencias.put(registro, listaEvidencias);
    }

    public void setTieneEvidencia(Boolean tieneEvidencia) {
        this.tieneEvidencia = tieneEvidencia;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }
    
    public void setAlineacionEje(EjesRegistro alineacionEje) {
//        System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setAlineacionEje(): " + eje);
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
//        System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setAlineacionLinea(): " + alineacionLinea);
        this.alineacionLinea = alineacionLinea;
        
        if(alineacionLinea == null)
            nulificarLinea();
    }
    
    public void nulificarLinea(){
         actividades = Collections.EMPTY_LIST;
//         nulificarActividad();
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
