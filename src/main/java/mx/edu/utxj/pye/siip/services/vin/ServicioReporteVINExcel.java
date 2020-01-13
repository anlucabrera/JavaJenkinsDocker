/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ContactosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.CorreosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.TelefonosEmpresa;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadesVinculacion;
import mx.edu.utxj.pye.siip.dto.vin.DTOProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbConvenios;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbReportesVINExcel;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbServiciosTecnologicosAnioMes;
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
public class ServicioReporteVINExcel implements EjbReportesVINExcel{

    @EJB    EjbCarga        ejbCarga;
    @EJB    EjbCatalogos    ejbCatalogos;
    @EJB    Facade          f;
    
    @EJB    EjbConvenios                        ejbConvenios;
    @EJB    EjbOrganismosVinculados             ejbOrganismosVinculados;
    @EJB    EjbServiciosTecnologicosAnioMes     ejbServiciosTecnologicosAnioMes;
    @EJB    EjbEgresados                        ejbEgresados;
    
    public static final String REPORTE_CONVENIOS_PLANTILLA = "reporte_convenios_plantilla.xlsx";
    public static final String REPORTE_CONVENIOS_COPIA = "reporte_convenios_copia.xlsx";
    public static final String REPORTE_CONVENIOS_ACTUALIZADO = "reporte_convenios_actualizado.xlsx";
    
    public static final String REPORTE_SERVICIOS_TECNOLOGICOS_PLANTILLA = "reporte_servicios_tecnologicos_plantilla.xlsx";
    public static final String REPORTE_SERVICIOS_TECNOLOGICOS_COPIA = "reporte_servicios_tecnologicos_copia.xlsx";
    public static final String REPORTE_SERVICIOS_TECNOLOGICOS_ACTUALIZADO = "reporte_servicios_tecnologicos_actualizado.xlsx";
    
    public static final String REPORTE_EGRESADOS_PLANTILLA = "reporte_egresados_plantilla.xlsx";
    public static final String REPORTE_EGRESADOS_COPIA = "reporte_egresados_copia.xlsx";
    public static final String REPORTE_EGRESADOS_ACTUALIZADO = "reporte_egresados_actualizado.xlsx";
    
    public static final String REPORTE_ORGANISMOS_VINCULADOS_PLANTILLA = "reporte_organismos_vinculados_plantilla.xlsx";
    public static final String REPORTE_ORGANISMOS_VINCULADOS_COPIA = "reporte_organismos_vinculados_copia.xlsx";
    public static final String REPORTE_ORGANISMOS_VINCULADOS_ACTUALIZADO = "reporte_organismos_vinculados_actualizado.xlsx";
    
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
    public String getReporteConvenios() throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[2]).concat(REPORTE_CONVENIOS_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_CONVENIOS_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_CONVENIOS_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteConvenios = new XSSFWorkbook();
            reporteConvenios = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaConvenios = reporteConvenios.getSheetAt(0);

            XSSFRow fila;
            XSSFCell celda;
            
//            Vaciado de información proveniente de la consulta
            List<Convenios> convenios = ejbConvenios.getReporteGeneralConvenios();
            
            for(Integer listaC = 0; listaC < convenios.size(); listaC++){
                Integer ubicacion = listaC + 3;
                
                fila = (XSSFRow) (Row) hojaConvenios.getRow(ubicacion);
                if(null == fila){
                    fila = hojaConvenios.createRow(ubicacion);
                }
                
//                Inicia el vaciado de los catalogos en las celdas indicadas

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(convenios.get(listaC).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(convenios.get(listaC).getRegistros().getEventoRegistro().getMes());
                
//                Fecha Firma
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(convenios.get(listaC).getFechaFirma()));
                
//                Vigencia
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(convenios.get(listaC).getVigencia()));
                
//                Estudiantes Beneficiados

//                EB-Hombres
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.NUMERIC);
                }
                fila.getCell(4).setCellValue(convenios.get(listaC).getEbh());
                
//                EB-Mujeres
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(convenios.get(listaC).getEbm());
                
//                Docentes beneficiados

//                DB-Hombres
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(convenios.get(listaC).getDbh());
                
//                DB-Mujeres
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(convenios.get(listaC).getDbm());
                
