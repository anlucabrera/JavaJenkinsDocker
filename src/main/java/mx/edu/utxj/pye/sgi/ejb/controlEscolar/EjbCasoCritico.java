package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import javafx.print.Collation;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EjbCasoCritico implements Serializable {
    @EJB EjbPacker packer;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    /**
     * Permite identificar si existen casos críticos abiertos para un estudiante en unidad de materia actual o anteriores
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa lista de casos criticos o código de error en caso de no poder generarla
     */
    public ResultadoEJB<List<DtoCasoCritico>> identificar(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion){
        try{
            //lista de unidades previas  a la unidad que se muestra en pantalla para buscar casos criticos tanto en la unidad actual como en las anteriores
            List<UnidadMateriaConfiguracion> unidades = em.createQuery("select umc from UnidadMateriaConfiguracion umc inner join umc.idUnidadMateria um where um.idMateria=:materia and umc.carga=:carga and um.noUnidad <= :unidad", UnidadMateriaConfiguracion.class)
                    .setParameter("materia", dtoCargaAcademica.getMateria())
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .setParameter("unidad", dtoUnidadConfiguracion.getUnidadMateria().getNoUnidad())
                    .getResultStream()
                    .collect(Collectors.toList());

            //lista de estados de casos críticos que indicas que estén abiertos
            List<CasoCriticoEstado> estadosAbiertos = Arrays.stream(CasoCriticoEstado.values())
                    .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() > 0D)
                    .collect(Collectors.toList());

            //lista de casos críticos abiertos del estudiante correspondientes a la carga académica
            List<CasoCritico> casosCriticos= em.createQuery("select cc from CasoCritico cc where cc.carga=:carga and cc.configuracion in :unidades and cc.idEstudiante=:estudiante and cc.estado in :estadosAbiertos order by cc.fechaRegistro desc", CasoCritico.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .setParameter("unidades", unidades)
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion())
                    .setParameter("estadosAbiertos", estadosAbiertos)
                    .getResultStream()
                    .collect(Collectors.toList());

            List<ResultadoEJB<DtoCasoCritico>> resultadoEJBS = casosCriticos.stream()
                    .map(casoCritico -> packer.packCasoCritico(casoCritico, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion))
                    .collect(Collectors.toList());
            ResultadoEJB.logErroresEnLista(resultadoEJBS, DtoCasoCritico.class);

            List<@NonNull DtoCasoCritico> dtos = resultadoEJBS
                    .stream()
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(dtos.isEmpty()) {
                List<DtoCasoCritico> l = Collections.EMPTY_LIST;
                return ResultadoEJB.crearErroneo(2, l, "No se encontraron casos críticos.");
            }
            return ResultadoEJB.crearCorrecto(dtos, "Lista de empaquetados de casos críticos");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo identificar el caso crítico en base al estudiante, carga académica y configuración de la unidad (EjbCasoCritico.identificarCasoCritico).", e, null);
        }
    }

    /**
     * Permite crear una nueva instancia de caso crítico y empaquertarla asignando cun zero como clave primaria del caso crítico para que al intentar registrar se detecte
     * como no persistida a la instancia, en caso de existir un caso critico abierto para el mismo estudiante, carga y unidad se devuelve el caso existente
     * para evitar abrir mas de un critico bajo las mismas condiciones
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return
     */
    public ResultadoEJB<DtoCasoCritico> generarNuevo(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion){
        try{
//            System.out.println("EjbCasoCritico.generarNuevo");
//            System.out.println("dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            ResultadoEJB<List<DtoCasoCritico>> identificar = identificar(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
//            System.out.println("identificar = " + identificar);
            if(identificar.getCorrecto()) return ResultadoEJB.crearCorrecto(identificar.getValor().get(0), "Caso crítico existente");
            //
            CasoCritico casoCritico = new CasoCritico(0);
            casoCritico.setCarga(dtoCargaAcademica.getCargaAcademica());
            casoCritico.setComentariosEspecialista(null);
            casoCritico.setComentariosTutor(null);
            casoCritico.setConfiguracion(dtoUnidadConfiguracion.getUnidadMateriaConfiguracion());
            casoCritico.setDescripcion(null);
            casoCritico.setEspecialista(null);
            casoCritico.setEstado(CasoCriticoEstado.SIN_REGISTRO.getLabel());
            casoCritico.setEvidenciaEspecialista(null);
            casoCritico.setEvidenciaTutor(null);
            casoCritico.setFechaCierre(null);
            casoCritico.setFechaRegistro(new Date());
            casoCritico.setIdEstudiante(dtoEstudiante.getInscripcionActiva().getInscripcion());
            casoCritico.setTipo(CasoCriticoTipo.ASISTENCIA_IRREGURLAR.getLabel());
            casoCritico.setTutor(dtoCargaAcademica.getGrupo().getTutor());

//            System.out.println("casoCritico = " + casoCritico.getTipo());
            ResultadoEJB<DtoCasoCritico> dtoCasoCriticoResultadoEJB = packer.packCasoCritico(casoCritico, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
//            System.out.println("dtoCasoCriticoResultadoEJB = " + dtoCasoCriticoResultadoEJB);

            if(!dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCritico.class);
            else return ResultadoEJB.crearCorrecto(dtoCasoCriticoResultadoEJB.getValor(), "Nuevo caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo generar un nuevo caso critico (EjbCasoCritico.generarNuevo).", DtoCasoCritico.class);
        }
    }

    /**
     * Crea o actualiza el caso critico
     * @param dtoCasoCritico Empaquetado del caso critico
     * @return Regresa el empaquetado del caso critico actualizado
     */
    public  ResultadoEJB<DtoCasoCritico> registrar(DtoCasoCritico dtoCasoCritico){
        try{
            //
            Boolean existe = (dtoCasoCritico.getCasoCritico().getCaso() != null && dtoCasoCritico.getCasoCritico().getCaso() > 0);
            System.out.println("dtoCasoCritico.getCasoCritico().getTipo() = " + dtoCasoCritico.getCasoCritico().getTipo());
            System.out.println("dtoCasoCritico.getCasoCritico().getEstado() = " + dtoCasoCritico.getCasoCritico().getEstado());
            if(!existe) {
                dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.REGISTRADO.getLabel());
                em.persist(dtoCasoCritico.getCasoCritico());
            }
            else em.merge(dtoCasoCritico.getCasoCritico());

            ResultadoEJB<DtoCasoCritico> dtoCasoCriticoResultadoEJB = packer.packCasoCritico(dtoCasoCritico.getCasoCritico(), dtoCasoCritico.getDtoEstudiante(), dtoCasoCritico.getDtoCargaAcademica(), dtoCasoCritico.getDtoUnidadConfiguracion());

            if(dtoCasoCriticoResultadoEJB.getCorrecto()) return ResultadoEJB.crearCorrecto(null, "Caso crítico registrado correctamente.");
            else return ResultadoEJB.crearErroneo(2, dtoCasoCriticoResultadoEJB.getMensaje(), DtoCasoCritico.class);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el caso crítico (EjbCasoCritico.registrarCasoCritico).", e, DtoCasoCritico.class);
        }
    }
}
