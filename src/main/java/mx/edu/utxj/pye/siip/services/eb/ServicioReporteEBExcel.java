/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionAulasCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionEquipamiento;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionInstalaciones;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbReportesEBExcel;
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
@Stateless
@MultipartConfig
public class ServicioReporteEBExcel implements EjbReportesEBExcel{

    @EJB    EjbCarga                                ejbCarga;
    @EJB    EjbCatalogos                            ejbCatalogos;
    @EJB    Facade                                  f;
    
    @EJB    EjbDistribucionEquipamiento             ejbDistribucionEquipamiento;
    @EJB    EjbMatriculaPeriodosEscolares           ejbMatriculaPeriodosEscolares;
    @EJB    EjbDistribucionInstalaciones            ejbDistribucionInstalaciones;
    
    public static final String REPORTE_DISTRIBUCION_EQUIPAMIENTO_PLANTILLA = "reporte_distribucion_equipamiento_plantilla.xlsx";
    public static final String REPORTE_DISTRIBUCION_EQUIPAMIENTO_COPIA = "reporte_distribucion_equipamiento_copia.xlsx";
    public static final String REPORTE_DISTRIBUCION_EQUIPAMIENTO_ACTUALIZADO = "reporte_distribucion_equipamiento_actualizado.xlsx";
    
    public static final String REPORTE_MATRICULA_PLANTILLA = "reporte_matricula_plantilla.xlsx";
    public static final String REPORTE_MATRICULA_COPIA = "reporte_matricula_copia.xlsx";
    public static final String REPORTE_MATRICULA_ACTUALIZADO = "reporte_matricula_actualizado.xlsx";
    
    public static final String REPORTE_DISTRIBUCION_INSTALACIONES_PLANTILLA = "reporte_distribucion_instalaciones_plantilla.xlsx";
    public static final String REPORTE_DISTRIBUCION_INSTALACIONES_COPIA = "reporte_distribucion_instalaciones_copia.xlsx";
    public static final String REPORTE_DISTRIBUCION_INSTALACIONES_ACTUALIZADO = "reporte_distribucion_instalaciones_actualizado.xlsx";
    
    @Getter private final String[] ejes = ServicioArchivos.EJES;

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    
    public String crearDirectorioReporte(String eje) {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(eje);
        return rutaPlantilla;
    }

    public String crearDirectorioReporteCompleto(String eje) {
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(eje);
        return rutaPlantillaC;
    }
    
    @Override
    public String getReporteDistribucionEquipamiento(Short ejercicioFiscal) throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[0]).concat(REPORTE_DISTRIBUCION_EQUIPAMIENTO_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[0]).concat(REPORTE_DISTRIBUCION_EQUIPAMIENTO_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[0]).concat(REPORTE_DISTRIBUCION_EQUIPAMIENTO_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteDistribucionEquipamiento = new XSSFWorkbook();
            reporteDistribucionEquipamiento = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaEquiposComputo = reporteDistribucionEquipamiento.getSheetAt(0);
            
            XSSFSheet hojaEquiposComputoInternet = reporteDistribucionEquipamiento.getSheetAt(1);

            XSSFRow fila;
            XSSFCell celda;

//            Contenido del documento
            
//            Vaciado de información proveniente de la consulta
            List<EquiposComputoCicloPeriodoEscolar> equipoComputo = ejbDistribucionEquipamiento.reporteEquiposComputoCicloPeriodoEscolares(ejercicioFiscal);
            List<EquiposComputoInternetCicloPeriodoEscolar> equipoComputoInternet = ejbDistribucionEquipamiento.reporteEquiposComputoInternetCicloPeriodoEscolares(ejercicioFiscal);
            
            for(Integer listaEqCom = 0; listaEqCom < equipoComputo.size(); listaEqCom++){
                Integer ubicacion = listaEqCom + 3;
                
                fila = (XSSFRow) (Row) hojaEquiposComputo.getRow(ubicacion);
                if(null == fila){
                    fila = hojaEquiposComputo.createRow(ubicacion);
                }
                
//                Inicia el vaciado de los catalogos en las celdas indicadas

                CiclosEscolares cicloEscolarTemporal = new CiclosEscolares();
                cicloEscolarTemporal = f.getEntityManager().find(CiclosEscolares.class, equipoComputo.get(listaEqCom).getCicloEscolar());
                
//                Ciclo Escolar
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.STRING);
                }
                fila.getCell(0).setCellValue(dateToCalendar(cicloEscolarTemporal.getInicio()).get(Calendar.YEAR) + "-" + dateToCalendar(cicloEscolarTemporal.getFin()).get(Calendar.YEAR));
                
                PeriodosEscolares periodoEscolarTemporal = new PeriodosEscolares();
                periodoEscolarTemporal = f.getEntityManager().find(PeriodosEscolares.class, equipoComputo.get(listaEqCom).getPeriodoEscolar());
                
