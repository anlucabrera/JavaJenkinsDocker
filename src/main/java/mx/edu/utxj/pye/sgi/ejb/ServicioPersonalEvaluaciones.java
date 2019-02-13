package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.TypedQuery;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServicioPersonalEvaluaciones implements EjbPersonalEvaluaciones {

    @EJB
    Facade f;

    @Override
    public Personal getPersonal(Integer clave) {
        f.setEntityClass(Personal.class);
        return (Personal) f.find(clave);
    }

    @Override
    public List<PeriodosEscolares> getPeriodos(Personal personal) {
        TypedQuery<DesempenioEvaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM DesempenioEvaluaciones e INNER JOIN e.desempenioEvaluacionResultadosList r WHERE r.desempenioEvaluacionResultadosPK.evaluado = :evaluado ORDER BY e.periodo DESC ", DesempenioEvaluaciones.class);
        q.setParameter("evaluado", personal.getClave());
        List<DesempenioEvaluaciones> evas = q.getResultList();

        if (evas.isEmpty()) {
            return null;
        } else {
            List<PeriodosEscolares> l = new ArrayList<>();
            evas.stream().forEach(e -> {
                f.setEntityClass(PeriodosEscolares.class);
                PeriodosEscolares periodo = (PeriodosEscolares) f.find(e.getPeriodo());
                l.add(periodo);
            });
            return l;
        }
    }

    @Override
    public Map<PeriodosEscolares, List<Evaluaciones360Resultados>> getEvaluaciones360PorPeriodo(Personal personal, @NonNull List<PeriodosEscolares> periodos) {
        TypedQuery<Evaluaciones360Resultados> q = f.getEntityManager().createQuery("SELECT r FROM Evaluaciones360Resultados r WHERE r.desempenioEvaluacionResultadosPK.evaluado =:evaluado", Evaluaciones360Resultados.class);
        q.setParameter("evaluado", personal.getClave());
        List<Evaluaciones360Resultados> resultados = q.getResultList();

        if (resultados.isEmpty()) {
            return null;
        }

        Map<PeriodosEscolares, List<Evaluaciones360Resultados>> map = new HashMap<>();
        periodos.stream().forEach(p -> {
            List<Evaluaciones360Resultados> l = resultados.stream().filter(r -> r.getEvaluaciones360().getPeriodo() == p.getPeriodo()).collect(Collectors.toList());
            if (!l.isEmpty()) {
                map.put(p, l);
            }
        });

        return map;
    }

    @Override
    public Map<PeriodosEscolares, DesempenioEvaluacionResultados> getEvaluacionesDesempenioPorPeriodo(Personal personal, List<PeriodosEscolares> periodos) {
        TypedQuery<DesempenioEvaluacionResultados> q = f.getEntityManager().createQuery("SELECT r FROM DesempenioEvaluacionResultados r WHERE r.desempenioEvaluacionResultadosPK.evaluado =:evaluado", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluado", personal.getClave());
        List<DesempenioEvaluacionResultados> resultados = q.getResultList();

        if (resultados.isEmpty()) {
            return null;
        }

        Map<PeriodosEscolares, DesempenioEvaluacionResultados> map = new HashMap<>();
        periodos.stream().forEach(p -> {
            List<DesempenioEvaluacionResultados> l = resultados.stream().filter(r -> r.getDesempenioEvaluaciones().getPeriodo() == p.getPeriodo()).collect(Collectors.toList());

            if (!l.isEmpty()) {
                map.put(p, l.get(0));
            }
        });

        return map;
    }

    @Override
    public Double calcularPromedio360(List<Evaluaciones360Resultados> resultados) {
        Double calificacion = 0D;
        Integer preguntas = 0;
        Double promedio;
        for (Evaluaciones360Resultados resultado : resultados) {
            if (resultado.getR1() != null) {                calificacion = calificacion + resultado.getR1();                preguntas=preguntas+1;            }
            if (resultado.getR2() != null) {                calificacion = calificacion + resultado.getR2();                preguntas=preguntas+1;            }
            if (resultado.getR3() != null) {                calificacion = calificacion + resultado.getR3();                preguntas=preguntas+1;            }
            if (resultado.getR4() != null) {                calificacion = calificacion + resultado.getR4();                preguntas=preguntas+1;            }
            if (resultado.getR5() != null) {                calificacion = calificacion + resultado.getR5();                preguntas=preguntas+1;            }
            if (resultado.getR6() != null) {                calificacion = calificacion + resultado.getR6();                preguntas=preguntas+1;            }
            if (resultado.getR7() != null) {                calificacion = calificacion + resultado.getR7();                preguntas=preguntas+1;            }
            if (resultado.getR8() != null) {                calificacion = calificacion + resultado.getR8();                preguntas=preguntas+1;            }
            if (resultado.getR9() != null) {                calificacion = calificacion + resultado.getR9();                preguntas=preguntas+1;            }
            if (resultado.getR10() != null) {               calificacion = calificacion + resultado.getR10();               preguntas=preguntas+1;            }
            if (resultado.getR11() != null) {               calificacion = calificacion + resultado.getR11();               preguntas=preguntas+1;            }
            if (resultado.getR12() != null) {               calificacion = calificacion + resultado.getR12();               preguntas=preguntas+1;            }
            if (resultado.getR13() != null) {               calificacion = calificacion + resultado.getR13();               preguntas=preguntas+1;            }
            if (resultado.getR14() != null) {               calificacion = calificacion + resultado.getR14();               preguntas=preguntas+1;            }
            if (resultado.getR15() != null) {               calificacion = calificacion + resultado.getR15();               preguntas=preguntas+1;            }
            if (resultado.getR16() != null) {               calificacion = calificacion + resultado.getR16();               preguntas=preguntas+1;            }
            if (resultado.getR17() != null) {               calificacion = calificacion + resultado.getR17();               preguntas=preguntas+1;            }
            if (resultado.getR18() != null) {               calificacion = calificacion + resultado.getR18();               preguntas=preguntas+1;            }
            if (resultado.getR19() != null) {               calificacion = calificacion + resultado.getR19();               preguntas=preguntas+1;            }
            if (resultado.getR20() != null) {               calificacion = calificacion + resultado.getR20();               preguntas=preguntas+1;            }
            if (resultado.getR21() != null) {               calificacion = calificacion + resultado.getR21();               preguntas=preguntas+1;            }
            if (resultado.getR22() != null) {               calificacion = calificacion + resultado.getR22();               preguntas=preguntas+1;            }
            if (resultado.getR23() != null) {               calificacion = calificacion + resultado.getR23();               preguntas=preguntas+1;            }
            if (resultado.getR24() != null) {               calificacion = calificacion + resultado.getR24();               preguntas=preguntas+1;            }
            if (resultado.getR25() != null) {               calificacion = calificacion + resultado.getR25();               preguntas=preguntas+1;            }
            if (resultado.getR26() != null) {               calificacion = calificacion + resultado.getR26();               preguntas=preguntas+1;            }
            if (resultado.getR27() != null) {               calificacion = calificacion + resultado.getR27();               preguntas=preguntas+1;            }
            if (resultado.getR28() != null) {               calificacion = calificacion + resultado.getR28();               preguntas=preguntas+1;            }
            if (resultado.getR29() != null) {               calificacion = calificacion + resultado.getR29();               preguntas=preguntas+1;            }
            if (resultado.getR30() != null) {               calificacion = calificacion + resultado.getR30();               preguntas=preguntas+1;            }
            if (resultado.getR31() != null) {               calificacion = calificacion + resultado.getR31();               preguntas=preguntas+1;            }
        }

        promedio = calificacion / preguntas;
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPersonalEvaluaciones.calcularPromedio360() EJB  el promedio es  = : " + promedio);
        return promedio;
    }

    @Override
    public Double calcularPromedioDesempenio(DesempenioEvaluacionResultados resultados) {
        Double calificacion = 0D;
        Double promedio;
        
        if (resultados.getR1() != null) {            calificacion = calificacion + resultados.getR1();        }
        if (resultados.getR2() != null) {            calificacion = calificacion + resultados.getR2();        }
        if (resultados.getR3() != null) {            calificacion = calificacion + resultados.getR3();        }
        if (resultados.getR4() != null) {            calificacion = calificacion + resultados.getR4();        }
        if (resultados.getR5() != null) {            calificacion = calificacion + resultados.getR5();        }
        if (resultados.getR6() != null) {            calificacion = calificacion + resultados.getR6();        }
        if (resultados.getR7() != null) {            calificacion = calificacion + resultados.getR7();        }
        if (resultados.getR8() != null) {            calificacion = calificacion + resultados.getR8();        }
        if (resultados.getR9() != null) {            calificacion = calificacion + resultados.getR9();        }
        if (resultados.getR10() != null) {           calificacion = calificacion + resultados.getR10();       }
        if (resultados.getR11() != null) {           calificacion = calificacion + resultados.getR11();       }
        if (resultados.getR12() != null) {           calificacion = calificacion + resultados.getR12();       }
        if (resultados.getR13() != null) {           calificacion = calificacion + resultados.getR13();       }
        if (resultados.getR14() != null) {           calificacion = calificacion + resultados.getR14();       }
        if (resultados.getR15() != null) {           calificacion = calificacion + resultados.getR15();       }
        if (resultados.getR16() != null) {           calificacion = calificacion + resultados.getR16();       }
        if (resultados.getR17() != null) {           calificacion = calificacion + resultados.getR17();       }
        if (resultados.getR18() != null) {           calificacion = calificacion + resultados.getR18();       }
        if (resultados.getR19() != null) {           calificacion = calificacion + resultados.getR19();       }
        if (resultados.getR20() != null) {           calificacion = calificacion + resultados.getR20();       }
        promedio = calificacion / 20;
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPersonalEvaluaciones.calcularPromedioDesempenio() EJB  el promedio es = : " + promedio);
        return promedio;
        
    }

    @Override
    public List<ListaEvaluaciones> empaquetar(Personal personal, List<PeriodosEscolares> periodos, Map<PeriodosEscolares, List<Evaluaciones360Resultados>> resultados360, Map<PeriodosEscolares, DesempenioEvaluacionResultados> resultadosDesempenio) {
        List<ListaEvaluaciones> l = new ArrayList<>();

        periodos.stream().forEach(p -> {
            System.out.println("EJB entra en el for each");
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPersonalEvaluaciones.empaquetar() EJB el periodo es p : " + p );
            ListaEvaluaciones fila = new ListaEvaluaciones(p);
            System.out.println("lista de ecaluaciones FILA EJB  = :¨" + fila);
            if (resultadosDesempenio.get(p) != null ) {
                fila.setDesempenioEvaluacionResultado(resultadosDesempenio.get(p));
                fila.setPromedioDesepenio(calcularPromedioDesempenio(fila.getDesempenioEvaluacionResultado()));
            }
            
            if(resultados360.get(p)!= null){
                fila.setEvaluacion360Resultado(resultados360.get(p));
                fila.setPromedio360(calcularPromedio360(fila.getEvaluacion360Resultado()));
            }
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPersonalEvaluaciones.empaquetar() EJB la fila = : " + fila);
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPersonalEvaluaciones.empaquetar() promedio 360 de la fila EJB : " + fila.getPromedio360());
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPersonalEvaluaciones.empaquetar() promedio desempenio de la fila EJB : " + fila.getPromedioDesepenio());
            if(fila.getPromedio360()!=null && fila.getPromedioDesepenio() != null){
            l.add(fila);
            }
        });
        System.out.println("EJB lista de resultados = : "+l);
        return l;
    }

}
