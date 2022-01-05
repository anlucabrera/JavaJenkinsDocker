/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.google.zxing.NotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.servlet.http.Part;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.AsentamientoPK;
import mx.edu.utxj.pye.sgi.enums.SeguroFacultativoValidacion;
import mx.edu.utxj.pye.sgi.enums.SegurosFacultativosEstatus;
import mx.edu.utxj.pye.sgi.enums.converter.SeguroFacultativoValidacionConverter;
import mx.edu.utxj.pye.sgi.enums.converter.SegurosFacultativosEstatusConverter;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import nl.lcs.qrscan.core.QrPdf;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroSeguroFacultativoEstudiante")
public class EjbRegistroSeguroFacultativoEstudiante {
    @EJB                EjbValidacionRol                    ejbValidacionRol;
    @EJB                Facade                              f;
    @EJB                EjbPacker                           pack;
    @EJB                mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean                    ejbPersonalBean;
    
    private             EntityManager                       em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Valida si el usuario es indentificado como estudiante al iniciar sesión
     * Hace relación con el EJB EjbValidacionRol para evitar la duplicidad de código, se relaciona mediante este EJB para identificar el módulo en caso de error.
     * @param matricula
     * @return 
     */
    public ResultadoEJB<Estudiante> validarEstudiante(String matricula){
        try {
            return ejbValidacionRol.validarEstudiante(matricula);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbRegistroSeguroFacultativoEstudiante.validarEstudiante)", e, Estudiante.class);
        }
    }
    
    /**
     * Método que obtiene una lista de tipo DtoSegurosFacultativos
     * Así mismo, se puede mandar a llamar para consultar históricos y mostrarlos al usuario
     * 
     * Se útiliza en interfaz de usuario del estudiante o métodos específicos del administrador
     * @param estudiante Parámetro de tipo DtoEstudiante que permite la búsqueda específica de registros de un estudiante
     * @return Regresa una lista de tipo DtoSeguroFacultativo
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> buscaSegurosFacultativosRegistrados(DtoEstudiante estudiante){
        try {
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, null,"No se puede verificar un seguro activo con un empaquetado nulo");
            if(estudiante.getPersona().getNombre() == null) return ResultadoEJB.crearErroneo(3, null, "No se puede verificar un seguro activo con un estudiante nulo");
            List<DtoSeguroFacultativo> listaSegurosFacultativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e INNER JOIN FETCH e.aspirante a INNER JOIN FETCH a.idPersona per WHERE per.idpersona = :persona ORDER BY sf.fechaRegistro DESC", SegurosFacultativosEstudiante.class)
                    .setParameter("persona", estudiante.getPersona().getIdpersona())
                    .getResultStream()
                    .distinct()
                    .map(seguroFacultativo -> packSeguroFacultativo(seguroFacultativo))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(listaSegurosFacultativos, "Listado de seguros facultativos por estudiante");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudieron consultar los registros de seguros facultativos, favor de reportar con el administrador del sistema. (EjbRegistroSeguroFacultativoEstudiante.buscaSegurosFacultativosRegistrados)", e, null);
        }
    }
    
    /**
     * Empaqueta un registro de Seguro facultativo en su correspondiente DTO
     * 
     * Uso para múltiples usuarios
     * @param seguroFacultativo Registro que se empaquetará
     * @return Regresa un Dto de tipo DtoSeguroFacultativo ya empaquetado
     */
    public ResultadoEJB<DtoSeguroFacultativo> packSeguroFacultativo(SegurosFacultativosEstudiante seguroFacultativo){
        try {
            if(seguroFacultativo == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un registro de seguro facultativo nulo", DtoSeguroFacultativo.class);
            if(seguroFacultativo.getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un registro de seguro facultativo con clave nula", DtoSeguroFacultativo.class);
            
            DtoEstudiante estudiante = pack.packEstudianteGeneral(seguroFacultativo.getEstudiante()).getValor();
            AreasUniversidad areasUniversidad = em.find(AreasUniversidad.class, seguroFacultativo.getEstudiante().getCarrera());
            AsentamientoPK asentamientoPK = new AsentamientoPK(seguroFacultativo.getEstudiante().getAspirante().getDomicilio().getIdEstado(), seguroFacultativo.getEstudiante().getAspirante().getDomicilio().getIdMunicipio(), seguroFacultativo.getEstudiante().getAspirante().getDomicilio().getIdAsentamiento());
            Asentamiento asentamiento = em.find(Asentamiento.class, asentamientoPK);
            String asentamientoCompleto = asentamiento.getMunicipio1().getEstado().getNombre().concat(" ").concat(asentamiento.getMunicipio1().getNombre()).concat(" ").concat(asentamiento.getNombreAsentamiento());
            if(seguroFacultativo.getUsuarioOperacion() != null){
                PersonalActivo personalActivo = ejbPersonalBean.pack(seguroFacultativo.getUsuarioOperacion());
                DtoSeguroFacultativo dtoConUsuarioOperacion = new DtoSeguroFacultativo(seguroFacultativo, estudiante, personalActivo, areasUniversidad, asentamientoCompleto);
                return ResultadoEJB.crearCorrecto(dtoConUsuarioOperacion, "Seguro facultativo empaquetado");
            }else{
                DtoSeguroFacultativo dtoSinUsuarioOperacion = new DtoSeguroFacultativo(seguroFacultativo, estudiante, areasUniversidad, asentamientoCompleto);
                return ResultadoEJB.crearCorrecto(dtoSinUsuarioOperacion, "Seguro facultativo empaquetado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el registro de seguro facultativo (EjbRegistroSeguroFacultativoEstudiante.packSeguroFacultativo)", e, DtoSeguroFacultativo.class);
        }
    }
    
    /**
     * Método que permite la creación de un nuevo registro de Seguro Facultativo, solo lo crea para estudiantes activos
     * 
     * Se utiliza en cuenta de usuario de tipo estudiante
     * @param estudiante Parámetro de tipo DtoEstudiante que permite la búsqueda específica de registros de un estudiante
     * @return Regresa una lista de tipo DtoSeguroFacultativo
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> crearRegistroSeguroFacultativoEstudianteActivo(DtoEstudiante estudiante){
        try {
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, null,"No se puede verificar un seguro activo con una empaquetado nulo");
            if(estudiante.getPersona().getNombre() == null) return ResultadoEJB.crearErroneo(3, null, "No se puede crear un seguro activo con un estudiante nulo");
            if(!estudiante.getInscripcionActiva().getActivo()) return ResultadoEJB.crearErroneo(4, null, "No se puede crear un seguro facultativo de un estudiante que no está activo");
            
            if(estudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)2) return ResultadoEJB.crearErroneo(5, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - crearRegistroSeguroFacultativoEstudianteActivo");
            if(estudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)3) return ResultadoEJB.crearErroneo(6, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - crearRegistroSeguroFacultativoEstudianteActivo");
            if(estudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)4) return ResultadoEJB.crearErroneo(7, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - crearRegistroSeguroFacultativoEstudianteActivo");
            if(estudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)7) return ResultadoEJB.crearErroneo(8, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - crearRegistroSeguroFacultativoEstudianteActivo");
            
            List<DtoSeguroFacultativo> listaSeguroFacultativo = new ArrayList<>();    
            SegurosFacultativosEstudiante seguroFacultativoNuevo = new SegurosFacultativosEstudiante();
            seguroFacultativoNuevo.setEstudiante(estudiante.getInscripcionActiva().getInscripcion());
            seguroFacultativoNuevo.setValidacionEnfermeria(SeguroFacultativoValidacion.EN_ESPERA_DE_VALIDACION.getLabel());
            seguroFacultativoNuevo.setEstatusTarjeton(SegurosFacultativosEstatus.REGISTRADO.getLabel());
            seguroFacultativoNuevo.setEstatusComprobanteLocalizacion(SegurosFacultativosEstatus.REGISTRADO.getLabel());
            seguroFacultativoNuevo.setEstatusComprobanteVigenciaDerechos(SegurosFacultativosEstatus.REGISTRADO.getLabel());
            seguroFacultativoNuevo.setEstatusRegistro(SegurosFacultativosEstatus.REGISTRADO.getLabel());
            seguroFacultativoNuevo.setFechaRegistro(new Date());
            em.persist(seguroFacultativoNuevo);
            listaSeguroFacultativo = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE sf.fechaRegistro = :fechaRegistro AND e.idEstudiante = :estudiante", SegurosFacultativosEstudiante.class)
                   .setParameter("fechaRegistro", seguroFacultativoNuevo.getFechaRegistro())
                   .setParameter("estudiante", estudiante.getInscripcionActiva().getInscripcion().getIdEstudiante())
                   .getResultStream()
                   .distinct()
                   .map(seguroFacultativo -> packSeguroFacultativo(seguroFacultativo))
                   .filter(res -> res.getCorrecto())
                   .map(ResultadoEJB::getValor)
                   .sorted(DtoSeguroFacultativo::compareTo)
                   .collect(Collectors.toList());     
           return ResultadoEJB.crearCorrecto(listaSeguroFacultativo, "El sistema ha detectado que el estudiante es activo se ha creado un nuevo registro de seguro facultativo, favor de llenar la información complementaría");             
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la creación del seguro facultativo del estudiante, favor de reportar con el administrador del sistema. (EjbRegistroSeguroFacultativoEstudiante.crearRegistroSeguroFacultativoEstudianteActivo)", e, null);
        }
    }
    
    /**
     * Método que permite la edición de los datos del registro del seguro facultativo del estudiante
     * 
     * Es utilizado en interfaces de usuario de tipo estudiante y parcialmente en administradores
     * @param seguroFacultativo Recibe una entity de tipo seguroFacultativo la cual contiene los cambios que se llevarán a cabo
     * @return Devuelve un dato de tipo Boolean, el cual nos indicará si se actualizó o no el registro.
     */
    public ResultadoEJB<Boolean> editaRegistroSeguroFacultativo(SegurosFacultativosEstudiante seguroFacultativo){
        try {
            if(seguroFacultativo == null) return ResultadoEJB.crearErroneo(2, null, "No se puede actualzar un seguro facultativo nulo");
            if(seguroFacultativo.getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se puede actualzar un seguro facultativo con una clave nula, favor de verificar la existencia del registro");
            em.merge(seguroFacultativo);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha actualizado la información de su registro de seguro facultativo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido actualizar la información del seguro facultativo seleccionado. (EjbRegistroSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo)", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la eliminación de un registro de seguro facultativo
     * Se utliza cuando el estudiante creó por error su registro de Seguro Facultativo
     * Únicamente se puede eliminar si el registro no ha cambiado de estatus, se contempla evidencias.
     * 
     * Uso solo para administradores del sistema o áreas correspondientes
     * @param seguroFacultativoEstudiante
     * @return 
     */
    public ResultadoEJB<Boolean> eliminaRegistroSeguroFacultativo(SegurosFacultativosEstudiante seguroFacultativoEstudiante, PersonalActivo personal){
        try {
            if(seguroFacultativoEstudiante == null) return ResultadoEJB.crearErroneo(2, null, "No se puede actualzar un seguro facultativo nulo");
            if(seguroFacultativoEstudiante.getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(3, null, "No se puede actualizar un seguro facultativo con una clave nula, favor de verificar la existencia del registro");
            Integer claveComprobar = seguroFacultativoEstudiante.getSeguroFacultativoEstudiante();
            if(SeguroFacultativoValidacionConverter.of(seguroFacultativoEstudiante.getValidacionEnfermeria()).getNivel() < 1.2D){
                return ResultadoEJB.crearErroneo(4, null, "No se pudo eliminar el registro del Seguro Facultativo seleccionado, esto debido a que ya fué dado de baja o dado de alta por el área de enfermería ó servicios estudiantiles");
            }else 
//                if(seguroFacultativoEstudiante.getUsuarioOperacion() == null || seguroFacultativoEstudiante.getUsuarioOperacion().equals(personal.getPersonal().getClave()))
                {
                if(!seguroFacultativoEstudiante.getRutaTarjeton().equals("") || seguroFacultativoEstudiante.getRutaTarjeton() != null) ServicioArchivos.eliminarArchivo(seguroFacultativoEstudiante.getRutaTarjeton());
                if(!seguroFacultativoEstudiante.getRutaComprobanteLocalizacion().equals("") || seguroFacultativoEstudiante.getRutaComprobanteLocalizacion() != null) ServicioArchivos.eliminarArchivo(seguroFacultativoEstudiante.getRutaComprobanteLocalizacion());
                if(!seguroFacultativoEstudiante.getRutaComprobanteVigenciaDeDerechos().equals("") || seguroFacultativoEstudiante.getRutaComprobanteVigenciaDeDerechos() != null) ServicioArchivos.eliminarArchivo(seguroFacultativoEstudiante.getRutaComprobanteVigenciaDeDerechos());
                em.remove(seguroFacultativoEstudiante);
                SegurosFacultativosEstudiante seguroFacultativoComprobacion = em.find(SegurosFacultativosEstudiante.class, claveComprobar);
                if(seguroFacultativoComprobacion == null){
                    return ResultadoEJB.crearCorrecto(true, "Se ha eliminado correctamente el registro del seguro facultativo seleccionado.");
                }else{
                    return ResultadoEJB.crearErroneo(5, "No se ha eliminado el registro del seguro facultativo seleccionado. Favor de verificar los datos", Boolean.TYPE);
                }
            }
//            else {
//                return ResultadoEJB.crearErroneo(6, null, "A este registro le está dando seguimiento el trabajador con clave: " + seguroFacultativoEstudiante.getUsuarioOperacion());
//            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar el registro de seguro facultativo, error interno. (EjbRegistroSeguroFacultativoEstudiante.eliminaRegistroSeguroFacultativo)", e, null);
        }
    }
    
    
    /**
     * Método que permite validar si el estudiante cuenta o no con un registro activo, para determinar si se crea ó no un nuevo registro de tipo seguro facultativo
     * 
     * Uso múltiple, usuario estudiante y administradores
     * @param dtoEstudiante Permite la búsqueda mediante matricula, y sirve de apoyo para informar si el estudiante está activo
     * @return 
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> validarSeguroFacultativoActivo(DtoEstudiante dtoEstudiante){
        try {
            if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(2, null,"No se puede verificar un seguro activo con una empaquetado nulo");
            if(dtoEstudiante.getPersona().getNombre() == null) return ResultadoEJB.crearErroneo(3, null, "No se puede verificar un seguro activo con un estudiante nulo");
            if(!dtoEstudiante.getInscripcionActiva().getActivo()) return ResultadoEJB.crearErroneo(4, null, "No se puede verificar un seguro facultativo de un estudiante que no está activo");
            
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)2) return ResultadoEJB.crearErroneo(5, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarSeguroFacultativoActivo");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)3) return ResultadoEJB.crearErroneo(6, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarSeguroFacultativoActivo");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)4) return ResultadoEJB.crearErroneo(7, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarSeguroFacultativoActivo");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)7) return ResultadoEJB.crearErroneo(8, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarSeguroFacultativoActivo");
            
            List<DtoSeguroFacultativo> listaSeguroFacultativo = new ArrayList<>();
            List<SegurosFacultativosEstudiante> segurosFacultativos = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE (e.matricula = :matricula) AND (sf.validacionEnfermeria = :validacion_enfermeria OR sf.validacionEnfermeria = :validacion_enfermeria_espera)" ,SegurosFacultativosEstudiante.class)
                    .setParameter("matricula", dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())
                    .setParameter("validacion_enfermeria", SeguroFacultativoValidacion.ALTA.getLabel())
                    .setParameter("validacion_enfermeria_espera", SeguroFacultativoValidacion.EN_ESPERA_DE_VALIDACION.getLabel())
                    .getResultList();
            
            if(segurosFacultativos.isEmpty()){
                List<SegurosFacultativosEstudiante> seguroFacultativoBajaEvitarDuplicado = em.createQuery("SELECT sf FROM SegurosFacultativosEstudiante sf INNER JOIN FETCH sf.estudiante e WHERE (e.matricula = :matricula) AND (e.periodo = :periodo) AND (sf.validacionEnfermeria = :validacion_enfermeria)" ,SegurosFacultativosEstudiante.class)
                    .setParameter("matricula", dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())
                    .setParameter("periodo", dtoEstudiante.getInscripcionActiva().getInscripcion().getPeriodo())
                    .setParameter("validacion_enfermeria", SeguroFacultativoValidacion.BAJA.getLabel())
                    .getResultList();
                if(seguroFacultativoBajaEvitarDuplicado.isEmpty()){
                    return ResultadoEJB.crearErroneo(9, null, "No se ha encontrado ningún registro activo de seguro facultativo");
                }else{
                    listaSeguroFacultativo.add(packSeguroFacultativo(seguroFacultativoBajaEvitarDuplicado.get(0)).getValor());
                    return ResultadoEJB.crearCorrecto(listaSeguroFacultativo, "Se ha encontrado un registro de seguro facultativo dado de baja en el mismo cuatrimestre, no es necesario crear uno automáticamente");
                }
            }
            
            if(segurosFacultativos.size() == 1) {
                listaSeguroFacultativo.add(packSeguroFacultativo(segurosFacultativos.get(0)).getValor());
                return ResultadoEJB.crearCorrecto(listaSeguroFacultativo, "Se ha encontrado un registro de seguro facultativo, no es necesario crear uno automáticamente");
            }
            
            if(segurosFacultativos.size() > 1) return ResultadoEJB.crearErroneo(10, null, "Se han encontrado más de dos registros de seguro facultativo, favor de contactar con el administrador del sistema");
            return ResultadoEJB.crearErroneo(7, null, "Ha ocurrido un error al momento de consultar el registro de seguro facultativo y no se ha podido realizar la validación");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo validar si el seguro facultativo del estudiante es activo (EjbRegistroSeguroFacultativoEstudiante.validarSeguroFacultativoActivo)", e, null);
        }
    }
    
    /**
     * Método que permite aplicar un registro automático de tipo Seguro facultativo
     * En caso de contar con uno existente solo se consulta el activo y lo devuelve en tipo de lista
     * 
     * Uso especifico para interfaz de usuario de tipo estudiante
     * @param dtoEstudiante Permite la búsqueda mediante matricula, y sirve de apoyo para informar si el estudiante está activo
     * @return Regresa una lista de tipo DtoSeguroFacultativo con sus características correspondientes a ResultadoEJB
     */
    public ResultadoEJB<List<DtoSeguroFacultativo>> aplicarRegistroAutomaticoSeguroFacultativo(DtoEstudiante dtoEstudiante){
        try {
            if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(2, null,"No se puede verificar un seguro activo con una empaquetado nulo");    
            if(dtoEstudiante.getPersona().getNombre() == null) return ResultadoEJB.crearErroneo(3, null, "No se puede verificar un seguro activo con un estudiante nulo");    
            if(!dtoEstudiante.getInscripcionActiva().getActivo()) return ResultadoEJB.crearErroneo(4, null, "No se puede verificar un seguro facultativo de un estudiante que no está activo");
            
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)2) return ResultadoEJB.crearErroneo(5, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - aplicarRegistroAutomaticoSeguroFacultativo");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)3) return ResultadoEJB.crearErroneo(6, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - aplicarRegistroAutomaticoSeguroFacultativo");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)4) return ResultadoEJB.crearErroneo(7, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - aplicarRegistroAutomaticoSeguroFacultativo");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)7) return ResultadoEJB.crearErroneo(8, null, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - aplicarRegistroAutomaticoSeguroFacultativo");
            
            ResultadoEJB<List<DtoSeguroFacultativo>> listaDtoSeguroFacultativo = validarSeguroFacultativoActivo(dtoEstudiante);
            if(validarSeguroFacultativoActivo(dtoEstudiante).getCorrecto()){
                return ResultadoEJB.crearCorrecto(listaDtoSeguroFacultativo.getValor(), listaDtoSeguroFacultativo.getMensaje());
            }else{
                listaDtoSeguroFacultativo = crearRegistroSeguroFacultativoEstudianteActivo(dtoEstudiante);
                if(listaDtoSeguroFacultativo.getCorrecto()){
                    return ResultadoEJB.crearCorrecto(listaDtoSeguroFacultativo.getValor(), "Se ha creado un nuevo registro de seguro facultativo de manera automática, favor de llenar los datos complementarios.");
                }else{
                    return ResultadoEJB.crearErroneo(5, null, "Ha ocurrido un error al momento de realizar el registro autómatico, favor de contactar al adinistrador del sistema");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido aplicar el registro automatico, ni consultar registros previos, favor de reportar con el administrador del sistema. (EjbRegistroSeguroFacultativoEstudiante.aplicarRegistroAutomaticoSeguroFacultativo)", e, null);
        }    
    }
    
    /**
     * Método que permite la validación de la CURP del estudiante, comparando el registro que se toma del archivo de tipo Tarjetón IMSS con el registro del estudiante
     * para detectar si ambas coinciden, en caso contrario devuelve un resultado de tipo ResultadoEJB Boolean
     * @param dtoEstudiante Parámetro que contiene los datos generales del estudiante
     * @param curp Se obtiene mediante el documento PDF Tarjetón IMSSS tipo de dato String
     * @return Falso o Verdadero
     */
    public ResultadoEJB<Boolean> validarCurp(DtoEstudiante dtoEstudiante, String curp){
        try {
            if(dtoEstudiante == null) return ResultadoEJB.crearErroneo(2, Boolean.FALSE,"No se puede verificar un seguro activo con una empaquetado nulo");    
            if(dtoEstudiante.getPersona().getNombre() == null) return ResultadoEJB.crearErroneo(3, Boolean.FALSE, "No se puede verificar un seguro activo con un estudiante nulo");    
            if(!dtoEstudiante.getInscripcionActiva().getActivo()) return ResultadoEJB.crearErroneo(4, Boolean.FALSE, "No se puede verificar un seguro facultativo de un estudiante que no está activo");
            
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)2) return ResultadoEJB.crearErroneo(5, Boolean.FALSE, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarCurp");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)3) return ResultadoEJB.crearErroneo(6, Boolean.FALSE, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarCurp");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)4) return ResultadoEJB.crearErroneo(7, Boolean.FALSE, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarCurp");
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante() == (short)7) return ResultadoEJB.crearErroneo(8, Boolean.FALSE, "No se puede crear un seguro facultativo de un estudiante que está dado de baja - validarCurp");
            
            if(dtoEstudiante.getPersona().getCurp().equals(curp)){
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La CURP, coincide con el estudiante que está logueado");
            }else{
                return ResultadoEJB.crearErroneo(2, Boolean.FALSE, "La CURP no coincide con el estudiante que está logueado, se ha eliminado el archivo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido validar la CURP (EjbRegistroSeguroFacultativoEstudiante.validarCurp)", e, null);
        }
    }
    
    public ResultadoEJB<DtoSeguroFacultativo> enviarRevisionSeguroFacultativoEstudiante(@NonNull DtoSeguroFacultativo dtoSeguroFacultativo){
        try {
            if(dtoSeguroFacultativo.getSeguroFactultativo().getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se puede envíar a revisión, se ha detectado que la clave de registro facultativo es nula");
            if(SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() == 1.1D){
                return ResultadoEJB.crearErroneo(3, null, "No se pudo enviar a revisión el registro del Seguro Facultativo seleccionado, esto debido a que ya fué dado de baja por el área de enfermería ó servicios estudiantiles");
            }else{
                if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusRegistro()).getNivel() == 1.3D){
                    return ResultadoEJB.crearErroneo(4, null,"No se puede enviar a revisión el regisro del Seguro Facultativo seleccionado, esto debido a que ya se encuentra validado por el área de enfermería ó servicios estudiantes");
                }else{
                    dtoSeguroFacultativo.getSeguroFactultativo().setEstatusRegistro(SegurosFacultativosEstatus.ENVIADO_PARA_VALIDACION.getLabel());
                    if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusTarjeton()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.ENVIADO_PARA_VALIDACION.getLabel());
                    if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteLocalizacion()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteLocalizacion(SegurosFacultativosEstatus.ENVIADO_PARA_VALIDACION.getLabel());
                    if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteVigenciaDerechos()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteVigenciaDerechos(SegurosFacultativosEstatus.ENVIADO_PARA_VALIDACION.getLabel());
                    em.merge(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(dtoSeguroFacultativo, "Se ha enviado a revisión el registro de su seguro facultativo correctamente, si no es la primera revisión recuerda que las evidencias ya validadas no se tomarán en cuenta para la revisión");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido enviar a revisión su registro de seguro facultativo", e, null);
        }
    }
    
    public ResultadoEJB<DtoSeguroFacultativo> cancelarEnvioARevision(@NonNull DtoSeguroFacultativo dtoSeguroFacultativo){
        try {
            if(dtoSeguroFacultativo.getSeguroFactultativo().getSeguroFacultativoEstudiante() == null) return ResultadoEJB.crearErroneo(2, null, "No se puede cancelar el envío a revisión, se ha detectado que la clave de registro facultativo es nula");
            if(SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() == 1.1D){
                return ResultadoEJB.crearErroneo(3, null, "No se pudo cancelar el envío a revisión del registro del Seguro Facultativo seleccionado, esto debido a que ya fué dado de baja por el área de enfermería ó servicios estudiantiles");
            }else{
                if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusRegistro()).getNivel() == 1.3D){
                    return ResultadoEJB.crearErroneo(4, null,"No se pudo cancelar el envío a revisión el regisro del Seguro Facultativo seleccionado, esto debido a que ya se encuentra validado por el área de enfermería ó servicios estudiantes");
                }else{
                    dtoSeguroFacultativo.getSeguroFactultativo().setEstatusRegistro(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                    if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusTarjeton()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                    if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteLocalizacion()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteLocalizacion(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                    if(SegurosFacultativosEstatusConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusComprobanteVigenciaDerechos()).getNivel() != 1.3D) dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteVigenciaDerechos(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                    em.merge(dtoSeguroFacultativo.getSeguroFactultativo());
                    return ResultadoEJB.crearCorrecto(dtoSeguroFacultativo, "Se ha cancelado el envío a revisión del registro de su seguro facultativo correctamente");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido cancelar el envío a revisión de su registro de seguro facultativo", e, null);
        }
    }
    
    private static final Logger LOG = Logger.getLogger(EjbRegistroSeguroFacultativoEstudiante.class.getName());
    
    /**
     * Método que permite el registro de evidencia de Tarjetón del IMMS, para el complemento de información del Registro de seguro facultativo
     * Guardar el archivo en el directorio del servidor y posteriormente actualiza el registro en base de datos guardando la dirección del archivo
     * 
     * Uso especifico para interfaz de tipo usuario, uso genérico
     * @param dtoSeguroFacultativo Contiene los datos necesarios para validar y registrar la ruta del archivo
     * @param archivo Contiene el archivo a subir en el servidor
     * @return Devuelve un mapa que contiene la información necesaria para identificar si el archivo se subió ó no
     * @throws Throwable 
     */
    public ResultadoEJB<Map.Entry<Boolean, Integer>> registrarArchivoTarjetonImssSf(DtoSeguroFacultativo dtoSeguroFacultativo, Part archivo) throws Throwable{
        try {
            Map<Boolean, Integer> map = new HashMap<>();
            if (dtoSeguroFacultativo == null || archivo == null) {
                map.put(Boolean.FALSE, 0);
                return ResultadoEJB.crearErroneo(2, map.entrySet().iterator().next(), "No se ha podido subir la información debido a que el estudiante o el archivo es nulo");
            }
            String rutaValidar = "";
            if (SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() != 1.1D) {
                final List<Boolean> res = new ArrayList<>();
                try {
                    String rutaAbsoluta = ServicioArchivos.almacenarArchivoSeguroFacultativo(dtoSeguroFacultativo.getEstudiante(), archivo);
                    dtoSeguroFacultativo.getSeguroFactultativo().setRutaTarjeton(rutaAbsoluta);
                    rutaValidar = rutaAbsoluta;
                    res.add(true);
                } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                    res.add(Boolean.FALSE);
                    LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
                }
                Long correctos = res.stream().filter(r -> r).count();
                Long incorrectos = res.stream().filter(r -> !r).count();
                if (correctos == 0) {
                    dtoSeguroFacultativo.getSeguroFactultativo().setRutaTarjeton(null);
                    dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                    editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                    map.put(Boolean.FALSE, 0);
                    return ResultadoEJB.crearErroneo(3, map.entrySet().iterator().next(), "No se ha podido subir el Tarjetón IMSS, ocurrió un error al momento de almacenarlo en el servidor.");
                } else {
                    try {
                        Path path = Paths.get(rutaValidar);
                        URI uri = path.toUri();
                        QrPdf pdf = new QrPdf(Paths.get(uri));
                        String qrCode = pdf.getQRCode(1, true, true);
                        String[] parts = qrCode.split("\\|");
//                        Arrays.stream(parts).forEach(System.out::println);
                        //                Seguro social
                        String num_seg_social = parts[2].replaceAll("[^-\\d]+", "").trim();
                        //                Nombre Completo
                        String nombre_validacion = parts[3].replaceAll("Nombre: ", "").trim();
                        //                Curp - Comparar con la base de datos
                        String curp_modificado = parts[4].replaceAll("CURP: ", "").trim();
                        ResultadoEJB<Boolean> vCurp = validarCurp(dtoSeguroFacultativo.getEstudiante(), curp_modificado);
                        if (vCurp.getCorrecto()) {
                            dtoSeguroFacultativo.getSeguroFactultativo().setNss(num_seg_social);
                            dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.VALIDADO.getLabel());
                            editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                            map.put(incorrectos == 0, correctos.intValue());
                            return ResultadoEJB.crearCorrecto(map.entrySet().iterator().next(), "Se ha subido correctamente el archivo de tipo Tarjetón IMSS y se ha dato de alta el Número de Seguro Social, favor de verificarlo");
                        } else {
                            eliminarArchivoTarjetonImss(dtoSeguroFacultativo);
                            dtoSeguroFacultativo.getSeguroFactultativo().setRutaTarjeton(null);
                            dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                            editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                            map.put(Boolean.FALSE, 0);
                            return ResultadoEJB.crearErroneo(4, map.entrySet().iterator().next(), "No se ha podido subir el Tarjetón IMSS, la CURP no corresponde con el estudiante que está subiendo el archivo.");
                        }
                    } catch (NotFoundException | IOException e) {
                        eliminarArchivoTarjetonImss(dtoSeguroFacultativo);
                        dtoSeguroFacultativo.getSeguroFactultativo().setRutaTarjeton(null);
                        dtoSeguroFacultativo.getSeguroFactultativo().setEstatusTarjeton(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                        editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
                        map.put(Boolean.FALSE, 0);
                        return ResultadoEJB.crearErroneo(5, map.entrySet().iterator().next(), "No se ha podido subir el Tarjetón IMSS, el archivo pdf no es del tipo Tarjetón del IMSS.");
                    } 
                }
            } else {
                map.put(Boolean.FALSE, 0);
                return ResultadoEJB.crearErroneo(6, map.entrySet().iterator().next(), "No se ha podido subir la información debido a que la validación no está disponible para actualizar");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido subir el archivo de Tarjetón IMSS (EjbRegistroSeguroFacultativoEstudiante.registrarArchivoTarjetonImssSf)", e, null);
        }
    }
    
    /**
     * Método que permite el registro de evidencia del Comprobante de localización, para el complemento de información del Registro de seguro facultativo
     * Guardar el archivo en el directorio del servidor y posteriormente actualiza el registro en base de datos guardando la dirección del archivo
     * 
     * Uso especifico para interfaz de tipo usuario, uso genérico
     * @param dtoSeguroFacultativo Contiene los datos necesarios para validar y registrar la ruta del archivo
     * @param archivo Contiene el archivo a subir en el servidor
     * @return Devuelve un mapa que contiene la información necesaria para identificar si el archivo se subió ó no
     * @throws Throwable 
     */
    public Map.Entry<Boolean, Integer> registrarArchivoRutaComprobanteLocalizacionSf(DtoSeguroFacultativo dtoSeguroFacultativo, Part archivo) throws Throwable{
        Map<Boolean, Integer> map = new HashMap<>();
        if(dtoSeguroFacultativo == null || archivo == null){
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        if(SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() != 1.1D){
            final List<Boolean> res = new ArrayList<>();
            try {
                String rutaAbsoluta = ServicioArchivos.almacenarArchivoSeguroFacultativo(dtoSeguroFacultativo.getEstudiante(), archivo);
                dtoSeguroFacultativo.getSeguroFactultativo().setRutaComprobanteLocalizacion(rutaAbsoluta);
                res.add(true);
            } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);  
            }
            Long correctos = res.stream().filter(r -> r).count();
            Long incorrectos = res.stream().filter(r -> !r).count();
            if(correctos == 0){
                eliminarArchivoComprobanteLocalizacion(dtoSeguroFacultativo);
                dtoSeguroFacultativo.getSeguroFactultativo().setRutaComprobanteLocalizacion(null);
                dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteLocalizacion(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
            }else{
                editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
            }
            map.put(incorrectos == 0, correctos.intValue());
            return map.entrySet().iterator().next();
        }else{
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
    }
    
    /**
     * Método que permite el registro de evidencia del Comprobante de Vigencia de Derechos, para el complemento de información del Registro de seguro facultativo
     * Guardar el archivo en el directorio del servidor y posteriormente actualiza el registro en base de datos guardando la dirección del archivo
     * 
     * Uso especifico para interfaz de tipo usuario, uso genérico
     * @param dtoSeguroFacultativo Contiene los datos necesarios para validar y registrar la ruta del archivo
     * @param archivo Contiene el archivo a subir en el servidor
     * @return Devuelve un mapa que contiene la información necesaria para identificar si el archivo se subió ó no
     * @throws Throwable 
     */
    public Map.Entry<Boolean, Integer> registrarArchivoComprobanteVigenciaDerechosSf(DtoSeguroFacultativo dtoSeguroFacultativo, Part archivo) throws Throwable{
        Map<Boolean, Integer> map = new HashMap<>();
        if(dtoSeguroFacultativo == null || archivo == null){
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        if(SeguroFacultativoValidacionConverter.of(dtoSeguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() != 1.1D){
            final List<Boolean> res = new ArrayList<>();
            try {
                String rutaAbsoluta = ServicioArchivos.almacenarArchivoSeguroFacultativo(dtoSeguroFacultativo.getEstudiante(), archivo);
                dtoSeguroFacultativo.getSeguroFactultativo().setRutaComprobanteVigenciaDeDerechos(rutaAbsoluta);
                res.add(true);
            } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);  
            }
            Long correctos = res.stream().filter(r -> r).count();
            Long incorrectos = res.stream().filter(r -> !r).count();
            if(correctos == 0){
                eliminarArchivoComprobanteVigenciaDerechos(dtoSeguroFacultativo);
                dtoSeguroFacultativo.getSeguroFactultativo().setRutaComprobanteVigenciaDeDerechos(null);
                dtoSeguroFacultativo.getSeguroFactultativo().setEstatusComprobanteVigenciaDerechos(SegurosFacultativosEstatus.REGISTRADO.getLabel());
                editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
            }else{
                editaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo());
            }
            map.put(incorrectos == 0, correctos.intValue());
            return map.entrySet().iterator().next();
        }else{
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
    }
    
    /**
     * Método que permite la eliminación del archivo del Tarjetón del IMMS del registro de Seguro Facultativo
     * 
     * Uso especifico para interfaz de tipo usuario, uso genérico
     * @param seguroFacultativo Contiene la ruta del archivo a eliminar
     * @return Tipo de dato Boolean para determinar si la acción tuvo o no tuvo éxito
     */
    public Boolean eliminarArchivoTarjetonImss(DtoSeguroFacultativo seguroFacultativo) {
        if (seguroFacultativo == null) return false;
        if (SeguroFacultativoValidacionConverter.of(seguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() != 1.1D) {
            try {
                if (!seguroFacultativo.getSeguroFactultativo().getRutaTarjeton().equals("") || seguroFacultativo.getSeguroFactultativo().getRutaTarjeton() != null) {
                    ServicioArchivos.eliminarArchivo(seguroFacultativo.getSeguroFactultativo().getRutaTarjeton());
                }
                seguroFacultativo.getSeguroFactultativo().setRutaTarjeton(null);
                em.merge(seguroFacultativo.getSeguroFactultativo());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + seguroFacultativo.getSeguroFactultativo().getRutaTarjeton(), e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Método que permite la eliminación del archivo del Comprobante de Localización del registro de Seguro Facultativo
     * 
     * Uso especifico para interfaz de tipo usuario, uso genérico
     * @param seguroFacultativo Contiene la ruta del archivo a eliminar
     * @return Tipo de dato Boolean para determinar si la acción tuvo o no tuvo éxito
     */
    public Boolean eliminarArchivoComprobanteLocalizacion(DtoSeguroFacultativo seguroFacultativo){
        if(seguroFacultativo == null) return false;
        if(SeguroFacultativoValidacionConverter.of(seguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() != 1.1D){
            try {
                if(!seguroFacultativo.getSeguroFactultativo().getRutaComprobanteLocalizacion().equals("") || seguroFacultativo.getSeguroFactultativo().getRutaComprobanteLocalizacion() != null){
                    ServicioArchivos.eliminarArchivo(seguroFacultativo.getSeguroFactultativo().getRutaComprobanteLocalizacion());
                }
                seguroFacultativo.getSeguroFactultativo().setRutaComprobanteLocalizacion(null);
                em.merge(seguroFacultativo.getSeguroFactultativo());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se eliminó la envidencia: " + seguroFacultativo.getSeguroFactultativo().getRutaComprobanteLocalizacion(), e);
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * Método que permite la eliminación del archivo del Comprobante de Vigencia de Derechos del registro de Seguro Facultativo
     * 
     * Uso especifico para interfaz de tipo usuario, uso genérico
     * @param seguroFacultativo Contiene la ruta del archivo a eliminar
     * @return Tipo de dato Boolean para determinar si la acción tuvo o no tuvo éxito
     */
    public Boolean eliminarArchivoComprobanteVigenciaDerechos(DtoSeguroFacultativo seguroFacultativo){
        if(seguroFacultativo == null) return false;
        if(SeguroFacultativoValidacionConverter.of(seguroFacultativo.getSeguroFactultativo().getValidacionEnfermeria()).getNivel() != 1.1D){
            try {
                if(!seguroFacultativo.getSeguroFactultativo().getRutaComprobanteVigenciaDeDerechos().equals("") || seguroFacultativo.getSeguroFactultativo().getRutaComprobanteVigenciaDeDerechos() != null){
                    ServicioArchivos.eliminarArchivo(seguroFacultativo.getSeguroFactultativo().getRutaComprobanteVigenciaDeDerechos());
                }
                seguroFacultativo.getSeguroFactultativo().setRutaComprobanteVigenciaDeDerechos(null);
                em.merge(seguroFacultativo.getSeguroFactultativo());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se eliminó la envidencia: " + seguroFacultativo.getSeguroFactultativo().getRutaComprobanteVigenciaDeDerechos(), e);
                return false;
            }
            return true;
        }else{
            return false;
        }
    }
}
