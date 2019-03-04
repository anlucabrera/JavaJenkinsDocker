/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.eb.DTOEficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEficienciaTerminalTitulacionRegistro;
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
public class ServicioEficienciaTerminalTitulacionRegistro implements EjbEficienciaTerminalTitulacionRegistro {

    @EJB    Facade facadeEscolar;
    @EJB    EjbModulos ejbModulos;

    @Inject Caster  caster;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public List<DTOEficienciaTerminalTitulacionRegistro> getListaEficienciaTerminalTitulacionRegistros(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

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

            try {
                if (primeraHoja.getSheetName().equals("Eficiencia_Terminal")) {
                    for (int i = 4; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if ((fila.getCell(0).getDateCellValue() != null)) {
                            areaUniversidad = new AreasUniversidad();
                            dtoEficienciaTerminalTitulacionRegistro = new DTOEficienciaTerminalTitulacionRegistro();
                            eficienciaTerminalTitulacionRegistro = new EficienciaTerminalTitulacionRegistro();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        if (DateUtil.isCellDateFormatted(fila.getCell(1))) {
                                            eficienciaTerminalTitulacionRegistro.setFechaCorte(fila.getCell(1).getDateCellValue());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Fecha de corte en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(3).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case FORMULA:
                                        areaUniversidad.setArea((short) ((int) fila.getCell(3).getNumericCellValue()));
                                        areaUniversidad.setNombre(fila.getCell(4).getStringCellValue());
                                        eficienciaTerminalTitulacionRegistro.setProgramaEducativo(areaUniversidad.getArea());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Programa Educativo en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(6).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case FORMULA:
                                        eficienciaTerminalTitulacionRegistro.setGeneracion((short) ((int) fila.getCell(6).getNumericCellValue()));
                                        dtoEficienciaTerminalTitulacionRegistro.setGeneracion(fila.getCell(7).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Generación en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(9).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case FORMULA:
                                        eficienciaTerminalTitulacionRegistro.setPeriodoInicio((int) fila.getCell(9).getNumericCellValue());
                                        dtoEficienciaTerminalTitulacionRegistro.setPeriodoInicio(fila.getCell(10).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Inicio en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(12).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(12).getCellTypeEnum()) {
                                    case FORMULA:
                                        eficienciaTerminalTitulacionRegistro.setPeriodoFin((int) fila.getCell(12).getNumericCellValue());
                                        dtoEficienciaTerminalTitulacionRegistro.setPeriodoFin(fila.getCell(13).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Fin en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(14).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(14).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setAlumnosh((int) fila.getCell(14).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Estudiantes Hombres en la columna: " + (14 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(15).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(15).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setAlumnosm((int) fila.getCell(15).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Estudiantes Mujeres en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(17).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(17).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setEgrecorh((int) fila.getCell(17).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Egresados hombres en la columna: " + (17 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(18).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(18).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setEgrecorm((int) fila.getCell(18).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Egresados mujeres en la columna: " + (18 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(20).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(20).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setRezagadosh((int) fila.getCell(20).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Rezagados Hombres en la columna: " + (20 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(21).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(21).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setRezagadosm((int) fila.getCell(21).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Rezagados Mujeres en la columna: " + (21 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(23).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(23).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setSegcarrerah((int) fila.getCell(23).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Segunda Carrera Hombres en la columna: " + (23 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(24).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(24).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setSegcarreram((int) fila.getCell(24).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Segunda Carrera Mujeres en la columna: " + (24 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(26).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(26).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setEgresadosh((int) fila.getCell(26).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Egresados Hombres en la columna: " + (26 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(27).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(27).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setEgresadosm((int) fila.getCell(27).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Egresados Mujeres en la columna: " + (27 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(29).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(29).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setTituladosh((int) fila.getCell(29).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Titulados Hombres en la columna: " + (29 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(30).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(30).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setTituladosm((int) fila.getCell(30).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Titulados Mujeres en la columna: " + (30 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(32).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(32).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setRegistradosh((int) fila.getCell(32).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Registrados Hombres en la columna: " + (32 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(33).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(33).getCellTypeEnum()) {
                                    case NUMERIC:
                                        eficienciaTerminalTitulacionRegistro.setRegistradosm((int) fila.getCell(33).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Registrados Mujeres en la columna: " + (33 + 1) + " y fila: " + (i + 1));
                            }

                            dtoEficienciaTerminalTitulacionRegistro.setAreasUniversidad(areaUniversidad);
                            dtoEficienciaTerminalTitulacionRegistro.setEficienciaTerminalTitulacionRegistro(eficienciaTerminalTitulacionRegistro);

                            dtoEficienciaTerminalTitulacionRegistros.add(dtoEficienciaTerminalTitulacionRegistro);
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
                        return dtoEficienciaTerminalTitulacionRegistros;
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
    public void guardaEficienciaTerminalTitulacionRegistros(List<DTOEficienciaTerminalTitulacionRegistro> listaEficienciaTerminalTitulacionRegistro, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaEficienciaTerminalTitulacionRegistro.stream().forEach((eficienciaTerminalTitulacionRegistro) -> {
            facadeEscolar.setEntityClass(EficienciaTerminalTitulacionRegistro.class);
            EficienciaTerminalTitulacionRegistro eficienciaTerminalEncontrado = getEficienciaTerminalTitulacionRegistro(eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro());
            Boolean registroAlmacenado = false;
            if (eficienciaTerminalEncontrado != null) {
                listaCondicional.add(eficienciaTerminalTitulacionRegistro.getAreasUniversidad().getNombre() + " " + eficienciaTerminalTitulacionRegistro.getGeneracion());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if(ejbModulos.getEventoRegistro().getEventoRegistro().equals(eficienciaTerminalEncontrado.getRegistros().getEventoRegistro().getEventoRegistro())){
                    eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro().setRegistro(eficienciaTerminalEncontrado.getRegistro());
                    facadeEscolar.edit(eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro());
                }else{
                    listaCondicional.remove(eficienciaTerminalTitulacionRegistro.getAreasUniversidad().getNombre() + " " + eficienciaTerminalTitulacionRegistro.getGeneracion());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro().setRegistro(registro.getRegistro());
                facadeEscolar.create(eficienciaTerminalTitulacionRegistro.getEficienciaTerminalTitulacionRegistro());
            }
            facadeEscolar.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizaron los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public EficienciaTerminalTitulacionRegistro getEficienciaTerminalTitulacionRegistro(EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro) {
        EficienciaTerminalTitulacionRegistro eficienciaTerminalEncontrado = new EficienciaTerminalTitulacionRegistro();
        TypedQuery<EficienciaTerminalTitulacionRegistro> query = em.createQuery("SELECT e FROM EficienciaTerminalTitulacionRegistro e WHERE e.programaEducativo = :programaEducativo AND e.generacion = :generacion", EficienciaTerminalTitulacionRegistro.class);
        query.setParameter("programaEducativo", eficienciaTerminalTitulacionRegistro.getProgramaEducativo());
        query.setParameter("generacion", eficienciaTerminalTitulacionRegistro.getGeneracion());
        try {
            eficienciaTerminalEncontrado = query.getSingleResult();
            eficienciaTerminalEncontrado.setRegistros(ejbModulos.buscaRegistroPorClave(eficienciaTerminalEncontrado.getRegistro()));
        } catch (NoResultException | NonUniqueResultException ex) {
            eficienciaTerminalEncontrado = null;
            ex.toString();
        }
        return eficienciaTerminalEncontrado;
    }

    @Override
    public List<DTOEficienciaTerminalTitulacionRegistro> getFiltroEficienciaTerminalEjercicioMesArea(Short ejercicio, String mes, Short area) {
        try {
            List<DTOEficienciaTerminalTitulacionRegistro> dtoEficienciaTerminal = new ArrayList<>();
            List<EficienciaTerminalTitulacionRegistro> eficienciaTerminal = em.createQuery("SELECT e FROM EficienciaTerminalTitulacionRegistro e JOIN e.registros r JOIN r.eventoRegistro er JOIN er.ejercicioFiscal f WHERE f.anio = :anio AND er.mes = :mes AND r.area = :area", EficienciaTerminalTitulacionRegistro.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            eficienciaTerminal.forEach((et) -> {
                em.refresh(et);
                ActividadesPoa a = et.getRegistros().getActividadesPoaList().isEmpty() ? null : et.getRegistros().getActividadesPoaList().get(0);
                if (a != null) {
                    dtoEficienciaTerminal.add(new DTOEficienciaTerminalTitulacionRegistro(
                            em.find(AreasUniversidad.class, et.getProgramaEducativo()),
                            caster.generacionToString(em.find(Generaciones.class, et.getGeneracion())),
                            caster.periodoToStringAnio(em.find(PeriodosEscolares.class, et.getPeriodoInicio())),
                            caster.periodoToStringAnio(em.find(PeriodosEscolares.class, et.getPeriodoFin())),
                            et,
                            a
                    ));
                } else {
                    dtoEficienciaTerminal.add(new DTOEficienciaTerminalTitulacionRegistro(
                            em.find(AreasUniversidad.class, et.getProgramaEducativo()),
                            caster.generacionToString(em.find(Generaciones.class, et.getGeneracion())),
                            caster.periodoToStringAnio(em.find(PeriodosEscolares.class, et.getPeriodoInicio())),
                            caster.periodoToStringAnio(em.find(PeriodosEscolares.class, et.getPeriodoFin())),
                            et
                    ));
                }
            });
            return dtoEficienciaTerminal;
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

}
