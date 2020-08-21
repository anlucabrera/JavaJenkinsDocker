package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controladores.ch.PersonalAdmin;
import mx.edu.utxj.pye.sgi.dto.ListaAlumnosEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.apache.poi.ss.formula.functions.Even;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Stateless (name = "EjbSeguimientoCitasSE" )
public class EjbSeguimientoCitasSE {
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @Inject PersonalAdmin admin;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }

    /**
     * Verifica que el personal logueado sea de Servicios Escolares
     *
     * @param clave Clave del trabajador logueado
     * @return Resultado del Proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaSE(Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbAdministracionEstudiantesSE.validaSE)", e, null);
        }

    }

    /**
     * Obtiene una lista de evetos registrados de tipo registro de citas
     *
     * @return Resultado Ejb
     */
    public ResultadoEJB<List<EventoEscolar>> getListaEventosCitas() {
        try {
            List<EventoEscolar> eventoEscolarList = new ArrayList<>();
            eventoEscolarList = em.createQuery("select e from EventoEscolar e where e.tipo=:tipo order by e.periodo desc", EventoEscolar.class)
                    .setParameter("tipo", EventoEscolarTipo.REGISTRO_CITAS.getLabel())
                    .getResultList()
            ;
            if (eventoEscolarList == null || eventoEscolarList.isEmpty()) {
                return ResultadoEJB.crearErroneo(3, eventoEscolarList, "No eventos de registros de citas");
            } else {
                return ResultadoEJB.crearCorrecto(eventoEscolarList, "Lista de eventos");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de eventos de registro de citas. (EjbSeguimientoCitasSE.getListaEventosCitas)", e, null);
        }
    }

    /**
     * Obtiene el ultimo evento de registro de citas aperturado
     *
     * @return Resultado Ejb
     */
    public ResultadoEJB<EventoEscolar> getUltimoEventoCitas() {
        try {
            EventoEscolar eventoEscolar = new EventoEscolar();
            eventoEscolar = em.createQuery("select e from EventoEscolar e where e.tipo=:tipo order by e.periodo desc", EventoEscolar.class)
                    .setParameter("tipo", EventoEscolarTipo.REGISTRO_CITAS.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (eventoEscolar == null) {
                return ResultadoEJB.crearErroneo(2, eventoEscolar, "No se encontro ningún evento de registro de citas");
            } else {
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento encontrado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener el último evento de registro de citas (EjbSeguimientoCitasSE.getUltimoEventoCitas)", e, null);
        }
    }

    /**
     *
     * @param procesosInscripcion
     * @return
     */
    public ResultadoEJB<TramitesEscolares> getTramitesbyEvento(@NonNull ProcesosInscripcion procesosInscripcion) {
        try {
            if (procesosInscripcion == null) {
                return ResultadoEJB.crearErroneo(2, new TramitesEscolares(), "El evento no debe ser nulo");
            }
            //Verifica tramite aperturado
            TramitesEscolares te= new TramitesEscolares();
            te = em.createQuery("select t from TramitesEscolares t where t.tipoTramite=:tipoT and t.periodo=:periodo and T.tipoPersona=:tipoPer", TramitesEscolares.class)
                    .setParameter("tipoT", TramiteEscolar.INSCRIPCION.getLabel())
                    .setParameter("periodo",procesosInscripcion.getIdPeriodo())
                    .setParameter("tipoPer", TipoPersonaTramite.ASPIRANTE.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(te==null ){return ResultadoEJB.crearErroneo(3,te,"No existen tramites para el evento seleccionado");}
            else {return ResultadoEJB.crearCorrecto(te,"Lista de tramiste escolares");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de tramites escolares (EjbSeguimientoCitasSE.getTramitesbyEvento)", e, null);

        }

    }
    /**
     * Permite obtener la lista de periodos escolares a elegir al realizar la asignación docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            //buscar lista de periodos escolares ordenados de forma descendente para que al elegir un periodo en la asignación docente aparezca primero el mas actual
            final List<PeriodosEscolares> periodos = em.createQuery("select ee from EventoEscolar ee where ee.tipo = :tipo order by ee.periodo desc", EventoEscolar.class)
                    .setParameter("tipo", EventoEscolarTipo.CARGA_ACADEMICA.getLabel())
                    .getResultStream()
                    .map(eventoEscolar -> em.find(PeriodosEscolares.class, eventoEscolar.getPeriodo()))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
//            System.out.println("periodos = " + periodos);
//            System.out.println("EventoEscolarTipo.CARGA_ACADEMICA.getLabel() = " + EventoEscolarTipo.CARGA_ACADEMICA.getLabel());
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionAcademica.getPeriodosDescendentes)", e, null);
        }
    }

    /**
     * Busca cita confirmada por aspirante
     * @param a aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<CitasAspirantes> getCitabyAspirante(@NonNull Aspirante a,@NonNull TramitesEscolares te) {
        try {
            if (a == null) {
                return ResultadoEJB.crearErroneo(2, new CitasAspirantes(), "El aspirante no debe ser nulo");
            }
            if (te == null) {
                return ResultadoEJB.crearErroneo(3, new CitasAspirantes(), "El aspirante no debe ser nulo");
            }
            //Se busca la cita
            CitasAspirantes c = new CitasAspirantes();
            c = em.createQuery("select c from CitasAspirantes c where c.citasAspirantesPK.idAspirante=:idAspirante and  c.citasAspirantesPK.idTramite=:idTramite", CitasAspirantes.class)
                    .setParameter("idAspirante", a.getIdAspirante())
                    .setParameter("idTramite", te.getIdTramite())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (c == null) {
                return ResultadoEJB.crearErroneo(4, c, "El aspirante no cuenta con cita programada para el proceso de inscripción");
            } else {
                return ResultadoEJB.crearCorrecto(c, "El estudiante cuenta con cita programada para su inscripción");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener la cita por aspirante(EjbSeguimientoCitasSE.getCitabyAspirante)", e, null);
        }
    }

    /**
     * Actualiza la cita del asspirante como atendido
     * @param c cita del aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<CitasAspirantes> updateCita (@NonNull CitasAspirantes c){
        try{
            if(c==null){return ResultadoEJB.crearErroneo(2,new CitasAspirantes(), "La cita no debe ser nula");}
            c.setStatus(EstatusCita.ATENDIDO.getLabel());
            em.merge(c);
            f.setEntityClass(CitasAspirantes.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(c,"Atendido");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al actualizar la cita del aspirante (EjbSeguimientoCitasSE.updateCita)", e, null);
        }
    }
}
