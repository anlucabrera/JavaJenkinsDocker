/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadAreasEstudio;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadDisciplinas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpoAreasAcademicas;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerposAcademicosR;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
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
public class ServicioCuerposAcademicos implements EjbCuerposAcademicos {

    @EJB
    Facade facadeCapitalHumano;
    @EJB
    EjbModulos ejbModulos;

    private static final Logger LOG = Logger.getLogger(ServicioCuerposAcademicos.class.getName());
    
    @Override
    public List<DTOCuerposAcademicosR> getListaCuerposAcademicos(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOCuerposAcademicosR> listaDtoCuerposAcademicosR = new ArrayList<>();
            CuerposAcademicosRegistro cuerpoAcademicoRegistro;
            CuerpacadDisciplinas cuerpacadDisciplina;
            CuerpacadAreasEstudio cuerpacadAreasEstudio;
            DTOCuerposAcademicosR dtoCuerpoAcademicoR;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
                    for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if ((fila.getCell(0).getNumericCellValue() > 0)) {
                            cuerpoAcademicoRegistro = new CuerposAcademicosRegistro();
                            cuerpacadDisciplina = new CuerpacadDisciplinas();
                            cuerpacadAreasEstudio = new CuerpacadAreasEstudio();
                            dtoCuerpoAcademicoR = new DTOCuerposAcademicosR();

//                    Cuerpo Académico
                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        cuerpoAcademicoRegistro.setCuerpoAcademico(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cuerpo académico en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

//                    Fecha de inicio
                            if (fila.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(2))) {
                                            cuerpoAcademicoRegistro.setFechaInicio(fila.getCell(2).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha Inicio del cuerpo académico en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

//                    Fecha de termino
                            if (fila.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(3))) {
                                            cuerpoAcademicoRegistro.setFechaTermino(fila.getCell(3).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha Termino del cuerpo académico en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }

//                    Nombre cuerpo académico
                            if (fila.getCell(4).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case STRING:
                                        cuerpoAcademicoRegistro.setNombre(fila.getCell(4).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nombre del cuerpo académico en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

//                    Nivel de reconocimiento
                            if (fila.getCell(5).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case STRING:
                                        cuerpoAcademicoRegistro.setNivelProdep(fila.getCell(5).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nivel Prodep del cuerpo académico en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

//                    Área de estudio
                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        cuerpacadAreasEstudio.setNombre(fila.getCell(8).getStringCellValue());
                                        cuerpacadAreasEstudio.setAreaEstudio((short) ((int) fila.getCell(9).getNumericCellValue()));
                                        cuerpoAcademicoRegistro.setAreaEstudio(cuerpacadAreasEstudio);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Área de estudio del cuerpo académico en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

//                    Disciplina
                            if (fila.getCell(11).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(11).getCellTypeEnum()) {
                                    case FORMULA:
                                        cuerpacadDisciplina.setNombre(fila.getCell(10).getStringCellValue());
                                        cuerpacadDisciplina.setDisciplina((short) ((int) fila.getCell(11).getNumericCellValue()));
                                        cuerpoAcademicoRegistro.setDisciplina(cuerpacadDisciplina);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Disciplina del cuerpo académico en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                            }

                            dtoCuerpoAcademicoR.setCuerposAcademicosRegistro(cuerpoAcademicoRegistro);
                            listaDtoCuerposAcademicosR.add(dtoCuerpoAcademicoR);
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
                        return listaDtoCuerposAcademicosR;
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
    public List<DTOCuerpAcadIntegrantes> getListaCuerpAcadIntegrantes(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOCuerpAcadIntegrantes> listaDTOCuerpAcadIntegrantes = new ArrayList<>();
            CuerpacadIntegrantes cuerpoacadIntegrante;
            CuerposAcademicosRegistro cuerposAcademicosRegistro;
            Personal personal;
            DTOCuerpAcadIntegrantes dtoCuerpAcadIntegrante;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
                    for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                        if (fila.getCell(2).getNumericCellValue() > 0) {
                            cuerpoacadIntegrante = new CuerpacadIntegrantes();
                            cuerposAcademicosRegistro = new CuerposAcademicosRegistro();
                            personal = new Personal();
                            dtoCuerpAcadIntegrante = new DTOCuerpAcadIntegrantes();

//                    Cuerpo Académico
                            if (fila.getCell(0).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case STRING:
                                        cuerposAcademicosRegistro.setCuerpoAcademico(fila.getCell(0).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cuerpo académico en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }

//                    Personal
                            if (fila.getCell(1).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case STRING:
                                        personal.setNombre(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Personal en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

//                    Clave Personal
                            if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case FORMULA:
                                        personal.setClave((int) fila.getCell(2).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Clave personal en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            cuerpoacadIntegrante.setCuerpoAcademico(cuerposAcademicosRegistro);
                            cuerpoacadIntegrante.setPersonal(personal.getClave());
                            dtoCuerpAcadIntegrante.setPersonal(personal);
                            dtoCuerpAcadIntegrante.setCuerpoAcademicoIntegrantes(cuerpoacadIntegrante);

                            listaDTOCuerpAcadIntegrantes.add(dtoCuerpAcadIntegrante);
                        }
                    }
                    libroRegistro.close();
                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        ServicioArchivos.escribeLog(datosInvalidos);
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return listaDTOCuerpAcadIntegrantes;
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
    public List<CuerpacadLineas> getListaCuerpAcadLineas(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<CuerpacadLineas> listaCuerpacadLineas = new ArrayList<>();
            CuerpacadLineas cuerpacadLinea;
            CuerposAcademicosRegistro cuerpoAcademicoRegistro;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
                    for (int i = 2; i <= tercerHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) tercerHoja.getRow(i);
                        if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                            cuerpacadLinea = new CuerpacadLineas();
                            cuerpoAcademicoRegistro = new CuerposAcademicosRegistro();

//                    Cuerpo Académico
                            if (fila.getCell(0).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case STRING:
                                        cuerpoAcademicoRegistro.setCuerpoAcademico(fila.getCell(0).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Cuerpo académico en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }

//                    Líneas de investigación
                            if (fila.getCell(1).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case STRING:
                                        cuerpacadLinea.setNombre(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Linea de investigación en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }
                            cuerpacadLinea.setCuerpoAcademico(cuerpoAcademicoRegistro);

                            listaCuerpacadLineas.add(cuerpacadLinea);
                        }
                    }
                    libroRegistro.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        ServicioArchivos.escribeLog(datosInvalidos);
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excel.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return listaCuerpacadLineas;
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
    public void guardaCuerposAcademicos(List<DTOCuerposAcademicosR> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        if (!listaCuerposAcademicos.isEmpty()) {
            listaCuerposAcademicos.forEach((cuerpoAcademicos) -> {
                facadeCapitalHumano.setEntityClass(CuerposAcademicosRegistro.class);
                CuerposAcademicosRegistro cuerpAcadEncontrado = getCuerpoAcademico(cuerpoAcademicos.getCuerposAcademicosRegistro());
                Boolean registroAlmacenado = false;
                if (cuerpAcadEncontrado != null) {
                    facadeCapitalHumano.refresh(cuerpAcadEncontrado);
                    listaCondicional.add(cuerpoAcademicos.getCuerposAcademicosRegistro().getCuerpoAcademico());
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio() == cuerpAcadEncontrado.getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio() && cuerpAcadEncontrado.getEstatus() == true) {
                        cuerpoAcademicos.getCuerposAcademicosRegistro().setRegistro(cuerpAcadEncontrado.getRegistro());
                        cuerpoAcademicos.getCuerposAcademicosRegistro().setEstatus(true);
                        facadeCapitalHumano.edit(cuerpoAcademicos.getCuerposAcademicosRegistro());
                    } else {
                        listaCondicional.remove(cuerpoAcademicos.getCuerposAcademicosRegistro().getCuerpoAcademico());
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    cuerpoAcademicos.getCuerposAcademicosRegistro().setRegistro(registro.getRegistro());
                    cuerpoAcademicos.getCuerposAcademicosRegistro().setEstatus(true);
                    facadeCapitalHumano.create(cuerpoAcademicos.getCuerposAcademicosRegistro());
                }
                facadeCapitalHumano.flush();
            });
        }
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaCuerpAcadIntegrantes(List<DTOCuerpAcadIntegrantes> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        if (!listaCuerposAcademicos.isEmpty()) {
            listaCuerposAcademicos.forEach((cuerpacadIntegrante) -> {
                try {
                    facadeCapitalHumano.setEntityClass(CuerpacadIntegrantes.class);
                    CuerpacadIntegrantes cuerpacadIntEncontrado = getCuerpacadIntegrantes(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes());
                    Boolean registroAlmacenado = false;
                    if (cuerpacadIntEncontrado != null) {
                        facadeCapitalHumano.refresh(cuerpacadIntEncontrado);
                        listaCondicional.add(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().getCuerpoAcademico() + " " + cuerpacadIntegrante.getPersonal().getNombre());
                        registroAlmacenado = true;
                    }
                    if (registroAlmacenado) {
                        if (ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio() == cuerpacadIntEncontrado.getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio() && cuerpacadIntEncontrado.getEstatus() == true) {
                            cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().setRegistro(cuerpacadIntEncontrado.getRegistro());
                            cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().setEstatus(true);
                            cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().setRegistro(cuerpacadIntEncontrado.getCuerpoAcademico().getRegistro());
                            facadeCapitalHumano.edit(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes());
                        } else {
                            listaCondicional.remove(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().getCuerpoAcademico() + " " + cuerpacadIntegrante.getPersonal().getNombre());
                        }
                    } else {
                        Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                        cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().setRegistro(getRegistroCuerpoAcademicoEspecifico(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().getCuerpoAcademico().getCuerpoAcademico()));
                        cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().setRegistro(registro.getRegistro());
                        cuerpacadIntegrante.getCuerpoAcademicoIntegrantes().setEstatus(true);
                        facadeCapitalHumano.create(cuerpacadIntegrante.getCuerpoAcademicoIntegrantes());
                    }
                    facadeCapitalHumano.flush();
                } catch (Throwable ex) {
                    Logger.getLogger(ServicioCuerposAcademicos.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaCuerpAcadLineas(List<CuerpacadLineas> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        if (!listaCuerposAcademicos.isEmpty()) {
            listaCuerposAcademicos.forEach((cuerpoacadLinea) -> {
                try {
                    facadeCapitalHumano.setEntityClass(CuerpacadLineas.class);
                    CuerpacadLineas cuerpacadLEncontrado = getCuerpacadLineas(cuerpoacadLinea);
                    Boolean registroAlmacenado = false;
                    if (cuerpacadLEncontrado != null) {
                        facadeCapitalHumano.refresh(cuerpacadLEncontrado);
                        listaCondicional.add(cuerpoacadLinea.getCuerpoAcademico().getCuerpoAcademico() + " " + cuerpacadLEncontrado.getNombre());
                        registroAlmacenado = true;
                    }
                    if (registroAlmacenado) {
                        if (ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio() == cuerpacadLEncontrado.getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio() && cuerpacadLEncontrado.getEstatus() == true) {
                            cuerpoacadLinea.setRegistro(cuerpacadLEncontrado.getRegistro());
                            cuerpoacadLinea.getCuerpoAcademico().setRegistro(cuerpacadLEncontrado.getCuerpoAcademico().getRegistro());
                            cuerpoacadLinea.setEstatus(true);
                            facadeCapitalHumano.edit(cuerpoacadLinea);
                        } else {
                            listaCondicional.remove(cuerpoacadLinea.getCuerpoAcademico().getCuerpoAcademico() + " " + cuerpacadLEncontrado.getNombre());
                        }
                    } else {
                        Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                        cuerpoacadLinea.getCuerpoAcademico().setRegistro(getRegistroCuerpoAcademicoEspecifico(cuerpoacadLinea.getCuerpoAcademico().getCuerpoAcademico()));
                        cuerpoacadLinea.setRegistro(registro.getRegistro());
                        cuerpoacadLinea.setEstatus(true);
                        facadeCapitalHumano.create(cuerpoacadLinea);
                    }
                    facadeCapitalHumano.flush();
                } catch (Throwable ex) {
                    Logger.getLogger(ServicioCuerposAcademicos.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroCuerpoAcademicoEspecifico(String cuerpoAcademico) {
//        TypedQuery<CuerposAcademicosRegistro> query = em.createNamedQuery("CuerposAcademicosRegistro.findByCuerpoAcademico", CuerposAcademicosRegistro.class);
//        query.setParameter("cuerpoAcademico", cuerpoAcademico);
//        Integer registro = query.getSingleResult().getRegistro();
//        return registro;
        TypedQuery<CuerposAcademicosRegistro> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerposAcademicosRegistro c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerposAcademicosRegistro.class);
        query.setParameter("cuerpoAcademico", cuerpoAcademico);
        Integer registro = 0;
        try {
            registro = query.getSingleResult().getRegistro();
        } catch (NoResultException | NonUniqueResultException ex) {
            registro = null;
            ex.toString();
        }

        return registro;
    }

    @Override
    public CuerposAcademicosRegistro getCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico) {
        TypedQuery<CuerposAcademicosRegistro> query = facadeCapitalHumano.getEntityManager().createNamedQuery("CuerposAcademicosRegistro.findByCuerpoAcademico", CuerposAcademicosRegistro.class);
        query.setParameter("cuerpoAcademico", cuerpoAcademico.getCuerpoAcademico());
        try {
            cuerpoAcademico = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpoAcademico = null;
            ex.toString();
        }
        return cuerpoAcademico;
    }

    @Override
    public CuerpacadIntegrantes getCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante) {
        TypedQuery<CuerpacadIntegrantes> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadIntegrantes c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.personal = :personal", CuerpacadIntegrantes.class);
        query.setParameter("cuerpoAcademico", cuerpacadIntegrante.getCuerpoAcademico().getCuerpoAcademico());
        query.setParameter("personal", cuerpacadIntegrante.getPersonal());
        try {
            cuerpacadIntegrante = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadIntegrante = null;
            ex.toString();
        }
        return cuerpacadIntegrante;
    }

    @Override
    public CuerpacadLineas getCuerpacadLineas(CuerpacadLineas cuerpacadLinea) {
        TypedQuery<CuerpacadLineas> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadLineas c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.nombre = :nombre", CuerpacadLineas.class);
        query.setParameter("cuerpoAcademico", cuerpacadLinea.getCuerpoAcademico().getCuerpoAcademico());
        query.setParameter("nombre", cuerpacadLinea.getNombre());
        try {
            cuerpacadLinea = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadLinea = null;
            ex.toString();
        }
        return cuerpacadLinea;
    }

    @Override
    public List<CuerpacadDisciplinas> getCuerpacadDisciplinas() {
        List<CuerpacadDisciplinas> cuerpacadDisciplinas = new ArrayList<>();
        TypedQuery<CuerpacadDisciplinas> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadDisciplinas c ORDER BY c.nombre", CuerpacadDisciplinas.class);
        try {
            cuerpacadDisciplinas = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadDisciplinas = null;
        }
        return cuerpacadDisciplinas;
    }

    @Override
    public List<CuerpacadAreasEstudio> getCuerpacadAreasEstudio() {
        List<CuerpacadAreasEstudio> cuerpacadAreasEstudios = new ArrayList<>();
        TypedQuery<CuerpacadAreasEstudio> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadAreasEstudio c ORDER BY c.nombre", CuerpacadAreasEstudio.class);
        try {
            cuerpacadAreasEstudios = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            cuerpacadAreasEstudios = null;
        }
        return cuerpacadAreasEstudios;
    }

    @Override
    public List<CuerposAcademicosRegistro> getCuerposAcademicosAct() {
        List<CuerposAcademicosRegistro> genLst = new ArrayList<>();
        TypedQuery<CuerposAcademicosRegistro> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerposAcademicosRegistro c", CuerposAcademicosRegistro.class);

        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
        return genLst;
    }

    @Override
    public List<DTOCuerposAcademicosR> getFiltroCuerposAcademicosEjercicioMesArea(Short ejercicio, Short area) throws Throwable {
        List<DTOCuerposAcademicosR> listaDtoCar = new ArrayList<>();
        List<CuerposAcademicosRegistro> cuerposAcademicosRegistros = new ArrayList<>();
        try {
            cuerposAcademicosRegistros = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerposAcademicosRegistro c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area", CuerposAcademicosRegistro.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
            cuerposAcademicosRegistros.forEach((c) -> {
                facadeCapitalHumano.getEntityManager().refresh(c);
                listaDtoCar.add(new DTOCuerposAcademicosR(
                        c
                ));
            });
            return listaDtoCar;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
     @Override
    public List<CuerposAcademicosRegistro> getFiltroCuerposAcademicosEdicion(Short ejercicio, Short area) throws Throwable {
         try {
            return facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerposAcademicosRegistro c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area", CuerposAcademicosRegistro.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
         } catch (NoResultException e) {
             return Collections.EMPTY_LIST;
         }
    }
    

    @Override
    public List<DTOCuerpAcadIntegrantes> getFiltroCuerpAcadIntegrantesEjercicioMesArea(Short ejercicio, Short area) throws Throwable {
        List<DTOCuerpAcadIntegrantes> listaDtoCai = new ArrayList<>();
        List<CuerpacadIntegrantes> cuerpacadIntegrantes = new ArrayList<>();
        try {
            cuerpacadIntegrantes = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadIntegrantes c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area", CuerpacadIntegrantes.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
            cuerpacadIntegrantes.forEach((c) -> {
                facadeCapitalHumano.getEntityManager().refresh(c);
                listaDtoCai.add(new DTOCuerpAcadIntegrantes(
                        c,
                        facadeCapitalHumano.getEntityManager().find(Personal.class, c.getPersonal())
                ));
            });
            return listaDtoCai;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<CuerpacadLineas> getFiltroCuerpAcadLineasEjercicioMesArea(Short ejercicio, Short area) throws Throwable {
        try {
            List<CuerpacadLineas> cuerpacadLineas = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadLineas c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area", CuerpacadLineas.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
            cuerpacadLineas.stream().forEach((c) -> {
                facadeCapitalHumano.getEntityManager().refresh(c);
            });

            return cuerpacadLineas;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> buscaRegistrosCuerpAcadIntegrantesByCuerpAcad(CuerposAcademicosRegistro cuerposAcademicosRegistro) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            return registros = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadIntegrantes c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerpacadIntegrantes.class)
                    .setParameter("cuerpoAcademico", cuerposAcademicosRegistro)
                    .getResultStream()
                    .map(c -> c.getRegistro())
                    .collect(Collectors.toList());
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<Integer> buscaRegistrosCuerpAcadLineasByCuerpAcad(CuerposAcademicosRegistro cuerposAcademicosRegistro) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            return registros = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadLineas c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerpacadLineas.class)
                    .setParameter("cuerpoAcademico", cuerposAcademicosRegistro)
                    .getResultStream()
                    .map(c -> c.getRegistro())
                    .collect(Collectors.toList());
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTOCuerpoAreasAcademicas> getCuerpoAreasAcademicas() throws Throwable {
        try {
            List<DTOCuerpoAreasAcademicas> dtoCAA = new ArrayList<>();
            List<AreasUniversidad> areasUniversidad = facadeCapitalHumano.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria AND a.vigente = :vigente ORDER BY a.nombre ASC", AreasUniversidad.class)
                    .setParameter("categoria", 8)
                    .setParameter("vigente", "1")
                    .getResultList();
            areasUniversidad.stream().forEach((a) -> {
                dtoCAA.add(new DTOCuerpoAreasAcademicas(
                        a
                ));
            });
            return dtoCAA;
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Boolean verificaCuerpoAreaAcademica(String cuerpoAcademico, AreasUniversidad areaUniversidad) {
        try {
            CuerpoAreasAcademicas caa = facadeCapitalHumano.getEntityManager().createQuery("SELECT a FROM CuerpoAreasAcademicas a INNER JOIN a.cuerposAcademicosRegistro c WHERE c.cuerpoAcademico = :cuerpoAcademico AND a.cuerpoAreasAcademicasPK.areaAcademica = :areaAcademica",CuerpoAreasAcademicas.class)
                    .setParameter("cuerpoAcademico",cuerpoAcademico)
                    .setParameter("areaAcademica",areaUniversidad.getArea())
                    .getSingleResult();
            if(caa != null) return true;
            else return false;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean guardarCuerpoAreaAcademica(CuerpoAreasAcademicas cuerpoAreasAcademica) {
        try {
            if (verificaCuerpoAreaAcademica(cuerpoAreasAcademica.getCuerpoAreasAcademicasPK().getCuerpoAcademico(), facadeCapitalHumano.getEntityManager().find(AreasUniversidad.class, cuerpoAreasAcademica.getCuerpoAreasAcademicasPK().getAreaAcademica()))) {
                eliminarCuerpoAreaAcademica(cuerpoAreasAcademica);
            } else {
                facadeCapitalHumano.setEntityClass(CuerpoAreasAcademicas.class);
                facadeCapitalHumano.create(cuerpoAreasAcademica);
                facadeCapitalHumano.flush();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo asignar el área académica al cuerpo académico.", e);
            return false;
        }
    }

    @Override
    public Boolean eliminarCuerpoAreaAcademica(CuerpoAreasAcademicas cuerpoAreasAcademica) {
        try {
            if (verificaCuerpoAreaAcademica(cuerpoAreasAcademica.getCuerpoAreasAcademicasPK().getCuerpoAcademico(), facadeCapitalHumano.getEntityManager().find(AreasUniversidad.class, cuerpoAreasAcademica.getCuerpoAreasAcademicasPK().getAreaAcademica()))) {
                facadeCapitalHumano.setEntityClass(CuerpoAreasAcademicas.class);
                facadeCapitalHumano.remove(cuerpoAreasAcademica);
                facadeCapitalHumano.flush();
            }
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo asignar el área académica al cuerpo académico.", e);
            return false;
        }
    }

    @Override
    public Boolean bajaCuerpoAcademico(CuerposAcademicosRegistro cuerposAcademicosRegistro) {
        try {
            Integer comprueba = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerposAcademicosRegistro c SET c.estatus = false WHERE c.registro = :registro")
                    .setParameter("registro", cuerposAcademicosRegistro.getRegistro())
                    .executeUpdate();
            if(comprueba != 0){
                Integer participantes = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadIntegrantes c SET c.estatus = false WHERE c.cuerpoAcademico.cuerpoAcademico = :cuerpoAcademico")
                        .setParameter("cuerpoAcademico", cuerposAcademicosRegistro.getCuerpoAcademico())
                        .executeUpdate();
                Integer lineasInvestigacion = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadLineas c SET c.estatus = false WHERE c.cuerpoAcademico.cuerpoAcademico = :cuerpoAcademico")
                        .setParameter("cuerpoAcademico", cuerposAcademicosRegistro.getCuerpoAcademico())
                        .executeUpdate();
                Messages.addGlobalInfo("<b>Se ha dado de baja el siguiente Cuerpo Académico: </b> " + cuerposAcademicosRegistro.getCuerpoAcademico());
                return true;
            }else{
                Messages.addGlobalInfo("<b>No se ha podido dar de baja el siguiente Cuerpo Académico: </b> " + cuerposAcademicosRegistro.getCuerpoAcademico());
                return false;
            }
        } catch (Exception e) {
            Messages.addGlobalInfo("<b>Ha ocurrido un error durante la baja del siguiente Cuerpo Académico: </b> " + cuerposAcademicosRegistro.getCuerpoAcademico() + " Error: " + e);
            return false;
        }
    }

    @Override
    public Boolean altaCuerpoAcademico(CuerposAcademicosRegistro cuerposAcademicosRegistro) {
        try {
            Integer comprueba = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerposAcademicosRegistro c SET c.estatus = true WHERE c.registro = :registro")
                    .setParameter("registro", cuerposAcademicosRegistro.getRegistro())
                    .executeUpdate();
            if(comprueba != 0){
                Integer participantes = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadIntegrantes c SET c.estatus = true WHERE c.cuerpoAcademico.cuerpoAcademico = :cuerpoAcademico")
                        .setParameter("cuerpoAcademico", cuerposAcademicosRegistro.getCuerpoAcademico())
                        .executeUpdate();
                Integer lineasInvestigacion = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadLineas c SET c.estatus = true WHERE c.cuerpoAcademico.cuerpoAcademico = :cuerpoAcademico")
                        .setParameter("cuerpoAcademico", cuerposAcademicosRegistro.getCuerpoAcademico())
                        .executeUpdate();
                Messages.addGlobalInfo("<b>Se ha dado de alta el siguiente Cuerpo Académico: </b> " + cuerposAcademicosRegistro.getCuerpoAcademico());
                return true;
            }else{
                Messages.addGlobalInfo("<b>No se ha podido dar de alta el siguiente Cuerpo Académico: </b> " + cuerposAcademicosRegistro.getCuerpoAcademico());
                return false;
            }
        } catch (Exception e) {
            Messages.addGlobalInfo("<b>Ha ocurrido un error durante la activación del siguiente Cuerpo Académico: </b> " + cuerposAcademicosRegistro.getCuerpoAcademico());
            return false;
        }
    }

    @Override
    public Boolean bajaCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante) {
        try {
            Integer participantes = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadIntegrantes c SET c.estatus = false WHERE c.registro = :registro")
                        .setParameter("registro", cuerpacadIntegrante.getRegistro())
                        .executeUpdate();
            if(participantes != 0){
                Messages.addGlobalInfo("<b>Se ha dado de baja el siguiente Participante: </b> " + cuerpacadIntegrante.getPersonal());
                return true;
            }else{
                Messages.addGlobalInfo("<b>No se ha podido dar de baja el siguiente Participante: </b> " + cuerpacadIntegrante.getPersonal());
                return false;
            }
        } catch (Exception e) {
            Messages.addGlobalInfo("<b>Ha ocurrido un error durante la baja del siguiente Participante: </b> " + cuerpacadIntegrante.getPersonal());
            return false;
        }
    }

    @Override
    public Boolean altaCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante) {
        try {
            CuerposAcademicosRegistro cuerpoAcademicoRegistro = facadeCapitalHumano.getEntityManager().find(CuerposAcademicosRegistro.class, cuerpacadIntegrante.getCuerpoAcademico().getRegistro());

            if (cuerpoAcademicoRegistro.getEstatus() == true) {
                Integer participantes = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadIntegrantes c SET c.estatus = true WHERE c.registro = :registro")
                        .setParameter("registro", cuerpacadIntegrante.getRegistro())
                        .executeUpdate();
                if (participantes != 0) {
                    Messages.addGlobalInfo("<b>Se ha dado de alta el siguiente Participante: </b> " + cuerpacadIntegrante.getPersonal());
                    return true;
                } else {
                    Messages.addGlobalInfo("<b>No se ha podido dar de alta el siguiente Participante: </b> " + cuerpacadIntegrante.getPersonal());
                    return false;
                }
            } else {
                Messages.addGlobalInfo("<b>No se ha podido dar de alta el siguiente participante debido a que el cuerpo académico al que pertenece se encuentra dado de baja: </b> " + cuerpacadIntegrante.getPersonal());
                return false;
            }
        } catch (Exception e) {
            Messages.addGlobalInfo("<b>Ha ocurrido un error durante el alta del siguiente Participante: </b> " + cuerpacadIntegrante.getPersonal());
            return false;
        }
    }

    @Override
    public Boolean bajaCuerpacadLineas(CuerpacadLineas cuerpacadLineas) {
        try {
            Integer participantes = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadLineas c SET c.estatus = false WHERE c.registro = :registro")
                        .setParameter("registro", cuerpacadLineas.getRegistro())
                        .executeUpdate();
            if(participantes != 0){
                Messages.addGlobalInfo("<b>Se ha dado de baja la siguiente Línea de Investigación: </b> " + cuerpacadLineas.getNombre());
                return true;
            }else{
                Messages.addGlobalInfo("<b>No se ha podido dar de baja la siguiente Línea de Investigación: </b> " + cuerpacadLineas.getNombre());
                return false;
            }
        } catch (Exception e) {
            Messages.addGlobalInfo("<b>Ha ocurrido un error durante la baja de la siguiente Línea de Investigación: </b> " + cuerpacadLineas.getNombre());
            return false;
        }
    }

    @Override
    public Boolean altaCuerpacadLineas(CuerpacadLineas cuerpacadLineas) {
        try {
            CuerposAcademicosRegistro cuerpoAcademicoRegistro = facadeCapitalHumano.getEntityManager().find(CuerposAcademicosRegistro.class, cuerpacadLineas.getCuerpoAcademico().getRegistro());

            if (cuerpoAcademicoRegistro.getEstatus() == true) {
                Integer participantes = facadeCapitalHumano.getEntityManager().createQuery("UPDATE CuerpacadLineas c SET c.estatus = true WHERE c.registro = :registro")
                        .setParameter("registro", cuerpacadLineas.getRegistro())
                        .executeUpdate();
                if (participantes != 0) {
                    Messages.addGlobalInfo("<b>Se ha dado de alta el siguiente Participante: </b> " + cuerpacadLineas.getNombre());
                    return true;
                } else {
                    Messages.addGlobalInfo("<b>No se ha podido dar de alta el siguiente Participante: </b> " + cuerpacadLineas.getNombre());
                    return false;
                }
            } else {
                Messages.addGlobalInfo("<b>No se ha podido dar de alta la siguiente línea de investigación debido a que el cuerpo académico al que pertenece se encuentra dado de baja: </b> " + cuerpacadLineas.getNombre());
                return false;
            }
        } catch (Exception e) {
            Messages.addGlobalInfo("<b>Ha ocurrido un error durante la alta del siguiente Participante: </b> " + cuerpacadLineas.getNombre());
            return false;
        }
    }

    @Override
    public CuerposAcademicosRegistro editaCuerpoAcademicoRegistro(CuerposAcademicosRegistro cuerpoAcademicoRegistro) {
        try {
            facadeCapitalHumano.setEntityClass(CuerposAcademicosRegistro.class);
            facadeCapitalHumano.edit(cuerpoAcademicoRegistro);
            facadeCapitalHumano.flush();
            return cuerpoAcademicoRegistro;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + cuerpoAcademicoRegistro.getCuerpoAcademico(), e);
            return null;
        }
    }

    @Override
    public CuerpacadIntegrantes editaCuerpoAcademicoIntegrante(CuerpacadIntegrantes cuerpoAcademicoIntegrante) {
        try {
            facadeCapitalHumano.setEntityClass(CuerpacadIntegrantes.class);
            facadeCapitalHumano.edit(cuerpoAcademicoIntegrante);
            facadeCapitalHumano.flush();
            return cuerpoAcademicoIntegrante;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + cuerpoAcademicoIntegrante.getCuerpoAcademico(), e);
            return null;
        }
    }

    @Override
    public CuerpacadLineas editaCuerpoAcademicoLineaInvestigacion(CuerpacadLineas cuerpoAcademicoLinea) {
        try {
            facadeCapitalHumano.setEntityClass(CuerpacadLineas.class);
            facadeCapitalHumano.edit(cuerpoAcademicoLinea);
            facadeCapitalHumano.flush();
            return cuerpoAcademicoLinea;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + cuerpoAcademicoLinea.getNombre(), e);
            return null;
        }
    }

    @Override
    public Boolean buscaCuerpoAcademicoIntegranteExistente(CuerpacadIntegrantes cuerpacadIntegrante) {
        try {
            CuerpacadIntegrantes cai = new CuerpacadIntegrantes();
            cai = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadIntegrantes c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.personal = :personal AND c.registro <> :registro",CuerpacadIntegrantes.class)
                .setParameter("cuerpoAcademico", cuerpacadIntegrante.getCuerpoAcademico().getCuerpoAcademico())
                .setParameter("personal", cuerpacadIntegrante.getPersonal())
                .setParameter("registro", cuerpacadIntegrante.getRegistro())
                .getSingleResult();
            if(cai != null){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

    @Override
    public Boolean buscaCuerpoAcademicoLineaInvestigacionExistente(CuerpacadLineas cuerpacadLinea) {
        try {
            CuerpacadLineas cali = new CuerpacadLineas();
            cali = facadeCapitalHumano.getEntityManager().createQuery("SELECT c FROM CuerpacadLineas c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.nombre = :nombre AND c.registro <> :registro",CuerpacadLineas.class)
                .setParameter("cuerpoAcademico", cuerpacadLinea.getCuerpoAcademico().getCuerpoAcademico())
                .setParameter("nombre", cuerpacadLinea.getNombre())
                .setParameter("registro", cuerpacadLinea.getRegistro())
                .getSingleResult();
            if(cali != null){
                return true;
            }else{
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }
    
}
