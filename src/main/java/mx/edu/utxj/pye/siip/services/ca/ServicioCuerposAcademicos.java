/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadAreasEstudio;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadDisciplinas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadIntegrantes;
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

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public List<DTOCuerposAcademicosR> getListaCuerposAcademicos(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOCuerposAcademicosR> listaDtoCuerposAcademicosR = new ArrayList<>();
            CuerposAcademicosRegistro cuerpoAcademicoRegistro;
            CuerpacadDisciplinas cuerpacadDisciplina;
            CuerpacadAreasEstudio cuerpacadAreasEstudio;
            AreasUniversidad areasUniversidad;
            DTOCuerposAcademicosR dtoCuerpoAcademicoR;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
            XSSFRow fila;

            if ((primeraHoja.getSheetName().equals("Cuerpos_Académicos")) || (segundaHoja.getSheetName().equals("Miembros_Cuerpos_Académicos")) || (tercerHoja.getSheetName().equals("Lineas_Investigación"))) {
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if ((fila.getCell(0).getNumericCellValue() > 0)) {
                        cuerpoAcademicoRegistro = new CuerposAcademicosRegistro();
                        cuerpacadDisciplina = new CuerpacadDisciplinas();
                        cuerpacadAreasEstudio = new CuerpacadAreasEstudio();
                        areasUniversidad = new AreasUniversidad();
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

//                    Área académica
                        if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(7).getCellTypeEnum()) {
                                case FORMULA:
                                    areasUniversidad.setNombre(fila.getCell(6).getStringCellValue());
                                    areasUniversidad.setArea((short) ((int) fila.getCell(7).getNumericCellValue()));
                                    cuerpoAcademicoRegistro.setArea(areasUniversidad.getArea());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Área Académica del cuerpo académico en la columna: " + (6 + 1) + " y fila: " + (i + 1));
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

                        dtoCuerpoAcademicoR.setArea(areasUniversidad);
                        dtoCuerpoAcademicoR.setCuerposAcademicosRegistro(cuerpoAcademicoRegistro);
                        listaDtoCuerposAcademicosR.add(dtoCuerpoAcademicoR);
                    }
                }
                libroRegistro.close();

                if (validarCelda.contains(false)) {
                    addDetailMessage("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                    addDetailMessage(datosInvalidos.toString());

                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    return Collections.EMPTY_LIST;
                } else {
                    addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoCuerposAcademicosR;
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            addDetailMessage("<b>Ocurrio un error en la lectura del archivo</b>");
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
                    addDetailMessage("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                    ServicioArchivos.escribeLog(datosInvalidos);
                    addDetailMessage(datosInvalidos.toString());

                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    return Collections.EMPTY_LIST;
                } else {
                    addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaDTOCuerpAcadIntegrantes;
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            addDetailMessage("<b>Ocurrio un error en la lectura del archivo</b>");
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
                    addDetailMessage("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                    ServicioArchivos.escribeLog(datosInvalidos);
                    addDetailMessage(datosInvalidos.toString());

                    excel.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    return Collections.EMPTY_LIST;
                } else {
                    addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                    return listaCuerpacadLineas;
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }
        } else {
            addDetailMessage("<b>Ocurrio un error en la lectura del archivo</b>");
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
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
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
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
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
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroCuerpoAcademicoEspecifico(String cuerpoAcademico) {
//        TypedQuery<CuerposAcademicosRegistro> query = em.createNamedQuery("CuerposAcademicosRegistro.findByCuerpoAcademico", CuerposAcademicosRegistro.class);
//        query.setParameter("cuerpoAcademico", cuerpoAcademico);
//        Integer registro = query.getSingleResult().getRegistro();
//        return registro;
        TypedQuery<CuerposAcademicosRegistro> query = em.createQuery("SELECT c FROM CuerposAcademicosRegistro c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerposAcademicosRegistro.class);
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
        TypedQuery<CuerposAcademicosRegistro> query = em.createNamedQuery("CuerposAcademicosRegistro.findByCuerpoAcademico", CuerposAcademicosRegistro.class);
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
        TypedQuery<CuerpacadIntegrantes> query = em.createQuery("SELECT c FROM CuerpacadIntegrantes c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.personal = :personal", CuerpacadIntegrantes.class);
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
        TypedQuery<CuerpacadLineas> query = em.createQuery("SELECT c FROM CuerpacadLineas c JOIN c.cuerpoAcademico ca WHERE ca.cuerpoAcademico = :cuerpoAcademico AND c.nombre = :nombre", CuerpacadLineas.class);
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
        TypedQuery<CuerpacadDisciplinas> query = em.createQuery("SELECT c FROM CuerpacadDisciplinas c ORDER BY c.nombre", CuerpacadDisciplinas.class);
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
        TypedQuery<CuerpacadAreasEstudio> query = em.createQuery("SELECT c FROM CuerpacadAreasEstudio c ORDER BY c.nombre" ,CuerpacadAreasEstudio.class);
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
        TypedQuery<CuerposAcademicosRegistro> query = em.createQuery("SELECT c FROM CuerposAcademicosRegistro c", CuerposAcademicosRegistro.class);
        
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
            cuerposAcademicosRegistros = em.createQuery("SELECT c FROM CuerposAcademicosRegistro c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area AND c.estatus = true", CuerposAcademicosRegistro.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
            cuerposAcademicosRegistros.forEach((c) -> {
                em.refresh(c);
                listaDtoCar.add(new DTOCuerposAcademicosR(
                        c,
                        em.find(AreasUniversidad.class, c.getArea())
                ));
            });
            return listaDtoCar;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<DTOCuerpAcadIntegrantes> getFiltroCuerpAcadIntegrantesEjercicioMesArea(Short ejercicio, Short area) throws Throwable {
        List<DTOCuerpAcadIntegrantes> listaDtoCai = new ArrayList<>();
        List<CuerpacadIntegrantes> cuerpacadIntegrantes = new ArrayList<>();
        try {
            cuerpacadIntegrantes = em.createQuery("SELECT c FROM CuerpacadIntegrantes c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area AND c.cuerpoAcademico.estatus = true", CuerpacadIntegrantes.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
            cuerpacadIntegrantes.forEach((c) -> {
                em.refresh(c);
                listaDtoCai.add(new DTOCuerpAcadIntegrantes(
                        c,
                        em.find(Personal.class, c.getPersonal())
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
            List<CuerpacadLineas> cuerpacadLineas = em.createQuery("SELECT c FROM CuerpacadLineas c JOIN c.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND r.area = :area AND c.cuerpoAcademico.estatus = true", CuerpacadLineas.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("area", area)
                    .getResultList();
            cuerpacadLineas.stream().forEach((c) -> {
                em.refresh(c);
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
            return registros = em.createQuery("SELECT c FROM CuerpacadIntegrantes c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerpacadIntegrantes.class)
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
            return registros = em.createQuery("SELECT c FROM CuerpacadLineas c WHERE c.cuerpoAcademico = :cuerpoAcademico", CuerpacadLineas.class)
                    .setParameter("cuerpoAcademico", cuerposAcademicosRegistro)
                    .getResultStream()
                    .map(c -> c.getRegistro())
                    .collect(Collectors.toList());
        }catch (NoResultException ex){
            return Collections.EMPTY_LIST;
        }
    }
    
}
