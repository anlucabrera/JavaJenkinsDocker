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
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
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
@Stateless(name = "EjbReporteExcelSeguroFacultativo")
@MultipartConfig
public class EjbReporteExcelSeguroFacultativo {
    @EJB        EjbCarga                            ejbCarga;
    @EJB        Facade                              f;
    @EJB        EjbValidacionSeguroFacultativo      ejbValidacionSeguroFacultativo;
    @EJB        EjbConsultaSegurosFacultativos      ejbConsultaSegurosFacultativos;
    
    @Inject     Caster                              caster;
    
    /**
     * Variables que se ocupan para leer el archivo principal de la plantilla para el reporte
     * Variables con terminación copia y actualizado son ocupadas para la creación del reporte final
     */
    
    public static final String REPORTE_SF_CUATRIMESTRAL = "reporte_cuatrimestral.xlsx";
    public static final String REPORTE_SF_CUATRIMESTRAL_COPIA = "reporte_cuatrimestral_copia.xlsx";
    public static final String REPORTE_SF_CUATRIMESTRAL_ACTUALIZADO = "reporte_cuatrimestral_actualizado.xlsx";
    
    public static final String REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO = "reporte_programa_educativo.xlsx";
    public static final String REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_COPIA = "reporte_programa_educativo_copia.xlsx";
    public static final String REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_ACTUALIZADO = "reporte_programa_educativo_actualizado.xlsx";
    
    public static final String REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_GRUPO = "reporte_grupal.xlsx";
    public static final String REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_GRUPO_COPIA = "reporte_grupal_copia.xlsx";
    public static final String REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_GRUPO_ACTUALIZADO = "reporte_grupal_actualizado.xlsx";
    
    /**
     * Método que permite la creación del directorio para la lectura de la plantilla para el reporte
     * @return 
     */
    public String crearDirectorioReporte() {
        String rutaPlantilla = ejbCarga.crearDirectorioSeguroFacultativoReporte();
        return rutaPlantilla;
    }

    /**
     * Método que permite la creación del directorio para la creación del reporte final
     * @return 
     */
    public String crearDirectorioReporteCompleto() {
        String rutaPlantillaC = ejbCarga.crearDirectorioSeguroFacultativoReporteCompleto();
        return rutaPlantillaC;
    }
    
    public ResultadoEJB<String> getReporteSfCuatrimestralGeneral(PeriodosEscolares periodoEscolar, Integer claveTrabajador){
        return getReporteSfCuatrimestral(ejbValidacionSeguroFacultativo.obtenerSfPorPeriodo(periodoEscolar), periodoEscolar, claveTrabajador);
    }
    
    public ResultadoEJB<String> getReporteSfCuatrimestralDirector(PeriodosEscolares periodoEscolar, PersonalActivo claveTrabajador){
        return getReporteSfCuatrimestral(ejbConsultaSegurosFacultativos.obtenerSfPorPeriodoDirector(claveTrabajador, periodoEscolar), periodoEscolar, claveTrabajador.getPersonal().getClave());
    }
    
    public ResultadoEJB<String> getReporteSfCuatrimestralAsesorAcademicoEstadia(ResultadoEJB<List<Integer>> estudiantesAsesoradosEstadia, PeriodosEscolares periodoEscolar, PersonalActivo claveAsesor){
        return getReporteSfCuatrimestral(ejbConsultaSegurosFacultativos.obtenerSFCuatrimestral(estudiantesAsesoradosEstadia, periodoEscolar), periodoEscolar, claveAsesor.getPersonal().getClave());
    }
    
    /**
     * Método que permite la creación y vaciado de información en el reporte de excel de tipo Seguro Facultativo con filtro cuatrimestral
     * @param listaSeguroFacultativo
     * @param periodoEscolar Uso para el filtrado de información
     * @param claveTrabajador Uso para incluir en el nombre del archivo final y evitar conflictos con el archivo final
     * @return Devuelve la ruta en donde se encuentra el archivo creado, permitiendo así la descarga desde la vista
     */
    public ResultadoEJB<String> getReporteSfCuatrimestral(ResultadoEJB<List<DtoSeguroFacultativo>> listaSeguroFacultativo, PeriodosEscolares periodoEscolar, Integer claveTrabajador){
        try {
            if(periodoEscolar == null) return ResultadoEJB.crearErroneo(1, null, "No se puede generar un reporte de seguro facultativo con un periodo escolar nulo");
            if(periodoEscolar.getPeriodo() == null) return ResultadoEJB.crearErroneo(2, null, "No se puede generar un reporte de seguro facultativo con un periodo escolar con clave nula");

            String plantilla = crearDirectorioReporte().concat(REPORTE_SF_CUATRIMESTRAL);
            String plantillaCopia = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("-").concat(REPORTE_SF_CUATRIMESTRAL_COPIA));
            String plantillaCompleto = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("-").concat(REPORTE_SF_CUATRIMESTRAL_ACTUALIZADO));

            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteSfCuatrimestral = new XSSFWorkbook();
            reporteSfCuatrimestral = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaSeguroFacultativo = reporteSfCuatrimestral.getSheetAt(0);

