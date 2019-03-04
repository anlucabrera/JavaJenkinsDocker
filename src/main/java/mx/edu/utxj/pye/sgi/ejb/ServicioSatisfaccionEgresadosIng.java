/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIngPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioSatisfaccionEgresadosIng implements EjbSatisfaccionEgresadosIng {
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    @Getter @Setter Integer periodo;
    @EJB Facade f;
    @EJB Facade2 f2;
    
    @Override
    public Evaluaciones getEvaluacionActiva(){
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Satisfacción de egresados de ingenieria");
        q.setParameter("fecha", new Date());
        
        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return new Evaluaciones();
        }else{
            return l.get(0);
        }
    }

    
    @Override
    public EncuestaSatisfaccionEgresadosIng getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas){
        try {
            EncuestaSatisfaccionEgresadosIngPK pk = new EncuestaSatisfaccionEgresadosIngPK(evaluacion.getEvaluacion(), evaluador);
            f.setEntityClass(EncuestaSatisfaccionEgresadosIng.class);

            EncuestaSatisfaccionEgresadosIng r = (EncuestaSatisfaccionEgresadosIng) f.find(pk);

            if (r == null) {
                r = new EncuestaSatisfaccionEgresadosIng(pk);
                f.create(r);
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
            if (r.getR8() != null) {
                respuestas.put("p9", r.getR9().toString());
            }
            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    @Override
    public boolean actualizarResultado(EncuestaSatisfaccionEgresadosIng resultado){
        if(resultado != null){
            f.setEntityClass(EncuestaSatisfaccionEgresadosIng.class);
            f.edit(resultado);
        }
        
        Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();        
        return comparador.isCompleto(resultado);
    }

    @Override
    public void actualizarRespuestaPorPregunta(EncuestaSatisfaccionEgresadosIng resultado, String pregunta, String respuesta, Map<String,String> respuestas){
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
        }
        
        respuestas.put(pregunta, respuesta);
    }

    @Override
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Satisfacción de egresados de ingeniería");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 1f, "¿La infraestructura física con que fue dotada la universidad, le pareció?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 2f, "¿El equipamiento de los laboratorios y talleres, le pareció?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 3f, "¿Los servicios prestados en la Bolsa de Trabajo de la Universidad cómo los considera?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 4f, "¿El nivel de conocimiento y dominio de los temas mostrado por sus profesores al momento "
                + "de impartirle la cátedra, le pareció?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 5f, "¿El nivel de conocimiento y dominio por parte de los profesores en el manejo de los equipos que se encuentran "
                + "en los laboratorios y talleres al momento de realizar las prácticas que su carrera requiere, lo considera?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 6f, "¿La experiencia práctica adquirida por parte suya, derivado de las visitas, prácticas en las empresas, las considera?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 7f, "¿Cómo considera la preparación académica adquirida?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 8f, "¿Considera que la estadía complementó su preparación para el mercado laboral?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 9f, "¿Cómo califica la Continuidad de Estudios (Licenciatura o Ingeniería)?", ""));
        
        l.add(a1);
        
        
        
        return l;
    }

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("5", "MB", "Muy bien"));
        l.add(new SelectItem("4", "B", "Bien"));
        l.add(new SelectItem("3", "R", "Regular"));
        l.add(new SelectItem("2", "M", "Mal"));
        l.add(new SelectItem("1", "P", "Pésimo"));
        l.add(new SelectItem("0", "NA", "No aplica"));

        return l;
    }
    
    @Override
    public List<EncuestaSatisfaccionEgresadosIng> resultadosEncuesta(){
        TypedQuery<EncuestaSatisfaccionEgresadosIng> q = em.createQuery("select e from EncuestaSatisfaccionEgresadosIng e ", EncuestaSatisfaccionEgresadosIng.class);
        List<EncuestaSatisfaccionEgresadosIng> pr = q.getResultList();
        return pr;
    }
    
    @Override
    public Alumnos obtenerAlumnos(String matricula) {
        TypedQuery<Periodos> periodoAct = f2.getEntityManager().createQuery("SELECT p FROM Periodos AS p",Periodos.class);
        List<Periodos> periodos = periodoAct.getResultList();
        periodos.stream().forEach(x -> {
            Boolean activo = true;
            Boolean acv = x.getActivo().equals(true);
            if (activo.equals(acv)) {
                periodo = x.getPeriodosPK().getCvePeriodo();
                System.out.println("Periodo:"+ periodo);
            }
        });

        TypedQuery<Alumnos> q = f2.getEntityManager()
                .createQuery("SELECT a from Alumnos a WHERE a.matricula=:matricula AND a.cveStatus = :estatus AND a.grupos.gruposPK.cvePeriodo = :periodo AND a.gradoActual =:grado", Alumnos.class);
        q.setParameter("estatus", 1);
        q.setParameter("periodo", periodo);
        q.setParameter("matricula", matricula);
        q.setParameter("grado", 11);
        
        List<Alumnos> l = q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }
    
    @Override
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion){
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares)f.find(evaluacion.getPeriodo());
    }
    
}
