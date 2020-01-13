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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AulasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaTiposInstalaciones;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionInstalaciones;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbPlantillasEBExcel;
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
public class ServicioPlantillasEBExcel implements EjbPlantillasEBExcel{

    @EJB
    EjbCarga ejbCarga;
    @EJB
    EjbCatalogos ejbCatalogos;
    
    @EJB    EjbDistribucionInstalaciones ejbDistribucionInstalaciones;
    
    public static final String MATRICULA_PLANTILLA = "matricula_periodo_escolar.xlsx";
    public static final String MATRICULA_COPIA = "matricula_periodo_escolar_copia.xlsx";
    public static final String MATRICULA_ACTUALIZADO = "matricula_periodo_escolar_actualizado.xlsx";
    
    public static final String EFICIENCIA_TERMINAL_PLANTILLA = "eficiencia_terminal.xlsx";
    public static final String EFICIENCIA_TERMINAL_COPIA = "eficiencia_terminal_copia.xlsx";
    public static final String EFICIENCIA_TERMINAL_ACTUALIZADO = "eficiencia_terminal_actualizado.xlsx";
    
    public static final String DISTRIBUCION_EQUIPAMIENTO_PLANTILLA = "distribucion_equipamiento.xlsx";
    public static final String DISTRIBUCION_EQUIPAMIENTO_COPIA = "distribucion_equipamiento_copia.xlsx";
    public static final String DISTRIBUCION_EQUIPAMIENTO_ACTUALIZADO = "distribucion_equipamiento_actualizado.xlsx";
    
    public static final String DISTRIBUCION_INSTALACIONES_PLANTILLA = "distribucion_instalaciones.xlsx";
    public static final String DISTRIBUCION_INSTALACIONES_COPIA = "distribucion_instalaciones_copia.xlsx";
    public static final String DISTRIBUCION_INSTALACIONES_ACTUALIZADO = "distribucion_instalaciones_actualizado.xlsx";
    
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    
    public String crearDirectorioPlantilla(String eje) {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(eje);
        return rutaPlantilla;
    }

    public String crearDirectorioPlantillaCompleto(String eje) {
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaCompleto(eje);
        return rutaPlantillaC;
    }
    
//    TODO: Verificar plantilla para que la interpretacion de los datos sea correctamente mostrada en el archivo de excel
    @Override
    public String getPlantillaMatriculaPeriodosEscolares() throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantilla(ejes[0]);
        String rutaPlantillaCopia = ejbCarga.crearDirectorioPlantillaCompleto(ejes[0]);
        String rutaPlantillaCompleta = ejbCarga.crearDirectorioPlantillaCompleto(ejes[0]);
        
        String plantilla = rutaPlantilla.concat(MATRICULA_PLANTILLA);
        String plantillaCopia = rutaPlantillaCopia.concat(MATRICULA_COPIA);
        String plantillaCompleta = rutaPlantillaCompleta.concat(MATRICULA_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroMatriculaPeriodoEscolar = new XSSFWorkbook();
            libroMatriculaPeriodoEscolar = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroMatriculaPeriodoEscolar.getSheetAt(1);
            
            List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());
            List<AreasUniversidad> programasEducativos = ejbCatalogos.getProgramasEducativos();
            
            XSSFRow fila;
            XSSFCell celda;
                        
//            Ciclos Escolares
            for(Integer listaCE = 0; listaCE < ciclosEscolares.size(); listaCE++){
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaCE + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila)
                    fila = catalogos.createRow(ubicacion);
                
//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo Escolar
                celda = fila.getCell(1);
                if (null == celda) 
                    fila.createCell(1, CellType.STRING);
                LocalDate inicioGeneracion = ciclosEscolares.get(listaCE).getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate finGeneracion = ciclosEscolares.get(listaCE).getFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String cicloCompuesto = inicioGeneracion.getYear() + "-" + finGeneracion.getYear();
                fila.getCell(1).setCellValue(cicloCompuesto);
                
//                Clave
                celda = fila.getCell(2);
                if (null == celda) 
                    fila.createCell(2, CellType.NUMERIC);
                fila.getCell(2).setCellValue(ciclosEscolares.get(listaCE).getCiclo());
            }
            
//            Periodos Escolares
            for (Integer listaPE = 0; listaPE < periodosEscolares.size(); listaPE++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaPE + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila)
                    fila = catalogos.createRow(ubicacion);

//                Inicia el vaciado de los catalogos en las celdas indicadas
//               Ciclo 
                celda = fila.getCell(4);
                if (null == celda)
                    fila.createCell(4, CellType.NUMERIC);
                fila.getCell(4).setCellValue(periodosEscolares.get(listaPE).getCiclo().getCiclo());
                
