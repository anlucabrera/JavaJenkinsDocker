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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.ca.DTOServiciosEnfemeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.apache.poi.ss.usermodel.CellType;
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
public class ServicioServiciosEnfermeriaCicloPeriodos implements EjbServiciosEnfermeriaCicloPeriodos {

    @EJB    Facade      facadeEscolar;

    @EJB    EjbModulos  ejbModulos;
    
    @Inject Caster      caster;

    @Override
    public List<DTOServiciosEnfemeriaCicloPeriodos> getListaServiciosEnfermeriaCicloPeriodos(String rutaArchivo) throws Throwable {
        if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();

            List<DTOServiciosEnfemeriaCicloPeriodos> listaDtoServiciosEnfemeriaCicloPeriodos = new ArrayList<>();
            ServiciosEnfermeriaCicloPeriodos servicioEnfermeriaCicloPeriodo;
            ServiciosEnfermeriaTipo servicioEnfermeriaTipo;
            DTOServiciosEnfemeriaCicloPeriodos dtoServicioEnfermeriaCicloPeriodo;

            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Servicios Enfermería"))) {
                    for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        if (fila.getCell(1).getNumericCellValue() != 0) {
                            servicioEnfermeriaCicloPeriodo = new ServiciosEnfermeriaCicloPeriodos();
                            servicioEnfermeriaTipo = new ServiciosEnfermeriaTipo();
                            dtoServicioEnfermeriaCicloPeriodo = new DTOServiciosEnfemeriaCicloPeriodos();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioEnfermeriaCicloPeriodo.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                                        dtoServicioEnfermeriaCicloPeriodo.setCicloEscolar(fila.getCell(2).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Ciclo Escolar en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioEnfermeriaCicloPeriodo.setPeriodoEscolar((int) fila.getCell(4).getNumericCellValue());
                                        dtoServicioEnfermeriaCicloPeriodo.setPeriodoEscolar(fila.getCell(5).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Periodo Escolar en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(7).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case FORMULA:
                                        servicioEnfermeriaTipo.setServicio((short) ((int) fila.getCell(7).getNumericCellValue()));
                                        servicioEnfermeriaTipo.setDescripcion(fila.getCell(8).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Servicio en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case NUMERIC:
                                        servicioEnfermeriaCicloPeriodo.setEstH((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Estudiantes Hombres en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(10).getCellTypeEnum()) {
                                    case NUMERIC:
                                        servicioEnfermeriaCicloPeriodo.setEstM((int) fila.getCell(10).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Estudiantes Mujeres en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(12).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(12).getCellTypeEnum()) {
                                    case NUMERIC:
                                        servicioEnfermeriaCicloPeriodo.setPerH((int) fila.getCell(12).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Personal Hombres en la columna: " + (12 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(13).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(13).getCellTypeEnum()) {
                                    case NUMERIC:
                                        servicioEnfermeriaCicloPeriodo.setPerM((int) fila.getCell(13).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Personal Mujeres en la columna: " + (13 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(15).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(15).getCellTypeEnum()) {
                                    case STRING:
                                        servicioEnfermeriaCicloPeriodo.setMes(fila.getCell(15).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Mes en la columna: " + (15 + 1) + " y fila: " + (i + 1));
                            }

                            servicioEnfermeriaCicloPeriodo.setServicio(servicioEnfermeriaTipo);
                            dtoServicioEnfermeriaCicloPeriodo.setServiciosEnfermeriaCicloPeriodos(servicioEnfermeriaCicloPeriodo);

                            listaDtoServiciosEnfemeriaCicloPeriodos.add(dtoServicioEnfermeriaCicloPeriodo);
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
                        return listaDtoServiciosEnfemeriaCicloPeriodos;
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
    public void guardaServiciosEnfermeriaCicloPeriodos(List<DTOServiciosEnfemeriaCicloPeriodos> listaServiciosEnfermeriaCicloPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        List<String> listaCondicional = new ArrayList<>();
        listaServiciosEnfermeriaCicloPeriodos.forEach((servicioEnfermeria) -> {
            facadeEscolar.setEntityClass(ServiciosEnfermeriaCicloPeriodos.class);
            ServiciosEnfermeriaCicloPeriodos servicioEncontrado = getServicioEnfermeriaCicloPeriodo(servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos());
            Boolean registroAlmacenado = false;
            if (servicioEncontrado != null) {
                listaCondicional.add(servicioEnfermeria.getPeriodoEscolar() + " " + servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().getServicio().getDescripcion());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                if(ejbModulos.getEventoRegistro().equals(servicioEncontrado.getRegistros().getEventoRegistro())){
                    servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().setRegistro(servicioEncontrado.getRegistro());
                    facadeEscolar.edit(servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos());
                }else{
                    listaCondicional.remove(servicioEnfermeria.getPeriodoEscolar() + " " + servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().getServicio().getDescripcion());
                }
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos().setRegistro(registro.getRegistro());
                facadeEscolar.create(servicioEnfermeria.getServiciosEnfermeriaCicloPeriodos());
            }
            facadeEscolar.flush();
        });
        Messages.addGlobalInfo("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public ServiciosEnfermeriaCicloPeriodos getServicioEnfermeriaCicloPeriodo(ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos) {
        ServiciosEnfermeriaCicloPeriodos servicioEncontrado = new ServiciosEnfermeriaCicloPeriodos();
        TypedQuery<ServiciosEnfermeriaCicloPeriodos> query = facadeEscolar.getEntityManager().createQuery("SELECT s FROM ServiciosEnfermeriaCicloPeriodos s JOIN s.servicio t WHERE s.cicloEscolar = :cicloEscolar AND s.periodoEscolar = :periodoEscolar AND t.servicio = :servicio AND s.mes = :mes", ServiciosEnfermeriaCicloPeriodos.class);
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

    @Override
    public List<ServiciosEnfermeriaTipo> getServiciosEnfermeriaTipos() throws Throwable {
        try {
            return facadeEscolar.getEntityManager().createQuery("SELECT s FROM ServiciosEnfermeriaTipo s ORDER BY s.descripcion",ServiciosEnfermeriaTipo.class)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<DTOServiciosEnfemeriaCicloPeriodos> getFiltroServiciosEnfermeriaEjercicioMesArea(Short ejercicio, String mes, Short area) {
        List<DTOServiciosEnfemeriaCicloPeriodos> listaDtoServEnf = new ArrayList<>();
        List<ServiciosEnfermeriaCicloPeriodos> serviciosEnfermeria = new ArrayList<>();
        try {
            serviciosEnfermeria = facadeEscolar.getEntityManager().createQuery("SELECT secp FROM ServiciosEnfermeriaCicloPeriodos secp JOIN secp.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", ServiciosEnfermeriaCicloPeriodos.class)
                    .setParameter("anio", ejercicio)
                    .setParameter("mes", mes)
                    .setParameter("area", area)
                    .getResultList();
            serviciosEnfermeria.forEach((senf) -> {
                facadeEscolar.getEntityManager().refresh(senf);
                listaDtoServEnf.add(new DTOServiciosEnfemeriaCicloPeriodos(
                        senf,
                        caster.cicloEscolarToString(facadeEscolar.getEntityManager().find(CiclosEscolares.class, senf.getCicloEscolar())),
                        caster.periodoToString(facadeEscolar.getEntityManager().find(PeriodosEscolares.class, senf.getPeriodoEscolar()))
                ));
            });
            return listaDtoServEnf;
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

}
