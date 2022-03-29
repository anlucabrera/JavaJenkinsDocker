/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCumplimientoCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
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
    public static final String ACTUALIZADO_ESTADIA = "seguimientoEstadia.xlsx";
    public static final String ACTUALIZADO_ESTADIA_DIRECCION = "seguimientoEstadiaAreaAcademica.xlsx";
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite obtener la lista de cumplimiento de carga de documento por documento y programa educativo
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
                        grados.add(8);
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
                       
            return ResultadoEJB.crearCorrecto(listaCumplimientoDocumentosOrdenada, "Lista de seguimiento de actividades de estadía por programa educativo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de seguimiento de actividades de estadía por programa educativo. (EjbReportesCartaResponsivaCursoIMSS.getSeguimientoActividadesEstadia)", e, null);
        }
    }
    
    /**
     * Permite verificar el event de estadía con los parametros seleccionados
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
            
            return ResultadoEJB.crearCorrecto(eventoEstadia, "Evento encontrado con los parametros seleccionados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe el evento con los parametros seleccionados (EjbReportesCartaResponsivaCursoIMSS.buscarEventoSeleccionado).", e, EventoVinculacion.class);
        }
    }
    
}
