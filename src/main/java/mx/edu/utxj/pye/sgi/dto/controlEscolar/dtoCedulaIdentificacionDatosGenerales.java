/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class dtoCedulaIdentificacionDatosGenerales {
    /*Apartado a1= new Apartado(1f);
        a1.setContenido("Datos generales");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 1f, "Matricula:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 2f, "Nombre:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 3f, "Carrera:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 4f, "Sistema:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 5f, "Grado:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 5f, "Grupo:",""));
        l.add(a1);
    */
 @Getter @Setter private Integer p1;//Matricula
 @Getter @Setter private String p2;//Nombre completo
 @Getter @Setter private String p3;//Carrera
 @Getter @Setter private String p4;//Sistema
 @Getter @Setter private Integer p5;//Grado
 @Getter @Setter private String p6;//Grupo
 
    
}
