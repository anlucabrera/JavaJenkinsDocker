package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import mx.edu.utxj.pye.sgi.controlador.EstudioSocioeconomicoOperativo;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Stateless
public class EjbEvaluacionEstudioSocioeconomico implements Serializable {
    @EJB
    Facade f;
    @EJB
    Facade2 f2;

    public Evaluaciones evaluacionActiva() {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Estudio socioeconómico");
        q.setParameter("fecha", new Date());

        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return new Evaluaciones();
        }else{
            return l.get(0);
        }
    }

    public AlumnosEncuestas getEvaluador(Integer periodo, String matricula) {
        TypedQuery<AlumnosEncuestas> q = f2.getEntityManager().createQuery("SELECT e from AlumnosEncuestas e WHERE e.matricula=:matricula AND e.cvePeriodo=:periodo", AlumnosEncuestas.class);
        q.setParameter("periodo", periodo);
        q.setParameter("matricula", matricula);

        List<AlumnosEncuestas> l = q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }

    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion) {
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares)f.find(evaluacion.getPeriodo());
    }

    public EvaluacionesEstudioSocioeconomicoResultados getResultado(Evaluaciones evaluacion, AlumnosEncuestas evaluador) {
        TypedQuery<EvaluacionesEstudioSocioeconomicoResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionesEstudioSocioeconomicoResultados r WHERE r.evaluacionesEstudioSocioeconomicoResultadosPK.evaluacion=:evaluacion AND r.evaluacionesEstudioSocioeconomicoResultadosPK.evaluador=:evaluador", EvaluacionesEstudioSocioeconomicoResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", Integer.parseInt(evaluador.getMatricula()));
        List<EvaluacionesEstudioSocioeconomicoResultados> l = q.getResultList();

        if(!l.isEmpty()){
            return l.get(0);
        }else{
            f.setEntityClass(EvaluacionesEstudioSocioeconomicoResultados.class);
            EvaluacionesEstudioSocioeconomicoResultados resultados = new EvaluacionesEstudioSocioeconomicoResultados(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getMatricula()));
            f.create(resultados);
            return resultados;
        }
    }

    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();

        Apartado a1 = new Apartado(1F, "", new ArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,1.0f, "¿Tienes hijos?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,2.0f, "¿Eres madre soltera o padre soltero?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,3.0f, "¿Trabajas?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,3.1f, "¿Cuál es el ingreso mensual aproximado que percibes en tu trabajo?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,4.0f, "¿Sabes si tus padres padecen alguna enfermedad terminal?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,4.1f, "¿Cuál?", ""));
        a1.getPreguntas().add(new Abierta(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,      1.0f,4.2f, "Escribe que enfermedad padecen: ", 3));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,5.0f, "¿En tu familia existe algún problema adverso que afecte tu desempeño escolar?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,5.1f, "¿Qué tipo de problema?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,6.0f, "Según tus condiciones de vida ¿consideras que vives en pobreza extrema?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,7.0f, "¿Consideras que padeces algún tipo de desnutrición?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,8.0f, "¿Tienes alguna deficiencia física o mental?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,8.1f, "¿Cuál?", ""));
        a1.getPreguntas().add(new Abierta(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,      1.0f,8.2f, "Escribe cual deficiencia física o mental", 3));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,9.0f, "¿Dependes económicamente de tus padres?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,10.0f, "¿Cuál es el ingreso mensual aproximado de tus padres?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,11.0f, "¿Cuál es la escolaridad máxima alcanzada por tu padre?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ESTUDIO_SOCIOECONOMICO,     1.0f,12.0f, "¿Cuál es la escolaridad máxima alcanzada por tu madre?", ""));

        l.add(a1);

        return l;
    }

    public ResultadoEJB<EvaluacionesEstudioSocioeconomicoResultados> actualizar(String id, String valor, EvaluacionesEstudioSocioeconomicoResultados resultado){
//        System.out.println("EjbEvaluacionEstudioSocioeconomico.actualizar");
//        System.out.println("id = [" + id + "], valor = [" + valor + "], resultado = [" + resultado + "]");
        try {
            switch (id){
                case "p0": resultado.setR1TienesHijos(valor); break;
                case "p1": resultado.setR2MadrePadreSoltero(valor); break;
                case "p2": resultado.setR3Trabajas(valor); break;
                case "p3": resultado.setR3aTrabajasIngresoMensual(Double.parseDouble(valor)); break;
                case "p4": resultado.setR4PadresEnfermedadTerminal(valor); break;
                case "p5": resultado.setR4aPadresEnfermedadTerminalCual(valor); break;
                case "p6": resultado.setR4bPadresEnfermedadTerminalOtra(valor); break;
                case "p7": resultado.setR5ProblemaAdverso(valor); break;
                case "p8": resultado.setR5aProblemaAdversoTipo(valor); break;
                case "p9": resultado.setR6PobrezaExtrema(valor); break;
                case "p10": resultado.setR7Desnutricion(valor); break;
                case "p11": resultado.setR8DeficienciaFisicaMental(valor); break;
                case "p12": resultado.setR8aDeficienciaFisicaMentalCual(valor); break;
                case "p13": resultado.setR8bDeficienciaFisicaMentalOtra(valor); break;
                case "p14": resultado.setR9DependenciaEconomicaPadres(valor); break;
                case "p15": resultado.setR10IngresoMensualPadres(Double.parseDouble(valor)); break;
                case "p16": resultado.setR11EscolaridadMaximaPadre(valor); break;
                case "p17": resultado.setR12EscolaridadMaximaMadre(valor); break;
            }
            return ResultadoEJB.crearCorrecto(resultado, "Valor guardado en memoria");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error: ", e, EvaluacionesEstudioSocioeconomicoResultados.class);
        }
    }

    public ResultadoEJB<EvaluacionesEstudioSocioeconomicoResultados> guardar(EvaluacionesEstudioSocioeconomicoResultados resultado){
        try{
            f.edit(resultado);
            return ResultadoEJB.crearCorrecto(resultado, "Valor guardado en base de datos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error: ", e, EvaluacionesEstudioSocioeconomicoResultados.class);
        }
    }

    public ResultadoEJB<Boolean> verificarVisibilidad(String id, EvaluacionesEstudioSocioeconomicoResultados resultado){
        try {
            Boolean res = false;
            switch (id){
                case "p3": res = Objects.equals(resultado.getR3Trabajas(), "Sí"); break;
                case "p5": res = Objects.equals(resultado.getR4PadresEnfermedadTerminal(), "Sí"); break;
                case "p6": res = Objects.equals(resultado.getR4PadresEnfermedadTerminal(), "Sí") && Objects.equals(resultado.getR4aPadresEnfermedadTerminalCual(), EstudioSocioeconomicoOperativo.EnfermedadTerminal.OTRA.toString()); break;
                case "p8": res = Objects.equals(resultado.getR5ProblemaAdverso(), "Sí"); break;
                case "p12": res = Objects.equals(resultado.getR8DeficienciaFisicaMental(), "Sí"); break;
                case "p13": res = Objects.equals(resultado.getR8DeficienciaFisicaMental(), "Sí") && Objects.equals(resultado.getR8aDeficienciaFisicaMentalCual(), EstudioSocioeconomicoOperativo.Deficiencia.OTRA.toString()); break;
            }
            return ResultadoEJB.crearCorrecto(res, "Comprobacion correcta");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error: ", e, Boolean.class);
        }
    }
}
