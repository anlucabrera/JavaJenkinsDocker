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
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
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
    @EJB    EjbMatriculaPeriodosEscolares   ejbMatriculaPeriodosEscolares;
    @EJB    EjbServiciosTecnologicosAnioMes ejbServiciosTecnologicosAnioMes;
    @EJB    EjbEgresados    ejbEgresados;
    @EJB    EjbCatalogos    ejbCatalogos;

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

}
