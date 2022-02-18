/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.Part;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNotificacionesAreas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesAreas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCe;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCeImagenes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesEnlaces;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroNotificaciones")
public class EjbRegistroNotificaciones {
    @EJB        Facade                      f;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
//    TODO: Validar roles, hacer la referencia del alcance si es general o específico
    
    /**
     * Método que permite el registro de una nueva notificación en el sistema
     * @param notificacionCE    Entidad que contiene los datos de la nueva notificación y que será persistida
     * @return Devuelve la entidad ya persistida la cual ya contiene el id único de la notificación nueva
     */
    public ResultadoEJB<NotificacionesCe> guardaNotificacion(@NonNull NotificacionesCe notificacionCE) {
        try {
            notificacionCE.setFechaRegistro(new Date());
            em.persist(notificacionCE);
            return ResultadoEJB.crearCorrecto(notificacionCE, "Notificación guardada correctamente en sistema.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.guardaNotificacion): ", e, NotificacionesCe.class);
        }
    }
    
    /**
     * Método que permite la eliminación general de una notificación.
     * Eliminando de igual manera las imágenes asignadas a la notificación almacenadas en el servidor
     * Elimina enlaces asignados
     * Elimina áreas asignadas a la notificación
     * @param notificacionCE Parámetro que contiene el identificador único de la notificación que será eliminada
     * @return Devuelve VERDADERO cuando la operación ha sido ejecutada con éxito, devuelve FALSO cuando hubo un error, ambos casos incluyen mensajes de verificación
     */
    public ResultadoEJB<Boolean> eliminarNotificacion(@NonNull NotificacionesCe notificacionCE) {
        try {
            Integer notificacion = notificacionCE.getNotificacion();
            ResultadoEJB<Boolean> resEliminarImagenes = eliminarNotificacionImagenGeneral(notificacionCE);
            if (resEliminarImagenes.getCorrecto()) {
                em.remove(notificacionCE);
                em.flush();
                NotificacionesCe notEncontrada = em.find(NotificacionesCe.class, notificacion);
                if (notEncontrada == null) {
                    return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado correctamente la notificación, incluidos imágenes, áreas asignadas y enlaces correspondientes a la notificación");
                } else {
                    return ResultadoEJB.crearErroneo(2, "No se ha podido eliminar la notificación", Boolean.TYPE);
                }
            } else {
                return ResultadoEJB.crearErroneo(3, resEliminarImagenes.getMensaje(), Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.eliminarNotificacion): ", e, Boolean.TYPE);
        }
    }

    /**
     * Método que permite la actualización de información de una notificación en específico.
     * @param notificacionCe Entidad que contiene la información actualizada y preparada para la modificación en base de datos
     * @return Devuelve la entidad actualizada y sincronizada con la base de datos.
     */
    public ResultadoEJB<NotificacionesCe> editarNotificacion(@NonNull NotificacionesCe notificacionCe){
        try {
            em.merge(notificacionCe);
            return ResultadoEJB.crearCorrecto(notificacionCe, "Se han actualizado los datos de la notificación: " + notificacionCe.getNotificacion());
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido actualizar la información de la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.editarNotificacion): ", e, NotificacionesCe.class);
        }
    }
    
    /**
     * Método que es utilizado para consultar las últimas diez notificaciones registradas, método ocupado desde que el usuario ingresa al módulo de registro de notificaciones
     * @return Lista de entidades de tipo NotificacionesCe
     */
    public ResultadoEJB<List<NotificacionesCe>> consultarNotificacionesUltimosDiez(){
        try {
            List<NotificacionesCe> listaNotificaciones = em.createQuery("SELECT n FROM NotificacionesCe n ORDER BY n.horaInicio ASC", NotificacionesCe.class)
                    .setMaxResults(10)
                    .getResultList();
            if(!listaNotificaciones.isEmpty())return ResultadoEJB.crearCorrecto(listaNotificaciones, "Listado de las últimas díez notificaciones registradas.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"Aún no se han registrado notificaciones.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las ultimas diez notificaciones registradas, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarNotificacionesUltimosDiez): ", e, null);
        }
    }
    
