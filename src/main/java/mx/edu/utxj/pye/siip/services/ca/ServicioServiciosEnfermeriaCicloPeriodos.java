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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOServiciosEnfemeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
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
public class ServicioServiciosEnfermeriaCicloPeriodos implements EjbServiciosEnfermeriaCicloPeriodos {

    @EJB
    Facade facadeEscolar;

    @EJB
    EjbModulos ejbModulos;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaServiciosEnfermeriaCicloPeriodos getListaServiciosEnfermeriaCicloPeriodos(String rutaArchivo) throws Throwable {
        ListaServiciosEnfermeriaCicloPeriodos listaServiciosEnfermeriaCicloPeriodos = new ListaServiciosEnfermeriaCicloPeriodos();

        List<DTOServiciosEnfemeriaCicloPeriodos> listaDtoServiciosEnfemeriaCicloPeriodos = new ArrayList<>();
        ServiciosEnfermeriaCicloPeriodos servicioEnfermeriaCicloPeriodo;
        ServiciosEnfermeriaTipo servicioEnfermeriaTipo;
        DTOServiciosEnfemeriaCicloPeriodos dtoServicioEnfermeriaCicloPeriodo;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        if ((primeraHoja.getSheetName().equals("Servicios Enfermería"))) {
            for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    servicioEnfermeriaCicloPeriodo = new ServiciosEnfermeriaCicloPeriodos();
                    servicioEnfermeriaTipo = new ServiciosEnfermeriaTipo();
                    dtoServicioEnfermeriaCicloPeriodo = new DTOServiciosEnfemeriaCicloPeriodos();

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            servicioEnfermeriaCicloPeriodo.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            dtoServicioEnfermeriaCicloPeriodo.setCicloEscolar(fila.getCell(2).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            servicioEnfermeriaCicloPeriodo.setPeriodoEscolar((int) fila.getCell(4).getNumericCellValue());
                            dtoServicioEnfermeriaCicloPeriodo.setPeriodoEscolar(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            servicioEnfermeriaTipo.setServicio((short) ((int) fila.getCell(7).getNumericCellValue()));
                            servicioEnfermeriaTipo.setDescripcion(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case NUMERIC:
                            servicioEnfermeriaCicloPeriodo.setEstH((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            servicioEnfermeriaCicloPeriodo.setEstM((int) fila.getCell(10).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case NUMERIC:
                            servicioEnfermeriaCicloPeriodo.setPerH((int) fila.getCell(12).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(13).getCellTypeEnum()) {
                        case NUMERIC:
                            servicioEnfermeriaCicloPeriodo.setPerM((int) fila.getCell(13).getNumericCellValue());
                            break;
                        default:
                            break;
                    }

                    switch (fila.getCell(15).getCellTypeEnum()) {
                        case STRING:
                            servicioEnfermeriaCicloPeriodo.setMes(fila.getCell(15).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    servicioEnfermeriaCicloPeriodo.setServicio(servicioEnfermeriaTipo);
                    dtoServicioEnfermeriaCicloPeriodo.setServiciosEnfermeriaCicloPeriodos(servicioEnfermeriaCicloPeriodo);

                    listaDtoServiciosEnfemeriaCicloPeriodos.add(dtoServicioEnfermeriaCicloPeriodo);
                }
            }

            listaServiciosEnfermeriaCicloPeriodos.setServiciosEnfermeriaCicloPeriodos(listaDtoServiciosEnfemeriaCicloPeriodos);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaServiciosEnfermeriaCicloPeriodos;
    }

    @Override
    public void guardaServiciosEnfermeriaCicloPeriodos(ListaServiciosEnfermeriaCicloPeriodos listaServiciosEnfermeriaCicloPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaServiciosEnfermeriaCicloPeriodos.getServiciosEnfermeriaCicloPeriodos().forEach((servicioEnfermeria) -> {
            facadeEscolar.setEntityClass(ServiciosEnfermeriaCicloPeriodos.class);
            ServiciosEnfermeriaCicloPeriodos servicioEncontrado = getServicioEnfermeriaCicloPeriodo(servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos());
            Boolean registroAlmacenado = false;
            if (servicioEncontrado != null) {
                listaCondicional.add(servicioEnfermeria.getPeriodoEscolar() + " " + servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().getServicio().getDescripcion());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().setRegistro(servicioEncontrado.getRegistro());
                facadeEscolar.edit(servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().setRegistro(registro.getRegistro());
                facadeEscolar.create(servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos());
            }
            facadeEscolar.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ServiciosEnfermeriaCicloPeriodos getServicioEnfermeriaCicloPeriodo(ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos) {
        ServiciosEnfermeriaCicloPeriodos servicioEncontrado = new ServiciosEnfermeriaCicloPeriodos();
        TypedQuery<ServiciosEnfermeriaCicloPeriodos> query = em.createQuery("SELECT s FROM ServiciosEnfermeriaCicloPeriodos s JOIN s.servicio t WHERE s.cicloEscolar = :cicloEscolar AND s.periodoEscolar = :periodoEscolar AND t.servicio = :servicio AND s.mes = :mes", ServiciosEnfermeriaCicloPeriodos.class);
        query.setParameter("cicloEscolar", serviciosEnfermeriaCicloPeriodos.getCicloEscolar());
        query.setParameter("periodoEscolar", serviciosEnfermeriaCicloPeriodos.getPeriodoEscolar());
        query.setParameter("servicio", serviciosEnfermeriaCicloPeriodos.getServicio().getServicio());
        query.setParameter("mes", serviciosEnfermeriaCicloPeriodos.getMes());
        try {
            servicioEncontrado = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            servicioEncontrado = null;
            ex.toString();
        }
        return servicioEncontrado;
    }

}
