package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Combinaciones;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServicioEvaluacion360Combinaciones implements EjbEvaluacion360Combinaciones {

    @EJB
    Facade f;

    private Evaluaciones360Resultados comprobarPersistencia(Evaluaciones360Resultados resultado){
        Evaluaciones360Resultados resultadoBD = f.getEntityManager().createQuery("select r from Evaluaciones360Resultados  r where r.evaluaciones360ResultadosPK.evaluado=:evaluado and r.evaluaciones360ResultadosPK.evaluacion=:evaluacion and r.tipo=:tipo", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", resultado.getEvaluaciones360ResultadosPK().getEvaluacion())
                .setParameter("evaluado", resultado.getEvaluaciones360ResultadosPK().getEvaluado())
                .setParameter("tipo", resultado.getTipo())
                .getResultStream()
                .filter(r -> {//si el tipo de evaluacion es de subordinado se permite multiple y se debe corroborar tmb por la clave del evaluado para determinar si existe
                    if(r==null) return false;
                    if(!r.getTipo().equals("Subordinado")) return true;
                    return r.getEvaluaciones360ResultadosPK().getEvaluador() == resultado.getEvaluaciones360ResultadosPK().getEvaluador();
                })
                .findFirst()
                .orElse(resultado);
        // f.getEntityManager().find(Evaluaciones360Resultados.class, resultado.getEvaluaciones360ResultadosPK());
        return resultadoBD!=null?resultadoBD:resultado;
    }

    @Override
    public List<Evaluaciones360Resultados> generar(Evaluaciones360 evaluacion) {
        List<Personal> plantilla = getPersonalActivo();
        Map<Personal, List<Personal>> directivosOperativos = getDirectivosOperativos(plantilla);
        List<Evaluaciones360Resultados> l = new SerializableArrayList<>();

        plantilla.stream().forEach(evaluado -> {
            Map<PeriodosEscolares, Personal> evaluadoresAnteriores = getEvaluadoresAnteriores(evaluado, evaluacion);
            Evaluaciones360Resultados superior = comprobarPersistencia(new Evaluaciones360Resultados(evaluacion.getEvaluacion(), getEvaluadorSuperior(evaluado, directivosOperativos), evaluado.getClave()));
            Integer claveSubordinado = directivosOperativos.containsKey(evaluado) ? getSubordinado(evaluado, evaluadoresAnteriores, directivosOperativos.get(evaluado)) : evaluado.getClave();
            Evaluaciones360Resultados subordinado = comprobarPersistencia(new Evaluaciones360Resultados(evaluacion.getEvaluacion(), claveSubordinado, evaluado.getClave()));
            Integer claveIgual = getIgual(directivosOperativos.containsKey(evaluado), evaluado, evaluadoresAnteriores, directivosOperativos.keySet(), plantilla);
            Evaluaciones360Resultados igual = comprobarPersistencia(new Evaluaciones360Resultados(evaluacion.getEvaluacion(), claveIgual, evaluado.getClave()));

            if (superior.getEvaluaciones360ResultadosPK().getEvaluador() != 0) {
                superior.setTipo("Superior");
                PersonalCategorias categoria = f.getEntityManager().find(PersonalCategorias.class, evaluado.getCategoria360().getCategoria());
                superior.setCategoria(categoria);//superior.setCategoria((short)50);
                superior.setEvaluaciones360(evaluacion);
                l.add(superior);
            }

            if (subordinado.getEvaluaciones360ResultadosPK().getEvaluador() != 0) {
                PersonalCategorias categoria = f.getEntityManager().find(PersonalCategorias.class, evaluado.getCategoria360().getCategoria());
                subordinado.setCategoria(categoria);//subordinado.setCategoria((short)50);
                //TODO: agregar política del 50% +1
                if (subordinado.getEvaluaciones360ResultadosPK().getEvaluado() == subordinado.getEvaluaciones360ResultadosPK().getEvaluador()) {
                    subordinado.setTipo("Autoevaluación");
                    subordinado.setEvaluaciones360(evaluacion);
                    l.add(subordinado);
                } else {
                    subordinado.setTipo("Subordinado");
                    List<Personal> subordinados = directivosOperativos.get(evaluado);
                    if(subordinados != null){
                        subordinados
                                .stream()
                                .limit(subordinados.size() / 2 + 1)
                                .forEach(evaluador -> {
                            Evaluaciones360Resultados subordinadoN = comprobarPersistencia(new Evaluaciones360Resultados(evaluacion.getEvaluacion(), evaluador.getClave(), evaluado.getClave()));
                            subordinadoN.setTipo("Subordinado");
                            subordinadoN.setEvaluaciones360(evaluacion);
                            l.add(subordinadoN);
                        });
                    }
                }
            }

            if (igual.getEvaluaciones360ResultadosPK().getEvaluador() != 0) {
                igual.setTipo("Igual");
                PersonalCategorias categoria = f.getEntityManager().find(PersonalCategorias.class, evaluado.getCategoria360().getCategoria());
                igual.setCategoria(categoria);//igual.setCategoria((short)50);
                igual.setEvaluaciones360(evaluacion);
                l.add(igual);
            }
        });

        return l;
    }

    @Override
    public List<Personal> getPersonalActivo() {
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.status <> :status order by p.clave", Personal.class);
        q.setParameter("status", 'B');
        return q.getResultList();
    }

    @Override
    public Map<Personal, List<Personal>> getDirectivosOperativos(@NonNull List<Personal> plantilla) {
        Map<Personal, List<Personal>> map = new HashMap<>();
        List<Personal> directivos = plantilla.stream().filter(p -> p.getActividad().getActividad() == 2 || p.getActividad().getActividad() == 4).collect(Collectors.toList());
        directivos.stream().forEach(d -> {
            List<Personal> operativos = plantilla.stream().filter(p -> (p.getAreaOperativa() == d.getAreaOperativa() || p.getAreaSuperior() == d.getAreaOperativa()) && !Objects.equals(p.getClave(), d.getClave())).collect(Collectors.toList());
            map.put(d, operativos);
        });
        return map;
    }

    @Override
    public Map<PeriodosEscolares, Personal> getEvaluadoresAnteriores(Personal personal, Evaluaciones360 evaluacionActiva) {
        TypedQuery<Evaluaciones360Resultados> q = f.getEntityManager().createQuery("SELECT r FROM Evaluaciones360Resultados r INNER JOIN r.evaluaciones360 e WHERE r.evaluaciones360ResultadosPK.evaluado = :evaluado and r.evaluaciones360ResultadosPK.evaluacion <> :evaluacion ORDER BY e.periodo DESC", Evaluaciones360Resultados.class);
        q.setParameter("evaluado", personal.getClave());
        q.setParameter("evaluacion", evaluacionActiva.getEvaluacion());
        List<Evaluaciones360Resultados> l = q.getResultList();

        Map<PeriodosEscolares, Personal> map = new HashMap<>();
        l.stream().forEach(r -> {
            f.setEntityClass(PeriodosEscolares.class);
            PeriodosEscolares periodo = (PeriodosEscolares) f.find(r.getEvaluaciones360().getPeriodo());
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getEvaluadoresAnteriores() periodo: " + periodo);
            map.put(periodo, r.getPersonal1());
        });

        return map;
    }

    private int getEvaluadorSuperior(Personal evaluado, Map<Personal, List<Personal>> direcivosOperativos) {
        List<Map.Entry<Personal, List<Personal>>> superiores = direcivosOperativos.entrySet().stream().filter(entrada -> entrada.getValue().contains(evaluado)).collect(Collectors.toList());

        if (!superiores.isEmpty()) {
            return superiores.get(0).getKey().getClave();
        }

        return 0;
    }

    private Integer getSubordinado(Personal evaluado, Map<PeriodosEscolares, Personal> evaluadoresAnteriores, List<Personal> subordinados) {
        //si no tiene subordinados se evalua asi mismo
        if (subordinados.isEmpty()) {
            return evaluado.getClave();
        }

        //si nadie lo ha evaluado y tiene subordinados lo evaluado el primer subordinado
        if (evaluadoresAnteriores.isEmpty()) {
            int random = (int) (Math.random() * (double) subordinados.size());
            return subordinados.get(random).getClave();
        }

        //si ya fue evaluado y tiene subordinados se verifica quienes de sus subordinados no lo ha evaluado
        List<Personal> subordinadosNoEvaluadores = subordinados.stream().filter(s -> !evaluadoresAnteriores.containsValue(s)).collect(Collectors.toList());

        //si ya fue evaluado, tiene subordinados y hay subordinados que no lo han evaluado, lo evalua el primer subordinado que no lo ha evaluado
        if (!subordinadosNoEvaluadores.isEmpty()) {
            int random = (int) (Math.random() * (double) subordinadosNoEvaluadores.size());
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getSubordinado() random: " + random);
            return subordinadosNoEvaluadores.get(random).getClave();
        } else {//si tiene subordinados pero ya lo han evaluado todos lo evalua el subordinado que menos veces lo ha evaluado
            //TODO: diferenciar la frecuencia de evaluaciones
            int random = (int) (Math.random() * (double) subordinados.size());
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getSubordinado() random: " + random);
            subordinados.get(random);
        }

        return 0;
    }

    @Override
    public Boolean detectarConfiguracion(Evaluaciones360 evaluacion) {
        Query q = f.getEntityManager().createQuery("SELECT COUNT(r.evaluaciones360ResultadosPK.evaluacion) FROM Evaluaciones360Resultados r WHERE r.evaluaciones360ResultadosPK.evaluacion=:evaluacion");
        q.setParameter("evaluacion", evaluacion.getEvaluacion());

        Object o = q.getSingleResult();
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.detectarConfiguracion(" + o + ") class: " + o.getClass());

        Long count = (Long) o;
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.detectarConfiguracion() count: " + count);

        return count > 0;
    }

    private Integer getIgual(boolean esDirectivo, Personal evaluado, Map<PeriodosEscolares, Personal> evaluadoresAnteriores, Set<Personal> setDirectivos, List<Personal> plantilla) {

        if (esDirectivo) {
            List<Personal> iguales = setDirectivos.stream().filter(p -> p.getAreaSuperior() == evaluado.getAreaSuperior() && !Objects.equals(p.getClave(), evaluado.getClave())).collect(Collectors.toList());

            //si son de secretaria academica
            if (evaluado.getAreaSuperior() == 28) {
                //si es jefe de departamento en secretaria academica los iguales solo deben ser jefes de departamento
                if (evaluado.getCategoriaOperativa().getCategoria() == 24) {
                    iguales = setDirectivos.stream().filter(p -> p.getAreaSuperior() == evaluado.getAreaSuperior() && !Objects.equals(p.getClave(), evaluado.getClave()) && p.getCategoriaOperativa().getCategoria() == 24).collect(Collectors.toList());
                } else {//si no son jefes de departamento, los iguales no deben ser jefes de departamento
                    iguales = setDirectivos.stream().filter(p -> p.getAreaSuperior() == evaluado.getAreaSuperior() && !Objects.equals(p.getClave(), evaluado.getClave()) && p.getCategoriaOperativa().getCategoria() != 24).collect(Collectors.toList());
                }
            } else {
                if (evaluado.getCategoriaOperativa().getCategoria() == 24 && iguales.isEmpty()) {
                    iguales = setDirectivos.stream().filter(p -> !Objects.equals(p.getClave(), evaluado.getClave()) && p.getCategoriaOperativa().getCategoria() == 24).collect(Collectors.toList());
                }
            }

            List<Personal> igualesNoEvaluadores = iguales.stream().filter(p -> !evaluadoresAnteriores.containsValue(p)).collect(Collectors.toList());

            //si no tiene personal de su mismo rango
            if (iguales.isEmpty()) {
                return 0;
            }

            //si ya todos sus iguales lo han evaluado, lo evalua el primer igual
            if (igualesNoEvaluadores.isEmpty()) {
                //TODO: verificar cual igual lo ha evaluado menos veces
                int random = (int) (Math.random() * (double) iguales.size());
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() random: " + random);
                return iguales.get(random).getClave();
            } else {//si hay iguales que no lo han evaluado lo evalua el primer igual que no lo haya evaluado                
                int random = (int) (Math.random() * (double) igualesNoEvaluadores.size());
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() random: " + random);
                return igualesNoEvaluadores.get(random).getClave();
            }
        } else {
            //Los iguales no serán directivos, deberan tener la misma area operativa y no deberá ser el mismo evaluado
            List<Personal> iguales = plantilla.stream().filter(p -> !setDirectivos.contains(p) && p.getAreaOperativa() == evaluado.getAreaOperativa() && !p.equals(evaluado)).collect(Collectors.toList());

            //si es una secretaria
            if (evaluado.getCategoriaOperativa().getCategoria() == 34) {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() es secretaria de area: " + evaluado);
                List<Personal> igualesPtcs = plantilla.stream().filter(p -> !setDirectivos.contains(p) && p.getAreaOperativa() == evaluado.getAreaOperativa() && !p.equals(evaluado) && p.getCategoriaOperativa().getCategoria() == 32).collect(Collectors.toList());
                //si es una secretaria de carrera la evaluaran ptcs
                if (!igualesPtcs.isEmpty()) {
                    iguales = igualesPtcs;
                }
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() evaluadores posibles: " + iguales);
            }

            //si es un PTC
            if (evaluado.getCategoriaOperativa().getCategoria() == 32) {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() es un PTC");
                iguales = plantilla.stream().filter(p -> !setDirectivos.contains(p) && p.getAreaOperativa() == evaluado.getAreaOperativa() && !p.equals(evaluado) && p.getCategoriaOperativa().getCategoria() == 32).collect(Collectors.toList());
            }

            //si es un PA
            if (evaluado.getCategoriaOperativa().getCategoria() == 30) {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() es un PA");
                iguales = plantilla.stream().filter(p -> !setDirectivos.contains(p) && p.getAreaOperativa() == evaluado.getAreaOperativa() && !p.equals(evaluado) && p.getCategoriaOperativa().getCategoria() == 30).collect(Collectors.toList());
            }

            //si es un Laboratorista
            if (evaluado.getCategoriaOperativa().getCategoria() == 41) {
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() es un LAB");
                iguales = plantilla.stream().filter(p -> !setDirectivos.contains(p) && p.getAreaOperativa() == evaluado.getAreaOperativa() && !p.equals(evaluado) && p.getCategoriaOperativa().getCategoria() == 41).collect(Collectors.toList());
                if (iguales.isEmpty()) {
                    iguales = plantilla.stream().filter(p -> !setDirectivos.contains(p) && p.getAreaOperativa() == evaluado.getAreaOperativa() && !p.equals(evaluado) && p.getCategoriaOperativa().getCategoria() == 30).collect(Collectors.toList());
                }
            }

            //si no tiene iguales nadie mas lo evalua
            if (iguales.isEmpty()) {
                return 0;
            }

            List<Personal> igualesNoEvaluadores = iguales.stream().filter(p -> !evaluadoresAnteriores.containsValue(p)).collect(Collectors.toList());
            //si ya todos sus iguales lo han evaluado, lo evalua el primer igual
            if (igualesNoEvaluadores.isEmpty()) {
                //TODO: verificar cual igual lo ha evaluado menos veces
                int random = (int) (Math.random() * (double) iguales.size());
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() random: " + random);
                return iguales.get(random).getClave();
            } else {//si hay iguales que no lo han evaluado lo evalua el primer igual que no lo haya evaluado
                int random = (int) (Math.random() * (double) igualesNoEvaluadores.size());
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getIgual() random: " + random);
                return igualesNoEvaluadores.get(random).getClave();
            }
        }
    }

    @Override
    public void guardarCombinaciones(List<Evaluaciones360Resultados> resultados) {
        resultados.stream().forEach(r -> {
            f.edit(r);
        });
    }

    @Override
    public PeriodosEscolares getPeriodoActual() {
        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("prontuario.periodoActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getPeriodoActual()" + l.get(0));
            return l.get(0);
        }
    }

    @Override
    public Listaperiodosescolares getPeriodoEscolar(Integer periodo) {
        TypedQuery<Listaperiodosescolares> q = f.getEntityManager().createQuery("SELECT lp from Listaperiodosescolares as lp WHERE lp.periodo = :periodo", Listaperiodosescolares.class);
        q.setParameter("periodo", periodo);
        List<Listaperiodosescolares> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public List<Evaluaciones360> getEvaluaciones360() {
        TypedQuery<Evaluaciones360> q = f.getEntityManager().createQuery("SELECT e from Evaluaciones360 as e ORDER BY e.evaluacion DESC", Evaluaciones360.class);
        List<Evaluaciones360> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<DesempenioEvaluaciones> getEvaluacionesDesempenio() {
        TypedQuery<DesempenioEvaluaciones> q = f.getEntityManager().createQuery("SELECT e from DesempenioEvaluaciones as e ORDER BY e.evaluacion DESC", DesempenioEvaluaciones.class);

        List<DesempenioEvaluaciones> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<Evaluaciones360Resultados> getResultados360(Integer evaluacion, Integer evaluado) {

        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_resultados_evaluacion_360", Evaluaciones360Resultados.class).
                registerStoredProcedureParameter("par_evaluacion", Integer.class, ParameterMode.IN).setParameter("par_evaluacion", evaluacion).
                registerStoredProcedureParameter("par_evaluado", Integer.class, ParameterMode.IN).setParameter("par_evaluado", evaluado);
        List<Evaluaciones360Resultados> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            l.forEach(x-> {
                Evaluaciones360Resultados e = f.getEntityManager().find(Evaluaciones360Resultados.class, x.getEvaluaciones360ResultadosPK());
                f.refresh(e);
            });
            return l;
        }
    }

    @Override
    public Personal getPersonalEvaluador(Integer evaluador) {
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.clave = :evaluador", Personal.class);
        q.setParameter("evaluador", evaluador);
        List<Personal> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public AreasUniversidad getAreaPorClave(Short area) {
        TypedQuery<AreasUniversidad> q = f.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.area  = :area", AreasUniversidad.class);
        q.setParameter("area", area);
        List<AreasUniversidad> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public PersonalCategorias getCategoriaPorClave(Integer categoria) {
        TypedQuery<PersonalCategorias> q = f.getEntityManager().createQuery("SELECT p from PersonalCategorias p WHERE p.categoria = :categoria", PersonalCategorias.class);
        q.setParameter("categoria", categoria);
        List<PersonalCategorias> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public List<PersonalCategorias> getCategorias() {
        TypedQuery<PersonalCategorias> q = f.getEntityManager().createQuery("SELECT pc FROM PersonalCategorias as pc WHERE pc.categoria >= :categoria", PersonalCategorias.class);
        q.setParameter("categoria", 49);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getCategorias() EJB lista de categorias");
//        q.getResultList().forEach(System.out::println);
        List<PersonalCategorias> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public Evaluaciones360 editaEvaluacion360(Evaluaciones360 evaluacion, Date fi, Date ff) {
        Evaluaciones360 e = f.getEntityManager().find(Evaluaciones360.class, evaluacion.getEvaluacion());
        e.setFechaInicio(fi);
        e.setFechaFin(ff);
        f.edit(e);
        f.getEntityManager().flush();
        return e;
    }

    @Override
    public DesempenioEvaluaciones editaDesempenioEvaluaciones(DesempenioEvaluaciones evaluacion, Date fi, Date ff) {
        DesempenioEvaluaciones e = f.getEntityManager().find(DesempenioEvaluaciones.class, evaluacion.getEvaluacion());
        e.setFechaInicio(fi);
        e.setFechaFin(ff);
        f.edit(e);
        f.getEntityManager().flush();
        return e;
    }

    @Override
    public Evaluaciones360Resultados getCombinacion(Integer evaluacion, Integer evaluado, Integer evaluador) {
        TypedQuery<Evaluaciones360Resultados> q = f.getEntityManager().createQuery("SELECT e from Evaluaciones360Resultados e WHERE e.evaluaciones360ResultadosPK.evaluacion = :evaluacion AND e.evaluaciones360ResultadosPK.evaluado = :evaluado AND e.evaluaciones360ResultadosPK.evaluador = :evaluador", Evaluaciones360Resultados.class);
        q.setParameter("evaluacion", evaluacion);
        q.setParameter("evaluado", evaluado);
        q.setParameter("evaluador", evaluador);
        List<Evaluaciones360Resultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public DesempenioEvaluacionResultados getCombinacionDesempenio(Integer evaluacion, Integer evaluado, Integer evaluador) {
//        Personal pEvaluado = f.getEntityManager().find(Personal.class, evaluado);
//        Personal pEvaluador = f.getEntityManager().find(Personal.class, evaluador);
//        TypedQuery<DesempenioEvaluacionResultados> q = f.getEntityManager().createQuery("SELECT e FROM DesempenioEvaluacionResultados e WHERE e.pk.evaluacion = :evaluacion AND e.personal = :evaluado AND e.personal1 = :evaluador", DesempenioEvaluacionResultados.class);}
        TypedQuery<DesempenioEvaluacionResultados> q = f.getEntityManager().createQuery("SELECT d from DesempenioEvaluacionResultados d WHERE d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion AND d.desempenioEvaluacionResultadosPK.evaluado = :evaluado AND d.desempenioEvaluacionResultadosPK.evaluador = :evaluador", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluacion", evaluacion);
        q.setParameter("evaluado", evaluado);
        q.setParameter("evaluador", evaluador);
        List<DesempenioEvaluacionResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.getCombinacionDesempenio() EJB combinacion : " + l.get(0));
            return l.get(0);
        }
    }

    @Override
    public Evaluaciones360Resultados editaCombinacion360(Evaluaciones360Resultados evaluacion, Integer evaluado, Integer evaluador, Short categoria, String tipo) {
        // se conserva la evaluacion anterior para su posterior eliminacion
        f.setEntityClass(Evaluaciones360Resultados.class);
        Evaluaciones360Resultados viejaEvaluacion = evaluacion;
        Integer evaluadorClave = evaluador, evaluadoClave = evaluado;
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.editaCombinacion360() evaluacion anterior para eliminar : " + viejaEvaluacion.getEvaluaciones360ResultadosPK());
        // se crea la evaluacion nueva
        Evaluaciones360Resultados nuevaEvaluacion = new Evaluaciones360Resultados(viejaEvaluacion.getEvaluaciones360ResultadosPK().getEvaluacion(), evaluadorClave, evaluadoClave);

//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.editaCombinacion360() evaluacion recien creada= : " + nuevaEvaluacion.getEvaluaciones360ResultadosPK());
        // se edita la evaluacion   

        f.setEntityClass(PersonalCategorias.class);
        PersonalCategorias personaC = f.getEntityManager().find(PersonalCategorias.class, categoria);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.editaCombinacion360() item personal categoirias : " + personaC.getNombre());
        nuevaEvaluacion.setCategoria(personaC);
        nuevaEvaluacion.setTipo(tipo);
        f.create(nuevaEvaluacion);
        f.flush();
        f.refresh(nuevaEvaluacion);
        //se elimina la evaluacion
        Evaluaciones360Resultados eEliminada = f.getEntityManager().find(Evaluaciones360Resultados.class, viejaEvaluacion.getEvaluaciones360ResultadosPK());
        f.remove(eEliminada);
        f.flush();
        return nuevaEvaluacion;
    }

    @Override
    public DesempenioEvaluacionResultados editaCombinacionDesempenio(DesempenioEvaluacionResultados evaluacion, Integer evaluado, Integer evaluador) {
        f.setEntityClass(DesempenioEvaluacionResultados.class);
        DesempenioEvaluacionResultados evaluacionAntigua = f.getEntityManager().find(DesempenioEvaluacionResultados.class, evaluacion.getDesempenioEvaluacionResultadosPK());
        Integer evaluadorClave = evaluador, evaluadoClave = evaluado;
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.editaCombinacionDesempenio() la evaluacion a eliminar : " + evaluacionAntigua.getDesempenioEvaluacionResultadosPK());
        DesempenioEvaluacionResultados nuevaEvaluacion = new DesempenioEvaluacionResultados(evaluacionAntigua.getDesempenioEvaluacionResultadosPK().getEvaluacion(), evaluadorClave, evaluadoClave);
        f.create(nuevaEvaluacion);
        f.flush();
        f.refresh(nuevaEvaluacion);
        DesempenioEvaluacionResultados eEliminada = f.getEntityManager().find(DesempenioEvaluacionResultados.class, evaluacionAntigua.getDesempenioEvaluacionResultadosPK());
        f.remove(eEliminada);
        f.flush();
        return evaluacionAntigua;
    }

    @Override
    public Evaluaciones360Resultados nuevaCombinacion360(Integer evaluacion, Integer evaluado, Integer evaluador, Short categoria, String tipo) {
        f.setEntityClass(Evaluaciones360Resultados.class);
        // se crea la evaluacion nueva
        Evaluaciones360Resultados nuevaEvaluacion = new Evaluaciones360Resultados(evaluacion, evaluador, evaluado);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.editaCombinacion360() evaluacion recien creada= : " + nuevaEvaluacion.getEvaluaciones360ResultadosPK());
        // se edita la evaluacion   
        f.setEntityClass(PersonalCategorias.class);
        PersonalCategorias personaC = f.getEntityManager().find(PersonalCategorias.class, categoria);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.editaCombinacion360() item personal categoirias : " + personaC.getNombre());
        nuevaEvaluacion.setCategoria(personaC);
        nuevaEvaluacion.setTipo(tipo);
        f.create(nuevaEvaluacion);
        f.flush();
        f.refresh(nuevaEvaluacion);
        return nuevaEvaluacion;
    }

    @Override
    public DesempenioEvaluacionResultados nuevaCo0mbinacionDesempenio(Integer evaluacion, Integer evaluado, Integer evaluador) {
        f.setEntityClass(DesempenioEvaluacionResultados.class);
        DesempenioEvaluacionResultados nuevaEvaluacion = new DesempenioEvaluacionResultados(evaluacion, evaluador, evaluado);
        f.create(nuevaEvaluacion);
        f.flush();
        f.refresh(nuevaEvaluacion);
        return nuevaEvaluacion;
    }

    @Override
    public Evaluaciones360Resultados eliminaCombinacion360(ListaEvaluacion360Combinaciones combinacion) {
        f.setEntityClass(Evaluaciones360Resultados.class);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.eliminaCombinacion360() llega la combinacion : " + combinacion);
        TypedQuery<Evaluaciones360Resultados> q = f.getEntityManager()
                .createQuery("SELECT e FROM Evaluaciones360Resultados e WHERE e.evaluaciones360ResultadosPK.evaluacion = :evaluacion AND e.evaluaciones360ResultadosPK.evaluado = :evaluado AND e.evaluaciones360ResultadosPK.evaluador = :evaluador", Evaluaciones360Resultados.class);
        q.setParameter("evaluacion", combinacion.getEvaluacion());
        q.setParameter("evaluador", combinacion.getNominaEvaluador());
        q.setParameter("evaluado", combinacion.getNominaEvaluado());
        List<Evaluaciones360Resultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.eliminaCombinacion360() no se pudo encontrar la ecombinacion por lo tanto no se elimino");
            return null;
        } else {
            Evaluaciones360Resultados evaluacionElimina = f.getEntityManager().find(Evaluaciones360Resultados.class, l.get(0).getEvaluaciones360ResultadosPK());
            f.remove(evaluacionElimina);
//            f.refresh(evaluacionElimina);
            f.flush();
            return evaluacionElimina;
        }
    }

    @Override
    public DesempenioEvaluacionResultados eliminaCombinacionDes(ListaEvaluacionDesempenio combinacion) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.eliminaCombinacionDes() llega la combinacion : " + combinacion);
        f.setEntityClass(DesempenioEvaluaciones.class);
        TypedQuery<DesempenioEvaluacionResultados> q = f.getEntityManager()
                .createQuery("SELECT e FROM DesempenioEvaluacionResultados e WHERE e.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion AND e.desempenioEvaluacionResultadosPK.evaluado = :evaluado AND e.desempenioEvaluacionResultadosPK.evaluador = :evaluador", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluacion", combinacion.getEvaluacion());
        q.setParameter("evaluador", combinacion.getEvaluador());
        q.setParameter("evaluado", combinacion.getEvaluado());
        List<DesempenioEvaluacionResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.eliminaCombinacionDes() no se pudo encontrar la combinacion po lo tanto no se elimino");
            return null;
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacion360Combinaciones.eliminaCombinacionDes() pasa la validacion y encuentra la combinacion : " + l.get(0).getDesempenioEvaluacionResultadosPK());
            DesempenioEvaluacionResultados evaluacionElimina = f.getEntityManager().find(DesempenioEvaluacionResultados.class, l.get(0).getDesempenioEvaluacionResultadosPK());
            f.remove(evaluacionElimina);
//            f.refresh(evaluacionElimina);
            f.flush();
            return evaluacionElimina;
        }
    }
}