    public ResultadoEJB<List<NotificacionesCe>> consultarNotificacionesActivas(int clave){
        try {
//            List<NotificacionesCe> listaNotificaciones = em.createNamedQuery("NotificacionesCe.findAll")
            List<NotificacionesCe> listaNotificaciones = em.createQuery("SELECT n FROM NotificacionesCe n WHERE n.personaRegistro = :clave ORDER BY n.horaInicio ASC")
                    .setParameter("clave", clave)
//                    .setParameter("fechaF", fechaF)
//                    .setMaxResults(10)
                    .getResultList();
            if(!listaNotificaciones.isEmpty())return ResultadoEJB.crearCorrecto(listaNotificaciones, "Listado de las últimas díez notificaciones registradas.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"Aún no se han registrado notificaciones.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las ultimas diez notificaciones registradas, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarNotificacionesUltimosDiez): ", e, null);
        }
    }
    public ResultadoEJB<List<NotificacionesCe>> consultarNotificacionesAlumnos(Date fechaI, Date fechaF){
        try {
//            List<NotificacionesCe> listaNotificaciones = em.createNamedQuery("NotificacionesCe.findAll")
            List<NotificacionesCe> listaNotificaciones = em.createQuery("SELECT n FROM NotificacionesCe n WHERE n.alcance LIKE 'Todos' AND n.horaInicio BETWEEN :fechaI AND :fechaF OR n.alcance LIKE 'Alumnos' AND n.horaInicio BETWEEN :fechaI AND :fechaF ORDER BY n.horaInicio ASC")
                    .setParameter("fechaI", fechaI)
                    .setParameter("fechaF", fechaF)
//                    .setMaxResults(10)
                    .getResultList();
            if(!listaNotificaciones.isEmpty())return ResultadoEJB.crearCorrecto(listaNotificaciones, "Listado de las últimas díez notificaciones registradas.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"Aún no se han registrado notificaciones.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las ultimas diez notificaciones registradas, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarNotificacionesUltimosDiez): ", e, null);
        }
    }
    public ResultadoEJB<List<NotificacionesCe>> consultarNotificacionesTrabajador(Date fechaI, Date fechaF){
        try {
//            List<NotificacionesCe> listaNotificaciones = em.createNamedQuery("NotificacionesCe.findAll")
            List<NotificacionesCe> listaNotificaciones = em.createQuery("SELECT n FROM NotificacionesCe n WHERE n.alcance LIKE 'Personal' AND n.horaInicio BETWEEN :fechaI AND :fechaF OR n.alcance LIKE 'Todos' AND n.horaInicio BETWEEN :fechaI AND :fechaF ORDER BY n.horaInicio ASC")
                    .setParameter("fechaI", fechaI)
                    .setParameter("fechaF", fechaF)
//                    .setMaxResults(10)
                    .getResultList();
            if(!listaNotificaciones.isEmpty())return ResultadoEJB.crearCorrecto(listaNotificaciones, "Listado de las últimas díez notificaciones registradas.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"Aún no se han registrado notificaciones.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las ultimas diez notificaciones registradas, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarNotificacionesUltimosDiez): ", e, null);
        }
    }
    
    public ResultadoEJB<List<NotificacionesCe>> consultarFechasNotificaciones(){
        try {
            List<NotificacionesCe> listaNotificaciones = em.createQuery("SELECT n.horaInicio FROM NotificacionesCe n ORDER BY n.horaInicio ASC", NotificacionesCe.class)
                    .setMaxResults(10)
                    .getResultList();
            if(!listaNotificaciones.isEmpty())return ResultadoEJB.crearCorrecto(listaNotificaciones, "Listado de las últimas díez notificaciones registradas.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"Aún no se han registrado notificaciones.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las ultimas diez notificaciones registradas, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarNotificacionesUltimosDiez): ", e, null);
        }
    }
    
