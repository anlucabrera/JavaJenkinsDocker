/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTipos;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vin.DTOServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbServiciosTecnologicosAnioMes;
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
@MultipartConfig
public class ServicioServiciosTecnologicosAnioMes implements EjbServiciosTecnologicosAnioMes {

    @EJB    Facade facadeVinculacion;
    @EJB    EjbModulos ejbModulos;
    @EJB    EjbOrganismosVinculados ejbOrganismosVinculados;
    
    @Inject Caster caster;
    
    private static final Logger LOG = Logger.getLogger(ServicioOrganismosVinculados.class.getName());

    @Override
    public List<ServiciosTecnologicosAnioMes> getListaServiciosTecnologicosAnioMes(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
//          Listas para muestra del usuario
            List<ServiciosTecnologicosAnioMes> serviciosTecnologicosAnioMes = new ArrayList<>();
            ServiciosTecnologicosAnioMes servicioTecnologicoAnioMes;
            ServiciosTipos servicioTipo;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Servicios Tecnológicos")) || (segundaHoja.getSheetName().equals("Participantes Serv. Tec."))) {
//            Lectura de la primera hoja
                    for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                            servicioTecnologicoAnioMes = new ServiciosTecnologicosAnioMes();
                            servicioTipo = new ServiciosTipos();

//                    Clave Servicio Tecnológico
                            if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologicoAnioMes.setServicio(fila.getCell(2).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Servicio en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

//                    ServicioTipo
                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTipo.setServtipo((short) ((int) fila.getCell(4).getNumericCellValue()));
                                        servicioTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Tipo de servicio en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            servicioTecnologicoAnioMes.setServicioTipo(servicioTipo);
//                    Nombre Servicio Tecnológico
                            if (fila.getCell(6).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case STRING:
                                        servicioTecnologicoAnioMes.setNombre(fila.getCell(6).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nombre en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }

//                    Fecha de inicio
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                                            servicioTecnologicoAnioMes.setFechaInicio(fila.getCell(7).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de Inicio en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

//                    Fecha de termino
                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(8))) {
                                            servicioTecnologicoAnioMes.setFechaTermino(fila.getCell(8).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de termino en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

//                    Duracion Horas
                            if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case NUMERIC:
                                        servicioTecnologicoAnioMes.setDuracion((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Duración en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }

//                    Monto Ingresado
                            if (fila.getCell(11).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(11).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologicoAnioMes.setMontoIngresado(BigDecimal.valueOf(fila.getCell(11).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Monto Ingresado en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                            }

//                    Facilitador
                            if (fila.getCell(12).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(12).getCellTypeEnum()) {
                                    case STRING:
                                        servicioTecnologicoAnioMes.setFacilitador(fila.getCell(12).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Facilitador en la columna: " + (12 + 1) + " y fila: " + (i + 1));
                            }

//                    Servicio Demandado
                            if (fila.getCell(14).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(14).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologicoAnioMes.setServicioDemandado(fila.getCell(14).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Servicio Demandado en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                            }

                            serviciosTecnologicosAnioMes.add(servicioTecnologicoAnioMes);
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
                        return serviciosTecnologicosAnioMes;
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
    public List<DTOServiciosTecnologicosParticipantes> getListaServiciosTecnologicosParticipantes(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
//          Listas para muestra del usuario
            List<DTOServiciosTecnologicosParticipantes> dTOServiciosTecnologicosParticipantes = new ArrayList<>();
            ServiciosTecnologicosParticipantes servicioTecnologicoParticipante;
            ServiciosTecnologicosAnioMes servicioTecnologico;
            AreasUniversidad areaUniversidad;
            Estado estado;
            Municipio municipio;
            MunicipioPK municipioPK;
            OrganismosVinculados organismosVinculados;
            DTOServiciosTecnologicosParticipantes dtoServicioTecnologicoParticipante;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Servicios Tecnológicos")) || (segundaHoja.getSheetName().equals("Participantes Serv. Tec."))) {
//            Lectura de segunda hoja
                    for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                        if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                            servicioTecnologicoParticipante = new ServiciosTecnologicosParticipantes();
                            servicioTecnologico = new ServiciosTecnologicosAnioMes();
                            areaUniversidad = new AreasUniversidad();
                            estado = new Estado();
                            municipio = new Municipio();
                            municipioPK = new MunicipioPK();
                            organismosVinculados = new OrganismosVinculados();
                            dtoServicioTecnologicoParticipante = new DTOServiciosTecnologicosParticipantes();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologico.setServicio(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Servicio Tecnológico en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(2).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case STRING:
                                        servicioTecnologicoParticipante.setNombre(fila.getCell(2).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nombre en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologicoParticipante.setSexo(fila.getCell(4).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Sexo en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case NUMERIC:
                                        servicioTecnologicoParticipante.setEdad((short) ((int) fila.getCell(5).getNumericCellValue()));
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Edad en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case FORMULA:
                                        estado.setIdestado(((int) fila.getCell(7).getNumericCellValue()));
                                        estado.setNombre(fila.getCell(8).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Estado en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(14).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(14).getCellTypeEnum()) {
                                    case FORMULA:
                                        municipioPK.setClaveEstado(estado.getIdestado());
                                        municipioPK.setClaveMunicipio((int) fila.getCell(14).getNumericCellValue());
                                        municipio.setEstado(estado);
                                        municipio.setMunicipioPK(municipioPK);
                                        municipio.setNombre(fila.getCell(15).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Municipio en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(17).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(17).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologicoParticipante.setLenguaIndigena(fila.getCell(17).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Lengua Indígena en la columna: " + (17 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(19).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(19).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioTecnologicoParticipante.setDiscapacidad(fila.getCell(19).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Discapacidad en la columna: " + (19 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(21).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(21).getCellTypeEnum()) {
                                    case FORMULA:
                                        if (fila.getCell(21).getNumericCellValue() != 0) {
                                            organismosVinculados.setNombre(fila.getCell(20).getStringCellValue());
                                            organismosVinculados.setEmpresa((int) fila.getCell(21).getNumericCellValue());
                                            servicioTecnologicoParticipante.setEmpresa(organismosVinculados);
                                            dtoServicioTecnologicoParticipante.setOrganismoVinculadoString(organismosVinculados.getNombre());
                                        } else {
                                            servicioTecnologicoParticipante.setEmpresa(null);
                                            dtoServicioTecnologicoParticipante.setOrganismoVinculadoString("N/A");
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Empresa en la columna: " + (20 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(23).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(23).getCellTypeEnum()) {
                                    case FORMULA:
                                        if (fila.getCell(23).getNumericCellValue() != 0) {
                                            servicioTecnologicoParticipante.setGeneracion((short) ((int) fila.getCell(23).getNumericCellValue()));
                                            dtoServicioTecnologicoParticipante.setGeneracion(fila.getCell(24).getStringCellValue());
                                        } else {
                                            servicioTecnologicoParticipante.setGeneracion(null);
                                            dtoServicioTecnologicoParticipante.setGeneracion(fila.getCell(24).getStringCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (24 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(26).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(26).getCellTypeEnum()) {
                                    case FORMULA:
                                        if (fila.getCell(26).getNumericCellValue() != 0) {
                                            areaUniversidad.setArea((short) ((int) fila.getCell(26).getNumericCellValue()));
                                            areaUniversidad.setNombre(fila.getCell(27).getStringCellValue());
                                            servicioTecnologicoParticipante.setProgramaEducativo(areaUniversidad.getArea());
                                            dtoServicioTecnologicoParticipante.setProgramaEducativo(areaUniversidad);
                                        } else {
                                            servicioTecnologicoParticipante.setProgramaEducativo(null);
                                            areaUniversidad.setNombre(fila.getCell(27).getStringCellValue());
                                            dtoServicioTecnologicoParticipante.setProgramaEducativo(areaUniversidad);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (27 + 1) + " y fila: " + (i + 1));
                            }

                            servicioTecnologicoParticipante.setServicioTecnologico(servicioTecnologico);
                            servicioTecnologicoParticipante.setMunicipio(municipio);
                            dtoServicioTecnologicoParticipante.setServiciosTecnologicosParticipantes(servicioTecnologicoParticipante);

                            dTOServiciosTecnologicosParticipantes.add(dtoServicioTecnologicoParticipante);
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
                        return dTOServiciosTecnologicosParticipantes;
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
    public void guardaServiciosTecnologicosAnioMes(List<ServiciosTecnologicosAnioMes> listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaServiciosTecnologicosAnioMes.forEach((servicioTecnologico) -> {
            facadeVinculacion.setEntityClass(ServiciosTecnologicosAnioMes.class);
            ServiciosTecnologicosAnioMes servicioEncontrado = getServiciosTecnologicosAnioMes(servicioTecnologico);
            Boolean registroAlmacenado = false;
            if (servicioEncontrado != null) {
                listaCondicional.add(servicioTecnologico.getServicio());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if (ejbModulos.getEventoRegistro().equals(servicioEncontrado.getRegistros().getEventoRegistro())) {
                    servicioTecnologico.setRegistro(servicioEncontrado.getRegistro());
                    facadeVinculacion.edit(servicioTecnologico);
                }else{
                    listaCondicional.remove(servicioTecnologico.getServicio());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                servicioTecnologico.setRegistro(registro.getRegistro());
                facadeVinculacion.create(servicioTecnologico);
            }
            facadeVinculacion.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaServiciosTecnologicosParticipantes(List<DTOServiciosTecnologicosParticipantes> listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaServiciosTecnologicosAnioMes.forEach((servicioTecnologicoParticipante) -> {
            facadeVinculacion.setEntityClass(ServiciosTecnologicosParticipantes.class);
            ServiciosTecnologicosParticipantes servTecParEncontrado = getServiciosTecnologicosParticipantes(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes());
            Boolean registroAlmacenado = false;
            if (servTecParEncontrado != null) {
                listaCondicional.add(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getServicioTecnologico().getServicio() + " " + servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getNombre());
                registroAlmacenado = true;
            }
            servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getServicioTecnologico().setRegistro(getRegistroServicioTecnologicoEspecifico(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getServicioTecnologico().getServicio()));
            if (servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getEmpresa() != null) {
                servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getEmpresa().getEmpresa()));
            }
            if (registroAlmacenado) {
                if(ejbModulos.getEventoRegistro().equals(servTecParEncontrado.getRegistros().getEventoRegistro())){
                    servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().setRegistro(servTecParEncontrado.getRegistro());
                    facadeVinculacion.edit(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes());
                }else{
                    listaCondicional.remove(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getServicioTecnologico().getServicio() + " " + servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().getNombre());
                }
                
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().setRegistro(registro.getRegistro());
                facadeVinculacion.create(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes());
            }
            facadeVinculacion.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroServicioTecnologicoEspecifico(String servicio) {
        TypedQuery<ServiciosTecnologicosAnioMes> query = facadeVinculacion.getEntityManager().createNamedQuery("ServiciosTecnologicosAnioMes.findByServicio", ServiciosTecnologicosAnioMes.class);
        query.setParameter("servicio", servicio);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public ServiciosTecnologicosAnioMes getServiciosTecnologicosAnioMes(ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes) {
        TypedQuery<ServiciosTecnologicosAnioMes> query = facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.servicio = :servicio", ServiciosTecnologicosAnioMes.class);
        query.setParameter("servicio", serviciosTecnologicosAnioMes.getServicio());
        try {
            serviciosTecnologicosAnioMes = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            serviciosTecnologicosAnioMes = null;
            ex.toString();
        }
        return serviciosTecnologicosAnioMes;
    }

    @Override
    public ServiciosTecnologicosParticipantes getServiciosTecnologicosParticipantes(ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes) {
        TypedQuery<ServiciosTecnologicosParticipantes> query = facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosParticipantes s JOIN s.servicioTecnologico st WHERE st.servicio = :servicioTecnologico AND s.nombre = :nombre", ServiciosTecnologicosParticipantes.class);
        query.setParameter("servicioTecnologico", serviciosTecnologicosParticipantes.getServicioTecnologico().getServicio());
        query.setParameter("nombre", serviciosTecnologicosParticipantes.getNombre());
        try {
            serviciosTecnologicosParticipantes = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            serviciosTecnologicosParticipantes = null;
            ex.toString();
        }
        return serviciosTecnologicosParticipantes;
    }

    @Override
    public List<ServiciosTecnologicosAnioMes> getFiltroServiciosTecnologicosEjercicioMesArea(Short ejercicio, String mes, Short area) {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosAnioMes s JOIN s.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ServiciosTecnologicosAnioMes.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public List<DTOServiciosTecnologicosParticipantes> getFiltroServiciosTecnologicosPartEjercicioMesArea(Short ejercicio, String mes, Short area) {
        try {
            List<DTOServiciosTecnologicosParticipantes> dtoServTec = new ArrayList<>();
            List<ServiciosTecnologicosParticipantes> dtoServTecPart = new ArrayList<>();
            dtoServTecPart = facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosParticipantes s JOIN s.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ServiciosTecnologicosParticipantes.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            dtoServTecPart.forEach((t) -> {
                dtoServTec.add(new DTOServiciosTecnologicosParticipantes(
                        validaGeneracion(t.getGeneracion()),
                        validaAreaUniversidad(t.getProgramaEducativo()),
                        t  
                ));
            });
            return dtoServTec;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public String validaGeneracion(Short generacion) {
        String generacionString = "";
        if (generacion != null) {
            generacionString = caster.generacionToString(facadeVinculacion.getEntityManager().find(Generaciones.class, generacion));
        }
        return generacionString;
    }

    public AreasUniversidad validaAreaUniversidad(Short programaEducativo) {
        AreasUniversidad areaUniversidad = null;
        if (programaEducativo != null) {
            areaUniversidad = facadeVinculacion.getEntityManager().find(AreasUniversidad.class, programaEducativo);
        }
        return areaUniversidad;
    }

    @Override
    public List<Integer> buscaRegistroParticipantesServiciosTecnologicos(ServiciosTecnologicosAnioMes servicioTecnologico) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            return registros = facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosParticipantes s WHERE s.servicioTecnologico.servicio = :servicioTecnologico", ServiciosTecnologicosParticipantes.class)
                    .setParameter("servicioTecnologico", servicioTecnologico.getServicio())
                    .getResultStream()
                    .map(s -> s.getRegistro())
                    .collect(Collectors.toList());
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<ServiciosTipos> getListaServiciosTipo() throws Throwable {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTipos s ORDER BY s.descripcion", ServiciosTipos.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public ServiciosTecnologicosAnioMes editaServicioTecnologicoAnioMes(ServiciosTecnologicosAnioMes servicioTecnologicoAnioMes) {
        try {
            facadeVinculacion.setEntityClass(ServiciosTecnologicosAnioMes.class);
            facadeVinculacion.edit(servicioTecnologicoAnioMes);
            facadeVinculacion.flush();
            return servicioTecnologicoAnioMes;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + servicioTecnologicoAnioMes.getNombre(), e);
            return null;
        }
    }

    @Override
    public ServiciosTecnologicosParticipantes editaServicioTecnologicoParticipante(ServiciosTecnologicosParticipantes servicioTecnologicoParticipante) {
        try {
            facadeVinculacion.setEntityClass(ServiciosTecnologicosParticipantes.class);
            facadeVinculacion.edit(servicioTecnologicoParticipante);
            facadeVinculacion.flush();
            return servicioTecnologicoParticipante;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + servicioTecnologicoParticipante.getNombre(), e);
            return null;
        }
    }

    @Override
    public List<ServiciosTecnologicosAnioMes> getReporteGeneralServiciosTecnologicos(Short ejercicio) {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosAnioMes s INNER JOIN s.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY s.fechaInicio",ServiciosTecnologicosAnioMes.class)
                    .setParameter("ejercicioFiscal", ejercicio)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<ServiciosTecnologicosParticipantes> getReporteGeneralServiciosTecnologicoParticipantes(Short ejercicio) {
        try {
            return facadeVinculacion.getEntityManager().createQuery("SELECT s FROM ServiciosTecnologicosParticipantes s INNER JOIN s.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY s.servicioTecnologico.fechaInicio",ServiciosTecnologicosParticipantes.class)
                    .setParameter("ejercicioFiscal", ejercicio)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

}
