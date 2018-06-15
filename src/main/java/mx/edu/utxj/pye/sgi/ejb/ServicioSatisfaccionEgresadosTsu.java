/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsuPK;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosTsu;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioSatisfaccionEgresadosTsu implements EjbSatisfaccionEgresadosTsu {
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    @EJB Facade f;
    @EJB Facade2 f2;
    
    @Override
    public Evaluaciones getEvaluacionActiva(){
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Satisfacción de egresados de TSU");
        q.setParameter("fecha", new Date());
        
        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }

    
    @Override
    public ResultadosEncuestaSatisfaccionTsu getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas){
        try {
            ResultadosEncuestaSatisfaccionTsuPK pk = new ResultadosEncuestaSatisfaccionTsuPK(evaluacion.getEvaluacion(), evaluador);
            f.setEntityClass(ResultadosEncuestaSatisfaccionTsu.class);

            ResultadosEncuestaSatisfaccionTsu r = (ResultadosEncuestaSatisfaccionTsu) f.find(pk);

            if (r == null) {
                r = new ResultadosEncuestaSatisfaccionTsu(pk);
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
    public boolean actualizarResultado(ResultadosEncuestaSatisfaccionTsu resultado){
        if(resultado != null){
            f.setEntityClass(ResultadosEncuestaSatisfaccionTsu.class);
            f.edit(resultado);
        }
        
        Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();        
        return comparador.isCompleto(resultado);
    }

    @Override
    public void actualizarRespuestaPorPregunta(ResultadosEncuestaSatisfaccionTsu resultado, String pregunta, String respuesta, Map<String,String> respuestas){
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
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Satisfacción de egresados de TSU");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 1f, "¿La infraestructura física con que fue dotada la universidad, le pareció?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 2f, "¿El equipamiento de los laboratorios y talleres le pareció?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 3f, "¿Los servicios prestados en la Bolsa de Trabajo de la universidad como los considera?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 4f, "¿El nivel de conocimiento y dominio de los temas mostrado por sus profesores al momento de impartirle la cátedra le pareció?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 5f, "¿El nivel de conocimiento y dominio por parte de los profesores en el manejo de los equipos que se encuentran en los laboratorios y talleres al momento de realizar las prácticas que su carrera requiere, lo considera?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 6f, "¿La experiencia práctica adquirida por parte suya, derivado de las visitas, prácticas en las empresas, las considera?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 7f, "¿Cómo considera la preparación académica adquirida?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 8f, "¿Considera que la estadía complementó su preparación para el mercado laboral?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SATISFACCION_EGRESADOS_TSU.getNumero() , 1f, 9f, "¿Cómo califica el Modelo Educativo de 5B Técnico Superior Universitario o Profesional Asociado?", ""));
        
        l.add(a1);
        
        
        
        return l;
    }

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "MB", "Muy bien"));
        l.add(new SelectItem("4", "B", "Bien"));
        l.add(new SelectItem("3", "R", "Regular"));
        l.add(new SelectItem("2", "M", "Mal"));
        l.add(new SelectItem("1", "P", "Pésimo"));
        l.add(new SelectItem("0", "NA", "No aplica"));

        return l;
    }
    
    @Override
    public List<ResultadosEncuestaSatisfaccionTsu> resultadosEncuesta(){
        TypedQuery<ResultadosEncuestaSatisfaccionTsu> q = em.createQuery("select r from ResultadosEncuestaSatisfaccionTsu r ", ResultadosEncuestaSatisfaccionTsu.class);
        List<ResultadosEncuestaSatisfaccionTsu> pr = q.getResultList();
        return pr;
    }

    @Override
    public ResultadosEncuestaSatisfaccionTsu actualizarEstatus(ResultadosEncuestaSatisfaccionTsu updateEncSatTsu){
        f.setEntityClass(ResultadosEncuestaSatisfaccionTsu.class);
        f.edit(updateEncSatTsu);
        f.flush();
        return updateEncSatTsu;
    }
    
    @Override
    public ResultadosEncuestaSatisfaccionTsu obtenerEncuestaPorEvaluador(Integer matricula){
        TypedQuery<ResultadosEncuestaSatisfaccionTsu> q = f.getEntityManager().createQuery("SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.resultadosEncuestaSatisfaccionTsuPK.evaluador = :matricula",ResultadosEncuestaSatisfaccionTsu.class);
        q.setParameter("matricula", matricula);
        List<ResultadosEncuestaSatisfaccionTsu> pr=q.getResultList();
        if(pr.isEmpty()){
            return null;
        }else{
            return pr.get(0);
        }
    }
}
