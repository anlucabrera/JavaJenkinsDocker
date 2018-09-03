/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

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
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTONivelOcupacionEgresadosG;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaEgresados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
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
public class ServicioEgresados implements EjbEgresados {
    
    @EJB
    Facade facadeVinculacion;
    
    @EJB
    EjbModulos ejbModulos;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaEgresados getListaEgresados(String rutaArchivo) throws Throwable {
        ListaEgresados listaEgresados = new ListaEgresados();
//        Vista
        List<DTOActividadEgresadoGeneracion> listaDtoActividadEG = new ArrayList<>();
        ActividadEgresadoGeneracion actividadEG;
        ActividadEgresadoTipos actividadEgresadoTipo;
        DTOActividadEgresadoGeneracion dtoActividadEG;

//        Vista
        List<DTOActividadEconomicaEgresadoG> listaDtoActividadEconomicaEG = new ArrayList<>();
        ActividadEconomicaEgresadoGeneracion actividadEconomicaEG;
        GirosTipo girosTipo;
        SectoresTipo sectoresTipo;
        DTOActividadEconomicaEgresadoG dtoActividadEconomicaEG;

//        Vista
        List<DTONivelOcupacionEgresadosG> listaDtonivelOcupacionEG = new ArrayList<>();
        NivelOcupacionEgresadosGeneracion nivelOcupacionEG;
        NivelOcupacionTipos nivelOcupacionTipo;
        DTONivelOcupacionEgresadosG dtoNivelOcupacionEG;

//        Vista
        List<DTONivelIngresoEgresadosG> listaDtoNivelIngresoEG = new ArrayList<>();
        NivelIngresosEgresadosGeneracion nivelIngresosEG;
        NivelIngresosTipos nivelIngresoTipo;
        DTONivelIngresoEgresadosG dtoNivelIngresoEG;
        
        AreasUniversidad areasUniversidad;
        
        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFSheet segundaHoja = libroRegistro.getSheetAt(1);
        XSSFSheet tercerHoja = libroRegistro.getSheetAt(2);
        XSSFSheet cuartaHoja = libroRegistro.getSheetAt(3);
        XSSFRow fila;
        if ((primeraHoja.getSheetName().equals("Actividad Egresados-as")) || (segundaHoja.getSheetName().equals("Actividad Económica Eg")) || (tercerHoja.getSheetName().equals("Nivel Ocupación") || (cuartaHoja.getSheetName()).equals("Nivel Ingresos"))) {
//            Lectura de la primera hoja
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if (fila.getCell(0).getNumericCellValue() != 0) {
                    actividadEG = new ActividadEgresadoGeneracion();
                    actividadEgresadoTipo = new ActividadEgresadoTipos();
                    dtoActividadEG = new DTOActividadEgresadoGeneracion();
                    areasUniversidad = new AreasUniversidad();
//                    Fecha de corte
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                actividadEG.setFecha(fila.getCell(0).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Generacion
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            dtoActividadEG.setGeneracion(fila.getCell(1).getStringCellValue());
                            actividadEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                    Programa Educativo
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                            areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                            actividadEG.setProgramaEducativo(areasUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
//                    Tipo de actividad
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            actividadEgresadoTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                            actividadEgresadoTipo.setActividad((short) ((fila.getCell(6).getNumericCellValue())));
                            break;
                        default:
                            break;
                    }
//                    Hombres
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            actividadEG.setHombres((int) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Mujeres
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            actividadEG.setMujeres((int) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Total
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            dtoActividadEG.setTotal((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Agregación de elementos a los objetos
                    actividadEG.setActividad(actividadEgresadoTipo);
                    dtoActividadEG.setProgramaEducativo(areasUniversidad);
                    dtoActividadEG.setActividadEgresadoGeneracion(actividadEG);
                    
                    listaDtoActividadEG.add(dtoActividadEG);
                }
            }
//            Lectura de la segunda hoja
            for (int i = 2; i <= segundaHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) segundaHoja.getRow(i);
                if (fila.getCell(0).getNumericCellValue() != 0) {
                    actividadEconomicaEG = new ActividadEconomicaEgresadoGeneracion();
                    girosTipo = new GirosTipo();
                    sectoresTipo = new SectoresTipo();
                    dtoActividadEconomicaEG = new DTOActividadEconomicaEgresadoG();
                    areasUniversidad = new AreasUniversidad();
//                   Fecha de corte
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                actividadEconomicaEG.setFecha(fila.getCell(0).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                   Generación
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            dtoActividadEconomicaEG.setGeneracion(fila.getCell(1).getStringCellValue());
                            actividadEconomicaEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                   Programa Educativo
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                            areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                            actividadEconomicaEG.setProgramaEducativo(areasUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
//                   Sector Trabajo
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            sectoresTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                            sectoresTipo.setSector((short) ((int) fila.getCell(6).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                   Cantidad Sector Trabajo
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            actividadEconomicaEG.setCantSector((int) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                   Actividad Económica
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            girosTipo.setDescripcion(fila.getCell(8).getStringCellValue());
                            girosTipo.setGiro((short) ((int) fila.getCell(9).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                   Cantidad Actividad Económica
                    switch (fila.getCell(10).getCellTypeEnum()) {
                        case NUMERIC:
                            actividadEconomicaEG.setCantGiro((int) fila.getCell(10).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                   Agregación de elementos a los objetos
                    actividadEconomicaEG.setSector(sectoresTipo);
                    actividadEconomicaEG.setGiro(girosTipo);
                    
                    dtoActividadEconomicaEG.setProgramaEducativo(areasUniversidad);
                    dtoActividadEconomicaEG.setActividadEconomicaEgresadoGeneracion(actividadEconomicaEG);
                    
                    listaDtoActividadEconomicaEG.add(dtoActividadEconomicaEG);
                }
            }
//            Lectura de la tercera hoja
            for (int i = 2; i <= tercerHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) tercerHoja.getRow(i);
                if (fila.getCell(0).getNumericCellValue() != 0) {
                    nivelOcupacionEG = new NivelOcupacionEgresadosGeneracion();
                    nivelOcupacionTipo = new NivelOcupacionTipos();
                    areasUniversidad = new AreasUniversidad();
                    dtoNivelOcupacionEG = new DTONivelOcupacionEgresadosG();
//                    Fecha de corte
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                nivelOcupacionEG.setFecha(fila.getCell(0).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Generación
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            dtoNivelOcupacionEG.setGeneracion(fila.getCell(1).getStringCellValue());
                            nivelOcupacionEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                    Programa Educativo
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                            areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                            nivelOcupacionEG.setProgramaEducativo(areasUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
//                    Ocupación
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            nivelOcupacionTipo.setDescripcion(fila.getCell(5).getStringCellValue());
                            nivelOcupacionTipo.setOcupacion((short) ((int) fila.getCell(6).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                    Hombres
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            nivelOcupacionEG.setHombres((int) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Mujeres
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            nivelOcupacionEG.setMujeres((int) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Total
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            dtoNivelOcupacionEG.setTotal((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Agregación de elementos a los objetos
                    nivelOcupacionEG.setOcupacion(nivelOcupacionTipo);
                    
                    dtoNivelOcupacionEG.setProgramaEducativo(areasUniversidad);
                    dtoNivelOcupacionEG.setNivelOcupacionEgresadosGeneracion(nivelOcupacionEG);
                    
                    listaDtonivelOcupacionEG.add(dtoNivelOcupacionEG);
                }
            }
//            Lectura de la cuarta hoja
            for (int i = 2; i <= cuartaHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) cuartaHoja.getRow(i);
                if (fila.getCell(0).getNumericCellValue() != 0) {
                    nivelIngresosEG = new NivelIngresosEgresadosGeneracion();
                    nivelIngresoTipo = new NivelIngresosTipos();
                    areasUniversidad = new AreasUniversidad();
                    dtoNivelIngresoEG = new DTONivelIngresoEgresadosG();
//                    Fecha de corte
                    switch (fila.getCell(0).getCellTypeEnum()) {
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(fila.getCell(0))) {
                                nivelIngresosEG.setFecha(fila.getCell(0).getDateCellValue());
                            }
                            break;
                        default:
                            break;
                    }
//                    Generación
                    switch (fila.getCell(2).getCellTypeEnum()) {
                        case FORMULA:
                            dtoNivelIngresoEG.setGeneracion(fila.getCell(1).getStringCellValue());
                            nivelIngresosEG.setGeneracion((short) ((int) fila.getCell(2).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                    Programa Educativo
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            areasUniversidad.setNombre(fila.getCell(3).getStringCellValue());
                            areasUniversidad.setArea((short) ((int) fila.getCell(4).getNumericCellValue()));
                            nivelIngresosEG.setProgramaEducativo(areasUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
//                    Ingreso
                    switch (fila.getCell(6).getCellTypeEnum()) {
                        case FORMULA:
                            nivelIngresoTipo.setIngresos(fila.getCell(5).getStringCellValue());
                            nivelIngresoTipo.setNivel((short) ((int) fila.getCell(6).getNumericCellValue()));
                            break;
                        default:
                            break;
                    }
//                    Hombres
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case NUMERIC:
                            nivelIngresosEG.setHombres((int) fila.getCell(7).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Mujeres
                    switch (fila.getCell(8).getCellTypeEnum()) {
                        case NUMERIC:
                            nivelIngresosEG.setMujeres((int) fila.getCell(8).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
//                    Total
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case FORMULA:
                            dtoNivelIngresoEG.setTotal((int) fila.getCell(9).getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    nivelIngresosEG.setIngreso(nivelIngresoTipo);
                    
                    dtoNivelIngresoEG.setProgramaEducativo(areasUniversidad);
                    dtoNivelIngresoEG.setNivelIngresosEgresadosGeneracion(nivelIngresosEG);
                    
                    listaDtoNivelIngresoEG.add(dtoNivelIngresoEG);
                }
            }
            
            listaEgresados.setDtoActividadEgresadosGeneracion(listaDtoActividadEG);
            listaEgresados.setDtoActividadEconomicaEgresadoG(listaDtoActividadEconomicaEG);
            listaEgresados.setDtoNivelOcupacionEgresadosG(listaDtonivelOcupacionEG);
            listaEgresados.setDtoNivelIngresoEgresadosG(listaDtoNivelIngresoEG);
            
            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaEgresados;
    }

//    @Override
//    public void guardaEgresados(ListaEgresados listaEgresados) {
////        Actividades Egresados
//        listaEgresados.getActividadEgresadosGeneracion().forEach((actividadEG) -> {
//            facadeVinculacion.setEntityClass(ActividadEgresadoGeneracion.class);
//            facadeVinculacion.create(actividadEG);
//            facadeVinculacion.flush();
//        });
//        
////        Actividades Económicas Egresados
//        listaEgresados.getActividadEconomicaEgresadosGeneracion().forEach((actividadEconomicaEG) -> {
//            facadeVinculacion.setEntityClass(ActividadEconomicaEgresadoGeneracion.class);
//            facadeVinculacion.create(actividadEconomicaEG);
//            facadeVinculacion.flush();
//        });
//        
////        Nivel Ocupación
//        listaEgresados.getNivelOcupacionEgresadosGeneracion().forEach((nivelOcupacionEG) -> {
//            facadeVinculacion.setEntityClass(NivelOcupacionEgresadosGeneracion.class);
//            facadeVinculacion.create(nivelOcupacionEG);
//            facadeVinculacion.flush();
//        });
////        Nivel Ingresos
//        listaEgresados.getNivelIngresosEgresadosGeneracion().forEach((nivelIngresoEG) -> {
//            facadeVinculacion.setEntityClass(NivelIngresosEgresadosGeneracion.class);
//            facadeVinculacion.create(nivelIngresoEG);
//            facadeVinculacion.flush();
//        });
//    }
    @Override
    public void guardaActividadEgresadoGeneracion(List<DTOActividadEgresadoGeneracion> listaActividadEgresadoGeneracion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Actividades Egresados
        List<String> listaCondicional = new ArrayList<>();
        listaActividadEgresadoGeneracion.forEach((actividadEG) -> {
            facadeVinculacion.setEntityClass(ActividadEgresadoGeneracion.class);
            ActividadEgresadoGeneracion actividadEGEncontrado = getActividadEgresadoGeneracion(actividadEG.getActividadEgresadoGeneracion());
            Boolean registroAlmacenado = false;
            if (actividadEGEncontrado != null) {
                listaCondicional.add(actividadEG.getActividadEgresadoGeneracion().getFecha() + " " + actividadEG.getGeneracion() + " " + actividadEG.getProgramaEducativo().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                actividadEG.getActividadEgresadoGeneracion().setRegistro(actividadEGEncontrado.getRegistro());
                facadeVinculacion.edit(actividadEG.getActividadEgresadoGeneracion());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                actividadEG.getActividadEgresadoGeneracion().setRegistro(registro.getRegistro());
                facadeVinculacion.create(actividadEG.getActividadEgresadoGeneracion());
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public void guardaActividadEcnomicaEgresadoG(List<DTOActividadEconomicaEgresadoG> listaActividadEconomicaEgresadoG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Actividades Económicas Egresados
        List<String> listaCondicional = new ArrayList<>();
        listaActividadEconomicaEgresadoG.forEach((actividadEconomicaEG) -> {
            facadeVinculacion.setEntityClass(ActividadEconomicaEgresadoGeneracion.class);
            ActividadEconomicaEgresadoGeneracion actividadEEGEncontrado = getActividadEconomicaEgresadoGeneracion(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion());
            Boolean registroAlmacenado = false;
            if (actividadEEGEncontrado != null) {
                listaCondicional.add(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion().getFecha() + " " + actividadEconomicaEG.getProgramaEducativo().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion().setRegistro(actividadEEGEncontrado.getRegistro());
                facadeVinculacion.edit(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion().setRegistro(registro.getRegistro());
                facadeVinculacion.create(actividadEconomicaEG.getActividadEconomicaEgresadoGeneracion());
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public void guardaNivelOcupacionEgresadoG(List<DTONivelOcupacionEgresadosG> listaNivelOcupacionEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Nivel Ocupación
        List<String> listaCondicional = new ArrayList<>();
        listaNivelOcupacionEgresadosG.forEach((nivelOcupacionEG) -> {
            facadeVinculacion.setEntityClass(NivelOcupacionEgresadosGeneracion.class);
            NivelOcupacionEgresadosGeneracion nivelOEGEncontrado = getNivelOcupacionEgresadosGeneracion(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion());
            Boolean registroAlmacenado = false;
            if (nivelOEGEncontrado != null) {
                listaCondicional.add(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion().getFecha() + " " + nivelOcupacionEG.getProgramaEducativo().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion().setRegistro(nivelOEGEncontrado.getRegistro());
                facadeVinculacion.edit(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion().setRegistro(registro.getRegistro());
                facadeVinculacion.create(nivelOcupacionEG.getNivelOcupacionEgresadosGeneracion());
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public void guardaNivelIngresoEgresadoG(List<DTONivelIngresoEgresadosG> listaNivelIngresoEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
//        Nivel Ingresos
        List<String> listaCondicional = new ArrayList<>();
        listaNivelIngresoEgresadosG.forEach((nivelIngresoEG) -> {
            facadeVinculacion.setEntityClass(NivelIngresosEgresadosGeneracion.class);
            NivelIngresosEgresadosGeneracion nivelIEGEncontrado = getNivelIngresosEgresadosGeneracion(nivelIngresoEG.getNivelIngresosEgresadosGeneracion());
            Boolean registroAlmacenado = false;
            if (nivelIEGEncontrado != null) {
                listaCondicional.add(nivelIngresoEG.getNivelIngresosEgresadosGeneracion().getFecha() + " " + nivelIngresoEG.getProgramaEducativo().getNombre());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                nivelIngresoEG.getNivelIngresosEgresadosGeneracion().setRegistro(nivelIEGEncontrado.getRegistro());
                facadeVinculacion.edit(nivelIngresoEG.getNivelIngresosEgresadosGeneracion());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                nivelIngresoEG.getNivelIngresosEgresadosGeneracion().setRegistro(registro.getRegistro());
                facadeVinculacion.create(nivelIngresoEG.getNivelIngresosEgresadosGeneracion());
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public ActividadEgresadoGeneracion getActividadEgresadoGeneracion(ActividadEgresadoGeneracion actividadEgresadoGeneracion) {
        TypedQuery<ActividadEgresadoGeneracion> query = em.createQuery("SELECT a FROM ActividadEgresadoGeneracion a JOIN a.actividad t WHERE a.fecha = :fecha AND a.generacion = :generacion AND a.programaEducativo = :programaEducativo AND t.actividad = :actividad", ActividadEgresadoGeneracion.class);
        query.setParameter("fecha", actividadEgresadoGeneracion.getFecha());
        query.setParameter("generacion", actividadEgresadoGeneracion.getGeneracion());
        query.setParameter("programaEducativo", actividadEgresadoGeneracion.getProgramaEducativo());
        query.setParameter("actividad", actividadEgresadoGeneracion.getActividad().getActividad());
        try {
            actividadEgresadoGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            actividadEgresadoGeneracion = null;
            ex.toString();
        }
        return actividadEgresadoGeneracion;
    }
    
    @Override
    public ActividadEconomicaEgresadoGeneracion getActividadEconomicaEgresadoGeneracion(ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion) {
        TypedQuery<ActividadEconomicaEgresadoGeneracion> query = em.createQuery("SELECT a FROM ActividadEconomicaEgresadoGeneracion a JOIN a.sector s WHERE a.fecha = :fecha AND a.generacion = :generacion AND a.programaEducativo = :programaEducativo AND s.sector = :sector", ActividadEconomicaEgresadoGeneracion.class);
        query.setParameter("fecha", actividadEconomicaEgresadoGeneracion.getFecha());
        query.setParameter("generacion", actividadEconomicaEgresadoGeneracion.getGeneracion());
        query.setParameter("programaEducativo", actividadEconomicaEgresadoGeneracion.getProgramaEducativo());
        query.setParameter("sector", actividadEconomicaEgresadoGeneracion.getSector().getSector());
        try {
            actividadEconomicaEgresadoGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            actividadEconomicaEgresadoGeneracion = null;
            ex.toString();
        }
        return actividadEconomicaEgresadoGeneracion;
    }
    
    @Override
    public NivelOcupacionEgresadosGeneracion getNivelOcupacionEgresadosGeneracion(NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion) {
        TypedQuery<NivelOcupacionEgresadosGeneracion> query = em.createQuery("SELECT n FROM NivelOcupacionEgresadosGeneracion n JOIN n.ocupacion o WHERE n.fecha = :fecha AND n.generacion = :generacion AND n.programaEducativo = :programaEducativo AND o.ocupacion = :ocupacion", NivelOcupacionEgresadosGeneracion.class);
        query.setParameter("fecha", nivelOcupacionEgresadosGeneracion.getFecha());
        query.setParameter("generacion", nivelOcupacionEgresadosGeneracion.getGeneracion());
        query.setParameter("programaEducativo", nivelOcupacionEgresadosGeneracion.getProgramaEducativo());
        query.setParameter("ocupacion", nivelOcupacionEgresadosGeneracion.getOcupacion().getOcupacion());
        try {
            nivelOcupacionEgresadosGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            nivelOcupacionEgresadosGeneracion = null;
            ex.toString();
        }
        return nivelOcupacionEgresadosGeneracion;
    }
    
    @Override
    public NivelIngresosEgresadosGeneracion getNivelIngresosEgresadosGeneracion(NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion) {
        TypedQuery<NivelIngresosEgresadosGeneracion> query = em.createQuery("SELECT n FROM NivelIngresosEgresadosGeneracion n JOIN n.ingreso i WHERE n.fecha = :fecha AND n.generacion = :generacion AND n.programaEducativo = :programaEducativo AND i.nivel = :nivel", NivelIngresosEgresadosGeneracion.class);
        query.setParameter("fecha", nivelIngresosEgresadosGeneracion.getFecha());
        query.setParameter("generacion", nivelIngresosEgresadosGeneracion.getGeneracion());
        query.setParameter("programaEducativo", nivelIngresosEgresadosGeneracion.getProgramaEducativo());
        query.setParameter("nivel", nivelIngresosEgresadosGeneracion.getIngreso().getNivel());
        try {
            nivelIngresosEgresadosGeneracion = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            nivelIngresosEgresadosGeneracion = null;
            ex.toString();
        }
        return nivelIngresosEgresadosGeneracion;
    }
}
