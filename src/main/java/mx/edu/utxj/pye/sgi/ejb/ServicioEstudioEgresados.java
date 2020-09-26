package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioEgresados;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

@Stateful
public class ServicioEstudioEgresados implements EjbEstudioEgresados {

    @EJB
    Facade f;

    @EJB
    Facade2 f2;
    private EntityManager em;
    private  EntityManager em2;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }

    @Override
    public List<Generaciones> getGeneraciones() {
        List<Generaciones> q= em.createQuery("SELECT g from Generaciones g WHERE g.fin <= :anioActual ORDER BY g.inicio DESC, g.fin DESC", Generaciones.class)
                .setParameter("anioActual", (short) (new Date()).getYear() + 1900)
                .getResultList()
                ;
        return q;
    }

    @Override
    public Evaluaciones geteEvaluacionActiva() {
        Evaluaciones  q = new Evaluaciones();
        q = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", "Estudio egresados")
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                            .orElse(null);
        return  q;

    }

    @Override
    public Alumnos getAlumnoPorMatricula(String matricula) {
        Alumnos a= new Alumnos();
        return a = em2.createQuery("SELECT a from Alumnos as a WHERE a.matricula = :matricula ORDER BY a.gradoActual DESC", Alumnos.class)
                .setParameter("matricula", matricula.toString())
                .getResultStream()
                .findFirst()
                .orElse(null)
        ;

    }

    @Override
    public Personas getDatosPersonalesAlumnos(Integer persona) {
        Personas p = new Personas();
        return p = em2.createQuery("SELECT p from Personas as p WHERE p.personasPK.cvePersona = :persona", Personas.class)
            .setParameter("persona", persona)
            .getResultStream()
            .findFirst()
            .orElse(null);

    }

    @Override
    public EvaluacionEstudioEgresadosResultados getResultados(Evaluaciones evaluacion, Integer evaluador) {
        try {
            EvaluacionEstudioEgresadosResultadosPK pk = new EvaluacionEstudioEgresadosResultadosPK(evaluacion.getEvaluacion(), evaluador);
           // System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEstudioEgresados.getResultados() EJB llave primaria : " + pk);
            f.setEntityClass(EvaluacionEstudioEgresadosResultados.class);
            EvaluacionEstudioEgresadosResultados r = (EvaluacionEstudioEgresadosResultados) f.find(pk);
            if (r == null) {
                r = new EvaluacionEstudioEgresadosResultados(pk);
                em.persist(r);
            }
           // System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEstudioEgresados.getResultados() EJB resultados : =  " + r);
            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    @Override
    public boolean actualizarResultado(EvaluacionEstudioEgresadosResultados resultado) {
        if (resultado != null) {
            f.setEntityClass(EvaluacionEstudioEgresadosResultados.class);
            em.merge(resultado);
        }

        Comparador<EvaluacionEstudioEgresadosResultados> comparador = new ComparadorEvaluacionEstudioEgresados();
        return comparador.isCompleto(resultado);
//        return true;
    }

    @Override
    public void actualizarRespuestaPorPregunta(EvaluacionEstudioEgresadosResultados resultado, String pregunta, String respuesta) {
        //System.out.println("EJB ::: ==>>Pregunta : " + pregunta + ", respueta : " + respuesta);
        switch (pregunta.trim()) {
            // case"p0001":resultado.setR1(respuesta);break;
            case"p0001":resultado.setR1(respuesta);break;
            case"p0002":resultado.setR2(respuesta);break;
            case"p0003":resultado.setR3(respuesta);break;
            case"p0004":resultado.setR4(respuesta);break;
            case"p0005":resultado.setR5(Short.parseShort(respuesta));break;
            case"p0006":resultado.setR6(Short.parseShort(respuesta));break;
            case"p0007":resultado.setR7(Short.parseShort(respuesta));break;
            case"p0008":resultado.setR8(Short.parseShort(respuesta));break;
            case"p0009":resultado.setR9(respuesta);break;
            case"p0010":resultado.setR10(respuesta);break;
            case"p0011":resultado.setR11(respuesta);break;
            case"p0012":resultado.setR12(respuesta);break;
            case"p0013":resultado.setR13(respuesta);break;
            case"p0014":resultado.setR14(respuesta);break;
            case"p0015":resultado.setR15(respuesta);break;
            case"p0016":resultado.setR16(respuesta);break;
            case"p0017":resultado.setR17(respuesta);break;
            case"p0018":resultado.setR18(respuesta);break;
            case"p0019":resultado.setR19(respuesta);break;
            case"p0020":resultado.setR20(respuesta);break;
            case"p0021":resultado.setR21(respuesta);break;
            case"p0022":resultado.setR22(respuesta);break;
            case"p0023":resultado.setR23(respuesta);break;
            case"p0024":resultado.setR24(respuesta);break;
            case"p0025":resultado.setR25(respuesta);break;
            case"p0026":resultado.setR26(respuesta);break;
            case"p0027":resultado.setR27(respuesta);break;
            case"p0028":resultado.setR28(respuesta);break;
            case"p0029":resultado.setR29(respuesta);break;
            case"p0030":resultado.setR30(respuesta);break;
            case"p0031":resultado.setR31(respuesta);break;
            case"p0032":resultado.setR32(respuesta);break;
            case"p0033":resultado.setR33(respuesta);break;
            case"p0034":resultado.setR34(respuesta);break;
            case"p0035":resultado.setR35(respuesta);break;
            case"p0036":resultado.setR36(respuesta);break;
            case"p0037":resultado.setR37(respuesta);break;
            case"p0038":resultado.setR38(respuesta);break;
            case"p0039":resultado.setR39(respuesta);break;
            case"p0040":resultado.setR40(respuesta);break;
            case"p0041":resultado.setR41(respuesta);break;
            case"p0042":resultado.setR42(respuesta);break;
            case"p0043":resultado.setR43(respuesta);break;
            case"p0044":resultado.setR44(respuesta);break;
            case"p0045":resultado.setR45(respuesta);break;
            case"p0046":resultado.setR46(respuesta);break;
            case"p0047":resultado.setR47(respuesta);break;
            case"p0048":resultado.setR48(respuesta);break;
            case"p0049":resultado.setR49(respuesta);break;
            case"p0050":resultado.setR50(respuesta);break;
            case"p0051":resultado.setR51(respuesta);break;
            case"p0052":resultado.setR52(respuesta);break;
            case"p0053":resultado.setR53(respuesta);break;
            case"p0054":resultado.setR54(respuesta);break;
            case"p0055":resultado.setR55(respuesta);break;
            case"p0056":resultado.setR56(respuesta);break;
            case"p0057":resultado.setR57(respuesta);break;
            case"p0058":resultado.setR58(respuesta);break;
            case"p0059":resultado.setR59(respuesta);break;
            case"p0060":resultado.setR60(Short.parseShort(respuesta));break;
            case"p0061":resultado.setR61(Short.parseShort(respuesta));break;
            case"p0062":resultado.setR62(Short.parseShort(respuesta));break;
            case"p0063":resultado.setR63(Short.parseShort(respuesta));break;
            case"p0064":resultado.setR64a(Short.parseShort(respuesta));break;
            case"p0065":resultado.setR64b(Integer.parseInt(respuesta));break;
            case"p0066":resultado.setR65(Short.parseShort(respuesta));break;
            case"p0067":resultado.setR66(Short.parseShort(respuesta));break;
            case"p0068":resultado.setR67a(Short.parseShort(respuesta));break;
            case"p0069":resultado.setR67b(Short.parseShort(respuesta));break;
            case"p0070":resultado.setR67c(Short.parseShort(respuesta));break;
            case"p0071":resultado.setR67d(Short.parseShort(respuesta));break;
            case"p0072":resultado.setR67e(Short.parseShort(respuesta));break;
            case"p0073":resultado.setR67f(Short.parseShort(respuesta));break;
            case"p0074":resultado.setR68(respuesta);break;
            case"p0075":resultado.setR69(respuesta);break;
            case"p0076":resultado.setR70(respuesta);break;
            case"p0077":resultado.setR71(respuesta);break;
            case"p0078":resultado.setR72(respuesta);break;
            case"p0079":resultado.setR73(respuesta);break;
            case"p0080":resultado.setR74(respuesta);break;
            case"p0081":resultado.setR75(respuesta);break;
            case"p0082":resultado.setR76(respuesta);break;
            case"p0083":resultado.setR77(respuesta);break;
            case"p0084":resultado.setR78(Short.parseShort(respuesta));break;
            case"p0085":resultado.setR79(Short.parseShort(respuesta));break;
            case"p0086":resultado.setR80(Short.parseShort(respuesta));break;
            case"p0087":resultado.setR81(Short.parseShort(respuesta));break;
            case"p0088":resultado.setR82(Short.parseShort(respuesta));break;
            case"p0089":resultado.setR83(Short.parseShort(respuesta));break;
            case"p0090":resultado.setR84(Short.parseShort(respuesta));break;
            case"p0091":resultado.setR85(Short.parseShort(respuesta));break;
            case"p0092":resultado.setR86a(Short.parseShort(respuesta));break;
            case"p0093":resultado.setR86b(Short.parseShort(respuesta));break;
            case"p0094":resultado.setR86c(Short.parseShort(respuesta));break;
            case"p0095":resultado.setR86d(Short.parseShort(respuesta));break;
            case"p0096":resultado.setR86e(Short.parseShort(respuesta));break;
            case"p0097":resultado.setR86f(Short.parseShort(respuesta));break;
            case"p0098":resultado.setR86g(Short.parseShort(respuesta));break;
            case"p0099":resultado.setR86h(Short.parseShort(respuesta));break;
            case"p0100":resultado.setR86i(Short.parseShort(respuesta));break;
            case"p0101":resultado.setR86j(Short.parseShort(respuesta));break;
            case"p0102":resultado.setR86k(Short.parseShort(respuesta));break;
            case"p0103":resultado.setR86l(Short.parseShort(respuesta));break;
            case"p0104":resultado.setR87(Short.parseShort(respuesta));break;
            case"p0105":resultado.setR88(Short.parseShort(respuesta));break;
            case"p0106":resultado.setR89(Short.parseShort(respuesta));break;
            case"p0107":resultado.setR90aa(Short.parseShort(respuesta));break;
            case"p0108":resultado.setR90ab(Short.parseShort(respuesta));break;
            case"p0109":resultado.setR90ba(Short.parseShort(respuesta));break;
            case"p0110":resultado.setR90bb(Short.parseShort(respuesta));break;
            case"p0111":resultado.setR90ca(Short.parseShort(respuesta));break;
            case"p0112":resultado.setR90cb(Short.parseShort(respuesta));break;
            case"p0113":resultado.setR90da(Short.parseShort(respuesta));break;
            case"p0114":resultado.setR90db(Short.parseShort(respuesta));break;
            case"p0115":resultado.setR90ea(Short.parseShort(respuesta));break;
            case"p0116":resultado.setR90eb(Short.parseShort(respuesta));break;
            case"p0117":resultado.setR90fa(Short.parseShort(respuesta));break;
            case"p0118":resultado.setR90fb(Short.parseShort(respuesta));break;
            case"p0119":resultado.setR90ga(Short.parseShort(respuesta));break;
            case"p0120":resultado.setR90gb(Short.parseShort(respuesta));break;
            case"p0121":resultado.setR90ha(Short.parseShort(respuesta));break;
            case"p0122":resultado.setR90hb(Short.parseShort(respuesta));break;
            case"p0123":resultado.setR90ia(Short.parseShort(respuesta));break;
            case"p0124":resultado.setR90ib(Short.parseShort(respuesta));break;
            case"p0125":resultado.setR90ja(Short.parseShort(respuesta));break;
            case"p0126":resultado.setR90jb(Short.parseShort(respuesta));break;
            case"p0127":resultado.setR90ka(Short.parseShort(respuesta));break;
            case"p0128":resultado.setR90kb(Short.parseShort(respuesta));break;
            case"p0129":resultado.setR915a(Short.parseShort(respuesta));break;
            case"p0130":resultado.setR915b(Short.parseShort(respuesta));break;
            case"p0131":resultado.setR915c(Short.parseShort(respuesta));break;
            case"p0132":resultado.setR915d(Short.parseShort(respuesta));break;
            case"p0133":resultado.setR915e(Short.parseShort(respuesta));break;
            case"p0134":resultado.setR915f(Short.parseShort(respuesta));break;
            case"p0135":resultado.setR915g(Short.parseShort(respuesta));break;
            case"p0136":resultado.setR915h(Short.parseShort(respuesta));break;
            case"p0137":resultado.setR915i(Short.parseShort(respuesta));break;
            case"p0138":resultado.setR915j(Short.parseShort(respuesta));break;
            case"p0139":resultado.setR91a(Short.parseShort(respuesta));break;
            case"p0140":resultado.setR91b(Short.parseShort(respuesta));break;
            case"p0141":resultado.setR91c(Short.parseShort(respuesta));break;
            case"p0142":resultado.setR91d(Short.parseShort(respuesta));break;
            case"p0143":resultado.setR91e(Short.parseShort(respuesta));break;
            case"p0144":resultado.setR91f(Short.parseShort(respuesta));break;
            case"p0145":resultado.setR91g(Short.parseShort(respuesta));break;
            case"p0146":resultado.setR91h(Short.parseShort(respuesta));break;
            case"p0147":resultado.setR91i(Short.parseShort(respuesta));break;
            case"p0148":resultado.setR91j(Short.parseShort(respuesta));break;
            case"p0149":resultado.setR91k(Short.parseShort(respuesta));break;
            case"p0150":resultado.setR91l(Short.parseShort(respuesta));break;
            case"p0151":resultado.setR91m(Short.parseShort(respuesta));break;
            case"p0152":resultado.setR92(Short.parseShort(respuesta));break;
            case"p0153":resultado.setR93(Short.parseShort(respuesta));break;
            case"p0154":resultado.setR94(respuesta);break;
            case"p0155":resultado.setR95(respuesta);break;

        }
    }

    @Override
    public void vaciarRespuestas(@NonNull EvaluacionEstudioEgresadosResultados resultado, @NonNull Map<String, String> respuestas) {
        respuestas.clear();

        //respuestas.put("p0001", resultado.getR1());
        respuestas.put("p0001", resultado.getR1());
        respuestas.put("p0002", resultado.getR2());
        respuestas.put("p0003", resultado.getR3());
        respuestas.put("p0004", resultado.getR4());
        respuestas.put("p0005", resultado.getR5() != null ? resultado.getR5().toString() : null);
        respuestas.put("p0006", resultado.getR6() != null ? resultado.getR6().toString() : null);
        respuestas.put("p0007", resultado.getR7() != null ? resultado.getR7().toString() : null);
        respuestas.put("p0008", resultado.getR8() != null ? resultado.getR8().toString() : null);
        respuestas.put("p0009", resultado.getR9());
        respuestas.put("p0010", resultado.getR10());
        respuestas.put("p0011", resultado.getR11());
        respuestas.put("p0012", resultado.getR12());
        respuestas.put("p0013", resultado.getR13());
        respuestas.put("p0014", resultado.getR14());
        respuestas.put("p0015", resultado.getR15());
        respuestas.put("p0016", resultado.getR16());
        respuestas.put("p0017", resultado.getR17());
        respuestas.put("p0018", resultado.getR18());
        respuestas.put("p0019", resultado.getR19());
        respuestas.put("p0020", resultado.getR20());
        respuestas.put("p0021", resultado.getR21());
        respuestas.put("p0022", resultado.getR22());
        respuestas.put("p0023", resultado.getR23());
        respuestas.put("p0024", resultado.getR24());
        respuestas.put("p0025", resultado.getR25());
        respuestas.put("p0026", resultado.getR26());
        respuestas.put("p0027", resultado.getR27());
        respuestas.put("p0028", resultado.getR28());
        respuestas.put("p0029", resultado.getR29());
        respuestas.put("p0030", resultado.getR30());
        respuestas.put("p0031", resultado.getR31());
        respuestas.put("p0032", resultado.getR32());
        respuestas.put("p0033", resultado.getR33());
        respuestas.put("p0034", resultado.getR34());
        respuestas.put("p0035", resultado.getR35());
        respuestas.put("p0036", resultado.getR36());
        respuestas.put("p0037", resultado.getR37());
        respuestas.put("p0038", resultado.getR38());
        respuestas.put("p0039", resultado.getR39());
        respuestas.put("p0040", resultado.getR40());
        respuestas.put("p0041", resultado.getR41());
        respuestas.put("p0042", resultado.getR42());
        respuestas.put("p0043", resultado.getR43());
        respuestas.put("p0044", resultado.getR44());
        respuestas.put("p0045", resultado.getR45());
        respuestas.put("p0046", resultado.getR46());
        respuestas.put("p0047", resultado.getR47());
        respuestas.put("p0048", resultado.getR48());
        respuestas.put("p0049", resultado.getR49());
        respuestas.put("p0050", resultado.getR50());
        respuestas.put("p0051", resultado.getR51());
        respuestas.put("p0052", resultado.getR52());
        respuestas.put("p0053", resultado.getR53());
        respuestas.put("p0054", resultado.getR54());
        respuestas.put("p0055", resultado.getR55());
        respuestas.put("p0056", resultado.getR56());
        respuestas.put("p0057", resultado.getR57());
        respuestas.put("p0058", resultado.getR58());
        respuestas.put("p0059", resultado.getR59());
        respuestas.put("p0060", resultado.getR60() != null ? resultado.getR60().toString() : null);
        respuestas.put("p0061", resultado.getR61() != null ? resultado.getR61().toString() : null);
        respuestas.put("p0062", resultado.getR62() != null ? resultado.getR62().toString() : null);
        respuestas.put("p0063", resultado.getR63() != null ? resultado.getR63().toString() : null);
        respuestas.put("p0064", resultado.getR64a() != null ? resultado.getR64a().toString() : null);
        respuestas.put("p0065", resultado.getR64b() != null ? resultado.getR64b().toString() : null);
        respuestas.put("p0066", resultado.getR65() != null ? resultado.getR65().toString() : null);
        respuestas.put("p0067", resultado.getR66() != null ? resultado.getR66().toString() : null);
        respuestas.put("p0068", resultado.getR67a() != null ? resultado.getR67a().toString() : null);
        respuestas.put("p0069", resultado.getR67b() != null ? resultado.getR67b().toString() : null);
        respuestas.put("p0070", resultado.getR67c() != null ? resultado.getR67c().toString() : null);
        respuestas.put("p0071", resultado.getR67d() != null ? resultado.getR67d().toString() : null);
        respuestas.put("p0072", resultado.getR67e() != null ? resultado.getR67e().toString() : null);
        respuestas.put("p0073", resultado.getR67f() != null ? resultado.getR67f().toString() : null);
        respuestas.put("p0074", resultado.getR68());
        respuestas.put("p0075", resultado.getR69());
        respuestas.put("p0076", resultado.getR70());
        respuestas.put("p0077", resultado.getR71());
        respuestas.put("p0078", resultado.getR72());
        respuestas.put("p0079", resultado.getR73());
        respuestas.put("p0080", resultado.getR74());
        respuestas.put("p0081", resultado.getR75());
        respuestas.put("p0082", resultado.getR76());
        respuestas.put("p0083", resultado.getR77());
        respuestas.put("p0084", resultado.getR78() != null ? resultado.getR78().toString() : null);
        respuestas.put("p0085", resultado.getR79() != null ? resultado.getR79().toString() : null);
        respuestas.put("p0086", resultado.getR80() != null ? resultado.getR80().toString() : null);
        respuestas.put("p0087", resultado.getR81() != null ? resultado.getR81().toString() : null);
        respuestas.put("p0088", resultado.getR82() != null ? resultado.getR82().toString() : null);
        respuestas.put("p0089", resultado.getR83() != null ? resultado.getR83().toString() : null);
        respuestas.put("p0090", resultado.getR84() != null ? resultado.getR84().toString() : null);
        respuestas.put("p0091", resultado.getR85() != null ? resultado.getR85().toString() : null);
        respuestas.put("p0092", resultado.getR86a() != null ? resultado.getR86a().toString() : null);
        respuestas.put("p0093", resultado.getR86b() != null ? resultado.getR86b().toString() : null);
        respuestas.put("p0094", resultado.getR86c() != null ? resultado.getR86c().toString() : null);
        respuestas.put("p0095", resultado.getR86d() != null ? resultado.getR86d().toString() : null);
        respuestas.put("p0096", resultado.getR86e() != null ? resultado.getR86e().toString() : null);
        respuestas.put("p0097", resultado.getR86f() != null ? resultado.getR86f().toString() : null);
        respuestas.put("p0098", resultado.getR86g() != null ? resultado.getR86g().toString() : null);
        respuestas.put("p0099", resultado.getR86h() != null ? resultado.getR86h().toString() : null);
        respuestas.put("p0100", resultado.getR86i() != null ? resultado.getR86i().toString() : null);
        respuestas.put("p0101", resultado.getR86j() != null ? resultado.getR86j().toString() : null);
        respuestas.put("p0102", resultado.getR86k() != null ? resultado.getR86k().toString() : null);
        respuestas.put("p0103", resultado.getR86l() != null ? resultado.getR86l().toString() : null);
        respuestas.put("p0104", resultado.getR87() != null ? resultado.getR87().toString() : null);
        respuestas.put("p0105", resultado.getR88() != null ? resultado.getR88().toString() : null);
        respuestas.put("p0106", resultado.getR89() != null ? resultado.getR89().toString() : null);
        respuestas.put("p0107", resultado.getR90aa() != null ? resultado.getR90aa().toString() : null);
        respuestas.put("p0108", resultado.getR90ab() != null ? resultado.getR90ab().toString() : null);
        respuestas.put("p0109", resultado.getR90ba() != null ? resultado.getR90ba().toString() : null);
        respuestas.put("p0110", resultado.getR90bb() != null ? resultado.getR90bb().toString() : null);
        respuestas.put("p0111", resultado.getR90ca() != null ? resultado.getR90ca().toString() : null);
        respuestas.put("p0112", resultado.getR90cb() != null ? resultado.getR90cb().toString() : null);
        respuestas.put("p0113", resultado.getR90da() != null ? resultado.getR90da().toString() : null);
        respuestas.put("p0114", resultado.getR90db() != null ? resultado.getR90db().toString() : null);
        respuestas.put("p0115", resultado.getR90ea() != null ? resultado.getR90ea().toString() : null);
        respuestas.put("p0116", resultado.getR90eb() != null ? resultado.getR90eb().toString() : null);
        respuestas.put("p0117", resultado.getR90fa() != null ? resultado.getR90fa().toString() : null);
        respuestas.put("p0118", resultado.getR90fb() != null ? resultado.getR90fb().toString() : null);
        respuestas.put("p0119", resultado.getR90ga() != null ? resultado.getR90ga().toString() : null);
        respuestas.put("p0120", resultado.getR90gb() != null ? resultado.getR90gb().toString() : null);
        respuestas.put("p0121", resultado.getR90ha() != null ? resultado.getR90ha().toString() : null);
        respuestas.put("p0122", resultado.getR90hb() != null ? resultado.getR90hb().toString() : null);
        respuestas.put("p0123", resultado.getR90ia() != null ? resultado.getR90ia().toString() : null);
        respuestas.put("p0124", resultado.getR90ib() != null ? resultado.getR90ib().toString() : null);
        respuestas.put("p0125", resultado.getR90ja() != null ? resultado.getR90ja().toString() : null);
        respuestas.put("p0126", resultado.getR90jb() != null ? resultado.getR90jb().toString() : null);
        respuestas.put("p0127", resultado.getR90ka() != null ? resultado.getR90ka().toString() : null);
        respuestas.put("p0128", resultado.getR90kb() != null ? resultado.getR90kb().toString() : null);
        respuestas.put("p0129", resultado.getR915a() != null ? resultado.getR915a().toString() : null);
        respuestas.put("p0130", resultado.getR915b() != null ? resultado.getR915b().toString() : null);
        respuestas.put("p0131", resultado.getR915c() != null ? resultado.getR915c().toString() : null);
        respuestas.put("p0132", resultado.getR915d() != null ? resultado.getR915d().toString() : null);
        respuestas.put("p0133", resultado.getR915e() != null ? resultado.getR915e().toString() : null);
        respuestas.put("p0134", resultado.getR915f() != null ? resultado.getR915f().toString() : null);
        respuestas.put("p0135", resultado.getR915g() != null ? resultado.getR915g().toString() : null);
        respuestas.put("p0136", resultado.getR915h() != null ? resultado.getR915h().toString() : null);
        respuestas.put("p0137", resultado.getR915i() != null ? resultado.getR915i().toString() : null);
        respuestas.put("p0138", resultado.getR915j() != null ? resultado.getR915j().toString() : null);
        respuestas.put("p0139", resultado.getR91a() != null ? resultado.getR91a().toString() : null);
        respuestas.put("p0140", resultado.getR91b() != null ? resultado.getR91b().toString() : null);
        respuestas.put("p0141", resultado.getR91c() != null ? resultado.getR91c().toString() : null);
        respuestas.put("p0142", resultado.getR91d() != null ? resultado.getR91d().toString() : null);
        respuestas.put("p0143", resultado.getR91e() != null ? resultado.getR91e().toString() : null);
        respuestas.put("p0144", resultado.getR91f() != null ? resultado.getR91f().toString() : null);
        respuestas.put("p0145", resultado.getR91g() != null ? resultado.getR91g().toString() : null);
        respuestas.put("p0146", resultado.getR91h() != null ? resultado.getR91h().toString() : null);
        respuestas.put("p0147", resultado.getR91i() != null ? resultado.getR91i().toString() : null);
        respuestas.put("p0148", resultado.getR91j() != null ? resultado.getR91j().toString() : null);
        respuestas.put("p0149", resultado.getR91k() != null ? resultado.getR91k().toString() : null);
        respuestas.put("p0150", resultado.getR91l() != null ? resultado.getR91l().toString() : null);
        respuestas.put("p0151", resultado.getR91m() != null ? resultado.getR91m().toString() : null);
        respuestas.put("p0152", resultado.getR92() != null ? resultado.getR92().toString() : null);
        respuestas.put("p0153", resultado.getR93() != null ? resultado.getR93().toString() : null);
        respuestas.put("p0154", resultado.getR94());
        respuestas.put("p0155", resultado.getR95());

    }

    public List<ProgramasEducativos> getProgramasEducativos() {
        List<ProgramasEducativos> l = new ArrayList<>();
        l = em.createQuery("SELECT p FROM ProgramasEducativos p", ProgramasEducativos.class)
                .getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<SelectItem> selectItemsProgramasEducativos() {
        List<SelectItem> lpe = new ArrayList<>();
        for (ProgramasEducativos p : getProgramasEducativos()) {
            lpe.add(new SelectItem(p.getSiglas(), p.getNombre(), p.getNombre()));
        }
        return lpe;
    }

    @Override
    public Alumnos getEstudianteSaiiut(String matricula) {
        Alumnos a = new Alumnos();
        return a  = em2.createQuery("SELECT e FROM Alumnos e WHERE e.matricula= :matricula ORDER BY e.fechaAlta DESC", Alumnos.class)
                .setParameter("matricula", matricula)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Personas getPersonaSaiiut(Integer clave) {
        Personas p = new Personas();
        return p = em2.createQuery("SELECT p from Personas AS p WHERE p.personasPK.cvePersona = :clave", Personas.class)
                .setParameter("clave", clave)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public EvaluacionEstudioEgresadosResultados getResultadoIndividual(String evaluador) {
        EvaluacionEstudioEgresadosResultados e = new EvaluacionEstudioEgresadosResultados();
       return e = em.createQuery(""
                + "SELECT e from EvaluacionEstudioEgresadosResultados as e WHERE e.evaluacionEstudioEgresadosResultadosPK.evaluador = :evaluador "
                + "ORDER BY e.evaluacionEstudioEgresadosResultadosPK.evaluacion DESC", EvaluacionEstudioEgresadosResultados.class)
                .setParameter("evaluador", Integer.parseInt(evaluador))
               .getResultStream()
               .findFirst()
               .orElse(null);
    }

    @Override
    public Evaluaciones getLastEvaluacion() {
        Evaluaciones e = new Evaluaciones();
       return e = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo ORDER BY e.evaluacion desc", Evaluaciones.class)
                .setParameter("tipo", "Estudio egresados")
               .getResultStream()
               .findFirst()
               .orElse(null);
    }

    @Override
    public List<EvaluacionEstudioEgresadosResultados> getRestultadosEgresados() {
        List<EvaluacionEstudioEgresadosResultados> r = new ArrayList<>();
        r = em.createQuery("SELECT e FROM EvaluacionEstudioEgresadosResultados e", EvaluacionEstudioEgresadosResultados.class)
                .getResultList();
        if (r.isEmpty() || r == null) {
            return null;
        } else {
            return r;
        }

    }

    @Override
    public List<EvaluacionEstudioEgresadosResultados> getResultadosPorGeneracionTSU(String generacion) {
        List<EvaluacionEstudioEgresadosResultados> r = new ArrayList<>();
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEstudioEgresados.getResultadosPorGeneracionTSU() la generacion que entra ; " + generacion);
         r = em.createQuery("SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r2 = :generacion", EvaluacionEstudioEgresadosResultados.class)
                .setParameter("generacion", generacion)
                .getResultList();
        if (r.isEmpty() || r == null) {
            return null;
        } else {
            return r;
        }
    }

    @Override
    public List<EvaluacionEstudioEgresadosResultados> getResultadosPorGeneracionING(String generacion) {
        //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEstudioEgresados.getResultadosPorGeneracionTSU() la generacion que entra ; " + generacion);
        List<EvaluacionEstudioEgresadosResultados> e = new ArrayList<>();
         e = em.createQuery("SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r3 = :generacion", EvaluacionEstudioEgresadosResultados.class)
                .setParameter("generacion", generacion)
                .getResultList();
        if (e.isEmpty() || e == null) {
            return null;
        } else {
            return e;
        }
    }

    @Override
    public List<EvaluacionEstudioEgresadosResultados> getResultadosPorSilgas(String siglas) {
       // System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEstudioEgresados.getResultadosPorSilgas() las siglas que entran son : " + siglas);
        List<EvaluacionEstudioEgresadosResultados> r = new ArrayList<>();
        r= em.createQuery("SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r4 = :siglas", EvaluacionEstudioEgresadosResultados.class)
                .setParameter("siglas", siglas)
                .getResultList();

        if (r.isEmpty() || r == null) {
            return null;
        } else {
            return r;
        }
    }

    @Override
    public List<EvaluacionEstudioEgresadosResultados> getResultadosEvActiva(Integer evaluacion) {
        List<EvaluacionEstudioEgresadosResultados> e = new ArrayList<>();
        e = em.createQuery("SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.evaluacionEstudioEgresadosResultadosPK.evaluacion=:evaluacion", EvaluacionEstudioEgresadosResultados.class)
                .setParameter("evaluacion", evaluacion)
                .getResultList();

        //System.out.println("Tamaño de la lista en ejb de resultados de la evaluacion activa"+e.size());
        if (e.isEmpty() || e == null) {
            return new ArrayList<>();
        } else {
            return e;
        }
    }

    @Override
    public Alumnos procedimiento(String matricula) {
        Short grado = 11;
        Short grado2= 6;
        Integer periodo =54;

        List<Alumnos> a = em2
                .createQuery("select a from Alumnos as a "
                        + "inner join Grupos as g on a.grupos.gruposPK.cveGrupo = g.gruposPK.cveGrupo "
                        + "where (g.gruposPK.cvePeriodo = :periodo or g.gruposPK.cvePeriodo=:periodo2) and (a.cveStatus = :estatus1 or a.cveStatus = :estatus2) and a.matricula = :matricula and (a.gradoActual = :grado or a.gradoActual=:grado2)", Alumnos.class)
                .setParameter("periodo", 53)
                .setParameter("periodo2",54)
                .setParameter("estatus1", 1)
                .setParameter("estatus2", 6)
                .setParameter("matricula", matricula)
                .setParameter("grado", grado)
                .setParameter("grado2",grado2)
                .getResultStream().collect(Collectors.toList());
       // a.forEach(x -> System.out.println(x.getMatricula() + "-" + x.getGrupos().getGruposPK().getCvePeriodo()));
        if (!a.isEmpty()) {
            return a.get(0);
        } else {
            return null;
        }
    }

}
