/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.cuestionario;

import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import edu.mx.utxj.pye.seut.util.util.Cuestionario;

/**
 *
 * @author UTXJ
 */
public class CuestionarioEvaluacionDesempenio extends Cuestionario{    
    private static final long serialVersionUID = -7195728564352546630L;

    public CuestionarioEvaluacionDesempenio() {
        Integer encuesta = 12;
        String opciones = "5=Excelente@,4=Muy bien@,3=Bueno@,2=Regular@,1=Deficiente";
        
        Float apartado = 1F;//Metas
        this.preguntas.add(new Opciones(encuesta, apartado, 1F, "Cumplimiento en los objetivos y metas institucionales, favoreciendo la productividad, eficiencia y eficacia en la ejecución de sus funciones.", opciones));
    
        apartado = 2F;//Criterio
        this.preguntas.add(new Opciones(encuesta, apartado, 2F, "Conocimiento y aplicación de las normas establecidas, que sustenten sus acciones y/o le permitan formular propuestas para favorecer el proceso institucional y personal de toma de decisiones.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 3F, "Acciones tomadas y/o soluciones planteadas ante circunstancias imprevistas, privilegiadas el enfoque hacia los mejores resultados.", opciones));
        
        apartado = 3F;//Liderazgo y dirección
        this.preguntas.add(new Opciones(encuesta, apartado, 4F, "Capacidad para coordinar y organizar actividades conjuntas, conduciendo acciones adecuadas en el manejo y motivación del personal; obteniendo los mejores resultados sobre el propósito dado.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 5F, "Capacidad para conducir y controlar el equipo de trabajo, mediante la integración e identificación de actividades colectivas; privilegiando la consecución de los objetivos comunes y llevando a los miembros del grupo hacia los logros superiores.", opciones));
        
        apartado = 4F;//Calidad del trabajo
        this.preguntas.add(new Opciones(encuesta, apartado, 6F, "Presentación, estructura, contenido, precisión, oportunidad y limpieza del trabajo encomendado para el desarrollo de sus propias funciones, así como las de sus compañeros y autoridades.", opciones));
        
        apartado = 5F;//Madurez y discreción
        this.preguntas.add(new Opciones(encuesta, apartado, 7F, "Capacidad para el manejo y uso de la información clasificada como confidencial, a la que tiene acceso por la naturaleza de su puesto; privilegiando la eficiencia en sus funciones, en un marco de madurez y prudencia.", opciones));
        
        apartado = 6F;//Iniciativa
        this.preguntas.add(new Opciones(encuesta, apartado, 8F, "Capacidad para la actuación espontánea, con una amplia disposición para aportar ideas innovadoras; que permitan mejorar su trabajo y el de sus demás compañeros dentro de la institución.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 9F, "Actitud para desempeñar sus funciones sin necesidad de supervisión estrecha, actuando siempre de manera constructiva y profesional; aunque no cuente con una dirección o seguimiento permanentes.", opciones));
        
        apartado = 7F;//Colaboración y compromiso
        this.preguntas.add(new Opciones(encuesta, apartado, 10F, "Interés, entusiasmo y disposición hacia las tareas que representan labores cotidianas, manifestando su involucramiento para ser parte de los esfuerzos institucionales; aun cuando éstas sean en otras áreas.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 11F, "Interés, entusiasmo y disposición hacia las tareas que representan labores complementarias, manifestado su involucramiento para ser parte de los esfuerzos adicionales que realiza la institución; aun cuando éstas sean honorarios no convencionales o pertenezcan a otras áreas distintas a la propia.", opciones));
        
        apartado = 8F;//Buen uso de los recursos
        this.preguntas.add(new Opciones(encuesta, apartado, 12F, "Capacidad para el conocimiento y manejo apropiado de la infraestructura, el equipamiento y los recursos materiales disponibles dentro de la institución; aun cuando estos pertenezcan a otras áreas distintas a la propia.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 13F, "Cuidado y uso racional de dichos recursos dentro de la institución privilegiando el mayor ahorro y la adecuada conservación o mantenimiento de los mismos.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 14F, "Capacidad en la iniciativa necesaria, para proponer el mayor aprovechamiento y mejor uso de los recursos dentro de la institución.", opciones));
        
        apartado = 9F;//Disciplina
        this.preguntas.add(new Opciones(encuesta, apartado, 15F, "Actitud dentro del área de trabajo, observancia hacia las normas y disposiciones institucionales, acatamiento de órdenes e instrucciones y respeto a las líneas jerárquicas.", opciones));
        
        apartado = 10F;//Trabajo colaborativo y en equipo
        this.preguntas.add(new Opciones(encuesta, apartado, 16F, "Integración y participación dentro del equipo de trabajo, mostrando compromiso en el alcance de los objetivos comunes; privilegiando el interés de la colectividad institucional.", opciones));
        
        apartado = 11F;//Relaciones interpersonales
        this.preguntas.add(new Opciones(encuesta, apartado, 17F, "Capacidad de desarrollar condiciones propicias en el ambiente laboral, mediante su comportamiento con los compañeros de trabajo y los usuarios del servicio proporcionado; colaborando con la construcción de una adecuada imagen  de la institución.", opciones));
        
        apartado = 12F;//Aspecto e imagen personal
        this.preguntas.add(new Opciones(encuesta, apartado, 18F, "Capacidad para el cuidado de su propia imagen y presentación personal; logrando que sea de manera adecuada y limpia, privilegiando una buena impresión visual.", opciones));
        
        apartado = 13F;//Capacitación
        this.preguntas.add(new Opciones(encuesta, apartado, 19F, "Disposición, interés e iniciativa para su anticipación en los programas de desarrollo previstos, para el mejoramiento tanto de sus propias tareas y responsabilidades; así como de su crecimiento personal y evolución profesional.", opciones));
        this.preguntas.add(new Opciones(encuesta, apartado, 20F, "Capacidad para compartir, ejecutar acciones y aplicar los conocimientos adquiridos en los programas de desarrollo previstos; que le permita potenciar la productividad, eficiencia y eficacia de su trabajo y el de sus propios compañeros.", opciones));
        
        apartado = 14F;//Comentarios y sugerencias sobre las áreas de oportunidad
        this.preguntas.add(new Abierta(encuesta, apartado, 21F, "Comentarios y sugerencias sobre las áreas de oportunidad", 5));
        establecerPosiciones();
    }
    
}
