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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.servgen.DTOEquiposComputoCPE;
import mx.edu.utxj.pye.siip.dto.servgen.DTOEquiposComputoInternetCPE;
import mx.edu.utxj.pye.siip.entity.servgen.list.ListaDistribucionEquipamiento;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionEquipamiento;
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
public class ServicioDistribucionEquipamiento implements EjbDistribucionEquipamiento {

    @EJB
    Facade facadeServGen;
    @EJB
    EjbModulos ejbModulos;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaDistribucionEquipamiento getListaDistribucionEquipamiento(String rutaArchivo) throws Throwable {
        ListaDistribucionEquipamiento listaDistribucionEquipamiento = new ListaDistribucionEquipamiento();
//          Listas para muestra del usuario
        List<DTOEquiposComputoCPE> dtoEquiposComputoCPE = new ArrayList<>();
        EquiposComputoCicloPeriodoEscolar equipoComputoCPE;
        DTOEquiposComputoCPE dtoEquipoComputoCPE;

        List<DTOEquiposComputoInternetCPE> dtoEquiposComputoInternetCPE = new ArrayList<>();
        EquiposComputoInternetCicloPeriodoEscolar equipoComputoInternetCPE;
        DTOEquiposComputoInternetCPE dtoEquipoComputoInternetCPE;

//        Utilización y apertura del archivo recibido
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        if ((primeraHoja.getSheetName().equals("Distribución_Equipo_Computo")) || (segundaHoja.getSheetName().equals("Distribucion_Equipo_Comp_Int"))) {
//            Lectura de la primera hoja
            for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    equipoComputoCPE = new EquiposComputoCicloPeriodoEscolar();
                    dtoEquipoComputoCPE = new DTOEquiposComputoCPE();

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                            equipoComputoCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                            equipoComputoCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoCPE.setTotal((int) fila.getCell(4).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoCPE.setEscritorio((int) fila.getCell(5).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoCPE.setPortatiles((int) fila.getCell(6).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setDtcEsc((int) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setDtcPort((int) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setDaEsc((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setDaPort((int) fila.getCell(10).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setEstEsc((int) fila.getCell(11).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setEstPor((int) fila.getCell(12).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(13).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setAdmEsc((int) fila.getCell(13).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setAdmPort((int) fila.getCell(14).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(15).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setMmsEsc((int) fila.getCell(15).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(16).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoCPE.setMmsPort((int) fila.getCell(16).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    dtoEquipoComputoCPE.setEquiposComputoCicloPeriodoEscolar(equipoComputoCPE);
                    dtoEquiposComputoCPE.add(dtoEquipoComputoCPE);
                }
            }
//            Lectura de la segunda hoja
            for (int i = 3; i <= segundaHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    equipoComputoInternetCPE = new EquiposComputoInternetCicloPeriodoEscolar();
                    dtoEquipoComputoInternetCPE = new DTOEquiposComputoInternetCPE();

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoInternetCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                            equipoComputoInternetCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoInternetCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                            equipoComputoInternetCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoInternetCPE.setTotal((int) fila.getCell(4).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoInternetCPE.setEscritorio((int) fila.getCell(5).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            dtoEquipoComputoInternetCPE.setPortatiles((int) fila.getCell(6).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setDtcEsc((int) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setDtcPort((int) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setDaEsc((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setDaPort((int) fila.getCell(10).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setEstEsc((int) fila.getCell(11).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setEstPor((int) fila.getCell(12).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(13).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setAdmEsc((int) fila.getCell(13).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setAdmPort((int) fila.getCell(14).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(15).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setMmsEsc((int) fila.getCell(15).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(16).getCellTypeEnum()) {
                        case NUMERIC:
                            equipoComputoInternetCPE.setMmsPort((int) fila.getCell(16).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    dtoEquipoComputoInternetCPE.setEquiposComputoInternetCicloPeriodoEscolar(equipoComputoInternetCPE);
                    dtoEquiposComputoInternetCPE.add(dtoEquipoComputoInternetCPE);
                }
            }

            listaDistribucionEquipamiento.setDtoEquiposComputoCPE(dtoEquiposComputoCPE);
            listaDistribucionEquipamiento.setDtoEquiposComputoInternetCPE(dtoEquiposComputoInternetCPE);
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDistribucionEquipamiento;
    }

    @Override
    public void guardaEquipoComputoCPE(ListaDistribucionEquipamiento listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionEquipamiento.getDtoEquiposComputoCPE().forEach((equiposComputoCPE) -> {
            facadeServGen.setEntityClass(EquiposComputoCicloPeriodoEscolar.class);
            EquiposComputoCicloPeriodoEscolar equipoEncontrado = getEquiposComputCicloPeriodoEscolar(equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar());
            Boolean registroAlmacenado = false;
            if (equipoEncontrado != null) {
                listaCondicional.add(equiposComputoCPE.getCicloEscolar() + " " + equiposComputoCPE.getPeriodoEscolar());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar().setRegistro(equipoEncontrado.getRegistro());
                facadeServGen.edit(equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar().setRegistro(registro.getRegistro());
                facadeServGen.create(equiposComputoCPE.getEquiposComputoCicloPeriodoEscolar());
            }
            facadeServGen.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaEquipoComputoInternetCPE(ListaDistribucionEquipamiento listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionEquipamiento.getDtoEquiposComputoInternetCPE().forEach((equiposComputoInternetCPE) -> {
            facadeServGen.setEntityClass(EquiposComputoInternetCicloPeriodoEscolar.class);
            EquiposComputoInternetCicloPeriodoEscolar equipoInternetEncontrado = getEquiposComputoInternetCicloPeriodoEscolar(equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar());
            Boolean registroAlmacenado = false;
            if (equipoInternetEncontrado != null) {
                listaCondicional.add(equiposComputoInternetCPE.getCicloEscolar() + " " + equiposComputoInternetCPE.getPeriodoEscolar());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar().setRegistro(equipoInternetEncontrado.getRegistro());
                facadeServGen.edit(equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar().setRegistro(registro.getRegistro());
                facadeServGen.create(equiposComputoInternetCPE.getEquiposComputoInternetCicloPeriodoEscolar());
            }
            facadeServGen.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public EquiposComputoCicloPeriodoEscolar getEquiposComputCicloPeriodoEscolar(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar) {
        TypedQuery<EquiposComputoCicloPeriodoEscolar> query = em.createQuery("SELECT e FROM EquiposComputoCicloPeriodoEscolar e WHERE e.cicloEscolar = :cicloEscolar AND e.periodoEscolar = :periodoEscolar", EquiposComputoCicloPeriodoEscolar.class);
        query.setParameter("cicloEscolar", equiposComputoCicloPeriodoEscolar.getCicloEscolar());
        query.setParameter("periodoEscolar", equiposComputoCicloPeriodoEscolar.getPeriodoEscolar());
        try {
            equiposComputoCicloPeriodoEscolar = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            equiposComputoCicloPeriodoEscolar = null;
            ex.toString();
        }
        return equiposComputoCicloPeriodoEscolar;
    }

    @Override
    public EquiposComputoInternetCicloPeriodoEscolar getEquiposComputoInternetCicloPeriodoEscolar(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar) {
        TypedQuery<EquiposComputoInternetCicloPeriodoEscolar> query = em.createQuery("SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.cicloEscolar = :cicloEscolar AND e.periodoEscolar = :periodoEscolar", EquiposComputoInternetCicloPeriodoEscolar.class);
        query.setParameter("cicloEscolar", equiposComputoInternetCicloPeriodoEscolar.getCicloEscolar());
        query.setParameter("periodoEscolar", equiposComputoInternetCicloPeriodoEscolar.getPeriodoEscolar());
        try {
            equiposComputoInternetCicloPeriodoEscolar = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            equiposComputoInternetCicloPeriodoEscolar = null;
            ex.toString();
        }
        return equiposComputoInternetCicloPeriodoEscolar;
    }

}
