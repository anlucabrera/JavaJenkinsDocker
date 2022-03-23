/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.DtoSeguimientoTestVocacional;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestVocacional;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorTestVocacional;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EjbSeguimientoTestVocacional {
    @EJB            Facade                                              f;
    @EJB            mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean             ejbPersonalBean;
    @EJB            EjbPropiedades                                      ejbPropiedades;
    @EJB            EjbValidacionRol                                    ejbValidacionRol;
    @EJB            EjbTestVocacional                                   ejbTestVocacionalEstudiante;
    
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite verificar que el área de psicopedagogía solo tenga acceso al modulo específico
     * @param clave
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarPsicopedagogia(Integer clave){
        return ejbValidacionRol.validarPsicopedagogia(clave);
    }
    
    /**
     * Obtiene únicamente los periodos escolares que contienen información ya registrada de la aplicación de Test Vocacional
     * @return 
     */
    public ResultadoEJB<List<PeriodosEscolares>> obtenerPeriodosEscolaresGruposTests(){
        try {
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT tv.periodoEscolar FROM TestVocacional tv GROUP BY tv.periodoEscolar ORDER BY tv.periodoEscolar DESC", Integer.class)
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrado periodos escolares con infomación registrada en sistema");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares que cuenta con información registrada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con registros de tipo Test Vocacional (EjbSeguimientoTestVocacional.obtenerPeriodosEscolaresGruposTests)", e, null);
        }
    }
    
    /**
     * Obtiene los programas educativos filtrados por periodo escolar que de igual manera solo tengan información de tipo Test Vocacional
     * @param periodoEscolar
     * @return 
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerProgramasEducativosTestsPorPeriodo(PeriodosEscolares periodoEscolar){
        try {
            List<AreasUniversidad> programasEducativos = em.createQuery("SELECT e.carrera FROM Estudiante e INNER JOIN FETCH e.aspirante a INNER JOIN FETCH a.idPersona p INNER JOIN FETCH p.testVocacional tv WHERE e.periodo = :periodoEscolar AND tv.periodoEscolar = :periodoEscolar GROUP BY e.carrera ORDER BY e.carrera ASC", Short.class)
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .map(programaEducativo -> em.find(AreasUniversidad.class, programaEducativo))
                    .distinct()
                    .sorted(Comparator.comparing(programaEducativo -> programaEducativo.getNivelEducativo().getNivel()))
                    .collect(Collectors.toList());
            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrado programas educativo con informacion registrada en sistema");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativos, "Lista de programas educativos que cuentan con información registrada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con registros de tipo Test Vocacional (EjbSeguimientoTestVocacional.obtenerProgramasEducativosTestsPorPeriodo)", e, null);
        }
    }
    
    /**
     * Obtiene los grupos del periodo y programa educativos que contienen información de registros de Test Vocacional.
     * El listado de grupos no se filtra para obtener la información de estudiantes que cuentan o no con al menos un registro.
     * @param periodoEscolar
     * @param programaEducativo
     * @return 
     */
    public ResultadoEJB<List<Grupo>> obtenerGruposTestsPorPeriodoPrograma(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo){
        try {
            List<Grupo> grupos = em.createQuery("SELECT g FROM Grupo g WHERE g.periodo = :periodoEscolar AND g.idPe = :programaEducativo GROUP BY g.idGrupo ORDER BY g.grado, g.literal", Grupo.class)
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultList();
            if(grupos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrado grupos en el periodo y programa educativo seleccionados");
            }else{
                return ResultadoEJB.crearCorrecto(grupos, "Lista de grupos obtenida correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos con registros de tipo Test Vocacional (EjbSeguimientoTestVocacional.obtenerGruposTestsPorPeriodoPrograma)", e, null);
        }
    }
    
    /**
     * Obtiene la lista de estudiantes que realizarón, están realizando o no han ingresado al sistema para la aplicación de Test Vocacinal
     * Se obtiene información específica para la fácil detección en la interfaz del usuario
     * @param periodoEscolar
     * @param programaEducativo
     * @param grupo
     * @return 
     */
    public ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante>> obtenerListaTestVocacionalPorPeriodoProgramaGrupo(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo, Grupo grupo){
        try {
            List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaResultados = new ArrayList<>();
            List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.grupo g INNER JOIN FETCH e.tipoEstudiante te WHERE g.idGrupo = :grupo AND te.idTipoEstudiante NOT IN (2,3,7)", Estudiante.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultList();
            if(listaEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han podido recuperar los datos debido a que el grupo seleccionado no cuenta con estudiantes");
            }else{
                listaEstudiantes.stream().forEach((e) -> {
                    ResultadoEJB<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> pack = packTestVocacionalResultado(e);
                    if(pack.getCorrecto())
                        listaResultados.add(pack.getValor());
                });
                String mensaje = "Lista de resultados de estudiantes del grupo: " + grupo.getGrado() + "° " + grupo.getLiteral();
                return ResultadoEJB.crearCorrecto(listaResultados, mensaje);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener los resultados de los estudiantes debido a un problema interno (EjbSeguimientoTestVocacional.obtenerTestVocacionalPorPeriodoProgramaGrupo)", e, null);
        }
    }
    
    /***
     * Método que permite el empaquetado de un test vocacional de estudiantes en general.
     * Presenta tres tipos de resultados:
     * Caso 1.- Detecta si existen registros
     * Caso 2.- Detecta si el registro está incompleto
     * Caso 3.- Detecta si el registro está completado y obtiene los resultados del mismo
     * @param estudiante
     * @return 
     */
    public ResultadoEJB<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> packTestVocacionalResultado(Estudiante estudiante){
            try {
            TestVocacional testVocacional = em.createQuery("SELECT tv FROM Estudiante e INNER JOIN FETCH e.aspirante a INNER JOIN FETCH a.idPersona p INNER JOIN FETCH p.testVocacional tv WHERE tv.idPersona = :persona AND tv.periodoEscolar = :periodoEscolar", TestVocacional.class)
                    .setParameter("persona", estudiante.getAspirante().getIdPersona().getIdpersona())
                    .setParameter("periodoEscolar", estudiante.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
            if(testVocacional == null){
                DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante packTest = new DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante(estudiante, programaEducativo, false, "Sin ingresar al sistema");
//                Empaquetado caso 1, sin Test Vocacional Realizado
                return ResultadoEJB.crearCorrecto(packTest, "Caso 1");
            }else{
                Comparador<TestVocacional> comparador = new ComparadorTestVocacional();
                Boolean finalizado = comparador.isCompleto(testVocacional);
                if(!finalizado){
                    
                    DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante packTest = new DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante(estudiante, programaEducativo, true, "Iniciado, sin terminar", testVocacional);
//                    Empaquetado caso 2, Test Vocacional sin terminar aún no se pueden consultar los resultados.
                    return ResultadoEJB.crearCorrecto(packTest, "Caso 2");
                }else{
                    ResultadoEJB<Map<String, Double>> resultadosTest = ejbTestVocacionalEstudiante.obtenerResultadosCompletosTestVocacional(testVocacional);
                    if(resultadosTest.getCorrecto()){
                        Map<String, Double> resultadoTestMap = resultadosTest.getValor();
                        List<String> listaResultados = new ArrayList<>();
                        for (Map.Entry<String, Double> i : resultadoTestMap.entrySet()) {
                            listaResultados.add("PE: " + i.getKey() + " Porcentaje: " + i.getValue());
                        }
                        DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante packTest = 
                                new DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante(
                                        estudiante, 
                                        programaEducativo, 
                                        finalizado, 
                                        "Test Completado", 
                                        testVocacional, 
                                        listaResultados.get(0), 
                                        listaResultados.get(1), 
                                        listaResultados.get(2));
//                        Empaquetado caso 3, Test Vocacional con Resultados
                        return ResultadoEJB.crearCorrecto(packTest, "Caso 3");
                    }else{
                        return ResultadoEJB.crearErroneo(2, null, "No se han podido recuperar los resultados del Test Vocacional, ocurrió un error al momento de generarlos");
                    }
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo crear el empaquetado del resultado del Test Vocacional (EjbSeguimientoTestVocacional.packTestVocacionalResultado)", e, null);
        }
    }
    
    /**
     * Método que permite obtener los resultados grupales de la realización del Test Vocacional
     * @param periodoEscolar
     * @param programaEducativo
     * @return 
     */
    public ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal>> listaResultadosTestVocacionalResumenGrupal(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo){
        try {
            if(periodoEscolar == null || programaEducativo == null) return ResultadoEJB.crearErroneo(2, null, "Asegurese de elegir un periodo escolar y un programa educativo para mostrar el resumen grupal");    
//            Lista que contiene el resumen de los resultados
            List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal> listaAvanceGrupal = new ArrayList<>();
            
            ResultadoEJB<List<Grupo>> resultadoEjbListaGrupos = obtenerGruposTestsPorPeriodoPrograma(periodoEscolar, programaEducativo);
            
            if(!resultadoEjbListaGrupos.getCorrecto()){
                return ResultadoEJB.crearErroneo(1, null, "Para visualizar un resumen de resultados es necesario que el programa educativo contenga grupos asignados");
            }else{
                resultadoEjbListaGrupos.getValor().stream().forEach((g) -> {
                    DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal resumenGrupal = new DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal();
                    resumenGrupal.setGrupo(g);
                    g.getEstudianteList().removeIf(tipoEstudiante -> tipoEstudiante.getTipoEstudiante().getIdTipoEstudiante().equals((short)2) || tipoEstudiante.getTipoEstudiante().getIdTipoEstudiante().equals(3) || tipoEstudiante.getTipoEstudiante().getIdTipoEstudiante().equals(7));
                    resumenGrupal.setTotalEstudiantesGrupo(g.getEstudianteList().size());
                    List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> resultadoEstudiante = obtenerListaTestVocacionalPorPeriodoProgramaGrupo(periodoEscolar, programaEducativo, g).getValor();
                    
                    List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaCompletos = new ArrayList<>();
                    List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaIncompletos = new ArrayList<>();
                    List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaSinEntrar = new ArrayList<>();
                    
                    listaCompletos = obtenerListaCompletos(resultadoEstudiante);
                    listaIncompletos = obtenerListaIncompletos(resultadoEstudiante);
                    listaSinEntrar = obtenerListaSinEntrar(resultadoEstudiante);
                    
                    resumenGrupal.setTotalEstudiantesGrupo(resultadoEstudiante.size());
                    
                    resumenGrupal.setTotalCompletos(listaCompletos.size());
                    resumenGrupal.setTotalIncompletos(listaIncompletos.size());
                    resumenGrupal.setTotalSinEntrar(listaSinEntrar.size());
                    
                    resumenGrupal.setPorcentaje(obtenerPorcentaje(resumenGrupal.getTotalEstudiantesGrupo(), resumenGrupal.getTotalIncompletos(), resumenGrupal.getTotalSinEntrar()));
                    
                    listaAvanceGrupal.add(resumenGrupal);
                });
                return ResultadoEJB.crearCorrecto(listaAvanceGrupal, "Lista de resultados de Avance Grupal de Resultados de Test Vocacional obtenidos correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el resumen grupal de la aplicación del Test Vocacional (EjbSeguimientoTestVocacional.listaResultadosTestVocacionalResumenGrupal)", e, null);
        }
    }
    
    /**
     * 
     * @param periodoEscolar
     * @return 
     */
    public ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo>> listaResultadosTestVocacionalResumenProgramaEducativo(PeriodosEscolares periodoEscolar){
//        try {
            if(periodoEscolar == null) return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se pueden consultar los resultados de test vocacional por programa educativo con un periodo escolar nulo");
            ResultadoEJB<List<AreasUniversidad>> ejbProgramasEducativos = obtenerProgramasEducativosTestsPorPeriodo(periodoEscolar);
            List<AreasUniversidad> programasEducativos= ejbProgramasEducativos.getValor();
            
//            Lista que contiene el resumen de los resultados de la aplicación de Test Vocacional, Se regresa como resultado final
            List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo> listAvanceProgramaEducativo = new ArrayList<>();
            if(!ejbProgramasEducativos.getCorrecto()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "No se pueden consultar los resultados por programa educativo debido a que la lista se encuentra vacía, no se detectaron resultados de estudiantes con registros activos o terminados");
            }else{
                programasEducativos.stream().forEach((pe) -> {
                    DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo resumenProgramaEducativo = new DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo();
//                    Siglas
                    resumenProgramaEducativo.setSiglas(pe.getSiglas());
//                    Programa educativo
                    resumenProgramaEducativo.setProgramaEducativo(pe.getNombre());
//                    Si no se detecta ningún empaquetado se devuelve una tabla en ceros
//                        Total Matricula
                    resumenProgramaEducativo.setTotalMatricula(0);
//                        Total Completos
                    resumenProgramaEducativo.setTotalCompletos(0);
//                        Total Incompletos
                    resumenProgramaEducativo.setTotalIncompletos(0);
//                        Total Sin Entrar
                    resumenProgramaEducativo.setTotalSinEntrar(0);
//                        Total Porcentaje
                    resumenProgramaEducativo.setPorcentaje(0.0D);
                    
//                    resumenProgramaEducativo.setProgramaEducativoCompleto(pe);
                    
//                    Obtiene la lista de estudiantes para poder crear el empaquetado de tipo resultado de test vocacional de cada estudiante
                    ResultadoEJB<List<Estudiante>> ejbListaEstudiante = conteoEstudiantesActivosProgramaEducativoPorPeriodo(periodoEscolar, pe);
                    
//                    Verifica que existan estudiantes para empaquetar, de lo contrario el metodo siguiente no sería necesario
                    if(ejbListaEstudiante.getCorrecto()){
//                        Se llena la lista de estudiantes para proceder a empaquetar los resultados del test vocacional
                        List<Estudiante> listaEstudiante = ejbListaEstudiante.getValor();
//                        Total Matricula - Dato Verificado
                        resumenProgramaEducativo.setTotalMatricula(listaEstudiante.size()); 
                        
//                        resumenProgramaEducativo.setListaTotalMatricula(listaEstudiante);
                        
//                        Stream que se utiliza para empaquetar el resultado de cada estudiante y poder obtener los resultados
//            Lista que contiene los empaquetados de Test Vocacional, se utiliza para obtener los resultados
                        List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaTestVocacionalesResultados = new ArrayList<>();
                        listaEstudiante.stream().forEach((re) -> {
                            ResultadoEJB<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> packTest = packTestVocacionalResultado(re);
                            if(packTest.getCorrecto()){
                                DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante testVocacionalEstudiantes = packTest.getValor();
                                listaTestVocacionalesResultados.add(testVocacionalEstudiantes);
                            }
                        });
                        
                        if(!listaTestVocacionalesResultados.isEmpty()){
                            List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaCompletosAvanceGrupal = new ArrayList<>();
                            List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaIncompletosAvanceGrupal = new ArrayList<>();
                            List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaSinEntrarAvanceGrupal = new ArrayList<>();
                            
                            listaCompletosAvanceGrupal = obtenerListaCompletos(listaTestVocacionalesResultados);
                            listaIncompletosAvanceGrupal = obtenerListaIncompletos(listaTestVocacionalesResultados);
                            listaSinEntrarAvanceGrupal = obtenerListaSinEntrar(listaTestVocacionalesResultados);
                            
                            resumenProgramaEducativo.setTotalCompletos(listaCompletosAvanceGrupal.size());
                            resumenProgramaEducativo.setTotalIncompletos(listaIncompletosAvanceGrupal.size());
                            resumenProgramaEducativo.setTotalSinEntrar(listaSinEntrarAvanceGrupal.size());
                            
//                            resumenProgramaEducativo.setListaTotalCompletos(listaCompletosAvanceGrupal);
//                            resumenProgramaEducativo.setListaTotalIncompletos(listaIncompletosAvanceGrupal);
//                            resumenProgramaEducativo.setListaTotalSinEntrar(listaCompletosAvanceGrupal);
                            
                            resumenProgramaEducativo.setPorcentaje(obtenerPorcentaje(resumenProgramaEducativo.getTotalMatricula(), resumenProgramaEducativo.getTotalIncompletos(), resumenProgramaEducativo.getTotalSinEntrar()));
                        }
                    }
                    listAvanceProgramaEducativo.add(resumenProgramaEducativo);
                });
                return ResultadoEJB.crearCorrecto(listAvanceProgramaEducativo, "Lista de resultados de Avance por Programa Educativo de Resultados de Test Vocacional obtenidos correctamente");
            }
//        } catch (Exception e) {
//            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el resumen por programa educativo de la aplicación del Test Vocacional (EjbSeguimientoTestVocacional.listaResultadosTestVocacionalResumenProgramaEducativo)", e, null);
//        }
    }
    
    
    public ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante>> obtenerListaTestVocacionalPorPeriodo(PeriodosEscolares periodoEscolar){
        try {
            List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaResultados = new ArrayList<>();
            List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.tipoEstudiante te WHERE e.periodo = :periodoEscolar AND te.idTipoEstudiante NOT IN (2,3,7) ORDER BY e.periodo, e.carrera, e.grupo.grado, e.grupo.literal", Estudiante.class)
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .getResultList();
            if(listaEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han podido recuperar los datos debido a que el grupo seleccionado no cuenta con estudiantes");
            }else{
                listaEstudiantes.stream().forEach((e) -> {
                    ResultadoEJB<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> pack = packTestVocacionalResultado(e);
                    if(pack.getCorrecto())
                        listaResultados.add(pack.getValor());
                });
                String mensaje = "Lista de resultados de estudiantes del periodo: " + periodoEscolar.getMesInicio().getMes() + " - " + periodoEscolar.getMesFin().getMes() + " " + periodoEscolar.getAnio();
                return ResultadoEJB.crearCorrecto(listaResultados, mensaje);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener los resultados de los estudiantes debido a un problema interno (EjbSeguimientoTestVocacional.obtenerTestVocacionalPorPeriodoProgramaGrupo)", e, null);
        }
    }
    
    public ResultadoEJB<List<Estudiante>> conteoEstudiantesActivosProgramaEducativoPorPeriodo(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo){
        try {
            List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN FETCH e.tipoEstudiante te WHERE e.periodo = :periodoEscolar AND e.carrera = :programaEducativo AND te.idTipoEstudiante NOT IN (2,3,7) GROUP BY e.idEstudiante", Estudiante.class)
                    .setParameter("periodoEscolar", periodoEscolar.getPeriodo())
                    .setParameter("programaEducativo", programaEducativo.getArea())
                    .getResultList();
            if(listaEstudiantes == null || listaEstudiantes.isEmpty()){
                return ResultadoEJB.crearErroneo(1, null, "No se puede obtener el resumen de resultados del programa educativo por periodo debido a que en este periodo y programa no hay estudiantes");
            }else{
                return ResultadoEJB.crearCorrecto(listaEstudiantes, "Cantidad de estudiantes activos en el programa educativo y periodo escolar seleccionado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el conteo de estudiantes del programa educativo por periodo debido a un error interno", e, null);
        }
    }
    
    /**
     * Elimina los elementos que no ingresaron al Test Vocacional y los elementos que tiene el Test Incompleto - Obtiene el número de estudiantes que tienen completo el Test Vocacional
     * @param listaCompletos
     * @return 
     */
    public List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> obtenerListaCompletos(List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaCompletos){
        List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaNuevo = new ArrayList<>();
        if(!listaCompletos.isEmpty()){
            listaCompletos.stream().forEach((t) -> {
                if(t.getEstatusTest().equals("Test Completado")){
                    listaNuevo.add(t);
                }
            });
            return listaNuevo;
        }else{
            return Collections.EMPTY_LIST;
        }
    }
     
    /**
     * Elimina los elementos que no ingresaron al Test Vocacional y los elementos que tienen el Test Completo - Obtiene el número de estudiantes que tienen incompleto el Test Vocacional
     * @param listaInCompletos
     * @return 
     */
    public List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> obtenerListaIncompletos(List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaInCompletos){
        List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaNuevo = new ArrayList<>();
        if(!listaInCompletos.isEmpty()){
            listaInCompletos.stream().forEach((t) -> {
                if(t.getEstatusTest().equals("Iniciado, sin terminar")){
                    listaNuevo.add(t);
                }
            });
            return listaNuevo;
        }else{
            return Collections.EMPTY_LIST;
        }
    }
    
    /**
     * Elimina los elementos que ingresaron al Test Vocacional - Obtiene el número de estudiantes que no entraron
     * @param listaSinEntrar
     * @return 
     */
    public List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> obtenerListaSinEntrar(List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaSinEntrar){
        List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaNuevo = new ArrayList<>();
        if(!listaSinEntrar.isEmpty()){
            listaSinEntrar.stream().forEach((t) -> {
                if(t.getEstatusTest().equals("Sin ingresar al sistema")){
                    listaNuevo.add(t);
                }
            });
            return listaNuevo;
        }else{
            return Collections.EMPTY_LIST;
        }
    }
    
    public Double obtenerPorcentaje(Integer totalMatricula, Integer totalIncompletos, Integer sinEntrar){
        Double promedio = 0.0D;
        try {
            if(totalMatricula == 0 || totalMatricula == null) return promedio;
            if(totalIncompletos == 0 || totalIncompletos == null) return promedio;
            if(sinEntrar == 0 || sinEntrar == null) return promedio;
            Integer conteo = totalMatricula - totalIncompletos - sinEntrar;
            Double porcentaje = 100 * conteo / (double) totalMatricula;
            return ((double) Math.round(porcentaje * 100d) / 100d);
        } catch (Exception e) {
            return promedio;
        }
    }
    
}