//                Recursos Obtenidos
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(convenios.get(listaC).getRecursosObtenidos());
                
                OrganismosVinculados orgVin = ejbOrganismosVinculados.getOrganismoVinculadoPorEmpresa(convenios.get(listaC).getEmpresa().getEmpresa());
                
//                Empresa
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(orgVin.getNombre());
                
//                Objetivo
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(convenios.get(listaC).getObjetivo());
                
//                Descripción
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(convenios.get(listaC).getDescripcion());
                
//                Impacto
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(convenios.get(listaC).getImpacto());
            }
//            Termina edición del documento
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteConvenios.write(archivoSalida);
            reporteConvenios.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteConvenios() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteConvenios() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getReporteServiciosTecnologicos() throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[2]).concat(REPORTE_SERVICIOS_TECNOLOGICOS_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_SERVICIOS_TECNOLOGICOS_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_SERVICIOS_TECNOLOGICOS_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteServiciosTecnologicos = new XSSFWorkbook();
            reporteServiciosTecnologicos = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaServiciosTecnologicos = reporteServiciosTecnologicos.getSheetAt(0);
            XSSFSheet hojaServiciosTecnologicosParticipantes = reporteServiciosTecnologicos.getSheetAt(1);

            XSSFRow fila;
            XSSFCell celda;
            
//            Vaciado de información proveniente de la consulta
            List<ServiciosTecnologicosAnioMes> servTec = ejbServiciosTecnologicosAnioMes.getReporteGeneralServiciosTecnologicos();
            List<ServiciosTecnologicosParticipantes> servTecPart = ejbServiciosTecnologicosAnioMes.getReporteGeneralServiciosTecnologicoParticipantes();
            
            for(Integer listaST = 0; listaST < servTec.size(); listaST++){
                Integer ubicacion = listaST + 2;
                
                fila = (XSSFRow) (Row) hojaServiciosTecnologicos.getRow(ubicacion);
                if(null == fila){
                    fila = hojaServiciosTecnologicos.createRow(ubicacion);
                }
                
//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(servTec.get(listaST).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(servTec.get(listaST).getRegistros().getEventoRegistro().getMes());
                
//                Servicio Tecnológico
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(servTec.get(listaST).getServicio());
                
//                Tipo Servicio Tecnologico
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(servTec.get(listaST).getServicioTipo().getDescripcion());
                
//                Nombre Servicio Tecnológico
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(servTec.get(listaST).getNombre());
                
//                Fecha de Inicio
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(servTec.get(listaST).getFechaInicio()));
                
//                Fecha de Termino
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(servTec.get(listaST).getFechaTermino()));
                
//                Duración
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(servTec.get(listaST).getDuracion());
                
//                Monto Ingresado
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue("$ " + servTec.get(listaST).getMontoIngresado());
                
//                Facilitador
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(servTec.get(listaST).getFacilitador());
                
//                Servicio Demandado
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(servTec.get(listaST).getServicioDemandado());
            }
            