//               Cuatrimestre 
                celda = fila.getCell(5);
                if (null == celda)
                    fila.createCell(5, CellType.STRING);
                String compuesto = periodosEscolares.get(listaPE).getMesInicio().getMes() + "-" + periodosEscolares.get(listaPE).getMesFin().getMes();
                fila.getCell(5).setCellValue(compuesto);
                
//               Auxiliar 
                celda = fila.getCell(6);
                if (null == celda)
                    fila.createCell(6, CellType.STRING);
                fila.getCell(6).setCellValue(periodosEscolares.get(listaPE).getCiclo().getCiclo()+compuesto);     
                
//               Periodo 
                celda = fila.getCell(7);
                if (null == celda)
                    fila.createCell(7, CellType.NUMERIC);
                fila.getCell(7).setCellValue(periodosEscolares.get(listaPE).getPeriodo());
            }
            
//            Programas Educativos
            for (Integer listaProEd = 0; listaProEd < programasEducativos.size(); listaProEd++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaProEd + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Carrera
                celda = fila.getCell(9);
                if (null == celda)
                    fila.createCell(9, CellType.STRING);
                fila.getCell(9).setCellValue(programasEducativos.get(listaProEd).getNombre());
                
//                Siglas
                celda = fila.getCell(10);
                if (null == celda)
                    fila.createCell(10, CellType.STRING);
                fila.getCell(10).setCellValue(programasEducativos.get(listaProEd).getSiglas());
                
//                Area
                celda = fila.getCell(11);
                if (null == celda)
                    fila.createCell(11, CellType.NUMERIC);
                fila.getCell(11).setCellValue(programasEducativos.get(listaProEd).getArea());
            }
            
            XSSFSheet catalogosProgramasEducativos = libroMatriculaPeriodoEscolar.getSheetAt(2);
//            Programas Educativos
            for (Integer listaProEd = 0; listaProEd < programasEducativos.size(); listaProEd++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = listaProEd + 3;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogosProgramasEducativos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogosProgramasEducativos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Carrera
                celda = fila.getCell(0);
                if (null == celda)
                    fila.createCell(0, CellType.STRING);
                fila.getCell(0).setCellValue(programasEducativos.get(listaProEd).getNombre());
                
//                Siglas
                celda = fila.getCell(1);
                if (null == celda)
                    fila.createCell(1, CellType.STRING);
                fila.getCell(1).setCellValue(programasEducativos.get(listaProEd).getSiglas());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleta).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroMatriculaPeriodoEscolar.write(archivoSalida);
            libroMatriculaPeriodoEscolar.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaMatriculaPeriodosEscolares() Archivo no escontrado");
        } catch (IOException ex){
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaMatriculaPeriodosEscolares() Error de lectura o escritura (i/o");
        }
        return plantillaCompleta;
    }    

    @Override
    public String getPlantillaEficienciaTerminal() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[0]).concat(EFICIENCIA_TERMINAL_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[0]).concat(EFICIENCIA_TERMINAL_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[0]).concat(EFICIENCIA_TERMINAL_ACTUALIZADO);
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroEficienciaTerminal = new XSSFWorkbook();
            libroEficienciaTerminal = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroEficienciaTerminal.getSheetAt(1);
            
            List<Generaciones> generaciones = ejbCatalogos.getGeneracionesAct().stream().sorted(Comparator.comparing(Generaciones::getGeneracion).reversed()).collect(Collectors.toList());
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());
            List<AreasUniversidad> programasEducativos = ejbCatalogos.getProgramasEducativos();
            
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
                fila.getCell(1).setCellValue(generaciones.get(gen).getInicio()+"-"+generaciones.get(gen).getFin());
                
//                Clave
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(generaciones.get(gen).getGeneracion());
            }
            
//            Periodos Escolares - Periodo Inicio - Periodo Fin
            for (Integer pe = 0; pe < periodosEscolares.size(); pe++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = pe + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Mes Inicio
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(periodosEscolares.get(pe).getMesInicio().getMes() + " " + periodosEscolares.get(pe).getAnio());
                
//                Periodo
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(periodosEscolares.get(pe).getPeriodo());
                
//                Mes Fin
                celda = fila.getCell(7);
                if (null == celda) {
                    fila.createCell(7, CellType.STRING);
                }
                fila.getCell(7).setCellValue(periodosEscolares.get(pe).getMesFin().getMes() + " " + periodosEscolares.get(pe).getAnio());
                
//                Periodo
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(periodosEscolares.get(pe).getPeriodo());
            }
            
//            Programas Educativos
            for (Integer au = 0; au < programasEducativos.size(); au++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = au + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Programa Educativo
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(programasEducativos.get(au).getNombre());
                
//                Nivel - Siglas
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.STRING);
                }
                fila.getCell(11).setCellValue(programasEducativos.get(au).getSiglas());
                
