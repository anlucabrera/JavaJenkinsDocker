package mx.edu.utxj.pye.siip.dto.ca;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
public final class DtoAsesoriasTutorias {
    @Getter private AreasUniversidad areaPOA;
    @Getter private DTOAsesoriasTutoriasCicloPeriodos registro;//representa al registro seleccionado o al registro circulante en la tabla de datos.
    @Getter private EjesRegistro eje;
    @Getter private EventosRegistros eventoActual, eventoSeleccionado;
    @Getter private PeriodosEscolares periodo;    
    @Getter private RegistrosTipo registroTipo;
    
    
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad; 
    
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    @Getter private Short area;
    @Getter private String rutaArchivo;
    
    @Getter private List<EventosRegistros> eventosPorPeriodo;
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    @Getter private List<DTOAsesoriasTutoriasCicloPeriodos> lista;
    @Getter private List<PeriodosEscolares> periodos;
    @Getter private List<Part> archivos;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
//    @Getter private Map<DTOAsesoriasTutoriasCicloPeriodos, List<EvidenciasDetalle>> evidencias;//representa las evidencias del registro seleccionado o registro circulante

    public DtoAsesoriasTutorias() {
        setRegistroTipo(new RegistrosTipo((short)3));
        setEje(new EjesRegistro(3));
//        evidencias = new HashMap<>();
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }   

    public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }

    public void setLista(List<DTOAsesoriasTutoriasCicloPeriodos> lista) {
        this.lista = lista;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodo == null){//si el periodo es nulo se nulifican los eventos
            setEventosPorPeriodo(Collections.EMPTY_LIST);
        }
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
//            eventosPorPeriodo.forEach(e -> System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setEventosPorPeriodo() evento: " + e.getMes()));
            setEventoSeleccionado(eventosPorPeriodo.get(0));//se selecciona el primer evento de la lista
            if(getEventosPorPeriodo().contains(getEventoActual())){//si la lista de eventos contiene al evento actual se declara a este como seleccionado
                setEventoSeleccionado(getEventoActual());
            }
        }
    }

    public void setRegistro(DTOAsesoriasTutoriasCicloPeriodos registro) {
        this.registro = registro;
        if(registro==null){//si el registro es nullo se nulifican las evidencias
            setListaEvidencias(Collections.EMPTY_LIST);
        }
//        else{
//            if(evidencias.get(registro) != null){
//                setListaEvidencias(evidencias.get(registro));
//            }
//        }
    }

//    private void setEvidencias(Map<DTOAsesoriasTutoriasCicloPeriodos, List<EvidenciasDetalle>> evidencias) {
//        this.evidencias = evidencias;
////        registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().setEvidenciasList(evidencias.get(registro));
//    }
    
//    public void addEvidencia(DTOAsesoriasTutoriasCicloPeriodos registro, List<EvidenciasDetalle> evidencias){
////        System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.addEvidencia(" + evidencias.size() + ") registro: " + registro);
////        evidencias.forEach(ed -> System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.addEvidencia() ed: " + ed));
//        this.evidencias.put(registro, evidencias);
//    }

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
        this.alineacionEje = alineacionEje;
        
        if(alineacionEje == null)
            nulificarEje();
    }
    
    private void nulificarEje(){
        estrategias = Collections.EMPTY_LIST;
        nulificarEstrategia();
    }

    public void setAlineacionEstrategia(Estrategias alineacionEstrategia) {
        this.alineacionEstrategia = alineacionEstrategia;
        
        if(alineacionEstrategia == null)
            nulificarEstrategia();
    }
    
    private void nulificarEstrategia(){
        lineasAccion = Collections.EMPTY_LIST;
        nulificarLinea();
    }

    public void setAlineacionLinea(LineasAccion alineacionLinea) {
        this.alineacionLinea = alineacionLinea;
        
        if(alineacionLinea == null)
            nulificarLinea();
    }
    
    private void nulificarLinea(){
         actividades = Collections.EMPTY_LIST;
         nulificarActividad();
     }

    public void setAlineacionActividad(ActividadesPoa alineacionActividad) {
        this.alineacionActividad = alineacionActividad;
        
        if(alineacionActividad == null)
            nulificarActividad();
        else{
            registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getActividadesPoaList().add(alineacionActividad);
            this.alineacionActividad.getRegistrosList().add(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros());
        }
    }
    
    private void nulificarActividad(){
        if(registro != null) registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getActividadesPoaList().remove(alineacionActividad);
        
        if(alineacionActividad != null) alineacionActividad.getRegistrosList().remove(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros());
    }
    
    public void setActividades(List<ActividadesPoa> actividades) {
        this.actividades = actividades;
        if(actividades.isEmpty())
            nulificarActividad();
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
