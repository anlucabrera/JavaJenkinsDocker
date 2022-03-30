/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAlineacionAcedemica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroPrevioEvidInstEval;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
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
public class ServicioRegAliEdu {
    
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite leer los registros capturados en la plantilla de excel
     * @param rutaArchivo Lista de indicadores que se guardarán
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    public List<DtoAlineacionAcedemica.Presentacion> getListaCatalogos(String rutaArchivo) throws Throwable {
        List<DtoAlineacionAcedemica.Presentacion> listaDtoCatalogoses = new ArrayList<>();
        DtoAlineacionAcedemica.Presentacion presentacion = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
        PlanEstudio estudio;
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFSheet terceraHoja = libroRegistro.getSheetAt(2);
        XSSFSheet cuartaHoja = libroRegistro.getSheetAt(3);
        XSSFRow fila;
        try {
            if (primeraHoja.getSheetName().equals("ObjetivoEducacional")) {
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if ((!"".equals(fila.getCell(4).getStringCellValue()))) {
                        estudio = new PlanEstudio();
                        presentacion = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case NUMERIC:
                                estudio.setIdPlanEstudio((int) fila.getCell(1).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(2).getCellTypeEnum()) {
                            case FORMULA:
                                estudio.setDescripcion(fila.getCell(2).getStringCellValue());
                                presentacion.setPlanEstudio(estudio);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case FORMULA:
                                presentacion.setClave(fila.getCell(4).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(6).getCellTypeEnum()) {
                            case STRING:
                                presentacion.setDescripcion(fila.getCell(6).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        presentacion.setTipoR("Ob");
                        listaDtoCatalogoses.add(presentacion);
                    }
                    libroRegistro.close();
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }

            if (segundaHoja.getSheetName().equals("AtributoEgreso")) {
                for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                    if ((!"".equals(fila.getCell(4).getStringCellValue()))) {
                        estudio = new PlanEstudio();
                        presentacion = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case NUMERIC:
                                estudio.setIdPlanEstudio((int) fila.getCell(1).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(2).getCellTypeEnum()) {
                            case FORMULA:
                                estudio.setDescripcion(fila.getCell(2).getStringCellValue());
                                presentacion.setPlanEstudio(estudio);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case FORMULA:
                                presentacion.setClave(fila.getCell(4).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(6).getCellTypeEnum()) {
                            case STRING:
                                presentacion.setDescripcion(fila.getCell(6).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        presentacion.setTipoR("Ae");
                        listaDtoCatalogoses.add(presentacion);
                    }
                    libroRegistro.close();
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }

            if (terceraHoja.getSheetName().equals("CriterioDesempenio")) {
                for (int i = 2; i <= terceraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) terceraHoja.getRow(i);
                    if ((!"".equals(fila.getCell(4).getStringCellValue()))) {
                        estudio = new PlanEstudio();
                        presentacion = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case NUMERIC:
                                estudio.setIdPlanEstudio((int) fila.getCell(1).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(2).getCellTypeEnum()) {
                            case FORMULA:
                                estudio.setDescripcion(fila.getCell(2).getStringCellValue());
                                presentacion.setPlanEstudio(estudio);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case FORMULA:
                                presentacion.setClave(fila.getCell(4).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(6).getCellTypeEnum()) {
                            case STRING:
                                presentacion.setDescripcion(fila.getCell(6).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        presentacion.setTipoR("Cr");
                        listaDtoCatalogoses.add(presentacion);
                    }
                    libroRegistro.close();
                }
            } else {
                libroRegistro.close();
                excel.delete();
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                Messages.addGlobalWarn("<b>El archivo cargado no corresponde al registro</b>");
                return Collections.EMPTY_LIST;
            }
            if (cuartaHoja.getSheetName().equals("Indicador")) {
                for (int i = 2; i <= cuartaHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) cuartaHoja.getRow(i);
                    if ((!"".equals(fila.getCell(4).getStringCellValue()))) {
                        estudio = new PlanEstudio();
                        presentacion = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case NUMERIC:
                                estudio.setIdPlanEstudio((int) fila.getCell(1).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(2).getCellTypeEnum()) {
                            case FORMULA:
                                estudio.setDescripcion(fila.getCell(2).getStringCellValue());
                                presentacion.setPlanEstudio(estudio);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case FORMULA:
                                presentacion.setClave(fila.getCell(4).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(6).getCellTypeEnum()) {
                            case STRING:
                                presentacion.setDescripcion(fila.getCell(6).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        presentacion.setTipoR("In");
                        listaDtoCatalogoses.add(presentacion);
                    }
                    libroRegistro.close();
                }
                return listaDtoCatalogoses.stream().sorted(DtoAlineacionAcedemica.Presentacion::compareTo).collect(Collectors.toList());
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

    }
    
    public List<DtoAlineacionAcedemica.Presentacion> getListaAlineacionEducativa(String rutaArchivo) throws Throwable {
        List<DtoAlineacionAcedemica.Presentacion> listaDtoCatalogoses = new ArrayList<>();
        DtoAlineacionAcedemica.Presentacion objetivos = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
        DtoAlineacionAcedemica.Presentacion criterio = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
        DtoAlineacionAcedemica.Presentacion indicador = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
        DtoAlineacionAcedemica.Presentacion atributo = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
        
        
        PlanEstudio estudio;
        PlanEstudioMateria estudioMateria;
        Materia materia;
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(5);
        XSSFRow fila;
        try {
            if (primeraHoja.getSheetName().equals("Alineacion")) {
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if (fila.getCell(2).getNumericCellValue() != 0d) {
                        estudio = new PlanEstudio();
                        estudioMateria = new PlanEstudioMateria();
                        materia = new Materia();
                        objetivos = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        criterio = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        atributo = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        indicador = new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(), new AreasUniversidad(), "");
                        switch (fila.getCell(0).getCellTypeEnum()) {
                            case NUMERIC:
                                estudio.setIdPlanEstudio((int) fila.getCell(0).getNumericCellValue());
                                estudioMateria.setGrado((int) fila.getCell(0).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case FORMULA:
                                estudio.setDescripcion(fila.getCell(1).getStringCellValue());
                                objetivos.setPlanEstudio(estudio);
                                criterio.setPlanEstudio(estudio);
                                atributo.setPlanEstudio(estudio);
                                indicador.setPlanEstudio(estudio);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(6).getCellTypeEnum()) {
//                        switch (fila.getCell(7).getCellTypeEnum()) {
                            case NUMERIC:
                                estudioMateria.setGrado((int) fila.getCell(6).getNumericCellValue());
//                                estudioMateria.setGrado((int) fila.getCell(7).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(7).getCellTypeEnum()) {
//                        switch (fila.getCell(8).getCellTypeEnum()) {
                            case STRING:
                                materia.setNombre(fila.getCell(7).getStringCellValue());
//                                materia.setNombre(fila.getCell(8).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(8).getCellTypeEnum()) {
//                        switch (fila.getCell(9).getCellTypeEnum()) {
                            case FORMULA:
                                estudioMateria.setClaveMateria(fila.getCell(8).getStringCellValue());
//                                estudioMateria.setClaveMateria(fila.getCell(9).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(9).getCellTypeEnum()) {
//                        switch (fila.getCell(10).getCellTypeEnum()) {
                            case FORMULA:
                                estudioMateria.setIdPlanMateria((int) fila.getCell(9).getNumericCellValue());
//                                estudioMateria.setIdPlanMateria((int) fila.getCell(10).getNumericCellValue());
                                materia.setIdMateria((int) fila.getCell(9).getNumericCellValue());
//                                materia.setIdMateria((int) fila.getCell(10).getNumericCellValue());
                                estudioMateria.setIdMateria(new Materia());
                                estudioMateria.setIdMateria(materia);
                                objetivos.setPlanEstudioMateria(estudioMateria);
                                criterio.setPlanEstudioMateria(estudioMateria);
                                atributo.setPlanEstudioMateria(estudioMateria);
                                indicador.setPlanEstudioMateria(estudioMateria);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(12).getCellTypeEnum()) {
//                        switch (fila.getCell(14).getCellTypeEnum()) {
                            case STRING:
                                objetivos.setClave(fila.getCell(12).getStringCellValue());
//                                objetivos.setClave(fila.getCell(14).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(13).getCellTypeEnum()) {
//                        switch (fila.getCell(15).getCellTypeEnum()) {
                            case FORMULA:
                                objetivos.setDescripcion(fila.getCell(13).getStringCellValue());
//                                objetivos.setDescripcion(fila.getCell(15).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(14).getCellTypeEnum()) {
//                        switch (fila.getCell(17).getCellTypeEnum()) {
                            case STRING:
                                objetivos.setNivelA(fila.getCell(14).getStringCellValue());
//                                objetivos.setNivelA(fila.getCell(17).getStringCellValue()); Se verificas para la decion de a que taba se almacenara
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(15).getCellTypeEnum()) {
//                       switch (fila.getCell(18).getCellTypeEnum()) {
                            case FORMULA:
                                objetivos.setIde((int) fila.getCell(15).getNumericCellValue());
//                                objetivos.setIde((int) fila.getCell(18).getNumericCellValue());
                                break;
                            default:
                                break;
                        }

                        switch (fila.getCell(18).getCellTypeEnum()) {
//                       switch (fila.getCell(22).getCellTypeEnum()) {
                            case STRING:
                                atributo.setClave(fila.getCell(18).getStringCellValue());
//                                atributo.setClave(fila.getCell(22).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(19).getCellTypeEnum()) {
//                       switch (fila.getCell(23).getCellTypeEnum()) {
                            case FORMULA:
                                atributo.setDescripcion(fila.getCell(19).getStringCellValue());
//                                atributo.setDescripcion(fila.getCell(23).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(20).getCellTypeEnum()) {
//                       switch (fila.getCell(25).getCellTypeEnum()) {
                            case FORMULA:
                                atributo.setIde((int) fila.getCell(20).getNumericCellValue());
//                                atributo.setIde((int) fila.getCell(25).getNumericCellValue());
                                break;
                            default:
                                break;
                        }

                        switch (fila.getCell(23).getCellTypeEnum()) {
//                       switch (fila.getCell(29).getCellTypeEnum()) {
                            case STRING:
                                criterio.setClave(fila.getCell(23).getStringCellValue());
//                                criterio.setClave(fila.getCell(29).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(24).getCellTypeEnum()) {
//                       switch (fila.getCell(30).getCellTypeEnum()) {
                            case FORMULA:
                                criterio.setDescripcion(fila.getCell(24).getStringCellValue());
//                                criterio.setDescripcion(fila.getCell(30).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(25).getCellTypeEnum()) {
//                       switch (fila.getCell(32).getCellTypeEnum()) {
                            case FORMULA:
                                criterio.setIde((int) fila.getCell(25).getNumericCellValue());
//                                criterio.setIde((int) fila.getCell(32).getNumericCellValue());
                                break;
                            default:
                                break;
                        }

                        switch (fila.getCell(28).getCellTypeEnum()) {
//                       switch (fila.getCell(36).getCellTypeEnum()) {
                            case STRING:
                                indicador.setClave(fila.getCell(28).getStringCellValue());
//                                indicador.setClave(fila.getCell(36).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(29).getCellTypeEnum()) {
//                        switch (fila.getCell(37).getCellTypeEnum()) {
                            case FORMULA:
                                indicador.setDescripcion(fila.getCell(29).getStringCellValue());
//                                indicador.setDescripcion(fila.getCell(37).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(30).getCellTypeEnum()) {
//                       switch (fila.getCell(38).getCellTypeEnum()) {
                            case NUMERIC:
                                indicador.setMeta((double) fila.getCell(30).getNumericCellValue());
//                                indicador.setMeta((double) fila.getCell(38).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(31).getCellTypeEnum()) {
//                       switch (fila.getCell(40).getCellTypeEnum()) {
                            case FORMULA:
                                indicador.setIde((int) fila.getCell(31).getNumericCellValue());
//                                indicador.setIde((int) fila.getCell(40).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        
                        objetivos.setTipoR("Ob");
                        criterio.setTipoR("Cr");
                        atributo.setTipoR("Ae");
                        indicador.setTipoR("In");

                        if (!objetivos.getDescripcion().equals("No aplica")) {
                            listaDtoCatalogoses.add(objetivos);
                        } 
                        if (!criterio.getDescripcion().equals("No aplica")) {
                            listaDtoCatalogoses.add(criterio);
                        } 
                        if (!atributo.getDescripcion().equals("No aplica")) {
                            listaDtoCatalogoses.add(atributo);
                        }
                        if (!indicador.getDescripcion().equals("No aplica")) {
                            listaDtoCatalogoses.add(indicador);
                        }
                    }
                    libroRegistro.close();
                }
                return listaDtoCatalogoses.stream().sorted(DtoAlineacionAcedemica.Presentacion::compareToALC).collect(Collectors.toList());
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

    }
    
    public List<MetasPropuestas> getListaMetasPlantilla(String rutaArchivo) throws Throwable {
        List<MetasPropuestas> metasPropuestases = new ArrayList<>();
       
        PlanEstudio estudio;
        PlanEstudioMateria estudioMateria;
        Materia materia;
        MetasPropuestas propuestas;
        
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(1);
        XSSFRow fila;
        try {
            if (primeraHoja.getSheetName().equals("Alineacio")) {
                for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                    fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                    if (fila.getCell(2).getNumericCellValue() != 0d) {
                        estudio = new PlanEstudio();
                        estudioMateria = new PlanEstudioMateria();
                        materia = new Materia();
                        propuestas = new MetasPropuestas();
                        switch (fila.getCell(0).getCellTypeEnum()) {
                            case NUMERIC:
                                estudio.setIdPlanEstudio((int) fila.getCell(0).getNumericCellValue());
                                estudioMateria.setGrado((int) fila.getCell(0).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(1).getCellTypeEnum()) {
                            case FORMULA:
                                estudio.setDescripcion(fila.getCell(1).getStringCellValue());
                                estudioMateria.setIdPlan(new PlanEstudio());
                                estudioMateria.setIdPlan(estudio);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(4).getCellTypeEnum()) {
                            case NUMERIC:
                                estudioMateria.setGrado((int) fila.getCell(4).getNumericCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(7).getCellTypeEnum()) {
                            case STRING:
                                materia.setNombre(fila.getCell(7).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(8).getCellTypeEnum()) {
                            case FORMULA:
                                estudioMateria.setClaveMateria(fila.getCell(8).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(9).getCellTypeEnum()) {
                            case FORMULA:
                                estudioMateria.setIdPlanMateria((int) fila.getCell(9).getNumericCellValue());
                                materia.setIdMateria((int) fila.getCell(9).getNumericCellValue());
                                estudioMateria.setIdMateria(new Materia());
                                estudioMateria.setIdMateria(materia);
                                propuestas.setIdPlanMateria(new PlanEstudioMateria());
                                propuestas.setIdPlanMateria(estudioMateria);
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(10).getCellTypeEnum()) {
                            case STRING:
                                propuestas.setAutonomo(fila.getCell(10).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(11).getCellTypeEnum()) {
                            case STRING:
                                propuestas.setDestacado(fila.getCell(11).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(12).getCellTypeEnum()) {
                            case STRING:
                                propuestas.setSatisfactorio(fila.getCell(12).getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        switch (fila.getCell(13).getCellTypeEnum()) {
                            case STRING:
                                propuestas.setNoAcreditado(fila.getCell(13).getStringCellValue());
                                break;
                            default:
                                break;
                        }

                        metasPropuestases.add(propuestas);
                    }
                    libroRegistro.close();
                }
                return metasPropuestases;
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

    }
    
    
    
    
    /**
     * Permite buscar si existe un registro con la unidad y evidencia de la lista previa
     * @param dtoRegistroPrevioEvidInstEval Registro de la lista previa
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvaluacionSugerida> getRegistroEvaluacionSugerida(DtoRegistroPrevioEvidInstEval dtoRegistroPrevioEvidInstEval){
        try {
            
            EvaluacionSugerida evaluacionReg = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idUnidadMateria=:unidad and e.evidencia.evidencia=:evidencia", EvaluacionSugerida.class)
                    .setParameter("unidad", dtoRegistroPrevioEvidInstEval.getUnidadMateria().getIdUnidadMateria())
                    .setParameter("evidencia", dtoRegistroPrevioEvidInstEval.getEvidenciaEvaluacion().getEvidencia())
                    .getResultStream()
                    .findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(evaluacionReg, "Registro de evaluación sugerida encontrado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener registro de la unidad materia. (ServicioRegEvidInsEval.getRegistroEvaluacionSugerida)", e, null);
        }
    }
    
}
