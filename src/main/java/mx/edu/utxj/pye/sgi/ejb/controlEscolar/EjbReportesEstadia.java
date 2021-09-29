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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsigAsesorAcadEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCumplimientoEstDocEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEficienciaEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteActividadesEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoZonaInfluenciaEstIns;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoZonaInfluenciaEstPrograma;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.RelacionDocumentoEstadiaEvento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbReportesEstadia")
public class EjbReportesEstadia {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbSeguimientoEstadia ejbSeguimientoEstadia;
    @EJB EjbPacker ejbPacker;
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
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRolesReportesEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==18) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            }
            else if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==48) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            }
            else if (p.getPersonal().getCategoriaOperativa().getCategoria() == 15 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getAreaOperativa() == 10 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getCategoriaOperativa().getCategoria()== 38 || p.getPersonal().getCategoriaOperativa().getCategoria()==43) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal con rol asignado en el proceso de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbSeguimientoEstadia.validarRolesEstadia)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de seguimiento de actividades de estadía por programa educativo dependiendo del nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteActividadesEstadia>> getSeguimientoActividadesEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
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
           
            List<DtoReporteActividadesEstadia> listaSegActEstadia = new ArrayList<>();
            
                if (!listaProgramasGeneracion.isEmpty()) {
                    
                listaSegActEstadia = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area IN :lista", AreasUniversidad.class)
                        .setParameter("lista", listaProgramasGeneracion)
                        .getResultStream()
                        .map(e -> packSegActPrograma(e, eventoSeleccionado).getValor())
                        .sorted(DtoReporteActividadesEstadia::compareTo)
                        .collect(Collectors.toList());
                }
            return ResultadoEJB.crearCorrecto(listaSegActEstadia, "Lista de seguimiento de actividades de estadía por programa educativo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de seguimiento de actividades de estadía por programa educativo. (EjbReportesEstadia.getSeguimientoActividadesEstadia)", e, null);
        }
    }
    
     /**
     * Empaqueta el seguimiento de actividades de estadía en un DTO Wrapper
     * @param programaEducativo 
     * @param eventoSeleccionado
     * @return Seguimiento de actividades de estadía empaquetada
     */
    public ResultadoEJB<DtoReporteActividadesEstadia> packSegActPrograma(AreasUniversidad programaEducativo, EventoEstadia eventoSeleccionado){
        try{
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar programa educativo nulo.", DtoReporteActividadesEstadia.class);
            if(programaEducativo.getArea()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar programa educativo con clave nula.", DtoReporteActividadesEstadia.class);

            AreasUniversidad programaBD = em.find(AreasUniversidad.class, programaEducativo.getArea());
            if(programaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar programa educativo no registrado previamente en base de datos.", DtoReporteActividadesEstadia.class);
            
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            
            List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.idPe=:programaEducativo AND g.grado IN :grados AND g.generacion=:generacion", Estudiante.class)
                    .setParameter("programaEducativo", programaBD.getArea())   
                    .setParameter("grados", grados)
                    .setParameter("generacion", eventoSeleccionado.getGeneracion())
                    .getResultStream()

                    .collect(Collectors.toList());
            
            List<Estudiante> listaActivos = listaEstudiantes.stream().filter(p-> p.getTipoEstudiante().getIdTipoEstudiante()!=2 && p.getTipoEstudiante().getIdTipoEstudiante()!=3).collect(Collectors.toList());

            List<SeguimientoEstadiaEstudiante> listaSeguimiento = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s INNER JOIN s.estudiante e INNER JOIN e.grupo g WHERE s.evento=:evento AND g.idPe=:programaEducativo AND g.grado IN :grados", SeguimientoEstadiaEstudiante.class)
                    .setParameter("programaEducativo", programaBD.getArea())   
                    .setParameter("grados", grados)
                    .setParameter("evento", eventoSeleccionado)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            Integer sinAsignar = listaEstudiantes.size()- listaSeguimiento.size();
            
            Double porcentajeAsignacion;
            if(!listaSeguimiento.isEmpty()){
                Double division = (double)listaSeguimiento.size()/listaEstudiantes.size();
                porcentajeAsignacion = division*100;
            }else{
                porcentajeAsignacion = 0.0;
            }
            
            List<SeguimientoEstadiaEstudiante> listaInfoEstadia = listaSeguimiento.stream().filter(p->p.getEmpresa()!=null && p.getProyecto()!=null).collect(Collectors.toList());
            
            Integer sinInfo = listaEstudiantes.size()- listaInfoEstadia.size();
            
            Double porcentajeRegistro;
            if(!listaSeguimiento.isEmpty()){
                Double division = (double)listaInfoEstadia.size()/listaEstudiantes.size();
                porcentajeRegistro = division*100;
            }else{
                porcentajeRegistro = 0.0;
            }
            
            List<SeguimientoEstadiaEstudiante> listaValidados = listaSeguimiento.stream().filter(p->p.getValidacionCoordinador()).collect(Collectors.toList());
            
            Integer sinValidar = listaEstudiantes.size()- listaValidados.size();
            
            Double porcentajeValidacion;
            if(!listaSeguimiento.isEmpty()){
                Double division = (double)listaValidados.size()/listaEstudiantes.size();
                porcentajeValidacion = division*100;
            }else{
                porcentajeValidacion = 0.0;
            }
            
            DtoReporteActividadesEstadia  dtoReporteActividadesEstadia = new DtoReporteActividadesEstadia(programaBD, listaEstudiantes.size(), listaActivos.size(), listaSeguimiento.size(),sinAsignar, porcentajeAsignacion, listaInfoEstadia.size(), sinInfo, porcentajeRegistro, listaValidados.size(), sinValidar, porcentajeValidacion);
            
            return ResultadoEJB.crearCorrecto(dtoReporteActividadesEstadia, "Seguimiento de actividades de estadía empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el seguimiento de actividades de estadía. (EjbReportesEstadia.packSegActPrograma).", e, DtoReporteActividadesEstadia.class);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes asignados por programa educativo y asesor académico de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAsigAsesorAcadEstadia>> getAsignacionAsesorAcademicoPE(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
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
           
            List<DtoAsigAsesorAcadEstadia> listaAsigAsesorAcadPE = new ArrayList<>();
            
                if (!listaProgramasGeneracion.isEmpty()) {
                    listaProgramasGeneracion.forEach(programa -> {
                        AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, programa);
                        
                        List<Integer> grados = new ArrayList<>();
                        grados.add(6);
                        grados.add(11);
                        
                        List<SeguimientoEstadiaEstudiante> listaSeguimientosPE = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.grupo.idPe=:programa AND s.estudiante.grupo.grado IN :grados", SeguimientoEstadiaEstudiante.class)
                                .setParameter("evento", eventoSeleccionado.getEvento())
                                .setParameter("programa", programaEducativo.getArea())
                                .setParameter("grados", grados)
                                .getResultStream()
                                .collect(Collectors.toList());
                        
                        List<Integer> listaAsesorAcademicosPE = listaSeguimientosPE.stream().map(p->p.getAsesor().getAsesorEstadia()).distinct().collect(Collectors.toList());
                        
                            listaAsesorAcademicosPE.forEach(asesor -> {
                                
                                AsesorAcademicoEstadia asesorAcademicoEstadia = em.createQuery("SELECT a FROM AsesorAcademicoEstadia a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel AND a.asesorEstadia=:clave", AsesorAcademicoEstadia.class)
                                    .setParameter("generacion", eventoSeleccionado.getGeneracion())
                                    .setParameter("nivel", eventoSeleccionado.getNivel())
                                    .setParameter("clave", asesor)
                                    .getResultStream()
                                    .findFirst()
                                    .orElse(null);
                               
                                List<SeguimientoEstadiaEstudiante> listaEstudiantesAsignados = listaSeguimientosPE.stream().filter(p->p.getAsesor().getAsesorEstadia().equals(asesorAcademicoEstadia.getAsesorEstadia())).collect(Collectors.toList());
                                
                                Personal asesorAcademico = em.find(Personal.class, asesorAcademicoEstadia.getPersonal());
                                
                                DtoAsigAsesorAcadEstadia dtoAsigAsesorAcadEstadia = new DtoAsigAsesorAcadEstadia();
                                dtoAsigAsesorAcadEstadia.setProgramaEducativo(programaEducativo);
                                dtoAsigAsesorAcadEstadia.setAsesorAcademico(asesorAcademico);
                                dtoAsigAsesorAcadEstadia.setEstudiantesAsignados(listaEstudiantesAsignados.size());
                                listaAsigAsesorAcadPE.add(dtoAsigAsesorAcadEstadia);
                        });
                    });
                    
                }
            return ResultadoEJB.crearCorrecto(listaAsigAsesorAcadPE.stream().sorted(DtoAsigAsesorAcadEstadia::compareTo).collect(Collectors.toList()), "Lista de estudiantes asignados por programa educativo y asesor académico.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista estudiantes asignados por programa educativo y asesor académico. (EjbReportesEstadia.getAsignacionAsesorAcademicoPE)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de cumplimiento de carga de documento por documento y programa educativo
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCumplimientoEstDocEstadia>> getCumplimientoDocumentoPE(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            String proceso;
            if(nivelEducativo.getNivel().equals("TSU")){
                proceso="EstadiaTSU";
            }else{
                proceso="EstadiaIngLic";
            }
            
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
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
            
            List<DtoCumplimientoEstDocEstadia> listaCumplimientoDocumentos = new ArrayList<>();
            
                if (!listaProgramasGeneracion.isEmpty()) {
                    
                listaProgramasGeneracion.forEach(programa -> {
                        AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, programa);
                        
                        List<Integer> grados = new ArrayList<>();
                        grados.add(6);
                        grados.add(11);

                    List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.idPe=:programa AND g.grado IN :grados AND g.generacion=:generacion", Estudiante.class)
                            .setParameter("programa", programaEducativo.getArea())
                            .setParameter("grados", grados)
                            .setParameter("generacion", eventoSeleccionado.getGeneracion())
                            .getResultStream()
                            .collect(Collectors.toList());
                   
                    List<Estudiante> listaActivos = listaEstudiantes.stream().filter(p -> p.getTipoEstudiante().getIdTipoEstudiante() != 2 && p.getTipoEstudiante().getIdTipoEstudiante() != 3).collect(Collectors.toList());
                    
                        List<SeguimientoEstadiaEstudiante> listaSeguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.grupo.idPe=:programa", SeguimientoEstadiaEstudiante.class)
                                .setParameter("evento", eventoSeleccionado.getEvento())
                                .setParameter("programa", programa)
                                .getResultStream()
                                .collect(Collectors.toList());
                        
                        List<Integer> listaSeg = listaSeguimientoEstadia.stream().map(p->p.getSeguimiento()).collect(Collectors.toList());
                            
                        List<DocumentoProceso> listaDocumentoProceso = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso=:proceso AND d.documento.activo=:valor", DocumentoProceso.class)
                                .setParameter("proceso", proceso)
                                .setParameter("valor", Boolean.TRUE)
                                .getResultStream()
                                .collect(Collectors.toList());
                        
                            listaDocumentoProceso.forEach(documento -> {
                                RelacionDocumentoEstadiaEvento relacionDocumentoEstadiaEvento = em.find(RelacionDocumentoEstadiaEvento.class, documento.getDocumento().getDocumento());
                                
                                EventoEstadia eventoDocumento = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, relacionDocumentoEstadiaEvento.getActividad()).getValor();
                                
                                List<DocumentoSeguimientoEstadia> listaDocumento = new ArrayList<>();
                                
                                Integer conDocumento = 0, sinDocumento = 0, docValidado = 0;
                                
                                if (!listaSeguimientoEstadia.isEmpty()) {
                                    
                                    listaDocumento = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d INNER JOIN d.seguimientoEstadia s WHERE d.evento.evento=:evento AND s.seguimiento IN :lista", DocumentoSeguimientoEstadia.class)
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

                                DtoCumplimientoEstDocEstadia  dtoCumplimientoEstDocEstadia = new DtoCumplimientoEstDocEstadia();
                                dtoCumplimientoEstDocEstadia.setProgramaEducativo(programaEducativo);
                                dtoCumplimientoEstDocEstadia.setDocumentoProceso(documento);
                                dtoCumplimientoEstDocEstadia.setEstudiantesIniciaron(listaEstudiantes.size());
                                dtoCumplimientoEstDocEstadia.setEstudiantesActivos(listaActivos.size());
                                dtoCumplimientoEstDocEstadia.setConDocumento(conDocumento);
                                dtoCumplimientoEstDocEstadia.setSinDocumento(sinDocumento);
                                dtoCumplimientoEstDocEstadia.setDocumentoValidado(docValidado);
                                dtoCumplimientoEstDocEstadia.setPorcentajeCumplimiento(porcentajeCumplimiento);
                                dtoCumplimientoEstDocEstadia.setPorcentajeValidacion(procentajeValidacion);
                                listaCumplimientoDocumentos.add(dtoCumplimientoEstDocEstadia);
                        });
                    });
                }
                
                List<DtoCumplimientoEstDocEstadia> listaCumplimientoDocumentosOrdenada = listaCumplimientoDocumentos.stream().sorted(DtoCumplimientoEstDocEstadia::compareTo).collect(Collectors.toList());
                       
            return ResultadoEJB.crearCorrecto(listaCumplimientoDocumentosOrdenada, "Lista de seguimiento de actividades de estadía por programa educativo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de seguimiento de actividades de estadía por programa educativo. (EjbReportesEstadia.getSeguimientoActividadesEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eficiencia de estadía por programa educativo dependiendo del nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEficienciaEstadia>> getEficienciaEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
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
           
            List<DtoEficienciaEstadia> listaProgramasEducativos = new ArrayList<>();
            
                if (!listaProgramasGeneracion.isEmpty()) {
                    
                listaProgramasEducativos = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area IN :lista", AreasUniversidad.class)
                        .setParameter("lista", listaProgramasGeneracion)
                        .getResultStream()
                        .map(e -> packEficienciaPrograma(e, eventoSeleccionado).getValor())
                        .sorted(DtoEficienciaEstadia::compareTo)
                        .collect(Collectors.toList());
                }
            return ResultadoEJB.crearCorrecto(listaProgramasEducativos, "Lista de eficiencia de estadía por programa educativo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de de eficiencia de estadía por programa educativo. (EjbReportesEstadia.getEficienciaEstadia)", e, null);
        }
    }
    
     /**
     * Empaqueta la eficiencia de estadía en un DTO Wrapper
     * @param programaEducativo 
     * @param eventoSeleccionado
     * @return Eficiencia de estadía empaquetada
     */
    public ResultadoEJB<DtoEficienciaEstadia> packEficienciaPrograma(AreasUniversidad programaEducativo, EventoEstadia eventoSeleccionado){
        try{
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar programa educativo nulo.", DtoEficienciaEstadia.class);
            if(programaEducativo.getArea()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar programa ediucativo con clave nula.", DtoEficienciaEstadia.class);

            AreasUniversidad programaBD = em.find(AreasUniversidad.class, programaEducativo.getArea());
            if(programaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar programa ediucativo no registrado previamente en base de datos.", DtoEficienciaEstadia.class);
            
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);

//            List<Estudiante> listaEstudiantes= em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.idPe=:programaEducativo AND g.grado IN :grados", Estudiante.class)
//                        .setParameter("programaEducativo", programaBD.getArea())
//                        .setParameter("grados", grados)
//                        .getResultStream()
//                        .collect(Collectors.toList());
            
//            List<Estudiante> listaRegulares= listaEstudiantes.stream().filter(p -> p.getTipoEstudiante().getIdTipoEstudiante()==1 || p.getTipoEstudiante().getIdTipoEstudiante()==4).collect(Collectors.toList());
//            
//            List<Estudiante> listaBajas= listaEstudiantes.stream().filter(p -> p.getTipoEstudiante().getIdTipoEstudiante()==2 || p.getTipoEstudiante().getIdTipoEstudiante()==3).collect(Collectors.toList());
           
//            List<Integer> listaMatriculas = listaEstudiantes.stream().map(p -> p.getMatricula()).collect(Collectors.toList());
            
            List<SeguimientoEstadiaEstudiante> listaSeguimiento = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s INNER JOIN s.estudiante e INNER JOIN e.grupo g WHERE s.evento=:evento AND g.idPe=:programaEducativo AND g.grado IN :grados", SeguimientoEstadiaEstudiante.class)
                    .setParameter("programaEducativo", programaBD.getArea())   
                    .setParameter("grados", grados)
                    .setParameter("evento", eventoSeleccionado)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<SeguimientoEstadiaEstudiante> listaAcreditados = listaSeguimiento.stream().filter(p -> p.getPromedioAsesorInterno()>=8.0 && p.getPromedioAsesorExterno()>=8.0).collect(Collectors.toList());
            
            List<SeguimientoEstadiaEstudiante> listaNoAcreditados = listaSeguimiento.stream().filter(p -> p.getPromedioAsesorInterno()<8.0 || p.getPromedioAsesorExterno()<8.0).collect(Collectors.toList());
            
            Double eficienciaEstadia;
            if(!listaSeguimiento.isEmpty()){
                Double division = (double)listaAcreditados.size()/listaSeguimiento.size();
                eficienciaEstadia = division*100;
            }else{
                eficienciaEstadia = 0.0;
            }
            
            DtoEficienciaEstadia  dtoEficienciaEstadia = new DtoEficienciaEstadia(programaBD, listaSeguimiento.size(), listaAcreditados.size(), listaNoAcreditados.size(),eficienciaEstadia);
            
            return ResultadoEJB.crearCorrecto(dtoEficienciaEstadia, "Eficiencia de estadía empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la eficiencia de estadía. (EjbReportesEstadia.packEficienciaPrograma).", e, DtoEficienciaEstadia.class);
        }
    }
    
     /**
     * Permite obtener la lista de estudiantes con seguimiento de estadía de la generación y nivel seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoSeguimientoEstadia>> getListaEstudiantesPromedio(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
            Integer grado;
            if(nivelEducativo.getNivel().equals("TSU")){
                grado = 6;
            }else{
                grado = 11;
            }
            Grupo grupo = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.grado=:grado ORDER BY g.periodo DESC",  Grupo.class)
                    .setParameter("generacion",  generacion.getGeneracion())
                    .setParameter("grado",  grado)
                    .getResultStream().findFirst().orElse(null);
           
            List<DtoSeguimientoEstadia> seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.periodo=:periodo", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("periodo", grupo.getPeriodo())
                    .getResultStream()
                    .map(seg -> ejbSeguimientoEstadia.packSeguimiento(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoSeguimientoEstadia::compareTo)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado. (EjbReportesEstadia.getListaEstudiantesPromedio)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de zona de influencia institucional
     * @param listadoEstudiantes
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoZonaInfluenciaEstIns>> getListaZonaInfluenciaInstitucional(List<DtoSeguimientoEstadia> listadoEstudiantes){
        try{
            List<MunicipioPK> listaMunicipios = listadoEstudiantes.stream().filter(p->p.getEmpresa() != null).map(p-> p.getEmpresa().getLocalidad().getMunicipio().getMunicipioPK()).distinct().collect(Collectors.toList());
            
            List<DtoZonaInfluenciaEstIns> listaZonaInfluenciaIns = new ArrayList<>();
            
            listaMunicipios.forEach(mun -> {
                Municipio municipioBD = em.createQuery("SELECT m FROM Municipio m WHERE m.municipioPK.claveEstado=:estado AND m.municipioPK.claveMunicipio=:municipio", Municipio.class)
                    .setParameter("estado", mun.getClaveEstado())
                    .setParameter("municipio", mun.getClaveMunicipio())
                    .getResultStream().findFirst().orElse(null);
                
                Long estudiantesMunicipio = listadoEstudiantes.stream().filter(p -> p.getEmpresa() != null && p.getEmpresa().getLocalidad().getMunicipio().getMunicipioPK()== mun).count();

                Integer estudiantesColocados = (int) (long) estudiantesMunicipio;
               
                Double division = (double) estudiantesColocados / listadoEstudiantes.size();
                Double porcentajeEstudiantes = division * 100;

                DtoZonaInfluenciaEstIns dtoZonaInfluenciaEstIns = new DtoZonaInfluenciaEstIns(municipioBD, estudiantesColocados, porcentajeEstudiantes);
                listaZonaInfluenciaIns.add(dtoZonaInfluenciaEstIns);
            });
            
            List<DtoZonaInfluenciaEstIns> listaZonaInfluenciaInsOrdenada = listaZonaInfluenciaIns.stream().sorted(DtoZonaInfluenciaEstIns::compareTo).collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaZonaInfluenciaInsOrdenada, "Lista de zona de influencia institucional.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de zona de influencia institucional. (EjbReportesEstadia.getListaZonaInfluenciaInstitucional)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de zona de influencia por programa educativo
     * @param listadoEstudiantes
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoZonaInfluenciaEstPrograma>> getListaZonaInfluenciaProgramaEducativo(List<DtoSeguimientoEstadia> listadoEstudiantes){
        try{
            List<AreasUniversidad> listaProgramasEducativos = listadoEstudiantes.stream().map(p-> p.getDtoEstudiante().getProgramaEducativo()).distinct().collect(Collectors.toList());
            
            List<DtoZonaInfluenciaEstPrograma> listaZonaInfluenciaPrograma = new ArrayList<>();
            
            listaProgramasEducativos.forEach(programa -> {
                
            AreasUniversidad programaBD = em.find(AreasUniversidad.class, programa.getArea());
            
            List<MunicipioPK> listaMunicipios = listadoEstudiantes.stream().filter(p->p.getDtoEstudiante().getProgramaEducativo()==programa && p.getEmpresa() != null).map(p-> p.getEmpresa().getLocalidad().getMunicipio().getMunicipioPK()).distinct().collect(Collectors.toList());
            
            listaMunicipios.forEach(mun -> {
                Municipio municipioBD = em.createQuery("SELECT m FROM Municipio m WHERE m.municipioPK.claveEstado=:estado AND m.municipioPK.claveMunicipio=:municipio", Municipio.class)
                    .setParameter("estado", mun.getClaveEstado())
                    .setParameter("municipio", mun.getClaveMunicipio())
                    .getResultStream().findFirst().orElse(null);
                
                Long estudiantesMunicipio = listadoEstudiantes.stream().filter(p -> p.getSeguimientoEstadiaEstudiante().getEstudiante().getGrupo().getIdPe() == programaBD.getArea() && p.getEmpresa() != null && p.getEmpresa().getLocalidad().getMunicipio().getMunicipioPK()== mun).count();

                Integer estudiantesColocados = (int) (long) estudiantesMunicipio;

                Double division = (double) estudiantesColocados / listadoEstudiantes.size();
                Double porcentajeEstudiantes = division * 100;

                DtoZonaInfluenciaEstPrograma dtoZonaInfluenciaEstPrograma = new DtoZonaInfluenciaEstPrograma(programaBD, municipioBD, estudiantesColocados, porcentajeEstudiantes);
                listaZonaInfluenciaPrograma.add(dtoZonaInfluenciaEstPrograma);
            });
            
            });
            
            List<DtoZonaInfluenciaEstPrograma> listaZonaInfluenciaProgramaOrdenada = listaZonaInfluenciaPrograma.stream().sorted(DtoZonaInfluenciaEstPrograma::compareTo).collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaZonaInfluenciaProgramaOrdenada, "Lista de zona de influencia por programa educativo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de zona de influencia por programa educativo. (EjbReportesEstadia.getListaZonaInfluenciaProgramaEducativo)", e, null);
        }
    }
    
    /* APARTADOS PARA GENERAR EL ARCHIVO DE EXCEL CON LOS REPORTES */
    
     /**
     * Permite generar el archivo de excel con los reportes de estadía de la generación y nivel educativo seleccionado
     * @param listaSegActEstadia
     * @param listaAsigAsesorAcad
     * @param listaCumplimientoEstudiante
     * @param listaEficienciaEstadia 
     * @param listaEstudiantesPromedio 
     * @param listaZonaInfluenciaEstIns 
     * @param listaZonaInfluenciaEstPrograma 
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReportesEstadia(List<DtoReporteActividadesEstadia> listaSegActEstadia, List<DtoAsigAsesorAcadEstadia> listaAsigAsesorAcad, List<DtoCumplimientoEstDocEstadia> listaCumplimientoEstudiante,List<DtoEficienciaEstadia> listaEficienciaEstadia, List<DtoSeguimientoEstadia> listaEstudiantesPromedio, List<DtoZonaInfluenciaEstIns> listaZonaInfluenciaEstIns, List<DtoZonaInfluenciaEstPrograma> listaZonaInfluenciaEstPrograma, Generaciones generacion, ProgramasEducativosNiveles nivelEducativo) throws Throwable {
        String gen = generacion.getInicio()+ "-" + generacion.getFin();
        String niv = nivelEducativo.getNivel();
        String rutaPlantilla = "C:\\archivos\\seguimientoEstadia\\reportesEstadia.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesEstadia(gen,niv);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_ESTADIA);
        
        Map beans = new HashMap();
        beans.put("segActEst", listaSegActEstadia);
        beans.put("asigAsesor", listaAsigAsesorAcad);
        beans.put("cumpDoc", listaCumplimientoEstudiante);
        beans.put("efiEst", listaEficienciaEstadia);
        beans.put("estProm", listaEstudiantesPromedio);
        beans.put("zonaInfIns", listaZonaInfluenciaEstIns);
        beans.put("zonaInfProg", listaZonaInfluenciaEstPrograma);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
     /**
     * Permite generar el archivo de excel con los reportes de estadía de la generación y nivel educativo seleccionado del área académica correspondiente
     * @param listaEficienciaEstadia 
     * @param listaEstudiantesPromedio 
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReportesEstadiaAreaAcademica(List<DtoEficienciaEstadia> listaEficienciaEstadia, List<DtoSeguimientoEstadia> listaEstudiantesPromedio, Generaciones generacion, ProgramasEducativosNiveles nivelEducativo) throws Throwable {
        String gen = generacion.getInicio()+ "-" + generacion.getFin();
        String niv = nivelEducativo.getNivel();
        String rutaPlantilla = "C:\\archivos\\seguimientoEstadia\\reportesEstadiaAreaAcademica.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesEstadia(gen,niv);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_ESTADIA_DIRECCION);
        
        Map beans = new HashMap();
        beans.put("efiEst", listaEficienciaEstadia);
        beans.put("estProm", listaEstudiantesPromedio);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
}
