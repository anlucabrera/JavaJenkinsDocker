/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCumplimientoCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteDocumentosVinculacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.facade.Facade;
import net.sf.jxls.transformer.XLSTransformer;
/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbReportesCartaResponsivaCursoIMSS")
public class EjbReportesCartaResponsivaCursoIMSS {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB EjbCargaCartaResponsivaCursoIMSS ejbCargaCartaResponsivaCursoIMSS;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbCarga ejbCarga;
    public static final String ACTUALIZADO_VINCULACION = "reportesVinculacion.xlsx";
    Integer evidencias = 0, evidenciasValidadas = 0, evidenciasSinValidar = 0;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite obtener el cumplimiento de carga de documentos de seguimientos de vinculación por programa educativo, de la generación y nivel seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCumplimientoCartaResponsivaCursoIMSS>> getCumplimientoDocumentoPE(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            
            List<Short> listaProgramasNivel = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.nivelEducativo.nivel=:nivel", AreasUniversidad.class)
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .map(p -> p.getArea())
                    .collect(Collectors.toList());
            
            List<Short> listaProgramasGeneracion= em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.idPe IN :lista",  Grupo.class)
                    .setParameter("generacion",  generacion.getGeneracion())
                    .setParameter("lista",  listaProgramasNivel)
                    .getResultStream()
                    .map(p -> p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
            
            List<DtoCumplimientoCartaResponsivaCursoIMSS> listaCumplimientoDocumentos = new ArrayList<>();
            
                if (!listaProgramasGeneracion.isEmpty()) {
                    
                listaProgramasGeneracion.forEach(programa -> {
                        AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, programa);
                        
                        List<Integer> grados = new ArrayList<>();
                        grados.add(6);
                        grados.add(11);

                    List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.idPe=:programa AND g.grado IN :grados AND g.generacion=:generacion", Estudiante.class)
                            .setParameter("programa", programaEducativo.getArea())
                            .setParameter("grados", grados)
                            .setParameter("generacion", generacion.getGeneracion())
                            .getResultStream()
                            .collect(Collectors.toList());
                   
                    List<Estudiante> listaActivos = listaEstudiantes.stream().filter(p -> p.getTipoEstudiante().getIdTipoEstudiante() != 2 && p.getTipoEstudiante().getIdTipoEstudiante() != 3).collect(Collectors.toList());
                    
                        List<SeguimientoVinculacionEstudiante> listaSeguimientos = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.grupo.generacion=:generacion AND s.nivel=:nivel AND s.estudiante.grupo.idPe=:programa", SeguimientoVinculacionEstudiante.class)
                                .setParameter("generacion", generacion.getGeneracion())
                                .setParameter("nivel", nivelEducativo.getNivel())
                                .setParameter("programa", programa)
                                .getResultStream()
                                .collect(Collectors.toList());
                        
                        List<Integer> listaSeg = listaSeguimientos.stream().map(p->p.getSeguimientoVinculacion()).collect(Collectors.toList());
                            
                        List<DocumentoProceso> listaDocumentoProceso = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso=:proceso AND d.documento.activo=:valor", DocumentoProceso.class)
                                .setParameter("proceso", "ResponsivaCursosIMSS")
                                .setParameter("valor", Boolean.TRUE)
                                .getResultStream()
                                .collect(Collectors.toList());
                        
                            listaDocumentoProceso.forEach(documento -> {
                                EventoVinculacion eventoDocumento = buscarEventoSeleccionado(generacion, nivelEducativo, documento.getDocumento().getDocumento()).getValor();
                                
                                List<DocumentoSeguimientoVinculacion> listaDocumento = new ArrayList<>();
                                
                                Integer conDocumento = 0, sinDocumento = 0, docValidado = 0;
                                
                                if (!listaSeguimientos.isEmpty()) {
                                    
                                    listaDocumento = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d INNER JOIN d.seguimiento s WHERE d.evento.evento=:evento AND s.seguimientoVinculacion IN :lista", DocumentoSeguimientoVinculacion.class)
                                        .setParameter("evento", eventoDocumento.getEvento())
                                        .setParameter("lista", listaSeg)
                                        .getResultStream()
                                        .collect(Collectors.toList());
                                    
                                    conDocumento = listaDocumento.size();
                                }
                                
                                
                                    sinDocumento = listaEstudiantes.size()-listaDocumento.size();
                                    docValidado = (int) (long) listaDocumento.stream().filter(d->d.getValidado()).count();
                                 
                                Double div1 = (double)listaDocumento.size()/listaEstudiantes.size();
                                Double porcentajeCumplimiento = div1 * 100;
                                Double div2 = (double)docValidado/listaEstudiantes.size();
                                Double procentajeValidacion = div2 * 100;

                                DtoCumplimientoCartaResponsivaCursoIMSS  dtoCumplimientoCartaResponsivaCursoIMSS = new DtoCumplimientoCartaResponsivaCursoIMSS();
                                dtoCumplimientoCartaResponsivaCursoIMSS.setProgramaEducativo(programaEducativo);
                                dtoCumplimientoCartaResponsivaCursoIMSS.setDocumentoProceso(documento);
                                dtoCumplimientoCartaResponsivaCursoIMSS.setEstudiantesIniciaron(listaEstudiantes.size());
                                dtoCumplimientoCartaResponsivaCursoIMSS.setEstudiantesActivos(listaActivos.size());
                                dtoCumplimientoCartaResponsivaCursoIMSS.setConDocumento(conDocumento);
                                dtoCumplimientoCartaResponsivaCursoIMSS.setSinDocumento(sinDocumento);
                                dtoCumplimientoCartaResponsivaCursoIMSS.setDocumentoValidado(docValidado);
                                dtoCumplimientoCartaResponsivaCursoIMSS.setPorcentajeCumplimiento(porcentajeCumplimiento);
                                dtoCumplimientoCartaResponsivaCursoIMSS.setPorcentajeValidacion(procentajeValidacion);
                                listaCumplimientoDocumentos.add(dtoCumplimientoCartaResponsivaCursoIMSS);
                        });
                    });
                }
                
                List<DtoCumplimientoCartaResponsivaCursoIMSS> listaCumplimientoDocumentosOrdenada = listaCumplimientoDocumentos.stream().sorted(DtoCumplimientoCartaResponsivaCursoIMSS::compareTo).collect(Collectors.toList());
                       
            return ResultadoEJB.crearCorrecto(listaCumplimientoDocumentosOrdenada, "Cumplimiento de carga de documentos de seguimientos de vinculación por programa educativo, de la generación y nivel seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el cumplimiento de carga de documentos de seguimientos de vinculación por programa educativo, de la generación y nivel seleccionado. (EjbReportesCartaResponsivaCursoIMSS.getCumplimientoDocumentoPE)", e, null);
        }
    }
    
    /**
     * Permite verificar el evento de vinculación de la generación, nivel educativo y documento
     * @param generacion
     * @param nivel
     * @param documento
     * @return Resultado del proceso.
     */
    public ResultadoEJB<EventoVinculacion> buscarEventoSeleccionado(Generaciones generacion,  ProgramasEducativosNiveles nivel, Integer documento){
        try{
            EventoVinculacion eventoEstadia = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.documentoProceso.documento.documento=:documento", EventoVinculacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .setParameter("documento", documento)
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(eventoEstadia, "Evento encontrado de la generación, nivel educativo y documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe el evento de la generación, nivel educativo y documento (EjbReportesCartaResponsivaCursoIMSS.buscarEventoSeleccionado).", e, EventoVinculacion.class);
        }
    }
    
     /**
     * Permite obtener el reporte de porcentajes de cumplimiento y validación de documentos de seguimientos de vinculación
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteDocumentosVinculacion>> getReporteDocumentosVinculacion(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
           List<Short> listaProgramasNivel = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.nivelEducativo=:nivel", AreasUniversidad.class)
                    .setParameter("nivel", nivelEducativo)
                    .getResultStream()
                    .map(p -> p.getArea())
                    .collect(Collectors.toList());
            
            List<Short> listaProgramasGeneracion= em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.idPe IN :lista",  Grupo.class)
                    .setParameter("generacion",  generacion.getGeneracion())
                    .setParameter("lista",  listaProgramasNivel)
                    .getResultStream()
                    .map(p -> p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
           
            List<DtoReporteDocumentosVinculacion> listaRepDocVinculacion = new ArrayList<>();
            
                if (!listaProgramasGeneracion.isEmpty()) {
                    
                listaRepDocVinculacion = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area IN :lista", AreasUniversidad.class)
                        .setParameter("lista", listaProgramasGeneracion)
                        .getResultStream()
                        .map(e -> packReporteDocumentosPrograma(e, generacion, nivelEducativo).getValor())
                        .sorted(DtoReporteDocumentosVinculacion::compareTo)
                        .collect(Collectors.toList());
                }
            return ResultadoEJB.crearCorrecto(listaRepDocVinculacion, "Reporte de porcentajes de cumplimiento y validación de documentos de seguimientos de vinculación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el reporte de porcentajes de cumplimiento y validación de documentos de seguimientos de vinculación. (EjbReportesCartaResponsivaCursoIMSS.getReporteDocumentosVinculacion)", e, null);
        }
    }
    
      /**
     * Empaqueta el reporte por programa educativo en un DTO Wrapper
     * @param programaEducativo 
     * @param generacion
     * @param nivelEducativo
     * @return Seguimiento de actividades de estadía empaquetada
     */
    public ResultadoEJB<DtoReporteDocumentosVinculacion> packReporteDocumentosPrograma(AreasUniversidad programaEducativo, Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            evidencias = 0; evidenciasValidadas = 0; evidenciasSinValidar = 0;
            
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar programa educativo nulo.", DtoReporteDocumentosVinculacion.class);
            if(programaEducativo.getArea()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar programa educativo con clave nula.", DtoReporteDocumentosVinculacion.class);

            AreasUniversidad programaBD = em.find(AreasUniversidad.class, programaEducativo.getArea());
            if(programaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar programa educativo no registrado previamente en base de datos.", DtoReporteDocumentosVinculacion.class);
            
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            
            List<Short> tipos = new ArrayList<>();
            tipos.add((short)1);
            tipos.add((short)4);
            
            List<Estudiante> listaActivos = em.createQuery("SELECT e FROM Estudiante e WHERE e.grupo.idPe=:programa AND e.grupo.grado IN :grados AND e.grupo.generacion=:generacion AND e.tipoEstudiante.idTipoEstudiante IN :tipos", Estudiante.class)
                    .setParameter("programa", programaBD.getArea())   
                    .setParameter("grados", grados)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("tipos", tipos)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<SeguimientoVinculacionEstudiante> listaSeguimiento = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s INNER JOIN s.estudiante e INNER JOIN e.grupo g WHERE g.generacion=:generacion AND s.nivel=:nivel AND g.idPe=:programaEducativo", SeguimientoVinculacionEstudiante.class)
                    .setParameter("programaEducativo", programaBD.getArea()) 
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            listaSeguimiento.forEach(seguimiento -> {
                List<DocumentoSeguimientoVinculacion> listaDocumentosVinculacion = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento", seguimiento.getSeguimientoVinculacion())   
                    .getResultStream()
                    .collect(Collectors.toList());
                
                
                if(listaDocumentosVinculacion.size()== 2){
                        evidencias = 1 + evidencias;
                }
                if(listaDocumentosVinculacion.stream().filter(p->p.getValidado()).collect(Collectors.toList()).size() == 2){
                        evidenciasValidadas = 1 + evidenciasValidadas;
                }
                if(listaDocumentosVinculacion.stream().filter(p->!p.getValidado()).collect(Collectors.toList()).size() == 2){
                        evidenciasSinValidar = 1 + evidenciasSinValidar;
                }
            });
            
            Integer estudiantesSinEvidencia = listaActivos.size() - evidencias;
            
            Double porcentajeCumplimiento = (double) evidencias/listaActivos.size() * 100;
            
            Double porcentajeValidacion = (double) evidenciasValidadas/listaActivos.size() * 100;
            
            DtoReporteDocumentosVinculacion  dtoReporteDocumentosVinculacion = new DtoReporteDocumentosVinculacion(programaBD, listaActivos.size(), evidencias, estudiantesSinEvidencia, evidenciasValidadas, evidenciasSinValidar, porcentajeCumplimiento, porcentajeValidacion);
            
            return ResultadoEJB.crearCorrecto(dtoReporteDocumentosVinculacion, "Reporte por programa educativo empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el reporte por programa educativo. (EjbReportesCartaResponsivaCursoIMSS.packReporteDocumentosPrograma).", e, DtoReporteDocumentosVinculacion.class);
        }
    }
    
     /**
     * Permite generar el archivo de excel con los reportes de vinculación de la generación y nivel educativo seleccionado
     * @param listaCumplimientoEstudiante
     * @param listaReporteVinculacion
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReportes(List<DtoCumplimientoCartaResponsivaCursoIMSS> listaCumplimientoEstudiante, List<DtoReporteDocumentosVinculacion> listaReporteVinculacion, Generaciones generacion, ProgramasEducativosNiveles nivelEducativo) throws Throwable {
        String gen = generacion.getInicio()+ "-" + generacion.getFin();
        String nivel= "";
        
        switch (nivelEducativo.getNivel()) {
                case "TSU":
                    nivel = "TSU";
                    break;
                case "5A":
                    nivel = "ING";
                    break;
                case "5B":
                    nivel = "LIC";
                    break;
                case "5B3":
                    nivel = "IP";
                    break;
                default:
                    break;
        }
        
        String rutaPlantilla = "C:\\archivos\\cartaResponsivaCursoIMSS\\reportesVinculacion.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesCartaResponsivaCursoIMSS(gen,nivel);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_VINCULACION);
        
        Map beans = new HashMap();
        beans.put("cumpDoc", listaCumplimientoEstudiante);
        beans.put("cumpVal", listaReporteVinculacion);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
}
