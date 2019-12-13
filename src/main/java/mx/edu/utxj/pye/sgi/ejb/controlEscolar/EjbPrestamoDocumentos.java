/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PrestamosDocumentos;
import mx.edu.utxj.pye.sgi.enums.DocumentosEstudiante;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbPrestamoDocumentos")
public class EjbPrestamoDocumentos {
    @EJB        EjbPersonalBean             ejbPersonalBean;
    @EJB        Facade                      facade;
    @EJB        EjbPropiedades              ep;
    @EJB        Facade                      f;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Método que valida si el usuario que ha ingresado es del área de servicios escolares 
     * @param clave
     * @return 
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(@NonNull Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbPrestamoDocumentos.validarServiciosEscolares)", e, null);
        }
    }
    
    /**
     * Método ocupado en el autocomplete de la vista de préstamo de documentación que permite la búsqueda por matricula nombre y apellidos de cualquier grado, grupo, carrera ,periodo y estatus.
     * @param pista
     * @return 
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(@NonNull String pista){
        try{
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo, e.carrera", Estudiante.class)
                    .setParameter("pista", pista)
                    .getResultList();
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();

            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno() + " " + estudiante.getAspirante().getIdPersona().getApellidoMaterno() + " " + estudiante.getAspirante().getIdPersona().getNombre() + " - " + estudiante.getMatricula();
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });

            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbPrestamoDocumentos.buscarEstudiante)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de estudiante por idEstudiante
     * @param estudiante
     * @return 
     */
    public ResultadoEJB<Estudiante> buscaEstudiante(@NonNull Integer estudiante){
        try {
            return ResultadoEJB.crearCorrecto(em.find(Estudiante.class, estudiante), "Estudiante encontrado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar al estudiante (EjbPrestamoDocumentos.buscaEstudiante).", e, Estudiante.class);
        }
    }
    
