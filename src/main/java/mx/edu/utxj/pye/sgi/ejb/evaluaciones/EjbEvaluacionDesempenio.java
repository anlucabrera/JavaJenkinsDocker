package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import edu.mx.utxj.pye.seut.util.util.Cuestionario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalDesempenioEvaluacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EjbEvaluacionDesempenio{
    
    @EJB EjbAdministracionEncuestas ejbAdmin;
    
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public ResultadoEJB<DesempenioEvaluaciones> obtenerEvaluacionActiva() {
        try{
            DesempenioEvaluaciones evaluacion = em.createQuery("SELECT d FROM DesempenioEvaluaciones d WHERE :fecha BETWEEN d.fechaInicio AND d.fechaFin ORDER BY d.evaluacion desc", DesempenioEvaluaciones.class)
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(new DesempenioEvaluaciones());
            if(evaluacion.equals(new DesempenioEvaluaciones())) return ResultadoEJB.crearErroneo(2,"La evaluacion no se encontro", DesempenioEvaluaciones.class);
            return ResultadoEJB.crearCorrecto(evaluacion, "Se obtuvo la evaluacion");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EjbEvaluacion3601, obtenerEvaluacionActiva)", e, null);
        }
    }
    
    public ResultadoEJB<Personal> validarPersona(Integer clave){
        Personal p = em.createQuery("select p from Personal as p where p.clave = :clave and (p.actividad.actividad = :actividad1 or p.actividad.actividad = :actividad2)", Personal.class)
                .setParameter("clave", clave)
                .setParameter("actividad1", 2)
                .setParameter("actividad2", 4)
                .getResultStream().findFirst().orElse(new Personal());
        if(p.equals(new Personal())) return ResultadoEJB.crearErroneo(1, "El objeto no puede ser nulo", Personal.class);
        return ResultadoEJB.crearCorrecto(p, "Instancia encontrada");
    }
    
    public ResultadoEJB<PeriodosEscolares> obtenerPeriodo(Integer periodo){
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, periodo);
        if(pe.equals(new PeriodosEscolares())) return ResultadoEJB.crearErroneo(1, "El periodo no puede ser nulo", PeriodosEscolares.class);
        return ResultadoEJB.crearCorrecto(pe, "Periodo encontrado");
    }
    
    public ResultadoEJB<List<Personal>> obtenerListaSubordinados(Personal directivo){
        List<Personal> lista = em.createQuery("select p from Personal as p where (p.areaOperativa = :areaOp and p.actividad.actividad <> 2 and p.status <> :estatus and p.clave <> :clave) or (p.areaSuperior = :areaOp and p.status <> :estatus) or (p.areaOperativa = :areaOp and p.actividad.actividad <> 4 and p.status <> :estatus and p.clave <> :clave) ORDER BY p.categoriaOperativa.categoria, p.nombre", Personal.class)
                .setParameter("areaOp", directivo.getAreaOperativa())
                .setParameter("estatus", "B".charAt(0))
                .setParameter("clave", directivo.getClave())
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "E", "Excelente"));
        l.add(new SelectItem("4", "MB", "Muy Bien"));
        l.add(new SelectItem("3", "B", "Bueno"));
        l.add(new SelectItem("2", "R", "Regular"));
        l.add(new SelectItem("1", "D", "Deficiente"));

        return l;
    }

    
    public List<Apartado> getApartados(Cuestionario cuestionario) {
        String etiquetas[] = {
            "METAS",
            "CRITERIO",
            "LIDERAZGO Y DIRECCIÓN",
            "CALIDAD DEL TRABAJO",
            "MADUREZ Y DISCRECIÓN",
            "INICIATIVA",
            "COLABORACIÓN Y COMPROMISO",
            "BUEN USO DE LOS RECURSOS",
            "DISCIPLINA",
            "TRABAJO COLABORATIVO Y EN EQUIPO",
            "RELACIONES INTERPERSONALES",
            "ASPECTO E IMAGEN PERSONAL",
            "CAPACITACIÓN",
            "COMENTARIOS Y SUGERENCIAS"};

        List<Apartado> l = new ArrayList<>();

        Apartado a = null;
        int i = 0;
        for (Pregunta p : cuestionario.getPreguntas()) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.getApartados() p: " + p);
            if (a == null || !a.getId().equals(p.getApartado())) {//si el apartado es nulo o si la pregunta pertenece a un apartado diferente al de la pregunta anterior se crea un nuevo apartado
                if (a != null) {
                    a.setContenido(etiquetas[i]);
                    l.add(a);
                    i++;
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.getApartados(1<>" + i + "): a" + a);
                }

                a = new Apartado(p.getApartado(), "", new ArrayList<>());
                a.getPreguntas().add(p);
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.getApartados(2<>" + i + ") a: " + a);
            } else {
                a.getPreguntas().add(p);
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.getApartados(3<>" + i + ") a:" + a);
            }
        }

        if (a != null) {
            a.setContenido(etiquetas[i]);
            l.add(a);
        }

