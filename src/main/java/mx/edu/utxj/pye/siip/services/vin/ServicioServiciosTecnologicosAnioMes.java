/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbServiciosTecnologicosAnioMes;
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
public class ServicioServiciosTecnologicosAnioMes implements EjbServiciosTecnologicosAnioMes {

    @EJB
    Facade facadeVinculacion;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaServiciosTecnologicosAnioMes getListaServiciosTecnologicosAnioMes(String rutaArchivo) throws Throwable {
//          Lista General
        ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes = new ListaServiciosTecnologicosAnioMes();

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
        if ((primeraHoja.getSheetName().equals("Servicios Tecnológicos")) || (segundaHoja.getSheetName().equals("Participantes Serv. Tec."))) {
//            Lectura de la primera hoja
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                    servicioTecnologicoAnioMes = new ServiciosTecnologicosAnioMes();
                    servicioTipo = new ServiciosTipos();

//                    Clave Servicio Tecnológico
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologicoAnioMes.setServicio(fila.getCell(2).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    ServicioTipo
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTipo.setServtipo((short) ((int) fila.getCell(4).getNumericCellValue()));
                            servicioTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    servicioTecnologicoAnioMes.setServicioTipo(servicioTipo);
//                    Nombre Servicio Tecnológico
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case STRING:
                            servicioTecnologicoAnioMes.setNombre(fila.getCell(6).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Fecha de inicio
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(7))) {
                                servicioTecnologicoAnioMes.setFechaInicio(fila.getCell(7).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Fecha de termino
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(8))) {
                                servicioTecnologicoAnioMes.setFechaTermino(fila.getCell(8).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Duracion Horas
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case NUMERIC:
                            servicioTecnologicoAnioMes.setDuracion((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Monto Ingresado
                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologicoAnioMes.setMontoIngresado(BigDecimal.valueOf(fila.getCell(11).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                    Facilitador
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case STRING:
                            servicioTecnologicoAnioMes.setFacilitador(fila.getCell(12).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Servicio Demandado
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologicoAnioMes.setServicioDemandado(fila.getCell(14).getStringCellValue());
                            break;
                        default:
                            break;
                    }

                    serviciosTecnologicosAnioMes.add(servicioTecnologicoAnioMes);
                }
            }
            listaServiciosTecnologicosAnioMes.setServiciosTecnologicosAnioMes(serviciosTecnologicosAnioMes);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaServiciosTecnologicosAnioMes;
    }

    @Override
    public ListaServiciosTecnologicosAnioMes getListaServiciosTecnologicosParticipantes(String rutaArchivo) throws Throwable {
        //          Lista General
        ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes = new ListaServiciosTecnologicosAnioMes();

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

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologico.setServicio(fila.getCell(1).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case STRING:
                            servicioTecnologicoParticipante.setNombre(fila.getCell(2).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologicoParticipante.setSexo(fila.getCell(4).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case NUMERIC:
                            servicioTecnologicoParticipante.setEdad((short) ((int) fila.getCell(5).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            estado.setIdestado(((int) fila.getCell(7).getNumericCellValue()));
                            estado.setNombre(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
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
                    switch (fila.getCell(17).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologicoParticipante.setLenguaIndigena(fila.getCell(17).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(19).getCellTypeEnum()) {
                        case FORMULA:
                            servicioTecnologicoParticipante.setDiscapacidad(fila.getCell(19).getStringCellValue());
                            break;
                        default:
                            break;
                    }
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
                    servicioTecnologicoParticipante.setServicioTecnologico(servicioTecnologico);
                    servicioTecnologicoParticipante.setMunicipio(municipio);
                    dtoServicioTecnologicoParticipante.setServiciosTecnologicosParticipantes(servicioTecnologicoParticipante);

                    dTOServiciosTecnologicosParticipantes.add(dtoServicioTecnologicoParticipante);
                }
            }

            listaServiciosTecnologicosAnioMes.setDtoServiciosTecnologicosParticipantes(dTOServiciosTecnologicosParticipantes);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaServiciosTecnologicosAnioMes;
    }

    @Override
    public void guardaServiciosTecnologicosAnioMes(ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaServiciosTecnologicosAnioMes.getServiciosTecnologicosAnioMes().forEach((servicioTecnologico) -> {
            facadeVinculacion.setEntityClass(ServiciosTecnologicosAnioMes.class);
            ServiciosTecnologicosAnioMes servicioEncontrado = getServiciosTecnologicosAnioMes(servicioTecnologico);
            Boolean registroAlmacenado = false;
            if (servicioEncontrado != null) {
                listaCondicional.add(servicioTecnologico.getServicio());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                servicioTecnologico.setRegistro(servicioEncontrado.getRegistro());
                facadeVinculacion.edit(servicioTecnologico);
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                servicioTecnologico.setRegistro(registro.getRegistro());
                facadeVinculacion.create(servicioTecnologico);
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaServiciosTecnologicosParticipantes(ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaServiciosTecnologicosAnioMes.getDtoServiciosTecnologicosParticipantes().forEach((servicioTecnologicoParticipante) -> {
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
                servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().setRegistro(servTecParEncontrado.getRegistro());
                facadeVinculacion.edit(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes().setRegistro(registro.getRegistro());
                facadeVinculacion.create(servicioTecnologicoParticipante.getServiciosTecnologicosParticipantes());
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public Integer getRegistroServicioTecnologicoEspecifico(String servicio) {
        TypedQuery<ServiciosTecnologicosAnioMes> query = em.createNamedQuery("ServiciosTecnologicosAnioMes.findByServicio", ServiciosTecnologicosAnioMes.class);
        query.setParameter("servicio", servicio);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public ServiciosTecnologicosAnioMes getServiciosTecnologicosAnioMes(ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes) {
        TypedQuery<ServiciosTecnologicosAnioMes> query = em.createQuery("SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.servicio = :servicio", ServiciosTecnologicosAnioMes.class);
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
        TypedQuery<ServiciosTecnologicosParticipantes> query = em.createQuery("SELECT s FROM ServiciosTecnologicosParticipantes s JOIN s.servicioTecnologico st WHERE st.servicio = :servicioTecnologico AND s.nombre = :nombre", ServiciosTecnologicosParticipantes.class);
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

}
