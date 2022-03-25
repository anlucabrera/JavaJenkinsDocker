/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluacion;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;

import mx.edu.utxj.pye.sgi.entity.ch.view.*;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Calculable;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor2;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor3;
import mx.edu.utxj.pye.sgi.funcional.PromediarDesempenio;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EjbAdministracionEncuestas{

    @EJB Facade f;
    @EJB Facade2 f2;
    private EntityManager em;
    private EntityManager em2;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarPersonal(Integer clave){   
        try {
            String pista = ",".concat(String.valueOf(clave).trim()).concat(",").trim();
            ConfiguracionPropiedades claveConfiguracionPropiedad = Objects.requireNonNull(em.createQuery("SELECT c FROM ConfiguracionPropiedades c WHERE c.tipo = :tipo AND c.clave = :clave AND c.valorCadena LIKE CONCAT('%',:pista,'%')", ConfiguracionPropiedades.class)
                    .setParameter("tipo", "Cadena")
                    .setParameter("pista", pista)
                    .setParameter("clave", "seguimientoEvaluaciones")
                    .getResultStream()
                    .findFirst().orElse(new ConfiguracionPropiedades()));
            if (claveConfiguracionPropiedad.getClave() == null || claveConfiguracionPropiedad.getClave().equals("") || claveConfiguracionPropiedad.getClave().isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se ha encontrado la clave del trabajador, no tiene asignado el módulo");
            } else {
                PersonalActivo p = ejbPersonalBean.pack(clave);
                Filter<PersonalActivo> filtro = new Filter<>();
                filtro.setEntity(p);
                return ResultadoEJB.crearCorrecto(filtro, "Se ha encontrado la clave de trabajador asignada a este módulo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar correctamente la validación. (EjbValidacionRol.validarPsicopedagogia)", e, null);
        }
    }
    
    public PeriodosEscolares getPeriodoActual() {
//        TypedQuery<PeriodosEscolares> q = f.getEntityManager().createQuery("SELECT MAX (p.periodo) FROM PeriodosEscolares AS p ", PeriodosEscolares.class);
//        return (PeriodosEscolares)q.getSingleResult();

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("prontuario.periodoActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.getPeriodoActual()" + l.get(0));
            return l.get(0);
        }
    }

    
    public List<Grupos> esTutor(Integer maestro, Integer periodo) {
//       //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.esTutor() esta es la clave del maestro obtenida : --- > " + maestro);
        TypedQuery<Grupos> q = f2.getEntityManager().createQuery("SELECT g FROM Grupos AS g WHERE g.cveMaestro = :cveMaestro AND g.gruposPK.cvePeriodo = :cvePeriodo", Grupos.class);
        q.setParameter("cveMaestro", maestro);
        q.setParameter("cvePeriodo", periodo);
        List<Grupos> lg = q.getResultList();
        if (lg.isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    
    public List<Alumnos> getListaDeAlumnosPorDocente(Integer grupo) {
        TypedQuery<Alumnos> q = f2.getEntityManager().createQuery(" SELECT a FROM Alumnos AS a WHERE a.grupos.gruposPK.cveGrupo = :grupo ", Alumnos.class);
        q.setParameter("grupo", grupo);
//        q.setParameter("periodo", periodo);
        List<Alumnos> la = q.getResultList();
        if (la == null || la.isEmpty()) {
            return new ArrayList<>();
        } else {
//            for (int i = la.size()-1; i >= la.size()-1; i--) {
//               //System.out.println(" alumno " + la);
//            }
            return la;

        }

    }

    
    public List<VistaAlumnosPye> findAllByMatricula(String matricula) {
//        List<VistaAlumnosPye> salida;
        Query q = f2.getEntityManager().createNativeQuery("SELECT * from [saiiut].[vista_alumnos_pye] as v  where v.matricula = ?1 and v.cve_periodo is not null", VistaAlumnosPye.class);
        //TypedQuery<VistaAlumnosPye> q =  //em.createNamedQuery("VistaAlumnosPye.findByMatricula", VistaAlumnosPye.class);
        q.setParameter(1, matricula);
//        salida = q.getResultList();
        return q.getResultList();
    }

    
    public List<Personal> esDirectorDeCarrera(Short areaSup, Integer actividad, Integer catOp, Integer clave) {
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and  p.categoriaOperativa.categoria= :categoria AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("clave", clave);
//       //System.out.println("el directivo es : " + q.getResultList());
        return q.getResultList();
    }

    
    public List<AreasUniversidad> obtenerAreasDirector(Short identificador, String estatus) {
       //System.out.println("parametro identificador : " + identificador + " parametro estatus : " + estatus);
        AreasUniversidad a = f.getEntityManager().find(AreasUniversidad.class, identificador);
       //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.obtenerAreasDirector() el area es : " + a);
//        TypedQuery<AreasUniversidad> q = f.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior = :area", AreasUniversidad.class);
        TypedQuery<AreasUniversidad> q = f.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior = :area and a.vigente = :vigente", AreasUniversidad.class);

        q.setParameter("area", identificador);
        q.setParameter("vigente", "1");
       //System.out.println("resultado de la consulta " + q.getResultList());
        List<AreasUniversidad> lp = q.getResultList();
        if (lp == null || lp.isEmpty()) {
            return null;
        } else {
           //System.out.println("lista de carreras" + lp);
           //System.out.println("las carreras son :  ");
            lp.forEach(System.err::println);
            return lp;
        }
    }

    
    public List<AreasUniversidad> obtenerAreasPorDirectorReporteSA(Short areaSeleccionada) {
       //System.out.println("el area seleccionada es : " + areaSeleccionada);
        List<AreasUniversidad> lcarrera = new ArrayList<>();
        AreasUniversidad a = f.getEntityManager().find(AreasUniversidad.class, areaSeleccionada);
        TypedQuery<AreasUniversidad> ac = f.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior = :area", AreasUniversidad.class);
        ac.setParameter("area", a.getArea());
        List<AreasUniversidad> lc = ac.getResultList();
        if (lc == null) {
           //System.out.println("nos e encontraron carreras adjudicadas a esta area");
            return null;
        } else {
           //System.out.println("la lista de carreras ontenidas en el EBJ de el area " + a.getNombre() + " es : ");
            lc.forEach(System.err::println);
            lc.forEach(x -> {
                if ("1".equalsIgnoreCase(x.getVigente())) {
                   //System.out.println("es valida la carrera");
                    lcarrera.add(x);
                } else {
                   //System.out.println("El area : " + x.getNombre() + " no se encuentra activa y no tiene datos que evaluar");
                }
            });
            return lcarrera;
        }
    }

    
    public List<ListaEvaluacionDocentesResultados> resultadosEvaluacionGlobalDirector(String siglas, Integer evaluacion) {
       //System.out.println("EJB siglas : " + siglas + ", EJB Evaluacion : " + evaluacion);
        TypedQuery<ListaEvaluacionDocentesResultados> q = f.getEntityManager().createQuery("SELECT l FROM ListaEvaluacionDocentesResultados l WHERE l.siglas = :siglas and l.evaluacion = :evaluacion", ListaEvaluacionDocentesResultados.class);
        q.setParameter("siglas", siglas);
        q.setParameter("evaluacion", evaluacion);
        List<ListaEvaluacionDocentesResultados> ld = q.getResultList();
        if (ld == null || ld.isEmpty()) {
           //System.out.println("la lista de resultados por  area esta vacia");
            return null;
        } else {
           //System.out.println("lista de resultados por director" + ld.size());
            return ld;
        }
    }

    
    public Evaluaciones360 getEvaluacion360Administracion(Integer periodo) {
        TypedQuery<Evaluaciones360> q = f.getEntityManager().createQuery("SELECT e from Evaluaciones360 e WHERE e.periodo = :periodo", Evaluaciones360.class);
        q.setParameter("periodo", periodo);
        List<Evaluaciones360> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    
    public DesempenioEvaluaciones getEvaluacionDesempenioAdministracion(Integer periodo) {
        TypedQuery<DesempenioEvaluaciones> q = f.getEntityManager().createQuery("SELECT d from DesempenioEvaluaciones d WHERE d.periodo = :periodo", DesempenioEvaluaciones.class);
        q.setParameter("periodo", periodo);
        List<DesempenioEvaluaciones> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    
    public List<Evaluaciones360Resultados> getEvaluaciones360ResultadosSubordinados(Evaluaciones360 evaluacion, Integer evaluado) {
        List<Evaluaciones360Resultados> lista = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion and e.evaluaciones360ResultadosPK.evaluado = :evaluado", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .setParameter("evaluado", evaluado)
                .getResultList();
        if(lista.isEmpty()) return Collections.EMPTY_LIST;
        return lista;
    }

    
    public List<DesempenioEvaluacionResultados> getEvaluacionesDesempenioSubordinados(Integer evaluacion, Integer evaluado) {
        List<DesempenioEvaluacionResultados> lista = em.createQuery("select e from DesempenioEvaluacionResultados as e where e.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion and e.desempenioEvaluacionResultadosPK.evaluado = :evaluado", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", evaluado)
                .getResultList();
        if(lista.isEmpty()) return Collections.EMPTY_LIST;
        return lista;
    }

    
    public Evaluaciones getEvaluaciones(Integer periodo, String tipo) {
        Evaluaciones e = em.createQuery("SELECT e FROM Evaluaciones as e WHERE e.periodo = :periodo AND e.tipo LIKE CONCAT('%',:tipo,'%')", Evaluaciones.class)
                .setParameter("periodo", periodo)
                .setParameter("tipo", tipo)
                .getResultStream()
                .findFirst()
                .orElse(new Evaluaciones());
        return e;
    }

    
    public List<EvaluacionTutoresResultados> getEvaluacionesTutoresResultados(Evaluaciones evaluacion, Integer evaluado) {
        TypedQuery<EvaluacionTutoresResultados> q = f.getEntityManager().createQuery("SELECT e from EvaluacionTutoresResultados e where e.evaluaciones.evaluacion = :evaluacion and e.personal.clave = :evaluado", EvaluacionTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluado", evaluado);
        List<EvaluacionTutoresResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    
    public EvaluacionDocentesMaterias getEvaluacionDoncete(Integer periodo) {
        TypedQuery<EvaluacionDocentesMaterias> q = f.getEntityManager().createQuery("SELECT e from EvaluacionDocentesMaterias e WHERE e.periodo = :periodo", EvaluacionDocentesMaterias.class);
        q.setParameter("periodo", periodo);
        List<EvaluacionDocentesMaterias> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    
    public List<EvaluacionDocentesMateriaResultados> getEvaluacionDocentesResultadosPromedioGeneral(EvaluacionDocentesMaterias evaluacion, Integer evaluado) {
        TypedQuery<EvaluacionDocentesMateriaResultados> q = f.getEntityManager().createQuery("SELECT e from EvaluacionDocentesMateriaResultados e WHERE e.evaluaciones.evaluacion = :evaluacion AND e.evaluacionDocentesMateriaResultadosPK.evaluado = :evaluado ", EvaluacionDocentesMateriaResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluado", evaluado);
        List<EvaluacionDocentesMateriaResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
           //System.out.println("Regresa null getEvaluacionDocentesResultadosPromedioGeneral");
            return null;
        } else {
           //System.out.println("el tamaño de la lista es : " + l.size());
            return l;
        }
    }

    
    public List<VistaEvaluacionDocenteMateriaPye> getMateriasPorDocente(Integer subordinado, Integer periodo) {
        TypedQuery<VistaEvaluacionDocenteMateriaPye> q = f2.getEntityManager().createQuery("SELECT v from VistaEvaluacionDocenteMateriaPye v WHERE v.numeroNomina = :clave AND v.periodo = :periodo", VistaEvaluacionDocenteMateriaPye.class);
        q.setParameter("periodo", periodo);
        q.setParameter("clave", subordinado.toString());
        List<VistaEvaluacionDocenteMateriaPye> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<EvaluacionDocentesMateriaResultados> getEvaluacionDocentesResultadosPromedioMateria(EvaluacionDocentesMaterias evaluacion, Integer evaluado, String materia) {
        TypedQuery<EvaluacionDocentesMateriaResultados> q = f.getEntityManager().createQuery("SELECT e from EvaluacionDocentesMateriaResultados e WHERE e.evaluaciones.evaluacion = :evaluacion AND e.evaluacionDocentesMateriaResultadosPK.evaluado = :evaluado AND e.evaluacionDocentesMateriaResultadosPK.cveMateria = :materia", EvaluacionDocentesMateriaResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluado", evaluado);
        q.setParameter("materia", materia);
        List<EvaluacionDocentesMateriaResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<EvaluacionDocentesMateriaResultados> getGlobalEvaluacionDocentes(EvaluacionDocentesMaterias evaluacion) {
        TypedQuery<EvaluacionDocentesMateriaResultados> q = f.getEntityManager().createQuery("SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE  e.evaluaciones.evaluacion = :evaluacion", EvaluacionDocentesMateriaResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        List<EvaluacionDocentesMateriaResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<ListaPersonal> getListadoDocentesPorArea(Short area) {
       //System.out.println("area seleccionada : " + area);
        if (area == 47) {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.getListadoDocentesPorArea() es el area 47");
            TypedQuery<ListaPersonal> q = f.getEntityManager().createQuery("SELECT p FROM ListaPersonal p WHERE p.areaSuperior = :area ", ListaPersonal.class);
            q.setParameter("area", area);
//            q.setParameter("status", "R");
            List<ListaPersonal> l = q.getResultList();
           //System.out.println("lista de personal de idiomas : " + q.getResultList());
            if (l.isEmpty() || l == null) {
                return null;
            } else {
               //System.out.println("la lista de docentes : ");
                l.forEach(System.err::println);
                return l;
            }
        } else if (area == 999) {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.getListadoDocentesPorArea() es el area 999");
            TypedQuery<ListaPersonal> q = f.getEntityManager().createQuery("SELECT p FROM ListaPersonal p WHERE p.actividad <> :docente and p.experienciaDocente >= :experienciaDocente", ListaPersonal.class);
//            q.setParameter("area", area);
            q.setParameter("docente", 3);
            q.setParameter("experienciaDocente", 1);
            List<ListaPersonal> l = q.getResultList();
            if (l.isEmpty() || l == null) {
                return null;
            } else {
               //System.out.println("la lista de docentes : ");
                l.forEach(System.err::println);
                return l;
            }
        } else {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.getListadoDocentesPorArea() es el area : " + area);
            TypedQuery<ListaPersonal> q = f.getEntityManager().createQuery("SELECT p FROM ListaPersonal p WHERE p.areaSuperior = :area ", ListaPersonal.class);
            q.setParameter("area", area);
//            q.setParameter("docente", 3);
//            q.setParameter("experienciaDocente", 1);
            List<ListaPersonal> l = q.getResultList();
            if (l.isEmpty() || l == null) {
                return null;
            } else {
               //System.out.println("la lista de docentes : ");
                l.forEach(System.err::println);
                return l;
            }
        }

    }

    
    public List<ListaPersonal> personalGeneral() {
        TypedQuery<ListaPersonal> q = f.getEntityManager().createQuery("SELECT l from ListaPersonal l WHERE  l.experienciaDocente >= :experienciaDocente", ListaPersonal.class);
        q.setParameter("experienciaDocente", 1);
        List<ListaPersonal> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<ListaPersonal> personalGeneralCompleta() {
        List<ListaPersonal> l = em.createQuery("SELECT l from ListaPersonal l ", ListaPersonal.class)
                .getResultList();
        if (l.isEmpty()) {
            return Collections.EMPTY_LIST;
        } else {
            return l;
        }
    }

    
    public Evaluaciones360 nuevaEvaluacion360(Evaluaciones360 evaluacion) {
        Evaluaciones360 evaBD = f.getEntityManager().createQuery("select e from Evaluaciones360 e where e.periodo=:periodo", Evaluaciones360.class)
                .setParameter("periodo", evaluacion.getPeriodo())
                .getResultStream()
                .findFirst()
                .orElse(null);
        if(evaBD != null) return  evaBD;

        evaluacion.setEvaluaciones360ResultadosList(null);
        f.create(evaluacion);
        return evaluacion;
    }

    
    public DesempenioEvaluaciones nuevaEvaluacionDesempenio(DesempenioEvaluaciones evaluacion) {
        if (evaluacion != null) {
            f.create(evaluacion);
            evaluacion.setDesempenioEvaluacionResultadosList(null);
        }
        return evaluacion;
    }

    
    public List<Evaluaciones360> getListaEvaluaciones360() {
        TypedQuery<Evaluaciones360> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones360 e", Evaluaciones360.class);
        List<Evaluaciones360> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<Evaluaciones360Resultados> getListaResultadosEvaluacion360(Integer evaluacion, Integer periodo) {
        TypedQuery<Evaluaciones360Resultados> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones360 e WHERE e.evaluacion = :evaluacion AND e.periodo = :periodo", Evaluaciones360Resultados.class);
        q.setParameter("evaluacion", evaluacion);
        q.setParameter("periodo", periodo);
        List<Evaluaciones360Resultados> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<DesempenioEvaluaciones> getListaEvaluacionesDesempenio() {
        TypedQuery<DesempenioEvaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM DesempenioEvaluaciones e", DesempenioEvaluaciones.class);
        List<DesempenioEvaluaciones> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<DesempenioEvaluacionResultados> getListaResultadosEvaluacionDesempenio(Integer evaluacion, Integer periodo) {
        TypedQuery<DesempenioEvaluacionResultados> q = f.getEntityManager().createQuery("SELECT e FROM DesempenioEvaluaciones e WHERE e.evaluacion = :evaluacion AND e.periodo = :periodo", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluacion", evaluacion);
        q.setParameter("periodo", periodo);
        List<DesempenioEvaluacionResultados> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<VistaAlumnosPye> getEstudiantesGeneral() {
        TypedQuery<VistaAlumnosPye> q = f2.getEntityManager().createQuery("SELECT e from VistaAlumnosPye e WHERE e.cvePeriodo = :periodo AND e.descripcion = :descripcion ", VistaAlumnosPye.class);
        q.setParameter("periodo", getPeriodoActual().getPeriodo());
        q.setParameter("descripcion", "Regular");
        List<VistaAlumnosPye> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<Alumnos> getAlumnosPeriodoActual() {
        TypedQuery<Alumnos> q = f2.getEntityManager().createQuery("SELECT e from Alumnos e WHERE e.grupos.gruposPK.cvePeriodo = 47", Alumnos.class);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public List<Alumno> getEstudiantesSEScolaes() {
        TypedQuery<Alumno> q = f.getEntityManager().createQuery("SELECT a from Alumno a WHERE a.alumnoPK.periodo = :periodo", Alumno.class);
        q.setParameter("periodo", getPeriodoActual().getPeriodo());
       //System.out.println("mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas.getEstudiantesSEScolaes() tamaño de lista de alumnos " + q.getResultList().size());
        List<Alumno> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public List<Alumno> getEstudiantePorMatricula(Integer matricula) {
        TypedQuery<Alumno> q = f.getEntityManager().createQuery("SELECT a from Alumno a WHERE a.alumnoPK.matricula = :matricula AND a.alumnoPK.periodo = :periodo", Alumno.class);
        q.setParameter("periodo", getPeriodoActual().getPeriodo());
        q.setParameter("matricula", matricula);
        List<Alumno> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    
    public AreasUniversidad getProgramaPorClave(Short clave) {
        TypedQuery<AreasUniversidad> q = f.getEntityManager().createQuery("SELECT p from AreasUniversidad as p WHERE p.area = :pe", AreasUniversidad.class);
        q.setParameter("pe", clave);
        List<AreasUniversidad> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }
    
    public ResultadoEJB<List<DtoEvaluacion>> obtenerEvaluaciones360(){
        List<DtoEvaluacion> lista = em.createQuery("select e from Evaluaciones360 as e ORDER BY e.periodo desc", Evaluaciones360.class)
                .getResultStream()
                .map(ev -> packEvaluacion(ev))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<DtoEvaluacion> packEvaluacion(Evaluaciones360 ev){
        if(ev.equals(new Evaluaciones360())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede se nula", DtoEvaluacion.class);
        Evaluaciones360 evBD = em.find(Evaluaciones360.class, ev.getEvaluacion());
        return ResultadoEJB.crearCorrecto(new DtoEvaluacion(evBD.getEvaluacion(), evBD.getPeriodo(), "Evaluación 360°"), "La evaluacion ha sido empaquetado");
    }
    
    public ResultadoEJB<List<DtoEvaluacion>> obtenerEvaluacionesDesempenio(){
        List<DtoEvaluacion> lista = em.createQuery("select d from DesempenioEvaluaciones as d ORDER BY d.periodo desc", DesempenioEvaluaciones.class)
                .getResultStream()
                .map(ev -> packEvaluacion(ev))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<DtoEvaluacion> packEvaluacion(DesempenioEvaluaciones ev){
        if(ev.equals(new DesempenioEvaluaciones())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede se nula", DtoEvaluacion.class);
        DesempenioEvaluaciones evBD = em.find(DesempenioEvaluaciones.class, ev.getEvaluacion());
        return ResultadoEJB.crearCorrecto(new DtoEvaluacion(evBD.getEvaluacion(), evBD.getPeriodo(), "Evaluación Desempeño"), "La evaluacion ha sido empaquetado");
    }
    
    public ResultadoEJB<List<DtoEvaluacion>> obtenerEvaluaciones(String tipo){
        List<DtoEvaluacion> listE = em.createQuery("select e from Evaluaciones as e where e.tipo LIKE CONCAT('%',:tipo,'%')", Evaluaciones.class)
                .setParameter("tipo", tipo)
                .getResultStream()
                .map(e -> packEvaluacion(e))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(listE.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacia");
        return ResultadoEJB.crearCorrecto(listE, "La lista esta completa");
    }
    
    public ResultadoEJB<DtoEvaluacion> packEvaluacion(Evaluaciones e){
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede ser nulo", DtoEvaluacion.class);
        Evaluaciones eBD = em.find(Evaluaciones.class, e.getEvaluacion());
        return ResultadoEJB.crearCorrecto(new DtoEvaluacion(eBD.getEvaluacion(), eBD.getPeriodo(), eBD.getTipo()), "Evaluación empaquetada");
    }
    
    public ResultadoEJB<Evaluaciones360> obtenerEv360(Integer evaluacion){
        Evaluaciones360 ev = em.createQuery("select e from Evaluaciones360 as e where e.evaluacion = :evaluacion", Evaluaciones360.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream()
                .findFirst()
                .orElse(new Evaluaciones360());
        if(ev.equals(new Evaluaciones360())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede ser nulo", Evaluaciones360.class);
        return ResultadoEJB.crearCorrecto(ev, "Evaluacion econtrada");
    }
    
    public ResultadoEJB<DesempenioEvaluaciones> obtenerEvDes(Integer evaluacion){
        DesempenioEvaluaciones ev = em.createQuery("select e from DesempenioEvaluaciones as e where e.evaluacion = :evaluacion", DesempenioEvaluaciones.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream()
                .findFirst()
                .orElse(new DesempenioEvaluaciones());
        if(ev.equals(new DesempenioEvaluaciones())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede ser nulo", DesempenioEvaluaciones.class);
        return ResultadoEJB.crearCorrecto(ev, "Evaluacion econtrada");
    }
    
    public ResultadoEJB<Evaluaciones> obtenerEvaluacion(Integer evaluacion){
        Evaluaciones ev = em.createQuery("select e from Evaluaciones as e where e.evaluacion = :evaluacion", Evaluaciones.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream()
                .findFirst()
                .orElse(new Evaluaciones());
        if(ev.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(1, "La evaluacion no debe ser nula", Evaluaciones.class);
        return ResultadoEJB.crearCorrecto(ev, "Evaluación encontrada");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> obtenerResultados1(Integer evaluacion){
        List<EvaluacionDocentesMateriaResultados> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados as e where e.evaluacionDocentesMateriaResultadosPK.evaluacion = :evaluacion and e.completo = :completo", EvaluacionDocentesMateriaResultados.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> obtenerResultados2(Integer evaluacion){
        List<EvaluacionDocentesMateriaResultados3> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados3 as e where e.evaluacionDocentesMateriaResultados3PK.evaluacion = :evaluacion and e.completo = :completo", EvaluacionDocentesMateriaResultados3.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> obtenerResultados3(Integer evaluacion){
        List<EvaluacionDocentesMateriaResultados4> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados4 as e where e.evaluacionDocentesMateriaResultados4PK.evaluacion = :evaluacion and e.completo = :completo", EvaluacionDocentesMateriaResultados4.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> obtenerResultados4(Integer evaluacion){
        List<EvaluacionDocentesMateriaResultados5> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados5 as e where e.evaluacionDocentesMateriaResultados5PK.evaluacion = :evaluacion and e.completo = :completo", EvaluacionDocentesMateriaResultados5.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> obtenerResultados1(Integer evaluacion, Integer clave){
        List<EvaluacionDocentesMateriaResultados> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados as e where e.evaluacionDocentesMateriaResultadosPK.evaluacion = :evaluacion and e.evaluacionDocentesMateriaResultadosPK.evaluado = :evaluado and e.completo = :completo", EvaluacionDocentesMateriaResultados.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> obtenerResultados2(Integer evaluacion, Integer clave){
        List<EvaluacionDocentesMateriaResultados3> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados3 as e where e.evaluacionDocentesMateriaResultados3PK.evaluacion = :evaluacion and e.evaluacionDocentesMateriaResultados3PK.evaluado = :evaluado and e.completo = :completo", EvaluacionDocentesMateriaResultados3.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> obtenerResultados3(Integer evaluacion, Integer clave){
        List<EvaluacionDocentesMateriaResultados4> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados4 as e where e.evaluacionDocentesMateriaResultados4PK.evaluacion = :evaluacion and e.evaluacionDocentesMateriaResultados4PK.evaluado = :evaluado and e.completo = :completo", EvaluacionDocentesMateriaResultados4.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> obtenerResultados4(Integer evaluacion, Integer clave){
        List<EvaluacionDocentesMateriaResultados5> lista = em.createQuery("select e from EvaluacionDocentesMateriaResultados5 as e where e.evaluacionDocentesMateriaResultados5PK.evaluacion = :evaluacion and e.evaluacionDocentesMateriaResultados5PK.evaluado = :evaluado and e.completo = :completo", EvaluacionDocentesMateriaResultados5.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .setParameter("completo", Boolean.TRUE)
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<Personal> obtenerPersona(Integer clave){
        Personal p = em.createQuery("select p from Personal as p where p.clave = :clave", Personal.class)
                .setParameter("clave", clave)
                .getResultStream()
                .findFirst()
                .orElse(new Personal());
        if(p.equals(new Personal())) return ResultadoEJB.crearErroneo(1, "Persona no encontrada", Personal.class);
        return ResultadoEJB.crearCorrecto(p, "Persona encontrada");
    }
    
    public ResultadoEJB<AreasUniversidad> obtenerArea(Short area){
        AreasUniversidad au = em.createQuery("select a from AreasUniversidad as a where a.area = :area", AreasUniversidad.class)
                .setParameter("area", area)
                .getResultStream()
                .findFirst()
                .orElse(new AreasUniversidad());
        if(au.equals(new AreasUniversidad())) return ResultadoEJB.crearErroneo(1, "Area no encontrada", AreasUniversidad.class);
        return ResultadoEJB.crearCorrecto(au, "Area encontrada");
    }

    public ResultadoEJB<List<EvaluacionTutoresResultados>> obtenerResultadosTutor1(Integer evaluacion){
        List<EvaluacionTutoresResultados> resultado = em.createQuery("select e from EvaluacionTutoresResultados as e where e.evaluacionTutoresResultadosPK.evaluacion = :evaluacion GROUP BY e.evaluacionTutoresResultadosPK.evaluado", EvaluacionTutoresResultados.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream().collect(Collectors.toList());
        if(resultado.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(resultado, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionTutoresResultados2>> obtenerResultadosTutor2(Integer evaluacion){
        List<EvaluacionTutoresResultados2> resultado = em.createQuery("select e from EvaluacionTutoresResultados2 as e where e.evaluacionTutoresResultados2PK.evaluacion = :evaluacion GROUP BY e.evaluacionTutoresResultados2PK.evaluado", EvaluacionTutoresResultados2.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream().collect(Collectors.toList());
        if(resultado.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(resultado, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionTutoresResultados3>> obtenerResultadosTutor3(Integer evaluacion){
        List<EvaluacionTutoresResultados3> resultado = em.createQuery("select e from EvaluacionTutoresResultados3 as e where e.evaluacionTutoresResultados3PK.evaluacion = :evaluacion GROUP BY e.evaluacionTutoresResultados3PK.evaluado", EvaluacionTutoresResultados3.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream().collect(Collectors.toList());
        if(resultado.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(resultado, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionTutoresResultados>> obtenerResultadosTutor1(Integer evaluacion, Integer clave){
        Comparador<EvaluacionTutoresResultados>  comparador  = new ComparadorEvaluacionTutor();
        List<EvaluacionTutoresResultados> resultado = em.createQuery("select e from EvaluacionTutoresResultados as e where e.evaluacionTutoresResultadosPK.evaluacion = :evaluacion and e.evaluacionTutoresResultadosPK.evaluado = :evaluado", EvaluacionTutoresResultados.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .getResultStream()
                .filter(x -> comparador.isCompleto(x))
                .collect(Collectors.toList());
        if(resultado.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(resultado, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionTutoresResultados2>> obtenerResultadosTutor2(Integer evaluacion, Integer clave){
        Comparador<EvaluacionTutoresResultados2>  comparador  = new ComparadorEvaluacionTutor2();
        List<EvaluacionTutoresResultados2> resultado = em.createQuery("select e from EvaluacionTutoresResultados2 as e where e.evaluacionTutoresResultados2PK.evaluacion = :evaluacion and e.evaluacionTutoresResultados2PK.evaluado = :evaluado and e.completo = :completo", EvaluacionTutoresResultados2.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .setParameter("completo", Boolean.TRUE)
                .getResultStream()
                .filter(x -> comparador.isCompleto(x))
                .collect(Collectors.toList());
        if(resultado.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(resultado, "Lista completa");
    }
    
    public ResultadoEJB<List<EvaluacionTutoresResultados3>> obtenerResultadosTutor3(Integer evaluacion, Integer clave){
        Comparador<EvaluacionTutoresResultados3>  comparador  = new ComparadorEvaluacionTutor3();
        List<EvaluacionTutoresResultados3> resultado = em.createQuery("select e from EvaluacionTutoresResultados3 as e where e.evaluacionTutoresResultados3PK.evaluacion = :evaluacion and e.evaluacionTutoresResultados3PK.evaluado = :evaluado", EvaluacionTutoresResultados3.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("evaluado", clave)
                .getResultStream()
                .filter(x -> comparador.isCompleto(x))
                .collect(Collectors.toList());
        if(resultado.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "La lista esta vacía");
        return ResultadoEJB.crearCorrecto(resultado, "Lista completa");
    }
    
    public ResultadoEJB<List<Personal>> obtenerListaPersonas(){
        List<Personal> listaP = em.createQuery("select p from Personal as p ORDER BY p.clave asc", Personal.class)
                .getResultList();
        if(listaP.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(listaP, "La lista esta completa");
    }
    
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodos(){
        List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares as p where p.periodo > :periodo ORDER BY p.periodo desc", PeriodosEscolares.class)
                .setParameter("periodo", 44)
                .getResultStream().distinct().collect(Collectors.toList());
        if(periodos.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(periodos, "Lista completa");
    }
    
    public ResultadoEJB<PeriodosEscolares> obtenerPeriodo(Integer periodo){
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, periodo);
        return ResultadoEJB.crearCorrecto(pe, "Periodo escolar");
    }
    
    public ResultadoEJB<List<ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360>> obtenerResultadosCedulas(Integer evaluacion){
        List<ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360> lista = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion GROUP BY e.evaluaciones360ResultadosPK.evaluado", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream()
                .map(resultado -> packResultado(resultado))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360> packResultado(Evaluaciones360Resultados evaluacion){
        if(evaluacion.equals(new Evaluaciones360Resultados())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede ser nula", ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360.class);
        Evaluaciones360 ev = em.find(Evaluaciones360.class, evaluacion.getEvaluaciones360ResultadosPK().getEvaluacion());
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, ev.getPeriodo());
        Personal p = em.find(Personal.class, evaluacion.getEvaluaciones360ResultadosPK().getEvaluado());
        AreasUniversidad areaOp = em.find(AreasUniversidad.class, p.getAreaOperativa());
        AreasUniversidad areaSup = em.find(AreasUniversidad.class, p.getAreaSuperior());
        PersonalCategorias catOp = em.find(PersonalCategorias.class, evaluacion.getCategoria().getCategoria());
        List<Evaluaciones360Resultados> resultados = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion and e.evaluaciones360ResultadosPK.evaluado = :evaluado", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", ev.getEvaluacion())
                .setParameter("evaluado", p.getClave())
                .getResultList();
        String evaluadores = resultados.stream().map(e -> e.getEvaluaciones360ResultadosPK().getEvaluador()).collect(Collectors.toList()).toString();
        String evaluadoresNombres = resultados.stream().map(e -> e.getPersonal1().getNombre()).collect(Collectors.toList()).toString();
        Double r1 = resultados.stream().filter(x -> x.getR1() != null).mapToDouble(Evaluaciones360Resultados::getR1).average().orElse(0d);
        Double r2 = resultados.stream().filter(x -> x.getR2() != null).mapToDouble(Evaluaciones360Resultados::getR2).average().orElse(0d);
        Double r3 = resultados.stream().filter(x -> x.getR3() != null).mapToDouble(Evaluaciones360Resultados::getR3).average().orElse(0d);
        Double r4 = resultados.stream().filter(x -> x.getR4() != null).mapToDouble(Evaluaciones360Resultados::getR4).average().orElse(0d);
        Double r5 = resultados.stream().filter(x -> x.getR5() != null).mapToDouble(Evaluaciones360Resultados::getR5).average().orElse(0d);
        Double r6 = resultados.stream().filter(x -> x.getR6() != null).mapToDouble(Evaluaciones360Resultados::getR6).average().orElse(0d);
        Double r7 = resultados.stream().filter(x -> x.getR7() != null).mapToDouble(Evaluaciones360Resultados::getR7).average().orElse(0d);
        Double r8 = resultados.stream().filter(x -> x.getR8() != null).mapToDouble(Evaluaciones360Resultados::getR8).average().orElse(0d);
        Double r9 = resultados.stream().filter(x -> x.getR9() != null).mapToDouble(Evaluaciones360Resultados::getR9).average().orElse(0d);
        Double r10 = resultados.stream().filter(x -> x.getR10() != null).mapToDouble(Evaluaciones360Resultados::getR10).average().orElse(0d);
        Double r11 = resultados.stream().filter(x -> x.getR11() != null).mapToDouble(Evaluaciones360Resultados::getR11).average().orElse(0d);
        Double r12 = resultados.stream().filter(x -> x.getR12() != null).mapToDouble(Evaluaciones360Resultados::getR12).average().orElse(0d);
        Double r13 = resultados.stream().filter(x -> x.getR13() != null).mapToDouble(Evaluaciones360Resultados::getR13).average().orElse(0d);
        Double r14 = resultados.stream().filter(x -> x.getR14() != null).mapToDouble(Evaluaciones360Resultados::getR14).average().orElse(0d);
        Double r15 = resultados.stream().filter(x -> x.getR15() != null).mapToDouble(Evaluaciones360Resultados::getR15).average().orElse(0d);
        Double r16 = resultados.stream().filter(x -> x.getR16() != null).mapToDouble(Evaluaciones360Resultados::getR16).average().orElse(0d);
        Double r17 = resultados.stream().filter(x -> x.getR17() != null).mapToDouble(Evaluaciones360Resultados::getR17).average().orElse(0d);
        Double r18 = resultados.stream().filter(x -> x.getR18() != null).mapToDouble(Evaluaciones360Resultados::getR18).average().orElse(0d);
        Double r19 = resultados.stream().filter(x -> x.getR19() != null).mapToDouble(Evaluaciones360Resultados::getR19).average().orElse(0d);
        Double r20 = resultados.stream().filter(x -> x.getR20() != null).mapToDouble(Evaluaciones360Resultados::getR20).average().orElse(0d);
        Double r21 = resultados.stream().filter(x -> x.getR21() != null).mapToDouble(Evaluaciones360Resultados::getR21).average().orElse(0d);
        Double r22 = resultados.stream().filter(x -> x.getR22() != null).mapToDouble(Evaluaciones360Resultados::getR22).average().orElse(0d);
        Double r23 = resultados.stream().filter(x -> x.getR23() != null).mapToDouble(Evaluaciones360Resultados::getR23).average().orElse(0d);
        Double r24 = resultados.stream().filter(x -> x.getR24() != null).mapToDouble(Evaluaciones360Resultados::getR24).average().orElse(0d);
        Double r25 = resultados.stream().filter(x -> x.getR25() != null).mapToDouble(Evaluaciones360Resultados::getR25).average().orElse(0d);
        Double r26 = resultados.stream().filter(x -> x.getR26() != null).mapToDouble(Evaluaciones360Resultados::getR26).average().orElse(0d);
        Double r27 = resultados.stream().filter(x -> x.getR27() != null).mapToDouble(Evaluaciones360Resultados::getR27).average().orElse(0d);
        Double r28 = resultados.stream().filter(x -> x.getR28() != null).mapToDouble(Evaluaciones360Resultados::getR28).average().orElse(0d);
        Double r29 = resultados.stream().filter(x -> x.getR29() != null).mapToDouble(Evaluaciones360Resultados::getR29).average().orElse(0d);
        Double r30 = resultados.stream().filter(x -> x.getR30() != null).mapToDouble(Evaluaciones360Resultados::getR30).average().orElse(0d);
        Double r31 = resultados.stream().filter(x -> x.getR31() != null).mapToDouble(Evaluaciones360Resultados::getR31).average().orElse(0d);
        String r32 = resultados.stream().filter(x -> x.getR32() != null).map(e -> e.getR32()).collect(Collectors.toList()).toString();
        String r33 = resultados.stream().filter(x -> x.getR33() != null).map(e -> e.getR33()).collect(Collectors.toList()).toString();
        String completo = resultados.stream().map(tmp -> packCompleto(tmp)).collect(Collectors.toList()).toString();
        String inCompleto = resultados.stream().map(tmp -> packIncompleto(tmp)).collect(Collectors.toList()).toString();
        Double promedio = resultados.stream().mapToDouble(Evaluaciones360Resultados::getPromedio).average().orElse(0d);
        return ResultadoEJB.crearCorrecto(new ListaEvaluacion360Promedios.DtoListaResultadosEvaluacion360(ev, pe, quitarCorchetes(evaluadores), quitarCorchetes(evaluadoresNombres), p.getClave(), p
                , areaOp, areaSup, catOp, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, 
                r28, r29, r30, r31, quitarCorchetes(r32), quitarCorchetes(r33), quitarCorchetes(completo), quitarCorchetes(inCompleto), promedio), "Empaquetado completo");
    }
    
    public ResultadoEJB<List<ListaEvaluacion360Promedios.DtoListaReporteEvaluacion360>> obtenerReporteCedulas(Integer evaluacion){
        List<ListaEvaluacion360Promedios.DtoListaReporteEvaluacion360> lista = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion GROUP BY e.evaluaciones360ResultadosPK.evaluador", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream()
                .map(resultado -> packReporte(resultado))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacía");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<ListaEvaluacion360Promedios.DtoListaReporteEvaluacion360> packReporte(Evaluaciones360Resultados evaluacion){
        if(evaluacion.equals(new Evaluaciones360Resultados())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede ser nula", ListaEvaluacion360Promedios.DtoListaReporteEvaluacion360.class);
        Evaluaciones360 ev = em.find(Evaluaciones360.class, evaluacion.getEvaluaciones360().getEvaluacion());
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, ev.getPeriodo());
        Personal p = em.find(Personal.class, evaluacion.getEvaluaciones360ResultadosPK().getEvaluador());
        AreasUniversidad areaOp = em.find(AreasUniversidad.class, p.getAreaOperativa());
        AreasUniversidad areaSup = em.find(AreasUniversidad.class, p.getAreaSuperior());
        PersonalCategorias catOp = em.find(PersonalCategorias.class, evaluacion.getCategoria().getCategoria());
        List<Evaluaciones360Resultados> resultados = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion and e.evaluaciones360ResultadosPK.evaluador = :evaluador", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", ev.getEvaluacion())
                .setParameter("evaluador", p.getClave())
                .getResultList();
        String tipo = resultados.stream().map(e -> e.getTipo()).collect(Collectors.toList()).toString();
        String categoria = resultados.stream().map(e -> e.getPersonal1().getCategoriaOperativa().getCategoria()).collect(Collectors.toList()).toString();
        String categoriaNombre = resultados.stream().map(e -> e.getPersonal1().getCategoriaOperativa().getNombre()).collect(Collectors.toList()).toString();
        List<Boolean> listB = resultados.stream().map(e -> e.getCompleto()).collect(Collectors.toList());
        String completo = "";
        if(listB.contains(Boolean.FALSE)){
            completo = "0";
        }else{
            completo = "1";
        }
        String resultado = resultados.stream().map(e -> packCompleto(e)).collect(Collectors.toList()).toString();
        
        String evaluados = resultados.stream().map(e -> e.getEvaluaciones360ResultadosPK().getEvaluado()).collect(Collectors.toList()).toString();
        String evaluadosNombres = resultados.stream().map(e -> e.getPersonal().getNombre()).collect(Collectors.toList()).toString();
        
        return ResultadoEJB.crearCorrecto(new ListaEvaluacion360Promedios.DtoListaReporteEvaluacion360(pe, ev, p.getClave(), p, areaOp, areaSup, catOp, quitarCorchetes(tipo), quitarCorchetes(categoria),
                quitarCorchetes(categoriaNombre), completo, quitarCorchetes(resultado), quitarCorchetes(evaluados), quitarCorchetes(evaluadosNombres)), "Empaquetado completo");
    }
    
    public ResultadoEJB<List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio>> obtenerReultadosCedulas(Integer evaluacion){
        List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> lista = em.createQuery("select d from DesempenioEvaluacionResultados as d where d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion)
                .getResultStream()
                .map(resultado -> packResultado(resultado))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> packResultado(DesempenioEvaluacionResultados evaluacion){
        if(evaluacion.equals(new DesempenioEvaluacionResultados())) return ResultadoEJB.crearErroneo(1, "La evaluacion no puede ser nula", ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio.class);
        Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
        Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
        
        DesempenioEvaluaciones ev = em.find(DesempenioEvaluaciones.class, evaluacion.getDesempenioEvaluacionResultadosPK().getEvaluacion());
        PeriodosEscolares pe = em.find(PeriodosEscolares.class, ev.getPeriodo());
        Personal evaluador = em.find(Personal.class, evaluacion.getDesempenioEvaluacionResultadosPK().getEvaluador());
        AreasUniversidad evaluadorAreaOp = em.find(AreasUniversidad.class, evaluador.getAreaOperativa());
        AreasUniversidad evaluadorAreaSup = em.find(AreasUniversidad.class, evaluador.getAreaSuperior());
        PersonalCategorias evaluadorCatOp = em.find(PersonalCategorias.class, evaluador.getCategoriaOperativa().getCategoria());
        Personal evaluado = em.find(Personal.class, evaluacion.getDesempenioEvaluacionResultadosPK().getEvaluado());
        AreasUniversidad evaluadoAreaOp = em.find(AreasUniversidad.class, evaluado.getAreaOperativa());
        AreasUniversidad evaluadoAreaSup = em.find(AreasUniversidad.class, evaluado.getAreaSuperior());
        PersonalCategorias evaluadoCatOp = em.find(PersonalCategorias.class, evaluado.getCategoriaOperativa().getCategoria());
        
        DesempenioEvaluacionResultadosPK pk = new DesempenioEvaluacionResultadosPK(ev.getEvaluacion(), evaluador.getClave(), evaluado.getClave());
        DesempenioEvaluacionResultados resultado = em.find(DesempenioEvaluacionResultados.class, pk);
        
        Short r1 = resultado.getR1();
        Short r2 = resultado.getR2();
        Short r3 = resultado.getR3();
        Short r4 = resultado.getR4();
        Short r5 = resultado.getR5();
        Short r6 = resultado.getR6();
        Short r7 = resultado.getR7();
        Short r8 = resultado.getR8();
        Short r9 = resultado.getR9();
        Short r10 = resultado.getR10();
        Short r11 = resultado.getR11();
        Short r12 = resultado.getR12();
        Short r13 = resultado.getR13();
        Short r14 = resultado.getR14();
        Short r15 = resultado.getR15();
        Short r16 = resultado.getR16();
        Short r17 = resultado.getR17();
        Short r18 = resultado.getR18();
        Short r19 = resultado.getR19();
        Short r20 = resultado.getR20();
        String r21 = resultado.getR21();
        String completo = "";
        if(comparador.isCompleto(resultado)){
            completo = "1";
        }
        String inCompleto = "";
        if(!comparador.isCompleto(resultado)){
            inCompleto = "0";
        }
        Double promedio = promediar(resultado);
        return ResultadoEJB.crearCorrecto(new ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio(ev, pe, evaluador, evaluadorAreaOp, evaluadorAreaSup, 
                evaluadorCatOp, evaluado, evaluadoAreaOp, evaluadoAreaSup, evaluadoCatOp, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, 
                r19, r20, r21, completo, inCompleto, promedio), "Empaquetado completo");
    }
    
    public String quitarCorchetes(String valor){
        return valor.replace("[", "").replace("]", "");
    }
    
    public String packCompleto(Evaluaciones360Resultados e){
        if(e.getCompleto() == Boolean.FALSE){
            return "0";
        }else{
            return "1";
        }
    }
    public String packIncompleto(Evaluaciones360Resultados e){
        if(e.getIncompleto() == Boolean.FALSE){
            return "0";
        }else{
            return "1";
        }
    }
    
    public Double promediar(DesempenioEvaluacionResultados t){
        List<Integer> valores = new ArrayList<>();
        Integer r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0, r7 = 0, r8 = 0, r9 = 0, r10 = 0, r11 = 0, r12 = 0, r13 = 0, r14 = 0, r15 = 0, r16 = 0, r17 = 0, r18 = 0, r19 = 0, r20 = 0;
        if(t.getR1()  != null){r1  = t.getR1().intValue();}       if(t.getR2()  != null){r2  = t.getR2().intValue();}
        if(t.getR3()  != null){r3  = t.getR3().intValue();}       if(t.getR4()  != null){r4  = t.getR4().intValue();}
        if(t.getR5()  != null){r5  = t.getR5().intValue();}       if(t.getR6()  != null){r6  = t.getR6().intValue();}
        if(t.getR7()  != null){r7  = t.getR7().intValue();}       if(t.getR8()  != null){r8  = t.getR8().intValue();}
        if(t.getR9()  != null){r9  = t.getR9().intValue();}       if(t.getR10() != null){r10 = t.getR10().intValue();}
        if(t.getR11() != null){r11 = t.getR11().intValue();}      if(t.getR12() != null){r12 = t.getR12().intValue();}
        if(t.getR13() != null){r13 = t.getR13().intValue();}      if(t.getR14() != null){r14 = t.getR14().intValue();}
        if(t.getR15() != null){r15 = t.getR15().intValue();}      if(t.getR16() != null){r16 = t.getR16().intValue();}
        if(t.getR17() != null){r17 = t.getR17().intValue();}      if(t.getR18() != null){r18 = t.getR18().intValue();}
        if(t.getR19() != null){r19 = t.getR19().intValue();}      if(t.getR20() != null){r20 = t.getR20().intValue();}
        
        valores.add(r1);    valores.add(r2);   valores.add(r3);   valores.add(r4);   valores.add(r5);
        valores.add(r6);    valores.add(r7);   valores.add(r8);   valores.add(r9);   valores.add(r10);
        valores.add(r11);   valores.add(r12);  valores.add(r13);  valores.add(r14);  valores.add(r15);
        valores.add(r16);   valores.add(r17);  valores.add(r18);  valores.add(r19);  valores.add(r20);
        Double promedio = valores.stream().mapToInt(Integer::intValue).average().orElse(0d);
        return promedio;
    }
    
    public ResultadoEJB<List<ListaEvaluacion360Promedios>> obtenerResultadosEv360(Evaluaciones360 evaluacion) {
        List<ListaEvaluacion360Promedios> lista = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion GROUP BY e.evaluaciones360ResultadosPK.evaluado", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .getResultStream()
                .map(e -> packEv360(e))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<ListaEvaluacion360Promedios> packEv360(Evaluaciones360Resultados evaluacion) {
        if(evaluacion.equals(new Evaluaciones360Resultados())) return ResultadoEJB.crearErroneo(2, "El resultado no puede ser nulo", ListaEvaluacion360Promedios.class);
        List<Evaluaciones360Resultados> lista = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion and e.evaluaciones360ResultadosPK.evaluado = :evaluado", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluaciones360ResultadosPK().getEvaluacion())
                .setParameter("evaluado", evaluacion.getEvaluaciones360ResultadosPK().getEvaluado())
                .getResultList();
        Personal persona = obtenerPersona(evaluacion.getEvaluaciones360ResultadosPK().getEvaluado()).getValor();
        AreasUniversidad areaOp = obtenerArea(persona.getAreaOperativa()).getValor();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, "La lista esta vacia", ListaEvaluacion360Promedios.class);
        Double suma = lista.stream().mapToDouble(Evaluaciones360Resultados::getPromedio).sum();
        Double promedio = suma / lista.size();
        return ResultadoEJB.crearCorrecto(new ListaEvaluacion360Promedios(persona.getClave(), persona.getNombre(), areaOp.getNombre(),
                persona.getCategoriaOperativa().getNombre(), promedio), "Lista completa");
    }
    
    public ResultadoEJB<List<ListaEvaluacionDesempenioPromedios>> obtenerResultadosEvDesempenio(DesempenioEvaluaciones evaluacion) {
        List<ListaEvaluacionDesempenioPromedios> lista = em.createQuery("select d from DesempenioEvaluacionResultados as d where d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .getResultStream()
                .map(e -> packEvDesempenio(e))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<ListaEvaluacionDesempenioPromedios> packEvDesempenio(DesempenioEvaluacionResultados evaluacion) {
        if(evaluacion.equals(new DesempenioEvaluacionResultados())) return ResultadoEJB.crearErroneo(2, "El resultado no puede ser nulo", ListaEvaluacionDesempenioPromedios.class);
        List<DesempenioEvaluacionResultados> lista = em.createQuery("select d from DesempenioEvaluacionResultados as d where d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion and d.desempenioEvaluacionResultadosPK.evaluado = :evaluado", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion.getDesempenioEvaluacionResultadosPK().getEvaluacion())
                .setParameter("evaluado", evaluacion.getDesempenioEvaluacionResultadosPK().getEvaluado())
                .getResultList();
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, "La lista esta vacia", ListaEvaluacionDesempenioPromedios.class);
        Personal persona = obtenerPersona(evaluacion.getDesempenioEvaluacionResultadosPK().getEvaluado()).getValor();
        AreasUniversidad areaOp = obtenerArea(persona.getAreaOperativa()).getValor();
        
        Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
        Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
        Double promedio = lista.stream()
                .filter(encuesta -> comparador.isCompleto(encuesta))
                .mapToDouble(desempenio -> obtener.promediar(desempenio))
                .sum();
        return ResultadoEJB.crearCorrecto(new ListaEvaluacionDesempenioPromedios(persona.getClave(), persona.getNombre(), areaOp.getNombre(), 
                persona.getCategoriaOperativa().getNombre(), promedio), "Lista completa");
    }
    
    public ResultadoEJB<Boolean> comprobarEvaluacionesPeriodo(Integer periodo){
        Evaluaciones360 ev360 = em.createQuery("select e from Evaluaciones360 as e where e.periodo = :periodo", Evaluaciones360.class)
                .setParameter("periodo", periodo).getResultStream().findFirst().orElse(new Evaluaciones360());
        DesempenioEvaluaciones evDes = em.createQuery("select d from DesempenioEvaluaciones as d where d.periodo = :periodo", DesempenioEvaluaciones.class)
                .setParameter("periodo", periodo).getResultStream().findFirst().orElse(new DesempenioEvaluaciones());
        Evaluaciones evTutor = em.createQuery("select e from Evaluaciones as e where e.periodo = :periodo and e.tipo LIKE CONCAT('%',:tipo,'%')", Evaluaciones.class)
                .setParameter("periodo", periodo).setParameter("tipo", "Tutor").getResultStream().findFirst().orElse(new Evaluaciones());
        Evaluaciones evDoc = em.createQuery("select e from Evaluaciones as e where e.periodo = :periodo and e.tipo LIKE CONCAT('%',:tipo,'%')", Evaluaciones.class)
                .setParameter("periodo", periodo).setParameter("tipo", "Docente").getResultStream().findFirst().orElse(new Evaluaciones());
        
        if(ev360.equals(new Evaluaciones360()) && evDes.equals(new DesempenioEvaluaciones()) && evTutor.equals(new Evaluaciones()) && evDoc.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(1, "No cumple con las cuatro validaciones", Boolean.TYPE);
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El periodo esta habilitado");
    }
    
    
    
    
}
