/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.DtoSeguimientoTestVocacional;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbReporteExcelTestVocacional")
@MultipartConfig
public class EjbReporteExcelTestVocacional {
    @EJB        EjbCarga                            ejbCarga;
    @EJB        Facade                              f;
    @EJB        EjbSeguimientoTestVocacional        ejb;
    
    @Inject     Caster                              caster;
    
    public static final String REPORTE_TV_AVANCE = "reporte_tv_avance.xlsx";
    public static final String REPORTE_TV_AVANCE_COPIA = "reporte_tv_avance_copia.xlsx";
    public static final String REPORTE_TV_AVANCE_ACTUALIZADO = "reporte_tv_avance_actualizado.xlsx";
    
    public static final String REPORTE_TV_PROGRAMA_EDUCATIVO = "reporte_tv_programa_educativo.xlsx";
    public static final String REPORTE_TV_PROGRAMA_EDUCATIVO_COPIA = "reporte_tv_programa_educativo_copia.xlsx";
    public static final String REPORTE_TV_PROGRAMA_EDUCATIVO_ACTUALIZADO = "reporte_tv_programa_educativo_actualizado.xlsx";
    
    public static final String REPORTE_TV_GENERAL = "reporte_tv_general.xlsx";
    public static final String REPORTE_TV_GENERAL_COPIA = "reporte_tv_general_copia.xlsx";
    public static final String REPORTE_TV_GENERAL_ACTUALIZADO = "reporte_tv_general_actualizado.xlsx";
    
    /**
     * Método que permite la creación del directorio principal para la lectura de la plantilla para el reporte final
     * @return 
     */
    public String crearDirectorioReporte(){
        String rutaPlantilla = ejbCarga.crearDirectorioTestVocacionalReporte();
        return rutaPlantilla;
    }
    
    /**
     * Método que permite la creación del directorio para la creación del reporte final
     * @return 
     */
    public String crearDirectorioReporteCompleto(){
        String rutaPlantillaC = ejbCarga.crearDirectorioTestVocacionalReporteCompleto();
        return rutaPlantillaC;
    }
    
    /**
     * Método que permite la descarga de información del Avance de Test Vocacional mostrada en la interfaz de seguimiento.
     * No hace referencia a la base de datos
     * @param periodoEscolar
     * @param programaEducativo
     * @param grupo
     * @param claveTrabajador
     * @param listaDtoTestVocacionalAvanceProgramaEducativo
     * @param listaDtoTestVocacionalAvanceGrupal
     * @param listaDtoTestVocacionalResultadoEstudiante
     * @return 
     */
    public ResultadoEJB<String> getReporteAvanceTestVocacional(@NonNull PeriodosEscolares periodoEscolar, @NonNull AreasUniversidad programaEducativo, @NonNull Grupo grupo, @NonNull Integer claveTrabajador, List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo> listaDtoTestVocacionalAvanceProgramaEducativo, List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal> listaDtoTestVocacionalAvanceGrupal, List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaDtoTestVocacionalResultadoEstudiante){
        String plantilla = crearDirectorioReporte().concat(REPORTE_TV_AVANCE);
        String plantillaCopia = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("_").concat(REPORTE_TV_AVANCE_COPIA));
        String plantillaCompleto = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("_").concat(REPORTE_TV_AVANCE_ACTUALIZADO));
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteTvAvance = new XSSFWorkbook();
            reporteTvAvance = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            
//            Hoja de avance por periodo escolar - Muestra el avance Institucional mostrando únicamente a programas educativos que contengan a un estudiante que haya iniciado su test vocacional
            XSSFSheet hojaAvancePeriodoEscolar = reporteTvAvance.getSheetAt(0);
            
//            Hoja de avance por periodo escolar, programa educativo - Muestra el avance de todos los grupos pertencientes al programa educativo seleccionado
            XSSFSheet hojaAvancePeGrupos = reporteTvAvance.getSheetAt(1);
            