    /**
     * Método que permite realizar una búsqueda personalizada de las notificaciones registradas, la búsqueda es realizada con base a la fecha de registro de la notificación
     * @param fechaInicio Fecha de inicio seleccionada por el usuario desde la interfaz
     * @param fechaFin Fecha de fin seleccionada por el usuario desde la interfaz
     * @return Lista de entidades de tipo NotificacionesCe
     */
    public ResultadoEJB<List<NotificacionesCe>> consultaNotificacionesPorFechaRegistro(@NonNull Date fechaInicio, @NonNull Date fechaFin){
        try {
            List<NotificacionesCe> listaNotificaciones = em.createQuery("SELECT n FROM NotificacionesCe n WHERE n.fechaRegistro BETWEEN :fechaInicio AND :fechaFin ORDER BY n.fechaRegistro DESC", NotificacionesCe.class)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            if(!listaNotificaciones.isEmpty())return ResultadoEJB.crearCorrecto(listaNotificaciones, "Listado de notificaciones registradas en la fecha seleccionada.");
            else return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"No se ha registrado notificaciones en el periodo seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las notificaciones por fecha personalizada, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultaNotificacionesPorFechaRegistro)", e, null);
        }
    }
    
//    Administración de enlaces para la notificación
    
    /**
     * Método que permite el registro de la asignación de un enlace a una notificación.
     * @param notificacionesEnlaces Entidad que contiene los datos del nuevo enlace y que será persistido
     * @return Devuelve la entidad ya persistida la cual ya contiene el id único del enlace asignado a la notificación
     */
    public ResultadoEJB<NotificacionesEnlaces> guardaNotificacionEnlace(@NonNull NotificacionesEnlaces notificacionesEnlaces) {
        try {
            em.persist(notificacionesEnlaces);
            return ResultadoEJB.crearCorrecto(notificacionesEnlaces, "El enlace de la notificación ha sido guardado correctamente.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar el enlace que ha ingresado, favor de verificar la siguiente información. (EjbRegistroNotificaciones.guardaNotificacionEnlace): ", e, NotificacionesEnlaces.class);
        }
    }
    
    /**
     * Método que permite la eliminación del enlace de la notificación seleccionada por el usuario
     * @param notificacionEnlace Entidad que contiene el identificador único para la eliminación
     * @return Devuelve VERDADERO cuando la operación es ejecutada con éxito, Devuelve FALSO cuando no se ha podido ejecutar la operación o bien ocurre un error
     */
    public ResultadoEJB<Boolean> eliminarNotificacionEnlace(@NonNull NotificacionesEnlaces notificacionEnlace){
        try {
            Integer identificadorNotificacionEnlace = notificacionEnlace.getNotificacionEnlace();
            em.remove(notificacionEnlace);
            em.flush();
            NotificacionesCe notEnlaceEncontrado = em.find(NotificacionesCe.class, identificadorNotificacionEnlace);
            if(notEnlaceEncontrado == null){
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado correctamente el enlace de la notificación seleccionada");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se ha podido eliminar el enlace de la notificación seleccionada", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar el enlace de la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.eliminarNotificacionEnlace): ", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la actualización de información del enlace de la notificación seleccionada
     * @param notificacionEnlace Entidad que contiene la información actualizada del enlace de la notificación
     * @return Devuelve la entidad actualizada y sincronizada con la base de datos
     */
    public ResultadoEJB<NotificacionesEnlaces> editarNotificacionEnlace(@NonNull NotificacionesEnlaces notificacionEnlace){
        try {
            em.merge(notificacionEnlace);
            return ResultadoEJB.crearCorrecto(notificacionEnlace, "Se han actualizado los datos del enlace de la notificación: " + notificacionEnlace.getNotificacion().getNotificacion());
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido actualizar la información del enlace de la notificación seleccionada, favor de verificar la siguiente información. (EjbRegistroNotificaciones.editarNotificacionEnlace): ", e, NotificacionesEnlaces.class);
        }
    }
    
    /**
     * Método que permite la consulta de enlaces asignadas a una notificación específica
     * @param notificacion Entidad que contiene la información de la notificación seleccionada por el usuario
     * @return Lista de entidades de tipo NotificacionesEnlaces
     */
    public ResultadoEJB<List<NotificacionesEnlaces>> consultarEnlacesPorNotificacion(@NonNull NotificacionesCe notificacion){
        try {
            List<NotificacionesEnlaces> listaEnlaces = em.createQuery("SELECT ne FROM NotificacionesEnlaces ne WHERE ne.notificacion.notificacion = :notificacion ORDER BY ne.notificacionEnlace", NotificacionesEnlaces.class)
                    .setParameter("notificacion", notificacion.getNotificacion())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaEnlaces, "Lista de enlaces de la notificación seleccionada");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido consultar los enlaces de la notificacion seleccionada, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarEnlacesPorNotificacion): ", e, null);
        }
    }
    