            XSSFRow fila;
            XSSFCell celda;
            String genero = "";
            
            if(!listaSeguroFacultativo.getCorrecto()) return ResultadoEJB.crearErroneo(6, null, listaSeguroFacultativo.getMensaje());
            
//            Cuatrimestre
            Integer ubicacionCuatrimestre = 4;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionCuatrimestre);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionCuatrimestre);
            }
            celda = fila.getCell(2);
            if (null == celda) {
                fila.createCell(2, CellType.NUMERIC);
            }
            fila.getCell(2).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Fecha de reporte
            Integer ubicacionFecha = 5;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionFecha);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionFecha);
            }
            celda = fila.getCell(2);
            if (null == celda) {
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            

//            Inicia vaciado de información
            for(Integer listaSF = 0; listaSF < listaSeguroFacultativo.getValor().size(); listaSF++){
                Integer ubicacion = listaSF + 9;
                fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaSeguroFacultativo.createRow(ubicacion);
                }
                
//                Número
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(listaSF+1);
                
//                Programa educativo
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getProgramaEducativo().getNombre());
                
//                Cuatrimestre
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.NUMERIC);
                }
                fila.getCell(3).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstudiante().getGrupo().getGrado());
                
//                Grupo
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(String.valueOf(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstudiante().getGrupo().getLiteral()));
                
//                Sexo
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.STRING);
                }
                if(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getGenero() == 1){
                    genero = "Mujer";
                }else{
                    genero = "Hombre";
                }
                fila.getCell(5).setCellValue(genero);
                
//                Situación Académica
                celda = fila.getCell(6);
                if(null == celda){
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getInscripcionActiva().getInscripcion().getTipoEstudiante().getDescripcion());
                
//                Nombre estudiante
                celda = fila.getCell(7);
                if(null == celda){
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getApellidoPaterno().concat(" ").concat(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getApellidoMaterno().concat(" ").concat(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getNombre())));
                
//                Matricula
                celda = fila.getCell(8);
                if(null == celda){
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstudiante().getMatricula());
                
//                Número de seguro social
                celda = fila.getCell(9);
                if(null == celda){
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getNss());
                
//                Estatús del seguro, validación por el área de enfermería
                celda = fila.getCell(10);
                if(null == celda){
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getValidacionEnfermeria().concat(" - ").concat(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstatusRegistro()));
                
//                CURP
                celda = fila.getCell(11);
                if(null == celda){
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getCurp());
                
