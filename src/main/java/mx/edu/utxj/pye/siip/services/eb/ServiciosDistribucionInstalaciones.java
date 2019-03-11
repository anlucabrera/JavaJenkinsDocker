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
import mx.edu.utxj.pye.sgi.entity.pye2.AulasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaTiposInstalaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionAulasCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.eb.DTOCapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionAulasCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionLabTallCPE;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionInstalaciones;
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
@Stateful
public class ServiciosDistribucionInstalaciones implements EjbDistribucionInstalaciones {
    
    @EJB Facade facadeServGen;
    @EJB EjbModulos ejbModulos;
    
    @Inject Caster caster;

    @Override
    public List<DTOCapacidadInstaladaCiclosEscolares> getListaCapacidadInstaladaCiclosEScolares(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

//          Listas para muestra del usuario
            List<DTOCapacidadInstaladaCiclosEscolares> dtoCapacidadInstaladaCiclosEscolares = new ArrayList<>();
            CapacidadInstaladaCiclosEscolares capacidadInstaladaCE;
            CapacidadInstaladaTiposInstalaciones capacidadInstaladaTiposInstalaciones;
            DTOCapacidadInstaladaCiclosEscolares dtoCapacidadInstaladaCE;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Capacidad Instalada")) || (segundaHoja.getSheetName().equals("Distribución de Aulas")) || (tercerHoja.getSheetName().equals("Distribución de Lab. y Tall."))) {
//            Lectura de la primer hoja

                    for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if (fila.getCell(1).getNumericCellValue() != 0) {
                            capacidadInstaladaCE = new CapacidadInstaladaCiclosEscolares();
                            dtoCapacidadInstaladaCE = new DTOCapacidadInstaladaCiclosEscolares();
                            capacidadInstaladaTiposInstalaciones = new CapacidadInstaladaTiposInstalaciones();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoCapacidadInstaladaCE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                                        capacidadInstaladaCE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
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
                                        capacidadInstaladaTiposInstalaciones.setDescripcion(fila.getCell(2).getStringCellValue());
                                        capacidadInstaladaTiposInstalaciones.setInstalacion((short) ((int) fila.getCell(3).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Instalación en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            capacidadInstaladaCE.setInstalacion(capacidadInstaladaTiposInstalaciones);

                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        capacidadInstaladaTiposInstalaciones.setCapacidad((int) fila.getCell(4).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Capacidad en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case NUMERIC:
                                        capacidadInstaladaCE.setUnidades((int) fila.getCell(5).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Unidades en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoCapacidadInstaladaCE.setTotalEspaciosDocentes((int) (fila.getCell(6).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Espacios Docentes en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }

                            dtoCapacidadInstaladaCE.setCapacidadInstaladaCiclosEscolares(capacidadInstaladaCE);
                            dtoCapacidadInstaladaCiclosEscolares.add(dtoCapacidadInstaladaCE);
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
                        return dtoCapacidadInstaladaCiclosEscolares;
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
    public List<DTODistribucionAulasCPE> getListaDistribucionAulasCPE(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTODistribucionAulasCPE> dtoDistribucionAulasCPE = new ArrayList<>();
            DistribucionAulasCicloPeriodosEscolares distribucionAulaCPE;
            DTODistribucionAulasCPE dtoDistribucionAulaCPE;

            AulasTipo aulatipo;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Capacidad Instalada")) || (segundaHoja.getSheetName().equals("Distribución de Aulas")) || (tercerHoja.getSheetName().equals("Distribución de Lab. y Tall."))) {

//            Lectura de la segunda hoja
                    for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                        if (fila.getCell(1).getNumericCellValue() != 0) {
                            distribucionAulaCPE = new DistribucionAulasCicloPeriodosEscolares();
                            dtoDistribucionAulaCPE = new DTODistribucionAulasCPE();
                            aulatipo = new AulasTipo();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoDistribucionAulaCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                                        distribucionAulaCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
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
                                        dtoDistribucionAulaCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                                        distribucionAulaCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(5).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case FORMULA:
                                        aulatipo.setNombre(fila.getCell(4).getStringCellValue());
                                        aulatipo.setAulatipo((short) ((int) fila.getCell(5).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo de aula en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            distribucionAulaCPE.setAula(aulatipo);

                            if (fila.getCell(6).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case NUMERIC:
                                        distribucionAulaCPE.setNumero((int) fila.getCell(6).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Numero en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case FORMULA:
                                        aulatipo.setCapacidadTurno(fila.getCell(7).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Numero en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                        distribucionAulaCPE.setAcondicionadas((int) fila.getCell(8).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Aulas Acondicionadas en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            dtoDistribucionAulaCPE.setDistribucionAulasCicloPeriodosEscolares(distribucionAulaCPE);
                            dtoDistribucionAulasCPE.add(dtoDistribucionAulaCPE);
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
                        return dtoDistribucionAulasCPE;
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
    public List<DTODistribucionLabTallCPE> getListaDistribucionLabTall(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            //          Listas para muestra del usuario
            List<DTODistribucionLabTallCPE> dtoDistribucionLabsTallCPE = new ArrayList<>();
            AreasUniversidad areaUniversidad;
            DistribucionLabtallCicloPeriodosEscolares distribucionLabTallCPE;
            DTODistribucionLabTallCPE dtoDistribucionLabTallCPE;

            AulasTipo aulatipo;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Capacidad Instalada")) || (segundaHoja.getSheetName().equals("Distribución de Aulas")) || (tercerHoja.getSheetName().equals("Distribución de Lab. y Tall."))) {
//            Lectura de la tercer hoja
                    for (int i = 2; i <= tercerHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) tercerHoja.getRow(i);
                        if (fila.getCell(1).getNumericCellValue() != 0) {
                            distribucionLabTallCPE = new DistribucionLabtallCicloPeriodosEscolares();
                            aulatipo = new AulasTipo();
                            areaUniversidad = new AreasUniversidad();
                            dtoDistribucionLabTallCPE = new DTODistribucionLabTallCPE();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        dtoDistribucionLabTallCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                                        distribucionLabTallCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
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
                                        dtoDistribucionLabTallCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                                        distribucionLabTallCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(5).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case FORMULA:
                                        aulatipo.setNombre(fila.getCell(4).getStringCellValue());
                                        aulatipo.setAulatipo((short) ((int) fila.getCell(5).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo de aula en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            distribucionLabTallCPE.setAulaTipo(aulatipo);

                            if (fila.getCell(6).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case STRING:
                                        distribucionLabTallCPE.setNombre(fila.getCell(6).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nombre en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case STRING:
                                        distribucionLabTallCPE.setCapacidad(fila.getCell(7).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Capacidad en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        areaUniversidad.setArea((short) ((int) fila.getCell(9).getNumericCellValue()));
                                        areaUniversidad.setNombre(fila.getCell(8).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Área en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            distribucionLabTallCPE.setAreaResponsable(areaUniversidad.getArea());

                            if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(10).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(10))) {
                                            distribucionLabTallCPE.setFechaHabilitacion(fila.getCell(10).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de habilitación en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                            }

                            dtoDistribucionLabTallCPE.setAreaResponsable(areaUniversidad);
                            dtoDistribucionLabTallCPE.setDistribucionLabtallCicloPeriodosEscolares(distribucionLabTallCPE);

                            dtoDistribucionLabsTallCPE.add(dtoDistribucionLabTallCPE);
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
                        return dtoDistribucionLabsTallCPE;
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
    public void guardaCapacidadInstalada(List<DTOCapacidadInstaladaCiclosEscolares> listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionInstalaciones.forEach((capacidadInstalada) -> {
            if (ejbModulos.getPeriodoEscolarActivo().getCiclo().getCiclo() == capacidadInstalada.getCapacidadInstaladaCiclosEscolares().getCicloEscolar()) {
                facadeServGen.setEntityClass(CapacidadInstaladaCiclosEscolares.class);
                CapacidadInstaladaCiclosEscolares capacidadEncontrada = getCapacidadIntaladaCE(capacidadInstalada.getCapacidadInstaladaCiclosEscolares());
                Boolean registroAlmacenado = false;
                if (capacidadEncontrada != null) {
                    listaCondicional.add(capacidadInstalada.getCicloEscolar() + " " + capacidadInstalada.getTotalEspaciosDocentes());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if(ejbModulos.getPeriodoEscolarActivo().getCiclo().getCiclo() == capacidadEncontrada.getCicloEscolar()){
                        capacidadInstalada.getCapacidadInstaladaCiclosEscolares().setRegistro(capacidadEncontrada.getRegistro());
                        facadeServGen.edit(capacidadInstalada.getCapacidadInstaladaCiclosEscolares());
                    }else{
                        listaCondicional.remove(capacidadInstalada.getCicloEscolar() + " " + capacidadInstalada.getTotalEspaciosDocentes());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    capacidadInstalada.getCapacidadInstaladaCiclosEscolares().setRegistro(registro.getRegistro());
                    facadeServGen.create(capacidadInstalada.getCapacidadInstaladaCiclosEscolares());
                }
                facadeServGen.flush();
            }
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public void guardaDistribucionAulas(List<DTODistribucionAulasCPE> listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionInstalaciones.forEach((distribucionAula) -> {
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActivo(), distribucionAula.getDistribucionAulasCicloPeriodosEscolares().getPeriodoEscolar())) {
                facadeServGen.setEntityClass(DistribucionAulasCicloPeriodosEscolares.class);
                DistribucionAulasCicloPeriodosEscolares distribucionAulaEncontrada = getDistribucionAulasCPE(distribucionAula.getDistribucionAulasCicloPeriodosEscolares());
                Boolean registroAlmacenado = false;
                if (distribucionAulaEncontrada != null) {
                    listaCondicional.add(distribucionAula.getCicloEscolar() + " " + distribucionAula.getPeriodoEscolar());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if(ejbModulos.getPeriodoEscolarActivo().getPeriodo() == distribucionAulaEncontrada.getPeriodoEscolar()){
                        distribucionAula.getDistribucionAulasCicloPeriodosEscolares().setRegistro(distribucionAulaEncontrada.getRegistro());
                        facadeServGen.edit(distribucionAula.getDistribucionAulasCicloPeriodosEscolares());
                    }else{
                        listaCondicional.remove(distribucionAula.getCicloEscolar() + " " + distribucionAula.getPeriodoEscolar());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    distribucionAula.getDistribucionAulasCicloPeriodosEscolares().setRegistro(registro.getRegistro());
                    facadeServGen.create(distribucionAula.getDistribucionAulasCicloPeriodosEscolares());
                }
                facadeServGen.flush();
            }
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaDistribucionLabTall(List<DTODistribucionLabTallCPE> listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionInstalaciones.forEach((distribucionLabTall) -> {
            if (ejbModulos.validaPeriodoRegistro(ejbModulos.getPeriodoEscolarActivo(), distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares().getPeriodoEscolar())) {
                facadeServGen.setEntityClass(DistribucionLabtallCicloPeriodosEscolares.class);
                DistribucionLabtallCicloPeriodosEscolares distribucionLTCPEEncontrado = getDistribucionLabtallCicloPeriodosEscolares(distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares());
                Boolean registroAlmacenado = false;
                if (distribucionLTCPEEncontrado != null) {
                    listaCondicional.add(distribucionLabTall.getCicloEscolar() + " " + distribucionLabTall.getPeriodoEscolar());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if(ejbModulos.getPeriodoEscolarActivo().getPeriodo() == distribucionLTCPEEncontrado.getPeriodoEscolar()){
                        distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares().setRegistro(distribucionLTCPEEncontrado.getRegistro());
                        facadeServGen.edit(distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares());
                    }else{
                        listaCondicional.remove(distribucionLabTall.getCicloEscolar() + " " + distribucionLabTall.getPeriodoEscolar());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares().setRegistro(registro.getRegistro());
                    facadeServGen.create(distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares());
                }
                facadeServGen.flush();
            }
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public CapacidadInstaladaCiclosEscolares getCapacidadIntaladaCE(CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares) {
        TypedQuery<CapacidadInstaladaCiclosEscolares> query = facadeServGen.getEntityManager().createQuery("SELECT c FROM CapacidadInstaladaCiclosEscolares c JOIN c.instalacion i WHERE c.cicloEscolar = :cicloEscolar AND i.instalacion = :instalacion", CapacidadInstaladaCiclosEscolares.class);
        query.setParameter("cicloEscolar", capacidadInstaladaCiclosEscolares.getCicloEscolar());
        query.setParameter("instalacion", capacidadInstaladaCiclosEscolares.getInstalacion().getInstalacion());
        try {
            capacidadInstaladaCiclosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            capacidadInstaladaCiclosEscolares = null;
            ex.toString();
        }
        return capacidadInstaladaCiclosEscolares;
    }

    @Override
    public DistribucionAulasCicloPeriodosEscolares getDistribucionAulasCPE(DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares) {
        TypedQuery<DistribucionAulasCicloPeriodosEscolares> query = facadeServGen.getEntityManager().createQuery("SELECT d FROM DistribucionAulasCicloPeriodosEscolares d JOIN d.aula a WHERE d.cicloEscolar = :cicloEscolar AND d.periodoEscolar = :periodoEscolar AND a.aulatipo = :aula", DistribucionAulasCicloPeriodosEscolares.class);
        query.setParameter("cicloEscolar", distribucionAulasCicloPeriodosEscolares.getCicloEscolar());
        query.setParameter("periodoEscolar", distribucionAulasCicloPeriodosEscolares.getPeriodoEscolar());
        query.setParameter("aula", distribucionAulasCicloPeriodosEscolares.getAula().getAulatipo());
        try {
            distribucionAulasCicloPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            distribucionAulasCicloPeriodosEscolares = null;
            ex.toString();
        }
        return distribucionAulasCicloPeriodosEscolares;
    }

    @Override
    public DistribucionLabtallCicloPeriodosEscolares getDistribucionLabtallCicloPeriodosEscolares(DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares) {
        TypedQuery<DistribucionLabtallCicloPeriodosEscolares> query = facadeServGen.getEntityManager().createQuery("SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d JOIN d.aulaTipo a WHERE d.cicloEscolar = :cicloEscolar AND d.periodoEscolar = :periodoEscolar AND a.aulatipo = :aulaTipo AND d.nombre = :nombre", DistribucionLabtallCicloPeriodosEscolares.class);
        query.setParameter("cicloEscolar", distribucionLabtallCicloPeriodosEscolares.getCicloEscolar());
        query.setParameter("periodoEscolar", distribucionLabtallCicloPeriodosEscolares.getPeriodoEscolar());
        query.setParameter("aulaTipo", distribucionLabtallCicloPeriodosEscolares.getAulaTipo().getAulatipo());
        query.setParameter("nombre", distribucionLabtallCicloPeriodosEscolares.getNombre());
        try {
            distribucionLabtallCicloPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            distribucionLabtallCicloPeriodosEscolares = null;
            ex.toString();
        }
        return distribucionLabtallCicloPeriodosEscolares;
    }

    @Override
    public List<CapacidadInstaladaTiposInstalaciones> getCapacidadInstaladaTiposInstalacioneses() {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT c FROM CapacidadInstaladaTiposInstalaciones c ORDER BY c.descripcion", CapacidadInstaladaTiposInstalaciones.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<AulasTipo> getAulasTipos() {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT a FROM AulasTipo a ORDER BY a.nombre", AulasTipo.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipoCapInst, RegistrosTipo registrosTipoAulas, RegistrosTipo registrosTipoLabTall, EventosRegistros eventoRegistro, AreasUniversidad area) {
        List<Integer> clavesCiclosEscolares = facadeServGen.getEntityManager().createQuery("SELECT cice.cicloEscolar FROM CapacidadInstaladaCiclosEscolares cice INNER JOIN cice.registros r WHERE r.tipo.registroTipo=:tipo AND r.area = :area", Integer.class)
                .setParameter("tipo", registrosTipoCapInst.getRegistroTipo())
                .setParameter("area", area.getArea())
                .getResultList();
        List<Integer> claves = new ArrayList<>();
        
        List<Integer> claves1 = new ArrayList<>();
        if (!clavesCiclosEscolares.isEmpty()) {
            claves1 = facadeServGen.getEntityManager().createQuery("SELECT periodo.periodo FROM PeriodosEscolares periodo WHERE periodo.ciclo.ciclo IN :claves", Integer.class)
                    .setParameter("claves", clavesCiclosEscolares)
                    .getResultList();
            claves1.stream().forEach((t) -> {
                claves.add(t);
            });
        }
        
        List<Integer> claves2 = facadeServGen.getEntityManager().createQuery("SELECT dacpe.periodoEscolar FROM DistribucionAulasCicloPeriodosEscolares dacpe INNER JOIN dacpe.registros r WHERE r.tipo.registroTipo=:tipo AND r.area = :area",Integer.class)
                .setParameter("tipo", registrosTipoAulas.getRegistroTipo())
                .setParameter("area", area.getArea())
                .getResultList();
        
        List<Integer> claves3 = facadeServGen.getEntityManager().createQuery("SELECT dltcpe.periodoEscolar FROM DistribucionLabtallCicloPeriodosEscolares dltcpe INNER JOIN dltcpe.registros r WHERE r.tipo.registroTipo=:tipo AND r.area = :area",Integer.class)
                .setParameter("tipo", registrosTipoAulas.getRegistroTipo())
                .setParameter("area", area.getArea())
                .getResultList();
     
        claves2.stream().forEach((t) -> {
            claves.add(t);
        });
        
        claves3.stream().forEach((t) -> {
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
    public List<DTOCapacidadInstaladaCiclosEscolares> getListaCapacidadInstaladaPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        AreasUniversidad area = facadeServGen.getEntityManager().find(AreasUniversidad.class, claveArea);

        List<DTOCapacidadInstaladaCiclosEscolares> l = new ArrayList<>();
        List<CapacidadInstaladaCiclosEscolares> entities = facadeServGen.getEntityManager().createQuery("SELECT cice FROM CapacidadInstaladaCiclosEscolares cice INNER JOIN cice.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE r.area = :area AND cice.cicloEscolar = :ciclo AND t.registroTipo=:tipo", CapacidadInstaladaCiclosEscolares.class)
                .setParameter("area", area.getArea())
                .setParameter("ciclo", periodo.getCiclo().getCiclo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        entities.forEach(e -> {
            facadeServGen.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
            if (a != null) {
                l.add(new DTOCapacidadInstaladaCiclosEscolares(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        (e.getInstalacion().getCapacidad()*e.getUnidades()),
                        a
                ));
            } else {
                l.add(new DTOCapacidadInstaladaCiclosEscolares(
                         e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        (e.getInstalacion().getCapacidad()*e.getUnidades())
                ));
            }
        });
        return l;
    }

    @Override
    public List<DTODistribucionAulasCPE> getListaDistribucionAulasPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
        if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        AreasUniversidad area = facadeServGen.getEntityManager().find(AreasUniversidad.class, claveArea);

        List<DTODistribucionAulasCPE> l = new ArrayList<>();
        List<DistribucionAulasCicloPeriodosEscolares> entities = facadeServGen.getEntityManager().createQuery("SELECT dacpe FROM DistribucionAulasCicloPeriodosEscolares dacpe INNER JOIN dacpe.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE r.area = :area AND dacpe.periodoEscolar = :periodo AND t.registroTipo=:tipo", DistribucionAulasCicloPeriodosEscolares.class)
                .setParameter("area", area.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        
        entities.forEach(e -> {
            facadeServGen.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
            if (a != null) {
                l.add(new DTODistribucionAulasCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        a
                ));
            } else {
                l.add(new DTODistribucionAulasCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar()))
                ));
            }
        });
        return l;
    }

    @Override
    public List<DTODistribucionLabTallCPE> getListaDistribucionLabTallPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo) {
         if(evento == null || claveArea == null || periodo == null){
            return null;
        }
        AreasUniversidad area = facadeServGen.getEntityManager().find(AreasUniversidad.class, claveArea);

        List<DTODistribucionLabTallCPE> l = new ArrayList<>();
        List<DistribucionLabtallCicloPeriodosEscolares> entities = facadeServGen.getEntityManager().createQuery("SELECT dltcpe FROM DistribucionLabtallCicloPeriodosEscolares dltcpe INNER JOIN dltcpe.registros r INNER JOIN r.tipo t INNER JOIN r.eventoRegistro er WHERE r.area = :area AND dltcpe.periodoEscolar = :periodo AND t.registroTipo=:tipo", DistribucionLabtallCicloPeriodosEscolares.class)
                .setParameter("area", area.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .setParameter("tipo", registrosTipo.getRegistroTipo())
                .getResultList();
        
        entities.forEach(e -> {
            facadeServGen.getEntityManager().refresh(e);
            ActividadesPoa a = e.getRegistros().getActividadesPoaList().isEmpty() ? null : e.getRegistros().getActividadesPoaList().get(0);
            if (a != null) {
                l.add(new DTODistribucionLabTallCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        facadeServGen.getEntityManager().find(AreasUniversidad.class, e.getAreaResponsable()),
                        a
                ));
            } else {
                l.add(new DTODistribucionLabTallCPE(
                        e,
                        caster.cicloEscolarToString(facadeServGen.getEntityManager().find(CiclosEscolares.class, e.getCicloEscolar())),
                        caster.periodoToString(facadeServGen.getEntityManager().find(PeriodosEscolares.class, e.getPeriodoEscolar())),
                        facadeServGen.getEntityManager().find(AreasUniversidad.class, e.getAreaResponsable())
                ));
            }
        });
        return l;
    }

    @Override
    public Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipoCapInst, RegistrosTipo registrosTipoAulas, RegistrosTipo registrosTipoLabTall, AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException {
        if(periodos==null || periodos.isEmpty()) periodos = getPeriodosConregistro(registrosTipoCapInst,registrosTipoAulas,registrosTipoLabTall,eventoActual,area);
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
