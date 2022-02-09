/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionDocumentosProceso")
public class EjbAdministracionDocumentosProceso {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite validar si el usuario autenticado es personal con permiso para administración de documentos proceso
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRolesCatalogoDocumentosProceso(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getAreaOperativa() == 10 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getAreaOperativa() == 60 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getAreaOperativa() == 9 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal para administración del catálogo de documentos proceso.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbAdministracionDocumentosProceso.validarRolesCatalogoDocumentosProceso)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de procesos registrados
     * @param areaPersonal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getProcesos(String areaPersonal){
        try{
            List<String> listaProceso = new ArrayList<>();
            if (areaPersonal.equals("InformacionEstadistica")) {
                listaProceso.add("Admision");
                listaProceso.add("Inscripcion");
                listaProceso.add("InscripcionIngENT");
                listaProceso.add("InscripcionIngET");
                listaProceso.add("EstadiaTSU");
                listaProceso.add("EstadiaIngLic");
                listaProceso.add("TitulacionTSU");
                listaProceso.add("TitulacionIngLic");
            } else if(areaPersonal.equals("ServiciosEscolares")){
                listaProceso.add("Admision");
                listaProceso.add("Inscripcion");
                listaProceso.add("InscripcionIngENT");
                listaProceso.add("InscripcionIngET");
                listaProceso.add("EstadiaTSU");
                listaProceso.add("EstadiaIngLic");  
            }else{
                listaProceso.add("TitulacionTSU");
                listaProceso.add("TitulacionIngLic");
            }
            
            return ResultadoEJB.crearCorrecto(listaProceso, "Lista de procesos registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de procesos registrados. (EjbAdministracionDocumentosProceso.getProcesos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de procesos registrados
     * @param proceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DocumentoProceso>> getDocumentosProceso(String proceso){
        try{
            List<DocumentoProceso> listaDocumentosProceso = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso=:proceso ORDER BY d.documento.descripcion ASC",  DocumentoProceso.class)
                    .setParameter("proceso", proceso)
                    .getResultStream()
                    .collect(Collectors.toList());
          
            return ResultadoEJB.crearCorrecto(listaDocumentosProceso, "Lista de procesos registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de procesos registrados. (EjbAdministracionDocumentosProceso.getProcesos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de documentos disponibles para relacionar en un proceso
     * @param listaDocumentosProceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Documento>> getDocumentosDisponibles(List<DocumentoProceso> listaDocumentosProceso){
        try{
            List<Integer> documentosRelacionados = listaDocumentosProceso.stream().map(p->p.getDocumento().getDocumento()).collect(Collectors.toList());
            
            List<Documento> listaDocumentosDisponibles = em.createQuery("SELECT d FROM Documento d WHERE d.documento NOT IN :lista AND d.activo=:valor ORDER BY d.descripcion ASC",  Documento.class)
                    .setParameter("lista", documentosRelacionados)
                    .setParameter("valor", true)
                    .getResultStream()
                    .collect(Collectors.toList());
            
          
            return ResultadoEJB.crearCorrecto(listaDocumentosDisponibles, "Lista de documentos disponibles para relacionar en un proceso.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de documentos disponibles para relacionar en un proceso. (EjbAdministracionDocumentosProceso.getDocumentosDisponibles)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de documentos registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Documento>> getDocumentosRegistrados(){
        try{
            List<Documento> listaDocumentos = em.createQuery("SELECT d FROM Documento d",  Documento.class)
                    .getResultStream()
                    .collect(Collectors.toList());
          
            return ResultadoEJB.crearCorrecto(listaDocumentos, "Lista de documentos registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de documentos registrados. (EjbAdministracionDocumentosProceso.getDocumentosRegistrados)", e, null);
        }
    }
    
     /**
     * Permite activar o desactivar como obligatorio el documento del proceso seleccionado
     * @param documentoProceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoProceso> activarDesactivarDocumentoProcesoObligatorio(DocumentoProceso documentoProceso){
        try{
           
            Boolean valor;
            
            if(documentoProceso.getObligatorio()){
                valor = false;
            }else{
                valor = true;
            }
            
            documentoProceso.setObligatorio(valor);
            em.merge(documentoProceso);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(documentoProceso, "Se ha cambiado correctamente el status obligatorio el documento del proceso seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status obligatorio el documento del proceso seleccionado. (EjbAdministracionDocumentosProceso.activarDesactivarDocumentoProcesoObligatorio)", e, null);
        }
    }
    
    /**
     * Permite guardar el documento al proceso seleccionado
     * @param proceso
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoProceso> guardarDocumentoProceso(String proceso, Documento documento){
        try{
            
            DocumentoProceso documentoProceso = new DocumentoProceso();
            documentoProceso.setDocumento(documento);
            documentoProceso.setProceso(proceso);
            documentoProceso.setObligatorio(true);
            em.persist(documentoProceso);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(documentoProceso, "Se registró correctamente el documento al proceso seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente el documento al proceso seleccionado. (EjbAdministracionDocumentosProceso.guardarDocumentoProceso)", e, null);
        }
    }
    
    /**
     * Permite eliminar el documento del proceso seleccionado
     * @param documentoProceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarDocumentoProceso(DocumentoProceso documentoProceso){
        try{
            
            Integer delete = em.createQuery("DELETE FROM DocumentoProceso d WHERE d.documentoProceso=:documentoProceso", DocumentoProceso.class)
                .setParameter("documentoProceso", documentoProceso.getDocumentoProceso())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el documento del proceso seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el documento del proceso seleccionado. (EjbAdministracionDocumentosProceso.eliminarDocumentoProceso)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar el documento seleccionado
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<Documento> activarDesactivarDocumento(Documento documento){
        try{
           
            Boolean valor;
            
            if(documento.getActivo()){
                valor = false;
            }else{
                valor = true;
            }
            
            documento.setActivo(valor);
            em.merge(documento);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(documento, "Se ha cambiado correctamente el status del documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del documento seleccionado. (EjbAdministracionDocumentosProceso.activarDesactivarDocumento)", e, null);
        }
    }
    
    /**
     * Permite guardar el documento
     * @param documento
     * @param especificaciones
     * @param nomenclatura
     * @return Resultado del proceso
     */
    public ResultadoEJB<Documento> guardarDocumento(String documento, String especificaciones, String nomenclatura){
        try{
            
            Documento nuevoDocumento = new Documento();
            nuevoDocumento.setDescripcion(documento);
            nuevoDocumento.setEspecificaciones(especificaciones);
            nuevoDocumento.setNomenclatura(nomenclatura);
            nuevoDocumento.setActivo(true);
            em.persist(nuevoDocumento);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(nuevoDocumento, "Se registró correctamente el documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente el documento. (EjbAdministracionDocumentosProceso.guardarDocumento)", e, null);
        }
    }
    
    /**
     * Permite verificar si el documento que desea eliminar se encuentra a uno o más procesos
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> registrosDocumento(Documento documento){
        try{
            Boolean valor = true;
            
            List<DocumentoProceso> listaDocumento = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.documento.documento=:documento",  DocumentoProceso.class)
                    .setParameter("documento", documento.getDocumento())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            if(listaDocumento.isEmpty()){
                valor = false;
            }
            
            return ResultadoEJB.crearCorrecto(valor, "Se buscó correctamente el documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar correctamente el documento seleccionado. (EjbAdministracionDocumentosProceso.registrosDocumento)", e, null);
        }
    }
    
    /**
     * Permite eliminar el documento seleccionado
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarDocumento(Documento documento){
        try{
            
            Integer delete = em.createQuery("DELETE FROM Documento d WHERE d.documento=:documento", Documento.class)
                .setParameter("documento", documento.getDocumento())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el documento seleccionado. (EjbAdministracionDocumentosProceso.eliminarDocumento)", e, null);
        }
    }
    
     /**
     * Permite actualizar la información del documento correspondiente
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<Documento> actualizarDocumento(Documento documento){
        try{
            
            em.merge(documento);
            f.flush();

            return ResultadoEJB.crearCorrecto(documento, "Se ha actualizado la información del documento correspondiente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del documento correspondiente. (EjbAdministracionDocumentosProceso.actualizarDocumento)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe registro del documento seleccionado
     * @param documento
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosDocumento(Documento documento){
        try{
            Long documentoAspirante = em.createQuery("SELECT d FROM DocumentoAspiranteProceso d WHERE d.documento.documento=:documento", DocumentoAspiranteProceso.class)
                    .setParameter("documento", documento.getDocumento())
                    .getResultStream()
                    .count();
            
            Long documentoExpedienteTitulacion = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.documento.documento=:documento", DocumentoExpedienteTitulacion.class)
                    .setParameter("documento", documento.getDocumento())
                    .getResultStream()
                    .count();
            
            Long documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.documento.documento=:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("documento", documento.getDocumento())
                    .getResultStream()
                    .count();
                            
            Boolean valor;
            if(documentoAspirante>0 || documentoExpedienteTitulacion>0 || documentoSeguimientoEstadia>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registro del documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registro del documento seleccionado", e, Boolean.TYPE);
        }
    }
}