//                Correo Electrónico registrado ante el IMSS
                celda = fila.getCell(12);
                if(null == celda){
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getCorreoElectronico());
                
             }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteSfCuatrimestral.write(archivoSalida);
            reporteSfCuatrimestral.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
            
            return ResultadoEJB.crearCorrecto(plantillaCompleto, "Reporte cuatrimestral generado con éxito");
        } catch (FileNotFoundException fnfe) {
            return ResultadoEJB.crearErroneo(3, "EjbReporteExcelSeguroFacultativo.getReporteSfCuatrimestral() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", fnfe, null);
        } catch (IOException ioe) {
            return ResultadoEJB.crearErroneo(4, "EjbReporteExcelSeguroFacultativo.getReporteSfCuatrimestral() - Ha ocurrido un error al momento de generar el reporte, error de lectura o escritura en el archivo." , ioe, null);
        } catch (EncryptedDocumentException | InvalidFormatException mce) {
            return ResultadoEJB.crearErroneo(5, "EjbReporteExcelSeguroFacultativo.getReporteSfCuatrimestral() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", mce, null);
        }
    }
    
    /**
     * Método que permite la creación del reporte de excel de tipo Seguro Facultativo con filtro por Periodo escolar y programa educativo
     * @param periodoEscolar Uso para el filtrado de información
     * @param programaEducativo Uso para el filtrado de información
     * @param claveTrabajador Uso para incluir en el nombre del archivo final y evitar conflictos con el archivo final
     * @return Devuelve la ruta en donde se encuentra el archivo creado, permitiendo así la descarga desde la vista
     */
    public ResultadoEJB<String> getReporteSfProgramaEducativo(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo, Integer claveTrabajador){
        try {
            if(periodoEscolar == null) return ResultadoEJB.crearErroneo(1, null, "No se puede generar un reporte de seguro facultativo con un periodo escolar nulo");
            if(periodoEscolar.getPeriodo() == null) return ResultadoEJB.crearErroneo(2, null, "No se puede generar un reporte de seguro facultativo con un periodo escolar con clave nula");
            if(programaEducativo == null) return ResultadoEJB.crearErroneo(3, null, "No se puede generar un reporte de seguro facultativo con un programa educativo nulo");
            if(programaEducativo.getArea() == null) return ResultadoEJB.crearErroneo(4, null,"No se puede crear un reporte de seguro facultativo con un programa educativo con clave nula");

            String plantilla = crearDirectorioReporte().concat(REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO);
            String plantillaCopia = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("-").concat(REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_COPIA));
            String plantillaCompleto = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("-").concat(REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_ACTUALIZADO));

            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteSfCuatrimestral = new XSSFWorkbook();
            reporteSfCuatrimestral = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaSeguroFacultativo = reporteSfCuatrimestral.getSheetAt(0);

            XSSFRow fila;
            XSSFCell celda;
            String genero = "";
            
            ResultadoEJB<List<DtoSeguroFacultativo>> listaSeguroFacultativo = ejbValidacionSeguroFacultativo.obtenerSfPorPeriodoProgramaEducativo(periodoEscolar, programaEducativo);
            
//            Programa educativo
            Integer ubicacionCarrera = 3;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionCarrera);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionCarrera);
            }
            celda = fila.getCell(2);
            if (null == celda) {
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(programaEducativo.getNombre());
            
//            Cuatrimestre
            Integer ubicacionCuatrimestre = 3;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionCuatrimestre);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionCuatrimestre);
            }
            celda = fila.getCell(8);
            if (null == celda) {
                fila.createCell(8, CellType.NUMERIC);
            }
            fila.getCell(8).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Fecha de reporte
            Integer ubicacionFecha = 4;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionFecha);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionFecha);
            }
            celda = fila.getCell(8);
            if (null == celda) {
                fila.createCell(8, CellType.STRING);
            }
            fila.getCell(8).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            
            for(Integer listaSF = 0; listaSF < listaSeguroFacultativo.getValor().size(); listaSF++){
                Integer ubicacion = listaSF + 9;
                fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaSeguroFacultativo.createRow(ubicacion);
                }
                
//                Número
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(listaSF+1);
                
//                Situación Académica
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getInscripcionActiva().getInscripcion().getTipoEstudiante().getDescripcion());
                
//                Nombre del estudiante
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getApellidoPaterno().concat(" ").concat(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getApellidoMaterno().concat(" ").concat(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getNombre())));
                
//                Cuatrimestre
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstudiante().getGrupo().getGrado());
                
//                Grupo
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(String.valueOf(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstudiante().getGrupo().getLiteral()));
                
//                Sexo
                celda = fila.getCell(6);
                if(null == celda){
                    fila.createCell(6, CellType.STRING);
                }
                if(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getGenero() == 1){
                    genero = "Mujer";
                }else{
                    genero = "Hombre";
                }
                fila.getCell(6).setCellValue(genero);
                
//                Matricula
                celda = fila.getCell(7);
                if(null == celda){
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstudiante().getMatricula());
                
//                Número de seguro social
                celda = fila.getCell(8);
                if(null == celda){
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getNss());
                
//                Estatús del seguro, validación por el área de enfermería
                celda = fila.getCell(9);
                if(null == celda){
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getValidacionEnfermeria().concat(" - ").concat(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getEstatusRegistro()));
                
//                CURP
                celda = fila.getCell(10);
                if(null == celda){
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getEstudiante().getPersona().getCurp());
                
