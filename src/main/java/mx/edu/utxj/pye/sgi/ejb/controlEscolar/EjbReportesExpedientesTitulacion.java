/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.bean.CrearZipPrueba;
import com.github.adminfaces.starter.infra.model.Filter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteFotografiasTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteEstadisticoTitulacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
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
@Stateless(name = "EjbReportesExpedientesTitulacion")
public class EjbReportesExpedientesTitulacion {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB EjbCarga ejbCarga;
    
    public static final String ACTUALIZADOConcentrado = "reporteEstadistico.xlsx";
    public static final String ACTUALIZADOListado = "listadoFotografia.xlsx";
    
    List<String> filesListInDir = new ArrayList<>();
    
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRolesReportesTitulacion(Integer clave){
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
            else if (p.getPersonal().getAreaOperativa() == 60 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getAreaOperativa() == 16 && p.getPersonal().getAreaSuperior() == 5 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
//            else if (p.getPersonal().getAreaOperativa() == 6 && p.getPersonal().getStatus()!='B') {
//                filtro.setEntity(p);
//                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
//            }
//            else if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getAreaOperativa() == 23 && p.getPersonal().getStatus()!='B') {
//                filtro.setEntity(p);
//                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
//            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal con acceso a reportes académicos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbReportesExpedientesTitulacion.validarRolesReportesAcademicos)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de niveles educativos de la generación seleccionada que tienen evento de integración de expediente de titulación
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> obtenerListaNivelesGeneracion(Generaciones generacion){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
            List<String> nivelesEventos = em.createQuery("SELECT e FROM EventoTitulacion e WHERE e.generacion=:generacion ORDER BY e.nivel DESC",  EventoTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream()
                    .map(p->p.getNivel())
                    .distinct()
                    .collect(Collectors.toList());
          
            nivelesEventos.forEach(nivelEvento -> {
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, nivelEvento);
                listaNiveles.add(nivel);
            });
            
            return ResultadoEJB.crearCorrecto(listaNiveles.stream().sorted(Comparator.comparing(ProgramasEducativosNiveles::getNombre)).collect(Collectors.toList()), "Lista de niveles educativos que tienen eventos de integración de expedientes de titulación se obtuvieron correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos en los que se han registrado expedientes. (EjbReportesExpedientesTitulacion.obtenerListaNivelesGeneracion)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de programas educativos de la generación y nivel seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> obtenerListaProgramasEducativos(Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            List<AreasUniversidad> listaProgramas = new ArrayList<>();
            
            List<Short> programasNivel = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.nivelEducativo.nivel=:nivel",  AreasUniversidad.class)
                    .setParameter("nivel", nivel.getNivel())
                    .getResultStream()
                    .map(p->p.getArea())
                    .collect(Collectors.toList());
            
            List<Short> programasGeneracionNivel = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.idPe IN :programas",  Grupo.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("programas", programasNivel)
                    .getResultStream()
                    .map(p->p.getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
           
            programasGeneracionNivel.forEach(programaGenNiv -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, programaGenNiv);
                listaProgramas.add(programa);
            });
            
            return ResultadoEJB.crearCorrecto(listaProgramas.stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList()), "Lista de programas educativos de la generación y nivel seleccionado se obtuvieron correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos de la generación y nivel seleccionado. (EjbReportesExpedientesTitulacion.obtenerListaProgramasEducativos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de grupos grado máximo de la generación, nivel educativo y programa educativo seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Grupo>> getGruposGradoMaximoGeneracionNivel(Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            List<Integer> grados = new ArrayList<>();
            
            if(nivel.getNivel().equals("TSU")){
                grados.add(5); grados.add(6);
            }else{
                grados.add(10); grados.add(11);
            }
            
            
            Integer gradoMaximo = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.grado IN :grados ORDER BY g.grado DESC", Grupo.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .map(p->p.getGrado())
                    .findFirst().orElse(null);
            
            List<Grupo> listaGrupos = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.grado=:grado", Grupo.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("grado", gradoMaximo)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            
            return ResultadoEJB.crearCorrecto(listaGrupos, "Lista de grupos grado máximo de la generación, nivel educativo y programa educativo seleccionado se obtuvieron correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos grado máximo de la generación, nivel educativo y programa educativo seleccionado. (EjbReportesExpedientesTitulacion.getGruposGradoMaximoGeneracionNivel)", e, null);
        }
    }
    
    
     /**
     * Permite obtener el listado del reporte de carga de fotografía en expediente de titulación para la generación, nivel educativo y programa educativo seleccionado
     * @param generacion
     * @param nivel
     * @param programa
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteFotografiasTitulacion>> getReporteFotografiaTitulacionPrograma(Generaciones generacion, ProgramasEducativosNiveles nivel, AreasUniversidad programa){
        try{
            List<DtoReporteFotografiasTitulacion> listaReporteFotografia = new ArrayList<>();
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(5); tiposEstudiante.add(6); tiposEstudiante.add(7);
            
            List<Integer> grupos = getGruposGradoMaximoGeneracionNivel(generacion, nivel).getValor().stream().map(p->p.getIdGrupo()).collect(Collectors.toList());
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.generacion=:generacion AND g.idPe=:programa AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND g.idGrupo IN :grupos", Estudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("programa", programa.getArea())
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("grupos", grupos)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            estudiantes.forEach(estudiante -> {
                
                String situacionExpediente = "Sin expediente";
                String situacionFotografia = "Sin fotografía";
                String fechaFotografia = "Sin información";
                String rutaFotografia = "Sin información";
                
                ExpedienteTitulacion expedienteTitulacion = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.estudiante.matricula=:matricula AND e.evento.generacion=:generacion AND e.evento.nivel=:nivel AND e.activo=:valor ", ExpedienteTitulacion.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel()) 
                    .setParameter("valor", true)
                    .getResultStream()
                    .findFirst().orElse(null);
                
                MedioComunicacion medioComunicacion = em.find(MedioComunicacion.class, estudiante.getAspirante().getIdPersona().getIdpersona());
                
                if(expedienteTitulacion != null){
                     
                    if (expedienteTitulacion.getValidado()) {
                        situacionExpediente = String.valueOf(expedienteTitulacion.getExpediente()).concat(" - ").concat("Validado");
                    } else {
                        situacionExpediente = String.valueOf(expedienteTitulacion.getExpediente()).concat(" - ").concat("Sin validar");
                    }
                    
                    DocumentoExpedienteTitulacion documentoExpedienteTitulacion = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.expediente.expediente=:expediente AND d.ruta like concat('%',:pista,'%')", DocumentoExpedienteTitulacion.class)
                        .setParameter("expediente", expedienteTitulacion.getExpediente())
                        .setParameter("pista", "fotografia")
                        .getResultStream()
                        .findFirst().orElse(null);
                     
                    if(documentoExpedienteTitulacion != null){
                         situacionFotografia = "Cargada";
                         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                         fechaFotografia = sdf.format(documentoExpedienteTitulacion.getFechaCarga());
                         rutaFotografia = documentoExpedienteTitulacion.getRuta();
                     }
                }
                
                DtoReporteFotografiasTitulacion  dtoReporteFotografiasTitulacion = new DtoReporteFotografiasTitulacion(generacion, nivel, programa, estudiante, expedienteTitulacion, medioComunicacion, situacionExpediente, situacionFotografia, fechaFotografia, rutaFotografia);
                listaReporteFotografia.add(dtoReporteFotografiasTitulacion);
            });
            return ResultadoEJB.crearCorrecto(listaReporteFotografia.stream().sorted(DtoReporteFotografiasTitulacion::compareTo).collect(Collectors.toList()), "Listado del reporte de carga de fotografía en expediente de titulación para la generación, nivel educativo y programa educativo seleccionado se obtuvo correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el listado del reporte de carga de fotografía en expediente de titulación para la generación, nivel educativo y programa educativo seleccionado. (EjbReportesExpedientesTitulacion.getReporteFotografiaTitulacionPrograma)", e, null);
        }
    }
    
     /**
     * Permite obtener el listado del reporte de carga de fotografía en expediente de titulación para la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivel
     * @param programas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteFotografiasTitulacion>> getReporteFotografiaTitulacion(Generaciones generacion, ProgramasEducativosNiveles nivel, List<AreasUniversidad> programas){
        try{
            List<Short> programasEducativos = programas.stream().map(p->p.getArea()).collect(Collectors.toList());
            
            List<DtoReporteFotografiasTitulacion> listaReporteFotografia = new ArrayList<>();
            
            List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(5); tiposEstudiante.add(6); tiposEstudiante.add(7);
            
            List<Integer> grupos = getGruposGradoMaximoGeneracionNivel(generacion, nivel).getValor().stream().map(p->p.getIdGrupo()).collect(Collectors.toList());
            
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.generacion=:generacion AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND g.idPe IN :programas AND g.idGrupo IN :grupos", Estudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("tiposEstudiante", tiposEstudiante)
                    .setParameter("programas", programasEducativos)
                    .setParameter("grupos", grupos)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            estudiantes.forEach(estudiante -> {
                
                String situacionExpediente = "Sin expediente";
                String situacionFotografia = "Sin fotografía";
                String fechaFotografia = "Sin información";
                String rutaFotografia = "Sin información";
                
                AreasUniversidad programa = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
                
                ExpedienteTitulacion expedienteTitulacion = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.estudiante.matricula=:matricula AND e.evento.generacion=:generacion AND e.evento.nivel=:nivel AND e.activo=:valor ", ExpedienteTitulacion.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel()) 
                    .setParameter("valor", true)
                    .getResultStream()
                    .findFirst().orElse(null);
                
                MedioComunicacion medioComunicacion = em.find(MedioComunicacion.class, estudiante.getAspirante().getIdPersona().getIdpersona());
                
                if(medioComunicacion == null){
                    medioComunicacion = new MedioComunicacion();
                }
                
                if(expedienteTitulacion != null){
                    
                    if (expedienteTitulacion.getValidado()) {
                        situacionExpediente = String.valueOf(expedienteTitulacion.getExpediente()).concat(" - ").concat("Validado");
                    } else {
                        situacionExpediente = String.valueOf(expedienteTitulacion.getExpediente()).concat(" - ").concat("Sin validar");
                    }
                    
                     DocumentoExpedienteTitulacion documentoExpedienteTitulacion = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.expediente.expediente=:expediente AND d.ruta like concat('%',:pista,'%')", DocumentoExpedienteTitulacion.class)
                        .setParameter("expediente", expedienteTitulacion.getExpediente())
                        .setParameter("pista", "fotografia")
                        .getResultStream()
                        .findFirst().orElse(null);
                     
                     if(documentoExpedienteTitulacion != null){
                         situacionFotografia = "Cargada";
                         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                         fechaFotografia = sdf.format(documentoExpedienteTitulacion.getFechaCarga());
                         rutaFotografia = documentoExpedienteTitulacion.getRuta();
                     }
                
                }
                
                DtoReporteFotografiasTitulacion  dtoReporteFotografiasTitulacion = new DtoReporteFotografiasTitulacion(generacion, nivel, programa, estudiante, expedienteTitulacion, medioComunicacion, situacionExpediente, situacionFotografia, fechaFotografia, rutaFotografia);
                listaReporteFotografia.add(dtoReporteFotografiasTitulacion);
            });
            return ResultadoEJB.crearCorrecto(listaReporteFotografia.stream().sorted(DtoReporteFotografiasTitulacion::compareTo).collect(Collectors.toList()), "Listado del reporte de carga de fotografía en expediente de titulación para la generación y nivel educativo seleccionado se obtuvo correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el listado del reporte de carga de fotografía en expediente de titulación para la generación y nivel educativo seleccionado. (EjbReportesExpedientesTitulacion.getReporteFotografiaTitulacion)", e, null);
        }
    }
    
     /**
     * Permite obtener el reporte estadístico de imtegración de expedientes de titulación por generación y nivel educativo seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoReporteEstadisticoTitulacion>> getReporteEstadisticoTitulacion(Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            List<AreasUniversidad> programasEducativos = obtenerListaProgramasEducativos(generacion, nivel).getValor();
            
            List<DtoReporteEstadisticoTitulacion> listaReporteEstadistico = new ArrayList<>();
            
            programasEducativos.forEach(programa -> {
                
                List<Integer> tiposEstudiante = new ArrayList<>(); tiposEstudiante.add(5); tiposEstudiante.add(6); tiposEstudiante.add(7);
                
                List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11); 

                List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.grupo g WHERE g.generacion=:generacion AND e.tipoEstudiante.idTipoEstudiante NOT IN :tiposEstudiante AND g.idPe=:programa AND g.grado IN :grados ", Estudiante.class)
                        .setParameter("generacion", generacion.getGeneracion())
                        .setParameter("tiposEstudiante", tiposEstudiante)
                        .setParameter("programa", programa.getArea())
                        .setParameter("grados", grados)
                        .getResultStream()
                        .collect(Collectors.toList());
                
                List<Integer> matriculas = estudiantes.stream().map(p->p.getMatricula()).collect(Collectors.toList());
           
                Integer estudiantesIniciaron = (int) estudiantes.stream().count();
                
                Integer estudiantesActivos = (int) estudiantes.stream().filter(p->p.getTipoEstudiante().getIdTipoEstudiante()== 1 || p.getTipoEstudiante().getIdTipoEstudiante()== 4).count();
                
                Integer estudiantesAcreditados = (int) estudiantes.stream().filter(p->p.getTipoEstudiante().getIdTipoEstudiante()== 4).count();
                
                List<ExpedienteTitulacion> expedientesTitulacion = new ArrayList<>();
                
                expedientesTitulacion = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.evento.generacion=:generacion AND e.estudiante.matricula IN :matriculas", ExpedienteTitulacion.class)
                        .setParameter("generacion", generacion.getGeneracion())
                        .setParameter("matriculas", matriculas)
                        .getResultStream()
                        .collect(Collectors.toList());
                
                Integer estudiantesExpedienteValidado = 0;
                
                if(!expedientesTitulacion.isEmpty()){
                
                    estudiantesExpedienteValidado = (int) expedientesTitulacion.stream().filter(p->p.getValidado()).count();
                }
                
                DtoReporteEstadisticoTitulacion  dtoReporteEstadisticoTitulacion = new DtoReporteEstadisticoTitulacion(generacion, nivel, programa, estudiantesIniciaron, estudiantesActivos, estudiantesAcreditados, expedientesTitulacion.size(), estudiantesExpedienteValidado);
                        listaReporteEstadistico.add(dtoReporteEstadisticoTitulacion);
            });
            return ResultadoEJB.crearCorrecto(listaReporteEstadistico, "Reporte estadístico de imtegración de expedientes de titulación por generación y nivel educativo seleccionado se obtuvo correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el reporte estadístico de imtegración de expedientes de titulación por generación y nivel educativo seleccionado. (EjbReportesExpedientesTitulacion.getListaAprovechamientoEscolarPrograma)", e, null);
        }
    }
    
    public String getDescargarReporteEstadístico(Generaciones generacion, ProgramasEducativosNiveles nivel) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\reporteEstadistico.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADOConcentrado);
        
        Map beans = new HashMap();
        beans.put("repEst", getReporteEstadisticoTitulacion(generacion, nivel).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
    public String getDescargarReporteFotografia(Generaciones generacion, ProgramasEducativosNiveles nivel, List<AreasUniversidad> programas) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\listadoFotografia.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADOListado);
        
        Map beans = new HashMap();
        beans.put("repFot", getReporteFotografiaTitulacion(generacion, nivel, programas).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
    public String getDescargarFotografias(Generaciones generacion, ProgramasEducativosNiveles nivel) throws Throwable {
        
        String rutaGeneral = "C:\\archivos\\expedientesTitulacion\\" +generacion.getInicio()+"-"+generacion.getFin()+"\\"+nivel.getNivel();
        String rutaZipGeneral ="C:\\archivos\\expedientesTitulacion\\" +generacion.getInicio()+"-"+generacion.getFin()+"_"+nivel.getNivel()+".zip";
        
        File dir = new File(rutaGeneral);
        String zipDirName = rutaZipGeneral;

        zipDirectory(dir, zipDirName);

        return zipDirName;
    }
    
    /**
     * This method zips the directory
     *
     * @param dir
     * @param zipDirName
     */
    private void zipDirectory(File dir, String zipDirName) {
        try {
            populateFilesList(dir);
            try (FileOutputStream fos = new FileOutputStream(zipDirName)) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                for (String filePath : filesListInDir) {
                    if(filePath.contains("fotografia")){
                        //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                        ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
                        zos.putNextEntry(ze);
                        //read the file and write to ZipOutputStream
                        FileInputStream fis = new FileInputStream(filePath);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                        fis.close();
                    }
                }
                zos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     /**
     * This method populates all the files in a directory to a List
     *
     * @param dir
     * @throws IOException
     */
    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filesListInDir.add(file.getAbsolutePath());
            } else {
                populateFilesList(file);
            }
        }
    }
    
    
}
