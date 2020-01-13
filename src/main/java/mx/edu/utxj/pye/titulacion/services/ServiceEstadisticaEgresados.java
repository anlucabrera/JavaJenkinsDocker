/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EjbEstudioEgresados;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;

import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.entity.PersonasPK;
import mx.edu.utxj.pye.titulacion.dto.dtoEstadisticaEgresados;
import mx.edu.utxj.pye.titulacion.dto.dtoEstudianteParaReporte;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstadisticaEgresados;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServiceEstadisticaEgresados implements EjbEstadisticaEgresados{

    @EJB Facade facade;
    @EJB Facade2 facade2;
    
    @EJB EjbEstudioEgresados ejbEstudioEgresados;
    
    @EJB EjbCarga ejbCarga;
    
    // Parametros para generar reporte
    @Getter @Setter Integer repPeriodo = 0;
    @Getter @Setter String repCuatrimestre = "";
    @Getter @Setter List<MatriculaPeriodosEscolares> listadoInscritosGeneral = new ArrayList<>();
    
    public static final String ACTUALIZADOConcentrado = "concentradoTitulacion.xlsx";
    public static final String ACTUALIZADOListado = "listadoTitulacion.xlsx";
    
    @Override
    public List<Generaciones> getGeneracionesConRegistro() {
        List<Short> claves = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e", ExpedientesTitulacion.class)
                    .getResultStream()
                    .map(e -> e.getGeneracion())
                    .collect(Collectors.toList());

            return facade.getEntityManager().createQuery("SELECT g FROM Generaciones g WHERE g.generacion IN :claves ORDER BY g.generacion desc", Generaciones.class)
                    .setParameter("claves", claves)
                    .getResultList();
    }

    @Override
    public List<String> getNivelesConRegistro(Generaciones generacion) {
         if(generacion == null){
            return null;
        }

        List<Integer> niveles = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion", ExpedientesTitulacion.class)
                .setParameter("generacion", generacion.getGeneracion())
                .getResultList()
                .stream()
                .map(p -> p.getNivel())
                .distinct()
                .collect(Collectors.toList());
       
        List<String> listaNivelesTotal = new ArrayList<>();
        niveles.forEach(nivel -> {
            if(nivel == 1){
                listaNivelesTotal.add("Técnico Superior Universitario");
            }else{
                listaNivelesTotal.add("Ingeniería/Licenciatura");
            }
        });
                
        List<String> listaNiveles = listaNivelesTotal.stream().distinct().collect(Collectors.toList());
        
        return listaNiveles;
    }

    @Override
    public List<dtoEstadisticaEgresados> obtenerReporteIntegracionExp(Generaciones generacion, String nivel) {
        try{
           
            List<Integer> grados = new ArrayList<>();
            
            switch (nivel) {
                case "Técnico Superior Universitario":
                    grados.add(5);
                    grados.add(6);
                    break;
                case "Ingeniería/Licenciatura":
                    grados.add(10);
                    grados.add(11);
                    break;
                default:
                    System.err.println("No existe nivel educativo");
                    break;
            }
            
            ProcesosGeneraciones procesosGeneraciones = facade.getEntityManager().createQuery("SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.generacion =:generacion AND p.procesosGeneracionesPK.grado IN :grados", ProcesosGeneraciones.class)
                            .setParameter("generacion", generacion.getGeneracion())
                            .setParameter("grados", grados)
                            .getSingleResult();
        
            repPeriodo = procesosGeneraciones.getProcesosGeneracionesPK().getPeriodo();
            repCuatrimestre = String.valueOf(procesosGeneraciones.getProcesosGeneracionesPK().getGrado());
            
            listadoInscritosGeneral = obtenerEstudiantesInscritosGeneral(repPeriodo, repCuatrimestre);
            
            String cuatrimestre = String.valueOf(procesosGeneraciones.getProcesosGeneracionesPK().getGrado());
            
            List<Short> listaProgramas = facade.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo =:periodo AND m.cuatrimestre =:cuatrimestre", MatriculaPeriodosEscolares.class)
                        .setParameter("periodo", procesosGeneraciones.getProcesosGeneracionesPK().getPeriodo())
                        .setParameter("cuatrimestre", cuatrimestre)
                        .getResultList()
                        .stream()
                        .map(p -> p.getProgramaEducativo())
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
           
            List<dtoEstadisticaEgresados> listaDtoEstEgresados = new ArrayList<>();
            
            listaProgramas.forEach(programa -> {
                dtoEstadisticaEgresados dto = new dtoEstadisticaEgresados();
                
                AreasUniversidad progEdu = facade.getEntityManager().find(AreasUniversidad.class, programa);
                
                List<MatriculaPeriodosEscolares> listaInscritos = obtenerEstudiantesInscritos(procesosGeneraciones.getProcesosGeneracionesPK().getPeriodo(), cuatrimestre, programa);
                List<Alumnos> listaEgresados = obtenerEgresados(listaInscritos, procesosGeneraciones);
                List<Alumnos> listaAcredEstadia = obtenerEgresadosAcreditaronEstadia(listaInscritos, procesosGeneraciones);
                List<ExpedientesTitulacion> listaExp = obtenerExpedientes(listaInscritos, procesosGeneraciones, progEdu.getSiglas());
                List<ExpedientesTitulacion> listaExpVal = obtenerExpedientesValidados(listaInscritos, procesosGeneraciones, progEdu.getSiglas());
                
                String genInicio = String.valueOf(generacion.getInicio());
                String genFin = String.valueOf(generacion.getFin());
                PeriodosEscolares periodo = facade.getEntityManager().find(PeriodosEscolares.class, procesosGeneraciones.getProcesosGeneracionesPK().getPeriodo());
                String anio = String.valueOf(periodo.getAnio());
                
                dto.setGeneracion(genInicio.concat(" - ").concat(genFin));
                dto.setPrograma(progEdu.getSiglas());
                dto.setPeriodo(periodo.getMesInicio().getAbreviacion().concat(" - ").concat(periodo.getMesFin().getAbreviacion()).concat(" ").concat(anio));
                dto.setCuatrimestre(procesosGeneraciones.getProcesosGeneracionesPK().getGrado());
                dto.setInscritos(listaInscritos.size());
                dto.setEgresados(listaEgresados.size());
                dto.setAcredEstadia(listaAcredEstadia.size());
                dto.setIntExp(listaExp.size());
                dto.setExpVal(listaExpVal.size());
                dto.setListaInscritos(listaInscritos);
                
                listaDtoEstEgresados.add(dto);
            });
            
            return listaDtoEstEgresados;
            
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<MatriculaPeriodosEscolares> obtenerEstudiantesInscritos(Integer periodo, String cuatrimestre, Short programa) {
         
        List<MatriculaPeriodosEscolares> listaMatricula = facade.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo =:periodo AND m.cuatrimestre =:cuatrimestre AND m.programaEducativo =:programa", MatriculaPeriodosEscolares.class)
                        .setParameter("periodo", periodo)
                        .setParameter("cuatrimestre", cuatrimestre)
                        .setParameter("programa", programa)
                        .getResultList();
        
        return listaMatricula;
    }
   
    @Override
    public List<Alumnos> obtenerEgresados(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones procesosGeneraciones) {
        
        List<Integer> claveStatus = Arrays.asList(1, 6, 7, 8, 9);
        
        List<String> matriculas = listaMatriculas.stream().map(p -> p.getMatricula()).collect(Collectors.toList());

        List<Alumnos> listaAlumnosEgresados = facade2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.grupos.gruposPK.cvePeriodo =:periodo AND a.cveStatus IN :status AND a.gradoActual =:grado AND a.matricula IN :matriculas", Alumnos.class)
                .setParameter("periodo", procesosGeneraciones.getProcesosGeneracionesPK().getPeriodo())
                .setParameter("status",claveStatus)
                .setParameter("grado", procesosGeneraciones.getProcesosGeneracionesPK().getGrado())
                .setParameter("matriculas", matriculas)
                .getResultStream().collect(Collectors.toList());
        
        return listaAlumnosEgresados;
    }
    
     
    @Override
    public List<Alumnos> obtenerEgresadosAcreditaronEstadia(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones procesosGeneraciones) {
        
        Integer status = 6;
        
        List<String> matriculas = listaMatriculas.stream().map(p -> p.getMatricula()).collect(Collectors.toList());

        List<Alumnos> listaAlumnosEgresados = facade2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.grupos.gruposPK.cvePeriodo =:periodo AND a.cveStatus =:status AND a.gradoActual =:grado AND a.matricula IN :matriculas", Alumnos.class)
                .setParameter("periodo", procesosGeneraciones.getProcesosGeneracionesPK().getPeriodo())
                .setParameter("status",status)
                .setParameter("grado", procesosGeneraciones.getProcesosGeneracionesPK().getGrado())
                .setParameter("matriculas", matriculas)
                .getResultStream().collect(Collectors.toList());
        
        return listaAlumnosEgresados;
    }


    @Override
    public List<ExpedientesTitulacion> obtenerExpedientes(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones proceso, String programa) {
        
        List<String> matriculas = listaMatriculas.stream().map(p -> p.getMatricula()).collect(Collectors.toList());
         
        List<ExpedientesTitulacion> listaExpedientes = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.proceso.proceso =:proceso AND e.programaEducativo =:programa AND e.matricula.matricula IN :matriculas", ExpedientesTitulacion.class)
                .setParameter("proceso", proceso.getProcesosGeneracionesPK().getProceso())
                .setParameter("programa", programa)
                .setParameter("matriculas", matriculas)
                .getResultStream().collect(Collectors.toList());
        
        return listaExpedientes;
    }

    @Override
    public List<ExpedientesTitulacion> obtenerExpedientesValidados(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones proceso, String programa) {
        
        List<String> matriculas = listaMatriculas.stream().map(p -> p.getMatricula()).collect(Collectors.toList());
        
        List<ExpedientesTitulacion> listaExpedientesVal = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.proceso.proceso =:proceso AND e.programaEducativo =:programa AND e.validado =:validado AND e.matricula.matricula IN :matriculas", ExpedientesTitulacion.class)
                .setParameter("proceso", proceso.getProcesosGeneracionesPK().getProceso())
                .setParameter("programa", programa)
                .setParameter("validado", true)
                .setParameter("matriculas", matriculas)
                .getResultStream().collect(Collectors.toList());
        
        return listaExpedientesVal;
    }
    
    @Override
    public List<dtoEstudianteParaReporte> obtenerListadoGeneral(Generaciones generacion) {
        
        String genInicio = String.valueOf(generacion.getInicio());
        String genFin = String.valueOf(generacion.getFin());
                
        List<dtoEstudianteParaReporte> listaDtoEstReporte = new ArrayList<>();
        
        //construir la lista de dto's para mostrar en tabla
        listadoInscritosGeneral.forEach(i -> {
            String genero = i.getCurp().substring(10, 11);
            Alumnos alumno = facade2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.grupos.gruposPK.cvePeriodo =:periodo AND a.gradoActual =:grado AND a.matricula =:matricula", Alumnos.class)
            .setParameter("periodo", repPeriodo)
            .setParameter("grado", Short.valueOf(repCuatrimestre))
            .setParameter("matricula", i.getMatricula())
            .getResultStream().findFirst().orElse(null);
            
            PersonasPK pk = new PersonasPK(alumno.getAlumnosPK().getCveAlumno(), alumno.getAlumnosPK().getCveUniversidad());
            Personas persona = facade2.getEntityManager().find(Personas.class, pk);
            
//            Alumnos alumno = ejbEstudioEgresados.getAlumnoPorMatricula(i.getMatricula());
//            Personas persona = ejbEstudioEgresados.getDatosPersonalesAlumnos(alumno.getAlumnosPK().getCveAlumno());
            String status = convertirStatusSAIIUT(alumno.getCveStatus());
            AreasUniversidad programa = facade.getEntityManager().find(AreasUniversidad.class, i.getProgramaEducativo());
            String expTit = expedienteTitulacion(i.getMatricula(), programa.getSiglas());
            
            dtoEstudianteParaReporte dto = new dtoEstudianteParaReporte();
            dto.setGeneracion(genInicio.concat(" - ").concat(genFin));
            dto.setProgEducativo(programa.getSiglas());
            dto.setMatricula(i.getMatricula());
            dto.setNombre(persona.getApellidoPat().concat(" ").concat(persona.getApellidoMat()).concat(" ").concat(persona.getNombre()));
            dto.setGenero(genero);
            dto.setStatus(status);
            dto.setExpValidado(expTit);
            
            listaDtoEstReporte.add(dto);
        });
        
        return listaDtoEstReporte;
    }

    @Override
    public String convertirStatusSAIIUT(Integer claveStatus) {
        String status ="";
        switch (claveStatus) {
               case 1:
                   status ="Regular";
                   break;
               case 4:
                   status ="Baja Temporal";
                   break;
               case 5:
                   status ="Baja Definitiva";
                   break;
               case 6:
                   status ="Egresado No Titulado";
                   break;
               case 7:
                   status ="Titulación en Trámite";
                   break;
               case 8:
                   status ="Titulado sin documentos";
                   break;
               case 9:
                   status ="Titulado con documentos";
                   break;
               case 13:
                   status ="No concluyó estadía";
                   break;
               default:
                   System.err.println("No existe clave de status del estudiante");
                   break;
           }
        return status;
    }

    @Override
    public String expedienteTitulacion(String matricula, String programa) {
        String expediente="";
        ExpedientesTitulacion expTit = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.programaEducativo =:programa AND e.matricula.matricula =:matricula", ExpedientesTitulacion.class)
                .setParameter("programa", programa)
                .setParameter("matricula", matricula)
                .getResultStream().findFirst().orElse(null);
        
        if (expTit == null) {
            expediente = "Sin expediente";
        } else {
            if(expTit.getValidado()){
                expediente = "Validado";
            }else{
                expediente = "Sin validar";
            }
        }
       
        return expediente;
    }
    
    @Override
    public List<MatriculaPeriodosEscolares> obtenerEstudiantesInscritosGeneral(Integer periodo, String cuatrimestre) {
         List<MatriculaPeriodosEscolares> listaMatricula = facade.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.periodo =:periodo AND m.cuatrimestre =:cuatrimestre", MatriculaPeriodosEscolares.class)
                        .setParameter("periodo", periodo)
                        .setParameter("cuatrimestre", cuatrimestre)
                        .getResultList();
        
        return listaMatricula;
    }
    
    @Override
    public String getReporteGeneracionNivel(Generaciones generacion, String nivel) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\reporteGeneralTit.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADOConcentrado);
        
        Map beans = new HashMap();
        beans.put("concentradoTit", obtenerReporteIntegracionExp(generacion, nivel));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getListadoGeneracionNivel(Generaciones generacion) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\reporteListadoTit.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADOListado);
        
        Map beans = new HashMap();
        beans.put("listadoTit", obtenerListadoGeneral(generacion));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }

}
