/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelOcupacionEgresadosG;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig()
public class ServicioEgresados implements EjbEgresados {

    @EJB    Facade      facadeVinculacion;
    @EJB    EjbModulos  ejbModulos;
    
    @Inject Caster      caster;

    @Override
    public List<DTOActividadEgresadoGeneracion> getListaActividadEgresadoGeneracion(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
            //        Vista
            List<DTOActividadEgresadoGeneracion> listaDtoActividadEG = new ArrayList<>();
            ActividadEgresadoGeneracion actividadEG;
            ActividadEgresadoTipos actividadEgresadoTipo;
            DTOActividadEgresadoGeneracion dtoActividadEG;

            AreasUniversidad areasUniversidad;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFSheet cuartaHoja = libroRegistro.getSheetAt(3);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Actividad Egresados-as")) || (segundaHoja.getSheetName().equals("Actividad Económica Eg")) || (tercerHoja.getSheetName().equals("Nivel Ocupación") || (cuartaHoja.getSheetName()).equals("Nivel Ingresos"))) {
//            Lectura de la primera hoja
                    for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if (fila.getCell(0).getNumericCellValue() != 0) {
                            actividadEG = new ActividadEgresadoGeneracion();
                            actividadEgresadoTipo = new ActividadEgresadoTipos();
                            dtoActividadEG = new DTOActividadEgresadoGeneracion();
                            areasUniversidad = new AreasUniversidad();
//                    Fecha de corte
                            if (fila.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                            actividadEG.setFecha(fila.getCell(0).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de corte en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }
//                    Generacion
                            if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoActividadEG.setGeneracion(fila.getCell(1).getStringCellValue());
                                        actividadEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }
//                    Programa Educativo
                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                                        areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                                        actividadEG.setProgramaEducativo(areasUniversidad.getArea());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }
//                    Tipo de actividad
                            if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case FORMULA:
                                        actividadEgresadoTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                                        actividadEgresadoTipo.setActividad((short) ((fila.getCell(6).getNumericCellValue())));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo de actividad en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }
//                    Hombres
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                        actividadEG.setHombres((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Numero de Hombres en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }
//                    Mujeres
                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                        actividadEG.setMujeres((int) fila.getCell(8).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Numero de mujeres en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }
//                    Total
                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoActividadEG.setTotal((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }
//                    Agregación de elementos a los objetos
                            actividadEG.setActividad(actividadEgresadoTipo);
                            dtoActividadEG.setProgramaEducativo(areasUniversidad);
                            dtoActividadEG.setActividadEgresadoGeneracion(actividadEG);

                            listaDtoActividadEG.add(dtoActividadEG);
                        }
                    }
                    libroRegistro.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return listaDtoActividadEG;
                    }

                } else {
                    libroRegistro.close();
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                    return Collections.EMPTY_LIST;
                }
            } catch (IOException e) {
                libroRegistro.close();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            Messages.addGlobalError("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTOActividadEconomicaEgresadoG> getListaActividadEconomicaEgresadoGeneracion(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

//        Vista
            List<DTOActividadEconomicaEgresadoG> listaDtoActividadEconomicaEG = new ArrayList<>();
            ActividadEconomicaEgresadoGeneracion actividadEconomicaEG;
            GirosTipo girosTipo;
            SectoresTipo sectoresTipo;
            DTOActividadEconomicaEgresadoG dtoActividadEconomicaEG;

            AreasUniversidad areasUniversidad;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFSheet cuartaHoja = libroRegistro.getSheetAt(3);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Actividad Egresados-as")) || (segundaHoja.getSheetName().equals("Actividad Económica Eg")) || (tercerHoja.getSheetName().equals("Nivel Ocupación") || (cuartaHoja.getSheetName()).equals("Nivel Ingresos"))) {
//            Lectura de la segunda hoja
                    for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                        if (fila.getCell(0).getNumericCellValue() != 0) {
                            actividadEconomicaEG = new ActividadEconomicaEgresadoGeneracion();
                            girosTipo = new GirosTipo();
                            sectoresTipo = new SectoresTipo();
                            dtoActividadEconomicaEG = new DTOActividadEconomicaEgresadoG();
                            areasUniversidad = new AreasUniversidad();
//                   Fecha de corte
                            if (fila.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                            actividadEconomicaEG.setFecha(fila.getCell(0).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de corte en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }
//                   Generación
                            if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoActividadEconomicaEG.setGeneracion(fila.getCell(1).getStringCellValue());
                                        actividadEconomicaEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }
//                   Programa Educativo
                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                                        areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                                        actividadEconomicaEG.setProgramaEducativo(areasUniversidad.getArea());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }
//                   Sector Trabajo
                            if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case FORMULA:
                                        sectoresTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                                        sectoresTipo.setSector((short) ((int) fila.getCell(6).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Sector en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }
//                   Cantidad Sector Trabajo
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                        actividadEconomicaEG.setCantSector((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cantidad Sector Trabajo en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }
//                   Actividad Económica
                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        girosTipo.setDescripcion(fila.getCell(8).getStringCellValue());
                                        girosTipo.setGiro((short) ((int) fila.getCell(9).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Actividad Económica en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }
//                   Cantidad Actividad Económica
                            if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(10).getCellTypeEnum()) {
                                    case NUMERIC:
                                        actividadEconomicaEG.setCantGiro((int) fila.getCell(10).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cantidad Actividad Económica en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                            }
//                   Agregación de elementos a los objetos
                            actividadEconomicaEG.setSector(sectoresTipo);
                            actividadEconomicaEG.setGiro(girosTipo);

                            dtoActividadEconomicaEG.setProgramaEducativo(areasUniversidad);
                            dtoActividadEconomicaEG.setActividadEconomicaEgresadoGeneracion(actividadEconomicaEG);

                            listaDtoActividadEconomicaEG.add(dtoActividadEconomicaEG);
                        }
                    }
                    libroRegistro.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return listaDtoActividadEconomicaEG;
                    }

                } else {
                    libroRegistro.close();
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                    return Collections.EMPTY_LIST;
                }
            } catch (IOException e) {
                libroRegistro.close();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            Messages.addGlobalError("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTONivelOcupacionEgresadosG> getListaNivelOcupacionEgresadoGeneracion(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
//        Vista
            List<DTONivelOcupacionEgresadosG> listaDtonivelOcupacionEG = new ArrayList<>();
            NivelOcupacionEgresadosGeneracion nivelOcupacionEG;
            NivelOcupacionTipos nivelOcupacionTipo;
            DTONivelOcupacionEgresadosG dtoNivelOcupacionEG;

            AreasUniversidad areasUniversidad;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFSheet cuartaHoja = libroRegistro.getSheetAt(3);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Actividad Egresados-as")) || (segundaHoja.getSheetName().equals("Actividad Económica Eg")) || (tercerHoja.getSheetName().equals("Nivel Ocupación") || (cuartaHoja.getSheetName()).equals("Nivel Ingresos"))) {
//            Lectura de la tercera hoja
                    for (int i = 2; i <= tercerHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) tercerHoja.getRow(i);
                        if (fila.getCell(0).getNumericCellValue() != 0) {
                            nivelOcupacionEG = new NivelOcupacionEgresadosGeneracion();
                            nivelOcupacionTipo = new NivelOcupacionTipos();
                            areasUniversidad = new AreasUniversidad();
                            dtoNivelOcupacionEG = new DTONivelOcupacionEgresadosG();
//                    Fecha de corte
                            if (fila.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                            nivelOcupacionEG.setFecha(fila.getCell(0).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de corte en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }

//                    Generación
                            if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoNivelOcupacionEG.setGeneracion(fila.getCell(1).getStringCellValue());
                                        nivelOcupacionEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

//                    Programa Educativo
                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                                        areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                                        nivelOcupacionEG.setProgramaEducativo(areasUniversidad.getArea());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }

//                    Ocupación
                            if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case FORMULA:
                                        nivelOcupacionTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                                        nivelOcupacionTipo.setOcupacion((short) ((int) fila.getCell(6).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Ocupación en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

//                    Hombres
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                        nivelOcupacionEG.setHombres((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Hombres en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

//                    Mujeres
                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                        nivelOcupacionEG.setMujeres((int) fila.getCell(8).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Mujeres en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

//                    Total
                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoNivelOcupacionEG.setTotal((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }

//                    Agregación de elementos a los objetos
                            nivelOcupacionEG.setOcupacion(nivelOcupacionTipo);

                            dtoNivelOcupacionEG.setProgramaEducativo(areasUniversidad);
                            dtoNivelOcupacionEG.setNivelOcupacionEgresadosGeneracion(nivelOcupacionEG);

                            listaDtonivelOcupacionEG.add(dtoNivelOcupacionEG);
                        }
                    }
                    libroRegistro.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return listaDtonivelOcupacionEG;
                    }
                } else {
                    libroRegistro.close();
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                    return Collections.EMPTY_LIST;
                }
            } catch (IOException e) {
                libroRegistro.close();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            Messages.addGlobalError("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTONivelIngresoEgresadosG> getListaNivelIngresoEgresadoGeneracion(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
            //        Vista
            List<DTONivelIngresoEgresadosG> listaDtoNivelIngresoEG = new ArrayList<>();
            NivelIngresosEgresadosGeneracion nivelIngresosEG;
            NivelIngresosTipos nivelIngresoTipo;
            DTONivelIngresoEgresadosG dtoNivelIngresoEG;

            AreasUniversidad areasUniversidad;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFSheet cuartaHoja = libroRegistro.getSheetAt(3);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Actividad Egresados-as")) || (segundaHoja.getSheetName().equals("Actividad Económica Eg")) || (tercerHoja.getSheetName().equals("Nivel Ocupación") || (cuartaHoja.getSheetName()).equals("Nivel Ingresos"))) {
//            Lectura de la cuarta hoja
                    for (int i = 2; i <= cuartaHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) cuartaHoja.getRow(i);
                        if (fila.getCell(0).getNumericCellValue() != 0) {
                            nivelIngresosEG = new NivelIngresosEgresadosGeneracion();
                            nivelIngresoTipo = new NivelIngresosTipos();
                            areasUniversidad = new AreasUniversidad();
                            dtoNivelIngresoEG = new DTONivelIngresoEgresadosG();

//                    Fecha de corte
                            if (fila.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                            nivelIngresosEG.setFecha(fila.getCell(0).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de corte en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }

//                    Generación
                            if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoNivelIngresoEG.setGeneracion(fila.getCell(1).getStringCellValue());
                                        nivelIngresosEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

//                    Programa Educativo
                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                                        areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                                        nivelIngresosEG.setProgramaEducativo(areasUniversidad.getArea());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }

//                    Ingreso
                            if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case FORMULA:
                                        nivelIngresoTipo.setIngresos(fila.getCell(5).getStringCellValue());
                                        nivelIngresoTipo.setNivel((short) ((int) fila.getCell(6).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nivel de Ingreso en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

//                    Hombres
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                        nivelIngresosEG.setHombres((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Hombres en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

//                    Mujeres
                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                        nivelIngresosEG.setMujeres((int) fila.getCell(8).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Mujeres en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

//                    Total
                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoNivelIngresoEG.setTotal((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }
                            nivelIngresosEG.setIngreso(nivelIngresoTipo);

                            dtoNivelIngresoEG.setProgramaEducativo(areasUniversidad);
                            dtoNivelIngresoEG.setNivelIngresosEgresadosGeneracion(nivelIngresosEG);

                            listaDtoNivelIngresoEG.add(dtoNivelIngresoEG);
                        }
                    }
                    libroRegistro.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return listaDtoNivelIngresoEG;
                    }

                } else {
                    libroRegistro.close();
                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                    return Collections.EMPTY_LIST;
                }

            } catch (IOException e) {
                libroRegistro.close();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalError("<b>Ocurrió un error durante la lectura del archivo, asegurese de haber registrado correctamente su información</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            Messages.addGlobalError("<b>Ocurrio un error en la lectura del archivo</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void guardaActividadEgresadoGeneracion(List<DTOActividadEgresadoGeneracion> listaActividadEgresadoGeneracion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Actividades Egresados
        List<String> listaCondicional = new ArrayList<>();
        listaActividadEgresadoGeneracion.forEach((actividadEG) -> {
            facadeVinculacion.setEntityClass(ActividadEgresadoGeneracion.class);
            ActividadEgresadoGeneracion actividadEGEncontrado = getActividadEgresadoGeneracion(actividadEG.getActividadEgresadoGeneracion());
            Boolean registroAlmacenado = false;
            if (actividadEGEncontrado != null) {
                listaCondicional.add(actividadEG.getActividadEgresadoGeneracion().getFecha() + " " + actividadEG.getGeneracion() + " " + actividadEG.getProgramaEducativo().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                
                    actividadEG.getActividadEgresadoGeneracion().setRegistro(actividadEGEncontrado.getRegistro());
                    facadeVinculacion.edit(actividadEG.getActividadEgresadoGeneracion());
                
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                actividadEG.getActividadEgresadoGeneracion().setRegistro(registro.getRegistro());
                facadeVinculacion.create(actividadEG.getActividadEgresadoGeneracion());
            }
            facadeVinculacion.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaActividadEcnomicaEgresadoG(List<DTOActividadEconomicaEgresadoG> listaActividadEconomicaEgresadoG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Actividades Económicas Egresados
        List<String> listaCondicional = new ArrayList<>();
        listaActividadEconomicaEgresadoG.forEach((actividadEconomicaEG) -> {

            facadeVinculacion.setEntityClass(ActividadEconomicaEgresadoGeneracion.class);
            ActividadEconomicaEgresadoGeneracion actividadEEGEncontrado = getActividadEconomicaEgresadoGeneracion(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion());
            Boolean registroAlmacenado = false;
            if (actividadEEGEncontrado != null) {
                listaCondicional.add(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion().getFecha() + " " + actividadEconomicaEG.getProgramaEducativo().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                
                    actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion().setRegistro(actividadEEGEncontrado.getRegistro());
                    facadeVinculacion.edit(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion());
                
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion().setRegistro(registro.getRegistro());
                facadeVinculacion.create(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion());                
            }
            facadeVinculacion.flush();

        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaNivelOcupacionEgresadoG(List<DTONivelOcupacionEgresadosG> listaNivelOcupacionEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Nivel Ocupación
        List<String> listaCondicional = new ArrayList<>();
        listaNivelOcupacionEgresadosG.forEach((nivelOcupacionEG) -> {
            
                facadeVinculacion.setEntityClass(NivelOcupacionEgresadosGeneracion.class);
                NivelOcupacionEgresadosGeneracion nivelOEGEncontrado = getNivelOcupacionEgresadosGeneracion(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion());
                Boolean registroAlmacenado = false;
                if (nivelOEGEncontrado != null) {
                    listaCondicional.add(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion().getFecha() + " " + nivelOcupacionEG.getProgramaEducativo().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                   
                        nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion().setRegistro(nivelOEGEncontrado.getRegistro());
                        facadeVinculacion.edit(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion());
                    
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion().setRegistro(registro.getRegistro());
                    facadeVinculacion.create(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion());
                    
                    
                }
                facadeVinculacion.flush();
            
            
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaNivelIngresoEgresadoG(List<DTONivelIngresoEgresadosG> listaNivelIngresoEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Nivel Ingresos
        List<String> listaCondicional = new ArrayList<>();
        listaNivelIngresoEgresadosG.forEach((nivelIngresoEG) -> {
            
                facadeVinculacion.setEntityClass(NivelIngresosEgresadosGeneracion.class);
                NivelIngresosEgresadosGeneracion nivelIEGEncontrado = getNivelIngresosEgresadosGeneracion(nivelIngresoEG.getNivelIngresosEgresadosGeneracion());
                Boolean registroAlmacenado = false;
                if (nivelIEGEncontrado != null) {
                    listaCondicional.add(nivelIngresoEG.getNivelIngresosEgresadosGeneracion().getFecha() + " " + nivelIngresoEG.getProgramaEducativo().getNombre());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    
                        nivelIngresoEG.getNivelIngresosEgresadosGeneracion().setRegistro(nivelIEGEncontrado.getRegistro());
                        facadeVinculacion.edit(nivelIngresoEG.getNivelIngresosEgresadosGeneracion());
                    
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    nivelIngresoEG.getNivelIngresosEgresadosGeneracion().setRegistro(registro.getRegistro());
                    facadeVinculacion.create(nivelIngresoEG.getNivelIngresosEgresadosGeneracion());
                }
                facadeVinculacion.flush();
            
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ActividadEgresadoGeneracion getActividadEgresadoGeneracion(ActividadEgresadoGeneracion actividadEgresadoGeneracion) {
        TypedQuery<ActividadEgresadoGeneracion> query = facadeVinculacion.getEntityManager().createQuery("SELECT a FROM ActividadEgresadoGeneracion a JOIN a.actividad t WHERE a.fecha = :fecha AND a.generacion = :generacion AND a.programaEducativo = :programaEducativo AND t.actividad = :actividad", ActividadEgresadoGeneracion.class);
        query.setParameter("fecha", actividadEgresadoGeneracion.getFecha());
        query.setParameter("generacion", actividadEgresadoGeneracion.getGeneracion());
        query.setParameter("programaEducativo", actividadEgresadoGeneracion.getProgramaEducativo());
        query.setParameter("actividad", actividadEgresadoGeneracion.getActividad().getActividad());
        try {
            actividadEgresadoGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            actividadEgresadoGeneracion = null;
            ex.toString();
        }
        return actividadEgresadoGeneracion;
    }

    @Override
    public ActividadEconomicaEgresadoGeneracion getActividadEconomicaEgresadoGeneracion(ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion) {
        TypedQuery<ActividadEconomicaEgresadoGeneracion> query = facadeVinculacion.getEntityManager().createQuery("SELECT a FROM ActividadEconomicaEgresadoGeneracion a JOIN a.sector s JOIN a.giro g WHERE a.fecha = :fecha AND a.generacion = :generacion AND a.programaEducativo = :programaEducativo AND s.sector = :sector AND g.giro = :giro", ActividadEconomicaEgresadoGeneracion.class);
        query.setParameter("fecha", actividadEconomicaEgresadoGeneracion.getFecha());
        query.setParameter("generacion", actividadEconomicaEgresadoGeneracion.getGeneracion());
        query.setParameter("programaEducativo", actividadEconomicaEgresadoGeneracion.getProgramaEducativo());
        query.setParameter("sector", actividadEconomicaEgresadoGeneracion.getSector().getSector());
        query.setParameter("giro", actividadEconomicaEgresadoGeneracion.getGiro().getGiro());
        try {
            actividadEconomicaEgresadoGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            actividadEconomicaEgresadoGeneracion = null;
            ex.toString();
        }
        return actividadEconomicaEgresadoGeneracion;
    }

    @Override
    public NivelOcupacionEgresadosGeneracion getNivelOcupacionEgresadosGeneracion(NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion) {
        TypedQuery<NivelOcupacionEgresadosGeneracion> query = facadeVinculacion.getEntityManager().createQuery("SELECT n FROM NivelOcupacionEgresadosGeneracion n JOIN n.ocupacion o WHERE n.fecha = :fecha AND n.generacion = :generacion AND n.programaEducativo = :programaEducativo AND o.ocupacion = :ocupacion", NivelOcupacionEgresadosGeneracion.class);
        query.setParameter("fecha", nivelOcupacionEgresadosGeneracion.getFecha());
        query.setParameter("generacion", nivelOcupacionEgresadosGeneracion.getGeneracion());
        query.setParameter("programaEducativo", nivelOcupacionEgresadosGeneracion.getProgramaEducativo());
        query.setParameter("ocupacion", nivelOcupacionEgresadosGeneracion.getOcupacion().getOcupacion());
        try {
            nivelOcupacionEgresadosGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            nivelOcupacionEgresadosGeneracion = null;
            ex.toString();
        }
        return nivelOcupacionEgresadosGeneracion;
    }

    @Override
    public NivelIngresosEgresadosGeneracion getNivelIngresosEgresadosGeneracion(NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion) {
        TypedQuery<NivelIngresosEgresadosGeneracion> query = facadeVinculacion.getEntityManager().createQuery("SELECT n FROM NivelIngresosEgresadosGeneracion n JOIN n.ingreso i WHERE n.fecha = :fecha AND n.generacion = :generacion AND n.programaEducativo = :programaEducativo AND i.nivel = :nivel", NivelIngresosEgresadosGeneracion.class);
        query.setParameter("fecha", nivelIngresosEgresadosGeneracion.getFecha());
        query.setParameter("generacion", nivelIngresosEgresadosGeneracion.getGeneracion());
        query.setParameter("programaEducativo", nivelIngresosEgresadosGeneracion.getProgramaEducativo());
        query.setParameter("nivel", nivelIngresosEgresadosGeneracion.getIngreso().getNivel());
        try {
            nivelIngresosEgresadosGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            nivelIngresosEgresadosGeneracion = null;
            ex.toString();
        }
        return nivelIngresosEgresadosGeneracion;
    }

    @Override
    public List<ActividadEgresadoTipos> getActividadEgresadoTipos() throws Throwable {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT a FROM ActividadEgresadoTipos a ORDER BY a.descripcion", ActividadEgresadoTipos.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<NivelOcupacionTipos> getNivelOcupacionTipos() throws Throwable {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT n FROM NivelOcupacionTipos n ORDER BY n.descripcion", NivelOcupacionTipos.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<NivelIngresosTipos> getNivelIngresosTipos() throws Throwable {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT n FROM NivelIngresosTipos n ORDER BY n.nivel", NivelIngresosTipos.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTOActividadEgresadoGeneracion> getFiltroActividadEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area) {
        List<DTOActividadEgresadoGeneracion> listaDto = new ArrayList<>();
        List<ActividadEgresadoGeneracion> actividaEgresados = new ArrayList<>();
        try {
            actividaEgresados = facadeVinculacion.getEntityManager().createQuery("SELECT aeg FROM ActividadEgresadoGeneracion aeg JOIN aeg.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ActividadEgresadoGeneracion.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            
            actividaEgresados.forEach((aeg) -> {
                facadeVinculacion.getEntityManager().refresh(aeg);
                listaDto.add(new DTOActividadEgresadoGeneracion(
                        aeg,
                        caster.generacionToString(facadeVinculacion.getEntityManager().find(Generaciones.class, aeg.getGeneracion())),
                        facadeVinculacion.getEntityManager().find(AreasUniversidad.class, aeg.getProgramaEducativo()),
                        aeg.getHombres()+aeg.getMujeres()
                ));
            });
            return listaDto;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTOActividadEconomicaEgresadoG> getFiltroActividadEconomicaEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area) {
        List<DTOActividadEconomicaEgresadoG> listaDto = new ArrayList<>();
        List<ActividadEconomicaEgresadoGeneracion> actividaEconomicaEgresadoGeneracion = new ArrayList<>();
        try {
            actividaEconomicaEgresadoGeneracion = facadeVinculacion.getEntityManager().createQuery("SELECT aeeg FROM ActividadEconomicaEgresadoGeneracion aeeg JOIN aeeg.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ActividadEconomicaEgresadoGeneracion.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            
            actividaEconomicaEgresadoGeneracion.forEach((aeeg) -> {
                facadeVinculacion.getEntityManager().refresh(aeeg);
                listaDto.add(new DTOActividadEconomicaEgresadoG(
                        aeeg,
                        caster.generacionToString(facadeVinculacion.getEntityManager().find(Generaciones.class, aeeg.getGeneracion())),
                        facadeVinculacion.getEntityManager().find(AreasUniversidad.class, aeeg.getProgramaEducativo())
                ));
            });
            return listaDto;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTONivelOcupacionEgresadosG> getFiltroActividadNivelOcupacionEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area) {
        List<DTONivelOcupacionEgresadosG> listaDto = new ArrayList<>();
        List<NivelOcupacionEgresadosGeneracion> nivelOcupacionEgresados = new ArrayList<>();
        try {
            nivelOcupacionEgresados = facadeVinculacion.getEntityManager().createQuery("SELECT noeg FROM NivelOcupacionEgresadosGeneracion noeg JOIN noeg.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", NivelOcupacionEgresadosGeneracion.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            
            nivelOcupacionEgresados.forEach((noeg) -> {
                facadeVinculacion.getEntityManager().refresh(noeg);
                listaDto.add(new DTONivelOcupacionEgresadosG(
                        noeg,
                        caster.generacionToString(facadeVinculacion.getEntityManager().find(Generaciones.class, noeg.getGeneracion())),
                        facadeVinculacion.getEntityManager().find(AreasUniversidad.class, noeg.getProgramaEducativo()),
                        noeg.getHombres()+noeg.getMujeres()
                ));
            });
            return listaDto;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTONivelIngresoEgresadosG> getFiltroNivelIngresoEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area) {
        List<DTONivelIngresoEgresadosG> listaDto = new ArrayList<>();
        List<NivelIngresosEgresadosGeneracion> nivelIngresos = new ArrayList<>();
        try {
            nivelIngresos = facadeVinculacion.getEntityManager().createQuery("SELECT nieg FROM NivelIngresosEgresadosGeneracion nieg JOIN nieg.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", NivelIngresosEgresadosGeneracion.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            
            nivelIngresos.forEach((nieg) -> {
                facadeVinculacion.getEntityManager().refresh(nieg);
                listaDto.add(new DTONivelIngresoEgresadosG(
                        nieg,
                        caster.generacionToString(facadeVinculacion.getEntityManager().find(Generaciones.class, nieg.getGeneracion())),
                        facadeVinculacion.getEntityManager().find(AreasUniversidad.class, nieg.getProgramaEducativo()),
                        nieg.getHombres()+nieg.getMujeres()
                ));
            });
            return listaDto;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<ActividadEgresadoGeneracion> getReporteActividadEgresadoPorEjercicio() {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT a FROM ActividadEgresadoGeneracion a INNER JOIN a.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY a.fecha",ActividadEgresadoGeneracion.class)
                    .setParameter("ejercicioFiscal", ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<ActividadEconomicaEgresadoGeneracion> getReporteActividadEconomicaEgresadoPorEjercicio() {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT a FROM ActividadEconomicaEgresadoGeneracion a INNER JOIN a.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY a.fecha",ActividadEconomicaEgresadoGeneracion.class)
                    .setParameter("ejercicioFiscal", ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<NivelOcupacionEgresadosGeneracion> getReporteNivelOcupacionEgresadoPorEjercicio() {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT n FROM NivelOcupacionEgresadosGeneracion n INNER JOIN n.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY n.fecha",NivelOcupacionEgresadosGeneracion.class)
                    .setParameter("ejercicioFiscal", ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<NivelIngresosEgresadosGeneracion> getReporteNivelIngresosEgresadoPorEjercicio() {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT n FROM NivelIngresosEgresadosGeneracion n INNER JOIN n.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY n.fecha",NivelIngresosEgresadosGeneracion.class)
                    .setParameter("ejercicioFiscal", ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio())
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }
    
    
}
