package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;

import java.io.Serializable;

@RequiredArgsConstructor @EqualsAndHashCode @ToString
public class DtoEvaluacionParesAcademicos implements Serializable {

    @Getter @Setter private PersonalActivo evaluador,
                                            evaluado;

    @Getter @Setter private PersonalCategorias categoriaEvaluador,
                                               categoriaEvaluado;
    @Getter @Setter private EvaluacionParesAcademicos combinacion;
    @Getter @Setter private  Boolean editable;
    //@Getter @Setter private List<PersonalActivo> docentesAreaCat;


    public DtoEvaluacionParesAcademicos(PersonalActivo evaluador, PersonalActivo evaluado, PersonalCategorias categoriaEvaluador, PersonalCategorias categoriaEvaluado, EvaluacionParesAcademicos combinacion, Boolean editable) {
        this.evaluador = evaluador;
        this.evaluado = evaluado;
        this.categoriaEvaluador = categoriaEvaluador;
        this.categoriaEvaluado = categoriaEvaluado;
        this.combinacion = combinacion;
        this.editable = editable;
    }
}