//                Periodo Escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(periodoEscolarTemporal.getMesInicio().getMes()+"-"+periodoEscolarTemporal.getMesFin().getMes());
                
//                Ejercicio
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(equipoComputo.get(listaEqCom).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(equipoComputo.get(listaEqCom).getRegistros().getEventoRegistro().getMes());

//                Total Escritorio Suma
                Integer totalEscritorioEC = equipoComputo.get(listaEqCom).getDtcEsc()+equipoComputo.get(listaEqCom).getDaEsc()+equipoComputo.get(listaEqCom).getEstEsc()+equipoComputo.get(listaEqCom).getAdmEsc()+equipoComputo.get(listaEqCom).getMmsEsc();
//                Total Portatiles Suma
                Integer totalPortatilesEC = equipoComputo.get(listaEqCom).getDtcPort()+equipoComputo.get(listaEqCom).getDaPort()+equipoComputo.get(listaEqCom).getEstPor()+equipoComputo.get(listaEqCom).getAdmPort()+equipoComputo.get(listaEqCom).getMmsPort();
//                Total Suma
                Integer totalGeneralEC = totalEscritorioEC+totalPortatilesEC;
                
//                Total
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(totalGeneralEC);
                
//                Escritorio
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(totalEscritorioEC);
                
//                Portatiles
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(totalPortatilesEC);
                
//                Docentes de tiempo completo

//                D- Escritorio
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(equipoComputo.get(listaEqCom).getDtcEsc());
                
//                D- Portatiles
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(equipoComputo.get(listaEqCom).getDtcPort());
                
//                Docentes de asignatura

//                DA- Escritorio
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(equipoComputo.get(listaEqCom).getDaEsc());
                
//               DA- Portatiles 
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.NUMERIC);
                }
                fila.getCell(10).setCellValue(equipoComputo.get(listaEqCom).getDaPort());
                
//                Estudiantes

//                E-Escritorio
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.NUMERIC);
                }
                fila.getCell(11).setCellValue(equipoComputo.get(listaEqCom).getEstEsc());
                
//                E-Portatiles
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.NUMERIC);
                }
                fila.getCell(12).setCellValue(equipoComputo.get(listaEqCom).getEstPor());
                
//                Personal Administrativo

//                P-Escritorio
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.NUMERIC);
                }
                fila.getCell(13).setCellValue(equipoComputo.get(listaEqCom).getAdmEsc());
                
//                P-Portatiles
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.NUMERIC);
                }
                fila.getCell(14).setCellValue(equipoComputo.get(listaEqCom).getAdmPort());

//                Mandos Medios Y Superiores

//                M-Escritorio
                celda = fila.getCell(15);
                if (null == celda) {
                    fila.createCell(15, CellType.NUMERIC);
                }
                fila.getCell(15).setCellValue(equipoComputo.get(listaEqCom).getMmsEsc());
                
