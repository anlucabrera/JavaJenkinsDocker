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
    AVISO("Aviso", 1d),
    ACTIVIDAD("Actividad", 1d),
    CAMPANA("Campaña", 1d),
    CAPACITACION("Capacitación", 1d),
    CEREMONIA("Ceremonia", 1d),
    CICLO_CONFERENCIAS("Ciclo de conferencias", 1d),
    CONCURSO("Concurso", 1d),
    CONCURSO_INSTITUCIONAL("Concurso Institucional", 1d),
    CONFERENCIA("Conferencia", 1d),
    CONGRESO("Congreso", 1d),
    CURSO("Curso", 1d),
    CURSO_CONFERENCIA("Curso / Conferencia", 1d),
    CURSO_TALLER("Curso / Taller", 1d),
    DIFUSION("Difusión", 1d),
    EVENTO("Evento", 1d),
    EVENTO_NACIONAL("Evento nacional", 1d),
    EXPOSICION("Exposición", 1d),
    PARTICIPACION("Participación", 1d),
    PLATICA("Plática", 1d),
    PLATICA_CONFERENCIA("Plática y/o Conferencia", 1d),
    PRESTACION("Prestación", 1d),
    REUNION("Reunión", 1d),
    SESION("Sesión", 1d),
    TALLER("Taller", 1d);
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
