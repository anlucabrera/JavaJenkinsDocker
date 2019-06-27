package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Stateless(name = "EjbCapturaCalificaciones")
public class EjbCapturaCalificaciones {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;

    /**
     * Permite validar si el usuario logueado es un docente
     * @param clave Número de nómina del usuario logueado
     * @return Regresa la instancia del personal si es que cumple con ser docente o codigo de error de lo contrario
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            //TODO: agregar filtros para validar al docente
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar. (EjbCapturaCalificaciones.validarDocente)", e, null);
        }
    }

    /**
     * Permite vertificar si hay un evento de captura de calificaciones activo para el docente
     * @param docente Instacia del docente validado
     * @return Regresa el evento escolar detectado para el docente o código de error de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CAPTURA_CALIFICACIONES, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para captura de calificaciones por el docente (EjbCapturaCalificaciones.verificarEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite vertificar si hay un evento de captura de calificaciones extemporanea activo para el docente
     * @param docente Instacia del docente validado
     * @return Regresa el evento escolar detectado para el docente o código de error de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEventoExtemporaneo(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CAPTURA_CALIFICACIONES_EXTEMPORANEA, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para captura de calificaciones extemporánea por el docente (EjbCapturaCalificaciones.verificarEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite obtener una lista de periodos escolares ordenados en forma descendente por fecha en los que el docente ha tenido asignaciones académicas
     * @param docente Docente del cual se van a obtener sus periodos con asignaciones
     * @return Lista de periodos o código de error de lo contrario
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCaptura(PersonalActivo docente){
        try{
            //TODO: consultar los periodos en los que el docente tenga aisgnaciones registradas y ordenarlo en forma descentente

            List<PeriodosEscolares> periodos = Collections.emptyList();
            if(periodos.isEmpty()){
                //TODO: en caso de no tener ningun periodo arrojar  un código de error
                return  ResultadoEJB.crearErroneo(2, periodos,"");
            }else{
                return ResultadoEJB.crearCorrecto(periodos, "Periodos descendentes con asignación académica del docente");
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que el docente ha tenido captura de calificaciones (EjbCapturaCalificaciones.getPeriodosConCaptura).", e, null);
        }
    }

    /**
     * Permite obtener la lista de cargas académicas que un docente tiene en el periodo escolar seleccionado por el usuario
     * @param docente Docente logueado en sistema
     * @param periodo Periodo seleccionado en pantalla
     * @return Regresa lista de cargas cadémicas o código de error de lo contrario
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargasAcadémicasPorPeriodo(PersonalActivo docente, PeriodosEscolares periodo){
        try {
            //TODO: obtener la lista de cargas académicas del docente
            List<DtoCargaAcademica> cargas = Collections.EMPTY_LIST;
            if(cargas.isEmpty()) return  ResultadoEJB.crearErroneo(2, cargas, "Usted no tiene carga académica en el periodo seleccionado");
            else return ResultadoEJB.crearCorrecto(cargas, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista cargas cadémicas por docente y periodo (EjbCapturaCalificaciones.getCargasAcadémicasPorPeriodo).", e, null);
        }
    }

    /**
     * Permite obtener un mapa de unidad y sus configuraciones correspondientes a la materia, grupo, periodo y docente de la carga académica especificada
     * @param dtoCargaAcademica  Carga de la que se desea conocer sus configuraciones
     * @return
     */
    public ResultadoEJB<Map<UnidadMateria, UnidadMateriaConfiguracion>> getConfiguraciones(DtoCargaAcademica dtoCargaAcademica){
        try {
            //TODO: obtener mapa de configuraciones
            Map<UnidadMateria, UnidadMateriaConfiguracion> configuraciones = Collections.EMPTY_MAP;
            if(configuraciones.isEmpty()) return  ResultadoEJB.crearErroneo(2, configuraciones, "No se encontraron configuraciones en la carga académica seleccionada.");
            else return ResultadoEJB.crearCorrecto(configuraciones, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de unidades y configuraciones (EjbCapturaCalificaciones.getConfiguraciones).", e, null);
        }
    }
}
