/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.text.DateFormat;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesDto;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.converter.ComisionOficioEstatusConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.converter.PlanAccionTutorialEstadoConverter;

/**
 *
 * @author UTXJ
 */
@Named(value = "caster")
@ApplicationScoped
public class Caster {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    @EJB EjbPropiedades ep;
    private DecimalFormat df = new DecimalFormat("0.##");
    @EJB Facade f;
    private EntityManager em;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbRegistroAsesoriaTutoria ejbTutor;
    @EJB EjbPeriodoEventoRegistro ejbPeriodo;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public Caster() {
    }

    public int toInt(long l) {
        return (int) l;
    }

    public float toFloat(long l) {
        return (float) l;
    }

    public int indexOf(List l, Object e) {
        return l.indexOf(e);
    }

    public String periodoToString(PeriodosEscolares periodo) {
        return (new StringBuilder())
                .append(periodo.getMesInicio().getMes())
                .append(" - ")
                .append(periodo.getMesFin().getMes())
                .append(" ")
                .append(periodo.getMesInicio().getMes().equals("Septiembre") ? sdf.format(periodo.getCiclo().getInicio()) : sdf.format(periodo.getCiclo().getFin()))
                .toString();
    }

    public String clavePeriodoToString(Integer periodo){
        if(periodo == null) return "Clave de periodo nulo";
        PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, periodo);
        return periodoToString(periodoEscolar);
    }

    public String cicloEscolarToString(CiclosEscolares cicloEscolar){
        Calendar calendario = new GregorianCalendar();
        Calendar calendario2 = new GregorianCalendar();
        calendario.setTime(cicloEscolar.getInicio());
        calendario2.setTime(cicloEscolar.getFin());
        return (new StringBuilder())
                .append(calendario.get(Calendar.YEAR))
                .append(" - ")
                .append(calendario2.get(Calendar.YEAR))
                .toString();
    }

    public String clavePeriodoToCicloString(Integer periodo){
        try {
            PeriodosEscolares periodosEscolares = em.find(PeriodosEscolares.class, periodo);
            CiclosEscolares ciclo = periodosEscolares.getCiclo();
            return cicloEscolarToString(ciclo);
        }catch (Exception e){
            return "Error";
        }
    }

    public String periodoToStringAnio(PeriodosEscolares periodo){
        return (new StringBuilder())
                .append(periodo.getMesInicio().getMes())
                .append(" ")
                .append(periodo.getAnio())
                .toString();
    }
    
    public static short planeacionToTotal(PlaneacionesCuatrimestrales pc){
        return (short)(pc.getHorasClaseTsu() + pc.getHorasClaseIng() + pc.getEstadias() + pc.getProyectoInvestigacion() + pc.getAsesoriaClase() + pc.getTutoriaIndividual() + pc.getTutoriaGrupal() + pc.getReunionAcademia() + pc.getActividadesVarias());
    }
    
    public String getEjercicioFiscal(){
        return String.valueOf(ep.leerPropiedadEntera("finanzasEjercicioFiscal").getAsInt());
    }

    public String generacionToString(Generaciones generacion) {
        return (new StringBuilder())
                .append(generacion.getInicio())
                .append(" - ")
                .append(generacion.getFin())
                .toString();
    }

    public String organismoVinculadoToString(OrganismosVinculados organismo) {
        return (new StringBuilder())
                .append(organismo.getNombre())
                .toString();
    }

    public long getEvidenciasTamanioLimite() {
        return 50 * 1024 * 1024;
    }

    public String bytesToMegabytes(long bytes) {
        return String.format("%s MB", df.format((double) bytes / (double) (1024 * 1024)));
    }

    public String rutaToArchivoNombre(String ruta) {
        return FilenameUtils.getName(ruta);
    }

    public String mimeToTipo(String mime) {
        switch (mime) {
            case "image/png":
                return "PNG";
            case "image/jpeg":
            case "image/jpg":
                return "JPG";
            case "application/pdf":
                return "PDF";
            default:
                return "NA";
        }
    }

    public String numeroOficioToPath(ComisionOficios oficio){
        char[] chars = oficio.getOficio().toCharArray();
        chars[9] = '_';
        chars[14] = '_';
        return String.valueOf(chars);
    }

    public TramitesDto tramiteToListaTramitesDto(Tramites tramite){
        PersonalActivo seguidor = ejbPersonalBean.pack(em.find(Personal.class, tramite.getClave()));
        PersonalActivo comisionado = ejbPersonalBean.pack(em.find(Personal.class, tramite.getComisionOficios().getComisionado()));
        return new TramitesDto(seguidor,comisionado,tramite);
    }

    public Boolean tramiteEditablePorOperativo(Tramites tramite, Personal logueado){
        if(tramite == null) return false;
        ComisionOficioEstatus estatus = ComisionOficioEstatusConverter.of(tramite.getComisionOficios().getEstatus());
        if(estatus == null) return false;
        return (estatus.getId() < ComisionOficioEstatus.APROBADO_POR_SUPERIOR.getId() || estatus == ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_FIZCALIZACION)
                && (logueado.getClave() == tramite.getComisionOficios().getComisionado() || logueado.getClave() == tramite.getClave());
    }

    public String tramiteToDestino(Tramites tramite){
        if(tramite == null) return "";
        Municipio municipio = em.find(Municipio.class, new MunicipioPK(tramite.getComisionOficios().getEstado(), tramite.getComisionOficios().getMunicipio()));
        Estado estado = em.find(Estado.class, tramite.getComisionOficios().getEstado());
        return tramite.getComisionOficios().getDependencia().concat(", ").concat(municipio.getNombre()).concat(", ").concat(estado.getNombre());
    }

    public String comisionOficioEstatusToLabel(ComisionOficioEstatus estatus){
        return estatus.getLabel().replaceAll("_", " ");
    }

    public String enumLabelToString(String label){
        return label.replaceAll("_", " ");
    }

    public static void main(String[] args) {
        Caster caster = new Caster();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Caster.main(): " + caster.bytesToMegabytes(450l));
    }

     public String cicloToString(CiclosEscolares ciclo){
        return (new StringBuilder())
                .append(ciclo.getInicio())
                .append("-")
                .append(ciclo.getFin())
                .toString();

    }
    
    public long getDocsTitulacionTamanioLimite() {
        return 5 * 1024 * 1024;
    }

    public String dtoCargaAcademicaToString(DtoCargaAcademica dtoCargaAcademica){
        if(dtoCargaAcademica == null) return "Null";
        return DtoCargaAcademica.toLabel(dtoCargaAcademica);
        /*return dtoCargaAcademica.getPrograma().getSiglas().concat(" - ")
                .concat(String.valueOf(dtoCargaAcademica.getGrupo().getGrado())).concat(dtoCargaAcademica.getGrupo().getLiteral().toString()).concat(" - ")
                .concat(dtoCargaAcademica.getMateria().getNombre());*/
    }

    public String dtoUnidadConfiguracionToString(DtoUnidadConfiguracion dtoUnidadConfiguracion){
        if(dtoUnidadConfiguracion == null) return "Null";
        return String.valueOf(dtoUnidadConfiguracion.getUnidadMateria().getNoUnidad()).concat(".  ")
                .concat(dtoUnidadConfiguracion.getUnidadMateria().getNombre());
    }

    public String dtoEstudianteToString(DtoEstudiante dtoEstudiante){
        if(dtoEstudiante == null) return "Estudiante nulo";
        return dtoEstudiante.getPersona().getApellidoPaterno().concat(" ")
                .concat(dtoEstudiante.getPersona().getApellidoMaterno()).concat(" ")
                .concat(dtoEstudiante.getPersona().getNombre()).concat(" (")
                .concat(String.valueOf(dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())).concat(")");
    }

    public String fechaEventoEscolarToString(Date fechaInicio, Date fechaFin) {
        SimpleDateFormat dia = new SimpleDateFormat("dd");
        SimpleDateFormat anio = new SimpleDateFormat("yyyy");
        return dia.format(fechaInicio)
                +" de "+
                convertirMes(fechaInicio)
                +" de "+
                anio.format(fechaInicio)
                +" al "+
                dia.format(fechaFin)+
                " de "+
                convertirMes(fechaFin)
                +" de "+
                anio.format(fechaFin);
    }

    public static String convertirFormatoFecha(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            String horaCadena = formatter.format(date);
            return horaCadena;
        }else return "La fecha no es correcta";
    }

    public static String convertirMes(Date date){
        String result="";
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int month=0;

        try{
            month=calendar.get(Calendar.MONTH);
        }catch(Exception ex){}
        switch(month){
            case 0:
            {
                result="Enero";
                break;
            }
            case 1:
            {
                result="Febrero";
                break;
            }
            case 2:
            {
                result="Marzo";
                break;
            }
            case 3:
            {
                result="Abril";
                break;
            }
            case 4:
            {
                result="Mayo";
                break;
            }
            case 5:
            {
                result="Junio";
                break;
            }
            case 6:
            {
                result="Julio";
                break;
            }
            case 7:
            {
                result="Agosto";
                break;
            }
            case 8:
            {
                result="Septiembre";
                break;
            }
            case 9:
            {
                result="Octubre";
                break;
            }
            case 10:
            {
                result="Noviembre";
                break;
            }
            case 11:
            {
                result="Diciembre";
                break;
            }
            default:
            {
                result="Error";
                break;
            }
        }
        return result;
    }
    
    public DtoEstudianteComplete dtoEstudianteAutocomplete(Integer idEstudiante) {
        if (idEstudiante == null) {
            return null;
        } else {
            ResultadoEJB<DtoEstudianteComplete> res = ejbTutor.empaquetaDtoEstudianteComplete(idEstudiante);
            if(res.getCorrecto()) return res.getValor();
            else return null;
        }
    }
    
    public String personalActivoLabel(Integer clave){
         try {
            return ejbTutor.personalActivoLabel(clave);
        } catch (Exception e) {
            return "";
        }
    }
    
    public Integer obtenerAreaOperativa(Integer clave){
        try {
            return (ejbTutor.obtenerAreaOperativa(clave)).getValor();
        }catch (Exception e){
            return 0;
        }
    }

    public String parseHoraMinutos(Date hora){
        DateFormat formatoHora = new SimpleDateFormat("HH:mm a");    
        return formatoHora.format(hora);
    }
    
    public Boolean permiteEliminarPAT(String estado){
        if(estado.isEmpty() || estado.equals(""))return Boolean.FALSE;
        else return (PlanAccionTutorialEstadoConverter.of(estado).getNivel() < 2.2D);
    }
    
    public Boolean validarDirector(String estado){
        if(PlanAccionTutorialEstadoConverter.of(estado).getNivel() > 1D || PlanAccionTutorialEstadoConverter.of(estado).getNivel() == -1.1D){
            return true;
        }else{
            return false;
        }
    }
    
    public String leyendaValidacion(String estado){
        if(estado.isEmpty() || estado.equals("")){
            return "Pendiente de registro";
        }else{
            return (PlanAccionTutorialEstadoConverter.of(estado).getLabel());
        }
    }
    
