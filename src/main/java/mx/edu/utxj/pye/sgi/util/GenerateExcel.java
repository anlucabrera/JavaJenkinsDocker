/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.ConsultaCalificacionesEstudiante;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.RegistroEgresadosTerminacionEstudios;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PlanCompetencias;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.omnifaces.cdi.ViewScoped;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class GenerateExcel implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private enum Border { INSIDE_V, INSIDE_H, LEFT, TOP, BOTTOM, RIGHT }
    @Getter @Setter private XSSFWorkbook libro;
    @Getter @Setter private XWPFDocument word;
    @Getter @Setter private String nombreExcel, nombreWord, salida, base, hoja;
    @Getter @Setter private DecimalFormat df = new DecimalFormat("0.00");
    @EJB EjbSeguimientoTestDiagnosticoAprendizaje ejbSTDA;
    @Inject ConsultaCalificacionesEstudiante controlador;
    InputStream in;
    OutputStream out;

    public void obtenerLibro(String nombre){
        nombreExcel = nombre;
        try {
            in = new FileInputStream(ServicioArchivos.carpetaRaiz.concat(File.separator).concat("temporales").concat(File.separator).concat(nombreExcel));
            libro = new XSSFWorkbook(in);
            //System.out.println("Se obtuvo el libro:"+ libro);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void obtenerWord(String nombre){
        nombreWord = nombre;
        try {
            in = new FileInputStream(ServicioArchivos.carpetaRaiz.concat(File.separator).concat("temporales").concat(File.separator).concat(nombreWord));
            word = new XWPFDocument(in);
            //System.out.println("Se obtuvo el libro:"+ libro);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void escribirLibro(String carpeta, String eje, String nombreHoja){
        try {
            base = ServicioArchivos.carpetaRaiz
                    .concat(carpeta).concat(File.separator)
                    .concat(eje).concat(File.separator);

            ServicioArchivos.addCarpetaRelativa(base);
            salida = nombreHoja;

            ServicioArchivos.eliminarArchivo(base.concat(salida));

            out = new FileOutputStream(new File(base.concat(salida)));
            libro.write(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void escribirWord(String carpeta, String eje, String nombreHoja){
        try {
            base = ServicioArchivos.carpetaRaiz
                    .concat(carpeta).concat(File.separator)
                    .concat(eje).concat(File.separator);

            ServicioArchivos.addCarpetaRelativa(base);
            salida = nombreHoja;

            ServicioArchivos.eliminarArchivo(base.concat(salida));

            out = new FileOutputStream(new File(base.concat(salida)));
            word.write(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GenerateExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String enviarLibro(){
        File file = new File(base.concat(salida));
        String ruta = "/sii2/media".concat(file.toURI().toString().split("archivos")[1]);
        return ruta;
    }
    
    public void abrirArchivo(String carpeta, String eje, String archivo){
        try {
            base = ServicioArchivos.carpetaRaiz
                    .concat(carpeta).concat(File.separator)
                    .concat(eje).concat(File.separator);
            File file = new File(base.concat(archivo));
            Runtime.getRuntime().exec("cmd /c start "+file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteFolder(String carpeta, String eje, String archivo) {
        base = ServicioArchivos.carpetaRaiz
                    .concat(carpeta).concat(File.separator)
                    .concat(eje).concat(File.separator);
        File file = new File(base.concat(archivo));
        if(!file.exists()) return;
        if(file.isFile()){
            file.delete();
        }
    }
//    
//    public static void main(String[] args){
//        GenerateExcel g = new GenerateExcel();
//        g.deleteFolder("resultados_test", "Grupo_14_8A_59", "Resultados_Test.xlsx");
//    }
    
    public void eliminarLibros(Integer numero, Integer hojas){
        for(int i = 1; i <= hojas; i++){
            libro.removeSheetAt(numero);
        }
    }

    public void escribirDatosExcel(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto, TestDiagnosticoAprendizaje test,String excel){
        XSSFRow nombre = libro.getSheet(excel).getRow(11);     XSSFCell celdaN = nombre.getCell(4);    celdaN.setCellValue(dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPaterno()+" "+dto.getPersonas().getApellidoMaterno());
        XSSFRow matricula = libro.getSheet(excel).getRow(11);     XSSFCell celdaM = matricula.getCell(13);    celdaM.setCellValue(dto.getAlumnos().getMatricula());
        XSSFRow carrea = libro.getSheet(excel).getRow(12);     XSSFCell celdaC = carrea.getCell(2);    celdaC.setCellValue(dto.getAreasUniversidad().getNombre());
        XSSFRow grupo = libro.getSheet(excel).getRow(12);     XSSFCell celdaG = grupo.getCell(10);    celdaG.setCellValue(dto.getGrado()+dto.getGrupo());
        XSSFRow fecha = libro.getSheet(excel).getRow(12);     XSSFCell celdaF = fecha.getCell(14);    celdaF.setCellValue(test.getFechaAplicacion());
        XSSFRow tutor = libro.getSheet(excel).getRow(55);     XSSFCell celdaT = tutor.getCell(3);    celdaT.setCellValue(dto.getTutor().getNombre());
        Integer[] datos = {
            test.getR1().intValue(), test.getR2().intValue(), test.getR3().intValue(), test.getR4().intValue(), test.getR5().intValue(), test.getR6().intValue(),
            test.getR7().intValue(), test.getR8().intValue(), test.getR9().intValue(), test.getR10().intValue(), test.getR11().intValue(), test.getR12().intValue(),
            test.getR13().intValue(), test.getR14().intValue(), test.getR15().intValue(), test.getR16().intValue(), test.getR17().intValue(), test.getR18().intValue(),
            test.getR19().intValue(), test.getR20().intValue(), test.getR21().intValue(), test.getR22().intValue(), test.getR23().intValue(), test.getR24().intValue(),
            test.getR25().intValue(), test.getR26().intValue(), test.getR27().intValue(), test.getR28().intValue(), test.getR29().intValue(), test.getR30().intValue(),
            test.getR31().intValue(), test.getR32().intValue(), test.getR33().intValue(), test.getR34().intValue(), test.getR35().intValue(), test.getR36().intValue()
        };
        
        XSSFRow fila;
        for (int i = 0; i < datos.length; i++) {
            fila = libro.getSheet(excel).getRow(18+i);
            XSSFCell celda = fila.getCell(8);
            celda.setCellValue(datos[i]);
        }
        
        Integer[] visual = {
            test.getR1().intValue(), test.getR5().intValue(), test.getR9().intValue(), test.getR10().intValue(), test.getR11().intValue(), test.getR16().intValue(), 
            test.getR17().intValue(), test.getR22().intValue(), test.getR26().intValue(), test.getR27().intValue(), test.getR32().intValue(), test.getR36().intValue()
        };
        
        Integer[] auditivo = {
            test.getR2().intValue(), test.getR3().intValue(), test.getR12().intValue(), test.getR13().intValue(), test.getR15().intValue(), test.getR19().intValue(), 
            test.getR20().intValue(), test.getR23().intValue(), test.getR24().intValue(), test.getR28().intValue(), test.getR29().intValue(), test.getR33().intValue()
        };
        
        Integer[] kinestesico = {
            test.getR4().intValue(), test.getR6().intValue(), test.getR7().intValue(), test.getR8().intValue(), test.getR14().intValue(), test.getR18().intValue(), 
            test.getR21().intValue(), test.getR25().intValue(), test.getR30().intValue(), test.getR31().intValue(), test.getR34().intValue(), test.getR35().intValue() 
        };
        
        List<Integer> listVisual = new ArrayList<>();
        List<Integer> listAuditivo = new ArrayList<>();
        List<Integer> listKinestesico = new ArrayList<>();
        
        listVisual.add(test.getR1().intValue());    listVisual.add(test.getR5().intValue());   listVisual.add(test.getR9().intValue());   listVisual.add(test.getR10().intValue());
        listVisual.add(test.getR11().intValue());   listVisual.add(test.getR16().intValue());  listVisual.add(test.getR17().intValue());  listVisual.add(test.getR22().intValue());
        listVisual.add(test.getR26().intValue());   listVisual.add(test.getR27().intValue());  listVisual.add(test.getR32().intValue());  listVisual.add(test.getR36().intValue());
        
        listAuditivo.add(test.getR2().intValue()); listAuditivo.add(test.getR3().intValue()); listAuditivo.add(test.getR12().intValue()); listAuditivo.add(test.getR13().intValue());
        listAuditivo.add(test.getR15().intValue()); listAuditivo.add(test.getR19().intValue()); listAuditivo.add(test.getR20().intValue()); listAuditivo.add(test.getR23().intValue());
        listAuditivo.add(test.getR24().intValue()); listAuditivo.add(test.getR28().intValue()); listAuditivo.add(test.getR29().intValue()); listAuditivo.add(test.getR33().intValue());
        
        listKinestesico.add(test.getR4().intValue());  listKinestesico.add(test.getR6().intValue());  listKinestesico.add(test.getR7().intValue());
        listKinestesico.add(test.getR8().intValue());  listKinestesico.add(test.getR14().intValue());  listKinestesico.add(test.getR18().intValue());
        listKinestesico.add(test.getR21().intValue());  listKinestesico.add(test.getR25().intValue());  listKinestesico.add(test.getR30().intValue());
        listKinestesico.add(test.getR31().intValue());  listKinestesico.add(test.getR34().intValue());  listKinestesico.add(test.getR35().intValue());
        
        for (int i = 0; i < visual.length; i++) {
            fila = libro.getSheet(excel).getRow(19+i);
            XSSFCell celda = fila.getCell(11);
            celda.setCellValue(visual[i]);
        }
        
        for (int i = 0; i < auditivo.length; i++) {
            fila = libro.getSheet(excel).getRow(19+i);
            XSSFCell celda = fila.getCell(13);
            celda.setCellValue(auditivo[i]);
        }
        
        for (int i = 0; i < kinestesico.length; i++) {
            fila = libro.getSheet(excel).getRow(19+i);
            XSSFCell celda = fila.getCell(15);
            celda.setCellValue(kinestesico[i]);
        }
        
        Integer sumaV = listVisual.stream().mapToInt(Integer::intValue).sum();
        Integer sumaA = listAuditivo.stream().mapToInt(Integer::intValue).sum();
        Integer sumaK = listKinestesico.stream().mapToInt(Integer::intValue).sum();
        Integer suma = sumaV + sumaA + sumaK;
        
        Double promedioV = (sumaV.doubleValue() / suma.doubleValue()) * 100;
        Double promedioA = (sumaA.doubleValue() / suma.doubleValue()) * 100;
        Double promedioK = (sumaK.doubleValue() / suma.doubleValue()) * 100;
        Double sumaPromedio = promedioV + promedioA + promedioK;
        
        XSSFRow totalV = libro.getSheet(excel).getRow(31);     XSSFCell celdaV = totalV.getCell(11);    celdaV.setCellValue(sumaV);
        XSSFRow totalA = libro.getSheet(excel).getRow(31);     XSSFCell celdaA = totalA.getCell(13);    celdaA.setCellValue(sumaA);
        XSSFRow totalK = libro.getSheet(excel).getRow(31);     XSSFCell celdaK = totalK.getCell(15);    celdaK.setCellValue(sumaK);
        
        XSSFRow totalV1 = libro.getSheet(excel).getRow(35);     XSSFCell celdaV1 = totalV1.getCell(14);    celdaV1.setCellValue(sumaV);
        XSSFRow totalA1 = libro.getSheet(excel).getRow(36);     XSSFCell celdaA1 = totalA1.getCell(14);    celdaA1.setCellValue(sumaA);
        XSSFRow totalK1 = libro.getSheet(excel).getRow(37);     XSSFCell celdaK1 = totalK1.getCell(14);    celdaK1.setCellValue(sumaK);
        XSSFRow total = libro.getSheet(excel).getRow(38);     XSSFCell celdaTotal = total.getCell(14);    celdaTotal.setCellValue(suma);
        XSSFRow totalPV = libro.getSheet(excel).getRow(35);     XSSFCell celdaPV = totalPV.getCell(13);    celdaPV.setCellValue(promedioV);
        XSSFRow totalPA = libro.getSheet(excel).getRow(36);     XSSFCell celdaPA = totalPA.getCell(13);    celdaPA.setCellValue(promedioA);
        XSSFRow totalPK = libro.getSheet(excel).getRow(37);     XSSFCell celdaPK = totalPK.getCell(13);    celdaPK.setCellValue(promedioK);
        XSSFRow totalProm = libro.getSheet(excel).getRow(38);     XSSFCell celdaProm = totalProm.getCell(13);    celdaProm.setCellValue(sumaPromedio);
        
    }
    
    public void escribirDatosExcel(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto, TestDiagnosticoAprendizaje test,String excel){
        XSSFRow nombre = libro.getSheet(excel).getRow(11);     XSSFCell celdaN = nombre.getCell(4);    celdaN.setCellValue(dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPat()+" "+dto.getPersonas().getApellidoMat());
        XSSFRow matricula = libro.getSheet(excel).getRow(11);     XSSFCell celdaM = matricula.getCell(13);    celdaM.setCellValue(dto.getAlumnos().getMatricula());
        XSSFRow carrea = libro.getSheet(excel).getRow(12);     XSSFCell celdaC = carrea.getCell(2);    celdaC.setCellValue(dto.getCarrerasCgut().getNombre());
        XSSFRow grupo = libro.getSheet(excel).getRow(12);     XSSFCell celdaG = grupo.getCell(10);    celdaG.setCellValue(dto.getGrado()+dto.getGrupo());
        XSSFRow fecha = libro.getSheet(excel).getRow(12);     XSSFCell celdaF = fecha.getCell(14);    celdaF.setCellValue(test.getFechaAplicacion());
        XSSFRow tutor = libro.getSheet(excel).getRow(55);     XSSFCell celdaT = tutor.getCell(3);    celdaT.setCellValue(dto.getTutor().getNombre());
        
        Integer[] datos = {
            test.getR1().intValue(), test.getR2().intValue(), test.getR3().intValue(), test.getR4().intValue(), test.getR5().intValue(), test.getR6().intValue(),
            test.getR7().intValue(), test.getR8().intValue(), test.getR9().intValue(), test.getR10().intValue(), test.getR11().intValue(), test.getR12().intValue(),
            test.getR13().intValue(), test.getR14().intValue(), test.getR15().intValue(), test.getR16().intValue(), test.getR17().intValue(), test.getR18().intValue(),
            test.getR19().intValue(), test.getR20().intValue(), test.getR21().intValue(), test.getR22().intValue(), test.getR23().intValue(), test.getR24().intValue(),
            test.getR25().intValue(), test.getR26().intValue(), test.getR27().intValue(), test.getR28().intValue(), test.getR29().intValue(), test.getR30().intValue(),
            test.getR31().intValue(), test.getR32().intValue(), test.getR33().intValue(), test.getR34().intValue(), test.getR35().intValue(), test.getR36().intValue()
        };
        
        XSSFRow fila;
        for (int i = 0; i < datos.length; i++) {
            fila = libro.getSheet(excel).getRow(18+i);
            XSSFCell celda = fila.getCell(8);
            celda.setCellValue(datos[i]);
        }
        
        Integer[] visual = {
            test.getR1().intValue(), test.getR5().intValue(), test.getR9().intValue(), test.getR10().intValue(), test.getR11().intValue(), test.getR16().intValue(), 
            test.getR17().intValue(), test.getR22().intValue(), test.getR26().intValue(), test.getR27().intValue(), test.getR32().intValue(), test.getR36().intValue()
        };
        
        Integer[] auditivo = {
            test.getR2().intValue(), test.getR3().intValue(), test.getR12().intValue(), test.getR13().intValue(), test.getR15().intValue(), test.getR19().intValue(), 
            test.getR20().intValue(), test.getR23().intValue(), test.getR24().intValue(), test.getR28().intValue(), test.getR29().intValue(), test.getR33().intValue()
        };
        
        Integer[] kinestesico = {
            test.getR4().intValue(), test.getR6().intValue(), test.getR7().intValue(), test.getR8().intValue(), test.getR14().intValue(), test.getR18().intValue(), 
            test.getR21().intValue(), test.getR25().intValue(), test.getR30().intValue(), test.getR31().intValue(), test.getR34().intValue(), test.getR35().intValue() 
        };
        
        List<Integer> listVisual = new ArrayList<>();
        List<Integer> listAuditivo = new ArrayList<>();
        List<Integer> listKinestesico = new ArrayList<>();
        
        listVisual.add(test.getR1().intValue());    listVisual.add(test.getR5().intValue());   listVisual.add(test.getR9().intValue());   listVisual.add(test.getR10().intValue());
        listVisual.add(test.getR11().intValue());   listVisual.add(test.getR16().intValue());  listVisual.add(test.getR17().intValue());  listVisual.add(test.getR22().intValue());
        listVisual.add(test.getR26().intValue());   listVisual.add(test.getR27().intValue());  listVisual.add(test.getR32().intValue());  listVisual.add(test.getR36().intValue());
        
        listAuditivo.add(test.getR2().intValue()); listAuditivo.add(test.getR3().intValue()); listAuditivo.add(test.getR12().intValue()); listAuditivo.add(test.getR13().intValue());
        listAuditivo.add(test.getR15().intValue()); listAuditivo.add(test.getR19().intValue()); listAuditivo.add(test.getR20().intValue()); listAuditivo.add(test.getR23().intValue());
        listAuditivo.add(test.getR24().intValue()); listAuditivo.add(test.getR28().intValue()); listAuditivo.add(test.getR29().intValue()); listAuditivo.add(test.getR33().intValue());
        
        listKinestesico.add(test.getR4().intValue());  listKinestesico.add(test.getR6().intValue());  listKinestesico.add(test.getR7().intValue());
        listKinestesico.add(test.getR8().intValue());  listKinestesico.add(test.getR14().intValue());  listKinestesico.add(test.getR18().intValue());
        listKinestesico.add(test.getR21().intValue());  listKinestesico.add(test.getR25().intValue());  listKinestesico.add(test.getR30().intValue());
        listKinestesico.add(test.getR31().intValue());  listKinestesico.add(test.getR34().intValue());  listKinestesico.add(test.getR35().intValue());
        
        for (int i = 0; i < visual.length; i++) {
            fila = libro.getSheet(excel).getRow(19+i);
            XSSFCell celda = fila.getCell(11);
            celda.setCellValue(visual[i]);
        }
        
        for (int i = 0; i < auditivo.length; i++) {
            fila = libro.getSheet(excel).getRow(19+i);
            XSSFCell celda = fila.getCell(13);
            celda.setCellValue(auditivo[i]);
        }
        
        for (int i = 0; i < kinestesico.length; i++) {
            fila = libro.getSheet(excel).getRow(19+i);
            XSSFCell celda = fila.getCell(15);
            celda.setCellValue(kinestesico[i]);
        }
        
        Integer sumaV = listVisual.stream().mapToInt(Integer::intValue).sum();
        Integer sumaA = listAuditivo.stream().mapToInt(Integer::intValue).sum();
        Integer sumaK = listKinestesico.stream().mapToInt(Integer::intValue).sum();
        Integer suma = sumaV + sumaA + sumaK;
        
        Double promedioV = (sumaV.doubleValue() / suma.doubleValue()) * 100;
        Double promedioA = (sumaA.doubleValue() / suma.doubleValue()) * 100;
        Double promedioK = (sumaK.doubleValue() / suma.doubleValue()) * 100;
        Double sumaPromedio = promedioV + promedioA + promedioK;
        
        XSSFRow totalV = libro.getSheet(excel).getRow(31);     XSSFCell celdaV = totalV.getCell(11);    celdaV.setCellValue(sumaV);
        XSSFRow totalA = libro.getSheet(excel).getRow(31);     XSSFCell celdaA = totalA.getCell(13);    celdaA.setCellValue(sumaA);
        XSSFRow totalK = libro.getSheet(excel).getRow(31);     XSSFCell celdaK = totalK.getCell(15);    celdaK.setCellValue(sumaK);
        
        XSSFRow totalV1 = libro.getSheet(excel).getRow(35);     XSSFCell celdaV1 = totalV1.getCell(14);    celdaV1.setCellValue(sumaV);
        XSSFRow totalA1 = libro.getSheet(excel).getRow(36);     XSSFCell celdaA1 = totalA1.getCell(14);    celdaA1.setCellValue(sumaA);
        XSSFRow totalK1 = libro.getSheet(excel).getRow(37);     XSSFCell celdaK1 = totalK1.getCell(14);    celdaK1.setCellValue(sumaK);
        XSSFRow total = libro.getSheet(excel).getRow(38);     XSSFCell celdaTotal = total.getCell(14);    celdaTotal.setCellValue(suma);
        XSSFRow totalPV = libro.getSheet(excel).getRow(35);     XSSFCell celdaPV = totalPV.getCell(13);    celdaPV.setCellValue(promedioV);
        XSSFRow totalPA = libro.getSheet(excel).getRow(36);     XSSFCell celdaPA = totalPA.getCell(13);    celdaPA.setCellValue(promedioA);
        XSSFRow totalPK = libro.getSheet(excel).getRow(37);     XSSFCell celdaPK = totalPK.getCell(13);    celdaPK.setCellValue(promedioK);
        XSSFRow totalProm = libro.getSheet(excel).getRow(38);     XSSFCell celdaProm = totalProm.getCell(13);    celdaProm.setCellValue(sumaPromedio);
        
    }
    
    public void escribirDatosExcelHoja0(List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> lista, String excel){
        
        List<DtoAlumnosEncuesta.DtoResultadoTestEstudiante> listDto;
        
        XSSFRow totalParticipantes = libro.getSheet(excel).getRow(0);     XSSFCell celdaTP = totalParticipantes.getCell(2);    celdaTP.setCellValue(lista.size());
        XSSFSheet hoja = libro.getSheet(excel);
        XSSFRow fila;
        
        listDto = lista.stream().map(dto -> pack(dto)).collect(Collectors.toList());
        
        for(int i = 0; i < lista.size(); i++){
            DtoAlumnosEncuesta.DtoResultadoTestEstudiante dto = listDto.get(i);
            fila = libro.getSheet(excel).getRow(2+i);
            XSSFCell celdaV = fila.getCell(0); celdaV.setCellValue(dto.getListaV().stream().mapToInt(Integer::intValue).sum());
            XSSFCell celdaA = fila.getCell(1); celdaA.setCellValue(dto.getListaA().stream().mapToInt(Integer::intValue).sum());
            XSSFCell celdaK = fila.getCell(2); celdaK.setCellValue(dto.getListaK().stream().mapToInt(Integer::intValue).sum());
        }
        
        Integer totalV = listDto.stream().map(dto -> dto.getListaV().stream().mapToInt(Integer::intValue).sum()).mapToInt(Integer::intValue).sum();
        Integer totalA = listDto.stream().map(dto -> dto.getListaA().stream().mapToInt(Integer::intValue).sum()).mapToInt(Integer::intValue).sum();
        Integer totalK = listDto.stream().map(dto -> dto.getListaK().stream().mapToInt(Integer::intValue).sum()).mapToInt(Integer::intValue).sum();
        
        XSSFRow porcentajeV = libro.getSheet(excel).getRow(43);     XSSFCell celdaPV = porcentajeV.getCell(0);    celdaPV.setCellValue((double) totalV.doubleValue() / lista.size());
        XSSFRow porcentajeA = libro.getSheet(excel).getRow(43);     XSSFCell celdaPA = porcentajeA.getCell(1);    celdaPA.setCellValue((double) totalA.doubleValue() / lista.size());
        XSSFRow porcentajeK = libro.getSheet(excel).getRow(43);     XSSFCell celdaPK = porcentajeK.getCell(2);    celdaPK.setCellValue((double) totalK.doubleValue() / lista.size());
    }
    
    public void escribirDatosExcelHoja0S(List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> lista, String excel){
        
        List<DtoAlumnosEncuesta.DtoResultadoTestEstudiante> listDto;
        
        XSSFRow totalParticipantes = libro.getSheet(excel).getRow(0);     XSSFCell celdaTP = totalParticipantes.getCell(2);    celdaTP.setCellValue(lista.size());
        
        XSSFRow fila;
        
        listDto = lista.stream().map(dto -> pack(dto)).collect(Collectors.toList());
        
        for(int i = 0; i < lista.size(); i++){
            DtoAlumnosEncuesta.DtoResultadoTestEstudiante dto = listDto.get(i);
            fila = libro.getSheet(excel).getRow(2+i);
            XSSFCell celdaV = fila.getCell(0); celdaV.setCellValue(dto.getListaV().stream().mapToInt(Integer::intValue).sum());
            XSSFCell celdaA = fila.getCell(1); celdaA.setCellValue(dto.getListaA().stream().mapToInt(Integer::intValue).sum());
            XSSFCell celdaK = fila.getCell(2); celdaK.setCellValue(dto.getListaK().stream().mapToInt(Integer::intValue).sum());
        }
        
        Integer totalV = listDto.stream().map(dto -> dto.getListaV().stream().mapToInt(Integer::intValue).sum()).mapToInt(Integer::intValue).sum();
        Integer totalA = listDto.stream().map(dto -> dto.getListaA().stream().mapToInt(Integer::intValue).sum()).mapToInt(Integer::intValue).sum();
        Integer totalK = listDto.stream().map(dto -> dto.getListaK().stream().mapToInt(Integer::intValue).sum()).mapToInt(Integer::intValue).sum();
        
        XSSFRow porcentajeV = libro.getSheet(excel).getRow(43);     XSSFCell celdaPV = porcentajeV.getCell(0);    celdaPV.setCellValue((double) totalV.doubleValue() / lista.size());
        XSSFRow porcentajeA = libro.getSheet(excel).getRow(43);     XSSFCell celdaPA = porcentajeA.getCell(1);    celdaPA.setCellValue((double) totalA.doubleValue() / lista.size());
        XSSFRow porcentajeK = libro.getSheet(excel).getRow(43);     XSSFCell celdaPK = porcentajeK.getCell(2);    celdaPK.setCellValue((double) totalK.doubleValue() / lista.size());
    }
    
    public DtoAlumnosEncuesta.DtoResultadoTestEstudiante pack(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoA){
        List<Integer> listVisual = new ArrayList<>();
        List<Integer> listAuditivo = new ArrayList<>();
        List<Integer> listKinestesico = new ArrayList<>();
        TestDiagnosticoAprendizaje test = ejbSTDA.obtenerTest(dtoA).getValor();
        listVisual.add(test.getR1().intValue());    listVisual.add(test.getR5().intValue());   listVisual.add(test.getR9().intValue());   listVisual.add(test.getR10().intValue());
        listVisual.add(test.getR11().intValue());   listVisual.add(test.getR16().intValue());  listVisual.add(test.getR17().intValue());  listVisual.add(test.getR22().intValue());
        listVisual.add(test.getR26().intValue());   listVisual.add(test.getR27().intValue());  listVisual.add(test.getR32().intValue());  listVisual.add(test.getR36().intValue());
        
        listAuditivo.add(test.getR2().intValue()); listAuditivo.add(test.getR3().intValue()); listAuditivo.add(test.getR12().intValue()); listAuditivo.add(test.getR13().intValue());
        listAuditivo.add(test.getR15().intValue()); listAuditivo.add(test.getR19().intValue()); listAuditivo.add(test.getR20().intValue()); listAuditivo.add(test.getR23().intValue());
        listAuditivo.add(test.getR24().intValue()); listAuditivo.add(test.getR28().intValue()); listAuditivo.add(test.getR29().intValue()); listAuditivo.add(test.getR33().intValue());
        
        listKinestesico.add(test.getR4().intValue());  listKinestesico.add(test.getR6().intValue());  listKinestesico.add(test.getR7().intValue());
        listKinestesico.add(test.getR8().intValue());  listKinestesico.add(test.getR14().intValue());  listKinestesico.add(test.getR18().intValue());
        listKinestesico.add(test.getR21().intValue());  listKinestesico.add(test.getR25().intValue());  listKinestesico.add(test.getR30().intValue());
        listKinestesico.add(test.getR31().intValue());  listKinestesico.add(test.getR34().intValue());  listKinestesico.add(test.getR35().intValue());
        DtoAlumnosEncuesta.DtoResultadoTestEstudiante dto = new DtoAlumnosEncuesta.DtoResultadoTestEstudiante(test.getTestDiagnosticoAprendizajePK().getEvaluador(), listVisual, listAuditivo, listKinestesico);
        return dto;
    }
    
    public DtoAlumnosEncuesta.DtoResultadoTestEstudiante pack(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dtoA){
        List<Integer> listVisual = new ArrayList<>();
        List<Integer> listAuditivo = new ArrayList<>();
        List<Integer> listKinestesico = new ArrayList<>();
        TestDiagnosticoAprendizaje test = ejbSTDA.obtenerTest(dtoA).getValor();
        listVisual.add(test.getR1().intValue());    listVisual.add(test.getR5().intValue());   listVisual.add(test.getR9().intValue());   listVisual.add(test.getR10().intValue());
        listVisual.add(test.getR11().intValue());   listVisual.add(test.getR16().intValue());  listVisual.add(test.getR17().intValue());  listVisual.add(test.getR22().intValue());
        listVisual.add(test.getR26().intValue());   listVisual.add(test.getR27().intValue());  listVisual.add(test.getR32().intValue());  listVisual.add(test.getR36().intValue());
        
        listAuditivo.add(test.getR2().intValue()); listAuditivo.add(test.getR3().intValue()); listAuditivo.add(test.getR12().intValue()); listAuditivo.add(test.getR13().intValue());
        listAuditivo.add(test.getR15().intValue()); listAuditivo.add(test.getR19().intValue()); listAuditivo.add(test.getR20().intValue()); listAuditivo.add(test.getR23().intValue());
        listAuditivo.add(test.getR24().intValue()); listAuditivo.add(test.getR28().intValue()); listAuditivo.add(test.getR29().intValue()); listAuditivo.add(test.getR33().intValue());
        
        listKinestesico.add(test.getR4().intValue());  listKinestesico.add(test.getR6().intValue());  listKinestesico.add(test.getR7().intValue());
        listKinestesico.add(test.getR8().intValue());  listKinestesico.add(test.getR14().intValue());  listKinestesico.add(test.getR18().intValue());
        listKinestesico.add(test.getR21().intValue());  listKinestesico.add(test.getR25().intValue());  listKinestesico.add(test.getR30().intValue());
        listKinestesico.add(test.getR31().intValue());  listKinestesico.add(test.getR34().intValue());  listKinestesico.add(test.getR35().intValue());
        DtoAlumnosEncuesta.DtoResultadoTestEstudiante dto = new DtoAlumnosEncuesta.DtoResultadoTestEstudiante(test.getTestDiagnosticoAprendizajePK().getEvaluador(), listVisual, listAuditivo, listKinestesico);
        return dto;
    }
    
    public void escribirDatosPrincipalesWord(List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDto, Integer noEstudiantes, String fecha){
        String tutor = listaDto.get(0).getTutor().getNombre();
        String carrera = listaDto.get(0).getAreasUniversidad().getNombre();
        String grupo = listaDto.get(0).getGrupo();
        String cuatrimestre = String.valueOf(listaDto.get(0).getGrado());
        
        XWPFParagraph paragraphT = word.createParagraph(); XWPFRun runT = paragraphT.createRun(); runT.setBold(true); runT.setText("Nombre del Profesor: "+ tutor);
        XWPFParagraph paragraphC = word.createParagraph(); XWPFRun runC = paragraphC.createRun(); runC.setBold(true); runC.setText("Carrera: "+ carrera);
        XWPFParagraph paragraphG = word.createParagraph(); XWPFRun runG = paragraphG.createRun(); runG.setBold(true); runG.setText("Grupo: "+ grupo);
        XWPFParagraph paragraphC1 = word.createParagraph(); XWPFRun runC1 = paragraphC1.createRun(); runC1.setBold(true); runC1.setText("Cuatrimestre: "+ cuatrimestre);
        XWPFParagraph paragraphN = word.createParagraph(); XWPFRun runN = paragraphN.createRun(); runN.setBold(true); runN.setText("Número de alumnos atendidos: "+ noEstudiantes);
        XWPFParagraph paragraphF = word.createParagraph(); XWPFRun runF = paragraphF.createRun(); runF.setBold(true); runF.setText("Fecha: "+ fecha);
        
        XWPFParagraph parrafoVacio = word.createParagraph(); XWPFRun runVacio = parrafoVacio.createRun(); runVacio.setBold(true); runVacio.setText("");
        
        XWPFParagraph subtitulo1 = word.createParagraph(); XWPFRun runS1 = subtitulo1.createRun(); runS1.setBold(true); 
        runS1.setText("Análisis de la tendencia general de los estilos de aprendizaje del grupo");
        
        XWPFParagraph parrafo1 = word.createParagraph(); parrafo1.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP1 = parrafo1.createRun();
        runP1.setText("Con la finalidad de fundamentar las acciones de acompañamiento pertinentes, se estudia el estado de desarrollo personal y potencial de aprendizaje,"
                + " mediante la aplicación del test para determinar el canal de aprendizaje de preferencia a nivel individual y grupal, obteniendo los siguientes resultados:");
    }
    
    public void escribirDatosPrincipalesWordS(List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaDto, Integer noEstudiantes, String fecha){
        String tutor = listaDto.get(0).getTutor().getNombre()+" "+ listaDto.get(0).getTutor().getApellidoPat()+" "+listaDto.get(0).getTutor().getApellidoMat();
        String carrera = "Lic. en "+listaDto.get(0).getCarrerasCgut().getNombre();
        String grupo = listaDto.get(0).getGrupo();
        String cuatrimestre = String.valueOf(listaDto.get(0).getGrado());
        
        XWPFParagraph paragraphT = word.createParagraph(); XWPFRun runT = paragraphT.createRun(); runT.setBold(true); runT.setText("Nombre del Profesor: "+ tutor);
        XWPFParagraph paragraphC = word.createParagraph(); XWPFRun runC = paragraphC.createRun(); runC.setBold(true); runC.setText("Carrera: "+ carrera);
        XWPFParagraph paragraphG = word.createParagraph(); XWPFRun runG = paragraphG.createRun(); runG.setBold(true); runG.setText("Grupo: "+ grupo);
        XWPFParagraph paragraphC1 = word.createParagraph(); XWPFRun runC1 = paragraphC1.createRun(); runC1.setBold(true); runC1.setText("Cuatrimestre: "+ cuatrimestre);
        XWPFParagraph paragraphN = word.createParagraph(); XWPFRun runN = paragraphN.createRun(); runN.setBold(true); runN.setText("Número de alumnos atendidos: "+ noEstudiantes);
        XWPFParagraph paragraphF = word.createParagraph(); XWPFRun runF = paragraphF.createRun(); runF.setBold(true); runF.setText("Fecha: "+ fecha);
        
        XWPFParagraph parrafoVacio = word.createParagraph(); XWPFRun runVacio = parrafoVacio.createRun(); runVacio.setBold(true); runVacio.setText("");
        
        XWPFParagraph subtitulo1 = word.createParagraph(); XWPFRun runS1 = subtitulo1.createRun(); runS1.setBold(true); 
        runS1.setText("Análisis de la tendencia general de los estilos de aprendizaje del grupo");
        
        XWPFParagraph parrafo1 = word.createParagraph(); parrafo1.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP1 = parrafo1.createRun();
        runP1.setText("Con la finalidad de fundamentar las acciones de acompañamiento pertinentes, se estudia el estado de desarrollo personal y potencial de aprendizaje,"
                + " mediante la aplicación del test para determinar el canal de aprendizaje de preferencia a nivel individual y grupal, obteniendo los siguientes resultados:");
    }
    
    public void agregarTablaWord(List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> lista) throws Exception{
        List<DtoAlumnosEncuesta.DtoResultadoTestEstudiante> listDto;
        List<DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje> listDtoR;
         
        XWPFParagraph parrafoVacio = word.createParagraph(); XWPFRun runVacio = parrafoVacio.createRun(); runVacio.setBold(true); runVacio.setText("");
        
        listDto = lista.stream().map(dto -> pack(dto)).collect(Collectors.toList());
        listDtoR = listDto.stream().map(dtoR -> packDto(dtoR)).collect(Collectors.toList());
        
        int cols = 3;
        int rows = 2 + listDto.size();
  
        
        //create table
      XWPFTable table = word.createTable();
      table.setWidth(100);
      
		
      //create first row
      XWPFTableRow tableRowOne = table.getRow(0); 
      tableRowOne.getCell(0).setText("");
      tableRowOne.addNewTableCell().setText("GRUPO");
      tableRowOne.addNewTableCell().setText("");
      tableRowOne.addNewTableCell().setText("");
      tableRowOne.addNewTableCell().setText("Total (%)");
      tableRowOne.addNewTableCell().setText("");
      
		
      //create second row
      XWPFTableRow tableRowTwo = table.createRow();
      tableRowTwo.getCell(0).setText("No.");
      tableRowTwo.getCell(1).setText("Matrícula");
      tableRowTwo.getCell(2).setText("Nombre");
      tableRowTwo.getCell(3).setText("Visual");
      tableRowTwo.getCell(4).setText("Auditivo");
      tableRowTwo.getCell(5).setText("Kinestésico");
      df.setRoundingMode(RoundingMode.HALF_UP);
		
        for(int f = 0; f < listDto.size(); f++){
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = lista.get(f);
            DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje dtoRes = listDtoR.stream().filter(d -> d.getMatricula().equals(dto.getAlumnos().getMatricula())).findFirst().get();
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText(String.valueOf(f + 1));
            tableRow.getCell(1).setText(String.valueOf(dto.getAlumnos().getMatricula()));
            tableRow.getCell(2).setText(dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPaterno()+" "+dto.getPersonas().getApellidoMaterno());
            tableRow.getCell(3).setText(String.valueOf(df.format(dtoRes.getPV())));
            tableRow.getCell(4).setText(String.valueOf(df.format(dtoRes.getPA())));
            tableRow.getCell(5).setText(String.valueOf(df.format(dtoRes.getPK())));
        }
        
        Double totalV = listDtoR.stream().mapToDouble(DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje::getPV).average().getAsDouble();
        Double totalA = listDtoR.stream().mapToDouble(DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje::getPA).average().getAsDouble();
        Double totalK = listDtoR.stream().mapToDouble(DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje::getPK).average().getAsDouble();
        
        
        XWPFTableRow tableRowP = table.createRow();
        tableRowP.getCell(0).setText("");
        tableRowP.getCell(1).setText("");
        tableRowP.getCell(2).setText("PROMEDIO");
        tableRowP.getCell(3).setText(String.valueOf(df.format(totalV)));
        tableRowP.getCell(4).setText(String.valueOf(df.format(totalA)));
        tableRowP.getCell(5).setText(String.valueOf(df.format(totalK)));
        
        XWPFParagraph parrafoVacio1 = word.createParagraph(); XWPFRun runVacio1 = parrafoVacio1.createRun(); runVacio1.setBold(true); runVacio1.setText("");
        
        XWPFParagraph subtitulo2 = word.createParagraph(); XWPFRun runS2 = subtitulo2.createRun(); runS2.setBold(true); 
        runS2.setText("Resumen de estilos de aprendizaje");
        
        XWPFParagraph parrafoVacio12 = word.createParagraph(); XWPFRun runVacio12 = parrafoVacio12.createRun(); runVacio12.setBold(true); runVacio12.setText("");
        
        XWPFParagraph parrafo2 = word.createParagraph(); parrafo2.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP2 = parrafo2.createRun();
        runP2.setText("Como se observa en los datos obtenidos, el grupo "+lista.get(0).getGrado()+" '"+lista.get(0).getGrupo()+"' de la carrera de "+lista.get(0).getAreasUniversidad().getNombre()+", "
                + "tiende a ser mayormente \"visual\" obteniendo el "+String.valueOf(df.format(totalV))+"%, "
                + "seguido del estilo de aprendizaje \"kinestésico\", con un "+String.valueOf(df.format(totalK))+"% y \"auditivo\" con un "+String.valueOf(df.format(totalA))+"%.");
        
        XWPFParagraph subtitulo3 = word.createParagraph(); XWPFRun runS3 = subtitulo3.createRun(); runS3.setBold(true); 
        runS3.setText("Estrategias, métodos y/o herramientas acordes al tipo de aprendizaje");
        
        XWPFParagraph parrafoVacio2 = word.createParagraph(); XWPFRun runVacio2 = parrafoVacio2.createRun(); runVacio2.setBold(true); runVacio2.setText("");
        
        XWPFParagraph subtitulo4 = word.createParagraph(); XWPFRun runS4 = subtitulo4.createRun(); runS4.setBold(true); 
        runS4.setText("1. Visual");
        
        XWPFParagraph parrafoVacio3 = word.createParagraph(); XWPFRun runVacio3 = parrafoVacio3.createRun(); runVacio3.setBold(true); runVacio3.setText("");
        
        XWPFParagraph parrafo3 = word.createParagraph(); parrafo3.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP3 = parrafo3.createRun();
        runP3.setText("Definición. Es un método de enseñanza que utiliza un conjunto de organizadores gráficos tanto para representar información, "
                + "como para trabajar con ideas y conceptos, que al utilizarlos ayudan a los estudiantes a pensar y a aprender más efectivamente.");
        
        XWPFParagraph parrafoVacio4 = word.createParagraph(); XWPFRun runVacio4 = parrafoVacio4.createRun(); runVacio4.setBold(true); runVacio4.setText("");
        
        XWPFParagraph subtitulo5 = word.createParagraph(); XWPFRun runS5 = subtitulo5.createRun(); runS5.setBold(true); 
        runS5.setText("2. Kinestésico");
        
        XWPFParagraph parrafoVacio5 = word.createParagraph(); XWPFRun runVacio5 = parrafoVacio5.createRun(); runVacio5.setBold(true); runVacio5.setText("");
        
        XWPFParagraph parrafo4 = word.createParagraph(); parrafo4.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP4 = parrafo4.createRun();
        runP4.setText("Definición. Se trata del aprendizaje relacionado con sensaciones y movimientos. En otras palabras, es lo que ocurre cuando se aprende "
                + "más fácilmente al movernos y tocar las cosas, como cuando caminamos al recitar información o hacemos un experimento manipulando instrumentos "
                + "de laboratorio. Este sistema es más lento que los otros dos, pero tiende a generar un aprendizaje más profundo y difícil de olvidar.");
        
        XWPFParagraph parrafoVacio6 = word.createParagraph(); XWPFRun runVacio6 = parrafoVacio6.createRun(); runVacio6.setBold(true); runVacio6.setText("");
        
        XWPFParagraph subtitulo6 = word.createParagraph(); XWPFRun runS6 = subtitulo6.createRun(); runS6.setBold(true); 
        runS6.setText("3. Auditivo");
        
        XWPFParagraph parrafoVacio7 = word.createParagraph(); XWPFRun runVacio7 = parrafoVacio7.createRun(); runVacio7.setBold(true); runVacio7.setText("");
        
        XWPFParagraph parrafo5 = word.createParagraph(); parrafo5.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP5 = parrafo5.createRun();
        runP5.setText("Definición. Es un método de enseñanza que se dirige a las estudiantes cuyo estilo de aprendizaje se orienta más hacia la asimilación "
                + "de la información a través del oído y no por la vista. Si bien la gran mayoría de la gente tiende a ser principalmente visuales en la forma"
                + " de relacionarse con el mundo alrededor de ellos, la estimulación de audio se emplea a menudo como un medio secundario de encontrar y absorber conocimientos.");
        
        XWPFParagraph parrafoVacio8 = word.createParagraph(); XWPFRun runVacio8 = parrafoVacio8.createRun(); runVacio8.setBold(true); runVacio8.setText("");
        XWPFParagraph parrafoVacio9 = word.createParagraph(); XWPFRun runVacio9 = parrafoVacio9.createRun(); runVacio9.setBold(true); runVacio9.setText("");
        
        XWPFParagraph testATT = word.createParagraph(); testATT.setAlignment(ParagraphAlignment.CENTER); XWPFRun runATT = testATT.createRun(); runATT.setBold(true); 
        runATT.setText("ATENTAMENTE");
        
        XWPFParagraph parrafoVacio10 = word.createParagraph(); XWPFRun runVacio10 = parrafoVacio10.createRun(); runVacio10.setBold(true); runVacio10.setText("");
        XWPFParagraph parrafoVacio11 = word.createParagraph(); XWPFRun runVacio11 = parrafoVacio11.createRun(); runVacio11.setBold(true); runVacio11.setText("");
        
        XWPFParagraph line = word.createParagraph(); line.setAlignment(ParagraphAlignment.CENTER); XWPFRun runline = line.createRun(); runline.setBold(true); 
        runline.setText("_____________________________");
        XWPFParagraph tTutor = word.createParagraph(); tTutor.setAlignment(ParagraphAlignment.CENTER); XWPFRun runt = tTutor.createRun(); runt.setBold(true); 
        runt.setText("Tutor");
        
    }
    
    public void agregarTablaWordS(List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> lista) throws Exception{
        List<DtoAlumnosEncuesta.DtoResultadoTestEstudiante> listDto;
        List<DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje> listDtoR;
         
        XWPFParagraph parrafoVacio = word.createParagraph(); XWPFRun runVacio = parrafoVacio.createRun(); runVacio.setBold(true); runVacio.setText("");
        
        listDto = lista.stream().map(dto -> pack(dto)).collect(Collectors.toList());
        listDtoR = listDto.stream().map(dtoR -> packDto(dtoR)).collect(Collectors.toList());
        
        int cols = 3;
        int rows = 2 + listDto.size();
  
        
        //create table
      XWPFTable table = word.createTable();
      table.setWidth(100);
      
		
      //create first row
      XWPFTableRow tableRowOne = table.getRow(0); 
      tableRowOne.getCell(0).setText("");
      tableRowOne.addNewTableCell().setText("GRUPO");
      tableRowOne.addNewTableCell().setText("");
      tableRowOne.addNewTableCell().setText("");
      tableRowOne.addNewTableCell().setText("Total (%)");
      tableRowOne.addNewTableCell().setText("");
      
		
      //create second row
      XWPFTableRow tableRowTwo = table.createRow();
      tableRowTwo.getCell(0).setText("No.");
      tableRowTwo.getCell(1).setText("Matrícula");
      tableRowTwo.getCell(2).setText("Nombre");
      tableRowTwo.getCell(3).setText("Visual");
      tableRowTwo.getCell(4).setText("Auditivo");
      tableRowTwo.getCell(5).setText("Kinestésico");
      df.setRoundingMode(RoundingMode.HALF_UP);
		
        for(int f = 0; f < listDto.size(); f++){
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = lista.get(f);
            DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje dtoRes = listDtoR.stream().filter(d -> d.getMatricula().equals(Integer.parseInt(dto.getAlumnos().getMatricula()))).findFirst().get();
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText(String.valueOf(f + 1));
            tableRow.getCell(1).setText(String.valueOf(dto.getAlumnos().getMatricula()));
            tableRow.getCell(2).setText(dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPat()+" "+dto.getPersonas().getApellidoMat());
            tableRow.getCell(3).setText(String.valueOf(df.format(dtoRes.getPV())));
            tableRow.getCell(4).setText(String.valueOf(df.format(dtoRes.getPA())));
            tableRow.getCell(5).setText(String.valueOf(df.format(dtoRes.getPK())));
        }
        
        Double totalV = listDtoR.stream().mapToDouble(DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje::getPV).average().getAsDouble();
        Double totalA = listDtoR.stream().mapToDouble(DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje::getPA).average().getAsDouble();
        Double totalK = listDtoR.stream().mapToDouble(DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje::getPK).average().getAsDouble();
        
        
        XWPFTableRow tableRowP = table.createRow();
        tableRowP.getCell(0).setText("");
        tableRowP.getCell(1).setText("");
        tableRowP.getCell(2).setText("PROMEDIO");
        tableRowP.getCell(3).setText(String.valueOf(df.format(totalV)));
        tableRowP.getCell(4).setText(String.valueOf(df.format(totalA)));
        tableRowP.getCell(5).setText(String.valueOf(df.format(totalK)));
        
        XWPFParagraph parrafoVacio1 = word.createParagraph(); XWPFRun runVacio1 = parrafoVacio1.createRun(); runVacio1.setBold(true); runVacio1.setText("");
        
        XWPFParagraph subtitulo2 = word.createParagraph(); XWPFRun runS2 = subtitulo2.createRun(); runS2.setBold(true); 
        runS2.setText("Resumen de estilos de aprendizaje");
        
        XWPFParagraph parrafoVacio12 = word.createParagraph(); XWPFRun runVacio12 = parrafoVacio12.createRun(); runVacio12.setBold(true); runVacio12.setText("");
        
        XWPFParagraph parrafo2 = word.createParagraph(); parrafo2.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP2 = parrafo2.createRun();
        runP2.setText("Como se observa en los datos obtenidos, el grupo "+lista.get(0).getGrado()+" '"+lista.get(0).getGrupo()+"' de la carrera de Lic. en "+lista.get(0).getCarrerasCgut().getNombre()+", "
                + "tiende a ser mayormente \"visual\" obteniendo el "+String.valueOf(df.format(totalV))+"%, "
                + "seguido del estilo de aprendizaje \"kinestésico\", con un "+String.valueOf(df.format(totalK))+"% y \"auditivo\" con un "+String.valueOf(df.format(totalA))+"%.");
        
        XWPFParagraph subtitulo3 = word.createParagraph(); XWPFRun runS3 = subtitulo3.createRun(); runS3.setBold(true); 
        runS3.setText("Estrategias, métodos y/o herramientas acordes al tipo de aprendizaje");
        
        XWPFParagraph parrafoVacio2 = word.createParagraph(); XWPFRun runVacio2 = parrafoVacio2.createRun(); runVacio2.setBold(true); runVacio2.setText("");
        
        XWPFParagraph subtitulo4 = word.createParagraph(); XWPFRun runS4 = subtitulo4.createRun(); runS4.setBold(true); 
        runS4.setText("1. Visual");
        
        XWPFParagraph parrafoVacio3 = word.createParagraph(); XWPFRun runVacio3 = parrafoVacio3.createRun(); runVacio3.setBold(true); runVacio3.setText("");
        
        XWPFParagraph parrafo3 = word.createParagraph(); parrafo3.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP3 = parrafo3.createRun();
        runP3.setText("Definición. Es un método de enseñanza que utiliza un conjunto de organizadores gráficos tanto para representar información, "
                + "como para trabajar con ideas y conceptos, que al utilizarlos ayudan a los estudiantes a pensar y a aprender más efectivamente.");
        
        XWPFParagraph parrafoVacio4 = word.createParagraph(); XWPFRun runVacio4 = parrafoVacio4.createRun(); runVacio4.setBold(true); runVacio4.setText("");
        
        XWPFParagraph subtitulo5 = word.createParagraph(); XWPFRun runS5 = subtitulo5.createRun(); runS5.setBold(true); 
        runS5.setText("2. Auditivo");
        
        XWPFParagraph parrafoVacio5 = word.createParagraph(); XWPFRun runVacio5 = parrafoVacio5.createRun(); runVacio5.setBold(true); runVacio5.setText("");
        
        XWPFParagraph parrafo4 = word.createParagraph(); parrafo4.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP4 = parrafo4.createRun();
        runP4.setText("Definición. Se trata del aprendizaje relacionado con sensaciones y movimientos. En otras palabras, es lo que ocurre cuando se aprende "
                + "más fácilmente al movernos y tocar las cosas, como cuando caminamos al recitar información o hacemos un experimento manipulando instrumentos "
                + "de laboratorio. Este sistema es más lento que los otros dos, pero tiende a generar un aprendizaje más profundo y difícil de olvidar.");
        
        XWPFParagraph parrafoVacio6 = word.createParagraph(); XWPFRun runVacio6 = parrafoVacio6.createRun(); runVacio6.setBold(true); runVacio6.setText("");
        
        XWPFParagraph subtitulo6 = word.createParagraph(); XWPFRun runS6 = subtitulo6.createRun(); runS6.setBold(true); 
        runS6.setText("3. Kinestésico");
        
        XWPFParagraph parrafoVacio7 = word.createParagraph(); XWPFRun runVacio7 = parrafoVacio7.createRun(); runVacio7.setBold(true); runVacio7.setText("");
        
        XWPFParagraph parrafo5 = word.createParagraph(); parrafo5.setAlignment(ParagraphAlignment.BOTH); XWPFRun runP5 = parrafo5.createRun();
        runP5.setText("Definición. Es un método de enseñanza que se dirige a las estudiantes cuyo estilo de aprendizaje se orienta más hacia la asimilación "
                + "de la información a través del oído y no por la vista. Si bien la gran mayoría de la gente tiende a ser principalmente visuales en la forma"
                + " de relacionarse con el mundo alrededor de ellos, la estimulación de audio se emplea a menudo como un medio secundario de encontrar y absorber conocimientos.");
        
        XWPFParagraph parrafoVacio8 = word.createParagraph(); XWPFRun runVacio8 = parrafoVacio8.createRun(); runVacio8.setBold(true); runVacio8.setText("");
        XWPFParagraph parrafoVacio9 = word.createParagraph(); XWPFRun runVacio9 = parrafoVacio9.createRun(); runVacio9.setBold(true); runVacio9.setText("");
        
        XWPFParagraph testATT = word.createParagraph(); testATT.setAlignment(ParagraphAlignment.CENTER); XWPFRun runATT = testATT.createRun(); runATT.setBold(true); 
        runATT.setText("ATENTAMENTE");
        
        XWPFParagraph parrafoVacio10 = word.createParagraph(); XWPFRun runVacio10 = parrafoVacio10.createRun(); runVacio10.setBold(true); runVacio10.setText("");
        XWPFParagraph parrafoVacio11 = word.createParagraph(); XWPFRun runVacio11 = parrafoVacio11.createRun(); runVacio11.setBold(true); runVacio11.setText("");
        
        XWPFParagraph line = word.createParagraph(); line.setAlignment(ParagraphAlignment.CENTER); XWPFRun runline = line.createRun(); runline.setBold(true); 
        runline.setText("_____________________________");
        XWPFParagraph tTutor = word.createParagraph(); tTutor.setAlignment(ParagraphAlignment.CENTER); XWPFRun runt = tTutor.createRun(); runt.setBold(true); 
        runt.setText("Tutor");
        
    }
    
    public void escribirDatosExcel(String[] datos,int i, int row,String excel){
        XSSFRow fila = libro.getSheet(excel).createRow(i + row);
        for (int j = 0; j < datos.length; j++) {
            XSSFCell celda = fila.createCell(j);
            celda.setCellValue(datos[j]);
        }
    }
    
    public DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje packDto(DtoAlumnosEncuesta.DtoResultadoTestEstudiante dto){
        DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje dtoR;
        Integer sV = dto.getListaV().stream().mapToInt(Integer::intValue).sum();
        Integer sA = dto.getListaA().stream().mapToInt(Integer::intValue).sum();
        Integer sK = dto.getListaK().stream().mapToInt(Integer::intValue).sum();
        Integer total = sV + sA + sK;
        Double pV = (sV.doubleValue() / total.doubleValue()) * 100;
        Double pA = (sA.doubleValue() / total.doubleValue()) * 100;
        Double pK = (sK.doubleValue() / total.doubleValue()) * 100;
        
        dtoR = new DtoAlumnosEncuesta.DtoResultadoTestEstudiantePorcentaje(dto.getMatricula(), pV, pA, pK);
        return dtoR;
    }
    
    //////////////Metodos para generar el documento de word
    
    public void escribirDatosWordAEP(VariablesProntuario vp1, VariablesProntuario vp2,VariablesProntuario vp3, VariablesProntuario vp4, DtoEstudiante estudiante,
                                     AreasUniversidad programa, String proyecto, String fecha, RegistroEgresadosTerminacionEstudios registro){
        StringBuilder programaEducativo = new StringBuilder(programa.getNombre());
        programaEducativo = programaEducativo.delete(0, 9);

        if(vp4.getValor().length() <= 20){
            agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }

        XWPFTable tabPrincipal = word.createTable(4, 1);
        setTableWidth(tabPrincipal, "9000");
        setTableAlign(tabPrincipal, ParagraphAlignment.CENTER);
        removeBorders(tabPrincipal);
        setCellsWidth(tabPrincipal.getRow(0).getCell(0), 9000);

        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "La Universidad Tecnológica de Xicotepec de Juárez manifiesta que, "+ agregarTextoPorGenero(estudiante), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 1, "AvantGarde Bk BT", " C. " + estudiante.getPersona().getNombre().toUpperCase()+estudiante.getPersona().getApellidoPaterno().toUpperCase()+" "+estudiante.getPersona().getApellidoMaterno().toUpperCase(), Boolean.TRUE, 12, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 2, "AvantGarde Bk BT", " de la carrera de Técnico Superior Universitario en"+ programaEducativo.toString() + " con número de matrícula ", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 3, "AvantGarde Bk BT", String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), Boolean.TRUE, 12, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 4, "AvantGarde Bk BT", " presentó la memoria de Estadía: ", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 5, "AvantGarde Bk BT", " '"+ proyecto +"'" , Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.TRUE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 6, "AvantGarde Bk BT", ", cumpliendo satisfactoriamente con lo estipulado en la única opción de titulación.", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);

        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT","", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);

        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 2, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT","Por lo que se extiende la presente, para los efectos legales que haya lugar, en la Ciudad de Xicotepec de Juárez, Puebla a los "+ fecha + ".", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 3, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT","", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"Times New Roman", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);


        XWPFTable tab = word.createTable(1, 3);
        setTableWidth(tab, "9000");
        setTableAlign(tab, ParagraphAlignment.CENTER);
        removeLeftBorder(tab);
        removeRightBorder(tab);
        removeBottomBorder(tab);
        removeInsideHBorder(tab);
        removeInsideVBorder(tab);
        setCellsWidth(tab.getRow(0).getCell(0), 4150);
        setCellsWidth(tab.getRow(0).getCell(1), 700);
        setCellsWidth(tab.getRow(0).getCell(2), 4150);
        setTableCellBorder(tab.getRow(0).getCell(1), Border.TOP, STBorder.NIL, 0);
        setTableCellBorder(tab.getRow(0).getCell(0), Border.TOP, STBorder.SINGLE, 200);
        setTableCellBorder(tab.getRow(0).getCell(2), Border.TOP, STBorder.SINGLE, 200);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 0, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                vp1.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 0, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                vp3.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 1, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                vp2.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 2, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                vp4.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        if(programa.getAreaSuperior().equals(Short.parseShort("28")) || programa.getAreaSuperior().equals(Short.parseShort("27")) ||
                programa.getAreaSuperior().equals(Short.parseShort("24")) || programa.getAreaSuperior().equals(Short.parseShort("25"))){
            agregarParrafosVacios(2, 7, 2.0);
        }else{
            agregarParrafosVacios(3, 7, 2.0);
        }


        XWPFTable tab2 = word.createTable(1, 6);
        setTableWidth(tab2, "10200");
        setTableAlign(tab2, ParagraphAlignment.CENTER);
        removeLeftBorder(tab2);
        removeRightBorder(tab2);
        removeBottomBorder(tab2);
        removeInsideHBorder(tab2);
        removeInsideVBorder(tab2);
        removeTopBorder(tab2);

        tab2.getRow(0).setHeight((2100 / 10)); //set height 1/10 inch.
        tab2.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"

        tab2.getRow(0).getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(3).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(4).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(5).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);

        setTableCellBorder(tab2.getRow(0).getCell(1), Border.BOTTOM, STBorder.SINGLE, 200);
        setTableCellBorder(tab2.getRow(0).getCell(3), Border.BOTTOM, STBorder.SINGLE, 200);
        setTableCellBorder(tab2.getRow(0).getCell(5), Border.BOTTOM, STBorder.SINGLE, 200);

        setCellsWidth(tab2.getRow(0).getCell(0), 2000);
        setCellsWidth(tab2.getRow(0).getCell(1), 2100);
        setCellsWidth(tab2.getRow(0).getCell(2), 950);
        setCellsWidth(tab2.getRow(0).getCell(3), 2100);
        setCellsWidth(tab2.getRow(0).getCell(4), 950);
        setCellsWidth(tab2.getRow(0).getCell(5), 2100);

        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 0, 0, 0, ParagraphAlignment.RIGHT, 0,"AvantGarde Bk BT", "Control de registro: folio", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getFolio()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "; libro", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 3, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getLibro()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 4, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "y fojas:", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 5, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getFoja()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

    }
    
    public void escribirDatosWordASS(VariablesProntuario vp1, VariablesProntuario vp2,VariablesProntuario vp3, VariablesProntuario vp4, DtoEstudiante estudiante,
                                     AreasUniversidad programa, String fecha, RegistroEgresadosTerminacionEstudios registro){
        StringBuilder programaEducativo = new StringBuilder(programa.getNombre());
        programaEducativo = programaEducativo.delete(0, 9);

        XWPFTable tabPrincipal = word.createTable(5, 1);
        setTableWidth(tabPrincipal, "9000");
        setTableAlign(tabPrincipal, ParagraphAlignment.CENTER);
        removeBorders(tabPrincipal);
        setCellsWidth(tabPrincipal.getRow(0).getCell(0), 9000);

        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "La Universidad Tecnológica de Xicotepec de Juárez hace constar que, "+ agregarTextoPorGenero(estudiante), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 1, "AvantGarde Bk BT", " C. " + estudiante.getPersona().getNombre().toUpperCase()+estudiante.getPersona().getApellidoPaterno().toUpperCase()+" "+estudiante.getPersona().getApellidoMaterno().toUpperCase(), Boolean.TRUE, 12, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 2, "AvantGarde Bk BT", " de la carrera de Técnico Superior Universitario en"+ programaEducativo.toString() + " con número de matrícula ", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 3, "AvantGarde Bk BT", String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), Boolean.TRUE, 12, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.BOTH, 4, "AvantGarde Bk BT", " concluyó satisfactoriamente su Servicio Social en un periodo de 6 meses en el cual se cumplieron 480 horas, conforme a lo dispuesto en el Artículo 55 de la Ley Reglamentaria del Artículo 5° Constitucional, relativo al ejercicio de las profesiones. ", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);

        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT","", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 2, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT","", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 3, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "Por lo que se extiende la presente, para los efectos legales que haya lugar, en la Ciudad de Xicotepec de Juárez, Puebla, a los "+ fecha.concat("."), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 4, 0, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT","", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.5);
        
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        agregarTextoParrafo(0,0,ParagraphAlignment.CENTER,0,"AvantGarde Bk BT", "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
//
        XWPFTable tab = word.createTable(1, 3);
        setTableWidth(tab, "9000");
        setTableAlign(tab, ParagraphAlignment.CENTER);
        removeLeftBorder(tab);
        removeRightBorder(tab);
        removeBottomBorder(tab);
        removeInsideHBorder(tab);
        removeInsideVBorder(tab);
        setCellsWidth(tab.getRow(0).getCell(0), 4150);
        setCellsWidth(tab.getRow(0).getCell(1), 700);
        setCellsWidth(tab.getRow(0).getCell(2), 4150);
        setTableCellBorder(tab.getRow(0).getCell(1), Border.TOP, STBorder.NIL, 0);
        setTableCellBorder(tab.getRow(0).getCell(0), Border.TOP, STBorder.SINGLE, 200);
        setTableCellBorder(tab.getRow(0).getCell(2), Border.TOP, STBorder.SINGLE, 200);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 0, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                vp1.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 0, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                vp3.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 1, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                "", Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                vp2.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 2, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                vp4.getValor(), Boolean.FALSE, 12, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarParrafosVacios(2, 7, 2.0);

        XWPFTable tab2 = word.createTable(1, 6);
        setTableWidth(tab2, "10200");
        setTableAlign(tab2, ParagraphAlignment.CENTER);
        removeLeftBorder(tab2);
        removeRightBorder(tab2);
        removeBottomBorder(tab2);
        removeInsideHBorder(tab2);
        removeInsideVBorder(tab2);
        removeTopBorder(tab2);

        tab2.getRow(0).setHeight((2100 / 10)); //set height 1/10 inch.
        tab2.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"

        tab2.getRow(0).getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(3).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(4).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(5).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);

        setTableCellBorder(tab2.getRow(0).getCell(1), Border.BOTTOM, STBorder.SINGLE, 200);
        setTableCellBorder(tab2.getRow(0).getCell(3), Border.BOTTOM, STBorder.SINGLE, 200);
        setTableCellBorder(tab2.getRow(0).getCell(5), Border.BOTTOM, STBorder.SINGLE, 200);

        setCellsWidth(tab2.getRow(0).getCell(0), 2000);
        setCellsWidth(tab2.getRow(0).getCell(1), 2100);
        setCellsWidth(tab2.getRow(0).getCell(2), 950);
        setCellsWidth(tab2.getRow(0).getCell(3), 2100);
        setCellsWidth(tab2.getRow(0).getCell(4), 950);
        setCellsWidth(tab2.getRow(0).getCell(5), 2100);

        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 0, 0, 0, ParagraphAlignment.RIGHT, 0,"AvantGarde Bk BT", "Control de registro: folio", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getFolio()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "; libro", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 3, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getLibro()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 4, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "y fojas:", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 5, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getFoja()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
    }
    
    public void escribirDatosWordCE(VariablesProntuario vp1, VariablesProntuario vp2, VariablesProntuario vp3, VariablesProntuario vp4, DtoEstudiante estudiante, AreasUniversidad programa,
                                    String fecha, Personal director, RegistroEgresadosTerminacionEstudios registro, List<PlanCompetencias> listaEspecificas, List<PlanCompetencias> listaGenericas){
        StringBuilder programaEducativo = new StringBuilder(programa.getNombre());
        programaEducativo = programaEducativo.delete(0, 9);
        
        DtoInscripcion inscripcion1 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 1).findFirst().get(); 
        DtoInscripcion inscripcion6 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 6).findFirst().get(); 
        
        Estudiante estudiante1 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 1).findFirst().get().getInscripcion();
        Estudiante estudiante2 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 2).findFirst().get().getInscripcion();
        Estudiante estudiante3 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 3).findFirst().get().getInscripcion();
        Estudiante estudiante4 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 4).findFirst().get().getInscripcion();
        Estudiante estudiante5 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 5).findFirst().get().getInscripcion();
        Estudiante estudiante6 = estudiante.getInscripciones().stream().filter(est -> est.getGrupo().getGrado() == 6).findFirst().get().getInscripcion();
        
        String periodoInicio = inscripcion1.getPeriodo().getMesInicio().getMes()+" "+inscripcion1.getPeriodo().getAnio();
        String periodoFin = inscripcion6.getPeriodo().getMesFin().getMes()+" "+inscripcion6.getPeriodo().getAnio();
        
        XWPFTable tabPrincipal = word.createTable(2, 2);
        setTableWidth(tabPrincipal, "8900");
        setTableAlign(tabPrincipal, ParagraphAlignment.CENTER);
        setCellsWidth(tabPrincipal.getRow(0).getCell(0), 1800);
        setCellsWidth(tabPrincipal.getRow(0).getCell(1), 6400);
        setCellsWidth(tabPrincipal.getRow(1).getCell(1), 6400);
        mergeCellsVertically(tabPrincipal, 0, 0, 1);
        removeLeftBorder(tabPrincipal);
        removeRightBorder(tabPrincipal);
        //removeBottomBorder(tabPrincipal);
        removeInsideHBorder(tabPrincipal);
        removeInsideVBorder(tabPrincipal);
        removeTopBorder(tabPrincipal);



        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "CERTIFICADO DE ESTUDIOS", Boolean.TRUE, 14, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "La Universidad Tecnológica de Xicotepec de Juárez certifica que, según constancias existentes en el archivo escolar, "+ agregarTextoPorGenero(estudiante), Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 1, "AvantGarde Bk BT", " C. " + estudiante.getPersona().getNombre().toUpperCase()+" "+estudiante.getPersona().getApellidoPaterno().toUpperCase()+" "+estudiante.getPersona().getApellidoMaterno().toUpperCase(), Boolean.TRUE, 9, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 2, "AvantGarde Bk BT", " con matrícula ", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 3, "AvantGarde Bk BT", String.valueOf(estudiante.getInscripcionActiva().getInscripcion().getMatricula()), Boolean.TRUE, 9, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH,4, "AvantGarde Bk BT", " cursó y aprobó las asignaturas que integran el plan de estudios ", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 5, "AvantGarde Bk BT", String.valueOf(estudiante6.getGrupo().getPlan().getAnio()), Boolean.TRUE, 9, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 6, "AvantGarde Bk BT", " en el periodo escolar ", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 7, "AvantGarde Bk BT", periodoInicio+" - "+periodoFin, Boolean.TRUE, 9, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 8, "AvantGarde Bk BT", " de la carrera Técnico Superior Universitario en ", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 9, "AvantGarde Bk BT",  programaEducativo.toString(), Boolean.TRUE, 9, UnderlinePatterns.SINGLE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabPrincipal, XWPFTableCell.XWPFVertAlign.CENTER, 1, 1, 0, 0, ParagraphAlignment.BOTH, 10, "AvantGarde Bk BT", " obteniendo las calificaciones finales que a continuación se anotan.", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarTextoParrafo(0, 0, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        
        List<DtoCargaAcademica> lista1 = controlador.getCargasAcademicas(estudiante1);
        lista1.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista2 = controlador.getCargasAcademicas(estudiante2);
        lista2.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista3 = controlador.getCargasAcademicas(estudiante3);
        lista3.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista4 = controlador.getCargasAcademicas(estudiante4);
        lista4.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));
        List<DtoCargaAcademica> lista5 = controlador.getCargasAcademicas(estudiante5);
        lista5.sort((DtoCargaAcademica o1, DtoCargaAcademica o2) -> o1.getPlanEstudioMateria().getClaveMateria().compareTo(o2.getPlanEstudioMateria().getClaveMateria()));

        Integer totalMaterias = lista1.size() + lista2.size() + lista3.size() + lista4.size() + lista5.size();
        Integer filas = 4 + totalMaterias;

        XWPFTable tabMaterias = word.createTable(filas, 4);
        CTTblLayoutType type = tabMaterias.getCTTbl().getTblPr().addNewTblLayout();
        type.setType(STTblLayoutType.FIXED);
        setTableWidth(tabMaterias, "8800");
        setTableAlign(tabMaterias, ParagraphAlignment.CENTER);

        setCellsWidth(tabMaterias.getRow(0).getCell(0), 7300);
        setCellsWidth(tabMaterias.getRow(0).getCell(1), 800);
        setCellsWidth(tabMaterias.getRow(0).getCell(2), 1700);
        setCellsWidth(tabMaterias.getRow(1).getCell(2), 900);
        setCellsWidth(tabMaterias.getRow(1).getCell(3), 800);

        setStyleCell(tabMaterias.getRow(0).getCell(0), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(0).getCell(1), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(0).getCell(2), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(1).getCell(0), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(1).getCell(2), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(1).getCell(3), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, STBorder.SINGLE);

        mergeCellsVertically(tabMaterias, 0, 0, 1);
        mergeCellsVertically(tabMaterias, 1, 0, 1);
        mergeCellsHorizontal(tabMaterias, 0, 2, 3);
        mergeCellsHorizontal(tabMaterias, totalMaterias + 3, 0, 1);
        mergeCellsHorizontal(tabMaterias, totalMaterias + 2, 2, 3);
        mergeCellsHorizontal(tabMaterias, totalMaterias + 3, 2, 3);
        tabMaterias.getRow(0).setHeight(1900 / 10); //set height 1/10 inch.
        tabMaterias.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        tabMaterias.getRow(1).setHeight(1900 / 10); //set height 1/10 inch.
        tabMaterias.getRow(1).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"

        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "ASIGNATURAS",
                Boolean.TRUE, 7, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.CENTER, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "HORAS",
                Boolean.TRUE, 7, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.CENTER, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "CALIFICACIÓN",
                Boolean.TRUE, 7, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.CENTER, 1, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "NÚMERO",
                Boolean.TRUE, 7, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.CENTER, 1, 3, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "LETRA",
                Boolean.TRUE, 7, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        
        BigDecimal promedioFinalM;
        String escala;
        List<BigDecimal> promediosList = new ArrayList<>();
        for(int i = 0; i <= lista1.size() - 1; i++){
            DtoCargaAcademica carga = lista1.get(i);
            Integer hrsPracticas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasPracticas).sum();
            Integer hrsTeoricas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasTeoricas).sum();
            promedioFinalM = obtenerPromedio(carga, estudiante1);
            escala = obtenerEscala(carga, estudiante1);
            promediosList.add(promedioFinalM);
            XWPFTableRow row = tabMaterias.getRow(i + 2);
            agregarInformacionFila(row, carga, promedioFinalM, escala, hrsPracticas, hrsTeoricas);
        } 
        Integer fila = lista1.size() + 2;
        for(int i = 0; i <= lista2.size() - 1; i++){
            DtoCargaAcademica carga = lista2.get(i);
            Integer hrsPracticas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasPracticas).sum();
            Integer hrsTeoricas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasTeoricas).sum();
            promedioFinalM = obtenerPromedio(carga, estudiante2);
            escala = obtenerEscala(carga, estudiante2);
            promediosList.add(promedioFinalM);
            XWPFTableRow row = tabMaterias.getRow(i + fila);
            agregarInformacionFila(row, carga, promedioFinalM, escala, hrsPracticas, hrsTeoricas);
        }
        
        Integer fil = lista1.size() + lista2.size() + 2;
        for(int i = 0; i <= lista3.size() - 1; i++){
            DtoCargaAcademica carga = lista3.get(i);
            Integer hrsPracticas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasPracticas).sum();
            Integer hrsTeoricas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasTeoricas).sum();
            promedioFinalM = obtenerPromedio(carga, estudiante3);
            escala = obtenerEscala(carga, estudiante3);
            promediosList.add(promedioFinalM);
            XWPFTableRow row = tabMaterias.getRow(i + fil);
            agregarInformacionFila(row, carga, promedioFinalM, escala, hrsPracticas, hrsTeoricas);
        }
        
        Integer fi = lista1.size() + lista2.size() + lista3.size() + 2;
        for(int i = 0; i <= lista4.size() - 1; i++){
            DtoCargaAcademica carga = lista4.get(i);
            Integer hrsPracticas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasPracticas).sum();
            Integer hrsTeoricas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasTeoricas).sum();
            promedioFinalM = obtenerPromedio(carga, estudiante4);
            escala = obtenerEscala(carga, estudiante4);
            promediosList.add(promedioFinalM);
            XWPFTableRow row = tabMaterias.getRow(i + fi);
            agregarInformacionFila(row, carga, promedioFinalM, escala, hrsPracticas, hrsTeoricas);
        }
        
        Integer f = lista1.size() + lista2.size() + lista3.size() + lista4.size() + 2;
        for(int i = 0; i <= lista5.size() - 1; i++){
            DtoCargaAcademica carga = lista5.get(i);
            Integer hrsPracticas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasPracticas).sum();
            Integer hrsTeoricas = carga.getPlanEstudioMateria().getIdMateria().getUnidadMateriaList().stream().mapToInt(UnidadMateria::getHorasTeoricas).sum();
            promedioFinalM = obtenerPromedio(carga, estudiante5);
            escala = obtenerEscala(carga, estudiante5);
            promediosList.add(promedioFinalM);
            XWPFTableRow row = tabMaterias.getRow(i + f);
            agregarInformacionFila(row, carga, promedioFinalM, escala, hrsPracticas, hrsTeoricas);
        }
        //System.out.println("Tamaño de la lista:"+ promediosList.size());
        
        BigDecimal suma = promediosList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if(suma.equals(BigDecimal.ZERO)) return;
        BigDecimal promedioFinal = suma.divide(new BigDecimal(totalMaterias), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        String promedio = promedioFinal.toString();
//        System.out.println("Promedio final:" + promedio);
        Integer row = totalMaterias + 2;
        setStyleCell(tabMaterias.getRow(row).getCell(0), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(row).getCell(1), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(row).getCell(2), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, STBorder.SINGLE);
        setStyleCell(tabMaterias.getRow(row + 1).getCell(1), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, STBorder.SINGLE);

        tabMaterias.getRow(row).setHeight((1900 / 10)); //set height 1/10 inch.
        tabMaterias.getRow(row).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.BOTTOM, row, 0, 0, 0, ParagraphAlignment.LEFT, 0, "AvantGarde Bk Bt", "Estadía Profesional", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.BOTTOM, row, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk Bt", "525", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.BOTTOM, row, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk Bt", "AC", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        tabMaterias.getRow(row + 1).setHeight((1900 / 10)); //set height 1/10 inch.
        tabMaterias.getRow(row + 1).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        setTableCellBorder(tabMaterias.getRow(row + 1).getCell(0), Border.RIGHT, STBorder.NIL, 0);
        setTableCellBorder(tabMaterias.getRow(row + 1).getCell(1), Border.LEFT, STBorder.NIL, 0);

        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.BOTTOM, row + 1, 0, 0, 0, ParagraphAlignment.RIGHT, 0, "AvantGarde Bk Bt", "Promedio Final", Boolean.TRUE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tabMaterias, XWPFTableCell.XWPFVertAlign.BOTTOM, row + 1, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk Bt", promedio, Boolean.TRUE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        if(totalMaterias == 35){
            agregarParrafosVacios(11, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 36){
            agregarParrafosVacios(12, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 37){
            agregarParrafosVacios(11, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 38){
            agregarParrafosVacios(10, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 39){
            agregarParrafosVacios(9, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 40){
            agregarParrafosVacios(8, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 41){
            agregarParrafosVacios(7, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 42){
            agregarParrafosVacios(6, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 43){
            agregarParrafosVacios(5, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 44){
            agregarParrafosVacios(6, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 45){
            agregarParrafosVacios(4, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 46){
            agregarParrafosVacios(3, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 47){
            agregarParrafosVacios(2, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        if(totalMaterias == 48){
            agregarParrafosVacios(1, 8, 1.0);
            agregarTextoParrafo(0, 40, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }
        
        XWPFTable tabDescripcion = word.createTable(3 + listaEspecificas.size() + listaGenericas.size(), 1);
        
        setTableWidth(tabDescripcion, "8800");
        setTableAlign(tabDescripcion, ParagraphAlignment.CENTER);
        setCellsWidth(tabDescripcion.getRow(0).getCell(0), 8800);
        setTableCellBorder(tabDescripcion.getRow(0).getCell(0), Border.BOTTOM, STBorder.SINGLE, 0);
        setTableCellBorder(tabDescripcion.getRow(1).getCell(0), Border.BOTTOM, STBorder.SINGLE, 0);
        setTableCellBorder(tabDescripcion.getRow(listaEspecificas.size() + 2).getCell(0), Border.TOP, STBorder.SINGLE, 0);
        setTableCellBorder(tabDescripcion.getRow(listaEspecificas.size() + 2).getCell(0), Border.BOTTOM, STBorder.SINGLE, 0);

        tabDescripcion.getRow(0).setHeight((4200 / 10)); //set height 1/10 inch.
        tabDescripcion.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        agregarTextoCelda(tabDescripcion, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 0, 40, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", 
                "COMPETENCIAS PROFESIONALES QUE AVALA EL PRESENTE CERTIFICADO", Boolean.TRUE, 8, UnderlinePatterns.NONE, Boolean.TRUE, Boolean.TRUE, 1.0);
        
        if(!listaEspecificas.isEmpty()){
            agregarTextoCelda(tabDescripcion, XWPFTableCell.XWPFVertAlign.BOTTOM, 1, 0, 0, 0, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "Competencias Específicas", Boolean.TRUE, 8, UnderlinePatterns.NONE, Boolean.TRUE, Boolean.FALSE, 1.0);
            for (int i = 0; i <= listaEspecificas.size() - 1; i++) {
                setStyleCell(tabDescripcion.getRow(i + 2).getCell(0), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, STBorder.NIL);
                PlanCompetencias c = listaEspecificas.get(i);
                agregarTextoCelda(tabDescripcion, XWPFTableCell.XWPFVertAlign.BOTTOM, i + 2, 0, 60, 240, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT",
                        c.getDescripcion(), Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
            }
        }
        
        if(!listaGenericas.isEmpty()){
            agregarTextoCelda(tabDescripcion, XWPFTableCell.XWPFVertAlign.BOTTOM, listaEspecificas.size() + 2, 0, 0, 0, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "Competencias Genéricas", Boolean.TRUE, 8, UnderlinePatterns.NONE, Boolean.TRUE, Boolean.FALSE, 1.0);
            for (int i = 0; i <= listaGenericas.size() - 1; i++) {
                if(i == listaGenericas.size() - 1){
                    setStyleCell(tabDescripcion.getRow(i + 3 + listaEspecificas.size()).getCell(0), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, STBorder.NIL);
                }else{
                    setStyleCell(tabDescripcion.getRow(i + 3 + listaEspecificas.size()).getCell(0), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, STBorder.NIL);
                }

                PlanCompetencias c = listaGenericas.get(i);
                agregarTextoCelda(tabDescripcion, XWPFTableCell.XWPFVertAlign.BOTTOM, i + 3 + listaEspecificas.size(), 0 , 60, 240, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT",
                        c.getDescripcion(), Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
            }
        }
        Integer numeroC = listaEspecificas.size() + listaGenericas.size();
         
        agregarParrafosVacios(1, 9, 1.0);
         
        agregarTextoParrafo(0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "El presente certificado ampara " + totalMaterias + " asignaturas de un total de " + totalMaterias + ", y " + convertirNumero(numeroC) 
                        + " competencias profesionales de un total de " + convertirNumero(numeroC) + ".", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        
        agregarTextoParrafo(0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        
        agregarTextoParrafo(0, 0, ParagraphAlignment.BOTH, 0, "AvantGarde Bk BT", "Expedido en Xicotepec de Juárez, Puebla a los " + fecha + ".", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
         
        if (numeroC.equals(5)) {
            agregarParrafosVacios(13, 9, 1.0);
        }
        if (numeroC.equals(6)) {
            agregarParrafosVacios(10, 9 , 1.0);
        }
        if (numeroC.equals(7)) {
            agregarParrafosVacios(7, 9, 1.0);
        }
        if (numeroC.equals(8)) {
            agregarParrafosVacios(4, 9 , 1.0);
        }
         
        XWPFTable tab = word.createTable(1, 3);
        setTableWidth(tab, "8800");  
        setTableAlign(tab, ParagraphAlignment.CENTER);
        removeLeftBorder(tab);
        removeRightBorder(tab);
        removeBottomBorder(tab);
        removeInsideHBorder(tab);
        removeInsideVBorder(tab);
        setCellsWidth(tab.getRow(0).getCell(0), 4150);
        setCellsWidth(tab.getRow(0).getCell(1), 500);
        setCellsWidth(tab.getRow(0).getCell(2), 4150);
        setTableCellBorder(tab.getRow(0).getCell(1), Border.TOP, STBorder.NIL, 0);
        setTableCellBorder(tab.getRow(0).getCell(0), Border.TOP, STBorder.SINGLE, 200);
        setTableCellBorder(tab.getRow(0).getCell(2), Border.TOP, STBorder.SINGLE, 200);
        
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 0, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                vp1.getValor(), Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 0, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                vp3.getValor(), Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);

        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.CENTER, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                "", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.CENTER, 0, 1, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                "", Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT",
                vp2.getValor(), Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.TRUE, 1.0);
        agregarTextoCelda(tab, XWPFTableCell.XWPFVertAlign.TOP, 0, 2, 0, 0, ParagraphAlignment.CENTER, 1, "AvantGarde Bk BT",
                vp4.getValor(), Boolean.FALSE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        //create first row

        if(director.getAreaOperativa() == Short.parseShort("28")){
            agregarParrafosVacios(2, 9, 1.0);
            agregarTextoParrafo(0, 60, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }else {
            agregarParrafosVacios(3, 9, 1.0);
            agregarTextoParrafo(0, 60, ParagraphAlignment.LEFT, 0, "AvantGarde Bk BT", "", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, 1.0);
        }


        
        XWPFTable tabEscalas = word.createTable(3, 8);
        setTableWidth(tabEscalas, "8800");  
        setTableAlign(tabEscalas, ParagraphAlignment.CENTER);
        mergeCellsHorizontal(tabEscalas, 0, 0, 7);
        setCellsWidth(tabEscalas.getRow(1).getCell(0), 1600);
        setCellsWidth(tabEscalas.getRow(1).getCell(1), 1600);
        setCellsWidth(tabEscalas.getRow(1).getCell(2), 500);
        setCellsWidth(tabEscalas.getRow(1).getCell(3), 1700);
        setCellsWidth(tabEscalas.getRow(1).getCell(4), 400);
        setCellsWidth(tabEscalas.getRow(1).getCell(5), 1250);
        setCellsWidth(tabEscalas.getRow(1).getCell(6), 500);
        setCellsWidth(tabEscalas.getRow(1).getCell(7), 1250);
        tabEscalas.getRow(0).setHeight((int) (1650 / 10)); //set height 1/10 inch.
        tabEscalas.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        tabEscalas.getRow(1).setHeight((int) (1650 / 10)); //set height 1/10 inch.
        tabEscalas.getRow(1).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        tabEscalas.getRow(2).setHeight((int) (1650 / 10)); //set height 1/10 inch.
        tabEscalas.getRow(2).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"

        setStyleTable(tabEscalas, 3, 8, STBorder.SINGLE);

        agregarTextoCelda(tabEscalas, XWPFTableCell.XWPFVertAlign.CENTER, 0, 0, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "DESCRIPCIÓN DE LA ESCALA ALFANUMÉRICA", Boolean.TRUE, 5, 
                UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        
        agregarInformacion(tabEscalas.getRow(1), "Asignaturas No Integradoras", "AU = Autónomo", "10", "DE = Destacado", "9", "SA = Satisfactorio", "8", "NA = No Acreditado");
        agregarInformacion(tabEscalas.getRow(2), "Asignaturas Integradoras", "CA = Competente Autónomo", "10", "CD = Competente Destacado", "9", "CO = Competente", "8", "NA = No Acreditado");

        if(director.getAreaOperativa() == Short.parseShort("28")){
            agregarParrafosVacios(2, 9, 1.0);
        }else {
            agregarParrafosVacios(3, 9, 1.0);
        }


        XWPFTable tab2 = word.createTable(1, 6);
        setTableWidth(tab2, "10200");
        setTableAlign(tab2, ParagraphAlignment.CENTER);
        removeLeftBorder(tab2);
        removeRightBorder(tab2);
        removeBottomBorder(tab2);
        removeInsideHBorder(tab2);
        removeInsideVBorder(tab2);
        removeTopBorder(tab2);

        tab2.getRow(0).setHeight((2100 / 10)); //set height 1/10 inch.
        tab2.getRow(0).getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"

        tab2.getRow(0).getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(3).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(4).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        tab2.getRow(0).getCell(5).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);

        setTableCellBorder(tab2.getRow(0).getCell(1), Border.BOTTOM, STBorder.SINGLE, 200);
        setTableCellBorder(tab2.getRow(0).getCell(3), Border.BOTTOM, STBorder.SINGLE, 200);
        setTableCellBorder(tab2.getRow(0).getCell(5), Border.BOTTOM, STBorder.SINGLE, 200);

        setCellsWidth(tab2.getRow(0).getCell(0), 2000);
        setCellsWidth(tab2.getRow(0).getCell(1), 2100);
        setCellsWidth(tab2.getRow(0).getCell(2), 950);
        setCellsWidth(tab2.getRow(0).getCell(3), 2100);
        setCellsWidth(tab2.getRow(0).getCell(4), 950);
        setCellsWidth(tab2.getRow(0).getCell(5), 2100);

        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 0, 0, 0, ParagraphAlignment.RIGHT, 0,"AvantGarde Bk BT", "Control de registro: folio", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 1, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getFolio()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 2, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "; libro", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 3, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getLibro()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 4, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "y fojas:", Boolean.FALSE, 8, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        agregarTextoCelda(tab2, XWPFTableCell.XWPFVertAlign.BOTTOM, 0, 5, 0, 0, ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", String.valueOf(registro.getFoja()), Boolean.TRUE, 9, UnderlinePatterns.NONE, Boolean.FALSE, Boolean.FALSE, 1.0);
        
    }
    
    public void setTableAlign(XWPFTable table, ParagraphAlignment align) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJc jc = (tblPr.isSetJc()? tblPr.getJc() : tblPr.addNewJc());
        STJc.Enum en = STJc.Enum.forInt(align.getValue());
        jc.setVal(en);
    }
    
    public void setTableWidth(XWPFTable table,String width){  
        CTTbl ttbl = table.getCTTbl();  
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
        tblWidth.setW(new BigInteger(width));  
        tblWidth.setType(STTblWidth.DXA);  
    }  
    
    public void setCellsWidth(XWPFTableCell cell, int width){
        CTTc cttc = cell.getCTTc();  
        CTTcPr cellPr = cttc.addNewTcPr();  
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
    }
    
    public void removeBorders(XWPFTable table) {
        final CTTblPr pr = getTblPr(table, false);
        if (pr != null && pr.isSetTblBorders()) {
            pr.unsetTblBorders();
        }
    }
    
    private CTTblPr getTblPr(XWPFTable table, boolean force) {
        return (table.getCTTbl().getTblPr() != null) ? table.getCTTbl().getTblPr()
                : (force ? table.getCTTbl().addNewTblPr() : null);
    }
    
    
    public void removeInsideHBorder(XWPFTable tab) {
        removeBorder(tab, Border.INSIDE_H);
    }
 
    public void removeInsideVBorder(XWPFTable tab) {
        removeBorder(tab, Border.INSIDE_V);
    }
 
    public void removeTopBorder(XWPFTable tab) {
        removeBorder(tab, Border.TOP);
    }
 
    public void removeBottomBorder(XWPFTable tab) {
        removeBorder(tab, Border.BOTTOM);
    }
 
    public void removeLeftBorder(XWPFTable tab) {
        removeBorder(tab, Border.LEFT);
    }
 
    public void removeRightBorder(XWPFTable tab) {
        removeBorder(tab, Border.RIGHT);
    }
    
    private void removeBorder(XWPFTable tab, final Border border) {
        final Function<CTTblBorders,Boolean> isSet;
        final Consumer<CTTblBorders> unSet;
        switch (border) {
            case INSIDE_H:
                isSet = CTTblBorders::isSetInsideH;
                unSet = CTTblBorders::unsetInsideH;
                break;
            case INSIDE_V:
                isSet = CTTblBorders::isSetInsideV;
                unSet = CTTblBorders::unsetInsideV;
                break;
            case LEFT:
                isSet = CTTblBorders::isSetLeft;
                unSet = CTTblBorders::unsetLeft;
                break;
            case TOP:
                isSet = CTTblBorders::isSetTop;
                unSet = CTTblBorders::unsetTop;
                break;
            case RIGHT:
                isSet = CTTblBorders::isSetRight;
                unSet = CTTblBorders::unsetRight;
                break;
            case BOTTOM:
                isSet = CTTblBorders::isSetBottom;
                unSet = CTTblBorders::unsetBottom;
                break;
            default:
                return;
        }
 
        final CTTblBorders tbl = getTblBorders(tab, false);
        if (tbl != null && isSet.apply(tbl)) {
            unSet.accept(tbl);
            cleanupTblBorders(tab);
        }
 
    }
    
    private CTTblBorders getTblBorders(XWPFTable tab, boolean force) {
        CTTblPr tblPr = getTblPr(tab, force);
        return tblPr == null ? null
               : tblPr.isSetTblBorders() ? tblPr.getTblBorders()
               : force ? tblPr.addNewTblBorders()
               : null;
    }
    
    private void cleanupTblBorders(XWPFTable table) {
        final CTTblPr pr = getTblPr(table, false);
        if (pr != null && pr.isSetTblBorders()) {
            final CTTblBorders b = pr.getTblBorders();
            if (!(b.isSetInsideH() ||
                b.isSetInsideV() ||
                b.isSetTop() ||
                b.isSetBottom() ||
                b.isSetLeft() ||
                b.isSetRight())) {
                pr.unsetTblBorders();
            }
        }
    }
    
    private void mergeCellsHorizontal(XWPFTable table, int row, int startCol,int endCol) {
		for (int cellIndex = startCol; cellIndex <= endCol; cellIndex++) {
			XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
			if (cellIndex == startCol) {
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}
    
    private void mergeCellsVertically(XWPFTable table, int col, int fromRow,int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			if (rowIndex == fromRow) {
				cell.getCTTc().addNewTcPr().addNewVMerge()
						.setVal(STMerge.RESTART);
			} else {
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}	
    
    public List<BigDecimal> packPromedios(BigDecimal promedio){
        List<BigDecimal> promedios = new ArrayList<>();
        promedios.add(promedio);
        return promedios;
    }
    
    public BigDecimal sumaPromedios(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        BigDecimal promedio = BigDecimal.ZERO;
        if(estudiante.getCalificacionList().isEmpty()){
            promedio = controlador.getPromedioFinalAlineacion(dtoCargaAcademica, estudiante);
        }
        if(estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty()){
            promedio = controlador.getPromedioFinal(dtoCargaAcademica, estudiante);
        }
        return promedio;
    }
    
    static void setTableCellBorder(XWPFTableCell cell, Border border, STBorder.Enum type, Integer size) {
        CTTc tc = cell.getCTTc();
        CTTcPr tcPr = tc.getTcPr();
        if (tcPr == null) {
            tcPr = tc.addNewTcPr();
        }
        CTTcBorders tcBorders = tcPr.getTcBorders();
        if (tcBorders == null) {
            tcBorders = tcPr.addNewTcBorders();
        }
        if (border == Border.LEFT) {
            CTBorder left = tcBorders.getLeft();
            if (left == null) {
                left = tcBorders.addNewLeft();
            }
            left.setVal(type);
            left.setSz(BigInteger.valueOf(DxaUtils.dxa2points(size)));
        } else if (border == Border.TOP) {
            CTBorder top = tcBorders.getTop();
            if (top == null) {
                top = tcBorders.addNewTop();

            }
            top.setVal(type);
            top.setSz(BigInteger.valueOf(DxaUtils.dxa2points(size)));
        } else if (border == Border.BOTTOM) {
            CTBorder bottom = tcBorders.getBottom();
            if (bottom == null) {
                bottom = tcBorders.addNewBottom();
            }
            bottom.setVal(type);
            bottom.setSz(BigInteger.valueOf(DxaUtils.dxa2points(size)));
        } else if (border == Border.RIGHT) {
            CTBorder right = tcBorders.getRight();
            if (right == null) {
                right = tcBorders.addNewRight();
            }
            right.setVal(type);
            right.setSz(BigInteger.valueOf(DxaUtils.dxa2points(size)));
        }
    }
    
    public BigDecimal obtenerPromedio(DtoCargaAcademica carga, Estudiante estudiante){
        BigDecimal promedioFinalM = BigDecimal.ZERO;
        if(estudiante.getCalificacionList().isEmpty()){
            promedioFinalM = controlador.getPromedioFinalAlineacion(carga, estudiante);

        }
        if (estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty()) {
            promedioFinalM = controlador.getPromedioFinal(carga, estudiante);
        }
            return promedioFinalM;
    }
    
    public String obtenerEscala(DtoCargaAcademica carga, Estudiante estudiante){
        BigDecimal promedioFinalM;
        String escala = "";
        if(estudiante.getCalificacionList().isEmpty()){
                promedioFinalM = controlador.getPromedioFinalAlineacion(carga, estudiante);
                if (promedioFinalM.compareTo(BigDecimal.valueOf(0)) == 0 || promedioFinalM.compareTo(BigDecimal.valueOf(8)) < 0) {
                    escala = "NA";
                }
                if (promedioFinalM.compareTo(BigDecimal.valueOf(10)) == 0) {
                    if(carga.getMateria().getUnidadMateriaList().get(0).getIntegradora()){
                        escala = "CA";
                    }else{
                        escala = "AU";
                    }

                }
                if ((promedioFinalM.compareTo(BigDecimal.valueOf(9)) >= 0) && (promedioFinalM.compareTo(BigDecimal.valueOf(10)) < 0)) {
                    if(carga.getMateria().getUnidadMateriaList().get(0).getIntegradora()){
                        escala = "CD";
                    }else{
                        escala = "DE";
                    }
                }
                if (promedioFinalM.compareTo(BigDecimal.valueOf(8)) >= 0 && promedioFinalM.compareTo(BigDecimal.valueOf(9)) < 0) {
                    if(carga.getMateria().getUnidadMateriaList().get(0).getIntegradora()){
                        escala = "CO";
                    }else{
                        escala = "SA";
                    }
                }
            }
            if(estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty()){
                promedioFinalM = controlador.getPromedioFinal(carga, estudiante);
                if (promedioFinalM.compareTo(BigDecimal.valueOf(0)) == 0 || promedioFinalM.compareTo(BigDecimal.valueOf(8)) < 0) {
                    escala = "NA";
                }
                if (promedioFinalM.compareTo(BigDecimal.valueOf(10)) == 0) {
                    if(carga.getMateria().getUnidadMateriaList().get(0).getIntegradora()){
                        escala = "CA";
                    }else{
                        escala = "AU";
                    }

                }
                if ((promedioFinalM.compareTo(BigDecimal.valueOf(9)) >= 0) && (promedioFinalM.compareTo(BigDecimal.valueOf(10)) < 0)) {
                    if(carga.getMateria().getUnidadMateriaList().get(0).getIntegradora()){
                        escala = "CD";
                    }else{
                        escala = "DE";
                    }
                }
                if (promedioFinalM.compareTo(BigDecimal.valueOf(8)) >= 0 && promedioFinalM.compareTo(BigDecimal.valueOf(9)) < 0) {
                    if(carga.getMateria().getUnidadMateriaList().get(0).getIntegradora()){
                        escala = "CO";
                    }else{
                        escala = "SA";
                    }
                }
            }
            return escala;
    }
    
    public void agregarInformacionFila(XWPFTableRow row,DtoCargaAcademica carga, BigDecimal promedio, String escala, Integer hrsPracticas, Integer hrsTeoricas){
        int twipsPerInch = 1900;
        row.setHeight((int) (twipsPerInch * 1 / 10)); //set height 1/10 inch.
        row.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        row.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        row.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        row.getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
        row.getCell(3).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);

        setTableCellBorder(row.getCell(0), Border.TOP, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(0), Border.BOTTOM, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(0), Border.RIGHT, STBorder.SINGLE, 0);
        XWPFParagraph pMateria = row.getCell(0).getParagraphs().get(0);
        pMateria.setSpacingAfter(0);
        pMateria.setSpacingBefore(0);
        pMateria.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun rMateria = pMateria.insertNewRun(0);
        rMateria.setFontSize(8);
        rMateria.setFontFamily("AvantGarde Bk BT");
        rMateria.setText(carga.getCargaAcademica().getIdPlanMateria().getIdMateria().getNombre());

        setTableCellBorder(row.getCell(1), Border.TOP, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(1), Border.BOTTOM, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(1), Border.RIGHT, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(1), Border.LEFT, STBorder.SINGLE, 0);
        XWPFParagraph pHrs = row.getCell(1).getParagraphs().get(0);
        pHrs.setSpacingAfter(0);
        pHrs.setSpacingBefore(0);
        pHrs.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rHrs = pHrs.insertNewRun(0);
        rHrs.setFontSize(8);
        rHrs.setFontFamily("AvantGarde Bk BT");
        rHrs.setText(String.valueOf(hrsPracticas + hrsTeoricas));


        setTableCellBorder(row.getCell(2), Border.TOP, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(2), Border.BOTTOM, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(2), Border.RIGHT, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(2), Border.LEFT, STBorder.SINGLE, 0);
        XWPFParagraph pCal = row.getCell(2).getParagraphs().get(0);
        pCal.setSpacingAfter(0);
        pCal.setSpacingBefore(0);
        pCal.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rCal = pCal.insertNewRun(0);
        rCal.setFontSize(8);
        rCal.setFontFamily("AvantGarde Bk BT");
        rCal.setText(promedio.toString());

        setTableCellBorder(row.getCell(3), Border.TOP, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(3), Border.BOTTOM, STBorder.SINGLE, 0);
        setTableCellBorder(row.getCell(3), Border.LEFT, STBorder.SINGLE, 0);
        XWPFParagraph pLetr = row.getCell(3).getParagraphs().get(0);
        pLetr.setSpacingAfter(0);
        pLetr.setSpacingBefore(0);
        pLetr.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rLetr = pLetr.insertNewRun(0);
        rLetr.setFontSize(8);
        rLetr.setFontFamily("AvantGarde Bk BT");
        rLetr.setText(escala);
    }
    
    public void agregarInformacion(XWPFTableRow fila, String texto1,  String texto2,  String texto3,  String texto4,  String texto5,  String texto6,  String texto7,  String texto8){
        int twipsPerInch = 2100;
        fila.setHeight((int) (twipsPerInch * 1 / 10)); //set height 1/10 inch.
        fila.getCtRow().getTrPr().getTrHeightArray(0).setHRule(STHeightRule.EXACT); //set w:hRule="exact"
        
        fila.getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(1).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(3).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(4).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(5).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(6).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        fila.getCell(7).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        XWPFParagraph pF01 = fila.getCell(0).getParagraphs().get(0);
        pF01.setSpacingAfter(0); pF01.setSpacingBefore(0); pF01.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun rF01 = pF01.insertNewRun(0); rF01.setFontSize(5); rF01.setBold(true); rF01.setFontFamily("AvantGarde Bk BT");
        rF01.setText(texto1);
        
        XWPFParagraph pF02 = fila.getCell(1).getParagraphs().get(0);
        pF02.setSpacingAfter(0); pF02.setSpacingBefore(0); pF02.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun rF02 = pF02.insertNewRun(0); rF02.setFontSize(5); rF02.setBold(true); rF02.setFontFamily("AvantGarde Bk BT");
        rF02.setText(texto2);
        
        XWPFParagraph pF03 = fila.getCell(2).getParagraphs().get(0);
        pF03.setSpacingAfter(0); pF03.setSpacingBefore(0); pF03.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rF03 = pF03.insertNewRun(0); rF03.setFontSize(5); rF03.setBold(true); rF03.setFontFamily("AvantGarde Bk BT");
        rF03.setText(texto3);
        
        XWPFParagraph pF04 = fila.getCell(3).getParagraphs().get(0);
        pF04.setSpacingAfter(0); pF04.setSpacingBefore(0); pF04.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun rF04 = pF04.insertNewRun(0); rF04.setFontSize(5); rF04.setBold(true); rF04.setFontFamily("AvantGarde Bk BT");
        rF04.setText(texto4);
        
        XWPFParagraph pF05 = fila.getCell(4).getParagraphs().get(0);
        pF05.setSpacingAfter(0); pF05.setSpacingBefore(0); pF05.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rF05 = pF05.insertNewRun(0); rF05.setFontSize(5); rF05.setBold(true); rF05.setFontFamily("AvantGarde Bk BT");
        rF05.setText(texto5);
        
        XWPFParagraph pF06 = fila.getCell(5).getParagraphs().get(0);
        pF06.setSpacingAfter(0); pF06.setSpacingBefore(0); pF06.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun rF06 = pF06.insertNewRun(0); rF06.setFontSize(5); rF06.setBold(true); rF06.setFontFamily("AvantGarde Bk BT");
        rF06.setText(texto6);
        
        XWPFParagraph pF07 = fila.getCell(6).getParagraphs().get(0);
        pF07.setSpacingAfter(0); pF07.setSpacingBefore(0); pF07.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rF07 = pF07.insertNewRun(0); rF07.setFontSize(5); rF07.setBold(true); rF07.setFontFamily("AvantGarde Bk BT");
        rF07.setText(texto7);
        
        XWPFParagraph pF08 = fila.getCell(7).getParagraphs().get(0);
        pF08.setSpacingAfter(0); pF08.setSpacingBefore(0); pF08.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun rF08 = pF08.insertNewRun(0); rF08.setFontSize(5); rF08.setBold(true); rF08.setFontFamily("AvantGarde Bk BT");
        rF08.setText(texto8);
    }
    
    public String convertirNumero(Integer numero){
        String letra = "";
        switch(numero){
            case 1:
                letra = "uno";
                break;
            case 2:
                letra = "dos";
                break;
            case 3:
                letra = "tres";
                break;
            case 4:
                letra = "cuatro";
                break;
            case 5:
                letra = "cinco";
                break;
            case 6:
                letra = "seis";
                break;
            case 7:
                letra = "siete";
                break;
            case 8:
                letra = "ocho";
                break;
            case 9:
                letra = "nueve";
                break;
        }
        return letra;
    }
    
    public void agregarParrafosVacios(Integer numero, Integer fontSize, Double interlineado){
        for(int i = 1; i <= numero; i++){
            agregarTextoParrafo(0,0,ParagraphAlignment.CENTER, 0, "AvantGarde Bk BT", "", Boolean.TRUE, fontSize, UnderlinePatterns.NONE, Boolean.FALSE, interlineado);
        }
    }
    
    public void agregarTextoCelda(XWPFTable tabla, XWPFTableCell.XWPFVertAlign alingVert,Integer fila, Integer celda, Integer espacioA, Integer espacioD, ParagraphAlignment aligment, Integer numRun, String fontFamily, String texto, Boolean negritas, Integer fontSize, UnderlinePatterns underline, Boolean italic, Boolean romperLinea, Double interlineado){
        tabla.getRow(fila).getCell(celda).setVerticalAlignment(alingVert);
        XWPFParagraph parrafo = tabla.getRow(fila).getCell(celda).getParagraphs().get(0);
        parrafo.setSpacingBefore(espacioA);
        parrafo.setSpacingAfter(espacioD);
        parrafo.setAlignment(aligment);
        parrafo.setSpacingLineRule(LineSpacingRule.EXACT); 
        parrafo.setSpacingBetween(interlineado);
        XWPFRun run = parrafo.insertNewRun(numRun);
        run.setFontSize(fontSize);
        run.setFontFamily(fontFamily);
        run.setBold(negritas);
        run.setUnderline(underline);
        run.setItalic(italic);
        run.setText(texto);
        if(romperLinea.equals(Boolean.TRUE)){
            run.addBreak();
        }
    }
    
    public void agregarTextoParrafo(Integer espacioA, Integer espacioD, ParagraphAlignment aligment, Integer numRun, String fontFamily, String texto, Boolean negritas, Integer fontSize, UnderlinePatterns underline, Boolean italic, Double intelineado){
        XWPFParagraph parrafo = word.createParagraph();
        parrafo.setSpacingBefore(espacioA);
        parrafo.setSpacingAfter(espacioD);
        parrafo.setAlignment(aligment);
        parrafo.setSpacingLineRule(LineSpacingRule.EXACT); 
        parrafo.setSpacingBetween(intelineado);
        XWPFRun run = parrafo.insertNewRun(numRun);
        run.setFontSize(fontSize);
        run.setFontFamily(fontFamily);
        run.setBold(negritas);
        run.setUnderline(underline);
        run.setItalic(italic);
        run.setText(texto);
    }
    
    public String agregarTextoPorGenero(DtoEstudiante estudiante){
        String texto = "";
        if(estudiante.getPersona().getGenero() == Short.parseShort("1")){
            texto = "la";
        }else{
            texto = "el";
        }
        return texto;
    }

    public void setStyleCell(XWPFTableCell celda, Boolean top, Boolean bottom, Boolean right, Boolean left, STBorder.Enum tipo){
        if(top.equals(Boolean.TRUE)){
                setTableCellBorder(celda, Border.TOP, tipo, 0);
            }
        if(bottom.equals(Boolean.TRUE)){
                setTableCellBorder(celda, Border.BOTTOM, tipo, 0);
            }
        if(left.equals(Boolean.TRUE)){
                setTableCellBorder(celda, Border.LEFT, tipo, 0);
            }
        if(right.equals(Boolean.TRUE)){
                setTableCellBorder(celda, Border.RIGHT, tipo, 0);
            }
    }

    public void setStyleTable(XWPFTable table, Integer numRow, Integer numCelda, STBorder.Enum tipo){
        for (int r = 0; r < numRow; r++){
            XWPFTableRow row = table.getRow(r);
            for (int c = 0; c < numCelda; c++){
                XWPFTableCell cell = row.getCell(c);
                setStyleCell(cell, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, tipo);
            }
        }
    }
}
