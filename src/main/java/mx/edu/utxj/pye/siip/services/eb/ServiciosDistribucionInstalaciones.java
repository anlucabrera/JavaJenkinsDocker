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
import mx.edu.utxj.pye.sgi.entity.pye2.AulasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaTiposInstalaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionAulasCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.servgen.DTOCapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.siip.dto.servgen.DTODistribucionAulasCPE;
import mx.edu.utxj.pye.siip.dto.servgen.DTODistribucionLabTallCPE;
import mx.edu.utxj.pye.siip.entity.servgen.list.ListaDistribucionInstalaciones;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionInstalaciones;
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
public class ServiciosDistribucionInstalaciones implements EjbDistribucionInstalaciones {

    @EJB
    Facade facadeServGen;
    @EJB
    EjbModulos ejbModulos;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaDistribucionInstalaciones getListaDistribucionInstalaciones(String rutaArchivo) throws Throwable {
        ListaDistribucionInstalaciones listaDistribucionInstalaciones = new ListaDistribucionInstalaciones();
//          Listas para muestra del usuario
        List<DTOCapacidadInstaladaCiclosEscolares> dtoCapacidadInstaladaCiclosEscolares = new ArrayList<>();
        CapacidadInstaladaCiclosEscolares capacidadInstaladaCE;
        CapacidadInstaladaTiposInstalaciones capacidadInstaladaTiposInstalaciones;
        DTOCapacidadInstaladaCiclosEscolares dtoCapacidadInstaladaCE;

        List<DTODistribucionAulasCPE> dtoDistribucionAulasCPE = new ArrayList<>();
        DistribucionAulasCicloPeriodosEscolares distribucionAulaCPE;
        DTODistribucionAulasCPE dtoDistribucionAulaCPE;

        List<DTODistribucionLabTallCPE> dtoDistribucionLabsTallCPE = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        DistribucionLabtallCicloPeriodosEscolares distribucionLabTallCPE;
        DTODistribucionLabTallCPE dtoDistribucionLabTallCPE;

        AulasTipo aulatipo;

//        Utilización y apertura del archivo recibido
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
        XSSFRow fila;
        if ((primeraHoja.getSheetName().equals("Capacidad Instalada")) || (segundaHoja.getSheetName().equals("Distribución de Aulas")) || (tercerHoja.getSheetName().equals("Distribución de Lab. y Tall."))) {
//            Lectura de la primer hoja

            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    capacidadInstaladaCE = new CapacidadInstaladaCiclosEscolares();
                    dtoCapacidadInstaladaCE = new DTOCapacidadInstaladaCiclosEscolares();
                    capacidadInstaladaTiposInstalaciones = new CapacidadInstaladaTiposInstalaciones();

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            dtoCapacidadInstaladaCE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                            capacidadInstaladaCE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            capacidadInstaladaTiposInstalaciones.setDescripcion(fila.getCell(2).getStringCellValue());
                            capacidadInstaladaTiposInstalaciones.setInstalacion((short) ((int) fila.getCell(3).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }

                    capacidadInstaladaCE.setInstalacion(capacidadInstaladaTiposInstalaciones);

                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            capacidadInstaladaTiposInstalaciones.setCapacidad((int) fila.getCell(4).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case NUMERIC:
                            capacidadInstaladaCE.setUnidades((int) fila.getCell(5).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            dtoCapacidadInstaladaCE.setTotalEspaciosDocentes((int) (fila.getCell(6).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
                    dtoCapacidadInstaladaCE.setCapacidadInstaladaCiclosEscolares(capacidadInstaladaCE);
                    dtoCapacidadInstaladaCiclosEscolares.add(dtoCapacidadInstaladaCE);
                }
            }
//            Lectura de la segunda hoja
            for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    distribucionAulaCPE = new DistribucionAulasCicloPeriodosEscolares();
                    dtoDistribucionAulaCPE = new DTODistribucionAulasCPE();
                    aulatipo = new AulasTipo();
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            dtoDistribucionAulaCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                            distribucionAulaCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            dtoDistribucionAulaCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                            distribucionAulaCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case FORMULA:
                            aulatipo.setNombre(fila.getCell(4).getStringCellValue());
                            aulatipo.setAulatipo((short) ((int) fila.getCell(5).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
                    distribucionAulaCPE.setAula(aulatipo);

                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case NUMERIC:
                            distribucionAulaCPE.setNumero((int) fila.getCell(6).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            aulatipo.setCapacidadTurno(fila.getCell(7).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            distribucionAulaCPE.setAcondicionadas((int) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    dtoDistribucionAulaCPE.setDistribucionAulasCicloPeriodosEscolares(distribucionAulaCPE);
                    dtoDistribucionAulasCPE.add(dtoDistribucionAulaCPE);
                }
            }
//            Lectura de la tercer hoja
            for (int i = 2; i <= tercerHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) tercerHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    distribucionLabTallCPE = new DistribucionLabtallCicloPeriodosEscolares();
                    aulatipo = new AulasTipo();
                    areaUniversidad = new AreasUniversidad();
                    dtoDistribucionLabTallCPE = new DTODistribucionLabTallCPE();
                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            dtoDistribucionLabTallCPE.setCicloEscolar(fila.getCell(0).getStringCellValue());
                            distribucionLabTallCPE.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(3).getCellTypeEnum()) {
                        case FORMULA:
                            dtoDistribucionLabTallCPE.setPeriodoEscolar(fila.getCell(2).getStringCellValue());
                            distribucionLabTallCPE.setPeriodoEscolar((int) fila.getCell(3).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(5).getCellTypeEnum()) {
                        case FORMULA:
                            aulatipo.setNombre(fila.getCell(4).getStringCellValue());
                            aulatipo.setAulatipo((short) ((int) fila.getCell(5).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
                    distribucionLabTallCPE.setAulaTipo(aulatipo);

                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case STRING:
                            distribucionLabTallCPE.setNombre(fila.getCell(6).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case STRING:
                            distribucionLabTallCPE.setCapacidad(fila.getCell(7).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            areaUniversidad.setArea((short) ((int) fila.getCell(9).getNumericCellValue()));
                            areaUniversidad.setNombre(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    distribucionLabTallCPE.setAreaResponsable(areaUniversidad.getArea());
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(10))) {
                                distribucionLabTallCPE.setFechaHabilitacion(fila.getCell(10).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                    dtoDistribucionLabTallCPE.setAreaResponsable(areaUniversidad);
                    dtoDistribucionLabTallCPE.setDistribucionLabtallCicloPeriodosEscolares(distribucionLabTallCPE);

                    dtoDistribucionLabsTallCPE.add(dtoDistribucionLabTallCPE);
                }
            }

            listaDistribucionInstalaciones.setDtoCapacidadInstaladaCiclosEscolares(dtoCapacidadInstaladaCiclosEscolares);
            listaDistribucionInstalaciones.setDtoDistribucionAulasCPE(dtoDistribucionAulasCPE);
            listaDistribucionInstalaciones.setDtoDistribucionLabTallCPE(dtoDistribucionLabsTallCPE);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDistribucionInstalaciones;
    }

    @Override
    public void guardaCapacidadIntalada(ListaDistribucionInstalaciones listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionInstalaciones.getDtoCapacidadInstaladaCiclosEscolares().forEach((capacidadInstalada) -> {
            facadeServGen.setEntityClass(CapacidadInstaladaCiclosEscolares.class);
            CapacidadInstaladaCiclosEscolares capacidadEncontrada = getCapacidadIntaladaCE(capacidadInstalada.getCapacidadInstaladaCiclosEscolares());
            Boolean registroAlmacenado = false;
            if (capacidadEncontrada != null) {
                listaCondicional.add(capacidadInstalada.getCicloEscolar() + " " + capacidadInstalada.getTotalEspaciosDocentes());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                capacidadInstalada.getCapacidadInstaladaCiclosEscolares().setRegistro(capacidadEncontrada.getRegistro());
                facadeServGen.edit(capacidadInstalada.getCapacidadInstaladaCiclosEscolares());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                capacidadInstalada.getCapacidadInstaladaCiclosEscolares().setRegistro(registro.getRegistro());
                facadeServGen.create(capacidadInstalada.getCapacidadInstaladaCiclosEscolares());
            }
            facadeServGen.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaDistribucionAulas(ListaDistribucionInstalaciones listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionInstalaciones.getDtoDistribucionAulasCPE().forEach((distribucionAula) -> {
            facadeServGen.setEntityClass(DistribucionAulasCicloPeriodosEscolares.class);
            DistribucionAulasCicloPeriodosEscolares distribucionAulaEncontrada = getDistribucionAulasCPE(distribucionAula.getDistribucionAulasCicloPeriodosEscolares());
            Boolean registroAlmacenado = false;
            if (distribucionAulaEncontrada != null) {
                listaCondicional.add(distribucionAula.getCicloEscolar() + " " + distribucionAula.getPeriodoEscolar());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                distribucionAula.getDistribucionAulasCicloPeriodosEscolares().setRegistro(distribucionAulaEncontrada.getRegistro());
                facadeServGen.edit(distribucionAula.getDistribucionAulasCicloPeriodosEscolares());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                distribucionAula.getDistribucionAulasCicloPeriodosEscolares().setRegistro(registro.getRegistro());
                facadeServGen.create(distribucionAula.getDistribucionAulasCicloPeriodosEscolares());
            }
            facadeServGen.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public void guardaDistribucionLabTall(ListaDistribucionInstalaciones listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaDistribucionInstalaciones.getDtoDistribucionLabTallCPE().forEach((distribucionLabTall) -> {
            facadeServGen.setEntityClass(DistribucionLabtallCicloPeriodosEscolares.class);
            DistribucionLabtallCicloPeriodosEscolares distribucionLTCPEEncontrado = getDistribucionLabtallCicloPeriodosEscolares(distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares());
            Boolean registroAlmacenado = false;
            if (distribucionLTCPEEncontrado != null) {
                listaCondicional.add(distribucionLabTall.getCicloEscolar() + " " + distribucionLabTall.getPeriodoEscolar());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares().setRegistro(distribucionLTCPEEncontrado.getRegistro());
                facadeServGen.edit(distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares().setRegistro(registro.getRegistro());
                facadeServGen.create(distribucionLabTall.getDistribucionLabtallCicloPeriodosEscolares());
            }
            facadeServGen.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public CapacidadInstaladaCiclosEscolares getCapacidadIntaladaCE(CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares) {
        TypedQuery<CapacidadInstaladaCiclosEscolares> query = em.createQuery("SELECT c FROM CapacidadInstaladaCiclosEscolares c JOIN c.instalacion i WHERE c.cicloEscolar = :cicloEscolar AND i.instalacion = :instalacion", CapacidadInstaladaCiclosEscolares.class);
        query.setParameter("cicloEscolar", capacidadInstaladaCiclosEscolares.getCicloEscolar());
        query.setParameter("instalacion", capacidadInstaladaCiclosEscolares.getInstalacion().getInstalacion());
        try {
            capacidadInstaladaCiclosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            capacidadInstaladaCiclosEscolares = null;
            ex.toString();
        }
        return capacidadInstaladaCiclosEscolares;
    }

    @Override
    public DistribucionAulasCicloPeriodosEscolares getDistribucionAulasCPE(DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares) {
        TypedQuery<DistribucionAulasCicloPeriodosEscolares> query = em.createQuery("SELECT d FROM DistribucionAulasCicloPeriodosEscolares d JOIN d.aula a WHERE d.cicloEscolar = :cicloEscolar AND d.periodoEscolar = :periodoEscolar AND a.aulatipo = :aula", DistribucionAulasCicloPeriodosEscolares.class);
        query.setParameter("cicloEscolar", distribucionAulasCicloPeriodosEscolares.getCicloEscolar());
        query.setParameter("periodoEscolar", distribucionAulasCicloPeriodosEscolares.getPeriodoEscolar());
        query.setParameter("aula", distribucionAulasCicloPeriodosEscolares.getAula().getAulatipo());
        try {
            distribucionAulasCicloPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            distribucionAulasCicloPeriodosEscolares = null;
            ex.toString();
        }
        return distribucionAulasCicloPeriodosEscolares;
    }

    @Override
    public DistribucionLabtallCicloPeriodosEscolares getDistribucionLabtallCicloPeriodosEscolares(DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares) {
        TypedQuery<DistribucionLabtallCicloPeriodosEscolares> query = em.createQuery("SELECT d FROM DistribucionLabtallCicloPeriodosEscolares d JOIN d.aulaTipo a WHERE d.cicloEscolar = :cicloEscolar AND d.periodoEscolar = :periodoEscolar AND a.aulatipo = :aulaTipo AND d.nombre = :nombre", DistribucionLabtallCicloPeriodosEscolares.class);
        query.setParameter("cicloEscolar", distribucionLabtallCicloPeriodosEscolares.getCicloEscolar());
        query.setParameter("periodoEscolar", distribucionLabtallCicloPeriodosEscolares.getPeriodoEscolar());
        query.setParameter("aulaTipo", distribucionLabtallCicloPeriodosEscolares.getAulaTipo().getAulatipo());
        query.setParameter("nombre", distribucionLabtallCicloPeriodosEscolares.getNombre());
        try {
            distribucionLabtallCicloPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            distribucionLabtallCicloPeriodosEscolares = null;
            ex.toString();
        }
        return distribucionLabtallCicloPeriodosEscolares;
    }

}