//    TODO: Corregir método, no acecpta el primer dia del mes
    public String fechaMinina(Integer anio, String mes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(anio, ejbPeriodo.getNumeroMes(mes)-1,1);
        Date date = calendar.getTime();
        DateFormat formatoFechaMinima = new SimpleDateFormat("yyyy-MM-dd");
        String fechaMinimaCadena = formatoFechaMinima.format(date);
        return fechaMinimaCadena;
    }
    
    public String fechaMaxima(Integer anio, String mes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(anio, ejbPeriodo.getNumeroMes(mes)-1,1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date = calendar.getTime();
        DateFormat formatoFechaMaxima = new SimpleDateFormat("yyyy-MM-dd");
        String fechaMaximaCadena = formatoFechaMaxima.format(date);
        return fechaMaximaCadena;
    }

    public AreasUniversidad getNombreCarrera(Short carreraEstudiante){
        ResultadoEJB<AreasUniversidad> res = ejbTutor.getCarreraEstudiante(carreraEstudiante);
        if(res.getCorrecto())return res.getValor();
        else return (new AreasUniversidad());
    }

    public static String obtenerNombreMes(Date mes) {
        if (mes == null) {
            return "nulo";
        }
        DateFormat formatoMes = new SimpleDateFormat("MM");
        String horaCadena = formatoMes.format(mes);
        Integer convertido = Integer.valueOf(horaCadena);

        String mesNombre = "nulo";
        Integer[] meses = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] mesesNombre = {"nulo", "enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
        for (int i = 0; i < meses.length; i++) {
//            System.err.println("Comparativo:  " + meses[i] + " HoraCadena: " + convertido);
            if (Objects.equals(meses[i], convertido)) {
                mesNombre = mesesNombre[i];
            }
        }
        return mesNombre;
    }

    public static String obtenerEjercicio(Date ejercicio) {
        if (ejercicio == null) {
            return "nulo";
        }
        DateFormat formatoEjercicio = new SimpleDateFormat("yyyy");
        String ejercicioCadena = formatoEjercicio.format(ejercicio);
        return ejercicioCadena;
    }
    public static String tipoEvaluacionconverter(String tipo){
        String tipoEv = new String();
        if(tipo.equals(EvaluacionesTipo.DOCENTE.getLabel()) || tipo.equals(EvaluacionesTipo.DOCENTE_2.getLabel() )|| tipo.equals(EvaluacionesTipo.DOCENTE_3.getLabel()) || tipo.equals(EvaluacionesTipo.DOCENTE_4.getLabel())){
            tipoEv= "Docente por asignatura";
        }else if (tipo.equals(EvaluacionesTipo.TUTOR.getLabel()) || tipo.equals(EvaluacionesTipo.TUTOR_2.getLabel())){
            tipoEv= "Tutor";
        }
        return tipoEv;
    }
}
