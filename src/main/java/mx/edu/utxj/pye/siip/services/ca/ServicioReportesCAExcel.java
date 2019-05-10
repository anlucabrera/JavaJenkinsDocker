/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

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
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasMensualPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActividadesVarias;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCiclosPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbReportesCAExcel;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbServiciosEnfermeriaCicloPeriodos;
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
public class ServicioReportesCAExcel implements EjbReportesCAExcel{

    @EJB    EjbCarga                                ejbCarga;
    @EJB    EjbCatalogos                            ejbCatalogos;
    @EJB    EjbAsesoriasTutoriasCiclosPeriodos      ejbAsesoriasTutoriasCiclosPeriodos;
    @EJB    EjbActividadesVarias                    ejbActividadesVarias;
    @EJB    EjbServiciosEnfermeriaCicloPeriodos     ejbServiciosEnfermeriaCicloPeriodos;
    @EJB    Facade                                  f;
    
    public static final String REPORTE_ACTIVIDADES_VARIAS_PLANTILLA = "reporte_actividades_varias_plantilla.xlsx";
    public static final String REPORTE_ACTIVIDADES_VARIAS_COPIA = "reporte_actividades_varias_copia.xlsx";
    public static final String REPORTE_ACTIVIDADES_VARIAS_ACTUALIZADO = "reporte_actividades_varias_actualizado.xlsx";
    
    public static final String REPORTE_ASESORIAS_TUTORIAS_PLANTILLA = "reporte_asesorias_tutorias_plantilla.xlsx";
    public static final String REPORTE_ASESORIAS_TUTORIAS_COPIA = "reporte_asesorias_tutorias_copia.xlsx";
    public static final String REPORTE_ASESORIAS_TUTORIAS_ACTUALIZADO = "reporte_asesorias_tutorias_actualizado.xlsx";
    
    public static final String REPORTE_SERVICIOS_ENFERMERIA_PLANTILLA = "reporte_servicios_enfermeria_plantilla.xlsx";
    public static final String REPORTE_SERVICIOS_ENFERMERIA_COPIA = "reporte_servicios_enfermeria_copia.xlsx";
    public static final String REPORTE_SERVICIOS_ENFERMERIA_ACTUALIZADO = "reporte_servicios_enfermeria_actualizado.xlsx";
    
    @Getter private final String[] ejes = ServicioArchivos.EJES;

    public String crearDirectorioReporte(String eje) {
        String rutaPlantilla = ejbCarga.crearDirectorioReporte(eje);
        return rutaPlantilla;
    }

    public String crearDirectorioReporteCompleto(String eje) {
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompleto(eje);
        return rutaPlantillaC;
    }
    
    @Override
    public String getReporteActividadesVarias() throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[1]).concat(REPORTE_ACTIVIDADES_VARIAS_PLANTILLA);
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[1]).concat(REPORTE_ACTIVIDADES_VARIAS_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[1]).concat(REPORTE_ACTIVIDADES_VARIAS_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteActividadesVarias = new XSSFWorkbook();
            reporteActividadesVarias = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet catalogos = reporteActividadesVarias.getSheetAt(0);
            XSSFRow fila;
            XSSFCell celda;
            
//            Vaciado de información proveniente de la consulta
            List<ActividadesVariasRegistro> actividadesVariasReporte = ejbActividadesVarias.reporteActividadesVariasPorEjercicio();
            
            for(Integer listaActVar = 0; listaActVar < actividadesVariasReporte.size(); listaActVar++){
                Integer ubicacion = listaActVar + 2;
                
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if(null == fila){
                    fila = catalogos.createRow(ubicacion);
                }
                
//                Inicia el vaciado de los catalogos en las celdas indicadas

//                Registro
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(actividadesVariasReporte.get(listaActVar).getRegistro());
                
//                Año
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(actividadesVariasReporte.get(listaActVar).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(actividadesVariasReporte.get(listaActVar).getRegistros().getEventoRegistro().getMes());

//                Búsqueda de área temporal
                AreasUniversidad areaTemp = new AreasUniversidad();
                areaTemp = f.getEntityManager().find(AreasUniversidad.class, actividadesVariasReporte.get(listaActVar).getRegistros().getArea());
                
//                Siglas
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(areaTemp.getSiglas());
                
//                Tipo
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(areaTemp.getCategoria().getDescripcion());
                
//                Nombre del área
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(areaTemp.getNombre());
                
//                Nombre de la actividad
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(actividadesVariasReporte.get(listaActVar).getNombre());
                
//                Fecha de inicio
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(actividadesVariasReporte.get(listaActVar).getFechaInicio()));
                
//                Fecha de Fin
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(actividadesVariasReporte.get(listaActVar).getFechaFin()));
                
//                Lugar de realización
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(actividadesVariasReporte.get(listaActVar).getLugar());
                
//                Objetivo
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(actividadesVariasReporte.get(listaActVar).getObjetivo());
                
//                Impacto ó beneficio
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(actividadesVariasReporte.get(listaActVar).getImpactoBeneficio());
                
                
//                Total hombres
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(actividadesVariasReporte.get(listaActVar).getTotalHombres());
                
//                Total mujeres
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.STRING);
                }
                fila.getCell(13).setCellValue(actividadesVariasReporte.get(listaActVar).getTotalMujeres());
                
