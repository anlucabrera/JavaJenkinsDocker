/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestVocacional;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorTestVocacional;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbTestVocacional")
public class EjbTestVocacional {
    @EJB                EjbValidacionRol                                        ejbValidacionRol;
    @EJB                Facade                                                  f;
    @EJB                EjbPacker                                               pack;
    @EJB                mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean                 ejbPersonalBean;
    private             EntityManager                                           em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    
    /**
     * Valida si el usuario es identificado como estudiante al iniciar sesión
     * Hace relación con el EJB EjbValidacionRol para evitar la duplicidad de código, se relaciona mediante este EJB para identificar el módulo en caso de error.
     * @param matricula
     * @return 
     */
    public ResultadoEJB<Estudiante> validarEstudiante(String matricula){
        try {
            return ejbValidacionRol.validarEstudiante(matricula);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbTestVocacional.validarEstudiante)", e, Estudiante.class);
        }
    }
    
    /////////////////////////////////////////////////// PREGUNTAS ///////////////////////////////////////////////////////////////////////////
    /**
     * Obtiene las preguntas del cuestionario
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Apartado>> apartados(){
        try {
            List<Apartado> a = new ArrayList<>();
            Apartado a1 = new Apartado(1f);
            a1.setContenido("Test Vocacional");
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 1f, "Tengo interés por aprender a programar aplicaciones de software utilizando lógica de programación y lenguajes específicos, para resolver problemas determinados.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 2f, "Me agrada la idea de aprender a manejar la energía en pequeña escala y por medio de ésta hacer que funciones máquinas y aparatos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 3f, "Me llama la atención el manejo de la industria o fábrica.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 4f, "Tengo el interés por desarrollar los elementos multimedia con herramientas de Hardware y Software especializadas para cumplir con los objetivos y metas del proyecto.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 5f, "Siempre he querido aprender las técnicas culinarias en la preparación de cocina nacional e internacional.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 6f, "Me gustaría tener los conocimientos específicos que puedan eliminar el dolor por contracturas o situaciones musculares.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 7f, "Me gustaría transformar materias primas, mediante el uso de la tecnología pertinente, para proporcionar valor agregado.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 8f, "Siempre he tenido habilidades para manejar y administrar el dinero.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 9f, "Desearía aplicar la tecnología con base en la biología para crear algún producto.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 10f, "Me llama la atención el control de pozos y ductos petroleros.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 11f, "Tengo especial interés por las ciencias agronómicas, ambientales y ecológicas.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 12f, "Me gustaría  desarrollar Bases de datos relacionales mediante un análisis de la información y acorde a las necesidades de la organización para administrar la información.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 13f, "Me gustaría aprender a controlar máquinas o robots para que realicen una tarea.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 14f, "Me gustaría aprender a manejar herramientas y maquinaria pesada.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 15f, "Sería interesante desarrollar y gestionar un sitio de comercio electrónico a través de aplicaciones web para contribuir al desarrollo  de la organización.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 16f, "Quisiera aprender a desarrollar nuevos diseños de la presentación y combinación de los alimentos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 17f, "Tengo el interés por estudiar el área de la salud de manera distinta a la medicina o la enfermería", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 18f, "Tengo el interés por aprender diferentes técnicas para conservar y transformar productos alimenticios.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 19f, "Tengo el interés por evaluar la información financiera integral utilizando técnicas de registro contable y métodos de análisis financiero para la toma de decisiones.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 20f, "Tengo el interés por los procesos biotecnológicos orientado a la biotecnología en el sector salud, agropecuario, farmacéutico, ambiental y alimentario.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 21f, "Tengo el interés por saber cómo es que un motor hace funcionar una máquina.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 22f, "Me gustaría planear el proceso de producción agrícola sustentable con base en la evaluación de los recursos disponibles y las condiciones ambientales, para realizar una propuesta tecnológica.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 23f, "Quisiera desarrollar un sitio web ligado a una base de datos con herramientas de diseño web, un lenguaje de programación y un manejador de base de datos para publicar, recopilar y consultar información general de la organización y los usuarios.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 24f, "Me llama la atención el cómo funcionan las puertas que pueden abrirse solas.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 25f, "Quiero saber cómo es que un motor hace funcionar una máquina.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 26f, "Me gustaría crear aplicaciones Multimedia, mediante herramientas informáticas, considerando los requerimientos establecidos por el cliente.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 27f, "Me gustaría trabajar en un restaurant o negocio que ofrezca servicios de alimentos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 28f, "Me gusta la actividad física y el deporte.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 29f, "Me gustaría estudiar la composición química de un material, muestra o producto alimenticio.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 30f, "Me gustaría administrar el capital humano, mediante los procesos de planeación, reclutamiento, selección, desarrollo y evaluación del factor humano.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 31f, "Considero que es importante poner una solución a la contaminación ambiental ocasionada por los residuos industriales.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 32f, "Quisiera saber cómo funciona una plataforma marítima o un pozo petrolero.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 33f, "Tengo conocimientos básicos en las ciencias básicas de matemáticas, física y biología.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 34f, "Me gustaría implementar sistemas de información de calidad, a través de técnicas avanzadas de desarrollo de software para eficientar los procesos de las organizaciones.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 35f, "Quiero aprender a guardar en la memoria de una máquina órdenes para mejorar la productividad de una empresa.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 36f, "Me agrada todo lo que tenga que ver con mecánica.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 37f, "Sería interesante formular un plan de negocios de comercio electrónico con base en las características de los productos y servicios a comercializar para definir una estrategia de modelo de negocio.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 38f, "Poseo habilidades manuales e intelectuales para el uso de la tecnología que requiere el manejo de alimentos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 39f, "Siento deseos por ayudar a los demás de manera desinteresada.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 40f, "Es interesante conocer las formas en que se puede aumentar la calidad de un producto comestible.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 41f, "Me agradaría saber planificar, organizar, dirigir, coordinar y controlar las actividades de una empresa u organización.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 42f, "Tengo los conocimientos básicos en Biología, Física, Matemáticas, Química y Estadística.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 43f, "Quiero aprender a darle mantenimiento a máquinas que se utilizan en empresas petroleras.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 44f, "Sería interesante dirigir el sistema de producción agrícola mediante la planeación y supervisión del manejo agronómico y la normatividad aplicable, para lograr las metas de producción.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 45f, "Administrar sistemas operativos: Windows y basados en Unix, de acuerdo a las necesidades de la organización, para el óptimo funcionamiento de los recursos informáticos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 46f, "Me interesa saber cómo es que llegan las señales de radio y televisión.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 47f, "Me gustaría saber cómo funciona la maquinaria de una embotelladora.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 48f, "Tengo deseo de integrar la propuesta del proyecto del sitio de comercio electrónico, considerando los requerimientos técnicos, el alcance del proyecto y los costos para su aprobación por parte del cliente.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 49f, "Me gustaría aprender e interpretar las lenguas extranjeras bajo el contexto culinario.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 50f, "Me gustaría ayudar a personas con capacidades diferentes.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 51f, "Me gustaría saber cómo es el proceso de fabricación de un producto alimenticio como los lácteos, los refrescos, los vinos, etc.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 52f, "Considero de vital importancia el trabajo que aportan los empleados y colaboradores de una organización.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 53f, "Me parecería importante, diseñar o estudiar organismos para crear antibióticos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 54f, "Me gustaría diseñar y crear una máquina que mejore el trabajo en la búsqueda y extracción del petróleo.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 55f, "Quiero mostrar el interés por el cuidado, protección y conservación del medio ambiente.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 56f, "Tengo las competencias necesarias para satisfacer los requerimientos tecnológicos de las organizaciones y las personas, utilizando la computación como principal herramienta.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 57f, "Tengo los conocimientos para realizar cálculos para saber la fuerza, presión o velocidad con la cual debe moverse un mecanismo.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 58f, "Me gustaría diseñar y crear una máquina que mejore el trabajo de una fábrica.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 59f, "Tengo interés en poder implementar un sitio de comercio electrónico considerando la estructura, el diseño creativo y funcional, de un proyecto.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 60f, "Tengo la capacidad para ofrecer un servicio de alimentos y bebidas de excelencia culinaria.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 61f, "Me gustaría dirigir el tratamiento terapéutico de un paciente a través de técnicas de masoterapia y electroterapia, y llevar el seguimiento de su evolución.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 62f, "Tengo el interés por estudiar a los microorganismos que pueden ayudar o dañar la producción de algún producto.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 63f, "Me gustaría analizar las necesidades de la gente para poder lanzar un producto.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 64f, "Sería una satisfacción poder apoyar con mi conocimiento al desarrollo de vacunas más seguras y nuevos medicamentos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 65f, "Sería interesante supervisar el manejo y mantenimiento de la maquinaria de una plataforma o pozo para dar seguridad a los trabajadores", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 66f, "Siempre he querido desarrollar sistemas de producción agrícola protegida determinando la infraestructura y el sistema de automatización acorde a las necesidades técnicas y a los recursos disponibles.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 67f, "Me interesaría implementar redes locales de acuerdo a estándares que garanticen su operatividad; para compartir recursos de tecnologías de información.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 68f, "Me gusta investigar la forma en que funcionan los pequeños dispositivos que se encuentran en celulares, computadoras, televisores, ipod, cámara de video y demás tarjetas verdes dentro de los aparatos eléctricos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 69f, "Sería interesante supervisar el manejo y mantenimiento de las máquinas de una empresa o fabrica para dar seguridad a los trabajadores.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 70f, "Poseo habilidades para diseñar, programar  y administrar  sitios web y de comercio electrónico.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 71f, "Me agradaría aprender técnicas para la elaboración de panadería, pastelería y repostería.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 72f, "Me gusta convivir con las personas a mi alrededor independientemente si tienen o no una discapacidad física notoria.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 73f, "Tengo el interés por saber cómo se transforma un producto alimenticio por ejemplo, el pan, desde que se siembra hasta que se consume.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 74f, "Quisiera intervenir en el intercambio de un producto, bien o servicio entre dos países.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 75f, "Sería de gran ayuda crear productos que consuman menos energía y produzcan menos desechos en su producción.    ", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 76f, "Quiero aprender a elaborar piezas que son indispensables en el buen funcionamiento de maquinaria del rubro petrolero.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 77f, "Me gustaría poseer la creatividad e ingenio, así como un razonamiento crítico-analítico para la innovación en soluciones de problemas reales del sector agrícola.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 78f, "Quisiera implementar y administrar sistemas manejadores de bases de datos acorde a los requerimientos de información de la organización.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 79f, "Quisiera aprender a utilizar diferentes tipos de sensores ya sea de movimiento o de temperatura para realizar alarmas o cualquier otro tipo de dispositivo de seguridad.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 80f, "Sería interesante elaborar piezas que son indispensables en el buen funcionamiento de una máquina.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 81f, "Me gustaría integrar la aplicación multimedia programando los componentes gráficos, de vídeo, audio, interactividad y animación, para concluir el proyecto.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 82f, "Considero importante establecer procedimientos, políticas y controles de las operaciones de los diferentes servicios de alimentos y bebidas.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 83f, "Me gustaría tener la habilidad de rehabilitar y reintegrar a una persona con discapacidad a sus actividades habituales, para contribuir a mejorar su calidad de vida.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 84f, "Considero que la tecnología es importante para mejorar la producción y calidad de todo lo que comemos.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 85f, "Me complacería poder asesorar a las personas que quieren poner una empresa.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 86f, "Me gustaría aprender a manipular o utilizar organismos para limpiar un sitio contaminado.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 87f, "Me gustaría aprender a manejar herramientas de perforación y maquinaria pesada.", ""));
            a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_VOCACIONAL.getNumero(), 1f, 88f, "Me gustaría aprender a dirigir la operación de unidades de producción agrícola protegida a través del control de variables y parámetros de operatividad y mantenimiento.", ""));
            a.add(a1);
            return ResultadoEJB.crearCorrecto(a, "Se generarón las preguntas correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron generar las preguntas (EjbTestVocacional.apartados)", e, null);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /////////////////////////////////////////////// Posibles respuestas ////////////////////////////////////////////////////////////////
    
    public ResultadoEJB<List<SelectItem>> respuestas(){
        try {
            List<SelectItem> l = new ArrayList<>();
            l.add(new SelectItem("5","D","Demasiado"));
            l.add(new SelectItem("4","M","Mucho"));
            l.add(new SelectItem("3","R","Regular"));
            l.add(new SelectItem("2","P","Poco"));
            l.add(new SelectItem("1","N","Nada"));
            return ResultadoEJB.crearCorrecto(l, "");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron generar las respuestas (EjbTestVocacional.respuestas)", e, null);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public ResultadoEJB<Boolean> consultarTerminado(Persona personaEstudiante){
//        try {
//            
//        } catch (Exception e) {
//            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar el registro del test vocacional del estudiante (EjbTestVocacional.consultarTerminado)", e, null);
//        }
//    }
//    
    /**
     * Obtiene los resultado guardados del test vocacional por persona de tipo estudiante
     * En caso de no existir se crea un registro nuevo
     * @param personaEstudiante Se requiere para obtener el id de la personal del estudiante
     * @return 
     */
    public ResultadoEJB<TestVocacional> getResultados (DtoEstudiante personaEstudiante, Map<String, String> respuestas){
        try {
            if(personaEstudiante == null) return ResultadoEJB.crearErroneo(2, new TestVocacional(), "El estudiante no debe ser nulo");
            if(personaEstudiante.getPersona().getIdpersona() == null) ResultadoEJB.crearErroneo(2, new TestVocacional(), "La clave persona del estudiante no debe ser nula");
            TestVocacional resultados = new TestVocacional();
            resultados = em.createQuery("SELECT t FROM TestVocacional t WHERE t.idPersona = :idPersona", TestVocacional.class)
                    .setParameter("idPersona", personaEstudiante.getPersona().getIdpersona())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(resultados == null){
                resultados = new TestVocacional();
                resultados.setIdPersona(personaEstudiante.getPersona().getIdpersona());
                resultados.setFechaCreacion(new Date());
                resultados.setCarreraInteresOu(Boolean.FALSE);
                resultados.setCompleto(Boolean.FALSE);
                resultados.setPeriodoEscolar(personaEstudiante.getInscripcionActiva().getPeriodo().getPeriodo());
                em.persist(resultados);
                em.flush();
            }
            if(resultados.getR1() != null){respuestas.put("r1", resultados.getR1().toString());}
            if(resultados.getR2() != null){respuestas.put("r2", resultados.getR2().toString());}
            if(resultados.getR3() != null){respuestas.put("r3", resultados.getR3().toString());}
            if(resultados.getR4() != null){respuestas.put("r4", resultados.getR4().toString());}
            if(resultados.getR5() != null){respuestas.put("r5", resultados.getR5().toString());}
            if(resultados.getR6() != null){respuestas.put("r6", resultados.getR6().toString());}
            if(resultados.getR7() != null){respuestas.put("r7", resultados.getR7().toString());}
            if(resultados.getR8() != null){respuestas.put("r8", resultados.getR8().toString());}
            if(resultados.getR9() != null){respuestas.put("r9", resultados.getR9().toString());}
            if(resultados.getR10() != null){respuestas.put("r10", resultados.getR10().toString());}
            if(resultados.getR11() != null){respuestas.put("r11", resultados.getR11().toString());}
            if(resultados.getR12() != null){respuestas.put("r12", resultados.getR12().toString());}
            if(resultados.getR13() != null){respuestas.put("r13", resultados.getR13().toString());}
            if(resultados.getR14() != null){respuestas.put("r14", resultados.getR14().toString());}
            if(resultados.getR15() != null){respuestas.put("r15", resultados.getR15().toString());}
            if(resultados.getR16() != null){respuestas.put("r16", resultados.getR16().toString());}
            if(resultados.getR17() != null){respuestas.put("r17", resultados.getR17().toString());}
            if(resultados.getR18() != null){respuestas.put("r18", resultados.getR18().toString());}
            if(resultados.getR19() != null){respuestas.put("r19", resultados.getR19().toString());}
            if(resultados.getR20() != null){respuestas.put("r20", resultados.getR20().toString());}
            if(resultados.getR21() != null){respuestas.put("r21", resultados.getR21().toString());}
            if(resultados.getR22() != null){respuestas.put("r22", resultados.getR22().toString());}
            if(resultados.getR23() != null){respuestas.put("r23", resultados.getR23().toString());}
            if(resultados.getR24() != null){respuestas.put("r24", resultados.getR24().toString());}
            if(resultados.getR25() != null){respuestas.put("r25", resultados.getR25().toString());}
            if(resultados.getR26() != null){respuestas.put("r26", resultados.getR26().toString());}
            if(resultados.getR27() != null){respuestas.put("r27", resultados.getR27().toString());}
            if(resultados.getR28() != null){respuestas.put("r28", resultados.getR28().toString());}
            if(resultados.getR29() != null){respuestas.put("r29", resultados.getR29().toString());}
            if(resultados.getR30() != null){respuestas.put("r30", resultados.getR30().toString());}
            if(resultados.getR31() != null){respuestas.put("r31", resultados.getR31().toString());}
            if(resultados.getR32() != null){respuestas.put("r32", resultados.getR32().toString());}
            if(resultados.getR33() != null){respuestas.put("r33", resultados.getR33().toString());}
            if(resultados.getR34() != null){respuestas.put("r34", resultados.getR34().toString());}
            if(resultados.getR35() != null){respuestas.put("r35", resultados.getR35().toString());}
            if(resultados.getR36() != null){respuestas.put("r36", resultados.getR36().toString());}
            if(resultados.getR37() != null){respuestas.put("r37", resultados.getR37().toString());}
            if(resultados.getR38() != null){respuestas.put("r38", resultados.getR38().toString());}
            if(resultados.getR39() != null){respuestas.put("r39", resultados.getR39().toString());}
            if(resultados.getR40() != null){respuestas.put("r40", resultados.getR40().toString());}
            if(resultados.getR41() != null){respuestas.put("r41", resultados.getR41().toString());}
            if(resultados.getR42() != null){respuestas.put("r42", resultados.getR42().toString());}
            if(resultados.getR43() != null){respuestas.put("r43", resultados.getR43().toString());}
            if(resultados.getR44() != null){respuestas.put("r44", resultados.getR44().toString());}
            if(resultados.getR45() != null){respuestas.put("r45", resultados.getR45().toString());}
            if(resultados.getR46() != null){respuestas.put("r46", resultados.getR46().toString());}
            if(resultados.getR47() != null){respuestas.put("r47", resultados.getR47().toString());}
            if(resultados.getR48() != null){respuestas.put("r48", resultados.getR48().toString());}
            if(resultados.getR49() != null){respuestas.put("r49", resultados.getR49().toString());}
            if(resultados.getR50() != null){respuestas.put("r50", resultados.getR50().toString());}
            if(resultados.getR51() != null){respuestas.put("r51", resultados.getR51().toString());}
            if(resultados.getR52() != null){respuestas.put("r52", resultados.getR52().toString());}
            if(resultados.getR53() != null){respuestas.put("r53", resultados.getR53().toString());}
            if(resultados.getR54() != null){respuestas.put("r54", resultados.getR54().toString());}
            if(resultados.getR55() != null){respuestas.put("r55", resultados.getR55().toString());}
            if(resultados.getR56() != null){respuestas.put("r56", resultados.getR56().toString());}
            if(resultados.getR57() != null){respuestas.put("r57", resultados.getR57().toString());}
            if(resultados.getR58() != null){respuestas.put("r58", resultados.getR58().toString());}
            if(resultados.getR59() != null){respuestas.put("r59", resultados.getR59().toString());}
            if(resultados.getR60() != null){respuestas.put("r60", resultados.getR60().toString());}
            if(resultados.getR61() != null){respuestas.put("r61", resultados.getR61().toString());}
            if(resultados.getR62() != null){respuestas.put("r62", resultados.getR62().toString());}
            if(resultados.getR63() != null){respuestas.put("r63", resultados.getR63().toString());}
            if(resultados.getR64() != null){respuestas.put("r64", resultados.getR64().toString());}
            if(resultados.getR65() != null){respuestas.put("r65", resultados.getR65().toString());}
            if(resultados.getR66() != null){respuestas.put("r66", resultados.getR66().toString());}
            if(resultados.getR67() != null){respuestas.put("r67", resultados.getR67().toString());}
            if(resultados.getR68() != null){respuestas.put("r68", resultados.getR68().toString());}
            if(resultados.getR69() != null){respuestas.put("r69", resultados.getR69().toString());}
            if(resultados.getR70() != null){respuestas.put("r70", resultados.getR70().toString());}
            if(resultados.getR71() != null){respuestas.put("r71", resultados.getR71().toString());}
            if(resultados.getR72() != null){respuestas.put("r72", resultados.getR72().toString());}
            if(resultados.getR73() != null){respuestas.put("r73", resultados.getR73().toString());}
            if(resultados.getR74() != null){respuestas.put("r74", resultados.getR74().toString());}
            if(resultados.getR75() != null){respuestas.put("r75", resultados.getR75().toString());}
            if(resultados.getR76() != null){respuestas.put("r76", resultados.getR76().toString());}
            if(resultados.getR77() != null){respuestas.put("r77", resultados.getR77().toString());}
            if(resultados.getR78() != null){respuestas.put("r78", resultados.getR78().toString());}
            if(resultados.getR79() != null){respuestas.put("r79", resultados.getR79().toString());}
            if(resultados.getR80() != null){respuestas.put("r80", resultados.getR80().toString());}
            if(resultados.getR81() != null){respuestas.put("r81", resultados.getR81().toString());}
            if(resultados.getR82() != null){respuestas.put("r82", resultados.getR82().toString());}
            if(resultados.getR83() != null){respuestas.put("r83", resultados.getR83().toString());}
            if(resultados.getR84() != null){respuestas.put("r84", resultados.getR84().toString());}
            if(resultados.getR85() != null){respuestas.put("r85", resultados.getR85().toString());}
            if(resultados.getR86() != null){respuestas.put("r86", resultados.getR86().toString());}
            if(resultados.getR87() != null){respuestas.put("r87", resultados.getR87().toString());}
            if(resultados.getR88() != null){respuestas.put("r88", resultados.getR88().toString());}
            return ResultadoEJB.crearCorrecto(resultados, "Resultados obtenidos correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados del test vocacional (EjbTestVocacional.getResultados)", e, null);
        }
    }
    
    /**
     * Actualiza / refresca los resultados del Test Vocacional
     * @param resultado
     * @param pregunta 
     * @param respuesta 
     * @param respuestas 
     */
    public void actualizarRespuestaPregunta(TestVocacional resultado, String pregunta, Short respuesta, Map<String,String> respuestas){
        try {
            switch (pregunta.trim()){
                case "r1": resultado.setR1(respuesta); break;
                case "r2": resultado.setR2(respuesta); break;
                case "r3": resultado.setR3(respuesta); break;
                case "r4": resultado.setR4(respuesta); break;
                case "r5": resultado.setR5(respuesta); break;
                case "r6": resultado.setR6(respuesta); break;
                case "r7": resultado.setR7(respuesta); break;
                case "r8": resultado.setR8(respuesta); break;
                case "r9": resultado.setR9(respuesta); break;
                case "r10": resultado.setR10(respuesta); break;
                case "r11": resultado.setR11(respuesta); break;
                case "r12": resultado.setR12(respuesta); break;
                case "r13": resultado.setR13(respuesta); break;
                case "r14": resultado.setR14(respuesta); break;
                case "r15": resultado.setR15(respuesta); break;
                case "r16": resultado.setR16(respuesta); break;
                case "r17": resultado.setR17(respuesta); break;
                case "r18": resultado.setR18(respuesta); break;
                case "r19": resultado.setR19(respuesta); break;
                case "r20": resultado.setR20(respuesta); break;
                case "r21": resultado.setR21(respuesta); break;
                case "r22": resultado.setR22(respuesta); break;
                case "r23": resultado.setR23(respuesta); break;
                case "r24": resultado.setR24(respuesta); break;
                case "r25": resultado.setR25(respuesta); break;
                case "r26": resultado.setR26(respuesta); break;
                case "r27": resultado.setR27(respuesta); break;
                case "r28": resultado.setR28(respuesta); break;
                case "r29": resultado.setR29(respuesta); break;
                case "r30": resultado.setR30(respuesta); break;
                case "r31": resultado.setR31(respuesta); break;
                case "r32": resultado.setR32(respuesta); break;
                case "r33": resultado.setR33(respuesta); break;
                case "r34": resultado.setR34(respuesta); break;
                case "r35": resultado.setR35(respuesta); break;
                case "r36": resultado.setR36(respuesta); break;
                case "r37": resultado.setR37(respuesta); break;
                case "r38": resultado.setR38(respuesta); break;
                case "r39": resultado.setR39(respuesta); break;
                case "r40": resultado.setR40(respuesta); break;
                case "r41": resultado.setR41(respuesta); break;
                case "r42": resultado.setR42(respuesta); break;
                case "r43": resultado.setR43(respuesta); break;
                case "r44": resultado.setR44(respuesta); break;
                case "r45": resultado.setR45(respuesta); break;
                case "r46": resultado.setR46(respuesta); break;
                case "r47": resultado.setR47(respuesta); break;
                case "r48": resultado.setR48(respuesta); break;
                case "r49": resultado.setR49(respuesta); break;
                case "r50": resultado.setR50(respuesta); break;
                case "r51": resultado.setR51(respuesta); break;
                case "r52": resultado.setR52(respuesta); break;
                case "r53": resultado.setR53(respuesta); break;
                case "r54": resultado.setR54(respuesta); break;
                case "r55": resultado.setR55(respuesta); break;
                case "r56": resultado.setR56(respuesta); break;
                case "r57": resultado.setR57(respuesta); break;
                case "r58": resultado.setR58(respuesta); break;
                case "r59": resultado.setR59(respuesta); break;
                case "r60": resultado.setR60(respuesta); break;
                case "r61": resultado.setR61(respuesta); break;
                case "r62": resultado.setR62(respuesta); break;
                case "r63": resultado.setR63(respuesta); break;
                case "r64": resultado.setR64(respuesta); break;
                case "r65": resultado.setR65(respuesta); break;
                case "r66": resultado.setR66(respuesta); break;
                case "r67": resultado.setR67(respuesta); break;
                case "r68": resultado.setR68(respuesta); break;
                case "r69": resultado.setR69(respuesta); break;
                case "r70": resultado.setR70(respuesta); break;
                case "r71": resultado.setR71(respuesta); break;
                case "r72": resultado.setR72(respuesta); break;
                case "r73": resultado.setR73(respuesta); break;
                case "r74": resultado.setR74(respuesta); break;
                case "r75": resultado.setR75(respuesta); break;
                case "r76": resultado.setR76(respuesta); break;
                case "r77": resultado.setR77(respuesta); break;
                case "r78": resultado.setR78(respuesta); break;
                case "r79": resultado.setR79(respuesta); break;
                case "r80": resultado.setR80(respuesta); break;
                case "r81": resultado.setR81(respuesta); break;
                case "r82": resultado.setR82(respuesta); break;
                case "r83": resultado.setR83(respuesta); break;
                case "r84": resultado.setR84(respuesta); break;
                case "r85": resultado.setR85(respuesta); break;
                case "r86": resultado.setR86(respuesta); break;
                case "r87": resultado.setR87(respuesta); break;
                case "r88": resultado.setR88(respuesta); break;
            }
            respuestas.put(pregunta, String.valueOf(respuesta));
        } catch (Exception e) {}
    }
    
    public ResultadoEJB<TestVocacional> actualizarCompleto(TestVocacional resultados){
        try {
            if(resultados == null) return ResultadoEJB.crearErroneo(2, new TestVocacional(), "Los resultados no deben ser nulos");
            resultados.setCompleto(true);
            if(resultados.getFechaTermino() == null)resultados.setFechaTermino(new Date());
            em.merge(resultados);
            em.flush();
            return ResultadoEJB.crearCorrecto(resultados, "Test Vocacional finalizado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al actualizar el valor que indica que ha finalizado el test vocacional (EjbTestVocacional.actualizarCompleto)", e, null);
        }
    }
    
    public boolean actualizarResultado(TestVocacional resultado) {
        Comparador<TestVocacional> comparador = new ComparadorTestVocacional();
        try {
            if (resultado != null) {
                em.merge(resultado);
                em.flush();
            }
            return comparador.isCompleto(resultado);
        } catch (Exception e) {
            return comparador.isCompleto(resultado);
        }
    }
    
