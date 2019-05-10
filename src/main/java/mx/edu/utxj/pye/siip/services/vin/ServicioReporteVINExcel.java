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
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbConvenios;
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
    
    public static final String REPORTE_CONVENIOS_PLANTILLA = "reporte_convenios_plantilla.xlsx";
    public static final String REPORTE_CONVENIOS_COPIA = "reporte_convenios_copia.xlsx";
    public static final String REPORTE_CONVENIOS_ACTUALIZADO = "reporte_convenios_actualizado.xlsx";
    
    public static final String REPORTE_SERVICIOS_TECNOLOGICOS_PLANTILLA = "reporte_servicios_tecnologicos_plantilla.xlsx";
    public static final String REPORTE_SERVICIOS_TECNOLOGICOS_COPIA = "reporte_servicios_tecnologicos_copia.xlsx";
    public static final String REPORTE_SERVICIOS_TECNOLOGICOS_ACTUALIZADO = "reporte_servicios_tecnologicos_actualizado.xlsx";
    
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
            XSSFSheet hojaDistribucionEquipamiento = reporteConvenios.getSheetAt(0);

            XSSFRow fila;
            XSSFCell celda;
            
//            Vaciado de información proveniente de la consulta
            List<Convenios> convenios = ejbConvenios.getReporteGeneralConvenios();
            
            for(Integer listaC = 0; listaC < convenios.size(); listaC++){
                Integer ubicacion = listaC + 3;
                
                fila = (XSSFRow) (Row) hojaDistribucionEquipamiento.getRow(ubicacion);
                if(null == fila){
                    fila = hojaDistribucionEquipamiento.createRow(ubicacion);
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
                    Generaciones generacion = new Generaciones();
                    generacion = f.getEntityManager().find(Generaciones.class, servTecPart.get(listaSTP).getGeneracion());
                    celda = fila.getCell(13);
                    if (null == celda) {
                        fila.createCell(13, CellType.STRING);
                    }
                    fila.getCell(13).setCellValue(generacion.getInicio()+"-"+generacion.getFin());
                }
                
//                Programa Educativo
                if(servTecPart.get(listaSTP).getProgramaEducativo() != null){
                    AreasUniversidad programaEducativo = new AreasUniversidad();
                    programaEducativo = f.getEntityManager().find(AreasUniversidad.class, servTecPart.get(listaSTP).getProgramaEducativo());
                    celda = fila.getCell(14);
                    if (null == celda) {
                        fila.createCell(14, CellType.STRING);
                    }
                    fila.getCell(14).setCellValue(programaEducativo.getNombre());
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
    
}