//        setApartadosEtiquetas(l);
//        l.forEach((a2) -> {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.getApartados() a2: " + a2);
//        });
        return l;
    }

    
    public void setApartadosEtiquetas(List<Apartado> apartados) {
        String etiquetas[] = {
            "METAS",
            "CRITERIO",
            "LIDERAZGO y DIRECCIÓN",
            "CALIDAD DEL TRABAJO",
            "MADUREZ Y DISCRECIÓN",
            "INICIATIVA",
            "COLABORACIÓN Y COMPROMISO",
            "BUEN USO DE LOS RECURSOS",
            "DISCIPLINA",
            "TRABAJO COLABORATIVO Y EN EQUIPO",
            "RELACIONES INTERPERSONALES",
            "ASCPECTO E IMAGEN PERSONAL",
            "CAPACITACIÓN",
            "COMENTARIOS Y SUJERENCIAS SOBRE LAS AREAS DE OPORTUNIDAD",};

        int i = 0;
        for (Apartado a : apartados) {
            apartados.get(i).setContenido(etiquetas[i]);
            i++;
        }
    }

    
    public List<ListaPersonal> getListaDirectivos() {
        TypedQuery<ListaPersonal> q = em.createQuery("select lp from ListaPersonal lp where lp.actividad = 2 order by lp.nombre", ListaPersonal.class);

        return q.getResultList();
    }

    
    public List<ListaPersonal> getListaSubordinados(ListaPersonal directivo) {
//        System.out.println("directivo = [" + directivo + "]");
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.getListaEvaluados(): " + (directivo.getActividad() == 2));
        if (directivo.getActividad() == 2 || directivo.getActividad() == 4) {
            TypedQuery<ListaPersonal> q = em.createQuery("select lp from ListaPersonal lp where (lp.areaOperativa = :areaOperativa and lp.actividad <> 2 and lp.status <> :status and lp.clave <> :dir) or (lp.areaSuperior = :areaOperativa and lp.status <> :status and lp.clave <> :dir) or (lp.areaOperativa = :areaOperativa and lp.actividad <> 4 and lp.status <> :status and lp.clave <> :dir) order by lp.categoriaOperativaNombre, lp.nombre", ListaPersonal.class);
            q.setParameter("areaOperativa", directivo.getAreaOperativa());
            q.setParameter("status", 'B');
            q.setParameter("dir", directivo.getClave());
            List<ListaPersonal> l = q.getResultList();
            if (l.isEmpty() || l == null) {
                return null;
            } else {
                if (directivo.getClave() == 390) {
                    return l.stream()
                            .filter(p -> p.getClave()==570).collect(Collectors.toList());
                } else {
                    return l.stream()
                            .filter(p -> p.getClave() != directivo.getClave()).collect(Collectors.toList());
                }
            }
        } else {
            return new ArrayList<>();
        }
    }

    
    public DesempenioEvaluaciones evaluacionActiva() {
        StoredProcedureQuery spq = em.createStoredProcedureQuery("buscar_desempenio_evaluacion_activa", DesempenioEvaluaciones.class);
        List<DesempenioEvaluaciones> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    
    public void actualizarRespuestaPorPregunta(DesempenioEvaluacionResultados resultado, Float pregunta, String respuesta) {
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
                resultado.setR21(respuesta);
                break;
        }
    }

    
    public void cargarResultadosAlmacenados(DesempenioEvaluaciones desempenioEvaluacion, Personal directivo, List<Personal> subordinados) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() la clave : " + directivo.getClave() + " lista de subordinados : " + subordinados);
        //System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() subordinados: " + subordinados);
        List<Integer> claves = new ArrayList<>();
        subordinados.forEach((lp) -> {
            if (lp.getClave().equals(directivo.getClave())) {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() es director : " + directivo.getNombre());
            } else {
                claves.add(lp.getClave());
            }
        });

