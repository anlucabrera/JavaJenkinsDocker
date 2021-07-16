/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroDocumentosOficiales")
public class EjbRegistroDocumentosOficiales {
    @EJB Facade f;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite identificar a una lista de estudiantes
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(String pista){
        try{
            
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Integer> matriculas = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                   .setParameter("pista", pista)
                   .getResultStream()
                   .map(p->p.getMatricula())
                   .distinct()
                   .collect(Collectors.toList());
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            matriculas.forEach(matricula -> {
                Estudiante estudiante = em.createQuery("select e from Estudiante e WHERE e.matricula=:matricula ORDER BY e.periodo DESC", Estudiante.class)
                   .setParameter("matricula", matricula)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
                
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno()+" "+ estudiante.getAspirante().getIdPersona().getApellidoMaterno()+" "+ estudiante.getAspirante().getIdPersona().getNombre()+ " - " + estudiante.getMatricula();
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                String periodoEscolar = periodo.getMesInicio().getMes()+" - "+periodo.getMesFin().getMes()+" "+periodo.getAnio();
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete, periodoEscolar, programaEducativo);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes activos. (EjbRegistroDocumentosOficiales.buscarEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de documentos de inscripción por aspirante
     * @param aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDocumentoAspirante>> getConsultaDocumentosInscripcion(Aspirante aspirante){
        try{
            List<DtoDocumentoAspirante> listaDocumentos = new ArrayList<>();
           
                List<String> procesos = Arrays.asList("Inscripcion");
                listaDocumentos = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso IN :procesos AND d.documento.activo =:valor ORDER BY d.proceso, d.documentoProceso", DocumentoProceso.class)
                    .setParameter("procesos", procesos)
                    .setParameter("valor", true)
                    .getResultStream()
                    .map(doc -> pack(doc, aspirante).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaDocumentos, "Lista de documentos de inscripción por aspirante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de documentos de inscripción por aspirante. (EjbRegistroDocumentosOficiales.getConsultaDocumentosInscripcion)", e, null);
        }
    }
    
     /**
     * Empaqueta un documento del proceso en su DTO Wrapper
     * @param documentoProceso
     * @param aspirante
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoDocumentoAspirante> pack(DocumentoProceso documentoProceso, Aspirante aspirante){
        try{
            if(documentoProceso == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un documento nulo.", DtoDocumentoAspirante.class);
            if(documentoProceso.getDocumentoProceso()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un documento con clave nula.", DtoDocumentoAspirante.class);

            DocumentoProceso documentoProcesoBD = em.find(DocumentoProceso.class, documentoProceso.getDocumentoProceso());
            if(documentoProcesoBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un documento no registrado previamente en base de datos.", DtoDocumentoAspirante.class);

            DocumentoAspiranteProceso documentoAspirante = em.createQuery("SELECT d FROM DocumentoAspiranteProceso d WHERE d.aspirante =:aspirante AND d.documento =:documento", DocumentoAspiranteProceso.class)
                    .setParameter("aspirante", aspirante)
                    .setParameter("documento", documentoProcesoBD.getDocumento())
                    .getResultStream()
                    .findFirst()
                    .orElse(new DocumentoAspiranteProceso());
            
            PeriodosEscolares periodoEscolarBD = em.find(PeriodosEscolares.class, aspirante.getIdProcesoInscripcion().getIdPeriodo());
            String anioInscripcion = Integer.toString(periodoEscolarBD.getAnio());
            
            DtoDocumentoAspirante dto = new DtoDocumentoAspirante();
            dto.setDocumentoAspiranteProceso(documentoAspirante);
            dto.setDocumentoProceso(documentoProcesoBD);
            dto.setAnioInscripcion(anioInscripcion);
            return ResultadoEJB.crearCorrecto(dto, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbRegistroDocumentosOficiales. pack).", e, DtoDocumentoAspirante.class);
        }
    }
    
    /**
     * Valida que el usuario logueado sea un estudiante
     * @param matricula
     * @return
     */
    public ResultadoEJB<Estudiante> validaEstudiante(Integer matricula){
        try{
            Estudiante e = em.createQuery("select e from Estudiante as e where e.matricula = :matricula order by e.periodo DESC", Estudiante.class).setParameter("matricula", matricula)
                    .getResultStream().findFirst().orElse(new Estudiante());
            return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbRegistroDocumentosOficiales.validadEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la información del estudiante
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoEstudianteComplete> getInformacionEstudiante(Estudiante estudiante){
        try{
            String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno() + " " + estudiante.getAspirante().getIdPersona().getApellidoMaterno() + " " + estudiante.getAspirante().getIdPersona().getNombre() + " - " + estudiante.getMatricula();
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            String periodoEscolar = periodo.getMesInicio().getMes()+ " - " + periodo.getMesFin().getMes()+ " " + periodo.getAnio();
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
            DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete, periodoEscolar, programaEducativo);
           
            return ResultadoEJB.crearCorrecto(dtoEstudianteComplete, "Información del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información del estudiantes. (EjbRegistroDocumentosOficiales.getInformacionEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la generación del estudiante
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> getGeneracionEstudiante(Estudiante estudiante){
        try{
            Generaciones generacionBD = em.find(Generaciones.class, estudiante.getGrupo().getGeneracion());
            
            String generacion = generacionBD.getInicio()+" - "+generacionBD.getFin();
           
            return ResultadoEJB.crearCorrecto(generacion, "Generación del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la generación del estudiantes. (EjbRegistroDocumentosOficiales.getGeneracionEstudiante)", e, null);
        }
    }
    
}