//                Correo Electrónico registrado ante el IMSS
                celda = fila.getCell(11);
                if(null == celda){
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(listaSeguroFacultativo.getValor().get(listaSF).getSeguroFactultativo().getCorreoElectronico());
                
             }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteSfCuatrimestral.write(archivoSalida);
            reporteSfCuatrimestral.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
            
            return ResultadoEJB.crearCorrecto(plantillaCompleto, "Reporte cuatrimestral y por programa educativo generado con éxito");
        } catch (FileNotFoundException fnfe) {
            return ResultadoEJB.crearErroneo(5, "EjbReporteExcelSeguroFacultativo.getReporteSfProgramaEducativo() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", fnfe, null);
        } catch (IOException ioe) {
            return ResultadoEJB.crearErroneo(6, "EjbReporteExcelSeguroFacultativo.getReporteSfProgramaEducativo() - Ha ocurrido un error al momento de generar el reporte, error de lectura o escritura en el archivo." , ioe, null);
        } catch (EncryptedDocumentException | InvalidFormatException mce) {
            return ResultadoEJB.crearErroneo(7, "EjbReporteExcelSeguroFacultativo.getReporteSfProgramaEducativo() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", mce, null);
        }
    }
    
    
    public ResultadoEJB<String> getReporteSfGrupal(PeriodosEscolares periodoEscolar, AreasUniversidad programaEducativo, List<DtoSeguroFacultativo> listaSFPeriodoProgramaEducativoGrupo, Integer claveTrabajador){
        try {
            if(listaSFPeriodoProgramaEducativoGrupo == null) return ResultadoEJB.crearErroneo(1, null, "No se puede generar un reporte de seguro facultativo con una lista con valores nulos");
            if(listaSFPeriodoProgramaEducativoGrupo.isEmpty()) return ResultadoEJB.crearErroneo(2, null, "No se puede generar un reporte de seguro facultativo con una lista de seguro facultativo vacía");
            
            String plantilla = crearDirectorioReporte().concat(REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_GRUPO);
            String plantillaCopia = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("-").concat(REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_GRUPO_COPIA));
            String plantillaCompleto = crearDirectorioReporteCompleto().concat(String.valueOf(claveTrabajador).concat("-").concat(REPORTE_SF_CUATRIMESTRAL_PROGRAMA_EDUCATIVO_GRUPO_ACTUALIZADO));

            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteSfCuatrimestral = new XSSFWorkbook();
            reporteSfCuatrimestral = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaSeguroFacultativo = reporteSfCuatrimestral.getSheetAt(0);

            XSSFRow fila;
            XSSFCell celda;
            String genero = "";
            
//            Programa educativo
            Integer ubicacionCarrera = 3;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionCarrera);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionCarrera);
            }
            celda = fila.getCell(2);
            if (null == celda) {
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(programaEducativo.getNombre());
            
//            Tutor
            Integer ubicacionTutor = 4;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionTutor);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionTutor);
            }
            celda = fila.getCell(2);
            if (null == celda) {
                fila.createCell(2, CellType.STRING);
            }
            fila.getCell(2).setCellValue(caster.personalActivoLabel(listaSFPeriodoProgramaEducativoGrupo.get(0).getSeguroFactultativo().getEstudiante().getGrupo().getTutor()));
            
//            Cuatrimestre
            Integer ubicacionCuatrimestre = 3;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionCuatrimestre);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionCuatrimestre);
            }
            celda = fila.getCell(6);
            if (null == celda) {
                fila.createCell(6, CellType.STRING);
            }
            fila.getCell(6).setCellValue(caster.periodoToString(periodoEscolar));
            
//            Grupo
            Integer ubicacionGrupo = 4;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionGrupo);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionGrupo);
            }
            celda = fila.getCell(6);
            if (null == celda) {
                fila.createCell(6, CellType.NUMERIC);
            }
            fila.getCell(6).setCellValue(String.valueOf(listaSFPeriodoProgramaEducativoGrupo.get(0).getSeguroFactultativo().getEstudiante().getGrupo().getGrado()).concat(" - ").concat(String.valueOf(listaSFPeriodoProgramaEducativoGrupo.get(0).getSeguroFactultativo().getEstudiante().getGrupo().getLiteral())));
            