//    TODO: hacer método que le muestrén al estudiante los resultados del Test Vocacional
    
    public ResultadoEJB<String> obtenerResultadosTestVocacional(TestVocacional testVocacional){
        try {
            if(testVocacional == null) return ResultadoEJB.crearErroneo(2, null, "No se pueden obtener los resultados con un test vocacional vacío");
            Comparador<TestVocacional> comparador = new ComparadorTestVocacional();
            Boolean finalizado = comparador.isCompleto(testVocacional);
            if(finalizado){
                Integer resultadoTIADSM = 0;
                Integer resultadoMAA = 0;
                Integer resultadoMAI = 0;
                Integer resultadoTIAEVND = 0;
                Integer resultadoGASTRO = 0;
                Integer resultadoTFAR = 0;
                Integer resultadoPA = 0;
                Integer resultadoAACH = 0;
                Integer resultadoQAB = 0;
                Integer resultadoMAP = 0;
                Integer resultadoASP = 0;
            
                resultadoTIADSM = Integer.valueOf(testVocacional.getR1())
                                + Integer.valueOf(testVocacional.getR12())
                                + Integer.valueOf(testVocacional.getR23())
                                + Integer.valueOf(testVocacional.getR34()) 
                                + Integer.valueOf(testVocacional.getR45())
                                + Integer.valueOf(testVocacional.getR56()) 
                                + Integer.valueOf(testVocacional.getR67())
                                + Integer.valueOf(testVocacional.getR78());

                resultadoMAA = Integer.valueOf(testVocacional.getR2())
                                + Integer.valueOf(testVocacional.getR13())
                                + Integer.valueOf(testVocacional.getR24())
                                + Integer.valueOf(testVocacional.getR35()) 
                                + Integer.valueOf(testVocacional.getR46())
                                + Integer.valueOf(testVocacional.getR57()) 
                                + Integer.valueOf(testVocacional.getR68())
                                + Integer.valueOf(testVocacional.getR79());

                resultadoMAI = Integer.valueOf(testVocacional.getR3())
                                + Integer.valueOf(testVocacional.getR14())
                                + Integer.valueOf(testVocacional.getR25())
                                + Integer.valueOf(testVocacional.getR36()) 
                                + Integer.valueOf(testVocacional.getR47())
                                + Integer.valueOf(testVocacional.getR58()) 
                                + Integer.valueOf(testVocacional.getR69())
                                + Integer.valueOf(testVocacional.getR80());

                resultadoTIAEVND = Integer.valueOf(testVocacional.getR4())
                                + Integer.valueOf(testVocacional.getR15())
                                + Integer.valueOf(testVocacional.getR26())
                                + Integer.valueOf(testVocacional.getR37()) 
                                + Integer.valueOf(testVocacional.getR48())
                                + Integer.valueOf(testVocacional.getR59()) 
                                + Integer.valueOf(testVocacional.getR70())
                                + Integer.valueOf(testVocacional.getR81());

                resultadoGASTRO = Integer.valueOf(testVocacional.getR5())
                                + Integer.valueOf(testVocacional.getR16())
                                + Integer.valueOf(testVocacional.getR27())
                                + Integer.valueOf(testVocacional.getR38()) 
                                + Integer.valueOf(testVocacional.getR49())
                                + Integer.valueOf(testVocacional.getR60()) 
                                + Integer.valueOf(testVocacional.getR71())
                                + Integer.valueOf(testVocacional.getR82());

                resultadoTFAR = Integer.valueOf(testVocacional.getR6())
                                + Integer.valueOf(testVocacional.getR17())
                                + Integer.valueOf(testVocacional.getR28())
                                + Integer.valueOf(testVocacional.getR39()) 
                                + Integer.valueOf(testVocacional.getR50())
                                + Integer.valueOf(testVocacional.getR61()) 
                                + Integer.valueOf(testVocacional.getR72())
                                + Integer.valueOf(testVocacional.getR83());

                resultadoPA = Integer.valueOf(testVocacional.getR7())
                                + Integer.valueOf(testVocacional.getR18())
                                + Integer.valueOf(testVocacional.getR29())
                                + Integer.valueOf(testVocacional.getR40()) 
                                + Integer.valueOf(testVocacional.getR51())
                                + Integer.valueOf(testVocacional.getR62()) 
                                + Integer.valueOf(testVocacional.getR73())
                                + Integer.valueOf(testVocacional.getR84());

                resultadoAACH = Integer.valueOf(testVocacional.getR8())
                                + Integer.valueOf(testVocacional.getR19())
                                + Integer.valueOf(testVocacional.getR30())
                                + Integer.valueOf(testVocacional.getR41()) 
                                + Integer.valueOf(testVocacional.getR52())
                                + Integer.valueOf(testVocacional.getR63()) 
                                + Integer.valueOf(testVocacional.getR74())
                                + Integer.valueOf(testVocacional.getR85());

                resultadoQAB = Integer.valueOf(testVocacional.getR9())
                                + Integer.valueOf(testVocacional.getR20())
                                + Integer.valueOf(testVocacional.getR31())
                                + Integer.valueOf(testVocacional.getR42()) 
                                + Integer.valueOf(testVocacional.getR53())
                                + Integer.valueOf(testVocacional.getR64()) 
                                + Integer.valueOf(testVocacional.getR75())
                                + Integer.valueOf(testVocacional.getR86());

                resultadoMAP = Integer.valueOf(testVocacional.getR10())
                                + Integer.valueOf(testVocacional.getR21())
                                + Integer.valueOf(testVocacional.getR32())
                                + Integer.valueOf(testVocacional.getR43()) 
                                + Integer.valueOf(testVocacional.getR54())
                                + Integer.valueOf(testVocacional.getR65()) 
                                + Integer.valueOf(testVocacional.getR76())
                                + Integer.valueOf(testVocacional.getR87());

                resultadoASP = Integer.valueOf(testVocacional.getR11())
                                + Integer.valueOf(testVocacional.getR22())
                                + Integer.valueOf(testVocacional.getR33())
                                + Integer.valueOf(testVocacional.getR44()) 
                                + Integer.valueOf(testVocacional.getR55())
                                + Integer.valueOf(testVocacional.getR66()) 
                                + Integer.valueOf(testVocacional.getR77())
                                + Integer.valueOf(testVocacional.getR88());

                if(resultadoTIADSM > resultadoMAA               && resultadoTIADSM > resultadoMAI       && resultadoTIADSM > resultadoTIAEVND       && resultadoTIADSM > resultadoGASTRO
                        && resultadoTIADSM > resultadoTFAR      && resultadoTIADSM > resultadoPA        && resultadoTIADSM > resultadoAACH          && resultadoTIADSM > resultadoQAB
                        && resultadoTIADSM > resultadoMAP       && resultadoTIADSM > resultadoASP){
                    return ResultadoEJB.crearCorrecto("TIADSM", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoMAA > resultadoTIADSM               && resultadoMAA > resultadoMAI       && resultadoMAA > resultadoTIAEVND       && resultadoMAA > resultadoGASTRO
                        && resultadoMAA > resultadoTFAR      && resultadoMAA > resultadoPA        && resultadoMAA > resultadoAACH          && resultadoMAA > resultadoQAB
                        && resultadoMAA > resultadoMAP       && resultadoMAA > resultadoASP){
                    return ResultadoEJB.crearCorrecto("MECAA", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoMAI > resultadoMAA               && resultadoMAI > resultadoTIADSM       && resultadoMAI > resultadoTIAEVND       && resultadoMAI > resultadoGASTRO
                        && resultadoMAI > resultadoTFAR      && resultadoMAI > resultadoPA        && resultadoMAI > resultadoAACH          && resultadoMAI > resultadoQAB
                        && resultadoMAI > resultadoMAP       && resultadoMAI > resultadoASP){
                    return ResultadoEJB.crearCorrecto("MAI", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoTIAEVND > resultadoMAA               && resultadoTIAEVND > resultadoMAI       && resultadoTIAEVND > resultadoTIADSM       && resultadoTIAEVND > resultadoGASTRO
                        && resultadoTIAEVND > resultadoTFAR      && resultadoTIAEVND > resultadoPA        && resultadoTIAEVND > resultadoAACH          && resultadoTIAEVND > resultadoQAB
                        && resultadoTIAEVND > resultadoMAP       && resultadoTIAEVND > resultadoASP){
                    return ResultadoEJB.crearCorrecto("TIEVND", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoGASTRO > resultadoMAA               && resultadoGASTRO > resultadoMAI       && resultadoGASTRO > resultadoTIAEVND       && resultadoGASTRO > resultadoTIADSM
                        && resultadoGASTRO > resultadoTFAR      && resultadoGASTRO > resultadoPA        && resultadoGASTRO > resultadoAACH          && resultadoGASTRO > resultadoQAB
                        && resultadoGASTRO > resultadoMAP       && resultadoGASTRO > resultadoASP){
                    return ResultadoEJB.crearCorrecto("GAS", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoTFAR > resultadoMAA                 && resultadoTFAR > resultadoMAI       && resultadoTFAR > resultadoTIAEVND       && resultadoTFAR > resultadoGASTRO
                        && resultadoTFAR > resultadoTIADSM      && resultadoTFAR > resultadoPA        && resultadoTFAR > resultadoAACH          && resultadoTFAR > resultadoQAB
                        && resultadoTFAR > resultadoMAP         && resultadoTFAR > resultadoASP){
                    return ResultadoEJB.crearCorrecto("TFAR", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoPA > resultadoMAA               && resultadoPA > resultadoMAI           && resultadoPA > resultadoTIAEVND       && resultadoPA > resultadoGASTRO
                        && resultadoPA > resultadoTFAR      && resultadoPA > resultadoTIADSM        && resultadoPA > resultadoAACH          && resultadoPA > resultadoQAB
                        && resultadoPA > resultadoMAP       && resultadoPA > resultadoASP){
                    return ResultadoEJB.crearCorrecto("PA", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoAACH > resultadoMAA               && resultadoAACH > resultadoMAI       && resultadoAACH > resultadoTIAEVND       && resultadoAACH > resultadoGASTRO
                        && resultadoAACH > resultadoTFAR      && resultadoAACH > resultadoPA        && resultadoAACH > resultadoTIADSM          && resultadoAACH > resultadoQAB
                        && resultadoAACH > resultadoMAP       && resultadoAACH > resultadoASP){
                    return ResultadoEJB.crearCorrecto("AACH", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoQAB > resultadoMAA               && resultadoQAB > resultadoMAI       && resultadoQAB > resultadoTIAEVND       && resultadoQAB > resultadoGASTRO
                        && resultadoQAB > resultadoTFAR      && resultadoQAB > resultadoPA        && resultadoQAB > resultadoAACH          && resultadoQAB > resultadoTIADSM
                        && resultadoQAB > resultadoMAP       && resultadoQAB > resultadoASP){
                    return ResultadoEJB.crearCorrecto("QAB", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoMAP > resultadoMAA                  && resultadoMAP > resultadoMAI       && resultadoMAP > resultadoTIAEVND       && resultadoMAP > resultadoGASTRO
                        && resultadoMAP > resultadoTFAR         && resultadoMAP > resultadoPA        && resultadoMAP > resultadoAACH          && resultadoMAP > resultadoQAB
                        && resultadoMAP > resultadoTIADSM       && resultadoMAP > resultadoASP){
                    return ResultadoEJB.crearCorrecto("MIAP", "Intereses profesionales obtenidos correctamente");

                }else if(resultadoASP > resultadoMAA               && resultadoASP > resultadoMAI       && resultadoASP > resultadoTIAEVND       && resultadoASP > resultadoGASTRO
                        && resultadoASP > resultadoTFAR      && resultadoASP > resultadoPA        && resultadoASP > resultadoAACH          && resultadoASP > resultadoQAB
                        && resultadoASP > resultadoMAP       && resultadoASP > resultadoTIADSM){
                    return ResultadoEJB.crearCorrecto("ASP", "Intereses profesionales obtenidos correctamente");
                }else{
                    return ResultadoEJB.crearErroneo(3, "Por favor revise sus resultados en la parte inferior de está interfaz", "No se ha podido obtener los resultados del Test Vocacional");
                }
            }else{
                return ResultadoEJB.crearErroneo(2, "Test Incompleto","Aún no ha completado su Test Vocacional");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener los resultados del test vocacional (EjbTestVocacional.obtenerResultadosTestVocacional)", e, null);
        }
    }
    
    public ResultadoEJB<Map<String, Double>> obtenerResultadosCompletosTestVocacional(TestVocacional testVocacional){
        try {
            if(testVocacional == null) return ResultadoEJB.crearErroneo(2, null, "No se pueden obtener los resultados con un test vocacional vacío");
            Comparador<TestVocacional> comparador = new ComparadorTestVocacional();
            Boolean finalizado = comparador.isCompleto(testVocacional);
            Map<String, Double> mapaResultados = new HashMap<>();
            if(finalizado){
                Integer resultadoTIADSM = 0;
                Integer resultadoMAA = 0;
                Integer resultadoMAI = 0;
                Integer resultadoTIAEVND = 0;
                Integer resultadoGASTRO = 0;
                Integer resultadoTFAR = 0;
                Integer resultadoPA = 0;
                Integer resultadoAACH = 0;
                Integer resultadoQAB = 0;
                Integer resultadoMAP = 0;
                Integer resultadoASP = 0;
            
                resultadoTIADSM = Integer.valueOf(testVocacional.getR1())
                                + Integer.valueOf(testVocacional.getR12())
                                + Integer.valueOf(testVocacional.getR23())
                                + Integer.valueOf(testVocacional.getR34()) 
                                + Integer.valueOf(testVocacional.getR45())
                                + Integer.valueOf(testVocacional.getR56()) 
                                + Integer.valueOf(testVocacional.getR67())
                                + Integer.valueOf(testVocacional.getR78());

                resultadoMAA = Integer.valueOf(testVocacional.getR2())
                                + Integer.valueOf(testVocacional.getR13())
                                + Integer.valueOf(testVocacional.getR24())
                                + Integer.valueOf(testVocacional.getR35()) 
                                + Integer.valueOf(testVocacional.getR46())
                                + Integer.valueOf(testVocacional.getR57()) 
                                + Integer.valueOf(testVocacional.getR68())
                                + Integer.valueOf(testVocacional.getR79());

                resultadoMAI = Integer.valueOf(testVocacional.getR3())
                                + Integer.valueOf(testVocacional.getR14())
                                + Integer.valueOf(testVocacional.getR25())
                                + Integer.valueOf(testVocacional.getR36()) 
                                + Integer.valueOf(testVocacional.getR47())
                                + Integer.valueOf(testVocacional.getR58()) 
                                + Integer.valueOf(testVocacional.getR69())
                                + Integer.valueOf(testVocacional.getR80());

                resultadoTIAEVND = Integer.valueOf(testVocacional.getR4())
                                + Integer.valueOf(testVocacional.getR15())
                                + Integer.valueOf(testVocacional.getR26())
                                + Integer.valueOf(testVocacional.getR37()) 
                                + Integer.valueOf(testVocacional.getR48())
                                + Integer.valueOf(testVocacional.getR59()) 
                                + Integer.valueOf(testVocacional.getR70())
                                + Integer.valueOf(testVocacional.getR81());

                resultadoGASTRO = Integer.valueOf(testVocacional.getR5())
                                + Integer.valueOf(testVocacional.getR16())
                                + Integer.valueOf(testVocacional.getR27())
                                + Integer.valueOf(testVocacional.getR38()) 
                                + Integer.valueOf(testVocacional.getR49())
                                + Integer.valueOf(testVocacional.getR60()) 
                                + Integer.valueOf(testVocacional.getR71())
                                + Integer.valueOf(testVocacional.getR82());

                resultadoTFAR = Integer.valueOf(testVocacional.getR6())
                                + Integer.valueOf(testVocacional.getR17())
                                + Integer.valueOf(testVocacional.getR28())
                                + Integer.valueOf(testVocacional.getR39()) 
                                + Integer.valueOf(testVocacional.getR50())
                                + Integer.valueOf(testVocacional.getR61()) 
                                + Integer.valueOf(testVocacional.getR72())
                                + Integer.valueOf(testVocacional.getR83());

                resultadoPA = Integer.valueOf(testVocacional.getR7())
                                + Integer.valueOf(testVocacional.getR18())
                                + Integer.valueOf(testVocacional.getR29())
                                + Integer.valueOf(testVocacional.getR40()) 
                                + Integer.valueOf(testVocacional.getR51())
                                + Integer.valueOf(testVocacional.getR62()) 
                                + Integer.valueOf(testVocacional.getR73())
                                + Integer.valueOf(testVocacional.getR84());

                resultadoAACH = Integer.valueOf(testVocacional.getR8())
                                + Integer.valueOf(testVocacional.getR19())
                                + Integer.valueOf(testVocacional.getR30())
                                + Integer.valueOf(testVocacional.getR41()) 
                                + Integer.valueOf(testVocacional.getR52())
                                + Integer.valueOf(testVocacional.getR63()) 
                                + Integer.valueOf(testVocacional.getR74())
                                + Integer.valueOf(testVocacional.getR85());

                resultadoQAB = Integer.valueOf(testVocacional.getR9())
                                + Integer.valueOf(testVocacional.getR20())
                                + Integer.valueOf(testVocacional.getR31())
                                + Integer.valueOf(testVocacional.getR42()) 
                                + Integer.valueOf(testVocacional.getR53())
                                + Integer.valueOf(testVocacional.getR64()) 
                                + Integer.valueOf(testVocacional.getR75())
                                + Integer.valueOf(testVocacional.getR86());

                resultadoMAP = Integer.valueOf(testVocacional.getR10())
                                + Integer.valueOf(testVocacional.getR21())
                                + Integer.valueOf(testVocacional.getR32())
                                + Integer.valueOf(testVocacional.getR43()) 
                                + Integer.valueOf(testVocacional.getR54())
                                + Integer.valueOf(testVocacional.getR65()) 
                                + Integer.valueOf(testVocacional.getR76())
                                + Integer.valueOf(testVocacional.getR87());

                resultadoASP = Integer.valueOf(testVocacional.getR11())
                                + Integer.valueOf(testVocacional.getR22())
                                + Integer.valueOf(testVocacional.getR33())
                                + Integer.valueOf(testVocacional.getR44()) 
                                + Integer.valueOf(testVocacional.getR55())
                                + Integer.valueOf(testVocacional.getR66()) 
                                + Integer.valueOf(testVocacional.getR77())
                                + Integer.valueOf(testVocacional.getR88());

                Double resultadoTIADSMDouble = 0.0D;
                Double resultadoMAADouble = 0.0D;
                Double resultadoMAIDouble = 0.0D;
                Double resultadoTIAEVNDDouble = 0.0D;
                Double resultadoGASTRODouble = 0.0D;
                Double resultadoTFARDouble = 0.0D;
                Double resultadoPADouble = 0.0D;
                Double resultadoAACHDouble = 0.0D;
                Double resultadoQABDouble = 0.0D;
                Double resultadoMAPDouble = 0.0D;
                Double resultadoASPDouble = 0.0D;
                
                resultadoTIADSMDouble = 100 * resultadoTIADSM / 40D;
                resultadoMAADouble = 100 * resultadoMAA / 40D;
                resultadoMAIDouble = 100 * resultadoMAI / 40D;
                resultadoTIAEVNDDouble = 100 * resultadoTIAEVND / 40D;
                resultadoGASTRODouble = 100 * resultadoGASTRO / 40D;
                resultadoTFARDouble = 100 * resultadoTFAR / 40D;
                resultadoPADouble = 100 * resultadoPA / 40D;
                resultadoAACHDouble = 100 * resultadoAACH / 40D;
                resultadoQABDouble = 100 * resultadoQAB / 40D;
                resultadoMAPDouble = 100 * resultadoMAP / 40D;
                resultadoASPDouble = 100 * resultadoASP / 40D;
                
                mapaResultados.put("TIADSM",resultadoTIADSMDouble);
                mapaResultados.put("MECAA",resultadoMAADouble);
                mapaResultados.put("MAI",resultadoMAIDouble);
                mapaResultados.put("TIEVND",resultadoTIAEVNDDouble);
                mapaResultados.put("GAS",resultadoGASTRODouble);
                mapaResultados.put("TFAR",resultadoTFARDouble);
                mapaResultados.put("PA",resultadoPADouble);
                mapaResultados.put("AACH",resultadoAACHDouble);
                mapaResultados.put("QAB",resultadoQABDouble);
                mapaResultados.put("MIAP",resultadoMAPDouble);
                mapaResultados.put("ASP",resultadoASPDouble);
                
                Map<String, Double> mapaOrdenado = mapaResultados.entrySet().stream()
                        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldVal, newVal) -> oldVal,
                                        LinkedHashMap::new
                                )
                        );
                
                return ResultadoEJB.crearCorrecto(mapaOrdenado, "Obtención de resultados del Test Vocacional");
            }else{
                return ResultadoEJB.crearErroneo(4, null, "Aún no ha completado su Test Vocacional");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener los resultados del test vocacional (EjbTestVocacional.obtenerResultadosTestVocacional)", e, null);
        }
    }
    
}