//            Servicios Tecnológicos Participantes
            
            for (Integer listaSTP = 0; listaSTP < servTecPart.size(); listaSTP++) {
                Integer ubicacion = listaSTP + 2;

                fila = (XSSFRow) (Row) hojaServiciosTecnologicosParticipantes.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaServiciosTecnologicosParticipantes.createRow(ubicacion);
                }

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(servTecPart.get(listaSTP).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(servTecPart.get(listaSTP).getRegistros().getEventoRegistro().getMes());
                
//                Servicio Tecnológico
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(servTecPart.get(listaSTP).getServicioTecnologico().getServicio());
                
//                Tipo Servicio
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(servTecPart.get(listaSTP).getServicioTecnologico().getServicioTipo().getDescripcion());
                
//                Servicio Tecnológico
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(servTecPart.get(listaSTP).getServicioTecnologico().getNombre());
                
//                Nombre Participante
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(servTecPart.get(listaSTP).getNombre());
                
//                Sexo
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(servTecPart.get(listaSTP).getSexo());
                
//                Edad
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(servTecPart.get(listaSTP).getEdad());
                
//                Estado
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(servTecPart.get(listaSTP).getMunicipio().getEstado().getNombre());
                
//                Municipio
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(servTecPart.get(listaSTP).getMunicipio().getNombre());
                
//                Lengua Índigena
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(servTecPart.get(listaSTP).getLenguaIndigena());
                
//                Discapacidad
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(servTecPart.get(listaSTP).getDiscapacidad());

//                Empresa
                if(servTecPart.get(listaSTP).getEmpresa() != null){
                    celda = fila.getCell(12);
                    if (null == celda) {
                        fila.createCell(12, CellType.STRING);
                    }
                    fila.getCell(12).setCellValue(servTecPart.get(listaSTP).getEmpresa().getNombre());
                }
                
//                Generacion
                if(servTecPart.get(listaSTP).getGeneracion() != null){
                    celda = fila.getCell(13);
                    if (null == celda) {
                        fila.createCell(13, CellType.STRING);
                    }
                    fila.getCell(13).setCellValue(getGeneracion(servTecPart.get(listaSTP).getGeneracion()));
                }
                
//                Programa Educativo
                if(servTecPart.get(listaSTP).getProgramaEducativo() != null){
                    celda = fila.getCell(14);
                    if (null == celda) {
                        fila.createCell(14, CellType.STRING);
                    }
                    fila.getCell(14).setCellValue(getFuenteInformacion(servTecPart.get(listaSTP).getProgramaEducativo()));
                }
                
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteServiciosTecnologicos.write(archivoSalida);
            reporteServiciosTecnologicos.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteServiciosTecnologicos() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteServiciosTecnologicos() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getReporteGeneralEgresados() throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[2]).concat(REPORTE_EGRESADOS_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_EGRESADOS_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_EGRESADOS_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteEgresados = new XSSFWorkbook();
            reporteEgresados = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaActividadEgresado = reporteEgresados.getSheetAt(0);
            XSSFSheet hojaActividadEconomicaEgresado = reporteEgresados.getSheetAt(1);
            XSSFSheet hojaNivelOcupacionEgresado = reporteEgresados.getSheetAt(2);
            XSSFSheet hojaNivelIngresosEgresado = reporteEgresados.getSheetAt(3);

            XSSFRow fila;
            XSSFCell celda;
            
//            Listas que contienen la información para el llenado en las hojas de excel
            List<ActividadEgresadoGeneracion> actividadEgresado = ejbEgresados.getReporteActividadEgresadoPorEjercicio();
            List<ActividadEconomicaEgresadoGeneracion> actividadEconomicaEgresado = ejbEgresados.getReporteActividadEconomicaEgresadoPorEjercicio();
            List<NivelOcupacionEgresadosGeneracion> nivelOcupacionEgresado = ejbEgresados.getReporteNivelOcupacionEgresadoPorEjercicio();
            List<NivelIngresosEgresadosGeneracion> nivelIngresosEgresadosGeneracions = ejbEgresados.getReporteNivelIngresosEgresadoPorEjercicio();

//            Actividad del Egresado

            for (Integer listaAE = 0; listaAE < actividadEgresado.size(); listaAE++) {
                Integer ubicacion = listaAE + 2;

                fila = (XSSFRow) (Row) hojaActividadEgresado.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaActividadEgresado.createRow(ubicacion);
                }

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(actividadEgresado.get(listaAE).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(actividadEgresado.get(listaAE).getRegistros().getEventoRegistro().getMes());
                
//                Fecha de Corte
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(actividadEgresado.get(listaAE).getFecha()));
                
//                Generacion
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(getGeneracion(actividadEgresado.get(listaAE).getGeneracion()));
                
//                Programa Educativo
                AreasUniversidad programaEducativo = new AreasUniversidad();
                programaEducativo = getProgramaEducativo(actividadEgresado.get(listaAE).getProgramaEducativo());
                
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
                
//                Tipo de egresado
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(actividadEgresado.get(listaAE).getActividad().getDescripcion());
                
//                Hombres
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(actividadEgresado.get(listaAE).getHombres());
                
//                Mujeres
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(actividadEgresado.get(listaAE).getMujeres());
                
//                Total
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(actividadEgresado.get(listaAE).getHombres()+actividadEgresado.get(listaAE).getMujeres());
                
//                Fuente de información
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(getFuenteInformacion(actividadEgresado.get(listaAE).getRegistros().getArea()));
                
            }

