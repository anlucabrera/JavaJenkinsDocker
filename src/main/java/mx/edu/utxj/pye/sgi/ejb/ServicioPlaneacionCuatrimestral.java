package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.persistence.TypedQuery;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Actividadesvarias;
import mx.edu.utxj.pye.sgi.entity.ch.Atividadesvariasplaneacionescuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesDetalles;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesLiberaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Validador;
import mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralLAB;
import mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPA;
import mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaTotalAlumnosCarreraPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

@Stateful
public class ServicioPlaneacionCuatrimestral implements EjbPlaneacionCuatrimestral {

    private static final long serialVersionUID = -8560915921611638551L;

    @EJB    Facade f;
    @EJB    Facade2 f2;
    @EJB    EjbPropiedades ep;
    @Resource    ManagedExecutorService exe;

    @Override
    public Eventos getEventoActivo() {
        TypedQuery<Eventos> q = f.getEntityManager().createQuery("SELECT e FROM Eventos e WHERE e.tipo = :tipo and :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evento desc", Eventos.class);
        q.setParameter("tipo", "Planeación cuatrimestral");
        q.setParameter("fecha", new Date());

        List<Eventos> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public PeriodosEscolares verificarPeriodo(Eventos evento) {
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares) f.find(evento.getPeriodo());
    }

    @Override
    public Personal getDirector(Integer clave) {
        final short directorCategoriaOperativa = (short) ep.leerPropiedadEntera("directorCategoriaOperativa").getAsInt();
        final short cordinadorCategoriaOperativa = 14;
        final short directorAreaSuperior = (short) ep.leerPropiedadEntera("directorAreaSuperior").getAsInt();
        f.setEntityClass(Personal.class);

        Personal personal = (Personal) f.find(clave);
        if (personal == null) {
            return null;
        } else {
            if (((personal.getCategoriaOperativa().getCategoria() == directorCategoriaOperativa) || (personal.getCategoriaOperativa().getCategoria() == cordinadorCategoriaOperativa)) && personal.getAreaSuperior() == directorAreaSuperior) {
                return personal;
            } else {
                return null;
            }
        }
    }

