/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOEficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaEficienciaTerminalTitulacionRegistros;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEficienciaTerminalTitulacionRegistro;
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
public class ServicioEficienciaTerminalTitulacionRegistro implements EjbEficienciaTerminalTitulacionRegistro {

    @EJB
    Facade facadeEscolar;

    @EJB
    EjbModulos ejbModulos;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaEficienciaTerminalTitulacionRegistros getListaEficienciaTerminalTitulacionRegistros(String rutaArchivo) throws Throwable {
//        Lista General
        ListaEficienciaTerminalTitulacionRegistros listaEficienciaTerminalTitulacionRegistros = new ListaEficienciaTerminalTitulacionRegistros();

//        Listas para muestra del usuario
        List<DTOEficienciaTerminalTitulacionRegistro> dtoEficienciaTerminalTitulacionRegistros = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        DTOEficienciaTerminalTitulacionRegistro dtoEficienciaTerminalTitulacionRegistro;
        EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro;

//        Utilización y apertura del archivo recibido
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;

        if (primeraHoja.getSheetName().equals("Eficiencia_Terminal")) {
            for (int i = 4; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if ((fila.getCell(0).getDateCellValue() != null)) {
                    areaUniversidad = new AreasUniversidad();
                    dtoEficienciaTerminalTitulacionRegistro = new DTOEficienciaTerminalTitulacionRegistro();
                    eficienciaTerminalTitulacionRegistro = new EficienciaTerminalTitulacionRegistro();

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            if (DateUtil.isCellDateFormatted(fila.getCell(1))) {
                                eficienciaTerminalTitulacionRegistro.setFechaCorte(fila.getCell(1).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            areaUniversidad.setArea((short) ((int) fila.getCell(3).getNumericCellValue()));
                            areaUniversidad.setNombre(fila.getCell(4).getStringCellValue());
                            eficienciaTerminalTitulacionRegistro.setProgramaEducativo(areaUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            eficienciaTerminalTitulacionRegistro.setGeneracion((short) ((int) fila.getCell(6).getNumericCellValue()));
                            dtoEficienciaTerminalTitulacionRegistro.setGeneracion(fila.getCell(7).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            eficienciaTerminalTitulacionRegistro.setPeriodoInicio((int) fila.getCell(9).getNumericCellValue());
                            dtoEficienciaTerminalTitulacionRegistro.setPeriodoInicio(fila.getCell(10).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case FORMULA:
                            eficienciaTerminalTitulacionRegistro.setPeriodoFin((int) fila.getCell(12).getNumericCellValue());
                            dtoEficienciaTerminalTitulacionRegistro.setPeriodoFin(fila.getCell(13).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setAlumnosh((int) fila.getCell(14).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(15).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setAlumnosm((int) fila.getCell(15).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(17).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setEgrecorh((int) fila.getCell(17).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(18).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setEgrecorm((int) fila.getCell(18).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(20).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setRezagadosh((int) fila.getCell(20).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(21).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setRezagadosm((int) fila.getCell(21).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(23).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setSegcarrerah((int) fila.getCell(23).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(24).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setSegcarreram((int) fila.getCell(24).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(26).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setEgresadosh((int) fila.getCell(26).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(27).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setEgresadosm((int) fila.getCell(27).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(29).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setTituladosh((int) fila.getCell(29).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(30).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setTituladosm((int) fila.getCell(30).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(32).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setRegistradosh((int) fila.getCell(32).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(33).getCellTypeEnum()) {
                        case NUMERIC:
                            eficienciaTerminalTitulacionRegistro.setRegistradosm((int) fila.getCell(33).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    dtoEficienciaTerminalTitulacionRegistro.setAreasUniversidad(areaUniversidad);
                    dtoEficienciaTerminalTitulacionRegistro.setEficienciaTerminalTitulacionRegistro(eficienciaTerminalTitulacionRegistro);

                    dtoEficienciaTerminalTitulacionRegistros.add(dtoEficienciaTerminalTitulacionRegistro);
                }
            }
            libroRegistro.close();
            listaEficienciaTerminalTitulacionRegistros.setEficienciaTerminalTitulacionRegistros(dtoEficienciaTerminalTitulacionRegistros);
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaEficienciaTerminalTitulacionRegistros;
    }

    @Override
    public void guardaEficienciaTerminalTitulacionRegistros(ListaEficienciaTerminalTitulacionRegistros listaEficienciaTerminalTitulacionRegistro, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaEficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistros().forEach((eficienciaTerminalTitulacionRegistro) -> {
            facadeEscolar.setEntityClass(EficienciaTerminalTitulacionRegistro.class);
            EficienciaTerminalTitulacionRegistro eficienciaTerminalEncontrado = getEficienciaTerminalTitulacionRegistro(eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro());
            Boolean registroAlmacenado = false;
            if (eficienciaTerminalEncontrado != null) {
                listaCondicional.add(eficienciaTerminalTitulacionRegistro.getAreasUniversidad() + " " + eficienciaTerminalTitulacionRegistro.getGeneracion());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro().setRegistro(eficienciaTerminalEncontrado.getRegistro());
                facadeEscolar.edit(eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro().setRegistro(registro.getRegistro());
                facadeEscolar.create(eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro());
            }
            facadeEscolar.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public EficienciaTerminalTitulacionRegistro getEficienciaTerminalTitulacionRegistro(EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro) {
        EficienciaTerminalTitulacionRegistro eficienciaTerminalEncontrado = new EficienciaTerminalTitulacionRegistro();
        TypedQuery<EficienciaTerminalTitulacionRegistro> query = em.createQuery("SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.programaEducativo = :programaEducativo AND e.generacion = :generacion", EficienciaTerminalTitulacionRegistro.class);
        query.setParameter("programaEducativo", eficienciaTerminalTitulacionRegistro.getProgramaEducativo());
        query.setParameter("generacion", eficienciaTerminalTitulacionRegistro.getGeneracion());
        try {
            eficienciaTerminalEncontrado = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            eficienciaTerminalEncontrado = null;
            ex.toString();
        }
        return eficienciaTerminalEncontrado;
    }

}
