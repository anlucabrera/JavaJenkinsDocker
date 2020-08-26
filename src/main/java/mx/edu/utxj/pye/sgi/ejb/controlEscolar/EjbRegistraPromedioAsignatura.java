package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadesCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Stateless
public class EjbRegistraPromedioAsignatura {
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbCapturaTareaIntegradora ejbCapturaTareaIntegradora;
    @EJB EjbPacker ejbPacker;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }

    public ResultadoEJB<List<DtoCargaAcademica>> obtenerCargasAcademasPorPeriodo(Integer periodo){
        try{
            List<@NonNull DtoCargaAcademica> dtoCargaAcademicas = em.createQuery("select c from CargaAcademica c inner join c.evento e where e.periodo=:periodo", CargaAcademica.class)
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .map(cargaAcademica -> ejbPacker.packCargaAcademica(cargaAcademica))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(dtoCargaAcademicas, "Lista de cargas académicas por periodo");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar obtener las cargas académicas por periodo (EjbRegistraPromedioAsignatura.obtenerCargasAcademasPorPeriodo).", e, null);
        }
    }

    /**
     * Permite calcular y registrar promedios por asignatura de forma masiva correspondiendo al periodo indicado en la variable de configuración periodoRegistroMasivoPromedios
     * @return Regresa un Point con la coordenada X indicando el total de promedios posibles y la coordenada Y indicando los que se pudieron registrar
     */
    public ResultadoEJB<Point> registrarMasivamentePromedios(){
        try{
            System.out.println("EjbRegistraPromedioAsignatura.registrarMasivamentePromedios");
            ResultadoEJB<Integer> verificarRegistro = verificarRegistro();
//            System.out.println("verificarRegistro = " + verificarRegistro);
            if(!verificarRegistro.getCorrecto()) return ResultadoEJB.crearErroneo(3, verificarRegistro.getMensaje(), Point.class);
            final List<ResultadoEJB<BigDecimal>> resultadoEJBS = new Vector<>();

            Integer periodoRegistroMasivoPromedios = ep.leerPropiedadEntera("periodoRegistroMasivoPromedios").orElse(52);
            ResultadoEJB<List<DtoCargaAcademica>> obtenerCargasAcademasPorPeriodo = obtenerCargasAcademasPorPeriodo(periodoRegistroMasivoPromedios);
            if(!obtenerCargasAcademasPorPeriodo.getCorrecto()){
                System.out.println("EjbRegistraPromedioAsignatura.registrarMasivamentePromedios");
                System.out.println("obtenerCargasAcademasPorPeriodo = " + obtenerCargasAcademasPorPeriodo);
                return ResultadoEJB.crearErroneo(2, "NO se pudo obtener las cargas por periodo: ".concat(obtenerCargasAcademasPorPeriodo.getMensaje()), Point.class);
            }

            List<DtoCargaAcademica> cargasCalculadas = new ArrayList<>();
            int generarPromediosRango = ep.leerPropiedadEntera("generarPromediosRango").orElse(1);
            @NonNull List<DtoCargaAcademica> dtoCargaAcademicas = obtenerCargasAcademasPorPeriodo.getValor().stream().filter(dtoCargaAcademica -> dtoCargaAcademica.getCargaAcademica().getCarga() >= verificarRegistro.getValor()).limit(generarPromediosRango).collect(Collectors.toList());
            dtoCargaAcademicas.stream().forEach(dtoCargaAcademica -> {
                System.out.println("dtoCargaAcademica " + cargasCalculadas.size() + " = " + dtoCargaAcademica);
                ResultadoEJB<List<DtoEstudiante>> packDtoEstudiantesGrupo = ejbPacker.packDtoEstudiantesHistoricoGrupo(dtoCargaAcademica);
                if(packDtoEstudiantesGrupo.getCorrecto()){
                    @NonNull List<DtoEstudiante> dtoEstudiantes = packDtoEstudiantesGrupo.getValor();
                    dtoEstudiantes.parallelStream().forEach(dtoEstudiante -> {
                        ResultadoEJB<List<DtoUnidadConfiguracion>> getConfiguraciones = ejbCapturaCalificaciones.getConfiguraciones(dtoCargaAcademica);
                        if(getConfiguraciones.getCorrecto()){
                            @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones = getConfiguraciones.getValor();
                            ResultadoEJB<DtoUnidadesCalificacion> packDtoUnidadesCalificacion = ejbPacker.packDtoUnidadesCalificacion(dtoCargaAcademica, dtoUnidadConfiguraciones, null);
                            if(packDtoUnidadesCalificacion.getCorrecto()){
                                @NonNull DtoUnidadesCalificacion dtoUnidadesCalificacion = packDtoUnidadesCalificacion.getValor();
                                ResultadoEJB<BigDecimal> promediarAsignatura = ejbCapturaTareaIntegradora.promediarAsignatura(dtoUnidadesCalificacion, dtoCargaAcademica, dtoEstudiante);
                                resultadoEJBS.add(promediarAsignatura);
                            }else System.out.println("Error: EjbRegistraPromedioAsignatura.registrarMasivamentePromedios proceso > packDtoUnidadesCalificacion = " + packDtoUnidadesCalificacion);
                        }else System.out.println("Error: EjbRegistraPromedioAsignatura.registrarMasivamentePromedios proceso > getConfiguraciones = " + getConfiguraciones);
                    });
                }else System.out.println("Error: EjbRegistraPromedioAsignatura.registrarMasivamentePromedios proceso > packDtoEstudiantesGrupo = " + packDtoEstudiantesGrupo);
                cargasCalculadas.add(dtoCargaAcademica);
            });
            Integer calculados = (int)resultadoEJBS.stream().filter(ResultadoEJB::getCorrecto).count();
            Point resultado = new Point(resultadoEJBS.size(), calculados);
            System.out.println("resultado = " + resultado);
            reportarRegistro();
            return ResultadoEJB.crearCorrecto(resultado, "Conteo de resultados");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar ejecutar de forma masiva el registro de promedios por asignatura (EjbRegistraPromedioAsignatura.registrarMasivamentePromedios).", e, Point.class);
        }
    }

    private ResultadoEJB<Boolean> reportarRegistro(){
        try{
            ConfiguracionPropiedades generarPromedios = em.find(ConfiguracionPropiedades.class, "generarPromedios");
            if(generarPromedios==null) return ResultadoEJB.crearErroneo(2, "No existe la clave -generarPromedios- en la tabla de configuracion de propiedades", Boolean.TYPE);

            generarPromedios.setValorEntero(0);
            em.merge(generarPromedios);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Reporte de registro de promedios realizado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar reportar que ya se realizó el registro masivo de promedios (EjbregistraPromedioAsignatura.reportarRegistro)", Boolean.TYPE);
        }
    }

    private ResultadoEJB<Integer> verificarRegistro(){
        try{
            ConfiguracionPropiedades generarPromedios = em.find(ConfiguracionPropiedades.class, "generarPromedios");
            if(generarPromedios==null) return ResultadoEJB.crearErroneo(2, "No existe la clave -generarPromedios- en la tabla de configuracion de propiedades", Integer.TYPE);

            if(generarPromedios.getValorEntero() <= 0) return ResultadoEJB.crearErroneo(3, "La variable de configuración -generarPromedios- debe tener un valor entero mayor que 0 para poder registrar de forma masiva los promedios.", Integer.TYPE);
            return ResultadoEJB.crearCorrecto(generarPromedios.getValorEntero(), "Se puede registrar de forma masiva los promedios");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar reportar que ya se realizó el registro masivo de promedios (EjbregistraPromedioAsignatura.reportarRegistro)", Integer.TYPE);
        }
    }
}