    @Override
    public Personal getSecretarioAcademico() {
        final short secretarioAcademicoCategoriaOperativa = (short) ep.leerPropiedadEntera("secretarioAcademicoCategoriaOperativa").getAsInt();
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p INNER JOIN p.categoriaOperativa co WHERE co.categoria = :categoria", Personal.class);
        q.setParameter("categoria", secretarioAcademicoCategoriaOperativa);

        List<Personal> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public Personal getJefePersonal() {
        final short jefePersonalAreaOperativa = (short) ep.leerPropiedadEntera("jefePersonalAreaOperativa").getAsInt();
        final short jefePersonalCategoria = (short) ep.leerPropiedadEntera("jefePersonalCategoria").getAsInt();
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p  INNER JOIN p.categoriaOperativa co WHERE co.categoria = :categoria and p.areaOperativa = :area", Personal.class);
        q.setParameter("area", jefePersonalAreaOperativa);
        q.setParameter("categoria", jefePersonalCategoria);

        List<Personal> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public List<Personal> getPersonalDocenteActivo() {
        final short asignacionLABCategoriaOficial = (short) ep.leerPropiedadEntera("asignacionLABCategoriaOficial").getAsInt();
        final short asignacionPACategoriaOficial = (short) ep.leerPropiedadEntera("asignacionPACategoriaOficial").getAsInt();
        final short asignacionPTCCategoriaOficial = (short) ep.leerPropiedadEntera("asignacionPTCCategoriaOficial").getAsInt();
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p  INNER JOIN p.categoriaOficial co WHERE co.categoria IN :categorias and p.status <> :status ORDER BY co.nombre, p.nombre", Personal.class);
        q.setParameter("categorias", Arrays.asList(asignacionLABCategoriaOficial, asignacionPACategoriaOficial, asignacionPTCCategoriaOficial));
        q.setParameter("status", 'B');

        return q.getResultList();
    }

    @Override
    public List<Personal> getPersonalDocenteColaborador(@NonNull List<Personal> docentes, Personal director) {
        List<Personal> l = f.getEntityManager().createQuery("SELECT p FROM Personal p INNER JOIN p.planeacionesCuatrimestralesList1 pc WHERE pc.director.clave=:director", Personal.class)
                .setParameter("director", director.getClave())
                .getResultList();
        
        if (l.isEmpty()) {
            l = docentes.stream().filter(p -> p.getAreaSuperior() == director.getAreaOperativa()).collect(Collectors.toList());
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.getPersonalDocenteColaborador() desde colaboradores");
//            l.stream().map(d -> d.getCategoriaOficial().getCategoria()).forEach(System.out::println);
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.getPersonalDocenteColaborador() en BD");
//            l.stream().map(d -> d.getCategoriaOficial().getCategoria()).forEach(System.out::println);
        }

        return l;
    }

    @Override
    public List<PlaneacionesCuatrimestrales> inicializarPlaneaciones(@NonNull List<Personal> colaboradores, PeriodosEscolares periodo, Personal director) {
        final List<PlaneacionesCuatrimestrales> l = f.getEntityManager().createQuery("SELECT pc FROM PlaneacionesCuatrimestrales pc WHERE pc.periodo=:periodo AND pc.director.clave=:director", PlaneacionesCuatrimestrales.class)
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("director", director.getClave())
                .getResultList();
        
        if(l.isEmpty()){
            colaboradores.stream().forEach(p -> {
    //            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.inicializarPlaneaciones() p: " + p);
                PlaneacionesCuatrimestrales pc = agregarDocente(colaboradores, director, p, periodo);
    //            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.inicializarPlaneaciones() pc: " + pc);
                l.add(pc);
            });
        }

        return l;
    }

    @Override
    public PlaneacionesCuatrimestrales agregarDocente(@NonNull List<Personal> colaboradores, Personal director, Personal docente, PeriodosEscolares periodo) {
//        PlaneacionesCuatrimestrales pc = new PlaneacionesCuatrimestrales
//        colaboradores.add(docente);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente() docente: " + docente);

        PlaneacionesCuatrimestrales pc = new PlaneacionesCuatrimestrales();
        pc.setEstadias((short) 0);
        pc.setComentariosDirector(" ");
        pc.setComentariosJefePersonal(" ");
        pc.setComentariosSecretarioAcademico(" ");
        pc.setComentariosSistema("Correcta.");
        pc.setDirector(director);
        pc.setDocente(docente);
        pc.setFechaValidacionJefePersonal(null);
        pc.setFechaValidacionSecretarioAdemico(null);
        pc.setPeriodo(periodo.getPeriodo());
        pc.setPlaneacion(docente.getClave());
        pc.setValidacionJefePersonal(false);
        pc.setValidacionSecretarioAdemico(false);
        pc.setValidacionSistema(true);

        List<PlaneacionesCuatrimestrales> lpc = f.getEntityManager().createQuery("SELECT pc FROM PlaneacionesCuatrimestrales pc WHERE pc.director.clave=:director AND pc.docente.clave=:docente AND pc.periodo=:periodo", PlaneacionesCuatrimestrales.class)
                .setParameter("director", director.getClave())
                .setParameter("docente", docente.getClave())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultList();
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente() lpc.size: " + lpc.size());
        switch (docente.getCategoriaOficial().getCategoria()) {
            case 32: //ptc
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente() es ptc");
                if (lpc.isEmpty()) {
                    final short asignacionPTCActividadesVariasMaximo = (short) (ep.leerPropiedadEntera("asignacionPTCActividadesVariasMaximo").getAsInt() - 2);
                    final short asignacionPTCAsesoriaClaseMaximo = (short) ep.leerPropiedadEntera("asignacionPTCAsesoriaClaseMaximo").getAsInt();
                    final short asignacionPTCHorasClaseMinimo =(short) (ep.leerPropiedadEntera("asignacionPTCHorasClaseMinimo").getAsInt() / 2 + 1);
                    final short asignacionPTCProyectoInvestigacionMinimo = (short) (ep.leerPropiedadEntera("asignacionPTCProyectoInvestigacionMinimo").getAsInt());
//                        final short asignacionPTCReunionAcademiaMaximo = (short) ep.leerPropiedadEntera("asignacionPTCReunionAcademiaMaximo").getAsInt();
                    final short asignacionPTCTutoriaIndividualMaximo = (short) ep.leerPropiedadEntera("asignacionPTCTutoriaIndividualMaximo").getAsInt();
                    final short asignacionPTCTutoriaGrupalMaximo = (short) ep.leerPropiedadEntera("asignacionPTCTutoriaGrupalMaximo").getAsInt();
                    pc.setActividadesVarias(asignacionPTCActividadesVariasMaximo);
                    pc.setAsesoriaClase(asignacionPTCAsesoriaClaseMaximo);
                    pc.setHorasClaseIng(asignacionPTCHorasClaseMinimo);
                    pc.setHorasClaseTsu(asignacionPTCHorasClaseMinimo);
                    pc.setProyectoInvestigacion(asignacionPTCProyectoInvestigacionMinimo);
                    pc.setReunionAcademia((short) 0);
                    pc.setTutoriaIndividual(asignacionPTCTutoriaIndividualMaximo);
                    pc.setTutoriaGrupal(asignacionPTCTutoriaGrupalMaximo);
                    pc.setTotal(Caster.planeacionToTotal(pc));
                    f.setEntityClass(PlaneacionesCuatrimestrales.class);
                    f.create(pc);
                    validarPlaneacion(pc);
                    guardarPlaneacion(pc);
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente(ptc) pc creada: " + pc);
                    return pc;
                } else {
                    pc = lpc.get(0);
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente(ptc) pc leida: " + pc);
                    return pc;
                }
//                break;
            case 30: //pa
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente() es pa");
                if (lpc.isEmpty()) {
//                        final short asignacionPAActividadesVariasMaximo = (short) ep.leerPropiedadEntera("asignacionPAActividadesVariasMaximo").getAsInt();
//                        final short asignacionPAAsesoriaClaseMaximo = (short) ep.leerPropiedadEntera("asignacionPAAsesoriaClaseMaximo").getAsInt();
//                        final short asignacionPACategoriaOficial = (short) ep.leerPropiedadEntera("asignacionPACategoriaOficial").getAsInt();
                    final short asignacionPAHorasClaseMaximo = (short) (ep.leerPropiedadEntera("asignacionPAHorasClaseMaximo").getAsInt() / 2);
//                        final short asignacionPAHorasClaseMinimo = (short) ep.leerPropiedadEntera("asignacionPAHorasClaseMinimo").getAsInt();
//                        final short asignacionPAReunionAcademiaMaximo = (short) ep.leerPropiedadEntera("asignacionPAReunionAcademiaMaximo").getAsInt();
//                        final short asignacionPATutoriaGrupalMaximo = (short) ep.leerPropiedadEntera("asignacionPATutoriaGrupalMaximo").getAsInt();
//                        final short asignacionPATutoriaIndividualMaximo = (short) ep.leerPropiedadEntera("asignacionPATutoriaIndividualMaximo").getAsInt();
                    pc.setActividadesVarias((short) 0);
                    pc.setAsesoriaClase((short) 0);
                    pc.setHorasClaseIng(asignacionPAHorasClaseMaximo);
                    pc.setHorasClaseTsu(asignacionPAHorasClaseMaximo);
                    pc.setProyectoInvestigacion((short) 0);
                    pc.setReunionAcademia((short) 0);
                    pc.setTutoriaIndividual((short) 0);
                    pc.setTutoriaGrupal((short) 0);
                    pc.setTotal(Caster.planeacionToTotal(pc));
                    f.setEntityClass(PlaneacionesCuatrimestrales.class);
                    f.create(pc);
                    validarPlaneacion(pc);
                    guardarPlaneacion(pc);
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente(pa) pc creada: " + pc);
                    return pc;
                } else {
                    pc = lpc.get(0);
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente(pa) pc leida: " + pc);
                    return pc;
                }
//                break;
            case 41: //lab
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente() es lab");
                if (lpc.isEmpty()) {
                    final short asignacionLABHorasClaseMaximo = (short) (ep.leerPropiedadEntera("asignacionLABHorasClaseMaximo").getAsInt() / 2);
                    pc.setActividadesVarias((short) 0);
                    pc.setAsesoriaClase((short) 0);
                    pc.setHorasClaseIng(asignacionLABHorasClaseMaximo);
                    pc.setHorasClaseTsu(asignacionLABHorasClaseMaximo);
                    pc.setProyectoInvestigacion((short) 0);
                    pc.setReunionAcademia((short) 0);
                    pc.setTutoriaIndividual((short) 0);
                    pc.setTutoriaGrupal((short) 0);
                    pc.setTotal(Caster.planeacionToTotal(pc));
                    f.setEntityClass(PlaneacionesCuatrimestrales.class);
                    f.create(pc);
                    validarPlaneacion(pc);
                    guardarPlaneacion(pc);
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente(lab) pc creada: " + pc);
                    return pc;
                } else {
                    pc = lpc.get(0);
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente(lab) pc leida: " + pc);
                    return pc;
                }
            default:
                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarDocente() es desconocido");
                return null;
        }
    }

    @Override
    public void eliminarDocente(List<Personal> colaboradores, PlaneacionesCuatrimestrales planeacion, List<PlaneacionesCuatrimestrales> planeaciones) {
        colaboradores.remove(planeacion.getDocente());
        planeaciones.remove(planeacion);
        f.setEntityClass(PlaneacionesCuatrimestrales.class);
        f.remove(planeacion);
        f.flush();
    }

    @Override
    public Map.Entry<Boolean, String> validarPlaneacion(PlaneacionesCuatrimestrales planeacion) {
        Map<Boolean, String> map = new HashMap<>();
        if (planeacion == null) {
            map.put(Boolean.FALSE, "La planeación es nula");
            return map.entrySet().stream().iterator().next();
        }

        if (planeacion.getDocente() == null) {
            map.put(Boolean.FALSE, "El docente de la planeación es nulo");
            return map.entrySet().stream().iterator().next();
        }

        if (planeacion.getDirector() == null) {
            map.put(Boolean.FALSE, "El director de la planeación es nulo");
            return map.entrySet().stream().iterator().next();
        }

        //validar horas clase
        final short asignacionPTCCategoriaOficial = (short) ep.leerPropiedadEntera("asignacionPTCCategoriaOficial").getAsInt();
        final short asignacionPACategoriaOficial = (short) ep.leerPropiedadEntera("asignacionPACategoriaOficial").getAsInt();
        final short asignacionLABCategoriaOficial = (short) ep.leerPropiedadEntera("asignacionLABCategoriaOficial").getAsInt();
        final short asignacionActividadesNoFrenteAGrupoMaximo = (short) ep.leerPropiedadEntera("asignacionActividadesNoFrenteAGrupoMaximo").getAsInt();

        Validador<PlaneacionesCuatrimestrales> validador;
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.validarPlaneacion() planeacion.getDocente().getCategoriaOficial().getCategoria(): " + planeacion.getDocente().getCategoriaOficial().getCategoria());
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.validarPlaneacion() asignacionPTCCategoriaOficial: " + asignacionPTCCategoriaOficial);
        if (planeacion.getDocente().getCategoriaOficial().getCategoria() == asignacionPTCCategoriaOficial) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.validarPlaneacion(): es PTC");
            final short asignacionPTCActividadesVariasMaximo = (short) ep.leerPropiedadEntera("asignacionPTCActividadesVariasMaximo").getAsInt();
            final short asignacionPTCAsesoriaClaseMaximo = (short) ep.leerPropiedadEntera("asignacionPTCAsesoriaClaseMaximo").getAsInt();
            final short asignacionPTCHorasClaseMaximo = (short) ep.leerPropiedadEntera("asignacionPTCHorasClaseMaximo").getAsInt();
            final short asignacionPTCHorasClaseMinimo = (short) ep.leerPropiedadEntera("asignacionPTCHorasClaseMinimo").getAsInt();
            final short asignacionPTCProyectoInvestigacionMaximo = (short) ep.leerPropiedadEntera("asignacionPTCProyectoInvestigacionMaximo").getAsInt();
            final short asignacionPTCProyectoInvestigacionMinimo = (short) ep.leerPropiedadEntera("asignacionPTCProyectoInvestigacionMinimo").getAsInt();
            final short asignacionPTCReunionAcademiaMaximo = (short) ep.leerPropiedadEntera("asignacionPTCReunionAcademiaMaximo").getAsInt();
            final short asignacionPTCTutoriaIndividualMaximo = (short) ep.leerPropiedadEntera("asignacionPTCTutoriaIndividualMaximo").getAsInt();
            final short asignacionPTCTutoriaGrupalMaximo = (short) ep.leerPropiedadEntera("asignacionPTCTutoriaGrupalMaximo").getAsInt();
            validador = new ValidadorPlaneacionCuatrimestralPTC(planeacion, asignacionPTCActividadesVariasMaximo, asignacionPTCAsesoriaClaseMaximo, asignacionPTCHorasClaseMaximo,
                    asignacionPTCHorasClaseMinimo, asignacionPTCProyectoInvestigacionMaximo, asignacionPTCProyectoInvestigacionMinimo, asignacionPTCReunionAcademiaMaximo,
                    asignacionPTCTutoriaIndividualMaximo, asignacionPTCTutoriaGrupalMaximo, asignacionActividadesNoFrenteAGrupoMaximo);
        } else if (planeacion.getDocente().getCategoriaOficial().getCategoria() == asignacionLABCategoriaOficial) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.validarPlaneacion(): es LAB");
            final short asignacionLABHorasClaseMaximo = (short) ep.leerPropiedadEntera("asignacionLABHorasClaseMaximo").getAsInt();
            final short asignacionLABHorasClaseMinimo = (short) ep.leerPropiedadEntera("asignacionLABHorasClaseMinimo").getAsInt();
            final short asignacionLABReunionAcademiaMaximo = (short) ep.leerPropiedadEntera("asignacionLABReunionAcademiaMaximo").getAsInt();
            validador = new ValidadorPlaneacionCuatrimestralLAB(planeacion, asignacionLABHorasClaseMaximo, asignacionLABHorasClaseMinimo, asignacionLABReunionAcademiaMaximo);
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.validarPlaneacion(): es PA");
            final short asignacionPAActividadesVariasMaximo = (short) ep.leerPropiedadEntera("asignacionPAActividadesVariasMaximo").getAsInt();
            final short asignacionPAAsesoriaClaseMaximo = (short) ep.leerPropiedadEntera("asignacionPAAsesoriaClaseMaximo").getAsInt();
            final short asignacionPAHorasClaseMaximo = (short) ep.leerPropiedadEntera("asignacionPAHorasClaseMaximo").getAsInt();
            final short asignacionPAHorasClaseMinimo = (short) ep.leerPropiedadEntera("asignacionPAHorasClaseMinimo").getAsInt();
            final short asignacionPAReunionAcademiaMaximo = (short) ep.leerPropiedadEntera("asignacionPAReunionAcademiaMaximo").getAsInt();
            final short asignacionPATutoriaIndividualMaximo = (short) ep.leerPropiedadEntera("asignacionPATutoriaIndividualMaximo").getAsInt();
            final short asignacionPATutoriaGrupalMaximo = (short) ep.leerPropiedadEntera("asignacionPATutoriaGrupalMaximo").getAsInt();
            validador = new ValidadorPlaneacionCuatrimestralPA(planeacion, asignacionPAActividadesVariasMaximo, asignacionPAAsesoriaClaseMaximo, asignacionPAHorasClaseMaximo, asignacionPAHorasClaseMinimo, asignacionPAReunionAcademiaMaximo, asignacionPATutoriaIndividualMaximo, asignacionPATutoriaGrupalMaximo, asignacionActividadesNoFrenteAGrupoMaximo);
        }

//        boolean esCorrecto = validador.esCorrecta(planeacion);
        Future<Boolean> res = exe.submit((Callable<Boolean>) validador);
        try {
            boolean tengoElValor = res.isDone();
            while (!tengoElValor) {
                Thread.sleep(150);
                tengoElValor = res.isDone();
            }

            boolean esCorrecto = res.get();
            if (esCorrecto) {
                map.put(esCorrecto, "La validación de la planeación cuatrimestral es correcta.");
                return map.entrySet().stream().iterator().next();
            } else {
                map.put(esCorrecto, "La validación de la planeación cuatrimestral es incorrecta. " + planeacion.getComentariosSistema());
                return map.entrySet().stream().iterator().next();
            }
        } catch (InterruptedException | ExecutionException ex) {
            throw new IllegalStateException("No se pudo validar la planeación", ex);
        }
    }

