/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EjbAsistenciaTutoriaGrupalEstudiante {
    @EJB        EjbValidacionRol            ejbValidacionRol;
    @EJB        EjbPropiedades              ep;
    @EJB        EjbPeriodoEventoRegistro    ejbPeriodoEventoRegistro;
    @EJB        EjbRegistroAsesoriaTutoria  ejbRegistroAsesoriaTutoria;
    @EJB        Facade                      f;
    
    @Inject     Caster                      caster;
    
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Valida si el usuario logueado es estudiante
     * @param matricula
     * @return 
     */
    public ResultadoEJB<Estudiante> validarEstudiante(Integer matricula){
        try {
            
            return ejbValidacionRol.validarEstudiante(matricula);
            
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiate no se pudo validar. (EjbAsistenciaTutoriaGrupalEstudiante.validarEstudiante)", e, Estudiante.class);
        }
    }
    
    /**
     * Obtiene la lista de periodos escolares con base a la asistencia del estudiante en tutorías grupales
     * @param matricula
     * @return 
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresPorParticipacionTutoriaGrupal(Integer matricula){
        try {
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT p.estudiante1.periodo FROM ParticipantesTutoriaGrupal p WHERE p.estudiante1.matricula = :matricula", Integer.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if(!periodosEscolares.isEmpty()) return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares con base a la asistencia e inasistencia registrada en tutorías grupales");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "Lista de periodos escolares vacía debido a que no se ha encontrado ninguna asistencia registrada");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares (EjbAsistenciaTutoriaGrupalEstudiante.obtenerPeriodosEscolaresPorParticipacionTutoriaGrupal)", e, null);
        }
    }
    
    /**
     * Comprueba el evento actual y devuelve un mapa de periodos escolares con eventos de registro - Para uso en consulta de asistencia  de tutorías
     * @param periodos
     * @param eventos
     * @param eventoRegistroActivo
     * @param matricula
     * @return
     * @throws PeriodoEscolarNecesarioNoRegistradoException 
     */
    public ResultadoEJB<Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>>> comprobarEventoActualEstudiante(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoRegistroActivo, Integer matricula) throws PeriodoEscolarNecesarioNoRegistradoException {
        try {
            if(periodos == null || periodos.isEmpty()) periodos = obtenerPeriodosEscolaresPorParticipacionTutoriaGrupal(matricula).getValor();
            return ejbPeriodoEventoRegistro.comprobarEventoActualResultado(periodos, eventos, eventoRegistroActivo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de periodos escolares y eventos registros (EjbAsistenciaTutoriaGrupalEstudiante.comprobarEventoActualEstdudiante).", e, null);
        }
    }
    
    /**
     * Obtiene la lista de Eventos de Registro por periodo
     * @param periodo
     * @return 
     */
    public ResultadoEJB<List<EventosRegistros>> getEventosRegistroPorPeriodo(PeriodosEscolares periodo){
        return ejbPeriodoEventoRegistro.getEventosRegistroPorPeriodo(periodo);
    }
    
    public ResultadoEJB<List<ParticipantesTutoriaGrupal>> getListaParticipantesTutoriaGrupalPorEventoRegistro(EventosRegistros eventoRegistroSeleccionado, Integer matricula){
        try {
            List<ParticipantesTutoriaGrupal> lista = em.createQuery("SELECT p FROM ParticipantesTutoriaGrupal p INNER JOIN p.estudiante1 e INNER JOIN p.tutoriasGrupales t WHERE t.eventoRegistro = :eventoRegistro AND e.matricula = :matricula ORDER BY t.tutoriaGrupal DESC", ParticipantesTutoriaGrupal.class)
                    .setParameter("eventoRegistro", eventoRegistroSeleccionado.getEventoRegistro())
                    .setParameter("matricula", matricula)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(lista, "Lista de participaciones en tutorías grupales");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de participación en tutorías grupales (EjbAsistenciaTutoriaGrupalEstudiante.getListaParticipantesTutoriaGrupalPorEventoRegistro)", e, null);
        }
    }
    
    public ResultadoEJB<ParticipantesTutoriaGrupal> editarAsistenciaParticipacionTutoriaGrupal(ParticipantesTutoriaGrupal participantesTutoriaGrupal) {
        try {
            ParticipantesTutoriaGrupal ptg = new ParticipantesTutoriaGrupal();
            ptg = participantesTutoriaGrupal;
            if(!(ejbRegistroAsesoriaTutoria.buscaParticipanteTutoriaGrupal(ptg.getParticipantesTutoriaGrupalPK().getTutoriaGrupal(),ptg.getParticipantesTutoriaGrupalPK().getEstudiante()).getCorrecto())){
                return ResultadoEJB.crearErroneo(2, "El tutor aún no ha realizado el pase de lista correspondiente", ParticipantesTutoriaGrupal.class);
            }else{
                em.merge(ptg);
                return ResultadoEJB.crearCorrecto(ptg, "La participación se ha actualizado correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la participación de la tutoría grupal (EjbAsistenciaTutoriaGrupalEstudiante.validarAsistenciaTutoriaGrupal)", e, null);
        }
    }
    
}