//            Actividad Económica del Egresado

            for (Integer listaAEE = 0; listaAEE < actividadEconomicaEgresado.size(); listaAEE++) {
                Integer ubicacion = listaAEE + 2;

                fila = (XSSFRow) (Row) hojaActividadEconomicaEgresado.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaActividadEconomicaEgresado.createRow(ubicacion);
                }

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(actividadEconomicaEgresado.get(listaAEE).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(actividadEconomicaEgresado.get(listaAEE).getRegistros().getEventoRegistro().getMes());
                
//                Fecha de Corte
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(actividadEconomicaEgresado.get(listaAEE).getFecha()));
                
//                Generacion
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(getGeneracion(actividadEconomicaEgresado.get(listaAEE).getGeneracion()));
                
                AreasUniversidad programaEducativo = new AreasUniversidad();
                programaEducativo = getProgramaEducativo(actividadEconomicaEgresado.get(listaAEE).getProgramaEducativo());
                
//                Siglas
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(programaEducativo.getSiglas());
                
//                Programa Educativo
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(programaEducativo.getNombre());
                
//                Sector
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(actividadEconomicaEgresado.get(listaAEE).getSector().getDescripcion());
                
//                Cantidad sector
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(actividadEconomicaEgresado.get(listaAEE).getCantSector());
                
//                Giro
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(actividadEconomicaEgresado.get(listaAEE).getGiro().getDescripcion());
                
//                Cantidad Giro
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(actividadEconomicaEgresado.get(listaAEE).getCantGiro());
                
//                Fuente de información
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(getFuenteInformacion(actividadEconomicaEgresado.get(listaAEE).getRegistros().getArea()));
            }

//            Nivel de Ocupación del Egresado
            for (Integer listaNOE = 0; listaNOE < nivelOcupacionEgresado.size(); listaNOE++) {
                Integer ubicacion = listaNOE + 2;

                fila = (XSSFRow) (Row) hojaNivelOcupacionEgresado.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaNivelOcupacionEgresado.createRow(ubicacion);
                }

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(nivelOcupacionEgresado.get(listaNOE).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(nivelOcupacionEgresado.get(listaNOE).getRegistros().getEventoRegistro().getMes());
                
//                Fecha de Corte
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(nivelOcupacionEgresado.get(listaNOE).getFecha()));
                
//                Generación
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(getGeneracion(nivelOcupacionEgresado.get(listaNOE).getGeneracion()));
                
                AreasUniversidad programaEducativo = new AreasUniversidad();
                programaEducativo = getProgramaEducativo(nivelOcupacionEgresado.get(listaNOE).getProgramaEducativo());
                
//                Siglas
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(programaEducativo.getSiglas());
                
//                Programa Educativo
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(programaEducativo.getNombre());
                
//                Nivel Ocupación
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(nivelOcupacionEgresado.get(listaNOE).getOcupacion().getDescripcion());
                
//                Hombres
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(nivelOcupacionEgresado.get(listaNOE).getHombres());
                
//                Mujeres
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(nivelOcupacionEgresado.get(listaNOE).getMujeres());
                
//                Total
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(nivelOcupacionEgresado.get(listaNOE).getHombres()+nivelOcupacionEgresado.get(listaNOE).getHombres());
                
//                Fuente de información
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(getFuenteInformacion(nivelOcupacionEgresado.get(listaNOE).getRegistros().getArea()));
            }

//            Nivel de Ingresos del Egresado

            for (Integer listaNIE = 0; listaNIE < nivelIngresosEgresadosGeneracions.size(); listaNIE++) {
                Integer ubicacion = listaNIE + 2;

                fila = (XSSFRow) (Row) hojaNivelIngresosEgresado.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaNivelIngresosEgresado.createRow(ubicacion);
                }

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(nivelIngresosEgresadosGeneracions.get(listaNIE).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(nivelIngresosEgresadosGeneracions.get(listaNIE).getRegistros().getEventoRegistro().getMes());
                
//                Fecha de Corte
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(nivelIngresosEgresadosGeneracions.get(listaNIE).getFecha()));
                
//                Generacion
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(getGeneracion(nivelIngresosEgresadosGeneracions.get(listaNIE).getGeneracion()));
                
                AreasUniversidad programaEducativo = new AreasUniversidad();
                programaEducativo = getProgramaEducativo(nivelIngresosEgresadosGeneracions.get(listaNIE).getProgramaEducativo());
                
