package mx.edu.utxj.pye.sgi.dto.consulta;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor @EqualsAndHashCode(of = "evaluacion")
public class DtoSatisfaccionServiciosEncuesta implements Serializable {
    @Getter @Setter @NonNull private Evaluaciones evaluacion;
    @Getter @Setter @NonNull private PeriodosEscolares periodosEscolares;
    @Getter @Setter @NonNull private CiclosEscolares ciclosEscolares;
    @Getter @Setter @NonNull private DtoSatisfaccionServiciosCuestionario cuestionario;
    @Getter private List<DtoSatisfaccionServiciosEstudiante> satisfaccionServiciosEstudiantes;
//    @Getter @Setter private Map<Pregunta, Map<BigDecimal, Long>> preguntaConteoValoresMap = new HashMap<>();
    @Getter @Setter private List<Conteo> conteos = new ArrayList<>();
    @Getter @Setter private List<ConteoInstitucional> conteosInstitucional = new ArrayList<>();

    public void setSatisfaccionServiciosEstudiantes(List<DtoSatisfaccionServiciosEstudiante> satisfaccionServiciosEstudiantes) {
        this.satisfaccionServiciosEstudiantes = satisfaccionServiciosEstudiantes;
        /*if(satisfaccionServiciosEstudiantes == null) return;

        cuestionario.preguntas.stream().forEach(pregunta -> {
            *//*Map<BigDecimal, Long> conteo = satisfaccionServiciosEstudiantes
                    .stream()
                    .map(dtoSatisfaccionServiciosEstudiante -> dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap())
                    .filter(map -> map.containsKey(pregunta))
                    .map(map -> map.values())
                    .flatMap(bigDecimals -> bigDecimals.stream())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            preguntaConteoValoresMap.put(pregunta, conteo);*//*

            List<Dato> datos = satisfaccionServiciosEstudiantes
                    .parallelStream()
                    .map(dtoSatisfaccionServiciosEstudiante -> dtoSatisfaccionServiciosEstudiante.generarDatos())
                    .flatMap(datoes -> datoes.stream())
                    .collect(Collectors.toList());

            Map<Dato, Long> conteoMap = datos.parallelStream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            List<Conteo> conteos = conteoMap.entrySet()
                    .parallelStream()
                    .map(datoLongEntry -> new Conteo(new Dato(datoLongEntry.getKey().programa, datoLongEntry.getKey().pregunta, datoLongEntry.getKey().respuesta), datoLongEntry.getValue()))
                    .collect(Collectors.toList());
            conteos.forEach(conteo -> {
                if(this.conteos.contains(conteo)) {
//                    System.out.println("conteo = " + conteo);
                    Integer index = this.conteos.indexOf(conteo);
                    Conteo conteoEnLista = this.conteos.get(index);
                    this.conteos.get(index).setFrecuencia(conteoEnLista.getFrecuencia() + conteo.getFrecuencia());
                }else this.conteos.add(conteo);
            });

            List<ConteoInstitucional> conteosInstitucional = conteoMap.entrySet()
                    .stream()
                    .map(datoLongEntry -> new ConteoInstitucional(new DatoInstitucional(datoLongEntry.getKey().pregunta, datoLongEntry.getKey().respuesta), datoLongEntry.getValue()))
                    .collect(Collectors.toList());
            conteosInstitucional.forEach(conteo -> {
                if(this.conteosInstitucional.contains(conteo)) {
                    Integer index = this.conteosInstitucional.indexOf(conteo);
//                    System.out.println("conteoInstitucional 1= " + this.conteosInstitucional.get(index));
                    ConteoInstitucional conteoEnLista = this.conteosInstitucional.get(index);
                    this.conteosInstitucional.get(index).setFrecuencia(conteoEnLista.getFrecuencia() + conteo.getFrecuencia());
//                    System.out.println("conteoInstitucional 2= " + this.conteosInstitucional.get(index));
                }else this.conteosInstitucional.add(conteo);
            });
        });*/
    }

    public Long getFrecuencia(AreasUniversidad programa, Pregunta pregunta, BigDecimal respuesta){
        if(conteos == null) return 0l;
        return conteos
                .stream()
                .filter(conteo -> Objects.equals(conteo.getPk().getPrograma(), programa))
                .filter(conteo -> Objects.equals(conteo.getPk().getPregunta(), pregunta))
                .filter(conteo -> Objects.equals(conteo.getPk().getRespuesta(), respuesta))
                .map(Conteo::getFrecuencia)
                .findFirst()
                .orElse(0l);
    }

    public Long getFrecuencia(Pregunta pregunta, BigDecimal respuesta){
        if(conteosInstitucional == null) return 0l;
        return conteosInstitucional
                .stream()
                .filter(conteo -> Objects.equals(conteo.getPk().getPregunta(), pregunta))
                .filter(conteo -> Objects.equals(conteo.getPk().getRespuesta(), respuesta))
                .map(ConteoInstitucional::getFrecuencia)
                .findFirst()
                .orElse(0l);
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class Dato{
        @Getter @Setter @NonNull private AreasUniversidad programa;
        @Getter @Setter @NonNull private Pregunta pregunta;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class DatoInstitucional{
        @Getter @Setter @NonNull private Pregunta pregunta;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class Conteo{
        @Getter @Setter @NonNull private Dato pk;
        @Getter @Setter private Long frecuencia;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class ConteoInstitucional {
        @Getter @Setter @NonNull private DatoInstitucional pk;
        @Getter @Setter private Long frecuencia;
    }
}