//            Hoja de avance grupal - Muestra el avance del test vocacional realizado por los estudiantes del grupo seleccionado
            XSSFSheet hojaAvanceGrupal = reporteTvAvance.getSheetAt(2);
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Vacíado de información correspondiente a las primeras celdas en la hoja Avance Periodo Escolar del archivo de excel
//            Cuatrimestre
            Integer ubicacionCuatrimestre = 3;
            fila = (XSSFRow) (Row) hojaAvancePeriodoEscolar.getRow(ubicacionCuatrimestre);
            if(null == fila){
                fila = hojaAvancePeriodoEscolar.createRow(ubicacionCuatrimestre);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Fecha de reporte
            Integer ubicacionFecha = 4;
            fila = (XSSFRow) (Row) hojaAvancePeriodoEscolar.getRow(ubicacionFecha);
            if(null == celda){
                fila = hojaAvancePeriodoEscolar.createRow(ubicacionFecha);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            
//            Iteración para vaciar información en la hoja Avance Periodo Escolar del archivo de excel
            for(Integer listaAPE = 0; listaAPE < listaDtoTestVocacionalAvanceProgramaEducativo.size(); listaAPE++){
                Integer ubicacion = listaAPE + 8;
                fila = (XSSFRow) (Row) hojaAvancePeriodoEscolar.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaAvancePeriodoEscolar.createRow(ubicacion);
                }
                
//                SIGLAS
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getSiglas());

//                PROGRAMA EDUCATIVO
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getProgramaEducativo());

//                TOTAL MATRICULA
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.NUMERIC);
                }
                fila.getCell(3).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getTotalMatricula());

//                TOTAL COMPLETOS
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getTotalCompletos());

//                TOTAL INCOMPLETOS
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getTotalIncompletos());

//                TOTAL SIN ENTRAR
                celda = fila.getCell(6);
                if(null == celda){
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getTotalSinEntrar());

//                PORCENTAJE
                celda = fila.getCell(7);
                if(null == celda){
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(listaDtoTestVocacionalAvanceProgramaEducativo.get(listaAPE).getPorcentaje());
            }
            
//           Hoja Avance PE Grupos 
//            Vacíado de información correspondiente a las primeras celdas en la hoja Avance PE Grupos del archivo de excel

//            Cuatrimestre
            Integer ubicacionCuatrimestreHojaPeGrupos = 3;
            fila = (XSSFRow) (Row) hojaAvancePeGrupos.getRow(ubicacionCuatrimestreHojaPeGrupos);
            if(null == fila){
                fila = hojaAvancePeGrupos.createRow(ubicacionCuatrimestreHojaPeGrupos);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Programa educativo
            Integer ubicacionProgramaEducativoHojaPeGrupos = 4;
            fila = (XSSFRow) (Row) hojaAvancePeGrupos.getRow(ubicacionProgramaEducativoHojaPeGrupos);
            if(null == fila){
                fila = hojaAvancePeGrupos.createRow(ubicacionProgramaEducativoHojaPeGrupos);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(programaEducativo.getNombre());
            
//            Fecha de reporte
            Integer ubicacionFechaHojaPeGrupos = 5;
            fila = (XSSFRow) (Row) hojaAvancePeGrupos.getRow(ubicacionFechaHojaPeGrupos);
            if(null == celda){
                fila = hojaAvancePeGrupos.createRow(ubicacionFechaHojaPeGrupos);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            
//            Iteración para vaciar información en la hoja Avance PE Grupos del archivo de excel
            for(Integer listaAPEG = 0; listaAPEG < listaDtoTestVocacionalAvanceGrupal.size(); listaAPEG++){
                Integer ubicacion = listaAPEG + 9;
                fila = (XSSFRow) (Row) hojaAvancePeGrupos.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaAvancePeGrupos.createRow(ubicacion);
                }
                
//                GRUPO
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getGrupo().getGrado() + "° " + listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getGrupo().getLiteral());

//                PROGRAMA EDUCATIVO
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(programaEducativo.getSiglas() + " - " + programaEducativo.getNombre());

//                TOTAL MATRICULA GRUPO
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.NUMERIC);
                }
                fila.getCell(3).setCellValue(listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getTotalEstudiantesGrupo());

//                TOTAL COMPLETOS
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getTotalCompletos());

//                TOTAL INCOMPLETOS
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getTotalIncompletos());

//                TOTAL SIN ENTRAR
                celda = fila.getCell(6);
                if(null == celda){
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getTotalSinEntrar());