//                Personalidades
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.STRING);
                }
                fila.getCell(14).setCellValue(actividadesVariasReporte.get(listaActVar).getPersonalidades());
                
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteActividadesVarias.write(archivoSalida);
            reporteActividadesVarias.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioReportesCAExcel.getReporteActividadesVarias() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioReportesCAExcel.getReporteActividadesVarias() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }
    
    @Override
    public String getReporteAsesoriasTutorias() throws Throwable { 
        String plantilla = crearDirectorioReporte(ejes[1]).concat(REPORTE_ASESORIAS_TUTORIAS_PLANTILLA);
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[1]).concat(REPORTE_ASESORIAS_TUTORIAS_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[1]).concat(REPORTE_ASESORIAS_TUTORIAS_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteAsesoriasTutorias = new XSSFWorkbook();
            reporteAsesoriasTutorias = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet catalogos = reporteAsesoriasTutorias.getSheetAt(0);
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Vaciado de información proveniente de la consulta
            List<AsesoriasTutoriasMensualPeriodosEscolares> asesoriasTutorias = ejbAsesoriasTutoriasCiclosPeriodos.getListaReporteGeneralAsesoriasTutorias();
            
            System.err.println("asesoriasTutorias.size(): " + asesoriasTutorias.size());
            
            for(Integer listaAsesTut = 0; listaAsesTut < asesoriasTutorias.size(); listaAsesTut++){
                Integer ubicacion = listaAsesTut + 2;
                
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if(null == fila){
                    fila = catalogos.createRow(ubicacion);
                }
                
//                Inicia el vaciado de los catalogos en las celdas indicadas

                PeriodosEscolares periodoEscolarTemporal = new PeriodosEscolares();
                periodoEscolarTemporal = f.getEntityManager().find(PeriodosEscolares.class, asesoriasTutorias.get(listaAsesTut).getPeriodoEscolar());

//                Periodo Escolar
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.STRING);
                }
                fila.getCell(0).setCellValue(periodoEscolarTemporal.getMesInicio().getMes()+"-"+periodoEscolarTemporal.getMesFin().getMes());
                
//                Ejercicio
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(asesoriasTutorias.get(listaAsesTut).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(asesoriasTutorias.get(listaAsesTut).getMes());
                
                AreasUniversidad programaEducativoTemp = new AreasUniversidad();
                programaEducativoTemp = f.getEntityManager().find(AreasUniversidad.class, asesoriasTutorias.get(listaAsesTut).getProgramaEducativo());
                
//                Programa Educativo
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(programaEducativoTemp.getNombre());
                
//                Cuatrimestre
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(asesoriasTutorias.get(listaAsesTut).getCuatrimestre());
                
//                Grupo
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(asesoriasTutorias.get(listaAsesTut).getGrupo());
                
//                Tipo Actividad
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(asesoriasTutorias.get(listaAsesTut).getTipoActividad());
                
//                Tipo
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(asesoriasTutorias.get(listaAsesTut).getTipo());
                
//                Número de tutorías y asesorías
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(asesoriasTutorias.get(listaAsesTut).getNoTutoriasAsesorias());
                
//                Asistentes Hombres
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(asesoriasTutorias.get(listaAsesTut).getAsistentesHombres());
                
//                Asistentes Mujeres    
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.NUMERIC);
                }
                fila.getCell(10).setCellValue(asesoriasTutorias.get(listaAsesTut).getAsistentesMujeres());
                
