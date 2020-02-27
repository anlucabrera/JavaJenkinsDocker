/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum NotificacionesTipo {
    REGISTRO("Registro",1d),
    CONVOCATORIA("Convocatoria",1d),
    RESULTADOS("Resultados",1d),
    EVALUACION("Evaluación",1d),
    ENCUESTA("Encuesta",1d),
    AVISO("Aviso",1d),
    BOLSA_TRABAJO("Bolsa de Trabajo",0d),
    EXAMEN("Examen",1d);
    @Getter @NonNull private String label;
    @Getter @NonNull private Double nivel;

    /**
     * Lista de tipos de avisos genéricos
     * @return Regresa la lista de tipos
     */
    public static List<NotificacionesTipo> ListaGenericos(){
        return Arrays.asList(NotificacionesTipo.values())
                .stream()
                .filter(notificacionTipo -> notificacionTipo.getNivel() > 0d)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista de tipos de avisos que no son genéricos
     * @return Regresa la lista de tipos
     */
    public static List<NotificacionesTipo> ListaEspecificos(){
        return Arrays.stream(NotificacionesTipo.values())
                .filter(notificacionTipo -> notificacionTipo.getNivel() < 1d)
                .collect(Collectors.toList());
    }
    
}
