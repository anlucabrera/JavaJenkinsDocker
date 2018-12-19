package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesPremiosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.enums.PremioTipo;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public class PremioCategoriaDto implements Serializable {
    @Getter @Setter @NonNull PremioTipo tipo;
    @Getter @Setter private Categorias categoria;
    @Getter @Setter private AreasUniversidad area;
    @Getter @Setter private Personal evaluador, evaluado;
    @Getter @Setter private Evaluaciones evaluacion;
    @Getter @Setter private EvaluacionesPremiosResultados resultado;
    @Getter @Setter private List<Personal> candidatos;
    @Getter @Setter private Boolean nuevo;
}