//                Siglas
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(programaEducativo.getSiglas());
                
//                Nombre
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(programaEducativo.getNombre());
                
//                Nivel de Ingresos
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(nivelIngresosEgresadosGeneracions.get(listaNIE).getIngreso().getIngresos());
                
//                Hombres
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.NUMERIC);
                }
                fila.getCell(7).setCellValue(nivelIngresosEgresadosGeneracions.get(listaNIE).getHombres());
                
//                Mujeres
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(nivelIngresosEgresadosGeneracions.get(listaNIE).getMujeres());
                
//                Total
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.NUMERIC);
                }
                fila.getCell(9).setCellValue(nivelIngresosEgresadosGeneracions.get(listaNIE).getHombres()+nivelIngresosEgresadosGeneracions.get(listaNIE).getMujeres());
                
//                Fuente de información
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(getFuenteInformacion(nivelIngresosEgresadosGeneracions.get(listaNIE).getRegistros().getArea()));
                
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteEgresados.write(archivoSalida);
            reporteEgresados.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteGeneralEgresados() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteGeneralEgresados() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }
    
    public String getGeneracion(Short generacionParametro){
        Generaciones generacion = new Generaciones();        
        generacion = f.getEntityManager().find(Generaciones.class,generacionParametro);
        return generacion.getInicio()+"-"+generacion.getFin();
    }
    
    public AreasUniversidad getProgramaEducativo(Short area){
        return f.getEntityManager().find(AreasUniversidad.class, area);
    }
    
    public String getFuenteInformacion(Short areasUniversidad){
        return f.getEntityManager().find(AreasUniversidad.class, areasUniversidad).getNombre();
    }

    @Override
    public String getReporteOrganismosVinculados() throws Throwable {
        String plantilla = crearDirectorioReporte(ejes[2]).concat(REPORTE_ORGANISMOS_VINCULADOS_PLANTILLA );
        String plantillaCopia = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_ORGANISMOS_VINCULADOS_COPIA);
        String plantillaCompleto = crearDirectorioReporteCompleto(ejes[2]).concat(REPORTE_ORGANISMOS_VINCULADOS_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();
            XSSFWorkbook reporteOrganismosVinculados = new XSSFWorkbook();
            reporteOrganismosVinculados = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);
            XSSFSheet hojaOrganismosVinculados = reporteOrganismosVinculados.getSheetAt(0);
            XSSFSheet hojaActividadesVinculacion = reporteOrganismosVinculados.getSheetAt(1);
            XSSFSheet hojaProgramasBeneficiados = reporteOrganismosVinculados.getSheetAt(2);
            XSSFSheet hojaTelefonosEmpresa = reporteOrganismosVinculados.getSheetAt(3);
            XSSFSheet hojaCorreosEmpresa = reporteOrganismosVinculados.getSheetAt(4);
            XSSFSheet hojaContactosEmpresa = reporteOrganismosVinculados.getSheetAt(5);
            
            XSSFRow fila;
            XSSFCell celda;
            
            ejbOrganismosVinculados.getReporteActividadesVinculacion();
            
            List<OrganismosVinculados> organismosVinculados = ejbOrganismosVinculados.getReporteOrganismosVinculados();
            List<OrganismosVinculados> actividadesVinculacion = ejbOrganismosVinculados.getReporteActividadesVinculacion();
            List<DTOActividadesVinculacion> dtoAV = new ArrayList<>();
            actividadesVinculacion.forEach((ov) -> {
                List<ActividadesVinculacion> actVin = new ArrayList<>();
                actVin.addAll(ov.getActividadesVinculacionList());
                actVin.forEach((av) -> {
                    dtoAV.add(new DTOActividadesVinculacion(av,ov));
                });
            });
            List<ProgramasBeneficiadosVinculacion> programasBeneficiados = ejbOrganismosVinculados.getReporteProgramasBeneficiadosVinculacion();
            List<TelefonosEmpresa> telefonosEmpresa = ejbOrganismosVinculados.getReporteTelefonosEmpresa();
            List<CorreosEmpresa> correosEmpresa = ejbOrganismosVinculados.getCorreosEmpresas();
            List<ContactosEmpresa> contactosEmpresa = ejbOrganismosVinculados.getReporteContactosEmpresa();
            