//                PORCENTAJE
                celda = fila.getCell(7);
                if(null == celda){
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(listaDtoTestVocacionalAvanceGrupal.get(listaAPEG).getPorcentaje());
            }
            
//           Hoja Avance Grupal - Listado de estudiantes
//            Vacíado de información correspondiente a las primeras celdas en la hoja Avance Grupal del archivo de excel

//            Cuatrimestre
            Integer ubicacionCuatrimestreEstudiantes = 3;
            fila = (XSSFRow) (Row) hojaAvanceGrupal.getRow(ubicacionCuatrimestreEstudiantes);
            if(null == fila){
                fila = hojaAvanceGrupal.createRow(ubicacionCuatrimestreEstudiantes);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Programa educativo
            Integer ubicacionProgramaEducativoEstudiantes = 4;
            fila = (XSSFRow) (Row) hojaAvanceGrupal.getRow(ubicacionProgramaEducativoEstudiantes);
            if(null == fila){
                fila = hojaAvanceGrupal.createRow(ubicacionProgramaEducativoEstudiantes);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(programaEducativo.getNombre());
            
//            Grupo
            Integer ubicacionGrupoEstudiantes = 5;
            fila = (XSSFRow) (Row) hojaAvanceGrupal.getRow(ubicacionGrupoEstudiantes);
            if(null == fila){
                fila = hojaAvanceGrupal.createRow(ubicacionGrupoEstudiantes);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(grupo.getGrado() + "° " + grupo.getLiteral());
            
//            Fecha de reporte
            Integer ubicacionFechaEstudiantes = 6;
            fila = (XSSFRow) (Row) hojaAvanceGrupal.getRow(ubicacionFechaEstudiantes);
            if(null == celda){
                fila = hojaAvanceGrupal.createRow(ubicacionFechaEstudiantes);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            
            //            Iteración para vaciar información en la hoja Avance PE Grupos del archivo de excel
            for(Integer listaEstudiantes = 0; listaEstudiantes < listaDtoTestVocacionalResultadoEstudiante.size(); listaEstudiantes++){
                Integer ubicacion = listaEstudiantes + 10;
                fila = (XSSFRow) (Row) hojaAvanceGrupal.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaAvanceGrupal.createRow(ubicacion);
                }
                
//                ESTUDIANTE
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstudiante().getMatricula() + " - " + listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " " + listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstudiante().getAspirante().getIdPersona().getApellidoMaterno() + " " + listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstudiante().getAspirante().getIdPersona().getNombre());
                
//                Programa educativo
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getProgramaEducativo().getNombre());
                
//                Grupo
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstudiante().getGrupo().getGrado() + "° " + listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstudiante().getGrupo().getLiteral());
                
//                Test Vocacional Aplicado
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacionalAplicada()?"Si":"No");
                
//                Estatus Test Vocacional
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getEstatusTest());
                
                if(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacionalAplicada()){
                    
//                Identificador
                    celda = fila.getCell(6);
                    if (null == celda) {
                        fila.createCell(6, CellType.NUMERIC);
                    }
                    fila.getCell(6).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getIdPersona());

//                Fechas de aplicación
//                Fecha Inicio
                    celda = fila.getCell(7);
                    if (null == celda) {
                        fila.createCell(7, CellType.STRING);
                    }
                    fila.getCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getFechaCreacion()));

//                Fecha Termino
                    if (listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getFechaTermino() != null) {
                        celda = fila.getCell(8);
                        if (null == celda) {
                            fila.createCell(8, CellType.STRING);
                        }
                        fila.getCell(8).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getFechaTermino()));
                    }
                    
//                Interes en otra carrera
//                SI / NO
                    if (listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getCarreraInteresOu() != null) {
                        celda = fila.getCell(9);
                        if (null == celda) {
                            fila.createCell(9, CellType.STRING);
                        }
                        fila.getCell(9).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getCarreraInteresOu() ? "Si" : "No");
                    }

//                Respuesta carrera de intéres
                    if (listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getRespuestaCarreraInteresOu() != null) {
                        celda = fila.getCell(10);
                        if (null == celda) {
                            fila.createCell(10, CellType.STRING);
                        }
                        fila.getCell(10).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTestVocacional().getRespuestaCarreraInteresOu());
                    }