//                M-Portatiles
                celda = fila.getCell(16);
                if (null == celda) {
                    fila.createCell(16, CellType.NUMERIC);
                }
                fila.getCell(16).setCellValue(equipoComputo.get(listaEqCom).getMmsPort());   
            }
            
            for(Integer listaEqComInt = 0; listaEqComInt < equipoComputoInternet.size(); listaEqComInt++){
                Integer ubicacion = listaEqComInt + 3;
                
                fila = (XSSFRow) (Row) hojaEquiposComputoInternet.getRow(ubicacion);
                if(null == fila){
                    fila = hojaEquiposComputoInternet.createRow(ubicacion);
                }
                
//                Inicia el vaciado de los catalogos en las celdas indicadas

                CiclosEscolares cicloEscolarTemporal = new CiclosEscolares();
                cicloEscolarTemporal = f.getEntityManager().find(CiclosEscolares.class, equipoComputoInternet.get(listaEqComInt).getCicloEscolar());
                
//                Ciclo Escolar
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.STRING);
                }
                fila.getCell(0).setCellValue(dateToCalendar(cicloEscolarTemporal.getInicio()).get(Calendar.YEAR) + "-" + dateToCalendar(cicloEscolarTemporal.getFin()).get(Calendar.YEAR));
                
                PeriodosEscolares periodoEscolarTemporal = new PeriodosEscolares();
                periodoEscolarTemporal = f.getEntityManager().find(PeriodosEscolares.class, equipoComputoInternet.get(listaEqComInt).getPeriodoEscolar());
                
//                Periodo Escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(periodoEscolarTemporal.getMesInicio().getMes()+"-"+periodoEscolarTemporal.getMesFin().getMes());
                
//                Ejercicio
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(equipoComputoInternet.get(listaEqComInt).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(equipoComputoInternet.get(listaEqComInt).getRegistros().getEventoRegistro().getMes());

//                Total Escritorio Suma
                Integer totalEscritorioEC = equipoComputoInternet.get(listaEqComInt).getDtcEsc()+equipoComputoInternet.get(listaEqComInt).getDaEsc()+equipoComputoInternet.get(listaEqComInt).getEstEsc()+equipoComputoInternet.get(listaEqComInt).getAdmEsc()+equipoComputoInternet.get(listaEqComInt).getMmsEsc();
//                Total Portatiles Suma
                Integer totalPortatilesEC = equipoComputoInternet.get(listaEqComInt).getDtcPort()+equipoComputoInternet.get(listaEqComInt).getDaPort()+equipoComputoInternet.get(listaEqComInt).getEstPor()+equipoComputoInternet.get(listaEqComInt).getAdmPort()+equipoComputoInternet.get(listaEqComInt).getMmsPort();
//                Total Suma
                Integer totalGeneralEC = totalEscritorioEC+totalPortatilesEC;
                
//                Total
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(totalGeneralEC);
                
//                Escritorio
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(totalEscritorioEC);
                
//                Portatiles
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(totalPortatilesEC);
                
//                Docentes de tiempo completo

//                D- Escritorio
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(equipoComputoInternet.get(listaEqComInt).getDtcEsc());
                
//                D- Portatiles
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(equipoComputoInternet.get(listaEqComInt).getDtcPort());
                
//                Docentes de asignatura

//                DA- Escritorio
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(equipoComputoInternet.get(listaEqComInt).getDaEsc());
                
//               DA- Portatiles 
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.NUMERIC);
                }
                fila.getCell(10).setCellValue(equipoComputoInternet.get(listaEqComInt).getDaPort());
                
//                Estudiantes

//                E-Escritorio
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.NUMERIC);
                }
                fila.getCell(11).setCellValue(equipoComputoInternet.get(listaEqComInt).getEstEsc());
                
//                E-Portatiles
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.NUMERIC);
                }
                fila.getCell(12).setCellValue(equipoComputoInternet.get(listaEqComInt).getEstPor());
                
