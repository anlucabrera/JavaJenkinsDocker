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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EmpresasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTipos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbCatalogoIemsRecurrentes;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbIems;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbPlantillasVINExcel;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbServiciosTecnologicosAnioMes;
import net.sf.jxls.transformer.XLSTransformer;
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
@MultipartConfig()
public class ServicioPlantillasVINExcel implements EjbPlantillasVINExcel {

    @EJB    EjbCarga    ejbCarga;
    @EJB    EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB    EjbCatalogoIemsRecurrentes ejbCatalogoIemsRecurrentes;
    @EJB    EjbMatriculaPeriodosEscolares   ejbMatriculaPeriodosEscolares;
    @EJB    EjbServiciosTecnologicosAnioMes ejbServiciosTecnologicosAnioMes;
    @EJB    EjbEgresados    ejbEgresados;
    @EJB    EjbCatalogos    ejbCatalogos;
    @EJB    EjbIems  ejbIems;

    public static final String CONVENIOS_PLANTILLA = "convenios.xlsx";
    public static final String CONVENIOS_ACTUALIZADO = "convenios_actualizado.xlsx";
    
    public static final String SERVICIOS_TECNOLOGICOS_PLANTILLA = "servicios_tecnologicos.xlsx";
    public static final String SERVICIOS_TECNOLOGICOS_COPIA = "servicios_tecnologicos_copia.xlsx";
    public static final String SERVICIOS_TECNOLOGICOS_ACTUALIZADO = "servicios_tecnologicos_actualizado.xlsx";
    
    public static final String ORGANISMOS_VINCULADOS_PLANTILLA = "organismos_vinculados.xlsx";
    public static final String ORGANISMOS_VINCULADOS_COPIA = "organismos_vinculados_copia.xlsx";
    public static final String ORGANISMOS_VINCULADOS_COMPLETO = "organismos_vinculados_actualizado.xlsx";
    
    public static final String EGRESADOS_PLANTILLA = "egresados.xlsx";
    public static final String EGRESADOS_COPIA = "egresados_copia.xlsx";
    public static final String EGRESADOS_COMPLETO = "egresados_actualizado.xlsx";
    
    public static final String BOLSATRABAJO_PLANTILLA = "bolsaTrabajo.xlsx";
    public static final String BOLSATRABAJO_ACTUALIZADO = "bolsaTrabajo_actualizado.xlsx";
    
    public static final String VISIND_PLANTILLA = "visitasIndustriales.xlsx";
    public static final String VISIND_ACTUALIZADO = "visitasIndustriales_actualizado.xlsx";
    
    public static final String DIFIEMS_PLANTILLA = "difusionIems.xlsx";
    public static final String DIFIEMS_COPIA = "difusionIems_copia.xlsx";
    public static final String DIFIEMS_ACTUALIZADO = "difusionIems_actualizado.xlsx";
    
    public static final String FERPROF_PLANTILLA = "feriasProfesiograficas.xlsx";
    public static final String FERPROF_ACTUALIZADO = "feriasProfesiograficas_actualizado.xlsx";
    
    public static final String IEMS_PLANTILLA = "IEMS.xlsx";
    public static final String IEMS_ACTUALIZADO = "IEMS_actualizado.xlsx";
    
    @Getter
    private final String[] ejes = ServicioArchivos.EJES;

    public String crearDirectorioPlantilla(String eje) {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(eje);
        return rutaPlantilla;
    }

    public String crearDirectorioPlantillaCompleto(String eje) {
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(eje);
        return rutaPlantillaC;
    }
    
