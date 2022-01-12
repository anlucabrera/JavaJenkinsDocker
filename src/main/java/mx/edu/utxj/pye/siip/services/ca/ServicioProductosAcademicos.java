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
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicosPersonal;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicos;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicosPersonal;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbProductosAcademicos;
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
public class ServicioProductosAcademicos implements EjbProductosAcademicos {

    @EJB    Facade facadeCapitalHumano;
    @EJB    EjbModulos ejbModulos;

    @Inject Caster  caster;    

    private static final Logger LOG = Logger.getLogger(ServicioProductosAcademicos.class.getName());
    
    @Override
    public List<DTOProductosAcademicos> getListaProductosAcademicos(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

//          Listas para muestra del usuario
            List<DTOProductosAcademicos> dtoProductosAcademicos = new ArrayList<>();
            ProductosAcademicos productoAcademico;
            Estado estado;
            Municipio municipio;
            MunicipioPK municipioPK;
            Pais pais;
            AreasUniversidad areaUniversidad;
            DTOProductosAcademicos dtoProductoAcademico;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFRow fila;
            
            try{
            if ((primeraHoja.getSheetName().equals("Productos Académicos")) || (segundaHoja.getSheetName().equals("Productos_Académicos_Personal"))) {
//            Lectura de la primera hoja
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if ((fila.getCell(1).getNumericCellValue() > 0)) {
                        productoAcademico = new ProductosAcademicos();
                        estado = new Estado();
                        municipio = new Municipio();
                        municipioPK = new MunicipioPK();
                        pais = new Pais();
                        areaUniversidad = new AreasUniversidad();
                        dtoProductoAcademico = new DTOProductosAcademicos();

//                    Clave Producto Académico
                        if (fila.getCell(2).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(2).getCellTypeEnum()) {
                                case FORMULA:
                                    productoAcademico.setProductoAcademico(fila.getCell(2).getStringCellValue());
                                    dtoProductoAcademico.setProdAcad(productoAcademico.getProductoAcademico());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Producto Académico en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                        }

//                    Area Academica
                        if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(4).getCellTypeEnum()) {
                                case FORMULA:
                                    areaUniversidad.setArea((short) (int) fila.getCell(4).getNumericCellValue());
                                    areaUniversidad.setNombre(fila.getCell(5).getStringCellValue());
                                    productoAcademico.setAreaAcademica(areaUniversidad.getArea());
                                    dtoProductoAcademico.setAreaUniversidad(areaUniversidad);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Área Académica en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                        }

//                    Tipo de Producto academico
                        if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(7).getCellTypeEnum()) {
                                case FORMULA:
                                    productoAcademico.setTipo(fila.getCell(7).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Tipo de producto académico en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                        }

//                    Nombre Producto Academico
                        if (fila.getCell(8).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(8).getCellTypeEnum()) {
                                case STRING:
                                    productoAcademico.setNombreProd(fila.getCell(8).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Nombre de producto académico en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                        }

//                    Evento o revista de presentacion
                        if (fila.getCell(9).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(9).getCellTypeEnum()) {
                                case STRING:
                                    productoAcademico.setEventrevPresentacion(fila.getCell(9).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Evento o revista de presentación en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                        }

//                    Fecha de inicio
                        if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(10).getCellTypeEnum()) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(fila.getCell(10))) {
                                        productoAcademico.setFechaInicio(fila.getCell(10).getDateCellValue());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Fecha de inicio en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                        }

//                    Fecha de fin
                        if (fila.getCell(11).getCellTypeEnum() == CellType.NUMERIC) {
                            switch (fila.getCell(11).getCellTypeEnum()) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(fila.getCell(11))) {
                                        productoAcademico.setFechaFin(fila.getCell(11).getDateCellValue());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Fecha de fin en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                        }

//                    Pais
                        if (fila.getCell(13).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(13).getCellTypeEnum()) {
                                case FORMULA:
                                    pais.setIdpais((int) fila.getCell(13).getNumericCellValue());
                                    pais.setNombre(fila.getCell(14).getStringCellValue());
                                    productoAcademico.setPais(pais);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: País en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                        }

//                    Estado
                        if (fila.getCell(18).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(18).getCellTypeEnum()) {
                                case FORMULA:
                                    estado.setIdestado((int) fila.getCell(18).getNumericCellValue());
                                    estado.setNombre(fila.getCell(19).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Estado en la columna: " + (19 + 1) + " y fila: " + (i + 1));
                        }

//                    Municipio
                        if (fila.getCell(25).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(25).getCellTypeEnum()) {
                                case FORMULA:
                                    municipioPK.setClaveEstado(estado.getIdestado());
                                    municipioPK.setClaveMunicipio((int) fila.getCell(25).getNumericCellValue());
                                    municipio.setMunicipioPK(municipioPK);
                                    municipio.setEstado(estado);
                                    municipio.setNombre(fila.getCell(26).getStringCellValue());
                                    productoAcademico.setMunicipio(municipio);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Municipio en la columna: " + (25 + 1) + " y fila: " + (i + 1));
                        }

//                    Lugar de realización
                        if (fila.getCell(27).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(27).getCellTypeEnum()) {
                                case STRING:
                                    productoAcademico.setLugar(fila.getCell(27).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Lugar de realización en la columna: " + (27 + 1) + " y fila: " + (i + 1));
                        }

//                    Descripción
                        if (fila.getCell(28).getCellTypeEnum() == CellType.STRING) {
                            switch (fila.getCell(28).getCellTypeEnum()) {
                                case STRING:
                                    productoAcademico.setDescripcion(fila.getCell(28).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Descripción en la columna: " + (28 + 1) + " y fila: " + (i + 1));
                        }

//                    ISSSN
                        if (fila.getCell(30).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(30).getCellTypeEnum()) {
                                case FORMULA:
                                        productoAcademico.setIssn(fila.getCell(30).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: ISSSN en la columna: " + (30 + 1) + " y fila: " + (i + 1));
                        }

//                    Arbitrado/Indexado
                        if (fila.getCell(32).getCellTypeEnum() == CellType.FORMULA) {
                            switch (fila.getCell(32).getCellTypeEnum()) {
                                case FORMULA:
                                    productoAcademico.setArbitradoIndexado(fila.getCell(32).getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            validarCelda.add(false);
                            datosInvalidos.add("Dato incorrecto: Arbitrado/Indexado en la columna: " + (32 + 1) + " y fila: " + (i + 1));
                        }

                        dtoProductoAcademico.setProductosAcademicos(productoAcademico);
                        dtoProductosAcademicos.add(dtoProductoAcademico);
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
                    return dtoProductosAcademicos;
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
    public List<DTOProductosAcademicosPersonal> getListaProductosAcademicosPersonal(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

//          Listas para muestra del usuario
            List<DTOProductosAcademicosPersonal> dtoProductosAcademicosPersonal = new ArrayList<>();
            ProductosAcademicosPersonal productoAcademicoPersonal;
            ProductosAcademicos productoAcademico;
            Personal personal;
            DTOProductosAcademicosPersonal dtoProductoAcademicoPersonal;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Productos Académicos")) || (segundaHoja.getSheetName().equals("Productos_Académicos_Personal"))) {
//            Lectura de segunda hoja
                    for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                        if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                            productoAcademicoPersonal = new ProductosAcademicosPersonal();
                            productoAcademico = new ProductosAcademicos();
                            personal = new Personal();
                            dtoProductoAcademicoPersonal = new DTOProductosAcademicosPersonal();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        productoAcademico.setProductoAcademico(fila.getCell(1).getStringCellValue());
                                        dtoProductoAcademicoPersonal.setProdAcad(productoAcademico.getProductoAcademico());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Producto Académico en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(3).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case FORMULA:
                                        personal.setClave((int) fila.getCell(3).getNumericCellValue());
                                        personal.setNombre(fila.getCell(4).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Personal en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            productoAcademicoPersonal.setPersonal(personal.getClave());
                            productoAcademicoPersonal.setProductoAcademico(productoAcademico);
                            dtoProductoAcademicoPersonal.setPersonal(personal);
                            dtoProductoAcademicoPersonal.setProductoAcademicoPersonal(productoAcademicoPersonal);

                            dtoProductosAcademicosPersonal.add(dtoProductoAcademicoPersonal);
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
                        return dtoProductosAcademicosPersonal;
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
    public void guardaProductosAcademicos(List<DTOProductosAcademicos> listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        listaProductosAcademicos.forEach((productoAcademico) -> {
            facadeCapitalHumano.setEntityClass(ProductosAcademicos.class);
            ProductosAcademicos proAcadEcontrado = getProductoAcademico(productoAcademico.getProductosAcademicos());
            Boolean registroAlmacenado = false;
            if (proAcadEcontrado != null) {
                listaCondicional.add(productoAcademico.getProductosAcademicos().getProductoAcademico());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if(ejbModulos.getEventoRegistro().equals(proAcadEcontrado.getRegistros().getEventoRegistro())){
                    productoAcademico.getProductosAcademicos().setRegistro(proAcadEcontrado.getRegistro());
                    facadeCapitalHumano.edit(productoAcademico.getProductosAcademicos());
                }else{
                    listaCondicional.remove(productoAcademico.getProductosAcademicos().getProductoAcademico());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                productoAcademico.getProductosAcademicos().setRegistro(registro.getRegistro());
                facadeCapitalHumano.create(productoAcademico.getProductosAcademicos());
            }
            facadeCapitalHumano.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public void guardaProductosAcademicosPersonal(List<DTOProductosAcademicosPersonal> listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        listaProductosAcademicos.forEach((productoAcademicoPersonal) -> {
            facadeCapitalHumano.setEntityClass(ProductosAcademicosPersonal.class);
            ProductosAcademicosPersonal prodAcadPEncontrado = getProductoAcademicoPersonal(productoAcademicoPersonal.getProductoAcademicoPersonal());
            Boolean registroAlmacenado = false;
            if (prodAcadPEncontrado != null) {
                listaCondicional.add(productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico() + " " + productoAcademicoPersonal.getPersonal().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if (ejbModulos.getEventoRegistro().equals(prodAcadPEncontrado.getRegistros().getEventoRegistro())) {
                    productoAcademicoPersonal.getProductoAcademicoPersonal().setRegistro(prodAcadPEncontrado.getRegistro());
                    productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico().setRegistro(prodAcadPEncontrado.getProductoAcademico().getRegistro());
                    facadeCapitalHumano.edit(productoAcademicoPersonal.getProductoAcademicoPersonal());
                } else {
                    listaCondicional.remove(productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico() + " " + productoAcademicoPersonal.getPersonal().getNombre());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico().setRegistro(getRegistroProductoAcademicoEspecifico(productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico().getProductoAcademico()));
                productoAcademicoPersonal.getProductoAcademicoPersonal().setRegistro(registro.getRegistro());
                facadeCapitalHumano.create(productoAcademicoPersonal.getProductoAcademicoPersonal());
            }
            facadeCapitalHumano.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroProductoAcademicoEspecifico(String productoAcademico) {
        TypedQuery<ProductosAcademicos> query = facadeCapitalHumano.getEntityManager().createNamedQuery("ProductosAcademicos.findByProductoAcademico", ProductosAcademicos.class);
        query.setParameter("productoAcademico", productoAcademico);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public ProductosAcademicos getProductoAcademico(ProductosAcademicos productoAcademico) {
        TypedQuery<ProductosAcademicos> query = facadeCapitalHumano.getEntityManager().createNamedQuery("ProductosAcademicos.findByProductoAcademico", ProductosAcademicos.class);
        query.setParameter("productoAcademico", productoAcademico.getProductoAcademico());
        try {
            productoAcademico = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            productoAcademico = null;
            ex.toString();
        }
        return productoAcademico;
    }

    @Override
    public ProductosAcademicosPersonal getProductoAcademicoPersonal(ProductosAcademicosPersonal productoAcademicoPersonal) {
        TypedQuery<ProductosAcademicosPersonal> query = facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicosPersonal p JOIN p.productoAcademico pa WHERE pa.productoAcademico = :productoAcademico AND p.personal = :personal", ProductosAcademicosPersonal.class);
        query.setParameter("productoAcademico", productoAcademicoPersonal.getProductoAcademico().getProductoAcademico());
        query.setParameter("personal", productoAcademicoPersonal.getPersonal());
        try {
            productoAcademicoPersonal = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            productoAcademicoPersonal = null;
        }
        return productoAcademicoPersonal;
    }

    @Override
    public List<DTOProductosAcademicos> getFiltroProductosAcademicosEjercicioMesArea(Short ejercicio, String mes, Short area) throws Throwable {
        List<DTOProductosAcademicos> listaDtoPA = new ArrayList<>();
        List<ProductosAcademicos> productosAcademicos = new ArrayList<>();
        try {
            productosAcademicos = facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicos p JOIN p.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ProductosAcademicos.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            productosAcademicos.forEach((t) -> {
                facadeCapitalHumano.getEntityManager().refresh(t);
                listaDtoPA.add(new DTOProductosAcademicos(
                        t,
                        facadeCapitalHumano.getEntityManager().find(AreasUniversidad.class, t.getAreaAcademica())
                ));
            });
            return listaDtoPA;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<ProductosAcademicos> getFiltroProductosAcademicosEjercicioMesAreaInt(Short ejercicio, String mes, Short area) throws Throwable {
        try {
            return facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicos p JOIN p.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ProductosAcademicos.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTOProductosAcademicosPersonal> getFiltroProductosAcademicosPersonalEjercicioMesArea(Short ejercicio, String mes, Short area) throws Throwable {
        List<DTOProductosAcademicosPersonal> listaDtoPAP = new ArrayList<>();
        List<ProductosAcademicosPersonal> productosAcademicosPersonal = new ArrayList<>();
        try {
            productosAcademicosPersonal = facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicosPersonal p JOIN p.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ProductosAcademicosPersonal.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            productosAcademicosPersonal.forEach((t) -> {
                facadeCapitalHumano.getEntityManager().refresh(t);
                listaDtoPAP.add(new DTOProductosAcademicosPersonal(
                        t,
                        facadeCapitalHumano.getEntityManager().find(Personal.class, t.getPersonal())
                ));
            });
            return listaDtoPAP;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> buscaRegistrosPersonalProductosAcademicos(ProductosAcademicos productoAcademico) throws Throwable {
        List<Integer> registros = new ArrayList<>();
        try {
            return registros = facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicosPersonal p WHERE p.productoAcademico = :productoAcademico", ProductosAcademicosPersonal.class)
                    .setParameter("productoAcademico", productoAcademico)
                    .getResultStream()
                    .map(p -> p.getRegistro())
                    .collect(Collectors.toList());
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Boolean buscaProductoAcademicoExistente(ProductosAcademicos productoAcademico) {
        try {
            ProductosAcademicos prodAcad = new ProductosAcademicos();
            prodAcad = facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicos p WHERE p.productoAcademico = :productoAcademico AND p.registro <> :registro", ProductosAcademicos.class)
                    .setParameter("productoAcademico", productoAcademico.getProductoAcademico())
                    .setParameter("registro", productoAcademico.getRegistro())
                    .getSingleResult();
            if (prodAcad != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

    @Override
    public Boolean buscaProductoAcademicoPersonalExistente(ProductosAcademicosPersonal productoAcademicoPersonal) {
        try {
            ProductosAcademicosPersonal prodAcadPer = new ProductosAcademicosPersonal();
            prodAcadPer = facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicosPersonal p WHERE p.productoAcademico.productoAcademico = :productoAcademico AND p.personal = :personal AND p.registro <> :registro", ProductosAcademicosPersonal.class)
                    .setParameter("productoAcademico", productoAcademicoPersonal.getProductoAcademico().getProductoAcademico())
                    .setParameter("personal", productoAcademicoPersonal.getPersonal())
                    .setParameter("registro", productoAcademicoPersonal.getRegistro())
                    .getSingleResult();
            if (prodAcadPer != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException | NonUniqueResultException ex) {
            return false;
        }
    }

    @Override
    public ProductosAcademicos editaProductoAcademico(ProductosAcademicos productoAcademico) {
        try {
            facadeCapitalHumano.setEntityClass(ProductosAcademicos.class);
            facadeCapitalHumano.edit(productoAcademico);
            facadeCapitalHumano.flush();
            return productoAcademico;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + productoAcademico.getProductoAcademico(), e);
            return null;
        }
    }

    @Override
    public ProductosAcademicosPersonal editaProductoAcademicoPersonal(ProductosAcademicosPersonal productoAcademicoPersonal) {
        try {
            facadeCapitalHumano.setEntityClass(ProductosAcademicosPersonal.class);
            facadeCapitalHumano.edit(productoAcademicoPersonal);
            facadeCapitalHumano.flush();
            return productoAcademicoPersonal;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No se pudo actualizar el registro: " + productoAcademicoPersonal.getProductoAcademico().getProductoAcademico(), e);
            return null;
        }
    }

    @Override
    public Pais getPaisProductoAcademico(ProductosAcademicos productoAcademico) {
        try {
            if(productoAcademico == null)
                return null;
            return facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicos pa JOIN pa.pais p WHERE pa.registro = :registro",Pais.class)
                .setParameter("registro", productoAcademico.getRegistro())
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Municipio getMunicipioProductoAcademico(ProductosAcademicos productoAcademico) {
        try {
            if(productoAcademico == null)
                return null;
            return facadeCapitalHumano.getEntityManager().createQuery("SELECT m FROM ProductosAcademicos pa JOIN pa.municipio m WHERE pa.registro = :registro",Municipio.class)
                    .setParameter("registro", productoAcademico.getRegistro())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ProductosAcademicos> getReporteGeneralProductosAcademicosPorEjercicio(Short ejercicio) {
        try {
            return facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicos p INNER JOIN p.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY p.fechaInicio",ProductosAcademicos.class)
                    .setParameter("ejercicioFiscal", ejercicio)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<ProductosAcademicosPersonal> getReporteGeneralProductosAcademicosPersonalPorEjercicio(Short ejercicio) {
        try {
            return facadeCapitalHumano.getEntityManager().createQuery("SELECT p FROM ProductosAcademicosPersonal p INNER JOIN p.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY p.productoAcademico.fechaInicio",ProductosAcademicosPersonal.class)
                    .setParameter("ejercicioFiscal", ejercicio)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }
    
}