//                Personal Administrativo

//                P-Escritorio
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.NUMERIC);
                }
                fila.getCell(13).setCellValue(equipoComputoInternet.get(listaEqComInt).getAdmEsc());
                
//                P-Portatiles
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.NUMERIC);
                }
                fila.getCell(14).setCellValue(equipoComputoInternet.get(listaEqComInt).getAdmPort());

//                Mandos Medios Y Superiores

//                M-Escritorio
                celda = fila.getCell(15);
                if (null == celda) {
                    fila.createCell(15, CellType.NUMERIC);
                }
                fila.getCell(15).setCellValue(equipoComputoInternet.get(listaEqComInt).getMmsEsc());
                
//                M-Portatiles
                celda = fila.getCell(16);
                if (null == celda) {
                    fila.createCell(16, CellType.NUMERIC);
                }
                fila.getCell(16).setCellValue(equipoComputoInternet.get(listaEqComInt).getMmsPort());   
            }

//            Termina edición del documento
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteDistribucionEquipamiento.write(archivoSalida);
            reporteDistribucionEquipamiento.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioReporteEBExcel.getReporteDistribucionEquipamiento() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioReporteEBExcel.getReporteDistribucionEquipamiento() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getReporteMatriculaPorEjercicio(Short ejercicio) throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[0]).concat(REPORTE_MATRICULA_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[0]).concat(REPORTE_MATRICULA_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[0]).concat(REPORTE_MATRICULA_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteMatricula = new XSSFWorkbook();
            reporteMatricula = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaMatricula = reporteMatricula.getSheetAt(0);

            XSSFRow fila;
            XSSFCell celda;
            
//            Lista que se vaciará en la hoja de excel
            List<MatriculaPeriodosEscolares> matriculaPeriodoEscolar = ejbMatriculaPeriodosEscolares.getReporteMatriculaPorEjercicio(ejercicio);
            
            for(Integer listaMPE = 0; listaMPE < matriculaPeriodoEscolar.size(); listaMPE++){
                Integer ubicacion = listaMPE + 2;
                
                fila = (XSSFRow) (Row) hojaMatricula.getRow(ubicacion);
                if(null == fila){
                    fila = hojaMatricula.createRow(ubicacion);
                }
                
//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getRegistros().getEventoRegistro().getMes());
                
//                Matricula Periodo Escolar
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getMatricula());
                
                PeriodosEscolares periodoEscolar = new PeriodosEscolares();
                periodoEscolar = f.getEntityManager().find(PeriodosEscolares.class, matriculaPeriodoEscolar.get(listaMPE).getPeriodo());
                
//                Periodo Escolar
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(periodoEscolar.getMesInicio().getMes()+"-"+periodoEscolar.getMesFin().getMes());
                
                AreasUniversidad programaEducativo = new AreasUniversidad();
                programaEducativo = getProgramaEducativo(matriculaPeriodoEscolar.get(listaMPE).getProgramaEducativo());
                
//                Siglas
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(programaEducativo.getSiglas());
                
//                Nombre programa educativo
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(programaEducativo.getNombre());
                
//                Cuatrimestre
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getCuatrimestre());
                
//                Grupo
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getGrupo());
                
//                CURP
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getCurp());
                
//                Lengua indígena
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getLenguaIndigena());
                
//                Discapacidad
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(matriculaPeriodoEscolar.get(listaMPE).getDiscapacidad());
                
//                Fuente de información
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(getFuenteInformacion(matriculaPeriodoEscolar.get(listaMPE).getRegistros().getArea()));
                
            }