    @Override
    public String getPlantillaConvenios() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(CONVENIOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(CONVENIOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        return plantillaC;
    }
    
    @Override
    public String getPlantillaServiciosTecnologicos() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaCopia = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String rutaPlantillaCompleta = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(SERVICIOS_TECNOLOGICOS_PLANTILLA);
        String plantillaCopia = rutaPlantillaCopia.concat(SERVICIOS_TECNOLOGICOS_COPIA);
        String plantillaCompleta = rutaPlantillaCompleta.concat(SERVICIOS_TECNOLOGICOS_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroServicios = new XSSFWorkbook();
            libroServicios = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroServicios.getSheetAt(2);

            List<Generaciones> generaciones = ejbCatalogos.getGeneracionesAct().stream().sorted(Comparator.comparing(Generaciones::getGeneracion).reversed()).collect(Collectors.toList());
            List<AreasUniversidad> areasUniversidad = ejbCatalogos.getProgramasEducativos();
            List<OrganismosVinculados> organismosVinculados = ejbOrganismosVinculados.getOrganismosVinculadoVigentes();
            List<ServiciosTipos> serviciosTipos = ejbServiciosTecnologicosAnioMes.getListaServiciosTipo();

            XSSFRow fila;
            XSSFCell celda;
            
//            Generaciones
            for(Integer listaGeneraciones = 0; listaGeneraciones < generaciones.size(); listaGeneraciones++){
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaGeneraciones + 3;
                
//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila)
                    fila = catalogos.createRow(ubicacion);
                
//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Generacion Busqueda
                celda = fila.getCell(1);
                if (null == celda)
                    fila.createCell(1, CellType.STRING);
                Short inicioGeneracion = generaciones.get(listaGeneraciones).getInicio();
                Short finGeneracion = generaciones.get(listaGeneraciones).getFin();
                String generacionCompuesto = inicioGeneracion + "-" + finGeneracion;
                fila.getCell(1).setCellValue(generacionCompuesto);
                
//                Inicio
                celda = fila.getCell(2);
                if(null == celda)
                    fila.createCell(2,CellType.NUMERIC);
                fila.getCell(2).setCellValue(generaciones.get(listaGeneraciones).getInicio());
                
//                Fin
                celda = fila.getCell(3);
                if(null == celda)
                    fila.createCell(3,CellType.NUMERIC);
                fila.getCell(3).setCellValue(generaciones.get(listaGeneraciones).getFin());
                
//                Generacion22
                celda = fila.getCell(4);
                if (null == celda)
                    fila.createCell(4, CellType.STRING);
                fila.getCell(4).setCellValue(generacionCompuesto);
                
//                Clave
                celda = fila.getCell(5);
                if (null == celda)
                    fila.createCell(5, CellType.NUMERIC);
                fila.getCell(5).setCellValue(generaciones.get(listaGeneraciones).getGeneracion());                
            }

//            Programas Educativos
            for (Integer listaArea = 0; listaArea < areasUniversidad.size(); listaArea++) {
                Integer ubicacion = listaArea + 3;
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila)
                    fila = catalogos.createRow(ubicacion);
                celda = fila.getCell(8);
                if (null == celda)
                    fila.createCell(8, CellType.STRING);
                fila.getCell(8).setCellValue(areasUniversidad.get(listaArea).getNombre());

                celda = fila.getCell(9);
                if (null == celda)
                    fila.createCell(9, CellType.NUMERIC);
                fila.getCell(9).setCellValue(areasUniversidad.get(listaArea).getArea());
            }
            
//            Organismos Vinculados
            for (Integer listaOrgVin = 0; listaOrgVin < organismosVinculados.size(); listaOrgVin++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaOrgVin + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila)
                    fila = catalogos.createRow(ubicacion);

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Nombre
                celda = fila.getCell(12);
                if (null == celda)
                    fila.createCell(12, CellType.STRING);
                fila.getCell(12).setCellValue(organismosVinculados.get(listaOrgVin).getNombre());
                
//                Clave
                celda = fila.getCell(13);
                if (null == celda)
                    fila.createCell(13, CellType.NUMERIC);
                fila.getCell(13).setCellValue(organismosVinculados.get(listaOrgVin).getEmpresa());
            }
            
//            Tipos de servicio
            for (Integer listaTS = 0; listaTS < serviciosTipos.size(); listaTS++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaTS + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila)
                    fila = catalogos.createRow(ubicacion);

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Servicio
                celda = fila.getCell(15);
                if (null == celda)
                    fila.createCell(15, CellType.STRING);
                fila.getCell(15).setCellValue(serviciosTipos.get(listaTS).getDescripcion());
                
//                Clave
                celda = fila.getCell(16);
                if (null == celda)
                    fila.createCell(16, CellType.NUMERIC);
                fila.getCell(16).setCellValue(serviciosTipos.get(listaTS).getServtipo());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleta).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroServicios.write(archivoSalida);
            libroServicios.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioPlantillasVINExcel.getPlantillaServiciosTecnologicos() Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioPlantillasVINExcel.getPlantillaServiciosTecnologicos() Error de lectura o escritura (i/o");
        }
        return plantillaCompleta;
    }

    @Override
    public String getPlantillaBolsaTrabajo() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(BOLSATRABAJO_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(BOLSATRABAJO_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String getPlantillaOrganismosVinculados() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[2]).concat(ORGANISMOS_VINCULADOS_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[2]).concat(ORGANISMOS_VINCULADOS_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[2]).concat(ORGANISMOS_VINCULADOS_COMPLETO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroOrganismosVinculados = new XSSFWorkbook();
            libroOrganismosVinculados = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogo = libroOrganismosVinculados.getSheetAt(1);
            
            List<OrganismosTipo> organismosTipos = ejbOrganismosVinculados.getOrganismosTipo();
            List<EmpresasTipo> empresasTipos = ejbOrganismosVinculados.getEmpresasTipos();
            List<GirosTipo> girosTipos = ejbOrganismosVinculados.getGirosTipo();
            List<SectoresTipo> sectoresTipos = ejbOrganismosVinculados.getSectoresTipo();
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Organismos Tipo    
            for (Integer ot = 0; ot < organismosTipos.size(); ot++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ot + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogo.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogo.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripción
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                fila.getCell(1).setCellValue(organismosTipos.get(ot).getDescripcion());

//                Clave
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(organismosTipos.get(ot).getOrgtipo());
            }
            
//            Tipo Empresa
            for (Integer te = 0; te < empresasTipos.size(); te++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = te + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogo.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogo.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripción
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(empresasTipos.get(te).getDescripcion());
                
//                Clave
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(empresasTipos.get(te).getEmptipo());
            }
            
//            Tipo Giro
            for (Integer tg = 0; tg < girosTipos.size(); tg++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = tg + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogo.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogo.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripción
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(girosTipos.get(tg).getDescripcion());
                
//                Clave
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(girosTipos.get(tg).getGiro());
            }
            
//           Tipo Sector 
            for (Integer ts = 0; ts < sectoresTipos.size(); ts++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ts + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogo.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogo.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripción
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(sectoresTipos.get(ts).getDescripcion());
                
//                Clave
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.NUMERIC);
                }
                fila.getCell(11).setCellValue(sectoresTipos.get(ts).getSector());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroOrganismosVinculados.write(archivoSalida);
            libroOrganismosVinculados.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioPlantillasVINExcel.getPlantillaOrganismosVinculados() Archivo no encontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioPlantillasVINExcel.getPlantillaOrganismosVinculados() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getPlantillaEgresados() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[2]).concat(EGRESADOS_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[2]).concat(EGRESADOS_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[2]).concat(EGRESADOS_COMPLETO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroEgresados = new XSSFWorkbook();
            libroEgresados = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroEgresados.getSheetAt(4);
            
            List<Generaciones> generaciones = ejbCatalogos.getGeneracionesAct().stream().sorted(Comparator.comparing(Generaciones::getGeneracion).reversed()).collect(Collectors.toList());
            List<AreasUniversidad> programasEducativos = ejbCatalogos.getProgramasEducativosGeneral();
            List<ActividadEgresadoTipos> actividadEgresadoTipos = ejbEgresados.getActividadEgresadoTipos();
            List<NivelOcupacionTipos> nivelOcupacionTipos = ejbEgresados.getNivelOcupacionTipos();
            List<SectoresTipo> sectoresTipos = ejbOrganismosVinculados.getSectoresTipo();
            List<GirosTipo> girosTipos = ejbOrganismosVinculados.getGirosTipo();
            List<NivelIngresosTipos> nivelIngresosTipos = ejbEgresados.getNivelIngresosTipos();
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Generaciones  
            for (Integer gen = 0; gen < generaciones.size(); gen++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = gen + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Generacion
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                Short inicioGeneracion = generaciones.get(gen).getInicio();
                Short finGeneracion = generaciones.get(gen).getFin();
                String generacionCompuesto = inicioGeneracion + "-" + finGeneracion;
                fila.getCell(1).setCellValue(generacionCompuesto);
                
//                Clave
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(generaciones.get(gen).getGeneracion());
            }
            
//            Programas Educativos
            for (Integer pre = 0; pre < programasEducativos.size(); pre++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = pre + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Programa Educativo
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(programasEducativos.get(pre).getNombre());
                
//                Clave
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(programasEducativos.get(pre).getArea());
            }
            
//            Actividades Egresado
            for (Integer ae = 0; ae < actividadEgresadoTipos.size(); ae++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ae + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Nombre
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(actividadEgresadoTipos.get(ae).getDescripcion());
                
//                Área
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(actividadEgresadoTipos.get(ae).getActividad());
            }
            
//            Ocupaciones
            for (Integer oc = 0; oc < nivelOcupacionTipos.size(); oc++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = oc + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ocupación
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(nivelOcupacionTipos.get(oc).getDescripcion());
                
//                Clave
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.NUMERIC);
                }
                fila.getCell(11).setCellValue(nivelOcupacionTipos.get(oc).getOcupacion());  
            }
            
