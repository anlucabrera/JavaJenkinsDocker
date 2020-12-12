package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.itextpdf.text.DocumentException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.DtoReporteEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class EjbReporteResultadosEvaluaciones {
    @EJB
    EjbPersonalBean ejbPersonalBean;
    @EJB
    Facade f;
    @EJB
    EjbValidacionRol ejbValidacionRol;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }

    /**
     * Valida que el personal autenticado sea docente
     *
     * @param clave
     * @return
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave) {
        try {
            return ejbValidacionRol.validarDocente(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbReporteResultadosEvaluaciones.validarDocente", e, null);

        }
    }

    public ResultadoEJB<List<Evaluaciones>> getEvaluaciones() {
        try {
            List<Evaluaciones> ev = em.createQuery("select  e from Evaluaciones e  where e.tipo =:tipo1 or (e.tipo=:tipo2  or e.tipo=:tipo4 or e.tipo=:tipo5 or e.tipo=:tipo6) order by e.periodo desc ", Evaluaciones.class)
                    .setParameter("tipo1", EvaluacionesTipo.TUTOR.getLabel())
                    .setParameter("tipo2", EvaluacionesTipo.TUTOR_2.getLabel())
                    .setParameter("tipo4", EvaluacionesTipo.DOCENTE_2.getLabel())
                    .setParameter("tipo5", EvaluacionesTipo.DOCENTE_3.getLabel())
                    .setParameter("tipo6", EvaluacionesTipo.DOCENTE_4.getLabel())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(ev, "Lista de evaluaciones");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones. (EjbReporteResultadosEvaluaciones.getEvaluaciones)", e, null);

        }
    }

    public ResultadoEJB<PeriodosEscolares> getPeriodobyEvaluacion(@NonNull Evaluaciones ev) {
        try {
            if (ev == null) {
                return ResultadoEJB.crearErroneo(2, new PeriodosEscolares(), "El periodo no debe ser nulo");
            }
            PeriodosEscolares p = em.createQuery("select p from  PeriodosEscolares p where p.periodo=:periodo", PeriodosEscolares.class)
                    .setParameter("periodo", ev.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (p == null) {
                return ResultadoEJB.crearErroneo(3, p, "No se encontró el periodo de la evaluación");
            } else {
                return ResultadoEJB.crearCorrecto(p, "Periodo evaluacion");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones. (EjbReporteResultadosEvaluaciones.getEvaluaciones)", e, null);

        }
    }

    /**
     * @return
     */

    public ResultadoEJB<List<DtoReporteEvaluaciones>> getEvaluacionesPack() {
        try {
            //Obtiene la lista de evaluaciones
            List<DtoReporteEvaluaciones> listDto = new ArrayList<>();
            ResultadoEJB<List<Evaluaciones>> ev = getEvaluaciones();
            if (ev.getCorrecto()) {
                ev.getValor().stream().forEach(e -> {
                    DtoReporteEvaluaciones dto = new DtoReporteEvaluaciones(new Evaluaciones(), new PeriodosEscolares());
                    dto.setEvaluacion(e);
                    //System.out.println("Tipo Ev ->" + e.getTipo() + "Periodo " + e.getPeriodo());
                    ResultadoEJB<PeriodosEscolares> p = getPeriodobyEvaluacion(e);
                    if (p.getCorrecto()) {
                        //System.out.println("Obtuvo periodo " + p.getValor());
                        dto.setPeriodoEscolar(p.getValor());
                        listDto.add(dto);
                    } else {
                        return;
                    }
                });
                return ResultadoEJB.crearCorrecto(listDto, "Lista de evaluaciones");
            } else {
                return ResultadoEJB.crearErroneo(2, listDto, "Error al obtener la lista de evaluaciones");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones. (EjbReporteResultadosEvaluaciones.getEvaluaciones)", e, null);

        }
    }


}
