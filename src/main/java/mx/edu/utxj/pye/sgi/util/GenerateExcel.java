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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class GenerateExcel implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private XSSFWorkbook libro;
    @Getter @Setter private XWPFDocument word;
    @Getter @Setter private String nombreExcel, nombreWord, salida, base, hoja;
    @Getter @Setter private DecimalFormat df = new DecimalFormat("0.00");
    @EJB EjbSeguimientoTestDiagnosticoAprendizaje ejbSTDA;
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

    public void eliminarLibros(String nombreExcel){
        for(int i=libro.getNumberOfSheets()-1;i>=0;i--){
            XSSFSheet tmpSheet =libro.getSheetAt(i);
            if(!tmpSheet.getSheetName().equals(nombreExcel)){
                libro.removeSheetAt(i);
            }
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
    
}
