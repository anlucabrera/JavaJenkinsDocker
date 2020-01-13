package mx.edu.utxj.pye.sgi.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.TypedQuery;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesClimaLaboralResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesClimaLaboralResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorClimaLaboral;

@Stateful
public class ServicioClimaLaboral implements EjbClimaLaboral {
    private static final long serialVersionUID = -4174086992327600513L;
    @EJB Facade f;

    @Override
    public Personal getEvaluador(Integer claveNomina) {
        f.setEntityClass(Personal.class);
        return (Personal)f.find(claveNomina);
    }

    @Override
    public Evaluaciones getEvaluacionActiva() {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Clima laboral");
        q.setParameter("fecha", new Date());
        
        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }

    @Override
    public EvaluacionesClimaLaboralResultados getResultado(Evaluaciones evaluacion, Personal evaluador) {
        try {
            EvaluacionesClimaLaboralResultadosPK pk = new EvaluacionesClimaLaboralResultadosPK(evaluacion.getEvaluacion(), evaluador.getClave());
            f.setEntityClass(EvaluacionesClimaLaboralResultados.class);

            EvaluacionesClimaLaboralResultados r = (EvaluacionesClimaLaboralResultados) f.find(pk);

            if (r == null) {
                r = new EvaluacionesClimaLaboralResultados(pk);
                f.create(r);
            }

            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    @Override
    public boolean actualizarResultado(EvaluacionesClimaLaboralResultados resultado) {
        if(resultado != null){
            f.setEntityClass(EvaluacionesClimaLaboralResultados.class);
            f.edit(resultado);
        }
        
        Comparador<EvaluacionesClimaLaboralResultados> comparador = new ComparadorClimaLaboral();        
        return comparador.isCompleto(resultado); 
    }

    @Override
    public void actualizarRespuestaPorPregunta(EvaluacionesClimaLaboralResultados resultado, String pregunta, String respuesta) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(respuesta); break;
            case "p2": resultado.setR2(respuesta); break;
            case "p3": resultado.setR3(respuesta); break;
            case "p4": resultado.setR4(respuesta); break;
            case "p5": resultado.setR5(respuesta); break;
            case "p6_1": resultado.setR61(Short.parseShort(respuesta)); break;
            case "p6_2": resultado.setR62(Short.parseShort(respuesta)); break;
            case "p7": resultado.setR7(respuesta); break;
            case "p8": resultado.setR8(respuesta); break;
            case "p8_1": resultado.setR81(respuesta); break;
            case "p9": resultado.setR9(respuesta); break;
            case "p10": resultado.setR10(respuesta); break;
            case "p11": resultado.setR11(respuesta); break;
            case "p12": resultado.setR12(respuesta); break;
            case "p13": resultado.setR13(respuesta); break;
            case "p14": resultado.setR14(respuesta); break;
            case "p15": resultado.setR15(respuesta); break;
            case "p16": resultado.setR16(respuesta); break;
            case "p17": resultado.setR17(respuesta); break;
            case "p18": resultado.setR18(Short.parseShort(respuesta)); break;
            case "p19": resultado.setR19(Short.parseShort(respuesta)); break;
            case "p20": resultado.setR20(Short.parseShort(respuesta)); break;
            case "p21": resultado.setR21(Short.parseShort(respuesta)); break;
            case "p22": resultado.setR22(Short.parseShort(respuesta)); break;
            case "p23": resultado.setR23(Short.parseShort(respuesta)); break;
            case "p24": resultado.setR24(Short.parseShort(respuesta)); break;
            case "p25": resultado.setR25(Short.parseShort(respuesta)); break;
            case "p26": resultado.setR26(Short.parseShort(respuesta)); break;
            case "p27": resultado.setR27(Short.parseShort(respuesta)); break;
            case "p28": resultado.setR28(Short.parseShort(respuesta)); break;
            case "p29": resultado.setR29(Short.parseShort(respuesta)); break;
            case "p30": resultado.setR30(Short.parseShort(respuesta)); break;
            case "p31": resultado.setR31(Short.parseShort(respuesta)); break;
            case "p32": resultado.setR32(Short.parseShort(respuesta)); break;
            case "p33": resultado.setR33(Short.parseShort(respuesta)); break;
            case "p34": resultado.setR34(Short.parseShort(respuesta)); break;
            case "p35": resultado.setR35(Short.parseShort(respuesta)); break;
            case "p36": resultado.setR36(Short.parseShort(respuesta)); break;
            case "p37": resultado.setR37(Short.parseShort(respuesta)); break;
            case "p38": resultado.setR38(Short.parseShort(respuesta)); break;
            case "p39": resultado.setR39(Short.parseShort(respuesta)); break;
            case "p40": resultado.setR40(Short.parseShort(respuesta)); break;
            case "p41": resultado.setR41(Short.parseShort(respuesta)); break;
            case "p42": resultado.setR42(Short.parseShort(respuesta)); break;
            case "p43": resultado.setR43(Short.parseShort(respuesta)); break;
            case "p44": resultado.setR44(Short.parseShort(respuesta)); break;
            case "p45": resultado.setR45(Short.parseShort(respuesta)); break;
            case "p46": resultado.setR46(Short.parseShort(respuesta)); break;
            case "p47": resultado.setR47(Short.parseShort(respuesta)); break;
            case "p48": resultado.setR48(Short.parseShort(respuesta)); break;
            case "p49": resultado.setR49(Short.parseShort(respuesta)); break;
            case "p50": resultado.setR50(Short.parseShort(respuesta)); break;
            case "p51": resultado.setR51(Short.parseShort(respuesta)); break;
            case "p52": resultado.setR52(Short.parseShort(respuesta)); break;
            case "p53": resultado.setR53(Short.parseShort(respuesta)); break;
            case "p54": resultado.setR54(Short.parseShort(respuesta)); break;
            case "p55": resultado.setR55(Short.parseShort(respuesta)); break;
            case "p56": resultado.setR56(Short.parseShort(respuesta)); break;
            case "p57": resultado.setR57(Short.parseShort(respuesta)); break;
            case "p58": resultado.setR58(Short.parseShort(respuesta)); break;
            case "p59": resultado.setR59(Short.parseShort(respuesta)); break;
            case "p60": resultado.setR60(Short.parseShort(respuesta)); break;
            case "p61": resultado.setR611(Short.parseShort(respuesta)); break;
            case "p62": resultado.setR621(Short.parseShort(respuesta)); break;
            case "p63": resultado.setR63(Short.parseShort(respuesta)); break;
            case "p64": resultado.setR64(Short.parseShort(respuesta)); break;
            case "p65": resultado.setR65(Short.parseShort(respuesta)); break;
            case "p66": resultado.setR66(Short.parseShort(respuesta)); break;
            case "p67": resultado.setR67(Short.parseShort(respuesta)); break;
            case "p68": resultado.setR68(Short.parseShort(respuesta)); break;
            case "p69": resultado.setR69(Short.parseShort(respuesta)); break;
            case "p70": resultado.setR70(Short.parseShort(respuesta)); break;
            case "p71": resultado.setR71(Short.parseShort(respuesta)); break;
            case "p72": resultado.setR72(Short.parseShort(respuesta)); break;
            case "p73": resultado.setR73(Short.parseShort(respuesta)); break;
            case "p74": resultado.setR74(Short.parseShort(respuesta)); break;
        }
    }

