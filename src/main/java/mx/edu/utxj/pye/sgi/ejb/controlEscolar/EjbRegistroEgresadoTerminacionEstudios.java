/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.RegistroEgresadosTerminacionEstudios;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PlanCompetencias;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbRegistroEgresadoTerminacionEstudios {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB Facade f;
    @EJB EjbPropiedades ep;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPacker ejbPacker;
    
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReincorporacion.validarServiciosEscolares)", e, null);
        }
    }
    
    public ResultadoEJB<DtoEstudiante> validarEstudiante(Integer matricula){
        try{
            DtoEstudiante e = packEstudiante(matricula).getValor();
            if(e.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) || e.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("4"))){
                return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante.");
            }else {
                return ResultadoEJB.crearErroneo(2, "El estudiante encontrado no tiene una inscripcion activa.", DtoEstudiante.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbConsultaCalificacion.validadEstudiante)", e, null);
        }
    }
    
    public ResultadoEJB<DtoEstudiante> packEstudiante(Integer matricula){
        try{
            //empaquetar estudiante
            List<DtoInscripcion> dtoInscripciones = em.createQuery("select e from Estudiante e where e.matricula=:matricula and (e.grupo.grado = :grado1 or e.grupo.grado = :grado2) order by e.periodo desc", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("grado1", 6)
                    .setParameter("grado2", 11)
                    .getResultStream()
                    .map(estudiante -> packInscripcion(estudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(dtoInscripciones.isEmpty()) return ResultadoEJB.crearErroneo(2, "No se pudo identificar ninguna inscripción de estudiante con la matrícula proporcionada.", DtoEstudiante.class);
            Aspirante aspirante = dtoInscripciones.get(0).getInscripcion().getAspirante();
            Persona persona = aspirante.getIdPersona();
            DtoInscripcion dtoInscripcionActiva = dtoInscripciones.stream()
                    .max(Comparator.comparingInt(value -> value.getPeriodo().getPeriodo()))
                    .orElse(null);
            if(dtoInscripcionActiva == null) return ResultadoEJB.crearErroneo(2, "El estudiante no tiene inscripción activa.", DtoEstudiante.class);
            DtoEstudiante dtoEstudiante = new DtoEstudiante(persona, aspirante, dtoInscripciones, dtoInscripcionActiva);
//            System.out.println("dtoEstudiante = " + dtoEstudiante);
            return ResultadoEJB.crearCorrecto(dtoEstudiante, "Estudiante empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el estudiante a partir de su mattricula (EjbPacker.packEstudiante).", e, DtoEstudiante.class);
        }
    }
    
    public ResultadoEJB<DtoInscripcion> packInscripcion(Estudiante estudiante){
        try{
            //empaquetar inscripción del estudiante
            if(estudiante == null) return  ResultadoEJB.crearErroneo(2, "No se pudo identificar al estudiante con la matricula y grupo proporcionados.", DtoInscripcion.class);

            Grupo grupo = estudiante.getGrupo();
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            Generaciones generacion = em.find(Generaciones.class, grupo.getGeneracion());
            DtoInscripcion dto = new DtoInscripcion(estudiante, grupo, periodo, generacion, true);
            return ResultadoEJB.crearCorrecto(dto, "Estudiante empaquetado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la inscripción del estudiante a partir de su mattricula (EjbPacker.packInscripcion).", e, DtoInscripcion.class);
        }
    }
    
    public ResultadoEJB<List<DtoEstudiante>> obtenerEstudiantes(){
        try{
            List<DtoEstudiante> estudiantes = em
                    .createQuery("SELECT e FROM Estudiante e where e.grupo.grado = :grado and ( e.tipoEstudiante.idTipoEstudiante = :tipo or e.tipoEstudiante.idTipoEstudiante = :tipo2)", Estudiante.class)
                    .setParameter("grado", Integer.parseInt(obtenerCuatrimestreEstudiantes().getValor().getValor()))
                    .setParameter("tipo", Short.parseShort("1"))
                    .setParameter("tipo2", Short.parseShort("4"))
                    .getResultStream()
                    .map(estudiante -> packEstudianteLista(estudiante.getMatricula()))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            //System.out.println("Tamaño:"+estudiantes.size());
            return ResultadoEJB.crearCorrecto(estudiantes, "");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo. (EjbConsultaCalificacion.packUnidadesmateria)", e, null);
        }
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerCuatrimestreEstudiantes(){
        VariablesProntuario vp = em.createQuery("SELECT v FROM VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado")
                .getResultStream()
                .findFirst()
                .orElse(new VariablesProntuario());
        //System.out.println(r);
        if(vp.getVariable()== null) return ResultadoEJB.crearErroneo(1, "El estudiante no cuenta con registro", VariablesProntuario.class);
        return ResultadoEJB.crearCorrecto(vp, "El estudiante ya cuenta con un registro");
    }
    
    public ResultadoEJB<DtoEstudiante> packEstudianteLista(Integer matricula){
        try{
            //empaquetar estudiante
            List<DtoInscripcion> dtoInscripciones = em.createQuery("select e from Estudiante e where e.matricula=:matricula order by e.periodo desc", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .map(estudiante -> packInscripcion(estudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            //System.out.println("Inscripcion" + dtoInscripciones.get(0).getGrupo().getGrado()+"-"+ dtoInscripciones.get(0).getInscripcion().getMatricula());
            if(dtoInscripciones.isEmpty()) return ResultadoEJB.crearErroneo(2, "No se pudo identificar ninguna inscripción de estudiante con la matrícula proporcionada.", DtoEstudiante.class);
            Aspirante aspirante = dtoInscripciones.get(0).getInscripcion().getAspirante();
            Persona persona = aspirante.getIdPersona();
            DtoInscripcion dtoInscripcionActiva = dtoInscripciones.stream()
                    .max(Comparator.comparingInt(value -> value.getPeriodo().getPeriodo()))
                    .orElse(null);
            if(dtoInscripcionActiva == null) return ResultadoEJB.crearErroneo(2, "El estudiante no tiene inscripción activa.", DtoEstudiante.class);
            DtoEstudiante dtoEstudiante = new DtoEstudiante(persona, aspirante, dtoInscripciones, dtoInscripcionActiva);
//            System.out.println("dtoEstudiante = " + dtoEstudiante);
            return ResultadoEJB.crearCorrecto(dtoEstudiante, "Estudiante empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el estudiante a partir de su mattricula (EjbPacker.packEstudiante).", e, DtoEstudiante.class);
        }
    }
    
    public ResultadoEJB<Boolean> verificarRegistro(Integer matricula, String generacion){
        RegistroEgresadosTerminacionEstudios r = em.createQuery("select r from RegistroEgresadosTerminacionEstudios as r where r.idEstudiante.matricula = :matricula and r.generacion = :generacion", RegistroEgresadosTerminacionEstudios.class)
                .setParameter("matricula", matricula)
                .setParameter("generacion", generacion)
                .getResultStream()
                .findFirst()
                .orElse(new RegistroEgresadosTerminacionEstudios());
        //System.out.println(r);
        if(r.getIdRegistro() == null) return ResultadoEJB.crearCorrecto(Boolean.FALSE, "El estudiante no cuenta con registro");
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El estudiante ya cuenta con un registro");
    }
    
    public ResultadoEJB<List<RegistroEgresadosTerminacionEstudios>> obtenerRegistros(Integer matricula){
        List<RegistroEgresadosTerminacionEstudios> listaR = em.createQuery("select r from RegistroEgresadosTerminacionEstudios as r where r.idEstudiante.matricula = :matricula", RegistroEgresadosTerminacionEstudios.class)
                .setParameter("matricula", matricula)
                .getResultStream().collect(Collectors.toList());
        if(listaR.isEmpty()) return ResultadoEJB.crearCorrecto(new ArrayList<>(), "La lista esta vacia");
        return ResultadoEJB.crearCorrecto(listaR, "Lista completa");
    }
    
    public ResultadoEJB<List<RegistroEgresadosTerminacionEstudios>> obtenerRegistros(){
        List<RegistroEgresadosTerminacionEstudios> listaR = em.createQuery("select r from RegistroEgresadosTerminacionEstudios as r ", RegistroEgresadosTerminacionEstudios.class)
                .getResultStream().collect(Collectors.toList());
        if(listaR.isEmpty()) return ResultadoEJB.crearCorrecto(new ArrayList<>(), "La lista esta vacia");
        return ResultadoEJB.crearCorrecto(listaR, "Lista completa");
    }
    
    public ResultadoEJB<RegistroEgresadosTerminacionEstudios> guardarRegistro(RegistroEgresadosTerminacionEstudios registro, Estudiante estudiante, Operacion operacion) {
        if(registro == null) return ResultadoEJB.crearErroneo(1, "Operación no autorizada.", RegistroEgresadosTerminacionEstudios.class);
        if(operacion == null) return ResultadoEJB.crearErroneo(2, "La operación no debe ser nula.", RegistroEgresadosTerminacionEstudios.class);
        RegistroEgresadosTerminacionEstudios r = em.createQuery("select r from RegistroEgresadosTerminacionEstudios as r where r.idEstudiante.idEstudiante = :id and r.generacion = :generacion", RegistroEgresadosTerminacionEstudios.class)
                .setParameter("id", registro.getIdEstudiante().getIdEstudiante())
                .setParameter("generacion", registro.getGeneracion())
                .getResultStream().findFirst().orElse(null);
        //System.out.println("Registro:"+ r);
        switch (operacion) {
            case PERSISTIR:
                if(r == null){
                    r = registro;
                    r.setIdEstudiante(estudiante);
                    r.setGeneracion(registro.getGeneracion());
                    r.setFolio(registro.getFolio());
                    r.setLibro(registro.getLibro());
                    r.setFoja(registro.getFoja());
                    r.setFechaEmision(registro.getFechaEmision());
                    em.persist(r);
                            return ResultadoEJB.crearCorrecto(r, "Se ha agregado correctamente");
                    
                }else{
                    return ResultadoEJB.crearErroneo(4, "La operacion no puede ser efectuada.", RegistroEgresadosTerminacionEstudios.class);
                }
            default:
                return ResultadoEJB.crearErroneo(3, "Operación no autorizada.", RegistroEgresadosTerminacionEstudios.class);
        }

    }
    
    
    //////////////////Metodos para validar el registro a realizar//////////////
    public ResultadoEJB<RegistroEgresadosTerminacionEstudios> obtenerUltimoRegistro(){
        RegistroEgresadosTerminacionEstudios r = em.createQuery("SELECT r from RegistroEgresadosTerminacionEstudios as r ORDER BY r.idRegistro desc", RegistroEgresadosTerminacionEstudios.class)
                .getResultStream().findFirst().orElse(new RegistroEgresadosTerminacionEstudios());
        if(r == null) return ResultadoEJB.crearErroneo(1, "Aun no hay registro en la tabla", RegistroEgresadosTerminacionEstudios.class);
        return ResultadoEJB.crearCorrecto(r, "Registro encontrado");
    }
    
    public ResultadoEJB<RegistroEgresadosTerminacionEstudios> obtenerUltimoRegistro(String generacion){
        RegistroEgresadosTerminacionEstudios r = em.createQuery("SELECT r from RegistroEgresadosTerminacionEstudios as r where r.generacion = :generacion ORDER BY r.idRegistro desc", RegistroEgresadosTerminacionEstudios.class)
                .setParameter("generacion", generacion)
                .getResultStream().findFirst().orElse(new RegistroEgresadosTerminacionEstudios());
        if(r == null) return ResultadoEJB.crearErroneo(1, "Aun no hay registro en la tabla", RegistroEgresadosTerminacionEstudios.class);
        return ResultadoEJB.crearCorrecto(r, "Registro encontrado");
    }
    
    
    public ResultadoEJB<Estudiante> obtenerEstudiante(Integer matricula){
        Estudiante e = em.createQuery("select e from Estudiante as e where e.matricula = :matricula and e.grupo.grado = :grado", Estudiante.class)
                .setParameter("matricula", matricula)
                .setParameter("grado", 6)
                .getResultStream().findFirst().orElse(new Estudiante());
        if(e.getIdEstudiante() == null) return ResultadoEJB.crearErroneo(1, "No se encontro el estudiante", Estudiante.class);
        return ResultadoEJB.crearCorrecto(e, "Estudiante");
    }
    
    public ResultadoEJB<RegistroEgresadosTerminacionEstudios> obtenerRegistro(Integer matricula){
        RegistroEgresadosTerminacionEstudios r = em.createQuery("SELECT r FROM RegistroEgresadosTerminacionEstudios as r where r.idEstudiante.idEstudiante = :matricula", RegistroEgresadosTerminacionEstudios.class)
                .setParameter("matricula", matricula)
                .getResultStream().findFirst().orElse(new RegistroEgresadosTerminacionEstudios());
        if(r.getIdRegistro() == null) return ResultadoEJB.crearErroneo(1, "No hay registro", RegistroEgresadosTerminacionEstudios.class);
        return ResultadoEJB.crearCorrecto(r, "Registro");
    }
    
    public ResultadoEJB<SeguimientoEstadiaEstudiante> obtenerEstadia(Integer idEstudiante){
        SeguimientoEstadiaEstudiante s = em.createQuery("select s from SeguimientoEstadiaEstudiante as s where s.estudiante.idEstudiante = :idEstudiante", SeguimientoEstadiaEstudiante.class)
                .setParameter("idEstudiante", idEstudiante)
                .getResultStream().findFirst().orElse(new SeguimientoEstadiaEstudiante());
        if(s.getEstudiante().getIdEstudiante() == null) return ResultadoEJB.crearErroneo(1, "Seguimiento no encontrado", SeguimientoEstadiaEstudiante.class);
        return ResultadoEJB.crearCorrecto(s, "Seguimiento");
    }
    
    public ResultadoEJB<AreasUniversidad> obtenerProgramaEducativo(Short programa){
        AreasUniversidad a = em.createQuery("select a from AreasUniversidad as a where a.area = :area", AreasUniversidad.class)
                .setParameter("area", programa)
                .getResultStream().findFirst().orElse(new AreasUniversidad());
        if(a.getArea() == null) return ResultadoEJB.crearErroneo(1, "Area no encontrada", AreasUniversidad.class);
        return ResultadoEJB.crearCorrecto(a, "Area");
    }
    
    public ResultadoEJB<Personal> obtenerJefeServciosEscolares(){
        Personal p = em.createQuery("select p from Personal as p where p.areaOperativa = :areaOp and (p.categoriaOperativa.categoria = :categoriaOp or p.categoriaOperativa.categoria = :categoriaOp1)", Personal.class)
                .setParameter("areaOp", Short.parseShort("10"))
                .setParameter("categoriaOp", Short.parseShort("20"))
                .setParameter("categoriaOp1", Short.parseShort("24"))
                .getResultStream().findFirst().orElse(new Personal());
        return ResultadoEJB.crearCorrecto(p, "");
    }
    
    public ResultadoEJB<Personal> obtenerDirectorCarrera(Short area){
        AreasUniversidad areaA = em.createQuery("select a from AreasUniversidad as a where a.area = :areaS", AreasUniversidad.class)
                .setParameter("areaS", area)
                .getResultStream().findFirst().orElse(new AreasUniversidad());
        Personal p = em.createQuery("select p from Personal as p where p.clave = :clave", Personal.class)
                .setParameter("clave", areaA.getResponsable())
                .getResultStream().findFirst().orElse(new Personal());
        return ResultadoEJB.crearCorrecto(p, "");
    }
    
    public ResultadoEJB<List<PlanCompetencias>> obtenerListaCompetencias(PlanEstudio plan, String tipo){
        List<PlanCompetencias> listaC = em.createQuery("select p from PlanCompetencias as p where p.idPlan = :plan and p.tipo = :tipo", PlanCompetencias.class)
                .setParameter("plan", plan.getIdPlanEstudio())
                .setParameter("tipo", tipo)
                .getResultStream()
                .collect(Collectors.toList());
        if(listaC.isEmpty()) return ResultadoEJB.crearCorrecto(new ArrayList<>(), "Lista vacia");
        return ResultadoEJB.crearCorrecto(listaC, "Lista comleta");
    }
    
    public ResultadoEJB<List<Estudiante>> obtenerEstudiante(){
        List<Estudiante> e = em.createQuery("select e from Estudiante as e where e.grupo.grado = :grado and (e.tipoEstudiante.idTipoEstudiante = :tipo1 or e.tipoEstudiante.idTipoEstudiante = :tipo2)", Estudiante.class)
                .setParameter("grado", 6)
                .setParameter("tipo1", 1)
                .setParameter("tipo2", 4)
                .getResultStream()
                .map(estudiante -> verificarRegistro(estudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(e, "Lista completa");
    }
    
    public ResultadoEJB<Estudiante> verificarRegistro(Estudiante e){
        Estudiante estudiante;
        ResultadoEJB<RegistroEgresadosTerminacionEstudios> registro = obtenerRegistro(e.getIdEstudiante());
        if(registro.getValor() == null){
            estudiante = e;
        }else{
            return ResultadoEJB.crearErroneo(1, "El estudiante ya cuenta con un registro", Estudiante.class);
        }
        return ResultadoEJB.crearCorrecto(estudiante, "Estudiante agregado");
    }
    
    public ResultadoEJB<VariablesProntuario> obtenerNivelEducativo(Integer clave){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :clave", VariablesProntuario.class)
                .setParameter("clave", clave.toString())
                .getResultStream().findFirst().orElse(new VariablesProntuario());
        return ResultadoEJB.crearCorrecto(vp, "Variable");
    }

    public ResultadoEJB<VariablesProntuario> obtenerCargo(Integer clave){
        VariablesProntuario vp = em.createQuery("select v from VariablesProntuario as v where v.nombre = :clave and v.descripcion = :descripcion", VariablesProntuario.class)
                .setParameter("clave", clave.toString())
                .setParameter("descripcion", "cargo")
                .getResultStream().findFirst().orElse(new VariablesProntuario());
        return ResultadoEJB.crearCorrecto(vp, "Variable");
    }
}