//            Organismos Vinculados
            for (Integer listaOV = 0; listaOV < organismosVinculados.size(); listaOV++) {
                Integer ubicacion = listaOV + 2;

                fila = (XSSFRow) (Row) hojaOrganismosVinculados.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaOrganismosVinculados.createRow(ubicacion);
                }

//                Ejercicio
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(organismosVinculados.get(listaOV).getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                
//                Mes
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(organismosVinculados.get(listaOV).getRegistros().getEventoRegistro().getMes());
                
//                Fecha
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(organismosVinculados.get(listaOV).getFecha()));
                
//                Nombre Empresa
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(organismosVinculados.get(listaOV).getNombre());
                
//                Tipo de organismo
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(organismosVinculados.get(listaOV).getOrgTip().getDescripcion());
                
//                Tipo de empresa
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.STRING);
                }
                fila.getCell(5).setCellValue(organismosVinculados.get(listaOV).getEmpTip().getDescripcion());
                
//                Giro de la empresa
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.STRING);
                }
                fila.getCell(6).setCellValue(organismosVinculados.get(listaOV).getGiro().getDescripcion());
                
//                Giro Específico
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(organismosVinculados.get(listaOV).getGiroEspecifico());
                
//                Sector
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.STRING);
                }
                fila.getCell(8).setCellValue(organismosVinculados.get(listaOV).getSector().getDescripcion());
                
//                Sector específico
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(organismosVinculados.get(listaOV).getSector().getCorresponde());
                
//                Dirección
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(organismosVinculados.get(listaOV).getDireccion());
                
//                País
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(organismosVinculados.get(listaOV).getPais().getNombre());
                
//                Estado
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.STRING);
                }
                fila.getCell(12).setCellValue(organismosVinculados.get(listaOV).getLocalidad().getMunicipio().getEstado().getNombre());
                
//                Municipio
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.STRING);
                }
                fila.getCell(13).setCellValue(organismosVinculados.get(listaOV).getLocalidad().getMunicipio().getNombre());
                
//                Localidad
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.STRING);
                }
                fila.getCell(14).setCellValue(organismosVinculados.get(listaOV).getLocalidad().getNombre());
                
//                Codigo Postal
                celda = fila.getCell(15);
                if (null == celda) {
                    fila.createCell(15, CellType.STRING);
                }
                fila.getCell(15).setCellValue(organismosVinculados.get(listaOV).getCp());
                
//                Representante principal
                celda = fila.getCell(16);
                if (null == celda) {
                    fila.createCell(16, CellType.STRING);
                }
                fila.getCell(16).setCellValue(organismosVinculados.get(listaOV).getRepresentantePrincipal());
                
//                Cargo del representante principal
                celda = fila.getCell(17);
                if (null == celda) {
                    fila.createCell(17, CellType.STRING);
                }
                fila.getCell(17).setCellValue(organismosVinculados.get(listaOV).getCargoRepresentantePrincipal());
                
//                Telefono Principal
                celda = fila.getCell(18);
                if (null == celda) {
                    fila.createCell(18, CellType.STRING);
                }
                fila.getCell(18).setCellValue(organismosVinculados.get(listaOV).getTelefonoPrincipal());
                
//                Email Principal
                celda = fila.getCell(19);
                if (null == celda) {
                    fila.createCell(19, CellType.STRING);
                }
                fila.getCell(19).setCellValue(organismosVinculados.get(listaOV).getEmailPrincipal());
                
//                Convenio
                celda = fila.getCell(20);
                if (null == celda) {
                    fila.createCell(20, CellType.STRING);
                }
                fila.getCell(20).setCellValue(organismosVinculados.get(listaOV).getConvenio());
                
//                Estatus
                celda = fila.getCell(21);
                if (null == celda) {
                    fila.createCell(21, CellType.STRING);
                }
                String estado;
                if(organismosVinculados.get(listaOV).getEstatus()){
                    estado = "Alta";
                }else{
                    estado = "Baja";
                }
                fila.getCell(21).setCellValue(estado);
                
            }
            
//            Actividades Vinculadas con el Organismo
            for (Integer listAV = 0; listAV < dtoAV.size(); listAV++) {
                Integer ubicacion = listAV + 2;

                fila = (XSSFRow) (Row) hojaActividadesVinculacion.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaActividadesVinculacion.createRow(ubicacion);
                }
                
