/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoInternetCPE;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionEquipamiento;
import org.apache.poi.ss.usermodel.CellType;
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
@Stateful
public class ServicioDistribucionEquipamiento implements EjbDistribucionEquipamiento {

    @EJB    Facade facadeServGen;
    @EJB    EjbModulos ejbModulos;
    
    @Inject Caster caster;
    
    @Override
    public List<DTOEquiposComputoCPE> getListaEquiposComputoCPE(String rutaArchivo) throws Throwable{
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

//          Listas para muestra del usuario
            List<DTOEquiposComputoCPE> dtoEquiposComputoCPE = new ArrayList<>();
            EquiposComputoCicloPeriodoEscolar equipoComputoCPE;
            DTOEquiposComputoCPE dtoEquipoComputoCPE;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFRow fila;
            
            try{
            if ((primeraHoja.getSheetName().equals("Distribución_Equipo_Computo")) || (segundaHoja.getSheetName().equals("Distribucion_Equipo_Comp_Int"))) {
//            Lectura de la primera hoja
                for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if (fila.getCell(1).getNumericCellValue() != 0) {
                        equipoComputoCPE = new EquiposComputoCicloPeriodoEscolar();
                        dtoEquipoComputoCPE = new DTOEquiposComputoCPE();

                        if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(1).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                                    equipoComputoCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Ciclo Escolar en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(3).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(3).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                                    equipoComputoCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(4).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoCPE.setTotal((int) fila.getCell(4).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Total en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(5).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(5).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoCPE.setEscritorio((int) fila.getCell(5).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Equipos Escritorio en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(6).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoCPE.setPortatiles((int) fila.getCell(6).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Equipos Portatiles en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(7).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setDtcEsc((int) fila.getCell(7).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes de tiempo completo escritorio en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(8).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setDtcPort((int) fila.getCell(8).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes de tiempo completo portatiles en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(9).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setDaEsc((int) fila.getCell(9).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Profesores de asignatura escritorio en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(10).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setDaPort((int) fila.getCell(10).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Profesores de asignatura portatiles en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(11).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(11).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setEstEsc((int) fila.getCell(11).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estudiantes Escritorio en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(12).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(12).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setEstPor((int) fila.getCell(12).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estudiantes Portatiles en la columna: " + (12 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(13).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(13).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setAdmEsc((int) fila.getCell(13).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Administrativos Escritorio en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(14).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(14).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setAdmPort((int) fila.getCell(14).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Administrativos Portatiles en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(15).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(15).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setMmsEsc((int) fila.getCell(15).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Mandos Medios y Superiores Escritorio en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(16).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(16).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoCPE.setMmsPort((int) fila.getCell(16).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Mandos Medios y Superiores Portatil en la columna: " + (16 + 1) + " y fila: " + (i + 1));
                        }

                        dtoEquipoComputoCPE.setEquiposComputoCicloPeriodoEscolar(equipoComputoCPE);
                        dtoEquiposComputoCPE.add(dtoEquipoComputoCPE);
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
                    return dtoEquiposComputoCPE;
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
    public List<DTOEquiposComputoInternetCPE> getListaEquiposComputoInternetCPE(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {

            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOEquiposComputoInternetCPE> dtoEquiposComputoInternetCPE = new ArrayList<>();
            EquiposComputoInternetCicloPeriodoEscolar equipoComputoInternetCPE;
            DTOEquiposComputoInternetCPE dtoEquipoComputoInternetCPE;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFRow fila;
            
            try{
            if ((primeraHoja.getSheetName().equals("Distribución_Equipo_Computo")) || (segundaHoja.getSheetName().equals("Distribucion_Equipo_Comp_Int"))) {
//            Lectura de la segunda hoja
                for (int i = 3; i <= segundaHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                    if (fila.getCell(1).getNumericCellValue() != 0) {
                        equipoComputoInternetCPE = new EquiposComputoInternetCicloPeriodoEscolar();
                        dtoEquipoComputoInternetCPE = new DTOEquiposComputoInternetCPE();

                        if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(1).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoInternetCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                                    equipoComputoInternetCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Ciclo Escolar en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(3).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(3).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoInternetCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                                    equipoComputoInternetCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(4).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoInternetCPE.setTotal((int) fila.getCell(4).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Total en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(5).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(5).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoInternetCPE.setEscritorio((int) fila.getCell(5).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Equipos de escritorio en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(6).getCellTypeEnum()) {
                                case FORMULA:
                                    dtoEquipoComputoInternetCPE.setPortatiles((int) fila.getCell(6).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Equipos portatiles en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(7).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setDtcEsc((int) fila.getCell(7).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes de tiempo completo Escritorio en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(8).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setDtcPort((int) fila.getCell(8).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes de tiempo completo Portatiles en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(9).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setDaEsc((int) fila.getCell(9).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes de Asignatura Escritorio en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(10).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setDaPort((int) fila.getCell(10).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Docentes de Asignatura Portatiles en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(11).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(11).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setEstEsc((int) fila.getCell(11).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estudiantes Escritorio en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(12).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(12).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setEstPor((int) fila.getCell(12).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estudiantes Portatiles en la columna: " + (12 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(13).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(13).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setAdmEsc((int) fila.getCell(13).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Administrativos Escritorio en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(14).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(14).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setAdmPort((int) fila.getCell(14).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Administrativos Portatiles en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(15).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(15).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setMmsEsc((int) fila.getCell(15).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Mandos Medios y Superiores Escritorio en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                        }

                        if (fila.getCell(16).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(16).getCellTypeEnum()) {
                                case NUMERIC:
                                    equipoComputoInternetCPE.setMmsPort((int) fila.getCell(16).getNumericCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Mandos Medios y Superiores Portatiles en la columna: " + (16 + 1) + " y fila: " + (i + 1));
                        }

                        dtoEquipoComputoInternetCPE.setEquiposComputoInternetCicloPeriodoEscolar(equipoComputoInternetCPE);
                        dtoEquiposComputoInternetCPE.add(dtoEquipoComputoInternetCPE);
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
                    return dtoEquiposComputoInternetCPE;
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
    public void guardaEquipoComputoCPE(List<DTOEquiposComputoCPE> listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionEquipamiento.forEach((equiposComputoCPE) -> {
            if(ejbModulos.getPeriodoEscolarActivo().getAnio() == (facadeServGen.getEntityManager().find(PeriodosEscolares.class, equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar().getPeriodoEscolar()).getAnio())){
                facadeServGen.setEntityClass(EquiposComputoCicloPeriodoEscolar.class);
                EquiposComputoCicloPeriodoEscolar equipoEncontrado = getEquiposComputCicloPeriodoEscolar(equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar());
                Boolean registroAlmacenado = false;
                if (equipoEncontrado != null) {
                    listaCondicional.add(equiposComputoCPE.getCicloEscolar() + " " + equiposComputoCPE.getPeriodoEscolar());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getPeriodoEscolarActivo().getAnio() == equipoEncontrado.getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()) {
                        equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar().setRegistro(equipoEncontrado.getRegistro());
                        facadeServGen.edit(equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar());
                    } else {
                        listaCondicional.remove(equiposComputoCPE.getCicloEscolar() + " " + equiposComputoCPE.getPeriodoEscolar());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar().setRegistro(registro.getRegistro());
                    facadeServGen.create(equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar());
                }
                facadeServGen.flush();
            }
        });
        Messages.addGlobalInfo("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaEquipoComputoInternetCPE(List<DTOEquiposComputoInternetCPE> listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionEquipamiento.forEach((equiposComputoInternetCPE) -> {
            if(ejbModulos.getPeriodoEscolarActivo().getAnio() == (facadeServGen.getEntityManager().find(PeriodosEscolares.class, equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar().getPeriodoEscolar()).getAnio())){
                facadeServGen.setEntityClass(EquiposComputoInternetCicloPeriodoEscolar.class);
                EquiposComputoInternetCicloPeriodoEscolar equipoInternetEncontrado = getEquiposComputoInternetCicloPeriodoEscolar(equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar());
                Boolean registroAlmacenado = false;
                if (equipoInternetEncontrado != null) {
                    listaCondicional.add(equiposComputoInternetCPE.getCicloEscolar() + " " + equiposComputoInternetCPE.getPeriodoEscolar());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getPeriodoEscolarActivo().getAnio() == equipoInternetEncontrado.getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()) {
                        equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar().setRegistro(equipoInternetEncontrado.getRegistro());
                        facadeServGen.edit(equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar());
                    } else {
                        listaCondicional.remove(equiposComputoInternetCPE.getCicloEscolar() + " " + equiposComputoInternetCPE.getPeriodoEscolar());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar().setRegistro(registro.getRegistro());
                    facadeServGen.create(equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar());
                }
                facadeServGen.flush();
            }
        });
        Messages.addGlobalInfo("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public EquiposComputoCicloPeriodoEscolar getEquiposComputCicloPeriodoEscolar(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar) {
        TypedQuery<EquiposComputoCicloPeriodoEscolar> query = facadeServGen.getEntityManager().createQuery("SELECT e FROM EquiposComputoCicloPeriodoEscolar e WHERE e.cicloEscolar = :cicloEscolar AND e.periodoEscolar = :periodoEscolar", EquiposComputoCicloPeriodoEscolar.class);
        query.setParameter("cicloEscolar", equiposComputoCicloPeriodoEscolar.getCicloEscolar());
        query.setParameter("periodoEscolar", equiposComputoCicloPeriodoEscolar.getPeriodoEscolar());
        try {
            equiposComputoCicloPeriodoEscolar = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            equiposComputoCicloPeriodoEscolar = null;
            ex.toString();
        }
        return equiposComputoCicloPeriodoEscolar;
    }

    @Override
    public EquiposComputoInternetCicloPeriodoEscolar getEquiposComputoInternetCicloPeriodoEscolar(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar) {
        TypedQuery<EquiposComputoInternetCicloPeriodoEscolar> query = facadeServGen.getEntityManager().createQuery("SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.cicloEscolar = :cicloEscolar AND e.periodoEscolar = :periodoEscolar", EquiposComputoInternetCicloPeriodoEscolar.class);
        query.setParameter("cicloEscolar", equiposComputoInternetCicloPeriodoEscolar.getCicloEscolar());
        query.setParameter("periodoEscolar", equiposComputoInternetCicloPeriodoEscolar.getPeriodoEscolar());
        try {
            equiposComputoInternetCicloPeriodoEscolar = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            equiposComputoInternetCicloPeriodoEscolar = null;
            ex.toString();
        }
        return equiposComputoInternetCicloPeriodoEscolar;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipoEqC, RegistrosTipo registrosTipoEqCI, EventosRegistros eventoRegistro, AreasUniversidad area) {
        List<Integer> claves1 = facadeServGen.getEntityManager().createQuery("SELECT ecpe.periodoEscolar FROM EquiposComputoCicloPeriodoEscolar ecpe INNER JOIN ecpe.registros r WHERE r.tipo.registroTipo=:tipo AND r.area = :area", Integer.class)
                .setParameter("tipo", registrosTipoEqC.getRegistroTipo())
                .setParameter("area", area.getArea())
                .getResultList();
        
        List<Integer> claves2 = facadeServGen.getEntityManager().createQuery("SELECT ecipe.periodoEscolar FROM EquiposComputoInternetCicloPeriodoEscolar ecipe INNER JOIN ecipe.registros r WHERE r.tipo.registroTipo=:tipo AND r.area = :area",Integer.class)
                .setParameter("tipo", registrosTipoEqCI.getRegistroTipo())
                .setParameter("area", area.getArea())
                .getResultList();
        
        List<Integer> claves = new ArrayList<>();
        
        claves1.stream().forEach((t) -> {
            claves.add(t);
        });
        
        claves2.stream().forEach((t) -> {
            claves.add(t);
        });
        
        List<PeriodosEscolares> l = new ArrayList<>();
        if(claves.isEmpty()){
            l = facadeServGen.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero) AND (p.anio = :anio)",PeriodosEscolares.class)
                    .setParameter("mes", ejbModulos.getNumeroMes(eventoRegistro.getMes()))
                    .setParameter("anio", eventoRegistro.getEjercicioFiscal().getAnio())
                    .getResultList();
        }else{
            l = facadeServGen.getEntityManager().createQuery("SELECT periodo FROM PeriodosEscolares periodo WHERE periodo.periodo IN :claves ORDER BY periodo.periodo desc", PeriodosEscolares.class)
                    .setParameter("claves", claves)
                    .getResultList();
        }
        return l;
    }

    @Override
    public List<DTOEquiposComputoCPE> getListaEquiposComputoPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        AreasUniversidad area = facadeServGen.getEntityManager().find(AreasUniversidad.class, claveArea);

        List<DTOEquiposComputoCPE> l = new ArrayList<>();
        List<EquiposComputoCicloPeriodoEscolar> entities = facadeServGen.getEntityManager().createQuery("SELECT eccpe FROM EquiposComputoCicloPeriodoEscolar eccpe INNER JOIN eccpe.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE r.area = :area AND eccpe.periodoEscolar = :periodo AND t.registroTipo=:tipo", EquiposComputoCicloPeriodoEscolar.class)
                .setParameter("area", area.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        entities.forEach(e -> {
            facadeServGen.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
            if (a != null) {
                l.add(new DTOEquiposComputoCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        e.getDtcEsc()+e.getDtcPort()+e.getDaEsc()+e.getDaPort()+e.getEstEsc()+e.getEstPor()+e.getAdmEsc()+e.getAdmPort()+e.getMmsEsc()+e.getMmsPort(),
                        e.getDtcEsc()+e.getDaEsc()+e.getEstEsc()+e.getAdmEsc()+e.getMmsEsc(),
                        e.getDtcPort()+e.getDaPort()+e.getEstPor()+e.getAdmPort()+e.getMmsPort(),
                        a
                ));
            } else {
                l.add(new DTOEquiposComputoCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        e.getDtcEsc()+e.getDtcPort()+e.getDaEsc()+e.getDaPort()+e.getEstEsc()+e.getEstPor()+e.getAdmEsc()+e.getAdmPort()+e.getMmsEsc()+e.getMmsPort(),
                        e.getDtcEsc()+e.getDaEsc()+e.getEstEsc()+e.getAdmEsc()+e.getMmsEsc(),
                        e.getDtcPort()+e.getDaPort()+e.getEstPor()+e.getAdmPort()+e.getMmsPort()
                ));
            }
        });
        return l;
    }

    @Override
    public List<DTOEquiposComputoInternetCPE> getListaEquiposComputoInternetPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        AreasUniversidad area = facadeServGen.getEntityManager().find(AreasUniversidad.class, claveArea);

        List<DTOEquiposComputoInternetCPE> l = new ArrayList<>();
        List<EquiposComputoInternetCicloPeriodoEscolar> entities = facadeServGen.getEntityManager().createQuery("SELECT ecicpe FROM EquiposComputoInternetCicloPeriodoEscolar ecicpe INNER JOIN ecicpe.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE r.area = :area AND ecicpe.periodoEscolar = :periodo AND t.registroTipo=:tipo", EquiposComputoInternetCicloPeriodoEscolar.class)
                .setParameter("area", area.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        
        entities.forEach(e -> {
            facadeServGen.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
            if (a != null) {
                l.add(new DTOEquiposComputoInternetCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        e.getDtcEsc()+e.getDtcPort()+e.getDaEsc()+e.getDaPort()+e.getEstEsc()+e.getEstPor()+e.getAdmEsc()+e.getAdmPort()+e.getMmsEsc()+e.getMmsPort(),
                        e.getDtcEsc()+e.getDaEsc()+e.getEstEsc()+e.getAdmEsc()+e.getMmsEsc(),
                        e.getDtcPort()+e.getDaPort()+e.getEstPor()+e.getAdmPort()+e.getMmsPort(),
                        a
                ));
            } else {
                l.add(new DTOEquiposComputoInternetCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        e.getDtcEsc()+e.getDtcPort()+e.getDaEsc()+e.getDaPort()+e.getEstEsc()+e.getEstPor()+e.getAdmEsc()+e.getAdmPort()+e.getMmsEsc()+e.getMmsPort(),
                        e.getDtcEsc()+e.getDaEsc()+e.getEstEsc()+e.getAdmEsc()+e.getMmsEsc(),
                        e.getDtcPort()+e.getDaPort()+e.getEstPor()+e.getAdmPort()+e.getMmsPort()
                ));
            }
        });
        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipoEC, RegistrosTipo registrosTipoECI, AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException {
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro(registrosTipoEC,registrosTipoECI,eventoActual,area);
        if(periodos==null || periodos.isEmpty()) return null;
        if(eventoActual == null) eventoActual = ejbModulos.getEventoRegistro();
        if(eventoActual == null) return null;

        PeriodosEscolares reciente = periodos.get(0);
        Boolean existe = eventos.contains(eventoActual);
        if(!existe){
            if(eventos.size() <3){
                eventos = new ArrayList<>(Stream.concat(Stream.of(eventoActual), eventos.stream()).collect(Collectors.toList()));
            }else{
                PeriodosEscolares periodo = facadeServGen.getEntityManager().find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
                if(periodo == null) throw new PeriodoEscolarNecesarioNoRegistradoException(reciente.getPeriodo() + 1, caster.periodoToString(reciente));
                periodos = new ArrayList<>(Stream.concat(Stream.of(periodo), periodos.stream()).collect(Collectors.toList()));
                eventos.clear();
                eventos.add(eventoActual);
            }
        }
        Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
        map.put(periodos, eventos);
        return map.entrySet().iterator().next();
    }
    
}
