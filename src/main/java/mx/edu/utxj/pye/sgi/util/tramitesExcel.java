/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class tramitesExcel implements Serializable{

    private static final long serialVersionUID = -7233060041213879724L;
    @Getter @Setter private XSSFWorkbook libro;
    @Getter @Setter private String nombreExcel, salida, base, hoja, cvePersona, nombreCompleto, superior;
    @Getter @Setter private Integer index;
    @Getter @Setter private Date fecha = new Date();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat df = new DecimalFormat("#.#");
    private final DecimalFormat df2 = new DecimalFormat("#.##");
    InputStream in;
    OutputStream out;
    
    
    public void generarExcel() throws IOException{
        fecha = new Date();
        obtenerLibro();
        editarHojaAviso();
        editarHojaInforme();
        editarHojaGastoAnticipado();
        editarHojaDeRemision();
        editarHojaTramitePago();
        editarHojaReciboEconomico();
        escribirLibro();
        String ruta = enviarLibro();
        Ajax.oncomplete("descargar('" + enviarLibro() + "');");
    }

    //////////////////////Metodo que se utiliza para obtener el la ruta del libro a editar\\\\\\\\\\\\\\\\\\\
    public void obtenerLibro(){
        nombreExcel = "tramites.xlsx";
        try {
            in = new FileInputStream(ServicioArchivos.carpetaRaiz.concat(File.separator).concat(nombreExcel));
            libro = new XSSFWorkbook(in);
            System.out.println("Se obtuvo el libro:"+ libro);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(tramitesExcel.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(tramitesExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////
    public void editarLibro(){
        int index1 = 0;
        libro.getAllNames().stream().collect(Collectors.toList()).forEach(x -> {
            hoja = x.getSheetName();
            index = x.getSheetIndex();
            System.out.println("Libro:"+ x.getSheetName()+ "-Numero:"+ index);
        });

        if(hoja.equals("Aviso de Comisión")){

        }
        if(hoja.equals("Informe de Comisión")){

        }
        if(hoja.equals("Gasto Anticipado")){

        }
        if(hoja.equals("Remitir comprobación")){

        }
        if(hoja.equals("Trámite de pago")){

        }
    }

    ////////////////////Metodo que permite crear el libro y guardarlo en una ruta relativa dentro del disco local.\\\\\\\\\\\\\\\
    public void escribirLibro() {
        try {
            base = ServicioArchivos.carpetaRaiz
                    .concat("tramites_pago").concat(File.separator)
                    .concat("579").concat(File.separator);

            ServicioArchivos.addCarpetaRelativa(base);
            salida = nombreExcel;

            ServicioArchivos.eliminarArchivo(base.concat(salida));

            out = new FileOutputStream(new File(base.concat(salida)));
            libro.write(out);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ///////////////////Metodo que enviar los archivos y descargarlos\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public String enviarLibro() {
        System.err.println("jvv.aldesa.sgot.util.ExcelWritter.enviarLibro() ruta: " + base.concat(salida));
        File file = new File(base.concat(salida));
        String ruta = "/sii/media".concat(file.toURI().toString().split("archivos")[1]);
        System.err.println("jvv.aldesa.sgot.util.ExcelWritter.enviarLibro() ruta 2: " + ruta);
        return ruta;
//        return new File(base.concat(salida));
    }

    //////////////////Metodo que permite modificar la primer hoja del libro obtenido
    public void editarHojaAviso(){
        XSSFSheet hojaAvisoComision = libro.getSheetAt(0);
        escribirCelda(hojaAvisoComision.getRow(11), 14, sdf.format(new Date()));
        escribirCelda(hojaAvisoComision.getRow(14), 3 , "Marcelino López Miguel");
        escribirCelda(hojaAvisoComision.getRow(14), 14, "579");
        escribirCelda(hojaAvisoComision.getRow(15), 5 , "DIRECCIÓN DE PLANEACIÓN Y EVALUACIÓN");
        escribirCelda(hojaAvisoComision.getRow(16), 3 , "Técnico/a Laboratorista");
        escribirCelda(hojaAvisoComision.getRow(16), 9 , "(BANCOMER)");
        escribirCelda(hojaAvisoComision.getRow(16), 13, "1234567891012345");
        escribirCelda(hojaAvisoComision.getRow(17), 10, "marcelino.lopez@utxicotepec.edu.mx");
        escribirCelda(hojaAvisoComision.getRow(27), 7 , "UTXJ-DSW/0001/18");
        escribirCelda(hojaAvisoComision.getRow(28), 5 , "Universidad Tecnológica de Xicotepec de Juárez, Xicotepec de Juárez, Puebla, Mexico");
        escribirCelda(hojaAvisoComision.getRow(30), 5 , "20/03/2019"+" al "+"21/03/2019");
        escribirCelda(hojaAvisoComision.getRow(32), 7 , 170.00);
        escribirCelda(hojaAvisoComision.getRow(32), 14, "I");
        escribirCelda(hojaAvisoComision.getRow(33), 7 , 200.00);
        escribirCelda(hojaAvisoComision.getRow(33), 14, "II");
        escribirCelda(hojaAvisoComision.getRow(35), 4 , "Accord");
        escribirCelda(hojaAvisoComision.getRow(35), 9 , "");
        escribirCelda(hojaAvisoComision.getRow(35), 14, 8);
        escribirCelda(hojaAvisoComision.getRow(37), 14, "PRODEP");
        escribirCelda(hojaAvisoComision.getRow(39), 9 , 100.00);
        escribirCelda(hojaAvisoComision.getRow(39), 9 , 0.00);
        escribirCelda(hojaAvisoComision.getRow(39), 9 , 400.00);
        escribirCelda(hojaAvisoComision.getRow(39), 9 , 70.00);
        escribirCelda(hojaAvisoComision.getRow(39), 9 , 50.00);
        escribirCelda(hojaAvisoComision.getRow(51), 3 , "LIC. MARICARMEN TOLENTINO CASTILLO");
        escribirCelda(hojaAvisoComision.getRow(52), 3 , "DIRECTORA DE PLANEACION Y EVALUACION");
        escribirCelda(hojaAvisoComision.getRow(51), 9 , "T.S.U MARCELINO LOPEZ MIGUEL");
        escribirCelda(hojaAvisoComision.getRow(52), 9 , "TECNICO LABORATORISTA");
    }

    //////////////////Metodo que permite modificar la segunda hoja del libro obtenido
    public void editarHojaInforme(){
        XSSFSheet hojaInformeComision = libro.getSheetAt(1);
        escribirCelda(hojaInformeComision.getRow(11), 14, sdf.format(new Date()));
        escribirCelda(hojaInformeComision.getRow(14), 3 , "Marcelino López Miguel");
        escribirCelda(hojaInformeComision.getRow(14), 14, "579");
        escribirCelda(hojaInformeComision.getRow(15), 5 , "DIRECCIÓN DE PLANEACIÓN Y EVALUACIÓN");
        escribirCelda(hojaInformeComision.getRow(16), 3 , "Técnico/a Laboratorista");
        escribirCelda(hojaInformeComision.getRow(19), 7 , "UTXJ-DSW/0001/18" );
        escribirCelda(hojaInformeComision.getRow(21), 5 , "Universidad Tecnológica de Xicotepec de Juárez, Xicotepec de Juárez, Puebla, Mexico");
        escribirCelda(hojaInformeComision.getRow(23), 5 , "20/03/2019"+" al "+"21/03/2019");
        escribirCelda(hojaInformeComision.getRow(24), 5 , "ASUNTO");
        escribirCelda(hojaInformeComision.getRow(27), 4 , "Accord");
        escribirCelda(hojaInformeComision.getRow(27), 9 , "NINGUNO");
        escribirCelda(hojaInformeComision.getRow(27), 14, 8);
        escribirCelda(hojaInformeComision.getRow(31), 2 , "CONCLUSION");
        escribirCelda(hojaInformeComision.getRow(47), 3 , "LIC. MARICARMEN TOLENTINO CASTILLO");
        escribirCelda(hojaInformeComision.getRow(48), 3 , "DIRECTORA DE PLANEACION Y EVALUACION");
        escribirCelda(hojaInformeComision.getRow(47), 9 , "T.S.U MARCELINO LOPEZ MIGUEL");
        escribirCelda(hojaInformeComision.getRow(48), 9 , "TECNICO LABORATORISTA");
    }

    //////////////////Metodo que permite modificar la tercer hoja del libro obtenido
    public void editarHojaGastoAnticipado(){
        XSSFSheet hojaGastoAnticipado = libro.getSheetAt(2);
        escribirCelda(hojaGastoAnticipado.getRow(11), 14, sdf.format(new Date()));
        escribirCelda(hojaGastoAnticipado.getRow(11), 14, sdf.format(new Date()));
        escribirCelda(hojaGastoAnticipado.getRow(14), 3 , "Marcelino López Miguel");
        escribirCelda(hojaGastoAnticipado.getRow(14), 14, "579");
        escribirCelda(hojaGastoAnticipado.getRow(15), 5 , "DIRECCIÓN DE PLANEACIÓN Y EVALUACIÓN");
        escribirCelda(hojaGastoAnticipado.getRow(16), 3 , "Técnico/a Laboratorista");
        escribirCelda(hojaGastoAnticipado.getRow(16), 9 , "(BANCOMER)");
        escribirCelda(hojaGastoAnticipado.getRow(16), 13, "1234567891012345");
        escribirCelda(hojaGastoAnticipado.getRow(17), 10, "marcelino.lopez@utxicotepec.edu.mx");
        escribirCelda(hojaGastoAnticipado.getRow(20), 2 , "POR CONCEPTO DE...");
        escribirCelda(hojaGastoAnticipado.getRow(37), 3 , 240.00);
        escribirCelda(hojaGastoAnticipado.getRow(37), 8 , "Docientos cuarenta");
        escribirCelda(hojaGastoAnticipado.getRow(54), 2 , "MARCELINO LÓPEZ MIGUEL");
        escribirCelda(hojaGastoAnticipado.getRow(55), 2 , "TECNICO LABORATORISTA");
        escribirCelda(hojaGastoAnticipado.getRow(54), 7 , "MARICARMEN TOLENTINO CASTILLO");
        escribirCelda(hojaGastoAnticipado.getRow(55), 7 , "DIRECTORA DE PLANEACIÓN Y EVALUACIÓN");
    }

    //////////////////Metodo que permite modificar la cuarta hoja del libro obtenido
    public void editarHojaDeRemision(){
        XSSFSheet hojaRemision = libro.getSheetAt(3);
        escribirCelda(hojaRemision.getRow(11), 11, sdf.format(new Date()));
        escribirCelda(hojaRemision.getRow(15), 2, "Se anexa documentación comprobatoria");
        escribirCelda(hojaRemision.getRow(18), 2, "Por concepto de...");
        escribirCelda(hojaRemision.getRow(21), 2, "Dirección de Planeación y Evaluación");
        escribirCelda(hojaRemision.getRow(25), 5, sdf.format(new Date()));
        escribirCelda(hojaRemision.getRow(25), 10, 450.00);
        escribirCelda(hojaRemision.getRow(30), 4, "Asegurar la calidad académica");
        escribirCelda(hojaRemision.getRow(31), 4, "Consolidar el Programa de Capacitación y Formación del Capital Humano");
        escribirCelda(hojaRemision.getRow(32), 4, "Ampliar y mejorar los programas de becas institucionales");
        escribirCelda(hojaRemision.getRow(33), 4, "Actualización del sitio web de la universidad.");
        escribirCelda(hojaRemision.getRow(58), 4, "MARCELINO LÓPEZ MIGUEL");
        escribirCelda(hojaRemision.getRow(59), 4, "TECNICO LABORATORISTA");
    }

    //////////////////Metodo que permite modificar la quinta hoja del libro obtenido
    public void editarHojaTramitePago(){
        XSSFSheet hojaTramite = libro.getSheetAt(4);
        escribirCelda(hojaTramite.getRow(11), 11, sdf.format(new Date()));
        escribirCelda(hojaTramite.getRow(16), 5 , "DIRECCIÓN DE PLANEACIÓN Y EVALUACIÓN");
        escribirCelda(hojaTramite.getRow(18), 5 , "COMISIÓN");
        escribirCelda(hojaTramite.getRow(20), 5 , 450.00);
        escribirCelda(hojaTramite.getRow(22), 5 , "MARCELINO LOPEZ MIGUEL");
        escribirCelda(hojaTramite.getRow(25), 9 , "1234567891234567");
        escribirCelda(hojaTramite.getRow(26), 9 , "123");
        escribirCelda(hojaTramite.getRow(28), 9 , "marcelino.lopez@utxicotepec.edu.mx");
        escribirCelda(hojaTramite.getRow(32), 5 , sdf.format(new Date()));
        escribirCelda(hojaTramite.getRow(32), 10, 450.00);
        escribirCelda(hojaTramite.getRow(37), 4, "Asegurar la calidad académica");
        escribirCelda(hojaTramite.getRow(38), 4, "Consolidar el Programa de Capacitación y Formación del Capital Humano");
        escribirCelda(hojaTramite.getRow(39), 4, "Ampliar y mejorar los programas de becas institucionales");
        escribirCelda(hojaTramite.getRow(40), 4, "Actualización del sitio web de la universidad.");
        escribirCelda(hojaTramite.getRow(62), 4, "MARCELINO LÓPEZ MIGUEL");
        escribirCelda(hojaTramite.getRow(63), 4, "TECNICO LABORATORISTA");
    }

    //////////////////Metodo que permite modificar la sexta hoja del libro obtenido
    public void editarHojaReciboEconomico(){
        XSSFSheet hojaReciboEconomico = libro.getSheetAt(5);
        escribirCelda(hojaReciboEconomico.getRow(10), 14, "PYE-001");
        escribirCelda(hojaReciboEconomico.getRow(13), 4 , 450.00);
        escribirCelda(hojaReciboEconomico.getRow(13), 14, sdf.format(new Date()));
        escribirCelda(hojaReciboEconomico.getRow(17), 2 , 450.00);
        escribirCelda(hojaReciboEconomico.getRow(17), 4 , "Cuatrocientos cincuenta");
        escribirCelda(hojaReciboEconomico.getRow(20), 2 , "Por concepto de...");
        escribirCelda(hojaReciboEconomico.getRow(51), 2 , "MARCELINO LÓPEZ MIGUEL");
        escribirCelda(hojaReciboEconomico.getRow(52), 2 , "TÉCNICO LABORATORISTA");
    }



    private Double redondear(Double valor){
        if(valor != null){
            return Double.valueOf(df2.format(valor));
        }else{
            return 0D;
        }
    }
    
    private void crearFila(XSSFSheet hoja, Integer index){
        if(hoja.getRow(index) == null){
            hoja.createRow(index);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, String valor) {
        if (valor != null) {
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.STRING);
            }
            fila.getCell(indice).setCellType(CellType.STRING);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, Short valor) {
        if (valor != null) {
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.NUMERIC);
            }
            fila.getCell(indice).setCellType(CellType.NUMERIC);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    @SneakyThrows(NullPointerException.class)
    private void escribirCelda(XSSFRow fila, Integer indice, Double valor) {
        if (valor != null) {
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.NUMERIC);
            }
            fila.getCell(indice).setCellType(CellType.NUMERIC);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, Integer valor) {
        if (valor != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.escribirCelda(" + indice + ") valor: " + valor);
//            System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.escribirCelda(" + indice + ") fila: " + fila);
//            System.out.println("mx.edu.utxj.pye.sgi.util.ExcelWritter.escribirCelda(" + indice + ") celda: " + fila.getCell(indice));
            XSSFCell celda = fila.getCell(indice);
            if (celda == null) {
                fila.createCell(indice, CellType.NUMERIC);
            }
            fila.getCell(indice).setCellType(CellType.NUMERIC);
            fila.getCell(indice).setCellValue(valor);
        }
    }

    private void escribirCelda(XSSFRow fila, Integer indice, Date valor) {
        escribirCelda(fila, indice, sdf.format(valor));
    }
    

}