//        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() claves: " + claves);
        TypedQuery<DesempenioEvaluacionResultados> q = em.createQuery("select a from DesempenioEvaluacionResultados a where a.desempenioEvaluacionResultadosPK.evaluacion=:evaluacion and a.desempenioEvaluacionResultadosPK.evaluador = :evaluador and a.desempenioEvaluacionResultadosPK.evaluado in :claves", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluacion", desempenioEvaluacion.getEvaluacion());
        q.setParameter("evaluador", directivo.getClave());
        q.setParameter("claves", claves);

        List<DesempenioEvaluacionResultados> l = q.getResultList();

        desempenioEvaluacion.setDesempenioEvaluacionResultadosList(new ArrayList<>());
        subordinados
                //                .stream()
                //                .filter(s -> !Objects.equals(s.getClave(), directivo.getClave()))
                //                .collect(Collectors.toList())
                .forEach((subordinado) -> {
                    DesempenioEvaluacionResultadosPK pk = new DesempenioEvaluacionResultadosPK(desempenioEvaluacion.getEvaluacion(), directivo.getClave(), subordinado.getClave());
                    DesempenioEvaluacionResultados der = new DesempenioEvaluacionResultados(pk);

                    if (l.contains(der)) {
                        der = l.get(l.indexOf(der));
                    } else {
                        /*f.setEntityClass(der.getClass());
                        f.create(der);
                        f.getEntityManager().flush();
                        f.getEntityManager().refresh(der);*/
                        em.persist(der);
                        em.flush();
                        em.refresh(der);
                    }

                    desempenioEvaluacion.getDesempenioEvaluacionResultadosList().add(der);
                });
    }
    
    public void cargarResultadosAlmacenados(DesempenioEvaluaciones desempenioEvaluacion, ListaPersonal directivo, List<ListaPersonal> subordinados) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() la clave : " + directivo.getClave() + " lista de subordinados : " + subordinados);
        //System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() subordinados: " + subordinados);
        List<Integer> claves = new ArrayList<>();
        subordinados.forEach((lp) -> {
            if (lp.getClave().equals(directivo.getClave())) {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() es director : " + directivo.getNombre());
            } else {
                claves.add(lp.getClave());
            }
        });

