/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.dto.Dto;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.*;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbAdministracionEncuestaServicios {


    @EJB private Facade f;
    @EJB private Facade2 f2;
    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager(); em2 = f2.getEntityManager();
    }

    public Evaluaciones evaluacionPeriodoAnteiror(){
        String periodoAnteiror = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
        .setParameter("nombre", "periodoEncuestaServicios").getResultStream().findFirst().orElse(null)).getValor();
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", Integer.parseInt(periodoAnteiror))
                .setParameter("tipo", "Servicios")
                .getResultStream().collect(Collectors.toList());
        if (e.isEmpty()) {
            return new Evaluaciones();
        } else {
            return e.get(0);
        }

    }

    public Evaluaciones evaluacionEstadiaPeridoActual() {
        String periodoEncuesta = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaServiciosActual")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", Integer.parseInt(periodoEncuesta))
                .setParameter("tipo", "Servicios")
                .getResultStream().collect(Collectors.toList());
        if (e.isEmpty()) {
            return new Evaluaciones();
        } else {
            return e.get(0);
        }
    }

    /**
     * Metodo que ayuda a la busqueda de todos los estudiantes activos en el periodo actual
     * @return Devuelve la lista completa de los estudiantes activos
     */
    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerAlumnosNoAccedieron() {
        String periodoEscolar = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaServiciosActual")
                .getResultStream().findFirst().orElse(null)).getValor();
        String periodoEscolarAnterior = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaServicios")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado1 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado1")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado2 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado2")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado3 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado3")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado4 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado4")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuesta = new ArrayList<>();
        dtoAlumnosEncuesta = em2.createQuery("select a from Alumnos as a " +
                "where (a.cveStatus = :estatus or a.cveStatus = :estatus2) " +
                "and (a.grupos.gruposPK.cvePeriodo = :periodo or a.grupos.gruposPK.cvePeriodo = :periodoActual) " +
                "and (a.gradoActual = :grado1 or a.gradoActual = :grado2 or a.gradoActual = :grado3 or a.gradoActual = :grado4)", Alumnos.class)
                .setParameter("estatus", 1)
                .setParameter("estatus2", 6)
                .setParameter("periodo", Integer.parseInt(periodoEscolarAnterior))
                .setParameter("periodoActual", Integer.parseInt(periodoEscolar))
                .setParameter("grado1", Short.parseShort(grado1))
                .setParameter("grado2", Short.parseShort(grado2))
                .setParameter("grado3", Short.parseShort(grado3))
                .setParameter("grado4", Short.parseShort(grado4))
                .getResultStream().map(alumnos -> packEstudiantesEncuesta(alumnos)).filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoAlumnosEncuesta, "Se empaqueto con éxito.");
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerAlumnosNoAccedieronCE() {
        String periodoEscolar = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaServiciosActual")
                .getResultStream().findFirst().orElse(null)).getValor();
        String periodoEscolarAnterior = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaServicios")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado1 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado1")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado2 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado2")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado3 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado3")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado4 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado4")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAlumnosEncuesta = new ArrayList<>();
        dtoAlumnosEncuesta = em.createQuery("select e from Estudiante as e " +
                "where e.tipoEstudiante.idTipoEstudiante = :tipoEstudiante " +
                "and (e.periodo = :periodo or e.periodo = :periodoActual) " +
                "and (e.grupo.grado = :grado1 or e.grupo.grado = :grado2 or e.grupo.grado = :grado3 or e.grupo.grado = :grado4)", Estudiante.class)
                .setParameter("tipoEstudiante", Short.parseShort("1"))
                .setParameter("periodo", Integer.parseInt(periodoEscolarAnterior))
                .setParameter("periodoActual", Integer.parseInt(periodoEscolar))
                .setParameter("grado1", Integer.parseInt(grado1))
                .setParameter("grado2", Integer.parseInt(grado2))
                .setParameter("grado3", Integer.parseInt(grado3))
                .setParameter("grado4", Integer.parseInt(grado4))
                .getResultStream().map(estudiante -> packEstudiantesEncuesta(estudiante)).filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoAlumnosEncuesta, "Se empaqueto con éxito.");
    }

    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> packEstudiantesEncuesta(Estudiante estudiante){
        try{
            Estudiante estudianteBD = em.find(Estudiante.class, estudiante.getIdEstudiante());
            Grupo grupo = em.find(Grupo.class, estudianteBD.getGrupo().getIdGrupo());
            AreasUniversidad areasUniversidad = em.find(AreasUniversidad.class, estudianteBD.getCarrera());
            Personal tutor = em.find(Personal.class, grupo.getTutor());
            DtoAlumnosEncuesta.DtoDirectores dtoDirector = listaDirectores().getValor().stream()
                    .filter(siglas -> siglas.getSiglas().equals(areasUniversidad.getSiglas())).findFirst().orElse(null);
            assert dtoDirector != null;
            ListaUsuarioClaveNomina listaUsuarioClaveNomina = em2.createQuery("select l from ListaUsuarioClaveNomina as l where l.numeroNomina = :noNomima", ListaUsuarioClaveNomina.class)
                    .setParameter("noNomima", dtoDirector.getClaveDirector().toString()).getResultStream().findFirst().orElse(null);
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar(
                    estudianteBD, estudianteBD.getAspirante().getIdPersona(), grupo, areasUniversidad, tutor, listaUsuarioClaveNomina,
                    areasUniversidad.getSiglas(), grupo.getLiteral().toString(), (short) estudiante.getGrupo().getGrado());
            //System.out.println(dto);
            return ResultadoEJB.crearCorrecto( dto,"");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1,"No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar.class);
        }
    }

    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> packEstudiantesEncuesta(Alumnos alumnos){
        try{
            Personas alumno = em2.createQuery("select p from Personas as p where p.personasPK.cvePersona = :cveAlumno", Personas.class)
                    .setParameter("cveAlumno", alumnos.getAlumnosPK().getCveAlumno()).getResultStream().findFirst().orElse(null);
            assert alumno != null;
            //System.out.println("Datos personales:"+alumno.getNombre()+" "+alumno.getApellidoPat()+" "+alumno.getApellidoMat());
            Grupos grupos = alumnos.getGrupos();
            //System.out.println("Grupo:"+ grupos.getGrado()+" "+grupos.getIdGrupo());
            CarrerasCgut carrerasCgut = em2.createQuery("select c from CarrerasCgut as c where c.cveCarrera = :cveCarrera", CarrerasCgut.class)
                    .setParameter("cveCarrera", grupos.getGruposPK().getCveCarrera()).getResultStream().findFirst().orElse(null);
            DtoAlumnosEncuesta.DtoCarrera dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera();
            assert carrerasCgut != null;
            if(carrerasCgut.getAbreviatura().equals("AARH")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "AARH");}
            if(carrerasCgut.getAbreviatura().equals("ADMON")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "AD");}
            if(carrerasCgut.getAbreviatura().equals("ASP")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "ASP");}
            if(carrerasCgut.getAbreviatura().equals("BIO")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "BIO");}
            if(carrerasCgut.getAbreviatura().equals("EA")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "EA");}
            if(carrerasCgut.getAbreviatura().equals("FAT")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "FOT");}
            if(carrerasCgut.getAbreviatura().equals("GASTRO")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "GAS");}
            if(carrerasCgut.getAbreviatura().equals("IBIO")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IBIO");}
            if(carrerasCgut.getAbreviatura().equals("IDIE")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IDIE");}
            if(carrerasCgut.getAbreviatura().equals("IMECA")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IMECA");}
            if(carrerasCgut.getAbreviatura().equals("IMI")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IMI");}
            if(carrerasCgut.getAbreviatura().equals("INF")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IN");}
            if(carrerasCgut.getAbreviatura().equals("IPA")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IPA");}
            if(carrerasCgut.getAbreviatura().equals("IPRI")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "IPRI");}
            if(carrerasCgut.getAbreviatura().equals("ITIC")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "ITIC");}
            if(carrerasCgut.getAbreviatura().equals("LTF")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "LTEFI");}
            if(carrerasCgut.getAbreviatura().equals("MAI")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "MAI");}
            if(carrerasCgut.getAbreviatura().equals("MAA")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "MECAA");}
            if(carrerasCgut.getAbreviatura().equals("MAP")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "MIAP");}
            if(carrerasCgut.getAbreviatura().equals("PAL")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "PA");}
            if(carrerasCgut.getAbreviatura().equals("PAI")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "PAI");}
            if(carrerasCgut.getAbreviatura().equals("QAB")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "QAB");}
            if(carrerasCgut.getAbreviatura().equals("TFAR")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "TFAR");}
            if(carrerasCgut.getAbreviatura().equals("TICAMC")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "TICAMC");}
            if(carrerasCgut.getAbreviatura().equals("TICASI")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "TICASI");}
            if(carrerasCgut.getAbreviatura().equals("LGASTRO")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "LGASTRO");}
            if(carrerasCgut.getAbreviatura().equals("AACH")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "AACH");}
            if(carrerasCgut.getAbreviatura().equals("TIEVND")){dtoCarrera = new DtoAlumnosEncuesta.DtoCarrera(carrerasCgut.getCveCarrera(), carrerasCgut.getNombre(), "TIEVND");}
            //System.out.println("Transformación de la carrera:"+ dtoCarrera.getNombre()+"-"+dtoCarrera.getAbreviatura());
            Personas docente = em2.createQuery("select p from Personas as p where p.personasPK.cvePersona = :cvePersona", Personas.class)
                    .setParameter("cvePersona", grupos.getCveMaestro()).getResultStream().findFirst().orElse(null);
            assert docente != null;
            //System.out.println("Información del tutor:"+docente.getNombre()+" "+docente.getApellidoPat()+" "+docente.getApellidoMat());
            DtoAlumnosEncuesta.DtoCarrera finalDtoCarrera = dtoCarrera;
            DtoAlumnosEncuesta.DtoDirectores dtoDirector = listaDirectores().getValor().stream()
                    .filter(siglas -> siglas.getSiglas().equals(finalDtoCarrera.getAbreviatura())).findFirst().orElse(null);
            assert dtoDirector != null;
            ListaUsuarioClaveNomina listaUsuarioClaveNomina = em2.createQuery("select l from ListaUsuarioClaveNomina as l where l.numeroNomina = :noNomima", ListaUsuarioClaveNomina.class)
                    .setParameter("noNomima", dtoDirector.getClaveDirector().toString()).getResultStream().findFirst().orElse(null);
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral(
                    alumnos, alumno, grupos, dtoCarrera, docente, listaUsuarioClaveNomina, dtoCarrera.getAbreviatura(), grupos.getIdGrupo(), alumnos.getGradoActual()
            );
            //System.out.println("DTO Alumno:"+dto);
            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1,"No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral.class);
        }
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoDirectores>> listaDirectores(){
        List<DtoAlumnosEncuesta.DtoDirectores> dtoDirectores = new ArrayList<>();
        List<AreasUniversidad> au = em.createQuery("select a from AreasUniversidad  as a where a.categoria.categoria = :categoria", AreasUniversidad.class)
                .setParameter("categoria", Short.parseShort("8")).getResultStream().collect(Collectors.toList());
        au.forEach(area -> {
            em.createQuery("select a from AreasUniversidad as a where a.areaSuperior = :area and a.vigente = :vigente", AreasUniversidad.class)
                    .setParameter("vigente", "1")
                    .setParameter("area", area.getArea()).getResultStream().forEach(programa -> {
                        Personal personal = em.find(Personal.class, area.getResponsable());
                        dtoDirectores.add(new DtoAlumnosEncuesta.DtoDirectores(
                                programa.getNombre(), programa.getSiglas(), personal.getClave(), personal.getNombre()
                        ));
                    });
        });
        return ResultadoEJB.crearCorrecto(dtoDirectores, "Cargas académicas por docente y periodo");
    }

    /**
     * Metodo que ayuda con la coleccion de la informacion de la encuesta realizada por un estudiante
     * @param matricula parametro que se envia para realizar la busqueda
     * @return devuelve los datos del estudiante
     */
    public EncuestaServiciosResultados obtenerResultadosEncServXMatricula(Integer matricula) {
        Integer evaluacionActiva = evaluacionPeriodoAnteiror().getEvaluacion();
        Integer evaluacionActivaActual = evaluacionEstadiaPeridoActual().getEvaluacion();
        List<EncuestaServiciosResultados> esr = f.getEntityManager()
                .createQuery("SELECT e FROM EncuestaServiciosResultados as e where e.encuestaServiciosResultadosPK.evaluador = :matricula AND " +
                        "(e.encuestaServiciosResultadosPK.evaluacion =:evaluacion or e.encuestaServiciosResultadosPK.evaluacion = :evaluacion2)", EncuestaServiciosResultados.class)
                .setParameter("evaluacion", evaluacionActiva)
                .setParameter("evaluacion2", evaluacionActivaActual)
                .setParameter("matricula", matricula).getResultStream().collect(Collectors.toList());
        if(esr.isEmpty()){
            return new EncuestaServiciosResultados();
        }else{
            return esr.get(0);
        }
    }

    /**
     * Metodo de busqueda de todos los estudiantes de acuerdo al director
     * @param cveDirector cadena que se envia para realizar la busqueda
     * @return devuelve la informacion de todos los estudiantes que se encuentran a su cargo
     */
    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> obtenerResultadosXDirector(String cveDirector) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> dtoAlumnosEncuestaDirector = obtenerEstudiantesSaiiutyCE().getValor()
                .stream()
                .filter(alumno -> alumno.getCveDirector() == Integer.parseInt(cveDirector)).collect(Collectors.toList());
        if(dtoAlumnosEncuestaDirector.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnosEncuestaDirector;
        }
    }

    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> obtenerResultadosXTutor(Integer cveMaestro, Integer cveNomina) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> dtoAlumnosEncuestaTutor = obtenerEstudiantesSaiiutyCE().getValor()
                .stream()
                .filter(alumno -> alumno.getCveMaestro().equals(cveMaestro) || alumno.getCveMaestro().equals(cveNomina)).collect(Collectors.toList());
        if(dtoAlumnosEncuestaTutor.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnosEncuestaTutor;
        }
    }


    public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> avanceEncuestaServiciosPorGrupo(){
        List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> aespg = new ArrayList<>();
        List<DtoAlumnosEncuesta> dtoAE = new ArrayList<>();
        Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> ae1 = obtenerAlumnosNoAccedieron().getValor();
        ae1.forEach(x -> {
            EncuestaServiciosResultados esr = obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
            if(esr != null){
                if(comparador.isCompleto(esr)){
                    dtoAE.add(new DtoAlumnosEncuesta(
                            x.getAlumnos().getMatricula(), x.getGrupos().getGruposPK().getCvePeriodo(), x.getPersonas().getNombre(), x.getPersonas().getApellidoPat(), x.getPersonas().getApellidoMat(),
                            x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), x.getAlumnos().getCveStatus(), x.getTutor().getPersonasPK().getCvePersona(),
                            x.getTutor().getNombre(), x.getTutor().getApellidoPat(), x.getTutor().getApellidoMat(), String.valueOf(x.getDtoDirector().getCvePersona())
                    ));
                }
            }
        });
        Map<String, Map<String, Map<Short, Long>>> groupingByCareerByGroupAndQuarter = dtoAE
                .stream().collect(Collectors.groupingBy(DtoAlumnosEncuesta::getSiglas,
                        Collectors.groupingBy(DtoAlumnosEncuesta::getGrupo,
                                Collectors.groupingBy(DtoAlumnosEncuesta::getGrado, Collectors.counting()))));
        groupingByCareerByGroupAndQuarter.forEach((k,v) -> {
            v.forEach((k1, v1) -> {
                v1.forEach((k2, v2) -> {
                    Map<String, Map<String, Map<Short, Long>>> groupingByCareerByGroupAndQuarterStudents = ae1
                            .stream().collect(Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getSiglas,
                                    Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getGrupo,
                                            Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getGrado, Collectors.counting()))));
                    groupingByCareerByGroupAndQuarterStudents.forEach((k3, v3) -> {
                        v3.forEach((k4, v4) -> {
                            v4.forEach((k5, v5) -> {
                                if(k.equals(k3) && k1.equals(k4) && k2.equals(k5)){
                                    Integer totalPorCarrera = Math.toIntExact(v5);
                                    Integer totalEST = Math.toIntExact(v2);
                                    Integer faltantesPorContestar = totalPorCarrera - totalEST;
                                    Double percentage = (totalEST.doubleValue() * 100) / totalPorCarrera.doubleValue();
                                    aespg.add(new ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo(
                                            k3, k4, k5, totalPorCarrera, totalEST, faltantesPorContestar, percentage
                                    ));
                                }
                            });
                        });
                    });
                });
            });
        });
        return aespg;
    }

    public List<ListaDatosAvanceEncuestaServicio> obtenerListaDatosAvanceEncuestaServicio(){
        List<ListaDatosAvanceEncuestaServicio> ldaes = new ArrayList<>();
        List<ListadoGraficaEncuestaServicios> lges = new ArrayList<>();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> ae = obtenerAlumnosNoAccedieron().getValor();
        ae.forEach(x -> {
            try {
                EncuestaServiciosResultados listaCompleta = obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
                Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
                if (listaCompleta != null) {
                    if(comparador.isCompleto(listaCompleta)){
                        lges.add(new ListadoGraficaEncuestaServicios(x.getSiglas(), Long.parseLong(x.getAlumnos().getMatricula())));
                    }
                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbAdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Map<String, Long> gropingByCareer = lges.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
        gropingByCareer.forEach((k, v) -> {
            Map<String, Long> groupingByCareer = ae.stream().collect(Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getSiglas, Collectors.counting()));
            groupingByCareer.forEach((k1, v1) -> {
                if(k.equals(k1)){
                    ldaes.add(new ListaDatosAvanceEncuestaServicio(k, Math.toIntExact(v1), Math.toIntExact(v), Math.toIntExact(v1 - v), (v.doubleValue() * 100) / v1.doubleValue()));
                }
            });
        });
        return ldaes;
    }

    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> packEstudianteSaiiut(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dtoAlumnosEncuestaGeneral){
        try {
            String tutor = "No hay tutor asignado";
            if(dtoAlumnosEncuestaGeneral.getGrupos().getCveMaestro() != null){
                tutor = dtoAlumnosEncuestaGeneral.getTutor().getNombre()+" "+dtoAlumnosEncuestaGeneral.getTutor().getApellidoPat()+" "+dtoAlumnosEncuestaGeneral.getTutor().getApellidoMat();
            }
            DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE(
                    Integer.parseInt(dtoAlumnosEncuestaGeneral.getAlumnos().getMatricula()),
                    dtoAlumnosEncuestaGeneral.getPersonas().getNombre()+" "+dtoAlumnosEncuestaGeneral.getPersonas().getApellidoPat()+" "+dtoAlumnosEncuestaGeneral.getPersonas().getApellidoMat(),
                    dtoAlumnosEncuestaGeneral.getGrado(), dtoAlumnosEncuestaGeneral.getSiglas(), dtoAlumnosEncuestaGeneral.getGrupo(), tutor, dtoAlumnosEncuestaGeneral.getGrupos().getCveMaestro(),
                    dtoAlumnosEncuestaGeneral.getDtoDirector().getCvePersona()
            );
            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1,"No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE.class);
        }
    }

    public ResultadoEJB<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> packEstudiantControlEscolar(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoAlumnosEncuestaGeneral){
        try {
            String tutor = "No hay tutor asignado";
            if(dtoAlumnosEncuestaGeneral.getTutor().getClave() != null){
                tutor = dtoAlumnosEncuestaGeneral.getTutor().getNombre();
            }
            DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE dto = new DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE(
                    dtoAlumnosEncuestaGeneral.getAlumnos().getMatricula(),
                    dtoAlumnosEncuestaGeneral.getPersonas().getNombre()+" "+dtoAlumnosEncuestaGeneral.getPersonas().getApellidoPaterno()+" "+dtoAlumnosEncuestaGeneral.getPersonas().getApellidoMaterno(),
                    dtoAlumnosEncuestaGeneral.getGrado(), dtoAlumnosEncuestaGeneral.getSiglas(), dtoAlumnosEncuestaGeneral.getGrupo(), tutor, dtoAlumnosEncuestaGeneral.getPersonas().getIdpersona(),
                    dtoAlumnosEncuestaGeneral.getDtoDirector().getCvePersona()
            );
            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1,"No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE.class);
        }
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE>> obtenerListaCompletaSaiiut(){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> lista =
        obtenerAlumnosNoAccedieron().getValor()
                .stream()
                .map(dtoAlumnosEncuestaGeneral -> packEstudianteSaiiut(dtoAlumnosEncuestaGeneral))
                .filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(lista, "Lista completada con exito");
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE>> obtenerListaCompletaControlEscolar(){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> lista =
                obtenerAlumnosNoAccedieronCE().getValor()
                        .stream()
                        .map(dtoAlumnosEncuestaGeneralControlEscolar -> packEstudiantControlEscolar(dtoAlumnosEncuestaGeneralControlEscolar))
                        .filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor)
                        .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(lista, "Lista completada con exito");
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE>> obtenerEstudiantesSaiiutyCE(){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaSaiiutyCE> estudiantes = Stream.concat(obtenerListaCompletaSaiiut().getValor().stream(), obtenerListaCompletaControlEscolar().getValor().stream()).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(estudiantes, "Lista completa");
    }
}