    @Override
    public void vaciarRespuestas(@NonNull EvaluacionesClimaLaboralResultados resultado, @NonNull Map<String, String> respuestas) {
        respuestas.clear();
        respuestas.put("p1", resultado.getR1());
        respuestas.put("p2", resultado.getR2());
        respuestas.put("p3", resultado.getR3());
        respuestas.put("p4", resultado.getR4());
        respuestas.put("p5", resultado.getR5());
        respuestas.put("p6_1", resultado.getR61()!=null?resultado.getR61().toString():null);
        respuestas.put("p6_2", resultado.getR62()!=null?resultado.getR62().toString():null);
        respuestas.put("p7", resultado.getR7());
        respuestas.put("p8", resultado.getR8());
        respuestas.put("p8_1", resultado.getR81());
        respuestas.put("p9", resultado.getR9());
        respuestas.put("p10", resultado.getR10());
        respuestas.put("p11", resultado.getR11());
        respuestas.put("p12", resultado.getR12());
        respuestas.put("p13", resultado.getR13());
        respuestas.put("p14", resultado.getR14());
        respuestas.put("p15", resultado.getR15());
        respuestas.put("p16", resultado.getR16());
        respuestas.put("p17", resultado.getR17());
        respuestas.put("p18", resultado.getR18()!=null?resultado.getR18().toString():null);
        respuestas.put("p19", resultado.getR19()!=null?resultado.getR19().toString():null);
        respuestas.put("p20", resultado.getR20()!=null?resultado.getR20().toString():null);
        respuestas.put("p21", resultado.getR21()!=null?resultado.getR21().toString():null);
        respuestas.put("p22", resultado.getR22()!=null?resultado.getR22().toString():null);
        respuestas.put("p23", resultado.getR23()!=null?resultado.getR23().toString():null);
        respuestas.put("p24", resultado.getR24()!=null?resultado.getR24().toString():null);
        respuestas.put("p25", resultado.getR25()!=null?resultado.getR25().toString():null);
        respuestas.put("p26", resultado.getR26()!=null?resultado.getR26().toString():null);
        respuestas.put("p27", resultado.getR27()!=null?resultado.getR27().toString():null);
        respuestas.put("p28", resultado.getR28()!=null?resultado.getR28().toString():null);
        respuestas.put("p29", resultado.getR29()!=null?resultado.getR29().toString():null);
        respuestas.put("p30", resultado.getR30()!=null?resultado.getR30().toString():null);
        respuestas.put("p31", resultado.getR31()!=null?resultado.getR31().toString():null);
        respuestas.put("p32", resultado.getR32()!=null?resultado.getR32().toString():null);
        respuestas.put("p33", resultado.getR33()!=null?resultado.getR33().toString():null);
        respuestas.put("p34", resultado.getR34()!=null?resultado.getR34().toString():null);
        respuestas.put("p35", resultado.getR35()!=null?resultado.getR35().toString():null);
        respuestas.put("p36", resultado.getR36()!=null?resultado.getR36().toString():null);
        respuestas.put("p37", resultado.getR37()!=null?resultado.getR37().toString():null);
        respuestas.put("p38", resultado.getR38()!=null?resultado.getR38().toString():null);
        respuestas.put("p39", resultado.getR39()!=null?resultado.getR39().toString():null);
        respuestas.put("p40", resultado.getR40()!=null?resultado.getR40().toString():null);
        respuestas.put("p41", resultado.getR41()!=null?resultado.getR41().toString():null);
        respuestas.put("p42", resultado.getR42()!=null?resultado.getR42().toString():null);
        respuestas.put("p43", resultado.getR43()!=null?resultado.getR43().toString():null);
        respuestas.put("p44", resultado.getR44()!=null?resultado.getR44().toString():null);
        respuestas.put("p45", resultado.getR45()!=null?resultado.getR45().toString():null);
        respuestas.put("p46", resultado.getR46()!=null?resultado.getR46().toString():null);
        respuestas.put("p47", resultado.getR47()!=null?resultado.getR47().toString():null);
        respuestas.put("p48", resultado.getR48()!=null?resultado.getR48().toString():null);
        respuestas.put("p49", resultado.getR49()!=null?resultado.getR49().toString():null);
        respuestas.put("p50", resultado.getR50()!=null?resultado.getR50().toString():null);
        respuestas.put("p51", resultado.getR51()!=null?resultado.getR51().toString():null);
        respuestas.put("p52", resultado.getR52()!=null?resultado.getR52().toString():null);
        respuestas.put("p53", resultado.getR53()!=null?resultado.getR53().toString():null);
        respuestas.put("p54", resultado.getR54()!=null?resultado.getR54().toString():null);
        respuestas.put("p55", resultado.getR55()!=null?resultado.getR55().toString():null);
        respuestas.put("p56", resultado.getR56()!=null?resultado.getR56().toString():null);
        respuestas.put("p57", resultado.getR57()!=null?resultado.getR57().toString():null);
        respuestas.put("p58", resultado.getR58()!=null?resultado.getR58().toString():null);
        respuestas.put("p59", resultado.getR59()!=null?resultado.getR59().toString():null);
        respuestas.put("p60", resultado.getR60()!=null?resultado.getR60().toString():null);
        respuestas.put("p61", resultado.getR611()!=null?resultado.getR611().toString():null);
        respuestas.put("p62", resultado.getR621()!=null?resultado.getR621().toString():null);
        respuestas.put("p63", resultado.getR63()!=null?resultado.getR63().toString():null);
        respuestas.put("p64", resultado.getR64()!=null?resultado.getR64().toString():null);
        respuestas.put("p65", resultado.getR65()!=null?resultado.getR65().toString():null);
        respuestas.put("p66", resultado.getR66()!=null?resultado.getR66().toString():null);
        respuestas.put("p67", resultado.getR67()!=null?resultado.getR67().toString():null);
        respuestas.put("p68", resultado.getR68()!=null?resultado.getR68().toString():null);
        respuestas.put("p69", resultado.getR69()!=null?resultado.getR69().toString():null);
        respuestas.put("p70", resultado.getR70()!=null?resultado.getR70().toString():null);
        respuestas.put("p71", resultado.getR71()!=null?resultado.getR71().toString():null);
        respuestas.put("p72", resultado.getR72()!=null?resultado.getR72().toString():null);
        respuestas.put("p73", resultado.getR73()!=null?resultado.getR73().toString():null);
        respuestas.put("p74", resultado.getR74()!=null?resultado.getR74().toString():null);
    }
    
}
