/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SatisfaccionServtecEducontAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbSatisfaccionServTecEduContAnioMes;
import org.omnifaces.util.Messages;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionSatisfaccionResultados;
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionesServtecEducont;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.vin.DTOEvaluacionSatEduContAnioMes;
import mx.edu.utxj.pye.siip.dto.vin.DTOSatisfaccionServTecEduContAnioMes;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author UTXJ
 */
@Stateless
@MultipartConfig
public class ServicioSatisfaccionServTecEduContAnioMes implements EjbSatisfaccionServTecEduContAnioMes{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    
    @Inject Caster caster;
    
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }

    @Override
    public List<SatisfaccionServtecEducontAnioMes> getListaSatisfaccionServTecEduContAnioMes(String rutaArchivo) throws Throwable {
       if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
//          Listas para muestra del usuario
            List<SatisfaccionServtecEducontAnioMes> listaSatisfaccionServtecEducontAnioMeses = new ArrayList<>();
            SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes;
            ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Satisfacción ServTec EduCont"))) {
//            Lectura de la primera hoja
                    for (int i = 3; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        
                        if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                            satisfaccionServtecEducontAnioMes = new SatisfaccionServtecEducontAnioMes();
                            serviciosTecnologicosAnioMes = new ServiciosTecnologicosAnioMes();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        serviciosTecnologicosAnioMes.setServicio(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Servicio Tecnológico en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }
                            
                            ServiciosTecnologicosAnioMes serviciosTecnologicosBD = getRegistroServicioTecnologicoEspecifico(serviciosTecnologicosAnioMes.getServicio()).getValor();
                            
                            satisfaccionServtecEducontAnioMes.setServicio(serviciosTecnologicosBD);

                            if (fila.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setTotalEncuestados((int) fila.getCell(2).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total de encuestadas y encuestados en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setNumMuySatisfechos((int) fila.getCell(3).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de encuestados muy satisfechos en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(4).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setNumSatisfechos((int) fila.getCell(4).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de encuestados sarisfechos en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setNumRegSatisfechos((int) fila.getCell(5).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de encuestados regularmente satisfechos en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(6).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setNumPocoSatisfechos((int) fila.getCell(6).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de encuestados poco satisfechos en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setNumNoSatisfechos((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de encuestados no satisfechos en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }

                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setNumNoAplica((int) fila.getCell(8).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de encuestados no aplica en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case NUMERIC:
                                         satisfaccionServtecEducontAnioMes.setTotalEvaluables((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Total de evaluables en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }

                            listaSatisfaccionServtecEducontAnioMeses.add(satisfaccionServtecEducontAnioMes);
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
                        return listaSatisfaccionServtecEducontAnioMeses;
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
    public ResultadoEJB<List<SatisfaccionServtecEducontAnioMes>> guardaSatisfaccionServTecEduCont(List<SatisfaccionServtecEducontAnioMes> listaSatisfaccionServTecEduContAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        try{
            List<SatisfaccionServtecEducontAnioMes> listaSatisfaccionServTecEduCont = new ArrayList<>();
             
            if(listaSatisfaccionServTecEduContAnioMes == null || listaSatisfaccionServTecEduContAnioMes.isEmpty()) return ResultadoEJB.crearErroneo(2, listaSatisfaccionServTecEduCont, "La lista de evidencias e instrumentos de evaluación no puede ser nula o vacía.");
            
            listaSatisfaccionServTecEduContAnioMes.forEach((satisServtecEducontAnioMes) -> {
                SatisfaccionServtecEducontAnioMes servicioEncontrado = getRegistroSatisfaccionServTecEduContEspecifico(satisServtecEducontAnioMes.getServicio().getServicio()).getValor();
                Boolean registroAlmacenado = false;
                if (servicioEncontrado != null) {
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getEventoRegistro().equals(servicioEncontrado.getRegistros().getEventoRegistro())) {
                        satisServtecEducontAnioMes.setRegistro(servicioEncontrado.getRegistro());
                        em.merge(satisServtecEducontAnioMes);
                        listaSatisfaccionServTecEduCont.add(satisServtecEducontAnioMes);
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    satisServtecEducontAnioMes.setRegistro(registro.getRegistro());
                    em.persist(satisServtecEducontAnioMes);
                    listaSatisfaccionServTecEduCont.add(satisServtecEducontAnioMes);
                }
                em.flush();
            });
            return ResultadoEJB.crearCorrecto(listaSatisfaccionServTecEduCont, "El registro de satisfacción de servicio tecnológico y educación continua se ha guardado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el registro de satisfacción de servicio tecnológico y educación continua. (ServicioSatisfaccionServTecEduContAnioMes.guardaSatisfaccionServTecEduCont)", e, null);
        }
    }

    @Override
    public ResultadoEJB<SatisfaccionServtecEducontAnioMes> getRegistroSatisfaccionServTecEduContEspecifico(String servicio) {
        try{
            SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes = em.createQuery("SELECT s FROM SatisfaccionServtecEducontAnioMes s WHERE s.servicio.servicio=:servicio", SatisfaccionServtecEducontAnioMes.class)
                .setParameter("servicio", servicio)
                .getResultStream()
                .findFirst().orElse(null);
            
            if (satisfaccionServtecEducontAnioMes == null) {
                return ResultadoEJB.crearErroneo(2, satisfaccionServtecEducontAnioMes, "No se encontró registro de satisfacción de servicio tecnológico y educación continua");
            } else {
                return ResultadoEJB.crearCorrecto(satisfaccionServtecEducontAnioMes, "Se encontró registro de satisfacción de servicio tecnológico y educación continua");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvo registro de satisfacción de servicio tecnológico y educación continua. (ServicioSatisfaccionServTecEduContAnioMes.getRegistroSatisfaccionServTecEduContEspecifico)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<DTOSatisfaccionServTecEduContAnioMes>> getFiltroSatisfaccionSerTecEduContEjercicioMesArea(Short ejercicio, String mes, Short area) {
       try{
            List<DTOSatisfaccionServTecEduContAnioMes> listaSatisfaccionServTecEduCont = em.createQuery("SELECT s FROM SatisfaccionServtecEducontAnioMes s JOIN s.registros r JOIN r.eventoRegistro e JOIN e.ejercicioFiscal f WHERE f.anio = :anio AND e.mes = :mes AND r.area = :area", SatisfaccionServtecEducontAnioMes.class)
                .setParameter("anio", ejercicio)
                .setParameter("mes", mes)
                .setParameter("area", area)
                .getResultStream()
                .map(satisfaccion -> pack(satisfaccion).getValor())
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
       
            return ResultadoEJB.crearCorrecto(listaSatisfaccionServTecEduCont, "Lista de registros de satisfacción de servicio tecnológico y educación continua educativo por ejercicio, mes y área.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros de satisfacción de servicio tecnológico y educación continua educativo por ejercicio, mes y área. (ServicioSatisfaccionServTecEduContAnioMes.getFiltroSatisfaccionSerTecEduContEjercicioMesArea)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<DTOSatisfaccionServTecEduContAnioMes>> getReporteGeneralSatisfaccionSerTecEduCont(Short ejercicio) {
       try{
            List<DTOSatisfaccionServTecEduContAnioMes> listaReporteSatisfaccionServTecEduCont = em.createQuery("SELECT s FROM SatisfaccionServtecEducontAnioMes s INNER JOIN s.servicio ser INNER JOIN s.registros r WHERE r.eventoRegistro.ejercicioFiscal.anio = :ejercicioFiscal ORDER BY ser.fechaInicio", SatisfaccionServtecEducontAnioMes.class)
                .setParameter("ejercicioFiscal", ejercicio)
                .getResultStream()
                .map(satisfaccion -> pack(satisfaccion).getValor())
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
       
            return ResultadoEJB.crearCorrecto(listaReporteSatisfaccionServTecEduCont, "Reporte de satisfacción de servicio tecnológico y educación continua educativo por ejercicio fiscal.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el reporte de satisfacción de servicio tecnológico y educación continua educativo por ejercicio fiscal. (ServicioSatisfaccionServTecEduContAnioMes.getReporteGeneralSatisfaccionSerTecEduCont)", e, null);
        }
    }
    
    public ResultadoEJB<DTOSatisfaccionServTecEduContAnioMes> pack(SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes){
        try{
            if(satisfaccionServtecEducontAnioMes == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un registro de satisfacción nulo.", DTOSatisfaccionServTecEduContAnioMes.class);
            if(satisfaccionServtecEducontAnioMes.getServicio().getServicio()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un un registro de satisfacción con clave nula.", DTOSatisfaccionServTecEduContAnioMes.class);

            SatisfaccionServtecEducontAnioMes satisfaccionBD = em.find(SatisfaccionServtecEducontAnioMes.class, satisfaccionServtecEducontAnioMes.getRegistro());
            if(satisfaccionBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un registro de satisfacción no registrado previamente en base de datos.", DTOSatisfaccionServTecEduContAnioMes.class);

            Double porMS = (double) satisfaccionBD.getNumMuySatisfechos() / satisfaccionBD.getTotalEncuestados();
            Double porS = (double) satisfaccionBD.getNumSatisfechos() / satisfaccionBD.getTotalEncuestados();
            Double porRS = (double) satisfaccionBD.getNumRegSatisfechos() / satisfaccionBD.getTotalEncuestados();
            Double porPS = (double) satisfaccionBD.getNumPocoSatisfechos() / satisfaccionBD.getTotalEncuestados();
            Double porNS = (double) satisfaccionBD.getNumNoSatisfechos() / satisfaccionBD.getTotalEncuestados();
            Double porNA = (double) satisfaccionBD.getNumNoAplica() / satisfaccionBD.getTotalEncuestados();

            Double sat5 = (double) ((satisfaccionBD.getNumMuySatisfechos() * 5) + (satisfaccionBD.getNumSatisfechos() * 4) + (satisfaccionBD.getNumRegSatisfechos() * 3) + (satisfaccionBD.getNumPocoSatisfechos() * 2) + (satisfaccionBD.getNumNoSatisfechos() * 1)) / satisfaccionBD.getTotalEvaluables();
            Double sat10 = sat5 * 2;
            Integer numSat = satisfaccionBD.getNumMuySatisfechos() + satisfaccionBD.getNumSatisfechos();
            Double sat = (double) numSat / satisfaccionBD.getTotalEncuestados();
            
            DTOSatisfaccionServTecEduContAnioMes dto = new DTOSatisfaccionServTecEduContAnioMes();
            dto.setSatisfaccionServtecEducontAnioMes(satisfaccionBD);
            dto.setPorcentajeMuySatisf(porMS);
            dto.setPorcentajeSatisf(porS);
            dto.setPorcentajeRegSatisf(porRS);
            dto.setPorcentajePocSatisf(porPS);
            dto.setPorcentajeNoSatisf(porNS);
            dto.setPorcentajeNoAplica(porNA);
            dto.setSatisBase5(sat5);
            dto.setSatisBase10(sat10);
            dto.setNumSatisfaccion(numSat);
            dto.setSatisfaccion(sat);
            
            return ResultadoEJB.crearCorrecto(dto, "Registro de satisfacción empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el registro de satisfacción (ServicioSatisfaccionServTecEduContAnioMes. pack).", e, DTOSatisfaccionServTecEduContAnioMes.class);
        }
    }

    @Override
    public  ResultadoEJB<List<ServiciosTecnologicosAnioMes>> getListaServicios() throws Throwable {
        try{
            EventosRegistros eventoRegistroActivo = ejbModulos.getEventoRegistro();
            
            List<ServiciosTecnologicosAnioMes> listaReporteSatisfaccionServTecEduCont = em.createQuery("SELECT s FROM ServiciosTecnologicosAnioMes s INNER JOIN s.registros r WHERE r.eventoRegistro.ejercicioFiscal.ejercicioFiscal =:ejercicio ORDER BY r.eventoRegistro.eventoRegistro, s.servicio ASC", ServiciosTecnologicosAnioMes.class)
                .setParameter("ejercicio", eventoRegistroActivo.getEjercicioFiscal().getEjercicioFiscal())
                .getResultStream()
                .collect(Collectors.toList());
       
            return ResultadoEJB.crearCorrecto(listaReporteSatisfaccionServTecEduCont, "Lista de servicios tecnológicos y educación continua registrados en el ejercicio fiscal vigente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener Lista de servicios tecnológicos y educación continua registrados en el ejercicio fiscal vigente. (ServicioSatisfaccionServTecEduContAnioMes.getListaServicios)", e, null);
        }
    }

    @Override
    public  ResultadoEJB<SatisfaccionServtecEducontAnioMes> actualizarSatisfaccionSerTecEduContAnioMes(SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes) {
        try{
            em.merge(satisfaccionServtecEducontAnioMes);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(satisfaccionServtecEducontAnioMes, "El registro de satisfacción de servicio tecnológico y educación continua se ha actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el registro de satisfacción de servicio tecnológico y educación continua. (ServicioSatisfaccionServTecEduContAnioMes.actualizarSatisfaccionSerTecEduContAnioMes)", e, null);
        }
    }

    @Override
    public ResultadoEJB<ServiciosTecnologicosAnioMes> getRegistroServicioTecnologicoEspecifico(String servicio) {
        try{
            ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes = em.createQuery("SELECT s FROM ServiciosTecnologicosAnioMes s WHERE s.servicio=:servicio", ServiciosTecnologicosAnioMes.class)
                .setParameter("servicio", servicio)
                .getResultStream()
                .findFirst().orElse(null);
            
            if (serviciosTecnologicosAnioMes == null) {
                return ResultadoEJB.crearErroneo(2, serviciosTecnologicosAnioMes, "No se encontró registro de servicio tecnológico");
            } else {
                return ResultadoEJB.crearCorrecto(serviciosTecnologicosAnioMes, "Se encontró registro de servicio tecnológico");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvo registro de servicio tecnológico. (ServicioSatisfaccionServTecEduContAnioMes.getRegistroServicioTecnologicoEspecifico)", e, null);
        }
    }

    @Override
    public List<DTOEvaluacionSatEduContAnioMes.LecturaPlantilla> getListaEvaluacionSatEduContAnioMes(String rutaArchivo) throws Throwable {
       if (Files.exists(Paths.get(rutaArchivo))) {
            List<Boolean> validarCelda = new ArrayList<>();
            List<String> datosInvalidos = new ArrayList<>();
//          Listas para muestra del usuario
            List<DTOEvaluacionSatEduContAnioMes.LecturaPlantilla> listaEvaluacionSatisfaccionResultados = new ArrayList<>();
            DTOEvaluacionSatEduContAnioMes.LecturaPlantilla dtoLecturaPlantilla;
            EvaluacionesServtecEducont evaluacionesServtecEducont;
            ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes;

//        Utilización y apertura del archivo recibido
            File excel = new File(rutaArchivo);
            XSSFWorkbook libroRegistro = new XSSFWorkbook();
            libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
            XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
            XSSFRow fila;

            try {
                if ((primeraHoja.getSheetName().equals("Evaluación Satisfacción EduCont"))) {
//            Lectura de la primera hoja
                    for (int i = 4; i <= primeraHoja.getLastRowNum(); i++) {
                        fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                        
                        if ((!"".equals(fila.getCell(1).getStringCellValue()))) {
                            dtoLecturaPlantilla = new DTOEvaluacionSatEduContAnioMes.LecturaPlantilla();
                            evaluacionesServtecEducont = new EvaluacionesServtecEducont();
                            serviciosTecnologicosAnioMes = new ServiciosTecnologicosAnioMes();

                            if (fila.getCell(1).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(1).getCellTypeEnum()) {
                                    case FORMULA:
                                        serviciosTecnologicosAnioMes.setServicio(fila.getCell(1).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Servicio Tecnológico en la columna: " + (1 + 1) + " y fila: " + (i + 1));
                            }
                            
                            ServiciosTecnologicosAnioMes serviciosTecnologicosBD = getRegistroServicioTecnologicoEspecifico(serviciosTecnologicosAnioMes.getServicio()).getValor();
                            
                            dtoLecturaPlantilla.setServiciosTecnologicosAnioMes(serviciosTecnologicosBD);

                            if (fila.getCell(2).getCellTypeEnum() == CellType.STRING) {
                                switch (fila.getCell(2).getCellTypeEnum()) {
                                    case STRING:
                                         dtoLecturaPlantilla.setPregunta(fila.getCell(2).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Pregunta en la columna: " + (2 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(3).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(3).getCellTypeEnum()) {
                                    case FORMULA:
                                         dtoLecturaPlantilla.setNumeroPregunta(fila.getCell(3).getStringCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Número de pregunta en la columna: " + (3 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(4).getCellTypeEnum() == CellType.FORMULA) {
                                switch (fila.getCell(4).getCellTypeEnum()) {
                                    case NUMERIC:
                                         evaluacionesServtecEducont.setEvaluacion((int)fila.getCell(4).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Clave de la evaluación en la columna: " + (4 + 1) + " y fila: " + (i + 1));
                            }
                            
                            EvaluacionesServtecEducont evaluacionBD = getEvaluacionActiva().getValor();
                            
                            dtoLecturaPlantilla.setEvaluacionesServtecEducont(evaluacionBD);
                            
                            
                            if (fila.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(5).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasA((int) fila.getCell(5).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas A en la columna: " + (5 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(6).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(6).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasB((int) fila.getCell(6).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas B en la columna: " + (6 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(7).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(7).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasC((int) fila.getCell(7).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas C en la columna: " + (7 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(8).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(8).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasD((int) fila.getCell(8).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas D en la columna: " + (8 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(9).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(9).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasE((int) fila.getCell(9).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas E en la columna: " + (9 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(10).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(10).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasF((int) fila.getCell(10).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas F en la columna: " + (10 + 1) + " y fila: " + (i + 1));
                            }
                            
                            if (fila.getCell(11).getCellTypeEnum() == CellType.NUMERIC) {
                                switch (fila.getCell(11).getCellTypeEnum()) {
                                    case NUMERIC:
                                         dtoLecturaPlantilla.setRespuestasG((int) fila.getCell(11).getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                validarCelda.add(false);
                                datosInvalidos.add("Dato incorrecto: Respuestas G en la columna: " + (11 + 1) + " y fila: " + (i + 1));
                            }



                            listaEvaluacionSatisfaccionResultados.add(dtoLecturaPlantilla);
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
                        return listaEvaluacionSatisfaccionResultados;
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
    public ResultadoEJB<List<EvaluacionSatisfaccionResultados>> guardaEvaluacionSatEduCont(List<DTOEvaluacionSatEduContAnioMes.LecturaPlantilla> listaEvaluacionSatisfaccionResultados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable {
        try{
            List<EvaluacionSatisfaccionResultados> listaEvaluacionesSatisfaccion = new ArrayList<>();
             
            if(listaEvaluacionSatisfaccionResultados == null || listaEvaluacionSatisfaccionResultados.isEmpty()) return ResultadoEJB.crearErroneo(2, listaEvaluacionesSatisfaccion, "La lista de evidencias e instrumentos de evaluación no puede ser nula o vacía.");
            
            listaEvaluacionSatisfaccionResultados.forEach((evaluacionSatisfaccion) -> {
                EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados = new EvaluacionSatisfaccionResultados();
                evaluacionSatisfaccionResultados.setEvaluacion(evaluacionSatisfaccion.getEvaluacionesServtecEducont());
                evaluacionSatisfaccionResultados.setServicioTecnologico(evaluacionSatisfaccion.getServiciosTecnologicosAnioMes());
                evaluacionSatisfaccionResultados.setNumeroPregunta(evaluacionSatisfaccion.getNumeroPregunta());
                evaluacionSatisfaccionResultados.setRespuestasA(evaluacionSatisfaccion.getRespuestasA());
                evaluacionSatisfaccionResultados.setRespuestasB(evaluacionSatisfaccion.getRespuestasB());
                evaluacionSatisfaccionResultados.setRespuestasC(evaluacionSatisfaccion.getRespuestasC());
                evaluacionSatisfaccionResultados.setRespuestasD(evaluacionSatisfaccion.getRespuestasD());
                evaluacionSatisfaccionResultados.setRespuestasE(evaluacionSatisfaccion.getRespuestasE());
                evaluacionSatisfaccionResultados.setRespuestasF(evaluacionSatisfaccion.getRespuestasF());
                evaluacionSatisfaccionResultados.setRespuestasG(evaluacionSatisfaccion.getRespuestasG());
                
                EvaluacionSatisfaccionResultados evaluacionSatisfaccionEncontrado = getRegistroEvaluacionSatEduContEspecifico(evaluacionSatisfaccion.getServiciosTecnologicosAnioMes().getServicio(), evaluacionSatisfaccion.getNumeroPregunta()).getValor();
                Boolean registroAlmacenado = false;
                if (evaluacionSatisfaccionEncontrado != null) {
                    registroAlmacenado = true;
                }
                if (registroAlmacenado) {
                    if (ejbModulos.getEventoRegistro().equals(evaluacionSatisfaccionEncontrado.getRegistros().getEventoRegistro())) {
                        evaluacionSatisfaccionResultados.setRegistro(evaluacionSatisfaccionEncontrado.getRegistro());
                        em.merge(evaluacionSatisfaccionResultados);
                        listaEvaluacionesSatisfaccion.add(evaluacionSatisfaccionResultados);
                    }
                } else {
                    Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                    evaluacionSatisfaccionResultados.setRegistro(registro.getRegistro());
                    em.persist(evaluacionSatisfaccionResultados);
                    listaEvaluacionesSatisfaccion.add(evaluacionSatisfaccionResultados);
                }
                em.flush();
            });
            return ResultadoEJB.crearCorrecto(listaEvaluacionesSatisfaccion, "El registro de evaluación de satisfacción de servicio tecnológico de educación continua se ha guardado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el registro de evalución de satisfacción de servicio tecnológico de educación continua. (ServicioSatisfaccionServTecEduContAnioMes.guardaEvaluacionSatEduCont)", e, null);
        }
    }

    @Override
    public ResultadoEJB<EvaluacionSatisfaccionResultados> getRegistroEvaluacionSatEduContEspecifico(String servicio, String numeroPregunta) {
        try{
            EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados = em.createQuery("SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.servicioTecnologico.servicio=:servicio AND e.numeroPregunta=:pregunta", EvaluacionSatisfaccionResultados.class)
                .setParameter("servicio", servicio)
                .setParameter("pregunta", numeroPregunta)
                .getResultStream()
                .findFirst().orElse(null);
            
            if (evaluacionSatisfaccionResultados == null) {
                return ResultadoEJB.crearErroneo(2, evaluacionSatisfaccionResultados, "No se encontró registro de evaluación de satisfacción de servicio tecnológico y educación continua");
            } else {
                return ResultadoEJB.crearCorrecto(evaluacionSatisfaccionResultados, "Se encontró registro de evaluación de satisfacción de servicio tecnológico y educación continua");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvo registro de evaluación de satisfacción de servicio tecnológico y educación continua. (ServicioSatisfaccionServTecEduContAnioMes.getRegistroEvaluacionSatEduContEspecifico)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros>> getFiltroEvaluacionSatEduContEjercicioMesArea(Short ejercicio, String mes, Short area) {
        try{
            List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros> listaDtoEvaluacionSatEduContAnioMeses = new ArrayList<>();
            
            List<EvaluacionSatisfaccionResultados> listaEvaluacionResultados = em.createQuery("SELECT e FROM EvaluacionSatisfaccionResultados e JOIN e.registros r JOIN r.eventoRegistro ev JOIN ev.ejercicioFiscal f WHERE f.anio = :anio AND ev.mes = :mes AND r.area = :area", EvaluacionSatisfaccionResultados.class)
                .setParameter("anio", ejercicio)
                .setParameter("mes", mes)
                .setParameter("area", area)
                .getResultStream()
                .collect(Collectors.toList());
                
            listaEvaluacionResultados.forEach(evalRes -> {
                    DTOEvaluacionSatEduContAnioMes.ConsultaRegistros dTOEvaluacionSatEduContAnioMes = new DTOEvaluacionSatEduContAnioMes.ConsultaRegistros();
                    dTOEvaluacionSatEduContAnioMes.setEvaluacionSatisfaccionResultados(evalRes);
                    
                    dTOEvaluacionSatEduContAnioMes.setNumeroPregunta(Integer.parseInt(evalRes.getNumeroPregunta()));
                    
                    switch(evalRes.getNumeroPregunta()){
                        case "1":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta1());
                            break;
                        case "2":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta2());
                            break;
                        case "3":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta3());
                            break;
                        case "4":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta4());
                            break;
                        case "5":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta5());
                            break;
                        case "6":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta6());
                            break;
                        case "7":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta7());
                            break;
                        case "8":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta8());
                            break;
                        case "9":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta9());
                            break;
                        case "10":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta10());
                            break;
                        case "11":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta11());
                            break;
                        case "12":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta12());
                            break;
                            
                        default:
                            break;
                    }
                    
                    dTOEvaluacionSatEduContAnioMes.setTotalH(evalRes.getRespuestasA() + evalRes.getRespuestasB() + evalRes.getRespuestasC() + evalRes.getRespuestasD() + evalRes.getRespuestasE() + evalRes.getRespuestasF() + evalRes.getRespuestasG());
                    dTOEvaluacionSatEduContAnioMes.setTotalI(evalRes.getRespuestasA() + evalRes.getRespuestasB() + evalRes.getRespuestasC() + evalRes.getRespuestasD() + evalRes.getRespuestasE() + evalRes.getRespuestasF());
                    dTOEvaluacionSatEduContAnioMes.setTotalJ((evalRes.getRespuestasA() * 5) + (evalRes.getRespuestasB() * 4) + (evalRes.getRespuestasC() * 3) + (evalRes.getRespuestasD() * 2) + (evalRes.getRespuestasE() * 1));
                    dTOEvaluacionSatEduContAnioMes.setSatisBase5((double)dTOEvaluacionSatEduContAnioMes.getTotalJ()/dTOEvaluacionSatEduContAnioMes.getTotalI());
                    dTOEvaluacionSatEduContAnioMes.setSatisBase10((double)dTOEvaluacionSatEduContAnioMes.getSatisBase5()*2);
                    dTOEvaluacionSatEduContAnioMes.setNumSatisfaccion(evalRes.getRespuestasA() + evalRes.getRespuestasB());
                    dTOEvaluacionSatEduContAnioMes.setSatisfaccion((double)dTOEvaluacionSatEduContAnioMes.getNumSatisfaccion()/dTOEvaluacionSatEduContAnioMes.getTotalH());
                   
                    listaDtoEvaluacionSatEduContAnioMeses.add(dTOEvaluacionSatEduContAnioMes);
                
                });
                
            return ResultadoEJB.crearCorrecto(listaDtoEvaluacionSatEduContAnioMeses, "Lista de registros de evaluación de satisfaccción de servicio tecnológico y educación continua educativo por ejercicio, mes y área.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros de evaluación de satisfaccción de servicio tecnológico y educación continua educativo por ejercicio, mes y área. (ServicioSatisfaccionServTecEduContAnioMes.getFiltroEvaluacionSatEduContEjercicioMesArea)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros>> getReporteGeneralEvaluacionSatEduCont(Short ejercicio) {
        try{
             List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros> listaDtoEvaluacionSatEduContAnioMeses = new ArrayList<>();
            
            List<EvaluacionSatisfaccionResultados> listaEvaluacionResultados = em.createQuery("SELECT e FROM EvaluacionSatisfaccionResultados e JOIN e.registros r JOIN r.eventoRegistro ev JOIN ev.ejercicioFiscal f WHERE f.anio = :anio", EvaluacionSatisfaccionResultados.class)
                .setParameter("anio", ejercicio)
                .getResultStream()
                .collect(Collectors.toList());
                
            listaEvaluacionResultados.forEach(evalRes -> {
                    DTOEvaluacionSatEduContAnioMes.ConsultaRegistros dTOEvaluacionSatEduContAnioMes = new DTOEvaluacionSatEduContAnioMes.ConsultaRegistros();
                    dTOEvaluacionSatEduContAnioMes.setEvaluacionSatisfaccionResultados(evalRes);
                    
                    dTOEvaluacionSatEduContAnioMes.setNumeroPregunta(Integer.parseInt(evalRes.getNumeroPregunta()));
                    
                    switch(evalRes.getNumeroPregunta()){
                        case "1":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta1());
                            break;
                        case "2":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta2());
                            break;
                        case "3":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta3());
                            break;
                        case "4":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta4());
                            break;
                        case "5":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta5());
                            break;
                        case "6":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta6());
                            break;
                        case "7":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta7());
                            break;
                        case "8":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta8());
                            break;
                        case "9":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta9());
                            break;
                        case "10":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta10());
                            break;
                        case "11":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta11());
                            break;
                        case "12":
                            dTOEvaluacionSatEduContAnioMes.setPregunta(evalRes.getEvaluacion().getPregunta12());
                            break;
                            
                        default:
                            break;
                    }
                    
                    dTOEvaluacionSatEduContAnioMes.setTotalH(evalRes.getRespuestasA() + evalRes.getRespuestasB() + evalRes.getRespuestasC() + evalRes.getRespuestasD() + evalRes.getRespuestasE() + evalRes.getRespuestasF() + evalRes.getRespuestasG());
                    dTOEvaluacionSatEduContAnioMes.setTotalI(evalRes.getRespuestasA() + evalRes.getRespuestasB() + evalRes.getRespuestasC() + evalRes.getRespuestasD() + evalRes.getRespuestasE() + evalRes.getRespuestasF());
                    dTOEvaluacionSatEduContAnioMes.setTotalJ((evalRes.getRespuestasA() * 5) + (evalRes.getRespuestasB() * 4) + (evalRes.getRespuestasC() * 3) + (evalRes.getRespuestasD() * 2) + (evalRes.getRespuestasE() * 1));
                    dTOEvaluacionSatEduContAnioMes.setSatisBase5((double)dTOEvaluacionSatEduContAnioMes.getTotalJ()/dTOEvaluacionSatEduContAnioMes.getTotalI());
                    dTOEvaluacionSatEduContAnioMes.setSatisBase10((double)dTOEvaluacionSatEduContAnioMes.getSatisBase5()*2);
                    dTOEvaluacionSatEduContAnioMes.setNumSatisfaccion(evalRes.getRespuestasA() + evalRes.getRespuestasB());
                    dTOEvaluacionSatEduContAnioMes.setSatisfaccion((double)dTOEvaluacionSatEduContAnioMes.getNumSatisfaccion()/dTOEvaluacionSatEduContAnioMes.getTotalH());
                   
                    listaDtoEvaluacionSatEduContAnioMeses.add(dTOEvaluacionSatEduContAnioMes);
                
                });
       
            return ResultadoEJB.crearCorrecto(listaDtoEvaluacionSatEduContAnioMeses, "Lista de registros de evaluación de satisfaccción de servicio tecnológico y educación continua educativo por ejercicio, mes y área.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de registros de evaluación de satisfaccción de servicio tecnológico y educación continua educativo por ejercicio, mes y área. (ServicioSatisfaccionServTecEduContAnioMes.getReporteGeneralEvaluacionSatEduCont)", e, null);
        }
    }
    
    @Override
    public ResultadoEJB<EvaluacionSatisfaccionResultados> actualizarEvaluacionSatEduContAnioMes(EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados) {
       try{
            em.merge(evaluacionSatisfaccionResultados);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(evaluacionSatisfaccionResultados, "El registro de evaluación de satisfacción de servicio tecnológico de educación continua se ha actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el registro de evaluación de satisfacción de servicio tecnológico de educación continua. (ServicioSatisfaccionServTecEduContAnioMes.actualizarEvaluacionSatEduContAnioMes)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla>> getListaPreguntasEvaluacion() throws Throwable {
       try{
           List<DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla> listaPreguntas = new ArrayList<>();
           
           EvaluacionesServtecEducont evaluacionesServtecEducont = em.createQuery("SELECT e FROM EvaluacionesServtecEducont e WHERE e.activa=:valor order by e.evaluacion DESC", EvaluacionesServtecEducont.class)
                .setParameter("valor", true)
                .getResultStream()
                .findFirst().orElse(null);
           
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg1 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "1", evaluacionesServtecEducont.getPregunta1());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg2 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "2", evaluacionesServtecEducont.getPregunta2());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg3 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "3", evaluacionesServtecEducont.getPregunta3());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg4 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "4", evaluacionesServtecEducont.getPregunta4());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg5 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "5", evaluacionesServtecEducont.getPregunta5());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg6 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "6", evaluacionesServtecEducont.getPregunta6());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg7 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "7", evaluacionesServtecEducont.getPregunta7());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg8 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "8", evaluacionesServtecEducont.getPregunta8());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg9 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "9", evaluacionesServtecEducont.getPregunta9());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg10 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "10", evaluacionesServtecEducont.getPregunta10());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg11 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "11", evaluacionesServtecEducont.getPregunta11());
           DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla preg12 = new DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla(evaluacionesServtecEducont, "12", evaluacionesServtecEducont.getPregunta12());

           listaPreguntas.add(preg1);
           listaPreguntas.add(preg2);
           listaPreguntas.add(preg3);
           listaPreguntas.add(preg4);
           listaPreguntas.add(preg5);
           listaPreguntas.add(preg6);
           listaPreguntas.add(preg7);
           listaPreguntas.add(preg8);
           listaPreguntas.add(preg9);
           listaPreguntas.add(preg10);
           listaPreguntas.add(preg11);
           listaPreguntas.add(preg12);

       
            return ResultadoEJB.crearCorrecto(listaPreguntas, "Lista de preguntas de evaluación activa de satisfacción de educación continua.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de preguntas de evaluación activa de satisfacción de educación continua. (ServicioSatisfaccionServTecEduContAnioMes.getListaPreguntasEvaluacion)", e, null);
        }
    }

    @Override
    public ResultadoEJB<EvaluacionesServtecEducont> getEvaluacionActiva() {
        try{
             EvaluacionesServtecEducont evaluacionesServtecEducont = em.createQuery("SELECT e FROM EvaluacionesServtecEducont e WHERE e.activa=:valor order by e.evaluacion DESC", EvaluacionesServtecEducont.class)
                .setParameter("valor", true)
                .getResultStream()
                .findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(evaluacionesServtecEducont, "Se encontró evaluación de satisfacción de servicio tecnológico y educación continua activa.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar evaluación de satisfacción de servicio tecnológico de educación continua activa. (ServicioSatisfaccionServTecEduContAnioMes.getEvaluacionActiva)", e, null);
        }
    }
}
