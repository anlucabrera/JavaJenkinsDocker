/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioAdministracionEncuestas implements EjbAdministracionEncuestas {

    @EJB
    Facade f;
    @EJB
    Facade2 f2;

    @Override

    public PeriodosEscolares getPeriodoActual() {
//        TypedQuery<PeriodosEscolares> q = f.getEntityManager().createQuery("SELECT MAX (p.periodo) FROM PeriodosEscolares AS p ", PeriodosEscolares.class);
//        return (PeriodosEscolares)q.getSingleResult();

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("prontuario.periodoActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getPeriodoActual()" + l.get(0));
            return l.get(0);
        }
    }

    @Override
    public List<Grupos> esTutor(Integer maestro, Integer periodo) {
//       //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.esTutor() esta es la clave del maestro obtenida : --- > " + maestro);
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

    @Override
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

    @Override
    public List<VistaAlumnosPye> findAllByMatricula(String matricula) {
//        List<VistaAlumnosPye> salida;
        Query q = f2.getEntityManager().createNativeQuery("SELECT * from [saiiut].[vista_alumnos_pye] as v  where v.matricula = ?1 and v.cve_periodo is not null", VistaAlumnosPye.class);
        //TypedQuery<VistaAlumnosPye> q =  //em.createNamedQuery("VistaAlumnosPye.findByMatricula", VistaAlumnosPye.class);
        q.setParameter(1, matricula);
//        salida = q.getResultList();
        return q.getResultList();
    }

    @Override
    public List<Personal> esDirectorDeCarrera(Short areaSup, Integer actividad, Integer catOp, Integer clave) {
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and  p.categoriaOperativa.categoria= :categoria AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("clave", clave);
//       //System.out.println("el directivo es : " + q.getResultList());
        return q.getResultList();
    }

    @Override
    public List<AreasUniversidad> obtenerAreasDirector(Short identificador, String estatus) {
       //System.out.println("parametro identificador : " + identificador + " parametro estatus : " + estatus);
        AreasUniversidad a = f.getEntityManager().find(AreasUniversidad.class, identificador);
       //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.obtenerAreasDirector() el area es : " + a);
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<Evaluaciones360Resultados> getEvaluaciones360ResultadosSubordinados(Evaluaciones360 evaluacion, Integer evaluado) {
        TypedQuery<Evaluaciones360Resultados> q = f.getEntityManager().createQuery("SELECT e from Evaluaciones360Resultados as e WHERE e.evaluaciones360.evaluacion = :evaluacion AND e.personal.clave = :evaluado", Evaluaciones360Resultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluado", evaluado);
        List<Evaluaciones360Resultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
           //System.out.println("no hay evaluacion en este periodo ");
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<DesempenioEvaluacionResultados> getEvaluacionesDesempenioSubordinados(Integer evaluacion, Integer evaluado) {
        TypedQuery<DesempenioEvaluacionResultados> q = f.getEntityManager().createQuery("SELECT d from DesempenioEvaluacionResultados as d WHERE d.desempenioEvaluaciones.evaluacion = :evaluacion AND d.personal.clave = :evaluado", DesempenioEvaluacionResultados.class);
        q.setParameter("evaluacion", evaluacion);
        q.setParameter("evaluado", evaluado);
        List<DesempenioEvaluacionResultados> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public Evaluaciones getEvaluaciones(Integer periodo, String tipo) {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.periodo = :periodo AND e.tipo = :tipo", Evaluaciones.class);
        q.setParameter("periodo", periodo);
        q.setParameter("tipo", tipo);
        List<Evaluaciones> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<ListaPersonal> getListadoDocentesPorArea(Short area) {
       //System.out.println("area seleccionada : " + area);
        if (area == 47) {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getListadoDocentesPorArea() es el area 47");
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
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getListadoDocentesPorArea() es el area 999");
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
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getListadoDocentesPorArea() es el area : " + area);
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

    @Override
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

    @Override
    public List<ListaPersonal> personalGeneralCompleta() {
        TypedQuery<ListaPersonal> q = f.getEntityManager().createQuery("SELECT l from ListaPersonal l ", ListaPersonal.class);
        List<ListaPersonal> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l;
        }
    }

    @Override
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

    @Override
    public DesempenioEvaluaciones nuevaEvaluacionDesempenio(DesempenioEvaluaciones evaluacion) {
        if (evaluacion != null) {
            f.create(evaluacion);
            evaluacion.setDesempenioEvaluacionResultadosList(null);
        }
        return evaluacion;
    }

    @Override
    public List<Evaluaciones360> getListaEvaluaciones360() {
        TypedQuery<Evaluaciones360> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones360 e", Evaluaciones360.class);
        List<Evaluaciones360> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
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

    @Override
    public List<DesempenioEvaluaciones> getListaEvaluacionesDesempenio() {
        TypedQuery<DesempenioEvaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM DesempenioEvaluaciones e", DesempenioEvaluaciones.class);
        List<DesempenioEvaluaciones> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
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

    @Override
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

    @Override
    public List<Alumnos> getAlumnosPeriodoActual() {
        TypedQuery<Alumnos> q = f2.getEntityManager().createQuery("SELECT e from Alumnos e WHERE e.grupos.gruposPK.cvePeriodo = 47", Alumnos.class);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Alumno> getEstudiantesSEScolaes() {
        TypedQuery<Alumno> q = f.getEntityManager().createQuery("SELECT a from Alumno a WHERE a.alumnoPK.periodo = :periodo", Alumno.class);
        q.setParameter("periodo", getPeriodoActual().getPeriodo());
       //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getEstudiantesSEScolaes() tamaño de lista de alumnos " + q.getResultList().size());
        List<Alumno> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
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

    @Override
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

}