//            Sectores Trabajo
            for (Integer st = 0; st < sectoresTipos.size(); st++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = st + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripción
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.STRING);
                }
                fila.getCell(13).setCellValue(sectoresTipos.get(st).getDescripcion());
                
//                Sector
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.NUMERIC);
                }
                fila.getCell(14).setCellValue(sectoresTipos.get(st).getSector());
            }
            
//            Giros de trabajo
            for (Integer gt = 0; gt < girosTipos.size(); gt++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = gt + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Giro
                celda = fila.getCell(16);
                if (null == celda) {
                    fila.createCell(16, CellType.STRING);
                }
                fila.getCell(16).setCellValue(girosTipos.get(gt).getDescripcion());
                
//                Clave
                celda = fila.getCell(17);
                if (null == celda) {
                    fila.createCell(17, CellType.NUMERIC);
                }
                fila.getCell(17).setCellValue(girosTipos.get(gt).getGiro());
            }
            
//            Nivel de Ingreso
            for (Integer ni = 0; ni < nivelIngresosTipos.size(); ni++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ni + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ingreso
                celda = fila.getCell(19);
                if (null == celda) {
                    fila.createCell(19, CellType.STRING);
                }
                fila.getCell(19).setCellValue(nivelIngresosTipos.get(ni).getIngresos());
                
//                Clave
                celda = fila.getCell(20);
                if (null == celda) {
                    fila.createCell(20, CellType.NUMERIC);
                }
                fila.getCell(20).setCellValue(nivelIngresosTipos.get(ni).getNivel());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroEgresados.write(archivoSalida);
            libroEgresados.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioPlantillasVINExcel.getPlantillaEgresados() Archivo no encontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.vin.ServicioPlantillasVINExcel.getPlantillaEgresados() Error de lectura y escritura (i/o");
        }
        return plantillaCompleto;
    }
    
    @Override
    public String getPlantillaVisitasIndustriales() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(VISIND_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(VISIND_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("ciclosEscolares", ejbCatalogos.getCiclosEscolaresAct());
        beans.put("periodosEscolares", ejbCatalogos.getPeriodosEscolaresAct());
        beans.put("programasEducativos", ejbCatalogos.getProgramasEducativos());
        beans.put("organismosVinculados", ejbOrganismosVinculados.getOrganismosVinculadoVigentes());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getPlantillaDifusionIEMS() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(DIFIEMS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(DIFIEMS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("catalogoIems", ejbCatalogoIemsRecurrentes.getCatalogoIEMS());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getPlantillaFeriasProfesiograficas() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(FERPROF_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(FERPROF_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("catalogoIems", ejbCatalogoIemsRecurrentes.getCatalogoIEMS());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

    @Override
    public String getPlantillaIEMS() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[2]);
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(ejes[2]);
        String plantilla = rutaPlantilla.concat(IEMS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(IEMS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("serviciosEducativos", ejbIems.getServEducativosAct());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);
        
        return plantillaC;
    }

}
