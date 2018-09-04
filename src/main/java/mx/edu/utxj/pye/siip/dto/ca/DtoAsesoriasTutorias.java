package mx.edu.utxj.pye.siip.dto.ca;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.RegistroSiipEtapa;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
public final class DtoAsesoriasTutorias {   
    @Getter private EjesRegistro eje;
    @Getter private EventosRegistros eventoActual, eventoSeleccionado;
    @Getter private PeriodosEscolares periodo;    
    @Getter private RegistrosTipo registroTipo;
    
    @Getter private RegistroSiipEtapa etapa;
    
    @Getter private Short area;
    @Getter private String rutaArchivo;
    
    @Getter private List<EventosRegistros> eventosPorPeriodo;
    @Getter private List<DTOAsesoriasTutoriasCicloPeriodos> lista;
    @Getter private List<PeriodosEscolares> periodos;

    public DtoAsesoriasTutorias() {
        setRegistroTipo(new RegistrosTipo((short)3));
        setEje(new EjesRegistro(3));
        setEtapa(RegistroSiipEtapa.MOSTRAR);
    }   

    public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }

    public void setLista(List<DTOAsesoriasTutoriasCicloPeriodos> lista) {
        this.lista = lista;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodo == null){
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
        if(periodos!=null && !periodos.isEmpty()){            
            setPeriodo(getPeriodos().get(0));
        }
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
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
//            eventosPorPeriodo.forEach(e -> System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setEventosPorPeriodo() evento: " + e.getMes()));
            setEventoSeleccionado(eventosPorPeriodo.get(0));
            if(getEventosPorPeriodo().contains(getEventoActual())){
                setEventoSeleccionado(getEventoActual());
            }
        }
    }
    
    
}
