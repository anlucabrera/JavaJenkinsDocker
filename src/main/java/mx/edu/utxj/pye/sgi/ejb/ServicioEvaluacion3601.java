package mx.edu.utxj.pye.sgi.ejb;

import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360ResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Habilidades;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360Reporte;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioEvaluacion3601 implements EjbEvaluacion3601 {

    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("4", "E", "Excelente"));
        l.add(new SelectItem("3", "B", "Bueno"));
        l.add(new SelectItem("2", "R", "Regular"));
        l.add(new SelectItem("1", "D", "Deficiente"));

        return l;
    }

    @Override
    public List<Apartado> getApartados() {
        List<Apartado> l = new ArrayList<>();

        Apartado a1 = new Apartado(1F, "DESEMPEÑO LABORAL", new ArrayList<>());
        a1.getPreguntas().add(new Opciones(2, 1.0f, 1.0f, "Responsabilidad", ""));
        a1.getPreguntas().add(new Opciones(2, 1.0f, 2.0f, "Exactitud y calidad en el trabajo", ""));
        a1.getPreguntas().add(new Opciones(2, 1.0f, 3.0f, "Productividad", ""));
        a1.getPreguntas().add(new Opciones(2, 1.0f, 4.0f, "Orden en el trabajo", ""));
        a1.getPreguntas().add(new Opciones(2, 1.0f, 5.0f, "Planificación del trabajo", ""));
        a1.getPreguntas().add(new Opciones(2, 1.0f, 6.0f, "Comprensión de situaciones", ""));

        Apartado a2 = new Apartado(2F, "ACTITUD HACIA LA INSTITUCIÓN", new ArrayList<>());
        a2.getPreguntas().add(new Opciones(2, 2.0f, 7.0f, "Actitud hacia la institución", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 8.0f, "Actitud hacia los superiores", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 9.0f, "Actitud hacia los compañeros", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 10.0f, "Actitud hacia el cliente", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 11.0f, "Cooperación con el equipo de trabajo", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 12.0f, "Capacidad para aceptar críticas", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 13.0f, "Capacidad para generar sugerencias constructivas", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 14.0f, "Presentación personal", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 15.0f, "Disposición", ""));
        a2.getPreguntas().add(new Opciones(2, 2.0f, 16.0f, "Puntualidad", ""));

        l.add(a1);
        l.add(a2);

        return l;
    }

    @Override
    public List<ListaPersonal> getListaDirectivos() {
        TypedQuery<ListaPersonal> q = em.createQuery("select lp from ListaPersonal lp where lp.actividad = 2 order by lp.nombre", ListaPersonal.class);

        return q.getResultList();
    }

    @Override
    public List<ListaPersonal> getListaSubordinados(ListaPersonal directivo) {
        Evaluaciones360 eva = evaluacionActiva();
        if (eva != null) {
            StoredProcedureQuery q = em.createStoredProcedureQuery("obtener_lista_personal_evaluar_360", ListaPersonal.class).
                    registerStoredProcedureParameter("par_evaluacion", Integer.class, ParameterMode.IN).setParameter("par_evaluacion", eva.getEvaluacion()).
                    registerStoredProcedureParameter("par_evaluador", Integer.class, ParameterMode.IN).setParameter("par_evaluador", directivo.getClave());

                return q.getResultList();
            }

        return null;
    }

    @Override
    public Evaluaciones360 evaluacionActiva() {
        StoredProcedureQuery spq = em.createStoredProcedureQuery("buscar_evaluacion_360_activa", Evaluaciones360.class);
        List<Evaluaciones360> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public void actualizarRespuestaPorPregunta(Evaluaciones360Resultados resultado, Float pregunta, String respuesta) {
        switch (pregunta.toString()) {
            case "1.0":
                resultado.setR1(Short.parseShort(respuesta));
                break;
            case "2.0":
                resultado.setR2(Short.parseShort(respuesta));
                break;
            case "3.0":
                resultado.setR3(Short.parseShort(respuesta));
                break;
            case "4.0":
                resultado.setR4(Short.parseShort(respuesta));
                break;
            case "5.0":
                resultado.setR5(Short.parseShort(respuesta));
                break;
            case "6.0":
                resultado.setR6(Short.parseShort(respuesta));
                break;
            case "7.0":
                resultado.setR7(Short.parseShort(respuesta));
                break;
            case "8.0":
                resultado.setR8(Short.parseShort(respuesta));
                break;
            case "9.0":
                resultado.setR9(Short.parseShort(respuesta));
                break;
            case "10.0":
                resultado.setR10(Short.parseShort(respuesta));
                break;
            case "11.0":
                resultado.setR11(Short.parseShort(respuesta));
                break;
            case "12.0":
                resultado.setR12(Short.parseShort(respuesta));
                break;
            case "13.0":
                resultado.setR13(Short.parseShort(respuesta));
                break;
            case "14.0":
                resultado.setR14(Short.parseShort(respuesta));
                break;
            case "15.0":
                resultado.setR15(Short.parseShort(respuesta));
                break;
            case "16.0":
                resultado.setR16(Short.parseShort(respuesta));
                break;
            case "17.0":
                resultado.setR17(Short.parseShort(respuesta));
                break;
            case "18.0":
                resultado.setR18(Short.parseShort(respuesta));
                break;
            case "19.0":
                resultado.setR19(Short.parseShort(respuesta));
                break;
            case "20.0":
                resultado.setR20(Short.parseShort(respuesta));
                break;
            case "21.0":
                resultado.setR21(Short.parseShort(respuesta));
                break;
            case "22.0":
                resultado.setR22(Short.parseShort(respuesta));
                break;
            case "23.0":
                resultado.setR23(Short.parseShort(respuesta));
                break;
            case "24.0":
                resultado.setR24(Short.parseShort(respuesta));
                break;
            case "25.0":
                resultado.setR25(Short.parseShort(respuesta));
                break;
            case "26.0":
                resultado.setR26(Short.parseShort(respuesta));
                break;
            case "27.0":
                resultado.setR27(Short.parseShort(respuesta));
                break;
            case "28.0":
                resultado.setR28(Short.parseShort(respuesta));
                break;
            case "29.0":
                resultado.setR29(Short.parseShort(respuesta));
                break;
            case "30.0":
                resultado.setR30(Short.parseShort(respuesta));
                break;
            case "31.0":
                resultado.setR31(Short.parseShort(respuesta));
                break;
            case "32.0":
                resultado.setR32(respuesta);
                break;
            case "33.0":
                resultado.setR33(respuesta);
                break;
        }
    }

    @Override
    public void cargarResultadosAlmacenados(Evaluaciones360 evaluacion, ListaPersonal directivo, List<ListaPersonal> subordinados) {
        try {
            if(subordinados == null || subordinados.isEmpty()) return;

            List<Integer> claves = new ArrayList<>();
            subordinados.forEach((lp) -> {
                claves.add(lp.getClave());
            });


            TypedQuery<Evaluaciones360Resultados> q = em.createQuery("select a from Evaluaciones360Resultados a where a.evaluaciones360ResultadosPK.evaluacion=:evaluacion and a.evaluaciones360ResultadosPK.evaluador = :evaluador and a.evaluaciones360ResultadosPK.evaluado in :claves", Evaluaciones360Resultados.class);
            q.setParameter("evaluacion", evaluacion.getEvaluacion());
            q.setParameter("evaluador", directivo.getClave());
            q.setParameter("claves", claves);

            List<Evaluaciones360Resultados> l = q.getResultList();

            evaluacion.setEvaluaciones360ResultadosList(new ArrayList<>());
            subordinados.forEach((subordinado) -> {
                Evaluaciones360ResultadosPK pk = new Evaluaciones360ResultadosPK(evaluacion.getEvaluacion(), directivo.getClave(), subordinado.getClave());
                Evaluaciones360Resultados der = new Evaluaciones360Resultados(pk);

                if (l.contains(der)) {
                    der = l.get(l.indexOf(der));
                } else {
                    /*f.setEntityClass(der.getClass());
                    f.create(der);
                    f.getEntityManager().flush();
                    f.getEntityManager().refresh(der);*/
                    em.persist(der);
                }

                evaluacion.getEvaluaciones360ResultadosList().add(der);
            });
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion3601.cargarResultadosAlmacenados() No se cargaron los resultados");
        }
    }

    @Override
    public String obtenerRespuestaPorPregunta(Evaluaciones360Resultados resultado, Float pregunta) {
        String res = null;
        switch (pregunta.toString()) {
            case "1.0":
                res = resultado.getR1() != null ? resultado.getR1().toString() : null;
                break;
            case "2.0":
                res = resultado.getR2() != null ? resultado.getR2().toString() : null;
                break;
            case "3.0":
                res = resultado.getR3() != null ? resultado.getR3().toString() : null;
                break;
            case "4.0":
                res = resultado.getR4() != null ? resultado.getR4().toString() : null;
                break;
            case "5.0":
                res = resultado.getR5() != null ? resultado.getR5().toString() : null;
                break;
            case "6.0":
                res = resultado.getR6() != null ? resultado.getR6().toString() : null;
                break;
            case "7.0":
                res = resultado.getR7() != null ? resultado.getR7().toString() : null;
                break;
            case "8.0":
                res = resultado.getR8() != null ? resultado.getR8().toString() : null;
                break;
            case "9.0":
                res = resultado.getR9() != null ? resultado.getR9().toString() : null;
                break;
            case "10.0":
                res = resultado.getR10() != null ? resultado.getR10().toString() : null;
                break;
            case "11.0":
                res = resultado.getR11() != null ? resultado.getR11().toString() : null;
                break;
            case "12.0":
                res = resultado.getR12() != null ? resultado.getR12().toString() : null;
                break;
            case "13.0":
                res = resultado.getR13() != null ? resultado.getR13().toString() : null;
                break;
            case "14.0":
                res = resultado.getR14() != null ? resultado.getR14().toString() : null;
                break;
            case "15.0":
                res = resultado.getR15() != null ? resultado.getR15().toString() : null;
                break;
            case "16.0":
                res = resultado.getR16() != null ? resultado.getR16().toString() : null;
                break;
            case "17.0":
                res = resultado.getR17() != null ? resultado.getR17().toString() : null;
                break;
            case "18.0":
                res = resultado.getR18() != null ? resultado.getR18().toString() : null;
                break;
            case "19.0":
                res = resultado.getR19() != null ? resultado.getR19().toString() : null;
                break;
            case "20.0":
                res = resultado.getR20() != null ? resultado.getR20().toString() : null;
                break;
            case "21.0":
                res = resultado.getR21() != null ? resultado.getR21().toString() : null;
                break;
            case "22.0":
                res = resultado.getR22() != null ? resultado.getR22().toString() : null;
                break;
            case "23.0":
                res = resultado.getR23() != null ? resultado.getR23().toString() : null;
                break;
            case "24.0":
                res = resultado.getR24() != null ? resultado.getR24().toString() : null;
                break;
            case "25.0":
                res = resultado.getR25() != null ? resultado.getR25().toString() : null;
                break;
            case "26.0":
                res = resultado.getR26() != null ? resultado.getR26().toString() : null;
                break;
            case "27.0":
                res = resultado.getR27() != null ? resultado.getR27().toString() : null;
                break;
            case "28.0":
                res = resultado.getR28() != null ? resultado.getR28().toString() : null;
                break;
            case "29.0":
                res = resultado.getR29() != null ? resultado.getR29().toString() : null;
                break;
            case "30.0":
                res = resultado.getR30() != null ? resultado.getR30().toString() : null;
                break;
            case "31.0":
                res = resultado.getR31() != null ? resultado.getR31().toString() : null;
                break;
            case "32.0":
                res = resultado.getR32() != null ? resultado.getR32() : null;
                break;
            case "33.0":
                res = resultado.getR33() != null ? resultado.getR33() : null;
                break;
        }

        return res;
    }

    @Override
    public List<ListaPersonalEvaluacion360> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones360 desempenioEvaluacion, ListaPersonal directivo) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion3601.obtenerListaResultadosPorEvaluacionEvaluador(): " + desempenioEvaluacion.getEvaluacion() + "," + directivo.getClave());
        StoredProcedureQuery q = em.createStoredProcedureQuery("obtener_lista_resultados_360_por_evaluacion_evaluador", ListaPersonalEvaluacion360.class).
                registerStoredProcedureParameter("par_evaluacion", Integer.class, ParameterMode.IN).setParameter("par_evaluacion", desempenioEvaluacion.getEvaluacion()).
                registerStoredProcedureParameter("par_evaluador", Integer.class, ParameterMode.IN).setParameter("par_evaluador", directivo.getClave());
        return q.getResultList();
    }

    @Override
    public void comprobarResultado(Evaluaciones360Resultados resultado) {
        Apartado a = getApartadoHabilidades(resultado.getCategoria().getCategoria());
        boolean completo = true;

        Double suma = 0D;
        Double cantidad = (double) (16 + a.getPreguntas().size());
        for (Integer i = 1; i <= 16 + a.getPreguntas().size(); i++) {
            if (obtenerRespuestaPorPregunta(resultado, (float) i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta(resultado, (float) i));
//                cantidad++;
            }
        }

        resultado.setCompleto(completo);
        resultado.setIncompleto(!completo);
        resultado.setPromedio(suma / cantidad);

    }

    @Override
    public Apartado getApartadoHabilidades(Short categoria) {
        StoredProcedureQuery q = em.createStoredProcedureQuery("obtener_lista_habilidades_por_categoria", Habilidades.class).
                registerStoredProcedureParameter("par_categoria", Integer.class, ParameterMode.IN).setParameter("par_categoria", categoria);
        List<Habilidades> l = q.getResultList();

        Apartado apartado = new Apartado(3f, "HABILIDADES", new ArrayList<>());
        Float index = 17f;
        for (Habilidades h : l) {
            apartado.getPreguntas().add(new Opciones(2, 3f, index, h.getNombre(), ""));
            index++;
        }

        return apartado;
    }

    @Override
    public List<ListaPersonalEvaluacion360Promedios> getPromediosPorEvaluado() {
        List<ListaPersonalEvaluacion360Promedios> l = em.createQuery("select l from ListaPersonalEvaluacion360Promedios l", ListaPersonalEvaluacion360Promedios.class)
                .getResultList();
        l.forEach(registro -> em.refresh(registro));
        return l;
    }

    @Override
    public List<ListaPersonalEvaluacion360Reporte> getReportesPorEvaluador() {
        return em.createQuery("select l from ListaPersonalEvaluacion360Reporte l", ListaPersonalEvaluacion360Reporte.class).getResultList();
        //f.setEntityClass(ListaPersonalEvaluacion360Reporte.class);
        //return f.findAll();
    }

}
