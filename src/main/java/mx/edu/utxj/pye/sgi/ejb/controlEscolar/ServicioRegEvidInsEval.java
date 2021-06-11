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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroPrevioEvidInstEval;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
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
public class ServicioRegEvidInsEval {
    
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public List<DtoRegistroPrevioEvidInstEval> getListaRegEvidInstEvaluacion(String rutaArchivo) throws Throwable {
        
        List<Boolean> validarCelda = new ArrayList<>();
        List<String> datosInvalidos = new ArrayList<>();
        
        List<DtoRegistroPrevioEvidInstEval> listaDtoRegEvidInstEvaluacion = new ArrayList<>();
        Materia materia;
        UnidadMateria unidadMateria;
        Criterio criterio;
        EvidenciaEvaluacion evidenciaEvaluacion;
        InstrumentoEvaluacion instrumentoEvaluacion;
        DtoRegistroPrevioEvidInstEval dtoRegEvidInstEvaluacion;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        int meta=0;
        
        try{
        if (primeraHoja.getSheetName().equals("Evidencias e instrumentos")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

                if (fila.getCell(22).getNumericCellValue()>0) {
                materia = new Materia();
                unidadMateria = new UnidadMateria();
                criterio = new Criterio();
                evidenciaEvaluacion = new EvidenciaEvaluacion();
                instrumentoEvaluacion = new InstrumentoEvaluacion();
                dtoRegEvidInstEvaluacion = new  DtoRegistroPrevioEvidInstEval();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case NUMERIC:
                        int grado= (int)fila.getCell(0).getNumericCellValue();
                        dtoRegEvidInstEvaluacion.setGrado(grado);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING: 
                        materia.setNombre(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA: 
                        materia.setIdMateria((int)fila.getCell(5).getNumericCellValue());
                        dtoRegEvidInstEvaluacion.setMateria(materia);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(10).getCellTypeEnum()) {
                    case FORMULA:
                        unidadMateria.setIdUnidadMateria((int)fila.getCell(10).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(11).getCellTypeEnum()) {
                    case FORMULA:
                        unidadMateria.setNombre(fila.getCell(11).getStringCellValue());
                        dtoRegEvidInstEvaluacion.setUnidadMateria(unidadMateria);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(12).getCellTypeEnum()) {
                    case FORMULA:
                        System.err.println("No Unidad " + fila.getCell(12).getNumericCellValue());
                        dtoRegEvidInstEvaluacion.setNoUnidad((int)fila.getCell(12).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(13).getCellTypeEnum()) {
                    case STRING: 
                        criterio.setTipo(fila.getCell(13).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(14).getCellTypeEnum()) {
                    case FORMULA: 
                        criterio.setCriterio((int)fila.getCell(14).getNumericCellValue());
                        dtoRegEvidInstEvaluacion.setCriterio(criterio);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(18).getCellTypeEnum()) {
                    case STRING: 
                        evidenciaEvaluacion.setDescripcion(fila.getCell(18).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(19).getCellTypeEnum()) {
                    case FORMULA: 
                        evidenciaEvaluacion.setEvidencia((int)fila.getCell(19).getNumericCellValue());
                        dtoRegEvidInstEvaluacion.setEvidenciaEvaluacion(evidenciaEvaluacion);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(20).getCellTypeEnum()) {
                    case STRING: 
                        instrumentoEvaluacion.setDescripcion(fila.getCell(20).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(21).getCellTypeEnum()) {
                    case FORMULA: 
                        instrumentoEvaluacion.setInstrumento((int)fila.getCell(21).getNumericCellValue());
                        dtoRegEvidInstEvaluacion.setInstrumentoEvaluacion(instrumentoEvaluacion);
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(22).getCellTypeEnum()) {
                    case NUMERIC: 
                        dtoRegEvidInstEvaluacion.setMetaInstrumento((int)fila.getCell(22).getNumericCellValue());
                        meta =  dtoRegEvidInstEvaluacion.getMetaInstrumento();
                        break;
                    default:
                        break;
                }
                
                if (meta <= 0) {
                        validarCelda.add(false);
                        datosInvalidos.add("Dato incorrecto: Valor meta instrumento incorrecto: <b>" + (5 + 1) + " y fila: " + (i + 1) + "</b> \n");
                }
                    
                    listaDtoRegEvidInstEvaluacion.add(dtoRegEvidInstEvaluacion);
                }
            }
            libroRegistro.close();
            if (validarCelda.contains(false)) {
                    Messages.addGlobalError("<b>La hoja de registros de evidencias e instrumentos de evaluación contiene datos que no son válidos, verifique los datos de la plantilla</b>");
                    Messages.addGlobalError(datosInvalidos.toString());
                    return Collections.EMPTY_LIST;
                } else {
                    Messages.addGlobalInfo("<b>Hoja de registros de evidencias e instrumentos de evaluación validada favor de verificar sus datos antes de guardar su información</b>");
                    return listaDtoRegEvidInstEvaluacion;
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

    }
    
    /**
     * Permite guardar la asignación de indicadores del criterio SABER
     * @param lista Lista de indicadores que se guardarán
     * @param periodosEscolar Lista de indicadores que se guardarán
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionSugerida>> guardarEvidInstEval(List<DtoRegistroPrevioEvidInstEval> lista, PeriodosEscolares periodosEscolar){
        try{
            List<EvaluacionSugerida> li = new ArrayList<>();
            
            if(lista == null || lista.isEmpty()) return ResultadoEJB.crearErroneo(2, li, "La lista de evidencias e instrumentos de evaluación no puede ser nula o vacía.");
          
            List<EvaluacionSugerida> l = new ArrayList<>();
           
            lista.forEach(evidenciaInstrumento -> {
                try {
                    if(evidenciaInstrumento.getMetaInstrumento()!= 0){
                        
                        EvaluacionSugerida evalReg = getRegistroEvaluacionSugerida(evidenciaInstrumento).getValor();
                        if(evalReg==null){
                            EvaluacionSugerida evalSug = new EvaluacionSugerida();
                            UnidadMateria um = em.find(UnidadMateria.class, evidenciaInstrumento.getUnidadMateria().getIdUnidadMateria());
                            evalSug.setUnidadMateria(um);
                            evalSug.setEvidencia(evidenciaInstrumento.getEvidenciaEvaluacion());
                            evalSug.setInstrumento(evidenciaInstrumento.getInstrumentoEvaluacion());
                            evalSug.setMetaInstrumento(evidenciaInstrumento.getMetaInstrumento());
                            evalSug.setPeriodoInicio(periodosEscolar.getPeriodo());
                            evalSug.setActivo(true);
                            em.persist(evalSug);
                            l.add(evalSug);
                        }else{
                            evalReg.setInstrumento(evidenciaInstrumento.getInstrumentoEvaluacion());
                            evalReg.setMetaInstrumento(evidenciaInstrumento.getMetaInstrumento());
                            evalReg.setPeriodoInicio(periodosEscolar.getPeriodo());
                            evalReg.setActivo(true);
                            em.merge(evalReg);
                            l.add(evalReg);
                        
                        }
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbRegistroEvidInstEvalMaterias.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "El registro de evidencias e instrumentos de evaluación se ha guardado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar las evidencias e instrumentos de evaluación se ha guardado correctamente. (EjbAsignacionIndicadoresCriterios.guardarListaEvidenciasInstrumentos)", e, null);
        }
    }
    
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