    @Override
    public void guardarPlaneacion(PlaneacionesCuatrimestrales planeacion) {
        f.edit(planeacion);
    }

//    @Override
    public void validar(@NonNull Personal validador, PlaneacionesCuatrimestrales planeacion) {
        Map.Entry<Boolean, String> entrada = validarPlaneacion(planeacion);
        if (entrada.getKey()) {
            if (validador.equals(getSecretarioAcademico())) {
                planeacion.setValidacionSecretarioAdemico(true);
            } else if (validador.equals(getJefePersonal())) {
                planeacion.setValidacionJefePersonal(true);
            } else {
                throw new RuntimeException("No se puede validar la planeación cuatrimestral debido a que el usuario no tiene privilegios como Secretaria(o) Académica(o) o Jefa(e) de Personal.");
            }
        } else {
            throw new RuntimeException("No se puede guardar debido a que la validación de la planeación cuatrimestral es incorrecta. Mensaje: " + entrada.getValue());
        }

        guardarPlaneacion(planeacion);
    }

    @Override
    public List<PlaneacionesCuatrimestrales> getPlaneacionesPorDirectorPeriodo(Personal director, PeriodosEscolares periodo) {
        TypedQuery<PlaneacionesCuatrimestrales> q = f.getEntityManager().createQuery("SELECT pc FROM PlaneacionesCuatrimestrales pc WHERE pc.director.clave=:claveDirector AND pc.periodo=:periodo", PlaneacionesCuatrimestrales.class);
        q.setParameter("claveDirector", director.getClave());
        q.setParameter("periodo", periodo.getPeriodo());

        return q.getResultList();
    }

