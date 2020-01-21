package mx.edu.utxj.pye.sgi.ejb.consulta;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.*;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EjbSatisfaccionServiciosConsulta {
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEncuestaServicios ejbEncuestaServicios;
    @EJB EjbPacker ejbPacker;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado(@NonNull Evaluaciones evaluacion){
        try{
            System.out.println("EjbSatisfaccionServiciosConsulta.calcularConcentrado");
            System.out.println("evaluacion = " + evaluacion);
            Evaluaciones evaluacionBD = em.find(Evaluaciones.class, evaluacion.getEvaluacion());
            System.out.println("evaluacionBD = " + evaluacionBD);
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, evaluacion.getPeriodo());
            System.out.println("periodo = " + periodo);
            List<EncuestaServiciosResultados> encuestaServiciosResultados = em.createQuery("select r from EncuestaServiciosResultados r inner join r.evaluaciones e where r.evaluaciones=:evaluacion ", EncuestaServiciosResultados.class)
                    .setParameter("evaluacion", evaluacionBD)
                    .getResultList();
            System.out.println("encuestaServiciosResultados = " + encuestaServiciosResultados.size());

            ResultadoEJB<DtoSatisfaccionServiciosCuestionario> generarCuestionario = generarCuestionario();
            System.out.println("generarCuestionario = " + generarCuestionario);
            if(!generarCuestionario.getCorrecto()) {
                System.out.println("EjbSatisfaccionServiciosConsulta.calcularSatisfaccionPorPreguntaAreaOPrograma");
                System.out.println("evaluacion = " + evaluacionBD);
                System.out.println("generarCuestionario.getMensaje() = " + generarCuestionario.getMensaje());
                return ResultadoEJB.crearErroneo(2, "No se pudo generar el cuestionario. ".concat(generarCuestionario.getMensaje()), DtoSatisfaccionServiciosEncuesta.class);
            }

            List<@NonNull DtoSatisfaccionServiciosEstudiante> dtoSatisfaccionServiciosEstudiantes = encuestaServiciosResultados
                    .stream()
                    .map(resultados -> ejbPacker.packDtoSatisfaccionServiciosEstudiante(resultados, evaluacion, generarCuestionario.getValor()))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            System.out.println("dtoSatisfaccionServiciosEstudiantes = " + dtoSatisfaccionServiciosEstudiantes.size());

            DtoSatisfaccionServiciosEncuesta dtoSatisfaccionServiciosEncuesta = new DtoSatisfaccionServiciosEncuesta(evaluacionBD, periodo, periodo.getCiclo(), generarCuestionario.getValor());
            System.out.println("dtoSatisfaccionServiciosEncuesta = " + dtoSatisfaccionServiciosEncuesta);
            dtoSatisfaccionServiciosEncuesta.setSatisfaccionServiciosEstudiantes(dtoSatisfaccionServiciosEstudiantes);
            return ResultadoEJB.crearCorrecto(dtoSatisfaccionServiciosEncuesta, "Concentrado de encuestas de satisfacción de servicios realizado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar crear el concentraro de encuesta de satisfacción de servicios (EjbSatisfaccionServiciosConsulta.calcularConcentrado).", e, DtoSatisfaccionServiciosEncuesta.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosCuestionario> generarCuestionario(){
        try{
            List<Apartado> apartados = ejbEncuestaServicios.getApartados();
            DtoSatisfaccionServiciosCuestionario cuestionario = new DtoSatisfaccionServiciosCuestionario(apartados);
            cuestionario.getApartados().forEach(apartado -> cuestionario.preguntas.addAll(apartado.getPreguntas()));
            cuestionario.ordenar();
            return ResultadoEJB.crearCorrecto(cuestionario, "Cuestionario generado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al generar el cuestionario para satisfacción de servicios (EjbSatisfaccionServiciosConsulta.generarCuestionario).", e, DtoSatisfaccionServiciosCuestionario.class);
        }
    }

    public ResultadoEJB<List<Evaluaciones>> buscarEvaluaciones(){
        try{
            //
            List<Evaluaciones> evaluaciones = em.createQuery("select e from Evaluaciones e where e.tipo=:tipo order by e.periodo desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.SERVICIOS.getLabel())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(evaluaciones, "Lista de evaluaciones");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, null);
        }
    }
}