//                Identificador
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(dtoAV.get(listAV).getOrganismoVinculado().getEmpresa());
                
//                Empresa
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(dtoAV.get(listAV).getOrganismoVinculado().getNombre());
                
//                Actividad para la que se vincula
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(dtoAV.get(listAV).getActividadVinculacion().getNombre());
                
            }

//            Programas Beneficiados con la Vinculación del Organismo Vinculado
            for (Integer listaPB = 0; listaPB < programasBeneficiados.size(); listaPB++) {
                Integer ubicacion = listaPB + 2;

                fila = (XSSFRow) (Row) hojaProgramasBeneficiados.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaProgramasBeneficiados.createRow(ubicacion);
                }
                
//                Identificador
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(programasBeneficiados.get(listaPB).getOrganismosVinculados().getEmpresa());
                
//                Empresa
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(programasBeneficiados.get(listaPB).getOrganismosVinculados().getNombre());
                
                AreasUniversidad programaEducativo = new AreasUniversidad();
                programaEducativo = getProgramaEducativo(programasBeneficiados.get(listaPB).getProgramasBeneficiadosVinculacionPK().getProgramaEducativo());
                
//                Siglas
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.STRING);
                }
                fila.getCell(2).setCellValue(programaEducativo.getSiglas());
                
//                Nombre programa educativo
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.STRING);
                }
                fila.getCell(3).setCellValue(programaEducativo.getNombre());
                
            }
            
//            Telefónos adicionales de la empresa
            for (Integer listaTE = 0; listaTE < telefonosEmpresa.size(); listaTE++) {
                Integer ubicacion = listaTE + 2;

                fila = (XSSFRow) (Row) hojaTelefonosEmpresa.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaTelefonosEmpresa.createRow(ubicacion);
                }
                
//                Identificador
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(telefonosEmpresa.get(listaTE).getEmpresa().getEmpresa());
                
//                Empresa
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(telefonosEmpresa.get(listaTE).getEmpresa().getNombre());
                                
//                Identificador
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(telefonosEmpresa.get(listaTE).getTelefonoPrincipal());
            }
            
//            Correos adicionales de la empresa
            for (Integer listaCE = 0; listaCE < correosEmpresa.size(); listaCE++) {
                Integer ubicacion = listaCE + 2;

                fila = (XSSFRow) (Row) hojaCorreosEmpresa.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaCorreosEmpresa.createRow(ubicacion);
                }
                
//                Identificador
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(correosEmpresa.get(listaCE).getEmpresa().getEmpresa());
                
//                Empresa
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(correosEmpresa.get(listaCE).getEmpresa().getNombre());
                                
//                Identificador
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(correosEmpresa.get(listaCE).getEmail());
            }
            
//            Contactos adicionales de la empresa
            for (Integer listaCE = 0; listaCE < contactosEmpresa.size(); listaCE++) {
                Integer ubicacion = listaCE + 2;

                fila = (XSSFRow) (Row) hojaContactosEmpresa.getRow(ubicacion);
                if (null == fila) {
                    fila = hojaContactosEmpresa.createRow(ubicacion);
                }
                
//                Identificador
                celda = fila.getCell(0);
                if (null == celda) {
                    fila.createCell(0, CellType.NUMERIC);
                }
                fila.getCell(0).setCellValue(contactosEmpresa.get(listaCE).getEmpresa().getEmpresa());
                
//                Empresa
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.NUMERIC);
                }
                fila.getCell(1).setCellValue(contactosEmpresa.get(listaCE).getEmpresa().getNombre());
                                
//                Representante
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(contactosEmpresa.get(listaCE).getRepresentante());
                
//                Cargo del representante
                celda = fila.getCell(3);
                if (null == celda) {
                    fila.createCell(3, CellType.NUMERIC);
                }
                fila.getCell(3).setCellValue(contactosEmpresa.get(listaCE).getCargoRepresentante());
            }

            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            reporteOrganismosVinculados.write(archivoSalida);
            reporteOrganismosVinculados.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteOrganismosVinculados() - Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioReporteVINExcel.getReporteOrganismosVinculados() - Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }
    
}
