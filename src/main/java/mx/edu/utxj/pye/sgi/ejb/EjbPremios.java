package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.PremioCategoriaDto;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesPremiosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesPremiosResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.PremioTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class EjbPremios implements Serializable {
    @EJB Facade f;

    /**
     * Obtiene la evaluación de premios activa o resultado nulo en caso de no existir
     * @return Intancia de la evaluación o null si no está activa
     */
    public Evaluaciones getEvaluacionActiva(){
        Evaluaciones evaluacion = f.getEntityManager().createQuery("select e from Evaluaciones e where current_date between e.fechaInicio and e.fechaFin order by e.evaluacion desc", Evaluaciones.class)
                .getResultStream()
                .findFirst()
                .orElse(null);
        //System.out.println("evaluacion = " + evaluacion);
        return evaluacion;
    }

    /**
     * Inicializa el dto que contiene los datos de la premiación y categoría, si ya votó carga el resultado anterior, de lo contrario genera una nueva instancia
     * @param evaluador Personal que se logueó
     * @return Resultado del proceso con el DTO sin el evaluado y como marca de nuevo
     */
    public ResultadoEJB<PremioCategoriaDto> categorizar(Personal evaluador){
        try{
            AreasUniversidad areaEvaluador = f.getEntityManager().find(AreasUniversidad.class, evaluador.getAreaOperativa());
            PremioTipo tipo;
            if(evaluaDocentes(evaluador, areaEvaluador)){
                tipo = PremioTipo.ACADEMIA;
            }else if(evaluaSecretarias(evaluador)){
                tipo = PremioTipo.SECRETARIAL;
            } else{
                tipo = PremioTipo.ADMINISTRATIVO;
            }
            PremioCategoriaDto dto = new PremioCategoriaDto(tipo);
            dto.setArea(areaEvaluador);
            dto.setEvaluador(evaluador);
            dto.setCandidatos(getCandidatosAEvaluar(dto));
            dto.setCategoria(areaEvaluador.getCategoria());
            dto.setEvaluacion(getEvaluacionActiva());
            dto.setNuevo(true);
            EvaluacionesPremiosResultados resultado = getResultado(dto.getEvaluacion(), dto.getEvaluador());
            if(resultado != null) {
                dto.setNuevo(false);
                dto.setEvaluado(resultado.getPersonal());
                dto.setResultado(resultado);
            }
            //dto.setResultado(getResultado(dto.getEvaluacion(), dto.getEvaluador(), dto.getEvaluado()));
            return ResultadoEJB.crearCorrecto(dto, "Categorización correcta");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al categorizar.", e, PremioCategoriaDto.class);
        }
    }

    /**
     * Verifica si existe el resultado de la evaluación en caso contrario
     * @param evaluacion
     * @param evaluador
     * @return
     */
    public EvaluacionesPremiosResultados getResultado(Evaluaciones evaluacion, Personal evaluador){
        return f.getEntityManager().createQuery("select r from EvaluacionesPremiosResultados r where r.evaluaciones.evaluacion=:evaluacion and r.personal1.clave=:evaluador", EvaluacionesPremiosResultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .setParameter("evaluador", evaluador.getClave())
                .getResultStream()
                .findAny()
                .orElse(null);
    }

    /**
     * Verifica si de acuerdo a su categría operativa y área debe evaluar a los docentes
     * @param evaluador Instancia del evaluador para obtener su categoría operativa
     * @param areaOperativa Instancia del area operativa del evaluador para tederminar su posición en el organigrama
     * @return TRUE si evalua a docentes, FALSE de lo contrario
     */
    public Boolean evaluaDocentes(Personal evaluador, AreasUniversidad areaOperativa){
        //System.out.println("evaluador = [" + evaluador + "], areaOperativa = [" + areaOperativa + "]");
        //System.out.println("(!Stream.of(8, 9).map(i -> i.shortValue()).collect(Collectors.toList()).contains(areaOperativa.getCategoria().getCategoria())) = " + (!Stream.of(8, 9).map(i -> i.shortValue()).collect(Collectors.toList()).contains(areaOperativa.getCategoria().getCategoria())));
        if(!(Stream.of(8, 9).map(i -> i.shortValue())
                .collect(Collectors.toList())
                .contains(areaOperativa.getCategoria().getCategoria()) || evaluador.getAreaOperativa() == 23)) return false; //debe pertener a área academica o programa educativo

        //System.out.println("if(!Stream.of(14, 18, 30, 32, 41).map(i -> i.shortValue()).collect(Collectors.toList()).contains(evaluador.getCategoriaOperativa().getCategoria())) = " + (!Stream.of(14, 18, 30, 32, 41).map(i -> i.shortValue()).collect(Collectors.toList()).contains(evaluador.getCategoriaOperativa().getCategoria())));
        if(!Stream.of(14, 18, 30, 32, 41).map(i -> i.shortValue()).collect(Collectors.toList()).contains(evaluador.getCategoriaOperativa().getCategoria())) return false; //debe ser coordinador de idiomas o director de carrera o PA o PTC o LAB

        return true;
    }

    /**
     * Verifica si de acuerdo a su categoría operativa debe evaluar a secretarias
     * @param evaluador Instancia del evaluador para obtener su categoría operativa
     * @return TRUE si evalua a secretarias, FALSE de lo contrario
     */
    public Boolean evaluaSecretarias(Personal evaluador){
        return Stream.of(34, 35, 36, 37).map(i -> i.shortValue()).collect(Collectors.toList()).contains(evaluador.getCategoriaOperativa().getCategoria());
    }

    /**
     * Genera la lista de los candidatos a elegir segun la categoría del premio y el área del evaluador
     * @param dto DTO que contiene los datos del evaluador y del tipo del premio, el dto ya debe contener al evaluador, tipo de premio y area operativa del evaluador
     * @return Lista de canditados a elegir
     */
    public List<Personal> getCandidatosAEvaluar(PremioCategoriaDto dto){
        List<Short> areaCategoriasAcademicas = Stream.of(8, 9).map(i -> i.shortValue()).collect(Collectors.toList());
        List<Short> categoriasDocentes = Stream.of(14, 18, 30, 32, 41).map(i -> i.shortValue()).collect(Collectors.toList());
        List<Short> categoriasSecretarias = Stream.of(34, 35, 36, 37, 38).map(i -> i.shortValue()).collect(Collectors.toList());

        List<Personal> l = f.getEntityManager().createQuery("select p from Personal  p where p.status <> 'B' and p.clave<>:clave order by p.clave", Personal.class)
                .setParameter("clave", dto.getEvaluador().getClave())
                .getResultStream()
                .collect(Collectors.toList());
        switch (dto.getTipo()){
            case ACADEMIA:
                if (dto.getEvaluador().getAreaOperativa() == 23) {
                    l = l.stream()
                            .filter(p -> {
                                if (dto.getEvaluador().getAreaOperativa() == 23) return p.getAreaOperativa() == 23;
                                else return true;
                            })
                            .collect(Collectors.toList());
                    break;
                }
                l = l.stream()
                        .filter(p -> {
                            if (dto.getEvaluador().getAreaOperativa() == 23) return p.getAreaOperativa() == 23;
                            else return true;
                        })
                        .filter(p -> {
                            AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, p.getAreaOperativa());
                            AreasUniversidad areaSuperior = f.getEntityManager().find(AreasUniversidad.class, p.getAreaOperativa());
                            return evaluaDocentes(p, area) && compartenArea(dto.getEvaluador(), p) && p.getAreaOperativa() != 23;
                        })
                        .collect(Collectors.toList());
                break;
            case SECRETARIAL:
                l = l.stream()
                        .filter(p -> evaluaSecretarias(p))
                        .collect(Collectors.toList());
                break;
            case  ADMINISTRATIVO:
                l = l.stream()
                        .filter(p -> {
                            AreasUniversidad area = f.getEntityManager().find(AreasUniversidad.class, p.getAreaOperativa());
                            return filtrarAdministrativo(p, area);
                        })
                        .collect(Collectors.toList());
                break;
        }

        return l;
    }

    private Boolean filtrarAdministrativo(Personal p, AreasUniversidad area){
        return !(evaluaDocentes(p, area) || evaluaSecretarias(p));
    }

    public Boolean compartenArea(Personal evaluador, Personal evaluado){
        if(evaluador.getAreaOperativa() == evaluado.getAreaOperativa() && evaluado.getAreaOperativa() == 23) return true; //si es de idiomas
        AreasUniversidad areaEvaluador = f.getEntityManager().find(AreasUniversidad.class, evaluador.getAreaOficial());
        AreasUniversidad areaEvaluado = f.getEntityManager().find(AreasUniversidad.class, evaluado.getAreaOficial());
        return  areaEvaluador.getArea() == areaEvaluado.getArea();
    }

    /**
     * Guarda los resultados de la premiciación
     * @param dto DTO que contiene los datos de la categoría del premio
     * @return  Resultado del proceso
     */
    public ResultadoEJB<PremioCategoriaDto> guardarResultado(PremioCategoriaDto dto){
        System.out.println("dto.getEvaluado() = " + dto.getEvaluado());
        try{
            EvaluacionesPremiosResultados resultado = getResultado(dto.getEvaluacion(), dto.getEvaluador());
            if(resultado!= null)
                f.remove(resultado);
            /*if(resultado != null) {
                dto.setResultado(resultado);
                dto.getResultado().setPersonal(dto.getEvaluado());
                dto.getResultado().setTipo(dto.getTipo().getLabel());
                dto.getResultado().getEvaluacionesPremiosResultadosPK().setEvaluado(dto.getEvaluado().getClave());
                System.out.println("dto.getResultado() = " + dto.getResultado());
                f.edit(dto.getResultado());
            }
            else {*/
                dto.setResultado(new EvaluacionesPremiosResultados(new EvaluacionesPremiosResultadosPK(dto.getEvaluacion().getEvaluacion(), dto.getEvaluador().getClave(), dto.getEvaluado().getClave())));
                dto.getResultado().setTipo(dto.getTipo().getLabel());
                f.create(dto.getResultado());
            //}

            ResultadoEJB<PremioCategoriaDto> res = ResultadoEJB.crearCorrecto(dto, "El resultado se ha guardado correctamente");
            return res;
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "Ocurrió un error al guardar el resultado.", e, PremioCategoriaDto.class);
        }
    }
}