//                Fecha de Registro    
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(asesoriasTutorias.get(listaAsesTut).getRegistros().getFechaRegistro()));
                
                AreasUniversidad areaAcademicaTemp = new AreasUniversidad();
                areaAcademicaTemp = f.getEntityManager().find(AreasUniversidad.class, asesoriasTutorias.get(listaAsesTut).getRegistros().getArea());
                
//                Área académica
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(areaAcademicaTemp.getNombre());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteAsesoriasTutorias.write(archivoSalida);
            reporteAsesoriasTutorias.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioReportesCAExcel.getReporteAsesoriasTutorias() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioReportesCAExcel.getReporteAsesoriasTutorias() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getReporteServiciosEnfermeria() throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[1]).concat(REPORTE_SERVICIOS_ENFERMERIA_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[1]).concat(REPORTE_SERVICIOS_ENFERMERIA_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[1]).concat(REPORTE_SERVICIOS_ENFERMERIA_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteServiciosEnfermeria = new XSSFWorkbook();
            reporteServiciosEnfermeria = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet catalogos = reporteServiciosEnfermeria.getSheetAt(0);
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Vaciado de información proveniente de la consulta
            List<ServiciosEnfermeriaCicloPeriodos> serviciosEnfermeria = ejbServiciosEnfermeriaCicloPeriodos.getReporteGeneralEjercicioServiciosEnfermeria();

            for(Integer listaServEnf = 0; listaServEnf < serviciosEnfermeria.size(); listaServEnf++){
                Integer ubicacion = listaServEnf + 3;
                
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if(null == fila){
                    fila = catalogos.createRow(ubicacion);
                }
                
//                Inicia el vaciado de los catalogos en las celdas indicadas
                
                CiclosEscolares cicloEscolarTemporal = new CiclosEscolares();
                cicloEscolarTemporal = f.getEntityManager().find(CiclosEscolares.class, serviciosEnfermeria.get(listaServEnf).getCicloEscolar());
                
//                Ciclo Escolar
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.STRING);
                }
                fila.getCell(0).setCellValue(dateToCalendar(cicloEscolarTemporal.getInicio()).get(Calendar.YEAR) + "-" + dateToCalendar(cicloEscolarTemporal.getFin()).get(Calendar.YEAR));
                
                PeriodosEscolares periodoEscolarTemporal = new PeriodosEscolares();
                periodoEscolarTemporal = f.getEntityManager().find(PeriodosEscolares.class, serviciosEnfermeria.get(listaServEnf).getPeriodoEscolar());

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
                fila.getCell(2).setCellValue(serviciosEnfermeria.get(listaServEnf).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(serviciosEnfermeria.get(listaServEnf).getMes());
                
//                Servicios de enfermería
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(serviciosEnfermeria.get(listaServEnf).getServicio().getDescripcion());
                
//                Estudiantes Atendidos Hombres
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(serviciosEnfermeria.get(listaServEnf).getEstH());
                
//                Estudiante Atendidos Mujeres
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(serviciosEnfermeria.get(listaServEnf).getEstM());
                
//                Personal Atendido Hombres
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(serviciosEnfermeria.get(listaServEnf).getPerH());
                
//                Personal Atendidos Mujeres
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(serviciosEnfermeria.get(listaServEnf).getPerM());
                
                AreasUniversidad areaRegistroTemp = new AreasUniversidad();
                areaRegistroTemp = f.getEntityManager().find(AreasUniversidad.class, serviciosEnfermeria.get(listaServEnf).getRegistros().getArea());
                
//                Área que registra - Fuente de información
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(areaRegistroTemp.getNombre());
                
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteServiciosEnfermeria.write(archivoSalida);
            reporteServiciosEnfermeria.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioReportesCAExcel.getReporteServiciosEnfermeria() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.ca.ServicioReportesCAExcel.getReporteServiciosEnfermeria() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }
    
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