//            Termina edición del documento
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteMatricula.write(archivoSalida);
            reporteMatricula.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioReporteEBExcel.getReporteMatriculaPorEjercicio() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioReporteEBExcel.getReporteMatriculaPorEjercicio() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;

    }
    
    public AreasUniversidad getProgramaEducativo(Short area){
        return f.getEntityManager().find(AreasUniversidad.class, area);
    }
    
    public String getFuenteInformacion(Short areasUniversidad){
        return f.getEntityManager().find(AreasUniversidad.class, areasUniversidad).getNombre();
    }

    @Override
    public String getReportePorPeriodoEscolarDistribucionInstalaciones(Short ejercicio, AreasUniversidad areaUniversidad) throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[0]).concat(REPORTE_DISTRIBUCION_INSTALACIONES_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[0]).concat(REPORTE_DISTRIBUCION_INSTALACIONES_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[0]).concat(REPORTE_DISTRIBUCION_INSTALACIONES_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteDistribucionInstalaciones = new XSSFWorkbook();
            reporteDistribucionInstalaciones = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaCapacidadInstalada = reporteDistribucionInstalaciones.getSheetAt(0);
            XSSFSheet hojaDistribucionInstalaciones = reporteDistribucionInstalaciones.getSheetAt(1);
            XSSFSheet hojaDistribucionLaboratoriosTalleres = reporteDistribucionInstalaciones.getSheetAt(2);

            XSSFRow fila;
            XSSFCell celda;
            
            List<CapacidadInstaladaCiclosEscolares> capacidadInstalada = ejbDistribucionInstalaciones.getReporteCuatrimestralCapacidadInstaladaCicloPeriodosEscolares(ejercicio, areaUniversidad);
            List<DistribucionAulasCicloPeriodosEscolares> distribucionAulas = ejbDistribucionInstalaciones.getReporteCuatrimestralDistribucionAulas(ejercicio, areaUniversidad);
            List<DistribucionLabtallCicloPeriodosEscolares> distribucionLabTall = ejbDistribucionInstalaciones.getReporteCuatrimestralDistribucionLaboratoriosTalleres(ejercicio, areaUniversidad);
            
//            Capacidad Instalada
            for(Integer listaCI = 0; listaCI < capacidadInstalada.size(); listaCI++){
                Integer ubicacion = listaCI + 3;
                
                fila = (XSSFRow) (Row) hojaCapacidadInstalada.getRow(ubicacion);
                if(null == fila){
                    fila = hojaCapacidadInstalada.createRow(ubicacion);
                }
                
//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(capacidadInstalada.get(listaCI).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(capacidadInstalada.get(listaCI).getRegistros().getEventoRegistro().getMes());
                
                CiclosEscolares cicloEscolarTemporal = new CiclosEscolares();
                cicloEscolarTemporal = f.getEntityManager().find(CiclosEscolares.class, capacidadInstalada.get(listaCI).getCicloEscolar());
                
//                Ciclo Escolar
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(dateToCalendar(cicloEscolarTemporal.getInicio()).get(Calendar.YEAR) + "-" + dateToCalendar(cicloEscolarTemporal.getFin()).get(Calendar.YEAR));
                
//                Descripción
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(capacidadInstalada.get(listaCI).getInstalacion().getDescripcion());
                
//                Capacidad
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(capacidadInstalada.get(listaCI).getInstalacion().getCapacidad());
                
//                Unidades
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(capacidadInstalada.get(listaCI).getUnidades());
                
//                Fuente de información
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(getFuenteInformacion(capacidadInstalada.get(listaCI).getRegistros().getArea()));
            }
            
//            Distribución de aulas
            for(Integer listaDA = 0; listaDA < distribucionAulas.size(); listaDA++){
                Integer ubicacion = listaDA + 3;
                
                fila = (XSSFRow) (Row) hojaDistribucionInstalaciones.getRow(ubicacion);
                if(null == fila){
                    fila = hojaDistribucionInstalaciones.createRow(ubicacion);
                }
                
//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(distribucionAulas.get(listaDA).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(distribucionAulas.get(listaDA).getRegistros().getEventoRegistro().getMes());
                
                CiclosEscolares cicloEscolarTemporal = new CiclosEscolares();
                cicloEscolarTemporal = f.getEntityManager().find(CiclosEscolares.class, distribucionAulas.get(listaDA).getCicloEscolar());
                
//                Ciclo Escolar
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(dateToCalendar(cicloEscolarTemporal.getInicio()).get(Calendar.YEAR) + "-" + dateToCalendar(cicloEscolarTemporal.getFin()).get(Calendar.YEAR));
                
                PeriodosEscolares periodoEscolarDA = new PeriodosEscolares();
                periodoEscolarDA = f.getEntityManager().find(PeriodosEscolares.class, distribucionAulas.get(listaDA).getPeriodoEscolar());
                
//                Periodo Escolar
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(periodoEscolarDA.getMesInicio().getMes()+"-"+periodoEscolarDA.getMesFin().getMes());
                
//                Nombre
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(distribucionAulas.get(listaDA).getAula().getNombre());
                
//                Capacidad Turno
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(distribucionAulas.get(listaDA).getAula().getCapacidadTurno());
                
//                Número
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(distribucionAulas.get(listaDA).getNumero());
                
//                Acondicionados
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(distribucionAulas.get(listaDA).getAcondicionadas());
                
//                Fuente de información
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(getFuenteInformacion(distribucionAulas.get(listaDA).getRegistros().getArea()));
            }
            
//            Distribución de laboratorios y talleres
            for(Integer listaDLT = 0; listaDLT < distribucionLabTall.size(); listaDLT++){
                Integer ubicacion = listaDLT + 3;
                
                fila = (XSSFRow) (Row) hojaDistribucionLaboratoriosTalleres.getRow(ubicacion);
                if(null == fila){
                    fila = hojaDistribucionLaboratoriosTalleres.createRow(ubicacion);
                }
                
//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(distribucionLabTall.get(listaDLT).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(distribucionLabTall.get(listaDLT).getRegistros().getEventoRegistro().getMes());
                
                CiclosEscolares cicloEscolarTemporal = new CiclosEscolares();
                cicloEscolarTemporal = f.getEntityManager().find(CiclosEscolares.class, distribucionLabTall.get(listaDLT).getCicloEscolar());
                
//                Ciclo Escolar
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(dateToCalendar(cicloEscolarTemporal.getInicio()).get(Calendar.YEAR) + "-" + dateToCalendar(cicloEscolarTemporal.getFin()).get(Calendar.YEAR));
                
                PeriodosEscolares periodoEscolarDLT = new PeriodosEscolares();
                periodoEscolarDLT = f.getEntityManager().find(PeriodosEscolares.class, distribucionLabTall.get(listaDLT).getPeriodoEscolar());
                
//                Periodo Escolar
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(periodoEscolarDLT.getMesInicio().getMes()+"-"+periodoEscolarDLT.getMesFin().getMes());
                
//                Aula
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(distribucionLabTall.get(listaDLT).getAulaTipo().getNombre());
                
//                Nombre
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(distribucionLabTall.get(listaDLT).getNombre());
                
//                Capacidad
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(distribucionLabTall.get(listaDLT).getCapacidad());
                
//                Área responsable
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(getFuenteInformacion(distribucionLabTall.get(listaDLT).getAreaResponsable()));
                
//                Fecha de habilitación
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(distribucionLabTall.get(listaDLT).getFechaHabilitacion()));
                
//                Fuente de información
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(getFuenteInformacion(distribucionLabTall.get(listaDLT).getRegistros().getArea()));
                
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteDistribucionInstalaciones.write(archivoSalida);
            reporteDistribucionInstalaciones.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioReporteEBExcel.getReportePorPeriodoEscolarDistribucionInstalaciones() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioReporteEBExcel.getReportePorPeriodoEscolarDistribucionInstalaciones() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }
    
}