//    Administración de áreas que serán asignadas a la notificación
    /**
     * Método que permite llenar en el DTO de NotificacionesAreas el listado de Áreas que serán utilizadas para la asignación con notificaciones. 
     * @return Devuelve el listado de áreas vigentes
     */
    public ResultadoEJB<List<DtoNotificacionesAreas>> consultarListadoAreasParaNotificacion(){
        try {
            List<DtoNotificacionesAreas> listaDtoNotificacionesAreas = new ArrayList<>();
            List<AreasUniversidad> listaAreasUniversidad = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente ORDER BY a.areaSuperior, a.nivelEducativo, a.nombre", AreasUniversidad.class)
                    .setParameter("vigente", "1")
                    .getResultList();
            listaAreasUniversidad.stream().forEach((a) -> {
                listaDtoNotificacionesAreas.add(new DtoNotificacionesAreas(
                        a
                ));
            });
            return ResultadoEJB.crearCorrecto(listaDtoNotificacionesAreas, "Listado de áreas universidad preparado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar el listado de áreas para la asignación de áreas con notificaciones, favor de verificar la siguiente información. (EjbRegistroNotificaciones.consultarListadoNotificacionesAreas): ", e, null);
        }
    }
    
    /**
     * Método que permite verificar si un área ya se encuentra asignada a la notificación, con el fin de evitar un error de duplicidad de información
     * @param notificacionArea  Entidad que contiene los datos del área y la notificación que serán ocupados para realizar la consulta a la base de datos
     * @return Devuelve VERDADERO cuando se encuentra registro en base de datos, Devuelve FALSO cuando ocurre un error en la consulta o bien no existe registro en base de datos
     */
    public ResultadoEJB<Boolean> verificaNotificacionArea(@NonNull NotificacionesAreas notificacionArea) {
        try {
            NotificacionesAreas na = em.createQuery("SELECT na FROM NotificacionesAreas na INNER JOIN na.notificacionesCe nce WHERE nce.notificacion = :notificacion AND na.notificacionesAreasPK.area = :area",NotificacionesAreas.class)
                    .setParameter("notificacion",notificacionArea.getNotificacionesAreasPK().getNotificacion())
                    .setParameter("area",notificacionArea.getNotificacionesAreasPK().getArea())
                    .getSingleResult();
            if(na != null) return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha encontrado un registro de la asignación de área con notificación");
            else return ResultadoEJB.crearErroneo(3, Boolean.FALSE,"No se ha encontrado registro de la asignación de área con notificación");
        } catch (NoResultException nre) {
            return ResultadoEJB.crearErroneo(2, "No se han podido recuperar los resultados en la consulta realizada en la base de datos: ", nre, Boolean.TYPE);
        } catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la verificación del área con la notificacion, favor de verificar la siguiente información. (EjbRegistroNotificaciones.verificaNotificacionArea): ", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite eliminar la asignación de un área en una notificación
     * @param notificacionArea  Contiene los parámetros necesarios para la eliminación de una asignación de área específica
     * @return Devuelve VERDADERO si la eliminación ha sido realizada con éxito, Devuelve FALSO si la verificación no es verdadera o ha surgido un error durante la operación
     */
    public ResultadoEJB<Boolean> eliminarNotificacionArea(@NonNull NotificacionesAreas notificacionArea) {
        try {
            ResultadoEJB<Boolean> resultadoVerificacion = verificaNotificacionArea(notificacionArea);
            if (resultadoVerificacion.getCorrecto()) {
                em.remove(notificacionArea);
                em.flush();
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado la asignación del área con la notificación");
            }else{
                return ResultadoEJB.crearErroneo(2, resultadoVerificacion.getMensaje(), Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la asignación del área con la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.eliminarNotificacionArea): ", e, Boolean.TYPE);
        }
    }
    
    /**
     * Guarda o elimina la asignación de un área en una notificación, mediante el método de verificación y eliminación 
     * @param notificacionArea Entidad que permitirá realizar la búsqueda y determinar si será asignada o eliminada
     * @return Devuelve VERDADERO si la asignación es eliminada o asignada, devuelve FALSO si en ambos casos no es posible realizar la operación de guardar o eliminar
     */
    public ResultadoEJB<Boolean> asignarNotificacionArea(@NonNull NotificacionesAreas notificacionArea) {
        try {
            ResultadoEJB<Boolean> resVerificacionArea = verificaNotificacionArea(notificacionArea);
            if (resVerificacionArea.getCorrecto()) {
                ResultadoEJB<Boolean> resEliminarNotificacionArea = eliminarNotificacionArea(notificacionArea);
                if(resEliminarNotificacionArea.getCorrecto()){
                    return ResultadoEJB.crearCorrecto(Boolean.TRUE, resEliminarNotificacionArea.getMensaje());
                }else{
                    return ResultadoEJB.crearErroneo(1, resEliminarNotificacionArea.getMensaje(), Boolean.TYPE);
                }
            } else {
                em.persist(notificacionArea);
                em.flush();
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha asignado correctamente el área con la notificación");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la asignación del área con la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.asignarNotificacionArea): ", Boolean.TYPE);
        }
    }
    
    
//    Administración de imagenes que serán asignadas a la notificación
    /**
     * Método que permite la asignación de una o mas imágenes en una notificación, incluye la copia del archivo en el servidor
     * @param notificacionImagen Contiene los datos necesarios para identificar quien es el usuario que sube la evidencia
     * @param archivos Contiene el listado de archivos que el usuario ha enviado desde la interfaz
     * @return Devuelve un mapa que contiene la cantidad de correctos y el numero de archivos que se han subido o guardado
     */
    public ResultadoEJB<Map.Entry<Boolean, Integer>> guardarNotificacionImagen(@NonNull NotificacionesCeImagenes notificacionImagen, @NonNull List<Part> archivos) {
        try {
            Map<Boolean, Integer> map = new HashMap<>();

            if (notificacionImagen == null || archivos == null || archivos.isEmpty()) {
                map.put(Boolean.FALSE, 0);
                return ResultadoEJB.crearErroneo(2, map.entrySet().iterator().next(), "Debe de seleccionar al menos un archivo para asignar evidencias");
            }
            final List<Boolean> res = new ArrayList<>();
            archivos.forEach((archivo) -> {
                try {
                    String rutaAbsoluta = ServicioArchivos.almacenarEvidenciaRegistroNotificacion(notificacionImagen.getNotificacion(), archivo);
                    NotificacionesCeImagenes nci = new NotificacionesCeImagenes(0, rutaAbsoluta, archivo.getContentType(), archivo.getSize());
                    nci.setNotificacion(notificacionImagen.getNotificacion());
                    em.persist(nci);
                    em.flush();
                    res.add(Boolean.TRUE);
                } catch (IOException | EvidenciaRegistroExtensionNoValidaException ex) {
                    res.add(Boolean.FALSE);
                }
            });
            
            Long correctos = res.stream().filter(r -> r).count();
            Long incorrectos = res.stream().filter(r -> !r).count();
            
            map.put(incorrectos == 0, correctos.intValue());
            return ResultadoEJB.crearCorrecto(map.entrySet().iterator().next(), "Se han agregado las imagenes que se muestran en el contador");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la imagen en la notificación seleccionada, favor de verificar la siguiente información. (EjbRegistroNotificaciones.guardarNotificacionImagen): ", e, null);
        }
    }
    
    /**
     * Método que permite la eliminación de una imagen asignada a una notificación, eliminación del servidor y base de datos
     * @param notificacionImagenCe Contiene el identificador único de la imagen que se eliminará, ruta para eliminación del servidor y id para base de datos
     * @return Devuelve VERDADERO si se ha detectado que el archivo ha sido eliminado correctamente del servidor y en base de datos, devuelve FALSE si se detecta un problema con la eliminación de ambas partes
     */
    public ResultadoEJB<Boolean> eliminarNotificacionImagen(@NonNull NotificacionesCeImagenes notificacionImagenCe) {
        try {
            if (notificacionImagenCe == null) {
                return ResultadoEJB.crearErroneo(2, "No se puede eliminar una imagen de una notificación que es nula", Boolean.TYPE);
            }
            if(!notificacionImagenCe.getRuta().equals("") || notificacionImagenCe.getRuta() != null){
                ServicioArchivos.eliminarArchivo(notificacionImagenCe.getRuta());
                em.remove(notificacionImagenCe);
                em.flush();
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado la imágen asignada a la notificación");
            }else{
                return ResultadoEJB.crearErroneo(3, "No se puede eliminar una imagen de una notificación que no contenga una ruta específica.", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la imagen de la notificación, favor de verificar la siguiente información. (EjbRegistroNotificaciones.eliminarNotificacionImagen): ", e, Boolean.TYPE);
        }
    }

    /**
     * Método que permite la eliminación directa de todas las imágenes asignadas a una notificación
     * @param notificacionCe Contiene el id único para solo eliminar las imágenes que sean pertenecientes a ella.
     * @return Devuelve VERDADERO si las evidencias fueron eliminadas correctamente, devuelve FALSE si las evidencias no fueron eliminadas correctamente
     */
    public ResultadoEJB<Boolean> eliminarNotificacionImagenGeneral(@NonNull NotificacionesCe notificacionCe){
        try {
            if (notificacionCe == null) {
                return ResultadoEJB.crearErroneo(2, "No se pueden eliminar imagenes de una notificación que es nula.", Boolean.TYPE);
            }
            NotificacionesCe nce = em.find(NotificacionesCe.class, notificacionCe.getNotificacion());
            if (!nce.getNotificacionesCeImagenesList().isEmpty()) {
                nce.getNotificacionesCeImagenesList().stream().forEach((notificacion) -> {
                    if (!notificacion.getRuta().equals("") || notificacion.getRuta() != null) {
                        ServicioArchivos.eliminarArchivo(notificacion.getRuta());
                        em.remove(notificacion);
                    }
                });
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha eliminado las imagenes asignadas a la notificación");
            } else {
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "No es necesario eliminar debido a que no hay imagenes asignadas a esta notificación.");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se han podido eliminar las imagenes del servidor, favor de verificar la siguiente información. (EjbRegistroNotificaciones.eliminarNotificacionImagenGeneral): ", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la búsqueda de imágenes asignadas a una notificación.
     * @param notificacion Entidad que contiene la información de la notificación seleccionada por el usuario
     * @return Lista de entidades de tipo NotificacionesCeImagenes
     */
    public ResultadoEJB<List<NotificacionesCeImagenes>> obtenerImagenesPorNotificacion(@NonNull NotificacionesCe notificacion){
        try {
            List<NotificacionesCeImagenes> listaImagenes = em.createQuery("SELECT ni FROM NotificacionesCeImagenes ni WHERE ni.notificacion = :notificacion",NotificacionesCeImagenes.class)
                    .setParameter("notificacion", notificacion.getNotificacion())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaImagenes, "Lista de imagenes asignadas a la notificación");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido consultar las imagenes de la notificación seleccionada, favor de verificar la siguiente información. (EjbRegistroNotificaciones.obtenerImagenesPorNotificacion)", e, null);
        }
    }
    
}