//                Resultado Primera Opcion
                    if (listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getPrimeraOpcionCarreraResultado() != null) {
                        celda = fila.getCell(11);
                        if (null == celda) {
                            fila.createCell(11, CellType.STRING);
                        }
                        fila.getCell(11).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getPrimeraOpcionCarreraResultado());
                    }

//                Resultado Segunda Opcion
                    if (listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getSegundaOpcionCarreraResultado() != null) {
                        celda = fila.getCell(12);
                        if (null == celda) {
                            fila.createCell(12, CellType.STRING);
                        }
                        fila.getCell(12).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getSegundaOpcionCarreraResultado());
                    }

//                Resultado Tercera Opcion
                    if (listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTerceraOpcionCarreraResultado() != null) {
                        celda = fila.getCell(13);
                        if (null == celda) {
                            fila.createCell(13, CellType.STRING);
                        }
                        fila.getCell(13).setCellValue(listaDtoTestVocacionalResultadoEstudiante.get(listaEstudiantes).getTerceraOpcionCarreraResultado());
                    }
                } 
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteTvAvance.write(archivoSalida);
            reporteTvAvance.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException fnfe) {
            return ResultadoEJB.crearErroneo(1, "EjbReporteExcelTestVocacional.getReporteAvanceTestVocacional() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", fnfe, null);
        } catch (IOException ioe) {
            return ResultadoEJB.crearErroneo(2, "EjbReporteExcelTestVocacional.getReporteAvanceTestVocacional() - Ha ocurrido un error al momento de generar el reporte, error de lectura o escritura en el archivo." , ioe, null);
        } catch (EncryptedDocumentException | InvalidFormatException mce) {
            return ResultadoEJB.crearErroneo(3, "EjbReporteExcelTestVocacional.getReporteAvanceTestVocacional() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", mce, null);
        }
        return ResultadoEJB.crearCorrecto(plantillaCompleto, "Ruta de descarga del reporte de avance de Test Vocacional");
    }
    
    public ResultadoEJB<String> getReporteAvanceTestVocacionalPorPeriodoEscolar(@NonNull PeriodosEscolares periodoEscolar, @NonNull Integer claveTrabajador){
        String plantilla = crearDirectorioReporte().concat(REPORTE_TV_GENERAL);
        String plantillaCopia = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("_").concat(REPORTE_TV_GENERAL_COPIA));
        String plantillaCompleto = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("_").concat(REPORTE_TV_GENERAL_ACTUALIZADO));
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteTvAvanceGeneral = new XSSFWorkbook();
            reporteTvAvanceGeneral = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            
//            Hoja de avance por periodo escolar - Muestra el avance Institucional mostrando a todos los estudiantes
            XSSFSheet hojaAvanceGeneral = reporteTvAvanceGeneral.getSheetAt(0);
            
            XSSFRow fila;
            XSSFCell celda;
            
            ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante>> resultadoLista = ejb.obtenerListaTestVocacionalPorPeriodo(periodoEscolar);
            if(!resultadoLista.getCorrecto()) return ResultadoEJB.crearErroneo(4, null, "No se ha podido consultar el listado cuatrimestral");
            List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante> listaEstudiantesCuatrimestre = resultadoLista.getValor();
            
//            Vacíado de información correspondiente a las primeras celdas en la hoja Avance Periodo Escolar del archivo de excel
//            Cuatrimestre
            Integer ubicacionCuatrimestre = 3;
            fila = (XSSFRow) (Row) hojaAvanceGeneral.getRow(ubicacionCuatrimestre);
            if(null == fila){
                fila = hojaAvanceGeneral.createRow(ubicacionCuatrimestre);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Fecha de reporte
            Integer ubicacionFecha = 4;
            fila = (XSSFRow) (Row) hojaAvanceGeneral.getRow(ubicacionFecha);
            if(null == celda){
                fila = hojaAvanceGeneral.createRow(ubicacionFecha);
            }
            celda = fila.getCell(2);
            if(null == celda){
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            
//            Iteración para vaciar información en la hoja Avance Periodo Escolar del archivo de excel
            
//            Iteración para vaciar información en la hoja Avance PE Grupos del archivo de excel
            for(Integer listaEstudiantes = 0; listaEstudiantes < listaEstudiantesCuatrimestre.size(); listaEstudiantes++){
                Integer ubicacion = listaEstudiantes + 8;
                fila = (XSSFRow) (Row) hojaAvanceGeneral.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaAvanceGeneral.createRow(ubicacion);
                }
                
//                ESTUDIANTE
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstudiante().getMatricula() + " - " + listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " " + listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstudiante().getAspirante().getIdPersona().getApellidoMaterno() + " " + listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstudiante().getAspirante().getIdPersona().getNombre());
                
//                Programa educativo
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getProgramaEducativo().getNombre());
                