//        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.cargarResultadosAlmacenados() claves: " + claves);
        TypedQuery<DesempenioEvaluacionResultados> q = em.createQuery("select a from DesempenioEvaluacionResultados a where a.desempenioEvaluacionResultadosPK.evaluacion=:evaluacion and a.desempenioEvaluacionResultadosPK.evaluador = :evaluador and a.desempenioEvaluacionResultadosPK.evaluado in :claves", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluacion", desempenioEvaluacion.getEvaluacion());
        q.setParameter("evaluador", directivo.getClave());
        q.setParameter("claves", claves);

        List<DesempenioEvaluacionResultados> l = q.getResultList();

        desempenioEvaluacion.setDesempenioEvaluacionResultadosList(new ArrayList<>());
        subordinados
                //                .stream()
                //                .filter(s -> !Objects.equals(s.getClave(), directivo.getClave()))
                //                .collect(Collectors.toList())
                .forEach((subordinado) -> {
                    DesempenioEvaluacionResultadosPK pk = new DesempenioEvaluacionResultadosPK(desempenioEvaluacion.getEvaluacion(), directivo.getClave(), subordinado.getClave());
                    DesempenioEvaluacionResultados der = new DesempenioEvaluacionResultados(pk);

                    if (l.contains(der)) {
                        der = l.get(l.indexOf(der));
                    } else {
                        /*f.setEntityClass(der.getClass());
                        f.create(der);
                        f.getEntityManager().flush();
                        f.getEntityManager().refresh(der);*/
                        em.persist(der);
                        em.flush();
                        em.refresh(der);
                    }

                    desempenioEvaluacion.getDesempenioEvaluacionResultadosList().add(der);
                });
    }

    
    public String obtenerRespuestaPorPregunta(DesempenioEvaluacionResultados resultado, Float pregunta) {
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
                res = resultado.getR21();
                break;
        }
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio.obtenerRespuestaPorPregunta(): " + res);
        return res;
    }

    
    public List<ListaPersonalDesempenioEvaluacion> obtenerListaResultadosPorEvaluacionEvaluador(DesempenioEvaluaciones desempenioEvaluacion, ListaPersonal directivo) {
        StoredProcedureQuery q = em.createStoredProcedureQuery("obtener_lista_resultados_por_evaluacion_evaluador", ListaPersonalDesempenioEvaluacion.class).
                registerStoredProcedureParameter("par_evaluacion", Integer.class, ParameterMode.IN).setParameter("par_evaluacion", desempenioEvaluacion.getEvaluacion()).
                registerStoredProcedureParameter("par_evaluador", Integer.class, ParameterMode.IN).setParameter("par_evaluador", directivo.getClave());
//        TypedQuery<ListaPersonalDesempenioEvaluacion> q = f.getEntityManager().createQuery("from ListaPersonalDesempenioEvaluacion lp where lp.pk.evaluacion = :evaluacion and lp.pk.evaluador=:evaluador", ListaPersonalDesempenioEvaluacion.class);
//        q.setParameter("evaluacion", desempenioEvaluacion.getEvaluacion());
//        q.setParameter("evaluador", directivo.getClave());

//        return q.getResultList();
        return q.getResultList();
    }

    
    public DesempenioEvaluaciones getUltimaEvaluacionDesempenio() {
        TypedQuery<DesempenioEvaluaciones> q = em.createQuery("SELECT e FROM DesempenioEvaluaciones e ORDER BY e.evaluacion DESC", DesempenioEvaluaciones.class);
        List<DesempenioEvaluaciones> l = q.getResultList();
        if(l==null || l.isEmpty()){
            return null;
        }else{
            return  l.get(0);
        }
    }

    
    public PeriodosEscolares getPeriodoDeLaEvaluacionDesempenio(Integer periodo) {
        System.out.println("mx.edu.utxj.pye.sgi.ejb.evaluaciones.ServicioEvaluacionDesempenio.getPeriodoDeLaEvaluacion() el periodo es = " +periodo);
        TypedQuery<PeriodosEscolares> q = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE p.periodo = :periodo", PeriodosEscolares.class);
        q.setParameter("periodo", periodo);
        List<PeriodosEscolares> l = q.getResultList();
        if( l== null || l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }
    
    
    public ResultadoEJB<List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio>> obtenerListaResultadosEvaluacionEvaluador(Integer evaluacion, Integer evaluador){
        List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> lista = em.createQuery("select d from DesempenioEvaluacionResultados as d where d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion and d.desempenioEvaluacionResultadosPK.evaluador = :evaluador", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluador", evaluador)
                .getResultStream()
                .map(resultado -> ejbAdmin.packResultado(resultado))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<Personal> obtenerPersona(Integer clave){
        Personal p = em.find(Personal.class, clave);
        return ResultadoEJB.crearCorrecto(p, "Persona encontrada");
    }
}
