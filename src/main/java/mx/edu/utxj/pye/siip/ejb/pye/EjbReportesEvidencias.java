/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.ejb.pye;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.dto.pye.DtoRegistroEvidencias;
import mx.edu.utxj.pye.siip.dto.pye.DtoTipoInformacionRegistro;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbReportesEvidencias")
public class EjbReportesEvidencias {
    
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB EjbCarga ejbCarga;
    
    @Inject Caster caster;
    
    @EJB Facade f;
    private EntityManager em;
    
    public static final String rutaPlantillaListado = "C:\\archivos\\modulos_registro\\reportes\\relacionRegistroEvidencias.xlsx";
    public static final String listadoActualizado = "listadoRegistroEvidencias.xlsx";
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite obtener el anio del ejercicio fiscal de consulta
     * @param ejercicio
     * @return Resultado del proceso
     */
    public ResultadoEJB<Short> getAnioEjercicio(Short ejercicio) {
        try{
            EjerciciosFiscales ejercicioFiscal = em.createQuery("SELECT e FROM EjerciciosFiscales e WHERE e.ejercicioFiscal=:ejercicio", EjerciciosFiscales.class)
                    .setParameter("ejercicio", ejercicio)
                    .getResultStream()
                    .findFirst().orElse(null);
          
            
            return ResultadoEJB.crearCorrecto(ejercicioFiscal.getAnio(), "Anio del ejercicio fiscal de consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el anio del ejercicio fiscal de consulta. (EjbReportesEvidencias.getAnioEjercicio)", e, null);
        }
    }
    