//                Grupo
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstudiante().getGrupo().getGrado() + "° " + listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstudiante().getGrupo().getLiteral());
                
//                Test Vocacional Aplicado
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacionalAplicada()?"Si":"No");
                
//                Estatus Test Vocacional
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getEstatusTest());
                
                if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacionalAplicada()) {
//                Identificador
                    celda = fila.getCell(6);
                    if (null == celda) {
                        fila.createCell(6, CellType.NUMERIC);
                    }
                    fila.getCell(6).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getIdPersona());

//                Fechas de aplicación
//                Fecha Inicio
                    celda = fila.getCell(7);
                    if (null == celda) {
                        fila.createCell(7, CellType.STRING);
                    }
                    fila.getCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getFechaCreacion()));

//                Fecha Termino
                    if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getFechaTermino() != null) {
                        celda = fila.getCell(8);
                        if (null == celda) {
                            fila.createCell(8, CellType.STRING);
                        }
                        fila.getCell(8).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getFechaTermino()));
                    }

//                Interes en otra carrera
//                SI / NO
                    if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getCarreraInteresOu() != null) {
                        celda = fila.getCell(9);
                        if (null == celda) {
                            fila.createCell(9, CellType.STRING);
                        }
                        fila.getCell(9).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getCarreraInteresOu() ? "Si" : "No");
                    }

//                Respuesta carrera de intéres
                    if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getCarreraInteresOu() != null) {
                        celda = fila.getCell(10);
                        if (null == celda) {
                            fila.createCell(10, CellType.STRING);
                        }
                        fila.getCell(10).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTestVocacional().getRespuestaCarreraInteresOu());
                    }

//                Resultado Primera Opcion
                    if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getPrimeraOpcionCarreraResultado() != null) {
                        celda = fila.getCell(11);
                        if (null == celda) {
                            fila.createCell(11, CellType.STRING);
                        }
                        fila.getCell(11).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getPrimeraOpcionCarreraResultado());
                    }

//                Resultado Segunda Opcion
                    if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getSegundaOpcionCarreraResultado() != null) {
                        celda = fila.getCell(12);
                        if (null == celda) {
                            fila.createCell(12, CellType.STRING);
                        }
                        fila.getCell(12).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getSegundaOpcionCarreraResultado());
                    }

//                Resultado Tercera Opcion
                    if (listaEstudiantesCuatrimestre.get(listaEstudiantes).getTerceraOpcionCarreraResultado() != null) {
                        celda = fila.getCell(13);
                        if (null == celda) {
                            fila.createCell(13, CellType.STRING);
                        }
                        fila.getCell(13).setCellValue(listaEstudiantesCuatrimestre.get(listaEstudiantes).getTerceraOpcionCarreraResultado());
                    }
                }
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteTvAvanceGeneral.write(archivoSalida);
            reporteTvAvanceGeneral.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException fnfe) {
            return ResultadoEJB.crearErroneo(1, "EjbReporteExcelTestVocacional.getReporteAvanceTestVocacionalPorPeriodoEscolar() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", fnfe, null);
        } catch (IOException ioe) {
            return ResultadoEJB.crearErroneo(2, "EjbReporteExcelTestVocacional.getReporteAvanceTestVocacionalPorPeriodoEscolar() - Ha ocurrido un error al momento de generar el reporte, error de lectura o escritura en el archivo." , ioe, null);
        } catch (EncryptedDocumentException | InvalidFormatException mce) {
            return ResultadoEJB.crearErroneo(3, "EjbReporteExcelTestVocacional.getReporteAvanceTestVocacionalPorPeriodoEscolar() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", mce, null);
        }
        return ResultadoEJB.crearCorrecto(plantillaCompleto, "Ruta de descarga del reporte de avance por periodo escolar del Test Vocacional");
    }
    
}
