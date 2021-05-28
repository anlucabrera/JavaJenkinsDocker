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
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoHistorialMovEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMovimientoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbHistorialMovEstudiante")
public class EjbHistorialMovEstudiante {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /* MÓDULO DE CONSULTA HISTORIAL MOVIMIENTOS DEL ESTUDIANTE - SERVICIOS ESCOLARES */
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbHistorialMovEstudiante.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite identificar a una lista de posibles estudiantes para consultar el historial de movimientos
     * @param pista Contenido que la pista que puede incluir parte del nombre, apellidos o curp del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<Persona>> buscarEstudiante(String pista){
        try{
             //buscar lista de personas por nombre o por curp, según la pista y ordenado por nombre
            List<Persona> listaPersonas = em.createQuery("select p from Persona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, p.curp) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre", Persona.class)
                    .setParameter("pista", pista)
                    .getResultList();
           
            return ResultadoEJB.crearCorrecto(listaPersonas, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes registrados. (EjbHistorialMovEstudiante.buscarEstudiante)", e, null);
        }
    }
   
    /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }
     
    /**
     * Permite obtener el registro de persona de la curp ingresada
     * @param curp Curp del estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Persona> buscarRegistroPersona(String curp){
        try{
              Persona persona = em.createQuery("SELECT p FROM Persona p WHERE p.curp =:curp", Persona.class)
                    .setParameter("curp", curp)
                    .getSingleResult();
             
              if(persona == null) return ResultadoEJB.crearErroneo(2, persona, "La curp ingresada no tiene registro.");
              else return ResultadoEJB.crearCorrecto(persona, "Registro de la persona con la curp ingresada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el registro de la persona con la curp ingresada. (EjbHistorialMovEstudiante.buscarRegistroPersona)", e, null);
        }
    }
   
     /**
     * Permite obtener el historial de lista de movimientos del estudiante, en todos los registros que se encuentre
     * @param persona Registro de la persona
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoHistorialMovEstudiante>> buscarHistorialMovEstudiante(Persona persona){
        try{
              List<DtoHistorialMovEstudiante> registrosAspirante = em.createQuery("SELECT a FROM Aspirante a WHERE a.idPersona.idpersona =:persona ORDER BY a.idProcesoInscripcion.idPeriodo ASC", Aspirante.class)
                    .setParameter("persona", persona.getIdpersona())
                    .getResultStream()
                    .distinct()
                    .map(historialMov -> pack(historialMov))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
              
              if(registrosAspirante.isEmpty()) return ResultadoEJB.crearErroneo(3, registrosAspirante, "La curp ingresada no tiene historial de registros.");
              else return ResultadoEJB.crearCorrecto(registrosAspirante, "Historial de registro de la curp ingresada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el historial de lista de movimiento del estudiante. (EjbHistorialMovEstudiante.buscarHistorialMovEstudiante)", e, null);
        }
    }
    
     /**
     * Empaqueta un registro de Persona en su DTO Wrapper para su historial de movimientos
     * @param aspirante Registro de Persona
     * @return historial del movimiento empaquetado
     */
    public ResultadoEJB<DtoHistorialMovEstudiante> pack(Aspirante aspirante){
        try{
            if(aspirante == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un registro de aspirante nula.", DtoHistorialMovEstudiante.class);
            if(aspirante.getIdAspirante()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un registro de aspirante con clave nula.", DtoHistorialMovEstudiante.class);

            Aspirante aspiranteBD = em.find(Aspirante.class, aspirante.getIdAspirante());
            
            if(aspiranteBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un registro de aspirante no registrado previamente en base de datos.", DtoHistorialMovEstudiante.class);
           
            List<Estudiante> listaEstudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.aspirante.idAspirante =:aspirante ORDER BY e.periodo ASC", Estudiante.class)
                    .setParameter("aspirante", aspiranteBD.getIdAspirante())
                    .getResultList();
            
            Estudiante estudiante = listaEstudiante.get(0);
            
            Integer lista = listaEstudiante.size();
            
            Estudiante estudianteActual = listaEstudiante.get(lista-1);
            
            AreasUniversidad programaActual = em.find(AreasUniversidad.class, estudianteActual.getCarrera());
            
            PeriodosEscolares periodoActual = em.find(PeriodosEscolares.class, estudianteActual.getPeriodo());
            
            List<DtoMovimientoEstudiante> listaMovimientos = new ArrayList<>();
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());

            DtoMovimientoEstudiante datosAdmision = getDatosAdmision(aspiranteBD).getValor();
            listaMovimientos.add(datosAdmision);
            DtoMovimientoEstudiante datosInscripcion = getDatosInscripcion(aspirante).getValor();
            listaMovimientos.add(datosInscripcion);
            List<DtoMovimientoEstudiante> datosReinscripciones = getDatosReinscripcion(estudiante.getMatricula()).getValor();
            if (!datosReinscripciones.isEmpty()) {
                datosReinscripciones.forEach(reinscripcion -> {
                listaMovimientos.add(reinscripcion);
                });
            } 
            DtoMovimientoEstudiante datosBaja = getDatosBaja(estudiante.getMatricula()).getValor();
            if (datosBaja != null) {
                listaMovimientos.add(datosBaja);
            } 
            
            List<DtoMovimientoEstudiante> listaOrdenada = listaMovimientos.stream()
                    .sorted(Comparator.comparing(DtoMovimientoEstudiante::getFecha))
                    .collect(Collectors.toList());
           
            DtoHistorialMovEstudiante dto = new DtoHistorialMovEstudiante(aspirante.getIdProcesoInscripcion(), aspirante, estudiante, programaEducativo, listaOrdenada, estudianteActual.getTipoEstudiante().getDescripcion(), programaActual.getNombre(), periodoActual);
           
            return ResultadoEJB.crearCorrecto(dto, "Información de historial de movimiento del estudiante empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la información de historial de movimiento del estudiante (EjbHistorialMovEstudiante. pack).", e,  DtoHistorialMovEstudiante.class);
        }
    }
    
     /**
     * Permite obtener los datos de admisión del estudiante seleccionado
     * @param aspirante Registro de aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoMovimientoEstudiante> getDatosAdmision(Aspirante aspirante){
        try{
           
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, aspirante.getIdProcesoInscripcion().getIdPeriodo());
            String periodoEscolar = periodo.getMesInicio().getMes()+ " - " + periodo.getMesFin().getMes()+ " " + periodo.getAnio();
            String tipoMovimiento = "Admisión";
            Persona persona = em.find(Persona.class, aspirante.getIdPersona().getIdpersona());
            String personalRealizo = persona.getApellidoPaterno()+ " " + persona.getApellidoMaterno()+ " " + persona.getNombre();
            String informacionMovimiento = "Folio aspirante: " +  aspirante.getFolioAspirante();
            
            DtoMovimientoEstudiante dto = new DtoMovimientoEstudiante(aspirante.getFechaRegistro(), periodoEscolar, tipoMovimiento, informacionMovimiento, personalRealizo);
            
            return ResultadoEJB.crearCorrecto(dto, "Datos admisión empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar datos de admisión (EjbHistorialMovEstudiante. getDatosAdmision).", e, DtoMovimientoEstudiante.class);
        }
    }
    
   /**
     * Permite obtener los datos de inscripción del estudiante seleccionado
     * @param aspirante Registro de aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoMovimientoEstudiante> getDatosInscripcion(Aspirante aspirante){
        try{
            Estudiante estudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.aspirante.idAspirante =:aspirante AND e.tipoRegistro =:tipoRegistro", Estudiante.class)
                    .setParameter("aspirante", aspirante.getIdAspirante())
                    .setParameter("tipoRegistro", "Inscripción")
                    .getSingleResult();
            
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            String periodoEscolar = periodo.getMesInicio().getMes()+ " - " + periodo.getMesFin().getMes()+ " " + periodo.getAnio();
            String tipoMovimiento = estudiante.getTipoRegistro();
            Personal personal = em.find(Personal.class, estudiante.getTrabajadorInscribe());
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
            Grupo grupo = em.find(Grupo.class, estudiante.getGrupo().getIdGrupo());
            String informacionMovimiento = "Grupo: "+ grupo.getGrado()+ "° " + grupo.getLiteral()+" "+ grupo.getIdSistema().getNombre()+ " de "+ programaEducativo.getNombre();
            
            DtoMovimientoEstudiante dto = new DtoMovimientoEstudiante(estudiante.getFechaAlta(), periodoEscolar, tipoMovimiento, informacionMovimiento, personal.getNombre());
            
            return ResultadoEJB.crearCorrecto(dto, "Datos inscripción empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar datos de inscripción (EjbHistorialMovEstudiante. getDatosInscripcion).", e, DtoMovimientoEstudiante.class);
        }
    }
    
    /**
     * Permite obtener los datos de reinscripción del estudiante seleccionado
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoMovimientoEstudiante>> getDatosReinscripcion(Integer matricula){
        try{
            List<String> listaTiposMovimientos = new ArrayList();
            listaTiposMovimientos.add("Reinscripcion Autónoma");
            listaTiposMovimientos.add("Reinscripcion");
            listaTiposMovimientos.add("Reincorporación otra UT");
            listaTiposMovimientos.add("Reincorporación misma UT");
            listaTiposMovimientos.add("Regularización de calificaciones por reincoporación");
            listaTiposMovimientos.add("Reincorporación otra generación");
            listaTiposMovimientos.add("Cambio de grupo");
            listaTiposMovimientos.add("Cambio de plan de estudio");
            listaTiposMovimientos.add("Cambio de programa educativo");
            listaTiposMovimientos.add("Equivalencia");
            
            
            List<Estudiante> listaEstudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.tipoRegistro IN :tipoRegistro", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("tipoRegistro", listaTiposMovimientos)
                    .getResultList();
           
            List<DtoMovimientoEstudiante> listaDtoEstudiantes = new ArrayList<>();
            
            listaEstudiante.forEach(est -> {
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, est.getPeriodo());
                String periodoEscolar = periodo.getMesInicio().getMes() + " - " + periodo.getMesFin().getMes() + " " + periodo.getAnio();
                String tipoMovimiento = est.getTipoRegistro();
                String reinscribio;
                if(est.getTrabajadorInscribe() == null){
                    reinscribio = "Proceso Autónomo";
                }else{
                    Personal personal = em.find(Personal.class, est.getTrabajadorInscribe());
                    reinscribio = personal.getNombre();
                }
                
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, est.getCarrera());
                Grupo grupo = em.find(Grupo.class, est.getGrupo().getIdGrupo());
                String informacionMovimiento = "Grupo: " + grupo.getGrado() + "° " + grupo.getLiteral() + " " + grupo.getIdSistema().getNombre() + " de " + programaEducativo.getNombre();

                DtoMovimientoEstudiante dto = new DtoMovimientoEstudiante(est.getFechaAlta(), periodoEscolar, tipoMovimiento, informacionMovimiento, reinscribio);
                listaDtoEstudiantes.add(dto);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Datos reinscripción empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar datos de reinscripción (EjbHistorialMovEstudiante. getDatosReinscripcion).", e, null);
        }
    }
    
    /**
     * Permite obtener los datos de baja del estudiante seleccionado
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoMovimientoEstudiante> getDatosBaja(Integer matricula){
        try{
            Baja baja = em.createQuery("SELECT b FROM Baja b INNER JOIN b.estudiante e WHERE e.matricula =:matricula", Baja.class)
                    .setParameter("matricula", matricula)
                    .getSingleResult();
            
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, baja.getPeriodoEscolar());
            String periodoEscolar = periodo.getMesInicio().getMes()+ " - " + periodo.getMesFin().getMes()+ " " + periodo.getAnio();
            String tipoMovimiento = "Baja";
            Personal personal = em.find(Personal.class, baja.getEmpleadoRegistro());
            BajasTipo bajaTipo = em.find(BajasTipo.class, baja.getTipoBaja());
            BajasCausa bajaCausa = em.find(BajasCausa.class, baja.getCausaBaja());
            
            String informacionMovimiento = bajaTipo.getDescripcion()+ " por "+ bajaCausa.getCausa();
            
            DtoMovimientoEstudiante dto = new DtoMovimientoEstudiante(baja.getFechaBaja(), periodoEscolar, tipoMovimiento, informacionMovimiento, personal.getNombre());
            
            return ResultadoEJB.crearCorrecto(dto, "Datos de baja empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar datos de baja (EjbHistorialMovEstudiante. getDatosBaja).", e, DtoMovimientoEstudiante.class);
        }
    }
    
    
}
