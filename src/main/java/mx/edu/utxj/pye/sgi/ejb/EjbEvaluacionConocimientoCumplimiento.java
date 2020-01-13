package mx.edu.utxj.pye.sgi.ejb;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import mx.edu.utxj.pye.sgi.controlador.EvaluacionConocimientoCodigoEtica;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionCodigoEtica;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
public class EjbEvaluacionConocimientoCumplimiento {

    @EJB
    Facade f;
    @EJB
    EjbPersonalBean ejbPersonalBean;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarPersonal(Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReincorporacion.validarServiciosEscolares)", e, null);
        }
    }

    /**
     * Permite verificar si hay un periodo abierto para reincoporaciones
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<Evaluaciones> verificarEvaluacion(){
        try{
            //verificar apertura del evento
            Evaluaciones eventoEscolar = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", "Evaluación de Conocimiento y Cumplimiento")
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2,eventoEscolar, "No existe evento aperturado del tipo solicitado.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para generacion de grupos (EjbGeneracionGrupos.).", e, Evaluaciones.class);
        }
    }

    public ResultadoEJB<List<Apartado>> apartados(){
        List<Apartado> a = new ArrayList<>();
        Apartado a1 = new Apartado(1f);
        a1.setContenido("");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 1f, "¿Sabes cuál es el objetivo del Código de Ética del Estado?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 2f, "¿Quiénes son los responsables de fomentar en los Servidores Públicos los principios y valores contenidos en el Código de Ética?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 3f, "Son objetivos del Código de Ética:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 4f, "Son principios del Código de Ética:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 5f, "Son valores del Código de Ética:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 6f, "El Código de Conducta de la Universidad Tecnológica de Xicotepec de Juárez es de observancia obligatoria ¿para quienes?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 7f, "¿Qué valores promueve la Universidad?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 8f, "¿Quién es el responsable de vigilar el cumplimiento de las normas de conducta y ética?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 9f, "¿Cómo está conformada la Comunidad Universitaria de la UTXJ?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ETICA.getNumero() , 1f, 10f, "¿Con qué visión debe contribuir un integrante de la Comunidad Universitaria?", ""));
        a.add(a1);
        return ResultadoEJB.crearCorrecto(a, "Se genero las preguntas correctamente");
    }

    public ResultadoEJB<List<SelectItem>> getRespuestasPosibles(Pregunta pregunta) {
        List<SelectItem> l = new ArrayList<>();
                if(pregunta.getNumero().equals(1f)){
                    l.add(new SelectItem("1", "A", "Guiar con valores a los integrantes de los municipios."));
                    l.add(new SelectItem("2", "B", "Establecer los principios y valores que deberán observar los Servidores Públicos en el ejercicio de su empleo, cargo o comisión."));
                    l.add(new SelectItem("3", "C", "Ninguna de las anteriores"));
                }
                if(pregunta.getNumero().equals(2f)){
                    l.add(new SelectItem("1", "A", "El Gobernador por conducto del Congreso del Estado"));
                    l.add(new SelectItem("2", "B", "Los Jefes de Departamento de la Universidad."));
                    l.add(new SelectItem("3", "C", "Los Titulares de las dependencias y entidades de la Administración Pública Estatal."));
                }
                if(pregunta.getNumero().equals(3f)){
                    l.add(new SelectItem("1", "A", "Fortalecer la confianza de los ciudadanos en las instituciones; Promover la cultura ética de la Administración Pública Estatal."));
                    l.add(new SelectItem("2", "B", "Colaborar a la consolidación de una cultura gubernamental de legalidad, integridad, transparencia y rendición de cuentas; Procurar un ambiente laboral, armonioso, profesional y basado en el respeto mutuo."));
                    l.add(new SelectItem("3", "C", "Todas las anteriores"));
                }
                if(pregunta.getNumero().equals(4f)){
                    l.add(new SelectItem("1", "A", "Legalidad, honradez, lealtad, imparcialidad, eficacia y eficiencia."));
                    l.add(new SelectItem("2", "B", "Eficacia, eficiencia y economía."));
                    l.add(new SelectItem("3", "C", "Transparencia y rendición de cuentas"));
                }
                if(pregunta.getNumero().equals(5f)){
                    l.add(new SelectItem("1", "A", "Honradez, igualdad, imparcialidad y justicia."));
                    l.add(new SelectItem("2", "B", "Responsabilidad, interés público, Transparencia, igualdad y no discriminación, respeto, entorno cultural y ecológico y liderazgo."));
                    l.add(new SelectItem("3", "C", "Legalidad, honradez, lealtad, imparcialidad, eficacia y eficiencia"));
                }
                if(pregunta.getNumero().equals(6f)){
                    l.add(new SelectItem("1", "A", "Servidores Públicos del Estado"));
                    l.add(new SelectItem("2", "B", "Servidores Públicos de la Universidad."));
                    l.add(new SelectItem("3", "C", "Alumnado, personal académico y administrativo de la Universidad."));
                }
                if(pregunta.getNumero().equals(7f)){
                    l.add(new SelectItem("1", "A", "Obediencia, recato, sencillez, cabalidad y perfección."));
                    l.add(new SelectItem("2", "B", "Calidad, compromiso, disciplina, eficacia, equidad, generosidad, honestidad, respeto, responsabilidad, servicio, tolerancia, transparencia."));
                    l.add(new SelectItem("3", "C", "Todas las anteriores."));
                }
                if(pregunta.getNumero().equals(8f)){
                    l.add(new SelectItem("1", "A", "El Comité de Igualdad Laboral y No Discriminación."));
                    l.add(new SelectItem("2", "B", "El Comité de Transparencia."));
                    l.add(new SelectItem("3", "C", "El Comité de Ética y Prevención de Conflictos de Interés"));
                }
                if(pregunta.getNumero().equals(9f)){
                    l.add(new SelectItem("1", "A", "Las y los estudiantes, personal académico y personal administrativo"));
                    l.add(new SelectItem("2", "B", "Sociedad y empresarios."));
                    l.add(new SelectItem("3", "C", "Servidores públicos, sector empresarial y sector productivo."));
                }
                if(pregunta.getNumero().equals(10f)){
                    l.add(new SelectItem("1", "A", "De compromiso, responsabilidad, tolerancia, honestidad y lealtad."));
                    l.add(new SelectItem("2", "B", "Obediencia, recato, sencillez, cabalidad y perfección."));
                    l.add(new SelectItem("3", "C", "Todas las anteriores."));
                }
        return ResultadoEJB.crearCorrecto(l, "");
    }

    public ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados> getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas) {
        try {
            EvaluacionConocimientoCodigoEticaResultadosPK pk = new EvaluacionConocimientoCodigoEticaResultadosPK(evaluacion.getEvaluacion(), evaluador);
            f.setEntityClass(EvaluacionConocimientoCodigoEticaResultados.class);

            EvaluacionConocimientoCodigoEticaResultados r = (EvaluacionConocimientoCodigoEticaResultados) f.find(pk);

            if (r == null) {
                r = new EvaluacionConocimientoCodigoEticaResultados(pk);
                em.persist(r);
            }

            if (r.getR1() != null) {
                respuestas.put("p1", r.getR1().toString());
            }
            if (r.getR2() != null) {
                respuestas.put("p2", r.getR2().toString());
            }
            if (r.getR3() != null) {
                respuestas.put("p3", r.getR3().toString());
            }
            if (r.getR4() != null) {
                respuestas.put("p4", r.getR4().toString());
            }
            if (r.getR5() != null) {
                respuestas.put("p5", r.getR5().toString());
            }
            if (r.getR6() != null) {
                respuestas.put("p6", r.getR6().toString());
            }
            if (r.getR7() != null) {
                respuestas.put("p7", r.getR7().toString());
            }
            if (r.getR8() != null) {
                respuestas.put("p8", r.getR8().toString());
            }
            if (r.getR9() != null) {
                respuestas.put("p9", r.getR9().toString());
            }
            if (r.getR10() != null) {
                respuestas.put("p10", r.getR10().toString());
            }
            return ResultadoEJB.crearCorrecto(r, "");
        } catch (NullPointerException ne) {
            return null;
        }
    }

    public boolean actualizarResultado(EvaluacionConocimientoCodigoEticaResultados resultado) {
        if(resultado != null){
            f.setEntityClass(EvaluacionConocimientoCodigoEticaResultados.class);
            f.edit(resultado);
        }

        Comparador<EvaluacionConocimientoCodigoEticaResultados> comparador = new ComparadorEvaluacionCodigoEtica();
        return comparador.isCompleto(resultado);
    }

    public void actualizarRespuestaPorPregunta(EvaluacionConocimientoCodigoEticaResultados resultado, String pregunta, String respuesta, Map<String,String> respuestas) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(Short.parseShort(respuesta)); break;
            case "p2": resultado.setR2(Short.parseShort(respuesta)); break;
            case "p3": resultado.setR3(Short.parseShort(respuesta)); break;
            case "p4": resultado.setR4(Short.parseShort(respuesta)); break;
            case "p5": resultado.setR5(Short.parseShort(respuesta)); break;
            case "p6": resultado.setR6(Short.parseShort(respuesta)); break;
            case "p7": resultado.setR7(Short.parseShort(respuesta)); break;
            case "p8": resultado.setR8(Short.parseShort(respuesta)); break;
            case "p9": resultado.setR9(Short.parseShort(respuesta)); break;
            case "p10": resultado.setR10(Short.parseShort(respuesta)); break;
        }

        respuestas.put(pregunta, respuesta);
    }
}