    /**
     * Permite obtener el evento del registro con el anio y mes de consulta
     * @param anio
     * @param mes
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventosRegistros> getEventoRegistro(Short anio, String mes) {
        try{
            EventosRegistros eventoRegistro = em.createQuery("SELECT e FROM EventosRegistros e WHERE e.ejercicioFiscal.anio=:anio AND e.mes=:mes",  EventosRegistros.class)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getResultStream()
                    .findFirst().orElse(null);
          
            
            return ResultadoEJB.crearCorrecto(eventoRegistro, "Evento de registro del anio y mes de consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el evento de registro del anio y mes de consulta. (EjbReportesEvidencias.getEventoRegistro)", e, null);
        }
    }
    
    /**
     * Permite descargar un archivo .zip que contiene las evidencias de módulo de registro del evento escolar seleccionado
     * @param eventosRegistros
     * @param listaRegistros
     * @param nombreModulo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    public String getDescargarEvidenciasModulo(EventosRegistros eventosRegistros, List<Registros> listaRegistros, String nombreModulo) throws Throwable {
        String rutaGeneral = "C:\\archivos\\"+eventosRegistros.getEjercicioFiscal().getAnio();
        String rutaZipGeneral ="C:\\archivos\\"+eventosRegistros.getEjercicioFiscal().getAnio()+"\\listadoEvidencias\\"+nombreModulo+"\\"+eventosRegistros.getMes()+".zip";
        
        File dir = new File(rutaGeneral);
        String zipDirName = rutaZipGeneral;
        
        List<Evidencias> listaEvidencias = obtenerListaEvidenciasRegistros(listaRegistros).getValor();
        
        String archivoListado = getDescargarRelacionRegistroEvidencias(eventosRegistros, nombreModulo, listaEvidencias);
        
        List<String> listaRutasEvidencias = obtenerListaRutasEvidencias(listaEvidencias).getValor();
        listaRutasEvidencias.add(archivoListado);

        zipDirectory(dir, listaRutasEvidencias, zipDirName);

        return zipDirName;
    }
    
     /**
     * Permite obtener la lista de evidencias del registro y evento escolar correspondiente
     * @param listaRegistros
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Evidencias>> obtenerListaEvidenciasRegistros(List<Registros> listaRegistros){
        try{
            List<Evidencias> listaEvidencias = em.createQuery("SELECT e FROM Evidencias e WHERE e.registrosList IN :registros",  Evidencias.class)
                    .setParameter("registros", listaRegistros)
                    .getResultStream()
                    .collect(Collectors.toList());
          
            
            return ResultadoEJB.crearCorrecto(listaEvidencias, "Lista evidencias del registro y evento escolar correspondiente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias del registro y evento escolar correspondiente. (EjbReportesEvidencias.obtenerListaRutasEvidencias)", e, null);
        }
    }
    
    /**
     * Permite descargar un archivo que contiene una relación de los registros y las evidencias registradas
     * @param eventoRegistro
     * @param nombreModulo
     * @param listaEvidencias
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    public String getDescargarRelacionRegistroEvidencias(EventosRegistros eventoRegistro, String nombreModulo, List<Evidencias> listaEvidencias) throws Throwable {
        String rutaPlantillaC = "C:\\archivos\\"+eventoRegistro.getEjercicioFiscal().getAnio()+"\\listadoEvidencias\\"+nombreModulo+"\\"+"listadoRegistroEvidencias.xlsx";
 
        Map beans = new HashMap();
        beans.put("regEvid", getListadoRegistroEvidencias(eventoRegistro, listaEvidencias).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantillaListado, beans, rutaPlantillaC);

        return rutaPlantillaC;
    }
    
     /**
     * Permite obtener el listado de los registros y sus evidencias registradas
     * @param eventoRegistro
     * @param listaEvidencias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRegistroEvidencias>> getListadoRegistroEvidencias(EventosRegistros eventoRegistro, List<Evidencias> listaEvidencias){
        try{
            List<DtoRegistroEvidencias> listadoRegistroEvidencias = new ArrayList<>();
            
            List<EvidenciasDetalle> listaEvidenciasDetalle = em.createQuery("SELECT e FROM EvidenciasDetalle e WHERE e.evidencia IN :evidencias",  EvidenciasDetalle.class)
                    .setParameter("evidencias", listaEvidencias)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            if(!listaEvidenciasDetalle.isEmpty()){
              
                listaEvidenciasDetalle.forEach(evidenciaDetalle -> {
                   
                    String ejercicio = String.valueOf(eventoRegistro.getEjercicioFiscal().getAnio());
                     
                    Registros registro = evidenciaDetalle.getEvidencia().getRegistrosList().get(0);
                    
                    DtoTipoInformacionRegistro dtoTipoInformacionRegistro = getObtenerInformacionRegistro(registro).getValor();
                    
                    String nombreEvidencia = caster.rutaToArchivoNombre(evidenciaDetalle.getRuta());
                     
                    DtoRegistroEvidencias dto = new DtoRegistroEvidencias(evidenciaDetalle, ejercicio, eventoRegistro.getMes(), dtoTipoInformacionRegistro, nombreEvidencia);
                     
                    listadoRegistroEvidencias.add(dto);
                });
            }
            
            return ResultadoEJB.crearCorrecto(listadoRegistroEvidencias.stream().sorted(DtoRegistroEvidencias::compareTo).collect(Collectors.toList()), "Listado de los registros y sus evidencias registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el listado de los registros y sus evidencias registradas. (EjbReportesEvidencias.getListadoRegistroEvidencias)", e, null);
        }
    }
    
    /**
     * Permite obtener la información del módulo de registro según su tipo de registro
     * @param registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoTipoInformacionRegistro> getObtenerInformacionRegistro(Registros registro){
        try{
            AreasUniversidad programaEducativo = new AreasUniversidad();
            CiclosEscolares cicloEscolar = new CiclosEscolares();
            PeriodosEscolares periodoEscolar = new PeriodosEscolares();
            Generaciones generacion = new Generaciones();
            Personal personal = new Personal();
            BecaTipos tipoBeca = new BecaTipos();
            
            DtoTipoInformacionRegistro dtoTipoInformacionRegistro = new DtoTipoInformacionRegistro();
            dtoTipoInformacionRegistro.setTipoRegistro(registro.getTipo().getNombre());
            
            
            switch (registro.getTipo().getRegistroTipo()) {
                case 1: // Bolsa de trabajo
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getBolsaTrabajo().getBolsatrab().concat(" Puesto: ").concat(registro.getBolsaTrabajo().getPuestoOfertado()).concat(" en ").concat(registro.getBolsaTrabajo().getEmpresa().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getBolsaTrabajo().getPeriodo());
                    break;
                case 2: // Bolsa de trabajo entrevistas
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getBolsaTrabajoEntrevistas().getBolsatrabent().getBolsatrab().concat(" - Matricula: ").concat(String.valueOf(registro.getBolsaTrabajoEntrevistas().getMatricula())));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 3: // Asesorías y tutorías mensuales
                    programaEducativo = em.find(AreasUniversidad.class, registro.getAsesoriasTutoriasMensualPeriodosEscolares().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getAsesoriasTutoriasMensualPeriodosEscolares().getCuatrimestre().concat("° ").concat(registro.getAsesoriasTutoriasMensualPeriodosEscolares().getGrupo()).concat(" de ").concat(programaEducativo.getSiglas()).concat(" ").concat(registro.getAsesoriasTutoriasMensualPeriodosEscolares().getTipoActividad()).concat(" - ").concat(registro.getAsesoriasTutoriasMensualPeriodosEscolares().getTipo()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getAsesoriasTutoriasMensualPeriodosEscolares().getPeriodoEscolar());
                    break;
                case 6: // Acervo bibliográfico
                    periodoEscolar = em.find(PeriodosEscolares.class, registro.getAcervoBibliograficoPeriodosEscolares().getPeriodoEscolar());
                    programaEducativo = em.find(AreasUniversidad.class, registro.getAcervoBibliograficoPeriodosEscolares().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Periodo escolar: ".concat(String.valueOf(periodoEscolar.getMesInicio().getMes())).concat(" - ").concat(String.valueOf(periodoEscolar.getMesFin().getMes())).concat(" ").concat(String.valueOf(periodoEscolar.getAnio())).concat(" de ").concat(programaEducativo.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(periodoEscolar.getPeriodo());
                    break;
                case 7: // Servicio enfermería
                    dtoTipoInformacionRegistro.setInformacionRegistro("Tipo de servicio: ".concat(registro.getServiciosEnfermeriaCicloPeriodos().getServicio().getDescripcion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getServiciosEnfermeriaCicloPeriodos().getPeriodoEscolar());
                    break;
                case 8: // Actividades varias
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getActividadesVariasRegistro().getNombre());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 9: // Becas
                    tipoBeca = em.find(BecaTipos.class, registro.getBecasPeriodosEscolares().getBeca());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Tipo de beca: ".concat(tipoBeca.getNombre()).concat(" y tipo de solicitud: ").concat(registro.getBecasPeriodosEscolares().getSolicitud()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getBecasPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo());
                    break;
                case 10: // Egetsu
                    generacion = em.find(Generaciones.class, registro.getEgetsuResultadosGeneraciones().getGeneracion());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Generación: ".concat(String.valueOf(generacion.getInicio())).concat(" - ").concat(String.valueOf(generacion.getFin())));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 11: // Exani
                    cicloEscolar = em.find(CiclosEscolares.class, registro.getExaniResultadosCiclosEscolares().getCicloEscolar());
                    programaEducativo = em.find(AreasUniversidad.class, registro.getExaniResultadosCiclosEscolares().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Ciclo escolar: ".concat(String.valueOf(cicloEscolar.getInicio())).concat(" - ").concat(String.valueOf(cicloEscolar.getFin())).concat(" de ").concat(programaEducativo.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 12: // Distribución de equipo de cómputo
                    periodoEscolar = em.find(PeriodosEscolares.class, registro.getEquiposComputoCicloPeriodoEscolar().getPeriodoEscolar());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Periodo escolar: ".concat(periodoEscolar.getMesInicio().getMes()).concat(" - ").concat(periodoEscolar.getMesFin().getMes()).concat(" ").concat(String.valueOf(periodoEscolar.getAnio())));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getEquiposComputoCicloPeriodoEscolar().getPeriodoEscolar());
                    break;
                case 13: // Distribución de equipo de cómputo con internet
                    periodoEscolar = em.find(PeriodosEscolares.class, registro.getEquiposComputoInternetCicloPeriodoEscolar().getPeriodoEscolar());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Periodo escolar: ".concat(periodoEscolar.getMesInicio().getMes()).concat(" - ").concat(periodoEscolar.getMesFin().getMes()).concat(" ").concat(String.valueOf(periodoEscolar.getAnio())));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(periodoEscolar.getPeriodo());
                    break;
                case 14: // Ingresos propios
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getIngresosPropiosCaptados().getConceptoIngresosCaptados().concat(" - ").concat(registro.getIngresosPropiosCaptados().getDescripcion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getIngresosPropiosCaptados().getPeriodoEscolar());
                    break;
                case 15: // Presupuestos
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getPresupuestos().getPresupuestoOperacion().concat(" ").concat(registro.getPresupuestos().getPresupuestoTipo()).concat(" ").concat(registro.getPresupuestos().getCapituloTipo().getNumero()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 16: // Actividades de formación integral
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getActividadesFormacionIntegral().getActividadFormacionIntegral().concat(" ").concat(registro.getActividadesFormacionIntegral().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getActividadesFormacionIntegral().getPeriodo());
                    break;
                case 17: // Actividades de formación integral participantes
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getParticipantesActividadesFormacionIntegral().getActividadFormacionIntegral().getActividadFormacionIntegral().concat(" Matricula: ").concat(registro.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getMatricula()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getParticipantesActividadesFormacionIntegral().getMatriculaPeriodosEscolares().getPeriodo());
                    break;
                case 18: // Capacidad instalada
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getCapacidadInstaladaCiclosEscolares().getInstalacion().getDescripcion());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 19: // Distribución de aulas
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getDistribucionAulasCicloPeriodosEscolares().getAula().getNombre());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getDistribucionAulasCicloPeriodosEscolares().getPeriodoEscolar());
                    break;
                case 20: // Distribución de laboratorios y talleres
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getDistribucionLabtallCicloPeriodosEscolares().getNombre());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getDistribucionLabtallCicloPeriodosEscolares().getPeriodoEscolar());
                    break;
                case 21: // Actividad del egresado y egresada
                    generacion = em.find(Generaciones.class, registro.getActividadEgresadoGeneracion().getGeneracion());
                    programaEducativo = em.find(AreasUniversidad.class, registro.getActividadEgresadoGeneracion().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Generación: ".concat(String.valueOf(generacion.getInicio())).concat(" - ").concat(String.valueOf(generacion.getFin())).concat(" de ").concat(programaEducativo.getSiglas()).concat(" Actividad: ").concat(registro.getActividadEgresadoGeneracion().getActividad().getDescripcion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 22: // Actividad económica egresados y egresadas
                    generacion = em.find(Generaciones.class, registro.getActividadEconomicaEgresadoGeneracion().getGeneracion());
                    programaEducativo = em.find(AreasUniversidad.class, registro.getActividadEconomicaEgresadoGeneracion().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Generación: ".concat(String.valueOf(generacion.getInicio())).concat(" - ").concat(String.valueOf(generacion.getFin())).concat(" de ").concat(programaEducativo.getSiglas()).concat(" Sector: ").concat(registro.getActividadEconomicaEgresadoGeneracion().getSector().getDescripcion()).concat(" Giro: ").concat(registro.getActividadEconomicaEgresadoGeneracion().getGiro().getDescripcion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 23: // Nivel de ocupación
                    generacion = em.find(Generaciones.class, registro.getNivelOcupacionEgresadosGeneracion().getGeneracion());
                    programaEducativo = em.find(AreasUniversidad.class, registro.getNivelOcupacionEgresadosGeneracion().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Generación: ".concat(String.valueOf(generacion.getInicio())).concat(" - ").concat(String.valueOf(generacion.getFin())).concat(" de ").concat(programaEducativo.getSiglas()).concat(" Ocupación: ").concat(registro.getNivelOcupacionEgresadosGeneracion().getOcupacion().getDescripcion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 24: // Nivel de ingresos
                    generacion = em.find(Generaciones.class, registro.getNivelIngresosEgresadosGeneracion().getGeneracion());
                    programaEducativo = em.find(AreasUniversidad.class, registro.getNivelIngresosEgresadosGeneracion().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Generación: ".concat(String.valueOf(generacion.getInicio())).concat(" - ").concat(String.valueOf(generacion.getFin())).concat(" de ").concat(programaEducativo.getSiglas()).concat(" Nivel de ingreso: ").concat(registro.getNivelIngresosEgresadosGeneracion().getIngreso().getIngresos()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 25: // Deserción
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getDesercionPeriodosEscolares().getDpe().concat(" Matricula: ").concat(registro.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo());
                    break;
                case 26: // Deserción por reprobación
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getDesercionReprobacionMaterias().getDpe().getDpe().concat(" Materia: "));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 27: // Organismos vinculados
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getOrganismosVinculados().getNombre());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 28: // Convenios
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getConvenios().getEmpresa().getNombre());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 29: // Difusión
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getDifusionIems().getIems().getNombre());
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 30: // Visitas industriales
                    programaEducativo = em.find(AreasUniversidad.class, registro.getVisitasIndustriales().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro(String.valueOf(registro.getVisitasIndustriales().getCuatrimestre()).concat("° ").concat(String.valueOf(registro.getVisitasIndustriales().getGrupo())).concat(" de ").concat(programaEducativo.getSiglas()).concat(" - ").concat(registro.getVisitasIndustriales().getEmpresa().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getVisitasIndustriales().getPeriodoEscolar());
                    break;
                case 31: // Registros movilidad
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getRegistrosMovilidad().getRegistroMovilidad().concat(" en ").concat(registro.getRegistrosMovilidad().getInstitucionOrganizacion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getRegistrosMovilidad().getPeriodoEscolarCursado());
                    break;
                case 32: // Movilidad estudiantil
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().getMatricula().concat(" ").concat(registro.getRegistroMovilidadEstudiante().getRegistroMovilidad().getRegistroMovilidad()).concat(" en ").concat(registro.getRegistroMovilidadEstudiante().getRegistroMovilidad().getInstitucionOrganizacion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getRegistroMovilidadEstudiante().getMatriculaPeriodosEscolares().getPeriodo());
                    break;
                case 33: // Movilidad docente
                    personal = em.find(Personal.class, registro.getRegistroMovilidadDocente().getClavePersonal());
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getRegistroMovilidadDocente().getRegistroMovilidad().getRegistroMovilidad().concat(" en ").concat(registro.getRegistroMovilidadDocente().getRegistroMovilidad().getInstitucionOrganizacion()).concat(personal.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 34: // Ferias profesiográficas
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getFeriasProfesiograficas().getFeria().concat(" - ").concat(registro.getFeriasProfesiograficas().getEvento()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 35: // Ferias profesiográficas participantes
                    dtoTipoInformacionRegistro.setInformacionRegistro("IEMS: ".concat(registro.getFeriasParticipantes().getIems().getNombre()).concat(" ").concat(registro.getFeriasParticipantes().getFeria().getFeria()).concat(" - ").concat(registro.getFeriasProfesiograficas().getEvento()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 36: // Servicios tecnológicos
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getServiciosTecnologicosAnioMes().getServicio().concat(" - ").concat(registro.getServiciosTecnologicosAnioMes().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 37: // Servicios tecnológicos participantes
                    dtoTipoInformacionRegistro.setInformacionRegistro("Nombre: ".concat(registro.getServiciosTecnologicosParticipantes().getNombre()).concat(" en ").concat(registro.getServiciosTecnologicosParticipantes().getServicioTecnologico().getServicio()).concat(" - ").concat(registro.getServiciosTecnologicosParticipantes().getServicioTecnologico().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 38: // Cuerpos académicos
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getCuerposAcademicosRegistro().getCuerpoAcademico().concat(" ").concat(registro.getCuerposAcademicosRegistro().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 39: // Cuerpos académicos integrantes
                    personal = em.find(Personal.class, registro.getCuerpacadIntegrantes().getPersonal());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Integrante: ".concat(personal.getNombre()).concat(" en ").concat(registro.getCuerpacadIntegrantes().getCuerpoAcademico().getCuerpoAcademico()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 40: // Cuerpos académicos líneas de investigación
                    dtoTipoInformacionRegistro.setInformacionRegistro("Nombre: ".concat(registro.getCuerpacadLineas().getNombre()).concat(" en ").concat(registro.getCuerpacadLineas().getCuerpoAcademico().getCuerpoAcademico()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 41: // Productos académicos
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getProductosAcademicos().getProductoAcademico().concat(" ").concat(registro.getProductosAcademicos().getNombreProd()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 42: // Productos académicos personal
                    personal = em.find(Personal.class, registro.getProductosAcademicosPersonal().getPersonal());
                    dtoTipoInformacionRegistro.setInformacionRegistro(personal.getNombre().concat(" ").concat(registro.getProductosAcademicosPersonal().getProductoAcademico().getProductoAcademico()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 43: // Matricula inicial
                    periodoEscolar = em.find(PeriodosEscolares.class, registro.getMatriculaPeriodosEscolares().getPeriodo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Periodo escolar: ".concat(periodoEscolar.getMesInicio().getMes()).concat(" - ").concat(periodoEscolar.getMesFin().getMes()).concat(" ").concat(String.valueOf(periodoEscolar.getAnio())));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getMatriculaPeriodosEscolares().getPeriodo());
                    break;
                case 44: // Programas educativos pertinentes y de calidad
                    programaEducativo = em.find(AreasUniversidad.class, registro.getProgramasPertcal().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Programa educativo: ".concat(programaEducativo.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 45: // Personal capacitado
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getPersonalCapacitado().getCurso().concat(" - ").concat(registro.getPersonalCapacitado().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(registro.getPersonalCapacitado().getPeriodo());
                    break;
                case 46: // Personal capacitado participantes
                    personal = em.find(Personal.class, registro.getParticipantesPersonalCapacitado().getPersonal());
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getParticipantesPersonalCapacitado().getPercap().getCurso().concat(" ").concat(personal.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 47: // Programas de estímulos
                    personal = em.find(Personal.class, registro.getProgramasEstimulos().getTrabajador());
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getProgramasEstimulos().getTipoPrograma().getDescripcion().concat(" ").concat(personal.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 48: // Comisiones académicas
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getComisionesAcademicas().getComisionAcademica().concat(" - ").concat(registro.getComisionesAcademicas().getTipoComision().getDescripcion()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 49: // Comisiones académicas participantes
                    personal = em.find(Personal.class, registro.getComisionesAcademicasParticipantes().getParticipante());
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getComisionesAcademicasParticipantes().getComisionAcademica().getComisionAcademica().concat(" ").concat(personal.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 50: // Reconocimiento prodep
                    personal = em.find(Personal.class, registro.getReconocimientoProdepRegistros().getDocente());
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getReconocimientoProdepRegistros().getCuerpAcad().getCuerpoAcademico().concat(" ").concat(personal.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 52: // Sesiones individuales
                    programaEducativo = em.find(AreasUniversidad.class, registro.getSesionIndividualMensualPsicopedogia().getProgramaEducativo());
                    dtoTipoInformacionRegistro.setInformacionRegistro("Área conflicto: ".concat(registro.getSesionIndividualMensualPsicopedogia().getAreaConflicto().getDescripcion()).concat(" Tipo de sesión: ").concat(registro.getSesionIndividualMensualPsicopedogia().getOtroTipoSesion().getDescripcion()).concat(" de ").concat(programaEducativo.getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 54: // Satisfacción de servicios tecnológicos y educación continua
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getSatisfaccionServtecEducontAnioMes().getServicio().getServicio().concat(" ").concat(registro.getSatisfaccionServtecEducontAnioMes().getServicio().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                case 55: // Resultados instrumento de evaluación educación continua
                    dtoTipoInformacionRegistro.setInformacionRegistro(registro.getEvaluacionSatisfaccionResultados().getServicioTecnologico().getServicio().concat(" ").concat(registro.getEvaluacionSatisfaccionResultados().getServicioTecnologico().getNombre()));
                    dtoTipoInformacionRegistro.setPeriodoEscolar(0);
                    break;
                default:
                    break;
            }
            
            return ResultadoEJB.crearCorrecto(dtoTipoInformacionRegistro, "La información del módulo de registro según su tipo de registro se obtuvo correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información del módulo de registro según su tipo de registro. (EjbReportesEvidencias.getObtenerInformacionRegistro)", e, null);
        }
    }
    
    
     /**
     * Permite obtener la lista de rutas de las evidencias del módulo de registro y evento seleccionado
     * @param listaEvidencias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> obtenerListaRutasEvidencias(List<Evidencias> listaEvidencias){
        try{
            List<String> listaRutas = new ArrayList<>();
            
            List<EvidenciasDetalle> listaEvidenciasDetalle = em.createQuery("SELECT e FROM EvidenciasDetalle e WHERE e.evidencia IN :evidencias",  EvidenciasDetalle.class)
                    .setParameter("evidencias", listaEvidencias)
                    .getResultStream()
                    .collect(Collectors.toList());
          
            if(!listaEvidenciasDetalle.isEmpty()){
                listaRutas = listaEvidenciasDetalle.stream().map(p->p.getRuta()).collect(Collectors.toList());
            }
            
            return ResultadoEJB.crearCorrecto(listaRutas, "Lista de rutas de las evidencias del módulo de registro y evento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de rutas de las evidencias del módulo de registro y evento seleccionado. (EjbReportesEvidencias.obtenerListaRutasEvidencias)", e, null);
        }
    }
    
    /**
     * Permite generar el archivo que contiene las evidencias del módulo de registro del evento seleccionado
     * @param dir
     * @param zipDirName
     * @return Resultado del proceso
     */
    private void zipDirectory(File dir, List<String> listaRutas, String zipDirName) {
        try {
            
            try (FileOutputStream fos = new FileOutputStream(zipDirName)) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                for (String filePath : listaRutas) {
                    File f = new File(filePath);
                    if (f.exists()) {

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
    
}