//    TODO: Buscar prestamo
    
    /**
     * Método que permite la consulta de documentos entregados por el estudiante.
     * Uso: Préstamo de documentos.
     * @param estudiante
     * @return 
     */
    public ResultadoEJB<Documentosentregadosestudiante> consultarDocumentosEstudiante(@NonNull Estudiante estudiante){
        try {
            Documentosentregadosestudiante documentosentregadosestudiante = em.find(Documentosentregadosestudiante.class, estudiante.getIdEstudiante());
            return ResultadoEJB.crearCorrecto(documentosentregadosestudiante, "Documentos entregados por el esudiante.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar la lista de documentos entregados por el estudiante (EjbPrestamoDocumentos.consultarDocumentosEstudiante).", Documentosentregadosestudiante.class);
        }
    }
    
    /**
     * Método que permite la consulta de préstamos de documentos que ha tenido el estudiante
     * @param estudiante
     * @return 
     */
    public ResultadoEJB<List<PrestamosDocumentos>> consultaPrestamosPorEstudiante(@NonNull Estudiante estudiante){
        try {
            List<PrestamosDocumentos> listaPrestamoDocumentos = em.createQuery("SELECT p FROM PrestamosDocumentos p WHERE p.estudiante.idEstudiante = :estudiante ORDER BY p.fechaPrestamo DESC", PrestamosDocumentos.class)
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .getResultList();
            if(!listaPrestamoDocumentos.isEmpty())return ResultadoEJB.crearCorrecto(listaPrestamoDocumentos, "Listado de préstamo de documentos por estudiante.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"El estudiante no cuenta con ningún préstamo de documento (EjbPrestamoDocumentos.consultaPrestamosPorEstudiante).");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar la lista de documentos prestados por estudiante (EjbPrestamoDocumentos.consultaPrestamosPorEstudiante).", e, null);
        }
    }
    
    /**
     * Método que permite la consulta de préstamos de documentos por fecha de préstamo personalizada
     * @param fechaInicio
     * @param fechaFin
     * @return 
     */
    public ResultadoEJB<List<PrestamosDocumentos>> consultaPrestamosPorFechaPrestamo(@NonNull Date fechaInicio, @NonNull Date fechaFin){
        try {
            List<PrestamosDocumentos> listaPrestamoDocumentos = em.createQuery("SELECT p FROM PrestamosDocumentos p WHERE p.fechaPrestamo BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPrestamo DESC", PrestamosDocumentos.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            if(!listaPrestamoDocumentos.isEmpty())return ResultadoEJB.crearCorrecto(listaPrestamoDocumentos, "Listado de préstamo de documentos por estudiante.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"No se ha registrado préstamo de documentos en el periodo seleccionado (EjbPrestamoDocumentos.consultaPrestamosPorFechaPrestamo).");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar la lista de documentos prestados por estudiante (EjbPrestamoDocumentos.consultaPrestamosPorFechaPrestamo).", e, null);
        }
    }
    
    /**
     * Método que permite la consulta de préstamo de documentos por fecha de préstamo personalizada y estudiante
     * @param fechaInicio
     * @param fechaFin
     * @param estudiante
     * @return 
     */
    public ResultadoEJB<List<PrestamosDocumentos>> consultaPrestamosEstudiantePorFechaPrestamo(@NonNull Estudiante estudiante, @NonNull Date fechaInicio, @NonNull Date fechaFin){
        try {
            List<PrestamosDocumentos> listaPrestamoDocumentos = em.createQuery("SELECT p FROM PrestamosDocumentos p WHERE (p.estudiante.idEstudiante = :estudiante) AND (p.fechaPrestamo BETWEEN :fechaInicio AND :fechaFin) ORDER BY p.fechaPrestamo DESC", PrestamosDocumentos.class)
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            if(!listaPrestamoDocumentos.isEmpty())return ResultadoEJB.crearCorrecto(listaPrestamoDocumentos, "Listado de préstamo de documentos por estudiante.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"No se ha registrado préstamo de documentos en el periodo seleccionado (EjbPrestamoDocumentos.consultaPrestamosPorFechaPrestamo).");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar la lista de documentos prestados por estudiante (EjbPrestamoDocumentos.consultaPrestamosPorFechaPrestamo).", e, null);
        }
    }
    
    public ResultadoEJB<List<PrestamosDocumentos>> consultaEntregasPorEstudiante(@NonNull Estudiante estudiante){
        try {
            List<PrestamosDocumentos> listaEntregaDocumentos = em.createQuery("SELECT p FROM PrestamosDocumentos p WHERE (p.estudiante.idEstudiante = :estudiante) AND (p.tipoPrestamo = :tipoPrestamo)", PrestamosDocumentos.class)
                    .setParameter("estudiante", estudiante.getIdEstudiante())
                    .setParameter("tipoPrestamo", "Entrega")
                    .getResultList();
            if(!listaEntregaDocumentos.isEmpty())return ResultadoEJB.crearCorrecto(listaEntregaDocumentos, "Listado de entregas de documentos por estudiante.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"No se ha registrado entrega de documentos del estudiante seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar la lista de documentos entregados por estudiante (EjbPrestamoDocumentos.consultaEntregasPorEstudiante).", e, null);
        }
    }
    
    /**
     * Método que permite la consulta de préstamos de documentos mostrando los últimos 10 registros
     * @return 
     */
    public ResultadoEJB<List<PrestamosDocumentos>> consultaPrestamosUltimosDiez(){
        try {
            List<PrestamosDocumentos> listaPrestamoDocumentos = em.createQuery("SELECT p FROM PrestamosDocumentos p ORDER BY p.fechaPrestamo DESC", PrestamosDocumentos.class)
                    .setMaxResults(10)
                    .getResultList();
            if(!listaPrestamoDocumentos.isEmpty())return ResultadoEJB.crearCorrecto(listaPrestamoDocumentos, "Listado de préstamo de documentos por estudiante.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"Aún no se han registrado préstamo de documentos (EjbPrestamoDocumentos.consultaPrestamosUltimosDiez).");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar la lista de documentos prestados por estudiante (EjbPrestamoDocumentos.consultaPrestamosUltimosDiez).", e, null);
        }
    }
    
    /**
     * Método que permite la verificación de la existencia del documento solicitado antes de realizar un préstamo
     * @param prestamosDocumentos
     * @return 
     */
    public ResultadoEJB<Boolean> verificaExistenciaDocumento(@NonNull PrestamosDocumentos prestamosDocumentos) {
        try {
            switch (prestamosDocumentos.getTipoDocumento()) {
                case "Acta de Nacimiento":
                    List<Documentosentregadosestudiante> entregadoActaNacimiento = em.createQuery("SELECT d FROM Documentosentregadosestudiante d WHERE d.estudiante1.idEstudiante = :estudiante", Documentosentregadosestudiante.class)
                            .setParameter("estudiante", prestamosDocumentos.getEstudiante().getIdEstudiante())
                            .getResultList();
                    if(!entregadoActaNacimiento.isEmpty()){
                        if(entregadoActaNacimiento.get(0).getActaNacimiento())return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El estudiante ha entregado el Acta de Nacimiento.");
                        else return ResultadoEJB.crearErroneo(6, Boolean.FALSE, "El estudiante no ha entregado aún acta de nacimiento (EjbPrestamoDocumentos.verificaExistenciaDocumento).");
                    }else{
                        return ResultadoEJB.crearErroneo(5, Boolean.FALSE, "El estudiante no ha entregado aún Acta de Nacimiento (EjbPrestamoDocumentos.verificaExistenciaDocumento).");
                    }
                case "Certificado de IEMS":
                    List<Documentosentregadosestudiante> entregadoCerticadoIEMS = em.createQuery("SELECT d FROM Documentosentregadosestudiante d WHERE d.estudiante1.idEstudiante = :estudiante", Documentosentregadosestudiante.class)
                            .setParameter("estudiante", prestamosDocumentos.getEstudiante().getIdEstudiante())
                            .getResultList();
                    if(!entregadoCerticadoIEMS.isEmpty()){
                        if(entregadoCerticadoIEMS.get(0).getCertificadoIems())return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El estudiante ha entregado Certificado de IEMS.");
                        else return ResultadoEJB.crearErroneo(4, Boolean.FALSE, "El estudiante no ha entregado aún Certificado IEMS (EjbPrestamoDocumentos.verificaExistenciaDocumento).");
                    }else{
                        return ResultadoEJB.crearErroneo(3, Boolean.FALSE, "El estudiante no ha entregado aún Certificado IEMS (EjbPrestamoDocumentos.verificaExistenciaDocumento).");
                    }
                default:
                    return ResultadoEJB.crearErroneo(2, Boolean.FALSE, "No se ha podido verificar la existencia del documento debido a que no hay ningun documento del tipo seleccionado (EjbPrestamoDocumentos.verificaExistenciaDocumento).");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar si el estudiante ha entregado previamente el documento en servicios escolares (EjbPrestamoDocumentos.verificaExistenciaDocumento).", e, Boolean.TYPE);
        }
    }

    /**
     * Método que permite verificar si existe una coincidencia activa del tipo de préstamo y documento ingresado por el usuario
     * @param prestamosDocumentos
     * @return 
     */
    public ResultadoEJB<Boolean> obtenerPrestamoActivo(@NonNull PrestamosDocumentos prestamosDocumentos){
        try {
            PrestamosDocumentos pd = em.createQuery("SELECT p FROM PrestamosDocumentos p WHERE p.estudiante.idEstudiante = :estudiante AND p.tipoPrestamo = :tipoPrestamo AND p.tipoDocumento = :tipoDocumento AND p.entregadoEstudiante = :entregadoEstudiante", PrestamosDocumentos.class)
                    .setParameter("estudiante", prestamosDocumentos.getEstudiante().getIdEstudiante())
                    .setParameter("tipoPrestamo", prestamosDocumentos.getTipoPrestamo())
                    .setParameter("tipoDocumento", prestamosDocumentos.getTipoDocumento())
                    .setParameter("entregadoEstudiante", false)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(pd == null)return ResultadoEJB.crearErroneo(2, Boolean.FALSE,"No se ha encontrado ningúna coincidencia activa");
            else return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha encontrado una coincidencia, debido a esto no es posible realizar el préstamo del documento seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido verificar el estatus del préstamo de documento en específico (EjbPrestamoDocumentos.obtenerPrestamoActivo).", e,Boolean.TYPE);
        }
    }
    
    /**
     * Método que registra el préstamo de un documento de un estudiante con la validaciones correspondientes:
     * 1.- Que exista registro en la tabla de documentos entregados por el estudiante
     * 2.- Verificar la existencia del documento en servicios escolares
     * 3.- Que no haya un registro vigente de préstamo de documento
     * @param prestamoDocumento
     * @return 
     */
    public ResultadoEJB<PrestamosDocumentos> guardarPrestamoDocumento(@NonNull PrestamosDocumentos prestamoDocumento){
        try {  
            ResultadoEJB<Boolean> resVerificaExistenciaDocumento = verificaExistenciaDocumento(prestamoDocumento);
            ResultadoEJB<Boolean> resObtenerPrestamoActivo = obtenerPrestamoActivo(prestamoDocumento);
            Documentosentregadosestudiante documentoParaActualizar = em.find(Documentosentregadosestudiante.class, prestamoDocumento.getEstudiante().getIdEstudiante());       
            if(documentoParaActualizar != null){
                if(resVerificaExistenciaDocumento.getCorrecto()){
                    if(!resObtenerPrestamoActivo.getCorrecto()){
                        switch (prestamoDocumento.getTipoDocumento()) {
                            case "Acta de Nacimiento":
                                em.persist(prestamoDocumento);
                                documentoParaActualizar.setActaNacimiento(Boolean.FALSE);
                                em.merge(documentoParaActualizar);
                                return ResultadoEJB.crearCorrecto(prestamoDocumento, "Se ha guardado correctamente el préstamo del documento.");
                            case "Certificado de IEMS":
                                em.persist(prestamoDocumento);
                                documentoParaActualizar.setCertificadoIems(Boolean.FALSE);
                                em.merge(documentoParaActualizar);
                                return ResultadoEJB.crearCorrecto(prestamoDocumento, "Se ha guardado correctamente el préstamo del documento.");
                            default:
                                return ResultadoEJB.crearErroneo(5, null, "No se ha podido guardar el documento del tipo seleccionado (EjbPrestamoDocumentos.guardarPrestamoDocumento).");
                        }
                    }else{
                        return ResultadoEJB.crearErroneo(4, null, resObtenerPrestamoActivo.getMensaje());
                    }
                }else {
                    return ResultadoEJB.crearErroneo(3, null, resVerificaExistenciaDocumento.getMensaje());
                }
            }else{
                return ResultadoEJB.crearErroneo(2, null, "No se ha podido realizar la búsqueda de los documentos entregados por el estudiante, por ello no es posible realizar un préstamo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el préstamo del documento (EjbPrestamoDocumentos.guardarPrestamoDocumento)", e, PrestamosDocumentos.class);
        }
    }
    
    /**
     * Método que permite la liberación del préstamo, actualizando el registro en cuestión y el estatus en la tabla documentos entregados por el estudiante, tomando la fecha del sistema en que se libera el préstamo
     * @param prestamosDocumentos
     * @return 
     */
    public ResultadoEJB<PrestamosDocumentos> liberarPrestamoDocumento(@NonNull PrestamosDocumentos prestamosDocumentos){
        try {
            Documentosentregadosestudiante documentoParaActualizar = em.find(Documentosentregadosestudiante.class, prestamosDocumentos.getEstudiante().getIdEstudiante());
            if (!prestamosDocumentos.getEntregadoEstudiante()) {
                prestamosDocumentos.setEntregadoEstudiante(Boolean.TRUE);
                prestamosDocumentos.setFechaDevolucion(new Date());
                em.merge(prestamosDocumentos);
                switch (prestamosDocumentos.getTipoDocumento()) {
                    case "Acta de Nacimiento":
                        documentoParaActualizar.setActaNacimiento(Boolean.TRUE);
                        em.merge(documentoParaActualizar);
                        return ResultadoEJB.crearCorrecto(prestamosDocumentos, "Se ha liberado correctamente el préstamo del documento. (Acta de Nacimiento).");
                    case "Certificado de IEMS":
                        documentoParaActualizar.setCertificadoIems(Boolean.TRUE);
                        em.merge(documentoParaActualizar);
                        return ResultadoEJB.crearCorrecto(prestamosDocumentos, "Se ha liberado correctamente el préstamo del documento. (Certificado de IEMS).");
                    default:
                        return ResultadoEJB.crearErroneo(3, null, "No se ha podido liberar el préstamo del documento del tipo seleccionado (EjbPrestamoDocumentos.liberarPrestamoDocumento).");
                }
            } else {
                prestamosDocumentos.setEntregadoEstudiante(Boolean.FALSE);
                prestamosDocumentos.setFechaDevolucion(null);
                em.merge(prestamosDocumentos);
                switch (prestamosDocumentos.getTipoDocumento()) {
                    case "Acta de Nacimiento":
                        documentoParaActualizar.setActaNacimiento(Boolean.FALSE);
                        em.merge(documentoParaActualizar);
                        return ResultadoEJB.crearCorrecto(prestamosDocumentos, "El préstamo del documento se ha actualizado correctamente (Acta de Nacimiento).");
                    case "Certificado de IEMS":
                        documentoParaActualizar.setCertificadoIems(Boolean.FALSE);
                        em.merge(documentoParaActualizar);
                        return ResultadoEJB.crearCorrecto(prestamosDocumentos, "El préstamo del documento se ha actualizado correctamente (Certificado de IEMS).");
                    default:
                        return ResultadoEJB.crearErroneo(3, null, "No se ha podido liberar el préstamo del documento del tipo seleccionado (EjbPrestamoDocumentos.liberarPrestamoDocumento).");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la liberación del préstamo del documento (EjbPrestamoDocumentos.liberarPrestamoDocumento)", e, PrestamosDocumentos.class);
        }
    }
    
    /**
     * Método que le permite al usuario ingresar las observaciones del préstamo del documento del estudiante.
     * @param prestamoDocumento
     * @return 
     */
    public ResultadoEJB<PrestamosDocumentos> actualizarObservacionesPrestamoDocumento(@NonNull PrestamosDocumentos prestamoDocumento){
        try {
            em.merge(prestamoDocumento);
            return ResultadoEJB.crearCorrecto(prestamoDocumento, "Se han actualzado correctamente las observaciones del prestamo del documento.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido guardar las observaciones del préstamo del documento (EjbPrestamoDocumentos.actualizarObservacionesPrestamoDocumento)", e, PrestamosDocumentos.class);
        }
    }
    
    /**
     * Método que elimina el préstamo del documento solo si no ha sido liberado previamente, actualizando el registro de documentos entregados por el estudiante.
     * @param prestamoDocumento
     * @return 
     */
    public ResultadoEJB<Boolean> eliminarPrestamoDocumento(@NonNull PrestamosDocumentos prestamoDocumento){
        try {
            Documentosentregadosestudiante documentoParaActualizar = em.find(Documentosentregadosestudiante.class, prestamoDocumento.getEstudiante().getIdEstudiante());
            if (!prestamoDocumento.getEntregadoEstudiante()) {
                em.remove(prestamoDocumento);
                switch (prestamoDocumento.getTipoDocumento()) {
                    case "Acta de Nacimiento":
                        documentoParaActualizar.setActaNacimiento(Boolean.TRUE);
                        em.merge(documentoParaActualizar);
                        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado correctamente el préstamo del documento. (Acta de Nacimiento).");
                    case "Certificado de IEMS":
                        documentoParaActualizar.setCertificadoIems(Boolean.TRUE);
                        em.merge(documentoParaActualizar);
                        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado correctamente el préstamo del documento. (Certificado de IEMS).");
                    default:
                        return ResultadoEJB.crearErroneo(3, null, "No se ha podido liberar el préstamo del documento del tipo seleccionado (EjbPrestamoDocumentos.eliminarPrestamoDocumento).");
                }
            }else{
                return ResultadoEJB.crearErroneo(2, null, "El préstamo ya ha sido liberado, favor de verificar la información");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar el préstamo seleccionado (EjbPrestamoDocumentos.eliminarPrestamoDocumento)", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite al usuario actualizar el estatus del documento entregado por parte del estudiante en caso de que no haya sido registrado con anterioridad, y poder llevar a cabo posteriormente préstamo de documentos.
     * @param estudiante
     * @param tipoDocumento
     * @return 
     */
    public ResultadoEJB<Boolean> altaDocumentoEstudiante(@NonNull Estudiante estudiante, @NonNull String tipoDocumento, @NonNull PrestamosDocumentos prestamoDocumento){
        try {
            Documentosentregadosestudiante documentoParaActualizar = em.find(Documentosentregadosestudiante.class, estudiante.getIdEstudiante());
            if(documentoParaActualizar != null){
                switch (tipoDocumento) {
                    case "Acta de Nacimiento":
                        if (documentoParaActualizar.getActaNacimiento()) {
                            return ResultadoEJB.crearErroneo(5, Boolean.FALSE,"El acta de nacimiento ya ha sido entregada previamente, no es necesario realizar un nuevo registro (Acta de Nacimiento).");
                        } else {
                            em.persist(prestamoDocumento);
                            documentoParaActualizar.setActaNacimiento(Boolean.TRUE);
                            em.merge(documentoParaActualizar);
                            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha actualizado correctamente la información. (Acta de Nacimiento).");
                        }
                    case "Certificado de IEMS":
                        if (documentoParaActualizar.getCertificadoIems()) {
                            return ResultadoEJB.crearErroneo(4, Boolean.FALSE,"El certificado de IEMS ya ha sido entregado previamente, no es necesario realizar un nuevo registro (Certificado de IEMS).");
                        } else {
                            em.persist(prestamoDocumento);
                            documentoParaActualizar.setCertificadoIems(Boolean.TRUE);
                            em.merge(documentoParaActualizar);
                            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha actualizado correctamente la información. (Certificado de IEMS).");
                        }
                    default:
                        return ResultadoEJB.crearErroneo(3, null, "No se ha podido actualizar debido a que no existe un documento del tipo seleccionado (EjbPrestamoDocumentos.altaDocumentoEstudiante).");
                }
            }else{
                return ResultadoEJB.crearErroneo(2, null, "No se ha podido realizar la búsqueda de los documentos entregados por el estudiante, por ello no es posible dar de alta documentos (EjbPrestamoDocumentos.altaDocumentoEstudiante)");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido dar del alta el documento del estudiante (EjbPrestamoDocumentos.altaDocumentoEstudiante)", e, Boolean.TYPE);
        }
    }
    
}