//                Clave
                celda = fila.getCell(12);
                if (null == celda) {
                    fila.createCell(12, CellType.NUMERIC);
                }
                fila.getCell(12).setCellValue(programasEducativos.get(au).getArea());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroEficienciaTerminal.write(archivoSalida);
            libroEficienciaTerminal.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaEficienciaTerminal() Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaEficienciaTerminal() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getPlantillaDistribucionEquipamiento() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[0]).concat(DISTRIBUCION_EQUIPAMIENTO_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[0]).concat(DISTRIBUCION_EQUIPAMIENTO_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[0]).concat(DISTRIBUCION_EQUIPAMIENTO_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroDistribucionEquipamiento = new XSSFWorkbook();
            libroDistribucionEquipamiento = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroDistribucionEquipamiento.getSheetAt(2);
            
            List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Ciclos Escolares
            for (Integer cs = 0; cs < ciclosEscolares.size(); cs++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = cs + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclos Escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                LocalDate inicioCiclo = ciclosEscolares.get(cs).getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate finCiclo = ciclosEscolares.get(cs).getFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String cicloCompuesto = inicioCiclo.getYear() + "-" + finCiclo.getYear();
                fila.getCell(1).setCellValue(cicloCompuesto);
                
//                Clave
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(ciclosEscolares.get(cs).getCiclo());
            }
            
//            Periodos Escolares
            for (Integer pe = 0; pe < periodosEscolares.size(); pe++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = pe + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclos
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo());
                
//                Cuatrimestre
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());
                
//                Auxiliar
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo()+periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());
                
