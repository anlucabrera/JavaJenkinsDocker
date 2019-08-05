package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConfiguracionUnidadMateriaRolDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDiasPeriodoEscolares;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;

@Stateless(name = "EjbConfiguracionUnidadMateria")
public class EjbConfiguracionUnidadMateria {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    /**
     * Permite validar si el usuario autenticado es personal docente
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbUnidadMateriaConfiguracion.validarDocente)", e, null);
        }
    }

    /**
     * Permite verificar si hay un periodo abierto para configurar unidades por materia
     * @param docente Personal Docente que va a realizar la configuración, permite funcionar como filtro en caso de un permiso especifico a su área o a su clave
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CONFIGURACION_DE_MATERIA, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para configuración de evaluaciones del personal docente (EjbConfiguradorMateria.verificarEvento)", e, EventoEscolar.class);
        }
    }

    /**
     * Permite obtener la lista de periodos escolares a elegir en el configurador de unidades por materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            final List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbUnidadMateriaConfiguracion.getPeriodosDescendentes)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo Periodo seleccionado en pantalla
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicaPorDocente(PersonalActivo docente, PeriodosEscolares periodo){
        try{
              List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.docente =:docente AND c.evento.periodo =:periodo", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
              if(cargas.isEmpty()) return ResultadoEJB.crearErroneo(2, cargas, "Usted no tiene carga académica en el periodo seleccionado.");
              else return ResultadoEJB.crearCorrecto(cargas, "Carga académica por docente y periodo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbUnidadMateriaConfiguracion.getCargaAcademicaPorDocente)", e, null);
        }
    }
    
     /**
     * Empaqueta una carga académica en su DTO Wrapper
     * @param cargaAcademica Carga académica que se va a empaquetar
     * @return Carga académica empaquetada
     */
    public ResultadoEJB<DtoCargaAcademica> pack(CargaAcademica cargaAcademica){
        try{
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una carga académica nula.", DtoCargaAcademica.class);
            if(cargaAcademica.getCarga() == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar una carga académica con clave nula.", DtoCargaAcademica.class);

            CargaAcademica cargaAcademicaBD = em.find(CargaAcademica.class, cargaAcademica.getCarga());
            if(cargaAcademicaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar una carga académica no registrada previamente en base de datos.", DtoCargaAcademica.class);
            
            Grupo grupo = em.find(Grupo.class, cargaAcademicaBD.getCveGrupo().getIdGrupo());
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, cargaAcademicaBD.getEvento().getPeriodo());
            PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, cargaAcademicaBD.getIdPlanMateria().getIdPlanMateria());
            PlanEstudio planEstudio = em.find(PlanEstudio.class, planEstudioMateria.getIdPlan().getIdPlanEstudio());
            Materia materia = em.find(Materia.class, planEstudioMateria.getIdMateria().getIdMateria());
            PersonalActivo docente = ejbPersonalBean.pack(cargaAcademicaBD.getDocente());
            AreasUniversidad programa = em.find(AreasUniversidad.class, planEstudioMateria.getIdPlan().getIdPe());
            DtoCargaAcademica dto = new DtoCargaAcademica(cargaAcademicaBD, periodo, docente, grupo, materia, programa, planEstudio, planEstudioMateria);
            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoCargaAcademica.class);
        }
    }
    
    /**
     * Permite verificar si existe configuración de las unidades materia de la carga académica seleccionada
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracion>> buscarConfiguracionUnidadMateria(DtoCargaAcademica dtoCargaAcademica){
        try{
            List<UnidadMateriaConfiguracion> listaUnidMatConf = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaUnidMatConf, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbUnidadMateriaConfiguracion.getConfiguracionUnidadMateria)", e, null);
        }
    }
 
    /**
     * Permite obtener la lista de configuración de unidad por materia sugerida, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se sugerirá configuración
     * @return Resultado del proceso
     */
    public List<DtoConfiguracionUnidadMateria> getConfiguracionUnidadMateriaSugerida(DtoCargaAcademica dtoCargaAcademica){
            List<DtoConfiguracionUnidadMateria> unidadMat = em.createQuery("SELECT um FROM UnidadMateria um WHERE um.idMateria.idMateria =:idMateria", UnidadMateria.class)
                    .setParameter("idMateria", dtoCargaAcademica.getCargaAcademica().getIdPlanMateria().getIdMateria().getIdMateria())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return unidadMat;
    }
   
    /**
     * Empaqueta una configuración sugeridad de la unidad materia en su DTO Wrapper
     * @param unidadMateria Unidades de la materia a empaquetar
     * @return Configuración sugerida de la unidad materia empaquetada
     */
    public ResultadoEJB<DtoConfiguracionUnidadMateria> pack(UnidadMateria unidadMateria){
        try{
            if(unidadMateria == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una configuración sugerida de la unidad nula.", DtoConfiguracionUnidadMateria.class);

            UnidadMateria unidadMateriaBD = em.find(UnidadMateria.class, unidadMateria.getIdUnidadMateria());
            if(unidadMateriaBD == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar una configuración sugerida de una unidad de materia no registrada previamente en base de datos.", DtoConfiguracionUnidadMateria.class);
         
            UnidadMateriaConfiguracion unidadMateriaConfiguracion = new UnidadMateriaConfiguracion();
            
            DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(unidadMateriaBD, unidadMateriaConfiguracion);
         
            return ResultadoEJB.crearCorrecto(dto, "Configuración sugerida de la unidad materia empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la configuración sugerida de la unidad materia (EjbUnidadMateriaConfiguracion.pack).", e, DtoConfiguracionUnidadMateria.class);
        }
    }
    
      
    /**
     * Permite obtener la cantidad de días que hay entre la fecha de inicio y fecha de fin del periodo escolar activo para realizar la configuración
     * @param periodoActivo Clave del periodo que se calculará
     * @return Resultado del proceso
     */
    public DtoDiasPeriodoEscolares getCalculoDiasPeriodoEscolar(Integer periodoActivo) {
        
            PeriodoEscolarFechas fechasPeriodos = em.createQuery("SELECT pef FROM PeriodoEscolarFechas pef WHERE pef.periodosEscolares.periodo =:periodo", PeriodoEscolarFechas.class)
                    .setParameter("periodo", periodoActivo)
                    .getSingleResult();
//            int dias = (int) ((fechasPeriodos.getFin().getTime() - fechasPeriodos.getInicio().getTime()) / 86400000);
            long fechaInicial = fechasPeriodos.getInicio().getTime();
            long fechaFinal = fechasPeriodos.getFin().getTime();
            long diferenciaDias = fechaFinal - fechaInicial;
            int dias = (int) TimeUnit.DAYS.convert(diferenciaDias, TimeUnit.MILLISECONDS);
            
            DtoDiasPeriodoEscolares dtoDiasPeriodoEscolares = new DtoDiasPeriodoEscolares(fechasPeriodos.getInicio(), fechasPeriodos.getFin(), dias);
           
            return dtoDiasPeriodoEscolares;
            
    }
    
     /**
     * Permite obtener configuración sugerida, calculando fecha de inicio y de fin de cada unidad de la carga académica seleccionada
     * @param dtoCargaAcademica Materia de la que se sugerirá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoConfiguracionUnidadMateria>> getConfiguracionSugerida(DtoCargaAcademica dtoCargaAcademica){
        try{
            Double valorPorHora = getValorPorHora(dtoCargaAcademica);
            
            DtoDiasPeriodoEscolares dtoDiasPeriodoEscolares = getCalculoDiasPeriodoEscolar(dtoCargaAcademica.getCargaAcademica().getEvento().getPeriodo());
           
            Integer diasUnidad = dtoDiasPeriodoEscolares.getDias()/dtoCargaAcademica.getMateria().getUnidadMateriaList().size();
           
            Date fechaMin = dtoDiasPeriodoEscolares.getFechaInicio();
           
            List<DtoConfiguracionUnidadMateria> dtoConfSug = new ArrayList<>();
            List<DtoConfiguracionUnidadMateria> unidadMat = getConfiguracionUnidadMateriaSugerida(dtoCargaAcademica);
            
            unidadMat.forEach(umc -> {
                UnidadMateria unidadMateriaBD = em.find(UnidadMateria.class, umc.getUnidadMateria().getIdUnidadMateria());
                
                UnidadMateriaConfiguracion unidadMateriaConfiguracion = new UnidadMateriaConfiguracion();
              
                switch (umc.getUnidadMateria().getNoUnidad()) {
                    case 1:
                        Calendar calendarFin1 = Calendar.getInstance();
                        calendarFin1.setTime(fechaMin);
                        calendarFin1.add(Calendar.DAY_OF_YEAR, diasUnidad);
                        unidadMateriaConfiguracion.setFechaInicio(fechaMin);
                        unidadMateriaConfiguracion.setFechaFin(calendarFin1.getTime());
                        break;
                    case 2:
                        Calendar calendarIni2 = Calendar.getInstance();
                        calendarIni2.setTime(fechaMin);
                        calendarIni2.add(Calendar.DAY_OF_YEAR, diasUnidad);
                        
                        Calendar calendarFin2 = Calendar.getInstance();
                        calendarFin2.setTime(fechaMin);
                        calendarFin2.add(Calendar.DAY_OF_YEAR, diasUnidad * 2);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni2.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin2.getTime());
                        break;
                    case 3:
                        Calendar calendarIni3 = Calendar.getInstance();
                        calendarIni3.setTime(fechaMin);
                        calendarIni3.add(Calendar.DAY_OF_YEAR, diasUnidad * 2);
                        
                        Calendar calendarFin3 = Calendar.getInstance();
                        calendarFin3.setTime(fechaMin);
                        calendarFin3.add(Calendar.DAY_OF_YEAR, diasUnidad * 3);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni3.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin3.getTime());
                        break;
                    case 4:
                        Calendar calendarIni4 = Calendar.getInstance();
                        calendarIni4.setTime(fechaMin);
                        calendarIni4.add(Calendar.DAY_OF_YEAR, diasUnidad * 3);
                        
                        Calendar calendarFin4 = Calendar.getInstance();
                        calendarFin4.setTime(fechaMin);
                        calendarFin4.add(Calendar.DAY_OF_YEAR, diasUnidad * 4);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni4.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin4.getTime());
                        break;
                    case 5:
                        Calendar calendarIni5 = Calendar.getInstance();
                        calendarIni5.setTime(fechaMin);
                        calendarIni5.add(Calendar.DAY_OF_YEAR, diasUnidad * 4);
                        
                        Calendar calendarFin5 = Calendar.getInstance();
                        calendarFin5.setTime(fechaMin);
                        calendarFin5.add(Calendar.DAY_OF_YEAR, diasUnidad * 5);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni5.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin5.getTime());
                        break;
                    case 6:
                        Calendar calendarIni6 = Calendar.getInstance();
                        calendarIni6.setTime(fechaMin);
                        calendarIni6.add(Calendar.DAY_OF_YEAR, diasUnidad * 5);
                        
                        Calendar calendarFin6 = Calendar.getInstance();
                        calendarFin6.setTime(fechaMin);
                        calendarFin6.add(Calendar.DAY_OF_YEAR, diasUnidad * 6);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni6.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin6.getTime());
                        break;
                    case 7:
                        Calendar calendarIni7 = Calendar.getInstance();
                        calendarIni7.setTime(fechaMin);
                        calendarIni7.add(Calendar.DAY_OF_YEAR, diasUnidad * 6);
                        
                        Calendar calendarFin7 = Calendar.getInstance();
                        calendarFin7.setTime(fechaMin);
                        calendarFin7.add(Calendar.DAY_OF_YEAR, diasUnidad * 7);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni7.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin7.getTime());
                        break;
                    case 8:
                        Calendar calendarIni8 = Calendar.getInstance();
                        calendarIni8.setTime(fechaMin);
                        calendarIni8.add(Calendar.DAY_OF_YEAR, diasUnidad * 7);
                        
                        Calendar calendarFin8 = Calendar.getInstance();
                        calendarFin8.setTime(fechaMin);
                        calendarFin8.add(Calendar.DAY_OF_YEAR, diasUnidad * 8);
                        
                        unidadMateriaConfiguracion.setFechaInicio(calendarIni8.getTime());
                        unidadMateriaConfiguracion.setFechaFin(calendarFin8.getTime());
                        break;
                    default:
                       
                        break;
                }
                
                Integer horasUnidad = umc.getUnidadMateria().getHorasTeoricas() + umc.getUnidadMateria().getHorasPracticas();

                Double porcentaje = horasUnidad * valorPorHora;

                unidadMateriaConfiguracion.setPorcentaje(porcentaje);
                        
                
                DtoConfiguracionUnidadMateria dtoConfiguracionUnidadMateria = new DtoConfiguracionUnidadMateria(unidadMateriaBD, unidadMateriaConfiguracion);
                dtoConfSug.add(dtoConfiguracionUnidadMateria);
               
            });
            return ResultadoEJB.crearCorrecto(dtoConfSug, "Lista de configuración sugerida de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración sugerida de la materia del docente. (EjbUnidadMateriaConfiguracion.getConfiguracionUnidadMateriaSugerida)", e, null);
        }
    }
    
    public Double getValorPorHora(DtoCargaAcademica dtoCargaAcademica) {
        Double valorPorHora = 0.0, horasTotales;

        TypedQuery<Double> hP = (TypedQuery<Double>) f.getEntityManager().createQuery("SELECT SUM(u.horasPracticas) FROM UnidadMateria u WHERE u.idMateria.idMateria =:materia GROUP BY u.idMateria.idMateria");
        hP.setParameter("materia", dtoCargaAcademica.getCargaAcademica().getIdPlanMateria().getIdMateria().getIdMateria());
        hP.getSingleResult();
        
        Number horasPracticas = ((Number) hP.getSingleResult());
        double horasP = horasPracticas.doubleValue();
        
        TypedQuery<Double> hT = (TypedQuery<Double>) f.getEntityManager().createQuery("SELECT SUM(u.horasTeoricas) FROM UnidadMateria u WHERE u.idMateria.idMateria =:materia GROUP BY u.idMateria.idMateria");
        hT.setParameter("materia", dtoCargaAcademica.getCargaAcademica().getIdPlanMateria().getIdMateria().getIdMateria());
        hT.getSingleResult();

        Number horasTeorias = ((Number) hT.getSingleResult());
        double horasT = horasTeorias.doubleValue();
       
        horasTotales = horasT + horasP;
        
        valorPorHora = 100 / horasTotales;
        
        return valorPorHora;
        
    }
    
    /**
     * Permite guardar la configuración de la unidad materia
     * @param configuracionUnidadMaterias Unidad configuracion materia que se va a guardar
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<DtoConfiguracionUnidadMateria>> guardarConfUnidadMateria(List<DtoConfiguracionUnidadMateria> configuracionUnidadMaterias, CargaAcademica cargaAcademica){
        try{            
            if(configuracionUnidadMaterias == null || configuracionUnidadMaterias.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(3, "La carga académica no debe ser nula.");
          
            List<DtoConfiguracionUnidadMateria> l = new ArrayList<>();
           
            configuracionUnidadMaterias.forEach(cum -> {
                try {
                    UnidadMateriaConfiguracion umc = new UnidadMateriaConfiguracion();
                    umc.setFechaInicio(cum.getUnidadMateriaConfiguracion().getFechaInicio());
                    umc.setFechaFin(cum.getUnidadMateriaConfiguracion().getFechaFin());
                    umc.setCarga(cargaAcademica);
                    umc.setIdUnidadMateria(cum.getUnidadMateria());
                    umc.setPorcentaje(cum.getUnidadMateriaConfiguracion().getPorcentaje());
                    f.create(umc);
                    DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(cum.getUnidadMateria(), umc);
                    l.add(dto);
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.guardarConfUnidadMateria)", e, null);
        }
    }
    
    /**
     * Permite guardar la configuración de la unidad materia
     * @param tareaIntegradora Unidad configuracion materia que se va a guardar
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<TareaIntegradora> guardarTareaIntegradora(TareaIntegradora tareaIntegradora, CargaAcademica cargaAcademica){
        try{            
            if(tareaIntegradora == null) return ResultadoEJB.crearErroneo(2, "La tarea integradora no debe ser nula.");
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(3, "La carga académica no debe ser nula.");
          
            TareaIntegradora ti = new TareaIntegradora();
            ti.setDescripcion(tareaIntegradora.getDescripcion());
            ti.setFechaEntrega(tareaIntegradora.getFechaEntrega());
            ti.setCarga(cargaAcademica);
            ti.setPorcentaje(tareaIntegradora.getPorcentaje());
            f.create(ti);
               
            return ResultadoEJB.crearCorrecto(ti, "La tarea integradora se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la tarea integradora de la materia. (EjbUnidadMateriaConfiguracion.guardarTareaIntegradora)", e, null);
        }
    }
    
     /**
     * Permite guardar la configuración de la unidad por criterios
     * @param configuracionUnidadMaterias Lista de Configuración Unidad Materia que se guardó previamente, para obtener la clave de la configuración
     * @param cargaAcademica Carga académica de la que se guardó configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracionCriterio>> guardarConfiguracionUnidadMateriaCriterios(List<DtoConfiguracionUnidadMateria> configuracionUnidadMaterias, DtoCargaAcademica cargaAcademica){
        try{  
            if(configuracionUnidadMaterias == null) return ResultadoEJB.crearErroneo(2, "La lista de configuración unidad materia no puede ser nula.");
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(3, "La carga académica no debe ser nula.");
          
            List<Criterio> listaCriterios = em.createQuery("SELECT c FROM Criterio c WHERE c.nivel =:nivel", Criterio.class)
                    .setParameter("nivel", cargaAcademica.getPrograma().getNivelEducativo().getNivel())
                    .getResultList();
            
            List<UnidadMateriaConfiguracionCriterio> lista = new ArrayList<>();
           
            configuracionUnidadMaterias.forEach(cum -> {
                try {
                    listaCriterios.forEach(c -> {
                        UnidadMateriaConfiguracionCriterioPK umcdPK = new UnidadMateriaConfiguracionCriterioPK(cum.getUnidadMateriaConfiguracion().getConfiguracion(), c.getCriterio()); 
                        UnidadMateriaConfiguracionCriterio umcd = new UnidadMateriaConfiguracionCriterio(umcdPK, c.getPorcentajeRecomendado());
                        f.create(umcd);
                        lista.add(umcd);
                    });
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(lista, "La configuración de la unidad materia por criterio se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia por criterio. (EjbUnidadMateriaConfiguracion.guardarConfiguracionUnidadMateriaCriterios)", e, null);
        }
    }
    
    /**
     * Permite eliminar la configuración de la unidad materia
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarConfUnidadMateria(CargaAcademica cargaAcademica){
        try{ 
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.");
            
            Integer delete = em.createQuery("DELETE FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:carga", UnidadMateriaConfiguracion.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "La configuración de la unidad materia se elimino correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar  la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.eliminarConfUnidadMateria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de configuración de unidad por materia, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoConfiguracionUnidadMateria>> getConfiguracionUnidadMateria(DtoCargaAcademica dtoCargaAcademica){
        try{
            List<DtoConfiguracionUnidadMateria> unidadMatConfDto = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(unidadMatConfDto, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbUnidadMateriaConfiguracion.getConfiguracionUnidadMateria)", e, null);
        }
    }
    
    /**
     * Empaqueta una configuración sugeridad de la unidad materia en su DTO Wrapper
     * @param unidadMateriaConfiguracion Unidades de la materia a empaquetar
     * @return Configuración sugerida de la unidad materia empaquetada
     */
    public ResultadoEJB<DtoConfiguracionUnidadMateria> pack(UnidadMateriaConfiguracion unidadMateriaConfiguracion){
        try{
            if(unidadMateriaConfiguracion == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una configuración de la unidad nula.", DtoConfiguracionUnidadMateria.class);

            UnidadMateria unidadMateria = em.find(UnidadMateria.class, unidadMateriaConfiguracion.getIdUnidadMateria().getIdUnidadMateria());
            
            DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(unidadMateria, unidadMateriaConfiguracion);
         
            return ResultadoEJB.crearCorrecto(dto, "Configuración de la unidad materia empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la configuración de la unidad materia (EjbUnidadMateriaConfiguracion.pack).", e, DtoConfiguracionUnidadMateria.class);
        }
    }
    
    /**
     * Permite obtener la lista de configuración de unidad por materia, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<TareaIntegradora> getTareaIntegradora(DtoCargaAcademica dtoCargaAcademica){
        try{
            TareaIntegradora tareaIntegradora = em.createQuery("SELECT ti FROM TareaIntegradora ti WHERE ti.carga.carga =:carga", TareaIntegradora.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getSingleResult();
            return ResultadoEJB.crearCorrecto(tareaIntegradora, "Tarea Integradora de la materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la tarea integradora de la materia seleccionada. (EjbUnidadMateriaConfiguracion.getTareaIntegradora)", e, null);
        }
    }
    
    /**
     * Permite eliminar la configuración de la unidad materia
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarTareaIntegradora(CargaAcademica cargaAcademica){
        try{ 
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.");
            
            Integer delete = em.createQuery("DELETE FROM TareaIntegradora t WHERE t.carga.carga =:carga", TareaIntegradora.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "La tarea integradora se elimino correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar  la tarea integradora. (EjbUnidadMateriaConfiguracion.eliminarTareaIntegradora)", e, null);
        }
    }
    
     /**
     * Permite detectar una lista de mensajes de posibles errores en la asignación docente, como es el caso de superar las horas máximas frente a grupo de acuerdo a si es PTC o PA el docente o si se han asignado
     * mas de una materia al mismo docente en un grupo detemrinado
     * @param rol DTO de la capa de sincronización con los datos seleccionados por el usuario
     * @return Lista de mensajes encontrados.
     */
    public ResultadoEJB<List<DtoAlerta>> identificarMensajes(ConfiguracionUnidadMateriaRolDocente rol){
        try{
            
            if(rol.getPeriodo().getPeriodo().equals(rol.getEventoActivo().getPeriodo()) && rol.getExiste()){
                
                DtoDiasPeriodoEscolares dtoDiasPeriodosEscolares = getCalculoDiasPeriodoEscolar(rol.getPeriodoActivo());
                final List<DtoAlerta> mensajes = new ArrayList<>();

                ResultadoEJB<List<DtoConfiguracionUnidadMateria>> resUnidadesConf = getConfiguracionUnidadMateria(rol.getCarga());
                ResultadoEJB<TareaIntegradora> resTareaIntegradora = getTareaIntegradora(rol.getCarga());
                TareaIntegradora tareaInt = resTareaIntegradora.getCorrecto()?resTareaIntegradora.getValor():null;
                
                if(resUnidadesConf.getCorrecto()){
                    List<UnidadMateriaConfiguracion> unidadMatConf = resUnidadesConf.getValor().stream()
                            .filter(dtoConfUnidad -> dtoConfUnidad.getUnidadMateriaConfiguracion()!= null)
                            .map(DtoConfiguracionUnidadMateria::getUnidadMateriaConfiguracion)
                            .distinct()
                            .collect(Collectors.toList());

                    unidadMatConf.forEach(conf -> {
                        if(conf.getFechaInicio().before(dtoDiasPeriodosEscolares.getFechaInicio()))
                        {
                            mensajes.add(new DtoAlerta(String.format("La fecha de inicio de la unidad %s es %s,y el periodo escolar activo inicia %s.", conf.getIdUnidadMateria().getNoUnidad(), conf.getFechaInicio(), dtoDiasPeriodosEscolares.getFechaInicio()), AlertaTipo.SUGERENCIA));
                        }
                        else if(conf.getFechaFin().before(dtoDiasPeriodosEscolares.getFechaFin())){
                            mensajes.add(new DtoAlerta(String.format("La fecha de fin de la unidad %s es %s,y el periodo escolar activo termina %s.", conf.getIdUnidadMateria().getNoUnidad(), conf.getFechaFin(), dtoDiasPeriodosEscolares.getFechaFin()), AlertaTipo.SUGERENCIA));
                        }
                        else if(tareaInt.getFechaEntrega().before(dtoDiasPeriodosEscolares.getFechaInicio()) || tareaInt.getFechaEntrega().after(dtoDiasPeriodosEscolares.getFechaInicio())){
                            mensajes.add(new DtoAlerta(String.format("La fecha de entrega de la tarea integradora es %s y los periodos en los que se debe encontrar son %s y %s.", tareaInt.getFechaEntrega(), dtoDiasPeriodosEscolares.getFechaInicio(), dtoDiasPeriodosEscolares.getFechaFin()), AlertaTipo.SUGERENCIA));
                        }
                    });
                }
                return ResultadoEJB.crearCorrecto(mensajes, "Lista de mensajes");
            }else{
                return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Sin mensajes");
            }
        }catch (Exception e){
//            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudieron identificar mensajes de configuración (EjbUnidadMateriaConfiguracion.identificarMensajes).", e, null);
        }
    }
    
    public Integer validarSumaPorcentajesUnidadTI(List<DtoConfiguracionUnidadMateria> listaUnidades, TareaIntegradora tareaIntegradora)
    {
            Integer valor = 0;
            Double porcentajeTI = tareaIntegradora.getPorcentaje();
            List<UnidadMateriaConfiguracion> unidadMatConf = new ArrayList<>();
          
            listaUnidades.forEach(l -> {
            UnidadMateriaConfiguracion umc = l.getUnidadMateriaConfiguracion();
            unidadMatConf.add(umc);
            });
            
            Double porcentajesUnidad = unidadMatConf.stream().mapToDouble(UnidadMateriaConfiguracion::getPorcentaje).sum();
            Double totalPorcentajes = porcentajeTI + porcentajesUnidad;
            
            if(totalPorcentajes > 100.00){
                valor = 1;
            }
            else if(totalPorcentajes < 100.00){
                valor = 2;
            }
            else{
                valor = 0;
            }
            
            return valor;
    }
    
    public Integer validarSumaPorcentajesUnidad(List<DtoConfiguracionUnidadMateria> listaUnidades)
    {
            Integer valor = 0;
            
            List<UnidadMateriaConfiguracion> unidadMatConf = new ArrayList<>();
          
            listaUnidades.forEach(l -> {
            UnidadMateriaConfiguracion umc = l.getUnidadMateriaConfiguracion();
            unidadMatConf.add(umc);
            });
            
            Double porcentajesUnidad = unidadMatConf.stream().mapToDouble(UnidadMateriaConfiguracion::getPorcentaje).sum();
            
            if(porcentajesUnidad > 100.00){
                valor = 1;
            }
            else if(porcentajesUnidad < 100.00){
                valor = 2;
            }
            else{
                valor = 0;
            }
            
            return valor;
    }
}