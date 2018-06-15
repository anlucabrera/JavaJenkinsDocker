package mx.edu.utxj.pye.sgi.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionControlInterno;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesControlInternoResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesControlInternoResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class ServicioEvaluacionControlInterno implements EjbEvaluacionControlInterno {
    private static final long serialVersionUID = 3889797314203721296L;
    
    @EJB Facade f;

    @Override
    public Personal getEvaluador(Integer claveNomina) {
        f.setEntityClass(Personal.class);
        return (Personal)f.find(claveNomina);
    }

    @Override
    public Evaluaciones getEvaluacionActiva() {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Control interno");
        q.setParameter("fecha", new Date());
        
        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }

    @Override
    public EvaluacionesControlInternoResultados getResultado(Evaluaciones evaluacion, Personal evaluador) {
        try {
            EvaluacionesControlInternoResultadosPK pk = new EvaluacionesControlInternoResultadosPK(evaluacion.getEvaluacion(), evaluador.getClave());
            f.setEntityClass(EvaluacionesControlInternoResultados.class);

            EvaluacionesControlInternoResultados r = (EvaluacionesControlInternoResultados) f.find(pk);

            if (r == null) {
                r = new EvaluacionesControlInternoResultados(pk);
                f.create(r);
            }

            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    @Override
    public boolean actualizarResultado(EvaluacionesControlInternoResultados resultado) {
        if(resultado != null){
            f.setEntityClass(EvaluacionesControlInternoResultados.class);
            f.edit(resultado);
        }
        
        Comparador<EvaluacionesControlInternoResultados> comparador = new ComparadorEvaluacionControlInterno();        
        return comparador.isCompleto(resultado);
    }

    @Override
    public void actualizarRespuestaPorPregunta(EvaluacionesControlInternoResultados resultado, String pregunta, String respuesta) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(respuesta); break;
            case "p2":
                resultado.setR2(respuesta);
                break;
            case "p3":
                resultado.setR3(respuesta);
                break;
            case "p4":
                resultado.setR4(respuesta);
                break;
            case "p5":
                resultado.setR5(respuesta);
                break;
            case "p6":
                resultado.setR6(respuesta);
                break;
            case "p7":
                resultado.setR7(respuesta);
                break;
            case "p8":
                resultado.setR8(respuesta);
                break;
            case "p9":
                resultado.setR9(respuesta);
                break;
            case "p10":
                resultado.setR10(respuesta);
                break;
            case "p11":
                resultado.setR11(respuesta);
                break;
            case "p12":
                resultado.setR12(respuesta);
                break;
            case "p13":
                resultado.setR13(respuesta);
                break;
            case "p14":
                resultado.setR14(respuesta);
                break;
            case "p15":
                resultado.setR15(respuesta);
                break;
            case "p16":
                resultado.setR16(respuesta);
                break;
            case "p17":
                resultado.setR17(respuesta);
                break;
            case "p18":
                resultado.setR18(respuesta);
                break;
            case "p19":
                resultado.setR19(respuesta);
                break;
            case "p20":
                resultado.setR20(respuesta);
                break;
            case "p21":
                resultado.setR21(respuesta);
                break;
            case "p22":
                resultado.setR22(respuesta);
                break;
            case "p23":
                resultado.setR23(respuesta);
                break;
            case "p24":
                resultado.setR24(respuesta);
                break;
            case "p25":
                resultado.setR25(respuesta);
                break;
            case "p26":
                resultado.setR26(respuesta);
                break;
            case "p27":
                resultado.setR27(respuesta);
                break;
        }
    }

}