//            Fecha de reporte
            Integer ubicacionFecha = 5;
            fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacionFecha);
            if (null == fila) {
                fila = hojaSeguroFacultativo.createRow(ubicacionFecha);
            }
            celda = fila.getCell(6);
            if (null == celda) {
                fila.createCell(6, CellType.STRING);
            }
            fila.getCell(6).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()));
            
            for(Integer listaSF = 0; listaSF < listaSFPeriodoProgramaEducativoGrupo.size(); listaSF++){
                Integer ubicacion = listaSF + 9;
                fila = (XSSFRow) (Row) hojaSeguroFacultativo.getRow(ubicacion);
                
                if(null == fila){
                    fila = hojaSeguroFacultativo.createRow(ubicacion);
                }
                
//                Número
                celda = fila.getCell(1);
                if(null == celda){
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(listaSF+1);
                
//                Situación Académica
                celda = fila.getCell(2);
                if(null == celda){
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getInscripcionActiva().getInscripcion().getTipoEstudiante().getDescripcion());
                
//                Nombre del estudiante
                celda = fila.getCell(3);
                if(null == celda){
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getPersona().getApellidoPaterno().concat(" ").concat(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getPersona().getApellidoMaterno().concat(" ").concat(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getPersona().getNombre())));
                
//                Sexo
                celda = fila.getCell(4);
                if(null == celda){
                    fila.createCell(4, CellType.STRING);
                }
                if(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getPersona().getGenero() == 1){
                    genero = "Mujer";
                }else{
                    genero = "Hombre";
                }
                fila.getCell(4).setCellValue(genero);
                
//                Matricula
                celda = fila.getCell(5);
                if(null == celda){
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getSeguroFactultativo().getEstudiante().getMatricula());
                
//                Número de seguro social
                celda = fila.getCell(6);
                if(null == celda){
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getSeguroFactultativo().getNss());
                
//                CURP
                celda = fila.getCell(7);
                if(null == celda){
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getPersona().getCurp());
                
//                Correo Electrónico registrado ante el IMSS
                celda = fila.getCell(8);
                if(null == celda){
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getSeguroFactultativo().getCorreoElectronico());
                
//                Télefono del estudiante
                celda = fila.getCell(9);
                if(null == celda){
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getPersona().getMedioComunicacion().getTelefonoMovil());
                
                if(!listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getAspirante().getContactoEmergenciasEstudianteList().isEmpty()){                    
//                Nombre del contacto emergencia
                    celda = fila.getCell(10);
                    if(null == celda){
                        fila.createCell(10, CellType.STRING);
                    }
                    fila.getCell(10).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getAspirante().getContactoEmergenciasEstudianteList().get(0).getApellidoPaterno().concat(" ").concat(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getAspirante().getContactoEmergenciasEstudianteList().get(0).getApellidoMaterno()).concat(" ").concat(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getAspirante().getContactoEmergenciasEstudianteList().get(0).getNombre()));
                    
//                    Teléfono de contacto emergencia
                    celda = fila.getCell(11);
                    if(null == celda){
                        fila.createCell(11, CellType.STRING);
                    }
                    fila.getCell(11).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getAspirante().getContactoEmergenciasEstudianteList().get(0).getTelefono());
                    
//                    Parestesco
                    celda = fila.getCell(12);
                    if(null == celda){
                        fila.createCell(12, CellType.STRING);
                    }
                    fila.getCell(12).setCellValue(listaSFPeriodoProgramaEducativoGrupo.get(listaSF).getEstudiante().getAspirante().getContactoEmergenciasEstudianteList().get(0).getParentesco());
                }
                
             }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteSfCuatrimestral.write(archivoSalida);
            reporteSfCuatrimestral.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
            
            return ResultadoEJB.crearCorrecto(plantillaCompleto, "Reporte cuatrimestral y por programa educativo generado con éxito");
        } catch (FileNotFoundException fnfe) {
            return ResultadoEJB.crearErroneo(3, "EjbReporteExcelSeguroFacultativo.getReporteSfGrupal() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", fnfe, null);
        } catch (IOException ioe) {
            return ResultadoEJB.crearErroneo(4, "EjbReporteExcelSeguroFacultativo.getReporteSfGrupal() - Ha ocurrido un error al momento de generar el reporte, error de lectura o escritura en el archivo." , ioe, null);
        } catch (EncryptedDocumentException | InvalidFormatException mce) {
            return ResultadoEJB.crearErroneo(5, "EjbReporteExcelSeguroFacultativo.getReporteSfGrupal() - Ha ocurrido un error al momento de generar el reporte, el archivo no ha sido encontrado.", mce, null);
        }
    }
    
    
}
