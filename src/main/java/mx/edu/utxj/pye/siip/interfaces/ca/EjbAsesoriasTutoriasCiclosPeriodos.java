/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAsesoriasTutoriasCiclosPeriodos {
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaAsesoriasTutorias(String rutaArchivo) throws Throwable;
    
    public void guardaAsesoriasTutorias(List<DTOAsesoriasTutoriasCicloPeriodos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public AsesoriasTutoriasCicloPeriodos getRegistroAsesoriaTutoriaCicloPeriodo(AsesoriasTutoriasCicloPeriodos asesoriaTutoriaCicloPeriodo);
    
    /**
     * Obtiene la lista de periodos con registros de tutorías y asesorías.
     * @return Lista de periodos.
     */
    public List<PeriodosEscolares> getPeriodosConregistro();
    
    /**
     * Obtiene la lista de eventos de registros correspondientes a un periodo escolar.
     * @param periodo Periodo escolar que filtra eventos de registro.
     * @return Lista de eventos de registro.
     */
    public List<EventosRegistros> getEventosPorPeriodo(PeriodosEscolares periodo);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @return Lista de registros mensuales.
     */
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaRegistrosPorEvento(EventosRegistros evento);
}
