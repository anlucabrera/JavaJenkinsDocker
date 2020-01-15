package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;

import javax.faces.model.SelectItem;
import javax.persistence.Entity;
import javax.servlet.http.Part;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import mx.edu.utxj.pye.sgi.dto.Apartado;

public class ReincorporacionRolServiciosEscolares extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo serviciosEscolares;

    /**
     * Representa la referencia al evento activo de reincorporación
     */
    @Getter @NonNull private EventoEscolar eventoActivo;

    /**
     * Periodo escolar en el que se hara la reincorporación
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Representa la clave del periodo activo
     */
    @Getter @NonNull private Integer periodoActivo;

    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;

    /**
     * Variable de apoyo para realizar la busqueda de los estudiantes
     */
    @Getter @NonNull private String buscquedaCurp;

    /**
     * Representa al resultado obtenido de la busqueda por curp
     */
    @Getter @NonNull private Persona persona;

    @Getter @Setter private DtoReincorporacion reincorporacion;
    /**
     * Variables que representa la busqueda por curp de la Persona
     */
    @Getter @Setter private String nombre, primerAp, segundoAp, estadoCivil, curp, url;
    @Getter @Setter private Integer paisItem, estadoItem, municipioItem, localidadItem,folioAspirante,index;
    @Getter @Setter private Date fechaNacimiento = new Date();
    @Getter @Setter private Generos genero;
    @Getter @Setter private Short areaAcademicaPO;
    @Getter @Setter private Short areaAcademicaSO;
    @Getter @Setter private Boolean dm = true,com = true,df = true,da = true,evif = true, estatusFicha = null,finalizado,mostrar;
    @Getter @Setter protected EncuestaAspirante resultado;
    @Getter @Setter private List<LenguaIndigena> listaLenguasIndigenas;

    /**
     * Variable que representa la apertura de los selectOneMenu para saber el estado de nacimiento
     */
    @Getter @Setter private Boolean estado, estadoExt;

    /**
     * Variable que representa el archivo de la CURP que el estudiante proporciona
     */
    @Getter @Setter private Part file;

    /**
     * Variable de lista de SelectItem para la representacion de los selectOneMenu de acuerdo al tipo
     */
    @Getter @NonNull private List<SelectItem> selectItemEstados, selectItemPaises, selectItemMunicipios, selectItemMunicipiosIEMS, selectItemLocalidades, selectItemGeneros, selectItemTipoSangre,
            selectItemDiscapacidades, selectItemMunicipiosProcedencia, selectItemAsentamientos, selectItemAsentamientosProcedencia, selectItemEstadosDomicilioRadica,
            selectItemEstadosProcedencia, selectItemEstadosTutor, selectItemEstadosIEMS, selectItemLocalidadesIEMS, selectItemAsentamientoTutor;

    @Getter @Setter @NonNull private List<SelectItem> listaIems,listaPEP,listaPES,selectItemAreasPO,selectItemAreasSO,respuestasDependientesEconomicos,respuestasPosiblesNivelEstudios;
    @Getter @Setter private List<Sistema> listaSistema;
    @Getter @Setter protected List<Apartado> apartados;
    @Getter @Setter private List<MedioDifusion> listaMedioDifusion;
    /**
     * Variables de apoyo
     */
    @Getter @Setter @NonNull private String opcion;
    @Getter @Setter private Boolean buscar, registrar;

    /**
     *Variable de apoyo referente a los antecedentes médicos del estudiante
     */
    @Getter @NonNull private List<String> selectDM;

    /**
     * Referente a los datos médicos encontrados del estudiante
     */
    @Getter @NonNull private DatosMedicos datosMedicos;

    /**
     * Referente a los medios de comunicación encontrados por el estudiante
     */
    @Getter @NonNull private MedioComunicacion medioComunicacion;

    /**
     *
     */
    @Getter @NonNull private Aspirante aspirante;

    @Getter @NonNull private Domicilio domicilio;

    @Getter @NonNull private DatosFamiliares datosFamiliares;

    @Getter @NonNull private TutorFamiliar tutorFamiliar;

    @Getter @NonNull private DatosAcademicos datosAcademicos;

    @Getter @NonNull private List<Ocupacion> ocupacion;

    @Getter @NonNull private List<EspecialidadCentro> especialidades;

    @Getter @NonNull private List<Escolaridad> escolaridad;

    public ReincorporacionRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
        super(filtro);
        this.serviciosEscolares = serviciosEscolares;
        this.programa = programa;
    }

    public void setServiciosEscolares(PersonalActivo serviciosEscolares) {
        this.serviciosEscolares = serviciosEscolares;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setBuscquedaCurp(String buscquedaCurp) {
        this.buscquedaCurp = buscquedaCurp;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public void setSelectItemEstados(List<SelectItem> selectItemEstados) {
        this.selectItemEstados = selectItemEstados;
    }

    public void setSelectItemPaises(List<SelectItem> selectItemPaises) {
        this.selectItemPaises = selectItemPaises;
    }

    public void setSelectItemMunicipios(List<SelectItem> selectItemMunicipios) {
        this.selectItemMunicipios = selectItemMunicipios;
    }

    public void setSelectItemLocalidades(List<SelectItem> selectItemLocalidades) {
        this.selectItemLocalidades = selectItemLocalidades;
    }

    public void setSelectItemGeneros(List<SelectItem> selectItemGeneros) {
        this.selectItemGeneros = selectItemGeneros;
    }

    public void setSelectDM(List<String> selectDM) {
        this.selectDM = selectDM;
    }

    public void setDatosMedicos(DatosMedicos datosMedicos) {
        this.datosMedicos = datosMedicos;
    }

    public void setMedioComunicacion(MedioComunicacion medioComunicacion) {
        this.medioComunicacion = medioComunicacion;
    }

    public void setSelectItemTipoSangre(List<SelectItem> selectItemTipoSangre) {
        this.selectItemTipoSangre = selectItemTipoSangre;
    }

    public void setSelectItemDiscapacidades(List<SelectItem> selectItemDiscapacidades) {
        this.selectItemDiscapacidades = selectItemDiscapacidades;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public void setSelectItemMunicipiosProcedencia(List<SelectItem> selectItemMunicipiosProcedencia) {
        this.selectItemMunicipiosProcedencia = selectItemMunicipiosProcedencia;
    }

    public void setSelectItemAsentamientos(List<SelectItem> selectItemAsentamientos) {
        this.selectItemAsentamientos = selectItemAsentamientos;
    }

    public void setSelectItemAsentamientosProcedencia(List<SelectItem> selectItemAsentamientosProcedencia) {
        this.selectItemAsentamientosProcedencia = selectItemAsentamientosProcedencia;
    }

    public void setSelectItemEstadosDomicilioRadica(List<SelectItem> selectItemEstadosDomicilioRadica) {
        this.selectItemEstadosDomicilioRadica = selectItemEstadosDomicilioRadica;
    }

    public void setSelectItemEstadosProcedencia(List<SelectItem> selectItemEstadosProcedencia) {
        this.selectItemEstadosProcedencia = selectItemEstadosProcedencia;
    }

    public void setDatosFamiliares(DatosFamiliares datosFamiliares) {
        this.datosFamiliares = datosFamiliares;
    }

    public void setTutorFamiliar(TutorFamiliar tutorFamiliar) {
        this.tutorFamiliar = tutorFamiliar;
    }

    public void setDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }

    public void setSelectItemEstadosTutor(List<SelectItem> selectItemEstadosTutor) {
        this.selectItemEstadosTutor = selectItemEstadosTutor;
    }

    public void setSelectItemEstadosIEMS(List<SelectItem> selectItemEstadosIEMS) {
        this.selectItemEstadosIEMS = selectItemEstadosIEMS;
    }

    public void setSelectItemMunicipiosIEMS(List<SelectItem> selectItemMunicipiosIEMS) {
        this.selectItemMunicipiosIEMS = selectItemMunicipiosIEMS;
    }

    public void setSelectItemLocalidadesIEMS(List<SelectItem> selectItemLocalidadesIEMS) {
        this.selectItemLocalidadesIEMS = selectItemLocalidadesIEMS;
    }

    public void setSelectItemAsentamientoTutor(List<SelectItem> selectItemAsentamientoTutor) {
        this.selectItemAsentamientoTutor = selectItemAsentamientoTutor;
    }

    public void setOcupacion(List<Ocupacion> ocupacion) {
        this.ocupacion = ocupacion;
    }

    public void setEspecialidades(List<EspecialidadCentro> especialidades) {
        this.especialidades = especialidades;
    }

    public void setEscolaridad(List<Escolaridad> escolaridad) {
        this.escolaridad = escolaridad;
    }
}
