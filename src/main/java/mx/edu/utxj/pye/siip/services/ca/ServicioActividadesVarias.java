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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActividadesVarias;
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
@Stateful
public class ServicioActividadesVarias implements EjbActividadesVarias {

    @EJB    Facade f;
    @EJB    EjbModulos ejbModulos;
    @EJB    EjbFiscalizacion ejbFiscalizacion;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<ActividadesVariasRegistro> getListaActividadesVarias(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<ActividadesVariasRegistro> actividadesVarias = new ArrayList<>();
            ActividadesVariasRegistro actividadVaria;

            File excelActividadadesVarias = new File(rutaArchivo);
            XSSFWorkbook workBookActividadVaria = new XSSFWorkbook();
            workBookActividadVaria = (XSSFWorkbook) WorkbookFactory.create(excelActividadadesVarias);
            XSSFSheet primeraHoja = workBookActividadVaria.getSheetAt(0);
            XSSFRow fila;

            try {
                if (primeraHoja.getSheetName().equals("Actividades Varias")) {
                    for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                        if ((fila.getCell(0).getDateCellValue() != null)) {
                            actividadVaria = new ActividadesVariasRegistro();

                            if (fila.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(0).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                            actividadVaria.setFechaInicio(fila.getCell(0).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha Inicial de la actividad en la columna: " + (0 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(1).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(1))) {
                                            actividadVaria.setFechaFin(fila.getCell(1).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha Final de la actividad en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(2).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case STRING:
                                        actividadVaria.setNombre(fila.getCell(2).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Nombre de la actividad en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(3).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case STRING:
                                        actividadVaria.setObjetivo(fila.getCell(3).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Objetivo de la actividad en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(4).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case STRING:
                                        actividadVaria.setLugar(fila.getCell(4).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Lugar de la actividad en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(5).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case STRING:
                                        actividadVaria.setImpactoBeneficio(fila.getCell(5).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Impacto beneficio de la actividad en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(6).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case NUMERIC:
                                        actividadVaria.setTotalMujeres((int) fila.getCell(6).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total Mujeres de la actividad en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                        actividadVaria.setTotalHombres((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total Hombres de la actividad en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(8).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case STRING:
                                        actividadVaria.setPersonalidades(fila.getCell(8).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Personalidades de la actividad en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            actividadesVarias.add(actividadVaria);
                        }
                    }
                    workBookActividadVaria.close();

                    if (validarCelda.contains(false)) {
                        Messages.addGlobalWarn("<b>El archivo cargado contiene datos que no son validos, verifique los datos de la plantilla</b>");
                        ServicioArchivos.escribeLog(datosInvalidos);
                        Messages.addGlobalWarn(datosInvalidos.toString());

                        excelActividadadesVarias.delete();
                        ServicioArchivos.eliminarArchivo(rutaArchivo);
                        return Collections.EMPTY_LIST;
                    } else {
                        Messages.addGlobalInfo("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
                        return actividadesVarias;
                    }

                } else {
                    workBookActividadVaria.close();
                    excelActividadadesVarias.delete();
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                    Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                    return Collections.EMPTY_LIST;
                }
            } catch (IOException e) {
                workBookActividadVaria.close();
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
    public void guardaActividadesVarias(List<ActividadesVariasRegistro> listaActividadesVarias, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaActividadesVarias.forEach((actividadesVarias) -> {
            f.setEntityClass(ActividadesVariasRegistro.class);
            ActividadesVariasRegistro avrEncontrado = buscaActividadVariaRegistroEspecifico(actividadesVarias);
            Boolean registroAlmacenado = false;
            if(avrEncontrado != null){
                listaCondicional.add(sdf.format(actividadesVarias.getFechaInicio()) + " " + actividadesVarias.getNombre());
                registroAlmacenado = true;
            }
            if(registroAlmacenado){
                if (ejbModulos.getEventoRegistro().equals(avrEncontrado.getRegistros().getEventoRegistro())) {
                    actividadesVarias.setRegistro(avrEncontrado.getRegistro());
                    f.edit(actividadesVarias);
                }else{
                    listaCondicional.remove(sdf.format(actividadesVarias.getFechaInicio()) + " " + actividadesVarias.getNombre());
                }
            }else{
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                actividadesVarias.setRegistro(registro.getRegistro());
                f.create(actividadesVarias);
            }
            f.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ActividadesVariasRegistro buscaActividadVariaRegistroEspecifico(ActividadesVariasRegistro avr) {
        try {
            ActividadesVariasRegistro actVar = em.createQuery("SELECT a FROM ActividadesVariasRegistro a WHERE a.fechaInicio = :fechaInicio AND a.nombre = :nombre", ActividadesVariasRegistro.class)
                    .setParameter("fechaInicio", avr.getFechaInicio())
                    .setParameter("nombre", avr.getNombre())
                    .getSingleResult();
            actVar.setRegistros(ejbModulos.buscaRegistroPorClave(actVar.getRegistro()));
            return actVar;
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }
    
    @Override
    public List<ActividadesVariasRegistro> getFiltroActividadesVariasEjercicioMesArea(Short ejercicio, String mes, Short area) {
        try {
            List<ActividadesVariasRegistro> actividadesVarias = em.createQuery("SELECT a FROM ActividadesVariasRegistro a JOIN a.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ActividadesVariasRegistro.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            actividadesVarias.forEach((t) -> {
                em.refresh(t);
            });
            return actividadesVarias;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }
    
}