    @Override
    public Map<Personal, List<PlaneacionesCuatrimestrales>> getPlaneacionesPorPeriodo(PeriodosEscolares periodo) {
        Map<Personal, List<PlaneacionesCuatrimestrales>> map = new HashMap<>();
        List<Personal> directores = getListaDirectores();

        directores.stream().forEach(d -> {
            map.put(d, getPlaneacionesPorDirectorPeriodo(d, periodo));
        });

        return map;
    }

//    @Override
    public List<Personal> getListaDirectores() {
        final short directorAreaSuperior = (short) ep.leerPropiedadEntera("directorAreaSuperior").getAsInt();
        final short directorCategoriaOperativa = (short) ep.leerPropiedadEntera("directorCategoriaOperativa").getAsInt();

        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.areaSuperior=:areaSuperior AND p.categoriaOperativa.categoria=:categoriaOperativa ORDER BY p.nombre", Personal.class);
        q.setParameter("areaSuperior", directorAreaSuperior);
        q.setParameter("categoriaOperativa", directorCategoriaOperativa);

        return q.getResultList();
    }

    @Override
    public void validar(Personal validador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map.Entry<Boolean, PlaneacionesCuatrimestrales> detectarReunionAcademia(Personal docente, Personal director, PeriodosEscolares periodo) {
        List<PlaneacionesCuatrimestrales> l = f.getEntityManager().createQuery("SELECT pc FROM PlaneacionesCuatrimestrales pc WHERE pc.docente.clave=:docente AND pc.periodo=:periodo AND pc.reunionAcademia>:horas AND pc.director.clave<>:director", PlaneacionesCuatrimestrales.class)
                .setParameter("docente", docente.getClave())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("horas", (short) 0)
                .setParameter("director", director.getClave())
                .getResultList();

        Map<Boolean, PlaneacionesCuatrimestrales> map = new HashMap<>();
        if (l.isEmpty()) {
            map.put(Boolean.FALSE, null);
        } else {
            map.put(Boolean.TRUE, l.get(0));
        }
        return map.entrySet().iterator().next();
    }

    @Override
    public void ordenarPlaneaciones(List<PlaneacionesCuatrimestrales> planeaciones) {
        Collections.sort(planeaciones,
                Comparator.comparing(a -> a.getDocente().getCategoriaOficial().getNombre().concat(a.getDocente().getNombre()))
        );
    }

    @Override
    public List<PlaneacionesLiberaciones> buscarPlaneacionLiberada(Integer periodo, Integer director) {
        TypedQuery<PlaneacionesLiberaciones> q = f.getEntityManager().createQuery("SELECT p FROM PlaneacionesLiberaciones p WHERE p.planeacionesLiberacionesPK.periodo = :periodo AND p.planeacionesLiberacionesPK.director = :director", PlaneacionesLiberaciones.class);
        q.setParameter("periodo", periodo);
        q.setParameter("director", director);

        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<PlaneacionesLiberaciones> buscarPlaneacionesLiberadasParaValidarSecretarioAcademico() {
        TypedQuery<PlaneacionesLiberaciones> q = f.getEntityManager().createQuery("SELECT p FROM PlaneacionesLiberaciones p WHERE p.liberacionDirector = :liberacionDirector", PlaneacionesLiberaciones.class);
        q.setParameter("liberacionDirector", true);

        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<PlaneacionesLiberaciones> buscarPlaneacionesLiberadasParaValidarJefePersonal() {
        TypedQuery<PlaneacionesLiberaciones> q = f.getEntityManager().createQuery("SELECT p FROM PlaneacionesLiberaciones p WHERE p.validacionSecretarioAcademico = :validacionSecretarioAcademico", PlaneacionesLiberaciones.class);
        q.setParameter("validacionSecretarioAcademico", true);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.buscarPlaneacionesLiberadasParaValidarJefePersonal()"+q.getResultList());
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public void actualizarPlaneacionLiberada(PlaneacionesLiberaciones planaeacionLiberadaActualizada) {
        f.setEntityClass(PlaneacionesLiberaciones.class);
        f.edit(planaeacionLiberadaActualizada);
        f.flush();
    }

    @Override
    public void agregarPlaneacionLiberada(PlaneacionesLiberaciones nuevaPlaneacionLiberada) {
        f.setEntityClass(PlaneacionesLiberaciones.class);
        f.create(nuevaPlaneacionLiberada);
        f.flush();
    }

    @Override
    public List<Actividadesvarias> buscarActividadesVarias() {
        TypedQuery<Actividadesvarias> q = f.getEntityManager().createQuery("SELECT a FROM Actividadesvarias a", Actividadesvarias.class);
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<Atividadesvariasplaneacionescuatrimestrales> buscarActividadesvariasplaneacionescuatrimestraleses(Integer planeacion) {
        TypedQuery<Atividadesvariasplaneacionescuatrimestrales> q = f.getEntityManager().createQuery(""
                + "SELECT a FROM Atividadesvariasplaneacionescuatrimestrales a WHERE a.atividadesvariasplaneacionescuatrimestralesPK.planeacion = :planeacion", Atividadesvariasplaneacionescuatrimestrales.class);
        q.setParameter("planeacion", planeacion);
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public Atividadesvariasplaneacionescuatrimestrales agregarAtividadesvariasplaneacionescuatrimestrales(Atividadesvariasplaneacionescuatrimestrales nuevaAtividadesvariasplaneacionescuatrimestrales) {
        f.setEntityClass(Atividadesvariasplaneacionescuatrimestrales.class);
        Atividadesvariasplaneacionescuatrimestrales busquedaAtividadesvariasplaneacionescuatrimestrales = new Atividadesvariasplaneacionescuatrimestrales();
        busquedaAtividadesvariasplaneacionescuatrimestrales = (Atividadesvariasplaneacionescuatrimestrales) f.find(nuevaAtividadesvariasplaneacionescuatrimestrales.getAtividadesvariasplaneacionescuatrimestralesPK());
        if (busquedaAtividadesvariasplaneacionescuatrimestrales == null) {
            f.create(nuevaAtividadesvariasplaneacionescuatrimestrales);
            f.flush();
            return nuevaAtividadesvariasplaneacionescuatrimestrales;
        } else {
            if ((nuevaAtividadesvariasplaneacionescuatrimestrales.getAtividadesvariasplaneacionescuatrimestralesPK().getActividad() == busquedaAtividadesvariasplaneacionescuatrimestrales.getAtividadesvariasplaneacionescuatrimestralesPK().getActividad())
                    && (nuevaAtividadesvariasplaneacionescuatrimestrales.getAtividadesvariasplaneacionescuatrimestralesPK().getPlaneacion() == busquedaAtividadesvariasplaneacionescuatrimestrales.getAtividadesvariasplaneacionescuatrimestralesPK().getPlaneacion())) {
                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarAtividadesvariasplaneacionescuatrimestrales()1");
                return null;
            } else {
                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioPlaneacionCuatrimestral.agregarAtividadesvariasplaneacionescuatrimestrales()2");
                f.create(nuevaAtividadesvariasplaneacionescuatrimestrales);
                f.flush();
                return nuevaAtividadesvariasplaneacionescuatrimestrales;
            }
        }
    }

    @Override
    public Actividadesvarias agregarActividadesvarias(Actividadesvarias nuevaActividadesvarias) {
        f.setEntityClass(Actividadesvarias.class);
        f.create(nuevaActividadesvarias);
        f.flush();
        return nuevaActividadesvarias;
    }

    @Override
    public void eliminarActividadVaria(Atividadesvariasplaneacionescuatrimestrales nuevaActividadesvarias) {
        f.setEntityClass(Atividadesvariasplaneacionescuatrimestrales.class);
        f.remove(nuevaActividadesvarias);
        f.flush();
    }

    @Override
    public void actualizarAtividadesvariasplaneacionescuatrimestrales(Atividadesvariasplaneacionescuatrimestrales nuevaActividadesvarias) {
        f.setEntityClass(Atividadesvariasplaneacionescuatrimestrales.class);
        f.edit(nuevaActividadesvarias);
        f.flush();
    }

    @Override
    public List<PlaneacionesCuatrimestrales> buscarPlaneacionesCuatrimestrales() {
        TypedQuery<PlaneacionesCuatrimestrales> q = f.getEntityManager().createQuery("SELECT p FROM PlaneacionesCuatrimestrales p", PlaneacionesCuatrimestrales.class);
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<PlaneacionesDetalles> buscraPlaneacionesDetalles(Integer periodo, Integer director) {
        TypedQuery<PlaneacionesDetalles> q = f.getEntityManager().createQuery("SELECT p FROM PlaneacionesDetalles p WHERE p.planeacionesDetallesPK.periodo = :periodo AND p.planeacionesDetallesPK.director = :director", PlaneacionesDetalles.class);
        q.setParameter("periodo", periodo);
        q.setParameter("director", director);
        if (q.getResultList() == null || q.getResultList().size() == 0) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public void actualizarPlaneacionesDetalles(PlaneacionesDetalles nuevaPlaneacionesDetalles) {
        f.setEntityClass(PlaneacionesDetalles.class);
        f.edit(nuevaPlaneacionesDetalles);
        f.flush();
    }

    @Override
    public void agregarPlaneacionesDetalles(PlaneacionesDetalles nuevaPlaneacionesDetalles) {
        f.setEntityClass(PlaneacionesDetalles.class);
        f.create(nuevaPlaneacionesDetalles);
        f.flush();
    }

    @Override
    public List<VistaTotalAlumnosCarreraPye> mostraralumnosProximosAEstadia() {
        TypedQuery<VistaTotalAlumnosCarreraPye> q = f2.getEntityManager().createQuery("SELECT v FROM VistaTotalAlumnosCarreraPye v", VistaTotalAlumnosCarreraPye.class);
        if (q.getResultList() == null || q.getResultList().isEmpty()) {
            return null;
        } else {
            return q.getResultList();
        }
    }

}