//                Periodo
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.NUMERIC);
                }
                fila.getCell(11).setCellValue(periodosEscolares.get(pe).getPeriodo());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroDistribucionEquipamiento.write(archivoSalida);
            libroDistribucionEquipamiento.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaDistribucionEquipamiento() Archivo no escontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaDistribucionEquipamiento() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto;
    }

    @Override
    public String getPlantillaDistribucionInstalaciones() throws Throwable {
        String plantilla = crearDirectorioPlantilla(ejes[0]).concat(DISTRIBUCION_INSTALACIONES_PLANTILLA);
        String plantillaCopia = crearDirectorioPlantillaCompleto(ejes[0]).concat(DISTRIBUCION_INSTALACIONES_COPIA);
        String plantillaCompleto = crearDirectorioPlantillaCompleto(ejes[0]).concat(DISTRIBUCION_INSTALACIONES_ACTUALIZADO);
        
        try {
            Files.copy(FileSystems.getDefault().getPath(plantilla), FileSystems.getDefault().getPath(plantillaCopia), StandardCopyOption.REPLACE_EXISTING);
            File archivoCopia = FileSystems.getDefault().getPath(plantillaCopia).toFile();

            XSSFWorkbook libroDistribucionInstaladas = new XSSFWorkbook();
            libroDistribucionInstaladas = (XSSFWorkbook) WorkbookFactory.create(archivoCopia);

            XSSFSheet catalogos = libroDistribucionInstaladas.getSheetAt(3);
            
            List<CiclosEscolares> ciclosEscolares = ejbCatalogos.getCiclosEscolaresAct().stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
            List<CapacidadInstaladaTiposInstalaciones> capacidadInstaladaTiposInstalacioneses = ejbDistribucionInstalaciones.getCapacidadInstaladaTiposInstalacioneses();
            List<PeriodosEscolares> periodosEscolares = ejbCatalogos.getPeriodosEscolaresAct().stream().sorted(Comparator.comparing(PeriodosEscolares::getPeriodo).reversed()).collect(Collectors.toList());
            List<AreasUniversidad> areasUniversidad = ejbCatalogos.getAreasAcademicasDistribucionAulas();
            List<AulasTipo> aulasTipos = ejbDistribucionInstalaciones.getAulasTipos();
            
            XSSFRow fila;
            XSSFCell celda;
            
//            Ciclos Escolares
            for (Integer ce = 0; ce < ciclosEscolares.size(); ce++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = ce + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo Escolar
                celda = fila.getCell(1);
                if (null == celda) {
                    fila.createCell(1, CellType.STRING);
                }
                LocalDate inicioCiclo = ciclosEscolares.get(ce).getInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate finCiclo = ciclosEscolares.get(ce).getFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String cicloCompuesto = inicioCiclo.getYear() + "-" + finCiclo.getYear();
                fila.getCell(1).setCellValue(cicloCompuesto);
                
//                Clave
                celda = fila.getCell(2);
                if (null == celda) {
                    fila.createCell(2, CellType.NUMERIC);
                }
                fila.getCell(2).setCellValue(ciclosEscolares.get(ce).getCiclo());
            }
            
//            Edificios Capacidad Instalada
            for (Integer eci = 0; eci < capacidadInstaladaTiposInstalacioneses.size(); eci++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = eci + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripción
                celda = fila.getCell(4);
                if (null == celda) {
                    fila.createCell(4, CellType.STRING);
                }
                fila.getCell(4).setCellValue(capacidadInstaladaTiposInstalacioneses.get(eci).getDescripcion());
                
//                Capacidad
                celda = fila.getCell(5);
                if (null == celda) {
                    fila.createCell(5, CellType.NUMERIC);
                }
                fila.getCell(5).setCellValue(capacidadInstaladaTiposInstalacioneses.get(eci).getCapacidad());
                
//                Clave
                celda = fila.getCell(6);
                if (null == celda) {
                    fila.createCell(6, CellType.NUMERIC);
                }
                fila.getCell(6).setCellValue(capacidadInstaladaTiposInstalacioneses.get(eci).getInstalacion());
            }
            
//            Periodos Escolares
            for (Integer pe = 0; pe < periodosEscolares.size(); pe++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = pe + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Ciclo
                celda = fila.getCell(8);
                if (null == celda) {
                    fila.createCell(8, CellType.NUMERIC);
                }
                fila.getCell(8).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo());
                
//                Cuatrimestre
                celda = fila.getCell(9);
                if (null == celda) {
                    fila.createCell(9, CellType.STRING);
                }
                fila.getCell(9).setCellValue(periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());
                
//                Auxiliar
                celda = fila.getCell(10);
                if (null == celda) {
                    fila.createCell(10, CellType.STRING);
                }
                fila.getCell(10).setCellValue(periodosEscolares.get(pe).getCiclo().getCiclo()+periodosEscolares.get(pe).getMesInicio().getMes()+"-"+periodosEscolares.get(pe).getMesFin().getMes());
                
//                Periodo
                celda = fila.getCell(11);
                if (null == celda) {
                    fila.createCell(11, CellType.NUMERIC);
                }
                fila.getCell(11).setCellValue(periodosEscolares.get(pe).getPeriodo());
            }
            
//            Areas Universidad
            for (Integer au = 0; au < areasUniversidad.size(); au++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = au + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Nombre
                celda = fila.getCell(13);
                if (null == celda) {
                    fila.createCell(13, CellType.STRING);
                }
                fila.getCell(13).setCellValue(areasUniversidad.get(au).getNombre());
                
//                Area
                celda = fila.getCell(14);
                if (null == celda) {
                    fila.createCell(14, CellType.NUMERIC);
                }
                fila.getCell(14).setCellValue(areasUniversidad.get(au).getArea());
            }
            
//            Tipos de aulas
            for (Integer at = 0; at < aulasTipos.size(); at++) {
//                Indica a partir de que posición empezar a llenar los datos en las celdas correspondientes - El cual se obtiene haciendo la suma de la iteración ligada a la lista y el numero de fila inicial
                Integer ubicacion = at + 4;

//                Declarar getRow en caso de usar tabla o bien existan datos posteriores al llenado de los datos, usar create unicamente cuando la plantilla no contenga ningun formato o no existan datos a la izquierda del archivo.
                fila = (XSSFRow) (Row) catalogos.getRow(ubicacion);
                if (null == fila) {
                    fila = catalogos.createRow(ubicacion);
                }

//                Inicia el vaciado de los catalogos en las celdas indicadas
//                Descripcion
                celda = fila.getCell(16);
                if (null == celda) {
                    fila.createCell(16, CellType.STRING);
                }
                fila.getCell(16).setCellValue(aulasTipos.get(at).getNombre());
                
//                Capacidad
                celda = fila.getCell(17);
                if (null == celda) {
                    fila.createCell(17, CellType.STRING);
                }
                fila.getCell(17).setCellValue(aulasTipos.get(at).getCapacidadTurno());
                
//                Clave
                celda = fila.getCell(18);
                if (null == celda) {
                    fila.createCell(18, CellType.NUMERIC);
                }
                fila.getCell(18).setCellValue(aulasTipos.get(at).getAulatipo());
            }
            
            File archivoFinal = FileSystems.getDefault().getPath(plantillaCompleto).toFile();
            FileOutputStream archivoSalida = new FileOutputStream(archivoFinal);
            libroDistribucionInstaladas.write(archivoSalida);
            libroDistribucionInstaladas.close();
            archivoSalida.flush();
            archivoSalida.close();
            Files.deleteIfExists(FileSystems.getDefault().getPath(plantillaCopia));
        } catch (FileNotFoundException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaDistribucionInstalaciones() Archivo no encontrado");
        } catch (IOException ex) {
            System.out.println("mx.edu.utxj.pye.siip.services.eb.ServicioPlantillasEBExcel.getPlantillaDistribucionInstalaciones() Error de lectura o escritura (i/o)");
        }
        return plantillaCompleto; 
    }
}
