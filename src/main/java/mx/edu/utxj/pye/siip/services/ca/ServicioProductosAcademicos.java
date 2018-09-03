/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
import mx.edu.utxj.pye.siip.dto.caphum.DTOProductosAcademicos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOProductosAcademicosPersonal;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaProductosAcademicos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbProductosAcademicos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
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
@Stateful
public class ServicioProductosAcademicos implements EjbProductosAcademicos {

    @EJB
    Facade facadeCapitalHumano;
    @EJB
    EjbModulos ejbModulos;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaProductosAcademicos getListaProductosAcademicos(String rutaArchivo) throws Throwable {
//          Lista General
        ListaProductosAcademicos listaProductosAcademicos = new ListaProductosAcademicos();
//          Listas para almacenar en base de datos

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
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            productoAcademico.setProductoAcademico(fila.getCell(2).getStringCellValue());
                            dtoProductoAcademico.setProdAcad(productoAcademico.getProductoAcademico());
                            break;
                        default:
                            break;
                    }
//                    Area Academica
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
//                    Tipo de Producto academico
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            productoAcademico.setTipo(fila.getCell(7).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Nombre Producto Academico
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case STRING:
                            productoAcademico.setNombreProd(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Evento o revista de presentacion
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case STRING:
                            productoAcademico.setEventrevPresentacion(fila.getCell(9).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Fecha de inicio
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(10))) {
                                productoAcademico.setFechaInicio(fila.getCell(10).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Fecha de fin
                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(11))) {
                                productoAcademico.setFechaFin(fila.getCell(11).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Pais
                    switch (fila.getCell(13).getCellTypeEnum()) {
                        case FORMULA:
                            pais.setIdpais((int) fila.getCell(13).getNumericCellValue());
                            pais.setNombre(fila.getCell(14).getStringCellValue());
                            productoAcademico.setPais(pais);
                            break;
                        default:
                            break;
                    }
//                    Estado
                    switch (fila.getCell(18).getCellTypeEnum()) {
                        case FORMULA:
                            estado.setIdestado((int) fila.getCell(18).getNumericCellValue());
                            estado.setNombre(fila.getCell(19).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Municipio
                    switch (fila.getCell(25).getCellTypeEnum()) {
                        case FORMULA:
                            municipioPK.setClaveEstado(estado.getIdestado());
                            municipioPK.setClaveMunicipio((int) fila.getCell(25).getNumericCellValue());
                            municipio.setMunicipioPK(municipioPK);
                            municipio.setEstado(estado);
                            productoAcademico.setMunicipio(municipio);
                            break;
                        default:
                            break;
                    }
//                    Lugar de realización
                    switch (fila.getCell(27).getCellTypeEnum()) {
                        case STRING:
                            productoAcademico.setLugar(fila.getCell(27).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Descripción
                    switch (fila.getCell(28).getCellTypeEnum()) {
                        case STRING:
                            productoAcademico.setDescripcion(fila.getCell(28).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    ISSSN
                    switch (fila.getCell(29).getCellTypeEnum()) {
                        case STRING:
                            productoAcademico.setIssn(fila.getCell(29).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Arbitrado/Indexado
                    switch (fila.getCell(31).getCellTypeEnum()) {
                        case FORMULA:
                            productoAcademico.setArbitradoIndexado(fila.getCell(31).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    dtoProductoAcademico.setProductosAcademicos(productoAcademico);

                    dtoProductosAcademicos.add(dtoProductoAcademico);
                }
            }
            listaProductosAcademicos.setDtoProductosAcademicos(dtoProductosAcademicos);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaProductosAcademicos;
    }

    @Override
    public ListaProductosAcademicos getListaProductosAcademicosPersonal(String rutaArchivo) throws Throwable {
//          Lista General
        ListaProductosAcademicos listaProductosAcademicos = new ListaProductosAcademicos();
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
        if ((primeraHoja.getSheetName().equals("Productos Académicos")) || (segundaHoja.getSheetName().equals("Productos_Académicos_Personal"))) {
//            Lectura de segunda hoja
            for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                    productoAcademicoPersonal = new ProductosAcademicosPersonal();
                    productoAcademico = new ProductosAcademicos();
                    personal = new Personal();
                    dtoProductoAcademicoPersonal = new DTOProductosAcademicosPersonal();
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            productoAcademico.setProductoAcademico(fila.getCell(1).getStringCellValue());
                            dtoProductoAcademicoPersonal.setProdAcad(productoAcademico.getProductoAcademico());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            personal.setClave((int) fila.getCell(3).getNumericCellValue());
                            personal.setNombre(fila.getCell(4).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    productoAcademicoPersonal.setPersonal(personal.getClave());
                    productoAcademicoPersonal.setProductoAcademico(productoAcademico);
                    dtoProductoAcademicoPersonal.setPersonal(personal);
                    dtoProductoAcademicoPersonal.setProductoAcademicoPersonal(productoAcademicoPersonal);

                    dtoProductosAcademicosPersonal.add(dtoProductoAcademicoPersonal);
                }
            }
            listaProductosAcademicos.setDtoProductosAcademicosPersonal(dtoProductosAcademicosPersonal);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaProductosAcademicos;
    }

    @Override
    public void guardaProductosAcademicos(ListaProductosAcademicos listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        listaProductosAcademicos.getDtoProductosAcademicos().forEach((productoAcademico) -> {
            facadeCapitalHumano.setEntityClass(ProductosAcademicos.class);
            ProductosAcademicos proAcadEcontrado = getProductoAcademico(productoAcademico.getProductosAcademicos());
            Boolean registroAlmacenado = false;
            if (proAcadEcontrado != null) {
                listaCondicional.add(productoAcademico.getProductosAcademicos().getProductoAcademico());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                productoAcademico.getProductosAcademicos().setRegistro(proAcadEcontrado.getRegistro());
                facadeCapitalHumano.edit(productoAcademico.getProductosAcademicos());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                productoAcademico.getProductosAcademicos().setRegistro(registro.getRegistro());
                facadeCapitalHumano.create(productoAcademico.getProductosAcademicos());
            }
            facadeCapitalHumano.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaProductosAcademicosPersonal(ListaProductosAcademicos listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
        List<String> listaCondicional = new ArrayList<>();
        listaProductosAcademicos.getDtoProductosAcademicosPersonal().forEach((productoAcademicoPersonal) -> {
            facadeCapitalHumano.setEntityClass(ProductosAcademicosPersonal.class);
            ProductosAcademicosPersonal prodAcadPEncontrado = getProductoAcademicoPersonal(productoAcademicoPersonal.getProductoAcademicoPersonal());
            Boolean registroAlmacenado = false;
            if (prodAcadPEncontrado != null) {
                listaCondicional.add(productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico() + " " + productoAcademicoPersonal.getPersonal().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                productoAcademicoPersonal.getProductoAcademicoPersonal().setRegistro(prodAcadPEncontrado.getRegistro());
                productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico().setRegistro(prodAcadPEncontrado.getProductoAcademico().getRegistro());
                facadeCapitalHumano.edit(productoAcademicoPersonal.getProductoAcademicoPersonal());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico().setRegistro(getRegistroProductoAcademicoEspecifico(productoAcademicoPersonal.getProductoAcademicoPersonal().getProductoAcademico().getProductoAcademico()));
                productoAcademicoPersonal.getProductoAcademicoPersonal().setRegistro(registro.getRegistro());
                facadeCapitalHumano.create(productoAcademicoPersonal.getProductoAcademicoPersonal());
            }
            facadeCapitalHumano.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroProductoAcademicoEspecifico(String productoAcademico) {
        TypedQuery<ProductosAcademicos> query = em.createNamedQuery("ProductosAcademicos.findByProductoAcademico", ProductosAcademicos.class);
        query.setParameter("productoAcademico", productoAcademico);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public ProductosAcademicos getProductoAcademico(ProductosAcademicos productoAcademico) {
        TypedQuery<ProductosAcademicos> query = em.createNamedQuery("ProductosAcademicos.findByProductoAcademico", ProductosAcademicos.class);
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
        TypedQuery<ProductosAcademicosPersonal> query = em.createQuery("SELECT p FROM ProductosAcademicosPersonal p JOIN p.productoAcademico pa WHERE pa.productoAcademico = :productoAcademico AND p.personal = :personal", ProductosAcademicosPersonal.class);
        query.setParameter("productoAcademico", productoAcademicoPersonal.getProductoAcademico().getProductoAcademico());
        query.setParameter("personal", productoAcademicoPersonal.getPersonal());
        try {
            productoAcademicoPersonal = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            productoAcademicoPersonal = null;
        }
        return productoAcademicoPersonal;
    }

}
